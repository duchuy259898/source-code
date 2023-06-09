/*
 * TopPagePanel.java
 *
 * Created on 2007/03/16, 9:16
 */

package com.geobeck.sosia.pos.main;

import java.awt.Desktop;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Cursor;
import java.sql.*;
import com.geobeck.sql.*;
import com.geobeck.swing.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.basicinfo.company.*;
import org.mozilla.browser.MozillaInitialization;
import org.mozilla.browser.MozillaPanel;

/**
 *
 * @author  katagiri
 */
public class TopPagePanel extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx
{
	/** Creates new form TopPagePanel */
	public TopPagePanel()
	{
		initComponents();
		this.setSize(833, 691);

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
                if (SystemInfo.getBaseConnection().getUrl().startsWith("jdbc:postgresql://127.0.0.1:5432")) {
                    
                    // インターネット未接続時の画面表示
                    btnSupport.setVisible(true);
                    SystemInfo.addMouseCursorChange(btnSupport);
                    
                } else {
                    
                    btnSupport.setVisible(false);

                    // サポートサイト表示
                    this.setName("web");

                    try {
                        setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
                        setLayout(new BorderLayout());
                        MozillaPanel panel = new MozillaPanel(MozillaPanel.VisibilityMode.FORCED_HIDDEN, MozillaPanel.VisibilityMode.FORCED_HIDDEN);

                        if (MozillaInitialization.getStatus().equals(MozillaInitialization.InitStatus.INITIALIZED)) {

                            panel.setUpdateTitle(false);
                            panel.getMozillaContainer().setBackground(new java.awt.Color(235, 235, 235));
                            add(panel, BorderLayout.CENTER);

                            String param = "&shopname=" + SystemInfo.getDirectoryName()
                                         + "&username=" + SystemInfo.getLoginID()
                                         + "&password=" + SystemInfo.getLoginPass();

                            //panel.load("http://support.sosia.jp/index.php?k=" + SystemInfo.getLoginID() + param);
                            panel.load("http://support.sosia.jp/portal/index.php?k=" + SystemInfo.getLoginID() + param);

                        } else {

                            btnSupport.setVisible(true);
                            SystemInfo.addMouseCursorChange(btnSupport);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

                SosiaPos.waitDialog.setVisible(false);
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSupport = new javax.swing.JButton();

        setLayout(null);

        btnSupport.setFont(new java.awt.Font("MS UI Gothic", 1, 14)); // NOI18N
        btnSupport.setForeground(java.awt.Color.blue);
        btnSupport.setText("SOSIA POSポータル");
        btnSupport.setBorder(null);
        btnSupport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupportActionPerformed(evt);
            }
        });
        add(btnSupport);
        btnSupport.setBounds(40, 30, 290, 40);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSupportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupportActionPerformed

        btnSupport.setCursor(null);

        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            String helpUrl = "http://support.sosia.jp/portal/index.php?k=" + SystemInfo.getLoginID();
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new java.net.URI(helpUrl));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        
    }//GEN-LAST:event_btnSupportActionPerformed
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSupport;
    // End of variables declaration//GEN-END:variables
}
