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
 * �I���w�b�_���Ǘ�
 * @author syouji
 */
public class InventoryRecords
{
	
	/**
	 * �X��
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
	 * �X��
	 * @return �X��
	 */
	public MstShop getShop()
	{
		return this.shop;
	}
	
	/**
	 * �X��
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
	 * �I���f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void load(ConnectionWrapper con) throws SQLException
	{
		//�ݒ肳��Ă���X�܂̒I�����擾�i�ߋ��̗���S�āj
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
	 * �I���f�[�^���擾����r�p�k�����擾����B
	 * @return �r�p�k��
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
	 * �I�����Ԃ̏����l�����߂�
	 * @param fromDate �I������FROM
	 * @param toDate �I������TO
	 */
	public void getInitialSpan(Calendar fromCal, Calendar toCal)
	{
		if (dataInventorys == null) return;
		if (dataInventorys.size() > 0)
		{
			//�ŐV�̒I�����Q��
			DataInventory di = dataInventorys.get(0);
			
			if (di.getFixed() == 1)
			{
				//�m��ς�
				currentIndex = null;
				
				//�I������ FROM �O��̒I�����{�P��
				fromCal.setTime(di.getInventoryDate());
				fromCal.add(Calendar.DATE, 1);
				
				//�I������ TO   �X�܂̒���
				toCal.setTime(fromCal.getTime());
				if (toCal.get(Calendar.DAY_OF_MONTH) < shop.getCutoffDay())
				{
					//�����̒��ߓ���ݒ�
					adjustCutoffDay(toCal, shop.getCutoffDay());
				}
				else
				{
					//�����̒��ߓ���ݒ�
					toCal.add(Calendar.MONTH, 1);
					adjustCutoffDay(toCal, shop.getCutoffDay());
				}
				
			}
			else
			{
				//�ꎞ�ۑ��̃f�[�^������
				currentIndex = 0;
				
				//�I������ TO �͈ꎞ�ۑ��̓��t���g�p
				toCal.setTime(di.getInventoryDate());
				
				//�I������ FROM �͂ЂƂO�̒I���f�[�^���擾
				if (dataInventorys.size() > 1)
				{
					DataInventory diPrev = dataInventorys.get(1);
					//�I������ FROM �O��̒I�����{�P��
					fromCal.setTime(diPrev.getInventoryDate());
					fromCal.add(Calendar.DATE, 1);
					
				}
				else
				{
					//�ꎞ�ۑ�����O�̒I��������
					fromCal.setTime(toCal.getTime());
				}
			}
		}
		else
		{
			//�܂���x���I������Ă��Ȃ�
			//From To �ɓ������Z�b�g
			Calendar now = Calendar.getInstance();
			fromCal.setTime(now.getTime());
			toCal.setTime(now.getTime());
			currentIndex = null;
		}
		
		return;
	}
	
	/**
	 * �����̒���
	 * @param cal �������
	 * @param cutoffDay ����
	 */
	private void adjustCutoffDay(Calendar cal, Integer cutoffDay)
	{
		
		//�������̎擾
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		if (lastDay < cutoffDay)
		{
			//�����̒�`�������𒴂��Ă���ꍇ�͌�����ݒ肷��
			cal.set(Calendar.DAY_OF_MONTH, lastDay);
		}
		else
		{
			//�����ɍ��킹��
			cal.set(Calendar.DAY_OF_MONTH, cutoffDay);
		}
		
	}
	
	
	/**
	 * ��ʂŎw�肳�ꂽ�I������(TO)����ɉߋ��̒I�����Ԃ����߂�
	 * @param baseDate �w���
	 * @param fromCal �I������FROM
	 * @param toCal �I������TO
	 * @param current_di �I���f�[�^
	 */
	public boolean getExistedSpan(Date baseDate, Calendar fromCal, Calendar toCal)
	{
		
		if (dataInventorys == null) return false;
		if (dataInventorys.size() > 0)
		{
			//�w������ߋ��̂ǂ̒I���f�[�^�ɊY�����邩��������
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
				//�����̒I�����Ԃɂ͊Y�����Ȃ�
				currentIndex = null;
				toCal.setTime(baseDate);
			}
			else
			{
				diSel = dataInventorys.get(index);
				currentIndex = index;

//				//����TO�͒I���I����
//				if (diSel.getFixed() == 1)
//				{
//					toCal.setTime(diSel.getInventoryDate());
//				}
//				else
//				{
//					//�m�肳��Ă��Ȃ��ꍇ�͎w������ێ�
//					toCal.setTime(baseDate);
//					
//					if (DateRange.diffDays(fromCal.getTime(), toCal.getTime()) > 31)
//					{
//						//�O��I���I�����{�P����R�P���ȏ�͈̔͂͐ݒ�s�Ƃ���
//						toCal.setTime(fromCal.getTime());
//						toCal.add(Calendar.DATE, 30);
//					}
//				}

				toCal.setTime(diSel.getInventoryDate());
			}

			//����FROM�́A�O�̒I���I�������狁�߂�
			if ((index + 1) < dataInventorys.size() )
			{
				diSel = dataInventorys.get(index + 1);
				
				//�I������ FROM �O��̒I�����{�P��
				fromCal.setTime(diSel.getInventoryDate());
				fromCal.add(Calendar.DATE, 1);
				
				if (DateRange.diffDays(fromCal.getTime(), toCal.getTime()) > 31)
				{
					//�O��I���I�����{�P����R�P���ȏ�͈̔͂͐ݒ�s�Ƃ���
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
	 * �I���̍X�V�Ƀp�X���[�h���K�v���`�F�b�N����
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
					//�O�X��̊m��ςݒI���ȑO��NG�i�v�p�X���[�h�j
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
					//�ҏW�s��
					editable = false;
				}
				break;
			}
		}
		return editable;
	}
}
