package id.ac.idu.webui.irs.irs;

import id.ac.idu.backend.model.Mdetilkurikulum;
import id.ac.idu.backend.model.Tirspasca;
import id.ac.idu.webui.kurikulum.kurikulum.KurikulumDetailCtrl;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.MultiLineMessageBox;
import id.ac.idu.webui.util.searchdialogs.DetilKurikulumExtendedSearchListBox;
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
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 4/21/12
 * Time: 10:54 PM
 */
public class DetilMatakuliahCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(DetilMatakuliahCtrl.class);

    protected Window windowDetilMatakuliah;
    protected Listbox listDetilMatakuliah;
    protected IrsDetailCtrl irsDetailCtrl;
    protected boolean isnew;

    private AnnotateDataBinder binder;

    protected Tirspasca tirspasca;

    protected Button btnSave;
    protected Button btnDelete;
    protected Button btnCancel;
    protected Button btnClose;

    protected Textbox txtbKode;
    protected Textbox txtbNama;
    protected Textbox txtbKelompok;
    protected Textbox txtbSks;

    protected Button btnSearchMatakuliahExtended;

    private String oldVar_txtbKode;
    private String oldVar_txtbNama;
    private String oldVar_txtbKelompok;
    private String oldVar_txtbSks;
    private String oldVar_txtbMun;

    public DetilMatakuliahCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        logger.info("Page Detil Matakuliah");

        super.doAfterCompose(window);
        self.setAttribute("controller", this, false);

        if (arg.containsKey("ModuleMainController")) {
            setIrsDetailCtrl((IrsDetailCtrl) arg.get("ModuleMainController"));
            getIrsDetailCtrl().setDetilMatakuliahCtrl(this);
        }
    }

    @SuppressWarnings("unchecked")
    public void onCreate$windowDetilMatakuliah(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        Map<String, Object> args = getCreationArgsMap(event);

        if (args.containsKey(IrsDetailCtrl.DATA)) {
            Tirspasca obj = (Tirspasca) args.get(IrsDetailCtrl.DATA);
            setTirspasca(obj);
        }

        if (args.containsKey(KurikulumDetailCtrl.LIST))
            listDetilMatakuliah = (Listbox) args.get(IrsDetailCtrl.LIST);
        else
            listDetilMatakuliah = null;

        if (args.containsKey(IrsDetailCtrl.CONTROL)) irsDetailCtrl = (IrsDetailCtrl) args.get(IrsDetailCtrl.CONTROL);
        else irsDetailCtrl = null;

        isnew = args.containsKey(IrsDetailCtrl.ISNEW) && (Boolean) args.get(IrsDetailCtrl.ISNEW);

        this.doInitButton(getIrsDetailCtrl().isModeEdit());
        this.doReadOnlyMode(!getIrsDetailCtrl().isModeEdit());
        binder.loadAll();
        this.doShowDialog(getTirspasca());
        this.doStoreInitValues();
    }

    public void onClick$btnClose(Event event) throws InterruptedException {
        try {
            doClose();
        } catch (final Exception e) {
            windowDetilMatakuliah.onClose();
        }
    }

    public void onClick$btnSave(Event event) throws InterruptedException {
        doSave();
    }

    public void onClick$btnDelete() {
        Listitem item = listDetilMatakuliah.getSelectedItem();
        getIrsDetailCtrl().getDelDetilMatakuliah().add((Tirspasca) item.getAttribute(IrsDetailCtrl.DATA));

        listDetilMatakuliah.removeItemAt(listDetilMatakuliah.getSelectedIndex());
        windowDetilMatakuliah.onClose();
    }

    public void onClick$btnCancel(Event event) {
        doResetInitValues();
    }

    public void doSave() throws InterruptedException {
        binder.saveAll();

        List _lst = new ArrayList();
        _lst.addAll(listDetilMatakuliah.getItems());
        int idx = listDetilMatakuliah.getSelectedIndex();


        ListModelList lml = (ListModelList) listDetilMatakuliah.getListModel();
        if(lml == null) lml = new ListModelList();
        else {
            lml.clear();
            for(Object obj : _lst) {
                Listitem item = (Listitem) obj;
                lml.add((Tirspasca) item.getAttribute(IrsDetailCtrl.DATA));
            }
        }
        Tirspasca obj = getTirspasca();
        obj.setMmahasiswa(getIrsDetailCtrl().getIrs().getMmahasiswa());
        obj.setMsekolah(getIrsDetailCtrl().getIrs().getMsekolah());
        obj.setMprodi(getIrsDetailCtrl().getIrs().getMprodi());
        obj.setCterm(getIrsDetailCtrl().getIrs().getCterm());
        obj.setCthajar(getIrsDetailCtrl().getIrs().getCthajar());
        obj.setCsmt(getIrsDetailCtrl().getIrs().getCsmt());
        setTirspasca(obj);
        if(!this.isnew) lml.set(idx, getTirspasca());
        else lml.add(listDetilMatakuliah.getItems().size(), getTirspasca());
        getIrsDetailCtrl().calcTotalSks();
        getIrsDetailCtrl().txtb_totalsks.setValue(getIrsDetailCtrl().getTotalsks());
        doStoreInitValues();
        windowDetilMatakuliah.onClose();
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

        windowDetilMatakuliah.onClose();
    }

    private void doInitButton(boolean b) {
        btnSave.setVisible(b);
        btnDelete.setVisible(b);
        btnCancel.setVisible(b);

        if(isnew) btnDelete.setVisible(false);
    }

    private void doReadOnlyMode(boolean b) {
        txtbKelompok.setReadonly(b);
        btnSearchMatakuliahExtended.setDisabled(b);
    }

    public void onClick$btnSearchMatakuliahExtended(Event event) {
        doSearchMatakuliahExtended(event);
    }

    private void doSearchMatakuliahExtended(Event event) {
        Mdetilkurikulum dtl = DetilKurikulumExtendedSearchListBox.show(windowDetilMatakuliah);
        if (dtl != null) {
            txtbNama.setValue(dtl.getMtbmtkl().getCnamamk());
            txtbKode.setValue(dtl.getMtbmtkl().getCkdmtk());
            //txtbMun.setValue(dtl.getCmun());
            int sks = 0;
            if(dtl.getMtbmtkl().getNsks()!=null) {
                sks = dtl.getMtbmtkl().getNsks();
            }
            txtbSks.setValue(String.valueOf(sks));
            Tirspasca obj = getTirspasca();
            obj.setMtbmtkl(dtl.getMtbmtkl());
            obj.setNsks(sks);
            setTirspasca(obj);
        }
    }

    private void doResetInitValues() {
        txtbKode.setValue(oldVar_txtbKode);
        txtbNama.setValue(oldVar_txtbNama);
        txtbKelompok.setValue(oldVar_txtbKelompok);
        txtbSks.setValue(oldVar_txtbSks);
        //txtbMun.setValue(oldVar_txtbMun);
        getIrsDetailCtrl().calcTotalSks();
    }

    private void doStoreInitValues() {
        if(isnew) return;

        oldVar_txtbKode = txtbKode.getValue();
        oldVar_txtbNama = txtbNama.getValue();
        oldVar_txtbKelompok = txtbKelompok.getValue();
        oldVar_txtbSks = txtbSks.getValue();
        //oldVar_txtbMun = txtbMun.getValue();
        getIrsDetailCtrl().calcTotalSks();
    }

    private boolean isDataChanged() {
        boolean changed = false;

        if (!oldVar_txtbKode.equals(txtbKode.getValue())) changed = true;

        if (!oldVar_txtbNama.equals(txtbNama.getValue())) changed = true;

        if (!oldVar_txtbKelompok.equals(txtbKelompok.getValue())) changed = true;

        if (!oldVar_txtbSks.equals(txtbSks.getValue())) changed = true;

        //if (!oldVar_txtbMun.equals(txtbMun.getValue())) changed = true;

        return changed;
    }

    private void doShowDialog(Tirspasca tirspasca) throws InterruptedException {
        try {
            windowDetilMatakuliah.doModal();
        } catch (final Exception e) {
            Messagebox.show(e.toString());
        }
    }

    public IrsDetailCtrl getIrsDetailCtrl() {
        return irsDetailCtrl;
    }

    public void setIrsDetailCtrl(IrsDetailCtrl irsDetailCtrl) {
        this.irsDetailCtrl = irsDetailCtrl;
    }

    public Tirspasca getTirspasca() {
        return tirspasca;
    }

    public void setTirspasca(Tirspasca tirspasca) {
        this.tirspasca = tirspasca;
    }
}
