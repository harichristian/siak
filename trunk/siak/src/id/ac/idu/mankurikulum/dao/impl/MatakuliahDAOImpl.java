package id.ac.idu.mankurikulum.dao.impl;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mpegawai;
import id.ac.idu.backend.model.Msesikuliah;
import id.ac.idu.backend.model.Mtbmtkl;
import id.ac.idu.mankurikulum.dao.MatakuliahDAO;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * User: hermanto
 * Date: 08 Mar 12
 * Time: 9:03:21
 */

public class MatakuliahDAOImpl extends BasisDAO<Mtbmtkl> implements MatakuliahDAO {
    @Override
    public Mtbmtkl getNewMatakuliah() {
        return new Mtbmtkl();
    }

    @Override
    public Mtbmtkl getMatakuliahById(int id) {
        return get(Mtbmtkl.class, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Mtbmtkl getMatakuliahByCode(String code) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Msesikuliah.class);
        criteria.add(Restrictions.eq("ckdmtk", code));
        return (Mtbmtkl) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mtbmtkl> getAllMatakuliah() {
        return getHibernateTemplate().loadAll(Mtbmtkl.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mtbmtkl> getMatakuliahByNama(String nama) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mtbmtkl.class);
        criteria.add(Restrictions.ilike("cnamamk", nama, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mtbmtkl> getMatakuliahByinggris(String inggris) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mtbmtkl.class);
        criteria.add(Restrictions.ilike("cingmk", inggris, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mtbmtkl> getMatakuliahBySingkatan(String singkatan) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mtbmtkl.class);
        criteria.add(Restrictions.ilike("csingmk", singkatan, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mtbmtkl> getMatakuliahBySks(Long sks) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mtbmtkl.class);
        criteria.add(Restrictions.eq("nsks", sks));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public void deleteMatakuliahById(int id) {
        Mtbmtkl mtbmtkl = getMatakuliahById(id);
        if (mtbmtkl != null) delete(mtbmtkl);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getCountAllMatakuliah() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Mtbmtkl"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllMatakuliahLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mtbmtkl.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike("cnamamk", text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc("cnamamk"));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Mtbmtkl> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }
}
