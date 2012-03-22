package id.ac.idu.administrasi.service;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mhari;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/8/12
 * Time: 7:50 PM
 * To change this template use File | Settings | File Templates.
 */
public interface HariService {
    /**
     * EN: Get a new Mhari object.<br>
     *
     * @return Mhari
     */
    public Mhari getNewHari();

    /**
     * EN: Get the count of all Mhari.<br>
     *
     * @return int
     */
    public int getCountAllHaris();

    /**
     * EN: Get an Mhari by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Mhari
     */
    public Mhari getHariByID(int id);

    /**
     * EN: Get a list of all Mhari.<br>
     *
     * @return List of Mhari
     */
    public List<Mhari> getAllHaris();

    /**
     * EN: Gets a list of Mhari where the Mhari name contains the %string% .<br>
     *
     * @param string Name of the Mhari
     * @return List of Mhari
     */
    public List<Mhari> getHarisLikeName(String string);

    /**
     * EN: Saves new or updates an Mhari.<br>
     */
    public void saveOrUpdate(Mhari entity);

    /**
     * EN: Deletes an Mhari.<br>
     */
    public void delete(Mhari entity);

    public ResultObject getAllHariLikeText(String text, int start, int pageSize);
}
