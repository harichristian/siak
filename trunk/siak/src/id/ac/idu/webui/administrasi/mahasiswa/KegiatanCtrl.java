package id.ac.idu.webui.administrasi.mahasiswa;

import id.ac.idu.backend.model.Mkgtmhs;
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

public class KegiatanCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(RiwayatKursusCtrl.class);

    protected Window windowKegiatanDtl;
    protected Listbox listKegiatan;
    protected MahasiswaKegiatanCtrl mahasiswaKegiatanCtrl;

    protected boolean isnew;

    private AnnotateDataBinder binder;

    protected Mkgtmhs mkgtmhs;

    protected Button btnSave;
    protected Button btnDelete;
    protected Button btnCancel;
    protected Button btnClose;

    protected Textbox txtbcno;
    protected Textbox txtbcnmsimposium;
    protected Textbox txtbcperanan;
    protected Textbox txtbctahun;
    protected Textbox txtbcinstitusi;

    private String oldVar_txtbcno;
    private String oldVar_txtbcnmsimposium;
    private String oldVar_txtbcperanan;
    private String oldVar_txtbctahun;
    private String oldVar_txtbcinstitusi;

    public KegiatanCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        logger.info("Page Detail Riwayat Pendidikan");

        super.doAfterCompose(window);
        self.setAttribute("controller", this, false);

        if (arg.containsKey("ModuleMainController")) {
            setMahasiswaKegiatanCtrl((MahasiswaKegiatanCtrl) arg.get("ModuleMainController"));
            getMahasiswaKegiatanCtrl().setKegiatanCtrl(this);
        }
    }

    @SuppressWarnings("unchecked")
    public void onCreate$windowKegiatanDtl(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        Map<String, Object> args = getCreationArgsMap(event);

        if (args.containsKey(MahasiswaKegiatanCtrl.DATA)) {
            Mkgtmhs anKursus = (Mkgtmhs) args.get(MahasiswaKegiatanCtrl.DATA);
            setMkgtmhs(anKursus);
        }

        if (args.containsKey(MahasiswaKegiatanCtrl.LIST))
            listKegiatan = (Listbox) args.get(MahasiswaKegiatanCtrl.LIST);
        else
            listKegiatan = null;

        if (args.containsKey(MahasiswaKegiatanCtrl.CONTROL)) mahasiswaKegiatanCtrl = (MahasiswaKegiatanCtrl) args.get(MahasiswaKegiatanCtrl.CONTROL);
        else mahasiswaKegiatanCtrl = null;

        isnew = args.containsKey(MahasiswaKegiatanCtrl.ISNEW) && (Boolean) args.get(MahasiswaKegiatanCtrl.ISNEW);

        this.doInitButton(getMahasiswaKegiatanCtrl().isModeEdit());
        this.doReadOnlyMode(!getMahasiswaKegiatanCtrl().isModeEdit());

        binder.loadAll();
        this.doShowDialog(getMkgtmhs());
        this.doStoreInitValues();
    }

    public void onClick$btnClose(Event event) throws InterruptedException {
        try {
            doClose();
        } catch (final Exception e) {
            windowKegiatanDtl.onClose();
        }
    }

    public void onClick$btnSave(Event event) throws InterruptedException {
        doSave();
    }

    public void onClick$btnDelete() {
        Listitem item = listKegiatan.getSelectedItem();
        getMahasiswaKegiatanCtrl().getDelKegiatan().add((Mkgtmhs) item.getAttribute(MahasiswaKegiatanCtrl.DATA));

        listKegiatan.removeItemAt(listKegiatan.getSelectedIndex());
        windowKegiatanDtl.onClose();
    }

    public void onClick$btnCancel(Event event) {
        doResetInitValues();
    }

    public void doSave() throws InterruptedException {
        binder.saveAll();

        List _lst = new ArrayList();
        _lst.addAll(listKegiatan.getItems());
        int idx = listKegiatan.getSelectedIndex();


        ListModelList lml = (ListModelList) listKegiatan.getListModel();
        if(lml == null) lml = new ListModelList();
        else {
            lml.clear();
            for(Object onPendidikan : _lst) {
                Listitem item = (Listitem) onPendidikan;
                lml.add((Mkgtmhs) item.getAttribute(MahasiswaKegiatanCtrl.DATA));
            }
        }

        if(!this.isnew) lml.set(idx, getMkgtmhs());
        else lml.add(listKegiatan.getItems().size(), getMkgtmhs());

        doStoreInitValues();
        windowKegiatanDtl.onClose();
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

        windowKegiatanDtl.onClose();
    }

    private void doInitButton(boolean b) {
        btnSave.setVisible(b);
        btnDelete.setVisible(b);
        btnCancel.setVisible(b);

        if(isnew) btnDelete.setVisible(false);
    }

    private void doReadOnlyMode(boolean b) {
        txtbcno.setReadonly(b);
        txtbcnmsimposium.setReadonly(b);
        txtbcperanan.setReadonly(b);
        txtbctahun.setReadonly(b);
        txtbcinstitusi.setReadonly(b);
    }

    private void doResetInitValues() {
        txtbcno.setValue(oldVar_txtbcno);
        txtbcnmsimposium.setValue(oldVar_txtbcnmsimposium);
        txtbcperanan.setValue(oldVar_txtbcperanan);
        txtbctahun.setValue(oldVar_txtbctahun);
        txtbcinstitusi.setValue(oldVar_txtbcinstitusi);
    }

    private void doStoreInitValues() {
        if(isnew) return;

        oldVar_txtbcno=txtbcno.getValue();
        oldVar_txtbcnmsimposium=txtbcnmsimposium.getValue();
        oldVar_txtbcperanan=txtbcperanan.getValue();
        oldVar_txtbctahun=txtbctahun.getValue();
        oldVar_txtbcinstitusi=txtbcinstitusi.getValue();
    }

    private boolean isDataChanged() {
        boolean changed = false;

        if (!oldVar_txtbcno.equals(txtbcno.getValue())) changed = true;
        if (!oldVar_txtbcnmsimposium.equals(txtbcnmsimposium.getValue())) changed = true;
        if (!oldVar_txtbcperanan.equals(txtbcperanan.getValue())) changed = true;
        if (!oldVar_txtbctahun.equals(txtbctahun.getValue())) changed = true;
        if (!oldVar_txtbcinstitusi.equals(txtbcinstitusi.getValue())) changed = true;

        return changed;
    }

    private void doShowDialog(Mkgtmhs mhistkursusmhs) throws InterruptedException {
        try {
            windowKegiatanDtl.doModal();
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

    public Mkgtmhs getMkgtmhs() {
        return mkgtmhs;
    }

    public void setMkgtmhs(Mkgtmhs mkgtmhs) {
        this.mkgtmhs = mkgtmhs;
    }
}
