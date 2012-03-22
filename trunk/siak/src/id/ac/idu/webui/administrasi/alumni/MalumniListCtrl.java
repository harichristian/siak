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
package id.ac.idu.webui.administrasi.alumni;

import id.ac.idu.administrasi.service.MalumniService;
import id.ac.idu.backend.model.Malumni;
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
 * This is the controller class for the /WEB-INF/pages/malumni/malumniList.zul
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
public class MalumniListCtrl extends GFCBaseListCtrl<Malumni> implements Serializable {

    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(MalumniListCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowMalumniList; // autowired
    protected Panel panelMalumniList; // autowired

    protected Borderlayout borderLayout_malumniList; // autowired
    protected Paging paging_MalumniList; // autowired
    protected Listbox listBoxMalumni; // autowired
    protected Listheader listheader_MalumniList_NIM; // autowired
    protected Listheader listheader_MalumniList_Nama; // autowired
    protected Listheader listheader_MalumniList_Sekolah; // autowired
    protected Listheader listheader_MalumniList_Prodi; // autowired

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Malumni> searchObj;

    // row count for listbox
    private int countRows;

    // Databinding
    private AnnotateDataBinder binder;
    private MalumniMainCtrl malumniMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient MalumniService malumniService;

    /**
     * default constructor.<br>
     */
    public MalumniListCtrl() {
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
            setMalumniMainCtrl((MalumniMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getMalumniMainCtrl().setMalumniListCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getMalumniMainCtrl().getSelectedMalumni() != null) {
                setSelectedMalumni(getMalumniMainCtrl().getSelectedMalumni());
            } else
                setSelectedMalumni(null);
        } else {
            setSelectedMalumni(null);
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

    public void onCreate$windowMalumniList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();

        binder.loadAll();
    }

    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_MalumniList.setPageSize(getCountRows());
        paging_MalumniList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_MalumniList_NIM.setSortAscending(new FieldComparator("mmahasiswa.cnim", true));
        listheader_MalumniList_NIM.setSortDescending(new FieldComparator("mmahasiswa.cnim", false));
        listheader_MalumniList_Nama.setSortAscending(new FieldComparator("mmahasiswa.cnama", true));
        listheader_MalumniList_Nama.setSortDescending(new FieldComparator("mmahasiswa.cnama", false));
        listheader_MalumniList_Sekolah.setSortAscending(new FieldComparator("msekolah.cnamaSekolah", true));
        listheader_MalumniList_Sekolah.setSortDescending(new FieldComparator("msekolah.cnamaSekolah", false));
        listheader_MalumniList_Prodi.setSortAscending(new FieldComparator("mprodi.cnmprogst", true));
        listheader_MalumniList_Prodi.setSortDescending(new FieldComparator("mprodi.cnmprogst", false));

        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Malumni>(Malumni.class, getCountRows());
        searchObj.addSort("mmahasiswa.cnama", false);
        setSearchObj(searchObj);

        // Set the BindingListModel
        getPagedBindingListWrapper().init(searchObj, getListBoxMalumni(), paging_MalumniList);
        BindingListModelList lml = (BindingListModelList) getListBoxMalumni().getModel();
        setMalumnis(lml);

        // check if first time opened and init databinding for selectedBean
        if (getSelectedMalumni() == null) {
            // init the bean with the first record in the List
            if (lml.getSize() > 0) {
                final int rowIndex = 0;
                // only for correct showing after Rendering. No effect as an
                // Event
                // yet.
                getListBoxMalumni().setSelectedIndex(rowIndex);
                // get the first entry and cast them to the needed object
                setSelectedMalumni((Malumni) lml.get(0));

                // call the onSelect Event for showing the objects data in the
                // statusBar
                Events.sendEvent(new Event("onSelect", getListBoxMalumni(), getSelectedMalumni()));
            }
        }

    }

    /**
     * Selects the object in the listbox and change the tab.<br>
     * Event is forwarded in the corresponding listbox.
     */
    public void onDoubleClickedMalumniItem(Event event) {
        // logger.debug(event.toString());

        Malumni anMalumni = getSelectedMalumni();

        if (anMalumni != null) {
            setSelectedMalumni(anMalumni);
            setMalumni(anMalumni);

            // check first, if the tabs are created
            if (getMalumniMainCtrl().getMalumniDetailCtrl() == null) {
                Events.sendEvent(new Event("onSelect", getMalumniMainCtrl().tabMalumniDetail, null));
                // if we work with spring beanCreation than we must check a
                // little bit deeper, because the Controller are preCreated ?
            } else if (getMalumniMainCtrl().getMalumniDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getMalumniMainCtrl().tabMalumniDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getMalumniMainCtrl().tabMalumniDetail, anMalumni));
        }
    }

