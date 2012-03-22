package id.ac.idu.webui.administrasi.statusMahasiswa;

import id.ac.idu.administrasi.service.StatusMahasiswaService;
import id.ac.idu.backend.model.Mstatusmhs;
import id.ac.idu.webui.util.GFCBaseCtrl;
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
 * Date: 3/8/12
 * Time: 11:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class StatusMahasiswaDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(StatusMahasiswaDetailCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowStatusMahasiswaDetail; // autowired

    protected Borderlayout borderlayout_StatusMahasiswaDetail; // autowired

    protected Textbox txtb_code; // autowired
    protected Textbox txtb_nama; // autowired
    protected Textbox txtb_keterangan; // autowired
    protected Button button_StatusMahasiswaDialog_PrintStatusMahasiswa; // autowired

    // Databinding
    protected transient AnnotateDataBinder binder;
    private StatusMahasiswaMainCtrl statusMahasiswaMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient StatusMahasiswaService statusMahasiswaService;

    /**
     * default constructor.<br>
     */
    public StatusMahasiswaDetailCtrl() {
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
            setStatusMahasiswaMainCtrl((StatusMahasiswaMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getStatusMahasiswaMainCtrl().setStatusMahasiswaDetailCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getStatusMahasiswaMainCtrl().getSelectedStatusMahasiswa() != null) {
                setSelectedStatusMahasiswa(getStatusMahasiswaMainCtrl().getSelectedStatusMahasiswa());
            } else
                setSelectedStatusMahasiswa(null);
        } else {
            setSelectedStatusMahasiswa(null);
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
    public void onCreate$windowStatusMahasiswaDetail(Event event) throws Exception {
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
        borderlayout_StatusMahasiswaDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowStatusMahasiswaDetail.invalidate();
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
        txtb_nama.setReadonly(b);
        txtb_keterangan.setReadonly(b);
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
    public Mstatusmhs getStatusMahasiswa() {
        // STORED IN THE module's MainController
        return getStatusMahasiswaMainCtrl().getSelectedStatusMahasiswa();
    }

    public void setStatusMahasiswa(Mstatusmhs anStatusMahasiswa) {
        // STORED IN THE module's MainController
        getStatusMahasiswaMainCtrl().setSelectedStatusMahasiswa(anStatusMahasiswa);
    }

    public Mstatusmhs getSelectedStatusMahasiswa() {
        // STORED IN THE module's MainController
        return getStatusMahasiswaMainCtrl().getSelectedStatusMahasiswa();
    }

    public void setSelectedStatusMahasiswa(Mstatusmhs selectedStatusMahasiswa) {
        // STORED IN THE module's MainController
        getStatusMahasiswaMainCtrl().setSelectedStatusMahasiswa(selectedStatusMahasiswa);
    }

    public BindingListModelList getStatusMahasiswas() {
        // STORED IN THE module's MainController
        return getStatusMahasiswaMainCtrl().getStatusMahasiswas();
    }

    public void setStatusMahasiswas(BindingListModelList statusMahasiswas) {
        // STORED IN THE module's MainController
        getStatusMahasiswaMainCtrl().setStatusMahasiswas(statusMahasiswas);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public void setStatusMahasiswaService(StatusMahasiswaService statusMahasiswaService) {
        this.statusMahasiswaService = statusMahasiswaService;
    }

    public StatusMahasiswaService getStatusMahasiswaService() {
        return this.statusMahasiswaService;
    }

    public void setStatusMahasiswaMainCtrl(StatusMahasiswaMainCtrl statusMahasiswaMainCtrl) {
        this.statusMahasiswaMainCtrl = statusMahasiswaMainCtrl;
    }

    public StatusMahasiswaMainCtrl getStatusMahasiswaMainCtrl() {
        return this.statusMahasiswaMainCtrl;
    }
}
