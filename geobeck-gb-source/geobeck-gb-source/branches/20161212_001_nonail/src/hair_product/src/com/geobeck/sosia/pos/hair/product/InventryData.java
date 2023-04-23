/*
 * InventryData.java
 *
 * Created on 2008/09/30, 11:50
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product;

import com.geobeck.sosia.pos.util.TaxUtil;
import com.geobeck.sql.ResultSetWrapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

/**
 * 棚卸データ管理クラス
 * @author syouji
 */
public class InventryData {
    
	protected	Integer			supplierID		=	0;
	protected	Integer			itemID          =   0;
	protected	String			itemName        =   null;
	protected	Integer			itemSeq         =   null;
	protected	String			itemClassName   =   null;
	protected	Integer			itemClassSeq    =   0;
	protected	String			placeName       =   null;
	protected	Integer			placeSeq        =   0;
	protected	Long			costPriceTaxIn  =   0l;
	protected	Long			costPriceTaxEx  =   0l;
	protected	Long			nyukoCostPriceTaxIn  =   0l;
	protected	Long			nyukoCostPriceTaxEx  =   0l;
        protected	Long			syukoCostPriceTaxIn  =   0l;
	protected	Long			syukoCostPriceTaxEx  =   0l;
	protected	Integer			kisyuNum        =   0;
	protected	Integer			nyukoNum        =   0;
	protected	Integer			nyukoAttachNum  =   0;
	protected	Integer			syukoNum        =   0;
	protected	Integer			jitsuZaiko      =   0;
	protected	Integer			fixed           =   0;
    
        protected       boolean                 updateFlag         =   false;
	protected	boolean         	jitsuZaikoNull     =   false;
        //IVS_LVTu start add 2015/07/22 New request #40693
        protected	Integer			itemClassID   =   null;

        public Integer getItemClassID() {
            return itemClassID;
        }

        public void setItemClassID(Integer itemClassID) {
            this.itemClassID = itemClassID;
        }
        //IVS_LVTu end add 2015/07/22 New request #40693
    
	/**
	 * コンストラクタ
	 */
    public InventryData() {
    }

	/**
	 * 仕入先IDを取得する
	 * @return 仕入先ID
	 */
	public Integer getSupplierID()
	{
		return supplierID;
	}    
    
	/**
	 * 商品IDを取得する
	 * @return 商品ID
	 */
	public Integer getItemID()
	{
		return itemID;
	}    
    
	/**
	 * 商品名を取得する
	 * @return 商品名
	 */
	public String getItemName()
	{
		return itemName;
	}

	/**
	 * 商品表示順を取得する
	 * @return 商品表示順
	 */
	public Integer getItemSeq()
	{
		return itemSeq;
	}    
    
	/**
	 * 分類名を取得する
	 * @return 分類名
	 */
	public String getItemClassName()
	{
		return itemClassName;
	}
    
	/**
	 * 分類の表示順を取得する
	 * @return 表示順
	 */
	public Integer getItemClassSeq()
	{
		return itemClassSeq;
	}
    
	/**
	 * 置場名を取得する
	 * @return 置場名
	 */
	public String getPlaceName()
	{
		return placeName;
	}

	/**
	 * 置き場の表示順を取得する
	 * @return 表示順
	 */
	public Integer getPlaceSeq()
	{
		return placeSeq;
	}
    
	/**
	 * 仕入単価（税込）を取得する
	 * @return 仕入単価
	 */
	public Long getCostPriceTaxIn()
	{
		return costPriceTaxIn;
	}
    
	/**
	 * 仕入単価（税抜）を取得する
	 * @return 仕入単価
	 */
	public Long getCostPriceTaxEx()
	{
		return costPriceTaxEx;
	}

	/**
	 * 入庫単価（税込）を取得する
	 * @return 入庫単価
	 */
	public Long getNyukoCostPriceTaxIn()
	{
		return nyukoCostPriceTaxIn;
	}

	/**
	 * 入庫単価（税抜）を取得する
	 * @return 入庫単価
	 */
	public Long getNyukoCostPriceTaxEx()
	{
		return nyukoCostPriceTaxEx;
	}

	/**
	 * 出庫単価（税込）を取得する
	 * @return 出庫単価
	 */
	public Long getSyukoCostPriceTaxIn()
	{
		return syukoCostPriceTaxIn;
	}

	/**
	 * 出庫単価（税抜）を取得する
	 * @return 出庫単価
	 */
	public Long getSyukoCostPriceTaxEx()
	{
		return syukoCostPriceTaxEx;
	}

	/**
	 * 期首在庫数を取得する。
	 * @return 期首在庫数
	 */
	public Integer getKisyuNum()
	{
		return kisyuNum;
	}
    
	/**
	 * 入庫数を取得する。
	 * @return 入庫数
	 */
	public Integer getNyukoNum()
	{
		return nyukoNum;
	}

