package id.ac.idu.webui.administrasi.mahasiswa.model;

import id.ac.idu.backend.model.Mkaryamhs;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import java.io.Serializable;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 30 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class KegiatanSearchKaryaList implements ListitemRenderer, Serializable  {
    private static final long serialVersionUID = 1925499383404057064L;
	private static final Logger logger = Logger.getLogger(KursusSearchList.class);

    @Override
	public void render(Listitem item, Object data) throws Exception {
		final Mkaryamhs karya = (Mkaryamhs) data;

		Listcell lc = new Listcell(karya.getCno());
		lc.setParent(item);
        lc = new Listcell(karya.getCnmkyrilm());
		lc.setParent(item);
        lc = new Listcell(karya.getCmediaterbit());
		lc.setParent(item);
        lc = new Listcell(karya.getCthnterbit());
		lc.setParent(item);
        lc = new Listcell(karya.getCket());
		lc.setParent(item);

        item.setAttribute("data", data);
		ComponentsCtrl.applyForward(item, "onDoubleClick=onKaryaItem");
	}
}
