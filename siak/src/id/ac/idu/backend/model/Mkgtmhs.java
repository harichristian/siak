package id.ac.idu.backend.model;
// Generated 11 Mar 12 23:21:10 by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * Mkgtmhs generated by hbm2java
 */
public class Mkgtmhs  implements java.io.Serializable {


     private int id;
     private int mahasiswaId;
     private String cno;
     private String cnmsimposium;
     private String cperanan;
     private String ctahun;
     private String cinstitusi;
     private Date dcreateddate;
     private String ccreatedby;
     private Date dupdateddate;
     private String cupdatedby;

    public Mkgtmhs() {
    }

	
    public Mkgtmhs(int id, int mahasiswaId, String cno) {
        this.id = id;
        this.mahasiswaId = mahasiswaId;
        this.cno = cno;
    }
    public Mkgtmhs(int id, int mahasiswaId, String cno, String cnmsimposium, String cperanan, String ctahun
            , String cinstitusi, Date dcreateddate, String ccreatedby, Date dupdateddate, String cupdatedby) {
       this.id = id;
       this.mahasiswaId = mahasiswaId;
       this.cno = cno;
       this.cnmsimposium = cnmsimposium;
       this.cperanan = cperanan;
       this.ctahun = ctahun;
       this.cinstitusi = cinstitusi;
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

    public int getMahasiswaId() {
        return mahasiswaId;
    }

    public void setMahasiswaId(int mahasiswaId) {
        this.mahasiswaId = mahasiswaId;
    }

    public String getCno() {
        return this.cno;
    }
    
    public void setCno(String cno) {
        this.cno = cno;
    }
    public String getCnmsimposium() {
        return this.cnmsimposium;
    }
    
    public void setCnmsimposium(String cnmsimposium) {
        this.cnmsimposium = cnmsimposium;
    }
    public String getCperanan() {
        return this.cperanan;
    }
    
    public void setCperanan(String cperanan) {
        this.cperanan = cperanan;
    }
    public String getCtahun() {
        return this.ctahun;
    }
    
    public void setCtahun(String ctahun) {
        this.ctahun = ctahun;
    }
    public String getCinstitusi() {
        return this.cinstitusi;
    }
    
    public void setCinstitusi(String cinstitusi) {
        this.cinstitusi = cinstitusi;
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


