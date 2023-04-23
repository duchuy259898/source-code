/*
 * EnvironmentalSettingPanel.java
 *
 * Created on 2007/02/09, 16:42
 */

package com.geobeck.sosia.pos.basicinfo;

import javax.swing.*;

import com.geobeck.barcode.*;
import com.geobeck.swing.*;
import com.geobeck.sosia.pos.master.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.util.*;

/**
 *
 * @author  katagiri
 */
public class EnvironmentalSettingPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx
{
	
	/** Creates new form EnvironmentalSettingPanel */
	public EnvironmentalSettingPanel()
	{
		super();
		es = new EnvironmentalSetting();
		initComponents();
		this.init();
		this.setPath("��{�ݒ�");
		this.setTitle("���ݒ�");
		this.setSize(405, 500);
                
                // �o�[�R�[�h�ݒ���\���ɂ���
                useBarcode.setVisible(false);
                notUseBarcode.setVisible(false);
                if (!SystemInfo.isUseBarcodeReader()) {
                    barcodeLabel.setVisible(false);
                    serialPortLabel.setVisible(false);
                    serialPort.setVisible(false);
                }
                
                // �|�C���g�V�X�e���ݒ���\���ɂ���
                pointLabel.setVisible(false);
                notUsePointCard.setVisible(false);
                usePointCard.setVisible(false);
                pointOutputCard.setVisible(false);
                pointOutputReceipt.setVisible(false);

	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        barcodeGroup = new javax.swing.ButtonGroup();
        pointcardGroup = new javax.swing.ButtonGroup();
        ctiGroup = new javax.swing.ButtonGroup();
        pointOutputGroup = new javax.swing.ButtonGroup();
        skinLabel = new javax.swing.JLabel();
        skin = new javax.swing.JComboBox();
        registButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        notUseCti = new javax.swing.JRadioButton();
        useCti = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        serialPortCti = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        pointLabel = new javax.swing.JLabel();
        notUsePointCard = new javax.swing.JRadioButton();
        usePointCard = new javax.swing.JRadioButton();
        pointOutputCard = new javax.swing.JRadioButton();
        pointOutputReceipt = new javax.swing.JRadioButton();
        notUseBarcode = new javax.swing.JRadioButton();
        useBarcode = new javax.swing.JRadioButton();
        barcodeLabel = new javax.swing.JLabel();
        serialPortLabel = new javax.swing.JLabel();
        serialPort = new javax.swing.JComboBox();

        skinLabel.setText("�X�L��");

        registButton.setIcon(SystemInfo.getImageIcon("/button/common/regist_off.jpg"));
        registButton.setBorderPainted(false);
        registButton.setContentAreaFilled(false);
        registButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/regist_on.jpg"));
        registButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("�R�[���V�X�e��");

        ctiGroup.add(notUseCti);
        notUseCti.setSelected(true);
        notUseCti.setText("�g�p���Ȃ�");
        notUseCti.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        notUseCti.setMargin(new java.awt.Insets(0, 0, 0, 0));
        notUseCti.setOpaque(false);

        ctiGroup.add(useCti);
        useCti.setText("�g�p����");
        useCti.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        useCti.setMargin(new java.awt.Insets(0, 0, 0, 0));
        useCti.setOpaque(false);

        jLabel5.setText("COM�|�[�g");

        jPanel1.setOpaque(false);
        jPanel1.setLayout(null);

        pointLabel.setText("�|�C���g�V�X�e��");
        jPanel1.add(pointLabel);
        pointLabel.setBounds(20, 40, 85, 13);

        pointcardGroup.add(notUsePointCard);
        notUsePointCard.setSelected(true);
        notUsePointCard.setText("�g�p���Ȃ�");
        notUsePointCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        notUsePointCard.setMargin(new java.awt.Insets(0, 0, 0, 0));
        notUsePointCard.setOpaque(false);
        jPanel1.add(notUsePointCard);
        notUsePointCard.setBounds(110, 30, 100, 24);

        pointcardGroup.add(usePointCard);
        usePointCard.setText("�g�p����");
        usePointCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        usePointCard.setMargin(new java.awt.Insets(0, 0, 0, 0));
        usePointCard.setOpaque(false);
        jPanel1.add(usePointCard);
        usePointCard.setBounds(220, 40, 100, 13);

        pointOutputGroup.add(pointOutputCard);
        pointOutputCard.setSelected(true);
        pointOutputCard.setText("�����C�g�J�[�h");
        pointOutputCard.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        pointOutputCard.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pointOutputCard.setOpaque(false);
        jPanel1.add(pointOutputCard);
        pointOutputCard.setBounds(110, 60, 100, 13);

        pointOutputGroup.add(pointOutputReceipt);
        pointOutputReceipt.setText("���V�[�g");
        pointOutputReceipt.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        pointOutputReceipt.setMargin(new java.awt.Insets(0, 0, 0, 0));
        pointOutputReceipt.setOpaque(false);
        jPanel1.add(pointOutputReceipt);
        pointOutputReceipt.setBounds(220, 60, 100, 13);

        barcodeGroup.add(notUseBarcode);
        notUseBarcode.setSelected(true);
        notUseBarcode.setText("�g�p���Ȃ�");
        notUseBarcode.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        notUseBarcode.setMargin(new java.awt.Insets(0, 0, 0, 0));
        notUseBarcode.setOpaque(false);
        jPanel1.add(notUseBarcode);
        notUseBarcode.setBounds(130, 150, 100, 24);

        barcodeGroup.add(useBarcode);
        useBarcode.setText("�g�p����");
        useBarcode.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        useBarcode.setMargin(new java.awt.Insets(0, 0, 0, 0));
        useBarcode.setOpaque(false);
        jPanel1.add(useBarcode);
        useBarcode.setBounds(220, 160, 100, 13);

        barcodeLabel.setText("�o�[�R�[�h���[�_�[");

        serialPortLabel.setText("COM�|�[�g");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 364, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(registButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(layout.createSequentialGroup()
                                .add(skinLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 62, Short.MAX_VALUE)
                                .add(skin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 233, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(jLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jLabel5)
                            .add(notUseCti, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(serialPortCti, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(useCti, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(93, 93, 93))
                    .add(layout.createSequentialGroup()
                        .add(barcodeLabel)
                        .add(54, 54, 54)
                        .add(serialPortLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 7, Short.MAX_VALUE)
                        .add(serialPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(93, 93, 93))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(registButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(skinLabel)
                    .add(skin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(22, 22, 22)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(notUseCti, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(useCti)
                    .add(jLabel4))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, serialPortCti, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(61, 61, 61)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(barcodeLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(serialPort, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, serialPortLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(39, 39, 39)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 188, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(74, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

	private void registButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_registButtonActionPerformed
	{//GEN-HEADEREND:event_registButtonActionPerformed
		if(this.checkInput())
		{
			this.regist();
		}
	}//GEN-LAST:event_registButtonActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup barcodeGroup;
    private javax.swing.JLabel barcodeLabel;
    private javax.swing.ButtonGroup ctiGroup;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton notUseBarcode;
    private javax.swing.JRadioButton notUseCti;
    private javax.swing.JRadioButton notUsePointCard;
    private javax.swing.JLabel pointLabel;
    private javax.swing.JRadioButton pointOutputCard;
    private javax.swing.ButtonGroup pointOutputGroup;
    private javax.swing.JRadioButton pointOutputReceipt;
    private javax.swing.ButtonGroup pointcardGroup;
    private javax.swing.JButton registButton;
    private javax.swing.JComboBox serialPort;
    private javax.swing.JComboBox serialPortCti;
    private javax.swing.JLabel serialPortLabel;
    private javax.swing.JComboBox skin;
    private javax.swing.JLabel skinLabel;
    private javax.swing.JRadioButton useBarcode;
    private javax.swing.JRadioButton useCti;
    private javax.swing.JRadioButton usePointCard;
    // End of variables declaration//GEN-END:variables
	
	private	EnvironmentalSetting	es	=	null;
	
	private void init()
	{
		skin.removeAllItems();
		
		for(MstSkin ms : es.getSkinList())
		{
			skin.addItem(ms);
		}
		
		serialPort.addItem(" ");
		serialPortCti.addItem(" ");
		for(String sp : es.getSerialPortList())
		{
			serialPort.addItem(sp);
			serialPortCti.addItem(sp);
		}
		
		this.setSelectedSkin();

                if( SystemInfo.isUseBarcodeReader() )
                {
                    this.useBarcode.setSelected(true);
                    this.notUseBarcode.setSelected(false);
                }
                else
                {
                    this.useBarcode.setSelected(false);
                    this.notUseBarcode.setSelected(true);
                }
                
		this.setSelectedSerialPort();
		this.setSelectedSerialPortCti();
                
                if( SystemInfo.isUsePointcard() )
                {   
                    this.usePointCard.setSelected(true);
                    this.notUsePointCard.setSelected(false);
                }
                else
                {    
                    this.usePointCard.setSelected(false);
                    this.notUsePointCard.setSelected(true);
                }
		
                if( SystemInfo.isUseCtiReader() )
                {
                    this.useCti.setSelected(true);
                    this.notUseCti.setSelected(false);
                }
                else
                {
                    this.useCti.setSelected(false);
                    this.notUseCti.setSelected(true);
                }
                
		//�X�L���@�\���g�p���Ă��Ȃ��̂Ŕ�\����
		skin.setVisible(false);
		skinLabel.setVisible(false);

                // �|�C���g�o�͋敪
                pointOutputReceipt.setSelected(SystemInfo.getPointOutputType() == 1);
	}
	
	private void setSelectedSkin()
	{
		for(Integer i = 0; i < es.getSkinList().size(); i ++)
		{
			if(es.getSkinList().get(i).getSkinID() == SystemInfo.getSkin().getSkinID())
			{
				skin.setSelectedIndex(i);
				return;
			}
		}
		
		if(0 < skin.getItemCount())
		{
			skin.setSelectedIndex(0);
		}
	}
	
	private Integer getSelectedSkinID()
	{
		if(0 <= skin.getSelectedIndex())
		{
			return	((MstSkin)skin.getSelectedItem()).getSkinID();
		}
		
		return	null;
	}
	
	private void setSelectedSerialPort()
	{
		String	selPortName	=	SystemInfo.getBarcordReaderParameters().getPortName();
  		for(int i = 0; i < es.getSerialPortList().size(); i ++)
		{
			if(es.getSerialPortList().get(i).equals(selPortName))
			{
                                serialPort.setSelectedIndex(i + 1);
				return;
                                
			}
		}
		
		if(0 < serialPort.getItemCount())
		{
			serialPort.setSelectedIndex(0);
		}
	}

	private void setSelectedSerialPortCti()
	{
		String	selPortName	=	SystemInfo.getCtiReaderParameters().getPortName();
  		for(int i = 0; i < es.getSerialPortList().size(); i ++)
		{
			if(es.getSerialPortList().get(i).equals(selPortName))
			{
                                serialPortCti.setSelectedIndex(i + 1);
				return;
                                
			}
		}
		
		if(0 < serialPortCti.getItemCount())
		{
			serialPortCti.setSelectedIndex(0);
		}
	}
        
	private boolean checkInput()
	{
		if(skin.getSelectedIndex() < 0)
		{
			return	false;
		}
		
		return	true;
	}
	
	private void regist()
	{
		Integer		skinID			=	skin.getSelectedIndex();
		Boolean		isUseBarcodeReader	=	useBarcode.isSelected();
		String		barcodeReaderPort	=	serialPort.getSelectedItem().toString().trim();
                Boolean         isUsePointcardReader    =       this.usePointCard.isSelected();
		Boolean		isUseCtiReader          =	useCti.isSelected();
		String		ctiReaderPort           =	serialPortCti.getSelectedItem().toString().trim();
                Integer         pointOutputType         =       pointOutputReceipt.isSelected() ? 1 : 0;
                
		//if(es.regist(skinID, isUseBarcodeReader, barcodeReaderPort))
                if(es.regist(skinID, isUseBarcodeReader, isUsePointcardReader, barcodeReaderPort, isUseCtiReader, ctiReaderPort, pointOutputType))
		{
			SystemInfo.openBarcodeReaderConnection((BarcodeListener)this.parentFrame);
                        SystemInfo.openPointcardConnection();
			MessageDialog.showMessageDialog(this,
					//MessageUtil.getMessage(7010),
                                        MessageUtil.getMessage(7011),
					this.getTitle(),
					JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
		}
	}
}