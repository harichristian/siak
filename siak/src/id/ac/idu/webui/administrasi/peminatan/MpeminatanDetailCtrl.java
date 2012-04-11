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
package id.ac.idu.webui.administrasi.peminatan;

import id.ac.idu.administrasi.service.MpeminatanService;
import id.ac.idu.backend.model.Mpeminatan;
import id.ac.idu.backend.model.Mprodi;
import id.ac.idu.util.Codec;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.GFCListModelCtrl;
import id.ac.idu.webui.util.searchdialogs.ProdiExtendedSearchListBox;
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
 * This is the controller class for the /WEB-INF/pages/mpeminatan/mpeminatanList.zul
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
public class MpeminatanDetailCtrl extends GFCBaseCtrl implements Serializable {

    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(MpeminatanDetailCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowMpeminatanDetail; // autowired

    protected Borderlayout borderlayout_MpeminatanDetail; // autowired

    protected Textbox txtb_jenis; // autowired
    protected Textbox txtb_nmpeminatan; // autowired
    protected Textbox txtb_nmprodi; // autowired
    protected Textbox txtb_nmpeminatan_eng; // autowired
    protected Textbox txtb_aktif; // autowired
    protected Button button_MpeminatanDialog_PrintMpeminatan; // autowired
    protected Button btnSearchProdiExtended;
    protected Listbox list_jenis;
    protected Bandbox cmb_jenis;

    // Databinding
    protected transient AnnotateDataBinder binder;
    private MpeminatanMainCtrl mpeminatanMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient MpeminatanService mpeminatanService;

    /**
     * default constructor.<br>
     */
    public MpeminatanDetailCtrl() {
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
            setMpeminatanMainCtrl((MpeminatanMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getMpeminatanMainCtrl().setMpeminatanDetailCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getMpeminatanMainCtrl().getSelectedMpeminatan() != null) {
                setSelectedMpeminatan(getMpeminatanMainCtrl().getSelectedMpeminatan());
            } else
                setSelectedMpeminatan(null);
        } else {
            setSelectedMpeminatan(null);
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
    public void onCreate$windowMpeminatanDetail(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

          GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.FlagPeminatan.class)).getEnumToList(),
                list_jenis, cmb_jenis, (getMpeminatan() != null)?getMpeminatan().getCaktif():null);

        binder.loadAll();

        doFitSize(event);
    }

    public void refreshCombo() {
        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.FlagPeminatan.class)).getEnumToList(),
                list_jenis, cmb_jenis, (getMpeminatan() != null)?getMpeminatan().getCaktif():null);
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
        borderlayout_MpeminatanDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowMpeminatanDetail.invalidate();
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

        txtb_jenis.setReadonly(b);
        txtb_nmpeminatan.setReadonly(b);
        txtb_nmpeminatan_eng.setReadonly(b);
        btnSearchProdiExtended.setDisabled(b);
        list_jenis.setDisabled(b);
        cmb_jenis.setDisabled(b);
    }

      /**
     * If the Button 'Search Branch ExtendedSearch' is clicked.<br>
     *
     * @param event
     */

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
        Mprodi mprodi = ProdiExtendedSearchListBox.show(windowMpeminatanDetail);

        if (mprodi != null) {
            txtb_nmprodi.setValue(mprodi.getCnmprogst());
            Mpeminatan apeminatan = getMpeminatan();
            apeminatan.setMprodi(mprodi);
            setMpeminatan(apeminatan);
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
    public Mpeminatan getMpeminatan() {
        // STORED IN THE module's MainController
        return getMpeminatanMainCtrl().getSelectedMpeminatan();
    }

    public void setMpeminatan(Mpeminatan anMpeminatan) {
        // STORED IN THE module's MainController
        getMpeminatanMainCtrl().setSelectedMpeminatan(anMpeminatan);
    }

    public Mpeminatan getSelectedMpeminatan() {
        // STORED IN THE module's MainController
        return getMpeminatanMainCtrl().getSelectedMpeminatan();
    }

    public void setSelectedMpeminatan(Mpeminatan selectedMpeminatan) {
        // STORED IN THE module's MainController
        getMpeminatanMainCtrl().setSelectedMpeminatan(selectedMpeminatan);
    }

    public BindingListModelList getMpeminatans() {
        // STORED IN THE module's MainController
        return getMpeminatanMainCtrl().getMpeminatans();
    }

    public void setMpeminatans(BindingListModelList mpeminatans) {
        // STORED IN THE module's MainController
        getMpeminatanMainCtrl().setMpeminatans(mpeminatans);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public void setMpeminatanService(MpeminatanService mpeminatanService) {
        this.mpeminatanService = mpeminatanService;
    }

    public MpeminatanService getMpeminatanService() {
        return this.mpeminatanService;
    }

    public void setMpeminatanMainCtrl(MpeminatanMainCtrl mpeminatanMainCtrl) {
        this.mpeminatanMainCtrl = mpeminatanMainCtrl;
    }

    public MpeminatanMainCtrl getMpeminatanMainCtrl() {
        return this.mpeminatanMainCtrl;
    }

}
