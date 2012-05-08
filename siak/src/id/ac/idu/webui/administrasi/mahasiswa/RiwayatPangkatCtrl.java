package id.ac.idu.webui.administrasi.mahasiswa;

import com.trg.search.Filter;
import id.ac.idu.backend.model.Mhistpangkatmhs;
import id.ac.idu.backend.model.Mpangkatgolongan;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.webui.administrasi.mahasiswa.model.KhususSearchPangkatList;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.MultiLineMessageBox;
import id.ac.idu.webui.util.pagging.PagedListWrapper;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.*;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 26 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class RiwayatPangkatCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(RiwayatPangkatCtrl.class);

    protected Window windowRiwayatPangkat;
    
    protected Button btnSave;
    protected Button btnDelete;
    protected Button btnCancel;
    protected Button btnClose;

    private String oldVar_txtbCkdPangkat;
    private String oldVar_txtbNmPangkat;
    private String oldVar_txtbTmt;
    private String oldVar_txtbKet;

    /* Lov Pangkat */
    private transient PagedListWrapper<Mpangkatgolongan> plwPangkat;
    protected Bandbox cmbPangkat;
    protected Paging pagingPangkat;
    protected Listbox listPangkat;
    protected Textbox txtbPangkat;
    protected Textbox txtbNmPangkat;
    protected Textbox tbPangkat;
    protected Textbox tbName;

    protected Listheader code;
    protected Listheader name;

    private int pageSize;

    protected Textbox txtbTmt;
    protected Textbox txtbKet;

    private transient Mhistpangkatmhs mhistpangkatmhs;
    private transient MahasiswaKhususCtrl mahasiswaKhususCtrl;
    private transient Listbox listRiwayatPangkat;
    private transient boolean isnew;

    private AnnotateDataBinder binder;

    public RiwayatPangkatCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        logger.info("Page Detail Riwayat Pendidikan");

        super.doAfterCompose(window);
        self.setAttribute("controller", this, false);

        if (arg.containsKey("ModuleMainController")) {
            setMahasiswaKhususCtrl((MahasiswaKhususCtrl) arg.get("ModuleMainController"));
            getMahasiswaKhususCtrl().setRiwayatPangkatCtrl(this);
        }
    }

    @SuppressWarnings("unchecked")
    public void onCreate$windowRiwayatPangkat(Event event) throws Exception {
        setPageSize(20);
        
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        Map<String, Object> args = getCreationArgsMap(event);

        if (args.containsKey(MahasiswaKhususCtrl.DATA)) {
            Mhistpangkatmhs anRiwayat = (Mhistpangkatmhs) args.get(MahasiswaKhususCtrl.DATA);
            setMhistpangkatmhs(anRiwayat);
        }

        if (args.containsKey(MahasiswaKhususCtrl.LIST)) listRiwayatPangkat = (Listbox) args.get(MahasiswaKhususCtrl.LIST);
        else listRiwayatPangkat = null;

        if (args.containsKey(MahasiswaKhususCtrl.PANGKAT_CONTROL)) mahasiswaKhususCtrl = (MahasiswaKhususCtrl) args.get(MahasiswaKhususCtrl.PANGKAT_CONTROL);
        else mahasiswaKhususCtrl = null;

        isnew = args.containsKey(MahasiswaKhususCtrl.ISNEW) && (Boolean) args.get(MahasiswaKhususCtrl.ISNEW);

        this.doInitButton(getMahasiswaKhususCtrl().isModeEdit());
        this.doReadOnlyMode(!getMahasiswaKhususCtrl().isModeEdit());

        binder.loadAll();
        this.doStoreInitValues();
        this.doShowDialog(getMhistpangkatmhs());
    }

    public void onOpen$cmbPangkat(Event event) {
        this.searchPangkat(null, null);
    }

    public void onClick$buttonSearchPkt(Event event) {
        Filter filter1 = null;
        Filter filter2 = null;

        if (StringUtils.isNotEmpty(tbPangkat.getValue()))
            filter1 = new Filter("ckdpangkatgolongan", "%" + tbPangkat.getValue() + "%", Filter.OP_ILIKE);

        if (StringUtils.isNotEmpty(tbName.getValue()))
            filter2 = new Filter("cnmpangkatgolongan", "%" + tbPangkat.getValue() + "%", Filter.OP_ILIKE);

        this.searchPangkat(filter1, filter2);
    }

    private void searchPangkat(Filter filter1, Filter filter2) {
        HibernateSearchObject<Mpangkatgolongan> soPangkat = new HibernateSearchObject<Mpangkatgolongan>(Mpangkatgolongan.class);

        code.setSortAscending(new FieldComparator("ckdpangkatgolongan",true));
        code.setSortDescending(new FieldComparator("ckdpangkatgolongan",false));
        name.setSortAscending(new FieldComparator("cnmpangkatgolongan",true));
        name.setSortDescending(new FieldComparator("cnmpangkatgolongan",false));

        if(filter1 != null) soPangkat.addFilter(filter1);
        if(filter2 != null) soPangkat.addFilter(filter2);

        soPangkat.addSort("ckdpangkatgolongan", false);
        pagingPangkat.setPageSize(pageSize);
		pagingPangkat.setDetailed(true);
		getPlwPangkat().init(soPangkat, listPangkat, pagingPangkat);
		listPangkat.setItemRenderer(new KhususSearchPangkatList());
    }

    public void onPangkatItem(Event event) {
        Listitem item = listPangkat.getSelectedItem();

        if (item != null) {
            Mpangkatgolongan aPangkat = (Mpangkatgolongan) item.getAttribute(MahasiswaKhususCtrl.LIST_DATA);
            txtbPangkat.setValue(aPangkat.getCkdpangkatgolongan());
            txtbNmPangkat.setValue(aPangkat.getCnmpangkatgolongan());
            getMhistpangkatmhs().setMpangkatgolongan(aPangkat);
        }

        cmbPangkat.close();
    }

    public void onClick$buttonClosePkt(Event event) {
        cmbPangkat.close();
	}

    public void onClick$btnClose(Event event) throws InterruptedException {
        try {
            doClose();
        } catch (final Exception e) {
            windowRiwayatPangkat.onClose();
        }
    }

    public void onClick$btnSave(Event event) throws InterruptedException {
        doSave();
    }

    public void doSave() throws InterruptedException {
        binder.saveAll();

        List _lst = new ArrayList();
        _lst.addAll(listRiwayatPangkat.getItems());
        int idx = listRiwayatPangkat.getSelectedIndex();

        ListModelList lml = (ListModelList) listRiwayatPangkat.getListModel();
        lml.clear();

        for(Object onPendidikan : _lst) {
            Listitem item = (Listitem) onPendidikan;
            lml.add((Mhistpangkatmhs) item.getAttribute(MahasiswaKhususCtrl.LIST_DATA));
        }

        if(!this.isnew) lml.set(idx, getMhistpangkatmhs());
        else lml.add(listRiwayatPangkat.getItems().size(), getMhistpangkatmhs());

        doStoreInitValues();
        windowRiwayatPangkat.onClose();
    }

    public void onClick$btnDelete() {
        Listitem item = listRiwayatPangkat.getSelectedItem();
        getMahasiswaKhususCtrl().getDelHisPangkat().add((Mhistpangkatmhs) item.getAttribute(MahasiswaKhususCtrl.LIST_DATA));

        listRiwayatPangkat.removeItemAt(listRiwayatPangkat.getSelectedIndex());
        windowRiwayatPangkat.onClose();
    }

    public void onClick$btnCancel(Event event) {
        doResetInitValues();
    }

    private void doClose() throws Exception {
        if (isDataChanged()) {
            String msg = Labels.getLabel("message_Data_Modified_Save_Data_YesNo");
            String title = Labels.getLabel("message.Information");

            MultiLineMessageBox.doSetTemplate();
            if (MultiLineMessageBox.show(msg, title, MultiLineMessageBox.YES | MultiLineMessageBox.NO, MultiLineMessageBox.QUESTION, true, new EventListener() {
                @Override
                public void onEvent(Event evt) {
                    switch (((Integer) evt.getData()).intValue()) {
                        case MultiLineMessageBox.YES:
                            try {
                                doSave();
                            } catch (final InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        case MultiLineMessageBox.NO:
                            break; //
                    }
                }
            }

            ) == MultiLineMessageBox.YES) {
            }
        }

        windowRiwayatPangkat.onClose();
    }

    private boolean isDataChanged() {
        boolean changed = false;

        if (!oldVar_txtbCkdPangkat.equals(txtbPangkat.getValue())) changed = true;

        if (!oldVar_txtbNmPangkat.equals(txtbNmPangkat.getValue())) changed = true;

        if (!oldVar_txtbTmt.equals(txtbTmt.getValue())) changed = true;

        if (!oldVar_txtbKet.equals(txtbKet.getValue())) changed = true;

        return changed;
    }

    private void doShowDialog(Mhistpangkatmhs mhistpangkatmhs) throws InterruptedException {
        try {
            windowRiwayatPangkat.doModal();
        } catch (final Exception e) {
            Messagebox.show(e.toString());
        }
    }

    private void doStoreInitValues() {
        if(isnew) return;
        
        oldVar_txtbCkdPangkat = txtbPangkat.getValue();
        oldVar_txtbNmPangkat = txtbNmPangkat.getValue();
        oldVar_txtbTmt = txtbTmt.getValue();
        oldVar_txtbKet = txtbKet.getValue();
    }

    private void doResetInitValues() {
        txtbPangkat.setValue(oldVar_txtbCkdPangkat);
        txtbNmPangkat.setValue(oldVar_txtbNmPangkat);
        txtbTmt.setValue(oldVar_txtbTmt);
        txtbKet.setValue(oldVar_txtbKet);
    }

    private void doInitButton(boolean b) {
        btnSave.setVisible(b);
        btnDelete.setVisible(b);
        btnCancel.setVisible(b);

        if(isnew) btnDelete.setVisible(false);
    }

    private void doReadOnlyMode(boolean b) {
        txtbTmt.setReadonly(b);
        txtbKet.setReadonly(b);
        cmbPangkat.setDisabled(b);
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public MahasiswaKhususCtrl getMahasiswaKhususCtrl() {
        return mahasiswaKhususCtrl;
    }

    public void setMahasiswaKhususCtrl(MahasiswaKhususCtrl mahasiswaKhususCtrl) {
        this.mahasiswaKhususCtrl = mahasiswaKhususCtrl;
    }

    public Mhistpangkatmhs getMhistpangkatmhs() {
        return mhistpangkatmhs;
    }

    public void setMhistpangkatmhs(Mhistpangkatmhs mhistpangkatmhs) {
        this.mhistpangkatmhs = mhistpangkatmhs;
    }

    public PagedListWrapper<Mpangkatgolongan> getPlwPangkat() {
        return plwPangkat;
    }

    public void setPlwPangkat(PagedListWrapper<Mpangkatgolongan> plwPangkat) {
        this.plwPangkat = plwPangkat;
    }
}
