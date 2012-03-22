package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.MkelDAO;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mkel;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/19/12
 * Time: 12:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class MkelDAOImpl extends BasisDAO<Mkel> implements MkelDAO {

    @Override
    public Mkel getNewMkel() {
        return new Mkel();
    }

    @Override
    public Mkel getMkelById(Long fil_Id) {
        return get(Mkel.class, fil_Id);
    }

    @SuppressWarnings("unchecked")
    public Mkel getMkelByFilNr(String fil_nr) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mkel.class);
        criteria.add(Restrictions.eq("filNr", fil_nr));

        return (Mkel) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Mkel> getAllMkels() {
        return getHibernateTemplate().loadAll(Mkel.class);
    }

    @Override
    public void deleteMkelById(long id) {
        Mkel mkel = getMkelById(id);
        if (mkel != null) {
            delete(mkel);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mkel> getMkelsLikeCity(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Mkel.class);
        criteria.add(Restrictions.ilike("filOrt", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mkel> getMkelsLikeName1(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Mkel.class);
        criteria.add(Restrictions.ilike("filName1", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mkel> getMkelsLikeNo(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Mkel.class);
        criteria.add(Restrictions.ilike("filNr", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public int getCountAllMkels() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Mkel"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllAlumniLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mkel.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike("cnamaKel", text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc("cnamaKel"));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Mkel> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }
}
