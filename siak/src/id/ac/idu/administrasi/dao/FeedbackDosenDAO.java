package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.model.Tfeedbackdosen;

import java.util.List;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 3/21/12
 * Time: 3:09 AM
 */
public interface FeedbackDosenDAO {
    /**
     * EN: Get a new Tfeedbackdosen object.<br>
     *
     * @return Tfeedbackdosen
     */
    public Tfeedbackdosen getNewFeedbackDosen();

    /**
     * EN: Get an Tfeedbackdosen by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Tfeedbackdosen
     */
    public Tfeedbackdosen getFeedbackDosenById(int id);

    /**
     * EN: Get a list of all Tfeedbackdosen.<br>
     *
     * @return List of Tfeedbackdosen
     */
    public List<Tfeedbackdosen> getAll();

    /**
     * EN: Get the count of all Tfeedbackdosen.<br>
     *
     * @return int
     */
    public int getCountAll();

    /**
     * EN: Get an Tfeedbackdosen object by its Term.<br>
     *
     * @param string / the Tfeedbackdosen Term
     * @return Tfeedbackdosen
     */
    public List<Tfeedbackdosen> getFeedbackDosenLikeTerm(String string);

    /**
     * EN: Gets a list of Tfeedbackdosen where the Tfeedbackdosen Kelompok contains the %string% .<br>
     *
     * @param string Kelompok of the Tfeedbackdosen
     * @return List of Tfeedbackdosen
     */
    public List<Tfeedbackdosen> getFeedbackDosenLikeKelompok(String string);

    /**
     * EN: Gets a list of Tfeedbackdosen where the Tfeedbackdosen mahasiswa name contains the %string% .<br>
     *
     * @param string Tfeedbackdosen of the Tfeedbackdosen
     * @return List of Tfeedbackdosen
     */
    public List<Tfeedbackdosen> getFeedbackDosenLikePegawaiName(String string);

    /**
     * EN: Deletes an Tfeedbackdosen by its Id.<br>
     *
     * @param id / the persistence identifier
     */
    public void deleteFeedbackDosenById(int id);

    /**
     * EN: Saves new or updates an Tfeedbackdosen.<br>
     */
    public void saveOrUpdate(Tfeedbackdosen entity);

    /**
     * EN: Deletes an Tfeedbackdosen.<br>
     */
    public void delete(Tfeedbackdosen entity);

    /**
     * EN: Saves an Tfeedbackdosen.<br>
     */
    public void save(Tfeedbackdosen entity);

    public List<Tfeedbackdosen> getFeedbackDosenByNip(String nip, String term, String kelompok);
}
