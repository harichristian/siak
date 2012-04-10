package id.ac.idu.irs.service;

import id.ac.idu.backend.model.Mmahasiswa;
import id.ac.idu.backend.model.Tirspasca;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/11/12
 * Time: 5:39 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IrsService {
    /**
     * EN: Get a new Tirspasca object.<br>
     *
     * @return Tirspasca
     */
    public Tirspasca getNewIrs();

    /**
     * EN: Get the count of all Tirspasca.<br>
     *
     * @return int
     */
    public int getCountAll();

    /**
     * EN: Get an Tirspasca by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Tirspasca
     */
    public Tirspasca getIrsById(int id);

    /**
     * EN: Get a list of all Tirspasca.<br>
     *
     * @return List of Tirspasca
     */
    public List<Tirspasca> getAll();

    /**
     * EN: Gets a list of Tirspasca where the Tirspasca term contains the %string% .<br>
     *
     * @param string Prodi of the Tirspasca
     * @return List of Tirspasca
     */
    public List<Tirspasca> getIrsLikeTerm(String string);

    /**
     * EN: Gets a list of Tirspasca where the Tirspasca kelompok contains the %string% .<br>
     *
     * @param string Cohort of the Tirspasca
     * @return List of Tirspasca
     */
    public List<Tirspasca> getIrsLikeKelompok(String string);

    /**
     * EN: Gets a list of Tirspasca where the Tirspasca mahasiswa name contains the %string% .<br>
     *
     * @param string Cohort of the Tirspasca
     * @return List of Tirspasca
     */
    public List<Tirspasca> getIrsLikeMahasiswaName(String string);

    /**
     * EN: Saves new or updates an Tirspasca.<br>
     */
    public void saveOrUpdate(Tirspasca entity);

    /**
     * EN: Deletes an Tirspasca.<br>
     */
    public void delete(Tirspasca entity);

    public void saveOrUpdatePaket(Tirspasca entity, Mmahasiswa mahasiswa);
}
