package id.ac.idu.webui.administrasi.jenjang;

import id.ac.idu.administrasi.service.JenjangService;
import id.ac.idu.backend.model.Mjenjang;
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
 * Time: 6:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class JenjangDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(JenjangDetailCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowJenjangDetail; // autowired

    protected Borderlayout borderlayout_JenjangDetail; // autowired

    protected Textbox txtb_code; // autowired
    protected Textbox txtb_nama; // autowired
    protected Textbox txtb_singkatan; // autowired
    protected Button button_JenjangDialog_PrintJenjang; // autowired

    // Databinding
    protected transient AnnotateDataBinder binder;
    private JenjangMainCtrl jenjangMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient JenjangService jenjangService;

    /**
     * default constructor.<br>
     */
    public JenjangDetailCtrl() {
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
            setJenjangMainCtrl((JenjangMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getJenjangMainCtrl().setJenjangDetailCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getJenjangMainCtrl().getSelectedJenjang() != null) {
                setSelectedJenjang(getJenjangMainCtrl().getSelectedJenjang());
            } else
                setSelectedJenjang(null);
        } else {
            setSelectedJenjang(null);
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
    public void onCreate$windowJenjangDetail(Event event) throws Exception {
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
        borderlayout_JenjangDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowJenjangDetail.invalidate();
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
        txtb_singkatan.setReadonly(b);
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
    public Mjenjang getJenjang() {
        // STORED IN THE module's MainController
        return getJenjangMainCtrl().getSelectedJenjang();
    }

    public void setJenjang(Mjenjang anJenjang) {
        // STORED IN THE module's MainController
        getJenjangMainCtrl().setSelectedJenjang(anJenjang);
    }

    public Mjenjang getSelectedJenjang() {
        // STORED IN THE module's MainController
        return getJenjangMainCtrl().getSelectedJenjang();
    }

    public void setSelectedJenjang(Mjenjang selectedJenjang) {
        // STORED IN THE module's MainController
        getJenjangMainCtrl().setSelectedJenjang(selectedJenjang);
    }

    public BindingListModelList getJenjangs() {
        // STORED IN THE module's MainController
        return getJenjangMainCtrl().getJenjangs();
    }

    public void setJenjangs(BindingListModelList jenjangs) {
        // STORED IN THE module's MainController
        getJenjangMainCtrl().setJenjangs(jenjangs);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public void setJenjangService(JenjangService jenjangService) {
        this.jenjangService = jenjangService;
    }

    public JenjangService getJenjangService() {
        return this.jenjangService;
    }

    public void setJenjangMainCtrl(JenjangMainCtrl jenjangMainCtrl) {
        this.jenjangMainCtrl = jenjangMainCtrl;
    }

    public JenjangMainCtrl getJenjangMainCtrl() {
        return this.jenjangMainCtrl;
    }
}
