package id.ac.idu.webui.administrasi.mahasiswa;

import id.ac.idu.administrasi.service.KodePosService;
import id.ac.idu.administrasi.service.MahasiswaService;
import id.ac.idu.backend.model.MkodePos;
import id.ac.idu.backend.model.Mmahasiswa;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.util.Codec;
import id.ac.idu.webui.administrasi.mahasiswa.model.OrderSearchKodeposList;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.GFCListModelCtrl;
import id.ac.idu.webui.util.pagging.PagedListWrapper;
import id.ac.idu.webui.util.test.EnumConverter;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.*;

import java.io.Serializable;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 11 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MahasiswaPribadiCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(MahasiswaDetailCtrl.class);

    protected Window windowPribadiDetail;
    protected Borderlayout borderlayout_Pribadi;
    protected Listbox txtb_cjnsmhs;
    protected Listbox txtb_cjenkel;
    protected Listbox txtb_cgoldar;
    protected Listbox txtb_ckdagama;
    protected Listbox txtb_cwarga;
    protected Listbox txtb_cstatnkh;
    protected Intbox txtb_kodeposid;
    protected Textbox txtb_kodepos;
    protected Intbox txtb_kodepos_srtid;
    protected Textbox txtb_kodepos_srt;

    private transient PagedListWrapper<MkodePos> plwKodepos;
    protected Paging paging_kodepos;
    protected Listbox listKodepos;
    protected Paging paging_kodepos_srt;
    protected Listbox listKodepos_srt;

    protected Textbox txtb_cnim;
    protected Textbox txtb_cnama ;
    protected Textbox txtb_noktp ;
    protected Textbox txtb_ctemplhr;
    protected Textbox txtb_calamat ;
    protected Textbox txtb_cnormh;
    protected Textbox txtb_crw ;
    protected Textbox txtb_crt ;
    protected Textbox txtb_cnotelp ;
    protected Textbox txtb_calamatsrt;
    protected Textbox txtb_cnohp ;
    protected Textbox txtb_cemail;
    protected Textbox txtb_cgelombang;
    protected Datebox txtb_dtglhr;
    protected Bandbox cmb_cjnsmhs;
    protected Bandbox cmb_cjenkel;
    protected Bandbox cmb_cgoldar;
    protected Bandbox cmb_ckdagama;
    protected Bandbox cmb_cstatnkh;
    protected Bandbox cmb_cwarga;
    protected Bandbox cmb_kodepos;
    protected Bandbox cmb_kodepos_srt;

    protected Textbox txth_cjnsmhs;

    private int pageSize;
    private String kodetype;

    protected transient AnnotateDataBinder binder;
    private transient MahasiswaService service;
    private transient KodePosService service2;

    private MahasiswaDetailCtrl detailCtrl;

    public MahasiswaPribadiCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        this.self.setAttribute("controller", this, false);

        if (arg.containsKey("ModuleMainController")) {
            setDetailCtrl((MahasiswaDetailCtrl) arg.get("ModuleMainController"));
            getDetailCtrl().setPribadiCtrl(this);

            if (getDetailCtrl().getSelected() != null) setSelected(getDetailCtrl().getSelected());
            else setSelected(null);
        } else setSelected(null);
    }
    
    @SuppressWarnings("unchecked")
    public void onCreate$windowPribadiDetail(Event event) throws Exception {
        setPageSize(20);
        
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);
        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.JenisMahasiswa.class)).getEnumToList(),
                txtb_cjnsmhs, cmb_cjnsmhs, (getSelected() != null)?getSelected().getCjnsmhs():null);
        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.JenisKelamin.class)).getEnumToList()
                , txtb_cjenkel, cmb_cjenkel, (getSelected() != null)?getSelected().getCjenkel():null);
        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.GolDarah.class)).getEnumToList()
                , txtb_cgoldar, cmb_cgoldar, (getSelected() != null)?getSelected().getCgoldar():null);
        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.Agama.class)).getEnumToList()
                , txtb_ckdagama, cmb_ckdagama, (getSelected() != null)?getSelected().getCkdagama():null);
        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.StatusNikah.class)).getEnumToList()
                , txtb_cstatnkh, cmb_cstatnkh, (getSelected() != null)?getSelected().getCstatnkh():null);
        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.WargaNegara.class)).getEnumToList()
                , txtb_cwarga, cmb_cwarga, (getSelected() != null)?getSelected().getCwarga():null);
        
        binder.loadAll();

        doFitSize(event);
    }

    public void onOpen$cmb_kodepos(Event event) {
        this.kodetype = "_1";
        HibernateSearchObject<MkodePos> soKodepos = new HibernateSearchObject<MkodePos>(MkodePos.class);
		soKodepos.addSort("kodepos", false);

		paging_kodepos.setPageSize(pageSize);
		paging_kodepos.setDetailed(true);
		getPlwKodepos().init(soKodepos, listKodepos, paging_kodepos);
		listKodepos.setItemRenderer(new OrderSearchKodeposList());
    }

    public void onOpen$cmb_kodepos_srt(Event event) {
        this.kodetype = "_2";
        HibernateSearchObject<MkodePos> soKodepos = new HibernateSearchObject<MkodePos>(MkodePos.class);
		soKodepos.addSort("kodepos", false);

		paging_kodepos_srt.setPageSize(pageSize);
		paging_kodepos_srt.setDetailed(true);
		getPlwKodepos().init(soKodepos, listKodepos_srt, paging_kodepos_srt);
		listKodepos_srt.setItemRenderer(new OrderSearchKodeposList());
    }

    public void onClick$button_close(Event event) {
        cmb_kodepos.close();
	}

    public void onClick$button_close_srt(Event event) {
        cmb_kodepos_srt.close();
	}

    public void onKodeposItem(Event event) {
        Listitem item;
        if(this.kodetype.equals("_1")) item = listKodepos.getSelectedItem();
        else item = listKodepos_srt.getSelectedItem();
        
        if (item != null) {
            MkodePos aKodepos = (MkodePos) item.getAttribute("data");
            getDetailCtrl().setKodepos(aKodepos);

            if(this.kodetype.equals("_1")) {
                txtb_kodeposid.setValue(Integer.valueOf((getDetailCtrl().getKodepos().getId()).trim()));
                txtb_kodepos.setValue(getDetailCtrl().getKodepos().getKodepos());
            }
            else {
                txtb_kodepos_srtid.setValue(Integer.valueOf((getDetailCtrl().getKodepos().getId()).trim()));
                txtb_kodepos_srt.setValue(getDetailCtrl().getKodepos().getKodepos());
            }
        }

        if(this.kodetype.equals("_1")) cmb_kodepos.close();
        else cmb_kodepos_srt.close();
    }

    public void onChange$onChanging(Event event) {
        try {
            Messagebox.show("test");
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void doFitSize(Event event) {
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
        final int maxListBoxHeight = height - 243;
        borderlayout_Pribadi.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowPribadiDetail.invalidate();
    }

    public void doReadOnlyMode(boolean b) {
        txtb_cnim.setReadonly(b);
        txtb_cnama.setReadonly(b);
        txtb_noktp.setReadonly(b);
        txtb_ctemplhr.setReadonly(b);
        txtb_calamat.setReadonly(b);
        txtb_cnormh.setReadonly(b);
        txtb_crw.setReadonly(b);
        txtb_crt.setReadonly(b);
        txtb_cnotelp.setReadonly(b);
        txtb_calamatsrt.setReadonly(b);
        txtb_cnohp.setReadonly(b);
        txtb_cemail.setReadonly(b);
        txtb_cgelombang.setReadonly(b);
        txtb_dtglhr.setDisabled(b);
        cmb_cjnsmhs.setDisabled(b);
        cmb_cjenkel.setDisabled(b);
        cmb_cgoldar.setDisabled(b);
        cmb_ckdagama.setDisabled(b);
        cmb_cstatnkh.setDisabled(b);
        cmb_cwarga.setDisabled(b);
        cmb_kodepos.setDisabled(b);
        cmb_kodepos_srt.setDisabled(b);
    }

    public MahasiswaService getService() {
        return service;
    }


    public void setService(MahasiswaService service) {
        this.service = service;
    }

    public KodePosService getService2() {
        return service2;
    }

    public void setService2(KodePosService service2) {
        this.service2 = service2;
    }

    public MahasiswaDetailCtrl getDetailCtrl() {
        return detailCtrl;
    }

    public void setDetailCtrl(MahasiswaDetailCtrl detailCtrl) {
        this.detailCtrl = detailCtrl;
    }

    public Listbox getTxtb_cgoldar() {
        return txtb_cgoldar;
    }

    public void setTxtb_cgoldar(Listbox txtb_cgoldar) {
        this.txtb_cgoldar = txtb_cgoldar;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public PagedListWrapper<MkodePos> getPlwKodepos() {
        return plwKodepos;
    }

    public void setPlwKodepos(PagedListWrapper<MkodePos> plwKodepos) {
        this.plwKodepos = plwKodepos;
    }

    public AnnotateDataBinder getBinder() {
        return binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public Mmahasiswa getSelected() {
        return getDetailCtrl().getSelected();
    }

    public void setSelected(Mmahasiswa selected) {
        getDetailCtrl().setSelected(selected);
    }

    public Textbox getTxth_cjnsmhs() {
        return txth_cjnsmhs;
    }
}
