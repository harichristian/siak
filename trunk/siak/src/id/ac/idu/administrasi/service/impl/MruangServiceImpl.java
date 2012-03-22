package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.MruangDAO;
import id.ac.idu.administrasi.service.MruangService;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mruang;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/6/12
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class MruangServiceImpl implements MruangService {

    private MruangDAO mruangDAO;

    public MruangDAO getMruangDAO() {
        return mruangDAO;
    }

    public void setMruangDAO(MruangDAO mruangDAO) {
        this.mruangDAO = mruangDAO;
    }

    @Override
    public Mruang getNewRuang() {
        return getMruangDAO().getNewRuang();
    }

    @Override
    public Mruang getMruangByID(int fil_nr) {
        return getMruangDAO().getMruangById(fil_nr);
    }

    @Override
    public List<Mruang> getAllMruangs() {
        return getMruangDAO().getAllMruangs();
    }

    @Override
    public void saveOrUpdate(Mruang mruang) {
        getMruangDAO().saveOrUpdate(mruang);
    }

    @Override
    public void delete(Mruang mruang) {
        getMruangDAO().delete(mruang);
    }

    @Override
    public List<Mruang> getMruangsLikeCity(String string) {
        return getMruangDAO().getMruangsLikeCity(string);
    }

    @Override
    public List<Mruang> getMruangsLikeName1(String string) {
        return getMruangDAO().getMruangsLikeName1(string);
    }

    @Override
    public List<Mruang> getMruangsLikeNo(String string) {
        return getMruangDAO().getMruangsLikeNo(string);
    }

    @Override
    public int getCountAllMruangs() {
        return getMruangDAO().getCountAllMruangs();
    }

     @Override
    public ResultObject getAllMruangLikeText(String text, int start, int pageSize) {
        return getMruangDAO().getAllMruangLikeText(text, start, pageSize);
    }
}
