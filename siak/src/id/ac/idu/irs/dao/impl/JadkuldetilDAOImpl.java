package id.ac.idu.irs.dao.impl;

import id.ac.idu.backend.model.*;
import id.ac.idu.irs.dao.JadkuldetilDAO;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.util.ConstantUtil;
import org.hibernate.criterion.*;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 10/03/12
 * Time: 17:07
 * To change this template use File | Settings | File Templates.
 */
public class JadkuldetilDAOImpl extends BasisDAO<Tjadkuldetil> implements JadkuldetilDAO {

    @Override
    public Tjadkuldetil getNewTjadkuldetil() {
        return new Tjadkuldetil();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Tjadkuldetil> getTjadkuldetilsByTjadkulmaster(Tjadkulmaster tjadkulmaster) {
        // /** initialize() lädt die entsprechenden Daten nach. */
        // return new ArrayList<Tjadkuldetil>(tjadkulmaster.getTjadkuldetils());

        List<Tjadkuldetil> result;

        DetachedCriteria criteria = DetachedCriteria.forClass(Tjadkuldetil.class);
        criteria.add(Restrictions.eq("tjadkulmaster", tjadkulmaster));

        result = getHibernateTemplate().findByCriteria(criteria);

        return result;

    }

    @Override
    public int getCountTjadkuldetilsByTjadkulmaster(Tjadkulmaster tjadkulmaster) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tjadkuldetil.class);
        criteria.add(Restrictions.eq("tjadkulmaster", tjadkulmaster));
        criteria.setProjection(Projections.rowCount());
        return DataAccessUtils.intResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public Tjadkuldetil getTjadkuldetilById(long id) {
        return get(Tjadkuldetil.class, id);
    }

    @Override
    public void deleteTjadkuldetilsByTjadkulmaster(Tjadkulmaster tjadkulmaster) {
        List<Tjadkuldetil> tjadkuldetil = getTjadkuldetilsByTjadkulmaster(tjadkulmaster);
        if (tjadkuldetil != null) {
            deleteAll(tjadkuldetil);
        }
    }

    @Override
    public int getCountAllTjadkuldetils() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Tjadkuldetil"));
    }

    @Override
    public List<Tjadkuldetil> getForPaket(Msekolah sekolah, Mprodi prodi, String term, Mtbmtkl matakuliah) {
        //List<Tjadkuldetil> result;
        DetachedCriteria criteria = DetachedCriteria.forClass(Tjadkuldetil.class);
        criteria.add(Restrictions.eq(ConstantUtil.SEKOLAH, sekolah));
        criteria.add(Restrictions.eq(ConstantUtil.PRODI, prodi));
        criteria.add(Restrictions.eq(ConstantUtil.TERM, term));
        criteria.add(Restrictions.eq(ConstantUtil.MATAKULIAH, matakuliah));
        return getHibernateTemplate().findByCriteria(criteria);
    }
}
