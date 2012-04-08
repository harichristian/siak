package id.ac.idu.webui.mankurikulum.sesi;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.backend.model.Msesikuliah;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.mankurikulum.service.SesiService;
import id.ac.idu.webui.office.report.OfficeSimpleDJReport;
import id.ac.idu.webui.util.*;
import org.apache.commons.lang.StringUtils;
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

public class SesiMainCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(SesiMainCtrl.class);

    protected Window windowSesiMain;

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

    protected Textbox txtb_Sesi_Code;
    protected Textbox txtb_Sesi_KodeSekolah;
    protected Textbox txtb_Sesi_JamAwal;
    protected Textbox txtb_Sesi_JamAkhir;
    protected Button buttonSesiList;

    private final String btnCtroller_ClassPrefix = "button_SesiMain_";
    private ButtonStatusCtrl btnCtrl;

    private Msesikuliah original;
    private Msesikuliah selected;
    private BindingListModelList binding;

    private SesiService service;

    private SesiListCtrl listCtrl;
    private SesiDetailCtrl detailCtrl;

    public SesiMainCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        this.self.setAttribute("controller", this, false);
    }

    public void onCreate$windowSesiMain(Event event) throws Exception {
        windowSesiMain.setContentStyle("padding:0px;");

        btnCtrl = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

        doCheckRights();
        tabList.setSelected(true);

        if (tabPanelList != null) ZksampleCommonUtils.createTabPanelContent(tabPanelList, this, "ModuleMainController"
                , "/WEB-INF/pages/mankurikulum/sesi/sesiList.zul");

        btnCtrl.setInitEdit();
    }

    public void onSelect$tabList(Event event) throws IOException {
        logger.debug(event.toString());

        if (tabPanelList.getFirstChild() != null) {
            tabList.setSelected(true);

            return;
        }

        if (tabPanelList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelList, this, "ModuleMainController", "/WEB-INF/pages/mankurikulum/sesi/sesiList.zul");
        }
    }

    public void onSelect$tabDetail(Event event) throws IOException {
        if (tabPanelDetail.getFirstChild() != null) {
            tabDetail.setSelected(true);

            getDetailCtrl().setSelected(getSelected());
            if(getDetailCtrl().getBinder() != null)
                getDetailCtrl().getBinder().loadAll();
            return;
        }

        if (tabPanelDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelDetail, this, "ModuleMainController", "/WEB-INF/pages/mankurikulum/sesi/sesiDetail.zul");
        }
    }

    public void onClick$checkbox_List_ShowAll(Event event) {
        txtb_Sesi_Code.setValue("");
        txtb_Sesi_JamAwal.setValue("");
        txtb_Sesi_JamAkhir.setValue("");
        this.searchList();
    }

    public void onClick$buttonSesiList(Event event) {
        Filter filter1 = null;
        Filter filter2 = null;
        Filter filter3 = null;

        if (StringUtils.isNotEmpty(txtb_Sesi_Code.getValue()))
            filter1 = new Filter("ckdsesi", "%" + txtb_Sesi_Code.getValue() + "%", Filter.OP_LIKE);

        if (StringUtils.isNotEmpty(txtb_Sesi_JamAwal.getValue()))
            filter2 = new Filter("cjamawal", txtb_Sesi_JamAwal.getValue() , Filter.OP_LIKE);

        if (StringUtils.isNotEmpty(txtb_Sesi_JamAkhir.getValue()))
            filter3 = new Filter("cjamakhir", "%" + txtb_Sesi_JamAkhir.getValue() + "%", Filter.OP_LIKE);

        this.searchList(filter1, filter2, filter3);
    }

    public void searchList(Filter... filters) {
        HibernateSearchObject<Msesikuliah> soData = new HibernateSearchObject<Msesikuliah>(Msesikuliah.class, getListCtrl().getCountRows());
        if(filters != null) {
            for(Filter anFilter : filters) {
                if(anFilter!=null) soData.addFilter(anFilter);
            }
        }

        soData.addSort("ckdsesi", false);

        if (getListCtrl().getBinder() != null) {
            getListCtrl().getPagedBindingListWrapper().setSearchObject(soData);

            Tab currentTab = tabbox_Main.getSelectedTab();

            if (!currentTab.equals(tabList)) tabList.setSelected(true);
            else currentTab.setSelected(true);
        }
    }

    private void doCheckRights() {
        final UserWorkspace workspace = getUserWorkspace();

        buttonSesiList.setVisible(true);
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
        if (getDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabDetail, null));
        } else if (getDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabDetail, null));
        }

        doStoreInitValues();

        final Msesikuliah anSesi = getService().getNewSesi();

        getDetailCtrl().setSelected(anSesi);
        getDetailCtrl().setSelected(getSelected());

        if (getDetailCtrl().getBinder() != null)
            getDetailCtrl().getBinder().loadAll();

        getDetailCtrl().doReadOnlyMode(false);
        btnCtrl.setInitNew();
        tabDetail.setSelected(true);
        getDetailCtrl().txtb_ckdsesi.focus();
    }

    public void onClick$btnSave(Event event) throws InterruptedException {
        doSave(event);
    }

    private void doSave(Event event) throws InterruptedException {
        getDetailCtrl().getBinder().saveAll();
        try {
            getService().saveOrUpdate(getDetailCtrl().getSelected());
            doStoreInitValues();
            getListCtrl().doFillListbox();
            Events.postEvent("onSelect", getListCtrl().getListBox(), getSelected());
            String str = getSelected().getCkdsesi();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));
        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
            doResetToInitValues();

            return;
        } finally {
            btnCtrl.setInitEdit();
            getDetailCtrl().doReadOnlyMode(true);
        }
    }

    public void onClick$btnEdit(Event event) throws InterruptedException {
        doEdit(event);
    }

    private void doEdit(Event event) {
        Tab currentTab = tabbox_Main.getSelectedTab();

        if (getDetailCtrl() == null)
            Events.sendEvent(new Event("onSelect", tabDetail, null));
        else if (getDetailCtrl().getBinder() == null)
            Events.sendEvent(new Event("onSelect", tabDetail, null));

        if (!currentTab.equals(tabDetail))
            tabDetail.setSelected(true);
        else
            currentTab.setSelected(true);

        getDetailCtrl().getBinder().loadAll();
        doStoreInitValues();
        btnCtrl.setBtnStatus_Edit();
        getDetailCtrl().doReadOnlyMode(false);
        getDetailCtrl().txtb_ckdsesi.focus();
    }

    public void onClick$btnDelete(Event event) throws InterruptedException {
        doDelete(event);
    }

    private void doDelete(Event event) throws InterruptedException {
        if (getDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabDetail, null));
        }

        if (getDetailCtrl().getBinder() == null) {
            return;
        }

        final Msesikuliah deldata = getSelected();
        if (deldata != null) {
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + deldata.getCkdsesi();
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
        setSelected(null);
        getListCtrl().doFillListbox();
        getDetailCtrl().getBinder().loadAll();
    }

    public void onClick$btnCancel(Event event) throws InterruptedException {
        doCancel(event);
    }

    private void doCancel(Event event) throws InterruptedException {
        doResetToInitValues();

        if (getDetailCtrl().getBinder() != null) {
            getDetailCtrl().getBinder().loadAll();
            getDetailCtrl().doReadOnlyMode(true);
            btnCtrl.setInitEdit();
        }
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

    public void doStoreInitValues() {
        if (getSelected() != null) {
            try {
                setOriginal((Msesikuliah) ZksampleBeanUtils.cloneBean(getSelected()));
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
                setSelected((Msesikuliah) ZksampleBeanUtils.cloneBean(getOriginal()));
                windowSesiMain.invalidate();
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

    /* get and set */
    public ButtonStatusCtrl getBtnCtrl() {
        return btnCtrl;
    }

    public void setBtnCtr(ButtonStatusCtrl btnCtrl) {
        this.btnCtrl = btnCtrl;
    }

    public Msesikuliah getOriginal() {
        return original;
    }

    public void setOriginal(Msesikuliah original) {
        this.original = original;
    }

    public Msesikuliah getSelected() {
        return selected;
    }

    public void setSelected(Msesikuliah selected) {
        this.selected = selected;
    }

    public BindingListModelList getBinding() {
        return binding;
    }

    public void setBinding(BindingListModelList binding) {
        this.binding = binding;
    }

    public SesiService getService() {
        return service;
    }

    public void setService(SesiService service) {
        this.service = service;
    }

    public SesiListCtrl getListCtrl() {
        return listCtrl;
    }

    public void setListCtrl(SesiListCtrl listCtrl) {
        this.listCtrl = listCtrl;
    }

    public SesiDetailCtrl getDetailCtrl() {
        return detailCtrl;
    }

    public void setDetailCtrl(SesiDetailCtrl detailCtrl) {
        this.detailCtrl = detailCtrl;
    }
}
