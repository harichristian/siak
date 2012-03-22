package id.ac.idu.webui.administrasi.calakademik;

import id.ac.idu.administrasi.service.CalakademikService;
import id.ac.idu.backend.model.Mcalakademik;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.webui.util.GFCBaseListCtrl;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.*;

import java.io.Serializable;

/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the /WEB-INF/pages/calakademik/officeList.zul
 * file.<br>
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 *
 * @author Hari
 * @changes 05/15/2009: sge Migrating the list models for paging. <br>
 * 07/24/2009: sge changings for clustering.<br>
 * 10/12/2009: sge changings in the saving routine.<br>
 * 11/07/2009: bbr changed to extending from GFCBaseCtrl<br>
 * (GenericForwardComposer) for spring-managed creation.<br>
 * 03/09/2009: sge changed for allow repainting after resizing.<br>
 * with the refresh button.<br>
 * 07/03/2010: sge modified for zk5.x with complete Annotated
 * Databinding.<br>
 */
public class CalakademikListCtrl extends GFCBaseListCtrl<Mcalakademik> implements Serializable {
    private static final long serialVersionUID = -2170565288232491362L;
    private static final Logger logger = Logger.getLogger(CalakademikListCtrl.class);

    /*
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    * All the components that are defined here and have a corresponding
    * component with the same 'id' in the zul-file are getting autowired by our
    * 'extends GFCBaseCtrl' GenericForwardComposer.
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    */
    protected Window windowCalakademikList; // autowired
    protected Panel panelCalakademikList; // autowired

    protected Borderlayout borderLayout_calakademikList; // autowired
    protected Paging paging_CalakademikList; // autowired
    protected Listbox listBoxCalakademik; // autowired
    protected Listheader listheader_CalakademikList_No; // autowired
    protected Listheader listheader_CalakademikList_TanggalAwal;
    protected Listheader listheader_CalakademikList_TanggalAkhir;
    protected Listheader listheader_CalakademikList_JamAwal;
    protected Listheader listheader_CalakademikList_JamAkhir;
    protected Listheader listheader_CalakademikList_Term;
    protected Listheader listheader_CalakademikList_TahunAjaran;
    protected Listheader listheader_CalakademikList_Semester;
    protected Listheader listheader_CalakademikList_Sekolah;
    protected Listheader listheader_CalakademikList_Prodi;
    protected Listheader listheader_CalakademikList_Kegiatan;


    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Mcalakademik> searchObj;

    // row count for listbox
    private int countRows;

    // Databinding
    private AnnotateDataBinder binder;
    private CalakademikMainCtrl calakademikMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient CalakademikService calakademikService;

    /**
     * default constructor.<br>
     */
    public CalakademikListCtrl() {
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
            setCalakademikMainCtrl((CalakademikMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getCalakademikMainCtrl().setCalakademikListCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getCalakademikMainCtrl().getSelectedCalakademik() != null) {
                setSelectedCalakademik(getCalakademikMainCtrl().getSelectedCalakademik());
            } else
                setSelectedCalakademik(null);
        } else {
            setSelectedCalakademik(null);
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

    public void onCreate$windowCalakademikList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();

        binder.loadAll();
    }

    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_CalakademikList.setPageSize(getCountRows());
        paging_CalakademikList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_CalakademikList_No.setSortAscending(new FieldComparator("cno", true));
        listheader_CalakademikList_No.setSortDescending(new FieldComparator("cno", false));
        listheader_CalakademikList_TanggalAwal.setSortAscending(new FieldComparator("dtglawal", true));
        listheader_CalakademikList_TanggalAkhir.setSortAscending(new FieldComparator("dtglakhir", true));
        listheader_CalakademikList_JamAwal.setSortAscending(new FieldComparator("cjamawal", true));
        listheader_CalakademikList_JamAkhir.setSortAscending(new FieldComparator("cjamakhir", true));
        listheader_CalakademikList_Term.setSortAscending(new FieldComparator("cterm", true));
        listheader_CalakademikList_TahunAjaran.setSortAscending(new FieldComparator("cthajar", true));
        listheader_CalakademikList_Semester.setSortAscending(new FieldComparator("csmt", true));
        listheader_CalakademikList_Sekolah.setSortAscending(new FieldComparator("msekolah.cnamaSekolah", true));
        listheader_CalakademikList_Prodi.setSortAscending(new FieldComparator("mprodi.cnmprogst", true));
        listheader_CalakademikList_Kegiatan.setSortAscending(new FieldComparator("mkegiatan.cnmkgt", true));
        listheader_CalakademikList_TanggalAwal.setSortDescending(new FieldComparator("dtglawal", false));
        listheader_CalakademikList_TanggalAkhir.setSortDescending(new FieldComparator("dtglakhir", false));
        listheader_CalakademikList_JamAwal.setSortDescending(new FieldComparator("cjamawal", false));
        listheader_CalakademikList_JamAkhir.setSortDescending(new FieldComparator("cjamakhir", false));
        listheader_CalakademikList_Term.setSortDescending(new FieldComparator("cterm", false));
        listheader_CalakademikList_TahunAjaran.setSortDescending(new FieldComparator("cthajar", false));
        listheader_CalakademikList_Semester.setSortDescending(new FieldComparator("csmt", false));
        listheader_CalakademikList_Sekolah.setSortDescending(new FieldComparator("msekolah.cnamaSekolah", false));
        listheader_CalakademikList_Prodi.setSortDescending(new FieldComparator("mprodi.cnmprogst", false));
        listheader_CalakademikList_Kegiatan.setSortDescending(new FieldComparator("mkegiatan.cnmkgt", false));

        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Mcalakademik>(Mcalakademik.class, getCountRows());
        searchObj.addSort("cno", false);
        setSearchObj(searchObj);

        // Set the BindingListModel
        getPagedBindingListWrapper().init(searchObj, getListBoxCalakademik(), paging_CalakademikList);
        BindingListModelList lml = (BindingListModelList) getListBoxCalakademik().getModel();
        setCalakademiks(lml);

        // check if first time opened and init databinding for selectedBean
        if (getSelectedCalakademik() == null) {
            // init the bean with the first record in the List
            if (lml.getSize() > 0) {
                final int rowIndex = 0;
                // only for correct showing after Rendering. No effect as an
                // Event
                // yet.
                getListBoxCalakademik().setSelectedIndex(rowIndex);
                // get the first entry and cast them to the needed object
                setSelectedCalakademik((Mcalakademik) lml.get(0));

                // call the onSelect Event for showing the objects data in the
                // statusBar
                Events.sendEvent(new Event("onSelect", getListBoxCalakademik(), getSelectedCalakademik()));
            }
        }

    }

