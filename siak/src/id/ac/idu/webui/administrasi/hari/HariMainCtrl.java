package id.ac.idu.webui.administrasi.hari;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.HariService;
import id.ac.idu.backend.model.Mhari;
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
 * Time: 7:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class HariMainCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(HariMainCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowHariMain; // autowired

    // Tabs
    protected Tabbox tabbox_HariMain; // autowired
    protected Tab tabHariList; // autowired
    protected Tab tabHariDetail; // autowired
    protected Tabpanel tabPanelHariList; // autowired
    protected Tabpanel tabPanelHariDetail; // autowired

    // filter components
    protected Checkbox checkbox_HariList_ShowAll; // autowired
    protected Textbox txtb_Hari_Code; // aurowired
    protected Button button_HariList_SearchCode; // aurowired
    protected Textbox txtb_Hari_Name; // aurowired
    protected Button button_HariList_SearchName; // aurowired

    // checkRights
    protected Button button_HariList_PrintList;

    // Button controller for the CRUD buttons
    private final String btnCtroller_ClassPrefix = "button_HariMain_";
    private ButtonStatusCtrl btnCtrlHari;
    protected Button btnNew; // autowired
    protected Button btnEdit; // autowired
    protected Button btnDelete; // autowired
    protected Button btnSave; // autowired
    protected Button btnCancel; // autowired

    protected Button btnHelp;

    // Tab-Controllers for getting the binders
    private HariListCtrl hariListCtrl;
    private HariDetailCtrl hariDetailCtrl;

    // Databinding
    private Mhari selectedHari;
    private BindingListModelList haris;

    // ServiceDAOs / Domain Classes
    private HariService hariService;

    // always a copy from the bean before modifying. Used for reseting
    private Mhari originalHari;

    /**
     * default constructor.<br>
     */
    public HariMainCtrl() {
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
    public void onCreate$windowHariMain(Event event) throws Exception {
        windowHariMain.setContentStyle("padding:0px;");

        // create the Button Controller. Disable not used buttons during working
        btnCtrlHari = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

        //doCheckRights();

        /**
         * Initiate the first loading by selecting the customerList tab and
         * create the components from the zul-file.
         */
        tabHariList.setSelected(true);

        if (tabPanelHariList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelHariList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/hari/hariList.zul");
        }

        // init the buttons for editMode
        btnCtrlHari.setInitEdit();
    }

    /**
     * When the tab 'tabHariList' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabHariList(Event event) throws IOException {
        logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelHariList.getFirstChild() != null) {
            tabHariList.setSelected(true);

            return;
        }

        if (tabPanelHariList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelHariList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/hari/hariList.zul");
        }

    }

    /**
     * When the tab 'tabPanelHariDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws IOException
     */
    public void onSelect$tabHariDetail(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelHariDetail.getFirstChild() != null) {
            tabHariDetail.setSelected(true);

            // refresh the Binding mechanism
            getHariDetailCtrl().setHari(getSelectedHari());
            getHariDetailCtrl().getBinder().loadAll();
            return;
        }

        if (tabPanelHariDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelHariDetail, this, "ModuleMainController", "/WEB-INF/pages/administrasi/hari/hariDetail.zul");
        }
    }

    /**
     * when the "print haris list" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_HariList_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        //new HariSimpleDJReport(win);
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_HariList_ShowAll(Event event) {
        // logger.debug(event.toString());

        // empty the text search boxes
        txtb_Hari_Code.setValue(""); // clear
        txtb_Hari_Name.setValue(""); // clear

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Mhari> soHari = new HibernateSearchObject<Mhari>(Mhari.class, getHariListCtrl().getCountRows());
        soHari.addSort(ConstantUtil.HARI_NAME, false);

        // Change the BindingListModel.
        if (getHariListCtrl().getBinder() != null) {
            getHariListCtrl().getPagedBindingListWrapper().setSearchObject(soHari);

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_HariMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabHariList)) {
                tabHariList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }

    }

    /**
     * Filter the hari list with 'like hari number'. <br>
     */
    public void onClick$button_HariList_SearchCode(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Hari_Code.getValue().isEmpty()) {
            checkbox_HariList_ShowAll.setChecked(false); // unCheck
            txtb_Hari_Name.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mhari> soHari = new HibernateSearchObject<Mhari>(Mhari.class, getHariListCtrl().getCountRows());
            soHari.addFilter(new Filter(ConstantUtil.HARI_CODE, "%" + txtb_Hari_Code.getValue() + "%", Filter.OP_ILIKE));
            soHari.addSort(ConstantUtil.HARI_CODE, false);

            // Change the BindingListModel.
            if (getHariListCtrl().getBinder() != null) {
                getHariListCtrl().getPagedBindingListWrapper().setSearchObject(soHari);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_HariMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabHariList)) {
                    tabHariList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the hari list with 'like hari name'. <br>
     */
    public void onClick$button_HariList_SearchName(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Hari_Name.getValue().isEmpty()) {
            checkbox_HariList_ShowAll.setChecked(false); // unCheck
            txtb_Hari_Code.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mhari> soHari = new HibernateSearchObject<Mhari>(Mhari.class, getHariListCtrl().getCountRows());
            soHari.addFilter(new Filter(ConstantUtil.HARI_NAME, "%" + txtb_Hari_Name.getValue() + "%", Filter.OP_ILIKE));
            soHari.addSort(ConstantUtil.HARI_NAME, false);

            // Change the BindingListModel.
            if (getHariListCtrl().getBinder() != null) {
                getHariListCtrl().getPagedBindingListWrapper().setSearchObject(soHari);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_HariMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabHariList)) {
                    tabHariList.setSelected(true);
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
        if (getHariDetailCtrl().getBinder() != null) {

            // refresh all dataBinder related controllers/components
            getHariDetailCtrl().getBinder().loadAll();

            // set editable Mode
            getHariDetailCtrl().doReadOnlyMode(true);

            btnCtrlHari.setInitEdit();
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
        Tab currentTab = tabbox_HariMain.getSelectedTab();

        // check first, if the tabs are created, if not than create it
        if (getHariDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabHariDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getHariDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabHariDetail, null));
        }

        // check if the tab is one of the Detail tabs. If so do not change the
        // selection of it
        if (!currentTab.equals(tabHariDetail)) {
            tabHariDetail.setSelected(true);
        } else {
            currentTab.setSelected(true);
        }

        getHariDetailCtrl().getBinder().loadAll();

        // remember the old vars
        doStoreInitValues();

        btnCtrlHari.setBtnStatus_Edit();

        getHariDetailCtrl().doReadOnlyMode(false);
        // set focus
        getHariDetailCtrl().txtb_code.focus();
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
        if (getHariDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabHariDetail, null));
        }

        // check first, if the tabs are created
        if (getHariDetailCtrl().getBinder() == null) {
            return;
        }

        final Mhari anHari = getSelectedHari();
        if (anHari != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anHari.getCnmhari();
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
                        getHariService().delete(anHari);
                    } catch (DataAccessException e) {
                        ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                    }
                }

            }

            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrlHari.setInitEdit();

        setSelectedHari(null);
        // refresh the list
        getHariListCtrl().doFillListbox();

        // refresh all dataBinder related controllers
        getHariDetailCtrl().getBinder().loadAll();
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
        getHariDetailCtrl().getBinder().saveAll();

        try {
            // save it to database
            getHariService().saveOrUpdate(getHariDetailCtrl().getHari());
            // if saving is successfully than actualize the beans as
            // origins.
            doStoreInitValues();
            // refresh the list
            getHariListCtrl().doFillListbox();
            // later refresh StatusBar
            Events.postEvent("onSelect", getHariListCtrl().getListBoxHari(), getSelectedHari());

            // show the objects data in the statusBar
            String str = getSelectedHari().getCnmhari();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetToInitValues();

            return;

        } finally {
            btnCtrlHari.setInitEdit();
            getHariDetailCtrl().doReadOnlyMode(true);
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
        if (getHariDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabHariDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getHariDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabHariDetail, null));
        }

        // remember the current object
        doStoreInitValues();

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        final Mhari anHari = getHariService().getNewHari();

        // set the beans in the related databinded controllers
        getHariDetailCtrl().setHari(anHari);
        getHariDetailCtrl().setSelectedHari(anHari);

        // Refresh the binding mechanism
        getHariDetailCtrl().setSelectedHari(getSelectedHari());
        if (getHariDetailCtrl().getBinder() != null) {
            getHariDetailCtrl().getBinder().loadAll();
        }

        // set editable Mode
        getHariDetailCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlHari.setInitNew();

        tabHariDetail.setSelected(true);
        // set focus
        getHariDetailCtrl().txtb_code.focus();

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

        if (tabbox_HariMain.getSelectedTab() == tabHariDetail) {
            getHariDetailCtrl().doFitSize(event);
        } else if (tabbox_HariMain.getSelectedTab() == tabHariList) {
            // resize and fill Listbox new
            getHariListCtrl().doFillListbox();
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

        if (getSelectedHari() != null) {

            try {
                setOriginalHari((Mhari) ZksampleBeanUtils.cloneBean(getSelectedHari()));
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

        if (getOriginalHari() != null) {

            try {
                setSelectedHari((Mhari) ZksampleBeanUtils.cloneBean(getOriginalHari()));
                // TODO Bug in DataBinder??
                windowHariMain.invalidate();

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

        // window_HariList.setVisible(workspace.isAllowed("window_HariList"));
        button_HariList_PrintList.setVisible(workspace.isAllowed("button_HariList_PrintList"));
        button_HariList_SearchCode.setVisible(workspace.isAllowed("button_HariList_SearchCode"));
        button_HariList_SearchName.setVisible(workspace.isAllowed("button_HariList_SearchName"));

        btnHelp.setVisible(workspace.isAllowed("button_HariMain_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_HariMain_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_HariMain_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_HariMain_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_HariMain_btnSave"));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public void setOriginalHari(Mhari originalHari) {
        this.originalHari = originalHari;
    }

    public Mhari getOriginalHari() {
        return this.originalHari;
    }

    public void setSelectedHari(Mhari selectedHari) {
        this.selectedHari = selectedHari;
    }

    public Mhari getSelectedHari() {
        return this.selectedHari;
    }

    public void setHaris(BindingListModelList haris) {
        this.haris = haris;
    }

    public BindingListModelList getHaris() {
        return this.haris;
    }

    public void setHariService(HariService hariService) {
        this.hariService = hariService;
    }

    public HariService getHariService() {
        return this.hariService;
    }

    public void setHariListCtrl(HariListCtrl hariListCtrl) {
        this.hariListCtrl = hariListCtrl;
    }

    public HariListCtrl getHariListCtrl() {
        return this.hariListCtrl;
    }

    public void setHariDetailCtrl(HariDetailCtrl hariDetailCtrl) {
        this.hariDetailCtrl = hariDetailCtrl;
    }

    public HariDetailCtrl getHariDetailCtrl() {
        return this.hariDetailCtrl;
    }
}