	/**
	 * 添付数を取得する。
	 * @return 添付数
	 */
	public Integer getNyukoAttachNum()
	{
		return nyukoAttachNum;
	}
    
	/**
	 * 出庫数を取得する。
	 * @return 出庫数
	 */
	public Integer getSyukoNum()
	{
		return syukoNum;
	}
    
	/**
	 * 実在庫数を取得する。
	 * @return 実在庫数
	 */
	public Integer getJitsuZaiko()
	{
		if (jitsuZaikoNull)
		{
			return null;
		}
		return jitsuZaiko;
	}

	/**
	 * 実在庫数をセットする。
	 * @param jitsuZaiko 実在庫数
	 */
	public void setJitsuZaiko(Integer jitsuZaiko)
	{
		if (jitsuZaiko == null)
		{
			if (this.jitsuZaiko != null)
			{
				updateFlag = true;
				this.jitsuZaiko = jitsuZaiko;
			}

			jitsuZaikoNull = true;
		}
		else
		{
			if (!jitsuZaiko.equals(this.jitsuZaiko))
			{
				updateFlag = true;
				this.jitsuZaiko = jitsuZaiko;
			}

			jitsuZaikoNull = false;
		}
	}

	/**
	 * 確定フラグを取得する。
	 * @return 確定フラグ
	 */
	public Integer getFixed()
	{
		return this.fixed;
	}    

    /*
     * 実在庫がNullか（棚卸未入力）
     */
    public boolean isJitsuZaikoNull()
    {
        return this.jitsuZaikoNull;
    }
    
    /**
     * ResultSetWrapperからデータをセットする。
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs, Double taxRate) throws SQLException
    {
        supplierID = rs.getInt("supplier_id");
        itemID = rs.getInt("item_id");
        itemName = rs.getString("item_name");
        itemSeq = rs.getInt("item_seq");
        itemClassName = rs.getString("item_class_name");
        //IVS_LVTu start add 2015/07/22 New request #40693
        itemClassID = rs.getInt("item_class_id");
        //IVS_LVTu end add 2015/07/22 New request #40693
        itemClassSeq = rs.getInt("item_class_seq");
        placeName = rs.getString("place_name");
        placeSeq = rs.getInt("place_seq");
        kisyuNum = rs.getInt("kisyu_num");
        nyukoNum = rs.getInt("nyuko_num");
        nyukoAttachNum = rs.getInt("nyuko_attach_num");
        syukoNum = rs.getInt("syuko_num");
        jitsuZaiko = rs.getInt("jitsu_zaiko");
        jitsuZaikoNull = rs.wasNull();

        costPriceTaxIn = rs.getLong("mst_cost_price");
        Long tax = TaxUtil.getTax(costPriceTaxIn, taxRate, 1);
        //IVS_LVTu start edit 2015/09/30 Bug #43033
        //costPriceTaxEx = costPriceTaxIn - tax;
        costPriceTaxEx = getTaxOffPrice(costPriceTaxIn , taxRate);
        //IVS_LVTu end edit 2015/09/30 Bug #43033

        nyukoCostPriceTaxIn = rs.getLong("nyuko_cost_price");
        //IVS_LVTu start edit 2015/09/10 Bug #42528
        //tax = TaxUtil.getTax(nyukoCostPriceTaxIn, taxRate, 1);
        //nyukoCostPriceTaxEx = nyukoCostPriceTaxIn - tax;
        Double temp = Math.ceil(nyukoCostPriceTaxIn/(1+ taxRate));
        nyukoCostPriceTaxEx = temp.longValue();
        //IVS_LVTu end edit 2015/09/10 Bug #42528

        syukoCostPriceTaxIn = rs.getLong("syuko_cost_price");
        //IVS_LVTu start add 2016/03/03 Bug #48974
        //tax = TaxUtil.getTax(syukoCostPriceTaxIn, taxRate, 1);
        //syukoCostPriceTaxEx = syukoCostPriceTaxIn - tax;
        syukoCostPriceTaxEx = getTaxOffPrice(syukoCostPriceTaxIn , taxRate);
        //IVS_LVTu end add 2016/03/03 Bug #48974
        
        updateFlag = false;
        
    }
    
    public boolean isUpdate() {
        return updateFlag;
    }
   
	public void setUpdate(boolean v)
	{
		this.updateFlag = v;
	}
        //IVS_LVTu start add 2015/09/30 Bug #43033
        private Long getTaxOffPrice(Long price, double taxRate)
        {
                Long	taxOffValue		=	0l;
                Double temptaxOffValue ;

                temptaxOffValue	=	(price / (1 + taxRate));
                BigDecimal a = new BigDecimal(temptaxOffValue);
                a = a.setScale(3, RoundingMode.HALF_UP);
                taxOffValue = ((Double)Math.ceil(a.doubleValue())).longValue();

            return taxOffValue;
        }
        //IVS_LVTu end add 2015/09/30 Bug #43033
}
