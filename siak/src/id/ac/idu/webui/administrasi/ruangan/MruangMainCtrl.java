package id.ac.idu.webui.administrasi.ruangan;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.MruangService;
import id.ac.idu.backend.model.Mruang;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.webui.administrasi.report.MruangSimpleDJReport;
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
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * Main controller for the mruang main module.<br>
 * <br>
 * zul-file: /WEB-INF/pages/mruang/mruangMain.zul.<br>
 * This class creates the Tabs + TabPanels. The components/data to all tabs are
 * created on demand on first time selecting the tab.<br>
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * <br>
 *
 * @author bbruhns
 * @author sgerth
 * @changes 07/04/2010: sge modified for zk5.x with complete Annotated
 * Databinding.<br>
 * <p/>
 * Managed tabs:<br>
 * - MruangListCtrl = Mruang List / Filialen Liste<br>
 * - MruangDetailCtrl = Mruang Details / Filiale-Details<br>
 */
public class MruangMainCtrl extends GFCBaseCtrl implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(MruangMainCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowMruangMain; // autowired

    // Tabs
    protected Tabbox tabbox_MruangMain; // autowired
    protected Tab tabMruangList; // autowired
    protected Tab tabMruangDetail; // autowired
    protected Tabpanel tabPanelMruangList; // autowired
    protected Tabpanel tabPanelMruangDetail; // autowired

    // filter components
    protected Checkbox checkbox_MruangList_ShowAll; // autowired
    protected Textbox txtb_Mruang_No; // aurowired
    protected Button button_MruangList_SearchNo; // aurowired
    protected Textbox txtb_Mruang_Name; // aurowired
    protected Button button_MruangList_SearchName; // aurowired
    protected Textbox txtb_Mruang_City; // aurowired
    protected Button button_MruangList_SearchCity; // aurowired

    // checkRights
    protected Button button_MruangList_PrintList;

    // Button controller for the CRUD buttons
    private final String btnCtroller_ClassPrefix = "button_MruangMain_";
    private ButtonStatusCtrl btnCtrlMruang;
    protected Button btnNew; // autowired
    protected Button btnEdit; // autowired
    protected Button btnDelete; // autowired
    protected Button btnSave; // autowired
    protected Button btnCancel; // autowired

    protected Button btnHelp;

    // Tab-Controllers for getting the binders
    private MruangListCtrl mruangListCtrl;
    private MruangDetailCtrl mruangDetailCtrl;

    // Databinding
    private Mruang selectedMruang;
    private BindingListModelList mruangs;

    // ServiceDAOs / Domain Classes
    private MruangService mruangService;

    // always a copy from the bean before modifying. Used for reseting
    private Mruang originalMruang;

    /**
     * default constructor.<br>
     */
    public MruangMainCtrl() {
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
    public void onCreate$windowMruangMain(Event event) throws Exception {
        windowMruangMain.setContentStyle("padding:0px;");

        // create the Button Controller. Disable not used buttons during working
        btnCtrlMruang = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

        //doCheckRights();

        /**
         * Initiate the first loading by selecting the customerList tab and
         * create the components from the zul-file.
         */
        tabMruangList.setSelected(true);

        if (tabPanelMruangList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelMruangList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/ruangan/ruangList.zul");
        }

        // init the buttons for editMode
        btnCtrlMruang.setInitEdit();
    }

    /**
     * When the tab 'tabMruangList' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabMruangList(Event event) throws IOException {
        logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelMruangList.getFirstChild() != null) {
            tabMruangList.setSelected(true);

            return;
        }

        if (tabPanelMruangList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelMruangList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/ruangan/ruangList.zul");
        }

    }

    /**
     * When the tab 'tabPanelMruangDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabMruangDetail(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelMruangDetail.getFirstChild() != null) {
            tabMruangDetail.setSelected(true);

            // refresh the Binding mechanism
            getMruangDetailCtrl().setMruang(getSelectedMruang());
            getMruangDetailCtrl().getBinder().loadAll();
            return;
        }

        if (tabPanelMruangDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelMruangDetail, this, "ModuleMainController", "/WEB-INF/pages/administrasi/ruangan/ruangDetail.zul");
        }
    }

    /**
     * when the "print mruangs list" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_MruangList_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        new MruangSimpleDJReport(win);
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_MruangList_ShowAll(Event event) {
        // logger.debug(event.toString());

        // empty the text search boxes
        txtb_Mruang_No.setValue(""); // clear
        txtb_Mruang_Name.setValue(""); // clear
        txtb_Mruang_City.setValue(""); // clear

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Mruang> soMruang = new HibernateSearchObject<Mruang>(Mruang.class, getMruangListCtrl().getCountRows());
        soMruang.addSort("filName1", false);

        // Change the BindingListModel.
        if (getMruangListCtrl().getBinder() != null) {
            getMruangListCtrl().getPagedBindingListWrapper().setSearchObject(soMruang);

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_MruangMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabMruangList)) {
                tabMruangList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }

    }

    /**
     * Filter the mruang list with 'like mruang number'. <br>
     */
    public void onClick$button_MruangList_SearchNo(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Mruang_No.getValue().isEmpty()) {
            checkbox_MruangList_ShowAll.setChecked(false); // unCheck
            txtb_Mruang_Name.setValue(""); // clear
            txtb_Mruang_City.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mruang> soMruang = new HibernateSearchObject<Mruang>(Mruang.class, getMruangListCtrl().getCountRows());
            soMruang.addFilter(new Filter("filNr", "%" + txtb_Mruang_No.getValue() + "%", Filter.OP_ILIKE));
            soMruang.addSort("filNr", false);

            // Change the BindingListModel.
            if (getMruangListCtrl().getBinder() != null) {
                getMruangListCtrl().getPagedBindingListWrapper().setSearchObject(soMruang);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_MruangMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabMruangList)) {
                    tabMruangList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the mruang list with 'like mruang name'. <br>
     */
    public void onClick$button_MruangList_SearchName(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Mruang_Name.getValue().isEmpty()) {
            checkbox_MruangList_ShowAll.setChecked(false); // unCheck
            txtb_Mruang_City.setValue(""); // clear
            txtb_Mruang_No.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mruang> soMruang = new HibernateSearchObject<Mruang>(Mruang.class, getMruangListCtrl().getCountRows());
            soMruang.addFilter(new Filter("filName1", "%" + txtb_Mruang_Name.getValue() + "%", Filter.OP_ILIKE));
            soMruang.addSort("filName1", false);

            // Change the BindingListModel.
            if (getMruangListCtrl().getBinder() != null) {
                getMruangListCtrl().getPagedBindingListWrapper().setSearchObject(soMruang);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_MruangMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabMruangList)) {
                    tabMruangList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the mruang list with 'like mruang city'. <br>
     */
    public void onClick$button_MruangList_SearchCity(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Mruang_City.getValue().isEmpty()) {
            checkbox_MruangList_ShowAll.setChecked(false); // unCheck
            txtb_Mruang_Name.setValue(""); // clear
            txtb_Mruang_No.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mruang> soMruang = new HibernateSearchObject<Mruang>(Mruang.class, getMruangListCtrl().getCountRows());
            soMruang.addFilter(new Filter("filOrt", "%" + txtb_Mruang_City.getValue() + "%", Filter.OP_ILIKE));
            soMruang.addSort("filOrt", false);

            // Change the BindingListModel.
            if (getMruangListCtrl().getBinder() != null) {
                getMruangListCtrl().getPagedBindingListWrapper().setSearchObject(soMruang);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_MruangMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabMruangList)) {
                    tabMruangList.setSelected(true);
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
        if (getMruangDetailCtrl().getBinder() != null) {

            // refresh all dataBinder related controllers/components
            getMruangDetailCtrl().getBinder().loadAll();

            // set editable Mode
            getMruangDetailCtrl().doReadOnlyMode(true);

            btnCtrlMruang.setInitEdit();
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
        Tab currentTab = tabbox_MruangMain.getSelectedTab();

        // check first, if the tabs are created, if not than create it
        if (getMruangDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabMruangDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getMruangDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabMruangDetail, null));
        }

        // check if the tab is one of the Detail tabs. If so do not change the
        // selection of it
        if (!currentTab.equals(tabMruangDetail)) {
            tabMruangDetail.setSelected(true);
        } else {
            currentTab.setSelected(true);
        }

        getMruangDetailCtrl().getBinder().loadAll();

        // remember the old vars
        doStoreInitValues();

        btnCtrlMruang.setBtnStatus_Edit();

        getMruangDetailCtrl().doReadOnlyMode(false);
        // set focus
        getMruangDetailCtrl().txtb_kdruangan.focus();
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
        if (getMruangDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabMruangDetail, null));
        }

        // check first, if the tabs are created
        if (getMruangDetailCtrl().getBinder() == null) {
            return;
        }

        final Mruang anMruang = getSelectedMruang();
        if (anMruang != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anMruang.getCnmRuang();
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
                     * Do not allow to modify the demo mruangs
                     */
                    if (getMruangDetailCtrl().getMruang().getId() <= 2) {
                        try {
                            ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
                            return;
                        } catch (final InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        try {
                            getMruangService().delete(anMruang);
                        } catch (DataAccessException e) {
                            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                        }
                    }
                }

            }

            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrlMruang.setInitEdit();

        setSelectedMruang(null);
        // refresh the list
        getMruangListCtrl().doFillListbox();

        // refresh all dataBinder related controllers
        getMruangDetailCtrl().getBinder().loadAll();
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
        getMruangDetailCtrl().getBinder().saveAll();

        try {

            /**
             * Do not allow to modify the demo mruangs
             */
//			if (getMruangDetailCtrl().getMruang().getId() <= 2) {
//				ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
//
//				doResetToInitValues();
//				getMruangDetailCtrl().getBinder().loadAll();
//				return;
//			}

            // save it to database
            getMruangService().saveOrUpdate(getMruangDetailCtrl().getMruang());
            // if saving is successfully than actualize the beans as
            // origins.
            doStoreInitValues();
            // refresh the list
            getMruangListCtrl().doFillListbox();
            // later refresh StatusBar
            Events.postEvent("onSelect", getMruangListCtrl().getListBoxMruang(), getSelectedMruang());

            // show the objects data in the statusBar
            String str = getSelectedMruang().getCnmRuang();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetToInitValues();

            return;

        } finally {
            btnCtrlMruang.setInitEdit();
            getMruangDetailCtrl().doReadOnlyMode(true);
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
        if (getMruangDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabMruangDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getMruangDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabMruangDetail, null));
        }

        // remember the current object
        doStoreInitValues();

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        final Mruang anMruang = getMruangService().getNewRuang();

        // set the beans in the related databinded controllers
        getMruangDetailCtrl().setMruang(anMruang);
        getMruangDetailCtrl().setSelectedMruang(anMruang);

        // Refresh the binding mechanism
        getMruangDetailCtrl().setSelectedMruang(getSelectedMruang());
        if (getMruangDetailCtrl().getBinder() != null) {
            getMruangDetailCtrl().getBinder().loadAll();
        }

        // set editable Mode
        getMruangDetailCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlMruang.setInitNew();

        tabMruangDetail.setSelected(true);
        // set focus
        getMruangDetailCtrl().txtb_kdruangan.focus();

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

        if (tabbox_MruangMain.getSelectedTab() == tabMruangDetail) {
            getMruangDetailCtrl().doFitSize(event);
        } else if (tabbox_MruangMain.getSelectedTab() == tabMruangList) {
            // resize and fill Listbox new
            getMruangListCtrl().doFillListbox();
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

        if (getSelectedMruang() != null) {

            try {
                setOriginalMruang((Mruang) ZksampleBeanUtils.cloneBean(getSelectedMruang()));
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

        if (getOriginalMruang() != null) {

            try {
                setSelectedMruang((Mruang) ZksampleBeanUtils.cloneBean(getOriginalMruang()));
                // TODO Bug in DataBinder??
                windowMruangMain.invalidate();

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

        // window_MruangList.setVisible(workspace.isAllowed("window_MruangList"));
        button_MruangList_PrintList.setVisible(workspace.isAllowed("button_MruangList_PrintList"));
        button_MruangList_SearchNo.setVisible(workspace.isAllowed("button_MruangList_SearchNo"));
        button_MruangList_SearchName.setVisible(workspace.isAllowed("button_MruangList_SearchName"));
        button_MruangList_SearchCity.setVisible(workspace.isAllowed("button_MruangList_SearchCity"));

        btnHelp.setVisible(workspace.isAllowed("button_MruangMain_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_MruangMain_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_MruangMain_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_MruangMain_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_MruangMain_btnSave"));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public void setOriginalMruang(Mruang originalMruang) {
        this.originalMruang = originalMruang;
    }

    public Mruang getOriginalMruang() {
        return this.originalMruang;
    }

    public void setSelectedMruang(Mruang selectedMruang) {
        this.selectedMruang = selectedMruang;
    }

    public Mruang getSelectedMruang() {
        return this.selectedMruang;
    }

    public void setMruangs(BindingListModelList mruangs) {
        this.mruangs = mruangs;
    }

    public BindingListModelList getMruangs() {
        return this.mruangs;
    }

    public void setMruangService(MruangService mruangService) {
        this.mruangService = mruangService;
    }

    public MruangService getMruangService() {
        return this.mruangService;
    }

    public void setMruangListCtrl(MruangListCtrl mruangListCtrl) {
        this.mruangListCtrl = mruangListCtrl;
    }

    public MruangListCtrl getMruangListCtrl() {
        return this.mruangListCtrl;
    }

    public void setMruangDetailCtrl(MruangDetailCtrl mruangDetailCtrl) {
        this.mruangDetailCtrl = mruangDetailCtrl;
    }

    public MruangDetailCtrl getMruangDetailCtrl() {
        return this.mruangDetailCtrl;
    }

}
