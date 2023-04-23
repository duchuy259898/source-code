/*
 * HairSearchAccountPanel.java
 *
 * Created on 2006/10/18, 20:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.account;

import com.geobeck.sosia.pos.account.*;
import com.geobeck.sosia.pos.data.account.DataSales;

/**
 *
 * @author katagiri
 */
public class HairSearchAccountPanel extends SearchAccountPanel
{
	
	/** Creates a new instance of HairSearchAccountPanel */
	public HairSearchAccountPanel()
	{
		super();
	}
	
	/**
	 * “`•[‚ª‘I‘ğ‚³‚ê‚½‚Æ‚«‚Ìˆ—‚ğs‚¤B
	 */
	protected void select()
	{
            //“`•[‚ª‘I‘ğ‚³‚ê‚Ä‚¢‚È‚¢ê‡‚Íˆ—‚ğ”²‚¯‚é
            if (searchResult.getSelectedRowCount() < 1) return;
            
            //‘I‘ğ‚³‚ê‚Ä‚¢‚é“`•[No.‚ğƒZƒbƒg
            this.setSelectedSlipNo(sa.get(searchResult.getSelectedRow()).getSales().getSlipNo());

            int currentIndex = 0;
            for (DataSales ds : targetList) {
                if (sa.get(searchResult.getSelectedRow()).getSales().getSlipNo().equals(ds.getSlipNo())) {
                    break;
                }
                currentIndex++;
            }
            
            //“`•[ŒŸõ‚©‚çA“`•[“ü—Í‚ğŠJ‚­ê‡
            if (this.isParentDialog()) {
                //IVS_LVTu start add 2015/10/09 Bug #43295
                HairInputAccountPanel.refShop = this.getSelectedShop();
                //IVS_LVTu end add 2015/10/09 Bug #43295
                HairInputAccountPanel hiap = new HairInputAccountPanel();
                hiap.setOpener(this);
                hiap.setTargetList(targetList);
                hiap.setCurrentIndex(currentIndex);
                hiap.load(this.getSelectedShop(), this.getSelectedSlipNo());
                hiap.showData();
                this.setVisible(false);
                parentFrame.changeContents(hiap);
            } else {
                this.setVisible(false);
            }
	}
}
