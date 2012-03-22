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
package id.ac.idu.webui.index;

import id.ac.idu.backend.model.Office;
import id.ac.idu.backend.service.OfficeService;
import id.ac.idu.common.menu.MainMenuCtrl;
import id.ac.idu.policy.model.UserImpl;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.ZksampleMessageUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zul.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the /WEB-INF/pages/index.zul file.<br>
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 *
 * @author bbruhns
 * @author sgerth
 * @changes 11/07/2009: bbr changed to extending from GFCBaseCtrl<br>
 * (GenericForwardComposer) for spring-managed creation.<br>
 */
public class IndexCtrl extends GFCBaseCtrl implements Serializable {

    private static final long serialVersionUID = -3407055074703929527L;
    private final static Logger logger = Logger.getLogger(IndexCtrl.class);

    protected Menubar mainMenuBar;             // autowired
    protected Label label_AppName;             // autowired
    protected Intbox currentDesktopHeight;     // autowired
    protected Intbox currentDesktopWidth;     // autowired
    protected Checkbox CBtreeMenu;             // autowired
    protected Tabs tabsIndexCenter;         // autowired

    private transient OfficeService officeService;
    private final int centerAreaHeightOffset = 50;
    private MainMenuCtrl mainMenuCtrl;        // Controllers

    public IndexCtrl() {
        super();
    }

    public void onCreate$outerIndexWindow(Event event) throws Exception {
        this.mainMenuBar.setVisible(false);
        createMainTreeMenu(event);
        doDemoMode();

        final String userName = ((UserImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        final String tenantId = "4711";
        final String officeID = "39";

        EventQueues.lookup("userNameEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeUser", null, userName));
        EventQueues.lookup("tenantIdEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeTenant", null, tenantId));
        EventQueues.lookup("officeIdEventQueue", EventQueues.DESKTOP, true).publish(new Event("onChangeOfficeId", null, officeID));
    }

    public void onClientInfo(ClientInfoEvent event) throws Exception {
        setCurrentDesktopHeight(event.getDesktopHeight() - this.centerAreaHeightOffset);
        setCurrentDesktopWidth(event.getDesktopWidth());

    }

    public String doGetLoggedInUser() {
        final String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return userName;
    }

    public void onClick$btnLogout() throws IOException {
        getUserWorkspace().doLogout(); // logout.
    }

    public void onClick$btnCloseAllTabs() throws IOException, InterruptedException {
        List<AbstractComponent> list = tabsIndexCenter.getChildren();
        try {
            while (!list.isEmpty()) {
                int i = list.size();
                if (list.get(i - 1) instanceof Tab) {
                    if (StringUtils.equals(((Tab) list.get(i - 1)).getId(), "tab_menu_Item_Home")) {
                        break;
                    } else {
                        ((Tab) list.get(i - 1)).onClose();
                    }
                }
            }
        } catch (Exception e) {
            ZksampleMessageUtils.showErrorMessage(e.toString());
        }
    }

    private void doDemoMode() {
        Office office = getOfficeService().getOfficeByID(Long.valueOf(1));
        getUserWorkspace().setOffice(office);
    }

    private void createMainTreeMenu(Event event) {
        Borderlayout bl = (Borderlayout) Path.getComponent("/outerIndexWindow/borderlayoutMain");
        West west = bl.getWest();
        west.setFlex(true);
        west.getChildren().clear();

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("indexController", this);
        Executions.createComponents("/WEB-INF/pages/mainTreeMenu.zul", west, map);
    }

    public void showWelcomePage() throws InterruptedException {
        Borderlayout bl = (Borderlayout) Path.getComponent("/outerIndexWindow/borderlayoutMain");
        Center center = bl.getCenter();
        center.getChildren().clear();
        Executions.createComponents("/WEB-INF/pages/welcome.zul", center, null);
    }

    public void onClick$btnIndexMySettings() throws IOException, InterruptedException {
        Window win = null;
        Window parentWin = (Window) Path.getComponent("/outerIndexWindow");
        try {
            win = (Window) Executions.createComponents("/WEB-INF/pages/sec_user/mySettings.zul", parentWin, null);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            ZksampleMessageUtils.showErrorMessage(e.getLocalizedMessage());
            win.detach();
        }
    }

