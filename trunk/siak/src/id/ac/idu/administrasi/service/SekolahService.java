package id.ac.idu.administrasi.service;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mpegawai;
import id.ac.idu.backend.model.Msekolah;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 08/03/12
 * Time: 0:39
 * To change this template use File | Settings | File Templates.
 */
public interface SekolahService {
    /**
     * Create a new Sekolah
     *
     * @return Msekolah
     */
    public Msekolah getNewSekolah();

    /**
     * Get a Sekolah by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Msekolah
     */
    public Msekolah getSekolahById(int id);

    /**
     * Get a Sekolah by code.<br>
     *
     * @param code
     * @return Msekolah
     */
    public Msekolah getSekolahByCode(String code);

    /**
     * Get a Sekolah by name.<br>
     *
     * @param name
     * @return Msekolah
     */
    public Msekolah getSekolahByName(String name);

    /**
     * Get list of Sekolah by its Pegawai.<br>
     *
     * @param pegawai
     * @return Msekolah
     */
    public List<Msekolah> getSekolahByPegawai(Mpegawai pegawai);

    /**
     * Get a list of all Sekolah.<br>
     *
     * @return List of Sekolah
     */
    public List<Msekolah> getAllSekolah();

    /**
     * Get the count of all Sekolah.<br>
     *
     * @return int
     */
    public int getCountAllSekolah();

    /**
     * Saves new or updates a Sekolah.<br>
     */
    public void saveOrUpdate(Msekolah entity);

    /**
     * Deletes a Sekolah.<br>
     */
    public void delete(Msekolah entity);

    /**
     * Saves an Sekolah.<br>
     */
    public void save(Msekolah entity);

    public ResultObject getAllSekolahLikeText(String text, int start, int pageSize);
}
