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
package id.ac.idu.webui.administrasi.alumni;

import id.ac.idu.administrasi.service.KodePosService;
import id.ac.idu.administrasi.service.MahasiswaService;
import id.ac.idu.administrasi.service.MalumniService;
import id.ac.idu.backend.model.*;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.webui.administrasi.mahasiswa.model.OrderSearchKodeposList;
import id.ac.idu.webui.irs.cutimhs.model.OrderSearchMahasiswaList;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.pagging.PagedListWrapper;
import id.ac.idu.webui.util.searchdialogs.MkabExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.MkecExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.MkelExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.MprovExtendedSearchListBox;
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
 * This is the controller class for the /WEB-INF/pages/malumni/malumniList.zul
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
public class MalumniDetailCtrl extends GFCBaseCtrl implements Serializable {

    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(MalumniDetailCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowMalumniDetail; // autowired

    protected Borderlayout borderlayout_MalumniDetail; // autowired

    protected Textbox txtb_nim; // autowired
    protected Textbox txtb_nama; // autowired
    protected Textbox txtb_tempatlahir;
    protected Textbox txtb_alamat;
    //protected Textbox txtb_kdpos;
    protected Textbox txtb_kodeposid;
    protected Textbox txtb_kodepos;
    protected Textbox txtb_nmprov;
    protected Textbox txtb_kab_id;
    protected Textbox txtb_nmkab;
    protected Textbox txtb_kec_id;
    protected Textbox txtb_nmkec;
    protected Textbox txtb_kel_id;
    protected Textbox txtb_nmkel;
    protected Textbox txtb_telp;
    protected Textbox txtb_hp;
    protected Textbox txtb_statusnikah;
    protected Textbox txtb_agama;
    protected Textbox txtb_bekerja;
    protected Bandbox cmb_cstatnkh;
    protected Bandbox cmb_ckdagama;
    protected Bandbox cmb_ckdbekerja;
    protected Button btnSearchProvExtended;
    protected Button btnSearchKabExtended;
    protected Button btnSearchKecExtended;
    protected Button btnSearchKelExtended;
    protected Bandbox cmb_kodepos;

    protected Button button_MalumniDialog_PrintMalumni; // autowired
    protected Intbox txtb_mhsid;
    protected Bandbox bandbox_Dialog_MahasiswaSearch;
    protected Paging paging_MahasiswaSearchList;
    private transient PagedListWrapper<Mmahasiswa> plwMahasiswa;
    protected Listbox listMahasiswaSearch;
    private int pageSize;

    private transient PagedListWrapper<MkodePos> plwKodepos;
    protected Paging paging_kodepos;
    protected Listbox listKodepos;

    // Databinding
    protected transient AnnotateDataBinder binder;
    private MalumniMainCtrl malumniMainCtrl;
    private MalumniDetailCtrl detailCtrl;

    // ServiceDAOs / Domain Classes
    private transient MalumniService malumniService;
    private transient MahasiswaService popupMhsService;
    private transient KodePosService kodePosService;

    private MkodePos kodepos;

    /**
     * default constructor.<br>
     */
    public MalumniDetailCtrl() {
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
            setMalumniMainCtrl((MalumniMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getMalumniMainCtrl().setMalumniDetailCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getMalumniMainCtrl().getSelectedMalumni() != null) {
                setSelectedMalumni(getMalumniMainCtrl().getSelectedMalumni());
            } else
                setSelectedMalumni(null);
        } else {
            setSelectedMalumni(null);
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
    public void onCreate$windowMalumniDetail(Event event) throws Exception {
        setPageSize(20);
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        binder.loadAll();

        doFitSize(event);
    }

    public void onOpen$bandbox_Dialog_MahasiswaSearch(Event event) {
        HibernateSearchObject<Mmahasiswa> soCustomer = new HibernateSearchObject<Mmahasiswa>(Mmahasiswa.class);
		soCustomer.addSort("cnim", false);

		paging_MahasiswaSearchList.setPageSize(pageSize);
		paging_MahasiswaSearchList.setDetailed(true);
		getPlwMahasiswa().init(soCustomer, listMahasiswaSearch, paging_MahasiswaSearchList);
		listMahasiswaSearch.setItemRenderer(new OrderSearchMahasiswaList());
    }

    public void onClick$button_bbox_Close(Event event) {
        bandbox_Dialog_MahasiswaSearch.close();
	}

    public void onMahasiswaItem(Event event) {
        Listitem item = listMahasiswaSearch.getSelectedItem();
        if (item != null) {

            Mmahasiswa aMahasiswa = (Mmahasiswa) item.getAttribute("data");
            getMalumniMainCtrl().setMahasiswa(aMahasiswa);

            txtb_nim.setValue(getMalumniMainCtrl().getMahasiswa().getCnim());
            txtb_mhsid.setValue(getMalumniMainCtrl().getMahasiswa().getId());
            txtb_nama.setValue(getMalumniMainCtrl().getMahasiswa().getCnama());
            txtb_tempatlahir.setValue(getMalumniMainCtrl().getMahasiswa().getCtemplhr());
            txtb_alamat.setValue(getMalumniMainCtrl().getMahasiswa().getCalamat());
            //txtb_kdpos.setValue(getMalumniMainCtrl().getMahasiswa().getKodeposId().toString());
            txtb_kodeposid.setValue(getMalumniMainCtrl().getMahasiswa().getKodeposId().toString());
            Integer kodePosId = getMalumniMainCtrl().getMahasiswa().getKodeposId();
            setKodepos(getKodePosService().getKodePosByStringId(kodePosId.toString()));
            txtb_kodepos.setValue(getKodepos().getKodepos());
            //txtb_prop_id.setValue(getMalumniMainCtrl().getMahasiswa().getC);
            //txtb_nmprov.setValue(getMalumniMainCtrl().getMahasiswa().get);
            //txtb_kab_id.setValue(getMalumniMainCtrl().getMahasiswa().get);
            //txtb_nmkab.setValue(getMalumniMainCtrl().getMahasiswa().get);
            //txtb_kec_id.setValue(getMalumniMainCtrl().getMahasiswa().get);
            //txtb_nmkec.setValue(getMalumniMainCtrl().getMahasiswa().get);
            //txtb_kel_id.setValue(getMalumniMainCtrl().getMahasiswa().get);
            //txtb_nmkel.setValue(getMalumniMainCtrl().getMahasiswa().get);
            txtb_telp.setValue(getMalumniMainCtrl().getMahasiswa().getCnotelp());
            txtb_hp.setValue(getMalumniMainCtrl().getMahasiswa().getCnohp());
            txtb_statusnikah.setValue(getMalumniMainCtrl().getMahasiswa().getCstatnkh().toString());
            txtb_agama.setValue(getMalumniMainCtrl().getMahasiswa().getCkdagama());
            txtb_bekerja.setValue(getMalumniMainCtrl().getMahasiswa().getCketkerja());

            bandbox_Dialog_MahasiswaSearch.setValue(getMalumniMainCtrl().getMahasiswa().getCnim());
        }
        bandbox_Dialog_MahasiswaSearch.close();
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
        borderlayout_MalumniDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowMalumniDetail.invalidate();
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
        txtb_nim.setReadonly(b);
        txtb_nama.setReadonly(b);
        txtb_tempatlahir.setReadonly(b);
        txtb_alamat.setReadonly(b);
        //txtb_kdpos.setReadonly(b);
        //txtb_prop_id.setReadonly(b);
        //txtb_nmprov.setReadonly(b);
        //txtb_kab_id.setReadonly(b);
        //txtb_nmkab.setReadonly(b);
        //txtb_kec_id.setReadonly(b);
        //txtb_nmkec.setReadonly(b);
        //txtb_kel_id.setReadonly(b);
        //txtb_nmkel.setReadonly(b);
        txtb_telp.setReadonly(b);
        txtb_hp.setReadonly(b);
        txtb_statusnikah.setReadonly(b);
        txtb_agama.setReadonly(b);
        txtb_bekerja.setReadonly(b);
        cmb_cstatnkh.setReadonly(b);
        cmb_ckdagama.setReadonly(b);
        cmb_ckdbekerja.setReadonly(b);
        bandbox_Dialog_MahasiswaSearch.setDisabled(b);
        btnSearchProvExtended.setDisabled(b);
        btnSearchKabExtended.setDisabled(b);
        btnSearchKecExtended.setDisabled(b);
        btnSearchKelExtended.setDisabled(b);

    }

      /**
     * If the Button 'Search Branch ExtendedSearch' is clicked.<br>
     *
     * @param event
     */
    public void onClick$btnSearchProvExtended(Event event) {
        doSearchMprovExtended(event);
    }

    /**
     * Opens the Search and Get Dialog for Branches.<br>
     * It appends/changes the branch object for the current bean.<br>
     *
     * @param event
     */
    private void doSearchMprovExtended(Event event) {
        Mprov mprov = MprovExtendedSearchListBox.show(windowMalumniDetail);

        if (mprov != null) {
//            txtb_kdalumni.setValue(malumni.getMmahasiswa().getCnim());
            txtb_nmprov.setValue(mprov.getCnamaProv());
            Malumni amalumni = getMalumni();
            amalumni.setMprov(mprov);
            setMalumni(amalumni);
        }
    }

      /**
     * If the Button 'Search Branch ExtendedSearch' is clicked.<br>
     *
     * @param event
     */
    public void onClick$btnSearchKabExtended(Event event) {
        doSearchMkabExtended(event);
    }

    /**
     * Opens the Search and Get Dialog for Branches.<br>
     * It appends/changes the branch object for the current bean.<br>
     *
     * @param event
     */
    private void doSearchMkabExtended(Event event) {
        Mkab mkab = MkabExtendedSearchListBox.show(windowMalumniDetail);

        if (mkab != null) {
//            txtb_kdalumni.setValue(malumni.getMmahasiswa().getCnim());
            txtb_nmkab.setValue(mkab.getCnamaKab());
            Malumni amalumni = getMalumni();
            amalumni.setMkab(mkab);
            setMalumni(amalumni);
        }
    }

      /**
     * If the Button 'Search Branch ExtendedSearch' is clicked.<br>
     *
     * @param event
     */
    public void onClick$btnSearchKecExtended(Event event) {
        doSearchMkecExtended(event);
    }

    /**
     * Opens the Search and Get Dialog for Branches.<br>
     * It appends/changes the branch object for the current bean.<br>
     *
     * @param event
     */
    private void doSearchMkecExtended(Event event) {
        Mkec mkec = MkecExtendedSearchListBox.show(windowMalumniDetail);

        if (mkec != null) {
            txtb_nmkec.setValue(mkec.getCnamaKec());
            Malumni amalumni = getMalumni();
            amalumni.setMkec(mkec);
            setMalumni(amalumni);
        }
    }
      /**
     * If the Button 'Search Branch ExtendedSearch' is clicked.<br>
     *
     * @param event
     */
    public void onClick$btnSearchKelExtended(Event event) {
        doSearchMkelExtended(event);
    }

    /**
     * Opens the Search and Get Dialog for Branches.<br>
     * It appends/changes the branch object for the current bean.<br>
     *
     * @param event
     */
    private void doSearchMkelExtended(Event event) {
        Mkel mkel = MkelExtendedSearchListBox.show(windowMalumniDetail);

        if (mkel != null) {
            txtb_nmkel.setValue(mkel.getCnamaKel());
            Malumni amalumni = getMalumni();
            amalumni.setMkel(mkel);
            setMalumni(amalumni);
        }
    }

    public void onKodeposItem(Event event) {
        Listitem item;
        item = listKodepos.getSelectedItem();

        if (item != null) {
            MkodePos aKodepos = (MkodePos) item.getAttribute("data");
            setKodepos(aKodepos);

            txtb_kodeposid.setValue((getKodepos().getId()).trim());
            txtb_kodepos.setValue(getDetailCtrl().getKodepos().getKodepos());

        }

        cmb_kodepos.close();
    }

    public void onOpen$cmb_kodepos(Event event) {
        //this.kodetype = "_1";
        HibernateSearchObject<MkodePos> soKodepos = new HibernateSearchObject<MkodePos>(MkodePos.class);
		soKodepos.addSort("kodepos", false);

		paging_kodepos.setPageSize(pageSize);
		paging_kodepos.setDetailed(true);
		getPlwKodepos().init(soKodepos, listKodepos, paging_kodepos);
		listKodepos.setItemRenderer(new OrderSearchKodeposList());
    }

    public void onClick$button_close(Event event) {
        cmb_kodepos.close();
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
    public Malumni getMalumni() {
        // STORED IN THE module's MainController
        return getMalumniMainCtrl().getSelectedMalumni();
    }

    public void setMalumni(Malumni anMalumni) {
        // STORED IN THE module's MainController
        getMalumniMainCtrl().setSelectedMalumni(anMalumni);
    }

    public Malumni getSelectedMalumni() {
        // STORED IN THE module's MainController
        return getMalumniMainCtrl().getSelectedMalumni();
    }

    public void setSelectedMalumni(Malumni selectedMalumni) {
        // STORED IN THE module's MainController
        getMalumniMainCtrl().setSelectedMalumni(selectedMalumni);
    }

    public BindingListModelList getMalumnis() {
        // STORED IN THE module's MainController
        return getMalumniMainCtrl().getMalumnis();
    }

    public void setMalumnis(BindingListModelList malumnis) {
        // STORED IN THE module's MainController
        getMalumniMainCtrl().setMalumnis(malumnis);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public void setMalumniService(MalumniService malumniService) {
        this.malumniService = malumniService;
    }

    public MalumniService getMalumniService() {
        return this.malumniService;
    }

    public void setMalumniMainCtrl(MalumniMainCtrl malumniMainCtrl) {
        this.malumniMainCtrl = malumniMainCtrl;
    }

    public MalumniMainCtrl getMalumniMainCtrl() {
        return this.malumniMainCtrl;
    }
    public PagedListWrapper<Mmahasiswa> getPlwMahasiswa() {
        return plwMahasiswa;
    }

    public MahasiswaService getPopupMhsService() {
        return popupMhsService;
    }

    public void setPopupMhsService(MahasiswaService popupMhsService) {
        this.popupMhsService = popupMhsService;
    }

    public void setPlwMahasiswa(PagedListWrapper<Mmahasiswa> plwMahasiswa) {
        this.plwMahasiswa = plwMahasiswa;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setKodePosService(KodePosService kodePosService) {
        this.kodePosService = kodePosService;
    }

    public KodePosService getKodePosService() {
        return this.kodePosService;
    }

    public PagedListWrapper<MkodePos> getPlwKodepos() {
        return plwKodepos;
    }

    public void setPlwKodepos(PagedListWrapper<MkodePos> plwKodepos) {
        this.plwKodepos = plwKodepos;
    }

    public MalumniDetailCtrl getDetailCtrl() {
        return detailCtrl;
    }

    public void setDetailCtrl(MalumniDetailCtrl detailCtrl) {
        this.detailCtrl = detailCtrl;
    }

    public MkodePos getKodepos() {
        return kodepos;
    }

    public void setKodepos(MkodePos kodepos) {
        this.kodepos = kodepos;
    }
}
