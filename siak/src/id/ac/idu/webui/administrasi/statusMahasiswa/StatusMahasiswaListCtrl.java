package id.ac.idu.webui.administrasi.statusMahasiswa;

import id.ac.idu.administrasi.service.StatusMahasiswaService;
import id.ac.idu.backend.model.Mstatusmhs;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.util.ConstantUtil;
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
 * Date: 3/8/12
 * Time: 11:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class StatusMahasiswaListCtrl extends GFCBaseListCtrl<Mstatusmhs> implements Serializable {
    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(StatusMahasiswaListCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowStatusMahasiswaList; // autowired
    protected Panel panelStatusMahasiswaList; // autowired

    protected Borderlayout borderLayout_statusMahasiswaList; // autowired
    protected Paging paging_StatusMahasiswaList; // autowired
    protected Listbox listBoxStatusMahasiswa; // autowired
    protected Listheader listheader_StatusMahasiswaList_Code; // autowired
    protected Listheader listheader_StatusMahasiswaList_Name; // autowired
    protected Listheader listheader_StatusMahasiswaList_Keterangan; // autowired

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Mstatusmhs> searchObj;

    // row count for listbox
    private int countRows;

    // Databinding
    private AnnotateDataBinder binder;
    private StatusMahasiswaMainCtrl statusMahasiswaMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient StatusMahasiswaService statusMahasiswaService;

    /**
     * default constructor.<br>
     */
    public StatusMahasiswaListCtrl() {
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
            setStatusMahasiswaMainCtrl((StatusMahasiswaMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getStatusMahasiswaMainCtrl().setStatusMahasiswaListCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getStatusMahasiswaMainCtrl().getSelectedStatusMahasiswa() != null) {
                setSelectedStatusMahasiswa(getStatusMahasiswaMainCtrl().getSelectedStatusMahasiswa());
            } else
                setSelectedStatusMahasiswa(null);
        } else {
            setSelectedStatusMahasiswa(null);
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

    public void onCreate$windowStatusMahasiswaList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();

        binder.loadAll();
    }

    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_StatusMahasiswaList.setPageSize(getCountRows());
        paging_StatusMahasiswaList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_StatusMahasiswaList_Code.setSortAscending(new FieldComparator(ConstantUtil.MAHASISWA_CODE, true));
        listheader_StatusMahasiswaList_Code.setSortDescending(new FieldComparator(ConstantUtil.MAHASISWA_CODE, false));
        listheader_StatusMahasiswaList_Name.setSortAscending(new FieldComparator(ConstantUtil.MAHASISWA_NAME, true));
        listheader_StatusMahasiswaList_Name.setSortDescending(new FieldComparator(ConstantUtil.MAHASISWA_NAME, false));
        listheader_StatusMahasiswaList_Keterangan.setSortAscending(new FieldComparator(ConstantUtil.KETERANGAN, true));
        listheader_StatusMahasiswaList_Keterangan.setSortDescending(new FieldComparator(ConstantUtil.KETERANGAN, false));

        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Mstatusmhs>(Mstatusmhs.class, getCountRows());
        searchObj.addSort(ConstantUtil.MAHASISWA_CODE, false);
        setSearchObj(searchObj);

        // Set the BindingListModel
        getPagedBindingListWrapper().init(searchObj, getListBoxStatusMahasiswa(), paging_StatusMahasiswaList);
        BindingListModelList lml = (BindingListModelList) getListBoxStatusMahasiswa().getModel();
        setStatusMahasiswas(lml);

        // check if first time opened and init databinding for selectedBean
        if (getSelectedStatusMahasiswa() == null) {
            // init the bean with the first record in the List
            if (lml.getSize() > 0) {
                final int rowIndex = 0;
                // only for correct showing after Rendering. No effect as an
                // Event
                // yet.
                getListBoxStatusMahasiswa().setSelectedIndex(rowIndex);
                // get the first entry and cast them to the needed object
                setSelectedStatusMahasiswa((Mstatusmhs) lml.get(0));

                // call the onSelect Event for showing the objects data in the
                // statusBar
                Events.sendEvent(new Event("onSelect", getListBoxStatusMahasiswa(), getSelectedStatusMahasiswa()));
            }
        }

    }

    /**
     * Selects the object in the listbox and change the tab.<br>
     * Event is forwarded in the corresponding listbox.
     */
    public void onDoubleClickedStatusMahasiswaItem(Event event) {
        // logger.debug(event.toString());

        Mstatusmhs anStatusMahasiswa = getSelectedStatusMahasiswa();

        if (anStatusMahasiswa != null) {
            setSelectedStatusMahasiswa(anStatusMahasiswa);
            setStatusMahasiswa(anStatusMahasiswa);

            // check first, if the tabs are created
            if (getStatusMahasiswaMainCtrl().getStatusMahasiswaDetailCtrl() == null) {
                Events.sendEvent(new Event("onSelect", getStatusMahasiswaMainCtrl().tabStatusMahasiswaDetail, null));
                // if we work with spring beanCreation than we must check a
                // little bit deeper, because the Controller are preCreated ?
            } else if (getStatusMahasiswaMainCtrl().getStatusMahasiswaDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getStatusMahasiswaMainCtrl().tabStatusMahasiswaDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getStatusMahasiswaMainCtrl().tabStatusMahasiswaDetail, anStatusMahasiswa));
        }
    }

    /**
     * When a listItem in the corresponding listbox is selected.<br>
     * Event is forwarded in the corresponding listbox.
     *
     * @param event
     */
    public void onSelect$listBoxStatusMahasiswa(Event event) {
        // logger.debug(event.toString());

        // selectedStatusMahasiswa is filled by annotated databinding mechanism
        Mstatusmhs anStatusMahasiswa = getSelectedStatusMahasiswa();

        if (anStatusMahasiswa == null) {
            return;
        }

        // check first, if the tabs are created
        if (getStatusMahasiswaMainCtrl().getStatusMahasiswaDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getStatusMahasiswaMainCtrl().tabStatusMahasiswaDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getStatusMahasiswaMainCtrl().getStatusMahasiswaDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getStatusMahasiswaMainCtrl().tabStatusMahasiswaDetail, null));
        }

        // INIT ALL RELATED Queries/OBJECTS/LISTS NEW
        getStatusMahasiswaMainCtrl().getStatusMahasiswaDetailCtrl().setSelectedStatusMahasiswa(anStatusMahasiswa);
        getStatusMahasiswaMainCtrl().getStatusMahasiswaDetailCtrl().setStatusMahasiswa(anStatusMahasiswa);

        // store the selected bean values as current
        getStatusMahasiswaMainCtrl().doStoreInitValues();

        // show the objects data in the statusBar
        String str = Labels.getLabel("common.StatusMahasiswa") + ": " + anStatusMahasiswa.getCnmstatmhs();
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
        borderLayout_statusMahasiswaList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowStatusMahasiswaList.invalidate();
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
    public Mstatusmhs getStatusMahasiswa() {
        // STORED IN THE module's MainController
        return getStatusMahasiswaMainCtrl().getSelectedStatusMahasiswa();
    }

    public void setStatusMahasiswa(Mstatusmhs anStatusMahasiswa) {
        // STORED IN THE module's MainController
        getStatusMahasiswaMainCtrl().setSelectedStatusMahasiswa(anStatusMahasiswa);
    }

    public void setSelectedStatusMahasiswa(Mstatusmhs selectedStatusMahasiswa) {
        // STORED IN THE module's MainController
        getStatusMahasiswaMainCtrl().setSelectedStatusMahasiswa(selectedStatusMahasiswa);
    }

    public Mstatusmhs getSelectedStatusMahasiswa() {
        // STORED IN THE module's MainController
        return getStatusMahasiswaMainCtrl().getSelectedStatusMahasiswa();
    }

    public void setStatusMahasiswas(BindingListModelList statusMahasiswas) {
        // STORED IN THE module's MainController
        getStatusMahasiswaMainCtrl().setStatusMahasiswas(statusMahasiswas);
    }

    public BindingListModelList getStatusMahasiswas() {
        // STORED IN THE module's MainController
        return getStatusMahasiswaMainCtrl().getStatusMahasiswas();
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setSearchObj(HibernateSearchObject<Mstatusmhs> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Mstatusmhs> getSearchObj() {
        return this.searchObj;
    }

    public void setStatusMahasiswaService(StatusMahasiswaService statusMahasiswaService) {
        this.statusMahasiswaService = statusMahasiswaService;
    }

    public StatusMahasiswaService getStatusMahasiswaService() {
        return this.statusMahasiswaService;
    }

    public Listbox getListBoxStatusMahasiswa() {
        return this.listBoxStatusMahasiswa;
    }

    public void setListBoxStatusMahasiswa(Listbox listBoxStatusMahasiswa) {
        this.listBoxStatusMahasiswa = listBoxStatusMahasiswa;
    }

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void setStatusMahasiswaMainCtrl(StatusMahasiswaMainCtrl statusMahasiswaMainCtrl) {
        this.statusMahasiswaMainCtrl = statusMahasiswaMainCtrl;
    }

    public StatusMahasiswaMainCtrl getStatusMahasiswaMainCtrl() {
        return this.statusMahasiswaMainCtrl;
    }
}
