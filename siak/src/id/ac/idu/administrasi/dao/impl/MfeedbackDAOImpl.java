package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.MfeedbackDAO;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mfeedback;
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
 * Date: 3/21/12
 * Time: 4:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class MfeedbackDAOImpl extends BasisDAO<Mfeedback> implements MfeedbackDAO {

    @Override
    public Mfeedback getNewMfeedback() {
        return new Mfeedback();
    }

    @Override
    public Mfeedback getMfeedbackById(Long fil_Id) {
        return get(Mfeedback.class, fil_Id);
    }

    @SuppressWarnings("unchecked")
    public Mfeedback getMfeedbackByFilNr(String fil_nr) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mfeedback.class);
        criteria.add(Restrictions.eq("filNr", fil_nr));

        return (Mfeedback) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Mfeedback> getAllMfeedbacks() {
        return getHibernateTemplate().loadAll(Mfeedback.class);
    }

    @Override
    public void deleteMfeedbackById(long id) {
        Mfeedback mfeedback = getMfeedbackById(id);
        if (mfeedback != null) {
            delete(mfeedback);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mfeedback> getMfeedbacksLikeCity(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Mfeedback.class);
        criteria.add(Restrictions.ilike("filOrt", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mfeedback> getMfeedbacksLikeName1(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Mfeedback.class);
        criteria.add(Restrictions.ilike("filName1", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mfeedback> getMfeedbacksLikeNo(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Mfeedback.class);
        criteria.add(Restrictions.ilike("filNr", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public int getCountAllMfeedbacks() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Mfeedback"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllAlumniLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mfeedback.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike("cnamaKab", text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc("cnamaKab"));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Mfeedback> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }
}

