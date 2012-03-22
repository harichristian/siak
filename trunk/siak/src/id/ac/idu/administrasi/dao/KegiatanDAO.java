package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mkegiatan;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 08/03/12
 * Time: 22:56
 * To change this template use File | Settings | File Templates.
 */
public interface KegiatanDAO {
    /**
     * Create a new Kegiatan
     *
     * @return Mkegiatan
     */
    public Mkegiatan getNewKegiatan();

    /**
     * Get a Kegiatan by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Mkegiatan
     */
    public Mkegiatan getKegiatanById(int id);

    /**
     * Get a Kegiatan by name.<br>
     *
     * @param name
     * @return Mkegiatan
     */
    public Mkegiatan getKegiatanByName(String name);

    /**
     * Get a list of all Kegiatan.<br>
     *
     * @return List of Kegiatan
     */
    public List<Mkegiatan> getAllKegiatan();

    /**
     * Get the count of all Kegiatan.<br>
     *
     * @return int
     */
    public int getCountAllKegiatan();

    /**
     * Deletes Kegiatan by its Id.<br>
     *
     * @param id
     */
    public void deleteKegiatanById(int id);

    /**
     * Saves new or updates an Kegiatan.<br>
     */
    public void saveOrUpdate(Mkegiatan entity);

    /**
     * Deletes an Kegiatan.
     */
    public void delete(Mkegiatan entity);

    /**
     * Saves an Kegiatan.<br>
     */
    public void save(Mkegiatan entity);

    public ResultObject getAllKegiatanLikeText(String text, int start, int pageSize);

}
