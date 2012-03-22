package id.ac.idu.mankurikulum.dao;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Msesikuliah;

import java.util.List;

public interface SesiDAO {

    public Msesikuliah getNewSesi();

    public Msesikuliah getSesiById(int id);

    public Msesikuliah getSesiByCode(String code);

    public List<Msesikuliah> getAllSesi();

    public int getCountAllSesi();

    public List<Msesikuliah> getSesiByJamAwal(String jamawal);

    public List<Msesikuliah> getSesiByJamAkhir(String jamakhir);

    public void deleteSesiById(int id);

    public void saveOrUpdate(Msesikuliah entity);

    public void delete(Msesikuliah entity);

    public void save(Msesikuliah entity);

    public ResultObject getAllSesiLikeText(String text, int start, int pageSize);
}