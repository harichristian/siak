package id.ac.idu.webui.administrasi.mahasiswa;

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
 * @Date 17 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MahasiswaListCtrl extends GFCBaseListCtrl<Mmahasiswa> implements Serializable {
    private static final long serialVersionUID = -2170565288232491362L;
    private static final Logger logger = Logger.getLogger(MahasiswaListCtrl.class);

    protected Window windowMahasiswaList;
    protected Borderlayout borderLayoutList;

    /* List Object */
    protected Paging paginglist;
    protected Listbox listBox;
    protected Listheader headerCnim;
    protected Listheader headerCnama;
    protected Listheader headerNoktp;
    protected Listheader headerCjnsmhs;
    protected Listheader headerCtemplhr;
    protected Listheader headerDtglhr;
    protected Listheader headerCjenkel;
    protected Listheader headerCgoldar;
    protected Listheader headerCkdagama;
    protected Listheader headerCstatnkh;

    private MahasiswaMainCtrl mainCtrl;

    private HibernateSearchObject<Mmahasiswa> searchObj;
    private int countRows;

    public MahasiswaListCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        logger.info("Page List Mahasiswa Loaded");

        super.doAfterCompose(window);
        this.self.setAttribute("controller", this, false);

        if (arg.containsKey("ModuleMainController")) {
            setMainCtrl((MahasiswaMainCtrl) arg.get("ModuleMainController"));
            getMainCtrl().setListCtrl(this);
        }
    }

    public void onCreate$windowMahasiswaList(Event event) throws Exception {
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
        initSort();

        searchObj = new HibernateSearchObject<Mmahasiswa>(Mmahasiswa.class, countRows);

        if (StringUtils.isNotEmpty(getMainCtrl().txtbNim.getValue()))
            searchObj.addFilter(new Filter("cnim", "%" + getMainCtrl().txtbNim.getValue() + "%", Filter.OP_ILIKE));

        if (StringUtils.isNotEmpty(getMainCtrl().txtbNama.getValue()))
            searchObj.addFilter(new Filter("cnama", "%" + getMainCtrl().txtbNama.getValue() + "%", Filter.OP_ILIKE));

        searchObj.addSort("cnim", false);

        getPagedBindingListWrapper().init(searchObj, listBox, paginglist);
        BindingListModelList lml = (BindingListModelList) listBox.getModel();
        setBinding(lml);

        if(lml.size() < 1) {
            return;
        }

        int rowIndex = listBox.getSelectedIndex();
        if(rowIndex < 0) rowIndex = 0;

        listBox.setSelectedIndex(rowIndex);
        setMahasiswa((Mmahasiswa) lml.get(rowIndex));
        
        Event ev = new Event(Events.ON_SELECT, listBox, getMahasiswa());
        Events.sendEvent(ev);
    }

    private void initSort() {
        headerCnim.setSortAscending(new FieldComparator("cnim",true));
        headerCnim.setSortDescending(new FieldComparator("cnim",false));
        headerCnama.setSortAscending(new FieldComparator("cnama",true));
        headerCnama.setSortDescending(new FieldComparator("cnama",false));
        headerNoktp.setSortAscending(new FieldComparator("noktp",true));
        headerNoktp.setSortDescending(new FieldComparator("noktp",false));
        headerCjnsmhs.setSortAscending(new FieldComparator("cjnsmhs",true));
        headerCjnsmhs.setSortDescending(new FieldComparator("cjnsmhs",false));
        headerCtemplhr.setSortAscending(new FieldComparator("ctemplhr",true));
        headerCtemplhr.setSortDescending(new FieldComparator("ctemplhr",false));
        headerDtglhr.setSortAscending(new FieldComparator("dtglhr",true));
        headerDtglhr.setSortDescending(new FieldComparator("dtglhr",false));
        headerCjenkel.setSortAscending(new FieldComparator("cjenkel",true));
        headerCjenkel.setSortDescending(new FieldComparator("cjenkel",false));
        headerCgoldar.setSortAscending(new FieldComparator("cgoldar",true));
        headerCgoldar.setSortDescending(new FieldComparator("cgoldar",false));
        headerCkdagama.setSortAscending(new FieldComparator("ckdagama",true));
        headerCkdagama.setSortDescending(new FieldComparator("ckdagama",false));
        headerCstatnkh.setSortAscending(new FieldComparator("cstatnkh",true));
        headerCstatnkh.setSortDescending(new FieldComparator("cstatnkh",false));
    }

    private void resizeWindow() {
        final int specialSize = 5;
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
        final int maxListBoxHeight = height - specialSize - 214;

        countRows = (int) Math.round(maxListBoxHeight / 17.7);
        borderLayoutList.setHeight(String.valueOf(maxListBoxHeight) + "px");
        windowMahasiswaList.invalidate();
    }

    public MahasiswaMainCtrl getMainCtrl() {
        return mainCtrl;
    }

    public void setMainCtrl(MahasiswaMainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public BindingListModelList getBinding() {
        return getMainCtrl().getBinding();
    }

    public void setBinding(BindingListModelList _obj) {
        getMainCtrl().setBinding(_obj);
    }

    public Mmahasiswa getMahasiswa() {
        return getMainCtrl().getMahasiswa();
    }

    public void setMahasiswa(Mmahasiswa _obj) {
        getMainCtrl().setMahasiswa(_obj);
    }

    public HibernateSearchObject<Mmahasiswa> getSearchObj() {
        return searchObj;
    }

    public void setSearchObj(HibernateSearchObject<Mmahasiswa> searchObj) {
        this.searchObj = searchObj;
    }

    public int getCountRows() {
        return this.countRows;
    }
}
