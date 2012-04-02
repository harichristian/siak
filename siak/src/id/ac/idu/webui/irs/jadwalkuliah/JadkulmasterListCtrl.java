package id.ac.idu.webui.irs.jadwalkuliah;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.backend.model.Tjadkuldetil;
import id.ac.idu.backend.model.Tjadkulmaster;
import id.ac.idu.backend.service.BrancheService;
import id.ac.idu.backend.service.CustomerService;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.irs.service.JadkulService;
import id.ac.idu.webui.administrasi.report.MpegawaiSimpleDJReport;
import id.ac.idu.webui.irs.report.JadkulmasterSimpleDJReport;
import id.ac.idu.webui.util.GFCBaseListCtrl;
import id.ac.idu.webui.util.ZksampleMessageUtils;
import id.ac.idu.webui.util.pagging.PagedListWrapper;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 11/03/12
 * Time: 19:03
 * To change this template use File | Settings | File Templates.
 */
public class JadkulmasterListCtrl extends GFCBaseListCtrl<Tjadkulmaster> implements Serializable {

    private static final long serialVersionUID = 5710086946825179284L;
    private static final Logger logger = Logger.getLogger(JadkulmasterListCtrl.class);

    private PagedListWrapper<Tjadkulmaster> plwTjadkulmasters;
    private PagedListWrapper<Tjadkuldetil> plwTjadkuldetils;
    //    private PagedListWrapper<Customer> plwCustomers;
    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window tjadkulmasterListWindow; // autowired
    // Listbox tjadkulmasters
    protected Paging paging_TjadkulmasterList; // autowired
    protected Listbox listBoxTjadkulmaster; // autowired
    protected Listheader listheader_TjadkulmasterList_TjadkulmasterNo; // autowired
    protected Listheader listheader_TjadkulmasterList_OderDescr; // autowired
    // Listbox tjadkulmasterPositions
    protected Paging paging_TjadkulmasterArticleList; // autowire
    protected Listbox listBoxTjadkulmasterArticle; // autowired
//    protected Listheader listheader_TjadkulmasterPosList_Tjadkulmasterpos_No; // autowired
//    protected Listheader listheader_TjadkulmasterPosList_Shorttext; // autowired
//    protected Listheader listheader_TjadkulmasterPosList_Count; // autowired
//    protected Listheader listheader_TjadkulmasterPosList_SinglePrice; // autowired
//    protected Listheader listheader_TjadkulmasterPosList_WholePrice; // autowired

//    protected Listfooter listfooter_TjadkulmasterPosList_Count; // autowired
//    protected Listfooter listfooter_TjadkulmasterPosList_WholePrice; // autowired

//    protected Hbox hBoxCustomerSearch; // autowired

    // bandbox searchCustomer
//    protected Bandbox bandbox_TjadkulmasterList_CustomerSearch;
//    protected Textbox tb_Tjadkulmasters_SearchCustNo; // autowired
//    protected Textbox tb_Tjadkulmasters_CustSearchMatchcode; // autowired
//    protected Textbox tb_Tjadkulmasters_SearchCustName1; // autowired
//    protected Textbox tb_Tjadkulmasters_SearchCustCity; // autowired
    // listbox searchCustomer
//    protected Paging paging_TjadkulmasterList_CustomerSearchList; // autowired
//    protected Listbox listBoxCustomerSearch; // autowired
//    transient protected Listheader listheader_CustNo; // autowired
//    protected Listheader listheader_CustMatchcode; // autowired
//    protected Listheader listheader_CustName1; // autowired
//    protected Listheader listheader_CustCity; // autowired

    // checkRights
    protected Button btnHelp; // autowired
    protected Button button_TjadkulmasterList_NewTjadkulmaster; // autowired

//    private transient HibernateSearchObject<Customer> searchObjCustomer;

    private transient int pageSizeTjadkulmasters;
    private int pageSizeTjadkuldetils;
//    private final int pageSizeSearchCustomers = 20;

    // ServiceDAOs / Domain Classes
//    private Customer customer;
    private Tjadkulmaster tjadkulmaster;
    private transient JadkulService jadkulService;
    private transient CustomerService customerService;
    private transient BrancheService brancheService;

