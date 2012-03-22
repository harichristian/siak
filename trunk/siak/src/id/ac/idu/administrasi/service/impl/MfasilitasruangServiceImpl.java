package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.MfasilitasruangDAO;
import id.ac.idu.administrasi.service.MfasilitasruangService;
import id.ac.idu.backend.model.Mfasilitasruang;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/9/12
 * Time: 11:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class MfasilitasruangServiceImpl implements MfasilitasruangService {

	private MfasilitasruangDAO mfasilitasruangDAO;

	public MfasilitasruangDAO getMfasilitasruangDAO() {
		return mfasilitasruangDAO;
	}

	public void setMfasilitasruangDAO(MfasilitasruangDAO mfasilitasruangDAO) {
		this.mfasilitasruangDAO = mfasilitasruangDAO;
	}

	@Override
	public Mfasilitasruang getNewMfasilitasruang() {
		return getMfasilitasruangDAO().getNewMfasilitasruang();
	}

	@Override
	public Mfasilitasruang getMfasilitasruangByID(Long fil_nr) {
		return getMfasilitasruangDAO().getMfasilitasruangById(fil_nr);
	}

	@Override
	public List<Mfasilitasruang> getAllMfasilitasruangs() {
		return getMfasilitasruangDAO().getAllMfasilitasruangs();
	}

	@Override
	public void saveOrUpdate(Mfasilitasruang mfasilitasruang) {
		getMfasilitasruangDAO().saveOrUpdate(mfasilitasruang);
	}

	@Override
	public void delete(Mfasilitasruang mfasilitasruang) {
		getMfasilitasruangDAO().delete(mfasilitasruang);
	}

	@Override
	public List<Mfasilitasruang> getMfasilitasruangsLikeCity(String string) {
		return getMfasilitasruangDAO().getMfasilitasruangsLikeCity(string);
	}

	@Override
	public List<Mfasilitasruang> getMfasilitasruangsLikeName1(String string) {
		return getMfasilitasruangDAO().getMfasilitasruangsLikeName1(string);
	}

	@Override
	public List<Mfasilitasruang> getMfasilitasruangsLikeNo(String string) {
		return getMfasilitasruangDAO().getMfasilitasruangsLikeNo(string);
	}

	@Override
	public int getCountAllMfasilitasruangs() {
		return getMfasilitasruangDAO().getCountAllMfasilitasruangs();
	}

}
