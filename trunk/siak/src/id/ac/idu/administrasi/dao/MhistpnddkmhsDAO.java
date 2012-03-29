package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.model.Mhistpnddkmhs;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 28 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public interface MhistpnddkmhsDAO {
    public Mhistpnddkmhs getNew();

    public Mhistpnddkmhs getById(int _id);

    public List<Mhistpnddkmhs> getByMahasiswaId(int _id);

    public List<Mhistpnddkmhs> getAll();

    public int getCount();

    public void deleteById(int _id);

	public void saveOrUpdate(Mhistpnddkmhs entity);

	public void delete(Mhistpnddkmhs entity);

	public void save(Mhistpnddkmhs entity);
}
