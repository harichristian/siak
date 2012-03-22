package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.KodePosDAO;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.MkodePos;
import org.springframework.dao.support.DataAccessUtils;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 13 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class KodePosDAOImpl extends BasisDAO<MkodePos> implements KodePosDAO {
    @Override
    public MkodePos getNewKodePos() {
        return new MkodePos();
    }

    @Override
    public MkodePos getKodePosById(int id) {
        return get(MkodePos.class, id);
    }

    @Override
    public MkodePos getKodePosByStringId(String id) {
        return get(MkodePos.class, id);
    }

    @Override
    public MkodePos getKodePos(String code) {
        return get(MkodePos.class, code);
    }

    @Override
    public void deleteKodePosById(int id) {
        MkodePos kps = getKodePosById(id);
		if(kps != null) delete(kps);
    }

    @Override
    public int getCount() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from MkodePos"));
    }
}
