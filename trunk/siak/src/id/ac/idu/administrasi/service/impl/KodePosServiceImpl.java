package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.KodePosDAO;
import id.ac.idu.administrasi.service.KodePosService;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.MkodePos;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 13 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class KodePosServiceImpl implements KodePosService {
    private KodePosDAO kodePosDAO;

    public KodePosDAO getKodePosDAO() {
        return kodePosDAO;
    }

    public void setKodePosDAO(KodePosDAO kodePosDAO) {
        this.kodePosDAO = kodePosDAO;
    }

    @Override
    public MkodePos getNewKodePos() {
        return getKodePosDAO().getNewKodePos();
    }

    @Override
    public MkodePos getKodePosById(int id) {
        return getKodePosDAO().getKodePosById(id);
    }

    @Override
    public MkodePos getKodePosByStringId(String id) {
        return getKodePosDAO().getKodePosByStringId(id);
    }

    @Override
    public MkodePos getKodePos(String code) {
        return getKodePosDAO().getKodePos(code);
    }

    @Override
    public int getCount() {
        return getKodePosDAO().getCount();
    }

    @Override
    public void saveOrUpdate(MkodePos entity) {
        getKodePosDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(MkodePos entity) {
        getKodePosDAO().delete(entity);
    }

    @Override
    public void save(MkodePos entity) {
        getKodePosDAO().save(entity);
    }

    @Override
    public ResultObject getAllMkodePosLikeText(String text, int start, int pageSize) {
        return getKodePosDAO().getAllMkodePosLikeText(text, start, pageSize);
    }
}
