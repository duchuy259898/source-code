/*
 * MstMailTemplateClassPanel.java
 *
 * Created on 2006/11/02, 16:19
 */

package com.geobeck.sosia.pos.mail;

import com.geobeck.sosia.pos.system.SystemInfo;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;
import java.sql.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.mail.*;
import com.geobeck.sosia.pos.swing.*;
import com.geobeck.sosia.pos.util.*;

/**
 *
 * @author  katagiri
 */
public class MstMailTemplateClassPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx
{
	
	/** Creates new form MstMailTemplateClassPanel */
	public MstMailTemplateClassPanel()
	{
		initComponents();
		this.setSize(438, 680);
		this.setPath("メール機能");
		this.setTitle("テンプレート分類登録");
		addMouseCursorChange();
		this.setListener();

                SystemInfo.initGroupShopComponents(target, 3);

		this.refresh();
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        deleteButton = new javax.swing.JButton();
        className = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)className.getDocument()).setDocumentFilter(
            new CustomFilter(20));
        renewButton = new javax.swing.JButton();
        datasScrollPane = new javax.swing.JScrollPane();
        datas = new javax.swing.JTable();
        dataNameLabel = new javax.swing.JLabel();
        addButton = new javax.swing.JButton();
        displaySeqLabel = new javax.swing.JLabel();
        displaySeq = new com.geobeck.swing.JFormattedTextFieldEx();
        ((PlainDocument)displaySeq.getDocument()).setDocumentFilter(
            new CustomFilter(4, CustomFilter.NUMERIC));
        targetLabel = new javax.swing.JLabel();
        target = new com.geobeck.sosia.pos.swing.JComboBoxLabel();

        setFocusCycleRoot(true);

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

        className.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        className.setColumns(20);
        className.setInputKanji(true);

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

        datasScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        datas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "分類名", "表示順"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        datas.setSelectionBackground(new java.awt.Color(220, 220, 220));
        datas.setSelectionForeground(new java.awt.Color(0, 0, 0));
        datas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        datas.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(datas, SystemInfo.getTableHeaderRenderer());
        datas.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        this.initTableColumnWidth();
        SelectTableCellRenderer.setSelectTableCellRenderer(datas);
        datas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                datasKeyReleased(evt);
            }
        });
        datas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                datasMouseReleased(evt);
            }
        });
        datasScrollPane.setViewportView(datas);

        dataNameLabel.setText("分類名");

        addButton.setIcon(SystemInfo.getImageIcon("/button/common/insert_off.jpg"));
        addButton.setBorderPainted(false);
        addButton.setContentAreaFilled(false);
        addButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/insert_on.jpg"));
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        displaySeqLabel.setText("挿入位置");

        displaySeq.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        displaySeq.setColumns(4);
        displaySeq.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        targetLabel.setText("対象");

        target.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        target.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                targetActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, datasScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(dataNameLabel)
                            .add(targetLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(target, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(className, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 70, Short.MAX_VALUE)
                        .add(displaySeqLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(displaySeq, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 68, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(addButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(renewButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(deleteButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(targetLabel)
                    .add(target, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dataNameLabel)
                    .add(className, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(displaySeqLabel)
                    .add(displaySeq, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(deleteButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(renewButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(addButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(datasScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

	private void addButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_addButtonActionPerformed
	{//GEN-HEADEREND:event_addButtonActionPerformed
		if(this.checkInput())
		{
			this.regist(true);
		}
	}//GEN-LAST:event_addButtonActionPerformed

	private void datasMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_datasMouseReleased
	{//GEN-HEADEREND:event_datasMouseReleased
		this.changeCurrentData();
	}//GEN-LAST:event_datasMouseReleased

	private void datasKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_datasKeyReleased
	{//GEN-HEADEREND:event_datasKeyReleased
		this.changeCurrentData();
	}//GEN-LAST:event_datasKeyReleased

	private void renewButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_renewButtonActionPerformed
	{//GEN-HEADEREND:event_renewButtonActionPerformed
		if(this.checkInput())
		{
			this.regist(false);
		}
	}//GEN-LAST:event_renewButtonActionPerformed

	private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteButtonActionPerformed
	{//GEN-HEADEREND:event_deleteButtonActionPerformed
		this.delete();
	}//GEN-LAST:event_deleteButtonActionPerformed

        private void targetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_targetActionPerformed
            this.refresh();
        }//GEN-LAST:event_targetActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private com.geobeck.swing.JFormattedTextFieldEx className;
    private javax.swing.JLabel dataNameLabel;
    private javax.swing.JTable datas;
    private javax.swing.JScrollPane datasScrollPane;
    private javax.swing.JButton deleteButton;
    private com.geobeck.swing.JFormattedTextFieldEx displaySeq;
    private javax.swing.JLabel displaySeqLabel;
    private javax.swing.JButton renewButton;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel target;
    private javax.swing.JLabel targetLabel;
    // End of variables declaration//GEN-END:variables
	
	private	MstMailTemplateClasses		mmtcs			=	new MstMailTemplateClasses();
	private	Integer						currentIndex	=	-1;
	
	private	MstMailTemplateClassFocusTraversalPolicy	ftp	=
			new MstMailTemplateClassFocusTraversalPolicy();
	
	/**
	 * テンプレート分類登録画面用FocusTraversalPolicyを取得する。
	 * @return  テンプレート分類登録画面用FocusTraversalPolicy
	 */
	public MstMailTemplateClassFocusTraversalPolicy getFocusTraversalPolicy()
	{
		return	ftp;
	}
	
	/**
	 * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
	 */
	private void addMouseCursorChange()
	{
		SystemInfo.addMouseCursorChange(addButton);
		SystemInfo.addMouseCursorChange(renewButton);
		SystemInfo.addMouseCursorChange(deleteButton);
	}
	
	/**
	 * コンポーネントの各リスナーをセットする。
	 */
	private void setListener()
	{
		className.addKeyListener(SystemInfo.getMoveNextField());
		className.addFocusListener(SystemInfo.getSelectText());
		displaySeq.addKeyListener(SystemInfo.getMoveNextField());
		displaySeq.addFocusListener(SystemInfo.getSelectText());
	}
	
	
	/**
	 * 入力項目をクリアする。
	 */
	private void clear()
	{
		className.setText("");
		displaySeq.setText("");
		if(0 < datas.getRowCount())
				datas.removeRowSelectionInterval(0, datas.getRowCount() - 1);
		currentIndex = -1;
		this.changeCurrentData();
	}
	
	/**
	 * 再表示を行う。
	 */
	private void refresh()
	{
		if(target.getSelectedItem() instanceof MstGroup)
		{
			MstGroup	selGroup	=	(MstGroup)target.getSelectedItem();
			mmtcs.setGroupID(selGroup.getGroupID());
			mmtcs.setShopID(0);
		}
		else if(target.getSelectedItem() instanceof MstShop)
		{
			MstShop	selShop	=	(MstShop)target.getSelectedItem();
			mmtcs.setShopID(selShop.getShopID());
			mmtcs.setGroupID(selShop.getGroupID());
		}
		
		try
		{
			//マスタをデータベースから読み込む
			mmtcs.load(SystemInfo.getConnection(), false);
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		//テーブルにマスタデータを表示する
		this.showTableData();
		//入力をクリアする
		this.clear();
	}
	
	
	/**
	 * テーブルにマスタデータを表示する。
	 */
	private void showTableData()
	{
		DefaultTableModel	model	=	(DefaultTableModel)datas.getModel();
		
		//全行削除
		model.setRowCount(0);
		datas.removeAll();
		
		for(MstMailTemplateClass mmtc : mmtcs)
		{
			Object[]	rowData	=	{	mmtc.getMailTemplateClassName(),
										mmtc.getDisplaySeq()	};
			model.addRow(rowData);
		}
	}
	
	/**
	 * 選択データが変更されたときの処理。
	 */
	private void changeCurrentData()
	{
		int	index	=	datas.getSelectedRow();
		
		if(0 <= index && index < mmtcs.size() && currentIndex != index)
		{
			currentIndex	=	index;
			//選択されているデータを表示
			this.showCurrentData();
		}
		
		//データが選択されている場合、更新・削除ボタンを使用可能にする
		renewButton.setEnabled(0 <= currentIndex);
		deleteButton.setEnabled(0 <= currentIndex);
	}
	
	/**
	 * 選択されているデータを表示する。
	 */
	private void showCurrentData()
	{
		className.setText(mmtcs.get(currentIndex).getMailTemplateClassName());
		displaySeq.setText(mmtcs.get(currentIndex).getDisplaySeq().toString());
	}
	
	/**
	 * 入力チェックを行う。
	 * @return true - ＯＫ
	 */
	private boolean checkInput()
	{
		//名称
		if(className.getText().equals(""))
		{
			return	false;
		}
		
		return	true;
	}
	
	/**
	 * 登録処理を行う。
	 * @param isInsert true - Insert
	 */
	private void regist(boolean isInsert)
	{
		MstMailTemplateClass	temp	=	new MstMailTemplateClass();
		
		if(target.getSelectedItem() instanceof MstGroup)
		{
			MstGroup	selGroup	=	(MstGroup)target.getSelectedItem();
			temp.setGroupID(selGroup.getGroupID());
			temp.setShopID(0);
		}
		else if(target.getSelectedItem() instanceof MstShop)
		{
			MstShop	selShop	=	(MstShop)target.getSelectedItem();
			temp.setShopID(selShop.getShopID());
			temp.setGroupID(selShop.getGroupID());
		}
		
		//行が選択されている場合、元のデータをセットする
		if(0 <= currentIndex && !isInsert)
		{
			temp.setMailTemplateClassID(mmtcs.get(currentIndex).getMailTemplateClassID());
			temp.setMailTemplateClassName(mmtcs.get(currentIndex).getMailTemplateClassName());
			temp.setDisplaySeq(mmtcs.get(currentIndex).getDisplaySeq());
		}
		
		//入力されたデータをセット
		temp.setMailTemplateClassName(className.getText());
		temp.setDisplaySeq((displaySeq.getText().equals("") ? -1 : Integer.parseInt(displaySeq.getText())));
		
		//データを登録
		ConnectionWrapper	con		=	SystemInfo.getConnection();
		
		try
		{
			con.begin();
			
			if(temp.regist(con, (isInsert ? -1 : currentIndex)))
			{
				con.commit();
				this.refresh();
			}
			else
			{
				con.rollback();
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * 削除処理を行う。
	 */
	private void delete()
	{
	    MstMailTemplateClass temp = mmtcs.get(currentIndex);
		
	    //削除確認
	    if (MessageDialog.showYesNoDialog(this,
			MessageUtil.getMessage(MessageUtil.CONFIRM_DELETE, temp.getMailTemplateClassName()),
			this.getTitle(),
			JOptionPane.WARNING_MESSAGE) != 0)
	    {
		return;
	    }

	    //データを登録
	    ConnectionWrapper con = SystemInfo.getConnection();

	    try
	    {
		con.begin();

		if(temp.delete(con)) {
		    con.commit();
		} else {
		    con.rollback();
		}
	    }
	    catch(SQLException e)
	    {
		SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
	    }
	    
	    refresh();
	}
	
	
	/**
	 * テンプレート分類登録画面用FocusTraversalPolicy
	 */
	private class MstMailTemplateClassFocusTraversalPolicy
					extends FocusTraversalPolicy
	{
		/**
		 * aComponent のあとでフォーカスを受け取る Component を返します。
		 * aContainer は aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダでなければなりません。
		 * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
		 * @return aComponent のあとにフォーカスを受け取る Component。適切な Component が見つからない場合は null
		 */
		public Component getComponentAfter(Container aContainer,
										   Component aComponent)
		{
			if (aComponent.equals(target))
			{
				return displaySeq;
			}
			else if (aComponent.equals(className))
			{
				return displaySeq;
			}
			else if (aComponent.equals(displaySeq))
			{
				return className;
			}
			
			return getDefaultComponent();
		}

		/**
		 * aComponent の前にフォーカスを受け取る Component を返します。
		 * aContainer は aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダでなければなりません。
		 * @param aContainer aComponent のフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @param aComponent aContainer のおそらく間接的な子、または aContainer 自体
		 * @return aComponent の前にフォーカスを受け取る Component。適切な Component が見つからない場合は null
		 */
		public Component getComponentBefore(Container aContainer,
											Component aComponent)
		{
			if (aComponent.equals(target))
			{
				return displaySeq;
			}
			else if (aComponent.equals(className))
			{
				return target;
			}
			else if (aComponent.equals(displaySeq))
			{
				return className;
			}
			
			return getDefaultComponent();
		}

		/**
		 * トラバーサルサイクルの最初の Component を返します。
		 * このメソッドは、順方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer 先頭の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの先頭の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getFirstComponent(Container aContainer)
		{
			return getDefaultComponent();
		}

		/**
		 * トラバーサルサイクルの最後の Component を返します。
		 * このメソッドは、逆方向のトラバーサルがラップするときに、次にフォーカスする Component を判定するために使用します。
		 * @param aContainer aContainer - 最後の Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルの最後の Componet、または適切な Component が見つからない場合は null
		 */
		public Component getLastComponent(Container aContainer)
		{
			return displaySeq;
		}

		/**
		 * フォーカスを設定するデフォルトコンポーネントを返します。
		 * aContainer をルートとするフォーカストラバーサルサイクルが新しく開始されたときに、このコンポーネントに最初にフォーカスが設定されます。
		 * @param aContainer デフォルトの Component を返すフォーカスサイクルのルートまたはフォーカストラバーサルポリシープロバイダ
		 * @return aContainer のトラバーサルサイクルのデフォルトの Componet、または適切な Component が見つからない場合は null
		 */
		public Component getDefaultComponent(Container aContainer)
		{
			return getDefaultComponent();
		}
		
		/**
		 * ウィンドウが最初に表示されたときにフォーカスが設定されるコンポーネントを返します。
		 * show() または setVisible(true) の呼び出しで一度ウィンドウが表示されると、
		 * 初期化コンポーネントはそれ以降使用されません。
		 * 一度別のウィンドウに移ったフォーカスが再び設定された場合、
		 * または、一度非表示状態になったウィンドウが再び表示された場合は、
		 * そのウィンドウの最後にフォーカスが設定されたコンポーネントがフォーカス所有者になります。
		 * このメソッドのデフォルト実装ではデフォルトコンポーネントを返します。
		 * @param window 初期コンポーネントが返されるウィンドウ
		 * @return 最初にウィンドウが表示されるときにフォーカス設定されるコンポーネント。適切なコンポーネントがない場合は null
		 */
		public Component getInitialComponent(Window window)
		{
			return getDefaultComponent();
		}
		
		public Component getDefaultComponent()
		{
			if(1 < target.getItemCount())
			{
				return	target;
			}
			else
			{
				return className;
			}
		}
	}
	/**
	 * JTableの列幅を初期化する。
	 */
	private void initTableColumnWidth()
	{
		//列の幅を設定する。
		datas.getColumnModel().getColumn(0).setPreferredWidth(240);
		datas.getColumnModel().getColumn(1).setPreferredWidth(50);
	}
	
	
	/**
	 * 列の表示位置を設定するTableCellRenderer
	 */
	private class TableCellAlignRenderer extends DefaultTableCellRenderer
	{
		/** Creates a new instance of ReservationTableCellRenderer */
		public TableCellAlignRenderer()
		{
			super();
		}

		/**
		 * テーブルセルレンダリングを返します。
		 * @param table JTable
		 * @param value セルに割り当てる値
		 * @param isSelected セルが選択されている場合は true
		 * @param hasFocus フォーカスがある場合は true
		 * @param row 行
		 * @param column 列
		 * @return テーブルセルレンダリング
		 */
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, column);
			
			switch(column)
			{
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
