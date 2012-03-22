package id.ac.idu.kurikulum.dao.impl;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mkurikulum;
import id.ac.idu.kurikulum.dao.KurikulumDAO;
import id.ac.idu.util.ConstantUtil;
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
 * Date: 3/9/12
 * Time: 2:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class KurikulumDAOImpl extends BasisDAO<Mkurikulum> implements KurikulumDAO {
    @Override
    public Mkurikulum getNewKurikulum() {
        return new Mkurikulum();
    }

    @Override
    public Mkurikulum getKurikulumById(int id) {
        return get(Mkurikulum.class, id);
    }

    @Override
    public Mkurikulum getKurikulumByCode(String code) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mkurikulum.class);
        criteria.add(Restrictions.eq(ConstantUtil.KURIKULUM_CODE, code));
        return (Mkurikulum) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Mkurikulum> getAllKurikulums() {
        return getHibernateTemplate().loadAll(Mkurikulum.class);
    }

    @Override
    public int getCountAllKurikulums() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Mkurikulum"));
    }

    @Override
    public List<Mkurikulum> getKurikulumLikeProdi(String name) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Mkurikulum> getKurikulumLikeCohort(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mkurikulum.class);
        criteria.add(Restrictions.ilike(ConstantUtil.COHORT, string, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public void deleteKurikulumById(int id) {
        Mkurikulum obj = getKurikulumById(id);
        if (obj != null) {
            delete(obj);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllKurikulumLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mkurikulum.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike(ConstantUtil.KURIKULUM_CODE, text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc(ConstantUtil.KURIKULUM_CODE));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Mkurikulum> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }
}
