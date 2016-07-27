/*
 * Copyright 2015 Samuel Franklyn.
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

import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.VK_ENTER;
import java.awt.event.KeyListener;
import java.util.Map;
import sesawi.client.service.TicketsServiceBean;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
public class PoliceNoKeyListener implements KeyListener {

    private final ExitDoorForm edf;
    private final TicketsServiceBean ticketsServiceBean;

    public PoliceNoKeyListener(ExitDoorForm edf, TicketsServiceBean ticketsServiceBean) {
        this.edf = edf;
        this.ticketsServiceBean = ticketsServiceBean;
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        if (ke.getKeyCode() != VK_ENTER) {
            return;
        }
        String policeNo = edf.getFieldTicketPoliceNo().getText().toUpperCase().concat("%");
        edf.getListModelTickets().clear();
        edf.setTicketsListMap(ticketsServiceBean.receiveEntryByTicketPoliceNo(policeNo));
        if (!edf.getTicketsListMap().isEmpty()) {
            for (Map ticketMap : edf.getTicketsListMap()) {
                edf.getListModelTickets().addElement(ticketMap.get("ticketNo"));
            }
            edf.getListTickets().setSelectedIndex(0);
        }
    }

}
