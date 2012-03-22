package id.ac.idu.irs.service;

import id.ac.idu.backend.model.Tcutimhs;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 10 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public interface CutimhsService {
    public Tcutimhs getNew();

    public Tcutimhs getByNo(String _no);

    public int getCount();

	public List<Tcutimhs> getAll();

    public List<Tcutimhs> getByTanggal(Date _tanggal);

    public List<Tcutimhs> getByTahun(String _tahun);

	public void saveOrUpdate(Tcutimhs entity);

	public void delete(Tcutimhs entity);

	public void save(Tcutimhs entity);
}
