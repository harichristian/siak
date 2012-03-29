package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.MpbahasamhsDAO;
import id.ac.idu.administrasi.service.MpbahasamhsService;
import id.ac.idu.backend.model.Mpbahasamhs;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 29 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MpbahasamhsServiceImpl implements MpbahasamhsService {

    private MpbahasamhsDAO mpbahasamhsDAO;

    public MpbahasamhsDAO getMpbahasamhsDAO() {
        return mpbahasamhsDAO;
    }

    public void setMpbahasamhsDAO(MpbahasamhsDAO mpbahasamhsDAO) {
        this.mpbahasamhsDAO = mpbahasamhsDAO;
    }

    @Override
    public Mpbahasamhs getNew() {
        return getMpbahasamhsDAO().getNew();
    }

    @Override
    public Mpbahasamhs getById(int _id) {
        return getMpbahasamhsDAO().getById(_id);
    }

    @Override
    public List<Mpbahasamhs> getByMahasiswaId(int _id) {
        return getMpbahasamhsDAO().getByMahasiswaId(_id);
    }

    @Override
    public List<Mpbahasamhs> getAll() {
        return getMpbahasamhsDAO().getAll();
    }

    @Override
    public int getCount() {
        return getMpbahasamhsDAO().getCount();
    }

    @Override
    public void saveOrUpdate(Mpbahasamhs entity) {
        getMpbahasamhsDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Mpbahasamhs entity) {
        getMpbahasamhsDAO().delete(entity);
    }

    @Override
    public void save(Mpbahasamhs entity) {
        getMpbahasamhsDAO().save(entity);
    }
}
