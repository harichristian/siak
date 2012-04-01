package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.model.Mprestasimhs;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 02 Apr 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public interface MprestasimhsDAO {
    public Mprestasimhs getNew();

    public Mprestasimhs getById(int _id);

    public List<Mprestasimhs> getByMahasiswaId(int _id);

    public List<Mprestasimhs> getAll();

    public int getCount();

    public void deleteById(int _id);

	public void saveOrUpdate(Mprestasimhs entity);

	public void delete(Mprestasimhs entity);

	public void save(Mprestasimhs entity);
}
