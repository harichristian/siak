package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mprodi;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/7/12
 * Time: 8:23 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProdiDAO {
    /**
     * EN: Get a new Mprodi object.<br>
     *
     * @return Mprodi
     */
    public Mprodi getNewProdi();

    /**
     * EN: Get an Mprodi by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Mprodi
     */
    public Mprodi getProdiById(int id);

    /**
     * EN: Get an Mprodi object by its Code.<br>
     *
     * @param code / the Mprodi Code
     * @return Mprodi
     */
    public Mprodi getProdiByCode(String code);

    /**
     * EN: Get a list of all Mprodi.<br>
     *
     * @return List of Mprodi
     */
    public List<Mprodi> getAllProdis();

    /**
     * EN: Get the count of all Mprodi.<br>
     *
     * @return int
     */
    public int getCountAllProdis();

    /**
     * EN: Gets a list of Mprodi where the Mprodi name contains the %string% .<br>
     *
     * @param name Name of the Mprodi
     * @return List of Mprodi
     */
    public List<Mprodi> getProdisLikeName(String name);

    /**
     * EN: Gets a list of Mprodi where the Mprodi singkatan contains the %string% .<br>
     *
     * @param string Singkatan of the Mprodi
     * @return List of Mprodi
     */
    public List<Mprodi> getProdisLikeSingkatan(String string);

    /**
     * EN: Gets a list of Mprodi where the Mprodi name contains the %string%
     * .<br>
     *
     * @param string Status of the Mprodi
     * @return List of Mprodi
     */
    public List<Mprodi> getProdiLikeStatus(String string);

    /**
     * EN: Deletes an Mprodi by its Id.<br>
     *
     * @param id / the persistence identifier
     */
    public void deleteProdiById(int id);

    /**
     * EN: Saves new or updates an Mprodi.<br>
     */
    public void saveOrUpdate(Mprodi entity);

    /**
     * EN: Deletes an Mprodi.<br>
     */
    public void delete(Mprodi entity);

    /**
     * EN: Saves an Mprodi.<br>
     */
    public void save(Mprodi entity);

    /**
     * EN: Load the relations data for an Entity.<br>
     */
    public void initialize(Mprodi prodi);

    public ResultObject getAllProdiLikeText(String text, int start, int pageSize);
}
