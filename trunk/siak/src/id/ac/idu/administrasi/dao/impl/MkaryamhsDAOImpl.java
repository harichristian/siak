package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.MkaryamhsDAO;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mkaryamhs;
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

public class MkaryamhsDAOImpl extends BasisDAO<Mkaryamhs> implements MkaryamhsDAO {
    @Override
    public Mkaryamhs getNew() {
        return new Mkaryamhs();
    }

    @Override
    public Mkaryamhs getById(int _id) {
        return get(Mkaryamhs.class, _id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mkaryamhs> getByMahasiswaId(int _id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mkaryamhs.class);
        criteria.add(Restrictions.eq("mahasiswa_id", _id));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mkaryamhs> getAll() {
        return getHibernateTemplate().loadAll(Mkaryamhs.class);
    }

    @Override
    public int getCount() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from mkgtmhs"));
    }

    @Override
    public void deleteById(int _id) {
        Mkaryamhs pkt = getById(_id);
		if(pkt != null) delete(pkt);
    }
}
