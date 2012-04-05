package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.ThajarDAO;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mthajar;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 05/04/12
 * Time: 14:29
 * To change this template use File | Settings | File Templates.
 */
public class ThajarDAOImpl extends BasisDAO<Mthajar> implements ThajarDAO {
    @Override
    public Mthajar getNewThajar() {
        return new Mthajar();
    }

    @Override
    public Mthajar getThajarById(int id) {
        return get(Mthajar.class, id);
    }

    @Override
    public Mthajar getThajarByKode(String kode) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mthajar.class);
        criteria.add(Restrictions.eq("kdThajar", kode));

        return (Mthajar) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Mthajar> getAllThajar() {
        return getHibernateTemplate().loadAll(Mthajar.class);
    }

    @Override
    public int getCountAllThajar() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from mthajar"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllThajarLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mthajar.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike("kdThajar", text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc("kdThajar"));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Mthajar> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }
}