package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.MkabDAO;
import id.ac.idu.administrasi.service.MkabService;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mkab;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/19/12
 * Time: 1:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class MkabServiceImpl implements MkabService {

    private MkabDAO mkabDAO;

    public MkabDAO getMkabDAO() {
        return mkabDAO;
    }

    public void setMkabDAO(MkabDAO mkabDAO) {
        this.mkabDAO = mkabDAO;
    }

    @Override
    public Mkab getNewMkab() {
        return getMkabDAO().getNewMkab();
    }

    @Override
    public Mkab getMkabByID(Long fil_nr) {
        return getMkabDAO().getMkabById(fil_nr);
    }

    @Override
    public List<Mkab> getAllMkabs() {
        return getMkabDAO().getAllMkabs();
    }

    @Override
    public void saveOrUpdate(Mkab mkab) {
        getMkabDAO().saveOrUpdate(mkab);
    }

    @Override
    public void delete(Mkab mkab) {
        getMkabDAO().delete(mkab);
    }

    @Override
    public List<Mkab> getMkabsLikeCity(String string) {
        return getMkabDAO().getMkabsLikeCity(string);
    }

    @Override
    public List<Mkab> getMkabsLikeName1(String string) {
        return getMkabDAO().getMkabsLikeName1(string);
    }

    @Override
    public List<Mkab> getMkabsLikeNo(String string) {
        return getMkabDAO().getMkabsLikeNo(string);
    }

    @Override
    public int getCountAllMkabs() {
        return getMkabDAO().getCountAllMkabs();
    }

     @Override
    public ResultObject getAllAlumniLikeText(String text, int start, int pageSize) {
        return getMkabDAO().getAllAlumniLikeText(text, start, pageSize);
    }

}
