package id.ac.idu.administrasi.service;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mjenjang;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/8/12
 * Time: 6:23 PM
 * To change this template use File | Settings | File Templates.
 */
public interface JenjangService {
    /**
     * EN: Get a new Mjenjang object.<br>
     *
     * @return Mjenjang
     */
    public Mjenjang getNewJenjang();

    /**
     * EN: Get the count of all Mjenjang.<br>
     *
     * @return int
     */
    public int getCountAllJenjangs();

    /**
     * EN: Get an Mjenjang by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Mjenjang
     */
    public Mjenjang getJenjangByID(int id);

    /**
     * EN: Get a list of all Mjenjang.<br>
     *
     * @return List of Mjenjang
     */
    public List<Mjenjang> getAllJenjangs();

    /**
     * EN: Gets a list of Mjenjang where the Mjenjang name contains the %string% .<br>
     *
     * @param string Name of the Mjenjang
     * @return List of Mjenjang
     */
    public List<Mjenjang> getJenjangsLikeName(String string);

    /**
     * EN: Gets a list of Mjenjang where the Mjenjang singkatan contains the %string% .<br>
     *
     * @param string Singkatan of the Mjenjang
     * @return List of Mjenjang
     */
    public List<Mjenjang> getJenjangsLikeSingkatan(String string);

    /**
     * EN: Saves new or updates an Mjenjang.<br>
     */
    public void saveOrUpdate(Mjenjang entity);

    /**
     * EN: Deletes an Mjenjang.<br>
     */
    public void delete(Mjenjang entity);

    public ResultObject getAllJenjangLikeText(String text, int start, int pageSize);
}
