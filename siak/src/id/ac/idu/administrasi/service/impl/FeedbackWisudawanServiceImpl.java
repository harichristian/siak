package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.*;
import id.ac.idu.administrasi.service.FeedbackWisudawanService;
import id.ac.idu.backend.model.Mfeedback;
import id.ac.idu.backend.model.Tfeedbackwisudawan;
import id.ac.idu.util.Codec;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 3/21/12
 * Time: 3:06 AM
 */
public class FeedbackWisudawanServiceImpl implements FeedbackWisudawanService{
    private FeedbackWisudawanDAO feedbackWisudawanDAO;
    private MahasiswaDAO mahasiswaDAO;
    private SekolahDAO sekolahDAO;
    private ProdiDAO prodiDAO;
    private FeedbackDAO feedbackDAO;

    public FeedbackWisudawanDAO getFeedbackWisudawanDAO() {
        return feedbackWisudawanDAO;
    }

    public void setFeedbackWisudawanDAO(FeedbackWisudawanDAO feedbackWisudawanDAO) {
        this.feedbackWisudawanDAO = feedbackWisudawanDAO;
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
    public Tfeedbackwisudawan getNewFeedbackWisudawan() {
        Tfeedbackwisudawan obj = getFeedbackWisudawanDAO().getNewFeedbackWisudawan();
        //obj.setMmahasiswa((Mmahasiswa) getMahasiswaDAO().getAll().get(0));
        //obj.setMsekolah((Msekolah) getSekolahDAO().getAllSekolah().get(0));
        //obj.setMprodi((Mprodi) getProdiDAO().getAllProdis().get(0));
        obj.setMfeedback((Mfeedback) getFeedbackDAO().getAll().get(0));
        obj.setNnopertanyaan(getFeedbackDAO().getAll().get(0).getNnopertanyaan());
        return obj;
    }

    @Override
    public List<Tfeedbackwisudawan> getNewFeedbackWisudawanList() {
        List<Mfeedback> lmf = getFeedbackDAO().getFeedbackLikeCode(Codec.KodeFeedback.A.getValue());
        List<Tfeedbackwisudawan> ltf = new ArrayList<Tfeedbackwisudawan>();
        for (Mfeedback mf : lmf) {
            Tfeedbackwisudawan obj = getFeedbackWisudawanDAO().getNewFeedbackWisudawan();
            obj.setMfeedback(mf);
            obj.setNnopertanyaan(mf.getNnopertanyaan());
            ltf.add(obj);
        }
        return ltf;
    }

    @Override
    public int getCountAll() {
        return getFeedbackWisudawanDAO().getCountAll();
    }

    @Override
    public Tfeedbackwisudawan getFeedbackWisudawanById(int id) {
        return getFeedbackWisudawanDAO().getFeedbackWisudawanById(id);
    }

    @Override
    public List<Tfeedbackwisudawan> getAll() {
        return getFeedbackWisudawanDAO().getAll();
    }

    @Override
    public List<Tfeedbackwisudawan> getFeedbackWisudawanLikeTerm(String string) {
        return getFeedbackWisudawanDAO().getFeedbackWisudawanLikeTerm(string);
    }

    @Override
    public List<Tfeedbackwisudawan> getFeedbackWisudawanLikeKelompok(String string) {
        return getFeedbackWisudawanDAO().getFeedbackWisudawanLikeKelompok(string);
    }

    @Override
    public List<Tfeedbackwisudawan> getFeedbackWisudawanLikeMahasiswaName(String string) {
        return getFeedbackWisudawanDAO().getFeedbackWisudawanLikeMahasiswaName(string);
    }

    @Override
    public void saveOrUpdate(Tfeedbackwisudawan entity) {
        getFeedbackWisudawanDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Tfeedbackwisudawan entity) {
        getFeedbackWisudawanDAO().delete(entity);
    }

    @Override
    public void saveOrUpdateList(List<Tfeedbackwisudawan> list) {
        for (Tfeedbackwisudawan entity:list) {
            getFeedbackWisudawanDAO().saveOrUpdate(entity);
        }
    }
    @Override
    public List<Tfeedbackwisudawan> getFeedbackWisudawanByNim(String string, String term, String kelompok) {
        return getFeedbackWisudawanDAO().getFeedbackWisudawanByNim(string, term, kelompok);
    }
}
