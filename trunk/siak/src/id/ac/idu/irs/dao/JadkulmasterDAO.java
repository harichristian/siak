package id.ac.idu.irs.dao;

import id.ac.idu.backend.model.Mpegawai;
import id.ac.idu.backend.model.Mprodi;
import id.ac.idu.backend.model.Msekolah;
import id.ac.idu.backend.model.Tjadkulmaster;

import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 10/03/12
 * Time: 17:06
 * To change this template use File | Settings | File Templates.
 */
public interface JadkulmasterDAO {

    public Tjadkulmaster getNewTjadkulmaster();

    public List<Tjadkulmaster> getTjadkulmastersForReport(Tjadkulmaster param);

    public List<Tjadkulmaster> getTjadkulmastersBySekolahAndProdiAndPegawai1(Msekolah sekolah, Mprodi prodi, Mpegawai pegawai1);

    public List<Tjadkulmaster> getTjadkulmastersBySekolahAndProdi(Msekolah sekolah, Mprodi prodi);

    public List<Tjadkulmaster> getTjadkulmastersBySekolah(Msekolah sekolah);

    public List<Tjadkulmaster> getTjadkulmastersByProdi(Mprodi prodi);

    public List<Tjadkulmaster> getTjadkulmastersByPegawai1(Mpegawai pegawai1);

    public List<Tjadkulmaster> getTjadkulmastersByPegawai2(Mpegawai pegawai2);

    public int getCountAllTjadkulmasters();

    public Tjadkulmaster getTjadkulmasterById(long id);

    public List<Tjadkulmaster> getAllTjadkulmasters();

    public void refresh(Tjadkulmaster tjadkulmaster);

    public void initialize(Tjadkulmaster tjadkulmaster);

    public void saveOrUpdate(Tjadkulmaster entity);

    public void delete(Tjadkulmaster entity);

    public void save(Tjadkulmaster entity);
}
