package id.ac.idu.webui.irs.kartustudi;

import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.ProdiService;
import id.ac.idu.backend.model.Customer;
import id.ac.idu.backend.model.Tjadkuldetil;
import id.ac.idu.backend.model.Tjadkulmaster;
import id.ac.idu.irs.service.JadkulService;
import id.ac.idu.webui.irs.jadwalkuliah.JadkulmasterDialogCtrl;
import id.ac.idu.webui.irs.report.JadkulmasterSimpleDJReport;
import id.ac.idu.webui.irs.report.KartustudiSimpleDJReport;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.ZksampleMessageUtils;
import id.ac.idu.webui.util.pagging.PagedListWrapper;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 06/04/12
 * Time: 16:43
 * To change this template use File | Settings | File Templates.
 */
public class KartuStudiCetakCtrl extends GFCBaseCtrl implements Serializable {

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
    protected Window kartuStudiCetakWindow; // autowired
    protected Textbox txtb_filTerm; // autowired
    protected Textbox txtb_filNimMulai; // autowired
    protected Textbox txtb_filNimAkhir; // autowired

    // not wired vars
    private transient Tjadkulmaster tjadkulmaster; // overhanded per param
    private transient Listbox listBoxTjadkulmaster; // overhanded per param

    // Button controller for the CRUD buttons
    private transient final String btnCtroller_ClassPrefix = "button_TjadkulmasterDialog_";
    protected Button button_TjadkulmasterDialog_PrintTjadkulmaster; // autowire

    // ServiceDAOs / Domain Classes
    private transient JadkulService jadkulService;
    private transient ProdiService prodiService;

    /**
     * default constructor.<br>
     */
    public KartuStudiCetakCtrl() {
        super();
    }

    /**
     * Before binding the data and calling the dialog window we check, if the
     * zul-file is called with a parameter for a selected user object in a Map.
     *
     * @param event
     * @throws Exception
     */
    public void onCreate$tjadkulmasterDialogWindow(Event event) throws Exception {
        Tjadkulmaster anTjadkulmaster = getJadkulService().getNewTjadkulmaster();
        setTjadkulmaster(anTjadkulmaster);

        // set Field Properties
        doSetFieldProperties();
        doShowDialog(getTjadkulmaster());
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
    public void onClose$tjadkulmasterDialogWindow(Event event) throws Exception {
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
            kartuStudiCetakWindow.onClose();
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
        //getTjadkulmaster().setCterm(txtb_filTerm.getValue());
        new KartustudiSimpleDJReport(win, getTjadkulmaster());
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
        kartuStudiCetakWindow.onClose();
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
        txtb_filTerm.setValue(anTjadkulmaster.getCterm());
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++ helpers ++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Set the properties of the fields, like maxLength.<br>
     */
    private void doSetFieldProperties() {
        txtb_filNimMulai.setValue("");
        txtb_filNimAkhir.setValue("");
        txtb_filTerm.setValue("");
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    // ++++++++++++++++++ getter / setter +++++++++++++++++++//
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

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
