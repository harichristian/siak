package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.MpbahasamhsDAO;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mpbahasamhs;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 29 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MpbahasamhsDAOImpl extends BasisDAO<Mpbahasamhs> implements MpbahasamhsDAO {
    @Override
    public Mpbahasamhs getNew() {
        return new Mpbahasamhs();
    }

    @Override
    public Mpbahasamhs getById(int _id) {
         return get(Mpbahasamhs.class, _id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mpbahasamhs> getByMahasiswaId(int _id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mpbahasamhs.class);
        criteria.add(Restrictions.eq("mahasiswa_id", _id));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mpbahasamhs> getAll() {
        return getHibernateTemplate().loadAll(Mpbahasamhs.class);
    }

    @Override
    public int getCount() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from mpbahasamhs"));
    }

    @Override
    public void deleteById(int _id) {
        Mpbahasamhs pkt = getById(_id);
		if(pkt != null) delete(pkt);
    }
}
