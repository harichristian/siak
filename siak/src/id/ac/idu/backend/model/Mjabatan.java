package id.ac.idu.backend.model;
// Generated 11 Mar 12 23:21:10 by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Mjabatan generated by hbm2java
 */
public class Mjabatan  implements java.io.Serializable {


     private int id;
     private String ckdjabatan;
     private String cnmjabatan;
     private Date dcreateddate;
     private String ccreatedby;
     private Date dupdateddate;
     private String cupdatedby;
     private Set<Mmhspascakhs> mmhspascakhses = new HashSet<Mmhspascakhs>(0);
     private Set<Mpegawai> mpegawais = new HashSet<Mpegawai>(0);

    public Mjabatan() {
    }

	
    public Mjabatan(int id, String ckdjabatan, String cnmjabatan) {
        this.id = id;
        this.ckdjabatan = ckdjabatan;
        this.cnmjabatan = cnmjabatan;
    }
    public Mjabatan(int id, String ckdjabatan, String cnmjabatan, Date dcreateddate, String ccreatedby, Date dupdateddate, String cupdatedby, Set<Mmhspascakhs> mmhspascakhses, Set<Mpegawai> mpegawais) {
       this.id = id;
       this.ckdjabatan = ckdjabatan;
       this.cnmjabatan = cnmjabatan;
       this.dcreateddate = dcreateddate;
       this.ccreatedby = ccreatedby;
       this.dupdateddate = dupdateddate;
       this.cupdatedby = cupdatedby;
       this.mmhspascakhses = mmhspascakhses;
       this.mpegawais = mpegawais;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public String getCkdjabatan() {
        return this.ckdjabatan;
    }
    
    public void setCkdjabatan(String ckdjabatan) {
        this.ckdjabatan = ckdjabatan;
    }
    public String getCnmjabatan() {
        return this.cnmjabatan;
    }
    
    public void setCnmjabatan(String cnmjabatan) {
        this.cnmjabatan = cnmjabatan;
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
    public Set<Mmhspascakhs> getMmhspascakhses() {
        return this.mmhspascakhses;
    }
    
    public void setMmhspascakhses(Set<Mmhspascakhs> mmhspascakhses) {
        this.mmhspascakhses = mmhspascakhses;
    }
    public Set<Mpegawai> getMpegawais() {
        return this.mpegawais;
    }
    
    public void setMpegawais(Set<Mpegawai> mpegawais) {
        this.mpegawais = mpegawais;
    }




}


