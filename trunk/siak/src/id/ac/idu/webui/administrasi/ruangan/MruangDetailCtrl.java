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
package id.ac.idu.webui.administrasi.ruangan;

import id.ac.idu.administrasi.service.MruangService;
import id.ac.idu.backend.model.Mruang;
import id.ac.idu.util.Codec;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.GFCListModelCtrl;
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
 * This is the controller class for the /WEB-INF/pages/mruang/mruangList.zul
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
public class MruangDetailCtrl extends GFCBaseCtrl implements Serializable {

    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(MruangDetailCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowMruangDetail; // autowired

    protected Borderlayout borderlayout_MruangDetail; // autowired

    protected Textbox txtb_kdruangan; // autowired
    protected Textbox txtb_namaruangan; // autowired
    protected Textbox txtb_maxkuliah; // autowired
    protected Textbox txtb_maxujian; // autowired
    protected Textbox txtb_status; // autowired
    protected Button button_MruangDialog_PrintMruang; // autowired
    protected Bandbox cmb_status;
    protected Listbox list_status;

    // Databinding
    protected transient AnnotateDataBinder binder;
    private MruangMainCtrl mruangMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient MruangService mruangService;

    /**
     * default constructor.<br>
     */
    public MruangDetailCtrl() {
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
            setMruangMainCtrl((MruangMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getMruangMainCtrl().setMruangDetailCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getMruangMainCtrl().getSelectedMruang() != null) {
                setSelectedMruang(getMruangMainCtrl().getSelectedMruang());
            } else
                setSelectedMruang(null);
        } else {
            setSelectedMruang(null);
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
    public void onCreate$windowMruangDetail(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.StatusRuangan.class)).getEnumToList(),
                list_status, cmb_status, (getMruang() != null)?getMruang().getCstatus():null);


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
        borderlayout_MruangDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowMruangDetail.invalidate();
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
        txtb_kdruangan.setReadonly(b);
        txtb_namaruangan.setReadonly(b);
        txtb_maxkuliah.setReadonly(b);
        txtb_maxujian.setReadonly(b);
//        txtb_status.setReadonly(b);
        cmb_status.setDisabled(b);
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
    public Mruang getMruang() {
        // STORED IN THE module's MainController
        return getMruangMainCtrl().getSelectedMruang();
    }

    public void setMruang(Mruang anMruang) {
        // STORED IN THE module's MainController
        getMruangMainCtrl().setSelectedMruang(anMruang);
    }

    public Mruang getSelectedMruang() {
        // STORED IN THE module's MainController
        return getMruangMainCtrl().getSelectedMruang();
    }

    public void setSelectedMruang(Mruang selectedMruang) {
        // STORED IN THE module's MainController
        getMruangMainCtrl().setSelectedMruang(selectedMruang);
    }

    public BindingListModelList getMruangs() {
        // STORED IN THE module's MainController
        return getMruangMainCtrl().getMruangs();
    }

    public void setMruangs(BindingListModelList mruangs) {
        // STORED IN THE module's MainController
        getMruangMainCtrl().setMruangs(mruangs);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public void setMruangService(MruangService mruangService) {
        this.mruangService = mruangService;
    }

    public MruangService getMruangService() {
        return this.mruangService;
    }

    public void setMruangMainCtrl(MruangMainCtrl mruangMainCtrl) {
        this.mruangMainCtrl = mruangMainCtrl;
    }

    public MruangMainCtrl getMruangMainCtrl() {
        return this.mruangMainCtrl;
    }

     public Textbox getTxtb_status() {
        return txtb_status;
    }

}
