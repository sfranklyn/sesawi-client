/*
 * Copyright 2014 Samuel Franklyn <sfranklyn@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sesawi.client.view;

//import com.github.sarxos.webcam.Webcam;
//import com.github.sarxos.webcam.WebcamPanel;
//import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;
//import java.io.File;
//import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
//import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import net.java.dev.designgridlayout.DesignGridLayout;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.printing.PDFPageable;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import sesawi.client.service.AppServiceBean;
import sesawi.client.service.ComputersServiceBean;
import sesawi.client.service.PricesServiceBean;
import sesawi.client.service.TicketsServiceBean;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Singleton
public class EntryDoorForm extends BaseForm {

    private static final long serialVersionUID = -7800506383045437821L;
    private static final Logger log = Logger.getLogger(EntryDoorForm.class.getName());

    private final DateTimeFormatter dtfNow = DateTimeFormat.
            forPattern("dd MMM yyyy HH:mm:ss");
    private final DateTimeFormatter dtfDate = DateTimeFormat.
            forPattern("dd MMM yyyy");
    private final DateTimeFormatter dtfTime = DateTimeFormat.
            forPattern("HH : mm : ss");
    private final DateTimeFormatter dtfTicket = DateTimeFormat.
            forPattern("ddMMMyyHHmmss");

    @Inject
    private AppServiceBean appServiceBean;
    @Inject
    private TicketsServiceBean ticketsServiceBean;
    @Inject
    private ComputersServiceBean computersServiceBean;
    @Inject
    private PricesServiceBean pricesServiceBean;

    private JLabel labelUserName;
    private JLabel labelComputerName;
    private JLabel labelDate;
    private JLabel labelPrices;
    private List<Map> pricesListMap;
    private JComboBox comboPrices;
    private JLabel labelTicketPoliceNo;
    private JTextField fieldTicketPoliceNo;
    private JButton buttonPrint;
    //private Webcam webcam;
    //private WebcamPanel webcamPanel;
    private String locationName;
    private String locationDesc;

    public JLabel getLabelDate() {
        return labelDate;
    }

    public void setLabelDate(JLabel labelDate) {
        this.labelDate = labelDate;
    }

    public AppServiceBean getAppServiceBean() {
        return appServiceBean;
    }

    public void setAppServiceBean(AppServiceBean appServiceBean) {
        this.appServiceBean = appServiceBean;
    }

    public String getNowStr() {
        return dtfNow.print(DateTime.now());
    }

    @PostConstruct
    public void init() {
        setTitle("Entry Door");

        JPanel panel = new JPanel();
        DesignGridLayout layout = new DesignGridLayout(panel);

        labelUserName = new JLabel(appServiceBean.getUserName());
        layout.row().grid(new JLabel("User Name:")).add(labelUserName);

        labelComputerName = new JLabel(appServiceBean.getComputerName());
        layout.row().grid(new JLabel("Computer Name:")).add(labelComputerName);

        labelDate = new JLabel(getNowStr());
        layout.row().grid(new JLabel("Date:")).add(labelDate);

        Timer timer = new Timer(1000, (ActionEvent e) -> {
            labelDate.setText(getNowStr());
        });
        timer.start();

        Map locationMap = computersServiceBean.receiveLocation();
        locationName = (String) locationMap.get("locationName");
        locationDesc = (String) locationMap.get("locationDesc");

        //webcam = Webcam.getDefault();
        //webcam.setViewSize(WebcamResolution.QVGA.getSize());
        //webcamPanel = new WebcamPanel(webcam);
        //layout.row().center().add(webcamPanel);
        labelPrices = new JLabel("Prices:");
        pricesListMap = pricesServiceBean.receive();
        DefaultComboBoxModel comboBoxModelPrices = new DefaultComboBoxModel();
        for (Map pricesMap : pricesListMap) {
            comboBoxModelPrices.addElement(pricesMap.get("priceCode"));
        }
        comboPrices = new JComboBox(comboBoxModelPrices);
        layout.row().grid(labelPrices).add(comboPrices);

        labelTicketPoliceNo = new JLabel("Police No:");
        fieldTicketPoliceNo = new JTextField("");
        layout.row().grid(labelTicketPoliceNo).add(fieldTicketPoliceNo);

        buttonPrint = new JButton("Print");
        buttonPrint.setMnemonic(KeyEvent.VK_P);
        buttonPrint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonPrintAction();
            }
        });
        layout.row().center().add(buttonPrint);

        add(panel);
        rootPane.setDefaultButton(buttonPrint);
        pack();
        setLocationRelativeTo(null);
        fieldTicketPoliceNo.requestFocus();
    }

    public void buttonPrintAction() {
        DateTime now = DateTime.now();
        String ticketNo = dtfTicket.print(now).toUpperCase();

        Map ticketsMap = new HashMap();

        String ticketId = UUID.randomUUID().toString().replaceAll("-", "");
        ticketsMap.put("ticketId", ticketId);
        ticketsMap.put("ticketNo", ticketNo);
        ticketsMap.put("locationName", locationName);
        ticketsMap.put("computerName", appServiceBean.getComputerName());
        ticketsMap.put("userName", appServiceBean.getUserName());
        ticketsMap.put("ticketEntryTime", now.toDate());
        String ticketPoliceNo = fieldTicketPoliceNo.getText();
        ticketsMap.put("ticketPoliceNo", ticketPoliceNo);
        Map pricesMap = pricesListMap.get(comboPrices.getSelectedIndex());

        String priceCode = (String) pricesMap.get("priceCode");
        ticketsMap.put("priceCode", priceCode);

        String priceEntry = (String) pricesMap.get("priceEntry");
        ticketsMap.put("priceEntry", priceEntry);

        String priceEntryHour = (String) pricesMap.get("priceEntryHour");
        ticketsMap.put("priceEntryHour", priceEntryHour);

        String pricePerHour = (String) pricesMap.get("pricePerHour");
        ticketsMap.put("pricePerHour", pricePerHour);

        String priceLost = (String) pricesMap.get("priceLost");
        ticketsMap.put("priceLost", priceLost);

        List<String> messages = ticketsServiceBean.sendEntry(ticketsMap);
        String dspMessage = "";
        if (!messages.isEmpty()) {
            for (String message : messages) {
                dspMessage = dspMessage.concat("\n" + message);
            }
            JOptionPane.showMessageDialog(null, dspMessage);
            return;
        }
        fieldTicketPoliceNo.setText("");

        //BufferedImage image = webcam.getImage();
        //try {
        //    ImageIO.write(image, "JPG", new File(appServiceBean.getPrintDir()
        //            + ticketNo + "a.jpg"));
        //} catch (IOException ex) {
        //    log.severe(ex.toString());
        //}
        
        try (PDDocument document = new PDDocument()) {

            PDPage page = new PDPage();

            document.addPage(page);

            PDFont font = PDType1Font.COURIER_BOLD;

            float fontSize = 10f;
            float fontWidth = font.getStringWidth("W") / 1000 * fontSize;
            float fontHeight = font.getFontDescriptor().getFontBoundingBox().
                    getHeight() / 1000 * fontSize;
            float startY = page.getMediaBox().getHeight() - fontHeight;

            try (PDPageContentStream cs
                    = new PDPageContentStream(document, page)) {

                cs.beginText();
                cs.setFont(font, fontSize);
                cs.moveTextPositionByAmount(0, startY);
                cs.drawString("      PRO PARKING");
                cs.moveTextPositionByAmount(0, -fontHeight * 2);
                cs.drawString("    SELAMAT DATANG DI");
                cs.moveTextPositionByAmount(0, -fontHeight);
                cs.drawString("   " + locationDesc);
                cs.moveTextPositionByAmount(0, -fontHeight);
                cs.drawString("No. Tiket  : " + ticketNo);
                cs.moveTextPositionByAmount(0, -fontHeight);
                cs.drawString("Tanggal    : " + dtfDate.print(now));
                cs.moveTextPositionByAmount(0, -fontHeight);
                cs.drawString("Jam Masuk  : " + dtfTime.print(now));
                cs.moveTextPositionByAmount(0, -fontHeight);
                cs.drawString("Kode       : " + priceCode);
                cs.endText();

                int bcWidth = 80;
                int bcHeight = 40;
                BitMatrix bitMatrix = new Code128Writer().
                        encode(ticketNo, BarcodeFormat.CODE_128, bcWidth, bcHeight);
                BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

                PDImageXObject pdxoi = LosslessFactory.createFromImage(document, bufferedImage);
                cs.drawImage(pdxoi, fontWidth * 0.6f, startY - (11.5f * fontHeight));

                cs.beginText();
                cs.setFont(font, fontSize);
                cs.moveTextPositionByAmount(fontWidth, startY - (12.8f * fontHeight));
                cs.drawString("Dilarang  meninggalkan  tiket  / ");
                cs.moveTextPositionByAmount(0, -fontHeight);
                cs.drawString("barang berharga dalam kendaraan");
                cs.moveTextPositionByAmount(0, -fontHeight);
                cs.drawString("Wajib menggunakan kunci tambahan");
                cs.moveTextPositionByAmount(0, -fontHeight);
                cs.drawString("Kelalaian  bukan  tanggung jawab");
                cs.moveTextPositionByAmount(0, -fontHeight);
                cs.drawString("pengelola / rumah sakit");
                cs.endText();
            }

            document.save(appServiceBean.getPrintDir() + ticketNo + "a.pdf");
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPageable(new PDFPageable(document));
            job.print();

        } catch (Exception ex) {
            log.severe(ex.toString());
        }
    }

}
