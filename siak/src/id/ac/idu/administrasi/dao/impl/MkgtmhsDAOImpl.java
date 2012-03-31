package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.MkgtmhsDAO;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mkgtmhs;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 31 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MkgtmhsDAOImpl extends BasisDAO<Mkgtmhs> implements MkgtmhsDAO {
    @Override
    public Mkgtmhs getNew() {
        return new Mkgtmhs();
    }

    @Override
    public Mkgtmhs getById(int _id) {
        return get(Mkgtmhs.class, _id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mkgtmhs> getByMahasiswaId(int _id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mkgtmhs.class);
        criteria.add(Restrictions.eq("mahasiswa_id", _id));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mkgtmhs> getAll() {
        return getHibernateTemplate().loadAll(Mkgtmhs.class);
    }

    @Override
    public int getCount() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from mkgtmhs"));
    }

    @Override
    public void deleteById(int _id) {
        Mkgtmhs pkt = getById(_id);
		if(pkt != null) delete(pkt);
    }
}
