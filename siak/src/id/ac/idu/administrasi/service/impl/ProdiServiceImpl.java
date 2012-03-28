package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.JenjangDAO;
import id.ac.idu.administrasi.dao.ProdiDAO;
import id.ac.idu.administrasi.service.ProdiService;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mjenjang;
import id.ac.idu.backend.model.Mprodi;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/7/12
 * Time: 8:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProdiServiceImpl implements ProdiService {
    private ProdiDAO prodiDAO;
    private JenjangDAO jenjangDAO;

    public ProdiDAO getProdiDAO() {
        return prodiDAO;
    }

    public void setProdiDAO(ProdiDAO prodiDAO) {
        this.prodiDAO = prodiDAO;
    }

    public JenjangDAO getJenjangDAO() {
        return jenjangDAO;
    }

    public void setJenjangDAO(JenjangDAO jenjangDAO) {
        this.jenjangDAO = jenjangDAO;
    }

    @Override
    public Mprodi getNewProdi() {
        //return getProdiDAO().getNewProdi();
        Mprodi obj = getProdiDAO().getNewProdi();
        obj.setMjenjang((Mjenjang) getJenjangDAO().getAllJenjangs().get(0));
        return obj;
    }

    @Override
    public int getCountAllProdis() {
        return getProdiDAO().getCountAllProdis();
    }

    @Override
    public Mprodi getProdiByID(int id) {
        return getProdiDAO().getProdiById(id);
    }

    @Override
    public List<Mprodi> getAllProdis() {
        return getProdiDAO().getAllProdis();
    }

    @Override
    public List<Mprodi> getProdisLikeName(String string) {
        return getProdiDAO().getProdisLikeName(string);
    }

    @Override
    public List<Mprodi> getProdisLikeSingkatan(String string) {
        return getProdiDAO().getProdisLikeSingkatan(string);
    }

    @Override
    public List<Mprodi> getProdisLikeStatus(String string) {
        return getProdiDAO().getProdiLikeStatus(string);
    }

    @Override
    public void saveOrUpdate(Mprodi entity) {
        getProdiDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Mprodi entity) {
        getProdiDAO().delete(entity);
    }

    /*@Override
    public List<Mjenjang> getAllJenjangs() {
        return getJenjangDAO().getAllJenjangs();
    }*/
    
    @Override
    public ResultObject getAllProdiLikeText(String text, int start, int pageSize) {
        return getProdiDAO().getAllProdiLikeText(text, start, pageSize);
    }
}
