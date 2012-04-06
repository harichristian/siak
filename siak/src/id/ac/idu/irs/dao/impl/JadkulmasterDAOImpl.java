package id.ac.idu.irs.dao.impl;

import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mpegawai;
import id.ac.idu.backend.model.Mprodi;
import id.ac.idu.backend.model.Msekolah;
import id.ac.idu.backend.model.Tjadkulmaster;
import id.ac.idu.irs.dao.JadkulmasterDAO;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 10/03/12
 * Time: 17:06
 * To change this template use File | Settings | File Templates.
 */
public class JadkulmasterDAOImpl extends BasisDAO<Tjadkulmaster> implements JadkulmasterDAO {

    @Override
    public Tjadkulmaster getNewTjadkulmaster() {
        return new Tjadkulmaster();
    }

    @Override
    public List<Tjadkulmaster> getTjadkulmastersForReport(Tjadkulmaster param) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tjadkulmaster.class);
        if (param != null) {
            System.out.println("if param != null");
            if (param.getMprodi() != null) {
                System.out.println("prodi = " + param.getMprodi().getCnmprogst());
                criteria.add(Restrictions.eq("mprodi", param.getMprodi()));
            }
            if (StringUtils.isNotBlank(param.getCterm())) {
                System.out.println("term = " + param.getCterm());
                criteria.add(Restrictions.eq("cterm", param.getCterm()));
            }
            if (StringUtils.isNotBlank(param.getCsmt())) {
                System.out.println("smt = " + param.getCsmt());
                criteria.add(Restrictions.eq("csmt", param.getCsmt()));
            }
            if (StringUtils.isNotBlank(param.getCthajar())) {
                System.out.println("thajar = " + param.getCthajar());
                criteria.add(Restrictions.eq("cthajar", param.getCthajar()));
            }
        }
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Tjadkulmaster> getTjadkulmastersBySekolahAndProdiAndPegawai1(Msekolah sekolah, Mprodi prodi, Mpegawai pegawai1) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tjadkulmaster.class);
        if (sekolah != null) {
            criteria.add(Restrictions.eq("msekolah", sekolah));
        }
        if (prodi != null) {
            criteria.add(Restrictions.eq("mprodi", prodi));
        }
        if (pegawai1 != null) {
            criteria.add(Restrictions.eq("mpegawai1", pegawai1));
        }
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Tjadkulmaster> getTjadkulmastersBySekolahAndProdi(Msekolah sekolah, Mprodi prodi) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tjadkulmaster.class);
        if (sekolah != null) {
            criteria.add(Restrictions.eq("msekolah", sekolah));
        }
        if (prodi != null) {
            criteria.add(Restrictions.eq("mprodi", prodi));
        }
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Tjadkulmaster> getTjadkulmastersBySekolah(Msekolah sekolah) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tjadkulmaster.class);
        if (sekolah != null) {
            criteria.add(Restrictions.eq("msekolah", sekolah));
        }
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Tjadkulmaster> getTjadkulmastersByProdi(Mprodi prodi) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tjadkulmaster.class);
        if (prodi != null) {
            criteria.add(Restrictions.eq("mprodi", prodi));
        }
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Tjadkulmaster> getTjadkulmastersByPegawai1(Mpegawai pegawai1) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tjadkulmaster.class);
        if (pegawai1 != null) {
            criteria.add(Restrictions.eq("mpegawai1", pegawai1));
        }
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<Tjadkulmaster> getTjadkulmastersByPegawai2(Mpegawai pegawai2) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tjadkulmaster.class);
        if (pegawai2 != null) {
            criteria.add(Restrictions.eq("mpegawai2", pegawai2));
        }
        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public Tjadkulmaster getTjadkulmasterById(long id) {
        return get(Tjadkulmaster.class, id);
    }


    @Override
    public List<Tjadkulmaster> getAllTjadkulmasters() {
        return getHibernateTemplate().loadAll(Tjadkulmaster.class);
    }

    @Override
    public int getCountAllTjadkulmasters() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from tjadkulmaster"));
    }

    /*
      * (non-Javadoc)
      *
      * @see
      * id.ac.idu.backend.dao.TjadkulmasterDAO#initialize(id.ac.idu.backend.model
      * .Tjadkulmaster)
      */
    @Override
    public void initialize(Tjadkulmaster tjadkulmaster) {
        super.initialize(tjadkulmaster);
    }
}
