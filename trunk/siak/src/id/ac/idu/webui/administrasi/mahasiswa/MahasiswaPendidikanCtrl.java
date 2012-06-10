package id.ac.idu.webui.administrasi.mahasiswa;

import com.trg.search.Filter;
import id.ac.idu.backend.model.*;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.util.Codec;
import id.ac.idu.webui.administrasi.mahasiswa.model.PendidikanSearchJenjangList;
import id.ac.idu.webui.administrasi.mahasiswa.model.PendidikanSearchProdiList;
import id.ac.idu.webui.administrasi.mahasiswa.model.PendidikanSearchUnivList;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.pagging.PagedListWrapper;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 28 Mar 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class MahasiswaPendidikanCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(MahasiswaKhususCtrl.class);

    private int pageSize;

    protected Window windowPendidikanDetail;
    protected Borderlayout borderPendidikan;

    /* Start Lov */
    private String _type;

    protected Bandbox cmbPgurTinggiS1;
    protected Paging pagingPgurTinggiS1;
    protected Listbox listPgurTinggiS1;
    protected Textbox tbCodeS1;
    protected Textbox tbNameS1;

    protected Bandbox cmbPgurTinggiS2;
    protected Paging pagingPgurTinggiS2;
    protected Listbox listPgurTinggiS2;
    protected Textbox tbCodeS2;
    protected Textbox tbNameS2;

    protected Bandbox cmbProgramS1;
    protected Paging  pagingProgramS1;
    protected Listbox listProgramS1;
    protected Textbox tbCodeProgramS1;
    protected Textbox tbNameProgramS1;

    protected Bandbox cmbProgramS2;
    protected Paging  pagingProgramS2;
    protected Listbox listProgramS2;
    protected Textbox tbCodeProgramS2;
    protected Textbox tbNameProgramS2;

    protected Bandbox cmbJenjangS1;
    protected Paging  pagingJenjangS1;
    protected Listbox listJenjangS1;
    protected Textbox tbCodeJenjangS1;
    protected Textbox tbNameJenjangS1;

    protected Bandbox cmbJenjangS2;
    protected Paging  pagingJenjangS2;
    protected Listbox listJenjangS2;
    protected Textbox tbCodeJenjangS2;
    protected Textbox tbNameJenjangS2;
    /* End Lov */

    protected Textbox txtbCodeS1;
    protected Textbox txtbNameS1;
    protected Textbox txtbStatusS1;
    protected Textbox txtbCodeProgramS1;
    protected Textbox txtbNamaProgramS1;
    protected Textbox txtbNamaJenjangS1;
    protected Decimalbox txtbIpkS1;
    protected Datebox txtbTglLulusS1;
    protected Textbox txtbBebanStudiS1;

    protected Textbox txtbCodeS2;
    protected Textbox txtbNameS2;
    protected Textbox txtbStatusS2;
    protected Textbox txtbCodeProgramS2;
    protected Textbox txtbNamaProgramS2;
    protected Textbox txtbNamaJenjangS2;
    protected Decimalbox txtbIpkS2;
    protected Datebox txtbTglLulusS2;
    protected Textbox txtbBebanStudiS2;

    protected Mhistpnddkmhs mhistpnddkmhsS1;
    protected Mhistpnddkmhs mhistpnddkmhsS2;

    private int mhsid;
    private MahasiswaDetailCtrl detailCtrl;
    private AnnotateDataBinder binder;

    private transient PagedListWrapper<Muniv> plwUniv;
    private transient PagedListWrapper<Mprodi> plwProdi;
    private transient PagedListWrapper<Mjenjang> plwJenjang;

    public MahasiswaPendidikanCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        logger.info("Page Khusus Makasiswa Loaded");

        super.doAfterCompose(window);
        self.setAttribute("controller", this, false);

        if(arg.containsKey("ModuleMainController")) {
            setDetailCtrl((MahasiswaDetailCtrl) arg.get("ModuleMainController"));
            getDetailCtrl().setPendidikanCtrl(this);
        }
    }

    @SuppressWarnings("unchecked")
    public void onCreate$windowPendidikanDetail(Event event) throws Exception {
        setPageSize(20);

        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

        doReadOnlyMode(!getDetailCtrl().getMainCtrl().btnSave.isVisible());

        this.doLoadData();
        binder.loadAll();
        this.doFitSize();
        this.doInitData();
    }

    private void doLoadData() {
        if(getMahasiswa() == null) return;
        
        Set<Mhistpnddkmhs> mhistpnddkmhses = getMahasiswa().getMhistpnddkmhses();

        if(getMahasiswa().getId() > 0) {
            Mhistpnddkmhs mhistpnddkmhs;
            for(Object m0 : mhistpnddkmhses) {
                mhistpnddkmhs = (Mhistpnddkmhs) m0;

                if(mhistpnddkmhs.getMjenjang().getCsingkat().equalsIgnoreCase(Codec.Jenjang.Jen1.getValue()))
                    setMhistpnddkmhsS1(mhistpnddkmhs);
                else if(mhistpnddkmhs.getMjenjang().getCsingkat().equalsIgnoreCase(Codec.Jenjang.Jen2.getValue()))
                    setMhistpnddkmhsS2(mhistpnddkmhs);
            }
        }

        if(mhistpnddkmhses.size() < 1) {
            boolean isCreateS1 = true;
            if(getMhistpnddkmhsS1()!=null)
                isCreateS1 = (!(getMhistpnddkmhsS1().getMahasiswaId() == getMahasiswa().getId()));

            if(isCreateS1) setMhistpnddkmhsS1(new Mhistpnddkmhs());

            boolean isCreateS2 = true;
            if(getMhistpnddkmhsS2()!=null)
                isCreateS2 = (!(getMhistpnddkmhsS2().getMahasiswaId() == getMahasiswa().getId()));

            if(isCreateS2) setMhistpnddkmhsS2(new Mhistpnddkmhs());
        }
    }

    public void doReadOnlyMode(boolean b) {
        cmbPgurTinggiS1.setDisabled(b);
        cmbProgramS1.setDisabled(b);
        cmbJenjangS1.setDisabled(b);

        txtbIpkS1.setReadonly(b);
        txtbTglLulusS1.setDisabled(b);
        txtbBebanStudiS1.setReadonly(b);

        cmbPgurTinggiS2.setDisabled(b);
        cmbProgramS2.setDisabled(b);
        cmbJenjangS2.setDisabled(b);

        txtbIpkS2.setReadonly(b);
        txtbTglLulusS2.setDisabled(b);
        txtbBebanStudiS2.setReadonly(b);
    }

    public void doInitData()  {
        if(getMhistpnddkmhsS1() == null)
            setMhistpnddkmhsS1(getDetailCtrl().getMainCtrl().getMhistpnddkmhsService().getNew());

        if(getMhistpnddkmhsS2() == null)
            setMhistpnddkmhsS2(getDetailCtrl().getMainCtrl().getMhistpnddkmhsService().getNew());
    }

    /* Lov Perguruan Tinggi */
    public void onOpen$cmbPgurTinggiS1(Event event) {
        this._type = Codec.Jenjang.Jen1.getValue();
        this.searchPerguruanS1(null);
    }

    public void onClick$buttonSearchPgurTinggiS1(Event event) {
        Filter filter1 = null;
        Filter filter2 = null;

        if (StringUtils.isNotEmpty(tbCodeS1.getValue()))
            filter1 = new Filter("ckdUniv", "%" + tbCodeS1.getValue() + "%", Filter.OP_ILIKE);

        if (StringUtils.isNotEmpty(tbNameS1.getValue()))
            filter2 = new Filter("cnamaUniv", "%" + tbNameS1.getValue() + "%", Filter.OP_ILIKE);

        this.searchPerguruanS1(filter1, filter2);
    }

    public void onClick$buttonClosePgurTinggiS1(Event event) {
        cmbPgurTinggiS1.close();
    }

    protected Listheader codeS1;
    protected Listheader nameS1;
    protected Listheader statusS1;
    
    public void searchPerguruanS1(Filter... filters) {
        codeS1.setSortAscending(new FieldComparator("ckdUniv",true));
        codeS1.setSortDescending(new FieldComparator("ckdUniv",false));
        nameS1.setSortAscending(new FieldComparator("cnamaUniv",true));
        nameS1.setSortDescending(new FieldComparator("cnamaUniv",false));
        statusS1.setSortAscending(new FieldComparator("cstatus",true));
        statusS1.setSortDescending(new FieldComparator("cstatus",false));

        HibernateSearchObject<Muniv> soUniv = new HibernateSearchObject<Muniv>(Muniv.class);

        if(filters != null) {
            for (Filter filter : filters)
                soUniv.addFilter(filter);
        }

        soUniv.addSort("ckdUniv", false);
        pagingPgurTinggiS1.setPageSize(pageSize);
		pagingPgurTinggiS1.setDetailed(true);
		getPlwUniv().init(soUniv, listPgurTinggiS1, pagingPgurTinggiS1);
		listPgurTinggiS1.setItemRenderer(new PendidikanSearchUnivList());
    }

    public void onOpen$cmbPgurTinggiS2(Event event) {
        this._type = Codec.Jenjang.Jen2.getValue();
        this.searchPerguruanS2(null);
    }

    public void onClick$buttonSearchPgurTinggiS2(Event event) {
        Filter filter1 = null;
        Filter filter2 = null;

        if (StringUtils.isNotEmpty(tbCodeS2.getValue()))
            filter1 = new Filter("ckdUniv", "%" + tbCodeS2.getValue() + "%", Filter.OP_ILIKE);

        if (StringUtils.isNotEmpty(tbNameS2.getValue()))
            filter2 = new Filter("cnamaUniv", "%" + tbNameS2.getValue() + "%", Filter.OP_ILIKE);

        this.searchPerguruanS2(filter1, filter2);
    }

    public void onClick$buttonClosePgurTinggiS2(Event event) {
        cmbPgurTinggiS2.close();
    }

    protected Listheader codeS2;
    protected Listheader nameS2;
    protected Listheader statusS2;
    
    public void searchPerguruanS2(Filter... filters) {
        codeS2.setSortAscending(new FieldComparator("ckdUniv",true));
        codeS2.setSortDescending(new FieldComparator("ckdUniv",false));
        nameS2.setSortAscending(new FieldComparator("cnamaUniv",true));;
        nameS2.setSortDescending(new FieldComparator("cnamaUniv",false));;
        statusS2.setSortAscending(new FieldComparator("cstatus",true));
        statusS2.setSortDescending(new FieldComparator("cstatus",false));

        HibernateSearchObject<Muniv> soUniv = new HibernateSearchObject<Muniv>(Muniv.class);

        if(filters != null) {
            for (Filter filter : filters)
                soUniv.addFilter(filter);
        }

        soUniv.addSort("ckdUniv", false);
        pagingPgurTinggiS2.setPageSize(pageSize);
		pagingPgurTinggiS2.setDetailed(true);
		getPlwUniv().init(soUniv, listPgurTinggiS2, pagingPgurTinggiS2);
		listPgurTinggiS2.setItemRenderer(new PendidikanSearchUnivList());
    }

    public void onUnivItem(Event event) {
        Listitem item = null;

        if(_type.endsWith(Codec.Jenjang.Jen1.getValue())) item = listPgurTinggiS1.getSelectedItem();
        else if(_type.endsWith(Codec.Jenjang.Jen2.getValue())) item = listPgurTinggiS2.getSelectedItem();

        if (item != null) {
            Muniv anUnivS = (Muniv) item.getAttribute(MahasiswaKhususCtrl.LIST_DATA);

            if(_type.endsWith(Codec.Jenjang.Jen1.getValue())) {
                txtbCodeS1.setValue(anUnivS.getCkdUniv());
                txtbNameS1.setValue(anUnivS.getCnamaUniv());
                txtbStatusS1.setValue(String.valueOf(anUnivS.getCstatus()));
                getMhistpnddkmhsS1().setMuniv(anUnivS);
            }
            else if(_type.endsWith(Codec.Jenjang.Jen2.getValue())) {
                txtbCodeS2.setValue(anUnivS.getCkdUniv());
                txtbNameS2.setValue(anUnivS.getCnamaUniv());
                txtbStatusS2.setValue(String.valueOf(anUnivS.getCstatus()));
                getMhistpnddkmhsS2().setMuniv(anUnivS);
            }
        }

        if(_type.endsWith(Codec.Jenjang.Jen1.getValue())) cmbPgurTinggiS1.close();
        else if(_type.endsWith(Codec.Jenjang.Jen2.getValue())) cmbPgurTinggiS2.close();
    }

    /* Lov Program Studi */
    public void onOpen$cmbProgramS1(Event event) {
        this._type = Codec.Jenjang.Jen1.getValue();
        this.searchProgramS1(null);
    }

    public void onClick$buttonSearchProgramS1(Event event) {
        Filter filter1 = null;
        Filter filter2 = null;

        if (StringUtils.isNotEmpty(tbCodeProgramS1.getValue()))
            filter1 = new Filter("ckdprogst", "%" + tbCodeProgramS1.getValue() + "%", Filter.OP_ILIKE);

        if (StringUtils.isNotEmpty(tbNameProgramS1.getValue()))
            filter2 = new Filter("cnmprogst", "%" + tbNameProgramS1.getValue() + "%", Filter.OP_ILIKE);

        this.searchProgramS1(filter1, filter2);
    }

    public void onClick$buttonCloseProgramS1(Event event) {
        cmbProgramS1.close();
    }

    protected Listheader codeProgramS1;
    protected Listheader nameProgramS1;
    
    public void searchProgramS1(Filter... filters) {
        codeProgramS1.setSortAscending(new FieldComparator("ckdprogst",true));
        codeProgramS1.setSortDescending(new FieldComparator("ckdprogst",false));
        nameProgramS1.setSortAscending(new FieldComparator("cnmprogst",true));
        nameProgramS1.setSortDescending(new FieldComparator("cnmprogst",false));

        HibernateSearchObject<Mprodi> soProdi = new HibernateSearchObject<Mprodi>(Mprodi.class);

        if(filters != null) {
            for (Filter filter : filters)
                soProdi.addFilter(filter);
        }

        soProdi.addSort("ckdprogst", false);
        pagingProgramS1.setPageSize(pageSize);
		pagingProgramS1.setDetailed(true);
		getPlwProdi().init(soProdi, listProgramS1, pagingProgramS1);
		listProgramS1.setItemRenderer(new PendidikanSearchProdiList());
    }


    public void onOpen$cmbProgramS2(Event event) {
        this._type = Codec.Jenjang.Jen2.getValue();
        this.searchProgramS2(null);
    }

    public void onClick$buttonSearchProgramS2(Event event) {
        Filter filter1 = null;
        Filter filter2 = null;

        if (StringUtils.isNotEmpty(tbCodeProgramS2.getValue()))
            filter1 = new Filter("ckdprogst", "%" + tbCodeProgramS2.getValue() + "%", Filter.OP_ILIKE);

        if (StringUtils.isNotEmpty(tbNameProgramS2.getValue()))
            filter1 = new Filter("cnmprogst", "%" + tbNameProgramS2.getValue() + "%", Filter.OP_ILIKE);

        this.searchProgramS2(filter1, filter2);
    }

    public void onClick$buttonCloseProgramS2(Event event) {
        cmbProgramS2.close();
    }

    protected Listheader codeProgramS2;
    protected Listheader nameProgramS2;
    
    public void searchProgramS2(Filter... filters) {
        codeProgramS2.setSortAscending(new FieldComparator("ckdprogst",true));
        codeProgramS2.setSortDescending(new FieldComparator("ckdprogst",false));
        nameProgramS2.setSortAscending(new FieldComparator("cnmprogst",true));
        nameProgramS2.setSortDescending(new FieldComparator("cnmprogst",false));
        HibernateSearchObject<Mprodi> soProdi = new HibernateSearchObject<Mprodi>(Mprodi.class);

        if(filters != null) {
            for (Filter filter : filters)
                soProdi.addFilter(filter);
        }

        soProdi.addSort("ckdprogst", false);
        pagingProgramS2.setPageSize(pageSize);
		pagingProgramS2.setDetailed(true);
		getPlwProdi().init(soProdi, listProgramS2, pagingProgramS2);
		listProgramS2.setItemRenderer(new PendidikanSearchProdiList());
    }

    public void onProdiItem(Event event) {
        Listitem item = null;

        if(_type.endsWith(Codec.Jenjang.Jen1.getValue())) item = listProgramS1.getSelectedItem();
        else if(_type.endsWith(Codec.Jenjang.Jen2.getValue())) item = listProgramS2.getSelectedItem();

        if (item != null) {
            Mprodi anProdi = (Mprodi) item.getAttribute(MahasiswaKhususCtrl.LIST_DATA);

            if(_type.endsWith(Codec.Jenjang.Jen1.getValue())) {
                txtbCodeProgramS1.setValue(anProdi.getCkdprogst());
                txtbNamaProgramS1.setValue(anProdi.getCnmprogst());
                getMhistpnddkmhsS1().setMprodi(anProdi);
            }
            else if(_type.endsWith(Codec.Jenjang.Jen2.getValue())) {
                txtbCodeProgramS2.setValue(anProdi.getCkdprogst());
                txtbNamaProgramS2.setValue(anProdi.getCnmprogst());
                getMhistpnddkmhsS2().setMprodi(anProdi);
            }
        }

        if(_type.endsWith(Codec.Jenjang.Jen1.getValue())) cmbProgramS1.close();
        else if(_type.endsWith(Codec.Jenjang.Jen2.getValue())) cmbProgramS2.close();
    }

    /* Lov Jenjang */
    public void onOpen$cmbJenjangS1(Event event) {
        this._type = Codec.Jenjang.Jen1.getValue();
        this.searchJenjangS1(null);
    }

    public void onClick$buttonSearchJenjangS1(Event event) {
        Filter filter1 = null;
        Filter filter2 = null;
        
        if (StringUtils.isNotEmpty(tbCodeJenjangS1.getValue()))
            filter1 = new Filter("ckdjen", "%" + tbCodeJenjangS1.getValue() + "%", Filter.OP_ILIKE);

        if (StringUtils.isNotEmpty(tbNameJenjangS1.getValue()))
            filter2 = new Filter("cnmjen", "%" + tbNameJenjangS1.getValue() + "%", Filter.OP_ILIKE);

        this.searchJenjangS1(filter1, filter2);
    }

    public void onClick$buttonCloseJenjangS1(Event event) {
        cmbJenjangS1.close();
    }

    protected Listheader codeJenjangS1;
    protected Listheader nameJenjangS1;
    public void searchJenjangS1(Filter... filters) {
        codeJenjangS1.setSortAscending(new FieldComparator("ckdjen",true));
        codeJenjangS1.setSortDescending(new FieldComparator("ckdjen",false));
        nameJenjangS1.setSortAscending(new FieldComparator("cnmjen",true));
        nameJenjangS1.setSortDescending(new FieldComparator("cnmjen",false));

        HibernateSearchObject<Mjenjang> soJenjang = new HibernateSearchObject<Mjenjang>(Mjenjang.class);

        if(filters != null) {
            for (Filter filter : filters)
                soJenjang.addFilter(filter);
        }
        soJenjang.addFilter(new Filter("csingkat", Codec.Jenjang.Jen1.getValue(), Filter.OP_EQUAL));

        soJenjang.addSort("ckdjen", false);
        pagingJenjangS1.setPageSize(pageSize);
		pagingJenjangS1.setDetailed(true);
		getPlwJenjang().init(soJenjang, listJenjangS1, pagingJenjangS1);
		listJenjangS1.setItemRenderer(new PendidikanSearchJenjangList());
    }

    public void onOpen$cmbJenjangS2(Event event) {
        this._type = Codec.Jenjang.Jen2.getValue();
        this.searchJenjangS2(null);
    }

    public void onClick$buttonSearchJenjangS2(Event event) {
        Filter filter1 = null;
        Filter filter2 = null;

        if (StringUtils.isNotEmpty(tbCodeJenjangS2.getValue()))
            filter1 = new Filter("ckdjen", "%" + tbCodeJenjangS2.getValue() + "%", Filter.OP_ILIKE);

        if (StringUtils.isNotEmpty(tbNameJenjangS2.getValue()))
            filter2 = new Filter("cnmjen", "%" + tbNameJenjangS2.getValue() + "%", Filter.OP_ILIKE);

        this.searchJenjangS2(filter1, filter2);
    }

    public void onClick$buttonCloseJenjangS2(Event event) {
        cmbJenjangS2.close();
    }

    protected Listheader codeJenjangS2;
    protected Listheader nameJenjangS2; 
    public void searchJenjangS2(Filter... filters) {
        codeJenjangS2.setSortAscending(new FieldComparator("ckdjen",true));
        codeJenjangS2.setSortDescending(new FieldComparator("ckdjen",false));
        nameJenjangS2.setSortAscending(new FieldComparator("cnmjen",true));
        nameJenjangS2.setSortDescending(new FieldComparator("cnmjen",false));

        HibernateSearchObject<Mjenjang> soJenjang = new HibernateSearchObject<Mjenjang>(Mjenjang.class);

        if(filters != null) {
            for (Filter filter : filters)
                soJenjang.addFilter(filter);
        }
        soJenjang.addFilter(new Filter("csingkat", Codec.Jenjang.Jen2.getValue(), Filter.OP_EQUAL));

        soJenjang.addSort("ckdjen", false);
        pagingJenjangS2.setPageSize(pageSize);
		pagingJenjangS2.setDetailed(true);
		getPlwJenjang().init(soJenjang, listJenjangS2, pagingJenjangS2);
		listJenjangS2.setItemRenderer(new PendidikanSearchJenjangList());
    }

    public void onJenjangItem(Event event) {
        Listitem item = null;
        
        if(_type.endsWith(Codec.Jenjang.Jen1.getValue())) item = listJenjangS1.getSelectedItem();
        else if(_type.endsWith(Codec.Jenjang.Jen2.getValue())) item = listJenjangS2.getSelectedItem();

        if (item != null) {
            Mjenjang anJenjang = (Mjenjang) item.getAttribute(MahasiswaKhususCtrl.LIST_DATA);

            if(_type.endsWith(Codec.Jenjang.Jen1.getValue())) {
                txtbNamaJenjangS1.setValue(anJenjang.getCnmjen());
                getMhistpnddkmhsS1().setMjenjang(anJenjang);
            }
            else if(_type.endsWith(Codec.Jenjang.Jen2.getValue())) {
                txtbNamaJenjangS2.setValue(anJenjang.getCnmjen());
                getMhistpnddkmhsS2().setMjenjang(anJenjang);
            }
        }

        if(_type.endsWith(Codec.Jenjang.Jen1.getValue())) cmbJenjangS1.close();
        else if(_type.endsWith(Codec.Jenjang.Jen2.getValue())) cmbJenjangS2.close();
    }
    
    
    
    public void doFitSize() {
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue();
        final int maxListBoxHeight = height - 245;

        borderPendidikan.setHeight(String.valueOf(maxListBoxHeight) + "px");
        windowPendidikanDetail.invalidate();
    }

    public void reLoadPage() {
        this.doLoadData();
        binder.loadAll();
        
        this.doFitSize();
        this.doInitData();
    }

    public MahasiswaDetailCtrl getDetailCtrl() {
        return detailCtrl;
    }

    public void setDetailCtrl(MahasiswaDetailCtrl detailCtrl) {
        this.detailCtrl = detailCtrl;
    }

    public AnnotateDataBinder getBinder() {
        return binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public PagedListWrapper<Muniv> getPlwUniv() {
        return plwUniv;
    }

    public void setPlwUniv(PagedListWrapper<Muniv> plwUniv) {
        this.plwUniv = plwUniv;
    }

    public PagedListWrapper<Mprodi> getPlwProdi() {
        return plwProdi;
    }

    public void setPlwProdi(PagedListWrapper<Mprodi> plwProdi) {
        this.plwProdi = plwProdi;
    }

    public PagedListWrapper<Mjenjang> getPlwJenjang() {
        return plwJenjang;
    }

    public void setPlwJenjang(PagedListWrapper<Mjenjang> plwJenjang) {
        this.plwJenjang = plwJenjang;
    }

    public Mmahasiswa getMahasiswa() {
        return getDetailCtrl().getMainCtrl().getMahasiswa();
    }

    public void setMahasiswa(Mmahasiswa _obj) {
        getDetailCtrl().getMainCtrl().setMahasiswa(_obj);
    }

    public Mhistpnddkmhs getMhistpnddkmhsS1() {
        return mhistpnddkmhsS1;
    }

    public void setMhistpnddkmhsS1(Mhistpnddkmhs mhistpnddkmhsS1) {
        this.mhistpnddkmhsS1 = mhistpnddkmhsS1;
    }

    public Mhistpnddkmhs getMhistpnddkmhsS2() {
        return mhistpnddkmhsS2;
    }

    public void setMhistpnddkmhsS2(Mhistpnddkmhs mhistpnddkmhsS2) {
        this.mhistpnddkmhsS2 = mhistpnddkmhsS2;
    }
}
