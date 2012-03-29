package id.ac.idu.administrasi.service;

import id.ac.idu.backend.model.Mhistkursusmhs;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 29 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public interface MhistkursusmhsService {
    public Mhistkursusmhs getNew();

    public Mhistkursusmhs getById(int _id);

    public List<Mhistkursusmhs> getByMahasiswaId(int _id);

    public List<Mhistkursusmhs> getAll();

    public int getCount();

	public void saveOrUpdate(Mhistkursusmhs entity);

	public void delete(Mhistkursusmhs entity);

	public void save(Mhistkursusmhs entity);
}
