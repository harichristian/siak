package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Malumni;
import id.ac.idu.backend.model.Thistkerja;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 4/13/12
 * Time: 10:35 AM
 * To change this template use File | Settings | File Templates.
 */
public interface HistKerjaDAO  {
  /**
     * Create a new HistKerja
     *
     * @return MhistKerja
     */
    public Thistkerja getNewHistKerja();

    /**
     * Get a HistKerja by its ID.<br>
     *
     * @param id / the persistence identifier
     * @return MhistKerja
     */
    public Thistkerja getHistKerjaById(int id);

    /**
     * Get a HistKerja by name.<br>
     *
     * @param name
     * @return MhistKerja
     */
    public Thistkerja getHistKerjaByName(String name);

    /**
     * Get a list of all HistKerja.<br>
     *
     * @return List of HistKerja
     */
    public List<Thistkerja> getAllHistKerja();

    /**
     * Get the count of all HistKerja.<br>
     *
     * @return int
     */
    public int getCountAllHistKerja();

    /**
     * Deletes HistKerja by its Id.<br>
     *
     * @param id
     */
    public void deleteHistKerjaById(int id);

    /**
     * Saves new or updates an HistKerja.<br>
     */
    public void saveOrUpdate(Thistkerja entity);

    /**
     * Deletes an HistKerja.
     */
    public void delete(Thistkerja entity);

    /**
     * Saves an HistKerja.<br>
     */
    public void save(Thistkerja entity);

     /**
     * EN: Get a paged list of all Branches.<br>
     * DE: Gibt eine paged Liste aller Branchen zurueck.<br>
     *
     * @param text     Text for search / SuchText
     * @param start    StartRecord / Start Datensatz
     * @param pageSize Count of Records / Anzahl Datensaetze
     * @return List of YoutubeLinks / Liste von YoutubeLinks
     */
    public ResultObject getAllHistKerjaLikeText(String text, int start, int pageSize);

     public List<Thistkerja> getAllHIstkerjaByAlumni(Malumni alumni);

}