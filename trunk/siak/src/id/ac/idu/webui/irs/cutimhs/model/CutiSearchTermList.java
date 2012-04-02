package id.ac.idu.webui.irs.cutimhs.model;

import id.ac.idu.backend.model.Mterm;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import java.io.Serializable;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 03 Apr 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class CutiSearchTermList implements ListitemRenderer, Serializable {
    private static final long serialVersionUID = 1925499383404057064L;
	private static final Logger logger = Logger.getLogger(OrderSearchMahasiswaList.class);

    @Override
	public void render(Listitem item, Object data) throws Exception {
		final Mterm term = (Mterm) data;

		Listcell lc = new Listcell(term.getKdTerm());
		lc.setParent(item);
		lc = new Listcell(term.getDeskripsi());
		lc.setParent(item);

		item.setAttribute("data", data);
		ComponentsCtrl.applyForward(item, "onDoubleClick=onTermItem");
	}
}
