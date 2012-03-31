package id.ac.idu.webui.administrasi.feedbackalumni;

import com.trg.search.Filter;
import id.ac.idu.administrasi.service.FeedbackAlumniService;
import id.ac.idu.backend.model.*;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.util.Codec;
import id.ac.idu.util.ConstantUtil;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.pagging.PagedBindingListWrapper;
import id.ac.idu.webui.util.pagging.PagedListWrapper;
import id.ac.idu.webui.util.searchdialogs.MalumniExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.ProdiExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.SekolahExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.TermExtendedSearchListBox;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.*;

import java.io.Serializable;
import java.util.List;

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
    protected Textbox txtb_nim; // autowired
    protected Textbox txtb_pertanyaan; // autowired
    protected Textbox txtb_jawaban;
    protected Textbox txtb_nmalumni; // autowired
    protected Textbox txtb_kelompok; // autowired
    protected Button button_FeedbackAlumniDialog_PrintFeedbackAlumni; // autowired
    protected Button btnSearchAlumniExtended;
    protected Textbox txtb_prodi;
    protected Button btnSearchProdiExtended;
    protected Button btnSearchTermExtended;
    protected Textbox txtb_sekolah;
    protected Button btnSearchSekolahExtended;
    protected Listbox list_jenis;
    protected Bandbox cmb_jenis;

    protected Paging paging_FeedbackAlumniDetailList;
    protected Paging paging_FeedbackList;
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
    private HibernateSearchObject<Tfeedbackalumni> searchObj;
    private HibernateSearchObject<Mfeedback> so;

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
            } else {
                setSelectedFeedbackAlumni(null);
            }
            if (getFeedbackAlumniMainCtrl().getSelectedFeedbackAlumniList() != null) {
                setSelectedFeedbackAlumniList(getFeedbackAlumniMainCtrl().getSelectedFeedbackAlumniList());
            } else {
                setSelectedFeedbackAlumniList(null);
            }
        } else {
            setSelectedFeedbackAlumni(null);
            setSelectedFeedbackAlumniList(null);
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
        //listheader_FeedbackAlumniDetailList_no.setSortAscending(new FieldComparator(ConstantUtil.NO_PERTANYAAN, true));
        //listheader_FeedbackAlumniDetailList_no.setSortDescending(new FieldComparator(ConstantUtil.NO_PERTANYAAN, false));
        //listheader_FeedbackAlumniDetailList_pertanyaan.setSortAscending(new FieldComparator(ConstantUtil.PERTANYAAN, true));
        //listheader_FeedbackAlumniDetailList_pertanyaan.setSortDescending(new FieldComparator(ConstantUtil.PERTANYAAN, false));
        //listheader_FeedbackAlumniDetailList_1.setSortAscending(new FieldComparator(ConstantUtil.JAWABAN, true));
        //listheader_FeedbackAlumniDetailList_1.setSortDescending(new FieldComparator(ConstantUtil.JAWABAN, false));

        // ++ create the searchObject and init sorting ++//
        so = new HibernateSearchObject<Mfeedback>(Mfeedback.class, getCountRows());
        so.addFilter(new Filter(ConstantUtil.JENIS_FEEDBACK, Codec.KodeFeedback.A.getValue().charAt(0), Filter.OP_EQUAL));
        so.addSort(ConstantUtil.NO_PERTANYAAN, false);
        searchObj = new HibernateSearchObject<Tfeedbackalumni>(Tfeedbackalumni.class, getCountRows());
        if(getSelectedFeedbackAlumni().getMmahasiswa()!=null) {
            searchObj.addFilter(new Filter(ConstantUtil.MAHASISWA_DOT_NIM, getSelectedFeedbackAlumni().getMmahasiswa().getCnim(), Filter.OP_EQUAL));
        }
        searchObj.addSort(ConstantUtil.NO_PERTANYAAN, false);
        // Change the BindingListModel.
        if (getBinder() != null) {

            /*getPagedBindingListFeedbackWrapper().init(so, getListBoxFeedbackAlumniDetail(), paging_FeedbackList);
            BindingListModelList blml = (BindingListModelList) getListBoxFeedbackAlumniDetail().getModel();
            setFeedbacks(blml);*/
            getPagedBindingListWrapper().init(searchObj, getListBoxFeedbackAlumniDetail(), paging_FeedbackAlumniDetailList);
            try{
                getPagedBindingListWrapper().addAll(getSelectedFeedbackAlumniList());
            } catch (NullPointerException e) {}
            //pagedBindingListWrapper = new PagedBindingListWrapper<Tfeedbackalumni>();
            //pagedBindingListFeedbackWrapper.addAll(getSelectedFeedbackAlumniList());

            BindingListModelList lml = (BindingListModelList) getListBoxFeedbackAlumniDetail().getModel();

            setFeedbackAlumnis(lml);
            if (getSelectedFeedbackAlumni() == null) {
                // init the bean with the first record in the List
                if (lml.getSize() > 0) {
                    final int rowIndex = 0;
                    getListBoxFeedbackDetailAlumni().setSelectedIndex(rowIndex);
                    setSelectedFeedbackAlumni((Tfeedbackalumni) lml.get(0));
                    Events.sendEvent(new Event("onSelect", getListBoxFeedbackDetailAlumni(), getSelectedFeedbackAlumni()));
                }
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
        btnSearchProdiExtended.setDisabled(b);
        btnSearchSekolahExtended.setDisabled(b);
        btnSearchAlumniExtended.setDisabled(b);
        btnSearchTermExtended.setDisabled(b);
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
            txtb_nim.setValue(malumni.getMmahasiswa().getCnim());
            txtb_nmalumni.setValue(malumni.getMmahasiswa().getCnama());
            Tfeedbackalumni afeedback = getFeedbackAlumni();
            afeedback.setMmahasiswa(malumni.getMmahasiswa());
            setFeedbackAlumni(afeedback);
        }
    }

    public void onClick$btnSearchSekolahExtended(Event event) {
        doSearchSekolahExtended(event);
    }

    private void doSearchSekolahExtended(Event event) {
        Msekolah sekolah = SekolahExtendedSearchListBox.show(windowFeedbackAlumniDetail);

        if (sekolah != null) {
            txtb_sekolah.setValue(sekolah.getCnamaSekolah());
            Tfeedbackalumni obj = getFeedbackAlumni();
            obj.setMsekolah(sekolah);
            setFeedbackAlumni(obj);
        }
    }

    public void onClick$btnSearchProdiExtended(Event event) {
        doSearchProdiExtended(event);
    }

    private void doSearchProdiExtended(Event event) {
        Mprodi prodi = ProdiExtendedSearchListBox.show(windowFeedbackAlumniDetail);

        if (prodi != null) {
            txtb_prodi.setValue(prodi.getCnmprogst());
            Tfeedbackalumni obj = getFeedbackAlumni();
            obj.setMprodi(prodi);
            setFeedbackAlumni(obj);
        }
    }

     public void onClick$btnSearchTermExtended(Event event) {
        doSearchTermExtended(event);
    }

    private void doSearchTermExtended(Event event) {
        Mterm term = TermExtendedSearchListBox.show(windowFeedbackAlumniDetail);

        if (term != null) {
            txtb_term.setValue(term.getKdTerm());
            Tfeedbackalumni obj = getFeedbackAlumni();
            obj.setCterm(term.getKdTerm());
            setFeedbackAlumni(obj);
        }
    }

    public void onChange$txtb_jawaban() {
        if (txtb_jawaban.getValue() != null) {
            Tfeedbackalumni obj = getFeedbackAlumni();
            obj.setCjawaban(txtb_jawaban.getValue());
            setFeedbackAlumni(obj);
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

    public List<Tfeedbackalumni> getFeedbackAlumniList() {
        // STORED IN THE module's MainController
        return getFeedbackAlumniMainCtrl().getSelectedFeedbackAlumniList();
    }

    public void setFeedbackAlumniList(List<Tfeedbackalumni> list) {
        // STORED IN THE module's MainController
        getFeedbackAlumniMainCtrl().setSelectedFeedbackAlumniList(list);
    }

    public Tfeedbackalumni getSelectedFeedbackAlumni() {
        // STORED IN THE module's MainController
        return getFeedbackAlumniMainCtrl().getSelectedFeedbackAlumni();
    }

    public void setSelectedFeedbackAlumni(Tfeedbackalumni selectedFeedbackAlumni) {
        // STORED IN THE module's MainController
        getFeedbackAlumniMainCtrl().setSelectedFeedbackAlumni(selectedFeedbackAlumni);
    }

    public List<Tfeedbackalumni> getSelectedFeedbackAlumniList() {
        // STORED IN THE module's MainController
        return getFeedbackAlumniMainCtrl().getSelectedFeedbackAlumniList();
    }

    public void setSelectedFeedbackAlumniList(List<Tfeedbackalumni> selectedFeedbackAlumniList) {
        // STORED IN THE module's MainController
        getFeedbackAlumniMainCtrl().setSelectedFeedbackAlumniList(selectedFeedbackAlumniList);
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

    public void setSearchObj(HibernateSearchObject<Tfeedbackalumni> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Tfeedbackalumni> getSearchObj() {
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


    private PagedListWrapper<Mfeedback> pagedListFeedbackWrapper;
    private PagedListWrapper<Tfeedbackalumni> pagedListWrapper;

    private PagedBindingListWrapper<Mfeedback> pagedBindingListFeedbackWrapper;
    private PagedBindingListWrapper<Tfeedbackalumni> pagedBindingListWrapper;

    public PagedListWrapper<Mfeedback> getPagedListFeedbackWrapper() {
        return pagedListFeedbackWrapper;
    }

    public void setPagedListFeedbackWrapper(PagedListWrapper<Mfeedback> pagedListFeedbackWrapper) {
        this.pagedListFeedbackWrapper = pagedListFeedbackWrapper;
    }
    public PagedListWrapper<Tfeedbackalumni> getPagedListWrapper() {
        return pagedListWrapper;
    }

    public void setPagedListWrapper(PagedListWrapper<Tfeedbackalumni> pagedListWrapper) {
        this.pagedListWrapper = pagedListWrapper;
    }

    public void setPagedBindingListFeedbackWrapper(PagedBindingListWrapper<Mfeedback> pagedBindingListFeedbackWrapper) {
        this.pagedBindingListFeedbackWrapper = pagedBindingListFeedbackWrapper;
    }

    public PagedBindingListWrapper<Mfeedback> getPagedBindingListFeedbackWrapper() {
        return pagedBindingListFeedbackWrapper;
    }
    public void setPagedBindingListWrapper(PagedBindingListWrapper<Tfeedbackalumni> pagedBindingListWrapper) {
        this.pagedBindingListWrapper = pagedBindingListWrapper;
    }

    public PagedBindingListWrapper<Tfeedbackalumni> getPagedBindingListWrapper() {
        return pagedBindingListWrapper;
    }
}
