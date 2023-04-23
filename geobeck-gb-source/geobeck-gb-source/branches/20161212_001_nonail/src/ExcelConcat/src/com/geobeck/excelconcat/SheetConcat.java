/*
 * SheetConcat.java
 *
 * Created on 2008/09/26, 15:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.excelconcat;

import com.geobeck.sosia.pos.report.util.ReportManager;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
//import org.apache.poi.hssf.usermodel.*;

/**
 *
 * @author takeda
 */
public class SheetConcat
{
	
	/** Creates a new instance of SheetConcat */
	public SheetConcat()
	{
	}
	
	
	public static boolean outputXLSFileList(String outputFileTitle, ArrayList<String> fileNameList, ArrayList<String> sheetnamelist)
	{
//		ArrayList<Workbook> wblistSrc = new ArrayList();
//		WritableWorkbook wbDst;
		jxl.Workbook wbSrc;
		WritableWorkbook wbDst;

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhms");
		String dateString = format.format(new java.util.Date());
		String strExportFile;
		
		try
		{
			strExportFile = ReportManager.getExportPath() + outputFileTitle + dateString + ".xls";
			wbDst = jxl.Workbook.createWorkbook(new File(strExportFile));
			
			for (int i = 0; i < fileNameList.size(); ++i)
			{
				wbSrc = jxl.Workbook.getWorkbook(new File(fileNameList.get(i)));
				wbDst.importSheet(sheetnamelist.get(i), i, wbSrc.getSheet(0));
			}

			wbDst.write();
			wbDst.close();

			Runtime runtime = Runtime.getRuntime();
			runtime.exec(ReportManager.getPdfViewerPath() + " " + strExportFile);
			System.out.println(ReportManager.getPdfViewerPath() + " " + strExportFile);
		}
		catch (BiffException ex)
		{
			ex.printStackTrace();
		}

		catch (jxl.write.WriteException ex)
		{
			ex.printStackTrace();
		}
 
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		/*
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		 **/
		return true;
	}
	
	
	/*
	final static private String destFile    = "C:\\Concat.xls";
	final static private String sourceFile1 = "C:\\DailyTechnicSales20080923055503.xls";
	final static private String sourceFile2 = "C:\\DailyTechnicSales20080924025439.xls";
	 
	 
	 
	public static void main(String args[]) {
		try {
			Workbook w0 = Workbook.getWorkbook(new File(sourceFile1));
			Workbook w1 = Workbook.getWorkbook(new File(sourceFile2));
			WritableWorkbook w2 = Workbook.createWorkbook(new File(destFile));
			w2.importSheet("hoge1", 0, w0.getSheet(0));
			w2.importSheet("hoge2", 1, w1.getSheet(0));
//            w2.copy(w1);
//            WritableSheet s1 = reateSheet("hoge", 0);
//            WritableSheet s2 = w2.createSheet("hoge", 1);
	 
//            s1. = w1.getSheet(0);
	 
			w2.write();
			w2.close();
		} catch (BiffException ex) {
			ex.printStackTrace();
		} catch (WriteException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	 
	 
		try {
	 
			FileInputStream is = new FileInputStream(sourceFile1);
			HSSFWorkbook srcBook = new HSSFWorkbook(is);
			HSSFSheet srcSheet = srcBook.getSheetAt(0);
			System.out.println("number of sheets : " + srcBook.getNumberOfSheets());
			System.out.println("sheet name : " + srcBook.getSheetName(0));
			int first = srcSheet.getFirstRowNum();
			int last  = srcSheet.getLastRowNum();
	 
			// ƒuƒbƒN‚ðì¬
			HSSFWorkbook destBook = new HSSFWorkbook();
			HSSFSheet destSheet = destBook.createSheet("hogehoge");
	 
	 
			HSSFRow  srcRow = null;
			HSSFCell srcCell = null;
			HSSFRow  destRow = null;
			HSSFCell destCell = null;
			HSSFFont font = destBook.createFont();
	 
	 
			srcSheet.addMergedRegion()
	 
			for(int nRow = first; nRow <= last; nRow++){
				srcRow  = srcSheet.getRow(nRow);
				destRow = destSheet.createRow(nRow);
				destRow.setHeight(srcRow.getHeight());
	 
				for(int nCell = 0; nCell < srcRow.getLastCellNum(); nCell++){
					srcCell = srcRow.getCell((short)nCell);
					destCell = destRow.createCell((short)nCell);
					destSheet.setColumnWidth((short)nCell, srcSheet.getColumnWidth((short)nCell));
	 
	 
	 
					HSSFCellStyle style = destBook.createCellStyle();
					HSSFCellStyle srcStyle = srcCell.getCellStyle();
	 
					style.setAlignment(srcStyle.getAlignment());
					style.setBorderBottom(srcStyle.getBorderBottom());
					style.setBorderLeft(srcStyle.getBorderLeft());
					style.setBorderRight(srcStyle.getBorderRight());
					style.setBorderTop(srcStyle.getBorderTop());
//                    style.setBottomBorderColor(srcStyle.getBorderLeft());
					style.setDataFormat(srcStyle.getDataFormat());
					style.setFillBackgroundColor(srcStyle.getFillBackgroundColor());
					style.setFillForegroundColor(srcStyle.getFillForegroundColor());
					style.setFillPattern(srcStyle.getFillPattern());
	 
					HSSFFont srcFont  = srcBook.getFontAt(srcStyle.getFontIndex());
					font.setBoldweight(srcFont.getBoldweight());
					font.setColor(srcFont.getColor());
					font.setFontHeight(srcFont.getFontHeight());
					font.setFontHeightInPoints(srcFont.getFontHeightInPoints());
					font.setFontName(srcFont.getFontName());
					font.setItalic(srcFont.getItalic());
					font.setStrikeout(srcFont.getStrikeout());
					font.setTypeOffset(srcFont.getTypeOffset());
					font.setUnderline(srcFont.getUnderline());
					style.setFont(font);
	 
					style.setHidden(srcStyle.getHidden());
					style.setIndention(srcStyle.getIndention());
					style.setLeftBorderColor(srcStyle.getLeftBorderColor());
					style.setRightBorderColor(srcStyle.getRightBorderColor());
					style.setRotation(srcStyle.getRotation());
					style.setTopBorderColor(srcStyle.getTopBorderColor());
					style.setVerticalAlignment(srcStyle.getVerticalAlignment());
					style.setWrapText(srcStyle.getWrapText());
	 
					destCell.setCellStyle(style);
	 
					destCell.setEncoding(HSSFCell.ENCODING_UTF_16);
					//destCell.setEncoding(srcCell.getEncoding());
					destCell.setCellType(srcCell.getCellType());
					switch(srcCell.getCellType()){
						case HSSFCell.CELL_TYPE_BLANK:
						case HSSFCell.CELL_TYPE_ERROR:
							break;
						case HSSFCell.CELL_TYPE_STRING:
							destCell.setCellValue(srcCell.getStringCellValue());
							break;
						case HSSFCell.CELL_TYPE_NUMERIC:
							destCell.setCellValue(srcCell.getNumericCellValue());
							break;
						case HSSFCell.CELL_TYPE_BOOLEAN:
							destCell.setCellValue(srcCell.getNumericCellValue());
							break;
						case HSSFCell.CELL_TYPE_FORMULA:
							destCell.setCellValue(srcCell.getCellFormula());
							break;
					}
	 
				}
			}
	 
			FileOutputStream out = new FileOutputStream(destFile);
			destBook.write(out);
			out.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	 
	}
	 */
}
