package id.ac.idu.webui.administrasi.feedbackalumni;

import com.trg.search.Filter;
import id.ac.idu.administrasi.service.FeedbackAlumniService;
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
import java.util.ArrayList;
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
    protected Radiogroup rdi;
    protected Listitem ls;
    protected Radio[] child1;
    protected Textbox xsx;
    protected Listcell lscell;

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

    public  void setRadioOnListBox(List<Tfeedbackalumni> lfa){

       // get All items first
//        Listhead listhead = new Listhead();
//		Listheader listheader = new Listheader("No");
//		listheader.setHflex("1");
//		listheader.setAlign("left");
//        listheader.setWidth("40px");
//        listheader.
//		listheader.setParent(listhead);
//		listheader = new Listheader("Pertanyaan");
//		listheader.setHflex("1");
//         listheader.setWidth("550px");
//		listheader.setParent(listhead);
//        listheader = new Listheader("Jawaban");
//		listheader.setHflex("1");
//         listheader.setWidth("400px");
//		listheader.setParent(listhead);
//        listhead.setParent(listBoxFeedbackAlumniDetail);
        ListBoxUtil.resetList(listBoxFeedbackAlumniDetail);

        if (lfa.size()==0)  {
            try {
                initJawaban();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        for (int i=0; i < lfa.size();i++){
            //get a list item

            Tfeedbackalumni fa = new Tfeedbackalumni();
            fa = (Tfeedbackalumni) lfa.get(i);
//            Listitem ltm = new Listitem(fa.getStrId(),fa);
              Listitem ltm = new Listitem();

//            listBoxFeedbackAlumniDetail.getItems().set(i,ltm);
//            for (int j=0;j < ((Radiogroup)((Listcell)((Listitem) listBoxFeedbackAlumniDetail.getItems().get(i)).getChildren().get(2)).getChildren().get(3)).getItems().size();j++){
//                       ((Radio)((Radiogroup)((Listcell)((Listitem) listBoxFeedbackAlumniDetail.getItems().get(i)).getChildren().get(2)).getChildren().get(3)).getItemAtIndex(j)).setValue(String.valueOf(fa.getId()));
//                        if (fa.getCjawaban().equals(String.valueOf(j))){
//                          ((Radio)((Radiogroup)((Listcell)((Listitem) listBoxFeedbackAlumniDetail.getItems().get(i)).getChildren().get(2)).getChildren().get(3)).getItemAtIndex(j)).setSelected(true);
//                        }
//            }
            ltm.setParent(listBoxFeedbackAlumniDetail);
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
           // radio5.setParent(listcell);
            radiogroup.setParent(listcell);
			listcell.setParent(ltm);
        }
            setListBoxFeedbackAlumni(null);
            setListBoxFeedbackAlumni(listBoxFeedbackAlumniDetail);
            //get the of item
//            List<Listcell> lcell =  oneItem.getChildren();
//            Listcell   objcell = (Listcell) lcell.get(2);
//            List<Object>  theObject = objcell.getChildren();
//            // get radio group
//            Radiogroup rdg = (Radiogroup) theObject.get(3);
//            //set radio value with id
//            for (int j=0; j<rdg.getItems().size(); j++) {
//                   Radio rad = rdg.getItemAtIndex(j);
//                   rad.setValue(fa.getStrId());
//                   if (fa.getCjawaban().equals(String.valueOf(j))){
//                        rad.setSelected(true);
//                   }
//            }
//        }
    }

     public void initJawaban() throws InterruptedException {
     ListBoxUtil.resetList(listBoxFeedbackAlumniDetail);

        List<Mfeedback> lfa =   mfeedbackService.getAllMfeedbackByType(Codec.KodeFeedback.A.getName());
        if (lfa.size()==0)   {
            ZksampleMessageUtils.showErrorMessage("Pertanyaan belum di buat !");
        }

        for (int i=0; i < lfa.size();i++){
            //get a list item

            Mfeedback fa = new Mfeedback();
            fa = (Mfeedback) lfa.get(i);

            Listitem ltm = new Listitem();

            ltm.setParent(listBoxFeedbackAlumniDetail);
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
           setListBoxFeedbackAlumni(null);
           setListBoxFeedbackAlumni(listBoxFeedbackAlumniDetail);
    }

    public List<Tfeedbackalumni> setlbtolist(List<Tfeedbackalumni> getList)  {
        List<Tfeedbackalumni> lfa =  new ArrayList<Tfeedbackalumni>();
        lfa = getList;
        for (int i=0;i < lfa.size();i++){
             ((Tfeedbackalumni)  lfa.get(i)).setCjawaban(getJawabanfromlb(((Tfeedbackalumni)  lfa.get(i)).getMfeedback().getId()));
        }
        return lfa;
    }

    public String getJawabanfromlb(int id)   {
        String jawaban="";
        String strId=String.valueOf(id);;
        for (int i=0;i < listBoxFeedbackAlumniDetail.getItems().size();i++){
            Listitem li = listBoxFeedbackAlumniDetail.getItemAtIndex(i);
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

//    public void onCheck$rdi(Event event) {
//      Listitem li = (Listitem) ls.getValue();
//      Radio radio =   rdi.getSelectedItem();
//      List<Radio> lr = rdi.getItems();
//      lr.size();
//
//         List<Object> lsc = lscell.getChildren();
//        Textbox tx =  (Textbox) lsc.get(1);
//        tx.getValue();
//
//    }

    public void onCheck$child1(Event event) {
//         String idx = child1[1].getValue();
//         for (int i=0;i < getListBoxFeedbackAlumniDetail().getItems().size();i++){
//             if (String.valueOf(((Tfeedbackalumni)((Listitem) getListBoxFeedbackAlumniDetail().getItems().get(i)).getValue()).getId()).equals(idx)){
//                 ((Tfeedbackalumni)((Listitem) getListBoxFeedbackAlumniDetail().getItems().get(i)).getValue()).setCjawaban("1");
//             }
//         }
       getFeedbackAlumni().setCjawaban("1");
       setFeedbackAlumni(getFeedbackAlumni());
    }

    public void onChange$xsx(Event event){
        String s = xsx.getValue();
        if (!s.equals("1") || !s.equals("2") || !s.equals("3") || !s.equals("4") || !s.equals("5") ) {
            xsx.setValue("");
            return;
        }
        getSelectedFeedbackAlumni().setCjawaban(s);
    }

//     public void onCheck$child1(Event event) {
////        Listbox box = (Listbox) child1.getParent().getParent().getParent().getParent();
//        ls.setSelected(true);
//        Listitem lt = (Listitem) child1.getParent().getParent().getParent();
//        lt.setSelected(true);
//        List<Listcell> listC = lt.getChildren();
//        Listcell cell =  (Listcell) listC.get(2);
//        List<Object> objcell =  cell.getChildren();
//        Textbox tx = (Textbox)     objcell.get(1);
//        tx.getValue();
//        Listitem lt1 = listBoxFeedbackAlumniDetail.getSelectedItem();
//        List<Object> listC1 = lt1.getChildren();
////          xsx.setValue("1");
////          Radio radio =  child1;
////          List<Radio> lr = rdi.getItems();
////          lr.size();
////
////        Listbox lbox = ls.getListbox();
////        List<Listcell> lsc = ls.getChildren();
////        Listcell   lscellc = lsc.get(2);
////        List<Object> slsc = lscell.getChildren();
////        Textbox tx =  (Textbox) slsc.get(1);
////        tx.getValue();
//    }

//    public void onSelect$lscell(Event event){
//        List<Object> lsc = lscell.getChildren();
//        Textbox tx =  (Textbox) lsc.get(1);
//        tx.getValue();
//    }

//    public void onSelect$ls(Event event){
//        List<Listcell> lsc = ls.getChildren();
//        Listcell   lscellc = lsc.get(2);
//        List<Object> slsc = lscell.getChildren();
//        Textbox tx =  (Textbox) slsc.get(1);
//        tx.getValue();
//    }

//     public void onSelect$listBoxFeedbackAlumniDetail(Event event){
//        Listitem ltm = listBoxFeedbackAlumniDetail.getSelectedItem();
//        List<Object> lsc = ltm.getChildren();
//        Listitem lsb = (Listitem) lsc.get(1);
//        Listcell  lscellc = (Listcell) lsb.getChildren();
//        List<Object> slsc = lscellc.getChildren();
//        Textbox tx =  (Textbox) slsc.get(1);
//        tx.getValue();
//    }

    public void doFillListbox() throws Exception {

        doFitSize();

        // set the paging params


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

        if(getSelectedFeedbackAlumni()!=null && getSelectedFeedbackAlumni().getMmahasiswa()!=null) {
            searchObj.addFilterAnd(new Filter(ConstantUtil.MAHASISWA_DOT_NIM, getSelectedFeedbackAlumni().getMmahasiswa().getCnim(), Filter.OP_EQUAL));
            searchObj.addFilterAnd(new Filter("cterm", getSelectedFeedbackAlumni().getCterm(), Filter.OP_EQUAL));
//            searchObj.addFilter(new Filter("nnopertanyaan", getSelectedFeedbackAlumni().getNnopertanyaan(), Filter.OP_EQUAL));
        }
        searchObj.addSort(ConstantUtil.NO_PERTANYAAN, false);

        // Change the BindingListModel.
        if (getBinder() != null) {

            /*getPagedBindingListFeedbackWrapper().init(so, getListBoxFeedbackAlumniDetail(), paging_FeedbackList);
            BindingListModelList blml = (BindingListModelList) getListBoxFeedbackAlumniDetail().getModel();
            setFeedbacks(blml);*/

            //set radio
            setRadioOnListBox(getFeedbackAlumniMainCtrl().getList(getSelectedFeedbackAlumni()));

//            Listbox lb =  new Listbox();
            //tutup dulu ya
//            getPagedBindingListWrapper().init(searchObj, getListBoxFeedbackAlumniDetail(), paging_FeedbackAlumniDetailList);
            // ampe sini

//              getPagedBindingListWrapper().clear();
//                getPagedBindingListWrapper().addAll(getSelectedFeedbackAlumniList());
//                 getPagedBindingListWrapper().addAll(getFeedbackAlumniMainCtrl().getList(getSelectedFeedbackAlumni()));
//              lb =  getListBoxFeedbackAlumniDetail();
//                for (int i=lb.getItemCount()-1;i>0;i--){
//                    Tfeedbackalumni fa = (Tfeedbackalumni) lb.getItemAtIndex(i).getValue();
//                    if (fa.getMmahasiswa().getId()!=getSelectedFeedbackAlumni().getMmahasiswa().getId()){
//                        lb.removeItemAt(i);
//                    }
//
//                }
//            getPagedBindingListWrapper().init(searchObj, lb, paging_FeedbackAlumniDetailList);
            try{
                getPagedBindingListWrapper().clear();
//                getPagedBindingListWrapper().addAll(getSelectedFeedbackAlumniList());
                 getPagedBindingListWrapper().addAll(getFeedbackAlumniMainCtrl().getList(getSelectedFeedbackAlumni()));
//                 feedbackAlumniMainCtrl.doSearchDetail(getSelectedFeedbackAlumni());
            } catch (NullPointerException e) {}
            //pagedBindingListWrapper = new PagedBindingListWrapper<Tfeedbackalumni>();
            //pagedBindingListFeedbackWrapper.addAll(getSelectedFeedbackAlumniList());



            BindingListModelList lml = (BindingListModelList) getListBoxFeedbackAlumniDetail().getModel();

            setFeedbackAlumnis(lml);
            if (getSelectedFeedbackAlumni() == null && lml!=null) {
                // init the bean with the first record in the List
                if (lml.getSize() > 0) {
                    final int rowIndex = 0;
                    getListBoxFeedbackDetailAlumni().setSelectedIndex(rowIndex);
                    setSelectedFeedbackAlumni((Tfeedbackalumni) lml.get(0));

//                    Events.sendEvent(new Event("onSelect", lb, getSelectedFeedbackAlumni()));
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
