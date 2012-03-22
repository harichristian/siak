package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.model.Mkurmhs;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/9/12
 * Time: 2:43 AM
 * To change this template use File | Settings | File Templates.
 */
public interface KurikulumMahasiswaDAO {
    /**
     * EN: Get a new Mkurmhs object.<br>
     *
     * @return Mkurmhs
     */
    public Mkurmhs getNewKurikulumMahasiswa();

    /**
     * EN: Get an Mkurmhs by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Mkurmhs
     */
    public Mkurmhs getKurikulumMahasiswaById(int id);

    /**
     * EN: Get an Mkurmhs object by its Code.<br>
     *
     * @param code / the Mkurmhs Code
     * @return Mkurmhs
     */
    public Mkurmhs getKurikulumMahasiswaByCode(String code);

    /**
     * EN: Get a list of all Mkurmhs.<br>
     *
     * @return List of Mkurmhs
     */
    public List<Mkurmhs> getAllKurikulumMahasiswas();

    /**
     * EN: Get the count of all Mkurmhs.<br>
     *
     * @return int
     */
    public int getCountAllKurikulumMahasiswas();

    /**
     * EN: Gets a list of Mkurmhs where the Mkurmhs name contains the %string% .<br>
     *
     * @param name Name of the Mkurmhs
     * @return List of Mkurmhs
     */
    public List<Mkurmhs> getKurikulumMahasiswaLikeName(String name);

    /**
     * EN: Gets a list of Mkurmhs where the Mkurmhs keterangan contains the %string% .<br>
     *
     * @param string Mkurmhs of the Mkurmhs
     * @return List of Mkurmhs
     */
    public List<Mkurmhs> getKurikulumMahasiswasLikeKeterangan(String string);

    /**
     * EN: Deletes an Mkurmhs by its Id.<br>
     *
     * @param id / the persistence identifier
     */
    public void deleteKurikulumMahasiswaById(int id);

    /**
     * EN: Saves new or updates an Mkurmhs.<br>
     */
    public void saveOrUpdate(Mkurmhs entity);

    /**
     * EN: Deletes an Mkurmhs.<br>
     */
    public void delete(Mkurmhs entity);

    /**
     * EN: Saves an Mkurmhs.<br>
     */
    public void save(Mkurmhs entity);
}
