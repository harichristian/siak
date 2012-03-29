package id.ac.idu.webui.administrasi.feedbackdosen;

import id.ac.idu.administrasi.service.FeedbackDosenService;
import id.ac.idu.backend.model.Tfeedbackdosen;
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
public class FeedbackDosenListCtrl extends GFCBaseListCtrl<Tfeedbackdosen> implements Serializable {
    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(FeedbackDosenListCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowFeedbackDosenList; // autowired
    protected Panel panelFeedbackDosenList; // autowired

    protected Borderlayout borderLayout_feedbackDosenList; // autowired
    protected Paging paging_FeedbackDosenList; // autowired
    protected Listbox listBoxFeedbackDosen; // autowired
    protected Listheader listheader_FeedbackDosenList_nama; // autowired
    protected Listheader listheader_FeedbackDosenList_term; // autowired
    protected Listheader listheader_FeedbackDosenList_kelompok; // autowired
    protected Listheader listheader_FeedbackDosenList_prodi; // autowired
    //protected Listheader listheader_FeedbackDosenList_kesan; // autowired

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Tfeedbackdosen> searchObj;

    // row count for listbox
    private int countRows;

    // Databinding
    private AnnotateDataBinder binder;
    private FeedbackDosenMainCtrl feedbackDosenMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient FeedbackDosenService feedbackDosenService;

    /**
     * default constructor.<br>
     */
    public FeedbackDosenListCtrl() {
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
            getFeedbackDosenMainCtrl().setFeedbackDosenListCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getFeedbackDosenMainCtrl().getSelectedFeedbackDosen() != null) {
                setSelectedFeedbackDosen(getFeedbackDosenMainCtrl().getSelectedFeedbackDosen());
            } else
                setSelectedFeedbackDosen(null);
        } else {
            setSelectedFeedbackDosen(null);
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

    public void onCreate$windowFeedbackDosenList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();

        binder.loadAll();
    }

    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_FeedbackDosenList.setPageSize(getCountRows());
        paging_FeedbackDosenList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_FeedbackDosenList_nama.setSortAscending(new FieldComparator(ConstantUtil.PEGAWAI_DOT_NAMA, true));
        listheader_FeedbackDosenList_nama.setSortDescending(new FieldComparator(ConstantUtil.PEGAWAI_DOT_NAMA, false));
        listheader_FeedbackDosenList_term.setSortAscending(new FieldComparator(ConstantUtil.TERM, true));
        listheader_FeedbackDosenList_term.setSortDescending(new FieldComparator(ConstantUtil.TERM, false));
        listheader_FeedbackDosenList_kelompok.setSortAscending(new FieldComparator(ConstantUtil.KELOMPOK, true));
        listheader_FeedbackDosenList_kelompok.setSortDescending(new FieldComparator(ConstantUtil.KELOMPOK, false));
        listheader_FeedbackDosenList_prodi.setSortAscending(new FieldComparator(ConstantUtil.PRODI_DOT_NAME, true));
        listheader_FeedbackDosenList_prodi.setSortDescending(new FieldComparator(ConstantUtil.PRODI_DOT_NAME, false));

        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Tfeedbackdosen>(Tfeedbackdosen.class, getCountRows());
        searchObj.addSort(ConstantUtil.TERM, false);
        setSearchObj(searchObj);

        // Set the BindingListModel
        getPagedBindingListWrapper().init(searchObj, getListBoxFeedbackDosen(), paging_FeedbackDosenList);
        BindingListModelList lml = (BindingListModelList) getListBoxFeedbackDosen().getModel();
        setFeedbackDosens(lml);

