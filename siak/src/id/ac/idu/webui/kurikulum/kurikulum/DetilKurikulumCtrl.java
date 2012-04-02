package id.ac.idu.webui.kurikulum.kurikulum;

import id.ac.idu.backend.model.Mdetilkurikulum;
import id.ac.idu.backend.model.Mtbmtkl;
import id.ac.idu.util.Codec;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.GFCListModelCtrl;
import id.ac.idu.webui.util.MultiLineMessageBox;
import id.ac.idu.webui.util.searchdialogs.MatakuliahExtendedSearchListBox;
import id.ac.idu.webui.util.test.EnumConverter;
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
 * @Date 3/31/12
 * Time: 4:12 PM
 */
public class DetilKurikulumCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(DetilKurikulumCtrl.class);

    protected Window windowDetilKurikulum;
    protected Listbox listDetilKurikulum;
    protected KurikulumDetailCtrl kurikulumDetailCtrl;
    protected boolean isnew;

    private AnnotateDataBinder binder;

    protected Mdetilkurikulum mdetilkurikulum;

    protected Button btnSave;
    protected Button btnDelete;
    protected Button btnCancel;
    protected Button btnClose;

    protected Textbox txtbKode;
    protected Textbox txtbNama;
    protected Textbox txtbJenis;
    protected Listbox listJenis;
    protected Bandbox cmbJenis;
    protected Textbox txtbStatus;
    protected Listbox listStatus;
    protected Bandbox cmbStatus;
    protected Textbox txtbMun;
    protected Listbox listMun;
    protected Bandbox cmbMun;
    protected Textbox txtbTerm;
    protected Textbox txtbLintasProdi;
    protected Listbox listLintasProdi;
    protected Bandbox cmbLintasProdi;

    protected Button btnSearchMatakuliahExtended;

    private String oldVar_txtbKode;
    private String oldVar_txtbNama;
    private String oldVar_txtbJenis;
    private String oldVar_txtbStatus;
    private String oldVar_txtbMun;
    private String oldVar_txtbTerm;
    private String oldVar_txtbLintasProdi;

    public DetilKurikulumCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        logger.info("Page Detil Kurikulum");

        super.doAfterCompose(window);
        self.setAttribute("controller", this, false);

        if (arg.containsKey("ModuleMainController")) {
            setKurikulumDetailCtrl((KurikulumDetailCtrl) arg.get("ModuleMainController"));
            getKurikulumDetailCtrl().setDetilKurikulumCtrl(this);
        }
    }

    @SuppressWarnings("unchecked")
    public void onCreate$windowDetilKurikulum(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        Map<String, Object> args = getCreationArgsMap(event);

        if (args.containsKey(KurikulumDetailCtrl.DATA)) {
            Mdetilkurikulum obj = (Mdetilkurikulum) args.get(KurikulumDetailCtrl.DATA);
            setMdetilkurikulum(obj);
        }

        if (args.containsKey(KurikulumDetailCtrl.LIST))
            listDetilKurikulum = (Listbox) args.get(KurikulumDetailCtrl.LIST);
        else
            listDetilKurikulum = null;

        if (args.containsKey(KurikulumDetailCtrl.CONTROL)) kurikulumDetailCtrl = (KurikulumDetailCtrl) args.get(KurikulumDetailCtrl.CONTROL);
        else kurikulumDetailCtrl = null;

        isnew = args.containsKey(KurikulumDetailCtrl.ISNEW) && (Boolean) args.get(KurikulumDetailCtrl.ISNEW);

        this.doInitButton(getKurikulumDetailCtrl().isModeEdit());
        this.doReadOnlyMode(!getKurikulumDetailCtrl().isModeEdit());
        /*Jenis Combo*/
        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.JenisMatakuliah.class)).getEnumToList(),
                listJenis, cmbJenis, (getMdetilkurikulum() != null)?getMdetilkurikulum().getCjenis():null);
        /*Status Combo*/
        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.StatusAktif.class)).getEnumToList(),
                listStatus, cmbStatus, (getMdetilkurikulum() != null)?getMdetilkurikulum().getCstatus():null);
        /*Mun Combo*/
        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.Mun.class)).getEnumToList(),
                listMun, cmbMun, (getMdetilkurikulum() != null)?getMdetilkurikulum().getCmun():null);
        /*Lintas Prodi Combo*/
        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.LintasProdi.class)).getEnumToList(),
                listLintasProdi, cmbLintasProdi, (getMdetilkurikulum() != null)?getMdetilkurikulum().getClintasprodi():null);

        binder.loadAll();
        this.doShowDialog(getMdetilkurikulum());
        this.doStoreInitValues();
    }

    public void onClick$btnClose(Event event) throws InterruptedException {
        try {
            doClose();
        } catch (final Exception e) {
            windowDetilKurikulum.onClose();
        }
    }

    public void onClick$btnSave(Event event) throws InterruptedException {
        doSave();
    }

    public void onClick$btnDelete() {
        Listitem item = listDetilKurikulum.getSelectedItem();
        getKurikulumDetailCtrl().getDelDetilKurikulum().add((Mdetilkurikulum) item.getAttribute(KurikulumDetailCtrl.DATA));

        listDetilKurikulum.removeItemAt(listDetilKurikulum.getSelectedIndex());
        windowDetilKurikulum.onClose();
    }

    public void onClick$btnCancel(Event event) {
        doResetInitValues();
    }

    public void doSave() throws InterruptedException {
        binder.saveAll();

        List _lst = new ArrayList();
        _lst.addAll(listDetilKurikulum.getItems());
        int idx = listDetilKurikulum.getSelectedIndex();


        ListModelList lml = (ListModelList) listDetilKurikulum.getListModel();
        if(lml == null) lml = new ListModelList();
        else {
            lml.clear();
            for(Object obj : _lst) {
                Listitem item = (Listitem) obj;
                lml.add((Mdetilkurikulum) item.getAttribute(KurikulumDetailCtrl.DATA));
            }
        }
        Mdetilkurikulum obj = getMdetilkurikulum();
        if (txtbJenis.getValue() == listJenis.getSelectedItem().getValue().toString()) {
            obj.setCjenis(txtbJenis.getValue());
        } else {
            txtbJenis.setValue(listJenis.getSelectedItem().getValue().toString());
            obj.setCjenis(listJenis.getSelectedItem().getValue().toString());
        }
        if (txtbStatus.getValue() == listStatus.getSelectedItem().getValue().toString()) {
            obj.setCstatus(txtbStatus.getValue());
        } else {
            txtbStatus.setValue(listStatus.getSelectedItem().getValue().toString());
            obj.setCstatus(listStatus.getSelectedItem().getValue().toString());
        }
        if (txtbMun.getValue() == listMun.getSelectedItem().getValue().toString()) {
            obj.setCmun(txtbMun.getValue());
        } else {
            txtbMun.setValue(listMun.getSelectedItem().getValue().toString());
            obj.setCmun(listMun.getSelectedItem().getValue().toString());
        }
        if (txtbLintasProdi.getValue() == listLintasProdi.getSelectedItem().getValue().toString()) {
            obj.setClintasprodi(txtbLintasProdi.getValue());
        } else {
            txtbLintasProdi.setValue(listLintasProdi.getSelectedItem().getValue().toString());
            obj.setClintasprodi(listLintasProdi.getSelectedItem().getValue().toString());
        }
        obj.setMprodi(getKurikulumDetailCtrl().getKurikulum().getMprodi());
        setMdetilkurikulum(obj);
        if(!this.isnew) lml.set(idx, getMdetilkurikulum());
        else lml.add(listDetilKurikulum.getItems().size(), getMdetilkurikulum());

        doStoreInitValues();
        windowDetilKurikulum.onClose();
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

        windowDetilKurikulum.onClose();
    }

    private void doInitButton(boolean b) {
        btnSave.setVisible(b);
        btnDelete.setVisible(b);
        btnCancel.setVisible(b);

        if(isnew) btnDelete.setVisible(false);
    }

    private void doReadOnlyMode(boolean b) {
        //txtbKode.setReadonly(b);
        //txtbNama.setReadonly(b);
        txtbJenis.setReadonly(b);
        listJenis.setDisabled(b);
        cmbJenis.setDisabled(b);
        txtbStatus.setReadonly(b);
        listStatus.setDisabled(b);
        cmbStatus.setDisabled(b);
        txtbMun.setReadonly(b);
        listMun.setDisabled(b);
        cmbMun.setDisabled(b);
        txtbTerm.setReadonly(b);
        txtbLintasProdi.setReadonly(b);
        listLintasProdi.setDisabled(b);
        cmbLintasProdi.setDisabled(b);
        btnSearchMatakuliahExtended.setDisabled(b);
    }

    public void onClick$btnSearchMatakuliahExtended(Event event) {
        doSearchMatakuliahExtended(event);
    }

    private void doSearchMatakuliahExtended(Event event) {
        Mtbmtkl matkul = MatakuliahExtendedSearchListBox.show(windowDetilKurikulum);

        if (matkul != null) {
            txtbNama.setValue(matkul.getCnamamk());
            txtbKode.setValue(matkul.getCkdmtk());
            Mdetilkurikulum obj = getMdetilkurikulum();
            obj.setMtbmtkl(matkul);
            setMdetilkurikulum(obj);
        }
    }

    private void doResetInitValues() {
        txtbKode.setValue(oldVar_txtbKode);
        txtbNama.setValue(oldVar_txtbNama);
        txtbJenis.setValue(oldVar_txtbJenis);
        txtbStatus.setValue(oldVar_txtbStatus);
        txtbMun.setValue(oldVar_txtbMun);
        txtbTerm.setValue(oldVar_txtbTerm);
        txtbLintasProdi.setValue(oldVar_txtbLintasProdi);
    }

    private void doStoreInitValues() {
        if(isnew) return;

        oldVar_txtbKode = txtbKode.getValue();
        oldVar_txtbNama = txtbNama.getValue();
        oldVar_txtbJenis = txtbJenis.getValue();
        oldVar_txtbStatus = txtbStatus.getValue();
        oldVar_txtbMun = txtbMun.getValue();
        oldVar_txtbTerm = txtbTerm.getValue();
        oldVar_txtbLintasProdi = txtbLintasProdi.getValue();
    }

    private boolean isDataChanged() {
        boolean changed = false;

        if (!oldVar_txtbKode.equals(txtbKode.getValue())) changed = true;

        if (!oldVar_txtbNama.equals(txtbNama.getValue())) changed = true;

        if (!oldVar_txtbJenis.equals(txtbJenis.getValue())) changed = true;

        if (!oldVar_txtbStatus.equals(txtbStatus.getValue())) changed = true;

        if (!oldVar_txtbMun.equals(txtbMun.getValue())) changed = true;

        if (!oldVar_txtbTerm.equals(txtbTerm.getValue())) changed = true;

        if (!oldVar_txtbLintasProdi.equals(txtbLintasProdi.getValue())) changed = true;

        return changed;
    }

    private void doShowDialog(Mdetilkurikulum mdetilkurikulum) throws InterruptedException {
        try {
            windowDetilKurikulum.doModal();
        } catch (final Exception e) {
            Messagebox.show(e.toString());
        }
    }

    public KurikulumDetailCtrl getKurikulumDetailCtrl() {
        return kurikulumDetailCtrl;
    }

    public void setKurikulumDetailCtrl(KurikulumDetailCtrl kurikulumDetailCtrl) {
        this.kurikulumDetailCtrl = kurikulumDetailCtrl;
    }

    public Mdetilkurikulum getMdetilkurikulum() {
        return mdetilkurikulum;
    }

    public void setMdetilkurikulum(Mdetilkurikulum mdetilkurikulum) {
        this.mdetilkurikulum = mdetilkurikulum;
    }
}
