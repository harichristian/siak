package id.ac.idu.administrasi.service;

import id.ac.idu.backend.model.Tfeedbackwisudawan;

import java.util.List;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 3/21/12
 * Time: 3:03 AM
 */
public interface FeedbackWisudawanService {
    /**
     * EN: Get a new Tfeedbackwisudawan object.<br>
     *
     * @return Tfeedbackwisudawan
     */
    public Tfeedbackwisudawan getNewFeedbackWisudawan();

    public List<Tfeedbackwisudawan> getNewFeedbackWisudawanList();

    /**
     * EN: Get the count of all Tfeedbackwisudawan.<br>
     *
     * @return int
     */
    public int getCountAll();

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
     * EN: Gets a list of Tfeedbackwisudawan where the Tfeedbackwisudawan term contains the %string% .<br>
     *
     * @param string term of the Tfeedbackwisudawan
     * @return List of Tfeedbackwisudawan
     */
    public List<Tfeedbackwisudawan> getFeedbackWisudawanLikeTerm(String string);

    /**
     * EN: Gets a list of Tfeedbackwisudawan where the Tfeedbackwisudawan kelompok contains the %string% .<br>
     *
     * @param string kelompok of the Tfeedbackwisudawan
     * @return List of Tfeedbackwisudawan
     */
    public List<Tfeedbackwisudawan> getFeedbackWisudawanLikeKelompok(String string);

    /**
     * EN: Gets a list of Tfeedbackwisudawan where the Tfeedbackwisudawan mahasiswa name contains the %string% .<br>
     *
     * @param string mahasiswa name of the Tfeedbackwisudawan
     * @return List of Tfeedbackwisudawan
     */
    public List<Tfeedbackwisudawan> getFeedbackWisudawanLikeMahasiswaName(String string);

    /**
     * EN: Saves new or updates an Tfeedbackwisudawan.<br>
     */
    public void saveOrUpdate(Tfeedbackwisudawan entity);

    /**
     * EN: Deletes an Tfeedbackwisudawan.<br>
     */
    public void delete(Tfeedbackwisudawan entity);

    /**
     * EN: Saves new or updates a list of Tfeedbackwisudawan.<br>
     */
    public void saveOrUpdateList(List<Tfeedbackwisudawan> list);

    public List<Tfeedbackwisudawan> getFeedbackWisudawanByNim(String string, String term, String kelompok);
}
