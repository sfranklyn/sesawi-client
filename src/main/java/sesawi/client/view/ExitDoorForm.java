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
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterJob;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
//import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
//import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.java.dev.designgridlayout.DesignGridLayout;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.printing.PDFPageable;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import sesawi.client.qualifier.SesawiMessage;
import sesawi.client.service.AppServiceBean;
import sesawi.client.service.ComputersServiceBean;
import sesawi.client.service.TicketsServiceBean;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Singleton
public class ExitDoorForm extends BaseForm {

    private static final long serialVersionUID = -2039755504208613541L;
    private static final Logger log = Logger.
            getLogger(ExitDoorForm.class.getName());
    private final DateTimeFormatter dtfNow = DateTimeFormat.
            forPattern("dd MMM yyyy HH:mm:ss");
    private final DateTimeFormatter dtfDate = DateTimeFormat.
            forPattern("dd MMM yyyy");
    private final DateTimeFormatter dtfTime = DateTimeFormat.
            forPattern("HH : mm : ss");

    @Inject
    private AppServiceBean appServiceBean;
    @Inject
    private TicketsServiceBean ticketsServiceBean;
    @Inject
    private ComputersServiceBean computersServiceBean;
    @Inject
    @SesawiMessage
    private ResourceBundle messageSource;

    private DesignGridLayout layout;
    private JLabel labelUserName;
    private JLabel labelComputerName;
    private JLabel labelDate;

    private JLabel labelBarcode;
    private JTextField fieldBarcode;
    private BarCodeKeyListener barCodeKeyListener;

    private JLabel labelTicketPoliceNo;
    private JTextField fieldTicketPoliceNo;
    private PoliceNoKeyListener policeNoKeyListener;

    private JLabel labelTicketNo;
    private JTextField fieldTicketNo;

    private JLabel labelTicketsComputerName;
    private JTextField fieldComputerName;

    private JLabel labelTicketsUserName;
    private JTextField fieldUserName;

    //private JLabel labelImage;
    //private Webcam webcam;
    //private WebcamPanel webcamPanel;
    private JButton buttonPrint;
    private List<Map> ticketsListMap;
    private DefaultListModel listModelTickets;
    private JList listTickets;
    private JLabel labelLostTickets;
    private JCheckBox checkLostTickets;
    private String locationDesc;

    public String getNowStr() {
        return dtfNow.print(DateTime.now());
    }

