package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.ProdiDAO;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Branche;
import id.ac.idu.backend.model.Mprodi;
import id.ac.idu.backend.model.Mprodi;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/7/12
 * Time: 8:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProdiDAOImpl extends BasisDAO<Mprodi> implements ProdiDAO {
    @Override
    public Mprodi getNewProdi() {
        return new Mprodi();
    }

    @Override
    public Mprodi getProdiById(int id) {
        return get(Mprodi.class, id);
    }

    @Override
    public Mprodi getProdiByCode(String code) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mprodi.class);
        criteria.add(Restrictions.eq("ckdprogst", code));

        return (Mprodi) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Mprodi> getAllProdis() {
        return getHibernateTemplate().loadAll(Mprodi.class);
    }

    @Override
    public int getCountAllProdis() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Mprodi"));
    }

    @Override
    public List<Mprodi> getProdisLikeName(String name) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mprodi.class);
        criteria.add(Restrictions.ilike("cnmprogst", name, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mprodi> getProdisLikeSingkatan(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mprodi.class);
        criteria.add(Restrictions.ilike("csingkat", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mprodi> getProdiLikeStatus(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mprodi.class);
        criteria.add(Restrictions.ilike("cstatus", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public void deleteProdiById(int id) {
        Mprodi obj = getProdiById(id);
        if (obj != null) {
            delete(obj);
        }
    }

    @Override
    public void initialize(Mprodi obj) {
        super.initialize(obj);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllProdiLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mprodi.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike("cnmprogst", text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc("cnmprogst"));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Mprodi> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }
}