    // filter components
    protected Checkbox checkbox_KegiatanList_ShowAll; // autowired
    protected Textbox txtb_Kegiatan_No; // aurowired
    protected Button button_KegiatanList_SearchNo; // aurowired
    protected Textbox txtb_Kegiatan_Name; // aurowired
    protected Button button_KegiatanList_SearchName; // aurowired

    protected Button button_PrintList; // aurowired

    /**
     * default constructor.<br>
     */
    public JadkulmasterListCtrl() {
        super();
    }

    public void onCreate$tjadkulmasterListWindow(Event event) throws Exception {
        /* autowire comps the vars */
        // doOnCreateCommon(tjadkulmasterListWindow, event);

        /* set comps cisible dependent of the users rights */
//        doCheckRights();

        // get the params map that are overhanded by creation.
        Map<String, Object> args = getCreationArgsMap(event);

        // check if the tjadkulmasterList is called with a customer param
//        if (args.containsKey("customerDialogCtrl")) {
//            hBoxCustomerSearch.setVisible(false);
//        } else {
//            hBoxCustomerSearch.setVisible(true);
//        }

        // check if the tjadkulmasterList is called with a customer param
//        if (args.containsKey("customer")) {
//            setCustomer((Customer) args.get("customer"));
//        } else {
//            setCustomer(null);
//        }

        // check if the tjadkulmasterList is called from the Customer Dialog
        // and set the pageSizes
        if (args.containsKey("rowSizeTjadkulmasters")) {
            int rowSize = (Integer) args.get("rowSizeTjadkulmasters");
            setPageSizeTjadkulmasters(rowSize);
        } else {
            setPageSizeTjadkulmasters(15);
        }
        if (args.containsKey("rowSizeTjadkuldetils")) {
            int rowSize = (Integer) args.get("rowSizeTjadkuldetils");
            setPageSizeTjadkuldetils(rowSize);
        } else {
            setPageSizeTjadkuldetils(15);
        }

        paintComponents();

    }

