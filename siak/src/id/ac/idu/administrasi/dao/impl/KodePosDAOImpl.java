package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.KodePosDAO;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Branche;
import id.ac.idu.backend.model.MkodePos;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

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
    public MkodePos getKodePosById(String id) {
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
    public void deleteKodePosById(String id) {
        MkodePos kps = getKodePosById(id);
		if(kps != null) delete(kps);
    }

    @Override
    public int getCount() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from MkodePos"));
    }

       @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllMkodePosLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(MkodePos.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike("kodepos", text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc("kodepos"));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Branche> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }
}
