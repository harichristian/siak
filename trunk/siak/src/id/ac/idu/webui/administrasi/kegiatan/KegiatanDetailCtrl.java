package id.ac.idu.webui.administrasi.kegiatan;

import id.ac.idu.administrasi.service.KegiatanService;
import id.ac.idu.backend.model.Mkegiatan;
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
 * Date: 08/03/12
 * Time: 23:10
 * To change this template use File | Settings | File Templates.
 */
public class KegiatanDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(KegiatanDetailCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowKegiatanDetail; // autowired

    protected Borderlayout borderlayout_KegiatanDetail; // autowired

    protected Textbox txtb_filNr; // autowired
    protected Textbox txtb_filName1; // autowired
    protected Button button_KegiatanDialog_PrintKegiatan; // autowired

    // Databinding
    protected transient AnnotateDataBinder binder;
    private KegiatanMainCtrl kegiatanMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient KegiatanService kegiatanService;

    /**
     * default constructor.<br>
     */
    public KegiatanDetailCtrl() {
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
            setKegiatanMainCtrl((KegiatanMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getKegiatanMainCtrl().setKegiatanDetailCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getKegiatanMainCtrl().getSelectedKegiatan() != null) {
                setSelectedKegiatan(getKegiatanMainCtrl().getSelectedKegiatan());
            } else
                setSelectedKegiatan(null);
        } else {
            setSelectedKegiatan(null);
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
    public void onCreate$windowKegiatanDetail(Event event) throws Exception {
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
        borderlayout_KegiatanDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowKegiatanDetail.invalidate();
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
    public Mkegiatan getKegiatan() {
        // STORED IN THE module's MainController
        return getKegiatanMainCtrl().getSelectedKegiatan();
    }

    public void setKegiatan(Mkegiatan anKegiatan) {
        // STORED IN THE module's MainController
        getKegiatanMainCtrl().setSelectedKegiatan(anKegiatan);
    }

    public Mkegiatan getSelectedKegiatan() {
        // STORED IN THE module's MainController
        return getKegiatanMainCtrl().getSelectedKegiatan();
    }

    public void setSelectedKegiatan(Mkegiatan selectedKegiatan) {
        // STORED IN THE module's MainController
        getKegiatanMainCtrl().setSelectedKegiatan(selectedKegiatan);
    }

    public BindingListModelList getKegiatans() {
        // STORED IN THE module's MainController
        return getKegiatanMainCtrl().getKegiatans();
    }

    public void setKegiatans(BindingListModelList offices) {
        // STORED IN THE module's MainController
        getKegiatanMainCtrl().setKegiatans(offices);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public void setKegiatanService(KegiatanService kegiatanService) {
        this.kegiatanService = kegiatanService;
    }

    public KegiatanService getKegiatanService() {
        return this.kegiatanService;
    }

    public void setKegiatanMainCtrl(KegiatanMainCtrl kegiatanMainCtrl) {
        this.kegiatanMainCtrl = kegiatanMainCtrl;
    }

    public KegiatanMainCtrl getKegiatanMainCtrl() {
        return this.kegiatanMainCtrl;
    }
}
