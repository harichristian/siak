package id.ac.idu.webui.mankurikulum.sesi.model;

import id.ac.idu.backend.model.Msekolah;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.FieldComparator;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.api.Listheader;

import java.io.Serializable;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 30 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class SesiSearchSekolahList implements ListitemRenderer, Serializable {
    private static final long serialVersionUID = 1925499383404057064L;
	private static final Logger logger = Logger.getLogger(SesiSearchSekolahList.class);


    @Override
	public void render(Listitem item, Object data) throws Exception {
		final Msekolah sekolah = (Msekolah) data;

		Listcell lc = new Listcell(sekolah.getCkdsekolah());
		lc.setParent(item);
        lc = new Listcell(sekolah.getCnamaSekolah());
        lc.setParent(item);

		item.setAttribute("data", data);
		ComponentsCtrl.applyForward(item, "onDoubleClick=onSekolah");
	}
}
