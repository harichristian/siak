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
package id.ac.idu.webui.administrasi.peminatan;

import id.ac.idu.administrasi.service.MpeminatanService;
import id.ac.idu.backend.model.Mpeminatan;
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
 * This is the controller class for the /WEB-INF/pages/mpeminatan/mpeminatanList.zul
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
public class MpeminatanListCtrl extends GFCBaseListCtrl<Mpeminatan> implements Serializable {

    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(MpeminatanListCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowMpeminatanList; // autowired
    protected Panel panelMpeminatanList; // autowired

    protected Borderlayout borderLayout_mpeminatanList; // autowired
    protected Paging paging_MpeminatanList; // autowired
    protected Listbox listBoxMpeminatan; // autowired
    protected Listheader listheader_MpeminatanList_jenis; // autowired
    protected Listheader listheader_MpeminatanList_nmprodi; // autowired
    protected Listheader listheader_MpeminatanList_nmpeminatan;
    protected Listheader listheader_MpeminatanList_nmpeminanatan_eng;
    protected Listheader listheader_MpeminatanList_aktif;

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Mpeminatan> searchObj;

    // row count for listbox
    private int countRows;

    // Databinding
    private AnnotateDataBinder binder;
    private MpeminatanMainCtrl mpeminatanMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient MpeminatanService mpeminatanService;

    /**
     * default constructor.<br>
     */
    public MpeminatanListCtrl() {
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
            setMpeminatanMainCtrl((MpeminatanMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getMpeminatanMainCtrl().setMpeminatanListCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getMpeminatanMainCtrl().getSelectedMpeminatan() != null) {
                setSelectedMpeminatan(getMpeminatanMainCtrl().getSelectedMpeminatan());
            } else
                setSelectedMpeminatan(null);
        } else {
            setSelectedMpeminatan(null);
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

    public void onCreate$windowMpeminatanList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();

        binder.loadAll();
    }

    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_MpeminatanList.setPageSize(getCountRows());
        paging_MpeminatanList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_MpeminatanList_jenis.setSortAscending(new FieldComparator("ckdminat", true));
        listheader_MpeminatanList_jenis.setSortDescending(new FieldComparator("ckdminat", false));
        listheader_MpeminatanList_nmpeminatan.setSortAscending(new FieldComparator("cnmminat", true));
        listheader_MpeminatanList_nmpeminatan.setSortDescending(new FieldComparator("cnmminat", false));
        listheader_MpeminatanList_nmpeminanatan_eng.setSortAscending(new FieldComparator("cnmminating", true));
        listheader_MpeminatanList_nmpeminanatan_eng.setSortDescending(new FieldComparator("cnmminating", false));
        listheader_MpeminatanList_nmprodi.setSortAscending(new FieldComparator("mprodi.cnmprogst", true));
        listheader_MpeminatanList_nmprodi.setSortDescending(new FieldComparator("mprodi.cnmprogst", false));
        listheader_MpeminatanList_aktif.setSortAscending(new FieldComparator("caktif", true));
        listheader_MpeminatanList_aktif.setSortDescending(new FieldComparator("caktif", false));

        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Mpeminatan>(Mpeminatan.class, getCountRows());
        searchObj.addSort("cnmminat", false);
        setSearchObj(searchObj);

        // Set the BindingListModel
        getPagedBindingListWrapper().init(searchObj, getListBoxMpeminatan(), paging_MpeminatanList);
        BindingListModelList lml = (BindingListModelList) getListBoxMpeminatan().getModel();
        setMpeminatans(lml);

        // check if first time opened and init databinding for selectedBean
        if (getSelectedMpeminatan() == null) {
            // init the bean with the first record in the List
            if (lml.getSize() > 0) {
                final int rowIndex = 0;
                // only for correct showing after Rendering. No effect as an
                // Event
                // yet.
                getListBoxMpeminatan().setSelectedIndex(rowIndex);
                // get the first entry and cast them to the needed object
                setSelectedMpeminatan((Mpeminatan) lml.get(0));

                // call the onSelect Event for showing the objects data in the
                // statusBar
                Events.sendEvent(new Event("onSelect", getListBoxMpeminatan(), getSelectedMpeminatan()));
            }
        }

    }

    /**
     * Selects the object in the listbox and change the tab.<br>
     * Event is forwarded in the corresponding listbox.
     */
    public void onDoubleClickedMpeminatanItem(Event event) {
        // logger.debug(event.toString());

        Mpeminatan anMpeminatan = getSelectedMpeminatan();

        if (anMpeminatan != null) {
            setSelectedMpeminatan(anMpeminatan);
            setMpeminatan(anMpeminatan);

            // check first, if the tabs are created
            if (getMpeminatanMainCtrl().getMpeminatanDetailCtrl() == null) {
                Events.sendEvent(new Event("onSelect", getMpeminatanMainCtrl().tabMpeminatanDetail, null));
                // if we work with spring beanCreation than we must check a
                // little bit deeper, because the Controller are preCreated ?
            } else if (getMpeminatanMainCtrl().getMpeminatanDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getMpeminatanMainCtrl().tabMpeminatanDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getMpeminatanMainCtrl().tabMpeminatanDetail, anMpeminatan));
        }
    }

