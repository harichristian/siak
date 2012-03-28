package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.model.Mmhspascakhs;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 23 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public interface MmhspascakhsDAO {

    public Mmhspascakhs getNew();

    public Mmhspascakhs getById(int _id);

    public List<Mmhspascakhs> getAll();

    public int getCount();

    public void deleteById(int _id);

	public void saveOrUpdate(Mmhspascakhs entity);

	public void delete(Mmhspascakhs entity);

	public void save(Mmhspascakhs entity);
}
