package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.MalumniDAO;
import id.ac.idu.administrasi.service.MalumniService;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Malumni;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/12/12
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class MalumniServiceImpl implements MalumniService {

    private MalumniDAO malumniDAO;

    public MalumniDAO getMalumniDAO() {
        return malumniDAO;
    }

    public void setMalumniDAO(MalumniDAO malumniDAO) {
        this.malumniDAO = malumniDAO;
    }

    @Override
    public Malumni getNewMalumni() {
        return getMalumniDAO().getNewMalumni();
    }

    @Override
    public Malumni getMalumniByID(Long fil_nr) {
        return getMalumniDAO().getMalumniById(fil_nr);
    }

    @Override
    public List<Malumni> getAllMalumnis() {
        return getMalumniDAO().getAllMalumnis();
    }

    @Override
    public void saveOrUpdate(Malumni malumni) {
        getMalumniDAO().saveOrUpdate(malumni);
    }

    @Override
    public void delete(Malumni malumni) {
        getMalumniDAO().delete(malumni);
    }

    @Override
    public List<Malumni> getMalumnisLikeCity(String string) {
        return getMalumniDAO().getMalumnisLikeCity(string);
    }

    @Override
    public List<Malumni> getMalumnisLikeName1(String string) {
        return getMalumniDAO().getMalumnisLikeName1(string);
    }

    @Override
    public List<Malumni> getMalumnisLikeNo(String string) {
        return getMalumniDAO().getMalumnisLikeNo(string);
    }

    @Override
    public int getCountAllMalumnis() {
        return getMalumniDAO().getCountAllMalumnis();
    }

     @Override
    public ResultObject getAllAlumniLikeText(String text, int start, int pageSize) {
        return getMalumniDAO().getAllAlumniLikeText(text, start, pageSize);
    }

}