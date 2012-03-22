package id.ac.idu.irs.dao.impl;

import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Tcutimhs;
import id.ac.idu.irs.dao.CutimhsDAO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.Date;
import java.util.List;

/**
 * User: hermanto
 * Date: 08 Mar 12
 * Time: 9:03:21
 */

public class CutimhsDAOImpl extends BasisDAO<Tcutimhs> implements CutimhsDAO {

    @Override
    public Tcutimhs getNew() {
        return new Tcutimhs();
    }

    @Override
    public Tcutimhs getByNo(String _no) {
        return get(Tcutimhs.class, _no);
    }

    @Override
    public List<Tcutimhs> getAll() {
        return getHibernateTemplate().loadAll(Tcutimhs.class);
    }

    @Override
    public List<Tcutimhs> getByTanggal(Date _tanggal) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tcutimhs.class);
		criteria.add(Restrictions.eq("dtglsurat", _tanggal));
		return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Tcutimhs> getByTahun(String _tahun) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tcutimhs.class);
		criteria.add(Restrictions.eq("cthajar", _tahun));
		return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public void deleteByNo(String _no) {
        Tcutimhs cutimhs = getByNo(_no);
		if(cutimhs != null) delete(cutimhs);
    }

    @Override
    public int getCount() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Tcutimhs"));
    }
}
