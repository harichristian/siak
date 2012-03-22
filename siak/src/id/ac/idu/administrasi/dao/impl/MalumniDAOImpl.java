package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.MalumniDAO;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Malumni;
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
 * Date: 3/12/12
 * Time: 3:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class MalumniDAOImpl extends BasisDAO<Malumni> implements MalumniDAO {

    @Override
    public Malumni getNewMalumni() {
        return new Malumni();
    }

    @Override
    public Malumni getMalumniById(Long fil_Id) {
        return get(Malumni.class, fil_Id);
    }

    @SuppressWarnings("unchecked")
    public Malumni getMalumniByFilNr(String fil_nr) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Malumni.class);
        criteria.add(Restrictions.eq("filNr", fil_nr));

        return (Malumni) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Malumni> getAllMalumnis() {
        return getHibernateTemplate().loadAll(Malumni.class);
    }

    @Override
    public void deleteMalumniById(long id) {
        Malumni malumni = getMalumniById(id);
        if (malumni != null) {
            delete(malumni);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Malumni> getMalumnisLikeCity(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Malumni.class);
        criteria.add(Restrictions.ilike("filOrt", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Malumni> getMalumnisLikeName1(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Malumni.class);
        criteria.add(Restrictions.ilike("filName1", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Malumni> getMalumnisLikeNo(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Malumni.class);
        criteria.add(Restrictions.ilike("filNr", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public int getCountAllMalumnis() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Malumni"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllAlumniLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Malumni.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike("mmahasiswa", text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc("mmahasiswa"));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Malumni> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }
}
