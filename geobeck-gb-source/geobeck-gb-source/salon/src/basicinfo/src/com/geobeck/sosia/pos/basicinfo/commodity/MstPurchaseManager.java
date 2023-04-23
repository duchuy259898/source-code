/*
 * MstPurchaseManager.java
 *
 * Created on 2007/04/03, 20:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.basicinfo.commodity;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.master.product.*;
import com.geobeck.sosia.pos.master.commodity.*;

/**
 *
 * @author katagiri
 */
public class MstPurchaseManager
{
	/**
	 * �R���X�g���N�^
	 */
	public MstPurchaseManager(MstSupplier supplier)
	{
		this.setSupplier(supplier);
		itemClasses	=	new MstItemClasses();
		reference	=	new ArrayList<ArrayList<MstPurchase>>();
		selected	=	new ArrayList<ArrayList<MstPurchase>>();
		this.loadItemClasses();
		this.initArrays();
		this.loadPurchases();
	}
	
	/**
	 * �d����
	 */
	private MstSupplier			supplier		=	null;
	
	/**
	 * ���i����
	 */
	private	MstItemClasses						itemClasses		=	null;
	/**
	 * �Q�Ə��i
	 */
	private	ArrayList<ArrayList<MstPurchase>>	reference		=	null;
	/**
	 * �I�����i
	 */
	private	ArrayList<ArrayList<MstPurchase>>	selected		=	null;
	
	/**
	 * �I������Ă��鏤�i���ނ̃C���f�b�N�X
	 */
	private	Integer				selectedIndex	=	-1;

	/**
	 * �d������擾����B
	 * @return �d����
	 */
	public MstSupplier getSupplier()
	{
		return supplier;
	}

	/**
	 * �d�����ݒ肷��B
	 * @param supplier �d����
	 */
	public void setSupplier(MstSupplier supplier)
	{
		this.supplier = supplier;
	}

	/**
	 * ���i���ނ��擾����B
	 * @return ���i����
	 */
	public MstItemClasses getItemClasses()
	{
		return itemClasses;
	}

	/**
	 * ���i���ނ�ݒ肷��B
	 * @param itemClasses ���i����
	 */
	public void setItemClasses(MstItemClasses itemClasses)
	{
		this.itemClasses = itemClasses;
	}

	/**
	 * �Q�Ə��i���擾����B
	 * @return �Q�Ə��i
	 */
	public ArrayList<ArrayList<MstPurchase>> getReference()
	{
		return reference;
	}

	/**
	 * �Q�Ə��i��ݒ肷��B
	 * @param reference �Q�Ə��i
	 */
	public void setReference(ArrayList<ArrayList<MstPurchase>> reference)
	{
		this.reference = reference;
	}

	/**
	 * �I�����i���擾����B
	 * @return �I�����i
	 */
	public ArrayList<ArrayList<MstPurchase>> getSelected()
	{
		return selected;
	}

	/**
	 * �I�����i��ݒ肷��B
	 * @param selected �I�����i
	 */
	public void setSelected(ArrayList<ArrayList<MstPurchase>> selected)
	{
		this.selected = selected;
	}

	/**
	 * �I������Ă��鏤�i���ނ̃C���f�b�N�X���擾����B
	 * @return �I������Ă��鏤�i���ނ̃C���f�b�N�X
	 */
	public Integer getSelectedIndex()
	{
		return selectedIndex;
	}

	/**
	 * �I������Ă��鏤�i���ނ̃C���f�b�N�X��ݒ肷��B
	 * @param selectedIndex �I������Ă��鏤�i���ނ̃C���f�b�N�X
	 */
	public void setSelectedIndex(Integer selectedIndex)
	{
		this.selectedIndex = selectedIndex;
	}
	
	/**
	 * ���i���ނ�ǂݍ��ށB
	 * @return true - �����Afalse - ���s
	 */
	private boolean loadItemClasses()
	{
		boolean		result	=	false;
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			itemClasses.load(con);
			
			for(MstItemClass mic : itemClasses)
			{
				mic.loadItem(con);
			}
			
			result	=	true;
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}
	
