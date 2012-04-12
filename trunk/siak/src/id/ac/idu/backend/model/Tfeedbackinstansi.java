package id.ac.idu.backend.model;
// Generated 11 Mar 12 23:21:10 by Hibernate Tools 3.2.1.GA


import id.ac.idu.util.Codec;

import java.util.Date;

/**
 * Tfeedbackinstansi generated by hbm2java
 */
public class Tfeedbackinstansi  implements java.io.Serializable {


     private int id;
     private Malumni malumni;
     private String cjnsinstansi;
     private String cnminstansi;
     private String calminstansi;
     private String ckesanpesan;
     private String ccreatedby;
     private Date dcreateddate;
     private String cupdatedby;
     private Date dupdatedate;
     private String nmInstansi;

    public String getNmInstansi() {
        return Codec.JenisInstansi.valueOf(getCjnsinstansi()).getLabel();
    }

    public void setNmInstansi(String nmInstansi) {
        this.nmInstansi = nmInstansi;
    }



    public Tfeedbackinstansi() {
    }

	
    public Tfeedbackinstansi(int id, Malumni malumni, String cjnsinstansi, String cnminstansi, String calminstansi, String ckesanpesan) {
        this.id = id;
        this.malumni = malumni;
        this.cjnsinstansi = cjnsinstansi;
        this.cnminstansi = cnminstansi;
        this.calminstansi = calminstansi;
        this.ckesanpesan = ckesanpesan;
    }
    public Tfeedbackinstansi(int id, Malumni malumni, String cjnsinstansi, String cnminstansi, String calminstansi, String ckesanpesan, String ccreatedby, Date dcreateddate, String cupdatedby, Date dupdatedate) {
       this.id = id;
       this.malumni = malumni;
       this.cjnsinstansi = cjnsinstansi;
       this.cnminstansi = cnminstansi;
       this.calminstansi = calminstansi;
       this.ckesanpesan = ckesanpesan;
       this.ccreatedby = ccreatedby;
       this.dcreateddate = dcreateddate;
       this.cupdatedby = cupdatedby;
       this.dupdatedate = dupdatedate;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public Malumni getMalumni() {
        return this.malumni;
    }
    
    public void setMalumni(Malumni malumni) {
        this.malumni = malumni;
    }
    public String getCjnsinstansi() {
        return this.cjnsinstansi;
    }
    
    public void setCjnsinstansi(String cjnsinstansi) {
        this.cjnsinstansi = cjnsinstansi;
    }
    public String getCnminstansi() {
        return this.cnminstansi;
    }
    
    public void setCnminstansi(String cnminstansi) {
        this.cnminstansi = cnminstansi;
    }
    public String getCalminstansi() {
        return this.calminstansi;
    }
    
    public void setCalminstansi(String calminstansi) {
        this.calminstansi = calminstansi;
    }
    public String getCkesanpesan() {
        return this.ckesanpesan;
    }
    
    public void setCkesanpesan(String ckesanpesan) {
        this.ckesanpesan = ckesanpesan;
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
    public Date getDupdatedate() {
        return this.dupdatedate;
    }
    
    public void setDupdatedate(Date dupdatedate) {
        this.dupdatedate = dupdatedate;
    }




}

