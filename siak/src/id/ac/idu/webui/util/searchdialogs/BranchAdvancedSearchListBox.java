/**
 * Copyright 2010 the original author or authors.
 *
 * This file is part of Zksample2. http://zksample2.sourceforge.net/
 *
 * Zksample2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Zksample2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Zksample2.  If not, see <http://www.gnu.org/licenses/gpl.html>.
 */
package id.ac.idu.webui.util.searchdialogs;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Branche;
import id.ac.idu.backend.service.BrancheService;
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
 * This class creates a modal window as a dialog in which the user <br>
 * can search and select a branch object. By onClosing this box <b>returns</b>
 * an object or null. <br>
 * The object can returned by selecting and clicking the OK button or by
 * DoubleClicking on an item from the list.<br>
 * <p/>
 * <br>
 * This is a basic skeleton which is extended for database paging.<br>
 * <br>
 * <p/>
 * <pre>
 * call: Branch branch = BranchAdvancedSearchListBox.show(parentComponent);
 * </pre>
 *
 * @author bbruhns
 * @author sgerth
 */
public class BranchAdvancedSearchListBox extends Window implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(BranchAdvancedSearchListBox.class);

    // the paging component
    private Paging _paging;

    // pageSize
    private int pageSize = 19;

    // the data listbox
    private Listbox listbox;

    // the model for the listbox
    private ListModelList listModelList;

    // the windows title
    private final String _title = Labels.getLabel("message.Information.AdvancedSearch") + " / " + Labels.getLabel("common.Branch");

    // 1. Listheader
    private final String _listHeader1 = Labels.getLabel("common.Description");

    // the windows height
    private final int _height = 400;

    // the windows width
    private final int _width = 300;

    // the returned bean object
    private Branche branche = null;

    // The service from which we get the data
    private BrancheService brancheService;

    /**
     * The Call method.
     *
     * @param parent The parent component
     * @return a BeanObject from the listBox or null.
     */
    public static Branche show(Component parent) {
        return new BranchAdvancedSearchListBox(parent).getBranche();
    }

    /**
     * Private Constructor. So it can only be created with the static show()
     * method.<br>
     *
     * @param parent
     */
    private BranchAdvancedSearchListBox(Component parent) {
        super();

        setParent(parent);

        createBox();
    }

    /**
     * Creates the components, sets the model and show the window as modal.<br>
     */
    @SuppressWarnings("unchecked")
    private void createBox() {

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
        center.setFlex(true);
        center.setParent(bl);

        South south = new South();
        south.setHeight("26px");
        south.setParent(bl);

        // OK Button
        Button btnOK = new Button();
        btnOK.setLabel("OK");
        btnOK.addEventListener("onClick", new OnCloseListener());
        btnOK.setParent(south);

        Div divCenter = new Div();
        divCenter.setWidth("100%");
        divCenter.setHeight("100%");
        divCenter.setParent(center);

        // Paging
        this._paging = new Paging();
        this._paging.setDetailed(true);
        this._paging.addEventListener("onPaging", new OnPagingEventListener());
        this._paging.setPageSize(getPageSize());
        this._paging.setParent(divCenter);

        // Listbox
        this.listbox = new Listbox();
        this.listbox.setStyle("border: none;");
        this.listbox.setHeight("100%");
        this.listbox.setVisible(true);
        this.listbox.setParent(divCenter);
        this.listbox.setItemRenderer(new SearchBoxItemRenderer());

        Listhead listhead = new Listhead();
        listhead.setParent(this.listbox);
        Listheader listheader = new Listheader();
        listheader.setSclass("FDListBoxHeader1");
        listheader.setParent(listhead);
        listheader.setLabel(this._listHeader1);

        /**
         * init the model.<br>
         * The ResultObject is a helper class that holds the generic list and
         * the totalRecord count as int value.
         */
        ResultObject ro = getBrancheService().getAllBranches(0, getPageSize());
        List<Branche> resultList = (List<Branche>) ro.getList();
        this._paging.setTotalSize(ro.getTotalCount());

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

            Branche branche = (Branche) data;

            Listcell lc = new Listcell(branche.getBraBezeichnung());
            lc.setParent(item);

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
            Branche branche = (Branche) li.getAttribute("data");

            setBranche(branche);
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

            // refresh the list
            refreshModel(start);
        }
    }

    /**
     * Refreshes the list by calling the DAO methode with the modified search
     * object. <br>
     *
     * @param so    SearchObject, holds the entity and properties to search. <br>
     * @param start Row to start. <br>
     */
    @SuppressWarnings("unchecked")
    void refreshModel(int start) {

        // clear old data
        getListModelList().clear();

        // init the model
        ResultObject ro = getBrancheService().getAllBranches(start, getPageSize());
        List<Branche> resultList = (List<Branche>) ro.getList();
        this._paging.setTotalSize(ro.getTotalCount());

        // set the model
        setListModelList(new ListModelList(resultList));
        this.listbox.setModel(getListModelList());
    }

    /**
     * Inner OnCloseListener class.<br>
     */
    final class OnCloseListener implements EventListener {
        @Override
        public void onEvent(Event event) throws Exception {

            if (BranchAdvancedSearchListBox.this.listbox.getSelectedItem() != null) {
                Listitem li = BranchAdvancedSearchListBox.this.listbox.getSelectedItem();
                Branche branche = (Branche) li.getAttribute("data");

                setBranche(branche);
            }
            onClose();
        }
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    public Branche getBranche() {
        return this.branche;
    }

    private void setBranche(Branche branche) {
        this.branche = branche;
    }

    public BrancheService getBrancheService() {
        if (this.brancheService == null) {
            this.brancheService = (BrancheService) SpringUtil.getBean("brancheService");
        }
        return this.brancheService;
    }

    public void setBrancheService(BrancheService brancheService) {
        this.brancheService = brancheService;
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
