package id.ac.idu.backend.model;
// Generated 11 Mar 12 23:21:10 by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Mfasilitas generated by hbm2java
 */
public class Mfasilitas  implements java.io.Serializable {


     private int id;
     private String ckdfasilitas;
     private String cnamaFasilitas;
     private Date dcreateddate;
     private String ccreatedby;
     private Date dupdateddate;
     private String cupdatedby;
     private Set<Mfasilitasruang> mfasilitasruangs = new HashSet<Mfasilitasruang>(0);

    public Mfasilitas() {
    }

	
    public Mfasilitas(int id, String ckdfasilitas, String cnamaFasilitas) {
        this.id = id;
        this.ckdfasilitas = ckdfasilitas;
        this.cnamaFasilitas = cnamaFasilitas;
    }
    public Mfasilitas(int id, String ckdfasilitas, String cnamaFasilitas, Date dcreateddate, String ccreatedby, Date dupdateddate, String cupdatedby, Set<Mfasilitasruang> mfasilitasruangs) {
       this.id = id;
       this.ckdfasilitas = ckdfasilitas;
       this.cnamaFasilitas = cnamaFasilitas;
       this.dcreateddate = dcreateddate;
       this.ccreatedby = ccreatedby;
       this.dupdateddate = dupdateddate;
       this.cupdatedby = cupdatedby;
       this.mfasilitasruangs = mfasilitasruangs;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public String getCkdfasilitas() {
        return this.ckdfasilitas;
    }
    
    public void setCkdfasilitas(String ckdfasilitas) {
        this.ckdfasilitas = ckdfasilitas;
    }
    public String getCnamaFasilitas() {
        return this.cnamaFasilitas;
    }
    
    public void setCnamaFasilitas(String cnamaFasilitas) {
        this.cnamaFasilitas = cnamaFasilitas;
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
    public Set<Mfasilitasruang> getMfasilitasruangs() {
        return this.mfasilitasruangs;
    }
    
    public void setMfasilitasruangs(Set<Mfasilitasruang> mfasilitasruangs) {
        this.mfasilitasruangs = mfasilitasruangs;
    }




}


