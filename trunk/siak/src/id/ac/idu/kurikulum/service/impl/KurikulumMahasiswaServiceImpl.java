package id.ac.idu.kurikulum.service.impl;

import id.ac.idu.administrasi.dao.MahasiswaDAO;
import id.ac.idu.administrasi.dao.ProdiDAO;
import id.ac.idu.backend.model.Mkurmhs;
import id.ac.idu.kurikulum.dao.KurikulumDAO;
import id.ac.idu.kurikulum.dao.KurikulumMahasiswaDAO;
import id.ac.idu.kurikulum.service.KurikulumMahasiswaService;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/9/12
 * Time: 3:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class KurikulumMahasiswaServiceImpl implements KurikulumMahasiswaService{
    private KurikulumMahasiswaDAO kurikulumMahasiswaDAO;
    private MahasiswaDAO mahasiswaDAO;
    private KurikulumDAO kurikulumDAO;
    private ProdiDAO prodiDAO;

    public KurikulumMahasiswaDAO getKurikulumMahasiswaDAO() {
		return kurikulumMahasiswaDAO;
	}

	public void setKurikulumMahasiswaDAO(KurikulumMahasiswaDAO kurikulumMahasiswaDAO) {
		this.kurikulumMahasiswaDAO = kurikulumMahasiswaDAO;
	}

    public MahasiswaDAO getMahasiswaDAO() {
		return mahasiswaDAO;
	}

	public void setMahasiswaDAO(MahasiswaDAO mahasiswaDAO) {
		this.mahasiswaDAO = mahasiswaDAO;
	}

    public KurikulumDAO getKurikulumDAO() {
		return kurikulumDAO;
	}

	public void setKurikulumDAO(KurikulumDAO kurikulumDAO) {
		this.kurikulumDAO = kurikulumDAO;
	}

    public ProdiDAO getProdiDAO() {
		return prodiDAO;
	}

	public void setProdiDAO(ProdiDAO prodiDAO) {
		this.prodiDAO = prodiDAO;
	}

    @Override
    public Mkurmhs getNewKurikulumMahasiswa() {
        return getKurikulumMahasiswaDAO().getNewKurikulumMahasiswa();
    }

    @Override
    public int getCountAllKurikulumMahasiswas() {
        return getKurikulumMahasiswaDAO().getCountAllKurikulumMahasiswas();
    }

    @Override
    public Mkurmhs getKurikulumMahasiswaById(int id) {
        return getKurikulumMahasiswaDAO().getKurikulumMahasiswaById(id);
    }

    @Override
    public List<Mkurmhs> getAllKurikulumMahasiswas() {
        return getKurikulumMahasiswaDAO().getAllKurikulumMahasiswas();
    }

    @Override
    public List<Mkurmhs> getKurikulumMahasiswasLikeProdi(String string) {
        return getKurikulumMahasiswaDAO().getKurikulumMahasiswaLikeProdi(string);
    }

    @Override
    public List<Mkurmhs> getKurikulumMahasiswasLikeCohort(String string) {
        return getKurikulumMahasiswaDAO().getKurikulumMahasiswaLikeCohort(string);
    }

    @Override
    public void saveOrUpdate(Mkurmhs entity) {
        getKurikulumMahasiswaDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Mkurmhs entity) {
        getKurikulumMahasiswaDAO().saveOrUpdate(entity);
    }
}
