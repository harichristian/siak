package id.ac.idu.administrasi.service;

import id.ac.idu.backend.model.Mmhspascakhs;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 24 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public interface MmhspascakhsService {
    public Mmhspascakhs getNew();

    public Mmhspascakhs getById(int _id);

    public List<Mmhspascakhs> getAll();

    public int getCount();

	public void saveOrUpdate(Mmhspascakhs entity);

	public void delete(Mmhspascakhs entity);

	public void save(Mmhspascakhs entity);
}
