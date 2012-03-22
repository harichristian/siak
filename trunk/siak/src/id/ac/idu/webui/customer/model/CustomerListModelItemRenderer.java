/**
 * Copyright 2010 the original author or authors.
 *
 * This file is part of Zksample2. http://zksample2.sourceforge.net/
 *
 * Zksample2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Zksample2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Zksample2.  If not, see <http://www.gnu.org/licenses/gpl.html>.
 */
package id.ac.idu.webui.customer.model;

import id.ac.idu.backend.model.Customer;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import java.io.Serializable;

/**
 * Item renderer for listitems in the listbox.
 *
 * @author bbruhns
 * @author sgerth
 */
public class CustomerListModelItemRenderer implements ListitemRenderer, Serializable {

    private static final long serialVersionUID = 1925499383404057064L;
    private final static Logger logger = Logger.getLogger(CustomerListModelItemRenderer.class);

    @Override
    public void render(Listitem item, Object data) throws Exception {

        final Customer customer = (Customer) data;

        Listcell lc = new Listcell(customer.getKunNr());
        lc.setParent(item);
        lc = new Listcell(customer.getKunMatchcode());
        lc.setParent(item);
        lc = new Listcell(customer.getKunName1());
        lc.setParent(item);
        lc = new Listcell(customer.getKunName2());
        lc.setParent(item);
        lc = new Listcell(customer.getKunOrt());
        lc.setParent(item);
        ComponentsCtrl.applyForward(lc, "onMouseOver=onMouseOverListCell");

        lc = new Listcell();
        final Checkbox cb = new Checkbox();
        cb.setDisabled(true);
        cb.setChecked(customer.checkKunMahnsperre());
        lc.appendChild(cb);
        lc.setParent(item);

        item.setAttribute("data", data);
        ComponentsCtrl.applyForward(item, "onDoubleClick=onCustomerItemDoubleClicked");

    }

}
