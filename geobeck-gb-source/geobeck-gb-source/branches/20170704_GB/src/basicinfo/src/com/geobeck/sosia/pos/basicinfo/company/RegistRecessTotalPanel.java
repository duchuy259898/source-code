/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.basicinfo.company;

import com.geobeck.sosia.pos.hair.master.company.MstAPI;
import com.geobeck.sosia.pos.master.company.MstShifts;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.master.company.MstStaffs;
import com.geobeck.sosia.pos.swing.SelectTableCellRenderer;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.swing.CustomFilter;
import com.geobeck.swing.MessageDialog;
import com.geobeck.swing.SwingUtil;
import com.geobeck.util.SQLUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import org.apache.commons.collections.map.LinkedMap;

/**
 *
 * @author nhtvinh
 */
public class RegistRecessTotalPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {
    private MstShop   shop;
    private Date      scheduleDate;
    private     DataSchedules   scheds;
    private MstShifts shifts;
    private int recessId; //if update then recessId > 0
    private MstAPI mstApi = null;
    private List<DataRecess> listDataRecess; //corresponding data of recessTable
    private DataRecess recessSelect;
    private boolean isUpdate;
    private Integer allianceRecessId;
    //IVS_NHTVINH start add 2016/10/26 New request #57853 
    //[gb]Ç©ÇÒÇ¥ÇµAPIóp_ã@î\í«â¡ÅiÉXÉ^ÉbÉtÉVÉtÉgãxåeìoò^âÊñ Åj_ÉwÉãÉvÇ∆ãxåeÇÃèdï°ïsâ¬ëŒâû
    private DateFormat dateFormatFull = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
    private DateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");
    //IVS_NHTVINH end add 2016/10/26 New request #57853 
    
    static public boolean ShowDialog(Frame owner, MstShop shop, Date targetDate ) {

        SystemInfo.getLogger().log(Level.INFO, "ãxåeìoò^");
        
        RegistRecessTotalPanel dlg = new RegistRecessTotalPanel();
//        dlg.ShowCloseBtn();
        dlg.shop = shop;
        dlg.scheduleDate = targetDate;
        dlg.addMouseCursorChange();
        dlg.loadData();
        //IVS_LVTu start add 2016/11/23 New request #58702
        SystemInfo.getMstUser().setShopID(shop.getShopID());
        //IVS_LVTu end add 2016/11/23 New request #58702
        
        SwingUtil.openAnchorDialog(owner, true, dlg, "ãxåeìoò^", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER);
        
        dlg.dispose();
        return true;
    }  
    
