/*
 * OpenCustomerPopupMenu.java
 *
 * Created on 2007/11/09, 15:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.customer;

import javax.swing.*;

import com.geobeck.swing.*;

/**
 *
 * @author katagiri
 */
public class OpenCustomerPopupMenu extends JPopupMenu
{
	private	java.awt.Frame		owner		=	null;
	private	boolean				modal		=	false;
	private	Integer				customerID	=	null;
	
	/** Creates a new instance of OpenCustomerPopupMenu */
	public OpenCustomerPopupMenu(java.awt.Frame owner, boolean modal, Integer customerID)
	{
		super();
		this.owner	=	owner;
		this.modal	=	modal;
		this.customerID	=	customerID;
		this.initMenu();
	}
	
	private void initMenu()
	{
		JMenuItem	customerMenu = new javax.swing.JMenuItem();
        customerMenu.setFont(new java.awt.Font("MS UI Gothic", 0, 24));
        customerMenu.setText("\u9867\u5ba2\u60c5\u5831");
        customerMenu.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                openMstCustomerDialog(evt);
            }
        });

        this.add(customerMenu);
	}
	
	private void openMstCustomerDialog(java.awt.event.ActionEvent evt)
	{
		MstCustomerPanel	mcp	=	new MstCustomerPanel(customerID);
		SwingUtil.openDialog(owner, modal, mcp, "å⁄ãqèÓïÒìoò^");
		((JDialog)mcp.getParent().getParent().getParent().getParent()).dispose();
	}
}
