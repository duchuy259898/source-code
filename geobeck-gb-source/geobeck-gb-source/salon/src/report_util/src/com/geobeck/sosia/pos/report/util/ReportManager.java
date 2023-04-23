/*
 * ReportManager.java
 *
 * Created on 2006/05/12, 9:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.report.util;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import java.text.*;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.system.SystemInfo;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;

/**
 * ���[�o�͊Ǘ�
 *
 * @author katagiri
 */
public class ReportManager {

    /**
     * �e���v���[�g�t�H���_�̃p�X
     */
    private static final String TEMPLATE_PATH = "./reports/";
    private static final String REPORT_XML_FILE_EXT = ".jasper";
    private static final String IE_PATH = "C:/Program Files/Internet Explorer/IEXPLORE.EXE";
    public static final Integer PDF_FILE = 0;
    public static final Integer EXCEL_FILE = 1;

    /**
     * �R���X�g���N�^
     */
    public ReportManager() {
    }
    /**
     * �o�̓t�H���_�̃p�X
     */
    private static String exportPath = "";
    /**
     * PDFViewer�̃p�X
     */
    private static String pdfViewerPath = "";
    /**
     * ExcelViewer�̃p�X
     */
    private static String excelViewerlPath = "";
    /**
     * rtfViewer�̃p�X
     */
    private static String rtfViewerPath = "";

    /**
     * �o�̓t�H���_�̃p�X
     *
     * @return �o�̓t�H���_�̃p�X
     */
    public static String getExportPath() {
        return System.getenv(SystemInfo.getTempDirStr()) + "/";
    }

    /**
     * PDFViewer�̃p�X
     *
     * @return PDFViewer�̃p�X
     */
    public static String getPdfViewerPath() {
        if (!SystemInfo.isWindows()) {
            return "open ";
        } else if (IOUtil.isExistFile(IE_PATH)) {
            //IE�����݂���΁AIE�̃p�X��Ԃ�
            return IE_PATH;
        } else if (IOUtil.isExistFile(pdfViewerPath)) {
            //PDF�r���[�A�[�����݂���΁APDF�r���[�A�[�̃p�X��Ԃ�
            return pdfViewerPath;
        }

        return "";
    }

    /**
     * PDFViewer�̃p�X
     *
     * @param aPdfViewerPath PDFViewer�̃p�X
     */
    public static void setPdfViewerPath(String aPdfViewerPath) {
        pdfViewerPath = aPdfViewerPath;
    }

    /**
     * ExcelViewer�̃p�X
     *
     * @return ExcelViewer�̃p�X
     */
    public static String getExcelViewerlPath() {
        return excelViewerlPath;
    }

    /**
     * ExcelViewer�̃p�X
     *
     * @param aExcelViewerlPath ExcelViewer�̃p�X
     */
    public static void setExcelViewerlPath(String aExcelViewerlPath) {
        excelViewerlPath = aExcelViewerlPath;
    }

    /**
     * rtfViewer�̃p�X
     *
     * @return rtfViewer�̃p�X
     */
    public static String getRtfViewerPath() {
        return rtfViewerPath;
    }

    /**
     * rtfViewer�̃p�X
     *
     * @param aRtfViewerPath rtfViewer�̃p�X
     */
    public static void setRtfViewerPath(String aRtfViewerPath) {
        rtfViewerPath = aRtfViewerPath;
    }

    /**
     * ���|�[�g���o�͂���B
     *
     * @param templateFile �e���v���[�g�t�@�C����
     * @param reportName ���|�[�g�t�@�C�����i�g���q�����j
     * @param exportType �o�͌`��
     * @param paramMap �p�����[�^�}�b�v
     */
    public static void exportReport(
            InputStream report,
            String reportName,
            int exportType,
            HashMap<String, Object> paramMap) {
        ReportManager.exportReport(report, reportName, exportType, paramMap, null);
    }

    /**
     * ���|�[�g���o�͂���B
     *
     * @param templateFile �e���v���[�g�t�@�C����
     * @param reportName ���|�[�g�t�@�C�����i�g���q�����j
     * @param exportType �o�͌`��
     * @param paramMap �p�����[�^�}�b�v
     */
    public static void exportReport(
            InputStream report,
            Object reportName,
            int exportType,
            HashMap<String, Object> paramMap,
            Collection collection) {
        ReportManager.exportReport(report, reportName, exportType, paramMap, collection, null, null);
    }

