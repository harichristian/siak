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
package id.ac.idu.webui.administrasi.ruangan;

import id.ac.idu.administrasi.service.MruangService;
import id.ac.idu.backend.model.Mruang;
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
 * This is the controller class for the /WEB-INF/pages/mruang/mruangList.zul
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
public class MruangListCtrl extends GFCBaseListCtrl<Mruang> implements Serializable {

    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(MruangListCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowMruangList; // autowired
    protected Panel panelMruangList; // autowired

    protected Borderlayout borderLayout_mruangList; // autowired
    protected Paging paging_MruangList; // autowired
    protected Listbox listBoxMruang; // autowired
    protected Listheader listheader_MruangList_kdruangan; // autowired
    protected Listheader listheader_MruangList_nmruangan; // autowired
    protected Listheader listheader_MruangList_maxkuliah; // autowired
    protected Listheader listheader_MruangList_maxujian; // autowired
    protected Listheader listheader_MruangList_status; // autowired

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Mruang> searchObj;

    // row count for listbox
    private int countRows;

    // Databinding
    private AnnotateDataBinder binder;
    private MruangMainCtrl mruangMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient MruangService mruangService;

    /**
     * default constructor.<br>
     */
    public MruangListCtrl() {
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
            setMruangMainCtrl((MruangMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getMruangMainCtrl().setMruangListCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getMruangMainCtrl().getSelectedMruang() != null) {
                setSelectedMruang(getMruangMainCtrl().getSelectedMruang());
            } else
                setSelectedMruang(null);
        } else {
            setSelectedMruang(null);
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

    public void onCreate$windowMruangList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();

        binder.loadAll();
    }

    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_MruangList.setPageSize(getCountRows());
        paging_MruangList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_MruangList_kdruangan.setSortAscending(new FieldComparator("ckdruang", true));
        listheader_MruangList_kdruangan.setSortDescending(new FieldComparator("ckdruang", false));
        listheader_MruangList_nmruangan.setSortAscending(new FieldComparator("cnmRuang", true));
        listheader_MruangList_nmruangan.setSortDescending(new FieldComparator("cnmRuang", false));
        listheader_MruangList_maxkuliah.setSortAscending(new FieldComparator("nmaxIsi", true));
        listheader_MruangList_maxkuliah.setSortDescending(new FieldComparator("nmaxIsi", false));
        listheader_MruangList_maxujian.setSortAscending(new FieldComparator("nmaxUji", true));
        listheader_MruangList_maxujian.setSortDescending(new FieldComparator("nmaxUji", false));
        listheader_MruangList_status.setSortAscending(new FieldComparator("cstatus", true));
        listheader_MruangList_status.setSortDescending(new FieldComparator("cstatus", false));

        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Mruang>(Mruang.class, getCountRows());
        searchObj.addSort("cnmRuang", false);
        setSearchObj(searchObj);

        // Set the BindingListModel
        getPagedBindingListWrapper().init(searchObj, getListBoxMruang(), paging_MruangList);
        BindingListModelList lml = (BindingListModelList) getListBoxMruang().getModel();
        setMruangs(lml);

        // check if first time opened and init databinding for selectedBean
        if (getSelectedMruang() == null) {
            // init the bean with the first record in the List
            if (lml.getSize() > 0) {
                final int rowIndex = 0;
                // only for correct showing after Rendering. No effect as an
                // Event
                // yet.
                getListBoxMruang().setSelectedIndex(rowIndex);
                // get the first entry and cast them to the needed object
                setSelectedMruang((Mruang) lml.get(0));

                // call the onSelect Event for showing the objects data in the
                // statusBar
                Events.sendEvent(new Event("onSelect", getListBoxMruang(), getSelectedMruang()));
            }
        }

    }

    /**
     * Selects the object in the listbox and change the tab.<br>
     * Event is forwarded in the corresponding listbox.
     */
    public void onDoubleClickedMruangItem(Event event) {
        // logger.debug(event.toString());

        Mruang anMruang = getSelectedMruang();

        if (anMruang != null) {
            setSelectedMruang(anMruang);
            setMruang(anMruang);

            // check first, if the tabs are created
            if (getMruangMainCtrl().getMruangDetailCtrl() == null) {
                Events.sendEvent(new Event("onSelect", getMruangMainCtrl().tabMruangDetail, null));
                // if we work with spring beanCreation than we must check a
                // little bit deeper, because the Controller are preCreated ?
            } else if (getMruangMainCtrl().getMruangDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getMruangMainCtrl().tabMruangDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getMruangMainCtrl().tabMruangDetail, anMruang));
        }
    }

    /**
     * When a listItem in the corresponding listbox is selected.<br>
     * Event is forwarded in the corresponding listbox.
     *
     * @param event
     */
    public void onSelect$listBoxMruang(Event event) {
        // logger.debug(event.toString());

        // selectedMruang is filled by annotated databinding mechanism
        Mruang anMruang = getSelectedMruang();

        if (anMruang == null) {
            return;
        }

        // check first, if the tabs are created
        if (getMruangMainCtrl().getMruangDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getMruangMainCtrl().tabMruangDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getMruangMainCtrl().getMruangDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getMruangMainCtrl().tabMruangDetail, null));
        }

        // INIT ALL RELATED Queries/OBJECTS/LISTS NEW
        getMruangMainCtrl().getMruangDetailCtrl().setSelectedMruang(anMruang);
        getMruangMainCtrl().getMruangDetailCtrl().setMruang(anMruang);

        // store the selected bean values as current
        getMruangMainCtrl().doStoreInitValues();

        // show the objects data in the statusBar
        String str = Labels.getLabel("common.Mruang") + ": " + anMruang.getCnmRuang();
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
        borderLayout_mruangList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowMruangList.invalidate();
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
    public Mruang getMruang() {
        // STORED IN THE module's MainController
        return getMruangMainCtrl().getSelectedMruang();
    }

    public void setMruang(Mruang anMruang) {
        // STORED IN THE module's MainController
        getMruangMainCtrl().setSelectedMruang(anMruang);
    }

    public void setSelectedMruang(Mruang selectedMruang) {
        // STORED IN THE module's MainController
        getMruangMainCtrl().setSelectedMruang(selectedMruang);
    }

    public Mruang getSelectedMruang() {
        // STORED IN THE module's MainController
        return getMruangMainCtrl().getSelectedMruang();
    }

    public void setMruangs(BindingListModelList mruangs) {
        // STORED IN THE module's MainController
        getMruangMainCtrl().setMruangs(mruangs);
    }

    public BindingListModelList getMruangs() {
        // STORED IN THE module's MainController
        return getMruangMainCtrl().getMruangs();
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setSearchObj(HibernateSearchObject<Mruang> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Mruang> getSearchObj() {
        return this.searchObj;
    }

    public void setMruangService(MruangService mruangService) {
        this.mruangService = mruangService;
    }

    public MruangService getMruangService() {
        return this.mruangService;
    }

    public Listbox getListBoxMruang() {
        return this.listBoxMruang;
    }

    public void setListBoxMruang(Listbox listBoxMruang) {
        this.listBoxMruang = listBoxMruang;
    }

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void setMruangMainCtrl(MruangMainCtrl mruangMainCtrl) {
        this.mruangMainCtrl = mruangMainCtrl;
    }

    public MruangMainCtrl getMruangMainCtrl() {
        return this.mruangMainCtrl;
    }

}
