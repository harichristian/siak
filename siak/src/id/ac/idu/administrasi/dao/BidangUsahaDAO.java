package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mbidangusaha;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 4/13/12
 * Time: 6:21 AM
 * To change this template use File | Settings | File Templates.
 */
public interface BidangUsahaDAO {
     /**
     * Create a new BidangUsaha
     *
     * @return MbidangUsaha
     */
    public Mbidangusaha getNewBidangusaha();

    /**
     * Get a BidangUSaha by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return MbidangUsaha
     */
    public Mbidangusaha getBidangusahaById(int id);

    /**
     * Get a HistKerja by name.<br>
     *
     * @param name
     * @return Mjabatan
     */
    public Mbidangusaha getBidangusahaByName(String name);

    /**
     * Get a list of all Bidangusaha.<br>
     *
     * @return List of Bidangusaha
     */
    public List<Mbidangusaha> getAllBidangusaha();

    /**
     * Get the count of all Bidangusaha.<br>
     *
     * @return int
     */
    public int getCountAllBidangusaha();

    /**
     * Deletes Bidangusaha by its Id.<br>
     *
     * @param id
     */
    public void deleteBidangusahaById(int id);

    /**
     * Saves new or updates an Bidangusaha.<br>
     */
    public void saveOrUpdate(Mbidangusaha entity);

    /**
     * Deletes an Bidangusaha.
     */
    public void delete(Mbidangusaha entity);

    /**
     * Saves an Mbidangusaha.<br>
     */
    public void save(Mbidangusaha entity);

     /**
     * EN: Get a paged list of all Branches.<br>
     * DE: Gibt eine paged Liste aller Branchen zurueck.<br>
     *
     * @param text     Text for search / SuchText
     * @param start    StartRecord / Start Datensatz
     * @param pageSize Count of Records / Anzahl Datensaetze
     * @return List of YoutubeLinks / Liste von YoutubeLinks
     */
    public ResultObject getAllBidangusahaLikeText(String text, int start, int pageSize);
}