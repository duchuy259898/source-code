/*
 * RegistRecessPanel.java
 *
 * Created on 2009/05/01, 10:44
 */

package com.geobeck.sosia.pos.basicinfo.company;

import com.geobeck.sosia.pos.master.company.MstShift;
import com.geobeck.sosia.pos.master.company.MstShifts;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.swing.MessageDialog;
import com.geobeck.swing.SwingUtil;
import java.awt.Frame;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author  takeda
 */
//public class RegistRecessPanel extends javax.swing.JPanel {
public class RegistRecessPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {
    
    private String[] columnNames = {"ÉXÉ^ÉbÉtñº", "ãxåeéûä‘ÇP", "ãxåeéûä‘ÇQ", "ãxåeéûä‘ÇR", "ãxåeéûä‘ÇS", "ãxåeéûä‘ÇT", "ãxåeéûä‘ÇU", "ãxåeéûä‘ÇV", "ãxåeéûä‘ÇW", "ãxåeéûä‘ÇX", "ãxåeéûä‘ÇPÇO"};
    
    private MstShop   shop;
    private MstShifts shifts;
    private Date      scheduleDate;
    private DataSchedules scheds;
    
    
    /*
     * ãxåeéûä‘ìoò^âÊñ ÇÉ_ÉCÉAÉçÉOÇ∆ÇµÇƒÅAï\é¶Ç∑ÇÈ
     */
    static public boolean ShowDialog(Frame owner, MstShop shop, Date targetDate ) {

        SystemInfo.getLogger().log(Level.INFO, "ãxåeéûä‘ìoò^");
        
        RegistRecessPanel dlg = new RegistRecessPanel();
        dlg.ShowCloseBtn();
        dlg.shop = shop;
        dlg.scheduleDate = targetDate;
        dlg.loadData();
        
        SwingUtil.openDialog(owner, true, dlg, "ãxåeéûä‘ìoò^");
        
        dlg.dispose();
        return true;
    }    
    
    /*
     * É_ÉCÉAÉçÉOÇ∆ÇµÇƒï\é¶Ç≥ÇÍÇΩèÍçáÇÕÅAï¬Ç∂ÇÈÉ{É^ÉìÇóLå¯âª
     */
    private void ShowCloseBtn() {
        btnClose.setVisible(true);
    }
    
    /*
     * å„énññ
     */
    private void dispose() {
        if(this.isDialog()) {
            ((JDialog)this.getParent().getParent().getParent().getParent()).dispose();
        }
    }
    
    /** Creates new form RegistRecessPanel */
    private RegistRecessPanel() {
        initComponents();
        
        this.setTitle("ãxåeéûä‘ìoò^");
        
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.CENTER);
        recessTable.getColumnModel().getColumn(0).setCellRenderer(r);
        
        int nBasePanelWidth = new ShiftPanelBase().getPreferredSize().width;
        
        recessTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        for (int i = 1; i <= 10; i++) {
            recessTable.getColumnModel().getColumn(i).setPreferredWidth(nBasePanelWidth);
        }

