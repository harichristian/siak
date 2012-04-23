package id.ac.idu.kurikulum.service.impl;

import id.ac.idu.administrasi.dao.ProdiDAO;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mdetilkurikulum;
import id.ac.idu.kurikulum.dao.DetilKurikulumDAO;
import id.ac.idu.kurikulum.service.DetilKurikulumService;

import java.util.List;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 3/31/12
 * Time: 9:51 AM
 */
public class DetilKurikulumServiceImpl implements DetilKurikulumService{
    private DetilKurikulumDAO detilKurikulumDAO;
    private ProdiDAO prodiDAO;

    public DetilKurikulumDAO getDetilKurikulumDAO() {
        return detilKurikulumDAO;
    }

    public void setDetilKurikulumDAO(DetilKurikulumDAO detilKurikulumDAO) {
        this.detilKurikulumDAO = detilKurikulumDAO;
    }

    public ProdiDAO getProdiDAO() {
        return prodiDAO;
    }

    public void setProdiDAO(ProdiDAO prodiDAO) {
        this.prodiDAO = prodiDAO;
    }

    @Override
    public Mdetilkurikulum getNew() {
        return getDetilKurikulumDAO().getNew();
    }

    @Override
    public int getCountAll() {
        return getDetilKurikulumDAO().getCountAll();
    }

    @Override
    public Mdetilkurikulum getById(int id) {
        return getDetilKurikulumDAO().getById(id);
    }

    @Override
    public List<Mdetilkurikulum> getAll() {
        return getDetilKurikulumDAO().getAll();
    }

    @Override
    public List<Mdetilkurikulum> getLikeProdiName(String string) {
        return getDetilKurikulumDAO().getLikeProdiName(string);
    }

    @Override
    public List<Mdetilkurikulum> getLikeCohort(String string) {
        return getDetilKurikulumDAO().getLikeCohort(string);
    }

    @Override
    public void saveOrUpdate(Mdetilkurikulum entity) {
        getDetilKurikulumDAO().saveOrUpdate(entity);
    }

    @Override
    public void delete(Mdetilkurikulum entity) {
        getDetilKurikulumDAO().delete(entity);
    }

    @Override
    public ResultObject getAllLikeText(String text, int start, int pageSize) {
        return getDetilKurikulumDAO().getAllLikeText(text, start, pageSize);
    }

    @Override
    public ResultObject getAllLikeMatakuliah(String text, int start, int pageSize) {
        return getDetilKurikulumDAO().getAllLikeMatakuliah(text, start, pageSize);
    }
}
