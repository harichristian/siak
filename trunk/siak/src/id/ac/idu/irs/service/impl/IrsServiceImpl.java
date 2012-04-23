package id.ac.idu.irs.service.impl;

import id.ac.idu.administrasi.dao.MahasiswaDAO;
import id.ac.idu.administrasi.dao.ProdiDAO;
import id.ac.idu.administrasi.dao.SekolahDAO;
import id.ac.idu.backend.model.*;
import id.ac.idu.irs.dao.IrsDAO;
import id.ac.idu.irs.dao.JadkuldetilDAO;
import id.ac.idu.irs.dao.JadkulmasterDAO;
import id.ac.idu.irs.dao.PaketDAO;
import id.ac.idu.irs.service.IrsService;
import id.ac.idu.mankurikulum.dao.MatakuliahDAO;
import id.ac.idu.util.Codec;

import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/11/12
 * Time: 5:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class IrsServiceImpl implements IrsService{
    private IrsDAO irsDAO;
    private MahasiswaDAO mahasiswaDAO;
    private SekolahDAO sekolahDAO;
    private ProdiDAO prodiDAO;
    private MatakuliahDAO matakuliahDAO;
    private PaketDAO paketDAO;
    private JadkulmasterDAO jadkulmasterDAO;
    private JadkuldetilDAO jadkuldetilDAO;

    public IrsDAO getIrsDAO() {
        return irsDAO;
    }

    public void setIrsDAO(IrsDAO irsDAO) {
        this.irsDAO = irsDAO;
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

    public MatakuliahDAO getMatakuliahDAO() {
        return matakuliahDAO;
    }

    public void setMatakuliahDAO(MatakuliahDAO matakuliahDAO) {
        this.matakuliahDAO = matakuliahDAO;
    }

    public PaketDAO getPaketDAO() {
        return paketDAO;
    }

    public void setPaketDAO(PaketDAO paketDAO) {
        this.paketDAO = paketDAO;
    }

    public JadkulmasterDAO getJadkulmasterDAO() {
        return jadkulmasterDAO;
    }

    public void setJadkulmasterDAO(JadkulmasterDAO jadkulmasterDAO) {
        this.jadkulmasterDAO = jadkulmasterDAO;
    }

    public JadkuldetilDAO getJadkuldetilDAO() {
        return jadkuldetilDAO;
    }

    public void setJadkuldetilDAO(JadkuldetilDAO jadkuldetilDAO) {
        this.jadkuldetilDAO = jadkuldetilDAO;
    }
    @Override
    public Tirspasca getNewIrs() {
        return getIrsDAO().getNewIrs();
    }

    @Override
    public int getCountAll() {
        return getIrsDAO().getCountAll();
    }

    @Override
    public Tirspasca getIrsById(int id) {
        return getIrsDAO().getIrsById(id);
    }

    @Override
    public List<Tirspasca> getAll() {
        return getIrsDAO().getAll();
    }

    @Override
    public List<Tirspasca> getIrsLikeTerm(String string) {
        return getIrsDAO().getIrsLikeTerm(string);
    }

    @Override
    public List<Tirspasca> getIrsLikeKelompok(String string) {
        return getIrsDAO().getIrsLikeKelompok(string);
    }

    @Override
    public List<Tirspasca> getIrsLikeMahasiswaName(String string) {
        return getIrsLikeMahasiswaName(string);
    }

    @Override
    public void saveOrUpdate(Tirspasca entity) {
        getIrsDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Tirspasca entity) {
        getIrsDAO().delete(entity);
    }

    @Override
    public void deleteList(Set<Tirspasca> list) {
        for(Tirspasca entity : list) {
            if(entity.getId() != 0) getIrsDAO().delete(entity);
        }
    }

    @Override
    public void saveOrUpdateList(Set<Tirspasca> list) {
        for(Tirspasca entity : list) {
            getIrsDAO().saveOrUpdate(entity);
        }
    }

    @Override
    public void saveOrUpdatePaket(Tirspasca entity, Mmahasiswa mahasiswa) {
        List<Tpaketkuliah> paketList = getPaketDAO().getPaketForTransaction(entity.getMprodi(), entity.getCterm(), entity.getCthajar());
        String error = "";
        List<Tirspasca> irsList;
        //get mahasiswa list;
        List<Mmahasiswa> mahasiswaList = getMahasiswaDAO().getForPaket(entity.getMmahasiswa(), mahasiswa);
        if(mahasiswaList.size() > 0) {
            for(Mmahasiswa mhs : mahasiswaList){
                if(paketList.size() > 0) {
                    if(mhs.getMstatusmhs().getCkdstatmhs().equals(Codec.StatusMahasiswa.Status1.getValue())) {
                        for(Tpaketkuliah paket : paketList) {
                            Tirspasca irs = getNewIrs();
                            //set mahasiswa
                            irs.setMmahasiswa(mhs);
                            irs.setMprodi(entity.getMprodi());
                            irs.setMsekolah(entity.getMsekolah());
                            irs.setCterm(entity.getCterm());
                            irs.setCthajar(entity.getCthajar());
                            irs.setCsmt(entity.getCsmt());
                            //cek tjadkulmaster term, ckdsekolah, ckdprogst
                            List<Tjadkuldetil> jadkulDetil = getJadkuldetilDAO().getForPaket(entity.getMsekolah(), entity.getMprodi(), entity.getCterm(), paket.getMtbmtkl());
                            if (jadkulDetil.size() > 0) {
                                //cek bentrok kelompok
                                //cek nmaks, nisi tjadkuldetil
                                for(Tjadkuldetil detil : jadkulDetil) {
                                    int tempIsi = (detil.getNisi() == null)?0:detil.getNisi();
                                    if(tempIsi < detil.getNmaks()) {
                                        //update tjadkul nisi = nisi + 1
                                        int isi = tempIsi + 1;
                                        detil.setNisi(isi);
                                        getJadkuldetilDAO().saveOrUpdate(detil);
                                        //save tirspasca
                                        irs.setCkelompok(detil.getCkelompok());
                                        irs.setMtbmtkl(detil.getMtbmtkl());
                                        irs.setNsks(detil.getMtbmtkl().getNsks());
                                        getIrsDAO().saveOrUpdate(irs);
                                    } else {
                                        error += "\n NIM: "+mhs.getCnim()+" Matakuliah: "+detil.getMtbmtkl().getCnamamk()+" Kelompok: "+detil.getCkelompok()+" penuh.";
                                    }
                                }
                            } else {
                                error += "\n NIM: "+mhs.getCnim()+" " + paket.getMtbmtkl().getCnamamk() + " tidak ada jadwal. ";
                            }
                        }
                    } else {
                        error += "\n NIM: "+mhs.getCnim()+" tidak aktif.";
                    }
                } else {
                    error += "\n Tidak ada paket untuk prodi, term dan tahun ajaran yang dipilih";
                }
            }
        } else {
            error = "Daftar mahasiswa tidak ditemukan";
        }
        if(!error.isEmpty()){
            getIrsDAO().throwError(error);
        }
    }
}
