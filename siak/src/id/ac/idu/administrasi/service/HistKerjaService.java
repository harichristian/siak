package id.ac.idu.administrasi.service;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Malumni;
import id.ac.idu.backend.model.Thistkerja;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 4/13/12
 * Time: 10:55 AM
 * To change this template use File | Settings | File Templates.
 */
public interface HistKerjaService {
 public Thistkerja getNewHistKerja();


    public Thistkerja getHistKerjaByID(int id);

    public Thistkerja getHistKerjaByName(String name);

    public List<Thistkerja> getAllHistKerja();

    public int getCountAllHistKerja();

    public void saveOrUpdate(Thistkerja histkerja);

    public void save(Thistkerja histkerja);

    public void delete(Thistkerja histKerja);

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
