package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.FeedbackDosenDAO;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mpegawai;
import id.ac.idu.backend.model.Tfeedbackdosen;
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
    public List<Tfeedbackdosen> getFeedbackDosenByNip(String nip, String term, String kelompok) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tfeedbackdosen.class, "tfeedbackdosen");
        DetachedCriteria subCriteria = DetachedCriteria.forClass(Mpegawai.class, "tfeedbackdosen.mpegawai");
        subCriteria.setProjection(Projections.id());
        subCriteria.createAlias("model.tfeedbackdosen.mpegawai", "tfeedbackdosen.mpegawai", CriteriaSpecification.INNER_JOIN);
        subCriteria.createAlias("model.tfeedbackdosen", "tfeedbackdosen", CriteriaSpecification.INNER_JOIN);
        subCriteria.add(Restrictions.eqProperty(ConstantUtil.PEGAWAI_DOT_NIP, nip));
        subCriteria.add(Restrictions.eqProperty(ConstantUtil.TERM, term));
        subCriteria.add(Restrictions.eqProperty(ConstantUtil.KELOMPOK, kelompok));
        //criteria.add(Expression.eq("lang.langCode", "EN"));
        subCriteria.add(Restrictions.eqProperty("tfeedbackdosen.pegawaiId","mpegawai.pegawaiId"));
        //criteria.add(Subqueries.lt(0, subCriteria));
        return getHibernateTemplate().findByCriteria(criteria);
    }
}
