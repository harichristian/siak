package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.MhistkursusmhsDAO;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mhistkursusmhs;
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

public class MhistkursusmhsDAOImpl extends BasisDAO<Mhistkursusmhs> implements MhistkursusmhsDAO {
    @Override
    public Mhistkursusmhs getNew() {
        return new Mhistkursusmhs();
    }

    @Override
    public Mhistkursusmhs getById(int _id) {
        return get(Mhistkursusmhs.class, _id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mhistkursusmhs> getByMahasiswaId(int _id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mhistkursusmhs.class);
        criteria.add(Restrictions.eq("mahasiswa_id", _id));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mhistkursusmhs> getAll() {
        return getHibernateTemplate().loadAll(Mhistkursusmhs.class);
    }

    @Override
    public int getCount() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from mhistkursusmhs"));
    }

    @Override
    public void deleteById(int _id) {
        Mhistkursusmhs pkt = getById(_id);
		if(pkt != null) delete(pkt);
    }
}
