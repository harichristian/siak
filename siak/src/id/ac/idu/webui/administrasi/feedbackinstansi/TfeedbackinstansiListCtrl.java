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
package id.ac.idu.webui.administrasi.feedbackinstansi;

import id.ac.idu.administrasi.service.TfeedbackinstansiService;
import id.ac.idu.backend.model.Tfeedbackinstansi;
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
 * This is the controller class for the /WEB-INF/pages/tfeedbackinstansi/tfeedbackinstansiList.zul
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
public class TfeedbackinstansiListCtrl extends GFCBaseListCtrl<Tfeedbackinstansi> implements Serializable {

    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(TfeedbackinstansiListCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowTfeedbackinstansiList; // autowired
    protected Panel panelTfeedbackinstansiList; // autowired

    protected Borderlayout borderLayout_tfeedbackinstansiList; // autowired
    protected Paging paging_TfeedbackinstansiList; // autowired
    protected Listbox listBoxTfeedbackinstansi; // autowired
    protected Listheader listheader_TfeedbackinstansiList_jenis; // autowired
    protected Listheader listheader_TfeedbackinstansiList_nminstansi; // autowired
    protected Listheader listheader_TfeedbackinstansiList_alamat; // autowired
    protected Listheader listheader_TfeedbackinstansiList_nmalumni; // autowired
    protected Listheader listheader_TfeedbackinstansiList_kesan; // autowired

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Tfeedbackinstansi> searchObj;

    // row count for listbox
    private int countRows;

    // Databinding
    private AnnotateDataBinder binder;
    private TfeedbackinstansiMainCtrl tfeedbackinstansiMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient TfeedbackinstansiService tfeedbackinstansiService;

    /**
     * default constructor.<br>
     */
    public TfeedbackinstansiListCtrl() {
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
            setTfeedbackinstansiMainCtrl((TfeedbackinstansiMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getTfeedbackinstansiMainCtrl().setTfeedbackinstansiListCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getTfeedbackinstansiMainCtrl().getSelectedTfeedbackinstansi() != null) {
                setSelectedTfeedbackinstansi(getTfeedbackinstansiMainCtrl().getSelectedTfeedbackinstansi());
            } else
                setSelectedTfeedbackinstansi(null);
        } else {
            setSelectedTfeedbackinstansi(null);
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

    public void onCreate$windowTfeedbackinstansiList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();

        binder.loadAll();
    }

    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_TfeedbackinstansiList.setPageSize(getCountRows());
        paging_TfeedbackinstansiList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_TfeedbackinstansiList_jenis.setSortAscending(new FieldComparator("cjnsinstansi", true));
        listheader_TfeedbackinstansiList_jenis.setSortDescending(new FieldComparator("cjnsinstansi", false));
        listheader_TfeedbackinstansiList_nminstansi.setSortAscending(new FieldComparator("cnminstansi", true));
        listheader_TfeedbackinstansiList_nminstansi.setSortDescending(new FieldComparator("cnminstansi", false));
        listheader_TfeedbackinstansiList_alamat.setSortAscending(new FieldComparator("calminstansi", true));
        listheader_TfeedbackinstansiList_alamat.setSortDescending(new FieldComparator("calminstansi", false));
        listheader_TfeedbackinstansiList_nmalumni.setSortAscending(new FieldComparator("malumni.mmahasiswa.cnama", true));
        listheader_TfeedbackinstansiList_nmalumni.setSortDescending(new FieldComparator("malumni.mmahasiswa.cnama", false));
        listheader_TfeedbackinstansiList_kesan.setSortAscending(new FieldComparator("ckesanpesan", true));
        listheader_TfeedbackinstansiList_kesan.setSortDescending(new FieldComparator("ckesanpesan", false));

        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Tfeedbackinstansi>(Tfeedbackinstansi.class, getCountRows());
        searchObj.addSort("cnminstansi", false);
        setSearchObj(searchObj);

        // Set the BindingListModel
        getPagedBindingListWrapper().init(searchObj, getListBoxTfeedbackinstansi(), paging_TfeedbackinstansiList);
        BindingListModelList lml = (BindingListModelList) getListBoxTfeedbackinstansi().getModel();
        setTfeedbackinstansis(lml);

        // check if first time opened and init databinding for selectedBean
        if (getSelectedTfeedbackinstansi() == null) {
            // init the bean with the first record in the List
            if (lml.getSize() > 0) {
                final int rowIndex = 0;
                // only for correct showing after Rendering. No effect as an
                // Event
                // yet.
                getListBoxTfeedbackinstansi().setSelectedIndex(rowIndex);
                // get the first entry and cast them to the needed object
                setSelectedTfeedbackinstansi((Tfeedbackinstansi) lml.get(0));

                // call the onSelect Event for showing the objects data in the
                // statusBar
                Events.sendEvent(new Event("onSelect", getListBoxTfeedbackinstansi(), getSelectedTfeedbackinstansi()));
            }
        }

    }

    /**
     * Selects the object in the listbox and change the tab.<br>
     * Event is forwarded in the corresponding listbox.
     */
    public void onDoubleClickedTfeedbackinstansiItem(Event event) {
        // logger.debug(event.toString());

        Tfeedbackinstansi anTfeedbackinstansi = getSelectedTfeedbackinstansi();

        if (anTfeedbackinstansi != null) {
            setSelectedTfeedbackinstansi(anTfeedbackinstansi);
            setTfeedbackinstansi(anTfeedbackinstansi);

            // check first, if the tabs are created
            if (getTfeedbackinstansiMainCtrl().getTfeedbackinstansiDetailCtrl() == null) {
                Events.sendEvent(new Event("onSelect", getTfeedbackinstansiMainCtrl().tabTfeedbackinstansiDetail, null));
                // if we work with spring beanCreation than we must check a
                // little bit deeper, because the Controller are preCreated ?
            } else if (getTfeedbackinstansiMainCtrl().getTfeedbackinstansiDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getTfeedbackinstansiMainCtrl().tabTfeedbackinstansiDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getTfeedbackinstansiMainCtrl().tabTfeedbackinstansiDetail, anTfeedbackinstansi));
        }
    }

