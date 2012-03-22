package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.MruangDAO;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Branche;
import id.ac.idu.backend.model.Mruang;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/6/12
 * Time: 4:21 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class MruangDAOImpl extends BasisDAO<Mruang> implements MruangDAO {

    @Override
    public Mruang getNewRuang() {
        return new Mruang();
    }

    @Override
    public Mruang getMruangById(int fil_Id) {
        return get(Mruang.class, fil_Id);
    }

    @SuppressWarnings("unchecked")
    public Mruang getMruangByFilNr(String fil_nr) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mruang.class);
        criteria.add(Restrictions.eq("filNr", fil_nr));

        return (Mruang) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Mruang> getAllMruangs() {
        return getHibernateTemplate().loadAll(Mruang.class);
    }

    @Override
    public void deleteMruangById(int id) {
        Mruang mruang = getMruangById(id);
        if (mruang != null) {
            delete(mruang);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mruang> getMruangsLikeCity(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Mruang.class);
        criteria.add(Restrictions.ilike("filOrt", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mruang> getMruangsLikeName1(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Mruang.class);
        criteria.add(Restrictions.ilike("filName1", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mruang> getMruangsLikeNo(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Mruang.class);
        criteria.add(Restrictions.ilike("filNr", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public int getCountAllMruangs() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Mruang"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllMruangLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mruang.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike("cnmRuang", text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc("cnmRuang"));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Branche> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }
}
