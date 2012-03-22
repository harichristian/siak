package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.*;
import id.ac.idu.administrasi.service.FeedbackAlumniService;
import id.ac.idu.backend.model.Mfeedback;
import id.ac.idu.backend.model.Mprodi;
import id.ac.idu.backend.model.Msekolah;
import id.ac.idu.backend.model.Tfeedbackalumni;

import java.util.List;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 3/21/12
 * Time: 3:06 AM
 */
public class FeedbackAlumniServiceImpl implements FeedbackAlumniService{
    private FeedbackAlumniDAO feedbackAlumniDAO;
    private MahasiswaDAO mahasiswaDAO;
    private SekolahDAO sekolahDAO;
    private ProdiDAO prodiDAO;
    private FeedbackDAO feedbackDAO;

    public FeedbackAlumniDAO getFeedbackAlumniDAO() {
        return feedbackAlumniDAO;
    }

    public void setFeedbackAlumniDAO(FeedbackAlumniDAO feedbackAlumniDAO) {
        this.feedbackAlumniDAO = feedbackAlumniDAO;
    }

    public MahasiswaDAO getMahasiswaDAO() {
        return mahasiswaDAO;
    }

    public void setMahasiswaDAO(MahasiswaDAO mahasiswaDAO) {
        this.mahasiswaDAO = mahasiswaDAO;
    }

    public SekolahDAO getSekolahDAO() {
        return sekolahDAO;
    }

    public void setSekolahDAO(SekolahDAO sekolahDAO) {
        this.sekolahDAO = sekolahDAO;
    }

    public ProdiDAO getProdiDAO() {
        return prodiDAO;
    }

    public void setProdiDAO(ProdiDAO prodiDAO) {
        this.prodiDAO = prodiDAO;
    }

    public FeedbackDAO getFeedbackDAO() {
        return feedbackDAO;
    }

    public void setFeedbackDAO(FeedbackDAO feedbackDAO) {
        this.feedbackDAO = feedbackDAO;
    }

    @Override
    public Tfeedbackalumni getNewFeedbackAlumni() {
        Tfeedbackalumni obj = getFeedbackAlumniDAO().getNewFeedbackAlumni();
        //obj.setMmahasiswa((Mmahasiswa) getMahasiswaDAO().getAll().get(0));
        obj.setMsekolah((Msekolah) getSekolahDAO().getAllSekolah().get(0));
        obj.setMprodi((Mprodi) getProdiDAO().getAllProdis().get(0));
        obj.setMfeedback((Mfeedback) getFeedbackDAO().getAll().get(0));
        obj.setNnopertanyaan(getFeedbackDAO().getAll().get(0).getNnopertanyaan());
        return obj;
    }

    @Override
    public int getCountAll() {
        return getFeedbackAlumniDAO().getCountAll();
    }

    @Override
    public Tfeedbackalumni getFeedbackAlumniById(int id) {
        return getFeedbackAlumniDAO().getFeedbackAlumniById(id);
    }

    @Override
    public List<Tfeedbackalumni> getAll() {
        return getFeedbackAlumniDAO().getAll();
    }

    @Override
    public List<Tfeedbackalumni> getFeedbackAlumniLikeTerm(String string) {
        return getFeedbackAlumniDAO().getFeedbackAlumniLikeTerm(string);
    }

    @Override
    public List<Tfeedbackalumni> getFeedbackAlumniLikeKelompok(String string) {
        return getFeedbackAlumniDAO().getFeedbackAlumniLikeKelompok(string);
    }

    @Override
    public List<Tfeedbackalumni> getFeedbackAlumniLikeMahasiswaName(String string) {
        return getFeedbackAlumniLikeMahasiswaName(string);
    }

    @Override
    public void saveOrUpdate(Tfeedbackalumni entity) {
        getFeedbackAlumniDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Tfeedbackalumni entity) {
        getFeedbackAlumniDAO().delete(entity);
    }
}
