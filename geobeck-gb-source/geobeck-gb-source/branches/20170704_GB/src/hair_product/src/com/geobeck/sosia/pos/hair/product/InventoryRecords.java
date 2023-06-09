/*
 * InventoryRecords.java
 *
 * Created on 2008/10/03, 15:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product;

import com.geobeck.sosia.pos.hair.data.commodity.DataInventory;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * 棚卸ヘッダ情報管理
 * @author syouji
 */
public class InventoryRecords
{
	
	/**
	 * 店舗
	 */
	protected MstShop       shop		=	new MstShop();
	protected Integer		inventoryDivision;
	
	protected	ArrayList<DataInventory>	dataInventorys		=	null;
	protected   Integer currentIndex    =   null;
	
	/** Creates a new instance of InventoryRecords */
	public InventoryRecords()
	{
	}
	
	/**
	 * 店舗
	 * @return 店舗
	 */
	public MstShop getShop()
	{
		return this.shop;
	}
	
	/**
	 * 店舗
	 * @param shop
	 */
	public void setShop(MstShop shop)
	{
		this.shop = shop;
	}

	public Integer getInventoryDivision()
	{
		return this.inventoryDivision;
	}

	public void setInventoryDivision(Integer inventoryDivision)
	{
		this.inventoryDivision = inventoryDivision;
	}

	/**
	 * 棚卸データを読み込む。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void load(ConnectionWrapper con) throws SQLException
	{
		//設定されている店舗の棚卸を取得（過去の履歴全て）
		dataInventorys = new ArrayList<DataInventory>();
		currentIndex = null;
		
		ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());
		
		while(rs.next())
		{
			DataInventory di = new DataInventory();
			di.setData(rs);
			dataInventorys.add(di);
		}
		rs.close();
		
	}
	
	/**
	 * 棚卸データを取得するＳＱＬ文を取得する。
	 * @return ＳＱＬ文
	 */
	private String getSelectSQL()
	{
		StringBuilder buf = new StringBuilder();
		buf.append(" SELECT\n")
			.append("     data_inventory.*\n")
			.append(" FROM\n")
			.append("     data_inventory\n")
			.append(" WHERE\n")
			.append("     shop_id = ").append(SQLUtil.convertForSQL(shop.getShopID())).append("\n")
			.append(" AND\n")
			.append("     delete_date is null\n");

                // 2011-01-14 ADD
                if (inventoryDivision != null) {
                    buf.append(" AND\n")
                            .append("     inventory_division = ").append(SQLUtil.convertForSQL(inventoryDivision)).append("\n");
                }

/*
		if (getInventoryDivision() != null)
		{
			buf.append(" AND\n")
				.append(" inventory_id in (\n")
					.append(" select distinct inventory_id from data_inventory_detail\n")
					.append(" where inventory_division = ").append(getInventoryDivision()).append("\n")
					.append(" and delete_date is null\n")
				.append(")\n");
		}
 */
		
		buf.append(" order by\n")
			.append("     inventory_date DESC\n");
		
		return	new String(buf);
	}
	
	
	/**
	 * 棚卸期間の初期値を求める
	 * @param fromDate 棚卸期間FROM
	 * @param toDate 棚卸期間TO
	 */
	public void getInitialSpan(Calendar fromCal, Calendar toCal)
	{
		if (dataInventorys == null) return;
		if (dataInventorys.size() > 0)
		{
			//最新の棚卸を参照
			DataInventory di = dataInventorys.get(0);
			
			if (di.getFixed() == 1)
			{
				//確定済み
				currentIndex = null;
				
				//棚卸期間 FROM 前回の棚卸日＋１日
				fromCal.setTime(di.getInventoryDate());
				fromCal.add(Calendar.DATE, 1);
				
				//棚卸期間 TO   店舗の締日
				toCal.setTime(fromCal.getTime());
				if (toCal.get(Calendar.DAY_OF_MONTH) < shop.getCutoffDay())
				{
					//当月の締め日を設定
					adjustCutoffDay(toCal, shop.getCutoffDay());
				}
				else
				{
					//翌月の締め日を設定
					toCal.add(Calendar.MONTH, 1);
					adjustCutoffDay(toCal, shop.getCutoffDay());
				}
				
			}
			else
			{
				//一時保存のデータがある
				currentIndex = 0;
				
				//棚卸期間 TO は一時保存の日付を使用
				toCal.setTime(di.getInventoryDate());
				
				//棚卸期間 FROM はひとつ前の棚卸データより取得
				if (dataInventorys.size() > 1)
				{
					DataInventory diPrev = dataInventorys.get(1);
					//棚卸期間 FROM 前回の棚卸日＋１日
					fromCal.setTime(diPrev.getInventoryDate());
					fromCal.add(Calendar.DATE, 1);
					
				}
				else
				{
					//一時保存から前の棚卸が無い
					fromCal.setTime(toCal.getTime());
				}
			}
		}
		else
		{
			//まだ一度も棚卸されていない
			//From To に当日をセット
			Calendar now = Calendar.getInstance();
			fromCal.setTime(now.getTime());
			toCal.setTime(now.getTime());
			currentIndex = null;
		}
		
		return;
	}
	
