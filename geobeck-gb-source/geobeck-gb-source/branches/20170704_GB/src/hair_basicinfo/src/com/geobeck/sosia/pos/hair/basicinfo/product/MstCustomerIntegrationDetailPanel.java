/*
 * MstAutoMailSettingDetailPanel.java
 *
 * Created on 2011/02/23
 */

package com.geobeck.sosia.pos.hair.basicinfo.product;

import com.geobeck.sosia.pos.hair.customer.MstCustomerPanel;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.logging.*;
import com.geobeck.sosia.pos.search.account.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.util.DateUtil;
import java.util.*;
import javax.swing.table.DefaultTableModel;

class RadioButtonRenderer implements TableCellRenderer {
  public Component getTableCellRendererComponent(JTable table, Object value,
                   boolean isSelected, boolean hasFocus, int row, int column) {
    if (value==null) return null;
    return (Component)value;
  }
}
class RadioButtonEditor extends    DefaultCellEditor
                        implements ItemListener {
  private JRadioButton button;

  public RadioButtonEditor(JCheckBox checkBox) {
    super(checkBox);
  }

  public Component getTableCellEditorComponent(JTable table, Object value,
                   boolean isSelected, int row, int column) {
    if (value==null) return null;
    button = (JRadioButton)value;
    button.addItemListener(this);
    return (Component)value;
  }

  public Object getCellEditorValue() {
    button.removeItemListener(this);
    return button;
  }

  public void itemStateChanged(ItemEvent e) {
    super.fireEditingStopped();
  }
}

/**
 *
 * @author  geobeck
 */
public class MstCustomerIntegrationDetailPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx
{
        private boolean name;
        private boolean post;
        private boolean address;
        private boolean tel1;
        private boolean tel2;
        private boolean birthday;
        private boolean chkNotMember;

	public MstCustomerIntegrationDetailPanel(Integer customerId,
                                                    boolean name,
                                                    boolean post,
                                                    boolean address,
                                                    boolean tel1,
                                                    boolean tel2,
                                                    boolean birthday,
                                                    boolean chkNotMember)
	{
            super();

            //�`�F�b�N������ێ�
            this.name=name;
            this.post=post;
            this.address=address;
            this.tel1=tel1;
            this.tel2=tel2;
            this.birthday=birthday;
            this.chkNotMember=chkNotMember;
            
            initComponents();
            this.addMouseCursorChange();
	    this.setSize(800, 600);
            this.setPath("��{�ݒ� >> �ڋq�֘A");
            this.setTitle("�ڋq����(�ڍ�)");

            customer_info.getColumnModel().getColumn(1).setCellRenderer(new MyCellRenderer());



            this.load(customerId);
            this.showData();

	}

        class MyCellRenderer extends DefaultTableCellRenderer{
        public MyCellRenderer(){
            super();
        }

        public Component getTableCellRendererComponent(JTable table,
                                            Object value,boolean isSelected,boolean hasFocus,int row,int column){
            super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);

            //������ݒ�A���̏ꍇ�́u����v
            setHorizontalAlignment(this.LEFT);
            setVerticalAlignment(this.TOP);

