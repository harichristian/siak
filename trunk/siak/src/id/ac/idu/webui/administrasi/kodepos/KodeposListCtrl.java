package id.ac.idu.webui.administrasi.kodepos;

import com.trg.search.Filter;
import id.ac.idu.backend.model.MkodePos;
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
 * @Date 30 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class KodeposListCtrl extends GFCBaseListCtrl<MkodePos> implements Serializable {
    private static final long serialVersionUID = -2170565288232491362L;
    private static final Logger logger = Logger.getLogger(KodeposListCtrl.class);

    protected Window windowKodeposList;
    protected Borderlayout borderLayoutList;

    /* List Object */
    protected Paging paginglist;
    protected Listbox listBox;
    protected Listheader headerId;
    protected Listheader headerKodepos;

    private KodeposMainCtrl mainCtrl;

    private HibernateSearchObject<MkodePos> searchObj;
    private int countRows;

    public KodeposListCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        logger.info("Page List Mahasiswa Loaded");

        super.doAfterCompose(window);
        this.self.setAttribute("controller", this, false);

        if (arg.containsKey("ModuleMainController")) {
            setMainCtrl((KodeposMainCtrl) arg.get("ModuleMainController"));
            getMainCtrl().setListCtrl(this);
        }
    }

    public void onCreate$windowKodeposList(Event event) throws Exception {
        AnnotateDataBinder binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);
        loadListData();
        binder.loadAll();
    }

    public void onSelect$listBox(Event event) {
        MkodePos anObject = getMkodePos();

        if (anObject == null) return;

        getMainCtrl().setMkodePos(anObject);
        getMainCtrl().doStoreInitValues();

        String str = "Kodepos : " + anObject.getKodepos();
        EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));
    }

    public void onKodeposItem(Event event) {
        final MkodePos anObject = getMkodePos();
        getMainCtrl().setMkodePos(anObject);

        if (anObject == null) return;

        Events.sendEvent(new Event("onSelect", getMainCtrl().tabDetail, anObject));
        getMainCtrl().tabDetail.setSelected(true);
    }

    public void loadListData() {
        resizeWindow();
        paginglist.setPageSize(countRows);
        paginglist.setDetailed(true);
        initSort();

        searchObj = new HibernateSearchObject<MkodePos>(MkodePos.class, countRows);

        if (StringUtils.isNotEmpty(getMainCtrl().txtbId.getValue()))
            searchObj.addFilter(new Filter("id", "%" + getMainCtrl().txtbId.getValue() + "%", com.trg.search.Filter.OP_LIKE));

        if (StringUtils.isNotEmpty(getMainCtrl().txtbKodePos.getValue()))
            searchObj.addFilter(new Filter("kodepos", "%" + getMainCtrl().txtbKodePos.getValue() + "%", com.trg.search.Filter.OP_LIKE));

        searchObj.addSort("kodepos", false);

        getPagedBindingListWrapper().init(searchObj, listBox, paginglist);
        BindingListModelList lml = (BindingListModelList) listBox.getModel();
        setBinding(lml);

        if(lml.size() < 1) {
            return;
        }

        int rowIndex = listBox.getSelectedIndex();
        if(rowIndex < 0) rowIndex = 0;

        listBox.setSelectedIndex(rowIndex);
        setMkodePos((MkodePos) lml.get(rowIndex));

        Event ev = new Event(Events.ON_SELECT, listBox, getMkodePos());
        Events.sendEvent(ev);
    }

    private void initSort() {
        headerId.setSortAscending(new FieldComparator("id",true));
        headerId.setSortDescending(new FieldComparator("id",false));
        headerKodepos.setSortAscending(new FieldComparator("kodepos",true));
        headerKodepos.setSortDescending(new FieldComparator("kodepos",false));
    }

    private void resizeWindow() {
        final int specialSize = 5;
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
        final int maxListBoxHeight = height - specialSize - 214;

        countRows = (int) Math.round(maxListBoxHeight / 17.7);
        borderLayoutList.setHeight(String.valueOf(maxListBoxHeight) + "px");
        windowKodeposList.invalidate();
    }

    public KodeposMainCtrl getMainCtrl() {
        return mainCtrl;
    }

    public void setMainCtrl(KodeposMainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public MkodePos getMkodePos() {
        return getMainCtrl().getMkodePos();
    }

    public void setMkodePos(MkodePos mkodePos) {
        getMainCtrl().setMkodePos(mkodePos);
    }

    public BindingListModelList getBinding() {
        return getMainCtrl().getBinding();
    }

    public void setBinding(BindingListModelList _obj) {
        getMainCtrl().setBinding(_obj);
    }

    public Listbox getListBox() {
        return listBox;
    }

    public void setListBox(Listbox listBox) {
        this.listBox = listBox;
    }
}
