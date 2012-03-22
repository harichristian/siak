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
package id.ac.idu.webui.administrasi.fasilitasruang;

import id.ac.idu.administrasi.service.MfasilitasruangService;
import id.ac.idu.backend.model.Mfasilitasruang;
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
 * This is the controller class for the /WEB-INF/pages/mfasilitasruang/mfasilitasruangList.zul
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
public class MfasilitasruangListCtrl extends GFCBaseListCtrl<Mfasilitasruang> implements Serializable {

    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(MfasilitasruangListCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowMfasilitasruangList; // autowired
    protected Panel panelMfasilitasruangList; // autowired

    protected Borderlayout borderLayout_mfasilitasruangList; // autowired
    protected Paging paging_MfasilitasruangList; // autowired
    protected Listbox listBoxMfasilitasruang; // autowired
    protected Listheader listheader_MfasilitasruangList_kdruang; // autowired
    protected Listheader listheader_MfasilitasruangList_nmruang; // autowired
    protected Listheader listheader_MfasilitasruangList_kdfasilitas; // autowired
    protected Listheader listheader_MfasilitasruangList_nmfasilitas; // autowired
    protected Listheader listheader_MfasilitasruangList_jumlah; // autowired
    protected Listheader listheader_MfasilitasruangList_status; // autowired

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Mfasilitasruang> searchObj;

    // row count for listbox
    private int countRows;

    // Databinding
    private AnnotateDataBinder binder;
    private MfasilitasruangMainCtrl mfasilitasruangMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient MfasilitasruangService mfasilitasruangService;

    /**
     * default constructor.<br>
     */
    public MfasilitasruangListCtrl() {
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
            setMfasilitasruangMainCtrl((MfasilitasruangMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getMfasilitasruangMainCtrl().setMfasilitasruangListCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getMfasilitasruangMainCtrl().getSelectedMfasilitasruang() != null) {
                setSelectedMfasilitasruang(getMfasilitasruangMainCtrl().getSelectedMfasilitasruang());
            } else
                setSelectedMfasilitasruang(null);
        } else {
            setSelectedMfasilitasruang(null);
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

    public void onCreate$windowMfasilitasruangList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();

        binder.loadAll();
    }

    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_MfasilitasruangList.setPageSize(getCountRows());
        paging_MfasilitasruangList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_MfasilitasruangList_kdruang.setSortAscending(new FieldComparator("mruang.ckdruang", true));
        listheader_MfasilitasruangList_kdruang.setSortDescending(new FieldComparator("mruang.ckdruang", false));
        listheader_MfasilitasruangList_nmruang.setSortAscending(new FieldComparator("mruang.cnmRuang", true));
        listheader_MfasilitasruangList_nmruang.setSortDescending(new FieldComparator("mruang.cnmRuang", false));
        listheader_MfasilitasruangList_kdfasilitas.setSortAscending(new FieldComparator("mfasilitas.ckdfasilitas", true));
        listheader_MfasilitasruangList_kdfasilitas.setSortDescending(new FieldComparator("mfasilitas.ckdfasilitas", false));
        listheader_MfasilitasruangList_nmfasilitas.setSortAscending(new FieldComparator("mfasilitas.cnamaFasilitas", true));
        listheader_MfasilitasruangList_nmfasilitas.setSortDescending(new FieldComparator("mfasilitas.cnamaFasilitas", false));
        listheader_MfasilitasruangList_jumlah.setSortAscending(new FieldComparator("njml", true));
        listheader_MfasilitasruangList_jumlah.setSortDescending(new FieldComparator("njml", false));
        listheader_MfasilitasruangList_status.setSortAscending(new FieldComparator("cstatus", true));
        listheader_MfasilitasruangList_status.setSortDescending(new FieldComparator("cstatus", false));

        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Mfasilitasruang>(Mfasilitasruang.class, getCountRows());
        searchObj.addSort("mruang.cnmRuang", false);
        setSearchObj(searchObj);

        // Set the BindingListModel
        getPagedBindingListWrapper().init(searchObj, getListBoxMfasilitasruang(), paging_MfasilitasruangList);
        BindingListModelList lml = (BindingListModelList) getListBoxMfasilitasruang().getModel();
        setMfasilitasruangs(lml);

        // check if first time opened and init databinding for selectedBean
        if (getSelectedMfasilitasruang() == null) {
            // init the bean with the first record in the List
            if (lml.getSize() > 0) {
                final int rowIndex = 0;
                // only for correct showing after Rendering. No effect as an
                // Event
                // yet.
                getListBoxMfasilitasruang().setSelectedIndex(rowIndex);
                // get the first entry and cast them to the needed object
                setSelectedMfasilitasruang((Mfasilitasruang) lml.get(0));

                // call the onSelect Event for showing the objects data in the
                // statusBar
                Events.sendEvent(new Event("onSelect", getListBoxMfasilitasruang(), getSelectedMfasilitasruang()));
            }
        }

    }

    /**
     * Selects the object in the listbox and change the tab.<br>
     * Event is forwarded in the corresponding listbox.
     */
    public void onDoubleClickedMfasilitasruangItem(Event event) {
        // logger.debug(event.toString());

        Mfasilitasruang anMfasilitasruang = getSelectedMfasilitasruang();

        if (anMfasilitasruang != null) {
            setSelectedMfasilitasruang(anMfasilitasruang);
            setMfasilitasruang(anMfasilitasruang);

            // check first, if the tabs are created
            if (getMfasilitasruangMainCtrl().getMfasilitasruangDetailCtrl() == null) {
                Events.sendEvent(new Event("onSelect", getMfasilitasruangMainCtrl().tabMfasilitasruangDetail, null));
                // if we work with spring beanCreation than we must check a
                // little bit deeper, because the Controller are preCreated ?
            } else if (getMfasilitasruangMainCtrl().getMfasilitasruangDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getMfasilitasruangMainCtrl().tabMfasilitasruangDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getMfasilitasruangMainCtrl().tabMfasilitasruangDetail, anMfasilitasruang));
        }
    }

