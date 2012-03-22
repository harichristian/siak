package id.ac.idu.webui.administrasi.jenjang;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.JenjangService;
import id.ac.idu.backend.model.Mjenjang;
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
 * Time: 6:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class JenjangMainCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(JenjangMainCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowJenjangMain; // autowired

    // Tabs
    protected Tabbox tabbox_JenjangMain; // autowired
    protected Tab tabJenjangList; // autowired
    protected Tab tabJenjangDetail; // autowired
    protected Tabpanel tabPanelJenjangList; // autowired
    protected Tabpanel tabPanelJenjangDetail; // autowired

    // filter components
    protected Checkbox checkbox_JenjangList_ShowAll; // autowired
    protected Textbox txtb_Jenjang_Code; // aurowired
    protected Button button_JenjangList_SearchCode; // aurowired
    protected Textbox txtb_Jenjang_Name; // aurowired
    protected Button button_JenjangList_SearchName; // aurowired
    protected Textbox txtb_Jenjang_Singkatan; // aurowired
    protected Button button_JenjangList_SearchSingkatan; // aurowired

    // checkRights
    protected Button button_JenjangList_PrintList;

    // Button controller for the CRUD buttons
    private final String btnCtroller_ClassPrefix = "button_JenjangMain_";
    private ButtonStatusCtrl btnCtrlJenjang;
    protected Button btnNew; // autowired
    protected Button btnEdit; // autowired
    protected Button btnDelete; // autowired
    protected Button btnSave; // autowired
    protected Button btnCancel; // autowired

    protected Button btnHelp;

    // Tab-Controllers for getting the binders
    private JenjangListCtrl jenjangListCtrl;
    private JenjangDetailCtrl jenjangDetailCtrl;

    // Databinding
    private Mjenjang selectedJenjang;
    private BindingListModelList jenjangs;

    // ServiceDAOs / Domain Classes
    private JenjangService jenjangService;

    // always a copy from the bean before modifying. Used for reseting
    private Mjenjang originalJenjang;

    /**
     * default constructor.<br>
     */
    public JenjangMainCtrl() {
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
    public void onCreate$windowJenjangMain(Event event) throws Exception {
        windowJenjangMain.setContentStyle("padding:0px;");

        // create the Button Controller. Disable not used buttons during working
        btnCtrlJenjang = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

        //doCheckRights();

        /**
         * Initiate the first loading by selecting the customerList tab and
         * create the components from the zul-file.
         */
        tabJenjangList.setSelected(true);

        if (tabPanelJenjangList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelJenjangList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/jenjang/jenjangList.zul");
        }

        // init the buttons for editMode
        btnCtrlJenjang.setInitEdit();
    }

    /**
     * When the tab 'tabJenjangList' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabJenjangList(Event event) throws IOException {
        logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelJenjangList.getFirstChild() != null) {
            tabJenjangList.setSelected(true);

            return;
        }

        if (tabPanelJenjangList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelJenjangList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/jenjang/jenjangList.zul");
        }

    }

    /**
     * When the tab 'tabPanelJenjangDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws IOException
     */
    public void onSelect$tabJenjangDetail(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelJenjangDetail.getFirstChild() != null) {
            tabJenjangDetail.setSelected(true);

            // refresh the Binding mechanism
            getJenjangDetailCtrl().setJenjang(getSelectedJenjang());
            getJenjangDetailCtrl().getBinder().loadAll();
            return;
        }

        if (tabPanelJenjangDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelJenjangDetail, this, "ModuleMainController", "/WEB-INF/pages/administrasi/jenjang/jenjangDetail.zul");
        }
    }

    /**
     * when the "print jenjangs list" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_JenjangList_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        //new JenjangSimpleDJReport(win);
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_JenjangList_ShowAll(Event event) {
        // logger.debug(event.toString());

        // empty the text search boxes
        txtb_Jenjang_Code.setValue(""); // clear
        txtb_Jenjang_Name.setValue(""); // clear
        txtb_Jenjang_Singkatan.setValue(""); // clear

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Mjenjang> soJenjang = new HibernateSearchObject<Mjenjang>(Mjenjang.class, getJenjangListCtrl().getCountRows());
        soJenjang.addSort(ConstantUtil.JENJANG_NAME, false);

        // Change the BindingListModel.
        if (getJenjangListCtrl().getBinder() != null) {
            getJenjangListCtrl().getPagedBindingListWrapper().setSearchObject(soJenjang);

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_JenjangMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabJenjangList)) {
                tabJenjangList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }

    }

    /**
     * Filter the jenjang list with 'like jenjang number'. <br>
     */
    public void onClick$button_JenjangList_SearchCode(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Jenjang_Code.getValue().isEmpty()) {
            checkbox_JenjangList_ShowAll.setChecked(false); // unCheck
            txtb_Jenjang_Name.setValue(""); // clear
            txtb_Jenjang_Singkatan.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mjenjang> soJenjang = new HibernateSearchObject<Mjenjang>(Mjenjang.class, getJenjangListCtrl().getCountRows());
            soJenjang.addFilter(new Filter(ConstantUtil.JENJANG_CODE, "%" + txtb_Jenjang_Code.getValue() + "%", Filter.OP_ILIKE));
            soJenjang.addSort(ConstantUtil.JENJANG_CODE, false);

            // Change the BindingListModel.
            if (getJenjangListCtrl().getBinder() != null) {
                getJenjangListCtrl().getPagedBindingListWrapper().setSearchObject(soJenjang);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_JenjangMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabJenjangList)) {
                    tabJenjangList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the jenjang list with 'like jenjang name'. <br>
     */
    public void onClick$button_JenjangList_SearchName(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Jenjang_Name.getValue().isEmpty()) {
            checkbox_JenjangList_ShowAll.setChecked(false); // unCheck
            txtb_Jenjang_Singkatan.setValue(""); // clear
            txtb_Jenjang_Code.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mjenjang> soJenjang = new HibernateSearchObject<Mjenjang>(Mjenjang.class, getJenjangListCtrl().getCountRows());
            soJenjang.addFilter(new Filter(ConstantUtil.JENJANG_NAME, "%" + txtb_Jenjang_Name.getValue() + "%", Filter.OP_ILIKE));
            soJenjang.addSort(ConstantUtil.JENJANG_NAME, false);

            // Change the BindingListModel.
            if (getJenjangListCtrl().getBinder() != null) {
                getJenjangListCtrl().getPagedBindingListWrapper().setSearchObject(soJenjang);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_JenjangMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabJenjangList)) {
                    tabJenjangList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the jenjang list with 'like jenjang city'. <br>
     */
    public void onClick$button_JenjangList_SearchSingkatan(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Jenjang_Singkatan.getValue().isEmpty()) {
            checkbox_JenjangList_ShowAll.setChecked(false); // unCheck
            txtb_Jenjang_Name.setValue(""); // clear
            txtb_Jenjang_Code.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mjenjang> soJenjang = new HibernateSearchObject<Mjenjang>(Mjenjang.class, getJenjangListCtrl().getCountRows());
            soJenjang.addFilter(new Filter(ConstantUtil.JENJANG_SINGKATAN, "%" + txtb_Jenjang_Singkatan.getValue() + "%", Filter.OP_ILIKE));
            soJenjang.addSort(ConstantUtil.JENJANG_SINGKATAN, false);

            // Change the BindingListModel.
            if (getJenjangListCtrl().getBinder() != null) {
                getJenjangListCtrl().getPagedBindingListWrapper().setSearchObject(soJenjang);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_JenjangMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabJenjangList)) {
                    tabJenjangList.setSelected(true);
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
        if (getJenjangDetailCtrl().getBinder() != null) {

            // refresh all dataBinder related controllers/components
            getJenjangDetailCtrl().getBinder().loadAll();

            // set editable Mode
            getJenjangDetailCtrl().doReadOnlyMode(true);

            btnCtrlJenjang.setInitEdit();
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
        Tab currentTab = tabbox_JenjangMain.getSelectedTab();

        // check first, if the tabs are created, if not than create it
        if (getJenjangDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabJenjangDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getJenjangDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabJenjangDetail, null));
        }

        // check if the tab is one of the Detail tabs. If so do not change the
        // selection of it
        if (!currentTab.equals(tabJenjangDetail)) {
            tabJenjangDetail.setSelected(true);
        } else {
            currentTab.setSelected(true);
        }

        getJenjangDetailCtrl().getBinder().loadAll();

        // remember the old vars
        doStoreInitValues();

        btnCtrlJenjang.setBtnStatus_Edit();

        getJenjangDetailCtrl().doReadOnlyMode(false);
        // set focus
        getJenjangDetailCtrl().txtb_code.focus();
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
        if (getJenjangDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabJenjangDetail, null));
        }

        // check first, if the tabs are created
        if (getJenjangDetailCtrl().getBinder() == null) {
            return;
        }

        final Mjenjang anJenjang = getSelectedJenjang();
        if (anJenjang != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anJenjang.getCnmjen();
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
                        getJenjangService().delete(anJenjang);
                    } catch (DataAccessException e) {
                        ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                    }
                }

            }

            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrlJenjang.setInitEdit();

        setSelectedJenjang(null);
        // refresh the list
        getJenjangListCtrl().doFillListbox();

        // refresh all dataBinder related controllers
        getJenjangDetailCtrl().getBinder().loadAll();
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
        getJenjangDetailCtrl().getBinder().saveAll();

        try {
            // save it to database
            getJenjangService().saveOrUpdate(getJenjangDetailCtrl().getJenjang());
            // if saving is successfully than actualize the beans as
            // origins.
            doStoreInitValues();
            // refresh the list
            getJenjangListCtrl().doFillListbox();
            // later refresh StatusBar
            Events.postEvent("onSelect", getJenjangListCtrl().getListBoxJenjang(), getSelectedJenjang());

            // show the objects data in the statusBar
            String str = getSelectedJenjang().getCnmjen();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetToInitValues();

            return;

        } finally {
            btnCtrlJenjang.setInitEdit();
            getJenjangDetailCtrl().doReadOnlyMode(true);
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
        if (getJenjangDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabJenjangDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getJenjangDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabJenjangDetail, null));
        }

        // remember the current object
        doStoreInitValues();

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        final Mjenjang anJenjang = getJenjangService().getNewJenjang();

        // set the beans in the related databinded controllers
        getJenjangDetailCtrl().setJenjang(anJenjang);
        getJenjangDetailCtrl().setSelectedJenjang(anJenjang);

        // Refresh the binding mechanism
        getJenjangDetailCtrl().setSelectedJenjang(getSelectedJenjang());
        if (getJenjangDetailCtrl().getBinder() != null) {
            getJenjangDetailCtrl().getBinder().loadAll();
        }

        // set editable Mode
        getJenjangDetailCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlJenjang.setInitNew();

        tabJenjangDetail.setSelected(true);
        // set focus
        getJenjangDetailCtrl().txtb_code.focus();

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

        if (tabbox_JenjangMain.getSelectedTab() == tabJenjangDetail) {
            getJenjangDetailCtrl().doFitSize(event);
        } else if (tabbox_JenjangMain.getSelectedTab() == tabJenjangList) {
            // resize and fill Listbox new
            getJenjangListCtrl().doFillListbox();
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

        if (getSelectedJenjang() != null) {

            try {
                setOriginalJenjang((Mjenjang) ZksampleBeanUtils.cloneBean(getSelectedJenjang()));
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

        if (getOriginalJenjang() != null) {

            try {
                setSelectedJenjang((Mjenjang) ZksampleBeanUtils.cloneBean(getOriginalJenjang()));
                // TODO Bug in DataBinder??
                windowJenjangMain.invalidate();

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

        // window_JenjangList.setVisible(workspace.isAllowed("window_JenjangList"));
        button_JenjangList_PrintList.setVisible(workspace.isAllowed("button_JenjangList_PrintList"));
        button_JenjangList_SearchCode.setVisible(workspace.isAllowed("button_JenjangList_SearchCode"));
        button_JenjangList_SearchName.setVisible(workspace.isAllowed("button_JenjangList_SearchName"));
        button_JenjangList_SearchSingkatan.setVisible(workspace.isAllowed("button_JenjangList_SearchSingkatan"));

        btnHelp.setVisible(workspace.isAllowed("button_JenjangMain_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_JenjangMain_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_JenjangMain_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_JenjangMain_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_JenjangMain_btnSave"));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public void setOriginalJenjang(Mjenjang originalJenjang) {
        this.originalJenjang = originalJenjang;
    }

    public Mjenjang getOriginalJenjang() {
        return this.originalJenjang;
    }

    public void setSelectedJenjang(Mjenjang selectedJenjang) {
        this.selectedJenjang = selectedJenjang;
    }

    public Mjenjang getSelectedJenjang() {
        return this.selectedJenjang;
    }

    public void setJenjangs(BindingListModelList jenjangs) {
        this.jenjangs = jenjangs;
    }

    public BindingListModelList getJenjangs() {
        return this.jenjangs;
    }

    public void setJenjangService(JenjangService jenjangService) {
        this.jenjangService = jenjangService;
    }

    public JenjangService getJenjangService() {
        return this.jenjangService;
    }

    public void setJenjangListCtrl(JenjangListCtrl jenjangListCtrl) {
        this.jenjangListCtrl = jenjangListCtrl;
    }

    public JenjangListCtrl getJenjangListCtrl() {
        return this.jenjangListCtrl;
    }

    public void setJenjangDetailCtrl(JenjangDetailCtrl jenjangDetailCtrl) {
        this.jenjangDetailCtrl = jenjangDetailCtrl;
    }

    public JenjangDetailCtrl getJenjangDetailCtrl() {
        return this.jenjangDetailCtrl;
    }
}
