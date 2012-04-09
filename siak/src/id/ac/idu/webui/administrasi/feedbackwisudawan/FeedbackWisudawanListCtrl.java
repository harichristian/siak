package id.ac.idu.webui.administrasi.feedbackwisudawan;

import id.ac.idu.administrasi.service.FeedbackWisudawanService;
import id.ac.idu.backend.model.Tfeedbackwisudawan;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.util.ConstantUtil;
import id.ac.idu.webui.util.GFCBaseListCtrl;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
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
public class FeedbackWisudawanListCtrl extends GFCBaseListCtrl<Tfeedbackwisudawan> implements Serializable {
    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(FeedbackWisudawanListCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowFeedbackWisudawanList; // autowired
    protected Panel panelFeedbackWisudawanList; // autowired

    protected Borderlayout borderLayout_feedbackWisudawanList; // autowired
    protected Paging paging_FeedbackWisudawanList; // autowired
    protected Listbox listBoxFeedbackWisudawan; // autowired
    protected Listheader listheader_FeedbackWisudawanList_nama; // autowired
    protected Listheader listheader_FeedbackWisudawanList_term; // autowired
    protected Listheader listheader_FeedbackWisudawanList_kelompok; // autowired
    protected Listheader listheader_FeedbackWisudawanList_prodi; // autowired
    //protected Listheader listheader_FeedbackWisudawanList_kesan; // autowired

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Tfeedbackwisudawan> searchObj;

    // row count for listbox
    private int countRows;

    // Databinding
    private AnnotateDataBinder binder;
    private FeedbackWisudawanMainCtrl feedbackWisudawanMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient FeedbackWisudawanService feedbackWisudawanService;

    /**
     * default constructor.<br>
     */
    public FeedbackWisudawanListCtrl() {
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
            getFeedbackWisudawanMainCtrl().setFeedbackWisudawanListCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getFeedbackWisudawanMainCtrl().getSelectedFeedbackWisudawan() != null) {
                setSelectedFeedbackWisudawan(getFeedbackWisudawanMainCtrl().getSelectedFeedbackWisudawan());
            } else
                setSelectedFeedbackWisudawan(null);
        } else {
            setSelectedFeedbackWisudawan(null);
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

    public void onCreate$windowFeedbackWisudawanList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();

        binder.loadAll();
    }

    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_FeedbackWisudawanList.setPageSize(getCountRows());
        paging_FeedbackWisudawanList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_FeedbackWisudawanList_nama.setSortAscending(new FieldComparator(ConstantUtil.MAHASISWA_DOT_NAME, true));
        listheader_FeedbackWisudawanList_nama.setSortDescending(new FieldComparator(ConstantUtil.MAHASISWA_DOT_NAME, false));
        listheader_FeedbackWisudawanList_term.setSortAscending(new FieldComparator(ConstantUtil.TERM, true));
        listheader_FeedbackWisudawanList_term.setSortDescending(new FieldComparator(ConstantUtil.TERM, false));
        listheader_FeedbackWisudawanList_kelompok.setSortAscending(new FieldComparator(ConstantUtil.KELOMPOK, true));
        listheader_FeedbackWisudawanList_kelompok.setSortDescending(new FieldComparator(ConstantUtil.KELOMPOK, false));
        listheader_FeedbackWisudawanList_prodi.setSortAscending(new FieldComparator(ConstantUtil.PRODI_DOT_NAME, true));
        listheader_FeedbackWisudawanList_prodi.setSortDescending(new FieldComparator(ConstantUtil.PRODI_DOT_NAME, false));

        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Tfeedbackwisudawan>(Tfeedbackwisudawan.class, getCountRows());
        searchObj.addSort(ConstantUtil.TERM, false);
        setSearchObj(searchObj);

        // Set the BindingListModel
        getPagedBindingListWrapper().init(searchObj, getListBoxFeedbackWisudawan(), paging_FeedbackWisudawanList);
        BindingListModelList lml = (BindingListModelList) getListBoxFeedbackWisudawan().getModel();
        setFeedbackWisudawans(lml);

