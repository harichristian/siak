package id.ac.idu.kurikulum.service;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mdetilkurikulum;
import id.ac.idu.backend.model.Tirspasca;

import java.util.List;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 3/31/12
 * Time: 9:51 AM
 */
public interface DetilKurikulumService {
    /**
     * EN: Get a new Mdetilkurikulum object.<br>
     *
     * @return Mdetilkurikulum
     */
    public Mdetilkurikulum getNew();

    /**
     * EN: Get the count of all Mdetilkurikulum.<br>
     *
     * @return int
     */
    public int getCountAll();

    /**
     * EN: Get an Mdetilkurikulum by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Mdetilkurikulum
     */
    public Mdetilkurikulum getById(int id);

    /**
     * EN: Get a list of all Mdetilkurikulum.<br>
     *
     * @return List of Mdetilkurikulum
     */
    public List<Mdetilkurikulum> getAll();

    /**
     * EN: Gets a list of Mdetilkurikulum where the prodi name contains the %string% .<br>
     *
     * @param string name of the prodi
     * @return List of Mdetilkurikulum
     */
    public List<Mdetilkurikulum> getLikeProdiName(String string);

    /**
     * EN: Gets a list of Mdetilkurikulum where the cohort contains the %string% .<br>
     *
     * @param string Cohort of the Mdetilkurikulum
     * @return List of Mdetilkurikulum
     */
    public List<Mdetilkurikulum> getLikeCohort(String string);

    /**
     * EN: Saves new or updates Mdetilkurikulum.<br>
     */
    public void saveOrUpdate(Mdetilkurikulum entity);

    /**
     * EN: Deletes Mdetilkurikulum.<br>
     */
    public void delete(Mdetilkurikulum entity);

    public ResultObject getAllLikeText(String text, int start, int pageSize);

    public ResultObject getAllLikeMatakuliah(String text, int start, int pageSize);

    public ResultObject getAllByJoin(String text, String prodi, String term, String thajar, int start, int pageSize);

    public ResultObject getAllByIrs(String text, Tirspasca irs, int start, int pageSize);
}