            return this;
            }
        }

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateGroup = new javax.swing.ButtonGroup();
        sexGroup = new javax.swing.ButtonGroup();
        visitTypeGroup = new javax.swing.ButtonGroup();
        integrationButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        autoMailScrollPane = new javax.swing.JScrollPane();
        customer_list = new com.geobeck.swing.JTableEx();
        backButton = new javax.swing.JButton();
        autoMailScrollPane1 = new javax.swing.JScrollPane();
        customer_info = new com.geobeck.swing.JTableEx();

        setFocusCycleRoot(true);

        integrationButton.setIcon(SystemInfo.getImageIcon("/button/commodity/cust_integration_off.jpg"));
        integrationButton.setBorderPainted(false);
        integrationButton.setPressedIcon(SystemInfo.getImageIcon("/button/commodity/cust_integration_on.jpg"));
        integrationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                integrationButtonActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("MS UI Gothic", 1, 12));
        jLabel3.setText("�y�d���ڋq�ꗗ�z");

        jLabel4.setFont(new java.awt.Font("MS UI Gothic", 1, 12));
        jLabel4.setText("�y�ڋq���z�i���̃��X�g��I������ƕ\�����܂��j");

        jLabel35.setText("<html>�ڋq�������s���ꍇ�́A��ɂȂ�ڋq�Ɂu��́v�̃`�F�b�N�����Ă��������B<br><br>�܂��A�����ڋq���Y���̏ꍇ�͓����������ڋq�Ɂu�����v�̃`�F�b�N�����Ă��������B");

        jLabel36.setForeground(java.awt.Color.red);
        jLabel36.setText("<html>�ڋq�������s���ƁA�ߋ��̗��X�������o�^����A��̂ƂȂ�Ȃ��ڋq�f�[�^��<br><br>�폜����܂��̂ŁA�ڋq�����������ۂ͏\���ɂ����ӂ��������B");

        autoMailScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        customer_list.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "�ڋq���", "�ڋqNO", "�ڋq��", "���X��", "���", "����"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        customer_list.setSelectionBackground(new java.awt.Color(255, 205, 130));
        customer_list.setSelectionForeground(new java.awt.Color(0, 0, 0));
        customer_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customer_list.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(customer_list, SystemInfo.getTableHeaderRenderer());
        customer_list.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        this.initTableColumnWidth();
        customer_list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customer_listMouseReleased(evt);
            }
        });
        customer_list.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                customer_listPropertyChange(evt);
            }
        });
        customer_list.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                customer_listKeyReleased(evt);
            }
        });
        autoMailScrollPane.setViewportView(customer_list);
        customer_list.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        customer_list.getColumnModel().getColumn(0).setHeaderValue("�ڋq���");
        customer_list.getColumnModel().getColumn(1).setHeaderValue("�ڋqNO");
        customer_list.getColumnModel().getColumn(2).setHeaderValue("�ڋq��");
        customer_list.getColumnModel().getColumn(3).setHeaderValue("���X��");
        customer_list.getColumnModel().getColumn(4).setHeaderValue("���");
        customer_list.getColumnModel().getColumn(5).setHeaderValue("����");
        customer_list.getAccessibleContext().setAccessibleParent(customer_list);

        backButton.setIcon(SystemInfo.getImageIcon("/button/common/back_off.jpg"));
        backButton.setBorderPainted(false);
        backButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/back_on.jpg"));
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        autoMailScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        customer_info.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"�ڋqNO", null},
                {"����", null},
                {"�ӂ肪��", null},
                {"�X�֔ԍ�", null},
                {"�s���{��", null},
                {"�s�撬��", null},
                {"����E�Ԓn", null},
                {"�}���V��������", null},
                {"�d�b�ԍ�", null},
                {"�g�єԍ�", null},
                {"PC���[��", null},
                {"�g�у��[��", null},
                {"���N����", null},
                {"����", null},
                {"�E��", null},
                {"���l", null}
            },
            new String [] {
                "�^�C�g�� 1", "�^�C�g�� 2"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        customer_info.setRowHeight(20);
        customer_info.setSelectionBackground(new java.awt.Color(204, 204, 204));
        customer_info.setSelectionForeground(new java.awt.Color(0, 0, 0));
        customer_info.setTableHeader(null);
        customer_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customer_list.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(customer_list, SystemInfo.getTableHeaderRenderer());
        customer_list.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        this.initCustomerInfoColumnWidth();
        customer_info.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customer_infoMouseReleased(evt);
            }
        });
        customer_info.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                customer_infoPropertyChange(evt);
            }
        });
        customer_info.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                customer_infoKeyReleased(evt);
            }
        });
        autoMailScrollPane1.setViewportView(customer_info);
        customer_info.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel36)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel3)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 398, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, autoMailScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(layout.createSequentialGroup()
                        .add(82, 82, 82)
                        .add(integrationButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(backButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(37, 37, 37))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(18, 18, 18)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(autoMailScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                            .add(jLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(34, 34, 34)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(integrationButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(backButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(jLabel4))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(autoMailScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 386, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(jLabel36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 42, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(autoMailScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE))
                .add(45, 45, 45))
        );
    }// </editor-fold>//GEN-END:initComponents

/**/
        private void customer_listMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customer_listMouseReleased
            this.changeCurrentData();
}//GEN-LAST:event_customer_listMouseReleased

        private void customer_listKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customer_listKeyReleased
            this.changeCurrentData();
}//GEN-LAST:event_customer_listKeyReleased

        private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
            this.closeWindow();
        }//GEN-LAST:event_backButtonActionPerformed

        private void customer_listPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_customer_listPropertyChange
            // TODO add your handling code here:
            this.changeIntegration();
        }//GEN-LAST:event_customer_listPropertyChange

        private void integrationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_integrationButtonActionPerformed
            // TODO add your handling code here:

            //���̓`�F�b�N
            if (!checkInput())
            {
                return;
            }

            if (MessageDialog.showYesNoDialog(
                    this,
                    "�ڋq�������s���ƁA���ɖ߂����Ƃ͂ł��܂���B\n���̂܂܎��s���Ă���낵���ł����H",
                    this.getTitle(),
                    JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION)
            {
                return;
            }

            if(this.regist()) {
                MessageDialog.showMessageDialog(
                    this,
                    MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS),
                    this.getTitle(),
                    JOptionPane.INFORMATION_MESSAGE);
           } else {
                MessageDialog.showMessageDialog(
                    this,
                    MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED,"�d���ڋq"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
           }

           this.closeWindow();
        }//GEN-LAST:event_integrationButtonActionPerformed

        private void customer_infoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customer_infoMouseReleased
            // TODO add your handling code here:
        }//GEN-LAST:event_customer_infoMouseReleased

        private void customer_infoPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_customer_infoPropertyChange
            // TODO add your handling code here:
        }//GEN-LAST:event_customer_infoPropertyChange

        private void customer_infoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customer_infoKeyReleased
            // TODO add your handling code here:
        }//GEN-LAST:event_customer_infoKeyReleased

