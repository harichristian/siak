package id.ac.idu.webui.kurikulum.kurikulum;

import id.ac.idu.backend.model.Mkurikulum;
import id.ac.idu.backend.model.Mprodi;
import id.ac.idu.kurikulum.service.KurikulumService;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.searchdialogs.ProdiExtendedSearchListBox;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.*;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/9/12
 * Time: 3:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class KurikulumDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(KurikulumDetailCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowKurikulumDetail; // autowired

    protected Borderlayout borderlayout_KurikulumDetail; // autowired

    protected Textbox txtb_code; // autowired
    protected Textbox txtb_cohort; // autowired
    protected Textbox txtb_prodi;
    protected Button btnSearchProdiExtended;
    protected Button button_KurikulumDialog_PrintKurikulum; // autowired

    // Databinding
    protected transient AnnotateDataBinder binder;
    private KurikulumMainCtrl kurikulumMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient KurikulumService kurikulumService;

    /**
     * default constructor.<br>
     */
    public KurikulumDetailCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);

        /**
         * 1. Set an 'alias' for this composer name to access it in the
         * zul-file.<br>
         * 2. Set the parameter 'recurse' to 'false' to avoid problems with
         * managing more than one zul-file in one page. Otherwise it would be
         * overridden and can ends in curious error messages.
         */
        this.self.setAttribute("controller", this, false);

        /**
         * 1. Get the overhanded MainController.<br>
         * 2. Set this controller in the MainController.<br>
         * 3. Check if a 'selectedObject' exists yet in the MainController.<br>
         */
        if (arg.containsKey("ModuleMainController")) {
            setKurikulumMainCtrl((KurikulumMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getKurikulumMainCtrl().setKurikulumDetailCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getKurikulumMainCtrl().getSelectedKurikulum() != null) {
                setSelectedKurikulum(getKurikulumMainCtrl().getSelectedKurikulum());
            } else
                setSelectedKurikulum(null);
        } else {
            setSelectedKurikulum(null);
        }

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // +++++++++++++++ Component Events ++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    /**
     * Automatically called method from zk.
     *
     * @param event
     * @throws Exception
     */
    public void onCreate$windowKurikulumDetail(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        binder.loadAll();

        doFitSize(event);
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // +++++++++++++++++ Business Logic ++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++++++ Helpers ++++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    /**
     * Recalculates the container size for this controller and resize them.
     * <p/>
     * Calculate how many rows have been place in the listbox. Get the
     * currentDesktopHeight from a hidden Intbox from the index.zul that are
     * filled by onClientInfo() in the indexCtroller.
     */
    public void doFitSize(Event event) {
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
        final int maxListBoxHeight = height - 148;
        borderlayout_KurikulumDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowKurikulumDetail.invalidate();
    }

    /**
     * Set all components in readOnly/disabled modus.
     * <p/>
     * true = all components are readOnly or disabled.<br>
     * false = all components are accessable.<br>
     *
     * @param b
     */
    public void doReadOnlyMode(boolean b) {
        txtb_code.setReadonly(b);
        txtb_cohort.setReadonly(b);
        btnSearchProdiExtended.setDisabled(b);
    }


    public void onClick$btnSearchProdiExtended(Event event) {
        doSearchProdiExtended(event);
    }

    private void doSearchProdiExtended(Event event) {
        Mprodi prodi = ProdiExtendedSearchListBox.show(windowKurikulumDetail);

        if (prodi != null) {
            txtb_prodi.setValue(prodi.getCnmprogst());
            Mkurikulum obj = getKurikulum();
            obj.setMprodi(prodi);
            setKurikulum(obj);
        }
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    /**
     * Best Pratice Hint:<br>
     * The setters/getters for the local annotated data binded Beans/Sets are
     * administered in the module's mainController. Working in this way you have
     * clean line to share this beans/sets with other controllers.
     */
    public Mkurikulum getKurikulum() {
        // STORED IN THE module's MainController
        return getKurikulumMainCtrl().getSelectedKurikulum();
    }

    public void setKurikulum(Mkurikulum anKurikulum) {
        // STORED IN THE module's MainController
        getKurikulumMainCtrl().setSelectedKurikulum(anKurikulum);
    }

    public Mkurikulum getSelectedKurikulum() {
        // STORED IN THE module's MainController
        return getKurikulumMainCtrl().getSelectedKurikulum();
    }

    public void setSelectedKurikulum(Mkurikulum selectedKurikulum) {
        // STORED IN THE module's MainController
        getKurikulumMainCtrl().setSelectedKurikulum(selectedKurikulum);
    }

    public BindingListModelList getKurikulums() {
        // STORED IN THE module's MainController
        return getKurikulumMainCtrl().getKurikulums();
    }

    public void setKurikulums(BindingListModelList kurikulums) {
        // STORED IN THE module's MainController
        getKurikulumMainCtrl().setKurikulums(kurikulums);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public void setKurikulumService(KurikulumService kurikulumService) {
        this.kurikulumService = kurikulumService;
    }

    public KurikulumService getKurikulumService() {
        return this.kurikulumService;
    }

    public void setKurikulumMainCtrl(KurikulumMainCtrl kurikulumMainCtrl) {
        this.kurikulumMainCtrl = kurikulumMainCtrl;
    }

    public KurikulumMainCtrl getKurikulumMainCtrl() {
        return this.kurikulumMainCtrl;
    }
}
