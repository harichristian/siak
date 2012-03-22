package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.model.Mfeedback;

import java.util.List;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 3/21/12
 * Time: 3:27 AM
 */
public interface FeedbackDAO {
    /**
     * EN: Get a new Mfeedback object.<br>
     *
     * @return Mfeedback
     */
    public Mfeedback getNewFeedback();

    /**
     * EN: Get an Mfeedback by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Mfeedback
     */
    public Mfeedback getFeedbackById(int id);

    /**
     * EN: Get a list of all Mfeedback.<br>
     *
     * @return List of Mfeedback
     */
    public List<Mfeedback> getAll();

    /**
     * EN: Get the count of all Mfeedback.<br>
     *
     * @return int
     */
    public int getCountAll();

    /**
     * EN: Get an Mfeedback object by its code.<br>
     *
     * @param string / the Mfeedback code
     * @return Mfeedback
     */
    public List<Mfeedback> getFeedbackLikeCode(String string);

    /**
     * EN: Gets a list of Mfeedback where the Mfeedback prodi name contains the %string% .<br>
     *
     * @param string prodi name of the Mfeedback
     * @return List of Mfeedback
     */
    public List<Mfeedback> getFeedbackLikeProdiName(String string);

    /**
     * EN: Gets a list of Mfeedback where the Mfeedback sekolah name contains the %string% .<br>
     *
     * @param string sekolah name of the Mfeedback
     * @return List of Mfeedback
     */
    public List<Mfeedback> getFeedbackLikeSekolahName(String string);

    /**
     * EN: Deletes an Mfeedback by its Id.<br>
     *
     * @param id / the persistence identifier
     */
    public void deleteFeedbackById(int id);

    /**
     * EN: Saves new or updates an Mfeedback.<br>
     */
    public void saveOrUpdate(Mfeedback entity);

    /**
     * EN: Deletes an Mfeedback.<br>
     */
    public void delete(Mfeedback entity);

    /**
     * EN: Saves an Mfeedback.<br>
     */
    public void save(Mfeedback entity);
}
