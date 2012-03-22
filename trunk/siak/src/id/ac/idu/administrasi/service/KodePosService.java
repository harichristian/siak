package id.ac.idu.administrasi.service;

import id.ac.idu.backend.model.MkodePos;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 13 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public interface KodePosService {
    public MkodePos getNewKodePos();

    public MkodePos getKodePosById(int id);

    public MkodePos getKodePosByStringId(String id);

    public MkodePos getKodePos(String code);

    public int getCount();

    public void saveOrUpdate(MkodePos entity);

    public void delete(MkodePos entity);

    public void save(MkodePos entity);
}
