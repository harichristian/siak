package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.MfasilitasruangDAO;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mfasilitasruang;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/9/12
 * Time: 10:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class MfasilitasruangDAOImpl extends BasisDAO<Mfasilitasruang> implements MfasilitasruangDAO {

	@Override
	public Mfasilitasruang getNewMfasilitasruang() {
		return new Mfasilitasruang();
	}

	@Override
	public Mfasilitasruang getMfasilitasruangById(Long fil_Id) {
		return get(Mfasilitasruang.class, fil_Id);
	}

	@SuppressWarnings("unchecked")
	public Mfasilitasruang getMfasilitasruangByFilNr(String fil_nr) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Mfasilitasruang.class);
		criteria.add(Restrictions.eq("filNr", fil_nr));

		return (Mfasilitasruang) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
	}

	@Override
	public List<Mfasilitasruang> getAllMfasilitasruangs() {
		return getHibernateTemplate().loadAll(Mfasilitasruang.class);
	}

	@Override
	public void deleteMfasilitasruangById(long id) {
		Mfasilitasruang mfasilitasruang = getMfasilitasruangById(id);
		if (mfasilitasruang != null) {
			delete(mfasilitasruang);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Mfasilitasruang> getMfasilitasruangsLikeCity(String string) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Mfasilitasruang.class);
		criteria.add(Restrictions.ilike("filOrt", string, MatchMode.ANYWHERE));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Mfasilitasruang> getMfasilitasruangsLikeName1(String string) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Mfasilitasruang.class);
		criteria.add(Restrictions.ilike("filName1", string, MatchMode.ANYWHERE));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Mfasilitasruang> getMfasilitasruangsLikeNo(String string) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Mfasilitasruang.class);
		criteria.add(Restrictions.ilike("filNr", string, MatchMode.ANYWHERE));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public int getCountAllMfasilitasruangs() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Mfasilitasruang"));
	}
}