    /**
     * Selects the object in the listbox and change the tab.<br>
     * Event is forwarded in the corresponding listbox.
     */
    public void onDoubleClickedCalakademikItem(Event event) {
        // logger.debug(event.toString());

        Mcalakademik anCalakademik = getSelectedCalakademik();

        if (anCalakademik != null) {
            setSelectedCalakademik(anCalakademik);
            setCalakademik(anCalakademik);

            // check first, if the tabs are created
            if (getCalakademikMainCtrl().getCalakademikDetailCtrl() == null) {
                Events.sendEvent(new Event("onSelect", getCalakademikMainCtrl().tabCalakademikDetail, null));
                // if we work with spring beanCreation than we must check a
                // little bit deeper, because the Controller are preCreated ?
            } else if (getCalakademikMainCtrl().getCalakademikDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getCalakademikMainCtrl().tabCalakademikDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getCalakademikMainCtrl().tabCalakademikDetail, anCalakademik));
        }
    }

    /**
     * When a listItem in the corresponding listbox is selected.<br>
     * Event is forwarded in the corresponding listbox.
     *
     * @param event
     */
    public void onSelect$listBoxCalakademik(Event event) {
        // logger.debug(event.toString());

        // selectedCalakademik is filled by annotated databinding mechanism
        Mcalakademik anCalakademik = getSelectedCalakademik();

        if (anCalakademik == null) {
            return;
        }

        // check first, if the tabs are created
        if (getCalakademikMainCtrl().getCalakademikDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getCalakademikMainCtrl().tabCalakademikDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getCalakademikMainCtrl().getCalakademikDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getCalakademikMainCtrl().tabCalakademikDetail, null));
        }

        // INIT ALL RELATED Queries/OBJECTS/LISTS NEW
        getCalakademikMainCtrl().getCalakademikDetailCtrl().setSelectedCalakademik(anCalakademik);
        getCalakademikMainCtrl().getCalakademikDetailCtrl().setCalakademik(anCalakademik);

        // store the selected bean values as current
        getCalakademikMainCtrl().doStoreInitValues();

        // show the objects data in the statusBar
        String str = Labels.getLabel("common.Calakademik") + ": " + anCalakademik.toString();
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
        borderLayout_calakademikList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowCalakademikList.invalidate();
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
    public Mcalakademik getCalakademik() {
        // STORED IN THE module's MainController
        return getCalakademikMainCtrl().getSelectedCalakademik();
    }

    public void setCalakademik(Mcalakademik anCalakademik) {
        // STORED IN THE module's MainController
        getCalakademikMainCtrl().setSelectedCalakademik(anCalakademik);
    }

    public void setSelectedCalakademik(Mcalakademik selectedCalakademik) {
        // STORED IN THE module's MainController
        getCalakademikMainCtrl().setSelectedCalakademik(selectedCalakademik);
    }

    public Mcalakademik getSelectedCalakademik() {
        // STORED IN THE module's MainController
        return getCalakademikMainCtrl().getSelectedCalakademik();
    }

    public void setCalakademiks(BindingListModelList offices) {
        // STORED IN THE module's MainController
        getCalakademikMainCtrl().setCalakademiks(offices);
    }

    public BindingListModelList getCalakademiks() {
        // STORED IN THE module's MainController
        return getCalakademikMainCtrl().getCalakademiks();
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setSearchObj(HibernateSearchObject<Mcalakademik> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Mcalakademik> getSearchObj() {
        return this.searchObj;
    }

    public void setCalakademikService(CalakademikService calakademikService) {
        this.calakademikService = calakademikService;
    }

    public CalakademikService getCalakademikService() {
        return this.calakademikService;
    }

    public Listbox getListBoxCalakademik() {
        return this.listBoxCalakademik;
    }

    public void setListBoxCalakademik(Listbox listBoxCalakademik) {
        this.listBoxCalakademik = listBoxCalakademik;
    }

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void setCalakademikMainCtrl(CalakademikMainCtrl calakademikMainCtrl) {
        this.calakademikMainCtrl = calakademikMainCtrl;
    }

    public CalakademikMainCtrl getCalakademikMainCtrl() {
        return this.calakademikMainCtrl;
    }

}
