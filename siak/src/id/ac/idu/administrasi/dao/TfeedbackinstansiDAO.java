package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.model.Tfeedbackinstansi;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/12/12
 * Time: 8:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TfeedbackinstansiDAO {
       /**
     * EN: Get a new Tfeedbackinstansi object.<br>
     * DE: Gibt ein neues Tfeedbackinstansi (Niederlassung) Objekt zurueck.<br>
     *
     * @return Tfeedbackinstansi
     */
    public Tfeedbackinstansi getNewTfeedbackinstansi();

    /**
     * EN: Get an Tfeedbackinstansi by its ID.<br>
     * DE: Gibt ein Tfeedbackinstansi anhand seiner ID zurueck.<br>
     *
     * @param filId / the persistence identifier / der PrimaerKey
     * @return Tfeedbackinstansi / Tfeedbackinstansi
     */
    public Tfeedbackinstansi getTfeedbackinstansiById(Long filId);

    /**
     * EN: Get an Tfeedbackinstansi object by its ID.<br>
     * DE: Gibt ein Tfeedbackinstansi Objekt anhand seiner ID zurueck.<br>
     *
     * @param fil_nr / the tfeedbackinstansi number / die Niederlassungs Nummer
     * @return Tfeedbackinstansi / Tfeedbackinstansi
     */
    public Tfeedbackinstansi getTfeedbackinstansiByFilNr(String fil_nr);

    /**
     * EN: Get a list of all Tfeedbackinstansis.<br>
     * DE: Gibt eine Liste aller Tfeedbackinstansis zurueck.<br>
     *
     * @return List of Tfeedbackinstansis / Liste von Tfeedbackinstansis
     */
    public List<Tfeedbackinstansi> getAllTfeedbackinstansis();

    /**
     * EN: Get the count of all Tfeedbackinstansis.<br>
     * DE: Gibt die Anzahl aller Tfeedbackinstansis zurueck.<br>
     *
     * @return int
     */
    public int getCountAllTfeedbackinstansis();

    /**
     * EN: Gets a list of Tfeedbackinstansis where the city name contains the %string% .<br>
     * DE: Gibt eine Liste aller Tfeedbackinstansis zurueck bei denen der Stadtname
     * %string% enthaelt.<br>
     *
     * @param string Name of the city / Stadtnamen
     * @return List of Tfeedbackinstansis / Liste of Tfeedbackinstansis
     */
    public List<Tfeedbackinstansi> getTfeedbackinstansisLikeCity(String string);

    /**
     * EN: Gets a list of Tfeedbackinstansis where the tfeedbackinstansi name1 contains the %string% .<br>
     * DE: Gibt eine Liste aller Tfeedbackinstansis zurueck bei denen der Tfeedbackinstansi Name1
     * %string% enthaelt.<br>
     *
     * @param string Name1 of the tfeedbackinstansi / Name1 vom Tfeedbackinstansi
     * @return List of Tfeedbackinstansis / Liste of Tfeedbackinstansis
     */
    public List<Tfeedbackinstansi> getTfeedbackinstansisLikeName1(String string);

    /**
     * EN: Gets a list of Tfeedbackinstansis where the tfeedbackinstansi number contains the %string%
     * .<br>
     * DE: Gibt eine Liste aller Tfeedbackinstansis zurueck bei denen die Tfeedbackinstansi Nummer
     * %string% enthaelt.<br>
     *
     * @param string Number of the tfeedbackinstansi / Nummer vom Tfeedbackinstansi
     * @return List of Tfeedbackinstansis / Liste of Tfeedbackinstansis
     */
    public List<Tfeedbackinstansi> getTfeedbackinstansisLikeNo(String string);

    /**
     * EN: Deletes an Tfeedbackinstansi by its Id.<br>
     * DE: Loescht ein Tfeedbackinstansi anhand seiner Id.<br>
     *
     * @param id / the persistence identifier / der PrimaerKey
     */
    public void deleteTfeedbackinstansiById(long id);

    /**
     * EN: Saves new or updates an Tfeedbackinstansi.<br>
     * DE: Speichert neu oder aktualisiert ein Tfeedbackinstansi.<br>
     */
    public void saveOrUpdate(Tfeedbackinstansi entity);

    /**
     * EN: Deletes an Tfeedbackinstansi.<br>
     * DE: Loescht ein Tfeedbackinstansi.<br>
     */
    public void delete(Tfeedbackinstansi entity);

    /**
     * EN: Saves an Tfeedbackinstansi.<br>
     * DE: Speichert ein Tfeedbackinstansi.<br>
     */
    public void save(Tfeedbackinstansi entity);
}
