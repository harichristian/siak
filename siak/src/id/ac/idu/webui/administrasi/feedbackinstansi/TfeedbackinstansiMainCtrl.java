package id.ac.idu.webui.administrasi.feedbackinstansi;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.TfeedbackinstansiService;
import id.ac.idu.backend.model.Tfeedbackinstansi;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.webui.administrasi.report.TfeedbackinstansiSimpleDJReport;
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
 * Main controller for the tfeedbackinstansi main module.<br>
 * <br>
 * zul-file: /WEB-INF/pages/tfeedbackinstansi/tfeedbackinstansiMain.zul.<br>
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
 * - TfeedbackinstansiListCtrl = Tfeedbackinstansi List / Filialen Liste<br>
 * - TfeedbackinstansiDetailCtrl = Tfeedbackinstansi Details / Filiale-Details<br>
 */
public class TfeedbackinstansiMainCtrl extends GFCBaseCtrl implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(TfeedbackinstansiMainCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowTfeedbackinstansiMain; // autowired

    // Tabs
    protected Tabbox tabbox_TfeedbackinstansiMain; // autowired
    protected Tab tabTfeedbackinstansiList; // autowired
    protected Tab tabTfeedbackinstansiDetail; // autowired
    protected Tabpanel tabPanelTfeedbackinstansiList; // autowired
    protected Tabpanel tabPanelTfeedbackinstansiDetail; // autowired

    // filter components
    protected Checkbox checkbox_TfeedbackinstansiList_ShowAll; // autowired
    protected Textbox txtb_Tfeedbackinstansi_No; // aurowired
    protected Button button_TfeedbackinstansiList_SearchNo; // aurowired
    protected Textbox txtb_Tfeedbackinstansi_Name; // aurowired
    protected Button button_TfeedbackinstansiList_SearchName; // aurowired
    protected Textbox txtb_Tfeedbackinstansi_City; // aurowired
    protected Button button_TfeedbackinstansiList_SearchCity; // aurowired

    // checkRights
    protected Button button_TfeedbackinstansiList_PrintList;

    // Button controller for the CRUD buttons
    private final String btnCtroller_ClassPrefix = "button_TfeedbackinstansiMain_";
    private ButtonStatusCtrl btnCtrlTfeedbackinstansi;
    protected Button btnNew; // autowired
    protected Button btnEdit; // autowired
    protected Button btnDelete; // autowired
    protected Button btnSave; // autowired
    protected Button btnCancel; // autowired

    protected Button btnHelp;

    // Tab-Controllers for getting the binders
    private TfeedbackinstansiListCtrl tfeedbackinstansiListCtrl;
    private TfeedbackinstansiDetailCtrl tfeedbackinstansiDetailCtrl;

    // Databinding
    private Tfeedbackinstansi selectedTfeedbackinstansi;
    private BindingListModelList tfeedbackinstansis;

    // ServiceDAOs / Domain Classes
    private TfeedbackinstansiService tfeedbackinstansiService;

    // always a copy from the bean before modifying. Used for reseting
    private Tfeedbackinstansi originalTfeedbackinstansi;

    /**
     * default constructor.<br>
     */
    public TfeedbackinstansiMainCtrl() {
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
    public void onCreate$windowTfeedbackinstansiMain(Event event) throws Exception {
        windowTfeedbackinstansiMain.setContentStyle("padding:0px;");

        // create the Button Controller. Disable not used buttons during working
        btnCtrlTfeedbackinstansi = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

        //doCheckRights();

        /**
         * Initiate the first loading by selecting the customerList tab and
         * create the components from the zul-file.
         */
        tabTfeedbackinstansiList.setSelected(true);

        if (tabPanelTfeedbackinstansiList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelTfeedbackinstansiList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/feedbackinstansi/tfeedbackinstansiList.zul");
        }

        // init the buttons for editMode
        btnCtrlTfeedbackinstansi.setInitEdit();
    }

    /**
     * When the tab 'tabTfeedbackinstansiList' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabTfeedbackinstansiList(Event event) throws IOException {
        logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelTfeedbackinstansiList.getFirstChild() != null) {
            tabTfeedbackinstansiList.setSelected(true);

            return;
        }

        if (tabPanelTfeedbackinstansiList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelTfeedbackinstansiList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/feedbackinstansi/tfeedbackinstansiList.zul");
        }

    }

    /**
     * When the tab 'tabPanelTfeedbackinstansiDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabTfeedbackinstansiDetail(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelTfeedbackinstansiDetail.getFirstChild() != null) {
            tabTfeedbackinstansiDetail.setSelected(true);

            // refresh the Binding mechanism
            getTfeedbackinstansiDetailCtrl().setTfeedbackinstansi(getSelectedTfeedbackinstansi());
            getTfeedbackinstansiDetailCtrl().getBinder().loadAll();

            //mastiin ke render lagi
            getTfeedbackinstansiDetailCtrl().doRenderCombo();
            return;
        }

        if (tabPanelTfeedbackinstansiDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelTfeedbackinstansiDetail, this, "ModuleMainController", "/WEB-INF/pages/administrasi/feedbackinstansi/tfeedbackinstansiDetail.zul");
        }
    }

    /**
     * when the "print tfeedbackinstansis list" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_TfeedbackinstansiList_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        new TfeedbackinstansiSimpleDJReport(win);
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_TfeedbackinstansiList_ShowAll(Event event) {
        // logger.debug(event.toString());

        // empty the text search boxes
        txtb_Tfeedbackinstansi_No.setValue(""); // clear
        txtb_Tfeedbackinstansi_Name.setValue(""); // clear
        txtb_Tfeedbackinstansi_City.setValue(""); // clear

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Tfeedbackinstansi> soTfeedbackinstansi = new HibernateSearchObject<Tfeedbackinstansi>(Tfeedbackinstansi.class, getTfeedbackinstansiListCtrl().getCountRows());
        soTfeedbackinstansi.addSort("filName1", false);

        // Change the BindingListModel.
        if (getTfeedbackinstansiListCtrl().getBinder() != null) {
            getTfeedbackinstansiListCtrl().getPagedBindingListWrapper().setSearchObject(soTfeedbackinstansi);

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_TfeedbackinstansiMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabTfeedbackinstansiList)) {
                tabTfeedbackinstansiList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }

    }

    /**
     * Filter the tfeedbackinstansi list with 'like tfeedbackinstansi number'. <br>
     */
    public void onClick$button_TfeedbackinstansiList_SearchNo(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Tfeedbackinstansi_No.getValue().isEmpty()) {
            checkbox_TfeedbackinstansiList_ShowAll.setChecked(false); // unCheck
            txtb_Tfeedbackinstansi_Name.setValue(""); // clear
            txtb_Tfeedbackinstansi_City.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Tfeedbackinstansi> soTfeedbackinstansi = new HibernateSearchObject<Tfeedbackinstansi>(Tfeedbackinstansi.class, getTfeedbackinstansiListCtrl().getCountRows());
            soTfeedbackinstansi.addFilter(new Filter("filNr", "%" + txtb_Tfeedbackinstansi_No.getValue() + "%", Filter.OP_ILIKE));
            soTfeedbackinstansi.addSort("filNr", false);

            // Change the BindingListModel.
            if (getTfeedbackinstansiListCtrl().getBinder() != null) {
                getTfeedbackinstansiListCtrl().getPagedBindingListWrapper().setSearchObject(soTfeedbackinstansi);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_TfeedbackinstansiMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabTfeedbackinstansiList)) {
                    tabTfeedbackinstansiList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the tfeedbackinstansi list with 'like tfeedbackinstansi name'. <br>
     */
    public void onClick$button_TfeedbackinstansiList_SearchName(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Tfeedbackinstansi_Name.getValue().isEmpty()) {
            checkbox_TfeedbackinstansiList_ShowAll.setChecked(false); // unCheck
            txtb_Tfeedbackinstansi_City.setValue(""); // clear
            txtb_Tfeedbackinstansi_No.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Tfeedbackinstansi> soTfeedbackinstansi = new HibernateSearchObject<Tfeedbackinstansi>(Tfeedbackinstansi.class, getTfeedbackinstansiListCtrl().getCountRows());
            soTfeedbackinstansi.addFilter(new Filter("filName1", "%" + txtb_Tfeedbackinstansi_Name.getValue() + "%", Filter.OP_ILIKE));
            soTfeedbackinstansi.addSort("filName1", false);

            // Change the BindingListModel.
            if (getTfeedbackinstansiListCtrl().getBinder() != null) {
                getTfeedbackinstansiListCtrl().getPagedBindingListWrapper().setSearchObject(soTfeedbackinstansi);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_TfeedbackinstansiMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabTfeedbackinstansiList)) {
                    tabTfeedbackinstansiList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the tfeedbackinstansi list with 'like tfeedbackinstansi city'. <br>
     */
    public void onClick$button_TfeedbackinstansiList_SearchCity(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Tfeedbackinstansi_City.getValue().isEmpty()) {
            checkbox_TfeedbackinstansiList_ShowAll.setChecked(false); // unCheck
            txtb_Tfeedbackinstansi_Name.setValue(""); // clear
            txtb_Tfeedbackinstansi_No.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Tfeedbackinstansi> soTfeedbackinstansi = new HibernateSearchObject<Tfeedbackinstansi>(Tfeedbackinstansi.class, getTfeedbackinstansiListCtrl().getCountRows());
            soTfeedbackinstansi.addFilter(new Filter("filOrt", "%" + txtb_Tfeedbackinstansi_City.getValue() + "%", Filter.OP_ILIKE));
            soTfeedbackinstansi.addSort("filOrt", false);

            // Change the BindingListModel.
            if (getTfeedbackinstansiListCtrl().getBinder() != null) {
                getTfeedbackinstansiListCtrl().getPagedBindingListWrapper().setSearchObject(soTfeedbackinstansi);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_TfeedbackinstansiMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabTfeedbackinstansiList)) {
                    tabTfeedbackinstansiList.setSelected(true);
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
        if (getTfeedbackinstansiDetailCtrl().getBinder() != null) {

            // refresh all dataBinder related controllers/components
            getTfeedbackinstansiDetailCtrl().getBinder().loadAll();

            // set editable Mode
            getTfeedbackinstansiDetailCtrl().doReadOnlyMode(true);

            btnCtrlTfeedbackinstansi.setInitEdit();
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
        Tab currentTab = tabbox_TfeedbackinstansiMain.getSelectedTab();

        // check first, if the tabs are created, if not than create it
        if (getTfeedbackinstansiDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabTfeedbackinstansiDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getTfeedbackinstansiDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabTfeedbackinstansiDetail, null));
        }

        // check if the tab is one of the Detail tabs. If so do not change the
        // selection of it
        if (!currentTab.equals(tabTfeedbackinstansiDetail)) {
            tabTfeedbackinstansiDetail.setSelected(true);
        } else {
            currentTab.setSelected(true);
        }

        getTfeedbackinstansiDetailCtrl().getBinder().loadAll();

        // remember the old vars
        doStoreInitValues();

        btnCtrlTfeedbackinstansi.setBtnStatus_Edit();

        getTfeedbackinstansiDetailCtrl().doReadOnlyMode(false);
        // set focus
        getTfeedbackinstansiDetailCtrl().txtb_jenis.focus();
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
        if (getTfeedbackinstansiDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabTfeedbackinstansiDetail, null));
        }

        // check first, if the tabs are created
        if (getTfeedbackinstansiDetailCtrl().getBinder() == null) {
            return;
        }

        final Tfeedbackinstansi anTfeedbackinstansi = getSelectedTfeedbackinstansi();
        if (anTfeedbackinstansi != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anTfeedbackinstansi.getCnminstansi();
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
                     * Do not allow to modify the demo tfeedbackinstansis
                     */
                    if (getTfeedbackinstansiDetailCtrl().getTfeedbackinstansi().getId() <= 2) {
                        try {
                            ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
                            return;
                        } catch (final InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        try {
                            getTfeedbackinstansiService().delete(anTfeedbackinstansi);
                        } catch (DataAccessException e) {
                            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                        }
                    }
                }

            }

            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrlTfeedbackinstansi.setInitEdit();

        setSelectedTfeedbackinstansi(null);
        // refresh the list
        getTfeedbackinstansiListCtrl().doFillListbox();

        // refresh all dataBinder related controllers
        getTfeedbackinstansiDetailCtrl().getBinder().loadAll();
    }

    /**
     * Saves all involved Beans to the DB.
     *
     * @param event
     * @throws InterruptedException
     */
    private void doSave(Event event) throws InterruptedException {
        // logger.debug(event.toString());

        if (getTfeedbackinstansiDetailCtrl().jenis_instansi!=null){

        getTfeedbackinstansiDetailCtrl().txtb_jenis.setValue(getTfeedbackinstansiDetailCtrl().jenis_instansi);
        getTfeedbackinstansiDetailCtrl().getTfeedbackinstansi().setCjnsinstansi(getTfeedbackinstansiDetailCtrl().jenis_instansi);
        // save all components data in the several tabs to the bean
        getTfeedbackinstansiDetailCtrl().getBinder().saveAll();

        try {

            /**
             * Do not allow to modify the demo tfeedbackinstansis
             */
//			if (getTfeedbackinstansiDetailCtrl().getTfeedbackinstansi().getId() <= 2) {
//				ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
//
//				doResetToInitValues();
//				getTfeedbackinstansiDetailCtrl().getBinder().loadAll();
//				return;
//			}

            // save it to database
            getTfeedbackinstansiService().saveOrUpdate(getTfeedbackinstansiDetailCtrl().getTfeedbackinstansi());
            // if saving is successfully than actualize the beans as
            // origins.
            doStoreInitValues();
            // refresh the list
            getTfeedbackinstansiListCtrl().doFillListbox();
            // later refresh StatusBar
            Events.postEvent("onSelect", getTfeedbackinstansiListCtrl().getListBoxTfeedbackinstansi(), getSelectedTfeedbackinstansi());

            // show the objects data in the statusBar
            String str = getSelectedTfeedbackinstansi().getCnminstansi();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetToInitValues();

            return;

        } finally {
            btnCtrlTfeedbackinstansi.setInitEdit();
            getTfeedbackinstansiDetailCtrl().doReadOnlyMode(true);
        }

        } else {
             ZksampleMessageUtils.showErrorMessage("Jenis Instansi Mohon Dipilih.");
             return;
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
        if (getTfeedbackinstansiDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabTfeedbackinstansiDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getTfeedbackinstansiDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabTfeedbackinstansiDetail, null));
        }

        // remember the current object
        doStoreInitValues();

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        final Tfeedbackinstansi anTfeedbackinstansi = getTfeedbackinstansiService().getNewTfeedbackinstansi();

        // set the beans in the related databinded controllers
        getTfeedbackinstansiDetailCtrl().setTfeedbackinstansi(anTfeedbackinstansi);
        getTfeedbackinstansiDetailCtrl().setSelectedTfeedbackinstansi(anTfeedbackinstansi);

        // Refresh the binding mechanism
        getTfeedbackinstansiDetailCtrl().setSelectedTfeedbackinstansi(getSelectedTfeedbackinstansi());
        if (getTfeedbackinstansiDetailCtrl().getBinder() != null) {
            getTfeedbackinstansiDetailCtrl().getBinder().loadAll();
        }

        getTfeedbackinstansiDetailCtrl().doRenderCombo();


        // set editable Mode
        getTfeedbackinstansiDetailCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlTfeedbackinstansi.setInitNew();

        tabTfeedbackinstansiDetail.setSelected(true);
        // set focus
        getTfeedbackinstansiDetailCtrl().txtb_jenis.focus();

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

        if (tabbox_TfeedbackinstansiMain.getSelectedTab() == tabTfeedbackinstansiDetail) {
            getTfeedbackinstansiDetailCtrl().doFitSize(event);
        } else if (tabbox_TfeedbackinstansiMain.getSelectedTab() == tabTfeedbackinstansiList) {
            // resize and fill Listbox new
            getTfeedbackinstansiListCtrl().doFillListbox();
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

        if (getSelectedTfeedbackinstansi() != null) {

            try {
                setOriginalTfeedbackinstansi((Tfeedbackinstansi) ZksampleBeanUtils.cloneBean(getSelectedTfeedbackinstansi()));
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

        if (getOriginalTfeedbackinstansi() != null) {

            try {
                setSelectedTfeedbackinstansi((Tfeedbackinstansi) ZksampleBeanUtils.cloneBean(getOriginalTfeedbackinstansi()));
                // TODO Bug in DataBinder??
                windowTfeedbackinstansiMain.invalidate();

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

        // window_TfeedbackinstansiList.setVisible(workspace.isAllowed("window_TfeedbackinstansiList"));
        button_TfeedbackinstansiList_PrintList.setVisible(workspace.isAllowed("button_TfeedbackinstansiList_PrintList"));
        button_TfeedbackinstansiList_SearchNo.setVisible(workspace.isAllowed("button_TfeedbackinstansiList_SearchNo"));
        button_TfeedbackinstansiList_SearchName.setVisible(workspace.isAllowed("button_TfeedbackinstansiList_SearchName"));
        button_TfeedbackinstansiList_SearchCity.setVisible(workspace.isAllowed("button_TfeedbackinstansiList_SearchCity"));

        btnHelp.setVisible(workspace.isAllowed("button_TfeedbackinstansiMain_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_TfeedbackinstansiMain_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_TfeedbackinstansiMain_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_TfeedbackinstansiMain_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_TfeedbackinstansiMain_btnSave"));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public void setOriginalTfeedbackinstansi(Tfeedbackinstansi originalTfeedbackinstansi) {
        this.originalTfeedbackinstansi = originalTfeedbackinstansi;
    }

    public Tfeedbackinstansi getOriginalTfeedbackinstansi() {
        return this.originalTfeedbackinstansi;
    }

    public void setSelectedTfeedbackinstansi(Tfeedbackinstansi selectedTfeedbackinstansi) {
        this.selectedTfeedbackinstansi = selectedTfeedbackinstansi;
    }

    public Tfeedbackinstansi getSelectedTfeedbackinstansi() {
        return this.selectedTfeedbackinstansi;
    }

    public void setTfeedbackinstansis(BindingListModelList tfeedbackinstansis) {
        this.tfeedbackinstansis = tfeedbackinstansis;
    }

    public BindingListModelList getTfeedbackinstansis() {
        return this.tfeedbackinstansis;
    }

    public void setTfeedbackinstansiService(TfeedbackinstansiService tfeedbackinstansiService) {
        this.tfeedbackinstansiService = tfeedbackinstansiService;
    }

    public TfeedbackinstansiService getTfeedbackinstansiService() {
        return this.tfeedbackinstansiService;
    }

    public void setTfeedbackinstansiListCtrl(TfeedbackinstansiListCtrl tfeedbackinstansiListCtrl) {
        this.tfeedbackinstansiListCtrl = tfeedbackinstansiListCtrl;
    }

    public TfeedbackinstansiListCtrl getTfeedbackinstansiListCtrl() {
        return this.tfeedbackinstansiListCtrl;
    }

    public void setTfeedbackinstansiDetailCtrl(TfeedbackinstansiDetailCtrl tfeedbackinstansiDetailCtrl) {
        this.tfeedbackinstansiDetailCtrl = tfeedbackinstansiDetailCtrl;
    }

    public TfeedbackinstansiDetailCtrl getTfeedbackinstansiDetailCtrl() {
        return this.tfeedbackinstansiDetailCtrl;
    }

}
