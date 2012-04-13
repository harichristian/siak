package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.BidangUsahaDAO;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Branche;
import id.ac.idu.backend.model.Mbidangusaha;
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
 * Date: 4/13/12
 * Time: 8:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class BidangUsahaDAOImpl extends BasisDAO<Mbidangusaha> implements BidangUsahaDAO{

    @Override
    public Mbidangusaha getNewBidangusaha() {
        return new Mbidangusaha();
    }

    @Override
      public Mbidangusaha getBidangusahaById(int id) {
          return get(Mbidangusaha.class, id);
      }

      @Override
      public Mbidangusaha getBidangusahaByName(String name) {
          DetachedCriteria criteria = DetachedCriteria.forClass(Mbidangusaha.class);
          criteria.add(Restrictions.eq("cnmbidusaha", name));

          return (Mbidangusaha) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
      }

      @Override
      public List<Mbidangusaha> getAllBidangusaha() {
          return getHibernateTemplate().loadAll(Mbidangusaha.class);
      }

      @Override
      public int getCountAllBidangusaha() {
          return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from mbidangusaha"));
      }

      @Override
      public void deleteBidangusahaById(int id) {
          Mbidangusaha bidangusaha = getBidangusahaById(id);
          if (bidangusaha != null) {
              delete(bidangusaha);
          }
      }

      @SuppressWarnings("unchecked")
      @Override
      public ResultObject getAllBidangusahaLikeText(String text, int start, int pageSize) {
          DetachedCriteria criteria = DetachedCriteria.forClass(Mbidangusaha.class);

          if (!StringUtils.isEmpty(text)) {
              criteria.add(Restrictions.ilike("cnmbidusaha", text, MatchMode.ANYWHERE));
          }

          criteria.addOrder(Order.asc("cnmbidusaha"));

          int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

          List<Branche> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

          return new ResultObject(list, totalCount);
      }
  }