	/**
	 * �z�������������B
	 * @return true - �����Afalse - ���s
	 */
	private boolean initArrays()
	{
		boolean		result	=	false;
		
		reference.clear();
		
		for(MstItemClass mic : itemClasses)
		{
			ArrayList<MstPurchase>	temp	=	new ArrayList<MstPurchase>();
			
			for(MstItem	mi : mic)
			{
				MstPurchase		mp	=	new MstPurchase(this.getSupplier(), mi);
				temp.add(mp);
			}
			
			reference.add(temp);
			selected.add(new ArrayList<MstPurchase>());
		}
		
		return	result;
	}
	
	
	/**
	 * �d�����i��ǂݍ��ށB
	 * @return true - �����Afalse - ���s
	 */
	private boolean loadPurchases()
	{
		boolean		result	=	false;
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			ResultSetWrapper	rs	=	con.executeQuery(this.getLoadPurchasSQL());
			
			while(rs.next())
			{
				Integer	itemClassIndex	=	this.getItemClassIndexByID(rs.getInt("item_class_id"));
				Integer	itemIndex		=	this.getItemIndexByID(reference.get(itemClassIndex), rs.getInt("item_id"));
				reference.get(itemClassIndex).get(itemIndex).setCostPrice(rs.getInt("cost_price"));
				this.movePurchase(true, itemClassIndex, itemIndex);
			}
			
			rs.close();
			
			result	=	true;
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}
	
	/**
	 * �d�����i��ǂݍ��ނr�p�k�����擾����B
	 * @return �d�����i��ǂݍ��ނr�p�k��
	 */
	private String getLoadPurchasSQL()
	{
		return	"select mic.item_class_id, mi.item_id, mp.cost_price\n" +
				"from mst_purchase mp\n" +
				"inner join mst_item mi\n" +
				"on mi.item_id = mp.item_id\n" +
				"and mi.delete_date is null\n" +
				"inner join mst_item_class mic\n" +
				"on mic.item_class_id = mi.item_class_id\n" +
				"and mic.delete_date is null\n" +
				"where mp.delete_date is null\n" +
				"and mp.supplier_id = " + SQLUtil.convertForSQL(this.getSupplier().getSupplierID()) + "\n" +
				"order by mic.item_class_id, mi.item_id\n";
	}
	
	/**
	 * ���i���ނh�c���珤�i���ނ̃C���f�b�N�X���擾����B
	 * @param itemClassID ���i���ނh�c
	 * @return ���i���ނ̃C���f�b�N�X
	 */
	public Integer getItemClassIndexByID(Integer itemClassID)
	{
		for(Integer i = 0; i < itemClasses.size(); i ++)
		{
			if(itemClasses.get(i).getItemClassID() == itemClassID)
			{
				return	i;
			}
		}
		
		return	-1;
	}
	
	/**
	 * ���i�h�c���珤�i�̃C���f�b�N�X���擾����B
	 * @param itemClass ���i����
	 * @param itemID ���i�h�c
	 * @return ���i�̃C���f�b�N�X
	 */
	public Integer getItemIndexByID(ArrayList<MstPurchase> itemClass, Integer itemID)
	{
		for(Integer i = 0; i < itemClass.size(); i ++)
		{
			if(itemClass.get(i).getItemID() == itemID)
			{
				return	i;
			}
		}
		
		return	-1;
	}
	
	/**
	 * �d�����i��ݒ肷��B
	 * @param itemClassIndex ���i���ނ̃C���f�b�N�X
	 * @param itemIndex ���i�̃C���f�b�N�X
	 * @param costPrice �d�����i
	 */
	public void setCostPrice(Integer itemClassIndex, Integer itemIndex, Integer costPrice)
	{
		if(0 <= itemClassIndex && itemClassIndex < selected.size())
		{
			if(0 <= itemIndex && itemIndex < selected.get(itemClassIndex).size())
			{
				selected.get(itemClassIndex).get(itemIndex).setCostPrice(costPrice);
			}
		}
	}
	
