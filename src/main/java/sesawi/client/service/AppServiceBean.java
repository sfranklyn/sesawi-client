/*
 * Copyright 2013 Samuel Franklyn <sfranklyn@gmail.com>.
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
package sesawi.client.service;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Singleton;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Singleton
public class AppServiceBean {

    private static final Logger log = Logger.getLogger(AppServiceBean.class.getName());
    private MessageDigest messageDigest = null;
    private String userName;
    private String computerName;
    private String printDir;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public String getPrintDir() {
        return printDir;
    }

    public void setPrintDir(String printDir) {
        this.printDir = printDir;
    }

    @PostConstruct
    public void init() {
        try {
            messageDigest = MessageDigest.getInstance("SHA-512");

            computerName = ManagementFactory.getRuntimeMXBean().getName().split("@")[1];
        } catch (NoSuchAlgorithmException ex) {
            log.log(Level.SEVERE, "sesawi:" + ex.toString(), ex);
        }

        printDir = "tickets/";
        if (!new File(printDir).mkdir()) {}
    }

    public String hashPassword(final String password) {
        byte[] hash = null;
        try {
            messageDigest.reset();
            hash = messageDigest.digest(password.getBytes());
        } catch (Exception ex) {
            log.log(Level.SEVERE, "sesawi:" + ex.toString(), ex);
        }
        String result = "";
        if (hash != null) {
            for (int i = 0; i < hash.length; i++) {
                result += Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1);
            }
        }
        return result;
    }
}
