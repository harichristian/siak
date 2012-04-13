package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.BidangUsahaDAO;
import id.ac.idu.administrasi.service.BidangUsahaService;
import id.ac.idu.backend.bean.ResultObject;
import id.ac.idu.backend.model.Mbidangusaha;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 4/13/12
 * Time: 8:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class BidangUsahaServiceImpl implements BidangUsahaService {
    private BidangUsahaDAO bidangUsahaDAO;


    public BidangUsahaDAO getBidangUsahaDAO() {
        return bidangUsahaDAO;
    }

    public void setBidangUsahaDAO(BidangUsahaDAO bidangUsahaDAO) {
        this.bidangUsahaDAO = bidangUsahaDAO;
    }

    @Override
    public Mbidangusaha getNewBidangusaha() {
        return getBidangUsahaDAO().getNewBidangusaha();
    }

    @Override
    public Mbidangusaha getBidangusahaByID(int id) {
        return getBidangUsahaDAO().getBidangusahaById(id);
    }

    @Override
    public Mbidangusaha getBidangusahaByName(String name) {
        return getBidangUsahaDAO().getBidangusahaByName(name);
    }

    @Override
    public List<Mbidangusaha> getAllBidangusaha() {
        return getBidangUsahaDAO().getAllBidangusaha();
    }

    @Override
    public int getCountAllBidangusaha() {
        return getBidangUsahaDAO().getCountAllBidangusaha();
    }

    @Override
    public void saveOrUpdate(Mbidangusaha bidangusaha) {
        getBidangUsahaDAO().saveOrUpdate(bidangusaha);
    }

    @Override
    public void delete(Mbidangusaha bidangusaha) {
        getBidangUsahaDAO().delete(bidangusaha);
    }

     @Override
    public ResultObject getAllBidangusahaLikeText(String text, int start, int pageSize) {
        return getBidangUsahaDAO().getAllBidangusahaLikeText(text, start, pageSize);
    }


}