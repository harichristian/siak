/**
 * Copyright 2010 the original author or authors.
 *
 * This file is part of Zksample2. http://zksample2.sourceforge.net/
 *
 * Zksample2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Zksample2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Zksample2.  If not, see <http://www.gnu.org/licenses/gpl.html>.
 */
package id.ac.idu.webui.administrasi.feedback;

import id.ac.idu.administrasi.service.MfeedbackService;
import id.ac.idu.backend.model.Mfeedback;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.webui.util.GFCBaseListCtrl;
import org.apache.log4j.Logger;
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
 * This is the controller class for the /WEB-INF/pages/mfeedback/mfeedbackList.zul
 * file.<br>
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 *
 * @author bbruhns
 * @author sgerth
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
public class MfeedbackListCtrl extends GFCBaseListCtrl<Mfeedback> implements Serializable {

    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(MfeedbackListCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowMfeedbackList; // autowired
    protected Panel panelMfeedbackList; // autowired

    protected Borderlayout borderLayout_mfeedbackList; // autowired
    protected Paging paging_MfeedbackList; // autowired
    protected Listbox listBoxMfeedback; // autowired
    protected Listheader listheader_MfeedbackList_jenis; // autowired
    protected Listheader listheader_MfeedbackList_nmsekolah; // autowired
    protected Listheader listheader_MfeedbackList_nmprodi; // autowired
    protected Listheader listheader_MfeedbackList_nopertanyaan; // autowired
    protected Listheader listheader_MfeedbackList_pertanyaan; // autowired

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Mfeedback> searchObj;

    // row count for listbox
    private int countRows;

    // Databinding
    private AnnotateDataBinder binder;
    private MfeedbackMainCtrl mfeedbackMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient MfeedbackService mfeedbackService;

    /**
     * default constructor.<br>
     */
    public MfeedbackListCtrl() {
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
            setMfeedbackMainCtrl((MfeedbackMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getMfeedbackMainCtrl().setMfeedbackListCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getMfeedbackMainCtrl().getSelectedMfeedback() != null) {
                setSelectedMfeedback(getMfeedbackMainCtrl().getSelectedMfeedback());
            } else
                setSelectedMfeedback(null);
        } else {
            setSelectedMfeedback(null);
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

    public void onCreate$windowMfeedbackList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();

        binder.loadAll();
    }

    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_MfeedbackList.setPageSize(getCountRows());
        paging_MfeedbackList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_MfeedbackList_jenis.setSortAscending(new FieldComparator("ckdfeedback", true));
        listheader_MfeedbackList_jenis.setSortDescending(new FieldComparator("ckdfeedback", false));
        listheader_MfeedbackList_nmsekolah.setSortAscending(new FieldComparator("msekolah.cnamaSekolah", true));
        listheader_MfeedbackList_nmsekolah.setSortDescending(new FieldComparator("msekolah.cnamaSekolah", false));
        listheader_MfeedbackList_nmprodi.setSortAscending(new FieldComparator("mprodi.cnmprogst", true));
        listheader_MfeedbackList_nmprodi.setSortDescending(new FieldComparator("mprodi.cnmprogst", false));
        listheader_MfeedbackList_nopertanyaan.setSortAscending(new FieldComparator("nnopertanyaan", true));
        listheader_MfeedbackList_nopertanyaan.setSortDescending(new FieldComparator("nnopertanyaan", false));
        listheader_MfeedbackList_pertanyaan.setSortAscending(new FieldComparator("cpertanyaan", true));
        listheader_MfeedbackList_pertanyaan.setSortDescending(new FieldComparator("cpertanyaan", false));

        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Mfeedback>(Mfeedback.class, getCountRows());
        searchObj.addSort("ckdfeedback", false);
        setSearchObj(searchObj);

        // Set the BindingListModel
        getPagedBindingListWrapper().init(searchObj, getListBoxMfeedback(), paging_MfeedbackList);
        BindingListModelList lml = (BindingListModelList) getListBoxMfeedback().getModel();
        setMfeedbacks(lml);

        // check if first time opened and init databinding for selectedBean
        if (getSelectedMfeedback() == null) {
            // init the bean with the first record in the List
            if (lml.getSize() > 0) {
                final int rowIndex = 0;
                // only for correct showing after Rendering. No effect as an
                // Event
                // yet.
                getListBoxMfeedback().setSelectedIndex(rowIndex);
                // get the first entry and cast them to the needed object
                setSelectedMfeedback((Mfeedback) lml.get(0));

                // call the onSelect Event for showing the objects data in the
                // statusBar
                Events.sendEvent(new Event("onSelect", getListBoxMfeedback(), getSelectedMfeedback()));
            }
        }

    }

    /**
     * Selects the object in the listbox and change the tab.<br>
     * Event is forwarded in the corresponding listbox.
     */
    public void onDoubleClickedMfeedbackItem(Event event) {
        // logger.debug(event.toString());

        Mfeedback anMfeedback = getSelectedMfeedback();

        if (anMfeedback != null) {
            setSelectedMfeedback(anMfeedback);
            setMfeedback(anMfeedback);

            // check first, if the tabs are created
            if (getMfeedbackMainCtrl().getMfeedbackDetailCtrl() == null) {
                Events.sendEvent(new Event("onSelect", getMfeedbackMainCtrl().tabMfeedbackDetail, null));
                // if we work with spring beanCreation than we must check a
                // little bit deeper, because the Controller are preCreated ?
            } else if (getMfeedbackMainCtrl().getMfeedbackDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getMfeedbackMainCtrl().tabMfeedbackDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getMfeedbackMainCtrl().tabMfeedbackDetail, anMfeedback));
        }
    }

