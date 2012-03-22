package id.ac.idu.administrasi.service;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mkel;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/19/12
 * Time: 12:52 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MkelService {
 /**
     * EN: Get a new Mkel object.<br>
     * DE: Gibt ein neues Mkel (Niederlassung) Objekt zurueck.<br>
     *
     * @return Mkel
     */
    public Mkel getNewMkel();

    /**
     * EN: Get the count of all Mkels.<br>
     * DE: Gibt die Anzahl aller Mkels zurueck.<br>
     *
     * @return int
     */
    public int getCountAllMkels();

    /**
     * EN: Get an Mkel by its ID.<br>
     * DE: Gibt ein Mkel anhand seiner ID zurueck.<br>
     *
     * @param fil_nr / the persistence identifier / der PrimaerKey
     * @return Mkel / Mkel
     */
    public Mkel getMkelByID(Long fil_nr);

    /**
     * EN: Get a list of all Mkels.<br>
     * DE: Gibt eine Liste aller Mkels zurueck.<br>
     *
     * @return List of Mkels / Liste von Mkels
     */
    public List<Mkel> getAllMkels();

    /**
     * EN: Gets a list of Mkels where the city name contains the %string% .<br>
     * DE: Gibt eine Liste aller Mkels zurueck bei denen der Stadtname
     * %string% enthaelt.<br>
     *
     * @param string Name of the city / Stadtnamen
     * @return List of Mkels / Liste of Mkels
     */
    public List<Mkel> getMkelsLikeCity(String string);

    /**
     * EN: Gets a list of Mkels where the mkel name1 contains the %string% .<br>
     * DE: Gibt eine Liste aller Mkels zurueck bei denen der Mkel Name1
     * %string% enthaelt.<br>
     *
     * @param string Name1 of the mkel / Name1 vom Mkel
     * @return List of Mkels / Liste of Mkels
     */
    public List<Mkel> getMkelsLikeName1(String string);

    /**
     * EN: Gets a list of Mkels where the mkel number contains the %string%
     * .<br>
     * DE: Gibt eine Liste aller Mkels zurueck bei denen die Mkel Nummer
     * %string% enthaelt.<br>
     *
     * @param string Number of the mkel / Nummer vom Mkel
     * @return List of Mkels / Liste of Mkels
     */
    public List<Mkel> getMkelsLikeNo(String string);

    /**
     * EN: Saves new or updates an Mkel.<br>
     * DE: Speichert neu oder aktualisiert ein Mkel.<br>
     */
    public void saveOrUpdate(Mkel entity);

    /**
     * EN: Deletes an Mkel.<br>
     * DE: Loescht ein Mkel.<br>
     */
    public void delete(Mkel mkel);

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