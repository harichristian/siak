package id.ac.idu.webui.administrasi.updatekonsentrasi;

import id.ac.idu.administrasi.service.MahasiswaService;
import id.ac.idu.backend.model.Mmahasiswa;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.webui.util.ButtonStatusCtrl;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.ZksampleCommonUtils;
import id.ac.idu.webui.util.ZksampleMessageUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.*;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 29 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class UpdateKonsentrasiMainCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(UpdateKonsentrasiMainCtrl.class);

    protected Window windowUpdateKonsentrasiMain;

    /* Main Tab */
    protected Tabbox tabboxMain;
    protected Tab tabList;
    protected Tabpanel tabPanelList;
    protected Tab tabDetail;
    protected Tabpanel tabPanelDetail;

    private final String BTN_PREIFX = "BTN_KONSENTRASI_";
    private BindingListModelList binding;

    /* Main Button Control */
    protected ButtonStatusCtrl btnCtrl;
    protected Button btnNew;
    protected Button btnEdit;
    protected Button btnDelete;
    protected Button btnSave;
    protected Button btnCancel;
    protected Button btnHelp;

    /* Page Controller */
    private UpdateKonsentrasiListCtrl listCtrl;
    private UpdateKonsentrasiDetailCtrl detailCtrl;

    /* Serach text */
    protected Textbox txtbNim;
    protected Textbox txtbNama;

    private MahasiswaService service;

    private Mmahasiswa mahasiswa;
    private Mmahasiswa original;

    public UpdateKonsentrasiMainCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        logger.info("Page Update Konsentrasi Loaded");

        super.doAfterCompose(window);
        self.setAttribute("controller", this, false);
    }

    public void onCreate$windowUpdateKonsentrasiMain(Event event) throws Exception {
        windowUpdateKonsentrasiMain.setContentStyle("padding:0px;");
        btnCtrl = new ButtonStatusCtrl(getUserWorkspace(), BTN_PREIFX, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

        doCheckRights();
        tabList.setSelected(true);

        if (tabPanelList != null) {
            if(tabPanelList.getFirstChild() == null) ZksampleCommonUtils.createTabPanelContent(tabPanelList, this
                    , "ModuleMainController" , "/WEB-INF/pages/administrasi/updatekonsentrasi/pageList.zul");
        }
    }

    public void onSelect$tabList(Event event) throws IOException {
        if (tabPanelList.getFirstChild() != null) {
            tabList.setSelected(true);

            return;
        }

        if (tabPanelList != null) {
            if(tabPanelList.getFirstChild() == null)
                ZksampleCommonUtils.createTabPanelContent(tabPanelList, this , "ModuleMainController", "/WEB-INF/pages/administrasi/updatekonsentrasi/pageList.zul");
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
                ZksampleCommonUtils.createTabPanelContent(tabPanelDetail, this , "ModuleMainController", "/WEB-INF/pages/administrasi/updatekonsentrasi/pageDetail.zul");
        }
    }

    public void onClick$btnRefresh(Event event) throws InterruptedException {
        if (tabboxMain.getSelectedTab() == tabDetail) Events.sendEvent(new Event("onSelect", tabDetail, null));
        else if (tabboxMain.getSelectedTab() == tabList) getListCtrl().loadListData();
    }

    public void onClick$buttonSearch(Event event) {
        if(getListCtrl().getBinding() != null) {
            getListCtrl().loadListData();

            Tab currentTab = tabboxMain.getSelectedTab();

            if (!currentTab.equals(tabList)) tabList.setSelected(true);
            else currentTab.setSelected(true);
        }
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

    public void onClick$btnSave(Event event) throws Exception {
        doSave(event);
    }

    public void doSave(Event event) throws Exception{
        try {
            getService().saveOrUpdate(getMahasiswa());

            doStoreInitValues();
            getListCtrl().loadListData();
            Events.postEvent("onSelect", getListCtrl().getListBox(), getMahasiswa());
            String str = getMahasiswa().getCnim();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));
        }
        catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
            doResetToInitValues();

            return;
        }
        finally {
            btnCtrl.setInitEdit();
            getDetailCtrl().doReadOnlyMode(true);
        }
    }

    public void onClick$btnEdit(Event event) {
        Tab currentTab = tabboxMain.getSelectedTab();

        if (getDetailCtrl() == null)
            Events.sendEvent(new Event("onSelect", tabDetail, null));
        else if (getDetailCtrl().getBinder() == null)
            Events.sendEvent(new Event("onSelect", tabDetail, null));

        if (!currentTab.equals(tabDetail)) tabDetail.setSelected(true);
        else currentTab.setSelected(true);

        if(getMahasiswa() == null) return;

        getDetailCtrl().getBinder().loadAll();

        doStoreInitValues();
        btnCtrl.setBtnStatus_Edit();
        getDetailCtrl().doReadOnlyMode(false);
    }

    public void onClick$btnCancel(Event event) {
        doResetToInitValues();

        if (getDetailCtrl().getBinder() != null) {
            getDetailCtrl().getBinder().loadAll();
            getDetailCtrl().doReadOnlyMode(true);
            btnCtrl.setInitEdit();
        }
    }

    private void doCheckRights() {
        btnHelp.setVisible(true);
        btnNew.setVisible(false);
        btnEdit.setVisible(true);
        btnDelete.setVisible(false);
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

    public void doResetToInitValues() {

        if (getOriginal() != null) {

            try {
                setMahasiswa((Mmahasiswa) ZksampleBeanUtils.cloneBean(getOriginal()));
                windowUpdateKonsentrasiMain.invalidate();
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

    public UpdateKonsentrasiListCtrl getListCtrl() {
        return listCtrl;
    }

    public void setListCtrl(UpdateKonsentrasiListCtrl listCtrl) {
        this.listCtrl = listCtrl;
    }

    public UpdateKonsentrasiDetailCtrl getDetailCtrl() {
        return detailCtrl;
    }

    public void setDetailCtrl(UpdateKonsentrasiDetailCtrl detailCtrl) {
        this.detailCtrl = detailCtrl;
    }

    public BindingListModelList getBinding() {
        return binding;
    }

    public void setBinding(BindingListModelList binding) {
        this.binding = binding;
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

    public MahasiswaService getService() {
        return service;
    }

    public void setService(MahasiswaService service) {
        this.service = service;
    }
}
