package id.ac.idu.kurikulum.service.impl;

import id.ac.idu.administrasi.dao.ProdiDAO;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mkurikulum;
import id.ac.idu.kurikulum.dao.KurikulumDAO;
import id.ac.idu.kurikulum.service.KurikulumService;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/9/12
 * Time: 3:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class KurikulumServiceImpl implements KurikulumService {
    private KurikulumDAO kurikulumDAO;
    private ProdiDAO prodiDAO;

    public KurikulumDAO getKurikulumDAO() {
        return kurikulumDAO;
    }

    public void setKurikulumDAO(KurikulumDAO kurikulumDAO) {
        this.kurikulumDAO = kurikulumDAO;
    }

    public ProdiDAO getProdiDAO() {
        return prodiDAO;
    }

    public void setProdiDAO(ProdiDAO prodiDAO) {
        this.prodiDAO = prodiDAO;
    }

    @Override
    public Mkurikulum getNewKurikulum() {
        return getKurikulumDAO().getNewKurikulum();
    }

    @Override
    public int getCountAllKurikulums() {
        return getKurikulumDAO().getCountAllKurikulums();
    }

    @Override
    public Mkurikulum getKurikulumById(int id) {
        return getKurikulumDAO().getKurikulumById(id);
    }

    @Override
    public List<Mkurikulum> getAllKurikulums() {
        return getKurikulumDAO().getAllKurikulums();
    }

    @Override
    public List<Mkurikulum> getKurikulumsLikeProdi(String string) {
        return getKurikulumDAO().getKurikulumLikeProdi(string);
    }

    @Override
    public List<Mkurikulum> getKurikulumLikeCohort(String string) {
        return getKurikulumDAO().getKurikulumLikeCohort(string);
    }

    @Override
    public void saveOrUpdate(Mkurikulum entity) {
        getKurikulumDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Mkurikulum entity) {
        getKurikulumDAO().delete(entity);
    }

    @Override
    public ResultObject getAllKurikulumLikeText(String text, int start, int pageSize) {
        return getKurikulumDAO().getAllKurikulumLikeText(text, start, pageSize);
    }
}
