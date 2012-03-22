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
package id.ac.idu.webui.administrasi.pegawai;

import id.ac.idu.administrasi.service.MpegawaiService;
import id.ac.idu.backend.model.Mpegawai;
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
 * This is the controller class for the /WEB-INF/pages/mpegawai/mpegawaiList.zul
 * file.<br>
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * 
 * @changes 05/15/2009: sge Migrating the list models for paging. <br>
 *          07/24/2009: sge changings for clustering.<br>
 *          10/12/2009: sge changings in the saving routine.<br>
 *          11/07/2009: bbr changed to extending from GFCBaseCtrl<br>
 *          (GenericForwardComposer) for spring-managed creation.<br>
 *          03/09/2009: sge changed for allow repainting after resizing.<br>
 *          with the refresh button.<br>
 *          07/03/2010: sge modified for zk5.x with complete Annotated
 *          Databinding.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class MpegawaiListCtrl extends GFCBaseListCtrl<Mpegawai> implements Serializable {

	private static final long serialVersionUID = -1L;
	private static final Logger logger = Logger.getLogger(MpegawaiListCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window windowMpegawaiList; // autowired
	protected Panel panelMpegawaiList; // autowired

	protected Borderlayout borderLayout_mpegawaiList; // autowired
	protected Paging paging_MpegawaiList; // autowired
	protected Listbox listBoxMpegawai; // autowired
	protected Listheader listheader_MpegawaiList_NIP; // autowired
	protected Listheader listheader_MpegawaiList_Nama; // autowired
	protected Listheader listheader_MpegawaiList_Dtglahir; // autowired
    protected Listheader listheader_MpegawaiList_Status; // autowired

	// NEEDED for ReUse in the SearchWindow
	private HibernateSearchObject<Mpegawai> searchObj;

	// row count for listbox
	private int countRows;

	// Databinding
	private AnnotateDataBinder binder;
	private MpegawaiMainCtrl mpegawaiMainCtrl;

	// ServiceDAOs / Domain Classes
	private transient MpegawaiService mpegawaiService;

	/**
	 * default constructor.<br>
	 */
	public MpegawaiListCtrl() {
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
			setMpegawaiMainCtrl((MpegawaiMainCtrl) arg.get("ModuleMainController"));

			// SET THIS CONTROLLER TO THE module's Parent/MainController
			getMpegawaiMainCtrl().setMpegawaiListCtrl(this);

			// Get the selected object.
			// Check if this Controller if created on first time. If so,
			// than the selectedXXXBean should be null
			if (getMpegawaiMainCtrl().getSelectedMpegawai() != null) {
				setSelectedMpegawai(getMpegawaiMainCtrl().getSelectedMpegawai());
			} else
				setSelectedMpegawai(null);
		} else {
			setSelectedMpegawai(null);
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

	public void onCreate$windowMpegawaiList(Event event) throws Exception {
		binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

		doFillListbox();

		binder.loadAll();
	}

	public void doFillListbox() {

		doFitSize();

		// set the paging params
		paging_MpegawaiList.setPageSize(getCountRows());
		paging_MpegawaiList.setDetailed(true);

		// not used listheaders must be declared like ->
		// lh.setSortAscending(""); lh.setSortDescending("")
		listheader_MpegawaiList_NIP.setSortAscending(new FieldComparator("cnip", true));
		listheader_MpegawaiList_NIP.setSortDescending(new FieldComparator("cnip", false));
		listheader_MpegawaiList_Nama.setSortAscending(new FieldComparator("cnama", true));
		listheader_MpegawaiList_Nama.setSortDescending(new FieldComparator("cnama", false));
		listheader_MpegawaiList_Dtglahir.setSortAscending(new FieldComparator("dtglahir", true));
		listheader_MpegawaiList_Dtglahir.setSortDescending(new FieldComparator("dtglahir", false));
        listheader_MpegawaiList_Status.setSortAscending(new FieldComparator("cstatus", true));
		listheader_MpegawaiList_Status.setSortDescending(new FieldComparator("cstatus", false));

		// ++ create the searchObject and init sorting ++//
		// ++ create the searchObject and init sorting ++//
		searchObj = new HibernateSearchObject<Mpegawai>(Mpegawai.class, getCountRows());
		searchObj.addSort("cnama", false);
		setSearchObj(searchObj);

		// Set the BindingListModel
		getPagedBindingListWrapper().init(searchObj, getListBoxMpegawai(), paging_MpegawaiList);
		BindingListModelList lml = (BindingListModelList) getListBoxMpegawai().getModel();
		setMpegawais(lml);

		// check if first time opened and init databinding for selectedBean
		if (getSelectedMpegawai() == null) {
			// init the bean with the first record in the List
			if (lml.getSize() > 0) {
				final int rowIndex = 0;
				// only for correct showing after Rendering. No effect as an
				// Event
				// yet.
				getListBoxMpegawai().setSelectedIndex(rowIndex);
				// get the first entry and cast them to the needed object
				setSelectedMpegawai((Mpegawai) lml.get(0));

				// call the onSelect Event for showing the objects data in the
				// statusBar
				Events.sendEvent(new Event("onSelect", getListBoxMpegawai(), getSelectedMpegawai()));
			}
		}

	}

	/**
	 * Selects the object in the listbox and change the tab.<br>
	 * Event is forwarded in the corresponding listbox.
	 */
	public void onDoubleClickedMpegawaiItem(Event event) {
		// logger.debug(event.toString());

		Mpegawai anMpegawai = getSelectedMpegawai();

		if (anMpegawai != null) {
			setSelectedMpegawai(anMpegawai);
			setMpegawai(anMpegawai);

			// check first, if the tabs are created
			if (getMpegawaiMainCtrl().getMpegawaiDetailCtrl() == null) {
				Events.sendEvent(new Event("onSelect", getMpegawaiMainCtrl().tabMpegawaiDetail, null));
				// if we work with spring beanCreation than we must check a
				// little bit deeper, because the Controller are preCreated ?
			} else if (getMpegawaiMainCtrl().getMpegawaiDetailCtrl().getBinder() == null) {
				Events.sendEvent(new Event("onSelect", getMpegawaiMainCtrl().tabMpegawaiDetail, null));
			}

			Events.sendEvent(new Event("onSelect", getMpegawaiMainCtrl().tabMpegawaiDetail, anMpegawai));
		}
	}

	/**
	 * When a listItem in the corresponding listbox is selected.<br>
	 * Event is forwarded in the corresponding listbox.
	 *
	 * @param event
	 */
	public void onSelect$listBoxMpegawai(Event event) {
		// logger.debug(event.toString());

		// selectedMpegawai is filled by annotated databinding mechanism
		Mpegawai anMpegawai = getSelectedMpegawai();

		if (anMpegawai == null) {
			return;
		}

		// check first, if the tabs are created
		if (getMpegawaiMainCtrl().getMpegawaiDetailCtrl() == null) {
			Events.sendEvent(new Event("onSelect", getMpegawaiMainCtrl().tabMpegawaiDetail, null));
			// if we work with spring beanCreation than we must check a little
			// bit deeper, because the Controller are preCreated ?
		} else if (getMpegawaiMainCtrl().getMpegawaiDetailCtrl().getBinder() == null) {
			Events.sendEvent(new Event("onSelect", getMpegawaiMainCtrl().tabMpegawaiDetail, null));
		}

		// INIT ALL RELATED Queries/OBJECTS/LISTS NEW
		getMpegawaiMainCtrl().getMpegawaiDetailCtrl().setSelectedMpegawai(anMpegawai);
		getMpegawaiMainCtrl().getMpegawaiDetailCtrl().setMpegawai(anMpegawai);

		// store the selected bean values as current
		getMpegawaiMainCtrl().doStoreInitValues();

		// show the objects data in the statusBar
		String str = "Pegawai " + ": " + anMpegawai.getCnama();
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
	 *
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
		borderLayout_mpegawaiList.setHeight(String.valueOf(maxListBoxHeight) + "px");

		windowMpegawaiList.invalidate();
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
	public Mpegawai getMpegawai() {
		// STORED IN THE module's MainController
		return getMpegawaiMainCtrl().getSelectedMpegawai();
	}

	public void setMpegawai(Mpegawai anMpegawai) {
		// STORED IN THE module's MainController
		getMpegawaiMainCtrl().setSelectedMpegawai(anMpegawai);
	}

	public void setSelectedMpegawai(Mpegawai selectedMpegawai) {
		// STORED IN THE module's MainController
		getMpegawaiMainCtrl().setSelectedMpegawai(selectedMpegawai);
	}

	public Mpegawai getSelectedMpegawai() {
		// STORED IN THE module's MainController
		return getMpegawaiMainCtrl().getSelectedMpegawai();
	}

	public void setMpegawais(BindingListModelList mpegawais) {
		// STORED IN THE module's MainController
		getMpegawaiMainCtrl().setMpegawais(mpegawais);
	}

	public BindingListModelList getMpegawais() {
		// STORED IN THE module's MainController
		return getMpegawaiMainCtrl().getMpegawais();
	}

	public void setBinder(AnnotateDataBinder binder) {
		this.binder = binder;
	}

	public AnnotateDataBinder getBinder() {
		return this.binder;
	}

	public void setSearchObj(HibernateSearchObject<Mpegawai> searchObj) {
		this.searchObj = searchObj;
	}

	public HibernateSearchObject<Mpegawai> getSearchObj() {
		return this.searchObj;
	}

	public void setMpegawaiService(MpegawaiService mpegawaiService) {
		this.mpegawaiService = mpegawaiService;
	}

	public MpegawaiService getMpegawaiService() {
		return this.mpegawaiService;
	}

	public Listbox getListBoxMpegawai() {
		return this.listBoxMpegawai;
	}

	public void setListBoxMpegawai(Listbox listBoxMpegawai) {
		this.listBoxMpegawai = listBoxMpegawai;
	}

	public int getCountRows() {
		return this.countRows;
	}

	public void setCountRows(int countRows) {
		this.countRows = countRows;
	}

	public void setMpegawaiMainCtrl(MpegawaiMainCtrl mpegawaiMainCtrl) {
		this.mpegawaiMainCtrl = mpegawaiMainCtrl;
	}

	public MpegawaiMainCtrl getMpegawaiMainCtrl() {
		return this.mpegawaiMainCtrl;
	}

}
