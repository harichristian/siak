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
package id.ac.idu.backend.dao.impl;

import id.ac.idu.backend.dao.MyCalendarEventDAO;
import id.ac.idu.backend.model.MyCalendarEvent;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * EN: DAO methods implementation for the <b>Calendar Event</b> model class.<br>
 * DE: DAO Methoden Implementierung fuer die <b>Calendar Event</b> Model Klasse.<br>
 *
 * @author bbruhns
 * @author Stephan Gerth
 */
@Repository
public class MyCalendarEventDAOImpl extends BasisDAO<MyCalendarEvent> implements MyCalendarEventDAO {

    @Override
    public MyCalendarEvent getNewCalendarEvent() {
        return new MyCalendarEvent();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<MyCalendarEvent> getAllCalendarEventsByUserId(long usrId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(MyCalendarEvent.class);
        criteria.add(Restrictions.eq("secUser.id", usrId));
        criteria.setFetchMode("secUser", FetchMode.JOIN);

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<MyCalendarEvent> getCalendarEventsForBeginDate(Date beginDate, long usrId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(MyCalendarEvent.class);
        criteria.add(Restrictions.eq("beginDate", beginDate));
        criteria.add(Restrictions.eq("secUser.id", usrId));
        criteria.setFetchMode("secUser", FetchMode.JOIN);

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public MyCalendarEvent getCalendarEventByID(final long id) {
        return get(MyCalendarEvent.class, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<MyCalendarEvent> getCalendarEventsFromToDate(Date beginDate, Date endDate, long usrId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(MyCalendarEvent.class);
        criteria.add(Restrictions.ge("beginDate", beginDate));
        criteria.add(Restrictions.le("endDate", endDate));
        criteria.add(Restrictions.eq("secUser.id", usrId));
        criteria.setFetchMode("secUser", FetchMode.JOIN);

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public int getCountAllCalendarEvents() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from MyCalendarEvent"));
    }

}
