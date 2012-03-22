package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.JenjangDAO;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Branche;
import id.ac.idu.backend.model.Mjenjang;
import id.ac.idu.backend.model.Mjenjang;
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
 * Time: 6:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class JenjangDAOImpl extends BasisDAO<Mjenjang> implements JenjangDAO {
    @Override
    public Mjenjang getNewJenjang() {
        return new Mjenjang();
    }

    @Override
    public Mjenjang getJenjangById(int id) {
        return get(Mjenjang.class, id);
    }

    @Override
    public Mjenjang getJenjangByCode(String code) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mjenjang.class);
        criteria.add(Restrictions.eq(ConstantUtil.JENJANG_CODE, code));
        return (Mjenjang) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Mjenjang> getAllJenjangs() {
        return getHibernateTemplate().loadAll(Mjenjang.class);
    }

    @Override
    public int getCountAllJenjangs() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Mjenjang"));
    }

    @Override
    public List<Mjenjang> getJenjangsLikeName(String name) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mjenjang.class);
        criteria.add(Restrictions.ilike(ConstantUtil.JENJANG_NAME, name, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mjenjang> getJenjangsLikeSingkatan(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mjenjang.class);
        criteria.add(Restrictions.ilike(ConstantUtil.JENJANG_SINGKATAN, string, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public void deleteJenjangById(int id) {
        Mjenjang obj = getJenjangById(id);
        if (obj != null) {
            delete(obj);
        }
    }

    @Override
    public void initialize(Mjenjang obj) {
        super.initialize(obj);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllJenjangLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mjenjang.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike("cnmjen", text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc("cnmjen"));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Mjenjang> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }
}
