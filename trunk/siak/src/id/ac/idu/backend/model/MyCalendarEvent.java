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
package id.ac.idu.backend.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

/**
 * EN: Model class for <b>Calendar Event</b>.<br>
 * DE: Model Klasse fuer <b>Kalender Termin</b>.<br>
 *
 * @author bbruhns
 * @author sgerth
 */
public class MyCalendarEvent implements java.io.Serializable, Entity {

    private static final long serialVersionUID = 1L;

    private long id = Long.MIN_VALUE;
    private int version;
    private String title;
    private String content;
    private Date beginDate;
    private Date endDate;
    private String headerColor;
    private String contentColor;
    private SecUser secUser;
    private int locked = 0;

    public boolean isNew() {
        return (getId() == Long.MIN_VALUE);
    }

    public MyCalendarEvent() {
    }

    public MyCalendarEvent(long id, String content, Date beginDate, Date endDate, SecUser secUser) {
        this.setId(id);
        this.content = content;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.secUser = secUser;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    /**
     * EN: Hibernate version field. Do not touch this!.<br>
     * DE: Hibernate Versions Info. Bitte nicht benutzen!<br>
     */
    public int getVersion() {
        return this.version;
    }

    /**
     * EN: Hibernate version field. Do not touch this!.<br>
     * DE: Hibernate Versions Info. Bitte nicht benutzen!<br>
     */
    public void setVersion(int version) {
        this.version = version;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setHeaderColor(String headerColor) {
        this.headerColor = headerColor;
    }

    public String getHeaderColor() {
        return headerColor;
    }

    public void setContentColor(String contentColor) {
        this.contentColor = contentColor;
    }

    public String getContentColor() {
        return contentColor;
    }

    public void setSecUser(SecUser secUser) {
        this.secUser = secUser;
    }

    public SecUser getSecUser() {
        return secUser;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }

    public int isLocked() {
        return locked;
    }

    public void putLocked(Boolean locked) {
        this.setLocked(((locked) ? 1 : 0));
    }

    public Boolean checkLocked() {
        return (this.isLocked() == 1);
    }

    @Override
    public int hashCode() {
        return Long.valueOf(getId()).hashCode();
    }

    public boolean equals(MyCalendarEvent calendarEvent) {
        return getId() == calendarEvent.getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof MyCalendarEvent) {
            MyCalendarEvent calendarEvent = (MyCalendarEvent) obj;
            return equals(calendarEvent);
        }

        return false;
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", getId()).toString();
    }

}
