package id.ac.idu.webui.administrasi.mahasiswa;

import id.ac.idu.backend.model.Mprestasimhs;
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
 * @Date 02 Apr 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class PrestasiCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(PrestasiCtrl.class);

    protected Window windowDetailPrestasi;
    protected MahasiswaPekerjaanCtrl mahasiswaPekerjaanCtrl;

    protected boolean isnew;

    private AnnotateDataBinder binder;

    protected Mprestasimhs mprestasimhs;
    protected Listbox listPrestasi;

    protected Textbox txtbcno;
    protected Textbox txtbcnmprestasi;
    protected Textbox txtbcnmberi;
    protected Textbox txtbcthnterima;
    protected Textbox txtbcket;

    private String oldVar_txtbcno;
    private String oldVar_txtbcnmprestasi;
    private String oldVar_txtbcnmberi;
    private String oldVar_txtbcthnterima;
    private String oldVar_txtbcket;

    protected Button btnSave;
    protected Button btnDelete;
    protected Button btnCancel;
    protected Button btnClose;

    public PrestasiCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        logger.info("Page Detail Riwayat Pendidikan");

        super.doAfterCompose(window);
        self.setAttribute("controller", this, false);

        if (arg.containsKey("ModuleMainController")) {
            setMahasiswaPekerjaanCtrl((MahasiswaPekerjaanCtrl) arg.get("ModuleMainController"));
            getMahasiswaPekerjaanCtrl().setPrestasiCtrl(this);
        }
    }

    @SuppressWarnings("unchecked")
    public void onCreate$windowDetailPrestasi(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        Map<String, Object> args = getCreationArgsMap(event);

        if (args.containsKey(MahasiswaKegiatanCtrl.DATA)) {
            Mprestasimhs anPrestasi = (Mprestasimhs) args.get(MahasiswaKegiatanCtrl.DATA);
            setMprestasimhs(anPrestasi);
        }

        if (args.containsKey(MahasiswaKegiatanCtrl.LIST))
            listPrestasi = (Listbox) args.get(MahasiswaKegiatanCtrl.LIST);
        else
            listPrestasi = null;

        if (args.containsKey(MahasiswaPekerjaanCtrl.CONTROL)) mahasiswaPekerjaanCtrl = (MahasiswaPekerjaanCtrl) args.get(MahasiswaKegiatanCtrl.CONTROL);
        else mahasiswaPekerjaanCtrl = null;

        isnew = args.containsKey(MahasiswaKegiatanCtrl.ISNEW) && (Boolean) args.get(MahasiswaKegiatanCtrl.ISNEW);

        this.doInitButton(getMahasiswaPekerjaanCtrl().isModeEdit());
        this.doReadOnlyMode(!getMahasiswaPekerjaanCtrl().isModeEdit());

        binder.loadAll();
        this.doShowDialog(getMprestasimhs());
        this.doStoreInitValues();
    }

    public void onClick$btnClose(Event event) throws InterruptedException {
        try {
            doClose();
        } catch (final Exception e) {
            windowDetailPrestasi.onClose();
        }
    }

    public void onClick$btnSave(Event event) throws InterruptedException {
        doSave();
    }

    public void onClick$btnDelete() {
        Listitem item = listPrestasi.getSelectedItem();
        getMahasiswaPekerjaanCtrl().getDelPrestasi().add((Mprestasimhs) item.getAttribute(MahasiswaPekerjaanCtrl.DATA));

        listPrestasi.removeItemAt(listPrestasi.getSelectedIndex());
        windowDetailPrestasi.onClose();
    }

    public void onClick$btnCancel(Event event) {
        doResetInitValues();
    }

    public void doSave() throws InterruptedException {
        binder.saveAll();

        List _lst = new ArrayList();
        _lst.addAll(listPrestasi.getItems());
        int idx = listPrestasi.getSelectedIndex();


        ListModelList lml = (ListModelList) listPrestasi.getListModel();
        if(lml == null) lml = new ListModelList();
        else {
            lml.clear();
            for(Object onPendidikan : _lst) {
                Listitem item = (Listitem) onPendidikan;
                lml.add((Mprestasimhs) item.getAttribute(MahasiswaPekerjaanCtrl.DATA));
            }
        }

        if(!this.isnew) lml.set(idx, getMprestasimhs());
        else lml.add(listPrestasi.getItems().size(), getMprestasimhs());

        doStoreInitValues();
        windowDetailPrestasi.onClose();
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

        windowDetailPrestasi.onClose();
    }

    private void doInitButton(boolean b) {
        btnSave.setVisible(b);
        btnDelete.setVisible(b);
        btnCancel.setVisible(b);

        if(isnew) btnDelete.setVisible(false);
    }

    private void doReadOnlyMode(boolean b) {
        txtbcno.setReadonly(b);
        txtbcnmprestasi.setReadonly(b);
        txtbcnmberi.setReadonly(b);
        txtbcthnterima.setReadonly(b);
        txtbcket.setReadonly(b);
    }

    private void doShowDialog(Mprestasimhs mprestasimhs) throws InterruptedException {
        try {
            windowDetailPrestasi.doModal();
        } catch (final Exception e) {
            Messagebox.show(e.toString());
        }
    }

    private void doStoreInitValues() {
        if(isnew) return;

        oldVar_txtbcno=txtbcno.getValue();
        oldVar_txtbcnmprestasi=txtbcnmprestasi.getValue();
        oldVar_txtbcnmberi=txtbcnmberi.getValue();
        oldVar_txtbcthnterima=txtbcthnterima.getValue();
        oldVar_txtbcket=txtbcket.getValue();
    }

    private void doResetInitValues() {
        txtbcno.setValue(oldVar_txtbcno);
        txtbcnmprestasi.setValue(oldVar_txtbcnmprestasi);
        txtbcnmberi.setValue(oldVar_txtbcnmberi);
        txtbcthnterima.setValue(oldVar_txtbcthnterima);
        txtbcket.setValue(oldVar_txtbcket);
    }

    private boolean isDataChanged() {
        boolean changed = false;

        if (!oldVar_txtbcno.equals(txtbcno.getValue())) changed = true;
        if (!oldVar_txtbcnmprestasi.equals(txtbcnmprestasi.getValue())) changed = true;
        if (!oldVar_txtbcnmberi.equals(txtbcnmberi.getValue())) changed = true;
        if (!oldVar_txtbcthnterima.equals(txtbcthnterima.getValue())) changed = true;
        if (!oldVar_txtbcket.equals(txtbcket.getValue())) changed = true;

        return changed;
    }

    public MahasiswaPekerjaanCtrl getMahasiswaPekerjaanCtrl() {
        return mahasiswaPekerjaanCtrl;
    }

    public void setMahasiswaPekerjaanCtrl(MahasiswaPekerjaanCtrl mahasiswaPekerjaanCtrl) {
        this.mahasiswaPekerjaanCtrl = mahasiswaPekerjaanCtrl;
    }

    public AnnotateDataBinder getBinder() {
        return binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public Mprestasimhs getMprestasimhs() {
        return mprestasimhs;
    }

    public void setMprestasimhs(Mprestasimhs mprestasimhs) {
        this.mprestasimhs = mprestasimhs;
    }
}
