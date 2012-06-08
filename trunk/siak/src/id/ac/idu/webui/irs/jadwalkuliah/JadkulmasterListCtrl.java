package id.ac.idu.webui.irs.jadwalkuliah;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.backend.model.Tjadkuldetil;
import id.ac.idu.backend.model.Tjadkulmaster;
import id.ac.idu.backend.service.BrancheService;
import id.ac.idu.backend.service.CustomerService;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.irs.service.JadkulService;
import id.ac.idu.webui.util.GFCBaseListCtrl;
import id.ac.idu.webui.util.ZksampleMessageUtils;
import id.ac.idu.webui.util.pagging.PagedListWrapper;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
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
    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window tjadkulmasterListWindow; // autowired
    protected Paging paging_TjadkulmasterList; // autowired
    protected Listbox listBoxTjadkulmaster; // autowired
    protected Listheader listheader_TjadkulmasterList_TjadkulmasterNo; // autowired
    protected Listheader listheader_TjadkulmasterList_OderDescr; // autowired
    protected Listheader listheader_TjadkulmasterList_Pegawai1; // autowired
    protected Listheader listheader_TjadkulmasterList_Pegawai2; // autowired
    protected Listheader listheader_TjadkulmasterList_Matakuliah; // autowired
    protected Listheader listheader_TjadkulmasterList_Term; // autowired
    protected Listheader listheader_TjadkulmasterList_Kelompok; // autowired
    protected Listheader listheader_TjadkulmasterList_Tahunajaran; // autowired
    protected Listheader listheader_TjadkulmasterList_Semester; // autowired
    protected Listheader listheader_TjadkulmasterList_Sks; // autowired
    protected Paging paging_TjadkulmasterArticleList; // autowire
    protected Listbox listBoxTjadkulmasterArticle; // autowired
    protected Button btnHelp; // autowired
    protected Button button_TjadkulmasterList_NewTjadkulmaster; // autowired

    protected Listheader listheader_TjadkulmasterPosList_Tjadkulmasterpos_Hari; // autowired
    protected Listheader listheader_TjadkulmasterPosList_Sesi; // autowired
    protected Listheader listheader_TjadkulmasterPosList_Ruang; // autowired
    protected Listheader listheader_TjadkulmasterPosList_Jumlahsesi; // autowired
    protected Listheader listheader_TjadkulmasterPosList_Maks; // autowired
    protected Listheader listheader_TjadkulmasterPosList_Isi; // autowired

    private transient int pageSizeTjadkulmasters;
    private int pageSizeTjadkuldetils;

    // ServiceDAOs / Domain Classes
    private Tjadkulmaster tjadkulmaster;
    private transient JadkulService jadkulService;
    private transient CustomerService customerService;
    private transient BrancheService brancheService;

    // filter components
    protected Checkbox checkbox_KegiatanList_ShowAll; // autowired
    protected Textbox txtb_Kegiatan_No; // aurowired
    protected Button button_KegiatanList_SearchNo; // aurowired
    protected Textbox txtb_Dosen2; // aurowired
    protected Button button_Dosen2; // aurowired
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
        /* set comps cisible dependent of the users rights */
