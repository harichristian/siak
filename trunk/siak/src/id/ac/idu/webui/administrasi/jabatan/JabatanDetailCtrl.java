package id.ac.idu.webui.administrasi.jabatan;

import id.ac.idu.administrasi.service.JabatanService;
import id.ac.idu.backend.model.Mjabatan;
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
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the /WEB-INF/pages/jabatan/officeList.zul
 * file.<br>
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 *
 * @author Hari
 * @changes 05/15/2009: sge Migrating the list models for paging. <br>
 * 07/24/2009: sge changings for clustering.<br>
 * 10/12/2009: sge changings in the saving routine.<br>
 * 11/07/2009: bbr changed to extending from GFCBaseCtrl<br>
 * (GenericForwardComposer) for spring-managed creation.<br>
 * 07/04/2010: sge modified for zk5.x with complete Annotated
 * Databinding.<br>
 */
public class JabatanDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(JabatanDetailCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowJabatanDetail; // autowired

    protected Borderlayout borderlayout_JabatanDetail; // autowired

    protected Textbox txtb_filNr; // autowired
    protected Textbox txtb_filName1; // autowired
    protected Button button_JabatanDialog_PrintJabatan; // autowired

    // Databinding
    protected transient AnnotateDataBinder binder;
    private JabatanMainCtrl jabatanMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient JabatanService jabatanService;

    /**
     * default constructor.<br>
     */
    public JabatanDetailCtrl() {
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
            setJabatanMainCtrl((JabatanMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getJabatanMainCtrl().setJabatanDetailCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getJabatanMainCtrl().getSelectedJabatan() != null) {
                setSelectedJabatan(getJabatanMainCtrl().getSelectedJabatan());
            } else
                setSelectedJabatan(null);
        } else {
            setSelectedJabatan(null);
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
    public void onCreate$windowJabatanDetail(Event event) throws Exception {
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
        borderlayout_JabatanDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowJabatanDetail.invalidate();
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
        txtb_filNr.setReadonly(b);
        txtb_filName1.setReadonly(b);
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
    public Mjabatan getJabatan() {
        // STORED IN THE module's MainController
        return getJabatanMainCtrl().getSelectedJabatan();
    }

    public void setJabatan(Mjabatan anJabatan) {
        // STORED IN THE module's MainController
        getJabatanMainCtrl().setSelectedJabatan(anJabatan);
    }

    public Mjabatan getSelectedJabatan() {
        // STORED IN THE module's MainController
        return getJabatanMainCtrl().getSelectedJabatan();
    }

    public void setSelectedJabatan(Mjabatan selectedJabatan) {
        // STORED IN THE module's MainController
        getJabatanMainCtrl().setSelectedJabatan(selectedJabatan);
    }

    public BindingListModelList getJabatans() {
        // STORED IN THE module's MainController
        return getJabatanMainCtrl().getJabatans();
    }

    public void setJabatans(BindingListModelList offices) {
        // STORED IN THE module's MainController
        getJabatanMainCtrl().setJabatans(offices);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public void setJabatanService(JabatanService jabatanService) {
        this.jabatanService = jabatanService;
    }

    public JabatanService getJabatanService() {
        return this.jabatanService;
    }

    public void setJabatanMainCtrl(JabatanMainCtrl jabatanMainCtrl) {
        this.jabatanMainCtrl = jabatanMainCtrl;
    }

    public JabatanMainCtrl getJabatanMainCtrl() {
        return this.jabatanMainCtrl;
    }
}
