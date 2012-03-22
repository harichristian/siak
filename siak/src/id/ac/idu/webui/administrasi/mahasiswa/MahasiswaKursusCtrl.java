package id.ac.idu.webui.administrasi.mahasiswa;

import id.ac.idu.webui.util.GFCBaseCtrl;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Window;

import java.io.Serializable;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 12 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MahasiswaKursusCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(MahasiswaKursusCtrl.class);

    protected Window windowKursusDetail;
    protected Borderlayout borderlayout_Kursus;

    protected transient AnnotateDataBinder binder;
    private MahasiswaDetailCtrl detailCtrl;

    public MahasiswaKursusCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        this.self.setAttribute("controller", this, false);

        if (arg.containsKey("ModuleMainController")) {
            setDetailCtrl((MahasiswaDetailCtrl) arg.get("ModuleMainController"));
            getDetailCtrl().setKursusCtrl(this);
        }
    }

    public void onCreate$windowKursusDetail(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);
        binder.loadAll();
        doFitSize(event);
    }

    public void doFitSize(Event event) {
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
        final int maxListBoxHeight = height - 243;
        borderlayout_Kursus.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowKursusDetail.invalidate();
    }

    public MahasiswaDetailCtrl getDetailCtrl() {
        return detailCtrl;
    }

    public void setDetailCtrl(MahasiswaDetailCtrl detailCtrl) {
        this.detailCtrl = detailCtrl;
    }
}
