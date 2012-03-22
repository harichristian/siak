package id.ac.idu.administrasi.service;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mprodi;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/7/12
 * Time: 8:54 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProdiService {
    /**
     * EN: Get a new Mprodi object.<br>
     *
     * @return Mprodi
     */
    public Mprodi getNewProdi();

    /**
     * EN: Get the count of all Mprodi.<br>
     *
     * @return int
     */
    public int getCountAllProdis();

    /**
     * EN: Get an Mprodi by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Mprodi
     */
    public Mprodi getProdiByID(int id);

    /**
     * EN: Get a list of all Mprodi.<br>
     *
     * @return List of Mprodi
     */
    public List<Mprodi> getAllProdis();

    /**
     * EN: Gets a list of Mprodi where the Mprodi name contains the %string% .<br>
     *
     * @param string Name of the Mprodi
     * @return List of Mprodi
     */
    public List<Mprodi> getProdisLikeName(String string);

    /**
     * EN: Gets a list of Mprodi where the Mprodi singkatan contains the %string% .<br>
     *
     * @param string Singkatan of the Muniv
     * @return List of Mprodi
     */
    public List<Mprodi> getProdisLikeSingkatan(String string);

    /**
     * EN: Gets a list of Mprodi where the Mprodi status contains the %string%
     * .<br>
     *
     * @param string Status of the Mprodi
     * @return List of Mprodi
     */
    public List<Mprodi> getProdisLikeStatus(String string);

    /**
     * EN: Saves new or updates an Mprodi.<br>
     */
    public void saveOrUpdate(Mprodi entity);

    /**
     * EN: Deletes an Mprodi.<br>
     */
    public void delete(Mprodi entity);

    /**
     * EN: Get a list of all Mjenjang.<br>
     *
     * @return List of Mjenjang
     */
    //public List<Mjenjang> getAllJenjangs();

    public ResultObject getAllProdiLikeText(String text, int start, int pageSize);
}
