/*
 * JExcelApi.java
 *
 * Created on 2009/04/22, 10:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.logging.*;
import java.util.Calendar;

import jxl.Workbook;
import jxl.write.WritableWorkbook;
import jxl.write.WritableSheet;
import jxl.write.WritableCell;
import jxl.write.WritableCellFeatures;
import jxl.write.WritableCellFormat;
import com.geobeck.sosia.pos.report.util.ReportManager;
import com.geobeck.sosia.pos.system.SystemInfo;
import jxl.CellView;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.write.Blank;
import jxl.write.WritableFont;

/**
 *
 * @author geobeck
 */
public class JExcelApi {

    private WritableWorkbook workbook = null;
    private WritableSheet sheet = null;
    private String title = "";
    private String exportFile = "";
    private String file = "";
    /**
     * Creates a new instance of JExcelApi
     */
    public JExcelApi(String title) {
        this.title = title;
    }
   
    public String getFilePath()
    {
        return this.exportFile;
    }
    public void setTemplateFile(String templateFile) {

        try {

            // ファイル保存ダイアログが表示されるようにテンプレートとして作成する
            exportFile =
                    ReportManager.getExportPath()
                    + this.title
                    + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date())
                    + ".xlt";

            workbook = Workbook.createWorkbook(new File(exportFile), Workbook.getWorkbook(getClass().getResourceAsStream(templateFile)));

            // １番目のシートを取得
            this.sheet = workbook.getSheet(0);

            // ページ設定のデフォルト
            setDefaultSheetSettings();

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
        }
    }

    private void setDefaultSheetSettings() {

        //------------------------------
        // ページ設定のデフォルト
        //------------------------------

        jxl.SheetSettings settings = sheet.getSettings();

        // 拡大縮小
        settings.setFitToPages(false);

    }

    public WritableSheet getTargetSheet() {
        return sheet;
    }

    public void setTargetSheet(int index) {
        sheet = workbook.getSheet(--index);
    }

    public void setTargetSheet(String name) {
        sheet = workbook.getSheet(name);
    }

    public void insertRow(int rowIndex) {
        try{
        sheet.insertRow(rowIndex);
        for (int col = 0; col < sheet.getColumns(); col++) {
            sheet.getWritableCell(col, rowIndex).copyTo(col, rowIndex + 1);
            //sheet.getWritableCell(col, rowIndex + 1).setCellFormat(sheet.getWritableCell(col, rowIndex).getCellFormat());
            //sheet.getWritableCell(col, rowIndex+1).setCellFeatures(sheet.getWritableCell(col,rowIndex).getCellFeatures());
        }
       }catch (Exception e){
          System.out.print(e.toString());
       }
    }

    public void insertRow(int baseRow, int capacity) {

        if (capacity == 0) {
            return;
        }

        // コピー元の行高さ取得
        int height = sheet.getRowView(baseRow - 1).getSize();

        // 指定行数分コピーする
        for (int i = 0; i < capacity; i++) {
            sheet.insertRow(baseRow);
            try {
                // 行高さ設定
                sheet.setRowView(baseRow, height);
            } catch (Exception e) {
                SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);

            }
        }

        // コピーした行の書式を設定する
        for (int i = 0; i < capacity; i++) {

            int row = baseRow + i;

            for (int col = 0; col < sheet.getColumns(); col++) {

                try {

                    WritableCell c = sheet.getWritableCell(col, baseRow - 1);
                    sheet.addCell(c.copyTo(col, row));

                } catch (Exception e) {
                    SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
                }
            }
        }
    }

    public void insertColumn(int baseColumn, int capacity) {

        if (capacity == 0) {
            return;
        }

        CellView view = sheet.getColumnView(baseColumn);

        // 指定列数分コピーする
        for (int i = 0; i < capacity; i++) {
            sheet.insertColumn(baseColumn);
        }

        // コピーした列の書式を設定する
        for (int i = 0; i < capacity; i++) {

            int col = baseColumn + i;

            sheet.setColumnView(col, view);

            for (int row = 0; row < sheet.getRows(); row++) {

                try {

                    WritableCell c = sheet.getWritableCell(baseColumn - 1, row);
                    sheet.addCell(c.copyTo(col, row));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void removeRow(int row) {
        row--;
        sheet.removeRow(row);
    }

    public void removeColumn(int column) {
        column--;
        sheet.removeColumn(column);
    }

    public void setValue(int col, int row, Object value) {
        this.setValue(col, row, value, null);
    }

    public void setValue(int col, int row, Object value, Colour colour) {

        col--;
        row--;

        try {

            WritableCell cell = sheet.getWritableCell(col, row);

            if (value == null) {

                jxl.write.Blank blank = new jxl.write.Blank(col, row);
                if (cell.getCellFeatures() != null) {
                    blank.setCellFeatures(new WritableCellFeatures(cell.getCellFeatures()));
                }
                blank.setCellFormat(cell.getCellFormat());

                sheet.addCell(blank);

            } else if (value instanceof java.lang.String) {

                jxl.write.Label label = new jxl.write.Label(col, row, value.toString());
                if (cell.getCellFeatures() != null) {
                    label.setCellFeatures(new WritableCellFeatures(cell.getCellFeatures()));
                }

                if (colour != null) {
                    WritableCellFormat format = new WritableCellFormat(cell.getCellFormat());
                    WritableFont font = new WritableFont(format.getFont());
                    font.setColour(colour);
                    format.setFont(font);
                    label.setCellFormat(format);
                } else {
                    label.setCellFormat(cell.getCellFormat());
                }

                sheet.addCell(label);

            } else if (value instanceof java.util.Date) {

                Calendar cal = Calendar.getInstance();
                cal.setTime((java.util.Date) value);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.add(Calendar.HOUR_OF_DAY, 9);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);

                jxl.write.DateTime date = new jxl.write.DateTime(col, row, cal.getTime());

                if (cell.getCellFeatures() != null) {
                    date.setCellFeatures(new WritableCellFeatures(cell.getCellFeatures()));
                }

                if (colour != null) {
                    WritableCellFormat format = new WritableCellFormat(cell.getCellFormat());
                    WritableFont font = new WritableFont(format.getFont());
                    font.setColour(colour);
                    format.setFont(font);
                    date.setCellFormat(format);
                } else {
                    date.setCellFormat(cell.getCellFormat());
                }

                sheet.addCell(date);

            } else if (value instanceof Calendar) {

                Calendar cal = (Calendar) value;
                cal.add(Calendar.HOUR_OF_DAY, 9);

                jxl.write.DateTime date = new jxl.write.DateTime(col, row, cal.getTime());

                if (cell.getCellFeatures() != null) {
                    date.setCellFeatures(new WritableCellFeatures(cell.getCellFeatures()));
                }

                if (colour != null) {
                    WritableCellFormat format = new WritableCellFormat(cell.getCellFormat());
                    WritableFont font = new WritableFont(format.getFont());
                    font.setColour(colour);
                    format.setFont(font);
                    date.setCellFormat(format);
                } else {
                    date.setCellFormat(cell.getCellFormat());
                }

                sheet.addCell(date);

            } else {

                jxl.write.Number number = new jxl.write.Number(col, row, Double.valueOf(value.toString()));
                if (cell.getCellFeatures() != null) {
                    number.setCellFeatures(new WritableCellFeatures(cell.getCellFeatures()));
                }

                if (colour != null) {
                    WritableCellFormat format = new WritableCellFormat(cell.getCellFormat());
                    WritableFont font = new WritableFont(format.getFont());
                    font.setColour(colour);
                    format.setFont(font);
                    number.setCellFormat(format);
                } else {
                    number.setCellFormat(cell.getCellFormat());
                }

                sheet.addCell(number);
            }

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
        }

    }

    public String getValue(int col, int row) {

        col--;
        row--;

        return sheet.getCell(col, row).getContents();
    }
    /*public WritableWorkbook setWorkBook()
    {
        
    }*/
    
    public void openWorkbook() {
        try {

            workbook.write();
            workbook.close();
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
     public void openWorkbook(String temp) {
        try {

            //workbook.write();
            //workbook.close();
            String p = "cmd /c \"" + temp + "\"";

            Runtime runtime = Runtime.getRuntime();
            if (SystemInfo.isWindows()) {
                runtime.exec("cmd /c \"" + temp + "\"");
            } else {
                runtime.exec("open " + temp);
            }

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
        }
    }
    public void openWorkBookHidden()
    {
        try {
            workbook.write();
            workbook.close();
         } catch (Exception e) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
        }
    }
    public void mergeCells(int col1, int row1, int col2, int row2) {

        col1--;
        row1--;
        col2--;
        row2--;

        try {

            sheet.mergeCells(col1, row1, col2, row2);

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
        }

    }

    public void setUpperLine(int col, int row) {

        col--;
        row--;

        try {

            WritableCellFormat format = new WritableCellFormat(sheet.getWritableCell(col, row).getCellFormat());
            format.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
            sheet.getWritableCell(col, row).setCellFormat(format);

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
        }

    }

    public void setUnderLine(int col, int row) {

        col--;
        row--;

        try {

            WritableCellFormat format = new WritableCellFormat(sheet.getWritableCell(col, row).getCellFormat());
            format.setBorder(jxl.format.Border.BOTTOM, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
            sheet.getWritableCell(col, row).setCellFormat(format);

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
        }

    }

    public void copySheet(int index, String name) {
        copySheet(index, name, workbook.getSheets().length + 1);
    }

    public void copySheet(int index, String name, int pos) {
        workbook.copySheet(--index, name, pos);
    }

    public void removeSheet(int index) {
        workbook.removeSheet(--index);
    }

    public void setPrintTitlesRow(int firstRow, int lastRow) {

        firstRow--;
        lastRow--;

        // ページ設定　行のタイトル
        sheet.getSettings().setPrintTitlesRow(firstRow, lastRow);
    }
}
