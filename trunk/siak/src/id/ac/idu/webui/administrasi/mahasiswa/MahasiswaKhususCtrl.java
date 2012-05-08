package id.ac.idu.webui.administrasi.mahasiswa;

import com.trg.search.Filter;
import id.ac.idu.backend.model.*;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.webui.administrasi.mahasiswa.model.KhususSearchHisPangkatList;
import id.ac.idu.webui.administrasi.mahasiswa.model.KhususSearchPendidikanList;
import id.ac.idu.webui.administrasi.mahasiswa.model.KhususSearchJabatanList;
import id.ac.idu.webui.administrasi.mahasiswa.model.KhususSearchPangkatList;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.ZksampleMessageUtils;
import id.ac.idu.webui.util.pagging.PagedListWrapper;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.*;
import org.zkoss.zul.api.Bandbox;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 21 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MahasiswaKhususCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(MahasiswaKhususCtrl.class);

    public static final String DATA = "riwayat";
    public static final String LIST = "listRiwayatPendidikan";
    public static final String KHUSUS_CONTROL = "riwayatPendidikanCtrl";
    public static final String PANGKAT_CONTROL = "riwayatPangkatCtrl";
    public static final String ISNEW = "isnew";
    public static final String LIST_DATA = "data";

    private int pageSize;
    private int mhsid;

    protected Window windowKhususDetail;
    protected Borderlayout borderKhusus;
    
    protected Textbox txtbNip;
    protected Textbox txtbKesatuan;

    /* Lov Object */
    private transient PagedListWrapper<Mpangkatgolongan> plwPangkat;
    protected Bandbox cmbPangkat;
    protected Paging pagingPangkat;
    protected Listbox listPangkat;
    protected Textbox txtbPangkat;
    protected Textbox tbPangkat;
    protected Textbox tbName;

    private transient PagedListWrapper<Mjabatan> plwJabatan;
    protected Bandbox cmbJabatan;
    protected Paging pagingJabatan;
    protected Listbox listJabatan;
    protected Textbox txtbJabatan;
    protected Textbox tbJabatan;
    
    protected Button btnNewRiwayatPendidikan;
    protected Button btnDeleteRiwayatPendidikan;
    protected Button btnNewRiwayatPangkat;
    protected Button btnDeleteRiwayatPangkat;

    private transient PagedListWrapper<Mppumhskhusus> plwKhusus;
    private RiwayatPendidikanCtrl riwayatPendidikanCtrl;
    protected Paging pagingRiwayatPendidikan;
    protected Listbox listRiwayatPendidikan;

    protected Listheader jenis;
    protected Listheader tahun;
    protected Listheader cketerangan;


    private transient PagedListWrapper<Mhistpangkatmhs> plwHisPangkat;
    private RiwayatPangkatCtrl riwayatPangkatCtrl;
    protected Paging pagingRiwayatPangkat;
    protected Listbox listRiwayatPangkat;

    protected Listheader cdpangkat;
    protected Listheader nmpangkat;
    protected Listheader ctmt;
    protected Listheader dketerangan;

    protected Listheader code;
    protected Listheader name;
    protected Listheader jabatan;

    private MahasiswaDetailCtrl detailCtrl;
    private AnnotateDataBinder binder;

    protected Set<Mppumhskhusus> delMppum = new HashSet<Mppumhskhusus>();
    protected Set<Mhistpangkatmhs> delHisPangkat = new HashSet<Mhistpangkatmhs>();

    public MahasiswaKhususCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        logger.info("Page Khusus Makasiswa Loaded");

        super.doAfterCompose(window);
        self.setAttribute("controller", this, false);

        if(arg.containsKey("ModuleMainController")) {
            setDetailCtrl((MahasiswaDetailCtrl) arg.get("ModuleMainController"));
            getDetailCtrl().setKhususCtrl(this);
        }
    }

    @SuppressWarnings("unchecked")
    public void onCreate$windowKhususDetail(Event event) throws Exception {
        setPageSize(20);
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doReadOnlyMode(!getDetailCtrl().getMainCtrl().btnSave.isVisible());
        if(getMahasiswa() != null) {
            loadRiwayatPendidikan();
            loadRiwayatPangkat();
        }
        
        binder.loadAll();
        this.doFitSize();
    }

    public void loadRiwayatPendidikan() {
        this.mhsid = getMahasiswa().getId();

        jenis.setSortAscending(new FieldComparator("cnmbentuk",true));
        jenis.setSortDescending(new FieldComparator("cnmbentuk",false));
        tahun.setSortAscending(new FieldComparator("ctahun",true));
        tahun.setSortDescending(new FieldComparator("ctahun",false));
        cketerangan.setSortAscending(new FieldComparator("cket",true));
        cketerangan.setSortDescending(new FieldComparator("cket",false));
        
        HibernateSearchObject<Mppumhskhusus> hisKhusus = new HibernateSearchObject<Mppumhskhusus>(Mppumhskhusus.class);
        if(getMahasiswa() != null)
            hisKhusus.addFilter(new Filter("mahasiswaId", this.mhsid, Filter.OP_EQUAL));
        
        hisKhusus.addSort("cno", false);
        
        pagingRiwayatPendidikan.setPageSize(pageSize);
		pagingRiwayatPendidikan.setDetailed(true);
		getPlwKhusus().init(hisKhusus, listRiwayatPendidikan, pagingRiwayatPendidikan);
		listRiwayatPendidikan.setItemRenderer(new KhususSearchPendidikanList());
    }

    public void onRiwayatPendidikan(Event event) throws Exception {
        Listitem item = listRiwayatPendidikan.getSelectedItem();
        if(item == null) return;

        final Mppumhskhusus hisKhusus = (Mppumhskhusus) item.getAttribute(MahasiswaKhususCtrl.LIST_DATA);
        this.showDetailRiwayatPendidikan(hisKhusus, false);
    }

    public void onClick$btnNewRiwayatPendidikan() throws Exception {
        final Mppumhskhusus mppumhskhusus = getDetailCtrl().getMainCtrl().getMppumhskhususService().getNew();
        this.showDetailRiwayatPendidikan(mppumhskhusus, true);
    }

    public void onClick$btnDeleteRiwayatPendidikan() throws Exception {
        Listitem item = listRiwayatPendidikan.getSelectedItem();
        if(item == null) return;
        
        getDelMppum().add((Mppumhskhusus) item.getAttribute(MahasiswaKhususCtrl.LIST_DATA));
        listRiwayatPendidikan.removeItemAt(listRiwayatPendidikan.getSelectedIndex());
    }

    public void showDetailRiwayatPendidikan(Mppumhskhusus mppumhskhusus, boolean isnew) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(MahasiswaKhususCtrl.DATA, mppumhskhusus);
        map.put(MahasiswaKhususCtrl.LIST, listRiwayatPendidikan);
        map.put(MahasiswaKhususCtrl.KHUSUS_CONTROL, this);
        map.put(MahasiswaKhususCtrl.ISNEW, isnew);

        try {
            Executions.createComponents("/WEB-INF/pages/administrasi/mahasiswa/pageRiwayatPendidikan.zul", null, map);
        } catch (final Exception e) {
            logger.error("onOpenWindow:: error opening window / " + e.getMessage());
            ZksampleMessageUtils.showErrorMessage(e.toString());
        }
    }

    public void loadRiwayatPangkat() {
        cdpangkat.setSortAscending(new FieldComparator("mpangkatgolongan.ckdpangkatgolongan",true));
        cdpangkat.setSortDescending(new FieldComparator("mpangkatgolongan.ckdpangkatgolongan",false));
        nmpangkat.setSortAscending(new FieldComparator("mpangkatgolongan.cnmpangkatgolongan",true));
        nmpangkat.setSortDescending(new FieldComparator("mpangkatgolongan.cnmpangkatgolongan",false));
        ctmt.setSortAscending(new FieldComparator("ctmt",true));
        ctmt.setSortDescending(new FieldComparator("ctmt",false));
        dketerangan.setSortAscending(new FieldComparator("cket",true));
        dketerangan.setSortDescending(new FieldComparator("cket",false));

        HibernateSearchObject<Mhistpangkatmhs> hisPangkat = new HibernateSearchObject<Mhistpangkatmhs>(Mhistpangkatmhs.class);
        if(getMahasiswa() != null)
            hisPangkat.addFilter(new Filter("mahasiswaId", this.mhsid, Filter.OP_EQUAL));

        hisPangkat.addSort("id", false);

        pagingRiwayatPangkat.setPageSize(pageSize);
		pagingRiwayatPangkat.setDetailed(true);
		getPlwHisPangkat().init(hisPangkat, listRiwayatPangkat, pagingRiwayatPangkat);
		listRiwayatPangkat.setItemRenderer(new KhususSearchHisPangkatList());
    }

    public void onRiwayatPangkat(Event event) throws Exception {
        Listitem item = listRiwayatPangkat.getSelectedItem();
        if(item == null) return;

        final Mhistpangkatmhs hisPangkat = (Mhistpangkatmhs) item.getAttribute(MahasiswaKhususCtrl.LIST_DATA);
        this.showDetailRiwayatPangkat(hisPangkat, false);
    }

    public void onClick$btnNewRiwayatPangkat() throws Exception {
        final Mhistpangkatmhs mhistpangkatmhs = getDetailCtrl().getMainCtrl().getMhistpangkatmhsService().getNew();
        this.showDetailRiwayatPangkat(mhistpangkatmhs, true);
    }

    public void onClick$btnDeleteRiwayatPangkat() throws Exception {
        Listitem item = listRiwayatPangkat.getSelectedItem();
        if(item == null) return;
        
        getDelHisPangkat().add((Mhistpangkatmhs) item.getAttribute(MahasiswaKhususCtrl.LIST_DATA));
        listRiwayatPangkat.removeItemAt(listRiwayatPangkat.getSelectedIndex());
    }

    public void showDetailRiwayatPangkat(Mhistpangkatmhs mhistpangkatmhs, boolean isnew) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(MahasiswaKhususCtrl.DATA, mhistpangkatmhs);
        map.put(MahasiswaKhususCtrl.LIST, listRiwayatPangkat);
        map.put(MahasiswaKhususCtrl.PANGKAT_CONTROL, this);
        map.put(MahasiswaKhususCtrl.ISNEW, isnew);

        try {
            Executions.createComponents("/WEB-INF/pages/administrasi/mahasiswa/pageRiwayatPangkat.zul", null, map);
        } catch (final Exception e) {
            logger.error("onOpenWindow:: error opening window / " + e.getMessage());
            ZksampleMessageUtils.showErrorMessage(e.toString());
        }
    }

    public void onOpen$cmbPangkat(Event event) {
        this.searchPangkat(null, null);
    }

    public void onClick$buttonSearchPkt(Event event) {
        Filter filter1 = null;
        Filter filter2 = null;

        if (StringUtils.isNotEmpty(tbPangkat.getValue()))
            filter1 = new Filter("ckdpangkatgolongan", "%" + tbPangkat.getValue() + "%", Filter.OP_ILIKE);

        if (StringUtils.isNotEmpty(tbName.getValue()))
            filter2 = new Filter("cnmpangkatgolongan", "%" + tbPangkat.getValue() + "%", Filter.OP_ILIKE);

        this.searchPangkat(filter1, filter2);
    }

    private void searchPangkat(Filter filter1, Filter filter2) {
        HibernateSearchObject<Mpangkatgolongan> soPangkat = new HibernateSearchObject<Mpangkatgolongan>(Mpangkatgolongan.class);

        code.setSortAscending(new FieldComparator("ckdpangkatgolongan",true));
        code.setSortDescending(new FieldComparator("ckdpangkatgolongan",false));
        name.setSortAscending(new FieldComparator("cnmpangkatgolongan",true));
        name.setSortDescending(new FieldComparator("cnmpangkatgolongan",false));

        if(filter1 != null) soPangkat.addFilter(filter1);
        if(filter2 != null) soPangkat.addFilter(filter2);

        soPangkat.addSort("ckdpangkatgolongan", false);
        pagingPangkat.setPageSize(pageSize);
		pagingPangkat.setDetailed(true);
		getPlwPangkat().init(soPangkat, listPangkat, pagingPangkat);
		listPangkat.setItemRenderer(new KhususSearchPangkatList());
    }

    public void onPangkatItem(Event event) {
        Listitem item = listPangkat.getSelectedItem();

        if (item != null) {
            Mpangkatgolongan aPangkat = (Mpangkatgolongan) item.getAttribute(MahasiswaKhususCtrl.LIST_DATA);
            txtbPangkat.setValue(aPangkat.getCnmpangkatgolongan());
            getMahasiswa().getMmhspascakhs().setMpangkatgolongan(aPangkat);
        }

        cmbPangkat.close();
    }

    public void onClick$buttonClosePkt(Event event) {
        cmbPangkat.close();
	}

    public void onOpen$cmbJabatan(Event event) {
        this.searchJabatan(null);
    }

    public void onClick$buttonSearchJbt(Event event) {
        if (StringUtils.isNotEmpty(tbJabatan.getValue()))
            this.searchJabatan(new Filter("cnmjabatan", "%" + tbJabatan.getValue() + "%", Filter.OP_ILIKE));
        else
            this.searchJabatan(null);
    }

    private void searchJabatan(Filter filter) {
        HibernateSearchObject<Mjabatan> soJabatan = new HibernateSearchObject<Mjabatan>(Mjabatan.class);

        jabatan.setSortAscending(new FieldComparator("cnmjabatan",true));
        jabatan.setSortDescending(new FieldComparator("cnmjabatan",false));
        
        if(filter != null) soJabatan.addFilter(filter);
        
		soJabatan.addSort("ckdjabatan", false);

        pagingJabatan.setPageSize(pageSize);
		pagingJabatan.setDetailed(true);
		getPlwJabatan().init(soJabatan, listJabatan, pagingJabatan);
		listJabatan.setItemRenderer(new KhususSearchJabatanList());
    }

    public void onJabatanItem(Event event) {
        Listitem item = listJabatan.getSelectedItem();

        if (item != null) {
            Mjabatan aJabatan = (Mjabatan) item.getAttribute(MahasiswaKhususCtrl.LIST_DATA);
            txtbJabatan.setValue(aJabatan.getCnmjabatan());
            getMahasiswa().getMmhspascakhs().setMjabatan(aJabatan);
        }

        cmbJabatan.close();
    }

    public void onClick$buttonCloseJbt(Event event) {
        cmbJabatan.close();
	}

    public void doFitSize() {
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue();
        final int maxListBoxHeight = height - 277;

        borderKhusus.setHeight(String.valueOf(maxListBoxHeight) + "px");
        windowKhususDetail.invalidate();
    }

    public void doReadOnlyMode(boolean b) {
        txtbNip.setReadonly(b);
        cmbPangkat.setDisabled(b);
        cmbJabatan.setDisabled(b);
        txtbKesatuan.setReadonly(b);

        btnNewRiwayatPendidikan.setDisabled(b);
        btnDeleteRiwayatPendidikan.setDisabled(b);
        btnNewRiwayatPangkat.setDisabled(b);
        btnDeleteRiwayatPangkat.setDisabled(b);
    }

    public void reLoadPage() {
        binder.loadAll();
        this.doFitSize();
        
        if(this.mhsid == getMahasiswa().getId())
            return;

        loadRiwayatPendidikan();
        loadRiwayatPangkat();
    }

    public boolean isModeEdit() {
        return (getDetailCtrl().getMainCtrl().btnSave.isVisible());
    }

    public MahasiswaDetailCtrl getDetailCtrl() {
        return detailCtrl;
    }

    public void setDetailCtrl(MahasiswaDetailCtrl detailCtrl) {
        this.detailCtrl = detailCtrl;
    }

    public RiwayatPendidikanCtrl getRiwayatPendidikanCtrl() {
        return riwayatPendidikanCtrl;
    }

    public void setRiwayatPendidikanCtrl(RiwayatPendidikanCtrl riwayatPendidikanCtrl) {
        this.riwayatPendidikanCtrl = riwayatPendidikanCtrl;
    }

    public RiwayatPangkatCtrl getRiwayatPangkatCtrl() {
        return riwayatPangkatCtrl;
    }

    public void setRiwayatPangkatCtrl(RiwayatPangkatCtrl riwayatPangkatCtrl) {
        this.riwayatPangkatCtrl = riwayatPangkatCtrl;
    }

    public AnnotateDataBinder getBinder() {
        return binder;
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

    public PagedListWrapper<Mpangkatgolongan> getPlwPangkat() {
        return plwPangkat;
    }

    public void setPlwPangkat(PagedListWrapper<Mpangkatgolongan> plwPangkat) {
        this.plwPangkat = plwPangkat;
    }

    public PagedListWrapper<Mjabatan> getPlwJabatan() {
        return plwJabatan;
    }

    public void setPlwJabatan(PagedListWrapper<Mjabatan> plwJabatan) {
        this.plwJabatan = plwJabatan;
    }

    public PagedListWrapper<Mppumhskhusus> getPlwKhusus() {
        return plwKhusus;
    }

    public void setPlwKhusus(PagedListWrapper<Mppumhskhusus> plwKhusus) {
        this.plwKhusus = plwKhusus;
    }

    public PagedListWrapper<Mhistpangkatmhs> getPlwHisPangkat() {
        return plwHisPangkat;
    }

    public void setPlwHisPangkat(PagedListWrapper<Mhistpangkatmhs> plwHisPangkat) {
        this.plwHisPangkat = plwHisPangkat;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Listbox getListRiwayatPendidikan() {
        return listRiwayatPendidikan;
    }

    public void setListRiwayatPendidikan(Listbox listRiwayatPendidikan) {
        this.listRiwayatPendidikan = listRiwayatPendidikan;
    }

    public Listbox getListRiwayatPangkat() {
        return listRiwayatPangkat;
    }

    public void setListRiwayatPangkat(Listbox listRiwayatPangkat) {
        this.listRiwayatPangkat = listRiwayatPangkat;
    }

    public Set<Mppumhskhusus> getDelMppum() {
        return delMppum;
    }

    public void setDelMppum(Set<Mppumhskhusus> delMppum) {
        this.delMppum = delMppum;
    }

    public Set<Mhistpangkatmhs> getDelHisPangkat() {
        return delHisPangkat;
    }

    public void setDelHisPangkat(Set<Mhistpangkatmhs> delHisPangkat) {
        this.delHisPangkat = delHisPangkat;
    }
}
