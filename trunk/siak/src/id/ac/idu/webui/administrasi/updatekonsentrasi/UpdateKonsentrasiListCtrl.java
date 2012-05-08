package id.ac.idu.webui.administrasi.updatekonsentrasi;

import com.trg.search.Filter;
import id.ac.idu.backend.model.Mmahasiswa;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.webui.util.GFCBaseListCtrl;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.*;

import java.io.Serializable;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 29 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class UpdateKonsentrasiListCtrl extends GFCBaseListCtrl<Mmahasiswa> implements Serializable {

    private static final long serialVersionUID = -2170565288232491362L;
    private static final Logger logger = Logger.getLogger(UpdateKonsentrasiListCtrl.class);

    protected Window windowUpdateKonsentrasiList;
    protected Borderlayout borderLayoutList;
    protected UpdateKonsentrasiMainCtrl mainCtrl;

    /* List Object */
    protected Paging paginglist;
    protected Listbox listBox;
    protected Listheader headerCnim;
    protected Listheader headerCnama;
    protected Listheader headerNoktp;
    protected Listheader headerCjnsmhs;
    protected Listheader headerCnmminat;

    private int countRows;
    private HibernateSearchObject<Mmahasiswa> searchObj;

    public UpdateKonsentrasiListCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);
        this.self.setAttribute("controller", this, false);

        if (arg.containsKey("ModuleMainController")) {
            setMainCtrl((UpdateKonsentrasiMainCtrl) arg.get("ModuleMainController"));
            getMainCtrl().setListCtrl(this);
        }
    }

    public void onCreate$windowUpdateKonsentrasiList(Event event) throws Exception {
        AnnotateDataBinder binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);
        loadListData();
        binder.loadAll();
    }

    public void onSelect$listBox(Event event) {
        Mmahasiswa anObject = getMahasiswa();

        if (anObject == null) return;

        getMainCtrl().setMahasiswa(anObject);
        getMainCtrl().doStoreInitValues();

        String str = "Mahasiswa: " + anObject.getCnim();
        EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));
    }

    public void onDoubleClicked(Event event) {
        final Mmahasiswa anObject = getMahasiswa();
        getMainCtrl().setMahasiswa(anObject);

        if (anObject == null) return;

        Events.sendEvent(new Event("onSelect", getMainCtrl().tabDetail, anObject));
        getMainCtrl().tabDetail.setSelected(true);
    }

    public void loadListData() {
        resizeWindow();
        paginglist.setPageSize(countRows);
        paginglist.setDetailed(true);

        headerCnim.setSortAscending(new FieldComparator("cnim",true));
        headerCnim.setSortDescending(new FieldComparator("cnim",true));

        headerCnama.setSortAscending(new FieldComparator("cnama",true));
        headerCnama.setSortDescending(new FieldComparator("cnama",true));

        headerNoktp.setSortAscending(new FieldComparator("noktp",true));
        headerNoktp.setSortDescending(new FieldComparator("noktp",true));

        headerCjnsmhs.setSortAscending(new FieldComparator("cjnsmhs",true));
        headerCjnsmhs.setSortDescending(new FieldComparator("cjnsmhs",true));

        headerCnmminat.setSortAscending(new FieldComparator("cnmminat",true));
        headerCnmminat.setSortDescending(new FieldComparator("cnmminat",true));

        searchObj = new HibernateSearchObject<Mmahasiswa>(Mmahasiswa.class, countRows);

        if (StringUtils.isNotEmpty(getMainCtrl().txtbNim.getValue()))
            searchObj.addFilter(new Filter("cnim", "%" + getMainCtrl().txtbNim.getValue() + "%", Filter.OP_ILIKE));

        if (StringUtils.isNotEmpty(getMainCtrl().txtbNama.getValue()))
            searchObj.addFilter(new Filter("cnama", "%" + getMainCtrl().txtbNama.getValue() + "%", Filter.OP_ILIKE));

        searchObj.addSort("cnim", false);

        getPagedBindingListWrapper().init(searchObj, getListBox(), paginglist);
        BindingListModelList lml = (BindingListModelList) getListBox().getModel();
        setBinding(lml);

        if(getMahasiswa() == null) {
            if(lml.size() > 0) {
                final int rowIndex = 0;
                getListBox().setSelectedIndex(rowIndex);
                setMahasiswa((Mmahasiswa) lml.get(0));
                Events.sendEvent(new Event(Events.ON_SELECT, getListBox(), getMahasiswa()));
            }
        }
    }

    private void resizeWindow() {
        final int specialSize = 5;
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
        final int maxListBoxHeight = height - specialSize - 214;

        countRows = (int) Math.round(maxListBoxHeight / 17.7);
        borderLayoutList.setHeight(String.valueOf(maxListBoxHeight) + "px");
        windowUpdateKonsentrasiList.invalidate();
    }

    public BindingListModelList getBinding() {
        return getMainCtrl().getBinding();
    }

    public void setBinding(BindingListModelList _obj) {
        getMainCtrl().setBinding(_obj);
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

    public void setMahasiswa(Mmahasiswa _obj) {
        getMainCtrl().setMahasiswa(_obj);
    }

    public Listbox getListBox() {
        return listBox;
    }

    public void setListBox(Listbox listBox) {
        this.listBox = listBox;
    }
}
