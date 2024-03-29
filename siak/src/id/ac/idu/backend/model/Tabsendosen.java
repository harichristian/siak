package id.ac.idu.backend.model;
// Generated 11 Mar 12 23:21:10 by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * Tabsendosen generated by hbm2java
 */
public class Tabsendosen  implements java.io.Serializable {


     private int id;
     private Mruang mruang;
     private Msekolah msekolah;
     private Mpegawai mpegawai;
     private Mprodi mprodi;
     private Mtbmtkl mtbmtkl;
     private Msesikuliah msesikuliah;
     private Mhari mhari;
     private String cterm;
     private String ckelompok;
     private Date dtglkul;
     private String cket;
     private String cthajar;
     private String csmt;
     private String cmateri;
     private Date dcreateddate;
     private String ccreatedby;
     private Date dupdateddate;
     private String cupdatedby;

    public Tabsendosen() {
    }

	
    public Tabsendosen(int id, Mruang mruang, Msekolah msekolah, Mpegawai mpegawai, Mprodi mprodi, Mtbmtkl mtbmtkl, Msesikuliah msesikuliah, Mhari mhari, String cterm, String ckelompok, Date dtglkul) {
        this.id = id;
        this.mruang = mruang;
        this.msekolah = msekolah;
        this.mpegawai = mpegawai;
        this.mprodi = mprodi;
        this.mtbmtkl = mtbmtkl;
        this.msesikuliah = msesikuliah;
        this.mhari = mhari;
        this.cterm = cterm;
        this.ckelompok = ckelompok;
        this.dtglkul = dtglkul;
    }
    public Tabsendosen(int id, Mruang mruang, Msekolah msekolah, Mpegawai mpegawai, Mprodi mprodi, Mtbmtkl mtbmtkl, Msesikuliah msesikuliah, Mhari mhari, String cterm, String ckelompok, Date dtglkul, String cket, String cthajar, String csmt, String cmateri, Date dcreateddate, String ccreatedby, Date dupdateddate, String cupdatedby) {
       this.id = id;
       this.mruang = mruang;
       this.msekolah = msekolah;
       this.mpegawai = mpegawai;
       this.mprodi = mprodi;
       this.mtbmtkl = mtbmtkl;
       this.msesikuliah = msesikuliah;
       this.mhari = mhari;
       this.cterm = cterm;
       this.ckelompok = ckelompok;
       this.dtglkul = dtglkul;
       this.cket = cket;
       this.cthajar = cthajar;
       this.csmt = csmt;
       this.cmateri = cmateri;
       this.dcreateddate = dcreateddate;
       this.ccreatedby = ccreatedby;
       this.dupdateddate = dupdateddate;
       this.cupdatedby = cupdatedby;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public Mruang getMruang() {
        return this.mruang;
    }
    
    public void setMruang(Mruang mruang) {
        this.mruang = mruang;
    }
    public Msekolah getMsekolah() {
        return this.msekolah;
    }
    
    public void setMsekolah(Msekolah msekolah) {
        this.msekolah = msekolah;
    }
    public Mpegawai getMpegawai() {
        return this.mpegawai;
    }
    
    public void setMpegawai(Mpegawai mpegawai) {
        this.mpegawai = mpegawai;
    }
    public Mprodi getMprodi() {
        return this.mprodi;
    }
    
    public void setMprodi(Mprodi mprodi) {
        this.mprodi = mprodi;
    }
    public Mtbmtkl getMtbmtkl() {
        return this.mtbmtkl;
    }
    
    public void setMtbmtkl(Mtbmtkl mtbmtkl) {
        this.mtbmtkl = mtbmtkl;
    }
    public Msesikuliah getMsesikuliah() {
        return this.msesikuliah;
    }
    
    public void setMsesikuliah(Msesikuliah msesikuliah) {
        this.msesikuliah = msesikuliah;
    }
    public Mhari getMhari() {
        return this.mhari;
    }
    
    public void setMhari(Mhari mhari) {
        this.mhari = mhari;
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
    public Date getDtglkul() {
        return this.dtglkul;
    }
    
    public void setDtglkul(Date dtglkul) {
        this.dtglkul = dtglkul;
    }
    public String getCket() {
        return this.cket;
    }
    
    public void setCket(String cket) {
        this.cket = cket;
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
    public String getCmateri() {
        return this.cmateri;
    }
    
    public void setCmateri(String cmateri) {
        this.cmateri = cmateri;
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


