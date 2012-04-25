package id.ac.idu.webui.irs.irs;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.ProdiService;
import id.ac.idu.backend.model.Mmahasiswa;
import id.ac.idu.backend.model.Tirspasca;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.irs.service.IrsService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/12/12
 * Time: 6:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class IrsMainCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(IrsMainCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window windowIrsMain; // autowired

	// Tabs
	protected Tabbox tabbox_IrsMain; // autowired
	protected Tab tabIrsList; // autowired
	protected Tab tabIrsDetail; // autowired
	protected Tabpanel tabPanelIrsList; // autowired
	protected Tabpanel tabPanelIrsDetail; // autowired

	// filter components
	protected Checkbox checkbox_IrsList_ShowAll; // autowired
	protected Textbox txtb_Irs_Mahasiswa; // aurowired
	protected Button button_IrsList_SearchMahasiswa; // aurowired
	protected Textbox txtb_Irs_Sekolah; // aurowired
	protected Button button_IrsList_SearchSekolah; // aurowired
	protected Textbox txtb_Irs_Prodi; // aurowired
	protected Button button_IrsList_SearchProdi; // aurowired

	// checkRights
	protected Button button_IrsList_PrintList;

	// Button controller for the CRUD buttons
	private final String btnCtroller_ClassPrefix = "button_IrsMain_";
	private ButtonStatusCtrl btnCtrlIrs;
	protected Button btnNew; // autowired
	protected Button btnEdit; // autowired
	protected Button btnDelete; // autowired
	protected Button btnSave; // autowired
	protected Button btnCancel; // autowired

	protected Button btnHelp;

	// Tab-Controllers for getting the binders
	private IrsListCtrl irsListCtrl;
	private IrsDetailCtrl irsDetailCtrl;

	// Databinding
	private Tirspasca selectedIrs;
	private BindingListModelList irss;
	private String totalSks;

	// ServiceDAOs / Domain Classes
	private IrsService irsService;
	private ProdiService prodiService;

	// always a copy from the bean before modifying. Used for reseting
	private Tirspasca originalIrs;
    private Mmahasiswa mahasiswa;

	/**
	 * default constructor.<br>
	 */
	public IrsMainCtrl() {
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
	public void onCreate$windowIrsMain(Event event) throws Exception {
		windowIrsMain.setContentStyle("padding:0px;");

		// create the Button Controller. Disable not used buttons during working
		btnCtrlIrs = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

		//doCheckRights();

		/**
		 * Initiate the first loading by selecting the customerList tab and
		 * create the components from the zul-file.
		 */
		tabIrsList.setSelected(true);

		if (tabPanelIrsList != null) {
			ZksampleCommonUtils.createTabPanelContent(tabPanelIrsList, this, "ModuleMainController", "/WEB-INF/pages/irs/irs/irsList.zul");
		}

		// init the buttons for editMode
		btnCtrlIrs.setInitEdit();
	}

	/**
	 * When the tab 'tabIrsList' is selected.<br>
	 * Loads the zul-file into the tab.
	 *
	 * @param event
	 * @throws java.io.IOException
	 */
	public void onSelect$tabIrsList(Event event) throws IOException {
		logger.debug(event.toString());

		// Check if the tabpanel is already loaded
		if (tabPanelIrsList.getFirstChild() != null) {
			tabIrsList.setSelected(true);

			return;
		}

		if (tabPanelIrsList != null) {
			ZksampleCommonUtils.createTabPanelContent(tabPanelIrsList, this, "ModuleMainController", "/WEB-INF/pages/irs/irs/irsList.zul");
		}

	}

	/**
	 * When the tab 'tabPanelIrsDetail' is selected.<br>
	 * Loads the zul-file into the tab.
	 *
	 * @param event
	 * @throws IOException
	 */
	public void onSelect$tabIrsDetail(Event event) throws IOException {
		// logger.debug(event.toString());
        Listitem item;
        Tirspasca obj;
        int total = 0;
		// Check if the tabpanel is already loaded
		if (tabPanelIrsDetail.getFirstChild() != null) {
			tabIrsDetail.setSelected(true);

			// refresh the Binding mechanism
			getIrsDetailCtrl().setIrs(getSelectedIrs());
            if(getSelectedIrs() != null) {
                getIrsDetailCtrl().loadDetilMatakuliah();
            } else {
                getIrsDetailCtrl().getPlwDetilMatakuliah().clear();
            }
			getIrsDetailCtrl().getBinder().loadAll();
			return;
		}

		if (tabPanelIrsDetail != null) {
			ZksampleCommonUtils.createTabPanelContent(tabPanelIrsDetail, this, "ModuleMainController", "/WEB-INF/pages/irs/irs/irsDetail.zul");
		}
	}

	/**
	 * when the "print irss list" button is clicked.
	 *
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$button_IrsList_PrintList(Event event) throws InterruptedException {
		final Window win = (Window) Path.getComponent("/outerIndexWindow");
		//new IrsSimpleDJReport(win);
	}

	/**
	 * when the checkBox 'Show All' for filtering is checked. <br>
	 *
	 * @param event
	 */
	public void onCheck$checkbox_IrsList_ShowAll(Event event) {
		// logger.debug(event.toString());

		// empty the text search boxes
		txtb_Irs_Mahasiswa.setValue(""); // clear
		txtb_Irs_Sekolah.setValue(""); // clear
		//txtb_Irs_Prodi.setValue(""); // clear

		// ++ create the searchObject and init sorting ++//
		HibernateSearchObject<Tirspasca> soIrs = new HibernateSearchObject<Tirspasca>(Tirspasca.class, getIrsListCtrl().getCountRows());
		soIrs.addSort(ConstantUtil.ID, false);

		// Change the BindingListModel.
		if (getIrsListCtrl().getBinder() != null) {
			getIrsListCtrl().getPagedBindingListWrapper().setSearchObject(soIrs);

			// get the current Tab for later checking if we must change it
			Tab currentTab = tabbox_IrsMain.getSelectedTab();

			// check if the tab is one of the Detail tabs. If so do not
			// change the selection of it
			if (!currentTab.equals(tabIrsList)) {
				tabIrsList.setSelected(true);
			} else {
				currentTab.setSelected(true);
			}
		}

	}

	/**
	 * Filter the irs list with 'like irs number'. <br>
	 */
	public void onClick$button_IrsList_SearchMahasiswa(Event event) throws Exception {
		// logger.debug(event.toString());

		// if not empty
		if (!txtb_Irs_Mahasiswa.getValue().isEmpty()) {
			checkbox_IrsList_ShowAll.setChecked(false); // unCheck
			txtb_Irs_Sekolah.setValue(""); // clear
			txtb_Irs_Prodi.setValue(""); // clear

			// ++ create the searchObject and init sorting ++//
			HibernateSearchObject<Tirspasca> soIrs = new HibernateSearchObject<Tirspasca>(Tirspasca.class, getIrsListCtrl().getCountRows());
			soIrs.addFilter(new Filter(ConstantUtil.MAHASISWA_DOT_NAMA, "%" + txtb_Irs_Mahasiswa.getValue() + "%", Filter.OP_ILIKE));
			soIrs.addSort(ConstantUtil.MAHASISWA_DOT_NAMA, false);

			// Change the BindingListModel.
			if (getIrsListCtrl().getBinder() != null) {
				getIrsListCtrl().getPagedBindingListWrapper().setSearchObject(soIrs);

				// get the current Tab for later checking if we must change it
				Tab currentTab = tabbox_IrsMain.getSelectedTab();

				// check if the tab is one of the Detail tabs. If so do not
				// change the selection of it
				if (!currentTab.equals(tabIrsList)) {
					tabIrsList.setSelected(true);
				} else {
					currentTab.setSelected(true);
				}
			}
		}
	}

	/**
	 * Filter the irs list with 'like irs name'. <br>
	 */
	public void onClick$button_IrsList_SearchSekolah(Event event) throws Exception {
		// logger.debug(event.toString());

		// if not empty
		if (!txtb_Irs_Sekolah.getValue().isEmpty()) {
			checkbox_IrsList_ShowAll.setChecked(false); // unCheck
			txtb_Irs_Prodi.setValue(""); // clear
			txtb_Irs_Mahasiswa.setValue(""); // clear

			// ++ create the searchObject and init sorting ++//
			HibernateSearchObject<Tirspasca> soIrs = new HibernateSearchObject<Tirspasca>(Tirspasca.class, getIrsListCtrl().getCountRows());
			soIrs.addFilter(new Filter(ConstantUtil.SEKOLAH_DOT_NAME, "%" + txtb_Irs_Sekolah.getValue() + "%", Filter.OP_ILIKE));
			soIrs.addSort(ConstantUtil.SEKOLAH_DOT_NAME, false);

			// Change the BindingListModel.
			if (getIrsListCtrl().getBinder() != null) {
				getIrsListCtrl().getPagedBindingListWrapper().setSearchObject(soIrs);

				// get the current Tab for later checking if we must change it
				Tab currentTab = tabbox_IrsMain.getSelectedTab();

				// check if the tab is one of the Detail tabs. If so do not
				// change the selection of it
				if (!currentTab.equals(tabIrsList)) {
					tabIrsList.setSelected(true);
				} else {
					currentTab.setSelected(true);
				}
			}
		}
	}

	/**
	 * Filter the irs list with 'like irs'. <br>
	 */
	public void onClick$button_IrsList_SearchProdi(Event event) throws Exception {
		// logger.debug(event.toString());
        // if not empty
		if (!txtb_Irs_Prodi.getValue().isEmpty()) {
			checkbox_IrsList_ShowAll.setChecked(false); // unCheck
			txtb_Irs_Mahasiswa.setValue(""); // clear
			txtb_Irs_Sekolah.setValue(""); // clear

			// ++ create the searchObject and init sorting ++//
			HibernateSearchObject<Tirspasca> soIrs = new HibernateSearchObject<Tirspasca>(Tirspasca.class, getIrsListCtrl().getCountRows());
			soIrs.addFilter(new Filter(ConstantUtil.PRODI_DOT_NAMA, "%" + txtb_Irs_Prodi.getValue() + "%", Filter.OP_ILIKE));
			soIrs.addSort(ConstantUtil.PRODI_DOT_NAMA, false);

			// Change the BindingListModel.
			if (getIrsListCtrl().getBinder() != null) {
				getIrsListCtrl().getPagedBindingListWrapper().setSearchObject(soIrs);

				// get the current Tab for later checking if we must change it
				Tab currentTab = tabbox_IrsMain.getSelectedTab();

				// check if the tab is one of the Detail tabs. If so do not
				// change the selection of it
				if (!currentTab.equals(tabIrsList)) {
					tabIrsList.setSelected(true);
				} else {
					currentTab.setSelected(true);
				}
			}
		}
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
		if (getIrsDetailCtrl().getBinder() != null) {

			// refresh all dataBinder related controllers/components
			getIrsDetailCtrl().getBinder().loadAll();

			// set editable Mode
			getIrsDetailCtrl().doReadOnlyMode(true);

			btnCtrlIrs.setInitEdit();
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
		Tab currentTab = tabbox_IrsMain.getSelectedTab();

		// check first, if the tabs are created, if not than create it
		if (getIrsDetailCtrl() == null) {
			Events.sendEvent(new Event("onSelect", tabIrsDetail, null));
			// if we work with spring beanCreation than we must check a little
			// bit deeper, because the Controller are preCreated ?
		} else if (getIrsDetailCtrl().getBinder() == null) {
			Events.sendEvent(new Event("onSelect", tabIrsDetail, null));
		}

		// check if the tab is one of the Detail tabs. If so do not change the
		// selection of it
		if (!currentTab.equals(tabIrsDetail)) {
			tabIrsDetail.setSelected(true);
		} else {
			currentTab.setSelected(true);
		}

		getIrsDetailCtrl().getBinder().loadAll();

		// remember the old vars
		doStoreInitValues();

		btnCtrlIrs.setBtnStatus_Edit();

		getIrsDetailCtrl().doReadOnlyMode(false);
		// set focus
		//getIrsDetailCtrl().txtb_term.focus();
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
		if (getIrsDetailCtrl().getBinder() == null) {
			Events.sendEvent(new Event("onSelect", tabIrsDetail, null));
		}

		// check first, if the tabs are created
		if (getIrsDetailCtrl().getBinder() == null) {
			return;
		}

		final Tirspasca anIrs = getSelectedIrs();
		if (anIrs != null) {

			// Show a confirm box
			final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anIrs.getMmahasiswa().getCnama();
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
                        getIrsService().delete(anIrs);
                    } catch (DataAccessException e) {
                        ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                    }
				}

			}

			) == MultiLineMessageBox.YES) {
			}

		}

		btnCtrlIrs.setInitEdit();

		setSelectedIrs(null);
		// refresh the list
		getIrsListCtrl().doFillListbox();

		// refresh all dataBinder related controllers
		getIrsDetailCtrl().getBinder().loadAll();
	}

	/**
	 * Saves all involved Beans to the DB.
	 *
	 * @param event
	 * @throws InterruptedException
	 */
	private void doSave(Event event) throws InterruptedException {
        if(getMahasiswa() != null){
            getIrsDetailCtrl().getIrs().setMmahasiswa(getMahasiswa());
        }
        // save all components data in the several tabs to the bean
		getIrsDetailCtrl().getBinder().saveAll();
        Listitem item;
        /* List */
        Tirspasca obj;
        Set<Tirspasca> sets = new HashSet<Tirspasca>();

        List listObj = getIrsDetailCtrl().getListDetilMatakuliah().getItems();
        for(Object dtl : listObj) {
            item = (Listitem) dtl;
            obj = (Tirspasca) item.getAttribute(IrsDetailCtrl.DATA);
            if(getSelectedIrs().getId() > 0) {
                obj.setMmahasiswa(getSelectedIrs().getMmahasiswa());
                obj.setMsekolah(getSelectedIrs().getMsekolah());
                obj.setMprodi(getSelectedIrs().getMprodi());
                obj.setCterm(getSelectedIrs().getCterm());
                obj.setCthajar(getSelectedIrs().getCthajar());
                obj.setCsmt(getSelectedIrs().getCsmt());
            }
            sets.add(obj);
        }
		try {
            // save it to database
			getIrsService().deleteList(getIrsDetailCtrl().getDelDetilMatakuliah());
            getIrsService().saveOrUpdateList(sets);
			// if saving is successfully than actualize the beans as
			// origins.
			doStoreInitValues();
			// refresh the list
			getIrsListCtrl().doFillListbox();
			// later refresh StatusBar
			Events.postEvent("onSelect", getIrsListCtrl().getListBoxIrs(), getSelectedIrs());

			// show the objects data in the statusBar
			String str = getSelectedIrs().getMmahasiswa().getCnama();
			EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

		} catch (DataAccessException e) {
			ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

			// Reset to init values
			doResetToInitValues();

			return;

		} finally {
			btnCtrlIrs.setInitEdit();
			getIrsDetailCtrl().doReadOnlyMode(true);
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
		if (getIrsDetailCtrl() == null) {
			Events.sendEvent(new Event("onSelect", tabIrsDetail, null));
			// if we work with spring beanCreation than we must check a little
			// bit deeper, because the Controller are preCreated ?
		} else if (getIrsDetailCtrl().getBinder() == null) {
			Events.sendEvent(new Event("onSelect", tabIrsDetail, null));
		}

		// remember the current object
		doStoreInitValues();

		/** !!! DO NOT BREAK THE TIERS !!! */
		// We don't create a new DomainObject() in the frontend.
		// We GET it from the backend.
		final Tirspasca anIrs = getIrsService().getNewIrs();
        //final Mprodi anProdi = getProdiService().getAllProdis().get(0);
        //anIrs.setMprodi(anProdi);
		// set the beans in the related databinded controllers
		getIrsDetailCtrl().setIrs(anIrs);
		getIrsDetailCtrl().setSelectedIrs(anIrs);
        getIrsDetailCtrl().getPlwDetilMatakuliah().clear();

		// Refresh the binding mechanism
		getIrsDetailCtrl().setSelectedIrs(getSelectedIrs());
		if (getIrsDetailCtrl().getBinder()!=null) {
            getIrsDetailCtrl().getBinder().loadAll();
        }

		// set editable Mode
		getIrsDetailCtrl().doReadOnlyMode(false);

		// set the ButtonStatus to New-Mode
		btnCtrlIrs.setInitNew();

		tabIrsDetail.setSelected(true);
		// set focus
		//getIrsDetailCtrl().txtb_term.focus();

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

		if (tabbox_IrsMain.getSelectedTab() == tabIrsDetail) {
			getIrsDetailCtrl().doFitSize(event);
		} else if (tabbox_IrsMain.getSelectedTab() == tabIrsList) {
			// resize and fill Listbox new
			getIrsListCtrl().doFillListbox();
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

		if (getSelectedIrs() != null) {

			try {
				setOriginalIrs((Tirspasca) ZksampleBeanUtils.cloneBean(getSelectedIrs()));
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

		if (getOriginalIrs() != null) {

			try {
				setSelectedIrs((Tirspasca) ZksampleBeanUtils.cloneBean(getOriginalIrs()));
				// TODO Bug in DataBinder??
				windowIrsMain.invalidate();

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

		// window_IrsList.setVisible(workspace.isAllowed("window_IrsList"));
		button_IrsList_PrintList.setVisible(workspace.isAllowed("button_IrsList_PrintList"));
		button_IrsList_SearchMahasiswa.setVisible(workspace.isAllowed("button_IrsList_SearchMahasiswa"));
		button_IrsList_SearchSekolah.setVisible(workspace.isAllowed("button_IrsList_SearchSekolah"));
		//button_IrsList_SearchProdi.setVisible(workspace.isAllowed("button_IrsList_SearchProdi"));

		btnHelp.setVisible(workspace.isAllowed("button_IrsMain_btnHelp"));
		btnNew.setVisible(workspace.isAllowed("button_IrsMain_btnNew"));
		btnEdit.setVisible(workspace.isAllowed("button_IrsMain_btnEdit"));
		btnDelete.setVisible(workspace.isAllowed("button_IrsMain_btnDelete"));
		btnSave.setVisible(workspace.isAllowed("button_IrsMain_btnSave"));

	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	public void setOriginalIrs(Tirspasca originalIrs) {
		this.originalIrs = originalIrs;
	}

	public Tirspasca getOriginalIrs() {
		return this.originalIrs;
	}

	public void setSelectedIrs(Tirspasca selectedIrs) {
		this.selectedIrs = selectedIrs;
	}

	public Tirspasca getSelectedIrs() {
		return this.selectedIrs;
	}

	public void setIrss(BindingListModelList irss) {
		this.irss = irss;
	}

	public BindingListModelList getIrss() {
		return this.irss;
	}

	public void setIrsService(IrsService irsService) {
		this.irsService = irsService;
	}

	public IrsService getIrsService() {
		return this.irsService;
	}

	public void setIrsListCtrl(IrsListCtrl irsListCtrl) {
		this.irsListCtrl = irsListCtrl;
	}

	public IrsListCtrl getIrsListCtrl() {
		return this.irsListCtrl;
	}

	public void setIrsDetailCtrl(IrsDetailCtrl irsDetailCtrl) {
		this.irsDetailCtrl = irsDetailCtrl;
	}

	public IrsDetailCtrl getIrsDetailCtrl() {
		return this.irsDetailCtrl;
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

    public void setTotalSks(String totalSks) {
		this.totalSks = totalSks;
	}

	public String getTotalSks() {
		return this.totalSks;
	}
}
