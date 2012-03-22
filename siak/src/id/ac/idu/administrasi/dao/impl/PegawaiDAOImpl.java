package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.PegawaiDAO;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Branche;
import id.ac.idu.backend.model.Mjenjang;
import id.ac.idu.backend.model.Mpegawai;
import id.ac.idu.backend.model.Mprodi;
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
 * Time: 17:28
 * To change this template use File | Settings | File Templates.
 */
public class PegawaiDAOImpl extends BasisDAO<Mpegawai> implements PegawaiDAO {

    @Override
    public Mpegawai getNewPegawai() {
        return new Mpegawai();
    }

    @Override
    public Mpegawai getPegawaiById(int id) {
        return get(Mpegawai.class, id);
    }

    @Override
    public Mpegawai getPegawaiByNip(String nip) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mpegawai.class);
        criteria.add(Restrictions.eq("cnip", nip));

        return (Mpegawai) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public Mpegawai getPegawaiByName(String name) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mpegawai.class);
        criteria.add(Restrictions.eq("cnama", name));

        return (Mpegawai) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Mpegawai> getPegawaiByProdi(Mprodi prodi) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mpegawai.class);
        criteria.add(Restrictions.eq("mprodi", prodi));
        criteria.addOrder(org.hibernate.criterion.Order.asc("ckdpegawai")); // set the order

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mpegawai> getPegawaiByJenjang(Mjenjang jenjang) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mpegawai.class);
        criteria.add(Restrictions.eq("mjenjang", jenjang));
        criteria.addOrder(org.hibernate.criterion.Order.asc("ckdpegawai")); // set the order

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Mpegawai> getAllPegawai() {
        return getHibernateTemplate().loadAll(Mpegawai.class);
    }

    @Override
    public int getCountAllPegawai() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from mpegawai"));
    }

    @Override
    public void deletePegawaiById(int id) {
        Mpegawai pegawai = getPegawaiById(id);
        if (pegawai != null) {
            delete(pegawai);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllPegawaiLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mpegawai.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike("cnama", text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc("cnama"));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Mpegawai> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }
}
