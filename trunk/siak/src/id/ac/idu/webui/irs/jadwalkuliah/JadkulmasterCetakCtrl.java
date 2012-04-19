package id.ac.idu.webui.irs.jadwalkuliah;

import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.ProdiService;
import id.ac.idu.backend.model.Customer;
import id.ac.idu.backend.model.Mprodi;
import id.ac.idu.backend.model.Tjadkuldetil;
import id.ac.idu.backend.model.Tjadkulmaster;
import id.ac.idu.backend.service.BrancheService;
import id.ac.idu.irs.service.JadkulService;
import id.ac.idu.webui.irs.report.JadkulmasterSimpleDJReport;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.ZksampleMessageUtils;
import id.ac.idu.webui.util.pagging.PagedListWrapper;
import id.ac.idu.webui.util.searchdialogs.ProdiExtendedSearchListBox;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.*;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 06/04/12
 * Time: 1:16
 * To change this template use File | Settings | File Templates.
 */
public class JadkulmasterCetakCtrl extends GFCBaseCtrl implements Serializable {

    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(JadkulmasterDialogCtrl.class);

    private transient PagedListWrapper<Tjadkuldetil> plwTjadkuldetils;
    private transient PagedListWrapper<Customer> plwCustomers;

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window tjadkulmasterCetakWindow; // autowired
    protected Textbox txtb_code; // autowired
    protected Textbox txtb_filProdi; // autowired
    protected Textbox txtb_filTerm; // autowired
    protected Textbox txtb_filTahunajaran; // autowired
    protected Textbox txtb_filSemester; // autowired
    protected Button btnSearchProdiExtended;  // autowired
    protected Combobox boxPrint;

    // not wired vars
    private transient Tjadkulmaster tjadkulmaster; // overhanded per param
    private transient Listbox listBoxTjadkulmaster; // overhanded per param

    // Button controller for the CRUD buttons
    private transient final String btnCtroller_ClassPrefix = "button_TjadkulmasterDialog_";
    protected Button button_TjadkulmasterDialog_PrintTjadkulmaster; // autowire

    // ServiceDAOs / Domain Classes
    private transient JadkulService jadkulService;
    private transient ProdiService prodiService;
    private transient BrancheService brancheService;

    /**
     * default constructor.<br>
     */
    public JadkulmasterCetakCtrl() {
        super();
    }

    /**
     * Before binding the data and calling the dialog window we check, if the
     * zul-file is called with a parameter for a selected user object in a Map.
     *
     * @param event
     * @throws Exception
     */
    public void onCreate$tjadkulmasterCetakWindow(Event event) throws Exception {
        Tjadkulmaster anTjadkulmaster = getJadkulService().getNewTjadkulmaster();
        setTjadkulmaster(anTjadkulmaster);

        // set Field Properties
        doSetFieldProperties();
        doShowDialog(getTjadkulmaster());
    }

