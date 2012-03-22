package id.ac.idu.webui.administrasi.prodi;

import id.ac.idu.administrasi.service.ProdiService;
import id.ac.idu.backend.model.Mprodi;
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
 * User: valeo
 * Date: 3/7/12
 * Time: 9:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProdiListCtrl extends GFCBaseListCtrl<Mprodi> implements Serializable {
    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(ProdiListCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowProdiList; // autowired
    protected Panel panelProdiList; // autowired

    protected Borderlayout borderLayout_prodiList; // autowired
    protected Paging paging_ProdiList; // autowired
    protected Listbox listBoxProdi; // autowired
    protected Listheader listheader_ProdiList_Code; // autowired
    protected Listheader listheader_ProdiList_Name; // autowired
    protected Listheader listheader_ProdiList_Singkatan; // autowired
    protected Listheader listheader_ProdiList_Status; // autowired

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Mprodi> searchObj;

    // row count for listbox
    private int countRows;

    // Databinding
    private AnnotateDataBinder binder;
    private ProdiMainCtrl prodiMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient ProdiService prodiService;

    /**
     * default constructor.<br>
     */
    public ProdiListCtrl() {
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
            setProdiMainCtrl((ProdiMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getProdiMainCtrl().setProdiListCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getProdiMainCtrl().getSelectedProdi() != null) {
                setSelectedProdi(getProdiMainCtrl().getSelectedProdi());
            } else
                setSelectedProdi(null);
        } else {
            setSelectedProdi(null);
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

    public void onCreate$windowProdiList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();

        binder.loadAll();
    }

    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_ProdiList.setPageSize(getCountRows());
        paging_ProdiList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_ProdiList_Code.setSortAscending(new FieldComparator("ckdprogst", true));
        listheader_ProdiList_Code.setSortDescending(new FieldComparator("ckdprogst", false));
        listheader_ProdiList_Name.setSortAscending(new FieldComparator("cnmprogst", true));
        listheader_ProdiList_Name.setSortDescending(new FieldComparator("cnmprogst", false));
        listheader_ProdiList_Singkatan.setSortAscending(new FieldComparator("csingkat", true));
        listheader_ProdiList_Singkatan.setSortDescending(new FieldComparator("csingkat", false));
        listheader_ProdiList_Status.setSortAscending(new FieldComparator("cstatus", true));
        listheader_ProdiList_Status.setSortDescending(new FieldComparator("cstatus", false));

        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Mprodi>(Mprodi.class, getCountRows());
        searchObj.addSort("ckdprogst", false);
        setSearchObj(searchObj);

        // Set the BindingListModel
        getPagedBindingListWrapper().init(searchObj, getListBoxProdi(), paging_ProdiList);
        BindingListModelList lml = (BindingListModelList) getListBoxProdi().getModel();
        setProdis(lml);

        // check if first time opened and init databinding for selectedBean
        if (getSelectedProdi() == null) {
            // init the bean with the first record in the List
            if (lml.getSize() > 0) {
                final int rowIndex = 0;
                // only for correct showing after Rendering. No effect as an
                // Event
                // yet.
                getListBoxProdi().setSelectedIndex(rowIndex);
                // get the first entry and cast them to the needed object
                setSelectedProdi((Mprodi) lml.get(0));

                // call the onSelect Event for showing the objects data in the
                // statusBar
                Events.sendEvent(new Event("onSelect", getListBoxProdi(), getSelectedProdi()));
            }
        }

    }

    /**
     * Selects the object in the listbox and change the tab.<br>
     * Event is forwarded in the corresponding listbox.
     */
    public void onDoubleClickedProdiItem(Event event) {
        // logger.debug(event.toString());

        Mprodi anProdi = getSelectedProdi();

        if (anProdi != null) {
            setSelectedProdi(anProdi);
            setProdi(anProdi);

            // check first, if the tabs are created
            if (getProdiMainCtrl().getProdiDetailCtrl() == null) {
                Events.sendEvent(new Event("onSelect", getProdiMainCtrl().tabProdiDetail, null));
                // if we work with spring beanCreation than we must check a
                // little bit deeper, because the Controller are preCreated ?
            } else if (getProdiMainCtrl().getProdiDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getProdiMainCtrl().tabProdiDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getProdiMainCtrl().tabProdiDetail, anProdi));
        }
    }

    /**
     * When a listItem in the corresponding listbox is selected.<br>
     * Event is forwarded in the corresponding listbox.
     *
     * @param event
     */
    public void onSelect$listBoxProdi(Event event) {
        // logger.debug(event.toString());

        // selectedProdi is filled by annotated databinding mechanism
        Mprodi anProdi = getSelectedProdi();

        if (anProdi == null) {
            return;
        }

        // check first, if the tabs are created
        if (getProdiMainCtrl().getProdiDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getProdiMainCtrl().tabProdiDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getProdiMainCtrl().getProdiDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getProdiMainCtrl().tabProdiDetail, null));
        }

        // INIT ALL RELATED Queries/OBJECTS/LISTS NEW
        getProdiMainCtrl().getProdiDetailCtrl().setSelectedProdi(anProdi);
        getProdiMainCtrl().getProdiDetailCtrl().setProdi(anProdi);

        // store the selected bean values as current
        getProdiMainCtrl().doStoreInitValues();

        // show the objects data in the statusBar
        String str = Labels.getLabel("common.Prodi") + ": " + anProdi.getCnmprogst();
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
        borderLayout_prodiList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowProdiList.invalidate();
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
    public Mprodi getProdi() {
        // STORED IN THE module's MainController
        return getProdiMainCtrl().getSelectedProdi();
    }

    public void setProdi(Mprodi anProdi) {
        // STORED IN THE module's MainController
        getProdiMainCtrl().setSelectedProdi(anProdi);
    }

    public void setSelectedProdi(Mprodi selectedProdi) {
        // STORED IN THE module's MainController
        getProdiMainCtrl().setSelectedProdi(selectedProdi);
    }

    public Mprodi getSelectedProdi() {
        // STORED IN THE module's MainController
        return getProdiMainCtrl().getSelectedProdi();
    }

    public void setProdis(BindingListModelList prodis) {
        // STORED IN THE module's MainController
        getProdiMainCtrl().setProdis(prodis);
    }

    public BindingListModelList getProdis() {
        // STORED IN THE module's MainController
        return getProdiMainCtrl().getProdis();
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setSearchObj(HibernateSearchObject<Mprodi> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Mprodi> getSearchObj() {
        return this.searchObj;
    }

    public void setProdiService(ProdiService prodiService) {
        this.prodiService = prodiService;
    }

    public ProdiService getProdiService() {
        return this.prodiService;
    }

    public Listbox getListBoxProdi() {
        return this.listBoxProdi;
    }

    public void setListBoxProdi(Listbox listBoxProdi) {
        this.listBoxProdi = listBoxProdi;
    }

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void setProdiMainCtrl(ProdiMainCtrl prodiMainCtrl) {
        this.prodiMainCtrl = prodiMainCtrl;
    }

    public ProdiMainCtrl getProdiMainCtrl() {
        return this.prodiMainCtrl;
    }
}