        // ÉpÉlÉãÇÃïùÇÉeÅ[ÉuÉãïù + 50 Ç…Ç∑ÇÈ
        this.setSize(80 + nBasePanelWidth * 3 + 50,600);
        SwingUtil.setJTableHeaderRenderer(recessTable, SystemInfo.getTableHeaderRenderer());
        recessTable.getTableHeader().setReorderingAllowed(false);
        recessTable.getTableHeader().setResizingAllowed(false);
        recessTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        
        SystemInfo.addMouseCursorChange(btnRegist);
        SystemInfo.addMouseCursorChange(btnClose);
    }

    /*
     * ì˙ïtÅAìXï‹ÇÊÇËÅAÉXÉPÉWÉÖÅ[ÉãÅïãxåeÉfÅ[É^Çì«Ç›çûÇÒÇ≈ï\é¶Ç∑ÇÈ
     */
    private boolean loadData() {
        // ëŒè€ì˙ïtÇï\é¶
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy'îN'MM'åé'dd'ì˙'");
        targetDate.setText(sdf.format(scheduleDate));
        
        scheds = new DataSchedules();
        shifts = new MstShifts();
        
        // ÉXÉPÉWÉÖÅ[ÉãÇÃåüçıÉLÅ[Çìoò^
        scheds.setScheduleDate(scheduleDate);
        scheds.setShop(shop);
        scheds.setOpener(this);
        
        // ìXï‹ÉVÉtÉgÇÃåüçı
        shifts.setShopId(shop.getShopID());
        
        // ÉXÉPÉWÉÖÅ[ÉãÉfÅ[É^Çì«Ç›çûÇﬁÅAÇ†ÇÌÇπÇƒÉVÉtÉgÉ}ÉXÉ^Çì«Ç›çûÇÒÇ≈Ç®Ç≠ÅiÇPìxÇæÇØÅj
        try{
            scheds.load(SystemInfo.getConnection(), false);
            shifts.load(SystemInfo.getConnection(), false);
        }catch(SQLException e){
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
        
        
        DefaultTableModel model = (DefaultTableModel)recessTable.getModel();
        
        
        
        // ÉXÉPÉWÉÖÅ[ÉãÇÃì¸Ç¡ÇƒÇ¢Ç»Ç¢ÉXÉ^ÉbÉtÇÕÅAï\é¶ëŒè€Ç©ÇÁÇÕÇ∏Ç∑ÅB
        DataSchedules removeSched = new DataSchedules();
        for( DataSchedule sched : scheds ){
            if( sched.getShiftId() == 0 ){
                removeSched.add(sched);
            }
        }
        for( DataSchedule sched : removeSched ){
            scheds.remove(sched);
        }
        
        // ëSÇƒÇÃÉRÉìÉ|Å[ÉlÉìÉgÇçÌèúÇµÇƒÇ®Ç≠
        this.recessTable.removeAll();
        
        // éÊìæÇµÇΩÉfÅ[É^ÇâÊñ Ç…ï\é¶Ç∑ÇÈ
        int rowCount = scheds.size();
        for( int row = 0; row < rowCount; row++ ){
            DataSchedule sched = scheds.get(row);
            
            ShiftPanelBase[] timePanel  = new ShiftPanelBase[10];
            for (int i = 0; i < 10; i++) {
                timePanel[i] = new ShiftPanelBase();
            }
            JTextArea staffText  = new JTextArea();
            JLabel    staffLabel = new JLabel();
            
            
            DataRecesses recesses = sched.getRecesses();
            for( int recessId = 1; recessId <= 10; recessId++ ){
                DataRecess recess = recesses.getRecess(recessId);
                
                if( recess == null ){
                    recess = new DataRecess();
                    recess.setStaffId(sched.getStaffId());
                    recess.setRecessId(recessId);
                    recess.setScheduleDate(this.scheduleDate);
                    recesses.add( recess );
                }
                timePanel[recessId-1].setFromTime(recess.getStartTime());
                timePanel[recessId-1].setToTime(recess.getEndTime());
            }
            
            MstStaff staff = new MstStaff();
            staff.setStaffID(sched.getStaffId());
            try{
                staff.load(SystemInfo.getConnection());
            }catch(SQLException e){
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                return false;
            }
            
            String strStaff;
            MstShift shift = shifts.getShift(sched.getShiftId());
            if( shift != null ){
                StringBuilder sb = new StringBuilder();
                sb.append("<html>");
                sb.append(staff.getFullStaffName());
                sb.append("<br />");
                //sb.append("\n\r");
                sb.append(shift.getStartTime().substring(0, 2));
                sb.append(":");
                sb.append(shift.getStartTime().substring(2, 4));
                sb.append("Å`" );
                sb.append(shift.getEndTime().substring(0, 2));
                sb.append(":");
                sb.append(shift.getEndTime().substring(2, 4));
                sb.append("</html>");
                strStaff = sb.toString();
            }else{
                strStaff = "<html>" + staff.getFullStaffName() + "</html>";
            }
            staffText.setText(strStaff);
            staffText.setFont(targetDate.getFont());
            //staffText.setHorizontalAlignment(SwingConstants.CENTER);
            staffLabel.setText(strStaff);
            staffLabel.setHorizontalAlignment(SwingConstants.CENTER);

            Object[] rowData = { staffLabel /*strStaff*/,
                                 timePanel[0],
                                 timePanel[1],
                                 timePanel[2],
                                 timePanel[3],
                                 timePanel[4],
                                 timePanel[5],
                                 timePanel[6],
                                 timePanel[7],
                                 timePanel[8],
                                 timePanel[9]
                               };
            model.addRow(rowData);

            // ÉåÉìÉ_ÉâÅ[ÇÃà◊Ç…ÉRÉìÉ|Å[ÉlÉìÉgÇ…Ç‡í«â¡
            for (int i = 0; i < 10; i++) {
                this.recessTable.add(timePanel[i]);
            }
        }
        
        return true;
    }
   
    /*
     * âÊñ ÇÃÉfÅ[É^Çì«Ç›çûÇÒÇ≈ÅAãxåeÉfÅ[É^Çìoò^Ç∑ÇÈ
     */
    private boolean registData()
    {
        DefaultTableModel model = (DefaultTableModel)recessTable.getModel();
        int rowCount = model.getRowCount();
        
        // ì¸óÕÉfÅ[É^Çì‡ïîÉfÅ[É^Ç…îΩâfÇ∑ÇÈ
        for( int row = 0; row < rowCount; row++ ){
            DataSchedule sched = scheds.get(row);
            DataRecesses recesses = sched.getRecesses();
            for( int recessId = 1; recessId <= 10; recessId++ ){
                DataRecess recess = recesses.getRecess(recessId);
                ShiftPanelBase timePanel  = (ShiftPanelBase)model.getValueAt(row, recessId);
                
                if( recess != null ){ /* çsçÏê¨éûÇ…çÏÇ¡ÇƒÇ¢ÇÈÇÃÇ≈Ç±Ç±Ç≈NullÇ…Ç»ÇÈÇ±Ç∆ÇÕñ≥Ç¢ÇÕÇ∏ÅIÅI */
                    recess.setStartTime(timePanel.getFromTime());
                    recess.setEndTime(timePanel.getToTime());
                }
            }
        }
        
        // ãxåeÉfÅ[É^Çìoò^Ç∑ÇÈ
        ConnectionWrapper con = SystemInfo.getConnection();
        try{
            con.begin();
            scheds.registRecesses(con);
            con.commit();
        }catch(Exception e){
            try{
                con.rollback();
            }catch(SQLException ex){
                SystemInfo.getLogger().log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
        
        return true;
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        recessScrollPane = new javax.swing.JScrollPane();
        recessTable = new com.geobeck.swing.JTableEx();
        jLabel1 = new javax.swing.JLabel();
        targetDate = new javax.swing.JLabel();
        btnClose = new javax.swing.JButton();
        btnClose.setVisible(false);
        btnClose.setContentAreaFilled(false);
        btnRegist = new javax.swing.JButton();

        recessScrollPane.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        recessTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ÉXÉ^ÉbÉtñº", "ãxåeéûä‘ÇP", "ãxåeéûä‘ÇQ", "ãxåeéûä‘ÇR", "ãxåeéûä‘ÇS", "ãxåeéûä‘ÇT", "ãxåeéûä‘ÇU", "ãxåeéûä‘ÇV", "ãxåeéûä‘ÇW", "ãxåeéûä‘ÇX", "ãxåeéûä‘ÇPÇO"
            }
        ));
        recessTable.setAutoscrolls(false);
        recessScrollPane.setViewportView(recessTable);

        jLabel1.setText("ëŒè€ì˙");

        targetDate.setText("yyyyîNmmåéddì˙");

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

        btnRegist.setBackground(new java.awt.Color(255, 255, 255));
        btnRegist.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
        btnRegist.setBorderPainted(false);
        btnRegist.setContentAreaFilled(false);
        btnRegist.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_on.jpg"));
        btnRegist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(recessScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 968, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(targetDate, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(btnRegist, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnRegist, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(targetDate, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(23, 23, 23)
                .addComponent(recessScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistActionPerformed
        
        if (registData())
        {
            MessageDialog.showMessageDialog(this,
                MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS),
                this.getTitle(),
                JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            MessageDialog.showMessageDialog(this,
                MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED, "ãxåeéûä‘"),
                this.getTitle(),
                JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnRegistActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        if(this.isDialog()) {
            ((JDialog)this.getParent().getParent().getParent().getParent()).setVisible(false);
        } else {
            this.setVisible(false);
        }
    }//GEN-LAST:event_btnCloseActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnRegist;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane recessScrollPane;
    private com.geobeck.swing.JTableEx recessTable;
    private javax.swing.JLabel targetDate;
    // End of variables declaration//GEN-END:variables
    
    /*
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test");
        RegistRecessPanel panel = new RegistRecessPanel();
        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        
    }
     */

}
