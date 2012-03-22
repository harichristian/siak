package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.PangkatDAO;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mpangkatgolongan;
import id.ac.idu.util.ConstantUtil;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/8/12
 * Time: 10:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class PangkatDAOImpl extends BasisDAO<Mpangkatgolongan> implements PangkatDAO {
    @Override
    public Mpangkatgolongan getNewPangkat() {
        return new Mpangkatgolongan();
    }

    @Override
    public Mpangkatgolongan getPangkatById(int id) {
        return get(Mpangkatgolongan.class, id);
    }

    @Override
    public Mpangkatgolongan getPangkatByCode(String code) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mpangkatgolongan.class);
        criteria.add(Restrictions.eq(ConstantUtil.PANGKAT_CODE, code));
        return (Mpangkatgolongan) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Mpangkatgolongan> getAllPangkats() {
        return getHibernateTemplate().loadAll(Mpangkatgolongan.class);
    }

    @Override
    public int getCountAllPangkats() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Mpangkatgolongan"));
    }

    @Override
    public List<Mpangkatgolongan> getPangkatsLikeName(String name) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mpangkatgolongan.class);
        criteria.add(Restrictions.ilike(ConstantUtil.PANGKAT_NAME, name, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public void deletePangkatById(int id) {
        Mpangkatgolongan obj = getPangkatById(id);
        if (obj != null) {
            delete(obj);
        }
    }
}
