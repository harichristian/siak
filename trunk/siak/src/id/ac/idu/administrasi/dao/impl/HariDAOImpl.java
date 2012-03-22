package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.HariDAO;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mhari;
import id.ac.idu.util.ConstantUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/8/12
 * Time: 7:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class HariDAOImpl extends BasisDAO<Mhari> implements HariDAO {
    @Override
    public Mhari getNewHari() {
        return new Mhari();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Mhari getHariById(int id) {
        return get(Mhari.class, id);
    }

    @Override
    public Mhari getHariByCode(String code) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mhari.class);
        criteria.add(Restrictions.eq(ConstantUtil.HARI_CODE, code));
        return (Mhari) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Mhari> getAllHaris() {
        return getHibernateTemplate().loadAll(Mhari.class);
    }

    @Override
    public int getCountAllHaris() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Mhari"));
    }

    @Override
    public List<Mhari> getHarisLikeName(String name) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mhari.class);
        criteria.add(Restrictions.ilike(ConstantUtil.HARI_NAME, name, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public void deleteHariById(int id) {
        Mhari obj = getHariById(id);
        if (obj != null) {
            delete(obj);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllHariLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mhari.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike("cnmhari", text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc("cnmhari"));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Mhari> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }
}
