package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.CalendarAkademikDAO;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mcalakademik;
import id.ac.idu.backend.model.Mkegiatan;
import id.ac.idu.backend.model.Mprodi;
import id.ac.idu.backend.model.Msekolah;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 09/03/12
 * Time: 0:42
 * To change this template use File | Settings | File Templates.
 */
public class CalendarAkademikDAOImpl extends BasisDAO<Mcalakademik> implements CalendarAkademikDAO {

    @Override
    public Mcalakademik getNewCalakademik() {
        return new Mcalakademik();
    }

    @Override
    public Mcalakademik getCalakademikById(int id) {
        return get(Mcalakademik.class, id);
    }

    @Override
    public Mcalakademik getCalakademikByNo(String no) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mcalakademik.class);
        criteria.add(Restrictions.eq("cno", no));

        return (Mcalakademik) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public Mcalakademik getCalakademikByTahunAjaran(String tahunAjaran) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mcalakademik.class);
        criteria.add(Restrictions.eq("cthajar", tahunAjaran));

        return (Mcalakademik) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public Mcalakademik getCalakademikByTerm(String term) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mcalakademik.class);
        criteria.add(Restrictions.eq("cterm", term));

        return (Mcalakademik) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Mcalakademik> getCalakademikByProdi(Mprodi prodi) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mcalakademik.class);
        criteria.add(Restrictions.eq("mprodi", prodi));
        criteria.addOrder(org.hibernate.criterion.Order.asc("cno")); // set the order

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mcalakademik> getCalakademikBySekolah(Msekolah sekolah) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mcalakademik.class);
        criteria.add(Restrictions.eq("msekolah", sekolah));
        criteria.addOrder(org.hibernate.criterion.Order.asc("cno")); // set the order

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mcalakademik> getCalakademikByKegiatan(Mkegiatan kegiatan) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mcalakademik.class);
        criteria.add(Restrictions.eq("mkegiatan", kegiatan));
        criteria.addOrder(org.hibernate.criterion.Order.asc("cno")); // set the order

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mcalakademik> getCalakademikBySekolahAndProdi(Msekolah sekolah, Mprodi prodi) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mcalakademik.class);
        if (sekolah != null) {
            criteria.add(Restrictions.eq("msekolah", sekolah));
        }
        if (prodi != null) {
            criteria.add(Restrictions.eq("mprodi", prodi));
        }
        criteria.addOrder(org.hibernate.criterion.Order.asc("cno")); // set the order

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mcalakademik> getAllCalakademik() {
        return getHibernateTemplate().loadAll(Mcalakademik.class);
    }

    @Override
    public int getCountAllCalakademik() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from mcalakademik"));
    }

    @Override
    public void deleteCalakademikById(int id) {
        Mcalakademik calakademik = getCalakademikById(id);
        if (calakademik != null) {
            delete(calakademik);
        }
    }
}
