package id.ac.idu.webui.administrasi.hari;

import id.ac.idu.administrasi.service.HariService;
import id.ac.idu.backend.model.Mhari;
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
 * Time: 7:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class HariListCtrl extends GFCBaseListCtrl<Mhari> implements Serializable {
    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(HariListCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowHariList; // autowired
    protected Panel panelHariList; // autowired

    protected Borderlayout borderLayout_hariList; // autowired
    protected Paging paging_HariList; // autowired
    protected Listbox listBoxHari; // autowired
    protected Listheader listheader_HariList_Code; // autowired
    protected Listheader listheader_HariList_Name; // autowired

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Mhari> searchObj;

    // row count for listbox
    private int countRows;

    // Databinding
    private AnnotateDataBinder binder;
    private HariMainCtrl hariMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient HariService hariService;

    /**
     * default constructor.<br>
     */
    public HariListCtrl() {
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
            setHariMainCtrl((HariMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getHariMainCtrl().setHariListCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getHariMainCtrl().getSelectedHari() != null) {
                setSelectedHari(getHariMainCtrl().getSelectedHari());
            } else
                setSelectedHari(null);
        } else {
            setSelectedHari(null);
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

    public void onCreate$windowHariList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();

        binder.loadAll();
    }

    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_HariList.setPageSize(getCountRows());
        paging_HariList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_HariList_Code.setSortAscending(new FieldComparator(ConstantUtil.HARI_CODE, true));
        listheader_HariList_Code.setSortDescending(new FieldComparator(ConstantUtil.HARI_CODE, false));
        listheader_HariList_Name.setSortAscending(new FieldComparator(ConstantUtil.HARI_NAME, true));
        listheader_HariList_Name.setSortDescending(new FieldComparator(ConstantUtil.HARI_NAME, false));

        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Mhari>(Mhari.class, getCountRows());
        searchObj.addSort(ConstantUtil.HARI_CODE, false);
        setSearchObj(searchObj);

        // Set the BindingListModel
        getPagedBindingListWrapper().init(searchObj, getListBoxHari(), paging_HariList);
        BindingListModelList lml = (BindingListModelList) getListBoxHari().getModel();
        setHaris(lml);

        // check if first time opened and init databinding for selectedBean
        if (getSelectedHari() == null) {
            // init the bean with the first record in the List
            if (lml.getSize() > 0) {
                final int rowIndex = 0;
                // only for correct showing after Rendering. No effect as an
                // Event
                // yet.
                getListBoxHari().setSelectedIndex(rowIndex);
                // get the first entry and cast them to the needed object
                setSelectedHari((Mhari) lml.get(0));

                // call the onSelect Event for showing the objects data in the
                // statusBar
                Events.sendEvent(new Event("onSelect", getListBoxHari(), getSelectedHari()));
            }
        }

    }

    /**
     * Selects the object in the listbox and change the tab.<br>
     * Event is forwarded in the corresponding listbox.
     */
    public void onDoubleClickedHariItem(Event event) {
        // logger.debug(event.toString());

        Mhari anHari = getSelectedHari();

        if (anHari != null) {
            setSelectedHari(anHari);
            setHari(anHari);

            // check first, if the tabs are created
            if (getHariMainCtrl().getHariDetailCtrl() == null) {
                Events.sendEvent(new Event("onSelect", getHariMainCtrl().tabHariDetail, null));
                // if we work with spring beanCreation than we must check a
                // little bit deeper, because the Controller are preCreated ?
            } else if (getHariMainCtrl().getHariDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getHariMainCtrl().tabHariDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getHariMainCtrl().tabHariDetail, anHari));
        }
    }

    /**
     * When a listItem in the corresponding listbox is selected.<br>
     * Event is forwarded in the corresponding listbox.
     *
     * @param event
     */
    public void onSelect$listBoxHari(Event event) {
        // logger.debug(event.toString());

        // selectedHari is filled by annotated databinding mechanism
        Mhari anHari = getSelectedHari();

        if (anHari == null) {
            return;
        }

        // check first, if the tabs are created
        if (getHariMainCtrl().getHariDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getHariMainCtrl().tabHariDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getHariMainCtrl().getHariDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getHariMainCtrl().tabHariDetail, null));
        }

        // INIT ALL RELATED Queries/OBJECTS/LISTS NEW
        getHariMainCtrl().getHariDetailCtrl().setSelectedHari(anHari);
        getHariMainCtrl().getHariDetailCtrl().setHari(anHari);

        // store the selected bean values as current
        getHariMainCtrl().doStoreInitValues();

        // show the objects data in the statusBar
        String str = Labels.getLabel("common.Hari") + ": " + anHari.getCnmhari();
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
        borderLayout_hariList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowHariList.invalidate();
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
    public Mhari getHari() {
        // STORED IN THE module's MainController
        return getHariMainCtrl().getSelectedHari();
    }

    public void setHari(Mhari anHari) {
        // STORED IN THE module's MainController
        getHariMainCtrl().setSelectedHari(anHari);
    }

    public void setSelectedHari(Mhari selectedHari) {
        // STORED IN THE module's MainController
        getHariMainCtrl().setSelectedHari(selectedHari);
    }

    public Mhari getSelectedHari() {
        // STORED IN THE module's MainController
        return getHariMainCtrl().getSelectedHari();
    }

    public void setHaris(BindingListModelList haris) {
        // STORED IN THE module's MainController
        getHariMainCtrl().setHaris(haris);
    }

    public BindingListModelList getHaris() {
        // STORED IN THE module's MainController
        return getHariMainCtrl().getHaris();
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setSearchObj(HibernateSearchObject<Mhari> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Mhari> getSearchObj() {
        return this.searchObj;
    }

    public void setHariService(HariService hariService) {
        this.hariService = hariService;
    }

    public HariService getHariService() {
        return this.hariService;
    }

    public Listbox getListBoxHari() {
        return this.listBoxHari;
    }

    public void setListBoxHari(Listbox listBoxHari) {
        this.listBoxHari = listBoxHari;
    }

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void setHariMainCtrl(HariMainCtrl hariMainCtrl) {
        this.hariMainCtrl = hariMainCtrl;
    }

    public HariMainCtrl getHariMainCtrl() {
        return this.hariMainCtrl;
    }
}