//        doCheckRights();

        // get the params map that are overhanded by creation.
        Map<String, Object> args = getCreationArgsMap(event);

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
        // ++ create the searchObject and init sorting ++//
        // only in sample app init with all tjadkulmasters
        HibernateSearchObject<Tjadkulmaster> soTjadkulmaster = new HibernateSearchObject<Tjadkulmaster>(Tjadkulmaster.class, getPageSizeTjadkulmasters());
        soTjadkulmaster.addSort("id", false);

        // set the paging params
        paging_TjadkulmasterList.setPageSize(getPageSizeTjadkulmasters());
        paging_TjadkulmasterList.setDetailed(true);

        // Sort header
        listheader_TjadkulmasterList_TjadkulmasterNo.setSortAscending(new FieldComparator("msekolah.cnamaSekolah", true));
        listheader_TjadkulmasterList_TjadkulmasterNo.setSortDescending(new FieldComparator("msekolah.cnamaSekolah", false));
        listheader_TjadkulmasterList_OderDescr.setSortAscending(new FieldComparator("mprodi.cnmprogst", true));
        listheader_TjadkulmasterList_OderDescr.setSortDescending(new FieldComparator("mprodi.cnmprogst", false));
        listheader_TjadkulmasterList_Pegawai1.setSortAscending(new FieldComparator("mpegawai1.cnama", true));
        listheader_TjadkulmasterList_Pegawai1.setSortDescending(new FieldComparator("mpegawai1.cnama", false));
        listheader_TjadkulmasterList_Pegawai2.setSortAscending(new FieldComparator("mpegawai2.cnama", true));
        listheader_TjadkulmasterList_Pegawai2.setSortDescending(new FieldComparator("mpegawai2.cnama", false));
        listheader_TjadkulmasterList_Matakuliah.setSortAscending(new FieldComparator("mtbmtkl.cnamamk", true));
        listheader_TjadkulmasterList_Matakuliah.setSortDescending(new FieldComparator("mtbmtkl.cnamamk", false));
        listheader_TjadkulmasterList_Term.setSortAscending(new FieldComparator("cterm", true));
        listheader_TjadkulmasterList_Term.setSortDescending(new FieldComparator("cterm", false));
        listheader_TjadkulmasterList_Kelompok.setSortAscending(new FieldComparator("ckelompok", true));
        listheader_TjadkulmasterList_Kelompok.setSortDescending(new FieldComparator("ckelompok", false));
        listheader_TjadkulmasterList_Tahunajaran.setSortAscending(new FieldComparator("cthajar", true));
        listheader_TjadkulmasterList_Tahunajaran.setSortDescending(new FieldComparator("cthajar", false));
        listheader_TjadkulmasterList_Semester.setSortAscending(new FieldComparator("csmt", true));
        listheader_TjadkulmasterList_Semester.setSortDescending(new FieldComparator("csmt", false));
        listheader_TjadkulmasterList_Sks.setSortAscending(new FieldComparator("nsks", true));
        listheader_TjadkulmasterList_Sks.setSortDescending(new FieldComparator("nsks", false));

        // Sort detail
        listheader_TjadkulmasterPosList_Tjadkulmasterpos_Hari.setSortAscending(new FieldComparator("mhari.cnmhari", true));
        listheader_TjadkulmasterPosList_Tjadkulmasterpos_Hari.setSortDescending(new FieldComparator("mhari.cnmhari", false));
        listheader_TjadkulmasterPosList_Sesi.setSortAscending(new FieldComparator("msesikuliah.ckdsesi", true));
        listheader_TjadkulmasterPosList_Sesi.setSortDescending(new FieldComparator("msesikuliah.ckdsesi", false));
        listheader_TjadkulmasterPosList_Ruang.setSortAscending(new FieldComparator("mruang.cnmRuang", true));
        listheader_TjadkulmasterPosList_Ruang.setSortDescending(new FieldComparator("mruang.cnmRuang", false));
        listheader_TjadkulmasterPosList_Jumlahsesi.setSortAscending(new FieldComparator("njmlsesi", true));
        listheader_TjadkulmasterPosList_Jumlahsesi.setSortDescending(new FieldComparator("njmlsesi", false));
        listheader_TjadkulmasterPosList_Maks.setSortAscending(new FieldComparator("nmaks", true));
        listheader_TjadkulmasterPosList_Maks.setSortDescending(new FieldComparator("nmaks", false));
        listheader_TjadkulmasterPosList_Isi.setSortAscending(new FieldComparator("nisi", true));
        listheader_TjadkulmasterPosList_Isi.setSortDescending(new FieldComparator("nisi", false));

        paging_TjadkulmasterArticleList.setPageSize(getPageSizeTjadkuldetils());
        paging_TjadkulmasterArticleList.setDetailed(true);

        getPlwTjadkulmasters().init(soTjadkulmaster, listBoxTjadkulmaster, paging_TjadkulmasterList);
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
        Listitem item = this.listBoxTjadkulmaster.getSelectedItem();

        if (item != null) {
            // CAST AND STORE THE SELECTED OBJECT
            Tjadkulmaster tjadkulmaster = (Tjadkulmaster) item.getAttribute("data");

            if (tjadkulmaster != null) {
                // Set the ListModel and the itemRenderer for the tjadkulmaster
                HibernateSearchObject<Tjadkuldetil> soTjadkuldetil = new HibernateSearchObject<Tjadkuldetil>(Tjadkuldetil.class, getPageSizeTjadkuldetils());
                soTjadkuldetil.addFilter(new Filter("tjadkulmaster", tjadkulmaster, Filter.OP_EQUAL));
                // deeper loading of the relation to prevent the lazy loading
                // problem.
                soTjadkuldetil.addFetch("mruang");

                // Set the ListModel.
                getPlwTjadkuldetils().init(soTjadkuldetil, listBoxTjadkulmasterArticle, paging_TjadkulmasterArticleList);
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
        ZksampleMessageUtils.doShowNotImplementedMessage();
    }

    /**
     * when the "help" button is clicked. <br>
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$btnHelp(Event event) throws InterruptedException {
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
        paintComponents();
        tjadkulmasterListWindow.invalidate();
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_KegiatanList_ShowAll(Event event) {
        txtb_Kegiatan_No.setValue(""); // clear
        txtb_Kegiatan_Name.setValue(""); // clear
        txtb_Dosen2.setValue(""); // clear
        paintComponents();
    }

    /**
     * Filter the kegiatan list with 'like kegiatan number'. <br>
     */
    public void onClick$button_KegiatanList_SearchNo(Event event) throws Exception {
        // if not empty
        if (!txtb_Kegiatan_No.getValue().isEmpty()) {
            checkbox_KegiatanList_ShowAll.setChecked(false); // unCheck
            txtb_Kegiatan_Name.setValue(""); // clear
            txtb_Dosen2.setValue(""); // clear

            HibernateSearchObject<Tjadkulmaster> soTjadkulmaster = new HibernateSearchObject<Tjadkulmaster>(Tjadkulmaster.class, getPageSizeTjadkulmasters());
            soTjadkulmaster.addFilter(new Filter("msekolah.cnamaSekolah", "%" + txtb_Kegiatan_No.getValue() + "%", Filter.OP_ILIKE));
            soTjadkulmaster.addSort("id", false);

            paging_TjadkulmasterList.setPageSize(getPageSizeTjadkulmasters());
            paging_TjadkulmasterList.setDetailed(true);

            paging_TjadkulmasterArticleList.setPageSize(getPageSizeTjadkuldetils());
            paging_TjadkulmasterArticleList.setDetailed(true);

            getPlwTjadkulmasters().init(soTjadkulmaster, listBoxTjadkulmaster, paging_TjadkulmasterList);
            listBoxTjadkulmaster.setItemRenderer(new JadkulmasterListItemRenderer());
            listBoxTjadkulmasterArticle.setItemRenderer(new JadkuldetilListItemRenderer());

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
                }
            }

        }
    }

    /**
     * Filter the kegiatan list with 'like kegiatan name'. <br>
     */
    public void onClick$button_KegiatanList_SearchName(Event event) throws Exception {
        // if not empty
        if (!txtb_Kegiatan_Name.getValue().isEmpty()) {
            checkbox_KegiatanList_ShowAll.setChecked(false); // unCheck
            txtb_Kegiatan_No.setValue(""); // clear
            txtb_Dosen2.setValue("");

            HibernateSearchObject<Tjadkulmaster> soTjadkulmaster = new HibernateSearchObject<Tjadkulmaster>(Tjadkulmaster.class, getPageSizeTjadkulmasters());
            soTjadkulmaster.addFilter(new Filter("mpegawai1.cnama", "%" + txtb_Kegiatan_Name.getValue() + "%", Filter.OP_ILIKE));
            soTjadkulmaster.addSort("id", false);

            paging_TjadkulmasterList.setPageSize(getPageSizeTjadkulmasters());
            paging_TjadkulmasterList.setDetailed(true);

            paging_TjadkulmasterArticleList.setPageSize(getPageSizeTjadkuldetils());
            paging_TjadkulmasterArticleList.setDetailed(true);

            getPlwTjadkulmasters().init(soTjadkulmaster, listBoxTjadkulmaster, paging_TjadkulmasterList);
            listBoxTjadkulmaster.setItemRenderer(new JadkulmasterListItemRenderer());
            listBoxTjadkulmasterArticle.setItemRenderer(new JadkuldetilListItemRenderer());

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
                }
            }
        }
    }

    /**
     * Filter the kegiatan list with 'like kegiatan name'. <br>
     */
    public void onClick$button_Dosen2(Event event) throws Exception {
        // if not empty
        if (!txtb_Dosen2.getValue().isEmpty()) {
            checkbox_KegiatanList_ShowAll.setChecked(false); // unCheck
            txtb_Kegiatan_No.setValue(""); // clear
            txtb_Kegiatan_Name.setValue(""); // clear

            HibernateSearchObject<Tjadkulmaster> soTjadkulmaster = new HibernateSearchObject<Tjadkulmaster>(Tjadkulmaster.class, getPageSizeTjadkulmasters());
            soTjadkulmaster.addFilter(new Filter("mpegawai2.cnama", "%" + txtb_Dosen2.getValue() + "%", Filter.OP_ILIKE));
            soTjadkulmaster.addSort("id", false);

            paging_TjadkulmasterList.setPageSize(getPageSizeTjadkulmasters());
            paging_TjadkulmasterList.setDetailed(true);

            paging_TjadkulmasterArticleList.setPageSize(getPageSizeTjadkuldetils());
            paging_TjadkulmasterArticleList.setDetailed(true);

            getPlwTjadkulmasters().init(soTjadkulmaster, listBoxTjadkulmaster, paging_TjadkulmasterList);
            listBoxTjadkulmaster.setItemRenderer(new JadkulmasterListItemRenderer());
            listBoxTjadkulmasterArticle.setItemRenderer(new JadkuldetilListItemRenderer());

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
                }
            }
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
    }

    /**
     * when the "search/filter" button is clicked.
     *
     * @param event
     */
    public void onClick$button_bbox_CustomerSearch_Search(Event event) {
        doSearch();
    }

    public void onOpen$bandbox_TjadkulmasterList_CustomerSearch(Event event) throws Exception {
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
    }

    /**
     * when doubleClick on a item in the customerSearch listbox.<br>
     * <br>
     * Select the customer and search all tjadkulmasters for him.
     *
     * @param event
     */
    public void onDoubleClickedCustomerItem(Event event) {
    }

    public void onClick$button_PrintList(Event event) throws InterruptedException {
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
