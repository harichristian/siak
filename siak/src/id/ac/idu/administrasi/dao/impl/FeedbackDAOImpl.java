package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.FeedbackDAO;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mfeedback;
import id.ac.idu.util.ConstantUtil;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 3/21/12
 * Time: 3:30 AM
 */
public class FeedbackDAOImpl extends BasisDAO<Mfeedback> implements FeedbackDAO{
    @Override
    public Mfeedback getNewFeedback() {
        return new Mfeedback();
    }

    @Override
    public Mfeedback getFeedbackById(int id) {
        return get(Mfeedback.class, id);
    }

    @Override
    public List<Mfeedback> getAll() {
        return getHibernateTemplate().loadAll(Mfeedback.class);
    }

    @Override
    public int getCountAll() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Mfeedback"));
    }

    @Override
    public List<Mfeedback> getFeedbackLikeCode(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mfeedback.class);
        criteria.add(Restrictions.ilike(ConstantUtil.FEEDBACK_CODE, string, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mfeedback> getFeedbackLikeProdiName(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mfeedback.class);
        criteria.add(Restrictions.ilike(ConstantUtil.PRODI_DOT_NAME, string, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mfeedback> getFeedbackLikeSekolahName(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mfeedback.class);
        criteria.add(Restrictions.ilike(ConstantUtil.SEKOLAH_DOT_NAME, string, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public void deleteFeedbackById(int id) {
        Mfeedback obj = getFeedbackById(id);
        if (obj != null) {
            delete(obj);
        }
    }
}
