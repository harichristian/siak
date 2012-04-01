package id.ac.idu.webui.administrasi.mahasiswa.model;

import id.ac.idu.backend.model.Mprestasimhs;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import java.io.Serializable;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 02 Apr 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class PrestasiSearchList implements ListitemRenderer, Serializable {
    private static final long serialVersionUID = 1925499383404057064L;
	private static final Logger logger = Logger.getLogger(PribadiSearchKodeposList.class);

    @Override
	public void render(Listitem item, Object data) throws Exception {
		final Mprestasimhs prestasi = (Mprestasimhs) data;

		Listcell lc = new Listcell(prestasi.getCno());
		lc.setParent(item);
        lc = new Listcell(prestasi.getCnmprestasi());
		lc.setParent(item);
        lc = new Listcell(String.valueOf(prestasi.getCnmberi()));
		lc.setParent(item);
        lc = new Listcell(String.valueOf(prestasi.getCthnterima()));
		lc.setParent(item);
        lc = new Listcell(String.valueOf(prestasi.getCket()));
		lc.setParent(item);

		item.setAttribute("data", data);
		ComponentsCtrl.applyForward(item, "onDoubleClick=onPrestasiItem");
	}
}
