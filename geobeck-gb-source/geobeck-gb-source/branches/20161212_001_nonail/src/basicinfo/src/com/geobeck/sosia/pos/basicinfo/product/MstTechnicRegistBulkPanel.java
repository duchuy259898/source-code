/*
 * MstTechnicRegistBulkPanel.java
 *
 * Created on 2017/01/04, 10:10
 */

package com.geobeck.sosia.pos.basicinfo.product;

import com.geobeck.sosia.pos.hair.master.product.MstTechnic;
import com.geobeck.sosia.pos.hair.master.product.MstTechnicClass;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.product.MstUseProduct;
import com.geobeck.sosia.pos.products.ProductClass;
import com.geobeck.sosia.pos.products.ProductClasses;
import com.geobeck.sosia.pos.swing.SelectTableCellRenderer;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.swing.CustomFilter;
import com.geobeck.swing.MessageDialog;
import com.geobeck.swing.SwingUtil;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.JTextField;
import javax.swing.text.PlainDocument;
import org.apache.commons.lang.BooleanUtils;

/**
 * 技術一括登録画面
 * @author 
 */
public class MstTechnicRegistBulkPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {
    
    /** 英数記号の正規表現 */
    public static final String ALPHAMERIC_SYMBOL = "0-9a-zA-Z\\p{Punct}";
    
