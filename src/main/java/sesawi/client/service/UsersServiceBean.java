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

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Singleton;
import sesawi.api.UsersRpc;
import sesawi.client.qualifier.SesawiMessage;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Singleton
public class UsersServiceBean {

    private static final Logger log = Logger.getLogger(UsersServiceBean.class.getName());
    @Inject
    @SesawiMessage
    private ResourceBundle messageSource;
    @Inject
    private UsersRpc usersRpc;
    @Inject
    private AppServiceBean appServiceBean;

    public List<String> logIn(String userName, String userPassword) {
        List<String> errorList = new ArrayList<>();
        if (userName == null) {
            errorList.add(messageSource.getString("user_name_required"));
        }
        if ("".equals(userName)) {
            errorList.add(messageSource.getString("user_name_required"));
        }
        if (userPassword == null) {
            errorList.add(messageSource.getString("user_password_required"));
        }
        if ("".equals(userPassword)) {
            errorList.add(messageSource.getString("user_password_required"));
        }

        if (errorList.size() > 0) {
            return errorList;
        }
        try {
            errorList.addAll(usersRpc.logIn(userName, userPassword, 
                    appServiceBean.getComputerName()));
            if (!errorList.isEmpty()) {
                return errorList;
            }
        } catch (RuntimeException ex) {
            errorList.add(ex.getMessage());
            return errorList;
        }
        appServiceBean.setUserName(userName);
        return errorList;
    }

    public List<String> openShift(String userName, String userPassword,
            String computerName) {
        List<String> errorList = new ArrayList<>();
        if (userName == null) {
            errorList.add(messageSource.getString("user_name_required"));
        }
        if ("".equals(userName)) {
            errorList.add(messageSource.getString("user_name_required"));
        }
        if (userPassword == null) {
            errorList.add(messageSource.getString("user_password_required"));
        }
        if ("".equals(userPassword)) {
            errorList.add(messageSource.getString("user_password_required"));
        }

        if (errorList.size() > 0) {
            return errorList;
        }
        try {
            errorList.addAll(usersRpc.openShift(userName, userPassword,
                    computerName));
            if (!errorList.isEmpty()) {
                return errorList;
            }
        } catch (RuntimeException ex) {
            errorList.add(ex.getMessage());
            return errorList;
        }
        appServiceBean.setUserName(userName);
        return errorList;
    }

    public List<String> closeShift(String userName, String userPassword,
            String computerName) {
        List<String> errorList = new ArrayList<>();
        if (userName == null) {
            errorList.add(messageSource.getString("user_name_required"));
        }
        if ("".equals(userName)) {
            errorList.add(messageSource.getString("user_name_required"));
        }
        if (userPassword == null) {
            errorList.add(messageSource.getString("user_password_required"));
        }
        if ("".equals(userPassword)) {
            errorList.add(messageSource.getString("user_password_required"));
        }

        if (errorList.size() > 0) {
            return errorList;
        }
        try {
            errorList.addAll(usersRpc.closeShift(userName, userPassword,
                    computerName));
            if (!errorList.isEmpty()) {
                return errorList;
            }
        } catch (RuntimeException ex) {
            errorList.add(ex.getMessage());
            return errorList;
        }
        appServiceBean.setUserName(userName);
        return errorList;
    }

}
