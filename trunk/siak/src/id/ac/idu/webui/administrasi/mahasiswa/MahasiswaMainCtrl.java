package id.ac.idu.webui.administrasi.mahasiswa;

import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.MahasiswaService;
import id.ac.idu.backend.model.Mmahasiswa;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.webui.office.report.OfficeSimpleDJReport;
import id.ac.idu.webui.util.*;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.*;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

public class MahasiswaMainCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(MahasiswaMainCtrl.class);

    protected Window windowMahasiswaMain;

    protected Tabbox tabbox_Main;
    protected Tab tabList;
    protected Tab tabDetail;
    protected Tabpanel tabPanelList;
    protected Tabpanel tabPanelDetail;

    protected Button btnNew;
    protected Button btnEdit;
    protected Button btnDelete;
    protected Button btnSave;
    protected Button btnCancel;
    protected Button btnHelp;

    protected Checkbox checkbox_List_ShowAll;
    protected Button button_List_PrintList;

    protected Textbox txtb_src_nim;
    protected Button button_SearchNim;
    protected Textbox txtb_src_nama;
    protected Button button_SearchNama;

    private final String btnCtroller_ClassPrefix = "button_MahasiswaMain_";
    private ButtonStatusCtrl btnCtrl;
    
    private BindingListModelList binding;
    private MahasiswaService service;
    private MahasiswaListCtrl listCtrl;
    private MahasiswaDetailCtrl detailCtrl;

    public MahasiswaMainCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        this.self.setAttribute("controller", this, false);
    }

    public void onCreate$windowMahasiswaMain(Event event) throws Exception {
        windowMahasiswaMain.setContentStyle("padding:0px;");
        btnCtrl = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);
        doCheckRights();
        tabList.setSelected(true);

        if (tabPanelList != null) ZksampleCommonUtils.createTabPanelContent(tabPanelList, this, "ModuleMainController"
                , "/WEB-INF/pages/administrasi/mahasiswa/pageList.zul");
        
        btnCtrl.setInitEdit();
    }

    public void onSelect$tabList(Event event) throws IOException {
        logger.debug(event.toString());

        if (tabPanelList.getFirstChild() != null) {
            tabList.setSelected(true);

            return;
        }

        if (tabPanelList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/mahasiswa/pageList.zul");
        }
    }

    public void onSelect$tabDetail(Event event) throws IOException {
        if (tabPanelDetail.getFirstChild() != null) {
            tabDetail.setSelected(true);
            getDetailCtrl().loadAll();

            return;
        }

        if (tabPanelDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelDetail, this, "ModuleMainController", "/WEB-INF/pages/administrasi/mahasiswa/pageDetail.zul");
        }
    }

    public void onClick$button_List_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        new OfficeSimpleDJReport(win);
    }

    public void onClick$checkbox_List_ShowAll(Event event) {
        txtb_src_nim.setValue("");
        txtb_src_nama.setValue("");
        
        HibernateSearchObject<Mmahasiswa> soData = new HibernateSearchObject<Mmahasiswa>(Mmahasiswa.class, getListCtrl().getCountRows());
        soData.addSort("ckdmtk", false);

        if (getListCtrl().getBinder() != null) {
            getListCtrl().getPagedBindingListWrapper().setSearchObject(soData);

            Tab currentTab = tabbox_Main.getSelectedTab();

            if (!currentTab.equals(tabList)) tabList.setSelected(true);
            else currentTab.setSelected(true);
        }
    }

    private void doCheckRights() {
        final UserWorkspace workspace = getUserWorkspace();

        button_List_PrintList.setVisible(true);
        button_SearchNim.setVisible(true);
        button_SearchNama.setVisible(true);
        btnHelp.setVisible(true);
        btnNew.setVisible(true);
        btnEdit.setVisible(true);
        btnDelete.setVisible(true);
        btnSave.setVisible(true);
    }

    /* Global */
    public void onClick$btnHelp(Event event) throws InterruptedException {
        doHelp(event);
    }

    private void doHelp(Event event) throws InterruptedException {
        ZksampleMessageUtils.doShowNotImplementedMessage();
    }

    public void onClick$btnNew(Event event) throws InterruptedException {
        doNew(event);
    }

    private void doNew(Event event) {
        if (getDetailCtrl() == null) Events.sendEvent(new Event("onSelect", tabDetail, null));
        else Events.sendEvent(new Event("onSelect", tabDetail, null));

        getDetailCtrl().doNew(event);
        btnCtrl.setInitNew();
        tabDetail.setSelected(true);
    }

    public void onClick$btnSave(Event event) throws InterruptedException {
        doSave(event);
        btnCtrl.setInitEdit();
    }

    private void doSave(Event event) throws InterruptedException {
        getDetailCtrl().doSave(event);
    }

    public void onClick$btnEdit(Event event) throws InterruptedException {
        doEdit(event);
    }

    private void doEdit(Event event) {
        Tab currentTab = tabbox_Main.getSelectedTab();

        if (getDetailCtrl() == null) Events.sendEvent(new Event("onSelect", tabDetail, null));
        else Events.sendEvent(new Event("onSelect", tabDetail, null));

        if (!currentTab.equals(tabDetail)) tabDetail.setSelected(true);
        else currentTab.setSelected(true);

        getDetailCtrl().loadAll();
        getDetailCtrl().doStoreInitValues();
        btnCtrl.setBtnStatus_Edit();
        getDetailCtrl().doReadOnlyMode(false);
    }

    public void onClick$btnDelete(Event event) throws InterruptedException {
        doDelete(event);
    }

    private void doDelete(Event event) throws InterruptedException {
        Events.sendEvent(new Event("onSelect", tabDetail, null));

        /*if (getDetailCtrl().getBinder() == null) {
            return;
        }*/

        final Mmahasiswa deldata = getSelected();
        if (deldata != null) {
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + deldata.getCnim();
            final String title = Labels.getLabel("message.Deleting.Record");

            MultiLineMessageBox.doSetTemplate();
            if (MultiLineMessageBox.show(msg, title, Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, true, new EventListener() {
                @Override
                public void onEvent(Event evt) {
                    switch (((Integer) evt.getData()).intValue()) {
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
                    if (getDetailCtrl().getSelected().getId() <= 2) {
                        try {
                            ZksampleMessageUtils.doShowNotAllowedForDemoRecords();
                            return;
                        } catch (final InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            getService().delete(deldata);
                        } catch (DataAccessException e) {
                            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                        }
                    }
                }

            }) == MultiLineMessageBox.YES) {
            }
        }

        btnCtrl.setInitEdit();
        getListCtrl().doFillListbox();
        getDetailCtrl().loadAll();
    }

    public void onClick$btnCancel(Event event) throws InterruptedException {
        doCancel(event);
    }

    private void doCancel(Event event) throws InterruptedException {
        getDetailCtrl().doResetToInitValues();
        getDetailCtrl().loadAll();
        getDetailCtrl().doReadOnlyMode(true);
        btnCtrl.setInitEdit();
    }

    public void onClick$btnRefresh(Event event) throws InterruptedException {
        doResizeSelectedTab(event);
    }

    private void doResizeSelectedTab(Event event) {
        if (tabbox_Main.getSelectedTab() == tabDetail) {
            getDetailCtrl().doFitSize(event);
        } else if (tabbox_Main.getSelectedTab() == tabList) {
            getListCtrl().doFillListbox();
        }
    }
    
    /* get and set */
    public ButtonStatusCtrl getBtnCtrl() {
        return btnCtrl;
    }

    public void setBtnCtr(ButtonStatusCtrl btnCtrl) {
        this.btnCtrl = btnCtrl;
    }

    public BindingListModelList getBinding() {
        return binding;
    }

    public void setBinding(BindingListModelList binding) {
        this.binding = binding;
    }

    public MahasiswaService getService() {
        return service;
    }

    public void setService(MahasiswaService service) {
        this.service = service;
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

    public Mmahasiswa getSelected() {
        return getDetailCtrl().getSelected();
    }

    public void setSelected(Mmahasiswa mahasiswa) {
        getDetailCtrl().setSelected(mahasiswa);
    }
}
