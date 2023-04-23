/*
 * SearchProposal.java
 *
 * Created on 2015/11/23, 14:13
 */

package com.geobeck.sosia.pos.search.customer;

import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.hair.customer.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.swing.*;

/**
 * @author lvtu
 */
public class SearchProposal extends javax.swing.JDialog
{
	/**
	 * 店舗ＩＤ
	 */
	private MstShop     Shop            = null;
        
        private Integer     customerID      = null;
        
        private Integer     slipNo          = null;
        
        ArrayList<DataProposal> arrProposal = new  ArrayList<DataProposal>();
        
        DataProposal selectedProposal       = null;
        
        DataProposal referProposal       = null;

        public DataProposal getSelectedProposal() {
            return selectedProposal;
        }

        public void setSelectedProposal(DataProposal selectedProposal) {
            this.selectedProposal = selectedProposal;
        }
	
	/**
	 * コンストラクタ
	 * @param parent 
	 * @param modal 
	 */
	
	public SearchProposal(java.awt.Frame parent, boolean modal, Integer customerID, Integer shopID, Integer slipNo, DataProposal dtProposal)
	{       
            super(parent, modal);
            this.customerID = customerID;
            if ( shopID == null) {
                this.Shop = SystemInfo.getCurrentShop();
            }else {
                this.Shop = new MstShop();
                this.Shop.setShopID(shopID);
            }
            if (slipNo!= null && slipNo > 0) {
                this.slipNo = slipNo;
            }
            if ( dtProposal != null) {
                referProposal = dtProposal;
            }
            init();
            selectedProposal = new DataProposal();
	}
    
	
        
        // 共通初期化処理
        private void init() {
            initComponents();
            addMouseCursorChange();
            SwingUtil.moveCenter(this);
            searchProposal();
        }
        

