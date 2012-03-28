package id.ac.idu.administrasi.service;

import id.ac.idu.backend.model.Mhistpangkatmhs;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 25 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public interface MhistpangkatmhsService {
    public Mhistpangkatmhs getNew();

    public Mhistpangkatmhs getById(int _id);

    public List<Mhistpangkatmhs> getByMahasiswaId(int _id);

    public List<Mhistpangkatmhs> getAll();

    public int getCount();

	public void saveOrUpdate(Mhistpangkatmhs entity);

	public void delete(Mhistpangkatmhs entity);

	public void save(Mhistpangkatmhs entity);
}
