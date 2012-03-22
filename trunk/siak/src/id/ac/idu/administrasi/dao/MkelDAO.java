package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mkel;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/19/12
 * Time: 12:36 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MkelDAO  {
    /* EN: Get a new Mkel object.<br>
     * DE: Gibt ein neues Mkel (Niederlassung) Objekt zurueck.<br>
     *
     * @return Mkel
     */
    public Mkel getNewMkel();

    /**
     * EN: Get an Mkel by its ID.<br>
     * DE: Gibt ein Mkel anhand seiner ID zurueck.<br>
     *
     * @param filId / the persistence identifier / der PrimaerKey
     * @return Mkel / Mkel
     */
    public Mkel getMkelById(Long filId);

    /**
     * EN: Get an Mkel object by its ID.<br>
     * DE: Gibt ein Mkel Objekt anhand seiner ID zurueck.<br>
     *
     * @param fil_nr / the mkel number / die Niederlassungs Nummer
     * @return Mkel / Mkel
     */
    public Mkel getMkelByFilNr(String fil_nr);

    /**
     * EN: Get a list of all Mkels.<br>
     * DE: Gibt eine Liste aller Mkels zurueck.<br>
     *
     * @return List of Mkels / Liste von Mkels
     */
    public List<Mkel> getAllMkels();

    /**
     * EN: Get the count of all Mkels.<br>
     * DE: Gibt die Anzahl aller Mkels zurueck.<br>
     *
     * @return int
     */
    public int getCountAllMkels();

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
     * EN: Deletes an Mkel by its Id.<br>
     * DE: Loescht ein Mkel anhand seiner Id.<br>
     *
     * @param id / the persistence identifier / der PrimaerKey
     */
    public void deleteMkelById(long id);

    /**
     * EN: Saves new or updates an Mkel.<br>
     * DE: Speichert neu oder aktualisiert ein Mkel.<br>
     */
    public void saveOrUpdate(Mkel entity);

    /**
     * EN: Deletes an Mkel.<br>
     * DE: Loescht ein Mkel.<br>
     */
    public void delete(Mkel entity);

    /**
     * EN: Saves an Mkel.<br>
     * DE: Speichert ein Mkel.<br>
     */
    public void save(Mkel entity);

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