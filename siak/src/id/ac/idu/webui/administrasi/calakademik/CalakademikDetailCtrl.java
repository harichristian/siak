package id.ac.idu.webui.administrasi.calakademik;

import id.ac.idu.administrasi.service.CalakademikService;
import id.ac.idu.administrasi.service.KegiatanService;
import id.ac.idu.administrasi.service.ProdiService;
import id.ac.idu.administrasi.service.SekolahService;
import id.ac.idu.backend.model.*;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.searchdialogs.KegiatanExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.ProdiExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.SekolahExtendedSearchListBox;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.*;

import java.io.Serializable;

/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the /WEB-INF/pages/calakademik/officeList.zul
 * file.<br>
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 *
 * @author Hari
 * @changes 05/15/2009: sge Migrating the list models for paging. <br>
 * 07/24/2009: sge changings for clustering.<br>
 * 10/12/2009: sge changings in the saving routine.<br>
 * 11/07/2009: bbr changed to extending from GFCBaseCtrl<br>
 * (GenericForwardComposer) for spring-managed creation.<br>
 * 07/04/2010: sge modified for zk5.x with complete Annotated
 * Databinding.<br>
 */
public class CalakademikDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(CalakademikDetailCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowCalakademikDetail; // autowired

    protected Borderlayout borderlayout_CalakademikDetail; // autowired

    protected Textbox txtb_filNr; // autowired
    protected Datebox txtb_filTanggalAwal; // autowired
    protected Datebox txtb_filTanggalAkhir; // autowired
    protected Intbox txtb_filJamAwal; // autowired
    protected Intbox txtb_filJamAkhir; // autowired
    protected Textbox txtb_filTerm; // autowired
    protected Textbox txtb_filTahunAjaran; // autowired
    protected Textbox txtb_filSemester; // autowired
    protected Textbox txtb_filSekolah; // autowired
    protected Textbox txtb_filProdi; // autowired
    protected Textbox txtb_filKegiatan; // autowired

    protected Button button_CalakademikDialog_PrintCalakademik; // autowired
    protected Button btnSearchSekolahExtended; // autowired
    protected Button btnSearchProdiExtended; // autowired
    protected Button btnSearchKegiatanExtended; // autowired

    // Databinding
    protected transient AnnotateDataBinder binder;
    private CalakademikMainCtrl calakademikMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient CalakademikService calakademikService;
    private transient SekolahService sekolahService;
    private transient ProdiService prodiService;
    private transient KegiatanService kegiatanService;

    public SekolahService getSekolahService() {
        return sekolahService;
    }

    public void setSekolahService(SekolahService sekolahService) {
        this.sekolahService = sekolahService;
    }

    public KegiatanService getKegiatanService() {
        return kegiatanService;
    }

    public void setKegiatanService(KegiatanService kegiatanService) {
        this.kegiatanService = kegiatanService;
    }

    /**
     * default constructor.<br>
     */
    public CalakademikDetailCtrl() {
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
            setCalakademikMainCtrl((CalakademikMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getCalakademikMainCtrl().setCalakademikDetailCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getCalakademikMainCtrl().getSelectedCalakademik() != null) {
                setSelectedCalakademik(getCalakademikMainCtrl().getSelectedCalakademik());
            } else
                setSelectedCalakademik(null);
        } else {
            setSelectedCalakademik(null);
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
    public void onCreate$windowCalakademikDetail(Event event) throws Exception {
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
        borderlayout_CalakademikDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowCalakademikDetail.invalidate();
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
        txtb_filTanggalAwal.setReadonly(b);
        txtb_filTanggalAkhir.setReadonly(b);
        txtb_filJamAwal.setReadonly(b);
        txtb_filJamAkhir.setReadonly(b);
        txtb_filTerm.setReadonly(b);
        txtb_filTahunAjaran.setReadonly(b);
        txtb_filSemester.setReadonly(b);
        txtb_filSekolah.setReadonly(b);
        txtb_filKegiatan.setReadonly(b);
        txtb_filProdi.setReadonly(b);

        btnSearchSekolahExtended.setDisabled(b);
        btnSearchProdiExtended.setDisabled(b);
        btnSearchKegiatanExtended.setDisabled(b);
    }

    /*--------------------------- PRODI LOV ---------------------------*/
    public void onClick$btnSearchProdiExtended(Event event) {
        doSearchProdiExtended(event);
    }

    private void doSearchProdiExtended(Event event) {
        Mprodi prodi = ProdiExtendedSearchListBox.show(windowCalakademikDetail);

        if (prodi != null) {
            txtb_filProdi.setValue(prodi.getCkdprogst() + " - " + prodi.getCnmprogst());
            Mcalakademik aCalakademik = getCalakademik();
            aCalakademik.setMprodi(prodi);
            setCalakademik(aCalakademik);
        }
    }

    public void onChange$txtb_filProdi() {
        if (txtb_filProdi.getValue() != null) {
            if (NumberUtils.isNumber(txtb_filProdi.getValue())) {
                Mprodi prodi = prodiService.getProdiByID(Integer.parseInt(txtb_filProdi.getValue()));

                if (prodi != null) {
                    txtb_filProdi.setValue(txtb_filProdi.getValue() + " - " + prodi.getCnmprogst());
                    getSelectedCalakademik().setMprodi(prodi);
                } else {
                    txtb_filProdi.setValue("Data Tidak Ditemukan");
                }
            } else {
                txtb_filProdi.setValue("Input Data Salah");
            }
        }
    }

    /*--------------------------- SEKOLAH LOV ---------------------------*/
    public void onClick$btnSearchSekolahExtended(Event event) {
        doSearchSekolahExtended(event);
    }

    private void doSearchSekolahExtended(Event event) {
        Msekolah sekolah = SekolahExtendedSearchListBox.show(windowCalakademikDetail);

        if (sekolah != null) {
            txtb_filSekolah.setValue(sekolah.getCkdsekolah() + " - " + sekolah.getCnamaSekolah());
            Mcalakademik aCalakademik = getCalakademik();
            aCalakademik.setMsekolah(sekolah);
            setCalakademik(aCalakademik);
        }
    }

    public void onChange$txtb_filSekolah() {
        if (txtb_filSekolah.getValue() != null) {
            if (NumberUtils.isNumber(txtb_filSekolah.getValue())) {
                Msekolah sekolah = sekolahService.getSekolahById(Integer.parseInt(txtb_filSekolah.getValue()));

                if (sekolah != null) {
                    txtb_filSekolah.setValue(txtb_filSekolah.getValue() + " - " + sekolah.getCnamaSekolah());
                    getSelectedCalakademik().setMsekolah(sekolah);
                } else {
                    txtb_filSekolah.setValue("Data Tidak Ditemukan");
                }
            } else {
                txtb_filSekolah.setValue("Input Data Salah");
            }
        }
    }

    /*--------------------------- KEGIATAN LOV ---------------------------*/
    public void onClick$btnSearchKegiatanExtended(Event event) {
        doSearchKegiatanExtended(event);
    }

    private void doSearchKegiatanExtended(Event event) {
        Mkegiatan kegiatan = KegiatanExtendedSearchListBox.show(windowCalakademikDetail);

        if (kegiatan != null) {
            txtb_filKegiatan.setValue(kegiatan.getCkdkgt() + " - " + kegiatan.getCnmkgt());
            Mcalakademik aCalakademik = getCalakademik();
            aCalakademik.setMkegiatan(kegiatan);
            setCalakademik(aCalakademik);
        }
    }
    public void onChange$txtb_filKegiatan() {
        if (txtb_filKegiatan.getValue() != null) {
            if (NumberUtils.isNumber(txtb_filKegiatan.getValue())) {
                Mkegiatan kegiatan = kegiatanService.getKegiatanById(Integer.parseInt(txtb_filKegiatan.getValue()));

                if (kegiatan != null) {
                    txtb_filKegiatan.setValue(txtb_filKegiatan.getValue() + " - " + kegiatan.getCnmkgt());
                    getSelectedCalakademik().setMkegiatan(kegiatan);
                } else {
                    txtb_filKegiatan.setValue("Data Tidak Ditemukan");
                }
            } else {
                txtb_filKegiatan.setValue("Input Data Salah");
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
    public Mcalakademik getCalakademik() {
        // STORED IN THE module's MainController
        return getCalakademikMainCtrl().getSelectedCalakademik();
    }

    public void setCalakademik(Mcalakademik anCalakademik) {
        // STORED IN THE module's MainController
        getCalakademikMainCtrl().setSelectedCalakademik(anCalakademik);
    }

    public Mcalakademik getSelectedCalakademik() {
        // STORED IN THE module's MainController
        return getCalakademikMainCtrl().getSelectedCalakademik();
    }

    public void setSelectedCalakademik(Mcalakademik selectedCalakademik) {
        // STORED IN THE module's MainController
        getCalakademikMainCtrl().setSelectedCalakademik(selectedCalakademik);
    }

    public BindingListModelList getCalakademiks() {
        // STORED IN THE module's MainController
        return getCalakademikMainCtrl().getCalakademiks();
    }

    public void setCalakademiks(BindingListModelList offices) {
        // STORED IN THE module's MainController
        getCalakademikMainCtrl().setCalakademiks(offices);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public void setCalakademikService(CalakademikService calakademikService) {
        this.calakademikService = calakademikService;
    }

    public CalakademikService getCalakademikService() {
        return this.calakademikService;
    }

    public void setCalakademikMainCtrl(CalakademikMainCtrl calakademikMainCtrl) {
        this.calakademikMainCtrl = calakademikMainCtrl;
    }

    public CalakademikMainCtrl getCalakademikMainCtrl() {
        return this.calakademikMainCtrl;
    }

    public ProdiService getProdiService() {
        return prodiService;
    }

    public void setProdiService(ProdiService prodiService) {
        this.prodiService = prodiService;
    }

}
