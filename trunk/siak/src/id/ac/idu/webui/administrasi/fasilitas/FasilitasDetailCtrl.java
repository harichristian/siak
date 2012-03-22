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
package id.ac.idu.webui.administrasi.fasilitas;

import id.ac.idu.administrasi.service.FasilitasService;
import id.ac.idu.backend.model.Mfasilitas;
import id.ac.idu.webui.util.GFCBaseCtrl;
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
 * This is the controller class for the /WEB-INF/pages/fasilitas/fasilitasList.zul
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
public class FasilitasDetailCtrl extends GFCBaseCtrl implements Serializable {

    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(FasilitasDetailCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowFasilitasDetail; // autowired

    protected Borderlayout borderlayout_FasilitasDetail; // autowired

    protected Textbox txtb_kdfasilitas; // autowired
    protected Textbox txtb_nmfasilitas; // autowired
    protected Button button_FasilitasDialog_PrintFasilitas; // autowired

    // Databinding
    protected transient AnnotateDataBinder binder;
    private FasilitasMainCtrl fasilitasMainCtrl;

    // ServiceDAOs / Domain Classes
    private transient FasilitasService fasilitasService;

    /**
     * default constructor.<br>
     */
    public FasilitasDetailCtrl() {
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
            setFasilitasMainCtrl((FasilitasMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getFasilitasMainCtrl().setFasilitasDetailCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getFasilitasMainCtrl().getSelectedFasilitas() != null) {
                setSelectedFasilitas(getFasilitasMainCtrl().getSelectedFasilitas());
            } else
                setSelectedFasilitas(null);
        } else {
            setSelectedFasilitas(null);
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
    public void onCreate$windowFasilitasDetail(Event event) throws Exception {
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
        borderlayout_FasilitasDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowFasilitasDetail.invalidate();
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
        txtb_kdfasilitas.setReadonly(b);
        txtb_nmfasilitas.setReadonly(b);
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
    public Mfasilitas getFasilitas() {
        // STORED IN THE module's MainController
        return getFasilitasMainCtrl().getSelectedFasilitas();
    }

    public void setFasilitas(Mfasilitas anFasilitas) {
        // STORED IN THE module's MainController
        getFasilitasMainCtrl().setSelectedFasilitas(anFasilitas);
    }

    public Mfasilitas getSelectedFasilitas() {
        // STORED IN THE module's MainController
        return getFasilitasMainCtrl().getSelectedFasilitas();
    }

    public void setSelectedFasilitas(Mfasilitas selectedFasilitas) {
        // STORED IN THE module's MainController
        getFasilitasMainCtrl().setSelectedFasilitas(selectedFasilitas);
    }

    public BindingListModelList getFasilitass() {
        // STORED IN THE module's MainController
        return getFasilitasMainCtrl().getFasilitass();
    }

    public void setFasilitass(BindingListModelList fasilitass) {
        // STORED IN THE module's MainController
        getFasilitasMainCtrl().setFasilitass(fasilitass);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public void setFasilitasService(FasilitasService fasilitasService) {
        this.fasilitasService = fasilitasService;
    }

    public FasilitasService getFasilitasService() {
        return this.fasilitasService;
    }

    public void setFasilitasMainCtrl(FasilitasMainCtrl fasilitasMainCtrl) {
        this.fasilitasMainCtrl = fasilitasMainCtrl;
    }

    public FasilitasMainCtrl getFasilitasMainCtrl() {
        return this.fasilitasMainCtrl;
    }

}
