package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.MmhspascakhsDAO;
import id.ac.idu.administrasi.service.MmhspascakhsService;
import id.ac.idu.backend.model.Mmhspascakhs;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 24 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MmhspascakhsServiceImpl implements MmhspascakhsService {
    private MmhspascakhsDAO mmhspascakhsDAO;

    public MmhspascakhsDAO getMmhspascakhsDAO() {
        return mmhspascakhsDAO;
    }

    public void setMmhspascakhsDAO(MmhspascakhsDAO mmhspascakhsDAO) {
        this.mmhspascakhsDAO = mmhspascakhsDAO;
    }

    @Override
    public Mmhspascakhs getNew() {
        return getMmhspascakhsDAO().getNew();
    }

    @Override
    public Mmhspascakhs getById(int _id) {
        return getMmhspascakhsDAO().getById(_id);
    }

    @Override
    public List<Mmhspascakhs> getAll() {
        return getMmhspascakhsDAO().getAll();
    }

    @Override
    public int getCount() {
        return getMmhspascakhsDAO().getCount();
    }

    @Override
    public void saveOrUpdate(Mmhspascakhs entity) {
        getMmhspascakhsDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Mmhspascakhs entity) {
        getMmhspascakhsDAO().delete(entity);
    }

    @Override
    public void save(Mmhspascakhs entity) {
        getMmhspascakhsDAO().save(entity);
    }
}
