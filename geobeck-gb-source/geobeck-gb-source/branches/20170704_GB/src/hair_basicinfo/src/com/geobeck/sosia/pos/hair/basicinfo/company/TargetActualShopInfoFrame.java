/*
 * TargetActualShopInfoFrame.java
 *
 * Created on 2008/09/19, 13:52
 */

package com.geobeck.sosia.pos.hair.basicinfo.company;

import com.geobeck.sosia.pos.master.company.MstShop;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.WindowEvent;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.swing.MessageDialog;
import com.geobeck.sosia.pos.util.MessageUtil;

/**
 *
 * @author  shiera.delusa
 */
public class TargetActualShopInfoFrame extends JFrame
{
    private final String FRAME_TITLE = "�ڕW �� ���� �� �ғ�����";    
    
    //constants:
    private final static int VERIFY_SAVE_DATA = 13000;    
	private ArrayList<Integer> arrYear;
    private int nCurrentEditingYear;
      
	/** Creates new form TargetActualShopInfoFrame */
	public TargetActualShopInfoFrame()
	{
		this.setTitle( FRAME_TITLE );
		initComponents();

                SystemInfo.initGroupShopComponents(cblShop, 2);
		cblShop.setSelectedIndex(0);

		this.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
                this.setLocationRelativeTo( null );

		this.initYearComboBox( getSelectedShopID() );
		this.setCurrentEditingYear();
		this.tablePanel.loadYearData( getSelectedShopID(), getCurrentEditingYear());
	}
    
    /**
     * OVERRIDING processWindowEvent()
     */
    protected void processWindowEvent(WindowEvent e) {
        if( e.getID() == WindowEvent.WINDOW_CLOSING )
        {   
            this.prepareCloseWindow();
        }
        super.processWindowEvent(e);
    }

    private void prepareCloseWindow()
    {
        if( this.tablePanel.isCellEdited() )
        {
            if( displayMessage( VERIFY_SAVE_DATA ) == JOptionPane.OK_OPTION )
            {
                this.tablePanel.registerAllDataToDB( getSelectedShopID(), getCurrentEditingYear());
                this.tablePanel.resetCellEditedFlag();
            }
        }
    }
    
    private void initYearComboBox(int nShopId)
    {
		arrYear = DataTargetResultDAO.getExistingYear(nShopId);

		this.yearComboBox.removeAllItems();
		
		for (int i = 0; i < arrYear.size(); ++i)
		{
			this.yearComboBox.addItem(arrYear.get(i) + "�N�x");
		}

		this.yearComboBox.setSelectedIndex(0);
    }
    
    private int displayMessage( int messageType )
    {
        int userSelection = JOptionPane.CANCEL_OPTION;
        
        switch( messageType )
        {
            case VERIFY_SAVE_DATA:
            {
                userSelection = MessageDialog.showYesNoDialog( this, 
                        MessageUtil.getMessage( VERIFY_SAVE_DATA ), 
                    this.FRAME_TITLE, JOptionPane.OK_CANCEL_OPTION );
                break;
            }
            default:
                break;
        }
        
        return userSelection;
    }

    /**
     * �I������Ă���X�܂��擾����B
     * @return �I������Ă���X��
     */
    private MstShop getSelectedShop() {
        if(0 <= cblShop.getSelectedIndex())
            return	(MstShop)cblShop.getSelectedItem();
        else
            return	null;
    }

    /**
     * �I������Ă���X�܂�ID���擾����B
     * @return �I������Ă���X�܂�ID
     */
    private Integer getSelectedShopID() {
        MstShop	ms	=	this.getSelectedShop();
        
        if(ms != null)
            return	ms.getShopID();
        else
            return	0;
    }

	private int getCurrentEditingYear()
	{
        return nCurrentEditingYear;
	}

	private void setCurrentEditingYear()
	{
		nCurrentEditingYear = this.arrYear.get(this.yearComboBox.getSelectedIndex());
	}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        menuPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        yearComboBox = new javax.swing.JComboBox();
        displayButton = new javax.swing.JButton();
        registerButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        cblShop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        shopLabel = new javax.swing.JLabel();
        tablePanel = new com.geobeck.sosia.pos.hair.basicinfo.company.TargetActualInfoTablePanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getAccessibleContext().setAccessibleName("990");
        getAccessibleContext().setAccessibleDescription("750");
        menuPanel.setPreferredSize(new java.awt.Dimension(980, 40));
        jLabel1.setText("\u8868\u793a\u5e74\u5ea6");

