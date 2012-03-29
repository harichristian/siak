package id.ac.idu.webui.irs.aktifmhs;

import id.ac.idu.administrasi.service.MahasiswaService;
import id.ac.idu.backend.model.Mmahasiswa;
import id.ac.idu.backend.model.Tcutimhs;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.irs.service.CutimhsService;
import id.ac.idu.webui.irs.aktifmhs.model.OrderSearchMahasiswaList;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.pagging.PagedListWrapper;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.*;

import java.io.Serializable;

/**
 * User: hermanto
 * Date: 08 Mar 12
 * Time: 16:55:28
 */

public class DetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(DetailCtrl.class);

    protected Window windowAktifmhsDetail;
    protected Borderlayout borderlayout_Detail;
    protected Bandbox bandbox_Dialog_MahasiswaSearch;
    protected Paging paging_MahasiswaSearchList;
    protected Listbox listMahasiswaSearch;

    protected Textbox txtb_term;
    protected Textbox txtb_nim;
    protected Textbox txtb_nama;
    protected Intbox txtb_mhsid;
    protected Datebox txtb_tanggal;

    protected transient AnnotateDataBinder binder;

    private transient PagedListWrapper<Mmahasiswa> plwMahasiswa;

    private transient CutimhsService service;
    private transient MahasiswaService service2;

    private int pageSize;
    private MainCtrl mainCtrl;

    public DetailCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        this.self.setAttribute("controller", this, false);

        if (arg.containsKey("ModuleMainController")) {
            setMainCtrl((MainCtrl) arg.get("ModuleMainController"));
            getMainCtrl().setDetailCtrl(this);

            if (getMainCtrl().getSelected() != null) setSelected(getMainCtrl().getSelected());
            else setSelected(null);
        } else setSelected(null);
    }

    public void onCreate$windowAktifmhsDetail(Event event) throws Exception {
        setPageSize(20);
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);
        binder.loadAll();
        doFitSize(event);

        txtb_tanggal.setDisabled(true);
        txtb_mhsid.setVisible(false);
    }

    public void onOpen$bandbox_Dialog_MahasiswaSearch(Event event) {
        HibernateSearchObject<Mmahasiswa> soCuti = new HibernateSearchObject<Mmahasiswa>(Mmahasiswa.class);
		soCuti.addFilter(new com.trg.search.Filter("ckdstatmhs", "2", com.trg.search.Filter.OP_EQUAL));
        soCuti.addSort("cnim", false);

		paging_MahasiswaSearchList.setPageSize(pageSize);
		paging_MahasiswaSearchList.setDetailed(true);
		getPlwMahasiswa().init(soCuti, listMahasiswaSearch, paging_MahasiswaSearchList);
		listMahasiswaSearch.setItemRenderer(new OrderSearchMahasiswaList());
    }

    public void onClick$button_bbox_Close(Event event) {
        bandbox_Dialog_MahasiswaSearch.close();
	}

    public void onMahasiswaItem(Event event) {
        Listitem item = listMahasiswaSearch.getSelectedItem();
        if (item != null) {

            Mmahasiswa aMahasiswa = (Mmahasiswa) item.getAttribute("data");
            getMainCtrl().setMahasiswa(aMahasiswa);

            txtb_nim.setValue(getMainCtrl().getMahasiswa().getCnim());
            txtb_mhsid.setValue(getMainCtrl().getMahasiswa().getId());
            txtb_nama.setValue(getMainCtrl().getMahasiswa().getCnama());
            bandbox_Dialog_MahasiswaSearch.setValue(getMainCtrl().getMahasiswa().getCnim());
        }
        bandbox_Dialog_MahasiswaSearch.close();
    }

    public void doFitSize(Event event) {
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
        final int maxListBoxHeight = height - 148;
        borderlayout_Detail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowAktifmhsDetail.invalidate();
    }

    public void doReadOnlyMode(boolean b) {
        txtb_term.setReadonly(b);
        txtb_tanggal.setDisabled(b);
        bandbox_Dialog_MahasiswaSearch.setDisabled(b);
    }

    public Tcutimhs getSelected() {
        return getMainCtrl().getSelected();
    }

    public void setSelected(Tcutimhs _obj) {
        getMainCtrl().setSelected(_obj);
    }

    public BindingListModelList getBinding() {
        return getMainCtrl().getBinding();
    }

    public void setBinding(BindingListModelList _obj) {
        getMainCtrl().setBinding(_obj);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public MainCtrl getMainCtrl() {
        return mainCtrl;
    }

    public void setMainCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public CutimhsService getService() {
        return service;
    }

    public void setService(CutimhsService service) {
        this.service = service;
    }

    public MahasiswaService getService2() {
        return service2;
    }

    public void setService2(MahasiswaService service2) {
        this.service2 = service2;
    }

    public PagedListWrapper<Mmahasiswa> getPlwMahasiswa() {
        return plwMahasiswa;
    }

    public void setPlwMahasiswa(PagedListWrapper<Mmahasiswa> plwMahasiswa) {
        this.plwMahasiswa = plwMahasiswa;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}