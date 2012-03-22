package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.MprovDAO;
import id.ac.idu.administrasi.service.MprovService;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mprov;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/19/12
 * Time: 11:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class MprovServiceImpl implements MprovService {

    private MprovDAO mprovDAO;

    public MprovDAO getMprovDAO() {
        return mprovDAO;
    }

    public void setMprovDAO(MprovDAO mprovDAO) {
        this.mprovDAO = mprovDAO;
    }

    @Override
    public Mprov getNewMprov() {
        return getMprovDAO().getNewMprov();
    }

    @Override
    public Mprov getMprovByID(Long fil_nr) {
        return getMprovDAO().getMprovById(fil_nr);
    }

    @Override
    public List<Mprov> getAllMprovs() {
        return getMprovDAO().getAllMprovs();
    }

    @Override
    public void saveOrUpdate(Mprov mprov) {
        getMprovDAO().saveOrUpdate(mprov);
    }

    @Override
    public void delete(Mprov mprov) {
        getMprovDAO().delete(mprov);
    }

    @Override
    public List<Mprov> getMprovsLikeCity(String string) {
        return getMprovDAO().getMprovsLikeCity(string);
    }

    @Override
    public List<Mprov> getMprovsLikeName1(String string) {
        return getMprovDAO().getMprovsLikeName1(string);
    }

    @Override
    public List<Mprov> getMprovsLikeNo(String string) {
        return getMprovDAO().getMprovsLikeNo(string);
    }

    @Override
    public int getCountAllMprovs() {
        return getMprovDAO().getCountAllMprovs();
    }

     @Override
    public ResultObject getAllAlumniLikeText(String text, int start, int pageSize) {
        return getMprovDAO().getAllAlumniLikeText(text, start, pageSize);
    }

}