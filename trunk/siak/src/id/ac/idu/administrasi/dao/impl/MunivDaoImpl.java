/**
 *
 */
package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.MunivDao;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Muniv;
import id.ac.idu.backend.model.Muniv;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author valeo
 */
@Repository
public class MunivDaoImpl extends BasisDAO<Muniv> implements MunivDao {

    /* (non-Javadoc)
      * @see id.ac.idu.backend.dao.MunivDao#getNewMuniv()
      */
    @Override
    public Muniv getNewMuniv() {
        return new Muniv();
    }

    /* (non-Javadoc)
      * @see id.ac.idu.backend.dao.MunivDao#getMunivById(java.lang.Long)
      */
    @Override
    public Muniv getMunivById(Long munivId) {
        return get(Muniv.class, munivId);
    }

    /* (non-Javadoc)
      * @see id.ac.idu.backend.dao.MunivDao#getMunivByCkdUniv(java.lang.String)
      */
    @SuppressWarnings("unchecked")
    @Override
    public Muniv getMunivByCkdUniv(String ckdUniv) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Muniv.class);
        criteria.add(Restrictions.eq("ckdUniv", ckdUniv));

        return (Muniv) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    /* (non-Javadoc)
      * @see id.ac.idu.backend.dao.MunivDao#getAllMunivs()
      */
    @Override
    public List<Muniv> getAllMunivs() {
        return getHibernateTemplate().loadAll(Muniv.class);
    }

    /* (non-Javadoc)
      * @see id.ac.idu.backend.dao.MunivDao#getCountAllMunivs()
      */
    @Override
    public int getCountAllMunivs() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Muniv"));
    }

    /* (non-Javadoc)
      * @see id.ac.idu.backend.dao.MunivDao#getMunivsLikeName(java.lang.String)
      */
    @SuppressWarnings("unchecked")
    @Override
    public List<Muniv> getMunivsLikeName(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Muniv.class);
        criteria.add(Restrictions.ilike("cnamaUniv", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    /* (non-Javadoc)
      * @see id.ac.idu.backend.dao.MunivDao#getMunivsLikeName1(java.lang.String)
      */
    @SuppressWarnings("unchecked")
    @Override
    public List<Muniv> getMunivsLikeName1(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Muniv.class);
        criteria.add(Restrictions.ilike("cnamaUniv", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    /* (non-Javadoc)
      * @see id.ac.idu.backend.dao.MunivDao#getMunivsLikeCode(java.lang.String)
      */
    @SuppressWarnings("unchecked")
    @Override
    public List<Muniv> getMunivsLikeCode(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Muniv.class);
        criteria.add(Restrictions.ilike("ckdUniv", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    /* (non-Javadoc)
      * @see id.ac.idu.backend.dao.MunivDao#deleteMunivById(long)
      */
    @Override
    public void deleteMunivById(long id) {
        Muniv muniv = getMunivById(id);
        if (muniv != null) {
            delete(muniv);
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllUnivLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Muniv.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike("cnamaUniv", text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc("cnamaUniv"));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Muniv> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }
}
