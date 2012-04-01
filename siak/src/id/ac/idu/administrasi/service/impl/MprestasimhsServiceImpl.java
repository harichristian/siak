package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.MprestasimhsDAO;
import id.ac.idu.administrasi.service.MprestasimhsService;
import id.ac.idu.backend.model.Mprestasimhs;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 02 Apr 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MprestasimhsServiceImpl implements MprestasimhsService {
    private MprestasimhsDAO mprestasimhsDAO;

    public MprestasimhsDAO getMprestasimhsDAO() {
        return mprestasimhsDAO;
    }

    public void setMprestasimhsDAO(MprestasimhsDAO mprestasimhsDAO) {
        this.mprestasimhsDAO = mprestasimhsDAO;
    }

    @Override
    public Mprestasimhs getNew() {
        return getMprestasimhsDAO().getNew();
    }

    @Override
    public Mprestasimhs getById(int _id) {
        return getMprestasimhsDAO().getById(_id);
    }

    @Override
    public List<Mprestasimhs> getByMahasiswaId(int _id) {
        return getMprestasimhsDAO().getByMahasiswaId(_id);
    }

    @Override
    public List<Mprestasimhs> getAll() {
        return getMprestasimhsDAO().getAll();
    }

    @Override
    public int getCount() {
        return getMprestasimhsDAO().getCount();
    }

    @Override
    public void saveOrUpdate(Mprestasimhs entity) {
        getMprestasimhsDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Mprestasimhs entity) {
        getMprestasimhsDAO().delete(entity);
    }

    @Override
    public void save(Mprestasimhs entity) {
        getMprestasimhsDAO().save(entity);
    }
}