    private void paintComponents() {
        // set the bandbox to readonly
//        bandbox_TjadkulmasterList_CustomerSearch.setReadonly(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
//        listheader_TjadkulmasterList_TjadkulmasterNo.setSortAscending(new FieldComparator("aufNr", true));
//        listheader_TjadkulmasterList_TjadkulmasterNo.setSortDescending(new FieldComparator("aufNr", false));
//        listheader_TjadkulmasterList_OderDescr.setSortAscending(new FieldComparator("aufBezeichnung", true));
//        listheader_TjadkulmasterList_OderDescr.setSortDescending(new FieldComparator("aufBezeichnung", false));

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
//        listheader_TjadkulmasterPosList_Tjadkulmasterpos_No.setSortAscending(new FieldComparator("aupPosition", true));
//        listheader_TjadkulmasterPosList_Tjadkulmasterpos_No.setSortDescending(new FieldComparator("aupPosition", false));
//        listheader_TjadkulmasterPosList_Shorttext.setSortAscending(new FieldComparator("mruang.artKurzbezeichnung", true));
//        listheader_TjadkulmasterPosList_Shorttext.setSortDescending(new FieldComparator("mruang.artKurzbezeichnung", false));
//        listheader_TjadkulmasterPosList_Count.setSortAscending(new FieldComparator("aupMenge", true));
//        listheader_TjadkulmasterPosList_Count.setSortDescending(new FieldComparator("aupMenge", false));
//        listheader_TjadkulmasterPosList_SinglePrice.setSortAscending(new FieldComparator("aupEinzelwert", true));
//        listheader_TjadkulmasterPosList_SinglePrice.setSortDescending(new FieldComparator("aupEinzelwert", false));
//        listheader_TjadkulmasterPosList_WholePrice.setSortAscending(new FieldComparator("aupGesamtwert", true));
//        listheader_TjadkulmasterPosList_WholePrice.setSortDescending(new FieldComparator("aupGesamtwert", false));

        // ++ create the searchObject and init sorting ++//
        // only in sample app init with all tjadkulmasters
        HibernateSearchObject<Tjadkulmaster> soTjadkulmaster = new HibernateSearchObject<Tjadkulmaster>(Tjadkulmaster.class, getPageSizeTjadkulmasters());
        soTjadkulmaster.addSort("id", false);

        // set the paging params
        paging_TjadkulmasterList.setPageSize(getPageSizeTjadkulmasters());
        paging_TjadkulmasterList.setDetailed(true);

        paging_TjadkulmasterArticleList.setPageSize(getPageSizeTjadkuldetils());
        paging_TjadkulmasterArticleList.setDetailed(true);

        // Set the ListModel for the tjadkulmasters.
//        if (customer == null) {
//            // Set the ListModel.
//            getPlwTjadkulmasters().init(soTjadkulmaster, listBoxTjadkulmaster, paging_TjadkulmasterList);
//        } else {
//            soTjadkulmaster.addFilter(new Filter("customer", customer, Filter.OP_EQUAL));

        // Set the ListModel.
        getPlwTjadkulmasters().init(soTjadkulmaster, listBoxTjadkulmaster, paging_TjadkulmasterList);
//        }
        listBoxTjadkulmaster.setItemRenderer(new JadkulmasterListItemRenderer());

        listBoxTjadkulmasterArticle.setItemRenderer(new JadkuldetilListItemRenderer());

        // init the first entry
        ListModelList lml = (ListModelList) listBoxTjadkulmaster.getModel();

        // Now we would show the corresponding detail list of the first
        // selected entry of the MASTER Table
        // We became not the first item FROM the list because it's not
        // rendered at this time.
        // So we take the first entry in the ListModelList and set as
        // selected.
        if (lml.getSize() > 0) {
            int rowIndex = 0;
            listBoxTjadkulmaster.setSelectedIndex(rowIndex);
            // get the first entry and cast them to the needed object
            Tjadkulmaster anTjadkulmaster = (Tjadkulmaster) lml.get(rowIndex);
            if (anTjadkulmaster != null) {
                // get the related tjadkulmaster positions
                HibernateSearchObject<Tjadkuldetil> soTjadkuldetil = new HibernateSearchObject<Tjadkuldetil>(Tjadkuldetil.class, getPageSizeTjadkuldetils());
                soTjadkuldetil.addFilter(new Filter("tjadkulmaster", anTjadkulmaster, Filter.OP_EQUAL));
                // deeper loading of the relation to prevent the lazy
                // loading problem.
                soTjadkuldetil.addFetch("mruang");

                // Set the ListModel.
                getPlwTjadkuldetils().init(soTjadkuldetil, listBoxTjadkulmasterArticle, paging_TjadkulmasterArticleList);

                /** +++ get the SUM of the tjadkulmasterpositions for the ListFooter +++ */
//                String s = String.valueOf(getJadkulService().getTjadkulmasterSum(anTjadkulmaster));
//                if (s != "null") {
//                    listfooter_TjadkulmasterPosList_WholePrice.setLabel(s);
//                    // listfooter_TjadkulmasterPosList_WholePrice.setLabel(String.valueOf(getJadkulService().getTjadkulmasterSum(anTjadkulmaster)));
//                } else
//                    listfooter_TjadkulmasterPosList_WholePrice.setLabel("0.00");

            }
        }
    }

    /**
     * SetVisible for components by checking if there's a right for it.
     */
    private void doCheckRights() {

        final UserWorkspace workspace = getUserWorkspace();

        tjadkulmasterListWindow.setVisible(workspace.isAllowed("tjadkulmasterListWindow"));

        btnHelp.setVisible(workspace.isAllowed("button_TjadkulmasterList_btnHelp"));
        button_TjadkulmasterList_NewTjadkulmaster.setVisible(workspace.isAllowed("button_TjadkulmasterList_NewTjadkulmaster"));

    }

