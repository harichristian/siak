package id.ac.idu.webui.administrasi.tahunajar;

import id.ac.idu.administrasi.service.ThajarService;
import id.ac.idu.backend.model.Mthajar;
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
 * User: Hari Christian
 * Date: 05/04/12
 * Time: 14:50
 * To change this template use File | Settings | File Templates.
 */
public class ThajarDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(ThajarDetailCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowThajarDetail; // autowired

    protected Borderlayout borderlayout_ThajarDetail; // autowired

    protected Textbox txtb_filNr; // autowired
    protected Intbox txtb_filName1; // autowired
    protected Button button_ThajarDialog_PrintThajar; // autowired

    // Databinding
    protected transient AnnotateDataBinder binder;
    private ThajarMainCtrl thajarMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient ThajarService thajarService;

    /**
     * default constructor.<br>
     */
    public ThajarDetailCtrl() {
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
            setThajarMainCtrl((ThajarMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getThajarMainCtrl().setThajarDetailCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getThajarMainCtrl().getSelectedThajar() != null) {
                setSelectedThajar(getThajarMainCtrl().getSelectedThajar());
            } else
                setSelectedThajar(null);
        } else {
            setSelectedThajar(null);
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
    public void onCreate$windowThajarDetail(Event event) throws Exception {
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
        borderlayout_ThajarDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowThajarDetail.invalidate();
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
    public Mthajar getThajar() {
        // STORED IN THE module's MainController
        return getThajarMainCtrl().getSelectedThajar();
    }

    public void setThajar(Mthajar anThajar) {
        // STORED IN THE module's MainController
        getThajarMainCtrl().setSelectedThajar(anThajar);
    }

    public Mthajar getSelectedThajar() {
        // STORED IN THE module's MainController
        return getThajarMainCtrl().getSelectedThajar();
    }

    public void setSelectedThajar(Mthajar selectedThajar) {
        // STORED IN THE module's MainController
        getThajarMainCtrl().setSelectedThajar(selectedThajar);
    }

    public BindingListModelList getThajars() {
        // STORED IN THE module's MainController
        return getThajarMainCtrl().getThajars();
    }

    public void setThajars(BindingListModelList offices) {
        // STORED IN THE module's MainController
        getThajarMainCtrl().setThajars(offices);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public void setThajarService(ThajarService thajarService) {
        this.thajarService = thajarService;
    }

    public ThajarService getThajarService() {
        return this.thajarService;
    }

    public void setThajarMainCtrl(ThajarMainCtrl thajarMainCtrl) {
        this.thajarMainCtrl = thajarMainCtrl;
    }

    public ThajarMainCtrl getThajarMainCtrl() {
        return this.thajarMainCtrl;
    }
}
