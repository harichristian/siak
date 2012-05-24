package id.ac.idu.webui.administrasi.pegawai;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.MpegawaiService;
import id.ac.idu.administrasi.service.MprovService;
import id.ac.idu.backend.model.Mpegawai;
import id.ac.idu.backend.model.Mprov;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.webui.administrasi.report.MpegawaiSimpleDJReport;
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
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * Main controller for the mpegawai main module.<br>
 * <br>
 * zul-file: /WEB-INF/pages/mpegawai/mpegawaiMain.zul.<br>
 * This class creates the Tabs + TabPanels. The components/data to all tabs are
 * created on demand on first time selecting the tab.<br>
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * <br>
 * 
 * @changes 07/04/2010: sge modified for zk5.x with complete Annotated
 *          Databinding.<br>
 * 
 *          Managed tabs:<br>
 *          - MpegawaiListCtrl = Mpegawai List / Filialen Liste<br>
 *          - MpegawaiDetailCtrl = Mpegawai Details / Filiale-Details<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class MpegawaiMainCtrl extends GFCBaseCtrl implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(MpegawaiMainCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window windowMpegawaiMain; // autowired

	// Tabs
	protected Tabbox tabbox_MpegawaiMain; // autowired
	protected Tab tabMpegawaiList; // autowired
	protected Tab tabMpegawaiDetail; // autowired
	protected Tabpanel tabPanelMpegawaiList; // autowired
	protected Tabpanel tabPanelMpegawaiDetail; // autowired

	// filter components
	protected Checkbox checkbox_MpegawaiList_ShowAll; // autowired
	protected Textbox txtb_Mpegawai_No; // aurowired
	protected Button button_MpegawaiList_SearchNo; // aurowired
	protected Textbox txtb_Mpegawai_Name; // aurowired
	protected Button button_MpegawaiList_SearchName; // aurowired
	protected Textbox txtb_Mpegawai_City; // aurowired
//	protected Button button_MpegawaiList_SearchCity; // aurowired

	// checkRights
	protected Button button_MpegawaiList_PrintList;

	// Button controller for the CRUD buttons
	private final String btnCtroller_ClassPrefix = "button_MpegawaiMain_";
	private ButtonStatusCtrl btnCtrlMpegawai;
	protected Button btnNew; // autowired
	protected Button btnEdit; // autowired
	protected Button btnDelete; // autowired
	protected Button btnSave; // autowired
	protected Button btnCancel; // autowired

	protected Button btnHelp;

	// Tab-Controllers for getting the binders
	private MpegawaiListCtrl mpegawaiListCtrl;
	private MpegawaiDetailCtrl mpegawaiDetailCtrl;

	// Databinding
	private Mpegawai selectedMpegawai;
	private BindingListModelList mpegawais;

	// ServiceDAOs / Domain Classes
	private MpegawaiService mpegawaiService;

	// always a copy from the bean before modifying. Used for reseting
	private Mpegawai originalMpegawai;

    public MprovService getMprovService() {
        return mprovService;
    }

    public void setMprovService(MprovService mprovService) {
        this.mprovService = mprovService;
    }

    private MprovService mprovService;

	/**
	 * default constructor.<br>
	 */
	public MpegawaiMainCtrl() {
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
	public void onCreate$windowMpegawaiMain(Event event) throws Exception {
		windowMpegawaiMain.setContentStyle("padding:0px;");

		// create the Button Controller. Disable not used buttons during working
		btnCtrlMpegawai = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

		//doCheckRights();

		/**
		 * Initiate the first loading by selecting the customerList tab and
		 * create the components from the zul-file.
		 */
		tabMpegawaiList.setSelected(true);

		if (tabPanelMpegawaiList != null) {
			ZksampleCommonUtils.createTabPanelContent(tabPanelMpegawaiList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/pegawai/mpegawaiList.zul");
		}

		// init the buttons for editMode
		btnCtrlMpegawai.setInitEdit();
	}

	/**
	 * When the tab 'tabMpegawaiList' is selected.<br>
	 * Loads the zul-file into the tab.
	 *
	 * @param event
	 * @throws java.io.IOException
	 */
	public void onSelect$tabMpegawaiList(Event event) throws IOException {
		logger.debug(event.toString());

		// Check if the tabpanel is already loaded
		if (tabPanelMpegawaiList.getFirstChild() != null) {
			tabMpegawaiList.setSelected(true);

			return;
		}

		if (tabPanelMpegawaiList != null) {
			ZksampleCommonUtils.createTabPanelContent(tabPanelMpegawaiList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/pegawai/mpegawaiList.zul");
		}

	}

	/**
	 * When the tab 'tabPanelMpegawaiDetail' is selected.<br>
	 * Loads the zul-file into the tab.
	 *
	 * @param event
	 * @throws java.io.IOException
	 */
	public void onSelect$tabMpegawaiDetail(Event event) throws IOException {
		// logger.debug(event.toString());

		// Check if the tabpanel is already loaded
		if (tabPanelMpegawaiDetail.getFirstChild() != null) {
			tabMpegawaiDetail.setSelected(true);

			// refresh the Binding mechanism
			getMpegawaiDetailCtrl().setMpegawai(getSelectedMpegawai());

			getMpegawaiDetailCtrl().getBinder().loadAll();
            getMpegawaiDetailCtrl().doResetCombo();
            if (getSelectedMpegawai()!=null)     {
                if(getSelectedMpegawai().getCkdpostg()!=null) {
                    getMpegawaiDetailCtrl().txtb_kdpostg.setValue(getSelectedMpegawai().getCkdpostg().toString());
                }
                if(getSelectedMpegawai().getCkdposrm()!=null) {
                    getMpegawaiDetailCtrl().txtb_kdpos.setValue(getSelectedMpegawai().getCkdposrm().toString());
                }
                if (getSelectedMpegawai().getCproprm()!=null) {
                    getMpegawaiDetailCtrl().txtb_proprm.setValue(((Mprov)mprovService.getMprovByID(Long.parseLong(getSelectedMpegawai().getCproprm()))).getCnamaProv());
                }

                if (getSelectedMpegawai().getCproprm()!=null) {
                    getMpegawaiDetailCtrl().txtb_proptg.setValue(((Mprov)mprovService.getMprovByID(Long.parseLong(getSelectedMpegawai().getCproptg()))).getCnamaProv());
                }
            }
			return;
		}

		if (tabPanelMpegawaiDetail != null) {
			ZksampleCommonUtils.createTabPanelContent(tabPanelMpegawaiDetail, this, "ModuleMainController", "/WEB-INF/pages/administrasi/pegawai/mpegawaiDetail.zul");
		}
	}

	/**
	 * when the "print mpegawais list" button is clicked.
	 *
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$button_MpegawaiList_PrintList(Event event) throws InterruptedException {
		final Window win = (Window) Path.getComponent("/outerIndexWindow");
		new MpegawaiSimpleDJReport(win);
	}

	/**
	 * when the checkBox 'Show All' for filtering is checked. <br>
	 *
	 * @param event
	 */
	public void onCheck$checkbox_MpegawaiList_ShowAll(Event event) {
		// logger.debug(event.toString());

		// empty the text search boxes
		txtb_Mpegawai_No.setValue(""); // clear
		txtb_Mpegawai_Name.setValue(""); // clear
//		txtb_Mpegawai_City.setValue(""); // clear

		// ++ create the searchObject and init sorting ++//
		HibernateSearchObject<Mpegawai> soMpegawai = new HibernateSearchObject<Mpegawai>(Mpegawai.class, getMpegawaiListCtrl().getCountRows());
		soMpegawai.addSort("cnama", false);

		// Change the BindingListModel.
		if (getMpegawaiListCtrl().getBinder() != null) {
			getMpegawaiListCtrl().getPagedBindingListWrapper().setSearchObject(soMpegawai);

			// get the current Tab for later checking if we must change it
			Tab currentTab = tabbox_MpegawaiMain.getSelectedTab();

			// check if the tab is one of the Detail tabs. If so do not
			// change the selection of it
			if (!currentTab.equals(tabMpegawaiList)) {
				tabMpegawaiList.setSelected(true);
			} else {
				currentTab.setSelected(true);
			}
		}

	}

	/**
	 * Filter the mpegawai list with 'like mpegawai number'. <br>
	 */
	public void onClick$button_MpegawaiList_SearchNo(Event event) throws Exception {
		// logger.debug(event.toString());

		// if not empty
		if (!txtb_Mpegawai_No.getValue().isEmpty()) {
			checkbox_MpegawaiList_ShowAll.setChecked(false); // unCheck
			txtb_Mpegawai_Name.setValue(""); // clear
//			txtb_Mpegawai_City.setValue(""); // clear

			// ++ create the searchObject and init sorting ++//
			HibernateSearchObject<Mpegawai> soMpegawai = new HibernateSearchObject<Mpegawai>(Mpegawai.class, getMpegawaiListCtrl().getCountRows());
			soMpegawai.addFilter(new Filter("cnip", "%" + txtb_Mpegawai_No.getValue() + "%", Filter.OP_ILIKE));
			soMpegawai.addSort("cnama", false);

			// Change the BindingListModel.
			if (getMpegawaiListCtrl().getBinder() != null) {
				getMpegawaiListCtrl().getPagedBindingListWrapper().setSearchObject(soMpegawai);

				// get the current Tab for later checking if we must change it
				Tab currentTab = tabbox_MpegawaiMain.getSelectedTab();

				// check if the tab is one of the Detail tabs. If so do not
				// change the selection of it
				if (!currentTab.equals(tabMpegawaiList)) {
					tabMpegawaiList.setSelected(true);
				} else {
					currentTab.setSelected(true);
				}
			}
		}
	}

	/**
	 * Filter the mpegawai list with 'like mpegawai name'. <br>
	 */
	public void onClick$button_MpegawaiList_SearchName(Event event) throws Exception {
		// logger.debug(event.toString());

		// if not empty
		if (!txtb_Mpegawai_Name.getValue().isEmpty()) {
			checkbox_MpegawaiList_ShowAll.setChecked(false); // unCheck
//			txtb_Mpegawai_City.setValue(""); // clear
			txtb_Mpegawai_No.setValue(""); // clear

			// ++ create the searchObject and init sorting ++//
			HibernateSearchObject<Mpegawai> soMpegawai = new HibernateSearchObject<Mpegawai>(Mpegawai.class, getMpegawaiListCtrl().getCountRows());
			soMpegawai.addFilter(new Filter("cnama", "%" + txtb_Mpegawai_Name.getValue() + "%", Filter.OP_ILIKE));
			soMpegawai.addSort("cnama", false);

			// Change the BindingListModel.
			if (getMpegawaiListCtrl().getBinder() != null) {
				getMpegawaiListCtrl().getPagedBindingListWrapper().setSearchObject(soMpegawai);

				// get the current Tab for later checking if we must change it
				Tab currentTab = tabbox_MpegawaiMain.getSelectedTab();

				// check if the tab is one of the Detail tabs. If so do not
				// change the selection of it
				if (!currentTab.equals(tabMpegawaiList)) {
					tabMpegawaiList.setSelected(true);
				} else {
					currentTab.setSelected(true);
				}
			}
		}
	}

	/**
	 * Filter the mpegawai list with 'like mpegawai city'. <br>
	 */
//	public void onClick$button_MpegawaiList_SearchCity(Event event) throws Exception {
//		// logger.debug(event.toString());
//
//		// if not empty
//		if (!txtb_Mpegawai_City.getValue().isEmpty()) {
//			checkbox_MpegawaiList_ShowAll.setChecked(false); // unCheck
//			txtb_Mpegawai_Name.setValue(""); // clear
//			txtb_Mpegawai_No.setValue(""); // clear
//
//			// ++ create the searchObject and init sorting ++//
//			HibernateSearchObject<Mpegawai> soMpegawai = new HibernateSearchObject<Mpegawai>(Mpegawai.class, getMpegawaiListCtrl().getCountRows());
//			soMpegawai.addFilter(new Filter("filOrt", "%" + txtb_Mpegawai_City.getValue() + "%", Filter.OP_ILIKE));
//			soMpegawai.addSort("filOrt", false);
//
//			// Change the BindingListModel.
//			if (getMpegawaiListCtrl().getBinder() != null) {
//				getMpegawaiListCtrl().getPagedBindingListWrapper().setSearchObject(soMpegawai);
//
//				// get the current Tab for later checking if we must change it
//				Tab currentTab = tabbox_MpegawaiMain.getSelectedTab();
//
//				// check if the tab is one of the Detail tabs. If so do not
//				// change the selection of it
//				if (!currentTab.equals(tabMpegawaiList)) {
//					tabMpegawaiList.setSelected(true);
//				} else {
//					currentTab.setSelected(true);
//				}
//			}
//
//		}
//	}

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
		if (getMpegawaiDetailCtrl().getBinder() != null) {

			// refresh all dataBinder related controllers/components
			getMpegawaiDetailCtrl().getBinder().loadAll();

            getMpegawaiDetailCtrl().doResetCombo();
            if (getSelectedMpegawai()!=null)     {
                if (getSelectedMpegawai().getCkdpostg() != null) {
                    getMpegawaiDetailCtrl().txtb_kdpostg.setValue(getSelectedMpegawai().getCkdpostg().toString());
                }
                if (getSelectedMpegawai().getCkdposrm() != null) {
                    getMpegawaiDetailCtrl().txtb_kdpos.setValue(getSelectedMpegawai().getCkdposrm().toString());
                }
                if (getSelectedMpegawai().getCproprm()!=null) {
                    getMpegawaiDetailCtrl().txtb_proprm.setValue(((Mprov)mprovService.getMprovByID(Long.parseLong(getSelectedMpegawai().getCproprm()))).getCnamaProv());
                }

                if (getSelectedMpegawai().getCproprm()!=null) {
                    getMpegawaiDetailCtrl().txtb_proptg.setValue(((Mprov)mprovService.getMprovByID(Long.parseLong(getSelectedMpegawai().getCproptg()))).getCnamaProv());
                }
            }

			// set editable Mode
			getMpegawaiDetailCtrl().doReadOnlyMode(true);

			btnCtrlMpegawai.setInitEdit();
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
		Tab currentTab = tabbox_MpegawaiMain.getSelectedTab();

		// check first, if the tabs are created, if not than create it
		if (getMpegawaiDetailCtrl() == null) {
			Events.sendEvent(new Event("onSelect", tabMpegawaiDetail, null));
			// if we work with spring beanCreation than we must check a little
			// bit deeper, because the Controller are preCreated ?
		} else if (getMpegawaiDetailCtrl().getBinder() == null) {
			Events.sendEvent(new Event("onSelect", tabMpegawaiDetail, null));
		}

		// check if the tab is one of the Detail tabs. If so do not change the
		// selection of it
		if (!currentTab.equals(tabMpegawaiDetail)) {
			tabMpegawaiDetail.setSelected(true);
		} else {
			currentTab.setSelected(true);
		}

		getMpegawaiDetailCtrl().getBinder().loadAll();

		// remember the old vars
		doStoreInitValues();

		btnCtrlMpegawai.setBtnStatus_Edit();

		getMpegawaiDetailCtrl().doReadOnlyMode(false);
		// set focus
		getMpegawaiDetailCtrl().txtb_nip.focus();
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
		if (getMpegawaiDetailCtrl().getBinder() == null) {
			Events.sendEvent(new Event("onSelect", tabMpegawaiDetail, null));
		}

		// check first, if the tabs are created
		if (getMpegawaiDetailCtrl().getBinder() == null) {
			return;
		}

		final Mpegawai anMpegawai = getSelectedMpegawai();
		if (anMpegawai != null) {

			// Show a confirm box
			final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anMpegawai.getCnama();
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

					/**
					 * Do not allow to modify the demo mpegawais
					 */
					if (getMpegawaiDetailCtrl().getMpegawai().getId() <= 2) {
						try {
							ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
							return;
						} catch (final InterruptedException e) {
							throw new RuntimeException(e);
						}

					} else {
						try {
							getMpegawaiService().delete(anMpegawai);
						} catch (DataAccessException e) {
							ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
						}
					}
				}

			}

			) == MultiLineMessageBox.YES) {
			}

		}

		btnCtrlMpegawai.setInitEdit();

		setSelectedMpegawai(null);
		// refresh the list
		getMpegawaiListCtrl().doFillListbox();

		// refresh all dataBinder related controllers
		getMpegawaiDetailCtrl().getBinder().loadAll();
	}

	/**
	 * Saves all involved Beans to the DB.
	 *
	 * @param event
	 * @throws InterruptedException
	 */
	private void doSave(Event event) throws InterruptedException {
		// logger.debug(event.toString());

		// save all components data in the several tabs to the bean
		getMpegawaiDetailCtrl().getBinder().saveAll();

		try {

			/**
			 * Do not allow to modify the demo mpegawais
			 */
//			if (getMpegawaiDetailCtrl().getMpegawai().getId() <= 2) {
//				ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
//
//				doResetToInitValues();
//				getMpegawaiDetailCtrl().getBinder().loadAll();
//				return;
//			}

			// save it to database
			getMpegawaiService().saveOrUpdate(getMpegawaiDetailCtrl().getMpegawai());
			// if saving is successfully than actualize the beans as
			// origins.
			doStoreInitValues();
			// refresh the list
			getMpegawaiListCtrl().doFillListbox();
			// later refresh StatusBar
			Events.postEvent("onSelect", getMpegawaiListCtrl().getListBoxMpegawai(), getSelectedMpegawai());

			// show the objects data in the statusBar
			String str = getSelectedMpegawai().getCnama();
			EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

		} catch (DataAccessException e) {
			ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

			// Reset to init values
			doResetToInitValues();

			return;

		} finally {
			btnCtrlMpegawai.setInitEdit();
			getMpegawaiDetailCtrl().doReadOnlyMode(true);
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
		if (getMpegawaiDetailCtrl() == null) {
			Events.sendEvent(new Event("onSelect", tabMpegawaiDetail, null));
			// if we work with spring beanCreation than we must check a little
			// bit deeper, because the Controller are preCreated ?
		} else if (getMpegawaiDetailCtrl().getBinder() == null) {
			Events.sendEvent(new Event("onSelect", tabMpegawaiDetail, null));
		}

		// remember the current object
		doStoreInitValues();

		/** !!! DO NOT BREAK THE TIERS !!! */
		// We don't create a new DomainObject() in the frontend.
		// We GET it from the backend.
		final Mpegawai anMpegawai = getMpegawaiService().getNewMpegawai();

		// set the beans in the related databinded controllers
		getMpegawaiDetailCtrl().setMpegawai(anMpegawai);
		getMpegawaiDetailCtrl().setSelectedMpegawai(anMpegawai);

		// Refresh the binding mechanism
		getMpegawaiDetailCtrl().setSelectedMpegawai(getSelectedMpegawai());
        if  ( getMpegawaiDetailCtrl().getBinder() !=null)  {
		        getMpegawaiDetailCtrl().getBinder().loadAll();
        }

         getMpegawaiDetailCtrl().doResetCombo();
         getMpegawaiDetailCtrl().txtb_kdpostg.setValue("");
         getMpegawaiDetailCtrl().txtb_kdpos.setValue("");
         getMpegawaiDetailCtrl().txtb_proprm.setValue("");
         getMpegawaiDetailCtrl().txtb_proptg.setValue("");

		// set editable Mode
		getMpegawaiDetailCtrl().doReadOnlyMode(false);

		// set the ButtonStatus to New-Mode
		btnCtrlMpegawai.setInitNew();

		tabMpegawaiDetail.setSelected(true);
		// set focus
		getMpegawaiDetailCtrl().txtb_nip.focus();

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

		if (tabbox_MpegawaiMain.getSelectedTab() == tabMpegawaiDetail) {
			getMpegawaiDetailCtrl().doFitSize(event);
		} else if (tabbox_MpegawaiMain.getSelectedTab() == tabMpegawaiList) {
			// resize and fill Listbox new
			getMpegawaiListCtrl().doFillListbox();
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
	 * @see
	 */
	public void doStoreInitValues() {

		if (getSelectedMpegawai() != null) {

			try {
				setOriginalMpegawai((Mpegawai) ZksampleBeanUtils.cloneBean(getSelectedMpegawai()));
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
	 * @see
	 *
	 */
	public void doResetToInitValues() {

		if (getOriginalMpegawai() != null) {

			try {
				setSelectedMpegawai((Mpegawai) ZksampleBeanUtils.cloneBean(getOriginalMpegawai()));
				// TODO Bug in DataBinder??
				windowMpegawaiMain.invalidate();

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

		// window_MpegawaiList.setVisible(workspace.isAllowed("window_MpegawaiList"));
		button_MpegawaiList_PrintList.setVisible(workspace.isAllowed("button_MpegawaiList_PrintList"));
		button_MpegawaiList_SearchNo.setVisible(workspace.isAllowed("button_MpegawaiList_SearchNo"));
		button_MpegawaiList_SearchName.setVisible(workspace.isAllowed("button_MpegawaiList_SearchName"));
//		button_MpegawaiList_SearchCity.setVisible(workspace.isAllowed("button_MpegawaiList_SearchCity"));

		btnHelp.setVisible(workspace.isAllowed("button_MpegawaiMain_btnHelp"));
		btnNew.setVisible(workspace.isAllowed("button_MpegawaiMain_btnNew"));
		btnEdit.setVisible(workspace.isAllowed("button_MpegawaiMain_btnEdit"));
		btnDelete.setVisible(workspace.isAllowed("button_MpegawaiMain_btnDelete"));
		btnSave.setVisible(workspace.isAllowed("button_MpegawaiMain_btnSave"));

	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

	public void setOriginalMpegawai(Mpegawai originalMpegawai) {
		this.originalMpegawai = originalMpegawai;
	}

	public Mpegawai getOriginalMpegawai() {
		return this.originalMpegawai;
	}

	public void setSelectedMpegawai(Mpegawai selectedMpegawai) {
		this.selectedMpegawai = selectedMpegawai;
	}

	public Mpegawai getSelectedMpegawai() {
		return this.selectedMpegawai;
	}

	public void setMpegawais(BindingListModelList mpegawais) {
		this.mpegawais = mpegawais;
	}

	public BindingListModelList getMpegawais() {
		return this.mpegawais;
	}

	public void setMpegawaiService(MpegawaiService mpegawaiService) {
		this.mpegawaiService = mpegawaiService;
	}

	public MpegawaiService getMpegawaiService() {
		return this.mpegawaiService;
	}

	public void setMpegawaiListCtrl(MpegawaiListCtrl mpegawaiListCtrl) {
		this.mpegawaiListCtrl = mpegawaiListCtrl;
	}

	public MpegawaiListCtrl getMpegawaiListCtrl() {
		return this.mpegawaiListCtrl;
	}

	public void setMpegawaiDetailCtrl(MpegawaiDetailCtrl mpegawaiDetailCtrl) {
		this.mpegawaiDetailCtrl = mpegawaiDetailCtrl;
	}

	public MpegawaiDetailCtrl getMpegawaiDetailCtrl() {
		return this.mpegawaiDetailCtrl;
	}

}