    public void onSelect$listBoxTjadkulmaster(Event event) throws Exception {
        // logger.debug(event.toString());

        Listitem item = this.listBoxTjadkulmaster.getSelectedItem();

        if (item != null) {
            // CAST AND STORE THE SELECTED OBJECT
            Tjadkulmaster tjadkulmaster = (Tjadkulmaster) item.getAttribute("data");

            if (tjadkulmaster != null) {
                // Set the ListModel and the itemRenderer for the tjadkulmaster
                // mruangs.g

                HibernateSearchObject<Tjadkuldetil> soTjadkuldetil = new HibernateSearchObject<Tjadkuldetil>(Tjadkuldetil.class, getPageSizeTjadkuldetils());
                soTjadkuldetil.addFilter(new Filter("tjadkulmaster", tjadkulmaster, Filter.OP_EQUAL));
                // deeper loading of the relation to prevent the lazy loading
                // problem.
                soTjadkuldetil.addFetch("mruang");

                // Set the ListModel.
                getPlwTjadkuldetils().init(soTjadkuldetil, listBoxTjadkulmasterArticle, paging_TjadkulmasterArticleList);

                // +++ get the SUM of the tjadkulmasterpositions +++ //
//                String s = String.valueOf(getJadkulService().getTjadkulmasterSum(tjadkulmaster));
//                if (s != "null") {
//                    listfooter_TjadkulmasterPosList_WholePrice.setLabel(String.valueOf(getJadkulService().getTjadkulmasterSum(tjadkulmaster)));
//                } else
//                    listfooter_TjadkulmasterPosList_WholePrice.setLabel("0.00");

            }

        }

    }

    /**
     * Call the Tjadkulmaster dialog with the selected entry. <br>
     * <br>
     * This methode is forwarded from the listboxes item renderer. <br>
     * see: id.ac.idu.webui.tjadkulmaster.model.TjadkulmasterListModelItemRenderer.java <br>
     *
     * @param event
     * @throws Exception
     */
    public void onDoubleClickedTjadkulmasterItem(Event event) throws Exception {

        // get the selected object
        Listitem item = listBoxTjadkulmaster.getSelectedItem();

        if (item != null) {
            // CAST AND STORE THE SELECTED OBJECT
            Tjadkulmaster anTjadkulmaster = (Tjadkulmaster) item.getAttribute("data");

            showDetailView(anTjadkulmaster);

        }
    }

    /**
     * Call the tjadkulmaster dialog with a new empty entry. <br>
     */
    public void onClick$button_TjadkulmasterList_NewTjadkulmaster(Event event) throws Exception {
        // logger.debug(event.toString());

        // create a new tjadkulmaster object
        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        Tjadkulmaster anTjadkulmaster = getJadkulService().getNewTjadkulmaster();

        showDetailView(anTjadkulmaster);
    }

    /**
     * Opens the detail view. <br>
     * Overhanded some params in a map if needed. <br>
     *
     * @throws Exception
     */
    private void showDetailView(Tjadkulmaster anTjadkulmaster) throws Exception {
        /*
           * We can call our Dialog zul-file with parameters. So we can call them
           * with a object of the selected item. For handed over these parameter
           * only a Map is accepted. So we put the object in a HashMap.
           */
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("tjadkulmaster", anTjadkulmaster);
        /*
           * we can additionally handed over the listBox, so we have in the dialog
           * access to the listbox Listmodel. This is fine for syncronizing the
           * data in the customerListbox from the dialog when we do a delete, edit
           * or insert a customer.
           */
        map.put("listBoxTjadkulmaster", listBoxTjadkulmaster);
        map.put("jadkulmasterListCtrl", this);

        // call the zul-file with the parameters packed in a map
        try {
            Executions.createComponents("/WEB-INF/pages/irs/jadwalkuliah/jadkulmasterDialog.zul", null, map);
        } catch (final Exception e) {
            logger.error("onOpenWindow:: error opening window / " + e.getMessage());

            ZksampleMessageUtils.showErrorMessage(e.toString());
        }

    }

