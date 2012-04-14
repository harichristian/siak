package id.ac.idu.webui.administrasi.alumni;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.HistKerjaService;
import id.ac.idu.administrasi.service.MalumniService;
import id.ac.idu.backend.model.Malumni;
import id.ac.idu.backend.model.Mmahasiswa;
import id.ac.idu.backend.model.Thistkerja;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.webui.administrasi.report.MalumniSimpleDJReport;
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
import java.util.List;

/**
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * Main controller for the malumni main module.<br>
 * <br>
 * zul-file: /WEB-INF/pages/malumni/malumniMain.zul.<br>
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
 * - MalumniListCtrl = Malumni List / Filialen Liste<br>
 * - MalumniDetailCtrl = Malumni Details / Filiale-Details<br>
 */
public class MalumniMainCtrl extends GFCBaseCtrl implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(MalumniMainCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowMalumniMain; // autowired

    // Tabs
    protected Tabbox tabbox_MalumniMain; // autowired
    protected Tab tabMalumniList; // autowired
    protected Tab tabMalumniDetail; // autowired
    protected Tabpanel tabPanelMalumniList; // autowired
    protected Tabpanel tabPanelMalumniDetail; // autowired
    protected Tab  tabMalumniPekerjaan;
    protected Tabpanel tabPanelMalumniPekerjaan;

    // filter components
    protected Checkbox checkbox_MalumniList_ShowAll; // autowired
    protected Textbox txtb_Malumni_No; // aurowired
    protected Button button_MalumniList_SearchNo; // aurowired
    protected Textbox txtb_Malumni_Name; // aurowired
    protected Button button_MalumniList_SearchName; // aurowired
    protected Textbox txtb_Malumni_City; // aurowired
    protected Button button_MalumniList_SearchCity; // aurowired

    // checkRights
    protected Button button_MalumniList_PrintList;

    // Button controller for the CRUD buttons
    private final String btnCtroller_ClassPrefix = "button_MalumniMain_";
    private ButtonStatusCtrl btnCtrlMalumni;
    protected Button btnNew; // autowired
    protected Button btnEdit; // autowired
    protected Button btnDelete; // autowired
    protected Button btnSave; // autowired
    protected Button btnCancel; // autowired

    protected Button btnHelp;

    // Tab-Controllers for getting the binders
    private MalumniListCtrl malumniListCtrl;
    private MalumniDetailCtrl malumniDetailCtrl;
    private MalumniPekerjaanCtrl malumniPekerjaanCtrl;

    // Databinding
    private Malumni selectedMalumni;
    private BindingListModelList malumnis;

    // ServiceDAOs / Domain Classes
    private MalumniService malumniService;
    private HistKerjaService histKerjaService;

    // always a copy from the bean before modifying. Used for reseting
    private Malumni originalMalumni;
    private Mmahasiswa mahasiswa;

    /**
     * default constructor.<br>
     */
    public MalumniMainCtrl() {
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
    public void onCreate$windowMalumniMain(Event event) throws Exception {
        windowMalumniMain.setContentStyle("padding:0px;");

        // create the Button Controller. Disable not used buttons during working
        btnCtrlMalumni = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

        //doCheckRights();

        /**
         * Initiate the first loading by selecting the customerList tab and
         * create the components from the zul-file.
         */
        tabMalumniList.setSelected(true);

        if (tabPanelMalumniList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelMalumniList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/alumni/malumniList.zul");
        }

        // init the buttons for editMode
        btnCtrlMalumni.setInitEdit();
    }

    /**
     * When the tab 'tabMalumniList' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabMalumniList(Event event) throws IOException {
        logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelMalumniList.getFirstChild() != null) {
            tabMalumniList.setSelected(true);

            return;
        }

        if (tabPanelMalumniList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelMalumniList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/alumni/malumniList.zul");
        }

    }

    /**
     * When the tab 'tabPanelMalumniDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabMalumniDetail(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelMalumniDetail.getFirstChild() != null) {
            tabMalumniDetail.setSelected(true);

            // refresh the Binding mechanism
            getMalumniDetailCtrl().setMalumni(getSelectedMalumni());
            getMalumniDetailCtrl().getBinder().loadAll();
            getMalumniDetailCtrl().doResetCombo();
            return;
        }

        if (tabPanelMalumniDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelMalumniDetail, this, "ModuleMainController", "/WEB-INF/pages/administrasi/alumni/malumniDetail.zul");
        }
    }

     /**
     * When the tab 'tabPanelMalumniDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabMalumniPekerjaan(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelMalumniPekerjaan.getFirstChild() != null) {
            tabMalumniPekerjaan.setSelected(true);

            // refresh the Binding mechanism

                getMalumniPekerjaanCtrl().setMalumni(getSelectedMalumni());
                getMalumniPekerjaanCtrl().getBinder().loadAll();

            return;
        }

        if (tabPanelMalumniPekerjaan != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelMalumniPekerjaan, this, "ModuleMainController", "/WEB-INF/pages/administrasi/alumni/malumniPekerjaan.zul");
        }
    }

    /**
     * when the "print malumnis list" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_MalumniList_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        new MalumniSimpleDJReport(win);
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_MalumniList_ShowAll(Event event) {
        // logger.debug(event.toString());

        // empty the text search boxes
        txtb_Malumni_No.setValue(""); // clear
        txtb_Malumni_Name.setValue(""); // clear


        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Malumni> soMalumni = new HibernateSearchObject<Malumni>(Malumni.class, getMalumniListCtrl().getCountRows());
        soMalumni.addSort("mmahasiswa.cnama", false);

        // Change the BindingListModel.
        if (getMalumniListCtrl().getBinder() != null) {
            getMalumniListCtrl().getPagedBindingListWrapper().setSearchObject(soMalumni);

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_MalumniMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabMalumniList)) {
                tabMalumniList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }

    }

    /**
     * Filter the malumni list with 'like malumni number'. <br>
     */
    public void onClick$button_MalumniList_SearchNo(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Malumni_No.getValue().isEmpty()) {
            checkbox_MalumniList_ShowAll.setChecked(false); // unCheck
            txtb_Malumni_Name.setValue(""); // clear
            txtb_Malumni_City.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Malumni> soMalumni = new HibernateSearchObject<Malumni>(Malumni.class, getMalumniListCtrl().getCountRows());
            soMalumni.addFilter(new Filter("mmahasiswa.cnim", "%" + txtb_Malumni_No.getValue() + "%", Filter.OP_ILIKE));
            soMalumni.addSort("mmahasiswa.cnama", false);

            // Change the BindingListModel.
            if (getMalumniListCtrl().getBinder() != null) {
                getMalumniListCtrl().getPagedBindingListWrapper().setSearchObject(soMalumni);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_MalumniMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabMalumniList)) {
                    tabMalumniList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the malumni list with 'like malumni name'. <br>
     */
    public void onClick$button_MalumniList_SearchName(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Malumni_Name.getValue().isEmpty()) {
            checkbox_MalumniList_ShowAll.setChecked(false); // unCheck
            txtb_Malumni_City.setValue(""); // clear
            txtb_Malumni_No.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Malumni> soMalumni = new HibernateSearchObject<Malumni>(Malumni.class, getMalumniListCtrl().getCountRows());
            soMalumni.addFilter(new Filter("mmahasiswa.cnama", "%" + txtb_Malumni_Name.getValue() + "%", Filter.OP_ILIKE));
            soMalumni.addSort("mmahasiswa.cnama", false);

            // Change the BindingListModel.
            if (getMalumniListCtrl().getBinder() != null) {
                getMalumniListCtrl().getPagedBindingListWrapper().setSearchObject(soMalumni);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_MalumniMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabMalumniList)) {
                    tabMalumniList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the malumni list with 'like malumni city'. <br>
     */
    public void onClick$button_MalumniList_SearchCity(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Malumni_City.getValue().isEmpty()) {
            checkbox_MalumniList_ShowAll.setChecked(false); // unCheck
            txtb_Malumni_Name.setValue(""); // clear
            txtb_Malumni_No.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Malumni> soMalumni = new HibernateSearchObject<Malumni>(Malumni.class, getMalumniListCtrl().getCountRows());
            soMalumni.addFilter(new Filter("msekolah.cnamaSekolah", "%" + txtb_Malumni_City.getValue() + "%", Filter.OP_ILIKE));
            soMalumni.addSort("mmahasiswa.cnama", false);

            // Change the BindingListModel.
            if (getMalumniListCtrl().getBinder() != null) {
                getMalumniListCtrl().getPagedBindingListWrapper().setSearchObject(soMalumni);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_MalumniMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabMalumniList)) {
                    tabMalumniList.setSelected(true);
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
        if (getMalumniDetailCtrl().getBinder() != null) {

            // refresh all dataBinder related controllers/components
            getMalumniDetailCtrl().getBinder().loadAll();

            // set editable Mode
            getMalumniDetailCtrl().doReadOnlyMode(true);

            btnCtrlMalumni.setInitEdit();
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
        Tab currentTab = tabbox_MalumniMain.getSelectedTab();

        // check first, if the tabs are created, if not than create it
        if (getMalumniDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabMalumniDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getMalumniDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabMalumniDetail, null));
        }

        // check if the tab is one of the Detail tabs. If so do not change the
        // selection of it
        if (!currentTab.equals(tabMalumniDetail)) {
            tabMalumniDetail.setSelected(true);
        } else {
            currentTab.setSelected(true);
        }

        getMalumniDetailCtrl().getBinder().loadAll();

        // remember the old vars
        doStoreInitValues();

        btnCtrlMalumni.setBtnStatus_Edit();

        getMalumniDetailCtrl().doReadOnlyMode(false);
        // set focus
        getMalumniDetailCtrl().txtb_nim.focus();
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
        if (getMalumniDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabMalumniDetail, null));
        }

        // check first, if the tabs are created
        if (getMalumniDetailCtrl().getBinder() == null) {
            return;
        }

        final Malumni anMalumni = getSelectedMalumni();
        if (anMalumni != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anMalumni.getMmahasiswa().getCnama();
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
                        getMalumniService().delete(anMalumni);
                    } catch (DataAccessException e) {
                        ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                    }
                }
            }
            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrlMalumni.setInitEdit();

        setSelectedMalumni(null);
        // refresh the list
        getMalumniListCtrl().doFillListbox();

        // refresh all dataBinder related controllers
        getMalumniDetailCtrl().getBinder().loadAll();
    }

    /**
     * Saves all involved Beans to the DB.
     *
     * @param event
     * @throws InterruptedException
     */
    private void doSave(Event event) throws InterruptedException {
//        getMalumniDetailCtrl().getMalumni().setMmahasiswa(getMahasiswa());
        getMalumniDetailCtrl().getBinder().saveAll();
        try {

            getMalumniService().saveOrUpdate(getMalumniDetailCtrl().getMalumni());
            List<Thistkerja> hisList = getMalumniPekerjaanCtrl().getThistKerjaList(getMalumniDetailCtrl().getMalumni());
              for (int i=0; i < hisList.size();i++) {
                      getHistKerjaService().saveOrUpdate((Thistkerja) hisList.get(i));
                }

            doStoreInitValues();
            getMalumniListCtrl().doFillListbox();
            Events.postEvent("onSelect", getMalumniListCtrl().getListBoxMalumni(), getSelectedMalumni());
            String str = getSelectedMalumni().getMmahasiswa().getCnama();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));
        }
        catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
            doResetToInitValues();

            return;
        }
        finally {
            btnCtrlMalumni.setInitEdit();
            getMalumniDetailCtrl().doReadOnlyMode(true);
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
        // get the current Tab for later checking if we must change it
         Tab currentTab = tabbox_MalumniMain.getSelectedTab();

         if (!currentTab.equals(tabMalumniPekerjaan)) {

                if (getMalumniDetailCtrl() == null) {
                    Events.sendEvent(new Event("onSelect", tabMalumniDetail, null));
                    // if we work with spring beanCreation than we must check a little
                    // bit deeper, because the Controller are preCreated ?
                } else if (getMalumniDetailCtrl().getBinder() == null) {
                    Events.sendEvent(new Event("onSelect", tabMalumniDetail, null));
                }
        final Malumni anMalumni = getMalumniService().getNewMalumni();

        // set the beans in the related databinded controllers
        getMalumniDetailCtrl().setMalumni(anMalumni);
        getMalumniDetailCtrl().setSelectedMalumni(anMalumni);

        // Refresh the binding mechanism
        getMalumniDetailCtrl().setSelectedMalumni(getSelectedMalumni());
        if (getMalumniDetailCtrl().getBinder() != null) {
            getMalumniDetailCtrl().getBinder().loadAll();
        }

        getMalumniDetailCtrl().doResetCombo();
        // set editable Mode
        getMalumniDetailCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlMalumni.setInitNew();

        tabMalumniDetail.setSelected(true);
        // set focus
        getMalumniDetailCtrl().txtb_nim.focus();
         }  else {
                if (getMalumniPekerjaanCtrl() == null) {
                    Events.sendEvent(new Event("onSelect", tabMalumniPekerjaan, getMalumniDetailCtrl().getSelectedMalumni()));
                    // if we work with spring beanCreation than we must check a little
                    // bit deeper, because the Controller are preCreated ?
                } else if (getMalumniPekerjaanCtrl().getBinder() == null) {
                    Events.sendEvent(new Event("onSelect", tabMalumniPekerjaan, getMalumniDetailCtrl().getSelectedMalumni()));
                }

        final Malumni anMalumni = getMalumniService().getNewMalumni();

        // set the beans in the related databinded controllers
        getMalumniPekerjaanCtrl().setMalumni(anMalumni);
        getMalumniPekerjaanCtrl().setSelectedMalumni(anMalumni);

        // Refresh the binding mechanism
        getMalumniPekerjaanCtrl().setSelectedMalumni(getSelectedMalumni());
        if (getMalumniPekerjaanCtrl().getBinder() != null) {
            getMalumniPekerjaanCtrl().getBinder().loadAll();
        }


        // set editable Mode
//        getMalumniPekerjaanCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlMalumni.setInitNew();

       getMalumniPekerjaanCtrl().doNew();


       tabMalumniPekerjaan.setSelected(true);

         }

        // remember the current object
        doStoreInitValues();

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.


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

        if (tabbox_MalumniMain.getSelectedTab() == tabMalumniDetail) {
            getMalumniDetailCtrl().doFitSize(event);
        } else if (tabbox_MalumniMain.getSelectedTab() == tabMalumniList) {
            // resize and fill Listbox new
            getMalumniListCtrl().doFillListbox();
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

        if (getSelectedMalumni() != null) {

            try {
                setOriginalMalumni((Malumni) ZksampleBeanUtils.cloneBean(getSelectedMalumni()));
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

        if (getOriginalMalumni() != null) {

            try {
                setSelectedMalumni((Malumni) ZksampleBeanUtils.cloneBean(getOriginalMalumni()));
                // TODO Bug in DataBinder??
                windowMalumniMain.invalidate();

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

        // window_MalumniList.setVisible(workspace.isAllowed("window_MalumniList"));
        button_MalumniList_PrintList.setVisible(workspace.isAllowed("button_MalumniList_PrintList"));
        button_MalumniList_SearchNo.setVisible(workspace.isAllowed("button_MalumniList_SearchNo"));
        button_MalumniList_SearchName.setVisible(workspace.isAllowed("button_MalumniList_SearchName"));
        button_MalumniList_SearchCity.setVisible(workspace.isAllowed("button_MalumniList_SearchCity"));

        btnHelp.setVisible(workspace.isAllowed("button_MalumniMain_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_MalumniMain_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_MalumniMain_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_MalumniMain_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_MalumniMain_btnSave"));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public void setOriginalMalumni(Malumni originalMalumni) {
        this.originalMalumni = originalMalumni;
    }

    public Malumni getOriginalMalumni() {
        return this.originalMalumni;
    }

    public void setSelectedMalumni(Malumni selectedMalumni) {
        this.selectedMalumni = selectedMalumni;
    }

    public Malumni getSelectedMalumni() {
        return this.selectedMalumni;
    }

    public void setMalumnis(BindingListModelList malumnis) {
        this.malumnis = malumnis;
    }

    public BindingListModelList getMalumnis() {
        return this.malumnis;
    }

    public void setMalumniService(MalumniService malumniService) {
        this.malumniService = malumniService;
    }

    public MalumniService getMalumniService() {
        return this.malumniService;
    }

    public HistKerjaService getHistKerjaService() {
        return histKerjaService;
    }

    public void setHistKerjaService(HistKerjaService histKerjaService) {
        this.histKerjaService = histKerjaService;
    }

    public void setMalumniListCtrl(MalumniListCtrl malumniListCtrl) {
        this.malumniListCtrl = malumniListCtrl;
    }

    public MalumniListCtrl getMalumniListCtrl() {
        return this.malumniListCtrl;
    }

    public void setMalumniDetailCtrl(MalumniDetailCtrl malumniDetailCtrl) {
        this.malumniDetailCtrl = malumniDetailCtrl;
    }

    public MalumniDetailCtrl getMalumniDetailCtrl() {
        return this.malumniDetailCtrl;
    }

    public MalumniPekerjaanCtrl getMalumniPekerjaanCtrl() {
        return malumniPekerjaanCtrl;
    }

    public void setMalumniPekerjaanCtrl(MalumniPekerjaanCtrl malumniPekerjaanCtrl) {
        this.malumniPekerjaanCtrl = malumniPekerjaanCtrl;
    }

    public Mmahasiswa getMahasiswa() {
        return mahasiswa;
    }

    public void setMahasiswa(Mmahasiswa mahasiswa) {
        this.mahasiswa = mahasiswa;
    }

}
