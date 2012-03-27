package id.ac.idu.webui.irs.jadwalkuliah;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.PegawaiService;
import id.ac.idu.administrasi.service.ProdiService;
import id.ac.idu.administrasi.service.SekolahService;
import id.ac.idu.backend.model.*;
import id.ac.idu.backend.service.BrancheService;
import id.ac.idu.backend.service.CustomerService;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.irs.service.JadkulService;
import id.ac.idu.mankurikulum.service.MatakuliahService;
import id.ac.idu.webui.util.ButtonStatusCtrl;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.MultiLineMessageBox;
import id.ac.idu.webui.util.ZksampleMessageUtils;
import id.ac.idu.webui.util.pagging.PagedListWrapper;
import id.ac.idu.webui.util.searchdialogs.MatakuliahExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.PegawaiExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.ProdiExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.SekolahExtendedSearchListBox;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 11/03/12
 * Time: 19:03
 * To change this template use File | Settings | File Templates.
 */
public class JadkulmasterDialogCtrl extends GFCBaseCtrl implements Serializable {

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
    protected Window tjadkulmasterDialogWindow; // autowired
    protected Textbox txtb_filSekolah; // autowired
    protected Textbox txtb_filProdi; // autowired
    protected Textbox txtb_filMatakuliah; // autowired
    protected Textbox txtb_filPegawai1; // autowired
    protected Textbox txtb_filPegawai2; // autowired
    protected Textbox txtb_filTerm; // autowired
    protected Textbox txtb_filKelompok; // autowired
    protected Textbox txtb_filTahunajaran; // autowired
    protected Textbox txtb_filSemester; // autowired
    protected Intbox txtb_filSks; // autowired
    protected Textbox txtb_filKeterangan; // autowired
    protected Textbox txtb_filLintasprodi; // autowired

    protected Paging paging_ListBoxTjadkulmasterTjadkuldetils; // autowired
    protected Listbox listBoxTjadkulmasterTjadkuldetils; // autowired

    protected Listheader listheader_TjadkulmasterPosList2_Tjadkulmasterpos_No; // autowired
    protected Listheader listheader_TjadkulmasterPosList2_Shorttext; // autowired
    protected Listheader listheader_TjadkulmasterPosList2_Count; // autowired
    protected Listheader listheader_TjadkulmasterPosList2_SinglePrice; // autowired
    protected Listheader listheader_TjadkulmasterPosList2_WholePrice; // autowired

    protected Button btnSearchSekolahExtended;  // autowired
    protected Button btnSearchProdiExtended;  // autowired
    protected Button btnSearchMatakuliahExtended;  // autowired
    protected Button btnSearchPegawai1Extended; // autowired
    protected Button btnSearchPegawai2Extended; // autowired

    // search components
    // bandbox for searchCustomer
    protected Bandbox bandbox_TjadkulmasterDialog_CustomerSearch; // autowired
    protected Textbox tb_Tjadkulmasters_SearchCustNo; // autowired
    protected Textbox tb_Tjadkulmasters_CustSearchMatchcode; // autowired
    protected Textbox tb_Tjadkulmasters_SearchCustName1; // autowired
    protected Textbox tb_Tjadkulmasters_SearchCustCity; // autowired
    protected Paging paging_TjadkulmasterDialog_CustomerSearchList; // autowired
    protected Listbox listBoxCustomerSearch; // autowired
    protected Listheader listheader_CustNo_2; // autowired
    protected Listheader listheader_CustMatchcode_2; // autowired
    protected Listheader listheader_CustName1_2; // autowired
    protected Listheader listheader_CustCity_2; // autowired

    // not wired vars
    private transient Tjadkulmaster tjadkulmaster; // overhanded per param
    private transient Listbox listBoxTjadkulmaster; // overhanded per param
    private transient JadkulmasterListCtrl jadkulmasterListCtrl; // overhanded per param

    // old value vars for edit mode. that we can check if something
    // on the values are edited since the last init.
    private transient String oldVar_txtb_filSekolah; // autowired
    private transient String oldVar_txtb_filProdi; // autowired
    private transient String oldVar_txtb_filMatakuliah; // autowired
    private transient String oldVar_txtb_filPegawai1; // autowired
    private transient String oldVar_txtb_filPegawai2; // autowired
    private transient String oldVar_txtb_filTerm; // autowired
    private transient String oldVar_txtb_filKelompok; // autowired
    private transient String oldVar_txtb_filTahunajaran; // autowired
    private transient String oldVar_txtb_filSemester; // autowired
    private transient int oldVar_txtb_filSks; // autowired
    private transient String oldVar_txtb_filKeterangan; // autowired
    private transient String oldVar_txtb_filLintasprodi; // autowired

    private transient boolean validationOn;

    // Button controller for the CRUD buttons
    private transient final String btnCtroller_ClassPrefix = "button_TjadkulmasterDialog_";
    private transient ButtonStatusCtrl btnCtrl;
    protected Button btnNew; // autowire
    protected Button btnEdit; // autowire
    protected Button btnDelete; // autowire
    protected Button btnSave; // autowire
    protected Button btnCancel; // autowire
    protected Button btnClose; // autowire

    protected Button btnHelp; // autowire
    protected Button button_TjadkulmasterDialog_PrintTjadkulmaster; // autowire
    protected Button button_TjadkulmasterDialog_NewTjadkuldetil; // autowire

    private int pageSizeTjadkuldetil;
    private int pageSizeSearchCustomer;

    // ServiceDAOs / Domain Classes
    private transient Customer customer;
    private transient JadkulService jadkulService;
    private transient PegawaiService pegawaiService;
    private transient SekolahService sekolahService;
    private transient ProdiService prodiService;
    private transient MatakuliahService matakuliahService;
    private transient CustomerService customerService;
    private transient BrancheService brancheService;

