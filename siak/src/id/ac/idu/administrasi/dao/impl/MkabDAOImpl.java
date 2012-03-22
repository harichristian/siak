package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.MkabDAO;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mkab;
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
 * Time: 12:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class MkabDAOImpl extends BasisDAO<Mkab> implements MkabDAO {

    @Override
    public Mkab getNewMkab() {
        return new Mkab();
    }

    @Override
    public Mkab getMkabById(Long fil_Id) {
        return get(Mkab.class, fil_Id);
    }

    @SuppressWarnings("unchecked")
    public Mkab getMkabByFilNr(String fil_nr) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mkab.class);
        criteria.add(Restrictions.eq("filNr", fil_nr));

        return (Mkab) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Mkab> getAllMkabs() {
        return getHibernateTemplate().loadAll(Mkab.class);
    }

    @Override
    public void deleteMkabById(long id) {
        Mkab mkab = getMkabById(id);
        if (mkab != null) {
            delete(mkab);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mkab> getMkabsLikeCity(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Mkab.class);
        criteria.add(Restrictions.ilike("filOrt", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mkab> getMkabsLikeName1(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Mkab.class);
        criteria.add(Restrictions.ilike("filName1", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mkab> getMkabsLikeNo(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Mkab.class);
        criteria.add(Restrictions.ilike("filNr", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public int getCountAllMkabs() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Mkab"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllAlumniLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mkab.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike("cnamaKab", text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc("cnamaKab"));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Mkab> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }
}
