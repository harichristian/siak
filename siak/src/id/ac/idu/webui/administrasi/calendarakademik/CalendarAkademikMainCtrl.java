package id.ac.idu.webui.administrasi.calendarakademik;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.CalendarAkademikService;
import id.ac.idu.backend.model.Mcalakademik;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.webui.util.*;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.*;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 09/03/12
 * Time: 12:01
 * To change this template use File | Settings | File Templates.
 */
public class CalendarAkademikMainCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(CalendarAkademikMainCtrl.class);

    /*
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    * All the components that are defined here and have a corresponding
    * component with the same 'id' in the zul-file are getting autowired by our
    * 'extends GFCBaseCtrl' GenericForwardComposer.
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    */
    protected Window windowCalakademikMain; // autowired

    // Tabs
    protected Tabbox tabbox_CalakademikMain; // autowired
    protected Tab tabCalakademikList; // autowired
    protected Tab tabCalakademikDetail; // autowired
    protected Tabpanel tabPanelCalakademikList; // autowired
    protected Tabpanel tabPanelCalakademikDetail; // autowired

    // filter components
    protected Checkbox checkbox_CalakademikList_ShowAll; // autowired
    protected Textbox txtb_Calakademik_No; // aurowired
    protected Button button_CalakademikList_SearchNo; // aurowired
    protected Textbox txtb_Calakademik_Name; // aurowired
    protected Button button_CalakademikList_SearchName; // aurowired

    // checkRights
    protected Button button_CalakademikList_PrintList;

    // Button controller for the CRUD buttons
    private final String btnCtroller_ClassPrefix = "button_CalakademikMain_";
    private ButtonStatusCtrl btnCtrlCalakademik;
    protected Button btnNew; // autowired
    protected Button btnEdit; // autowired
    protected Button btnDelete; // autowired
    protected Button btnSave; // autowired
    protected Button btnCancel; // autowired

    protected Button btnHelp;

    // Tab-Controllers for getting the binders
    private CalendarAkademikListCtrl calakademikListCtrl;
    private CalendarAkademikDetailCtrl calakademikDetailCtrl;

    // Databinding
    private Mcalakademik selectedCalakademik;
    private BindingListModelList calakademiks;

    // ServiceDAOs / Domain Classes
    private CalendarAkademikService calakademikService;

    // always a copy from the bean before modifying. Used for reseting
    private Mcalakademik originalCalakademik;

    /**
     * default constructor.<br>
     */
    public CalendarAkademikMainCtrl() {
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
    public void onCreate$windowCalakademikMain(Event event) throws Exception {
        windowCalakademikMain.setContentStyle("padding:0px;");

        // create the Button Controller. Disable not used buttons during working
        btnCtrlCalakademik = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

//                doCheckRights();

        /**
         * Initiate the first loading by selecting the customerList tab and
         * create the components from the zul-file.
         */
        tabCalakademikList.setSelected(true);

        if (tabPanelCalakademikList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelCalakademikList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/kalender/CalendarAkademikList.zul");
        }

        // init the buttons for editMode
        btnCtrlCalakademik.setInitEdit();
    }

    /**
     * When the tab 'tabCalakademikList' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabCalakademikList(Event event) throws IOException {
        logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelCalakademikList.getFirstChild() != null) {
            tabCalakademikList.setSelected(true);

            return;
        }

        if (tabPanelCalakademikList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelCalakademikList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/kalender/CalendarAkademikList.zul");
        }

    }

    /**
     * When the tab 'tabPanelCalakademikDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws IOException
     */
    public void onSelect$tabCalakademikDetail(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelCalakademikDetail.getFirstChild() != null) {
            tabCalakademikDetail.setSelected(true);

            // refresh the Binding mechanism
            getCalendarAkademikDetailCtrl().setCalakademik(getSelectedCalakademik());
            getCalendarAkademikDetailCtrl().getBinder().loadAll();
            return;
        }

        if (tabPanelCalakademikDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelCalakademikDetail, this, "ModuleMainController", "/WEB-INF/pages/administrasi/kalender/CalendarAkademikDetail.zul");
        }
    }

    /**
     * when the "print calakademiks list" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_CalakademikList_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        //new CalakademikSimpleDJReport(win); // TODO
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_CalakademikList_ShowAll(Event event) {
        // logger.debug(event.toString());

        // empty the text search boxes
        txtb_Calakademik_No.setValue(""); // clear
        txtb_Calakademik_Name.setValue(""); // clear

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Mcalakademik> soCalakademik = new HibernateSearchObject<Mcalakademik>(Mcalakademik.class, getCalendarAkademikListCtrl().getCountRows());
        soCalakademik.addSort("filName1", false);

        // Change the BindingListModel.
        if (getCalendarAkademikListCtrl().getBinder() != null) {
            getCalendarAkademikListCtrl().getPagedBindingListWrapper().setSearchObject(soCalakademik);

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_CalakademikMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabCalakademikList)) {
                tabCalakademikList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }

    }

    /**
     * Filter the calakademik list with 'like calakademik number'. <br>
     */
    public void onClick$button_CalakademikList_SearchNo(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Calakademik_No.getValue().isEmpty()) {
            checkbox_CalakademikList_ShowAll.setChecked(false); // unCheck
            txtb_Calakademik_Name.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mcalakademik> soCalakademik = new HibernateSearchObject<Mcalakademik>(Mcalakademik.class, getCalendarAkademikListCtrl().getCountRows());
            soCalakademik.addFilter(new Filter("filNr", "%" + txtb_Calakademik_No.getValue() + "%", Filter.OP_ILIKE));
            soCalakademik.addSort("filNr", false);

            // Change the BindingListModel.
            if (getCalendarAkademikListCtrl().getBinder() != null) {
                getCalendarAkademikListCtrl().getPagedBindingListWrapper().setSearchObject(soCalakademik);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_CalakademikMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabCalakademikList)) {
                    tabCalakademikList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the calakademik list with 'like calakademik name'. <br>
     */
    public void onClick$button_CalakademikList_SearchName(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Calakademik_Name.getValue().isEmpty()) {
            checkbox_CalakademikList_ShowAll.setChecked(false); // unCheck
            txtb_Calakademik_No.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mcalakademik> soCalakademik = new HibernateSearchObject<Mcalakademik>(Mcalakademik.class, getCalendarAkademikListCtrl().getCountRows());
            soCalakademik.addFilter(new Filter("filName1", "%" + txtb_Calakademik_Name.getValue() + "%", Filter.OP_ILIKE));
            soCalakademik.addSort("filName1", false);

            // Change the BindingListModel.
            if (getCalendarAkademikListCtrl().getBinder() != null) {
                getCalendarAkademikListCtrl().getPagedBindingListWrapper().setSearchObject(soCalakademik);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_CalakademikMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabCalakademikList)) {
                    tabCalakademikList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * When the "help" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$btnHelp(Event event) throws InterruptedException {
        doHelp(event);
    }

    /**
     * When the "new" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$btnNew(Event event) throws InterruptedException {
        doNew(event);
    }

    /**
     * When the "save" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$btnSave(Event event) throws InterruptedException {
        doSave(event);
    }

    /**
     * When the "cancel" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$btnEdit(Event event) throws InterruptedException {
        doEdit(event);
    }

    /**
     * When the "delete" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$btnDelete(Event event) throws InterruptedException {
        doDelete(event);
    }

    /**
     * When the "cancel" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$btnCancel(Event event) throws InterruptedException {
        doCancel(event);
    }

    /**
     * when the "refresh" button is clicked. <br>
     * <br>
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$btnRefresh(Event event) throws InterruptedException {
        doResizeSelectedTab(event);
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // +++++++++++++++++ Business Logic ++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    /**
     * 1. Cancel the current action.<br>
     * 2. Reset the values to its origin.<br>
     * 3. Set UI components back to readonly/disable mode.<br>
     * 4. Set the buttons in edit mode.<br>
     *
     * @param event
     * @throws InterruptedException
     */
    private void doCancel(Event event) throws InterruptedException {
        // logger.debug(event.toString());

        // reset to the original object
        doResetToInitValues();

        // check first, if the tabs are created
        if (getCalendarAkademikDetailCtrl().getBinder() != null) {

            // refresh all dataBinder related controllers/components
            getCalendarAkademikDetailCtrl().getBinder().loadAll();

            // set editable Mode
            getCalendarAkademikDetailCtrl().doReadOnlyMode(true);

            btnCtrlCalakademik.setInitEdit();
        }
    }

    /**
     * Sets all UI-components to writable-mode. Sets the buttons to edit-Mode.
     * Checks first, if the NEEDED TABS with its contents are created. If not,
     * than create it by simulate an onSelect() with calling Events.sendEvent()
     *
     * @param event
     * @throws InterruptedException
     */
    private void doEdit(Event event) {
        // logger.debug(event.toString());

        // get the current Tab for later checking if we must change it
        Tab currentTab = tabbox_CalakademikMain.getSelectedTab();

        // check first, if the tabs are created, if not than create it
        if (getCalendarAkademikDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabCalakademikDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getCalendarAkademikDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabCalakademikDetail, null));
        }

        // check if the tab is one of the Detail tabs. If so do not change the
        // selection of it
        if (!currentTab.equals(tabCalakademikDetail)) {
            tabCalakademikDetail.setSelected(true);
        } else {
            currentTab.setSelected(true);
        }

        getCalendarAkademikDetailCtrl().getBinder().loadAll();

        // remember the old vars
        doStoreInitValues();

        btnCtrlCalakademik.setBtnStatus_Edit();

        getCalendarAkademikDetailCtrl().doReadOnlyMode(false);
        // set focus
        getCalendarAkademikDetailCtrl().txtb_filNr.focus();
    }

    /**
     * Deletes the selected Bean from the DB.
     *
     * @param event
     * @throws InterruptedException
     * @throws InterruptedException
     */
    private void doDelete(Event event) throws InterruptedException {
        // logger.debug(event.toString());

        // check first, if the tabs are created, if not than create them
        if (getCalendarAkademikDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabCalakademikDetail, null));
        }

        // check first, if the tabs are created
        if (getCalendarAkademikDetailCtrl().getBinder() == null) {
            return;
        }

        final Mcalakademik anCalakademik = getSelectedCalakademik();
        if (anCalakademik != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anCalakademik.toString();
            final String title = Labels.getLabel("message.Deleting.Record");

            MultiLineMessageBox.doSetTemplate();
            if (MultiLineMessageBox.show(msg, title, Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, true, new EventListener() {
                @Override
                public void onEvent(Event evt) {
                    switch (((Integer) evt.getData()).intValue()) {
                        case MultiLineMessageBox.YES:
                            try {
                                deleteBean();
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            break; //
                        case MultiLineMessageBox.NO:
                            break; //
                    }
                }

                private void deleteBean() throws InterruptedException {

                    /**
                     * Do not allow to modify the demo calakademiks
                     */
                    if (getCalendarAkademikDetailCtrl().getCalakademik().getId() <= 2) {
                        try {
                            ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
                            return;
                        } catch (final InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        try {
                            getCalendarAkademikService().delete(anCalakademik);
                        } catch (DataAccessException e) {
                            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                        }
                    }
                }

            }

            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrlCalakademik.setInitEdit();

        setSelectedCalakademik(null);
        // refresh the list
        getCalendarAkademikListCtrl().doFillListbox();

        // refresh all dataBinder related controllers
        getCalendarAkademikDetailCtrl().getBinder().loadAll();
    }

    /**
     * Saves all involved Beans to the DB.
     *
     * @param event
     * @throws InterruptedException
     */
    private void doSave(Event event) throws InterruptedException {
        // logger.debug(event.toString());

        // save all components data in the several tabs to the bean
        getCalendarAkademikDetailCtrl().getBinder().saveAll();

        try {

            /**
             * Do not allow to modify the demo calakademiks
             */
//                if (getCalendarAkademikDetailCtrl().getCalakademik().getId() <= 2) {
//                    ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
//
//                    doResetToInitValues();
//                    getCalendarAkademikDetailCtrl().getBinder().loadAll();
//                    return;
//                }

            // save it to database
            getCalendarAkademikService().saveOrUpdate(getCalendarAkademikDetailCtrl().getCalakademik());
            // if saving is successfully than actualize the beans as
            // origins.
            doStoreInitValues();
            // refresh the list
            getCalendarAkademikListCtrl().doFillListbox();
            // later refresh StatusBar
            Events.postEvent("onSelect", getCalendarAkademikListCtrl().getListBoxCalakademik(), getSelectedCalakademik());

            // show the objects data in the statusBar
            String str = getSelectedCalakademik().toString();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetToInitValues();

            return;

        } finally {
            btnCtrlCalakademik.setInitEdit();
            getCalendarAkademikDetailCtrl().doReadOnlyMode(true);
        }
    }

    /**
     * Sets all UI-components to writable-mode. Stores the current Beans as
     * originBeans and get new Objects from the backend.
     *
     * @param event
     * @throws InterruptedException
     */
    private void doNew(Event event) {
        // logger.debug(event.toString());

        // check first, if the tabs are created
        if (getCalendarAkademikDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabCalakademikDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getCalendarAkademikDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabCalakademikDetail, null));
        }

        // remember the current object
        doStoreInitValues();

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        final Mcalakademik anCalakademik = getCalendarAkademikService().getNewCalakademik();

        // set the beans in the related databinded controllers
        getCalendarAkademikDetailCtrl().setCalakademik(anCalakademik);
        getCalendarAkademikDetailCtrl().setSelectedCalakademik(anCalakademik);

        // Refresh the binding mechanism
        getCalendarAkademikDetailCtrl().setSelectedCalakademik(getSelectedCalakademik());

        if (getCalendarAkademikDetailCtrl().getBinder() != null) {
            getCalendarAkademikDetailCtrl().getBinder().loadAll();
        }

        // set editable Mode
        getCalendarAkademikDetailCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlCalakademik.setInitNew();

        tabCalakademikDetail.setSelected(true);
        // set focus
        getCalendarAkademikDetailCtrl().txtb_filNr.focus();

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++++++ Helpers ++++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    /**
     * Resizes the container from the selected Tab.
     *
     * @param event
     */
    private void doResizeSelectedTab(Event event) {
        // logger.debug(event.toString());

        if (tabbox_CalakademikMain.getSelectedTab() == tabCalakademikDetail) {
            getCalendarAkademikDetailCtrl().doFitSize(event);
        } else if (tabbox_CalakademikMain.getSelectedTab() == tabCalakademikList) {
            // resize and fill Listbox new
            getCalendarAkademikListCtrl().doFillListbox();
        }
    }

    /**
     * Opens the help screen for the current module.
     *
     * @param event
     * @throws InterruptedException
     */
    private void doHelp(Event event) throws InterruptedException {

        ZksampleMessageUtils.doShowNotImplementedMessage();

        // we stop the propagation of the event, because zk will call ALL events
        // with the same name in the namespace and 'btnHelp' is a standard
        // button in this application and can often appears.
        // Events.getRealOrigin((ForwardEvent) event).stopPropagation();
        event.stopPropagation();
    }

    /**
     * Saves the selected object's current properties. We can get them back if a
     * modification is canceled.
     *
     * @see
     */
    public void doStoreInitValues() {

        if (getSelectedCalakademik() != null) {

            try {
                setOriginalCalakademik((Mcalakademik) ZksampleBeanUtils.cloneBean(getSelectedCalakademik()));
            } catch (final IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (final InstantiationException e) {
                throw new RuntimeException(e);
            } catch (final InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (final NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Reset the selected object to its origin property values.
     *
     * @see
     */
    public void doResetToInitValues() {

        if (getOriginalCalakademik() != null) {

            try {
                setSelectedCalakademik((Mcalakademik) ZksampleBeanUtils.cloneBean(getOriginalCalakademik()));
                // TODO Bug in DataBinder??
                windowCalakademikMain.invalidate();

            } catch (final IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (final InstantiationException e) {
                throw new RuntimeException(e);
            } catch (final InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (final NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * User rights check. <br>
     * Only components are set visible=true if the logged-in <br>
     * user have the right for it. <br>
     * <p/>
     * The rights are get from the spring framework users grantedAuthority(). A
     * right is only a string. <br>
     */
    // TODO move it to zul
    private void doCheckRights() {

        final UserWorkspace workspace = getUserWorkspace();

        // window_CalakademikList.setVisible(workspace.isAllowed("window_CalakademikList"));
        button_CalakademikList_PrintList.setVisible(workspace.isAllowed("button_CalakademikList_PrintList"));
        button_CalakademikList_SearchNo.setVisible(workspace.isAllowed("button_CalakademikList_SearchNo"));
        button_CalakademikList_SearchName.setVisible(workspace.isAllowed("button_CalakademikList_SearchName"));


        btnHelp.setVisible(workspace.isAllowed("button_CalakademikMain_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_CalakademikMain_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_CalakademikMain_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_CalakademikMain_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_CalakademikMain_btnSave"));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public void setOriginalCalakademik(Mcalakademik originalCalakademik) {
        this.originalCalakademik = originalCalakademik;
    }

    public Mcalakademik getOriginalCalakademik() {
        return this.originalCalakademik;
    }

    public void setSelectedCalakademik(Mcalakademik selectedCalakademik) {
        this.selectedCalakademik = selectedCalakademik;
    }

    public Mcalakademik getSelectedCalakademik() {
        return this.selectedCalakademik;
    }

    public void setCalakademiks(BindingListModelList calakademiks) {
        this.calakademiks = calakademiks;
    }

    public BindingListModelList getCalakademiks() {
        return this.calakademiks;
    }

    public void setCalendarAkademikService(CalendarAkademikService calakademikService) {
        this.calakademikService = calakademikService;
    }

    public CalendarAkademikService getCalendarAkademikService() {
        return this.calakademikService;
    }

    public void setCalendarAkademikListCtrl(CalendarAkademikListCtrl calakademikListCtrl) {
        this.calakademikListCtrl = calakademikListCtrl;
    }

    public CalendarAkademikListCtrl getCalendarAkademikListCtrl() {
        return this.calakademikListCtrl;
    }

    public void setCalendarAkademikDetailCtrl(CalendarAkademikDetailCtrl calakademikDetailCtrl) {
        this.calakademikDetailCtrl = calakademikDetailCtrl;
    }

    public CalendarAkademikDetailCtrl getCalendarAkademikDetailCtrl() {
        return this.calakademikDetailCtrl;
    }

}
