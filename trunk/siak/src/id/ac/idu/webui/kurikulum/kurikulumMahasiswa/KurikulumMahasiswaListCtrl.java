package id.ac.idu.webui.kurikulum.kurikulumMahasiswa;

import id.ac.idu.backend.model.Mkurmhs;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.kurikulum.service.KurikulumMahasiswaService;
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
 * Date: 3/9/12
 * Time: 3:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class KurikulumMahasiswaListCtrl extends GFCBaseListCtrl<Mkurmhs> implements Serializable {
    private static final long serialVersionUID = -1L;
	private static final Logger logger = Logger.getLogger(KurikulumMahasiswaListCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window windowKurikulumMahasiswaList; // autowired
	protected Panel panelKurikulumMahasiswaList; // autowired

	protected Borderlayout borderLayout_kurikulumMahasiswaList; // autowired
	protected Paging paging_KurikulumMahasiswaList; // autowired
	protected Listbox listBoxKurikulumMahasiswa; // autowired
	protected Listheader listheader_KurikulumMahasiswaList_Code; // autowired
	protected Listheader listheader_KurikulumMahasiswaList_Cohort; // autowired
	protected Listheader listheader_KurikulumMahasiswaList_Prodi; // autowired
    protected Listheader listheader_KurikulumMahasiswaList_Nim;
    protected Listheader listheader_KurikulumMahasiswaList_Nama;
    protected Listheader listheader_KurikulumMahasiswaList_Thajar;
    protected Listheader listheader_KurikulumMahasiswaList_Term;
	// NEEDED for ReUse in the SearchWindow
	private HibernateSearchObject<Mkurmhs> searchObj;

	// row count for listbox
	private int countRows;

	// Databinding
	private AnnotateDataBinder binder;
	private KurikulumMahasiswaMainCtrl kurikulumMahasiswaMainCtrl;

	// ServiceDAOs / Domain Classes
	private transient KurikulumMahasiswaService kurikulumMahasiswaService;

	/**
	 * default constructor.<br>
	 */
	public KurikulumMahasiswaListCtrl() {
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
			setKurikulumMahasiswaMainCtrl((KurikulumMahasiswaMainCtrl) arg.get("ModuleMainController"));

			// SET THIS CONTROLLER TO THE module's Parent/MainController
			getKurikulumMahasiswaMainCtrl().setKurikulumMahasiswaListCtrl(this);

			// Get the selected object.
			// Check if this Controller if created on first time. If so,
			// than the selectedXXXBean should be null
			if (getKurikulumMahasiswaMainCtrl().getSelectedKurikulumMahasiswa() != null) {
				setSelectedKurikulumMahasiswa(getKurikulumMahasiswaMainCtrl().getSelectedKurikulumMahasiswa());
			} else
				setSelectedKurikulumMahasiswa(null);
		} else {
			setSelectedKurikulumMahasiswa(null);
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

	public void onCreate$windowKurikulumMahasiswaList(Event event) throws Exception {
		binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

		doFillListbox();

		binder.loadAll();
	}

	public void doFillListbox() {

		doFitSize();

		// set the paging params
		paging_KurikulumMahasiswaList.setPageSize(getCountRows());
		paging_KurikulumMahasiswaList.setDetailed(true);

		// not used listheaders must be declared like ->
		// lh.setSortAscending(""); lh.setSortDescending("")
        listheader_KurikulumMahasiswaList_Nim.setSortAscending(new FieldComparator(ConstantUtil.MAHASISWA_DOT_NIM, true));
        listheader_KurikulumMahasiswaList_Nim.setSortDescending(new FieldComparator(ConstantUtil.MAHASISWA_DOT_NIM, false));
        listheader_KurikulumMahasiswaList_Nama.setSortAscending(new FieldComparator(ConstantUtil.MAHASISWA_DOT_NAMA, true));
        listheader_KurikulumMahasiswaList_Nama.setSortDescending(new FieldComparator(ConstantUtil.MAHASISWA_DOT_NAMA, false));
		listheader_KurikulumMahasiswaList_Code.setSortAscending(new FieldComparator(ConstantUtil.KURIKULUM_DOT_CODE, true));
		listheader_KurikulumMahasiswaList_Code.setSortDescending(new FieldComparator(ConstantUtil.KURIKULUM_DOT_CODE, false));
		listheader_KurikulumMahasiswaList_Cohort.setSortAscending(new FieldComparator(ConstantUtil.COHORT, true));
		listheader_KurikulumMahasiswaList_Cohort.setSortDescending(new FieldComparator(ConstantUtil.COHORT, false));
		listheader_KurikulumMahasiswaList_Prodi.setSortAscending(new FieldComparator(ConstantUtil.PRODI_DOT_NAME, true));
		listheader_KurikulumMahasiswaList_Prodi.setSortDescending(new FieldComparator(ConstantUtil.PRODI_DOT_NAME, false));
        listheader_KurikulumMahasiswaList_Thajar.setSortAscending(new FieldComparator(ConstantUtil.THAJAR, true));
        listheader_KurikulumMahasiswaList_Thajar.setSortDescending(new FieldComparator(ConstantUtil.THAJAR, false));
        listheader_KurikulumMahasiswaList_Term.setSortAscending(new FieldComparator(ConstantUtil.TERM, true));
        listheader_KurikulumMahasiswaList_Term.setSortDescending(new FieldComparator(ConstantUtil.TERM, false));
		// ++ create the searchObject and init sorting ++//
		// ++ create the searchObject and init sorting ++//
		searchObj = new HibernateSearchObject<Mkurmhs>(Mkurmhs.class, getCountRows());
		searchObj.addSort(ConstantUtil.KURIKULUM, false);
		setSearchObj(searchObj);

		// Set the BindingListModel
		getPagedBindingListWrapper().init(searchObj, getListBoxKurikulumMahasiswa(), paging_KurikulumMahasiswaList);
		BindingListModelList lml = (BindingListModelList) getListBoxKurikulumMahasiswa().getModel();
		setKurikulumMahasiswas(lml);

		// check if first time opened and init databinding for selectedBean
		if (getSelectedKurikulumMahasiswa() == null) {
			// init the bean with the first record in the List
			if (lml.getSize() > 0) {
				final int rowIndex = 0;
				// only for correct showing after Rendering. No effect as an
				// Event
				// yet.
				getListBoxKurikulumMahasiswa().setSelectedIndex(rowIndex);
				// get the first entry and cast them to the needed object
				setSelectedKurikulumMahasiswa((Mkurmhs) lml.get(0));

				// call the onSelect Event for showing the objects data in the
				// statusBar
				Events.sendEvent(new Event("onSelect", getListBoxKurikulumMahasiswa(), getSelectedKurikulumMahasiswa()));
			}
		}

	}

	/**
	 * Selects the object in the listbox and change the tab.<br>
	 * Event is forwarded in the corresponding listbox.
	 */
	public void onDoubleClickedKurikulumMahasiswaItem(Event event) {
		// logger.debug(event.toString());

		Mkurmhs anKurikulumMahasiswa = getSelectedKurikulumMahasiswa();

		if (anKurikulumMahasiswa != null) {
			setSelectedKurikulumMahasiswa(anKurikulumMahasiswa);
			setKurikulumMahasiswa(anKurikulumMahasiswa);

			// check first, if the tabs are created
			if (getKurikulumMahasiswaMainCtrl().getKurikulumMahasiswaDetailCtrl() == null) {
				Events.sendEvent(new Event("onSelect", getKurikulumMahasiswaMainCtrl().tabKurikulumMahasiswaDetail, null));
				// if we work with spring beanCreation than we must check a
				// little bit deeper, because the Controller are preCreated ?
			} else if (getKurikulumMahasiswaMainCtrl().getKurikulumMahasiswaDetailCtrl().getBinder() == null) {
				Events.sendEvent(new Event("onSelect", getKurikulumMahasiswaMainCtrl().tabKurikulumMahasiswaDetail, null));
			}

			Events.sendEvent(new Event("onSelect", getKurikulumMahasiswaMainCtrl().tabKurikulumMahasiswaDetail, anKurikulumMahasiswa));
		}
	}

	/**
	 * When a listItem in the corresponding listbox is selected.<br>
	 * Event is forwarded in the corresponding listbox.
	 *
	 * @param event
	 */
	public void onSelect$listBoxKurikulumMahasiswa(Event event) {
		// logger.debug(event.toString());

		// selectedKurikulumMahasiswa is filled by annotated databinding mechanism
		Mkurmhs anKurikulumMahasiswa = getSelectedKurikulumMahasiswa();

		if (anKurikulumMahasiswa == null) {
			return;
		}

		// check first, if the tabs are created
		if (getKurikulumMahasiswaMainCtrl().getKurikulumMahasiswaDetailCtrl() == null) {
			Events.sendEvent(new Event("onSelect", getKurikulumMahasiswaMainCtrl().tabKurikulumMahasiswaDetail, null));
			// if we work with spring beanCreation than we must check a little
			// bit deeper, because the Controller are preCreated ?
		} else if (getKurikulumMahasiswaMainCtrl().getKurikulumMahasiswaDetailCtrl().getBinder() == null) {
			Events.sendEvent(new Event("onSelect", getKurikulumMahasiswaMainCtrl().tabKurikulumMahasiswaDetail, null));
		}

		// INIT ALL RELATED Queries/OBJECTS/LISTS NEW
		getKurikulumMahasiswaMainCtrl().getKurikulumMahasiswaDetailCtrl().setSelectedKurikulumMahasiswa(anKurikulumMahasiswa);
		getKurikulumMahasiswaMainCtrl().getKurikulumMahasiswaDetailCtrl().setKurikulumMahasiswa(anKurikulumMahasiswa);

		// store the selected bean values as current
		getKurikulumMahasiswaMainCtrl().doStoreInitValues();

		// show the objects data in the statusBar
		String str = Labels.getLabel("common.KurikulumMahasiswa") + ": " + anKurikulumMahasiswa.getMmahasiswa().getCnama();
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
		borderLayout_kurikulumMahasiswaList.setHeight(String.valueOf(maxListBoxHeight) + "px");

		windowKurikulumMahasiswaList.invalidate();
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
	public Mkurmhs getKurikulumMahasiswa() {
		// STORED IN THE module's MainController
		return getKurikulumMahasiswaMainCtrl().getSelectedKurikulumMahasiswa();
	}

	public void setKurikulumMahasiswa(Mkurmhs anKurikulumMahasiswa) {
		// STORED IN THE module's MainController
		getKurikulumMahasiswaMainCtrl().setSelectedKurikulumMahasiswa(anKurikulumMahasiswa);
	}

	public void setSelectedKurikulumMahasiswa(Mkurmhs selectedKurikulumMahasiswa) {
		// STORED IN THE module's MainController
		getKurikulumMahasiswaMainCtrl().setSelectedKurikulumMahasiswa(selectedKurikulumMahasiswa);
	}

	public Mkurmhs getSelectedKurikulumMahasiswa() {
		// STORED IN THE module's MainController
		return getKurikulumMahasiswaMainCtrl().getSelectedKurikulumMahasiswa();
	}

	public void setKurikulumMahasiswas(BindingListModelList kurikulumMahasiswas) {
		// STORED IN THE module's MainController
		getKurikulumMahasiswaMainCtrl().setKurikulumMahasiswas(kurikulumMahasiswas);
	}

	public BindingListModelList getKurikulumMahasiswas() {
		// STORED IN THE module's MainController
		return getKurikulumMahasiswaMainCtrl().getKurikulumMahasiswas();
	}

	public void setBinder(AnnotateDataBinder binder) {
		this.binder = binder;
	}

	public AnnotateDataBinder getBinder() {
		return this.binder;
	}

	public void setSearchObj(HibernateSearchObject<Mkurmhs> searchObj) {
		this.searchObj = searchObj;
	}

	public HibernateSearchObject<Mkurmhs> getSearchObj() {
		return this.searchObj;
	}

	public void setKurikulumMahasiswaService(KurikulumMahasiswaService kurikulumMahasiswaService) {
		this.kurikulumMahasiswaService = kurikulumMahasiswaService;
	}

	public KurikulumMahasiswaService getKurikulumMahasiswaService() {
		return this.kurikulumMahasiswaService;
	}

	public Listbox getListBoxKurikulumMahasiswa() {
		return this.listBoxKurikulumMahasiswa;
	}

	public void setListBoxKurikulumMahasiswa(Listbox listBoxKurikulumMahasiswa) {
		this.listBoxKurikulumMahasiswa = listBoxKurikulumMahasiswa;
	}

	public int getCountRows() {
		return this.countRows;
	}

	public void setCountRows(int countRows) {
		this.countRows = countRows;
	}

	public void setKurikulumMahasiswaMainCtrl(KurikulumMahasiswaMainCtrl kurikulumMahasiswaMainCtrl) {
		this.kurikulumMahasiswaMainCtrl = kurikulumMahasiswaMainCtrl;
	}

	public KurikulumMahasiswaMainCtrl getKurikulumMahasiswaMainCtrl() {
		return this.kurikulumMahasiswaMainCtrl;
	}
}
