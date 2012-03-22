package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.MfeedbackDAO;
import id.ac.idu.administrasi.service.MfeedbackService;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mfeedback;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/21/12
 * Time: 4:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class MfeedbackServiceImpl implements MfeedbackService {

    private MfeedbackDAO mfeedbackDAO;

    public MfeedbackDAO getMfeedbackDAO() {
        return mfeedbackDAO;
    }

    public void setMfeedbackDAO(MfeedbackDAO mfeedbackDAO) {
        this.mfeedbackDAO = mfeedbackDAO;
    }

    @Override
    public Mfeedback getNewMfeedback() {
        return getMfeedbackDAO().getNewMfeedback();
    }

    @Override
    public Mfeedback getMfeedbackByID(Long fil_nr) {
        return getMfeedbackDAO().getMfeedbackById(fil_nr);
    }

    @Override
    public List<Mfeedback> getAllMfeedbacks() {
        return getMfeedbackDAO().getAllMfeedbacks();
    }

    @Override
    public void saveOrUpdate(Mfeedback mfeedback) {
        getMfeedbackDAO().saveOrUpdate(mfeedback);
    }

    @Override
    public void delete(Mfeedback mfeedback) {
        getMfeedbackDAO().delete(mfeedback);
    }

    @Override
    public List<Mfeedback> getMfeedbacksLikeCity(String string) {
        return getMfeedbackDAO().getMfeedbacksLikeCity(string);
    }

    @Override
    public List<Mfeedback> getMfeedbacksLikeName1(String string) {
        return getMfeedbackDAO().getMfeedbacksLikeName1(string);
    }

    @Override
    public List<Mfeedback> getMfeedbacksLikeNo(String string) {
        return getMfeedbackDAO().getMfeedbacksLikeNo(string);
    }

    @Override
    public int getCountAllMfeedbacks() {
        return getMfeedbackDAO().getCountAllMfeedbacks();
    }

     @Override
    public ResultObject getAllAlumniLikeText(String text, int start, int pageSize) {
        return getMfeedbackDAO().getAllAlumniLikeText(text, start, pageSize);
    }

}