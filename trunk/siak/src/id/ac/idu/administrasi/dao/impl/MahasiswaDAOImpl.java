package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.MahasiswaDAO;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mmahasiswa;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 10 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MahasiswaDAOImpl extends BasisDAO<Mmahasiswa> implements MahasiswaDAO {
    @Override
    public Mmahasiswa getNew() {
        return new Mmahasiswa();
    }

    @Override
    public Mmahasiswa getById(Long _id) {
        return get(Mmahasiswa.class, _id);
    }

    @Override
    public int getCount() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Mmahasiswa"));
    }

    @Override
    public List<Mmahasiswa> getAll() {
        return getHibernateTemplate().loadAll(Mmahasiswa.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mmahasiswa> getByNim(String _nim) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mmahasiswa.class);
        criteria.add(Restrictions.ilike("cnim", _nim, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mmahasiswa> getByNama(String _Nama) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mmahasiswa.class);
        criteria.add(Restrictions.ilike("cnama", _Nama, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public void deleteById(Long _id) {
        Mmahasiswa mhs = getById(_id);
		if(mhs != null) delete(mhs);
    }
}
