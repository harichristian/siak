package id.ac.idu.webui.kurikulum.updateKurikulumProdi;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.ProdiService;
import id.ac.idu.backend.model.Mkurmhs;
import id.ac.idu.backend.model.Mmahasiswa;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.kurikulum.service.KurikulumMahasiswaService;
import id.ac.idu.util.ConstantUtil;
import id.ac.idu.webui.util.*;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.*;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 4/3/12
 * Time: 10:41 AM
 */
public class KurikulumProdiMainCtrl extends GFCBaseCtrl implements Serializable {

    private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(KurikulumProdiMainCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window windowKurikulumProdiMain; // autowired

	// Tabs
	protected Tabbox tabbox_KurikulumMahasiswaMain; // autowired
	protected Tab tabKurikulumMahasiswaList; // autowired
	protected Tab tabKurikulumMahasiswaDetail; // autowired
	protected Tabpanel tabPanelKurikulumMahasiswaList; // autowired
	protected Tabpanel tabPanelKurikulumMahasiswaDetail; // autowired

	// filter components
	protected Checkbox checkbox_KurikulumMahasiswaList_ShowAll; // autowired
	protected Textbox txtb_KurikulumMahasiswa_Code; // aurowired
	protected Button button_KurikulumMahasiswaList_SearchCode; // aurowired
	protected Textbox txtb_KurikulumMahasiswa_Cohort; // aurowired
	protected Button button_KurikulumMahasiswaList_SearchCohort; // aurowired
	//protected Textbox txtb_KurikulumMahasiswa_Prodi; // aurowired
	//protected Button button_KurikulumMahasiswaList_SearchProdi; // aurowired

	// checkRights
	protected Button button_KurikulumMahasiswaList_PrintList;

	// Button controller for the CRUD buttons
	private final String btnCtroller_ClassPrefix = "button_KurikulumMahasiswaMain_";
	private ButtonStatusCtrl btnCtrlKurikulumMahasiswa;
	protected Button btnNew; // autowired
	protected Button btnEdit; // autowired
	protected Button btnDelete; // autowired
	protected Button btnSave; // autowired
	protected Button btnCancel; // autowired

	protected Button btnHelp;

	// Tab-Controllers for getting the binders
	private KurikulumProdiListCtrl kurikulumProdiListCtrl;
	private KurikulumProdiDetailCtrl kurikulumProdiDetailCtrl;

	// Databinding
	private Mkurmhs selectedKurikulumMahasiswa;
	private BindingListModelList kurikulumMahasiswas;

	// ServiceDAOs / Domain Classes
	private KurikulumMahasiswaService kurikulumMahasiswaService;
	private ProdiService prodiService;

	// always a copy from the bean before modifying. Used for reseting
	private Mkurmhs originalKurikulumMahasiswa;
    private Mmahasiswa mahasiswa;

	/**
	 * default constructor.<br>
	 */
	public KurikulumProdiMainCtrl() {
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
	public void onCreate$windowKurikulumProdiMain(Event event) throws Exception {
		windowKurikulumProdiMain.setContentStyle("padding:0px;");

		// create the Button Controller. Disable not used buttons during working
		btnCtrlKurikulumMahasiswa = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

		//doCheckRights();

		/**
		 * Initiate the first loading by selecting the customerList tab and
		 * create the components from the zul-file.
		 */
		tabKurikulumMahasiswaList.setSelected(true);

		if (tabPanelKurikulumMahasiswaList != null) {
			ZksampleCommonUtils.createTabPanelContent(tabPanelKurikulumMahasiswaList, this, "ModuleMainController", "/WEB-INF/pages/kurikulum/updateKurikulumProdi/kurikulumProdiList.zul");
		}

		// init the buttons for editMode
		btnCtrlKurikulumMahasiswa.setInitEdit();
	}

	/**
	 * When the tab 'tabKurikulumMahasiswaList' is selected.<br>
	 * Loads the zul-file into the tab.
	 *
	 * @param event
	 * @throws java.io.IOException
	 */
	public void onSelect$tabKurikulumMahasiswaList(Event event) throws IOException {
		logger.debug(event.toString());

		// Check if the tabpanel is already loaded
		if (tabPanelKurikulumMahasiswaList.getFirstChild() != null) {
			tabKurikulumMahasiswaList.setSelected(true);

			return;
		}

		if (tabPanelKurikulumMahasiswaList != null) {
			ZksampleCommonUtils.createTabPanelContent(tabPanelKurikulumMahasiswaList, this, "ModuleMainController", "/WEB-INF/pages/kurikulum/updateKurikulumProdi/kurikulumProdiList.zul");
		}

	}

	/**
	 * When the tab 'tabPanelKurikulumMahasiswaDetail' is selected.<br>
	 * Loads the zul-file into the tab.
	 *
	 * @param event
	 * @throws IOException
	 */
	public void onSelect$tabKurikulumMahasiswaDetail(Event event) throws IOException {
		// logger.debug(event.toString());

		// Check if the tabpanel is already loaded
		if (tabPanelKurikulumMahasiswaDetail.getFirstChild() != null) {
			tabKurikulumMahasiswaDetail.setSelected(true);

			// refresh the Binding mechanism
			getKurikulumProdiDetailCtrl().setKurikulumMahasiswa(getSelectedKurikulumMahasiswa());
			getKurikulumProdiDetailCtrl().getBinder().loadAll();
			return;
		}

		if (tabPanelKurikulumMahasiswaDetail != null) {
			ZksampleCommonUtils.createTabPanelContent(tabPanelKurikulumMahasiswaDetail, this, "ModuleMainController", "/WEB-INF/pages/kurikulum/updateKurikulumProdi/kurikulumProdiDetail.zul");
		}
	}

	/**
	 * when the "print kurikulumMahasiswas list" button is clicked.
	 *
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$button_KurikulumMahasiswaList_PrintList(Event event) throws InterruptedException {
		final Window win = (Window) Path.getComponent("/outerIndexWindow");
		//new KurikulumMahasiswaSimpleDJReport(win);
	}

	/**
	 * when the checkBox 'Show All' for filtering is checked. <br>
	 *
	 * @param event
	 */
	public void onCheck$checkbox_KurikulumMahasiswaList_ShowAll(Event event) {
		// logger.debug(event.toString());

		// empty the text search boxes
		txtb_KurikulumMahasiswa_Code.setValue(""); // clear
		txtb_KurikulumMahasiswa_Cohort.setValue(""); // clear
		//txtb_KurikulumMahasiswa_Prodi.setValue(""); // clear

		// ++ create the searchObject and init sorting ++//
		HibernateSearchObject<Mkurmhs> soKurikulumMahasiswa = new HibernateSearchObject<Mkurmhs>(Mkurmhs.class, getKurikulumProdiListCtrl().getCountRows());
		soKurikulumMahasiswa.addSort(ConstantUtil.ID, false);

		// Change the BindingListModel.
		if (getKurikulumProdiListCtrl().getBinder() != null) {
			getKurikulumProdiListCtrl().getPagedBindingListWrapper().setSearchObject(soKurikulumMahasiswa);

			// get the current Tab for later checking if we must change it
			Tab currentTab = tabbox_KurikulumMahasiswaMain.getSelectedTab();

			// check if the tab is one of the Detail tabs. If so do not
			// change the selection of it
			if (!currentTab.equals(tabKurikulumMahasiswaList)) {
				tabKurikulumMahasiswaList.setSelected(true);
			} else {
				currentTab.setSelected(true);
			}
		}

	}

	/**
	 * Filter the kurikulumMahasiswa list with 'like kurikulumMahasiswa number'. <br>
	 */
	public void onClick$button_KurikulumMahasiswaList_SearchCode(Event event) throws Exception {
		// logger.debug(event.toString());

		// if not empty
		if (!txtb_KurikulumMahasiswa_Code.getValue().isEmpty()) {
			checkbox_KurikulumMahasiswaList_ShowAll.setChecked(false); // unCheck
			txtb_KurikulumMahasiswa_Cohort.setValue(""); // clear
			//txtb_KurikulumMahasiswa_Prodi.setValue(""); // clear

			// ++ create the searchObject and init sorting ++//
			HibernateSearchObject<Mkurmhs> soKurikulumMahasiswa = new HibernateSearchObject<Mkurmhs>(Mkurmhs.class, getKurikulumProdiListCtrl().getCountRows());
			soKurikulumMahasiswa.addFilter(new Filter(ConstantUtil.KURIKULUM_DOT_CODE, "%" + txtb_KurikulumMahasiswa_Code.getValue() + "%", Filter.OP_ILIKE));
			soKurikulumMahasiswa.addSort(ConstantUtil.KURIKULUM_DOT_CODE, false);

			// Change the BindingListModel.
			if (getKurikulumProdiListCtrl().getBinder() != null) {
				getKurikulumProdiListCtrl().getPagedBindingListWrapper().setSearchObject(soKurikulumMahasiswa);

				// get the current Tab for later checking if we must change it
				Tab currentTab = tabbox_KurikulumMahasiswaMain.getSelectedTab();

				// check if the tab is one of the Detail tabs. If so do not
				// change the selection of it
				if (!currentTab.equals(tabKurikulumMahasiswaList)) {
					tabKurikulumMahasiswaList.setSelected(true);
				} else {
					currentTab.setSelected(true);
				}
			}
		}
	}

	/**
	 * Filter the kurikulumMahasiswa list with 'like kurikulumMahasiswa name'. <br>
	 */
	public void onClick$button_KurikulumMahasiswaList_SearchCohort(Event event) throws Exception {
		// logger.debug(event.toString());

		// if not empty
		if (!txtb_KurikulumMahasiswa_Cohort.getValue().isEmpty()) {
			checkbox_KurikulumMahasiswaList_ShowAll.setChecked(false); // unCheck
			//txtb_KurikulumMahasiswa_Prodi.setValue(""); // clear
			txtb_KurikulumMahasiswa_Code.setValue(""); // clear

			// ++ create the searchObject and init sorting ++//
			HibernateSearchObject<Mkurmhs> soKurikulumMahasiswa = new HibernateSearchObject<Mkurmhs>(Mkurmhs.class, getKurikulumProdiListCtrl().getCountRows());
			soKurikulumMahasiswa.addFilter(new Filter(ConstantUtil.COHORT, "%" + txtb_KurikulumMahasiswa_Cohort.getValue() + "%", Filter.OP_ILIKE));
			soKurikulumMahasiswa.addSort(ConstantUtil.COHORT, false);

			// Change the BindingListModel.
			if (getKurikulumProdiListCtrl().getBinder() != null) {
				getKurikulumProdiListCtrl().getPagedBindingListWrapper().setSearchObject(soKurikulumMahasiswa);

				// get the current Tab for later checking if we must change it
				Tab currentTab = tabbox_KurikulumMahasiswaMain.getSelectedTab();

				// check if the tab is one of the Detail tabs. If so do not
				// change the selection of it
				if (!currentTab.equals(tabKurikulumMahasiswaList)) {
					tabKurikulumMahasiswaList.setSelected(true);
				} else {
					currentTab.setSelected(true);
				}
			}
		}
	}

	/**
	 * Filter the kurikulumMahasiswa list with 'like kurikulumMahasiswa'. <br>
	 */
	public void onClick$button_KurikulumMahasiswaList_SearchProdi(Event event) throws Exception {
		// logger.debug(event.toString());
        /* Temporarily Disabled
		// if not empty
		if (!txtb_KurikulumMahasiswa_Prodi.getValue().isEmpty()) {
			checkbox_KurikulumMahasiswaList_ShowAll.setChecked(false); // unCheck
			txtb_KurikulumMahasiswa_Cohort.setValue(""); // clear
			txtb_KurikulumMahasiswa_Code.setValue(""); // clear

			// ++ create the searchObject and init sorting ++//
			HibernateSearchObject<MkurikulumMahasiswa> soKurikulumMahasiswa = new HibernateSearchObject<MkurikulumMahasiswa>(MkurikulumMahasiswa.class, getKurikulumMahasiswaListCtrl().getCountRows());
			soKurikulumMahasiswa.addFilter(new Filter(MkurikulumMahasiswa.PRODI, "%" + txtb_KurikulumMahasiswa_Prodi.getValue() + "%", Filter.OP_ILIKE));
			soKurikulumMahasiswa.addSort(MkurikulumMahasiswa.PRODI, false);

			// Change the BindingListModel.
			if (getKurikulumMahasiswaListCtrl().getBinder() != null) {
				getKurikulumMahasiswaListCtrl().getPagedBindingListWrapper().setSearchObject(soKurikulumMahasiswa);

				// get the current Tab for later checking if we must change it
				Tab currentTab = tabbox_KurikulumMahasiswaMain.getSelectedTab();

				// check if the tab is one of the Detail tabs. If so do not
				// change the selection of it
				if (!currentTab.equals(tabKurikulumMahasiswaList)) {
					tabKurikulumMahasiswaList.setSelected(true);
				} else {
					currentTab.setSelected(true);
				}
			}
		}
        */
	}

	/**
	 * When the "help" button is clicked.
	 *
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btnHelp(Event event) throws InterruptedException {
		doHelp(event);
	}

	/**
	 * When the "new" button is clicked.
	 *
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btnNew(Event event) throws InterruptedException {
		doNew(event);
	}

	/**
	 * When the "save" button is clicked.
	 *
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btnSave(Event event) throws InterruptedException {
		doSave(event);
	}

	/**
	 * When the "cancel" button is clicked.
	 *
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btnEdit(Event event) throws InterruptedException {
		doEdit(event);
	}

	/**
	 * When the "delete" button is clicked.
	 *
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btnDelete(Event event) throws InterruptedException {
		doDelete(event);
	}

	/**
	 * When the "cancel" button is clicked.
	 *
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btnCancel(Event event) throws InterruptedException {
		doCancel(event);
	}

	/**
	 * when the "refresh" button is clicked. <br>
	 * <br>
	 *
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$btnRefresh(Event event) throws InterruptedException {
		doResizeSelectedTab(event);
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// +++++++++++++++++ Business Logic ++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	/**
	 * 1. Cancel the current action.<br>
	 * 2. Reset the values to its origin.<br>
	 * 3. Set UI components back to readonly/disable mode.<br>
	 * 4. Set the buttons in edit mode.<br>
	 *
	 * @param event
	 * @throws InterruptedException
	 */
	private void doCancel(Event event) throws InterruptedException {
		// logger.debug(event.toString());

		// reset to the original object
		doResetToInitValues();

		// check first, if the tabs are created
		if (getKurikulumProdiDetailCtrl().getBinder() != null) {

			// refresh all dataBinder related controllers/components
			getKurikulumProdiDetailCtrl().getBinder().loadAll();

			// set editable Mode
			getKurikulumProdiDetailCtrl().doReadOnlyMode(true);

			btnCtrlKurikulumMahasiswa.setInitEdit();
		}
	}

	/**
	 * Sets all UI-components to writable-mode. Sets the buttons to edit-Mode.
	 * Checks first, if the NEEDED TABS with its contents are created. If not,
	 * than create it by simulate an onSelect() with calling Events.sendEvent()
	 *
	 * @param event
	 * @throws InterruptedException
	 */
	private void doEdit(Event event) {
		// logger.debug(event.toString());

		// get the current Tab for later checking if we must change it
		Tab currentTab = tabbox_KurikulumMahasiswaMain.getSelectedTab();

		// check first, if the tabs are created, if not than create it
		if (getKurikulumProdiDetailCtrl() == null) {
			Events.sendEvent(new Event("onSelect", tabKurikulumMahasiswaDetail, null));
			// if we work with spring beanCreation than we must check a little
			// bit deeper, because the Controller are preCreated ?
		} else if (getKurikulumProdiDetailCtrl().getBinder() == null) {
			Events.sendEvent(new Event("onSelect", tabKurikulumMahasiswaDetail, null));
		}

		// check if the tab is one of the Detail tabs. If so do not change the
		// selection of it
		if (!currentTab.equals(tabKurikulumMahasiswaDetail)) {
			tabKurikulumMahasiswaDetail.setSelected(true);
		} else {
			currentTab.setSelected(true);
		}

		getKurikulumProdiDetailCtrl().getBinder().loadAll();

		// remember the old vars
		doStoreInitValues();

		btnCtrlKurikulumMahasiswa.setBtnStatus_Edit();

		getKurikulumProdiDetailCtrl().doReadOnlyMode(false);
		// set focus
		getKurikulumProdiDetailCtrl().txtb_code.focus();
	}

	/**
	 * Deletes the selected Bean from the DB.
	 *
	 * @param event
	 * @throws InterruptedException
	 * @throws InterruptedException
	 */
	private void doDelete(Event event) throws InterruptedException {
		// logger.debug(event.toString());

		// check first, if the tabs are created, if not than create them
		if (getKurikulumProdiDetailCtrl().getBinder() == null) {
			Events.sendEvent(new Event("onSelect", tabKurikulumMahasiswaDetail, null));
		}

		// check first, if the tabs are created
		if (getKurikulumProdiDetailCtrl().getBinder() == null) {
			return;
		}

		final Mkurmhs anKurikulumMahasiswa = getSelectedKurikulumMahasiswa();
		if (anKurikulumMahasiswa != null) {

			// Show a confirm box
			final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anKurikulumMahasiswa.getMmahasiswa().getCnama();
			final String title = Labels.getLabel("message.Deleting.Record");

			MultiLineMessageBox.doSetTemplate();
			if (MultiLineMessageBox.show(msg, title, Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, true, new EventListener() {
				@Override
				public void onEvent(Event evt) {
					switch (((Integer) evt.getData()).intValue()) {
					case MultiLineMessageBox.YES:
						try {
							deleteBean();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break; //
					case MultiLineMessageBox.NO:
						break; //
					}
				}

				private void deleteBean() throws InterruptedException {
                    try {
                        getKurikulumMahasiswaService().delete(anKurikulumMahasiswa);
                    } catch (DataAccessException e) {
                        ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                    }
				}

			}

			) == MultiLineMessageBox.YES) {
			}

		}

		btnCtrlKurikulumMahasiswa.setInitEdit();

		setSelectedKurikulumMahasiswa(null);
		// refresh the list
		getKurikulumProdiListCtrl().doFillListbox();

		// refresh all dataBinder related controllers
		getKurikulumProdiDetailCtrl().getBinder().loadAll();
	}

	/**
	 * Saves all involved Beans to the DB.
	 *
	 * @param event
	 * @throws InterruptedException
	 */
	private void doSave(Event event) throws InterruptedException {
		//getKurikulumProdiDetailCtrl().getKurikulumMahasiswa().setMmahasiswa(getMahasiswa());
        // save all components data in the several tabs to the bean
		getKurikulumProdiDetailCtrl().getBinder().saveAll();

		try {
            // save it to database
			//getKurikulumMahasiswaService().saveOrUpdate(getKurikulumProdiDetailCtrl().getKurikulumMahasiswa());
            getSelectedKurikulumMahasiswa().setCterm(getKurikulumProdiDetailCtrl().txtb_termBaru.getValue());
            getSelectedKurikulumMahasiswa().setCthajar(getKurikulumProdiDetailCtrl().txtb_thajarBaru.getValue());
            getKurikulumMahasiswaService().saveOrUpdate(getSelectedKurikulumMahasiswa());
			// if saving is successfully than actualize the beans as
			// origins.
			doStoreInitValues();
			// refresh the list
			getKurikulumProdiListCtrl().doFillListbox();
			// later refresh StatusBar
			Events.postEvent("onSelect", getKurikulumProdiListCtrl().getListBoxKurikulumMahasiswa(), getSelectedKurikulumMahasiswa());

			// show the objects data in the statusBar
			String str = getSelectedKurikulumMahasiswa().getMmahasiswa().getCnama();
			EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

		} catch (DataAccessException e) {
			ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

			// Reset to init values
			doResetToInitValues();

			return;

		} finally {
			btnCtrlKurikulumMahasiswa.setInitEdit();
			getKurikulumProdiDetailCtrl().doReadOnlyMode(true);
		}
	}

	/**
	 * Sets all UI-components to writable-mode. Stores the current Beans as
	 * originBeans and get new Objects from the backend.
	 *
	 * @param event
	 * @throws InterruptedException
	 */
	private void doNew(Event event) {
		// logger.debug(event.toString());

		// check first, if the tabs are created
		if (getKurikulumProdiDetailCtrl() == null) {
			Events.sendEvent(new Event("onSelect", tabKurikulumMahasiswaDetail, null));
			// if we work with spring beanCreation than we must check a little
			// bit deeper, because the Controller are preCreated ?
		} else if (getKurikulumProdiDetailCtrl().getBinder() == null) {
			Events.sendEvent(new Event("onSelect", tabKurikulumMahasiswaDetail, null));
		}

		// remember the current object
		doStoreInitValues();

		/** !!! DO NOT BREAK THE TIERS !!! */
		// We don't create a new DomainObject() in the frontend.
		// We GET it from the backend.
		final Mkurmhs anKurikulumMahasiswa = getKurikulumMahasiswaService().getNewKurikulumMahasiswa();
        //final Mprodi anProdi = getProdiService().getAllProdis().get(0);
        //anKurikulumMahasiswa.setMprodi(anProdi);
		// set the beans in the related databinded controllers
		getKurikulumProdiDetailCtrl().setKurikulumMahasiswa(anKurikulumMahasiswa);
		getKurikulumProdiDetailCtrl().setSelectedKurikulumMahasiswa(anKurikulumMahasiswa);

		// Refresh the binding mechanism
		getKurikulumProdiDetailCtrl().setSelectedKurikulumMahasiswa(getSelectedKurikulumMahasiswa());
		if (getKurikulumProdiDetailCtrl().getBinder()!=null) {
            getKurikulumProdiDetailCtrl().getBinder().loadAll();
        }

		// set editable Mode
		getKurikulumProdiDetailCtrl().doReadOnlyMode(false);

		// set the ButtonStatus to New-Mode
		btnCtrlKurikulumMahasiswa.setInitNew();

		tabKurikulumMahasiswaDetail.setSelected(true);
		// set focus
		getKurikulumProdiDetailCtrl().txtb_code.focus();

	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++++++ Helpers ++++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	/**
	 * Resizes the container from the selected Tab.
	 *
	 * @param event
	 */
	private void doResizeSelectedTab(Event event) {
		// logger.debug(event.toString());

		if (tabbox_KurikulumMahasiswaMain.getSelectedTab() == tabKurikulumMahasiswaDetail) {
			getKurikulumProdiDetailCtrl().doFitSize(event);
		} else if (tabbox_KurikulumMahasiswaMain.getSelectedTab() == tabKurikulumMahasiswaList) {
			// resize and fill Listbox new
			getKurikulumProdiListCtrl().doFillListbox();
		}
	}

	/**
	 * Opens the help screen for the current module.
	 *
	 * @param event
	 * @throws InterruptedException
	 */
	private void doHelp(Event event) throws InterruptedException {

		ZksampleMessageUtils.doShowNotImplementedMessage();

		// we stop the propagation of the event, because zk will call ALL events
		// with the same name in the namespace and 'btnHelp' is a standard
		// button in this application and can often appears.
		// Events.getRealOrigin((ForwardEvent) event).stopPropagation();
		event.stopPropagation();
	}

	/**
	 * Saves the selected object's current properties. We can get them back if a
	 * modification is canceled.
	 *
	 */
	public void doStoreInitValues() {

		if (getSelectedKurikulumMahasiswa() != null) {

			try {
				setOriginalKurikulumMahasiswa((Mkurmhs) ZksampleBeanUtils.cloneBean(getSelectedKurikulumMahasiswa()));
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (final InstantiationException e) {
				throw new RuntimeException(e);
			} catch (final InvocationTargetException e) {
				throw new RuntimeException(e);
			} catch (final NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Reset the selected object to its origin property values.
	 *
	 *
	 */
	public void doResetToInitValues() {

		if (getOriginalKurikulumMahasiswa() != null) {

			try {
				setSelectedKurikulumMahasiswa((Mkurmhs) ZksampleBeanUtils.cloneBean(getOriginalKurikulumMahasiswa()));
				// TODO Bug in DataBinder??
				windowKurikulumProdiMain.invalidate();

			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (final InstantiationException e) {
				throw new RuntimeException(e);
			} catch (final InvocationTargetException e) {
				throw new RuntimeException(e);
			} catch (final NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * User rights check. <br>
	 * Only components are set visible=true if the logged-in <br>
	 * user have the right for it. <br>
	 *
	 * The rights are get from the spring framework users grantedAuthority(). A
	 * right is only a string. <br>
	 */
	// TODO move it to zul
	private void doCheckRights() {

		final UserWorkspace workspace = getUserWorkspace();

		// window_KurikulumMahasiswaList.setVisible(workspace.isAllowed("window_KurikulumMahasiswaList"));
		button_KurikulumMahasiswaList_PrintList.setVisible(workspace.isAllowed("button_KurikulumMahasiswaList_PrintList"));
		button_KurikulumMahasiswaList_SearchCode.setVisible(workspace.isAllowed("button_KurikulumMahasiswaList_SearchCode"));
		button_KurikulumMahasiswaList_SearchCohort.setVisible(workspace.isAllowed("button_KurikulumMahasiswaList_SearchName"));
		//button_KurikulumMahasiswaList_SearchProdi.setVisible(workspace.isAllowed("button_KurikulumMahasiswaList_SearchProdi"));

		btnHelp.setVisible(workspace.isAllowed("button_KurikulumMahasiswaMain_btnHelp"));
		btnNew.setVisible(workspace.isAllowed("button_KurikulumMahasiswaMain_btnNew"));
		btnEdit.setVisible(workspace.isAllowed("button_KurikulumMahasiswaMain_btnEdit"));
		btnDelete.setVisible(workspace.isAllowed("button_KurikulumMahasiswaMain_btnDelete"));
		btnSave.setVisible(workspace.isAllowed("button_KurikulumMahasiswaMain_btnSave"));

	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	public void setOriginalKurikulumMahasiswa(Mkurmhs originalKurikulumMahasiswa) {
		this.originalKurikulumMahasiswa = originalKurikulumMahasiswa;
	}

	public Mkurmhs getOriginalKurikulumMahasiswa() {
		return this.originalKurikulumMahasiswa;
	}

	public void setSelectedKurikulumMahasiswa(Mkurmhs selectedKurikulumMahasiswa) {
		this.selectedKurikulumMahasiswa = selectedKurikulumMahasiswa;
	}

	public Mkurmhs getSelectedKurikulumMahasiswa() {
		return this.selectedKurikulumMahasiswa;
	}

	public void setKurikulumMahasiswas(BindingListModelList kurikulumMahasiswas) {
		this.kurikulumMahasiswas = kurikulumMahasiswas;
	}

	public BindingListModelList getKurikulumMahasiswas() {
		return this.kurikulumMahasiswas;
	}

	public void setKurikulumMahasiswaService(KurikulumMahasiswaService kurikulumMahasiswaService) {
		this.kurikulumMahasiswaService = kurikulumMahasiswaService;
	}

	public KurikulumMahasiswaService getKurikulumMahasiswaService() {
		return this.kurikulumMahasiswaService;
	}

	public void setKurikulumProdiListCtrl(KurikulumProdiListCtrl kurikulumProdiListCtrl) {
		this.kurikulumProdiListCtrl = kurikulumProdiListCtrl;
	}

	public KurikulumProdiListCtrl getKurikulumProdiListCtrl() {
		return this.kurikulumProdiListCtrl;
	}

	public void setKurikulumProdiDetailCtrl(KurikulumProdiDetailCtrl kurikulumProdiDetailCtrl) {
		this.kurikulumProdiDetailCtrl = kurikulumProdiDetailCtrl;
	}

	public KurikulumProdiDetailCtrl getKurikulumProdiDetailCtrl() {
		return this.kurikulumProdiDetailCtrl;
	}


	public void setProdiService(ProdiService prodiService) {
		this.prodiService = prodiService;
	}

	public ProdiService getProdiService() {
		return this.prodiService;
	}


    public Mmahasiswa getMahasiswa() {
        return mahasiswa;
    }

    public void setMahasiswa(Mmahasiswa mahasiswa) {
        this.mahasiswa = mahasiswa;
    }
}
