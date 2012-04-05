package id.ac.idu.webui.administrasi.term;

import id.ac.idu.administrasi.service.TermService;
import id.ac.idu.backend.model.Mterm;
import id.ac.idu.backend.model.Mthajar;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.searchdialogs.ThajarExtendedSearchListBox;
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
 * Date: 28/03/12
 * Time: 10:51
 * To change this template use File | Settings | File Templates.
 */
public class TermDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(TermDetailCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowTermDetail; // autowired

    protected Borderlayout borderlayout_TermDetail; // autowired

    protected Textbox txtb_filNr; // autowired
    protected Textbox txtb_filName1; // autowired
    protected Textbox txtb_filThajar; // autowired
    protected Textbox txtb_filSmt; // autowired
    protected Datebox txtb_filTanggalMulai; // autowired
    protected Datebox txtb_filTanggalAkhir; // autowired
    protected Button btnSearchThajarExtended; // autowired
    protected Row rowThajar; // autowired
    protected Button button_TermDialog_PrintTerm; // autowired

    // Databinding
    protected transient AnnotateDataBinder binder;
    private TermMainCtrl termMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient TermService termService;

    /**
     * default constructor.<br>
     */
    public TermDetailCtrl() {
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
            setTermMainCtrl((TermMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getTermMainCtrl().setTermDetailCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getTermMainCtrl().getSelectedTerm() != null) {
                setSelectedTerm(getTermMainCtrl().getSelectedTerm());
            } else
                setSelectedTerm(null);
        } else {
            setSelectedTerm(null);
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
    public void onCreate$windowTermDetail(Event event) throws Exception {
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
        borderlayout_TermDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowTermDetail.invalidate();
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
        txtb_filTanggalMulai.setReadonly(b);
        txtb_filTanggalAkhir.setReadonly(b);
        btnSearchThajarExtended.setDisabled(b);

    }

    /*--------------------------- TAHUN AJAR LOV ---------------------------*/
    public void onClick$btnSearchThajarExtended(Event event) {
        doSearchThajarExtended(event);
    }

    private void doSearchThajarExtended(Event event) {
        Mthajar thajar = ThajarExtendedSearchListBox.show(windowTermDetail);

        if (thajar != null) {
            rowThajar.setVisible(true);
            txtb_filThajar.setValue(thajar.getCthajar());
            txtb_filSmt.setValue(thajar.getCsmt());
            Mterm aTerm = getTerm();
            aTerm.setMthajar(thajar);
            setTerm(aTerm);
        } else {
            rowThajar.setVisible(false);
            txtb_filThajar.setValue("");
            txtb_filSmt.setValue("");
            Mterm aTerm = getTerm();
            aTerm.setMthajar(null);
            setTerm(aTerm);
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
    public Mterm getTerm() {
        // STORED IN THE module's MainController
        return getTermMainCtrl().getSelectedTerm();
    }

    public void setTerm(Mterm anTerm) {
        // STORED IN THE module's MainController
        getTermMainCtrl().setSelectedTerm(anTerm);
    }

    public Mterm getSelectedTerm() {
        // STORED IN THE module's MainController
        return getTermMainCtrl().getSelectedTerm();
    }

    public void setSelectedTerm(Mterm selectedTerm) {
        // STORED IN THE module's MainController
        getTermMainCtrl().setSelectedTerm(selectedTerm);
    }

    public BindingListModelList getTerms() {
        // STORED IN THE module's MainController
        return getTermMainCtrl().getTerms();
    }

    public void setTerms(BindingListModelList offices) {
        // STORED IN THE module's MainController
        getTermMainCtrl().setTerms(offices);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public void setTermService(TermService termService) {
        this.termService = termService;
    }

    public TermService getTermService() {
        return this.termService;
    }

    public void setTermMainCtrl(TermMainCtrl termMainCtrl) {
        this.termMainCtrl = termMainCtrl;
    }

    public TermMainCtrl getTermMainCtrl() {
        return this.termMainCtrl;
    }
}
