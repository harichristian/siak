package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mjabatan;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 05/03/12
 * Time: 19:05
 */
public interface JabatanDAO {

    /**
     * Create a new Jabatan
     *
     * @return Mjabatan
     */
    public Mjabatan getNewJabatan();

    /**
     * Get a Jabatan by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Mjabatan
     */
    public Mjabatan getJabatanById(int id);

    /**
     * Get a Jabatan by name.<br>
     *
     * @param name
     * @return Mjabatan
     */
    public Mjabatan getJabatanByName(String name);

    /**
     * Get a list of all Jabatan.<br>
     *
     * @return List of Jabatan
     */
    public List<Mjabatan> getAllJabatan();

    /**
     * Get the count of all Jabatan.<br>
     *
     * @return int
     */
    public int getCountAllJabatan();

    /**
     * Deletes Jabatan by its Id.<br>
     *
     * @param id
     */
    public void deleteJabatanById(int id);

    /**
     * Saves new or updates an Jabatan.<br>
     */
    public void saveOrUpdate(Mjabatan entity);

    /**
     * Deletes an Jabatan.
     */
    public void delete(Mjabatan entity);

    /**
     * Saves an Jabatan.<br>
     */
    public void save(Mjabatan entity);

     /**
     * EN: Get a paged list of all Branches.<br>
     * DE: Gibt eine paged Liste aller Branchen zurueck.<br>
     *
     * @param text     Text for search / SuchText
     * @param start    StartRecord / Start Datensatz
     * @param pageSize Count of Records / Anzahl Datensaetze
     * @return List of YoutubeLinks / Liste von YoutubeLinks
     */
    public ResultObject getAllJabatanLikeText(String text, int start, int pageSize);

    public Mjabatan getJabatanByCode(String code);

}
