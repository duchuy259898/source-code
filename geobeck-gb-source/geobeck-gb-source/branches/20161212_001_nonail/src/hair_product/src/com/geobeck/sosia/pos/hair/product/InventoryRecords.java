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
 * ’I‰µƒwƒbƒ_î•ñŠÇ—
 * @author syouji
 */
public class InventoryRecords
{
	
	/**
	 * “X•Ü
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
	 * “X•Ü
	 * @return “X•Ü
	 */
	public MstShop getShop()
	{
		return this.shop;
	}
	
	/**
	 * “X•Ü
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
	 * ’I‰µƒf[ƒ^‚ğ“Ç‚İ‚ŞB
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void load(ConnectionWrapper con) throws SQLException
	{
		//İ’è‚³‚ê‚Ä‚¢‚é“X•Ü‚Ì’I‰µ‚ğæ“¾i‰ß‹‚Ì—š—ğ‘S‚Äj
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
	 * ’I‰µƒf[ƒ^‚ğæ“¾‚·‚é‚r‚p‚k•¶‚ğæ“¾‚·‚éB
	 * @return ‚r‚p‚k•¶
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
	 * ’I‰µŠúŠÔ‚Ì‰Šú’l‚ğ‹‚ß‚é
	 * @param fromDate ’I‰µŠúŠÔFROM
	 * @param toDate ’I‰µŠúŠÔTO
	 */
	public void getInitialSpan(Calendar fromCal, Calendar toCal)
	{
		if (dataInventorys == null) return;
		if (dataInventorys.size() > 0)
		{
			//ÅV‚Ì’I‰µ‚ğQÆ
			DataInventory di = dataInventorys.get(0);
			
			if (di.getFixed() == 1)
			{
				//Šm’èÏ‚İ
				currentIndex = null;
				
				//’I‰µŠúŠÔ FROM ‘O‰ñ‚Ì’I‰µ“ú{‚P“ú
				fromCal.setTime(di.getInventoryDate());
				fromCal.add(Calendar.DATE, 1);
				
				//’I‰µŠúŠÔ TO   “X•Ü‚Ì’÷“ú
				toCal.setTime(fromCal.getTime());
				if (toCal.get(Calendar.DAY_OF_MONTH) < shop.getCutoffDay())
				{
					//“–Œ‚Ì’÷‚ß“ú‚ğİ’è
					adjustCutoffDay(toCal, shop.getCutoffDay());
				}
				else
				{
					//—‚Œ‚Ì’÷‚ß“ú‚ğİ’è
					toCal.add(Calendar.MONTH, 1);
					adjustCutoffDay(toCal, shop.getCutoffDay());
				}
				
			}
			else
			{
				//ˆê•Û‘¶‚Ìƒf[ƒ^‚ª‚ ‚é
				currentIndex = 0;
				
				//’I‰µŠúŠÔ TO ‚Íˆê•Û‘¶‚Ì“ú•t‚ğg—p
				toCal.setTime(di.getInventoryDate());
				
				//’I‰µŠúŠÔ FROM ‚Í‚Ğ‚Æ‚Â‘O‚Ì’I‰µƒf[ƒ^‚æ‚èæ“¾
				if (dataInventorys.size() > 1)
				{
					DataInventory diPrev = dataInventorys.get(1);
					//’I‰µŠúŠÔ FROM ‘O‰ñ‚Ì’I‰µ“ú{‚P“ú
					fromCal.setTime(diPrev.getInventoryDate());
					fromCal.add(Calendar.DATE, 1);
					
				}
				else
				{
					//ˆê•Û‘¶‚©‚ç‘O‚Ì’I‰µ‚ª–³‚¢
					fromCal.setTime(toCal.getTime());
				}
			}
		}
		else
		{
			//‚Ü‚¾ˆê“x‚à’I‰µ‚³‚ê‚Ä‚¢‚È‚¢
			//From To ‚É“–“ú‚ğƒZƒbƒg
			Calendar now = Calendar.getInstance();
			fromCal.setTime(now.getTime());
			toCal.setTime(now.getTime());
			currentIndex = null;
		}
		
		return;
	}
	
