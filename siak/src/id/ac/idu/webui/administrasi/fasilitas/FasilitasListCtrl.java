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
package id.ac.idu.webui.administrasi.fasilitas;

import id.ac.idu.administrasi.service.FasilitasService;
import id.ac.idu.backend.model.Mfasilitas;
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
 * This is the controller class for the /WEB-INF/pages/fasilitas/fasilitasList.zul
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
public class FasilitasListCtrl extends GFCBaseListCtrl<Mfasilitas> implements Serializable {

    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(FasilitasListCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowFasilitasList; // autowired
    protected Panel panelFasilitasList; // autowired

    protected Borderlayout borderLayout_fasilitasList; // autowired
    protected Paging paging_FasilitasList; // autowired
    protected Listbox listBoxFasilitas; // autowired
    protected Listheader listheader_FasilitasList_No; // autowired
    protected Listheader listheader_FasilitasList_Name1; // autowired

    // NEEDED for ReUse in the SearchWindow
    private HibernateSearchObject<Mfasilitas> searchObj;

    // row count for listbox
    private int countRows;

    // Databinding
    private AnnotateDataBinder binder;
    private FasilitasMainCtrl fasilitasMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient FasilitasService fasilitasService;

    /**
     * default constructor.<br>
     */
    public FasilitasListCtrl() {
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
            setFasilitasMainCtrl((FasilitasMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getFasilitasMainCtrl().setFasilitasListCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getFasilitasMainCtrl().getSelectedFasilitas() != null) {
                setSelectedFasilitas(getFasilitasMainCtrl().getSelectedFasilitas());
            } else
                setSelectedFasilitas(null);
        } else {
            setSelectedFasilitas(null);
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

    public void onCreate$windowFasilitasList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();

        binder.loadAll();
    }

    public void doFillListbox() {

        doFitSize();

        // set the paging params
        paging_FasilitasList.setPageSize(getCountRows());
        paging_FasilitasList.setDetailed(true);

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listheader_FasilitasList_No.setSortAscending(new FieldComparator("ckdfasilitas", true));
        listheader_FasilitasList_No.setSortDescending(new FieldComparator("ckdfasilitas", false));
        listheader_FasilitasList_Name1.setSortAscending(new FieldComparator("cnamaFasilitas", true));
        listheader_FasilitasList_Name1.setSortDescending(new FieldComparator("cnamaFasilitas", false));


        // ++ create the searchObject and init sorting ++//
        // ++ create the searchObject and init sorting ++//
        searchObj = new HibernateSearchObject<Mfasilitas>(Mfasilitas.class, getCountRows());
        searchObj.addSort("cnamaFasilitas", false);
        setSearchObj(searchObj);

        // Set the BindingListModel
        getPagedBindingListWrapper().init(searchObj, getListBoxFasilitas(), paging_FasilitasList);
        BindingListModelList lml = (BindingListModelList) getListBoxFasilitas().getModel();
        setFasilitass(lml);

        // check if first time opened and init databinding for selectedBean
        if (getSelectedFasilitas() == null) {
            // init the bean with the first record in the List
            if (lml.getSize() > 0) {
                final int rowIndex = 0;
                // only for correct showing after Rendering. No effect as an
                // Event
                // yet.
                getListBoxFasilitas().setSelectedIndex(rowIndex);
                // get the first entry and cast them to the needed object
                setSelectedFasilitas((Mfasilitas) lml.get(0));

                // call the onSelect Event for showing the objects data in the
                // statusBar
                Events.sendEvent(new Event("onSelect", getListBoxFasilitas(), getSelectedFasilitas()));
            }
        }

    }

    /**
     * Selects the object in the listbox and change the tab.<br>
     * Event is forwarded in the corresponding listbox.
     */
    public void onDoubleClickedFasilitasItem(Event event) {
        // logger.debug(event.toString());

        Mfasilitas anFasilitas = getSelectedFasilitas();

        if (anFasilitas != null) {
            setSelectedFasilitas(anFasilitas);
            setFasilitas(anFasilitas);

            // check first, if the tabs are created
            if (getFasilitasMainCtrl().getFasilitasDetailCtrl() == null) {
                Events.sendEvent(new Event("onSelect", getFasilitasMainCtrl().tabFasilitasDetail, null));
                // if we work with spring beanCreation than we must check a
                // little bit deeper, because the Controller are preCreated ?
            } else if (getFasilitasMainCtrl().getFasilitasDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getFasilitasMainCtrl().tabFasilitasDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getFasilitasMainCtrl().tabFasilitasDetail, anFasilitas));
        }
    }

    /**
     * When a listItem in the corresponding listbox is selected.<br>
     * Event is forwarded in the corresponding listbox.
     *
     * @param event
     */
    public void onSelect$listBoxFasilitas(Event event) {
        // logger.debug(event.toString());

        // selectedFasilitas is filled by annotated databinding mechanism
        Mfasilitas anFasilitas = getSelectedFasilitas();

        if (anFasilitas == null) {
            return;
        }

        // check first, if the tabs are created
        if (getFasilitasMainCtrl().getFasilitasDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getFasilitasMainCtrl().tabFasilitasDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getFasilitasMainCtrl().getFasilitasDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getFasilitasMainCtrl().tabFasilitasDetail, null));
        }

