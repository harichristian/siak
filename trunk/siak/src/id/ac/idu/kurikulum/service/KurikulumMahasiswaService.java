package id.ac.idu.kurikulum.service;

import id.ac.idu.backend.model.Mkurmhs;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/9/12
 * Time: 3:00 PM
 * To change this template use File | Settings | File Templates.
 */
public interface KurikulumMahasiswaService {
    /**
	 * EN: Get a new Mkurmhs object.<br>
	 *
	 * @return Mkurmhs
	 */
	public Mkurmhs getNewKurikulumMahasiswa();

	/**
	 * EN: Get the count of all Mkurmhs.<br>
	 *
	 * @return int
	 */
	public int getCountAllKurikulumMahasiswas();

	/**
	 * EN: Get an Mkurmhs by its ID.<br>
	 *
	 * @param id
	 *            / the persistence identifier
	 * @return Mkurmhs
	 */
	public Mkurmhs getKurikulumMahasiswaById(int id);

	/**
	 * EN: Get a list of all Mkurmhs.<br>
	 *
	 * @return List of Mkurmhs
	 */
	public List<Mkurmhs> getAllKurikulumMahasiswas();

	/**
	 * EN: Gets a list of Mkurmhs where the Mkurmhs prodi contains the %string% .<br>
	 *
	 * @param string
	 *            name of the prodi
	 * @return List of Mkurikulum
	 */
	public List<Mkurmhs> getKurikulumMahasiswasLikeProdi(String string);

	/**
	 * EN: Gets a list of Mkurmhs where the Mkurmhs cohort contains the %string% .<br>
	 *
	 * @param string
	 *            name of the Cohort
	 * @return List of Mkurmhs
	 */
	public List<Mkurmhs> getKurikulumMahasiswasLikeCohort(String string);

	/**
	 * EN: Saves new or updates an Mkurmhs.<br>
	 */
	public void saveOrUpdate(Mkurmhs entity);

	/**
	 * EN: Deletes an Mkurikulum.<br>
	 */
	public void delete(Mkurmhs entity);
}
