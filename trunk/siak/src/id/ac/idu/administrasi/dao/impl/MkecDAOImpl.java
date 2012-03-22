package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.MkecDAO;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mkec;
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
 * Time: 12:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class MkecDAOImpl extends BasisDAO<Mkec> implements MkecDAO {

    @Override
    public Mkec getNewMkec() {
        return new Mkec();
    }

    @Override
    public Mkec getMkecById(Long fil_Id) {
        return get(Mkec.class, fil_Id);
    }

    @SuppressWarnings("unchecked")
    public Mkec getMkecByFilNr(String fil_nr) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mkec.class);
        criteria.add(Restrictions.eq("filNr", fil_nr));

        return (Mkec) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Mkec> getAllMkecs() {
        return getHibernateTemplate().loadAll(Mkec.class);
    }

    @Override
    public void deleteMkecById(long id) {
        Mkec mkec = getMkecById(id);
        if (mkec != null) {
            delete(mkec);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mkec> getMkecsLikeCity(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Mkec.class);
        criteria.add(Restrictions.ilike("filOrt", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mkec> getMkecsLikeName1(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Mkec.class);
        criteria.add(Restrictions.ilike("filName1", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mkec> getMkecsLikeNo(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Mkec.class);
        criteria.add(Restrictions.ilike("filNr", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public int getCountAllMkecs() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Mkec"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllAlumniLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mkec.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike("cnamaKec", text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc("cnamaKec"));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Mkec> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }
}

