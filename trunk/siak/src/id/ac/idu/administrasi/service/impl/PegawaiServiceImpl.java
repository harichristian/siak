package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.PegawaiDAO;
import id.ac.idu.administrasi.service.PegawaiService;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mjenjang;
import id.ac.idu.backend.model.Mpegawai;
import id.ac.idu.backend.model.Mprodi;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 08/03/12
 * Time: 17:31
 * To change this template use File | Settings | File Templates.
 */
public class PegawaiServiceImpl implements PegawaiService {
    private PegawaiDAO pegawaiDAO;

    public PegawaiDAO getPegawaiDAO() {
        return pegawaiDAO;
    }

    public void setPegawaiDAO(PegawaiDAO pegawaiDAO) {
        this.pegawaiDAO = pegawaiDAO;
    }

    @Override
    public Mpegawai getNewPegawai() {
        return getPegawaiDAO().getNewPegawai();
    }

    @Override
    public Mpegawai getPegawaiById(int id) {
        return getPegawaiDAO().getPegawaiById(id);
    }

    @Override
    public Mpegawai getPegawaiByNip(String nip) {
        return getPegawaiDAO().getPegawaiByNip(nip);
    }

    @Override
    public Mpegawai getPegawaiByName(String name) {
        return getPegawaiDAO().getPegawaiByName(name);
    }

    @Override
    public List<Mpegawai> getPegawaiByProdi(Mprodi prodi) {
        return getPegawaiDAO().getPegawaiByProdi(prodi);
    }

    @Override
    public List<Mpegawai> getPegawaiByJenjang(Mjenjang jenjang) {
        return getPegawaiDAO().getPegawaiByJenjang(jenjang);
    }

    @Override
    public List<Mpegawai> getAllPegawai() {
        return getPegawaiDAO().getAllPegawai();
    }

    @Override
    public int getCountAllPegawai() {
        return getPegawaiDAO().getCountAllPegawai();
    }

    @Override
    public void saveOrUpdate(Mpegawai entity) {
        getPegawaiDAO().save(entity);
    }

    @Override
    public void delete(Mpegawai entity) {
        getPegawaiDAO().delete(entity);
    }

    @Override
    public void save(Mpegawai entity) {
        getPegawaiDAO().save(entity);
    }

    @Override
    public ResultObject getAllPegawaiLikeText(String text, int start, int pageSize) {
        return getPegawaiDAO().getAllPegawaiLikeText(text, start, pageSize);
    }
}
