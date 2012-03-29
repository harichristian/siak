package id.ac.idu.webui.administrasi.mahasiswa.model;

import id.ac.idu.backend.model.Mpbahasamhs;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import java.io.Serializable;


/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 29 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class KursusSearchBahasaList implements ListitemRenderer, Serializable {
    private static final long serialVersionUID = 1925499383404057064L;
	private static final Logger logger = Logger.getLogger(KursusSearchBahasaList.class);

    @Override
	public void render(Listitem item, Object data) throws Exception {
		final Mpbahasamhs bahasa = (Mpbahasamhs) data;

		Listcell lc = new Listcell(bahasa.getCno());
		lc.setParent(item);
        lc = new Listcell(bahasa.getCnmbahasa());
		lc.setParent(item);
        lc = new Listcell(bahasa.getCstatus());
		lc.setParent(item);
        lc = new Listcell(bahasa.getCket());
		lc.setParent(item);

        item.setAttribute("data", data);
		ComponentsCtrl.applyForward(item, "onDoubleClick=onBahasaItem");
	}
}
