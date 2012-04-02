package id.ac.idu.webui.administrasi.mahasiswa;

import com.trg.search.Filter;
import id.ac.idu.backend.model.Mjenjang;
import id.ac.idu.backend.model.Mmahasiswa;
import id.ac.idu.backend.model.Mpeminatan;
import id.ac.idu.backend.model.Mprodi;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.util.Codec;
import id.ac.idu.webui.administrasi.mahasiswa.model.StatusSearchJenjangList;
import id.ac.idu.webui.administrasi.mahasiswa.model.StatusSearchPeminatanList;
import id.ac.idu.webui.administrasi.mahasiswa.model.StatusSearchProdiList;
import id.ac.idu.webui.util.EnumConverter;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.GFCListModelCtrl;
import id.ac.idu.webui.util.pagging.PagedListWrapper;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.*;

import java.io.Serializable;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 02 Apr 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MahasiswaStatus extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(MahasiswaStatus.class);

    private int pageSize;

    protected Window windowStatusDetail;
    protected Borderlayout borderStatus;

    private MahasiswaDetailCtrl detailCtrl;
    private AnnotateDataBinder binder;

    private transient PagedListWrapper<Mprodi> plwProdi;
    protected Paging pagingmprodi;
    protected Listbox listmprodi;
    protected Textbox tbckdprogst;
    protected Textbox tbcnmprogst;
    protected Textbox txtbmprodi;

    private transient PagedListWrapper<Mjenjang> plwJenjang;
    protected Paging pagingmjenjang;
    protected Listbox listmjenjang;
    protected Textbox tbckdjen;
    protected Textbox tbcnmjen;
    protected Textbox txtbmjenjang;

    private transient PagedListWrapper<Mpeminatan> plwPeminatan;
    protected Paging pagingmpeminatan;
    protected Listbox listmpeminatan;
    protected Textbox tbckdminat;
    protected Textbox tbcnmminat;
    protected Textbox txtbmpeminatan;

    protected Textbox txtbcnosttb;
    protected Datebox txtbdtglsttb;
    protected Datebox txtbdtglmasuk;
    protected Datebox txtbdtglwisuda;
    protected Datebox txtbdtglyudisi;
    protected Datebox txtbdtglteori;
    protected Checkbox chkbcflagspbm;
    protected Checkbox chkbcflagnilai;
    protected Listbox txtbcstatawal;
    protected Bandbox cmbcstatawal;
    protected Bandbox cmbmprodi;
    protected Bandbox cmbmjenjang;
    protected Bandbox cmbmpeminatan;
    protected Textbox txtbcthnlaporan;

    public MahasiswaStatus() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        logger.info("Page Khusus Makasiswa Loaded");

        super.doAfterCompose(window);
        self.setAttribute("controller", this, false);

        if(arg.containsKey("ModuleMainController")) {
            setDetailCtrl((MahasiswaDetailCtrl) arg.get("ModuleMainController"));
            getDetailCtrl().setStatusCtrl(this);
        }
    }

    @SuppressWarnings("unchecked")
    public void onCreate$windowStatusDetail(Event event) throws Exception {
        setPageSize(20);
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        GFCListModelCtrl.getInstance().setListModel((new EnumConverter(Codec.StatusAwal.class)).getEnumToList()
                ,txtbcstatawal, cmbcstatawal, (getMahasiswa() != null)?getMahasiswa().getCstatawal():null);

        binder.loadAll();
        doReadOnlyMode(!getDetailCtrl().getMainCtrl().btnSave.isVisible());
        this.doFitSize();
    }

    public void onOpen$cmbmjenjang(Event event) {
        this.searchJenjang();
    }

    public void onClick$buttonSearchMjenjang(Event event) {
        Filter filter1 = null;
        Filter filter2 = null;
        
        if (StringUtils.isNotEmpty(tbckdjen.getValue()))
            filter1 = new Filter("ckdjen", "%" + tbckdjen.getValue() + "%", Filter.OP_LIKE);

        if (StringUtils.isNotEmpty(tbcnmjen.getValue()))
            filter2 = new Filter("cnmjen", "%" + tbcnmjen.getValue() + "%", Filter.OP_LIKE);

        this.searchJenjang(filter1, filter2);
    }

    public void searchJenjang(Filter... filters) {
        HibernateSearchObject<Mjenjang> soJenjang = new HibernateSearchObject<Mjenjang>(Mjenjang.class);

        if(filters != null) {
            for(Filter anFilter : filters) {
                if(anFilter!=null) soJenjang.addFilter(anFilter);
            }
        }

        soJenjang.addSort("ckdjen", false);
        pagingmjenjang.setPageSize(pageSize);
		pagingmjenjang.setDetailed(true);
		getPlwJenjang().init(soJenjang, listmjenjang, pagingmjenjang);
		listmjenjang.setItemRenderer(new StatusSearchJenjangList());
    }

    public void onJenjangItem(Event event) {
        Listitem item = listmjenjang.getSelectedItem();

        if (item != null) {
            Mjenjang aJenjang = (Mjenjang) item.getAttribute("data");

            getMahasiswa().setMjenjang(aJenjang);
            txtbmjenjang.setValue(aJenjang.getCkdjen());

            getMahasiswa().setMprodi(null);
            txtbmprodi.setValue("");
            
            getMahasiswa().setMpeminatan(null);
            txtbmpeminatan.setValue("");
        }

        cmbmjenjang.close();
    }

    public void onClick$buttonCloseMjenjang(Event event) {
        cmbmjenjang.close();
    }

    public void onOpen$cmbmprodi(Event event) {
        this.searchProdi();
    }

    public void onClick$buttonSearchMprodi(Event event) {
        Filter filter1 = null;
        Filter filter2 = null;
        Filter filter3 = null;

        if (StringUtils.isNotEmpty(tbckdprogst.getValue()))
            filter1 = new Filter("ckdprogst", "%" + tbckdprogst.getValue() + "%", Filter.OP_LIKE);

        if (StringUtils.isNotEmpty(tbcnmprogst.getValue()))
            filter2 = new Filter("cnmprogst", "%" + tbcnmprogst.getValue() + "%", Filter.OP_LIKE);

        if(getMahasiswa().getMjenjang() != null)
            filter3 = new Filter("mjenjang.ckdjen", getMahasiswa().getMjenjang().getCkdjen() , Filter.OP_EQUAL);

        this.searchProdi(filter1, filter2, filter3);
    }

    public void searchProdi(Filter... filters) {
        HibernateSearchObject<Mprodi> soProdi = new HibernateSearchObject<Mprodi>(Mprodi.class);

        if(filters != null) {
            for(Filter anFilter : filters) {
                if(anFilter!=null) soProdi.addFilter(anFilter);
            }
        }

        soProdi.addSort("ckdprogst", false);
        pagingmprodi.setPageSize(pageSize);
		pagingmprodi.setDetailed(true);
		getPlwProdi().init(soProdi, listmprodi, pagingmprodi);
		listmprodi.setItemRenderer(new StatusSearchProdiList());
    }

    public void onProdiItem(Event event) {
        Listitem item = listmprodi.getSelectedItem();

        if (item != null) {
            Mprodi aProdi = (Mprodi) item.getAttribute("data");

            getMahasiswa().setMprodi(aProdi);
            txtbmprodi.setValue(aProdi.getCnmprogst());

            getMahasiswa().setMpeminatan(null);
            txtbmpeminatan.setValue("");
        }

        cmbmprodi.close();
    }

    public void onClick$buttonCloseMprodi(Event event) {
        cmbmprodi.close();
    }

    public void onOpen$cmbmpeminatan(Event event) {
        this.searchPeminatan();
    }

    public void onClick$buttonSearchMpeminatan(Event event) {
        Filter filter1 = null;
        Filter filter2 = null;
        Filter filter3 = null;

        if (StringUtils.isNotEmpty(tbckdminat.getValue()))
            filter1 = new Filter("ckdminat", "%" + tbckdminat.getValue() + "%", Filter.OP_LIKE);

        if (StringUtils.isNotEmpty(tbcnmminat.getValue()))
            filter2 = new Filter("cnmminat", "%" + tbcnmminat.getValue() + "%", Filter.OP_LIKE);

        if (getMahasiswa().getMprodi() != null)
            filter3 = new Filter("mprodi.ckdprogst", getMahasiswa().getMprodi().getCkdprogst() , Filter.OP_EQUAL);

        this.searchPeminatan(filter1, filter2, filter3);
    }

    public void searchPeminatan(Filter... filters) {
        HibernateSearchObject<Mpeminatan> soPeminatan= new HibernateSearchObject<Mpeminatan>(Mpeminatan.class);

        if(filters != null) {
            for(Filter anFilter : filters) {
                if(anFilter!=null) soPeminatan.addFilter(anFilter);
            }
        }

        soPeminatan.addSort("ckdminat", false);
        pagingmpeminatan.setPageSize(pageSize);
		pagingmpeminatan.setDetailed(true);
		getPlwPeminatan().init(soPeminatan, listmpeminatan, pagingmpeminatan);
		listmpeminatan.setItemRenderer(new StatusSearchPeminatanList());
    }

    public void onPeminatanItem(Event event) {
        Listitem item = listmjenjang.getSelectedItem();

        if (item != null) {
            Mpeminatan aPeminatan = (Mpeminatan) item.getAttribute("data");

            getMahasiswa().setMpeminatan(aPeminatan);
            txtbmpeminatan.setValue(aPeminatan.getCkdminat());
        }

        cmbmpeminatan.close();
    }

    public void onClick$buttonCloseMpeminatan(Event event) {
        cmbmpeminatan.close();
    }

    public void onSelect$txtbcstatawal(Event event) {
        cmbcstatawal.setValue(txtbcstatawal.getSelectedItem().getLabel());
        getMahasiswa().setCstatawal((String) txtbcstatawal.getSelectedItem().getValue());
        cmbcstatawal.close();
    }

    public void doReadOnlyMode(boolean b) {
        txtbcnosttb.setReadonly(b);
        txtbdtglsttb.setDisabled(b);
        txtbdtglmasuk.setDisabled(b);
        txtbdtglwisuda.setDisabled(b);
        txtbdtglyudisi.setDisabled(b);
        txtbdtglteori.setDisabled(b);
        chkbcflagspbm.setDisabled(b);
        chkbcflagnilai.setDisabled(b);
        cmbcstatawal.setDisabled(b);
        cmbmprodi.setDisabled(b);
        cmbmjenjang.setDisabled(b);
        cmbmpeminatan.setDisabled(b);
        txtbcthnlaporan.setReadonly(b);
    }

    public void doFitSize() {
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue();
        final int maxListBoxHeight = height - 243;

        borderStatus.setHeight(String.valueOf(maxListBoxHeight) + "px");
        windowStatusDetail.invalidate();
    }

    public void doReset() {
        GFCListModelCtrl.getInstance().doCheckBox(txtbcstatawal, cmbcstatawal
                , (getMahasiswa()!=null)?String.valueOf(getMahasiswa().getCstatawal()):null);

        if(chkbcflagspbm.getValue().equals("Y")) chkbcflagspbm.setChecked(true);
        else chkbcflagspbm.setChecked(false);
    }

    public void reLoadPage() {
        binder.loadAll();
        this.doReset();
        this.doFitSize();
    }

    public AnnotateDataBinder getBinder() {
        return binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public MahasiswaDetailCtrl getDetailCtrl() {
        return detailCtrl;
    }

    public void setDetailCtrl(MahasiswaDetailCtrl detailCtrl) {
        this.detailCtrl = detailCtrl;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Mmahasiswa getMahasiswa() {
        return getDetailCtrl().getMainCtrl().getMahasiswa();
    }

    public void setMahasiswa(Mmahasiswa _obj) {
        getDetailCtrl().getMainCtrl().setMahasiswa(_obj);
    }

    public PagedListWrapper<Mprodi> getPlwProdi() {
        return plwProdi;
    }

    public void setPlwProdi(PagedListWrapper<Mprodi> plwProdi) {
        this.plwProdi = plwProdi;
    }

    public PagedListWrapper<Mjenjang> getPlwJenjang() {
        return plwJenjang;
    }

    public void setPlwJenjang(PagedListWrapper<Mjenjang> plwJenjang) {
        this.plwJenjang = plwJenjang;
    }

    public PagedListWrapper<Mpeminatan> getPlwPeminatan() {
        return plwPeminatan;
    }

    public void setPlwPeminatan(PagedListWrapper<Mpeminatan> plwPeminatan) {
        this.plwPeminatan = plwPeminatan;
    }
}
