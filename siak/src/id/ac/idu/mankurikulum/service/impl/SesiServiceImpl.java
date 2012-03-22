package id.ac.idu.mankurikulum.service.impl;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Msesikuliah;
import id.ac.idu.mankurikulum.dao.SesiDAO;
import id.ac.idu.mankurikulum.service.SesiService;

import java.util.List;

/**
 * User: hermanto
 * Date: 08 Mar 12
 * Time: 8:37:30
 */

public class SesiServiceImpl implements SesiService {

    private SesiDAO sesiDAO;

    public SesiDAO getSesiDAO() {
        return sesiDAO;
    }

    public void setSesiDAO(SesiDAO sesiDao) {
        this.sesiDAO = sesiDao;
    }

    @Override
    public Msesikuliah getNewSesi() {
        return getSesiDAO().getNewSesi();
    }

    @Override
    public Msesikuliah getSesiById(int id) {
        return getSesiDAO().getSesiById(id);
    }

    @Override
    public Msesikuliah getSesiByCode(String code) {
        return getSesiDAO().getSesiByCode(code);
    }

    @Override
    public List<Msesikuliah> getAllSesi() {
        return getSesiDAO().getAllSesi();
    }

    @Override
    public int getCountAllSesi() {
        return getSesiDAO().getCountAllSesi();
    }

    @Override
    public List<Msesikuliah> getSesiByJamAwal(String jamawal) {
        return getSesiDAO().getSesiByJamAwal(jamawal);
    }

    @Override
    public List<Msesikuliah> getSesiByJamAkhir(String jamakhir) {
        return getSesiDAO().getSesiByJamAwal(jamakhir);
    }

    @Override
    public void saveOrUpdate(Msesikuliah entity) {
        getSesiDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Msesikuliah entity) {
        getSesiDAO().delete(entity);
    }

    @Override
    public void save(Msesikuliah entity) {
        getSesiDAO().save(entity);
    }
    
    @Override
    public ResultObject getAllSesiLikeText(String text, int start, int pageSize) {
        return getSesiDAO().getAllSesiLikeText(text, start, pageSize);
    }
}
