package id.ac.idu.webui.administrasi.feedback;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.MfeedbackService;
import id.ac.idu.backend.model.Mfeedback;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.webui.administrasi.report.MfeedbackSimpleDJReport;
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
 * Main controller for the mfeedback main module.<br>
 * <br>
 * zul-file: /WEB-INF/pages/mfeedback/mfeedbackMain.zul.<br>
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
 * - MfeedbackListCtrl = Mfeedback List / Filialen Liste<br>
 * - MfeedbackDetailCtrl = Mfeedback Details / Filiale-Details<br>
 */
public class MfeedbackMainCtrl extends GFCBaseCtrl implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(MfeedbackMainCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowMfeedbackMain; // autowired

    // Tabs
    protected Tabbox tabbox_MfeedbackMain; // autowired
    protected Tab tabMfeedbackList; // autowired
    protected Tab tabMfeedbackDetail; // autowired
    protected Tabpanel tabPanelMfeedbackList; // autowired
    protected Tabpanel tabPanelMfeedbackDetail; // autowired

    // filter components
    protected Checkbox checkbox_MfeedbackList_ShowAll; // autowired
    protected Textbox txtb_Mfeedback_No; // aurowired
    protected Button button_MfeedbackList_SearchNo; // aurowired
    protected Textbox txtb_Mfeedback_Name; // aurowired
    protected Button button_MfeedbackList_SearchName; // aurowired
    protected Textbox txtb_Mfeedback_City; // aurowired
    protected Button button_MfeedbackList_SearchCity; // aurowired

    // checkRights
    protected Button button_MfeedbackList_PrintList;

    // Button controller for the CRUD buttons
    private final String btnCtroller_ClassPrefix = "button_MfeedbackMain_";
    private ButtonStatusCtrl btnCtrlMfeedback;
    protected Button btnNew; // autowired
    protected Button btnEdit; // autowired
    protected Button btnDelete; // autowired
    protected Button btnSave; // autowired
    protected Button btnCancel; // autowired

    protected Button btnHelp;

    // Tab-Controllers for getting the binders
    private MfeedbackListCtrl mfeedbackListCtrl;
    private MfeedbackDetailCtrl mfeedbackDetailCtrl;

    // Databinding
    private Mfeedback selectedMfeedback;
    private BindingListModelList mfeedbacks;

    // ServiceDAOs / Domain Classes
    private MfeedbackService mfeedbackService;

    // always a copy from the bean before modifying. Used for reseting
    private Mfeedback originalMfeedback;

    /**
     * default constructor.<br>
     */
    public MfeedbackMainCtrl() {
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
    public void onCreate$windowMfeedbackMain(Event event) throws Exception {
        windowMfeedbackMain.setContentStyle("padding:0px;");

        // create the Button Controller. Disable not used buttons during working
        btnCtrlMfeedback = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

        //doCheckRights();

        /**
         * Initiate the first loading by selecting the customerList tab and
         * create the components from the zul-file.
         */
        tabMfeedbackList.setSelected(true);

        if (tabPanelMfeedbackList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelMfeedbackList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/feedback/mfeedbackList.zul");
        }

        // init the buttons for editMode
        btnCtrlMfeedback.setInitEdit();
    }

    /**
     * When the tab 'tabMfeedbackList' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabMfeedbackList(Event event) throws IOException {
        logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelMfeedbackList.getFirstChild() != null) {
            tabMfeedbackList.setSelected(true);

            return;
        }

        if (tabPanelMfeedbackList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelMfeedbackList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/feedback/mfeedbackList.zul");
        }

    }

    /**
     * When the tab 'tabPanelMfeedbackDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabMfeedbackDetail(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelMfeedbackDetail.getFirstChild() != null) {
            tabMfeedbackDetail.setSelected(true);

            // refresh the Binding mechanism
            getMfeedbackDetailCtrl().setMfeedback(getSelectedMfeedback());
            getMfeedbackDetailCtrl().getBinder().loadAll();

            //refresh combo
            getMfeedbackDetailCtrl().doRenderCombo();
            return;
        }

        if (tabPanelMfeedbackDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelMfeedbackDetail, this, "ModuleMainController", "/WEB-INF/pages/administrasi/feedback/mfeedbackDetail.zul");
        }
    }

    /**
     * when the "print mfeedbacks list" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_MfeedbackList_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        new MfeedbackSimpleDJReport(win);
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_MfeedbackList_ShowAll(Event event) {
        // logger.debug(event.toString());

        // empty the text search boxes
        txtb_Mfeedback_No.setValue(""); // clear
        txtb_Mfeedback_Name.setValue(""); // clear
        txtb_Mfeedback_City.setValue(""); // clear

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Mfeedback> soMfeedback = new HibernateSearchObject<Mfeedback>(Mfeedback.class, getMfeedbackListCtrl().getCountRows());
        soMfeedback.addSort("filName1", false);

        // Change the BindingListModel.
        if (getMfeedbackListCtrl().getBinder() != null) {
            getMfeedbackListCtrl().getPagedBindingListWrapper().setSearchObject(soMfeedback);

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_MfeedbackMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabMfeedbackList)) {
                tabMfeedbackList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }

    }

    /**
     * Filter the mfeedback list with 'like mfeedback number'. <br>
     */
    public void onClick$button_MfeedbackList_SearchNo(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Mfeedback_No.getValue().isEmpty()) {
            checkbox_MfeedbackList_ShowAll.setChecked(false); // unCheck
            txtb_Mfeedback_Name.setValue(""); // clear
            txtb_Mfeedback_City.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mfeedback> soMfeedback = new HibernateSearchObject<Mfeedback>(Mfeedback.class, getMfeedbackListCtrl().getCountRows());
            soMfeedback.addFilter(new Filter("filNr", "%" + txtb_Mfeedback_No.getValue() + "%", Filter.OP_ILIKE));
            soMfeedback.addSort("filNr", false);

            // Change the BindingListModel.
            if (getMfeedbackListCtrl().getBinder() != null) {
                getMfeedbackListCtrl().getPagedBindingListWrapper().setSearchObject(soMfeedback);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_MfeedbackMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabMfeedbackList)) {
                    tabMfeedbackList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the mfeedback list with 'like mfeedback name'. <br>
     */
    public void onClick$button_MfeedbackList_SearchName(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Mfeedback_Name.getValue().isEmpty()) {
            checkbox_MfeedbackList_ShowAll.setChecked(false); // unCheck
            txtb_Mfeedback_City.setValue(""); // clear
            txtb_Mfeedback_No.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mfeedback> soMfeedback = new HibernateSearchObject<Mfeedback>(Mfeedback.class, getMfeedbackListCtrl().getCountRows());
            soMfeedback.addFilter(new Filter("filName1", "%" + txtb_Mfeedback_Name.getValue() + "%", Filter.OP_ILIKE));
            soMfeedback.addSort("filName1", false);

            // Change the BindingListModel.
            if (getMfeedbackListCtrl().getBinder() != null) {
                getMfeedbackListCtrl().getPagedBindingListWrapper().setSearchObject(soMfeedback);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_MfeedbackMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabMfeedbackList)) {
                    tabMfeedbackList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the mfeedback list with 'like mfeedback city'. <br>
     */
    public void onClick$button_MfeedbackList_SearchCity(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Mfeedback_City.getValue().isEmpty()) {
            checkbox_MfeedbackList_ShowAll.setChecked(false); // unCheck
            txtb_Mfeedback_Name.setValue(""); // clear
            txtb_Mfeedback_No.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mfeedback> soMfeedback = new HibernateSearchObject<Mfeedback>(Mfeedback.class, getMfeedbackListCtrl().getCountRows());
            soMfeedback.addFilter(new Filter("filOrt", "%" + txtb_Mfeedback_City.getValue() + "%", Filter.OP_ILIKE));
            soMfeedback.addSort("filOrt", false);

            // Change the BindingListModel.
            if (getMfeedbackListCtrl().getBinder() != null) {
                getMfeedbackListCtrl().getPagedBindingListWrapper().setSearchObject(soMfeedback);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_MfeedbackMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabMfeedbackList)) {
                    tabMfeedbackList.setSelected(true);
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
        if (getMfeedbackDetailCtrl().getBinder() != null) {

            // refresh all dataBinder related controllers/components
            getMfeedbackDetailCtrl().getBinder().loadAll();

            // set editable Mode
            getMfeedbackDetailCtrl().doReadOnlyMode(true);

            btnCtrlMfeedback.setInitEdit();
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
        Tab currentTab = tabbox_MfeedbackMain.getSelectedTab();

        // check first, if the tabs are created, if not than create it
        if (getMfeedbackDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabMfeedbackDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getMfeedbackDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabMfeedbackDetail, null));
        }

        // check if the tab is one of the Detail tabs. If so do not change the
        // selection of it
        if (!currentTab.equals(tabMfeedbackDetail)) {
            tabMfeedbackDetail.setSelected(true);
        } else {
            currentTab.setSelected(true);
        }

        getMfeedbackDetailCtrl().getBinder().loadAll();

        // remember the old vars
        doStoreInitValues();

        btnCtrlMfeedback.setBtnStatus_Edit();

        getMfeedbackDetailCtrl().doReadOnlyMode(false);
        // set focus
        getMfeedbackDetailCtrl().txtb_jenis.focus();
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
        if (getMfeedbackDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabMfeedbackDetail, null));
        }

        // check first, if the tabs are created
        if (getMfeedbackDetailCtrl().getBinder() == null) {
            return;
        }

        final Mfeedback anMfeedback = getSelectedMfeedback();
        if (anMfeedback != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anMfeedback.getCpertanyaan();
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
                     * Do not allow to modify the demo mfeedbacks
                     */
                    if (getMfeedbackDetailCtrl().getMfeedback().getId() <= 2) {
                        try {
                            ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
                            return;
                        } catch (final InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        try {
                            getMfeedbackService().delete(anMfeedback);
                        } catch (DataAccessException e) {
                            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                        }
                    }
                }

            }

            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrlMfeedback.setInitEdit();

        setSelectedMfeedback(null);
        // refresh the list
        getMfeedbackListCtrl().doFillListbox();

        // refresh all dataBinder related controllers
        getMfeedbackDetailCtrl().getBinder().loadAll();
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

//        Character jenis = getMfeedbackDetailCtrl().list_jenis.getSelectedItem().getValue().toString().charAt(0);
//        String textJenis = getMfeedbackDetailCtrl().txtb_jenis.getValue();

//        if (jenis!=null) {
           // getMfeedbackDetailCtrl().getMfeedback().setCkdfeedback(jenis);

//        }
        getMfeedbackDetailCtrl().getBinder().saveAll();

        try {

            /**
             * Do not allow to modify the demo mfeedbacks
             */
//			if (getMfeedbackDetailCtrl().getMfeedback().getId() <= 2) {
//				ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
//
//				doResetToInitValues();
//				getMfeedbackDetailCtrl().getBinder().loadAll();
//				return;
//			}

            // save it to database
            getMfeedbackService().saveOrUpdate(getMfeedbackDetailCtrl().getMfeedback());
            // if saving is successfully than actualize the beans as
            // origins.
            doStoreInitValues();
            // refresh the list
            getMfeedbackListCtrl().doFillListbox();
            // later refresh StatusBar
            Events.postEvent("onSelect", getMfeedbackListCtrl().getListBoxMfeedback(), getSelectedMfeedback());

            // show the objects data in the statusBar
            String str = getSelectedMfeedback().getCpertanyaan();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetToInitValues();

            return;

        } finally {
            btnCtrlMfeedback.setInitEdit();
            getMfeedbackDetailCtrl().doReadOnlyMode(true);
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
        if (getMfeedbackDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabMfeedbackDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getMfeedbackDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabMfeedbackDetail, null));
        }

        // remember the current object
        doStoreInitValues();

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        final Mfeedback anMfeedback = getMfeedbackService().getNewMfeedback();

        // set the beans in the related databinded controllers
        getMfeedbackDetailCtrl().setMfeedback(anMfeedback);
        getMfeedbackDetailCtrl().setSelectedMfeedback(anMfeedback);

        // Refresh the binding mechanism
        getMfeedbackDetailCtrl().setSelectedMfeedback(getSelectedMfeedback());
        if (getMfeedbackDetailCtrl().getBinder() != null) {
            getMfeedbackDetailCtrl().getBinder().loadAll();
        }

        // set editable Mode
        getMfeedbackDetailCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlMfeedback.setInitNew();

        tabMfeedbackDetail.setSelected(true);
        // set focus
        getMfeedbackDetailCtrl().txtb_jenis.focus();

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

        if (tabbox_MfeedbackMain.getSelectedTab() == tabMfeedbackDetail) {
            getMfeedbackDetailCtrl().doFitSize(event);
        } else if (tabbox_MfeedbackMain.getSelectedTab() == tabMfeedbackList) {
            // resize and fill Listbox new
            getMfeedbackListCtrl().doFillListbox();
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

        if (getSelectedMfeedback() != null) {

            try {
                setOriginalMfeedback((Mfeedback) ZksampleBeanUtils.cloneBean(getSelectedMfeedback()));
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

        if (getOriginalMfeedback() != null) {

            try {
                setSelectedMfeedback((Mfeedback) ZksampleBeanUtils.cloneBean(getOriginalMfeedback()));
                // TODO Bug in DataBinder??
                windowMfeedbackMain.invalidate();

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

        // window_MfeedbackList.setVisible(workspace.isAllowed("window_MfeedbackList"));
        button_MfeedbackList_PrintList.setVisible(workspace.isAllowed("button_MfeedbackList_PrintList"));
        button_MfeedbackList_SearchNo.setVisible(workspace.isAllowed("button_MfeedbackList_SearchNo"));
        button_MfeedbackList_SearchName.setVisible(workspace.isAllowed("button_MfeedbackList_SearchName"));
        button_MfeedbackList_SearchCity.setVisible(workspace.isAllowed("button_MfeedbackList_SearchCity"));

        btnHelp.setVisible(workspace.isAllowed("button_MfeedbackMain_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_MfeedbackMain_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_MfeedbackMain_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_MfeedbackMain_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_MfeedbackMain_btnSave"));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public void setOriginalMfeedback(Mfeedback originalMfeedback) {
        this.originalMfeedback = originalMfeedback;
    }

    public Mfeedback getOriginalMfeedback() {
        return this.originalMfeedback;
    }

    public void setSelectedMfeedback(Mfeedback selectedMfeedback) {
        this.selectedMfeedback = selectedMfeedback;
    }

    public Mfeedback getSelectedMfeedback() {
        return this.selectedMfeedback;
    }

    public void setMfeedbacks(BindingListModelList mfeedbacks) {
        this.mfeedbacks = mfeedbacks;
    }

    public BindingListModelList getMfeedbacks() {
        return this.mfeedbacks;
    }

    public void setMfeedbackService(MfeedbackService mfeedbackService) {
        this.mfeedbackService = mfeedbackService;
    }

    public MfeedbackService getMfeedbackService() {
        return this.mfeedbackService;
    }

    public void setMfeedbackListCtrl(MfeedbackListCtrl mfeedbackListCtrl) {
        this.mfeedbackListCtrl = mfeedbackListCtrl;
    }

    public MfeedbackListCtrl getMfeedbackListCtrl() {
        return this.mfeedbackListCtrl;
    }

    public void setMfeedbackDetailCtrl(MfeedbackDetailCtrl mfeedbackDetailCtrl) {
        this.mfeedbackDetailCtrl = mfeedbackDetailCtrl;
    }

    public MfeedbackDetailCtrl getMfeedbackDetailCtrl() {
        return this.mfeedbackDetailCtrl;
    }

}
