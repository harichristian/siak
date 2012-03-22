package id.ac.idu.webui.administrasi.sekolah;

import id.ac.idu.administrasi.service.SekolahService;
import id.ac.idu.backend.model.Msekolah;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.webui.util.GFCBaseListCtrl;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.*;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 08/03/12
 * Time: 0:48
 * To change this template use File | Settings | File Templates.
 */
public class SekolahListCtrl extends GFCBaseListCtrl<Msekolah> implements Serializable {
    private static final long serialVersionUID = -2170565288232491362L;
    private static final Logger logger = Logger.getLogger(SekolahListCtrl.class);

    /*
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    * All the components that are defined here and have a corresponding
    * component with the same 'id' in the zul-file are getting autowired by our
    * 'extends GFCBaseCtrl' GenericForwardComposer.
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    */
    protected Window windowSekolahList; // autowired
    protected Panel panelSekolahList; // autowired

    protected Borderlayout borderLayout_sekolahList; // autowired
    protected Paging paging_SekolahList; // autowired
    protected Listbox listBoxSekolah; // autowired
    protected Listheader listheader_SekolahList_No; // autowired
    protected Listheader listheader_SekolahList_Name1; // autowired
    protected Listheader listheader_SekolahList_Singkatan; // autowired
    protected Listheader listheader_SekolahList_NamaInggris; // autowired
    protected Listheader listheader_SekolahList_Visi; // autowired
    protected Listheader listheader_SekolahList_Misi; // autowired
    protected Listheader listheader_SekolahList_Pegawai; // autowired
    protected Listheader listheader_SekolahList_NoSk; // autowired

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Msekolah> searchObj;

    // row count for listbox
    private int countRows;

    // Databinding
    private AnnotateDataBinder binder;
    private SekolahMainCtrl sekolahMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient SekolahService sekolahService;

    /**
     * default constructor.<br>
     */
    public SekolahListCtrl() {
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

        /**
         * 1. Get the overhanded MainController.<br>
         * 2. Set this controller in the MainController.<br>
         * 3. Check if a 'selectedObject' exists yet in the MainController.<br>
         */
        if (arg.containsKey("ModuleMainController")) {
            setSekolahMainCtrl((SekolahMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getSekolahMainCtrl().setSekolahListCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getSekolahMainCtrl().getSelectedSekolah() != null) {
                setSelectedSekolah(getSekolahMainCtrl().getSelectedSekolah());
            } else
                setSelectedSekolah(null);
        } else {
            setSelectedSekolah(null);
        }
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

    public void onCreate$windowSekolahList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();

        binder.loadAll();
    }

    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_SekolahList.setPageSize(getCountRows());
        paging_SekolahList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_SekolahList_No.setSortAscending(new FieldComparator("ckdsekolah", true));
        listheader_SekolahList_No.setSortDescending(new FieldComparator("ckdsekolah", false));
        listheader_SekolahList_Name1.setSortAscending(new FieldComparator("cnamaSekolah", true));
        listheader_SekolahList_Name1.setSortDescending(new FieldComparator("cnamaSekolah", false));
        listheader_SekolahList_Singkatan.setSortAscending(new FieldComparator("csingkatan", true));
        listheader_SekolahList_Singkatan.setSortDescending(new FieldComparator("csingkatan", false));
        listheader_SekolahList_NamaInggris.setSortAscending(new FieldComparator("cnamaIng", true));
        listheader_SekolahList_NamaInggris.setSortDescending(new FieldComparator("cnamaIng", false));
        listheader_SekolahList_Visi.setSortAscending(new FieldComparator("visi", true));
        listheader_SekolahList_Visi.setSortDescending(new FieldComparator("visi", false));
        listheader_SekolahList_Misi.setSortAscending(new FieldComparator("misi", true));
        listheader_SekolahList_Misi.setSortDescending(new FieldComparator("misi", false));
        listheader_SekolahList_Pegawai.setSortAscending(new FieldComparator("mpegawai.cnama", true));
        listheader_SekolahList_Pegawai.setSortDescending(new FieldComparator("mpegawai.cnama", false));
        listheader_SekolahList_NoSk.setSortAscending(new FieldComparator("noSk", true));
        listheader_SekolahList_NoSk.setSortDescending(new FieldComparator("noSk", false));

        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Msekolah>(Msekolah.class, getCountRows());
        searchObj.addSort("ckdsekolah", false);
        setSearchObj(searchObj);

        // Set the BindingListModel
        getPagedBindingListWrapper().init(searchObj, getListBoxSekolah(), paging_SekolahList);
        BindingListModelList lml = (BindingListModelList) getListBoxSekolah().getModel();
        setSekolahs(lml);

        // check if first time opened and init databinding for selectedBean
        if (getSelectedSekolah() == null) {
            // init the bean with the first record in the List
            if (lml.getSize() > 0) {
                final int rowIndex = 0;
                // only for correct showing after Rendering. No effect as an
                // Event
                // yet.
                getListBoxSekolah().setSelectedIndex(rowIndex);
                // get the first entry and cast them to the needed object
                setSelectedSekolah((Msekolah) lml.get(0));

                // call the onSelect Event for showing the objects data in the
                // statusBar
                Events.sendEvent(new Event("onSelect", getListBoxSekolah(), getSelectedSekolah()));
            }
        }

    }