    /**
     * When a listItem in the corresponding listbox is selected.<br>
     * Event is forwarded in the corresponding listbox.
     *
     * @param event
     */
    public void onSelect$listBoxMfeedback(Event event) {
        // logger.debug(event.toString());

        // selectedMfeedback is filled by annotated databinding mechanism
        Mfeedback anMfeedback = getSelectedMfeedback();

        if (anMfeedback == null) {
            return;
        }

        // check first, if the tabs are created
        if (getMfeedbackMainCtrl().getMfeedbackDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getMfeedbackMainCtrl().tabMfeedbackDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getMfeedbackMainCtrl().getMfeedbackDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getMfeedbackMainCtrl().tabMfeedbackDetail, null));
        }

        // INIT ALL RELATED Queries/OBJECTS/LISTS NEW
        getMfeedbackMainCtrl().getMfeedbackDetailCtrl().setSelectedMfeedback(anMfeedback);
        getMfeedbackMainCtrl().getMfeedbackDetailCtrl().setMfeedback(anMfeedback);

        // store the selected bean values as current
        getMfeedbackMainCtrl().doStoreInitValues();

        // show the objects data in the statusBar
        String str ="Feedback  " + ": " + anMfeedback.getCpertanyaan();
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
        borderLayout_mfeedbackList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowMfeedbackList.invalidate();
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
    public Mfeedback getMfeedback() {
        // STORED IN THE module's MainController
        return getMfeedbackMainCtrl().getSelectedMfeedback();
    }

    public void setMfeedback(Mfeedback anMfeedback) {
        // STORED IN THE module's MainController
        getMfeedbackMainCtrl().setSelectedMfeedback(anMfeedback);
    }

    public void setSelectedMfeedback(Mfeedback selectedMfeedback) {
        // STORED IN THE module's MainController
        getMfeedbackMainCtrl().setSelectedMfeedback(selectedMfeedback);
    }

    public Mfeedback getSelectedMfeedback() {
        // STORED IN THE module's MainController
        return getMfeedbackMainCtrl().getSelectedMfeedback();
    }

    public void setMfeedbacks(BindingListModelList mfeedbacks) {
        // STORED IN THE module's MainController
        getMfeedbackMainCtrl().setMfeedbacks(mfeedbacks);
    }

    public BindingListModelList getMfeedbacks() {
        // STORED IN THE module's MainController
        return getMfeedbackMainCtrl().getMfeedbacks();
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setSearchObj(HibernateSearchObject<Mfeedback> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Mfeedback> getSearchObj() {
        return this.searchObj;
    }

    public void setMfeedbackService(MfeedbackService mfeedbackService) {
        this.mfeedbackService = mfeedbackService;
    }

    public MfeedbackService getMfeedbackService() {
        return this.mfeedbackService;
    }

    public Listbox getListBoxMfeedback() {
        return this.listBoxMfeedback;
    }

    public void setListBoxMfeedback(Listbox listBoxMfeedback) {
        this.listBoxMfeedback = listBoxMfeedback;
    }

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void setMfeedbackMainCtrl(MfeedbackMainCtrl mfeedbackMainCtrl) {
        this.mfeedbackMainCtrl = mfeedbackMainCtrl;
    }

    public MfeedbackMainCtrl getMfeedbackMainCtrl() {
        return this.mfeedbackMainCtrl;
    }

}
