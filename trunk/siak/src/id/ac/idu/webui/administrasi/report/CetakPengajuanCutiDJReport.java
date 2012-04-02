package id.ac.idu.webui.administrasi.report;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import id.ac.idu.backend.model.Tcutimhs;
import id.ac.idu.irs.service.CutimhsService;
import id.ac.idu.webui.util.ZksampleDateFormat;
import id.ac.idu.webui.util.ZksampleMessageUtils;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Window;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author <a href="dbbottle@gmail.com">hermanto</a>
 * @Date 02 Apr 12
 * ==================================================================
 * Copyright (c) 2012  All rights reserved.
 * ==================================================================
 */

public class CetakPengajuanCutiDJReport extends Window implements Serializable {
    private static final long serialVersionUID = 1L;

    private Iframe iFrame;
    private ByteArrayOutputStream output;
    private InputStream mediais;
    private AMedia amedia;
    private final String zksample2title = "[Zksample2] DynamicJasper Report Sample";
    
    public CetakPengajuanCutiDJReport(Component parent) throws InterruptedException {
        super();
        this.setParent(parent);

        try {
            doPrint();
        } catch (final Exception e) {
            ZksampleMessageUtils.showErrorMessage(e.toString());
        }
    }

    public void doPrint() throws JRException, ColumnBuilderException, ClassNotFoundException, IOException {

        final FastReportBuilder drb = new FastReportBuilder();
        DynamicReport dr;

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
        String no = Labels.getLabel("Nomor Surat");
        String tglsurat = Labels.getLabel("Tanggal Surat");
        String term = Labels.getLabel("Term");
        String nim = Labels.getLabel("Nim");
        String nama = Labels.getLabel("Nama Mahasiswa");

        drb.addColumn(no, "cnosurat", String.class.getName(), 20, columnStyleText, columnStyleTextBold);
        drb.addColumn(tglsurat, "dtglsurat", String.class.getName(), 50, columnStyleText, columnStyleTextBold);
        drb.addColumn(term, "cterm", String.class.getName(), 50, columnStyleText, columnStyleTextBold);
        drb.addColumn(nim, "mmahasiswa.cnim", String.class.getName(), 50, columnStyleText, columnStyleTextBold);
        drb.addColumn(nama, "mmahasiswa.cnama", String.class.getName(), 50, columnStyleText, columnStyleTextBold);

        drb.setTitle(zksample2title);
        drb.setSubtitle("Daftar Pengajuan Cuti Mahasiswa: " + ZksampleDateFormat.getDateTimeFormater().format(new Date()));
        drb.setSubtitleStyle(subtitleStyle);
        drb.setPrintBackgroundOnOddRows(true);
        drb.setUseFullPageWidth(true);
        dr = drb.build();

        CutimhsService anData = (CutimhsService) SpringUtil.getBean("cutimhsService");
        List<Tcutimhs> resultList = anData.getAll();

        List data = new ArrayList(resultList.size());
        SimpleDateFormat dtformat = new SimpleDateFormat("dd MMM yyyy");

        for (Tcutimhs obj : resultList) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("cnosurat", obj.getCnosurat());
            map.put("dtglsurat", dtformat.format(obj.getDtglsurat()));
            map.put("cterm", obj.getCterm());
            map.put("mmahasiswa.cnim", obj.getMmahasiswa().getCnim());
            map.put("mmahasiswa.cnama", obj.getMmahasiswa().getCnama());
            data.add(map);
        }

        JRDataSource ds = new JRBeanCollectionDataSource(data);
        JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), ds);

        String outputFormat = "PDF";

        output = new ByteArrayOutputStream();

        if (outputFormat.equalsIgnoreCase("PDF")) {
            JasperExportManager.exportReportToPdfStream(jp, output);
            mediais = new ByteArrayInputStream(output.toByteArray());
            amedia = new AMedia("CutiMahasiswa.pdf", "pdf", "application/pdf", mediais);

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

        setTitle("Cetak Pengajuan Cuti Mahasiswa");
        setId("ReportWindow");
        setVisible(true);
        setMaximizable(true);
        setMinimizable(true);
        setSizable(true);
        setClosable(true);
        setHeight("60%");
        setWidth("50%");
        addEventListener("onClose", new OnCloseReportEventListener());

        iFrame = new Iframe();
        iFrame.setId("cetakCuti");
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

    public final class OnCloseReportEventListener implements org.zkoss.zk.ui.event.EventListener {
        @Override
        public void onEvent(Event event) throws Exception {
            closeReportWindow();
        }
    }

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
}
