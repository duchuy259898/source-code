/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.merge.cell;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

/**
 *
 * @author Tran Thi Mai Loan
 */

public class AttributiveCellTableModel extends DefaultTableModel {

  protected CellAttribute cellAtt;
    
  public AttributiveCellTableModel() {
    this((Vector)null, 0);
  }

  public AttributiveCellTableModel(Vector columnNames, int numRows) {
    setColumnIdentifiers(columnNames);
    dataVector = new Vector();
    setNumRows(numRows);
    cellAtt = new DefaultCellAttribute(numRows,columnNames.size());
  }
  public AttributiveCellTableModel(Object[] columnNames, int numRows) {
    this(convertToVector(columnNames), numRows);
  }  
  public AttributiveCellTableModel(Vector data, Vector columnNames) {
    setDataVector(data, columnNames);
  }
  public AttributiveCellTableModel(Object[][] data, Object[] columnNames) {
    setDataVector(data, columnNames);
  }


  public void addColumn(Object columnName, Vector columnData) {
    if (columnName == null)
      throw new IllegalArgumentException("addColumn() - null parameter");
    columnIdentifiers.addElement(columnName);
    int index = 0;
    Enumeration enumeration = dataVector.elements();
    while (enumeration.hasMoreElements()) {
      Object value;
      if ((columnData != null) && (index < columnData.size()))
	  value = columnData.elementAt(index);
      else
	value = null;
      ((Vector)enumeration.nextElement()).addElement(value);
      index++;
    }

    cellAtt.addColumn();
    fireTableStructureChanged();
  }

   public void addRow(Vector rowData) {
    dataVector.addElement(rowData);
    cellAtt.addRow();
  }
   
  public void insertRow(int row, Vector rowData) {
    if (rowData == null) {
      rowData = new Vector(getColumnCount());
    }
    else {
      rowData.setSize(getColumnCount());
    }
    dataVector.insertElementAt(rowData, row);
    cellAtt.insertRow(row);
    newRowsAdded(new TableModelEvent(this, row, row,
       TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
  }

  public CellAttribute getCellAttribute() {
    return cellAtt;
  }

  public void setCellAttribute(CellAttribute newCellAtt) {
    cellAtt = newCellAtt;
  }
 
}
