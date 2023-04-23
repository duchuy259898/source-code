/*
 * MstTechnicPanel.java
 *
 * Created on 2006/10/20, 10:58
 */
package com.geobeck.sosia.pos.hair.data.message;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;
import java.sql.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.util.*;
import com.geobeck.sosia.pos.swing.*;
import com.geobeck.sosia.pos.master.product.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.io.*;
import java.text.SimpleDateFormat;

/**
 *
 * @author katagiri
 */
public class DataMessagesPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {

    private Integer selIndex = -1;
    private DataMessages listMsg = new DataMessages();
    private static final String LOG_FILE_NAME = "msg.log";
    public boolean fromMain = false;
    public boolean isLoad = false;

    public boolean isIsLoad() {
        return isLoad;
    }

    public void setIsLoad(boolean isLoad) {
        this.isLoad = isLoad;
    }

    /**
     * Creates new form MstTechnicPanel
     */
    
    public DataMessagesPanel(boolean fromMain) {
        super();
        this.fromMain = fromMain;
        initComponents();
        addMouseCursorChange();
        this.setSize(798, 608);
        this.setPath("お知らせ");
        this.setTitle("お知らせ");
        this.init();
        tblog.setRowHeight(43);
        tblog.getTableHeader().setPreferredSize(new Dimension(400, 0));
    }

    private static String getLogFilePath() {
        return SystemInfo.getLogRoot() + "/log";
    }

