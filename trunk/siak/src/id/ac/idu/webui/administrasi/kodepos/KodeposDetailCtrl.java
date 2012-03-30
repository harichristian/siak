package id.ac.idu.webui.administrasi.kodepos;

import id.ac.idu.administrasi.service.KodePosService;
import id.ac.idu.backend.model.MkodePos;
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
 * @Date 30 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class KodeposDetailCtrl extends GFCBaseCtrl implements Serializable {

    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(KodeposDetailCtrl.class);

    protected Window windowKodeposDetail;
    protected Borderlayout borderDetail;

    protected Textbox txtbkodepos;

    protected AnnotateDataBinder binder;
    private KodePosService kodePosService;
    
    private KodeposMainCtrl mainCtrl;

    public KodeposDetailCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        this.self.setAttribute("controller", this, false);

        if (arg.containsKey("ModuleMainController")) {
            setMainCtrl((KodeposMainCtrl) arg.get("ModuleMainController"));
            getMainCtrl().setDetailCtrl(this);

            if (getMainCtrl().getMkodePos() != null) {
                setMkodePos(getMainCtrl().getMkodePos());
            }
            else setMkodePos(null);
        }
        else setMkodePos(null);
    }

    public void onCreate$windowKodeposDetail(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);
        doFitSize(event);
        binder.loadAll();
    }

    public void doFitSize(Event event) {
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
        final int maxListBoxHeight = height - 148;
        borderDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");
        windowKodeposDetail.invalidate();
    }

    public void doReadOnlyMode(boolean b) {
        txtbkodepos.setReadonly(b);
    }

    public KodeposMainCtrl getMainCtrl() {
        return mainCtrl;
    }

    public void setMainCtrl(KodeposMainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public MkodePos getMkodePos() {
        return getMainCtrl().getMkodePos();
    }

    public void setMkodePos(MkodePos mkodePos) {
        getMainCtrl().setMkodePos(mkodePos);
    }

    public AnnotateDataBinder getBinder() {
        return binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public KodePosService getKodePosService() {
        return kodePosService;
    }

    public void setKodePosService(KodePosService kodePosService) {
        this.kodePosService = kodePosService;
    }
}
