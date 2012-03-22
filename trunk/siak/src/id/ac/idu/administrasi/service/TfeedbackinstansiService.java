package id.ac.idu.administrasi.service;

import id.ac.idu.backend.model.Tfeedbackinstansi;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/13/12
 * Time: 1:03 AM
 * To change this template use File | Settings | File Templates.
 */
public interface TfeedbackinstansiService {
    /**
     * EN: Get a new Tfeedbackinstansi object.<br>
     * DE: Gibt ein neues Tfeedbackinstansi (Niederlassung) Objekt zurueck.<br>
     *
     * @return Tfeedbackinstansi
     */
    public Tfeedbackinstansi getNewTfeedbackinstansi();

    /**
     * EN: Get the count of all Tfeedbackinstansis.<br>
     * DE: Gibt die Anzahl aller Tfeedbackinstansis zurueck.<br>
     *
     * @return int
     */
    public int getCountAllTfeedbackinstansis();

    /**
     * EN: Get an Tfeedbackinstansi by its ID.<br>
     * DE: Gibt ein Tfeedbackinstansi anhand seiner ID zurueck.<br>
     *
     * @param fil_nr / the persistence identifier / der PrimaerKey
     * @return Tfeedbackinstansi / Tfeedbackinstansi
     */
    public Tfeedbackinstansi getTfeedbackinstansiByID(Long fil_nr);

    /**
     * EN: Get a list of all Tfeedbackinstansis.<br>
     * DE: Gibt eine Liste aller Tfeedbackinstansis zurueck.<br>
     *
     * @return List of Tfeedbackinstansis / Liste von Tfeedbackinstansis
     */
    public List<Tfeedbackinstansi> getAllTfeedbackinstansis();

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
     * EN: Saves new or updates an Tfeedbackinstansi.<br>
     * DE: Speichert neu oder aktualisiert ein Tfeedbackinstansi.<br>
     */
    public void saveOrUpdate(Tfeedbackinstansi ofice);

    /**
     * EN: Deletes an Tfeedbackinstansi.<br>
     * DE: Loescht ein Tfeedbackinstansi.<br>
     */
    public void delete(Tfeedbackinstansi tfeedbackinstansi);
}
