package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mprov;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/19/12
 * Time: 11:12 AM
 * To change this template use File | Settings | File Templates.
 */
public interface MprovDAO { /**
     * EN: Get a new Mprov object.<br>
     * DE: Gibt ein neues Mprov (Niederlassung) Objekt zurueck.<br>
     *
     * @return Mprov
     */
    public Mprov getNewMprov();

    /**
     * EN: Get an Mprov by its ID.<br>
     * DE: Gibt ein Mprov anhand seiner ID zurueck.<br>
     *
     * @param filId / the persistence identifier / der PrimaerKey
     * @return Mprov / Mprov
     */
    public Mprov getMprovById(Long filId);

    /**
     * EN: Get an Mprov object by its ID.<br>
     * DE: Gibt ein Mprov Objekt anhand seiner ID zurueck.<br>
     *
     * @param fil_nr / the mprov number / die Niederlassungs Nummer
     * @return Mprov / Mprov
     */
    public Mprov getMprovByFilNr(String fil_nr);

    /**
     * EN: Get a list of all Mprovs.<br>
     * DE: Gibt eine Liste aller Mprovs zurueck.<br>
     *
     * @return List of Mprovs / Liste von Mprovs
     */
    public List<Mprov> getAllMprovs();

    /**
     * EN: Get the count of all Mprovs.<br>
     * DE: Gibt die Anzahl aller Mprovs zurueck.<br>
     *
     * @return int
     */
    public int getCountAllMprovs();

    /**
     * EN: Gets a list of Mprovs where the city name contains the %string% .<br>
     * DE: Gibt eine Liste aller Mprovs zurueck bei denen der Stadtname
     * %string% enthaelt.<br>
     *
     * @param string Name of the city / Stadtnamen
     * @return List of Mprovs / Liste of Mprovs
     */
    public List<Mprov> getMprovsLikeCity(String string);

    /**
     * EN: Gets a list of Mprovs where the mprov name1 contains the %string% .<br>
     * DE: Gibt eine Liste aller Mprovs zurueck bei denen der Mprov Name1
     * %string% enthaelt.<br>
     *
     * @param string Name1 of the mprov / Name1 vom Mprov
     * @return List of Mprovs / Liste of Mprovs
     */
    public List<Mprov> getMprovsLikeName1(String string);

    /**
     * EN: Gets a list of Mprovs where the mprov number contains the %string%
     * .<br>
     * DE: Gibt eine Liste aller Mprovs zurueck bei denen die Mprov Nummer
     * %string% enthaelt.<br>
     *
     * @param string Number of the mprov / Nummer vom Mprov
     * @return List of Mprovs / Liste of Mprovs
     */
    public List<Mprov> getMprovsLikeNo(String string);

    /**
     * EN: Deletes an Mprov by its Id.<br>
     * DE: Loescht ein Mprov anhand seiner Id.<br>
     *
     * @param id / the persistence identifier / der PrimaerKey
     */
    public void deleteMprovById(long id);

    /**
     * EN: Saves new or updates an Mprov.<br>
     * DE: Speichert neu oder aktualisiert ein Mprov.<br>
     */
    public void saveOrUpdate(Mprov entity);

    /**
     * EN: Deletes an Mprov.<br>
     * DE: Loescht ein Mprov.<br>
     */
    public void delete(Mprov entity);

    /**
     * EN: Saves an Mprov.<br>
     * DE: Speichert ein Mprov.<br>
     */
    public void save(Mprov entity);

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
