/**
 *
 */
package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.model.Muniv;

import java.util.List;

/**
 * @author valeo
 */
public interface MunivDao {
    /**
     * EN: Get a new Muniv object.<br>
     *
     * @return Muniv
     */
    public Muniv getNewMuniv();

    /**
     * EN: Get an Muniv by its ID.<br>
     *
     * @param munivId / the persistence identifier
     * @return Muniv / Muniv
     */
    public Muniv getMunivById(Long munivId);

    /**
     * EN: Get an Muniv object by its Code.<br>
     *
     * @param ckdUniv / the Muniv Code
     * @return Muniv / Muniv
     */
    public Muniv getMunivByCkdUniv(String ckdUniv);

    /**
     * EN: Get a list of all Muniv.<br>
     *
     * @return List of Offices / Liste von Offices
     */
    public List<Muniv> getAllMunivs();

    /**
     * EN: Get the count of all Munivs.<br>
     *
     * @return int
     */
    public int getCountAllMunivs();

    /**
     * EN: Gets a list of Munivs where the Muniv name contains the %string% .<br>
     *
     * @param string Name of the Muniv
     * @return List of Munivs
     */
    public List<Muniv> getMunivsLikeName(String string);

    /**
     * EN: Gets a list of Munivs where the Muniv name1 contains the %string% .<br>
     *
     * @param string Name1 of the Muniv
     * @return List of Munivs
     */
    public List<Muniv> getMunivsLikeName1(String string);

    /**
     * EN: Gets a list of Munivs where the Muniv name contains the %string%
     * .<br>
     *
     * @param string Number of the Muniv
     * @return List of Munivs
     */
    public List<Muniv> getMunivsLikeCode(String string);

    /**
     * EN: Deletes an Muniv by its Id.<br>
     *
     * @param id / the persistence identifier
     */
    public void deleteMunivById(long id);

    /**
     * EN: Saves new or updates an Muniv.<br>
     */
    public void saveOrUpdate(Muniv entity);

    /**
     * EN: Deletes an Muniv.<br>
     */
    public void delete(Muniv entity);

    /**
     * EN: Saves an Muniv.<br>
     */
    public void save(Muniv entity);

}
