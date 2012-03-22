package id.ac.idu.webui.administrasi.fasilitas;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.FasilitasService;
import id.ac.idu.backend.model.Mfasilitas;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.webui.administrasi.report.FasilitasSimpleDJReport;
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
 * Main controller for the fasilitas main module.<br>
 * <br>
 * zul-file: /WEB-INF/pages/fasilitas/fasilitasMain.zul.<br>
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
 * - FasilitasListCtrl = Fasilitas List / Filialen Liste<br>
 * - FasilitasDetailCtrl = Fasilitas Details / Filiale-Details<br>
 */
public class FasilitasMainCtrl extends GFCBaseCtrl implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(FasilitasMainCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowFasilitasMain; // autowired

    // Tabs
    protected Tabbox tabbox_FasilitasMain; // autowired
    protected Tab tabFasilitasList; // autowired
    protected Tab tabFasilitasDetail; // autowired
    protected Tabpanel tabPanelFasilitasList; // autowired
    protected Tabpanel tabPanelFasilitasDetail; // autowired

    // filter components
    protected Checkbox checkbox_FasilitasList_ShowAll; // autowired
    protected Textbox txtb_Fasilitas_No; // aurowired
    protected Button button_FasilitasList_SearchNo; // aurowired
    protected Textbox txtb_Fasilitas_Name; // aurowired
    protected Button button_FasilitasList_SearchName; // aurowired
    protected Textbox txtb_Fasilitas_City; // aurowired
    protected Button button_FasilitasList_SearchCity; // aurowired

    // checkRights
    protected Button button_FasilitasList_PrintList;

    // Button controller for the CRUD buttons
    private final String btnCtroller_ClassPrefix = "button_FasilitasMain_";
    private ButtonStatusCtrl btnCtrlFasilitas;
    protected Button btnNew; // autowired
    protected Button btnEdit; // autowired
    protected Button btnDelete; // autowired
    protected Button btnSave; // autowired
    protected Button btnCancel; // autowired

    protected Button btnHelp;

    // Tab-Controllers for getting the binders
    private FasilitasListCtrl fasilitasListCtrl;
    private FasilitasDetailCtrl fasilitasDetailCtrl;

    // Databinding
    private Mfasilitas selectedFasilitas;
    private BindingListModelList fasilitass;

    // ServiceDAOs / Domain Classes
    private FasilitasService fasilitasService;

    // always a copy from the bean before modifying. Used for reseting
    private Mfasilitas originalFasilitas;

    /**
     * default constructor.<br>
     */
    public FasilitasMainCtrl() {
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
    public void onCreate$windowFasilitasMain(Event event) throws Exception {
        windowFasilitasMain.setContentStyle("padding:0px;");

        // create the Button Controller. Disable not used buttons during working
        btnCtrlFasilitas = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

        //doCheckRights();

        /**
         * Initiate the first loading by selecting the customerList tab and
         * create the components from the zul-file.
         */
        tabFasilitasList.setSelected(true);

        if (tabPanelFasilitasList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelFasilitasList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/fasilitas/fasilitasList.zul");
        }

        // init the buttons for editMode
        btnCtrlFasilitas.setInitEdit();
    }

    /**
     * When the tab 'tabFasilitasList' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabFasilitasList(Event event) throws IOException {
        logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelFasilitasList.getFirstChild() != null) {
            tabFasilitasList.setSelected(true);

            return;
        }

        if (tabPanelFasilitasList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelFasilitasList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/fasilitas/FasilitasList.zul");
        }

    }

    /**
     * When the tab 'tabPanelFasilitasDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabFasilitasDetail(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelFasilitasDetail.getFirstChild() != null) {
            tabFasilitasDetail.setSelected(true);

            // refresh the Binding mechanism
            getFasilitasDetailCtrl().setFasilitas(getSelectedFasilitas());
            getFasilitasDetailCtrl().getBinder().loadAll();
            return;
        }

        if (tabPanelFasilitasDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelFasilitasDetail, this, "ModuleMainController", "/WEB-INF/pages/administrasi/fasilitas/fasilitasDetail.zul");
        }
    }

    /**
     * when the "print fasilitass list" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_FasilitasList_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        new FasilitasSimpleDJReport(win);
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_FasilitasList_ShowAll(Event event) {
        // logger.debug(event.toString());

        // empty the text search boxes
        txtb_Fasilitas_No.setValue(""); // clear
        txtb_Fasilitas_Name.setValue(""); // clear
        txtb_Fasilitas_City.setValue(""); // clear

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Mfasilitas> soFasilitas = new HibernateSearchObject<Mfasilitas>(Mfasilitas.class, getFasilitasListCtrl().getCountRows());
        soFasilitas.addSort("filName1", false);

        // Change the BindingListModel.
        if (getFasilitasListCtrl().getBinder() != null) {
            getFasilitasListCtrl().getPagedBindingListWrapper().setSearchObject(soFasilitas);

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_FasilitasMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabFasilitasList)) {
                tabFasilitasList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }

    }

    /**
     * Filter the fasilitas list with 'like fasilitas number'. <br>
     */
    public void onClick$button_FasilitasList_SearchNo(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Fasilitas_No.getValue().isEmpty()) {
            checkbox_FasilitasList_ShowAll.setChecked(false); // unCheck
            txtb_Fasilitas_Name.setValue(""); // clear
            txtb_Fasilitas_City.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mfasilitas> soFasilitas = new HibernateSearchObject<Mfasilitas>(Mfasilitas.class, getFasilitasListCtrl().getCountRows());
            soFasilitas.addFilter(new Filter("filNr", "%" + txtb_Fasilitas_No.getValue() + "%", Filter.OP_ILIKE));
            soFasilitas.addSort("filNr", false);

            // Change the BindingListModel.
            if (getFasilitasListCtrl().getBinder() != null) {
                getFasilitasListCtrl().getPagedBindingListWrapper().setSearchObject(soFasilitas);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_FasilitasMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabFasilitasList)) {
                    tabFasilitasList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the fasilitas list with 'like fasilitas name'. <br>
     */
    public void onClick$button_FasilitasList_SearchName(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Fasilitas_Name.getValue().isEmpty()) {
            checkbox_FasilitasList_ShowAll.setChecked(false); // unCheck
            txtb_Fasilitas_City.setValue(""); // clear
            txtb_Fasilitas_No.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mfasilitas> soFasilitas = new HibernateSearchObject<Mfasilitas>(Mfasilitas.class, getFasilitasListCtrl().getCountRows());
            soFasilitas.addFilter(new Filter("filName1", "%" + txtb_Fasilitas_Name.getValue() + "%", Filter.OP_ILIKE));
            soFasilitas.addSort("filName1", false);

            // Change the BindingListModel.
            if (getFasilitasListCtrl().getBinder() != null) {
                getFasilitasListCtrl().getPagedBindingListWrapper().setSearchObject(soFasilitas);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_FasilitasMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabFasilitasList)) {
                    tabFasilitasList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the fasilitas list with 'like fasilitas city'. <br>
     */
    public void onClick$button_FasilitasList_SearchCity(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Fasilitas_City.getValue().isEmpty()) {
            checkbox_FasilitasList_ShowAll.setChecked(false); // unCheck
            txtb_Fasilitas_Name.setValue(""); // clear
            txtb_Fasilitas_No.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mfasilitas> soFasilitas = new HibernateSearchObject<Mfasilitas>(Mfasilitas.class, getFasilitasListCtrl().getCountRows());
            soFasilitas.addFilter(new Filter("filOrt", "%" + txtb_Fasilitas_City.getValue() + "%", Filter.OP_ILIKE));
            soFasilitas.addSort("filOrt", false);

            // Change the BindingListModel.
            if (getFasilitasListCtrl().getBinder() != null) {
                getFasilitasListCtrl().getPagedBindingListWrapper().setSearchObject(soFasilitas);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_FasilitasMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabFasilitasList)) {
                    tabFasilitasList.setSelected(true);
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
        if (getFasilitasDetailCtrl().getBinder() != null) {

            // refresh all dataBinder related controllers/components
            getFasilitasDetailCtrl().getBinder().loadAll();

            // set editable Mode
            getFasilitasDetailCtrl().doReadOnlyMode(true);

            btnCtrlFasilitas.setInitEdit();
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
        Tab currentTab = tabbox_FasilitasMain.getSelectedTab();

        // check first, if the tabs are created, if not than create it
        if (getFasilitasDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabFasilitasDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getFasilitasDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabFasilitasDetail, null));
        }

        // check if the tab is one of the Detail tabs. If so do not change the
        // selection of it
        if (!currentTab.equals(tabFasilitasDetail)) {
            tabFasilitasDetail.setSelected(true);
        } else {
            currentTab.setSelected(true);
        }

        getFasilitasDetailCtrl().getBinder().loadAll();

        // remember the old vars
        doStoreInitValues();

        btnCtrlFasilitas.setBtnStatus_Edit();

        getFasilitasDetailCtrl().doReadOnlyMode(false);
        // set focus
        getFasilitasDetailCtrl().txtb_kdfasilitas.focus();
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
        if (getFasilitasDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabFasilitasDetail, null));
        }

        // check first, if the tabs are created
        if (getFasilitasDetailCtrl().getBinder() == null) {
            return;
        }

        final Mfasilitas anFasilitas = getSelectedFasilitas();
        if (anFasilitas != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anFasilitas.getCnamaFasilitas();
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
                     * Do not allow to modify the demo fasilitass
                     */
                    if (getFasilitasDetailCtrl().getFasilitas().getId() <= 2) {
                        try {
                            ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
                            return;
                        } catch (final InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        try {
                            getFasilitasService().delete(anFasilitas);
                        } catch (DataAccessException e) {
                            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                        }
                    }
                }

            }

            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrlFasilitas.setInitEdit();

        setSelectedFasilitas(null);
        // refresh the list
        getFasilitasListCtrl().doFillListbox();

        // refresh all dataBinder related controllers
        getFasilitasDetailCtrl().getBinder().loadAll();
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
        getFasilitasDetailCtrl().getBinder().saveAll();

        try {

            /**
             * Do not allow to modify the demo fasilitass
             //			 */
//			if (getFasilitasDetailCtrl().getFasilitas().getId() <= 2) {
//				ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
//
//				doResetToInitValues();
//				getFasilitasDetailCtrl().getBinder().loadAll();
//				return;
//			}

            // save it to database
            getFasilitasService().saveOrUpdate(getFasilitasDetailCtrl().getFasilitas());
            // if saving is successfully than actualize the beans as
            // origins.
            doStoreInitValues();
            // refresh the list
            getFasilitasListCtrl().doFillListbox();
            // later refresh StatusBar
            Events.postEvent("onSelect", getFasilitasListCtrl().getListBoxFasilitas(), getSelectedFasilitas());

            // show the objects data in the statusBar
            String str = getSelectedFasilitas().getCnamaFasilitas();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetToInitValues();

            return;

        } finally {
            btnCtrlFasilitas.setInitEdit();
            getFasilitasDetailCtrl().doReadOnlyMode(true);
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
        if (getFasilitasDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabFasilitasDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getFasilitasDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabFasilitasDetail, null));
        }

        // remember the current object
        doStoreInitValues();

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        final Mfasilitas anFasilitas = getFasilitasService().getNewMfasilitas();

        // set the beans in the related databinded controllers
        getFasilitasDetailCtrl().setFasilitas(anFasilitas);
        getFasilitasDetailCtrl().setSelectedFasilitas(anFasilitas);

        // Refresh the binding mechanism
        getFasilitasDetailCtrl().setSelectedFasilitas(getSelectedFasilitas());
        if (getFasilitasDetailCtrl().getBinder() != null) {
            getFasilitasDetailCtrl().getBinder().loadAll();
        }
        // set editable Mode
        getFasilitasDetailCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlFasilitas.setInitNew();

        tabFasilitasDetail.setSelected(true);
        // set focus
        getFasilitasDetailCtrl().txtb_kdfasilitas.focus();

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

        if (tabbox_FasilitasMain.getSelectedTab() == tabFasilitasDetail) {
            getFasilitasDetailCtrl().doFitSize(event);
        } else if (tabbox_FasilitasMain.getSelectedTab() == tabFasilitasList) {
            // resize and fill Listbox new
            getFasilitasListCtrl().doFillListbox();
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
     * @see doResetToInitValues()
     */
    public void doStoreInitValues() {

        if (getSelectedFasilitas() != null) {

            try {
                setOriginalFasilitas((Mfasilitas) ZksampleBeanUtils.cloneBean(getSelectedFasilitas()));
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
     * @see doStoreInitValues()
     */
    public void doResetToInitValues() {

        if (getOriginalFasilitas() != null) {

            try {
                setSelectedFasilitas((Mfasilitas) ZksampleBeanUtils.cloneBean(getOriginalFasilitas()));
                // TODO Bug in DataBinder??
                windowFasilitasMain.invalidate();

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

        // window_FasilitasList.setVisible(workspace.isAllowed("window_FasilitasList"));
        button_FasilitasList_PrintList.setVisible(workspace.isAllowed("button_FasilitasList_PrintList"));
        button_FasilitasList_SearchNo.setVisible(workspace.isAllowed("button_FasilitasList_SearchNo"));
        button_FasilitasList_SearchName.setVisible(workspace.isAllowed("button_FasilitasList_SearchName"));
        button_FasilitasList_SearchCity.setVisible(workspace.isAllowed("button_FasilitasList_SearchCity"));

        btnHelp.setVisible(workspace.isAllowed("button_FasilitasMain_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_FasilitasMain_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_FasilitasMain_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_FasilitasMain_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_FasilitasMain_btnSave"));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public void setOriginalFasilitas(Mfasilitas originalFasilitas) {
        this.originalFasilitas = originalFasilitas;
    }

    public Mfasilitas getOriginalFasilitas() {
        return this.originalFasilitas;
    }

    public void setSelectedFasilitas(Mfasilitas selectedFasilitas) {
        this.selectedFasilitas = selectedFasilitas;
    }

    public Mfasilitas getSelectedFasilitas() {
        return this.selectedFasilitas;
    }

    public void setFasilitass(BindingListModelList fasilitass) {
        this.fasilitass = fasilitass;
    }

    public BindingListModelList getFasilitass() {
        return this.fasilitass;
    }

    public void setFasilitasService(FasilitasService fasilitasService) {
        this.fasilitasService = fasilitasService;
    }

    public FasilitasService getFasilitasService() {
        return this.fasilitasService;
    }

    public void setFasilitasListCtrl(FasilitasListCtrl fasilitasListCtrl) {
        this.fasilitasListCtrl = fasilitasListCtrl;
    }

    public FasilitasListCtrl getFasilitasListCtrl() {
        return this.fasilitasListCtrl;
    }

    public void setFasilitasDetailCtrl(FasilitasDetailCtrl fasilitasDetailCtrl) {
        this.fasilitasDetailCtrl = fasilitasDetailCtrl;
    }

    public FasilitasDetailCtrl getFasilitasDetailCtrl() {
        return this.fasilitasDetailCtrl;
    }

}
