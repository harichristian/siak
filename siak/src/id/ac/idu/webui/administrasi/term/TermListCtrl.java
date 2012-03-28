package id.ac.idu.webui.administrasi.term;

import id.ac.idu.administrasi.service.TermService;
import id.ac.idu.backend.model.Mterm;
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
 * Date: 28/03/12
 * Time: 10:51
 * To change this template use File | Settings | File Templates.
 */
public class TermListCtrl extends GFCBaseListCtrl<Mterm> implements Serializable {
    private static final long serialVersionUID = -2170565288232491362L;
    private static final Logger logger = Logger.getLogger(TermListCtrl.class);

    /*
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    * All the components that are defined here and have a corresponding
    * component with the same 'id' in the zul-file are getting autowired by our
    * 'extends GFCBaseCtrl' GenericForwardComposer.
    * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    */
    protected Window windowTermList; // autowired
    protected Panel panelTermList; // autowired

    protected Borderlayout borderLayout_termList; // autowired
    protected Paging paging_TermList; // autowired
    protected Listbox listBoxTerm; // autowired
    protected Listheader listheader_TermList_No; // autowired
    protected Listheader listheader_TermList_Name1; // autowired

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Mterm> searchObj;

    // row count for listbox
    private int countRows;

    // Databinding
    private AnnotateDataBinder binder;
    private TermMainCtrl termMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient TermService termService;

    /**
     * default constructor.<br>
     */
    public TermListCtrl() {
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
            setTermMainCtrl((TermMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getTermMainCtrl().setTermListCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getTermMainCtrl().getSelectedTerm() != null) {
                setSelectedTerm(getTermMainCtrl().getSelectedTerm());
            } else
                setSelectedTerm(null);
        } else {
            setSelectedTerm(null);
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

    public void onCreate$windowTermList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();

        binder.loadAll();
    }

    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_TermList.setPageSize(getCountRows());
        paging_TermList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_TermList_No.setSortAscending(new FieldComparator("kdTerm", true));
        listheader_TermList_No.setSortDescending(new FieldComparator("kdTerm", false));
        listheader_TermList_Name1.setSortAscending(new FieldComparator("deskripsi", true));
        listheader_TermList_Name1.setSortDescending(new FieldComparator("deskripsi", false));

        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Mterm>(Mterm.class, getCountRows());
        searchObj.addSort("kdTerm", false);
        setSearchObj(searchObj);

        // Set the BindingListModel
        getPagedBindingListWrapper().init(searchObj, getListBoxTerm(), paging_TermList);
        BindingListModelList lml = (BindingListModelList) getListBoxTerm().getModel();
        setTerms(lml);

        // check if first time opened and init databinding for selectedBean
        if (getSelectedTerm() == null) {
            // init the bean with the first record in the List
            if (lml.getSize() > 0) {
                final int rowIndex = 0;
                // only for correct showing after Rendering. No effect as an
                // Event
                // yet.
                getListBoxTerm().setSelectedIndex(rowIndex);
                // get the first entry and cast them to the needed object
                setSelectedTerm((Mterm) lml.get(0));

                // call the onSelect Event for showing the objects data in the
                // statusBar
                Events.sendEvent(new Event("onSelect", getListBoxTerm(), getSelectedTerm()));
            }
        }

    }

    /**
     * Selects the object in the listbox and change the tab.<br>
     * Event is forwarded in the corresponding listbox.
     */
    public void onDoubleClickedTermItem(Event event) {
        // logger.debug(event.toString());

        Mterm anTerm = getSelectedTerm();

        if (anTerm != null) {
            setSelectedTerm(anTerm);
            setTerm(anTerm);

            // check first, if the tabs are created
            if (getTermMainCtrl().getTermDetailCtrl() == null) {
                Events.sendEvent(new Event("onSelect", getTermMainCtrl().tabTermDetail, null));
                // if we work with spring beanCreation than we must check a
                // little bit deeper, because the Controller are preCreated ?
            } else if (getTermMainCtrl().getTermDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getTermMainCtrl().tabTermDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getTermMainCtrl().tabTermDetail, anTerm));
        }
    }

    /**
     * When a listItem in the corresponding listbox is selected.<br>
     * Event is forwarded in the corresponding listbox.
     *
     * @param event
     */
    public void onSelect$listBoxTerm(Event event) {
        // logger.debug(event.toString());

        // selectedTerm is filled by annotated databinding mechanism
        Mterm anTerm = getSelectedTerm();

        if (anTerm == null) {
            return;
        }

        // check first, if the tabs are created
        if (getTermMainCtrl().getTermDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getTermMainCtrl().tabTermDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getTermMainCtrl().getTermDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getTermMainCtrl().tabTermDetail, null));
        }

        // INIT ALL RELATED Queries/OBJECTS/LISTS NEW
        getTermMainCtrl().getTermDetailCtrl().setSelectedTerm(anTerm);
        getTermMainCtrl().getTermDetailCtrl().setTerm(anTerm);

        // store the selected bean values as current
        getTermMainCtrl().doStoreInitValues();

        // show the objects data in the statusBar
        String str = Labels.getLabel("common.Term") + ": " + anTerm.toString();
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
        borderLayout_termList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowTermList.invalidate();
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
    public Mterm getTerm() {
        // STORED IN THE module's MainController
        return getTermMainCtrl().getSelectedTerm();
    }

    public void setTerm(Mterm anTerm) {
        // STORED IN THE module's MainController
        getTermMainCtrl().setSelectedTerm(anTerm);
    }

    public void setSelectedTerm(Mterm selectedTerm) {
        // STORED IN THE module's MainController
        getTermMainCtrl().setSelectedTerm(selectedTerm);
    }

    public Mterm getSelectedTerm() {
        // STORED IN THE module's MainController
        return getTermMainCtrl().getSelectedTerm();
    }

    public void setTerms(BindingListModelList offices) {
        // STORED IN THE module's MainController
        getTermMainCtrl().setTerms(offices);
    }

    public BindingListModelList getTerms() {
        // STORED IN THE module's MainController
        return getTermMainCtrl().getTerms();
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setSearchObj(HibernateSearchObject<Mterm> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Mterm> getSearchObj() {
        return this.searchObj;
    }

    public void setTermService(TermService termService) {
        this.termService = termService;
    }

    public TermService getTermService() {
        return this.termService;
    }

    public Listbox getListBoxTerm() {
        return this.listBoxTerm;
    }

    public void setListBoxTerm(Listbox listBoxTerm) {
        this.listBoxTerm = listBoxTerm;
    }

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void setTermMainCtrl(TermMainCtrl termMainCtrl) {
        this.termMainCtrl = termMainCtrl;
    }

    public TermMainCtrl getTermMainCtrl() {
        return this.termMainCtrl;
    }

}
