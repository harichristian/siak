package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.model.Mkgtmhs;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 31 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public interface MkgtmhsDAO {
    public Mkgtmhs getNew();

    public Mkgtmhs getById(int _id);

    public List<Mkgtmhs> getByMahasiswaId(int _id);

    public List<Mkgtmhs> getAll();

    public int getCount();

    public void deleteById(int _id);

	public void saveOrUpdate(Mkgtmhs entity);

	public void delete(Mkgtmhs entity);

	public void save(Mkgtmhs entity);
}
