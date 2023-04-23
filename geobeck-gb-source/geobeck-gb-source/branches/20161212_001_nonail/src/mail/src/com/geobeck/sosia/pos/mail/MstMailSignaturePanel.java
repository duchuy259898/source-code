/*
 * MstMailSignaturePanel.java
 *
 * Created on 2006/11/02, 14:22
 */

package com.geobeck.sosia.pos.mail;

import java.sql.*;
import java.awt.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.mail.*;
import com.geobeck.sosia.pos.system.*;

/**
 *
 * @author  katagiri
 */
public class MstMailSignaturePanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx
{
	
	/**
	 * Creates new form MstMailSignaturePanel
	 */
	public MstMailSignaturePanel()
	{
		initComponents();
		this.setSize(384, 348);
		this.setPath("メール機能");
		this.setTitle("署名登録");
		this.init();
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        signatureGroup = new javax.swing.ButtonGroup();
        signature1 = new javax.swing.JRadioButton();
        signature2 = new javax.swing.JRadioButton();
        signature3 = new javax.swing.JRadioButton();
        signatureBodyScrollPane = new javax.swing.JScrollPane();
        signatureBody = new javax.swing.JTextArea();
        renewButton = new javax.swing.JButton();
        targetLabel = new javax.swing.JLabel();
        target = new com.geobeck.sosia.pos.swing.JComboBoxLabel();

        setFocusCycleRoot(true);
        setFocusTraversalPolicy(getFocusTraversalPolicy());
        signatureGroup.add(signature1);
        signature1.setSelected(true);
        signature1.setText("\u7f72\u540d\uff11");
        signature1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        signature1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        signature1.setOpaque(false);

        signatureGroup.add(signature2);
        signature2.setText("\u7f72\u540d\uff12");
        signature2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        signature2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        signature2.setOpaque(false);

        signatureGroup.add(signature3);
        signature3.setText("\u7f72\u540d\uff13");
        signature3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        signature3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        signature3.setOpaque(false);

        signatureBody.setColumns(20);
        signatureBody.setRows(5);
        signatureBodyScrollPane.setViewportView(signatureBody);

        renewButton.setIcon(SystemInfo.getImageIcon("/button/common/update_off.jpg"));
        renewButton.setBorderPainted(false);
        renewButton.setContentAreaFilled(false);
        renewButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/update_on.jpg"));
        renewButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                renewButtonActionPerformed(evt);
            }
        });

        targetLabel.setText("\u5bfe\u8c61");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, signatureBodyScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(signature1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(signature2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(signature3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 43, Short.MAX_VALUE)
                        .add(renewButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(targetLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(target, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 304, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {signature1, signature2, signature3}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(targetLabel)
                    .add(target, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(signature2)
                    .add(signature3)
                    .add(signature1)
                    .add(renewButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(signatureBodyScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 281, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

	private void renewButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_renewButtonActionPerformed
	{//GEN-HEADEREND:event_renewButtonActionPerformed
		this.regist();
	}//GEN-LAST:event_renewButtonActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton renewButton;
    private javax.swing.JRadioButton signature1;
    private javax.swing.JRadioButton signature2;
    private javax.swing.JRadioButton signature3;
    private javax.swing.JTextArea signatureBody;
    private javax.swing.JScrollPane signatureBodyScrollPane;
    private javax.swing.ButtonGroup signatureGroup;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel target;
    private javax.swing.JLabel targetLabel;
    // End of variables declaration//GEN-END:variables
	
	private	MstMailSignature	currentSignature	=	null;
	
	/**
	 * 署名登録画面用FocusTraversalPolicy
	 */
	private	MstMailSignatureFocusTraversalPolicy	ftp	=
			new MstMailSignatureFocusTraversalPolicy();
	
	/**
	 * 署名登録画面用FocusTraversalPolicyを取得する。
	 * @return 署名登録画面用FocusTraversalPolicy
	 */
	public MstMailSignatureFocusTraversalPolicy getFocusTraversalPolicy()
	{
		return	ftp;
	}
	
	public MstMailSignature getCurrentSignature()
	{
		if(currentSignature == null)
		{
			currentSignature	=	new MstMailSignature();
		}
		return currentSignature;
	}

	public void setCurrentSignature(MstMailSignature currentSignature)
	{
		this.currentSignature = currentSignature;
	}
	
	private void init()
	{
//		SystemInfo.initGroupShopComponents(targetLabel, target, 3);
		target.addItem((SystemInfo.getCurrentShop().getShopID() == 0
				? SystemInfo.getGroup()
				: SystemInfo.getCurrentShop()));
		
		this.addSignatureChangeListener(signature1);
		this.addSignatureChangeListener(signature2);
		this.addSignatureChangeListener(signature3);
		
		this.changeCurrentSignature();
	}
	
	private void addSignatureChangeListener(javax.swing.JRadioButton sigRadio)
	{
        sigRadio.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                changeCurrentSignature();
            }
        });
	}
	
	private void changeCurrentSignature()
	{
		getCurrentSignature().setMailSignaturBody("");
		
		if(target.getSelectedItem() instanceof MstGroup)
		{
			MstGroup	selGroup	=	(MstGroup)target.getSelectedItem();
			getCurrentSignature().setGroupID(selGroup.getGroupID());
			getCurrentSignature().setShopID(0);
		}
		else if(target.getSelectedItem() instanceof MstShop)
		{
			MstShop	selShop	=	(MstShop)target.getSelectedItem();
			getCurrentSignature().setShopID(selShop.getShopID());
			getCurrentSignature().setGroupID(selShop.getGroupID());
		}
		
		getCurrentSignature().setMailSignaturID(this.getSelectedSignatureID());
		
		try
		{
			getCurrentSignature().load(SystemInfo.getConnection());
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		signatureBody.setText(getCurrentSignature().getMailSignaturBody());
		
		signatureBody.updateUI();
	}
	
	private Integer getSelectedSignatureID()
	{
		if(signature1.isSelected())
		{
			return	1;
		}
		else if(signature2.isSelected())
		{
			return	2;
		}
		else if(signature3.isSelected())
		{
			return	3;
		}
		
		return	0;
	}
	
	private void regist()
	{
		getCurrentSignature().setMailSignaturBody(signatureBody.getText());
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			try
			{
				con.begin();

				if(getCurrentSignature().regist(con))
				{
					con.commit();
				}
				else
				{
					con.rollback();
				}
			}
			catch(SQLException e)
			{
				con.rollback();
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	
	/**
	 * 署名登録画面用FocusTraversalPolicy
	 */
	private class MstMailSignatureFocusTraversalPolicy
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
				return getSelectedSignature();
			}
			else if (aComponent.equals(signature1))
			{
				return signatureBody;
			}
			else if (aComponent.equals(signature2))
			{
				return signatureBody;
			}
			else if (aComponent.equals(signature3))
			{
				return signatureBody;
			}
			else if(aComponent.equals(signatureBody))
			{
				return	target;
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
				return signatureBody;
			}
			else if (aComponent.equals(signature1))
			{
				return target;
			}
			else if (aComponent.equals(signature2))
			{
				return target;
			}
			else if (aComponent.equals(signature3))
			{
				return target;
			}
			else if(aComponent.equals(signatureBody))
			{
				return	getSelectedSignature();
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
			return signatureBody;
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
				return getSelectedSignature();
			}
		}
		
		public Component getSelectedSignature()
		{
			if(signature1.isSelected())
			{
				return	signature1;
			}
			else if(signature2.isSelected())
			{
				return	signature2;
			}
			else
			{
				return	signature3;
			}
		}
	}
}
