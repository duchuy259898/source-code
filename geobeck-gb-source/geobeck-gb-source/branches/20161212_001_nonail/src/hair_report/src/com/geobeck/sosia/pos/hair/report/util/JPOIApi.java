/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report.util;

import com.geobeck.sosia.pos.report.util.ReportManager;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hssf.usermodel.HSSFChart;
import org.apache.poi.hssf.usermodel.HSSFChart.HSSFSeries;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.RichTextString;

/**
 *
 * @author ivs
 */
public class JPOIApi {

    private HSSFSheet sheet = null;
    private HSSFWorkbook wb = null;
    private String title = "";

    public HSSFSheet getSheet() {
        return sheet;
    }
    private String exportFile = "";

    public String getFilePath() {
        return this.exportFile;
    }

    public JPOIApi(String title) {
        this.title = title;
    }

    public void setCellValue(int colNum, int rowNum, Calendar value) {
        colNum--;
        rowNum--;
        //エクセルの行変数
        HSSFRow row = null;
        //エクセルのセル変数
        HSSFCell cell = null;

        //行取得する
        row = sheet.getRow(rowNum);

        //セル取得する
        cell = row.getCell(convertIntToShort(colNum));
        
        cell.setCellValue(value);

    }

    public void setCellValue(int colNum, int rowNum, Date value) {
        colNum--;
        rowNum--;
        //エクセルの行変数
        HSSFRow row = null;
        //エクセルのセル変数
        HSSFCell cell = null;

        //行取得する
        row = sheet.getRow(rowNum);

        //セル取得する
        cell = row.getCell(convertIntToShort(colNum));


        cell.setCellValue(value);

    }
    
    
    public void setCellValue(int colNum, int rowNum, RichTextString value) {
        colNum--;
        rowNum--;
        //エクセルの行変数
        HSSFRow row = null;
        //エクセルのセル変数
        HSSFCell cell = null;

        //行取得する
        row = sheet.getRow(rowNum);

        //セル取得する
        cell = row.getCell(convertIntToShort(colNum));


        cell.setCellValue(value);

    }
    
    
    public void setCellValue(int colNum, int rowNum, String value) {
        colNum--;
        rowNum--;
        //エクセルの行変数
        try {
            HSSFRow row = null;
            //エクセルのセル変数
            HSSFCell cell = null;

            //行取得する
            row = sheet.getRow(rowNum);

            //セル取得する
            cell = row.getCell(convertIntToShort(colNum));


            cell.setCellValue(value);
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getMessage());
        }

    }

    public void setCellValue(int colNum, int rowNum, boolean value) {
        colNum--;
        rowNum--;
        //エクセルの行変数
        HSSFRow row = null;
        //エクセルのセル変数
        HSSFCell cell = null;

        //行取得する
        row = sheet.getRow(rowNum);

        //セル取得する
        cell = row.getCell(convertIntToShort(colNum));


        cell.setCellValue(value);

    }

    public void setCellValue(int colNum, int rowNum, double value) {
        colNum--;
        rowNum--;
        //エクセルの行変数
        HSSFRow row = null;
        //エクセルのセル変数
        HSSFCell cell = null;

        //行取得する
        row = sheet.getRow(rowNum);

        //セル取得する
        cell = row.getCell(convertIntToShort(colNum));


        cell.setCellValue(value);

    }

    public String getCellValue(int colNum, int rowNum) {
        colNum--;
        rowNum--;
        //エクセルの行変数
        HSSFRow row = null;
        //エクセルのセル変数
        HSSFCell cell = null;
        row = sheet.getRow(rowNum);
        cell = row.getCell(convertIntToShort(colNum));
        return cell.getStringCellValue();
    }
    
    public void setFormularActive() {
        
        HSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
        
    }
    
    public void setTemplateFile(String templateFile) {

        try {
            InputStream is = getClass().getResourceAsStream(templateFile);
             
            wb = new HSSFWorkbook(is);
            // ファイル保存ダイアログが表示されるようにテンプレートとして作成する
            exportFile =
                    ReportManager.getExportPath()
                    + this.title
                    + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date())
                    + ".xlt";

            // １番目のシートを取得
            this.sheet = wb.getSheetAt(0);
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
        }
    }

    public void openWorkbook() {
        try {
            FileOutputStream stream = new FileOutputStream(exportFile);
            sheet.setAlternativeFormula(true);
            wb.write(stream);
            stream.close();
            String p = "cmd /c \"" + exportFile + "\"";

            Runtime runtime = Runtime.getRuntime();
            if (SystemInfo.isWindows()) {
                runtime.exec("cmd /c \"" + exportFile + "\"");
            } else {
                runtime.exec("open " + exportFile);
            }

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
        }
    }

    public Short convertIntToShort(int value) {
        return Short.parseShort(String.valueOf(value));
    }
}
