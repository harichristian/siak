package id.ac.idu.webui.kurikulum.kurikulumMahasiswa;

import id.ac.idu.administrasi.service.MahasiswaService;
import id.ac.idu.backend.model.*;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.kurikulum.service.KurikulumMahasiswaService;
import id.ac.idu.webui.irs.cutimhs.model.OrderSearchMahasiswaList;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.pagging.PagedListWrapper;
import id.ac.idu.webui.util.searchdialogs.KurikulumExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.ProdiExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.TermExtendedSearchListBox;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.*;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/9/12
 * Time: 4:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class KurikulumMahasiswaDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -1L;
	private static final Logger logger = Logger.getLogger(KurikulumMahasiswaDetailCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window windowKurikulumMahasiswaDetail; // autowired

	protected Borderlayout borderlayout_KurikulumMahasiswaDetail; // autowired

	protected Textbox txtb_mahasiswa;
    protected Textbox txtb_code; // autowired
    protected Button btnSearchCodeExtended;
	protected Textbox txtb_cohort; // autowired
    protected Textbox txtb_prodi;
    protected Button btnSearchProdiExtended;
    protected Textbox txtb_thajar;
    protected Textbox txtb_term;
    protected Button btnSearchTermExtended;
//    protected Textbox txtb_semester;
	protected Button button_KurikulumMahasiswaDialog_PrintKurikulumMahasiswa; // autowired

    // POPUP variables
    protected Textbox txtb_nim;
    protected Textbox txtb_nama;
    protected Intbox txtb_mhsid;
    protected Bandbox bandbox_Dialog_MahasiswaSearch;
    protected Paging paging_MahasiswaSearchList;
    private transient PagedListWrapper<Mmahasiswa> plwMahasiswa;
    protected Listbox listMahasiswaSearch;
    private int pageSize;

	// Databinding
	protected transient AnnotateDataBinder binder;
	private KurikulumMahasiswaMainCtrl kurikulumMahasiswaMainCtrl;

	// ServiceDAOs / Domain Classes
	private transient KurikulumMahasiswaService kurikulumMahasiswaService;
    private transient MahasiswaService popupMhsService;

	/**
	 * default constructor.<br>
	 */
	public KurikulumMahasiswaDetailCtrl() {
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
			getKurikulumMahasiswaMainCtrl().setKurikulumMahasiswaDetailCtrl(this);

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
	public void onCreate$windowKurikulumMahasiswaDetail(Event event) throws Exception {
        setPageSize(20);
		binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

		binder.loadAll();

		doFitSize(event);
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
	public void doFitSize(Event event) {
		final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
		final int maxListBoxHeight = height - 148;
		borderlayout_KurikulumMahasiswaDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

		windowKurikulumMahasiswaDetail.invalidate();
	}

	/**
	 * Set all components in readOnly/disabled modus.
	 *
	 * true = all components are readOnly or disabled.<br>
	 * false = all components are accessable.<br>
	 *
	 * @param b
	 */
	public void doReadOnlyMode(boolean b) {
//		txtb_semester.setReadonly(b);
		//txtb_cohort.setReadonly(b);
		//txtb_prodi.setReadonly(b);
		//txtb_mahasiswa.setReadonly(b);
		txtb_thajar.setReadonly(b);
		//txtb_term.setReadonly(b);
        bandbox_Dialog_MahasiswaSearch.setDisabled(b);
        btnSearchCodeExtended.setDisabled(b);
        btnSearchProdiExtended.setDisabled(b);
        btnSearchTermExtended.setDisabled(b);
	}

    public void onClick$btnSearchProdiExtended(Event event) {
        doSearchProdiExtended(event);
    }

    private void doSearchProdiExtended(Event event) {
        Mprodi prodi = ProdiExtendedSearchListBox.show(windowKurikulumMahasiswaDetail);

        if (prodi != null) {
            txtb_prodi.setValue(prodi.getCnmprogst());
            Mkurmhs obj = getKurikulumMahasiswa();
            obj.setMprodi(prodi);
            setKurikulumMahasiswa(obj);
        }
    }

    public void onClick$btnSearchCodeExtended(Event event) {
        doSearchCodeExtended(event);
    }

    private void doSearchCodeExtended(Event event) {
        Mkurikulum kurikulum = KurikulumExtendedSearchListBox.show(windowKurikulumMahasiswaDetail);

        if (kurikulum != null) {
            txtb_code.setValue(kurikulum.getCkodekur());
            txtb_cohort.setValue(kurikulum.getCcohort());
            Mkurmhs obj = getKurikulumMahasiswa();
            obj.setMkurikulum(kurikulum);
            obj.setCcohort(kurikulum.getCcohort());
            setKurikulumMahasiswa(obj);
        }
    }

    public void onClick$btnSearchTermExtended(Event event) {
        doSearchTermExtended(event);
    }

    private void doSearchTermExtended(Event event) {
        Mterm term = TermExtendedSearchListBox.show(windowKurikulumMahasiswaDetail);

        if (term != null) {
            txtb_term.setValue(term.getKdTerm());
            Mkurmhs obj = getKurikulumMahasiswa();
            obj.setCterm(term.getKdTerm());
            setKurikulumMahasiswa(obj);
        }
    }
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //
	// ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
	// +++++++++++++++++++++++++++++++++++++++++++++++++ //

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

	public Mkurmhs getSelectedKurikulumMahasiswa() {
		// STORED IN THE module's MainController
		return getKurikulumMahasiswaMainCtrl().getSelectedKurikulumMahasiswa();
	}

	public void setSelectedKurikulumMahasiswa(Mkurmhs selectedKurikulumMahasiswa) {
		// STORED IN THE module's MainController
		getKurikulumMahasiswaMainCtrl().setSelectedKurikulumMahasiswa(selectedKurikulumMahasiswa);
	}

	public BindingListModelList getKurikulumMahasiswas() {
		// STORED IN THE module's MainController
		return getKurikulumMahasiswaMainCtrl().getKurikulumMahasiswas();
	}

	public void setKurikulumMahasiswas(BindingListModelList kurikulumMahasiswas) {
		// STORED IN THE module's MainController
		getKurikulumMahasiswaMainCtrl().setKurikulumMahasiswas(kurikulumMahasiswas);
	}

	public AnnotateDataBinder getBinder() {
		return this.binder;
	}

	public void setBinder(AnnotateDataBinder binder) {
		this.binder = binder;
	}

	public void setKurikulumMahasiswaService(KurikulumMahasiswaService kurikulumMahasiswaService) {
		this.kurikulumMahasiswaService = kurikulumMahasiswaService;
	}

	public KurikulumMahasiswaService getKurikulumMahasiswaService() {
		return this.kurikulumMahasiswaService;
	}

	public void setKurikulumMahasiswaMainCtrl(KurikulumMahasiswaMainCtrl kurikulumMahasiswaMainCtrl) {
		this.kurikulumMahasiswaMainCtrl = kurikulumMahasiswaMainCtrl;
	}

	public KurikulumMahasiswaMainCtrl getKurikulumMahasiswaMainCtrl() {
		return this.kurikulumMahasiswaMainCtrl;
	}

    //POPUP SECTION
    public PagedListWrapper<Mmahasiswa> getPlwMahasiswa() {
        return plwMahasiswa;
    }

    public MahasiswaService getPopupMhsService() {
        return popupMhsService;
    }

    public void setPopupMhsService(MahasiswaService popupMhsService) {
        this.popupMhsService = popupMhsService;
    }

    public void setPlwMahasiswa(PagedListWrapper<Mmahasiswa> plwMahasiswa) {
        this.plwMahasiswa = plwMahasiswa;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


    public void onOpen$bandbox_Dialog_MahasiswaSearch(Event event) {
        HibernateSearchObject<Mmahasiswa> soCustomer = new HibernateSearchObject<Mmahasiswa>(Mmahasiswa.class);
		soCustomer.addSort("cnim", false);

		paging_MahasiswaSearchList.setPageSize(pageSize);
		paging_MahasiswaSearchList.setDetailed(true);
		getPlwMahasiswa().init(soCustomer, listMahasiswaSearch, paging_MahasiswaSearchList);
		listMahasiswaSearch.setItemRenderer(new OrderSearchMahasiswaList());
    }

    public void onClick$button_bbox_Close(Event event) {
        bandbox_Dialog_MahasiswaSearch.close();
	}

    public void onMahasiswaItem(Event event) {
        Listitem item = listMahasiswaSearch.getSelectedItem();
        if (item != null) {

            Mmahasiswa aMahasiswa = (Mmahasiswa) item.getAttribute("data");
            getKurikulumMahasiswaMainCtrl().setMahasiswa(aMahasiswa);
            //txtb_nim.setValue(getKurikulumMahasiswaMainCtrl().getMahasiswa().getCnim());

            txtb_nim.setValue(aMahasiswa.getCnim());
            Mkurmhs obj = getKurikulumMahasiswa();
            obj.setMmahasiswa(aMahasiswa);
            setKurikulumMahasiswa(obj);

            bandbox_Dialog_MahasiswaSearch.setValue(getKurikulumMahasiswaMainCtrl().getMahasiswa().getCnim());
        }
        bandbox_Dialog_MahasiswaSearch.close();
    }
}
