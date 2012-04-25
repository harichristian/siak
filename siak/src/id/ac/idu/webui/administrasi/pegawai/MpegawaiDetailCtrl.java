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
package id.ac.idu.webui.administrasi.pegawai;

import id.ac.idu.administrasi.service.MpegawaiService;
import id.ac.idu.backend.model.Mjabatan;
import id.ac.idu.backend.model.MkodePos;
import id.ac.idu.backend.model.Mpegawai;
import id.ac.idu.backend.model.Mprov;
import id.ac.idu.util.Codec;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.GFCListModelCtrl;
import id.ac.idu.webui.util.ListBoxUtil;
import id.ac.idu.webui.util.ZksampleMessageUtils;
import id.ac.idu.webui.util.searchdialogs.JabatanExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.KodePosExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.MprovExtendedSearchListBox;
import id.ac.idu.webui.util.test.EnumConverter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.*;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the /WEB-INF/pages/mpegawai/mpegawaiList.zul
 * file.<br>
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * 
 * @changes 05/15/2009: sge Migrating the list models for paging. <br>
 *          07/24/2009: sge changings for clustering.<br>
 *          10/12/2009: sge changings in the saving routine.<br>
 *          11/07/2009: bbr changed to extending from GFCBaseCtrl<br>
 *          (GenericForwardComposer) for spring-managed creation.<br>
 *          07/04/2010: sge modified for zk5.x with complete Annotated
 *          Databinding.<br>
 * 
 * @author bbruhns
 * @author sgerth
 */
public class MpegawaiDetailCtrl extends GFCBaseCtrl implements Serializable {

	private static final long serialVersionUID = -8352659530536077973L;
	private static final Logger logger = Logger.getLogger(MpegawaiDetailCtrl.class);

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * All the components that are defined here and have a corresponding
	 * component with the same 'id' in the zul-file are getting autowired by our
	 * 'extends GFCBaseCtrl' GenericForwardComposer.
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	protected Window windowMpegawaiDetail; // autowired

	protected Borderlayout borderlayout_MpegawaiDetail; // autowired

	protected Textbox txtb_nip; // autowired
	protected Textbox txtb_nama; // autowired
	protected Textbox txtb_tempatlahir; // autowired
	protected Datebox txtb_tglahir; // autowired
    protected Textbox txtb_jeniskelamin;
    protected Textbox txtb_aktif;
    protected Textbox txtb_kdpos;
    protected Textbox txtb_goldarah;
    protected Textbox txtb_agama;
    protected Textbox txtb_statusnikah;
    protected Textbox txtb_pendidikan;
    protected Textbox txtb_kdnegara;
    protected Textbox txtb_rumah1;
    protected Textbox txtb_rumah2;
    protected Textbox txtb_proprm_id;
    protected Textbox txtb_proprm_nm;
    protected Textbox txtb_kodearean;
    protected Textbox txtb_telpon;
    protected Textbox txtb_hp;
    protected Textbox txtb_tinggal1;
    protected Textbox txtb_tinggal2;
    protected Textbox txtb_kdpos_tgl;
    protected Textbox txtb_proprm_id_tgl;
    protected Textbox txtb_proprm_nm_tgl;
    protected Textbox txtb_email;
    protected Textbox txtb_tglkerjar;
    protected Textbox txtb_sk;
    protected Datebox txtb_tglSK;
    protected Textbox txtb_golongan;
    protected Bandbox cmb_jeniskelamin;
    protected Listbox txtb_listjeniskelamin;
    protected Bandbox cmb_agama;
    protected Listbox txtb_listagama;
    protected Bandbox cmb_statusnikah;
    protected Listbox txtb_liststatusnikah;
    protected Bandbox cmb_aktif;
    protected Listbox txtb_listaktif;
    protected Bandbox cmb_goldarah;
    protected Listbox txtb_listgoldarah;
    protected Bandbox cmb_pendidikan;
    protected Listbox txtb_listpendidikan;
    protected Button btnSearchKodePosExtended;
    protected Button btnSearchJabatanExtended;
    protected Textbox txtb_jabatan;
    protected Textbox txtb_telp1;
    protected Textbox txtb_telp2;
    protected Textbox txtb_tg1;
    protected Textbox txtb_tg2;
    protected Textbox txtb_kdpostg;
    protected Datebox txtb_tglmasukkerja;
    protected Textbox txtb_noskkerja;
    protected Datebox txtb_tglskkerja;
    protected Textbox txtb_cgol;
    protected Textbox txtb_nippns;
    protected Textbox txtb_ktp;
    protected Textbox txtb_depan;
    protected Textbox txtb_belakang;
    protected Textbox txtb_npwp;
    protected Textbox txtb_tk;
    protected Button btnSearchPropRMExtended;
    protected Textbox txtb_proprm;
    protected Button btnSearchKodePosTGExtended;
    protected Button btnSearchPropTGExtended;
    protected Textbox txtb_proptg;
    protected Image image;
    protected Textbox txtb_foto;



