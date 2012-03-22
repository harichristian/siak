package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.MpeminatanDAO;
import id.ac.idu.administrasi.service.MpeminatanService;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mpeminatan;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/21/12
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class MpeminatanServiceImpl implements MpeminatanService {

    private MpeminatanDAO mpeminatanDAO;

    public MpeminatanDAO getMpeminatanDAO() {
        return mpeminatanDAO;
    }

    public void setMpeminatanDAO(MpeminatanDAO mpeminatanDAO) {
        this.mpeminatanDAO = mpeminatanDAO;
    }

    @Override
    public Mpeminatan getNewMpeminatan() {
        return getMpeminatanDAO().getNewMpeminatan();
    }

    @Override
    public Mpeminatan getMpeminatanByID(Long fil_nr) {
        return getMpeminatanDAO().getMpeminatanById(fil_nr);
    }

    @Override
    public List<Mpeminatan> getAllMpeminatans() {
        return getMpeminatanDAO().getAllMpeminatans();
    }

    @Override
    public void saveOrUpdate(Mpeminatan mpeminatan) {
        getMpeminatanDAO().saveOrUpdate(mpeminatan);
    }

    @Override
    public void delete(Mpeminatan mpeminatan) {
        getMpeminatanDAO().delete(mpeminatan);
    }

    @Override
    public List<Mpeminatan> getMpeminatansLikeCity(String string) {
        return getMpeminatanDAO().getMpeminatansLikeCity(string);
    }

    @Override
    public List<Mpeminatan> getMpeminatansLikeName1(String string) {
        return getMpeminatanDAO().getMpeminatansLikeName1(string);
    }

    @Override
    public List<Mpeminatan> getMpeminatansLikeNo(String string) {
        return getMpeminatanDAO().getMpeminatansLikeNo(string);
    }

    @Override
    public int getCountAllMpeminatans() {
        return getMpeminatanDAO().getCountAllMpeminatans();
    }

     @Override
    public ResultObject getAllAlumniLikeText(String text, int start, int pageSize) {
        return getMpeminatanDAO().getAllAlumniLikeText(text, start, pageSize);
    }

}