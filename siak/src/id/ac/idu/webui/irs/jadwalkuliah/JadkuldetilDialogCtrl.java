package id.ac.idu.webui.irs.jadwalkuliah;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.HariService;
import id.ac.idu.administrasi.service.MruangService;
import id.ac.idu.backend.model.*;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.irs.service.JadkulService;
import id.ac.idu.mankurikulum.service.SesiService;
import id.ac.idu.webui.util.ButtonStatusCtrl;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.MultiLineMessageBox;
import id.ac.idu.webui.util.ZksampleMessageUtils;
import id.ac.idu.webui.util.pagging.PagedListWrapper;
import id.ac.idu.webui.util.searchdialogs.HariExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.MruangExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.SesiExtendedSearchListBox;
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
 * Time: 19:04
 * To change this template use File | Settings | File Templates.
 */
public class JadkuldetilDialogCtrl extends GFCBaseCtrl implements Serializable {

    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(JadkuldetilDialogCtrl.class);

    private transient PagedListWrapper<Tjadkuldetil> plwTjadkuldetils;
    private transient PagedListWrapper<Article> plwArticles;

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window tjadkuldetilDialogWindow; // autowired

    // input area
    protected Textbox txtb_filHari; // autowired
    protected Textbox txtb_filSesi; // autowired
    protected Textbox txtb_filRuang; // autowired
    protected Textbox txtb_filLintasprodi; // autowired
    protected Intbox txtb_filJumlahsesi; // autowired
    protected Intbox txtb_filMaks; // autowired
    protected Intbox txtb_filIsi; // autowired
    protected Button btnSearchRuangExtended;
    protected Button btnSearchHariExtended;
    protected Button btnSearchSesiExtended;

    // overhanded vars from parent controller
    private transient Listbox listBoxTjadkulmasterTjadkuldetils; // overhanded
    private transient Tjadkulmaster tjadkulmaster; // overhanded
    private transient Tjadkuldetil tjadkuldetil; // overhanded
    private transient JadkulmasterDialogCtrl jadkulmasterDialogCtrl; // overhanded
    private transient JadkulmasterListCtrl jadkulmasterListCtrl; // overhanded

    // old value vars for edit mode. that we can check if something
    // on the values are edited since the last init.
    private transient String oldVar_txtb_filHari;
    private transient String oldVar_txtb_filSesi;
    private transient String oldVar_txtb_filRuang;
    private transient String oldVar_txtb_filLintasprodi;
    private transient int oldVar_txtb_filJumlahsesi;
    private transient int oldVar_txtb_filMaks;
    private transient int oldVar_txtb_filIsi;

    private transient boolean validationOn;

    // Button controller for the CRUD buttons
    private transient final String btnCtroller_RightPrefix = "button_TjadkuldetilDialog_";
    private transient ButtonStatusCtrl btnCtrl;
    protected Button btnNew; // autowire
    protected Button btnEdit; // autowire
    protected Button btnDelete; // autowire
    protected Button btnSave; // autowire
    protected Button btnCancel; // autowire
    protected Button btnClose; // autowire

    protected Button btnHelp; // autowire

    // ServiceDAOs / Domain Classes
    private transient JadkulService jadkulService;
    private transient HariService hariService;
    private transient MruangService mruangService;
    private transient SesiService sesiService;

    /**
     * default constructor.<br>
     */
    public JadkuldetilDialogCtrl() {
        super();
    }

    /**
     * Before binding the data and calling the dialog window we check, if the
     * zul-file is called with a parameter for a selected user object in a Map.
     *
     * @param event
     * @throws Exception
     */
    public void onCreate$tjadkuldetilDialogWindow(Event event) throws Exception {
        /* set components visible dependent of the users rights */
//        doCheckRights();

        // create the Button Controller. Disable not used buttons during working
        btnCtrl = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_RightPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel, btnClose);

        // get the params map that are overhanded by creation.
        Map<String, Object> args = getCreationArgsMap(event);

