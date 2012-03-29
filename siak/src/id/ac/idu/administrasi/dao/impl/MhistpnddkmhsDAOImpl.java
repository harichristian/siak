package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.MhistpnddkmhsDAO;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mhistpnddkmhs;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 28 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MhistpnddkmhsDAOImpl extends BasisDAO<Mhistpnddkmhs> implements MhistpnddkmhsDAO {
    @Override
    public Mhistpnddkmhs getNew() {
        return new Mhistpnddkmhs();
    }

    @Override
    public Mhistpnddkmhs getById(int _id) {
        return get(Mhistpnddkmhs.class, _id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mhistpnddkmhs> getByMahasiswaId(int _id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mhistpnddkmhs.class);
        criteria.add(Restrictions.eq("mahasiswa_id", _id));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mhistpnddkmhs> getAll() {
        return getHibernateTemplate().loadAll(Mhistpnddkmhs.class);
    }

    @Override
    public int getCount() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from mhistpnddkmhs"));
    }

    @Override
    public void deleteById(int _id) {
        Mhistpnddkmhs pkt = getById(_id);
		if(pkt != null) delete(pkt);
    }
}
