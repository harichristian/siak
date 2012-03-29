package id.ac.idu.webui.administrasi.feedbackalumni;

import id.ac.idu.administrasi.service.FeedbackAlumniService;
import id.ac.idu.backend.model.Tfeedbackalumni;
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
public class FeedbackAlumniListCtrl extends GFCBaseListCtrl<Tfeedbackalumni> implements Serializable {
    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(FeedbackAlumniListCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowFeedbackAlumniList; // autowired
    protected Panel panelFeedbackAlumniList; // autowired

    protected Borderlayout borderLayout_feedbackAlumniList; // autowired
    protected Paging paging_FeedbackAlumniList; // autowired
    protected Listbox listBoxFeedbackAlumni; // autowired
    protected Listheader listheader_FeedbackAlumniList_nama; // autowired
    protected Listheader listheader_FeedbackAlumniList_term; // autowired
    protected Listheader listheader_FeedbackAlumniList_kelompok; // autowired
    protected Listheader listheader_FeedbackAlumniList_prodi; // autowired
    //protected Listheader listheader_FeedbackAlumniList_kesan; // autowired

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Tfeedbackalumni> searchObj;

    // row count for listbox
    private int countRows;

    // Databinding
    private AnnotateDataBinder binder;
    private FeedbackAlumniMainCtrl feedbackAlumniMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient FeedbackAlumniService feedbackAlumniService;

    /**
     * default constructor.<br>
     */
    public FeedbackAlumniListCtrl() {
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
            getFeedbackAlumniMainCtrl().setFeedbackAlumniListCtrl(this);

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

    public void onCreate$windowFeedbackAlumniList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();

        binder.loadAll();
    }

    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_FeedbackAlumniList.setPageSize(getCountRows());
        paging_FeedbackAlumniList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_FeedbackAlumniList_nama.setSortAscending(new FieldComparator(ConstantUtil.MAHASISWA_DOT_NAME, true));
        listheader_FeedbackAlumniList_nama.setSortDescending(new FieldComparator(ConstantUtil.MAHASISWA_DOT_NAME, false));
        listheader_FeedbackAlumniList_term.setSortAscending(new FieldComparator(ConstantUtil.TERM, true));
        listheader_FeedbackAlumniList_term.setSortDescending(new FieldComparator(ConstantUtil.TERM, false));
        listheader_FeedbackAlumniList_kelompok.setSortAscending(new FieldComparator(ConstantUtil.KELOMPOK, true));
        listheader_FeedbackAlumniList_kelompok.setSortDescending(new FieldComparator(ConstantUtil.KELOMPOK, false));
        listheader_FeedbackAlumniList_prodi.setSortAscending(new FieldComparator(ConstantUtil.PRODI_DOT_NAME, true));
        listheader_FeedbackAlumniList_prodi.setSortDescending(new FieldComparator(ConstantUtil.PRODI_DOT_NAME, false));

        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Tfeedbackalumni>(Tfeedbackalumni.class, getCountRows());
        searchObj.addSort(ConstantUtil.TERM, false);
        setSearchObj(searchObj);

        // Set the BindingListModel
        getPagedBindingListWrapper().init(searchObj, getListBoxFeedbackAlumni(), paging_FeedbackAlumniList);
        BindingListModelList lml = (BindingListModelList) getListBoxFeedbackAlumni().getModel();
        setFeedbackAlumnis(lml);

        // check if first time opened and init databinding for selectedBean
        if (getSelectedFeedbackAlumni() == null) {
            // init the bean with the first record in the List
            if (lml.getSize() > 0) {
                final int rowIndex = 0;
                // only for correct showing after Rendering. No effect as an
                // Event
                // yet.
                getListBoxFeedbackAlumni().setSelectedIndex(rowIndex);
                // get the first entry and cast them to the needed object
                setSelectedFeedbackAlumni((Tfeedbackalumni) lml.get(0));

                // call the onSelect Event for showing the objects data in the
                // statusBar
                Events.sendEvent(new Event("onSelect", getListBoxFeedbackAlumni(), getSelectedFeedbackAlumni()));
            }
        }

    }

