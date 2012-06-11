package id.ac.idu.webui.irs.paket;

import com.trg.search.Filter;
import id.ac.idu.backend.model.Mprodi;
import id.ac.idu.backend.model.Msekolah;
import id.ac.idu.backend.model.Mtbmtkl;
import id.ac.idu.backend.model.Tpaketkuliah;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.irs.service.PaketService;
import id.ac.idu.util.ConstantUtil;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.ListBoxUtil;
import id.ac.idu.webui.util.ZksampleMessageUtils;
import id.ac.idu.webui.util.pagging.PagedBindingListWrapper;
import id.ac.idu.webui.util.pagging.PagedListWrapper;
import id.ac.idu.webui.util.searchdialogs.MatakuliahExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.ProdiExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.SekolahExtendedSearchListBox;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.*;
import org.zkoss.zul.api.Listheader;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/13/12
 * Time: 10:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class PaketDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -1L;
	private static final Logger logger = Logger.getLogger(PaketDetailCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window windowPaketDetail; // autowired

	protected Borderlayout borderlayout_PaketDetail; // autowired

	protected Textbox txtb_nim;         //lookup
    protected Textbox txtb_nama;        //lookup
    protected Textbox txtb_sekolah;     //lookup
    protected Button btnSearchSekolahExtended;
    protected Textbox txtb_kodeprodi;
	protected Textbox txtb_prodi;       //lookup
    protected Button btnSearchProdiExtended;
	protected Textbox txtb_jenjang;     //lookup

    protected Textbox txtb_term;        //header
    protected Textbox txtb_thajar;      //header
	protected Textbox txtb_semester;    //header

    protected Textbox txtb_matakuliah;  //detail
    protected Button btnSearchMatakuliahExtended;
    protected Textbox txtb_kelompok;    //detail
	protected Button button_PaketDialog_PrintPaket; // autowired
    protected Paging pagingDetilMatkul;
    protected Listbox listboxDetilMatkul;
    protected Listheader listHeaderKode;
    protected Listheader listHeaderNama;
    protected Button btnNewMatkul;
    protected Button btnDeleteMatkul;

    // row count for listbox
    private int countRows;
    private int pageSize;

    private HibernateSearchObject<Tpaketkuliah> searchObj;
    //private HibernateSearchObject<Mtbmtkl> so;
    protected PagedBindingListWrapper<Tpaketkuliah> pagedBindingListWrapper;
    private transient PagedListWrapper<Tpaketkuliah> plwDetilMatkul;

	// Databinding
	protected transient AnnotateDataBinder binder;
	private PaketMainCtrl paketMainCtrl;
    private List<Tpaketkuliah> selectedPaketList;

	// ServiceDAOs / Domain Classes
	private transient PaketService paketService;

	/**
	 * default constructor.<br>
	 */
	public PaketDetailCtrl() {
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
			getPaketMainCtrl().setPaketDetailCtrl(this);

			// Get the selected object.
			// Check if this Controller if created on fpakett time. If so,
			// than the selectedXXXBean should be null
			if (getPaketMainCtrl().getSelectedPaket() != null) {
				setSelectedPaket(getPaketMainCtrl().getSelectedPaket());
			} else {
				setSelectedPaket(null);
            }
            if (getPaketMainCtrl().getSelectedPaketList() != null) {
                setSelectedPaketList(getPaketMainCtrl().getSelectedPaketList());
            } else {
                setSelectedPaketList(null);
            }
		} else {
			setSelectedPaket(null);
            setSelectedPaketList(null);
		}
        //this.listDetilMatkul
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
	public void onCreate$windowPaketDetail(Event event) throws Exception {
        setPageSize(20);
		binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);
        doFillListbox();
		binder.loadAll();
        btnNewMatkul.setVisible(false);
        btnDeleteMatkul.setVisible(false);
		doFitSize(event);
	}
    public void doFitSize() {
        // normally 0 ! Or we have a i.e. a toolBar on top of the listBox.
        final int specialSize = 5;
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
        final int maxListBoxHeight = height - specialSize - 148;
        setCountRows((int) Math.round(maxListBoxHeight / 17.7));
    }
    public void doFillListbox() throws Exception {
        doFitSize();
        searchObj = new HibernateSearchObject<Tpaketkuliah>(Tpaketkuliah.class, getCountRows());
        if(getSelectedPaket()!=null) {
            searchObj.addFilterAnd(new Filter(ConstantUtil.TERM, getSelectedPaket().getCterm(), Filter.OP_EQUAL));
            searchObj.addFilterAnd(new Filter(ConstantUtil.SEKOLAH, getSelectedPaket().getMsekolah(), Filter.OP_EQUAL));
            searchObj.addFilterAnd(new Filter(ConstantUtil.PRODI, getSelectedPaket().getMprodi(), Filter.OP_EQUAL));
        }
        searchObj.addSort(ConstantUtil.MATAKULIAH_DOT_CODE, false);
        // Change the BindingListModel.
        if (getBinder() != null) {
            try{
                getPagedBindingListWrapper().clear();
                getPagedBindingListWrapper().addAll(getPaketMainCtrl().getList(getSelectedPaket()));
            } catch (NullPointerException e) {}
            BindingListModelList lml = (BindingListModelList) getListboxDetilMatkul().getModel();
            setPakets(lml);
            if (getSelectedPaket() == null && lml!=null) {
                // init the bean with the first record in the List
                if (lml.getSize() > 0) {
                    final int rowIndex = 0;
                    getListboxDetilMatkul().setSelectedIndex(rowIndex);
                    setSelectedPaket((Tpaketkuliah) lml.get(0));
                    Events.sendEvent(new Event("onSelect", getListboxDetilMatkul(), getSelectedPaket()));
                }
            }
        }
        pagingDetilMatkul.setPageSize(pageSize);
		pagingDetilMatkul.setDetailed(true);
		//getPlwDetilMatkul().init(searchObj, listDetilMatkul, pagingDetilMatkul);
		//listDetilMatkul.setItemRenderer(new DetilKurikulumSearchList());
        //getPagedBindingListWrapper().clear();
        setListBox(getPaketMainCtrl().getList(getSelectedPaket()));
    }
     public  void setListBox(List<Tpaketkuliah> lfa){
        getPaketMainCtrl().setSelectedPaketList(lfa);
        ListBoxUtil.resetList(listboxDetilMatkul);
        for (int i=0; i < lfa.size();i++){
            Tpaketkuliah fa = new Tpaketkuliah();
            fa = (Tpaketkuliah) lfa.get(i);
            Listitem ltm = new Listitem();
            ltm.setAttribute("data", fa);
            ltm.setParent(listboxDetilMatkul);
            Listcell listcell = new Listcell(fa.getMtbmtkl().getCkdmtk());
			listcell.setParent(ltm);
			listcell = new Listcell(fa.getMtbmtkl().getCnamamk());
			listcell.setParent(ltm);
        }
        //setListboxDetilMatkul(null);
        setListboxDetilMatkul(listboxDetilMatkul);
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
		borderlayout_PaketDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

		windowPaketDetail.invalidate();
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
		//txtb_nim.setReadonly(b);
		//txtb_mahasiswa.setReadonly(b);
		//txtb_sekolah.setReadonly(b);
		//txtb_prodi.setReadonly(b);
		txtb_thajar.setReadonly(b);
		txtb_term.setReadonly(b);
        btnSearchSekolahExtended.setDisabled(b);
        btnSearchProdiExtended.setDisabled(b);
        btnSearchMatakuliahExtended.setDisabled(b);
        btnNewMatkul.setVisible(!b);
        btnDeleteMatkul.setVisible(!b);
	}

    public void onClick$btnSearchProdiExtended(Event event) {
        doSearchProdiExtended(event);
    }

    private void doSearchProdiExtended(Event event) {
        Mprodi prodi = ProdiExtendedSearchListBox.show(windowPaketDetail);

        if (prodi != null) {
            txtb_kodeprodi.setValue(prodi.getCkdprogst());
            txtb_prodi.setValue(prodi.getCnmprogst());
            Tpaketkuliah obj = getPaket();
            obj.setMprodi(prodi);
            setPaket(obj);
        }
    }

    public void onClick$btnSearchSekolahExtended(Event event) {
        doSearchSekolahExtended(event);
    }

    private void doSearchSekolahExtended(Event event) {
        Msekolah sekolah = SekolahExtendedSearchListBox.show(windowPaketDetail);

        if (sekolah != null) {
            txtb_sekolah.setValue(sekolah.getCnamaSekolah());
            Tpaketkuliah obj = getPaket();
            obj.setMsekolah(sekolah);
            setPaket(obj);
        }
    }

    public void onClick$btnSearchMatakuliahExtended(Event event) {
        doSearchMatakuliahExtended(event);
    }

    public void onClick$btnNewMatkul(Event event) {
        doSearchMatakuliahExtended(event);
    }

    public void onClick$btnDeleteMatkul(Event event) throws InterruptedException {
        try {
        Listitem item = listboxDetilMatkul.getSelectedItem();
        getPaketMainCtrl().getSelectedPaketList().remove((Tpaketkuliah) item.getAttribute("data"));
        getPaketMainCtrl().getDelPaketList().add((Tpaketkuliah) item.getAttribute("data"));
        listboxDetilMatkul.removeItemAt(listboxDetilMatkul.getSelectedIndex());
        } catch(Exception e) {
            ZksampleMessageUtils.showErrorMessage("Pilih baris data yang ingin dihapus");
        }
    }

    private void doSearchMatakuliahExtended(Event event) {
        Mtbmtkl matkul = MatakuliahExtendedSearchListBox.show(windowPaketDetail);

        if (matkul != null) {
            Tpaketkuliah obj = new Tpaketkuliah();
            obj.setCterm(getPaket().getCterm());
            obj.setCthajar(getPaket().getCthajar());
            obj.setMprodi(getPaket().getMprodi());
            obj.setMsekolah(getPaket().getMsekolah());
            obj.setMtbmtkl(matkul);
            List<Tpaketkuliah> list = getPaketMainCtrl().getSelectedPaketList();
            list.add(obj);
            setListBox(list);
            //setSelectedPaketList(list);
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
	public Tpaketkuliah getPaket() {
		// STORED IN THE module's MainController
		return getPaketMainCtrl().getSelectedPaket();
	}

	public void setPaket(Tpaketkuliah anPaket) {
		// STORED IN THE module's MainController
		getPaketMainCtrl().setSelectedPaket(anPaket);
	}

	public Tpaketkuliah getSelectedPaket() {
		// STORED IN THE module's MainController
		return getPaketMainCtrl().getSelectedPaket();
	}

	public void setSelectedPaket(Tpaketkuliah selectedPaket) {
		// STORED IN THE module's MainController
		getPaketMainCtrl().setSelectedPaket(selectedPaket);
	}

	public BindingListModelList getPakets() {
		// STORED IN THE module's MainController
		return getPaketMainCtrl().getPakets();
	}

	public void setPakets(BindingListModelList pakets) {
		// STORED IN THE module's MainController
		getPaketMainCtrl().setPakets(pakets);
	}

	public AnnotateDataBinder getBinder() {
		return this.binder;
	}

	public void setBinder(AnnotateDataBinder binder) {
		this.binder = binder;
	}

	public void setPaketService(PaketService paketService) {
		this.paketService = paketService;
	}

	public PaketService getPaketService() {
		return this.paketService;
	}

	public void setPaketMainCtrl(PaketMainCtrl paketMainCtrl) {
		this.paketMainCtrl = paketMainCtrl;
	}

	public PaketMainCtrl getPaketMainCtrl() {
		return this.paketMainCtrl;
	}

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void setPagedBindingListWrapper(PagedBindingListWrapper<Tpaketkuliah> pagedBindingListWrapper) {
        this.pagedBindingListWrapper = pagedBindingListWrapper;
    }

    public PagedBindingListWrapper<Tpaketkuliah> getPagedBindingListWrapper() {
        return pagedBindingListWrapper;
    }

    public Listbox getListboxDetilMatkul() {
        return this.listboxDetilMatkul;
    }

    public void setListboxDetilMatkul(Listbox listboxDetilMatkul) {
        this.listboxDetilMatkul = listboxDetilMatkul;
    }

    public void setSelectedPaketList(List<Tpaketkuliah> selectedPaketList) {
        // STORED IN THE module's MainController
        getPaketMainCtrl().setSelectedPaketList(selectedPaketList);
    }

    public PagedListWrapper<Tpaketkuliah> getPlwDetilMatkul() {
        return plwDetilMatkul;
    }

    public void setPlwDetilKurikulum(PagedListWrapper<Tpaketkuliah> plwDetilMatkul) {
        this.plwDetilMatkul = plwDetilMatkul;
    }
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
