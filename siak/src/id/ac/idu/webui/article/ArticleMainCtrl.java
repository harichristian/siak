package id.ac.idu.webui.article;

import com.trg.search.Filter;
import id.ac.idu.UserWorkspace;
import id.ac.idu.backend.model.Article;
import id.ac.idu.backend.service.ArticleService;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.backend.util.ZksampleBeanUtils;
import id.ac.idu.webui.article.report.ArticleSimpleDJReport;
import id.ac.idu.webui.util.*;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.*;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * Main controller for the article main module.<br>
 * <br>
 * zul-file: /WEB-INF/pages/article/articleMain.zul.<br>
 * <br>
 * This class creates the Tabs + TabPanels. The components/data to all tabs are
 * created on demand on first time selecting the tab.<br>
 * This controller holds all getters/setters for the used databinding beans/sets
 * in all related tabs. In the child tabs controllers their databinding
 * setters/getters pointed to this mainTabController.<br>
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * <br>
 *
 * @author bbruhns
 * @author sgerth
 * @changes 07/03/2010: sge modified for zk5.x with complete Annotated
 * Databinding.<br>
 * <p/>
 * Managed tabs:<br>
 * - ArticleListCtrl = Article List / ArticleListe<br>
 * - ArticleDetailCtrl = Article Details / ArticleDetails<br>
 * Dialog window:<br>
 * - ArticleDialogCtrl = Article Details as a Dialog / ArticleDetails
 * als Dialog<br>
 */