    /**
     * When a listItem in the corresponding listbox is selected.<br>
     * Event is forwarded in the corresponding listbox.
     *
     * @param event
     */
    public void onSelect$listBoxTfeedbackinstansi(Event event) {
        // logger.debug(event.toString());

        // selectedTfeedbackinstansi is filled by annotated databinding mechanism
        Tfeedbackinstansi anTfeedbackinstansi = getSelectedTfeedbackinstansi();

        if (anTfeedbackinstansi == null) {
            return;
        }

        // check first, if the tabs are created
        if (getTfeedbackinstansiMainCtrl().getTfeedbackinstansiDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getTfeedbackinstansiMainCtrl().tabTfeedbackinstansiDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getTfeedbackinstansiMainCtrl().getTfeedbackinstansiDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getTfeedbackinstansiMainCtrl().tabTfeedbackinstansiDetail, null));
        }

        // INIT ALL RELATED Queries/OBJECTS/LISTS NEW
        getTfeedbackinstansiMainCtrl().getTfeedbackinstansiDetailCtrl().setSelectedTfeedbackinstansi(anTfeedbackinstansi);
        getTfeedbackinstansiMainCtrl().getTfeedbackinstansiDetailCtrl().setTfeedbackinstansi(anTfeedbackinstansi);

        // store the selected bean values as current
        getTfeedbackinstansiMainCtrl().doStoreInitValues();

        // show the objects data in the statusBar
        String str ="Feedback Instansi " + ": " + anTfeedbackinstansi.getCnminstansi();
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
        borderLayout_tfeedbackinstansiList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowTfeedbackinstansiList.invalidate();
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
    public Tfeedbackinstansi getTfeedbackinstansi() {
        // STORED IN THE module's MainController
        return getTfeedbackinstansiMainCtrl().getSelectedTfeedbackinstansi();
    }

    public void setTfeedbackinstansi(Tfeedbackinstansi anTfeedbackinstansi) {
        // STORED IN THE module's MainController
        getTfeedbackinstansiMainCtrl().setSelectedTfeedbackinstansi(anTfeedbackinstansi);
    }

    public void setSelectedTfeedbackinstansi(Tfeedbackinstansi selectedTfeedbackinstansi) {
        // STORED IN THE module's MainController
        getTfeedbackinstansiMainCtrl().setSelectedTfeedbackinstansi(selectedTfeedbackinstansi);
    }

    public Tfeedbackinstansi getSelectedTfeedbackinstansi() {
        // STORED IN THE module's MainController
        return getTfeedbackinstansiMainCtrl().getSelectedTfeedbackinstansi();
    }

    public void setTfeedbackinstansis(BindingListModelList tfeedbackinstansis) {
        // STORED IN THE module's MainController
        getTfeedbackinstansiMainCtrl().setTfeedbackinstansis(tfeedbackinstansis);
    }

    public BindingListModelList getTfeedbackinstansis() {
        // STORED IN THE module's MainController
        return getTfeedbackinstansiMainCtrl().getTfeedbackinstansis();
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setSearchObj(HibernateSearchObject<Tfeedbackinstansi> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Tfeedbackinstansi> getSearchObj() {
        return this.searchObj;
    }

    public void setTfeedbackinstansiService(TfeedbackinstansiService tfeedbackinstansiService) {
        this.tfeedbackinstansiService = tfeedbackinstansiService;
    }

    public TfeedbackinstansiService getTfeedbackinstansiService() {
        return this.tfeedbackinstansiService;
    }

    public Listbox getListBoxTfeedbackinstansi() {
        return this.listBoxTfeedbackinstansi;
    }

    public void setListBoxTfeedbackinstansi(Listbox listBoxTfeedbackinstansi) {
        this.listBoxTfeedbackinstansi = listBoxTfeedbackinstansi;
    }

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void setTfeedbackinstansiMainCtrl(TfeedbackinstansiMainCtrl tfeedbackinstansiMainCtrl) {
        this.tfeedbackinstansiMainCtrl = tfeedbackinstansiMainCtrl;
    }

    public TfeedbackinstansiMainCtrl getTfeedbackinstansiMainCtrl() {
        return this.tfeedbackinstansiMainCtrl;
    }

}
