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
package id.ac.idu.common.menu;

import id.ac.idu.common.menu.dropdown.ZkossDropDownMenuFactory;
import id.ac.idu.common.menu.tree.ZkossTreeMenuFactory;
import id.ac.idu.webui.index.IndexCtrl;
import id.ac.idu.webui.util.WindowBaseCtrl;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 * Main menu controller. <br>
 * <br>
 * Added the buttons for expanding/closing the menu tree. Calls the menu
 * factory.
 *
 * @author bbruhns
 * @author sgerth
 */
public class MainMenuCtrl extends WindowBaseCtrl implements Serializable {

    private static final long serialVersionUID = -909795057747345551L;
    private static final Logger logger = Logger.getLogger(MainMenuCtrl.class);
    private Window mainMenuWindow; // autowire the IDSpace
    private Tree mainMenuTree;
    private static String bgColor = "D6DCDE";
    private static String bgColorInner = "white";
    private IndexCtrl indexCtrl;

    public void onCreate$mainMenuWindow(Event event) throws Exception {
        doOnCreateCommon(this.mainMenuWindow, event); // do the autowire stuff
        if (args.containsKey("indexController")) {
            setIndexCtrl((IndexCtrl) args.get("indexController"));
            getIndexCtrl().setMainMenuCtrl(this); // SET THIS CONTROLLER TO THE module's Parent/MainController
        }
        createMenu();
    }

