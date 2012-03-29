package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.MhistkursusmhsDAO;
import id.ac.idu.administrasi.service.MhistkursusmhsService;
import id.ac.idu.backend.model.Mhistkursusmhs;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 29 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MhistkursusmhsServiceImpl implements MhistkursusmhsService {

    private MhistkursusmhsDAO mhistkursusmhsDAO;

    public MhistkursusmhsDAO getMhistkursusmhsDAO() {
        return mhistkursusmhsDAO;
    }

    public void setMhistkursusmhsDAO(MhistkursusmhsDAO mhistkursusmhsDAO) {
        this.mhistkursusmhsDAO = mhistkursusmhsDAO;
    }

    @Override
    public Mhistkursusmhs getNew() {
        return getMhistkursusmhsDAO().getNew();
    }

    @Override
    public Mhistkursusmhs getById(int _id) {
        return getMhistkursusmhsDAO().getById(_id);
    }

    @Override
    public List<Mhistkursusmhs> getByMahasiswaId(int _id) {
        return getMhistkursusmhsDAO().getByMahasiswaId(_id);
    }

    @Override
    public List<Mhistkursusmhs> getAll() {
        return getMhistkursusmhsDAO().getAll();
    }

    @Override
    public int getCount() {
        return getMhistkursusmhsDAO().getCount();
    }

    @Override
    public void saveOrUpdate(Mhistkursusmhs entity) {
        getMhistkursusmhsDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Mhistkursusmhs entity) {
        getMhistkursusmhsDAO().delete(entity);
    }

    @Override
    public void save(Mhistkursusmhs entity) {
        getMhistkursusmhsDAO().save(entity);
    }
}
