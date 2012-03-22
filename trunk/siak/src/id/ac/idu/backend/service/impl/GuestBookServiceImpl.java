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
package id.ac.idu.backend.service.impl;

import id.ac.idu.backend.dao.GuestBookDAO;
import id.ac.idu.backend.model.GuestBook;
import id.ac.idu.backend.service.GuestBookService;

/**
 * EN: Service implementation for methods that depends on <b>Guestbook</b>.<br>
 * DE: Service Methoden Implementierung betreffend <b>Gaestebuch</b>.<br>
 *
 * @author bbruhns
 * @author sgerth
 */
public class GuestBookServiceImpl implements GuestBookService {

    private GuestBookDAO guestBookDAO;

    public GuestBookDAO getGuestBookDAO() {
        return guestBookDAO;
    }

    public void setGuestBookDAO(GuestBookDAO guestBookDAO) {
        this.guestBookDAO = guestBookDAO;
    }

    @Override
    public GuestBook getNewGuestBook() {
        return getGuestBookDAO().getNewGuestBook();
    }

    @Override
    public void delete(GuestBook guestBook) {
        getGuestBookDAO().delete(guestBook);
    }

    @Override
    public void saveOrUpdate(GuestBook guestBook) {
        getGuestBookDAO().saveOrUpdate(guestBook);
    }

    @Override
    public int getCountAllGuestBooks() {
        return getGuestBookDAO().getCountAllGuestBooks();
    }

}
