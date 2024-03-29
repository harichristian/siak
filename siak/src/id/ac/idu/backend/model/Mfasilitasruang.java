package id.ac.idu.backend.model;
// Generated 11 Mar 12 23:21:10 by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * Mfasilitasruang generated by hbm2java
 */
public class Mfasilitasruang  implements java.io.Serializable {


     private int id;
     private Mruang mruang;
     private Mfasilitas mfasilitas;
     private int njml;
     private char cstatus;
     private Date dcreateddate;
     private String ccreatedby;
     private Date dupdateddate;
     private String cupdatedby;

    public Mfasilitasruang() {
    }

	
    public Mfasilitasruang(int id, Mruang mruang, Mfasilitas mfasilitas, int njml, char cstatus) {
        this.id = id;
        this.mruang = mruang;
        this.mfasilitas = mfasilitas;
        this.njml = njml;
        this.cstatus = cstatus;
    }
    public Mfasilitasruang(int id, Mruang mruang, Mfasilitas mfasilitas, int njml, char cstatus, Date dcreateddate, String ccreatedby, Date dupdateddate, String cupdatedby) {
       this.id = id;
       this.mruang = mruang;
       this.mfasilitas = mfasilitas;
       this.njml = njml;
       this.cstatus = cstatus;
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
    public Mfasilitas getMfasilitas() {
        return this.mfasilitas;
    }
    
    public void setMfasilitas(Mfasilitas mfasilitas) {
        this.mfasilitas = mfasilitas;
    }
    public int getNjml() {
        return this.njml;
    }
    
    public void setNjml(int njml) {
        this.njml = njml;
    }
    public char getCstatus() {
        return this.cstatus;
    }
    
    public void setCstatus(char cstatus) {
        this.cstatus = cstatus;
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


