package id.ac.idu.webui.administrasi.sekolah;

import id.ac.idu.administrasi.service.PegawaiService;
import id.ac.idu.administrasi.service.SekolahService;
import id.ac.idu.backend.model.Mpegawai;
import id.ac.idu.backend.model.Msekolah;
import id.ac.idu.backend.model.Muniv;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.searchdialogs.PegawaiExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.UnivExtendedSearchListBox;
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
 * User: Hari Christian
 * Date: 08/03/12
 * Time: 0:48
 * To change this template use File | Settings | File Templates.
 */
public class SekolahDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(SekolahDetailCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowSekolahDetail; // autowired

    protected Borderlayout borderlayout_SekolahDetail; // autowired

    protected Textbox txtb_filNr; // autowired
    protected Textbox txtb_filName1; // autowired
    protected Textbox txtb_filSingkatan; // autowired
    protected Textbox txtb_filNamaInggris; // autowired
    protected Textbox txtb_filVisi; // autowired
    protected Textbox txtb_filMisi; // autowired
    protected Textbox txtb_filNoSk; // autowired
    protected Textbox txtb_filPegawai; // autowired
    protected Textbox txtb_filUniversitas; // autowired

    protected Button button_SekolahDialog_PrintSekolah; // autowired
    protected Button btnSearchPegawaiExtended; // autowired
    protected Button btnSearchUniversitasExtended; // autowired

    // Databinding
    protected transient AnnotateDataBinder binder;
    private SekolahMainCtrl sekolahMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient SekolahService sekolahService;
    private transient PegawaiService pegawaiService;

    /**
     * default constructor.<br>
     */
    public SekolahDetailCtrl() {
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
            setSekolahMainCtrl((SekolahMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getSekolahMainCtrl().setSekolahDetailCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getSekolahMainCtrl().getSelectedSekolah() != null) {
                setSelectedSekolah(getSekolahMainCtrl().getSelectedSekolah());
            } else
                setSelectedSekolah(null);
        } else {
            setSelectedSekolah(null);
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
    public void onCreate$windowSekolahDetail(Event event) throws Exception {
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
     * <p/>
     * Calculate how many rows have been place in the listbox. Get the
     * currentDesktopHeight from a hidden Intbox from the index.zul that are
     * filled by onClientInfo() in the indexCtroller.
     */
    public void doFitSize(Event event) {
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
        final int maxListBoxHeight = height - 148;
        borderlayout_SekolahDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowSekolahDetail.invalidate();
    }

    /**
     * Set all components in readOnly/disabled modus.
     * <p/>
     * true = all components are readOnly or disabled.<br>
     * false = all components are accessable.<br>
     *
     * @param b
     */
    public void doReadOnlyMode(boolean b) {
        txtb_filNr.setReadonly(b);
        txtb_filName1.setReadonly(b);
        txtb_filSingkatan.setReadonly(b);
        txtb_filNamaInggris.setReadonly(b);
        txtb_filVisi.setReadonly(b);
        txtb_filMisi.setReadonly(b);
        txtb_filNoSk.setReadonly(b);
        txtb_filPegawai.setReadonly(b);
        txtb_filUniversitas.setReadonly(b);
        btnSearchPegawaiExtended.setDisabled(b);
        btnSearchUniversitasExtended.setDisabled(b);
    }

    /**
     * If the Button 'Search Branch ExtendedSearch' is clicked.<br>
     *
     * @param event
     */
    public void onClick$btnSearchUniversitasExtended(Event event) {
        doSearchUniversitasExtended(event);
    }

    /**
     * Opens the Search and Get Dialog for Branches.<br>
     * It appends/changes the branch object for the current bean.<br>
     *
     * @param event
     */
    private void doSearchUniversitasExtended(Event event) {
        Muniv univ = UnivExtendedSearchListBox.show(windowSekolahDetail);

        if (univ != null) {
            txtb_filUniversitas.setValue(univ.getCkdUniv() + " - " + univ.getCnamaUniv());
            Msekolah aSekolah = getSekolah();
            aSekolah.setMuniv(univ);
            setSekolah(aSekolah);
        }
    }

    /**
     * If the Button 'Search Branch ExtendedSearch' is clicked.<br>
     *
     * @param event
     */
    public void onClick$btnSearchPegawaiExtended(Event event) {
        doSearchPegawaiExtended(event);
    }

    /**
     * Opens the Search and Get Dialog for Branches.<br>
     * It appends/changes the branch object for the current bean.<br>
     *
     * @param event
     */
    private void doSearchPegawaiExtended(Event event) {
        Mpegawai pegawai = PegawaiExtendedSearchListBox.show(windowSekolahDetail);

        if (pegawai != null) {
            txtb_filPegawai.setValue(pegawai.getCnip() + " - " + pegawai.getCnama());
            Msekolah aSekolah = getSekolah();
            aSekolah.setMpegawai(pegawai);
            setSekolah(aSekolah);
        }
    }

    public void onChange$txtb_filPegawai() {
        if (txtb_filPegawai.getValue() != null) {
            Mpegawai pegawai = pegawaiService.getPegawaiByNip(txtb_filPegawai.getValue());

            if (pegawai != null) {
                txtb_filPegawai.setValue(txtb_filPegawai.getValue() + " - " + pegawai.getCnama());
                getSelectedSekolah().setMpegawai(pegawai);
            } else {
                txtb_filPegawai.setValue("Data Tidak Ditemukan");
            }
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
    public Msekolah getSekolah() {
        // STORED IN THE module's MainController
        return getSekolahMainCtrl().getSelectedSekolah();
    }

    public void setSekolah(Msekolah anSekolah) {
        // STORED IN THE module's MainController
        getSekolahMainCtrl().setSelectedSekolah(anSekolah);
    }

    public Msekolah getSelectedSekolah() {
        // STORED IN THE module's MainController
        return getSekolahMainCtrl().getSelectedSekolah();
    }

    public void setSelectedSekolah(Msekolah selectedSekolah) {
        // STORED IN THE module's MainController
        getSekolahMainCtrl().setSelectedSekolah(selectedSekolah);
    }

    public BindingListModelList getSekolahs() {
        // STORED IN THE module's MainController
        return getSekolahMainCtrl().getSekolahs();
    }

    public void setSekolahs(BindingListModelList offices) {
        // STORED IN THE module's MainController
        getSekolahMainCtrl().setSekolahs(offices);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public void setSekolahService(SekolahService sekolahService) {
        this.sekolahService = sekolahService;
    }

    public SekolahService getSekolahService() {
        return this.sekolahService;
    }

    public void setSekolahMainCtrl(SekolahMainCtrl sekolahMainCtrl) {
        this.sekolahMainCtrl = sekolahMainCtrl;
    }

    public SekolahMainCtrl getSekolahMainCtrl() {
        return this.sekolahMainCtrl;
    }

    public PegawaiService getPegawaiService() {
        return pegawaiService;
    }

    public void setPegawaiService(PegawaiService pegawaiService) {
        this.pegawaiService = pegawaiService;
    }

}
