package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.*;
import id.ac.idu.administrasi.service.FeedbackDosenService;
import id.ac.idu.backend.model.Mfeedback;
import id.ac.idu.backend.model.Mpegawai;
import id.ac.idu.backend.model.Tfeedbackdosen;
import id.ac.idu.util.Codec;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 3/21/12
 * Time: 3:06 AM
 */
public class FeedbackDosenServiceImpl implements FeedbackDosenService{
    private FeedbackDosenDAO feedbackDosenDAO;
    private MahasiswaDAO mahasiswaDAO;
    private SekolahDAO sekolahDAO;
    private ProdiDAO prodiDAO;
    private FeedbackDAO feedbackDAO;

    public FeedbackDosenDAO getFeedbackDosenDAO() {
        return feedbackDosenDAO;
    }

    public void setFeedbackDosenDAO(FeedbackDosenDAO feedbackDosenDAO) {
        this.feedbackDosenDAO = feedbackDosenDAO;
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
    public Tfeedbackdosen getNewFeedbackDosen() {
        Tfeedbackdosen obj = getFeedbackDosenDAO().getNewFeedbackDosen();
        //obj.setMmahasiswa((Mmahasiswa) getMahasiswaDAO().getAll().get(0));
        //obj.setMsekolah((Msekolah) getSekolahDAO().getAllSekolah().get(0));
        //obj.setMprodi((Mprodi) getProdiDAO().getAllProdis().get(0));
        obj.setMfeedback((Mfeedback) getFeedbackDAO().getAll().get(0));
        obj.setNnopertanyaan(getFeedbackDAO().getAll().get(0).getNnopertanyaan());
        return obj;
    }

    @Override
    public List<Tfeedbackdosen> getNewFeedbackDosenList() {
        List<Mfeedback> lmf = getFeedbackDAO().getFeedbackLikeCode(Codec.KodeFeedback.D.getValue());
        List<Tfeedbackdosen> ltf = new ArrayList<Tfeedbackdosen>();
        for (Mfeedback mf : lmf) {
            Tfeedbackdosen obj = getFeedbackDosenDAO().getNewFeedbackDosen();
            obj.setMfeedback(mf);
            obj.setNnopertanyaan(mf.getNnopertanyaan());
            ltf.add(obj);
        }
        return ltf;
    }

    @Override
    public int getCountAll() {
        return getFeedbackDosenDAO().getCountAll();
    }

    @Override
    public Tfeedbackdosen getFeedbackDosenById(int id) {
        return getFeedbackDosenDAO().getFeedbackDosenById(id);
    }

    @Override
    public List<Tfeedbackdosen> getAll() {
        return getFeedbackDosenDAO().getAll();
    }

    @Override
    public List<Tfeedbackdosen> getFeedbackDosenLikeTerm(String string) {
        return getFeedbackDosenDAO().getFeedbackDosenLikeTerm(string);
    }

    @Override
    public List<Tfeedbackdosen> getFeedbackDosenLikeKelompok(String string) {
        return getFeedbackDosenDAO().getFeedbackDosenLikeKelompok(string);
    }

    @Override
    public List<Tfeedbackdosen> getFeedbackDosenLikePegawaiName(String string) {
        return getFeedbackDosenDAO().getFeedbackDosenLikePegawaiName(string);
    }

    @Override
    public void saveOrUpdate(Tfeedbackdosen entity) {
        getFeedbackDosenDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Tfeedbackdosen entity) {
        getFeedbackDosenDAO().delete(entity);
    }

    @Override
    public void saveOrUpdateList(List<Tfeedbackdosen> list) {
        for (Tfeedbackdosen entity:list) {
            getFeedbackDosenDAO().saveOrUpdate(entity);
        }
    }
    @Override
    public List<Tfeedbackdosen> getFeedbackDosenByNip(Mpegawai string, String term, String kelompok) {
        return getFeedbackDosenDAO().getFeedbackDosenByNip(string, term, kelompok);
    }
}