    private void loadData() {
        try{
            //load shop name
            cblShop.setText(shop.getShopName());
            
            //load schedule date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy'îN'MM'åé'dd'ì˙'");
            targetDate.setText(sdf.format(scheduleDate));        
        
            //load shift time shop
            StringBuilder sb = new StringBuilder();
            if( shop != null ) {
                sb.append("<html>");
                sb.append(this.getTimeStr(shop.getOpenHour(), shop.getOpenMinute()));
                sb.append("Å`" );
                sb.append(this.getTimeStr(shop.getCloseHour(), shop.getCloseMinute()));
                sb.append("</html>");
            }
            lbShiftTimeShop.setText(sb.toString());
            
            //load list staff
            loadListStaff();
            
            //load list time
            loadListTime();
            
            //load data table
            loadRecessTable();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(btnNewRow);
        SystemInfo.addMouseCursorChange(btnClose);
        SystemInfo.addMouseCursorChange(deleteButton);
        SystemInfo.addMouseCursorChange(updateButton);
    }
    
    public void loadRecessTable() {
        try{ 
            DataSchedules   schedsTemp;
            scheds = new DataSchedules();
            schedsTemp= new DataSchedules();
            shifts = new MstShifts();
            listDataRecess = new LinkedList<DataRecess>();
            
            scheds.setScheduleDate(scheduleDate);
            scheds.setShop(shop);
            scheds.setOpener(this);
            shifts.setShopId(shop.getShopID());
            schedsTemp.setScheduleDate(scheduleDate);
            schedsTemp.setShop(shop);
            
            scheds.clear();
            schedsTemp.clear();
            shifts.clear();
        
            scheds.load(SystemInfo.getConnection(), false);
            schedsTemp.loadCheduleDetail(SystemInfo.getConnection());
            shifts.load(SystemInfo.getConnection(), false);
            
            DataSchedules schedsActi = new DataSchedules();
            for( DataSchedule sched : scheds ) {
                if( sched.getShiftId() != 0 && sched.getShopId().equals(shop.getShopID())) {
                    schedsActi.add(sched);
                }
            }
            
            
            this.recessTable.removeAll();
            DefaultTableModel model = (DefaultTableModel)recessTable.getModel();
            model.setRowCount(0);
            int rowCount = schedsActi.size();
            
            for( int row = 0; row < rowCount; row++ ) {
                DataSchedule sched = schedsActi.get(row);
                DataRecesses recesses = sched.getRecesses();
                
                MstStaff staff = new MstStaff();
                staff.setStaffID(sched.getStaffId());
                try{
                    staff.load(SystemInfo.getConnection());
                }catch(SQLException e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                }
                if (recesses.size() > 0) {
                    Collections.sort(recesses, new Comparator<DataRecess>() {
                        @Override
                        public int compare(DataRecess t, DataRecess t1) {
                            return t.getStartTime().compareTo(t1.getStartTime());
                        }
                    });
                  }
                for(DataRecess dataRecess : recesses) {
                    listDataRecess.add(dataRecess);
                    StringBuilder shiftTime = new StringBuilder();
                    if((!dataRecess.getStartTime().trim().equals("") && (!dataRecess.getStartTime().equals("")))){
                        shiftTime.append(dataRecess.getStartTime().substring(0, 2));
                        shiftTime.append(":");
                        shiftTime.append(dataRecess.getStartTime().substring(2, 4));
                        shiftTime.append("Å`" );
                        shiftTime.append(dataRecess.getEndTime().substring(0, 2));
                        shiftTime.append(":");
                        shiftTime.append(dataRecess.getEndTime().substring(2, 4));
                        shiftTime.append("");
                    }
                    String staffName = "";
                    for(String name : staff.getStaffName()){
                        staffName += (name + "  ");
                    }
                    Object[] rowData = {dataRecess, staffName, shiftTime.toString(), dataRecess.getNote()};
                    
                    model.addRow(rowData);
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadListStaff() {
        try{
            scheds = new DataSchedules();
            scheds.setScheduleDate(scheduleDate);
            scheds.setShop(shop);
            scheds.setOpener(this);
            scheds.clear();
            scheds.load(SystemInfo.getConnection(), false);
            
            MstStaffs staffs = new MstStaffs();
            staffs.setShopIDList(this.shop.getShopID().toString());
            try{
                staffs.loadOnlyShop(SystemInfo.getConnection(), false);
            }catch(SQLException e){
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            chargeStaff.addItem(new MstStaff());
            for ( MstStaff ms : staffs) {
                for( DataSchedule sched : scheds ) {
                    if(ms.getStaffID().equals(sched.getStaffId()) && sched.getShiftId() != 0 && sched.getShopId().equals(shop.getShopID())) {
                        chargeStaff.addItem(ms);
                    }
                }
            }
            if(chargeStaff.getItemCount() > 0) {
                chargeStaff.setSelectedIndex(0);
            }   
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadListTime() {
        try {
            ((JLabel)recessStartTime.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
            ((JLabel)recessEndTime.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
            
            JTextField recessStartTimeField = (JTextField)recessStartTime.getEditor().getEditorComponent();
            recessStartTimeField.setHorizontalAlignment(JTextField.CENTER);
            JTextField recessEndTimeField = (JTextField)recessEndTime.getEditor().getEditorComponent();
            recessEndTimeField.setHorizontalAlignment(JTextField.CENTER);
            recessStartTimeField.setEditable(false);
            recessEndTimeField.setEditable(false);
        
            Integer term        = null;            
            Integer openHour    = null;       
            Integer openMinute  = null;     
            Integer closeHour   = null;      
            Integer closeMinute = null;    
            if ( shop.getShopID() != null) {
                term        = shop.getTerm();          
                openHour    = shop.getOpenHour();       
                openMinute  = shop.getOpenMinute();     
                closeHour   = shop.getCloseHour();      
                closeMinute = shop.getCloseMinute();    
            }

            if (openHour != null && closeHour != null && term != null) {

                for (int h = openHour; h <= closeHour; h++) {
                    for (int m = 0; m < 60; m += term) {
                        if (h == openHour && m < openMinute) continue;
                        if (h == closeHour && closeMinute < m) break;
                        recessStartTime.addItem(String.format("%1$02d", h) + ":" + String.format("%1$02d", m));
                        recessEndTime.addItem(String.format("%1$02d", h) + ":" + String.format("%1$02d", m));
                   }
                }
            }
            recessStartTime.setSelectedIndex(-1);
            recessEndTime.setSelectedIndex(-1);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private String getTimeStr(Integer hour, Integer minute) {
        String strHour = hour < 10 ? "0" + hour : "" + hour; 
        String strMinute = minute < 10 ? "0" + minute : "" + minute; 
        
        return strHour + ":" + strMinute;        
    }
    
    private void dispose() {
        if(this.isDialog()) {
            ((JDialog)this.getParent().getParent().getParent().getParent()).dispose();
        }
    }

    /**
     * Creates new form RegistRecessTotalPanel
     */
    public RegistRecessTotalPanel() {
        initComponents();
        
        this.setTitle("ãxåeéûä‘ìoò^");
        
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.CENTER);
        
        int nBasePanelWidth = new ShiftPanelBase().getPreferredSize().width;
        this.setSize(150 + nBasePanelWidth * 3 + 50,600);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        cblShop = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        targetDate = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lbShiftTimeShop = new javax.swing.JLabel();
        staffLabel = new javax.swing.JLabel();
        chargeStaff = new javax.swing.JComboBox();
        recessStartTimeLabel = new javax.swing.JLabel();
        recessStartTime = new javax.swing.JComboBox();
        Component cs = recessStartTime.getEditor().getEditorComponent();
        PlainDocument docs = (PlainDocument)((JTextComponent)cs).getDocument();
        docs.setDocumentFilter(new CustomFilter(5, "0-9:"));
        recessStartTimeLabel1 = new javax.swing.JLabel();
        recessEndTime = new javax.swing.JComboBox();
        Component ce = recessEndTime.getEditor().getEditorComponent();
        PlainDocument doce = (PlainDocument)((JTextComponent)ce).getDocument();
        doce.setDocumentFilter(new CustomFilter(5, "0-9:"));
        chargeStaffNo = new javax.swing.JTextField();
        memoLabel1 = new javax.swing.JLabel();
        noteScrollPane1 = new javax.swing.JScrollPane();
        recessNote = new com.geobeck.swing.JTextAreaEx();
        deleteButton = new javax.swing.JButton();
        updateButton = new javax.swing.JButton();
        btnNewRow = new javax.swing.JButton();
        bedsScrollPane = new javax.swing.JScrollPane();
        recessTable = new javax.swing.JTable();
        btnClose = new javax.swing.JButton();
        btnClose.setContentAreaFilled(false);

        setPreferredSize(new java.awt.Dimension(988, 589));

        jLabel1.setText("ëŒè€");

        cblShop.setText("xxx");

        jLabel3.setText("ëŒè€ì˙");

        targetDate.setText("xxx");

        jLabel5.setText("âcã∆éûä‘");

        lbShiftTimeShop.setText("xxx");

        staffLabel.setText("íSìñé“");

        chargeStaff.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
        chargeStaff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        chargeStaff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chargeStaffActionPerformed(evt);
            }
        });

        recessStartTimeLabel.setText("ãxåeéûä‘");

        recessStartTime.setEditable(true);
        recessStartTime.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
        recessStartTime.setMaximumRowCount(10);
        recessStartTime.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                recessStartTimeItemStateChanged(evt);
            }
        });
        recessStartTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recessStartTimeActionPerformed(evt);
            }
        });

        recessStartTimeLabel1.setText("Å`");

        recessEndTime.setEditable(true);
        recessEndTime.setFont(new java.awt.Font("MS UI Gothic", 0, 14)); // NOI18N
        recessEndTime.setMaximumRowCount(10);
        recessEndTime.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                recessEndTimeItemStateChanged(evt);
            }
        });
        recessEndTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recessEndTimeActionPerformed(evt);
            }
        });

        chargeStaffNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chargeStaffNoActionPerformed(evt);
            }
        });
        chargeStaffNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                chargeStaffNoFocusLost(evt);
            }
        });

        memoLabel1.setText("ÉRÉÅÉìÉg");

        noteScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        recessNote.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        recessNote.setColumns(20);
        recessNote.setLineWrap(true);
        recessNote.setRows(5);
        recessNote.setTabSize(4);
        recessNote.setInputKanji(true);
        noteScrollPane1.setViewportView(recessNote);

        deleteButton.setBackground(new java.awt.Color(255, 255, 255));
        deleteButton.setForeground(new java.awt.Color(255, 255, 255));
        deleteButton.setBorderPainted(false);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        updateButton.setBackground(new java.awt.Color(255, 255, 255));
        updateButton.setForeground(new java.awt.Color(255, 255, 255));
        updateButton.setBorderPainted(false);
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        btnNewRow.setBackground(new java.awt.Color(255, 255, 255));
        btnNewRow.setForeground(new java.awt.Color(255, 255, 255));
        btnNewRow.setBorderPainted(false);
        btnNewRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewRowActionPerformed(evt);
            }
        });

        bedsScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        recessTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "ÉXÉ^ÉbÉtñº", "ãxåeéûä‘", "ÉRÉÅÉìÉg"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        recessTable.setSelectionBackground(new java.awt.Color(220, 220, 220));
        recessTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        recessTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        recessTable.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(recessTable, SystemInfo.getTableHeaderRenderer());
        recessTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(recessTable);
        recessTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                recessTableMouseClicked(evt);
            }
        });
        recessTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                recessTableKeyReleased(evt);
            }
        });
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        recessTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        recessTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        recessTable.getColumnModel().getColumn(0).setMinWidth(0);
        recessTable.getColumnModel().getColumn(0).setMaxWidth(0);
        recessTable.getColumnModel().getColumn(1).setMaxWidth(250);
        recessTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        recessTable.getColumnModel().getColumn(2).setMaxWidth(200);
        recessTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        recessTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        recessTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (recessTable.getSelectedRow() > -1) {
                    loadRowDataUpdate();
                }
            }
        });
        bedsScrollPane.setViewportView(recessTable);

        btnClose.setBackground(new java.awt.Color(255, 255, 255));
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
        btnClose.setBorderPainted(false);
        btnClose.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bedsScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(staffLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(chargeStaffNo, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(chargeStaff, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(23, 23, 23)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(memoLabel1)
                                        .addGap(18, 18, 18)
                                        .addComponent(noteScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addComponent(recessStartTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(recessStartTime, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(recessStartTimeLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(recessEndTime, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(25, 41, Short.MAX_VALUE)
                                .addComponent(btnNewRow, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cblShop)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(targetDate)
                                        .addGap(26, 26, 26)
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbShiftTimeShop)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(28, 28, 28))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cblShop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnNewRow, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(updateButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(31, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(targetDate, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbShiftTimeShop, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnClose, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(recessEndTime, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(recessStartTimeLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(recessStartTime, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(recessStartTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(chargeStaff, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(staffLabel)
                                .addComponent(chargeStaffNo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(deleteButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(memoLabel1)
                    .addComponent(noteScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(bedsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                .addContainerGap())
        );

        deleteButton.setEnabled(false);
        deleteButton.setIcon(SystemInfo.getImageIcon("/button/common/delete_off.jpg"));
        deleteButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/delete_on.jpg"));
        updateButton.setEnabled(false);
        updateButton.setIcon(SystemInfo.getImageIcon("/button/common/update_off.jpg"));
        updateButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/update_on.jpg"));
        btnNewRow.setIcon(SystemInfo.getImageIcon("/button/common/insert_off.jpg"));
        btnNewRow.setPressedIcon(SystemInfo.getImageIcon("/button/common/insert_on.jpg"));
    }// </editor-fold>//GEN-END:initComponents

    private void chargeStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargeStaffActionPerformed
        if (chargeStaffNo.getName() != null) return;

        MstStaff ms= (MstStaff)chargeStaff.getSelectedItem();

        if (chargeStaff.getSelectedIndex() == 0) {
            chargeStaffNo.setText("");
        }

        if (null != ms && ms.getStaffID() != null) {
            chargeStaffNo.setText(ms.getStaffNo());
        }

        if(!this.isShowing()) {
            return;
        } 
    }//GEN-LAST:event_chargeStaffActionPerformed

    private void recessStartTimeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_recessStartTimeItemStateChanged
        //refreshRecessTime(recessStartTime, recessStartTimeField);
    }//GEN-LAST:event_recessStartTimeItemStateChanged

    private void recessEndTimeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_recessEndTimeItemStateChanged
        //refreshRecessTime(recessEndTime, recessEndTimeField);
    }//GEN-LAST:event_recessEndTimeItemStateChanged

    private void recessEndTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recessEndTimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_recessEndTimeActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        deleteButton.setCursor(null);
        deleteDataRecess();
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        updateButton.setCursor(null);
        isUpdate = true;
        registDataRecess();
    }//GEN-LAST:event_updateButtonActionPerformed

    private void btnNewRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewRowActionPerformed
        btnNewRow.setCursor(null);
        isUpdate = false;
        registDataRecess();
    }//GEN-LAST:event_btnNewRowActionPerformed

    private void recessStartTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recessStartTimeActionPerformed

    }//GEN-LAST:event_recessStartTimeActionPerformed

    private void recessTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recessTableMouseClicked
        
    }//GEN-LAST:event_recessTableMouseClicked

    private void recessTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_recessTableKeyReleased

    }//GEN-LAST:event_recessTableKeyReleased

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        if(this.isDialog()) {
            ((JDialog)this.getParent().getParent().getParent().getParent()).setVisible(false);
        } else {
            this.setVisible(false);
        }
    }//GEN-LAST:event_btnCloseActionPerformed

    private void chargeStaffNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chargeStaffNoActionPerformed
        if (!chargeStaffNo.getText().equals("")) {

            chargeStaffNo.setName("1");
            this.setStaff(chargeStaff, chargeStaffNo.getText());
            chargeStaffNo.setName(null);

        } else {

            chargeStaff.setSelectedIndex(0);
        }
    }//GEN-LAST:event_chargeStaffNoActionPerformed

    private void chargeStaffNoFocusLost(java.awt.event.FocusEvent evt) {                                        

        if (!chargeStaffNo.getText().equals("")) {

            chargeStaffNo.setName("1");
            this.setStaff(chargeStaff, chargeStaffNo.getText());
            chargeStaffNo.setName(null);

        } else {

            chargeStaff.setSelectedIndex(0);
        }
    } 
    
    private void setStaff(JComboBox staffCombo, String staffNo) {

	    for (int i = 0; i < staffCombo.getItemCount(); i++) {

		 MstStaff ms  = (MstStaff)staffCombo.getItemAt(i);

		if (ms.getStaffID() == null) {

		    staffCombo.setSelectedIndex(0);

		} else if ( ms.getStaffNo().equals(staffNo)) {

		    staffCombo.setSelectedIndex(i);
		    return;
		}
	    }
    }
    
    private void registDataRecess() {
        try{
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (!this.checkInput()) {
                return;
            }
            if(!isUpdate){
                recessSelect = null;
            }else {
                int selectedRow = recessTable.getSelectedRow();
                if(selectedRow < 0) {
                    return;
                }
                recessSelect = (DataRecess)recessTable.getValueAt(selectedRow, 0);
            }
            if (registData()) {

                //IVS_LVTu start edit 2016/11/23 New request #58702
                //if(SystemInfo.getUserAPI() != null && SystemInfo.getUserAPI() != 0) {
                Integer shopId = shop.getShopID();
                if(SystemInfo.getUserByUserApi() != null && SystemInfo.getUserByUserApi() != 0) {
                //IVS_LVTu end edit 2016/11/23 New request #58702    
                    if(!sendRegistRecessAPI(SystemInfo.getLoginID(), ((MstStaff)this.chargeStaff.getSelectedItem()).getStaffID(), 
                            scheduleDate, recessId, null, shopId)){
                        MessageDialog.showMessageDialog(this,
                            "î}ëÃÇ∆ÇÃòAìÆÇ™Ç≈Ç´Ç‹ÇπÇÒÇ≈ÇµÇΩÅB",
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
                
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS),
                        this.getTitle(),
                        JOptionPane.INFORMATION_MESSAGE);
                
                //if(null == recessSelect) //add new recess(not update)
                loadRecessTable();
                //else {
                //    updateRowTable();
                //}
                resetData();
            } else {
                MessageDialog.showMessageDialog(this,
                        MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "ãxåeéûä‘"),
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }
       }catch(Exception e){
           e.printStackTrace();
       }finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    private void deleteDataRecess() {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (MessageDialog.showYesNoDialog(
                this,
                MessageUtil.getMessage(MessageUtil.CONFIRM_DELETE, "ãxåe"),
                this.getTitle(),
                JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
                return;
            }
            Integer allianceRecessId = null;
            //IVS_NHTVINH start update 2016/10/19 New request #55977
            //IVS_LVTu start edit 2016/11/23 New request #58702
            //if(SystemInfo.getUserAPI() != null && SystemInfo.getUserAPI() != 0) {
            if(SystemInfo.getUserByUserApi() != null && SystemInfo.getUserByUserApi() != 0) {
            //IVS_LVTu end edit 2016/11/23 New request #58702    
                if(null != recessSelect){
                    allianceRecessId = recessSelect.getAllianceRecessId();
                    if(null == allianceRecessId){
                        ConnectionWrapper con = SystemInfo.getConnection();
                        con.begin();
                        try{
                            allianceRecessId = recessSelect.getAllianceRecessIdFromDb(con);
                        }catch(SQLException ex) {
                            ex.printStackTrace();
                            con.rollback();
                            return;
                        }
                        con.commit();
                    }
                }
            }
            //IVS_NHTVINH start update 2016/10/19 New request #55977
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" delete from data_recess");
            sql.append(" where");
            sql.append("         staff_id = " + SQLUtil.convertForSQL(this.recessSelect.getStaffId()));
            sql.append("     and schedule_date = " + SQLUtil.convertForSQLDateOnly(scheduleDate));
            sql.append("     and recess_id = " + SQLUtil.convertForSQL(recessId));

            try {
                SystemInfo.getConnection().executeUpdate(sql.toString());
            } catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            //IVS_LVTu start edit 2016/11/23 New request #58702
            //if(SystemInfo.getUserAPI() != null && SystemInfo.getUserAPI() != 0) {
            if(SystemInfo.getUserByUserApi() != null && SystemInfo.getUserByUserApi() != 0) {
            //IVS_LVTu end edit 2016/11/23 New request #58702    
                Integer shopId = shop.getShopID();
                if(!sendRegistRecessAPI(SystemInfo.getLoginID(), this.recessSelect.getStaffId(), 
                        scheduleDate, recessId, allianceRecessId, shopId)){
                    MessageDialog.showMessageDialog(this,
                        "î}ëÃÇ∆ÇÃòAìÆÇ™Ç≈Ç´Ç‹ÇπÇÒÇ≈ÇµÇΩÅB",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                }
                else {
                    MessageDialog.showMessageDialog(this,
                        "çÌèúÇµÇ‹ÇµÇΩ",
                        this.getTitle(),
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
            //deleteRowTable();
            loadRecessTable();
            resetData();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    private boolean checkInput() {

            if (chargeStaff.getSelectedIndex() < 1) {
                    MessageDialog.showMessageDialog(
                            this,
                            MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
                            "íSìñé“"), this.getTitle(), JOptionPane.ERROR_MESSAGE);
                    chargeStaffNo.requestFocusInWindow();
                    return false;
            }

            if(recessStartTime.getSelectedItem().toString().length() == 0) {
                MessageDialog.showMessageDialog(
                        this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
                        "ãxåeéûä‘"), this.getTitle(), JOptionPane.ERROR_MESSAGE);
                recessStartTime.requestFocusInWindow();
                return	false;
            }

            if(recessEndTime.getSelectedItem().toString().length() == 0) {
                MessageDialog.showMessageDialog(
                        this,
                        MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY,
                        "ãxåeéûä‘"), this.getTitle(), JOptionPane.ERROR_MESSAGE);
                recessEndTime.requestFocusInWindow();
                return	false;
            }
            
            if ( checkTime()) {
                MessageDialog.showMessageDialog(
                        this,
                        "ãxåeéûä‘ÇÃê›íËÇ™ê≥ÇµÇ≠Ç†ÇËÇ‹ÇπÇÒÅB", this.getTitle(), JOptionPane.ERROR_MESSAGE);
                recessEndTime.requestFocusInWindow();
                return	false;
            }
            
            if(isRegistTimeOverlap()){
                MessageDialog.showMessageDialog(this,
                            "ì¸óÕÇ≥ÇÍÇΩéûä‘ë—Ç…ï ÇÃãxåeÇ™ìoò^Ç≥ÇÍÇƒÇ¢Ç‹Ç∑ÅB\n èdï°ÇµÇ»Ç¢éûä‘Çì¸óÕÇµÇƒÇ≠ÇæÇ≥Ç¢ÅB",
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            //nhtvinh start add 20161017 New request #54380
            //IVS_LVTu start edit 2016/11/23 New request #58702
            //if(SystemInfo.getUserAPI() != null && SystemInfo.getUserAPI() != 0){
            if(SystemInfo.getUserByUserApi() != null && SystemInfo.getUserByUserApi() != 0) {
            //IVS_LVTu end edit 2016/11/23 New request #58702    
                if(isRegisterOutsiteBusinessTime()){
                    return false;
                }
                
                //IVS_NHTVINH start add 2016/10/26 New request #57853 
                //[gb]Ç©ÇÒÇ¥ÇµAPIóp_ã@î\í«â¡ÅiÉXÉ^ÉbÉtÉVÉtÉgãxåeìoò^âÊñ Åj_ÉwÉãÉvÇ∆ãxåeÇÃèdï°ïsâ¬ëŒâû
                if(isOverlapTimeHelp()){
                    MessageDialog.showMessageDialog(this,
                            "ÉwÉãÉvéûä‘Ç∆ãxåeéûä‘Çèdï°ÇµÇƒ\nìoò^Ç∑ÇÈÇ±Ç∆ÇÕÇ≈Ç´Ç‹ÇπÇÒÅB",
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                    }
                //IVS_NHTVINH end add 2016/10/26 New request #57853      
                //IVS_LVTu start add  2016/11/23 New request #58700
                boolean isRight;
                DataSchedule scheduleStaff = new DataSchedule();
                ConnectionWrapper con = SystemInfo.getConnection();
                
                //ÉXÉ^ÉbÉtID
                int staffId = -1;
                if (this.chargeStaff.getSelectedIndex() > 0) {
                    staffId = ((MstStaff)this.chargeStaff.getSelectedItem()).getStaffID();
                }
                scheduleStaff.setStaffId(staffId);
                //ì˙ït
                scheduleStaff.setScheduleDate(scheduleDate);
                String startTime = (String)recessStartTime.getSelectedItem();
                String endStart = (String)recessEndTime.getSelectedItem();
                //äJénéûä‘
                Integer timeStartRegist = Integer.parseInt(startTime.replaceAll(":", "").trim());
                //èIóπéûä‘
                Integer timeEndRegist = Integer.parseInt(endStart.replaceAll(":", "").trim());

                try {
                    isRight = scheduleStaff.loadByID(con);
                    if(isRight) {
                        // start_time
                        Integer startSchedule   = Integer.parseInt(scheduleStaff.getStartTime().trim().equals("") ? "0" : scheduleStaff.getStartTime());
                        // end_time
                        Integer endSchedule     = Integer.parseInt(scheduleStaff.getEndTime().trim().equals("") ? "0" : scheduleStaff.getEndTime().trim());
                        // check time.
                        if((timeStartRegist < startSchedule)||(timeEndRegist > endSchedule)){
                            isRight = false;
                        }
                    }
                    if(!isRight) {
                        MessageDialog.showMessageDialog(this,
                            "ãxåeéûä‘Ç™ÉVÉtÉgéûä‘äOÇ≈Ç∑",
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                } catch(SQLException e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                }catch(NumberFormatException e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                }
                //IVS_LVTu end add  2016/11/23 New request #58700
            }
            //nhtvinh end add 20161017 New request #54380
            
            return true;
	}
    
    //nhtvinh start add 20161017 New request #55977
        /**
         * check start time register and end time register outside business time 
        */
        private Boolean isRegisterOutsiteBusinessTime() {
            try{
                int staffId = -1;
                if (this.chargeStaff.getSelectedIndex() > 0) {
                        staffId = ((MstStaff)this.chargeStaff.getSelectedItem()).getStaffID();

                }
                Integer shopId = shop.getShopID();
                Integer startTimeShift = shop.getOpenHour()*100 + shop.getOpenMinute();
                Integer endTimeShift = shop.getCloseHour()*100 + shop.getCloseMinute();

                String startTime = (String)recessStartTime.getSelectedItem();
                String endStart = (String)recessEndTime.getSelectedItem();
                if(null != startTime){
                    startTime = startTime.replaceAll(":", "").trim();
                    Integer timeStartRegist = Integer.parseInt(startTime);
                    if(timeStartRegist < startTimeShift || timeStartRegist > endTimeShift){
                        MessageDialog.showMessageDialog(this,
                        "ãxåeéûä‘Ç™âcã∆éûä‘äOÇ≈Ç∑",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                        return true;
                    }
                }
                if(null != endStart){
                    endStart = endStart.replaceAll(":", "").trim();
                    Integer timeEndRegist = Integer.parseInt(endStart);
                    if(timeEndRegist > endTimeShift){
                        MessageDialog.showMessageDialog(this,
                        "ãxåeéûä‘Ç™âcã∆éûä‘äOÇ≈Ç∑",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                        return true;
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return false;
        }   
        //nhtvinh end add 20161017 New request #54380
        
    private boolean  checkTime() {
        boolean flag = false;
        String strStartTime = "";
        String strEndTime = "";
        Date startTime;
        Date endTime;
        
        strStartTime = recessStartTime.getSelectedItem().toString().trim();
        strEndTime = recessEndTime.getSelectedItem().toString().trim();

        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");

        try {
            if ( strStartTime.equals("") || strEndTime.equals("")) {
                return false;
            }
            startTime = parser.parse(strStartTime);
            endTime = parser.parse(strEndTime);

            if (startTime.after(endTime) || startTime.equals(endTime)) {
                flag = true;
            }
        } catch (ParseException ex) {
            Logger.getLogger(RegistPersonalRecessPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        return flag;
    }
    
    private Boolean isRegistTimeOverlap() {
        try {
            DataRecess dataress = new DataRecess();
            int staffID = -1;
            if (this.chargeStaff.getSelectedIndex() > 0) {
                    staffID = ((MstStaff)this.chargeStaff.getSelectedItem()).getStaffID();

            }
            dataress.setStaffId(staffID);
            dataress.setStartTime(recessStartTime.getSelectedItem().toString().replace(":", ""));

            dataress.setEndTime(recessEndTime.getSelectedItem().toString().replace(":", ""));
            dataress.setScheduleDate(scheduleDate);
            if (recessId > 0 && isUpdate) { //check if it's update case
                dataress.setRecessId(recessId);
            } else {
                dataress.setRecessId(-1);
            }
            
            ConnectionWrapper con = SystemInfo.getConnection();
            con.begin();
            //IVS_LVTu start edit 2016/11/23 New request #58702
            //if(SystemInfo.getUserAPI() != null && SystemInfo.getUserAPI() != 0) {
            if(SystemInfo.getUserByUserApi() != null && SystemInfo.getUserByUserApi() != 0) {
            //IVS_LVTu end edit 2016/11/23 New request #58702    
                if(dataress.checkTimeRegistOverlap(con))
                    return true;
            }
            con.commit();
            
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    
    //IVS_NHTVINH start add 2016/10/26 New request #57853 
    //[gb]Ç©ÇÒÇ¥ÇµAPIóp_ã@î\í«â¡ÅiÉXÉ^ÉbÉtÉVÉtÉgãxåeìoò^âÊñ Åj_ÉwÉãÉvÇ∆ãxåeÇÃèdï°ïsâ¬ëŒâû
    private Boolean isOverlapTimeHelp(){
        try{
            DataScheduleDetail dataScheduleDetail = new DataScheduleDetail();
            int staffID = -1;
            if (this.chargeStaff.getSelectedIndex() > 0) {
                    staffID = ((MstStaff)this.chargeStaff.getSelectedItem()).getStaffID();
            }
            dataScheduleDetail.setStaffID(staffID);
            dataScheduleDetail.setScheduleDate(scheduleDate);
            
            String timeStart = recessStartTime.getSelectedItem().toString().trim();
            String timeEnd = recessEndTime.getSelectedItem().toString().trim();
            String scheduleString = dateFormatDay.format(scheduleDate);
            timeStart = scheduleString + " " + timeStart;
            timeEnd = scheduleString + " " + timeEnd;
            dataScheduleDetail.setExtStartTime(dateFormatFull.parse(timeStart));
            dataScheduleDetail.setExtEndTime(dateFormatFull.parse(timeEnd));
            
            ConnectionWrapper con = SystemInfo.getConnection();
            con.begin();
            //IVS_LVTu start edit 2016/11/23 New request #58702
            //if(SystemInfo.getUserAPI() != null && SystemInfo.getUserAPI() != 0) {
            if(SystemInfo.getUserByUserApi() != null && SystemInfo.getUserByUserApi() != 0) {
            //IVS_LVTu end edit 2016/11/23 New request #58702    
                if(dataScheduleDetail.isTimeRegistOverlap(con))
                    return true;
            }
            con.commit();
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
    //IVS_NHTVINH end add 2016/10/26 New request #57853 
    
    private void addNewRowTable() {
        try {
            String staffName = chargeStaff.getSelectedItem().toString();
            String shiftTime = recessStartTime.getSelectedItem().toString() 
                                + "Å`" + recessEndTime.getSelectedItem().toString();
            
            //add new data recess to table
            DefaultTableModel model = (DefaultTableModel)recessTable.getModel();
            Object[] rowData = {staffName, shiftTime.toString(), recessNote.getText()};  
            model.addRow(rowData);
            
            //add new data recess to list
            DataRecess dataress = new DataRecess();
            dataress.setRecessId(recessId);
            int staffID = -1;
            if (this.chargeStaff.getSelectedIndex() > 0) {
                    staffID = ((MstStaff)this.chargeStaff.getSelectedItem()).getStaffID();

            }
            dataress.setStaffId(staffID);

            dataress.setStartTime(recessStartTime.getSelectedItem().toString().replace(":", ""));
            dataress.setEndTime(recessEndTime.getSelectedItem().toString().replace(":", ""));

            dataress.setScheduleDate(scheduleDate);

            dataress.setNote(recessNote.getText());
            if(null != allianceRecessId)
                dataress.setAllianceRecessId(allianceRecessId);
            listDataRecess.add(dataress);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * post api and response data.
     * @param login_id
     * @param staff_id
     * @param schedule_date
     * @param recess_id
     * @param alliance_recess_id
     * @param shopId
     * @return 
     */
    private Boolean sendRegistRecessAPI(String login_id, int staff_id, Date schedule_date, 
                                        int recess_id, Integer alliance_recess_id, Integer shopId) {
        try{
            //String url = "http://10.32.5.21/web/s/send/recess.php";
            mstApi = new MstAPI(0);
            String apiUrl = mstApi.getApiUrl();
            String url = apiUrl + "/s/send/recess.php";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
            con.setRequestProperty( "charset", "utf-8");

            Format formatter = new SimpleDateFormat("yyyy/MM/dd");
            String dateString = formatter.format(schedule_date);
            //set parameter
            Map mapParam = new LinkedMap();
            mapParam.put("login_id", login_id);
            mapParam.put("staff_id", staff_id);
            mapParam.put("schedule_date", dateString);
            mapParam.put("recess_id", recess_id);
            mapParam.put("alliance_recess_id", alliance_recess_id);
            mapParam.put("shop_id", shopId);
            Gson gson = new Gson(); 
            String jsonParam = gson.toJson(mapParam); 
            String urlParameters = "param=" + jsonParam;

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            //get response
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
            }
            in.close();
            
            try{
                String responseOb = response.toString().substring(6, response.length());
                JsonParser parser = new JsonParser();
                JsonObject objectResponse = parser.parse(responseOb).getAsJsonObject();
                if(null != objectResponse){
                    JsonObject result = (JsonObject)objectResponse.get("results");
                    if(null != result){
                        JsonElement allianceId = result.get("alliance_reserve_id");
                        allianceRecessId = allianceId.getAsInt();
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            
            if(response.toString().contains("\"code\":200")){
                return true;
            }

        }catch(Exception e){
            return false;
        }
        return false;
    }
    
    private boolean registData() {
        boolean isDel = false;
        DataRecess dataress = new DataRecess();
        int staffID = -1;
        if (this.chargeStaff.getSelectedIndex() > 0) {
                staffID = ((MstStaff)this.chargeStaff.getSelectedItem()).getStaffID();

        }
        dataress.setStaffId(staffID);

        dataress.setStartTime(recessStartTime.
                getSelectedItem().toString().trim().replace(":", ""));
        dataress.setEndTime(recessEndTime.getSelectedItem().toString().trim().replace(":", ""));
        
        dataress.setScheduleDate(scheduleDate);

        dataress.setNote(recessNote.getText().trim());
        SystemInfo.getLogger().log(Level.INFO, "äJénéûä‘" + dataress.getStartTime());
        SystemInfo.getLogger().log(Level.INFO, "èIóπéûä‘" + dataress.getEndTime() );

        // ãxåeÉfÅ[É^Çìoò^Ç∑ÇÈ
        ConnectionWrapper con = SystemInfo.getConnection();
        try {
            con.begin();
            
            if (isUpdate) {
                //IVS_LVTu start add 2016/11/01 New request #55963
                //update data_recess delete_date
                if(this.recessSelect != null && this.recessSelect.getStaffId() != staffID) {
                    allianceRecessId = null;
                    //IVS_LVTu start edit 2016/11/23 New request #58702
                    //if(SystemInfo.getUserAPI() != null && SystemInfo.getUserAPI() != 0) {
                    if(SystemInfo.getUserByUserApi() != null && SystemInfo.getUserByUserApi() != 0) {
                    //IVS_LVTu end edit 2016/11/23 New request #58702    
                        if(null != recessSelect){
                            allianceRecessId = recessSelect.getAllianceRecessId();
                            if(null == allianceRecessId){
                                try{
                                    allianceRecessId = recessSelect.getAllianceRecessIdFromDb(con);
                                }catch(SQLException ex) {
                                    ex.printStackTrace();
                                    con.rollback();
                                }
                            }
                        }
                    }
                    this.recessSelect.deleteDataRecess(con);
                    isDel = true;
                    
                     //1ÇrecessIdÇ…ÉZÉbÉgÇ∑ÇÈÅB
                    int recessId = 1;
                    dataress.setRecessId(recessId);

                    //ñ≥Ç≠Ç»ÇÈÇ‹Ç≈Ç∏Ç¡Ç∆åüçı
                    while (dataress.isExists(con)) {
                        recessId ++;
                        dataress.setRecessId(recessId);
                    }
                }else {
                    dataress.setRecessId(recessId);
                }
                //IVS_LVTu end add 2016/11/01 New request #55963
            } else {

                //1ÇrecessIdÇ…ÉZÉbÉgÇ∑ÇÈÅB
                int recessId = 1;
                dataress.setRecessId(recessId);

                //ñ≥Ç≠Ç»ÇÈÇ‹Ç≈Ç∏Ç¡Ç∆åüçı
                while (dataress.isExists(con)) {
                    recessId ++;
                    dataress.setRecessId(recessId);
                }
            }
            recessId = dataress.getRecessId();
            dataress.regist(con);
            con.commit();
            //IVS_LVTu start add 2016/11/01 New request #55963
            //IVS_LVTu start edit 2016/11/23 New request #58702
            //if(SystemInfo.getUserAPI() != null && SystemInfo.getUserAPI() != 0 && isDel && allianceRecessId != null && allianceRecessId != 0) {
            if(SystemInfo.getUserByUserApi() != null && SystemInfo.getUserByUserApi() != 0
                    && isDel && allianceRecessId != null && allianceRecessId != 0) {
            //IVS_LVTu end edit 2016/11/23 New request #58702    
                Integer shopId = shop.getShopID();
                sendRegistRecessAPI(SystemInfo.getLoginID(), this.recessSelect.getStaffId(), 
                        scheduleDate, this.recessSelect.getRecessId(), allianceRecessId, shopId);
            }
            //IVS_LVTu end add 2016/11/01 New request #55963
            
        } catch(Exception e) {
            try {
                con.rollback();
            } catch(SQLException ex) {
                SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }

        return true;
    }
    
    public void loadRowDataUpdate(){
        try{
            int rowIndex = recessTable.getSelectedRow();
            recessSelect = listDataRecess.get(rowIndex);
            if(null != recessSelect){
                recessId = recessSelect.getRecessId();
                chargeStaffNo.setText(recessSelect.getStaffId().toString());
                recessNote.setText(recessSelect.getNote());
                StringBuilder startTime = new StringBuilder();
                StringBuilder endTime = new StringBuilder();
                
                startTime.append(recessSelect.getStartTime().substring(0, 2));
                startTime.append(":");
                startTime.append(recessSelect.getStartTime().substring(2, 4));
                
                endTime.append(recessSelect.getEndTime().substring(0, 2));
                endTime.append(":");
                endTime.append(recessSelect.getEndTime().substring(2, 4));
                
                int sizeListTime = recessStartTime.getModel().getSize();
                for(int i=0; i<sizeListTime; i++ ){
                    Object timeCompare = recessStartTime.getModel().getElementAt(i);
                    if(timeCompare.toString().equals(startTime.toString())){
                        recessStartTime.setSelectedIndex(i);
                    }
                    if(timeCompare.toString().equals(endTime.toString())){
                        recessEndTime.setSelectedIndex(i);
                    }
                }
                
                int sizeListStaff = chargeStaff.getModel().getSize();
                for(int j=0; j<sizeListStaff; j++){
                    MstStaff staffCompare = (MstStaff)chargeStaff.getModel().getElementAt(j);
                    if(null != staffCompare){
                        if(recessSelect.getStaffId().equals(staffCompare.getStaffID())){
                            chargeStaff.setSelectedIndex(j);
                            break;
                        }
                    }
                }
            }
            updateButton.setEnabled(true);
            deleteButton.setEnabled(true);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void resetData(){
        chargeStaffNo.setText("");
        chargeStaff.setSelectedIndex(0);
        recessStartTime.setSelectedIndex(-1);
        recessEndTime.setSelectedIndex(-1);
        recessNote.setText("");
        recessId = 0;
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        recessSelect = null;
        isUpdate = false;
        allianceRecessId = null;
        recessTable.clearSelection();
    }                       

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane bedsScrollPane;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnNewRow;
    private javax.swing.JLabel cblShop;
    private javax.swing.JComboBox chargeStaff;
    private javax.swing.JTextField chargeStaffNo;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel lbShiftTimeShop;
    private javax.swing.JLabel memoLabel1;
    private javax.swing.JScrollPane noteScrollPane1;
    private javax.swing.JComboBox recessEndTime;
    private com.geobeck.swing.JTextAreaEx recessNote;
    private javax.swing.JComboBox recessStartTime;
    private javax.swing.JLabel recessStartTimeLabel;
    private javax.swing.JLabel recessStartTimeLabel1;
    private javax.swing.JTable recessTable;
    private javax.swing.JLabel staffLabel;
    private javax.swing.JLabel targetDate;
    private javax.swing.JButton updateButton;
    // End of variables declaration//GEN-END:variables
}
