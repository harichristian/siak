package id.ac.idu.webui.irs.paket;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.ProdiService;
import id.ac.idu.backend.model.Tpaketkuliah;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.irs.service.PaketService;
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
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/13/12
 * Time: 10:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class PaketMainCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(PaketMainCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window windowPaketMain; // autowired

	// Tabs
	protected Tabbox tabbox_PaketMain; // autowired
	protected Tab tabPaketList; // autowired
	protected Tab tabPaketDetail; // autowired
	protected Tabpanel tabPanelPaketList; // autowired
	protected Tabpanel tabPanelPaketDetail; // autowired

	// filter components
	protected Checkbox checkbox_PaketList_ShowAll; // autowired
	protected Textbox txtb_Paket_Term; // aurowired
	protected Button button_PaketList_SearchTerm; // aurowired
	protected Textbox txtb_Paket_Thajar; // aurowired
	protected Button button_PaketList_SearchThajar; // aurowired
	protected Textbox txtb_Paket_Prodi; // aurowired
	protected Button button_PaketList_SearchProdi; // aurowired

	// checkRights
	protected Button button_PaketList_PrintList;

	// Button controller for the CRUD buttons
	private final String btnCtroller_ClassPrefix = "button_PaketMain_";
	private ButtonStatusCtrl btnCtrlPaket;
	protected Button btnNew; // autowired
	protected Button btnEdit; // autowired
	protected Button btnDelete; // autowired
	protected Button btnSave; // autowired
	protected Button btnCancel; // autowired

	protected Button btnHelp;

	// Tab-Controllers for getting the binders
	private PaketListCtrl paketListCtrl;
	private PaketDetailCtrl paketDetailCtrl;

	// Databinding
	private Tpaketkuliah selectedPaket;
	private BindingListModelList pakets;

	// ServiceDAOs / Domain Classes
	private PaketService paketService;
	private ProdiService prodiService;

	// always a copy from the bean before modifying. Used for reseting
	private Tpaketkuliah originalPaket;

	/**
	 * default constructor.<br>
	 */
	public PaketMainCtrl() {
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
	public void onCreate$windowPaketMain(Event event) throws Exception {
		windowPaketMain.setContentStyle("padding:0px;");

		// create the Button Controller. Disable not used buttons during working
		btnCtrlPaket = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

		//doCheckRights();

		/**
		 * Initiate the fpakett loading by selecting the customerList tab and
		 * create the components from the zul-file.
		 */
		tabPaketList.setSelected(true);

		if (tabPanelPaketList != null) {
			ZksampleCommonUtils.createTabPanelContent(tabPanelPaketList, this, "ModuleMainController", "/WEB-INF/pages/irs/paket/paketList.zul");
		}

		// init the buttons for editMode
		btnCtrlPaket.setInitEdit();
	}

	/**
	 * When the tab 'tabPaketList' is selected.<br>
	 * Loads the zul-file into the tab.
	 *
	 * @param event
	 * @throws java.io.IOException
	 */
	public void onSelect$tabPaketList(Event event) throws IOException {
		logger.debug(event.toString());

		// Check if the tabpanel is already loaded
		if (tabPanelPaketList.getFirstChild() != null) {
			tabPaketList.setSelected(true);

			return;
		}

		if (tabPanelPaketList != null) {
			ZksampleCommonUtils.createTabPanelContent(tabPanelPaketList, this, "ModuleMainController", "/WEB-INF/pages/irs/paket/paketList.zul");
		}

	}

	/**
	 * When the tab 'tabPanelPaketDetail' is selected.<br>
	 * Loads the zul-file into the tab.
	 *
	 * @param event
	 * @throws IOException
	 */
	public void onSelect$tabPaketDetail(Event event) throws IOException {
		// logger.debug(event.toString());

		// Check if the tabpanel is already loaded
		if (tabPanelPaketDetail.getFirstChild() != null) {
			tabPaketDetail.setSelected(true);

			// refresh the Binding mechanism
			getPaketDetailCtrl().setPaket(getSelectedPaket());
			getPaketDetailCtrl().getBinder().loadAll();
			return;
		}

		if (tabPanelPaketDetail != null) {
			ZksampleCommonUtils.createTabPanelContent(tabPanelPaketDetail, this, "ModuleMainController", "/WEB-INF/pages/irs/paket/paketDetail.zul");
		}
	}

	/**
	 * when the "print pakets list" button is clicked.
	 *
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$button_PaketList_PrintList(Event event) throws InterruptedException {
		final Window win = (Window) Path.getComponent("/outerIndexWindow");
		//new PaketSimpleDJReport(win);
	}

	/**
	 * when the checkBox 'Show All' for filtering is checked. <br>
	 *
	 * @param event
	 */
	public void onCheck$checkbox_PaketList_ShowAll(Event event) {
		// logger.debug(event.toString());

		// empty the text search boxes
		txtb_Paket_Term.setValue(""); // clear
		txtb_Paket_Thajar.setValue(""); // clear
		txtb_Paket_Prodi.setValue(""); // clear

		// ++ create the searchObject and init sorting ++//
		HibernateSearchObject<Tpaketkuliah> soPaket = new HibernateSearchObject<Tpaketkuliah>(Tpaketkuliah.class, getPaketListCtrl().getCountRows());
		soPaket.addSort(ConstantUtil.ID, false);

		// Change the BindingListModel.
		if (getPaketListCtrl().getBinder() != null) {
			getPaketListCtrl().getPagedBindingListWrapper().setSearchObject(soPaket);

			// get the current Tab for later checking if we must change it
			Tab currentTab = tabbox_PaketMain.getSelectedTab();

			// check if the tab is one of the Detail tabs. If so do not
			// change the selection of it
			if (!currentTab.equals(tabPaketList)) {
				tabPaketList.setSelected(true);
			} else {
				currentTab.setSelected(true);
			}
		}

	}

	/**
	 * Filter the paket list with 'like paket number'. <br>
	 */
	public void onClick$button_PaketList_SearchTerm(Event event) throws Exception {
		// logger.debug(event.toString());

		// if not empty
		if (!txtb_Paket_Term.getValue().isEmpty()) {
			checkbox_PaketList_ShowAll.setChecked(false); // unCheck
			txtb_Paket_Thajar.setValue(""); // clear
			txtb_Paket_Prodi.setValue(""); // clear

			// ++ create the searchObject and init sorting ++//
			HibernateSearchObject<Tpaketkuliah> soPaket = new HibernateSearchObject<Tpaketkuliah>(Tpaketkuliah.class, getPaketListCtrl().getCountRows());
			soPaket.addFilter(new Filter(ConstantUtil.TERM, "%" + txtb_Paket_Term.getValue() + "%", Filter.OP_ILIKE));
			soPaket.addSort(ConstantUtil.TERM, false);

			// Change the BindingListModel.
			if (getPaketListCtrl().getBinder() != null) {
				getPaketListCtrl().getPagedBindingListWrapper().setSearchObject(soPaket);

				// get the current Tab for later checking if we must change it
				Tab currentTab = tabbox_PaketMain.getSelectedTab();

				// check if the tab is one of the Detail tabs. If so do not
				// change the selection of it
				if (!currentTab.equals(tabPaketList)) {
					tabPaketList.setSelected(true);
				} else {
					currentTab.setSelected(true);
				}
			}
		}
	}

	/**
	 * Filter the paket list with 'like paket name'. <br>
	 */
	public void onClick$button_PaketList_SearchThajar(Event event) throws Exception {
		// logger.debug(event.toString());

		// if not empty
		if (!txtb_Paket_Thajar.getValue().isEmpty()) {
			checkbox_PaketList_ShowAll.setChecked(false); // unCheck
			txtb_Paket_Prodi.setValue(""); // clear
			txtb_Paket_Term.setValue(""); // clear

			// ++ create the searchObject and init sorting ++//
			HibernateSearchObject<Tpaketkuliah> soPaket = new HibernateSearchObject<Tpaketkuliah>(Tpaketkuliah.class, getPaketListCtrl().getCountRows());
			soPaket.addFilter(new Filter(ConstantUtil.THAJAR, "%" + txtb_Paket_Thajar.getValue() + "%", Filter.OP_ILIKE));
			soPaket.addSort(ConstantUtil.THAJAR, false);

			// Change the BindingListModel.
			if (getPaketListCtrl().getBinder() != null) {
				getPaketListCtrl().getPagedBindingListWrapper().setSearchObject(soPaket);

				// get the current Tab for later checking if we must change it
				Tab currentTab = tabbox_PaketMain.getSelectedTab();

				// check if the tab is one of the Detail tabs. If so do not
				// change the selection of it
				if (!currentTab.equals(tabPaketList)) {
					tabPaketList.setSelected(true);
				} else {
					currentTab.setSelected(true);
				}
			}
		}
	}

	/**
	 * Filter the paket list with 'like paket'. <br>
	 */
	public void onClick$button_PaketList_SearchProdi(Event event) throws Exception {
		// logger.debug(event.toString());
        // if not empty
		if (!txtb_Paket_Prodi.getValue().isEmpty()) {
			checkbox_PaketList_ShowAll.setChecked(false); // unCheck
			txtb_Paket_Term.setValue(""); // clear
			txtb_Paket_Thajar.setValue(""); // clear

			// ++ create the searchObject and init sorting ++//
			HibernateSearchObject<Tpaketkuliah> soPaket = new HibernateSearchObject<Tpaketkuliah>(Tpaketkuliah.class, getPaketListCtrl().getCountRows());
			soPaket.addFilter(new Filter(ConstantUtil.PRODI_DOT_NAMA, "%" + txtb_Paket_Prodi.getValue() + "%", Filter.OP_ILIKE));
			soPaket.addSort(ConstantUtil.PRODI_DOT_NAMA, false);

			// Change the BindingListModel.
			if (getPaketListCtrl().getBinder() != null) {
				getPaketListCtrl().getPagedBindingListWrapper().setSearchObject(soPaket);

				// get the current Tab for later checking if we must change it
				Tab currentTab = tabbox_PaketMain.getSelectedTab();

				// check if the tab is one of the Detail tabs. If so do not
				// change the selection of it
				if (!currentTab.equals(tabPaketList)) {
					tabPaketList.setSelected(true);
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

		// check fpakett, if the tabs are created
		if (getPaketDetailCtrl().getBinder() != null) {

			// refresh all dataBinder related controllers/components
			getPaketDetailCtrl().getBinder().loadAll();

			// set editable Mode
			getPaketDetailCtrl().doReadOnlyMode(true);

			btnCtrlPaket.setInitEdit();
		}
	}

	/**
	 * Sets all UI-components to writable-mode. Sets the buttons to edit-Mode.
	 * Checks fpakett, if the NEEDED TABS with its contents are created. If not,
	 * than create it by simulate an onSelect() with calling Events.sendEvent()
	 *
	 * @param event
	 * @throws InterruptedException
	 */
	private void doEdit(Event event) {
		// logger.debug(event.toString());

		// get the current Tab for later checking if we must change it
		Tab currentTab = tabbox_PaketMain.getSelectedTab();

		// check fpakett, if the tabs are created, if not than create it
		if (getPaketDetailCtrl() == null) {
			Events.sendEvent(new Event("onSelect", tabPaketDetail, null));
			// if we work with spring beanCreation than we must check a little
			// bit deeper, because the Controller are preCreated ?
		} else if (getPaketDetailCtrl().getBinder() == null) {
			Events.sendEvent(new Event("onSelect", tabPaketDetail, null));
		}

		// check if the tab is one of the Detail tabs. If so do not change the
		// selection of it
		if (!currentTab.equals(tabPaketDetail)) {
			tabPaketDetail.setSelected(true);
		} else {
			currentTab.setSelected(true);
		}

		getPaketDetailCtrl().getBinder().loadAll();

		// remember the old vars
		doStoreInitValues();

		btnCtrlPaket.setBtnStatus_Edit();

		getPaketDetailCtrl().doReadOnlyMode(false);
		// set focus
		getPaketDetailCtrl().txtb_term.focus();
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

		// check fpakett, if the tabs are created, if not than create them
		if (getPaketDetailCtrl().getBinder() == null) {
			Events.sendEvent(new Event("onSelect", tabPaketDetail, null));
		}

		// check fpakett, if the tabs are created
		if (getPaketDetailCtrl().getBinder() == null) {
			return;
		}

		final Tpaketkuliah anPaket = getSelectedPaket();
		if (anPaket != null) {

			// Show a confirm box
			final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anPaket.getCterm();
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
                        getPaketService().delete(anPaket);
                    } catch (DataAccessException e) {
                        ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                    }
				}

			}

			) == MultiLineMessageBox.YES) {
			}

		}

		btnCtrlPaket.setInitEdit();

		setSelectedPaket(null);
		// refresh the list
		getPaketListCtrl().doFillListbox();

		// refresh all dataBinder related controllers
		getPaketDetailCtrl().getBinder().loadAll();
	}

	/**
	 * Saves all involved Beans to the DB.
	 *
	 * @param event
	 * @throws InterruptedException
	 */
	private void doSave(Event event) throws InterruptedException {
		// logger.debug(event.toString());
        /*if(getPaketDetailCtrl().getPaket().getMprodi() == null) {
            final Mprodi anProdi = getProdiService().getNewProdi();
            getPaketDetailCtrl().getPaket().setMprodi(anProdi);
        }*/
		// save all components data in the several tabs to the bean
		getPaketDetailCtrl().getBinder().saveAll();

		try {
            // save it to database
			getPaketService().saveOrUpdate(getPaketDetailCtrl().getPaket());
			// if saving is successfully than actualize the beans as
			// origins.
			doStoreInitValues();
			// refresh the list
			getPaketListCtrl().doFillListbox();
			// later refresh StatusBar
			Events.postEvent("onSelect", getPaketListCtrl().getListBoxPaket(), getSelectedPaket());

			// show the objects data in the statusBar
			String str = getSelectedPaket().getCterm();
			EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

		} catch (DataAccessException e) {
			ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

			// Reset to init values
			doResetToInitValues();

			return;

		} finally {
			btnCtrlPaket.setInitEdit();
			getPaketDetailCtrl().doReadOnlyMode(true);
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

		// check fpakett, if the tabs are created
		if (getPaketDetailCtrl() == null) {
			Events.sendEvent(new Event("onSelect", tabPaketDetail, null));
			// if we work with spring beanCreation than we must check a little
			// bit deeper, because the Controller are preCreated ?
		} else if (getPaketDetailCtrl().getBinder() == null) {
			Events.sendEvent(new Event("onSelect", tabPaketDetail, null));
		}

		// remember the current object
		doStoreInitValues();

		/** !!! DO NOT BREAK THE TIERS !!! */
		// We don't create a new DomainObject() in the frontend.
		// We GET it from the backend.
		final Tpaketkuliah anPaket = getPaketService().getNewPaket();
        //final Mprodi anProdi = getProdiService().getAllProdis().get(0);
        //anPaket.setMprodi(anProdi);
		// set the beans in the related databinded controllers
		getPaketDetailCtrl().setPaket(anPaket);
		getPaketDetailCtrl().setSelectedPaket(anPaket);

		// Refresh the binding mechanism
		getPaketDetailCtrl().setSelectedPaket(getSelectedPaket());
		if (getPaketDetailCtrl().getBinder()!=null) {
            getPaketDetailCtrl().getBinder().loadAll();
        }

		// set editable Mode
		getPaketDetailCtrl().doReadOnlyMode(false);

		// set the ButtonStatus to New-Mode
		btnCtrlPaket.setInitNew();

		tabPaketDetail.setSelected(true);
		// set focus
		getPaketDetailCtrl().txtb_term.focus();

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

		if (tabbox_PaketMain.getSelectedTab() == tabPaketDetail) {
			getPaketDetailCtrl().doFitSize(event);
		} else if (tabbox_PaketMain.getSelectedTab() == tabPaketList) {
			// resize and fill Listbox new
			getPaketListCtrl().doFillListbox();
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

		if (getSelectedPaket() != null) {

			try {
				setOriginalPaket((Tpaketkuliah) ZksampleBeanUtils.cloneBean(getSelectedPaket()));
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

		if (getOriginalPaket() != null) {

			try {
				setSelectedPaket((Tpaketkuliah) ZksampleBeanUtils.cloneBean(getOriginalPaket()));
				// TODO Bug in DataBinder??
				windowPaketMain.invalidate();

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

		// window_PaketList.setVisible(workspace.isAllowed("window_PaketList"));
		button_PaketList_PrintList.setVisible(workspace.isAllowed("button_PaketList_PrintList"));
		button_PaketList_SearchTerm.setVisible(workspace.isAllowed("button_PaketList_SearchTerm"));
		button_PaketList_SearchThajar.setVisible(workspace.isAllowed("button_PaketList_SearchThajar"));
		//button_PaketList_SearchProdi.setVisible(workspace.isAllowed("button_PaketList_SearchProdi"));

		btnHelp.setVisible(workspace.isAllowed("button_PaketMain_btnHelp"));
		btnNew.setVisible(workspace.isAllowed("button_PaketMain_btnNew"));
		btnEdit.setVisible(workspace.isAllowed("button_PaketMain_btnEdit"));
		btnDelete.setVisible(workspace.isAllowed("button_PaketMain_btnDelete"));
		btnSave.setVisible(workspace.isAllowed("button_PaketMain_btnSave"));

	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	public void setOriginalPaket(Tpaketkuliah originalPaket) {
		this.originalPaket = originalPaket;
	}

	public Tpaketkuliah getOriginalPaket() {
		return this.originalPaket;
	}

	public void setSelectedPaket(Tpaketkuliah selectedPaket) {
		this.selectedPaket = selectedPaket;
	}

	public Tpaketkuliah getSelectedPaket() {
		return this.selectedPaket;
	}

	public void setPakets(BindingListModelList pakets) {
		this.pakets = pakets;
	}

	public BindingListModelList getPakets() {
		return this.pakets;
	}

	public void setPaketService(PaketService paketService) {
		this.paketService = paketService;
	}

	public PaketService getPaketService() {
		return this.paketService;
	}

	public void setPaketListCtrl(PaketListCtrl paketListCtrl) {
		this.paketListCtrl = paketListCtrl;
	}

	public PaketListCtrl getPaketListCtrl() {
		return this.paketListCtrl;
	}

	public void setPaketDetailCtrl(PaketDetailCtrl paketDetailCtrl) {
		this.paketDetailCtrl = paketDetailCtrl;
	}

	public PaketDetailCtrl getPaketDetailCtrl() {
		return this.paketDetailCtrl;
	}


	public void setProdiService(ProdiService prodiService) {
		this.prodiService = prodiService;
	}

	public ProdiService getProdiService() {
		return this.prodiService;
	}
}
