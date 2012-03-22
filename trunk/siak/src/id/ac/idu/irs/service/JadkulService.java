package id.ac.idu.irs.service;

import id.ac.idu.backend.model.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 11/03/12
 * Time: 4:07
 * To change this template use File | Settings | File Templates.
 */
public interface JadkulService {

    // ########################################################## //
    // ################### Jadkulmaster ################### //
    // ########################################################## //

    public Tjadkulmaster getNewTjadkulmaster();

    public List<Tjadkulmaster> getTjadkulmastersBySekolahAndProdiAndPegawai1(Msekolah sekolah, Mprodi prodi, Mpegawai pegawai1);

    public List<Tjadkulmaster> getTjadkulmastersBySekolahAndProdi(Msekolah sekolah, Mprodi prodi);

    public List<Tjadkulmaster> getTjadkulmastersBySekolah(Msekolah sekolah);

    public List<Tjadkulmaster> getTjadkulmastersByProdi(Mprodi prodi);

    public List<Tjadkulmaster> getTjadkulmastersByPegawai1(Mpegawai pegawai1);

    public List<Tjadkulmaster> getTjadkulmastersByPegawai2(Mpegawai pegawai2);

    public int getCountAllTjadkulmasters();

    public int getCountAllTjadkuldetils();

    public List<Tjadkulmaster> getAllTjadkulmasters();

    public void initialize(Tjadkulmaster proxy);

    public Tjadkulmaster getTjadkulmasterById(long id);

    public void saveOrUpdate(Tjadkulmaster tjadkulmaster);

    public void delete(Tjadkulmaster tjadkulmaster);

    // ########################################################## //
    // ########### Jadkuldetil ########### //
    // ########################################################## //
    //

    public List<Tjadkuldetil> getTjadkuldetilsByTjadkulmaster(Tjadkulmaster tjadkulmaster);

    public int getCountTjadkuldetilsByTjadkulmaster(Tjadkulmaster tjadkulmaster);

    public Tjadkuldetil getNewTjadkuldetil();

    public void saveOrUpdate(Tjadkuldetil tjadkuldetil);

    public void delete(Tjadkuldetil tjadkuldetil);
}
    