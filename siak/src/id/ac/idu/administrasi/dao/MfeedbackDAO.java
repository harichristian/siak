package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mfeedback;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/21/12
 * Time: 4:03 AM
 * To change this template use File | Settings | File Templates.
 */
public interface MfeedbackDAO {
     /* EN: Get a new Mfeedback object.<br>
     * DE: Gibt ein neues Mfeedback (Niederlassung) Objekt zurueck.<br>
     *
     * @return Mfeedback
     */
    public Mfeedback getNewMfeedback();

    /**
     * EN: Get an Mfeedback by its ID.<br>
     * DE: Gibt ein Mfeedback anhand seiner ID zurueck.<br>
     *
     * @param filId / the persistence identifier / der PrimaerKey
     * @return Mfeedback / Mfeedback
     */
    public Mfeedback getMfeedbackById(Long filId);

    /**
     * EN: Get an Mfeedback object by its ID.<br>
     * DE: Gibt ein Mfeedback Objekt anhand seiner ID zurueck.<br>
     *
     * @param fil_nr / the mfeedback number / die Niederlassungs Nummer
     * @return Mfeedback / Mfeedback
     */
    public Mfeedback getMfeedbackByFilNr(String fil_nr);

    /**
     * EN: Get a list of all Mfeedbacks.<br>
     * DE: Gibt eine Liste aller Mfeedbacks zurueck.<br>
     *
     * @return List of Mfeedbacks / Liste von Mfeedbacks
     */
    public List<Mfeedback> getAllMfeedbacks();

    /**
     * EN: Get the count of all Mfeedbacks.<br>
     * DE: Gibt die Anzahl aller Mfeedbacks zurueck.<br>
     *
     * @return int
     */
    public int getCountAllMfeedbacks();

    /**
     * EN: Gets a list of Mfeedbacks where the city name contains the %string% .<br>
     * DE: Gibt eine Liste aller Mfeedbacks zurueck bei denen der Stadtname
     * %string% enthaelt.<br>
     *
     * @param string Name of the city / Stadtnamen
     * @return List of Mfeedbacks / Liste of Mfeedbacks
     */
    public List<Mfeedback> getMfeedbacksLikeCity(String string);

    /**
     * EN: Gets a list of Mfeedbacks where the mfeedback name1 contains the %string% .<br>
     * DE: Gibt eine Liste aller Mfeedbacks zurueck bei denen der Mfeedback Name1
     * %string% enthaelt.<br>
     *
     * @param string Name1 of the mfeedback / Name1 vom Mfeedback
     * @return List of Mfeedbacks / Liste of Mfeedbacks
     */
    public List<Mfeedback> getMfeedbacksLikeName1(String string);

    /**
     * EN: Gets a list of Mfeedbacks where the mfeedback number contains the %string%
     * .<br>
     * DE: Gibt eine Liste aller Mfeedbacks zurueck bei denen die Mfeedback Nummer
     * %string% enthaelt.<br>
     *
     * @param string Number of the mfeedback / Nummer vom Mfeedback
     * @return List of Mfeedbacks / Liste of Mfeedbacks
     */
    public List<Mfeedback> getMfeedbacksLikeNo(String string);

    /**
     * EN: Deletes an Mfeedback by its Id.<br>
     * DE: Loescht ein Mfeedback anhand seiner Id.<br>
     *
     * @param id / the persistence identifier / der PrimaerKey
     */
    public void deleteMfeedbackById(long id);

    /**
     * EN: Saves new or updates an Mfeedback.<br>
     * DE: Speichert neu oder aktualisiert ein Mfeedback.<br>
     */
    public void saveOrUpdate(Mfeedback entity);

    /**
     * EN: Deletes an Mfeedback.<br>
     * DE: Loescht ein Mfeedback.<br>
     */
    public void delete(Mfeedback entity);

    /**
     * EN: Saves an Mfeedback.<br>
     * DE: Speichert ein Mfeedback.<br>
     */
    public void save(Mfeedback entity);

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