    /**
     * when the "Tjadkulmaster search" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_TjadkulmasterList_TjadkulmasterNameSearch(Event event) throws InterruptedException {
        // logger.debug(event.toString());

        ZksampleMessageUtils.doShowNotImplementedMessage();
    }

    /**
     * when the "help" button is clicked. <br>
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$btnHelp(Event event) throws InterruptedException {
        // logger.debug(event.toString());

        ZksampleMessageUtils.doShowNotImplementedMessage();
    }

    /**
     * when the "refresh" button is clicked. <br>
     * <br>
     * Refreshes the view by calling the onCreate event manually.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$btnRefresh(Event event) throws InterruptedException {
        // logger.debug(event.toString());

        paintComponents();
        tjadkulmasterListWindow.invalidate();
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
        HibernateSearchObject<Tjadkulmaster> soKegiatan = new HibernateSearchObject<Tjadkulmaster>(Tjadkulmaster.class, 10);
        soKegiatan.addSort("id", false);
        // Change the BindingListModel.
//        if (getKegiatanListCtrl().getBinder() != null) {
        this.getPagedBindingListWrapper().setSearchObject(soKegiatan);

        // get the current Tab for later checking if we must change it
//            Tab currentTab = tabbox_KegiatanMain.getSelectedTab();

        // check if the tab is one of the Detail tabs. If so do not
        // change the selection of it
//            if (!currentTab.equals(tabKegiatanList)) {
//                tabKegiatanList.setSelected(true);
//            } else {
//                currentTab.setSelected(true);
//            }
//        }

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
            HibernateSearchObject<Tjadkulmaster> soKegiatan = new HibernateSearchObject<Tjadkulmaster>(Tjadkulmaster.class, 10);
            soKegiatan.addFilter(new Filter("msekolah.cnamaSekolah", "%" + txtb_Kegiatan_No.getValue() + "%", Filter.OP_ILIKE));
            soKegiatan.addSort("msekolah.cnamaSekolah", false);

            // Change the BindingListModel.
//            if (getKegiatanListCtrl().getBinder() != null) {
            this.getPagedBindingListWrapper().setSearchObject(soKegiatan);

            // get the current Tab for later checking if we must change it
//                Tab currentTab = tabbox_KegiatanMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
//                if (!currentTab.equals(tabKegiatanList)) {
//                    tabKegiatanList.setSelected(true);
//                } else {
//                    currentTab.setSelected(true);
//                }
//            }
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
            HibernateSearchObject<Tjadkulmaster> soKegiatan = new HibernateSearchObject<Tjadkulmaster>(Tjadkulmaster.class, 10);
            soKegiatan.addFilter(new Filter("mpegawai1.cnama", "%" + txtb_Kegiatan_Name.getValue() + "%", Filter.OP_ILIKE));
            soKegiatan.addSort("mpegawai1.cnama", false);

            // Change the BindingListModel.
//            if (getKegiatanListCtrl().getBinder() != null) {
            this.getPagedBindingListWrapper().setSearchObject(soKegiatan);

            // get the current Tab for later checking if we must change it
//                Tab currentTab = tabbox_KegiatanMain.getSelectedTab();
//
//                // check if the tab is one of the Detail tabs. If so do not
//                // change the selection of it
//                if (!currentTab.equals(tabKegiatanList)) {
//                    tabKegiatanList.setSelected(true);
//                } else {
//                    currentTab.setSelected(true);
//                }
//            }
        }
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    // ++++++++++++++ bandbox search Customer +++++++++++++++//
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

    /**
     * when the "close" button of the search bandbox is clicked.
     *
     * @param event
     */
    public void onClick$button_bbox_CustomerSearch_Close(Event event) {
        // logger.debug(event.toString());

//        bandbox_TjadkulmasterList_CustomerSearch.close();
    }

    /**
     * when the "search/filter" button is clicked.
     *
     * @param event
     */
    public void onClick$button_bbox_CustomerSearch_Search(Event event) {
        // logger.debug(event.toString());

        doSearch();
    }

    public void onOpen$bandbox_TjadkulmasterList_CustomerSearch(Event event) throws Exception {
        // logger.debug(event.toString());

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
//        listheader_CustNo.setSortAscending(new FieldComparator("kunNr", true));
//        listheader_CustNo.setSortDescending(new FieldComparator("kunNr", false));
//        listheader_CustMatchcode.setSortAscending(new FieldComparator("kunMatchcode", true));
//        listheader_CustMatchcode.setSortDescending(new FieldComparator("kunMatchcode", false));
//        listheader_CustName1.setSortAscending(new FieldComparator("kunName1", true));
//        listheader_CustName1.setSortDescending(new FieldComparator("kunName1", false));
//        listheader_CustCity.setSortAscending(new FieldComparator("kunOrt", true));
//        listheader_CustCity.setSortDescending(new FieldComparator("kunOrt", false));

        // set the paging params
//        paging_TjadkulmasterList_CustomerSearchList.setPageSize(pageSizeSearchCustomers);
//        paging_TjadkulmasterList_CustomerSearchList.setDetailed(true);

        // ++ create the searchObject and init sorting ++ //
//        if (getSearchObjCustomer() == null) {
//            setSearchObjCustomer(new HibernateSearchObject<Customer>(Customer.class, pageSizeSearchCustomers));
//            getSearchObjCustomer().addSort("kunMatchcode", false);
//            setSearchObjCustomer(searchObjCustomer);
//        }

        // Set the ListModel.
//        getPlwCustomers().init(getSearchObjCustomer(), listBoxCustomerSearch, paging_TjadkulmasterList_CustomerSearchList);
        // set the itemRenderer
//        listBoxCustomerSearch.setItemRenderer(new TjadkulmasterSearchCustomerListModelItemRenderer());
    }

