package id.ac.idu.webui.grade;

import id.ac.idu.administrasi.service.GradeService;
import id.ac.idu.backend.model.Mgrade;
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
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the /WEB-INF/pages/grade/officeList.zul
 * file.<br>
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 *
 * @author Hari
 * @changes 05/15/2009: sge Migrating the list models for paging. <br>
 * 07/24/2009: sge changings for clustering.<br>
 * 10/12/2009: sge changings in the saving routine.<br>
 * 11/07/2009: bbr changed to extending from GFCBaseCtrl<br>
 * (GenericForwardComposer) for spring-managed creation.<br>
 * 03/09/2009: sge changed for allow repainting after resizing.<br>
 * with the refresh button.<br>
 * 07/03/2010: sge modified for zk5.x with complete Annotated
 * Databinding.<br>
 */
public class GradeListCtrl extends GFCBaseListCtrl<Mgrade> implements Serializable {
    private static final long serialVersionUID = -2170565288232491362L;
    private static final Logger logger = Logger.getLogger(GradeListCtrl.class);

    /*
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    * All the components that are defined here and have a corresponding
    * component with the same 'id' in the zul-file are getting autowired by our
    * 'extends GFCBaseCtrl' GenericForwardComposer.
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    */
    protected Window windowGradeList; // autowired
    protected Panel panelGradeList; // autowired

    protected Borderlayout borderLayout_gradeList; // autowired
    protected Paging paging_GradeList; // autowired
    protected Listbox listBoxGrade; // autowired
    protected Listheader listheader_GradeList_No; // autowired
    protected Listheader listheader_GradeList_Name1; // autowired
    protected Listheader listheader_GradeList_Bobot;
    protected Listheader listheader_GradeList_NilaiAwal;
    protected Listheader listheader_GradeList_NilaiAkhir;
    protected Listheader listheader_GradeList_Prodi;
    protected Listheader listheader_GradeList_Jenjang;


    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Mgrade> searchObj;

    // row count for listbox
    private int countRows;

    // Databinding
    private AnnotateDataBinder binder;
    private GradeMainCtrl gradeMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient GradeService gradeService;

    /**
     * default constructor.<br>
     */
    public GradeListCtrl() {
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
            setGradeMainCtrl((GradeMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getGradeMainCtrl().setGradeListCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getGradeMainCtrl().getSelectedGrade() != null) {
                setSelectedGrade(getGradeMainCtrl().getSelectedGrade());
            } else
                setSelectedGrade(null);
        } else {
            setSelectedGrade(null);
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

    public void onCreate$windowGradeList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();

        binder.loadAll();
    }

    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_GradeList.setPageSize(getCountRows());
        paging_GradeList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_GradeList_No.setSortAscending(new FieldComparator("ckdgrade", true));
        listheader_GradeList_No.setSortDescending(new FieldComparator("ckdgrade", false));
        listheader_GradeList_Name1.setSortAscending(new FieldComparator("cgrade", true));
        listheader_GradeList_Name1.setSortDescending(new FieldComparator("cgrade", false));
        listheader_GradeList_Bobot.setSortAscending(new FieldComparator("nbobot", true));
        listheader_GradeList_NilaiAwal.setSortAscending(new FieldComparator("nnilawal", true));
        listheader_GradeList_NilaiAkhir.setSortAscending(new FieldComparator("nnilakhir", true));
        listheader_GradeList_Prodi.setSortAscending(new FieldComparator("mprodi.cnmprogst", true));
        listheader_GradeList_Jenjang.setSortAscending(new FieldComparator("mjenjang.cnmjen", true));
        listheader_GradeList_Bobot.setSortDescending(new FieldComparator("nbobot", false));
        listheader_GradeList_NilaiAwal.setSortDescending(new FieldComparator("nnilawal", false));
        listheader_GradeList_NilaiAkhir.setSortDescending(new FieldComparator("nnilakhir", false));
        listheader_GradeList_Prodi.setSortDescending(new FieldComparator("mprodi.cnmprogst", false));
        listheader_GradeList_Jenjang.setSortDescending(new FieldComparator("mjenjang.cnmjen", false));

        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Mgrade>(Mgrade.class, getCountRows());
        searchObj.addSort("ckdgrade", false);
        setSearchObj(searchObj);

        // Set the BindingListModel
        getPagedBindingListWrapper().init(searchObj, getListBoxGrade(), paging_GradeList);
        BindingListModelList lml = (BindingListModelList) getListBoxGrade().getModel();
        setGrades(lml);

        // check if first time opened and init databinding for selectedBean
        if (getSelectedGrade() == null) {
            // init the bean with the first record in the List
            if (lml.getSize() > 0) {
                final int rowIndex = 0;
                // only for correct showing after Rendering. No effect as an
                // Event
                // yet.
                getListBoxGrade().setSelectedIndex(rowIndex);
                // get the first entry and cast them to the needed object
                setSelectedGrade((Mgrade) lml.get(0));

                // call the onSelect Event for showing the objects data in the
                // statusBar
                Events.sendEvent(new Event("onSelect", getListBoxGrade(), getSelectedGrade()));
            }
        }

    }

