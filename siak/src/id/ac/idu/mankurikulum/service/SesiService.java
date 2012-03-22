package id.ac.idu.mankurikulum.service;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Msesikuliah;

import java.util.List;

/**
 * User: hermanto
 * Date: 08 Mar 12
 * Time: 8:40:12
 */

public interface SesiService {
    public Msesikuliah getNewSesi();

    public Msesikuliah getSesiById(int id);

    public Msesikuliah getSesiByCode(String code);

    public List<Msesikuliah> getAllSesi();

    public int getCountAllSesi();

    public List<Msesikuliah> getSesiByJamAwal(String jamawal);

    public List<Msesikuliah> getSesiByJamAkhir(String jamakhir);

    public void saveOrUpdate(Msesikuliah entity);

    public void delete(Msesikuliah entity);

    public void save(Msesikuliah entity);

    public ResultObject getAllSesiLikeText(String text, int start, int pageSize);
}
