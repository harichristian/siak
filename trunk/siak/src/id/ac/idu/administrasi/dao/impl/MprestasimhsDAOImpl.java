package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.MprestasimhsDAO;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mprestasimhs;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 02 Apr 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MprestasimhsDAOImpl extends BasisDAO<Mprestasimhs> implements MprestasimhsDAO {
    @Override
    public Mprestasimhs getNew() {
        return new Mprestasimhs();
    }

    @Override
    public Mprestasimhs getById(int _id) {
        return get(Mprestasimhs.class, _id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mprestasimhs> getByMahasiswaId(int _id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mprestasimhs.class);
        criteria.add(Restrictions.eq("mahasiswa_id", _id));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mprestasimhs> getAll() {
        return getHibernateTemplate().loadAll(Mprestasimhs.class);
    }

    @Override
    public int getCount() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from mprestasimhs"));
    }

    @Override
    public void deleteById(int _id) {
        Mprestasimhs khs = getById(_id);
		if(khs != null) delete(khs);
    }
}
