package id.ac.idu.webui.administrasi.feedbackalumni;

import id.ac.idu.administrasi.service.FeedbackAlumniService;
import id.ac.idu.backend.model.Malumni;
import id.ac.idu.backend.model.Tfeedbackalumni;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.searchdialogs.MalumniExtendedSearchListBox;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.*;

import java.io.Serializable;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 3/21/12
 * Time: 4:07 AM
 */
public class FeedbackAlumniDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(FeedbackAlumniDetailCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowFeedbackAlumniDetail; // autowired

    protected Borderlayout borderlayout_FeedbackAlumniDetail; // autowired

    protected Textbox txtb_term; // autowired
    protected Textbox txtb_nomer; // autowired
    protected Textbox txtb_pertanyaan; // autowired
    protected Textbox txtb_jawaban;
    protected Textbox txtb_nmalumni; // autowired
    protected Textbox txtb_kelompok; // autowired
    protected Button button_FeedbackAlumniDialog_PrintFeedbackAlumni; // autowired
    protected Button btnSearchAlumniExtended;
    protected Listbox list_jenis;
    protected Bandbox cmb_jenis;

    // Databinding
    protected transient AnnotateDataBinder binder;
    private FeedbackAlumniMainCtrl feedbackAlumniMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient FeedbackAlumniService feedbackAlumniService;

    /**
     * default constructor.<br>
     */
    public FeedbackAlumniDetailCtrl() {
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
            setFeedbackAlumniMainCtrl((FeedbackAlumniMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getFeedbackAlumniMainCtrl().setFeedbackAlumniDetailCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getFeedbackAlumniMainCtrl().getSelectedFeedbackAlumni() != null) {
                setSelectedFeedbackAlumni(getFeedbackAlumniMainCtrl().getSelectedFeedbackAlumni());
            } else
                setSelectedFeedbackAlumni(null);
        } else {
            setSelectedFeedbackAlumni(null);
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
    public void onCreate$windowFeedbackAlumniDetail(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        /*GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.JenisInstansi.class)).getEnumToList(),
                list_jenis, cmb_jenis, (getFeedbackAlumni() != null)?getFeedbackAlumni().getCjnsinstansi():null);*/

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
        borderlayout_FeedbackAlumniDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowFeedbackAlumniDetail.invalidate();
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
        txtb_term.setReadonly(b);
        txtb_kelompok.setReadonly(b);
        txtb_nomer.setReadonly(b);
        txtb_pertanyaan.setReadonly(b);
        txtb_jawaban.setReadonly(b);
        btnSearchAlumniExtended.setDisabled(b);
        //list_jenis.setDisabled(b);
        //cmb_jenis.setDisabled(b);
    }

      /**
     * If the Button 'Search Branch ExtendedSearch' is clicked.<br>
     *
     * @param event
     */
    public void onClick$btnSearchAlumniExtended(Event event) {
        doSearchAlumniExtended(event);
    }

    /**
     * Opens the Search and Get Dialog for Branches.<br>
     * It appends/changes the branch object for the current bean.<br>
     *
     * @param event
     */
    private void doSearchAlumniExtended(Event event) {
        Malumni malumni = MalumniExtendedSearchListBox.show(windowFeedbackAlumniDetail);

        if (malumni != null) {
//            txtb_kdalumni.setValue(malumni.getMmahasiswa().getCnim());
            txtb_nmalumni.setValue(malumni.getMmahasiswa().getCnama());
            Tfeedbackalumni afeedback = getFeedbackAlumni();
            afeedback.setMmahasiswa(malumni.getMmahasiswa());
            setFeedbackAlumni(afeedback);
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
    public Tfeedbackalumni getFeedbackAlumni() {
        // STORED IN THE module's MainController
        return getFeedbackAlumniMainCtrl().getSelectedFeedbackAlumni();
    }

    public void setFeedbackAlumni(Tfeedbackalumni anFeedbackAlumni) {
        // STORED IN THE module's MainController
        getFeedbackAlumniMainCtrl().setSelectedFeedbackAlumni(anFeedbackAlumni);
    }

    public Tfeedbackalumni getSelectedFeedbackAlumni() {
        // STORED IN THE module's MainController
        return getFeedbackAlumniMainCtrl().getSelectedFeedbackAlumni();
    }

    public void setSelectedFeedbackAlumni(Tfeedbackalumni selectedFeedbackAlumni) {
        // STORED IN THE module's MainController
        getFeedbackAlumniMainCtrl().setSelectedFeedbackAlumni(selectedFeedbackAlumni);
    }

    public BindingListModelList getFeedbackAlumnis() {
        // STORED IN THE module's MainController
        return getFeedbackAlumniMainCtrl().getFeedbackAlumnis();
    }

    public void setFeedbackAlumnis(BindingListModelList Tfeedbackalumni) {
        // STORED IN THE module's MainController
        getFeedbackAlumniMainCtrl().setFeedbackAlumnis(Tfeedbackalumni);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public void setFeedbackAlumniService(FeedbackAlumniService feedbackAlumniService) {
        this.feedbackAlumniService = feedbackAlumniService;
    }

    public FeedbackAlumniService getFeedbackAlumniService() {
        return this.feedbackAlumniService;
    }

    public void setFeedbackAlumniMainCtrl(FeedbackAlumniMainCtrl feedbackAlumniMainCtrl) {
        this.feedbackAlumniMainCtrl = feedbackAlumniMainCtrl;
    }

    public FeedbackAlumniMainCtrl getFeedbackAlumniMainCtrl() {
        return this.feedbackAlumniMainCtrl;
    }
}
