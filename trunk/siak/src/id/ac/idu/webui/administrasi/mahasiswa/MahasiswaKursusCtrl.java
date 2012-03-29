package id.ac.idu.webui.administrasi.mahasiswa;

import com.trg.search.Filter;
import id.ac.idu.backend.model.Mhistkursusmhs;
import id.ac.idu.backend.model.Mmahasiswa;
import id.ac.idu.backend.model.Mpbahasamhs;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.webui.administrasi.mahasiswa.model.KursusSearchBahasaList;
import id.ac.idu.webui.administrasi.mahasiswa.model.KursusSearchList;
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

public class MahasiswaKursusCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(MahasiswaKursusCtrl.class);

    protected transient AnnotateDataBinder binder;

    protected Window windowKursusDetail;
    protected Borderlayout borderKursus;

    private MahasiswaDetailCtrl detailCtrl;
    private int pageSize;

    private int mhsid;

    public static final String DATA = "data";
    public static final String LIST = "LIST";
    public static final String CONTROL = "CONTROL";
    public static final String ISNEW = "ISNEW";

    protected Button btnNewKursus;
    protected Button btnDeleteKursus;
    protected Button btnNewBahasa;
    protected Button btnDeleteBahasa;

    protected Set<Mhistkursusmhs> delKursus = new HashSet<Mhistkursusmhs>();
    private transient PagedListWrapper<Mhistkursusmhs> plwKursus;
    protected Listbox listRiwayatKursus;
    protected Paging pagingRiwayatKursus;

    protected Set<Mpbahasamhs> delBahasa = new HashSet<Mpbahasamhs>();
    private transient PagedListWrapper<Mpbahasamhs> plwBahasa;
    protected Listbox listRiwayatBahasa;
    protected Paging pagingRiwayatBahasa;

    protected RiwayatBahasaCtrl riwayatBahasaCtrl;
    protected RiwayatKursusCtrl riwayatKursusCtrl;

    public MahasiswaKursusCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        logger.info("Page Pribadi Makasiswa Loaded");

        super.doAfterCompose(window);
        self.setAttribute("controller", this, false);

        if(arg.containsKey("ModuleMainController")) {
            setDetailCtrl((MahasiswaDetailCtrl) arg.get("ModuleMainController"));
            getDetailCtrl().setKursusCtrl(this);
        }
        else setMahasiswa(null);
    }

    @SuppressWarnings("unchecked")
    public void onCreate$windowKursusDetail(Event event) throws Exception {
        setPageSize(20);

        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doReadOnlyMode(!getDetailCtrl().getMainCtrl().btnSave.isVisible());
        if(getMahasiswa() != null) {
            loadRiwayatKursus();
            loadRiwayatBahasa();
        }

        binder.loadAll();
        this.doFitSize();
    }

    public void loadRiwayatKursus() {
        this.mhsid = getMahasiswa().getId();

        HibernateSearchObject<Mhistkursusmhs> onKursus = new HibernateSearchObject<Mhistkursusmhs>(Mhistkursusmhs.class);
        if(getMahasiswa() != null)
            onKursus.addFilter(new Filter("mahasiswaId", this.mhsid, Filter.OP_EQUAL));

        onKursus.addSort("cno", false);

        pagingRiwayatKursus.setPageSize(pageSize);
		pagingRiwayatKursus.setDetailed(true);
		getPlwKursus().init(onKursus, listRiwayatKursus, pagingRiwayatKursus);
		listRiwayatKursus.setItemRenderer(new KursusSearchList());
    }

    public void onClick$btnNewKursus(Event event) throws Exception {
        final Mhistkursusmhs mhistkursusmhs = getDetailCtrl().getMainCtrl().getMhistkursusmhsService().getNew();
        this.showDetailKursus(mhistkursusmhs, true);
    }

    public void onClick$btnDeleteKursus(Event event) throws Exception {
        Listitem item = listRiwayatKursus.getSelectedItem();
        getDelKursus().add((Mhistkursusmhs) item.getAttribute(MahasiswaKursusCtrl.DATA));
        listRiwayatKursus.removeItemAt(listRiwayatKursus.getSelectedIndex());
    }

    public void onKursusItem(Event event) throws Exception {
        Listitem item = listRiwayatKursus.getSelectedItem();
        if(item == null) return;

        final Mhistkursusmhs anKursus = (Mhistkursusmhs) item.getAttribute(MahasiswaKursusCtrl.DATA);
        this.showDetailKursus(anKursus, false);
    }

    public void showDetailKursus(Mhistkursusmhs mhistkursusmhs, boolean isnew) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(MahasiswaKursusCtrl.DATA, mhistkursusmhs);
        map.put(MahasiswaKursusCtrl.LIST, listRiwayatKursus);
        map.put(MahasiswaKursusCtrl.CONTROL, this);
        map.put(MahasiswaKursusCtrl.ISNEW, isnew);

        try {
            Executions.createComponents("/WEB-INF/pages/administrasi/mahasiswa/pageRiwayatKursus.zul", null, map);
        } catch (final Exception e) {
            logger.error("onOpenWindow:: error opening window / " + e.getMessage());
            ZksampleMessageUtils.showErrorMessage(e.toString());
        }
    }

    public void loadRiwayatBahasa() {
        this.mhsid = getMahasiswa().getId();

        HibernateSearchObject<Mpbahasamhs> onBahasa = new HibernateSearchObject<Mpbahasamhs>(Mpbahasamhs.class);
        if(getMahasiswa() != null)
            onBahasa.addFilter(new Filter("mahasiswaId", this.mhsid, Filter.OP_EQUAL));

        onBahasa.addSort("cno", false);

        pagingRiwayatBahasa.setPageSize(pageSize);
		pagingRiwayatBahasa.setDetailed(true);
		getPlwBahasa().init(onBahasa, listRiwayatBahasa, pagingRiwayatBahasa);
		listRiwayatBahasa.setItemRenderer(new KursusSearchBahasaList());
    }

    public void onClick$btnNewBahasa(Event event) throws Exception {
        final Mpbahasamhs mpbahasamhs = getDetailCtrl().getMainCtrl().getMpbahasamhsService().getNew();
        this.showDetailBahasa(mpbahasamhs, true);
    }

    public void onClick$btnDeleteBahasa(Event event) throws Exception {
        Listitem item = listRiwayatBahasa.getSelectedItem();
        getDelBahasa().add((Mpbahasamhs) item.getAttribute(MahasiswaKursusCtrl.DATA));
        listRiwayatBahasa.removeItemAt(listRiwayatBahasa.getSelectedIndex());
    }

    public void onBahasaItem(Event event) throws Exception {
        Listitem item = listRiwayatBahasa.getSelectedItem();
        if(item == null) return;

        final Mpbahasamhs anBahasa = (Mpbahasamhs) item.getAttribute(MahasiswaKursusCtrl.DATA);
        this.showDetailBahasa(anBahasa, false);
    }

    public void showDetailBahasa(Mpbahasamhs mpbahasamhs, boolean isnew) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(MahasiswaKursusCtrl.DATA, mpbahasamhs);
        map.put(MahasiswaKursusCtrl.LIST, listRiwayatBahasa);
        map.put(MahasiswaKursusCtrl.CONTROL, this);
        map.put(MahasiswaKursusCtrl.ISNEW, isnew);

        try {
            Executions.createComponents("/WEB-INF/pages/administrasi/mahasiswa/pageRiwayatBahasa.zul", null, map);
        } catch (final Exception e) {
            logger.error("onOpenWindow:: error opening window / " + e.getMessage());
            ZksampleMessageUtils.showErrorMessage(e.toString());
        }
    }

    public void reLoadPage() {
        binder.loadAll();
        this.doFitSize();

        if(this.mhsid == getMahasiswa().getId())
            return;

        loadRiwayatKursus();
        loadRiwayatBahasa();
    }

    private void doFitSize() {
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue();
        final int maxListBoxHeight = height - 245;

        borderKursus.setHeight(String.valueOf(maxListBoxHeight) + "px");
        windowKursusDetail.invalidate();
    }

    public void doReadOnlyMode(boolean b) {
        btnNewKursus.setDisabled(b);
        btnDeleteKursus.setDisabled(b);
        btnNewBahasa.setDisabled(b);
        btnDeleteBahasa.setDisabled(b);
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

    public PagedListWrapper<Mhistkursusmhs> getPlwKursus() {
        return plwKursus;
    }

    public void setPlwKursus(PagedListWrapper<Mhistkursusmhs> plwKursus) {
        this.plwKursus = plwKursus;
    }

    public PagedListWrapper<Mpbahasamhs> getPlwBahasa() {
        return plwBahasa;
    }

    public void setPlwBahasa(PagedListWrapper<Mpbahasamhs> plwBahasa) {
        this.plwBahasa = plwBahasa;
    }

    public Set<Mpbahasamhs> getDelBahasa() {
        return delBahasa;
    }

    public void setDelBahasa(Set<Mpbahasamhs> delBahasa) {
        this.delBahasa = delBahasa;
    }

    public Set<Mhistkursusmhs> getDelKursus() {
        return delKursus;
    }

    public void setDelKursus(Set<Mhistkursusmhs> delKursus) {
        this.delKursus = delKursus;
    }

    public Listbox getListRiwayatBahasa() {
        return listRiwayatBahasa;
    }

    public void setListRiwayatBahasa(Listbox listRiwayatBahasa) {
        this.listRiwayatBahasa = listRiwayatBahasa;
    }

    public Listbox getListRiwayatKursus() {
        return listRiwayatKursus;
    }

    public void setListRiwayatKursus(Listbox listRiwayatKursus) {
        this.listRiwayatKursus = listRiwayatKursus;
    }

    public RiwayatBahasaCtrl getRiwayatBahasaCtrl() {
        return riwayatBahasaCtrl;
    }

    public void setRiwayatBahasaCtrl(RiwayatBahasaCtrl riwayatBahasaCtrl) {
        this.riwayatBahasaCtrl = riwayatBahasaCtrl;
    }

    public RiwayatKursusCtrl getRiwayatKursusCtrl() {
        return riwayatKursusCtrl;
    }

    public void setRiwayatKursusCtrl(RiwayatKursusCtrl riwayatKursusCtrl) {
        this.riwayatKursusCtrl = riwayatKursusCtrl;
    }
}
