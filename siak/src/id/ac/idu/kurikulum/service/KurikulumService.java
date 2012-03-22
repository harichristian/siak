package id.ac.idu.kurikulum.service;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mkurikulum;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/9/12
 * Time: 3:04 AM
 * To change this template use File | Settings | File Templates.
 */
public interface KurikulumService {
    /**
     * EN: Get a new Mkurikulum object.<br>
     *
     * @return Mkurikulum
     */
    public Mkurikulum getNewKurikulum();

    /**
     * EN: Get the count of all Mkurikulum.<br>
     *
     * @return int
     */
    public int getCountAllKurikulums();

    /**
     * EN: Get an Mkurikulum by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Mkurikulum
     */
    public Mkurikulum getKurikulumById(int id);

    /**
     * EN: Get a list of all Mkurikulum.<br>
     *
     * @return List of Mkurikulum
     */
    public List<Mkurikulum> getAllKurikulums();

    /**
     * EN: Gets a list of Mkurikulum where the Mkurikulum prodi contains the %string% .<br>
     *
     * @param string Prodi of the Mkurikulum
     * @return List of Mkurikulum
     */
    public List<Mkurikulum> getKurikulumsLikeProdi(String string);

    /**
     * EN: Gets a list of Mkurikulum where the Mkurikulum cohort contains the %string% .<br>
     *
     * @param string Cohort of the Mkurikulum
     * @return List of Mkurikulum
     */
    public List<Mkurikulum> getKurikulumLikeCohort(String string);

    /**
     * EN: Saves new or updates an Mkurikulum.<br>
     */
    public void saveOrUpdate(Mkurikulum entity);

    /**
     * EN: Deletes an Mkurikulum.<br>
     */
    public void delete(Mkurikulum entity);

    public ResultObject getAllKurikulumLikeText(String text, int start, int pageSize);
}
