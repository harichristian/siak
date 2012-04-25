package id.ac.idu.webui.irs.txPaket;

import com.trg.search.Filter;
import id.ac.idu.administrasi.service.MahasiswaService;
import id.ac.idu.backend.model.*;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.irs.service.IrsService;
import id.ac.idu.util.Codec;
import id.ac.idu.webui.irs.cutimhs.model.OrderSearchMahasiswaList;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.pagging.PagedListWrapper;
import id.ac.idu.webui.util.searchdialogs.MatakuliahExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.ProdiExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.SekolahExtendedSearchListBox;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.*;

import java.io.Serializable;

/**
 * @author <a href="valeo.gumilang@gmail.com">Valeo Gumilang</a>
 * @Date 4/9/12
 * Time: 9:01 AM
 */
public class TxDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -1L;
        private static final Logger logger = Logger.getLogger(TxDetailCtrl.class);

        /*
         * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
         * All the components that are defined here and have a corresponding
         * component with the same 'id' in the zul-file are getting autowired by our
         * 'extends GFCBaseCtrl' GenericForwardComposer.
         * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
         */
        protected Window windowTxDetail; // autowired

        protected Borderlayout borderlayout_TxDetail; // autowired

        //protected Textbox txtb_nim;         //lookup
        //protected Textbox txtb_nama;        //lookup
        protected Textbox txtb_sekolah;     //lookup
        protected Button btnSearchSekolahExtended;
        protected Textbox txtb_kodeprodi;
        protected Textbox txtb_prodi;
        protected Button btnSearchProdiExtended;       //lookup
        protected Textbox txtb_jenjang;     //lookup

        protected Textbox txtb_term;        //header
        protected Textbox txtb_thajar;      //header
        protected Textbox txtb_semester;    //header

        protected Textbox txtb_matakuliah;  //detail
        protected Button btnSearchMatakuliahExtended;
        protected Textbox txtb_kelompok;    //detail
        protected Button button_IrsDialog_PrintIrs; // autowired

        // POPUP variables
        protected Textbox txtb_nim;
        protected Textbox txtb_nimTo;
        protected Textbox txtb_nama;
        protected Textbox txtb_namaTo;
        protected Intbox txtb_mhsid;
        protected Bandbox bandbox_Dialog_MahasiswaSearch;
        protected Bandbox bandbox_Dialog_MahasiswaSearchTo;
        protected Paging paging_MahasiswaSearchList;
        protected Paging paging_MahasiswaSearchListTo;
        private transient PagedListWrapper<Mmahasiswa> plwMahasiswa;
        private transient PagedListWrapper<Mmahasiswa> plwMahasiswaTo;
        protected Listbox listMahasiswaSearch;
        protected Listbox listMahasiswaSearchTo;
        private int pageSize;

        protected Textbox tb_Nim;
        protected Textbox tb_Nama;
        protected Textbox tb_NoKtp;
        protected Textbox tb_NimTo;
        protected Textbox tb_NamaTo;
        protected Textbox tb_NoKtpTo;

        // Databinding
        protected transient AnnotateDataBinder binder;
        private TxMainCtrl txMainCtrl;

        // ServiceDAOs / Domain Classes
        private transient IrsService irsService;
        private transient MahasiswaService popupMhsService;

        /**
         * default constructor.<br>
         */
        public TxDetailCtrl() {
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
                setTxMainCtrl((TxMainCtrl) arg.get("ModuleMainController"));

                // SET THIS CONTROLLER TO THE module's Parent/MainController
                getTxMainCtrl().setTxDetailCtrl(this);

                // Get the selected object.
                // Check if this Controller if created on first time. If so,
                // than the selectedXXXBean should be null
                if (getTxMainCtrl().getSelectedIrs() != null) {
                    setSelectedIrs(getTxMainCtrl().getSelectedIrs());
                } else
                    setSelectedIrs(null);
            } else {
                setSelectedIrs(null);
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
        public void onCreate$windowTxDetail(Event event) throws Exception {
            setPageSize(20);
            binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);

            binder.loadAll();

            doFitSize(event);
        }

        // +++++++++++++++++++++++++++++++++++++++++++++++++ //
        // +++++++++++++++++ Business Logic ++++++++++++++++ //
        // +++++++++++++++++++++++++++++++++++++++++++++++++ //

        // +++++++++++++++++++++++++++++++++++++++++++++++++ //
        // ++++++++++++++++++++ Helpers ++++++++++++++++++++ //
        // +++++++++++++++++++++++++++++++++++++++++++++++++ //

        /**
         * Recalculates the container size for this controller and resize them.
         *
         * Calculate how many rows have been place in the listbox. Get the
         * currentDesktopHeight from a hidden Intbox from the index.zul that are
         * filled by onClientInfo() in the indexCtroller.
         */
        public void doFitSize(Event event) {
            final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
            final int maxListBoxHeight = height - 148;
            borderlayout_TxDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

            windowTxDetail.invalidate();
        }

        /**
         * Set all components in readOnly/disabled modus.
         *
         * true = all components are readOnly or disabled.<br>
         * false = all components are accessable.<br>
         *
         * @param b
         */
        public void doReadOnlyMode(boolean b) {
            //txtb_nim.setReadonly(b);
            //txtb_mahasiswa.setReadonly(b);
            //txtb_sekolah.setReadonly(b);
            //txtb_prodi.setReadonly(b);
            txtb_thajar.setReadonly(b);
            txtb_term.setReadonly(b);
            //txtb_kelompok.setReadonly(b);
            //txtb_semester.setReadonly(b);
            bandbox_Dialog_MahasiswaSearch.setDisabled(b);
            bandbox_Dialog_MahasiswaSearchTo.setDisabled(b);
            btnSearchSekolahExtended.setDisabled(b);
            btnSearchProdiExtended.setDisabled(b);
            //btnSearchMatakuliahExtended.setDisabled(b);
        }

        public void onClick$btnSearchProdiExtended(Event event) {
            doSearchProdiExtended(event);
        }

        private void doSearchProdiExtended(Event event) {
            Mprodi prodi = ProdiExtendedSearchListBox.show(windowTxDetail);

            if (prodi != null) {
                txtb_kodeprodi.setValue(prodi.getCkdprogst());
                txtb_prodi.setValue(prodi.getCnmprogst());
                Tirspasca obj = getIrs();
                obj.setMprodi(prodi);
                setIrs(obj);
            }
        }

        public void onClick$btnSearchSekolahExtended(Event event) {
            doSearchSekolahExtended(event);
        }

        private void doSearchSekolahExtended(Event event) {
            Msekolah sekolah = SekolahExtendedSearchListBox.show(windowTxDetail);

            if (sekolah != null) {
                txtb_sekolah.setValue(sekolah.getCnamaSekolah());
                Tirspasca obj = getIrs();
                obj.setMsekolah(sekolah);
                setIrs(obj);
            }
        }

        public void onClick$btnSearchMatakuliahExtended(Event event) {
            doSearchMatakuliahExtended(event);
        }

        private void doSearchMatakuliahExtended(Event event) {
            Mtbmtkl matkul = MatakuliahExtendedSearchListBox.show(windowTxDetail);

            if (matkul != null) {
                txtb_matakuliah.setValue(matkul.getCnamamk());
                Tirspasca obj = getIrs();
                obj.setMtbmtkl(matkul);
                setIrs(obj);
            }
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
        public Tirspasca getIrs() {
            // STORED IN THE module's MainController
            return getTxMainCtrl().getSelectedIrs();
        }

        public void setIrs(Tirspasca anIrs) {
            // STORED IN THE module's MainController
            getTxMainCtrl().setSelectedIrs(anIrs);
        }

        public Tirspasca getSelectedIrs() {
            // STORED IN THE module's MainController
            return getTxMainCtrl().getSelectedIrs();
        }

        public void setSelectedIrs(Tirspasca selectedIrs) {
            // STORED IN THE module's MainController
            getTxMainCtrl().setSelectedIrs(selectedIrs);
        }

        public BindingListModelList getTxs() {
            // STORED IN THE module's MainController
            return getTxMainCtrl().getTxs();
        }

        public void setTxs(BindingListModelList txs) {
            // STORED IN THE module's MainController
            getTxMainCtrl().setTxs(txs);
        }

        public AnnotateDataBinder getBinder() {
            return this.binder;
        }

        public void setBinder(AnnotateDataBinder binder) {
            this.binder = binder;
        }

        public void setIrsService(IrsService irsService) {
            this.irsService = irsService;
        }

        public IrsService getIrsService() {
            return this.irsService;
        }

        public void setTxMainCtrl(TxMainCtrl txMainCtrl) {
            this.txMainCtrl = txMainCtrl;
        }

        public TxMainCtrl getTxMainCtrl() {
            return this.txMainCtrl;
        }

        //POPUP SECTION
        public PagedListWrapper<Mmahasiswa> getPlwMahasiswa() {
            return plwMahasiswa;
        }

        public PagedListWrapper<Mmahasiswa> getPlwMahasiswaTo() {
            return plwMahasiswaTo;
        }

        public MahasiswaService getPopupMhsService() {
            return popupMhsService;
        }

        public void setPopupMhsService(MahasiswaService popupMhsService) {
            this.popupMhsService = popupMhsService;
        }

        public void setPlwMahasiswa(PagedListWrapper<Mmahasiswa> plwMahasiswa) {
            this.plwMahasiswa = plwMahasiswa;
        }

        public void setPlwMahasiswaTo(PagedListWrapper<Mmahasiswa> plwMahasiswaTo) {
            this.plwMahasiswaTo = plwMahasiswaTo;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public void onOpen$bandbox_Dialog_MahasiswaSearch(Event event) {
            HibernateSearchObject<Mmahasiswa> so = new HibernateSearchObject<Mmahasiswa>(Mmahasiswa.class);
            so.addFilter(new Filter("mstatusmhs.ckdstatmhs", Codec.StatusMahasiswa.Status1.getValue(), com.trg.search.Filter.OP_EQUAL));
            so.addSort("cnim", false);

            paging_MahasiswaSearchList.setPageSize(pageSize);
            paging_MahasiswaSearchList.setDetailed(true);
            getPlwMahasiswa().init(so, listMahasiswaSearch, paging_MahasiswaSearchList);
            listMahasiswaSearch.setItemRenderer(new OrderSearchMahasiswaList());
        }
        public void onClick$button_bbox_Search(Event event) {
            Filter filter1 = null;
            Filter filter2 = null;
            Filter filter3 = null;

            if (StringUtils.isNotEmpty(tb_Nim.getValue()))
                filter1 = new Filter("cnim", "%" + tb_Nim.getValue() + "%", Filter.OP_LIKE);

            if (StringUtils.isNotEmpty(tb_Nama.getValue()))
                filter2 = new Filter("cnama", "%" + tb_Nama.getValue() + "%", Filter.OP_LIKE);

            if (StringUtils.isNotEmpty(tb_NoKtp.getValue()))
                filter3 = new Filter("noktp", "%" + tb_NoKtp.getValue() + "%", Filter.OP_LIKE);

            this.searchMahasiswa(filter1, filter2, filter3);
        }

        public void searchMahasiswa(Filter... filters) {

            HibernateSearchObject<Mmahasiswa> so = new HibernateSearchObject<Mmahasiswa>(Mmahasiswa.class);
            so.addFilter(new Filter("mstatusmhs.ckdstatmhs", Codec.StatusMahasiswa.Status1.getValue(), com.trg.search.Filter.OP_EQUAL));
            if(filters != null) {
                for(Filter anFilter : filters) {
                    if(anFilter != null) so.addFilter(anFilter);
                }
            }

            so.addSort("cnim", false);

            paging_MahasiswaSearchList.setPageSize(pageSize);
            paging_MahasiswaSearchList.setDetailed(true);
            getPlwMahasiswa().init(so, listMahasiswaSearch, paging_MahasiswaSearchList);
            listMahasiswaSearch.setItemRenderer(new OrderSearchMahasiswaList());
        }
        public void onClick$button_bbox_Close(Event event) {
            bandbox_Dialog_MahasiswaSearch.close();
        }

        public void onMahasiswaItem(Event event) {
            Listitem item = listMahasiswaSearch.getSelectedItem();
            if (item != null) {
                Mmahasiswa aMahasiswa = (Mmahasiswa) item.getAttribute("data");
                getTxMainCtrl().setMahasiswa(aMahasiswa);
                if (aMahasiswa != null) {
                txtb_nim.setValue(aMahasiswa.getCnim());
                txtb_nama.setValue(aMahasiswa.getCnama());
                Tirspasca obj = getIrs();
                obj.setMmahasiswa(aMahasiswa);
                setIrs(obj);
                bandbox_Dialog_MahasiswaSearch.setValue(getTxMainCtrl().getMahasiswa().getCnim());
                }
            }
            bandbox_Dialog_MahasiswaSearch.close();
            Listitem itemTo = listMahasiswaSearchTo.getSelectedItem();
            if (itemTo != null) {
                Mmahasiswa aMahasiswa = (Mmahasiswa) itemTo.getAttribute("data");
                //getTxMainCtrl().setMahasiswa(aMahasiswa);
                getTxMainCtrl().setMahasiswaTo(aMahasiswa);
                if (aMahasiswa != null) {
                txtb_nimTo.setValue(aMahasiswa.getCnim());
                txtb_namaTo.setValue(aMahasiswa.getCnama());
                Tirspasca obj = getIrs();
                //obj.setMmahasiswa(aMahasiswa);
                setIrs(obj);
                bandbox_Dialog_MahasiswaSearchTo.setValue(getTxMainCtrl().getMahasiswaTo().getCnim());
                }
            }
            bandbox_Dialog_MahasiswaSearchTo.close();
        }

        public void onOpen$bandbox_Dialog_MahasiswaSearchTo(Event event) {
            HibernateSearchObject<Mmahasiswa> so = new HibernateSearchObject<Mmahasiswa>(Mmahasiswa.class);
            so.addFilter(new Filter("mstatusmhs.ckdstatmhs", Codec.StatusMahasiswa.Status1.getValue(), com.trg.search.Filter.OP_EQUAL));
            so.addSort("cnim", false);

            paging_MahasiswaSearchListTo.setPageSize(pageSize);
            paging_MahasiswaSearchListTo.setDetailed(true);
            getPlwMahasiswaTo().init(so, listMahasiswaSearchTo, paging_MahasiswaSearchListTo);
            listMahasiswaSearchTo.setItemRenderer(new OrderSearchMahasiswaList());
        }
        public void onClick$button_bbox_SearchTo(Event event) {
            Filter filter1 = null;
            Filter filter2 = null;
            Filter filter3 = null;

            if (StringUtils.isNotEmpty(tb_NimTo.getValue()))
                filter1 = new Filter("cnim", "%" + tb_NimTo.getValue() + "%", Filter.OP_LIKE);

            if (StringUtils.isNotEmpty(tb_NamaTo.getValue()))
                filter2 = new Filter("cnama", "%" + tb_NamaTo.getValue() + "%", Filter.OP_LIKE);

            if (StringUtils.isNotEmpty(tb_NoKtpTo.getValue()))
                filter3 = new Filter("noktp", "%" + tb_NoKtpTo.getValue() + "%", Filter.OP_LIKE);

            this.searchMahasiswaTo(filter1, filter2, filter3);
        }

        public void searchMahasiswaTo(Filter... filters) {

            HibernateSearchObject<Mmahasiswa> so = new HibernateSearchObject<Mmahasiswa>(Mmahasiswa.class);
            so.addFilter(new Filter("mstatusmhs.ckdstatmhs", Codec.StatusMahasiswa.Status1.getValue(), com.trg.search.Filter.OP_EQUAL));
            if(filters != null) {
                for(Filter anFilter : filters) {
                    if(anFilter != null) so.addFilter(anFilter);
                }
            }

            so.addSort("cnim", false);

            paging_MahasiswaSearchListTo.setPageSize(pageSize);
            paging_MahasiswaSearchListTo.setDetailed(true);
            getPlwMahasiswa().init(so, listMahasiswaSearchTo, paging_MahasiswaSearchListTo);
            listMahasiswaSearchTo.setItemRenderer(new OrderSearchMahasiswaList());
        }

        public void onClick$button_bbox_CloseTo(Event event) {
            bandbox_Dialog_MahasiswaSearchTo.close();
        }

        public void onMahasiswaItemTo(Event event) {
            Listitem item = listMahasiswaSearchTo.getSelectedItem();
            if (item != null) {
                Mmahasiswa aMahasiswa = (Mmahasiswa) item.getAttribute("data");
                //getTxMainCtrl().setMahasiswa(aMahasiswa);
                getTxMainCtrl().setMahasiswaTo(aMahasiswa);
                if (aMahasiswa != null) {
                txtb_nimTo.setValue(aMahasiswa.getCnim());
                txtb_namaTo.setValue(aMahasiswa.getCnama());
                Tirspasca obj = getIrs();
                //obj.setMmahasiswa(aMahasiswa);
                setIrs(obj);
                bandbox_Dialog_MahasiswaSearchTo.setValue(getTxMainCtrl().getMahasiswaTo().getCnim());
                }
            }
            bandbox_Dialog_MahasiswaSearchTo.close();
        }

        public Mmahasiswa getMahasiswaTo() {
            // STORED IN THE module's MainController
            return getTxMainCtrl().getMahasiswaTo();
        }

        public void setMahasiswaTo(Mmahasiswa mhs) {
            // STORED IN THE module's MainController
            getTxMainCtrl().setMahasiswaTo(mhs);
        }
}
