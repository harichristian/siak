package id.ac.idu.administrasi.service.impl;

import id.ac.idu.administrasi.dao.MpegawaiDAO;
import id.ac.idu.administrasi.service.MpegawaiService;
import id.ac.idu.backend.model.Mpegawai;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/10/12
 * Time: 11:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class MpegawaiServiceImpl  implements MpegawaiService {

   private MpegawaiDAO mpegawaiDAO;

	public MpegawaiDAO getMpegawaiDAO() {
		return mpegawaiDAO;
	}

	public void setMpegawaiDAO(MpegawaiDAO mpegawaiDAO) {
		this.mpegawaiDAO = mpegawaiDAO;
	}

	@Override
	public Mpegawai getNewMpegawai() {
		return getMpegawaiDAO().getNewMpegawai();
	}

	@Override
	public Mpegawai getMpegawaiByID(Long fil_nr) {
		return getMpegawaiDAO().getMpegawaiById(fil_nr);
	}

	@Override
	public List<Mpegawai> getAllMpegawais() {
		return getMpegawaiDAO().getAllMpegawais();
	}

	@Override
	public void saveOrUpdate(Mpegawai mpegawai) {
		getMpegawaiDAO().saveOrUpdate(mpegawai);
	}

	@Override
	public void delete(Mpegawai mpegawai) {
		getMpegawaiDAO().delete(mpegawai);
	}

	@Override
	public List<Mpegawai> getMpegawaisLikeCity(String string) {
		return getMpegawaiDAO().getMpegawaisLikeCity(string);
	}

	@Override
	public List<Mpegawai> getMpegawaisLikeName1(String string) {
		return getMpegawaiDAO().getMpegawaisLikeName1(string);
	}

	@Override
	public List<Mpegawai> getMpegawaisLikeNo(String string) {
		return getMpegawaiDAO().getMpegawaisLikeNo(string);
	}

    @Override
	public int getCountAllMpegawais() {
		return getMpegawaiDAO().getCountAllMpegawais();
	}
}
