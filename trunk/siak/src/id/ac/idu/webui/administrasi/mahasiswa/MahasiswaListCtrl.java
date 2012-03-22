package id.ac.idu.webui.administrasi.mahasiswa;

import id.ac.idu.administrasi.service.MahasiswaService;
import id.ac.idu.backend.model.Mmahasiswa;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.webui.util.GFCBaseListCtrl;
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
 * User: hermanto
 * Date: 08 Mar 12
 * Time: 16:55:45
 */

public class MahasiswaListCtrl extends GFCBaseListCtrl<Mmahasiswa> implements Serializable {
    private static final long serialVersionUID = -2170565288232491362L;
    private static final Logger logger = Logger.getLogger(MahasiswaListCtrl.class);

    protected Window windowMahasiswaList;
    private transient MahasiswaService service;

    protected Panel panelList;
    protected Borderlayout borderLayout_List;
    protected Paging paging_list;
    protected Listbox listBox;

    private HibernateSearchObject<Mmahasiswa> searchObj;
    private int countRows;
    private AnnotateDataBinder binder;
    private MahasiswaMainCtrl mainCtrl;

    protected Listheader list_header_cnim;
    protected Listheader list_header_cnama;
    protected Listheader list_header_noktp;
    protected Listheader list_header_cjnsmhs;
    protected Listheader list_header_ctemplhr;
    protected Listheader list_header_dtglhr;
    protected Listheader list_header_cjenkel;
    protected Listheader list_header_cgoldar;
    protected Listheader list_header_ckdagama;
    protected Listheader list_header_cstatnkh;


    public MahasiswaListCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);

        this.self.setAttribute("controller", this, false);

        if (arg.containsKey("ModuleMainController")) {
            setMainCtrl((MahasiswaMainCtrl) arg.get("ModuleMainController"));
            getMainCtrl().setListCtrl(this);

            /*if (getMainCtrl().getSelected() != null) setSelected(getMainCtrl().getSelected());
            else setSelected(null);*/
        } /*else {
            setSelected(null);
        }*/
    }

    public void onCreate$windowMahasiswaList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();
        binder.loadAll();
    }

    public void doFillListbox() {
        doFitSize();

        paging_list.setPageSize(getCountRows());
        paging_list.setDetailed(true);

        list_header_cnim.setSortAscending(new FieldComparator("cnim",true));
        list_header_cnama.setSortAscending(new FieldComparator("cnama",true));
        list_header_noktp.setSortAscending(new FieldComparator("noktp",true));
        list_header_cjnsmhs.setSortAscending(new FieldComparator("cjnsmhs",true));
        list_header_ctemplhr.setSortAscending(new FieldComparator("ctemplhr",true));
        list_header_dtglhr.setSortAscending(new FieldComparator("dtglhr",true));
        list_header_cjenkel.setSortAscending(new FieldComparator("cjenkel",true));
        list_header_cgoldar.setSortAscending(new FieldComparator("cgoldar",true));
        list_header_ckdagama.setSortAscending(new FieldComparator("ckdagama",true));
        list_header_cstatnkh.setSortAscending(new FieldComparator("cstatnkh",true));
        list_header_cnim.setSortDescending(new FieldComparator("cnim",true));
        list_header_cnama.setSortDescending(new FieldComparator("cnama",true));
        list_header_noktp.setSortDescending(new FieldComparator("noktp",true));
        list_header_cjnsmhs.setSortDescending(new FieldComparator("cjnsmhs",true));
        list_header_ctemplhr.setSortDescending(new FieldComparator("ctemplhr",true));
        list_header_dtglhr.setSortDescending(new FieldComparator("dtglhr",true));
        list_header_cjenkel.setSortDescending(new FieldComparator("cjenkel",true));
        list_header_cgoldar.setSortDescending(new FieldComparator("cgoldar",true));
        list_header_ckdagama.setSortDescending(new FieldComparator("ckdagama",true));
        list_header_cstatnkh.setSortDescending(new FieldComparator("cstatnkh",true));

        searchObj = new HibernateSearchObject<Mmahasiswa>(Mmahasiswa.class, getCountRows());
        searchObj.addSort("cnim", false);
        setSearchObj(searchObj);

        getPagedBindingListWrapper().init(searchObj, getListBox(), paging_list);
        BindingListModelList lml = (BindingListModelList) getListBox().getModel();
        setBinding(lml);

        if (getSelected() == null) {
            if (lml.size() > 0) {
                final int rowIndex = 0;
                getListBox().setSelectedIndex(rowIndex);
                setSelected((Mmahasiswa) lml.get(0));
                Events.sendEvent(new Event("onSelect", getListBox(), getSelected()));
            } else new MahasiswaDetailCtrl();
        }
    }

    public void onDoubleClickedOfficeItem(Event event) {
        Mmahasiswa anObject = getSelected();

        if (anObject != null) {
            setSelected(anObject);

            if (getMainCtrl().getDetailCtrl() == null) Events.sendEvent(new Event("onSelect", getMainCtrl().tabDetail, null));
            else Events.sendEvent(new Event("onSelect", getMainCtrl().tabDetail, null));

            Events.sendEvent(new Event("onSelect", getMainCtrl().tabDetail, anObject));
        }
    }

    public void onSelect$listBox(Event event) {
        Mmahasiswa anObject = getSelected();

        if (anObject == null) return;
        if (getMainCtrl().getDetailCtrl() == null) Events.sendEvent(new Event("onSelect", getMainCtrl().tabDetail, null));
        else  Events.sendEvent(new Event("onSelect", getMainCtrl().tabDetail, null));

        getMainCtrl().getDetailCtrl().setSelected(anObject);
        getMainCtrl().getDetailCtrl().doStoreInitValues();

        String str = "Mahasiswa: " + anObject.getCnim();
        EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

    }

    public void doFitSize() {
        final int specialSize = 5;
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
        final int maxListBoxHeight = height - specialSize - 148;
        setCountRows((int) Math.round(maxListBoxHeight / 17.7));
        borderLayout_List.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowMahasiswaList.invalidate();
    }

    public Mmahasiswa getSelected() {
        return getMainCtrl().getSelected();
    }

    public void setSelected(Mmahasiswa _obj) {
        getMainCtrl().setSelected(_obj);
    }

    public HibernateSearchObject<Mmahasiswa> getSearchObj() {
        return searchObj;
    }

    public void setSearchObj(HibernateSearchObject<Mmahasiswa> searchObj) {
        this.searchObj = searchObj;
    }

    public int getCountRows() {
        return countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public BindingListModelList getBinding() {
        return getMainCtrl().getBinding();
    }

    public void setBinding(BindingListModelList _obj) {
        getMainCtrl().setBinding(_obj);
    }

    public AnnotateDataBinder getBinder() {
        return binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public MahasiswaMainCtrl getMainCtrl() {
        return mainCtrl;
    }

    public void setMainCtrl(MahasiswaMainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public MahasiswaService getService() {
        return service;
    }

    public void setService(MahasiswaService service) {
        this.service = service;
    }

    public Listbox getListBox() {
        return listBox;
    }

    public void setListBox(Listbox listBox) {
        this.listBox = listBox;
    }
}
