package id.ac.idu.webui.administrasi.peminatan;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.MpeminatanService;
import id.ac.idu.backend.model.Mpeminatan;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.webui.administrasi.report.MpeminatanSimpleDJReport;
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
 * Main controller for the mpeminatan main module.<br>
 * <br>
 * zul-file: /WEB-INF/pages/mpeminatan/mpeminatanMain.zul.<br>
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
 * - MpeminatanListCtrl = Mpeminatan List / Filialen Liste<br>
 * - MpeminatanDetailCtrl = Mpeminatan Details / Filiale-Details<br>
 */
public class MpeminatanMainCtrl extends GFCBaseCtrl implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(MpeminatanMainCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowMpeminatanMain; // autowired

    // Tabs
    protected Tabbox tabbox_MpeminatanMain; // autowired
    protected Tab tabMpeminatanList; // autowired
    protected Tab tabMpeminatanDetail; // autowired
    protected Tabpanel tabPanelMpeminatanList; // autowired
    protected Tabpanel tabPanelMpeminatanDetail; // autowired

    // filter components
    protected Checkbox checkbox_MpeminatanList_ShowAll; // autowired
    protected Textbox txtb_Mpeminatan_No; // aurowired
    protected Button button_MpeminatanList_SearchNo; // aurowired
    protected Textbox txtb_Mpeminatan_Name; // aurowired
    protected Button button_MpeminatanList_SearchName; // aurowired
    protected Textbox txtb_Mpeminatan_City; // aurowired
    protected Button button_MpeminatanList_SearchCity; // aurowired

    // checkRights
    protected Button button_MpeminatanList_PrintList;

    // Button controller for the CRUD buttons
    private final String btnCtroller_ClassPrefix = "button_MpeminatanMain_";
    private ButtonStatusCtrl btnCtrlMpeminatan;
    protected Button btnNew; // autowired
    protected Button btnEdit; // autowired
    protected Button btnDelete; // autowired
    protected Button btnSave; // autowired
    protected Button btnCancel; // autowired

    protected Button btnHelp;

    // Tab-Controllers for getting the binders
    private MpeminatanListCtrl mpeminatanListCtrl;
    private MpeminatanDetailCtrl mpeminatanDetailCtrl;

    // Databinding
    private Mpeminatan selectedMpeminatan;
    private BindingListModelList mpeminatans;

    // ServiceDAOs / Domain Classes
    private MpeminatanService mpeminatanService;

    // always a copy from the bean before modifying. Used for reseting
    private Mpeminatan originalMpeminatan;

    /**
     * default constructor.<br>
     */
    public MpeminatanMainCtrl() {
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
    public void onCreate$windowMpeminatanMain(Event event) throws Exception {
        windowMpeminatanMain.setContentStyle("padding:0px;");

        // create the Button Controller. Disable not used buttons during working
        btnCtrlMpeminatan = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

        //doCheckRights();

        /**
         * Initiate the first loading by selecting the customerList tab and
         * create the components from the zul-file.
         */
        tabMpeminatanList.setSelected(true);

        if (tabPanelMpeminatanList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelMpeminatanList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/peminatan/mpeminatanList.zul");
        }

        // init the buttons for editMode
        btnCtrlMpeminatan.setInitEdit();
    }

    /**
     * When the tab 'tabMpeminatanList' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabMpeminatanList(Event event) throws IOException {
        logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelMpeminatanList.getFirstChild() != null) {
            tabMpeminatanList.setSelected(true);

            return;
        }

        if (tabPanelMpeminatanList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelMpeminatanList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/peminatan/mpeminatanList.zul");
        }

    }

    /**
     * When the tab 'tabPanelMpeminatanDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabMpeminatanDetail(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelMpeminatanDetail.getFirstChild() != null) {
            tabMpeminatanDetail.setSelected(true);

            // refresh the Binding mechanism
            getMpeminatanDetailCtrl().setMpeminatan(getSelectedMpeminatan());
            getMpeminatanDetailCtrl().getBinder().loadAll();
            return;
        }

        if (tabPanelMpeminatanDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelMpeminatanDetail, this, "ModuleMainController", "/WEB-INF/pages/administrasi/peminatan/mpeminatanDetail.zul");
        }
    }

    /**
     * when the "print mpeminatans list" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_MpeminatanList_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        new MpeminatanSimpleDJReport(win);
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_MpeminatanList_ShowAll(Event event) {
        // logger.debug(event.toString());

        // empty the text search boxes
        txtb_Mpeminatan_No.setValue(""); // clear
        txtb_Mpeminatan_Name.setValue(""); // clear
        txtb_Mpeminatan_City.setValue(""); // clear

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Mpeminatan> soMpeminatan = new HibernateSearchObject<Mpeminatan>(Mpeminatan.class, getMpeminatanListCtrl().getCountRows());
        soMpeminatan.addSort("filName1", false);

        // Change the BindingListModel.
        if (getMpeminatanListCtrl().getBinder() != null) {
            getMpeminatanListCtrl().getPagedBindingListWrapper().setSearchObject(soMpeminatan);

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_MpeminatanMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabMpeminatanList)) {
                tabMpeminatanList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }

    }

    /**
     * Filter the mpeminatan list with 'like mpeminatan number'. <br>
     */
    public void onClick$button_MpeminatanList_SearchNo(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Mpeminatan_No.getValue().isEmpty()) {
            checkbox_MpeminatanList_ShowAll.setChecked(false); // unCheck
            txtb_Mpeminatan_Name.setValue(""); // clear
            txtb_Mpeminatan_City.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mpeminatan> soMpeminatan = new HibernateSearchObject<Mpeminatan>(Mpeminatan.class, getMpeminatanListCtrl().getCountRows());
            soMpeminatan.addFilter(new Filter("filNr", "%" + txtb_Mpeminatan_No.getValue() + "%", Filter.OP_ILIKE));
            soMpeminatan.addSort("filNr", false);

            // Change the BindingListModel.
            if (getMpeminatanListCtrl().getBinder() != null) {
                getMpeminatanListCtrl().getPagedBindingListWrapper().setSearchObject(soMpeminatan);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_MpeminatanMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabMpeminatanList)) {
                    tabMpeminatanList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the mpeminatan list with 'like mpeminatan name'. <br>
     */
    public void onClick$button_MpeminatanList_SearchName(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Mpeminatan_Name.getValue().isEmpty()) {
            checkbox_MpeminatanList_ShowAll.setChecked(false); // unCheck
            txtb_Mpeminatan_City.setValue(""); // clear
            txtb_Mpeminatan_No.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mpeminatan> soMpeminatan = new HibernateSearchObject<Mpeminatan>(Mpeminatan.class, getMpeminatanListCtrl().getCountRows());
            soMpeminatan.addFilter(new Filter("filName1", "%" + txtb_Mpeminatan_Name.getValue() + "%", Filter.OP_ILIKE));
            soMpeminatan.addSort("filName1", false);

            // Change the BindingListModel.
            if (getMpeminatanListCtrl().getBinder() != null) {
                getMpeminatanListCtrl().getPagedBindingListWrapper().setSearchObject(soMpeminatan);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_MpeminatanMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabMpeminatanList)) {
                    tabMpeminatanList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the mpeminatan list with 'like mpeminatan city'. <br>
     */
    public void onClick$button_MpeminatanList_SearchCity(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Mpeminatan_City.getValue().isEmpty()) {
            checkbox_MpeminatanList_ShowAll.setChecked(false); // unCheck
            txtb_Mpeminatan_Name.setValue(""); // clear
            txtb_Mpeminatan_No.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mpeminatan> soMpeminatan = new HibernateSearchObject<Mpeminatan>(Mpeminatan.class, getMpeminatanListCtrl().getCountRows());
            soMpeminatan.addFilter(new Filter("filOrt", "%" + txtb_Mpeminatan_City.getValue() + "%", Filter.OP_ILIKE));
            soMpeminatan.addSort("filOrt", false);

            // Change the BindingListModel.
            if (getMpeminatanListCtrl().getBinder() != null) {
                getMpeminatanListCtrl().getPagedBindingListWrapper().setSearchObject(soMpeminatan);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_MpeminatanMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabMpeminatanList)) {
                    tabMpeminatanList.setSelected(true);
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
        if (getMpeminatanDetailCtrl().getBinder() != null) {

            // refresh all dataBinder related controllers/components
            getMpeminatanDetailCtrl().getBinder().loadAll();

            // set editable Mode
            getMpeminatanDetailCtrl().doReadOnlyMode(true);

            btnCtrlMpeminatan.setInitEdit();
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
        Tab currentTab = tabbox_MpeminatanMain.getSelectedTab();

        // check first, if the tabs are created, if not than create it
        if (getMpeminatanDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabMpeminatanDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getMpeminatanDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabMpeminatanDetail, null));
        }

        // check if the tab is one of the Detail tabs. If so do not change the
        // selection of it
        if (!currentTab.equals(tabMpeminatanDetail)) {
            tabMpeminatanDetail.setSelected(true);
        } else {
            currentTab.setSelected(true);
        }

        getMpeminatanDetailCtrl().getBinder().loadAll();

        // remember the old vars
        doStoreInitValues();

        btnCtrlMpeminatan.setBtnStatus_Edit();

        getMpeminatanDetailCtrl().doReadOnlyMode(false);
        // set focus
        getMpeminatanDetailCtrl().txtb_jenis.focus();
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
        if (getMpeminatanDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabMpeminatanDetail, null));
        }

        // check first, if the tabs are created
        if (getMpeminatanDetailCtrl().getBinder() == null) {
            return;
        }

        final Mpeminatan anMpeminatan = getSelectedMpeminatan();
        if (anMpeminatan != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anMpeminatan.getCnmminat();
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
                     * Do not allow to modify the demo mpeminatans
                     */
                    if (getMpeminatanDetailCtrl().getMpeminatan().getId() <= 2) {
                        try {
                            ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
                            return;
                        } catch (final InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        try {
                            getMpeminatanService().delete(anMpeminatan);
                        } catch (DataAccessException e) {
                            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                        }
                    }
                }

            }

            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrlMpeminatan.setInitEdit();

        setSelectedMpeminatan(null);
        // refresh the list
        getMpeminatanListCtrl().doFillListbox();

        // refresh all dataBinder related controllers
        getMpeminatanDetailCtrl().getBinder().loadAll();
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
        getMpeminatanDetailCtrl().getBinder().saveAll();

        try {

            /**
             * Do not allow to modify the demo mpeminatans
             */
//			if (getMpeminatanDetailCtrl().getMpeminatan().getId() <= 2) {
//				ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
//
//				doResetToInitValues();
//				getMpeminatanDetailCtrl().getBinder().loadAll();
//				return;
//			}

            // save it to database
            getMpeminatanService().saveOrUpdate(getMpeminatanDetailCtrl().getMpeminatan());
            // if saving is successfully than actualize the beans as
            // origins.
            doStoreInitValues();
            // refresh the list
            getMpeminatanListCtrl().doFillListbox();
            // later refresh StatusBar
            Events.postEvent("onSelect", getMpeminatanListCtrl().getListBoxMpeminatan(), getSelectedMpeminatan());

            // show the objects data in the statusBar
            String str = getSelectedMpeminatan().getCnmminat();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetToInitValues();

            return;

        } finally {
            btnCtrlMpeminatan.setInitEdit();
            getMpeminatanDetailCtrl().doReadOnlyMode(true);
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
        if (getMpeminatanDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabMpeminatanDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getMpeminatanDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabMpeminatanDetail, null));
        }

        // remember the current object
        doStoreInitValues();

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        final Mpeminatan anMpeminatan = getMpeminatanService().getNewMpeminatan();

        // set the beans in the related databinded controllers
        getMpeminatanDetailCtrl().setMpeminatan(anMpeminatan);
        getMpeminatanDetailCtrl().setSelectedMpeminatan(anMpeminatan);

        // Refresh the binding mechanism
        getMpeminatanDetailCtrl().setSelectedMpeminatan(getSelectedMpeminatan());
        if (getMpeminatanDetailCtrl().getBinder() != null) {
            getMpeminatanDetailCtrl().getBinder().loadAll();
        }

        // set editable Mode
        getMpeminatanDetailCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlMpeminatan.setInitNew();

        tabMpeminatanDetail.setSelected(true);
        // set focus
        getMpeminatanDetailCtrl().txtb_jenis.focus();

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

        if (tabbox_MpeminatanMain.getSelectedTab() == tabMpeminatanDetail) {
            getMpeminatanDetailCtrl().doFitSize(event);
        } else if (tabbox_MpeminatanMain.getSelectedTab() == tabMpeminatanList) {
            // resize and fill Listbox new
            getMpeminatanListCtrl().doFillListbox();
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

        if (getSelectedMpeminatan() != null) {

            try {
                setOriginalMpeminatan((Mpeminatan) ZksampleBeanUtils.cloneBean(getSelectedMpeminatan()));
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

        if (getOriginalMpeminatan() != null) {

            try {
                setSelectedMpeminatan((Mpeminatan) ZksampleBeanUtils.cloneBean(getOriginalMpeminatan()));
                // TODO Bug in DataBinder??
                windowMpeminatanMain.invalidate();

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

        // window_MpeminatanList.setVisible(workspace.isAllowed("window_MpeminatanList"));
        button_MpeminatanList_PrintList.setVisible(workspace.isAllowed("button_MpeminatanList_PrintList"));
        button_MpeminatanList_SearchNo.setVisible(workspace.isAllowed("button_MpeminatanList_SearchNo"));
        button_MpeminatanList_SearchName.setVisible(workspace.isAllowed("button_MpeminatanList_SearchName"));
        button_MpeminatanList_SearchCity.setVisible(workspace.isAllowed("button_MpeminatanList_SearchCity"));

        btnHelp.setVisible(workspace.isAllowed("button_MpeminatanMain_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_MpeminatanMain_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_MpeminatanMain_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_MpeminatanMain_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_MpeminatanMain_btnSave"));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public void setOriginalMpeminatan(Mpeminatan originalMpeminatan) {
        this.originalMpeminatan = originalMpeminatan;
    }

    public Mpeminatan getOriginalMpeminatan() {
        return this.originalMpeminatan;
    }

    public void setSelectedMpeminatan(Mpeminatan selectedMpeminatan) {
        this.selectedMpeminatan = selectedMpeminatan;
    }

    public Mpeminatan getSelectedMpeminatan() {
        return this.selectedMpeminatan;
    }

    public void setMpeminatans(BindingListModelList mpeminatans) {
        this.mpeminatans = mpeminatans;
    }

    public BindingListModelList getMpeminatans() {
        return this.mpeminatans;
    }

    public void setMpeminatanService(MpeminatanService mpeminatanService) {
        this.mpeminatanService = mpeminatanService;
    }

    public MpeminatanService getMpeminatanService() {
        return this.mpeminatanService;
    }

    public void setMpeminatanListCtrl(MpeminatanListCtrl mpeminatanListCtrl) {
        this.mpeminatanListCtrl = mpeminatanListCtrl;
    }

    public MpeminatanListCtrl getMpeminatanListCtrl() {
        return this.mpeminatanListCtrl;
    }

    public void setMpeminatanDetailCtrl(MpeminatanDetailCtrl mpeminatanDetailCtrl) {
        this.mpeminatanDetailCtrl = mpeminatanDetailCtrl;
    }

    public MpeminatanDetailCtrl getMpeminatanDetailCtrl() {
        return this.mpeminatanDetailCtrl;
    }

}
