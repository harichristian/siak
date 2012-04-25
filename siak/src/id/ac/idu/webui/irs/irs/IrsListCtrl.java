package id.ac.idu.webui.irs.irs;

import id.ac.idu.backend.model.Tirspasca;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.irs.service.IrsService;
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
 * Date: 3/12/12
 * Time: 6:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class IrsListCtrl extends GFCBaseListCtrl<Tirspasca> implements Serializable {
    private static final long serialVersionUID = -1L;
	private static final Logger logger = Logger.getLogger(IrsListCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window windowIrsList; // autowired
	protected Panel panelIrsList; // autowired

	protected Borderlayout borderLayout_irsList; // autowired
	protected Paging paging_IrsList; // autowired
	protected Listbox listBoxIrs; // autowired
	protected Listheader listheader_IrsList_Nim; // autowired
    protected Listheader listheader_IrsList_Nama;
	protected Listheader listheader_IrsList_Sekolah; // autowired
	protected Listheader listheader_IrsList_Prodi; // autowired
	protected Listheader listheader_IrsList_Term;
	protected Listheader listheader_IrsList_Thajar;
	protected Listheader listheader_IrsList_Semester;

	// NEEDED for ReUse in the SearchWindow
	private HibernateSearchObject<Tirspasca> searchObj;

	// row count for listbox
	private int countRows;

	// Databinding
	private AnnotateDataBinder binder;
	private IrsMainCtrl irsMainCtrl;

	// ServiceDAOs / Domain Classes
	private transient IrsService irsService;

	/**
	 * default constructor.<br>
	 */
	public IrsListCtrl() {
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
			setIrsMainCtrl((IrsMainCtrl) arg.get("ModuleMainController"));

			// SET THIS CONTROLLER TO THE module's Parent/MainController
			getIrsMainCtrl().setIrsListCtrl(this);

			// Get the selected object.
			// Check if this Controller if created on first time. If so,
			// than the selectedXXXBean should be null
			if (getIrsMainCtrl().getSelectedIrs() != null) {
				setSelectedIrs(getIrsMainCtrl().getSelectedIrs());
			} else
				setSelectedIrs(null);
		} else {
			setSelectedIrs(null);
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

	public void onCreate$windowIrsList(Event event) throws Exception {
		binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

		doFillListbox();

		binder.loadAll();
	}

	public void doFillListbox() {

		doFitSize();

		// set the paging params
		paging_IrsList.setPageSize(getCountRows());
		paging_IrsList.setDetailed(true);

		// not used listheaders must be declared like ->
		// lh.setSortAscending(""); lh.setSortDescending("")
		listheader_IrsList_Nim.setSortAscending(new FieldComparator(ConstantUtil.MAHASISWA_DOT_NIM, true));
		listheader_IrsList_Nim.setSortDescending(new FieldComparator(ConstantUtil.MAHASISWA_DOT_NIM, false));
		listheader_IrsList_Nama.setSortAscending(new FieldComparator(ConstantUtil.MAHASISWA_DOT_NAMA, true));
        listheader_IrsList_Nama.setSortDescending(new FieldComparator(ConstantUtil.MAHASISWA_DOT_NAMA, false));
        listheader_IrsList_Sekolah.setSortAscending(new FieldComparator(ConstantUtil.SEKOLAH_DOT_NAME, true));
		listheader_IrsList_Sekolah.setSortDescending(new FieldComparator(ConstantUtil.SEKOLAH_DOT_NAME, false));
		listheader_IrsList_Prodi.setSortAscending(new FieldComparator(ConstantUtil.PRODI_DOT_NAME, true));
		listheader_IrsList_Prodi.setSortDescending(new FieldComparator(ConstantUtil.PRODI_DOT_NAME, false));
        listheader_IrsList_Term.setSortAscending(new FieldComparator(ConstantUtil.TERM, true));
        listheader_IrsList_Term.setSortDescending(new FieldComparator(ConstantUtil.TERM, false));
        listheader_IrsList_Thajar.setSortAscending(new FieldComparator(ConstantUtil.THAJAR, true));
        listheader_IrsList_Thajar.setSortDescending(new FieldComparator(ConstantUtil.THAJAR, false));
        listheader_IrsList_Semester.setSortAscending(new FieldComparator(ConstantUtil.SEMESTER, true));
        listheader_IrsList_Semester.setSortDescending(new FieldComparator(ConstantUtil.SEMESTER, false));
		// ++ create the searchObject and init sorting ++//
		// ++ create the searchObject and init sorting ++//
		searchObj = new HibernateSearchObject<Tirspasca>(Tirspasca.class, getCountRows());
		searchObj.addSort(ConstantUtil.MAHASISWA_DOT_NAMA, false);
		setSearchObj(searchObj);

		// Set the BindingListModel
		getPagedBindingListWrapper().init(searchObj, getListBoxIrs(), paging_IrsList);
		BindingListModelList lml = (BindingListModelList) getListBoxIrs().getModel();
		setIrss(lml);

		// check if first time opened and init databinding for selectedBean
		if (getSelectedIrs() == null) {
			// init the bean with the first record in the List
			if (lml.getSize() > 0) {
				final int rowIndex = 0;
				// only for correct showing after Rendering. No effect as an
				// Event
				// yet.
				getListBoxIrs().setSelectedIndex(rowIndex);
				// get the first entry and cast them to the needed object
				setSelectedIrs((Tirspasca) lml.get(0));

				// call the onSelect Event for showing the objects data in the
				// statusBar
				Events.sendEvent(new Event("onSelect", getListBoxIrs(), getSelectedIrs()));
			}
		}

	}

	/**
	 * Selects the object in the listbox and change the tab.<br>
	 * Event is forwarded in the corresponding listbox.
	 */
	public void onDoubleClickedIrsItem(Event event) {
		// logger.debug(event.toString());

		Tirspasca anIrs = getSelectedIrs();

		if (anIrs != null) {
			setSelectedIrs(anIrs);
			setIrs(anIrs);

			// check first, if the tabs are created
			if (getIrsMainCtrl().getIrsDetailCtrl() == null) {
				Events.sendEvent(new Event("onSelect", getIrsMainCtrl().tabIrsDetail, null));
				// if we work with spring beanCreation than we must check a
				// little bit deeper, because the Controller are preCreated ?
			} else if (getIrsMainCtrl().getIrsDetailCtrl().getBinder() == null) {
				Events.sendEvent(new Event("onSelect", getIrsMainCtrl().tabIrsDetail, null));
			}

			Events.sendEvent(new Event("onSelect", getIrsMainCtrl().tabIrsDetail, anIrs));
		}
	}

	/**
	 * When a listItem in the corresponding listbox is selected.<br>
	 * Event is forwarded in the corresponding listbox.
	 *
	 * @param event
	 */
	public void onSelect$listBoxIrs(Event event) {
		// logger.debug(event.toString());

		// selectedIrs is filled by annotated databinding mechanism
		Tirspasca anIrs = getSelectedIrs();

		if (anIrs == null) {
			return;
		}

		// check first, if the tabs are created
		if (getIrsMainCtrl().getIrsDetailCtrl() == null) {
			Events.sendEvent(new Event("onSelect", getIrsMainCtrl().tabIrsDetail, null));
			// if we work with spring beanCreation than we must check a little
			// bit deeper, because the Controller are preCreated ?
		} else if (getIrsMainCtrl().getIrsDetailCtrl().getBinder() == null) {
			Events.sendEvent(new Event("onSelect", getIrsMainCtrl().tabIrsDetail, null));
		}

		// INIT ALL RELATED Queries/OBJECTS/LISTS NEW
		getIrsMainCtrl().getIrsDetailCtrl().setSelectedIrs(anIrs);
		getIrsMainCtrl().getIrsDetailCtrl().setIrs(anIrs);

		// store the selected bean values as current
		getIrsMainCtrl().doStoreInitValues();

		// show the objects data in the statusBar
		String str = Labels.getLabel("common.Irs") + ": " + anIrs.getMmahasiswa().getCnama();
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
		borderLayout_irsList.setHeight(String.valueOf(maxListBoxHeight) + "px");

		windowIrsList.invalidate();
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
	public Tirspasca getIrs() {
		// STORED IN THE module's MainController
		return getIrsMainCtrl().getSelectedIrs();
	}

	public void setIrs(Tirspasca anIrs) {
		// STORED IN THE module's MainController
		getIrsMainCtrl().setSelectedIrs(anIrs);
	}

	public void setSelectedIrs(Tirspasca selectedIrs) {
		// STORED IN THE module's MainController
		getIrsMainCtrl().setSelectedIrs(selectedIrs);
	}

	public Tirspasca getSelectedIrs() {
		// STORED IN THE module's MainController
		return getIrsMainCtrl().getSelectedIrs();
	}

	public void setIrss(BindingListModelList irss) {
		// STORED IN THE module's MainController
		getIrsMainCtrl().setIrss(irss);
	}

	public BindingListModelList getIrss() {
		// STORED IN THE module's MainController
		return getIrsMainCtrl().getIrss();
	}

	public void setBinder(AnnotateDataBinder binder) {
		this.binder = binder;
	}

	public AnnotateDataBinder getBinder() {
		return this.binder;
	}

	public void setSearchObj(HibernateSearchObject<Tirspasca> searchObj) {
		this.searchObj = searchObj;
	}

	public HibernateSearchObject<Tirspasca> getSearchObj() {
		return this.searchObj;
	}

	public void setIrsService(IrsService irsService) {
		this.irsService = irsService;
	}

	public IrsService getIrsService() {
		return this.irsService;
	}

	public Listbox getListBoxIrs() {
		return this.listBoxIrs;
	}

	public void setListBoxIrs(Listbox listBoxIrs) {
		this.listBoxIrs = listBoxIrs;
	}

	public int getCountRows() {
		return this.countRows;
	}

	public void setCountRows(int countRows) {
		this.countRows = countRows;
	}

	public void setIrsMainCtrl(IrsMainCtrl irsMainCtrl) {
		this.irsMainCtrl = irsMainCtrl;
	}

	public IrsMainCtrl getIrsMainCtrl() {
		return this.irsMainCtrl;
	}
}
