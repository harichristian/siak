package id.ac.idu.webui.administrasi.mahasiswa;

import id.ac.idu.backend.model.Mppumhskhusus;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.MultiLineMessageBox;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 25 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class RiwayatPendidikanCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(RiwayatPendidikanCtrl.class);

    protected Window windowRiwayatPendidikan;

    protected Button btnSave;
    protected Button btnDelete;
    protected Button btnCancel;
    protected Button btnClose;

    protected Textbox txtbNo;
    protected Textbox txtbNmBentuk;
    protected Textbox txtbTahun;
    protected Textbox txtbKet;

    private String oldVar_txtbNo;
    private String oldVar_txtbNmBentuk;
    private String oldVar_txtbTahun;
    private String oldVar_txtbKet;

    private transient Mppumhskhusus mppumhskhusus;
    private transient Listbox listRiwayatPendidikan;
    private transient MahasiswaKhususCtrl mahasiswaKhususCtrl;
    private transient boolean isnew;

    private AnnotateDataBinder binder;

    public RiwayatPendidikanCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        logger.info("Page Detail Riwayat Pendidikan");

        super.doAfterCompose(window);
        self.setAttribute("controller", this, false);

        if (arg.containsKey("ModuleMainController")) {
            setMahasiswaKhususCtrl((MahasiswaKhususCtrl) arg.get("ModuleMainController"));
            getMahasiswaKhususCtrl().setRiwayatPendidikanCtrl(this);
        }
    }

    @SuppressWarnings("unchecked")
    public void onCreate$windowRiwayatPendidikan(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        Map<String, Object> args = getCreationArgsMap(event);

        if (args.containsKey(MahasiswaKhususCtrl.DATA)) {
            Mppumhskhusus anRiwayat = (Mppumhskhusus) args.get(MahasiswaKhususCtrl.DATA);
            setMppumhskhusus(anRiwayat);
        } 

        if (args.containsKey(MahasiswaKhususCtrl.LIST)) listRiwayatPendidikan = (Listbox) args.get(MahasiswaKhususCtrl.LIST);
        else listRiwayatPendidikan = null;

        if (args.containsKey(MahasiswaKhususCtrl.KHUSUS_CONTROL)) mahasiswaKhususCtrl = (MahasiswaKhususCtrl) args.get(MahasiswaKhususCtrl.KHUSUS_CONTROL);
        else mahasiswaKhususCtrl = null;

        isnew = args.containsKey(MahasiswaKhususCtrl.ISNEW) && (Boolean) args.get(MahasiswaKhususCtrl.ISNEW);

        this.doInitButton(getMahasiswaKhususCtrl().isModeEdit());
        this.doReadOnlyMode(!getMahasiswaKhususCtrl().isModeEdit());

        binder.loadAll();
        this.doStoreInitValues();
        this.doShowDialog(getMppumhskhusus());
    }

    public void onClick$btnClose(Event event) throws InterruptedException {
        try {
            doClose();
        } catch (final Exception e) {
            windowRiwayatPendidikan.onClose();
        }
    }

    public void onClick$btnSave(Event event) throws InterruptedException {
        doSave();
    }

    public void doSave() throws InterruptedException {
        binder.saveAll();

        List _lst = new ArrayList();
        _lst.addAll(listRiwayatPendidikan.getItems());
        int idx = listRiwayatPendidikan.getSelectedIndex();


        ListModelList lml = (ListModelList) listRiwayatPendidikan.getListModel();
        if(lml == null) lml = new ListModelList();
        else {
            lml.clear();
            for(Object onPendidikan : _lst) {
                Listitem item = (Listitem) onPendidikan;
                lml.add((Mppumhskhusus) item.getAttribute(MahasiswaKhususCtrl.LIST_DATA));
            }
        }
        
        if(!this.isnew) lml.set(idx, getMppumhskhusus());
        else lml.add(listRiwayatPendidikan.getItems().size(), getMppumhskhusus());

        doStoreInitValues();
        windowRiwayatPendidikan.onClose();
    }

    public void onClick$btnDelete() {
        Listitem item = listRiwayatPendidikan.getSelectedItem();
        getMahasiswaKhususCtrl().getDelMppum().add((Mppumhskhusus) item.getAttribute(MahasiswaKhususCtrl.LIST_DATA));

        listRiwayatPendidikan.removeItemAt(listRiwayatPendidikan.getSelectedIndex());
        windowRiwayatPendidikan.onClose();
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

        windowRiwayatPendidikan.onClose();
    }

    private boolean isDataChanged() {
        boolean changed = false;
        
        if (!oldVar_txtbNo.equals(txtbNo.getValue())) changed = true;

        if (!oldVar_txtbNmBentuk.equals(txtbNmBentuk.getValue())) changed = true;

        if (!oldVar_txtbTahun.equals(txtbTahun.getValue())) changed = true;

        if (!oldVar_txtbKet.equals(txtbKet.getValue())) changed = true;

        return changed;
    }

    private void doShowDialog(Mppumhskhusus mppumhskhusus) throws InterruptedException {
        try {
            windowRiwayatPendidikan.doModal();
        } catch (final Exception e) {
            Messagebox.show(e.toString());
        }
    }

    private void doStoreInitValues() {
        if(isnew) return;
        
        oldVar_txtbNo = txtbNo.getValue();
        oldVar_txtbNmBentuk = txtbNmBentuk.getValue();
        oldVar_txtbTahun = txtbTahun.getValue();
        oldVar_txtbKet = txtbKet.getValue();
    }

    private void doResetInitValues() {
        txtbNo.setValue(oldVar_txtbNo);
        txtbNmBentuk.setValue(oldVar_txtbNmBentuk);
        txtbTahun.setValue(oldVar_txtbTahun);
        txtbKet.setValue(oldVar_txtbKet);
    }

    private void doInitButton(boolean b) {
        btnSave.setVisible(b);
        btnDelete.setVisible(b);
        btnCancel.setVisible(b);
    }

    private void doReadOnlyMode(boolean b) {
        txtbNo.setReadonly(b);
        txtbNmBentuk.setReadonly(b);
        txtbTahun.setReadonly(b);
        txtbKet.setReadonly(b);
    }

    public Mppumhskhusus getMppumhskhusus() {
        return mppumhskhusus;
    }

    public void setMppumhskhusus(Mppumhskhusus mppumhskhusus) {
        this.mppumhskhusus = mppumhskhusus;
    }

    public MahasiswaKhususCtrl getMahasiswaKhususCtrl() {
        return mahasiswaKhususCtrl;
    }

    public void setMahasiswaKhususCtrl(MahasiswaKhususCtrl mahasiswaKhususCtrl) {
        this.mahasiswaKhususCtrl = mahasiswaKhususCtrl;
    }
}