	/**
	 * 締日の調整
	 * @param cal 日時情報
	 * @param cutoffDay 締日
	 */
	private void adjustCutoffDay(Calendar cal, Integer cutoffDay)
	{
		
		//月末日の取得
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		if (lastDay < cutoffDay)
		{
			//締日の定義が月末を超えている場合は月末を設定する
			cal.set(Calendar.DAY_OF_MONTH, lastDay);
		}
		else
		{
			//締日に合わせる
			cal.set(Calendar.DAY_OF_MONTH, cutoffDay);
		}
		
	}
	
	
	/**
	 * 画面で指定された棚卸期間(TO)を基準に過去の棚卸期間を求める
	 * @param baseDate 指定日
	 * @param fromCal 棚卸期間FROM
	 * @param toCal 棚卸期間TO
	 * @param current_di 棚卸データ
	 */
	public boolean getExistedSpan(Date baseDate, Calendar fromCal, Calendar toCal)
	{
		
		if (dataInventorys == null) return false;
		if (dataInventorys.size() > 0)
		{
			//指定日が過去のどの棚卸データに該当するか検索する
			int index = 0;
			while (index < dataInventorys.size())
			{
				
				DataInventory di = dataInventorys.get(index);
				
				int comp = DateRange.compareToDateOnly(baseDate, (java.util.Date)di.getInventoryDate());
				if (comp == 0)
				{
					break;
				}
				else if (comp > 0)
				{
					index--;
					break;
				}
				
				index++;
			}
			if (index == dataInventorys.size())
			{
				index = dataInventorys.size() - 1;
			}
			
			DataInventory diSel = null;
			
			if (index < 0)
			{
				//既存の棚卸期間には該当しない
				currentIndex = null;
				toCal.setTime(baseDate);
			}
			else
			{
				diSel = dataInventorys.get(index);
				currentIndex = index;

//				//期間TOは棚卸終了日
//				if (diSel.getFixed() == 1)
//				{
//					toCal.setTime(diSel.getInventoryDate());
//				}
//				else
//				{
//					//確定されていない場合は指定日を維持
//					toCal.setTime(baseDate);
//					
//					if (DateRange.diffDays(fromCal.getTime(), toCal.getTime()) > 31)
//					{
//						//前回棚卸終了日＋１から３１日以上の範囲は設定不可とする
//						toCal.setTime(fromCal.getTime());
//						toCal.add(Calendar.DATE, 30);
//					}
//				}

				toCal.setTime(diSel.getInventoryDate());
			}

			//期間FROMは、前の棚卸終了日から求める
			if ((index + 1) < dataInventorys.size() )
			{
				diSel = dataInventorys.get(index + 1);
				
				//棚卸期間 FROM 前回の棚卸日＋１日
				fromCal.setTime(diSel.getInventoryDate());
				fromCal.add(Calendar.DATE, 1);
				
				if (DateRange.diffDays(fromCal.getTime(), toCal.getTime()) > 31)
				{
					//前回棚卸終了日＋１から３１日以上の範囲は設定不可とする
					toCal.setTime(fromCal.getTime());
					toCal.add(Calendar.DATE, 30);
				}
				
			}
			else
			{
				fromCal.setTime(toCal.getTime());
			}
		}
		else
		{
			currentIndex = null;
			fromCal.setTime(baseDate);
			toCal.setTime(baseDate);
		}
		
		return true;
		
	}
	
	public DataInventory getCurrentInventry()
	{
		if (currentIndex != null)
		{
			return dataInventorys.get(currentIndex);
		}
		else
		{
			return null;
		}
	}

	public void setCurrentInventry(Integer inventoryID)
	{
		int index = 0;
		while (index < dataInventorys.size())
		{
			
			if (dataInventorys.get(index).getInventoryId() == inventoryID)
			{
				currentIndex = index;
				return;
			}
			index++;
		}
	}

	public DataInventory getDataInventory(Date date)
	{
		DataInventory prev = null;
		for (DataInventory d : dataInventorys)
		{
			int c = date.compareTo(d.getInventoryDate());
			if (c == 0)
			{
				return d;
			}
			else if (c > 0)
			{
				return prev;
			}

			prev = d;
		}

		return prev;
	}

	/**
	 *
	 */
	public DataInventory[] getInventorys()
	{
		return dataInventorys.toArray(new DataInventory[dataInventorys.size()]);
	}

	/**
	 * 棚卸の更新にパスワードが必要かチェックする
	 */
	public boolean isPasswordRequired(Integer inventryID, int acceptBeforeCount)
	{
		if (acceptBeforeCount < 0)
		{
			return false;
		}

		boolean required = false;
		int fixedCount = 0;
		for (int ii = 0; ii < dataInventorys.size(); ii++)
		{
			DataInventory di = dataInventorys.get(ii);
			if (di.getFixed() == 1)
			{
				fixedCount++;
			}

			if (dataInventorys.get(ii).getInventoryId().equals(inventryID))
			{
				if (fixedCount > acceptBeforeCount)
				{
					//前々回の確定済み棚卸以前はNG（要パスワード）
					required = true;
				}
				break;
			}
		}
		return required;
	}

	public boolean isEditable(Integer inventryID, int acceptBeforeCount)
	{
		if (acceptBeforeCount < 0)
		{
			return true;
		}

		boolean editable = true;
		int fixedCount = 0;
		for (int ii = 0; ii < dataInventorys.size(); ii++)
		{
			DataInventory di = dataInventorys.get(ii);
			if (di.getFixed() == 1)
			{
				fixedCount++;
			}

			if (dataInventorys.get(ii).getInventoryId().equals(inventryID))
			{
				if (fixedCount > acceptBeforeCount)
				{
					//編集不可
					editable = false;
				}
				break;
			}
		}
		return editable;
	}
}
