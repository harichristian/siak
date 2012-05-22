package id.ac.idu.webui.util.searchdialogs;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mdetilkurikulum;
import id.ac.idu.backend.model.Tirspasca;
import id.ac.idu.kurikulum.service.DetilKurikulumService;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.sys.ComponentsCtrl;
import org.zkoss.zul.*;
import org.zkoss.zul.event.PagingEvent;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 4/22/12
 * Time: 5:59 PM
 */
public class DetilKurikulumExtendedSearchListBox extends Window implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(DetilKurikulumExtendedSearchListBox.class);

    // the textbox for inserting the searchparameter
    private Textbox _textbox;

    // button for sending the ServiceMethod if searchParameters are used
    private Button _searchButton;

    // the paging component
    private Paging _paging;

    // pageSize
    private int pageSize = 18;

    // the data listbox
    private Listbox listbox;
    private Listbox listbox2;
    private Listbox listbox3;
    private Listbox listbox4;
    private Listbox listbox5;

    // the model for the listbox
    private ListModelList listModelList;

    // the windows title
    private final String _title = Labels.getLabel("message.Information.ExtendedSearch") + " / Matakuliah Kurikulum";

    // 1. Listheader
    private final String _listHeader1 = "Cohort | Prodi     | Matakuliah | MUN | SKS";
    private final String _listHeader2 = "Prodi";
    private final String _listHeader3 = "Matakuliah";
    private final String _listHeader4 = "MUN";
    private final String _listHeader5 = "SKS";

    // the windows height
    private final int _height = 410;

    // the windows width
    private final int _width = 300;

    // the returned bean object
    private Mdetilkurikulum obj = null;

    // The service from which we get the data
    private DetilKurikulumService objService;

    /**
     * The Call method.
     *
     * @param parent The parent component
     * @return a BeanObject from the listBox or null.
     */
    public static Mdetilkurikulum show(Component parent) {
        return new DetilKurikulumExtendedSearchListBox(parent).getObj();
    }

    public static Mdetilkurikulum show(Component parent, Tirspasca irs) {
        return new DetilKurikulumExtendedSearchListBox(parent, irs).getObj();
    }

    /**
     * Private Constructor. So it can only be created with the static show()
     * method.<br>
     *
     * @param parent
     */
    private DetilKurikulumExtendedSearchListBox(Component parent) {
        super();

        setParent(parent);

        createBox(null);
    }

    private DetilKurikulumExtendedSearchListBox(Component parent, Tirspasca irs) {
        super();

        setParent(parent);

        createBox(irs);
    }

    /**
     * Creates the components, sets the model and show the window as modal.<br>
     */
    @SuppressWarnings("unchecked")
    private void createBox(Tirspasca irs) {

        // Window
        this.setWidth(String.valueOf(this._width) + "px");
        this.setHeight(String.valueOf(this._height) + "px");
        this.setTitle(this._title);
        this.setVisible(true);
        this.setClosable(true);

        // Borderlayout
        Borderlayout bl = new Borderlayout();
        bl.setHeight("100%");
        bl.setWidth("100%");
        bl.setParent(this);

        Center center = new Center();
        center.setBorder("none");
        center.setFlex(true);
        center.setParent(bl);

        // Borderlayout
        Borderlayout bl2 = new Borderlayout();
        bl2.setHeight("100%");
        bl2.setWidth("20%");
        bl2.setParent(center);

        North north2 = new North();
        north2.setBorder("none");
        north2.setHeight("26px");
        north2.setParent(bl2);
        // Paging
        this._paging = new Paging();
        this._paging.setDetailed(true);
        this._paging.addEventListener("onPaging", new OnPagingEventListener());
        this._paging.setPageSize(getPageSize());
        this._paging.setParent(north2);

        Center center2 = new Center();
        center2.setBorder("none");
        center2.setFlex(true);
        center2.setParent(bl2);
        // Div Center area
        Div divCenter2 = new Div();
        divCenter2.setWidth("20%");
        divCenter2.setHeight("100%");
        divCenter2.setParent(center2);

        // Listbox
        this.listbox = new Listbox();
        this.listbox.setHeight("290px");
        this.listbox.setVisible(true);
        this.listbox.setParent(divCenter2);
        this.listbox.setItemRenderer(new SearchBoxItemRenderer());

        Listhead listhead = new Listhead();
        listhead.setParent(this.listbox);
        Listheader listheader = new Listheader();
        listheader.setSclass("FDListBoxHeader1");
        listheader.setParent(listhead);
        listheader.setLabel(this._listHeader1);

        South south2 = new South();
        south2.setBorder("none");
        south2.setHeight("26px");
        south2.setParent(bl2);
        // hbox for holding the Textbox + SearchButton
        Hbox hbox = new Hbox();
        hbox.setPack("stretch");
        hbox.setStyle("padding-left: 5px");
        hbox.setWidth("100%");
        hbox.setHeight("27px");
        hbox.setParent(south2);
        // textbox for inserting the search parameter
        this._textbox = new Textbox();
        this._textbox.setWidth("100%");
        this._textbox.setMaxlength(20);
        this._textbox.setParent(hbox);
        // serachButton
        this._searchButton = new Button();
        this._searchButton.setImage("/images/icons/search.gif");
        this._searchButton.addEventListener("onClick", new OnSearchListener());
        this._searchButton.setParent(hbox);

        South south = new South();
        south.setBorder("none");
        south.setHeight("30px");
        south.setParent(bl);

        Div divSouth = new Div();
        divSouth.setWidth("100%");
        divSouth.setHeight("100%");
        divSouth.setParent(south);

        Separator sep = new Separator();
        sep.setBar(true);
        sep.setOrient("horizontal");
        sep.setParent(divSouth);

        // Button
        Button btnOK = new Button();
        btnOK.setStyle("padding-left: 5px");
        btnOK.setLabel("OK");
        btnOK.addEventListener("onClick", new OnCloseListener());
        btnOK.setParent(divSouth);

        /**
         * init the model.<br>
         * The ResultObject is a helper class that holds the generic list and
         * the totalRecord count as int value.
         */
        ResultObject ro;
        List<Mdetilkurikulum> resultList;
        if(irs != null) {
            ro = getObjService().getAllByIrs("", irs, 0, getPageSize());
            resultList = (List<Mdetilkurikulum>) ro.getList();
            this._paging.setTotalSize(ro.getTotalCount());
        } else {
            ro = getObjService().getAllLikeText("", 0, getPageSize());
            resultList = (List<Mdetilkurikulum>) ro.getList();
            this._paging.setTotalSize(ro.getTotalCount());
        }
        // set the model
        setListModelList(new ListModelList(resultList));
        this.listbox.setModel(getListModelList());

        try {
            doModal();
        } catch (final SuspendNotAllowedException e) {
            logger.fatal("", e);
            this.detach();
        } catch (final InterruptedException e) {
            logger.fatal("", e);
            this.detach();
        }
    }

    /**
     * Inner ListItemRenderer class.<br>
     */
    final class SearchBoxItemRenderer implements ListitemRenderer {

        @Override
        public void render(Listitem item, Object data) throws Exception {

            Mdetilkurikulum obj = (Mdetilkurikulum) data;
            Listcell lc = new Listcell(obj.getCcohort());
            lc.setParent(item);
            Listcell lc1 = new Listcell("");
            lc1.setParent(item);
            Listcell lc15 = new Listcell(obj.getMprodi().getCnmprogst());
            lc15.setParent(item);
            Listcell lc2 = new Listcell(obj.getMtbmtkl().getCnamamk());
            lc2.setParent(item);
            Listcell lc3 = new Listcell(obj.getCmun());
            lc3.setParent(item);
            String sks = "";
            if(obj.getMtbmtkl().getNsks() != null) {
                sks = obj.getMtbmtkl().getNsks().toString();
            }
            Listcell lc4 = new Listcell(sks);
            lc4.setParent(item);

            item.setAttribute("data", data);
            ComponentsCtrl.applyForward(item, "onDoubleClick=onDoubleClicked");
        }
    }

    /**
     * If a DoubleClick appears on a listItem. <br>
     * This method is forwarded in the renderer.<br>
     *
     * @param event
     */
    public void onDoubleClicked(Event event) {

        if (this.listbox.getSelectedItem() != null) {
            Listitem li = this.listbox.getSelectedItem();
            Mdetilkurikulum obj = (Mdetilkurikulum) li.getAttribute("data");

            setObj(obj);
            this.onClose();
        }
    }

    /**
     * "onPaging" EventListener for the paging component. <br>
     * <br>
     * Calculates the next page by currentPage and pageSize values. <br>
     * Calls the method for refreshing the data with the new rowStart and
     * pageSize. <br>
     */
    public final class OnPagingEventListener implements EventListener {
        @Override
        public void onEvent(Event event) throws Exception {

            PagingEvent pe = (PagingEvent) event;
            int pageNo = pe.getActivePage();
            int start = pageNo * getPageSize();

            String searchText = DetilKurikulumExtendedSearchListBox.this._textbox.getValue();
            // refresh the list
            refreshModel(searchText, start);
        }
    }

    /**
     * Refreshes the list by calling the DAO methode with the modified search
     * object. <br>
     *
     * @param searchText SearchObject, holds the entity and properties to search. <br>
     * @param start      Row to start. <br>
     */
    @SuppressWarnings("unchecked")
    void refreshModel(String searchText, int start) {

        // clear old data
        getListModelList().clear();

        // init the model
        ResultObject ro = getObjService().getAllLikeMatakuliah(searchText, start, getPageSize());
        List<Mdetilkurikulum> resultList = (List<Mdetilkurikulum>) ro.getList();
        this._paging.setTotalSize(ro.getTotalCount());

        // set the model
        setListModelList(new ListModelList(resultList));
        this.listbox.setModel(getListModelList());
    }

    /**
     * Inner OnSearchListener class.<br>
     */
    final class OnSearchListener implements EventListener {
        @Override
        public void onEvent(Event event) throws Exception {

            String searchText = DetilKurikulumExtendedSearchListBox.this._textbox.getValue();

            // we start new
            refreshModel(searchText, 0);
        }
    }

    /**
     * Inner OnCloseListener class.<br>
     */
    final class OnCloseListener implements EventListener {
        @Override
        public void onEvent(Event event) throws Exception {

            if (DetilKurikulumExtendedSearchListBox.this.listbox.getSelectedItem() != null) {
                Listitem li = DetilKurikulumExtendedSearchListBox.this.listbox.getSelectedItem();
                Mdetilkurikulum obj = (Mdetilkurikulum) li.getAttribute("data");

                setObj(obj);
            }
            onClose();
        }
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public Mdetilkurikulum getObj() {
        return obj;
    }

    public void setObj(Mdetilkurikulum obj) {
        this.obj = obj;
    }

    public DetilKurikulumService getObjService() {
        if (this.objService == null) {
            this.objService = (DetilKurikulumService) SpringUtil.getBean("detilKurikulumService");
        }
        return objService;
    }

    public void setObjService(DetilKurikulumService objService) {
        this.objService = objService;
    }


    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setListModelList(ListModelList listModelList) {
        this.listModelList = listModelList;
    }

    public ListModelList getListModelList() {
        return this.listModelList;
    }
}
