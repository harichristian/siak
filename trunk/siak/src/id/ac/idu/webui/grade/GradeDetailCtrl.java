package id.ac.idu.webui.grade;

import id.ac.idu.administrasi.service.GradeService;
import id.ac.idu.administrasi.service.JenjangService;
import id.ac.idu.administrasi.service.ProdiService;
import id.ac.idu.backend.model.Mgrade;
import id.ac.idu.backend.model.Mjenjang;
import id.ac.idu.backend.model.Mprodi;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.searchdialogs.JenjangExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.ProdiExtendedSearchListBox;
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
 * This is the controller class for the /WEB-INF/pages/grade/officeList.zul
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
public class GradeDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(GradeDetailCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowGradeDetail; // autowired

    protected Borderlayout borderlayout_GradeDetail; // autowired

    protected Textbox txtb_filNr; // autowired
    protected Textbox txtb_filName1; // autowired
    protected Doublebox txtb_filBobot; // autowired
    protected Doublebox txtb_filNilaiAwal; // autowired
    protected Doublebox txtb_filNilaiAkhir; // autowired
    protected Textbox txtb_filProdi; // autowired
    protected Textbox txtb_filJenjang; // autowired

    protected Button button_GradeDialog_PrintGrade; // autowired
    protected Button btnSearchJenjangExtended; // autowired
    protected Button btnSearchProdiExtended; // autowired

    // Databinding
    protected transient AnnotateDataBinder binder;
    private GradeMainCtrl gradeMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient GradeService gradeService;
    private transient JenjangService jenjangService;
    private transient ProdiService prodiService;

    /**
     * default constructor.<br>
     */
    public GradeDetailCtrl() {
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
            setGradeMainCtrl((GradeMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getGradeMainCtrl().setGradeDetailCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getGradeMainCtrl().getSelectedGrade() != null) {
                setSelectedGrade(getGradeMainCtrl().getSelectedGrade());
            } else
                setSelectedGrade(null);
        } else {
            setSelectedGrade(null);
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
    public void onCreate$windowGradeDetail(Event event) throws Exception {
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
        borderlayout_GradeDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowGradeDetail.invalidate();
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
//        txtb_filProdi.setReadonly(b);
//        txtb_filJenjang.setReadonly(b);
        btnSearchJenjangExtended.setDisabled(b);
        btnSearchProdiExtended.setDisabled(b);

    }

    /**
     * If the Button 'Search Branch ExtendedSearch' is clicked.<br>
     *
     * @param event
     */
    public void onClick$btnSearchProdiExtended(Event event) {
        doSearchProdiExtended(event);
    }

    /**
     * Opens the Search and Get Dialog for Branches.<br>
     * It appends/changes the branch object for the current bean.<br>
     *
     * @param event
     */
    private void doSearchProdiExtended(Event event) {
        Mprodi prodi = ProdiExtendedSearchListBox.show(windowGradeDetail);

        if (prodi != null) {
            txtb_filProdi.setValue(prodi.getCkdprogst() + " - " + prodi.getCnmprogst());
            Mgrade aGrade = getGrade();
            aGrade.setMprodi(prodi);
            setGrade(aGrade);
        }
    }

//    public void onChange$txtb_filProdi() {
//        if (txtb_filProdi.getValue() != null) {
//            if (NumberUtils.isNumber(txtb_filProdi.getValue())) {
//                Mprodi prodi = prodiService.getProdiByID(Integer.parseInt(txtb_filProdi.getValue()));
//
//                if (prodi != null) {
//                    txtb_filProdi.setValue(txtb_filProdi.getValue() + " - " + prodi.getCnmprogst());
//                    getSelectedGrade().setMprodi(prodi);
//                } else {
//                    txtb_filProdi.setValue("Data Tidak Ditemukan");
//                }
//            } else {
//                txtb_filProdi.setValue("Input Data Salah");
//            }
//        }
//    }

    /**
     * If the Button 'Search Branch ExtendedSearch' is clicked.<br>
     *
     * @param event
     */
    public void onClick$btnSearchJenjangExtended(Event event) {
        doSearchJenjangExtended(event);
    }

    /**
     * Opens the Search and Get Dialog for Branches.<br>
     * It appends/changes the branch object for the current bean.<br>
     *
     * @param event
     */
    private void doSearchJenjangExtended(Event event) {
        Mjenjang jenjang = JenjangExtendedSearchListBox.show(windowGradeDetail);

        if (jenjang != null) {
            txtb_filJenjang.setValue(jenjang.getCkdjen() + " - " + jenjang.getCnmjen());
            Mgrade aGrade = getGrade();
            aGrade.setMjenjang(jenjang);
            setGrade(aGrade);
        }
    }


//    public void onChange$txtb_filJenjang() {
//        if (txtb_filJenjang.getValue() != null) {
//            if (NumberUtils.isNumber(txtb_filJenjang.getValue())) {
//                Mjenjang jenjang = jenjangService.getJenjangByID(Integer.parseInt(txtb_filJenjang.getValue()));
//
//                if (jenjang != null) {
//                    txtb_filJenjang.setValue(txtb_filJenjang.getValue() + " - " + jenjang.getCnmjen());
//                    getSelectedGrade().setMjenjang(jenjang);
//                } else {
//                    txtb_filJenjang.setValue("Data Tidak Ditemukan");
//                }
//            } else {
//                txtb_filJenjang.setValue("Input Data Salah");
//            }
//        }
//    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    /**
     * Best Pratice Hint:<br>
     * The setters/getters for the local annotated data binded Beans/Sets are
     * administered in the module's mainController. Working in this way you have
     * clean line to share this beans/sets with other controllers.
     */
    public Mgrade getGrade() {
        // STORED IN THE module's MainController
        return getGradeMainCtrl().getSelectedGrade();
    }

    public void setGrade(Mgrade anGrade) {
        // STORED IN THE module's MainController
        getGradeMainCtrl().setSelectedGrade(anGrade);
    }

    public Mgrade getSelectedGrade() {
        // STORED IN THE module's MainController
        return getGradeMainCtrl().getSelectedGrade();
    }

    public void setSelectedGrade(Mgrade selectedGrade) {
        // STORED IN THE module's MainController
        getGradeMainCtrl().setSelectedGrade(selectedGrade);
    }

    public BindingListModelList getGrades() {
        // STORED IN THE module's MainController
        return getGradeMainCtrl().getGrades();
    }

    public void setGrades(BindingListModelList offices) {
        // STORED IN THE module's MainController
        getGradeMainCtrl().setGrades(offices);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public void setGradeService(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    public GradeService getGradeService() {
        return this.gradeService;
    }

    public void setGradeMainCtrl(GradeMainCtrl gradeMainCtrl) {
        this.gradeMainCtrl = gradeMainCtrl;
    }

    public GradeMainCtrl getGradeMainCtrl() {
        return this.gradeMainCtrl;
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
