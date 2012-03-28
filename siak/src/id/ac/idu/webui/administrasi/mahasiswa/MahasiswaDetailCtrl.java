package id.ac.idu.webui.administrasi.mahasiswa;

import id.ac.idu.backend.model.Mhistpangkatmhs;
import id.ac.idu.backend.model.Mmahasiswa;
import id.ac.idu.backend.model.Mmhspascakhs;
import id.ac.idu.backend.model.Mppumhskhusus;
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

    private MahasiswaMainCtrl mainCtrl;
    private MahasiswaPribadiCtrl pribadiCtrl;
    private MahasiswaKhususCtrl khususCtrl;
    private MahasiswaPendidikanCtrl pendidikanCtrl;

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

    public void doNew(Event event) {
        final Mmhspascakhs nMmhspascakhs = getMainCtrl().getMmhspascakhsService().getNew();
        final Mmahasiswa nData = getMainCtrl().getMhsservice().getNew();
        nData.setMmhspascakhs(nMmhspascakhs);
        
        this.setMahasiswa(nData);
        this.checkAllTab();
        this.loadAllBind();
        this.doReadOnlyMode(false);
        this.doReset();
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

        try {
            getMainCtrl().getMahasiswa().getMppumhskhususes().clear();
            getMainCtrl().getMahasiswa().getMhistpangkatmhses().clear();
            if(getMahasiswa().getId() > 0)  {
                getMainCtrl().getMahasiswa().getMppumhskhususes().addAll(mppumhskhususes);
                getMainCtrl().getMahasiswa().getMhistpangkatmhses().addAll(mhistpangkatmhses);
            }
            else {
                getMainCtrl().getMahasiswa().setMppumhskhususes(mppumhskhususes);
                getMainCtrl().getMahasiswa().setMhistpangkatmhses(mhistpangkatmhses);
            }

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

    private void checkAllTab() {
        if(getPribadiCtrl() == null)
            Events.sendEvent(new Event("onSelect", tabPribadi, null));
        else if(getPribadiCtrl().getBinder() == null)
            Events.sendEvent(new Event("onSelect", tabPribadi, null));

        if(getKhususCtrl() == null)
            Events.sendEvent(new Event("onSelect", tabKhusus, null));
        else if(getKhususCtrl().getBinder() == null)
            Events.sendEvent(new Event("onSelect", tabKhusus, null));
    }

    private void loadAllBind() {
        if(getPribadiCtrl().getBinder() != null)
            getPribadiCtrl().getBinder().loadAll();

        if(getKhususCtrl().getBinder() != null)
            getKhususCtrl().getBinder().loadAll();
    }

    public void reLoadPage() {
        binder.loadAll();
        Events.sendEvent(new Event("onSelect", tabPribadi, getMainCtrl().getMahasiswa()));
    }

    private void saveAllBind() {
        getPribadiCtrl().getBinder().saveAll();
        getKhususCtrl().getBinder().saveAll();
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    private void doReadOnlyMode(boolean b) {
        getPribadiCtrl().doReadOnlyMode(b);
        getKhususCtrl().doReadOnlyMode(b);
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

    public Mmahasiswa getMahasiswa() {
        return getMainCtrl().getMahasiswa();
    }

    public void setMahasiswa(Mmahasiswa mahasiswa) {
        getMainCtrl().setMahasiswa(mahasiswa);
    }
}
