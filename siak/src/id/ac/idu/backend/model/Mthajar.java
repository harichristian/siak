package id.ac.idu.backend.model;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 05/04/12
 * Time: 13:34
 * To change this template use File | Settings | File Templates.
 */
public class Mthajar implements java.io.Serializable {
    private int id;
    private String cthajar;
    private String csmt;
    private String ccreatedby;
    private Date dcreateddate;
    private String cupdatedby;
    private Date dupdateddate;
    private Date dtglmulai;
    private Date dtglakhir;

    public Mthajar() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCthajar() {
        return cthajar;
    }

    public void setCthajar(String cthajar) {
        this.cthajar = cthajar;
    }

    public String getCsmt() {
        return csmt;
    }

    public void setCsmt(String csmt) {
        this.csmt = csmt;
    }

    public String getCcreatedby() {
        return ccreatedby;
    }

    public void setCcreatedby(String ccreatedby) {
        this.ccreatedby = ccreatedby;
    }

    public Date getDcreateddate() {
        return dcreateddate;
    }

    public void setDcreateddate(Date dcreateddate) {
        this.dcreateddate = dcreateddate;
    }

    public String getCupdatedby() {
        return cupdatedby;
    }

    public void setCupdatedby(String cupdatedby) {
        this.cupdatedby = cupdatedby;
    }

    public Date getDupdateddate() {
        return dupdateddate;
    }

    public void setDupdateddate(Date dupdateddate) {
        this.dupdateddate = dupdateddate;
    }

    public Date getDtglmulai() {
        return dtglmulai;
    }

    public void setDtglmulai(Date dtglmulai) {
        this.dtglmulai = dtglmulai;
    }

    public Date getDtglakhir() {
        return dtglakhir;
    }

    public void setDtglakhir(Date dtglakhir) {
        this.dtglakhir = dtglakhir;
    }
}
