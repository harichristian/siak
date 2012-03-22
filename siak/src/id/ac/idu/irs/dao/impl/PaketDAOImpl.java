package id.ac.idu.irs.dao.impl;

import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Tpaketkuliah;
import id.ac.idu.irs.dao.PaketDAO;
import id.ac.idu.util.ConstantUtil;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/13/12
 * Time: 10:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class PaketDAOImpl extends BasisDAO<Tpaketkuliah> implements PaketDAO {
    @Override
    public Tpaketkuliah getNewPaket() {
        return new Tpaketkuliah();
    }

    @Override
    public Tpaketkuliah getPaketById(int id) {
        return get(Tpaketkuliah.class, id);
    }

    @Override
    public List<Tpaketkuliah> getAll() {
        return getHibernateTemplate().loadAll(Tpaketkuliah.class);
    }

    @Override
    public int getCountAll() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Tpaketkuliah"));
    }

    @Override
    public List<Tpaketkuliah> getPaketLikeTerm(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tpaketkuliah.class);
        criteria.add(Restrictions.ilike(ConstantUtil.TERM, string, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Tpaketkuliah> getPaketLikeThajar(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tpaketkuliah.class);
        criteria.add(Restrictions.ilike(ConstantUtil.THAJAR, string, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Tpaketkuliah> getPaketLikeProdi(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tpaketkuliah.class);
        criteria.add(Restrictions.ilike(ConstantUtil.PRODI, string, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public void deletePaketById(int id) {
        Tpaketkuliah obj = getPaketById(id);
        if (obj != null) {
            delete(obj);
        }
    }
}
