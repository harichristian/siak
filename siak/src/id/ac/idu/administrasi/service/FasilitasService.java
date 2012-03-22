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
package id.ac.idu.administrasi.service;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mfasilitas;

import java.util.List;

/**
 * EN: Service methods Interface for working with <b>Mfasilitas</b> dependend DAOs.<br>
 * DE: Service Methoden Implementierung fuer die <b>Mfasilitas (Niederlassung)</b>
 * betreffenden DAOs.<br>
 *
 * @author bbruhns
 * @author sgerth
 */
public interface FasilitasService {

    /**
     * EN: Get a new Mfasilitas object.<br>
     * DE: Gibt ein neues Mfasilitas (Niederlassung) Objekt zurueck.<br>
     *
     * @return Mfasilitas
     */
    public Mfasilitas getNewMfasilitas();

    /**
     * EN: Get the count of all Mfasilitass.<br>
     * DE: Gibt die Anzahl aller Mfasilitass zurueck.<br>
     *
     * @return int
     */
    public int getCountAllMfasilitass();

    /**
     * EN: Get an Mfasilitas by its ID.<br>
     * DE: Gibt ein Mfasilitas anhand seiner ID zurueck.<br>
     *
     * @param fil_nr / the persistence identifier / der PrimaerKey
     * @return Mfasilitas / Mfasilitas
     */
    public Mfasilitas getMfasilitasByID(Long fil_nr);

    /**
     * EN: Get a list of all Mfasilitass.<br>
     * DE: Gibt eine Liste aller Mfasilitass zurueck.<br>
     *
     * @return List of Mfasilitass / Liste von Mfasilitass
     */
    public List<Mfasilitas> getAllMfasilitass();

    /**
     * EN: Gets a list of Mfasilitass where the city name contains the %string% .<br>
     * DE: Gibt eine Liste aller Mfasilitass zurueck bei denen der Stadtname
     * %string% enthaelt.<br>
     *
     * @param string Name of the city / Stadtnamen
     * @return List of Mfasilitass / Liste of Mfasilitass
     */
    public List<Mfasilitas> getMfasilitassLikeCity(String string);

    /**
     * EN: Gets a list of Mfasilitass where the fasilitas name1 contains the %string% .<br>
     * DE: Gibt eine Liste aller Mfasilitass zurueck bei denen der Mfasilitas Name1
     * %string% enthaelt.<br>
     *
     * @param string Name1 of the fasilitas / Name1 vom Mfasilitas
     * @return List of Mfasilitass / Liste of Mfasilitass
     */
    public List<Mfasilitas> getMfasilitassLikeName1(String string);

    /**
     * EN: Gets a list of Mfasilitass where the fasilitas number contains the %string%
     * .<br>
     * DE: Gibt eine Liste aller Mfasilitass zurueck bei denen die Mfasilitas Nummer
     * %string% enthaelt.<br>
     *
     * @param string Number of the fasilitas / Nummer vom Mfasilitas
     * @return List of Mfasilitass / Liste of Mfasilitass
     */
    public List<Mfasilitas> getMfasilitassLikeNo(String string);

    /**
     * EN: Saves new or updates an Mfasilitas.<br>
     * DE: Speichert neu oder aktualisiert ein Mfasilitas.<br>
     */
    public void saveOrUpdate(Mfasilitas ofice);

    /**
     * EN: Deletes an Mfasilitas.<br>
     * DE: Loescht ein Mfasilitas.<br>
     */
    public void delete(Mfasilitas fasilitas);

     /**
     * EN: Get a paged list of all Branches.<br>
     * DE: Gibt eine paged Liste aller Branchen zurueck.<br>
     *
     * @param text     Text for search / SuchText
     * @param start    StartRecord / Start Datensatz
     * @param pageSize Count of Records / Anzahl Datensaetze
     * @return List of YoutubeLinks / Liste von YoutubeLinks
     */
    public ResultObject getAllMfasilitasLikeText(String text, int start, int pageSize);
}
