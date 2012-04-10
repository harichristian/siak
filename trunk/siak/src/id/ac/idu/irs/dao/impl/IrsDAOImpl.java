package id.ac.idu.irs.dao.impl;

import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mmahasiswa;
import id.ac.idu.backend.model.Tirspasca;
import id.ac.idu.irs.dao.IrsDAO;
import id.ac.idu.util.ConstantUtil;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/11/12
 * Time: 5:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class IrsDAOImpl extends BasisDAO<Tirspasca> implements IrsDAO{
    @Override
    public Tirspasca getNewIrs() {
        return new Tirspasca();
    }

    @Override
    public Tirspasca getIrsById(int id) {
        return get(Tirspasca.class, id);
    }

    @Override
    public List<Tirspasca> getAll() {
        return getHibernateTemplate().loadAll(Tirspasca.class);
    }

    @Override
    public int getCountAll() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Tirspasca"));
    }

    @Override
    public List<Tirspasca> getIrsLikeTerm(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tirspasca.class);
        criteria.add(Restrictions.ilike(ConstantUtil.TERM, string, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Tirspasca> getIrsLikeKelompok(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tirspasca.class);
        criteria.add(Restrictions.ilike(ConstantUtil.KELOMPOK, string, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Tirspasca> getIrsLikeMahasiswaName(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tirspasca.class);
        criteria.add(Restrictions.ilike(ConstantUtil.MAHASISWA_DOT_NAME, string, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public void deleteIrsById(int id) {
        Tirspasca obj = getIrsById(id);
        if (obj != null) {
            delete(obj);
        }
    }

    @Override
    public void saveOrUpdatePaket(Tirspasca entity, Mmahasiswa mahasiswa) {

    }
}