    public static void exportReport(
            InputStream report,
            Object reportName,
            int exportType,
            HashMap<String, Object> paramMap,
            Collection collection, MediaSizeName size,
            OrientationRequested orientation) {
        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(report);

            JasperPrint print = null;

            if (collection == null) {
                print = JasperFillManager.fillReport(
                        jasperReport, paramMap);
            } else {
                print = JasperFillManager.fillReport(
                        jasperReport, paramMap, new JRBeanCollectionDataSource(collection));
            }
            removeBlankPage(print.getPages());
            switch (exportType) {
                //PDF
                case -1:
                    ReportManager.printFile(print);
                    break;
                //PDF
                case 0:
                    ReportManager.outputPDFFile(print, reportName.toString());
                    break;
                //Excel
                case 1:
                    ReportManager.outputExcelFile(print, reportName.toString());
                    break;
                case 2:
                    ReportManager.outputPrinter(print, reportName.toString());
                    break;
                case 3:
                    ReportManager.outputPrinter(print, (PrintService) reportName, size, orientation);
                    break;
            }
        } catch (JRException je) {
            SystemInfo.getLogger().log(Level.SEVERE, je.getLocalizedMessage(), je);
        } catch (SQLException se) {
            SystemInfo.getLogger().log(Level.SEVERE, se.getLocalizedMessage(), se);
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }finally {
            return;
        }
        
    }

    public static void removeBlankPage(List<JRPrintPage> pages) {

        for (Iterator<JRPrintPage> i = pages.iterator(); i.hasNext();) {
            JRPrintPage page = i.next();
            if (page.getElements().size() == 0) {
                i.remove();
            }
        }
    }

    /**
     * PDF�Ń��|�[�g���o�͂���B
     *
     * @param print JasperPrint
     * @param fileName �t�@�C����
     * @throws net.sf.jasperreports.engine.JRException
     * @throws java.io.IOException
     * @throws java.lang.Exception
     */
    private static void printFile(JasperPrint print)
            throws JRException, IOException, Exception {

        JasperPrintManager.printReport(print, false);
    }

    /**
     * PDF�Ń��|�[�g���o�͂���B
     *
     * @param print JasperPrint
     * @param fileName �t�@�C����
     * @throws net.sf.jasperreports.engine.JRException
     * @throws java.io.IOException
     * @throws java.lang.Exception
     */
    private static void outputPDFFile(JasperPrint print, String fileName)
            throws JRException, IOException, Exception {
        String exportFile = ReportManager.getExportPath() + fileName + ".pdf";

        JasperExportManager.exportReportToPdfFile(print, exportFile);

        Runtime runtime = Runtime.getRuntime();

        runtime.exec(ReportManager.getPdfViewerPath() + " " + exportFile);
    }

    /**
     * Excel�Ń��|�[�g���o�͂���B
     *
     * @param print JasperPrint
     * @param fileName �t�@�C����
     * @throws net.sf.jasperreports.engine.JRException
     * @throws java.io.IOException
     * @throws java.lang.Exception
     */
    private static void outputExcelFile(JasperPrint print, String fileName)
            throws JRException, IOException, Exception {
        /*
         String	exportFile	=	exportPath + fileName + ".xls";
		
         JasperExportManager.exportReportToPdfFile(print, exportFile);

         Runtime runtime = Runtime.getRuntime();

         runtime.exec(ReportManager.getExcelViewerlPath() + " " + exportFile);
         */
    }

    /**
     * ���|�[�g���o�͂���B
     *
     * @param print JasperPrint
     * @param printerName �v�����^��
     * @throws net.sf.jasperreports.engine.JRException
     * @throws java.io.IOException
     * @throws java.lang.Exception
     */
    private static void outputPrinter(JasperPrint print, String printerName)
            throws JRException, IOException, Exception {
        //�v�����^�p�o�̓N���X�̐���
        JRPrintServiceExporter exporter = new JRPrintServiceExporter();

        //�o�͑Ώۂ�JasperPrint�̐ݒ�
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);

        //����v�����^���v�����^���Ŏw��
        HashPrintServiceAttributeSet printAttribute =
                new HashPrintServiceAttributeSet();
        printAttribute.add(new PrinterName(printerName, Locale.getDefault()));
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET,
                printAttribute);
//		exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG,
//				true);

        //�o�͂̎��s
        exporter.exportReport();
    }

    /**
     * ���|�[�g���o�͂���B
     *
     * @param print JasperPrint
     * @param printerName �v�����^��
     * @throws net.sf.jasperreports.engine.JRException
     * @throws java.io.IOException
     * @throws java.lang.Exception
     */
    private static void outputPrinter(JasperPrint print, PrintService printer)
            throws JRException, IOException, Exception {
        ReportManager.outputPrinter(print, printer, null, null);
    }

    private static void outputPrinter(JasperPrint print, PrintService printer, MediaSizeName size, OrientationRequested orientation)
            throws JRException, IOException, Exception {
        //�v�����^�p�o�̓N���X�̐���
        JRExporter exporter = new JRPrintServiceExporter();

        //�o�͑Ώۂ�JasperPrint�̐ݒ�
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);

        //����v�����^���v�����^���Ŏw��
//		HashPrintServiceAttributeSet printAttribute =
//				new HashPrintServiceAttributeSet();
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printer);
//		exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG,
//				true);

        if (size != null && orientation != null) {
            PrintRequestAttributeSet atts = new HashPrintRequestAttributeSet();
            atts.add(size);         // �T�C�Y
            atts.add(orientation);  // ����
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, atts);
        }

        //�o�͂̎��s
        exporter.exportReport();
    }

    /**
     * ���[�֘A�̐ݒ肪����Ă��邩���擾����B
     *
     * @return true - �ݒ肳��Ă���B
     */
    public static boolean isSetReportSetting() {
        return (ReportManager.isSetPdfViewerPath());
    }

    /**
     * ���[�̏o�͐悪�ݒ肳��Ă��邩���擾����B
     *
     * @return true - �ݒ肳��Ă���B
     */
    public static boolean isSetExportPath() {
        if (ReportManager.getExportPath() == null
                || ReportManager.getExportPath().equals("")) {
            return false;
        }

        return true;
    }

    /**
     * �o�c�e�r���[�A�[�̃p�X���ݒ肳��Ă��邩���擾����B
     *
     * @return true - �ݒ肳��Ă���B
     */
    public static boolean isSetPdfViewerPath() {
        if (ReportManager.getPdfViewerPath() == null
                || ReportManager.getPdfViewerPath().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * ���[�֘A�̐ݒ��Insert����B
     *
     * @param exportPath ���[�̏o�͐�
     * @param pdfViewerPath �o�c�e�r���[�A�[�̃p�X
     * @return true - ����
     */
    public static boolean registReportSetting(String pdfViewerPath) {
        boolean result = false;

        ConnectionWrapper con = SystemInfo.getBaseConnection();

        try {
            con.begin();

            if (con.executeUpdate(ReportManager.getUpdateReportSettingSQL(
                    pdfViewerPath)) == 1) {
                con.commit();
                ReportManager.setPdfViewerPath(pdfViewerPath);
                result = true;
            } else {
                con.rollback();
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return result;
    }

    /**
     * ���[�֘A�̐ݒ��Update����B
     *
     * @param exportPath ���[�̏o�͐�
     * @param pdfViewerPath �o�c�e�r���[�A�[�̃p�X
     * @return true - ����
     */
    public static String getUpdateReportSettingSQL(String pdfViewerPath) {
        return "update mst_mac\n"
                + "set pdf_viewer_path = " + SQLUtil.convertForSQL(pdfViewerPath) + "\n"
                + "where login_id = " + SQLUtil.convertForSQL(SystemInfo.getLoginID()) + "\n"
                + "and mac_id = " + SQLUtil.convertForSQL(SystemInfo.getMacID()) + "\n";
    }

    /**
     * �G�N�Z���o��
     *
     * @param reportXmlFile �o�͑Ώۃ��|�[�g�e���v���[�g��
     * @param collection �o�͑Ώۃf�[�^
     * @param paramMap �}�b�s���O�p�����[�^
     * @param isViewer ���r���[�\����
     * @return true:���� false:���s
     */
    public static boolean outputXLS(InputStream report, String reportXmlFile, Collection collection, HashMap<String, Object> paramMap) {
        try {
            JasperReport jasperReport = null;
//			InputStream report = ReportLogic.class.getResourceAsStream(this.TEMPLATE_PATH + reportXmlFile + this.REPORT_XML_FILE_EXT);

            if (".jrxml".equals(REPORT_XML_FILE_EXT)) {
                jasperReport = JasperCompileManager.compileReport(report);
            } else if (".jasper".equals(REPORT_XML_FILE_EXT)) {
                jasperReport = (JasperReport) JRLoader.loadObject(report);
            }

            JasperPrint print = JasperFillManager.fillReport(jasperReport, paramMap, new JRBeanCollectionDataSource(collection));

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhms");
            String dateString = format.format(new java.util.Date());


            String exportFile = ReportManager.getExportPath() + reportXmlFile + dateString + ".xls";
            JRXlsExporter exporter = new JRXlsExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, exportFile);
            exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
            exporter.exportReport();

            // �����ς�Excel�t�@�C���ǂݍ���
            HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(exportFile));
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFPrintSetup ps = sheet.getPrintSetup();
            // A4���ɐݒ�
            ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
            ps.setLandscape(true);
            // Excel�t�@�C���ɍď�������
            wb.write(new FileOutputStream(exportFile));

            Runtime runtime = Runtime.getRuntime();
            runtime.exec(ReportManager.getPdfViewerPath() + " " + exportFile);
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }

        return true;
    }
}