    @PostConstruct
    public void init() {
        setTitle("Exit Door");

        JPanel ticketsPanel = new JPanel();
        layout = new DesignGridLayout(ticketsPanel);

        labelUserName = new JLabel(appServiceBean.getUserName());
        layout.row().grid(new JLabel("User Name:")).add(labelUserName);

        labelComputerName = new JLabel(appServiceBean.getComputerName());
        layout.row().grid(new JLabel("Computer Name:")).add(labelComputerName);

        labelDate = new JLabel(getNowStr());
        layout.row().grid(new JLabel("Date:")).add(labelDate);

        Map locationMap = computersServiceBean.receiveLocation();
        locationDesc = (String) locationMap.get("locationDesc");

        labelBarcode = new JLabel("Barcode:");
        fieldBarcode = new JTextField("");
        barCodeKeyListener = new BarCodeKeyListener(this, ticketsServiceBean);
        fieldBarcode.addKeyListener(barCodeKeyListener);
        layout.row().grid(labelBarcode).add(fieldBarcode);

        labelTicketPoliceNo = new JLabel("Police No:");
        fieldTicketPoliceNo = new JTextField("");
        policeNoKeyListener = new PoliceNoKeyListener(this, ticketsServiceBean);
        fieldTicketPoliceNo.addKeyListener(policeNoKeyListener);
        layout.row().grid(labelTicketPoliceNo).add(fieldTicketPoliceNo);

        labelTicketNo = new JLabel("Ticket No:");
        fieldTicketNo = new JTextField("");
        fieldTicketNo.setEditable(false);
        layout.row().grid(labelTicketNo).add(fieldTicketNo);

        //labelImage = new JLabel();
        //webcam = Webcam.getDefault();
        //webcam.setViewSize(WebcamResolution.QVGA.getSize());
        //webcamPanel = new WebcamPanel(webcam);
        //layout.row().grid().add(labelImage).add(webcamPanel);
        labelTicketsComputerName = new JLabel("Computer Name:");
        fieldComputerName = new JTextField("");
        fieldComputerName.setEditable(false);

        labelTicketsUserName = new JLabel("User Name:");
        fieldUserName = new JTextField("");
        fieldUserName.setEditable(false);

        labelLostTickets = new JLabel("Lost Tickets:");
        checkLostTickets = new JCheckBox();

        layout.row().grid(labelTicketsComputerName).add(fieldComputerName);
        layout.row().grid(labelTicketsUserName).add(fieldUserName);
        layout.row().grid(labelLostTickets).add(checkLostTickets);

        buttonPrint = new JButton("Print");
        buttonPrint.setMnemonic(KeyEvent.VK_P);
        buttonPrint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonPrintAction();
            }
        });

        layout.row().grid().add(buttonPrint);

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                labelDate.setText(getNowStr());
            }
        });
        timer.start();

        listModelTickets = new DefaultListModel();
        listTickets = new JList(listModelTickets);
        ticketListRefresh();
        listTickets.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                listSelect();
            }
        });

        JScrollPane scroller
                = new JScrollPane(
                        listTickets, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());
        listPanel.add(new JLabel("Ticket List"), BorderLayout.NORTH);
        listPanel.add(scroller, BorderLayout.CENTER);
        listPanel.setPreferredSize(new Dimension(180, 180));
        listPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel top = new JPanel();
        top.setName("TOP");
        top.setLayout(new BorderLayout());
        top.add(listPanel, BorderLayout.WEST);
        top.add(ticketsPanel, BorderLayout.CENTER);
        add(top);
        pack();
        setLocationRelativeTo(null);
    }

    public void listSelect() {
        if (ticketsListMap.isEmpty()) {
            return;
        }
        if (listTickets.getSelectedIndex() < 0) {
            return;
        }
        Map ticketMap = ticketsListMap.get(listTickets.getSelectedIndex());
        String ticketPoliceNo = (String) ticketMap.get("ticketPoliceNo");
        fieldTicketPoliceNo.setText(ticketPoliceNo);
        String ticketNo = (String) ticketMap.get("ticketNo");
        fieldTicketNo.setText(ticketNo);
        fieldComputerName.setText((String) ticketMap.get("computerName"));
        fieldUserName.setText((String) ticketMap.get("userName"));
        //BufferedImage img;
        //try {
        //    String fileName = appServiceBean.getPrintDir()
        //            + ticketNo + "a.jpg";
        //    img = ImageIO.read(new File(fileName));
        //    ImageIcon icon = new ImageIcon(img);
        //    labelImage.setIcon(icon);
        //} catch (IOException ex) {
        //    log.severe(ex.toString());
        //}
    }

    public void buttonPrintAction() {
        if (ticketsListMap.isEmpty()) {
            String dspMessage = messageSource.getString("ticket_list_empty");
            JOptionPane.showMessageDialog(null, dspMessage);
            return;
        }

        Map ticketsMap = new HashMap();
        DateTime dtEntry = new DateTime();
        DateTime dtExit = new DateTime();
        Long ticketDurationHours = 0l;
        Long ticketDurationMinutes = 0l;
        DecimalFormat df = new DecimalFormat("###,###,###");
        BigDecimal ticketPriceBD = BigDecimal.ZERO;

        try {
            ticketsMap = ticketsListMap.get(listTickets.getSelectedIndex());

            Date ticketEntryTime = (Date) ticketsMap.get("ticketEntryTime");

            Date ticketExitTime = new Date();
            ticketsMap.put("ticketExitTime", ticketExitTime);

            dtEntry = new DateTime(ticketEntryTime);
            dtExit = new DateTime(ticketExitTime);
            Duration duration = new Duration(dtEntry, dtExit);
            Long ticketDuration = duration.getStandardHours();
            ticketDurationHours = duration.getStandardMinutes() / 60;
            ticketDurationMinutes = duration.getStandardMinutes() % 60;
            ticketsMap.put("ticketDuration", ticketDuration.toString());
            ticketsMap.put("ticketLost", checkLostTickets.isSelected());

            List<String> messages = ticketsServiceBean.sendExit(ticketsMap);
            String dspMessage = "";
            if (!messages.isEmpty()) {
                for (String message : messages) {
                    dspMessage = dspMessage.concat("\n" + message);
                }
                JOptionPane.showMessageDialog(null, dspMessage);
                return;
            }

            ticketPriceBD = new BigDecimal(ticketsServiceBean.receivePrice(ticketsMap));
            fieldBarcode.setText("");
            fieldTicketPoliceNo.setText("");
            fieldTicketNo.setText("");
            fieldComputerName.setText("");
            fieldUserName.setText("");
            checkLostTickets.setSelected(false);
            //    labelImage.setIcon(null);

        } catch (HeadlessException ex) {
            log.severe(ex.toString());
        }

        String ticketNo = (String) ticketsMap.get("ticketNo");
        String ticketPoliceNo = (String) ticketsMap.get("ticketPoliceNo");
        String priceCode = (String) ticketsMap.get("priceCode");
        //BufferedImage image = webcam.getImage();
        //try {
        //    ImageIO.write(image, "JPG", new File(appServiceBean.getPrintDir()
        //            + ticketNo + "b.jpg"));
        //} catch (IOException ex) {
        //    log.severe(ex.toString());
        //}

        try (PDDocument document = new PDDocument()) {

            PDPage page = new PDPage();

            document.addPage(page);

            PDFont font = PDType1Font.COURIER_BOLD;

            float fontSize = 10f;
            float fontSizeBig = 14f;
            float fontWidth = font.getStringWidth("W") / 1000 * fontSize;
            float fontHeight = font.getFontDescriptor().getFontBoundingBox().
                    getHeight() / 1000 * fontSize;
            float startY = page.getMediaBox().getHeight() - fontHeight;

            try (PDPageContentStream cs
                    = new PDPageContentStream(document, page)) {

                cs.beginText();
                cs.setFont(font, fontSize);
                cs.moveTextPositionByAmount(fontWidth, startY);
                //             123456890
                cs.drawString("         TIKET PARKIR");
                cs.moveTextPositionByAmount(0, -fontHeight);
                cs.drawString("     " + locationDesc);
                cs.moveTextPositionByAmount(0, -2 * fontHeight);
                cs.drawString("No. Polisi     : " + ticketPoliceNo);
                cs.moveTextPositionByAmount(0, -fontHeight);
                cs.drawString("No. Tiket      : " + ticketNo);
                cs.moveTextPositionByAmount(0, -fontHeight);
                cs.drawString("Tanggal Masuk  : " + dtfDate.print(dtEntry));
                cs.moveTextPositionByAmount(0, -fontHeight);
                cs.drawString("Jam Masuk      : " + dtfTime.print(dtEntry));
                cs.moveTextPositionByAmount(0, -fontHeight);
                cs.drawString("Tanggal Keluar : " + dtfDate.print(dtExit));
                cs.moveTextPositionByAmount(0, -fontHeight);
                cs.drawString("Jam Keluar     : " + dtfTime.print(dtExit));
                cs.moveTextPositionByAmount(0, -fontHeight);
                cs.drawString("Kode           : " + priceCode);
                cs.moveTextPositionByAmount(0, -fontHeight);
                cs.drawString("Lama Parkir    : "
                        + ticketDurationHours.toString() + " Jam "
                        + ticketDurationMinutes.toString() + " Menit ");
                cs.moveTextPositionByAmount(0, -2 * fontHeight);
                cs.drawString("Biaya Parkir   : ");
                cs.setFont(font, fontSizeBig);
                cs.moveTextPositionByAmount(16 * fontWidth, 0);
                cs.drawString("Rp." + df.format(ticketPriceBD));
                cs.moveTextPositionByAmount(-16 * fontWidth, -2 * fontHeight);
                cs.setFont(font, fontSize);
                //             123456789
                cs.drawString("         TERIMA KASIH");
                cs.moveTextPositionByAmount(0, -fontHeight);
                cs.drawString("     ATAS KUNJUNGAN ANDA");
                cs.endText();
            }

            document.save(appServiceBean.getPrintDir() + ticketNo + "b.pdf");
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPageable(new PDFPageable(document));
            job.print();

            ticketListRefresh();

        } catch (Exception ex) {
            log.severe(ex.toString());
        }

    }

    public void ticketListRefresh() {
        listModelTickets.clear();
        ticketsListMap = ticketsServiceBean.receiveEntry();
        if (!ticketsListMap.isEmpty()) {
            for (Map ticketMap : ticketsListMap) {
                listModelTickets.addElement(ticketMap.get("ticketNo"));
            }
            listTickets.setModel(listModelTickets);
            if (!listModelTickets.isEmpty()) {
                listTickets.setSelectedIndex(0);
                listSelect();
            }
        }
    }

    public JTextField getFieldBarcode() {
        return fieldBarcode;
    }

    public void setFieldBarcode(JTextField fieldBarcode) {
        this.fieldBarcode = fieldBarcode;
    }

    public DefaultListModel getListModelTickets() {
        return listModelTickets;
    }

    public void setListModelTickets(DefaultListModel listModelTickets) {
        this.listModelTickets = listModelTickets;
    }

    public JList getListTickets() {
        return listTickets;
    }

    public void setListTickets(JList listTickets) {
        this.listTickets = listTickets;
    }

    public List<Map> getTicketsListMap() {
        return ticketsListMap;
    }

    public void setTicketsListMap(List<Map> ticketsListMap) {
        this.ticketsListMap = ticketsListMap;
    }

    public JTextField getFieldTicketPoliceNo() {
        return fieldTicketPoliceNo;
    }

    public void setFieldTicketPoliceNo(JTextField fieldTicketPoliceNo) {
        this.fieldTicketPoliceNo = fieldTicketPoliceNo;
    }

}
