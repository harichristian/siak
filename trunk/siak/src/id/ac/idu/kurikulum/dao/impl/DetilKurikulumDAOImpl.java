package id.ac.idu.kurikulum.dao.impl;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mdetilkurikulum;
import id.ac.idu.kurikulum.dao.DetilKurikulumDAO;
import id.ac.idu.util.ConstantUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 3/31/12
 * Time: 9:50 AM
 */
public class DetilKurikulumDAOImpl extends BasisDAO<Mdetilkurikulum> implements DetilKurikulumDAO {
    @Override
    public Mdetilkurikulum getNew() {
        return new Mdetilkurikulum();
    }

    @Override
    public Mdetilkurikulum getById(int id) {
        return get(Mdetilkurikulum.class, id);
    }

    @Override
    public Mdetilkurikulum getByKurikulumId(int kurikulumId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mdetilkurikulum.class);
        criteria.add(Restrictions.eq(ConstantUtil.KURIKULUM_ID, kurikulumId));
        return (Mdetilkurikulum) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Mdetilkurikulum> getAll() {
        return getHibernateTemplate().loadAll(Mdetilkurikulum.class);
    }

    @Override
    public int getCountAll() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from MDetilkurikulum"));
    }

    @Override
    public List<Mdetilkurikulum> getLikeProdiName(String name) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Mdetilkurikulum> getLikeCohort(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mdetilkurikulum.class);
        criteria.add(Restrictions.ilike(ConstantUtil.COHORT, string, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public void deleteById(int id) {
        Mdetilkurikulum obj = getById(id);
        if (obj != null) {
            delete(obj);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mdetilkurikulum.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike(ConstantUtil.KURIKULUM_ID, text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc(ConstantUtil.KURIKULUM_ID));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Mdetilkurikulum> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllLikeMatakuliah(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mdetilkurikulum.class);
        criteria.createAlias(ConstantUtil.MATAKULIAH,ConstantUtil.MATAKULIAH);

        if (!StringUtils.isEmpty(text)) {
            //criteria.createAlias(ConstantUtil.MATAKULIAH_NAME,ConstantUtil.MATAKULIAH_NAME);
            criteria.add(Restrictions.ilike(ConstantUtil.MATAKULIAH_DOT_NAMA, text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc(ConstantUtil.MATAKULIAH_DOT_NAMA));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Mdetilkurikulum> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }
}
