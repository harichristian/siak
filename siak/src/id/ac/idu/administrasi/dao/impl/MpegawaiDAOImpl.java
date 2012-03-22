package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.MpegawaiDAO;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mpegawai;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/9/12
 * Time: 4:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class MpegawaiDAOImpl extends BasisDAO<Mpegawai> implements MpegawaiDAO {

    @Override
    public Mpegawai getNewMpegawai(){
        return new Mpegawai();
    }

    @Override
	public Mpegawai getMpegawaiById(Long fil_Id) {
		return get(Mpegawai.class, fil_Id);
	}

	@SuppressWarnings("unchecked")
	public Mpegawai getMpegawaiByFilNr(String fil_nr) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Mpegawai.class);
		criteria.add(Restrictions.eq("filNr", fil_nr));

		return (Mpegawai) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
	}

	@Override
	public List<Mpegawai> getAllMpegawais() {
		return getHibernateTemplate().loadAll(Mpegawai.class);
	}

	@Override
	public void deleteMpegawaiById(long id) {
		Mpegawai mpegawai = getMpegawaiById(id);
		if (mpegawai != null) {
			delete(mpegawai);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Mpegawai> getMpegawaisLikeCity(String string) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Mpegawai.class);
		criteria.add(Restrictions.ilike("filOrt", string, MatchMode.ANYWHERE));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Mpegawai> getMpegawaisLikeName1(String string) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Mpegawai.class);
		criteria.add(Restrictions.ilike("filName1", string, MatchMode.ANYWHERE));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Mpegawai> getMpegawaisLikeNo(String string) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Mpegawai.class);
		criteria.add(Restrictions.ilike("filNr", string, MatchMode.ANYWHERE));

		return getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public int getCountAllMpegawais() {
		return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Mpegawai"));
	}
}
