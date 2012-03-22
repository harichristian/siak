package id.ac.idu.irs.service.impl;

import id.ac.idu.administrasi.dao.MahasiswaDAO;
import id.ac.idu.administrasi.dao.ProdiDAO;
import id.ac.idu.administrasi.dao.SekolahDAO;
import id.ac.idu.backend.model.*;
import id.ac.idu.irs.dao.IrsDAO;
import id.ac.idu.irs.service.IrsService;
import id.ac.idu.mankurikulum.dao.MatakuliahDAO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/11/12
 * Time: 5:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class IrsServiceImpl implements IrsService{
    private IrsDAO irsDAO;
    private MahasiswaDAO mahasiswaDAO;
    private SekolahDAO sekolahDAO;
    private ProdiDAO prodiDAO;
    private MatakuliahDAO matakuliahDAO;

    public IrsDAO getIrsDAO() {
        return irsDAO;
    }

    public void setIrsDAO(IrsDAO irsDAO) {
        this.irsDAO = irsDAO;
    }

    public MahasiswaDAO getMahasiswaDAO() {
        return mahasiswaDAO;
    }

    public void setMahasiswaDAO(MahasiswaDAO mahasiswaDAO) {
        this.mahasiswaDAO = mahasiswaDAO;
    }

    public SekolahDAO getSekolahDAO() {
        return sekolahDAO;
    }

    public void setSekolahDAO(SekolahDAO sekolahDAO) {
        this.sekolahDAO = sekolahDAO;
    }

    public ProdiDAO getProdiDAO() {
        return prodiDAO;
    }

    public void setProdiDAO(ProdiDAO prodiDAO) {
        this.prodiDAO = prodiDAO;
    }

    public MatakuliahDAO getMatakuliahDAO() {
        return matakuliahDAO;
    }

    public void setMatakuliahDAO(MatakuliahDAO matakuliahDAO) {
        this.matakuliahDAO = matakuliahDAO;
    }

    @Override
    public Tirspasca getNewIrs() {
        return getIrsDAO().getNewIrs();
    }

    @Override
    public int getCountAll() {
        return getIrsDAO().getCountAll();
    }

    @Override
    public Tirspasca getIrsById(int id) {
        return getIrsDAO().getIrsById(id);
    }

    @Override
    public List<Tirspasca> getAll() {
        return getIrsDAO().getAll();
    }

    @Override
    public List<Tirspasca> getIrsLikeTerm(String string) {
        return getIrsDAO().getIrsLikeTerm(string);
    }

    @Override
    public List<Tirspasca> getIrsLikeKelompok(String string) {
        return getIrsDAO().getIrsLikeKelompok(string);
    }

    @Override
    public List<Tirspasca> getIrsLikeMahasiswaName(String string) {
        return getIrsLikeMahasiswaName(string);
    }

    @Override
    public void saveOrUpdate(Tirspasca entity) {
        getIrsDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Tirspasca entity) {
        getIrsDAO().delete(entity);
    }
}