        // check if first time opened and init databinding for selectedBean
        if (getSelectedFeedbackWisudawan() == null) {
            // init the bean with the first record in the List
            if (lml.getSize() > 0) {
                final int rowIndex = 0;
                // only for correct showing after Rendering. No effect as an
                // Event
                // yet.
                getListBoxFeedbackWisudawan().setSelectedIndex(rowIndex);
                // get the first entry and cast them to the needed object
                setSelectedFeedbackWisudawan((Tfeedbackwisudawan) lml.get(0));

                // call the onSelect Event for showing the objects data in the
                // statusBar
                Events.sendEvent(new Event("onSelect", getListBoxFeedbackWisudawan(), getSelectedFeedbackWisudawan()));
            }
        }

    }

    /**
     * Selects the object in the listbox and change the tab.<br>
     * Event is forwarded in the corresponding listbox.
     */
    public void onDoubleClickedFeedbackWisudawanItem(Event event) {
        // logger.debug(event.toString());

        Tfeedbackwisudawan anFeedbackWisudawan = getSelectedFeedbackWisudawan();

        if (anFeedbackWisudawan != null) {
            setSelectedFeedbackWisudawan(anFeedbackWisudawan);
            setFeedbackWisudawan(anFeedbackWisudawan);

            // check first, if the tabs are created
            if (getFeedbackWisudawanMainCtrl().getFeedbackWisudawanDetailCtrl() == null) {
                Events.sendEvent(new Event("onSelect", getFeedbackWisudawanMainCtrl().tabFeedbackWisudawanDetail, null));
                // if we work with spring beanCreation than we must check a
                // little bit deeper, because the Controller are preCreated ?
            } else if (getFeedbackWisudawanMainCtrl().getFeedbackWisudawanDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getFeedbackWisudawanMainCtrl().tabFeedbackWisudawanDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getFeedbackWisudawanMainCtrl().tabFeedbackWisudawanDetail, anFeedbackWisudawan));
        }
    }

    /**
     * When a listItem in the corresponding listbox is selected.<br>
     * Event is forwarded in the corresponding listbox.
     *
     * @param event
     */
    public void onSelect$listBoxFeedbackWisudawan(Event event) {
        // logger.debug(event.toString());

        // selectedFeedbackWisudawan is filled by annotated databinding mechanism
        Tfeedbackwisudawan anFeedbackWisudawan = getSelectedFeedbackWisudawan();
        //List<Tfeedbackwisudawan> ltfa = getSelectedFeedbackWisudawanList();
        //List<Tfeedbackwisudawan> ltfa = getFeedbackWisudawanService().getFeedbackWisudawanByNim(anFeedbackWisudawan.getMmahasiswa().getCnim());

        if (anFeedbackWisudawan == null) {
            return;
        }
        List<Tfeedbackwisudawan> ltfa = getFeedbackWisudawanService().getFeedbackWisudawanByNim(anFeedbackWisudawan.getMmahasiswa(),
                anFeedbackWisudawan.getCterm(), anFeedbackWisudawan.getCkelompok());

        // check first, if the tabs are created
        if (getFeedbackWisudawanMainCtrl().getFeedbackWisudawanDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getFeedbackWisudawanMainCtrl().tabFeedbackWisudawanDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getFeedbackWisudawanMainCtrl().getFeedbackWisudawanDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getFeedbackWisudawanMainCtrl().tabFeedbackWisudawanDetail, null));
        }

        // INIT ALL RELATED Queries/OBJECTS/LISTS NEW
        getFeedbackWisudawanMainCtrl().getFeedbackWisudawanDetailCtrl().setSelectedFeedbackWisudawan(anFeedbackWisudawan);
        getFeedbackWisudawanMainCtrl().getFeedbackWisudawanDetailCtrl().setFeedbackWisudawan(anFeedbackWisudawan);

        getFeedbackWisudawanMainCtrl().getFeedbackWisudawanDetailCtrl().setSelectedFeedbackWisudawanList(ltfa);
        getFeedbackWisudawanMainCtrl().getFeedbackWisudawanDetailCtrl().setFeedbackWisudawanList(ltfa);
        /*
        // set the beans in the related databinded controllers
        getFeedbackWisudawanDetailCtrl().setFeedbackWisudawan(anFeedbackWisudawan);
        getFeedbackWisudawanDetailCtrl().setSelectedFeedbackWisudawan(anFeedbackWisudawan);

        getFeedbackWisudawanDetailCtrl().setFeedbackWisudawanList(listFeedbackWisudawan);
        getFeedbackWisudawanDetailCtrl().setSelectedFeedbackWisudawanList(listFeedbackWisudawan);
        getFeedbackWisudawanDetailCtrl().setSelectedFeedbackWisudawanList(getSelectedFeedbackWisudawanList());
        getFeedbackWisudawanDetailCtrl().setSelectedFeedbackWisudawanList(getSelectedFeedbackWisudawanList());
        if (getFeedbackWisudawanDetailCtrl().getBinder() != null) {
            getFeedbackWisudawanDetailCtrl().getBinder().loadAll();
        }
        PagedBindingListWrapper<Tfeedbackwisudawan> pblw = getFeedbackWisudawanDetailCtrl().getPagedBindingListWrapper();
        pblw.clear();
        pblw.addAll(getSelectedFeedbackWisudawanList());
        */
        // store the selected bean values as current
        getFeedbackWisudawanMainCtrl().doStoreInitValues();

        // show the objects data in the statusBar
        String str ="Feedback Wisudawan " + ": " + anFeedbackWisudawan.getMmahasiswa().getCnama();
        EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

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
    public void doFitSize() {
        // normally 0 ! Or we have a i.e. a toolBar on top of the listBox.
        final int specialSize = 5;
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
        final int maxListBoxHeight = height - specialSize - 148;
        setCountRows((int) Math.round(maxListBoxHeight / 17.7));
        borderLayout_feedbackWisudawanList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowFeedbackWisudawanList.invalidate();
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    // ++++++++++++++++++ getter / setter +++++++++++++++++++//
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

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

    public void setSelectedFeedbackWisudawan(Tfeedbackwisudawan selectedFeedbackWisudawan) {
        // STORED IN THE module's MainController
        getFeedbackWisudawanMainCtrl().setSelectedFeedbackWisudawan(selectedFeedbackWisudawan);
    }

    public Tfeedbackwisudawan getSelectedFeedbackWisudawan() {
        // STORED IN THE module's MainController
        return getFeedbackWisudawanMainCtrl().getSelectedFeedbackWisudawan();
    }

    public void setSelectedFeedbackWisudawanList(List<Tfeedbackwisudawan> selectedFeedbackWisudawanList) {
        // STORED IN THE module's MainController
        getFeedbackWisudawanMainCtrl().setSelectedFeedbackWisudawanList(selectedFeedbackWisudawanList);
    }

    public List<Tfeedbackwisudawan> getSelectedFeedbackWisudawanList() {
        // STORED IN THE module's MainController
        return getFeedbackWisudawanMainCtrl().getSelectedFeedbackWisudawanList();
    }

    public void setFeedbackWisudawans(BindingListModelList feedbackWisudawans) {
        // STORED IN THE module's MainController
        getFeedbackWisudawanMainCtrl().setFeedbackWisudawans(feedbackWisudawans);
    }

    public BindingListModelList getFeedbackWisudawans() {
        // STORED IN THE module's MainController
        return getFeedbackWisudawanMainCtrl().getFeedbackWisudawans();
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setSearchObj(HibernateSearchObject<Tfeedbackwisudawan> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Tfeedbackwisudawan> getSearchObj() {
        return this.searchObj;
    }

    public void setFeedbackWisudawanService(FeedbackWisudawanService feedbackWisudawanService) {
        this.feedbackWisudawanService = feedbackWisudawanService;
    }

    public FeedbackWisudawanService getFeedbackWisudawanService() {
        return this.feedbackWisudawanService;
    }

    public Listbox getListBoxFeedbackWisudawan() {
        return this.listBoxFeedbackWisudawan;
    }

    public void setListBoxFeedbackWisudawan(Listbox listBoxFeedbackWisudawan) {
        this.listBoxFeedbackWisudawan = listBoxFeedbackWisudawan;
    }

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void setFeedbackWisudawanMainCtrl(FeedbackWisudawanMainCtrl feedbackWisudawanMainCtrl) {
        this.feedbackWisudawanMainCtrl = feedbackWisudawanMainCtrl;
    }

    public FeedbackWisudawanMainCtrl getFeedbackWisudawanMainCtrl() {
        return this.feedbackWisudawanMainCtrl;
    }


}