        // check if first time opened and init databinding for selectedBean
        if (getSelectedFeedbackDosen() == null) {
            // init the bean with the first record in the List
            if (lml.getSize() > 0) {
                final int rowIndex = 0;
                // only for correct showing after Rendering. No effect as an
                // Event
                // yet.
                getListBoxFeedbackDosen().setSelectedIndex(rowIndex);
                // get the first entry and cast them to the needed object
                setSelectedFeedbackDosen((Tfeedbackdosen) lml.get(0));

                // call the onSelect Event for showing the objects data in the
                // statusBar
                Events.sendEvent(new Event("onSelect", getListBoxFeedbackDosen(), getSelectedFeedbackDosen()));
            }
        }

    }

    /**
     * Selects the object in the listbox and change the tab.<br>
     * Event is forwarded in the corresponding listbox.
     */
    public void onDoubleClickedFeedbackDosenItem(Event event) {
        // logger.debug(event.toString());

        Tfeedbackdosen anFeedbackDosen = getSelectedFeedbackDosen();

        if (anFeedbackDosen != null) {
            setSelectedFeedbackDosen(anFeedbackDosen);
            setFeedbackDosen(anFeedbackDosen);

            // check first, if the tabs are created
            if (getFeedbackDosenMainCtrl().getFeedbackDosenDetailCtrl() == null) {
                Events.sendEvent(new Event("onSelect", getFeedbackDosenMainCtrl().tabFeedbackDosenDetail, null));
                // if we work with spring beanCreation than we must check a
                // little bit deeper, because the Controller are preCreated ?
            } else if (getFeedbackDosenMainCtrl().getFeedbackDosenDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getFeedbackDosenMainCtrl().tabFeedbackDosenDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getFeedbackDosenMainCtrl().tabFeedbackDosenDetail, anFeedbackDosen));
        }
    }

    /**
     * When a listItem in the corresponding listbox is selected.<br>
     * Event is forwarded in the corresponding listbox.
     *
     * @param event
     */
    public void onSelect$listBoxFeedbackDosen(Event event) {
        // logger.debug(event.toString());

        // selectedFeedbackDosen is filled by annotated databinding mechanism
        Tfeedbackdosen anFeedbackDosen = getSelectedFeedbackDosen();
        //List<Tfeedbackdosen> ltfa = getSelectedFeedbackDosenList();
        //List<Tfeedbackdosen> ltfa = getFeedbackDosenService().getFeedbackDosenByNim(anFeedbackDosen.getMmahasiswa().getCnim());

        if (anFeedbackDosen == null) {
            return;
        }
        List<Tfeedbackdosen> ltfa = getFeedbackDosenService().getFeedbackDosenByNip(anFeedbackDosen.getMpegawai().getCnip(),
                anFeedbackDosen.getCterm(), anFeedbackDosen.getCkelompok());

        // check first, if the tabs are created
        if (getFeedbackDosenMainCtrl().getFeedbackDosenDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getFeedbackDosenMainCtrl().tabFeedbackDosenDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getFeedbackDosenMainCtrl().getFeedbackDosenDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getFeedbackDosenMainCtrl().tabFeedbackDosenDetail, null));
        }

        // INIT ALL RELATED Queries/OBJECTS/LISTS NEW
        getFeedbackDosenMainCtrl().getFeedbackDosenDetailCtrl().setSelectedFeedbackDosen(anFeedbackDosen);
        getFeedbackDosenMainCtrl().getFeedbackDosenDetailCtrl().setFeedbackDosen(anFeedbackDosen);

        getFeedbackDosenMainCtrl().getFeedbackDosenDetailCtrl().setSelectedFeedbackDosenList(ltfa);
        getFeedbackDosenMainCtrl().getFeedbackDosenDetailCtrl().setFeedbackDosenList(ltfa);
        /*
        // set the beans in the related databinded controllers
        getFeedbackDosenDetailCtrl().setFeedbackDosen(anFeedbackDosen);
        getFeedbackDosenDetailCtrl().setSelectedFeedbackDosen(anFeedbackDosen);

        getFeedbackDosenDetailCtrl().setFeedbackDosenList(listFeedbackDosen);
        getFeedbackDosenDetailCtrl().setSelectedFeedbackDosenList(listFeedbackDosen);
        getFeedbackDosenDetailCtrl().setSelectedFeedbackDosenList(getSelectedFeedbackDosenList());
        getFeedbackDosenDetailCtrl().setSelectedFeedbackDosenList(getSelectedFeedbackDosenList());
        if (getFeedbackDosenDetailCtrl().getBinder() != null) {
            getFeedbackDosenDetailCtrl().getBinder().loadAll();
        }
        PagedBindingListWrapper<Tfeedbackdosen> pblw = getFeedbackDosenDetailCtrl().getPagedBindingListWrapper();
        pblw.clear();
        pblw.addAll(getSelectedFeedbackDosenList());
        */
        // store the selected bean values as current
        getFeedbackDosenMainCtrl().doStoreInitValues();

        // show the objects data in the statusBar
        String str ="Feedback Dosen " + ": " + anFeedbackDosen.getMpegawai().getCnama();
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
        borderLayout_feedbackDosenList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowFeedbackDosenList.invalidate();
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
    public Tfeedbackdosen getFeedbackDosen() {
        // STORED IN THE module's MainController
        return getFeedbackDosenMainCtrl().getSelectedFeedbackDosen();
    }

    public void setFeedbackDosen(Tfeedbackdosen anFeedbackDosen) {
        // STORED IN THE module's MainController
        getFeedbackDosenMainCtrl().setSelectedFeedbackDosen(anFeedbackDosen);
    }

    public void setSelectedFeedbackDosen(Tfeedbackdosen selectedFeedbackDosen) {
        // STORED IN THE module's MainController
        getFeedbackDosenMainCtrl().setSelectedFeedbackDosen(selectedFeedbackDosen);
    }

    public Tfeedbackdosen getSelectedFeedbackDosen() {
        // STORED IN THE module's MainController
        return getFeedbackDosenMainCtrl().getSelectedFeedbackDosen();
    }

    public void setSelectedFeedbackDosenList(List<Tfeedbackdosen> selectedFeedbackDosenList) {
        // STORED IN THE module's MainController
        getFeedbackDosenMainCtrl().setSelectedFeedbackDosenList(selectedFeedbackDosenList);
    }

    public List<Tfeedbackdosen> getSelectedFeedbackDosenList() {
        // STORED IN THE module's MainController
        return getFeedbackDosenMainCtrl().getSelectedFeedbackDosenList();
    }

    public void setFeedbackDosens(BindingListModelList feedbackDosens) {
        // STORED IN THE module's MainController
        getFeedbackDosenMainCtrl().setFeedbackDosens(feedbackDosens);
    }

    public BindingListModelList getFeedbackDosens() {
        // STORED IN THE module's MainController
        return getFeedbackDosenMainCtrl().getFeedbackDosens();
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setSearchObj(HibernateSearchObject<Tfeedbackdosen> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Tfeedbackdosen> getSearchObj() {
        return this.searchObj;
    }

    public void setFeedbackDosenService(FeedbackDosenService feedbackDosenService) {
        this.feedbackDosenService = feedbackDosenService;
    }

    public FeedbackDosenService getFeedbackDosenService() {
        return this.feedbackDosenService;
    }

    public Listbox getListBoxFeedbackDosen() {
        return this.listBoxFeedbackDosen;
    }

    public void setListBoxFeedbackDosen(Listbox listBoxFeedbackDosen) {
        this.listBoxFeedbackDosen = listBoxFeedbackDosen;
    }

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void setFeedbackDosenMainCtrl(FeedbackDosenMainCtrl feedbackDosenMainCtrl) {
        this.feedbackDosenMainCtrl = feedbackDosenMainCtrl;
    }

    public FeedbackDosenMainCtrl getFeedbackDosenMainCtrl() {
        return this.feedbackDosenMainCtrl;
    }


}
