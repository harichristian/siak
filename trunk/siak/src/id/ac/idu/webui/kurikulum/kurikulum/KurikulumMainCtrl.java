package id.ac.idu.webui.kurikulum.kurikulum;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.ProdiService;
import id.ac.idu.backend.model.Mkurikulum;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.kurikulum.service.DetilKurikulumService;
import id.ac.idu.kurikulum.service.KurikulumService;
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
 * Date: 3/9/12
 * Time: 3:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class KurikulumMainCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(KurikulumMainCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowKurikulumMain; // autowired

    // Tabs
    protected Tabbox tabbox_KurikulumMain; // autowired
    protected Tab tabKurikulumList; // autowired
    protected Tab tabKurikulumDetail; // autowired
    protected Tabpanel tabPanelKurikulumList; // autowired
    protected Tabpanel tabPanelKurikulumDetail; // autowired

    // filter components
    protected Checkbox checkbox_KurikulumList_ShowAll; // autowired
    protected Textbox txtb_Kurikulum_Code; // aurowired
    protected Button button_KurikulumList_SearchCode; // aurowired
    protected Textbox txtb_Kurikulum_Cohort; // aurowired
    protected Button button_KurikulumList_SearchCohort; // aurowired
    //protected Textbox txtb_Kurikulum_Prodi; // aurowired
    //protected Button button_KurikulumList_SearchProdi; // aurowired

    // checkRights
    protected Button button_KurikulumList_PrintList;

    // Button controller for the CRUD buttons
    private final String btnCtroller_ClassPrefix = "button_KurikulumMain_";
    private ButtonStatusCtrl btnCtrlKurikulum;
    protected Button btnNew; // autowired
    protected Button btnEdit; // autowired
    protected Button btnDelete; // autowired
    protected Button btnSave; // autowired
    protected Button btnCancel; // autowired

    protected Button btnHelp;

    // Tab-Controllers for getting the binders
    private KurikulumListCtrl kurikulumListCtrl;
    private KurikulumDetailCtrl kurikulumDetailCtrl;

    // Databinding
    private Mkurikulum selectedKurikulum;
    private BindingListModelList kurikulums;

    // ServiceDAOs / Domain Classes
    private KurikulumService kurikulumService;
    private ProdiService prodiService;
    private DetilKurikulumService detilKurikulumService;

    // always a copy from the bean before modifying. Used for reseting
    private Mkurikulum originalKurikulum;

    /**
     * default constructor.<br>
     */
    public KurikulumMainCtrl() {
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
    public void onCreate$windowKurikulumMain(Event event) throws Exception {
        windowKurikulumMain.setContentStyle("padding:0px;");

        // create the Button Controller. Disable not used buttons during working
        btnCtrlKurikulum = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

        //doCheckRights();

        /**
         * Initiate the first loading by selecting the customerList tab and
         * create the components from the zul-file.
         */
        tabKurikulumList.setSelected(true);

        if (tabPanelKurikulumList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelKurikulumList, this, "ModuleMainController", "/WEB-INF/pages/kurikulum/kurikulum/kurikulumList.zul");
        }

        // init the buttons for editMode
        btnCtrlKurikulum.setInitEdit();
    }

    /**
     * When the tab 'tabKurikulumList' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabKurikulumList(Event event) throws IOException {
        logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelKurikulumList.getFirstChild() != null) {
            tabKurikulumList.setSelected(true);

            return;
        }

        if (tabPanelKurikulumList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelKurikulumList, this, "ModuleMainController", "/WEB-INF/pages/kurikulum/kurikulum/kurikulumList.zul");
        }

    }

    /**
     * When the tab 'tabPanelKurikulumDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws IOException
     */
    public void onSelect$tabKurikulumDetail(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelKurikulumDetail.getFirstChild() != null) {
            tabKurikulumDetail.setSelected(true);

            // refresh the Binding mechanism
            getKurikulumDetailCtrl().setKurikulum(getSelectedKurikulum());
            getKurikulumDetailCtrl().getBinder().loadAll();
            return;
        }

        if (tabPanelKurikulumDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelKurikulumDetail, this, "ModuleMainController", "/WEB-INF/pages/kurikulum/kurikulum/kurikulumDetail.zul");
        }
    }

    /**
     * when the "print kurikulums list" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_KurikulumList_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        //new KurikulumSimpleDJReport(win);
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_KurikulumList_ShowAll(Event event) {
        // logger.debug(event.toString());

        // empty the text search boxes
        txtb_Kurikulum_Code.setValue(""); // clear
        txtb_Kurikulum_Cohort.setValue(""); // clear
        //txtb_Kurikulum_Prodi.setValue(""); // clear

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Mkurikulum> soKurikulum = new HibernateSearchObject<Mkurikulum>(Mkurikulum.class, getKurikulumListCtrl().getCountRows());
        soKurikulum.addSort(ConstantUtil.KURIKULUM_CODE, false);

        // Change the BindingListModel.
        if (getKurikulumListCtrl().getBinder() != null) {
            getKurikulumListCtrl().getPagedBindingListWrapper().setSearchObject(soKurikulum);

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_KurikulumMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabKurikulumList)) {
                tabKurikulumList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }

    }

    /**
     * Filter the kurikulum list with 'like kurikulum number'. <br>
     */
    public void onClick$button_KurikulumList_SearchCode(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Kurikulum_Code.getValue().isEmpty()) {
            checkbox_KurikulumList_ShowAll.setChecked(false); // unCheck
            txtb_Kurikulum_Cohort.setValue(""); // clear
            //txtb_Kurikulum_Prodi.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mkurikulum> soKurikulum = new HibernateSearchObject<Mkurikulum>(Mkurikulum.class, getKurikulumListCtrl().getCountRows());
            soKurikulum.addFilter(new Filter(ConstantUtil.KURIKULUM_CODE, "%" + txtb_Kurikulum_Code.getValue() + "%", Filter.OP_ILIKE));
            soKurikulum.addSort(ConstantUtil.KURIKULUM_CODE, false);

            // Change the BindingListModel.
            if (getKurikulumListCtrl().getBinder() != null) {
                getKurikulumListCtrl().getPagedBindingListWrapper().setSearchObject(soKurikulum);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_KurikulumMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabKurikulumList)) {
                    tabKurikulumList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the kurikulum list with 'like kurikulum name'. <br>
     */
    public void onClick$button_KurikulumList_SearchCohort(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Kurikulum_Cohort.getValue().isEmpty()) {
            checkbox_KurikulumList_ShowAll.setChecked(false); // unCheck
            //txtb_Kurikulum_Prodi.setValue(""); // clear
            txtb_Kurikulum_Code.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mkurikulum> soKurikulum = new HibernateSearchObject<Mkurikulum>(Mkurikulum.class, getKurikulumListCtrl().getCountRows());
            soKurikulum.addFilter(new Filter(ConstantUtil.COHORT, "%" + txtb_Kurikulum_Cohort.getValue() + "%", Filter.OP_ILIKE));
            soKurikulum.addSort(ConstantUtil.KURIKULUM_CODE, false);

            // Change the BindingListModel.
            if (getKurikulumListCtrl().getBinder() != null) {
                getKurikulumListCtrl().getPagedBindingListWrapper().setSearchObject(soKurikulum);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_KurikulumMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabKurikulumList)) {
                    tabKurikulumList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the kurikulum list with 'like kurikulum'. <br>
     */
    public void onClick$button_KurikulumList_SearchProdi(Event event) throws Exception {
        // logger.debug(event.toString());
        /* Temporarily Disabled
		// if not empty
		if (!txtb_Kurikulum_Prodi.getValue().isEmpty()) {
			checkbox_KurikulumList_ShowAll.setChecked(false); // unCheck
			txtb_Kurikulum_Cohort.setValue(""); // clear
			txtb_Kurikulum_Code.setValue(""); // clear

			// ++ create the searchObject and init sorting ++//
			HibernateSearchObject<Mkurikulum> soKurikulum = new HibernateSearchObject<Mkurikulum>(Mkurikulum.class, getKurikulumListCtrl().getCountRows());
			soKurikulum.addFilter(new Filter(Mkurikulum.PRODI, "%" + txtb_Kurikulum_Prodi.getValue() + "%", Filter.OP_ILIKE));
			soKurikulum.addSort(Mkurikulum.PRODI, false);

			// Change the BindingListModel.
			if (getKurikulumListCtrl().getBinder() != null) {
				getKurikulumListCtrl().getPagedBindingListWrapper().setSearchObject(soKurikulum);

				// get the current Tab for later checking if we must change it
				Tab currentTab = tabbox_KurikulumMain.getSelectedTab();

				// check if the tab is one of the Detail tabs. If so do not
				// change the selection of it
				if (!currentTab.equals(tabKurikulumList)) {
					tabKurikulumList.setSelected(true);
				} else {
					currentTab.setSelected(true);
				}
			}
		}
        */
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
        if (getKurikulumDetailCtrl().getBinder() != null) {

            // refresh all dataBinder related controllers/components
            getKurikulumDetailCtrl().getBinder().loadAll();

            // set editable Mode
            getKurikulumDetailCtrl().doReadOnlyMode(true);

            btnCtrlKurikulum.setInitEdit();
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
        Tab currentTab = tabbox_KurikulumMain.getSelectedTab();

        // check first, if the tabs are created, if not than create it
        if (getKurikulumDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabKurikulumDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getKurikulumDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabKurikulumDetail, null));
        }

        // check if the tab is one of the Detail tabs. If so do not change the
        // selection of it
        if (!currentTab.equals(tabKurikulumDetail)) {
            tabKurikulumDetail.setSelected(true);
        } else {
            currentTab.setSelected(true);
        }

        getKurikulumDetailCtrl().getBinder().loadAll();

        // remember the old vars
        doStoreInitValues();

        btnCtrlKurikulum.setBtnStatus_Edit();

        getKurikulumDetailCtrl().doReadOnlyMode(false);
        // set focus
        getKurikulumDetailCtrl().txtb_code.focus();
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
        if (getKurikulumDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabKurikulumDetail, null));
        }

        // check first, if the tabs are created
        if (getKurikulumDetailCtrl().getBinder() == null) {
            return;
        }

        final Mkurikulum anKurikulum = getSelectedKurikulum();
        if (anKurikulum != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anKurikulum.getCkodekur();
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
                        getKurikulumService().delete(anKurikulum);
                    } catch (DataAccessException e) {
                        ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                    }
                }

            }

            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrlKurikulum.setInitEdit();

        setSelectedKurikulum(null);
        // refresh the list
        getKurikulumListCtrl().doFillListbox();

        // refresh all dataBinder related controllers
        getKurikulumDetailCtrl().getBinder().loadAll();
    }

    /**
     * Saves all involved Beans to the DB.
     *
     * @param event
     * @throws InterruptedException
     */
    private void doSave(Event event) throws InterruptedException {
        // logger.debug(event.toString());
        /*if(getKurikulumDetailCtrl().getKurikulum().getMprodi() == null) {
            final Mprodi anProdi = getProdiService().getNewProdi();
            getKurikulumDetailCtrl().getKurikulum().setMprodi(anProdi);
        }*/
        // save all components data in the several tabs to the bean
        getKurikulumDetailCtrl().getBinder().saveAll();

        try {
            // save it to database
            getKurikulumService().saveOrUpdate(getKurikulumDetailCtrl().getKurikulum());
            // if saving is successfully than actualize the beans as
            // origins.
            doStoreInitValues();
            // refresh the list
            getKurikulumListCtrl().doFillListbox();
            // later refresh StatusBar
            Events.postEvent("onSelect", getKurikulumListCtrl().getListBoxKurikulum(), getSelectedKurikulum());

            // show the objects data in the statusBar
            String str = getSelectedKurikulum().getCkodekur();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetToInitValues();

            return;

        } finally {
            btnCtrlKurikulum.setInitEdit();
            getKurikulumDetailCtrl().doReadOnlyMode(true);
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
        if (getKurikulumDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabKurikulumDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getKurikulumDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabKurikulumDetail, null));
        }

        // remember the current object
        doStoreInitValues();

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        final Mkurikulum anKurikulum = getKurikulumService().getNewKurikulum();
        //final Mprodi anProdi = getProdiService().getAllProdis().get(0);
        //anKurikulum.setMprodi(anProdi);
        // set the beans in the related databinded controllers
        getKurikulumDetailCtrl().setKurikulum(anKurikulum);
        getKurikulumDetailCtrl().setSelectedKurikulum(anKurikulum);

        // Refresh the binding mechanism
        getKurikulumDetailCtrl().setSelectedKurikulum(getSelectedKurikulum());
        if (getKurikulumDetailCtrl().getBinder() != null) {
            getKurikulumDetailCtrl().getBinder().loadAll();
        }

        // set editable Mode
        getKurikulumDetailCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlKurikulum.setInitNew();

        tabKurikulumDetail.setSelected(true);
        // set focus
        getKurikulumDetailCtrl().txtb_code.focus();

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

        if (tabbox_KurikulumMain.getSelectedTab() == tabKurikulumDetail) {
            getKurikulumDetailCtrl().doFitSize(event);
        } else if (tabbox_KurikulumMain.getSelectedTab() == tabKurikulumList) {
            // resize and fill Listbox new
            getKurikulumListCtrl().doFillListbox();
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

        if (getSelectedKurikulum() != null) {

            try {
                setOriginalKurikulum((Mkurikulum) ZksampleBeanUtils.cloneBean(getSelectedKurikulum()));
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

        if (getOriginalKurikulum() != null) {

            try {
                setSelectedKurikulum((Mkurikulum) ZksampleBeanUtils.cloneBean(getOriginalKurikulum()));
                // TODO Bug in DataBinder??
                windowKurikulumMain.invalidate();

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

        // window_KurikulumList.setVisible(workspace.isAllowed("window_KurikulumList"));
        button_KurikulumList_PrintList.setVisible(workspace.isAllowed("button_KurikulumList_PrintList"));
        button_KurikulumList_SearchCode.setVisible(workspace.isAllowed("button_KurikulumList_SearchCode"));
        button_KurikulumList_SearchCohort.setVisible(workspace.isAllowed("button_KurikulumList_SearchName"));
        //button_KurikulumList_SearchProdi.setVisible(workspace.isAllowed("button_KurikulumList_SearchProdi"));

        btnHelp.setVisible(workspace.isAllowed("button_KurikulumMain_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_KurikulumMain_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_KurikulumMain_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_KurikulumMain_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_KurikulumMain_btnSave"));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public void setOriginalKurikulum(Mkurikulum originalKurikulum) {
        this.originalKurikulum = originalKurikulum;
    }

    public Mkurikulum getOriginalKurikulum() {
        return this.originalKurikulum;
    }

    public void setSelectedKurikulum(Mkurikulum selectedKurikulum) {
        this.selectedKurikulum = selectedKurikulum;
    }

    public Mkurikulum getSelectedKurikulum() {
        return this.selectedKurikulum;
    }

    public void setKurikulums(BindingListModelList kurikulums) {
        this.kurikulums = kurikulums;
    }

    public BindingListModelList getKurikulums() {
        return this.kurikulums;
    }

    public void setKurikulumService(KurikulumService kurikulumService) {
        this.kurikulumService = kurikulumService;
    }

    public KurikulumService getKurikulumService() {
        return this.kurikulumService;
    }

    public void setKurikulumListCtrl(KurikulumListCtrl kurikulumListCtrl) {
        this.kurikulumListCtrl = kurikulumListCtrl;
    }

    public KurikulumListCtrl getKurikulumListCtrl() {
        return this.kurikulumListCtrl;
    }

    public void setKurikulumDetailCtrl(KurikulumDetailCtrl kurikulumDetailCtrl) {
        this.kurikulumDetailCtrl = kurikulumDetailCtrl;
    }

    public KurikulumDetailCtrl getKurikulumDetailCtrl() {
        return this.kurikulumDetailCtrl;
    }


    public void setProdiService(ProdiService prodiService) {
        this.prodiService = prodiService;
    }

    public ProdiService getProdiService() {
        return this.prodiService;
    }

    public DetilKurikulumService getDetilKurikulumService() {
        return detilKurikulumService;
    }

    public void setDetilKurikulumService(DetilKurikulumService detilKurikulumService) {
        this.detilKurikulumService = detilKurikulumService;
    }
}
