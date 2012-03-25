package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mmahasiswa;

import java.util.List;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 10 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public interface MahasiswaDAO {
    public Mmahasiswa getNew();

    public Mmahasiswa getById(Long _id);

    public int getCount();

	public List<Mmahasiswa> getAll();

    public List<Mmahasiswa> getByNim(String _nim);

    public List<Mmahasiswa> getByNama(String _Nama);

	public void deleteById(Long _id);

	public void saveOrUpdate(Mmahasiswa entity);

	public void delete(Mmahasiswa entity);

	public void save(Mmahasiswa entity);


     /**
     * EN: Get a paged list of all Branches.<br>
     * DE: Gibt eine paged Liste aller Branchen zurueck.<br>
     *
     * @param text     Text for search / SuchText
     * @param start    StartRecord / Start Datensatz
     * @param pageSize Count of Records / Anzahl Datensaetze
     * @return List of YoutubeLinks / Liste von YoutubeLinks
     */
    public ResultObject getAllMmahasiswaLikeText(String text, int start, int pageSize);
}
