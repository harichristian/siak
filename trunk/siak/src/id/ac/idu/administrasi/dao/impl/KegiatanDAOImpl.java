package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.KegiatanDAO;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mkegiatan;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 08/03/12
 * Time: 22:57
 * To change this template use File | Settings | File Templates.
 */
public class KegiatanDAOImpl extends BasisDAO<Mkegiatan> implements KegiatanDAO {
    @Override
    public Mkegiatan getNewKegiatan() {
        return new Mkegiatan();
    }

    @Override
    public Mkegiatan getKegiatanById(int id) {
        return get(Mkegiatan.class, id);
    }

    @Override
    public Mkegiatan getKegiatanByName(String name) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mkegiatan.class);
        criteria.add(Restrictions.eq("cnmkegiatan", name));

        return (Mkegiatan) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Mkegiatan> getAllKegiatan() {
        return getHibernateTemplate().loadAll(Mkegiatan.class);
    }

    @Override
    public int getCountAllKegiatan() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from mkegiatan"));
    }

    @Override
    public void deleteKegiatanById(int id) {
        Mkegiatan kegiatan = getKegiatanById(id);
        if (kegiatan != null) {
            delete(kegiatan);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllKegiatanLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mkegiatan.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike("cnmkgt", text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc("cnmkgt"));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Mkegiatan> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }
}