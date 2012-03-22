package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.CalendarAkademikDAO;
import id.ac.idu.administrasi.service.CalendarAkademikService;
import id.ac.idu.backend.model.Mcalakademik;
import id.ac.idu.backend.model.Mkegiatan;
import id.ac.idu.backend.model.Mprodi;
import id.ac.idu.backend.model.Msekolah;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 09/03/12
 * Time: 0:42
 * To change this template use File | Settings | File Templates.
 */
public class CalendarAkademikServiceImpl implements CalendarAkademikService {
    private CalendarAkademikDAO calendarAkademikDAO;

    public CalendarAkademikDAO getCalendarAkademikDAO() {
        return calendarAkademikDAO;
    }

    public void setCalendarAkademikDAO(CalendarAkademikDAO calendarAkademikDAO) {
        this.calendarAkademikDAO = calendarAkademikDAO;
    }

    @Override
    public Mcalakademik getNewCalakademik() {
        return getCalendarAkademikDAO().getNewCalakademik();
    }

    @Override
    public Mcalakademik getCalakademikById(int id) {
        return getCalendarAkademikDAO().getCalakademikById(id);
    }

    @Override
    public Mcalakademik getCalakademikByNo(String no) {
        return getCalendarAkademikDAO().getCalakademikByNo(no);
    }

    @Override
    public Mcalakademik getCalakademikByTahunAjaran(String tahunAjaran) {
        return getCalendarAkademikDAO().getCalakademikByTahunAjaran(tahunAjaran);
    }

    @Override
    public Mcalakademik getCalakademikByTerm(String term) {
        return getCalendarAkademikDAO().getCalakademikByTerm(term);
    }

    @Override
    public List<Mcalakademik> getCalakademikByProdi(Mprodi prodi) {
        return getCalendarAkademikDAO().getCalakademikByProdi(prodi);
    }

    @Override
    public List<Mcalakademik> getCalakademikBySekolah(Msekolah sekolah) {
        return getCalendarAkademikDAO().getCalakademikBySekolah(sekolah);
    }

    @Override
    public List<Mcalakademik> getCalakademikByKegiatan(Mkegiatan kegiatan) {
        return getCalendarAkademikDAO().getCalakademikByKegiatan(kegiatan);
    }

    @Override
    public List<Mcalakademik> getCalakademikBySekolahAndProdi(Msekolah sekolah, Mprodi prodi) {
        return getCalendarAkademikDAO().getCalakademikBySekolahAndProdi(sekolah, prodi);
    }

    @Override
    public List<Mcalakademik> getAllCalakademik() {
        return getCalendarAkademikDAO().getAllCalakademik();
    }

    @Override
    public int getCountAllCalakademik() {
        return getCalendarAkademikDAO().getCountAllCalakademik();
    }

    @Override
    public void saveOrUpdate(Mcalakademik entity) {
        getCalendarAkademikDAO().save(entity);
    }

    @Override
    public void delete(Mcalakademik entity) {
        getCalendarAkademikDAO().delete(entity);
    }

    @Override
    public void save(Mcalakademik entity) {
        getCalendarAkademikDAO().save(entity);
    }
}
