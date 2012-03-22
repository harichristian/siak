package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.model.Mpangkatgolongan;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/8/12
 * Time: 10:13 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PangkatDAO {
    /**
     * EN: Get a new Mpangkatgolongan object.<br>
     *
     * @return Mpangkatgolongan
     */
    public Mpangkatgolongan getNewPangkat();

    /**
     * EN: Get an Mpangkatgolongan by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Mpangkatgolongan
     */
    public Mpangkatgolongan getPangkatById(int id);

    /**
     * EN: Get an Mpangkatgolongan object by its Code.<br>
     *
     * @param code / the Mpangkatgolongan Code
     * @return Mpangkatgolongan
     */
    public Mpangkatgolongan getPangkatByCode(String code);

    /**
     * EN: Get a list of all Mpangkatgolongan.<br>
     *
     * @return List of Mpangkatgolongan
     */
    public List<Mpangkatgolongan> getAllPangkats();

    /**
     * EN: Get the count of all Mpangkatgolongan.<br>
     *
     * @return int
     */
    public int getCountAllPangkats();

    /**
     * EN: Gets a list of Mpangkatgolongan where the Mpangkatgolongan name contains the %string% .<br>
     *
     * @param name Name of the Mpangkatgolongan
     * @return List of Mpangkatgolongan
     */
    public List<Mpangkatgolongan> getPangkatsLikeName(String name);

    /**
     * EN: Deletes an Mpangkatgolongan by its Id.<br>
     *
     * @param id / the persistence identifier
     */
    public void deletePangkatById(int id);

    /**
     * EN: Saves new or updates an Mpangkatgolongan.<br>
     */
    public void saveOrUpdate(Mpangkatgolongan entity);

    /**
     * EN: Deletes an Mpangkatgolongan.<br>
     */
    public void delete(Mpangkatgolongan entity);

    /**
     * EN: Saves an Mpangkatgolongan.<br>
     */
    public void save(Mpangkatgolongan entity);
}
