package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.KodePosDAO;
import id.ac.idu.administrasi.dao.MahasiswaDAO;
import id.ac.idu.administrasi.service.MahasiswaService;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mmahasiswa;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 10 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MahasiswaServiceImpl implements MahasiswaService{

    private MahasiswaDAO mahasiswaDAO;
    private KodePosDAO kodeposDAO;
    
    public MahasiswaDAO getMahasiswaDAO() {
        return mahasiswaDAO;
    }

    public void setMahasiswaDAO(MahasiswaDAO mahasiswaDAO) {
        this.mahasiswaDAO = mahasiswaDAO;
    }

    public KodePosDAO getKodeposDAO() {
        return kodeposDAO;
    }

    public void setKodeposDAO(KodePosDAO kodeposDAO) {
        this.kodeposDAO = kodeposDAO;
    }

    @Override
    public Mmahasiswa getNew() {
        return getMahasiswaDAO().getNew();
    }

    @Override
    public Mmahasiswa getById(Long _id) {
        return getMahasiswaDAO().getById(_id);
    }

    @Override
    public int getCount() {
        return getMahasiswaDAO().getCount();
    }

    @Override
    public List<Mmahasiswa> getAll() {
        return getMahasiswaDAO().getAll();
    }

    @Override
    public List<Mmahasiswa> getByNim(String _nim) {
        return getMahasiswaDAO().getByNim(_nim);
    }

    @Override
    public List<Mmahasiswa> getByNama(String _nama) {
        return getMahasiswaDAO().getByNama(_nama);
    }

    @Override
    public void saveOrUpdate(Mmahasiswa entity) {
        getMahasiswaDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Mmahasiswa entity) {
        getMahasiswaDAO().delete(entity);
    }

    @Override
    public void save(Mmahasiswa entity) {
        getMahasiswaDAO().save(entity);
    }

      @Override
    public ResultObject getAllMmahasiswaLikeText(String text, int start, int pageSize) {
        return getMahasiswaDAO().getAllMmahasiswaLikeText(text, start, pageSize);
    }
}
