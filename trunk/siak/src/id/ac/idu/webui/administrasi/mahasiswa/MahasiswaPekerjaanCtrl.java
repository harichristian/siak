package id.ac.idu.webui.administrasi.mahasiswa;

import com.trg.search.Filter;
import id.ac.idu.backend.model.MkodePos;
import id.ac.idu.backend.model.Mmahasiswa;
import id.ac.idu.backend.model.Mprestasimhs;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.webui.administrasi.mahasiswa.model.KursusSearchList;
import id.ac.idu.webui.administrasi.mahasiswa.model.PrestasiSearchList;
import id.ac.idu.webui.administrasi.mahasiswa.model.PribadiSearchKodeposList;
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 02 Apr 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MahasiswaPekerjaanCtrl extends GFCBaseCtrl implements Serializable  {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(MahasiswaPekerjaanCtrl.class);

    private int pageSize;
    private int mhsid;

    protected Window windowPekerjaan;
    protected Borderlayout borderPekerjaan;

    protected Textbox txtbcnmkntor;
    protected Textbox txtbcalmkntor;
    protected Textbox txtbctelpkntor;
    protected Textbox txtbcfaxkantor;
    protected Textbox txtbckdposkntor;
    protected Bandbox cmbckdposkntor;
    protected Textbox txtbcketkerja;
    protected Button btnNewPrestasi;
    protected Button btnDeletePrestasi;

    public static final String DATA = "data";
    public static final String LIST = "LIST";
    public static final String CONTROL = "CONTROL";
    public static final String ISNEW = "ISNEW";

    /* Lov Kodepos */
    private transient PagedListWrapper<MkodePos> plwKodepos;
    protected Paging pagingCkdposkntor;
    protected Listbox listCkdposkntor;
    protected Listheader ckdposkntor;
    protected Textbox tbckdposkntor;

    /* List Prestasi */
    protected Paging pagingPrestasi;
    protected Listbox listPrestasi;
    private transient PagedListWrapper<Mprestasimhs> plwPrestasi;
    protected Set<Mprestasimhs> delPrestasi = new HashSet<Mprestasimhs>();

    protected Listheader cno;
    protected Listheader cnmprestasi;
    protected Listheader cnmberi;
    protected Listheader cthnterima;
    protected Listheader cket;

    private MahasiswaDetailCtrl detailCtrl;
    private PrestasiCtrl prestasiCtrl;
    private AnnotateDataBinder binder;
    
    public MahasiswaPekerjaanCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        logger.info("Page Khusus Makasiswa Loaded");

        super.doAfterCompose(window);
        self.setAttribute("controller", this, false);

        if(arg.containsKey("ModuleMainController")) {
            setDetailCtrl((MahasiswaDetailCtrl) arg.get("ModuleMainController"));
            getDetailCtrl().setPekerjaanCtrl(this);
        }
    }

    @SuppressWarnings("unchecked")
    public void onCreate$windowPekerjaan(Event event) throws Exception {
        setPageSize(20);
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doReadOnlyMode(!getDetailCtrl().getMainCtrl().btnSave.isVisible());
        if(getMahasiswa() != null)
            loadPrestasi();

        binder.loadAll();
        this.doFitSize();
    }

    public void loadPrestasi() {
        this.mhsid = getMahasiswa().getId();

        cno.setSortAscending(new FieldComparator("cno",true));
        cno.setSortDescending(new FieldComparator("cno",false));
        cnmprestasi.setSortAscending(new FieldComparator("cnmprestasi",true));
        cnmprestasi.setSortDescending(new FieldComparator("cnmprestasi",false));
        cnmberi.setSortAscending(new FieldComparator("cnmberi",true));
        cnmberi.setSortDescending(new FieldComparator("cnmberi",false));
        cthnterima.setSortAscending(new FieldComparator("cthnterima",true));
        cthnterima.setSortDescending(new FieldComparator("cthnterima",false));
        cket.setSortAscending(new FieldComparator("cket",true));
        cket.setSortDescending(new FieldComparator("cket",false));

        HibernateSearchObject<Mprestasimhs> onKursus = new HibernateSearchObject<Mprestasimhs>(Mprestasimhs.class);
        if(getMahasiswa() != null)
            onKursus.addFilter(new Filter("mahasiswaId", this.mhsid, Filter.OP_EQUAL));

        onKursus.addSort("cno", false);

        pagingPrestasi.setPageSize(pageSize);
		pagingPrestasi.setDetailed(true);
		getPlwPrestasi().init(onKursus, listPrestasi, pagingPrestasi);
		listPrestasi.setItemRenderer(new PrestasiSearchList());
    }

    public void onClick$btnNewPrestasi(Event event) throws Exception  {
        final Mprestasimhs mprestasimhs = getDetailCtrl().getMainCtrl().getMprestasimhsService().getNew();
        this.showDetailPrestasi(mprestasimhs, true);
    }

    public void onClick$btnDeletePrestasi(Event event) {
        Listitem item = listPrestasi.getSelectedItem();
        if(item == null) return;

        getDelPrestasi().add((Mprestasimhs) item.getAttribute(MahasiswaKursusCtrl.DATA));
        listPrestasi.removeItemAt(listPrestasi.getSelectedIndex());
    }

    public void onPrestasiItem(Event event) throws Exception{
        Listitem item = listPrestasi.getSelectedItem();
        if(item == null) return;

        final Mprestasimhs anBahasa = (Mprestasimhs) item.getAttribute(MahasiswaKursusCtrl.DATA);
        this.showDetailPrestasi(anBahasa, false);
    }

    public void showDetailPrestasi(Mprestasimhs mprestasimhs, boolean isnew) throws Exception  {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(MahasiswaKursusCtrl.DATA, mprestasimhs);
        map.put(MahasiswaKursusCtrl.LIST, listPrestasi);
        map.put(MahasiswaKursusCtrl.CONTROL, this);
        map.put(MahasiswaKursusCtrl.ISNEW, isnew);

        try {
            Executions.createComponents("/WEB-INF/pages/administrasi/mahasiswa/pagePrestasi.zul", null, map);
        } catch (final Exception e) {
            logger.error("onOpenWindow:: error opening window / " + e.getMessage());
            ZksampleMessageUtils.showErrorMessage(e.toString());
        }
    }

    public void onOpen$cmbckdposkntor(Event event) {
        this.searchKodepos();
    }

    public void searchKodepos(Filter... filters) {
        HibernateSearchObject<MkodePos> soKodepos = new HibernateSearchObject<MkodePos>(MkodePos.class);

        ckdposkntor.setSortAscending(new FieldComparator("kodepos",true));
        ckdposkntor.setSortDescending(new FieldComparator("kodepos",false));

        if(filters != null) {
            for(Filter anFilter : filters) {
                if(anFilter!=null) soKodepos.addFilter(anFilter);
            }
        }
        
        soKodepos.addSort("kodepos", false);
        pagingCkdposkntor.setPageSize(pageSize);
		pagingCkdposkntor.setDetailed(true);
		getPlwKodepos().init(soKodepos, listCkdposkntor, pagingCkdposkntor);
		listCkdposkntor.setItemRenderer(new PribadiSearchKodeposList());
    }

    public void onKodeposItem(Event event) {
        Listitem item = listCkdposkntor.getSelectedItem();

        if (item != null) {
            MkodePos aKodepos = (MkodePos) item.getAttribute("data");

            getDetailCtrl().getMainCtrl().getMahasiswa().setCkdposkntor(aKodepos);
            txtbckdposkntor.setValue(aKodepos.getKodepos());
        }

        cmbckdposkntor.close();
    }

    public void onClick$buttonSearchCkdposkntor(Event event) {
        Filter filter = null;

        if (StringUtils.isNotEmpty(tbckdposkntor.getValue()))
            filter = new Filter("kodepos", "%" + tbckdposkntor.getValue() + "%", Filter.OP_ILIKE);

        this.searchKodepos(filter);
    }

    public void onClick$buttonCloseCkdposkntor(Event event) {
        cmbckdposkntor.close();
    }

    public void reLoadPage() {
        binder.loadAll();
        this.doFitSize();

        if(getMahasiswa()== null) return;
        if(this.mhsid == getMahasiswa().getId()) return;

        loadPrestasi();
    }

    public void doFitSize() {
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue();
        final int maxListBoxHeight = height - 243;

        borderPekerjaan.setHeight(String.valueOf(maxListBoxHeight) + "px");
        windowPekerjaan.invalidate();
    }

    public void doReadOnlyMode(boolean b) {
        txtbcnmkntor.setReadonly(b);
        txtbcalmkntor.setReadonly(b);
        txtbctelpkntor.setReadonly(b);
        txtbcfaxkantor.setReadonly(b);
        cmbckdposkntor.setDisabled(b);
        txtbcketkerja.setReadonly(b);

        btnNewPrestasi.setDisabled(b);
        btnDeletePrestasi.setDisabled(b);
    }

    public boolean isModeEdit() {
        return (getDetailCtrl().getMainCtrl().btnSave.isVisible());
    }

    public Mmahasiswa getMahasiswa() {
        return getDetailCtrl().getMainCtrl().getMahasiswa();
    }

    public void setMahasiswa(Mmahasiswa _obj) {
        getDetailCtrl().getMainCtrl().setMahasiswa(_obj);
    }

    public MahasiswaDetailCtrl getDetailCtrl() {
        return detailCtrl;
    }

    public void setDetailCtrl(MahasiswaDetailCtrl detailCtrl) {
        this.detailCtrl = detailCtrl;
    }

    public PrestasiCtrl getPrestasiCtrl() {
        return prestasiCtrl;
    }

    public void setPrestasiCtrl(PrestasiCtrl prestasiCtrl) {
        this.prestasiCtrl = prestasiCtrl;
    }

    public AnnotateDataBinder getBinder() {
        return binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
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

    public PagedListWrapper<Mprestasimhs> getPlwPrestasi() {
        return plwPrestasi;
    }

    public void setPlwPrestasi(PagedListWrapper<Mprestasimhs> plwPrestasi) {
        this.plwPrestasi = plwPrestasi;
    }

    public Listbox getListPrestasi() {
        return listPrestasi;
    }

    public void setListPrestasi(Listbox listPrestasi) {
        this.listPrestasi = listPrestasi;
    }

    public Set<Mprestasimhs> getDelPrestasi() {
        return delPrestasi;
    }

    public void setDelPrestasi(Set<Mprestasimhs> delPrestasi) {
        this.delPrestasi = delPrestasi;
    }
}
