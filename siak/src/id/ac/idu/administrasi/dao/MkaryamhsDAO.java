package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.model.Mkaryamhs;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 31 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public interface MkaryamhsDAO {
    public Mkaryamhs getNew();

    public Mkaryamhs getById(int _id);

    public List<Mkaryamhs> getByMahasiswaId(int _id);

    public List<Mkaryamhs> getAll();

    public int getCount();

    public void deleteById(int _id);

	public void saveOrUpdate(Mkaryamhs entity);

	public void delete(Mkaryamhs entity);

	public void save(Mkaryamhs entity);
}
