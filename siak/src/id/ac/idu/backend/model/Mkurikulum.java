package id.ac.idu.backend.model;
// Generated 11 Mar 12 23:21:10 by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Mkurikulum generated by hbm2java
 */
public class Mkurikulum  implements java.io.Serializable {
	//public static String ID = "id";
    //public static String CODE = "ckodekur";
    //public static String COHORT = "ccohort";
    //public static String PRODI = "mprodi";

     private int id;
     private Mprodi mprodi;
     private String ckodekur;
     private String ccohort;
     private Date dcreateddate;
     private String ccreatedby;
     private Date dupdateddate;
     private String cupdatedby;
     private Mpeminatan mpeminatan;
     private Set<Mkurmhs> mkurmhses = new HashSet<Mkurmhs>(0);
     private Set<Mdetilkurikulum> mdetilkurikulums = new HashSet<Mdetilkurikulum>(0);

    public Mkurikulum() {
    }

	
    public Mkurikulum(int id, Mprodi mprodi, String ckodekur) {
        this.id = id;
        this.mprodi = mprodi;
        this.ckodekur = ckodekur;
    }
    public Mkurikulum(int id, Mprodi mprodi, String ckodekur, String ccohort, Date dcreateddate, String ccreatedby, Date dupdateddate, String cupdatedby, Set<Mkurmhs> mkurmhses) {
       this.id = id;
       this.mprodi = mprodi;
       this.ckodekur = ckodekur;
       this.ccohort = ccohort;
       this.dcreateddate = dcreateddate;
       this.ccreatedby = ccreatedby;
       this.dupdateddate = dupdateddate;
       this.cupdatedby = cupdatedby;
       this.mkurmhses = mkurmhses;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public Mprodi getMprodi() {
        return this.mprodi;
    }
    
    public void setMprodi(Mprodi mprodi) {
        this.mprodi = mprodi;
    }
    public String getCkodekur() {
        return this.ckodekur;
    }
    
    public void setCkodekur(String ckodekur) {
        this.ckodekur = ckodekur;
    }
    public String getCcohort() {
        return this.ccohort;
    }
    
    public void setCcohort(String ccohort) {
        this.ccohort = ccohort;
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
    public Set<Mkurmhs> getMkurmhses() {
        return this.mkurmhses;
    }
    
    public void setMkurmhses(Set<Mkurmhs> mkurmhses) {
        this.mkurmhses = mkurmhses;
    }

    public Mpeminatan getMpeminatan() {
        return this.mpeminatan;
    }

    public void setMpeminatan(Mpeminatan mpeminatan) {
        this.mpeminatan = mpeminatan;
    }

    public Set<Mdetilkurikulum> getMdetilkurikulums() {
        return this.mdetilkurikulums;
    }

    public void setMdetilkurikulums(Set<Mdetilkurikulum> mdetilkurikulums) {
        for(Object obj : mdetilkurikulums)
            ((Mdetilkurikulum) obj).setKurikulumId(this.id);
        this.mdetilkurikulums = mdetilkurikulums;
    }
}


