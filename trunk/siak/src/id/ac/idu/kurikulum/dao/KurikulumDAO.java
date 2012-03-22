package id.ac.idu.kurikulum.dao;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mkurikulum;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/9/12
 * Time: 2:48 AM
 * To change this template use File | Settings | File Templates.
 */
public interface KurikulumDAO {
    /**
     * EN: Get a new Mkurikulum object.<br>
     *
     * @return Mkurikulum
     */
    public Mkurikulum getNewKurikulum();

    /**
     * EN: Get an Mkurikulum by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Mkurikulum
     */
    public Mkurikulum getKurikulumById(int id);

    /**
     * EN: Get an Mkurikulum object by its Code.<br>
     *
     * @param code / the Mkurikulum Code
     * @return Mkurikulum
     */
    public Mkurikulum getKurikulumByCode(String code);

    /**
     * EN: Get a list of all Mkurikulum.<br>
     *
     * @return List of Mkurikulum
     */
    public List<Mkurikulum> getAllKurikulums();

    /**
     * EN: Get the count of all Mkurikulum.<br>
     *
     * @return int
     */
    public int getCountAllKurikulums();

    /**
     * EN: Gets a list of Mkurikulum where the Mkurikulum prodi contains the %string% .<br>
     *
     * @param name Name of the Mkurikulum
     * @return List of Mkurikulum
     */
    public List<Mkurikulum> getKurikulumLikeProdi(String name);

    /**
     * EN: Gets a list of Mkurikulum where the Mkurikulum cohort contains the %string% .<br>
     *
     * @param string Mkurikulum of the Mkurikulum
     * @return List of Mkurikulum
     */
    public List<Mkurikulum> getKurikulumLikeCohort(String string);

    /**
     * EN: Deletes an Mkurikulum by its Id.<br>
     *
     * @param id / the persistence identifier
     */
    public void deleteKurikulumById(int id);

    /**
     * EN: Saves new or updates an Mkurikulum.<br>
     */
    public void saveOrUpdate(Mkurikulum entity);

    /**
     * EN: Deletes an Mkurikulum.<br>
     */
    public void delete(Mkurikulum entity);

    /**
     * EN: Saves an Mkurikulum.<br>
     */
    public void save(Mkurikulum entity);

    public ResultObject getAllKurikulumLikeText(String text, int start, int pageSize);
}
