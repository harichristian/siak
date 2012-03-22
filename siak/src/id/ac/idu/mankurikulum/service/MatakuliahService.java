package id.ac.idu.mankurikulum.service;

import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mtbmtkl;

import java.util.List;

/**
 * User: hermanto
 * Date: 08 Mar 12
 * Time: 9:45:34
 */

public interface MatakuliahService {
    public Mtbmtkl getNewMatakuliah();

    public Mtbmtkl getMatakuliahById(int id);

    public Mtbmtkl getMatakuliahByCode(String code);

    public List<Mtbmtkl> getAllMatakuliah();

    public int getCountAllMatakuliah();

    public List<Mtbmtkl> getMatakuliahByNama(String nama);

    public List<Mtbmtkl> getMatakuliahByinggris(String inggris);

    public List<Mtbmtkl> getMatakuliahBySingkatan(String singkatan);

    public List<Mtbmtkl> getMatakuliahBySks(Long sks);

    public void saveOrUpdate(Mtbmtkl entity);

    public void delete(Mtbmtkl entity);

    public void save(Mtbmtkl entity);

    public ResultObject getAllMatakuliahLikeText(String text, int start, int pageSize);
}
