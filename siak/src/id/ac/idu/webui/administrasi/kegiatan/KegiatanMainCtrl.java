package id.ac.idu.webui.administrasi.kegiatan;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.KegiatanService;
import id.ac.idu.backend.model.Mkegiatan;
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
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 08/03/12
 * Time: 23:10
 * To change this template use File | Settings | File Templates.
 */
public class KegiatanMainCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(KegiatanMainCtrl.class);

    /*
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    * All the components that are defined here and have a corresponding
    * component with the same 'id' in the zul-file are getting autowired by our
    * 'extends GFCBaseCtrl' GenericForwardComposer.
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    */
    protected Window windowKegiatanMain; // autowired

    // Tabs
    protected Tabbox tabbox_KegiatanMain; // autowired
    protected Tab tabKegiatanList; // autowired
    protected Tab tabKegiatanDetail; // autowired
    protected Tabpanel tabPanelKegiatanList; // autowired
    protected Tabpanel tabPanelKegiatanDetail; // autowired

    // filter components
    protected Checkbox checkbox_KegiatanList_ShowAll; // autowired
    protected Textbox txtb_Kegiatan_No; // aurowired
    protected Button button_KegiatanList_SearchNo; // aurowired
    protected Textbox txtb_Kegiatan_Name; // aurowired
    protected Button button_KegiatanList_SearchName; // aurowired

    // checkRights
    protected Button button_KegiatanList_PrintList;

    // Button controller for the CRUD buttons
    private final String btnCtroller_ClassPrefix = "button_KegiatanMain_";
    private ButtonStatusCtrl btnCtrlKegiatan;
    protected Button btnNew; // autowired
    protected Button btnEdit; // autowired
    protected Button btnDelete; // autowired
    protected Button btnSave; // autowired
    protected Button btnCancel; // autowired

    protected Button btnHelp;

    // Tab-Controllers for getting the binders
    private KegiatanListCtrl kegiatanListCtrl;
    private KegiatanDetailCtrl kegiatanDetailCtrl;

    // Databinding
    private Mkegiatan selectedKegiatan;
    private BindingListModelList kegiatans;

    // ServiceDAOs / Domain Classes
    private KegiatanService kegiatanService;

    // always a copy from the bean before modifying. Used for reseting
    private Mkegiatan originalKegiatan;

    /**
     * default constructor.<br>
     */
    public KegiatanMainCtrl() {
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
    public void onCreate$windowKegiatanMain(Event event) throws Exception {
        windowKegiatanMain.setContentStyle("padding:0px;");

        // create the Button Controller. Disable not used buttons during working
        btnCtrlKegiatan = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

//            doCheckRights();

        /**
         * Initiate the first loading by selecting the customerList tab and
         * create the components from the zul-file.
         */
        tabKegiatanList.setSelected(true);

        if (tabPanelKegiatanList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelKegiatanList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/kegiatan/kegiatanList.zul");
        }

        // init the buttons for editMode
        btnCtrlKegiatan.setInitEdit();
    }

    /**
     * When the tab 'tabKegiatanList' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabKegiatanList(Event event) throws IOException {
        logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelKegiatanList.getFirstChild() != null) {
            tabKegiatanList.setSelected(true);

            return;
        }

        if (tabPanelKegiatanList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelKegiatanList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/kegiatan/kegiatanList.zul");
        }

    }

    /**
     * When the tab 'tabPanelKegiatanDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws IOException
     */
    public void onSelect$tabKegiatanDetail(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelKegiatanDetail.getFirstChild() != null) {
            tabKegiatanDetail.setSelected(true);

            // refresh the Binding mechanism
            getKegiatanDetailCtrl().setKegiatan(getSelectedKegiatan());
            getKegiatanDetailCtrl().getBinder().loadAll();
            return;
        }

        if (tabPanelKegiatanDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelKegiatanDetail, this, "ModuleMainController", "/WEB-INF/pages/administrasi/kegiatan/kegiatanDetail.zul");
        }
    }

    /**
     * when the "print kegiatans list" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_KegiatanList_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        //new KegiatanSimpleDJReport(win); // TODO
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_KegiatanList_ShowAll(Event event) {
        // logger.debug(event.toString());

        // empty the text search boxes
        txtb_Kegiatan_No.setValue(""); // clear
        txtb_Kegiatan_Name.setValue(""); // clear

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Mkegiatan> soKegiatan = new HibernateSearchObject<Mkegiatan>(Mkegiatan.class, getKegiatanListCtrl().getCountRows());
        soKegiatan.addSort("ckdkgt", false);

        // Change the BindingListModel.
        if (getKegiatanListCtrl().getBinder() != null) {
            getKegiatanListCtrl().getPagedBindingListWrapper().setSearchObject(soKegiatan);

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_KegiatanMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabKegiatanList)) {
                tabKegiatanList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }

    }

    /**
     * Filter the kegiatan list with 'like kegiatan number'. <br>
     */
    public void onClick$button_KegiatanList_SearchNo(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Kegiatan_No.getValue().isEmpty()) {
            checkbox_KegiatanList_ShowAll.setChecked(false); // unCheck
            txtb_Kegiatan_Name.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mkegiatan> soKegiatan = new HibernateSearchObject<Mkegiatan>(Mkegiatan.class, getKegiatanListCtrl().getCountRows());
            soKegiatan.addFilter(new Filter("ckdkgt", "%" + txtb_Kegiatan_No.getValue() + "%", Filter.OP_ILIKE));
            soKegiatan.addSort("ckdkgt", false);

            // Change the BindingListModel.
            if (getKegiatanListCtrl().getBinder() != null) {
                getKegiatanListCtrl().getPagedBindingListWrapper().setSearchObject(soKegiatan);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_KegiatanMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabKegiatanList)) {
                    tabKegiatanList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the kegiatan list with 'like kegiatan name'. <br>
     */
    public void onClick$button_KegiatanList_SearchName(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Kegiatan_Name.getValue().isEmpty()) {
            checkbox_KegiatanList_ShowAll.setChecked(false); // unCheck
            txtb_Kegiatan_No.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mkegiatan> soKegiatan = new HibernateSearchObject<Mkegiatan>(Mkegiatan.class, getKegiatanListCtrl().getCountRows());
            soKegiatan.addFilter(new Filter("cnmkgt", "%" + txtb_Kegiatan_Name.getValue() + "%", Filter.OP_ILIKE));
            soKegiatan.addSort("cnmkgt", false);

            // Change the BindingListModel.
            if (getKegiatanListCtrl().getBinder() != null) {
                getKegiatanListCtrl().getPagedBindingListWrapper().setSearchObject(soKegiatan);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_KegiatanMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabKegiatanList)) {
                    tabKegiatanList.setSelected(true);
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
        if (getKegiatanDetailCtrl().getBinder() != null) {

            // refresh all dataBinder related controllers/components
            getKegiatanDetailCtrl().getBinder().loadAll();

            // set editable Mode
            getKegiatanDetailCtrl().doReadOnlyMode(true);

            btnCtrlKegiatan.setInitEdit();
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
        Tab currentTab = tabbox_KegiatanMain.getSelectedTab();

        // check first, if the tabs are created, if not than create it
        if (getKegiatanDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabKegiatanDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getKegiatanDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabKegiatanDetail, null));
        }

        // check if the tab is one of the Detail tabs. If so do not change the
        // selection of it
        if (!currentTab.equals(tabKegiatanDetail)) {
            tabKegiatanDetail.setSelected(true);
        } else {
            currentTab.setSelected(true);
        }

        getKegiatanDetailCtrl().getBinder().loadAll();

        // remember the old vars
        doStoreInitValues();

        btnCtrlKegiatan.setBtnStatus_Edit();

        getKegiatanDetailCtrl().doReadOnlyMode(false);
        // set focus
        getKegiatanDetailCtrl().txtb_filNr.focus();
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
        if (getKegiatanDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabKegiatanDetail, null));
        }

        // check first, if the tabs are created
        if (getKegiatanDetailCtrl().getBinder() == null) {
            return;
        }

        final Mkegiatan anKegiatan = getSelectedKegiatan();
        if (anKegiatan != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anKegiatan.toString();
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
                     * Do not allow to modify the demo kegiatans
                     */
                    if (getKegiatanDetailCtrl().getKegiatan().getId() <= 2) {
                        try {
                            ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
                            return;
                        } catch (final InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        try {
                            getKegiatanService().delete(anKegiatan);
                        } catch (DataAccessException e) {
                            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                        }
                    }
                }

            }

            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrlKegiatan.setInitEdit();

        setSelectedKegiatan(null);
        // refresh the list
        getKegiatanListCtrl().doFillListbox();

        // refresh all dataBinder related controllers
        getKegiatanDetailCtrl().getBinder().loadAll();
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
        getKegiatanDetailCtrl().getBinder().saveAll();

        try {

            /**
             * Do not allow to modify the demo kegiatans
             */
//                if (getKegiatanDetailCtrl().getKegiatan().getId() <= 2) {
//                    ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
//
//                    doResetToInitValues();
//                    getKegiatanDetailCtrl().getBinder().loadAll();
//                    return;
//                }

            // save it to database
            getKegiatanService().saveOrUpdate(getKegiatanDetailCtrl().getKegiatan());
            // if saving is successfully than actualize the beans as
            // origins.
            doStoreInitValues();
            // refresh the list
            getKegiatanListCtrl().doFillListbox();
            // later refresh StatusBar
            Events.postEvent("onSelect", getKegiatanListCtrl().getListBoxKegiatan(), getSelectedKegiatan());

            // show the objects data in the statusBar
            String str = getSelectedKegiatan().toString();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetToInitValues();

            return;

        } finally {
            btnCtrlKegiatan.setInitEdit();
            getKegiatanDetailCtrl().doReadOnlyMode(true);
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
        if (getKegiatanDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabKegiatanDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getKegiatanDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabKegiatanDetail, null));
        }

        // remember the current object
        doStoreInitValues();

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        final Mkegiatan anKegiatan = getKegiatanService().getNewKegiatan();

        // set the beans in the related databinded controllers
        getKegiatanDetailCtrl().setKegiatan(anKegiatan);
        getKegiatanDetailCtrl().setSelectedKegiatan(anKegiatan);

        // Refresh the binding mechanism
        getKegiatanDetailCtrl().setSelectedKegiatan(getSelectedKegiatan());
        if (getKegiatanDetailCtrl().getBinder() != null) {
            getKegiatanDetailCtrl().getBinder().loadAll();
        }

        // set editable Mode
        getKegiatanDetailCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlKegiatan.setInitNew();

        tabKegiatanDetail.setSelected(true);
        // set focus
        getKegiatanDetailCtrl().txtb_filNr.focus();

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

        if (tabbox_KegiatanMain.getSelectedTab() == tabKegiatanDetail) {
            getKegiatanDetailCtrl().doFitSize(event);
        } else if (tabbox_KegiatanMain.getSelectedTab() == tabKegiatanList) {
            // resize and fill Listbox new
            getKegiatanListCtrl().doFillListbox();
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

        if (getSelectedKegiatan() != null) {

            try {
                setOriginalKegiatan((Mkegiatan) ZksampleBeanUtils.cloneBean(getSelectedKegiatan()));
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

        if (getOriginalKegiatan() != null) {

            try {
                setSelectedKegiatan((Mkegiatan) ZksampleBeanUtils.cloneBean(getOriginalKegiatan()));
                // TODO Bug in DataBinder??
                windowKegiatanMain.invalidate();

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

        // window_KegiatanList.setVisible(workspace.isAllowed("window_KegiatanList"));
        button_KegiatanList_PrintList.setVisible(workspace.isAllowed("button_KegiatanList_PrintList"));
        button_KegiatanList_SearchNo.setVisible(workspace.isAllowed("button_KegiatanList_SearchNo"));
        button_KegiatanList_SearchName.setVisible(workspace.isAllowed("button_KegiatanList_SearchName"));


        btnHelp.setVisible(workspace.isAllowed("button_KegiatanMain_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_KegiatanMain_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_KegiatanMain_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_KegiatanMain_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_KegiatanMain_btnSave"));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public void setOriginalKegiatan(Mkegiatan originalKegiatan) {
        this.originalKegiatan = originalKegiatan;
    }

    public Mkegiatan getOriginalKegiatan() {
        return this.originalKegiatan;
    }

    public void setSelectedKegiatan(Mkegiatan selectedKegiatan) {
        this.selectedKegiatan = selectedKegiatan;
    }

    public Mkegiatan getSelectedKegiatan() {
        return this.selectedKegiatan;
    }

    public void setKegiatans(BindingListModelList kegiatans) {
        this.kegiatans = kegiatans;
    }

    public BindingListModelList getKegiatans() {
        return this.kegiatans;
    }

    public void setKegiatanService(KegiatanService kegiatanService) {
        this.kegiatanService = kegiatanService;
    }

    public KegiatanService getKegiatanService() {
        return this.kegiatanService;
    }

    public void setKegiatanListCtrl(KegiatanListCtrl kegiatanListCtrl) {
        this.kegiatanListCtrl = kegiatanListCtrl;
    }

    public KegiatanListCtrl getKegiatanListCtrl() {
        return this.kegiatanListCtrl;
    }

    public void setKegiatanDetailCtrl(KegiatanDetailCtrl kegiatanDetailCtrl) {
        this.kegiatanDetailCtrl = kegiatanDetailCtrl;
    }

    public KegiatanDetailCtrl getKegiatanDetailCtrl() {
        return this.kegiatanDetailCtrl;
    }

}