    /**
     * SetVisible for components by checking if there's a right for it.
     */
    private void doCheckRights() {
        final UserWorkspace workspace = getUserWorkspace();
        tjadkulmasterCetakWindow.setVisible(workspace.isAllowed("tjadkulmasterCetakWindow"));
        button_TjadkulmasterDialog_PrintTjadkulmaster.setVisible(workspace.isAllowed("button_TjadkulmasterDialog_PrintTjadkulmaster"));
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++ Components events +++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * If we close the dialog window. <br>
     *
     * @param event
     * @throws Exception
     */
    public void onClose$tjadkulmasterCetakWindow(Event event) throws Exception {
        // logger.debug(event.toString());

        doClose();

    }

    /**
     * when the "help" button is clicked. <br>
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$btnHelp(Event event) throws InterruptedException {
        // logger.debug(event.toString());

        ZksampleMessageUtils.doShowNotImplementedMessage();
    }

    /**
     * when the "close" button is clicked. <br>
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$btnClose(Event event) throws InterruptedException {
        // logger.debug(event.toString());

        try {
            doClose();
        } catch (final Exception e) {
            // close anyway
            tjadkulmasterCetakWindow.onClose();
            // Messagebox.show(e.toString());
        }
    }

    /**
     * when 'print tjadkulmaster' is clicked.<br>
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_TjadkulmasterDialog_PrintTjadkulmaster(Event event) throws InterruptedException {
        // logger.debug(event.toString());
        doPrintTjadkulmasterReport(event);
    }

    private void doPrintTjadkulmasterReport(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        getTjadkulmaster().setCterm(txtb_filTerm.getValue());
        getTjadkulmaster().setCthajar(txtb_filTahunajaran.getValue());
        getTjadkulmaster().setCsmt(txtb_filSemester.getValue());
        new JadkulmasterSimpleDJReport(win, getTjadkulmaster(), (String) boxPrint.getSelectedItem().getValue());
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++ GUI operations +++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Closes the dialog window. <br>
     * <br>
     * Before closing we check if there are unsaved changes in <br>
     * the components and ask the user if saving the modifications. <br>
     */
    private void doClose() throws Exception {
        tjadkulmasterCetakWindow.onClose();
    }

    /**
     * Opens the Dialog window modal.
     * <p/>
     * It checks if the dialog opens with a new or existing object and set the
     * readOnly mode accordingly.
     *
     * @param anTjadkulmaster
     * @throws InterruptedException
     */
    public void doShowDialog(Tjadkulmaster anTjadkulmaster) throws InterruptedException {

        // if anTjadkulmaster == null then we opened the Dialog without
        // args for a given entity, so we get a new Obj().
        if (anTjadkulmaster == null) {
            /** !!! DO NOT BREAK THE TIERS !!! */
            // We don't create a new DomainObject() in the frontend.
            // We GET it from the backend.
            anTjadkulmaster = getJadkulService().getNewTjadkulmaster();
        }

        // fill the components with the data
        if (anTjadkulmaster.getMprodi() != null) {
            txtb_filProdi.setValue(anTjadkulmaster.getMprodi().getCnmprogst());
            txtb_code.setValue(anTjadkulmaster.getMprodi().getCkdprogst());
        }
        txtb_filTerm.setValue(anTjadkulmaster.getCterm());
        txtb_filTahunajaran.setValue(anTjadkulmaster.getCthajar());
        txtb_filSemester.setValue(anTjadkulmaster.getCsmt());
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++ helpers ++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Set the properties of the fields, like maxLength.<br>
     */
    private void doSetFieldProperties() {
        txtb_filProdi.setValue("");
        txtb_code.setValue("");
        txtb_filSemester.setValue("");
        txtb_filTahunajaran.setValue("");
        txtb_filTerm.setValue("");
    }

    /*--------------------------- PRODI LOV ---------------------------*/
    public void onClick$btnSearchProdiExtended(Event event) {
        doSearchProdiExtended(event);
    }

    private void doSearchProdiExtended(Event event) {
        Mprodi prodi = ProdiExtendedSearchListBox.show(tjadkulmasterCetakWindow);

        if (prodi != null) {
            txtb_filProdi.setValue(prodi.getCnmprogst());
            txtb_code.setValue(prodi.getCnmprogst());
            Tjadkulmaster tjadkulmaster = getTjadkulmaster();
            tjadkulmaster.setMprodi(prodi);
            setTjadkulmaster(tjadkulmaster);
        }
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    // ++++++++++++++++++ getter / setter +++++++++++++++++++//
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

    public BrancheService getBrancheService() {
        return this.brancheService;
    }

    public void setBrancheService(BrancheService brancheService) {
        this.brancheService = brancheService;
    }

    public Tjadkulmaster getTjadkulmaster() {
        return this.tjadkulmaster;
    }

    public void setTjadkulmaster(Tjadkulmaster tjadkulmaster) {
        this.tjadkulmaster = tjadkulmaster;
    }

    public void setPlwTjadkuldetils(PagedListWrapper<Tjadkuldetil> plwTjadkuldetils) {
        this.plwTjadkuldetils = plwTjadkuldetils;
    }

    public PagedListWrapper<Tjadkuldetil> getPlwTjadkuldetils() {
        return this.plwTjadkuldetils;
    }

    public void setPlwCustomers(PagedListWrapper<Customer> plwCustomers) {
        this.plwCustomers = plwCustomers;
    }

    public PagedListWrapper<Customer> getPlwCustomers() {
        return this.plwCustomers;
    }

    public JadkulService getJadkulService() {
        return jadkulService;
    }

    public void setJadkulService(JadkulService jadkulService) {
        this.jadkulService = jadkulService;
    }

    public ProdiService getProdiService() {
        return prodiService;
    }

    public void setProdiService(ProdiService prodiService) {
        this.prodiService = prodiService;
    }

}
