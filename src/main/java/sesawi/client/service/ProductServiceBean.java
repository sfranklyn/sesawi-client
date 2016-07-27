/*
 * Copyright 2014 Samuel Franklyn.
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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import redstone.xmlrpc.XmlRpcProxy;
import sesawi.api.ComputersRpc;
import sesawi.api.PricesRpc;
import sesawi.api.TicketsRpc;
import sesawi.api.UsersRpc;
import sesawi.client.qualifier.SesawiConfig;
import sesawi.client.qualifier.SesawiMessage;

/**
 *
 * @author Samuel Franklyn <sfranklyn at gmail.com>
 */
@Singleton
public class ProductServiceBean {

    private static final Logger log = Logger.getLogger(ProductServiceBean.class.getName());
    private String rpcServer;
    private Properties configProperties;

    @PostConstruct
    public void init() {
        try {
            configProperties = new Properties();
            Path configPath = Paths.get("config.properties");
            File configFile = configPath.toFile();
            configFile.createNewFile();
            configProperties.load(new FileReader(configFile));
            rpcServer = configProperties.getProperty("rpc_server");
            if (rpcServer == null) {
                configProperties.setProperty("rpc_server", "http://localhost:8080/sesawi/xml-rpc");
                rpcServer = configProperties.getProperty("rpc_server");
                configProperties.store(new FileWriter(configFile), null);
            }
        } catch (IOException | RuntimeException ex) {
            log.severe(ex.toString());
        }
    }

    @Produces
    @SesawiConfig
    public Properties getConfigProperties() {
        return configProperties;
    }

    @Produces
    @SesawiMessage
    public ResourceBundle getMessages() {
        return ResourceBundle.getBundle("messages");
    }

    @Produces
    public UsersRpc getUsersRpc() {
        UsersRpc usersRpc = null;
        try {
            usersRpc = (UsersRpc) XmlRpcProxy.
                    createProxy(new URL(rpcServer),
                            new Class[]{UsersRpc.class}, true);
        } catch (MalformedURLException ex) {
            log.severe(ex.toString());
        }
        return usersRpc;
    }

    @Produces
    public PricesRpc getPricesRpc() {
        PricesRpc pricesRpc = null;
        try {
            pricesRpc = (PricesRpc) XmlRpcProxy.
                    createProxy(new URL(rpcServer),
                            new Class[]{PricesRpc.class}, true);
        } catch (MalformedURLException ex) {
            log.severe(ex.toString());
        }
        return pricesRpc;
    }

    @Produces
    public TicketsRpc getTicketsRpc() {
        TicketsRpc ticketsRpc = null;
        try {
            ticketsRpc = (TicketsRpc) XmlRpcProxy.
                    createProxy(new URL(rpcServer),
                            new Class[]{TicketsRpc.class}, true);
        } catch (MalformedURLException ex) {
            log.severe(ex.toString());
        }
        return ticketsRpc;
    }

    @Produces
    public ComputersRpc getComputersRpc() {
        ComputersRpc computersRpc = null;
        try {
            computersRpc = (ComputersRpc) XmlRpcProxy.
                    createProxy(new URL(rpcServer),
                            new Class[]{ComputersRpc.class}, true);
        } catch (MalformedURLException ex) {
            log.severe(ex.toString());
        }
        return computersRpc;
    }

}
