package id.ac.idu.webui.irs.report;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import id.ac.idu.backend.model.Tjadkulmaster;
import id.ac.idu.irs.service.JadkulService;
import id.ac.idu.webui.util.ZksampleDateFormat;
import id.ac.idu.webui.util.ZksampleMessageUtils;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Window;

import java.io.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Hari Christian
 * Date: 02/04/12
 * Time: 12:04
 * To change this template use File | Settings | File Templates.
 */
public class JadkulmasterSimpleDJReport extends Window implements Serializable {

    private static final long serialVersionUID = 1L;

    private Iframe iFrame;
    private ByteArrayOutputStream output;
    private InputStream mediais;
    private AMedia amedia;
    private final String zksample2title = "Laporan Jadwal Kuliah";

    private Tjadkulmaster param;

    public JadkulmasterSimpleDJReport(Component parent, Tjadkulmaster obj) throws InterruptedException {
        super();
        this.setParent(parent);
        if (obj != null) {
            this.param = obj;
        }

        try {
            doPrint();
        } catch (final Exception e) {
            ZksampleMessageUtils.showErrorMessage(e.toString());
        }
    }

    public void doPrint() throws JRException, ColumnBuilderException, ClassNotFoundException, IOException {

        final FastReportBuilder drb = new FastReportBuilder();
        DynamicReport dr;

        /**
         * Set the styles. In a report created with DynamicReportBuilder we do
         * this in an other way.
         */
        // Rows content
        Style columnStyleNumbers = new Style();
        columnStyleNumbers.setFont(Font.ARIAL_SMALL);
        columnStyleNumbers.setHorizontalAlign(HorizontalAlign.RIGHT);

        // Header for number row content
        Style columnStyleNumbersBold = new Style();
        columnStyleNumbersBold.setFont(Font.ARIAL_MEDIUM_BOLD);
        columnStyleNumbersBold.setHorizontalAlign(HorizontalAlign.RIGHT);
        columnStyleNumbersBold.setBorderBottom(Border.PEN_1_POINT);

        // Rows content
        Style columnStyleText = new Style();
        columnStyleText.setFont(Font.ARIAL_SMALL);
        columnStyleText.setHorizontalAlign(HorizontalAlign.LEFT);

        // Header for String row content
        Style columnStyleTextBold = new Style();
        columnStyleTextBold.setFont(Font.ARIAL_MEDIUM_BOLD);
        columnStyleTextBold.setHorizontalAlign(HorizontalAlign.LEFT);
        columnStyleTextBold.setBorderBottom(Border.PEN_1_POINT);

        // Subtitle
        Style subtitleStyle = new Style();
        subtitleStyle.setHorizontalAlign(HorizontalAlign.LEFT);
        subtitleStyle.setFont(Font.ARIAL_MEDIUM_BOLD);

        // Localized column headers
        String f1 = "Sekolah";
        String f2 = "Prodi";
        String f3 = "Dosen";
        String f4 = "Mata Kuliah";
        String f5 = "Term";
        String f6 = "Kel";
        String f7 = "Thn";
        String f8 = "Smt";
        String f9 = "Sks";

        drb.addColumn(f1, "f1", String.class.getName(), 20, columnStyleText, columnStyleTextBold);
        drb.addColumn(f2, "f2", String.class.getName(), 20, columnStyleText, columnStyleTextBold);
        drb.addColumn(f3, "f3", String.class.getName(), 20, columnStyleText, columnStyleTextBold);
        drb.addColumn(f4, "f4", String.class.getName(), 20, columnStyleText, columnStyleTextBold);
        drb.addColumn(f5, "f5", String.class.getName(), 10, columnStyleText, columnStyleTextBold);
        drb.addColumn(f6, "f6", String.class.getName(), 10, columnStyleText, columnStyleTextBold);
        drb.addColumn(f7, "f7", String.class.getName(), 10, columnStyleText, columnStyleTextBold);
        drb.addColumn(f8, "f8", String.class.getName(), 10, columnStyleText, columnStyleTextBold);
        drb.addColumn(f9, "f9", String.class.getName(), 10, columnStyleText, columnStyleTextBold);

        // Sets the Report Columns, header, Title, Groups, Etc Formats
        // DynamicJasper documentation
        drb.setTitle(this.zksample2title);
        drb.setSubtitle("Tanggal Cetak: " + ZksampleDateFormat.getDateTimeFormater().format(new Date()));
        drb.setSubtitleStyle(subtitleStyle);
        drb.setPrintBackgroundOnOddRows(true);
        drb.setUseFullPageWidth(true);
        dr = drb.build();

        // Get information from database
        JadkulService as = (JadkulService) SpringUtil.getBean("jadkulService");
        List<Tjadkulmaster> resultList = as.getAllTjadkulmasters();

        // Create Datasource and put it in Dynamic Jasper Format
        List data = new ArrayList(resultList.size());

        for (Tjadkulmaster obj : resultList) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("f1", obj.getMsekolah().getCnamaSekolah());
            map.put("f2", obj.getMprodi().getCnmprogst());
            map.put("f3", obj.getMpegawai1().getCnama());
            map.put("f4", obj.getMtbmtkl().getCnamamk());
            map.put("f5", obj.getCterm());
            map.put("f6", obj.getCkelompok());
            map.put("f7", obj.getCthajar());
            map.put("f8", obj.getCsmt());
            map.put("f9", String.valueOf(obj.getNsks()));
            data.add(map);
        }

