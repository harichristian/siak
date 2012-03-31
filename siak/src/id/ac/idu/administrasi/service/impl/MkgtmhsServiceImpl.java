package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.MkgtmhsDAO;
import id.ac.idu.administrasi.service.MkgtmhsService;
import id.ac.idu.backend.model.Mkgtmhs;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 31 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MkgtmhsServiceImpl implements MkgtmhsService {
    private MkgtmhsDAO mkgtmhsDAO;

    public MkgtmhsDAO getMkgtmhsDAO() {
        return mkgtmhsDAO;
    }

    public void setMkgtmhsDAO(MkgtmhsDAO mkgtmhsDAO) {
        this.mkgtmhsDAO = mkgtmhsDAO;
    }

    @Override
    public Mkgtmhs getNew() {
        return getMkgtmhsDAO().getNew();
    }

    @Override
    public Mkgtmhs getById(int _id) {
        return getMkgtmhsDAO().getById(_id);
    }

    @Override
    public List<Mkgtmhs> getByMahasiswaId(int _id) {
        return getMkgtmhsDAO().getByMahasiswaId(_id);
    }

    @Override
    public List<Mkgtmhs> getAll() {
        return getMkgtmhsDAO().getAll();
    }

    @Override
    public int getCount() {
        return getMkgtmhsDAO().getCount();
    }

    @Override
    public void saveOrUpdate(Mkgtmhs entity) {
        getMkgtmhsDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Mkgtmhs entity) {
        getMkgtmhsDAO().delete(entity);
    }

    @Override
    public void save(Mkgtmhs entity) {
        getMkgtmhsDAO().save(entity);
    }
}
