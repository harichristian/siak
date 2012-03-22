package id.ac.idu.webui.grade;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.GradeService;
import id.ac.idu.backend.model.Mgrade;
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
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * Main controller for the grade main module.<br>
 * <br>
 * zul-file: /WEB-INF/pages/administrasi/grade/gradeMain.zul.<br>
 * This class creates the Tabs + TabPanels. The components/data to all tabs are
 * created on demand on first time selecting the tab.<br>
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * <br>
 *
 * @author Hari
 * @changes 07/04/2010: sge modified for zk5.x with complete Annotated
 * Databinding.<br>
 * <p/>
 * Managed tabs:<br>
 * - GradeListCtrl = Grade List / Filialen Liste<br>
 * - GradeDetailCtrl = Grade Details / Filiale-Details<br>
 */
public class GradeMainCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(GradeMainCtrl.class);

    /*
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    * All the components that are defined here and have a corresponding
    * component with the same 'id' in the zul-file are getting autowired by our
    * 'extends GFCBaseCtrl' GenericForwardComposer.
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    */
    protected Window windowGradeMain; // autowired

    // Tabs
    protected Tabbox tabbox_GradeMain; // autowired
    protected Tab tabGradeList; // autowired
    protected Tab tabGradeDetail; // autowired
    protected Tabpanel tabPanelGradeList; // autowired
    protected Tabpanel tabPanelGradeDetail; // autowired

    // filter components
    protected Checkbox checkbox_GradeList_ShowAll; // autowired
    protected Textbox txtb_Grade_No; // aurowired
    protected Button button_GradeList_SearchNo; // aurowired
    protected Textbox txtb_Grade_Name; // aurowired
    protected Button button_GradeList_SearchName; // aurowired

    // checkRights
    protected Button button_GradeList_PrintList;

    // Button controller for the CRUD buttons
    private final String btnCtroller_ClassPrefix = "button_GradeMain_";
    private ButtonStatusCtrl btnCtrlGrade;
    protected Button btnNew; // autowired
    protected Button btnEdit; // autowired
    protected Button btnDelete; // autowired
    protected Button btnSave; // autowired
    protected Button btnCancel; // autowired

    protected Button btnHelp;

    // Tab-Controllers for getting the binders
    private GradeListCtrl gradeListCtrl;
    private GradeDetailCtrl gradeDetailCtrl;

    // Databinding
    private Mgrade selectedGrade;
    private BindingListModelList grades;

    // ServiceDAOs / Domain Classes
    private GradeService gradeService;

    // always a copy from the bean before modifying. Used for reseting
    private Mgrade originalGrade;

    /**
     * default constructor.<br>
     */
    public GradeMainCtrl() {
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
    public void onCreate$windowGradeMain(Event event) throws Exception {
        windowGradeMain.setContentStyle("padding:0px;");

        // create the Button Controller. Disable not used buttons during working
        btnCtrlGrade = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

//                doCheckRights();

        /**
         * Initiate the first loading by selecting the customerList tab and
         * create the components from the zul-file.
         */
        tabGradeList.setSelected(true);

        if (tabPanelGradeList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelGradeList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/grade/gradeList.zul");
        }

        // init the buttons for editMode
        btnCtrlGrade.setInitEdit();
    }

    /**
     * When the tab 'tabGradeList' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabGradeList(Event event) throws IOException {
        logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelGradeList.getFirstChild() != null) {
            tabGradeList.setSelected(true);

            return;
        }

        if (tabPanelGradeList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelGradeList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/grade/gradeList.zul");
        }

    }

    /**
     * When the tab 'tabPanelGradeDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws IOException
     */
    public void onSelect$tabGradeDetail(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelGradeDetail.getFirstChild() != null) {
            tabGradeDetail.setSelected(true);

            // refresh the Binding mechanism
            getGradeDetailCtrl().setGrade(getSelectedGrade());
            getGradeDetailCtrl().getBinder().loadAll();
            return;
        }

        if (tabPanelGradeDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelGradeDetail, this, "ModuleMainController", "/WEB-INF/pages/administrasi/grade/gradeDetail.zul");
        }
    }

    /**
     * when the "print grades list" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_GradeList_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        //new GradeSimpleDJReport(win); // TODO
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_GradeList_ShowAll(Event event) {
        // logger.debug(event.toString());

        // empty the text search boxes
        txtb_Grade_No.setValue(""); // clear
        txtb_Grade_Name.setValue(""); // clear

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Mgrade> soGrade = new HibernateSearchObject<Mgrade>(Mgrade.class, getGradeListCtrl().getCountRows());
        soGrade.addSort("ckdgrade", false);

        // Change the BindingListModel.
        if (getGradeListCtrl().getBinder() != null) {
            getGradeListCtrl().getPagedBindingListWrapper().setSearchObject(soGrade);

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_GradeMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabGradeList)) {
                tabGradeList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }

    }

    /**
     * Filter the grade list with 'like grade number'. <br>
     */
    public void onClick$button_GradeList_SearchNo(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Grade_No.getValue().isEmpty()) {
            checkbox_GradeList_ShowAll.setChecked(false); // unCheck
            txtb_Grade_Name.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mgrade> soGrade = new HibernateSearchObject<Mgrade>(Mgrade.class, getGradeListCtrl().getCountRows());
            soGrade.addFilter(new Filter("ckdgrade", "%" + txtb_Grade_No.getValue() + "%", Filter.OP_ILIKE));
            soGrade.addSort("ckdgrade", false);

            // Change the BindingListModel.
            if (getGradeListCtrl().getBinder() != null) {
                getGradeListCtrl().getPagedBindingListWrapper().setSearchObject(soGrade);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_GradeMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabGradeList)) {
                    tabGradeList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the grade list with 'like grade name'. <br>
     */
    public void onClick$button_GradeList_SearchName(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Grade_Name.getValue().isEmpty()) {
            checkbox_GradeList_ShowAll.setChecked(false); // unCheck
            txtb_Grade_No.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mgrade> soGrade = new HibernateSearchObject<Mgrade>(Mgrade.class, getGradeListCtrl().getCountRows());
            soGrade.addFilter(new Filter("cgrade", "%" + txtb_Grade_Name.getValue() + "%", Filter.OP_ILIKE));
            soGrade.addSort("cgrade", false);

            // Change the BindingListModel.
            if (getGradeListCtrl().getBinder() != null) {
                getGradeListCtrl().getPagedBindingListWrapper().setSearchObject(soGrade);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_GradeMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabGradeList)) {
                    tabGradeList.setSelected(true);
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
        if (getGradeDetailCtrl().getBinder() != null) {

            // refresh all dataBinder related controllers/components
            getGradeDetailCtrl().getBinder().loadAll();

            // set editable Mode
            getGradeDetailCtrl().doReadOnlyMode(true);

            btnCtrlGrade.setInitEdit();
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
        Tab currentTab = tabbox_GradeMain.getSelectedTab();

        // check first, if the tabs are created, if not than create it
        if (getGradeDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabGradeDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getGradeDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabGradeDetail, null));
        }

        // check if the tab is one of the Detail tabs. If so do not change the
        // selection of it
        if (!currentTab.equals(tabGradeDetail)) {
            tabGradeDetail.setSelected(true);
        } else {
            currentTab.setSelected(true);
        }

        getGradeDetailCtrl().getBinder().loadAll();

        // remember the old vars
        doStoreInitValues();

        btnCtrlGrade.setBtnStatus_Edit();

        getGradeDetailCtrl().doReadOnlyMode(false);
        // set focus
        getGradeDetailCtrl().txtb_filNr.focus();
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
        if (getGradeDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabGradeDetail, null));
        }

        // check first, if the tabs are created
        if (getGradeDetailCtrl().getBinder() == null) {
            return;
        }

        final Mgrade anGrade = getSelectedGrade();
        if (anGrade != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anGrade.toString();
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
                     * Do not allow to modify the demo grades
                     */
                    if (getGradeDetailCtrl().getGrade().getId() <= 2) {
                        try {
                            ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
                            return;
                        } catch (final InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        try {
                            getGradeService().delete(anGrade);
                        } catch (DataAccessException e) {
                            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                        }
                    }
                }

            }

            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrlGrade.setInitEdit();

        setSelectedGrade(null);
        // refresh the list
        getGradeListCtrl().doFillListbox();

        // refresh all dataBinder related controllers
        getGradeDetailCtrl().getBinder().loadAll();
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
        getGradeDetailCtrl().getBinder().saveAll();

        try {

            /**
             * Do not allow to modify the demo grades
             */
//                if (getGradeDetailCtrl().getGrade().getId() <= 2) {
//                    ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
//
//                    doResetToInitValues();
//                    getGradeDetailCtrl().getBinder().loadAll();
//                    return;
//                }
            // save it to database
            getGradeService().saveOrUpdate(getGradeDetailCtrl().getGrade());
            // if saving is successfully than actualize the beans as
            // origins.
            doStoreInitValues();
            // refresh the list
            getGradeListCtrl().doFillListbox();
            // later refresh StatusBar
            Events.postEvent("onSelect", getGradeListCtrl().getListBoxGrade(), getSelectedGrade());

            // show the objects data in the statusBar
            String str = getSelectedGrade().toString();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetToInitValues();

            return;

        } finally {
            btnCtrlGrade.setInitEdit();
            getGradeDetailCtrl().doReadOnlyMode(true);
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
        if (getGradeDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabGradeDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getGradeDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabGradeDetail, null));
        }

        // remember the current object
        doStoreInitValues();

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        final Mgrade anGrade = getGradeService().getNewGrade();

        // set the beans in the related databinded controllers
        getGradeDetailCtrl().setGrade(anGrade);
        getGradeDetailCtrl().setSelectedGrade(anGrade);

        // Refresh the binding mechanism
        getGradeDetailCtrl().setSelectedGrade(getSelectedGrade());

        if (getGradeDetailCtrl().getBinder() != null) {
            getGradeDetailCtrl().getBinder().loadAll();
        }

        // set editable Mode
        getGradeDetailCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlGrade.setInitNew();

        tabGradeDetail.setSelected(true);
        // set focus
        getGradeDetailCtrl().txtb_filNr.focus();

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

        if (tabbox_GradeMain.getSelectedTab() == tabGradeDetail) {
            getGradeDetailCtrl().doFitSize(event);
        } else if (tabbox_GradeMain.getSelectedTab() == tabGradeList) {
            // resize and fill Listbox new
            getGradeListCtrl().doFillListbox();
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
        if (getSelectedGrade() != null) {
            try {
                setOriginalGrade((Mgrade) ZksampleBeanUtils.cloneBean(getSelectedGrade()));
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

        if (getOriginalGrade() != null) {

            try {
                setSelectedGrade((Mgrade) ZksampleBeanUtils.cloneBean(getOriginalGrade()));
                // TODO Bug in DataBinder??
                windowGradeMain.invalidate();

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

        // window_GradeList.setVisible(workspace.isAllowed("window_GradeList"));
        button_GradeList_PrintList.setVisible(workspace.isAllowed("button_GradeList_PrintList"));
        button_GradeList_SearchNo.setVisible(workspace.isAllowed("button_GradeList_SearchNo"));
        button_GradeList_SearchName.setVisible(workspace.isAllowed("button_GradeList_SearchName"));


        btnHelp.setVisible(workspace.isAllowed("button_GradeMain_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_GradeMain_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_GradeMain_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_GradeMain_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_GradeMain_btnSave"));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public void setOriginalGrade(Mgrade originalGrade) {
        this.originalGrade = originalGrade;
    }

    public Mgrade getOriginalGrade() {
        return this.originalGrade;
    }

    public void setSelectedGrade(Mgrade selectedGrade) {
        this.selectedGrade = selectedGrade;
    }

    public Mgrade getSelectedGrade() {
        return this.selectedGrade;
    }

    public void setGrades(BindingListModelList grades) {
        this.grades = grades;
    }

    public BindingListModelList getGrades() {
        return this.grades;
    }

    public void setGradeService(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    public GradeService getGradeService() {
        return this.gradeService;
    }

    public void setGradeListCtrl(GradeListCtrl gradeListCtrl) {
        this.gradeListCtrl = gradeListCtrl;
    }

    public GradeListCtrl getGradeListCtrl() {
        return this.gradeListCtrl;
    }

    public void setGradeDetailCtrl(GradeDetailCtrl gradeDetailCtrl) {
        this.gradeDetailCtrl = gradeDetailCtrl;
    }

    public GradeDetailCtrl getGradeDetailCtrl() {
        return this.gradeDetailCtrl;
    }

}
