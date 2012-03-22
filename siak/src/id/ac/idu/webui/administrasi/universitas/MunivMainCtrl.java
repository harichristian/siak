package id.ac.idu.webui.administrasi.universitas;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.MunivService;
import id.ac.idu.backend.model.Muniv;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.webui.administrasi.report.MunivSimpleDJReport;
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
 * Date: 3/6/12
 * Time: 1:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class MunivMainCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(MunivMainCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowMunivMain; // autowired

    // Tabs
    protected Tabbox tabbox_MunivMain; // autowired
    protected Tab tabMunivList; // autowired
    protected Tab tabMunivDetail; // autowired
    protected Tabpanel tabPanelMunivList; // autowired
    protected Tabpanel tabPanelMunivDetail; // autowired

    // filter components
    protected Checkbox checkbox_MunivList_ShowAll; // autowired
    protected Textbox txtb_Muniv_Code; // aurowired
    protected Button button_MunivList_SearchCode; // aurowired
    protected Textbox txtb_Muniv_Name; // aurowired
    protected Button button_MunivList_SearchName; // aurowired
    protected Textbox txtb_Muniv_Alamat; // aurowired
    protected Button button_MunivList_SearchAlamat; // aurowired

    // checkRights
    protected Button button_MunivList_PrintList;

    // Button controller for the CRUD buttons
    private final String btnCtroller_ClassPrefix = "button_MunivMain_";
    private ButtonStatusCtrl btnCtrlMuniv;
    protected Button btnNew; // autowired
    protected Button btnEdit; // autowired
    protected Button btnDelete; // autowired
    protected Button btnSave; // autowired
    protected Button btnCancel; // autowired

    protected Button btnHelp;

    // Tab-Controllers for getting the binders
    private MunivListCtrl munivListCtrl;
    private MunivDetailCtrl munivDetailCtrl;

    // Databinding
    private Muniv selectedMuniv;
    private BindingListModelList munivs;

    // ServiceDAOs / Domain Classes
    private MunivService munivService;

    // always a copy from the bean before modifying. Used for reseting
    private Muniv originalMuniv;

    /**
     * default constructor.<br>
     */
    public MunivMainCtrl() {
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
    public void onCreate$windowMunivMain(Event event) throws Exception {
        windowMunivMain.setContentStyle("padding:0px;");

        // create the Button Controller. Disable not used buttons during working
        btnCtrlMuniv = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

        //doCheckRights();

        /**
         * Initiate the first loading by selecting the customerList tab and
         * create the components from the zul-file.
         */
        tabMunivList.setSelected(true);

        if (tabPanelMunivList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelMunivList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/universitas/munivList.zul");
        }

        // init the buttons for editMode
        btnCtrlMuniv.setInitEdit();
    }

    /**
     * When the tab 'tabMunivList' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabMunivList(Event event) throws IOException {
        logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelMunivList.getFirstChild() != null) {
            tabMunivList.setSelected(true);

            return;
        }

        if (tabPanelMunivList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelMunivList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/universitas/munivList.zul");
        }

    }

    /**
     * When the tab 'tabPanelMunivDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws IOException
     */
    public void onSelect$tabMunivDetail(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelMunivDetail.getFirstChild() != null) {
            tabMunivDetail.setSelected(true);

            // refresh the Binding mechanism
            getMunivDetailCtrl().setMuniv(getSelectedMuniv());
            getMunivDetailCtrl().getBinder().loadAll();
            return;
        }

        if (tabPanelMunivDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelMunivDetail, this, "ModuleMainController", "/WEB-INF/pages/administrasi/universitas/munivDetail.zul");
        }
    }

    /**
     * when the "print munivs list" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_MunivList_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        new MunivSimpleDJReport(win);
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_MunivList_ShowAll(Event event) {
        // logger.debug(event.toString());

        // empty the text search boxes
        txtb_Muniv_Code.setValue(""); // clear
        txtb_Muniv_Name.setValue(""); // clear
        txtb_Muniv_Alamat.setValue(""); // clear

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Muniv> soMuniv = new HibernateSearchObject<Muniv>(Muniv.class, getMunivListCtrl().getCountRows());
        soMuniv.addSort("cnamaUniv", false);

        // Change the BindingListModel.
        if (getMunivListCtrl().getBinder() != null) {
            getMunivListCtrl().getPagedBindingListWrapper().setSearchObject(soMuniv);

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_MunivMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabMunivList)) {
                tabMunivList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }

    }

    /**
     * Filter the muniv list with 'like muniv number'. <br>
     */
    public void onClick$button_MunivList_SearchCode(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Muniv_Code.getValue().isEmpty()) {
            checkbox_MunivList_ShowAll.setChecked(false); // unCheck
            txtb_Muniv_Name.setValue(""); // clear
            txtb_Muniv_Alamat.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Muniv> soMuniv = new HibernateSearchObject<Muniv>(Muniv.class, getMunivListCtrl().getCountRows());
            soMuniv.addFilter(new Filter("ckdUniv", "%" + txtb_Muniv_Code.getValue() + "%", Filter.OP_ILIKE));
            soMuniv.addSort("ckdUniv", false);

            // Change the BindingListModel.
            if (getMunivListCtrl().getBinder() != null) {
                getMunivListCtrl().getPagedBindingListWrapper().setSearchObject(soMuniv);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_MunivMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabMunivList)) {
                    tabMunivList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the muniv list with 'like muniv name'. <br>
     */
    public void onClick$button_MunivList_SearchName(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Muniv_Name.getValue().isEmpty()) {
            checkbox_MunivList_ShowAll.setChecked(false); // unCheck
            txtb_Muniv_Alamat.setValue(""); // clear
            txtb_Muniv_Code.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Muniv> soMuniv = new HibernateSearchObject<Muniv>(Muniv.class, getMunivListCtrl().getCountRows());
            soMuniv.addFilter(new Filter("cnamaUniv", "%" + txtb_Muniv_Name.getValue() + "%", Filter.OP_ILIKE));
            soMuniv.addSort("cnamaUniv", false);

            // Change the BindingListModel.
            if (getMunivListCtrl().getBinder() != null) {
                getMunivListCtrl().getPagedBindingListWrapper().setSearchObject(soMuniv);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_MunivMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabMunivList)) {
                    tabMunivList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the muniv list with 'like muniv city'. <br>
     */
    public void onClick$button_MunivList_SearchAlamat(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Muniv_Alamat.getValue().isEmpty()) {
            checkbox_MunivList_ShowAll.setChecked(false); // unCheck
            txtb_Muniv_Name.setValue(""); // clear
            txtb_Muniv_Code.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Muniv> soMuniv = new HibernateSearchObject<Muniv>(Muniv.class, getMunivListCtrl().getCountRows());
            soMuniv.addFilter(new Filter("alamatUniv", "%" + txtb_Muniv_Alamat.getValue() + "%", Filter.OP_ILIKE));
            soMuniv.addSort("alamatUniv", false);

            // Change the BindingListModel.
            if (getMunivListCtrl().getBinder() != null) {
                getMunivListCtrl().getPagedBindingListWrapper().setSearchObject(soMuniv);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_MunivMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabMunivList)) {
                    tabMunivList.setSelected(true);
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
        if (getMunivDetailCtrl().getBinder() != null) {

            // refresh all dataBinder related controllers/components
            getMunivDetailCtrl().getBinder().loadAll();

            // set editable Mode
            getMunivDetailCtrl().doReadOnlyMode(true);

            btnCtrlMuniv.setInitEdit();
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
        Tab currentTab = tabbox_MunivMain.getSelectedTab();

        // check first, if the tabs are created, if not than create it
        if (getMunivDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabMunivDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getMunivDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabMunivDetail, null));
        }

        // check if the tab is one of the Detail tabs. If so do not change the
        // selection of it
        if (!currentTab.equals(tabMunivDetail)) {
            tabMunivDetail.setSelected(true);
        } else {
            currentTab.setSelected(true);
        }

        getMunivDetailCtrl().getBinder().loadAll();

        // remember the old vars
        doStoreInitValues();

        btnCtrlMuniv.setBtnStatus_Edit();

        getMunivDetailCtrl().doReadOnlyMode(false);
        // set focus
        getMunivDetailCtrl().txtb_ckdUniv.focus();
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
        if (getMunivDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabMunivDetail, null));
        }

        // check first, if the tabs are created
        if (getMunivDetailCtrl().getBinder() == null) {
            return;
        }

        final Muniv anMuniv = getSelectedMuniv();
        if (anMuniv != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anMuniv.getCnamaUniv();
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
                        getMunivService().delete(anMuniv);
                    } catch (DataAccessException e) {
                        ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                    }
                }

            }

            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrlMuniv.setInitEdit();

        setSelectedMuniv(null);
        // refresh the list
        getMunivListCtrl().doFillListbox();

        // refresh all dataBinder related controllers
        getMunivDetailCtrl().getBinder().loadAll();
    }

    /**
     * Saves all involved Beans to the DB.
     *
     * @param event
     * @throws InterruptedException
     */
    private void doSave(Event event) throws InterruptedException {
        // logger.debug(event.toString());
        if (getMunivDetailCtrl().getMuniv().getCupdatedby() == null) {
            getMunivDetailCtrl().getMuniv().setCupdatedby("1");
        }
        if (getMunivDetailCtrl().txtb_cstatus.getValue() == getMunivDetailCtrl().list_status.getSelectedItem().getValue().toString()) {
            getMunivDetailCtrl().getMuniv().setCstatus(getMunivDetailCtrl().txtb_cstatus.getValue().charAt(0));
        } else {
            getMunivDetailCtrl().txtb_cstatus.setValue(getMunivDetailCtrl().list_status.getSelectedItem().getValue().toString());
            getMunivDetailCtrl().getMuniv().setCstatus(getMunivDetailCtrl().list_status.getSelectedItem().getValue().toString().charAt(0));
        }
        // save all components data in the several tabs to the bean
        getMunivDetailCtrl().getBinder().saveAll();

        try {
            // save it to database
            getMunivService().saveOrUpdate(getMunivDetailCtrl().getMuniv());
            // if saving is successfully than actualize the beans as
            // origins.
            doStoreInitValues();
            // refresh the list
            getMunivListCtrl().doFillListbox();
            // later refresh StatusBar
            Events.postEvent("onSelect", getMunivListCtrl().getListBoxMuniv(), getSelectedMuniv());

            // show the objects data in the statusBar
            String str = getSelectedMuniv().getCnamaUniv();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetToInitValues();

            return;

        } finally {
            btnCtrlMuniv.setInitEdit();
            getMunivDetailCtrl().doReadOnlyMode(true);
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
        if (getMunivDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabMunivDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getMunivDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabMunivDetail, null));
        }

        // remember the current object
        doStoreInitValues();

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        final Muniv anMuniv = getMunivService().getNewMuniv();

        // set the beans in the related databinded controllers
        getMunivDetailCtrl().setMuniv(anMuniv);
        getMunivDetailCtrl().setSelectedMuniv(anMuniv);

        // Refresh the binding mechanism
        getMunivDetailCtrl().setSelectedMuniv(getSelectedMuniv());
        if (getMunivDetailCtrl().getBinder() != null) {
            getMunivDetailCtrl().getBinder().loadAll();
        }
        // set editable Mode
        getMunivDetailCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlMuniv.setInitNew();

        tabMunivDetail.setSelected(true);
        // set focus
        getMunivDetailCtrl().txtb_ckdUniv.focus();

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

        if (tabbox_MunivMain.getSelectedTab() == tabMunivDetail) {
            getMunivDetailCtrl().doFitSize(event);
        } else if (tabbox_MunivMain.getSelectedTab() == tabMunivList) {
            // resize and fill Listbox new
            getMunivListCtrl().doFillListbox();
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

        if (getSelectedMuniv() != null) {

            try {
                setOriginalMuniv((Muniv) ZksampleBeanUtils.cloneBean(getSelectedMuniv()));
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

        if (getOriginalMuniv() != null) {

            try {
                setSelectedMuniv((Muniv) ZksampleBeanUtils.cloneBean(getOriginalMuniv()));
                // TODO Bug in DataBinder??
                windowMunivMain.invalidate();

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

        // window_MunivList.setVisible(workspace.isAllowed("window_MunivList"));
        button_MunivList_PrintList.setVisible(workspace.isAllowed("button_MunivList_PrintList"));
        button_MunivList_SearchCode.setVisible(workspace.isAllowed("button_MunivList_SearchCode"));
        button_MunivList_SearchName.setVisible(workspace.isAllowed("button_MunivList_SearchName"));
        button_MunivList_SearchAlamat.setVisible(workspace.isAllowed("button_MunivList_SearchAlamat"));

        btnHelp.setVisible(workspace.isAllowed("button_MunivMain_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_MunivMain_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_MunivMain_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_MunivMain_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_MunivMain_btnSave"));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public void setOriginalMuniv(Muniv originalMuniv) {
        this.originalMuniv = originalMuniv;
    }

    public Muniv getOriginalMuniv() {
        return this.originalMuniv;
    }

    public void setSelectedMuniv(Muniv selectedMuniv) {
        this.selectedMuniv = selectedMuniv;
    }

    public Muniv getSelectedMuniv() {
        return this.selectedMuniv;
    }

    public void setMunivs(BindingListModelList munivs) {
        this.munivs = munivs;
    }

    public BindingListModelList getMunivs() {
        return this.munivs;
    }

    public void setMunivService(MunivService munivService) {
        this.munivService = munivService;
    }

    public MunivService getMunivService() {
        return this.munivService;
    }

    public void setMunivListCtrl(MunivListCtrl munivListCtrl) {
        this.munivListCtrl = munivListCtrl;
    }

    public MunivListCtrl getMunivListCtrl() {
        return this.munivListCtrl;
    }

    public void setMunivDetailCtrl(MunivDetailCtrl munivDetailCtrl) {
        this.munivDetailCtrl = munivDetailCtrl;
    }

    public MunivDetailCtrl getMunivDetailCtrl() {
        return this.munivDetailCtrl;
    }
}
