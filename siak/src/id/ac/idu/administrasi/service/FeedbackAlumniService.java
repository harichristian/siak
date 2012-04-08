package id.ac.idu.administrasi.service;

import id.ac.idu.backend.model.Mmahasiswa;
import id.ac.idu.backend.model.Tfeedbackalumni;

import java.util.List;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 3/21/12
 * Time: 3:03 AM
 */
public interface FeedbackAlumniService {
    /**
     * EN: Get a new Tfeedbackalumni object.<br>
     *
     * @return Tfeedbackalumni
     */
    public Tfeedbackalumni getNewFeedbackAlumni();

    public List<Tfeedbackalumni> getNewFeedbackAlumniList();

    /**
     * EN: Get the count of all Tfeedbackalumni.<br>
     *
     * @return int
     */
    public int getCountAll();

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
     * EN: Gets a list of Tfeedbackalumni where the Tfeedbackalumni term contains the %string% .<br>
     *
     * @param string term of the Tfeedbackalumni
     * @return List of Tfeedbackalumni
     */
    public List<Tfeedbackalumni> getFeedbackAlumniLikeTerm(String string);

    /**
     * EN: Gets a list of Tfeedbackalumni where the Tfeedbackalumni kelompok contains the %string% .<br>
     *
     * @param string kelompok of the Tfeedbackalumni
     * @return List of Tfeedbackalumni
     */
    public List<Tfeedbackalumni> getFeedbackAlumniLikeKelompok(String string);

    /**
     * EN: Gets a list of Tfeedbackalumni where the Tfeedbackalumni mahasiswa name contains the %string% .<br>
     *
     * @param string mahasiswa name of the Tfeedbackalumni
     * @return List of Tfeedbackalumni
     */
    public List<Tfeedbackalumni> getFeedbackAlumniLikeMahasiswaName(String string);

    /**
     * EN: Saves new or updates an Tfeedbackalumni.<br>
     */
    public void saveOrUpdate(Tfeedbackalumni entity);

    /**
     * EN: Deletes an Tfeedbackalumni.<br>
     */
    public void delete(Tfeedbackalumni entity);

    /**
     * EN: Saves new or updates a list of Tfeedbackalumni.<br>
     */
    public void saveOrUpdateList(List<Tfeedbackalumni> list);

    public List<Tfeedbackalumni> getFeedbackAlumniByNim(Mmahasiswa mhs, String term, String kelompok);
}
