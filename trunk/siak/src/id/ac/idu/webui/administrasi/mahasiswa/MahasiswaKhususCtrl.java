package id.ac.idu.webui.administrasi.mahasiswa;

import id.ac.idu.webui.util.GFCBaseCtrl;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import java.io.Serializable;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 12 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MahasiswaKhususCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(MahasiswaKhususCtrl.class);

    protected Window windowKhususDetail;
    protected Borderlayout borderlayout_Khusus;
    protected Textbox txtb_nip;
    protected Textbox txtb_pangkat;
    protected Textbox txtb_jabatan;
    protected Textbox txtb_kesatuan;

    protected transient AnnotateDataBinder binder;
    private MahasiswaDetailCtrl detailCtrl;

    public MahasiswaKhususCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        this.self.setAttribute("controller", this, false);

        if (arg.containsKey("ModuleMainController")) {
            setDetailCtrl((MahasiswaDetailCtrl) arg.get("ModuleMainController"));
            getDetailCtrl().setKhususCtrl(this);
        }
    }

    public void onCreate$windowKhususDetail(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);
        binder.loadAll();
        doFitSize(event);
    }

    public void doReadOnlyMode(boolean b) {
//        txtb_nip.setReadonly(b);
        //txtb_pangkat.setReadonly(b);
        //txtb_jabatan.setReadonly(b);
        //txtb_kesatuan.setReadonly(b);
    }

    public void doFitSize(Event event) {
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
        final int maxListBoxHeight = height - 243;
        borderlayout_Khusus.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowKhususDetail.invalidate();
    }

    public MahasiswaDetailCtrl getDetailCtrl() {
        return detailCtrl;
    }

    public void setDetailCtrl(MahasiswaDetailCtrl detailCtrl) {
        this.detailCtrl = detailCtrl;
    }
}
