package id.ac.idu.webui.administrasi.feedbackdosen;

import com.trg.search.Filter;
import id.ac.idu.administrasi.service.FeedbackDosenService;
import id.ac.idu.administrasi.service.MfeedbackService;
import id.ac.idu.backend.model.*;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.util.Codec;
import id.ac.idu.util.ConstantUtil;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.ListBoxUtil;
import id.ac.idu.webui.util.ZksampleMessageUtils;
import id.ac.idu.webui.util.pagging.PagedBindingListWrapper;
import id.ac.idu.webui.util.pagging.PagedListWrapper;
import id.ac.idu.webui.util.searchdialogs.PegawaiExtendedSearchListBox;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 3/21/12
 * Time: 4:07 AM
 */
public class FeedbackDosenDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(FeedbackDosenDetailCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowFeedbackDosenDetail; // autowired

    protected Borderlayout borderlayout_FeedbackDosenDetail; // autowired

    protected Textbox txtb_term; // autowired
    protected Textbox txtb_nip; // autowired
    protected Textbox txtb_pertanyaan; // autowired
    protected Textbox txtb_jawaban;
    protected Textbox txtb_nmdosen; // autowired
    protected Textbox txtb_kelompok; // autowired
    protected Button button_FeedbackDosenDialog_PrintFeedbackDosen; // autowired
    protected Button btnSearchDosenExtended;
    protected Textbox txtb_prodi;
    protected Button btnSearchProdiExtended;
    protected Button btnSearchTermExtended;
    protected Textbox txtb_sekolah;
    protected Button btnSearchSekolahExtended;
    protected Listbox list_jenis;
    protected Bandbox cmb_jenis;

    protected Paging paging_FeedbackDosenDetailList;
    protected Paging paging_FeedbackList;
    protected Listbox listBoxFeedbackDosenDetail;
    protected Listheader listheader_FeedbackDosenDetailList_no; // autowired
    protected Listheader listheader_FeedbackDosenDetailList_pertanyaan; // autowired
    protected Listheader listheader_FeedbackDosenDetailList_1; // autowired
    protected Listheader listheader_FeedbackDosenDetailList_2; // autowired
    protected Listheader listheader_FeedbackDosenDetailList_3; // autowired
    protected Listheader listheader_FeedbackDosenDetailList_4; // autowired
    protected Listheader listheader_FeedbackDosenDetailList_5; // autowired

    // Databinding
    protected transient AnnotateDataBinder binder;
    private FeedbackDosenMainCtrl feedbackDosenMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient FeedbackDosenService feedbackDosenService;

    // row count for listbox
    private int countRows;

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Tfeedbackdosen> searchObj;
    private HibernateSearchObject<Mfeedback> so;

      public MfeedbackService getMfeedbackService() {
        return mfeedbackService;
    }

    public void setMfeedbackService(MfeedbackService mfeedbackService) {
        this.mfeedbackService = mfeedbackService;
    }

    private MfeedbackService mfeedbackService;

    /**
     * default constructor.<br>
     */
    public FeedbackDosenDetailCtrl() {
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
            setFeedbackDosenMainCtrl((FeedbackDosenMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getFeedbackDosenMainCtrl().setFeedbackDosenDetailCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getFeedbackDosenMainCtrl().getSelectedFeedbackDosen() != null) {
                setSelectedFeedbackDosen(getFeedbackDosenMainCtrl().getSelectedFeedbackDosen());
            } else {
                setSelectedFeedbackDosen(null);
            }
            if (getFeedbackDosenMainCtrl().getSelectedFeedbackDosenList() != null) {
                setSelectedFeedbackDosenList(getFeedbackDosenMainCtrl().getSelectedFeedbackDosenList());
            } else {
                setSelectedFeedbackDosenList(null);
            }
        } else {
            setSelectedFeedbackDosen(null);
            setSelectedFeedbackDosenList(null);
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
    public void onCreate$windowFeedbackDosenDetail(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        /*GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.JenisInstansi.class)).getEnumToList(),
                list_jenis, cmb_jenis, (getFeedbackDosen() != null)?getFeedbackDosen().getCjnsinstansi():null);*/
        doFillListbox();
        binder.loadAll();

        doFitSize(event);
    }
    public void doFillListbox() {

        doFitSize();

        // set the paging params
//        paging_FeedbackDosenDetailList.setPageSize(getCountRows());
//        paging_FeedbackDosenDetailList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
//        listheader_FeedbackDosenDetailList_no.setSortAscending(new FieldComparator(ConstantUtil.NO_PERTANYAAN, true));
//        listheader_FeedbackDosenDetailList_no.setSortDescending(new FieldComparator(ConstantUtil.NO_PERTANYAAN, false));
//        listheader_FeedbackDosenDetailList_pertanyaan.setSortAscending(new FieldComparator(ConstantUtil.PERTANYAAN, true));
//        listheader_FeedbackDosenDetailList_pertanyaan.setSortDescending(new FieldComparator(ConstantUtil.PERTANYAAN, false));
//        listheader_FeedbackDosenDetailList_1.setSortAscending(new FieldComparator(ConstantUtil.JAWABAN, true));
//        listheader_FeedbackDosenDetailList_1.setSortDescending(new FieldComparator(ConstantUtil.JAWABAN, false));

        // ++ create the searchObject and init sorting ++//
        so = new HibernateSearchObject<Mfeedback>(Mfeedback.class, getCountRows());
        so.addFilter(new Filter(ConstantUtil.JENIS_FEEDBACK, Codec.KodeFeedback.D.getValue().charAt(0), Filter.OP_EQUAL));
        so.addSort(ConstantUtil.NO_PERTANYAAN, false);
        searchObj = new HibernateSearchObject<Tfeedbackdosen>(Tfeedbackdosen.class, getCountRows());
        if(getSelectedFeedbackDosen().getMpegawai()!=null) {
            searchObj.addFilter(new Filter(ConstantUtil.PEGAWAI_DOT_NIP, getSelectedFeedbackDosen().getMpegawai().getCnip(), Filter.OP_EQUAL));
        }
        searchObj.addSort(ConstantUtil.NO_PERTANYAAN, false);
        // Change the BindingListModel.
        if (getBinder() != null) {


            setRadioOnListBox(getFeedbackDosenMainCtrl().getList(getSelectedFeedbackDosen()));

            try{
                getPagedBindingListWrapper().clear();
                 getPagedBindingListWrapper().addAll(getFeedbackDosenMainCtrl().getList(getSelectedFeedbackDosen()));
            } catch (NullPointerException e) {}

            BindingListModelList lml = (BindingListModelList) getListBoxFeedbackDosenDetail().getModel();

            setFeedbackDosens(lml);
            if (getSelectedFeedbackDosen() == null) {
                // init the bean with the first record in the List
                if (lml.getSize() > 0) {
                    final int rowIndex = 0;
                    getListBoxFeedbackDetailDosen().setSelectedIndex(rowIndex);
                    setSelectedFeedbackDosen((Tfeedbackdosen) lml.get(0));
                    Events.sendEvent(new Event("onSelect", getListBoxFeedbackDetailDosen(), getSelectedFeedbackDosen()));
                }
            }
        }

    }

     public  void setRadioOnListBox(List<Tfeedbackdosen> lfa){

        ListBoxUtil.resetList(listBoxFeedbackDosenDetail);

        if (lfa.size()==0)  {
            try {
                initJawaban();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        for (int i=0; i < lfa.size();i++){
            //get a list item

            Tfeedbackdosen fa = new Tfeedbackdosen();
            fa = (Tfeedbackdosen) lfa.get(i);
              Listitem ltm = new Listitem();

            ltm.setParent(listBoxFeedbackDosenDetail);
            Listcell listcell = new Listcell();
            listcell.setLabel(String.valueOf(fa.getMfeedback().getNnopertanyaan()));
			listcell.setParent(ltm);
			listcell = new Listcell(fa.getMfeedback().getCpertanyaan());
			listcell.setParent(ltm);
            listcell = new Listcell();
            Radiogroup radiogroup = new Radiogroup();
            radiogroup.setId(String.valueOf(fa.getMfeedback().getId()));
            Radio radio1 = new Radio();
            radio1.setId(String.valueOf(fa.getId())+"1");
            radio1.setRadiogroup(radiogroup);
            radio1.setParent(radiogroup);
            radio1.setValue("1");
            radio1.setLabel("1");
            if (fa.getCjawaban()!=null)
                if (fa.getCjawaban().equals("1")) {
                         radio1.setChecked(true);
                }
            //radio1.setParent(listcell);
            Radio radio2 = new Radio();
            radio2.setId(String.valueOf(fa.getId())+"2");
            radio2.setRadiogroup(radiogroup);
            radio2.setParent(radiogroup);
            radio2.setValue("2");
            radio2.setLabel("2");
            if (fa.getCjawaban()!=null)
                if (fa.getCjawaban().equals("2")) {
                         radio2.setChecked(true);
                }
            //radio2.setParent(listcell);
             Radio radio3 = new Radio();
            radio3.setId(String.valueOf(fa.getId())+"3");
            radio3.setRadiogroup(radiogroup);
            radio3.setParent(radiogroup);
            radio3.setValue("3");
            radio3.setLabel("3");
            if (fa.getCjawaban()!=null)
                if (fa.getCjawaban().equals("3")) {
                         radio3.setChecked(true);
                }
            //radio3.setParent(listcell);
            Radio radio4 = new Radio();
            radio4.setId(String.valueOf(fa.getId())+"4");
            radio4.setRadiogroup(radiogroup);
            radio4.setParent(radiogroup);
            radio4.setValue("4");
            radio4.setLabel("4");
            if (fa.getCjawaban()!=null)
                if (fa.getCjawaban().equals("4")) {
                         radio4.setChecked(true);
                }
            //radio4.setParent(listcell);
            Radio radio5 = new Radio();
            radio5.setId(String.valueOf(fa.getId())+"5");
            radio5.setRadiogroup(radiogroup);
            radio5.setParent(radiogroup);
            radio5.setValue("5");
            radio5.setLabel("5");
            if (fa.getCjawaban()!=null)
                if (fa.getCjawaban().equals("5")) {
                         radio5.setChecked(true);
                }
            radiogroup.setParent(listcell);
			listcell.setParent(ltm);
        }
            setListBoxFeedbackDosen(null);
            setListBoxFeedbackDosen(listBoxFeedbackDosenDetail);
    }

     public void initJawaban() throws InterruptedException {
     ListBoxUtil.resetList(listBoxFeedbackDosenDetail);

        List<Mfeedback> lfa =   mfeedbackService.getAllMfeedbackByType(Codec.KodeFeedback.D.getName());
        if (lfa.size()==0)   {
            ZksampleMessageUtils.showErrorMessage("Pertanyaan belum di buat !");
        }

        for (int i=0; i < lfa.size();i++){
            //get a list item

            Mfeedback fa = new Mfeedback();
            fa = (Mfeedback) lfa.get(i);

            Listitem ltm = new Listitem();

            ltm.setParent(listBoxFeedbackDosenDetail);
            Listcell listcell = new Listcell();
            listcell.setLabel(String.valueOf(fa.getNnopertanyaan()));
			listcell.setParent(ltm);
			listcell = new Listcell(fa.getCpertanyaan());
			listcell.setParent(ltm);
            listcell = new Listcell();
            Radiogroup radiogroup = new Radiogroup();
            radiogroup.setId(String.valueOf(fa.getId()));
            Radio radio1 = new Radio();
            radio1.setId(String.valueOf(fa.getId())+"1");
            radio1.setRadiogroup(radiogroup);
            radio1.setParent(radiogroup);
            radio1.setValue("1");
            radio1.setLabel("1");

            //radio1.setParent(listcell);
            Radio radio2 = new Radio();
            radio2.setId(String.valueOf(fa.getId())+"2");
            radio2.setRadiogroup(radiogroup);
            radio2.setParent(radiogroup);
            radio2.setValue("2");
            radio2.setLabel("2");

            //radio2.setParent(listcell);
            Radio radio3 = new Radio();
            radio3.setId(String.valueOf(fa.getId())+"3");
            radio3.setRadiogroup(radiogroup);
            radio3.setParent(radiogroup);
            radio3.setValue("3");
            radio3.setLabel("3");

           // radio3.setParent(listcell);
            Radio radio4 = new Radio();
            radio4.setId(String.valueOf(fa.getId())+"4");
            radio4.setRadiogroup(radiogroup);
            radio4.setParent(radiogroup);
            radio4.setValue("4");
            radio4.setLabel("4");

            //radio4.setParent(listcell);
            Radio radio5 = new Radio();
            radio5.setId(String.valueOf(fa.getId())+"5");
            radio5.setRadiogroup(radiogroup);
            radio5.setParent(radiogroup);
            radio5.setValue("5");
            radio5.setLabel("5");

            //radio5.setParent(listcell);
            radiogroup.setParent(listcell);
			listcell.setParent(ltm);
        }
           setListBoxFeedbackDosen(null);
           setListBoxFeedbackDosen(listBoxFeedbackDosenDetail);
    }

    public List<Tfeedbackdosen> setlbtolist(List<Tfeedbackdosen> getList)  {
        List<Tfeedbackdosen> lfa =  new ArrayList<Tfeedbackdosen>();
        lfa = getList;
        for (int i=0;i < lfa.size();i++){
             ((Tfeedbackdosen)  lfa.get(i)).setCjawaban(getJawabanfromlb(((Tfeedbackdosen)  lfa.get(i)).getMfeedback().getId()));
        }
        return lfa;
    }

    public String getJawabanfromlb(int id)   {
        String jawaban="";
        String strId=String.valueOf(id);;
        for (int i=0;i < listBoxFeedbackDosenDetail.getItems().size();i++){
            Listitem li = listBoxFeedbackDosenDetail.getItemAtIndex(i);
            Listcell lc = (Listcell) li.getChildren().get(2);
            Radiogroup rg = (Radiogroup) lc.getChildren().get(0);
            if (rg.getId().equals(strId)){
               for (int j=0;j < rg.getChildren().size();j++){
                   Radio rad = (Radio) rg.getChildren().get(j);
                   if (rad.isSelected()) {
                       jawaban = ""+ (j + 1);
                   }
               }
            }

        }
        return jawaban;
    }

    public void doFitSize() {
        // normally 0 ! Or we have a i.e. a toolBar on top of the listBox.
        final int specialSize = 5;
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
        final int maxListBoxHeight = height - specialSize - 148;
        setCountRows((int) Math.round(maxListBoxHeight / 17.7));
        //borderLayout_feedbackDosenList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        //-- windowFeedbackDosenList.invalidate();
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
        borderlayout_FeedbackDosenDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowFeedbackDosenDetail.invalidate();
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
        btnSearchDosenExtended.setDisabled(b);
        btnSearchTermExtended.setDisabled(b);
    }

      /**
     * If the Button 'Search Branch ExtendedSearch' is clicked.<br>
     *
     * @param event
     */
    public void onClick$btnSearchDosenExtended(Event event) {
        doSearchDosenExtended(event);
    }

    /**
     * Opens the Search and Get Dialog for Branches.<br>
     * It appends/changes the branch object for the current bean.<br>
     *
     * @param event
     */
    private void doSearchDosenExtended(Event event) {
        Mpegawai pegawai = PegawaiExtendedSearchListBox.show(windowFeedbackDosenDetail);

        if (pegawai != null) {
            txtb_nip.setValue(pegawai.getCnip());
            txtb_nmdosen.setValue(pegawai.getCnama());
            Tfeedbackdosen afeedback = getFeedbackDosen();
            afeedback.setMpegawai(pegawai);
            setFeedbackDosen(afeedback);
        }
    }

    public void onClick$btnSearchSekolahExtended(Event event) {
        doSearchSekolahExtended(event);
    }

    private void doSearchSekolahExtended(Event event) {
        Msekolah sekolah = SekolahExtendedSearchListBox.show(windowFeedbackDosenDetail);

        if (sekolah != null) {
            txtb_sekolah.setValue(sekolah.getCnamaSekolah());
            Tfeedbackdosen obj = getFeedbackDosen();
            obj.setMsekolah(sekolah);
            setFeedbackDosen(obj);
        }
    }

    public void onClick$btnSearchProdiExtended(Event event) {
        doSearchProdiExtended(event);
    }

    private void doSearchProdiExtended(Event event) {
        Mprodi prodi = ProdiExtendedSearchListBox.show(windowFeedbackDosenDetail);

        if (prodi != null) {
            txtb_prodi.setValue(prodi.getCnmprogst());
            Tfeedbackdosen obj = getFeedbackDosen();
            obj.setMprodi(prodi);
            setFeedbackDosen(obj);
        }
    }

      public void onClick$btnSearchTermExtended(Event event) {
        doSearchTermExtended(event);
    }

    private void doSearchTermExtended(Event event) {
        Mterm term = TermExtendedSearchListBox.show(windowFeedbackDosenDetail);

        if (term != null) {
            txtb_term.setValue(term.getKdTerm());
            Tfeedbackdosen obj = getFeedbackDosen();
            obj.setCterm(term.getKdTerm());
            setFeedbackDosen(obj);
        }
    }

    public void onChange$txtb_jawaban() {
        if (txtb_jawaban.getValue() != null) {
            Tfeedbackdosen obj = getFeedbackDosen();
            obj.setCjawaban(txtb_jawaban.getValue());
            setFeedbackDosen(obj);
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
    public Tfeedbackdosen getFeedbackDosen() {
        // STORED IN THE module's MainController
        return getFeedbackDosenMainCtrl().getSelectedFeedbackDosen();
    }

    public void setFeedbackDosen(Tfeedbackdosen anFeedbackDosen) {
        // STORED IN THE module's MainController
        getFeedbackDosenMainCtrl().setSelectedFeedbackDosen(anFeedbackDosen);
    }

    public List<Tfeedbackdosen> getFeedbackDosenList() {
        // STORED IN THE module's MainController
        return getFeedbackDosenMainCtrl().getSelectedFeedbackDosenList();
    }

    public void setFeedbackDosenList(List<Tfeedbackdosen> list) {
        // STORED IN THE module's MainController
        getFeedbackDosenMainCtrl().setSelectedFeedbackDosenList(list);
    }

    public Tfeedbackdosen getSelectedFeedbackDosen() {
        // STORED IN THE module's MainController
        return getFeedbackDosenMainCtrl().getSelectedFeedbackDosen();
    }

    public void setSelectedFeedbackDosen(Tfeedbackdosen selectedFeedbackDosen) {
        // STORED IN THE module's MainController
        getFeedbackDosenMainCtrl().setSelectedFeedbackDosen(selectedFeedbackDosen);
    }

    public List<Tfeedbackdosen> getSelectedFeedbackDosenList() {
        // STORED IN THE module's MainController
        return getFeedbackDosenMainCtrl().getSelectedFeedbackDosenList();
    }

    public void setSelectedFeedbackDosenList(List<Tfeedbackdosen> selectedFeedbackDosenList) {
        // STORED IN THE module's MainController
        getFeedbackDosenMainCtrl().setSelectedFeedbackDosenList(selectedFeedbackDosenList);
    }

    public BindingListModelList getFeedbackDosens() {
        // STORED IN THE module's MainController
        return getFeedbackDosenMainCtrl().getFeedbackDosens();
    }

    public void setFeedbackDosens(BindingListModelList Tfeedbackdosen) {
        // STORED IN THE module's MainController
        getFeedbackDosenMainCtrl().setFeedbackDosens(Tfeedbackdosen);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public void setFeedbackDosenService(FeedbackDosenService feedbackDosenService) {
        this.feedbackDosenService = feedbackDosenService;
    }

    public FeedbackDosenService getFeedbackDosenService() {
        return this.feedbackDosenService;
    }

    public void setFeedbackDosenMainCtrl(FeedbackDosenMainCtrl feedbackDosenMainCtrl) {
        this.feedbackDosenMainCtrl = feedbackDosenMainCtrl;
    }

    public FeedbackDosenMainCtrl getFeedbackDosenMainCtrl() {
        return this.feedbackDosenMainCtrl;
    }
    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public Listbox getListBoxFeedbackDetailDosen() {
        return this.listBoxFeedbackDosenDetail;
    }

    public void setListBoxFeedbackDosen(Listbox listBoxFeedbackDetailDosen) {
        this.listBoxFeedbackDosenDetail = listBoxFeedbackDosenDetail;
    }

    public void setSearchObj(HibernateSearchObject<Tfeedbackdosen> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Tfeedbackdosen> getSearchObj() {
        return this.searchObj;
    }

    public Listbox getListBoxFeedbackDosenDetail() {
        return this.listBoxFeedbackDosenDetail;
    }

    public void setListBoxFeedbackDosenDetail(Listbox listBoxFeedbackDosenDetail) {
        this.listBoxFeedbackDosenDetail = listBoxFeedbackDosenDetail;
    }

    public void setSelectedFeedback(Mfeedback selectedFeedback) {
        // STORED IN THE module's MainController
        getFeedbackDosenMainCtrl().setSelectedFeedback(selectedFeedback);
    }

    public Mfeedback getSelectedFeedback() {
        // STORED IN THE module's MainController
        return getFeedbackDosenMainCtrl().getSelectedFeedback();
    }

    public void setFeedbacks(BindingListModelList feedbacks) {
        // STORED IN THE module's MainController
        getFeedbackDosenMainCtrl().setFeedbacks(feedbacks);
    }

    public BindingListModelList getFeedbacks() {
        // STORED IN THE module's MainController
        return getFeedbackDosenMainCtrl().getFeedbacks();
    }


    private PagedListWrapper<Mfeedback> pagedListFeedbackWrapper;
    private PagedListWrapper<Tfeedbackdosen> pagedListWrapper;

    private PagedBindingListWrapper<Mfeedback> pagedBindingListFeedbackWrapper;
    private PagedBindingListWrapper<Tfeedbackdosen> pagedBindingListWrapper;

    public PagedListWrapper<Mfeedback> getPagedListFeedbackWrapper() {
        return pagedListFeedbackWrapper;
    }

    public void setPagedListFeedbackWrapper(PagedListWrapper<Mfeedback> pagedListFeedbackWrapper) {
        this.pagedListFeedbackWrapper = pagedListFeedbackWrapper;
    }
    public PagedListWrapper<Tfeedbackdosen> getPagedListWrapper() {
        return pagedListWrapper;
    }

    public void setPagedListWrapper(PagedListWrapper<Tfeedbackdosen> pagedListWrapper) {
        this.pagedListWrapper = pagedListWrapper;
    }

    public void setPagedBindingListFeedbackWrapper(PagedBindingListWrapper<Mfeedback> pagedBindingListFeedbackWrapper) {
        this.pagedBindingListFeedbackWrapper = pagedBindingListFeedbackWrapper;
    }

    public PagedBindingListWrapper<Mfeedback> getPagedBindingListFeedbackWrapper() {
        return pagedBindingListFeedbackWrapper;
    }
    public void setPagedBindingListWrapper(PagedBindingListWrapper<Tfeedbackdosen> pagedBindingListWrapper) {
        this.pagedBindingListWrapper = pagedBindingListWrapper;
    }

    public PagedBindingListWrapper<Tfeedbackdosen> getPagedBindingListWrapper() {
        return pagedBindingListWrapper;
    }
}
