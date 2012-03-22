package id.ac.idu.irs.dao;

import id.ac.idu.backend.model.Tjadkulmaster;
import id.ac.idu.backend.model.Tjadkuldetil;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 10/03/12
 * Time: 17:06
 * To change this template use File | Settings | File Templates.
 */
public interface JadkuldetilDAO {
    public Tjadkuldetil getNewTjadkuldetil();

    public List<Tjadkuldetil> getTjadkuldetilsByTjadkulmaster(Tjadkulmaster tjadkulmaster);

    public int getCountTjadkuldetilsByTjadkulmaster(Tjadkulmaster tjadkulmaster);

    public int getCountAllTjadkuldetils();

    public Tjadkuldetil getTjadkuldetilById(long id);

    public void deleteTjadkuldetilsByTjadkulmaster(Tjadkulmaster tjadkulmaster);

    public void saveOrUpdate(Tjadkuldetil entity);

    public void delete(Tjadkuldetil entity);

    public void save(Tjadkuldetil entity);
}
