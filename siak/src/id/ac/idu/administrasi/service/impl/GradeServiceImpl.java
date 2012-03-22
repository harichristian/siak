package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.GradeDAO;
import id.ac.idu.administrasi.service.GradeService;
import id.ac.idu.backend.model.Mgrade;
import id.ac.idu.backend.model.Mjenjang;
import id.ac.idu.backend.model.Mprodi;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 07/03/12
 * Time: 12:23
 * To change this template use File | Settings | File Templates.
 */
public class GradeServiceImpl implements GradeService {
    private GradeDAO gradeDAO;

    public GradeDAO getGradeDAO() {
        return gradeDAO;
    }

    public void setGradeDAO(GradeDAO gradeDAO) {
        this.gradeDAO = gradeDAO;
    }

    @Override
    public Mgrade getNewGrade() {
        return getGradeDAO().getNewGrade();
    }

    @Override
    public Mgrade getGradeById(int id) {
        return getGradeDAO().getGradeById(id);
    }

    @Override
    public Mgrade getGradeByCode(String code) {
        return getGradeDAO().getGradeByCode(code);
    }

    @Override
    public Mgrade getGradeByName(String name) {
        return getGradeDAO().getGradeByName(name);
    }

    @Override
    public List<Mgrade> getGradeByProdi(Mprodi prodi) {
        return getGradeDAO().getGradeByProdi(prodi);
    }

    @Override
    public List<Mgrade> getGradeByJenjang(Mjenjang jenjang) {
        return getGradeDAO().getGradeByJenjang(jenjang);
    }

    @Override
    public List<Mgrade> getAllGrade() {
        return getGradeDAO().getAllGrade();
    }

    @Override
    public int getCountAllGrade() {
        return getGradeDAO().getCountAllGrade();
    }

    @Override
    public void saveOrUpdate(Mgrade entity) {
        getGradeDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Mgrade entity) {
        getGradeDAO().delete(entity);
    }

    @Override
    public void save(Mgrade entity) {
        getGradeDAO().save(entity);
    }
}
