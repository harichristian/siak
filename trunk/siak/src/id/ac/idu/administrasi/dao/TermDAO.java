package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mterm;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 28/03/12
 * Time: 10:26
 * To change this template use File | Settings | File Templates.
 */
public interface TermDAO {

    /**
     * Create a new Term
     *
     * @return Mterm
     */
    public Mterm getNewTerm();

    /**
     * Get a Term by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Mterm
     */
    public Mterm getTermById(int id);

    /**
     * Get a Term by name.<br>
     *
     * @param kode
     * @return Mterm
     */
    public Mterm getTermByKode(String kode);

    /**
     * Get a list of all Term.<br>
     *
     * @return List of Term
     */
    public List<Mterm> getAllTerm();

    /**
     * Get the count of all Term.<br>
     *
     * @return int
     */
    public int getCountAllTerm();

    /**
     * Saves new or updates an Term.<br>
     */
    public void saveOrUpdate(Mterm entity);

    /**
     * Deletes an Term.
     */
    public void delete(Mterm entity);

    /**
     * Saves an Term.<br>
     */
    public void save(Mterm entity);

    public ResultObject getAllTermLikeText(String text, int start, int pageSize);
}