        /**
	 * ユーザ検索ボタンを取得する
	 */
	private JButton getUserSearchButton(final DataProposal dtProposal)
	{
		JButton		searchButton	=	new JButton();
		searchButton.setBorderPainted(false);
		searchButton.setContentAreaFilled(false);
		searchButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/common/select_small_off.jpg")));
		searchButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/" + SystemInfo.getSkinPackage() + "/button/common/select_small_on.jpg")));
		searchButton.setSize(48, 25);
		searchButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
                            getDataProposalDetail(dtProposal);
                        }
		});
                // searchButton disenabled when data exists from screen okake
                if ( this.referProposal != null && this.referProposal.getProposalID() != null
                        && this.referProposal.getProposalID().equals(dtProposal.getProposalID())) {
                    searchButton.setEnabled(false);
                }
		return searchButton;
	}   
        
        private void getDataProposalDetail(DataProposal dtProposal){
            ConnectionWrapper con = SystemInfo.getConnection();

	    try {

                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                
               
		ResultSetWrapper rs = con.executeQuery(getSelectSQL(dtProposal));
                
                DataProposalDetail dtProposalDetail ;
		while (rs.next()) {
                    dtProposalDetail = new DataProposalDetail();
                    dtProposalDetail.setData(rs);
                    
                    selectedProposal.add(dtProposalDetail);
                    selectedProposal.setProposalID(dtProposalDetail.getProposalID());
		}

		rs.close();
                this.setVisible(false);

	    } catch(SQLException e) {
                
		SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                
            } finally {
                
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	    }
        }
        
        private String getSelectSQL(DataProposal dtProposal) {
            return	"select dpd.*,\n" +
                            "case when dpd.product_division = 2 then mic.item_class_id else mcc.course_class_id end as product_class_id,\n" +
                            "case when dpd.product_division = 2 then mic.item_class_name else mcc.course_class_name end as product_class_name,\n" +
                            "case when dpd.product_division = 2 then mi.item_name else mc.course_name end as product_name\n" +
                            "from data_proposal_detail dpd \n" +
                            "left join mst_item mi on mi.item_id = dpd.product_id\n" +
                            "left join mst_item_class mic on mic.item_class_id = mi.item_class_id\n" +
                            "left join mst_course mc on dpd.product_id = mc.course_id\n" +
                            "left join mst_course_class mcc on mcc.course_class_id = mc.course_class_id\n" +
                            "where dpd.shop_id = " + SQLUtil.convertForSQL(dtProposal.getShopID()) + "\n" +
                            "and dpd.proposal_id = " + SQLUtil.convertForSQL(dtProposal.getProposalID()) + "\n" +
                            "and dpd.delete_date is null" ;
        }
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backPanel = new com.geobeck.swing.ImagePanel();
        searchResultScrollPane = new javax.swing.JScrollPane();
        searchResult = new com.geobeck.swing.JTableEx();
        backButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setTitle("提案書一覧");
        setName("searchProposalFrame"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        backPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        backPanel.setImage(SystemInfo.getImageIcon("/contents_background.jpg"));
        backPanel.setRepeat(true);

        searchResultScrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        searchResultScrollPane.setFocusable(false);

        searchResult.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "作成日", "作成者", "提案書名", "コース金額", "商品金額", "合計金額", "有効期限", "選択"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        searchResult.setSelectionBackground(new java.awt.Color(255, 210, 142));
        searchResult.setSelectionForeground(new java.awt.Color(0, 0, 0));
        searchResult.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchResult.getTableHeader().setReorderingAllowed(false);
        SwingUtil.setJTableHeaderRenderer(searchResult, SystemInfo.getTableHeaderRenderer());
        searchResult.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT);
        this.initTableColumnWidth();
        SelectTableCellRenderer.setSelectTableCellRenderer(searchResult);
        searchResult.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchResultMouseClicked(evt);
            }
        });
        searchResultScrollPane.setViewportView(searchResult);

        backButton.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
        backButton.setBorderPainted(false);
        backButton.setFocusable(false);
        backButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backPrevious(evt);
            }
        });

        jLabel1.setText("＜提案書一覧＞");

        org.jdesktop.layout.GroupLayout backPanelLayout = new org.jdesktop.layout.GroupLayout(backPanel);
        backPanel.setLayout(backPanelLayout);
        backPanelLayout.setHorizontalGroup(
            backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(searchResultScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 629, Short.MAX_VALUE)
            .add(backPanelLayout.createSequentialGroup()
                .add(1, 1, 1)
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 98, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(backButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        backPanelLayout.setVerticalGroup(
            backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(backPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(backPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(backButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(searchResultScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 300, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(backPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(backPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
      
    }//GEN-LAST:event_formWindowOpened

	private void searchResultMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_searchResultMouseClicked
	{//GEN-HEADEREND:event_searchResultMouseClicked

	}//GEN-LAST:event_searchResultMouseClicked

	private void backPrevious(java.awt.event.ActionEvent evt)//GEN-FIRST:event_backPrevious
	{//GEN-HEADEREND:event_backPrevious
		this.setVisible(false);
	}//GEN-LAST:event_backPrevious
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private com.geobeck.swing.ImagePanel backPanel;
    private javax.swing.JLabel jLabel1;
    private com.geobeck.swing.JTableEx searchResult;
    private javax.swing.JScrollPane searchResultScrollPane;
    // End of variables declaration//GEN-END:variables
	
	
	/**
	 * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
	 */
	private void addMouseCursorChange()
	{  
		SystemInfo.addMouseCursorChange(backButton);
	}
	
	
	/**
	 * 検索ボタンを押したときの処理を行う。
	 */
	private void searchProposal()
	{
	    if (searchResult.getCellEditor() != null) {
		searchResult.getCellEditor().stopCellEditing();
	    }
	    
	    SwingUtil.clearTable(searchResult);
                
	    DefaultTableModel model = (DefaultTableModel)searchResult.getModel();
	    java.util.Date dt = SystemInfo.getSystemDate();
	    //コネクションを取得
	    ConnectionWrapper con = SystemInfo.getConnection();

	    try {

                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                
                StringBuilder sql = new StringBuilder(1000);
                sql.append("	SELECT dp.proposal_date,  \n"); 
                sql.append("		   dp.shop_id,  \n");
                sql.append("		   staff.staff_id,  \n");
                sql.append("		   staff.staff_name1,  \n");
                sql.append("		   staff.staff_name2,  \n");
                sql.append("		   dp.proposal_id,  \n");
                sql.append("		   dp.proposal_name,  \n");
                sql.append("		   sum(CASE WHEN dpd.product_division = 2 THEN (dpd.product_value * dpd.product_num) ELSE 0 END) AS item_value,  \n");
                sql.append("		   sum(CASE WHEN dpd.product_division = 5 THEN (dpd.product_value) ELSE 0 END) AS course_value,  \n");
                sql.append("		   dp.proposal_valid_date,  \n");
                sql.append("		   dp.contract_shop_id,  \n");
                sql.append("		   ms.shop_name,  \n");
                sql.append("		   dp.slip_no,  \n");
                sql.append("		   customer_id ,  \n");
                sql.append("		   proposal_memo  \n");
                sql.append("	FROM data_proposal dp  \n");
                sql.append("	INNER JOIN data_proposal_detail dpd ON dpd.shop_id = dp.shop_id  \n");
                sql.append("	AND dpd.proposal_id = dp.proposal_id  \n");
                sql.append("	INNER JOIN mst_shop ms_shop ON ms_shop.shop_id = dp.shop_id  \n");
                sql.append("	INNER JOIN mst_staff staff ON staff.staff_id = dp.staff_id  \n");
                sql.append("	LEFT JOIN mst_item mi ON mi.item_id = dpd.product_id  \n");
                sql.append("	LEFT JOIN mst_item_class mic ON mic.item_class_id = mi.item_class_id  \n");
                sql.append("	LEFT JOIN mst_course mc ON dpd.product_id = mc.course_id  \n");
                sql.append("	LEFT JOIN mst_course_class mcc ON mcc.course_class_id = mc.course_class_id  \n");
                sql.append("	LEFT JOIN mst_shop ms ON ms.shop_id = dp.contract_shop_id  \n");
                sql.append("	WHERE dp.customer_id =" + SQLUtil.convertForSQL(this.customerID));
                sql.append("	  AND ms_shop.shop_id =" + SQLUtil.convertForSQL(this.Shop.getShopID()));
                sql.append("	  AND (dp.slip_no IS NULL  \n");
                if ( this.slipNo != null) {
                    sql.append("	  OR dp.slip_no = " + SQLUtil.convertForSQL(this.slipNo)  +" \n");
                }
                sql.append("	  ) \n");
                sql.append("	  AND dp.proposal_valid_date >= date " + SQLUtil.convertForSQL(dt) + " \n");
                sql.append("	  AND dp.delete_date IS NULL  \n");
                sql.append("	  AND dpd.delete_date IS NULL  \n");
                sql.append("	GROUP BY dp.proposal_date,  \n");
                sql.append("			 dp.shop_id, \n");
                sql.append("			 staff.staff_id,  \n");
                sql.append("			 staff.staff_name1,  \n");
                sql.append("			 staff.staff_name2,  \n");
                sql.append("			 dp.proposal_id,  \n");
                sql.append("			 dp.proposal_name,  \n");
                sql.append("			 dp.proposal_valid_date,  \n");
                sql.append("                     dp.contract_shop_id,  \n");
                sql.append("			 ms.shop_name,  \n");
                sql.append("			 dp.slip_no,  \n");
                sql.append("                     customer_id ,  \n");
                sql.append("                     proposal_memo  \n");
                sql.append("	ORDER BY dp.proposal_date  \n");
               
		ResultSetWrapper rs = con.executeQuery(sql.toString());
                
                DataProposal dtProposal = null;
                Double itemValue    = 0d;
                Double courseValue  = 0d;
                Double totalValue   = 0d;
		while (rs.next()) {
                    dtProposal = new DataProposal();
                    dtProposal.setData(rs);
                    this.arrProposal.add(dtProposal);
                
                    itemValue = rs.getDouble("item_value");
                    courseValue = rs.getDouble("course_value");
                    totalValue = itemValue + courseValue;
                    
		    Object[] rowData = { DateUtil.format(dtProposal.getProposalDate(), "yyyy/MM/dd"),
					 (rs.getString("staff_name1") == null ? "": rs.getString("staff_name1") + " ") + (rs.getString("staff_name2") == null ? "" :rs.getString("staff_name2")), 
					 dtProposal.getProposalName(),
					 courseValue.longValue(),
					 itemValue.longValue(),
					 totalValue.longValue(),
					 DateUtil.format(dtProposal.getProposalValidDate(), "yyyy/MM/dd"),
                                        getUserSearchButton(dtProposal)};
		    model.addRow(rowData);
		}

		rs.close();

                searchResult.requestFocusInWindow();
                searchResult.changeSelection(0, 1, false, false);
                
	    } catch(SQLException e) {
                
		SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                
            } finally {
                
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	    }

	}

	private void initTableColumnWidth()
	{
		//列の幅を設定する。
		searchResult.getColumnModel().getColumn(0).setPreferredWidth(70);
		searchResult.getColumnModel().getColumn(1).setPreferredWidth(70);
		searchResult.getColumnModel().getColumn(2).setPreferredWidth(100);
		searchResult.getColumnModel().getColumn(3).setPreferredWidth(70);
		searchResult.getColumnModel().getColumn(4).setPreferredWidth(70);
		searchResult.getColumnModel().getColumn(5).setPreferredWidth(70);
		searchResult.getColumnModel().getColumn(6).setPreferredWidth(70);
		searchResult.getColumnModel().getColumn(7).setPreferredWidth(50);
	}

}
