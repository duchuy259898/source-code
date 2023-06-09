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
	 * 伝票が選択されたときの処理を行う。
	 */
	protected void select()
	{
            //伝票が選択されていない場合は処理を抜ける
            if (searchResult.getSelectedRowCount() < 1) return;
            
            //選択されている伝票No.をセット
            this.setSelectedSlipNo(sa.get(searchResult.getSelectedRow()).getSales().getSlipNo());

            int currentIndex = 0;
            for (DataSales ds : targetList) {
                if (sa.get(searchResult.getSelectedRow()).getSales().getSlipNo().equals(ds.getSlipNo())) {
                    break;
                }
                currentIndex++;
            }
            
            //伝票検索から、伝票入力を開く場合
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
