package id.ac.idu.irs.dao;

import id.ac.idu.backend.model.Mprodi;
import id.ac.idu.backend.model.Tpaketkuliah;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/13/12
 * Time: 10:14 AM
 * To change this template use File | Settings | File Templates.
 */
public interface PaketDAO {
    /**
     * EN: Get a new Tpaketkuliah object.<br>
     *
     * @return Tpaketkuliah
     */
    public Tpaketkuliah getNewPaket();

    /**
     * EN: Get an Tpaketkuliah by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Tpaketkuliah
     */
    public Tpaketkuliah getPaketById(int id);

    /**
     * EN: Get a list of all Tpaketkuliah.<br>
     *
     * @return List of Tpaketkuliah
     */
    public List<Tpaketkuliah> getAll();

    /**
     * EN: Get the count of all Tpaketkuliah.<br>
     *
     * @return int
     */
    public int getCountAll();

    /**
     * EN: Get an Tpaketkuliah object by its Term.<br>
     *
     * @param string / the Tpaketkuliah Term
     * @return Tpaketkuliah
     */
    public List<Tpaketkuliah> getPaketLikeTerm(String string);

    /**
     * EN: Gets a list of Tpaketkuliah where the Tpaketkuliah Thajar contains the %string% .<br>
     *
     * @param string Thajar of the Tpaketkuliah
     * @return List of Tpaketkuliah
     */
    public List<Tpaketkuliah> getPaketLikeThajar(String string);

    /**
     * EN: Gets a list of Tpaketkuliah where the Tpaketkuliah prodi name contains the %string% .<br>
     *
     * @param string Tpaketkuliah of the Tpaketkuliah
     * @return List of Tirspasca
     */
    public List<Tpaketkuliah> getPaketLikeProdi(String string);

    public List<Tpaketkuliah> getPaketForTransaction(Mprodi prodi, String term, String thajar);

    /**
     * EN: Deletes an Tpaketkuliah by its Id.<br>
     *
     * @param id / the persistence identifier
     */
    public void deletePaketById(int id);

    /**
     * EN: Saves new or updates an Tpaketkuliah.<br>
     */
    public void saveOrUpdate(Tpaketkuliah entity);

    /**
     * EN: Deletes an Tpaketkuliah.<br>
     */
    public void delete(Tpaketkuliah entity);

    /**
     * EN: Saves an Tpaketkuliah.<br>
     */
    public void save(Tpaketkuliah entity);
}
