package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.JabatanDAO;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mjabatan;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 05/03/12
 * Time: 19:39
 * To change this template use File | Settings | File Templates.
 */
public class JabatanDAOImpl extends BasisDAO<Mjabatan> implements JabatanDAO {
    @Override
    public Mjabatan getNewJabatan() {
        return new Mjabatan();
    }

    @Override
    public Mjabatan getJabatanById(int id) {
        return get(Mjabatan.class, id);
    }

    @Override
    public Mjabatan getJabatanByName(String name) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mjabatan.class);
        criteria.add(Restrictions.eq("cnmjabatan", name));

        return (Mjabatan) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Mjabatan> getAllJabatan() {
        return getHibernateTemplate().loadAll(Mjabatan.class);
    }

    @Override
    public int getCountAllJabatan() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from mjabatan"));
    }

    @Override
    public void deleteJabatanById(int id) {
        Mjabatan jabatan = getJabatanById(id);
        if (jabatan != null) {
            delete(jabatan);
        }
    }
}
