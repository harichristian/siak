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
package id.ac.idu.administrasi.dao.impl;


import id.ac.idu.administrasi.dao.FasilitasDAO;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Branche;
import id.ac.idu.backend.model.Mfasilitas;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * EN: DAO methods implementation for the <b>Mfasilitas</b> model class.<br>
 * DE: DAO Methoden Implementierung fuer die <b>Mfasilitas</b> Model Klasse.<br>
 *
 * @author bbruhns
 * @author Stephan Gerth
 */
@Repository
public class FasilitasDAOImpl extends BasisDAO<Mfasilitas> implements FasilitasDAO {

    @Override
    public Mfasilitas getNewMfasilitas() {
        return new Mfasilitas();
    }

    @Override
    public Mfasilitas getMfasilitasById(Long fil_Id) {
        return get(Mfasilitas.class, fil_Id);
    }

    @SuppressWarnings("unchecked")
    public Mfasilitas getMfasilitasByFilNr(String fil_nr) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mfasilitas.class);
        criteria.add(Restrictions.eq("filNr", fil_nr));

        return (Mfasilitas) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    public List<Mfasilitas> getAllMfasilitass() {
        return getHibernateTemplate().loadAll(Mfasilitas.class);
    }

    @Override
    public void deleteMfasilitasById(long id) {
        Mfasilitas fasilitas = getMfasilitasById(id);
        if (fasilitas != null) {
            delete(fasilitas);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mfasilitas> getMfasilitassLikeCity(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Mfasilitas.class);
        criteria.add(Restrictions.ilike("filOrt", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mfasilitas> getMfasilitassLikeName1(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Mfasilitas.class);
        criteria.add(Restrictions.ilike("filName1", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Mfasilitas> getMfasilitassLikeNo(String string) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Mfasilitas.class);
        criteria.add(Restrictions.ilike("filNr", string, MatchMode.ANYWHERE));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public int getCountAllMfasilitass() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Mfasilitas"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResultObject getAllMfasilitasLikeText(String text, int start, int pageSize) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Mfasilitas.class);

        if (!StringUtils.isEmpty(text)) {
            criteria.add(Restrictions.ilike("cnamaFasilitas", text, MatchMode.ANYWHERE));
        }

        criteria.addOrder(Order.asc("cnamaFasilitas"));

        int totalCount = getHibernateTemplate().findByCriteria(criteria).size();

        List<Branche> list = getHibernateTemplate().findByCriteria(criteria, start, pageSize);

        return new ResultObject(list, totalCount);
    }

}