    /**
     * Search/filter data for the filled out fields<br>
     * <br>
     * 1. Checks for each textbox if there are a value. <br>
     * 2. Checks which operator is selected. <br>
     * 3. Store the filter and value in the searchObject. <br>
     * 4. Call the ServiceDAO method with searchObject as parameter. <br>
     */
    private void doSearch() {

//        searchObjCustomer = new HibernateSearchObject<Customer>(Customer.class, pageSizeSearchCustomers);
//
//        // check which field have input
//        if (StringUtils.isNotEmpty(tb_Tjadkulmasters_SearchCustNo.getValue())) {
//            searchObjCustomer.addFilter(new Filter("kunNr", tb_Tjadkulmasters_SearchCustNo.getValue(), Filter.OP_EQUAL));
//        }
//
//        if (StringUtils.isNotEmpty(tb_Tjadkulmasters_CustSearchMatchcode.getValue())) {
//            searchObjCustomer.addFilter(new Filter("kunMatchcode", "%" + tb_Tjadkulmasters_CustSearchMatchcode.getValue().toUpperCase() + "%", Filter.OP_ILIKE));
//        }
//
//        if (StringUtils.isNotEmpty(tb_Tjadkulmasters_SearchCustName1.getValue())) {
//            searchObjCustomer.addFilter(new Filter("kunName1", "%" + tb_Tjadkulmasters_SearchCustName1.getValue() + "%", Filter.OP_ILIKE));
//        }
//
//        if (StringUtils.isNotEmpty(tb_Tjadkulmasters_SearchCustCity.getValue())) {
//            searchObjCustomer.addFilter(new Filter("kunOrt", "%" + tb_Tjadkulmasters_SearchCustCity.getValue() + "%", Filter.OP_ILIKE));
//        }
//
//        setSearchObjCustomer(this.searchObjCustomer);
//
//        // Set the ListModel.
//        getPlwCustomers().init(getSearchObjCustomer(), listBoxCustomerSearch, paging_TjadkulmasterList_CustomerSearchList);

    }