        // Generate the Jasper Print Object
        JRDataSource ds = new JRBeanCollectionDataSource(data);
        JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), ds);

        String outputFormat = "PDF";

        output = new ByteArrayOutputStream();

        if (outputFormat.equalsIgnoreCase("PDF")) {
            JasperExportManager.exportReportToPdfStream(jp, output);
            mediais = new ByteArrayInputStream(output.toByteArray());
            amedia = new AMedia("FirstReport.pdf", "pdf", "application/pdf", mediais);

            callReportWindow(this.amedia, "PDF");
        } else if (outputFormat.equalsIgnoreCase("XLS")) {
            JExcelApiExporter exporterXLS = new JExcelApiExporter();
            exporterXLS.setParameter(JExcelApiExporterParameter.JASPER_PRINT, jp);
            exporterXLS.setParameter(JExcelApiExporterParameter.OUTPUT_STREAM, output);
            exporterXLS.setParameter(JExcelApiExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
            exporterXLS.setParameter(JExcelApiExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);
            exporterXLS.setParameter(JExcelApiExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
            exporterXLS.exportReport();
            mediais = new ByteArrayInputStream(output.toByteArray());
            amedia = new AMedia("FileFormatExcel", "xls", "application/vnd.ms-excel", mediais);

            callReportWindow(this.amedia, "XLS");
        } else if (outputFormat.equalsIgnoreCase("RTF") || outputFormat.equalsIgnoreCase("DOC")) {
            JRRtfExporter exporterRTF = new JRRtfExporter();
            exporterRTF.setParameter(JRExporterParameter.JASPER_PRINT, jp);
            exporterRTF.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
            exporterRTF.exportReport();
            mediais = new ByteArrayInputStream(this.output.toByteArray());
            amedia = new AMedia("FileFormatRTF", "rtf", "application/rtf", mediais);

            callReportWindow(amedia, "RTF-DOC");
        }
    }

    private void callReportWindow(AMedia aMedia, String format) {
        final boolean modal = true;

        setTitle("Laporan Jadwal Kuliah");
        setId("ReportWindow");
        setVisible(true);
        setMaximizable(true);
        setMinimizable(true);
        setSizable(true);
        setClosable(true);
        setHeight("100%");
        setWidth("80%");
        addEventListener("onClose", new OnCloseReportEventListener());

        iFrame = new Iframe();
        iFrame.setId("jasperReportId");
        iFrame.setWidth("100%");
        iFrame.setHeight("100%");
        iFrame.setContent(aMedia);
        iFrame.setParent(this);

        if (modal == true) {
            try {
                doModal();
            } catch (final SuspendNotAllowedException e) {
                throw new RuntimeException(e);
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * EventListener for closing the Report Window.<br>
     *
     * @author sge
     */
    public final class OnCloseReportEventListener implements org.zkoss.zk.ui.event.EventListener {
        @Override
        public void onEvent(Event event) throws Exception {
            closeReportWindow();
        }
    }

    /**
     * We must clear something to prevent errors or problems <br>
     * by opening the report a few times. <br>
     *
     * @throws java.io.IOException
     */
    private void closeReportWindow() throws IOException {

        // TODO check this
        try {
            this.amedia.getStreamData().close();
            this.output.close();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

        onClose();

    }

    public Tjadkulmaster getParam() {
        return param;
    }

    public void setParam(Tjadkulmaster param) {
        this.param = param;
    }
}