    /**
     * When a listItem in the corresponding listbox is selected.<br>
     * Event is forwarded in the corresponding listbox.
     *
     * @param event
     */
    public void onSelect$listBoxMfasilitasruang(Event event) {
        // logger.debug(event.toString());

        // selectedMfasilitasruang is filled by annotated databinding mechanism
        Mfasilitasruang anMfasilitasruang = getSelectedMfasilitasruang();

        if (anMfasilitasruang == null) {
            return;
        }

        // check first, if the tabs are created
        if (getMfasilitasruangMainCtrl().getMfasilitasruangDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getMfasilitasruangMainCtrl().tabMfasilitasruangDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getMfasilitasruangMainCtrl().getMfasilitasruangDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getMfasilitasruangMainCtrl().tabMfasilitasruangDetail, null));
        }

        // INIT ALL RELATED Queries/OBJECTS/LISTS NEW
        getMfasilitasruangMainCtrl().getMfasilitasruangDetailCtrl().setSelectedMfasilitasruang(anMfasilitasruang);
        getMfasilitasruangMainCtrl().getMfasilitasruangDetailCtrl().setMfasilitasruang(anMfasilitasruang);

        // store the selected bean values as current
        getMfasilitasruangMainCtrl().doStoreInitValues();

        // show the objects data in the statusBar
        String str = "Fasilitas " + ": " + anMfasilitasruang.getMfasilitas().getCnamaFasilitas();
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
        borderLayout_mfasilitasruangList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowMfasilitasruangList.invalidate();
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
    public Mfasilitasruang getMfasilitasruang() {
        // STORED IN THE module's MainController
        return getMfasilitasruangMainCtrl().getSelectedMfasilitasruang();
    }

    public void setMfasilitasruang(Mfasilitasruang anMfasilitasruang) {
        // STORED IN THE module's MainController
        getMfasilitasruangMainCtrl().setSelectedMfasilitasruang(anMfasilitasruang);
    }

    public void setSelectedMfasilitasruang(Mfasilitasruang selectedMfasilitasruang) {
        // STORED IN THE module's MainController
        getMfasilitasruangMainCtrl().setSelectedMfasilitasruang(selectedMfasilitasruang);
    }

    public Mfasilitasruang getSelectedMfasilitasruang() {
        // STORED IN THE module's MainController
        return getMfasilitasruangMainCtrl().getSelectedMfasilitasruang();
    }

    public void setMfasilitasruangs(BindingListModelList mfasilitasruangs) {
        // STORED IN THE module's MainController
        getMfasilitasruangMainCtrl().setMfasilitasruangs(mfasilitasruangs);
    }

    public BindingListModelList getMfasilitasruangs() {
        // STORED IN THE module's MainController
        return getMfasilitasruangMainCtrl().getMfasilitasruangs();
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setSearchObj(HibernateSearchObject<Mfasilitasruang> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Mfasilitasruang> getSearchObj() {
        return this.searchObj;
    }

    public void setMfasilitasruangService(MfasilitasruangService mfasilitasruangService) {
        this.mfasilitasruangService = mfasilitasruangService;
    }

    public MfasilitasruangService getMfasilitasruangService() {
        return this.mfasilitasruangService;
    }

    public Listbox getListBoxMfasilitasruang() {
        return this.listBoxMfasilitasruang;
    }

    public void setListBoxMfasilitasruang(Listbox listBoxMfasilitasruang) {
        this.listBoxMfasilitasruang = listBoxMfasilitasruang;
    }

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void setMfasilitasruangMainCtrl(MfasilitasruangMainCtrl mfasilitasruangMainCtrl) {
        this.mfasilitasruangMainCtrl = mfasilitasruangMainCtrl;
    }

    public MfasilitasruangMainCtrl getMfasilitasruangMainCtrl() {
        return this.mfasilitasruangMainCtrl;
    }

}
