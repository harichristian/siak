package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.model.Mmahasiswa;
import id.ac.idu.backend.model.Tfeedbackwisudawan;

import java.util.List;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 3/21/12
 * Time: 3:09 AM
 */
public interface FeedbackWisudawanDAO {
    /**
     * EN: Get a new Tfeedbackwisudawan object.<br>
     *
     * @return Tfeedbackwisudawan
     */
    public Tfeedbackwisudawan getNewFeedbackWisudawan();

    /**
     * EN: Get an Tfeedbackwisudawan by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Tfeedbackwisudawan
     */
    public Tfeedbackwisudawan getFeedbackWisudawanById(int id);

    /**
     * EN: Get a list of all Tfeedbackwisudawan.<br>
     *
     * @return List of Tfeedbackwisudawan
     */
    public List<Tfeedbackwisudawan> getAll();

    /**
     * EN: Get the count of all Tfeedbackwisudawan.<br>
     *
     * @return int
     */
    public int getCountAll();

    /**
     * EN: Get an Tfeedbackwisudawan object by its Term.<br>
     *
     * @param string / the Tfeedbackwisudawan Term
     * @return Tfeedbackwisudawan
     */
    public List<Tfeedbackwisudawan> getFeedbackWisudawanLikeTerm(String string);

    /**
     * EN: Gets a list of Tfeedbackwisudawan where the Tfeedbackwisudawan Kelompok contains the %string% .<br>
     *
     * @param string Kelompok of the Tfeedbackwisudawan
     * @return List of Tfeedbackwisudawan
     */
    public List<Tfeedbackwisudawan> getFeedbackWisudawanLikeKelompok(String string);

    /**
     * EN: Gets a list of Tfeedbackwisudawan where the Tfeedbackwisudawan mahasiswa name contains the %string% .<br>
     *
     * @param string Tfeedbackwisudawan of the Tfeedbackwisudawan
     * @return List of Tfeedbackwisudawan
     */
    public List<Tfeedbackwisudawan> getFeedbackWisudawanLikeMahasiswaName(String string);

    /**
     * EN: Deletes an Tfeedbackwisudawan by its Id.<br>
     *
     * @param id / the persistence identifier
     */
    public void deleteFeedbackWisudawanById(int id);

    /**
     * EN: Saves new or updates an Tfeedbackwisudawan.<br>
     */
    public void saveOrUpdate(Tfeedbackwisudawan entity);

    /**
     * EN: Deletes an Tfeedbackwisudawan.<br>
     */
    public void delete(Tfeedbackwisudawan entity);

    /**
     * EN: Saves an Tfeedbackwisudawan.<br>
     */
    public void save(Tfeedbackwisudawan entity);

    public List<Tfeedbackwisudawan> getFeedbackWisudawanByNim(Mmahasiswa mhs, String term, String kelompok);
}
