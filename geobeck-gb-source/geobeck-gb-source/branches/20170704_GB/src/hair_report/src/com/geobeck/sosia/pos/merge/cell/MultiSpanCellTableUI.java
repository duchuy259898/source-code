/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.merge.cell;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;
import javax.swing.table.*;

/**
 *
 * @author Tran Thi Mai Loan
 */
public class MultiSpanCellTableUI extends BasicTableUI {
    
 @Override
  public void paint(Graphics g, JComponent c) {
    Rectangle clip = g.getClipBounds();
    Rectangle bounds = table.getBounds();
    bounds.x = bounds.y =0;
    int firstIndex = table.rowAtPoint(new Point(0, bounds.y));
    int  lastIndex = table.getRowCount()-1;
    Rectangle rowRect = new Rectangle(0,0, clip.width , table.getRowHeight() + table.getRowMargin());
    rowRect.y = firstIndex*rowRect.height;
    for (int index = firstIndex; index <= lastIndex; index++) {
      if (rowRect.intersects(clip)) {
	paintRow(g, index);
      }
      rowRect.y += rowRect.height;
    }
  }


  private void paintRow(Graphics g, int row) {
    Rectangle rect = g.getClipBounds();
    boolean drawn  = false;
    
    AttributiveCellTableModel tableModel = (AttributiveCellTableModel)table.getModel();
    CellSpan cellAtt = (CellSpan)tableModel.getCellAttribute();
    int numColumns = table.getColumnCount();
 
    for (int column = 0; column < numColumns; column++) {
   
      Rectangle cellRect = table.getCellRect(row,column,true);
      int cellRow,cellColumn;
      if (cellAtt.isVisible(row,column)) {
	cellRow    = row;
	cellColumn = column;
        // System.out.print("   "+column+" ");  // debug
      } else {
	cellRow    = row + cellAtt.getSpan(row,column)[CellSpan.ROW];
	cellColumn = column + cellAtt.getSpan(row,column)[CellSpan.COLUMN];
        //System.out.print("  ("+column+")");  // debug
      }
      if (cellRect.intersects(rect)) {
	drawn = true;
	paintCell(g, cellRect, cellRow, cellColumn);
      } else {
	if (drawn) break;
      } 
    }

  }

  private void paintCell(Graphics g, Rectangle cellRect, int row, int column) {
    int spacingHeight = table.getRowMargin();
    int spacingWidth  = table.getColumnModel().getColumnMargin();
    Color c = g.getColor();
    g.setColor(table.getGridColor());
    g.drawRect(cellRect.x,cellRect.y,cellRect.width-spacingWidth,cellRect.height-spacingHeight);
    g.setColor(c);
    cellRect.setBounds(cellRect.x, cellRect.y, cellRect.width - spacingWidth, cellRect.height -spacingHeight);
    
    if (table.isEditing() && table.getEditingRow()==row &&
	table.getEditingColumn()==column) {
      Component component = table.getEditorComponent();
      component.setBounds(cellRect);
      component.validate();
    }
    else {
      TableCellRenderer renderer = table.getCellRenderer(row, column);
  
      Component component = table.prepareRenderer(renderer, row, column);

      if (component.getParent() == null) {
	rendererPane.add(component);
      }
      rendererPane.paintComponent(g, component, table, cellRect.x, cellRect.y,
				  cellRect.width, cellRect.height, true);
    }
  }    
}
