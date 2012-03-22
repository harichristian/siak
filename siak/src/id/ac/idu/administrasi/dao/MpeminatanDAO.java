package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mpeminatan;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/21/12
 * Time: 1:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MpeminatanDAO  {
    /**
         * EN: Get a new Mpeminatan object.<br>
         * DE: Gibt ein neues Mpeminatan (Niederlassung) Objekt zurueck.<br>
         *
         * @return Mpeminatan
         */
        public Mpeminatan getNewMpeminatan();

        /**
         * EN: Get an Mpeminatan by its ID.<br>
         * DE: Gibt ein Mpeminatan anhand seiner ID zurueck.<br>
         *
         * @param filId / the persistence identifier / der PrimaerKey
         * @return Mpeminatan / Mpeminatan
         */
        public Mpeminatan getMpeminatanById(Long filId);

        /**
         * EN: Get an Mpeminatan object by its ID.<br>
         * DE: Gibt ein Mpeminatan Objekt anhand seiner ID zurueck.<br>
         *
         * @param fil_nr / the mpeminatan number / die Niederlassungs Nummer
         * @return Mpeminatan / Mpeminatan
         */
        public Mpeminatan getMpeminatanByFilNr(String fil_nr);

        /**
         * EN: Get a list of all Mpeminatans.<br>
         * DE: Gibt eine Liste aller Mpeminatans zurueck.<br>
         *
         * @return List of Mpeminatans / Liste von Mpeminatans
         */
        public List<Mpeminatan> getAllMpeminatans();

        /**
         * EN: Get the count of all Mpeminatans.<br>
         * DE: Gibt die Anzahl aller Mpeminatans zurueck.<br>
         *
         * @return int
         */
        public int getCountAllMpeminatans();

        /**
         * EN: Gets a list of Mpeminatans where the city name contains the %string% .<br>
         * DE: Gibt eine Liste aller Mpeminatans zurueck bei denen der Stadtname
         * %string% enthaelt.<br>
         *
         * @param string Name of the city / Stadtnamen
         * @return List of Mpeminatans / Liste of Mpeminatans
         */
        public List<Mpeminatan> getMpeminatansLikeCity(String string);

        /**
         * EN: Gets a list of Mpeminatans where the mpeminatan name1 contains the %string% .<br>
         * DE: Gibt eine Liste aller Mpeminatans zurueck bei denen der Mpeminatan Name1
         * %string% enthaelt.<br>
         *
         * @param string Name1 of the mpeminatan / Name1 vom Mpeminatan
         * @return List of Mpeminatans / Liste of Mpeminatans
         */
        public List<Mpeminatan> getMpeminatansLikeName1(String string);

        /**
         * EN: Gets a list of Mpeminatans where the mpeminatan number contains the %string%
         * .<br>
         * DE: Gibt eine Liste aller Mpeminatans zurueck bei denen die Mpeminatan Nummer
         * %string% enthaelt.<br>
         *
         * @param string Number of the mpeminatan / Nummer vom Mpeminatan
         * @return List of Mpeminatans / Liste of Mpeminatans
         */
        public List<Mpeminatan> getMpeminatansLikeNo(String string);

        /**
         * EN: Deletes an Mpeminatan by its Id.<br>
         * DE: Loescht ein Mpeminatan anhand seiner Id.<br>
         *
         * @param id / the persistence identifier / der PrimaerKey
         */
        public void deleteMpeminatanById(long id);

        /**
         * EN: Saves new or updates an Mpeminatan.<br>
         * DE: Speichert neu oder aktualisiert ein Mpeminatan.<br>
         */
        public void saveOrUpdate(Mpeminatan entity);

        /**
         * EN: Deletes an Mpeminatan.<br>
         * DE: Loescht ein Mpeminatan.<br>
         */
        public void delete(Mpeminatan entity);

        /**
         * EN: Saves an Mpeminatan.<br>
         * DE: Speichert ein Mpeminatan.<br>
         */
        public void save(Mpeminatan entity);

        /**
         * EN: Get a paged list of all Branches.<br>
         * DE: Gibt eine paged Liste aller Branchen zurueck.<br>
         *
         * @param text     Text for search / SuchText
         * @param start    StartRecord / Start Datensatz
         * @param pageSize Count of Records / Anzahl Datensaetze
         * @return List of YoutubeLinks / Liste von YoutubeLinks
         */
        public ResultObject getAllAlumniLikeText(String text, int start, int pageSize);
    }
