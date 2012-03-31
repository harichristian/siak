package id.ac.idu.webui.administrasi.mahasiswa;

import id.ac.idu.backend.model.Mpbahasamhs;
import id.ac.idu.util.Codec;
import id.ac.idu.webui.util.EnumConverter;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.GFCListModelCtrl;
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

public class RiwayatBahasaCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(RiwayatBahasaCtrl.class);

    protected Window windowRiwayatBahasa;
    protected Listbox listRiwayatBahasa;
    protected MahasiswaKursusCtrl mahasiswaKursusCtrl;
    protected boolean isnew;
    
    private AnnotateDataBinder binder;

    protected Mpbahasamhs mpbahasamhs;

    protected Button btnSave;
    protected Button btnDelete;
    protected Button btnCancel;
    protected Button btnClose;

    protected Textbox txtbNo;
    protected Textbox txtbCnmbahasa;
    protected Listbox txtbCstatus;
    protected Bandbox cmbCstatus;
    protected Textbox txtbCket;

    private String oldVar_txtbNo;
    private String oldVar_txtbCnmbahasa;
    private String oldVar_txtbCstatus;
    private String oldVar_cmbCstatus;
    private String oldVar_txtbCket;

    public RiwayatBahasaCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        logger.info("Page Detail Riwayat Pendidikan");

        super.doAfterCompose(window);
        self.setAttribute("controller", this, false);

        if (arg.containsKey("ModuleMainController")) {
            setMahasiswaKursusCtrl((MahasiswaKursusCtrl) arg.get("ModuleMainController"));
            getMahasiswaKursusCtrl().setRiwayatBahasaCtrl(this);
        }
    }

    @SuppressWarnings("unchecked")
    public void onCreate$windowRiwayatBahasa(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        Map<String, Object> args = getCreationArgsMap(event);

        if (args.containsKey(MahasiswaKursusCtrl.DATA)) {
            Mpbahasamhs anBahasa = (Mpbahasamhs) args.get(MahasiswaKursusCtrl.DATA);
            setMpbahasamhs(anBahasa);
        }

        if (args.containsKey(MahasiswaKursusCtrl.LIST))
            listRiwayatBahasa = (Listbox) args.get(MahasiswaKursusCtrl.LIST);
        else
            listRiwayatBahasa = null;

        if (args.containsKey(MahasiswaKursusCtrl.CONTROL)) mahasiswaKursusCtrl = (MahasiswaKursusCtrl) args.get(MahasiswaKursusCtrl.CONTROL);
        else mahasiswaKursusCtrl = null;

        isnew = args.containsKey(MahasiswaKursusCtrl.ISNEW) && (Boolean) args.get(MahasiswaKursusCtrl.ISNEW);

        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.StatusBahasa.class)).getEnumToList()
                ,txtbCstatus, cmbCstatus, (getMpbahasamhs() != null)?getMpbahasamhs().getCstatus():null);

        this.doInitButton(getMahasiswaKursusCtrl().isModeEdit());
        this.doReadOnlyMode(!getMahasiswaKursusCtrl().isModeEdit());

        binder.loadAll();
        this.doShowDialog(getMpbahasamhs());
        this.doStoreInitValues();
    }

    public void onSelect$txtbCstatus(Event event) {
        cmbCstatus.setValue(txtbCstatus.getSelectedItem().getLabel());
        getMpbahasamhs().setCstatus((String) txtbCstatus.getSelectedItem().getValue());
        cmbCstatus.close();
    }

    public void onClick$btnClose(Event event) throws InterruptedException {
        try {
            doClose();
        } catch (final Exception e) {
            windowRiwayatBahasa.onClose();
        }
    }

    public void onClick$btnSave(Event event) throws InterruptedException {
        doSave();
    }

    public void onClick$btnDelete() {
        Listitem item = listRiwayatBahasa.getSelectedItem();
        getMahasiswaKursusCtrl().getDelBahasa().add((Mpbahasamhs) item.getAttribute(MahasiswaKursusCtrl.DATA));

        listRiwayatBahasa.removeItemAt(listRiwayatBahasa.getSelectedIndex());
        windowRiwayatBahasa.onClose();
    }

    public void onClick$btnCancel(Event event) {
        doResetInitValues();
    }

    public void doSave() throws InterruptedException {
        binder.saveAll();

        List _lst = new ArrayList();
        _lst.addAll(listRiwayatBahasa.getItems());
        int idx = listRiwayatBahasa.getSelectedIndex();


        ListModelList lml = (ListModelList) listRiwayatBahasa.getListModel();
        if(lml == null) lml = new ListModelList();
        else {
            lml.clear();
            for(Object onPendidikan : _lst) {
                Listitem item = (Listitem) onPendidikan;
                lml.add((Mpbahasamhs) item.getAttribute(MahasiswaKursusCtrl.DATA));
            }
        }

        if(!this.isnew) lml.set(idx, getMpbahasamhs());
        else lml.add(listRiwayatBahasa.getItems().size(), getMpbahasamhs());

        doStoreInitValues();
        windowRiwayatBahasa.onClose();
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

        windowRiwayatBahasa.onClose();
    }

    private void doInitButton(boolean b) {
        btnSave.setVisible(b);
        btnDelete.setVisible(b);
        btnCancel.setVisible(b);

        if(isnew) btnDelete.setVisible(false);
    }

    private void doReadOnlyMode(boolean b) {
        txtbNo.setReadonly(b);
        txtbCnmbahasa.setReadonly(b);
        cmbCstatus.setDisabled(b);
        txtbCket.setReadonly(b);
    }

    private void doResetInitValues() {
        txtbNo.setValue(oldVar_txtbNo);
        txtbCnmbahasa.setValue(oldVar_txtbCnmbahasa);
        txtbCket.setValue(oldVar_txtbCket);
    }

    private void doStoreInitValues() {
        if(isnew) return;

        oldVar_txtbNo = txtbNo.getValue();
        oldVar_txtbCnmbahasa = txtbCnmbahasa.getValue();
        oldVar_txtbCket = txtbCket.getValue();
    }

    private boolean isDataChanged() {
        boolean changed = false;

        if (!oldVar_txtbNo.equals(txtbNo.getValue())) changed = true;

        if (!oldVar_txtbCnmbahasa.equals(txtbCnmbahasa.getValue())) changed = true;

        if (!oldVar_cmbCstatus.equals(cmbCstatus.getValue())) changed = true;

        if (!oldVar_txtbCket.equals(txtbCket.getValue())) changed = true;

        return changed;
    }

    private void doShowDialog(Mpbahasamhs mpbahasamhs) throws InterruptedException {
        try {
            windowRiwayatBahasa.doModal();
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

    public Mpbahasamhs getMpbahasamhs() {
        return mpbahasamhs;
    }

    public void setMpbahasamhs(Mpbahasamhs mpbahasamhs) {
        this.mpbahasamhs = mpbahasamhs;
    }
}
