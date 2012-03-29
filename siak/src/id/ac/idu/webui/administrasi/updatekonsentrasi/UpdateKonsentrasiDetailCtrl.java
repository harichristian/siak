package id.ac.idu.webui.administrasi.updatekonsentrasi;

import com.trg.search.Filter;
import id.ac.idu.backend.model.Mmahasiswa;
import id.ac.idu.backend.model.Mpeminatan;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.webui.administrasi.updatekonsentrasi.model.KonsentrasiSearchList;
import id.ac.idu.webui.util.GFCBaseCtrl;
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
 * @Date 29 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class UpdateKonsentrasiDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(UpdateKonsentrasiDetailCtrl.class);

    protected transient AnnotateDataBinder binder;

    protected Window windowUpdateKonsentrasiDetail;
    protected Borderlayout borderDetail;

    protected Bandbox cmbKonsentrasi;
    protected Paging pagingKonsentrasi;
    protected Listbox listKonsentrasi;
    protected Textbox tbKonsentrasi;
    protected Textbox txtbCodeStatBaru;
    protected Textbox txtbNamaStatBaru;

    private transient PagedListWrapper<Mpeminatan> plwKonsentrasi;
    private UpdateKonsentrasiMainCtrl mainCtrl;

    private int pageSize;

    public UpdateKonsentrasiDetailCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        logger.info("Page Detail Mahasiswa Loaded");

        super.doAfterCompose(window);
        self.setAttribute("controller", this, false);

        if(arg.containsKey("ModuleMainController")) {
            setMainCtrl((UpdateKonsentrasiMainCtrl) arg.get("ModuleMainController"));
            getMainCtrl().setDetailCtrl(this);
        }
        else setMahasiswa(null);
    }

    public void onCreate$windowUpdateKonsentrasiDetail(Event event) throws Exception {
        setPageSize(20);

        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);
        binder.loadAll();
        doFitSize();
    }

    public void onOpen$cmbKonsentrasi(Event event) {
        this.searchKonsentrasi(null);
    }

    public void onClick$buttonClose(Event event) {
        cmbKonsentrasi.close();
	}

    public void onClick$buttonSearch(Event event) {
        Filter filter = null;

        if (StringUtils.isNotEmpty(tbKonsentrasi.getValue()))
            filter = new Filter("cnmminat", "%" + tbKonsentrasi.getValue() + "%", Filter.OP_LIKE);

        this.searchKonsentrasi(filter);
    }

    public void searchKonsentrasi(Filter filter) {
        HibernateSearchObject<Mpeminatan> soKonsentrasi = new HibernateSearchObject<Mpeminatan>(Mpeminatan.class);
        if(filter != null) soKonsentrasi.addFilter(filter);

        soKonsentrasi.addSort("cnmminat", false);
        pagingKonsentrasi.setPageSize(pageSize);
		pagingKonsentrasi.setDetailed(true);
		getPlwKonsentrasi().init(soKonsentrasi, listKonsentrasi, pagingKonsentrasi);
		listKonsentrasi.setItemRenderer(new KonsentrasiSearchList());
    }

    public void onKonsentrasi(Event event) throws Exception{
        Listitem item = listKonsentrasi.getSelectedItem();

        if (item != null) {
            Mpeminatan aMinat = (Mpeminatan) item.getAttribute("data");
            txtbCodeStatBaru.setValue(aMinat.getCkdminat());
            txtbNamaStatBaru.setValue(aMinat.getCnmminat());
            getMahasiswa().setMpeminatan(aMinat);
        }

        cmbKonsentrasi.close();
    }

    public void doFitSize() {
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
        final int maxListBoxHeight = height - 218;
        borderDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowUpdateKonsentrasiDetail.invalidate();
    }

    public void doReadOnlyMode(boolean b) {
        cmbKonsentrasi.setDisabled(b);
    }

    public void reLoadPage() {
        binder.loadAll();
        txtbCodeStatBaru.setValue("");
        txtbNamaStatBaru.setValue("");
        doFitSize();
    }

    public AnnotateDataBinder getBinder() {
        return binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public UpdateKonsentrasiMainCtrl getMainCtrl() {
        return mainCtrl;
    }

    public void setMainCtrl(UpdateKonsentrasiMainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public Mmahasiswa getMahasiswa() {
        return getMainCtrl().getMahasiswa();
    }

    public void setMahasiswa(Mmahasiswa mahasiswa) {
        getMainCtrl().setMahasiswa(mahasiswa);
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public PagedListWrapper<Mpeminatan> getPlwKonsentrasi() {
        return plwKonsentrasi;
    }

    public void setPlwKonsentrasi(PagedListWrapper<Mpeminatan> plwKonsentrasi) {
        this.plwKonsentrasi = plwKonsentrasi;
    }
}
