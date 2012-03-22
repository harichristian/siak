package id.ac.idu.webui.administrasi.calendarakademik;

import id.ac.idu.administrasi.service.CalendarAkademikService;
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
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 09/03/12
 * Time: 12:02
 * To change this template use File | Settings | File Templates.
 */
public class CalendarAkademikListCtrl extends GFCBaseListCtrl<Mcalakademik> implements Serializable {
    private static final long serialVersionUID = -2170565288232491362L;
    private static final Logger logger = Logger.getLogger(CalendarAkademikListCtrl.class);

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
    protected Listheader listheader_CalakademikList_Name1; // autowired

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Mcalakademik> searchObj;

    // row count for listbox
    private int countRows;

    // Databinding
    private AnnotateDataBinder binder;
    private CalendarAkademikMainCtrl calendarAkademikMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient CalendarAkademikService calendarAkademikService;

    /**
     * default constructor.<br>
     */
    public CalendarAkademikListCtrl() {
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
            setCalendarAkademikMainCtrl((CalendarAkademikMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getCalendarAkademikMainCtrl().setCalendarAkademikListCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getCalendarAkademikMainCtrl().getSelectedCalakademik() != null) {
                setSelectedCalakademik(getCalendarAkademikMainCtrl().getSelectedCalakademik());
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
        listheader_CalakademikList_No.setSortAscending(new FieldComparator("ckdcalakademik", true));
        listheader_CalakademikList_No.setSortDescending(new FieldComparator("ckdcalakademik", false));
        listheader_CalakademikList_Name1.setSortAscending(new FieldComparator("cnmcalakademik", true));
        listheader_CalakademikList_Name1.setSortDescending(new FieldComparator("cnmcalakademik", false));

        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Mcalakademik>(Mcalakademik.class, getCountRows());
        searchObj.addSort("ckdcalakademik", false);
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
            if (getCalendarAkademikMainCtrl().getCalakademiks() == null) {
                Events.sendEvent(new Event("onSelect", getCalendarAkademikMainCtrl().tabCalakademikDetail, null));
                // if we work with spring beanCreation than we must check a
                // little bit deeper, because the Controller are preCreated ?
            } else if (getCalendarAkademikMainCtrl().getCalendarAkademikDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getCalendarAkademikMainCtrl().tabCalakademikDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getCalendarAkademikMainCtrl().tabCalakademikDetail, anCalakademik));
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
        if (getCalendarAkademikMainCtrl().getCalendarAkademikDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getCalendarAkademikMainCtrl().tabCalakademikDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getCalendarAkademikMainCtrl().getCalendarAkademikDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getCalendarAkademikMainCtrl().tabCalakademikDetail, null));
        }

        // INIT ALL RELATED Queries/OBJECTS/LISTS NEW
        getCalendarAkademikMainCtrl().getCalendarAkademikDetailCtrl().setSelectedCalakademik(anCalakademik);
        getCalendarAkademikMainCtrl().getCalendarAkademikDetailCtrl().setCalakademik(anCalakademik);

        // store the selected bean values as current
        getCalendarAkademikMainCtrl().doStoreInitValues();

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
        return getCalendarAkademikMainCtrl().getSelectedCalakademik();
    }

    public void setCalakademik(Mcalakademik anCalakademik) {
        // STORED IN THE module's MainController
        getCalendarAkademikMainCtrl().setSelectedCalakademik(anCalakademik);
    }

    public void setSelectedCalakademik(Mcalakademik selectedCalakademik) {
        // STORED IN THE module's MainController
        getCalendarAkademikMainCtrl().setSelectedCalakademik(selectedCalakademik);
    }

    public Mcalakademik getSelectedCalakademik() {
        // STORED IN THE module's MainController
        return getCalendarAkademikMainCtrl().getSelectedCalakademik();
    }

    public void setCalakademiks(BindingListModelList offices) {
        // STORED IN THE module's MainController
        getCalendarAkademikMainCtrl().setCalakademiks(offices);
    }

    public BindingListModelList getCalakademiks() {
        // STORED IN THE module's MainController
        return getCalendarAkademikMainCtrl().getCalakademiks();
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

    public void setCalendarAkademikService(CalendarAkademikService calendarAkademikService) {
        this.calendarAkademikService = calendarAkademikService;
    }

    public CalendarAkademikService getCalendarAkademikService() {
        return this.calendarAkademikService;
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

    public void setCalendarAkademikMainCtrl(CalendarAkademikMainCtrl calendarAkademikMainCtrl) {
        this.calendarAkademikMainCtrl = calendarAkademikMainCtrl;
    }

    public CalendarAkademikMainCtrl getCalendarAkademikMainCtrl() {
        return this.calendarAkademikMainCtrl;
    }

}
