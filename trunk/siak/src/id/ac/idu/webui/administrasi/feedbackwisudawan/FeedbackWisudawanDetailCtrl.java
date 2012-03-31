package id.ac.idu.webui.administrasi.feedbackwisudawan;

import com.trg.search.Filter;
import id.ac.idu.administrasi.service.FeedbackWisudawanService;
import id.ac.idu.backend.model.*;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.util.Codec;
import id.ac.idu.util.ConstantUtil;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.pagging.PagedBindingListWrapper;
import id.ac.idu.webui.util.pagging.PagedListWrapper;
import id.ac.idu.webui.util.searchdialogs.MahasiswaExtendedSearchListBox;
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
public class FeedbackWisudawanDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(FeedbackWisudawanDetailCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowFeedbackWisudawanDetail; // autowired

    protected Borderlayout borderlayout_FeedbackWisudawanDetail; // autowired

    protected Textbox txtb_term; // autowired
    protected Textbox txtb_nim; // autowired
    protected Textbox txtb_pertanyaan; // autowired
    protected Textbox txtb_jawaban;
    protected Textbox txtb_nmwisudawan; // autowired
    protected Textbox txtb_kelompok; // autowired
    protected Button button_FeedbackWisudawanDialog_PrintFeedbackWisudawan; // autowired
    protected Button btnSearchWisudawanExtended;
    protected Textbox txtb_prodi;
    protected Button btnSearchProdiExtended;
    protected Button btnSearchTermExtended;
    protected Textbox txtb_sekolah;
    protected Button btnSearchSekolahExtended;
    protected Listbox list_jenis;
    protected Bandbox cmb_jenis;

    protected Paging paging_FeedbackWisudawanDetailList;
    protected Paging paging_FeedbackList;
    protected Listbox listBoxFeedbackWisudawanDetail;
    protected Listheader listheader_FeedbackWisudawanDetailList_no; // autowired
    protected Listheader listheader_FeedbackWisudawanDetailList_pertanyaan; // autowired
    protected Listheader listheader_FeedbackWisudawanDetailList_1; // autowired
    protected Listheader listheader_FeedbackWisudawanDetailList_2; // autowired
    protected Listheader listheader_FeedbackWisudawanDetailList_3; // autowired
    protected Listheader listheader_FeedbackWisudawanDetailList_4; // autowired
    protected Listheader listheader_FeedbackWisudawanDetailList_5; // autowired

    // Databinding
    protected transient AnnotateDataBinder binder;
    private FeedbackWisudawanMainCtrl feedbackWisudawanMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient FeedbackWisudawanService feedbackWisudawanService;

    // row count for listbox
    private int countRows;

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Tfeedbackwisudawan> searchObj;
    private HibernateSearchObject<Mfeedback> so;

    /**
     * default constructor.<br>
     */
    public FeedbackWisudawanDetailCtrl() {
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
            setFeedbackWisudawanMainCtrl((FeedbackWisudawanMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getFeedbackWisudawanMainCtrl().setFeedbackWisudawanDetailCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getFeedbackWisudawanMainCtrl().getSelectedFeedbackWisudawan() != null) {
                setSelectedFeedbackWisudawan(getFeedbackWisudawanMainCtrl().getSelectedFeedbackWisudawan());
            } else {
                setSelectedFeedbackWisudawan(null);
            }
            if (getFeedbackWisudawanMainCtrl().getSelectedFeedbackWisudawanList() != null) {
                setSelectedFeedbackWisudawanList(getFeedbackWisudawanMainCtrl().getSelectedFeedbackWisudawanList());
            } else {
                setSelectedFeedbackWisudawanList(null);
            }
        } else {
            setSelectedFeedbackWisudawan(null);
            setSelectedFeedbackWisudawanList(null);
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
    public void onCreate$windowFeedbackWisudawanDetail(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        /*GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.JenisInstansi.class)).getEnumToList(),
                list_jenis, cmb_jenis, (getFeedbackWisudawan() != null)?getFeedbackWisudawan().getCjnsinstansi():null);*/
        doFillListbox();
        binder.loadAll();

        doFitSize(event);
    }
    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_FeedbackWisudawanDetailList.setPageSize(getCountRows());
        paging_FeedbackWisudawanDetailList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_FeedbackWisudawanDetailList_no.setSortAscending(new FieldComparator(ConstantUtil.NO_PERTANYAAN, true));
        listheader_FeedbackWisudawanDetailList_no.setSortDescending(new FieldComparator(ConstantUtil.NO_PERTANYAAN, false));
        listheader_FeedbackWisudawanDetailList_pertanyaan.setSortAscending(new FieldComparator(ConstantUtil.PERTANYAAN, true));
        listheader_FeedbackWisudawanDetailList_pertanyaan.setSortDescending(new FieldComparator(ConstantUtil.PERTANYAAN, false));
        listheader_FeedbackWisudawanDetailList_1.setSortAscending(new FieldComparator(ConstantUtil.JAWABAN, true));
        listheader_FeedbackWisudawanDetailList_1.setSortDescending(new FieldComparator(ConstantUtil.JAWABAN, false));

        // ++ create the searchObject and init sorting ++//
        so = new HibernateSearchObject<Mfeedback>(Mfeedback.class, getCountRows());
        so.addFilter(new Filter(ConstantUtil.JENIS_FEEDBACK, Codec.KodeFeedback.A.getValue().charAt(0), Filter.OP_EQUAL));
        so.addSort(ConstantUtil.NO_PERTANYAAN, false);
        searchObj = new HibernateSearchObject<Tfeedbackwisudawan>(Tfeedbackwisudawan.class, getCountRows());
        if(getSelectedFeedbackWisudawan().getMmahasiswa()!=null) {
            searchObj.addFilter(new Filter(ConstantUtil.MAHASISWA_DOT_NIM, getSelectedFeedbackWisudawan().getMmahasiswa().getCnim(), Filter.OP_EQUAL));
        }
        searchObj.addSort(ConstantUtil.NO_PERTANYAAN, false);
        // Change the BindingListModel.
        if (getBinder() != null) {

            /*getPagedBindingListFeedbackWrapper().init(so, getListBoxFeedbackWisudawanDetail(), paging_FeedbackList);
            BindingListModelList blml = (BindingListModelList) getListBoxFeedbackWisudawanDetail().getModel();
            setFeedbacks(blml);*/
            getPagedBindingListWrapper().init(searchObj, getListBoxFeedbackWisudawanDetail(), paging_FeedbackWisudawanDetailList);
            try{
                getPagedBindingListWrapper().addAll(getSelectedFeedbackWisudawanList());
            } catch (NullPointerException e) {}
            //pagedBindingListWrapper = new PagedBindingListWrapper<Tfeedbackwisudawan>();
            //pagedBindingListFeedbackWrapper.addAll(getSelectedFeedbackWisudawanList());

            BindingListModelList lml = (BindingListModelList) getListBoxFeedbackWisudawanDetail().getModel();

            setFeedbackWisudawans(lml);
            if (getSelectedFeedbackWisudawan() == null) {
                // init the bean with the first record in the List
                if (lml.getSize() > 0) {
                    final int rowIndex = 0;
                    getListBoxFeedbackDetailWisudawan().setSelectedIndex(rowIndex);
                    setSelectedFeedbackWisudawan((Tfeedbackwisudawan) lml.get(0));
                    Events.sendEvent(new Event("onSelect", getListBoxFeedbackDetailWisudawan(), getSelectedFeedbackWisudawan()));
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
        //borderLayout_feedbackWisudawanList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        //-- windowFeedbackWisudawanList.invalidate();
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
        borderlayout_FeedbackWisudawanDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowFeedbackWisudawanDetail.invalidate();
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
        btnSearchWisudawanExtended.setDisabled(b);
        btnSearchTermExtended.setDisabled(b);
    }

      /**
     * If the Button 'Search Branch ExtendedSearch' is clicked.<br>
     *
     * @param event
     */
    public void onClick$btnSearchWisudawanExtended(Event event) {
        doSearchWisudawanExtended(event);
    }

    /**
     * Opens the Search and Get Dialog for Branches.<br>
     * It appends/changes the branch object for the current bean.<br>
     *
     * @param event
     */
    private void doSearchWisudawanExtended(Event event) {
        Mmahasiswa mahasiswa = MahasiswaExtendedSearchListBox.show(windowFeedbackWisudawanDetail);

        if (mahasiswa != null) {
            txtb_nim.setValue(mahasiswa.getCnim());
            txtb_nmwisudawan.setValue(mahasiswa.getCnama());
            Tfeedbackwisudawan afeedback = getFeedbackWisudawan();
            afeedback.setMmahasiswa(mahasiswa);
            setFeedbackWisudawan(afeedback);
        }
    }

    public void onClick$btnSearchSekolahExtended(Event event) {
        doSearchSekolahExtended(event);
    }

    private void doSearchSekolahExtended(Event event) {
        Msekolah sekolah = SekolahExtendedSearchListBox.show(windowFeedbackWisudawanDetail);

        if (sekolah != null) {
            txtb_sekolah.setValue(sekolah.getCnamaSekolah());
            Tfeedbackwisudawan obj = getFeedbackWisudawan();
            obj.setMsekolah(sekolah);
            setFeedbackWisudawan(obj);
        }
    }

    public void onClick$btnSearchProdiExtended(Event event) {
        doSearchProdiExtended(event);
    }

    private void doSearchProdiExtended(Event event) {
        Mprodi prodi = ProdiExtendedSearchListBox.show(windowFeedbackWisudawanDetail);

        if (prodi != null) {
            txtb_prodi.setValue(prodi.getCnmprogst());
            Tfeedbackwisudawan obj = getFeedbackWisudawan();
            obj.setMprodi(prodi);
            setFeedbackWisudawan(obj);
        }
    }

       public void onClick$btnSearchTermExtended(Event event) {
        doSearchTermExtended(event);
    }

    private void doSearchTermExtended(Event event) {
        Mterm term = TermExtendedSearchListBox.show(windowFeedbackWisudawanDetail);

        if (term != null) {
            txtb_term.setValue(term.getKdTerm());
            Tfeedbackwisudawan obj = getFeedbackWisudawan();
            obj.setCterm(term.getKdTerm());
            setFeedbackWisudawan(obj);
        }
    }

    public void onChange$txtb_jawaban() {
        if (txtb_jawaban.getValue() != null) {
            Tfeedbackwisudawan obj = getFeedbackWisudawan();
            obj.setCjawaban(txtb_jawaban.getValue());
            setFeedbackWisudawan(obj);
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
    public Tfeedbackwisudawan getFeedbackWisudawan() {
        // STORED IN THE module's MainController
        return getFeedbackWisudawanMainCtrl().getSelectedFeedbackWisudawan();
    }

    public void setFeedbackWisudawan(Tfeedbackwisudawan anFeedbackWisudawan) {
        // STORED IN THE module's MainController
        getFeedbackWisudawanMainCtrl().setSelectedFeedbackWisudawan(anFeedbackWisudawan);
    }

    public List<Tfeedbackwisudawan> getFeedbackWisudawanList() {
        // STORED IN THE module's MainController
        return getFeedbackWisudawanMainCtrl().getSelectedFeedbackWisudawanList();
    }

    public void setFeedbackWisudawanList(List<Tfeedbackwisudawan> list) {
        // STORED IN THE module's MainController
        getFeedbackWisudawanMainCtrl().setSelectedFeedbackWisudawanList(list);
    }

    public Tfeedbackwisudawan getSelectedFeedbackWisudawan() {
        // STORED IN THE module's MainController
        return getFeedbackWisudawanMainCtrl().getSelectedFeedbackWisudawan();
    }

    public void setSelectedFeedbackWisudawan(Tfeedbackwisudawan selectedFeedbackWisudawan) {
        // STORED IN THE module's MainController
        getFeedbackWisudawanMainCtrl().setSelectedFeedbackWisudawan(selectedFeedbackWisudawan);
    }

    public List<Tfeedbackwisudawan> getSelectedFeedbackWisudawanList() {
        // STORED IN THE module's MainController
        return getFeedbackWisudawanMainCtrl().getSelectedFeedbackWisudawanList();
    }

    public void setSelectedFeedbackWisudawanList(List<Tfeedbackwisudawan> selectedFeedbackWisudawanList) {
        // STORED IN THE module's MainController
        getFeedbackWisudawanMainCtrl().setSelectedFeedbackWisudawanList(selectedFeedbackWisudawanList);
    }

    public BindingListModelList getFeedbackWisudawans() {
        // STORED IN THE module's MainController
        return getFeedbackWisudawanMainCtrl().getFeedbackWisudawans();
    }

    public void setFeedbackWisudawans(BindingListModelList Tfeedbackwisudawan) {
        // STORED IN THE module's MainController
        getFeedbackWisudawanMainCtrl().setFeedbackWisudawans(Tfeedbackwisudawan);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public void setFeedbackWisudawanService(FeedbackWisudawanService feedbackWisudawanService) {
        this.feedbackWisudawanService = feedbackWisudawanService;
    }

    public FeedbackWisudawanService getFeedbackWisudawanService() {
        return this.feedbackWisudawanService;
    }

    public void setFeedbackWisudawanMainCtrl(FeedbackWisudawanMainCtrl feedbackWisudawanMainCtrl) {
        this.feedbackWisudawanMainCtrl = feedbackWisudawanMainCtrl;
    }

    public FeedbackWisudawanMainCtrl getFeedbackWisudawanMainCtrl() {
        return this.feedbackWisudawanMainCtrl;
    }
    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public Listbox getListBoxFeedbackDetailWisudawan() {
        return this.listBoxFeedbackWisudawanDetail;
    }

    public void setListBoxFeedbackWisudawan(Listbox listBoxFeedbackDetailWisudawan) {
        this.listBoxFeedbackWisudawanDetail = listBoxFeedbackWisudawanDetail;
    }

    public void setSearchObj(HibernateSearchObject<Tfeedbackwisudawan> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Tfeedbackwisudawan> getSearchObj() {
        return this.searchObj;
    }

    public Listbox getListBoxFeedbackWisudawanDetail() {
        return this.listBoxFeedbackWisudawanDetail;
    }

    public void setListBoxFeedbackWisudawanDetail(Listbox listBoxFeedbackWisudawanDetail) {
        this.listBoxFeedbackWisudawanDetail = listBoxFeedbackWisudawanDetail;
    }

    public void setSelectedFeedback(Mfeedback selectedFeedback) {
        // STORED IN THE module's MainController
        getFeedbackWisudawanMainCtrl().setSelectedFeedback(selectedFeedback);
    }

    public Mfeedback getSelectedFeedback() {
        // STORED IN THE module's MainController
        return getFeedbackWisudawanMainCtrl().getSelectedFeedback();
    }

    public void setFeedbacks(BindingListModelList feedbacks) {
        // STORED IN THE module's MainController
        getFeedbackWisudawanMainCtrl().setFeedbacks(feedbacks);
    }

    public BindingListModelList getFeedbacks() {
        // STORED IN THE module's MainController
        return getFeedbackWisudawanMainCtrl().getFeedbacks();
    }


    private PagedListWrapper<Mfeedback> pagedListFeedbackWrapper;
    private PagedListWrapper<Tfeedbackwisudawan> pagedListWrapper;

    private PagedBindingListWrapper<Mfeedback> pagedBindingListFeedbackWrapper;
    private PagedBindingListWrapper<Tfeedbackwisudawan> pagedBindingListWrapper;

    public PagedListWrapper<Mfeedback> getPagedListFeedbackWrapper() {
        return pagedListFeedbackWrapper;
    }

    public void setPagedListFeedbackWrapper(PagedListWrapper<Mfeedback> pagedListFeedbackWrapper) {
        this.pagedListFeedbackWrapper = pagedListFeedbackWrapper;
    }
    public PagedListWrapper<Tfeedbackwisudawan> getPagedListWrapper() {
        return pagedListWrapper;
    }

    public void setPagedListWrapper(PagedListWrapper<Tfeedbackwisudawan> pagedListWrapper) {
        this.pagedListWrapper = pagedListWrapper;
    }

    public void setPagedBindingListFeedbackWrapper(PagedBindingListWrapper<Mfeedback> pagedBindingListFeedbackWrapper) {
        this.pagedBindingListFeedbackWrapper = pagedBindingListFeedbackWrapper;
    }

    public PagedBindingListWrapper<Mfeedback> getPagedBindingListFeedbackWrapper() {
        return pagedBindingListFeedbackWrapper;
    }
    public void setPagedBindingListWrapper(PagedBindingListWrapper<Tfeedbackwisudawan> pagedBindingListWrapper) {
        this.pagedBindingListWrapper = pagedBindingListWrapper;
    }

    public PagedBindingListWrapper<Tfeedbackwisudawan> getPagedBindingListWrapper() {
        return pagedBindingListWrapper;
    }
}
