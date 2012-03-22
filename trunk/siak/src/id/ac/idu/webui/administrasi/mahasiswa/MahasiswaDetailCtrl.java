package id.ac.idu.webui.administrasi.mahasiswa;

import id.ac.idu.administrasi.service.MahasiswaService;
import id.ac.idu.backend.model.MkodePos;
import id.ac.idu.backend.model.Mmahasiswa;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.util.Codec;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.ZksampleCommonUtils;
import id.ac.idu.webui.util.ZksampleMessageUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.*;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * User: hermanto
 * Date: 08 Mar 12
 * Time: 16:55:28
 */

public class MahasiswaDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(MahasiswaDetailCtrl.class);

    protected Window windowMahasiswaDetail;
    protected Tabbox tabbox_Detail_Mahasiswa;

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
    protected Tab tabStatus;
    protected Tabpanel tabPanelStatus;

    private transient MahasiswaService service;
    private MahasiswaMainCtrl mainCtrl;
    private MahasiswaPribadiCtrl pribadiCtrl;
    private MahasiswaKhususCtrl khususCtrl;
    private MahasiswaPendidikanCtrl pendidikanCtrl;
    private MahasiswaKursusCtrl kursusCtrl;
    private MahasiswaKegiatanCtrl kegiatanCtrl;
    private MahasiswaPekerjaanCtrl pekerjaanCtrl;
    private MahasiswaStatusCtrl statusCtrl;

    private Mmahasiswa original;
    private Mmahasiswa selected;

    private MkodePos kodepos;

    public MahasiswaDetailCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        this.self.setAttribute("controller", this, false);

        setMainCtrl((MahasiswaMainCtrl) arg.get("ModuleMainController"));
        getMainCtrl().setDetailCtrl(this);
    }

    public void onCreate$windowMahasiswaDetail(Event event) throws Exception {
        tabPribadi.setSelected(true);
        if (tabPanelPribadi != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelPribadi, this, "ModuleMainController", "/WEB-INF/pages/administrasi/mahasiswa/pagePribadi.zul");
        }

        if(getPribadiCtrl().getTxth_cjnsmhs().getValue().equals(Codec.JenisMahasiswa.JenMhs2.getValue()))
            tabKhusus.setVisible(true);
        else
            tabKhusus.setVisible(false);

    }

    public void onSelect$tabPribadi(Event event) throws IOException {
        if (tabPanelPribadi.getFirstChild() != null) {
            tabPribadi.setSelected(true);
            return;
        }
        if (tabPanelPribadi != null)
            ZksampleCommonUtils.createTabPanelContent(tabPanelPribadi, this, "ModuleMainController", "/WEB-INF/pages/administrasi/mahasiswa/pagePribadi.zul");
    }

    public void onSelect$tabKhusus(Event event) throws IOException {
        if (tabPanelKhusus.getFirstChild() != null) {
            tabKhusus.setSelected(true);
            return;
        }
        if (tabPanelKhusus != null)
            ZksampleCommonUtils.createTabPanelContent(tabPanelKhusus, this, "ModuleMainController", "/WEB-INF/pages/administrasi/mahasiswa/pageKhusus.zul");
    }

    public void onSelect$tabPendidikan(Event event) throws IOException {
        if (tabPanelPendidikan.getFirstChild() != null) {
            tabPendidikan.setSelected(true);
            return;
        }
        if (tabPanelPendidikan != null)
            ZksampleCommonUtils.createTabPanelContent(tabPanelPendidikan, this, "ModuleMainController", "/WEB-INF/pages/administrasi/mahasiswa/pagePendidikan.zul");
    }

    public void onSelect$tabKursus(Event event) throws IOException {
        if (tabPanelKursus.getFirstChild() != null) {
            tabKursus.setSelected(true);
            return;
        }
        if (tabPanelKursus != null)
            ZksampleCommonUtils.createTabPanelContent(tabPanelKursus, this, "ModuleMainController", "/WEB-INF/pages/administrasi/mahasiswa/pageKursus.zul");
    }

    public void onSelect$tabKegiatan(Event event) throws IOException {
        if (tabPanelKegiatan.getFirstChild() != null) {
            tabKegiatan.setSelected(true);
            return;
        }
        if (tabPanelKegiatan != null)
            ZksampleCommonUtils.createTabPanelContent(tabPanelKegiatan, this, "ModuleMainController", "/WEB-INF/pages/administrasi/mahasiswa/pageKegiatan.zul");
    }

    public void onSelect$tabPekerjaan(Event event) throws IOException {
        if (tabPanelPekerjaan.getFirstChild() != null) {
            tabPekerjaan.setSelected(true);
            return;
        }
        if (tabPanelPekerjaan != null)
            ZksampleCommonUtils.createTabPanelContent(tabPanelPekerjaan, this, "ModuleMainController", "/WEB-INF/pages/administrasi/mahasiswa/pagePekerjaan.zul");
    }

    public void onSelect$tabStatus(Event event) throws IOException {
        if (tabPanelStatus.getFirstChild() != null) {
            tabStatus.setSelected(true);
            return;
        }
        if (tabPanelStatus != null)
            ZksampleCommonUtils.createTabPanelContent(tabPanelStatus, this, "ModuleMainController", "/WEB-INF/pages/administrasi/mahasiswa/pageStatus.zul");
    }

    public void doNew(Event event) {
        doStoreInitValues();
        final Mmahasiswa anObject = getService().getNew();
        getPribadiCtrl().setSelected(anObject);

        if (getPribadiCtrl().getBinder() != null)
            getPribadiCtrl().getBinder().loadAll();

        getPribadiCtrl().doReadOnlyMode(false);
    }

    public void doSave(Event event) throws InterruptedException {
        getPribadiCtrl().getBinder().saveAll();
        try {
            getService().saveOrUpdate(getPribadiCtrl().getSelected());
            doStoreInitValues();
            getMainCtrl().getListCtrl().doFillListbox();
            Events.postEvent("onSelect", getMainCtrl().getListCtrl().getListBox(), getSelected());
            String str = getSelected().getCnim();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));
        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
            doResetToInitValues();

            return;
        } finally {
            getPribadiCtrl().doReadOnlyMode(true);
        }
    }

    public void doNewPribadi(Event event) {
        if (getPribadiCtrl() == null) Events.sendEvent(new Event("onSelect", tabPribadi, null));
        else Events.sendEvent(new Event("onSelect", tabPribadi, null));

        doStoreInitValues();

        final Mmahasiswa anObject = getService().getNew();

        getPribadiCtrl().setSelected(anObject);
        getPribadiCtrl().setSelected(getSelected());
        getPribadiCtrl().getBinder().loadAll();
        getPribadiCtrl().doReadOnlyMode(false);
    }

    public void doStoreInitValues() {
        if (getSelected() != null) {
            try {
                setOriginal((Mmahasiswa) ZksampleBeanUtils.cloneBean(getSelected()));
            } catch (final IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (final InstantiationException e) {
                throw new RuntimeException(e);
            } catch (final InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (final NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void doResetToInitValues() {
        if (getOriginal() != null) {

            try {
                setSelected((Mmahasiswa) ZksampleBeanUtils.cloneBean(getOriginal()));
                getMainCtrl().windowMahasiswaMain.invalidate();
            } catch (final IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (final InstantiationException e) {
                throw new RuntimeException(e);
            } catch (final InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (final NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void doFitSize(Event event) {
        windowMahasiswaDetail.invalidate();

        if(tabbox_Detail_Mahasiswa.getSelectedTab() == tabPribadi)
            getPribadiCtrl().doFitSize(event);
    }

    public void doReadOnlyMode(boolean b) {
        getPribadiCtrl().doReadOnlyMode(b);
        getPendidikanCtrl().doReadOnlyMode(b);
        getKhususCtrl().doReadOnlyMode(b);
    }

    public BindingListModelList getBinding() {
        return getMainCtrl().getBinding();
    }

    public void setBinding(BindingListModelList _obj) {
        getMainCtrl().setBinding(_obj);
    }

    public void loadAll() {
        if(getPribadiCtrl().getBinder() != null) {
            getPribadiCtrl().setSelected(getSelected());
            getPribadiCtrl().getBinder().loadAll();
        }
    }

    public void saveAll() {
        getPribadiCtrl().getBinder().saveAll();
    }
    
    public MahasiswaMainCtrl getMainCtrl() {
        return mainCtrl;
    }

    public void setMainCtrl(MahasiswaMainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public MahasiswaService getService() {
        return service;
    }

    public void setService(MahasiswaService service) {
        this.service = service;
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

    public MahasiswaStatusCtrl getStatusCtrl() {
        return statusCtrl;
    }

    public void setStatusCtrl(MahasiswaStatusCtrl statusCtrl) {
        this.statusCtrl = statusCtrl;
    }

    public MkodePos getKodepos() {
        return kodepos;
    }

    public void setKodepos(MkodePos kodepos) {
        this.kodepos = kodepos;
    }

    public Mmahasiswa getOriginal() {
        return original;
    }

    public void setOriginal(Mmahasiswa original) {
        this.original = original;
    }

    public Mmahasiswa getSelected() {
        return selected;
    }

    public void setSelected(Mmahasiswa selected) {
        this.selected = selected;
    }
}
