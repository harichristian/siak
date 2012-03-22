package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mkec;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/19/12
 * Time: 12:36 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MkecDAO  {
    /* EN: Get a new Mkec object.<br>
     * DE: Gibt ein neues Mkec (Niederlassung) Objekt zurueck.<br>
     *
     * @return Mkec
     */
    public Mkec getNewMkec();

    /**
     * EN: Get an Mkec by its ID.<br>
     * DE: Gibt ein Mkec anhand seiner ID zurueck.<br>
     *
     * @param filId / the persistence identifier / der PrimaerKey
     * @return Mkec / Mkec
     */
    public Mkec getMkecById(Long filId);

    /**
     * EN: Get an Mkec object by its ID.<br>
     * DE: Gibt ein Mkec Objekt anhand seiner ID zurueck.<br>
     *
     * @param fil_nr / the mkec number / die Niederlassungs Nummer
     * @return Mkec / Mkec
     */
    public Mkec getMkecByFilNr(String fil_nr);

    /**
     * EN: Get a list of all Mkecs.<br>
     * DE: Gibt eine Liste aller Mkecs zurueck.<br>
     *
     * @return List of Mkecs / Liste von Mkecs
     */
    public List<Mkec> getAllMkecs();

    /**
     * EN: Get the count of all Mkecs.<br>
     * DE: Gibt die Anzahl aller Mkecs zurueck.<br>
     *
     * @return int
     */
    public int getCountAllMkecs();

    /**
     * EN: Gets a list of Mkecs where the city name contains the %string% .<br>
     * DE: Gibt eine Liste aller Mkecs zurueck bei denen der Stadtname
     * %string% enthaelt.<br>
     *
     * @param string Name of the city / Stadtnamen
     * @return List of Mkecs / Liste of Mkecs
     */
    public List<Mkec> getMkecsLikeCity(String string);

    /**
     * EN: Gets a list of Mkecs where the mkec name1 contains the %string% .<br>
     * DE: Gibt eine Liste aller Mkecs zurueck bei denen der Mkec Name1
     * %string% enthaelt.<br>
     *
     * @param string Name1 of the mkec / Name1 vom Mkec
     * @return List of Mkecs / Liste of Mkecs
     */
    public List<Mkec> getMkecsLikeName1(String string);

    /**
     * EN: Gets a list of Mkecs where the mkec number contains the %string%
     * .<br>
     * DE: Gibt eine Liste aller Mkecs zurueck bei denen die Mkec Nummer
     * %string% enthaelt.<br>
     *
     * @param string Number of the mkec / Nummer vom Mkec
     * @return List of Mkecs / Liste of Mkecs
     */
    public List<Mkec> getMkecsLikeNo(String string);

    /**
     * EN: Deletes an Mkec by its Id.<br>
     * DE: Loescht ein Mkec anhand seiner Id.<br>
     *
     * @param id / the persistence identifier / der PrimaerKey
     */
    public void deleteMkecById(long id);

    /**
     * EN: Saves new or updates an Mkec.<br>
     * DE: Speichert neu oder aktualisiert ein Mkec.<br>
     */
    public void saveOrUpdate(Mkec entity);

    /**
     * EN: Deletes an Mkec.<br>
     * DE: Loescht ein Mkec.<br>
     */
    public void delete(Mkec entity);

    /**
     * EN: Saves an Mkec.<br>
     * DE: Speichert ein Mkec.<br>
     */
    public void save(Mkec entity);

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
