package id.ac.idu.webui.administrasi.mahasiswa.model;

import id.ac.idu.backend.model.Mppumhskhusus;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import java.io.Serializable;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 25 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class KhususSearchPendidikanList implements ListitemRenderer, Serializable {
    private static final long serialVersionUID = 1925499383404057064L;
	private static final Logger logger = Logger.getLogger(PribadiSearchKodeposList.class);

    @Override
	public void render(Listitem item, Object data) throws Exception {
		final Mppumhskhusus pendidikan = (Mppumhskhusus) data;

		Listcell lc = new Listcell(pendidikan.getCnmbentuk());
		lc.setParent(item);
        lc = new Listcell(pendidikan.getCtahun());
        lc.setParent(item);
        lc = new Listcell(pendidikan.getCket());
        lc.setParent(item);

		item.setAttribute("data", data);
		ComponentsCtrl.applyForward(item, "onDoubleClick=onRiwayatPendidikan");
	}
}
