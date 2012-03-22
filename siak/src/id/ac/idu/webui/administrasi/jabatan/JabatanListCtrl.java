package id.ac.idu.webui.administrasi.jabatan;

import id.ac.idu.administrasi.service.JabatanService;
import id.ac.idu.backend.model.Mjabatan;
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
 * This is the controller class for the /WEB-INF/pages/jabatan/officeList.zul
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
public class JabatanListCtrl extends GFCBaseListCtrl<Mjabatan> implements Serializable {
    private static final long serialVersionUID = -2170565288232491362L;
    private static final Logger logger = Logger.getLogger(JabatanListCtrl.class);

    /*
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    * All the components that are defined here and have a corresponding
    * component with the same 'id' in the zul-file are getting autowired by our
    * 'extends GFCBaseCtrl' GenericForwardComposer.
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    */
    protected Window windowJabatanList; // autowired
    protected Panel panelJabatanList; // autowired

    protected Borderlayout borderLayout_jabatanList; // autowired
    protected Paging paging_JabatanList; // autowired
    protected Listbox listBoxJabatan; // autowired
    protected Listheader listheader_JabatanList_No; // autowired
    protected Listheader listheader_JabatanList_Name1; // autowired

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Mjabatan> searchObj;

    // row count for listbox
    private int countRows;

    // Databinding
    private AnnotateDataBinder binder;
    private JabatanMainCtrl jabatanMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient JabatanService jabatanService;

    /**
     * default constructor.<br>
     */
    public JabatanListCtrl() {
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
            setJabatanMainCtrl((JabatanMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getJabatanMainCtrl().setJabatanListCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getJabatanMainCtrl().getSelectedJabatan() != null) {
                setSelectedJabatan(getJabatanMainCtrl().getSelectedJabatan());
            } else
                setSelectedJabatan(null);
        } else {
            setSelectedJabatan(null);
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

    public void onCreate$windowJabatanList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();

        binder.loadAll();
    }

    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_JabatanList.setPageSize(getCountRows());
        paging_JabatanList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_JabatanList_No.setSortAscending(new FieldComparator("ckdjabatan", true));
        listheader_JabatanList_No.setSortDescending(new FieldComparator("ckdjabatan", false));
        listheader_JabatanList_Name1.setSortAscending(new FieldComparator("cnmjabatan", true));
        listheader_JabatanList_Name1.setSortDescending(new FieldComparator("cnmjabatan", false));

        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Mjabatan>(Mjabatan.class, getCountRows());
        searchObj.addSort("ckdjabatan", false);
        setSearchObj(searchObj);

        // Set the BindingListModel
        getPagedBindingListWrapper().init(searchObj, getListBoxJabatan(), paging_JabatanList);
        BindingListModelList lml = (BindingListModelList) getListBoxJabatan().getModel();
        setJabatans(lml);

        // check if first time opened and init databinding for selectedBean
        if (getSelectedJabatan() == null) {
            // init the bean with the first record in the List
            if (lml.getSize() > 0) {
                final int rowIndex = 0;
                // only for correct showing after Rendering. No effect as an
                // Event
                // yet.
                getListBoxJabatan().setSelectedIndex(rowIndex);
                // get the first entry and cast them to the needed object
                setSelectedJabatan((Mjabatan) lml.get(0));

                // call the onSelect Event for showing the objects data in the
                // statusBar
                Events.sendEvent(new Event("onSelect", getListBoxJabatan(), getSelectedJabatan()));
            }
        }

    }

    /**
     * Selects the object in the listbox and change the tab.<br>
     * Event is forwarded in the corresponding listbox.
     */
    public void onDoubleClickedJabatanItem(Event event) {
        // logger.debug(event.toString());

        Mjabatan anJabatan = getSelectedJabatan();

        if (anJabatan != null) {
            setSelectedJabatan(anJabatan);
            setJabatan(anJabatan);

            // check first, if the tabs are created
            if (getJabatanMainCtrl().getJabatanDetailCtrl() == null) {
                Events.sendEvent(new Event("onSelect", getJabatanMainCtrl().tabJabatanDetail, null));
                // if we work with spring beanCreation than we must check a
                // little bit deeper, because the Controller are preCreated ?
            } else if (getJabatanMainCtrl().getJabatanDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getJabatanMainCtrl().tabJabatanDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getJabatanMainCtrl().tabJabatanDetail, anJabatan));
        }
    }

    /**
     * When a listItem in the corresponding listbox is selected.<br>
     * Event is forwarded in the corresponding listbox.
     *
     * @param event
     */
    public void onSelect$listBoxJabatan(Event event) {
        // logger.debug(event.toString());

        // selectedJabatan is filled by annotated databinding mechanism
        Mjabatan anJabatan = getSelectedJabatan();

        if (anJabatan == null) {
            return;
        }

        // check first, if the tabs are created
        if (getJabatanMainCtrl().getJabatanDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getJabatanMainCtrl().tabJabatanDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getJabatanMainCtrl().getJabatanDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getJabatanMainCtrl().tabJabatanDetail, null));
        }

        // INIT ALL RELATED Queries/OBJECTS/LISTS NEW
        getJabatanMainCtrl().getJabatanDetailCtrl().setSelectedJabatan(anJabatan);
        getJabatanMainCtrl().getJabatanDetailCtrl().setJabatan(anJabatan);

        // store the selected bean values as current
        getJabatanMainCtrl().doStoreInitValues();

        // show the objects data in the statusBar
        String str = Labels.getLabel("common.Jabatan") + ": " + anJabatan.toString();
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
        borderLayout_jabatanList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowJabatanList.invalidate();
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
    public Mjabatan getJabatan() {
        // STORED IN THE module's MainController
        return getJabatanMainCtrl().getSelectedJabatan();
    }

    public void setJabatan(Mjabatan anJabatan) {
        // STORED IN THE module's MainController
        getJabatanMainCtrl().setSelectedJabatan(anJabatan);
    }

    public void setSelectedJabatan(Mjabatan selectedJabatan) {
        // STORED IN THE module's MainController
        getJabatanMainCtrl().setSelectedJabatan(selectedJabatan);
    }

    public Mjabatan getSelectedJabatan() {
        // STORED IN THE module's MainController
        return getJabatanMainCtrl().getSelectedJabatan();
    }

    public void setJabatans(BindingListModelList offices) {
        // STORED IN THE module's MainController
        getJabatanMainCtrl().setJabatans(offices);
    }

    public BindingListModelList getJabatans() {
        // STORED IN THE module's MainController
        return getJabatanMainCtrl().getJabatans();
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setSearchObj(HibernateSearchObject<Mjabatan> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Mjabatan> getSearchObj() {
        return this.searchObj;
    }

    public void setJabatanService(JabatanService jabatanService) {
        this.jabatanService = jabatanService;
    }

    public JabatanService getJabatanService() {
        return this.jabatanService;
    }

    public Listbox getListBoxJabatan() {
        return this.listBoxJabatan;
    }

    public void setListBoxJabatan(Listbox listBoxJabatan) {
        this.listBoxJabatan = listBoxJabatan;
    }

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void setJabatanMainCtrl(JabatanMainCtrl jabatanMainCtrl) {
        this.jabatanMainCtrl = jabatanMainCtrl;
    }

    public JabatanMainCtrl getJabatanMainCtrl() {
        return this.jabatanMainCtrl;
    }

}
