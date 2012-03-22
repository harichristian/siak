package id.ac.idu.webui.administrasi.pangkat;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.PangkatService;
import id.ac.idu.backend.model.Mpangkatgolongan;
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
 * Time: 10:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class PangkatMainCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(PangkatMainCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowPangkatMain; // autowired

    // Tabs
    protected Tabbox tabbox_PangkatMain; // autowired
    protected Tab tabPangkatList; // autowired
    protected Tab tabPangkatDetail; // autowired
    protected Tabpanel tabPanelPangkatList; // autowired
    protected Tabpanel tabPanelPangkatDetail; // autowired

    // filter components
    protected Checkbox checkbox_PangkatList_ShowAll; // autowired
    protected Textbox txtb_Pangkat_Code; // aurowired
    protected Button button_PangkatList_SearchCode; // aurowired
    protected Textbox txtb_Pangkat_Name; // aurowired
    protected Button button_PangkatList_SearchName; // aurowired

    // checkRights
    protected Button button_PangkatList_PrintList;

    // Button controller for the CRUD buttons
    private final String btnCtroller_ClassPrefix = "button_PangkatMain_";
    private ButtonStatusCtrl btnCtrlPangkat;
    protected Button btnNew; // autowired
    protected Button btnEdit; // autowired
    protected Button btnDelete; // autowired
    protected Button btnSave; // autowired
    protected Button btnCancel; // autowired

    protected Button btnHelp;

    // Tab-Controllers for getting the binders
    private PangkatListCtrl pangkatListCtrl;
    private PangkatDetailCtrl pangkatDetailCtrl;

    // Databinding
    private Mpangkatgolongan selectedPangkat;
    private BindingListModelList pangkats;

    // ServiceDAOs / Domain Classes
    private PangkatService pangkatService;

    // always a copy from the bean before modifying. Used for reseting
    private Mpangkatgolongan originalPangkat;

    /**
     * default constructor.<br>
     */
    public PangkatMainCtrl() {
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
    public void onCreate$windowPangkatMain(Event event) throws Exception {
        windowPangkatMain.setContentStyle("padding:0px;");

        // create the Button Controller. Disable not used buttons during working
        btnCtrlPangkat = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

        //doCheckRights();

        /**
         * Initiate the first loading by selecting the customerList tab and
         * create the components from the zul-file.
         */
        tabPangkatList.setSelected(true);

        if (tabPanelPangkatList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelPangkatList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/pangkat/pangkatList.zul");
        }

        // init the buttons for editMode
        btnCtrlPangkat.setInitEdit();
    }

    /**
     * When the tab 'tabPangkatList' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabPangkatList(Event event) throws IOException {
        logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelPangkatList.getFirstChild() != null) {
            tabPangkatList.setSelected(true);

            return;
        }

        if (tabPanelPangkatList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelPangkatList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/pangkat/pangkatList.zul");
        }

    }

    /**
     * When the tab 'tabPanelPangkatDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws IOException
     */
    public void onSelect$tabPangkatDetail(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelPangkatDetail.getFirstChild() != null) {
            tabPangkatDetail.setSelected(true);

            // refresh the Binding mechanism
            getPangkatDetailCtrl().setPangkat(getSelectedPangkat());
            getPangkatDetailCtrl().getBinder().loadAll();
            return;
        }

        if (tabPanelPangkatDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelPangkatDetail, this, "ModuleMainController", "/WEB-INF/pages/administrasi/pangkat/pangkatDetail.zul");
        }
    }

    /**
     * when the "print pangkats list" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_PangkatList_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        //new PangkatSimpleDJReport(win);
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_PangkatList_ShowAll(Event event) {
        // logger.debug(event.toString());

        // empty the text search boxes
        txtb_Pangkat_Code.setValue(""); // clear
        txtb_Pangkat_Name.setValue(""); // clear

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Mpangkatgolongan> soPangkat = new HibernateSearchObject<Mpangkatgolongan>(Mpangkatgolongan.class, getPangkatListCtrl().getCountRows());
        soPangkat.addSort(ConstantUtil.PANGKAT_NAME, false);

        // Change the BindingListModel.
        if (getPangkatListCtrl().getBinder() != null) {
            getPangkatListCtrl().getPagedBindingListWrapper().setSearchObject(soPangkat);

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_PangkatMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabPangkatList)) {
                tabPangkatList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }

    }

    /**
     * Filter the pangkat list with 'like pangkat number'. <br>
     */
    public void onClick$button_PangkatList_SearchCode(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Pangkat_Code.getValue().isEmpty()) {
            checkbox_PangkatList_ShowAll.setChecked(false); // unCheck
            txtb_Pangkat_Name.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mpangkatgolongan> soPangkat = new HibernateSearchObject<Mpangkatgolongan>(Mpangkatgolongan.class, getPangkatListCtrl().getCountRows());
            soPangkat.addFilter(new Filter(ConstantUtil.PANGKAT_CODE, "%" + txtb_Pangkat_Code.getValue() + "%", Filter.OP_ILIKE));
            soPangkat.addSort(ConstantUtil.PANGKAT_CODE, false);

            // Change the BindingListModel.
            if (getPangkatListCtrl().getBinder() != null) {
                getPangkatListCtrl().getPagedBindingListWrapper().setSearchObject(soPangkat);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_PangkatMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabPangkatList)) {
                    tabPangkatList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the pangkat list with 'like pangkat name'. <br>
     */
    public void onClick$button_PangkatList_SearchName(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Pangkat_Name.getValue().isEmpty()) {
            checkbox_PangkatList_ShowAll.setChecked(false); // unCheck
            txtb_Pangkat_Code.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mpangkatgolongan> soPangkat = new HibernateSearchObject<Mpangkatgolongan>(Mpangkatgolongan.class, getPangkatListCtrl().getCountRows());
            soPangkat.addFilter(new Filter(ConstantUtil.PANGKAT_NAME, "%" + txtb_Pangkat_Name.getValue() + "%", Filter.OP_ILIKE));
            soPangkat.addSort(ConstantUtil.PANGKAT_NAME, false);

            // Change the BindingListModel.
            if (getPangkatListCtrl().getBinder() != null) {
                getPangkatListCtrl().getPagedBindingListWrapper().setSearchObject(soPangkat);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_PangkatMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabPangkatList)) {
                    tabPangkatList.setSelected(true);
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
        if (getPangkatDetailCtrl().getBinder() != null) {

            // refresh all dataBinder related controllers/components
            getPangkatDetailCtrl().getBinder().loadAll();

            // set editable Mode
            getPangkatDetailCtrl().doReadOnlyMode(true);

            btnCtrlPangkat.setInitEdit();
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
        Tab currentTab = tabbox_PangkatMain.getSelectedTab();

        // check first, if the tabs are created, if not than create it
        if (getPangkatDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabPangkatDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getPangkatDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabPangkatDetail, null));
        }

        // check if the tab is one of the Detail tabs. If so do not change the
        // selection of it
        if (!currentTab.equals(tabPangkatDetail)) {
            tabPangkatDetail.setSelected(true);
        } else {
            currentTab.setSelected(true);
        }

        getPangkatDetailCtrl().getBinder().loadAll();

        // remember the old vars
        doStoreInitValues();

        btnCtrlPangkat.setBtnStatus_Edit();

        getPangkatDetailCtrl().doReadOnlyMode(false);
        // set focus
        getPangkatDetailCtrl().txtb_code.focus();
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
        if (getPangkatDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabPangkatDetail, null));
        }

        // check first, if the tabs are created
        if (getPangkatDetailCtrl().getBinder() == null) {
            return;
        }

        final Mpangkatgolongan anPangkat = getSelectedPangkat();
        if (anPangkat != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anPangkat.getCnmpangkatgolongan();
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
                        getPangkatService().delete(anPangkat);
                    } catch (DataAccessException e) {
                        ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                    }
                }

            }

            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrlPangkat.setInitEdit();

        setSelectedPangkat(null);
        // refresh the list
        getPangkatListCtrl().doFillListbox();

        // refresh all dataBinder related controllers
        getPangkatDetailCtrl().getBinder().loadAll();
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
        getPangkatDetailCtrl().getBinder().saveAll();

        try {
            // save it to database
            getPangkatService().saveOrUpdate(getPangkatDetailCtrl().getPangkat());
            // if saving is successfully than actualize the beans as
            // origins.
            doStoreInitValues();
            // refresh the list
            getPangkatListCtrl().doFillListbox();
            // later refresh StatusBar
            Events.postEvent("onSelect", getPangkatListCtrl().getListBoxPangkat(), getSelectedPangkat());

            // show the objects data in the statusBar
            String str = getSelectedPangkat().getCnmpangkatgolongan();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetToInitValues();

            return;

        } finally {
            btnCtrlPangkat.setInitEdit();
            getPangkatDetailCtrl().doReadOnlyMode(true);
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
        if (getPangkatDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabPangkatDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getPangkatDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabPangkatDetail, null));
        }

        // remember the current object
        doStoreInitValues();

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        final Mpangkatgolongan anPangkat = getPangkatService().getNewPangkat();

        // set the beans in the related databinded controllers
        getPangkatDetailCtrl().setPangkat(anPangkat);
        getPangkatDetailCtrl().setSelectedPangkat(anPangkat);

        // Refresh the binding mechanism
        getPangkatDetailCtrl().setSelectedPangkat(getSelectedPangkat());
        if (getPangkatDetailCtrl().getBinder() != null) {
            getPangkatDetailCtrl().getBinder().loadAll();
        }

        // set editable Mode
        getPangkatDetailCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlPangkat.setInitNew();

        tabPangkatDetail.setSelected(true);
        // set focus
        getPangkatDetailCtrl().txtb_code.focus();

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

        if (tabbox_PangkatMain.getSelectedTab() == tabPangkatDetail) {
            getPangkatDetailCtrl().doFitSize(event);
        } else if (tabbox_PangkatMain.getSelectedTab() == tabPangkatList) {
            // resize and fill Listbox new
            getPangkatListCtrl().doFillListbox();
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

        if (getSelectedPangkat() != null) {

            try {
                setOriginalPangkat((Mpangkatgolongan) ZksampleBeanUtils.cloneBean(getSelectedPangkat()));
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

        if (getOriginalPangkat() != null) {

            try {
                setSelectedPangkat((Mpangkatgolongan) ZksampleBeanUtils.cloneBean(getOriginalPangkat()));
                // TODO Bug in DataBinder??
                windowPangkatMain.invalidate();

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

        // window_PangkatList.setVisible(workspace.isAllowed("window_PangkatList"));
        button_PangkatList_PrintList.setVisible(workspace.isAllowed("button_PangkatList_PrintList"));
        button_PangkatList_SearchCode.setVisible(workspace.isAllowed("button_PangkatList_SearchCode"));
        button_PangkatList_SearchName.setVisible(workspace.isAllowed("button_PangkatList_SearchName"));

        btnHelp.setVisible(workspace.isAllowed("button_PangkatMain_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_PangkatMain_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_PangkatMain_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_PangkatMain_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_PangkatMain_btnSave"));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public void setOriginalPangkat(Mpangkatgolongan originalPangkat) {
        this.originalPangkat = originalPangkat;
    }

    public Mpangkatgolongan getOriginalPangkat() {
        return this.originalPangkat;
    }

    public void setSelectedPangkat(Mpangkatgolongan selectedPangkat) {
        this.selectedPangkat = selectedPangkat;
    }

    public Mpangkatgolongan getSelectedPangkat() {
        return this.selectedPangkat;
    }

    public void setPangkats(BindingListModelList pangkats) {
        this.pangkats = pangkats;
    }

    public BindingListModelList getPangkats() {
        return this.pangkats;
    }

    public void setPangkatService(PangkatService pangkatService) {
        this.pangkatService = pangkatService;
    }

    public PangkatService getPangkatService() {
        return this.pangkatService;
    }

    public void setPangkatListCtrl(PangkatListCtrl pangkatListCtrl) {
        this.pangkatListCtrl = pangkatListCtrl;
    }

    public PangkatListCtrl getPangkatListCtrl() {
        return this.pangkatListCtrl;
    }

    public void setPangkatDetailCtrl(PangkatDetailCtrl pangkatDetailCtrl) {
        this.pangkatDetailCtrl = pangkatDetailCtrl;
    }

    public PangkatDetailCtrl getPangkatDetailCtrl() {
        return this.pangkatDetailCtrl;
    }
}