    /**
     * default constructor.<br>
     */
    public JadkulmasterDialogCtrl() {
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
        setPageSizeTjadkuldetil(10);
        setPageSizeSearchCustomer(20);

        /* set components visible dependent of the users rights */
        //doCheckRights();

        // create the Button Controller. Disable not used buttons during working
        btnCtrl = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel, btnClose);

        // get the params map that are overhanded by creation.
        Map<String, Object> args = getCreationArgsMap(event);

        if (args.containsKey("tjadkulmaster")) {
            System.out.println("if (args.containsKey(tjadkulmaster))");
            Tjadkulmaster anTjadkulmaster = (Tjadkulmaster) args.get("tjadkulmaster");
            setTjadkulmaster(anTjadkulmaster);
            // we must addionally check if there is NO customer object in
            // the tjadkulmaster, so its new.
//            if (anTjadkulmaster.getCustomer() != null) {
//                setCustomer(anTjadkulmaster.getCustomer());
//            }
        } else {
            System.out.println("else if (args.containsKey(tjadkulmaster))");
            setTjadkulmaster(null);
        }

        // we get the listBox Object for the offices list. So we have access
        // to it and can synchronize the shown data when we do insert, edit or
        // delete offices here.
        if (args.containsKey("listBoxTjadkulmaster")) {
            listBoxTjadkulmaster = (Listbox) args.get("listBoxTjadkulmaster");
        } else {
            listBoxTjadkulmaster = null;
        }

        if (args.containsKey("jadkulmasterListCtrl")) {
            jadkulmasterListCtrl = (JadkulmasterListCtrl) args.get("jadkulmasterListCtrl");
        } else {
            jadkulmasterListCtrl = null;
        }

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
//        listheader_TjadkulmasterPosList2_Tjadkulmasterpos_No.setSortAscending(new FieldComparator("aupId", true));
//        listheader_TjadkulmasterPosList2_Tjadkulmasterpos_No.setSortDescending(new FieldComparator("aupId", false));
//        listheader_TjadkulmasterPosList2_Shorttext.setSortAscending(new FieldComparator("article.artKurzbezeichnung", true));
//        listheader_TjadkulmasterPosList2_Shorttext.setSortDescending(new FieldComparator("article.artKurzbezeichnung", false));
//        listheader_TjadkulmasterPosList2_Count.setSortAscending(new FieldComparator("aupMenge", true));
//        listheader_TjadkulmasterPosList2_Count.setSortDescending(new FieldComparator("aupMenge", false));
//        listheader_TjadkulmasterPosList2_SinglePrice.setSortAscending(new FieldComparator("aupEinzelwert", true));
//        listheader_TjadkulmasterPosList2_SinglePrice.setSortDescending(new FieldComparator("aupEinzelwert", false));
//        listheader_TjadkulmasterPosList2_WholePrice.setSortAscending(new FieldComparator("aupGesamtwert", true));
//        listheader_TjadkulmasterPosList2_WholePrice.setSortDescending(new FieldComparator("aupGesamtwert", false));

        paging_ListBoxTjadkulmasterTjadkuldetils.setPageSize(pageSizeTjadkuldetil);
        paging_ListBoxTjadkulmasterTjadkuldetils.setDetailed(true);

        // Set the ListModel for the tjadkuldetils.
        if (getTjadkulmaster() != null) {
            System.out.println("if (getTjadkulmaster() != null)");
            if (!getTjadkulmaster().isNew()) {
                System.out.println("if (!getTjadkulmaster().isNew())");
                HibernateSearchObject<Tjadkuldetil> soTjadkuldetil = new HibernateSearchObject<Tjadkuldetil>(Tjadkuldetil.class, pageSizeTjadkuldetil);
                soTjadkuldetil.addFilter(new Filter("tjadkulmaster", getTjadkulmaster(), Filter.OP_EQUAL));
                // deeper loading of the relation to prevent the lazy
                // loading problem.
                //soTjadkuldetil.addFetch("article");

                // Set the ListModel.
                getPlwTjadkuldetils().init(soTjadkuldetil, listBoxTjadkulmasterTjadkuldetils, paging_ListBoxTjadkulmasterTjadkuldetils);

            } else {
                System.out.println("else if (!getTjadkulmaster().isNew())");
            }
        } else {
            System.out.println("else if (getTjadkulmaster() != null)");
        }
        listBoxTjadkulmasterTjadkuldetils.setItemRenderer(new JadkuldetilListItemRenderer());

        // set Field Properties
        doSetFieldProperties();

