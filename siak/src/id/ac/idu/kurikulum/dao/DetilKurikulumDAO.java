package id.ac.idu.kurikulum.dao;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mdetilkurikulum;
import id.ac.idu.backend.model.Tirspasca;

import java.util.List;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 3/31/12
 * Time: 9:50 AM
 */
public interface DetilKurikulumDAO {
    /**
     * EN: Get a new Mdetilkurikulum object.<br>
     *
     * @return Mdetilkurikulum
     */
    public Mdetilkurikulum getNew();

    /**
     * EN: Get an Mdetilkurikulum by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Mdetilkurikulum
     */
    public Mdetilkurikulum getById(int id);

    /**
     * EN: Get an Mdetilkurikulum object by its kurikulum id.<br>
     *
     * @param id / the kurikulum id
     * @return Mdetilkurikulum
     */
    public Mdetilkurikulum getByKurikulumId(int id);

    /**
     * EN: Get a list of all Mdetilkurikulum.<br>
     *
     * @return List of Mdetilkurikulum
     */
    public List<Mdetilkurikulum> getAll();

    /**
     * EN: Get the count of all Mdetilkurikulum.<br>
     *
     * @return int
     */
    public int getCountAll();

    /**
     * EN: Gets a list of Mdetilkurikulum where the prodi name contains the %string% .<br>
     *
     * @param name Name of the Prodi
     * @return List of Mdetilkurikulum
     */
    public List<Mdetilkurikulum> getLikeProdiName(String name);

    /**
     * EN: Gets a list of Mdetilkurikulum where the Mdetilkurikulum cohort contains the %string% .<br>
     *
     * @param string cohort of the Mdetilkurikulum
     * @return List of Mdetilkurikulum
     */
    public List<Mdetilkurikulum> getLikeCohort(String string);

    /**
     * EN: Deletes by its Id.<br>
     *
     * @param id / the persistence identifier
     */
    public void deleteById(int id);

    /**
     * EN: Saves new or updates.<br>
     */
    public void saveOrUpdate(Mdetilkurikulum entity);

    /**
     * EN: Deletes the entity.<br>
     */
    public void delete(Mdetilkurikulum entity);

    /**
     * EN: Saves the entity.<br>
     */
    public void save(Mdetilkurikulum entity);

    public ResultObject getAllLikeText(String text, int start, int pageSize);

    public ResultObject getAllLikeMatakuliah(String text, int start, int pageSize);

    public ResultObject getAllByJoin(String text, String prodi, String term, String thajar, int start, int pageSize);

    public ResultObject getAllByIrs(String text, Tirspasca irs, int start, int pageSize);
}
