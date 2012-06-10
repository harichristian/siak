/**
 * Copyright 2010 the original author or authors.
 *
 * This file is part of Zksample2. http://zksample2.sourceforge.net/
 *
 * Zksample2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Zksample2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Zksample2.  If not, see <http://www.gnu.org/licenses/gpl.html>.
 */
package id.ac.idu.webui.administrasi.alumni;

import id.ac.idu.administrasi.service.BidangUsahaService;
import id.ac.idu.administrasi.service.HistKerjaService;
import id.ac.idu.administrasi.service.JabatanService;
import id.ac.idu.administrasi.service.MalumniService;
import id.ac.idu.backend.model.Malumni;
import id.ac.idu.backend.model.Mbidangusaha;
import id.ac.idu.backend.model.Mjabatan;
import id.ac.idu.backend.model.Thistkerja;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.ListBoxUtil;
import id.ac.idu.webui.util.ZksampleMessageUtils;
import id.ac.idu.webui.util.searchdialogs.BidangUsahaExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.JabatanExtendedSearchListBox;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 * This is the controller class for the /WEB-INF/pages/malumni/malumniList.zul
 * file.<br>
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br>
 *
 * @author bbruhns
 * @author sgerth
 * @changes 05/15/2009: sge Migrating the list models for paging. <br>
 * 07/24/2009: sge changings for clustering.<br>
 * 10/12/2009: sge changings in the saving routine.<br>
 * 11/07/2009: bbr changed to extending from GFCBaseCtrl<br>
 * (GenericForwardComposer) for spring-managed creation.<br>
 * 07/04/2010: sge modified for zk5.x with complete Annotated
 * Databinding.<br>
 */
public class MalumniPekerjaanCtrl extends GFCBaseCtrl implements Serializable {

