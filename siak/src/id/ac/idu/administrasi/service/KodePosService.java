package id.ac.idu.administrasi.service;

import id.ac.idu.backend.bean.ResultObject;
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

    public MkodePos getKodePosById(String id);

    public MkodePos getKodePosByStringId(String id);

    public MkodePos getKodePos(String code);

    public int getCount();

    public void saveOrUpdate(MkodePos entity);

    public void delete(MkodePos entity);

    public void save(MkodePos entity);

     /**
     * EN: Get a paged list of all Branches.<br>
     * DE: Gibt eine paged Liste aller Branchen zurueck.<br>
     *
     * @param text     Text for search / SuchText
     * @param start    StartRecord / Start Datensatz
     * @param pageSize Count of Records / Anzahl Datensaetze
     * @return List of YoutubeLinks / Liste von YoutubeLinks
     */
    public ResultObject getAllMkodePosLikeText(String text, int start, int pageSize);
}