	/**
	 * ’÷“ú‚Ì’²®
	 * @param cal “úî•ñ
	 * @param cutoffDay ’÷“ú
	 */
	private void adjustCutoffDay(Calendar cal, Integer cutoffDay)
	{
		
		//Œ––“ú‚Ìæ“¾
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		if (lastDay < cutoffDay)
		{
			//’÷“ú‚Ì’è‹`‚ªŒ––‚ğ’´‚¦‚Ä‚¢‚éê‡‚ÍŒ––‚ğİ’è‚·‚é
			cal.set(Calendar.DAY_OF_MONTH, lastDay);
		}
		else
		{
			//’÷“ú‚É‡‚í‚¹‚é
			cal.set(Calendar.DAY_OF_MONTH, cutoffDay);
		}
		
	}
	
	
	/**
	 * ‰æ–Ê‚Åw’è‚³‚ê‚½’I‰µŠúŠÔ(TO)‚ğŠî€‚É‰ß‹‚Ì’I‰µŠúŠÔ‚ğ‹‚ß‚é
	 * @param baseDate w’è“ú
	 * @param fromCal ’I‰µŠúŠÔFROM
	 * @param toCal ’I‰µŠúŠÔTO
	 * @param current_di ’I‰µƒf[ƒ^
	 */
	public boolean getExistedSpan(Date baseDate, Calendar fromCal, Calendar toCal)
	{
		
		if (dataInventorys == null) return false;
		if (dataInventorys.size() > 0)
		{
			//w’è“ú‚ª‰ß‹‚Ì‚Ç‚Ì’I‰µƒf[ƒ^‚ÉŠY“–‚·‚é‚©ŒŸõ‚·‚é
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
				//Šù‘¶‚Ì’I‰µŠúŠÔ‚É‚ÍŠY“–‚µ‚È‚¢
				currentIndex = null;
				toCal.setTime(baseDate);
			}
			else
			{
				diSel = dataInventorys.get(index);
				currentIndex = index;

//				//ŠúŠÔTO‚Í’I‰µI—¹“ú
//				if (diSel.getFixed() == 1)
//				{
//					toCal.setTime(diSel.getInventoryDate());
//				}
//				else
//				{
//					//Šm’è‚³‚ê‚Ä‚¢‚È‚¢ê‡‚Íw’è“ú‚ğˆÛ
//					toCal.setTime(baseDate);
//					
//					if (DateRange.diffDays(fromCal.getTime(), toCal.getTime()) > 31)
//					{
//						//‘O‰ñ’I‰µI—¹“ú{‚P‚©‚ç‚R‚P“úˆÈã‚Ì”ÍˆÍ‚Íİ’è•s‰Â‚Æ‚·‚é
//						toCal.setTime(fromCal.getTime());
//						toCal.add(Calendar.DATE, 30);
//					}
//				}

				toCal.setTime(diSel.getInventoryDate());
			}

			//ŠúŠÔFROM‚ÍA‘O‚Ì’I‰µI—¹“ú‚©‚ç‹‚ß‚é
			if ((index + 1) < dataInventorys.size() )
			{
				diSel = dataInventorys.get(index + 1);
				
				//’I‰µŠúŠÔ FROM ‘O‰ñ‚Ì’I‰µ“ú{‚P“ú
				fromCal.setTime(diSel.getInventoryDate());
				fromCal.add(Calendar.DATE, 1);
				
				if (DateRange.diffDays(fromCal.getTime(), toCal.getTime()) > 31)
				{
					//‘O‰ñ’I‰µI—¹“ú{‚P‚©‚ç‚R‚P“úˆÈã‚Ì”ÍˆÍ‚Íİ’è•s‰Â‚Æ‚·‚é
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
	 * ’I‰µ‚ÌXV‚ÉƒpƒXƒ[ƒh‚ª•K—v‚©ƒ`ƒFƒbƒN‚·‚é
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
					//‘OX‰ñ‚ÌŠm’èÏ‚İ’I‰µˆÈ‘O‚ÍNGi—vƒpƒXƒ[ƒhj
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
					//•ÒW•s‰Â
					editable = false;
				}
				break;
			}
		}
		return editable;
	}
}
