package id.ac.idu.backend.model;
// Generated 11 Mar 12 23:21:10 by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * Mterm generated by hbm2java
 */
public class Mterm  implements java.io.Serializable {


     private int id;
     private String kdTerm;
     private String deskripsi;
     private String ccreatedby;
     private Date dcreateddate;
     private String cupdatedby;
     private Date dupdateddate;

    public Mterm() {
    }

	
    public Mterm(int id, String kdTerm) {
        this.id = id;
        this.kdTerm = kdTerm;
    }
    public Mterm(int id, String kdTerm, String deskripsi, String ccreatedby, Date dcreateddate, String cupdatedby, Date dupdateddate) {
       this.id = id;
       this.kdTerm = kdTerm;
       this.deskripsi = deskripsi;
       this.ccreatedby = ccreatedby;
       this.dcreateddate = dcreateddate;
       this.cupdatedby = cupdatedby;
       this.dupdateddate = dupdateddate;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public String getKdTerm() {
        return this.kdTerm;
    }
    
    public void setKdTerm(String kdTerm) {
        this.kdTerm = kdTerm;
    }
    public String getDeskripsi() {
        return this.deskripsi;
    }
    
    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
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


