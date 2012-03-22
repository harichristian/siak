package id.ac.idu.administrasi.service;

import id.ac.idu.backend.model.Mjabatan;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 06/03/12
 * Time: 0:35
 * To change this template use File | Settings | File Templates.
 */
public interface JabatanService {

    public Mjabatan getNewJabatan();


    public Mjabatan getJabatanByID(int id);

    public Mjabatan getJabatanByName(String name);

    public List<Mjabatan> getAllJabatan();

    public int getCountAllJabatan();

    public void saveOrUpdate(Mjabatan jabatan);

    public void delete(Mjabatan jabatan);
}