    private static final long serialVersionUID = -8352659530536077973L;
    private static final Logger logger = Logger.getLogger(MalumniPekerjaanCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowMalumniPekerjaan; // autowired

    protected Borderlayout borderlayout_MalumniPekerjaan; // autowired



    // Databinding
    protected transient AnnotateDataBinder binder;
    private MalumniMainCtrl malumniMainCtrl;
    private MalumniPekerjaanCtrl detailCtrl;

    // ServiceDAOs / Domain Classes
    private transient MalumniService malumniService;
    protected Listbox listBoxAlumniPekerjaan;
    private transient BidangUsahaService bidangUsahaService;
    private transient JabatanService jabatanService;
    private transient HistKerjaService histKerjaService;


    /**
     * default constructor.<br>
     */
    public MalumniPekerjaanCtrl() {
        super();
    }

    @Override
    public void doAfterCompose(Component window) throws Exception {
        super.doAfterCompose(window);

        /**
         * 1. Set an 'alias' for this composer name to access it in the
         * zul-file.<br>
         * 2. Set the parameter 'recurse' to 'false' to avoid problems with
         * managing more than one zul-file in one page. Otherwise it would be
         * overridden and can ends in curious error messages.
         */
        this.self.setAttribute("controller", this, false);

        /**
         * 1. Get the overhanded MainController.<br>
         * 2. Set this controller in the MainController.<br>
         * 3. Check if a 'selectedObject' exists yet in the MainController.<br>
         */
        if (arg.containsKey("ModuleMainController")) {
            setMalumniMainCtrl((MalumniMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getMalumniMainCtrl().setMalumniPekerjaanCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getMalumniMainCtrl().getSelectedMalumni() != null) {
                setSelectedMalumni(getMalumniMainCtrl().getSelectedMalumni());
            }
        }
    }


    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // +++++++++++++++ Component Events ++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    /**
     * Automatically called method from zk.
     *
     * @param event
     * @throws Exception
     */
    public void onCreate$windowMalumniPekerjaan(Event event) throws Exception {
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);
        binder.loadAll();
        doRenderList();
        doFitSize(event);
    }

    public void doRenderList(){
        ListBoxUtil.resetList(listBoxAlumniPekerjaan);
        if(getMalumni()!=null){
            if(getMalumni().getId() != 0){
                List<Thistkerja> listHist = getHistKerjaService().getAllHIstkerjaByAlumni(getMalumni());
                for (int i=0;listHist.size() > i;i++){
                    createRow(listHist.get(i));
                }
            }
        }
    }

    public void createRow(Thistkerja histkerja){

       Listitem ltm = new Listitem();
       ltm.setParent(listBoxAlumniPekerjaan);
       Listcell listcell = new Listcell();
       Textbox hisId  =  new Textbox();
       hisId.setVisible(false);
       hisId.setParent(listcell);
       hisId.setValue(Integer.valueOf(histkerja.getId()).toString());
       Checkbox checkbox = new Checkbox();
       checkbox.setId("c"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       checkbox.setParent(listcell);
       checkbox.setChecked(false);
       listcell.setParent(ltm);


       listcell = new Listcell();
       Textbox noUrut =  new Textbox();
       noUrut.setValue(""+histkerja.getNnourut());
       noUrut.setId("u" + String.valueOf(listBoxAlumniPekerjaan.getItemCount()) + "1");
       noUrut.setWidth("50px");
       noUrut.setParent(listcell);
       noUrut.setReadonly(true);
       listcell.setParent(ltm);

       listcell = new Listcell();
       Combobox jenis = new Combobox();
       jenis.setId("j"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       jenis.setWidth("100px");
//       jenis.setMold("rounded");
       Comboitem  negeri = new Comboitem();
       negeri.setId("N"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       negeri.setLabel("Negeri");
       negeri.setParent(jenis);
//       jenis.appendItem("Negeri");
       Comboitem  swasta = new Comboitem();
       swasta.setId("S"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       swasta.setParent(jenis);
       swasta.setLabel("Swasta");
//       jenis.appendItem("Swasta");
       if (String.valueOf(histkerja.getCjnsinstansi()).equals("1")) {
            jenis.setSelectedIndex(1);
        } else {
            jenis.setSelectedIndex(0);
        }
       jenis.setParent(listcell);
       jenis.setDisabled(true);
       listcell.setParent(ltm);

        listcell = new Listcell();
       Textbox nama =  new Textbox();
       nama.setId("n"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       nama.setWidth("150px");
       nama.setParent(listcell);
       nama.setMaxlength(50);
       nama.setValue(histkerja.getCnminstansi());
       nama.setReadonly(true);
       listcell.setParent(ltm);

       listcell = new Listcell();
       Textbox alamat =  new Textbox();
       alamat.setId("a"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       alamat.setWidth("250px");
       alamat.setMaxlength(100);
       alamat.setValue(histkerja.getCalinstansi());
       alamat.setParent(listcell);
       alamat.setReadonly(true);
       listcell.setParent(ltm);

       listcell = new Listcell();
       final Textbox usaha =  new Textbox();
       final Textbox usahaId =  new Textbox();
       usaha.setId("us"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       usahaId.setId("usid"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       usahaId.setVisible(false);
       usahaId.setValue(Integer.valueOf(histkerja.getMbidangusaha().getId()).toString());
       usahaId.setParent(listcell);
       usaha.setWidth("135px");
       usaha.setValue(histkerja.getMbidangusaha().getCnmbidusaha());
       usaha.setParent(listcell);
       usaha.setReadonly(true);
       listcell.setParent(ltm);
       Button btnUsaha = new Button();
       btnUsaha.setId("bu"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       btnUsaha.setLabel("...");
       btnUsaha.setWidth("15px");
       btnUsaha.addEventListener("onClick", new org.zkoss.zk.ui.event.EventListener() {
           public void onEvent(Event event) throws Exception {
               Mbidangusaha ush = BidangUsahaExtendedSearchListBox.show(windowMalumniPekerjaan);
               if (ush != null) {
                   usaha.setValue(ush.getCnmbidusaha());
                   usahaId.setValue("" + ush.getId());
               }
           }
       });
       btnUsaha.setDisabled(true);
       btnUsaha.setParent(listcell);

       listcell = new Listcell();
       final Textbox jabatan =  new Textbox();
       final Textbox jabatanId =  new Textbox();
       jabatan.setId("jb"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       jabatan.setValue(getJabatanService().getJabatanByCode(histkerja.getCkdkerja()).getCnmjabatan());
       jabatanId.setId("jbid"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       jabatanId.setVisible(false);
       jabatanId.setValue(""+getJabatanService().getJabatanByCode(histkerja.getCkdkerja()).getCkdjabatan());
       jabatanId.setParent(listcell);
       jabatan.setWidth("105px");
       jabatan.setParent(listcell);
       jabatan.setReadonly(true);
       listcell.setParent(ltm);
       Button btnJabatan = new Button();
       btnJabatan.setId("bj"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       btnJabatan.setLabel("...");
       btnJabatan.setWidth("15px");
       btnJabatan.addEventListener("onClick", new org.zkoss.zk.ui.event.EventListener() {
           public void onEvent(Event event) throws Exception {
               Mjabatan jbt = JabatanExtendedSearchListBox.show(windowMalumniPekerjaan);
               if (jbt != null) {
                   jabatan.setValue(jbt.getCnmjabatan());
                   jabatanId.setValue(jbt.getCkdjabatan());
               }
           }
       });
       btnJabatan.setDisabled(true);
       btnJabatan.setParent(listcell);

        listcell = new Listcell();
       Combobox range = new Combobox();
       range.setId("rg"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       range.setWidth("130px");
       range.appendItem(" < Rp. 2jt");
       range.appendItem("Rp. 2jt s/d 5jt");
       range.appendItem("Rp. 5jt s/d 10jt");
       range.appendItem("Rp. 10jt s/d 15 jt");
       range.appendItem(" > Rp. 15jt");
       range.setSelectedIndex(Integer.valueOf(histkerja.getCkdgaji().trim()));
       range.setDisabled(true);
       range.setParent(listcell);
       listcell.setParent(ltm);


    }



    public void doNew(){


       Listitem ltm = new Listitem();
       ltm.setParent(listBoxAlumniPekerjaan);
       Listcell listcell = new Listcell();
       Textbox hisId  =  new Textbox();
       hisId.setVisible(false);
       hisId.setParent(listcell);
       Checkbox checkbox = new Checkbox();
       checkbox.setId("c"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       checkbox.setParent(listcell);
       checkbox.setChecked(true);
       listcell.setParent(ltm);


       listcell = new Listcell();
       Textbox noUrut =  new Textbox();

       noUrut.setId("u"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       noUrut.setWidth("50px");
       noUrut.setParent(listcell);
       listcell.setParent(ltm);

       listcell = new Listcell();
       Combobox jenis = new Combobox();
       jenis.setId("j"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       jenis.setWidth("100px");
//       jenis.setMold("rounded");
       Comboitem  negeri = new Comboitem();
       negeri.setId("N"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       negeri.setLabel("Negeri");
       negeri.setParent(jenis);
//       jenis.appendItem("Negeri");
       Comboitem  swasta = new Comboitem();
       swasta.setId("S"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       swasta.setParent(jenis);
       swasta.setLabel("Swasta");
//       jenis.appendItem("Swasta");
       jenis.setParent(listcell);
       listcell.setParent(ltm);

        listcell = new Listcell();
       Textbox nama =  new Textbox();
       nama.setId("n"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       nama.setWidth("150px");
       nama.setParent(listcell);
       nama.setMaxlength(50);
       listcell.setParent(ltm);

       listcell = new Listcell();
       Textbox alamat =  new Textbox();
       alamat.setId("a"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       alamat.setWidth("250px");
       alamat.setMaxlength(100);
       alamat.setParent(listcell);
       listcell.setParent(ltm);

       listcell = new Listcell();
       final Textbox usaha =  new Textbox();
       final Textbox usahaId =  new Textbox();
       usaha.setId("us"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       usahaId.setId("usid"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       usahaId.setVisible(false);
       usahaId.setParent(listcell);
       usaha.setWidth("135px");
       usaha.setParent(listcell);
       usaha.setReadonly(true);
       listcell.setParent(ltm);
       Button btnUsaha = new Button();
       btnUsaha.setId("bu"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       btnUsaha.setLabel("...");
       btnUsaha.setWidth("15px");
       btnUsaha.addEventListener("onClick",new org.zkoss.zk.ui.event.EventListener() {
           	  public void onEvent(Event event) throws Exception {
                     Mbidangusaha ush = BidangUsahaExtendedSearchListBox.show(windowMalumniPekerjaan);
                     if (ush!=null) {
                         usaha.setValue(ush.getCnmbidusaha());
                         usahaId.setValue(""+ush.getId());
                     }
         	   }
      	  });
       btnUsaha.setParent(listcell);

       listcell = new Listcell();
       final Textbox jabatan =  new Textbox();
       final Textbox jabatanId =  new Textbox();
       jabatan.setId("jb"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       jabatanId.setId("jbid"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       jabatanId.setVisible(false);
       jabatanId.setParent(listcell);
       jabatan.setWidth("105px");
       jabatan.setParent(listcell);
       jabatan.setReadonly(true);
       listcell.setParent(ltm);
       Button btnJabatan = new Button();
       btnJabatan.setId("bj"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       btnJabatan.setLabel("...");
       btnJabatan.setWidth("15px");
       btnJabatan.addEventListener("onClick",new org.zkoss.zk.ui.event.EventListener() {
           	  public void onEvent(Event event) throws Exception {
                     Mjabatan jbt = JabatanExtendedSearchListBox.show(windowMalumniPekerjaan);
                     if (jbt!=null) {
                         jabatan.setValue(jbt.getCnmjabatan());
                         jabatanId.setValue(jbt.getCkdjabatan());
                     }
         	   }
      	  });
       btnJabatan.setParent(listcell);

        listcell = new Listcell();
       Combobox range = new Combobox();
       range.setId("rg"+String.valueOf(listBoxAlumniPekerjaan.getItemCount())+"1");
       range.setWidth("130px");
       range.appendItem(" < Rp. 2jt");
       range.appendItem("Rp. 2jt s/d 5jt");
       range.appendItem("Rp. 5jt s/d 10jt");
       range.appendItem("Rp. 10jt s/d 15 jt");
        range.appendItem(" > Rp. 15jt");
       range.setParent(listcell);
       listcell.setParent(ltm);


    }

    private List<Thistkerja> convertLtmtomodel() throws InterruptedException {
        List<Thistkerja> list = new ArrayList<Thistkerja>();

        for (int i=0; i < listBoxAlumniPekerjaan.getItems().size();i++){
            Thistkerja hiskerja = new Thistkerja();
            Listitem ltm = new Listitem();
            ltm = (Listitem) listBoxAlumniPekerjaan.getItemAtIndex(i);
            Listcell lc1 = (Listcell) ltm.getChildren().get(0);
            Textbox histId = (Textbox) lc1.getChildren().get(0);

            Checkbox check = (Checkbox) lc1.getChildren().get(1);
            if (check.isChecked()) {
                if (histId.getValue()!=null && (!histId.getValue().equals("")) && histId.getValue().length()>0 ){
                     hiskerja.setId(Integer.valueOf(histId.getValue()).intValue());
                }
                Listcell lc2 = (Listcell) ltm.getChildren().get(1);
                String nourut = ((Textbox) lc2.getChildren().get(0)).getValue();
                if (isInteger(nourut)){
                    hiskerja.setNnourut(Integer.valueOf(nourut).intValue());
                }else {
                      ZksampleMessageUtils.showErrorMessage("No Urut : "+nourut+" harus di isi oleh angka.");

                }
                String jenis = ""+((Combobox) ((Listcell) ltm.getChildren().get(2)).getChildren().get(0)).getSelectedIndex();
                hiskerja.setCjnsinstansi(jenis.charAt(0));
                String nama = ((Textbox) ((Listcell) ltm.getChildren().get(3)).getChildren().get(0)).getValue();
                hiskerja.setCnminstansi(nama);
                String alamat = ((Textbox) ((Listcell) ltm.getChildren().get(4)).getChildren().get(0)).getValue();
                hiskerja.setCalinstansi(alamat);
                String bidUsaha = ((Textbox) ((Listcell) ltm.getChildren().get(5)).getChildren().get(0)).getValue();
                hiskerja.setMbidangusaha(getBidangUsahaService().getBidangusahaByID(Integer.valueOf(bidUsaha).intValue()));
                String jabatan = ((Textbox) ((Listcell) ltm.getChildren().get(6)).getChildren().get(0)).getValue();
                hiskerja.setCkdkerja(jabatan);
                String gaji = ""+((Combobox) ((Listcell) ltm.getChildren().get(7)).getChildren().get(0)).getSelectedIndex();
                hiskerja.setCkdgaji(gaji);
                list.add(hiskerja);
            }
        }


        return list;
    }

    public boolean isInteger(String str) {
        return str.matches("^-?[0-9]+(\\.[0-9]+)?$");
    }

    public List<Thistkerja> getThistKerjaList(Malumni malumni) throws InterruptedException {
        List<Thistkerja> list = new ArrayList<Thistkerja>();

        list = convertLtmtomodel();
        for (int i=0;i < list.size();i++){
            list.get(i).setMalumni(malumni);
        }

        return list;
    }
    /**
     * Recalculates the container size for this controller and resize them.
     * <p/>
     * Calculate how many rows have been place in the listbox. Get the
     * currentDesktopHeight from a hidden Intbox from the index.zul that are
     * filled by onClientInfo() in the indexCtroller.
     */
    public void doFitSize(Event event) {
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
        final int maxListBoxHeight = height - 148;
        borderlayout_MalumniPekerjaan.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowMalumniPekerjaan.invalidate();
    }


    public void onClick$button_close(Event event) {

	}
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++ Setter/Getter ++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    /**
     * Best Pratice Hint:<br>
     * The setters/getters for the local annotated data binded Beans/Sets are
     * administered in the module's mainController. Working in this way you have
     * clean line to share this beans/sets with other controllers.
     */
    public Malumni getMalumni() {
        // STORED IN THE module's MainController
        return getMalumniMainCtrl().getSelectedMalumni();
    }

    public void setMalumni(Malumni anMalumni) {
        // STORED IN THE module's MainController
        getMalumniMainCtrl().setSelectedMalumni(anMalumni);
    }

    public Malumni getSelectedMalumni() {
        // STORED IN THE module's MainController
        return getMalumniMainCtrl().getSelectedMalumni();
    }

    public void setSelectedMalumni(Malumni selectedMalumni) {
        // STORED IN THE module's MainController
        getMalumniMainCtrl().setSelectedMalumni(selectedMalumni);
    }

    public BindingListModelList getMalumnis() {
        // STORED IN THE module's MainController
        return getMalumniMainCtrl().getMalumnis();
    }

    public void setMalumnis(BindingListModelList malumnis) {
        // STORED IN THE module's MainController
        getMalumniMainCtrl().setMalumnis(malumnis);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public void setMalumniService(MalumniService malumniService) {
        this.malumniService = malumniService;
    }

    public MalumniService getMalumniService() {
        return this.malumniService;
    }

    public BidangUsahaService getBidangUsahaService() {
        return bidangUsahaService;
    }

    public void setBidangUsahaService(BidangUsahaService bidangUsahaService) {
        this.bidangUsahaService = bidangUsahaService;
    }

    public JabatanService getJabatanService() {
        return jabatanService;
    }

    public void setJabatanService(JabatanService jabatanService) {
        this.jabatanService = jabatanService;
    }

    public HistKerjaService getHistKerjaService() {
        return histKerjaService;
    }

    public void setHistKerjaService(HistKerjaService histKerjaService) {
        this.histKerjaService = histKerjaService;
    }

    public void setMalumniMainCtrl(MalumniMainCtrl malumniMainCtrl) {
        this.malumniMainCtrl = malumniMainCtrl;
    }

    public MalumniMainCtrl getMalumniMainCtrl() {
        return this.malumniMainCtrl;
    }


    public MalumniPekerjaanCtrl getDetailCtrl() {
        return detailCtrl;
    }

    public void setDetailCtrl(MalumniPekerjaanCtrl detailCtrl) {
        this.detailCtrl = detailCtrl;
    }


}
