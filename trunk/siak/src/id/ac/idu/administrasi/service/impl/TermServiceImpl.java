package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.TermDAO;
import id.ac.idu.administrasi.service.TermService;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mterm;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 28/03/12
 * Time: 10:30
 * To change this template use File | Settings | File Templates.
 */
public class TermServiceImpl implements TermService {
    private TermDAO termDAO;

    public TermDAO getTermDAO() {
        return termDAO;
    }

    public void setTermDAO(TermDAO termDAO) {
        this.termDAO = termDAO;
    }

    @Override
    public Mterm getNewTerm() {
        return getTermDAO().getNewTerm();
    }

    @Override
    public Mterm getTermById(int id) {
        return getTermDAO().getTermById(id);
    }

    @Override
    public Mterm getTermByKode(String kode) {
        return getTermDAO().getTermByKode(kode);
    }

    @Override
    public List<Mterm> getAllTerm() {
        return getTermDAO().getAllTerm();
    }

    @Override
    public int getCountAllTerm() {
        return getTermDAO().getCountAllTerm();
    }

    @Override
    public void saveOrUpdate(Mterm term) {
        getTermDAO().saveOrUpdate(term);
    }

    @Override
    public void delete(Mterm term) {
        getTermDAO().delete(term);
    }

    @Override
    public void save(Mterm entity) {
        getTermDAO().save(entity);
    }

    @Override
    public ResultObject getAllTermLikeText(String text, int start, int pageSize) {
        return getTermDAO().getAllTermLikeText(text, start, pageSize);
    }
}
