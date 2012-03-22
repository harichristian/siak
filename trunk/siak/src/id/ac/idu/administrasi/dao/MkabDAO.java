package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mkab;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/19/12
 * Time: 12:36 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MkabDAO  {
   /* EN: Get a new Mkab object.<br>
     * DE: Gibt ein neues Mkab (Niederlassung) Objekt zurueck.<br>
     *
     * @return Mkab
     */
    public Mkab getNewMkab();

    /**
     * EN: Get an Mkab by its ID.<br>
     * DE: Gibt ein Mkab anhand seiner ID zurueck.<br>
     *
     * @param filId / the persistence identifier / der PrimaerKey
     * @return Mkab / Mkab
     */
    public Mkab getMkabById(Long filId);

    /**
     * EN: Get an Mkab object by its ID.<br>
     * DE: Gibt ein Mkab Objekt anhand seiner ID zurueck.<br>
     *
     * @param fil_nr / the mkab number / die Niederlassungs Nummer
     * @return Mkab / Mkab
     */
    public Mkab getMkabByFilNr(String fil_nr);

    /**
     * EN: Get a list of all Mkabs.<br>
     * DE: Gibt eine Liste aller Mkabs zurueck.<br>
     *
     * @return List of Mkabs / Liste von Mkabs
     */
    public List<Mkab> getAllMkabs();

    /**
     * EN: Get the count of all Mkabs.<br>
     * DE: Gibt die Anzahl aller Mkabs zurueck.<br>
     *
     * @return int
     */
    public int getCountAllMkabs();

    /**
     * EN: Gets a list of Mkabs where the city name contains the %string% .<br>
     * DE: Gibt eine Liste aller Mkabs zurueck bei denen der Stadtname
     * %string% enthaelt.<br>
     *
     * @param string Name of the city / Stadtnamen
     * @return List of Mkabs / Liste of Mkabs
     */
    public List<Mkab> getMkabsLikeCity(String string);

    /**
     * EN: Gets a list of Mkabs where the mkab name1 contains the %string% .<br>
     * DE: Gibt eine Liste aller Mkabs zurueck bei denen der Mkab Name1
     * %string% enthaelt.<br>
     *
     * @param string Name1 of the mkab / Name1 vom Mkab
     * @return List of Mkabs / Liste of Mkabs
     */
    public List<Mkab> getMkabsLikeName1(String string);

    /**
     * EN: Gets a list of Mkabs where the mkab number contains the %string%
     * .<br>
     * DE: Gibt eine Liste aller Mkabs zurueck bei denen die Mkab Nummer
     * %string% enthaelt.<br>
     *
     * @param string Number of the mkab / Nummer vom Mkab
     * @return List of Mkabs / Liste of Mkabs
     */
    public List<Mkab> getMkabsLikeNo(String string);

    /**
     * EN: Deletes an Mkab by its Id.<br>
     * DE: Loescht ein Mkab anhand seiner Id.<br>
     *
     * @param id / the persistence identifier / der PrimaerKey
     */
    public void deleteMkabById(long id);

    /**
     * EN: Saves new or updates an Mkab.<br>
     * DE: Speichert neu oder aktualisiert ein Mkab.<br>
     */
    public void saveOrUpdate(Mkab entity);

    /**
     * EN: Deletes an Mkab.<br>
     * DE: Loescht ein Mkab.<br>
     */
    public void delete(Mkab entity);

    /**
     * EN: Saves an Mkab.<br>
     * DE: Speichert ein Mkab.<br>
     */
    public void save(Mkab entity);

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
