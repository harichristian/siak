package id.ac.idu.webui.administrasi.prodi;

import id.ac.idu.administrasi.service.ProdiService;
import id.ac.idu.backend.model.Mjenjang;
import id.ac.idu.backend.model.Mpegawai;
import id.ac.idu.backend.model.Mprodi;
import id.ac.idu.backend.model.Msekolah;
import id.ac.idu.util.Codec;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.GFCListModelCtrl;
import id.ac.idu.webui.util.searchdialogs.JenjangExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.PegawaiExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.SekolahExtendedSearchListBox;
import id.ac.idu.webui.util.test.EnumConverter;
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
 * Date: 3/7/12
 * Time: 9:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProdiDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(ProdiDetailCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowProdiDetail; // autowired

    protected Borderlayout borderlayout_ProdiDetail; // autowired

    protected Textbox txtb_code; // autowired
    protected Textbox txtb_nama; // autowired
    protected Textbox txtb_singkatan; // autowired
    protected Textbox txtb_status; // autowired
    protected Listbox list_status;
    protected Bandbox cmb_status;
    protected Textbox txtb_jenjang;
    protected Button btnSearchJenjangExtended;
    protected Textbox txtb_sekolah;
    protected Button btnSearchSekolahExtended;
    protected Textbox txtb_inggris;
    protected Textbox txtb_kaprodi;
    protected Button btnSearchKaprodiExtended;
    protected Textbox txtb_sekkaprodi;
    protected Button btnSearchSekkaprodiExtended;
    protected Button button_ProdiDialog_PrintProdi; // autowired

    // Databinding
    protected transient AnnotateDataBinder binder;
    private ProdiMainCtrl prodiMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient ProdiService prodiService;

    /**
     * default constructor.<br>
     */
    public ProdiDetailCtrl() {
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
            setProdiMainCtrl((ProdiMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getProdiMainCtrl().setProdiDetailCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getProdiMainCtrl().getSelectedProdi() != null) {
                setSelectedProdi(getProdiMainCtrl().getSelectedProdi());
            } else
                setSelectedProdi(null);
        } else {
            setSelectedProdi(null);
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
    public void onCreate$windowProdiDetail(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);
        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.StatusAktif.class)).getEnumToList(),
                list_status, cmb_status, (getProdi() != null)?getProdi().getCstatus():null);
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
        borderlayout_ProdiDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowProdiDetail.invalidate();
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
        txtb_code.setReadonly(b);
        txtb_nama.setReadonly(b);
        txtb_singkatan.setReadonly(b);
        txtb_status.setReadonly(b);
        list_status.setDisabled(b);
        cmb_status.setDisabled(b);
        btnSearchJenjangExtended.setDisabled(b);
        btnSearchSekolahExtended.setDisabled(b);
        txtb_inggris.setReadonly(b);
        btnSearchKaprodiExtended.setDisabled(b);
        btnSearchSekkaprodiExtended.setDisabled(b);
    }

    public void onClick$btnSearchJenjangExtended(Event event) {
        doSearchJenjangExtended(event);
    }

    private void doSearchJenjangExtended(Event event) {
        Mjenjang jenjang = JenjangExtendedSearchListBox.show(windowProdiDetail);

        if (jenjang != null) {
            txtb_jenjang.setValue(jenjang.getCnmjen());
            Mprodi obj = getProdi();
            obj.setMjenjang(jenjang);
            setProdi(obj);
        }
    }

    public void onClick$btnSearchSekolahExtended(Event event) {
        doSearchSekolahExtended(event);
    }

    private void doSearchSekolahExtended(Event event) {
        Msekolah sekolah = SekolahExtendedSearchListBox.show(windowProdiDetail);

        if (sekolah != null) {
            txtb_sekolah.setValue(sekolah.getCnamaSekolah());
            Mprodi obj = getProdi();
            obj.setMsekolah(sekolah);
            setProdi(obj);
        }
    }

    /**
     * If the Lookup Button is clicked.<br>
     *
     * @param event
     */
    public void onClick$btnSearchKaprodiExtended(Event event) {
        doSearchKaprodiExtended(event);
    }

    /**
     * If the Lookup Button is clicked.<br>
     *
     * @param event
     */
    public void onClick$btnSearchSekkaprodiExtended(Event event) {
        doSearchSekkaprodiExtended(event);
    }

    /**
     * Opens the Search and Get Dialog for Pegawai.<br>
     * It appends/changes the branch object for the current bean.<br>
     *
     * @param event
     */
    private void doSearchKaprodiExtended(Event event) {
        Mpegawai pegawai = PegawaiExtendedSearchListBox.show(windowProdiDetail);

        System.out.println("pegawai id = " + pegawai.getId());
        if (pegawai != null) {
            txtb_kaprodi.setValue(pegawai.getCnip());
            Mprodi prodi = getProdi();
            prodi.setCnip(pegawai.getCnip());
            setProdi(prodi);
        }
    }

    /**
     * Opens the Search and Get Dialog for Pegawai.<br>
     * It appends/changes the branch object for the current bean.<br>
     *
     * @param event
     */
    private void doSearchSekkaprodiExtended(Event event) {
        Mpegawai pegawai = PegawaiExtendedSearchListBox.show(windowProdiDetail);

        System.out.println("pegawai id = " + pegawai.getId());
        if (pegawai != null) {
            txtb_sekkaprodi.setValue(pegawai.getCnip());
            Mprodi prodi = getProdi();
            prodi.setCnipsekprodi(pegawai.getCnip());
            setProdi(prodi);
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
    public Mprodi getProdi() {
        // STORED IN THE module's MainController
        return getProdiMainCtrl().getSelectedProdi();
    }

    public void setProdi(Mprodi anProdi) {
        // STORED IN THE module's MainController
        getProdiMainCtrl().setSelectedProdi(anProdi);
    }

    public Mprodi getSelectedProdi() {
        // STORED IN THE module's MainController
        return getProdiMainCtrl().getSelectedProdi();
    }

    public void setSelectedProdi(Mprodi selectedProdi) {
        // STORED IN THE module's MainController
        getProdiMainCtrl().setSelectedProdi(selectedProdi);
    }

    public BindingListModelList getProdis() {
        // STORED IN THE module's MainController
        return getProdiMainCtrl().getProdis();
    }

    public void setProdis(BindingListModelList prodis) {
        // STORED IN THE module's MainController
        getProdiMainCtrl().setProdis(prodis);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public void setProdiService(ProdiService prodiService) {
        this.prodiService = prodiService;
    }

    public ProdiService getProdiService() {
        return this.prodiService;
    }

    public void setProdiMainCtrl(ProdiMainCtrl prodiMainCtrl) {
        this.prodiMainCtrl = prodiMainCtrl;
    }

    public ProdiMainCtrl getProdiMainCtrl() {
        return this.prodiMainCtrl;
    }
}
