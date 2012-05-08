package id.ac.idu.webui.irs.irs;

import com.trg.search.Filter;
import id.ac.idu.administrasi.service.MahasiswaService;
import id.ac.idu.backend.model.*;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.irs.service.IrsService;
import id.ac.idu.util.Codec;
import id.ac.idu.util.ConstantUtil;
import id.ac.idu.webui.irs.cutimhs.model.OrderSearchMahasiswaList;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.ZksampleMessageUtils;
import id.ac.idu.webui.util.pagging.PagedListWrapper;
import id.ac.idu.webui.util.searchdialogs.MatakuliahExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.ProdiExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.SekolahExtendedSearchListBox;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.*;

import java.io.Serializable;
import java.util.HashMap;
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
public class IrsDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -1L;
	private static final Logger logger = Logger.getLogger(IrsDetailCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window windowIrsDetail; // autowired

	protected Borderlayout borderlayout_IrsDetail; // autowired

	//protected Textbox txtb_nim;         //lookup
    //protected Textbox txtb_nama;        //lookup
    protected Textbox txtb_sekolah;     //lookup
    protected Button btnSearchSekolahExtended;
	protected Textbox txtb_prodi;
    protected Button btnSearchProdiExtended;       //lookup
	protected Textbox txtb_jenjang;     //lookup

    protected Textbox txtb_term;        //header
    protected Textbox txtb_thajar;      //header
	protected Textbox txtb_semester;    //header

    protected Textbox txtb_matakuliah;  //detail
    protected Button btnSearchMatakuliahExtended;
    protected Textbox txtb_kelompok;    //detail
	protected Button button_IrsDialog_PrintIrs; // autowired

    // POPUP variables
    protected Textbox txtb_nim;
    protected Textbox txtb_nama;
    protected Intbox txtb_mhsid;
    protected Textbox txtb_totalsks;
    protected Bandbox bandbox_Dialog_MahasiswaSearch;
    protected Paging paging_MahasiswaSearchList;
    private transient PagedListWrapper<Mmahasiswa> plwMahasiswa;
    protected Listbox listMahasiswaSearch;
    private int pageSize;

    private int pageSizeMatakuliah;
    private int mahasiswaId;

    protected Textbox tb_Nim;
    protected Textbox tb_Nama;
    protected Textbox tb_NoKtp;

    public static final String DATA = "data";
    public static final String LIST = "LIST";
    public static final String CONTROL = "CONTROL";
    public static final String ISNEW = "ISNEW";

    protected Button btnNewDetilMatakuliah;
    protected Button btnDeleteDetilMatakuliah;

    protected Set<Tirspasca> delDetilMatakuliah = new HashSet<Tirspasca>();
    protected Set<Tirspasca> listIrs = new HashSet<Tirspasca>();
    private transient PagedListWrapper<Tirspasca> plwDetilMatakuliah;
    protected Listbox listDetilMatakuliah;
    protected Paging pagingDetilMatakuliah;

	// Databinding
	protected transient AnnotateDataBinder binder;
	private IrsMainCtrl irsMainCtrl;
    protected DetilMatakuliahCtrl detilMatakuliahCtrl;
    private String totalSks;

	// ServiceDAOs / Domain Classes
	private transient IrsService irsService;
    private transient MahasiswaService popupMhsService;

	/**
	 * default constructor.<br>
	 */
	public IrsDetailCtrl() {
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
			getIrsMainCtrl().setIrsDetailCtrl(this);

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
	public void onCreate$windowIrsDetail(Event event) throws Exception {
        setPageSize(20);
		binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);
        if(getIrs() != null) {
            loadDetilMatakuliah();
        }
		binder.loadAll();
		doFitSize(event);
	}
    public void loadDetilMatakuliah() {
        if(getIrs().getMmahasiswa() != null) {
            this.mahasiswaId = getIrs().getMmahasiswa().getId();
        }
        HibernateSearchObject<Tirspasca> so = new HibernateSearchObject<Tirspasca>(Tirspasca.class);
        if(getIrs() != null) {
            //so.addFilter(new Filter(ConstantUtil.KURIKULUM_ID, this.kurikulumId, Filter.OP_EQUAL));
            //mahasiswa
            so.addFilter(new Filter(ConstantUtil.MAHASISWA_DOT_ID, this.mahasiswaId, Filter.OP_EQUAL));
            //sekolah
            //prodi
            //term
            //thajar
            //semester
        }
        so.addSort(ConstantUtil.MAHASISWA_DOT_ID, false);

        pagingDetilMatakuliah.setPageSize(pageSize);
		pagingDetilMatakuliah.setDetailed(true);
		getPlwDetilMatakuliah().init(so, listDetilMatakuliah, pagingDetilMatakuliah);
		listDetilMatakuliah.setItemRenderer(new DetilMatakuliahSearchList());
        calcTotalSks();
    }

    public void calcTotalSks() {
        int total = 0;
        List listObj = getPlwDetilMatakuliah().getInnerList();
        //List listObj = listDetilMatakuliah.getItems();
        for(Object dtl : listObj) {
            //Listitem item = (Listitem) dtl;
            //Tirspasca irs = (Tirspasca) item.getAttribute(IrsDetailCtrl.DATA);
            total += (((Tirspasca)dtl).getNsks() != null)?((Tirspasca)dtl).getNsks():0;
            //total += (irs.getNsks() != null)?irs.getNsks():0;
        }
        setTotalsks(String.valueOf(total));
        txtb_totalsks.setValue(String.valueOf(total));
    }

    public void onClick$btnNewDetilMatakuliah(Event event) throws Exception {
        final Tirspasca dk = getIrsMainCtrl().getIrsService().getNewIrs();
        this.showDetail(dk, true);
    }

    public void onClick$btnDeleteDetilMatakuliah(Event event) throws Exception {
        Listitem item = listDetilMatakuliah.getSelectedItem();
        getDelDetilMatakuliah().add((Tirspasca) item.getAttribute(IrsDetailCtrl.DATA));
        Tirspasca irs = (Tirspasca) item.getAttribute(IrsDetailCtrl.DATA);
        int temp = Integer.valueOf(getTotalsks());
        int rowSks = (irs.getNsks()!=null)?irs.getNsks():0;
        setTotalsks(String.valueOf(temp - rowSks));
        listDetilMatakuliah.removeItemAt(listDetilMatakuliah.getSelectedIndex());
        //calcTotalSks();
		this.txtb_totalsks.setValue(String.valueOf(temp - rowSks));
        getBinder().loadAll();
    }

    public void onDetilMatakuliahItem(Event event) throws Exception {
        Listitem item = listDetilMatakuliah.getSelectedItem();
        if(item == null) return;

        final Tirspasca obj = (Tirspasca) item.getAttribute(IrsDetailCtrl.DATA);
        this.showDetail(obj, false);
    }

    public void onCalc(Event event) throws Exception {
        calcTotalSks();
    }

    public void showDetail(Tirspasca obj, boolean isnew) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(IrsDetailCtrl.DATA, obj);
        map.put(IrsDetailCtrl.LIST, listDetilMatakuliah);
        map.put(IrsDetailCtrl.CONTROL, this);
        map.put(IrsDetailCtrl.ISNEW, isnew);

        try {
            Executions.createComponents("/WEB-INF/pages/irs/irs/detilMatakuliah.zul", null, map);
        } catch (final Exception e) {
            logger.error("onOpenWindow:: error opening window / " + e.getMessage());
            ZksampleMessageUtils.showErrorMessage(e.toString());
        }
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
		borderlayout_IrsDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

		windowIrsDetail.invalidate();
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
		txtb_thajar.setReadonly(b);
		txtb_term.setReadonly(b);
		txtb_semester.setReadonly(b);
        bandbox_Dialog_MahasiswaSearch.setDisabled(b);
        btnSearchSekolahExtended.setDisabled(b);
        btnSearchProdiExtended.setDisabled(b);
	}

    public void onClick$btnSearchProdiExtended(Event event) {
        doSearchProdiExtended(event);
    }

    private void doSearchProdiExtended(Event event) {
        Mprodi prodi = ProdiExtendedSearchListBox.show(windowIrsDetail);

        if (prodi != null) {
            txtb_prodi.setValue(prodi.getCnmprogst());
            txtb_jenjang.setValue(prodi.getMjenjang().getCnmjen());
            Tirspasca obj = getIrs();
            obj.setMprodi(prodi);
            setIrs(obj);
        }
    }

    public void onClick$btnSearchSekolahExtended(Event event) {
        doSearchSekolahExtended(event);
    }

    private void doSearchSekolahExtended(Event event) {
        Msekolah sekolah = SekolahExtendedSearchListBox.show(windowIrsDetail);

        if (sekolah != null) {
            txtb_sekolah.setValue(sekolah.getCnamaSekolah());
            Tirspasca obj = getIrs();
            obj.setMsekolah(sekolah);
            setIrs(obj);
        }
    }

    public void onClick$btnSearchMatakuliahExtended(Event event) {
        doSearchMatakuliahExtended(event);
    }

    private void doSearchMatakuliahExtended(Event event) {
        Mtbmtkl matkul = MatakuliahExtendedSearchListBox.show(windowIrsDetail);

        if (matkul != null) {
            txtb_matakuliah.setValue(matkul.getCnamamk());
            Tirspasca obj = getIrs();
            obj.setMtbmtkl(matkul);
            setIrs(obj);
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
	public Tirspasca getIrs() {
		// STORED IN THE module's MainController
		return getIrsMainCtrl().getSelectedIrs();
	}

	public void setIrs(Tirspasca anIrs) {
		// STORED IN THE module's MainController
		getIrsMainCtrl().setSelectedIrs(anIrs);
	}

	public Tirspasca getSelectedIrs() {
		// STORED IN THE module's MainController
		return getIrsMainCtrl().getSelectedIrs();
	}

	public void setSelectedIrs(Tirspasca selectedIrs) {
		// STORED IN THE module's MainController
		getIrsMainCtrl().setSelectedIrs(selectedIrs);
	}

	public BindingListModelList getIrss() {
		// STORED IN THE module's MainController
		return getIrsMainCtrl().getIrss();
	}

	public void setIrss(BindingListModelList irss) {
		// STORED IN THE module's MainController
		getIrsMainCtrl().setIrss(irss);
	}

	public AnnotateDataBinder getBinder() {
		return this.binder;
	}

	public void setBinder(AnnotateDataBinder binder) {
		this.binder = binder;
	}

	public void setIrsService(IrsService irsService) {
		this.irsService = irsService;
	}

	public IrsService getIrsService() {
		return this.irsService;
	}

	public void setIrsMainCtrl(IrsMainCtrl irsMainCtrl) {
		this.irsMainCtrl = irsMainCtrl;
	}

	public IrsMainCtrl getIrsMainCtrl() {
		return this.irsMainCtrl;
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

    public PagedListWrapper<Tirspasca> getPlwDetilMatakuliah() {
        return plwDetilMatakuliah;
    }

    public void setPlwDetilMatakuliah(PagedListWrapper<Tirspasca> plwDetilMatakuliah) {
        this.plwDetilMatakuliah = plwDetilMatakuliah;
    }

    public Set<Tirspasca> getDelDetilMatakuliah() {
        return delDetilMatakuliah;
    }

    public void setDelDetilMatakuliah(Set<Tirspasca> delDetilMatakuliah) {
        this.delDetilMatakuliah = delDetilMatakuliah;
    }

    public Set<Tirspasca> getListIrs() {
        return listIrs;
    }

    public void setListIrs(Set<Tirspasca> listIrs) {
        this.listIrs = listIrs;
    }

    public boolean isModeEdit() {
        return (getIrsMainCtrl().btnSave.isVisible());
    }

    public void onOpen$bandbox_Dialog_MahasiswaSearch(Event event) {
        HibernateSearchObject<Mmahasiswa> so = new HibernateSearchObject<Mmahasiswa>(Mmahasiswa.class);
        so.addFilter(new Filter(ConstantUtil.STATUS_MAHASISWA_DOT_CODE, Codec.StatusMahasiswa.Status1.getValue(), Filter.OP_EQUAL));
        so.addSort("cnim", false);

		paging_MahasiswaSearchList.setPageSize(pageSize);
		paging_MahasiswaSearchList.setDetailed(true);
		getPlwMahasiswa().init(so, listMahasiswaSearch, paging_MahasiswaSearchList);
		listMahasiswaSearch.setItemRenderer(new OrderSearchMahasiswaList());
    }
    public void onClick$button_bbox_Search(Event event) {
        Filter filter1 = null;
        Filter filter2 = null;
        Filter filter3 = null;

        if (StringUtils.isNotEmpty(tb_Nim.getValue()))
            filter1 = new Filter("cnim", "%" + tb_Nim.getValue() + "%", Filter.OP_ILIKE);

        if (StringUtils.isNotEmpty(tb_Nama.getValue()))
            filter2 = new Filter("cnama", "%" + tb_Nama.getValue() + "%", Filter.OP_ILIKE);

        if (StringUtils.isNotEmpty(tb_NoKtp.getValue()))
            filter3 = new Filter("noktp", "%" + tb_NoKtp.getValue() + "%", Filter.OP_ILIKE);

        this.searchMahasiswa(filter1, filter2, filter3);
    }

    public void searchMahasiswa(Filter... filters) {

        HibernateSearchObject<Mmahasiswa> so = new HibernateSearchObject<Mmahasiswa>(Mmahasiswa.class);
        so.addFilter(new Filter("mstatusmhs.ckdstatmhs", Codec.StatusMahasiswa.Status1.getValue(), com.trg.search.Filter.OP_EQUAL));
        if(filters != null) {
            for(Filter anFilter : filters) {
                if(anFilter != null) so.addFilter(anFilter);
            }
        }

        so.addSort("cnim", false);

        paging_MahasiswaSearchList.setPageSize(pageSize);
        paging_MahasiswaSearchList.setDetailed(true);
        getPlwMahasiswa().init(so, listMahasiswaSearch, paging_MahasiswaSearchList);
        listMahasiswaSearch.setItemRenderer(new OrderSearchMahasiswaList());
    }

    public void onClick$button_bbox_Close(Event event) {
        bandbox_Dialog_MahasiswaSearch.close();
	}

    public void onMahasiswaItem(Event event) {
        Listitem item = listMahasiswaSearch.getSelectedItem();
        if (item != null) {
            Mmahasiswa aMahasiswa = (Mmahasiswa) item.getAttribute("data");
            getIrsMainCtrl().setMahasiswa(aMahasiswa);
            if (aMahasiswa != null) {
            txtb_nim.setValue(aMahasiswa.getCnim());
            txtb_nama.setValue(aMahasiswa.getCnama());
            Tirspasca obj = getIrs();
            obj.setMmahasiswa(aMahasiswa);
            setIrs(obj);
            bandbox_Dialog_MahasiswaSearch.setValue(getIrsMainCtrl().getMahasiswa().getCnim());
            }
        }
        bandbox_Dialog_MahasiswaSearch.close();
    }

    public DetilMatakuliahCtrl getDetilMatakuliahCtrl() {
        return detilMatakuliahCtrl;
    }

    public void setDetilMatakuliahCtrl(DetilMatakuliahCtrl detilMatakuliahCtrl) {
        this.detilMatakuliahCtrl = detilMatakuliahCtrl;
    }

    public Listbox getListDetilMatakuliah() {
        return listDetilMatakuliah;
    }

    public void setListDetilMatakuliah(Listbox listDetilMatakuliah) {
        this.listDetilMatakuliah = listDetilMatakuliah;
    }

    public String getTotalsks() {
		// STORED IN THE module's MainController
		return this.totalSks;
	}

	public void setTotalsks(String totalSks) {
		// STORED IN THE module's MainController
		this.totalSks = totalSks;
	}
}
