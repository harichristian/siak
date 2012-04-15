package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.JabatanDAO;
import id.ac.idu.administrasi.service.JabatanService;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mjabatan;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 06/03/12
 * Time: 0:39
 * To change this template use File | Settings | File Templates.
 */
public class JabatanServiceImpl implements JabatanService {
    private JabatanDAO jabatanDAO;

    public JabatanDAO getJabatanDAO() {
        return jabatanDAO;
    }

    public void setJabatanDAO(JabatanDAO jabatanDAO) {
        this.jabatanDAO = jabatanDAO;
    }

    @Override
    public Mjabatan getNewJabatan() {
        return getJabatanDAO().getNewJabatan();
    }

    @Override
    public Mjabatan getJabatanByID(int id) {
        return getJabatanDAO().getJabatanById(id);
    }

    @Override
    public Mjabatan getJabatanByName(String name) {
        return getJabatanDAO().getJabatanByName(name);
    }

    @Override
    public List<Mjabatan> getAllJabatan() {
        return getJabatanDAO().getAllJabatan();
    }

    @Override
    public int getCountAllJabatan() {
        return getJabatanDAO().getCountAllJabatan();
    }

    @Override
    public void saveOrUpdate(Mjabatan jabatan) {
        getJabatanDAO().saveOrUpdate(jabatan);
    }

    @Override
    public void delete(Mjabatan jabatan) {
        getJabatanDAO().delete(jabatan);
    }

     @Override
    public ResultObject getAllJabatanLikeText(String text, int start, int pageSize) {
        return getJabatanDAO().getAllJabatanLikeText(text, start, pageSize);
    }

    @Override
    public Mjabatan getJabatanByCode(String code) {
        return getJabatanDAO().getJabatanByCode(code);
    }
}
