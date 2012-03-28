package id.ac.idu.administrasi.dao.impl;

import id.ac.idu.administrasi.dao.MmhspascakhsDAO;
import id.ac.idu.backend.dao.impl.BasisDAO;
import id.ac.idu.backend.model.Mmhspascakhs;
import org.springframework.dao.support.DataAccessUtils;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 23 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MmhspascakhsDAOImpl extends BasisDAO<Mmhspascakhs> implements MmhspascakhsDAO {
    @Override
    public Mmhspascakhs getNew() {
        return new Mmhspascakhs();
    }

    @Override
    public Mmhspascakhs getById(int _id) {
        return get(Mmhspascakhs.class, _id);
    }

    @Override
    public List<Mmhspascakhs> getAll() {
        return getHibernateTemplate().loadAll(Mmhspascakhs.class);
    }

    @Override
    public int getCount() {
        return DataAccessUtils.intResult(getHibernateTemplate().find("select count(*) from Mmhspascakhs"));
    }

    @Override
    public void deleteById(int _id) {
        Mmhspascakhs mmhspascakhs = getById(_id);
		if(mmhspascakhs != null) delete(mmhspascakhs);
    }
}
