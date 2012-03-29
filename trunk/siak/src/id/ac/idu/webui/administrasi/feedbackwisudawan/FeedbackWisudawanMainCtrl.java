package id.ac.idu.webui.administrasi.feedbackwisudawan;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.FeedbackWisudawanService;
import id.ac.idu.backend.model.Mfeedback;
import id.ac.idu.backend.model.Tfeedbackwisudawan;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.util.ConstantUtil;
import id.ac.idu.webui.administrasi.report.FeedbackWisudawanSimpleDJReport;
import id.ac.idu.webui.util.*;
import id.ac.idu.webui.util.pagging.PagedBindingListWrapper;
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
import java.util.List;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 3/21/12
 * Time: 4:07 AM
 */
public class FeedbackWisudawanMainCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(FeedbackWisudawanMainCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowFeedbackWisudawanMain; // autowired

    // Tabs
    protected Tabbox tabbox_FeedbackWisudawanMain; // autowired
    protected Tab tabFeedbackWisudawanList; // autowired
    protected Tab tabFeedbackWisudawanDetail; // autowired
    protected Tabpanel tabPanelFeedbackWisudawanList; // autowired
    protected Tabpanel tabPanelFeedbackWisudawanDetail; // autowired

    // filter components
    protected Checkbox checkbox_FeedbackWisudawanList_ShowAll; // autowired
    protected Textbox txtb_FeedbackWisudawan_Term; // aurowired
    protected Button button_FeedbackWisudawanList_SearchTerm; // aurowired
    protected Textbox txtb_FeedbackWisudawan_Kelompok; // aurowired
    protected Button button_FeedbackWisudawanList_SearchKelompok; // aurowired
    protected Textbox txtb_FeedbackWisudawan_Name; // aurowired
    protected Button button_FeedbackWisudawanList_SearchName; // aurowired

    // checkRights
    protected Button button_FeedbackWisudawanList_PrintList;

    // Button controller for the CRUD buttons
    private final String btnCtroller_ClassPrefix = "button_FeedbackWisudawanMain_";
    private ButtonStatusCtrl btnCtrlFeedbackWisudawan;
    protected Button btnNew; // autowired
    protected Button btnEdit; // autowired
    protected Button btnDelete; // autowired
    protected Button btnSave; // autowired
    protected Button btnCancel; // autowired

    protected Button btnHelp;

    // Tab-Controllers for getting the binders
    private FeedbackWisudawanListCtrl feedbackWisudawanListCtrl;
    private FeedbackWisudawanDetailCtrl feedbackWisudawanDetailCtrl;

    // Databinding
    private Tfeedbackwisudawan selectedFeedbackWisudawan;
    private List<Tfeedbackwisudawan> selectedFeedbackWisudawanList;
    private BindingListModelList feedbackWisudawans;
    private Mfeedback selectedFeedback;
    private BindingListModelList feedbacks;

    // ServiceDAOs / Domain Classes
    private FeedbackWisudawanService feedbackWisudawanService;

    // always a copy from the bean before modifying. Used for reseting
    private Tfeedbackwisudawan originalFeedbackWisudawan;

    /**
     * default constructor.<br>
     */
    public FeedbackWisudawanMainCtrl() {
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
    public void onCreate$windowFeedbackWisudawanMain(Event event) throws Exception {
        windowFeedbackWisudawanMain.setContentStyle("padding:0px;");

        // create the Button Controller. Disable not used buttons during working
        btnCtrlFeedbackWisudawan = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

        //doCheckRights();

        /**
         * Initiate the first loading by selecting the customerList tab and
         * create the components from the zul-file.
         */
        tabFeedbackWisudawanList.setSelected(true);

        if (tabPanelFeedbackWisudawanList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelFeedbackWisudawanList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/feedbackwisudawan/feedbackWisudawanList.zul");
        }

        // init the buttons for editMode
        btnCtrlFeedbackWisudawan.setInitEdit();
    }

    /**
     * When the tab 'tabFeedbackWisudawanList' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabFeedbackWisudawanList(Event event) throws IOException {
        logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelFeedbackWisudawanList.getFirstChild() != null) {
            tabFeedbackWisudawanList.setSelected(true);

            return;
        }

        if (tabPanelFeedbackWisudawanList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelFeedbackWisudawanList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/feedbackwisudawan/feedbackWisudawanList.zul");
        }

    }

    /**
     * When the tab 'tabPanelFeedbackWisudawanDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabFeedbackWisudawanDetail(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelFeedbackWisudawanDetail.getFirstChild() != null) {
            tabFeedbackWisudawanDetail.setSelected(true);

            // refresh the Binding mechanism
            getFeedbackWisudawanDetailCtrl().setFeedbackWisudawan(getSelectedFeedbackWisudawan());
            getFeedbackWisudawanDetailCtrl().getBinder().loadAll();
            return;
        }

        if (tabPanelFeedbackWisudawanDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelFeedbackWisudawanDetail, this, "ModuleMainController", "/WEB-INF/pages/administrasi/feedbackwisudawan/feedbackWisudawanDetail.zul");
        }
    }

    /**
     * when the "print feedbackWisudawans list" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_FeedbackWisudawanList_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        new FeedbackWisudawanSimpleDJReport(win);
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_FeedbackWisudawanList_ShowAll(Event event) {
        // logger.debug(event.toString());

        // empty the text search boxes
        txtb_FeedbackWisudawan_Term.setValue(""); // clear
        txtb_FeedbackWisudawan_Name.setValue(""); // clear
        txtb_FeedbackWisudawan_Kelompok.setValue(""); // clear

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Tfeedbackwisudawan> soFeedbackWisudawan = new HibernateSearchObject<Tfeedbackwisudawan>(Tfeedbackwisudawan.class, getFeedbackWisudawanListCtrl().getCountRows());
        soFeedbackWisudawan.addSort(ConstantUtil.TERM, false);

        // Change the BindingListModel.
        if (getFeedbackWisudawanListCtrl().getBinder() != null) {
            getFeedbackWisudawanListCtrl().getPagedBindingListWrapper().setSearchObject(soFeedbackWisudawan);

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_FeedbackWisudawanMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabFeedbackWisudawanList)) {
                tabFeedbackWisudawanList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }

    }

    /**
     * Filter the feedbackWisudawan list with 'like feedbackWisudawan number'. <br>
     */
    public void onClick$button_FeedbackWisudawanList_SearchTerm(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_FeedbackWisudawan_Term.getValue().isEmpty()) {
            checkbox_FeedbackWisudawanList_ShowAll.setChecked(false); // unCheck
            txtb_FeedbackWisudawan_Name.setValue(""); // clear
            txtb_FeedbackWisudawan_Kelompok.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Tfeedbackwisudawan> soFeedbackWisudawan = new HibernateSearchObject<Tfeedbackwisudawan>(Tfeedbackwisudawan.class, getFeedbackWisudawanListCtrl().getCountRows());
            soFeedbackWisudawan.addFilter(new Filter(ConstantUtil.TERM, "%" + txtb_FeedbackWisudawan_Term.getValue() + "%", Filter.OP_ILIKE));
            soFeedbackWisudawan.addSort(ConstantUtil.TERM, false);

            // Change the BindingListModel.
            if (getFeedbackWisudawanListCtrl().getBinder() != null) {
                getFeedbackWisudawanListCtrl().getPagedBindingListWrapper().setSearchObject(soFeedbackWisudawan);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_FeedbackWisudawanMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabFeedbackWisudawanList)) {
                    tabFeedbackWisudawanList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the feedbackWisudawan list with 'like feedbackWisudawan name'. <br>
     */
    public void onClick$button_FeedbackWisudawanList_SearchName(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_FeedbackWisudawan_Name.getValue().isEmpty()) {
            checkbox_FeedbackWisudawanList_ShowAll.setChecked(false); // unCheck
            txtb_FeedbackWisudawan_Kelompok.setValue(""); // clear
            txtb_FeedbackWisudawan_Term.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Tfeedbackwisudawan> soFeedbackWisudawan = new HibernateSearchObject<Tfeedbackwisudawan>(Tfeedbackwisudawan.class, getFeedbackWisudawanListCtrl().getCountRows());
            soFeedbackWisudawan.addFilter(new Filter(ConstantUtil.MAHASISWA_DOT_NAME, "%" + txtb_FeedbackWisudawan_Name.getValue() + "%", Filter.OP_ILIKE));
            soFeedbackWisudawan.addSort(ConstantUtil.MAHASISWA_DOT_NAME, false);

            // Change the BindingListModel.
            if (getFeedbackWisudawanListCtrl().getBinder() != null) {
                getFeedbackWisudawanListCtrl().getPagedBindingListWrapper().setSearchObject(soFeedbackWisudawan);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_FeedbackWisudawanMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabFeedbackWisudawanList)) {
                    tabFeedbackWisudawanList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the feedbackWisudawan list with 'like feedbackWisudawan city'. <br>
     */
    public void onClick$button_FeedbackWisudawanList_SearchKelompok(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_FeedbackWisudawan_Kelompok.getValue().isEmpty()) {
            checkbox_FeedbackWisudawanList_ShowAll.setChecked(false); // unCheck
            txtb_FeedbackWisudawan_Name.setValue(""); // clear
            txtb_FeedbackWisudawan_Term.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Tfeedbackwisudawan> soFeedbackWisudawan = new HibernateSearchObject<Tfeedbackwisudawan>(Tfeedbackwisudawan.class, getFeedbackWisudawanListCtrl().getCountRows());
            soFeedbackWisudawan.addFilter(new Filter(ConstantUtil.KELOMPOK, "%" + txtb_FeedbackWisudawan_Kelompok.getValue() + "%", Filter.OP_ILIKE));
            soFeedbackWisudawan.addSort(ConstantUtil.KELOMPOK, false);

            // Change the BindingListModel.
            if (getFeedbackWisudawanListCtrl().getBinder() != null) {
                getFeedbackWisudawanListCtrl().getPagedBindingListWrapper().setSearchObject(soFeedbackWisudawan);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_FeedbackWisudawanMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabFeedbackWisudawanList)) {
                    tabFeedbackWisudawanList.setSelected(true);
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
        if (getFeedbackWisudawanDetailCtrl().getBinder() != null) {

            // refresh all dataBinder related controllers/components
            getFeedbackWisudawanDetailCtrl().getBinder().loadAll();

            // set editable Mode
            getFeedbackWisudawanDetailCtrl().doReadOnlyMode(true);

            btnCtrlFeedbackWisudawan.setInitEdit();
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
        Tab currentTab = tabbox_FeedbackWisudawanMain.getSelectedTab();

        // check first, if the tabs are created, if not than create it
        if (getFeedbackWisudawanDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabFeedbackWisudawanDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getFeedbackWisudawanDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabFeedbackWisudawanDetail, null));
        }

        // check if the tab is one of the Detail tabs. If so do not change the
        // selection of it
        if (!currentTab.equals(tabFeedbackWisudawanDetail)) {
            tabFeedbackWisudawanDetail.setSelected(true);
        } else {
            currentTab.setSelected(true);
        }

        getFeedbackWisudawanDetailCtrl().getBinder().loadAll();

        // remember the old vars
        doStoreInitValues();

        btnCtrlFeedbackWisudawan.setBtnStatus_Edit();

        getFeedbackWisudawanDetailCtrl().doReadOnlyMode(false);
        // set focus
        //getFeedbackWisudawanDetailCtrl().txtb_jawaban.focus();
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
        if (getFeedbackWisudawanDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabFeedbackWisudawanDetail, null));
        }

        // check first, if the tabs are created
        if (getFeedbackWisudawanDetailCtrl().getBinder() == null) {
            return;
        }

        final Tfeedbackwisudawan anFeedbackWisudawan = getSelectedFeedbackWisudawan();
        if (anFeedbackWisudawan != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anFeedbackWisudawan.getMmahasiswa().getCnama();
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
                     * Do not allow to modify the demo feedbackWisudawans
                     */
                    if (getFeedbackWisudawanDetailCtrl().getFeedbackWisudawan().getId() <= 2) {
                        try {
                            ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
                            return;
                        } catch (final InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        try {
                            getFeedbackWisudawanService().delete(anFeedbackWisudawan);
                        } catch (DataAccessException e) {
                            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                        }
                    }
                }

            }

            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrlFeedbackWisudawan.setInitEdit();

        setSelectedFeedbackWisudawan(null);
        // refresh the list
        getFeedbackWisudawanListCtrl().doFillListbox();

        // refresh all dataBinder related controllers
        getFeedbackWisudawanDetailCtrl().getBinder().loadAll();
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
        getFeedbackWisudawanDetailCtrl().getBinder().saveAll();

        try {

            /**
             * Create list of FeedbackWisudawan based on master feedback
             */
            /*setSelectedFeedbackWisudawanList(new ArrayList<Tfeedbackwisudawan>());
            for (Mfeedback mf : (List<Mfeedback>) getFeedbacks()) {
                final Tfeedbackwisudawan fa = getFeedbackWisudawanService().getNewFeedbackWisudawan();
                fa.setMmahasiswa(getFeedbackWisudawanDetailCtrl().getFeedbackWisudawan().getMmahasiswa());
                fa.setCterm(getFeedbackWisudawanDetailCtrl().getFeedbackWisudawan().getCterm());
                fa.setCkelompok(getFeedbackWisudawanDetailCtrl().getFeedbackWisudawan().getCkelompok());
                fa.setMprodi(getFeedbackWisudawanDetailCtrl().getFeedbackWisudawan().getMprodi());
                fa.setMsekolah(getFeedbackWisudawanDetailCtrl().getFeedbackWisudawan().getMsekolah());
                fa.setMfeedback(mf);
                fa.setCjawaban("1");
                getSelectedFeedbackWisudawanList().add(fa);
            } */
            getFeedbackWisudawanDetailCtrl().setSelectedFeedbackWisudawan(getSelectedFeedbackWisudawan());
            if (getFeedbackWisudawanDetailCtrl().getBinder() != null) {
                getFeedbackWisudawanDetailCtrl().getBinder().loadAll();
            }
            for (Tfeedbackwisudawan tfa : getFeedbackWisudawanDetailCtrl().getFeedbackWisudawanList()) {
                tfa.setMmahasiswa(getFeedbackWisudawanDetailCtrl().getSelectedFeedbackWisudawan().getMmahasiswa());
                tfa.setCterm(getFeedbackWisudawanDetailCtrl().getSelectedFeedbackWisudawan().getCterm());
                tfa.setCkelompok(getFeedbackWisudawanDetailCtrl().getSelectedFeedbackWisudawan().getCkelompok());
                tfa.setMprodi(getFeedbackWisudawanDetailCtrl().getSelectedFeedbackWisudawan().getMprodi());
                tfa.setMsekolah(getFeedbackWisudawanDetailCtrl().getSelectedFeedbackWisudawan().getMsekolah());
                //tfa.setCjawaban();
            }

            // save it to database
            //getFeedbackWisudawanService().saveOrUpdate(getFeedbackWisudawanDetailCtrl().getFeedbackWisudawan());
            getFeedbackWisudawanService().saveOrUpdateList(getFeedbackWisudawanDetailCtrl().getFeedbackWisudawanList());
            // if saving is successfully than actualize the beans as
            // origins.
            doStoreInitValues();
            // refresh the list
            getFeedbackWisudawanListCtrl().doFillListbox();
            // later refresh StatusBar
            Events.postEvent("onSelect", getFeedbackWisudawanListCtrl().getListBoxFeedbackWisudawan(), getSelectedFeedbackWisudawan());

            // show the objects data in the statusBar
            String str = getSelectedFeedbackWisudawan().getCterm();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetToInitValues();

            return;

        } finally {
            btnCtrlFeedbackWisudawan.setInitEdit();
            getFeedbackWisudawanDetailCtrl().doReadOnlyMode(true);
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
        if (getFeedbackWisudawanDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabFeedbackWisudawanDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getFeedbackWisudawanDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabFeedbackWisudawanDetail, null));
        }

        // remember the current object
        doStoreInitValues();

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        final Tfeedbackwisudawan anFeedbackWisudawan = getFeedbackWisudawanService().getNewFeedbackWisudawan();
        List<Tfeedbackwisudawan> listFeedbackWisudawan = getFeedbackWisudawanService().getNewFeedbackWisudawanList();
        // set the beans in the related databinded controllers
        getFeedbackWisudawanDetailCtrl().setFeedbackWisudawan(anFeedbackWisudawan);
        getFeedbackWisudawanDetailCtrl().setSelectedFeedbackWisudawan(anFeedbackWisudawan);

        getFeedbackWisudawanDetailCtrl().setFeedbackWisudawanList(listFeedbackWisudawan);
        getFeedbackWisudawanDetailCtrl().setSelectedFeedbackWisudawanList(listFeedbackWisudawan);

        // Refresh the binding mechanism
        getFeedbackWisudawanDetailCtrl().setSelectedFeedbackWisudawan(getSelectedFeedbackWisudawan());
        getFeedbackWisudawanDetailCtrl().setSelectedFeedbackWisudawan(getSelectedFeedbackWisudawan());
        if (getFeedbackWisudawanDetailCtrl().getBinder() != null) {
            getFeedbackWisudawanDetailCtrl().getBinder().loadAll();
        }

        getFeedbackWisudawanDetailCtrl().setSelectedFeedbackWisudawanList(getSelectedFeedbackWisudawanList());
        getFeedbackWisudawanDetailCtrl().setSelectedFeedbackWisudawanList(getSelectedFeedbackWisudawanList());
        if (getFeedbackWisudawanDetailCtrl().getBinder() != null) {
            getFeedbackWisudawanDetailCtrl().getBinder().loadAll();
        }
        PagedBindingListWrapper<Tfeedbackwisudawan> pblw = getFeedbackWisudawanDetailCtrl().getPagedBindingListWrapper();
        pblw.clear();
        pblw.addAll(getSelectedFeedbackWisudawanList());
        // set editable Mode
        getFeedbackWisudawanDetailCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlFeedbackWisudawan.setInitNew();

        tabFeedbackWisudawanDetail.setSelected(true);
        // set focus
        //getFeedbackWisudawanDetailCtrl().txtb_jawaban.focus();

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

        if (tabbox_FeedbackWisudawanMain.getSelectedTab() == tabFeedbackWisudawanDetail) {
            getFeedbackWisudawanDetailCtrl().doFitSize(event);
        } else if (tabbox_FeedbackWisudawanMain.getSelectedTab() == tabFeedbackWisudawanList) {
            // resize and fill Listbox new
            getFeedbackWisudawanListCtrl().doFillListbox();
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

        if (getSelectedFeedbackWisudawan() != null) {

            try {
                setOriginalFeedbackWisudawan((Tfeedbackwisudawan) ZksampleBeanUtils.cloneBean(getSelectedFeedbackWisudawan()));
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

        if (getOriginalFeedbackWisudawan() != null) {

            try {
                setSelectedFeedbackWisudawan((Tfeedbackwisudawan) ZksampleBeanUtils.cloneBean(getOriginalFeedbackWisudawan()));
                // TODO Bug in DataBinder??
                windowFeedbackWisudawanMain.invalidate();

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

        // window_FeedbackWisudawanList.setVisible(workspace.isAllowed("window_FeedbackWisudawanList"));
        button_FeedbackWisudawanList_PrintList.setVisible(workspace.isAllowed("button_FeedbackWisudawanList_PrintList"));
        button_FeedbackWisudawanList_SearchTerm.setVisible(workspace.isAllowed("button_FeedbackWisudawanList_SearchNo"));
        button_FeedbackWisudawanList_SearchKelompok.setVisible(workspace.isAllowed("button_FeedbackWisudawanList_SearchName"));
        button_FeedbackWisudawanList_SearchName.setVisible(workspace.isAllowed("button_FeedbackWisudawanList_SearchCity"));

        btnHelp.setVisible(workspace.isAllowed("button_FeedbackWisudawanMain_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_FeedbackWisudawanMain_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_FeedbackWisudawanMain_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_FeedbackWisudawanMain_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_FeedbackWisudawanMain_btnSave"));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public void setOriginalFeedbackWisudawan(Tfeedbackwisudawan originalFeedbackWisudawan) {
        this.originalFeedbackWisudawan = originalFeedbackWisudawan;
    }

    public Tfeedbackwisudawan getOriginalFeedbackWisudawan() {
        return this.originalFeedbackWisudawan;
    }

    public void setSelectedFeedbackWisudawan(Tfeedbackwisudawan selectedFeedbackWisudawan) {
        this.selectedFeedbackWisudawan = selectedFeedbackWisudawan;
        try {
        List<Tfeedbackwisudawan> ltf = this.selectedFeedbackWisudawanList;
        for(Tfeedbackwisudawan tf : ltf) {
            tf.setMmahasiswa(selectedFeedbackWisudawan.getMmahasiswa());
            tf.setCterm(selectedFeedbackWisudawan.getCterm());
            tf.setCkelompok(selectedFeedbackWisudawan.getCkelompok());
            tf.setMprodi(selectedFeedbackWisudawan.getMprodi());
            tf.setMsekolah(selectedFeedbackWisudawan.getMsekolah());
        }
        } catch (NullPointerException e) {}

    }

    public Tfeedbackwisudawan getSelectedFeedbackWisudawan() {
        return this.selectedFeedbackWisudawan;
    }

    public void setSelectedFeedbackWisudawanList(List<Tfeedbackwisudawan> selectedFeedbackWisudawanList) {
        this.selectedFeedbackWisudawanList = selectedFeedbackWisudawanList;
    }

    public List<Tfeedbackwisudawan> getSelectedFeedbackWisudawanList() {
        return this.selectedFeedbackWisudawanList;
    }

    public void setFeedbackWisudawans(BindingListModelList feedbackWisudawans) {
        this.feedbackWisudawans = feedbackWisudawans;
    }

    public BindingListModelList getFeedbackWisudawans() {
        return this.feedbackWisudawans;
    }

    public void setFeedbackWisudawanService(FeedbackWisudawanService feedbackWisudawanService) {
        this.feedbackWisudawanService = feedbackWisudawanService;
    }

    public FeedbackWisudawanService getFeedbackWisudawanService() {
        return this.feedbackWisudawanService;
    }

    public void setFeedbackWisudawanListCtrl(FeedbackWisudawanListCtrl feedbackWisudawanListCtrl) {
        this.feedbackWisudawanListCtrl = feedbackWisudawanListCtrl;
    }

    public FeedbackWisudawanListCtrl getFeedbackWisudawanListCtrl() {
        return this.feedbackWisudawanListCtrl;
    }

    public void setFeedbackWisudawanDetailCtrl(FeedbackWisudawanDetailCtrl feedbackWisudawanDetailCtrl) {
        this.feedbackWisudawanDetailCtrl = feedbackWisudawanDetailCtrl;
    }

    public FeedbackWisudawanDetailCtrl getFeedbackWisudawanDetailCtrl() {
        return this.feedbackWisudawanDetailCtrl;
    }

    public void setSelectedFeedback(Mfeedback selectedFeedback) {
        this.selectedFeedback = selectedFeedback;
    }

    public Mfeedback getSelectedFeedback() {
        return this.selectedFeedback;
    }

    public void setFeedbacks(BindingListModelList feedbacks) {
        this.feedbacks = feedbacks;
    }

    public BindingListModelList getFeedbacks() {
        return this.feedbacks;
    }
}
