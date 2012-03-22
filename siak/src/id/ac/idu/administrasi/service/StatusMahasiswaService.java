package id.ac.idu.administrasi.service;

import id.ac.idu.backend.model.Mstatusmhs;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/8/12
 * Time: 11:10 PM
 * To change this template use File | Settings | File Templates.
 */
public interface StatusMahasiswaService {
    /**
     * EN: Get a new Mstatusmhs object.<br>
     *
     * @return Mstatusmhs
     */
    public Mstatusmhs getNewStatusMahasiswa();

    /**
     * EN: Get the count of all Mstatusmhs.<br>
     *
     * @return int
     */
    public int getCountAllStatusMahasiswas();

    /**
     * EN: Get an Mstatusmhs by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Mstatusmhs
     */
    public Mstatusmhs getStatusMahasiswaById(int id);

    /**
     * EN: Get a list of all Mstatusmhs.<br>
     *
     * @return List of Mstatusmhs
     */
    public List<Mstatusmhs> getAllStatusMahasiswas();

    /**
     * EN: Gets a list of Mstatusmhs where the Mstatusmhs name contains the %string% .<br>
     *
     * @param string Name of the Mstatusmhs
     * @return List of Mstatusmhs
     */
    public List<Mstatusmhs> getStatusMahasiswasLikeName(String string);

    /**
     * EN: Gets a list of Mstatusmhs where the Mstatusmhs keterangan contains the %string% .<br>
     *
     * @param string Keterangan of the Mstatusmhs
     * @return List of Mstatusmhs
     */
    public List<Mstatusmhs> getStatusMahasiswasLikeKeterangan(String string);

    /**
     * EN: Saves new or updates an Mstatusmhs.<br>
     */
    public void saveOrUpdate(Mstatusmhs entity);

    /**
     * EN: Deletes an Mstatusmhs.<br>
     */
    public void delete(Mstatusmhs entity);
}
