package id.ac.idu.irs.dao;

import id.ac.idu.backend.model.Tirspasca;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/11/12
 * Time: 2:43 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IrsDAO {
    /**
     * EN: Get a new Tirspasca object.<br>
     *
     * @return Tirspasca
     */
    public Tirspasca getNewIrs();

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
     * EN: Get the count of all Tirspasca.<br>
     *
     * @return int
     */
    public int getCountAll();

    /**
     * EN: Get an Tirspasca object by its Term.<br>
     *
     * @param string / the Tirspasca Term
     * @return Tirspasca
     */
    public List<Tirspasca> getIrsLikeTerm(String string);

    /**
     * EN: Gets a list of Tirspasca where the Tirspasca Kelompok contains the %string% .<br>
     *
     * @param string Kelompok of the Tirspasca
     * @return List of Tirspasca
     */
    public List<Tirspasca> getIrsLikeKelompok(String string);

    /**
     * EN: Gets a list of Tirspasca where the Tirspasca mahasiswa name contains the %string% .<br>
     *
     * @param string Tirspasca of the Tirspasca
     * @return List of Tirspasca
     */
    public List<Tirspasca> getIrsLikeMahasiswaName(String string);

    /**
     * EN: Deletes an Tirspasca by its Id.<br>
     *
     * @param id / the persistence identifier
     */
    public void deleteIrsById(int id);

    /**
     * EN: Saves new or updates an Tirspasca.<br>
     */
    public void saveOrUpdate(Tirspasca entity);

    /**
     * EN: Deletes an Tirspasca.<br>
     */
    public void delete(Tirspasca entity);

    /**
     * EN: Saves an Tirspasca.<br>
     */
    public void save(Tirspasca entity);
}
