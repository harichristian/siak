package id.ac.idu.webui.irs.paket;

import id.ac.idu.backend.model.Tpaketkuliah;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.irs.service.PaketService;
import id.ac.idu.util.ConstantUtil;
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
 * User: valeo
 * Date: 3/13/12
 * Time: 10:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class PaketListCtrl extends GFCBaseListCtrl<Tpaketkuliah> implements Serializable {
    private static final long serialVersionUID = -1L;
	private static final Logger logger = Logger.getLogger(PaketListCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window windowPaketList; // autowired
	protected Panel panelPaketList; // autowired

	protected Borderlayout borderLayout_paketList; // autowired
	protected Paging paging_PaketList; // autowired
	protected Listbox listBoxPaket; // autowired
	protected Listheader listheader_PaketList_Term; // autowired
	protected Listheader listheader_PaketList_Thajar; // autowired
	protected Listheader listheader_PaketList_Prodi; // autowired

	// NEEDED for ReUse in the SearchWindow
	private HibernateSearchObject<Tpaketkuliah> searchObj;

	// row count for listbox
	private int countRows;

	// Databinding
	private AnnotateDataBinder binder;
	private PaketMainCtrl paketMainCtrl;

	// ServiceDAOs / Domain Classes
	private transient PaketService paketService;

	/**
	 * default constructor.<br>
	 */
	public PaketListCtrl() {
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
			setPaketMainCtrl((PaketMainCtrl) arg.get("ModuleMainController"));

			// SET THIS CONTROLLER TO THE module's Parent/MainController
			getPaketMainCtrl().setPaketListCtrl(this);

			// Get the selected object.
			// Check if this Controller if created on fpakett time. If so,
			// than the selectedXXXBean should be null
			if (getPaketMainCtrl().getSelectedPaket() != null) {
				setSelectedPaket(getPaketMainCtrl().getSelectedPaket());
			} else
				setSelectedPaket(null);
		} else {
			setSelectedPaket(null);
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

	public void onCreate$windowPaketList(Event event) throws Exception {
		binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

		doFillListbox();

		binder.loadAll();
	}

	public void doFillListbox() {

		doFitSize();

		// set the paging params
		paging_PaketList.setPageSize(getCountRows());
		paging_PaketList.setDetailed(true);

		// not used listheaders must be declared like ->
		// lh.setSortAscending(""); lh.setSortDescending("")
		listheader_PaketList_Term.setSortAscending(new FieldComparator(ConstantUtil.TERM, true));
		listheader_PaketList_Term.setSortDescending(new FieldComparator(ConstantUtil.TERM, false));
		listheader_PaketList_Thajar.setSortAscending(new FieldComparator(ConstantUtil.THAJAR, true));
		listheader_PaketList_Thajar.setSortDescending(new FieldComparator(ConstantUtil.THAJAR, false));
		listheader_PaketList_Prodi.setSortAscending(new FieldComparator(ConstantUtil.PRODI, true));
		listheader_PaketList_Prodi.setSortDescending(new FieldComparator(ConstantUtil.PRODI, false));

		// ++ create the searchObject and init sorting ++//
		// ++ create the searchObject and init sorting ++//
		searchObj = new HibernateSearchObject<Tpaketkuliah>(Tpaketkuliah.class, getCountRows());
		searchObj.addSort(ConstantUtil.TERM, false);
		setSearchObj(searchObj);

		// Set the BindingListModel
		getPagedBindingListWrapper().init(searchObj, getListBoxPaket(), paging_PaketList);
		BindingListModelList lml = (BindingListModelList) getListBoxPaket().getModel();
		setPakets(lml);

		// check if fpakett time opened and init databinding for selectedBean
		if (getSelectedPaket() == null) {
			// init the bean with the fpakett record in the List
			if (lml.getSize() > 0) {
				final int rowIndex = 0;
				// only for correct showing after Rendering. No effect as an
				// Event
				// yet.
				getListBoxPaket().setSelectedIndex(rowIndex);
				// get the fpakett entry and cast them to the needed object
				setSelectedPaket((Tpaketkuliah) lml.get(0));

				// call the onSelect Event for showing the objects data in the
				// statusBar
				Events.sendEvent(new Event("onSelect", getListBoxPaket(), getSelectedPaket()));
			}
		}

	}

	/**
	 * Selects the object in the listbox and change the tab.<br>
	 * Event is forwarded in the corresponding listbox.
	 */
	public void onDoubleClickedPaketItem(Event event) {
		// logger.debug(event.toString());

		Tpaketkuliah anPaket = getSelectedPaket();

		if (anPaket != null) {
			setSelectedPaket(anPaket);
			setPaket(anPaket);

			// check fpakett, if the tabs are created
			if (getPaketMainCtrl().getPaketDetailCtrl() == null) {
				Events.sendEvent(new Event("onSelect", getPaketMainCtrl().tabPaketDetail, null));
				// if we work with spring beanCreation than we must check a
				// little bit deeper, because the Controller are preCreated ?
			} else if (getPaketMainCtrl().getPaketDetailCtrl().getBinder() == null) {
				Events.sendEvent(new Event("onSelect", getPaketMainCtrl().tabPaketDetail, null));
			}

			Events.sendEvent(new Event("onSelect", getPaketMainCtrl().tabPaketDetail, anPaket));
		}
	}

	/**
	 * When a listItem in the corresponding listbox is selected.<br>
	 * Event is forwarded in the corresponding listbox.
	 *
	 * @param event
	 */
	public void onSelect$listBoxPaket(Event event) {
		// logger.debug(event.toString());

		// selectedPaket is filled by annotated databinding mechanism
		Tpaketkuliah anPaket = getSelectedPaket();

		if (anPaket == null) {
			return;
		}

		// check fpakett, if the tabs are created
		if (getPaketMainCtrl().getPaketDetailCtrl() == null) {
			Events.sendEvent(new Event("onSelect", getPaketMainCtrl().tabPaketDetail, null));
			// if we work with spring beanCreation than we must check a little
			// bit deeper, because the Controller are preCreated ?
		} else if (getPaketMainCtrl().getPaketDetailCtrl().getBinder() == null) {
			Events.sendEvent(new Event("onSelect", getPaketMainCtrl().tabPaketDetail, null));
		}

		// INIT ALL RELATED Queries/OBJECTS/LISTS NEW
		getPaketMainCtrl().getPaketDetailCtrl().setSelectedPaket(anPaket);
		getPaketMainCtrl().getPaketDetailCtrl().setPaket(anPaket);

		// store the selected bean values as current
		getPaketMainCtrl().doStoreInitValues();

		// show the objects data in the statusBar
		String str = Labels.getLabel("common.Paket") + ": " + anPaket.getCterm();
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
		borderLayout_paketList.setHeight(String.valueOf(maxListBoxHeight) + "px");

		windowPaketList.invalidate();
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
	public Tpaketkuliah getPaket() {
		// STORED IN THE module's MainController
		return getPaketMainCtrl().getSelectedPaket();
	}

	public void setPaket(Tpaketkuliah anPaket) {
		// STORED IN THE module's MainController
		getPaketMainCtrl().setSelectedPaket(anPaket);
	}

	public void setSelectedPaket(Tpaketkuliah selectedPaket) {
		// STORED IN THE module's MainController
		getPaketMainCtrl().setSelectedPaket(selectedPaket);
	}

	public Tpaketkuliah getSelectedPaket() {
		// STORED IN THE module's MainController
		return getPaketMainCtrl().getSelectedPaket();
	}

	public void setPakets(BindingListModelList pakets) {
		// STORED IN THE module's MainController
		getPaketMainCtrl().setPakets(pakets);
	}

	public BindingListModelList getPakets() {
		// STORED IN THE module's MainController
		return getPaketMainCtrl().getPakets();
	}

	public void setBinder(AnnotateDataBinder binder) {
		this.binder = binder;
	}

	public AnnotateDataBinder getBinder() {
		return this.binder;
	}

	public void setSearchObj(HibernateSearchObject<Tpaketkuliah> searchObj) {
		this.searchObj = searchObj;
	}

	public HibernateSearchObject<Tpaketkuliah> getSearchObj() {
		return this.searchObj;
	}

	public void setPaketService(PaketService paketService) {
		this.paketService = paketService;
	}

	public PaketService getPaketService() {
		return this.paketService;
	}

	public Listbox getListBoxPaket() {
		return this.listBoxPaket;
	}

	public void setListBoxPaket(Listbox listBoxPaket) {
		this.listBoxPaket = listBoxPaket;
	}

	public int getCountRows() {
		return this.countRows;
	}

	public void setCountRows(int countRows) {
		this.countRows = countRows;
	}

	public void setPaketMainCtrl(PaketMainCtrl paketMainCtrl) {
		this.paketMainCtrl = paketMainCtrl;
	}

	public PaketMainCtrl getPaketMainCtrl() {
		return this.paketMainCtrl;
	}
}