/**/
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane autoMailScrollPane;
    private javax.swing.JScrollPane autoMailScrollPane1;
    private javax.swing.JButton backButton;
    private com.geobeck.swing.JTableEx customer_info;
    private com.geobeck.swing.JTableEx customer_list;
    private javax.swing.ButtonGroup dateGroup;
    private javax.swing.JButton integrationButton;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.ButtonGroup sexGroup;
    private javax.swing.ButtonGroup visitTypeGroup;
    // End of variables declaration//GEN-END:variables

   protected SearchCostomer	sa	=	new SearchCostomer();
   protected MstCustomerIntegration customerIntegration = new MstCustomerIntegration();

   /**
    * �ڋq�����������s���B
    */
    public void load(Integer customerId)
    {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                sa.setName(this.name);
                sa.setPost(this.post);
                sa.setAddress(this.address);
                sa.setTel1(this.tel1);
                sa.setTel2(this.tel2);
                sa.setBirthday(this.birthday);
                sa.setChkNotMember(this.chkNotMember);
                
                ConnectionWrapper con = SystemInfo.getConnection();
                sa.loadCustomerDetail(con, customerId);

        } catch(SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

   /**
    * �d���ڋq�ꗗ��\������B
    */
    private void showData()
    {
        SwingUtil.clearTable(customer_list);

        DefaultTableModel model = (DefaultTableModel)customer_list.getModel();
        if( customer_list.getCellEditor() != null ) customer_list.getCellEditor().stopCellEditing();
	model.setRowCount(0);

        TableColumn c = customer_list.getColumnModel().getColumn(4);
        c.setCellRenderer(new RadioButtonsRenderer());
        c.setCellEditor(new RadioButtonsEditor(model));


         //customer_list.setDefaultRenderer(Object.class, new MultiLineCellRenderer());
         //customer_list.setRowSelectionAllowed(false);
         //customer_list.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for(CustomerData cd : sa)
	{
            model.addRow(new Object[]{
                getDetailButton(cd),
                cd.getCustomerNo_D(),
                cd,
                cd.getVisitNumber_D(),
                null,
                true
            });

            ary_intergration.add(cd.getCustomerId_D());
        }

    }

   /**
    * �ڋq�����o�^�������s���B
    */
    private boolean regist() {
        boolean result = false;
        try {

            ConnectionWrapper	con	=	SystemInfo.getConnection();

            con.begin();

            if(this.registData(con)) {
                con.commit();
                result = true ;
            } else {
                con.rollback();
                result = false;
            }
        } catch(SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return result ;
    }

        /**
     *
     * @param con
     * @return
     */
    private boolean registData(ConnectionWrapper con) {
        try {

            //�\��f�[�^(data_reservation)
            if(!customerIntegration.data_reservation_regist(con, ary_intergration, ary_main)) {
                return	false;
            }

            //�\��f�[�^TEMP(data_reservation_temp)
            if(!customerIntegration.data_reservation_temp_regist(con, ary_intergration, ary_main)) {
                return	false;
            }

            //�̔��f�[�^(data_sales)
            if(!customerIntegration.data_sales_regist(con, ary_intergration, ary_main)) {
                return	false;
            }

            //�ڋq�}�X�^(mst_customer)
            if(!customerIntegration.mst_customer_regist(con, ary_intergration, ary_main)) {
                return	false;
            }

            //�|�C���g�����f�[�^(data_point)
            if(!customerIntegration.data_point_regist(con, ary_intergration, ary_main)) {
                return	false;
            }

            //�C���[�W�J���e�f�[�^(data_image_karte)
            if(!customerIntegration.data_image_karte_regist(con, ary_intergration, ary_main)) {
                return	false;
            }

            //DM�����ڍ׃f�[�^(data_dm_history_detail)
            if(!customerIntegration.data_dm_history_detail_regist(con, ary_intergration, ary_main)) {
                return	false;
            }

            //�t���[���ڃ��[�U�f�[�^(mst_customer_free_heading)
            if(!customerIntegration.mst_customer_free_heading_regist(con, ary_intergration, ary_main)) {
                return	false;
            }

        } catch(SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return	false;
        }

        return true;
    }

        /**
         * �{�^���Ƀ}�E�X�J�[�\����������Ƃ��ɃJ�[�\����ύX����C�x���g��ǉ�����B
         */
        private void addMouseCursorChange() {
            SystemInfo.addMouseCursorChange(backButton);
            SystemInfo.addMouseCursorChange(integrationButton);
        }

        /**
	 * �ڍ׃{�^�����擾����
	 */
	private JButton getDetailButton(final CustomerData ad)
	{
            JButton button = new JButton();
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);

            button.setName("");
            button.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                            "/images/" + SystemInfo.getSkinPackage() + "/button/common/customer_off.jpg")));
            button.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
                            "/images/" + SystemInfo.getSkinPackage() + "/button/common/customer_on.jpg")));

            button.setSize(48, 25);
            button.setEnabled(true);

            button.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    MstCustomerPanel mcp = null;

                    try {

                        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        mcp = new MstCustomerPanel(ad.getCustomerId_D() , false, false, true);
                        SwingUtil.openAnchorDialog( null, true, mcp, "�ڋq���", SwingUtil.ANCHOR_HCENTER|SwingUtil.ANCHOR_VCENTER );

                    } finally {
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }

                    ((JDialog)mcp.getParent().getParent().getParent().getParent()).dispose();
                }
            });
            return button;
	}

	/**
	 * ���̓`�F�b�N���s���B
	 * @return ���̓G���[���Ȃ����true��Ԃ��B
	 */
	private boolean checkInput()
	{
            if (ary_main.size()==0)
            {
                MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(6004),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (ary_intergration.size()<2)
            {
                MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(6005),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (ary_intergration.indexOf(ary_main.get(0)) == -1)
            {
                MessageDialog.showMessageDialog(this,
                            MessageUtil.getMessage(6006),
                            this.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                return false;
            }

            return true;
	}

        private ArrayList<Integer> ary_intergration = new ArrayList<Integer>();
        private ArrayList<Integer> ary_main = new ArrayList<Integer>();
        private void changeIntegration() {
            //int		index		=	category.getSelectedIndex();
            //JTable	table	=	customer_info;
            int	row	=	customer_list.getSelectedRow();
            int	col	=	customer_list.getSelectedColumn();

            if(row < 0 || col < 1)	return;

            Boolean	customerlist;
            int curRow = 0;

                for(CustomerData cd : sa)
                {
                   if (curRow==row)
                   {
                       if (col==5)  //����
                       {
                           customerlist = (Boolean)customer_list.getValueAt(row, col);
                           if (customerlist==true)
                           {
                                if(!ary_intergration.contains(cd.getCustomerId_D()))
                                {
                                    ary_intergration.add(cd.getCustomerId_D());
                                }
                           }
                           else
                           {
                               if(ary_intergration.contains(cd.getCustomerId_D()))
                               {
                                    ary_intergration.remove(ary_intergration.indexOf(cd.getCustomerId_D()));
                               }
                           }
                       }
                       else if (col==4) //���
                       {
                                ary_main.clear();
                                ary_main.add(cd.getCustomerId_D());
                       }
                    }
                    curRow+=1;
                }      
        }

	/**
	 * JTable�̗񕝂�����������B
	 */
	private void initTableColumnWidth()
	{
            //��̕���ݒ肷��B
            
            customer_list.getColumnModel().getColumn(0).setPreferredWidth(30);      //�ڋq���
	    customer_list.getColumnModel().getColumn(1).setPreferredWidth(30);      //�ڋqNO
            customer_list.getColumnModel().getColumn(2).setPreferredWidth(80);      //�ڋq��
            customer_list.getColumnModel().getColumn(3).setPreferredWidth(25);      //���X��
            customer_list.getColumnModel().getColumn(4).setPreferredWidth(15);      //���
            customer_list.getColumnModel().getColumn(5).setPreferredWidth(15);      //����
	}

        /**
	 * JTable�̗񕝂�����������B
	 */
	private void initCustomerInfoColumnWidth()
	{
            //��̕���ݒ肷��B

            customer_info.getColumnModel().getColumn(0).setPreferredWidth(30);
	    customer_info.getColumnModel().getColumn(1).setPreferredWidth(150);
            
            customer_info.setRowHeight(15, 144);

            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer ();
            renderer.setBackground(Color.lightGray);
            customer_info.getColumnModel().getColumn(0).setCellRenderer(renderer);

	}

	/**
	 * �_�C�A���O�����
	 */
	private void closeWindow() {
                if(this.isDialog()) {
                        ((JDialog)this.getParent().getParent().getParent().getParent()).setVisible(false);
                } else {
                        this.setVisible(false);
                }
	}

        private void changeCurrentData() {

	    if (customer_list.getSelectedRow() < 0) return;

            CustomerData ad = (CustomerData)customer_list.getValueAt(customer_list.getSelectedRow(), 2);

            SwingUtil.clearTable(customer_info);

            DefaultTableModel model = (DefaultTableModel)customer_info.getModel();

            for (int row = 0; row < 16; row++) {
                switch (row) {
                    case 0:
                        model.addRow(new Object[]{
                        "�ڋqNO",
                        ad.getCustomerNo_D()
                        });
                        break;
                    case 1:
                        model.addRow(new Object[]{
                        "����",
                        ad.getCustomerName_D()
                        });
                        break;
                    case 2:
                        model.addRow(new Object[]{
                        "�ӂ肪��",
                        ad.getCustomerKana_D()
                        });
                        break;
                    case 3:
                        model.addRow(new Object[]{
                        "�X�֔ԍ�",
                        ad.getPostalcode_D()
                        });
                        break;
                    case 4:
                        model.addRow(new Object[]{
                        "�s���{��",
                        ad.getAddress1_D()
                        });
                        break;
                    case 5:
                        model.addRow(new Object[]{
                        "�s�撬��",
                        ad.getAddress2_D()
                        });
                        break;
                    case 6:
                        model.addRow(new Object[]{
                        "����E�Ԓn",
                        ad.getAddress3_D()
                        });
                        break;
                    case 7:
                        model.addRow(new Object[]{
                        "�}���V��������",
                        ad.getAddress4_D()
                        });
                        break;
                    case 8:
                        model.addRow(new Object[]{
                        "�d�b�ԍ�",
                        ad.getPhoneNumber_D()
                        });
                        break;
                    case 9:
                        model.addRow(new Object[]{
                        "�g�єԍ�",
                        ad.getCellularNumber_D()
                        });
                        break;
                    case 10:
                        model.addRow(new Object[]{
                        "PC���[��",
                        ad.getPc_mail_address_D()
                        });
                        break;
                    case 11:
                        model.addRow(new Object[]{
                        "�g�у��[��",
                        ad.getCellular_mail_address_D()
                        });
                        break;
                    case 12:
                        model.addRow(new Object[]{
                        "���N����",
                        DateUtil.format(ad.getBirthday_D(), "yyyy/MM/dd")
                        });
                        break;
                    case 13:
                        model.addRow(new Object[]{
                        "����",
                        ad.getSex_D()
                        });
                        break;
                    case 14:
                        model.addRow(new Object[]{
                        "�E��",
                        ad.getJob_D()
                        });
                        break;
                    case 15:
                        String crlf = "\n";
                        String note = ad.getNote_D();
                        if (note != null)
                        {
                            note = note.replace(crlf, "<br>");
                        }
                        else
                        {
                           note = "";
                        }

                        model.addRow(new Object[]{
                        "���l",
                        "<html>"+ note+"</html>+"
                        //ad.getNote_D()
                        });

                        customer_info.setRowHeight(15, 144);
                        break;
                }
            }

        }
}

        class RadioButtonsRenderer extends JRadioButton implements TableCellRenderer {
            public RadioButtonsRenderer() {
                super();
                setName("Table.cellRenderer");
                setHorizontalAlignment(SwingConstants.CENTER);
                setBackground(Color.white);
            }
            @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if(value instanceof Boolean) {
                    setSelected((Boolean)value);
                }
                return this;
            }
        }
        class RadioButtonsEditor extends JRadioButton implements TableCellEditor {
            public RadioButtonsEditor(final DefaultTableModel model) {
                super();
                setHorizontalAlignment(SwingConstants.CENTER);
                setBackground(Color.white);
                addActionListener(new ActionListener() {
                    @Override public void actionPerformed(ActionEvent e) {
                        fireEditingStopped();
                         for(int i=0; i<model.getRowCount(); i++) {
                             model.setValueAt(i==index, i, 4);
                         }
                    }
                });
            }
            private int index = -1;
            @Override public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                index = table.convertRowIndexToModel(row);
                if(value instanceof Boolean) {
                    setSelected((Boolean)value);
                }
                return this;
            }
            @Override public Object getCellEditorValue() {
                return isSelected();
            }

            //Copid from AbstractCellEditor
            //protected EventListenerList listenerList = new EventListenerList();
            //transient protected ChangeEvent changeEvent = null;
            @Override public boolean isCellEditable(java.util.EventObject e) {
                return true;
            }
            @Override public boolean shouldSelectCell(java.util.EventObject anEvent) {
                return true;
            }
            @Override public boolean stopCellEditing() {
                fireEditingStopped();
                return true;
            }
            @Override public void  cancelCellEditing() {
                fireEditingCanceled();
            }
            @Override public void addCellEditorListener(CellEditorListener l) {
                listenerList.add(CellEditorListener.class, l);
            }
            @Override public void removeCellEditorListener(CellEditorListener l) {
                listenerList.remove(CellEditorListener.class, l);
            }
            public CellEditorListener[] getCellEditorListeners() {
                return (CellEditorListener[])listenerList.getListeners(CellEditorListener.class);
            }
            protected void fireEditingStopped() {
                // Guaranteed to return a non-null array
                Object[] listeners = listenerList.getListenerList();
                // Process the listeners last to first, notifying
                // those that are interested in this event
                for(int i = listeners.length-2; i>=0; i-=2) {
                    if(listeners[i]==CellEditorListener.class) {
                        // Lazily create the event:
                        if(changeEvent == null) changeEvent = new ChangeEvent(this);
                        ((CellEditorListener)listeners[i+1]).editingStopped(changeEvent);
                    }
                }
            }
            protected void fireEditingCanceled() {
                // Guaranteed to return a non-null array
                Object[] listeners = listenerList.getListenerList();
                // Process the listeners last to first, notifying
                // those that are interested in this event
                for(int i = listeners.length-2; i>=0; i-=2) {
                    if(listeners[i]==CellEditorListener.class) {
                        // Lazily create the event:
                        if(changeEvent == null) changeEvent = new ChangeEvent(this);
                        ((CellEditorListener)listeners[i+1]).editingCanceled(changeEvent);
                    }
                }
            }
        }
