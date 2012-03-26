package id.ac.idu.backend.model;
// Generated 11 Mar 12 23:21:10 by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * MkodePos generated by hbm2java
 */
public class MkodePos  implements java.io.Serializable {


     private String id;
     private String kodepos;
     private Date dcreateddate;
     private String ccreatedby;
     private Date dupdateddate;
     private String cupdatedby;
     private Set<Mkel> mkels = new HashSet<Mkel>(0);

    public MkodePos() {
    }

	
    public MkodePos(String id, String kodepos) {
        this.id = id;
        this.kodepos = kodepos;
    }
    public MkodePos(String id, String kodepos, Date dcreateddate, String ccreatedby, Date dupdateddate, String cupdatedby, Set<Mkel> mkels) {
       this.id = id;
       this.kodepos = kodepos;
       this.dcreateddate = dcreateddate;
       this.ccreatedby = ccreatedby;
       this.dupdateddate = dupdateddate;
       this.cupdatedby = cupdatedby;
       this.mkels = mkels;
    }
   
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    public String getKodepos() {
        return this.kodepos;
    }
    
    public void setKodepos(String kodepos) {
        this.kodepos = kodepos;
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
    public Set<Mkel> getMkels() {
        return this.mkels;
    }
    
    public void setMkels(Set<Mkel> mkels) {
        this.mkels = mkels;
    }




}

