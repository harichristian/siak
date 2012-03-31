package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.MpeminatanDAO;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mpeminatan;
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
 * User: siak
 * Date: 3/21/12
 * Time: 1:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class MpeminatanDAOImpl  extends BasisDAO<Mpeminatan> implements MpeminatanDAO {

    @Override
    public Mpeminatan getNewMpeminatan() {
        return new Mpeminatan();
    }

    @Override
    public Mpeminatan getMpeminatanById(Long fil_Id) {
        return get(Mpeminatan.class, fil_Id);
    }

    @SuppressWarnings("unchecked")
    public Mpeminatan getMpeminatanByFilNr(String fil_nr) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mpeminatan.class);
        criteria.add(Restrictions.eq("filNr", fil_nr));

        return (Mpeminatan) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Mpeminatan> getAllMpeminatans() {
        return getHibernateTemplate().loadAll(Mpeminatan.class);
    }

    @Override
    public void deleteMpeminatanById(long id) {
        Mpeminatan mpeminatan = getMpeminatanById(id);
        if (mpeminatan != null) {
            delete(mpeminatan);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mpeminatan> getMpeminatansLikeCity(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Mpeminatan.class);
        criteria.add(Restrictions.ilike("filOrt", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mpeminatan> getMpeminatansLikeName1(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Mpeminatan.class);
        criteria.add(Restrictions.ilike("filName1", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mpeminatan> getMpeminatansLikeNo(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Mpeminatan.class);
        criteria.add(Restrictions.ilike("filNr", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public int getCountAllMpeminatans() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Mpeminatan"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllAlumniLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mpeminatan.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike("cnmminat", text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc("cnmminat"));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Mpeminatan> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllPeminatanLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mpeminatan.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike(ConstantUtil.PEMINATAN_NAME, text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc(ConstantUtil.PEMINATAN_NAME));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Mpeminatan> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }
}
