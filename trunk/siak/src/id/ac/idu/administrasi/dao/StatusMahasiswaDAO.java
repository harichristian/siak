package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.model.Mstatusmhs;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/8/12
 * Time: 11:01 PM
 * To change this template use File | Settings | File Templates.
 */
public interface StatusMahasiswaDAO {
    /**
     * EN: Get a new Mstatusmhs object.<br>
     *
     * @return Mstatusmhs
     */
    public Mstatusmhs getNewStatusMahasiswa();

    /**
     * EN: Get an Mstatusmhs by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Mstatusmhs
     */
    public Mstatusmhs getStatusMahasiswaById(int id);

    /**
     * EN: Get an Mstatusmhs object by its Code.<br>
     *
     * @param code / the Mstatusmhs Code
     * @return Mstatusmhs
     */
    public Mstatusmhs getStatusMahasiswaByCode(String code);

    /**
     * EN: Get a list of all Mstatusmhs.<br>
     *
     * @return List of Mstatusmhs
     */
    public List<Mstatusmhs> getAllStatusMahasiswas();

    /**
     * EN: Get the count of all Mstatusmhs.<br>
     *
     * @return int
     */
    public int getCountAllStatusMahasiswas();

    /**
     * EN: Gets a list of Mstatusmhs where the Mstatusmhs name contains the %string% .<br>
     *
     * @param name Name of the Mstatusmhs
     * @return List of Mstatusmhs
     */
    public List<Mstatusmhs> getStatusMahasiswaLikeName(String name);

    /**
     * EN: Gets a list of Mstatusmhs where the Mstatusmhs keterangan contains the %string% .<br>
     *
     * @param string Keterangan of the Mstatusmhs
     * @return List of Mstatusmhs
     */
    public List<Mstatusmhs> getStatusMahasiswasLikeKeterangan(String string);

    /**
     * EN: Deletes an Mstatusmhs by its Id.<br>
     *
     * @param id / the persistence identifier
     */
    public void deleteStatusMahasiswaById(int id);

    /**
     * EN: Saves new or updates an Mstatusmhs.<br>
     */
    public void saveOrUpdate(Mstatusmhs entity);

    /**
     * EN: Deletes an Mstatusmhs.<br>
     */
    public void delete(Mstatusmhs entity);

    /**
     * EN: Saves an Mstatusmhs.<br>
     */
    public void save(Mstatusmhs entity);
}
