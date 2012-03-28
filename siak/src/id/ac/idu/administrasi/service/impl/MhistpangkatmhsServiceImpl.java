package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.MhistpangkatmhsDAO;
import id.ac.idu.administrasi.service.MhistpangkatmhsService;
import id.ac.idu.backend.model.Mhistpangkatmhs;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 25 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MhistpangkatmhsServiceImpl implements MhistpangkatmhsService {
    private MhistpangkatmhsDAO mhistpangkatmhsDAO;

    public MhistpangkatmhsDAO getMhistpangkatmhsDAO() {
        return mhistpangkatmhsDAO;
    }

    public void setMhistpangkatmhsDAO(MhistpangkatmhsDAO mhistpangkatmhsDAO) {
        this.mhistpangkatmhsDAO = mhistpangkatmhsDAO;
    }

    @Override
    public Mhistpangkatmhs getNew() {
        return getMhistpangkatmhsDAO().getNew();
    }

    @Override
    public Mhistpangkatmhs getById(int _id) {
        return getMhistpangkatmhsDAO().getById(_id);
    }

    @Override
    public List<Mhistpangkatmhs> getByMahasiswaId(int _id) {
        return getMhistpangkatmhsDAO().getByMahasiswaId(_id);
    }

    @Override
    public List<Mhistpangkatmhs> getAll() {
        return getMhistpangkatmhsDAO().getAll();
    }

    @Override
    public int getCount() {
        return getMhistpangkatmhsDAO().getCount();
    }

    @Override
    public void saveOrUpdate(Mhistpangkatmhs entity) {
        getMhistpangkatmhsDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Mhistpangkatmhs entity) {
        getMhistpangkatmhsDAO().delete(entity);
    }

    @Override
    public void save(Mhistpangkatmhs entity) {
        getMhistpangkatmhsDAO().save(entity);
    }
}
