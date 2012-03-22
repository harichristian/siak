package id.ac.idu.webui.irs.paket;

import id.ac.idu.backend.model.Mprodi;
import id.ac.idu.backend.model.Msekolah;
import id.ac.idu.backend.model.Mtbmtkl;
import id.ac.idu.backend.model.Tpaketkuliah;
import id.ac.idu.irs.service.PaketService;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.searchdialogs.MatakuliahExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.ProdiExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.SekolahExtendedSearchListBox;
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

	// Databinding
	protected transient AnnotateDataBinder binder;
	private PaketMainCtrl paketMainCtrl;

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
			} else
				setSelectedPaket(null);
		} else {
			setSelectedPaket(null);
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
	public void onCreate$windowPaketDetail(Event event) throws Exception {
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
	}

    public void onClick$btnSearchProdiExtended(Event event) {
        doSearchProdiExtended(event);
    }

    private void doSearchProdiExtended(Event event) {
        Mprodi prodi = ProdiExtendedSearchListBox.show(windowPaketDetail);

        if (prodi != null) {
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

    private void doSearchMatakuliahExtended(Event event) {
        Mtbmtkl matkul = MatakuliahExtendedSearchListBox.show(windowPaketDetail);

        if (matkul != null) {
            txtb_matakuliah.setValue(matkul.getCnamamk());
            Tpaketkuliah obj = getPaket();
            obj.setMtbmtkl(matkul);
            setPaket(obj);
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
}