    private String getlogTimer() {
        String logPath = getLogFilePath() + "/" + LOG_FILE_NAME;
        String textLine;
        try {
            FileReader fr = new FileReader(logPath);
            BufferedReader reader = new BufferedReader(fr);
            textLine = reader.readLine();
            TwoByteToOneByte c = new TwoByteToOneByte();
            textLine = c.convert(textLine);
            textLine = textLine.replace(" ", "");
            String textLine1 = textLine.substring(0, 10);
            String textLine2 = textLine.substring(10);
            textLine = textLine1 + " " + textLine2;
            return textLine;

        } catch (Exception ex) {
            return "1900/01/01 00:00";
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        praiseTimeLimitGroup = new javax.swing.ButtonGroup();
        buttonGroupWebReservation = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        imagePanel1 = new com.geobeck.swing.ImagePanel();
        btnReadLater = new javax.swing.JButton();
        btnReaded = new javax.swing.JButton();
        blogScrollPane = new javax.swing.JScrollPane();
        tblog = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextMessage = new javax.swing.JTextPane();

        setAlignmentY(0.0F);
        setFocusCycleRoot(true);

        jPanel1.setOpaque(false);

        jLabel2.setBounds(748, 3, 70, 30);
        jLabel2.setIcon(SystemInfo.getImageIcon("/menu/main/title.jpg"));

        imagePanel1.setImage(SystemInfo.getImageIcon("/menu/main/txt.jpg"));
        imagePanel1.setLayout(null);

        btnReadLater.setIcon(SystemInfo.getImageIcon("/menu/main/btn_after.jpg"));

        btnReadLater.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        btnReadLater.setBorderPainted(false);

        btnReadLater.setContentAreaFilled(false);
        btnReadLater.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadLaterActionPerformed(evt);
            }
        });
        btnReadLater.setBounds(587, 3, 95, 30);
        imagePanel1.add(btnReadLater);
        btnReadLater.setBounds(390, 60, 150, 30);

        btnReaded.setIcon(SystemInfo.getImageIcon("/menu/main/btn_read.jpg"));
        btnReaded.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadedActionPerformed(evt);
            }
        });
        imagePanel1.add(btnReaded);
        btnReaded.setBounds(570, 60, 150, 30);

        tblog.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblog.setGridColor(new java.awt.Color(254, 118, 118));
        tblog.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblog.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(tblog, SystemInfo.getTableHeaderRenderer());
        tblog.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(tblog);
        tblog.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblogMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblogMouseReleased(evt);
            }
        });
        tblog.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblogKeyReleased(evt);
            }
        });
        blogScrollPane.setViewportView(tblog);
        tblog.getColumnModel().getColumn(0).setMinWidth(333);
        tblog.getColumnModel().getColumn(0).setPreferredWidth(333);
        tblog.getColumnModel().getColumn(0).setMaxWidth(333);

        jTextMessage.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(254, 118, 118)));
        jScrollPane2.setViewportView(jTextMessage);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(blogScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 335, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, 0)
                        .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 455, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(imagePanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 788, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(0, 29, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(blogScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                    .add(jScrollPane2))
                .add(0, 0, 0)
                .add(imagePanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 1, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnReadLaterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadLaterActionPerformed
        // TODO add your handling code here:
        if (this.isDialog()) {
            ((JDialog) this.getParent().getParent().getParent().getParent()).setVisible(false);
        } else {
            this.setVisible(false);
        }
    }//GEN-LAST:event_btnReadLaterActionPerformed

    private void btnReadedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadedActionPerformed
        // TODO add your handling code here:
        writLogMsg();
        if (this.isDialog()) {
            ((JDialog) this.getParent().getParent().getParent().getParent()).setVisible(false);
        } else {
            this.setVisible(false);
        }

    }//GEN-LAST:event_btnReadedActionPerformed

    private void tblogMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblogMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblogMouseClicked

    private void tblogMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblogMouseReleased
        // TODO add your handling code here:
         this.changeCurrentData();
    }//GEN-LAST:event_tblogMouseReleased

    private void tblogKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblogKeyReleased
        // TODO add your handling code here:
        this.changeCurrentData();
    }//GEN-LAST:event_tblogKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane blogScrollPane;
    private javax.swing.JButton btnReadLater;
    private javax.swing.JButton btnReaded;
    private javax.swing.ButtonGroup buttonGroupWebReservation;
    private com.geobeck.swing.ImagePanel imagePanel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane jTextMessage;
    private javax.swing.ButtonGroup praiseTimeLimitGroup;
    private javax.swing.JTable tblog;
    // End of variables declaration//GEN-END:variables

    /**
     * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
     */
    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(btnReadLater);
        SystemInfo.addMouseCursorChange(btnReaded);
    }

    /**
     * 初期化処理を行う。
     */
    private void init() {
        try {
            listMsg.setDateLog(this.getlogTimer().trim());
            listMsg.loadAll(SystemInfo.getBaseConnection(), fromMain);
            this.refresh();
        } catch (Exception ex) {
        }
    }

    private boolean writLogMsg() {
        String logPath = getLogFilePath() + "/" + LOG_FILE_NAME;
        boolean flag = false;
        try {
            Timestamp temp = null;
            DataOutputStream out = new DataOutputStream(new FileOutputStream(logPath));

            ConnectionWrapper con = SystemInfo.getBaseConnection();
            //サーバーのシステム日付を取得
            ResultSetWrapper rs = con.executeQuery("select current_timestamp as sysdate");
            if (rs.next()) {
                temp = rs.getTimestamp("sysdate");
            }
            //nhanvt start edit 20141021 Bug #31518
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            //out.writeBytes(sdf.format(temp).toString());
            out.writeBytes(temp.toString());
            //nhanvt end edit 20141021 Bug #31518
            out.close();
            flag = true;
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return flag;
    }

    /**
     * 再表示を行う。
     */
    private void refresh() {
        //テーブルに商品マスタデータを表示する
        this.showData();
        //入力をクリアする
        this.clear();
    }

    /**
     * 入力項目をクリアする。
     */
    private void clear() {
        selIndex = -1;
        this.changeCurrentData();
    }

    /**
     * データを表示する。
     */
    private void showData() {
        SwingUtil.clearTable(tblog);
        DefaultTableModel model = (DefaultTableModel) tblog.getModel();
        //nhanvt start edit 20141030 Request #31535
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        //nhanvt end edit 20141030 Request #31535
        for (DataMessage ms : listMsg) {
            this.setIsLoad(true);
            Object[] rowData = { sdf.format(ms.getViewableDate()) + "  " + ms.getMsgText1()
            };
            model.addRow(rowData);
            
        }
     
        
    }

    /**
     * 選択データが変更されたときの処理を行う。
     */
    private void changeCurrentData() {
        int index = tblog.getSelectedRow();

        if (0 <= index && index < listMsg.size() && index != selIndex) {
            selIndex = index;
            //選択されているデータを表示
            
            this.showCurrentData(listMsg.get(index));
        }
    }

    /**
     * 選択されたデータを入力項目に表示する。
     */
    private void showCurrentData(DataMessage msg) {
    
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        StringBuilder sql = new StringBuilder();
        String strShow = msg.getMsgText2().replaceAll("\n", "<br/>");
        strShow = strShow.replaceAll(" ", "&nbsp;");
        
        sql.append("<html>");
        //nhanvt start edit 20141030 Request #31535
        sql.append(" <div  style='font:bold 11px Tahoma;color:#fe7676;margin-left: 5px; margin-top: 10px;'>  "+ sdf.format(msg.getViewableDate()).toString()+"<br/>");
        sql.append(  msg.getMsgText1() +" </div>");
        sql.append(" <br/> <p style='font:8.5px Tahoma;margin-left: 5px;' > "+ strShow+"</p>");
        sql.append("</html>");
        //nhanvt end edit 20141030 Request #31535
        jTextMessage.setContentType("text/html");
        jTextMessage.setText(sql.toString());
        
    }

    //nhanvt start edit 20141022 Request #31535
    /**
     * JTableの列幅を初期化する。
     */
//    private void initTableColumnWidth() {
//        //列の幅を設定する。
//        tblog.getColumnModel().getColumn(0).setPreferredWidth(150);
//        tblog.getColumnModel().getColumn(1).setPreferredWidth(50);
//    }
    //nhanvt end edit 20141022 Request #31535
}
