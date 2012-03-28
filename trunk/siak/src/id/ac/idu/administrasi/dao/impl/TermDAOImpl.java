package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.TermDAO;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mterm;
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
 * Date: 28/03/12
 * Time: 10:26
 * To change this template use File | Settings | File Templates.
 */
public class TermDAOImpl extends BasisDAO<Mterm> implements TermDAO {
    @Override
    public Mterm getNewTerm() {
        return new Mterm();
    }

    @Override
    public Mterm getTermById(int id) {
        return get(Mterm.class, id);
    }

    @Override
    public Mterm getTermByKode(String kode) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mterm.class);
        criteria.add(Restrictions.eq("kdTerm", kode));

        return (Mterm) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Mterm> getAllTerm() {
        return getHibernateTemplate().loadAll(Mterm.class);
    }

    @Override
    public int getCountAllTerm() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from mterm"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllTermLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mterm.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike("deskripsi", text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc("deskripsi"));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Mterm> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }
}