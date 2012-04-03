package id.ac.idu.webui.kurikulum.updateKurikulumProdi;

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
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 4/3/12
 * Time: 10:41 AM
 */
public class KurikulumProdiListCtrl extends GFCBaseListCtrl<Mkurmhs> implements Serializable {
    private static final long serialVersionUID = -1L;
	private static final Logger logger = Logger.getLogger(KurikulumProdiListCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window windowKurikulumProdiList; // autowired
	protected Panel panelKurikulumMahasiswaList; // autowired

	protected Borderlayout borderLayout_kurikulumMahasiswaList; // autowired
	protected Paging paging_KurikulumMahasiswaList; // autowired
	protected Listbox listBoxKurikulumMahasiswa; // autowired
	protected Listheader listheader_KurikulumMahasiswaList_Code; // autowired
	protected Listheader listheader_KurikulumMahasiswaList_Cohort; // autowired
	protected Listheader listheader_KurikulumMahasiswaList_Prodi; // autowired

	// NEEDED for ReUse in the SearchWindow
	private HibernateSearchObject<Mkurmhs> searchObj;

	// row count for listbox
	private int countRows;

	// Databinding
	private AnnotateDataBinder binder;
	private KurikulumProdiMainCtrl kurikulumProdiMainCtrl;

	// ServiceDAOs / Domain Classes
	private transient KurikulumMahasiswaService kurikulumMahasiswaService;

	/**
	 * default constructor.<br>
	 */
	public KurikulumProdiListCtrl() {
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
			setKurikulumProdiMainCtrl((KurikulumProdiMainCtrl) arg.get("ModuleMainController"));

			// SET THIS CONTROLLER TO THE module's Parent/MainController
			getKurikulumProdiMainCtrl().setKurikulumProdiListCtrl(this);

			// Get the selected object.
			// Check if this Controller if created on first time. If so,
			// than the selectedXXXBean should be null
			if (getKurikulumProdiMainCtrl().getSelectedKurikulumMahasiswa() != null) {
				setSelectedKurikulumMahasiswa(getKurikulumProdiMainCtrl().getSelectedKurikulumMahasiswa());
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

	public void onCreate$windowKurikulumProdiList(Event event) throws Exception {
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
		listheader_KurikulumMahasiswaList_Code.setSortAscending(new FieldComparator(ConstantUtil.KURIKULUM, true));
		listheader_KurikulumMahasiswaList_Code.setSortDescending(new FieldComparator(ConstantUtil.KURIKULUM, false));
		listheader_KurikulumMahasiswaList_Cohort.setSortAscending(new FieldComparator(ConstantUtil.COHORT, true));
		listheader_KurikulumMahasiswaList_Cohort.setSortDescending(new FieldComparator(ConstantUtil.COHORT, false));
		listheader_KurikulumMahasiswaList_Prodi.setSortAscending(new FieldComparator(ConstantUtil.PRODI, true));
		listheader_KurikulumMahasiswaList_Prodi.setSortDescending(new FieldComparator(ConstantUtil.PRODI, false));

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
			if (getKurikulumProdiMainCtrl().getKurikulumProdiDetailCtrl() == null) {
				Events.sendEvent(new Event("onSelect", getKurikulumProdiMainCtrl().tabKurikulumMahasiswaDetail, null));
				// if we work with spring beanCreation than we must check a
				// little bit deeper, because the Controller are preCreated ?
			} else if (getKurikulumProdiMainCtrl().getKurikulumProdiDetailCtrl().getBinder() == null) {
				Events.sendEvent(new Event("onSelect", getKurikulumProdiMainCtrl().tabKurikulumMahasiswaDetail, null));
			}

			Events.sendEvent(new Event("onSelect", getKurikulumProdiMainCtrl().tabKurikulumMahasiswaDetail, anKurikulumMahasiswa));
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
		if (getKurikulumProdiMainCtrl().getKurikulumProdiDetailCtrl() == null) {
			Events.sendEvent(new Event("onSelect", getKurikulumProdiMainCtrl().tabKurikulumMahasiswaDetail, null));
			// if we work with spring beanCreation than we must check a little
			// bit deeper, because the Controller are preCreated ?
		} else if (getKurikulumProdiMainCtrl().getKurikulumProdiDetailCtrl().getBinder() == null) {
			Events.sendEvent(new Event("onSelect", getKurikulumProdiMainCtrl().tabKurikulumMahasiswaDetail, null));
		}

		// INIT ALL RELATED Queries/OBJECTS/LISTS NEW
		getKurikulumProdiMainCtrl().getKurikulumProdiDetailCtrl().setSelectedKurikulumMahasiswa(anKurikulumMahasiswa);
		getKurikulumProdiMainCtrl().getKurikulumProdiDetailCtrl().setKurikulumMahasiswa(anKurikulumMahasiswa);

		// store the selected bean values as current
		getKurikulumProdiMainCtrl().doStoreInitValues();

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

		windowKurikulumProdiList.invalidate();
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
		return getKurikulumProdiMainCtrl().getSelectedKurikulumMahasiswa();
	}

	public void setKurikulumMahasiswa(Mkurmhs anKurikulumMahasiswa) {
		// STORED IN THE module's MainController
		getKurikulumProdiMainCtrl().setSelectedKurikulumMahasiswa(anKurikulumMahasiswa);
	}

	public void setSelectedKurikulumMahasiswa(Mkurmhs selectedKurikulumMahasiswa) {
		// STORED IN THE module's MainController
		getKurikulumProdiMainCtrl().setSelectedKurikulumMahasiswa(selectedKurikulumMahasiswa);
	}

	public Mkurmhs getSelectedKurikulumMahasiswa() {
		// STORED IN THE module's MainController
		return getKurikulumProdiMainCtrl().getSelectedKurikulumMahasiswa();
	}

	public void setKurikulumMahasiswas(BindingListModelList kurikulumMahasiswas) {
		// STORED IN THE module's MainController
		getKurikulumProdiMainCtrl().setKurikulumMahasiswas(kurikulumMahasiswas);
	}

	public BindingListModelList getKurikulumMahasiswas() {
		// STORED IN THE module's MainController
		return getKurikulumProdiMainCtrl().getKurikulumMahasiswas();
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

	public void setKurikulumProdiMainCtrl(KurikulumProdiMainCtrl kurikulumProdiMainCtrl) {
		this.kurikulumProdiMainCtrl = kurikulumProdiMainCtrl;
	}

	public KurikulumProdiMainCtrl getKurikulumProdiMainCtrl() {
		return this.kurikulumProdiMainCtrl;
	}
}