    private void showPage(String zulFilePathName, String tabName) throws InterruptedException {
        try {
            final int workWithTabs = 1;
            if (workWithTabs == 1) {
                /* get an instance of the borderlayout defined in the zul-file */
                Borderlayout bl = (Borderlayout) Path.getComponent("/outerIndexWindow/borderlayoutMain");
                /* get an instance of the searched CENTER layout area */
                Center center = bl.getCenter();
                // get the tabs component
                Tabs tabs = (Tabs) center.getFellow("divCenter").getFellow("tabBoxIndexCenter").getFellow("tabsIndexCenter");

                /**
                 * Check if the tab is already opened than select them and<br>
                 * go out of here. If not than create them.<br>
                 */

                Tab checkTab = null;
                try {
                    // checkTab = (Tab) tabs.getFellow(tabName);
                    checkTab = (Tab) tabs.getFellow("tab_" + tabName.trim());
                    checkTab.setSelected(true);
                } catch (final ComponentNotFoundException ex) {
                    // Ignore if can not get tab.
                }

                if (checkTab == null) {

                    Tab tab = new Tab();
                    tab.setId("tab_" + tabName.trim());
                    tab.setLabel(tabName.trim());
                    tab.setClosable(true);

                    tab.setParent(tabs);

                    Tabpanels tabpanels = (Tabpanels) center.getFellow("divCenter").getFellow("tabBoxIndexCenter").getFellow("tabsIndexCenter").getFellow("tabpanelsBoxIndexCenter");
                    Tabpanel tabpanel = new Tabpanel();
                    tabpanel.setHeight("100%");
                    tabpanel.setStyle("padding: 0px;");
                    tabpanel.setParent(tabpanels);

                    /**
                     * Create the page and put it in the tabs area. If zul-file
                     * is not found, detach the created tab
                     */
                    try {
                        Executions.createComponents(zulFilePathName, tabpanel, null);
                        tab.setSelected(true);
                    } catch (final Exception e) {
                        tab.detach();
                    }

                }
            } else {
                /* get an instance of the borderlayout defined in the zul-file */
                Borderlayout bl = (Borderlayout) Path.getComponent("/outerIndexWindow/borderlayoutMain");
                /* get an instance of the searched CENTER layout area */
                Center center = bl.getCenter();
                /* clear the center child comps */
                center.getChildren().clear();
                /**
                 * create the page and put it in the center layout area
                 */
                Executions.createComponents(zulFilePathName, center, null);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("--> calling zul-file: " + zulFilePathName);
            }
        } catch (final Exception e) {
            Messagebox.show(e.toString());
        }
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    // ++++++++++++++++++ getter / setter +++++++++++++++++++//
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

    public void setOfficeService(OfficeService officeService) {
        this.officeService = officeService;
    }

    public OfficeService getOfficeService() {
        return this.officeService;
    }

    public void setCurrentDesktopHeight(int desktopHeight) {
        if (isTreeMenu() == true) {
            this.currentDesktopHeight.setValue(Integer.valueOf(desktopHeight));
        } else {
            this.currentDesktopHeight.setValue(Integer.valueOf(desktopHeight - 30));
        }
    }

    public int getCurrentDesktopHeight() {
        return this.currentDesktopHeight.getValue().intValue();
    }

    public void setCurrentDesktopWidth(int currentDesktopWidth) {
        this.currentDesktopWidth.setValue(Integer.valueOf(currentDesktopWidth));
    }

    public int getCurrentDesktopWidth() {
        return this.currentDesktopWidth.getValue().intValue();
    }

    public void setTreeMenu(boolean treeMenu) {
        this.CBtreeMenu.setChecked(treeMenu);
    }

    public boolean isTreeMenu() {
        return this.CBtreeMenu.isChecked();
    }

    public void setMainMenuCtrl(MainMenuCtrl mainMenuCtrl) {
        this.mainMenuCtrl = mainMenuCtrl;
    }

    public MainMenuCtrl getMainMenuCtrl() {
        return mainMenuCtrl;
    }

}
