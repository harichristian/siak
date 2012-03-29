package id.ac.idu.webui.irs.cutimhs;

import com.trg.search.Filter;
import id.ac.idu.administrasi.service.MahasiswaService;
import id.ac.idu.backend.model.Mmahasiswa;
import id.ac.idu.backend.model.Tcutimhs;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.irs.service.CutimhsService;
import id.ac.idu.util.Codec;
import id.ac.idu.webui.irs.cutimhs.model.OrderSearchMahasiswaList;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.pagging.PagedListWrapper;
import org.apache.commons.lang.StringUtils;
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

    protected Window windowCutimhsDetail;
    protected Borderlayout borderlayout_Detail;
    protected Bandbox bandbox_Dialog_MahasiswaSearch;
    protected Paging paging_MahasiswaSearchList;
    protected Listbox listMahasiswaSearch;

    protected Textbox txtb_term;
    protected Textbox txtb_nim;
    protected Textbox txtb_nama;
    protected Intbox txtb_mhsid;
    protected Datebox txtb_tanggal;

    /* Search Box */
    protected Textbox tb_Nim;
    protected Textbox tb_Nama;
    protected Textbox tb_NoKtp;

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

    public void onCreate$windowCutimhsDetail(Event event) throws Exception {
        setPageSize(20);
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);
        binder.loadAll();
        doFitSize(event);

        txtb_tanggal.setDisabled(true);
        txtb_mhsid.setVisible(false);
    }

    public void onOpen$bandbox_Dialog_MahasiswaSearch(Event event) {
        this.searchMahasiswa(null);
    }

    public void onClick$button_bbox_Search(Event event) {
        Filter filter1 = null;
        Filter filter2 = null;
        Filter filter3 = null;

        if (StringUtils.isNotEmpty(tb_Nim.getValue()))
            filter1 = new Filter("cnim", "%" + tb_Nim.getValue() + "%", Filter.OP_LIKE);

        if (StringUtils.isNotEmpty(tb_Nama.getValue()))
            filter2 = new Filter("cnama", "%" + tb_Nama.getValue() + "%", Filter.OP_LIKE);

        if (StringUtils.isNotEmpty(tb_NoKtp.getValue()))
            filter3 = new Filter("noktp", "%" + tb_NoKtp.getValue() + "%", Filter.OP_LIKE);
        
        this.searchMahasiswa(filter1, filter2, filter3);
    }

    public void searchMahasiswa(Filter... filters) {

        HibernateSearchObject<Mmahasiswa> soCuti = new HibernateSearchObject<Mmahasiswa>(Mmahasiswa.class);
        soCuti.addFilter(new com.trg.search.Filter("mstatusmhs.ckdstatmhs", Codec.StatusMahasiswa.Status1.getValue(), com.trg.search.Filter.OP_EQUAL));
        if(filters != null) {
            for(Filter anFilter : filters) {
                if(anFilter != null) soCuti.addFilter(anFilter);
            }
        }
        
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

        windowCutimhsDetail.invalidate();
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
