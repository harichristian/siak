package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.MprovDAO;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mprov;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/19/12
 * Time: 11:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class MprovDAOImpl extends BasisDAO<Mprov> implements MprovDAO {

    @Override
    public Mprov getNewMprov() {
        return new Mprov();
    }

    @Override
    public Mprov getMprovById(Long fil_Id) {
        return get(Mprov.class, fil_Id.intValue());
    }

    @SuppressWarnings("unchecked")
    public Mprov getMprovByFilNr(String fil_nr) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mprov.class);
        criteria.add(Restrictions.eq("filNr", fil_nr));

        return (Mprov) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Mprov> getAllMprovs() {
        return getHibernateTemplate().loadAll(Mprov.class);
    }

    @Override
    public void deleteMprovById(long id) {
        Mprov mprov = getMprovById(id);
        if (mprov != null) {
            delete(mprov);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mprov> getMprovsLikeCity(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Mprov.class);
        criteria.add(Restrictions.ilike("filOrt", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mprov> getMprovsLikeName1(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Mprov.class);
        criteria.add(Restrictions.ilike("filName1", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mprov> getMprovsLikeNo(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Mprov.class);
        criteria.add(Restrictions.ilike("filNr", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public int getCountAllMprovs() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Mprov"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllAlumniLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mprov.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike("cnamaProv", text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc("cnamaProv"));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Mprov> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }
}

