package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Malumni;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/12/12
 * Time: 3:38 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MalumniDAO {
      /**
     * EN: Get a new Malumni object.<br>
     * DE: Gibt ein neues Malumni (Niederlassung) Objekt zurueck.<br>
     *
     * @return Malumni
     */
    public Malumni getNewMalumni();

    /**
     * EN: Get an Malumni by its ID.<br>
     * DE: Gibt ein Malumni anhand seiner ID zurueck.<br>
     *
     * @param filId / the persistence identifier / der PrimaerKey
     * @return Malumni / Malumni
     */
    public Malumni getMalumniById(Long filId);

    /**
     * EN: Get an Malumni object by its ID.<br>
     * DE: Gibt ein Malumni Objekt anhand seiner ID zurueck.<br>
     *
     * @param fil_nr / the malumni number / die Niederlassungs Nummer
     * @return Malumni / Malumni
     */
    public Malumni getMalumniByFilNr(String fil_nr);

    /**
     * EN: Get a list of all Malumnis.<br>
     * DE: Gibt eine Liste aller Malumnis zurueck.<br>
     *
     * @return List of Malumnis / Liste von Malumnis
     */
    public List<Malumni> getAllMalumnis();

    /**
     * EN: Get the count of all Malumnis.<br>
     * DE: Gibt die Anzahl aller Malumnis zurueck.<br>
     *
     * @return int
     */
    public int getCountAllMalumnis();

    /**
     * EN: Gets a list of Malumnis where the city name contains the %string% .<br>
     * DE: Gibt eine Liste aller Malumnis zurueck bei denen der Stadtname
     * %string% enthaelt.<br>
     *
     * @param string Name of the city / Stadtnamen
     * @return List of Malumnis / Liste of Malumnis
     */
    public List<Malumni> getMalumnisLikeCity(String string);

    /**
     * EN: Gets a list of Malumnis where the malumni name1 contains the %string% .<br>
     * DE: Gibt eine Liste aller Malumnis zurueck bei denen der Malumni Name1
     * %string% enthaelt.<br>
     *
     * @param string Name1 of the malumni / Name1 vom Malumni
     * @return List of Malumnis / Liste of Malumnis
     */
    public List<Malumni> getMalumnisLikeName1(String string);

    /**
     * EN: Gets a list of Malumnis where the malumni number contains the %string%
     * .<br>
     * DE: Gibt eine Liste aller Malumnis zurueck bei denen die Malumni Nummer
     * %string% enthaelt.<br>
     *
     * @param string Number of the malumni / Nummer vom Malumni
     * @return List of Malumnis / Liste of Malumnis
     */
    public List<Malumni> getMalumnisLikeNo(String string);

    /**
     * EN: Deletes an Malumni by its Id.<br>
     * DE: Loescht ein Malumni anhand seiner Id.<br>
     *
     * @param id / the persistence identifier / der PrimaerKey
     */
    public void deleteMalumniById(long id);

    /**
     * EN: Saves new or updates an Malumni.<br>
     * DE: Speichert neu oder aktualisiert ein Malumni.<br>
     */
    public void saveOrUpdate(Malumni entity);

    /**
     * EN: Deletes an Malumni.<br>
     * DE: Loescht ein Malumni.<br>
     */
    public void delete(Malumni entity);

    /**
     * EN: Saves an Malumni.<br>
     * DE: Speichert ein Malumni.<br>
     */
    public void save(Malumni entity);

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
