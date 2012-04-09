package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.FeedbackDosenDAO;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mpegawai;
import id.ac.idu.backend.model.Tfeedbackdosen;
import id.ac.idu.backend.model.Tfeedbackwisudawan;
import id.ac.idu.util.ConstantUtil;
import org.hibernate.criterion.*;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 3/21/12
 * Time: 3:11 AM
 */
public class FeedbackDosenDAOImpl extends BasisDAO<Tfeedbackdosen> implements FeedbackDosenDAO {
    @Override
    public Tfeedbackdosen getNewFeedbackDosen() {
        return new Tfeedbackdosen();
    }

    @Override
    public Tfeedbackdosen getFeedbackDosenById(int id) {
        return get(Tfeedbackdosen.class, id);
    }

    @Override
    public List<Tfeedbackdosen> getAll() {
        return getHibernateTemplate().loadAll(Tfeedbackdosen.class);
    }

    @Override
    public int getCountAll() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Tfeedbackdosen"));
    }

    @Override
    public List<Tfeedbackdosen> getFeedbackDosenLikeTerm(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tfeedbackdosen.class);
        criteria.add(Restrictions.ilike(ConstantUtil.TERM, string, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Tfeedbackdosen> getFeedbackDosenLikeKelompok(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tfeedbackdosen.class);
        criteria.add(Restrictions.ilike(ConstantUtil.KELOMPOK, string, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Tfeedbackdosen> getFeedbackDosenLikePegawaiName(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tfeedbackdosen.class);
        //criteria.
        criteria.add(Restrictions.ilike(ConstantUtil.PEGAWAI_DOT_NAMA, string, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public void deleteFeedbackDosenById(int id) {
        Tfeedbackdosen obj = getFeedbackDosenById(id);
        if (obj != null) {
            delete(obj);
        }
    }
    @Override
    public List<Tfeedbackdosen> getFeedbackDosenByNip(Mpegawai nip, String term, String kelompok) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tfeedbackdosen.class);
        criteria.add(Restrictions.eq("mpegawai", nip));
        criteria.add(Restrictions.eq(ConstantUtil.TERM, term));
       // criteria.add(Restrictions.ilike(ConstantUtil.KELOMPOK, kelompok));

        return getHibernateTemplate().findByCriteria(criteria);
    }
}
