package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.MkelDAO;
import id.ac.idu.administrasi.service.MkelService;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mkel;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/19/12
 * Time: 1:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class MkelServiceImpl implements MkelService {

    private MkelDAO mkelDAO;

    public MkelDAO getMkelDAO() {
        return mkelDAO;
    }

    public void setMkelDAO(MkelDAO mkelDAO) {
        this.mkelDAO = mkelDAO;
    }

    @Override
    public Mkel getNewMkel() {
        return getMkelDAO().getNewMkel();
    }

    @Override
    public Mkel getMkelByID(Long fil_nr) {
        return getMkelDAO().getMkelById(fil_nr);
    }

    @Override
    public List<Mkel> getAllMkels() {
        return getMkelDAO().getAllMkels();
    }

    @Override
    public void saveOrUpdate(Mkel mkel) {
        getMkelDAO().saveOrUpdate(mkel);
    }

    @Override
    public void delete(Mkel mkel) {
        getMkelDAO().delete(mkel);
    }

    @Override
    public List<Mkel> getMkelsLikeCity(String string) {
        return getMkelDAO().getMkelsLikeCity(string);
    }

    @Override
    public List<Mkel> getMkelsLikeName1(String string) {
        return getMkelDAO().getMkelsLikeName1(string);
    }

    @Override
    public List<Mkel> getMkelsLikeNo(String string) {
        return getMkelDAO().getMkelsLikeNo(string);
    }

    @Override
    public int getCountAllMkels() {
        return getMkelDAO().getCountAllMkels();
    }

     @Override
    public ResultObject getAllAlumniLikeText(String text, int start, int pageSize) {
        return getMkelDAO().getAllAlumniLikeText(text, start, pageSize);
    }

}
