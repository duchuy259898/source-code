/*
 * SearchInventry.java
 *
 * Created on 2008/10/01, 15:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product;

import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.product.MstItemClass;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * 棚卸検索処理
 * @author syouji
 */
public class SearchInventry extends ArrayList<InventryData>
{
	
	/**
	 * 店舗
	 */
	protected MstShop       shop		=	new MstShop();
	
	/**
	 * 仕入先ID
	 */
	protected Integer       supplierID  =	null;
	
	/**
	 * 棚卸区分
	 */
	protected int           useDivision =	0;
	
	/**
	 * 棚卸期間(FROM)
	 */
	protected java.util.Date    fromDate    =   null;
	
	/**
	 * 棚卸期間(TO)
	 */
	protected java.util.Date    toDate    =   null;
	
	/**
	 * 棚卸ID
	 */
	protected Integer       inventoryID  =	null;

	/**
	 * 確定済みフラグ
	 */
	protected Integer       fixed = 0;

	/** 検索条件変更フラグ */
	protected boolean conditionChange   =   false;
        
        private Integer  isInStock = 0;
        //IVS_LVTu start add 2015/07/20 New request #40693
        private MstItemClass itemClass = new MstItemClass();

        public MstItemClass getItemClass() {
            return itemClass;
        }

        public void setItemClass(MstItemClass itemClass) {
            this.itemClass = itemClass;
        }
        //IVS_LVTu end add 2015/07/20 New request #40693	
	/**
	 * コンストラクタ
	 */
	public SearchInventry()
	{
	}
	
	/**
	 * 店舗
	 * @return 店舗
	 */
	public MstShop getShop()
	{
		return shop;
	}
	
	/**
	 * 店舗
	 * @param shop
	 */
	public void setShop(MstShop shop)
	{
		if (shop != null)
		{
			if (!shop.getShopID().equals(this.shop.getShopID()))
			{
				conditionChange = true;
			}
		}
		
		this.shop = shop;
	}
	
	/**
	 * 仕入先ID
	 * @return 仕入先ID
	 */
	public Integer getSupplierID()
	{
		return this.supplierID;
	}
	
	/**
	 * 仕入先ID
	 * @param supplierID
	 */
	public void setSupplierID(Integer supplierID)
	{
		if (supplierID != null)
		{
			if (!supplierID.equals(this.supplierID))
			{
				conditionChange = true;
			}
		}
		else
		{
			if (this.supplierID != null)
			{
				conditionChange = true;
			}
		}
		
		this.supplierID = supplierID;
	}
	
	/**
	 * 棚卸区分
	 * @return 棚卸区分
	 */
	public int getUseDivision()
	{
		return this.useDivision;
	}
	
	/**
	 * 棚卸区分
	 * @param useDivision
	 */
	public void setUseDivision(int useDivision)
	{
		if (this.useDivision != useDivision)
		{
			conditionChange = true;
		}
		this.useDivision = useDivision;
	}
	
	/**
	 * 棚卸期間(FROM)
	 * @return 棚卸期間(FROM)
	 */
	public java.util.Date getFromDate()
	{
		return this.fromDate;
	}
	
	/**
	 * 棚卸期間(FROM)
	 * @param fromDate
	 */
	public void setFromDate(java.util.Date fromDate)
	{
		if (fromDate != null)
		{
			if (!fromDate.equals(this.fromDate))
			{
				conditionChange = true;
			}
		}
		else
		{
			if (this.fromDate != null)
			{
				conditionChange = true;
			}
		}
		
		this.fromDate = fromDate;
	}
	
	/**
	 * 棚卸期間(TO)
	 * @return 棚卸期間(TO)
	 */
	public java.util.Date getToDate()
	{
		return this.toDate;
	}
	
	/**
	 * 棚卸期間(TO)
	 * @param toDate
	 */
	public void setToDate(java.util.Date toDate)
	{
		if (toDate != null)
		{
			if (!toDate.equals(this.toDate))
			{
				conditionChange = true;
			}
		}
		else
		{
			if (this.toDate != null)
			{
				conditionChange = true;
			}
		}
		
		this.toDate = toDate;
	}
	
	/**
	 * 仕入先ID
	 * @return 仕入先ID
	 */
	public Integer getInventoryID()
	{
		return this.inventoryID;
	}
	