	/**
	 * ���i��I���i�����j����B
	 * @param itemClassIndex ���i���ނ̃C���f�b�N�X
	 * @param itemIndex ���i�̃C���f�b�N�X
	 * @param isSelect true - �I���Afalse - ����
	 */
	public void movePurchase(boolean isSelect, Integer itemClassIndex, Integer itemIndex)
	{
		if(itemClassIndex == null || itemIndex == null)
		{
			return;
		}
		
		ArrayList<ArrayList<MstPurchase>>	from	=	(isSelect ? reference : selected);
		ArrayList<ArrayList<MstPurchase>>	to		=	(isSelect ? selected : reference);
		
		ArrayList<MstPurchase>	pcFrom	=	from.get(itemClassIndex);
		ArrayList<MstPurchase>	pcTo	=	to.get(itemClassIndex);
		
		pcTo.add(pcFrom.remove(itemIndex.intValue()));
		
		this.sort(isSelect, itemClassIndex);
	}
	
	/**
	 * ���i��S�đI���i�����j����B
	 * @param itemClassIndex ���i���ނ̃C���f�b�N�X
	 * @param isSelect true - �I���Afalse - ����
	 */
	public void moveAllPurchase(boolean isSelect, Integer itemClassIndex)
	{
		if(itemClassIndex == null)
		{
			return;
		}
		
		ArrayList<ArrayList<MstPurchase>>	from	=	(isSelect ? reference : selected);
		ArrayList<ArrayList<MstPurchase>>	to		=	(isSelect ? selected : reference);
		
		ArrayList<MstPurchase>	pcFrom	=	from.get(itemClassIndex);
		ArrayList<MstPurchase>	pcTo	=	to.get(itemClassIndex);
		
		while(0 < pcFrom.size())
		{
			pcTo.add(pcFrom.remove(0));
		}
		
		this.sort(isSelect, itemClassIndex);
	}
	
	/**
	 * �\�[�g���s���B
	 * @param isSelect 
	 * @param itemClassIndex 
	 */
	public void sort(boolean isSelect, Integer itemClassIndex)
	{
		if(itemClassIndex == null)
		{
			return;
		}
		
		ArrayList<ArrayList<MstPurchase>>	mpcs		=	(isSelect ? selected : reference);
		ArrayList<MstPurchase>	mpc	=	mpcs.get(itemClassIndex);
		Collections.sort(mpc, new ProductComparator());
	}
	
	private class ProductComparator implements Comparator<MstPurchase>
	{
		public int compare(MstPurchase mp0, MstPurchase mp1)
		{
			return	mp0.getDisplaySeq().compareTo(mp1.getDisplaySeq());
		}
	}
	
	/**
	 * �o�^�������s���B
	 * @return true - �����Afalse - ���s
	 */
	public boolean regist()
	{
		boolean		result	=	true;
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			con.begin();
			
			try
			{
				if(this.deleteData(con))
				{
					for(ArrayList<MstPurchase> mps : selected)
					{
						for(MstPurchase mp : mps)
						{
							if(!mp.regist(con))
							{
								result	=	false;
							}
							
							if(!result)
							{
								break;
							}
						}

						if(!result)
						{
							break;
						}
					}
				}
				else
				{
					result	=	false;
				}
			}
			catch(SQLException e)
			{
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
			
			if(result)
			{
				con.commit();
			}
			else
			{
				con.rollback();
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}
	
	/**
	 * ���̃f�[�^���폜����B
	 * @param con ConnectionWrapper
	 * @return true - �����Afalse - ���s
	 * @throws java.sql.SQLException SQLException
	 */
	private boolean deleteData(ConnectionWrapper con) throws SQLException
	{
		return (0 <= con.executeUpdate(this.getDeleteMstUseProductSQL()));
	}
	
	/**
	 * ���̃f�[�^���폜����r�p�k�����擾����B
	 * @return ���̃f�[�^���폜����r�p�k��
	 */
	private String getDeleteMstUseProductSQL()
	{
		return	"delete from mst_purchase\n" +
				"where supplier_id = " + SQLUtil.convertForSQL(this.getSupplier().getSupplierID()) + "\n";
	}
}
