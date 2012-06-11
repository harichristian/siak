package id.ac.idu.irs.service.impl;

import id.ac.idu.administrasi.dao.ProdiDAO;
import id.ac.idu.administrasi.dao.SekolahDAO;
import id.ac.idu.backend.model.Mprodi;
import id.ac.idu.backend.model.Msekolah;
import id.ac.idu.backend.model.Tpaketkuliah;
import id.ac.idu.irs.dao.PaketDAO;
import id.ac.idu.irs.service.PaketService;
import id.ac.idu.mankurikulum.dao.MatakuliahDAO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/13/12
 * Time: 10:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class PaketServiceImpl implements PaketService{
    private PaketDAO paketDAO;
    private SekolahDAO sekolahDAO;
    private ProdiDAO prodiDAO;
    private MatakuliahDAO matakuliahDAO;

    public PaketDAO getPaketDAO() {
        return paketDAO;
    }

    public void setPaketDAO(PaketDAO paketDAO) {
        this.paketDAO = paketDAO;
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

    public MatakuliahDAO getMatakuliahDAO() {
        return matakuliahDAO;
    }

    public void setMatakuliahDAO(MatakuliahDAO matakuliahDAO) {
        this.matakuliahDAO = matakuliahDAO;
    }
    @Override
    public Tpaketkuliah getNewPaket() {
        return getPaketDAO().getNewPaket();
    }

    @Override
    public int getCountAll() {
        return getPaketDAO().getCountAll();
    }

    @Override
    public Tpaketkuliah getPaketById(int id) {
        return getPaketDAO().getPaketById(id);
    }

    @Override
    public List<Tpaketkuliah> getAll() {
        return getPaketDAO().getAll();
    }

    @Override
    public List<Tpaketkuliah> getPaketLikeTerm(String string) {
        return getPaketDAO().getPaketLikeTerm(string);
    }

    @Override
    public List<Tpaketkuliah> getPaketLikeThajar(String string) {
        return getPaketDAO().getPaketLikeThajar(string);
    }

    @Override
    public List<Tpaketkuliah> getPaketLikeProdi(String string) {
        return getPaketDAO().getPaketLikeProdi(string);
    }

    @Override
    public void saveOrUpdate(Tpaketkuliah entity) {
        getPaketDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Tpaketkuliah entity) {
        getPaketDAO().delete(entity);
    }

    @Override
    public List<Tpaketkuliah> getPaketList(String cterm, Msekolah msekolah, Mprodi mprodi) {
        return getPaketDAO().getPaketList(cterm, msekolah, mprodi);
    }

    @Override
    public void saveOrUpdateList(List<Tpaketkuliah> selectedPaketList) {
        for (Tpaketkuliah entity:selectedPaketList) {
            getPaketDAO().saveOrUpdate(entity);
        }
    }

    @Override
    public void saveOrUpdateList(List<Tpaketkuliah> selectedPaketList, List<Tpaketkuliah> delPaketList) {
        for (Tpaketkuliah entity:selectedPaketList) {
            getPaketDAO().saveOrUpdate(entity);
        }
        for (Tpaketkuliah entity:delPaketList) {
            getPaketDAO().delete(entity);
        }
    }
}