	/**
	 * 棚卸ID
	 * @param inventoryID
	 */
	public void setInventoryID(Integer inventoryID)
	{
		if (inventoryID != null)
		{
			if (!inventoryID.equals(this.inventoryID))
			{
				conditionChange = true;
			}
		}
		else
		{
			if (this.inventoryID != null)
			{
				conditionChange = true;
			}
		}
		
		this.inventoryID = inventoryID;
	}
	
	
	
	/**
	 * 棚卸データを読み込む。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void load(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		//消費税率の取得
		Double taxRate = SystemInfo.getTaxRate(new Date());

                StringBuilder sql = new StringBuilder(1000);
                sql.append(" select");
                sql.append("     fixed");
                sql.append(" from");
                sql.append("     data_inventory");
                sql.append(" where");
                sql.append("     inventory_id = " + SQLUtil.convertForSQL(inventoryID));
                sql.append(" and inventory_division = " + SQLUtil.convertForSQL(useDivision));
		ResultSetWrapper rs = con.executeQuery(sql.toString());
                if (rs.next()) {
                    this.fixed = rs.getInt("fixed");
                } else {
                    this.fixed = 0;
                }

		rs = con.executeQuery(this.getSelectSQL());
		while (rs.next()) {
                    InventryData id = new InventryData();
                    id.setData(rs, taxRate);
                    this.add(id);
		}

		rs.close();
		
		conditionChange = false;
	}

	/**
	 * 棚卸データを取得するＳＱＬ文を取得する。
	 * @return 棚卸データを取得するＳＱＬ文
	 */
	private String getSelectSQL()
	{
		//期首在庫のキーとなる日付を求める
		Calendar prevCal = Calendar.getInstance();
		if (fromDate == null) {
                    prevCal.setTime(toDate);
                    prevCal.add(Calendar.DATE, -1); //Toの前日が前回の棚卸終了日
		} else {
                    prevCal.setTime(fromDate);
                    prevCal.add(Calendar.DATE, -1); //Fromの前日が前回の棚卸終了日
		}

		StringBuilder sql = new StringBuilder(1000);
                sql.append(" select * from (");

                sql.append(" select");
                sql.append("      mst_supplier_item.supplier_id");
                sql.append("     ,mst_supplier_item.item_id");
                sql.append("     ,mst_item.item_name");
                sql.append("     ,mst_item.display_seq           as item_seq");
                sql.append("     ,mst_item_class.item_class_name");
                //IVS_LVTu start add 2015/07/22 New request #40693
                sql.append("     ,mst_item_class.item_class_id");
                //IVS_LVTu end add 2015/07/22 New request #40693
                sql.append("     ,mst_item_class.display_seq     as item_class_seq");
                sql.append("     ,mst_place.place_name");
                sql.append("     ,mst_place.display_seq          as place_seq");
                //IVS_LVTu start add 2015/10/30 New request #44075
                //sql.append("     ,mst_supplier_item.cost_price   as mst_cost_price");
                if (inventoryID != null) {
                    sql.append("     ,case when zaiko.item_id  IS NULL then mst_supplier_item.cost_price else zaiko.cost_price end  AS mst_cost_price ");
                } else {
                    sql.append("     ,mst_supplier_item.cost_price   as mst_cost_price ");
                }
                //IVS_LVTu end add 2015/10/30 New request #44075
                sql.append("     ,Kisyu.real_stock               as kisyu_num");
                sql.append("     ,Nyuko.in_num                   as nyuko_num");
                sql.append("     ,Nyuko.attach_num               as nyuko_attach_num");
                sql.append("     ,Nyuko.cost_price               as nyuko_cost_price");
                
                if (useDivision == 1) {
                    // 店販用
                    sql.append(" ,coalesce(Syuko.out_num, 0) + coalesce(Sales0.item_num, 0) + coalesce(Sales.item_num, 0) as syuko_num");
                    sql.append(" ,coalesce(Syuko.cost_price, Sales0.cost_price, 0) as syuko_cost_price");
                }else{
                    // 業務用
                    sql.append(" ,coalesce(Syuko.out_num, 0) + coalesce(Sales.item_num, 0) as syuko_num");
                    sql.append(" ,coalesce(Syuko.cost_price, 0) as syuko_cost_price");
                }
                
		if (inventoryID != null) {
                    sql.append(" ,coalesce(Zaiko.real_stock, 0) as jitsu_zaiko");
                    sql.append(" ,Zaiko.fixed      as fixed");
		} else {
                    sql.append(" ,NULL as jitsu_zaiko");
                    sql.append(" ,0    as fixed");
		}
                
		sql.append(" from");
                
                //仕入先商品マスタ
		sql.append("     mst_supplier_item");
		
                //商品マスタ
                sql.append("         inner join mst_item");
                sql.append("                 on mst_supplier_item.item_id = mst_item.item_id");

//Updated 2012-04-02------------------------------------------------------------------------------
                // 未登録の場合は使用区分の条件で抽出する
//                sql.append("                and");
//                sql.append("                 (");
//                sql.append("                     case when");
//                sql.append("                         not exists");
//                sql.append("                         (");
//                sql.append("                             select");
//                sql.append("                                 1");
//                sql.append("                             from");
//                sql.append("                                 data_inventory");
//                sql.append("                             where");
//                sql.append("                                     delete_date is null");
//                sql.append("                                 and fixed = 1");
//                sql.append("                                 and shop_id = " + SQLUtil.convertForSQL(shop.getShopID()));
//                sql.append("                                 and inventory_division = " + SQLUtil.convertForSQL(useDivision));
//                sql.append("                                 and inventory_date = " + SQLUtil.convertForSQLDateOnly(toDate));
//                sql.append("                         )");
//                sql.append("                         then mst_item.item_use_division in (" + SQLUtil.convertForSQL(useDivision) + ", 3)");
//                sql.append("                         else true");
//                sql.append("                     end");
//                sql.append("                 )");
//Updated 2012-04-02------------------------------------------------------------------------------
                // 常に使用区分で判定するようにする
                sql.append("                and mst_item.item_use_division in (" + SQLUtil.convertForSQL(useDivision) + ", 3)");
//Updated 2012-04-02------------------------------------------------------------------------------

                sql.append("                and mst_item.delete_date is null");
                sql.append("                and mst_supplier_item.delete_date is null");

                //店舗使用技術・商品マスタ
                sql.append("         inner join mst_use_product");
                sql.append("                 on mst_use_product.product_id = mst_item.item_id");
                sql.append("                and mst_use_product.product_division = 2");
                sql.append("                and mst_use_product.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()));
                sql.append("                and mst_use_product.delete_date is null");

                //商品分類マスタ

                sql.append("         left join mst_item_class");
                sql.append("                on mst_item.item_class_id = mst_item_class.item_class_id");
                
                //置き場マスタ
                sql.append("         left join mst_place");
                sql.append("                on mst_item.place_id = mst_place.place_id");
                
                //Kisyu:期首在庫を求めるクエリ
                sql.append("         left join");
                sql.append("             (");
                sql.append("                 select");
                sql.append("                      data_inventory_detail.item_id");
                sql.append("                     ,data_inventory_detail.real_stock");
                sql.append("                 from");
                sql.append("                     data_inventory_detail");
                sql.append("                         inner join data_inventory");
                sql.append("                                 on data_inventory_detail.inventory_id = data_inventory.inventory_id");
                sql.append("                                and data_inventory_detail.inventory_division = data_inventory.inventory_division");
                sql.append("                 where");
                sql.append("                         data_inventory.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()));
                sql.append("                     and data_inventory_detail.inventory_division = " + SQLUtil.convertForSQL(useDivision));
                sql.append("                     and data_inventory.inventory_date = " + SQLUtil.convertForSQLDateOnly(prevCal));
                sql.append("                     and data_inventory.delete_date IS NULL");
                sql.append("                     and data_inventory_detail.delete_date IS NULL");
                sql.append("             ) Kisyu");
                sql.append("             on mst_supplier_item.item_id = Kisyu.item_id");
                
                //Nyuko:入庫数を求めるクエリ
                sql.append("         left join");
                sql.append("             (");
                sql.append("                 select");
                sql.append("                      store.supplier_id");
                sql.append("                     ,store_detail.item_id");
                sql.append("                     ,MAX(store_detail.cost_price) AS cost_price");
                sql.append("                     ,SUM(store_detail.in_num) AS in_num");
                sql.append("                     ,SUM(store_detail.attach_num) AS attach_num");
                sql.append("                 from");
                sql.append("                     data_slip_store_detail store_detail");
                sql.append("                         inner join data_slip_store store");
                sql.append("                                 on store_detail.shop_id = store.shop_id");
                sql.append("                                and store_detail.slip_no = store.slip_no");
                sql.append("                 where");
                sql.append("                         store.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()));

                if (fromDate != null && !fromDate.equals(toDate)) {
                    sql.append("                 and store.store_date between "+ SQLUtil.convertForSQLDateOnly(fromDate) + " and " + SQLUtil.convertForSQLDateOnly(toDate));
		} else {
                    //初回の棚卸範囲
                    sql.append("                 and store.store_date <= " + SQLUtil.convertForSQLDateOnly(toDate));
		}
                
                sql.append("                     and store.delete_date is null");
                sql.append("                     and store_detail.item_use_division = " + SQLUtil.convertForSQL(useDivision));
                sql.append("                     and store_detail.delete_date is null");
                sql.append("                 group by");
                sql.append("                      store.supplier_id");
                sql.append("                     ,store_detail.item_id");
                sql.append("             ) Nyuko");
                sql.append("             on mst_supplier_item.supplier_id = Nyuko.supplier_id");
                sql.append("            and mst_supplier_item.item_id = Nyuko.item_id");
		
                //Syuko:出庫数を求めるクエリ
                sql.append("         left join");
                sql.append("             (");
                sql.append("                 select");
                sql.append("                      ship.supplier_id");
                sql.append("                     ,ship_detail.item_id");
                sql.append("                     ,max(ship_detail.cost_price) as cost_price");
                sql.append("                     ,sum(ship_detail.out_num) as out_num");
                sql.append("                 from");
                sql.append("                     data_slip_ship_detail ship_detail");
                sql.append("                         inner join data_slip_ship ship");
                sql.append("                                 on ship_detail.shop_id = ship.shop_id");
                sql.append("                                and ship_detail.slip_no = ship.slip_no");
                sql.append("                 where");
                sql.append("                         ship.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()));
                
		if (fromDate != null && !fromDate.equals(toDate)) {
                    sql.append("                 and ship.ship_date BETWEEN "+ SQLUtil.convertForSQLDateOnly(fromDate) + " AND " + SQLUtil.convertForSQLDateOnly(toDate));
		} else {
                    sql.append("                 and ship.ship_date <= " + SQLUtil.convertForSQLDateOnly(toDate));
		}
                sql.append("                     and ship.delete_date is null");
                sql.append("                     and ship_detail.item_use_division = " + SQLUtil.convertForSQL(useDivision));
                sql.append("                     and ship_detail.delete_date is null");
                sql.append("                 group by");
                sql.append("                      ship.supplier_id");
                sql.append("                     ,ship_detail.item_id");
                sql.append("             ) Syuko");
                sql.append("             on mst_supplier_item.supplier_id = Syuko.supplier_id");
                sql.append("         and mst_supplier_item.item_id = Syuko.item_id");
                
                if (useDivision == 1) {
                    sql.append("     left join");
                    sql.append("         (");
                    sql.append("             select");
                    sql.append("                  sales_detail.product_id");
                    sql.append("                 ,sum(sales_detail.product_num) as item_num");
                    sql.append("                 ,max(m_si.cost_price) as cost_price");
                    sql.append("             from");
                    sql.append("                 data_sales_detail sales_detail");
                    sql.append("                     inner join data_sales sales");
                    sql.append("                             on sales_detail.shop_id = sales.shop_id");
                    sql.append("                            and sales_detail.slip_no = sales.slip_no");
                    sql.append("                     inner join mst_supplier_item m_si");
                    sql.append("                             on m_si.item_id = sales_detail.product_id");
                    sql.append("                            and m_si.delete_date is null");
                    sql.append("             where");
                    sql.append("                     sales.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()));
                    
                    if (fromDate != null && !fromDate.equals(toDate)) {
                        sql.append("             and sales.sales_date between "+ SQLUtil.convertForSQLDateOnly(fromDate) + " and " + SQLUtil.convertForSQLDateOnly(toDate));
                    } else {
                        sql.append("             and sales.sales_date <= " + SQLUtil.convertForSQLDateOnly(toDate));
                    }

                    sql.append("                 and sales.delete_date is null");
                    sql.append("                 and sales_detail.product_division = 2"); //商品
                    sql.append("                 and sales_detail.delete_date is null");
                    sql.append("             group by");
                    sql.append("                     sales_detail.product_id");
                    sql.append("         ) Sales0");
                    sql.append("         on mst_supplier_item.item_id = Sales0.product_id");
                }
                
                //Sales:販売数を求めるクエリ
                sql.append("         left join");
                sql.append("         (");
                sql.append("             select");
                sql.append("                  sales_detail.item_id");
                sql.append("                 ,sum(sales_detail.item_num) as item_num");
                sql.append("             from");
                sql.append("                 data_staff_sales_detail sales_detail");
                sql.append("                     inner join data_staff_sales sales");
                sql.append("                             on sales_detail.shop_id = sales.shop_id");
                sql.append("                            and sales_detail.slip_no = sales.slip_no");
                sql.append("             where");
                sql.append("                     sales.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()));
                
                if (fromDate != null && !fromDate.equals(toDate)) {
                    sql.append("             and sales.sales_date between "+ SQLUtil.convertForSQLDateOnly(fromDate) + " and " + SQLUtil.convertForSQLDateOnly(toDate));
		} else {
                    sql.append("             and sales.sales_date <= " + SQLUtil.convertForSQLDateOnly(toDate));
		}

                sql.append("                 and sales.delete_date is null");
                sql.append("                 and item_use_division = " + SQLUtil.convertForSQL(useDivision));
                sql.append("                 and sales_detail.delete_date is null");
                sql.append("             group by");
                sql.append("                 sales_detail.item_id");
                sql.append("         ) Sales");
                sql.append("         on mst_supplier_item.item_id = Sales.item_id");
		
		if (inventoryID != null) {
                    //Zaiko:実在庫数を求めるクエリ（入力済みの棚卸データ）
                    sql.append("     left join");
                    sql.append("         (");
                    sql.append("             select");
                    sql.append("                  data_inventory_detail.item_id");
                    sql.append("                 ,data_inventory_detail.real_stock");
                    sql.append("                 ,data_inventory.fixed");
                    sql.append("                 ,data_inventory_detail.cost_price");
                    sql.append("             from");
                    sql.append("                 data_inventory_detail");
                    sql.append("                     inner join data_inventory");
                    sql.append("                             on data_inventory_detail.inventory_id = data_inventory.inventory_id");
                    sql.append("                            and data_inventory_detail.inventory_division = data_inventory.inventory_division");
                    sql.append("             where");
                    sql.append("                     data_inventory.inventory_id = " + SQLUtil.convertForSQL(inventoryID));
                    sql.append("                 and data_inventory_detail.inventory_division = " + SQLUtil.convertForSQL(useDivision));
                    sql.append("                 and data_inventory.delete_date is null");
                    sql.append("                 and data_inventory_detail.delete_date is null");
                    sql.append("         ) zaiko");
                    sql.append("         on mst_supplier_item.item_id = zaiko.item_id");
		}

                sql.append(" where");
                sql.append("         mst_supplier_item.delete_date is null");
                
		//仕入先が指定された場合
		if (supplierID != null) {
                    sql.append(" and mst_supplier_item.supplier_id = " + SQLUtil.convertForSQL(supplierID));
		}

                //TMTrong start add 20150701 New request #38257
                if(isInStock == 1){
                    if(useDivision == 1){
                        sql.append(" AND (coalesce(Kisyu.real_stock,0) > 0 OR coalesce(Nyuko.in_num,0) > 0 OR (coalesce(Syuko.out_num, 0) + coalesce(Sales0.item_num, 0) + coalesce(Sales.item_num, 0) > 0) ");
                        sql.append(" OR (coalesce(Kisyu.real_stock,0) + coalesce(Nyuko.in_num,0)  + coalesce(Nyuko.attach_num,0) - (coalesce(Syuko.out_num, 0))) > 0) ");
                    }
                    else{
                        sql.append(" AND (coalesce(Kisyu.real_stock,0) > 0 OR coalesce(Nyuko.in_num,0) > 0 OR (coalesce(Syuko.out_num, 0) + coalesce(Sales.item_num, 0) > 0) ");
                        sql.append(" OR (coalesce(Kisyu.real_stock,0) + coalesce(Nyuko.in_num,0)  + coalesce(Nyuko.attach_num,0) - (coalesce(Syuko.out_num, 0))) > 0) ");
                    }
                }
                else if(isInStock == 2){
                    if(useDivision == 1){
                        //IVS_LVTu start edit 2015/07/22 Bug #40902
                        //sql.append(" AND (coalesce(Kisyu.real_stock,0) <= 0 AND coalesce(Nyuko.in_num,0) <= 0 AND (coalesce(Syuko.out_num, 0) + coalesce(Sales0.item_num, 0) + coalesce(Sales.item_num, 0) <=0) ) ");
                        sql.append(" AND (coalesce(Kisyu.real_stock,0) = 0 AND coalesce(Nyuko.in_num,0) = 0 AND (coalesce(Syuko.out_num, 0) + coalesce(Sales0.item_num, 0) + coalesce(Sales.item_num, 0) =0) ) ");
                    }else {
                        //sql.append(" AND (coalesce(Kisyu.real_stock,0) <= 0 AND coalesce(Nyuko.in_num,0) <= 0 AND (coalesce(Syuko.out_num, 0) + coalesce(Sales.item_num, 0) <=0) ) ");
                        sql.append(" AND (coalesce(Kisyu.real_stock,0) = 0 AND coalesce(Nyuko.in_num,0) = 0 AND (coalesce(Syuko.out_num, 0) + coalesce(Sales.item_num, 0) =0) ) ");
                        //IVS_LVTu end edit 2015/07/22 Bug #40902
                    }
                }
                //TMTrong end edit 20150701 New request #38257
                
                sql.append(" ) t");

                if (fixed.equals(1)) {
                    sql.append(" where jitsu_zaiko is not null");
                }

                sql.append(" order by");
                sql.append("     item_seq");
		
		return sql.toString();
	}

	/**
	 * 商品の分類でソート
	 */
	public void sortByClass()
	{
		
		Collections.sort(this, new Comparator<InventryData>()
		{
			public int compare(InventryData id1, InventryData id2)
			{
				//分類順を比較
				if (id1.getItemClassSeq() < id2.getItemClassSeq()) return -1;
				if (id1.getItemClassSeq() > id2.getItemClassSeq()) return 1;
				if (id1.getItemClassSeq() == id2.getItemClassSeq())
				{
					//分類が同じなら商品の表示順
					if (id1.getItemSeq() < id2.getItemSeq()) return -1;
					if (id1.getItemSeq() > id2.getItemSeq()) return 1;
					return 0;
				}
				return 0;
			}
		});
	}
	
	/**
	 * 商品の置き場でソート
	 */
	public void sortByPlace()
	{
		
		Collections.sort(this, new Comparator<InventryData>()
		{
			public int compare(InventryData id1, InventryData id2)
			{
				if (id1.getPlaceSeq() < id2.getPlaceSeq()) return -1;
				if (id1.getPlaceSeq() > id2.getPlaceSeq()) return 1;
				if (id1.getPlaceSeq() == id2.getPlaceSeq())
				{
					//置き場順が同じなら商品分類の表示順
					if (id1.getItemClassSeq() < id2.getItemClassSeq()) return -1;
					if (id1.getItemClassSeq() > id2.getItemClassSeq()) return 1;
					//商品の表示順
					if (id1.getItemSeq() < id2.getItemSeq()) return -1;
					if (id1.getItemSeq() > id2.getItemSeq()) return 1;
					return 0;
				}
				return 0;
			}
		});
	}
	
	/**
	 * 更新の有無
	 */
	public boolean isUpdate()
	{
		for(InventryData id : this)
		{
			if (id.isUpdate())
			{
				return true;
			}
		}
		
		return false;
	}

	/**
	 * 更新有無フラグを強制書き換え
	 */
	public void setUpdate(boolean val)
	{
		for(InventryData id : this)
		{
			id.setUpdate(val);
		}
	}
	
	/**
	 * 条件変更の有無
	 */
	public boolean isConditionChange()
	{
		return conditionChange;
	}
	
	/**
	 * 商品IDからリスト内のindexを求める
	 */
	public int indexOfItem(Integer itemID)
	{
		
		for (int i = 0; i < this.size(); i++)
		{
			if (this.get(i).getItemID().equals(itemID))
			{
				return i;
			}
		}
		
		return -1;
	}
    //TMTrong start add 20150630 New request #38257   
    /**
     * @return the isInStock
     */
    public Integer isInStock() {
        return isInStock;
    }

    /**
     * @param inStock the isInStock to set
     */
    public void setInStock(Integer inStock) {
        this.isInStock = inStock;
        this.conditionChange=true;
    }
    //TMTrong end add 20150630 New request #38257
	
}
