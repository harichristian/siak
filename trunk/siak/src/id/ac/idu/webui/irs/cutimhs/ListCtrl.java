package id.ac.idu.webui.irs.cutimhs;

import id.ac.idu.backend.model.Tcutimhs;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.irs.service.CutimhsService;
import id.ac.idu.util.Codec;
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

public class ListCtrl extends GFCBaseListCtrl<Tcutimhs> implements Serializable {
    private static final long serialVersionUID = -2170565288232491362L;
    private static final Logger logger = Logger.getLogger(ListCtrl.class);

    protected Window windowCutimhsList;
    private transient CutimhsService service;

    protected Panel panelList;
    protected Borderlayout borderLayout_List;
    protected Paging paging_list;
    protected Listbox listBox;

    private HibernateSearchObject<Tcutimhs> searchObj;
    private int countRows;
    private AnnotateDataBinder binder;
    private MainCtrl mainCtrl;

    protected Listheader listheader_filed1;
    protected Listheader listheader_filed2;
    protected Listheader listheader_filed3;
    protected Listheader listheader_filed4;
    protected Listheader listheader_filed5;
    protected Listheader listheader_filed6;

    public ListCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);

        this.self.setAttribute("controller", this, false);

        if (arg.containsKey("ModuleMainController")) {
            setMainCtrl((MainCtrl) arg.get("ModuleMainController"));
            getMainCtrl().setListCtrl(this);

            if (getMainCtrl().getSelected() != null) setSelected(getMainCtrl().getSelected());
            else setSelected(null);
        } else {
            setSelected(null);
        }
    }

    public void onCreate$windowCutimhsList(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doFillListbox();
        binder.loadAll();
    }

    public void doFillListbox() {
        doFitSize();

        paging_list.setPageSize(getCountRows());
        paging_list.setDetailed(true);

        listheader_filed1.setSortAscending(new FieldComparator("cnosurat", true));
        listheader_filed1.setSortDescending(new FieldComparator("cnosurat   ", true));

        listheader_filed2.setSortAscending(new FieldComparator("dtglsurat", false));
        listheader_filed2.setSortDescending(new FieldComparator("dtglsurat", false));

        listheader_filed3.setSortAscending(new FieldComparator("mahasiswa_id", true));
        listheader_filed3.setSortDescending(new FieldComparator("mahasiswa_id", true));

        listheader_filed4.setSortAscending(new FieldComparator("cterm", false));
        listheader_filed4.setSortDescending(new FieldComparator("cterm", false));

        listheader_filed5.setSortAscending(new FieldComparator("cthajar", false));
        listheader_filed5.setSortDescending(new FieldComparator("cthajar", false));

        listheader_filed6.setSortAscending(new FieldComparator("cket", false));
        listheader_filed6.setSortDescending(new FieldComparator("cket", false));

        searchObj = new HibernateSearchObject<Tcutimhs>(Tcutimhs.class, getCountRows());
        searchObj.addFilter(new com.trg.search.Filter("cjenis", Codec.JenisSurat.Status2.getValue(), com.trg.search.Filter.OP_EQUAL));
        searchObj.addSort("cnosurat", false);
        setSearchObj(searchObj);

        getPagedBindingListWrapper().init(searchObj, getListBox(), paging_list);
        BindingListModelList lml = (BindingListModelList) getListBox().getModel();
        setBinding(lml);

        if (getSelected() == null) {
            if (lml.size() > 0) {
                final int rowIndex = 0;
                getListBox().setSelectedIndex(rowIndex);
                setSelected((Tcutimhs) lml.get(0));
                Events.sendEvent(new Event("onSelect", getListBox(), getSelected()));
            } else new DetailCtrl();
        }
    }

    public void onDoubleClickedOfficeItem(Event event) {
        Tcutimhs anObject = getSelected();

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
        Tcutimhs anObject = getSelected();

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

        String str = anObject.getCnosurat();
        EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

    }

    public void onDoubleClickedItem(Event event) {
		Tcutimhs anObject = getSelected();

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

    public void doFitSize() {
        final int specialSize = 5;
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
        final int maxListBoxHeight = height - specialSize - 148;
        setCountRows((int) Math.round(maxListBoxHeight / 17.7));
        borderLayout_List.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowCutimhsList.invalidate();
    }

    public Tcutimhs getSelected() {
        return getMainCtrl().getSelected();
    }

    public void setSelected(Tcutimhs _obj) {
        getMainCtrl().setSelected(_obj);
    }

    public HibernateSearchObject<Tcutimhs> getSearchObj() {
        return searchObj;
    }

    public void setSearchObj(HibernateSearchObject<Tcutimhs> searchObj) {
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

    public MainCtrl getMainCtrl() {
        return mainCtrl;
    }

    public void setMainCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public CutimhsService getService() {
        return service;
    }

    public void setService(CutimhsService service) {
        this.service = service;
    }

    public Listbox getListBox() {
        return listBox;
    }

    public void setListBox(Listbox listBox) {
        this.listBox = listBox;
    }
}
