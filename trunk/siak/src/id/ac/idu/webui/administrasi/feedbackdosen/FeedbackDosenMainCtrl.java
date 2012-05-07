package id.ac.idu.webui.administrasi.feedbackdosen;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.FeedbackDosenService;
import id.ac.idu.backend.model.Mfeedback;
import id.ac.idu.backend.model.Tfeedbackdosen;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.util.ConstantUtil;
import id.ac.idu.webui.administrasi.report.FeedbackDosenSimpleDJReport;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 3/21/12
 * Time: 4:07 AM
 */
public class FeedbackDosenMainCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(FeedbackDosenMainCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowFeedbackDosenMain; // autowired

    // Tabs
    protected Tabbox tabbox_FeedbackDosenMain; // autowired
    protected Tab tabFeedbackDosenList; // autowired
    protected Tab tabFeedbackDosenDetail; // autowired
    protected Tabpanel tabPanelFeedbackDosenList; // autowired
    protected Tabpanel tabPanelFeedbackDosenDetail; // autowired

    // filter components
    protected Checkbox checkbox_FeedbackDosenList_ShowAll; // autowired
    protected Textbox txtb_FeedbackDosen_Term; // aurowired
    protected Button button_FeedbackDosenList_SearchTerm; // aurowired
    protected Textbox txtb_FeedbackDosen_Kelompok; // aurowired
    protected Button button_FeedbackDosenList_SearchKelompok; // aurowired
    protected Textbox txtb_FeedbackDosen_Nama; // aurowired
    protected Button button_FeedbackDosenList_SearchName; // aurowired

    // checkRights
    protected Button button_FeedbackDosenList_PrintList;

    // Button controller for the CRUD buttons
    private final String btnCtroller_ClassPrefix = "button_FeedbackDosenMain_";
    private ButtonStatusCtrl btnCtrlFeedbackDosen;
    protected Button btnNew; // autowired
    protected Button btnEdit; // autowired
    protected Button btnDelete; // autowired
    protected Button btnSave; // autowired
    protected Button btnCancel; // autowired

    protected Button btnHelp;

    // Tab-Controllers for getting the binders
    private FeedbackDosenListCtrl feedbackDosenListCtrl;
    private FeedbackDosenDetailCtrl feedbackDosenDetailCtrl;

    // Databinding
    private Tfeedbackdosen selectedFeedbackDosen;
    private List<Tfeedbackdosen> selectedFeedbackDosenList;
    private BindingListModelList feedbackDosens;
    private Mfeedback selectedFeedback;
    private BindingListModelList feedbacks;

    // ServiceDAOs / Domain Classes
    private FeedbackDosenService feedbackDosenService;

    // always a copy from the bean before modifying. Used for reseting
    private Tfeedbackdosen originalFeedbackDosen;

    /**
     * default constructor.<br>
     */
    public FeedbackDosenMainCtrl() {
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
    public void onCreate$windowFeedbackDosenMain(Event event) throws Exception {
        windowFeedbackDosenMain.setContentStyle("padding:0px;");

        // create the Button Controller. Disable not used buttons during working
        btnCtrlFeedbackDosen = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

        //doCheckRights();

        /**
         * Initiate the first loading by selecting the customerList tab and
         * create the components from the zul-file.
         */
        tabFeedbackDosenList.setSelected(true);

        if (tabPanelFeedbackDosenList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelFeedbackDosenList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/feedbackdosen/feedbackDosenList.zul");
        }

        // init the buttons for editMode
        btnCtrlFeedbackDosen.setInitEdit();
    }

    /**
     * When the tab 'tabFeedbackDosenList' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabFeedbackDosenList(Event event) throws IOException {
        logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelFeedbackDosenList.getFirstChild() != null) {
            tabFeedbackDosenList.setSelected(true);

            return;
        }

        if (tabPanelFeedbackDosenList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelFeedbackDosenList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/feedbackdosen/feedbackDosenList.zul");
        }

    }

    /**
     * When the tab 'tabPanelFeedbackDosenDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabFeedbackDosenDetail(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelFeedbackDosenDetail.getFirstChild() != null) {
            tabFeedbackDosenDetail.setSelected(true);

             try{
                doSearchDetail(getSelectedFeedbackDosen());
            }catch (Exception e)  {
               e.printStackTrace();
            }
            getFeedbackDosenDetailCtrl().getBinder().loadAll();
            return;
        }

        if (tabPanelFeedbackDosenDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelFeedbackDosenDetail, this, "ModuleMainController", "/WEB-INF/pages/administrasi/feedbackdosen/feedbackDosenDetail.zul");
        }
    }

     public void doSearchDetail(Tfeedbackdosen fa) throws Exception {

        if (getFeedbackDosenDetailCtrl().getBinder() != null) {

            getFeedbackDosenDetailCtrl().getPagedBindingListWrapper().clear();
            getFeedbackDosenDetailCtrl().setRadioOnListBox(getList(fa));

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_FeedbackDosenMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabFeedbackDosenDetail)) {
                tabFeedbackDosenDetail.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }
    }

     public List<Tfeedbackdosen> getList(Tfeedbackdosen feedbackDosen){
       List<Tfeedbackdosen> exlist = new ArrayList<Tfeedbackdosen>();
       if (feedbackDosen!=null && feedbackDosen.getMpegawai()!=null) {
           exlist =  feedbackDosenService.getFeedbackDosenByNip(feedbackDosen.getMpegawai(),feedbackDosen.getCterm(),feedbackDosen.getCkelompok());
       }
       return exlist;
    }

    /**
     * when the "print feedbackDosens list" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_FeedbackDosenList_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        new FeedbackDosenSimpleDJReport(win);
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_FeedbackDosenList_ShowAll(Event event) {
        // logger.debug(event.toString());

        // empty the text search boxes
        txtb_FeedbackDosen_Term.setValue(""); // clear
        txtb_FeedbackDosen_Nama.setValue(""); // clear
        txtb_FeedbackDosen_Kelompok.setValue(""); // clear

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Tfeedbackdosen> soFeedbackDosen = new HibernateSearchObject<Tfeedbackdosen>(Tfeedbackdosen.class, getFeedbackDosenListCtrl().getCountRows());
        soFeedbackDosen.addSort(ConstantUtil.TERM, false);

        // Change the BindingListModel.
        if (getFeedbackDosenListCtrl().getBinder() != null) {
            getFeedbackDosenListCtrl().getPagedBindingListWrapper().setSearchObject(soFeedbackDosen);

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_FeedbackDosenMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabFeedbackDosenList)) {
                tabFeedbackDosenList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }

    }

    /**
     * Filter the feedbackDosen list with 'like feedbackDosen number'. <br>
     */
    public void onClick$button_FeedbackDosenList_SearchTerm(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_FeedbackDosen_Term.getValue().isEmpty()) {
            checkbox_FeedbackDosenList_ShowAll.setChecked(false); // unCheck
            txtb_FeedbackDosen_Nama.setValue(""); // clear
            txtb_FeedbackDosen_Kelompok.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Tfeedbackdosen> soFeedbackDosen = new HibernateSearchObject<Tfeedbackdosen>(Tfeedbackdosen.class, getFeedbackDosenListCtrl().getCountRows());
            soFeedbackDosen.addFilter(new Filter(ConstantUtil.TERM, "%" + txtb_FeedbackDosen_Term.getValue() + "%", Filter.OP_ILIKE));
            soFeedbackDosen.addSort(ConstantUtil.TERM, false);

            // Change the BindingListModel.
            if (getFeedbackDosenListCtrl().getBinder() != null) {
                getFeedbackDosenListCtrl().getPagedBindingListWrapper().setSearchObject(soFeedbackDosen);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_FeedbackDosenMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabFeedbackDosenList)) {
                    tabFeedbackDosenList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the feedbackDosen list with 'like feedbackDosen name'. <br>
     */
    public void onClick$button_FeedbackDosenList_SearchNama(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_FeedbackDosen_Nama.getValue().isEmpty()) {
            checkbox_FeedbackDosenList_ShowAll.setChecked(false); // unCheck
            txtb_FeedbackDosen_Kelompok.setValue(""); // clear
            txtb_FeedbackDosen_Term.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Tfeedbackdosen> soFeedbackDosen = new HibernateSearchObject<Tfeedbackdosen>(Tfeedbackdosen.class, getFeedbackDosenListCtrl().getCountRows());
            soFeedbackDosen.addFilter(new Filter(ConstantUtil.PEGAWAI_DOT_NAMA, "%" + txtb_FeedbackDosen_Nama.getValue() + "%", Filter.OP_ILIKE));
            soFeedbackDosen.addSort(ConstantUtil.PEGAWAI_DOT_NAMA, false);

            // Change the BindingListModel.
            if (getFeedbackDosenListCtrl().getBinder() != null) {
                getFeedbackDosenListCtrl().getPagedBindingListWrapper().setSearchObject(soFeedbackDosen);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_FeedbackDosenMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabFeedbackDosenList)) {
                    tabFeedbackDosenList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the feedbackDosen list with 'like feedbackDosen city'. <br>
     */
    public void onClick$button_FeedbackDosenList_SearchKelompok(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_FeedbackDosen_Kelompok.getValue().isEmpty()) {
            checkbox_FeedbackDosenList_ShowAll.setChecked(false); // unCheck
            txtb_FeedbackDosen_Nama.setValue(""); // clear
            txtb_FeedbackDosen_Term.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Tfeedbackdosen> soFeedbackDosen = new HibernateSearchObject<Tfeedbackdosen>(Tfeedbackdosen.class, getFeedbackDosenListCtrl().getCountRows());
            soFeedbackDosen.addFilter(new Filter(ConstantUtil.KELOMPOK, "%" + txtb_FeedbackDosen_Kelompok.getValue() + "%", Filter.OP_ILIKE));
            soFeedbackDosen.addSort(ConstantUtil.KELOMPOK, false);

            // Change the BindingListModel.
            if (getFeedbackDosenListCtrl().getBinder() != null) {
                getFeedbackDosenListCtrl().getPagedBindingListWrapper().setSearchObject(soFeedbackDosen);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_FeedbackDosenMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabFeedbackDosenList)) {
                    tabFeedbackDosenList.setSelected(true);
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
        if (getFeedbackDosenDetailCtrl().getBinder() != null) {

            // refresh all dataBinder related controllers/components
            getFeedbackDosenDetailCtrl().getBinder().loadAll();

            // set editable Mode
            getFeedbackDosenDetailCtrl().doReadOnlyMode(true);

            btnCtrlFeedbackDosen.setInitEdit();
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
        Tab currentTab = tabbox_FeedbackDosenMain.getSelectedTab();

        // check first, if the tabs are created, if not than create it
        if (getFeedbackDosenDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabFeedbackDosenDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getFeedbackDosenDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabFeedbackDosenDetail, null));
        }

        // check if the tab is one of the Detail tabs. If so do not change the
        // selection of it
        if (!currentTab.equals(tabFeedbackDosenDetail)) {
            tabFeedbackDosenDetail.setSelected(true);
        } else {
            currentTab.setSelected(true);
        }

        getFeedbackDosenDetailCtrl().getBinder().loadAll();

        // remember the old vars
        doStoreInitValues();

        btnCtrlFeedbackDosen.setBtnStatus_Edit();

        getFeedbackDosenDetailCtrl().doReadOnlyMode(false);
        // set focus
        //getFeedbackDosenDetailCtrl().txtb_jawaban.focus();
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
        if (getFeedbackDosenDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabFeedbackDosenDetail, null));
        }

        // check first, if the tabs are created
        if (getFeedbackDosenDetailCtrl().getBinder() == null) {
            return;
        }

        final Tfeedbackdosen anFeedbackDosen = getSelectedFeedbackDosen();
        if (anFeedbackDosen != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anFeedbackDosen.getMpegawai().getCnama();
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
                     * Do not allow to modify the demo feedbackDosens
                     */
                    if (getFeedbackDosenDetailCtrl().getFeedbackDosen().getId() <= 2) {
                        try {
                            ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
                            return;
                        } catch (final InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        try {
                            getFeedbackDosenService().delete(anFeedbackDosen);
                        } catch (DataAccessException e) {
                            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                        }
                    }
                }

            }

            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrlFeedbackDosen.setInitEdit();

        setSelectedFeedbackDosen(null);
        // refresh the list
        getFeedbackDosenListCtrl().doFillListbox();

        // refresh all dataBinder related controllers
        getFeedbackDosenDetailCtrl().getBinder().loadAll();
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
        getFeedbackDosenDetailCtrl().getBinder().saveAll();

        try {

            /**
             * Create list of FeedbackDosen based on master feedback
             */
            /*setSelectedFeedbackDosenList(new ArrayList<Tfeedbackdosen>());
            for (Mfeedback mf : (List<Mfeedback>) getFeedbacks()) {
                final Tfeedbackdosen fa = getFeedbackDosenService().getNewFeedbackDosen();
                fa.setMmahasiswa(getFeedbackDosenDetailCtrl().getFeedbackDosen().getMmahasiswa());
                fa.setCterm(getFeedbackDosenDetailCtrl().getFeedbackDosen().getCterm());
                fa.setCkelompok(getFeedbackDosenDetailCtrl().getFeedbackDosen().getCkelompok());
                fa.setMprodi(getFeedbackDosenDetailCtrl().getFeedbackDosen().getMprodi());
                fa.setMsekolah(getFeedbackDosenDetailCtrl().getFeedbackDosen().getMsekolah());
                fa.setMfeedback(mf);
                fa.setCjawaban("1");
                getSelectedFeedbackDosenList().add(fa);
            } */
            getFeedbackDosenDetailCtrl().setSelectedFeedbackDosen(getSelectedFeedbackDosen());

//            if (getFeedbackDosenDetailCtrl().getBinder() != null) {
//                getFeedbackDosenDetailCtrl().getBinder().loadAll();
//            }
//            for (Tfeedbackdosen tfa : getFeedbackDosenDetailCtrl().getFeedbackDosenList()) {
//                tfa.setMpegawai(getFeedbackDosenDetailCtrl().getFeedbackDosen().getMpegawai());
//                tfa.setCterm(getFeedbackDosenDetailCtrl().getFeedbackDosen().getCterm());
//                tfa.setCkelompok(getFeedbackDosenDetailCtrl().getFeedbackDosen().getCkelompok());
//                tfa.setMprodi(getFeedbackDosenDetailCtrl().getFeedbackDosen().getMprodi());
//                tfa.setMsekolah(getFeedbackDosenDetailCtrl().getFeedbackDosen().getMsekolah());
//                //tfa.setCjawaban();
//            }

            // save it to database
            //getFeedbackDosenService().saveOrUpdate(getFeedbackDosenDetailCtrl().getFeedbackDosen());
            getFeedbackDosenService().saveOrUpdateList(getFeedbackDosenDetailCtrl().setlbtolist(getFeedbackDosenDetailCtrl().getFeedbackDosenList()));
            // if saving is successfully than actualize the beans as
            // origins.
            doStoreInitValues();
            // refresh the list
            getFeedbackDosenListCtrl().doFillListbox();
            // later refresh StatusBar
            Events.postEvent("onSelect", getFeedbackDosenListCtrl().getListBoxFeedbackDosen(), getSelectedFeedbackDosen());

            // show the objects data in the statusBar
            String str = getSelectedFeedbackDosen().getCterm();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetToInitValues();

            return;

        } finally {
            btnCtrlFeedbackDosen.setInitEdit();
            getFeedbackDosenDetailCtrl().doReadOnlyMode(true);
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
        if (getFeedbackDosenDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabFeedbackDosenDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getFeedbackDosenDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabFeedbackDosenDetail, null));
        }

        // remember the current object
        doStoreInitValues();

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        final Tfeedbackdosen anFeedbackDosen = getFeedbackDosenService().getNewFeedbackDosen();
        List<Tfeedbackdosen> listFeedbackDosen = getFeedbackDosenService().getNewFeedbackDosenList();
        // set the beans in the related databinded controllers
        getFeedbackDosenDetailCtrl().setFeedbackDosen(anFeedbackDosen);
        getFeedbackDosenDetailCtrl().setSelectedFeedbackDosen(anFeedbackDosen);

        getFeedbackDosenDetailCtrl().setFeedbackDosenList(listFeedbackDosen);
        getFeedbackDosenDetailCtrl().setSelectedFeedbackDosenList(listFeedbackDosen);

        // Refresh the binding mechanism
        getFeedbackDosenDetailCtrl().setSelectedFeedbackDosen(getSelectedFeedbackDosen());
        getFeedbackDosenDetailCtrl().setSelectedFeedbackDosen(getSelectedFeedbackDosen());
        if (getFeedbackDosenDetailCtrl().getBinder() != null) {
            getFeedbackDosenDetailCtrl().getBinder().loadAll();
        }

        getFeedbackDosenDetailCtrl().setSelectedFeedbackDosenList(getSelectedFeedbackDosenList());
        getFeedbackDosenDetailCtrl().setSelectedFeedbackDosenList(getSelectedFeedbackDosenList());
        if (getFeedbackDosenDetailCtrl().getBinder() != null) {
            getFeedbackDosenDetailCtrl().getBinder().loadAll();
        }
        PagedBindingListWrapper<Tfeedbackdosen> pblw = getFeedbackDosenDetailCtrl().getPagedBindingListWrapper();
        pblw.clear();
        pblw.addAll(getSelectedFeedbackDosenList());
        // set editable Mode
        getFeedbackDosenDetailCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlFeedbackDosen.setInitNew();

        tabFeedbackDosenDetail.setSelected(true);
        // set focus
        //getFeedbackDosenDetailCtrl().txtb_jawaban.focus();
        try {
        doResizeSelectedTab(null);
        getFeedbackDosenDetailCtrl().doFillListbox();
        } catch (Exception e) {}
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

        if (tabbox_FeedbackDosenMain.getSelectedTab() == tabFeedbackDosenDetail) {
            getFeedbackDosenDetailCtrl().doFitSize(event);
        } else if (tabbox_FeedbackDosenMain.getSelectedTab() == tabFeedbackDosenList) {
            // resize and fill Listbox new
            getFeedbackDosenListCtrl().doFillListbox();
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

        if (getSelectedFeedbackDosen() != null) {

            try {
                setOriginalFeedbackDosen((Tfeedbackdosen) ZksampleBeanUtils.cloneBean(getSelectedFeedbackDosen()));
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

        if (getOriginalFeedbackDosen() != null) {

            try {
                setSelectedFeedbackDosen((Tfeedbackdosen) ZksampleBeanUtils.cloneBean(getOriginalFeedbackDosen()));
                // TODO Bug in DataBinder??
                windowFeedbackDosenMain.invalidate();

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

        // window_FeedbackDosenList.setVisible(workspace.isAllowed("window_FeedbackDosenList"));
        button_FeedbackDosenList_PrintList.setVisible(workspace.isAllowed("button_FeedbackDosenList_PrintList"));
        button_FeedbackDosenList_SearchTerm.setVisible(workspace.isAllowed("button_FeedbackDosenList_SearchNo"));
        button_FeedbackDosenList_SearchKelompok.setVisible(workspace.isAllowed("button_FeedbackDosenList_SearchName"));
        button_FeedbackDosenList_SearchName.setVisible(workspace.isAllowed("button_FeedbackDosenList_SearchCity"));

        btnHelp.setVisible(workspace.isAllowed("button_FeedbackDosenMain_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_FeedbackDosenMain_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_FeedbackDosenMain_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_FeedbackDosenMain_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_FeedbackDosenMain_btnSave"));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public void setOriginalFeedbackDosen(Tfeedbackdosen originalFeedbackDosen) {
        this.originalFeedbackDosen = originalFeedbackDosen;
    }

    public Tfeedbackdosen getOriginalFeedbackDosen() {
        return this.originalFeedbackDosen;
    }

    public void setSelectedFeedbackDosen(Tfeedbackdosen selectedFeedbackDosen) {
        this.selectedFeedbackDosen = selectedFeedbackDosen;
        try {
        List<Tfeedbackdosen> ltf = this.selectedFeedbackDosenList;
        for(Tfeedbackdosen tf : ltf) {
            tf.setMpegawai(selectedFeedbackDosen.getMpegawai());
            tf.setCterm(selectedFeedbackDosen.getCterm());
            tf.setCkelompok(selectedFeedbackDosen.getCkelompok());
            tf.setMprodi(selectedFeedbackDosen.getMprodi());
            tf.setMsekolah(selectedFeedbackDosen.getMsekolah());
        }
        } catch (NullPointerException e) {}

    }

    public Tfeedbackdosen getSelectedFeedbackDosen() {
        return this.selectedFeedbackDosen;
    }

    public void setSelectedFeedbackDosenList(List<Tfeedbackdosen> selectedFeedbackDosenList) {
        this.selectedFeedbackDosenList = selectedFeedbackDosenList;
    }

    public List<Tfeedbackdosen> getSelectedFeedbackDosenList() {
        return this.selectedFeedbackDosenList;
    }

    public void setFeedbackDosens(BindingListModelList feedbackDosens) {
        this.feedbackDosens = feedbackDosens;
    }

    public BindingListModelList getFeedbackDosens() {
        return this.feedbackDosens;
    }

    public void setFeedbackDosenService(FeedbackDosenService feedbackDosenService) {
        this.feedbackDosenService = feedbackDosenService;
    }

    public FeedbackDosenService getFeedbackDosenService() {
        return this.feedbackDosenService;
    }

    public void setFeedbackDosenListCtrl(FeedbackDosenListCtrl feedbackDosenListCtrl) {
        this.feedbackDosenListCtrl = feedbackDosenListCtrl;
    }

    public FeedbackDosenListCtrl getFeedbackDosenListCtrl() {
        return this.feedbackDosenListCtrl;
    }

    public void setFeedbackDosenDetailCtrl(FeedbackDosenDetailCtrl feedbackDosenDetailCtrl) {
        this.feedbackDosenDetailCtrl = feedbackDosenDetailCtrl;
    }

    public FeedbackDosenDetailCtrl getFeedbackDosenDetailCtrl() {
        return this.feedbackDosenDetailCtrl;
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
