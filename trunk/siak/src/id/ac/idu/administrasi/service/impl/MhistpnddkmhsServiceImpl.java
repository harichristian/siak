package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.MhistpnddkmhsDAO;
import id.ac.idu.administrasi.service.MhistpnddkmhsService;
import id.ac.idu.backend.model.Mhistpnddkmhs;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 28 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MhistpnddkmhsServiceImpl implements MhistpnddkmhsService {
    private MhistpnddkmhsDAO mhistpnddkmhsDAO;

    public MhistpnddkmhsDAO getMhistpnddkmhsDAO() {
        return mhistpnddkmhsDAO;
    }

    public void setMhistpnddkmhsDAO(MhistpnddkmhsDAO mhistpnddkmhsDAO) {
        this.mhistpnddkmhsDAO = mhistpnddkmhsDAO;
    }

    @Override
    public Mhistpnddkmhs getNew() {
        return getMhistpnddkmhsDAO().getNew();
    }

    @Override
    public Mhistpnddkmhs getById(int _id) {
        return getMhistpnddkmhsDAO().getById(_id);
    }

    @Override
    public List<Mhistpnddkmhs> getByMahasiswaId(int _id) {
        return getMhistpnddkmhsDAO().getByMahasiswaId(_id);
    }

    @Override
    public List<Mhistpnddkmhs> getAll() {
        return getMhistpnddkmhsDAO().getAll();
    }

    @Override
    public int getCount() {
        return getMhistpnddkmhsDAO().getCount();
    }

    @Override
    public void saveOrUpdate(Mhistpnddkmhs entity) {
        getMhistpnddkmhsDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Mhistpnddkmhs entity) {
        getMhistpnddkmhsDAO().delete(entity);
    }

    @Override
    public void save(Mhistpnddkmhs entity) {
        getMhistpnddkmhsDAO().save(entity);
    }
}
