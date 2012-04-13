package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.HistKerjaDAO;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Branche;
import id.ac.idu.backend.model.Thistkerja;
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
 * Date: 4/13/12
 * Time: 10:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class HistKerjaDAOImpl  extends BasisDAO<Thistkerja> implements HistKerjaDAO {
    @Override
    public Thistkerja getNewHistKerja() {
        return new Thistkerja();
    }

    @Override
    public Thistkerja getHistKerjaById(int id) {
        return get(Thistkerja.class, id);
    }

    @Override
    public Thistkerja getHistKerjaByName(String name) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Thistkerja.class);
        criteria.add(Restrictions.eq("cnminstansi", name));

        return (Thistkerja) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Thistkerja> getAllHistKerja() {
        return getHibernateTemplate().loadAll(Thistkerja.class);
    }

    @Override
    public int getCountAllHistKerja() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from thistkerja"));
    }

    @Override
    public void deleteHistKerjaById(int id) {
        Thistkerja histKerja = getHistKerjaById(id);
        if (histKerja != null) {
            delete(histKerja);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllHistKerjaLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Thistkerja.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike("cnminstansi", text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc("cnminstansi"));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Branche> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }
}
