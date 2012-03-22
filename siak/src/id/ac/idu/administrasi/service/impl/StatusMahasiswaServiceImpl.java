package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.StatusMahasiswaDAO;
import id.ac.idu.administrasi.service.StatusMahasiswaService;
import id.ac.idu.backend.model.Mstatusmhs;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/8/12
 * Time: 11:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class StatusMahasiswaServiceImpl implements StatusMahasiswaService {
    private StatusMahasiswaDAO statusMahasiswaDAO;

    public StatusMahasiswaDAO getStatusMahasiswaDAO() {
        return statusMahasiswaDAO;
    }

    public void setStatusMahasiswaDAO(StatusMahasiswaDAO statusMahasiswaDAO) {
        this.statusMahasiswaDAO = statusMahasiswaDAO;
    }

    @Override
    public Mstatusmhs getNewStatusMahasiswa() {
        return getStatusMahasiswaDAO().getNewStatusMahasiswa();
    }

    @Override
    public int getCountAllStatusMahasiswas() {
        return getStatusMahasiswaDAO().getCountAllStatusMahasiswas();
    }

    @Override
    public Mstatusmhs getStatusMahasiswaById(int id) {
        return getStatusMahasiswaDAO().getStatusMahasiswaById(id);
    }

    @Override
    public List<Mstatusmhs> getAllStatusMahasiswas() {
        return getStatusMahasiswaDAO().getAllStatusMahasiswas();
    }

    @Override
    public List<Mstatusmhs> getStatusMahasiswasLikeName(String string) {
        return getStatusMahasiswaDAO().getStatusMahasiswaLikeName(string);
    }

    @Override
    public List<Mstatusmhs> getStatusMahasiswasLikeKeterangan(String string) {
        return getStatusMahasiswaDAO().getStatusMahasiswasLikeKeterangan(string);
    }

    @Override
    public void saveOrUpdate(Mstatusmhs entity) {
        getStatusMahasiswaDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Mstatusmhs entity) {
        getStatusMahasiswaDAO().delete(entity);
    }
}
