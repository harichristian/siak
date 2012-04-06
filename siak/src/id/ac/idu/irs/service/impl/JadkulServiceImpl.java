package id.ac.idu.irs.service.impl;

import id.ac.idu.backend.model.*;
import id.ac.idu.irs.dao.JadkuldetilDAO;
import id.ac.idu.irs.dao.JadkulmasterDAO;
import id.ac.idu.irs.service.JadkulService;

import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 11/03/12
 * Time: 4:07
 * To change this template use File | Settings | File Templates.
 */
public class JadkulServiceImpl implements JadkulService {

    private JadkulmasterDAO tjadkulmasterDAO;
    private JadkuldetilDAO tjadkuldetilDAO;

    public JadkulmasterDAO getJadkulmasterDAO() {
        return tjadkulmasterDAO;
    }

    public void setJadkulmasterDAO(JadkulmasterDAO tjadkulmasterDAO) {
        this.tjadkulmasterDAO = tjadkulmasterDAO;
    }

    public JadkuldetilDAO getJadkuldetilDAO() {
        return tjadkuldetilDAO;
    }

    public void setJadkuldetilDAO(JadkuldetilDAO tjadkuldetilDAO) {
        this.tjadkuldetilDAO = tjadkuldetilDAO;
    }

    /**
     * default Constructor
     */
    public JadkulServiceImpl() {
    }

    @Override
    public Tjadkulmaster getNewTjadkulmaster() {
        return getJadkulmasterDAO().getNewTjadkulmaster();
    }

    @Override
    public List<Tjadkulmaster> getTjadkulmastersForReport(Tjadkulmaster param) {
        return getJadkulmasterDAO().getTjadkulmastersForReport(param);
    }

    @Override
    public List<Tjadkulmaster> getTjadkulmastersBySekolahAndProdiAndPegawai1(Msekolah sekolah, Mprodi prodi, Mpegawai pegawai1) {
        return getJadkulmasterDAO().getTjadkulmastersBySekolahAndProdiAndPegawai1(sekolah, prodi, pegawai1);
    }

    @Override
    public List<Tjadkulmaster> getTjadkulmastersBySekolahAndProdi(Msekolah sekolah, Mprodi prodi) {
        return getJadkulmasterDAO().getTjadkulmastersBySekolahAndProdi(sekolah, prodi);
    }

    @Override
    public List<Tjadkulmaster> getTjadkulmastersBySekolah(Msekolah sekolah) {
        return getJadkulmasterDAO().getTjadkulmastersBySekolah(sekolah);
    }

    @Override
    public List<Tjadkulmaster> getTjadkulmastersByProdi(Mprodi prodi) {
        return getJadkulmasterDAO().getTjadkulmastersByProdi(prodi);
    }

    @Override
    public List<Tjadkulmaster> getTjadkulmastersByPegawai1(Mpegawai pegawai1) {
        return getJadkulmasterDAO().getTjadkulmastersByPegawai1(pegawai1);
    }

    @Override
    public List<Tjadkulmaster> getTjadkulmastersByPegawai2(Mpegawai pegawai2) {
        return getJadkulmasterDAO().getTjadkulmastersByPegawai2(pegawai2);
    }

    @Override
    public List<Tjadkuldetil> getTjadkuldetilsByTjadkulmaster(Tjadkulmaster tjadkulmaster) {
        /** lädt das Object neu */
        getJadkulmasterDAO().refresh(tjadkulmaster);
        getJadkulmasterDAO().initialize(tjadkulmaster);
        /** lädt in diesem Falle den zugehörigen Kunden nach */

        List<Tjadkuldetil> result = getJadkuldetilDAO().getTjadkuldetilsByTjadkulmaster(tjadkulmaster);

        return result;
    }

    @Override
    public int getCountTjadkuldetilsByTjadkulmaster(Tjadkulmaster tjadkulmaster) {
        int result = getJadkuldetilDAO().getCountTjadkuldetilsByTjadkulmaster(tjadkulmaster);
        return result;
    }

    @Override
    public List<Tjadkulmaster> getAllTjadkulmasters() {
        return getJadkulmasterDAO().getAllTjadkulmasters();
    }

    @Override
    public void initialize(Tjadkulmaster proxy) {
        getJadkulmasterDAO().initialize(proxy);

    }

    @Override
    public Tjadkulmaster getTjadkulmasterById(long id) {
        return getJadkulmasterDAO().getTjadkulmasterById(id);
    }

    @Override
    public void saveOrUpdate(Tjadkulmaster tjadkulmaster) {
        getJadkulmasterDAO().saveOrUpdate(tjadkulmaster);
    }

    @Override
    public void delete(Tjadkulmaster tjadkulmaster) {
        getJadkulmasterDAO().delete(tjadkulmaster);
    }

    @Override
    public Tjadkuldetil getNewTjadkuldetil() {
        return getJadkuldetilDAO().getNewTjadkuldetil();
    }

    @Override
    public void saveOrUpdate(Tjadkuldetil tjadkulmasterposition) {
        getJadkuldetilDAO().saveOrUpdate(tjadkulmasterposition);
    }

    @Override
    public void delete(Tjadkuldetil tjadkulmasterposition) {
        getJadkuldetilDAO().delete(tjadkulmasterposition);
    }

    @Override
    public int getCountAllTjadkulmasters() {
        return getJadkulmasterDAO().getCountAllTjadkulmasters();
    }

    @Override
    public int getCountAllTjadkuldetils() {
        return getJadkuldetilDAO().getCountAllTjadkuldetils();
    }
}
