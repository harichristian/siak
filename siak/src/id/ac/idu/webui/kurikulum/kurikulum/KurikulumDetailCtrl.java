package id.ac.idu.webui.kurikulum.kurikulum;

import com.trg.search.Filter;
import id.ac.idu.backend.model.Mdetilkurikulum;
import id.ac.idu.backend.model.Mkurikulum;
import id.ac.idu.backend.model.Mpeminatan;
import id.ac.idu.backend.model.Mprodi;
import id.ac.idu.backend.util.HibernateSearchObject;
import id.ac.idu.kurikulum.service.DetilKurikulumService;
import id.ac.idu.kurikulum.service.KurikulumService;
import id.ac.idu.util.ConstantUtil;
import id.ac.idu.webui.util.GFCBaseCtrl;
import id.ac.idu.webui.util.ZksampleMessageUtils;
import id.ac.idu.webui.util.pagging.PagedListWrapper;
import id.ac.idu.webui.util.searchdialogs.PeminatanExtendedSearchListBox;
import id.ac.idu.webui.util.searchdialogs.ProdiExtendedSearchListBox;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: valeo
 * Date: 3/9/12
 * Time: 3:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class KurikulumDetailCtrl extends GFCBaseCtrl implements Serializable {
    private static final long serialVersionUID = -1L;
    private static final Logger logger = Logger.getLogger(KurikulumDetailCtrl.class);

    /*
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      * All the components that are defined here and have a corresponding
      * component with the same 'id' in the zul-file are getting autowired by our
      * 'extends GFCBaseCtrl' GenericForwardComposer.
      * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      */
    protected Window windowKurikulumDetail; // autowired

    protected Borderlayout borderlayout_KurikulumDetail; // autowired

    protected Textbox txtb_code; // autowired
    protected Textbox txtb_cohort; // autowired
    protected Textbox txtb_prodi;
    protected Button btnSearchProdiExtended;
    protected Textbox txtb_peminatan;
    protected Button btnSearchPeminatanExtended;
    protected Button button_KurikulumDialog_PrintKurikulum; // autowired
    protected Listheader listHeaderKode;
    protected Listheader listHeaderNama;
    protected Listheader listHeaderJenis;
    protected Listheader listHeaderStatus;
    protected Listheader listHeaderMun;
    protected Listheader listHeaderTerm;
    protected Listheader listHeaderProdi;
    protected Borderlayout borderDetilKurikulum;

    private int pageSize;
    private int kurikulumId;
    private int countRows;

    public static final String DATA = "data";
    public static final String LIST = "LIST";
    public static final String CONTROL = "CONTROL";
    public static final String ISNEW = "ISNEW";

    protected Button btnNewDetilKurikulum;
    protected Button btnDeleteDetilKurikulum;

    protected Set<Mdetilkurikulum> delDetilKurikulum = new HashSet<Mdetilkurikulum>();
    private transient PagedListWrapper<Mdetilkurikulum> plwDetilKurikulum;
    protected Listbox listDetilKurikulum;
    protected Paging pagingDetilKurikulum;

    //protected RiwayatBahasaCtrl riwayatBahasaCtrl;
    //protected RiwayatKursusCtrl riwayatKursusCtrl;

    // Databinding
    protected transient AnnotateDataBinder binder;
    private KurikulumMainCtrl kurikulumMainCtrl;
    protected DetilKurikulumCtrl detilKurikulumCtrl;

    // ServiceDAOs / Domain Classes
    private transient KurikulumService kurikulumService;
    private transient DetilKurikulumService detilKurikulumService;

    /**
     * default constructor.<br>
     */
    public KurikulumDetailCtrl() {
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
            setKurikulumMainCtrl((KurikulumMainCtrl) arg.get("ModuleMainController"));

            // SET THIS CONTROLLER TO THE module's Parent/MainController
            getKurikulumMainCtrl().setKurikulumDetailCtrl(this);

            // Get the selected object.
            // Check if this Controller if created on first time. If so,
            // than the selectedXXXBean should be null
            if (getKurikulumMainCtrl().getSelectedKurikulum() != null) {
                setSelectedKurikulum(getKurikulumMainCtrl().getSelectedKurikulum());
            } else
                setSelectedKurikulum(null);
        } else {
            setSelectedKurikulum(null);
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
    public void onCreate$windowKurikulumDetail(Event event) throws Exception {
        setPageSize(20);
        binder = (AnnotateDataBinder) event.getTarget().getAttribute("binder", true);
        //doReadOnlyMode(!getDetailCtrl().getMainCtrl().btnSave.isVisible());
        if(getKurikulum() != null) {
            loadDetilKurikulum();
        }
        binder.loadAll();
        doFitSize(event);
    }

    public void loadDetilKurikulum() {
        doFitSize();

        // not used listheaders must be declared like ->
        // lh.setSortAscending(""); lh.setSortDescending("")
        listHeaderKode.setSortAscending(new FieldComparator(ConstantUtil.MATAKULIAH_DOT_CODE, true));
        listHeaderKode.setSortDescending(new FieldComparator(ConstantUtil.MATAKULIAH_DOT_CODE, false));
        listHeaderNama.setSortAscending(new FieldComparator(ConstantUtil.MATAKULIAH_DOT_NAMA, true));
        listHeaderNama.setSortDescending(new FieldComparator(ConstantUtil.MATAKULIAH_DOT_NAMA, false));
        listHeaderJenis.setSortAscending(new FieldComparator(ConstantUtil.JENIS, true));
        listHeaderJenis.setSortDescending(new FieldComparator(ConstantUtil.JENIS, false));
        listHeaderStatus.setSortAscending(new FieldComparator(ConstantUtil.STATUS, true));
        listHeaderStatus.setSortDescending(new FieldComparator(ConstantUtil.STATUS, false));
        listHeaderMun.setSortAscending(new FieldComparator(ConstantUtil.MUN, true));
        listHeaderMun.setSortDescending(new FieldComparator(ConstantUtil.MUN, false));
        listHeaderTerm.setSortAscending(new FieldComparator(ConstantUtil.TERM_SEMESTER, true));
        listHeaderTerm.setSortDescending(new FieldComparator(ConstantUtil.TERM_SEMESTER, false));
        listHeaderProdi.setSortAscending(new FieldComparator(ConstantUtil.LINTAS_PRODI, true));
        listHeaderProdi.setSortDescending(new FieldComparator(ConstantUtil.LINTAS_PRODI, false));

        this.kurikulumId = getKurikulum().getId();

        HibernateSearchObject<Mdetilkurikulum> so = new HibernateSearchObject<Mdetilkurikulum>(Mdetilkurikulum.class);
        if(getKurikulum() != null)
            so.addFilter(new Filter(ConstantUtil.KURIKULUM_ID, this.kurikulumId, Filter.OP_EQUAL));

        so.addSort(ConstantUtil.MATAKULIAH_DOT_CODE, false);

        pagingDetilKurikulum.setPageSize(pageSize);
		pagingDetilKurikulum.setDetailed(true);
		getPlwDetilKurikulum().init(so, listDetilKurikulum, pagingDetilKurikulum);
		listDetilKurikulum.setItemRenderer(new DetilKurikulumSearchList());
    }
    public void doFitSize() {
        // normally 0 ! Or we have a i.e. a toolBar on top of the listBox.
        final int specialSize = 5;
        final int height = ((Intbox) Path.getComponent("/outerIndexWindow/currentDesktopHeight")).getValue().intValue();
        final int maxListBoxHeight = height - specialSize - 148;
        setCountRows((int) Math.round(maxListBoxHeight / 17.7));
        borderDetilKurikulum.setHeight(String.valueOf(maxListBoxHeight) + "px");
        windowKurikulumDetail.invalidate();
    }

    public int getCountRows() {
        return this.countRows;
    }

    public void setCountRows(int countRows) {
        this.countRows = countRows;
    }

    public void onClick$btnNewDetilKurikulum(Event event) throws Exception {
        final Mdetilkurikulum dk = getKurikulumMainCtrl().getDetilKurikulumService().getNew();
        this.showDetail(dk, true);
    }

    public void onClick$btnDeleteDetilKurikulum(Event event) throws Exception {
        Listitem item = listDetilKurikulum.getSelectedItem();
        getDelDetilKurikulum().add((Mdetilkurikulum) item.getAttribute(KurikulumDetailCtrl.DATA));
        listDetilKurikulum.removeItemAt(listDetilKurikulum.getSelectedIndex());
    }

    public void onDetilKurikulumItem(Event event) throws Exception {
        Listitem item = listDetilKurikulum.getSelectedItem();
        if(item == null) return;

        final Mdetilkurikulum obj = (Mdetilkurikulum) item.getAttribute(KurikulumDetailCtrl.DATA);
        this.showDetail(obj, false);
    }

    public void showDetail(Mdetilkurikulum obj, boolean isnew) throws Exception {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(KurikulumDetailCtrl.DATA, obj);
        map.put(KurikulumDetailCtrl.LIST, listDetilKurikulum);
        map.put(KurikulumDetailCtrl.CONTROL, this);
        map.put(KurikulumDetailCtrl.ISNEW, isnew);

        try {
            Executions.createComponents("/WEB-INF/pages/kurikulum/kurikulum/detilKurikulum.zul", null, map);
        } catch (final Exception e) {
            logger.error("onOpenWindow:: error opening window / " + e.getMessage());
            ZksampleMessageUtils.showErrorMessage(e.toString());
        }
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // +++++++++++++++++ Business Logic ++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

    // +++++++++++++++++++++++++++++++++++++++++++++++++ //
    // ++++++++++++++++++++ Helpers ++++++++++++++++++++ //
    // +++++++++++++++++++++++++++++++++++++++++++++++++ //

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
        borderlayout_KurikulumDetail.setHeight(String.valueOf(maxListBoxHeight) + "px");

        windowKurikulumDetail.invalidate();
    }

    /**
     * Set all components in readOnly/disabled modus.
     * <p/>
     * true = all components are readOnly or disabled.<br>
     * false = all components are accessable.<br>
     *
     * @param b
     */
    public void doReadOnlyMode(boolean b) {
        txtb_code.setReadonly(b);
        txtb_cohort.setReadonly(b);
        btnSearchProdiExtended.setDisabled(b);
        btnSearchPeminatanExtended.setDisabled(b);
    }


    public void onClick$btnSearchProdiExtended(Event event) {
        doSearchProdiExtended(event);
    }

    private void doSearchProdiExtended(Event event) {
        Mprodi prodi = ProdiExtendedSearchListBox.show(windowKurikulumDetail);

        if (prodi != null) {
            txtb_prodi.setValue(prodi.getCnmprogst());
            Mkurikulum obj = getKurikulum();
            obj.setMprodi(prodi);
            setKurikulum(obj);
        }
    }

    public void onClick$btnSearchPeminatanExtended(Event event) {
        doSearchPeminatanExtended(event);
    }

    private void doSearchPeminatanExtended(Event event) {
        Mpeminatan peminatan = PeminatanExtendedSearchListBox.show(windowKurikulumDetail);

        if (peminatan != null) {
            txtb_peminatan.setValue(peminatan.getCnmminat());
            Mkurikulum obj = getKurikulum();
            obj.setMpeminatan(peminatan);
            setKurikulum(obj);
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
    public Mkurikulum getKurikulum() {
        // STORED IN THE module's MainController
        return getKurikulumMainCtrl().getSelectedKurikulum();
    }

    public void setKurikulum(Mkurikulum anKurikulum) {
        // STORED IN THE module's MainController
        getKurikulumMainCtrl().setSelectedKurikulum(anKurikulum);
    }

    public Mkurikulum getSelectedKurikulum() {
        // STORED IN THE module's MainController
        return getKurikulumMainCtrl().getSelectedKurikulum();
    }

    public void setSelectedKurikulum(Mkurikulum selectedKurikulum) {
        // STORED IN THE module's MainController
        getKurikulumMainCtrl().setSelectedKurikulum(selectedKurikulum);
    }

    public BindingListModelList getKurikulums() {
        // STORED IN THE module's MainController
        return getKurikulumMainCtrl().getKurikulums();
    }

    public void setKurikulums(BindingListModelList kurikulums) {
        // STORED IN THE module's MainController
        getKurikulumMainCtrl().setKurikulums(kurikulums);
    }

    public AnnotateDataBinder getBinder() {
        return this.binder;
    }

    public void setBinder(AnnotateDataBinder binder) {
        this.binder = binder;
    }

    public void setKurikulumService(KurikulumService kurikulumService) {
        this.kurikulumService = kurikulumService;
    }

    public KurikulumService getKurikulumService() {
        return this.kurikulumService;
    }

    public void setKurikulumMainCtrl(KurikulumMainCtrl kurikulumMainCtrl) {
        this.kurikulumMainCtrl = kurikulumMainCtrl;
    }

    public KurikulumMainCtrl getKurikulumMainCtrl() {
        return this.kurikulumMainCtrl;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public PagedListWrapper<Mdetilkurikulum> getPlwDetilKurikulum() {
        return plwDetilKurikulum;
    }

    public void setPlwDetilKurikulum(PagedListWrapper<Mdetilkurikulum> plwDetilKurikulum) {
        this.plwDetilKurikulum = plwDetilKurikulum;
    }

    public Set<Mdetilkurikulum> getDelDetilKurikulum() {
        return delDetilKurikulum;
    }

    public void setDelDetilKurikulum(Set<Mdetilkurikulum> delDetilKurikulum) {
        this.delDetilKurikulum = delDetilKurikulum;
    }

    public boolean isModeEdit() {
        return (getKurikulumMainCtrl().btnSave.isVisible());
    }

    public DetilKurikulumCtrl getDetilKurikulumCtrl() {
        return detilKurikulumCtrl;
    }

    public void setDetilKurikulumCtrl(DetilKurikulumCtrl detilKurikulumCtrl) {
        this.detilKurikulumCtrl = detilKurikulumCtrl;
    }

    public Listbox getListDetilKurikulum() {
        return listDetilKurikulum;
    }

    public void setListDetilKurikulum(Listbox listDetilKurikulum) {
        this.listDetilKurikulum = listDetilKurikulum;
    }
}
