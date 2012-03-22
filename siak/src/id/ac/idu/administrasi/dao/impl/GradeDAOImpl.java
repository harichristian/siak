package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.GradeDAO;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mgrade;
import id.ac.idu.backend.model.Mjenjang;
import id.ac.idu.backend.model.Mprodi;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 07/03/12
 * Time: 12:04
 * To change this template use File | Settings | File Templates.
 */
public class GradeDAOImpl extends BasisDAO<Mgrade> implements GradeDAO {

    @Override
    public Mgrade getNewGrade() {
        return new Mgrade();
    }

    @Override
    public Mgrade getGradeById(int id) {
        return get(Mgrade.class, id);
    }

    @Override
    public Mgrade getGradeByCode(String code) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mgrade.class);
        criteria.add(Restrictions.eq("ckdgrade", code));

        return (Mgrade) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public Mgrade getGradeByName(String name) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mgrade.class);
        criteria.add(Restrictions.eq("cgrade", name));

        return (Mgrade) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Mgrade> getGradeByProdi(Mprodi prodi) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mgrade.class);
        criteria.add(Restrictions.eq("mprodi", prodi));
        criteria.addOrder(org.hibernate.criterion.Order.asc("ckdgrade")); // set the order

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mgrade> getGradeByJenjang(Mjenjang jenjang) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mgrade.class);
        criteria.add(Restrictions.eq("mjenjang", jenjang));
        criteria.addOrder(org.hibernate.criterion.Order.asc("ckdgrade")); // set the order

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mgrade> getAllGrade() {
        return getHibernateTemplate().loadAll(Mgrade.class);
    }

    @Override
    public int getCountAllGrade() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from mgrade"));
    }

    @Override
    public void deleteGradeById(int id) {
        Mgrade grade = getGradeById(id);
        if (grade != null) {
            delete(grade);
        }
    }
}
