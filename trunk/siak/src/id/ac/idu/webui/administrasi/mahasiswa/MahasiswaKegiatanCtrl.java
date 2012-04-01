package id.ac.idu.webui.administrasi.mahasiswa;

import com.trg.search.Filter;
import id.ac.idu.backend.model.Mkgtmhs;
import id.ac.idu.backend.model.Mmahasiswa;
import id.ac.idu.backend.model.Mkaryamhs;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.webui.administrasi.mahasiswa.model.KegiatanSearchKaryaList;
import id.ac.idu.webui.administrasi.mahasiswa.model.KegiatanSearchList;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.ZksampleMessageUtils;
import id.ac.idu.webui.util.pagging.PagedListWrapper;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 29 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MahasiswaKegiatanCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(MahasiswaKegiatanCtrl.class);

    protected transient AnnotateDataBinder binder;

    protected Window windowKgtIlmiahDetail;
    protected Borderlayout borderDetailKegiatan;

    private MahasiswaDetailCtrl detailCtrl;
    private int pageSize;

    private int mhsid;

    public static final String DATA = "data";
    public static final String LIST = "LIST";
    public static final String CONTROL = "CONTROL";
    public static final String ISNEW = "ISNEW";

    protected Textbox txtbctujuan;

    protected Button btnNewKegiatan;
    protected Button btnDeleteKegiatan;
    protected Button btnNewKarya;
    protected Button btnDeleteKarya;

    protected Set<Mkgtmhs> delKegiatan = new HashSet<Mkgtmhs>();
    private transient PagedListWrapper<Mkgtmhs> plwKegiatan;
    protected Listbox listKegiatan;
    protected Paging pagingKegiatan;

    protected Listheader cno;
    protected Listheader cnmsimposium;
    protected Listheader cperanan;
    protected Listheader ctahun;
    protected Listheader cinstitusi;

    protected Set<Mkaryamhs> delKarya = new HashSet<Mkaryamhs>();
    private transient PagedListWrapper<Mkaryamhs> plwKarya;
    protected Listbox listKarya;
    protected Paging pagingKarya;

    protected Listheader cnob;
    protected Listheader cnmkyrilm;
    protected Listheader cmediaterbit;
    protected Listheader cthnterbit;
    protected Listheader cket;

    protected KaryaCtrl karyaCtrl;
    protected KegiatanCtrl kegiatanCtrl;

    public MahasiswaKegiatanCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        logger.info("Page Pribadi Makasiswa Loaded");

        super.doAfterCompose(window);
        self.setAttribute("controller", this, false);

        if(arg.containsKey("ModuleMainController")) {
            setDetailCtrl((MahasiswaDetailCtrl) arg.get("ModuleMainController"));
            getDetailCtrl().setKegiatanCtrl(this);
        }
        else setMahasiswa(null);
    }

    @SuppressWarnings("unchecked")
    public void onCreate$windowKgtIlmiahDetail(Event event) throws Exception {
        setPageSize(20);

        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doReadOnlyMode(!getDetailCtrl().getMainCtrl().btnSave.isVisible());
        if(getMahasiswa() != null) {
            loadKegiatan();
            loadKarya();
        }

        binder.loadAll();
        this.doFitSize();
    }

    public void loadKegiatan() {
        this.mhsid = getMahasiswa().getId();

        cno.setSortAscending(new FieldComparator("cno",true));
        cno.setSortDescending(new FieldComparator("cno",false));
        cnmsimposium.setSortAscending(new FieldComparator("cnmsimposium",true));
        cnmsimposium.setSortDescending(new FieldComparator("cnmsimposium",false));
        cperanan.setSortAscending(new FieldComparator("cperanan",true));
        cperanan.setSortDescending(new FieldComparator("cperanan",false));
        ctahun.setSortAscending(new FieldComparator("ctahun",true));
        ctahun.setSortDescending(new FieldComparator("ctahun",false));
        cinstitusi.setSortAscending(new FieldComparator("cinstitusi",true));
        cinstitusi.setSortDescending(new FieldComparator("cinstitusi",false));

        HibernateSearchObject<Mkgtmhs> onKegiatan = new HibernateSearchObject<Mkgtmhs>(Mkgtmhs.class);
        if(getMahasiswa() != null)
            onKegiatan.addFilter(new Filter("mahasiswaId", this.mhsid, Filter.OP_EQUAL));

        onKegiatan.addSort("cno", false);

        pagingKegiatan.setPageSize(pageSize);
		pagingKegiatan.setDetailed(true);
		getPlwKegiatan().init(onKegiatan, listKegiatan, pagingKegiatan);
		listKegiatan.setItemRenderer(new KegiatanSearchList());
    }

    public void onClick$btnNewKegiatan(Event event) throws Exception {
        final Mkgtmhs mhistkegiatanmhs = getDetailCtrl().getMainCtrl().getMkgtmhsService().getNew();
        this.showDetailKegiatan(mhistkegiatanmhs, true);
    }

    public void onClick$btnDeleteKegiatan(Event event) throws Exception {
        Listitem item = listKegiatan.getSelectedItem();
        if(item == null) return;
        
        getDelKegiatan().add((Mkgtmhs) item.getAttribute(MahasiswaKegiatanCtrl.DATA));
        listKegiatan.removeItemAt(listKegiatan.getSelectedIndex());
    }

    public void onKegiatanItem(Event event) throws Exception {
        Listitem item = listKegiatan.getSelectedItem();
        if(item == null) return;

        final Mkgtmhs anKegiatan = (Mkgtmhs) item.getAttribute(MahasiswaKegiatanCtrl.DATA);
        this.showDetailKegiatan(anKegiatan, false);
    }

    public void showDetailKegiatan(Mkgtmhs mhistkegiatanmhs, boolean isnew) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(MahasiswaKegiatanCtrl.DATA, mhistkegiatanmhs);
        map.put(MahasiswaKegiatanCtrl.LIST, listKegiatan);
        map.put(MahasiswaKegiatanCtrl.CONTROL, this);
        map.put(MahasiswaKegiatanCtrl.ISNEW, isnew);

        try {
            Executions.createComponents("/WEB-INF/pages/administrasi/mahasiswa/pageKegiatanDtl.zul", null, map);
        } catch (final Exception e) {
            logger.error("onOpenWindow:: error opening window / " + e.getMessage());
            ZksampleMessageUtils.showErrorMessage(e.toString());
        }
    }

    public void loadKarya() {
        this.mhsid = getMahasiswa().getId();

        cnob.setSortAscending(new FieldComparator("cno",true));
        cnob.setSortDescending(new FieldComparator("cno",false));
        cnmkyrilm.setSortAscending(new FieldComparator("cnmkyrilm",true));
        cnmkyrilm.setSortDescending(new FieldComparator("cnmkyrilm",false));
        cmediaterbit.setSortAscending(new FieldComparator("cmediaterbit",true));
        cmediaterbit.setSortDescending(new FieldComparator("cmediaterbit",false));
        cthnterbit.setSortAscending(new FieldComparator("cthnterbit",true));
        cthnterbit.setSortDescending(new FieldComparator("cthnterbit",false));
        cket.setSortAscending(new FieldComparator("cket",true));
        cket.setSortDescending(new FieldComparator("cket",false));

        HibernateSearchObject<Mkaryamhs> onKarya = new HibernateSearchObject<Mkaryamhs>(Mkaryamhs.class);
        if(getMahasiswa() != null)
            onKarya.addFilter(new Filter("mahasiswaId", this.mhsid, Filter.OP_EQUAL));

        onKarya.addSort("cno", false);

        pagingKarya.setPageSize(pageSize);
		pagingKarya.setDetailed(true);
		getPlwKarya().init(onKarya, listKarya, pagingKarya);
		listKarya.setItemRenderer(new KegiatanSearchKaryaList());
    }

    public void onClick$btnNewKarya(Event event) throws Exception {
        final Mkaryamhs mpkaryamhs = getDetailCtrl().getMainCtrl().getMkaryamhsService().getNew();
        this.showDetailKarya(mpkaryamhs, true);
    }

    public void onClick$btnDeleteKarya(Event event) throws Exception {
        Listitem item = listKarya.getSelectedItem();
        if(item == null) return;
        getDelKarya().add((Mkaryamhs) item.getAttribute(MahasiswaKegiatanCtrl.DATA));
        listKarya.removeItemAt(listKarya.getSelectedIndex());
    }

    public void onKaryaItem(Event event) throws Exception {
        Listitem item = listKarya.getSelectedItem();
        if(item == null) return;

        final Mkaryamhs anKarya = (Mkaryamhs) item.getAttribute(MahasiswaKegiatanCtrl.DATA);
        this.showDetailKarya(anKarya, false);
    }

    public void showDetailKarya(Mkaryamhs mpkaryamhs, boolean isnew) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(MahasiswaKegiatanCtrl.DATA, mpkaryamhs);
        map.put(MahasiswaKegiatanCtrl.LIST, listKarya);
        map.put(MahasiswaKegiatanCtrl.CONTROL, this);
        map.put(MahasiswaKegiatanCtrl.ISNEW, isnew);

        try {
            Executions.createComponents("/WEB-INF/pages/administrasi/mahasiswa/pageKaryaDtl.zul", null, map);
        } catch (final Exception e) {
            logger.error("onOpenWindow:: error opening window / " + e.getMessage());
            ZksampleMessageUtils.showErrorMessage(e.toString());
        }
    }

    public void reLoadPage() {
        binder.loadAll();
        this.doFitSize();

        if(getMahasiswa()== null) return;
        if(this.mhsid==getMahasiswa().getId()) return;

        loadKegiatan();
        loadKarya();
    }

    private void doFitSize() {
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue();
        final int maxListBoxHeight = height - 245;

        borderDetailKegiatan.setHeight(String.valueOf(maxListBoxHeight) + "px");
        windowKgtIlmiahDetail.invalidate();
    }

    public void doReadOnlyMode(boolean b) {
        txtbctujuan.setReadonly(b);
        
        btnNewKegiatan.setDisabled(b);
        btnDeleteKegiatan.setDisabled(b);
        btnNewKarya.setDisabled(b);
        btnDeleteKarya.setDisabled(b);
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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public PagedListWrapper<Mkgtmhs> getPlwKegiatan() {
        return plwKegiatan;
    }

    public void setPlwKegiatan(PagedListWrapper<Mkgtmhs> plwKegiatan) {
        this.plwKegiatan = plwKegiatan;
    }

    public PagedListWrapper<Mkaryamhs> getPlwKarya() {
        return plwKarya;
    }

    public void setPlwKarya(PagedListWrapper<Mkaryamhs> plwKarya) {
        this.plwKarya = plwKarya;
    }

    public Set<Mkaryamhs> getDelKarya() {
        return delKarya;
    }

    public void setDelKarya(Set<Mkaryamhs> delKarya) {
        this.delKarya = delKarya;
    }

    public Set<Mkgtmhs> getDelKegiatan() {
        return delKegiatan;
    }

    public void setDelKegiatan(Set<Mkgtmhs> delKegiatan) {
        this.delKegiatan = delKegiatan;
    }

    public Listbox getListKarya() {
        return listKarya;
    }

    public void setListKarya(Listbox listKarya) {
        this.listKarya = listKarya;
    }

    public Listbox getListKegiatan() {
        return listKegiatan;
    }

    public void setListKegiatan(Listbox listKegiatan) {
        this.listKegiatan = listKegiatan;
    }

    public KaryaCtrl getKaryaCtrl() {
        return karyaCtrl;
    }

    public void setKaryaCtrl(KaryaCtrl karyaCtrl) {
        this.karyaCtrl = karyaCtrl;
    }

    public KegiatanCtrl getKegiatanCtrl() {
        return kegiatanCtrl;
    }

    public void setKegiatanCtrl(KegiatanCtrl kegiatanCtrl) {
        this.kegiatanCtrl = kegiatanCtrl;
    }
}
