package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.ThajarDAO;
import id.ac.idu.administrasi.service.ThajarService;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mthajar;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 05/04/12
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public class ThajarServiceImpl implements ThajarService {
    private ThajarDAO thajarDAO;

    public ThajarDAO getThajarDAO() {
        return thajarDAO;
    }

    public void setThajarDAO(ThajarDAO thajarDAO) {
        this.thajarDAO = thajarDAO;
    }

    @Override
    public Mthajar getNewThajar() {
        return getThajarDAO().getNewThajar();
    }

    @Override
    public Mthajar getThajarById(int id) {
        return getThajarDAO().getThajarById(id);
    }

    @Override
    public Mthajar getThajarByKode(String kode) {
        return getThajarDAO().getThajarByKode(kode);
    }

    @Override
    public List<Mthajar> getAllThajar() {
        return getThajarDAO().getAllThajar();
    }

    @Override
    public int getCountAllThajar() {
        return getThajarDAO().getCountAllThajar();
    }

    @Override
    public void saveOrUpdate(Mthajar thajar) {
        getThajarDAO().saveOrUpdate(thajar);
    }

    @Override
    public void delete(Mthajar thajar) {
        getThajarDAO().delete(thajar);
    }

    @Override
    public void save(Mthajar entity) {
        getThajarDAO().save(entity);
    }

    @Override
    public ResultObject getAllThajarLikeText(String text, int start, int pageSize) {
        return getThajarDAO().getAllThajarLikeText(text, start, pageSize);
    }
}
