package id.ac.idu.webui.administrasi.sekolah;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.SekolahService;
import id.ac.idu.backend.model.Msekolah;
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
 * Time: 0:48
 * To change this template use File | Settings | File Templates.
 */
public class SekolahMainCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(SekolahMainCtrl.class);

    /*
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    * All the components that are defined here and have a corresponding
    * component with the same 'id' in the zul-file are getting autowired by our
    * 'extends GFCBaseCtrl' GenericForwardComposer.
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    */
    protected Window windowSekolahMain; // autowired

    // Tabs
    protected Tabbox tabbox_SekolahMain; // autowired
    protected Tab tabSekolahList; // autowired
    protected Tab tabSekolahDetail; // autowired
    protected Tabpanel tabPanelSekolahList; // autowired
    protected Tabpanel tabPanelSekolahDetail; // autowired

    // filter components
    protected Checkbox checkbox_SekolahList_ShowAll; // autowired
    protected Textbox txtb_Sekolah_No; // aurowired
    protected Button button_SekolahList_SearchNo; // aurowired
    protected Textbox txtb_Sekolah_Name; // aurowired
    protected Button button_SekolahList_SearchName; // aurowired

    // checkRights
    protected Button button_SekolahList_PrintList;

    // Button controller for the CRUD buttons
    private final String btnCtroller_ClassPrefix = "button_SekolahMain_";
    private ButtonStatusCtrl btnCtrlSekolah;
    protected Button btnNew; // autowired
    protected Button btnEdit; // autowired
    protected Button btnDelete; // autowired
    protected Button btnSave; // autowired
    protected Button btnCancel; // autowired

    protected Button btnHelp;

    // Tab-Controllers for getting the binders
    private SekolahListCtrl sekolahListCtrl;
    private SekolahDetailCtrl sekolahDetailCtrl;

    // Databinding
    private Msekolah selectedSekolah;
    private BindingListModelList sekolahs;

    // ServiceDAOs / Domain Classes
    private SekolahService sekolahService;

    // always a copy from the bean before modifying. Used for reseting
    private Msekolah originalSekolah;

    /**
     * default constructor.<br>
     */
    public SekolahMainCtrl() {
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
    public void onCreate$windowSekolahMain(Event event) throws Exception {
        windowSekolahMain.setContentStyle("padding:0px;");

        // create the Button Controller. Disable not used buttons during working
        btnCtrlSekolah = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

//                doCheckRights();

        /**
         * Initiate the first loading by selecting the customerList tab and
         * create the components from the zul-file.
         */
        tabSekolahList.setSelected(true);

        if (tabPanelSekolahList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelSekolahList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/sekolah/sekolahList.zul");
        }

        // init the buttons for editMode
        btnCtrlSekolah.setInitEdit();
    }

    /**
     * When the tab 'tabSekolahList' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabSekolahList(Event event) throws IOException {
        logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelSekolahList.getFirstChild() != null) {
            tabSekolahList.setSelected(true);

            return;
        }

        if (tabPanelSekolahList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelSekolahList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/sekolah/sekolahList.zul");
        }

    }

    /**
     * When the tab 'tabPanelSekolahDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws IOException
     */
    public void onSelect$tabSekolahDetail(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelSekolahDetail.getFirstChild() != null) {
            tabSekolahDetail.setSelected(true);

            // refresh the Binding mechanism
            getSekolahDetailCtrl().setSekolah(getSelectedSekolah());
            getSekolahDetailCtrl().getBinder().loadAll();
            return;
        }

        if (tabPanelSekolahDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelSekolahDetail, this, "ModuleMainController", "/WEB-INF/pages/administrasi/sekolah/sekolahDetail.zul");
        }
    }

    /**
     * when the "print sekolahs list" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_SekolahList_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        //new SekolahSimpleDJReport(win); // TODO
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_SekolahList_ShowAll(Event event) {
        // logger.debug(event.toString());

        // empty the text search boxes
        txtb_Sekolah_No.setValue(""); // clear
        txtb_Sekolah_Name.setValue(""); // clear

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Msekolah> soSekolah = new HibernateSearchObject<Msekolah>(Msekolah.class, getSekolahListCtrl().getCountRows());
        soSekolah.addSort("ckdsekolah", false);

        // Change the BindingListModel.
        if (getSekolahListCtrl().getBinder() != null) {
            getSekolahListCtrl().getPagedBindingListWrapper().setSearchObject(soSekolah);

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_SekolahMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabSekolahList)) {
                tabSekolahList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }

    }

    /**
     * Filter the sekolah list with 'like sekolah number'. <br>
     */
    public void onClick$button_SekolahList_SearchNo(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Sekolah_No.getValue().isEmpty()) {
            checkbox_SekolahList_ShowAll.setChecked(false); // unCheck
            txtb_Sekolah_Name.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Msekolah> soSekolah = new HibernateSearchObject<Msekolah>(Msekolah.class, getSekolahListCtrl().getCountRows());
            soSekolah.addFilter(new Filter("ckdsekolah", "%" + txtb_Sekolah_No.getValue() + "%", Filter.OP_ILIKE));
            soSekolah.addSort("ckdsekolah", false);

            // Change the BindingListModel.
            if (getSekolahListCtrl().getBinder() != null) {
                getSekolahListCtrl().getPagedBindingListWrapper().setSearchObject(soSekolah);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_SekolahMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabSekolahList)) {
                    tabSekolahList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the sekolah list with 'like sekolah name'. <br>
     */
    public void onClick$button_SekolahList_SearchName(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Sekolah_Name.getValue().isEmpty()) {
            checkbox_SekolahList_ShowAll.setChecked(false); // unCheck
            txtb_Sekolah_No.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Msekolah> soSekolah = new HibernateSearchObject<Msekolah>(Msekolah.class, getSekolahListCtrl().getCountRows());
            soSekolah.addFilter(new Filter("cnamaSekolah", "%" + txtb_Sekolah_Name.getValue() + "%", Filter.OP_ILIKE));
            soSekolah.addSort("ckdsekolah", false);

            // Change the BindingListModel.
            if (getSekolahListCtrl().getBinder() != null) {
                getSekolahListCtrl().getPagedBindingListWrapper().setSearchObject(soSekolah);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_SekolahMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabSekolahList)) {
                    tabSekolahList.setSelected(true);
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
        if (getSekolahDetailCtrl().getBinder() != null) {

            // refresh all dataBinder related controllers/components
            getSekolahDetailCtrl().getBinder().loadAll();

            // set editable Mode
            getSekolahDetailCtrl().doReadOnlyMode(true);

            btnCtrlSekolah.setInitEdit();
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
        Tab currentTab = tabbox_SekolahMain.getSelectedTab();

        // check first, if the tabs are created, if not than create it
        if (getSekolahDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabSekolahDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getSekolahDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabSekolahDetail, null));
        }

        // check if the tab is one of the Detail tabs. If so do not change the
        // selection of it
        if (!currentTab.equals(tabSekolahDetail)) {
            tabSekolahDetail.setSelected(true);
        } else {
            currentTab.setSelected(true);
        }

        getSekolahDetailCtrl().getBinder().loadAll();

        // remember the old vars
        doStoreInitValues();

        btnCtrlSekolah.setBtnStatus_Edit();

        getSekolahDetailCtrl().doReadOnlyMode(false);
        // set focus
        getSekolahDetailCtrl().txtb_filNr.focus();
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
        if (getSekolahDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabSekolahDetail, null));
        }

        // check first, if the tabs are created
        if (getSekolahDetailCtrl().getBinder() == null) {
            return;
        }

        final Msekolah anSekolah = getSelectedSekolah();
        if (anSekolah != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anSekolah.toString();
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
                     * Do not allow to modify the demo sekolahs
                     */
                    if (getSekolahDetailCtrl().getSekolah().getId() <= 2) {
                        try {
                            ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
                            return;
                        } catch (final InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        try {
                            getSekolahService().delete(anSekolah);
                        } catch (DataAccessException e) {
                            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                        }
                    }
                }

            }

            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrlSekolah.setInitEdit();

        setSelectedSekolah(null);
        // refresh the list
        getSekolahListCtrl().doFillListbox();

        // refresh all dataBinder related controllers
        getSekolahDetailCtrl().getBinder().loadAll();
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
        getSekolahDetailCtrl().getBinder().saveAll();

        try {

            /**
             * Do not allow to modify the demo sekolahs
             */
//                if (getSekolahDetailCtrl().getSekolah().getId() <= 2) {
//                    ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
//
//                    doResetToInitValues();
//                    getSekolahDetailCtrl().getBinder().loadAll();
//                    return;
//                }

            // save it to database
            getSekolahService().saveOrUpdate(getSekolahDetailCtrl().getSekolah());
            // if saving is successfully than actualize the beans as
            // origins.
            doStoreInitValues();
            // refresh the list
            getSekolahListCtrl().doFillListbox();
            // later refresh StatusBar
            Events.postEvent("onSelect", getSekolahListCtrl().getListBoxSekolah(), getSelectedSekolah());

            // show the objects data in the statusBar
            String str = getSelectedSekolah().toString();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetToInitValues();

            return;

        } finally {
            btnCtrlSekolah.setInitEdit();
            getSekolahDetailCtrl().doReadOnlyMode(true);
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
        if (getSekolahDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabSekolahDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getSekolahDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabSekolahDetail, null));
        }

        // remember the current object
        doStoreInitValues();

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        final Msekolah anSekolah = getSekolahService().getNewSekolah();

        // set the beans in the related databinded controllers
        getSekolahDetailCtrl().setSekolah(anSekolah);
        getSekolahDetailCtrl().setSelectedSekolah(anSekolah);

        // Refresh the binding mechanism
        getSekolahDetailCtrl().setSelectedSekolah(getSelectedSekolah());

        if (getSekolahDetailCtrl().getBinder() != null) {
            getSekolahDetailCtrl().getBinder().loadAll();
        }

        // set editable Mode
        getSekolahDetailCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlSekolah.setInitNew();

        tabSekolahDetail.setSelected(true);
        // set focus
        getSekolahDetailCtrl().txtb_filNr.focus();

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

        if (tabbox_SekolahMain.getSelectedTab() == tabSekolahDetail) {
            getSekolahDetailCtrl().doFitSize(event);
        } else if (tabbox_SekolahMain.getSelectedTab() == tabSekolahList) {
            // resize and fill Listbox new
            getSekolahListCtrl().doFillListbox();
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

        if (getSelectedSekolah() != null) {

            try {
                setOriginalSekolah((Msekolah) ZksampleBeanUtils.cloneBean(getSelectedSekolah()));
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

        if (getOriginalSekolah() != null) {

            try {
                setSelectedSekolah((Msekolah) ZksampleBeanUtils.cloneBean(getOriginalSekolah()));
                // TODO Bug in DataBinder??
                windowSekolahMain.invalidate();

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

        // window_SekolahList.setVisible(workspace.isAllowed("window_SekolahList"));
        button_SekolahList_PrintList.setVisible(workspace.isAllowed("button_SekolahList_PrintList"));
        button_SekolahList_SearchNo.setVisible(workspace.isAllowed("button_SekolahList_SearchNo"));
        button_SekolahList_SearchName.setVisible(workspace.isAllowed("button_SekolahList_SearchName"));


        btnHelp.setVisible(workspace.isAllowed("button_SekolahMain_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_SekolahMain_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_SekolahMain_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_SekolahMain_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_SekolahMain_btnSave"));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public void setOriginalSekolah(Msekolah originalSekolah) {
        this.originalSekolah = originalSekolah;
    }

    public Msekolah getOriginalSekolah() {
        return this.originalSekolah;
    }

    public void setSelectedSekolah(Msekolah selectedSekolah) {
        this.selectedSekolah = selectedSekolah;
    }

    public Msekolah getSelectedSekolah() {
        return this.selectedSekolah;
    }

    public void setSekolahs(BindingListModelList sekolahs) {
        this.sekolahs = sekolahs;
    }

    public BindingListModelList getSekolahs() {
        return this.sekolahs;
    }

    public void setSekolahService(SekolahService sekolahService) {
        this.sekolahService = sekolahService;
    }

    public SekolahService getSekolahService() {
        return this.sekolahService;
    }

    public void setSekolahListCtrl(SekolahListCtrl sekolahListCtrl) {
        this.sekolahListCtrl = sekolahListCtrl;
    }

    public SekolahListCtrl getSekolahListCtrl() {
        return this.sekolahListCtrl;
    }

    public void setSekolahDetailCtrl(SekolahDetailCtrl sekolahDetailCtrl) {
        this.sekolahDetailCtrl = sekolahDetailCtrl;
    }

    public SekolahDetailCtrl getSekolahDetailCtrl() {
        return this.sekolahDetailCtrl;
    }

}
