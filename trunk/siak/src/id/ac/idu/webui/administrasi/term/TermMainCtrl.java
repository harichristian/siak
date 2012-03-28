package id.ac.idu.webui.administrasi.term;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.TermService;
import id.ac.idu.backend.model.Mterm;
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
 * Date: 28/03/12
 * Time: 10:51
 * To change this template use File | Settings | File Templates.
 */
public class TermMainCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(TermMainCtrl.class);

    /*
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    * All the components that are defined here and have a corresponding
    * component with the same 'id' in the zul-file are getting autowired by our
    * 'extends GFCBaseCtrl' GenericForwardComposer.
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    */
    protected Window windowTermMain; // autowired

    // Tabs
    protected Tabbox tabbox_TermMain; // autowired
    protected Tab tabTermList; // autowired
    protected Tab tabTermDetail; // autowired
    protected Tabpanel tabPanelTermList; // autowired
    protected Tabpanel tabPanelTermDetail; // autowired

    // filter components
    protected Checkbox checkbox_TermList_ShowAll; // autowired
    protected Textbox txtb_Term_No; // aurowired
    protected Button button_TermList_SearchNo; // aurowired
    protected Textbox txtb_Term_Name; // aurowired
    protected Button button_TermList_SearchName; // aurowired

    // checkRights
    protected Button button_TermList_PrintList;

    // Button controller for the CRUD buttons
    private final String btnCtroller_ClassPrefix = "button_TermMain_";
    private ButtonStatusCtrl btnCtrlTerm;
    protected Button btnNew; // autowired
    protected Button btnEdit; // autowired
    protected Button btnDelete; // autowired
    protected Button btnSave; // autowired
    protected Button btnCancel; // autowired

    protected Button btnHelp;

    // Tab-Controllers for getting the binders
    private TermListCtrl termListCtrl;
    private TermDetailCtrl termDetailCtrl;

    // Databinding
    private Mterm selectedTerm;
    private BindingListModelList terms;

    // ServiceDAOs / Domain Classes
    private TermService termService;

    // always a copy from the bean before modifying. Used for reseting
    private Mterm originalTerm;

    /**
     * default constructor.<br>
     */
    public TermMainCtrl() {
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
    public void onCreate$windowTermMain(Event event) throws Exception {
        windowTermMain.setContentStyle("padding:0px;");

        // create the Button Controller. Disable not used buttons during working
        btnCtrlTerm = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

//            doCheckRights();

        /**
         * Initiate the first loading by selecting the customerList tab and
         * create the components from the zul-file.
         */
        tabTermList.setSelected(true);

        if (tabPanelTermList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelTermList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/term/termList.zul");
        }

        // init the buttons for editMode
        btnCtrlTerm.setInitEdit();
    }

    /**
     * When the tab 'tabTermList' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabTermList(Event event) throws IOException {
        logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelTermList.getFirstChild() != null) {
            tabTermList.setSelected(true);

            return;
        }

        if (tabPanelTermList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelTermList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/term/termList.zul");
        }

    }

    /**
     * When the tab 'tabPanelTermDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws IOException
     */
    public void onSelect$tabTermDetail(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelTermDetail.getFirstChild() != null) {
            tabTermDetail.setSelected(true);

            // refresh the Binding mechanism
            getTermDetailCtrl().setTerm(getSelectedTerm());
            getTermDetailCtrl().getBinder().loadAll();
            return;
        }

        if (tabPanelTermDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelTermDetail, this, "ModuleMainController", "/WEB-INF/pages/administrasi/term/termDetail.zul");
        }
    }

    /**
     * when the "print terms list" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_TermList_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        //new TermSimpleDJReport(win); // TODO
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_TermList_ShowAll(Event event) {
        // logger.debug(event.toString());

        // empty the text search boxes
        txtb_Term_No.setValue(""); // clear
        txtb_Term_Name.setValue(""); // clear

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Mterm> soTerm = new HibernateSearchObject<Mterm>(Mterm.class, getTermListCtrl().getCountRows());
        soTerm.addSort("kdTerm", false);

        // Change the BindingListModel.
        if (getTermListCtrl().getBinder() != null) {
            getTermListCtrl().getPagedBindingListWrapper().setSearchObject(soTerm);

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_TermMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabTermList)) {
                tabTermList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }

    }

    /**
     * Filter the term list with 'like term number'. <br>
     */
    public void onClick$button_TermList_SearchNo(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Term_No.getValue().isEmpty()) {
            checkbox_TermList_ShowAll.setChecked(false); // unCheck
            txtb_Term_Name.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mterm> soTerm = new HibernateSearchObject<Mterm>(Mterm.class, getTermListCtrl().getCountRows());
            soTerm.addFilter(new Filter("kdTerm", "%" + txtb_Term_No.getValue() + "%", Filter.OP_ILIKE));
            soTerm.addSort("kdTerm", false);

            // Change the BindingListModel.
            if (getTermListCtrl().getBinder() != null) {
                getTermListCtrl().getPagedBindingListWrapper().setSearchObject(soTerm);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_TermMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabTermList)) {
                    tabTermList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the term list with 'like term name'. <br>
     */
    public void onClick$button_TermList_SearchName(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_Term_Name.getValue().isEmpty()) {
            checkbox_TermList_ShowAll.setChecked(false); // unCheck
            txtb_Term_No.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Mterm> soTerm = new HibernateSearchObject<Mterm>(Mterm.class, getTermListCtrl().getCountRows());
            soTerm.addFilter(new Filter("deskripsi", "%" + txtb_Term_Name.getValue() + "%", Filter.OP_ILIKE));
            soTerm.addSort("deskripsi", false);

            // Change the BindingListModel.
            if (getTermListCtrl().getBinder() != null) {
                getTermListCtrl().getPagedBindingListWrapper().setSearchObject(soTerm);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_TermMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabTermList)) {
                    tabTermList.setSelected(true);
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
        if (getTermDetailCtrl().getBinder() != null) {

            // refresh all dataBinder related controllers/components
            getTermDetailCtrl().getBinder().loadAll();

            // set editable Mode
            getTermDetailCtrl().doReadOnlyMode(true);

            btnCtrlTerm.setInitEdit();
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
        Tab currentTab = tabbox_TermMain.getSelectedTab();

        // check first, if the tabs are created, if not than create it
        if (getTermDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabTermDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getTermDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabTermDetail, null));
        }

        // check if the tab is one of the Detail tabs. If so do not change the
        // selection of it
        if (!currentTab.equals(tabTermDetail)) {
            tabTermDetail.setSelected(true);
        } else {
            currentTab.setSelected(true);
        }

        getTermDetailCtrl().getBinder().loadAll();

        // remember the old vars
        doStoreInitValues();

        btnCtrlTerm.setBtnStatus_Edit();

        getTermDetailCtrl().doReadOnlyMode(false);
        // set focus
        getTermDetailCtrl().txtb_filNr.focus();
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
        if (getTermDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabTermDetail, null));
        }

        // check first, if the tabs are created
        if (getTermDetailCtrl().getBinder() == null) {
            return;
        }

        final Mterm anTerm = getSelectedTerm();
        if (anTerm != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anTerm.toString();
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
                     * Do not allow to modify the demo terms
                     */
                    if (getTermDetailCtrl().getTerm().getId() <= 2) {
                        try {
                            ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
                            return;
                        } catch (final InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        try {
                            getTermService().delete(anTerm);
                        } catch (DataAccessException e) {
                            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                        }
                    }
                }

            }

            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrlTerm.setInitEdit();

        setSelectedTerm(null);
        // refresh the list
        getTermListCtrl().doFillListbox();

        // refresh all dataBinder related controllers
        getTermDetailCtrl().getBinder().loadAll();
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
        getTermDetailCtrl().getBinder().saveAll();

        try {

            /**
             * Do not allow to modify the demo terms
             */
//                if (getTermDetailCtrl().getTerm().getId() <= 2) {
//                    ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
//
//                    doResetToInitValues();
//                    getTermDetailCtrl().getBinder().loadAll();
//                    return;
//                }

            // save it to database
            getTermService().saveOrUpdate(getTermDetailCtrl().getTerm());
            // if saving is successfully than actualize the beans as
            // origins.
            doStoreInitValues();
            // refresh the list
            getTermListCtrl().doFillListbox();
            // later refresh StatusBar
            Events.postEvent("onSelect", getTermListCtrl().getListBoxTerm(), getSelectedTerm());

            // show the objects data in the statusBar
            String str = getSelectedTerm().toString();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetToInitValues();

            return;

        } finally {
            btnCtrlTerm.setInitEdit();
            getTermDetailCtrl().doReadOnlyMode(true);
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
        if (getTermDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabTermDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getTermDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabTermDetail, null));
        }

        // remember the current object
        doStoreInitValues();

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        final Mterm anTerm = getTermService().getNewTerm();

        // set the beans in the related databinded controllers
        getTermDetailCtrl().setTerm(anTerm);
        getTermDetailCtrl().setSelectedTerm(anTerm);

        // Refresh the binding mechanism
        getTermDetailCtrl().setSelectedTerm(getSelectedTerm());
        if (getTermDetailCtrl().getBinder() != null) {
            getTermDetailCtrl().getBinder().loadAll();
        }

        // set editable Mode
        getTermDetailCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlTerm.setInitNew();

        tabTermDetail.setSelected(true);
        // set focus
        getTermDetailCtrl().txtb_filNr.focus();

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

        if (tabbox_TermMain.getSelectedTab() == tabTermDetail) {
            getTermDetailCtrl().doFitSize(event);
        } else if (tabbox_TermMain.getSelectedTab() == tabTermList) {
            // resize and fill Listbox new
            getTermListCtrl().doFillListbox();
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

        if (getSelectedTerm() != null) {

            try {
                setOriginalTerm((Mterm) ZksampleBeanUtils.cloneBean(getSelectedTerm()));
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

        if (getOriginalTerm() != null) {

            try {
                setSelectedTerm((Mterm) ZksampleBeanUtils.cloneBean(getOriginalTerm()));
                // TODO Bug in DataBinder??
                windowTermMain.invalidate();

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

        // window_TermList.setVisible(workspace.isAllowed("window_TermList"));
        button_TermList_PrintList.setVisible(workspace.isAllowed("button_TermList_PrintList"));
        button_TermList_SearchNo.setVisible(workspace.isAllowed("button_TermList_SearchNo"));
        button_TermList_SearchName.setVisible(workspace.isAllowed("button_TermList_SearchName"));


        btnHelp.setVisible(workspace.isAllowed("button_TermMain_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_TermMain_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_TermMain_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_TermMain_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_TermMain_btnSave"));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public void setOriginalTerm(Mterm originalTerm) {
        this.originalTerm = originalTerm;
    }

    public Mterm getOriginalTerm() {
        return this.originalTerm;
    }

    public void setSelectedTerm(Mterm selectedTerm) {
        this.selectedTerm = selectedTerm;
    }

    public Mterm getSelectedTerm() {
        return this.selectedTerm;
    }

    public void setTerms(BindingListModelList terms) {
        this.terms = terms;
    }

    public BindingListModelList getTerms() {
        return this.terms;
    }

    public void setTermService(TermService termService) {
        this.termService = termService;
    }

    public TermService getTermService() {
        return this.termService;
    }

    public void setTermListCtrl(TermListCtrl termListCtrl) {
        this.termListCtrl = termListCtrl;
    }

    public TermListCtrl getTermListCtrl() {
        return this.termListCtrl;
    }

    public void setTermDetailCtrl(TermDetailCtrl termDetailCtrl) {
        this.termDetailCtrl = termDetailCtrl;
    }

    public TermDetailCtrl getTermDetailCtrl() {
        return this.termDetailCtrl;
    }

}
