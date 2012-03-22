package id.ac.idu.webui.mankurikulum.sesi;

import id.ac.idu.backend.model.Msesikuliah;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.mankurikulum.service.SesiService;
import id.ac.idu.webui.util.GFCBaseListCtrl;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
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

public class SesiListCtrl extends GFCBaseListCtrl<Msesikuliah> implements Serializable {
    private static final long serialVersionUID = -2170565288232491362L;
    private static final Logger logger = Logger.getLogger(SesiListCtrl.class);

    protected Window windowSesiList;
    private transient SesiService service;

    protected Panel panelList;
    protected Borderlayout borderLayout_List;
    protected Paging paging_list;
    protected Listbox listBox;

    private HibernateSearchObject<Msesikuliah> searchObj;
    private int countRows;
    private AnnotateDataBinder binder;
    private SesiMainCtrl mainCtrl;

    protected Listheader listheader_Cdksesi;
    protected Listheader listheader_Sekolah_Id;
    protected Listheader listheader_Cjamawal;
    protected Listheader listheader_CJamakhir;


    public SesiListCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);

        this.self.setAttribute("controller", this, false);

        if (arg.containsKey("ModuleMainController")) {
            setMainCtrl((SesiMainCtrl) arg.get("ModuleMainController"));
            getMainCtrl().setListCtrl(this);

            if (getMainCtrl().getSelected() != null) setSelected(getMainCtrl().getSelected());
            else setSelected(null);
        } else {
            setSelected(null);
        }
    }

    public void onCreate$windowSesiList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();
        binder.loadAll();
    }

    public void doFillListbox() {
        doFitSize();

        paging_list.setPageSize(getCountRows());
        paging_list.setDetailed(true);

        listheader_Cdksesi.setSortAscending(new FieldComparator("ckdsesi", true));
        listheader_Cdksesi.setSortDescending(new FieldComparator("ckdsesi", true));
        listheader_Sekolah_Id.setSortAscending(new FieldComparator("msekolah.id", false));
        listheader_Sekolah_Id.setSortDescending(new FieldComparator("msekolah.id", false));
        listheader_Cjamawal.setSortAscending(new FieldComparator("cjamawal", true));
        listheader_Cjamawal.setSortDescending(new FieldComparator("cjamawal", true));
        listheader_CJamakhir.setSortAscending(new FieldComparator("cjamakhir", false));
        listheader_CJamakhir.setSortDescending(new FieldComparator("cjamakhir", false));

        searchObj = new HibernateSearchObject<Msesikuliah>(Msesikuliah.class, getCountRows());
        searchObj.addSort("ckdsesi", false);
        setSearchObj(searchObj);

        getPagedBindingListWrapper().init(searchObj, getListBox(), paging_list);
        BindingListModelList lml = (BindingListModelList) getListBox().getModel();
        setBinding(lml);

        if (getSelected() == null) {
            if (lml.size() > 0) {
                final int rowIndex = 0;
                getListBox().setSelectedIndex(rowIndex);
                setSelected((Msesikuliah) lml.get(0));
                Events.sendEvent(new Event("onSelect", getListBox(), getSelected()));
            } else new SesiDetailCtrl();
        }
    }

    public void onDoubleClickedOfficeItem(Event event) {
        Msesikuliah anObject = getSelected();

        if (anObject != null) {
            setSelected(anObject);

            if (getMainCtrl().getDetailCtrl() == null) {
                Events.sendEvent(new Event("onSelect", getMainCtrl().tabDetail, null));
            } else if (getMainCtrl().getDetailCtrl().getBinder() == null) {
                Events.sendEvent(new Event("onSelect", getMainCtrl().tabDetail, null));
            }

            Events.sendEvent(new Event("onSelect", getMainCtrl().tabDetail, anObject));
        }
    }

    public void onSelect$listBoxOffice(Event event) {
        Msesikuliah anObject = getSelected();

        if (anObject == null) {
            return;
        }

        if (getMainCtrl().getDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", getMainCtrl().tabDetail, null));
        } else if (getMainCtrl().getDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", getMainCtrl().tabDetail, null));
        }

        getMainCtrl().getDetailCtrl().setSelected(anObject);
        getMainCtrl().doStoreInitValues();

        String str = Labels.getLabel("common.Office") + ": " + anObject.getCkdsesi();
        EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

    }

    public void doFitSize() {
        final int specialSize = 5;
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
        final int maxListBoxHeight = height - specialSize - 148;
        setCountRows((int) Math.round(maxListBoxHeight / 17.7));
        borderLayout_List.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowSesiList.invalidate();
    }

    public Msesikuliah getSelected() {
        return getMainCtrl().getSelected();
    }

    public void setSelected(Msesikuliah _obj) {
        getMainCtrl().setSelected(_obj);
    }

    public HibernateSearchObject<Msesikuliah> getSearchObj() {
        return searchObj;
    }

    public void setSearchObj(HibernateSearchObject<Msesikuliah> searchObj) {
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

    public Listbox getListBox() {
        return listBox;
    }

    public void setListBox(Listbox listBox) {
        this.listBox = listBox;
    }
}
