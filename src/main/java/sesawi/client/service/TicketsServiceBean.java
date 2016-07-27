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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Singleton;
import sesawi.api.ComputersRpc;
import sesawi.api.TicketsRpc;

/**
 *
 * @author Samuel Franklyn <sfranklyn at gmail.com>
 */
@Singleton
public class TicketsServiceBean {

    private static final Logger log = Logger.getLogger(TicketsServiceBean.class.getName());
    @Inject
    private AppServiceBean appServiceBean;
    @Inject
    private TicketsRpc ticketsRpc;
    @Inject
    private ComputersRpc computersRpc;

    public List<String> sendEntry(Map ticketsMap) {
        List<String> errorList = new ArrayList<>();
        try {
            errorList.addAll(ticketsRpc.sendEntry(ticketsMap));
            if (!errorList.isEmpty()) {
                return errorList;
            }
        } catch (Exception ex) {
            log.severe(ex.toString());
            errorList.add(ex.toString());
        }
        return errorList;
    }

    public List<Map> receiveEntry() {
        List<Map> ticketsList = new ArrayList<>();
        try {
            Map locationMap = computersRpc.receiveLocation(appServiceBean.getComputerName());
            if (locationMap.containsKey("locationName")) {
                String locationName = (String) locationMap.get("locationName");
                ticketsList = ticketsRpc.receiveEntry(locationName);
            }
        } catch (Exception ex) {
            log.severe(ex.toString());
        }
        return ticketsList;
    }

    public List<Map> receiveEntryByTicketNo(String ticketNo) {
        List<Map> ticketsList = new ArrayList<>();
        try {
            Map locationMap = computersRpc.receiveLocation(appServiceBean.getComputerName());
            if (locationMap.containsKey("locationName")) {
                String locationName = (String) locationMap.get("locationName");
                ticketsList = ticketsRpc.receiveEntryByTicketNo(locationName, ticketNo);
            }
        } catch (Exception ex) {
            log.severe(ex.toString());
        }
        return ticketsList;
    }

    public List<Map> receiveEntryByTicketPoliceNo(String ticketPoliceNo) {
        List<Map> ticketsList = new ArrayList<>();
        try {
            Map locationMap = computersRpc.receiveLocation(appServiceBean.getComputerName());
            if (locationMap.containsKey("locationName")) {
                String locationName = (String) locationMap.get("locationName");
                ticketsList = ticketsRpc.receiveEntryByTicketPoliceNo(locationName, ticketPoliceNo);
            }
        } catch (Exception ex) {
            log.severe(ex.toString());
        }
        return ticketsList;
    }

    public Integer receivePrice(Map ticketsMap) {
        return ticketsRpc.receivePrice(ticketsMap);
    }

    public List<String> sendExit(Map ticketsMap) {
        List<String> errorList = new ArrayList<>();
        try {
            errorList.addAll(ticketsRpc.sendExit(ticketsMap));
            if (!errorList.isEmpty()) {
                return errorList;
            }
        } catch (Exception ex) {
            log.severe(ex.toString());
            errorList.add(ex.toString());
        }
        return errorList;
    }

}
