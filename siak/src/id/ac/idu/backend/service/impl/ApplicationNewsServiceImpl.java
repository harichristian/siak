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

import id.ac.idu.backend.dao.ApplicationNewsDAO;
import id.ac.idu.backend.model.ApplicationNews;
import id.ac.idu.backend.service.ApplicationNewsService;

import java.util.List;

/**
 * EN: Service implementation for methods that depends on
 * <b>ApplicationNews</b>.<br>
 * DE: Service Methoden Implementierung betreffend <b>ApplicationNews</b>.<br>
 *
 * @author bbruhns
 * @author sgerth
 */
public class ApplicationNewsServiceImpl implements ApplicationNewsService {

    private ApplicationNewsDAO applicationNewsDAO;

    public ApplicationNewsDAO getApplicationNewsDAO() {
        return applicationNewsDAO;
    }

    public void setApplicationNewsDAO(ApplicationNewsDAO applicationNewsDAO) {
        this.applicationNewsDAO = applicationNewsDAO;
    }

    @Override
    public List<ApplicationNews> getAllApplicationNews() {
        return getApplicationNewsDAO().getAllApplicationNews();
    }

    @Override
    public int getCountAllApplicationNews() {
        return getApplicationNewsDAO().getCountAllApplicationNews();
    }
}
