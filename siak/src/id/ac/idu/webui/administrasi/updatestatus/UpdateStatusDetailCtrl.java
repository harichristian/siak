package id.ac.idu.webui.administrasi.updatestatus;

import com.trg.search.Filter;
import id.ac.idu.backend.model.Mmahasiswa;
import id.ac.idu.backend.model.Mstatusmhs;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.webui.administrasi.updatestatus.model.StatusSearchList;
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

public class UpdateStatusDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(UpdateStatusDetailCtrl.class);

    protected transient AnnotateDataBinder binder;

    protected Window windowUpdateStatusDetail;
    protected Borderlayout borderDetail;

    protected Bandbox cmbStatus;
    protected Paging pagingStatus;
    protected Listbox listStatus;
    protected Textbox tbStatus;
    protected Textbox txtbCodeStatBaru;
    protected Textbox txtbNamaStatBaru;

    private transient PagedListWrapper<Mstatusmhs> plwStatus;
    private UpdateStatusMainCtrl mainCtrl;

    private int pageSize;
    
    public UpdateStatusDetailCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        logger.info("Page Detail Makasiswa Loaded");

        super.doAfterCompose(window);
        self.setAttribute("controller", this, false);

        if(arg.containsKey("ModuleMainController")) {
            setMainCtrl((UpdateStatusMainCtrl) arg.get("ModuleMainController"));
            getMainCtrl().setDetailCtrl(this);
        }
        else setMahasiswa(null);
    }

    public void onCreate$windowUpdateStatusDetail(Event event) throws Exception {
        setPageSize(20);
        
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);
        binder.loadAll();
        doFitSize();
    }

    public void onOpen$cmbStatus(Event event) {
        this.searchStatus(null);
    }

    public void onClick$buttonClose(Event event) {
        cmbStatus.close();
	}

    public void onClick$buttonSearch(Event event) {
        Filter filter = null;
        
        if (StringUtils.isNotEmpty(tbStatus.getValue()))
            filter = new Filter("cnmstatmhs", "%" + tbStatus.getValue() + "%", Filter.OP_ILIKE);

        this.searchStatus(filter);
    }

    protected Listheader cnmstatmhs;
    public void searchStatus(Filter filter) {
        cnmstatmhs.setSortAscending(new FieldComparator("cnmstatmhs",true));
        cnmstatmhs.setSortDescending(new FieldComparator("cnmstatmhs",false));
        
        HibernateSearchObject<Mstatusmhs> soStatus = new HibernateSearchObject<Mstatusmhs>(Mstatusmhs.class);
        if(filter != null) soStatus.addFilter(filter);

        soStatus.addSort("cnmstatmhs", false);
        pagingStatus.setPageSize(pageSize);
		pagingStatus.setDetailed(true);
		getPlwStatus().init(soStatus, listStatus, pagingStatus);
		listStatus.setItemRenderer(new StatusSearchList());
    }

    public void onStatus(Event event) throws Exception{
        Listitem item = listStatus.getSelectedItem();

        if (item != null) {
            Mstatusmhs aStatus = (Mstatusmhs) item.getAttribute("data");
            txtbCodeStatBaru.setValue(aStatus.getCkdstatmhs());
            txtbNamaStatBaru.setValue(aStatus.getCnmstatmhs());
            getMahasiswa().setMstatusmhs(aStatus);
        }
        
        cmbStatus.close();
    }

    public void doFitSize() {
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
        final int maxListBoxHeight = height - 218;
        borderDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowUpdateStatusDetail.invalidate();
    }

    public void doReadOnlyMode(boolean b) {
        cmbStatus.setDisabled(b);
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

    public UpdateStatusMainCtrl getMainCtrl() {
        return mainCtrl;
    }

    public void setMainCtrl(UpdateStatusMainCtrl mainCtrl) {
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

    public PagedListWrapper<Mstatusmhs> getPlwStatus() {
        return plwStatus;
    }

    public void setPlwStatus(PagedListWrapper<Mstatusmhs> plwStatus) {
        this.plwStatus = plwStatus;
    }
}