        doShowDialog(getTjadkulmaster());

    }

    /**
     * SetVisible for components by checking if there's a right for it.
     */
    private void doCheckRights() {

        final UserWorkspace workspace = getUserWorkspace();

        tjadkulmasterDialogWindow.setVisible(workspace.isAllowed("tjadkulmasterDialogWindow"));

        btnHelp.setVisible(workspace.isAllowed("button_TjadkulmasterDialog_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_TjadkulmasterDialog_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_TjadkulmasterDialog_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_TjadkulmasterDialog_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_TjadkulmasterDialog_btnSave"));
        btnClose.setVisible(workspace.isAllowed("button_TjadkulmasterDialog_btnClose"));

        button_TjadkulmasterDialog_PrintTjadkulmaster.setVisible(workspace.isAllowed("button_TjadkulmasterDialog_PrintTjadkulmaster"));
        button_TjadkulmasterDialog_NewTjadkuldetil.setVisible(workspace.isAllowed("button_TjadkulmasterDialog_NewTjadkuldetil"));
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
     * when the "save" button is clicked. <br>
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$btnSave(Event event) throws InterruptedException {
        // logger.debug(event.toString());

        doSave();
    }

    /**
     * when the "edit" button is clicked. <br>
     *
     * @param event
     */
    public void onClick$btnEdit(Event event) {
        // logger.debug(event.toString());

        doEdit();
    }

    /**
     * when the "new" button is clicked. <br>
     *
     * @param event
     */
    public void onClick$btnNew(Event event) {
        // logger.debug(event.toString());

        doNew();
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
     * when the "delete" button is clicked. <br>
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$btnDelete(Event event) throws InterruptedException {
        // logger.debug(event.toString());

        doDelete();
    }

    /**
     * when the "cancel" button is clicked. <br>
     *
     * @param event
     */
    public void onClick$btnCancel(Event event) {
        // logger.debug(event.toString());

        doCancel();
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
            tjadkulmasterDialogWindow.onClose();
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

        // doPrintReport();
        doPrintTjadkulmasterReport(event);
    }

    private void doPrintTjadkulmasterReport(Event event) throws InterruptedException {

//        Tjadkulmaster anTjadkulmaster = getTjadkulmaster();
//
//        Window win = (Window) Path.getComponent("/outerIndexWindow");
//
//        try {
//            new TjadkulmasterDJReport(win, anTjadkulmaster);
//        } catch (final InterruptedException e) {
//            ZksampleMessageUtils.showErrorMessage(e.toString());
//        }

    }

    /**
     * when the "new tjadkulmaster position" button is clicked.
     *
     * @param event
     */
    public void onClick$button_TjadkulmasterDialog_NewTjadkuldetil(Event event) throws InterruptedException {
        // logger.debug(event.toString());

        // create a new tjadkuldetil object
        Tjadkuldetil anTjadkuldetil = getJadkulService().getNewTjadkuldetil();

        /*
           * We can call our Dialog zul-file with parameters. So we can call them
           * with a object of the selected item. For handed over these parameter
           * only a Map is accepted. So we put the object in a HashMap.
           */
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("jadkulmasterListCtrl", jadkulmasterListCtrl);
        map.put("tjadkulmaster", getTjadkulmaster());
        map.put("tjadkuldetil", anTjadkuldetil);
        /*
           * we can additionally handed over the listBox, so we have in the dialog
           * access to the listbox Listmodel. This is fine for syncronizing the
           * data in the customerListbox from the dialog when we do a delete, edit
           * or insert a customer.
           */
        map.put("listBoxTjadkulmasterTjadkuldetils", listBoxTjadkulmasterTjadkuldetils);
        map.put("jadkulmasterDialogCtrl", this);

        // call the zul-file with the parameters packed in a map
        try {
            Executions.createComponents("/WEB-INF/pages/irs/jadwalkuliah/jadkuldetilDialog.zul", null, map);
        } catch (final Exception e) {
            logger.error("onOpenWindow:: error opening window / " + e.getMessage());

            // Show a error box
            String msg = e.getMessage();
            String title = Labels.getLabel("message.Error");

            MultiLineMessageBox.doSetTemplate();
            MultiLineMessageBox.show(msg, title, MultiLineMessageBox.OK, "ERROR", true);

        }
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++ GUI operations +++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Cancel the actual operation. <br>
     * <br>
     * Resets to the original status.<br>
     */
    private void doCancel() {
        doResetInitValues();
        btnCtrl.setInitEdit();
    }

    /**
     * Closes the dialog window. <br>
     * <br>
     * Before closing we check if there are unsaved changes in <br>
     * the components and ask the user if saving the modifications. <br>
     */
    private void doClose() throws Exception {

        if (isDataChanged()) {

            // Show a confirm box
            String msg = Labels.getLabel("message_Data_Modified_Save_Data_YesNo");
            String title = Labels.getLabel("message.Information");

            MultiLineMessageBox.doSetTemplate();
            if (MultiLineMessageBox.show(msg, title, MultiLineMessageBox.YES | MultiLineMessageBox.NO, MultiLineMessageBox.QUESTION, true, new EventListener() {
                @Override
                public void onEvent(Event evt) {
                    switch (((Integer) evt.getData()).intValue()) {
                        case MultiLineMessageBox.YES:
                            try {
                                doSave();
                            } catch (final InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        case MultiLineMessageBox.NO:
                            break; //
                    }
                }
            }

            ) == MultiLineMessageBox.YES) {
            }
        }

        tjadkulmasterDialogWindow.onClose();
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
            System.out.println("anTjadkulmaster == null");
            /** !!! DO NOT BREAK THE TIERS !!! */
            // We don't create a new DomainObject() in the frontend.
            // We GET it from the backend.
            anTjadkulmaster = getJadkulService().getNewTjadkulmaster();
        } else {
            System.out.println("anTjadkulmaster != null");
        }

        // set Readonly mode accordingly if the object is new or not.
        if (anTjadkulmaster.isNew()) {
            btnCtrl.setInitNew();
            doEdit();
        } else {
            btnCtrl.setInitEdit();
            doReadOnly();

            try {
                // fill the components with the data
                if (anTjadkulmaster.getMsekolah() != null) {
                    txtb_filSekolah.setValue(anTjadkulmaster.getMsekolah().getCnamaSekolah());
                }
                if (anTjadkulmaster.getMprodi() != null) {
                    txtb_filProdi.setValue(anTjadkulmaster.getMprodi().getCnmprogst());
                }
                if (anTjadkulmaster.getMtbmtkl() != null) {
                    txtb_filMatakuliah.setValue(anTjadkulmaster.getMtbmtkl().getCnamamk());
                }
                if (anTjadkulmaster.getMpegawai1() != null) {
                    txtb_filPegawai1.setValue(anTjadkulmaster.getMpegawai1().getCnama());
                }
                if (anTjadkulmaster.getMpegawai2() != null) {
                    txtb_filPegawai2.setValue(anTjadkulmaster.getMpegawai2().getCnama());
                }

                txtb_filTerm.setValue(anTjadkulmaster.getCterm());
                txtb_filKelompok.setValue(anTjadkulmaster.getCkelompok());
                txtb_filTahunajaran.setValue(anTjadkulmaster.getCthajar());
                txtb_filSemester.setValue(anTjadkulmaster.getCsmt());
                txtb_filSks.setValue(anTjadkulmaster.getNsks());
                txtb_filKeterangan.setValue(anTjadkulmaster.getCket());
                txtb_filLintasprodi.setValue(anTjadkulmaster.getClintasprodi());

            } catch (final Exception e) {
                Messagebox.show(e.toString());
            }
        }
        doStoreInitValues();
        tjadkulmasterDialogWindow.doModal(); // open the dialog in modal mode

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++ helpers ++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Set the properties of the fields, like maxLength.<br>
     */
    private void doSetFieldProperties() {
    }

    /**
     * Stores the init values in mem vars. <br>
     */
    private void doStoreInitValues() {
//        oldVar_txtb_filSekolah = txtb_filSekolah.getValue();
//        oldVar_txtb_filProdi = txtb_filProdi.getValue();
//        oldVar_txtb_filMatakuliah = txtb_filMatakuliah.getValue();
//        oldVar_txtb_filPegawai1 = txtb_filPegawai1.getValue();
//        oldVar_txtb_filPegawai2 = txtb_filPegawai2.getValue();
//        oldVar_txtb_filTerm = txtb_filTerm.getValue();
//        oldVar_txtb_filKelompok = txtb_filKelompok.getValue();
//        oldVar_txtb_filTahunajaran = txtb_filTahunajaran.getValue();
//        oldVar_txtb_filSemester = txtb_filSemester.getValue();
//        oldVar_txtb_filSks = txtb_filSks.getValue();
//        oldVar_txtb_filKeterangan = txtb_filKeterangan.getValue();
//        oldVar_txtb_filLintasprodi = txtb_filLintasprodi.getValue();
    }

    /**
     * Resets the init values from mem vars. <br>
     */
    private void doResetInitValues() {
        txtb_filSekolah.setValue(oldVar_txtb_filSekolah);
        txtb_filProdi.setValue(oldVar_txtb_filProdi);
        txtb_filMatakuliah.setValue(oldVar_txtb_filMatakuliah);
        txtb_filPegawai1.setValue(oldVar_txtb_filPegawai1);
        txtb_filPegawai2.setValue(oldVar_txtb_filPegawai2);
        txtb_filTerm.setValue(oldVar_txtb_filTerm);
        txtb_filKelompok.setValue(oldVar_txtb_filKelompok);
        txtb_filTahunajaran.setValue(oldVar_txtb_filTahunajaran);
        txtb_filSemester.setValue(oldVar_txtb_filSemester);
        txtb_filSks.setValue(oldVar_txtb_filSks);
        txtb_filKeterangan.setValue(oldVar_txtb_filKeterangan);
        txtb_filLintasprodi.setValue(oldVar_txtb_filLintasprodi);
    }

    /**
     * Checks, if data are changed since the last call of <br>
     * doStoreInitData() . <br>
     *
     * @return true, if data are changed, otherwise false
     */
    private boolean isDataChanged() {
        boolean changed = false;

        if (oldVar_txtb_filSekolah != txtb_filSekolah.getValue()) {
            changed = true;
        }
        if (oldVar_txtb_filProdi != txtb_filProdi.getValue()) {
            changed = true;
        }
        if (oldVar_txtb_filMatakuliah != txtb_filMatakuliah.getValue()) {
            changed = true;
        }
        if (oldVar_txtb_filPegawai1 != txtb_filPegawai1.getValue()) {
            changed = true;
        }
        if (oldVar_txtb_filPegawai2 != txtb_filPegawai2.getValue()) {
            changed = true;
        }
        if (oldVar_txtb_filTerm != txtb_filTerm.getValue()) {
            changed = true;
        }
        if (oldVar_txtb_filKelompok != txtb_filKelompok.getValue()) {
            changed = true;
        }
        if (oldVar_txtb_filTahunajaran != txtb_filTahunajaran.getValue()) {
            changed = true;
        }
        if (oldVar_txtb_filSemester != txtb_filSemester.getValue()) {
            changed = true;
        }
        if (oldVar_txtb_filSks != txtb_filSks.getValue()) {
            changed = true;
        }
        if (oldVar_txtb_filKeterangan != txtb_filKeterangan.getValue()) {
            changed = true;
        }
        if (oldVar_txtb_filLintasprodi != txtb_filLintasprodi.getValue()) {
            changed = true;
        }

        return changed;
    }

    /**
     * Sets the Validation by setting the accordingly constraints to the fields.
     */
    private void doSetValidation() {
        setValidationOn(true);
    }

    /**
     * Disables the Validation by setting empty constraints.
     */
    private void doRemoveValidation() {
        setValidationOn(false);
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // +++++++++++++++++++++++++ crud operations +++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Deletes a tjadkulmaster object from database.<br>
     *
     * @throws InterruptedException
     */
    private void doDelete() throws InterruptedException {

        final Tjadkulmaster tjadkulmaster = getTjadkulmaster();

        // Show a confirm box
        String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record");
        String title = Labels.getLabel("message.Deleting.Record");

        MultiLineMessageBox.doSetTemplate();
        if (MultiLineMessageBox.show(msg, title, MultiLineMessageBox.YES | MultiLineMessageBox.NO, MultiLineMessageBox.QUESTION, true, new EventListener() {
            @Override
            public void onEvent(Event evt) {
                switch (((Integer) evt.getData()).intValue()) {
                    case MultiLineMessageBox.YES:
                        try {
                            delete();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    case MultiLineMessageBox.NO:
                        break; //
                }
            }

            // delete from database
            private void delete() throws InterruptedException {

                try {
                    getJadkulService().delete(tjadkulmaster);
                } catch (DataAccessException e) {
                    ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                }

                // now synchronize the listBox in the parent zul-file
                ListModelList lml = (ListModelList) listBoxTjadkulmaster.getListModel();

                // Check if the branch object is new or updated
                // -1 means that the obj is not in the list, so it's
                // new.
                if (lml.indexOf(tjadkulmaster) == -1) {
                } else {
                    lml.remove(lml.indexOf(tjadkulmaster));
                }

                tjadkulmasterDialogWindow.onClose(); // close
                // the
                // dialog
            }
        }

        ) == MultiLineMessageBox.YES) {
        }

    }

    /**
     * Create a new tjadkulmaster object. <br>
     */
    private void doNew() {

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        setTjadkulmaster(getJadkulService().getNewTjadkulmaster());

        doClear(); // clear all commponents
        doEdit(); // edit mode

        btnCtrl.setBtnStatus_New();

        // remember the old vars
        doStoreInitValues();
    }

    /**
     * Set the components for edit mode. <br>
     */
    private void doEdit() {
//        txtb_filSekolah.setReadonly(false);
//        txtb_filProdi.setReadonly(false);
//        txtb_filMatakuliah.setReadonly(false);
//        txtb_filPegawai1.setReadonly(false);
//        txtb_filPegawai2.setReadonly(false);
        txtb_filTerm.setReadonly(false);
        txtb_filKelompok.setReadonly(false);
        txtb_filTahunajaran.setReadonly(false);
        txtb_filSemester.setReadonly(false);
        txtb_filSks.setReadonly(false);
        txtb_filKeterangan.setReadonly(false);
        txtb_filLintasprodi.setReadonly(false);

        btnSearchPegawai1Extended.setDisabled(false);
        btnSearchPegawai2Extended.setDisabled(false);
        btnSearchProdiExtended.setDisabled(false);
        btnSearchSekolahExtended.setDisabled(false);
        btnSearchMatakuliahExtended.setDisabled(false);
        btnCtrl.setBtnStatus_Edit();

        // remember the old vars
        doStoreInitValues();
    }

    /**
     * Set the components to ReadOnly. <br>
     */
    public void doReadOnly() {
//        txtb_filSekolah.setReadonly(true);
//        txtb_filProdi.setReadonly(true);
//        txtb_filMatakuliah.setReadonly(true);
//        txtb_filPegawai1.setReadonly(true);
//        txtb_filPegawai2.setReadonly(true);
        txtb_filTerm.setReadonly(true);
        txtb_filKelompok.setReadonly(true);
        txtb_filTahunajaran.setReadonly(true);
        txtb_filSemester.setReadonly(true);
        txtb_filSks.setReadonly(true);
        txtb_filKeterangan.setReadonly(true);
        txtb_filLintasprodi.setReadonly(true);

        btnSearchPegawai1Extended.setDisabled(true);
        btnSearchPegawai2Extended.setDisabled(true);
        btnSearchProdiExtended.setDisabled(true);
        btnSearchSekolahExtended.setDisabled(true);
        btnSearchMatakuliahExtended.setDisabled(true);
    }

    /**
     * Clears the components values. <br>
     */
    public void doClear() {
        // remove validation, if there are a save before
        doRemoveValidation();
        txtb_filSekolah.setValue("");
        txtb_filProdi.setValue("");
        txtb_filMatakuliah.setValue("");
        txtb_filPegawai1.setValue("");
        txtb_filPegawai2.setValue("");
        txtb_filTerm.setValue("");
        txtb_filKelompok.setValue("");
        txtb_filTahunajaran.setValue("");
        txtb_filSemester.setValue("");
        txtb_filSks.setValue(Integer.parseInt("0"));
        txtb_filKeterangan.setValue("");
        txtb_filLintasprodi.setValue("");

        // clear the listbox
        ListModelList lml = (ListModelList) this.listBoxTjadkulmasterTjadkuldetils.getModel();
        lml.clear();

        // doSetValidation();
    }

    /**
     * Saves the components to table. <br>
     *
     * @throws InterruptedException
     */
    public void doSave() throws InterruptedException {

        Tjadkulmaster anTjadkulmaster = getTjadkulmaster();
        anTjadkulmaster.setCterm(txtb_filTerm.getValue());
        anTjadkulmaster.setCkelompok(txtb_filKelompok.getValue());
        anTjadkulmaster.setCthajar(txtb_filTahunajaran.getValue());
        anTjadkulmaster.setCsmt(txtb_filSemester.getValue());
        anTjadkulmaster.setNsks(txtb_filSks.getValue());
        anTjadkulmaster.setCket(txtb_filKeterangan.getValue());
        anTjadkulmaster.setClintasprodi(txtb_filLintasprodi.getValue());
//        System.out.println("ID = " + anTjadkulmaster.getId());
//        System.out.println("TERM = " + anTjadkulmaster.getCterm());
//        System.out.println("PEGAWAI1 = " + anTjadkulmaster.getMpegawai1().getCnama());
        //Customer aCustomer = getCustomer();

        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // force validation, if on, than execute by component.getValue()
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        if (!isValidationOn()) {
            doSetValidation();
        }

//        kunNr.getValue();
//        kunName1.getValue();
        // bbox_Tjadkulmasters_CustomerSearch.getValue();

        // fill the tjadkulmaster object with the components data
//        anTjadkulmaster.setCustomer(aCustomer);
//        anTjadkulmaster.setAufNr(aufNr.getValue());
//        anTjadkulmaster.setAufBezeichnung(aufBezeichnung.getValue());

        // save it to database
        try {
            getJadkulService().saveOrUpdate(anTjadkulmaster);
        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetInitValues();

            doReadOnly();
            btnCtrl.setBtnStatus_Save();
            return;
        }

        // now synchronize the offices listBox
        ListModelList lml = (ListModelList) listBoxTjadkulmaster.getListModel();

        // Check if the object is new or updated
        // -1 means that the object is not in the list, so its new.
        if (lml.indexOf(anTjadkulmaster) == -1) {
            lml.add(anTjadkulmaster);
        } else {
            lml.set(lml.indexOf(anTjadkulmaster), anTjadkulmaster);
        }

        // bind the vars new for updating the components
        // officeCtrl.doBindNew();

        doReadOnly();
        btnCtrl.setBtnStatus_Save();
        // init the old values vars new
        doStoreInitValues();
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // +++++++++++++++ Tjadkulmaster Positions operations ++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * when an item in the tjadkulmaster positions list is double clicked. <br>
     *
     * @param event
     */
    public void onDoubleClickedTjadkuldetilItem(Event event) throws InterruptedException {
        // logger.debug(event.toString());

        // get the selected object
        Listitem item = listBoxTjadkulmasterTjadkuldetils.getSelectedItem();

        if (item != null) {
            // CAST AND STORE THE SELECTED OBJECT
            Tjadkuldetil anTjadkuldetil = (Tjadkuldetil) item.getValue();

            /*
                * We can call our Dialog zul-file with parameters. So we can call
                * them with a object of the selected item. For handed over these
                * parameter only a Map is accepted. So we put the object in a
                * HashMap.
                */
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("jadkulmasterListCtrl", jadkulmasterListCtrl);
            map.put("tjadkulmaster", getTjadkulmaster());
            map.put("tjadkuldetil", anTjadkuldetil);
            /*
                * we can additionally handed over the listBox, so we have in the
                * dialog access to the listbox Listmodel. This is fine for
                * syncronizing the data in the customerListbox from the dialog when
                * we do a delete, edit or insert a customer.
                */
            map.put("listBoxTjadkulmasterTjadkuldetils", listBoxTjadkulmasterTjadkuldetils);
            map.put("jadkulmasterDialogCtrl", this);

            // call the zul-file with the parameters packed in a map
            try {
                Executions.createComponents("/WEB-INF/pages/irs/jadwalkuliah/jadkuldetilDialog.zul", null, map);
            } catch (final Exception e) {
                logger.error("onOpenWindow:: error opening window / " + e.getMessage());

                // Show a error box
                String msg = e.getMessage();
                String title = Labels.getLabel("message.Error");
                MultiLineMessageBox.doSetTemplate();
                MultiLineMessageBox.show(msg, title, MultiLineMessageBox.OK, "ERROR", true);

            }
        }
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    // ++++++++++++++ bandbox search Customer +++++++++++++++//
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

    /**
     * when the "close" button is clicked.
     *
     * @param event
     */
    public void onClick$button_bbox_CustomerSearch_Close(Event event) {
        // logger.debug(event.toString());

        bandbox_TjadkulmasterDialog_CustomerSearch.close();
    }

    /**
     * when the "new" button is clicked.
     * <p/>
     * Calls the Customer dialog.
     *
     * @param event
     */
    public void onClick$button_bbox_CustomerSearch_NewCustomer(Event event) {
        // logger.debug(event.toString());

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        Customer aCustomer = getCustomerService().getNewCustomer();
        aCustomer.setOffice(getUserWorkspace().getOffice()); // init
        // customer.setBranche(Workspace.getBranche()); // init
        aCustomer.setBranche(getBrancheService().getBrancheById(new Integer(1033).longValue())); // init
        aCustomer.putKunMahnsperre(false); // init

        /*
           * We can call our Dialog zul-file with parameters. So we can call them
           * with a object of the selected item. For handed over these parameter
           * only a Map is accepted. So we put the object in a HashMap.
           */
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("customer", aCustomer);
        /*
           * we can additionally handed over the listBox, so we have in the dialog
           * access to the listbox Listmodel. This is fine for syncronizing the
           * data in the customerListbox from the dialog when we do a delete, edit
           * or insert a customer.
           */

        // call the zul-file with the parameters packed in a map
        Executions.createComponents("/WEB-INF/pages/customer/customerDialog.zul", null, map);
    }

    /**
     * when the "search/filter" button in the bandbox is clicked.
     *
     * @param event
     */
    public void onClick$button_bbox_CustomerSearch_Search(Event event) {
        // logger.debug(event.toString());

        doSearch();
    }

    public void onOpen$bandbox_TjadkulmasterDialog_CustomerSearch(Event event) throws Exception {
        // logger.debug(event.toString());

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Customer> soCustomer = new HibernateSearchObject<Customer>(Customer.class);
        soCustomer.addSort("kunName1", false);

        // set the paging params
        paging_TjadkulmasterDialog_CustomerSearchList.setPageSize(pageSizeSearchCustomer);
        paging_TjadkulmasterDialog_CustomerSearchList.setDetailed(true);

        // Set the ListModel.
        getPlwCustomers().init(soCustomer, listBoxCustomerSearch, paging_TjadkulmasterDialog_CustomerSearchList);
        // set the itemRenderer
        //listBoxCustomerSearch.setItemRenderer(new TjadkulmasterSearchCustomerListModelItemRenderer());
    }

    /**
     * Search/filter data for the filled out fields<br>
     * <br>
     * 1. Create a map with the count entries. <br>
     * 2. Store the propertynames and values to the map. <br>
     * 3. Call the ServiceDAO method with the map as parameter. <br>
     */
    private void doSearch() {

        // ++ create the searchObject and init sorting ++//
        HibernateSearchObject<Customer> soCustomer = new HibernateSearchObject<Customer>(Customer.class);

        if (StringUtils.isNotEmpty(tb_Tjadkulmasters_SearchCustNo.getValue())) {
            soCustomer.addFilter(new Filter("kunNr", tb_Tjadkulmasters_SearchCustNo.getValue(), Filter.OP_EQUAL));
        }
        if (StringUtils.isNotEmpty(tb_Tjadkulmasters_CustSearchMatchcode.getValue())) {
            soCustomer.addFilter(new Filter("kunMatchcode", "%" + tb_Tjadkulmasters_CustSearchMatchcode.getValue() + "%", Filter.OP_ILIKE));
        }
        if (StringUtils.isNotEmpty(tb_Tjadkulmasters_SearchCustName1.getValue())) {
            soCustomer.addFilter(new Filter("kunName1", "%" + tb_Tjadkulmasters_SearchCustName1.getValue() + "%", Filter.OP_ILIKE));
        }
        if (StringUtils.isNotEmpty(tb_Tjadkulmasters_SearchCustCity.getValue())) {
            soCustomer.addFilter(new Filter("kunOrt", "%" + tb_Tjadkulmasters_SearchCustCity.getValue() + "%", Filter.OP_ILIKE));
        }
        soCustomer.addSort("kunName1", false);

        // set the paging params
        paging_TjadkulmasterDialog_CustomerSearchList.setPageSize(pageSizeSearchCustomer);
        paging_TjadkulmasterDialog_CustomerSearchList.setDetailed(true);

        // Set the ListModel.
        getPlwCustomers().init(soCustomer, listBoxCustomerSearch, paging_TjadkulmasterDialog_CustomerSearchList);
        // set the itemRenderer
//        listBoxCustomerSearch.setItemRenderer(new TjadkulmasterSearchCustomerListModelItemRenderer());

    }

    /**
     * when doubleClick on a item in the customerSearch listbox.<br>
     * <br>
     * Select the customer and search all tjadkulmasters for him.
     *
     * @param event
     */
    public void onDoubleClickedCustomerItem(Event event) {
        // logger.debug(event.toString());

        // get the customer
        Listitem item = listBoxCustomerSearch.getSelectedItem();
        if (item != null) {

            Customer aCustomer = (Customer) item.getAttribute("data");
            setCustomer(aCustomer);

//            kunNr.setValue(getCustomer().getKunNr());
//            bandbox_TjadkulmasterDialog_CustomerSearch.setValue(getCustomer().getKunNr());
//            kunName1.setValue(getCustomer().getKunName1() + " " + getCustomer().getKunName2() + ", " + getCustomer().getKunOrt());
        }

        // close the bandbox
        bandbox_TjadkulmasterDialog_CustomerSearch.close();

    }

    /*--------------------------- SEKOLAH LOV ---------------------------*/
    public void onClick$btnSearchSekolahExtended(Event event) {
        doSearchSekolahExtended(event);
    }

    private void doSearchSekolahExtended(Event event) {
        Msekolah sekolah = SekolahExtendedSearchListBox.show(tjadkulmasterDialogWindow);

        if (sekolah != null) {
            txtb_filSekolah.setValue(sekolah.getCkdsekolah() + " - " + sekolah.getCnamaSekolah());
            Tjadkulmaster tjadkulmaster = getTjadkulmaster();
            tjadkulmaster.setMsekolah(sekolah);
            setTjadkulmaster(tjadkulmaster);
        }
    }

//    public void onChange$txtb_filSekolah() {
//        if (txtb_filSekolah.getValue() != null) {
//            if (NumberUtils.isNumber(txtb_filSekolah.getValue())) {
//                Msekolah sekolah = sekolahService.getSekolahById(Integer.parseInt(txtb_filSekolah.getValue()));
//
//                if (sekolah != null) {
//                    txtb_filSekolah.setValue(txtb_filSekolah.getValue() + " - " + sekolah.getCnamaSekolah());
//                    getTjadkulmaster().setMsekolah(sekolah);
//                } else {
//                    txtb_filSekolah.setValue("Data Tidak Ditemukan");
//                }
//            } else {
//                txtb_filSekolah.setValue("Input Data Salah");
//            }
//        }
//    }

    /*--------------------------- PRODI LOV ---------------------------*/
    public void onClick$btnSearchProdiExtended(Event event) {
        doSearchProdiExtended(event);
    }

    private void doSearchProdiExtended(Event event) {
        Mprodi prodi = ProdiExtendedSearchListBox.show(tjadkulmasterDialogWindow);

        if (prodi != null) {
            txtb_filProdi.setValue(prodi.getCkdprogst() + " - " + prodi.getCnmprogst());
            Tjadkulmaster tjadkulmaster = getTjadkulmaster();
            tjadkulmaster.setMprodi(prodi);
            setTjadkulmaster(tjadkulmaster);
        }
    }

//    public void onChange$txtb_filProdi() {
//        if (txtb_filProdi.getValue() != null) {
//            if (NumberUtils.isNumber(txtb_filProdi.getValue())) {
//                Mprodi prodi = prodiService.getProdiByID(Integer.parseInt(txtb_filProdi.getValue()));
//
//                if (prodi != null) {
//                    txtb_filProdi.setValue(txtb_filProdi.getValue() + " - " + prodi.getCnmprogst());
//                    getTjadkulmaster().setMprodi(prodi);
//                } else {
//                    txtb_filProdi.setValue("Data Tidak Ditemukan");
//                }
//            } else {
//                txtb_filProdi.setValue("Input Data Salah");
//            }
//        }
//    }

    /*--------------------------- MATAKULIAH LOV ---------------------------*/
    public void onClick$btnSearchMatakuliahExtended(Event event) {
        doSearchMatakuliahExtended(event);
    }

    private void doSearchMatakuliahExtended(Event event) {
        Mtbmtkl matakuliah = MatakuliahExtendedSearchListBox.show(tjadkulmasterDialogWindow);

        if (matakuliah != null) {
            txtb_filMatakuliah.setValue(matakuliah.getCkdmtk() + " - " + matakuliah.getCnamamk());
            Tjadkulmaster tjadkulmaster = getTjadkulmaster();
            tjadkulmaster.setMtbmtkl(matakuliah);
            setTjadkulmaster(tjadkulmaster);
        }
    }

//    public void onChange$txtb_filMatakuliah() {
//        if (txtb_filMatakuliah.getValue() != null) {
//            if (NumberUtils.isNumber(txtb_filMatakuliah.getValue())) {
//                Mtbmtkl matakuliah = matakuliahService.getMatakuliahById(Integer.parseInt(txtb_filMatakuliah.getValue()));
//
//                if (matakuliah != null) {
//                    txtb_filMatakuliah.setValue(txtb_filMatakuliah.getValue() + " - " + matakuliah.getCnamamk());
//                    getTjadkulmaster().setMtbmtkl(matakuliah);
//                } else {
//                    txtb_filMatakuliah.setValue("Data Tidak Ditemukan");
//                }
//            } else {
//                txtb_filMatakuliah.setValue("Input Data Salah");
//            }
//        }
//    }

    /*--------------------------- PEGAWAI1 LOV ---------------------------*/
    public void onClick$btnSearchPegawai1Extended(Event event) {
        doSearchPegawai1Extended(event);
    }

    private void doSearchPegawai1Extended(Event event) {
        Mpegawai pegawai = PegawaiExtendedSearchListBox.show(tjadkulmasterDialogWindow);

        if (pegawai != null) {
            txtb_filPegawai1.setValue(pegawai.getCnip() + " - " + pegawai.getCnama());
            Tjadkulmaster tjadkulmaster = getTjadkulmaster();
            tjadkulmaster.setMpegawai1(pegawai);
            setTjadkulmaster(tjadkulmaster);
        }
    }

//    public void onChange$txtb_filPegawai1() {
//        if (txtb_filPegawai1.getValue() != null) {
//            Mpegawai pegawai = pegawaiService.getPegawaiByNip(txtb_filPegawai1.getValue());
//
//            if (pegawai != null) {
//                txtb_filPegawai1.setValue(txtb_filPegawai1.getValue() + " - " + pegawai.getCnama());
//                getTjadkulmaster().setMpegawai1(pegawai);
//            } else {
//                txtb_filPegawai1.setValue("Data Tidak Ditemukan");
//            }
//        }
//    }

    /*--------------------------- PEGAWAI2 LOV ---------------------------*/
    public void onClick$btnSearchPegawai2Extended(Event event) {
        doSearchPegawai2Extended(event);
    }

    private void doSearchPegawai2Extended(Event event) {
        Mpegawai pegawai = PegawaiExtendedSearchListBox.show(tjadkulmasterDialogWindow);

        if (pegawai != null) {
            txtb_filPegawai2.setValue(pegawai.getCnip() + " - " + pegawai.getCnama());
            Tjadkulmaster tjadkulmaster = getTjadkulmaster();
            tjadkulmaster.setMpegawai2(pegawai);
            setTjadkulmaster(tjadkulmaster);
        }
    }

//    public void onChange$txtb_filPegawai2() {
//        if (txtb_filPegawai2.getValue() != null) {
//            Mpegawai pegawai = pegawaiService.getPegawaiByNip(txtb_filPegawai2.getValue());
//
//            if (pegawai != null) {
//                txtb_filPegawai2.setValue(txtb_filPegawai2.getValue() + " - " + pegawai.getCnama());
//                getTjadkulmaster().setMpegawai2(pegawai);
//            } else {
//                txtb_filPegawai2.setValue("Data Tidak Ditemukan");
//            }
//        }
//    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    // ++++++++++++++++++ getter / setter +++++++++++++++++++//
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

    public void setValidationOn(boolean validationOn) {
        this.validationOn = validationOn;
    }

    public boolean isValidationOn() {
        return this.validationOn;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public CustomerService getCustomerService() {
        return this.customerService;
    }

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

    public Listbox getListBoxTjadkulmasterTjadkuldetils() {
        return this.listBoxTjadkulmasterTjadkuldetils;
    }

    public void setListBoxTjadkulmasterTjadkuldetils(Listbox listBoxTjadkulmasterTjadkuldetils) {
        this.listBoxTjadkulmasterTjadkuldetils = listBoxTjadkulmasterTjadkuldetils;
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

    public int getPageSizeTjadkuldetil() {
        return this.pageSizeTjadkuldetil;
    }

    public void setPageSizeTjadkuldetil(int pageSizeTjadkuldetil) {
        this.pageSizeTjadkuldetil = pageSizeTjadkuldetil;
    }

    public int getPageSizeSearchCustomer() {
        return this.pageSizeSearchCustomer;
    }

    public void setPageSizeSearchCustomer(int pageSizeSearchCustomer) {
        this.pageSizeSearchCustomer = pageSizeSearchCustomer;
    }

    public JadkulService getJadkulService() {
        return jadkulService;
    }

    public void setJadkulService(JadkulService jadkulService) {
        this.jadkulService = jadkulService;
    }

    public PegawaiService getPegawaiService() {
        return pegawaiService;
    }

    public void setPegawaiService(PegawaiService pegawaiService) {
        this.pegawaiService = pegawaiService;
    }

    public SekolahService getSekolahService() {
        return sekolahService;
    }

    public void setSekolahService(SekolahService sekolahService) {
        this.sekolahService = sekolahService;
    }

    public ProdiService getProdiService() {
        return prodiService;
    }

    public void setProdiService(ProdiService prodiService) {
        this.prodiService = prodiService;
    }

    public MatakuliahService getMatakuliahService() {
        return matakuliahService;
    }

    public void setMatakuliahService(MatakuliahService matakuliahService) {
        this.matakuliahService = matakuliahService;
    }
}
