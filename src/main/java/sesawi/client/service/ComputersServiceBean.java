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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Singleton;
import sesawi.api.ComputersRpc;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Singleton
public class ComputersServiceBean {

    private static final Logger log = Logger.getLogger(ComputersServiceBean.class.getName());
    @Inject
    private ComputersRpc computersRpc;
    @Inject
    private AppServiceBean appServiceBean;    

    public Map<String,String> receiveLocation() {
        Map locationMap = new HashMap();
        try {
            locationMap = computersRpc.receiveLocation(appServiceBean.getComputerName());
            if (!locationMap.containsKey("locationDesc")) {
                locationMap.put("locationDesc", "Sesawi Group Building");
            }
            
        } catch (RuntimeException ex) {
            log.severe(ex.toString());
        }
        return locationMap;
    }

}
