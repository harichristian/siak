package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.model.Mgrade;
import id.ac.idu.backend.model.Mjenjang;
import id.ac.idu.backend.model.Mprodi;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 07/03/12
 * Time: 11:42
 * To change this template use File | Settings | File Templates.
 */
public interface GradeDAO {
    /**
     * Create a new Grade
     *
     * @return Mgrade
     */
    public Mgrade getNewGrade();

    /**
     * Get a Grade by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Mgrade
     */
    public Mgrade getGradeById(int id);

    /**
     * Get a Grade by code.<br>
     *
     * @param code
     * @return Mgrade
     */
    public Mgrade getGradeByCode(String code);

    /**
     * Get a Grade by name.<br>
     *
     * @param name
     * @return Mgrade
     */
    public Mgrade getGradeByName(String name);

    /**
     * Get list of Grade by its Prodi.<br>
     *
     * @param prodi
     * @return Mgrade
     */
    public List<Mgrade> getGradeByProdi(Mprodi prodi);

    /**
     * Get list of Grade by its Jenjang.<br>
     *
     * @param jenjang
     * @return Mgrade
     */
    public List<Mgrade> getGradeByJenjang(Mjenjang jenjang);

    /**
     * Get a list of all Grade.<br>
     *
     * @return List of Grade
     */
    public List<Mgrade> getAllGrade();

    /**
     * Get the count of all Grade.<br>
     *
     * @return int
     */
    public int getCountAllGrade();

    /**
     * Deletes a Grade by its Id.<br>
     *
     * @param id
     */
    public void deleteGradeById(int id);

    /**
     * Saves new or updates a Grade.<br>
     */
    public void saveOrUpdate(Mgrade entity);

    /**
     * Deletes a Grade.<br>
     */
    public void delete(Mgrade entity);

    /**
     * Saves an Grade.<br>
     */
    public void save(Mgrade entity);
}