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
package id.ac.idu.webui.administrasi.fasilitasruang;

import id.ac.idu.administrasi.service.MfasilitasruangService;
import id.ac.idu.backend.model.Mfasilitas;
import id.ac.idu.backend.model.Mfasilitasruang;
import id.ac.idu.backend.model.Mruang;
import id.ac.idu.util.Codec;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.GFCListModelCtrl;
import id.ac.idu.webui.util.searchdialogs.MfasilitasExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.MruangExtendedSearchListBox;
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
 * This is the controller class for the /WEB-INF/pages/mfasilitasruang/mfasilitasruangList.zul
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
public class MfasilitasruangDetailCtrl extends GFCBaseCtrl implements Serializable {

    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(MfasilitasruangDetailCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowMfasilitasruangDetail; // autowired

    protected Borderlayout borderlayout_MfasilitasruangDetail; // autowired

    protected Textbox txtb_kdruang; // autowired
    protected Textbox txtb_nmruang; // autowired
    protected Textbox txtb_kdfasilitas; // autowired
    protected Textbox txtb_nmfasilitas; // autowired
    protected Textbox txtb_jumlah;
    protected Button button_MfasilitasruangDialog_PrintMfasilitasruang; // autowired
    protected Button btnSearchRuangExtended; // autowired
    protected Button btnSearchFasilitasExtended;
    protected Listbox list_status;
    protected Bandbox cmb_status;

    // Databinding
    protected transient AnnotateDataBinder binder;
    private MfasilitasruangMainCtrl mfasilitasruangMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient MfasilitasruangService mfasilitasruangService;

    /**
     * default constructor.<br>
     */
    public MfasilitasruangDetailCtrl() {
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
            setMfasilitasruangMainCtrl((MfasilitasruangMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getMfasilitasruangMainCtrl().setMfasilitasruangDetailCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getMfasilitasruangMainCtrl().getSelectedMfasilitasruang() != null) {
                setSelectedMfasilitasruang(getMfasilitasruangMainCtrl().getSelectedMfasilitasruang());
            } else
                setSelectedMfasilitasruang(null);
        } else {
            setSelectedMfasilitasruang(null);
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
    public void onCreate$windowMfasilitasruangDetail(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

         GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.StatusRuangan.class)).getEnumToList(),
                list_status, cmb_status, (getMfasilitasruang() != null)?getMfasilitasruang().getCstatus():null);

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
        borderlayout_MfasilitasruangDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowMfasilitasruangDetail.invalidate();
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
        txtb_kdruang.setReadonly(b);
        txtb_jumlah.setReadonly(b);
        txtb_kdfasilitas.setReadonly(b);
        list_status.setDisabled(b);
        btnSearchRuangExtended.setDisabled(b);
        btnSearchFasilitasExtended.setDisabled(b);
        cmb_status.setDisabled(b);
    }

    /**
     * If the Button 'Search Branch ExtendedSearch' is clicked.<br>
     *
     * @param event
     */
    public void onClick$btnSearchRuangExtended(Event event) {
        doSearchRuangExtended(event);
    }

      /**
     * If the Button 'Search Branch ExtendedSearch' is clicked.<br>
     *
     * @param event
     */
    public void onClick$btnSearchFasilitasExtended(Event event) {
        doSearchFasilitasExtended(event);
    }

    /**
     * Opens the Search and Get Dialog for Branches.<br>
     * It appends/changes the branch object for the current bean.<br>
     *
     * @param event
     */
    private void doSearchRuangExtended(Event event) {
        Mruang mruang = MruangExtendedSearchListBox.show(windowMfasilitasruangDetail);

        if (mruang != null) {
            txtb_kdruang.setValue(mruang.getCkdruang());
            txtb_nmruang.setValue(mruang.getCnmRuang());
            Mfasilitasruang  aMfasilitasruang = getMfasilitasruang();
            aMfasilitasruang.setMruang(mruang);
            setMfasilitasruang(aMfasilitasruang);
        }
    }

    /**
     * Opens the Search and Get Dialog for Branches.<br>
     * It appends/changes the branch object for the current bean.<br>
     *
     * @param event
     */
    private void doSearchFasilitasExtended(Event event) {
        Mfasilitas mfasilitas = MfasilitasExtendedSearchListBox.show(windowMfasilitasruangDetail);

        if (mfasilitas != null) {
            txtb_kdfasilitas.setValue(mfasilitas.getCkdfasilitas());
            txtb_nmfasilitas.setValue(mfasilitas.getCnamaFasilitas());
            Mfasilitasruang  aMfasilitasruang = getMfasilitasruang();
            aMfasilitasruang.setMfasilitas(mfasilitas);
            setMfasilitasruang(aMfasilitasruang);
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
    public Mfasilitasruang getMfasilitasruang() {
        // STORED IN THE module's MainController
        return getMfasilitasruangMainCtrl().getSelectedMfasilitasruang();
    }

    public void setMfasilitasruang(Mfasilitasruang anMfasilitasruang) {
        // STORED IN THE module's MainController
        getMfasilitasruangMainCtrl().setSelectedMfasilitasruang(anMfasilitasruang);
    }

    public Mfasilitasruang getSelectedMfasilitasruang() {
        // STORED IN THE module's MainController
        return getMfasilitasruangMainCtrl().getSelectedMfasilitasruang();
    }

    public void setSelectedMfasilitasruang(Mfasilitasruang selectedMfasilitasruang) {
        // STORED IN THE module's MainController
        getMfasilitasruangMainCtrl().setSelectedMfasilitasruang(selectedMfasilitasruang);
    }

    public BindingListModelList getMfasilitasruangs() {
        // STORED IN THE module's MainController
        return getMfasilitasruangMainCtrl().getMfasilitasruangs();
    }

    public void setMfasilitasruangs(BindingListModelList mfasilitasruangs) {
        // STORED IN THE module's MainController
        getMfasilitasruangMainCtrl().setMfasilitasruangs(mfasilitasruangs);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public void setMfasilitasruangService(MfasilitasruangService mfasilitasruangService) {
        this.mfasilitasruangService = mfasilitasruangService;
    }

    public MfasilitasruangService getMfasilitasruangService() {
        return this.mfasilitasruangService;
    }

    public void setMfasilitasruangMainCtrl(MfasilitasruangMainCtrl mfasilitasruangMainCtrl) {
        this.mfasilitasruangMainCtrl = mfasilitasruangMainCtrl;
    }

    public MfasilitasruangMainCtrl getMfasilitasruangMainCtrl() {
        return this.mfasilitasruangMainCtrl;
    }

}
