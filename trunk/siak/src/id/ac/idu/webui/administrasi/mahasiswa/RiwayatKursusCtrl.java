package id.ac.idu.webui.administrasi.mahasiswa;

import id.ac.idu.backend.model.Mhistkursusmhs;
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
 * @Date 29 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class RiwayatKursusCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(RiwayatKursusCtrl.class);

    protected Window windowRiwayatKursus;
    protected Listbox listRiwayatKursus;
    protected MahasiswaKursusCtrl mahasiswaKursusCtrl;
    protected boolean isnew;

    private AnnotateDataBinder binder;

    protected Mhistkursusmhs mhistkursusmhs;

    protected Button btnSave;
    protected Button btnDelete;
    protected Button btnCancel;
    protected Button btnClose;

    protected Textbox txtbNo;
    protected Textbox txtbCnmkursus;
    protected Textbox txtbClama;
    protected Textbox txtbCthnselesai;
    protected Textbox txtbCtempat;
    protected Textbox txtbCket;

    private String oldVar_txtbNo;
    private String oldVar_txtbCnmkursus;
    private String oldVar_txtbClama;
    private String oldVar_txtbCthnselesai;
    private String oldVar_txtbCtempat;
    private String oldVar_txtbCket;

    public RiwayatKursusCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        logger.info("Page Detail Riwayat Pendidikan");

        super.doAfterCompose(window);
        self.setAttribute("controller", this, false);

        if (arg.containsKey("ModuleMainController")) {
            setMahasiswaKursusCtrl((MahasiswaKursusCtrl) arg.get("ModuleMainController"));
            getMahasiswaKursusCtrl().setRiwayatKursusCtrl(this);
        }
    }

    @SuppressWarnings("unchecked")
    public void onCreate$windowRiwayatKursus(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        Map<String, Object> args = getCreationArgsMap(event);

        if (args.containsKey(MahasiswaKursusCtrl.DATA)) {
            Mhistkursusmhs anKursus = (Mhistkursusmhs) args.get(MahasiswaKursusCtrl.DATA);
            setMhistkursusmhs(anKursus);
        }

        if (args.containsKey(MahasiswaKursusCtrl.LIST))
            listRiwayatKursus = (Listbox) args.get(MahasiswaKursusCtrl.LIST);
        else
            listRiwayatKursus = null;

        if (args.containsKey(MahasiswaKursusCtrl.CONTROL)) mahasiswaKursusCtrl = (MahasiswaKursusCtrl) args.get(MahasiswaKursusCtrl.CONTROL);
        else mahasiswaKursusCtrl = null;

        isnew = args.containsKey(MahasiswaKursusCtrl.ISNEW) && (Boolean) args.get(MahasiswaKursusCtrl.ISNEW);

        this.doInitButton(getMahasiswaKursusCtrl().isModeEdit());
        this.doReadOnlyMode(!getMahasiswaKursusCtrl().isModeEdit());

        binder.loadAll();
        this.doShowDialog(getMhistkursusmhs());
        this.doStoreInitValues();
    }

    public void onClick$btnClose(Event event) throws InterruptedException {
        try {
            doClose();
        } catch (final Exception e) {
            windowRiwayatKursus.onClose();
        }
    }

    public void onClick$btnSave(Event event) throws InterruptedException {
        doSave();
    }

    public void onClick$btnDelete() {
        Listitem item = listRiwayatKursus.getSelectedItem();
        getMahasiswaKursusCtrl().getDelKursus().add((Mhistkursusmhs) item.getAttribute(MahasiswaKursusCtrl.DATA));

        listRiwayatKursus.removeItemAt(listRiwayatKursus.getSelectedIndex());
        windowRiwayatKursus.onClose();
    }

    public void onClick$btnCancel(Event event) {
        doResetInitValues();
    }

    public void doSave() throws InterruptedException {
        binder.saveAll();

        List _lst = new ArrayList();
        _lst.addAll(listRiwayatKursus.getItems());
        int idx = listRiwayatKursus.getSelectedIndex();


        ListModelList lml = (ListModelList) listRiwayatKursus.getListModel();
        if(lml == null) lml = new ListModelList();
        else {
            lml.clear();
            for(Object onPendidikan : _lst) {
                Listitem item = (Listitem) onPendidikan;
                lml.add((Mhistkursusmhs) item.getAttribute(MahasiswaKursusCtrl.DATA));
            }
        }

        if(!this.isnew) lml.set(idx, getMhistkursusmhs());
        else lml.add(listRiwayatKursus.getItems().size(), getMhistkursusmhs());

        doStoreInitValues();
        windowRiwayatKursus.onClose();
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

        windowRiwayatKursus.onClose();
    }

    private void doInitButton(boolean b) {
        btnSave.setVisible(b);
        btnDelete.setVisible(b);
        btnCancel.setVisible(b);
    }

    private void doReadOnlyMode(boolean b) {
        txtbNo.setReadonly(b);
        txtbCnmkursus.setReadonly(b);
        txtbClama.setReadonly(b);
        txtbCthnselesai.setReadonly(b);
        txtbCtempat.setReadonly(b);
        txtbCket.setReadonly(b);
    }

    private void doResetInitValues() {
        txtbNo.setValue(oldVar_txtbNo);
        txtbCnmkursus.setValue(oldVar_txtbCnmkursus);
        txtbClama.setValue(oldVar_txtbClama);
        txtbCthnselesai.setValue(oldVar_txtbCthnselesai);
        txtbCtempat.setValue(oldVar_txtbCtempat);
        txtbCket.setValue(oldVar_txtbCket);
    }

    private void doStoreInitValues() {
        if(isnew) return;

        oldVar_txtbNo = txtbNo.getValue();
        oldVar_txtbCnmkursus = txtbCnmkursus.getValue();
        oldVar_txtbClama = txtbClama.getValue();
        oldVar_txtbCthnselesai = txtbCthnselesai.getValue();
        oldVar_txtbCtempat = txtbCtempat.getValue();
        oldVar_txtbCket = txtbCket.getValue();
    }

    private boolean isDataChanged() {
        boolean changed = false;

        if (!oldVar_txtbNo.equals(txtbNo.getValue())) changed = true;

        if (!oldVar_txtbCnmkursus.equals(txtbCnmkursus.getValue())) changed = true;

        if (!oldVar_txtbClama.equals(txtbClama.getValue())) changed = true;

        if (!oldVar_txtbCthnselesai.equals(txtbCthnselesai.getValue())) changed = true;

        if (!oldVar_txtbCtempat.equals(txtbCtempat.getValue())) changed = true;
        
        if (!oldVar_txtbCket.equals(txtbCket.getValue())) changed = true;

        return changed;
    }

    private void doShowDialog(Mhistkursusmhs mhistkursusmhs) throws InterruptedException {
        try {
            windowRiwayatKursus.doModal();
        } catch (final Exception e) {
            Messagebox.show(e.toString());
        }
    }

    public MahasiswaKursusCtrl getMahasiswaKursusCtrl() {
        return mahasiswaKursusCtrl;
    }

    public void setMahasiswaKursusCtrl(MahasiswaKursusCtrl mahasiswaKursusCtrl) {
        this.mahasiswaKursusCtrl = mahasiswaKursusCtrl;
    }

    public Mhistkursusmhs getMhistkursusmhs() {
        return mhistkursusmhs;
    }

    public void setMhistkursusmhs(Mhistkursusmhs mhistkursusmhs) {
        this.mhistkursusmhs = mhistkursusmhs;
    }
}