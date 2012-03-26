package id.ac.idu.administrasi.service;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mjabatan;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 06/03/12
 * Time: 0:35
 * To change this template use File | Settings | File Templates.
 */
public interface JabatanService {

    public Mjabatan getNewJabatan();


    public Mjabatan getJabatanByID(int id);

    public Mjabatan getJabatanByName(String name);

    public List<Mjabatan> getAllJabatan();

    public int getCountAllJabatan();

    public void saveOrUpdate(Mjabatan jabatan);

    public void delete(Mjabatan jabatan);

      /**
     * EN: Get a paged list of all Branches.<br>
     * DE: Gibt eine paged Liste aller Branchen zurueck.<br>
     *
     * @param text     Text for search / SuchText
     * @param start    StartRecord / Start Datensatz
     * @param pageSize Count of Records / Anzahl Datensaetze
     * @return List of YoutubeLinks / Liste von YoutubeLinks
     */
    public ResultObject getAllJabatanLikeText(String text, int start, int pageSize);
}
