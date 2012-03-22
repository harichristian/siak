package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mruang;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Deni Saputra
 * Date: 3/6/12
 * Time: 4:20 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MruangDAO {

    public Mruang getNewRuang();


    public Mruang getMruangById(int filId);

    /**
     * EN: Get an Mruang object by its ID.<br>
     * DE: Gibt ein Mruang Objekt anhand seiner ID zurueck.<br>
     *
     * @param fil_nr / the mruang number / die Niederlassungs Nummer
     * @return Mruang / Mruang
     */
    public Mruang getMruangByFilNr(String fil_nr);

    /**
     * EN: Get a list of all Mruangs.<br>
     * DE: Gibt eine Liste aller Mruangs zurueck.<br>
     *
     * @return List of Mruangs / Liste von Mruangs
     */
    public List<Mruang> getAllMruangs();

    /**
     * EN: Get the count of all Mruangs.<br>
     * DE: Gibt die Anzahl aller Mruangs zurueck.<br>
     *
     * @return int
     */
    public int getCountAllMruangs();

    /**
     * EN: Gets a list of Mruangs where the city name contains the %string% .<br>
     * DE: Gibt eine Liste aller Mruangs zurueck bei denen der Stadtname
     * %string% enthaelt.<br>
     *
     * @param string Name of the city / Stadtnamen
     * @return List of Mruangs / Liste of Mruangs
     */
    public List<Mruang> getMruangsLikeCity(String string);

    /**
     * EN: Gets a list of Mruangs where the mruang name1 contains the %string% .<br>
     * DE: Gibt eine Liste aller Mruangs zurueck bei denen der Mruang Name1
     * %string% enthaelt.<br>
     *
     * @param string Name1 of the mruang / Name1 vom Mruang
     * @return List of Mruangs / Liste of Mruangs
     */
    public List<Mruang> getMruangsLikeName1(String string);

    /**
     * EN: Gets a list of Mruangs where the mruang number contains the %string%
     * .<br>
     * DE: Gibt eine Liste aller Mruangs zurueck bei denen die Mruang Nummer
     * %string% enthaelt.<br>
     *
     * @param string Number of the mruang / Nummer vom Mruang
     * @return List of Mruangs / Liste of Mruangs
     */
    public List<Mruang> getMruangsLikeNo(String string);

    /**
     * EN: Deletes an Mruang by its Id.<br>
     * DE: Loescht ein Mruang anhand seiner Id.<br>
     *
     * @param id / the persistence identifier / der PrimaerKey
     */
    public void deleteMruangById(int id);

    /**
     * EN: Saves new or updates an Mruang.<br>
     * DE: Speichert neu oder aktualisiert ein Mruang.<br>
     */
    public void saveOrUpdate(Mruang entity);

    /**
     * EN: Deletes an Mruang.<br>
     * DE: Loescht ein Mruang.<br>
     */
    public void delete(Mruang entity);

    /**
     * EN: Saves an Mruang.<br>
     * DE: Speichert ein Mruang.<br>
     */
    public void save(Mruang entity);

     /**
     * EN: Get a paged list of all Branches.<br>
     * DE: Gibt eine paged Liste aller Branchen zurueck.<br>
     *
     * @param text     Text for search / SuchText
     * @param start    StartRecord / Start Datensatz
     * @param pageSize Count of Records / Anzahl Datensaetze
     * @return List of YoutubeLinks / Liste von YoutubeLinks
     */
    public ResultObject getAllMruangLikeText(String text, int start, int pageSize);

}
