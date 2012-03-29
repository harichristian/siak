/**
 * Copyright 2010 the original author or authors.
 *
 * This file is part of Zksample2. http://zksample2.sourceforge.net/
 *
 * Zksample2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Zksample2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Zksample2.  If not, see <http://www.gnu.org/licenses/gpl.html>.
 */
package id.ac.idu.webui.administrasi.feedbackinstansi;

import id.ac.idu.administrasi.service.TfeedbackinstansiService;
import id.ac.idu.backend.model.Malumni;
import id.ac.idu.backend.model.Tfeedbackinstansi;
import id.ac.idu.util.Codec;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.GFCListModelCtrl;
import id.ac.idu.webui.util.searchdialogs.MalumniExtendedSearchListBox;
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
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the /WEB-INF/pages/tfeedbackinstansi/tfeedbackinstansiList.zul
 * file.<br>
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 *
 * @author bbruhns
 * @author sgerth
 * @changes 05/15/2009: sge Migrating the list models for paging. <br>
 * 07/24/2009: sge changings for clustering.<br>
 * 10/12/2009: sge changings in the saving routine.<br>
 * 11/07/2009: bbr changed to extending from GFCBaseCtrl<br>
 * (GenericForwardComposer) for spring-managed creation.<br>
 * 07/04/2010: sge modified for zk5.x with complete Annotated
 * Databinding.<br>
 */
public class TfeedbackinstansiDetailCtrl extends GFCBaseCtrl implements Serializable {

    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(TfeedbackinstansiDetailCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowTfeedbackinstansiDetail; // autowired

    protected Borderlayout borderlayout_TfeedbackinstansiDetail; // autowired

    protected Textbox txtb_jenis; // autowired
    protected Textbox txtb_nminstansi; // autowired
    protected Textbox txtb_alamat; // autowired
    protected Textbox txtb_nmalumni; // autowired
    protected Textbox txtb_kdalumni;
    protected Textbox txtb_kesan; // autowired
    protected Button button_TfeedbackinstansiDialog_PrintTfeedbackinstansi; // autowired
    protected Button btnSearchAlumniExtended;
    protected Listbox list_jenis;
    protected Bandbox cmb_jenis;
    public String jenis_instansi;

    // Databinding
    protected transient AnnotateDataBinder binder;
    private TfeedbackinstansiMainCtrl tfeedbackinstansiMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient TfeedbackinstansiService tfeedbackinstansiService;

    /**
     * default constructor.<br>
     */
    public TfeedbackinstansiDetailCtrl() {
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
            setTfeedbackinstansiMainCtrl((TfeedbackinstansiMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getTfeedbackinstansiMainCtrl().setTfeedbackinstansiDetailCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getTfeedbackinstansiMainCtrl().getSelectedTfeedbackinstansi() != null) {
                setSelectedTfeedbackinstansi(getTfeedbackinstansiMainCtrl().getSelectedTfeedbackinstansi());
            } else
                setSelectedTfeedbackinstansi(null);
        } else {
            setSelectedTfeedbackinstansi(null);
        }

    }

     public void onSelect$list_jenis(Event event) throws Exception {
            Tfeedbackinstansi feed =  getTfeedbackinstansi();
            feed.setCjnsinstansi(list_jenis.getSelectedItem().getValue().toString());
            setTfeedbackinstansi(feed);
            jenis_instansi = list_jenis.getSelectedItem().getValue().toString();
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
    public void onCreate$windowTfeedbackinstansiDetail(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        binder.loadAll();

        doFitSize(event);
    }

    public void doRenderCombo(){
                resetListJenis();
                 GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.JenisInstansi.class)).getEnumToList(),
                list_jenis, cmb_jenis, (getTfeedbackinstansi() != null)?getTfeedbackinstansi().getCjnsinstansi():null);
    }

    public void resetListJenis(){
        while (list_jenis.getItemCount() > 0) {
            list_jenis.removeItemAt(0);
        }
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
        borderlayout_TfeedbackinstansiDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowTfeedbackinstansiDetail.invalidate();
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
        txtb_nminstansi.setReadonly(b);
        txtb_alamat.setReadonly(b);
//        txtb_kdalumni.setReadonly(b);
        txtb_kesan.setReadonly(b);
        btnSearchAlumniExtended.setDisabled(b);
        list_jenis.setDisabled(b);
        cmb_jenis.setDisabled(b);
    }

      /**
     * If the Button 'Search Branch ExtendedSearch' is clicked.<br>
     *
     * @param event
     */
    public void onClick$btnSearchAlumniExtended(Event event) {
        doSearchAlumniExtended(event);
    }

    /**
     * Opens the Search and Get Dialog for Branches.<br>
     * It appends/changes the branch object for the current bean.<br>
     *
     * @param event
     */
    private void doSearchAlumniExtended(Event event) {
        Malumni malumni = MalumniExtendedSearchListBox.show(windowTfeedbackinstansiDetail);

        if (malumni != null) {
//            txtb_kdalumni.setValue(malumni.getMmahasiswa().getCnim());
            txtb_nmalumni.setValue(malumni.getMmahasiswa().getCnama());
            Tfeedbackinstansi afeedback = getTfeedbackinstansi();
            afeedback.setMalumni(malumni);
            setTfeedbackinstansi(afeedback);
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
    public Tfeedbackinstansi getTfeedbackinstansi() {
        // STORED IN THE module's MainController
        return getTfeedbackinstansiMainCtrl().getSelectedTfeedbackinstansi();
    }

    public void setTfeedbackinstansi(Tfeedbackinstansi anTfeedbackinstansi) {
        // STORED IN THE module's MainController
        getTfeedbackinstansiMainCtrl().setSelectedTfeedbackinstansi(anTfeedbackinstansi);
    }

    public Tfeedbackinstansi getSelectedTfeedbackinstansi() {
        // STORED IN THE module's MainController
        return getTfeedbackinstansiMainCtrl().getSelectedTfeedbackinstansi();
    }

    public void setSelectedTfeedbackinstansi(Tfeedbackinstansi selectedTfeedbackinstansi) {
        // STORED IN THE module's MainController
        getTfeedbackinstansiMainCtrl().setSelectedTfeedbackinstansi(selectedTfeedbackinstansi);
    }

    public BindingListModelList getTfeedbackinstansis() {
        // STORED IN THE module's MainController
        return getTfeedbackinstansiMainCtrl().getTfeedbackinstansis();
    }

    public void setTfeedbackinstansis(BindingListModelList tfeedbackinstansis) {
        // STORED IN THE module's MainController
        getTfeedbackinstansiMainCtrl().setTfeedbackinstansis(tfeedbackinstansis);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public void setTfeedbackinstansiService(TfeedbackinstansiService tfeedbackinstansiService) {
        this.tfeedbackinstansiService = tfeedbackinstansiService;
    }

    public TfeedbackinstansiService getTfeedbackinstansiService() {
        return this.tfeedbackinstansiService;
    }

    public void setTfeedbackinstansiMainCtrl(TfeedbackinstansiMainCtrl tfeedbackinstansiMainCtrl) {
        this.tfeedbackinstansiMainCtrl = tfeedbackinstansiMainCtrl;
    }

    public TfeedbackinstansiMainCtrl getTfeedbackinstansiMainCtrl() {
        return this.tfeedbackinstansiMainCtrl;
    }

}
