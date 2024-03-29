package id.ac.idu.backend.model;
// Generated 11 Mar 12 23:21:10 by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;
import java.util.Date;

/**
 * Tdaftartugasmenulis generated by hbm2java
 */
public class Tdaftartugasmenulis  implements java.io.Serializable {


     private int nrecid;
     private String ckdsekolah;
     private String ckdprogst;
     private String cterm;
     private String cthajar;
     private String csmt;
     private String cnim;
     private String cnip1;
     private String cnip2;
     private String ctopik;
     private String cjenismenulis;
     private Date dtgldaftarmenulis;
     private BigDecimal nnilaiakhir;
     private String ckdgrade;
     private String cflag;
     private String cinstansi;
     private String calamat;
     private Date dtglkumpulbuku;
     private Date dtgldaftarsidangproposal;
     private Date dtgldaftarsidangthesis;
     private String ccreatedby;
     private Date dcreateddate;
     private String cupdatedby;
     private Date dupdateddate;

    public Tdaftartugasmenulis() {
    }

	
    public Tdaftartugasmenulis(int nrecid, String ckdsekolah, String ckdprogst, String cterm, String cnim, String cjenismenulis) {
        this.nrecid = nrecid;
        this.ckdsekolah = ckdsekolah;
        this.ckdprogst = ckdprogst;
        this.cterm = cterm;
        this.cnim = cnim;
        this.cjenismenulis = cjenismenulis;
    }
    public Tdaftartugasmenulis(int nrecid, String ckdsekolah, String ckdprogst, String cterm, String cthajar, String csmt, String cnim, String cnip1, String cnip2, String ctopik, String cjenismenulis, Date dtgldaftarmenulis, BigDecimal nnilaiakhir, String ckdgrade, String cflag, String cinstansi, String calamat, Date dtglkumpulbuku, Date dtgldaftarsidangproposal, Date dtgldaftarsidangthesis, String ccreatedby, Date dcreateddate, String cupdatedby, Date dupdateddate) {
       this.nrecid = nrecid;
       this.ckdsekolah = ckdsekolah;
       this.ckdprogst = ckdprogst;
       this.cterm = cterm;
       this.cthajar = cthajar;
       this.csmt = csmt;
       this.cnim = cnim;
       this.cnip1 = cnip1;
       this.cnip2 = cnip2;
       this.ctopik = ctopik;
       this.cjenismenulis = cjenismenulis;
       this.dtgldaftarmenulis = dtgldaftarmenulis;
       this.nnilaiakhir = nnilaiakhir;
       this.ckdgrade = ckdgrade;
       this.cflag = cflag;
       this.cinstansi = cinstansi;
       this.calamat = calamat;
       this.dtglkumpulbuku = dtglkumpulbuku;
       this.dtgldaftarsidangproposal = dtgldaftarsidangproposal;
       this.dtgldaftarsidangthesis = dtgldaftarsidangthesis;
       this.ccreatedby = ccreatedby;
       this.dcreateddate = dcreateddate;
       this.cupdatedby = cupdatedby;
       this.dupdateddate = dupdateddate;
    }
   
    public int getNrecid() {
        return this.nrecid;
    }
    
    public void setNrecid(int nrecid) {
        this.nrecid = nrecid;
    }
    public String getCkdsekolah() {
        return this.ckdsekolah;
    }
    
    public void setCkdsekolah(String ckdsekolah) {
        this.ckdsekolah = ckdsekolah;
    }
    public String getCkdprogst() {
        return this.ckdprogst;
    }
    
    public void setCkdprogst(String ckdprogst) {
        this.ckdprogst = ckdprogst;
    }
    public String getCterm() {
        return this.cterm;
    }
    
    public void setCterm(String cterm) {
        this.cterm = cterm;
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
    public String getCnim() {
        return this.cnim;
    }
    
    public void setCnim(String cnim) {
        this.cnim = cnim;
    }
    public String getCnip1() {
        return this.cnip1;
    }
    
    public void setCnip1(String cnip1) {
        this.cnip1 = cnip1;
    }
    public String getCnip2() {
        return this.cnip2;
    }
    
    public void setCnip2(String cnip2) {
        this.cnip2 = cnip2;
    }
    public String getCtopik() {
        return this.ctopik;
    }
    
    public void setCtopik(String ctopik) {
        this.ctopik = ctopik;
    }
    public String getCjenismenulis() {
        return this.cjenismenulis;
    }
    
    public void setCjenismenulis(String cjenismenulis) {
        this.cjenismenulis = cjenismenulis;
    }
    public Date getDtgldaftarmenulis() {
        return this.dtgldaftarmenulis;
    }
    
    public void setDtgldaftarmenulis(Date dtgldaftarmenulis) {
        this.dtgldaftarmenulis = dtgldaftarmenulis;
    }
    public BigDecimal getNnilaiakhir() {
        return this.nnilaiakhir;
    }
    
    public void setNnilaiakhir(BigDecimal nnilaiakhir) {
        this.nnilaiakhir = nnilaiakhir;
    }
    public String getCkdgrade() {
        return this.ckdgrade;
    }
    
    public void setCkdgrade(String ckdgrade) {
        this.ckdgrade = ckdgrade;
    }
    public String getCflag() {
        return this.cflag;
    }
    
    public void setCflag(String cflag) {
        this.cflag = cflag;
    }
    public String getCinstansi() {
        return this.cinstansi;
    }
    
    public void setCinstansi(String cinstansi) {
        this.cinstansi = cinstansi;
    }
    public String getCalamat() {
        return this.calamat;
    }
    
    public void setCalamat(String calamat) {
        this.calamat = calamat;
    }
    public Date getDtglkumpulbuku() {
        return this.dtglkumpulbuku;
    }
    
    public void setDtglkumpulbuku(Date dtglkumpulbuku) {
        this.dtglkumpulbuku = dtglkumpulbuku;
    }
    public Date getDtgldaftarsidangproposal() {
        return this.dtgldaftarsidangproposal;
    }
    
    public void setDtgldaftarsidangproposal(Date dtgldaftarsidangproposal) {
        this.dtgldaftarsidangproposal = dtgldaftarsidangproposal;
    }
    public Date getDtgldaftarsidangthesis() {
        return this.dtgldaftarsidangthesis;
    }
    
    public void setDtgldaftarsidangthesis(Date dtgldaftarsidangthesis) {
        this.dtgldaftarsidangthesis = dtgldaftarsidangthesis;
    }
    public String getCcreatedby() {
        return this.ccreatedby;
    }
    
    public void setCcreatedby(String ccreatedby) {
        this.ccreatedby = ccreatedby;
    }
    public Date getDcreateddate() {
        return this.dcreateddate;
    }
    
    public void setDcreateddate(Date dcreateddate) {
        this.dcreateddate = dcreateddate;
    }
    public String getCupdatedby() {
        return this.cupdatedby;
    }
    
    public void setCupdatedby(String cupdatedby) {
        this.cupdatedby = cupdatedby;
    }
    public Date getDupdateddate() {
        return this.dupdateddate;
    }
    
    public void setDupdateddate(Date dupdateddate) {
        this.dupdateddate = dupdateddate;
    }




}


