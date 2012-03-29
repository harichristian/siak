package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.model.Tfeedbackalumni;

import java.util.List;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 3/21/12
 * Time: 3:09 AM
 */
public interface FeedbackAlumniDAO {
    /**
     * EN: Get a new Tfeedbackalumni object.<br>
     *
     * @return Tfeedbackalumni
     */
    public Tfeedbackalumni getNewFeedbackAlumni();

    /**
     * EN: Get an Tfeedbackalumni by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Tfeedbackalumni
     */
    public Tfeedbackalumni getFeedbackAlumniById(int id);

    /**
     * EN: Get a list of all Tfeedbackalumni.<br>
     *
     * @return List of Tfeedbackalumni
     */
    public List<Tfeedbackalumni> getAll();

    /**
     * EN: Get the count of all Tfeedbackalumni.<br>
     *
     * @return int
     */
    public int getCountAll();

    /**
     * EN: Get an Tfeedbackalumni object by its Term.<br>
     *
     * @param string / the Tfeedbackalumni Term
     * @return Tfeedbackalumni
     */
    public List<Tfeedbackalumni> getFeedbackAlumniLikeTerm(String string);

    /**
     * EN: Gets a list of Tfeedbackalumni where the Tfeedbackalumni Kelompok contains the %string% .<br>
     *
     * @param string Kelompok of the Tfeedbackalumni
     * @return List of Tfeedbackalumni
     */
    public List<Tfeedbackalumni> getFeedbackAlumniLikeKelompok(String string);

    /**
     * EN: Gets a list of Tfeedbackalumni where the Tfeedbackalumni mahasiswa name contains the %string% .<br>
     *
     * @param string Tfeedbackalumni of the Tfeedbackalumni
     * @return List of Tfeedbackalumni
     */
    public List<Tfeedbackalumni> getFeedbackAlumniLikeMahasiswaName(String string);

    /**
     * EN: Deletes an Tfeedbackalumni by its Id.<br>
     *
     * @param id / the persistence identifier
     */
    public void deleteFeedbackAlumniById(int id);

    /**
     * EN: Saves new or updates an Tfeedbackalumni.<br>
     */
    public void saveOrUpdate(Tfeedbackalumni entity);

    /**
     * EN: Deletes an Tfeedbackalumni.<br>
     */
    public void delete(Tfeedbackalumni entity);

    /**
     * EN: Saves an Tfeedbackalumni.<br>
     */
    public void save(Tfeedbackalumni entity);

    public List<Tfeedbackalumni> getFeedbackAlumniByNim(String nim, String term, String kelompok);
}
