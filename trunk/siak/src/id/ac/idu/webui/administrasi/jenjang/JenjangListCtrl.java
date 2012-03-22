package id.ac.idu.webui.administrasi.jenjang;

import id.ac.idu.administrasi.service.JenjangService;
import id.ac.idu.backend.model.Mjenjang;
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
 * Time: 6:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class JenjangListCtrl extends GFCBaseListCtrl<Mjenjang> implements Serializable {
    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(JenjangListCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowJenjangList; // autowired
    protected Panel panelJenjangList; // autowired

    protected Borderlayout borderLayout_jenjangList; // autowired
    protected Paging paging_JenjangList; // autowired
    protected Listbox listBoxJenjang; // autowired
    protected Listheader listheader_JenjangList_Code; // autowired
    protected Listheader listheader_JenjangList_Name; // autowired
    protected Listheader listheader_JenjangList_Singkatan; // autowired

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Mjenjang> searchObj;

    // row count for listbox
    private int countRows;

    // Databinding
    private AnnotateDataBinder binder;
    private JenjangMainCtrl jenjangMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient JenjangService jenjangService;

    /**
     * default constructor.<br>
     */
    public JenjangListCtrl() {
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
            setJenjangMainCtrl((JenjangMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getJenjangMainCtrl().setJenjangListCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getJenjangMainCtrl().getSelectedJenjang() != null) {
                setSelectedJenjang(getJenjangMainCtrl().getSelectedJenjang());
            } else
                setSelectedJenjang(null);
        } else {
            setSelectedJenjang(null);
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

    public void onCreate$windowJenjangList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();

        binder.loadAll();
    }

    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_JenjangList.setPageSize(getCountRows());
        paging_JenjangList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_JenjangList_Code.setSortAscending(new FieldComparator(ConstantUtil.JENJANG_CODE, true));
        listheader_JenjangList_Code.setSortDescending(new FieldComparator(ConstantUtil.JENJANG_CODE, false));
        listheader_JenjangList_Name.setSortAscending(new FieldComparator(ConstantUtil.JENJANG_NAME, true));
        listheader_JenjangList_Name.setSortDescending(new FieldComparator(ConstantUtil.JENJANG_NAME, false));
        listheader_JenjangList_Singkatan.setSortAscending(new FieldComparator(ConstantUtil.JENJANG_SINGKATAN, true));
        listheader_JenjangList_Singkatan.setSortDescending(new FieldComparator(ConstantUtil.JENJANG_SINGKATAN, false));

        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Mjenjang>(Mjenjang.class, getCountRows());
        searchObj.addSort(ConstantUtil.JENJANG_CODE, false);
        setSearchObj(searchObj);

        // Set the BindingListModel
        getPagedBindingListWrapper().init(searchObj, getListBoxJenjang(), paging_JenjangList);
        BindingListModelList lml = (BindingListModelList) getListBoxJenjang().getModel();
        setJenjangs(lml);

        // check if first time opened and init databinding for selectedBean
        if (getSelectedJenjang() == null) {
            // init the bean with the first record in the List
            if (lml.getSize() > 0) {
                final int rowIndex = 0;
                // only for correct showing after Rendering. No effect as an
                // Event
                // yet.
                getListBoxJenjang().setSelectedIndex(rowIndex);
                // get the first entry and cast them to the needed object
                setSelectedJenjang((Mjenjang) lml.get(0));

                // call the onSelect Event for showing the objects data in the
                // statusBar
                Events.sendEvent(new Event("onSelect", getListBoxJenjang(), getSelectedJenjang()));
            }
        }

    }

    /**
     * Selects the object in the listbox and change the tab.<br>
     * Event is forwarded in the corresponding listbox.
     */
    public void onDoubleClickedJenjangItem(Event event) {
        // logger.debug(event.toString());

        Mjenjang anJenjang = getSelectedJenjang();

        if (anJenjang != null) {
            setSelectedJenjang(anJenjang);
            setJenjang(anJenjang);

            // check first, if the tabs are created
            if (getJenjangMainCtrl().getJenjangDetailCtrl() == null) {
                Events.sendEvent(new Event("onSelect", getJenjangMainCtrl().tabJenjangDetail, null));
                // if we work with spring beanCreation than we must check a
                // little bit deeper, because the Controller are preCreated ?
            } else if (getJenjangMainCtrl().getJenjangDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getJenjangMainCtrl().tabJenjangDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getJenjangMainCtrl().tabJenjangDetail, anJenjang));
        }
    }

    /**
     * When a listItem in the corresponding listbox is selected.<br>
     * Event is forwarded in the corresponding listbox.
     *
     * @param event
     */
    public void onSelect$listBoxJenjang(Event event) {
        // logger.debug(event.toString());

        // selectedJenjang is filled by annotated databinding mechanism
        Mjenjang anJenjang = getSelectedJenjang();

        if (anJenjang == null) {
            return;
        }

        // check first, if the tabs are created
        if (getJenjangMainCtrl().getJenjangDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getJenjangMainCtrl().tabJenjangDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getJenjangMainCtrl().getJenjangDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getJenjangMainCtrl().tabJenjangDetail, null));
        }

        // INIT ALL RELATED Queries/OBJECTS/LISTS NEW
        getJenjangMainCtrl().getJenjangDetailCtrl().setSelectedJenjang(anJenjang);
        getJenjangMainCtrl().getJenjangDetailCtrl().setJenjang(anJenjang);

        // store the selected bean values as current
        getJenjangMainCtrl().doStoreInitValues();

        // show the objects data in the statusBar
        String str = Labels.getLabel("common.Jenjang") + ": " + anJenjang.getCnmjen();
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
        borderLayout_jenjangList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowJenjangList.invalidate();
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
    public Mjenjang getJenjang() {
        // STORED IN THE module's MainController
        return getJenjangMainCtrl().getSelectedJenjang();
    }

    public void setJenjang(Mjenjang anJenjang) {
        // STORED IN THE module's MainController
        getJenjangMainCtrl().setSelectedJenjang(anJenjang);
    }

    public void setSelectedJenjang(Mjenjang selectedJenjang) {
        // STORED IN THE module's MainController
        getJenjangMainCtrl().setSelectedJenjang(selectedJenjang);
    }

    public Mjenjang getSelectedJenjang() {
        // STORED IN THE module's MainController
        return getJenjangMainCtrl().getSelectedJenjang();
    }

    public void setJenjangs(BindingListModelList jenjangs) {
        // STORED IN THE module's MainController
        getJenjangMainCtrl().setJenjangs(jenjangs);
    }

    public BindingListModelList getJenjangs() {
        // STORED IN THE module's MainController
        return getJenjangMainCtrl().getJenjangs();
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setSearchObj(HibernateSearchObject<Mjenjang> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Mjenjang> getSearchObj() {
        return this.searchObj;
    }

    public void setJenjangService(JenjangService jenjangService) {
        this.jenjangService = jenjangService;
    }

    public JenjangService getJenjangService() {
        return this.jenjangService;
    }

    public Listbox getListBoxJenjang() {
        return this.listBoxJenjang;
    }

    public void setListBoxJenjang(Listbox listBoxJenjang) {
        this.listBoxJenjang = listBoxJenjang;
    }

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void setJenjangMainCtrl(JenjangMainCtrl jenjangMainCtrl) {
        this.jenjangMainCtrl = jenjangMainCtrl;
    }

    public JenjangMainCtrl getJenjangMainCtrl() {
        return this.jenjangMainCtrl;
    }
}
