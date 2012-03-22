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
package id.ac.idu.backend.service;

import id.ac.idu.backend.model.ApplicationNews;

import java.util.List;

/**
 * EN: Service methods Interface for working with <b>ApplicationNews</b> dependend
 * DAOs.<br>
 * DE: Service Methoden Implementierung fuer die <b>ApplicationNews</b>
 * betreffenden DAOs.<br>
 *
 * @author bbruhns
 * @author sgerth
 */
public interface ApplicationNewsService {

    /**
     * EN: Get a list of all Application News.<br>
     * DE: Gibt eine Liste aller Anwendungs-News zurueck.<br>
     *
     * @return List of ApplicationNews / Liste von Anwendungs-News
     */
    public List<ApplicationNews> getAllApplicationNews();

    /**
     * EN: Get the count of all Application News.<br>
     * DE: Gibt die Anzahl aller Anwendungs-News zurueck.<br>
     *
     * @return int
     */
    public int getCountAllApplicationNews();

}
