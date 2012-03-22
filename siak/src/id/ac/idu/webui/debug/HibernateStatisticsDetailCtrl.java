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
package id.ac.idu.webui.debug;

import id.ac.idu.backend.model.HibernateStatistics;
import id.ac.idu.gui.service.GuiHibernateStatisticsService;
import id.ac.idu.webui.debug.model.HibernateStatisticDetailRowRenderer;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.event.CreateEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.*;

import java.util.ArrayList;
import java.util.Map;

/**
 * Controller for the HibernateStatistic Details, if the user opens a
 * Grid-Detail. <br>
 * Zul: /WEB-INF/pages/debug/HibernateStatisticsDetail.zul <br>
 *
 * @author bbruhns
 * @author sgerth
 */
public class HibernateStatisticsDetailCtrl extends GenericForwardComposer {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(HibernateStatisticsDetailCtrl.class);

    private transient Groupbox gb;

    // ServiceDAOs / Domain Classes
    private transient GuiHibernateStatisticsService guiHibernateStatisticsService;
    private HibernateStatistics statistics;

    @SuppressWarnings("unchecked")
    public void onCreate(Event event) throws Exception {

        this.gb = (Groupbox) event.getTarget();

        // System.out.println(ZkossComponentTreeUtil.getZulTree(self));

        // get the params map that are overhanded by creation.
        final CreateEvent ce = (CreateEvent) event;
        final Map<String, Object> args = ce.getArg();

        if (args.containsKey("hibernateStatistics")) {
            final HibernateStatistics hs = (HibernateStatistics) args.get("hibernateStatistics");
            setStatistics(hs);
        } else {
            setStatistics(null);
        }

        // Load the related data
        this.guiHibernateStatisticsService.initDetails(getStatistics());

        // Set the variable for accessing in the zul-file the bean.properties
        event.getTarget().setVariable("hs", getStatistics(), false);
        // event.getTarget().invalidate();

        // create the entity listBox
        doCreateEntityGrid(getStatistics());

    }

    /**
     * Create the detailgrid for the related entities and the counts of the CRUD
     * operations on it.
     *
     * @param hs
     */
    @SuppressWarnings("unchecked")
    private void doCreateEntityGrid(HibernateStatistics hs) {

        final Separator sep = new Separator();
        sep.setOrient("vertical");
        sep.setParent(this.gb);

        final Grid entityGrid = new Grid();
        entityGrid.setWidth("100%");
        entityGrid.setParent(this.gb);
        final Columns columns = new Columns();
        columns.setParent(entityGrid);

        Column col;
        col = new Column();
        col.setWidth("40%");
        col.setLabel("Entity");
        col.setParent(columns);
        col = new Column();
        col.setWidth("10%");
        col.setLabel("load");
        col.setParent(columns);
        col = new Column();
        col.setWidth("10%");
        col.setLabel("update");
        col.setParent(columns);
        col = new Column();
        col.setWidth("10%");
        col.setLabel("insert");
        col.setParent(columns);
        col = new Column();
        col.setWidth("10%");
        col.setLabel("delete");
        col.setParent(columns);
        col = new Column();
        col.setWidth("10%");
        col.setLabel("fetch");
        col.setParent(columns);
        col = new Column();
        col.setWidth("10%");
        col.setLabel("optimisticFailure");
        col.setParent(columns);

        final Rows rows = new Rows();
        rows.setParent(entityGrid);

        entityGrid.setRowRenderer(new HibernateStatisticDetailRowRenderer());
        entityGrid.setModel(new ListModelList(new ArrayList(getStatistics().getHibernateEntityStatisticsSet())));
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++//
    // ++++++++++++++++++ getter / setter +++++++++++++++++++//
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++//

    public GuiHibernateStatisticsService getGuiHibernateStatisticsService() {
        return this.guiHibernateStatisticsService;
    }

    public void setGuiHibernateStatisticsService(GuiHibernateStatisticsService guiHibernateStatisticsService) {
        this.guiHibernateStatisticsService = guiHibernateStatisticsService;
    }

    private HibernateStatistics getStatistics() {
        return this.statistics;
    }

    private void setStatistics(HibernateStatistics statistics) {
        this.statistics = statistics;
    }
}
