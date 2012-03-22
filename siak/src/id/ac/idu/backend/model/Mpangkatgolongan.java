package id.ac.idu.backend.model;
// Generated 11 Mar 12 23:21:10 by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Mpangkatgolongan generated by hbm2java
 */
public class Mpangkatgolongan  implements java.io.Serializable {
	//public static String ID = "id";
    //public static String CODE = "ckdpangkatgolongan";
    //public static String NAME = "cnmpangkatgolongan";

     private int id;
     private String ckdpangkatgolongan;
     private String cnmpangkatgolongan;
     private Date dcreateddate;
     private String ccreatedby;
     private Date dupdateddate;
     private String cupdatedby;
     private Set<Mmhspascakhs> mmhspascakhses = new HashSet<Mmhspascakhs>(0);
     private Set<Mhistpangkatmhs> mhistpangkatmhses = new HashSet<Mhistpangkatmhs>(0);

    public Mpangkatgolongan() {
    }

	
    public Mpangkatgolongan(int id, String ckdpangkatgolongan) {
        this.id = id;
        this.ckdpangkatgolongan = ckdpangkatgolongan;
    }
    public Mpangkatgolongan(int id, String ckdpangkatgolongan, String cnmpangkatgolongan, Date dcreateddate, String ccreatedby, Date dupdateddate, String cupdatedby, Set<Mmhspascakhs> mmhspascakhses, Set<Mhistpangkatmhs> mhistpangkatmhses) {
       this.id = id;
       this.ckdpangkatgolongan = ckdpangkatgolongan;
       this.cnmpangkatgolongan = cnmpangkatgolongan;
       this.dcreateddate = dcreateddate;
       this.ccreatedby = ccreatedby;
       this.dupdateddate = dupdateddate;
       this.cupdatedby = cupdatedby;
       this.mmhspascakhses = mmhspascakhses;
       this.mhistpangkatmhses = mhistpangkatmhses;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public String getCkdpangkatgolongan() {
        return this.ckdpangkatgolongan;
    }
    
    public void setCkdpangkatgolongan(String ckdpangkatgolongan) {
        this.ckdpangkatgolongan = ckdpangkatgolongan;
    }
    public String getCnmpangkatgolongan() {
        return this.cnmpangkatgolongan;
    }
    
    public void setCnmpangkatgolongan(String cnmpangkatgolongan) {
        this.cnmpangkatgolongan = cnmpangkatgolongan;
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
    public Set<Mhistpangkatmhs> getMhistpangkatmhses() {
        return this.mhistpangkatmhses;
    }
    
    public void setMhistpangkatmhses(Set<Mhistpangkatmhs> mhistpangkatmhses) {
        this.mhistpangkatmhses = mhistpangkatmhses;
    }




}


