package id.ac.idu.webui.mankurikulum.sesi;

import com.trg.search.Filter;
import id.ac.idu.administrasi.service.SekolahService;
import id.ac.idu.backend.model.Msekolah;
import id.ac.idu.backend.model.Msesikuliah;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.mankurikulum.service.SesiService;
import id.ac.idu.webui.mankurikulum.sesi.model.SesiSearchSekolahList;
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

public class SesiDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(SesiDetailCtrl.class);

    protected Window windowSesiDetail;
    protected Borderlayout borderlayout_Detail;

    protected Textbox txtb_ckdsesi;
    protected Textbox txtb_sekolah_id;
    protected Textbox txtb_sekolah_name;
    protected Textbox txtb_cjamawal;
    protected Textbox txtb_cjamakhir;
    protected Bandbox cmbSekolah;

    protected transient AnnotateDataBinder binder;

    private SesiMainCtrl mainCtrl;
    private transient SesiService service;
    private transient SekolahService sekolah;

    private transient PagedListWrapper<Msekolah> plwSekolah;
    protected Paging pagingSekolah;
    protected Listbox listSekolah;
    protected Textbox tbKode;
    protected Textbox tbNama;
    private int pageSize;

    public SesiDetailCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        this.self.setAttribute("controller", this, false);

        if (arg.containsKey("ModuleMainController")) {
            setMainCtrl((SesiMainCtrl) arg.get("ModuleMainController"));
            getMainCtrl().setDetailCtrl(this);

            if (getMainCtrl().getSelected() != null) setSelected(getMainCtrl().getSelected());
            else setSelected(null);
        } else setSelected(null);
    }

    public void onCreate$windowSesiDetail(Event event) throws Exception {
        setPageSize(20);
        
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);
        binder.loadAll();
        doFitSize(event);
    }

    /*public void onChange$txtb_sekolah_id() {
        if (txtb_sekolah_id.getValue() != null) {
            Msekolah aeObject = this.sekolah.getSekolahByCode(txtb_sekolah_id.getValue());

            if (aeObject != null) {
                txtb_sekolah_id.setValue(txtb_sekolah_id.getValue());
                txtb_sekolah_name.setValue(aeObject.getCnamaSekolah());
                getSelected().setMsekolah(aeObject);
            } else {
                try {
                    Messagebox.show("Data Tidak ditemukan [Sekolah Code : " + txtb_sekolah_id.getValue() + " ]");
                } catch (InterruptedException e) {
                }

                txtb_sekolah_id.setValue("");
                txtb_sekolah_name.setValue("");
                txtb_sekolah_id.setFocus(true);
            }
        }
    }*/

    public void onOpen$cmbSekolah(Event event) {
        this.searchSekolah();
    }

    public void onClick$buttonClose(Event event) {
        cmbSekolah.close();
    }

    public void onClick$buttonSearch(Event event) {
        Filter filter1 = null;
        Filter filter2 = null;

        if (StringUtils.isNotEmpty(tbKode.getValue()))
            filter1 = new Filter("ckdsekolah", "%" + tbKode.getValue() + "%", Filter.OP_ILIKE);

        if (tbNama.getValue() != null)
            filter2 = new Filter("cnamaSekolah", tbNama.getValue() , Filter.OP_EQUAL);

        this.searchSekolah(filter1, filter2);
    }

    public void searchSekolah(Filter... filters) {
        HibernateSearchObject<Msekolah> soSekolah = new HibernateSearchObject<Msekolah>(Msekolah.class);

        if(filters != null) {
            for(Filter anFilter : filters) {
                if(anFilter!=null) soSekolah.addFilter(anFilter);
            }
        }

        soSekolah.addSort("ckdsekolah", false);
        pagingSekolah.setPageSize(pageSize);
		pagingSekolah.setDetailed(true);
		getPlwSekolah().init(soSekolah, listSekolah, pagingSekolah);
		listSekolah.setItemRenderer(new SesiSearchSekolahList());
    }

    public void onSekolah(Event event) {
        Listitem item = listSekolah.getSelectedItem();
        if(item == null) return;

        final Msekolah anSekolah = (Msekolah) item.getAttribute("data");
        txtb_sekolah_id.setValue(anSekolah.getCkdsekolah());
        txtb_sekolah_name.setValue(anSekolah.getCnamaSekolah());
        getSelected().setMsekolah(anSekolah);

        cmbSekolah.close();
    }

    public void doFitSize(Event event) {
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
        final int maxListBoxHeight = height - 148;
        borderlayout_Detail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowSesiDetail.invalidate();
    }

    public void doReadOnlyMode(boolean b) {
        txtb_ckdsesi.setReadonly(b);
        txtb_cjamawal.setReadonly(b);
        txtb_cjamakhir.setReadonly(b);
        cmbSekolah.setDisabled(b);
    }

    public Msesikuliah getSelected() {
        return getMainCtrl().getSelected();
    }

    public void setSelected(Msesikuliah _obj) {
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

    public SesiMainCtrl getMainCtrl() {
        return mainCtrl;
    }

    public void setMainCtrl(SesiMainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public SesiService getService() {
        return service;
    }

    public void setService(SesiService service) {
        this.service = service;
    }

    public SekolahService getSekolah() {
        return sekolah;
    }

    public void setSekolah(SekolahService sekolah) {
        this.sekolah = sekolah;
    }

    public PagedListWrapper<Msekolah> getPlwSekolah() {
        return plwSekolah;
    }

    public void setPlwSekolah(PagedListWrapper<Msekolah> plwSekolah) {
        this.plwSekolah = plwSekolah;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
