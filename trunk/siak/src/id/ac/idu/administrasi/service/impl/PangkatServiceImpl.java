package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.PangkatDAO;
import id.ac.idu.administrasi.service.PangkatService;
import id.ac.idu.backend.model.Mpangkatgolongan;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/8/12
 * Time: 10:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class PangkatServiceImpl implements PangkatService {
    private PangkatDAO pangkatDAO;

    public PangkatDAO getPangkatDAO() {
        return pangkatDAO;
    }

    public void setPangkatDAO(PangkatDAO pangkatDAO) {
        this.pangkatDAO = pangkatDAO;
    }

    @Override
    public Mpangkatgolongan getNewPangkat() {
        return getPangkatDAO().getNewPangkat();
    }

    @Override
    public int getCountAllPangkats() {
        return getPangkatDAO().getCountAllPangkats();
    }

    @Override
    public Mpangkatgolongan getPangkatById(int id) {
        return getPangkatDAO().getPangkatById(id);
    }

    @Override
    public List<Mpangkatgolongan> getAllPangkats() {
        return getAllPangkats();
    }

    @Override
    public List<Mpangkatgolongan> getPangkatsLikeName(String string) {
        return getPangkatDAO().getPangkatsLikeName(string);
    }

    @Override
    public void saveOrUpdate(Mpangkatgolongan entity) {
        getPangkatDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Mpangkatgolongan entity) {
        getPangkatDAO().delete(entity);
    }
}
