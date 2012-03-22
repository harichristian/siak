package id.ac.idu.webui.administrasi.statusMahasiswa;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.StatusMahasiswaService;
import id.ac.idu.backend.model.Mstatusmhs;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.util.ConstantUtil;
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
 * User: valeo
 * Date: 3/8/12
 * Time: 11:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class StatusMahasiswaMainCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(StatusMahasiswaMainCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowStatusMahasiswaMain; // autowired

    // Tabs
    protected Tabbox tabbox_StatusMahasiswaMain; // autowired
    protected Tab tabStatusMahasiswaList; // autowired
    protected Tab tabStatusMahasiswaDetail; // autowired
    protected Tabpanel tabPanelStatusMahasiswaList; // autowired
    protected Tabpanel tabPanelStatusMahasiswaDetail; // autowired

    // filter components
    protected Checkbox checkbox_StatusMahasiswaList_ShowAll; // autowired
    protected Textbox txtb_StatusMahasiswa_Code; // aurowired
    protected Button button_StatusMahasiswaList_SearchCode; // aurowired
    protected Textbox txtb_StatusMahasiswa_Name; // aurowired
    protected Button button_StatusMahasiswaList_SearchName; // aurowired
    protected Textbox txtb_StatusMahasiswa_Keterangan; // aurowired
    protected Button button_StatusMahasiswaList_SearchKeterangan; // aurowired

    // checkRights
    protected Button button_StatusMahasiswaList_PrintList;

    // Button controller for the CRUD buttons
    private final String btnCtroller_ClassPrefix = "button_StatusMahasiswaMain_";
    private ButtonStatusCtrl btnCtrlStatusMahasiswa;
    protected Button btnNew; // autowired
    protected Button btnEdit; // autowired
    protected Button btnDelete; // autowired
    protected Button btnSave; // autowired
    protected Button btnCancel; // autowired

    protected Button btnHelp;

    // Tab-Controllers for getting the binders
    private StatusMahasiswaListCtrl statusMahasiswaListCtrl;
    private StatusMahasiswaDetailCtrl statusMahasiswaDetailCtrl;

    // Databinding
    private Mstatusmhs selectedStatusMahasiswa;
    private BindingListModelList statusMahasiswas;

    // ServiceDAOs / Domain Classes
    private StatusMahasiswaService statusMahasiswaService;

    // always a copy from the bean before modifying. Used for reseting
    private Mstatusmhs originalStatusMahasiswa;

    /**
     * default constructor.<br>
     */
    public StatusMahasiswaMainCtrl() {
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
    public void onCreate$windowStatusMahasiswaMain(Event event) throws Exception {
        windowStatusMahasiswaMain.setContentStyle("padding:0px;");

        // create the Button Controller. Disable not used buttons during working
        btnCtrlStatusMahasiswa = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

        //doCheckRights();

        /**
         * Initiate the first loading by selecting the customerList tab and
         * create the components from the zul-file.
         */
        tabStatusMahasiswaList.setSelected(true);

        if (tabPanelStatusMahasiswaList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelStatusMahasiswaList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/statusMahasiswa/statusMahasiswaList.zul");
        }

        // init the buttons for editMode
        btnCtrlStatusMahasiswa.setInitEdit();
    }

    /**
     * When the tab 'tabStatusMahasiswaList' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabStatusMahasiswaList(Event event) throws IOException {
        logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelStatusMahasiswaList.getFirstChild() != null) {
            tabStatusMahasiswaList.setSelected(true);

            return;
        }

        if (tabPanelStatusMahasiswaList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelStatusMahasiswaList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/statusMahasiswa/statusMahasiswaList.zul");
        }

    }

    /**
     * When the tab 'tabPanelStatusMahasiswaDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws IOException
     */
    public void onSelect$tabStatusMahasiswaDetail(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelStatusMahasiswaDetail.getFirstChild() != null) {
            tabStatusMahasiswaDetail.setSelected(true);

            // refresh the Binding mechanism
            getStatusMahasiswaDetailCtrl().setStatusMahasiswa(getSelectedStatusMahasiswa());
            getStatusMahasiswaDetailCtrl().getBinder().loadAll();
            return;
        }

        if (tabPanelStatusMahasiswaDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelStatusMahasiswaDetail, this, "ModuleMainController", "/WEB-INF/pages/administrasi/statusMahasiswa/statusMahasiswaDetail.zul");
        }
    }

    /**
     * when the "print statusMahasiswas list" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_StatusMahasiswaList_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        //new StatusMahasiswaSimpleDJReport(win);
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_StatusMahasiswaList_ShowAll(Event event) {
        // logger.debug(event.toString());

        // empty the text search boxes
        txtb_StatusMahasiswa_Code.setValue(""); // clear
        txtb_StatusMahasiswa_Name.setValue(""); // clear
        txtb_StatusMahasiswa_Keterangan.setValue(""); // clear

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Mstatusmhs> soStatusMahasiswa = new HibernateSearchObject<Mstatusmhs>(Mstatusmhs.class, getStatusMahasiswaListCtrl().getCountRows());
        soStatusMahasiswa.addSort(ConstantUtil.MAHASISWA_NAME, false);

        // Change the BindingListModel.
        if (getStatusMahasiswaListCtrl().getBinder() != null) {
            getStatusMahasiswaListCtrl().getPagedBindingListWrapper().setSearchObject(soStatusMahasiswa);

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_StatusMahasiswaMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabStatusMahasiswaList)) {
                tabStatusMahasiswaList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }

    }

    /**
     * Filter the statusMahasiswa list with 'like statusMahasiswa number'. <br>
     */
    public void onClick$button_StatusMahasiswaList_SearchCode(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_StatusMahasiswa_Code.getValue().isEmpty()) {
            checkbox_StatusMahasiswaList_ShowAll.setChecked(false); // unCheck
            txtb_StatusMahasiswa_Name.setValue(""); // clear
            txtb_StatusMahasiswa_Keterangan.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mstatusmhs> soStatusMahasiswa = new HibernateSearchObject<Mstatusmhs>(Mstatusmhs.class, getStatusMahasiswaListCtrl().getCountRows());
            soStatusMahasiswa.addFilter(new Filter(ConstantUtil.MAHASISWA_CODE, "%" + txtb_StatusMahasiswa_Code.getValue() + "%", Filter.OP_ILIKE));
            soStatusMahasiswa.addSort(ConstantUtil.MAHASISWA_CODE, false);

            // Change the BindingListModel.
            if (getStatusMahasiswaListCtrl().getBinder() != null) {
                getStatusMahasiswaListCtrl().getPagedBindingListWrapper().setSearchObject(soStatusMahasiswa);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_StatusMahasiswaMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabStatusMahasiswaList)) {
                    tabStatusMahasiswaList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the statusMahasiswa list with 'like statusMahasiswa name'. <br>
     */
    public void onClick$button_StatusMahasiswaList_SearchName(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_StatusMahasiswa_Name.getValue().isEmpty()) {
            checkbox_StatusMahasiswaList_ShowAll.setChecked(false); // unCheck
            txtb_StatusMahasiswa_Keterangan.setValue(""); // clear
            txtb_StatusMahasiswa_Code.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mstatusmhs> soStatusMahasiswa = new HibernateSearchObject<Mstatusmhs>(Mstatusmhs.class, getStatusMahasiswaListCtrl().getCountRows());
            soStatusMahasiswa.addFilter(new Filter(ConstantUtil.MAHASISWA_NAME, "%" + txtb_StatusMahasiswa_Name.getValue() + "%", Filter.OP_ILIKE));
            soStatusMahasiswa.addSort(ConstantUtil.MAHASISWA_NAME, false);

            // Change the BindingListModel.
            if (getStatusMahasiswaListCtrl().getBinder() != null) {
                getStatusMahasiswaListCtrl().getPagedBindingListWrapper().setSearchObject(soStatusMahasiswa);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_StatusMahasiswaMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabStatusMahasiswaList)) {
                    tabStatusMahasiswaList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the statusMahasiswa list with 'like statusMahasiswa city'. <br>
     */
    public void onClick$button_StatusMahasiswaList_SearchKeterangan(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_StatusMahasiswa_Keterangan.getValue().isEmpty()) {
            checkbox_StatusMahasiswaList_ShowAll.setChecked(false); // unCheck
            txtb_StatusMahasiswa_Name.setValue(""); // clear
            txtb_StatusMahasiswa_Code.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mstatusmhs> soStatusMahasiswa = new HibernateSearchObject<Mstatusmhs>(Mstatusmhs.class, getStatusMahasiswaListCtrl().getCountRows());
            soStatusMahasiswa.addFilter(new Filter(ConstantUtil.KETERANGAN, "%" + txtb_StatusMahasiswa_Keterangan.getValue() + "%", Filter.OP_ILIKE));
            soStatusMahasiswa.addSort(ConstantUtil.KETERANGAN, false);

            // Change the BindingListModel.
            if (getStatusMahasiswaListCtrl().getBinder() != null) {
                getStatusMahasiswaListCtrl().getPagedBindingListWrapper().setSearchObject(soStatusMahasiswa);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_StatusMahasiswaMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabStatusMahasiswaList)) {
                    tabStatusMahasiswaList.setSelected(true);
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
        if (getStatusMahasiswaDetailCtrl().getBinder() != null) {

            // refresh all dataBinder related controllers/components
            getStatusMahasiswaDetailCtrl().getBinder().loadAll();

            // set editable Mode
            getStatusMahasiswaDetailCtrl().doReadOnlyMode(true);

            btnCtrlStatusMahasiswa.setInitEdit();
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
        Tab currentTab = tabbox_StatusMahasiswaMain.getSelectedTab();

        // check first, if the tabs are created, if not than create it
        if (getStatusMahasiswaDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabStatusMahasiswaDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getStatusMahasiswaDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabStatusMahasiswaDetail, null));
        }

        // check if the tab is one of the Detail tabs. If so do not change the
        // selection of it
        if (!currentTab.equals(tabStatusMahasiswaDetail)) {
            tabStatusMahasiswaDetail.setSelected(true);
        } else {
            currentTab.setSelected(true);
        }

        getStatusMahasiswaDetailCtrl().getBinder().loadAll();

        // remember the old vars
        doStoreInitValues();

        btnCtrlStatusMahasiswa.setBtnStatus_Edit();

        getStatusMahasiswaDetailCtrl().doReadOnlyMode(false);
        // set focus
        getStatusMahasiswaDetailCtrl().txtb_code.focus();
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
        if (getStatusMahasiswaDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabStatusMahasiswaDetail, null));
        }

        // check first, if the tabs are created
        if (getStatusMahasiswaDetailCtrl().getBinder() == null) {
            return;
        }

        final Mstatusmhs anStatusMahasiswa = getSelectedStatusMahasiswa();
        if (anStatusMahasiswa != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anStatusMahasiswa.getCnmstatmhs();
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
                    try {
                        getStatusMahasiswaService().delete(anStatusMahasiswa);
                    } catch (DataAccessException e) {
                        ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                    }
                }

            }

            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrlStatusMahasiswa.setInitEdit();

        setSelectedStatusMahasiswa(null);
        // refresh the list
        getStatusMahasiswaListCtrl().doFillListbox();

        // refresh all dataBinder related controllers
        getStatusMahasiswaDetailCtrl().getBinder().loadAll();
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
        getStatusMahasiswaDetailCtrl().getBinder().saveAll();

        try {
            // save it to database
            getStatusMahasiswaService().saveOrUpdate(getStatusMahasiswaDetailCtrl().getStatusMahasiswa());
            // if saving is successfully than actualize the beans as
            // origins.
            doStoreInitValues();
            // refresh the list
            getStatusMahasiswaListCtrl().doFillListbox();
            // later refresh StatusBar
            Events.postEvent("onSelect", getStatusMahasiswaListCtrl().getListBoxStatusMahasiswa(), getSelectedStatusMahasiswa());

            // show the objects data in the statusBar
            String str = getSelectedStatusMahasiswa().getCnmstatmhs();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetToInitValues();

            return;

        } finally {
            btnCtrlStatusMahasiswa.setInitEdit();
            getStatusMahasiswaDetailCtrl().doReadOnlyMode(true);
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
        if (getStatusMahasiswaDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabStatusMahasiswaDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getStatusMahasiswaDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabStatusMahasiswaDetail, null));
        }

        // remember the current object
        doStoreInitValues();

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        final Mstatusmhs anStatusMahasiswa = getStatusMahasiswaService().getNewStatusMahasiswa();

        // set the beans in the related databinded controllers
        getStatusMahasiswaDetailCtrl().setStatusMahasiswa(anStatusMahasiswa);
        getStatusMahasiswaDetailCtrl().setSelectedStatusMahasiswa(anStatusMahasiswa);

        // Refresh the binding mechanism
        getStatusMahasiswaDetailCtrl().setSelectedStatusMahasiswa(getSelectedStatusMahasiswa());
        if (getStatusMahasiswaDetailCtrl().getBinder() != null) {
            getStatusMahasiswaDetailCtrl().getBinder().loadAll();
        }

        // set editable Mode
        getStatusMahasiswaDetailCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlStatusMahasiswa.setInitNew();

        tabStatusMahasiswaDetail.setSelected(true);
        // set focus
        getStatusMahasiswaDetailCtrl().txtb_code.focus();

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

        if (tabbox_StatusMahasiswaMain.getSelectedTab() == tabStatusMahasiswaDetail) {
            getStatusMahasiswaDetailCtrl().doFitSize(event);
        } else if (tabbox_StatusMahasiswaMain.getSelectedTab() == tabStatusMahasiswaList) {
            // resize and fill Listbox new
            getStatusMahasiswaListCtrl().doFillListbox();
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
     */
    public void doStoreInitValues() {

        if (getSelectedStatusMahasiswa() != null) {

            try {
                setOriginalStatusMahasiswa((Mstatusmhs) ZksampleBeanUtils.cloneBean(getSelectedStatusMahasiswa()));
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
     */
    public void doResetToInitValues() {

        if (getOriginalStatusMahasiswa() != null) {

            try {
                setSelectedStatusMahasiswa((Mstatusmhs) ZksampleBeanUtils.cloneBean(getOriginalStatusMahasiswa()));
                // TODO Bug in DataBinder??
                windowStatusMahasiswaMain.invalidate();

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

        // window_StatusMahasiswaList.setVisible(workspace.isAllowed("window_StatusMahasiswaList"));
        button_StatusMahasiswaList_PrintList.setVisible(workspace.isAllowed("button_StatusMahasiswaList_PrintList"));
        button_StatusMahasiswaList_SearchCode.setVisible(workspace.isAllowed("button_StatusMahasiswaList_SearchCode"));
        button_StatusMahasiswaList_SearchName.setVisible(workspace.isAllowed("button_StatusMahasiswaList_SearchName"));
        button_StatusMahasiswaList_SearchKeterangan.setVisible(workspace.isAllowed("button_StatusMahasiswaList_SearchKeterangan"));

        btnHelp.setVisible(workspace.isAllowed("button_StatusMahasiswaMain_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_StatusMahasiswaMain_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_StatusMahasiswaMain_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_StatusMahasiswaMain_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_StatusMahasiswaMain_btnSave"));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public void setOriginalStatusMahasiswa(Mstatusmhs originalStatusMahasiswa) {
        this.originalStatusMahasiswa = originalStatusMahasiswa;
    }

    public Mstatusmhs getOriginalStatusMahasiswa() {
        return this.originalStatusMahasiswa;
    }

    public void setSelectedStatusMahasiswa(Mstatusmhs selectedStatusMahasiswa) {
        this.selectedStatusMahasiswa = selectedStatusMahasiswa;
    }

    public Mstatusmhs getSelectedStatusMahasiswa() {
        return this.selectedStatusMahasiswa;
    }

    public void setStatusMahasiswas(BindingListModelList statusMahasiswas) {
        this.statusMahasiswas = statusMahasiswas;
    }

    public BindingListModelList getStatusMahasiswas() {
        return this.statusMahasiswas;
    }

    public void setStatusMahasiswaService(StatusMahasiswaService statusMahasiswaService) {
        this.statusMahasiswaService = statusMahasiswaService;
    }

    public StatusMahasiswaService getStatusMahasiswaService() {
        return this.statusMahasiswaService;
    }

    public void setStatusMahasiswaListCtrl(StatusMahasiswaListCtrl statusMahasiswaListCtrl) {
        this.statusMahasiswaListCtrl = statusMahasiswaListCtrl;
    }

    public StatusMahasiswaListCtrl getStatusMahasiswaListCtrl() {
        return this.statusMahasiswaListCtrl;
    }

    public void setStatusMahasiswaDetailCtrl(StatusMahasiswaDetailCtrl statusMahasiswaDetailCtrl) {
        this.statusMahasiswaDetailCtrl = statusMahasiswaDetailCtrl;
    }

    public StatusMahasiswaDetailCtrl getStatusMahasiswaDetailCtrl() {
        return this.statusMahasiswaDetailCtrl;
    }
}
