package id.ac.idu.webui.mankurikulum.matakuliah;

import id.ac.idu.backend.model.Mtbmtkl;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.mankurikulum.service.MatakuliahService;
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

public class MatakuliahListCtrl extends GFCBaseListCtrl<Mtbmtkl> implements Serializable {
    private static final long serialVersionUID = -2170565288232491362L;
    private static final Logger logger = Logger.getLogger(MatakuliahListCtrl.class);

    protected Window windowMatakuliahList;
    private transient MatakuliahService service;

    protected Panel panelList;
    protected Borderlayout borderLayout_List;
    protected Paging paging_list;
    protected Listbox listBox;

    private HibernateSearchObject<Mtbmtkl> searchObj;
    private int countRows;
    private AnnotateDataBinder binder;
    private MatakuliahMainCtrl mainCtrl;

    protected Listheader listheader_Cdkmtk;
    protected Listheader listheader_Nama;
    protected Listheader listheader_Nama2;
    protected Listheader listheader_Singkatan;
    protected Listheader listheader_Sks;
    protected Listheader listheader_Ket;


    public MatakuliahListCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);

        this.self.setAttribute("controller", this, false);

        if (arg.containsKey("ModuleMainController")) {
            setMainCtrl((MatakuliahMainCtrl) arg.get("ModuleMainController"));
            getMainCtrl().setListCtrl(this);

            if (getMainCtrl().getSelected() != null) setSelected(getMainCtrl().getSelected());
            else setSelected(null);
        } else {
            setSelected(null);
        }
    }

    public void onCreate$windowMatakuliahList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();
        binder.loadAll();
    }

    public void doFillListbox() {
        doFitSize();

        paging_list.setPageSize(getCountRows());
        paging_list.setDetailed(true);

        listheader_Cdkmtk.setSortAscending(new FieldComparator("ckdmtk", true));
        listheader_Cdkmtk.setSortDescending(new FieldComparator("ckdmtk", true));
        listheader_Nama.setSortAscending(new FieldComparator("cnamamk", true));
        listheader_Nama.setSortDescending(new FieldComparator("cnamamk", true));
        listheader_Nama2.setSortAscending(new FieldComparator("cingmk", true));
        listheader_Nama2.setSortDescending(new FieldComparator("cingmk", true));
        listheader_Singkatan.setSortAscending(new FieldComparator("csingmk", true));
        listheader_Singkatan.setSortDescending(new FieldComparator("csingmk", true));
        listheader_Sks.setSortAscending(new FieldComparator("nsks", true));
        listheader_Sks.setSortDescending(new FieldComparator("nsks", true));
        listheader_Ket.setSortAscending(new FieldComparator("keterangan", true));
        listheader_Ket.setSortDescending(new FieldComparator("keterangan", true));

        searchObj = new HibernateSearchObject<Mtbmtkl>(Mtbmtkl.class, getCountRows());
        searchObj.addSort("ckdmtk", false);
        setSearchObj(searchObj);

        getPagedBindingListWrapper().init(searchObj, getListBox(), paging_list);
        BindingListModelList lml = (BindingListModelList) getListBox().getModel();
        setBinding(lml);

        if (getSelected() == null) {
            if (lml.size() > 0) {
                final int rowIndex = 0;
                getListBox().setSelectedIndex(rowIndex);
                setSelected((Mtbmtkl) lml.get(0));
                Events.sendEvent(new Event("onSelect", getListBox(), getSelected()));
            } else new MatakuliahDetailCtrl();
        }
    }

    public void onDoubleClickedOfficeItem(Event event) {
        Mtbmtkl anObject = getSelected();

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

    public void onSelect$listBox(Event event) {
        Mtbmtkl anObject = getSelected();

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

        String str = Labels.getLabel("common.Office") + ": " + anObject.getCkdmtk();
        EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

    }

    public void doFitSize() {
        final int specialSize = 5;
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
        final int maxListBoxHeight = height - specialSize - 148;
        setCountRows((int) Math.round(maxListBoxHeight / 17.7));
        borderLayout_List.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowMatakuliahList.invalidate();
    }

    public Mtbmtkl getSelected() {
        return getMainCtrl().getSelected();
    }

    public void setSelected(Mtbmtkl _obj) {
        getMainCtrl().setSelected(_obj);
    }

    public HibernateSearchObject<Mtbmtkl> getSearchObj() {
        return searchObj;
    }

    public void setSearchObj(HibernateSearchObject<Mtbmtkl> searchObj) {
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

    public MatakuliahMainCtrl getMainCtrl() {
        return mainCtrl;
    }

    public void setMainCtrl(MatakuliahMainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public MatakuliahService getService() {
        return service;
    }

    public void setService(MatakuliahService service) {
        this.service = service;
    }

    public Listbox getListBox() {
        return listBox;
    }

    public void setListBox(Listbox listBox) {
        this.listBox = listBox;
    }
}
