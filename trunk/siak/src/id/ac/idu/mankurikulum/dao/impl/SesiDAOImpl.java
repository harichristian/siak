package id.ac.idu.mankurikulum.dao.impl;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Msesikuliah;
import id.ac.idu.mankurikulum.dao.SesiDAO;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

public class SesiDAOImpl extends BasisDAO<Msesikuliah> implements SesiDAO {

    @Override
    public Msesikuliah getNewSesi() {
        return new Msesikuliah();
    }

    @Override
    public Msesikuliah getSesiById(int id) {
        return get(Msesikuliah.class, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Msesikuliah getSesiByCode(String code) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Msesikuliah.class);
        criteria.add(Restrictions.eq("ckdsesi", code));
        return (Msesikuliah) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Msesikuliah> getAllSesi() {
        return getHibernateTemplate().loadAll(Msesikuliah.class);
    }

    @Override
    public void deleteSesiById(int id) {
        Msesikuliah msesikuliah = getSesiById(id);
        if (msesikuliah != null) delete(msesikuliah);

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Msesikuliah> getSesiByJamAwal(String jamawal) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Msesikuliah.class);
        criteria.add(Restrictions.eq("cjamawal", jamawal));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Msesikuliah> getSesiByJamAkhir(String jamakhir) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Msesikuliah.class);
        criteria.add(Restrictions.eq("cjamakhir", jamakhir));
        return getHibernateTemplate().findByCriteria(criteria);
    }


    @Override
    public int getCountAllSesi() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Msesikuliah"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllSesiLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Msesikuliah.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike("ckdsesi", text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc("ckdsesi"));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Msesikuliah> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }
}
