package id.ac.idu.administrasi.service;

import id.ac.idu.backend.model.Mpbahasamhs;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 29 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public interface MpbahasamhsService {
    public Mpbahasamhs getNew();

    public Mpbahasamhs getById(int _id);

    public List<Mpbahasamhs> getByMahasiswaId(int _id);

    public List<Mpbahasamhs> getAll();

    public int getCount();

	public void saveOrUpdate(Mpbahasamhs entity);

	public void delete(Mpbahasamhs entity);

	public void save(Mpbahasamhs entity);
}