        displayButton.setIcon(SystemInfo.getImageIcon( "/button/common/show_off.jpg" ));
        displayButton.setBorder(null);
        displayButton.setBorderPainted(false);
        displayButton.setIconTextGap(0);
        displayButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        displayButton.setPressedIcon(SystemInfo.getImageIcon( "/button/common/show_on.jpg" ));
        displayButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                displayButtonActionPerformed(evt);
            }
        });

        registerButton.setIcon(SystemInfo.getImageIcon( "/button/common/regist_off.jpg" ));
        registerButton.setText("\n");
        registerButton.setBorder(null);
        registerButton.setBorderPainted(false);
        registerButton.setIconTextGap(0);
        registerButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        registerButton.setPressedIcon(SystemInfo.getImageIcon( "/button/common/regist_on.jpg" ));
        registerButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                registerButtonActionPerformed(evt);
            }
        });

        closeButton.setIcon(SystemInfo.getImageIcon( "/button/common/close_off.jpg" ));
        closeButton.setBorder(null);
        closeButton.setBorderPainted(false);
        closeButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        closeButton.setPressedIcon(SystemInfo.getImageIcon( "/button/common/close_on.jpg" ));
        closeButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                closeButtonActionPerformed(evt);
            }
        });

        cblShop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cblShop.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cblShopActionPerformed(evt);
            }
        });

        shopLabel.setText("\u5e97\u8217");

        javax.swing.GroupLayout menuPanelLayout = new javax.swing.GroupLayout(menuPanel);
        menuPanel.setLayout(menuPanelLayout);
        menuPanelLayout.setHorizontalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(shopLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menuPanelLayout.createSequentialGroup()
                        .addGap(123, 123, 123)
                        .addComponent(displayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(menuPanelLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cblShop, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(yearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(20, 20, 20)
                .addComponent(registerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 498, Short.MAX_VALUE)
                .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        menuPanelLayout.setVerticalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cblShop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(shopLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(displayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(registerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(yearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 985, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(menuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 989, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(menuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(110, Short.MAX_VALUE))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void cblShopActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cblShopActionPerformed
	{//GEN-HEADEREND:event_cblShopActionPerformed
		this.initYearComboBox(getSelectedShopID());
	}//GEN-LAST:event_cblShopActionPerformed

    private void displayButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_displayButtonActionPerformed
    {//GEN-HEADEREND:event_displayButtonActionPerformed
        if( this.tablePanel.isCellEdited() )
        {
            if( displayMessage( VERIFY_SAVE_DATA ) == JOptionPane.OK_OPTION )
            {
                this.tablePanel.registerAllDataToDB( getSelectedShopID(), getCurrentEditingYear() );
            }
            this.tablePanel.resetCellEditedFlag();
        }
		
		setCurrentEditingYear();
        this.tablePanel.loadYearData( getSelectedShopID(), getCurrentEditingYear() );
    }//GEN-LAST:event_displayButtonActionPerformed

    private void registerButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_registerButtonActionPerformed
    {//GEN-HEADEREND:event_registerButtonActionPerformed
        this.tablePanel.registerAllDataToDB( getSelectedShopID(), getCurrentEditingYear() );
        this.tablePanel.resetCellEditedFlag();
		setCurrentEditingYear();
    }//GEN-LAST:event_registerButtonActionPerformed

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_closeButtonActionPerformed
    {//GEN-HEADEREND:event_closeButtonActionPerformed
        this.prepareCloseWindow();
        this.dispose();
    }//GEN-LAST:event_closeButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.geobeck.sosia.pos.swing.JComboBoxLabel cblShop;
    private javax.swing.JButton closeButton;
    private javax.swing.JButton displayButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JButton registerButton;
    private javax.swing.JLabel shopLabel;
    private com.geobeck.sosia.pos.hair.basicinfo.company.TargetActualInfoTablePanel tablePanel;
    private javax.swing.JComboBox yearComboBox;
    // End of variables declaration//GEN-END:variables
    
}