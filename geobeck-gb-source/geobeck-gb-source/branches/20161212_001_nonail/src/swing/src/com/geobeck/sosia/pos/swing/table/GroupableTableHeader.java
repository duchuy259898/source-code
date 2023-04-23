package com.geobeck.sosia.pos.swing.table;

import java.util.*;
import javax.swing.table.*;

public class GroupableTableHeader extends JTableHeader {

    private static final String uiClassID = "GroupableTableHeaderUI";
    protected List<ColumnGroup> columnGroups = null;
    
    public GroupableTableHeader(TableColumnModel model) {
        super(model);
        setUI(new GroupableTableHeaderUI());
        setReorderingAllowed(false);
    }
    
    public void updateUI(){
        setUI(new GroupableTableHeaderUI());
    }
  
    public void setReorderingAllowed(boolean b) {
        reorderingAllowed = false;
    }
    
    public void addColumnGroup(ColumnGroup g) {
        if (columnGroups == null) {
            columnGroups = new ArrayList<ColumnGroup>();
        }
        columnGroups.add(g);
    }

    public Enumeration getColumnGroups(TableColumn col) {
        if (columnGroups == null) return null;
        
        for (ColumnGroup cGroup : columnGroups) {
            Vector<ColumnGroup> v_ret = null;
            List<ColumnGroup> l = cGroup.getColumnGroups(col,new ArrayList<ColumnGroup>());
            if (l != null) {
                v_ret = new Vector<ColumnGroup>();
                for (ColumnGroup cg : l) {
                    v_ret.addElement(cg);
                }
            }
            if (v_ret != null) { 
                return v_ret.elements();
            }
        }
        
        return null;
    }
  
    public void setColumnMargin() {
        if (columnGroups == null) return;
        int columnMargin = getColumnModel().getColumnMargin();
        for (ColumnGroup cGroup : columnGroups) {
            cGroup.setColumnMargin(0);
        }
    }
  
}