public class ArticleMainCtrl extends GFCBaseCtrl implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ArticleMainCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowArticleMain; // autowired

    // Tabs
    protected Tabbox tabbox_ArticleMain; // autowired
    protected Tab tabArticleList; // autowired
    protected Tab tabArticleDetail; // autowired
    protected Tabpanel tabPanelArticleList; // autowired
    protected Tabpanel tabPanelArticleDetail; // autowired

    // filter components
    protected Checkbox checkbox_ArticleList_ShowAll; // autowired
    protected Textbox tb_Article_ArticleID; // aurowired
    protected Textbox tb_Article_Name; // aurowired

    // checkRights
    protected Button button_ArticleList_PrintList;
    protected Button button_ArticleList_SearchArticleID;
    protected Button button_ArticleList_SearchName;

    // Button controller for the CRUD buttons
    private final String btnCtroller_ClassPrefix = "button_ArticlesDialog_";
    private ButtonStatusCtrl btnCtrlArticle;
    protected Button btnNew; // autowired
    protected Button btnEdit; // autowired
    protected Button btnDelete; // autowired
    protected Button btnSave; // autowired
    protected Button btnCancel; // autowired

    protected Button btnHelp;

    // Tab-Controllers for getting access to their components/dataBinders
    private ArticleListCtrl articleListCtrl;
    private ArticleDetailCtrl articleDetailCtrl;
    // The same Detail as a modal Dialog

    // Databinding
    // private Article article;
    private Article selectedArticle;
    private BindingListModelList articles;

    // ServiceDAOs / Domain Classes
    private ArticleService articleService;

    // always a copy from the bean before modifying. Used for reseting
    private Article originalArticle;

    /**
     * default constructor.<br>
     */
    public ArticleMainCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);

        /**
         * 1. Set an 'alias' for this composer name to access it in the
         * zul-file.<br>
         * 2. Set the parameter 'recurse' to 'false' to avoid problems with
         * managing more than one zul-file in one page. Otherwise it would be
         * overridden and can ends in curious error messages.
         */
        this.self.setAttribute("controller", this, false);
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // +++++++++++++++ Component Events ++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    /**
     * Automatically called method from zk.
     *
     * @param event
     * @throws Exception
     */
    public void onCreate$windowArticleMain(Event event) throws Exception {

        // create the Button Controller. Disable not used buttons during working
        btnCtrlArticle = new ButtonStatusCtrl(getUserWorkspace(), btnCtroller_ClassPrefix, btnNew, btnEdit, btnDelete, btnSave, btnCancel);

        doCheckRights();

        /**
         * Initiate the first loading by selecting the customerList tab and
         * create the components from the zul-file.
         */
        tabArticleList.setSelected(true);
        if (tabPanelArticleList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelArticleList, this, "ModuleMainController", "/WEB-INF/pages/article/articleList.zul");
        }

        // Set the buttons for editMode
        btnCtrlArticle.setInitEdit();
    }

    /**
     * When the tab 'tabArticleList' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws IOException
     */
    public void onSelect$tabArticleList(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelArticleList.getFirstChild() != null) {
            tabArticleList.setSelected(true);
            return;
        }

        if (tabPanelArticleList != null) {
            ZksampleCommonUtils.createTabPanelContent(tabPanelArticleList, this, "ModuleMainController", "/WEB-INF/pages/article/articleList.zul");
        }
    }

    /**
     * When the tab 'tabPanelArticleDetail' is selected.<br>
     * Loads the zul-file into the tab.
     *
     * @param event
     * @throws IOException
     */
    public void onSelect$tabArticleDetail(Event event) throws IOException {
        // logger.debug(event.toString());

        // Check if the tabpanel is already loaded
        if (tabPanelArticleDetail.getFirstChild() != null) {
            tabArticleDetail.setSelected(true);

            // refresh the Binding mechanism
            getArticleDetailCtrl().setArticle(getSelectedArticle());
            getArticleDetailCtrl().getBinder().loadAll();
            return;
        }

        if (this.tabPanelArticleDetail != null) {
            ZksampleCommonUtils.createTabPanelContent(this.tabPanelArticleDetail, this, "ModuleMainController", "/WEB-INF/pages/article/articleDetail.zul");
        }
    }

    /**
     * when the "print articles list" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$button_ArticleList_PrintList(Event event) throws InterruptedException {
        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        new ArticleSimpleDJReport(win);
    }

    /**
     * when the checkBox 'Show All' for filtering is checked. <br>
     *
     * @param event
     */
    public void onCheck$checkbox_ArticleList_ShowAll(Event event) {
        doArticleListShowAll(event);
    }

    /**
     * When the "search for article No" button is clicked.
     *
     * @param event
     * @throws Exception
     */
    public void onClick$button_ArticleList_SearchArticleID(Event event) throws Exception {
        doArticleListSearchLikeArticleNo(event);
    }

    /**
     * When the "search for article No" button is clicked.
     *
     * @param event
     * @throws Exception
     */
    public void onClick$button_ArticleList_SearchName(Event event) throws Exception {
        doArticleListSearchLikeArticleName(event);
    }

    /**
     * When the "help" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$btnHelp(Event event) throws InterruptedException {
        doHelp(event);
    }

    /**
     * When the "new" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$btnNew(Event event) throws InterruptedException {
        doNew(event);
    }

    /**
     * When the "save" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$btnSave(Event event) throws InterruptedException {
        doSave(event);
    }

    /**
     * When the "cancel" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$btnEdit(Event event) throws InterruptedException {
        doEdit(event);
    }

    /**
     * When the "delete" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$btnDelete(Event event) throws InterruptedException {
        doDelete(event);
    }

    /**
     * When the "cancel" button is clicked.
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$btnCancel(Event event) throws InterruptedException {
        doCancel(event);
    }

    /**
     * when the "refresh" button is clicked. <br>
     * <br>
     *
     * @param event
     * @throws InterruptedException
     */
    public void onClick$btnRefresh(Event event) throws InterruptedException {
        doResizeSelectedTab(event);
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // +++++++++++++++++ Business Logic ++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    /**
     * Removes all Filters from the branch list and shows all. <br>
     *
     * @param event
     */
    private void doArticleListShowAll(Event event) {
        // logger.debug(event.toString());

        // empty the text search boxes
        tb_Article_ArticleID.setValue(""); // clear

        if (getArticleListCtrl().getBinder() != null) {
            getArticleListCtrl().getPagedBindingListWrapper().clearFilters();

            // get the current Tab for later checking if we must change it
            final Tab currentTab = tabbox_ArticleMain.getSelectedTab();

            // check if the tab is one of the Detail tabs. If so do not
            // change the selection of it
            if (!currentTab.equals(tabArticleList)) {
                tabArticleList.setSelected(true);
            } else {
                currentTab.setSelected(true);
            }
        }
    }

    /**
     * Filter the branch list with 'like article no'. <br>
     */
    private void doArticleListSearchLikeArticleNo(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!tb_Article_ArticleID.getValue().isEmpty()) {
            checkbox_ArticleList_ShowAll.setChecked(false); // unCheck
            tb_Article_Name.setValue("");

            if (getArticleListCtrl().getBinder() != null) {

                // ++ create a searchObject and init sorting ++//
                final HibernateSearchObject<Article> so = new HibernateSearchObject<Article>(Article.class, getArticleListCtrl().getCountRows());
                so.addFilter(new Filter("artNr", "%" + tb_Article_ArticleID.getValue() + "%", Filter.OP_ILIKE));
                so.addSort("artNr", false);

                // Change the BindingListModel.
                getArticleListCtrl().getPagedBindingListWrapper().setSearchObject(so);

                // get the current Tab for later checking if we must change it
                final Tab currentTab = this.tabbox_ArticleMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(this.tabArticleList)) {
                    tabArticleList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * Filter the branch list with 'like article name'. <br>
     */
    private void doArticleListSearchLikeArticleName(Event event) throws Exception {
        // logger.debug(event.toString());

        // if not empty
        if (!tb_Article_Name.getValue().isEmpty()) {
            checkbox_ArticleList_ShowAll.setChecked(false); // unCheck
            tb_Article_ArticleID.setValue("");

            if (getArticleListCtrl().getBinder() != null) {

                // ++ create a searchObject and init sorting ++//
                final HibernateSearchObject<Article> so = new HibernateSearchObject<Article>(Article.class, getArticleListCtrl().getCountRows());
                so.addFilter(new Filter("artKurzbezeichnung", "%" + tb_Article_Name.getValue() + "%", Filter.OP_ILIKE));
                so.addSort("artKurzbezeichnung", false);

                // Change the BindingListModel.
                getArticleListCtrl().getPagedBindingListWrapper().setSearchObject(so);

                // get the current Tab for later checking if we must change it
                final Tab currentTab = this.tabbox_ArticleMain.getSelectedTab();

                // check if the tab is one of the Detail tabs. If so do not
                // change the selection of it
                if (!currentTab.equals(this.tabArticleList)) {
                    tabArticleList.setSelected(true);
                } else {
                    currentTab.setSelected(true);
                }
            }
        }
    }

    /**
     * 1. Cancel the current action.<br>
     * 2. Reset the values to its origin.<br>
     * 3. Set UI components back to readonly/disable mode.<br>
     * 4. Set the buttons in edit mode.<br>
     *
     * @param event
     * @throws InterruptedException
     */
    private void doCancel(Event event) throws InterruptedException {
        // logger.debug(event.toString());

        // reset to the original object
        doResetToInitValues();

        // check first, if the tabs are created
        if (getArticleDetailCtrl().getBinder() != null) {

            // refresh all dataBinder related controllers/components
            getArticleDetailCtrl().getBinder().loadAll();

            // set editable Mode
            getArticleDetailCtrl().doReadOnlyMode(true);

            btnCtrlArticle.setInitEdit();
        }
    }

    /**
     * Sets all UI-components to writable-mode. Sets the buttons to edit-Mode.
     * Checks first, if the NEEDED TABS with its contents are created. If not,
     * than create it by simulate an onSelect() with calling Events.sendEvent()
     *
     * @param event
     * @throws InterruptedException
     */
    private void doEdit(Event event) {
        // logger.debug(event.toString());

        // get the current Tab for later checking if we must change it
        final Tab currentTab = this.tabbox_ArticleMain.getSelectedTab();

        // check first, if the tabs are created, if not than create it
        if (getArticleDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabArticleDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getArticleDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabArticleDetail, null));
        }

        // check if the tab is one of the Detail tabs. If so do not change the
        // selection of it
        if (!currentTab.equals(this.tabArticleDetail)) {
            tabArticleDetail.setSelected(true);
        } else {
            currentTab.setSelected(true);
        }

        getArticleDetailCtrl().getBinder().loadAll();

        // remember the old vars
        doStoreInitValues();

        btnCtrlArticle.setBtnStatus_Edit();

        getArticleDetailCtrl().doReadOnlyMode(false);
        // set focus
        getArticleDetailCtrl().txtb_artNr.focus();
    }

    /**
     * Deletes the selected Bean from the DB.
     *
     * @param event
     * @throws InterruptedException
     * @throws InterruptedException
     */
    private void doDelete(Event event) throws InterruptedException {
        // logger.debug(event.toString());

        // check first, if the tabs are created, if not than create them
        if (getArticleDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabArticleDetail, null));
        }

        // check first, if the tabs are created
        if (getArticleDetailCtrl().getBinder() == null) {
            return;
        }

        final Article anArticle = getSelectedArticle();
        if (anArticle != null) {

            // Show a confirm box
            final String msg = Labels.getLabel("message.Question.Are_you_sure_to_delete_this_record") + "\n\n --> " + anArticle.getArtKurzbezeichnung();
            final String title = Labels.getLabel("message.Deleting.Record");

            MultiLineMessageBox.doSetTemplate();
            if (MultiLineMessageBox.show(msg, title, Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, true, new EventListener() {
                @Override
                public void onEvent(Event evt) {
                    switch (((Integer) evt.getData()).intValue()) {
                        case MultiLineMessageBox.YES:
                            try {
                                deleteBean();
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            break; //
                        case MultiLineMessageBox.NO:
                            break; //
                    }
                }

                // delete from database
                private void deleteBean() throws InterruptedException {
                    try {
                        getArticleService().delete(anArticle);
                    } catch (DataAccessException e) {
                        ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());
                    }
                }

            }

            ) == MultiLineMessageBox.YES) {
            }

        }

        btnCtrlArticle.setInitEdit();

        setSelectedArticle(null);
        // refresh the list
        getArticleListCtrl().doFillListbox();

        // refresh all dataBinder related controllers
        getArticleDetailCtrl().getBinder().loadAll();
        // the listController only because we have a textbox in it.
        getArticleListCtrl().getBinder().loadAll();
    }

    /**
     * Saves all involved Beans to the DB.
     *
     * @param event
     * @throws InterruptedException
     */
    private void doSave(Event event) throws InterruptedException {
        // logger.debug(event.toString());

        // save all components data in the several tabs to the bean
        getArticleDetailCtrl().getBinder().saveAll();

        try {
            // save it to database
            getArticleService().saveOrUpdate(getArticleDetailCtrl().getArticle());
            // if saving is successfully than actualize the beans as
            // origins.
            doStoreInitValues();
            // refresh the list
            getArticleListCtrl().doFillListbox();
            // later refresh StatusBar
            Events.postEvent("onSelect", getArticleListCtrl().getListBoxArticle(), getSelectedArticle());

            // show the objects data in the statusBar
            final String str = getSelectedArticle().getArtKurzbezeichnung();
            EventQueues.lookup("selectedObjectEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeSelectedObject", null, str));

        } catch (DataAccessException e) {
            ZksampleMessageUtils.showErrorMessage(e.getMostSpecificCause().toString());

            // Reset to init values
            doResetToInitValues();

            return;

        } finally {
            btnCtrlArticle.setInitEdit();
            getArticleDetailCtrl().doReadOnlyMode(true);
        }
    }

    /**
     * Sets all UI-components to writable-mode. Stores the current Beans as
     * originBeans and get new Objects from the backend.
     *
     * @param event
     * @throws InterruptedException
     */
    private void doNew(Event event) {
        // logger.debug(event.toString());

        // check first, if the tabs are created
        if (getArticleDetailCtrl() == null) {
            Events.sendEvent(new Event("onSelect", tabArticleDetail, null));
            // if we work with spring beanCreation than we must check a little
            // bit deeper, because the Controller are preCreated ?
        } else if (getArticleDetailCtrl().getBinder() == null) {
            Events.sendEvent(new Event("onSelect", tabArticleDetail, null));
        }

        // remember the current object
        doStoreInitValues();

        /** !!! DO NOT BREAK THE TIERS !!! */
        // We don't create a new DomainObject() in the frontend.
        // We GET it from the backend.
        final Article anArticle = getArticleService().getNewArticle();

        // set the beans in the related databinded controllers
        getArticleDetailCtrl().setArticle(anArticle);
        getArticleDetailCtrl().setSelectedArticle(anArticle);

        // Refresh the binding mechanism
        getArticleDetailCtrl().setSelectedArticle(getSelectedArticle());
        getArticleDetailCtrl().getBinder().loadAll();

        // set editable Mode
        getArticleDetailCtrl().doReadOnlyMode(false);

        // set the ButtonStatus to New-Mode
        btnCtrlArticle.setInitNew();

        tabArticleDetail.setSelected(true);
        // set focus
        getArticleDetailCtrl().txtb_artNr.focus();

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++++++ Helpers ++++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    /**
     * Resizes the container from the selected Tab.
     *
     * @param event
     */
    private void doResizeSelectedTab(Event event) {
        // logger.debug(event.toString());

        if (tabbox_ArticleMain.getSelectedTab() == tabArticleDetail) {
            getArticleDetailCtrl().doFitSize(event);
        } else if (tabbox_ArticleMain.getSelectedTab() == tabArticleList) {
            // resize and fill Listbox new
            getArticleListCtrl().doFillListbox();
        }
    }

    /**
     * Opens the help screen for the current module.
     *
     * @param event
     * @throws InterruptedException
     */
    private void doHelp(Event event) throws InterruptedException {

        ZksampleMessageUtils.doShowNotImplementedMessage();

        // we stop the propagation of the event, because zk will call ALL events
        // with the same name in the namespace and 'btnHelp' is a standard
        // button in this application and can often appears.
        // Events.getRealOrigin((ForwardEvent) event).stopPropagation();
        event.stopPropagation();
    }

    /**
     * Saves the selected object's current properties. We can get them back if a
     * modification is canceled.
     *
     * @see doResetToInitValues()
     */
    public void doStoreInitValues() {

        if (getSelectedArticle() != null) {

            try {
                setOriginalArticle((Article) ZksampleBeanUtils.cloneBean(getSelectedArticle()));
            } catch (final IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (final InstantiationException e) {
                throw new RuntimeException(e);
            } catch (final InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (final NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Reset the selected object to its origin property values.
     *
     * @see doStoreInitValues()
     */
    public void doResetToInitValues() {

        if (getOriginalArticle() != null) {

            try {
                setSelectedArticle((Article) ZksampleBeanUtils.cloneBean(getOriginalArticle()));
                // TODO Bug in DataBinder??
                windowArticleMain.invalidate();

            } catch (final IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (final InstantiationException e) {
                throw new RuntimeException(e);
            } catch (final InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (final NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * User rights check. <br>
     * Only components are set visible=true if the logged-in <br>
     * user have the right for it. <br>
     * <p/>
     * The rights are get from the spring framework users grantedAuthority(). A
     * right is only a string. <br>
     */
    // TODO move it to the zul-file
    private void doCheckRights() {

        final UserWorkspace workspace = getUserWorkspace();

        button_ArticleList_PrintList.setVisible(workspace.isAllowed("button_BranchMain_PrintBranches"));
        button_ArticleList_SearchArticleID.setVisible(workspace.isAllowed("button_ArticleList_SearchArticleID"));
        button_ArticleList_SearchName.setVisible(workspace.isAllowed("button_ArticleList_SearchName"));

    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    // public void setArticle(Article article) {
    // this.article = article;
    // }
    //
    // public Article getArticle() {
    // return this.article;
    // }

    public void setOriginalArticle(Article originalArticle) {
        this.originalArticle = originalArticle;
    }

    public Article getOriginalArticle() {
        return this.originalArticle;
    }

    public void setSelectedArticle(Article selectedArticle) {
        this.selectedArticle = selectedArticle;
    }

    public Article getSelectedArticle() {
        return this.selectedArticle;
    }

    public void setArticles(BindingListModelList articles) {
        this.articles = articles;
    }

    public BindingListModelList getArticles() {
        return this.articles;
    }

    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    public ArticleService getArticleService() {
        return this.articleService;
    }

    public void setArticleListCtrl(ArticleListCtrl articleListCtrl) {
        this.articleListCtrl = articleListCtrl;
    }

    public ArticleListCtrl getArticleListCtrl() {
        return this.articleListCtrl;
    }

    public void setArticleDetailCtrl(ArticleDetailCtrl articleDetailCtrl) {
        this.articleDetailCtrl = articleDetailCtrl;
    }

    public ArticleDetailCtrl getArticleDetailCtrl() {
        return this.articleDetailCtrl;
    }

}
