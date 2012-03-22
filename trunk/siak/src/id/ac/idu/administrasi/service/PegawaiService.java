package id.ac.idu.administrasi.service;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mjenjang;
import id.ac.idu.backend.model.Mpegawai;
import id.ac.idu.backend.model.Mprodi;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 08/03/12
 * Time: 17:31
 * To change this template use File | Settings | File Templates.
 */
public interface PegawaiService {
    /**
     * Create a new Pegawai
     *
     * @return Mpegawai
     */
    public Mpegawai getNewPegawai();

    /**
     * Get a Pegawai by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Mpegawai
     */
    public Mpegawai getPegawaiById(int id);

    /**
     * Get a Pegawai by nip.<br>
     *
     * @param nip
     * @return Mpegawai
     */
    public Mpegawai getPegawaiByNip(String nip);

    /**
     * Get a Pegawai by name.<br>
     *
     * @param name
     * @return Mpegawai
     */
    public Mpegawai getPegawaiByName(String name);

    /**
     * Get list of Pegawai by its Prodi.<br>
     *
     * @param prodi
     * @return Mpegawai
     */
    public List<Mpegawai> getPegawaiByProdi(Mprodi prodi);

    /**
     * Get list of Pegawai by its Jenjang.<br>
     *
     * @param jenjang
     * @return Mpegawai
     */
    public List<Mpegawai> getPegawaiByJenjang(Mjenjang jenjang);

    /**
     * Get a list of all Pegawai.<br>
     *
     * @return List of Pegawai
     */
    public List<Mpegawai> getAllPegawai();

    /**
     * Get the count of all Pegawai.<br>
     *
     * @return int
     */
    public int getCountAllPegawai();

    /**
     * Saves new or updates a Pegawai.<br>
     */
    public void saveOrUpdate(Mpegawai entity);

    /**
     * Deletes a Pegawai.<br>
     */
    public void delete(Mpegawai entity);

    /**
     * Saves an Pegawai.<br>
     */
    public void save(Mpegawai entity);

    /**
     * EN: Get a paged list of all Branches.<br>
     * DE: Gibt eine paged Liste aller Branchen zurueck.<br>
     *
     * @param text     Text for search / SuchText
     * @param start    StartRecord / Start Datensatz
     * @param pageSize Count of Records / Anzahl Datensaetze
     * @return List of YoutubeLinks / Liste von YoutubeLinks
     */
    public ResultObject getAllPegawaiLikeText(String text, int start, int pageSize);
}
