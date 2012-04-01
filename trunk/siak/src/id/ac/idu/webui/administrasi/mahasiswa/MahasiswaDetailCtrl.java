package id.ac.idu.webui.administrasi.mahasiswa;

import id.ac.idu.backend.model.*;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.MultiLineMessageBox;
import id.ac.idu.webui.util.ZksampleCommonUtils;
import id.ac.idu.webui.util.ZksampleMessageUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 17 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MahasiswaDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(MahasiswaDetailCtrl.class);

    protected transient AnnotateDataBinder binder;

    /* Tab */
    protected Tab tabPribadi;
    protected Tabpanel tabPanelPribadi;
    protected Tab tabKhusus;
    protected Tabpanel tabPanelKhusus;
    protected Tab tabPendidikan;
    protected Tabpanel tabPanelPendidikan;
    protected Tab tabKursus;
    protected Tabpanel tabPanelKursus;
    protected Tab tabKegiatan;
    protected Tabpanel tabPanelKegiatan;
    protected Tab tabPekerjaan;
    protected Tabpanel tabPanelPekerjaan;

    private MahasiswaMainCtrl mainCtrl;
    private MahasiswaPribadiCtrl pribadiCtrl;
    private MahasiswaKhususCtrl khususCtrl;
    private MahasiswaPendidikanCtrl pendidikanCtrl;
    private MahasiswaKursusCtrl kursusCtrl;
    private MahasiswaKegiatanCtrl kegiatanCtrl;
    private MahasiswaPekerjaanCtrl pekerjaanCtrl;

    public MahasiswaDetailCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        logger.info("Page Detail Makasiswa Loaded");

        super.doAfterCompose(window);
        self.setAttribute("controller", this, false);

        if(arg.containsKey("ModuleMainController")) {
            setMainCtrl((MahasiswaMainCtrl) arg.get("ModuleMainController"));
            getMainCtrl().setDetailCtrl(this);
        }
        else setMahasiswa(null);
    }

    public void onCreate$windowMahasiswaDetail(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);
        binder.loadAll();

        tabPribadi.setSelected(true);
        
        if(tabPanelPribadi != null) {
            if(tabPanelPribadi.getFirstChild() == null) ZksampleCommonUtils.createTabPanelContent(tabPanelPribadi, this
                    , "ModuleMainController" , "/WEB-INF/pages/administrasi/mahasiswa/pagePribadi.zul");
        }
    }

    public void onSelect$tabPribadi(Event event) {
        if(tabPanelPribadi.getFirstChild() != null) {
            tabPribadi.setSelected(true);
            getPribadiCtrl().reLoadPage();

            return;
        }
        
        if (tabPanelPribadi != null) {
            if(tabPanelPribadi.getFirstChild() == null) ZksampleCommonUtils.createTabPanelContent(tabPanelPribadi, this
                    , "ModuleMainController" , "/WEB-INF/pages/administrasi/mahasiswa/pagePribadi.zul");
        }
    }

    public void onSelect$tabKhusus(Event event) {
        if(tabPanelKhusus.getFirstChild() != null) {
            tabKhusus.setSelected(true);
            getKhususCtrl().reLoadPage();

            return;
        }

        if(tabPanelKhusus != null) {
            if(tabPanelKhusus.getFirstChild() == null) ZksampleCommonUtils.createTabPanelContent(tabPanelKhusus, this
                    , "ModuleMainController" , "/WEB-INF/pages/administrasi/mahasiswa/pageKhusus.zul");
        }
    }

    public void onSelect$tabPendidikan(Event event) {
        if(tabPanelPendidikan.getFirstChild() != null) {
            tabPendidikan.setSelected(true);
            getPendidikanCtrl().reLoadPage();

            return;
        }

        if(tabPanelPendidikan != null) {
            if(tabPanelPendidikan.getFirstChild() == null) ZksampleCommonUtils.createTabPanelContent(tabPanelPendidikan, this
                    , "ModuleMainController" , "/WEB-INF/pages/administrasi/mahasiswa/pagePendidikan.zul");
        }
    }

    public void onSelect$tabKursus(Event event) {
        if(tabPanelKursus.getFirstChild() != null) {
            tabKursus.setSelected(true);
            getKursusCtrl().reLoadPage();

            return;
        }

        if(tabPanelKursus != null) {
            if(tabPanelKursus.getFirstChild() == null) ZksampleCommonUtils.createTabPanelContent(tabPanelKursus, this
                    , "ModuleMainController" , "/WEB-INF/pages/administrasi/mahasiswa/pageKursus.zul");
        }
    }

    public void onSelect$tabKegiatan(Event event) {
        if(tabPanelKegiatan.getFirstChild() != null) {
            tabKegiatan.setSelected(true);
            getKegiatanCtrl().reLoadPage();

            return;
        }

        if(tabPanelKegiatan != null) {
            if(tabPanelKegiatan.getFirstChild() == null) ZksampleCommonUtils.createTabPanelContent(tabPanelKegiatan, this
                    , "ModuleMainController" , "/WEB-INF/pages/administrasi/mahasiswa/pageKegiatan.zul");
        }
    }

    public void onSelect$tabPekerjaan(Event event) {
        if(tabPanelPekerjaan.getFirstChild() != null) {
            tabPekerjaan.setSelected(true);
            getPekerjaanCtrl().reLoadPage();

            return;
        }

        if(tabPanelPekerjaan != null) {
            if(tabPanelPekerjaan.getFirstChild() == null) ZksampleCommonUtils.createTabPanelContent(tabPanelPekerjaan, this
                    , "ModuleMainController" , "/WEB-INF/pages/administrasi/mahasiswa/pagePekerjaan.zul");
        }
    }

    public void doNew(Event event) {
        final Mmhspascakhs nMmhspascakhs = getMainCtrl().getMmhspascakhsService().getNew();
        final Mmahasiswa nData = getMainCtrl().getMhsservice().getNew();
        nData.setMmhspascakhs(nMmhspascakhs);
        
        this.setMahasiswa(nData);
        this.checkAllTab();
        this.loadAllBind();

        if(getPribadiCtrl().getBinder() != null) {
            this.doReadOnlyMode(false);
            this.doReset();
        }

        tabPribadi.setSelected(true);
    }

    public void doEdit(Event event) {
        if(getMainCtrl().getMahasiswa() == null) {
            doNew(event);
            return;
        }

        this.checkAllTab();
        this.loadAllBind();
        this.doReadOnlyMode(false);
        this.doReset();
    }

    public void doSave(Event event) throws InterruptedException  {
        this.saveAllBind();
        Listitem item;

        /* Tab Khusus */
        Mppumhskhusus oPendidikan;
        Set<Mppumhskhusus> mppumhskhususes = new HashSet<Mppumhskhusus>();
        
        List riwayatPendidikan = getKhususCtrl().getListRiwayatPendidikan().getItems();
        for(Object onPendidikan : riwayatPendidikan) {
            item = (Listitem) onPendidikan;
            oPendidikan = (Mppumhskhusus) item.getAttribute(MahasiswaKhususCtrl.LIST_DATA);
            if(getMahasiswa().getId() > 0) oPendidikan.setMahasiswaId(getMahasiswa().getId());
            mppumhskhususes.add(oPendidikan);
        }

        Mhistpangkatmhs oPangkat;
        Set<Mhistpangkatmhs> mhistpangkatmhses = new HashSet<Mhistpangkatmhs>();

        List riwayatPangkat = getKhususCtrl().getListRiwayatPangkat().getItems();
        for(Object onPangkat : riwayatPangkat) {
            item = (Listitem) onPangkat;
            oPangkat = (Mhistpangkatmhs) item.getAttribute(MahasiswaKhususCtrl.LIST_DATA);
            if(getMahasiswa().getId() > 0) oPangkat.setMahasiswaId(getMahasiswa().getId());
            mhistpangkatmhses.add(oPangkat);
        }

        /* Tab pendidikan */
        Set<Mhistpnddkmhs> mhistpnddkmhses = new HashSet<Mhistpnddkmhs>();
        Mhistpnddkmhs mhistpnddkmhsS1 = getPendidikanCtrl().getMhistpnddkmhsS1();
        Mhistpnddkmhs mhistpnddkmhsS2 = getPendidikanCtrl().getMhistpnddkmhsS2();
        
        if(mhistpnddkmhsS1 != null) {
            if(mhistpnddkmhsS1.getMjenjang()!= null && mhistpnddkmhsS1.getMprodi()!=null && mhistpnddkmhsS1.getMuniv()!=null) mhistpnddkmhses.add(mhistpnddkmhsS1);
        }

        if(mhistpnddkmhsS2 != null) {
            if(mhistpnddkmhsS2.getMjenjang()!= null && mhistpnddkmhsS2.getMprodi()!=null && mhistpnddkmhsS2.getMuniv()!=null) mhistpnddkmhses.add(mhistpnddkmhsS2);
        }

        /* Tab Kursus/Bahasa */
        Mhistkursusmhs oKursus;
        Set<Mhistkursusmhs> mhistkursusmhses = new HashSet<Mhistkursusmhs>();

        List anKursus = getKursusCtrl().getListRiwayatKursus().getItems();
        for(Object kursus : anKursus) {
            item = (Listitem) kursus;
            oKursus = (Mhistkursusmhs) item.getAttribute(MahasiswaKursusCtrl.DATA);
            if(getMahasiswa().getId() > 0) oKursus.setMahasiswaId(getMahasiswa().getId());
            mhistkursusmhses.add(oKursus);
        }

        Mpbahasamhs oBahasa;
        Set<Mpbahasamhs> mpbahasamhses = new HashSet<Mpbahasamhs>();

        List anBahasa = getKursusCtrl().getListRiwayatBahasa().getItems();
        for(Object bahasa : anBahasa) {
            item = (Listitem) bahasa;
            oBahasa = (Mpbahasamhs) item.getAttribute(MahasiswaKursusCtrl.DATA);
            if(getMahasiswa().getId() > 0) oBahasa.setMahasiswaId(getMahasiswa().getId());
            mpbahasamhses.add(oBahasa);
        }

        /* Tab Kegiatan Ilmiah */
        Mkgtmhs oKegiatan;
        Set<Mkgtmhs> mkgtmhses = new HashSet<Mkgtmhs>();

        List anKegiatan = getKegiatanCtrl().getListKegiatan().getItems();
        for(Object kegiatan : anKegiatan) {
            item = (Listitem) kegiatan;
            oKegiatan = (Mkgtmhs) item.getAttribute(MahasiswaKegiatanCtrl.DATA);
            if(getMahasiswa().getId() > 0) oKegiatan.setMahasiswaId(getMahasiswa().getId());
            mkgtmhses.add(oKegiatan);
        }

        Mkaryamhs oKarya;
        Set<Mkaryamhs> mkaryamhses = new HashSet<Mkaryamhs>();

        List anKarya = getKegiatanCtrl().getListKarya().getItems();
        for(Object karya : anKarya) {
            item = (Listitem) karya;
            oKarya = (Mkaryamhs) item.getAttribute(MahasiswaKegiatanCtrl.DATA);
            if(getMahasiswa().getId() > 0) oKarya.setMahasiswaId(getMahasiswa().getId());
            mkaryamhses.add(oKarya);
        }

        /* Tab Prestasi */

        Mprestasimhs oPrestasi;
        Set<Mprestasimhs> mprestasimhses = new HashSet<Mprestasimhs>();

        List anPrestasi = getPekerjaanCtrl().getListPrestasi().getItems();
        for(Object prestasi : anPrestasi) {
            item = (Listitem) prestasi;
            oPrestasi = (Mprestasimhs) item.getAttribute(MahasiswaPekerjaanCtrl.DATA);
            if(getMahasiswa().getId() > 0) oPrestasi.setMahasiswaId(getMahasiswa().getId());
            mprestasimhses.add(oPrestasi);
        }

        /* Save All */
        try {
            getMainCtrl().getMahasiswa().getMppumhskhususes().clear();   // Page Khusus
            getMainCtrl().getMahasiswa().getMhistpangkatmhses().clear(); // Page Khusus
            getMainCtrl().getMahasiswa().getMhistkursusmhses().clear();  // Page Kursus/Bahasa
            getMainCtrl().getMahasiswa().getMpbahasamhses().clear();     // Page Kursus/Bahasa
            getMainCtrl().getMahasiswa().getMkgtmhses().clear();         // Page Kegiatan/Karya
            getMainCtrl().getMahasiswa().getMkaryamhses().clear();       // Page Kegiatan/Karya
            getMainCtrl().getMahasiswa().getMprestasimhses().clear();    // Page Pekerjaan

            if(getMahasiswa().getId() > 0)  {
                getMainCtrl().getMahasiswa().getMppumhskhususes().addAll(mppumhskhususes);       // Page Khusus
                getMainCtrl().getMahasiswa().getMhistpangkatmhses().addAll(mhistpangkatmhses);   // Page Khusus
                getMainCtrl().getMahasiswa().getMhistkursusmhses().addAll(mhistkursusmhses);     // Kursus/Bahasa
                getMainCtrl().getMahasiswa().getMpbahasamhses().addAll(mpbahasamhses);           // Kursus/Bahasa
                getMainCtrl().getMahasiswa().getMkgtmhses().addAll(mkgtmhses);                   // Page Kegiatan/Karya
                getMainCtrl().getMahasiswa().getMkaryamhses().addAll(mkaryamhses);               // Page Kegiatan/Karya
                getMainCtrl().getMahasiswa().getMprestasimhses().addAll(mprestasimhses);         // Page Pekerjaan
            }
            else {
                getMainCtrl().getMahasiswa().setMppumhskhususes(mppumhskhususes);                // Page Khusus
                getMainCtrl().getMahasiswa().setMhistpangkatmhses(mhistpangkatmhses);            // Page Khusus
                getMainCtrl().getMahasiswa().setMhistkursusmhses(mhistkursusmhses);              // Page Kursus
                getMainCtrl().getMahasiswa().setMpbahasamhses(mpbahasamhses);                    // Page Kursus
                getMainCtrl().getMahasiswa().setMkgtmhses(mkgtmhses);                            // Page Kegiatan/Karya
                getMainCtrl().getMahasiswa().setMkaryamhses(mkaryamhses);                        // Page Kegiatan/Karya
                getMainCtrl().getMahasiswa().setMprestasimhses(mprestasimhses);                  // Page Pekerjaan
            }
            
            getMainCtrl().getMahasiswa().setMhistpnddkmhses(mhistpnddkmhses);
            
            getMainCtrl().getMhsservice().saveOrUpdate(getMainCtrl().getMahasiswa());
            getMainCtrl().getListCtrl().loadListData();
        }
        catch (DataAccessException e) { ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString()); }
        finally { this.doReadOnlyMode(true); }
    }

    public void doDelete(Event event) throws InterruptedException {
        final Mmahasiswa deldata = getMainCtrl().getMahasiswa();
        
        if (deldata != null) {
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + " : " +  deldata.getCnama() + " [" +deldata.getCnim() +"]";
            final String title = Labels.getLabel("message.Deleting.Record");

            MultiLineMessageBox.doSetTemplate();
            if (MultiLineMessageBox.show
                    (msg, title, Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, true,
                        new EventListener() {
                            @Override
                            public void onEvent(Event evt) {
                                switch ((Integer) evt.getData()) {
                                    case MultiLineMessageBox.YES:
                                        try {
                                            deleteBean();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case MultiLineMessageBox.NO:
                                        break;
                                }
                            }

                            private void deleteBean() throws InterruptedException {
                                if (getMainCtrl().getMahasiswa().getId() <= 2) {
                                    try {
                                        ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
                                        return;
                                    } catch (final InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                } else {
                                    try {
                                        getMainCtrl().getMhsservice().delete(deldata);
                                    } catch (DataAccessException e) {
                                        ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                                    }
                                }
                            }
                        }
                    ) == MultiLineMessageBox.YES
                ) {
            }
        }

        this.setMahasiswa(null);
        getMainCtrl().tabList.setSelected(true);

        this.checkAllTab();
    }

    public void doCancel(Event event) { this.doReadOnlyMode(true); }

    public void reLoadPage() {
        binder.loadAll();
        Events.sendEvent(new Event("onSelect", tabPribadi, getMainCtrl().getMahasiswa()));
    }

    private void saveAllBind() {
        getPribadiCtrl().getBinder().saveAll();
        getKhususCtrl().getBinder().saveAll();
        getPendidikanCtrl().getBinder().saveAll();
        getKursusCtrl().getBinder().saveAll();
        getKegiatanCtrl().getBinder().saveAll();
        getPekerjaanCtrl().getBinder().saveAll();
    }

    private void loadAllBind() {
        if(getPribadiCtrl().getBinder() != null)
            getPribadiCtrl().getBinder().loadAll();

        if(getKhususCtrl().getBinder() != null)
            getKhususCtrl().getBinder().loadAll();

        if(getPendidikanCtrl().getBinder() != null)
            getPendidikanCtrl().getBinder().loadAll();

        if(getKursusCtrl().getBinder() != null)
            getKursusCtrl().getBinder().loadAll();

        if(getKegiatanCtrl().getBinder() != null)
            getKegiatanCtrl().getBinder().loadAll();

        if(getPekerjaanCtrl().getBinder() != null)
            getPekerjaanCtrl().getBinder().loadAll();
    }

    private void doReadOnlyMode(boolean b) {
        if(getPribadiCtrl() != null) getPribadiCtrl().doReadOnlyMode(b);
        if(getPribadiCtrl() != null) getKhususCtrl().doReadOnlyMode(b);
        if(getPendidikanCtrl() != null) getPendidikanCtrl().doReadOnlyMode(b);
        if(getKursusCtrl() != null) getKursusCtrl().doReadOnlyMode(b);
        if(getKegiatanCtrl() != null) getKegiatanCtrl().doReadOnlyMode(b);
        if(getPekerjaanCtrl() != null) getPekerjaanCtrl().doReadOnlyMode(b);
    }

    private void checkAllTab() {
        if(getPribadiCtrl() == null)
            Events.sendEvent(new Event("onSelect", tabPribadi, null));
        else if(getPribadiCtrl().getBinder() == null)
            Events.sendEvent(new Event("onSelect", tabPribadi, null));

        if(getKhususCtrl() == null)
            Events.sendEvent(new Event("onSelect", tabKhusus, null));
        else if(getKhususCtrl().getBinder() == null)
            Events.sendEvent(new Event("onSelect", tabKhusus, null));

        if(getPendidikanCtrl() == null)
            Events.sendEvent(new Event("onSelect", tabPendidikan, null));
        else if(getPendidikanCtrl().getBinder() == null)
            Events.sendEvent(new Event("onSelect", tabPendidikan, null));

        if(getKursusCtrl() == null)
            Events.sendEvent(new Event("onSelect", tabKursus, null));
        else if(getKursusCtrl().getBinder() == null)
            Events.sendEvent(new Event("onSelect", tabKursus, null));

        if(getKegiatanCtrl() == null)
            Events.sendEvent(new Event("onSelect", tabKegiatan, null));
        else if(getKegiatanCtrl().getBinder() == null)
            Events.sendEvent(new Event("onSelect", tabKegiatan, null));

        if(getPekerjaanCtrl() == null)
            Events.sendEvent(new Event("onSelect", tabPekerjaan, null));
        else if(getPekerjaanCtrl().getBinder() == null)
            Events.sendEvent(new Event("onSelect", tabPekerjaan, null));
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    private void doReset() {
        getPribadiCtrl().doReset();
    }
    
    public MahasiswaMainCtrl getMainCtrl() {
        return mainCtrl;
    }

    public void setMainCtrl(MahasiswaMainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public MahasiswaPribadiCtrl getPribadiCtrl() {
        return pribadiCtrl;
    }

    public void setPribadiCtrl(MahasiswaPribadiCtrl pribadiCtrl) {
        this.pribadiCtrl = pribadiCtrl;
    }

    public MahasiswaKhususCtrl getKhususCtrl() {
        return khususCtrl;
    }

    public void setKhususCtrl(MahasiswaKhususCtrl khususCtrl) {
        this.khususCtrl = khususCtrl;
    }

    public MahasiswaPendidikanCtrl getPendidikanCtrl() {
        return pendidikanCtrl;
    }

    public void setPendidikanCtrl(MahasiswaPendidikanCtrl pendidikanCtrl) {
        this.pendidikanCtrl = pendidikanCtrl;
    }

    public MahasiswaKursusCtrl getKursusCtrl() {
        return kursusCtrl;
    }

    public void setKursusCtrl(MahasiswaKursusCtrl kursusCtrl) {
        this.kursusCtrl = kursusCtrl;
    }

    public MahasiswaKegiatanCtrl getKegiatanCtrl() {
        return kegiatanCtrl;
    }

    public void setKegiatanCtrl(MahasiswaKegiatanCtrl kegiatanCtrl) {
        this.kegiatanCtrl = kegiatanCtrl;
    }

    public MahasiswaPekerjaanCtrl getPekerjaanCtrl() {
        return pekerjaanCtrl;
    }

    public void setPekerjaanCtrl(MahasiswaPekerjaanCtrl pekerjaanCtrl) {
        this.pekerjaanCtrl = pekerjaanCtrl;
    }

    public Mmahasiswa getMahasiswa() {
        return getMainCtrl().getMahasiswa();
    }

    public void setMahasiswa(Mmahasiswa mahasiswa) {
        getMainCtrl().setMahasiswa(mahasiswa);
    }
}
