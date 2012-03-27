/**
 *
 */
package id.ac.idu.administrasi.service;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Muniv;

import java.util.List;

/**
 * @author valeo
 */
public interface MunivService {
    /**
     * EN: Get a new Muniv object.<br>
     *
     * @return Office
     */
    public Muniv getNewMuniv();

    /**
     * EN: Get the count of all Muniv.<br>
     *
     * @return int
     */
    public int getCountAllMunivs();

    /**
     * EN: Get an Office by its ID.<br>
     *
     * @param munivId / the persistence identifier
     * @return Muniv
     */
    public Muniv getMunivByID(Long munivId);

    /**
     * EN: Get a list of all Muniv.<br>
     *
     * @return List of Muniv
     */
    public List<Muniv> getAllMunivs();

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
     * EN: Gets a list of Munivs where the Muniv code contains the %string%
     * .<br>
     *
     * @param string Code of the Muniv
     * @return List of Munivs
     */
    public List<Muniv> getMunivsLikeCode(String string);

    /**
     * EN: Saves new or updates an Muniv.<br>
     */
    public void saveOrUpdate(Muniv muniv);

    /**
     * EN: Deletes an Muniv.<br>
     */
    public void delete(Muniv muniv);

    public ResultObject getAllUnivLikeText(String text, int start, int pageSize);
}
