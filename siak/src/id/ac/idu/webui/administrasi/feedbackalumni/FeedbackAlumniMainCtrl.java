package id.ac.idu.webui.administrasi.feedbackalumni;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.FeedbackAlumniService;
import id.ac.idu.backend.model.Mfeedback;
import id.ac.idu.backend.model.Tfeedbackalumni;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.util.ConstantUtil;
import id.ac.idu.webui.administrasi.report.FeedbackAlumniSimpleDJReport;
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
public class FeedbackAlumniMainCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(FeedbackAlumniMainCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowFeedbackAlumniMain; // autowired

    // Tabs
    protected Tabbox tabbox_FeedbackAlumniMain; // autowired
    protected Tab tabFeedbackAlumniList; // autowired
    protected Tab tabFeedbackAlumniDetail; // autowired
    protected Tabpanel tabPanelFeedbackAlumniList; // autowired
    protected Tabpanel tabPanelFeedbackAlumniDetail; // autowired

    // filter components
    protected Checkbox checkbox_FeedbackAlumniList_ShowAll; // autowired
    protected Textbox txtb_FeedbackAlumni_Term; // aurowired
    protected Button button_FeedbackAlumniList_SearchTerm; // aurowired
    protected Textbox txtb_FeedbackAlumni_Kelompok; // aurowired
    protected Button button_FeedbackAlumniList_SearchKelompok; // aurowired
    protected Textbox txtb_FeedbackAlumni_Name; // aurowired
    protected Button button_FeedbackAlumniList_SearchName; // aurowired

    // checkRights
    protected Button button_FeedbackAlumniList_PrintList;

    // Button controller for the CRUD buttons
    private final String btnCtroller_ClassPrefix = "button_FeedbackAlumniMain_";
    private ButtonStatusCtrl btnCtrlFeedbackAlumni;
    protected Button btnNew; // autowired
    protected Button btnEdit; // autowired
    protected Button btnDelete; // autowired
    protected Button btnSave; // autowired
    protected Button btnCancel; // autowired

    protected Button btnHelp;

    // Tab-Controllers for getting the binders
    private FeedbackAlumniListCtrl feedbackAlumniListCtrl;
    private FeedbackAlumniDetailCtrl feedbackAlumniDetailCtrl;

    // Databinding
    private Tfeedbackalumni selectedFeedbackAlumni;
    private List<Tfeedbackalumni> selectedFeedbackAlumniList;
    private BindingListModelList feedbackAlumnis;
    private Mfeedback selectedFeedback;
    private BindingListModelList feedbacks;

    // ServiceDAOs / Domain Classes
    private FeedbackAlumniService feedbackAlumniService;



    // always a copy from the bean before modifying. Used for reseting
    private Tfeedbackalumni originalFeedbackAlumni;

    /**
     * default constructor.<br>
     */
    public FeedbackAlumniMainCtrl() {
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
    public void onCreate$windowFeedbackAlumniMain(Event event) throws Exception {
        windowFeedbackAlumniMain.setContentStyle("padding:0px;");

        // create the Button Controller. Disable not used buttons during working
        btnCtrlFeedbackAlumni = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

        //doCheckRights();

        /**
         * Initiate the first loading by selecting the customerList tab and
         * create the components from the zul-file.
         */
        tabFeedbackAlumniList.setSelected(true);

        if (tabPanelFeedbackAlumniList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelFeedbackAlumniList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/feedbackalumni/feedbackAlumniList.zul");
        }

        // init the buttons for editMode
        btnCtrlFeedbackAlumni.setInitEdit();
    }

    /**
     * When the tab 'tabFeedbackAlumniList' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabFeedbackAlumniList(Event event) throws IOException {
        logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        doShowAll();
        if (tabPanelFeedbackAlumniList.getFirstChild() != null) {
            tabFeedbackAlumniList.setSelected(true);

            return;
        }

        if (tabPanelFeedbackAlumniList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelFeedbackAlumniList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/feedbackalumni/feedbackAlumniList.zul");
        }

    }

    /**
     * When the tab 'tabPanelFeedbackAlumniDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws java.io.IOException
     */
    public void onSelect$tabFeedbackAlumniDetail(Event event) throws IOException, InterruptedException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelFeedbackAlumniDetail.getFirstChild() != null) {
            tabFeedbackAlumniDetail.setSelected(true);

            // refresh the Binding mechanism
            try{
                doSearchDetail(getSelectedFeedbackAlumni());
            }catch (Exception e)  {
               e.printStackTrace();
            }
            getFeedbackAlumniDetailCtrl().getBinder().loadAll();
            return;
        }

        if (tabPanelFeedbackAlumniDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelFeedbackAlumniDetail, this, "ModuleMainController", "/WEB-INF/pages/administrasi/feedbackalumni/feedbackAlumniDetail.zul");
        }
    }

    public List<Tfeedbackalumni> getList(Tfeedbackalumni feedbackAlumni){
       List<Tfeedbackalumni> exlist = new ArrayList<Tfeedbackalumni>();
       if (feedbackAlumni!=null && feedbackAlumni.getMmahasiswa()!=null) {
           exlist =  feedbackAlumniService.getFeedbackAlumniByNim(feedbackAlumni.getMmahasiswa(),feedbackAlumni.getCterm(),feedbackAlumni.getCkelompok());
       }
       return exlist;

//        return  (List<Tfeedbackalumni>) hsearch;
    }
    /**
     * when the "print feedbackAlumnis list" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_FeedbackAlumniList_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        new FeedbackAlumniSimpleDJReport(win);
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_FeedbackAlumniList_ShowAll(Event event) {
        // logger.debug(event.toString());

        // empty the text search boxes
        txtb_FeedbackAlumni_Term.setValue(""); // clear
//        txtb_FeedbackAlumni_Name.setValue(""); // clear
//        txtb_FeedbackAlumni_Kelompok.setValue(""); // clear

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Tfeedbackalumni> soFeedbackAlumni = new HibernateSearchObject<Tfeedbackalumni>(Tfeedbackalumni.class, getFeedbackAlumniListCtrl().getCountRows());
        soFeedbackAlumni.addSort(ConstantUtil.TERM, false);

        // Change the BindingListModel.
        if (getFeedbackAlumniListCtrl().getBinder() != null) {
            getFeedbackAlumniListCtrl().getPagedBindingListWrapper().setSearchObject(soFeedbackAlumni);

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_FeedbackAlumniMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabFeedbackAlumniList)) {
                tabFeedbackAlumniList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }

    }


    /**
     * Filter the feedbackAlumni list with 'like feedbackAlumni number'. <br>
     */
    public void onClick$button_FeedbackAlumniList_SearchTerm(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_FeedbackAlumni_Term.getValue().isEmpty()) {
            checkbox_FeedbackAlumniList_ShowAll.setChecked(false); // unCheck
//            txtb_FeedbackAlumni_Name.setValue(""); // clear
//            txtb_FeedbackAlumni_Kelompok.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Tfeedbackalumni> soFeedbackAlumni = new HibernateSearchObject<Tfeedbackalumni>(Tfeedbackalumni.class, getFeedbackAlumniListCtrl().getCountRows());
            soFeedbackAlumni.addFilter(new Filter(ConstantUtil.TERM, "%" + txtb_FeedbackAlumni_Term.getValue() + "%", Filter.OP_ILIKE));
            soFeedbackAlumni.addSort(ConstantUtil.TERM, false);

            // Change the BindingListModel.
            if (getFeedbackAlumniListCtrl().getBinder() != null) {
                getFeedbackAlumniListCtrl().getPagedBindingListWrapper().setSearchObject(soFeedbackAlumni);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_FeedbackAlumniMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabFeedbackAlumniList)) {
                    tabFeedbackAlumniList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the feedbackAlumni list with 'like feedbackAlumni name'. <br>
     */
    public void onClick$button_FeedbackAlumniList_SearchName(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_FeedbackAlumni_Name.getValue().isEmpty()) {
            checkbox_FeedbackAlumniList_ShowAll.setChecked(false); // unCheck
            txtb_FeedbackAlumni_Kelompok.setValue(""); // clear
            txtb_FeedbackAlumni_Term.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Tfeedbackalumni> soFeedbackAlumni = new HibernateSearchObject<Tfeedbackalumni>(Tfeedbackalumni.class, getFeedbackAlumniListCtrl().getCountRows());
            soFeedbackAlumni.addFilter(new Filter(ConstantUtil.MAHASISWA_DOT_NAME, "%" + txtb_FeedbackAlumni_Name.getValue() + "%", Filter.OP_ILIKE));
            soFeedbackAlumni.addSort(ConstantUtil.MAHASISWA_DOT_NAME, false);

            // Change the BindingListModel.
            if (getFeedbackAlumniListCtrl().getBinder() != null) {
                getFeedbackAlumniListCtrl().getPagedBindingListWrapper().setSearchObject(soFeedbackAlumni);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_FeedbackAlumniMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabFeedbackAlumniList)) {
                    tabFeedbackAlumniList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the feedbackAlumni list with 'like feedbackAlumni city'. <br>
     */
    public void onClick$button_FeedbackAlumniList_SearchKelompok(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!txtb_FeedbackAlumni_Kelompok.getValue().isEmpty()) {
            checkbox_FeedbackAlumniList_ShowAll.setChecked(false); // unCheck
            txtb_FeedbackAlumni_Name.setValue(""); // clear
            txtb_FeedbackAlumni_Term.setValue(""); // clear

            // ++ create the searchObject and init sorting ++//
            HibernateSearchObject<Tfeedbackalumni> soFeedbackAlumni = new HibernateSearchObject<Tfeedbackalumni>(Tfeedbackalumni.class, getFeedbackAlumniListCtrl().getCountRows());
            soFeedbackAlumni.addFilter(new Filter(ConstantUtil.KELOMPOK, "%" + txtb_FeedbackAlumni_Kelompok.getValue() + "%", Filter.OP_ILIKE));
            soFeedbackAlumni.addSort(ConstantUtil.KELOMPOK, false);

            // Change the BindingListModel.
            if (getFeedbackAlumniListCtrl().getBinder() != null) {
                getFeedbackAlumniListCtrl().getPagedBindingListWrapper().setSearchObject(soFeedbackAlumni);

                // get the current Tab for later checking if we must change it
                Tab currentTab = tabbox_FeedbackAlumniMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(tabFeedbackAlumniList)) {
                    tabFeedbackAlumniList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }

        }
    }

     public void doShowAll() {
        // logger.debug(event.toString());

        // empty the text search boxes
        txtb_FeedbackAlumni_Term.setValue(""); // clear
//        txtb_FeedbackAlumni_Name.setValue(""); // clear
//        txtb_FeedbackAlumni_Kelompok.setValue(""); // clear

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Tfeedbackalumni> soFeedbackAlumni = new HibernateSearchObject<Tfeedbackalumni>(Tfeedbackalumni.class, getFeedbackAlumniListCtrl().getCountRows());
        soFeedbackAlumni.addSort(ConstantUtil.TERM, false);

        // Change the BindingListModel.
        if (getFeedbackAlumniListCtrl().getBinder() != null) {
            getFeedbackAlumniListCtrl().getPagedBindingListWrapper().setSearchObject(soFeedbackAlumni);

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_FeedbackAlumniMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabFeedbackAlumniList)) {
                tabFeedbackAlumniList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }
    }

    public void doSearchDetail(Tfeedbackalumni fa) throws Exception {
        // logger.debug(event.toString());

        // if not empty

            // ++ create the searchObject and init sorting ++//
//            HibernateSearchObject<Tfeedbackalumni> soFeedbackAlumni = new HibernateSearchObject<Tfeedbackalumni>(Tfeedbackalumni.class, getFeedbackAlumniListCtrl().getCountRows());
//            soFeedbackAlumni.addFilter(new Filter("mmahasiswa.cnim", fa.getMmahasiswa().getCnim() , Filter.OP_EQUAL));
//            soFeedbackAlumni.addFilter(new Filter("cterm", fa.getCterm() , Filter.OP_EQUAL));
//            soFeedbackAlumni.addSort("nnopertanyaan", false);

            // Change the BindingListModel.
        if (getFeedbackAlumniDetailCtrl().getBinder() != null) {

            getFeedbackAlumniDetailCtrl().getPagedBindingListWrapper().clear();
//            getFeedbackAlumniDetailCtrl().getPagedBindingListWrapper().setSearchObject(soFeedbackAlumni);
            getFeedbackAlumniDetailCtrl().setRadioOnListBox(getList(fa));

            // get the current Tab for later checking if we must change it
            Tab currentTab = tabbox_FeedbackAlumniMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabFeedbackAlumniDetail)) {
                tabFeedbackAlumniDetail.setSelected(true);
            } else {
                currentTab.setSelected(true);
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
        if (getFeedbackAlumniDetailCtrl().getBinder() != null) {

            // refresh all dataBinder related controllers/components
            getFeedbackAlumniDetailCtrl().getBinder().loadAll();

            // set editable Mode
            getFeedbackAlumniDetailCtrl().doReadOnlyMode(true);

            btnCtrlFeedbackAlumni.setInitEdit();
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
    private void doEdit(Event event)  {
        // logger.debug(event.toString());

        // get the current Tab for later checking if we must change it
        Tab currentTab = tabbox_FeedbackAlumniMain.getSelectedTab();

        // check first, if the tabs are created, if not than create it
        if (getFeedbackAlumniDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabFeedbackAlumniDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getFeedbackAlumniDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabFeedbackAlumniDetail, null));
        }

        // check if the tab is one of the Detail tabs. If so do not change the
        // selection of it
        if (!currentTab.equals(tabFeedbackAlumniDetail)) {
            tabFeedbackAlumniDetail.setSelected(true);
        } else {
            currentTab.setSelected(true);
        }

        try {
            doSearchDetail(getSelectedFeedbackAlumni());
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        getFeedbackAlumniDetailCtrl().getBinder().loadAll();

        // remember the old vars
        doStoreInitValues();

        btnCtrlFeedbackAlumni.setBtnStatus_Edit();

        getFeedbackAlumniDetailCtrl().doReadOnlyMode(false);
        // set focus
        //getFeedbackAlumniDetailCtrl().txtb_jawaban.focus();
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
        if (getFeedbackAlumniDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabFeedbackAlumniDetail, null));
        }

        // check first, if the tabs are created
        if (getFeedbackAlumniDetailCtrl().getBinder() == null) {
            return;
        }

        final Tfeedbackalumni anFeedbackAlumni = getSelectedFeedbackAlumni();
        if (anFeedbackAlumni != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anFeedbackAlumni.getMmahasiswa().getCnama();
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
                     * Do not allow to modify the demo feedbackAlumnis
                     */
                    if (getFeedbackAlumniDetailCtrl().getFeedbackAlumni().getId() <= 2) {
                        try {
                            ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
                            return;
                        } catch (final InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        try {
                            getFeedbackAlumniService().delete(anFeedbackAlumni);
                        } catch (DataAccessException e) {
                            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                        }
                    }
                }

            }

            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrlFeedbackAlumni.setInitEdit();

        setSelectedFeedbackAlumni(null);
        // refresh the list
        getFeedbackAlumniListCtrl().doFillListbox();

        // refresh all dataBinder related controllers
        getFeedbackAlumniDetailCtrl().getBinder().loadAll();
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
        getFeedbackAlumniDetailCtrl().getBinder().saveAll();

        try {

            /**
             * Create list of FeedbackAlumni based on master feedback
             */
            /*setSelectedFeedbackAlumniList(new ArrayList<Tfeedbackalumni>());
            for (Mfeedback mf : (List<Mfeedback>) getFeedbacks()) {
                final Tfeedbackalumni fa = getFeedbackAlumniService().getNewFeedbackAlumni();
                fa.setMmahasiswa(getFeedbackAlumniDetailCtrl().getFeedbackAlumni().getMmahasiswa());
                fa.setCterm(getFeedbackAlumniDetailCtrl().getFeedbackAlumni().getCterm());
                fa.setCkelompok(getFeedbackAlumniDetailCtrl().getFeedbackAlumni().getCkelompok());
                fa.setMprodi(getFeedbackAlumniDetailCtrl().getFeedbackAlumni().getMprodi());
                fa.setMsekolah(getFeedbackAlumniDetailCtrl().getFeedbackAlumni().getMsekolah());
                fa.setMfeedback(mf);
                fa.setCjawaban("1");
                getSelectedFeedbackAlumniList().add(fa);
            } */
            getFeedbackAlumniDetailCtrl().setSelectedFeedbackAlumni(getSelectedFeedbackAlumni());

//            if (getFeedbackAlumniDetailCtrl().getBinder() != null) {
//                getFeedbackAlumniDetailCtrl().getBinder().loadAll();
//            }

//            for (Tfeedbackalumni tfa : getFeedbackAlumniDetailCtrl().getFeedbackAlumniList()) {
//                tfa.setMmahasiswa(getFeedbackAlumniDetailCtrl().getSelectedFeedbackAlumni().getMmahasiswa());
//                tfa.setCterm(getFeedbackAlumniDetailCtrl().getSelectedFeedbackAlumni().getCterm());
//                tfa.setCkelompok(getFeedbackAlumniDetailCtrl().getSelectedFeedbackAlumni().getCkelompok());
//                tfa.setMprodi(getFeedbackAlumniDetailCtrl().getSelectedFeedbackAlumni().getMprodi());
//                tfa.setMsekolah(getFeedbackAlumniDetailCtrl().getSelectedFeedbackAlumni().getMsekolah());
//                //tfa.setCjawaban();
//            }

            // save it to database
            //getFeedbackAlumniService().saveOrUpdate(getFeedbackAlumniDetailCtrl().getFeedbackAlumni());
            getFeedbackAlumniService().saveOrUpdateList(getFeedbackAlumniDetailCtrl().setlbtolist(getFeedbackAlumniDetailCtrl().getFeedbackAlumniList()));
            // if saving is successfully than actualize the beans as
            // origins.
            doStoreInitValues();
            // refresh the list
            getFeedbackAlumniListCtrl().doFillListbox();
//            later refresh StatusBar

            Events.postEvent("onSelect", getFeedbackAlumniListCtrl().getListBoxFeedbackAlumni(), getSelectedFeedbackAlumni());

            // show the objects data in the statusBar
            String str = getSelectedFeedbackAlumni().getCterm();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));




        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetToInitValues();

            return;

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            btnCtrlFeedbackAlumni.setInitEdit();
            getFeedbackAlumniDetailCtrl().doReadOnlyMode(true);
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
        if (getFeedbackAlumniDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabFeedbackAlumniDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getFeedbackAlumniDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabFeedbackAlumniDetail, null));
        }

        // remember the current object
        doStoreInitValues();

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        final Tfeedbackalumni anFeedbackAlumni = getFeedbackAlumniService().getNewFeedbackAlumni();
        List<Tfeedbackalumni> listFeedbackAlumni = getFeedbackAlumniService().getNewFeedbackAlumniList();
        // set the beans in the related databinded controllers
        getFeedbackAlumniDetailCtrl().setFeedbackAlumni(anFeedbackAlumni);
        getFeedbackAlumniDetailCtrl().setSelectedFeedbackAlumni(anFeedbackAlumni);

        getFeedbackAlumniDetailCtrl().setFeedbackAlumniList(listFeedbackAlumni);
        getFeedbackAlumniDetailCtrl().setSelectedFeedbackAlumniList(listFeedbackAlumni);

        // Refresh the binding mechanism
        getFeedbackAlumniDetailCtrl().setSelectedFeedbackAlumni(getSelectedFeedbackAlumni());
        getFeedbackAlumniDetailCtrl().setSelectedFeedbackAlumni(getSelectedFeedbackAlumni());
        if (getFeedbackAlumniDetailCtrl().getBinder() != null) {
            getFeedbackAlumniDetailCtrl().getBinder().loadAll();
        }

        getFeedbackAlumniDetailCtrl().setSelectedFeedbackAlumniList(getSelectedFeedbackAlumniList());
        getFeedbackAlumniDetailCtrl().setSelectedFeedbackAlumniList(getSelectedFeedbackAlumniList());
        if (getFeedbackAlumniDetailCtrl().getBinder() != null) {
            getFeedbackAlumniDetailCtrl().getBinder().loadAll();
        }
        PagedBindingListWrapper<Tfeedbackalumni> pblw = getFeedbackAlumniDetailCtrl().getPagedBindingListWrapper();
        pblw.clear();
        pblw.addAll(getSelectedFeedbackAlumniList());
        // set editable Mode
        getFeedbackAlumniDetailCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlFeedbackAlumni.setInitNew();

        tabFeedbackAlumniDetail.setSelected(true);


        // set focus
        //getFeedbackAlumniDetailCtrl().txtb_jawaban.focus();
        try {
        doResizeSelectedTab(null);
        getFeedbackAlumniDetailCtrl().doFillListbox();
        } catch (Exception e) {}
    }

    // set radio simpen disini tapi mengacu ke master feedback

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

        if (tabbox_FeedbackAlumniMain.getSelectedTab() == tabFeedbackAlumniDetail) {
            getFeedbackAlumniDetailCtrl().doFitSize(event);
        } else if (tabbox_FeedbackAlumniMain.getSelectedTab() == tabFeedbackAlumniList) {
            // resize and fill Listbox new
            getFeedbackAlumniListCtrl().doFillListbox();
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

        if (getSelectedFeedbackAlumni() != null) {

            try {
                setOriginalFeedbackAlumni((Tfeedbackalumni) ZksampleBeanUtils.cloneBean(getSelectedFeedbackAlumni()));
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

        if (getOriginalFeedbackAlumni() != null) {

            try {
                setSelectedFeedbackAlumni((Tfeedbackalumni) ZksampleBeanUtils.cloneBean(getOriginalFeedbackAlumni()));
                // TODO Bug in DataBinder??
                windowFeedbackAlumniMain.invalidate();

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

        // window_FeedbackAlumniList.setVisible(workspace.isAllowed("window_FeedbackAlumniList"));
        button_FeedbackAlumniList_PrintList.setVisible(workspace.isAllowed("button_FeedbackAlumniList_PrintList"));
        button_FeedbackAlumniList_SearchTerm.setVisible(workspace.isAllowed("button_FeedbackAlumniList_SearchNo"));
        button_FeedbackAlumniList_SearchKelompok.setVisible(workspace.isAllowed("button_FeedbackAlumniList_SearchName"));
        button_FeedbackAlumniList_SearchName.setVisible(workspace.isAllowed("button_FeedbackAlumniList_SearchCity"));

        btnHelp.setVisible(workspace.isAllowed("button_FeedbackAlumniMain_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_FeedbackAlumniMain_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_FeedbackAlumniMain_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_FeedbackAlumniMain_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_FeedbackAlumniMain_btnSave"));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public void setOriginalFeedbackAlumni(Tfeedbackalumni originalFeedbackAlumni) {
        this.originalFeedbackAlumni = originalFeedbackAlumni;
    }

    public Tfeedbackalumni getOriginalFeedbackAlumni() {
        return this.originalFeedbackAlumni;
    }

    public void setSelectedFeedbackAlumni(Tfeedbackalumni selectedFeedbackAlumni) {
        this.selectedFeedbackAlumni = selectedFeedbackAlumni;
        try {
        List<Tfeedbackalumni> ltf = this.selectedFeedbackAlumniList;
        for(Tfeedbackalumni tf : ltf) {
            tf.setMmahasiswa(selectedFeedbackAlumni.getMmahasiswa());
            tf.setCterm(selectedFeedbackAlumni.getCterm());
            tf.setCkelompok(selectedFeedbackAlumni.getCkelompok());
            tf.setMprodi(selectedFeedbackAlumni.getMprodi());
            tf.setMsekolah(selectedFeedbackAlumni.getMsekolah());
        }
        } catch (NullPointerException e) {}

    }

    public Tfeedbackalumni getSelectedFeedbackAlumni() {
        return this.selectedFeedbackAlumni;
    }

    public void setSelectedFeedbackAlumniList(List<Tfeedbackalumni> selectedFeedbackAlumniList) {
        this.selectedFeedbackAlumniList = selectedFeedbackAlumniList;
    }

    public List<Tfeedbackalumni> getSelectedFeedbackAlumniList() {
        return this.selectedFeedbackAlumniList;
    }

    public void setFeedbackAlumnis(BindingListModelList feedbackAlumnis) {
        this.feedbackAlumnis = feedbackAlumnis;
    }

    public BindingListModelList getFeedbackAlumnis() {
        return this.feedbackAlumnis;
    }

    public void setFeedbackAlumniService(FeedbackAlumniService feedbackAlumniService) {
        this.feedbackAlumniService = feedbackAlumniService;
    }

    public FeedbackAlumniService getFeedbackAlumniService() {
        return this.feedbackAlumniService;
    }

    public void setFeedbackAlumniListCtrl(FeedbackAlumniListCtrl feedbackAlumniListCtrl) {
        this.feedbackAlumniListCtrl = feedbackAlumniListCtrl;
    }

    public FeedbackAlumniListCtrl getFeedbackAlumniListCtrl() {
        return this.feedbackAlumniListCtrl;
    }

    public void setFeedbackAlumniDetailCtrl(FeedbackAlumniDetailCtrl feedbackAlumniDetailCtrl) {
        this.feedbackAlumniDetailCtrl = feedbackAlumniDetailCtrl;
    }

    public FeedbackAlumniDetailCtrl getFeedbackAlumniDetailCtrl() {
        return this.feedbackAlumniDetailCtrl;
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
