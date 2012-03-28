package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.MhistpangkatmhsDAO;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mhistpangkatmhs;
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

public class MhistpangkatmhsDAOImpl extends BasisDAO<Mhistpangkatmhs> implements MhistpangkatmhsDAO {
    @Override
    public Mhistpangkatmhs getNew() {
        return new Mhistpangkatmhs();
    }

    @Override
    public Mhistpangkatmhs getById(int _id) {
        return get(Mhistpangkatmhs.class, _id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mhistpangkatmhs> getByMahasiswaId(int _id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mhistpangkatmhs.class);
        criteria.add(Restrictions.eq("mahasiswa_id", _id));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mhistpangkatmhs> getAll() {
        return getHibernateTemplate().loadAll(Mhistpangkatmhs.class);
    }

    @Override
    public int getCount() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from mhistpangkatmhs"));
    }

    @Override
    public void deleteById(int _id) {
        Mhistpangkatmhs pkt = getById(_id);
		if(pkt != null) delete(pkt);
    }
}
