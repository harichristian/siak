package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mthajar;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 05/04/12
 * Time: 14:28
 * To change this template use File | Settings | File Templates.
 */
public interface ThajarDAO {

    /**
     * Create a new Thajar
     *
     * @return Mthajar
     */
    public Mthajar getNewThajar();

    /**
     * Get a Thajar by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Mthajar
     */
    public Mthajar getThajarById(int id);

    /**
     * Get a Thajar by name.<br>
     *
     * @param kode
     * @return Mthajar
     */
    public Mthajar getThajarByKode(String kode);

    /**
     * Get a list of all Thajar.<br>
     *
     * @return List of Thajar
     */
    public List<Mthajar> getAllThajar();

    /**
     * Get the count of all Thajar.<br>
     *
     * @return int
     */
    public int getCountAllThajar();

    /**
     * Saves new or updates an Thajar.<br>
     */
    public void saveOrUpdate(Mthajar entity);

    /**
     * Deletes an Thajar.
     */
    public void delete(Mthajar entity);

    /**
     * Saves an Thajar.<br>
     */
    public void save(Mthajar entity);

    public ResultObject getAllThajarLikeText(String text, int start, int pageSize);
}