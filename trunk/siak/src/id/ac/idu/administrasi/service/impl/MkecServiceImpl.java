package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.MkecDAO;
import id.ac.idu.administrasi.service.MkecService;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mkec;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/19/12
 * Time: 1:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class MkecServiceImpl implements MkecService {

    private MkecDAO mkecDAO;

    public MkecDAO getMkecDAO() {
        return mkecDAO;
    }

    public void setMkecDAO(MkecDAO mkecDAO) {
        this.mkecDAO = mkecDAO;
    }

    @Override
    public Mkec getNewMkec() {
        return getMkecDAO().getNewMkec();
    }

    @Override
    public Mkec getMkecByID(Long fil_nr) {
        return getMkecDAO().getMkecById(fil_nr);
    }

    @Override
    public List<Mkec> getAllMkecs() {
        return getMkecDAO().getAllMkecs();
    }

    @Override
    public void saveOrUpdate(Mkec mkec) {
        getMkecDAO().saveOrUpdate(mkec);
    }

    @Override
    public void delete(Mkec mkec) {
        getMkecDAO().delete(mkec);
    }

    @Override
    public List<Mkec> getMkecsLikeCity(String string) {
        return getMkecDAO().getMkecsLikeCity(string);
    }

    @Override
    public List<Mkec> getMkecsLikeName1(String string) {
        return getMkecDAO().getMkecsLikeName1(string);
    }

    @Override
    public List<Mkec> getMkecsLikeNo(String string) {
        return getMkecDAO().getMkecsLikeNo(string);
    }

    @Override
    public int getCountAllMkecs() {
        return getMkecDAO().getCountAllMkecs();
    }

     @Override
    public ResultObject getAllAlumniLikeText(String text, int start, int pageSize) {
        return getMkecDAO().getAllAlumniLikeText(text, start, pageSize);
    }

}
