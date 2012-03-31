package id.ac.idu.webui.administrasi.mahasiswa;

import id.ac.idu.backend.model.Mkaryamhs;
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
 * @Date 30 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class KaryaCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(KaryaCtrl.class);

    protected Window windowKaryaDtl;
    protected Listbox listKarya;
    protected MahasiswaKegiatanCtrl mahasiswaKegiatanCtrl;
    protected boolean isnew;

    private AnnotateDataBinder binder;

    protected Mkaryamhs mkaryamhs;

    protected Button btnSave;
    protected Button btnDelete;
    protected Button btnCancel;
    protected Button btnClose;

    protected Textbox txtbcno;
    protected Textbox txtbcnmkyrilm;
    protected Textbox txtbcmediaterbit;
    protected Textbox txtbcthnterbit;
    protected Textbox txtbcket;

    private String oldVar_txtbcno;
    private String oldVar_txtbcnmkyrilm;
    private String oldVar_txtbcmediaterbit;
    private String oldVar_txtbcthnterbit;
    private String oldVar_txtbcket;

    public KaryaCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        logger.info("Page Detail Riwayat Pendidikan");

        super.doAfterCompose(window);
        self.setAttribute("controller", this, false);

        if (arg.containsKey("ModuleMainController")) {
            setMahasiswaKegiatanCtrl((MahasiswaKegiatanCtrl) arg.get("ModuleMainController"));
            getMahasiswaKegiatanCtrl().setKaryaCtrl(this);
        }
    }

    @SuppressWarnings("unchecked")
    public void onCreate$windowKaryaDtl(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        Map<String, Object> args = getCreationArgsMap(event);

        if (args.containsKey(MahasiswaKegiatanCtrl.DATA)) {
            Mkaryamhs anData = (Mkaryamhs) args.get(MahasiswaKegiatanCtrl.DATA);
            setMkaryamhs(anData);
        }

        if (args.containsKey(MahasiswaKegiatanCtrl.LIST))
            listKarya = (Listbox) args.get(MahasiswaKegiatanCtrl.LIST);
        else
            listKarya = null;

        if (args.containsKey(MahasiswaKegiatanCtrl.CONTROL)) mahasiswaKegiatanCtrl = (MahasiswaKegiatanCtrl) args.get(MahasiswaKegiatanCtrl.CONTROL);
        else mahasiswaKegiatanCtrl = null;

        isnew = args.containsKey(MahasiswaKegiatanCtrl.ISNEW) && (Boolean) args.get(MahasiswaKegiatanCtrl.ISNEW);

        this.doInitButton(getMahasiswaKegiatanCtrl().isModeEdit());
        this.doReadOnlyMode(!getMahasiswaKegiatanCtrl().isModeEdit());

        binder.loadAll();
        this.doShowDialog(getMkaryamhs());
        this.doStoreInitValues();
    }

    public void onClick$btnClose(Event event) throws InterruptedException {
        try {
            doClose();
        } catch (final Exception e) {
            windowKaryaDtl.onClose();
        }
    }

    public void onClick$btnSave(Event event) throws InterruptedException {
        doSave();
    }

    public void onClick$btnDelete() {
        Listitem item = listKarya.getSelectedItem();
        getMahasiswaKegiatanCtrl().getDelKarya().add((Mkaryamhs) item.getAttribute(MahasiswaKegiatanCtrl.DATA));

        listKarya.removeItemAt(listKarya.getSelectedIndex());
        windowKaryaDtl.onClose();
    }

    public void onClick$btnCancel(Event event) {
        doResetInitValues();
    }

    public void doSave() throws InterruptedException {
        binder.saveAll();

        List _lst = new ArrayList();
        _lst.addAll(listKarya.getItems());
        int idx = listKarya.getSelectedIndex();


        ListModelList lml = (ListModelList) listKarya.getListModel();
        if(lml == null) lml = new ListModelList();
        else {
            lml.clear();
            for(Object onPendidikan : _lst) {
                Listitem item = (Listitem) onPendidikan;
                lml.add((Mkaryamhs) item.getAttribute(MahasiswaKegiatanCtrl.DATA));
            }
        }

        if(!this.isnew) lml.set(idx, getMkaryamhs());
        else lml.add(listKarya.getItems().size(), getMkaryamhs());

        doStoreInitValues();
        windowKaryaDtl.onClose();
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

        windowKaryaDtl.onClose();
    }

    private void doInitButton(boolean b) {
        btnSave.setVisible(b);
        btnDelete.setVisible(b);
        btnCancel.setVisible(b);

        if(isnew) btnDelete.setVisible(false);
    }

    private void doReadOnlyMode(boolean b) {
        txtbcno.setReadonly(b);
        txtbcnmkyrilm.setReadonly(b);
        txtbcmediaterbit.setReadonly(b);
        txtbcthnterbit.setReadonly(b);
        txtbcket.setReadonly(b);
    }

    private void doResetInitValues() {
        txtbcno.setValue(oldVar_txtbcno);
        txtbcnmkyrilm.setValue(oldVar_txtbcnmkyrilm);
        txtbcmediaterbit.setValue(oldVar_txtbcmediaterbit);
        txtbcthnterbit.setValue(oldVar_txtbcthnterbit);
        txtbcket.setValue(oldVar_txtbcket);
    }

    private void doStoreInitValues() {
        if(isnew) return;

        oldVar_txtbcno=txtbcno.getValue();
        oldVar_txtbcnmkyrilm=txtbcnmkyrilm.getValue();
        oldVar_txtbcmediaterbit=txtbcmediaterbit.getValue();
        oldVar_txtbcthnterbit=txtbcthnterbit.getValue();
        oldVar_txtbcket=txtbcket.getValue();
    }

    private boolean isDataChanged() {
        boolean changed = false;

        if(!oldVar_txtbcno.equals(txtbcno.getValue())) changed = true;
        if(!oldVar_txtbcnmkyrilm.equals(txtbcnmkyrilm.getValue())) changed = true;
        if(!oldVar_txtbcmediaterbit.equals(txtbcmediaterbit.getValue())) changed = true;
        if(!oldVar_txtbcthnterbit.equals(txtbcthnterbit.getValue())) changed = true;
        if(!oldVar_txtbcket.equals(txtbcket.getValue())) changed = true;

        return changed;
    }

    private void doShowDialog(Mkaryamhs _data) throws InterruptedException {
        try {
            windowKaryaDtl.doModal();
        } catch (final Exception e) {
            Messagebox.show(e.toString());
        }
    }

    public MahasiswaKegiatanCtrl getMahasiswaKegiatanCtrl() {
        return mahasiswaKegiatanCtrl;
    }

    public void setMahasiswaKegiatanCtrl(MahasiswaKegiatanCtrl mahasiswaKegiatanCtrl) {
        this.mahasiswaKegiatanCtrl = mahasiswaKegiatanCtrl;
    }

    public Mkaryamhs getMkaryamhs() {
        return mkaryamhs;
    }

    public void setMkaryamhs(Mkaryamhs mkaryamhs) {
        this.mkaryamhs = mkaryamhs;
    }
}
