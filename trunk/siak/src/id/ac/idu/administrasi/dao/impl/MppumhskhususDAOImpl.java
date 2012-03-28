package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.MppumhskhususDAO;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mppumhskhusus;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 25 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MppumhskhususDAOImpl extends BasisDAO<Mppumhskhusus> implements MppumhskhususDAO {
    @Override
    public Mppumhskhusus getNew() {
        return new Mppumhskhusus();
    }

    @Override
    public Mppumhskhusus getById(int _id) {
        return get(Mppumhskhusus.class, _id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mppumhskhusus> getByMahasiswaId(int _id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mppumhskhusus.class);
        criteria.add(Restrictions.eq("mahasiswa_id", _id));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mppumhskhusus> getAll() {
        return getHibernateTemplate().loadAll(Mppumhskhusus.class);
    }

    @Override
    public int getCount() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from mppumhskhusus"));
    }

    @Override
    public void deleteById(int _id) {
        Mppumhskhusus khs = getById(_id);
		if(khs != null) delete(khs);
    }
}
