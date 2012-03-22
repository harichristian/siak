package id.ac.idu.administrasi.service;

import id.ac.idu.backend.model.Mcalakademik;
import id.ac.idu.backend.model.Mkegiatan;
import id.ac.idu.backend.model.Mprodi;
import id.ac.idu.backend.model.Msekolah;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 09/03/12
 * Time: 0:42
 * To change this template use File | Settings | File Templates.
 */
public interface CalendarAkademikService {
    /**
     * Create a new Calakademik
     *
     * @return Mcalakademik
     */
    public Mcalakademik getNewCalakademik();

    /**
     * Get a Calakademik by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return Mcalakademik
     */
    public Mcalakademik getCalakademikById(int id);

    /**
     * Get a Calakademik by no.<br>
     *
     * @param no
     * @return Mcalakademik
     */
    public Mcalakademik getCalakademikByNo(String no);

    /**
     * Get a Calakademik by tahun ajaran.<br>
     *
     * @param tahunAjaran
     * @return Mcalakademik
     */
    public Mcalakademik getCalakademikByTahunAjaran(String tahunAjaran);

    /**
     * Get a Calakademik by term.<br>
     *
     * @param term
     * @return Mcalakademik
     */
    public Mcalakademik getCalakademikByTerm(String term);

    /**
     * Get list of Calakademik by its Prodi.<br>
     *
     * @param prodi
     * @return Mcalakademik
     */
    public List<Mcalakademik> getCalakademikByProdi(Mprodi prodi);

    /**
     * Get list of Calakademik by its Sekolah.<br>
     *
     * @param sekolah
     * @return Mcalakademik
     */
    public List<Mcalakademik> getCalakademikBySekolah(Msekolah sekolah);

    /**
     * Get list of Calakademik by its Kegiatan.<br>
     *
     * @param kegiatan
     * @return Mcalakademik
     */
    public List<Mcalakademik> getCalakademikByKegiatan(Mkegiatan kegiatan);

    /**
     * Get list of Calakademik by its Prodi and Sekolah.<br>
     *
     * @param sekolah
     * @param prodi
     * @return Mcalakademik
     */
    public List<Mcalakademik> getCalakademikBySekolahAndProdi(Msekolah sekolah, Mprodi prodi);

    /**
     * Get a list of all Calakademik.<br>
     *
     * @return List of Calakademik
     */
    public List<Mcalakademik> getAllCalakademik();

    /**
     * Get the count of all Calakademik.<br>
     *
     * @return int
     */
    public int getCountAllCalakademik();

    /**
     * Saves new or updates a Calakademik.<br>
     */
    public void saveOrUpdate(Mcalakademik entity);

    /**
     * Deletes a Calakademik.<br>
     */
    public void delete(Mcalakademik entity);

    /**
     * Saves an Calakademik.<br>
     */
    public void save(Mcalakademik entity);
}
