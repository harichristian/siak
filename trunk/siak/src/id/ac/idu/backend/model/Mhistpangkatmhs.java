package id.ac.idu.backend.model;
// Generated 11 Mar 12 23:21:10 by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * Mhistpangkatmhs generated by hbm2java
 */
public class Mhistpangkatmhs  implements java.io.Serializable {


     private int id;
     private Mpangkatgolongan mpangkatgolongan;
     private int mahasiswaId;
     private String ctmt;
     private String cket;
     private Date dcreateddate;
     private String ccreatedby;
     private Date dupdateddate;
     private String cupdatedby;

    public Mhistpangkatmhs() {
    }

	
    public Mhistpangkatmhs(int id, Mpangkatgolongan mpangkatgolongan, int mahasiswaId) {
        this.id = id;
        this.mpangkatgolongan = mpangkatgolongan;
        this.mahasiswaId = mahasiswaId;
    }
    public Mhistpangkatmhs(int id, Mpangkatgolongan mpangkatgolongan, int mahasiswaId, String ctmt, String cket
            , Date dcreateddate, String ccreatedby, Date dupdateddate, String cupdatedby) {
       this.id = id;
       this.mpangkatgolongan = mpangkatgolongan;
       this.mahasiswaId = mahasiswaId;
       this.ctmt = ctmt;
       this.cket = cket;
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
    public Mpangkatgolongan getMpangkatgolongan() {
        return this.mpangkatgolongan;
    }
    
    public void setMpangkatgolongan(Mpangkatgolongan mpangkatgolongan) {
        this.mpangkatgolongan = mpangkatgolongan;
    }

    public int getMahasiswaId() {
        return mahasiswaId;
    }

    public void setMahasiswaId(int mahasiswaId) {
        this.mahasiswaId = mahasiswaId;
    }

    public String getCtmt() {
        return this.ctmt;
    }
    
    public void setCtmt(String ctmt) {
        this.ctmt = ctmt;
    }
    public String getCket() {
        return this.cket;
    }
    
    public void setCket(String cket) {
        this.cket = cket;
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