        // INIT ALL RELATED Queries/OBJECTS/LISTS NEW
        getFasilitasMainCtrl().getFasilitasDetailCtrl().setSelectedFasilitas(anFasilitas);
        getFasilitasMainCtrl().getFasilitasDetailCtrl().setFasilitas(anFasilitas);

        // store the selected bean values as current
        getFasilitasMainCtrl().doStoreInitValues();

        // show the objects data in the statusBar
        String str = " Fasilitas  : " + anFasilitas.getCnamaFasilitas();
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
        borderLayout_fasilitasList.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowFasilitasList.invalidate();
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
    public Mfasilitas getFasilitas() {
        // STORED IN THE module's MainController
        return getFasilitasMainCtrl().getSelectedFasilitas();
    }

    public void setFasilitas(Mfasilitas anFasilitas) {
        // STORED IN THE module's MainController
        getFasilitasMainCtrl().setSelectedFasilitas(anFasilitas);
    }

    public void setSelectedFasilitas(Mfasilitas selectedFasilitas) {
        // STORED IN THE module's MainController
        getFasilitasMainCtrl().setSelectedFasilitas(selectedFasilitas);
    }

    public Mfasilitas getSelectedFasilitas() {
        // STORED IN THE module's MainController
        return getFasilitasMainCtrl().getSelectedFasilitas();
    }

    public void setFasilitass(BindingListModelList fasilitass) {
        // STORED IN THE module's MainController
        getFasilitasMainCtrl().setFasilitass(fasilitass);
    }

    public BindingListModelList getFasilitass() {
        // STORED IN THE module's MainController
        return getFasilitasMainCtrl().getFasilitass();
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setSearchObj(HibernateSearchObject<Mfasilitas> searchObj) {
        this.searchObj = searchObj;
    }

    public HibernateSearchObject<Mfasilitas> getSearchObj() {
        return this.searchObj;
    }

    public void setFasilitasService(FasilitasService fasilitasService) {
        this.fasilitasService = fasilitasService;
    }

    public FasilitasService getFasilitasService() {
        return this.fasilitasService;
    }

    public Listbox getListBoxFasilitas() {
        return this.listBoxFasilitas;
    }

    public void setListBoxFasilitas(Listbox listBoxFasilitas) {
        this.listBoxFasilitas = listBoxFasilitas;
    }

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void setFasilitasMainCtrl(FasilitasMainCtrl fasilitasMainCtrl) {
        this.fasilitasMainCtrl = fasilitasMainCtrl;
    }

    public FasilitasMainCtrl getFasilitasMainCtrl() {
        return this.fasilitasMainCtrl;
    }

}
