package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mjenjang;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/8/12
 * Time: 6:54 AM
 * To change this template use File | Settings | File Templates.
 */
public interface JenjangDAO {
    /**
     * EN: Get a new Mjenjang object.<br>
     *
     * @return Mjenjang
     */
    public Mjenjang getNewJenjang();

    /**
     * EN: Get an Mjenjang by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Mjenjang
     */
    public Mjenjang getJenjangById(int id);

    /**
     * EN: Get an Mjenjang object by its Code.<br>
     *
     * @param code / the Mjenjang Code
     * @return Mjenjang
     */
    public Mjenjang getJenjangByCode(String code);

    /**
     * EN: Get a list of all Mjenjang.<br>
     *
     * @return List of Mjenjang
     */
    public List<Mjenjang> getAllJenjangs();

    /**
     * EN: Get the count of all Mjenjang.<br>
     *
     * @return int
     */
    public int getCountAllJenjangs();

    /**
     * EN: Gets a list of Mjenjang where the Mjenjang name contains the %string% .<br>
     *
     * @param name Name of the Mjenjang
     * @return List of Mjenjang
     */
    public List<Mjenjang> getJenjangsLikeName(String name);

    /**
     * EN: Gets a list of Mjenjang where the Mjenjang singkatan contains the %string% .<br>
     *
     * @param string Singkatan of the Mjenjang
     * @return List of Mjenjang
     */
    public List<Mjenjang> getJenjangsLikeSingkatan(String string);

    /**
     * EN: Deletes an Mjenjang by its Id.<br>
     *
     * @param id / the persistence identifier
     */
    public void deleteJenjangById(int id);

    /**
     * EN: Saves new or updates an Mjenjang.<br>
     */
    public void saveOrUpdate(Mjenjang entity);

    /**
     * EN: Deletes an Mjenjang.<br>
     */
    public void delete(Mjenjang entity);

    /**
     * EN: Saves an Mjenjang.<br>
     */
    public void save(Mjenjang entity);

    public void initialize(Mjenjang obj);

    public ResultObject getAllJenjangLikeText(String text, int start, int pageSize);
}
