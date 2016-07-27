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
import sesawi.api.PricesRpc;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Singleton
public class PricesServiceBean {
    
    private static final Logger log = Logger.getLogger(PricesServiceBean.class.getName());
    @Inject 
    private PricesRpc pricesRpc;

    public List<Map> receive() {
        List<Map> pricesList = new ArrayList<>();
        try {
            pricesList = pricesRpc.receive();
        } catch (Exception ex) {
            log.severe(ex.toString());
        }
        return pricesList;
    }

}