    private void createMenu() throws InterruptedException {
        Toolbarbutton toolbarbutton;

        final Groupbox gb = (Groupbox) getMainMenuWindow().getFellowIfAny("groupbox_menu");
        final Hbox hbox = new Hbox();

        hbox.setStyle("backgound-color: " + bgColorInner);
        hbox.setParent(gb);

        // ToolbarButton for expanding the menutree
        toolbarbutton = new Toolbarbutton();
        hbox.appendChild(toolbarbutton);
        toolbarbutton.setId("btnMainMenuExpandAll");
        toolbarbutton.setImage("/images/icons/folder_open_16x16.gif");
        toolbarbutton.setTooltiptext(Labels.getLabel("btnFolderExpand.tooltiptext"));
        toolbarbutton.addEventListener("onClick", new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                onClick$btnMainMenuExpandAll(event);
            }
        });
        toolbarbutton = new Toolbarbutton();
        hbox.appendChild(toolbarbutton);
        toolbarbutton.setId("btnMainMenuCollapseAll");
        toolbarbutton.setImage("/images/icons/folder_closed2_16x16.gif");
        toolbarbutton.setTooltiptext(Labels.getLabel("btnFolderCollapse.tooltiptext"));
        toolbarbutton.addEventListener("onClick", new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                onClick$btnMainMenuCollapseAll(event);
            }
        });

        Separator separator = createSeparator(false);
        separator.setWidth("97%");
        separator.setStyle("background-color: " + bgColorInner);
        separator.setBar(false);
        separator.setParent(gb);

        separator = createSeparator(false);
        separator.setWidth("97%");
        separator.setBar(true);
        separator.setParent(gb);

        mainMenuTree = new Tree();
        mainMenuTree.setStyle("overflow:auto; border: none;");
        mainMenuTree.setParent(gb);

        final Treechildren treechildren = new Treechildren();
        mainMenuTree.appendChild(treechildren);
        ZkossTreeMenuFactory.addMainMenu(treechildren);

        final Separator sep1 = new Separator();
        sep1.setWidth("97%");
        sep1.setBar(false);
        sep1.setParent(gb);

        /* as standard, call the dashboard page */
        showPage("/WEB-INF/pages/dashboard.zul", "menu_Item_Home", Labels.getLabel("menu_Item_Home"));
    }

    private static Separator createSeparator(boolean withBar) {

        final Separator sep = new Separator();
        sep.setStyle("backgound-color: " + bgColorInner);
        sep.setBar(withBar);

        return sep;
    }

    private void showPage(String zulFilePathName, String tabID, String tabLabel) throws InterruptedException {

        try {
            final int workWithTabs = 1;
            if (workWithTabs == 1) {
                final Borderlayout bl = (Borderlayout) Path.getComponent("/outerIndexWindow/borderlayoutMain");
                final Center center = bl.getCenter();
                final Tabs tabs = (Tabs) center.getFellow("divCenter").getFellow("tabBoxIndexCenter").getFellow("tabsIndexCenter");

                Tab checkTab = null;
                try {
                    checkTab = (Tab) tabs.getFellow("tab_" + tabID.trim());
                    checkTab.setSelected(true);
                } catch (final ComponentNotFoundException ex) {
                    // Ignore if can not get tab.
                }

                if (checkTab == null) {
                    final Tab tab = new Tab();
                    tab.setId("tab_" + tabID.trim());
                    if (tabLabel != null) {
                        tab.setLabel(tabLabel.trim());
                    } else {
                        tab.setLabel(tabLabel.trim());
                    }
                    tab.setClosable(true);
                    tab.setParent(tabs);

                    final Tabpanels tabpanels = (Tabpanels) center.getFellow("divCenter").getFellow("tabBoxIndexCenter").getFellow("tabsIndexCenter").getFellow("tabpanelsBoxIndexCenter");
                    final Tabpanel tabpanel = new Tabpanel();
                    tabpanel.setHeight("100%");
                    tabpanel.setStyle("padding: 0px;");
                    tabpanel.setParent(tabpanels);

                    Executions.createComponents(zulFilePathName, tabpanel, null);
                    tab.setSelected(true);
                }
            } else {
                final Borderlayout bl = (Borderlayout) Path.getComponent("/outerIndexWindow/borderlayoutMain");
                final Center center = bl.getCenter();
                center.getChildren().clear();
                Executions.createComponents(zulFilePathName, center, null);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("--> calling zul-file: " + zulFilePathName);
            }
        } catch (final Exception e) {
            Messagebox.show(e.toString());
        }
    }

    public void onClick$btnMainMenuExpandAll(Event event) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("--> " + event.toString());
        }
        doCollapseExpandAll(getMainMenuWindow(), true);
    }

    public void onClick$btnMainMenuCollapseAll(Event event) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("--> " + event.toString());
        }
        doCollapseExpandAll(getMainMenuWindow(), false);
    }

    public void onClick$btnMainMenuChange(Event event) throws Exception {
        // correct the desktop height
        final Checkbox cb = (Checkbox) Path.getComponent("/outerIndexWindow/CBtreeMenu");
        cb.setChecked(false);
        final Borderlayout bl = (Borderlayout) Path.getComponent("/outerIndexWindow/borderlayoutMain");
        final West west = bl.getWest();
        west.setVisible(false);

        final North north = bl.getNorth();
        north.setFlex(true); // that's important !!!!

        final Div div = (Div) north.getFellow("divDropDownMenu");
        final Menubar menuBar = (Menubar) div.getFellow("mainMenuBar");
        menuBar.setVisible(true);

        // generate the menu from the menuXMLFile
        ZkossDropDownMenuFactory.addDropDownMenu(menuBar);

        final Menuitem changeToTreeMenu = new Menuitem();
        changeToTreeMenu.setLabel(Labels.getLabel("menu_Item_backToTree"));
        changeToTreeMenu.setImage("/images/icons/refresh2_yellow_16x16.gif");
        changeToTreeMenu.setParent(menuBar);
        changeToTreeMenu.addEventListener("onClick", new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                final Borderlayout bl = (Borderlayout) Path.getComponent("/outerIndexWindow/borderlayoutMain");
                final West west = bl.getWest();
                west.setVisible(true);

                final North north = bl.getNorth();
                final Div div = (Div) north.getFellow("divDropDownMenu");
                final Menubar menuBar = (Menubar) div.getFellow("mainMenuBar");
                menuBar.getChildren().clear();
                menuBar.setVisible(false);
                north.setFlex(false); // that's important !!!!

                // correct the desktop height
                final Checkbox cb = (Checkbox) Path.getComponent("/outerIndexWindow/CBtreeMenu");
                cb.setChecked(true);

                final Window win = (Window) Path.getComponent("/outerIndexWindow");
                win.invalidate();

            }
        });

        final Window win = (Window) Path.getComponent("/outerIndexWindow");
        win.invalidate();
    }

    private void doCollapseExpandAll(Component component, boolean aufklappen) {
        if (component instanceof Treeitem) {
            final Treeitem treeitem = (Treeitem) component;
            treeitem.setOpen(aufklappen);
        }
        final Collection<?> com = component.getChildren();
        if (com != null) {
            for (final Iterator<?> iterator = com.iterator(); iterator.hasNext(); ) {
                doCollapseExpandAll((Component) iterator.next(), aufklappen);

            }
        }
    }

    public Window getMainMenuWindow() {
        return this.mainMenuWindow;
    }

    public void setMainMenuWindow(Window mainMenuWindow) {
        this.mainMenuWindow = mainMenuWindow;
    }

    public void setIndexCtrl(IndexCtrl indexCtrl) {
        this.indexCtrl = indexCtrl;
    }

    public IndexCtrl getIndexCtrl() {
        return indexCtrl;
    }

}
