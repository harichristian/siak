package id.ac.idu.webui.administrasi.mahasiswa.model;

import id.ac.idu.backend.model.Mjenjang;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import java.io.Serializable;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 28 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class PendidikanSearchJenjangList implements ListitemRenderer, Serializable {
    private static final long serialVersionUID = 1925499383404057064L;
	private static final Logger logger = Logger.getLogger(PendidikanSearchJenjangList.class);

    @Override
	public void render(Listitem item, Object data) throws Exception {
		final Mjenjang jenjang = (Mjenjang) data;

		Listcell lc = new Listcell(jenjang.getCkdjen());
		lc.setParent(item);
        lc = new Listcell(jenjang.getCnmjen());
		lc.setParent(item);

        item.setAttribute("data", data);
		ComponentsCtrl.applyForward(item, "onDoubleClick=onJenjangItem");
	}
}