	protected Button button_MpegawaiDialog_PrintMpegawai; // autowired

	// Databinding
	protected transient AnnotateDataBinder binder;
	private MpegawaiMainCtrl mpegawaiMainCtrl;

	// ServiceDAOs / Domain Classes
	private transient MpegawaiService mpegawaiService;

	/**
	 * default constructor.<br>
	 */
	public MpegawaiDetailCtrl() {
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
			setMpegawaiMainCtrl((MpegawaiMainCtrl) arg.get("ModuleMainController"));

			// SET THIS CONTROLLER TO THE module's Parent/MainController
			getMpegawaiMainCtrl().setMpegawaiDetailCtrl(this);

			// Get the selected object.
			// Check if this Controller if created on first time. If so,
			// than the selectedXXXBean should be null
			if (getMpegawaiMainCtrl().getSelectedMpegawai() != null) {
				setSelectedMpegawai(getMpegawaiMainCtrl().getSelectedMpegawai());
			} else
				setSelectedMpegawai(null);
		} else {
			setSelectedMpegawai(null);
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
	public void onCreate$windowMpegawaiDetail(Event event) throws Exception {
		binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        binder.loadAll();

		doFitSize(event);
	}

    public void doResetCombo(){
        ListBoxUtil.resetList(txtb_listjeniskelamin);
        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.JenisKelamin.class)).getEnumToList(),
                txtb_listjeniskelamin, cmb_jeniskelamin, (getMpegawai() != null) ? getMpegawai().getCjenklmn() : null);

        ListBoxUtil.resetList(txtb_listagama);
        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.Agama.class)).getEnumToList(),
                txtb_listagama, cmb_agama, (getMpegawai() != null)?getMpegawai().getCkdagama():null);

        ListBoxUtil.resetList(txtb_liststatusnikah);
        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.StatusNikah.class)).getEnumToList(),
                 txtb_liststatusnikah, cmb_statusnikah, (getMpegawai() != null) ? getMpegawai().getCstatus() : null);

        ListBoxUtil.resetList(txtb_listaktif);
        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.StatusAktif.class)).getEnumToList(),
                txtb_listaktif, cmb_aktif, (getMpegawai() != null)?getMpegawai().getCflagaktif():null);

        ListBoxUtil.resetList(txtb_listgoldarah);
        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.GolDarah.class)).getEnumToList(),
                txtb_listgoldarah, cmb_goldarah, (getMpegawai() != null)?getMpegawai().getCgldarah():null);

        ListBoxUtil.resetList(txtb_listpendidikan);
        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.PendidikanTerakhir.class)).getEnumToList(),
                txtb_listpendidikan, cmb_pendidikan, (getMpegawai() != null)?getMpegawai().getCpendakhir():null);

        try {
            image.setContent(new org.zkoss.image.AImage(new java.net.URL(txtb_foto.getValue())));
        } catch(Exception e) {
            image.setSrc("/emp.jpg");
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
	 *
	 * Calculate how many rows have been place in the listbox. Get the
	 * currentDesktopHeight from a hidden Intbox from the index.zul that are
	 * filled by onClientInfo() in the indexCtroller.
	 */
	public void doFitSize(Event event) {
		final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
		final int maxListBoxHeight = height - 148;
		borderlayout_MpegawaiDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

		windowMpegawaiDetail.invalidate();
	}

	/**
	 * Set all components in readOnly/disabled modus.
	 *
	 * true = all components are readOnly or disabled.<br>
	 * false = all components are accessable.<br>
	 *
	 * @param b
	 */
	public void doReadOnlyMode(boolean b) {
		txtb_nip.setReadonly(b);
		txtb_nama.setReadonly(b);
		txtb_tempatlahir.setReadonly(b);
		txtb_tglahir.setDisabled(b);
        txtb_aktif.setReadonly(b);
        txtb_jeniskelamin.setReadonly(b);
//        txtb_kdpos.setReadonly(b);
        txtb_goldarah.setReadonly(b);


       cmb_jeniskelamin.setDisabled(b);
       txtb_listjeniskelamin.setDisabled(b);
       cmb_agama.setDisabled(b);
       txtb_listagama.setDisabled(b);
       cmb_statusnikah.setDisabled(b);
       txtb_liststatusnikah.setDisabled(b);
       cmb_aktif.setDisabled(b);
       txtb_listaktif.setDisabled(b);
       cmb_goldarah.setDisabled(b);
       txtb_listgoldarah.setDisabled(b);
        cmb_pendidikan.setDisabled(b);
       txtb_listpendidikan.setDisabled(b);
        txtb_kdnegara.setReadonly(b);
        txtb_rumah1.setReadonly(b);
        txtb_rumah2.setReadonly(b);
        txtb_telp1.setReadonly(b);
        txtb_telp2.setReadonly(b);
        txtb_hp.setReadonly(b);
        txtb_tg1.setReadonly(b);
        txtb_tg2.setReadonly(b);
       // txtb_kdpostg.setReadonly(b);
        txtb_email.setReadonly(b);
        txtb_tglmasukkerja.setDisabled(b);
        txtb_noskkerja.setReadonly(b);
        txtb_tglskkerja.setDisabled(b);
        txtb_cgol.setReadonly(b);
        txtb_nippns.setReadonly(b);
        txtb_ktp.setReadonly(b);
        txtb_depan.setReadonly(b);
        txtb_belakang.setReadonly(b);
        txtb_npwp.setReadonly(b);
        txtb_tk.setReadonly(b);
        txtb_foto.setReadonly(b);


        btnSearchKodePosExtended.setDisabled(b);
        btnSearchJabatanExtended.setDisabled(b);
        btnSearchPropRMExtended.setDisabled(b);
        btnSearchKodePosTGExtended.setDisabled(b);
        btnSearchPropTGExtended.setDisabled(b);
	}

     public void onClick$btnSearchPropTGExtended(Event event){
         Mprov prop =  MprovExtendedSearchListBox.show(windowMpegawaiDetail);

        if (prop != null) {
            txtb_proptg.setValue(prop.getCnamaProv());
            Mpegawai pegawai = getMpegawai();
            pegawai.setCproptg(String.valueOf(prop.getId()));
            setMpegawai(pegawai);
        }
    }

    public void onClick$btnSearchKodePosTGExtended(Event event){
        MkodePos pos =  KodePosExtendedSearchListBox.show(windowMpegawaiDetail);

        if (pos != null) {
            txtb_kdpostg.setValue(pos.getKodepos());
            Mpegawai pegawai = getMpegawai();
            pegawai.setCkdpostg(Integer.parseInt(pos.getKodepos()));
            setMpegawai(pegawai);
        }
    }

    public void onClick$btnSearchPropRMExtended(Event event){
        Mprov prop =  MprovExtendedSearchListBox.show(windowMpegawaiDetail);

        if (prop != null) {
            txtb_proprm.setValue(prop.getCnamaProv());
            Mpegawai pegawai = getMpegawai();
            pegawai.setCproprm(String.valueOf(prop.getId()));
            setMpegawai(pegawai);
        }
    }
      /**
     * If the Button 'Search Branch ExtendedSearch' is clicked.<br>
     *
     * @param event
     */
    public void onClick$btnSearchJabatanExtended(Event event) {
        doSearchJabatanExtended(event);
    }

    /**
     * Opens the Search and Get Dialog for Branches.<br>
     * It appends/changes the branch object for the current bean.<br>
     *
     * @param event
     */
    private void doSearchJabatanExtended(Event event) {
        Mjabatan jabatan =  JabatanExtendedSearchListBox.show(windowMpegawaiDetail);

        if (jabatan != null) {
            txtb_jabatan.setValue(jabatan.getCnmjabatan());
            Mpegawai pegawai = getMpegawai();
            pegawai.setMjabatan(jabatan);
            setMpegawai(pegawai);
        }
    }

      /**
     * If the Button 'Search Branch ExtendedSearch' is clicked.<br>
     *
     * @param event
     */
    public void onClick$btnSearchKodePosExtended(Event event) {
        doSearchKodePosExtended(event);
    }

    /**
     * Opens the Search and Get Dialog for Branches.<br>
     * It appends/changes the branch object for the current bean.<br>
     *
     * @param event
     */
    private void doSearchKodePosExtended(Event event) {
        MkodePos pos =  KodePosExtendedSearchListBox.show(windowMpegawaiDetail);

        if (pos != null) {
            txtb_kdpos.setValue(pos.getKodepos());
            Mpegawai pegawai = getMpegawai();
            pegawai.setCkdposrm(Integer.parseInt(pos.getKodepos()));
            setMpegawai(pegawai);
        }
    }

    public void onClick$btnFoto(Event event) throws InterruptedException  {
        try {
            image.setContent(new org.zkoss.image.AImage(new URL(txtb_foto.getValue())));
        } catch (MalformedURLException e) {
            ZksampleMessageUtils.showErrorMessage("Harap masukkan protokol, contoh: Http://");
		} catch (IOException e) {
            ZksampleMessageUtils.showErrorMessage("Gambar tidak bisa dibaca atau ditemukan");
        } catch (IllegalArgumentException e) {
            if(StringUtils.containsIgnoreCase(e.toString(), "host = null")) {
                ZksampleMessageUtils.showErrorMessage("Harap masukkan host, contoh: Http://host.com");
            }
        } catch (NullPointerException e) {
            ZksampleMessageUtils.showErrorMessage("URL yang anda masukkan salah");
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
	public Mpegawai getMpegawai() {
		// STORED IN THE module's MainController
		return getMpegawaiMainCtrl().getSelectedMpegawai();
	}

	public void setMpegawai(Mpegawai anMpegawai) {
		// STORED IN THE module's MainController
		getMpegawaiMainCtrl().setSelectedMpegawai(anMpegawai);
	}

	public Mpegawai getSelectedMpegawai() {
		// STORED IN THE module's MainController
		return getMpegawaiMainCtrl().getSelectedMpegawai();
	}

	public void setSelectedMpegawai(Mpegawai selectedMpegawai) {
		// STORED IN THE module's MainController
		getMpegawaiMainCtrl().setSelectedMpegawai(selectedMpegawai);
	}

	public BindingListModelList getMpegawais() {
		// STORED IN THE module's MainController
		return getMpegawaiMainCtrl().getMpegawais();
	}

	public void setMpegawais(BindingListModelList mpegawais) {
		// STORED IN THE module's MainController
		getMpegawaiMainCtrl().setMpegawais(mpegawais);
	}

	public AnnotateDataBinder getBinder() {
		return this.binder;
	}

	public void setBinder(AnnotateDataBinder binder) {
		this.binder = binder;
	}

	public void setMpegawaiService(MpegawaiService mpegawaiService) {
		this.mpegawaiService = mpegawaiService;
	}

	public MpegawaiService getMpegawaiService() {
		return this.mpegawaiService;
	}

	public void setMpegawaiMainCtrl(MpegawaiMainCtrl mpegawaiMainCtrl) {
		this.mpegawaiMainCtrl = mpegawaiMainCtrl;
	}

	public MpegawaiMainCtrl getMpegawaiMainCtrl() {
		return this.mpegawaiMainCtrl;
	}

}
