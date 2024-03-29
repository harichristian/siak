package id.ac.idu.backend.model;// default package
// Generated Mar 21, 2012 2:09:06 AM by Hibernate Tools 3.1.0.beta4

import id.ac.idu.util.Codec;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * Mfeedback generated by hbm2java
 */

public class Mfeedback  implements java.io.Serializable {


    // Fields    

     private int id;
     private char ckdfeedback;
     private Msekolah msekolah;
     private Mprodi mprodi;
     private byte nnopertanyaan;
     private String cpertanyaan;
     private String ccreatedby;
     private Date ccreateddate;
     private String cupdatedby;
     private Date cupdateddate;
     private Set<Tfeedbackalumni> tfeedbackalumnis = new HashSet<Tfeedbackalumni>(0);

    public String getNmfeedback() {
        return  Codec.KodeFeedback.valueOf(String.valueOf(getCkdfeedback())).getLabel();
    }

    public void setNmfeedback(String nmfeedback) {
        this.nmfeedback = nmfeedback;
    }

    private String nmfeedback;


    // Constructors

    /** default constructor */
    public Mfeedback() {
    }

	/** minimal constructor */
    public Mfeedback(int id, char ckdfeedback, Msekolah msekolah, Mprodi mprodi, byte nnopertanyaan) {
        this.id = id;
        this.ckdfeedback = ckdfeedback;
        this.msekolah = msekolah;
        this.mprodi = mprodi;
        this.nnopertanyaan = nnopertanyaan;
    }
    
    /** full constructor */
    public Mfeedback(int id, char ckdfeedback, Msekolah msekolah, Mprodi mprodi, byte nnopertanyaan, String cpertanyaan
            , String ccreatedby, Date ccreateddate, String cupdatedby, Date cupdateddate, Set<Tfeedbackalumni> tfeedbackalumnis) {
        this.id = id;
        this.ckdfeedback = ckdfeedback;
        this.msekolah = msekolah;
        this.mprodi = mprodi;
        this.nnopertanyaan = nnopertanyaan;
        this.cpertanyaan = cpertanyaan;
        this.ccreatedby = ccreatedby;
        this.ccreateddate = ccreateddate;
        this.cupdatedby = cupdatedby;
        this.cupdateddate = cupdateddate;
        this.tfeedbackalumnis = tfeedbackalumnis;
    }
    

   
    // Property accessors

    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public char getCkdfeedback() {
        return this.ckdfeedback;
    }
    
    public void setCkdfeedback(char ckdfeedback) {
        this.ckdfeedback = ckdfeedback;
    }

    public Msekolah getMsekolah() {
        return this.msekolah;
    }
    
    public void setMsekolah(Msekolah msekolah) {
        this.msekolah = msekolah;
    }

    public Mprodi getMprodi() {
        return this.mprodi;
    }
    
    public void setMprodi(Mprodi mprodi) {
        this.mprodi = mprodi;
    }

    public byte getNnopertanyaan() {
        return this.nnopertanyaan;
    }
    
    public void setNnopertanyaan(byte nnopertanyaan) {
        this.nnopertanyaan = nnopertanyaan;
    }

    public String getCpertanyaan() {
        return this.cpertanyaan;
    }
    
    public void setCpertanyaan(String cpertanyaan) {
        this.cpertanyaan = cpertanyaan;
    }

    public String getCcreatedby() {
        return this.ccreatedby;
    }
    
    public void setCcreatedby(String ccreatedby) {
        this.ccreatedby = ccreatedby;
    }

    public Date getCcreateddate() {
        return this.ccreateddate;
    }
    
    public void setCcreateddate(Date ccreateddate) {
        this.ccreateddate = ccreateddate;
    }

    public String getCupdatedby() {
        return this.cupdatedby;
    }
    
    public void setCupdatedby(String cupdatedby) {
        this.cupdatedby = cupdatedby;
    }

    public Date getCupdateddate() {
        return this.cupdateddate;
    }
    
    public void setCupdateddate(Date cupdateddate) {
        this.cupdateddate = cupdateddate;
    }

    public Set<Tfeedbackalumni> getTfeedbackalumnis() {
        return this.tfeedbackalumnis;
    }
    
    public void setTfeedbackalumnis(Set<Tfeedbackalumni> tfeedbackalumnis) {
        this.tfeedbackalumnis = tfeedbackalumnis;
    }
   








}
