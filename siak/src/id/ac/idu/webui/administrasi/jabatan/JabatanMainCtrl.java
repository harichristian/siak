package id.ac.idu.webui.administrasi.jabatan;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.JabatanService;
import id.ac.idu.backend.model.Mjabatan;
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
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * Main controller for the jabatan main module.<br>
 * <br>
 * zul-file: /WEB-INF/pages/jabatan/jabatanMain.zul.<br>
 * This class creates the Tabs + TabPanels. The components/data to all tabs are
 * created on demand on first time selecting the tab.<br>
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * <br>
 *
 * @author Hari
 * @changes 07/04/2010: sge modified for zk5.x with complete Annotated
 * Databinding.<br>
 * <p/>
 * Managed tabs:<br>
 * - JabatanListCtrl = Jabatan List / Filialen Liste<br>
 * - JabatanDetailCtrl = Jabatan Details / Filiale-Details<br>
 */
public class JabatanMainCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(JabatanMainCtrl.class);

    /*
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    * All the components that are defined here and have a corresponding
    * component with the same 'id' in the zul-file are getting autowired by our
    * 'extends GFCBaseCtrl' GenericForwardComposer.
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    */
    protected Window windowJabatanMain; // autowired

    // Tabs
    protected Tabbox tabbox_JabatanMain; // autowired
    protected Tab tabJabatanList; // autowired
    protected Tab tabJabatanDetail; // autowired
    protected Tabpanel tabPanelJabatanList; // autowired
    protected Tabpanel tabPanelJabatanDetail; // autowired

    // filter components
    protected Checkbox checkbox_JabatanList_ShowAll; // autowired
    protected Textbox txtb_Jabatan_No; // aurowired
    protected Button button_JabatanList_SearchNo; // aurowired
    protected Textbox txtb_Jabatan_Name; // aurowired
    protected Button button_JabatanList_SearchName; // aurowired

    // checkRights
    protected Button button_JabatanList_PrintList;

    // Button controller for the CRUD buttons
    private final String btnCtroller_ClassPrefix = "button_JabatanMain_";
    private ButtonStatusCtrl btnCtrlJabatan;
    protected Button btnNew; // autowired
    protected Button btnEdit; // autowired
    protected Button btnDelete; // autowired
    protected Button btnSave; // autowired
    protected Button btnCancel; // autowired

    protected Button btnHelp;

    // Tab-Controllers for getting the binders
    private JabatanListCtrl jabatanListCtrl;
    private JabatanDetailCtrl jabatanDetailCtrl;

    // Databinding
    private Mjabatan selectedJabatan;
    private BindingListModelList jabatans;

    // ServiceDAOs / Domain Classes
    private JabatanService jabatanService;

    // always a copy from the bean before modifying. Used for reseting
    private Mjabatan originalJabatan;

    /**
     * default constructor.<br>
     */
    public JabatanMainCtrl() {
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
    public void onCreate$windowJabatanMain(Event event) throws Exception {
        windowJabatanMain.setContentStyle("padding:0px;");

        // create the Button Controller. Disable not used buttons during working
        btnCtrlJabatan = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

//            doCheckRights();

        /**
         * Initiate the first loading by selecting the customerList tab and
         * create the components from the zul-file.
         */
        tabJabatanList.setSelected(true);

        if (tabPanelJabatanList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelJabatanList, this, "ModuleMainController", "/WEB-INF/pages/jabatan/jabatanList.zul");
        }

        // init the buttons for editMode
        btnCtrlJabatan.setInitEdit();
    }

    /**
     * When the tab 'tabJabatanList' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabJabatanList(Event event) throws IOException {
        logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelJabatanList.getFirstChild() != null) {
            tabJabatanList.setSelected(true);

            return;
        }

        if (tabPanelJabatanList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelJabatanList, this, "ModuleMainController", "/WEB-INF/pages/jabatan/jabatanList.zul");
        }

    }

    /**
     * When the tab 'tabPanelJabatanDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws IOException
     */
    public void onSelect$tabJabatanDetail(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelJabatanDetail.getFirstChild() != null) {
            tabJabatanDetail.setSelected(true);

            // refresh the Binding mechanism
            getJabatanDetailCtrl().setJabatan(getSelectedJabatan());
            getJabatanDetailCtrl().getBinder().loadAll();
            return;
        }

        if (tabPanelJabatanDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelJabatanDetail, this, "ModuleMainController", "/WEB-INF/pages/jabatan/jabatanDetail.zul");
        }
    }

    /**
     * when the "print jabatans list" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_JabatanList_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        //new JabatanSimpleDJReport(win); // TODO
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_JabatanList_ShowAll(Event event) {
        // logger.debug(event.toString());

        // empty the text search boxes
        txtb_Jabatan_No.setValue(""); // clear
        txtb_Jabatan_Name.setValue(""); // clear

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Mjabatan> soJabatan = new HibernateSearchObject<Mjabatan>(Mjabatan.class, getJabatanListCtrl().getCountRows());
        soJabatan.addSort("ckdjabatan", false);

        // Change the BindingListModel.
        if (getJabatanListCtrl().getBinder() != null) {
            getJabatanListCtrl().getPagedBindingListWrapper().setSearchObject(soJabatan);

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_JabatanMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabJabatanList)) {
                tabJabatanList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }

    }

    /**
     * Filter the jabatan list with 'like jabatan number'. <br>
     */
    public void onClick$button_JabatanList_SearchNo(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Jabatan_No.getValue().isEmpty()) {
            checkbox_JabatanList_ShowAll.setChecked(false); // unCheck
            txtb_Jabatan_Name.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mjabatan> soJabatan = new HibernateSearchObject<Mjabatan>(Mjabatan.class, getJabatanListCtrl().getCountRows());
            soJabatan.addFilter(new Filter("ckdjabatan", "%" + txtb_Jabatan_No.getValue() + "%", Filter.OP_ILIKE));
            soJabatan.addSort("ckdjabatan", false);

            // Change the BindingListModel.
            if (getJabatanListCtrl().getBinder() != null) {
                getJabatanListCtrl().getPagedBindingListWrapper().setSearchObject(soJabatan);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_JabatanMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabJabatanList)) {
                    tabJabatanList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the jabatan list with 'like jabatan name'. <br>
     */
    public void onClick$button_JabatanList_SearchName(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Jabatan_Name.getValue().isEmpty()) {
            checkbox_JabatanList_ShowAll.setChecked(false); // unCheck
            txtb_Jabatan_No.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mjabatan> soJabatan = new HibernateSearchObject<Mjabatan>(Mjabatan.class, getJabatanListCtrl().getCountRows());
            soJabatan.addFilter(new Filter("cnmjabatan", "%" + txtb_Jabatan_Name.getValue() + "%", Filter.OP_ILIKE));
            soJabatan.addSort("cnmjabatan", false);

            // Change the BindingListModel.
            if (getJabatanListCtrl().getBinder() != null) {
                getJabatanListCtrl().getPagedBindingListWrapper().setSearchObject(soJabatan);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_JabatanMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabJabatanList)) {
                    tabJabatanList.setSelected(true);
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
        if (getJabatanDetailCtrl().getBinder() != null) {

            // refresh all dataBinder related controllers/components
            getJabatanDetailCtrl().getBinder().loadAll();

            // set editable Mode
            getJabatanDetailCtrl().doReadOnlyMode(true);

            btnCtrlJabatan.setInitEdit();
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
        Tab currentTab = tabbox_JabatanMain.getSelectedTab();

        // check first, if the tabs are created, if not than create it
        if (getJabatanDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabJabatanDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getJabatanDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabJabatanDetail, null));
        }

        // check if the tab is one of the Detail tabs. If so do not change the
        // selection of it
        if (!currentTab.equals(tabJabatanDetail)) {
            tabJabatanDetail.setSelected(true);
        } else {
            currentTab.setSelected(true);
        }

        getJabatanDetailCtrl().getBinder().loadAll();

        // remember the old vars
        doStoreInitValues();

        btnCtrlJabatan.setBtnStatus_Edit();

        getJabatanDetailCtrl().doReadOnlyMode(false);
        // set focus
        getJabatanDetailCtrl().txtb_filNr.focus();
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
        if (getJabatanDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabJabatanDetail, null));
        }

        // check first, if the tabs are created
        if (getJabatanDetailCtrl().getBinder() == null) {
            return;
        }

        final Mjabatan anJabatan = getSelectedJabatan();
        if (anJabatan != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anJabatan.toString();
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
                     * Do not allow to modify the demo jabatans
                     */
                    if (getJabatanDetailCtrl().getJabatan().getId() <= 2) {
                        try {
                            ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
                            return;
                        } catch (final InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        try {
                            getJabatanService().delete(anJabatan);
                        } catch (DataAccessException e) {
                            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                        }
                    }
                }

            }

            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrlJabatan.setInitEdit();

        setSelectedJabatan(null);
        // refresh the list
        getJabatanListCtrl().doFillListbox();

        // refresh all dataBinder related controllers
        getJabatanDetailCtrl().getBinder().loadAll();
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
        getJabatanDetailCtrl().getBinder().saveAll();

        try {

            /**
             * Do not allow to modify the demo jabatans
             */
//                if (getJabatanDetailCtrl().getJabatan().getId() <= 2) {
//                    ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
//
//                    doResetToInitValues();
//                    getJabatanDetailCtrl().getBinder().loadAll();
//                    return;
//                }

            // save it to database
            getJabatanService().saveOrUpdate(getJabatanDetailCtrl().getJabatan());
            // if saving is successfully than actualize the beans as
            // origins.
            doStoreInitValues();
            // refresh the list
            getJabatanListCtrl().doFillListbox();
            // later refresh StatusBar
            Events.postEvent("onSelect", getJabatanListCtrl().getListBoxJabatan(), getSelectedJabatan());

            // show the objects data in the statusBar
            String str = getSelectedJabatan().toString();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetToInitValues();

            return;

        } finally {
            btnCtrlJabatan.setInitEdit();
            getJabatanDetailCtrl().doReadOnlyMode(true);
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
        if (getJabatanDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabJabatanDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getJabatanDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabJabatanDetail, null));
        }

        // remember the current object
        doStoreInitValues();

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        final Mjabatan anJabatan = getJabatanService().getNewJabatan();

        // set the beans in the related databinded controllers
        getJabatanDetailCtrl().setJabatan(anJabatan);
        getJabatanDetailCtrl().setSelectedJabatan(anJabatan);

        // Refresh the binding mechanism
        getJabatanDetailCtrl().setSelectedJabatan(getSelectedJabatan());
        if (getJabatanDetailCtrl().getBinder() != null) {
            getJabatanDetailCtrl().getBinder().loadAll();
        }

        // set editable Mode
        getJabatanDetailCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlJabatan.setInitNew();

        tabJabatanDetail.setSelected(true);
        // set focus
        getJabatanDetailCtrl().txtb_filNr.focus();

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

        if (tabbox_JabatanMain.getSelectedTab() == tabJabatanDetail) {
            getJabatanDetailCtrl().doFitSize(event);
        } else if (tabbox_JabatanMain.getSelectedTab() == tabJabatanList) {
            // resize and fill Listbox new
            getJabatanListCtrl().doFillListbox();
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

        if (getSelectedJabatan() != null) {

            try {
                setOriginalJabatan((Mjabatan) ZksampleBeanUtils.cloneBean(getSelectedJabatan()));
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

        if (getOriginalJabatan() != null) {

            try {
                setSelectedJabatan((Mjabatan) ZksampleBeanUtils.cloneBean(getOriginalJabatan()));
                // TODO Bug in DataBinder??
                windowJabatanMain.invalidate();

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

        // window_JabatanList.setVisible(workspace.isAllowed("window_JabatanList"));
        button_JabatanList_PrintList.setVisible(workspace.isAllowed("button_JabatanList_PrintList"));
        button_JabatanList_SearchNo.setVisible(workspace.isAllowed("button_JabatanList_SearchNo"));
        button_JabatanList_SearchName.setVisible(workspace.isAllowed("button_JabatanList_SearchName"));


        btnHelp.setVisible(workspace.isAllowed("button_JabatanMain_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_JabatanMain_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_JabatanMain_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_JabatanMain_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_JabatanMain_btnSave"));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public void setOriginalJabatan(Mjabatan originalJabatan) {
        this.originalJabatan = originalJabatan;
    }

    public Mjabatan getOriginalJabatan() {
        return this.originalJabatan;
    }

    public void setSelectedJabatan(Mjabatan selectedJabatan) {
        this.selectedJabatan = selectedJabatan;
    }

    public Mjabatan getSelectedJabatan() {
        return this.selectedJabatan;
    }

    public void setJabatans(BindingListModelList jabatans) {
        this.jabatans = jabatans;
    }

    public BindingListModelList getJabatans() {
        return this.jabatans;
    }

    public void setJabatanService(JabatanService jabatanService) {
        this.jabatanService = jabatanService;
    }

    public JabatanService getJabatanService() {
        return this.jabatanService;
    }

    public void setJabatanListCtrl(JabatanListCtrl jabatanListCtrl) {
        this.jabatanListCtrl = jabatanListCtrl;
    }

    public JabatanListCtrl getJabatanListCtrl() {
        return this.jabatanListCtrl;
    }

    public void setJabatanDetailCtrl(JabatanDetailCtrl jabatanDetailCtrl) {
        this.jabatanDetailCtrl = jabatanDetailCtrl;
    }

    public JabatanDetailCtrl getJabatanDetailCtrl() {
        return this.jabatanDetailCtrl;
    }

}
