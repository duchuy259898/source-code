/*
 * MstFreeHeadingPanel.java
 *
 * Created on 2007/08/17, 19:45
 */

package com.geobeck.sosia.pos.hair.basicinfo.product;


import com.geobeck.sosia.pos.hair.master.product.*;

/**
 *
 * @author  kanemoto
 */
public class MstFreeHeadingPanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx
{
    
    /** Creates new form MstFreeHeadingPanel */
    public MstFreeHeadingPanel() {
	initComponents();
	this.init();
	this.setPath("基本設定 >> 顧客マスタ");
	this.setTitle("フリー項目登録");
	this.setSize(450, 691);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        freeHeadingClassTab = new javax.swing.JTabbedPane();

        freeHeadingClassTab.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                freeHeadingClassTabStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(freeHeadingClassTab, javax.swing.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(freeHeadingClassTab, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void freeHeadingClassTabStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_freeHeadingClassTabStateChanged
	this.changedTab();
    }//GEN-LAST:event_freeHeadingClassTabStateChanged
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane freeHeadingClassTab;
    // End of variables declaration//GEN-END:variables

    private MstFreeHeadingUnits		mfhus		=	null;
    private boolean                    isRequiresFlg           =       false;

    private void init()
    {
	    mfhus		=	new MstFreeHeadingUnits();

	    if( 0 < mfhus.size() )
	    {
		for(MstFreeHeadingUnitPanel mitdsp : mfhus)
		{
			freeHeadingClassTab.addTab(mitdsp.getItemTypeName(), mitdsp);
		}
		isRequiresFlg = true;

		this.changedTab();
	    }
    }

    private void changedTab()
    {
	    mfhus.get(freeHeadingClassTab.getSelectedIndex()).showData();
    }
    
    /**
     * タブの存在を返す
     * @return 編集可能タブが存在する場合にTrueを返す
     */
    public boolean isRequires()
    {
	return this.isRequiresFlg;
    }

}
