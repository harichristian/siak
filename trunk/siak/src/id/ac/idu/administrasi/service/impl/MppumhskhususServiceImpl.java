package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.MppumhskhususDAO;
import id.ac.idu.administrasi.service.MppumhskhususService;
import id.ac.idu.backend.model.Mppumhskhusus;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 25 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MppumhskhususServiceImpl implements MppumhskhususService {
    private MppumhskhususDAO mppumhskhususDAO;

    public MppumhskhususDAO getMppumhskhususDAO() {
        return mppumhskhususDAO;
    }

    public void setMppumhskhususDAO(MppumhskhususDAO mppumhskhususDAO) {
        this.mppumhskhususDAO = mppumhskhususDAO;
    }

    @Override
    public Mppumhskhusus getNew() {
        return getMppumhskhususDAO().getNew();
    }

    @Override
    public Mppumhskhusus getById(int _id) {
        return getMppumhskhususDAO().getById(_id);
    }

    @Override
    public List<Mppumhskhusus> getByMahasiswaId(int _id) {
        return getMppumhskhususDAO().getByMahasiswaId(_id);
    }

    @Override
    public List<Mppumhskhusus> getAll() {
        return getMppumhskhususDAO().getAll();
    }

    @Override
    public int getCount() {
        return getMppumhskhususDAO().getCount();
    }

    @Override
    public void saveOrUpdate(Mppumhskhusus entity) {
        getMppumhskhususDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Mppumhskhusus entity) {
        getMppumhskhususDAO().delete(entity);
    }

    @Override
    public void save(Mppumhskhusus entity) {
        getMppumhskhususDAO().save(entity);
    }
}
