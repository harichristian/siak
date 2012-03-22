package id.ac.idu.irs.service.impl;

import id.ac.idu.administrasi.dao.MahasiswaDAO;
import id.ac.idu.backend.dao.impl.OfficeDAOImpl;
import id.ac.idu.backend.model.Tcutimhs;
import id.ac.idu.irs.dao.CutimhsDAO;
import id.ac.idu.irs.service.CutimhsService;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 10 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class CutimhsServiceImpl implements CutimhsService {
    private CutimhsDAO cutimhsDAO;

    public CutimhsDAO getCutimhsDAO() {
        return cutimhsDAO;
    }

    public void setCutimhsDAO(CutimhsDAO cutimhsDAO) {
        this.cutimhsDAO = cutimhsDAO;
    }

    @Override
    public Tcutimhs getNew() {
        return this.getCutimhsDAO().getNew();
    }

    @Override
    public Tcutimhs getByNo(String _no) {
        return this.getCutimhsDAO().getByNo(_no);
    }

    @Override
    public int getCount() {
        return this.getCutimhsDAO().getCount();
    }

    @Override
    public List<Tcutimhs> getAll() {
        return this.getCutimhsDAO().getAll();
    }

    @Override
    public List<Tcutimhs> getByTanggal(Date _tanggal) {
        return this.getCutimhsDAO().getByTanggal(_tanggal);
    }

    @Override
    public List<Tcutimhs> getByTahun(String _tahun) {
        return this.getCutimhsDAO().getByTahun(_tahun);
    }

    @Override
    public void saveOrUpdate(Tcutimhs entity) {
            this.getCutimhsDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Tcutimhs entity) {
        this.getCutimhsDAO().delete(entity);
    }

    @Override
    public void save(Tcutimhs entity) {
        this.getCutimhsDAO().save(entity);
    }
}
