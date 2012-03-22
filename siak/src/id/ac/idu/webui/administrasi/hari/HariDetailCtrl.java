package id.ac.idu.webui.administrasi.hari;

import id.ac.idu.administrasi.service.HariService;
import id.ac.idu.backend.model.Mhari;
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
 * Time: 7:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class HariDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(HariDetailCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowHariDetail; // autowired

    protected Borderlayout borderlayout_HariDetail; // autowired

    protected Textbox txtb_code; // autowired
    protected Textbox txtb_nama; // autowired
    protected Button button_HariDialog_PrintHari; // autowired

    // Databinding
    protected transient AnnotateDataBinder binder;
    private HariMainCtrl hariMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient HariService hariService;

    /**
     * default constructor.<br>
     */
    public HariDetailCtrl() {
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
            setHariMainCtrl((HariMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getHariMainCtrl().setHariDetailCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getHariMainCtrl().getSelectedHari() != null) {
                setSelectedHari(getHariMainCtrl().getSelectedHari());
            } else
                setSelectedHari(null);
        } else {
            setSelectedHari(null);
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
    public void onCreate$windowHariDetail(Event event) throws Exception {
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
        borderlayout_HariDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowHariDetail.invalidate();
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
    public Mhari getHari() {
        // STORED IN THE module's MainController
        return getHariMainCtrl().getSelectedHari();
    }

    public void setHari(Mhari anHari) {
        // STORED IN THE module's MainController
        getHariMainCtrl().setSelectedHari(anHari);
    }

    public Mhari getSelectedHari() {
        // STORED IN THE module's MainController
        return getHariMainCtrl().getSelectedHari();
    }

    public void setSelectedHari(Mhari selectedHari) {
        // STORED IN THE module's MainController
        getHariMainCtrl().setSelectedHari(selectedHari);
    }

    public BindingListModelList getHaris() {
        // STORED IN THE module's MainController
        return getHariMainCtrl().getHaris();
    }

    public void setHaris(BindingListModelList haris) {
        // STORED IN THE module's MainController
        getHariMainCtrl().setHaris(haris);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public void setHariService(HariService hariService) {
        this.hariService = hariService;
    }

    public HariService getHariService() {
        return this.hariService;
    }

    public void setHariMainCtrl(HariMainCtrl hariMainCtrl) {
        this.hariMainCtrl = hariMainCtrl;
    }

    public HariMainCtrl getHariMainCtrl() {
        return this.hariMainCtrl;
    }
}