    /**
     * Selects the object in the listbox and change the tab.<br>
     * Event is forwarded in the corresponding listbox.
     */
    public void onDoubleClickedSekolahItem(Event event) {
        // logger.debug(event.toString());

        Msekolah anSekolah = getSelectedSekolah();

        if (anSekolah != null) {
            setSelectedSekolah(anSekolah);
            setSekolah(anSekolah);

            // check first, if the tabs are created
            if (getSekolahMainCtrl().getSekolahDetailCtrl() == null) {
                Events.sendEvent(new Event("onSelect", getSekolahMainCtrl().tabSekolahDetail, null));
                // if we work with spring beanCreation than we must check a
                // little bit deeper, because the Controller are preCreated ?
            } else if (getSekolahMainCtrl().getSekolahDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getSekolahMainCtrl().tabSekolahDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getSekolahMainCtrl().tabSekolahDetail, anSekolah));
        }
    }

    /**
     * When a listItem in the corresponding listbox is selected.<br>
     * Event is forwarded in the corresponding listbox.
     *
     * @param event
     */
    public void onSelect$listBoxSekolah(Event event) {
        // logger.debug(event.toString());

        // selectedSekolah is filled by annotated databinding mechanism
        Msekolah anSekolah = getSelectedSekolah();

        if (anSekolah == null) {
            return;
        }

        // check first, if the tabs are created
        if (getSekolahMainCtrl().getSekolahDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getSekolahMainCtrl().tabSekolahDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getSekolahMainCtrl().getSekolahDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getSekolahMainCtrl().tabSekolahDetail, null));
        }

        // INIT ALL RELATED Queries/OBJECTS/LISTS NEW
        getSekolahMainCtrl().getSekolahDetailCtrl().setSelectedSekolah(anSekolah);
        getSekolahMainCtrl().getSekolahDetailCtrl().setSekolah(anSekolah);

        // store the selected bean values as current
        getSekolahMainCtrl().doStoreInitValues();

        // show the objects data in the statusBar
        String str = Labels.getLabel("common.Sekolah") + ": " + anSekolah.toString();
        EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // +++++++++++++++++ Business Logic ++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++++++ Helpers ++++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    /**
     * Recalculates the container size for this controller and resize them.
     * <p/>
     * Calculate how many rows have been place in the listbox. Get the
     * currentDesktopHeight from a hidden Intbox from the index.zul that are
     * filled by onClientInfo() in the indexCtroller.
     */
    public void doFitSize() {
        // normally 0 ! Or we have a i.e. a toolBar on top of the listBox.
        final int specialSize = 5;
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
        final int maxListBoxHeight = height - specialSize - 148;
        setCountRows((int) Math.round(maxListBoxHeight / 17.7));
        borderLayout_sekolahList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowSekolahList.invalidate();
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    // ++++++++++++++++++ getter / setter +++++++++++++++++++//
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

    /**
     * Best Pratice Hint:<br>
     * The setters/getters for the local annotated data binded Beans/Sets are
     * administered in the module's mainController. Working in this way you have
     * clean line to share this beans/sets with other controllers.
     */
    public Msekolah getSekolah() {
        // STORED IN THE module's MainController
        return getSekolahMainCtrl().getSelectedSekolah();
    }

    public void setSekolah(Msekolah anSekolah) {
        // STORED IN THE module's MainController
        getSekolahMainCtrl().setSelectedSekolah(anSekolah);
    }

    public void setSelectedSekolah(Msekolah selectedSekolah) {
        // STORED IN THE module's MainController
        getSekolahMainCtrl().setSelectedSekolah(selectedSekolah);
    }

    public Msekolah getSelectedSekolah() {
        // STORED IN THE module's MainController
        return getSekolahMainCtrl().getSelectedSekolah();
    }

    public void setSekolahs(BindingListModelList offices) {
        // STORED IN THE module's MainController
        getSekolahMainCtrl().setSekolahs(offices);
    }

    public BindingListModelList getSekolahs() {
        // STORED IN THE module's MainController
        return getSekolahMainCtrl().getSekolahs();
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setSearchObj(HibernateSearchObject<Msekolah> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Msekolah> getSearchObj() {
        return this.searchObj;
    }

    public void setSekolahService(SekolahService sekolahService) {
        this.sekolahService = sekolahService;
    }

    public SekolahService getSekolahService() {
        return this.sekolahService;
    }

    public Listbox getListBoxSekolah() {
        return this.listBoxSekolah;
    }

    public void setListBoxSekolah(Listbox listBoxSekolah) {
        this.listBoxSekolah = listBoxSekolah;
    }

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void setSekolahMainCtrl(SekolahMainCtrl sekolahMainCtrl) {
        this.sekolahMainCtrl = sekolahMainCtrl;
    }

    public SekolahMainCtrl getSekolahMainCtrl() {
        return this.sekolahMainCtrl;
    }

}
