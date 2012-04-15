package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.HistKerjaDAO;
import id.ac.idu.administrasi.service.HistKerjaService;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Malumni;
import id.ac.idu.backend.model.Thistkerja;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 4/13/12
 * Time: 10:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class HistKerjaServiceImpl implements HistKerjaService {
    private HistKerjaDAO histKerjaDAO;

    public HistKerjaDAO getHistKerjaDAO() {
        return histKerjaDAO;
    }

    public void setHistKerjaDAO(HistKerjaDAO histKerjaDAO) {
        this.histKerjaDAO = histKerjaDAO;
    }

    @Override
    public Thistkerja getNewHistKerja() {
        return getHistKerjaDAO().getNewHistKerja();
    }

    @Override
    public Thistkerja getHistKerjaByID(int id) {
        return getHistKerjaDAO().getHistKerjaById(id);
    }

    @Override
    public Thistkerja getHistKerjaByName(String name) {
        return getHistKerjaDAO().getHistKerjaByName(name);
    }

    @Override
    public List<Thistkerja> getAllHistKerja() {
        return getHistKerjaDAO().getAllHistKerja();
    }

    @Override
    public int getCountAllHistKerja() {
        return getHistKerjaDAO().getCountAllHistKerja();
    }

    @Override
    public void saveOrUpdate(Thistkerja histKerja) {
        getHistKerjaDAO().saveOrUpdate(histKerja);
    }

    @Override
    public void save(Thistkerja histKerja) {
        getHistKerjaDAO().save(histKerja);
    }

    @Override
    public void delete(Thistkerja histKerja) {
        getHistKerjaDAO().delete(histKerja);
    }

     @Override
    public ResultObject getAllHistKerjaLikeText(String text, int start, int pageSize) {
        return getHistKerjaDAO().getAllHistKerjaLikeText(text, start, pageSize);
    }

    @Override
    public List<Thistkerja> getAllHIstkerjaByAlumni(Malumni alumni) {
        return getHistKerjaDAO().getAllHIstkerjaByAlumni(alumni);
    }
}
