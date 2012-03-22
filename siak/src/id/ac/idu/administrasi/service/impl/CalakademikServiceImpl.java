package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.CalakademikDAO;
import id.ac.idu.administrasi.service.CalakademikService;
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
public class CalakademikServiceImpl implements CalakademikService {
    private CalakademikDAO calakademikDAO;

    public CalakademikDAO getCalakademikDAO() {
        return calakademikDAO;
    }

    public void setCalakademikDAO(CalakademikDAO calakademikDAO) {
        this.calakademikDAO = calakademikDAO;
    }

    @Override
    public Mcalakademik getNewCalakademik() {
        return getCalakademikDAO().getNewCalakademik();
    }

    @Override
    public Mcalakademik getCalakademikById(int id) {
        return getCalakademikDAO().getCalakademikById(id);
    }

    @Override
    public Mcalakademik getCalakademikByNo(String no) {
        return getCalakademikDAO().getCalakademikByNo(no);
    }

    @Override
    public Mcalakademik getCalakademikByTahunAjaran(String tahunAjaran) {
        return getCalakademikDAO().getCalakademikByTahunAjaran(tahunAjaran);
    }

    @Override
    public Mcalakademik getCalakademikByTerm(String term) {
        return getCalakademikDAO().getCalakademikByTerm(term);
    }

    @Override
    public List<Mcalakademik> getCalakademikByProdi(Mprodi prodi) {
        return getCalakademikDAO().getCalakademikByProdi(prodi);
    }

    @Override
    public List<Mcalakademik> getCalakademikBySekolah(Msekolah sekolah) {
        return getCalakademikDAO().getCalakademikBySekolah(sekolah);
    }

    @Override
    public List<Mcalakademik> getCalakademikByKegiatan(Mkegiatan kegiatan) {
        return getCalakademikDAO().getCalakademikByKegiatan(kegiatan);
    }

    @Override
    public List<Mcalakademik> getCalakademikBySekolahAndProdi(Msekolah sekolah, Mprodi prodi) {
        return getCalakademikDAO().getCalakademikBySekolahAndProdi(sekolah, prodi);
    }

    @Override
    public List<Mcalakademik> getAllCalakademik() {
        return getCalakademikDAO().getAllCalakademik();
    }

    @Override
    public int getCountAllCalakademik() {
        return getCalakademikDAO().getCountAllCalakademik();
    }

    @Override
    public void saveOrUpdate(Mcalakademik entity) {
        getCalakademikDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Mcalakademik entity) {
        getCalakademikDAO().delete(entity);
    }

    @Override
    public void save(Mcalakademik entity) {
        getCalakademikDAO().save(entity);
    }
}
