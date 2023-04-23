/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report.util;

import com.geobeck.sosia.pos.report.util.ReportManager;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.awt.Color;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import jxl.Sheet;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hssf.usermodel.HSSFChart.HSSFSeries;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author ivs
 */
public class JPOIApiSaleTransittion {

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

    public JPOIApiSaleTransittion(String title) {
        this.title = title;
    }

    HSSFCellStyle defaultCellStyle ;
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

    public void setFormular(int destinationRowNum, int startRow, int endRow) {
        // Get the source / new row
        HSSFRow newRow = null;
        newRow = sheet.getRow(destinationRowNum);

        // Loop through source columns to add to new row
        for (int i = 0; i < newRow.getLastCellNum(); i++) {
            // Grab a copy of the old/new cell
            HSSFCell oldCell = newRow.getCell(i);
            if (oldCell == null) {
                oldCell = null;
                continue;
            }
            // Set the cell data value
            switch (oldCell.getCellType()) {
                case Cell.CELL_TYPE_FORMULA:
                    if (i % 2 == 0) {
                        String strFormula = "";
                        if (i == 2) {
                            strFormula = "Sum(C" + startRow + ":" + "C" + endRow + ")";
                        } else if (i == 4) {
                            strFormula = "Sum(E" + startRow + ":" + "E" + endRow + ")";
                        } else if (i == 6) {
                            strFormula = "Sum(G" + startRow + ":" + "G" + endRow + ")";
                        } else if (i == 8) {
                            strFormula = "Sum(I" + startRow + ":" + "I" + endRow + ")";
                        } else if (i == 10) {
                            strFormula = "Sum(K" + startRow + ":" + "K" + endRow + ")";
                        } else if (i == 12) {
                            strFormula = "Sum(M" + startRow + ":" + "M" + endRow + ")";
                        } else if (i == 14) {
                            strFormula = "Sum(O" + startRow + ":" + "O" + endRow + ")";
                        } else if (i == 16) {
                            strFormula = "Sum(Q" + startRow + ":" + "Q" + endRow + ")";
                        } else if (i == 18) {
                            strFormula = "Sum(S" + startRow + ":" + "S" + endRow + ")";
                        } else if (i == 20) {
                            strFormula = "Sum(U" + startRow + ":" + "U" + endRow + ")";
                        } else if (i == 22) {
                            strFormula = "Sum(W" + startRow + ":" + "W" + endRow + ")";
                        } else if (i == 24) {
                            strFormula = "Sum(Y" + startRow + ":" + "Y" + endRow + ")";
                        } else if (i == 26) {
                            strFormula = "Sum(AA" + startRow + ":" + "AA" + endRow + ")";
                        }
                        oldCell.setCellFormula(strFormula);
                    }
                    break;
            }
        }

    }

    public void insertRow(int rowIndex) {
        try {
            sheet.shiftRows(rowIndex, rowIndex + 1, 1);
        } catch (Exception e) {
            System.out.print(e.toString());
        }
 
    }

