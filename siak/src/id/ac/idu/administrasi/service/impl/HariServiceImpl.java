package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.HariDAO;
import id.ac.idu.administrasi.service.HariService;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mhari;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/8/12
 * Time: 7:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class HariServiceImpl implements HariService {
    private HariDAO hariDAO;

    public HariDAO getHariDAO() {
        return hariDAO;
    }

    public void setHariDAO(HariDAO hariDAO) {
        this.hariDAO = hariDAO;
    }

    @Override
    public Mhari getNewHari() {
        return getHariDAO().getNewHari();
    }

    @Override
    public int getCountAllHaris() {
        return getHariDAO().getCountAllHaris();
    }

    @Override
    public Mhari getHariByID(int id) {
        return getHariDAO().getHariById(id);
    }

    @Override
    public List<Mhari> getAllHaris() {
        return getHariDAO().getAllHaris();
    }

    @Override
    public List<Mhari> getHarisLikeName(String string) {
        return getHariDAO().getHarisLikeName(string);
    }

    @Override
    public void saveOrUpdate(Mhari entity) {
        getHariDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Mhari entity) {
        getHariDAO().delete(entity);
    }

    @Override
    public ResultObject getAllHariLikeText(String text, int start, int pageSize) {
        return getHariDAO().getAllHariLikeText(text, start, pageSize);
    }
}
