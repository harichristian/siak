package id.ac.idu.webui.administrasi.fasilitasruang;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.MfasilitasruangService;
import id.ac.idu.backend.model.Mfasilitasruang;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.webui.administrasi.report.MfasilitasruangSimpleDJReport;
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
 * Main controller for the mfasilitasruang main module.<br>
 * <br>
 * zul-file: /WEB-INF/pages/mfasilitasruang/mfasilitasruangMain.zul.<br>
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
 * - MfasilitasruangListCtrl = Mfasilitasruang List / Filialen Liste<br>
 * - MfasilitasruangDetailCtrl = Mfasilitasruang Details / Filiale-Details<br>
 */
public class MfasilitasruangMainCtrl extends GFCBaseCtrl implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(MfasilitasruangMainCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowMfasilitasruangMain; // autowired

    // Tabs
    protected Tabbox tabbox_MfasilitasruangMain; // autowired
    protected Tab tabMfasilitasruangList; // autowired
    protected Tab tabMfasilitasruangDetail; // autowired
    protected Tabpanel tabPanelMfasilitasruangList; // autowired
    protected Tabpanel tabPanelMfasilitasruangDetail; // autowired

    // filter components
    protected Checkbox checkbox_MfasilitasruangList_ShowAll; // autowired
    protected Textbox txtb_Mfasilitasruang_No; // aurowired
    protected Button button_MfasilitasruangList_SearchNo; // aurowired
    protected Textbox txtb_Mfasilitasruang_Name; // aurowired
    protected Button button_MfasilitasruangList_SearchName; // aurowired
    protected Textbox txtb_Mfasilitasruang_City; // aurowired
    protected Button button_MfasilitasruangList_SearchCity; // aurowired

    // checkRights
    protected Button button_MfasilitasruangList_PrintList;

    // Button controller for the CRUD buttons
    private final String btnCtroller_ClassPrefix = "button_MfasilitasruangMain_";
    private ButtonStatusCtrl btnCtrlMfasilitasruang;
    protected Button btnNew; // autowired
    protected Button btnEdit; // autowired
    protected Button btnDelete; // autowired
    protected Button btnSave; // autowired
    protected Button btnCancel; // autowired

    protected Button btnHelp;

    // Tab-Controllers for getting the binders
    private MfasilitasruangListCtrl mfasilitasruangListCtrl;
    private MfasilitasruangDetailCtrl mfasilitasruangDetailCtrl;

    // Databinding
    private Mfasilitasruang selectedMfasilitasruang;
    private BindingListModelList mfasilitasruangs;

    // ServiceDAOs / Domain Classes
    private MfasilitasruangService mfasilitasruangService;

    // always a copy from the bean before modifying. Used for reseting
    private Mfasilitasruang originalMfasilitasruang;

    /**
     * default constructor.<br>
     */
    public MfasilitasruangMainCtrl() {
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
    public void onCreate$windowMfasilitasruangMain(Event event) throws Exception {
        windowMfasilitasruangMain.setContentStyle("padding:0px;");

        // create the Button Controller. Disable not used buttons during working
        btnCtrlMfasilitasruang = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

        //doCheckRights();

        /**
         * Initiate the first loading by selecting the customerList tab and
         * create the components from the zul-file.
         */
        tabMfasilitasruangList.setSelected(true);

        if (tabPanelMfasilitasruangList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelMfasilitasruangList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/fasilitasruang/mfasilitasruangList.zul");
        }

        // init the buttons for editMode
        btnCtrlMfasilitasruang.setInitEdit();
    }

    /**
     * When the tab 'tabMfasilitasruangList' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabMfasilitasruangList(Event event) throws IOException {
        logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelMfasilitasruangList.getFirstChild() != null) {
            tabMfasilitasruangList.setSelected(true);

            return;
        }

        if (tabPanelMfasilitasruangList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelMfasilitasruangList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/fasilitasruang/mfasilitasruangList.zul");
        }

    }

    /**
     * When the tab 'tabPanelMfasilitasruangDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabMfasilitasruangDetail(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelMfasilitasruangDetail.getFirstChild() != null) {
            tabMfasilitasruangDetail.setSelected(true);

            // refresh the Binding mechanism
            getMfasilitasruangDetailCtrl().setMfasilitasruang(getSelectedMfasilitasruang());
            getMfasilitasruangDetailCtrl().getBinder().loadAll();
            return;
        }

        if (tabPanelMfasilitasruangDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelMfasilitasruangDetail, this, "ModuleMainController", "/WEB-INF/pages/administrasi/fasilitasruang/mfasilitasruangDetail.zul");
        }
    }

    /**
     * when the "print mfasilitasruangs list" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_MfasilitasruangList_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        new MfasilitasruangSimpleDJReport(win);
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_MfasilitasruangList_ShowAll(Event event) {
        // logger.debug(event.toString());

        // empty the text search boxes
        txtb_Mfasilitasruang_No.setValue(""); // clear
        txtb_Mfasilitasruang_Name.setValue(""); // clear
        txtb_Mfasilitasruang_City.setValue(""); // clear

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Mfasilitasruang> soMfasilitasruang = new HibernateSearchObject<Mfasilitasruang>(Mfasilitasruang.class, getMfasilitasruangListCtrl().getCountRows());
        soMfasilitasruang.addSort("mruang.cnmRuang", false);

        // Change the BindingListModel.
        if (getMfasilitasruangListCtrl().getBinder() != null) {
            getMfasilitasruangListCtrl().getPagedBindingListWrapper().setSearchObject(soMfasilitasruang);

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_MfasilitasruangMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabMfasilitasruangList)) {
                tabMfasilitasruangList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }

    }

    /**
     * Filter the mfasilitasruang list with 'like mfasilitasruang number'. <br>
     */
    public void onClick$button_MfasilitasruangList_SearchNo(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Mfasilitasruang_No.getValue().isEmpty()) {
            checkbox_MfasilitasruangList_ShowAll.setChecked(false); // unCheck
            txtb_Mfasilitasruang_Name.setValue(""); // clear
            txtb_Mfasilitasruang_City.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mfasilitasruang> soMfasilitasruang = new HibernateSearchObject<Mfasilitasruang>(Mfasilitasruang.class, getMfasilitasruangListCtrl().getCountRows());
            soMfasilitasruang.addFilter(new Filter("mruang.ckdruang", "%" + txtb_Mfasilitasruang_No.getValue() + "%", Filter.OP_ILIKE));
            soMfasilitasruang.addSort("mruang.cnmRuang", false);

            // Change the BindingListModel.
            if (getMfasilitasruangListCtrl().getBinder() != null) {
                getMfasilitasruangListCtrl().getPagedBindingListWrapper().setSearchObject(soMfasilitasruang);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_MfasilitasruangMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabMfasilitasruangList)) {
                    tabMfasilitasruangList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the mfasilitasruang list with 'like mfasilitasruang name'. <br>
     */
    public void onClick$button_MfasilitasruangList_SearchName(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Mfasilitasruang_Name.getValue().isEmpty()) {
            checkbox_MfasilitasruangList_ShowAll.setChecked(false); // unCheck
            txtb_Mfasilitasruang_City.setValue(""); // clear
            txtb_Mfasilitasruang_No.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mfasilitasruang> soMfasilitasruang = new HibernateSearchObject<Mfasilitasruang>(Mfasilitasruang.class, getMfasilitasruangListCtrl().getCountRows());
            soMfasilitasruang.addFilter(new Filter("mruang.cnmRuang", "%" + txtb_Mfasilitasruang_Name.getValue() + "%", Filter.OP_ILIKE));
            soMfasilitasruang.addSort("mruang.cnmRuang", false);

            // Change the BindingListModel.
            if (getMfasilitasruangListCtrl().getBinder() != null) {
                getMfasilitasruangListCtrl().getPagedBindingListWrapper().setSearchObject(soMfasilitasruang);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_MfasilitasruangMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabMfasilitasruangList)) {
                    tabMfasilitasruangList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the mfasilitasruang list with 'like mfasilitasruang city'. <br>
     */
    public void onClick$button_MfasilitasruangList_SearchCity(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Mfasilitasruang_City.getValue().isEmpty()) {
            checkbox_MfasilitasruangList_ShowAll.setChecked(false); // unCheck
            txtb_Mfasilitasruang_Name.setValue(""); // clear
            txtb_Mfasilitasruang_No.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mfasilitasruang> soMfasilitasruang = new HibernateSearchObject<Mfasilitasruang>(Mfasilitasruang.class, getMfasilitasruangListCtrl().getCountRows());
            soMfasilitasruang.addFilter(new Filter("mfasilitas.cnamaFasilitas", "%" + txtb_Mfasilitasruang_City.getValue() + "%", Filter.OP_ILIKE));
            soMfasilitasruang.addSort("mruang.cnmRuang", false);

            // Change the BindingListModel.
            if (getMfasilitasruangListCtrl().getBinder() != null) {
                getMfasilitasruangListCtrl().getPagedBindingListWrapper().setSearchObject(soMfasilitasruang);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_MfasilitasruangMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabMfasilitasruangList)) {
                    tabMfasilitasruangList.setSelected(true);
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
        if (getMfasilitasruangDetailCtrl().getBinder() != null) {

            // refresh all dataBinder related controllers/components
            getMfasilitasruangDetailCtrl().getBinder().loadAll();

            // set editable Mode
            getMfasilitasruangDetailCtrl().doReadOnlyMode(true);

            btnCtrlMfasilitasruang.setInitEdit();
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
        Tab currentTab = tabbox_MfasilitasruangMain.getSelectedTab();

        // check first, if the tabs are created, if not than create it
        if (getMfasilitasruangDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabMfasilitasruangDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getMfasilitasruangDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabMfasilitasruangDetail, null));
        }

        // check if the tab is one of the Detail tabs. If so do not change the
        // selection of it
        if (!currentTab.equals(tabMfasilitasruangDetail)) {
            tabMfasilitasruangDetail.setSelected(true);
        } else {
            currentTab.setSelected(true);
        }

        getMfasilitasruangDetailCtrl().getBinder().loadAll();

        // remember the old vars
        doStoreInitValues();

        btnCtrlMfasilitasruang.setBtnStatus_Edit();

        getMfasilitasruangDetailCtrl().doReadOnlyMode(false);
        // set focus
        getMfasilitasruangDetailCtrl().txtb_kdruang.focus();
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
        if (getMfasilitasruangDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabMfasilitasruangDetail, null));
        }

        // check first, if the tabs are created
        if (getMfasilitasruangDetailCtrl().getBinder() == null) {
            return;
        }

        final Mfasilitasruang anMfasilitasruang = getSelectedMfasilitasruang();
        if (anMfasilitasruang != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anMfasilitasruang.getMfasilitas().getCnamaFasilitas();
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
                     * Do not allow to modify the demo mfasilitasruangs
                     */
                    if (getMfasilitasruangDetailCtrl().getMfasilitasruang().getId() <= 2) {
                        try {
                            ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
                            return;
                        } catch (final InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        try {
                            getMfasilitasruangService().delete(anMfasilitasruang);
                        } catch (DataAccessException e) {
                            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                        }
                    }
                }

            }

            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrlMfasilitasruang.setInitEdit();

        setSelectedMfasilitasruang(null);
        // refresh the list
        getMfasilitasruangListCtrl().doFillListbox();

        // refresh all dataBinder related controllers
        getMfasilitasruangDetailCtrl().getBinder().loadAll();
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
        getMfasilitasruangDetailCtrl().getBinder().saveAll();

        try {

            /**
             * Do not allow to modify the demo mfasilitasruangs
             */
//			if (getMfasilitasruangDetailCtrl().getMfasilitasruang().getId() <= 2) {
//				ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
//
//				doResetToInitValues();
//				getMfasilitasruangDetailCtrl().getBinder().loadAll();
//				return;
//			}

            // save it to database
            getMfasilitasruangService().saveOrUpdate(getMfasilitasruangDetailCtrl().getMfasilitasruang());
            // if saving is successfully than actualize the beans as
            // origins.
            doStoreInitValues();
            // refresh the list
            getMfasilitasruangListCtrl().doFillListbox();
            // later refresh StatusBar
            Events.postEvent("onSelect", getMfasilitasruangListCtrl().getListBoxMfasilitasruang(), getSelectedMfasilitasruang());

            // show the objects data in the statusBar
            String str = getSelectedMfasilitasruang().getMfasilitas().getCnamaFasilitas();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetToInitValues();

            return;

        } finally {
            btnCtrlMfasilitasruang.setInitEdit();
            getMfasilitasruangDetailCtrl().doReadOnlyMode(true);
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
        if (getMfasilitasruangDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabMfasilitasruangDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getMfasilitasruangDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabMfasilitasruangDetail, null));
        }

        // remember the current object
        doStoreInitValues();

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        final Mfasilitasruang anMfasilitasruang = getMfasilitasruangService().getNewMfasilitasruang();

        // set the beans in the related databinded controllers
        getMfasilitasruangDetailCtrl().setMfasilitasruang(anMfasilitasruang);
        getMfasilitasruangDetailCtrl().setSelectedMfasilitasruang(anMfasilitasruang);

        // Refresh the binding mechanism
        getMfasilitasruangDetailCtrl().setSelectedMfasilitasruang(getSelectedMfasilitasruang());
        if (getMfasilitasruangDetailCtrl().getBinder() != null) {
            getMfasilitasruangDetailCtrl().getBinder().loadAll();
        }

        // set editable Mode
        getMfasilitasruangDetailCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlMfasilitasruang.setInitNew();

        tabMfasilitasruangDetail.setSelected(true);
        // set focus
        getMfasilitasruangDetailCtrl().txtb_kdruang.focus();

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

        if (tabbox_MfasilitasruangMain.getSelectedTab() == tabMfasilitasruangDetail) {
            getMfasilitasruangDetailCtrl().doFitSize(event);
        } else if (tabbox_MfasilitasruangMain.getSelectedTab() == tabMfasilitasruangList) {
            // resize and fill Listbox new
            getMfasilitasruangListCtrl().doFillListbox();
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

        if (getSelectedMfasilitasruang() != null) {

            try {
                setOriginalMfasilitasruang((Mfasilitasruang) ZksampleBeanUtils.cloneBean(getSelectedMfasilitasruang()));
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

        if (getOriginalMfasilitasruang() != null) {

            try {
                setSelectedMfasilitasruang((Mfasilitasruang) ZksampleBeanUtils.cloneBean(getOriginalMfasilitasruang()));
                // TODO Bug in DataBinder??
                windowMfasilitasruangMain.invalidate();

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

        // window_MfasilitasruangList.setVisible(workspace.isAllowed("window_MfasilitasruangList"));
        button_MfasilitasruangList_PrintList.setVisible(workspace.isAllowed("button_MfasilitasruangList_PrintList"));
        button_MfasilitasruangList_SearchNo.setVisible(workspace.isAllowed("button_MfasilitasruangList_SearchNo"));
        button_MfasilitasruangList_SearchName.setVisible(workspace.isAllowed("button_MfasilitasruangList_SearchName"));
        button_MfasilitasruangList_SearchCity.setVisible(workspace.isAllowed("button_MfasilitasruangList_SearchCity"));

        btnHelp.setVisible(workspace.isAllowed("button_MfasilitasruangMain_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_MfasilitasruangMain_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_MfasilitasruangMain_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_MfasilitasruangMain_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_MfasilitasruangMain_btnSave"));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public void setOriginalMfasilitasruang(Mfasilitasruang originalMfasilitasruang) {
        this.originalMfasilitasruang = originalMfasilitasruang;
    }

    public Mfasilitasruang getOriginalMfasilitasruang() {
        return this.originalMfasilitasruang;
    }

    public void setSelectedMfasilitasruang(Mfasilitasruang selectedMfasilitasruang) {
        this.selectedMfasilitasruang = selectedMfasilitasruang;
    }

    public Mfasilitasruang getSelectedMfasilitasruang() {
        return this.selectedMfasilitasruang;
    }

    public void setMfasilitasruangs(BindingListModelList mfasilitasruangs) {
        this.mfasilitasruangs = mfasilitasruangs;
    }

    public BindingListModelList getMfasilitasruangs() {
        return this.mfasilitasruangs;
    }

    public void setMfasilitasruangService(MfasilitasruangService mfasilitasruangService) {
        this.mfasilitasruangService = mfasilitasruangService;
    }

    public MfasilitasruangService getMfasilitasruangService() {
        return this.mfasilitasruangService;
    }

    public void setMfasilitasruangListCtrl(MfasilitasruangListCtrl mfasilitasruangListCtrl) {
        this.mfasilitasruangListCtrl = mfasilitasruangListCtrl;
    }

    public MfasilitasruangListCtrl getMfasilitasruangListCtrl() {
        return this.mfasilitasruangListCtrl;
    }

    public void setMfasilitasruangDetailCtrl(MfasilitasruangDetailCtrl mfasilitasruangDetailCtrl) {
        this.mfasilitasruangDetailCtrl = mfasilitasruangDetailCtrl;
    }

    public MfasilitasruangDetailCtrl getMfasilitasruangDetailCtrl() {
        return this.mfasilitasruangDetailCtrl;
    }

}