        if (args.containsKey("tjadkulmaster")) {
            tjadkulmaster = (Tjadkulmaster) args.get("tjadkulmaster");
        } else {
            setTjadkulmaster(null);
        }

        if (args.containsKey("tjadkuldetil")) {
            tjadkuldetil = (Tjadkuldetil) args.get("tjadkuldetil");
            setTjadkuldetil(tjadkuldetil);
            // we must addionally check if there is NO tjadkulmaster object in the
            // tjadkuldetil, so its new.
            if (tjadkuldetil.getTjadkulmaster() != null) {
                setTjadkulmaster(this.tjadkulmaster);
            }
        } else {
            setTjadkuldetil(null);
        }

        // we get the listBox Object for the offices list. So we have access
        // to it and can synchronize the shown data when we do insert, edit or
        // delete tjadkuldetils here.
        if (args.containsKey("listBoxTjadkulmasterTjadkuldetils")) {
            listBoxTjadkulmasterTjadkuldetils = (Listbox) args.get("listBoxTjadkulmasterTjadkuldetils");
        } else {
            listBoxTjadkulmasterTjadkuldetils = null;
        }

        if (args.containsKey("jadkulmasterDialogCtrl")) {
            jadkulmasterDialogCtrl = (JadkulmasterDialogCtrl) args.get("jadkulmasterDialogCtrl");
        } else {
            jadkulmasterDialogCtrl = null;
        }

        if (args.containsKey("jadkulmasterListCtrl")) {
            jadkulmasterListCtrl = (JadkulmasterListCtrl) args.get("jadkulmasterListCtrl");
        } else {
            jadkulmasterListCtrl = null;
        }

