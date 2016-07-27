/*
 * Copyright 2014 Samuel Franklyn <sfranklyn at gmail.com>.
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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import net.java.dev.designgridlayout.DesignGridLayout;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import sesawi.client.qualifier.SesawiMessage;
import sesawi.client.service.AppServiceBean;
import sesawi.client.service.UsersServiceBean;

/**
 *
 * @author Samuel Franklyn <sfranklyn at gmail.com>
 */
@Singleton
public class LogInForm extends BaseForm {

    private static final long serialVersionUID = -394447964597969311L;
    private static final Logger log = Logger.getLogger(LogInForm.class.getName());
    private JTextField textUser;
    private JPasswordField textPassword;
    private JButton buttonEntry;
    private JButton buttonExit;
    private JButton buttonOpenShift;
    private JButton buttonCloseShift;
    @Inject
    private AppServiceBean appServiceBean;
    @Inject
    private UsersServiceBean usersServiceBean;
    @Inject
    @SesawiMessage
    private ResourceBundle messageSource;

    public AppServiceBean getAppServiceBean() {
        return appServiceBean;
    }

    public void setAppServiceBean(AppServiceBean appServiceBean) {
        this.appServiceBean = appServiceBean;
    }

    public UsersServiceBean getUsersServiceBean() {
        return usersServiceBean;
    }

    public void setUsersServiceBean(UsersServiceBean usersServiceBean) {
        this.usersServiceBean = usersServiceBean;
    }

    @PostConstruct
    public void init() {
        setTitle(messageSource.getString("login_form_title"));

        JPanel panel = new JPanel();
        DesignGridLayout layout = new DesignGridLayout(panel);

        textUser = new JTextField("", 15);
        textPassword = new JPasswordField("", 15);

        buttonEntry = new JButton("Entry Door");
        buttonEntry.setMnemonic(KeyEvent.VK_E);
        buttonEntry.setActionCommand("Entry");
        buttonEntry.addActionListener((ActionEvent e) -> {
            buttonEntryAction();
        });

        buttonExit = new JButton("Exit Door");
        buttonExit.setMnemonic(KeyEvent.VK_X);
        buttonExit.setActionCommand("Exit");
        buttonExit.addActionListener((ActionEvent e) -> {
            buttonExitAction();
        });

        buttonOpenShift = new JButton("Open Shift");
        buttonOpenShift.setMnemonic(KeyEvent.VK_O);
        buttonOpenShift.setActionCommand("Open Shift");
        buttonOpenShift.addActionListener((ActionEvent e) -> {
            buttonOpenShiftAction();
        });

        buttonCloseShift = new JButton("Close Shift");
        buttonCloseShift.setMnemonic(KeyEvent.VK_C);
        buttonCloseShift.setActionCommand("Close Shift");
        buttonCloseShift.addActionListener((ActionEvent e) -> {
            buttonCloseShiftAction();
        });

        layout.row().grid(new JLabel("User Name")).add(textUser);
        layout.row().grid(new JLabel("Password")).add(textPassword);
        layout.row().grid().add(buttonEntry).add(buttonExit);
        layout.row().grid().add(buttonOpenShift).add(buttonCloseShift);                

        add(panel);
        pack();
        setLocationRelativeTo(null);
    }

    public void buttonEntryAction() {
        List<String> messages = usersServiceBean.logIn(textUser.getText(),
                new String(textPassword.getPassword()));
        String dspMessage = "";
        if (!messages.isEmpty()) {
            for (String message : messages) {
                dspMessage = dspMessage.concat("\n" + message);
            }
            JOptionPane.showMessageDialog(null, dspMessage);
            return;
        }
        setVisible(false);
        EntryDoorForm entryDoorForm = BeanProvider.
                getContextualReference(EntryDoorForm.class);
        entryDoorForm.setVisible(true);
    }

    public void buttonExitAction() {
        List<String> messages = usersServiceBean.logIn(textUser.getText(),
                new String(textPassword.getPassword()));
        String dspMessage = "";
        if (!messages.isEmpty()) {
            for (String message : messages) {
                dspMessage = dspMessage.concat("\n" + message);
            }
            JOptionPane.showMessageDialog(null, dspMessage);
            return;
        }
        setVisible(false);
        ExitDoorForm exitDoorForm = BeanProvider.
                getContextualReference(ExitDoorForm.class);
        exitDoorForm.setVisible(true);
    }

    public void buttonOpenShiftAction() {
        List<String> messages = usersServiceBean.openShift(textUser.getText(),
                new String(textPassword.getPassword()),
                appServiceBean.getComputerName());
        String dspMessage = "";
        if (!messages.isEmpty()) {
            for (String message : messages) {
                dspMessage = dspMessage.concat("\n" + message);
            }
            JOptionPane.showMessageDialog(null, dspMessage);
            return;
        }
        JOptionPane.showMessageDialog(null, messageSource.getString("shift_open"));
    }

    public void buttonCloseShiftAction() {
        List<String> messages = usersServiceBean.closeShift(textUser.getText(),
                new String(textPassword.getPassword()),
                appServiceBean.getComputerName());
        String dspMessage = "";
        if (!messages.isEmpty()) {
            for (String message : messages) {
                dspMessage = dspMessage.concat("\n" + message);
            }
            JOptionPane.showMessageDialog(null, dspMessage);
            return;
        }
        JOptionPane.showMessageDialog(null, messageSource.getString("shift_close"));
    }

}
