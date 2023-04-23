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
import org.apache.poi.hssf.util.CellRangeAddress8Bit;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.util.CellRangeAddress;

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
	
	public void setTargetSheet(int sheetNum) {
		sheet = wb.getSheetAt(sheetNum);
	}
	
	public void setTargetSheet(String sheetName) {
		sheet = wb.getSheet(sheetName);
	}
	
	public void copySheet(int sheetNum) {
		sheet = wb.cloneSheet(sheetNum);
	}
	/**
	 * 20161206 add
	 * 行コピー
	 * @param rowNumFrom
	 * @param rowNumTo 
	 */
	public void copyRow(int rowNumFrom, int rowNumTo) {
		int numMergedRegions = sheet.getNumMergedRegions();
		int lastRowNum = sheet.getLastRowNum();
		
		HSSFRow originalRow = sheet.getRow(rowNumFrom);
		sheet.createRow(lastRowNum + 1).setHeight(originalRow.getHeight());
		sheet.shiftRows(rowNumTo, lastRowNum + 1, 1);
		
        HSSFRow row2 = sheet.getRow(rowNumTo);
        HSSFCell cell = null, cell2 = null;
        CellStyle cellstyle = null;
		
		if (originalRow != null && row2 != null){
			row2.setHeight(originalRow.getHeight());
			for(int j = 0; j < originalRow.getLastCellNum(); j++){
				cell = originalRow.getCell(j);
				if(cell != null){
					cell2 = row2.createCell(j);
					cellstyle = cell.getCellStyle();
					cell2.setCellStyle(cellstyle);
					switch(cell.getCellType()) {
						case HSSFCell.CELL_TYPE_STRING:
							cell2.setCellValue(cell.getRichStringCellValue());
						break;
						case HSSFCell.CELL_TYPE_NUMERIC:
							if(DateUtil.isCellDateFormatted(cell)) {
								cell2.setCellValue(cell.getDateCellValue());
							}else{
								cell2.setCellValue(cell.getNumericCellValue());
							}
						break;
						case HSSFCell.CELL_TYPE_FORMULA:
							cell2.setCellFormula(cell.getCellFormula());
						break;
						case HSSFCell.CELL_TYPE_BOOLEAN:
							cell2.setCellValue(cell.getBooleanCellValue());
						break;
					}
				}
			}
			
			for (int i=0; i < numMergedRegions; i++) {	
				CellRangeAddress cra = sheet.getMergedRegion(i);
				// コピー元の行の場合
				if(cra.getFirstRow() == originalRow.getRowNum()) {
					CellRangeAddress cra2 = new CellRangeAddress(
							row2.getRowNum(),
							row2.getRowNum(),
							cra.getFirstColumn(), 
							cra.getLastColumn() );
					sheet.addMergedRegion(cra2);
				}
			}
		}
	}
}