    /**
     * when doubleClick on a item in the customerSearch listbox.<br>
     * <br>
     * Select the customer and search all tjadkulmasters for him.
     *
     * @param event
     */
    public void onDoubleClickedCustomerItem(Event event) {
//        // logger.debug(event.toString());
//
//        // get the customer
//        Listitem item = this.listBoxCustomerSearch.getSelectedItem();
//        if (item != null) {
//
//            /* clear the listboxes from older stuff */
//            if ((ListModelList) listBoxTjadkulmaster.getModel() != null) {
//                ((ListModelList) listBoxTjadkulmaster.getModel()).clear();
//            }
//            if ((ListModelList) listBoxTjadkulmasterArticle.getModel() != null) {
//                ((ListModelList) listBoxTjadkulmasterArticle.getModel()).clear();
//            }
//
//            Customer customer = (Customer) item.getAttribute("data");
//
//            bandbox_TjadkulmasterList_CustomerSearch.setValue(customer.getKunName1() + ", " + customer.getKunOrt());
//
//            // get all tjadkulmasters for the selected customer
//            HibernateSearchObject<Tjadkulmaster> soTjadkulmaster = new HibernateSearchObject<Tjadkulmaster>(Tjadkulmaster.class, getPageSizeTjadkulmasters());
//            soTjadkulmaster.addSort("aufNr", false);
//            soTjadkulmaster.addFilter(new Filter("customer", customer, Filter.OP_EQUAL));
//
//            // Set the ListModel.
//            getPlwTjadkulmasters().init(soTjadkulmaster, listBoxTjadkulmaster, paging_TjadkulmasterList);
//
//            // get the first object and poll and show the tjadkulmasterpositions
//            ListModelList lml = (ListModelList) listBoxTjadkulmaster.getModel();
//
//            if (lml.size() > 0) {
//
//                Tjadkulmaster anTjadkulmaster = (Tjadkulmaster) lml.get(0);
//
//                if (anTjadkulmaster != null) {
//                    HibernateSearchObject<Tjadkuldetil> soTjadkuldetil = new HibernateSearchObject<Tjadkuldetil>(Tjadkuldetil.class, getPageSizeTjadkuldetils());
//                    soTjadkuldetil.addFilter(new Filter("tjadkulmaster", anTjadkulmaster, Filter.OP_EQUAL));
//                    // deeper loading of the relation to prevent the lazy
//                    // loading problem.
//                    soTjadkuldetil.addFetch("mruang");
//
//                    getPlwTjadkuldetils().init(soTjadkuldetil, listBoxTjadkulmasterArticle, paging_TjadkulmasterArticleList);
//                }
//            } else {
//                // get a new Tjadkulmaster for searching that the resultList is cleared
//                Tjadkulmaster anTjadkulmaster = getJadkulService().getNewTjadkulmaster();
//                HibernateSearchObject<Tjadkuldetil> soTjadkuldetil = new HibernateSearchObject<Tjadkuldetil>(Tjadkuldetil.class, getPageSizeTjadkuldetils());
//                soTjadkuldetil.addFilter(new Filter("tjadkulmaster", anTjadkulmaster, Filter.OP_EQUAL));
//                // deeper loading of the relation to prevent the lazy
//                // loading problem.
//                soTjadkuldetil.addFetch("mruang");
//
//                getPlwTjadkuldetils().init(soTjadkuldetil, listBoxTjadkulmasterArticle, paging_TjadkulmasterArticleList);
//            }
//        }
//
//        // close the bandbox
//        bandbox_TjadkulmasterList_CustomerSearch.close();

    }

    public void onClick$button_PrintList(Event event) throws InterruptedException {
		final Window win = (Window) Path.getComponent("/outerIndexWindow");
		new JadkulmasterSimpleDJReport(win);
	}

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    // ++++++++++++++++++ getter / setter +++++++++++++++++++//
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

    public void setTjadkulmaster(Tjadkulmaster tjadkulmaster) {
        this.tjadkulmaster = tjadkulmaster;
    }

    public Tjadkulmaster getTjadkulmaster() {
        return this.tjadkulmaster;
    }

    public BrancheService getBrancheService() {
        return this.brancheService;
    }

    public void setBrancheService(BrancheService brancheService) {
        this.brancheService = brancheService;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public CustomerService getCustomerService() {
        return this.customerService;
    }

    public void setJadkulService(JadkulService jadkulService) {
        this.jadkulService = jadkulService;
    }

    public JadkulService getJadkulService() {
        return this.jadkulService;
    }

    public void setPageSizeTjadkulmasters(int pageSizeTjadkulmasters) {
        this.pageSizeTjadkulmasters = pageSizeTjadkulmasters;
    }

    public int getPageSizeTjadkulmasters() {
        return this.pageSizeTjadkulmasters;
    }

    public void setPageSizeTjadkuldetils(int pageSizeTjadkuldetils) {
        this.pageSizeTjadkuldetils = pageSizeTjadkuldetils;
    }

    public int getPageSizeTjadkuldetils() {
        return this.pageSizeTjadkuldetils;
    }

    public Window getTjadkulmasterListWindow() {
        return this.tjadkulmasterListWindow;
    }

    public Listbox getListBoxTjadkulmasterArticle() {
        return this.listBoxTjadkulmasterArticle;
    }

    public void setPlwTjadkulmasters(PagedListWrapper<Tjadkulmaster> plwTjadkulmasters) {
        this.plwTjadkulmasters = plwTjadkulmasters;
    }

    public PagedListWrapper<Tjadkulmaster> getPlwTjadkulmasters() {
        return this.plwTjadkulmasters;
    }

    public void setPlwTjadkuldetils(PagedListWrapper<Tjadkuldetil> plwTjadkuldetils) {
        this.plwTjadkuldetils = plwTjadkuldetils;
    }

    public PagedListWrapper<Tjadkuldetil> getPlwTjadkuldetils() {
        return this.plwTjadkuldetils;
    }

}
