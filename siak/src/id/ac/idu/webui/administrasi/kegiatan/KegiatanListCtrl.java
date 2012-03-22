package id.ac.idu.webui.administrasi.kegiatan;

import id.ac.idu.administrasi.service.KegiatanService;
import id.ac.idu.backend.model.Mkegiatan;
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
 * Time: 23:10
 * To change this template use File | Settings | File Templates.
 */
public class KegiatanListCtrl extends GFCBaseListCtrl<Mkegiatan> implements Serializable {
    private static final long serialVersionUID = -2170565288232491362L;
    private static final Logger logger = Logger.getLogger(KegiatanListCtrl.class);

    /*
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    * All the components that are defined here and have a corresponding
    * component with the same 'id' in the zul-file are getting autowired by our
    * 'extends GFCBaseCtrl' GenericForwardComposer.
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    */
    protected Window windowKegiatanList; // autowired
    protected Panel panelKegiatanList; // autowired

    protected Borderlayout borderLayout_kegiatanList; // autowired
    protected Paging paging_KegiatanList; // autowired
    protected Listbox listBoxKegiatan; // autowired
    protected Listheader listheader_KegiatanList_No; // autowired
    protected Listheader listheader_KegiatanList_Name1; // autowired

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Mkegiatan> searchObj;

    // row count for listbox
    private int countRows;

    // Databinding
    private AnnotateDataBinder binder;
    private KegiatanMainCtrl kegiatanMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient KegiatanService kegiatanService;

    /**
     * default constructor.<br>
     */
    public KegiatanListCtrl() {
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
            setKegiatanMainCtrl((KegiatanMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getKegiatanMainCtrl().setKegiatanListCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getKegiatanMainCtrl().getSelectedKegiatan() != null) {
                setSelectedKegiatan(getKegiatanMainCtrl().getSelectedKegiatan());
            } else
                setSelectedKegiatan(null);
        } else {
            setSelectedKegiatan(null);
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

    public void onCreate$windowKegiatanList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();

        binder.loadAll();
    }

    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_KegiatanList.setPageSize(getCountRows());
        paging_KegiatanList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_KegiatanList_No.setSortAscending(new FieldComparator("ckdkgt", true));
        listheader_KegiatanList_No.setSortDescending(new FieldComparator("ckdkgt", false));
        listheader_KegiatanList_Name1.setSortAscending(new FieldComparator("cnmkgt", true));
        listheader_KegiatanList_Name1.setSortDescending(new FieldComparator("cnmkgt", false));

        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Mkegiatan>(Mkegiatan.class, getCountRows());
        searchObj.addSort("ckdkgt", false);
        setSearchObj(searchObj);

        // Set the BindingListModel
        getPagedBindingListWrapper().init(searchObj, getListBoxKegiatan(), paging_KegiatanList);
        BindingListModelList lml = (BindingListModelList) getListBoxKegiatan().getModel();
        setKegiatans(lml);

        // check if first time opened and init databinding for selectedBean
        if (getSelectedKegiatan() == null) {
            // init the bean with the first record in the List
            if (lml.getSize() > 0) {
                final int rowIndex = 0;
                // only for correct showing after Rendering. No effect as an
                // Event
                // yet.
                getListBoxKegiatan().setSelectedIndex(rowIndex);
                // get the first entry and cast them to the needed object
                setSelectedKegiatan((Mkegiatan) lml.get(0));

                // call the onSelect Event for showing the objects data in the
                // statusBar
                Events.sendEvent(new Event("onSelect", getListBoxKegiatan(), getSelectedKegiatan()));
            }
        }

    }

