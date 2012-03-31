package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.MkaryamhsDAO;
import id.ac.idu.administrasi.service.MkaryamhsService;
import id.ac.idu.backend.model.Mkaryamhs;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 31 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MkaryamhsServiceImpl implements MkaryamhsService {
    private MkaryamhsDAO mkaryamhsDAO;

    public MkaryamhsDAO getMkaryamhsDAO() {
        return mkaryamhsDAO;
    }

    public void setMkaryamhsDAO(MkaryamhsDAO mkaryamhsDAO) {
        this.mkaryamhsDAO = mkaryamhsDAO;
    }

    @Override
    public Mkaryamhs getNew() {
        return getMkaryamhsDAO().getNew();
    }

    @Override
    public Mkaryamhs getById(int _id) {
        return getMkaryamhsDAO().getById(_id);
    }

    @Override
    public List<Mkaryamhs> getByMahasiswaId(int _id) {
        return getMkaryamhsDAO().getByMahasiswaId(_id);
    }

    @Override
    public List<Mkaryamhs> getAll() {
        return getMkaryamhsDAO().getAll();
    }

    @Override
    public int getCount() {
        return getMkaryamhsDAO().getCount();
    }

    @Override
    public void saveOrUpdate(Mkaryamhs entity) {
        getMkaryamhsDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Mkaryamhs entity) {
        getMkaryamhsDAO().delete(entity);
    }

    @Override
    public void save(Mkaryamhs entity) {
        getMkaryamhsDAO().save(entity);
    }
}
