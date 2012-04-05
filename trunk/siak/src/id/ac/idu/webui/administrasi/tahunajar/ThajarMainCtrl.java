package id.ac.idu.webui.administrasi.tahunajar;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.ThajarService;
import id.ac.idu.backend.model.Mthajar;
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
 * Date: 05/04/12
 * Time: 14:50
 * To change this template use File | Settings | File Templates.
 */
public class ThajarMainCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ThajarMainCtrl.class);

    /*
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    * All the components that are defined here and have a corresponding
    * component with the same 'id' in the zul-file are getting autowired by our
    * 'extends GFCBaseCtrl' GenericForwardComposer.
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    */
    protected Window windowThajarMain; // autowired

    // Tabs
    protected Tabbox tabbox_ThajarMain; // autowired
    protected Tab tabThajarList; // autowired
    protected Tab tabThajarDetail; // autowired
    protected Tabpanel tabPanelThajarList; // autowired
    protected Tabpanel tabPanelThajarDetail; // autowired

    // filter components
    protected Checkbox checkbox_ThajarList_ShowAll; // autowired
    protected Textbox txtb_Thajar_No; // aurowired
    protected Button button_ThajarList_SearchNo; // aurowired
    protected Textbox txtb_Thajar_Name; // aurowired
    protected Button button_ThajarList_SearchName; // aurowired

    // checkRights
    protected Button button_ThajarList_PrintList;

    // Button controller for the CRUD buttons
    private final String btnCtroller_ClassPrefix = "button_ThajarMain_";
    private ButtonStatusCtrl btnCtrlThajar;
    protected Button btnNew; // autowired
    protected Button btnEdit; // autowired
    protected Button btnDelete; // autowired
    protected Button btnSave; // autowired
    protected Button btnCancel; // autowired

    protected Button btnHelp;

    // Tab-Controllers for getting the binders
    private ThajarListCtrl thajarListCtrl;
    private ThajarDetailCtrl thajarDetailCtrl;

    // Databinding
    private Mthajar selectedThajar;
    private BindingListModelList thajars;

    // ServiceDAOs / Domain Classes
    private ThajarService thajarService;

    // always a copy from the bean before modifying. Used for reseting
    private Mthajar originalThajar;

    /**
     * default constructor.<br>
     */
    public ThajarMainCtrl() {
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
    public void onCreate$windowThajarMain(Event event) throws Exception {
        windowThajarMain.setContentStyle("padding:0px;");

        // create the Button Controller. Disable not used buttons during working
        btnCtrlThajar = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

//            doCheckRights();

        /**
         * Initiate the first loading by selecting the customerList tab and
         * create the components from the zul-file.
         */
        tabThajarList.setSelected(true);

        if (tabPanelThajarList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelThajarList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/tahunajar/thajarList.zul");
        }

        // init the buttons for editMode
        btnCtrlThajar.setInitEdit();
    }

    /**
     * When the tab 'tabThajarList' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabThajarList(Event event) throws IOException {
        logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelThajarList.getFirstChild() != null) {
            tabThajarList.setSelected(true);

            return;
        }

        if (tabPanelThajarList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelThajarList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/tahunajar/thajarList.zul");
        }

    }

    /**
     * When the tab 'tabPanelThajarDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws IOException
     */
    public void onSelect$tabThajarDetail(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelThajarDetail.getFirstChild() != null) {
            tabThajarDetail.setSelected(true);

            // refresh the Binding mechanism
            getThajarDetailCtrl().setThajar(getSelectedThajar());
            getThajarDetailCtrl().getBinder().loadAll();
            return;
        }

        if (tabPanelThajarDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelThajarDetail, this, "ModuleMainController", "/WEB-INF/pages/administrasi/tahunajar/thajarDetail.zul");
        }
    }

    /**
     * when the "print thajars list" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_ThajarList_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        //new ThajarSimpleDJReport(win); // TODO
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_ThajarList_ShowAll(Event event) {
        // logger.debug(event.toString());

        // empty the text search boxes
        txtb_Thajar_No.setValue(""); // clear
        txtb_Thajar_Name.setValue(""); // clear

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Mthajar> soThajar = new HibernateSearchObject<Mthajar>(Mthajar.class, getThajarListCtrl().getCountRows());
        soThajar.addSort("cthajar", false);

        // Change the BindingListModel.
        if (getThajarListCtrl().getBinder() != null) {
            getThajarListCtrl().getPagedBindingListWrapper().setSearchObject(soThajar);

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_ThajarMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabThajarList)) {
                tabThajarList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }

    }

    /**
     * Filter the thajar list with 'like thajar number'. <br>
     */
    public void onClick$button_ThajarList_SearchNo(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Thajar_No.getValue().isEmpty()) {
            checkbox_ThajarList_ShowAll.setChecked(false); // unCheck
            txtb_Thajar_Name.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mthajar> soThajar = new HibernateSearchObject<Mthajar>(Mthajar.class, getThajarListCtrl().getCountRows());
            soThajar.addFilter(new Filter("cthajar", "%" + txtb_Thajar_No.getValue() + "%", Filter.OP_ILIKE));
            soThajar.addSort("cthajar", false);

            // Change the BindingListModel.
            if (getThajarListCtrl().getBinder() != null) {
                getThajarListCtrl().getPagedBindingListWrapper().setSearchObject(soThajar);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_ThajarMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabThajarList)) {
                    tabThajarList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the thajar list with 'like thajar name'. <br>
     */
    public void onClick$button_ThajarList_SearchName(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Thajar_Name.getValue().isEmpty()) {
            checkbox_ThajarList_ShowAll.setChecked(false); // unCheck
            txtb_Thajar_No.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mthajar> soThajar = new HibernateSearchObject<Mthajar>(Mthajar.class, getThajarListCtrl().getCountRows());
            soThajar.addFilter(new Filter("csmt", "%" + txtb_Thajar_Name.getValue() + "%", Filter.OP_ILIKE));
            soThajar.addSort("csmt", false);

            // Change the BindingListModel.
            if (getThajarListCtrl().getBinder() != null) {
                getThajarListCtrl().getPagedBindingListWrapper().setSearchObject(soThajar);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_ThajarMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabThajarList)) {
                    tabThajarList.setSelected(true);
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
        if (getThajarDetailCtrl().getBinder() != null) {

            // refresh all dataBinder related controllers/components
            getThajarDetailCtrl().getBinder().loadAll();

            // set editable Mode
            getThajarDetailCtrl().doReadOnlyMode(true);

            btnCtrlThajar.setInitEdit();
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
        Tab currentTab = tabbox_ThajarMain.getSelectedTab();

        // check first, if the tabs are created, if not than create it
        if (getThajarDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabThajarDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getThajarDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabThajarDetail, null));
        }

        // check if the tab is one of the Detail tabs. If so do not change the
        // selection of it
        if (!currentTab.equals(tabThajarDetail)) {
            tabThajarDetail.setSelected(true);
        } else {
            currentTab.setSelected(true);
        }

        getThajarDetailCtrl().getBinder().loadAll();

        // remember the old vars
        doStoreInitValues();

        btnCtrlThajar.setBtnStatus_Edit();

        getThajarDetailCtrl().doReadOnlyMode(false);
        // set focus
        getThajarDetailCtrl().txtb_filNr.focus();
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
        if (getThajarDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabThajarDetail, null));
        }

        // check first, if the tabs are created
        if (getThajarDetailCtrl().getBinder() == null) {
            return;
        }

        final Mthajar anThajar = getSelectedThajar();
        if (anThajar != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anThajar.toString();
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
                     * Do not allow to modify the demo thajars
                     */
                    if (getThajarDetailCtrl().getThajar().getId() <= 2) {
                        try {
                            ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
                            return;
                        } catch (final InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        try {
                            getThajarService().delete(anThajar);
                        } catch (DataAccessException e) {
                            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                        }
                    }
                }

            }

            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrlThajar.setInitEdit();

        setSelectedThajar(null);
        // refresh the list
        getThajarListCtrl().doFillListbox();

        // refresh all dataBinder related controllers
        getThajarDetailCtrl().getBinder().loadAll();
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
        getThajarDetailCtrl().getBinder().saveAll();

        try {

            /**
             * Do not allow to modify the demo thajars
             */
//                if (getThajarDetailCtrl().getThajar().getId() <= 2) {
//                    ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
//
//                    doResetToInitValues();
//                    getThajarDetailCtrl().getBinder().loadAll();
//                    return;
//                }

            // save it to database
            getThajarService().saveOrUpdate(getThajarDetailCtrl().getThajar());
            // if saving is successfully than actualize the beans as
            // origins.
            doStoreInitValues();
            // refresh the list
            getThajarListCtrl().doFillListbox();
            // later refresh StatusBar
            Events.postEvent("onSelect", getThajarListCtrl().getListBoxThajar(), getSelectedThajar());

            // show the objects data in the statusBar
            String str = getSelectedThajar().toString();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetToInitValues();

            return;

        } finally {
            btnCtrlThajar.setInitEdit();
            getThajarDetailCtrl().doReadOnlyMode(true);
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
        if (getThajarDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabThajarDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getThajarDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabThajarDetail, null));
        }

        // remember the current object
        doStoreInitValues();

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        final Mthajar anThajar = getThajarService().getNewThajar();

        // set the beans in the related databinded controllers
        getThajarDetailCtrl().setThajar(anThajar);
        getThajarDetailCtrl().setSelectedThajar(anThajar);

        // Refresh the binding mechanism
        getThajarDetailCtrl().setSelectedThajar(getSelectedThajar());
        if (getThajarDetailCtrl().getBinder() != null) {
            getThajarDetailCtrl().getBinder().loadAll();
        }

        // set editable Mode
        getThajarDetailCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlThajar.setInitNew();

        tabThajarDetail.setSelected(true);
        // set focus
        getThajarDetailCtrl().txtb_filNr.focus();

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

        if (tabbox_ThajarMain.getSelectedTab() == tabThajarDetail) {
            getThajarDetailCtrl().doFitSize(event);
        } else if (tabbox_ThajarMain.getSelectedTab() == tabThajarList) {
            // resize and fill Listbox new
            getThajarListCtrl().doFillListbox();
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

        if (getSelectedThajar() != null) {

            try {
                setOriginalThajar((Mthajar) ZksampleBeanUtils.cloneBean(getSelectedThajar()));
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

        if (getOriginalThajar() != null) {

            try {
                setSelectedThajar((Mthajar) ZksampleBeanUtils.cloneBean(getOriginalThajar()));
                // TODO Bug in DataBinder??
                windowThajarMain.invalidate();

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

        // window_ThajarList.setVisible(workspace.isAllowed("window_ThajarList"));
        button_ThajarList_PrintList.setVisible(workspace.isAllowed("button_ThajarList_PrintList"));
        button_ThajarList_SearchNo.setVisible(workspace.isAllowed("button_ThajarList_SearchNo"));
        button_ThajarList_SearchName.setVisible(workspace.isAllowed("button_ThajarList_SearchName"));


        btnHelp.setVisible(workspace.isAllowed("button_ThajarMain_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_ThajarMain_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_ThajarMain_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_ThajarMain_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_ThajarMain_btnSave"));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public void setOriginalThajar(Mthajar originalThajar) {
        this.originalThajar = originalThajar;
    }

    public Mthajar getOriginalThajar() {
        return this.originalThajar;
    }

    public void setSelectedThajar(Mthajar selectedThajar) {
        this.selectedThajar = selectedThajar;
    }

    public Mthajar getSelectedThajar() {
        return this.selectedThajar;
    }

    public void setThajars(BindingListModelList thajars) {
        this.thajars = thajars;
    }

    public BindingListModelList getThajars() {
        return this.thajars;
    }

    public void setThajarService(ThajarService thajarService) {
        this.thajarService = thajarService;
    }

    public ThajarService getThajarService() {
        return this.thajarService;
    }

    public void setThajarListCtrl(ThajarListCtrl thajarListCtrl) {
        this.thajarListCtrl = thajarListCtrl;
    }

    public ThajarListCtrl getThajarListCtrl() {
        return this.thajarListCtrl;
    }

    public void setThajarDetailCtrl(ThajarDetailCtrl thajarDetailCtrl) {
        this.thajarDetailCtrl = thajarDetailCtrl;
    }

    public ThajarDetailCtrl getThajarDetailCtrl() {
        return this.thajarDetailCtrl;
    }

}
