package id.ac.idu.webui.administrasi.prodi;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.ProdiService;
import id.ac.idu.backend.model.Mprodi;
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

//import id.ac.idu.webui.administrasi.report.ProdiSimpleDJReport;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/7/12
 * Time: 9:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProdiMainCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ProdiMainCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowProdiMain; // autowired

    // Tabs
    protected Tabbox tabbox_ProdiMain; // autowired
    protected Tab tabProdiList; // autowired
    protected Tab tabProdiDetail; // autowired
    protected Tabpanel tabPanelProdiList; // autowired
    protected Tabpanel tabPanelProdiDetail; // autowired

    // filter components
    protected Checkbox checkbox_ProdiList_ShowAll; // autowired
    protected Textbox txtb_Prodi_Code; // aurowired
    protected Button button_ProdiList_SearchCode; // aurowired
    protected Textbox txtb_Prodi_Name; // aurowired
    protected Button button_ProdiList_SearchName; // aurowired
    protected Textbox txtb_Prodi_Alamat; // aurowired
    protected Button button_ProdiList_SearchAlamat; // aurowired

    // checkRights
    protected Button button_ProdiList_PrintList;

    // Button controller for the CRUD buttons
    private final String btnCtroller_ClassPrefix = "button_ProdiMain_";
    private ButtonStatusCtrl btnCtrlProdi;
    protected Button btnNew; // autowired
    protected Button btnEdit; // autowired
    protected Button btnDelete; // autowired
    protected Button btnSave; // autowired
    protected Button btnCancel; // autowired

    protected Button btnHelp;

    // Tab-Controllers for getting the binders
    private ProdiListCtrl prodiListCtrl;
    private ProdiDetailCtrl prodiDetailCtrl;

    // Databinding
    private Mprodi selectedProdi;
    private BindingListModelList prodis;

    // ServiceDAOs / Domain Classes
    private ProdiService prodiService;
    //private JenjangService jenjangService;

    // always a copy from the bean before modifying. Used for reseting
    private Mprodi originalProdi;

    /**
     * default constructor.<br>
     */
    public ProdiMainCtrl() {
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
    public void onCreate$windowProdiMain(Event event) throws Exception {
        windowProdiMain.setContentStyle("padding:0px;");

        // create the Button Controller. Disable not used buttons during working
        btnCtrlProdi = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

        //doCheckRights();

        /**
         * Initiate the first loading by selecting the customerList tab and
         * create the components from the zul-file.
         */
        tabProdiList.setSelected(true);

        if (tabPanelProdiList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelProdiList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/prodi/prodiList.zul");
        }

        // init the buttons for editMode
        btnCtrlProdi.setInitEdit();
    }

    /**
     * When the tab 'tabProdiList' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabProdiList(Event event) throws IOException {
        logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelProdiList.getFirstChild() != null) {
            tabProdiList.setSelected(true);

            return;
        }

        if (tabPanelProdiList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelProdiList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/prodi/prodiList.zul");
        }

    }

    /**
     * When the tab 'tabPanelProdiDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws IOException
     */
    public void onSelect$tabProdiDetail(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelProdiDetail.getFirstChild() != null) {
            tabProdiDetail.setSelected(true);

            // refresh the Binding mechanism
            getProdiDetailCtrl().setProdi(getSelectedProdi());
            getProdiDetailCtrl().getBinder().loadAll();
            return;
        }

        if (tabPanelProdiDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelProdiDetail, this, "ModuleMainController", "/WEB-INF/pages/administrasi/prodi/prodiDetail.zul");
        }
    }

    /**
     * when the "print prodis list" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_ProdiList_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        //new ProdiSimpleDJReport(win);
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_ProdiList_ShowAll(Event event) {
        // logger.debug(event.toString());

        // empty the text search boxes
        txtb_Prodi_Code.setValue(""); // clear
        txtb_Prodi_Name.setValue(""); // clear
        txtb_Prodi_Alamat.setValue(""); // clear

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Mprodi> soProdi = new HibernateSearchObject<Mprodi>(Mprodi.class, getProdiListCtrl().getCountRows());
        soProdi.addSort("cnmprogst", false);

        // Change the BindingListModel.
        if (getProdiListCtrl().getBinder() != null) {
            getProdiListCtrl().getPagedBindingListWrapper().setSearchObject(soProdi);

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_ProdiMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabProdiList)) {
                tabProdiList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }

    }

    /**
     * Filter the prodi list with 'like prodi number'. <br>
     */
    public void onClick$button_ProdiList_SearchCode(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Prodi_Code.getValue().isEmpty()) {
            checkbox_ProdiList_ShowAll.setChecked(false); // unCheck
            txtb_Prodi_Name.setValue(""); // clear
            txtb_Prodi_Alamat.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mprodi> soProdi = new HibernateSearchObject<Mprodi>(Mprodi.class, getProdiListCtrl().getCountRows());
            soProdi.addFilter(new Filter("ckdprogst", "%" + txtb_Prodi_Code.getValue() + "%", Filter.OP_ILIKE));
            soProdi.addSort("ckdprogst", false);

            // Change the BindingListModel.
            if (getProdiListCtrl().getBinder() != null) {
                getProdiListCtrl().getPagedBindingListWrapper().setSearchObject(soProdi);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_ProdiMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabProdiList)) {
                    tabProdiList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the prodi list with 'like prodi name'. <br>
     */
    public void onClick$button_ProdiList_SearchName(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Prodi_Name.getValue().isEmpty()) {
            checkbox_ProdiList_ShowAll.setChecked(false); // unCheck
            txtb_Prodi_Alamat.setValue(""); // clear
            txtb_Prodi_Code.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mprodi> soProdi = new HibernateSearchObject<Mprodi>(Mprodi.class, getProdiListCtrl().getCountRows());
            soProdi.addFilter(new Filter("cnmprogst", "%" + txtb_Prodi_Name.getValue() + "%", Filter.OP_ILIKE));
            soProdi.addSort("cnmprogst", false);

            // Change the BindingListModel.
            if (getProdiListCtrl().getBinder() != null) {
                getProdiListCtrl().getPagedBindingListWrapper().setSearchObject(soProdi);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_ProdiMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabProdiList)) {
                    tabProdiList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the prodi list with 'like prodi city'. <br>
     */
    public void onClick$button_ProdiList_SearchAlamat(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Prodi_Alamat.getValue().isEmpty()) {
            checkbox_ProdiList_ShowAll.setChecked(false); // unCheck
            txtb_Prodi_Name.setValue(""); // clear
            txtb_Prodi_Code.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mprodi> soProdi = new HibernateSearchObject<Mprodi>(Mprodi.class, getProdiListCtrl().getCountRows());
            soProdi.addFilter(new Filter("csingkat", "%" + txtb_Prodi_Alamat.getValue() + "%", Filter.OP_ILIKE));
            soProdi.addSort("csingkat", false);

            // Change the BindingListModel.
            if (getProdiListCtrl().getBinder() != null) {
                getProdiListCtrl().getPagedBindingListWrapper().setSearchObject(soProdi);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_ProdiMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabProdiList)) {
                    tabProdiList.setSelected(true);
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
        if (getProdiDetailCtrl().getBinder() != null) {

            // refresh all dataBinder related controllers/components
            getProdiDetailCtrl().getBinder().loadAll();

            // set editable Mode
            getProdiDetailCtrl().doReadOnlyMode(true);

            btnCtrlProdi.setInitEdit();
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
        Tab currentTab = tabbox_ProdiMain.getSelectedTab();

        // check first, if the tabs are created, if not than create it
        if (getProdiDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabProdiDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getProdiDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabProdiDetail, null));
        }

        // check if the tab is one of the Detail tabs. If so do not change the
        // selection of it
        if (!currentTab.equals(tabProdiDetail)) {
            tabProdiDetail.setSelected(true);
        } else {
            currentTab.setSelected(true);
        }

        getProdiDetailCtrl().getBinder().loadAll();

        // remember the old vars
        doStoreInitValues();

        btnCtrlProdi.setBtnStatus_Edit();

        getProdiDetailCtrl().doReadOnlyMode(false);
        // set focus
        getProdiDetailCtrl().txtb_code.focus();
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
        if (getProdiDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabProdiDetail, null));
        }

        // check first, if the tabs are created
        if (getProdiDetailCtrl().getBinder() == null) {
            return;
        }

        final Mprodi anProdi = getSelectedProdi();
        if (anProdi != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anProdi.getCnmprogst();
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
                        getProdiService().delete(anProdi);
                    } catch (DataAccessException e) {
                        ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                    }
                }

            }

            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrlProdi.setInitEdit();

        setSelectedProdi(null);
        // refresh the list
        getProdiListCtrl().doFillListbox();

        // refresh all dataBinder related controllers
        getProdiDetailCtrl().getBinder().loadAll();
    }

    /**
     * Saves all involved Beans to the DB.
     *
     * @param event
     * @throws InterruptedException
     */
    private void doSave(Event event) throws InterruptedException {
        if (getProdiDetailCtrl().txtb_status.getValue() == getProdiDetailCtrl().list_status.getSelectedItem().getValue().toString()) {
            getProdiDetailCtrl().getProdi().setCstatus(getProdiDetailCtrl().txtb_status.getValue());
        } else {
            getProdiDetailCtrl().txtb_status.setValue(getProdiDetailCtrl().list_status.getSelectedItem().getValue().toString());
            getProdiDetailCtrl().getProdi().setCstatus(getProdiDetailCtrl().list_status.getSelectedItem().getValue().toString());
        }
        // save all components data in the several tabs to the bean
        getProdiDetailCtrl().getBinder().saveAll();

        try {
            // save it to database
            getProdiService().saveOrUpdate(getProdiDetailCtrl().getProdi());
            // if saving is successfully than actualize the beans as
            // origins.
            doStoreInitValues();
            // refresh the list
            getProdiListCtrl().doFillListbox();
            // later refresh StatusBar
            Events.postEvent("onSelect", getProdiListCtrl().getListBoxProdi(), getSelectedProdi());

            // show the objects data in the statusBar
            String str = getSelectedProdi().getCnmprogst();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetToInitValues();

            return;

        } finally {
            btnCtrlProdi.setInitEdit();
            getProdiDetailCtrl().doReadOnlyMode(true);
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
        if (getProdiDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabProdiDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getProdiDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabProdiDetail, null));
        }

        // remember the current object
        doStoreInitValues();

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        final Mprodi anProdi = getProdiService().getNewProdi();
        //final Mjenjang anJenjang = getProdiService().getAllJenjangs().get(0);
        //anProdi.setMjenjang(anJenjang);

        // set the beans in the related databinded controllers
        getProdiDetailCtrl().setProdi(anProdi);
        getProdiDetailCtrl().setSelectedProdi(anProdi);

        // Refresh the binding mechanism
        getProdiDetailCtrl().setSelectedProdi(getSelectedProdi());
        if (getProdiDetailCtrl().getBinder() != null) {
            getProdiDetailCtrl().getBinder().loadAll();
        }

        // set editable Mode
        getProdiDetailCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlProdi.setInitNew();

        tabProdiDetail.setSelected(true);
        // set focus
        getProdiDetailCtrl().txtb_code.focus();

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

        if (tabbox_ProdiMain.getSelectedTab() == tabProdiDetail) {
            getProdiDetailCtrl().doFitSize(event);
        } else if (tabbox_ProdiMain.getSelectedTab() == tabProdiList) {
            // resize and fill Listbox new
            getProdiListCtrl().doFillListbox();
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

        if (getSelectedProdi() != null) {

            try {
                setOriginalProdi((Mprodi) ZksampleBeanUtils.cloneBean(getSelectedProdi()));
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

        if (getOriginalProdi() != null) {

            try {
                setSelectedProdi((Mprodi) ZksampleBeanUtils.cloneBean(getOriginalProdi()));
                // TODO Bug in DataBinder??
                windowProdiMain.invalidate();

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

        // window_ProdiList.setVisible(workspace.isAllowed("window_ProdiList"));
        button_ProdiList_PrintList.setVisible(workspace.isAllowed("button_ProdiList_PrintList"));
        button_ProdiList_SearchCode.setVisible(workspace.isAllowed("button_ProdiList_SearchCode"));
        button_ProdiList_SearchName.setVisible(workspace.isAllowed("button_ProdiList_SearchName"));
        button_ProdiList_SearchAlamat.setVisible(workspace.isAllowed("button_ProdiList_SearchAlamat"));

        btnHelp.setVisible(workspace.isAllowed("button_ProdiMain_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_ProdiMain_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_ProdiMain_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_ProdiMain_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_ProdiMain_btnSave"));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public void setOriginalProdi(Mprodi originalProdi) {
        this.originalProdi = originalProdi;
    }

    public Mprodi getOriginalProdi() {
        return this.originalProdi;
    }

    public void setSelectedProdi(Mprodi selectedProdi) {
        this.selectedProdi = selectedProdi;
    }

    public Mprodi getSelectedProdi() {
        return this.selectedProdi;
    }

    public void setProdis(BindingListModelList prodis) {
        this.prodis = prodis;
    }

    public BindingListModelList getProdis() {
        return this.prodis;
    }

    public void setProdiService(ProdiService prodiService) {
        this.prodiService = prodiService;
    }

    public ProdiService getProdiService() {
        return this.prodiService;
    }

    public void setProdiListCtrl(ProdiListCtrl prodiListCtrl) {
        this.prodiListCtrl = prodiListCtrl;
    }

    public ProdiListCtrl getProdiListCtrl() {
        return this.prodiListCtrl;
    }

    public void setProdiDetailCtrl(ProdiDetailCtrl prodiDetailCtrl) {
        this.prodiDetailCtrl = prodiDetailCtrl;
    }

    public ProdiDetailCtrl getProdiDetailCtrl() {
        return this.prodiDetailCtrl;
    }

    /*public void setJenjangService(JenjangService jenjangService) {
		this.jenjangService = jenjangService;
	}

	public JenjangService getJenjangService() {
		return this.jenjangService;
	}*/
}
