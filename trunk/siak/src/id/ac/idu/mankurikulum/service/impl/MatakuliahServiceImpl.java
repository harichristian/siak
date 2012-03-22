package id.ac.idu.mankurikulum.service.impl;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mtbmtkl;
import id.ac.idu.mankurikulum.dao.MatakuliahDAO;
import id.ac.idu.mankurikulum.service.MatakuliahService;

import java.util.List;

/**
 * User: hermanto
 * Date: 08 Mar 12
 * Time: 9:45:06
 */

public class MatakuliahServiceImpl implements MatakuliahService {
    private MatakuliahDAO matakuliahDAO;

    public MatakuliahDAO getMatakuliahDAO() {
        return matakuliahDAO;
    }

    public void setMatakuliahDAO(MatakuliahDAO matakuliahDAO) {
        this.matakuliahDAO = matakuliahDAO;
    }

    @Override
    public Mtbmtkl getNewMatakuliah() {
        return this.getMatakuliahDAO().getNewMatakuliah();
    }

    @Override
    public Mtbmtkl getMatakuliahById(int id) {
        return this.getMatakuliahDAO().getMatakuliahById(id);
    }

    @Override
    public Mtbmtkl getMatakuliahByCode(String code) {
        return this.getMatakuliahDAO().getMatakuliahByCode(code);
    }

    @Override
    public List<Mtbmtkl> getAllMatakuliah() {
        return this.getMatakuliahDAO().getAllMatakuliah();
    }

    @Override
    public int getCountAllMatakuliah() {
        return this.getMatakuliahDAO().getCountAllMatakuliah();
    }

    @Override
    public List<Mtbmtkl> getMatakuliahByNama(String nama) {
        return this.getMatakuliahDAO().getMatakuliahByNama(nama);
    }

    @Override
    public List<Mtbmtkl> getMatakuliahByinggris(String inggris) {
        return this.getMatakuliahDAO().getMatakuliahByinggris(inggris);
    }

    @Override
    public List<Mtbmtkl> getMatakuliahBySingkatan(String singkatan) {
        return this.getMatakuliahDAO().getMatakuliahBySingkatan(singkatan);
    }

    @Override
    public List<Mtbmtkl> getMatakuliahBySks(Long sks) {
        return this.getMatakuliahDAO().getMatakuliahBySks(sks);
    }

    @Override
    public void saveOrUpdate(Mtbmtkl entity) {
        this.getMatakuliahDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Mtbmtkl entity) {
        this.getMatakuliahDAO().delete(entity);
    }

    @Override
    public void save(Mtbmtkl entity) {
        this.getMatakuliahDAO().save(entity);
    }

    @Override
    public ResultObject getAllMatakuliahLikeText(String text, int start, int pageSize) {
        return getMatakuliahDAO().getAllMatakuliahLikeText(text, start, pageSize);
    }
}
