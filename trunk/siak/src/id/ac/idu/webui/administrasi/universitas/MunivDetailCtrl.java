package id.ac.idu.webui.administrasi.universitas;

import id.ac.idu.administrasi.service.MunivService;
import id.ac.idu.backend.model.Muniv;
import id.ac.idu.util.Codec;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.GFCListModelCtrl;
import id.ac.idu.webui.util.test.EnumConverter;
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
 * Date: 3/6/12
 * Time: 2:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class MunivDetailCtrl extends GFCBaseCtrl implements Serializable {

    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(MunivDetailCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowMunivDetail; // autowired

    protected Borderlayout borderlayout_MunivDetail; // autowired

    protected Textbox txtb_ckdUniv; // autowired
    protected Textbox txtb_cnamaUniv; // autowired
    protected Textbox txtb_alamatUniv; // autowired
    protected Textbox txtb_cstatus; // autowired
    protected Button button_MunivDialog_PrintMuniv; // autowired
    protected Listbox list_status;
    protected Bandbox cmb_status;

    // Databinding
    protected transient AnnotateDataBinder binder;
    private MunivMainCtrl munivMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient MunivService munivService;

    /**
     * default constructor.<br>
     */
    public MunivDetailCtrl() {
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
            setMunivMainCtrl((MunivMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getMunivMainCtrl().setMunivDetailCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getMunivMainCtrl().getSelectedMuniv() != null) {
                setSelectedMuniv(getMunivMainCtrl().getSelectedMuniv());
            } else
                setSelectedMuniv(null);
        } else {
            setSelectedMuniv(null);
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
    public void onCreate$windowMunivDetail(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);
        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.StatusUniversitas.class)).getEnumToList(),
                list_status, cmb_status, (getMuniv() != null)?getMuniv().getCstatus():null);
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
        borderlayout_MunivDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowMunivDetail.invalidate();
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
        txtb_ckdUniv.setReadonly(b);
        txtb_cnamaUniv.setReadonly(b);
        txtb_alamatUniv.setReadonly(b);
        txtb_cstatus.setReadonly(b);
        list_status.setDisabled(b);
        cmb_status.setDisabled(b);
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
    public Muniv getMuniv() {
        // STORED IN THE module's MainController
        return getMunivMainCtrl().getSelectedMuniv();
    }

    public void setMuniv(Muniv anMuniv) {
        // STORED IN THE module's MainController
        getMunivMainCtrl().setSelectedMuniv(anMuniv);
    }

    public Muniv getSelectedMuniv() {
        // STORED IN THE module's MainController
        return getMunivMainCtrl().getSelectedMuniv();
    }

    public void setSelectedMuniv(Muniv selectedMuniv) {
        // STORED IN THE module's MainController
        getMunivMainCtrl().setSelectedMuniv(selectedMuniv);
    }

    public BindingListModelList getMunivs() {
        // STORED IN THE module's MainController
        return getMunivMainCtrl().getMunivs();
    }

    public void setMunivs(BindingListModelList munivs) {
        // STORED IN THE module's MainController
        getMunivMainCtrl().setMunivs(munivs);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public void setMunivService(MunivService munivService) {
        this.munivService = munivService;
    }

    public MunivService getMunivService() {
        return this.munivService;
    }

    public void setMunivMainCtrl(MunivMainCtrl munivMainCtrl) {
        this.munivMainCtrl = munivMainCtrl;
    }

    public MunivMainCtrl getMunivMainCtrl() {
        return this.munivMainCtrl;
    }
}
