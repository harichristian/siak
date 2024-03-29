package id.ac.idu.backend.model;
// Generated 10 Mar 12 12:50:36 by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Tjadkulmaster generated by hbm2java
 */
public class Tjadkulmaster implements java.io.Serializable {

    private int id = Integer.MIN_VALUE;
    private String cterm;
    private Msekolah msekolah;
    private Mprodi mprodi;
    private Mtbmtkl mtbmtkl;
    private String ckelompok;
    private String cthajar;
    private String csmt;
    private Mpegawai mpegawai1;
    private Mpegawai mpegawai2;
    private Integer nsks;
    private String cket;
    private String clintasprodi;
    private Date dcreateddate;
    private String ccreatedby;
    private Date dupdateddate;
    private String cupdatedby;
    private Set<Tjadkuldetil> tjadkuldetils = new HashSet<Tjadkuldetil>(0);

    public boolean isNew() {
        return (getId() == Integer.MIN_VALUE);
    }

    public Msekolah getMsekolah() {
        return msekolah;
    }

    public void setMsekolah(Msekolah msekolah) {
        this.msekolah = msekolah;
    }

    public Mprodi getMprodi() {
        return mprodi;
    }

    public void setMprodi(Mprodi mprodi) {
        this.mprodi = mprodi;
    }

    public Mtbmtkl getMtbmtkl() {
        return mtbmtkl;
    }

    public void setMtbmtkl(Mtbmtkl mtbmtkl) {
        this.mtbmtkl = mtbmtkl;
    }

    public Mpegawai getMpegawai1() {
        return mpegawai1;
    }

    public void setMpegawai1(Mpegawai mpegawai1) {
        this.mpegawai1 = mpegawai1;
    }

    public Mpegawai getMpegawai2() {
        return mpegawai2;
    }

    public void setMpegawai2(Mpegawai mpegawai2) {
        this.mpegawai2 = mpegawai2;
    }

    public Set<Tjadkuldetil> getTjadkuldetils() {
        return tjadkuldetils;
    }

    public void setTjadkuldetils(Set<Tjadkuldetil> tjadkuldetils) {
        this.tjadkuldetils = tjadkuldetils;
    }

    public Tjadkulmaster() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCterm() {
        return this.cterm;
    }

    public void setCterm(String cterm) {
        this.cterm = cterm;
    }

    public String getCkelompok() {
        return this.ckelompok;
    }

    public void setCkelompok(String ckelompok) {
        this.ckelompok = ckelompok;
    }

    public String getCthajar() {
        return this.cthajar;
    }

    public void setCthajar(String cthajar) {
        this.cthajar = cthajar;
    }

    public String getCsmt() {
        return this.csmt;
    }

    public void setCsmt(String csmt) {
        this.csmt = csmt;
    }

    public Integer getNsks() {
        return this.nsks;
    }

    public void setNsks(Integer nsks) {
        this.nsks = nsks;
    }

    public String getCket() {
        return this.cket;
    }

    public void setCket(String cket) {
        this.cket = cket;
    }

    public String getClintasprodi() {
        return this.clintasprodi;
    }

    public void setClintasprodi(String clintasprodi) {
        this.clintasprodi = clintasprodi;
    }

    public Date getDcreateddate() {
        return this.dcreateddate;
    }

    public void setDcreateddate(Date dcreateddate) {
        this.dcreateddate = dcreateddate;
    }

    public String getCcreatedby() {
        return this.ccreatedby;
    }

    public void setCcreatedby(String ccreatedby) {
        this.ccreatedby = ccreatedby;
    }

    public Date getDupdateddate() {
        return this.dupdateddate;
    }

    public void setDupdateddate(Date dupdateddate) {
        this.dupdateddate = dupdateddate;
    }

    public String getCupdatedby() {
        return this.cupdatedby;
    }

    public void setCupdatedby(String cupdatedby) {
        this.cupdatedby = cupdatedby;
    }


}


