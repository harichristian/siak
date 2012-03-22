package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.SekolahDAO;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mpegawai;
import id.ac.idu.backend.model.Msekolah;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 08/03/12
 * Time: 0:34
 * To change this template use File | Settings | File Templates.
 */
public class SekolahDAOImpl extends BasisDAO<Msekolah> implements SekolahDAO {
    @Override
    public Msekolah getNewSekolah() {
        return new Msekolah();
    }

    @Override
    public Msekolah getSekolahById(int id) {
        return get(Msekolah.class, id);
    }

    @Override
    public Msekolah getSekolahByCode(String code) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Msekolah.class);
        criteria.add(Restrictions.eq("ckdsekolah", code));

        return (Msekolah) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public Msekolah getSekolahByName(String name) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Msekolah.class);
        criteria.add(Restrictions.eq("csekolah", name));

        return (Msekolah) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Msekolah> getSekolahByPegawai(Mpegawai pegawai) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Msekolah.class);
        criteria.add(Restrictions.eq("mpegawai", pegawai));
        criteria.addOrder(org.hibernate.criterion.Order.asc("ckdsekolah")); // set the order

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Msekolah> getAllSekolah() {
        return getHibernateTemplate().loadAll(Msekolah.class);
    }

    @Override
    public int getCountAllSekolah() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from msekolah"));
    }

    @Override
    public void deleteSekolahById(int id) {
        Msekolah sekolah = getSekolahById(id);
        if (sekolah != null) {
            delete(sekolah);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllSekolahLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Msekolah.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike("cnamaSekolah", text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc("cnamaSekolah"));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Msekolah> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }
}
