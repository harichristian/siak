package id.ac.idu.webui.irs.aktifmhs;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.administrasi.service.MahasiswaService;
import id.ac.idu.administrasi.service.StatusMahasiswaService;
import id.ac.idu.backend.model.Mmahasiswa;
import id.ac.idu.backend.model.Mstatusmhs;
import id.ac.idu.backend.model.Tcutimhs;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.irs.service.CutimhsService;
import id.ac.idu.util.Codec;
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

public class MainCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(MainCtrl.class);

    protected Window windowAktifmhsMain;

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
    
    protected Textbox txtb_search1;
	protected Datebox txtb_search2;
	protected Textbox txtb_search3;
	protected Button buttonSearch;

    private final String btnCtroller_ClassPrefix = "button_Ctrl_Main";
    private ButtonStatusCtrl btnCtrl;

    private Tcutimhs original;
    private Tcutimhs selected;
    private Mmahasiswa mahasiswa;

    private BindingListModelList binding;
    private CutimhsService service;
    private MahasiswaService service2;
    private StatusMahasiswaService statusService;

    private ListCtrl listCtrl;
    private DetailCtrl detailCtrl;

    public MainCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        this.self.setAttribute("controller", this, false);
    }

    public void onCreate$windowAktifmhsMain(Event event) throws Exception {
        windowAktifmhsMain.setContentStyle("padding:0px;");

        btnCtrl = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

        doCheckRights();
        tabList.setSelected(true);

        if (tabPanelList != null) ZksampleCommonUtils.createTabPanelContent(tabPanelList, this, "ModuleMainController"
                , "/WEB-INF/pages/irs/aktifmhs/pageList.zul");

        btnCtrl.setInitEdit();
    }

    public void onSelect$tabList(Event event) throws IOException {
        logger.debug(event.toString());

        if (tabPanelList.getFirstChild() != null) {
            tabList.setSelected(true);

            return;
        }

        if (tabPanelList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelList, this, "ModuleMainController"
                    , "/WEB-INF/pages/irs/aktifmhs/pageList.zul");
        }
    }

    public void onSelect$tabDetail(Event event) throws IOException {
        if (tabPanelDetail.getFirstChild() != null) {
            tabDetail.setSelected(true);

            getDetailCtrl().setSelected(getSelected());
            if(getDetailCtrl().getBinder() != null) getDetailCtrl().getBinder().loadAll();
            return;
        }

        if (tabPanelDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelDetail, this, "ModuleMainController"
                    , "/WEB-INF/pages/irs/aktifmhs/pageDetail.zul");
        }
    }

    public void onClick$button_List_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        new OfficeSimpleDJReport(win);
    }

    public void onClick$checkbox_List_ShowAll(Event event) {
        txtb_search1.setValue("");
        txtb_search2.setValue(null);
        txtb_search3.setValue("");
        this.searchList();
    }

    public void onClick$buttonSearch(Event event) {
        Filter filter1 = null;
        Filter filter2 = null;
        Filter filter3 = null;

        if (StringUtils.isNotEmpty(txtb_search1.getValue()))
            filter1 = new Filter("cnosurat", "%" + txtb_search1.getValue() + "%", Filter.OP_LIKE);

        if (txtb_search2.getValue() != null)
            filter2 = new Filter("dtglsurat", txtb_search2.getValue() , Filter.OP_EQUAL);

        if (StringUtils.isNotEmpty(txtb_search3.getValue()))
            filter3 = new Filter("mmahasiswa.cnim", "%" + txtb_search3.getValue() + "%", Filter.OP_LIKE);

        this.searchList(filter1, filter2, filter3);
    }

    public void searchList(Filter... filters) {
        HibernateSearchObject<Tcutimhs> soData = new HibernateSearchObject<Tcutimhs>(Tcutimhs.class , getListCtrl().getCountRows());
        if(filters != null) {
            for(Filter onFilter : filters) {
                if(onFilter!=null) soData.addFilter(onFilter);
            }
        }
        soData.addFilter(new com.trg.search.Filter("cjenis", Codec.JenisSurat.Status1.getValue(), com.trg.search.Filter.OP_EQUAL));
        soData.addSort("cnosurat", false);

        if (getListCtrl().getBinder() != null) {
            getListCtrl().getPagedBindingListWrapper().setSearchObject(soData);

            Tab currentTab = tabbox_Main.getSelectedTab();

            if (!currentTab.equals(tabList)) tabList.setSelected(true);
            else currentTab.setSelected(true);
        }
    }

    private void doCheckRights() {
        final UserWorkspace workspace = getUserWorkspace();

        buttonSearch.setVisible(true);
//        btnHelp.setVisible(true);
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

        final Tcutimhs anObject = getService().getNew();

        getDetailCtrl().setSelected(anObject);
        getDetailCtrl().setSelected(getSelected());

        if (getDetailCtrl().getBinder() != null)
            getDetailCtrl().getBinder().loadAll();

        btnCtrl.setInitNew();
        tabDetail.setSelected(true);
        getDetailCtrl().doReadOnlyMode(false);
    }

    public void onClick$btnSave(Event event) throws InterruptedException {
        doSave(event);
    }

    private void doSave(Event event) throws InterruptedException {
        final Mstatusmhs anStatus = getStatusService().getStatusMahasiswaById(Integer.valueOf(Codec.JenisSurat.Status1.getValue()));
        getMahasiswa().setMstatusmhs(anStatus);
        
        getDetailCtrl().getSelected().setCjenis(Codec.JenisSurat.Status1.getValue());
        getDetailCtrl().getSelected().setMmahasiswa(getMahasiswa());
        getDetailCtrl().getBinder().saveAll();
        try {
            getService().saveOrUpdate(getDetailCtrl().getSelected());
            getService2().saveOrUpdate(getMahasiswa());
            doStoreInitValues();
            getListCtrl().doFillListbox();
            Events.postEvent("onSelect", getListCtrl().getListBox(), getSelected());
            String str = getSelected().getCnosurat();
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

    public Mmahasiswa calculateMahasiswa() {
        Mmahasiswa mhs = getMahasiswa();
        mhs.getMstatusmhs().setCkdstatmhs(Codec.StatusMahasiswa.Status1.getValue());

        return mhs;
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

        if(getDetailCtrl().getSelected() == null)  {
            final Tcutimhs anObject = getService().getNew();
            getDetailCtrl().setSelected(anObject);
            getDetailCtrl().setSelected(getSelected());
        }

        getDetailCtrl().getBinder().loadAll();

        doStoreInitValues();
        btnCtrl.setBtnStatus_Edit();
        getDetailCtrl().doReadOnlyMode(false);
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

        final Tcutimhs deldata = getSelected();
        if (deldata != null) {
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + deldata.getCnosurat();
            final String title = Labels.getLabel("message.Deleting.Record");

            MultiLineMessageBox.doSetTemplate();
            if (MultiLineMessageBox.show(msg, title, Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, true, new EventListener() {
                @Override
                public void onEvent(Event evt) {
                    switch (((Integer) evt.getData()).intValue()) {
                        case MultiLineMessageBox.YES:
                            try {
                                deleteBean();
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        case MultiLineMessageBox.NO:
                            break;
                    }
                }

                private void deleteBean() throws InterruptedException {
                    try {
                        getService().delete(deldata);
                    }
                    catch (DataAccessException e) {
                        ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
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
                setOriginal((Tcutimhs) ZksampleBeanUtils.cloneBean(getSelected()));
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
                setSelected((Tcutimhs) ZksampleBeanUtils.cloneBean(getOriginal()));
                windowAktifmhsMain.invalidate();
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

    public Tcutimhs getOriginal() {
        return original;
    }

    public void setOriginal(Tcutimhs original) {
        this.original = original;
    }

    public Tcutimhs getSelected() {
        if(selected != null) {
            Date dtnow = new Date();
            SimpleDateFormat dtformat = new SimpleDateFormat("mmss");
            String stnow = "ATMS" + dtformat.format(dtnow);

            if(selected.getCnosurat() == null) selected.setCnosurat(stnow);
            if(selected.getDtglsurat() == null) selected.setDtglsurat(dtnow);
        }

        return selected;
    }

    public void setSelected(Tcutimhs selected) {
        this.selected = selected;
    }

    public BindingListModelList getBinding() {
        return binding;
    }

    public void setBinding(BindingListModelList binding) {
        this.binding = binding;
    }

    public CutimhsService getService() {
        return service;
    }

    public void setService(CutimhsService service) {
        this.service = service;
    }

    public MahasiswaService getService2() {
        return service2;
    }

    public void setService2(MahasiswaService service2) {
        this.service2 = service2;
    }

    public StatusMahasiswaService getStatusService() {
        return statusService;
    }

    public void setStatusService(StatusMahasiswaService statusService) {
        this.statusService = statusService;
    }

    public ListCtrl getListCtrl() {
        return listCtrl;
    }

    public void setListCtrl(ListCtrl listCtrl) {
        this.listCtrl = listCtrl;
    }

    public DetailCtrl getDetailCtrl() {
        return detailCtrl;
    }

    public void setDetailCtrl(DetailCtrl detailCtrl) {
        this.detailCtrl = detailCtrl;
    }

    public Mmahasiswa getMahasiswa() {
        return mahasiswa;
    }

    public void setMahasiswa(Mmahasiswa mahasiswa) {
        this.mahasiswa = mahasiswa;
    }
}
