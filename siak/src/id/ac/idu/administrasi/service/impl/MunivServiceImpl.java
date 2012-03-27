/**
 *
 */
package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.MunivDao;
import id.ac.idu.administrasi.service.MunivService;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Muniv;

import java.util.List;

/**
 * @author valeo
 */
public class MunivServiceImpl implements MunivService {
    private MunivDao munivDao;

    public MunivDao getMunivDao() {
        return munivDao;
    }

    public void setMunivDao(MunivDao munivDao) {
        this.munivDao = munivDao;
    }

    /* (non-Javadoc)
      * @see id.ac.idu.backend.service.MunivService#getNewMuniv()
      */
    @Override
    public Muniv getNewMuniv() {
        return getMunivDao().getNewMuniv();
    }

    /* (non-Javadoc)
      * @see id.ac.idu.backend.service.MunivService#getCountAllMunivs()
      */
    @Override
    public int getCountAllMunivs() {
        return getMunivDao().getCountAllMunivs();
    }

    /* (non-Javadoc)
      * @see id.ac.idu.backend.service.MunivService#getMunivByID(java.lang.Long)
      */
    @Override
    public Muniv getMunivByID(Long munivId) {
        return getMunivDao().getMunivById(munivId);
    }

    /* (non-Javadoc)
      * @see id.ac.idu.backend.service.MunivService#getAllMunivs()
      */
    @Override
    public List<Muniv> getAllMunivs() {
        return getMunivDao().getAllMunivs();
    }

    /* (non-Javadoc)
      * @see id.ac.idu.backend.service.MunivService#getMunivsLikeName(java.lang.String)
      */
    @Override
    public List<Muniv> getMunivsLikeName(String string) {
        return getMunivDao().getMunivsLikeName(string);
    }

    /* (non-Javadoc)
      * @see id.ac.idu.backend.service.MunivService#getMunivsLikeName1(java.lang.String)
      */
    @Override
    public List<Muniv> getMunivsLikeName1(String string) {
        return getMunivDao().getMunivsLikeName1(string);
    }

    /* (non-Javadoc)
      * @see id.ac.idu.backend.service.MunivService#getMunivsLikeCode(java.lang.String)
      */
    @Override
    public List<Muniv> getMunivsLikeCode(String string) {
        return getMunivDao().getMunivsLikeCode(string);
    }

    /* (non-Javadoc)
      * @see id.ac.idu.backend.service.MunivService#saveOrUpdate(id.ac.idu.backend.model.Muniv)
      */
    @Override
    public void saveOrUpdate(Muniv muniv) {
        getMunivDao().saveOrUpdate(muniv);
    }

    /* (non-Javadoc)
      * @see id.ac.idu.backend.service.MunivService#delete(id.ac.idu.backend.model.Muniv)
      */
    @Override
    public void delete(Muniv muniv) {
        getMunivDao().delete(muniv);
    }

    @Override
    public ResultObject getAllUnivLikeText(String text, int start, int pageSize) {
        return getMunivDao().getAllUnivLikeText(text, start, pageSize);
    }
}