    /**
     * When a listItem in the corresponding listbox is selected.<br>
     * Event is forwarded in the corresponding listbox.
     *
     * @param event
     */
    public void onSelect$listBoxMalumni(Event event) {
        // logger.debug(event.toString());

        // selectedMalumni is filled by annotated databinding mechanism
        Malumni anMalumni = getSelectedMalumni();

        if (anMalumni == null) {
            return;
        }

        // check first, if the tabs are created
        if (getMalumniMainCtrl().getMalumniDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getMalumniMainCtrl().tabMalumniDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getMalumniMainCtrl().getMalumniDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getMalumniMainCtrl().tabMalumniDetail, null));
        }

        // INIT ALL RELATED Queries/OBJECTS/LISTS NEW
        getMalumniMainCtrl().getMalumniDetailCtrl().setSelectedMalumni(anMalumni);
        getMalumniMainCtrl().getMalumniDetailCtrl().setMalumni(anMalumni);

        // store the selected bean values as current
        getMalumniMainCtrl().doStoreInitValues();

        // show the objects data in the statusBar
        String str = "Alumni" + ": " + anMalumni.getMmahasiswa().getCnama();
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
        borderLayout_malumniList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowMalumniList.invalidate();
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
    public Malumni getMalumni() {
        // STORED IN THE module's MainController
        return getMalumniMainCtrl().getSelectedMalumni();
    }

    public void setMalumni(Malumni anMalumni) {
        // STORED IN THE module's MainController
        getMalumniMainCtrl().setSelectedMalumni(anMalumni);
    }

    public void setSelectedMalumni(Malumni selectedMalumni) {
        // STORED IN THE module's MainController
        getMalumniMainCtrl().setSelectedMalumni(selectedMalumni);
    }

    public Malumni getSelectedMalumni() {
        // STORED IN THE module's MainController
        return getMalumniMainCtrl().getSelectedMalumni();
    }

    public void setMalumnis(BindingListModelList malumnis) {
        // STORED IN THE module's MainController
        getMalumniMainCtrl().setMalumnis(malumnis);
    }

    public BindingListModelList getMalumnis() {
        // STORED IN THE module's MainController
        return getMalumniMainCtrl().getMalumnis();
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setSearchObj(HibernateSearchObject<Malumni> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Malumni> getSearchObj() {
        return this.searchObj;
    }

    public void setMalumniService(MalumniService malumniService) {
        this.malumniService = malumniService;
    }

    public MalumniService getMalumniService() {
        return this.malumniService;
    }

    public Listbox getListBoxMalumni() {
        return this.listBoxMalumni;
    }

    public void setListBoxMalumni(Listbox listBoxMalumni) {
        this.listBoxMalumni = listBoxMalumni;
    }

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void setMalumniMainCtrl(MalumniMainCtrl malumniMainCtrl) {
        this.malumniMainCtrl = malumniMainCtrl;
    }

    public MalumniMainCtrl getMalumniMainCtrl() {
        return this.malumniMainCtrl;
    }

}
