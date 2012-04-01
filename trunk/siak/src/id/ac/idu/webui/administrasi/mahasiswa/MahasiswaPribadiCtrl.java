package id.ac.idu.webui.administrasi.mahasiswa;

import com.trg.search.Filter;
import id.ac.idu.backend.model.MkodePos;
import id.ac.idu.backend.model.Mmahasiswa;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.util.Codec;
import id.ac.idu.webui.administrasi.mahasiswa.model.PribadiSearchKodeposList;
import id.ac.idu.webui.util.EnumConverter;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.GFCListModelCtrl;
import id.ac.idu.webui.util.pagging.PagedListWrapper;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.*;

import java.io.Serializable;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 17 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MahasiswaPribadiCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(MahasiswaPribadiCtrl.class);

    protected transient AnnotateDataBinder binder;

    protected Window windowPribadiDetail;
    protected Borderlayout borderPribadi;
    private MahasiswaDetailCtrl detailCtrl;
    private int pageSize;


    /* Input Form */
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

    /* Combo Box */
    protected Listbox txtb_cjnsmhs;
    protected Listbox txtb_cjenkel;
    protected Listbox txtb_cgoldar;
    protected Listbox txtb_ckdagama;
    protected Listbox txtb_cwarga;
    protected Listbox txtb_cstatnkh;
    
    private String kodetype;

    /* Kode Pos */
    private transient PagedListWrapper<MkodePos> plwKodepos;
    protected Paging paging_kodepos;
    protected Listbox listKodepos;
    protected Paging paging_kodepos_srt;
    protected Listbox listKodepos_srt;
    protected Listheader kodepos;

    protected Textbox txtb_kodepos;
    protected Textbox tb_kodepos;
    protected Textbox txtb_kodepos_srt;
    protected Textbox tb_kodepos_srt;
    protected Listheader kodepos_srt;

    public MahasiswaPribadiCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        logger.info("Page Pribadi Makasiswa Loaded");

        super.doAfterCompose(window);
        self.setAttribute("controller", this, false);

        if(arg.containsKey("ModuleMainController")) {
            setDetailCtrl((MahasiswaDetailCtrl) arg.get("ModuleMainController"));
            getDetailCtrl().setPribadiCtrl(this);
        }
        else setMahasiswa(null);
    }

    @SuppressWarnings("unchecked")
    public void onCreate$windowPribadiDetail(Event event) throws Exception {
        setPageSize(20);

        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.JenisMahasiswa.class)).getEnumToList()
                ,txtb_cjnsmhs, cmb_cjnsmhs, (getMahasiswa() != null)?getMahasiswa().getCjnsmhs():null);
        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.JenisKelamin.class)).getEnumToList()
                , txtb_cjenkel, cmb_cjenkel, (getMahasiswa() != null)?getMahasiswa().getCjenkel():null);
        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.GolDarah.class)).getEnumToList()
                , txtb_cgoldar, cmb_cgoldar, (getMahasiswa() != null)?getMahasiswa().getCgoldar():null);
        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.Agama.class)).getEnumToList()
                , txtb_ckdagama, cmb_ckdagama, (getMahasiswa() != null)?getMahasiswa().getCkdagama():null);
        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.StatusNikah.class)).getEnumToList()
                , txtb_cstatnkh, cmb_cstatnkh, (getMahasiswa() != null)?getMahasiswa().getCstatnkh():null);
        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.WargaNegara.class)).getEnumToList()
                , txtb_cwarga, cmb_cwarga, (getMahasiswa() != null)?getMahasiswa().getCwarga():null);

        binder.loadAll();
        this.doFitSize();
        this.doTabKhusus();
        doReadOnlyMode(!getDetailCtrl().getMainCtrl().btnSave.isVisible());
    }

    public void onOpen$cmb_kodepos(Event event) {
        kodetype = "_1";
        this.searchKodepos(null);
    }

    public void onClick$buttonSearch(Event event) {
        if (StringUtils.isNotEmpty(tb_kodepos.getValue()))
            this.searchKodepos(new Filter("kodepos", "%" + tb_kodepos.getValue() + "%", Filter.OP_LIKE));
        else
            this.searchKodepos(null);
    }

    public void searchKodepos(Filter filter) {
        HibernateSearchObject<MkodePos> soKodepos = new HibernateSearchObject<MkodePos>(MkodePos.class);

        kodepos.setSortAscending(new FieldComparator("kodepos",true));
        kodepos.setSortDescending(new FieldComparator("kodepos",false));

        if(filter != null) soKodepos.addFilter(filter);
		soKodepos.addSort("kodepos", false);

		paging_kodepos.setPageSize(pageSize);
		paging_kodepos.setDetailed(true);
		getPlwKodepos().init(soKodepos, listKodepos, paging_kodepos);
		listKodepos.setItemRenderer(new PribadiSearchKodeposList());
    }

    public void onClick$button_close(Event event) {
        cmb_kodepos.close();
	}

    public void onOpen$cmb_kodepos_srt(Event event) {
        kodetype = "_2";
        this.searchKodeposSrt(null);
    }

    public void onClick$buttonSearchSrt(Event event) {
        if (StringUtils.isNotEmpty(tb_kodepos_srt.getValue()))
            this.searchKodeposSrt(new Filter("kodepos", "%" + tb_kodepos_srt.getValue() + "%", Filter.OP_LIKE));
        else
            this.searchKodeposSrt(null);
    }

    public void searchKodeposSrt(Filter filter) {
        HibernateSearchObject<MkodePos> soKodepos = new HibernateSearchObject<MkodePos>(MkodePos.class);

        kodepos_srt.setSortAscending(new FieldComparator("kodepos",true));
        kodepos_srt.setSortDescending(new FieldComparator("kodepos",false));

        if(filter != null) soKodepos.addFilter(filter);
		soKodepos.addSort("kodepos", false);

		paging_kodepos_srt.setPageSize(pageSize);
		paging_kodepos_srt.setDetailed(true);
		getPlwKodepos().init(soKodepos, listKodepos_srt, paging_kodepos_srt);
		listKodepos_srt.setItemRenderer(new PribadiSearchKodeposList());
    }

    public void onClick$button_close_srt(Event event) {
        cmb_kodepos_srt.close();
	}

    public void onKodeposItem(Event event) {
        Listitem item;
        if(kodetype.equals("_1")) item = listKodepos.getSelectedItem();
        else item = listKodepos_srt.getSelectedItem();

        if (item != null) {
            MkodePos aKodepos = (MkodePos) item.getAttribute("data");
            
            if(kodetype.equals("_1")) {
                getDetailCtrl().getMainCtrl().getMahasiswa().setKodeposId(aKodepos);
                txtb_kodepos.setValue(aKodepos.getKodepos());
            }
            else {
                getDetailCtrl().getMainCtrl().getMahasiswa().setKodeposSrtId(aKodepos);
                txtb_kodepos_srt.setValue(aKodepos.getKodepos());
            }
        }

        if(kodetype.equals("_1")) cmb_kodepos.close();
        else cmb_kodepos_srt.close();
    }

    public void onSelect$txtb_cjnsmhs(Event event) {
        cmb_cjnsmhs.setValue(txtb_cjnsmhs.getSelectedItem().getLabel());
        getMahasiswa().setCjnsmhs((String) txtb_cjnsmhs.getSelectedItem().getValue());
        cmb_cjnsmhs.close();
        this.doTabKhusus();
    }

    public void onSelect$txtb_cjenkel(Event event) {
        cmb_cjenkel.setValue(txtb_cjenkel.getSelectedItem().getLabel());
        getMahasiswa().setCjenkel(((String)txtb_cjenkel.getSelectedItem().getValue()).charAt(0));
        cmb_cjenkel.close();
    }

    public void onSelect$txtb_cgoldar(Event event) {
        cmb_cgoldar.setValue(txtb_cgoldar.getSelectedItem().getLabel());
        getMahasiswa().setCgoldar(((String)txtb_cgoldar.getSelectedItem().getValue()).charAt(0));
        cmb_cgoldar.close();
    }

    public void onSelect$txtb_ckdagama(Event event) {
        cmb_ckdagama.setValue(txtb_ckdagama.getSelectedItem().getLabel());
        getMahasiswa().setCkdagama((String) txtb_ckdagama.getSelectedItem().getValue());
        cmb_ckdagama.close();
    }

    public void onSelect$txtb_cstatnkh(Event event) {
        cmb_cstatnkh.setValue(txtb_cstatnkh.getSelectedItem().getLabel());
        getMahasiswa().setCstatnkh(((String)txtb_cstatnkh.getSelectedItem().getValue()).charAt(0));
        cmb_cstatnkh.close();
    }

    public void onSelect$txtb_cwarga(Event event) {
        cmb_cwarga.setValue(txtb_cwarga.getSelectedItem().getLabel());
        getMahasiswa().setCwarga(((String)txtb_cwarga.getSelectedItem().getValue()).charAt(0));
        cmb_cwarga.close();
    }

    private void doFitSize() {
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue();
        final int maxListBoxHeight = height - 277;

        borderPribadi.setHeight(String.valueOf(maxListBoxHeight) + "px");
        windowPribadiDetail.invalidate();
    }

    private void doTabKhusus() {
        if(getMahasiswa() == null) return;

        if(getMahasiswa().getCjnsmhs() == null) {
            getDetailCtrl().tabKhusus.setVisible(false);
            return;
        }

        if(getMahasiswa().getCjnsmhs().equals(Codec.JenisMahasiswa.JenMhs2.getValue())) {
            getDetailCtrl().tabKhusus.setVisible(true);
            if(getDetailCtrl().tabPanelKhusus.getChildren().size() > 0)
                getDetailCtrl().getKhususCtrl().doFitSize();
        }
        else
            getDetailCtrl().tabKhusus.setVisible(false);
    }

    public void doReset() {
        GFCListModelCtrl.getInstance().doCheckBox(txtb_cjnsmhs, cmb_cjnsmhs
                , (getMahasiswa()!=null)?String.valueOf(getMahasiswa().getCjnsmhs()):null);
        GFCListModelCtrl.getInstance().doCheckBox(txtb_cjenkel, cmb_cjenkel
                , (getMahasiswa()!=null)?String.valueOf(getMahasiswa().getCjenkel()):null);
        GFCListModelCtrl.getInstance().doCheckBox(txtb_cgoldar, cmb_cgoldar
                , (getMahasiswa()!=null)?String.valueOf(getMahasiswa().getCgoldar()):null);
        GFCListModelCtrl.getInstance().doCheckBox(txtb_ckdagama, cmb_ckdagama
                , (getMahasiswa()!=null)?String.valueOf(getMahasiswa().getCkdagama()):null);
        GFCListModelCtrl.getInstance().doCheckBox(txtb_cstatnkh, cmb_cstatnkh
                , (getMahasiswa()!=null)?String.valueOf(getMahasiswa().getCstatnkh()):null);
        GFCListModelCtrl.getInstance().doCheckBox(txtb_cwarga, cmb_cwarga
                , (getMahasiswa()!=null)?String.valueOf(getMahasiswa().getCwarga()):null);

        this.doTabKhusus();
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

    public void reLoadPage() {
        binder.loadAll();
        this.doReset();
    }

    public MahasiswaDetailCtrl getDetailCtrl() {
        return detailCtrl;
    }

    public void setDetailCtrl(MahasiswaDetailCtrl detailCtrl) {
        this.detailCtrl = detailCtrl;
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public Mmahasiswa getMahasiswa() {
        return getDetailCtrl().getMainCtrl().getMahasiswa();
    }

    public void setMahasiswa(Mmahasiswa _obj) {
        getDetailCtrl().getMainCtrl().setMahasiswa(_obj);
    }

    public PagedListWrapper<MkodePos> getPlwKodepos() {
        return plwKodepos;
    }

    public void setPlwKodepos(PagedListWrapper<MkodePos> plwKodepos) {
        this.plwKodepos = plwKodepos;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
