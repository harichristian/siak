package id.ac.idu.webui.mankurikulum.matakuliah;

import id.ac.idu.backend.model.Mtbmtkl;
import id.ac.idu.mankurikulum.service.MatakuliahService;
import id.ac.idu.webui.util.GFCBaseCtrl;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import java.io.Serializable;

/**
 * User: hermanto
 * Date: 08 Mar 12
 * Time: 16:55:28
 */

public class MatakuliahDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(MatakuliahDetailCtrl.class);

    protected Window windowMatakuliahDetail;
    protected Borderlayout borderlayout_Detail;

    protected Textbox txtb_ckdmtk;
    protected Textbox txtb_cnamamk;
    protected Textbox txtb_cingmk;
    protected Textbox txtb_csingmk;
    protected Intbox txtb_nsks;
    protected Textbox txtb_keterangan;

    protected transient AnnotateDataBinder binder;

    private MatakuliahMainCtrl mainCtrl;
    private transient MatakuliahService service;

    public MatakuliahDetailCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        this.self.setAttribute("controller", this, false);

        if (arg.containsKey("ModuleMainController")) {
            setMainCtrl((MatakuliahMainCtrl) arg.get("ModuleMainController"));
            getMainCtrl().setDetailCtrl(this);

            if (getMainCtrl().getSelected() != null) setSelected(getMainCtrl().getSelected());
            else setSelected(null);
        } else setSelected(null);
    }

    public void onCreate$windowMatakuliahDetail(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        binder.loadAll();

        doFitSize(event);
    }

    public void doFitSize(Event event) {
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
        final int maxListBoxHeight = height - 148;
        borderlayout_Detail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowMatakuliahDetail.invalidate();
    }

    public void doReadOnlyMode(boolean b) {
        txtb_ckdmtk.setReadonly(b);
        txtb_cnamamk.setReadonly(b);
        txtb_cingmk.setReadonly(b);
        txtb_csingmk.setReadonly(b);
        txtb_nsks.setReadonly(b);
        txtb_keterangan.setReadonly(b);
    }

    public Mtbmtkl getSelected() {
        return getMainCtrl().getSelected();
    }

    public void setSelected(Mtbmtkl _obj) {
        getMainCtrl().setSelected(_obj);
    }

    public BindingListModelList getBinding() {
        return getMainCtrl().getBinding();
    }

    public void setBinding(BindingListModelList _obj) {
        getMainCtrl().setBinding(_obj);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public MatakuliahMainCtrl getMainCtrl() {
        return mainCtrl;
    }

    public void setMainCtrl(MatakuliahMainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public MatakuliahService getService() {
        return service;
    }

    public void setService(MatakuliahService service) {
        this.service = service;
    }
}