    public MstTechnicRegistBulkPanel(Integer productDivision) {
        initComponents();
        addMouseCursorChange();
        this.setSize(830, 720);
        this.setPath("基本設定 >> 商品マスタ");
        this.setTitle("技術一括登録");
        this.init();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        registButton = new javax.swing.JButton();
        productClassScrollPane = new javax.swing.JScrollPane();
        technicTable = new javax.swing.JTable();
        technicNameLabel = new javax.swing.JLabel();
        technicName = new javax.swing.JTextField();
        ((PlainDocument)technicName.getDocument()).setDocumentFilter(
            new CustomFilter(30));
        technicNoLabel = new javax.swing.JLabel();
        technicNo = new javax.swing.JTextField();
        ((PlainDocument)technicNo.getDocument()).setDocumentFilter(
            new CustomFilter(15, ALPHAMERIC_SYMBOL));

        registButton.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
        registButton.setBorderPainted(false);
        registButton.setContentAreaFilled(false);
        registButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
        registButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registButtonActionPerformed(evt);
            }
        });

        technicTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "分類名", "価格(税込)", "施術時間", "賞美期限", "賞美日数", "WEB予約", "分類ID[hidden]"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Long.class, java.lang.Integer.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.Boolean.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        technicTable.setColumnSelectionAllowed(true);
        technicTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        technicTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        technicTable.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(technicTable, SystemInfo.getTableHeaderRenderer());
        technicTable.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        SelectTableCellRenderer.setSelectTableCellRenderer(technicTable);
        TableColumnModel model = technicTable.getColumnModel();
        model.getColumn(1).setCellEditor(new IntegerCellCustomEditor(new JTextField()));
        IntegerCellCustomEditor editor = new IntegerCellCustomEditor(new JTextField());
        editor.setLimit (4);
        model.getColumn(2).setCellEditor(editor);
        model.getColumn(4).setCellEditor(new IntegerCellCustomEditor(new JTextField()));
        productClassScrollPane.setViewportView(technicTable);
        if (technicTable.getColumnModel().getColumnCount() > 0) {
            technicTable.getColumnModel().getColumn(6).setResizable(false);
            technicTable.getColumnModel().getColumn(6).setPreferredWidth(0);
        }
        technicTable.getColumnModel().getColumn(0).setPreferredWidth(130);
        technicTable.getColumnModel().getColumn(6).setResizable(false);
        technicTable.getColumnModel().getColumn(6).setPreferredWidth(0);
        technicTable.getColumnModel().getColumn(6).setMinWidth(0);
        technicTable.getColumnModel().getColumn(6).setMaxWidth(0);

        technicNameLabel.setText("技術名");
        technicNameLabel.setMaximumSize(new java.awt.Dimension(41, 13));
        technicNameLabel.setMinimumSize(new java.awt.Dimension(41, 13));
        technicNameLabel.setName(""); // NOI18N
        technicNameLabel.setPreferredSize(new java.awt.Dimension(41, 13));

        technicName.setNextFocusableComponent(technicNo);

        technicNoLabel.setText("技術NO");

        technicNo.setNextFocusableComponent(technicName);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, productClassScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 543, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(technicNameLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(technicNoLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                        .add(18, 18, 18)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(technicName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                            .add(technicNo))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 8, Short.MAX_VALUE)
                .add(registButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(technicNameLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(registButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(technicName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(technicNoLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(technicNo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(21, 21, 21)
                .add(productClassScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 564, Short.MAX_VALUE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
        
	private void registButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_registButtonActionPerformed
	{//GEN-HEADEREND:event_registButtonActionPerformed

            if (technicTable.getCellEditor() != null) {
                technicTable.getCellEditor().stopCellEditing();
            }
            //入力チェック
            if (this.checkInput()) {
                
                registButton.setCursor(null);

                try {

                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                    if(this.regist()) {
                       
                        MessageDialog.showMessageDialog(this,
                                MessageUtil.getMessage(MessageUtil.INFO_REGIST_SUCCESS,
                                "技術一括登録"),
                                this.getTitle(),
                                JOptionPane.INFORMATION_MESSAGE);
                        this.init();
                        technicName.requestFocusInWindow();
                        
                    } else {
                        MessageDialog.showMessageDialog(this,
                                MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED,
                                "技術一括登録"),
                                this.getTitle(),
                                JOptionPane.ERROR_MESSAGE);
                    }                

                } finally {
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            
            }

	}//GEN-LAST:event_registButtonActionPerformed
                                                        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane productClassScrollPane;
    private javax.swing.JButton registButton;
    private javax.swing.JTextField technicName;
    private javax.swing.JLabel technicNameLabel;
    private javax.swing.JTextField technicNo;
    private javax.swing.JLabel technicNoLabel;
    private javax.swing.JTable technicTable;
    // End of variables declaration//GEN-END:variables
    
    /**
     * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
     */
    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(registButton);
    }
    
    /**
     * 初期化処理を行う。
     */
    private void init() {
	
        this.showTechnicClasses();
        technicName.setText("");
        technicNo.setText("");
    }
    
    
    /**
     * 分類を表示する。
     */
    private void showTechnicClasses() {
        SwingUtil.clearTable(technicTable);
        
        DefaultTableModel model	= (DefaultTableModel)technicTable.getModel();
        
        //分類の一覧を取得
        ProductClasses pcs = new ProductClasses();
	pcs.setProductDivision(1);
        
        try {
            pcs.load(SystemInfo.getConnection());
        } catch(SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        for(ProductClass pc : pcs) {
            Vector<Object> temp	= new Vector<>();
            temp.add(pc.getProductClassName());
            temp.add(null);
            temp.add(null);
            temp.add(null);
            temp.add(null);
            temp.add(null);
            temp.add(pc.getProductClassID());
            model.addRow(temp);
        }
    }
    
    /**
     * 登録処理を行う。
     * @return true - 成功、false - 失敗
     */
    public boolean regist() {
        
        boolean result = true;
        ConnectionWrapper con = null;
        try {
            con = SystemInfo.getConnection();
            
            con.begin();
            
            for (int i = 0; i < technicTable.getRowCount(); i++) {
                Integer price = (Integer)technicTable.getValueAt(i, 1);
                Integer opeTime = (Integer)technicTable.getValueAt(i, 2);
                Boolean isPraiseTime = (Boolean)technicTable.getValueAt(i, 3);
                if (isPraiseTime == null) {
                    isPraiseTime = false;
                }
                Integer praiseTimeLimit = (Integer)technicTable.getValueAt(i, 4);
                Boolean isWeb = (Boolean)technicTable.getValueAt(i, 5);
                if (isWeb == null) {
                    isWeb = false;
                }
                Integer classId = (Integer)technicTable.getValueAt(i, 6);

                //価格が未設定の場合は対象外
                if (price == null) {
                    continue;
                }
                
                //////////////////
                //技術マスタの登録
                //////////////////
                MstTechnic mstTec = new MstTechnic();
                //技術分類ID
                MstTechnicClass mstTecClass = new MstTechnicClass(classId);
                mstTec.setTechnicClass(mstTecClass);
                //技術コード
                mstTec.setTechnicNo(technicNo.getText());
                //技術名
                mstTec.setTechnicName(technicName.getText());
                //価格
                mstTec.setPrice(price.longValue());
                //施術時間
                mstTec.setOperationTime(opeTime);
                //賞美期限使用有無
                mstTec.setPraiseTime(isPraiseTime);
                //賞美期限（日）
                mstTec.setPraiseTimeLimit(praiseTimeLimit);
                //モバイルフラグ
                mstTec.setMobileFlag(BooleanUtils.toInteger(isWeb));

                //技術マスタ登録
                mstTec.registForBulk(con);
                
                //技術IDの取得
                Integer technicID = mstTec.getTechnicID();
                
                //技術分類使用中の店舗リスト取得
                ArrayList<Integer> shopList = this.getShopIdList(con, classId);
                
                for (Integer shopId : shopList) {
                    
                    //////////////////////////////
                    //店舗使用技術・商品マスタに登録
                    //////////////////////////////
                    MstUseProduct mstUseProduct = new MstUseProduct();
                    
                    //ショップID
                    MstShop mstShop = new MstShop();
                    mstShop.setShopID(shopId);
                    mstUseProduct.setShop(mstShop);
                    
                    //区分ID
                    mstUseProduct.setProductDivision(1);
                    //商品ID
                    mstUseProduct.setProductID(technicID);
                    //価格
                    mstUseProduct.setPrice(price.longValue());

                    mstUseProduct.registForBulk(con);
                }
            }
            
        } catch(SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            result = false;
        } finally {
            if(result) {
                if (con != null) {
                    try {
                        con.commit();
                    } catch (SQLException ex) {
                        Logger.getLogger(MstTechnicRegistBulkPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    result = true;
                }
            } else {
                if (con != null) {
                    try {
                        con.rollback();
                    } catch (SQLException ex) {
                        Logger.getLogger(MstTechnicRegistBulkPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        
        return	result;
    }
    
    /**
     * 技術分類使用中の店舗リストを返す
     * 
     * @param con
     * @param shopId
     * @return 店舗リスト
     * @throws SQLException 
     */
    private ArrayList<Integer> getShopIdList(ConnectionWrapper con, Integer shopId) throws SQLException {
        
        String strSql = "select x.shop_id from";
        strSql = strSql + "(select a.shop_id, a.product_id from mst_use_product a where a.product_division = 1 and a.delete_date is null) x,";
        strSql = strSql + "(select b.technic_class_id, b.technic_id from mst_technic b where b.technic_class_id = " + shopId.toString() + " and b.delete_date is null) y ";
        strSql = strSql + "where x.product_id = y.technic_id group by x.shop_id";

        ResultSetWrapper rs = con.executeQuery(strSql);
        
        ArrayList<Integer> list = new ArrayList<>();
        try {
            while(rs.next()) {
                list.add(rs.getInt("shop_id"));
            }
        } finally {
            rs.close();
        }
        
        return list;
    }
    /**
     * 入力チェック処理を行う。
     * @return true - 成功、false - 失敗
     */
    private boolean checkInput() {
        
        //技術名
        if (technicName.getText().equals("")) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "技術名"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            technicName.requestFocusInWindow();
            return false;
        }
        //技術NO
        if (technicNo.getText().equals("")) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "技術NO"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            technicNo.requestFocusInWindow();
            return false;
        }

        boolean isAllNoPrice = true;
        //全件、金額が未入力
        for (int i = 0; i < technicTable.getRowCount(); i++) {
            Integer price = (Integer)technicTable.getValueAt(i, 1);
            if (price != null) {
                isAllNoPrice = false;
            }
        }
        if (isAllNoPrice) {
            MessageDialog.showMessageDialog(this,
                        "登録データが存在しません。",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        //施術時間必須チェック
        boolean notEntered = false;
        for (int i = 0; i < technicTable.getRowCount(); i++) {
            Integer price = (Integer)technicTable.getValueAt(i, 1);
            Integer opeTime = (Integer)technicTable.getValueAt(i, 2);
            
            //価格が入力されている　且つ　施術時間が未入力の場合
            if (price != null && opeTime == null) {
                notEntered = true;
            }
        }
        if (notEntered) {
            MessageDialog.showMessageDialog(this,
                        "施術時間が未入力の分類があります。\n価格を入力された分類には施術時間を入力してください。",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        //賞美日数必須チェック
        boolean notLimit = false;
        for (int i = 0; i < technicTable.getRowCount(); i++) {
            Integer price = (Integer)technicTable.getValueAt(i, 1);
            Integer opeTime = (Integer)technicTable.getValueAt(i, 2);
            Boolean isPraiseTime = (Boolean)technicTable.getValueAt(i, 3);
            if (isPraiseTime == null) {
                isPraiseTime = false;
            }
            Integer praiseTimeLimit = (Integer)technicTable.getValueAt(i, 4);
            
            //価格と施術時間が設定済
            if (price != null && opeTime != null) {
                if (isPraiseTime && praiseTimeLimit == null) {
                    notLimit = true;
                    break;
                }
            }
        }
        if (notLimit) {
            MessageDialog.showMessageDialog(this,
                        "賞美日数が未入力の分類があります。\n賞美期限を入力された分類には賞美日数を入力してください。",
                        this.getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    public class IntegerCellCustomEditor extends DefaultCellEditor {
        
        JTextField textField = new JTextField();

        public IntegerCellCustomEditor(JTextField field) {
            super(field);
            this.textField = field;
            textField.setHorizontalAlignment(JTextField.RIGHT);
            ((PlainDocument)textField.getDocument()).setDocumentFilter(
                            new CustomFilter(9, CustomFilter.INTEGER));

            textField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    textField.selectAll();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column){
            if (value != null) {
                textField.setText(value.toString());
            } else {
                textField.setText("");
            }

            textField.selectAll();
            return textField;
        }

        @Override
        public Integer getCellEditorValue() {
            if (textField.getText().equals("")) {
                return null;
            } else {
                return Integer.parseInt(textField.getText());
            }            
        }
        
        /**
         * 入力上限文字数の設定
         * @param limit 上限文字数
         */
        public void setLimit(int limit) {
            ((PlainDocument)textField.getDocument()).setDocumentFilter(new CustomFilter(limit, CustomFilter.INTEGER));
        }
    }
}
