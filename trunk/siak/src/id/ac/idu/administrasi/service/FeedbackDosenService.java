package id.ac.idu.administrasi.service;

import id.ac.idu.backend.model.Mpegawai;
import id.ac.idu.backend.model.Tfeedbackdosen;

import java.util.List;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 3/21/12
 * Time: 3:03 AM
 */
public interface FeedbackDosenService {
    /**
     * EN: Get a new Tfeedbackdosen object.<br>
     *
     * @return Tfeedbackdosen
     */
    public Tfeedbackdosen getNewFeedbackDosen();

    public List<Tfeedbackdosen> getNewFeedbackDosenList();

    /**
     * EN: Get the count of all Tfeedbackdosen.<br>
     *
     * @return int
     */
    public int getCountAll();

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
     * EN: Gets a list of Tfeedbackdosen where the Tfeedbackdosen term contains the %string% .<br>
     *
     * @param string term of the Tfeedbackdosen
     * @return List of Tfeedbackdosen
     */
    public List<Tfeedbackdosen> getFeedbackDosenLikeTerm(String string);

    /**
     * EN: Gets a list of Tfeedbackdosen where the Tfeedbackdosen kelompok contains the %string% .<br>
     *
     * @param string kelompok of the Tfeedbackdosen
     * @return List of Tfeedbackdosen
     */
    public List<Tfeedbackdosen> getFeedbackDosenLikeKelompok(String string);

    /**
     * EN: Gets a list of Tfeedbackdosen where the Tfeedbackdosen mahasiswa name contains the %string% .<br>
     *
     * @param string mahasiswa name of the Tfeedbackdosen
     * @return List of Tfeedbackdosen
     */
    public List<Tfeedbackdosen> getFeedbackDosenLikePegawaiName(String string);

    /**
     * EN: Saves new or updates an Tfeedbackdosen.<br>
     */
    public void saveOrUpdate(Tfeedbackdosen entity);

    /**
     * EN: Deletes an Tfeedbackdosen.<br>
     */
    public void delete(Tfeedbackdosen entity);

    /**
     * EN: Saves new or updates a list of Tfeedbackdosen.<br>
     */
    public void saveOrUpdateList(List<Tfeedbackdosen> list);

    public List<Tfeedbackdosen> getFeedbackDosenByNip(Mpegawai string, String term, String kelompok);
}