    /**
     * Selects the object in the listbox and change the tab.<br>
     * Event is forwarded in the corresponding listbox.
     */
    public void onDoubleClickedGradeItem(Event event) {
        // logger.debug(event.toString());

        Mgrade anGrade = getSelectedGrade();

        if (anGrade != null) {
            setSelectedGrade(anGrade);
            setGrade(anGrade);

            // check first, if the tabs are created
            if (getGradeMainCtrl().getGradeDetailCtrl() == null) {
                Events.sendEvent(new Event("onSelect", getGradeMainCtrl().tabGradeDetail, null));
                // if we work with spring beanCreation than we must check a
                // little bit deeper, because the Controller are preCreated ?
            } else if (getGradeMainCtrl().getGradeDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getGradeMainCtrl().tabGradeDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getGradeMainCtrl().tabGradeDetail, anGrade));
        }
    }

    /**
     * When a listItem in the corresponding listbox is selected.<br>
     * Event is forwarded in the corresponding listbox.
     *
     * @param event
     */
    public void onSelect$listBoxGrade(Event event) {
        // logger.debug(event.toString());

        // selectedGrade is filled by annotated databinding mechanism
        Mgrade anGrade = getSelectedGrade();

        if (anGrade == null) {
            return;
        }

        // check first, if the tabs are created
        if (getGradeMainCtrl().getGradeDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getGradeMainCtrl().tabGradeDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getGradeMainCtrl().getGradeDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getGradeMainCtrl().tabGradeDetail, null));
        }

        // INIT ALL RELATED Queries/OBJECTS/LISTS NEW
        getGradeMainCtrl().getGradeDetailCtrl().setSelectedGrade(anGrade);
        getGradeMainCtrl().getGradeDetailCtrl().setGrade(anGrade);

        // store the selected bean values as current
        getGradeMainCtrl().doStoreInitValues();

        // show the objects data in the statusBar
        String str = Labels.getLabel("common.Grade") + ": " + anGrade.toString();
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
        borderLayout_gradeList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowGradeList.invalidate();
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
    public Mgrade getGrade() {
        // STORED IN THE module's MainController
        return getGradeMainCtrl().getSelectedGrade();
    }

    public void setGrade(Mgrade anGrade) {
        // STORED IN THE module's MainController
        getGradeMainCtrl().setSelectedGrade(anGrade);
    }

    public void setSelectedGrade(Mgrade selectedGrade) {
        // STORED IN THE module's MainController
        getGradeMainCtrl().setSelectedGrade(selectedGrade);
    }

    public Mgrade getSelectedGrade() {
        // STORED IN THE module's MainController
        return getGradeMainCtrl().getSelectedGrade();
    }

    public void setGrades(BindingListModelList offices) {
        // STORED IN THE module's MainController
        getGradeMainCtrl().setGrades(offices);
    }

    public BindingListModelList getGrades() {
        // STORED IN THE module's MainController
        return getGradeMainCtrl().getGrades();
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setSearchObj(HibernateSearchObject<Mgrade> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Mgrade> getSearchObj() {
        return this.searchObj;
    }

    public void setGradeService(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    public GradeService getGradeService() {
        return this.gradeService;
    }

    public Listbox getListBoxGrade() {
        return this.listBoxGrade;
    }

    public void setListBoxGrade(Listbox listBoxGrade) {
        this.listBoxGrade = listBoxGrade;
    }

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void setGradeMainCtrl(GradeMainCtrl gradeMainCtrl) {
        this.gradeMainCtrl = gradeMainCtrl;
    }

    public GradeMainCtrl getGradeMainCtrl() {
        return this.gradeMainCtrl;
    }

}
