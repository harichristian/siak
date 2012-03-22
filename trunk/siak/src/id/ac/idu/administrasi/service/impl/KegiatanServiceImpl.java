package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.KegiatanDAO;
import id.ac.idu.administrasi.service.KegiatanService;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mkegiatan;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 08/03/12
 * Time: 22:57
 * To change this template use File | Settings | File Templates.
 */
public class KegiatanServiceImpl implements KegiatanService {
    private KegiatanDAO kegiatanDAO;

    public KegiatanDAO getKegiatanDAO() {
        return kegiatanDAO;
    }

    public void setKegiatanDAO(KegiatanDAO kegiatanDAO) {
        this.kegiatanDAO = kegiatanDAO;
    }

    @Override
    public Mkegiatan getNewKegiatan() {
        return getKegiatanDAO().getNewKegiatan();
    }

    @Override
    public Mkegiatan getKegiatanById(int id) {
        return getKegiatanDAO().getKegiatanById(id);
    }

    @Override
    public Mkegiatan getKegiatanByName(String name) {
        return getKegiatanDAO().getKegiatanByName(name);
    }

    @Override
    public List<Mkegiatan> getAllKegiatan() {
        return getKegiatanDAO().getAllKegiatan();
    }

    @Override
    public int getCountAllKegiatan() {
        return getKegiatanDAO().getCountAllKegiatan();
    }

    @Override
    public void saveOrUpdate(Mkegiatan kegiatan) {
        getKegiatanDAO().saveOrUpdate(kegiatan);
    }

    @Override
    public void delete(Mkegiatan kegiatan) {
        getKegiatanDAO().delete(kegiatan);
    }

    @Override
    public void save(Mkegiatan entity) {
        getKegiatanDAO().save(entity);
    }
    
    @Override
    public ResultObject getAllKegiatanLikeText(String text, int start, int pageSize) {
        return getKegiatanDAO().getAllKegiatanLikeText(text, start, pageSize);
    }
}
