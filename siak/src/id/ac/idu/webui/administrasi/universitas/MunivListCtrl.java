package id.ac.idu.webui.administrasi.universitas;

import id.ac.idu.administrasi.service.MunivService;
import id.ac.idu.backend.model.Muniv;
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
 * Date: 3/6/12
 * Time: 1:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class MunivListCtrl extends GFCBaseListCtrl<Muniv> implements Serializable {
    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(MunivListCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowMunivList; // autowired
    protected Panel panelMunivList; // autowired

    protected Borderlayout borderLayout_munivList; // autowired
    protected Paging paging_MunivList; // autowired
    protected Listbox listBoxMuniv; // autowired
    protected Listheader listheader_MunivList_Code; // autowired
    protected Listheader listheader_MunivList_Name; // autowired
    protected Listheader listheader_MunivList_Alamat; // autowired
    protected Listheader listheader_MunivList_Status; // autowired

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Muniv> searchObj;

    // row count for listbox
    private int countRows;

    // Databinding
    private AnnotateDataBinder binder;
    private MunivMainCtrl munivMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient MunivService munivService;

    /**
     * default constructor.<br>
     */
    public MunivListCtrl() {
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
            setMunivMainCtrl((MunivMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getMunivMainCtrl().setMunivListCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getMunivMainCtrl().getSelectedMuniv() != null) {
                setSelectedMuniv(getMunivMainCtrl().getSelectedMuniv());
            } else
                setSelectedMuniv(null);
        } else {
            setSelectedMuniv(null);
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

    public void onCreate$windowMunivList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();

        binder.loadAll();
    }

    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_MunivList.setPageSize(getCountRows());
        paging_MunivList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_MunivList_Code.setSortAscending(new FieldComparator("ckdUniv", true));
        listheader_MunivList_Code.setSortDescending(new FieldComparator("ckdUniv", false));
        listheader_MunivList_Name.setSortAscending(new FieldComparator("cnamaUniv", true));
        listheader_MunivList_Name.setSortDescending(new FieldComparator("cnamaUniv", false));
        listheader_MunivList_Alamat.setSortAscending(new FieldComparator("alamatUniv", true));
        listheader_MunivList_Alamat.setSortDescending(new FieldComparator("alamatUniv", false));
        listheader_MunivList_Status.setSortAscending(new FieldComparator("cstatus", true));
        listheader_MunivList_Status.setSortDescending(new FieldComparator("cstatus", false));

        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Muniv>(Muniv.class, getCountRows());
        searchObj.addSort("ckdUniv", false);
        setSearchObj(searchObj);

        // Set the BindingListModel
        getPagedBindingListWrapper().init(searchObj, getListBoxMuniv(), paging_MunivList);
        BindingListModelList lml = (BindingListModelList) getListBoxMuniv().getModel();
        setMunivs(lml);

        // check if first time opened and init databinding for selectedBean
        if (getSelectedMuniv() == null) {
            // init the bean with the first record in the List
            if (lml.getSize() > 0) {
                final int rowIndex = 0;
                // only for correct showing after Rendering. No effect as an
                // Event
                // yet.
                getListBoxMuniv().setSelectedIndex(rowIndex);
                // get the first entry and cast them to the needed object
                setSelectedMuniv((Muniv) lml.get(0));

                // call the onSelect Event for showing the objects data in the
                // statusBar
                Events.sendEvent(new Event("onSelect", getListBoxMuniv(), getSelectedMuniv()));
            }
        }

    }

    /**
     * Selects the object in the listbox and change the tab.<br>
     * Event is forwarded in the corresponding listbox.
     */
    public void onDoubleClickedMunivItem(Event event) {
        // logger.debug(event.toString());

        Muniv anMuniv = getSelectedMuniv();

        if (anMuniv != null) {
            setSelectedMuniv(anMuniv);
            setMuniv(anMuniv);

            // check first, if the tabs are created
            if (getMunivMainCtrl().getMunivDetailCtrl() == null) {
                Events.sendEvent(new Event("onSelect", getMunivMainCtrl().tabMunivDetail, null));
                // if we work with spring beanCreation than we must check a
                // little bit deeper, because the Controller are preCreated ?
            } else if (getMunivMainCtrl().getMunivDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getMunivMainCtrl().tabMunivDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getMunivMainCtrl().tabMunivDetail, anMuniv));
        }
    }

    /**
     * When a listItem in the corresponding listbox is selected.<br>
     * Event is forwarded in the corresponding listbox.
     *
     * @param event
     */
    public void onSelect$listBoxMuniv(Event event) {
        // logger.debug(event.toString());

        // selectedMuniv is filled by annotated databinding mechanism
        Muniv anMuniv = getSelectedMuniv();

        if (anMuniv == null) {
            return;
        }

        // check first, if the tabs are created
        if (getMunivMainCtrl().getMunivDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getMunivMainCtrl().tabMunivDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getMunivMainCtrl().getMunivDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getMunivMainCtrl().tabMunivDetail, null));
        }

        // INIT ALL RELATED Queries/OBJECTS/LISTS NEW
        getMunivMainCtrl().getMunivDetailCtrl().setSelectedMuniv(anMuniv);
        getMunivMainCtrl().getMunivDetailCtrl().setMuniv(anMuniv);

        // store the selected bean values as current
        getMunivMainCtrl().doStoreInitValues();

        // show the objects data in the statusBar
        String str = Labels.getLabel("common.Muniv") + ": " + anMuniv.getCnamaUniv();
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
        borderLayout_munivList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowMunivList.invalidate();
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
    public Muniv getMuniv() {
        // STORED IN THE module's MainController
        return getMunivMainCtrl().getSelectedMuniv();
    }

    public void setMuniv(Muniv anMuniv) {
        // STORED IN THE module's MainController
        getMunivMainCtrl().setSelectedMuniv(anMuniv);
    }

    public void setSelectedMuniv(Muniv selectedMuniv) {
        // STORED IN THE module's MainController
        getMunivMainCtrl().setSelectedMuniv(selectedMuniv);
    }

    public Muniv getSelectedMuniv() {
        // STORED IN THE module's MainController
        return getMunivMainCtrl().getSelectedMuniv();
    }

    public void setMunivs(BindingListModelList munivs) {
        // STORED IN THE module's MainController
        getMunivMainCtrl().setMunivs(munivs);
    }

    public BindingListModelList getMunivs() {
        // STORED IN THE module's MainController
        return getMunivMainCtrl().getMunivs();
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setSearchObj(HibernateSearchObject<Muniv> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Muniv> getSearchObj() {
        return this.searchObj;
    }

    public void setMunivService(MunivService munivService) {
        this.munivService = munivService;
    }

    public MunivService getMunivService() {
        return this.munivService;
    }

    public Listbox getListBoxMuniv() {
        return this.listBoxMuniv;
    }

    public void setListBoxMuniv(Listbox listBoxMuniv) {
        this.listBoxMuniv = listBoxMuniv;
    }

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void setMunivMainCtrl(MunivMainCtrl munivMainCtrl) {
        this.munivMainCtrl = munivMainCtrl;
    }

    public MunivMainCtrl getMunivMainCtrl() {
        return this.munivMainCtrl;
    }
}
