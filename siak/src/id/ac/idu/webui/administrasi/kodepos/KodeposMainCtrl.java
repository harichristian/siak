package id.ac.idu.webui.administrasi.kodepos;

import id.ac.idu.administrasi.service.KodePosService;
import id.ac.idu.backend.model.MkodePos;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.webui.util.*;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.*;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 30 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class KodeposMainCtrl extends GFCBaseCtrl implements Serializable {

    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(KodeposMainCtrl.class);

    protected Window windowKodepos;

    private final String BTN_PREIFX = "BTN_KODEPOS_";

    /* Main Button Control */
    protected ButtonStatusCtrl btnCtrl;
    protected Button btnNew;
    protected Button btnEdit;
    protected Button btnDelete;
    protected Button btnSave;
    protected Button btnCancel;
    protected Button btnHelp;

    /* Search Panel */
    protected Textbox txtbId;
    protected Textbox txtbKodePos;

    /* Main Tab */
    protected Tabbox tabboxMain;
    protected Tab tabList;
    protected Tabpanel tabPanelList;
    protected Tab tabDetail;
    protected Tabpanel tabPanelDetail;

    /* Page Controller */
    private KodeposListCtrl listCtrl;
    private KodeposDetailCtrl detailCtrl;

    /* Service */
    private KodePosService kodePosService;

    /* Binding Object */
    private BindingListModelList binding;
    private MkodePos mkodePos;
    private MkodePos original;

    public KodeposMainCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        logger.info("Page Kodepos Loaded");

        super.doAfterCompose(window);
        self.setAttribute("controller", this, false);
    }

    public void onCreate$windowKodepos(Event event) throws Exception {
        windowKodepos.setContentStyle("padding:0px;");
        btnCtrl = new ButtonStatusCtrl(getUserWorkspace(), BTN_PREIFX, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

        doCheckRights();
        tabList.setSelected(true);

        if (tabPanelList != null) {
            if(tabPanelList.getFirstChild() == null) ZksampleCommonUtils.createTabPanelContent(tabPanelList, this
                    , "ModuleMainController" , "/WEB-INF/pages/administrasi/kodepos/pageList.zul");
        }
    }

    public void onSelect$tabList(Event event) throws IOException {
        if (tabPanelList.getFirstChild() != null) {
            tabList.setSelected(true);

            return;
        }

        if(tabPanelList != null)
            ZksampleCommonUtils.createTabPanelContent(tabPanelList, this, "ModuleMainController", "/WEB-INF/pages/administrasi/kodepos/pageList.zul");
    }

    public void onSelect$tabDetail(Event event) throws IOException {
        if(tabPanelDetail.getFirstChild() != null) {
            tabDetail.setSelected(true);

            getDetailCtrl().setMkodePos(getMkodePos());
            getDetailCtrl().getBinder().loadAll();
            
            return;
        }

        if(tabPanelDetail != null)
            ZksampleCommonUtils.createTabPanelContent(tabPanelDetail, this , "ModuleMainController", "/WEB-INF/pages/administrasi/kodepos/pageDetail.zul");
    }

    public void onCheck$checkboxShowAll(Event event) {
        txtbId.setValue("");
        txtbKodePos.setValue("");

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

    public void onClick$btnNew(Event event) throws InterruptedException {
        doNew(event);
    }

    public void onClick$btnEdit(Event event) throws InterruptedException {
        doEdit(event);
    }

    public void onClick$btnSave(Event event) throws InterruptedException {
        doSave(event);
    }

    public void onClick$btnDelete(Event event) throws InterruptedException {
        doDelete(event);
    }

    public void onClick$btnCancel(Event event) throws InterruptedException {
        doCancel(event);
    }

    private void doNew(Event event) {
        if (getDetailCtrl() == null)
            Events.sendEvent(new Event("onSelect", tabDetail, null));
        else if (getDetailCtrl().getBinder() == null)
            Events.sendEvent(new Event("onSelect", tabDetail, null));

        doStoreInitValues();
        final MkodePos anKodepos = getKodePosService().getNewKodePos();
        getDetailCtrl().setMkodePos(anKodepos);
        
        getDetailCtrl().setMkodePos(getMkodePos());
        if(getDetailCtrl().getBinder() != null)
            getDetailCtrl().getBinder().loadAll();
        getDetailCtrl().doReadOnlyMode(false);

        btnCtrl.setInitNew();
        tabDetail.setSelected(true);
        getDetailCtrl().txtbkodepos.focus();
    }

    private void doEdit(Event event) {
        Tab currentTab = tabboxMain.getSelectedTab();

        if (getDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabDetail, null));
        } else if (getDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabDetail, null));
        }

        if (!currentTab.equals(tabDetail)) tabDetail.setSelected(true);
        else currentTab.setSelected(true);

        if(getDetailCtrl().getBinder()!=null) {
            getDetailCtrl().getBinder().loadAll();
        }

        doStoreInitValues();
        btnCtrl.setBtnStatus_Edit();
        getDetailCtrl().doReadOnlyMode(false);
        getDetailCtrl().txtbkodepos.focus();
    }

    private void doSave(Event event) throws InterruptedException {
        getDetailCtrl().getBinder().saveAll();

        try {
            getKodePosService().saveOrUpdate(getMkodePos());
            doStoreInitValues();
            getListCtrl().loadListData();
            Events.postEvent("onSelect", getListCtrl().getListBox(), getMkodePos());

            String str = getMkodePos().getKodepos();
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

    private void doDelete(Event event) throws InterruptedException {
        if (getDetailCtrl().getBinder() == null)
            Events.sendEvent(new Event("onSelect", tabDetail, null));

        // check first, if the tabs are created
        if (getDetailCtrl().getBinder() == null) {
            return;
        }

        final MkodePos anKodepos = getMkodePos();
        if (anKodepos != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anKodepos.getKodepos();
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
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            break; //
                        case MultiLineMessageBox.NO:
                            break; //
                    }
                }

                private void deleteBean() throws InterruptedException {
                    try {
                        getKodePosService().delete(anKodepos);
                    } catch (DataAccessException e) {
                        ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                    }
                }

            }

            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrl.setInitEdit();
        setMkodePos(null);
        getListCtrl().loadListData();
        getDetailCtrl().getBinder().loadAll();
    }

    private void doCancel(Event event) {
        doResetToInitValues();
        if (getDetailCtrl().getBinder() != null) {

            getDetailCtrl().getBinder().loadAll();
            getDetailCtrl().doReadOnlyMode(true);
             btnCtrl.setInitEdit();
        }
    }

    private void doCheckRights() {
//        btnHelp.setVisible(true);
        btnNew.setVisible(true);
        btnEdit.setVisible(true);
        btnDelete.setVisible(true);
        btnSave.setVisible(false);
    }

    public void doStoreInitValues() {
        if (getMkodePos() != null) {
            try {
                final MkodePos anData = (MkodePos) ZksampleBeanUtils.cloneBean(getMkodePos());
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
                setMkodePos((MkodePos) ZksampleBeanUtils.cloneBean(getOriginal()));
                windowKodepos.invalidate();

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

    public KodeposDetailCtrl getDetailCtrl() {
        return detailCtrl;
    }

    public void setDetailCtrl(KodeposDetailCtrl detailCtrl) {
        this.detailCtrl = detailCtrl;
    }

    public KodeposListCtrl getListCtrl() {
        return listCtrl;
    }

    public void setListCtrl(KodeposListCtrl listCtrl) {
        this.listCtrl = listCtrl;
    }

    public KodePosService getKodePosService() {
        return kodePosService;
    }

    public void setKodePosService(KodePosService kodePosService) {
        this.kodePosService = kodePosService;
    }

    public BindingListModelList getBinding() {
        return binding;
    }

    public void setBinding(BindingListModelList binding) {
        this.binding = binding;
    }

    public MkodePos getMkodePos() {
        return mkodePos;
    }

    public void setMkodePos(MkodePos mkodePos) {
        this.mkodePos = mkodePos;
    }

    public MkodePos getOriginal() {
        return original;
    }

    public void setOriginal(MkodePos original) {
        this.original = original;
    }
}
