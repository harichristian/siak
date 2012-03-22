package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mhari;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/8/12
 * Time: 7:42 PM
 * To change this template use File | Settings | File Templates.
 */
public interface HariDAO {
    /**
     * EN: Get a new Mhari object.<br>
     *
     * @return Mhari
     */
    public Mhari getNewHari();

    /**
     * EN: Get an Mhari by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Mhari
     */
    public Mhari getHariById(int id);

    /**
     * EN: Get an Mhari object by its Code.<br>
     *
     * @param code / the Mhari Code
     * @return Mhari
     */
    public Mhari getHariByCode(String code);

    /**
     * EN: Get a list of all Mhari.<br>
     *
     * @return List of Mhari
     */
    public List<Mhari> getAllHaris();

    /**
     * EN: Get the count of all Mhari.<br>
     *
     * @return int
     */
    public int getCountAllHaris();

    /**
     * EN: Gets a list of Mhari where the Mhari name contains the %string% .<br>
     *
     * @param name Name of the Mhari
     * @return List of Mhari
     */
    public List<Mhari> getHarisLikeName(String name);

    /**
     * EN: Deletes an Mhari by its Id.<br>
     *
     * @param id / the persistence identifier
     */
    public void deleteHariById(int id);

    /**
     * EN: Saves new or updates an Mhari.<br>
     */
    public void saveOrUpdate(Mhari entity);

    /**
     * EN: Deletes an Mhari.<br>
     */
    public void delete(Mhari entity);

    /**
     * EN: Saves an Mhari.<br>
     */
    public void save(Mhari entity);

    public ResultObject getAllHariLikeText(String text, int start, int pageSize);
}
