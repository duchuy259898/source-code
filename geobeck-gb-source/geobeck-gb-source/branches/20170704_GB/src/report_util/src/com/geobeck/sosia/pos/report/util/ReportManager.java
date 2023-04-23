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
 * 帳票出力管理
 *
 * @author katagiri
 */
public class ReportManager {

    /**
     * テンプレートフォルダのパス
     */
    private static final String TEMPLATE_PATH = "./reports/";
    private static final String REPORT_XML_FILE_EXT = ".jasper";
    private static final String IE_PATH = "C:/Program Files/Internet Explorer/IEXPLORE.EXE";
    public static final Integer PDF_FILE = 0;
    public static final Integer EXCEL_FILE = 1;

    /**
     * コンストラクタ
     */
    public ReportManager() {
    }
    /**
     * 出力フォルダのパス
     */
    private static String exportPath = "";
    /**
     * PDFViewerのパス
     */
    private static String pdfViewerPath = "";
    /**
     * ExcelViewerのパス
     */
    private static String excelViewerlPath = "";
    /**
     * rtfViewerのパス
     */
    private static String rtfViewerPath = "";

    /**
     * 出力フォルダのパス
     *
     * @return 出力フォルダのパス
     */
    public static String getExportPath() {
        return System.getenv(SystemInfo.getTempDirStr()) + "/";
    }

    /**
     * PDFViewerのパス
     *
     * @return PDFViewerのパス
     */
    public static String getPdfViewerPath() {
        if (!SystemInfo.isWindows()) {
            return "open ";
        } else if (IOUtil.isExistFile(IE_PATH)) {
            //IEが存在すれば、IEのパスを返す
            return IE_PATH;
        } else if (IOUtil.isExistFile(pdfViewerPath)) {
            //PDFビューアーが存在すれば、PDFビューアーのパスを返す
            return pdfViewerPath;
        }

        return "";
    }

    /**
     * PDFViewerのパス
     *
     * @param aPdfViewerPath PDFViewerのパス
     */
    public static void setPdfViewerPath(String aPdfViewerPath) {
        pdfViewerPath = aPdfViewerPath;
    }

    /**
     * ExcelViewerのパス
     *
     * @return ExcelViewerのパス
     */
    public static String getExcelViewerlPath() {
        return excelViewerlPath;
    }

    /**
     * ExcelViewerのパス
     *
     * @param aExcelViewerlPath ExcelViewerのパス
     */
    public static void setExcelViewerlPath(String aExcelViewerlPath) {
        excelViewerlPath = aExcelViewerlPath;
    }

    /**
     * rtfViewerのパス
     *
     * @return rtfViewerのパス
     */
    public static String getRtfViewerPath() {
        return rtfViewerPath;
    }

    /**
     * rtfViewerのパス
     *
     * @param aRtfViewerPath rtfViewerのパス
     */
    public static void setRtfViewerPath(String aRtfViewerPath) {
        rtfViewerPath = aRtfViewerPath;
    }

    /**
     * レポートを出力する。
     *
     * @param templateFile テンプレートファイル名
     * @param reportName レポートファイル名（拡張子無し）
     * @param exportType 出力形式
     * @param paramMap パラメータマップ
     */
    public static void exportReport(
            InputStream report,
            String reportName,
            int exportType,
            HashMap<String, Object> paramMap) {
        ReportManager.exportReport(report, reportName, exportType, paramMap, null);
    }

    /**
     * レポートを出力する。
     *
     * @param templateFile テンプレートファイル名
     * @param reportName レポートファイル名（拡張子無し）
     * @param exportType 出力形式
     * @param paramMap パラメータマップ
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
     * PDFでレポートを出力する。
     *
     * @param print JasperPrint
     * @param fileName ファイル名
     * @throws net.sf.jasperreports.engine.JRException
     * @throws java.io.IOException
     * @throws java.lang.Exception
     */
    private static void printFile(JasperPrint print)
            throws JRException, IOException, Exception {

        JasperPrintManager.printReport(print, false);
    }

