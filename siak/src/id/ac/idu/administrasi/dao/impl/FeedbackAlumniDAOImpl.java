package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.FeedbackAlumniDAO;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Tfeedbackalumni;
import id.ac.idu.util.ConstantUtil;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 3/21/12
 * Time: 3:11 AM
 */
public class FeedbackAlumniDAOImpl extends BasisDAO<Tfeedbackalumni> implements FeedbackAlumniDAO {
    @Override
    public Tfeedbackalumni getNewFeedbackAlumni() {
        return new Tfeedbackalumni();
    }

    @Override
    public Tfeedbackalumni getFeedbackAlumniById(int id) {
        return get(Tfeedbackalumni.class, id);
    }

    @Override
    public List<Tfeedbackalumni> getAll() {
        return getHibernateTemplate().loadAll(Tfeedbackalumni.class);
    }

    @Override
    public int getCountAll() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Tfeedbackalumni"));
    }

    @Override
    public List<Tfeedbackalumni> getFeedbackAlumniLikeTerm(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tfeedbackalumni.class);
        criteria.add(Restrictions.ilike(ConstantUtil.TERM, string, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Tfeedbackalumni> getFeedbackAlumniLikeKelompok(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tfeedbackalumni.class);
        criteria.add(Restrictions.ilike(ConstantUtil.KELOMPOK, string, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Tfeedbackalumni> getFeedbackAlumniLikeMahasiswaName(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tfeedbackalumni.class);
        criteria.add(Restrictions.ilike(ConstantUtil.MAHASISWA_DOT_NAME, string, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public void deleteFeedbackAlumniById(int id) {
        Tfeedbackalumni obj = getFeedbackAlumniById(id);
        if (obj != null) {
            delete(obj);
        }
    }
}
