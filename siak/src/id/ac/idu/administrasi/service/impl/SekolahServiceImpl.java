package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.SekolahDAO;
import id.ac.idu.administrasi.service.SekolahService;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mpegawai;
import id.ac.idu.backend.model.Msekolah;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 08/03/12
 * Time: 0:41
 * To change this template use File | Settings | File Templates.
 */
public class SekolahServiceImpl implements SekolahService {
    private SekolahDAO sekolahDAO;

    public SekolahDAO getSekolahDAO() {
        return sekolahDAO;
    }

    public void setSekolahDAO(SekolahDAO sekolahDAO) {
        this.sekolahDAO = sekolahDAO;
    }

    @Override
    public Msekolah getNewSekolah() {
        return getSekolahDAO().getNewSekolah();
    }

    @Override
    public Msekolah getSekolahById(int id) {
        return getSekolahDAO().getSekolahById(id);
    }

    @Override
    public Msekolah getSekolahByCode(String code) {
        return getSekolahDAO().getSekolahByCode(code);
    }

    @Override
    public Msekolah getSekolahByName(String name) {
        return getSekolahDAO().getSekolahByName(name);
    }

    @Override
    public List<Msekolah> getSekolahByPegawai(Mpegawai pegawai) {
        return getSekolahDAO().getSekolahByPegawai(pegawai);
    }

    @Override
    public List<Msekolah> getAllSekolah() {
        return getSekolahDAO().getAllSekolah();
    }

    @Override
    public int getCountAllSekolah() {
        return getSekolahDAO().getCountAllSekolah();
    }

    @Override
    public void saveOrUpdate(Msekolah entity) {
        getSekolahDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Msekolah entity) {
        getSekolahDAO().delete(entity);
    }

    @Override
    public void save(Msekolah entity) {
        getSekolahDAO().save(entity);
    }

    @Override
    public ResultObject getAllSekolahLikeText(String text, int start, int pageSize) {
        return getSekolahDAO().getAllSekolahLikeText(text, start, pageSize);
    }
}