    /**
     * When a listItem in the corresponding listbox is selected.<br>
     * Event is forwarded in the corresponding listbox.
     *
     * @param event
     */
    public void onSelect$listBoxMpeminatan(Event event) {
        // logger.debug(event.toString());

        // selectedMpeminatan is filled by annotated databinding mechanism
        Mpeminatan anMpeminatan = getSelectedMpeminatan();

        if (anMpeminatan == null) {
            return;
        }

        // check first, if the tabs are created
        if (getMpeminatanMainCtrl().getMpeminatanDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getMpeminatanMainCtrl().tabMpeminatanDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getMpeminatanMainCtrl().getMpeminatanDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getMpeminatanMainCtrl().tabMpeminatanDetail, null));
        }

        // INIT ALL RELATED Queries/OBJECTS/LISTS NEW
        getMpeminatanMainCtrl().getMpeminatanDetailCtrl().setSelectedMpeminatan(anMpeminatan);
        getMpeminatanMainCtrl().getMpeminatanDetailCtrl().setMpeminatan(anMpeminatan);

        // store the selected bean values as current
        getMpeminatanMainCtrl().doStoreInitValues();

        // show the objects data in the statusBar
        String str ="Feedback  " + ": " + anMpeminatan.getCnmminat();
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
        borderLayout_mpeminatanList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowMpeminatanList.invalidate();
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
    public Mpeminatan getMpeminatan() {
        // STORED IN THE module's MainController
        return getMpeminatanMainCtrl().getSelectedMpeminatan();
    }

    public void setMpeminatan(Mpeminatan anMpeminatan) {
        // STORED IN THE module's MainController
        getMpeminatanMainCtrl().setSelectedMpeminatan(anMpeminatan);
    }

    public void setSelectedMpeminatan(Mpeminatan selectedMpeminatan) {
        // STORED IN THE module's MainController
        getMpeminatanMainCtrl().setSelectedMpeminatan(selectedMpeminatan);
    }

    public Mpeminatan getSelectedMpeminatan() {
        // STORED IN THE module's MainController
        return getMpeminatanMainCtrl().getSelectedMpeminatan();
    }

    public void setMpeminatans(BindingListModelList mpeminatans) {
        // STORED IN THE module's MainController
        getMpeminatanMainCtrl().setMpeminatans(mpeminatans);
    }

    public BindingListModelList getMpeminatans() {
        // STORED IN THE module's MainController
        return getMpeminatanMainCtrl().getMpeminatans();
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setSearchObj(HibernateSearchObject<Mpeminatan> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Mpeminatan> getSearchObj() {
        return this.searchObj;
    }

    public void setMpeminatanService(MpeminatanService mpeminatanService) {
        this.mpeminatanService = mpeminatanService;
    }

    public MpeminatanService getMpeminatanService() {
        return this.mpeminatanService;
    }

    public Listbox getListBoxMpeminatan() {
        return this.listBoxMpeminatan;
    }

    public void setListBoxMpeminatan(Listbox listBoxMpeminatan) {
        this.listBoxMpeminatan = listBoxMpeminatan;
    }

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void setMpeminatanMainCtrl(MpeminatanMainCtrl mpeminatanMainCtrl) {
        this.mpeminatanMainCtrl = mpeminatanMainCtrl;
    }

    public MpeminatanMainCtrl getMpeminatanMainCtrl() {
        return this.mpeminatanMainCtrl;
    }

}