    /**
     * Selects the object in the listbox and change the tab.<br>
     * Event is forwarded in the corresponding listbox.
     */
    public void onDoubleClickedFeedbackAlumniItem(Event event) {
        // logger.debug(event.toString());

        Tfeedbackalumni anFeedbackAlumni = getSelectedFeedbackAlumni();

        if (anFeedbackAlumni != null) {
            setSelectedFeedbackAlumni(anFeedbackAlumni);
            setFeedbackAlumni(anFeedbackAlumni);

            // check first, if the tabs are created
            if (getFeedbackAlumniMainCtrl().getFeedbackAlumniDetailCtrl() == null) {
                Events.sendEvent(new Event("onSelect", getFeedbackAlumniMainCtrl().tabFeedbackAlumniDetail, null));
                // if we work with spring beanCreation than we must check a
                // little bit deeper, because the Controller are preCreated ?
            } else if (getFeedbackAlumniMainCtrl().getFeedbackAlumniDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getFeedbackAlumniMainCtrl().tabFeedbackAlumniDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getFeedbackAlumniMainCtrl().tabFeedbackAlumniDetail, anFeedbackAlumni));
        }
    }

    /**
     * When a listItem in the corresponding listbox is selected.<br>
     * Event is forwarded in the corresponding listbox.
     *
     * @param event
     */
    public void onSelect$listBoxFeedbackAlumni(Event event) {
        // logger.debug(event.toString());

        // selectedFeedbackAlumni is filled by annotated databinding mechanism
        Tfeedbackalumni anFeedbackAlumni = getSelectedFeedbackAlumni();
        //List<Tfeedbackalumni> ltfa = getSelectedFeedbackAlumniList();
        //List<Tfeedbackalumni> ltfa = getFeedbackAlumniService().getFeedbackAlumniByNim(anFeedbackAlumni.getMmahasiswa().getCnim());

        if (anFeedbackAlumni == null) {
            return;
        }
        List<Tfeedbackalumni> ltfa = getFeedbackAlumniService().getFeedbackAlumniByNim(anFeedbackAlumni.getMmahasiswa().getCnim(),
                anFeedbackAlumni.getCterm(), anFeedbackAlumni.getCkelompok());

        // check first, if the tabs are created
        if (getFeedbackAlumniMainCtrl().getFeedbackAlumniDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getFeedbackAlumniMainCtrl().tabFeedbackAlumniDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getFeedbackAlumniMainCtrl().getFeedbackAlumniDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getFeedbackAlumniMainCtrl().tabFeedbackAlumniDetail, null));
        }

        // INIT ALL RELATED Queries/OBJECTS/LISTS NEW
        getFeedbackAlumniMainCtrl().getFeedbackAlumniDetailCtrl().setSelectedFeedbackAlumni(anFeedbackAlumni);
        getFeedbackAlumniMainCtrl().getFeedbackAlumniDetailCtrl().setFeedbackAlumni(anFeedbackAlumni);

        getFeedbackAlumniMainCtrl().getFeedbackAlumniDetailCtrl().setSelectedFeedbackAlumniList(ltfa);
        getFeedbackAlumniMainCtrl().getFeedbackAlumniDetailCtrl().setFeedbackAlumniList(ltfa);
        /*
        // set the beans in the related databinded controllers
        getFeedbackAlumniDetailCtrl().setFeedbackAlumni(anFeedbackAlumni);
        getFeedbackAlumniDetailCtrl().setSelectedFeedbackAlumni(anFeedbackAlumni);

        getFeedbackAlumniDetailCtrl().setFeedbackAlumniList(listFeedbackAlumni);
        getFeedbackAlumniDetailCtrl().setSelectedFeedbackAlumniList(listFeedbackAlumni);
        getFeedbackAlumniDetailCtrl().setSelectedFeedbackAlumniList(getSelectedFeedbackAlumniList());
        getFeedbackAlumniDetailCtrl().setSelectedFeedbackAlumniList(getSelectedFeedbackAlumniList());
        if (getFeedbackAlumniDetailCtrl().getBinder() != null) {
            getFeedbackAlumniDetailCtrl().getBinder().loadAll();
        }
        PagedBindingListWrapper<Tfeedbackalumni> pblw = getFeedbackAlumniDetailCtrl().getPagedBindingListWrapper();
        pblw.clear();
        pblw.addAll(getSelectedFeedbackAlumniList());
        */
        // store the selected bean values as current
        getFeedbackAlumniMainCtrl().doStoreInitValues();

        // show the objects data in the statusBar
        String str ="Feedback Alumni " + ": " + anFeedbackAlumni.getMmahasiswa().getCnama();
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
        borderLayout_feedbackAlumniList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowFeedbackAlumniList.invalidate();
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
    public Tfeedbackalumni getFeedbackAlumni() {
        // STORED IN THE module's MainController
        return getFeedbackAlumniMainCtrl().getSelectedFeedbackAlumni();
    }

    public void setFeedbackAlumni(Tfeedbackalumni anFeedbackAlumni) {
        // STORED IN THE module's MainController
        getFeedbackAlumniMainCtrl().setSelectedFeedbackAlumni(anFeedbackAlumni);
    }

    public void setSelectedFeedbackAlumni(Tfeedbackalumni selectedFeedbackAlumni) {
        // STORED IN THE module's MainController
        getFeedbackAlumniMainCtrl().setSelectedFeedbackAlumni(selectedFeedbackAlumni);
    }

    public Tfeedbackalumni getSelectedFeedbackAlumni() {
        // STORED IN THE module's MainController
        return getFeedbackAlumniMainCtrl().getSelectedFeedbackAlumni();
    }

    public void setSelectedFeedbackAlumniList(List<Tfeedbackalumni> selectedFeedbackAlumniList) {
        // STORED IN THE module's MainController
        getFeedbackAlumniMainCtrl().setSelectedFeedbackAlumniList(selectedFeedbackAlumniList);
    }

    public List<Tfeedbackalumni> getSelectedFeedbackAlumniList() {
        // STORED IN THE module's MainController
        return getFeedbackAlumniMainCtrl().getSelectedFeedbackAlumniList();
    }

    public void setFeedbackAlumnis(BindingListModelList feedbackAlumnis) {
        // STORED IN THE module's MainController
        getFeedbackAlumniMainCtrl().setFeedbackAlumnis(feedbackAlumnis);
    }

    public BindingListModelList getFeedbackAlumnis() {
        // STORED IN THE module's MainController
        return getFeedbackAlumniMainCtrl().getFeedbackAlumnis();
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setSearchObj(HibernateSearchObject<Tfeedbackalumni> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Tfeedbackalumni> getSearchObj() {
        return this.searchObj;
    }

    public void setFeedbackAlumniService(FeedbackAlumniService feedbackAlumniService) {
        this.feedbackAlumniService = feedbackAlumniService;
    }

    public FeedbackAlumniService getFeedbackAlumniService() {
        return this.feedbackAlumniService;
    }

    public Listbox getListBoxFeedbackAlumni() {
        return this.listBoxFeedbackAlumni;
    }

    public void setListBoxFeedbackAlumni(Listbox listBoxFeedbackAlumni) {
        this.listBoxFeedbackAlumni = listBoxFeedbackAlumni;
    }

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void setFeedbackAlumniMainCtrl(FeedbackAlumniMainCtrl feedbackAlumniMainCtrl) {
        this.feedbackAlumniMainCtrl = feedbackAlumniMainCtrl;
    }

    public FeedbackAlumniMainCtrl getFeedbackAlumniMainCtrl() {
        return this.feedbackAlumniMainCtrl;
    }


}
