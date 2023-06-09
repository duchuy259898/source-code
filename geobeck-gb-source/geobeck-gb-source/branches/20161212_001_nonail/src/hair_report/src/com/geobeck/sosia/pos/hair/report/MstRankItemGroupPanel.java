/*
 * MstRankItemGroupPanel.java
 *
 * Created on 2014/11/23
 */
package com.geobeck.sosia.pos.hair.report;

import com.geobeck.sosia.pos.system.SystemInfo;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;
import java.sql.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.sosia.pos.swing.*;
import com.geobeck.sosia.pos.util.*;

/**
 *
 * @author lvtu
 */
public class MstRankItemGroupPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {

    private MstRankItemGroups mrigs = new MstRankItemGroups();
    private Integer selIndex        = -1;

    /**
     * Creates new form ColorRankPanel
     */
    public MstRankItemGroupPanel() {
        super();
        initComponents();
        addMouseCursorChange();
        this.setSize(450, 450);
        this.setPath("顧客分析＞＞マッシャー分析");
        this.setTitle("商品Ｇ設定");
        this.setListener();
        this.init();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbRankItemGroup = new javax.swing.JLabel();
        txtRankItemGroup = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)txtRankItemGroup.getDocument()).setDocumentFilter(
            new CustomFilter(50));
        jPanel1 = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        renewButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        autoMailScrollPane = new javax.swing.JScrollPane();
        tbRankItemGroup = new com.geobeck.swing.JTableEx();
        btnClose = new javax.swing.JButton();

        setFocusCycleRoot(true);

        lbRankItemGroup.setText("商品グループ名");

        txtRankItemGroup.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        txtRankItemGroup.setColumns(20);
        txtRankItemGroup.setInputKanji(true);

        jPanel1.setOpaque(false);

        addButton.setIcon(SystemInfo.getImageIcon("/button/common/insert_off.jpg"));
        addButton.setBorderPainted(false);
        addButton.setContentAreaFilled(false);
        addButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/insert_on.jpg"));
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        renewButton.setIcon(SystemInfo.getImageIcon("/button/common/update_off.jpg"));
        renewButton.setBorderPainted(false);
        renewButton.setContentAreaFilled(false);
        renewButton.setEnabled(false);
        renewButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/update_on.jpg"));
        renewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renewButtonActionPerformed(evt);
            }
        });

        deleteButton.setIcon(SystemInfo.getImageIcon("/button/common/delete_off.jpg"));
        deleteButton.setBorderPainted(false);
        deleteButton.setContentAreaFilled(false);
        deleteButton.setEnabled(false);
        deleteButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/delete_on.jpg"));
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(98, Short.MAX_VALUE)
                .add(addButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(renewButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(deleteButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, deleteButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, renewButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, addButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        autoMailScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        tbRankItemGroup.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "商品グループ名", "詳細設定"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbRankItemGroup.setSelectionBackground(new java.awt.Color(220, 220, 220));
        tbRankItemGroup.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tbRankItemGroup.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbRankItemGroup.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(tbRankItemGroup, SystemInfo.getTableHeaderRenderer());
        tbRankItemGroup.setRowHeight(30);
        this.initTableColumnWidth();
        SelectTableCellRenderer.setSelectTableCellRenderer(tbRankItemGroup);
        tbRankItemGroup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tbRankItemGroupMouseReleased(evt);
            }
        });
        tbRankItemGroup.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbRankItemGroupKeyReleased(evt);
            }
        });
        autoMailScrollPane.setViewportView(tbRankItemGroup);

        btnClose.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
        btnClose.setBorderPainted(false);
        btnClose.setContentAreaFilled(false);
        btnClose.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(lbRankItemGroup)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(txtRankItemGroup, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 199, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(btnClose, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(autoMailScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(txtRankItemGroup, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(lbRankItemGroup))
                    .add(btnClose, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(29, 29, 29)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(autoMailScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

	private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteButtonActionPerformed
	{//GEN-HEADEREND:event_deleteButtonActionPerformed
            deleteButton.setCursor(null);
            try{
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                
                if(!this.delete())
                {
                    MessageDialog.showMessageDialog(this,
                                            MessageUtil.getMessage(MessageUtil.ERROR_DELETE_FAILED,
                                                            "商品Ｇ設定"),
                                            this.getTitle(),
                                            JOptionPane.ERROR_MESSAGE);
                }
            }
            finally
            {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
	}//GEN-LAST:event_deleteButtonActionPerformed

	private void renewButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_renewButtonActionPerformed
	{//GEN-HEADEREND:event_renewButtonActionPerformed
            renewButton.setCursor(null);
            try{
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                if (this.checkInput()) {
                    if(!this.regist(false))
                    {
                        MessageDialog.showMessageDialog(this,
                                            MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED,
                                                            "商品Ｇ設定"),
                                            this.getTitle(),
                                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            finally
            {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
	}//GEN-LAST:event_renewButtonActionPerformed

	private void addButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_addButtonActionPerformed
	{//GEN-HEADEREND:event_addButtonActionPerformed
            addButton.setCursor(null);
            try{
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                if (this.checkInput()) {
                    if(!this.regist(true))
                    {
                        MessageDialog.showMessageDialog(this,
                                            MessageUtil.getMessage(MessageUtil.ERROR_REGIST_FAILED,
                                                            "商品Ｇ設定"),
                                            this.getTitle(),
                                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            finally
            {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
	}//GEN-LAST:event_addButtonActionPerformed

    private void tbRankItemGroupKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbRankItemGroupKeyReleased
        this.changeCurrentData();
    }//GEN-LAST:event_tbRankItemGroupKeyReleased

    private void tbRankItemGroupMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbRankItemGroupMouseReleased
        this.changeCurrentData();
    }//GEN-LAST:event_tbRankItemGroupMouseReleased

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        ((JDialog)this.getParent().getParent().getParent().getParent()).setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JScrollPane autoMailScrollPane;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton deleteButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbRankItemGroup;
    private javax.swing.JButton renewButton;
    private com.geobeck.swing.JTableEx tbRankItemGroup;
    private com.geobeck.swing.JFormattedTextFieldEx txtRankItemGroup;
    // End of variables declaration//GEN-END:variables

    /**
     * 商品Ｇ設定画面用FocusTraversalPolicy
     */
    private RankItemGroupFocusTraversalPolicy ftp
            = new RankItemGroupFocusTraversalPolicy();

    /**
     * 商品Ｇ設定画面用RankItemGroupFocusTraversalPolicy。
     *
     * @return 小口項目登録画面用FocusTraversalPolicy
     */
    public RankItemGroupFocusTraversalPolicy getFocusTraversalPolicy() {
        return ftp;
    }

    /**
     * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
     */
    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(addButton);
        SystemInfo.addMouseCursorChange(renewButton);
        SystemInfo.addMouseCursorChange(deleteButton);
        SystemInfo.addMouseCursorChange(btnClose);
    }

    /**
     * コンポーネントの各リスナーをセットする。
     */
    private void setListener() {
        txtRankItemGroup.addKeyListener(SystemInfo.getMoveNextField());
        txtRankItemGroup.addFocusListener(SystemInfo.getSelectText());
    }

    /**
     * 初期化処理を行う。
     */
    private void init() {
        try {
            ConnectionWrapper con = SystemInfo.getConnection();

            mrigs.load(con);
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        this.showData();
    }

    /**
     * 再表示を行う。
     */
    private void refresh() {
        //データベースからデータを読み込む
        try {
            ConnectionWrapper con = SystemInfo.getConnection();

            mrigs.load(con);
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        //テーブルに小口項目データを表示する
        this.showData();
        //入力をクリアする
        this.clear();

        txtRankItemGroup.requestFocusInWindow();
    }

    /**
     * 入力項目をクリアする。
     */
    private void clear() {
        selIndex = -1;
        txtRankItemGroup.setText("");

        if (0 < tbRankItemGroup.getRowCount()) {
            tbRankItemGroup.removeRowSelectionInterval(0, tbRankItemGroup.getRowCount() - 1);
        }

        this.changeCurrentData();
    }

    /**
     * データを表示する。
     */
    private void showData() {
        DefaultTableModel model = (DefaultTableModel) tbRankItemGroup.getModel();

        //全行削除
        model.setRowCount(0);
        tbRankItemGroup.removeAll();

        for (MstRankItemGroup mg : mrigs) {
            Object[] rowData = {mg.getItemGroupName(),
                getSettingButton(mg)};
            model.addRow(rowData);
        }
    }
    
    /**
     * get button add table.
     * @param mg
     * @return 
     */
    private JButton getSettingButton(final MstRankItemGroup mg) {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        button.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/button/common/memo_off.jpg")));
        button.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/images/" + SystemInfo.getSkinPackage() + "/button/common/meno_on.jpg")));

        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductSettingDetail p = null;

                try {

                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    p = new ProductSettingDetail(mg);
                    SwingUtil.openAnchorDialog(null, true, p, "商品Ｇ設定＞詳細設定", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);

                } finally {
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }

                ((JDialog) p.getParent().getParent().getParent().getParent()).dispose();

                //showData();
                tbRankItemGroup.getCellEditor().stopCellEditing();
            }
        });

        return button;
    }

    /**
     * 選択データが変更されたときの処理を行う。
     */
    private void changeCurrentData() {
        int indexRow = tbRankItemGroup.getSelectedRow();
        int indexCol = tbRankItemGroup.getSelectedColumn();
        
        if (0 <= indexRow && indexRow < mrigs.size() && indexRow != selIndex) {
            if(indexCol == 0)
            {
                selIndex = indexRow;
                //選択されているデータを表示
                this.showCurrentData();
            }
        }

        renewButton.setEnabled(0 <= selIndex);
        deleteButton.setEnabled(0 <= selIndex);
    }

    /**
     * 選択されたデータを入力項目に表示する。
     */
    private void showCurrentData() {
        txtRankItemGroup.setText(mrigs.get(selIndex).getItemGroupName());
    }

    /**
     * 入力チェックを行う。
     *
     * @return 入力エラーがなければtrueを返す。
     */
    private boolean checkInput() {
        // check input Rank Name.
        if (txtRankItemGroup.getText().equals("")) {
            MessageDialog.showMessageDialog(this,
                    MessageUtil.getMessage(MessageUtil.ERROR_INPUT_EMPTY, "商品グループ名"),
                    this.getTitle(),
                    JOptionPane.ERROR_MESSAGE);
            txtRankItemGroup.requestFocusInWindow();
            return false;
        }

        return true;
    }

    /**
     * 入力されたデータを登録する。
     *
     * @param isAdd true - 追加処理
     * @return true - 成功
     */
    private boolean regist(boolean isAdd) {
        boolean result = false;
        MstRankItemGroup mr = new MstRankItemGroup();

        if (!isAdd && 0 <= selIndex) {
            mr.setItemGroupId(mrigs.get(selIndex).getItemGroupId());
        }
        
        mr.setItemGroupName(txtRankItemGroup.getText());
        
        ConnectionWrapper con = SystemInfo.getConnection();

        try {
            con.begin();

            if (mr.regist(con)) {
                con.commit();
                this.refresh();
                result = true;
            } else {
                con.rollback();
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return result;
    }

    /**
     * 商品Ｇ設定- delete  row.
     *
     * @return true - 成功
     */
    private boolean delete() {
        boolean result = false;
        MstRankItemGroup mr = null;
        if (0 <= selIndex && selIndex < mrigs.size()) {
            mr = mrigs.get(selIndex);
        }
        if(MessageDialog.showYesNoDialog(this,
					MessageUtil.getMessage(MessageUtil.CONFIRM_DELETE, mr.getItemGroupName()),
					this.getTitle(),
					JOptionPane.WARNING_MESSAGE) != 0)
        {
                return true;
        }
        ConnectionWrapper con = SystemInfo.getConnection();

        try {
            con.begin();

            if (mr.delete(con) && mr.deleteRankItemDetail(con)) {
                con.commit();
                this.refresh();
                result = true;
            } else {
                con.rollback();
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return result;
    }

    /**
     * 商品Ｇ設定RankItemGroupFocusTraversalPolicy
     */
    private class RankItemGroupFocusTraversalPolicy
            extends FocusTraversalPolicy {

        /**
         * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
         * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
         * @return aComponent のあとにフォーカスを受け取る Component。適切な Component が見つからない場合は
         * null
         */
        public Component getComponentAfter(Container aContainer,
                Component aComponent) {
            if (aComponent.equals(txtRankItemGroup)) {
                return txtRankItemGroup;
            } else if (aComponent.equals(txtRankItemGroup)) {
                return txtRankItemGroup;
            }

            return txtRankItemGroup;
        }

        /**
         * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
         * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
         * @return aComponent の前にフォーカスを受け取る Component。適切な Component が見つからない場合は
         * null
         */
        public Component getComponentBefore(Container aContainer,
                Component aComponent) {
            if (aComponent.equals(txtRankItemGroup)) {
                return txtRankItemGroup;
            } else if (aComponent.equals(txtRankItemGroup)) {
                return txtRankItemGroup;
            }

            return txtRankItemGroup;
        }

        /**
         * @param aContainer 先頭の Component
         * を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
         * @return aContainer のトラバーサルサイクルの先頭の Componet、または適切な Component
         * が見つからない場合は null
         */
        public Component getFirstComponent(Container aContainer) {
            return txtRankItemGroup;
        }

        /**
         * @param aContainer aContainer - 最後の Component
         * を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
         * @return aContainer のトラバーサルサイクルの最後の Componet、または適切な Component
         * が見つからない場合は null
         */
        public Component getLastComponent(Container aContainer) {
            return txtRankItemGroup;
        }

        /**
         * フォーカスを設定するデフォルトコンポーネントを返します。 aContainer
         * をルートとするフォーカストラバーサルサイクルが新しく開始されたときに、このコンポーネントに最初にフォーカスが設定されます。
         *
         * @param aContainer デフォルトの Component
         * を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
         * @return aContainer のトラバーサルサイクルのデフォルトの Componet、または適切な Component
         * が見つからない場合は null
         */
        public Component getDefaultComponent(Container aContainer) {
            return txtRankItemGroup;
        }

        /**
         * ウィンドウが最初に表示されたときにフォーカスが設定されるコンポーネントを返します。 show() または setVisible(true)
         * の呼び出しで一度ウィンドウが表示されると、 初期化コンポーネントはそれ以降使用されません。
         * 一度別のウィンドウに移ったフォーカスが再び設定された場合、 または、一度非表示状態になったウィンドウが再び表示された場合は、
         * そのウィンドウの最後にフォーカスが設定されたコンポーネントがフォーカス所有者になります。
         * このメソッドのデフォルト実装ではデフォルトコンポーネントを返します。
         *
         * @param window 初期コンポーネントが返されるウィンドウ
         * @return 最初にウィンドウが表示されるときにフォーカス設定されるコンポーネント。適切なコンポーネントがない場合は null
         */
        public Component getInitialComponent(Window window) {
            return txtRankItemGroup;
        }
    }

    /**
     * JTableの列幅を初期化する。
     */
    private void initTableColumnWidth() {
        //列の幅を設定する。
        tbRankItemGroup.getColumnModel().getColumn(0).setPreferredWidth(240);
        tbRankItemGroup.getColumnModel().getColumn(1).setPreferredWidth(50);
    }

    /**
     * 列の表示位置を設定するTableCellRenderer
     */
    private class TableCellAlignRenderer extends DefaultTableCellRenderer {

        /**
         * Creates a new instance of ReservationTableCellRenderer
         */
        public TableCellAlignRenderer() {
            super();
        }

        /**
         * テーブルセルレンダリングを返します。
         *
         * @param table JTable
         * @param value セルに割り当てる値
         * @param isSelected セルが選択されている場合は true
         * @param hasFocus フォーカスがある場合は true
         * @param row 行
         * @param column 列
         * @return テーブルセルレンダリング
         */
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);

            switch (column) {
                case 1:
                    super.setHorizontalAlignment(SwingConstants.RIGHT);
                    break;
                default:
                    super.setHorizontalAlignment(SwingConstants.LEFT);
                    break;
            }

            return this;
        }
    }
}
