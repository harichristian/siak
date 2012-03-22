package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.StatusMahasiswaDAO;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mstatusmhs;
import id.ac.idu.util.ConstantUtil;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/8/12
 * Time: 11:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class StatusMahasiswaDAOImpl extends BasisDAO<Mstatusmhs> implements StatusMahasiswaDAO {

    @Override
    public Mstatusmhs getNewStatusMahasiswa() {
        return new Mstatusmhs();
    }

    @Override
    public Mstatusmhs getStatusMahasiswaById(int id) {
        return get(Mstatusmhs.class, id);
    }

    @Override
    public Mstatusmhs getStatusMahasiswaByCode(String code) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mstatusmhs.class);
        criteria.add(Restrictions.eq(ConstantUtil.MAHASISWA_CODE, code));
        return (Mstatusmhs) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Mstatusmhs> getAllStatusMahasiswas() {
        return getHibernateTemplate().loadAll(Mstatusmhs.class);
    }

    @Override
    public int getCountAllStatusMahasiswas() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Mstatusmhs"));
    }

    @Override
    public List<Mstatusmhs> getStatusMahasiswaLikeName(String name) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mstatusmhs.class);
        criteria.add(Restrictions.ilike(ConstantUtil.MAHASISWA_NAME, name, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mstatusmhs> getStatusMahasiswasLikeKeterangan(String string) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mstatusmhs.class);
        criteria.add(Restrictions.ilike(ConstantUtil.KETERANGAN, string, MatchMode.ANYWHERE));
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public void deleteStatusMahasiswaById(int id) {
        Mstatusmhs obj = getStatusMahasiswaById(id);
        if (obj != null) {
            delete(obj);
        }
    }
}