        doShowDialog(getTjadkuldetil());

    }

    /**
     * SetVisible for components by checking if there's a right for it.
     */
    private void doCheckRights() {

        final UserWorkspace workspace = getUserWorkspace();

        tjadkuldetilDialogWindow.setVisible(workspace.isAllowed("tjadkuldetilDialogWindow"));

        btnHelp.setVisible(workspace.isAllowed("button_TjadkuldetilDialog_btnHelp"));
        btnNew.setVisible(workspace.isAllowed("button_TjadkuldetilDialog_btnNew"));
        btnEdit.setVisible(workspace.isAllowed("button_TjadkuldetilDialog_btnEdit"));
        btnDelete.setVisible(workspace.isAllowed("button_TjadkuldetilDialog_btnDelete"));
        btnSave.setVisible(workspace.isAllowed("button_TjadkuldetilDialog_btnSave"));
        btnClose.setVisible(workspace.isAllowed("button_TjadkuldetilDialog_btnClose"));

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
    public void onClose$tjadkuldetilDialogWindow(Event event) throws Exception {
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
            tjadkuldetilDialogWindow.onClose();
            // Messagebox.show(e.toString());
        }
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

        tjadkuldetilDialogWindow.onClose();
    }

    /**
     * Cancel the actual operation. <br>
     * <br>
     * Resets to the original status.<br>
     */
    private void doCancel() {
        doResetInitValues();

//        txtb_filHari.setReadonly(true);
//        txtb_filSesi.setReadonly(true);
//        txtb_filRuang.setReadonly(true);
        txtb_filLintasprodi.setReadonly(true);
        txtb_filJumlahsesi.setReadonly(true);
        txtb_filMaks.setReadonly(true);
        txtb_filIsi.setReadonly(true);

        btnSearchRuangExtended.setDisabled(true);
        btnSearchHariExtended.setDisabled(true);
        btnSearchSesiExtended.setDisabled(true);
        btnCtrl.setInitEdit();
    }

    /**
     * Writes the bean data to the components.<br>
     *
     * @param anTjadkuldetil Tjadkuldetil
     */
    public void doWriteBeanToComponents(Tjadkuldetil anTjadkuldetil) {
        if (anTjadkuldetil != null) {
            if (anTjadkuldetil.getMhari() != null) {
                txtb_filHari.setValue(anTjadkuldetil.getMhari().getCnmhari());
            }
            if (anTjadkuldetil.getMsesikuliah() != null) {
                txtb_filSesi.setValue(anTjadkuldetil.getMsesikuliah().getCkdsesi());
            }
            if (anTjadkuldetil.getMruang() != null) {
                txtb_filRuang.setValue(anTjadkuldetil.getMruang().getCnmRuang());
            }
            if (StringUtils.isNotBlank(anTjadkuldetil.getClintasprodi())) {
                txtb_filLintasprodi.setValue(anTjadkuldetil.getClintasprodi());
            }
            if (anTjadkuldetil.getNjmlsesi() != null) {
                txtb_filJumlahsesi.setValue(anTjadkuldetil.getNjmlsesi());
            }
            if (anTjadkuldetil.getNmaks() != null) {
                txtb_filMaks.setValue(anTjadkuldetil.getNmaks());
            }
            if (anTjadkuldetil.getNisi() != null) {
                txtb_filIsi.setValue(anTjadkuldetil.getNisi());
            }
        }
    }

    /**
     * Writes the components values to the bean.<br>
     *
     * @param anTjadkuldetil
     */
    public void doWriteComponentsToBean(Tjadkuldetil anTjadkuldetil) {
        if (anTjadkuldetil != null) {
            if (getTjadkulmaster() != null) {
                anTjadkuldetil.setTjadkulmaster(getTjadkulmaster());
                if (getTjadkulmaster().getMsekolah() != null) {
                    anTjadkuldetil.setMsekolah(getTjadkulmaster().getMsekolah());
                }
                if (getTjadkulmaster().getMprodi() != null) {
                    anTjadkuldetil.setMprodi(getTjadkulmaster().getMprodi());
                }
                if (getTjadkulmaster().getMtbmtkl() != null) {
                    anTjadkuldetil.setMtbmtkl(getTjadkulmaster().getMtbmtkl());
                }
                if (getTjadkulmaster().getCterm() != null) {
                    anTjadkuldetil.setCterm(getTjadkulmaster().getCterm());
                }
                if (getTjadkulmaster().getCkelompok() != null) {
                    anTjadkuldetil.setCkelompok(getTjadkulmaster().getCkelompok());
                }
            }
            if (txtb_filLintasprodi.getValue() != null) {
                anTjadkuldetil.setClintasprodi(txtb_filLintasprodi.getValue());
            }
            if (txtb_filJumlahsesi.getValue() != null) {
                anTjadkuldetil.setNjmlsesi(txtb_filJumlahsesi.getValue());
            }
            if (txtb_filMaks.getValue() != null) {
                anTjadkuldetil.setNmaks(txtb_filMaks.getValue());
            }
            if (txtb_filIsi.getValue() != null) {
                anTjadkuldetil.setNisi(txtb_filIsi.getValue());
            }
        }
    }

    /**
     * Opens the Dialog window modal.
     * <p/>
     * It checks if the dialog opens with a new or existing object and set the
     * readOnly mode accordingly.
     *
     * @param anTjadkuldetil
     * @throws InterruptedException
     */
    public void doShowDialog(Tjadkuldetil anTjadkuldetil) throws InterruptedException {

        // if aBranche == null then we opened the Dialog without
        // args for a given entity, so we get a new Obj().
        if (anTjadkuldetil == null) {

            /** !!! DO NOT BREAK THE TIERS !!! */
            // We don't create a new DomainObject() in the frontend.
            // We GET it from the backend.
            anTjadkuldetil = getJadkulService().getNewTjadkuldetil();
        }

        try {

            if (anTjadkuldetil.getTjadkulmaster() != null) {
                // fill the components with the data
                doWriteBeanToComponents(anTjadkuldetil);

            }

            // set Readonly mode accordingly if the object is new or not.
            if (anTjadkuldetil.isNew()) {
                btnCtrl.setInitNew();
                doEdit();
            } else {
                btnCtrl.setInitEdit();
                doReadOnly();
            }

            // stores the inital data for comparing if they are changed
            // during user action.
            doStoreInitValues();

            tjadkuldetilDialogWindow.doModal(); // open the dialog in
            // modal
            // mode
        } catch (final Exception e) {
            Messagebox.show(e.toString());
        }
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // ++++++++++++++++++++++++++++++ helpers ++++++++++++++++++++++++++
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Stores the init values in mem vars. <br>
     */
    private void doStoreInitValues() {
        oldVar_txtb_filHari = txtb_filHari.getValue();
        oldVar_txtb_filSesi = txtb_filSesi.getValue();
        oldVar_txtb_filRuang = txtb_filRuang.getValue();
        oldVar_txtb_filLintasprodi = txtb_filLintasprodi.getValue();
//        oldVar_txtb_filJumlahsesi = txtb_filJumlahsesi.getValue();
//        oldVar_txtb_filMaks = txtb_filMaks.getValue();
//        oldVar_txtb_filIsi = txtb_filIsi.getValue();
    }

    /**
     * Resets the init values from mem vars. <br>
     */
    private void doResetInitValues() {
        txtb_filHari.setValue(oldVar_txtb_filHari);
        txtb_filSesi.setValue(oldVar_txtb_filSesi);
        txtb_filRuang.setValue(oldVar_txtb_filRuang);
        txtb_filLintasprodi.setValue(oldVar_txtb_filLintasprodi);
//        txtb_filJumlahsesi.setValue(oldVar_txtb_filJumlahsesi);
//        txtb_filMaks.setValue(oldVar_txtb_filMaks);
//        txtb_filIsi.setValue(oldVar_txtb_filIsi);
    }

    /**
     * Checks, if data are changed since the last call of <br>
     * doStoreInitData() . <br>
     *
     * @return true, if data are changed, otherwise false
     */
    private boolean isDataChanged() {
        boolean changed = false;

        if (oldVar_txtb_filHari != txtb_filHari.getValue()) {
            changed = true;
        }
        if (oldVar_txtb_filSesi != txtb_filSesi.getValue()) {
            changed = true;
        }

        if (oldVar_txtb_filRuang != txtb_filRuang.getValue()) {
            changed = true;
        }

        if (oldVar_txtb_filLintasprodi != txtb_filLintasprodi.getValue()) {
            changed = true;
        }

//        if (oldVar_txtb_filJumlahsesi != txtb_filJumlahsesi.getValue()) {
//            changed = true;
//        }
//
//        if (oldVar_txtb_filMaks != txtb_filMaks.getValue()) {
//            changed = true;
//        }
//
//        if (oldVar_txtb_filIsi != txtb_filIsi.getValue()) {
//            changed = true;
//        }
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
     * Deletes a tjadkuldetil object from database.<br>
     *
     * @throws InterruptedException
     */
    private void doDelete() throws InterruptedException {

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
                    getJadkulService().delete(tjadkuldetil);
                } catch (DataAccessException e) {
                    ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                }

                // now synchronize the listBox in the parent zul-file
                final ListModelList lml = (ListModelList) listBoxTjadkulmasterTjadkuldetils.getListModel();
                // Check if the tjadkuldetil object is new or updated
                // -1 means that the obj is not in the list, so it's
                // new.
                if (lml.indexOf(tjadkuldetil) == -1) {
                } else {
                    lml.remove(lml.indexOf(tjadkuldetil));
                }

                // +++++++ now synchronize the listBox in the parent
                // zul-file
                // +++ //
                Listbox listBoxTjadkulmasterArticle = jadkulmasterListCtrl.getListBoxTjadkulmasterArticle();
                // now synchronize the tjadkuldetil listBox
                ListModelList lml3 = (ListModelList) listBoxTjadkulmasterArticle.getListModel();
                // Check if the tjadkuldetil object is new or updated
                // -1 means that the obj is not in the list, so it's
                // new.
                if (lml3.indexOf(tjadkuldetil) == -1) {
                } else {
                    lml3.remove(lml3.indexOf(tjadkuldetil));
                }

                tjadkuldetilDialogWindow.onClose(); // close
                // the
                // dialog
            }
        }

        ) == MultiLineMessageBox.YES) {
        }

    }

    /**
     * Create a new tjadkuldetil object. <br>
     */
    private void doNew() {

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        Tjadkuldetil anTjadkuldetil = getJadkulService().getNewTjadkuldetil();
        setTjadkuldetil(anTjadkuldetil);
        anTjadkuldetil.setTjadkulmaster(tjadkulmaster);

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
//        txtb_filHari.setReadonly(false);
//        txtb_filSesi.setReadonly(false);
//        txtb_filRuang.setReadonly(false);
        txtb_filLintasprodi.setReadonly(false);
        txtb_filJumlahsesi.setReadonly(false);
        txtb_filMaks.setReadonly(false);
        txtb_filIsi.setReadonly(false);

        btnSearchRuangExtended.setDisabled(false);
        btnSearchHariExtended.setDisabled(false);
        btnSearchSesiExtended.setDisabled(false);

        btnCtrl.setBtnStatus_Edit();

        // remember the old vars
        doStoreInitValues();
    }

    /**
     * Set the components to ReadOnly. <br>
     */
    public void doReadOnly() {
//        txtb_filHari.setReadonly(true);
//        txtb_filSesi.setReadonly(true);
//        txtb_filRuang.setReadonly(true);
        txtb_filLintasprodi.setReadonly(true);
        txtb_filJumlahsesi.setReadonly(true);
        txtb_filMaks.setReadonly(true);
        txtb_filIsi.setReadonly(true);

        btnSearchRuangExtended.setDisabled(true);
        btnSearchHariExtended.setDisabled(true);
        btnSearchSesiExtended.setDisabled(true);
    }

    /**
     * Clears the components values. <br>
     */
    public void doClear() {

        // remove validation, if there are a save before
        doRemoveValidation();

        txtb_filHari.setValue("");
        txtb_filSesi.setValue("");
        txtb_filRuang.setValue("");
        txtb_filLintasprodi.setValue("");
//        txtb_filJumlahsesi.setValue(Integer.parseInt("0"));
//        txtb_filMaks.setValue(Integer.parseInt("0"));
//        txtb_filIsi.setValue(Integer.parseInt("0"));

    }

    /**
     * Saves the components to table. <br>
     *
     * @throws InterruptedException
     */
    public void doSave() throws InterruptedException {

        Tjadkuldetil anTjadkuldetil = getTjadkuldetil();

        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // force validation, if on, than execute by component.getValue()
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        if (!isValidationOn()) {
            doSetValidation();
        }

        // fill the objects with the components data
        doWriteComponentsToBean(anTjadkuldetil);

        // save it to database
        try {
            getJadkulService().saveOrUpdate(anTjadkuldetil);
        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetInitValues();

            doReadOnly();
            btnCtrl.setBtnStatus_Save();
            return;
        }

        /** Synchronize the listbox in the TjadkulmasterDialog */

        HibernateSearchObject<Tjadkuldetil> soTjadkuldetil = new HibernateSearchObject<Tjadkuldetil>(Tjadkuldetil.class, jadkulmasterDialogCtrl.getPageSizeTjadkuldetil());
        soTjadkuldetil.addFilter(new Filter("tjadkulmaster", getTjadkulmaster(), Filter.OP_EQUAL));
        // deeper loading of a relation to prevent the lazy
        // loading problem.
//        soTjadkuldetil.addFetch("article");

        // Set the ListModel.
        getPlwTjadkuldetils().init(soTjadkuldetil, jadkulmasterDialogCtrl.listBoxTjadkulmasterTjadkuldetils, jadkulmasterDialogCtrl.paging_ListBoxTjadkulmasterTjadkuldetils);

        /** Synchronize the TjadkulmasterList */
        // Listbox listBoxTjadkulmasterArticle = jadkulmasterListCtrl.getListBoxTjadkulmasterArticle();
        // listBoxTjadkulmasterArticle.setModel(jadkulmasterDialogCtrl.listBoxTjadkulmasterTjadkuldetils.getModel());
        jadkulmasterListCtrl.getListBoxTjadkulmasterArticle().setModel(jadkulmasterDialogCtrl.listBoxTjadkulmasterTjadkuldetils.getModel());

        // synchronize the TotalCount from the paging component
        jadkulmasterListCtrl.paging_TjadkulmasterArticleList.setTotalSize(jadkulmasterDialogCtrl.paging_ListBoxTjadkulmasterTjadkuldetils.getTotalSize());

        doReadOnly();
        btnCtrl.setBtnStatus_Save();
        // init the old values vars new
        doStoreInitValues();
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    // ++++++++++++++ bandbox search Tjadkulmaster +++++++++++++++//
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

    /**
     * when the "close" button is clicked.
     *
     * @param event
     */
    public void onClick$bt_Tjadkulmasters_TjadkulmasterSearchClose(Event event) {
        // logger.debug(event.toString());
    }

    /**
     * when the "new" button is clicked.
     * <p/>
     * Calls the Tjadkulmaster dialog.
     *
     * @param event
     */
    public void onClick$bt_Tjadkulmasters_TjadkulmasterNew(Event event) {
        // logger.debug(event.toString());

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        Tjadkulmaster aTjadkulmaster = getJadkulService().getNewTjadkulmaster();
        //aTjadkulmaster.setOffice(getUserWorkspace().getOffice()); // init
        // tjadkulmaster.setBranche(Workspace.getBranche()); // init
        // TODO get the init values from a setup configuration
        //aTjadkulmaster.setBranche(getBrancheService().getBrancheById(new Integer(1033).longValue())); // init
        //aTjadkulmaster.putKunMahnsperre(false); // init

        /*
           * We can call our Dialog zul-file with parameters. So we can call them
           * with a object of the selected item. For handed over these parameter
           * only a Map is accepted. So we put the object in a HashMap.
           */
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("tjadkulmaster", aTjadkulmaster);
        /*
           * we can additionally handed over the listBox, so we have in the dialog
           * access to the listbox Listmodel. This is fine for syncronizing the
           * data in the tjadkulmasterListbox from the dialog when we do a delete, edit
           * or insert a tjadkulmaster.
           */

        // call the zul-file with the parameters packed in a map
        Executions.createComponents("/WEB-INF/pages/irs/jadwalkuliah/jadkulmasterDialog.zul", null, map);
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

    /*--------------------------- HARI LOV ---------------------------*/
    public void onClick$btnSearchHariExtended(Event event) {
        doSearchHariExtended(event);
    }

    private void doSearchHariExtended(Event event) {
        Mhari hari = HariExtendedSearchListBox.show(tjadkuldetilDialogWindow);

        if (hari != null) {
            txtb_filHari.setValue(hari.getCkdhari() + " - " + hari.getCnmhari());
            getTjadkuldetil().setMhari(hari);
        }
    }

//    public void onChange$txtb_filHari() {
//        if (txtb_filHari.getValue() != null) {
//            if (NumberUtils.isNumber(txtb_filHari.getValue())) {
//                Mhari hari = hariService.getHariByID(Integer.parseInt(txtb_filHari.getValue()));
//
//                if (hari != null) {
//                    txtb_filHari.setValue(txtb_filHari.getValue() + " - " + hari.getCnmhari());
//                    getTjadkuldetil().setMhari(hari);
//                } else {
//                    txtb_filHari.setValue("Data Tidak Ditemukan");
//                }
//            } else {
//                txtb_filHari.setValue("Input Data Salah");
//            }
//        }
//    }

    /*--------------------------- SESI LOV ---------------------------*/
    public void onClick$btnSearchSesiExtended(Event event) {
        doSearchSesiExtended(event);
    }

    private void doSearchSesiExtended(Event event) {
        Msesikuliah sesi = SesiExtendedSearchListBox.show(tjadkuldetilDialogWindow);

        if (sesi != null) {
            txtb_filSesi.setValue(sesi.getCkdsesi() + " - " + sesi.getCjamawal());
            getTjadkuldetil().setMsesikuliah(sesi);
        }
    }

//    public void onChange$txtb_filSesi() {
//        if (txtb_filSesi.getValue() != null) {
//            if (NumberUtils.isNumber(txtb_filSesi.getValue())) {
//                Msesikuliah sesikuliah = sesiService.getSesiById(Integer.parseInt(txtb_filSesi.getValue()));
//
//                if (sesikuliah != null) {
//                    txtb_filSesi.setValue(txtb_filSesi.getValue() + " - " + sesikuliah.getCkdsesi());
//                    getTjadkuldetil().setMsesikuliah(sesikuliah);
//                } else {
//                    txtb_filSesi.setValue("Data Tidak Ditemukan");
//                }
//            } else {
//                txtb_filSesi.setValue("Input Data Salah");
//            }
//        }
//    }

    /*--------------------------- RUANG LOV ---------------------------*/
    public void onClick$btnSearchRuangExtended(Event event) {
        doSearchRuangExtended(event);
    }

    private void doSearchRuangExtended(Event event) {
        Mruang ruang = MruangExtendedSearchListBox.show(tjadkuldetilDialogWindow);

        if (ruang != null) {
            txtb_filRuang.setValue(ruang.getCkdruang() + " - " + ruang.getCnmRuang());
            getTjadkuldetil().setMruang(ruang);
        }
    }

//    public void onChange$txtb_filRuang() {
//        if (txtb_filRuang.getValue() != null) {
//            if (NumberUtils.isNumber(txtb_filRuang.getValue())) {
//                Mruang ruang = mruangService.getMruangByID(Integer.parseInt(txtb_filRuang.getValue()));
//
//                if (ruang != null) {
//                    txtb_filRuang.setValue(txtb_filRuang.getValue() + " - " + ruang.getCnmRuang());
//                    getTjadkuldetil().setMruang(ruang);
//                } else {
//                    txtb_filRuang.setValue("Data Tidak Ditemukan");
//                }
//            } else {
//                txtb_filRuang.setValue("Input Data Salah");
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

    public void setJadkulService(JadkulService jadkulService) {
        this.jadkulService = jadkulService;
    }

    public JadkulService getJadkulService() {
        return this.jadkulService;
    }

    public Tjadkulmaster getTjadkulmaster() {
        return tjadkulmaster;
    }

    public void setTjadkulmaster(Tjadkulmaster tjadkulmaster) {
        this.tjadkulmaster = tjadkulmaster;
    }

    public HariService getHariService() {
        return hariService;
    }

    public void setHariService(HariService hariService) {
        this.hariService = hariService;
    }

    public MruangService getMruangService() {
        return mruangService;
    }

    public void setMruangService(MruangService mruangService) {
        this.mruangService = mruangService;
    }

    public SesiService getSesiService() {
        return sesiService;
    }

    public void setSesiService(SesiService sesiService) {
        this.sesiService = sesiService;
    }

    public void setTjadkuldetil(Tjadkuldetil tjadkuldetil) {
        this.tjadkuldetil = tjadkuldetil;
    }

    public Tjadkuldetil getTjadkuldetil() {
        return this.tjadkuldetil;
    }

    public void setPlwTjadkuldetils(PagedListWrapper<Tjadkuldetil> plwTjadkuldetils) {
        this.plwTjadkuldetils = plwTjadkuldetils;
    }

    public PagedListWrapper<Tjadkuldetil> getPlwTjadkuldetils() {
        return this.plwTjadkuldetils;
    }

    public void setPlwArticles(PagedListWrapper<Article> plwArticles) {
        this.plwArticles = plwArticles;
    }

    public PagedListWrapper<Article> getPlwArticles() {
        return this.plwArticles;
    }


}