    /**
     * PDFでレポートを出力する。
     *
     * @param print JasperPrint
     * @param fileName ファイル名
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
     * Excelでレポートを出力する。
     *
     * @param print JasperPrint
     * @param fileName ファイル名
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
     * レポートを出力する。
     *
     * @param print JasperPrint
     * @param printerName プリンタ名
     * @throws net.sf.jasperreports.engine.JRException
     * @throws java.io.IOException
     * @throws java.lang.Exception
     */
    private static void outputPrinter(JasperPrint print, String printerName)
            throws JRException, IOException, Exception {
        //プリンタ用出力クラスの生成
        JRPrintServiceExporter exporter = new JRPrintServiceExporter();

        //出力対象のJasperPrintの設定
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);

        //印刷プリンタをプリンタ名で指定
        HashPrintServiceAttributeSet printAttribute =
                new HashPrintServiceAttributeSet();
        printAttribute.add(new PrinterName(printerName, Locale.getDefault()));
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET,
                printAttribute);
//		exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG,
//				true);

        //出力の実行
        exporter.exportReport();
    }

    /**
     * レポートを出力する。
     *
     * @param print JasperPrint
     * @param printerName プリンタ名
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
        //プリンタ用出力クラスの生成
        JRExporter exporter = new JRPrintServiceExporter();

        //出力対象のJasperPrintの設定
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);

        //印刷プリンタをプリンタ名で指定
//		HashPrintServiceAttributeSet printAttribute =
//				new HashPrintServiceAttributeSet();
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printer);
//		exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG,
//				true);

        if (size != null && orientation != null) {
            PrintRequestAttributeSet atts = new HashPrintRequestAttributeSet();
            atts.add(size);         // サイズ
            atts.add(orientation);  // 方向
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, atts);
        }

        //出力の実行
        exporter.exportReport();
    }

    /**
     * 帳票関連の設定がされているかを取得する。
     *
     * @return true - 設定されている。
     */
    public static boolean isSetReportSetting() {
        return (ReportManager.isSetPdfViewerPath());
    }

    /**
     * 帳票の出力先が設定されているかを取得する。
     *
     * @return true - 設定されている。
     */
    public static boolean isSetExportPath() {
        if (ReportManager.getExportPath() == null
                || ReportManager.getExportPath().equals("")) {
            return false;
        }

        return true;
    }

    /**
     * ＰＤＦビューアーのパスが設定されているかを取得する。
     *
     * @return true - 設定されている。
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
     * 帳票関連の設定をInsertする。
     *
     * @param exportPath 帳票の出力先
     * @param pdfViewerPath ＰＤＦビューアーのパス
     * @return true - 成功
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
     * 帳票関連の設定をUpdateする。
     *
     * @param exportPath 帳票の出力先
     * @param pdfViewerPath ＰＤＦビューアーのパス
     * @return true - 成功
     */
    public static String getUpdateReportSettingSQL(String pdfViewerPath) {
        return "update mst_mac\n"
                + "set pdf_viewer_path = " + SQLUtil.convertForSQL(pdfViewerPath) + "\n"
                + "where login_id = " + SQLUtil.convertForSQL(SystemInfo.getLoginID()) + "\n"
                + "and mac_id = " + SQLUtil.convertForSQL(SystemInfo.getMacID()) + "\n";
    }

    /**
     * エクセル出力
     *
     * @param reportXmlFile 出力対象レポートテンプレート名
     * @param collection 出力対象データ
     * @param paramMap マッピングパラメータ
     * @param isViewer レビュー表示可否
     * @return true:成功 false:失敗
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

            // 生成済みExcelファイル読み込み
            HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(exportFile));
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFPrintSetup ps = sheet.getPrintSetup();
            // A4横に設定
            ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
            ps.setLandscape(true);
            // Excelファイルに再書き込み
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
