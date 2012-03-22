package id.ac.idu.administrasi.dao;

import id.ac.idu.backend.model.Mpegawai;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: siak
 * Date: 3/9/12
 * Time: 4:37 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MpegawaiDAO {
     public Mpegawai getNewMpegawai();


    public Mpegawai getMpegawaiById(Long filId);

	/**
	 * EN: Get an Mpegawai object by its ID.<br>
	 * DE: Gibt ein Mpegawai Objekt anhand seiner ID zurueck.<br>
	 *
	 * @param fil_nr
	 *            / the mpegawai number / die Niederlassungs Nummer
	 * @return Mpegawai / Mpegawai
	 */
	public Mpegawai getMpegawaiByFilNr(String fil_nr);

	/**
	 * EN: Get a list of all Mpegawais.<br>
	 * DE: Gibt eine Liste aller Mpegawais zurueck.<br>
	 *
	 * @return List of Mpegawais / Liste von Mpegawais
	 */
	public List<Mpegawai> getAllMpegawais();

	/**
	 * EN: Get the count of all Mpegawais.<br>
	 * DE: Gibt die Anzahl aller Mpegawais zurueck.<br>
	 *
	 * @return int
	 */
	public int getCountAllMpegawais();

	/**
	 * EN: Gets a list of Mpegawais where the city name contains the %string% .<br>
	 * DE: Gibt eine Liste aller Mpegawais zurueck bei denen der Stadtname
	 * %string% enthaelt.<br>
	 *
	 * @param string
	 *            Name of the city / Stadtnamen
	 * @return List of Mpegawais / Liste of Mpegawais
	 */
	public List<Mpegawai> getMpegawaisLikeCity(String string);

	/**
	 * EN: Gets a list of Mpegawais where the mpegawai name1 contains the %string% .<br>
	 * DE: Gibt eine Liste aller Mpegawais zurueck bei denen der Mpegawai Name1
	 * %string% enthaelt.<br>
	 *
	 * @param string
	 *            Name1 of the mpegawai / Name1 vom Mpegawai
	 * @return List of Mpegawais / Liste of Mpegawais
	 */
	public List<Mpegawai> getMpegawaisLikeName1(String string);

	/**
	 * EN: Gets a list of Mpegawais where the mpegawai number contains the %string%
	 * .<br>
	 * DE: Gibt eine Liste aller Mpegawais zurueck bei denen die Mpegawai Nummer
	 * %string% enthaelt.<br>
	 *
	 * @param string
	 *            Number of the mpegawai / Nummer vom Mpegawai
	 * @return List of Mpegawais / Liste of Mpegawais
	 */
	public List<Mpegawai> getMpegawaisLikeNo(String string);

	/**
	 * EN: Deletes an Mpegawai by its Id.<br>
	 * DE: Loescht ein Mpegawai anhand seiner Id.<br>
	 *
	 * @param id
	 *            / the persistence identifier / der PrimaerKey
	 */
	public void deleteMpegawaiById(long id);

	/**
	 * EN: Saves new or updates an Mpegawai.<br>
	 * DE: Speichert neu oder aktualisiert ein Mpegawai.<br>
	 */
	public void saveOrUpdate(Mpegawai entity);

	/**
	 * EN: Deletes an Mpegawai.<br>
	 * DE: Loescht ein Mpegawai.<br>
	 */
	public void delete(Mpegawai entity);

	/**
	 * EN: Saves an Mpegawai.<br>
	 * DE: Speichert ein Mpegawai.<br>
	 */
	public void save(Mpegawai entity);
}
