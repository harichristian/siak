package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.model.Mppumhskhusus;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 25 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public interface MppumhskhususDAO {
    public Mppumhskhusus getNew();

    public Mppumhskhusus getById(int _id);

    public List<Mppumhskhusus> getByMahasiswaId(int _id);
    
    public List<Mppumhskhusus> getAll();

    public int getCount();

    public void deleteById(int _id);

	public void saveOrUpdate(Mppumhskhusus entity);

	public void delete(Mppumhskhusus entity);

	public void save(Mppumhskhusus entity);
}
