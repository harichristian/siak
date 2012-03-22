package id.ac.idu.webui.administrasi.pangkat;

import id.ac.idu.administrasi.service.PangkatService;
import id.ac.idu.backend.model.Mpangkatgolongan;
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
 * Time: 10:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class PangkatListCtrl extends GFCBaseListCtrl<Mpangkatgolongan> implements Serializable {
    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(PangkatListCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowPangkatList; // autowired
    protected Panel panelPangkatList; // autowired

    protected Borderlayout borderLayout_pangkatList; // autowired
    protected Paging paging_PangkatList; // autowired
    protected Listbox listBoxPangkat; // autowired
    protected Listheader listheader_PangkatList_Code; // autowired
    protected Listheader listheader_PangkatList_Name; // autowired

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Mpangkatgolongan> searchObj;

    // row count for listbox
    private int countRows;

    // Databinding
    private AnnotateDataBinder binder;
    private PangkatMainCtrl pangkatMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient PangkatService pangkatService;

    /**
     * default constructor.<br>
     */
    public PangkatListCtrl() {
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
            setPangkatMainCtrl((PangkatMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getPangkatMainCtrl().setPangkatListCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getPangkatMainCtrl().getSelectedPangkat() != null) {
                setSelectedPangkat(getPangkatMainCtrl().getSelectedPangkat());
            } else
                setSelectedPangkat(null);
        } else {
            setSelectedPangkat(null);
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

    public void onCreate$windowPangkatList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();

        binder.loadAll();
    }

    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_PangkatList.setPageSize(getCountRows());
        paging_PangkatList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_PangkatList_Code.setSortAscending(new FieldComparator(ConstantUtil.PANGKAT_CODE, true));
        listheader_PangkatList_Code.setSortDescending(new FieldComparator(ConstantUtil.PANGKAT_CODE, false));
        listheader_PangkatList_Name.setSortAscending(new FieldComparator(ConstantUtil.PANGKAT_NAME, true));
        listheader_PangkatList_Name.setSortDescending(new FieldComparator(ConstantUtil.PANGKAT_NAME, false));

        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Mpangkatgolongan>(Mpangkatgolongan.class, getCountRows());
        searchObj.addSort(ConstantUtil.PANGKAT_CODE, false);
        setSearchObj(searchObj);

        // Set the BindingListModel
        getPagedBindingListWrapper().init(searchObj, getListBoxPangkat(), paging_PangkatList);
        BindingListModelList lml = (BindingListModelList) getListBoxPangkat().getModel();
        setPangkats(lml);

        // check if first time opened and init databinding for selectedBean
        if (getSelectedPangkat() == null) {
            // init the bean with the first record in the List
            if (lml.getSize() > 0) {
                final int rowIndex = 0;
                // only for correct showing after Rendering. No effect as an
                // Event
                // yet.
                getListBoxPangkat().setSelectedIndex(rowIndex);
                // get the first entry and cast them to the needed object
                setSelectedPangkat((Mpangkatgolongan) lml.get(0));

                // call the onSelect Event for showing the objects data in the
                // statusBar
                Events.sendEvent(new Event("onSelect", getListBoxPangkat(), getSelectedPangkat()));
            }
        }

    }

    /**
     * Selects the object in the listbox and change the tab.<br>
     * Event is forwarded in the corresponding listbox.
     */
    public void onDoubleClickedPangkatItem(Event event) {
        // logger.debug(event.toString());

        Mpangkatgolongan anPangkat = getSelectedPangkat();

        if (anPangkat != null) {
            setSelectedPangkat(anPangkat);
            setPangkat(anPangkat);

            // check first, if the tabs are created
            if (getPangkatMainCtrl().getPangkatDetailCtrl() == null) {
                Events.sendEvent(new Event("onSelect", getPangkatMainCtrl().tabPangkatDetail, null));
                // if we work with spring beanCreation than we must check a
                // little bit deeper, because the Controller are preCreated ?
            } else if (getPangkatMainCtrl().getPangkatDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getPangkatMainCtrl().tabPangkatDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getPangkatMainCtrl().tabPangkatDetail, anPangkat));
        }
    }

    /**
     * When a listItem in the corresponding listbox is selected.<br>
     * Event is forwarded in the corresponding listbox.
     *
     * @param event
     */
    public void onSelect$listBoxPangkat(Event event) {
        // logger.debug(event.toString());

        // selectedPangkat is filled by annotated databinding mechanism
        Mpangkatgolongan anPangkat = getSelectedPangkat();

        if (anPangkat == null) {
            return;
        }

        // check first, if the tabs are created
        if (getPangkatMainCtrl().getPangkatDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getPangkatMainCtrl().tabPangkatDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getPangkatMainCtrl().getPangkatDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getPangkatMainCtrl().tabPangkatDetail, null));
        }

        // INIT ALL RELATED Queries/OBJECTS/LISTS NEW
        getPangkatMainCtrl().getPangkatDetailCtrl().setSelectedPangkat(anPangkat);
        getPangkatMainCtrl().getPangkatDetailCtrl().setPangkat(anPangkat);

        // store the selected bean values as current
        getPangkatMainCtrl().doStoreInitValues();

        // show the objects data in the statusBar
        String str = Labels.getLabel("common.Pangkat") + ": " + anPangkat.getCnmpangkatgolongan();
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
        borderLayout_pangkatList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowPangkatList.invalidate();
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
    public Mpangkatgolongan getPangkat() {
        // STORED IN THE module's MainController
        return getPangkatMainCtrl().getSelectedPangkat();
    }

    public void setPangkat(Mpangkatgolongan anPangkat) {
        // STORED IN THE module's MainController
        getPangkatMainCtrl().setSelectedPangkat(anPangkat);
    }

    public void setSelectedPangkat(Mpangkatgolongan selectedPangkat) {
        // STORED IN THE module's MainController
        getPangkatMainCtrl().setSelectedPangkat(selectedPangkat);
    }

    public Mpangkatgolongan getSelectedPangkat() {
        // STORED IN THE module's MainController
        return getPangkatMainCtrl().getSelectedPangkat();
    }

    public void setPangkats(BindingListModelList pangkats) {
        // STORED IN THE module's MainController
        getPangkatMainCtrl().setPangkats(pangkats);
    }

    public BindingListModelList getPangkats() {
        // STORED IN THE module's MainController
        return getPangkatMainCtrl().getPangkats();
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setSearchObj(HibernateSearchObject<Mpangkatgolongan> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Mpangkatgolongan> getSearchObj() {
        return this.searchObj;
    }

    public void setPangkatService(PangkatService pangkatService) {
        this.pangkatService = pangkatService;
    }

    public PangkatService getPangkatService() {
        return this.pangkatService;
    }

    public Listbox getListBoxPangkat() {
        return this.listBoxPangkat;
    }

    public void setListBoxPangkat(Listbox listBoxPangkat) {
        this.listBoxPangkat = listBoxPangkat;
    }

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void setPangkatMainCtrl(PangkatMainCtrl pangkatMainCtrl) {
        this.pangkatMainCtrl = pangkatMainCtrl;
    }

    public PangkatMainCtrl getPangkatMainCtrl() {
        return this.pangkatMainCtrl;
    }
}