    /**
     * Selects the object in the listbox and change the tab.<br>
     * Event is forwarded in the corresponding listbox.
     */
    public void onDoubleClickedKegiatanItem(Event event) {
        // logger.debug(event.toString());

        Mkegiatan anKegiatan = getSelectedKegiatan();

        if (anKegiatan != null) {
            setSelectedKegiatan(anKegiatan);
            setKegiatan(anKegiatan);

            // check first, if the tabs are created
            if (getKegiatanMainCtrl().getKegiatanDetailCtrl() == null) {
                Events.sendEvent(new Event("onSelect", getKegiatanMainCtrl().tabKegiatanDetail, null));
                // if we work with spring beanCreation than we must check a
                // little bit deeper, because the Controller are preCreated ?
            } else if (getKegiatanMainCtrl().getKegiatanDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getKegiatanMainCtrl().tabKegiatanDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getKegiatanMainCtrl().tabKegiatanDetail, anKegiatan));
        }
    }

    /**
     * When a listItem in the corresponding listbox is selected.<br>
     * Event is forwarded in the corresponding listbox.
     *
     * @param event
     */
    public void onSelect$listBoxKegiatan(Event event) {
        // logger.debug(event.toString());

        // selectedKegiatan is filled by annotated databinding mechanism
        Mkegiatan anKegiatan = getSelectedKegiatan();

        if (anKegiatan == null) {
            return;
        }

        // check first, if the tabs are created
        if (getKegiatanMainCtrl().getKegiatanDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getKegiatanMainCtrl().tabKegiatanDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getKegiatanMainCtrl().getKegiatanDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getKegiatanMainCtrl().tabKegiatanDetail, null));
        }

        // INIT ALL RELATED Queries/OBJECTS/LISTS NEW
        getKegiatanMainCtrl().getKegiatanDetailCtrl().setSelectedKegiatan(anKegiatan);
        getKegiatanMainCtrl().getKegiatanDetailCtrl().setKegiatan(anKegiatan);

        // store the selected bean values as current
        getKegiatanMainCtrl().doStoreInitValues();

        // show the objects data in the statusBar
        String str = Labels.getLabel("common.Kegiatan") + ": " + anKegiatan.toString();
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
        borderLayout_kegiatanList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowKegiatanList.invalidate();
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
    public Mkegiatan getKegiatan() {
        // STORED IN THE module's MainController
        return getKegiatanMainCtrl().getSelectedKegiatan();
    }

    public void setKegiatan(Mkegiatan anKegiatan) {
        // STORED IN THE module's MainController
        getKegiatanMainCtrl().setSelectedKegiatan(anKegiatan);
    }

    public void setSelectedKegiatan(Mkegiatan selectedKegiatan) {
        // STORED IN THE module's MainController
        getKegiatanMainCtrl().setSelectedKegiatan(selectedKegiatan);
    }

    public Mkegiatan getSelectedKegiatan() {
        // STORED IN THE module's MainController
        return getKegiatanMainCtrl().getSelectedKegiatan();
    }

    public void setKegiatans(BindingListModelList offices) {
        // STORED IN THE module's MainController
        getKegiatanMainCtrl().setKegiatans(offices);
    }

    public BindingListModelList getKegiatans() {
        // STORED IN THE module's MainController
        return getKegiatanMainCtrl().getKegiatans();
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setSearchObj(HibernateSearchObject<Mkegiatan> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Mkegiatan> getSearchObj() {
        return this.searchObj;
    }

    public void setKegiatanService(KegiatanService kegiatanService) {
        this.kegiatanService = kegiatanService;
    }

    public KegiatanService getKegiatanService() {
        return this.kegiatanService;
    }

    public Listbox getListBoxKegiatan() {
        return this.listBoxKegiatan;
    }

    public void setListBoxKegiatan(Listbox listBoxKegiatan) {
        this.listBoxKegiatan = listBoxKegiatan;
    }

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void setKegiatanMainCtrl(KegiatanMainCtrl kegiatanMainCtrl) {
        this.kegiatanMainCtrl = kegiatanMainCtrl;
    }

    public KegiatanMainCtrl getKegiatanMainCtrl() {
        return this.kegiatanMainCtrl;
    }

}
