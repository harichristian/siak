package id.ac.idu.kurikulum.dao.impl;

import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mkurmhs;
import id.ac.idu.kurikulum.dao.KurikulumMahasiswaDAO;
import id.ac.idu.util.ConstantUtil;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/9/12
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class KurikulumMahasiswaDAOImpl extends BasisDAO<Mkurmhs> implements KurikulumMahasiswaDAO{
    @Override
    public Mkurmhs getNewKurikulumMahasiswa() {
        return new Mkurmhs();
    }

    @Override
    public Mkurmhs getKurikulumMahasiswaById(int id) {
        return get(Mkurmhs.class, id);
    }

    @Override
    public Mkurmhs getKurikulumMahasiswaByCode(String code) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mkurmhs.class);
		criteria.add(Restrictions.eq(ConstantUtil.KURIKULUM, code));
        return (Mkurmhs) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Mkurmhs> getAllKurikulumMahasiswas() {
        return getHibernateTemplate().loadAll(Mkurmhs.class);
    }

    @Override
    public int getCountAllKurikulumMahasiswas() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Mkurmhs"));
    }

    @Override
    public List<Mkurmhs> getKurikulumMahasiswaLikeProdi(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mkurmhs.class);
		criteria.add(Restrictions.ilike(ConstantUtil.PRODI, string, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mkurmhs> getKurikulumMahasiswaLikeCohort(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mkurmhs.class);
		criteria.add(Restrictions.ilike(ConstantUtil.COHORT, string, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public void deleteKurikulumMahasiswaById(int id) {
        Mkurmhs obj = getKurikulumMahasiswaById(id);
		if (obj != null) {
			delete(obj);
		}
    }
}
