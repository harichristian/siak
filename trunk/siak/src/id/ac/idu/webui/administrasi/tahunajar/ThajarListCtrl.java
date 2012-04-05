package id.ac.idu.webui.administrasi.tahunajar;

import id.ac.idu.administrasi.service.ThajarService;
import id.ac.idu.backend.model.Mthajar;
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
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 05/04/12
 * Time: 14:50
 * To change this template use File | Settings | File Templates.
 */
public class ThajarListCtrl extends GFCBaseListCtrl<Mthajar> implements Serializable {
    private static final long serialVersionUID = -2170565288232491362L;
    private static final Logger logger = Logger.getLogger(ThajarListCtrl.class);

    /*
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    * All the components that are defined here and have a corresponding
    * component with the same 'id' in the zul-file are getting autowired by our
    * 'extends GFCBaseCtrl' GenericForwardComposer.
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    */
    protected Window windowThajarList; // autowired
    protected Panel panelThajarList; // autowired

    protected Borderlayout borderLayout_thajarList; // autowired
    protected Paging paging_ThajarList; // autowired
    protected Listbox listBoxThajar; // autowired
    protected Listheader listheader_ThajarList_No; // autowired
    protected Listheader listheader_ThajarList_Name1; // autowired

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Mthajar> searchObj;

    // row count for listbox
    private int countRows;

    // Databinding
    private AnnotateDataBinder binder;
    private ThajarMainCtrl thajarMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient ThajarService thajarService;

    /**
     * default constructor.<br>
     */
    public ThajarListCtrl() {
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
            setThajarMainCtrl((ThajarMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getThajarMainCtrl().setThajarListCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getThajarMainCtrl().getSelectedThajar() != null) {
                setSelectedThajar(getThajarMainCtrl().getSelectedThajar());
            } else
                setSelectedThajar(null);
        } else {
            setSelectedThajar(null);
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

    public void onCreate$windowThajarList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();

        binder.loadAll();
    }

    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_ThajarList.setPageSize(getCountRows());
        paging_ThajarList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_ThajarList_No.setSortAscending(new FieldComparator("cthajar", true));
        listheader_ThajarList_No.setSortDescending(new FieldComparator("cthajar", false));
        listheader_ThajarList_Name1.setSortAscending(new FieldComparator("csmt", true));
        listheader_ThajarList_Name1.setSortDescending(new FieldComparator("csmt", false));

        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Mthajar>(Mthajar.class, getCountRows());
        searchObj.addSort("cthajar", false);
        setSearchObj(searchObj);

        // Set the BindingListModel
        getPagedBindingListWrapper().init(searchObj, getListBoxThajar(), paging_ThajarList);
        BindingListModelList lml = (BindingListModelList) getListBoxThajar().getModel();
        setThajars(lml);

        // check if first time opened and init databinding for selectedBean
        if (getSelectedThajar() == null) {
            // init the bean with the first record in the List
            if (lml.getSize() > 0) {
                final int rowIndex = 0;
                // only for correct showing after Rendering. No effect as an
                // Event
                // yet.
                getListBoxThajar().setSelectedIndex(rowIndex);
                // get the first entry and cast them to the needed object
                setSelectedThajar((Mthajar) lml.get(0));

                // call the onSelect Event for showing the objects data in the
                // statusBar
                Events.sendEvent(new Event("onSelect", getListBoxThajar(), getSelectedThajar()));
            }
        }

    }

    /**
     * Selects the object in the listbox and change the tab.<br>
     * Event is forwarded in the corresponding listbox.
     */
    public void onDoubleClickedThajarItem(Event event) {
        // logger.debug(event.toString());

        Mthajar anThajar = getSelectedThajar();

        if (anThajar != null) {
            setSelectedThajar(anThajar);
            setThajar(anThajar);

            // check first, if the tabs are created
            if (getThajarMainCtrl().getThajarDetailCtrl() == null) {
                Events.sendEvent(new Event("onSelect", getThajarMainCtrl().tabThajarDetail, null));
                // if we work with spring beanCreation than we must check a
                // little bit deeper, because the Controller are preCreated ?
            } else if (getThajarMainCtrl().getThajarDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getThajarMainCtrl().tabThajarDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getThajarMainCtrl().tabThajarDetail, anThajar));
        }
    }

    /**
     * When a listItem in the corresponding listbox is selected.<br>
     * Event is forwarded in the corresponding listbox.
     *
     * @param event
     */
    public void onSelect$listBoxThajar(Event event) {
        // logger.debug(event.toString());

        // selectedThajar is filled by annotated databinding mechanism
        Mthajar anThajar = getSelectedThajar();

        if (anThajar == null) {
            return;
        }

        // check first, if the tabs are created
        if (getThajarMainCtrl().getThajarDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getThajarMainCtrl().tabThajarDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getThajarMainCtrl().getThajarDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getThajarMainCtrl().tabThajarDetail, null));
        }

        // INIT ALL RELATED Queries/OBJECTS/LISTS NEW
        getThajarMainCtrl().getThajarDetailCtrl().setSelectedThajar(anThajar);
        getThajarMainCtrl().getThajarDetailCtrl().setThajar(anThajar);

        // store the selected bean values as current
        getThajarMainCtrl().doStoreInitValues();

        // show the objects data in the statusBar
        String str = Labels.getLabel("common.Thajar") + ": " + anThajar.toString();
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
        borderLayout_thajarList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowThajarList.invalidate();
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
    public Mthajar getThajar() {
        // STORED IN THE module's MainController
        return getThajarMainCtrl().getSelectedThajar();
    }

    public void setThajar(Mthajar anThajar) {
        // STORED IN THE module's MainController
        getThajarMainCtrl().setSelectedThajar(anThajar);
    }

    public void setSelectedThajar(Mthajar selectedThajar) {
        // STORED IN THE module's MainController
        getThajarMainCtrl().setSelectedThajar(selectedThajar);
    }

    public Mthajar getSelectedThajar() {
        // STORED IN THE module's MainController
        return getThajarMainCtrl().getSelectedThajar();
    }

    public void setThajars(BindingListModelList offices) {
        // STORED IN THE module's MainController
        getThajarMainCtrl().setThajars(offices);
    }

    public BindingListModelList getThajars() {
        // STORED IN THE module's MainController
        return getThajarMainCtrl().getThajars();
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setSearchObj(HibernateSearchObject<Mthajar> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Mthajar> getSearchObj() {
        return this.searchObj;
    }

    public void setThajarService(ThajarService thajarService) {
        this.thajarService = thajarService;
    }

    public ThajarService getThajarService() {
        return this.thajarService;
    }

    public Listbox getListBoxThajar() {
        return this.listBoxThajar;
    }

    public void setListBoxThajar(Listbox listBoxThajar) {
        this.listBoxThajar = listBoxThajar;
    }

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void setThajarMainCtrl(ThajarMainCtrl thajarMainCtrl) {
        this.thajarMainCtrl = thajarMainCtrl;
    }

    public ThajarMainCtrl getThajarMainCtrl() {
        return this.thajarMainCtrl;
    }

}
