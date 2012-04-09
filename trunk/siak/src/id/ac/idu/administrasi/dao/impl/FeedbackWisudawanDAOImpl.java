package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.FeedbackWisudawanDAO;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mmahasiswa;
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
public class FeedbackWisudawanDAOImpl extends BasisDAO<Tfeedbackwisudawan> implements FeedbackWisudawanDAO {
    @Override
    public Tfeedbackwisudawan getNewFeedbackWisudawan() {
        return new Tfeedbackwisudawan();
    }

    @Override
    public Tfeedbackwisudawan getFeedbackWisudawanById(int id) {
        return get(Tfeedbackwisudawan.class, id);
    }

    @Override
    public List<Tfeedbackwisudawan> getAll() {
        return getHibernateTemplate().loadAll(Tfeedbackwisudawan.class);
    }

    @Override
    public int getCountAll() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Tfeedbackwisudawan"));
    }

    @Override
    public List<Tfeedbackwisudawan> getFeedbackWisudawanLikeTerm(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tfeedbackwisudawan.class);
        criteria.add(Restrictions.ilike(ConstantUtil.TERM, string, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Tfeedbackwisudawan> getFeedbackWisudawanLikeKelompok(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tfeedbackwisudawan.class);
        criteria.add(Restrictions.ilike(ConstantUtil.KELOMPOK, string, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Tfeedbackwisudawan> getFeedbackWisudawanLikeMahasiswaName(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tfeedbackwisudawan.class);
        //criteria.
        criteria.add(Restrictions.ilike(ConstantUtil.MAHASISWA_DOT_NAMA, string, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public void deleteFeedbackWisudawanById(int id) {
        Tfeedbackwisudawan obj = getFeedbackWisudawanById(id);
        if (obj != null) {
            delete(obj);
        }
    }
    @Override
    public List<Tfeedbackwisudawan> getFeedbackWisudawanByNim(Mmahasiswa mhs, String term, String kelompok) {
      DetachedCriteria criteria = DetachedCriteria.forClass(Tfeedbackwisudawan.class);
//        criteria.add(Restrictions.ilike(ConstantUtil.MAHASISWA_DOT_NIM, nim, MatchMode.ANYWHERE));
        criteria.add(Restrictions.eq("mmahasiswa", mhs));
        criteria.add(Restrictions.eq(ConstantUtil.TERM, term));
       // criteria.add(Restrictions.ilike(ConstantUtil.KELOMPOK, kelompok));

        return getHibernateTemplate().findByCriteria(criteria);
    }
}
