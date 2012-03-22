package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.TfeedbackinstansiDAO;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Tfeedbackinstansi;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/13/12
 * Time: 12:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class TfeedbackinstansiDAOImpl extends BasisDAO<Tfeedbackinstansi> implements TfeedbackinstansiDAO {

    @Override
    public Tfeedbackinstansi getNewTfeedbackinstansi() {
        return new Tfeedbackinstansi();
    }

    @Override
    public Tfeedbackinstansi getTfeedbackinstansiById(Long fil_Id) {
        return get(Tfeedbackinstansi.class, fil_Id);
    }

    @SuppressWarnings("unchecked")
    public Tfeedbackinstansi getTfeedbackinstansiByFilNr(String fil_nr) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Tfeedbackinstansi.class);
        criteria.add(Restrictions.eq("filNr", fil_nr));

        return (Tfeedbackinstansi) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Tfeedbackinstansi> getAllTfeedbackinstansis() {
        return getHibernateTemplate().loadAll(Tfeedbackinstansi.class);
    }

    @Override
    public void deleteTfeedbackinstansiById(long id) {
        Tfeedbackinstansi tfeedbackinstansi = getTfeedbackinstansiById(id);
        if (tfeedbackinstansi != null) {
            delete(tfeedbackinstansi);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Tfeedbackinstansi> getTfeedbackinstansisLikeCity(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Tfeedbackinstansi.class);
        criteria.add(Restrictions.ilike("filOrt", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Tfeedbackinstansi> getTfeedbackinstansisLikeName1(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Tfeedbackinstansi.class);
        criteria.add(Restrictions.ilike("filName1", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Tfeedbackinstansi> getTfeedbackinstansisLikeNo(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Tfeedbackinstansi.class);
        criteria.add(Restrictions.ilike("filNr", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public int getCountAllTfeedbackinstansis() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Tfeedbackinstansi"));
    }

}
