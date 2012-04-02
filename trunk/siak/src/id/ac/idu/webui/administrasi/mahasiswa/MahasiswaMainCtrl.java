package id.ac.idu.webui.administrasi.mahasiswa;

import id.ac.idu.administrasi.service.*;
import id.ac.idu.backend.model.Mmahasiswa;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.webui.util.ButtonStatusCtrl;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.ZksampleCommonUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.*;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public class MahasiswaMainCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(MahasiswaMainCtrl.class);

    private final String BTN_PREIFX = "BTN_MHS_";

    /* service */
    private MahasiswaService mhsservice;
    private MmhspascakhsService mmhspascakhsService;
    private MppumhskhususService mppumhskhususService;
    private MhistpangkatmhsService mhistpangkatmhsService;
    private MhistkursusmhsService mhistkursusmhsService;
    private MpbahasamhsService mpbahasamhsService;
    private MkgtmhsService mkgtmhsService;
    private MkaryamhsService mkaryamhsService;
    private MprestasimhsService mprestasimhsService;
    private StatusMahasiswaService statusMahasiswaService;
    private MhistpnddkmhsService mhistpnddkmhsService;

    /* model */
    private Mmahasiswa mahasiswa;
    private Mmahasiswa original;

    /* Binding Object */
    private BindingListModelList binding;

    /*** Object Page ZUL ***/
    protected Window windowMahasiswaMain;

    /* Main Button Control */
    protected ButtonStatusCtrl btnCtrl;
    protected Button btnNew;
    protected Button btnEdit;
    protected Button btnDelete;
    protected Button btnSave;
    protected Button btnCancel;
    protected Button btnHelp;
    protected Textbox txtbNim;
    protected Textbox txtbNama;

    /* Main Tab */
    protected Tabbox tabboxMain;
    protected Tab tabList;
    protected Tabpanel tabPanelList;
    protected Tab tabDetail;
    protected Tabpanel tabPanelDetail;

    /* Page Controller */
    private MahasiswaListCtrl listCtrl;
    private MahasiswaDetailCtrl detailCtrl;

    public MahasiswaMainCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        logger.info("Page Mahasiswa Loaded");
        
        super.doAfterCompose(window);
        self.setAttribute("controller", this, false);
    }

    public void onCreate$windowMahasiswaMain(Event event) throws Exception {
        windowMahasiswaMain.setContentStyle("padding:0px;");
        btnCtrl = new ButtonStatusCtrl(getUserWorkspace(), BTN_PREIFX, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

        doCheckRights();
        tabList.setSelected(true);

        if (tabPanelList != null) {
            if(tabPanelList.getFirstChild() == null) ZksampleCommonUtils.createTabPanelContent(tabPanelList, this
                    , "ModuleMainController" , "/WEB-INF/pages/administrasi/mahasiswa/pageList.zul");
        }
    }

    public void onSelect$tabList(Event event) throws IOException {
        if (tabPanelList.getFirstChild() != null) {
            tabList.setSelected(true);

            return;
        }
        
        if (tabPanelList != null) {
            if(tabPanelList.getFirstChild() == null)
                ZksampleCommonUtils.createTabPanelContent(tabPanelList, this
                        , "ModuleMainController", "/WEB-INF/pages/administrasi/mahasiswa/pageList.zul");
        }
    }

    public void onSelect$tabDetail(Event event) throws IOException {
        if(tabPanelDetail.getFirstChild() != null) {
            tabDetail.setSelected(true);
            getDetailCtrl().reLoadPage();
            
            return;
        }
        
        if (tabPanelDetail != null) {
            if(tabPanelDetail.getFirstChild() == null)
                ZksampleCommonUtils.createTabPanelContent(tabPanelDetail, this
                        , "ModuleMainController", "/WEB-INF/pages/administrasi/mahasiswa/pageDetail.zul");
        }
    }

    public void onClick$btnNew(Event event) throws InterruptedException {
        btnCtrl.setInitNew();
        if(getDetailCtrl() == null)
            Events.sendEvent(new Event("onSelect", tabDetail, null));
        else if(getDetailCtrl().getBinder() == null)
            Events.sendEvent(new Event("onSelect", tabDetail, null));

        tabDetail.setSelected(true);

        this.doStoreInitValues();
        getDetailCtrl().doNew(event);
    }

    public void onClick$btnEdit(Event event) throws InterruptedException {
        btnCtrl.setBtnStatus_Edit();
        Tab currentTab = tabboxMain.getSelectedTab();
        if(!currentTab.equals(tabDetail)) tabDetail.setSelected(true);
        
        this.doStoreInitValues();

        if(getDetailCtrl() == null)
            Events.sendEvent(new Event("onSelect", tabDetail, null));
        else if(getDetailCtrl().getBinder() == null)
            Events.sendEvent(new Event("onSelect", tabDetail, null));

        getDetailCtrl().doEdit(event);
    }

    public void onClick$btnSave(Event event) throws InterruptedException {
        this.doStoreInitValues();
        getDetailCtrl().doSave(event);
        btnCtrl.setInitEdit();
    }

    public void onClick$btnDelete(Event event) throws InterruptedException {
        if(getDetailCtrl() == null)
            Events.sendEvent(new Event("onSelect", tabDetail, null));
        else if(getDetailCtrl().getBinder() == null)
            Events.sendEvent(new Event("onSelect", tabDetail, null));
        
        getDetailCtrl().doDelete(event);
        btnCtrl.setInitEdit();
        getListCtrl().loadListData();
    }

    public void onClick$btnCancel(Event event) throws InterruptedException {
        btnCtrl.setInitEdit();
        getDetailCtrl().doCancel(event);
    }

    public void onCheck$checkboxShowAll(Event event) {
        txtbNim.setValue("");
        txtbNama.setValue("");

        if(getListCtrl().getBinding() != null) {
            getListCtrl().loadListData();

            Tab currentTab = tabboxMain.getSelectedTab();

            if (!currentTab.equals(tabList)) tabList.setSelected(true);
            else currentTab.setSelected(true);
        }
    }

    public void onClick$buttonSearch(Event event) {
        if(getListCtrl().getBinding() != null) {
            getListCtrl().loadListData();

            Tab currentTab = tabboxMain.getSelectedTab();

            if (!currentTab.equals(tabList)) tabList.setSelected(true);
            else currentTab.setSelected(true);
        }
    }

    public void onClick$btnRefresh(Event event) throws InterruptedException {
        if (tabboxMain.getSelectedTab() == tabDetail)
            Events.sendEvent(new Event("onSelect", tabDetail, null));
        else if (tabboxMain.getSelectedTab() == tabList)
            getListCtrl().loadListData();
    }

    private void doCheckRights() {
        btnHelp.setVisible(true);
        btnNew.setVisible(true);
        btnEdit.setVisible(true);
        btnDelete.setVisible(true);
        btnSave.setVisible(false);
    }

    public void doStoreInitValues() {
        if (getMahasiswa() != null) {
            try {
                final Mmahasiswa anData = (Mmahasiswa) ZksampleBeanUtils.cloneBean(getMahasiswa());
                setOriginal(anData);
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

    /* Getet Seter  */
    public MahasiswaService getMhsservice() {
        return mhsservice;
    }

    public void setMhsservice(MahasiswaService mhsservice) {
        this.mhsservice = mhsservice;
    }

    public MmhspascakhsService getMmhspascakhsService() {
        return mmhspascakhsService;
    }

    public void setMmhspascakhsService(MmhspascakhsService mmhspascakhsService) {
        this.mmhspascakhsService = mmhspascakhsService;
    }

    public MppumhskhususService getMppumhskhususService() {
        return mppumhskhususService;
    }

    public void setMppumhskhususService(MppumhskhususService mppumhskhususService) {
        this.mppumhskhususService = mppumhskhususService;
    }

    public MhistpangkatmhsService getMhistpangkatmhsService() {
        return mhistpangkatmhsService;
    }

    public void setMhistpangkatmhsService(MhistpangkatmhsService mhistpangkatmhsService) {
        this.mhistpangkatmhsService = mhistpangkatmhsService;
    }

    public MhistkursusmhsService getMhistkursusmhsService() {
        return mhistkursusmhsService;
    }

    public void setMhistkursusmhsService(MhistkursusmhsService mhistkursusmhsService) {
        this.mhistkursusmhsService = mhistkursusmhsService;
    }

    public MpbahasamhsService getMpbahasamhsService() {
        return mpbahasamhsService;
    }

    public void setMpbahasamhsService(MpbahasamhsService mpbahasamhsService) {
        this.mpbahasamhsService = mpbahasamhsService;
    }

    public MkgtmhsService getMkgtmhsService() {
        return mkgtmhsService;
    }

    public void setMkgtmhsService(MkgtmhsService mkgtmhsService) {
        this.mkgtmhsService = mkgtmhsService;
    }

    public MkaryamhsService getMkaryamhsService() {
        return mkaryamhsService;
    }

    public void setMkaryamhsService(MkaryamhsService mkaryamhsService) {
        this.mkaryamhsService = mkaryamhsService;
    }

    public MprestasimhsService getMprestasimhsService() {
        return mprestasimhsService;
    }

    public void setMprestasimhsService(MprestasimhsService mprestasimhsService) {
        this.mprestasimhsService = mprestasimhsService;
    }

    public StatusMahasiswaService getStatusMahasiswaService() {
        return statusMahasiswaService;
    }

    public void setStatusMahasiswaService(StatusMahasiswaService statusMahasiswaService) {
        this.statusMahasiswaService = statusMahasiswaService;
    }

    public MhistpnddkmhsService getMhistpnddkmhsService() {
        return mhistpnddkmhsService;
    }

    public void setMhistpnddkmhsService(MhistpnddkmhsService mhistpnddkmhsService) {
        this.mhistpnddkmhsService = mhistpnddkmhsService;
    }

    public Mmahasiswa getMahasiswa() {
        return mahasiswa;
    }

    public void setMahasiswa(Mmahasiswa mahasiswa) {
        this.mahasiswa = mahasiswa;
    }

    public Mmahasiswa getOriginal() {
        return original;
    }

    public void setOriginal(Mmahasiswa original) {
        this.original = original;
    }

    public BindingListModelList getBinding() {
        return binding;
    }

    public void setBinding(BindingListModelList binding) {
        this.binding = binding;
    }

    public MahasiswaListCtrl getListCtrl() {
        return listCtrl;
    }

    public void setListCtrl(MahasiswaListCtrl listCtrl) {
        this.listCtrl = listCtrl;
    }

    public MahasiswaDetailCtrl getDetailCtrl() {
        return detailCtrl;
    }

    public void setDetailCtrl(MahasiswaDetailCtrl detailCtrl) {
        this.detailCtrl = detailCtrl;
    }
}
