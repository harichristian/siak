package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.TfeedbackinstansiDAO;
import id.ac.idu.administrasi.service.TfeedbackinstansiService;
import id.ac.idu.backend.model.Tfeedbackinstansi;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/13/12
 * Time: 1:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class TfeedbackinstansiServiceImpl implements TfeedbackinstansiService {

    private TfeedbackinstansiDAO tfeedbackinstansiDAO;

    public TfeedbackinstansiDAO getTfeedbackinstansiDAO() {
        return tfeedbackinstansiDAO;
    }

    public void setTfeedbackinstansiDAO(TfeedbackinstansiDAO tfeedbackinstansiDAO) {
        this.tfeedbackinstansiDAO = tfeedbackinstansiDAO;
    }

    @Override
    public Tfeedbackinstansi getNewTfeedbackinstansi() {
        return getTfeedbackinstansiDAO().getNewTfeedbackinstansi();
    }

    @Override
    public Tfeedbackinstansi getTfeedbackinstansiByID(Long fil_nr) {
        return getTfeedbackinstansiDAO().getTfeedbackinstansiById(fil_nr);
    }

    @Override
    public List<Tfeedbackinstansi> getAllTfeedbackinstansis() {
        return getTfeedbackinstansiDAO().getAllTfeedbackinstansis();
    }

    @Override
    public void saveOrUpdate(Tfeedbackinstansi tfeedbackinstansi) {
        getTfeedbackinstansiDAO().saveOrUpdate(tfeedbackinstansi);
    }

    @Override
    public void delete(Tfeedbackinstansi tfeedbackinstansi) {
        getTfeedbackinstansiDAO().delete(tfeedbackinstansi);
    }

    @Override
    public List<Tfeedbackinstansi> getTfeedbackinstansisLikeCity(String string) {
        return getTfeedbackinstansiDAO().getTfeedbackinstansisLikeCity(string);
    }

    @Override
    public List<Tfeedbackinstansi> getTfeedbackinstansisLikeName1(String string) {
        return getTfeedbackinstansiDAO().getTfeedbackinstansisLikeName1(string);
    }

    @Override
    public List<Tfeedbackinstansi> getTfeedbackinstansisLikeNo(String string) {
        return getTfeedbackinstansiDAO().getTfeedbackinstansisLikeNo(string);
    }

    @Override
    public int getCountAllTfeedbackinstansis() {
        return getTfeedbackinstansiDAO().getCountAllTfeedbackinstansis();
    }

}
