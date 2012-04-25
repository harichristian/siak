package id.ac.idu.webui.kurikulum.kurikulum;

import id.ac.idu.backend.model.Mkurikulum;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.kurikulum.service.KurikulumService;
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
 * Date: 3/9/12
 * Time: 3:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class KurikulumListCtrl extends GFCBaseListCtrl<Mkurikulum> implements Serializable {
    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(KurikulumListCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowKurikulumList; // autowired
    protected Panel panelKurikulumList; // autowired

    protected Borderlayout borderLayout_kurikulumList; // autowired
    protected Paging paging_KurikulumList; // autowired
    protected Listbox listBoxKurikulum; // autowired
    protected Listheader listheader_KurikulumList_Code; // autowired
    protected Listheader listheader_KurikulumList_Cohort; // autowired
    protected Listheader listheader_KurikulumList_Prodi; // autowired

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Mkurikulum> searchObj;

    // row count for listbox
    private int countRows;

    // Databinding
    private AnnotateDataBinder binder;
    private KurikulumMainCtrl kurikulumMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient KurikulumService kurikulumService;

    /**
     * default constructor.<br>
     */
    public KurikulumListCtrl() {
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
            setKurikulumMainCtrl((KurikulumMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getKurikulumMainCtrl().setKurikulumListCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getKurikulumMainCtrl().getSelectedKurikulum() != null) {
                setSelectedKurikulum(getKurikulumMainCtrl().getSelectedKurikulum());
            } else
                setSelectedKurikulum(null);
        } else {
            setSelectedKurikulum(null);
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

    public void onCreate$windowKurikulumList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();

        binder.loadAll();
    }

    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_KurikulumList.setPageSize(getCountRows());
        paging_KurikulumList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_KurikulumList_Code.setSortAscending(new FieldComparator(ConstantUtil.KURIKULUM_CODE, true));
        listheader_KurikulumList_Code.setSortDescending(new FieldComparator(ConstantUtil.KURIKULUM_CODE, false));
        listheader_KurikulumList_Cohort.setSortAscending(new FieldComparator(ConstantUtil.COHORT, true));
        listheader_KurikulumList_Cohort.setSortDescending(new FieldComparator(ConstantUtil.COHORT, false));
        listheader_KurikulumList_Prodi.setSortAscending(new FieldComparator(ConstantUtil.PRODI_DOT_NAMA, true));
        listheader_KurikulumList_Prodi.setSortDescending(new FieldComparator(ConstantUtil.PRODI_DOT_NAMA, false));

        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Mkurikulum>(Mkurikulum.class, getCountRows());
        searchObj.addSort(ConstantUtil.KURIKULUM_CODE, false);
        setSearchObj(searchObj);

        // Set the BindingListModel
        getPagedBindingListWrapper().init(searchObj, getListBoxKurikulum(), paging_KurikulumList);
        BindingListModelList lml = (BindingListModelList) getListBoxKurikulum().getModel();
        setKurikulums(lml);

        // check if first time opened and init databinding for selectedBean
        if (getSelectedKurikulum() == null) {
            // init the bean with the first record in the List
            if (lml.getSize() > 0) {
                final int rowIndex = 0;
                // only for correct showing after Rendering. No effect as an
                // Event
                // yet.
                getListBoxKurikulum().setSelectedIndex(rowIndex);
                // get the first entry and cast them to the needed object
                setSelectedKurikulum((Mkurikulum) lml.get(0));

                // call the onSelect Event for showing the objects data in the
                // statusBar
                Events.sendEvent(new Event("onSelect", getListBoxKurikulum(), getSelectedKurikulum()));
            }
        }

    }

    /**
     * Selects the object in the listbox and change the tab.<br>
     * Event is forwarded in the corresponding listbox.
     */
    public void onDoubleClickedKurikulumItem(Event event) {
        // logger.debug(event.toString());

        Mkurikulum anKurikulum = getSelectedKurikulum();

        if (anKurikulum != null) {
            setSelectedKurikulum(anKurikulum);
            setKurikulum(anKurikulum);

            // check first, if the tabs are created
            if (getKurikulumMainCtrl().getKurikulumDetailCtrl() == null) {
                Events.sendEvent(new Event("onSelect", getKurikulumMainCtrl().tabKurikulumDetail, null));
                // if we work with spring beanCreation than we must check a
                // little bit deeper, because the Controller are preCreated ?
            } else if (getKurikulumMainCtrl().getKurikulumDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getKurikulumMainCtrl().tabKurikulumDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getKurikulumMainCtrl().tabKurikulumDetail, anKurikulum));
        }
    }

    /**
     * When a listItem in the corresponding listbox is selected.<br>
     * Event is forwarded in the corresponding listbox.
     *
     * @param event
     */
    public void onSelect$listBoxKurikulum(Event event) {
        // logger.debug(event.toString());

        // selectedKurikulum is filled by annotated databinding mechanism
        Mkurikulum anKurikulum = getSelectedKurikulum();

        if (anKurikulum == null) {
            return;
        }

        // check first, if the tabs are created
        if (getKurikulumMainCtrl().getKurikulumDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getKurikulumMainCtrl().tabKurikulumDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getKurikulumMainCtrl().getKurikulumDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getKurikulumMainCtrl().tabKurikulumDetail, null));
        }

        // INIT ALL RELATED Queries/OBJECTS/LISTS NEW
        getKurikulumMainCtrl().getKurikulumDetailCtrl().setSelectedKurikulum(anKurikulum);
        getKurikulumMainCtrl().getKurikulumDetailCtrl().setKurikulum(anKurikulum);

        // store the selected bean values as current
        getKurikulumMainCtrl().doStoreInitValues();

        // show the objects data in the statusBar
        String str = Labels.getLabel("common.Kurikulum") + ": " + anKurikulum.getCkodekur();
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
        borderLayout_kurikulumList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowKurikulumList.invalidate();
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
    public Mkurikulum getKurikulum() {
        // STORED IN THE module's MainController
        return getKurikulumMainCtrl().getSelectedKurikulum();
    }

    public void setKurikulum(Mkurikulum anKurikulum) {
        // STORED IN THE module's MainController
        getKurikulumMainCtrl().setSelectedKurikulum(anKurikulum);
    }

    public void setSelectedKurikulum(Mkurikulum selectedKurikulum) {
        // STORED IN THE module's MainController
        getKurikulumMainCtrl().setSelectedKurikulum(selectedKurikulum);
    }

    public Mkurikulum getSelectedKurikulum() {
        // STORED IN THE module's MainController
        return getKurikulumMainCtrl().getSelectedKurikulum();
    }

    public void setKurikulums(BindingListModelList kurikulums) {
        // STORED IN THE module's MainController
        getKurikulumMainCtrl().setKurikulums(kurikulums);
    }

    public BindingListModelList getKurikulums() {
        // STORED IN THE module's MainController
        return getKurikulumMainCtrl().getKurikulums();
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setSearchObj(HibernateSearchObject<Mkurikulum> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Mkurikulum> getSearchObj() {
        return this.searchObj;
    }

    public void setKurikulumService(KurikulumService kurikulumService) {
        this.kurikulumService = kurikulumService;
    }

    public KurikulumService getKurikulumService() {
        return this.kurikulumService;
    }

    public Listbox getListBoxKurikulum() {
        return this.listBoxKurikulum;
    }

    public void setListBoxKurikulum(Listbox listBoxKurikulum) {
        this.listBoxKurikulum = listBoxKurikulum;
    }

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void setKurikulumMainCtrl(KurikulumMainCtrl kurikulumMainCtrl) {
        this.kurikulumMainCtrl = kurikulumMainCtrl;
    }

    public KurikulumMainCtrl getKurikulumMainCtrl() {
        return this.kurikulumMainCtrl;
    }
}
