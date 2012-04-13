package id.ac.idu.administrasi.service;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mbidangusaha;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 4/13/12
 * Time: 8:18 AM
 * To change this template use File | Settings | File Templates.
 */
public interface BidangUsahaService {

    public Mbidangusaha getNewBidangusaha();


    public Mbidangusaha getBidangusahaByID(int id);

    public Mbidangusaha getBidangusahaByName(String name);

    public List<Mbidangusaha> getAllBidangusaha();

    public int getCountAllBidangusaha();

    public void saveOrUpdate(Mbidangusaha bidangusaha);

    public void delete(Mbidangusaha bidangusaha);

      /**
     * EN: Get a paged list of all Branches.<br>
     * DE: Gibt eine paged Liste aller Branchen zurueck.<br>
     *
     * @param text     Text for search / SuchText
     * @param start    StartRecord / Start Datensatz
     * @param pageSize Count of Records / Anzahl Datensaetze
     * @return List of YoutubeLinks / Liste von YoutubeLinks
     */
    public ResultObject getAllBidangusahaLikeText(String text, int start, int pageSize);
}
