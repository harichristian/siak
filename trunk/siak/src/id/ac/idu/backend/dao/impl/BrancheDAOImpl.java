/**
 * Copyright 2010 the original author or authors.
 *
 * This file is part of Zksample2. http://zksample2.sourceforge.net/
 *
 * Zksample2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Zksample2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Zksample2.  If not, see <http://www.gnu.org/licenses/gpl.html>.
 */
package id.ac.idu.backend.dao.impl;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.BrancheDAO;
import id.ac.idu.backend.model.Branche;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.*;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * EN: DAO methods implementation for the <b>Branche</b> model class.<br>
 * DE: DAO Methoden Implementierung fuer die <b>Branche</b> Model Klasse.<br>
 *
 * @author bbruhns
 * @author Stephan Gerth
 */
@Repository
public class BrancheDAOImpl extends BasisDAO<Branche> implements BrancheDAO {

    @Override
    public Branche getNewBranche() {
        return new Branche();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Branche> getAllBranches() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Branche.class);
        criteria.addOrder(Order.asc("braBezeichnung"));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Branche getBrancheByID(long bra_id) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Branche.class);
        criteria.add(Restrictions.eq("id", Long.valueOf(bra_id)));

        return (Branche) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Branche getBrancheByName(String braBezeichnung) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Branche.class);
        criteria.add(Restrictions.eq("braBezeichnung", braBezeichnung));

        return (Branche) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Branche> getBranchesLikeName(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Branche.class);
        criteria.add(Restrictions.ilike("braBezeichnung", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public int getBrancheSize() {
        DetachedCriteria criteria = DetachedCriteria.forClass(Branche.class);
        criteria.setProjection(Projections.rowCount());
        return DataAccessUtils.intResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public int getCountAllBranches() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Branche"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllBranches(int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Branche.class);
        criteria.addOrder(Order.asc("braBezeichnung"));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Branche> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllBranchesLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Branche.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike("braBezeichnung", text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc("braBezeichnung"));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Branche> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }

}