    public void insertRow(int sourceRowNum, int destinationRowNum, int num) {
        sheet.shiftRows(destinationRowNum, sheet.getLastRowNum(), num, true, true, true);
        // Get the source / new row
        for (int inum = 0; inum < num; inum++) {
            HSSFRow newRow = null;
            newRow = sheet.getRow(destinationRowNum + inum);

            HSSFRow sourceRow = sheet.getRow(sourceRowNum);

            newRow.setHeight(sourceRow.getHeight());
            // Loop through source columns to add to new row
            for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
                // Grab a copy of the old/new cell
                HSSFCell oldCell = sourceRow.getCell(i);
                HSSFCell newCell = newRow.createCell(i);

                // If the old cell is null jump to next cell
                if (oldCell == null) {
                    newCell = null;
                    continue;
                }

                // Copy style from old cell and apply to new cell
                //  HSSFCellStyle newCellStyle = wb.createCellStyle();
                HSSFCellStyle newCellStyle = oldCell.getCellStyle();
                //newCellStyle.cloneStyleFrom(oldCell.getCellStyle());

                newCell.setCellStyle(newCellStyle);

                // If there is a cell comment, copy
                if (newCell.getCellComment() != null) {
                    newCell.setCellComment(oldCell.getCellComment());
                }

                // If there is a cell hyperlink, copy
                if (oldCell.getHyperlink() != null) {
                    newCell.setHyperlink(oldCell.getHyperlink());
                }

                // Set the cell data type
                newCell.setCellType(oldCell.getCellType());

                // Set the cell data value
                switch (oldCell.getCellType()) {
                    case Cell.CELL_TYPE_BLANK:
                        newCell.setCellValue(oldCell.getStringCellValue());
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        newCell.setCellValue(oldCell.getBooleanCellValue());
                        break;
                    case Cell.CELL_TYPE_ERROR:
                        newCell.setCellErrorValue(oldCell.getErrorCellValue());
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        String strFormula = "";
                        if (oldCell.getCellFormula().contains("SUM")) {
                            strFormula = oldCell.getCellFormula().replaceAll("[0-9]+", String.valueOf((destinationRowNum + 1)));
                        } else {
                            /*
                             * String[] temp =
                             * oldCell.getCellFormula().split("/"); String
                             * nameCell = temp[0].replaceAll("[0-9]+", "");
                             * strFormula = nameCell + (destinationRowNum + 1) +
                             * "/" + temp[1];
                             *
                             */
                            String[] temp = oldCell.getCellFormula().split("/");
                            String nameCell = temp[0].replaceAll("[0-9]+", "");
                            String[] temp2 = temp[1].split(",");
                            String nameCell2 = temp2[2].replaceAll("[0-9]+", "");
                            strFormula = nameCell + (destinationRowNum + 1) + "/" + temp2[0] + "," + temp2[1] + "," + nameCell2 + (destinationRowNum + 1) + "/" + temp[2];
                        }

                        newCell.setCellFormula(strFormula);
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        newCell.setCellValue(0);
                        break;
                    case Cell.CELL_TYPE_STRING:
                        newCell.setCellValue(0);
                        break;
                }
            }

            // If there are are any merged regions in the source row, copy to new row
            for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
                CellRangeAddress cellRangeAddress = sheet.getMergedRegion(i);
                if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
                    CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
                            (newRow.getRowNum()
                            + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
                            cellRangeAddress.getFirstColumn(),
                            cellRangeAddress.getLastColumn());
                    sheet.addMergedRegion(newCellRangeAddress);
                }
            }
        }

    }

    public void copyRow(int sourceRowNum, int destinationRowNum) {
        // Get the source / new row
        HSSFRow newRow = null;
        newRow = sheet.getRow(destinationRowNum);

        HSSFRow sourceRow = sheet.getRow(sourceRowNum);

        // If the row exist in destination, push down all rows by 1 else create a new row
        if (newRow != null) {
            sheet.shiftRows(destinationRowNum, sheet.getLastRowNum(), 1);
        } else {
            newRow = sheet.createRow(destinationRowNum);
        }

        // Loop through source columns to add to new row
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            // Grab a copy of the old/new cell
            HSSFCell oldCell = sourceRow.getCell(i);
            HSSFCell newCell = newRow.createCell(i);

            // If the old cell is null jump to next cell
            if (oldCell == null) {
                newCell = null;
                continue;
            }

            // Copy style from old cell and apply to new cell
            
            //HSSFCellStyle newCellStyle =  newCell.getCellStyle(); //= defaultCellStyle.cl;
            //newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
            //newCell.setCellStyle(newCellStyle);
            newCell.setCellStyle(oldCell.getCellStyle());
            
            // If there is a cell comment, copy
            if (newCell.getCellComment() != null) {
                newCell.setCellComment(oldCell.getCellComment());
            }

            // If there is a cell hyperlink, copy
            if (oldCell.getHyperlink() != null) {
                newCell.setHyperlink(oldCell.getHyperlink());
            }

            // Set the cell data type
            newCell.setCellType(oldCell.getCellType());

            // Set the cell data value
            switch (oldCell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_ERROR:
                    newCell.setCellErrorValue(oldCell.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    String strFormula = "";
                    if (oldCell.getCellFormula().contains("SUM")) {
                        strFormula = oldCell.getCellFormula().replaceAll("[0-9]+", String.valueOf((destinationRowNum + 1)));
                    } else {
                        /*
                         * String[] temp = oldCell.getCellFormula().split("/");
                         * String nameCell = temp[0].replaceAll("[0-9]+", "");
                         * strFormula = nameCell + (destinationRowNum + 1) + "/"
                         * + temp[1];
                         *
                         */
                        String[] temp = oldCell.getCellFormula().split("/");
                        String nameCell = temp[0].replaceAll("[0-9]+", "");
                        String[] temp2 = temp[1].split(",");
                        String nameCell2 = temp2[2].replaceAll("[0-9]+", "");
                        strFormula = nameCell + (destinationRowNum + 1) + "/" + temp2[0] + "," + temp2[1] + "," + nameCell2 + (destinationRowNum + 1) + "/" + temp[2];
                    }

                    newCell.setCellFormula(strFormula);
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    newCell.setCellValue(0);
                    break;
                case Cell.CELL_TYPE_STRING:
                    newCell.setCellValue(0);
                    break;
            }
        }

        // If there are are any merged regions in the source row, copy to new row
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress cellRangeAddress = sheet.getMergedRegion(i);
            if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
                CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
                        (newRow.getRowNum()
                        + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
                        cellRangeAddress.getFirstColumn(),
                        cellRangeAddress.getLastColumn());
                sheet.addMergedRegion(newCellRangeAddress);
            }
        }
    }

    public void removeRow(int rowIndex) {
        int lastRowNum = sheet.getLastRowNum();
        if (rowIndex >= 0 && rowIndex < lastRowNum) {
            sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
        }
        if (rowIndex == lastRowNum) {
            HSSFRow removingRow = sheet.getRow(rowIndex);
            if (removingRow != null) {
                sheet.removeRow(removingRow);
            }
        }
       
    }

    public void removeRowAt(int rowIndex) {
//        int lastRowNum = sheet.getLastRowNum();
//        if (rowIndex >= 0 && rowIndex < lastRowNum) {
//            sheet.shiftRows(rowIndex-1, lastRowNum, 1);
//        }
        HSSFRow removingRow = sheet.getRow(rowIndex - 1);
        if (removingRow != null) {
            sheet.removeRow(removingRow);
        }
    }

    public void DeleteRow(int rowIndex) {
        HSSFRow removingRow = sheet.getRow(rowIndex);

        sheet.removeRow(removingRow);   // this only deletes all the cell values

        int lastRowNum = sheet.getLastRowNum();

        if (rowIndex >= 0 && rowIndex < lastRowNum) {
            sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
        }
    }

    public void DeleteRowAt(int rowIndex) {

        int lastRowNum = sheet.getLastRowNum();

        if (rowIndex >= 0 && rowIndex < lastRowNum) {
            try {
                sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
            } catch (Exception e) {
            }
        }
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
         switch (cell.getCellType()) {
             
             case Cell.CELL_TYPE_BLANK:
                   return cell.getStringCellValue();
                    
                case Cell.CELL_TYPE_BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                   
                case Cell.CELL_TYPE_ERROR:
                    return String.valueOf(cell.getErrorCellValue());
                   
                
                case Cell.CELL_TYPE_NUMERIC:
                     return String.valueOf(cell.getNumericCellValue());

                case Cell.CELL_TYPE_STRING:
                    return String.valueOf(cell.getStringCellValue());
         }
        return cell.getStringCellValue();
    }

    public void setFormularActive() {

        HSSFFormulaEvaluator.evaluateAllFormulaCells(wb);

    }

    public void setTemplateFile(String templateFile) {

        try {
            InputStream is = getClass().getResourceAsStream(templateFile);

            wb = new HSSFWorkbook(is);
            defaultCellStyle= wb.createCellStyle();
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

    public void copyCell(HSSFCell cellOld, HSSFCell cellNew) {
        cellNew.setCellStyle(cellOld.getCellStyle());
        
        //cellNew.setEncoding(cellOld.());
        cellNew.setCellValue("");
        switch (cellOld.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                try {
                cellNew.setCellValue(cellOld.getStringCellValue());
                }catch (Exception e) {
                    
                }
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                try {
                cellNew.setCellValue(cellOld.getNumericCellValue());
                }catch (Exception e) {
                    
                }
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                try {
                cellNew.setCellValue(HSSFCell.CELL_TYPE_BLANK);
                }catch (Exception e) {
                    
                }
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                try {
                cellNew.setCellValue(cellOld.getBooleanCellValue());
                }catch (Exception e) {
                    
                }
                break;
        }
        
    }

    public HSSFCell getCell(int colNum, int rowNum) {
        colNum--;
        rowNum--;
        //エクセルの行変数
        HSSFRow row = null;
        //エクセルのセル変数
        HSSFCell cell = null;
        row = sheet.getRow(rowNum);
        if(row==null) {
            row = sheet.createRow(rowNum);
        }
        cell = row.getCell(convertIntToShort(colNum));
        if(cell==null) {
            cell = row.createCell(colNum,1);
        }
        return cell;
    }

    public void CreateCell(int row, int column, String value) {
        HSSFRow existingRow = sheet.getRow(row);
        Cell existingCell = existingRow.getCell(column);
        CellStyle currentStyle = existingCell.getCellStyle();

        //cell.setCellValue(date);
        //apply previous style      

        //HSSFRow row = sheet.createRow(1);
        HSSFCell cell = existingRow.createCell(column);
        cell.setCellValue(new HSSFRichTextString(value));
        //cell.setCellStyle(style);      
        cell.setCellStyle(currentStyle);
        //sheet.autoSizeColumn((short) 1);
    }

    public void RemoveCell(int row, int column) {
        HSSFCell cell = sheet.getRow(row).getCell(column);
        sheet.getRow(row).removeCell(cell);
        
    }
}
