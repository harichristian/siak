package id.ac.idu.webui.administrasi.calendarakademik;

import id.ac.idu.administrasi.service.CalendarAkademikService;
import id.ac.idu.administrasi.service.JenjangService;
import id.ac.idu.administrasi.service.ProdiService;
import id.ac.idu.backend.model.Mcalakademik;
import id.ac.idu.backend.model.Mjenjang;
import id.ac.idu.backend.model.Mprodi;
import id.ac.idu.webui.util.GFCBaseCtrl;
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
 * Date: 09/03/12
 * Time: 12:02
 * To change this template use File | Settings | File Templates.
 */
public class CalendarAkademikDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(CalendarAkademikDetailCtrl.class);

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
    protected Textbox txtb_filName1; // autowired
    protected Intbox txtb_filBobot; // autowired
    protected Intbox txtb_filNilaiAwal; // autowired
    protected Intbox txtb_filNilaiAkhir; // autowired
    protected Textbox txtb_filProdi; // autowired
    protected Textbox txtb_filJenjang; // autowired

    protected Button button_CalakademikDialog_PrintCalakademik; // autowired

    // Databinding
    protected transient AnnotateDataBinder binder;
    private CalendarAkademikMainCtrl calakademikMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient CalendarAkademikService calendarAkademikService;
    private transient JenjangService jenjangService;
    private transient ProdiService prodiService;

    /**
     * default constructor.<br>
     */
    public CalendarAkademikDetailCtrl() {
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
            setCalendarAkademikMainCtrl((CalendarAkademikMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getCalendarAkademikMainCtrl().setCalendarAkademikDetailCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getCalendarAkademikMainCtrl().getSelectedCalakademik() != null) {
                setSelectedCalakademik(getCalendarAkademikMainCtrl().getSelectedCalakademik());
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
        txtb_filName1.setReadonly(b);
        txtb_filBobot.setReadonly(b); // autowired
        txtb_filNilaiAwal.setReadonly(b); // autowired
        txtb_filNilaiAkhir.setReadonly(b); // autowired
        txtb_filProdi.setReadonly(b);
        txtb_filJenjang.setReadonly(b);

    }

    public void onChange$txtb_filProdi() {
        if (txtb_filProdi.getValue() != null) {
            Mprodi prodi = prodiService.getProdiByID(Integer.parseInt(txtb_filProdi.getValue()));

            if (prodi != null) {
                txtb_filProdi.setValue(txtb_filProdi.getValue() + " - " + prodi.getCnmprogst());
                getSelectedCalakademik().setMprodi(prodi);
            } else {
                txtb_filProdi.setValue("Data Tidak Ditemukan");
            }
        }
    }

    public void onChange$txtb_filJenjang() {
        if (txtb_filJenjang.getValue() != null) {
            Mjenjang jenjang = jenjangService.getJenjangByID(Integer.parseInt(txtb_filJenjang.getValue()));

            if (jenjang != null) {
                txtb_filJenjang.setValue(txtb_filJenjang.getValue() + " - " + jenjang.getCnmjen());
                //getSelectedCalakademik().setMjenjang(jenjang);
            } else {
                txtb_filJenjang.setValue("Data Tidak Ditemukan");
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
        return getCalendarAkademikMainCtrl().getSelectedCalakademik();
    }

    public void setCalakademik(Mcalakademik anCalakademik) {
        // STORED IN THE module's MainController
        getCalendarAkademikMainCtrl().setSelectedCalakademik(anCalakademik);
    }

    public Mcalakademik getSelectedCalakademik() {
        // STORED IN THE module's MainController
        return getCalendarAkademikMainCtrl().getSelectedCalakademik();
    }

    public void setSelectedCalakademik(Mcalakademik selectedCalakademik) {
        // STORED IN THE module's MainController
        getCalendarAkademikMainCtrl().setSelectedCalakademik(selectedCalakademik);
    }

    public BindingListModelList getCalakademiks() {
        // STORED IN THE module's MainController
        return getCalendarAkademikMainCtrl().getCalakademiks();
    }

    public void setCalakademiks(BindingListModelList offices) {
        // STORED IN THE module's MainController
        getCalendarAkademikMainCtrl().setCalakademiks(offices);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public void setCalendarAkademikService(CalendarAkademikService calendarAkademikService) {
        this.calendarAkademikService = calendarAkademikService;
    }

    public CalendarAkademikService getCalendarAkademikService() {
        return this.calendarAkademikService;
    }

    public void setCalendarAkademikMainCtrl(CalendarAkademikMainCtrl calakademikMainCtrl) {
        this.calakademikMainCtrl = calakademikMainCtrl;
    }

    public CalendarAkademikMainCtrl getCalendarAkademikMainCtrl() {
        return this.calakademikMainCtrl;
    }

    public JenjangService getJenjangService() {
        return jenjangService;
    }

    public void setJenjangService(JenjangService jenjangService) {
        this.jenjangService = jenjangService;
    }

    public ProdiService getProdiService() {
        return prodiService;
    }

    public void setProdiService(ProdiService prodiService) {
        this.prodiService = prodiService;
    }

}
