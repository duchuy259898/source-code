package com.geobeck.sosia.pos.swing.table;

import java.util.*;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.table.*;

public class ColumnGroup {
    
    protected TableCellRenderer renderer;
    protected List<TableColumn> v;
    protected String text;
    protected int margin=0;

    public ColumnGroup(String text) {
        this(null,text);
    }

    public ColumnGroup(TableCellRenderer renderer,String text) {
      
        if (renderer == null) {
            this.renderer = new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
                    JTableHeader header = table.getTableHeader();
                    if (header != null) {
                        setForeground(header.getForeground());
                        setBackground(header.getBackground());
                        setFont(header.getFont());
                    }
                    setHorizontalAlignment(JLabel.CENTER);
                    setText((value == null) ? "" : value.toString());
                    setBorder(UIManager.getBorder("TableHeader.cellBorder"));
                    return this;
                }
            };
        } else {
            this.renderer = renderer;
        }
        
        this.text = text;
        v = new ArrayList<TableColumn>();
  }

  
    public void add(TableColumn obj) {
        if (obj == null) return;
        v.add(obj);
    }

    public List<ColumnGroup> getColumnGroups(TableColumn c, ArrayList<ColumnGroup> g) {
        
        g.add(this);
        if (v.contains(c)) return g;
        
        for (Object obj : v) {
            if (obj instanceof ColumnGroup) {
                ArrayList<ColumnGroup> cgList = new ArrayList<ColumnGroup>((ArrayList<ColumnGroup>)g);
                List<ColumnGroup> groups = (List<ColumnGroup>)((ColumnGroup)obj).getColumnGroups(c, cgList);
                if (groups != null) return groups;
            }
        }
        
        return null;
    }
    
    public TableCellRenderer getHeaderRenderer() {
        return renderer;
    }
    
    public void setHeaderRenderer(TableCellRenderer renderer) {
        if (renderer != null) {
            this.renderer = renderer;
        }
    }

    public Object getHeaderValue() {
        return text;
    }
  
    public Dimension getSize(JTable table) {
        
        Component comp = renderer.getTableCellRendererComponent(table, getHeaderValue(), false, false,-1, -1);
        int height = comp.getPreferredSize().height; 
        int width  = 0;
        
        for (Object obj : v) {
            if (obj instanceof TableColumn) {
                TableColumn aColumn = (TableColumn)obj;
                width += aColumn.getWidth();
                width += margin;
            } else {
                width += ((ColumnGroup)obj).getSize(table).width;
            }
        }
        return new Dimension(width, height);
    }

    public void setColumnMargin(int margin) {
        this.margin = margin;
        for (Object obj : v) {
            if (obj instanceof ColumnGroup) {
                ((ColumnGroup)obj).setColumnMargin(margin);
            }
        }
    }
}