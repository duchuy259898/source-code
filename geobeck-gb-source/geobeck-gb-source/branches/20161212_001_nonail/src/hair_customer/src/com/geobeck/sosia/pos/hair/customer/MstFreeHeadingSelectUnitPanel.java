/*
 * MstFreeHeadingSelectUnitPanel.java
 *
 * Created on 2007/08/20, 14:31
 */

package com.geobeck.sosia.pos.hair.customer;

import java.util.logging.*;
import com.geobeck.sql.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.hair.master.product.*;

/**
 *
 * @author  kanemoto
 */
public class MstFreeHeadingSelectUnitPanel extends javax.swing.JPanel {
    private MstFreeHeadingClass mfhc = null;		// フリー項目区分
    
    /** Creates new form MstFreeHeadingSelectUnitPanel */
    public MstFreeHeadingSelectUnitPanel( MstFreeHeadingClass mfhc ) {
		this.mfhc = mfhc;
		initComponents();
		init();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        freeHeadingLabel = new javax.swing.JLabel();
        freeHeading = new javax.swing.JComboBox();

        setOpaque(false);
        freeHeadingLabel.setText("\u30d5\u30ea\u30fc\u9805\u76ee");

        freeHeading.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(freeHeadingLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(freeHeading, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(freeHeadingLabel)
                .addComponent(freeHeading, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox freeHeading;
    private javax.swing.JLabel freeHeadingLabel;
    // End of variables declaration//GEN-END:variables
    
    /**
     * 初期化を行う
     */
    private void init()
    {
		ConnectionWrapper con	=	SystemInfo.getConnection();

		MstFreeHeading		w_mfh = null;
		MstFreeHeadingClass	w_mfhc = null;

		w_mfhc = new MstFreeHeadingClass();
		w_mfhc.setData( mfhc );
		w_mfh = new MstFreeHeading();
		w_mfh.setFreeHeadingClass( w_mfhc );
		w_mfh.setFreeHeadingID( -1 );
		w_mfh.setFreeHeadingName( " " );
		w_mfh.setDisplaySeq( 0 );
		freeHeading.addItem( w_mfh );

		try
		{
			mfhc.loadFreeHeading( con );
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}

		for( MstFreeHeading mfh : mfhc )
		{
			freeHeading.addItem( mfh );
		}

		freeHeadingLabel.setText( this.mfhc.getFreeHeadingClassName() );
    }
    
    /**
     * フリー項目区分を取得する
     */
    public Integer getFreeHeadingClassID()
    {
		return this.mfhc.getFreeHeadingClassID();
    }
    
    /**
     * フリー項目を取得する
     */
    public MstFreeHeading getFreeHeading()
    {
		return (MstFreeHeading)freeHeading.getSelectedItem();
    }
    
    /**
     * 選択項目をクリアする
     */
    public void setSelectedInit()
    {
		freeHeading.setSelectedIndex( 0 );
    }
    
    /**
     * フリー項目をセットする
     */
    public void setFreeHeading( MstFreeHeading mfh )
    {
		for( int i = 0; i < freeHeading.getItemCount(); i++  )
		{
			if( ( (MstFreeHeading)freeHeading.getItemAt( i ) ).getFreeHeadingID() == mfh.getFreeHeadingID() )
			{
				freeHeading.setSelectedIndex( i );
			}
		}
    }
}
