package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.JenjangDAO;
import id.ac.idu.administrasi.service.JenjangService;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mjenjang;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/8/12
 * Time: 6:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class JenjangServiceImpl implements JenjangService {
    private JenjangDAO jenjangDAO;

    public JenjangDAO getJenjangDAO() {
        return jenjangDAO;
    }

    public void setJenjangDAO(JenjangDAO jenjangDAO) {
        this.jenjangDAO = jenjangDAO;
    }

    @Override
    public Mjenjang getNewJenjang() {
        return getJenjangDAO().getNewJenjang();
    }

    @Override
    public int getCountAllJenjangs() {
        return getJenjangDAO().getCountAllJenjangs();
    }

    @Override
    public Mjenjang getJenjangByID(int id) {
        return getJenjangDAO().getJenjangById(id);
    }

    @Override
    public List<Mjenjang> getAllJenjangs() {
        return getJenjangDAO().getAllJenjangs();
    }

    @Override
    public List<Mjenjang> getJenjangsLikeName(String string) {
        return getJenjangDAO().getJenjangsLikeName(string);
    }

    @Override
    public List<Mjenjang> getJenjangsLikeSingkatan(String string) {
        return getJenjangDAO().getJenjangsLikeSingkatan(string);
    }

    @Override
    public void saveOrUpdate(Mjenjang entity) {
        getJenjangDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Mjenjang entity) {
        getJenjangDAO().delete(entity);
    }

    @Override
    public ResultObject getAllJenjangLikeText(String text, int start, int pageSize) {
        return getJenjangDAO().getAllJenjangLikeText(text, start, pageSize);
    }
}
