package id.ac.idu.webui.administrasi.feedbackalumni;

import id.ac.idu.administrasi.service.FeedbackAlumniService;
import id.ac.idu.backend.model.Malumni;
import id.ac.idu.backend.model.Mfeedback;
import id.ac.idu.backend.model.Tfeedbackalumni;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.util.ConstantUtil;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.pagging.PagedBindingListWrapper;
import id.ac.idu.webui.util.pagging.PagedListWrapper;
import id.ac.idu.webui.util.searchdialogs.MalumniExtendedSearchListBox;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
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

    protected Paging paging_FeedbackAlumniDetailList;
    protected Listbox listBoxFeedbackAlumniDetail;
    protected Listheader listheader_FeedbackAlumniDetailList_no; // autowired
    protected Listheader listheader_FeedbackAlumniDetailList_pertanyaan; // autowired
    protected Listheader listheader_FeedbackAlumniDetailList_1; // autowired
    protected Listheader listheader_FeedbackAlumniDetailList_2; // autowired
    protected Listheader listheader_FeedbackAlumniDetailList_3; // autowired
    protected Listheader listheader_FeedbackAlumniDetailList_4; // autowired
    protected Listheader listheader_FeedbackAlumniDetailList_5; // autowired

    // Databinding
    protected transient AnnotateDataBinder binder;
    private FeedbackAlumniMainCtrl feedbackAlumniMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient FeedbackAlumniService feedbackAlumniService;

    // row count for listbox
    private int countRows;

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Mfeedback> searchObj;

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
        doFillListbox();
        binder.loadAll();

        doFitSize(event);
    }
    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_FeedbackAlumniDetailList.setPageSize(getCountRows());
        paging_FeedbackAlumniDetailList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_FeedbackAlumniDetailList_no.setSortAscending(new FieldComparator(ConstantUtil.MAHASISWA_DOT_NAME, true));
        listheader_FeedbackAlumniDetailList_no.setSortDescending(new FieldComparator(ConstantUtil.MAHASISWA_DOT_NAME, false));
        listheader_FeedbackAlumniDetailList_pertanyaan.setSortAscending(new FieldComparator(ConstantUtil.TERM, true));
        listheader_FeedbackAlumniDetailList_pertanyaan.setSortDescending(new FieldComparator(ConstantUtil.TERM, false));
        listheader_FeedbackAlumniDetailList_1.setSortAscending(new FieldComparator(ConstantUtil.KELOMPOK, true));
        listheader_FeedbackAlumniDetailList_1.setSortDescending(new FieldComparator(ConstantUtil.KELOMPOK, false));
        listheader_FeedbackAlumniDetailList_2.setSortAscending(new FieldComparator(ConstantUtil.PRODI_DOT_NAME, true));
        listheader_FeedbackAlumniDetailList_2.setSortDescending(new FieldComparator(ConstantUtil.PRODI_DOT_NAME, false));
        listheader_FeedbackAlumniDetailList_3.setSortAscending(new FieldComparator(ConstantUtil.KELOMPOK, true));
        listheader_FeedbackAlumniDetailList_3.setSortDescending(new FieldComparator(ConstantUtil.KELOMPOK, false));
        listheader_FeedbackAlumniDetailList_4.setSortAscending(new FieldComparator(ConstantUtil.PRODI_DOT_NAME, true));
        listheader_FeedbackAlumniDetailList_4.setSortDescending(new FieldComparator(ConstantUtil.PRODI_DOT_NAME, false));
        listheader_FeedbackAlumniDetailList_5.setSortAscending(new FieldComparator(ConstantUtil.PRODI_DOT_NAME, true));
        listheader_FeedbackAlumniDetailList_5.setSortDescending(new FieldComparator(ConstantUtil.PRODI_DOT_NAME, false));

        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Mfeedback>(Mfeedback.class, getCountRows());
        searchObj.addSort(ConstantUtil.ID, false);
        setSearchObj(searchObj);

        // Set the BindingListModel
        getPagedBindingListWrapper().init(searchObj, getListBoxFeedbackAlumniDetail(), paging_FeedbackAlumniDetailList);
        BindingListModelList lml = (BindingListModelList) getListBoxFeedbackAlumniDetail().getModel();
        setFeedbacks(lml);

        // check if first time opened and init databinding for selectedBean
        if (getSelectedFeedback() == null) {
            // init the bean with the first record in the List
            if (lml.getSize() > 0) {
                final int rowIndex = 0;
                // only for correct showing after Rendering. No effect as an
                // Event
                // yet.
                getListBoxFeedbackDetailAlumni().setSelectedIndex(rowIndex);
                // get the first entry and cast them to the needed object
                setSelectedFeedback((Mfeedback) lml.get(0));

                // call the onSelect Event for showing the objects data in the
                // statusBar
                Events.sendEvent(new Event("onSelect", getListBoxFeedbackDetailAlumni(), getSelectedFeedback()));
            }
        }

    }
    public void doFitSize() {
        // normally 0 ! Or we have a i.e. a toolBar on top of the listBox.
        final int specialSize = 5;
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
        final int maxListBoxHeight = height - specialSize - 148;
        setCountRows((int) Math.round(maxListBoxHeight / 17.7));
        //borderLayout_feedbackAlumniList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        //-- windowFeedbackAlumniList.invalidate();
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
    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public Listbox getListBoxFeedbackDetailAlumni() {
        return this.listBoxFeedbackAlumniDetail;
    }

    public void setListBoxFeedbackAlumni(Listbox listBoxFeedbackDetailAlumni) {
        this.listBoxFeedbackAlumniDetail = listBoxFeedbackAlumniDetail;
    }

    public void setSearchObj(HibernateSearchObject<Mfeedback> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Mfeedback> getSearchObj() {
        return this.searchObj;
    }

    public Listbox getListBoxFeedbackAlumniDetail() {
        return this.listBoxFeedbackAlumniDetail;
    }

    public void setListBoxFeedbackAlumniDetail(Listbox listBoxFeedbackAlumniDetail) {
        this.listBoxFeedbackAlumniDetail = listBoxFeedbackAlumniDetail;
    }

    public void setSelectedFeedback(Mfeedback selectedFeedback) {
        // STORED IN THE module's MainController
        getFeedbackAlumniMainCtrl().setSelectedFeedback(selectedFeedback);
    }

    public Mfeedback getSelectedFeedback() {
        // STORED IN THE module's MainController
        return getFeedbackAlumniMainCtrl().getSelectedFeedback();
    }

    public void setFeedbacks(BindingListModelList feedbacks) {
        // STORED IN THE module's MainController
        getFeedbackAlumniMainCtrl().setFeedbacks(feedbacks);
    }

    public BindingListModelList getFeedbacks() {
        // STORED IN THE module's MainController
        return getFeedbackAlumniMainCtrl().getFeedbacks();
    }


    private PagedListWrapper<Mfeedback> pagedListWrapper;

    private PagedBindingListWrapper<Mfeedback> pagedBindingListWrapper;

    public PagedListWrapper<Mfeedback> getPagedListWrapper() {
        return pagedListWrapper;
    }

    public void setPagedListWrapper(PagedListWrapper<Mfeedback> pagedListWrapper) {
        this.pagedListWrapper = pagedListWrapper;
    }

    public void setPagedBindingListWrapper(PagedBindingListWrapper<Mfeedback> pagedBindingListWrapper) {
        this.pagedBindingListWrapper = pagedBindingListWrapper;
    }

    public PagedBindingListWrapper<Mfeedback> getPagedBindingListWrapper() {
        return pagedBindingListWrapper;
    }
}
