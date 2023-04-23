/*
 * HairBillsListPanel.java
 *
 * Created on 2006/10/18, 20:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.account;

/**
 *
 * @author katagiri
 */
public class HairBillsListPanel extends com.geobeck.sosia.pos.account.BillsListPanel
{
	
	/**
	 * Creates a new instance of HairBillsListPanel
	 */
	public HairBillsListPanel()
	{
		super();
		// PDFo—Í‚ÍŒ»“_‚Å‚Í”ñ•\¦
		this.changePrintButtonVisible( false );
	}
	
	
	public HairBillsListPanel(Integer customerID)
	{
		super(customerID);
	}
	
	
        @Override
	protected void setBill()
	{
		HairInputAccountPanel	iap	=	(HairInputAccountPanel)this.getOpener();
		iap.setBill();
	}
}
