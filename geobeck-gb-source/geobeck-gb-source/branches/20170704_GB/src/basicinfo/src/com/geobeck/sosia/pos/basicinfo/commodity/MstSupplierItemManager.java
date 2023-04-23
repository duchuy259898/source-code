/*
 * MstSupplierItemManager.java
 *
 * Created on 2008/09/11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.basicinfo.commodity;

import com.geobeck.sosia.pos.master.commodity.MstSupplier;
import com.geobeck.sosia.pos.master.commodity.MstSupplierItem;
import com.geobeck.sosia.pos.master.product.MstItem;
import com.geobeck.sosia.pos.master.product.MstItemClass;
import com.geobeck.sosia.pos.master.product.MstItemClasses;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;


/**
 *
 * @author mizukawa
 */
public class MstSupplierItemManager
{
	/**
	 * �R���X�g���N�^
	 */
	public MstSupplierItemManager()
	{
		itemClasses	=	new MstItemClasses();
		reference	=	new ArrayList<ArrayList<MstSupplierItem>>();
		selected	=	new ArrayList<ArrayList<MstSupplierItem>>();
		this.loadItemClasses();
		this.initArrays();
		this.loadSupplierItems();
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
	private	ArrayList<ArrayList<MstSupplierItem>>	reference		=	null;
	/**
	 * �I�����i
	 */
	private	ArrayList<ArrayList<MstSupplierItem>>	selected		=	null;
	
	/**
	 * �I������Ă��鏤�i���ނ̃C���f�b�N�X
	 */
	private	Integer				selectedIndex	=	-1;
	
	/**
	 * �ēǂݍ���
	 */
	public void reload()
	{
		reference	=	new ArrayList<ArrayList<MstSupplierItem>>();
		selected	=	new ArrayList<ArrayList<MstSupplierItem>>();
		
		this.initArrays();
		this.loadSupplierItems();
	}
	
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
	public ArrayList<ArrayList<MstSupplierItem>> getReference()
	{
		return reference;
	}
	
	/**
	 * �Q�Ə��i��ݒ肷��B
	 * @param reference �Q�Ə��i
	 */
	public void setReference(ArrayList<ArrayList<MstSupplierItem>> reference)
	{
		this.reference = reference;
	}
	
	/**
	 * �I�����i���擾����B
	 * @return �I�����i
	 */
	public ArrayList<ArrayList<MstSupplierItem>> getSelected()
	{
		return selected;
	}
	
	/**
	 * �I�����i��ݒ肷��B
	 * @param selected �I�����i
	 */
	public void setSelected(ArrayList<ArrayList<MstSupplierItem>> selected)
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
			ArrayList<MstSupplierItem>	temp	=	new ArrayList<MstSupplierItem>();
			
			for(MstItem	mi : mic)
			{
				MstSupplierItem		msi	=	new MstSupplierItem(null, mi);
				temp.add(msi);
			}
			
			reference.add(temp);
			selected.add(new ArrayList<MstSupplierItem>());
		}
		
		return	result;
	}
	
	
	/**
	 * �d�����i��ǂݍ��ށB
	 * @return true - �����Afalse - ���s
	 */
	private boolean loadSupplierItems()
	{
		boolean		result	=	false;
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			ResultSetWrapper	rs	=	con.executeQuery(this.getLoadSupplierItemSQL());
			
			while(rs.next())
			{
				int itemClassId = rs.getInt("item_class_id");
				int itemId = rs.getInt("item_id");
				Integer	itemClassIndex	=	this.getItemClassIndexByID(itemClassId);
				Integer	itemIndex		=	this.getItemIndexByID(reference.get(itemClassIndex), itemId);
				Integer supplierId		=	rs.getInt("supplier_id");

				ArrayList<MstSupplierItem> supplierItemArray = reference.get(itemClassIndex);
				MstSupplierItem msi = supplierItemArray.get(itemIndex);
				msi.setCostPrice(rs.getInt("cost_price"));
				this.selectSupplierItem(supplierId, itemClassIndex, itemIndex);
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
	private String getLoadSupplierItemSQL()
	{
		return	"select mic.item_class_id, mi.item_id, msi.supplier_id, msi.cost_price\n" +
			"from mst_supplier_item msi\n" +
			"inner join mst_item mi\n" +
			"on mi.item_id = msi.item_id\n" +
			"and mi.delete_date is null\n" +
			"inner join mst_item_class mic\n" +
			"on mic.item_class_id = mi.item_class_id\n" +
			"and mic.delete_date is null\n" +
			"where msi.delete_date is null\n" +
//				"and msi.supplier_id = " + SQLUtil.convertForSQL(this.getSupplier().getSupplierID()) + "\n" +
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
			Integer ici = itemClasses.get(i).getItemClassID();
			if(itemClassID.equals(ici))
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
	public Integer getItemIndexByID(ArrayList<MstSupplierItem> itemClass, Integer itemID)
	{
		for(Integer i = 0; i < itemClass.size(); i ++)
		{
			if(itemClass.get(i).getItemID().equals(itemID))
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
	public void moveSupplierItem(boolean isSelect, Integer itemClassIndex, Integer itemIndex)
	{
		if(itemClassIndex == null || itemIndex == null)
		{
			return;
		}
		
		ArrayList<ArrayList<MstSupplierItem>>	from		=	(isSelect ? reference : selected);
		ArrayList<ArrayList<MstSupplierItem>>	to			=	(isSelect ? selected : reference);
		MstSupplier								supplier	=	(isSelect ? null : this.supplier);
		
		ArrayList<MstSupplierItem>	pcFrom	=	from.get(itemClassIndex);
		ArrayList<MstSupplierItem>	pcTo	=	to.get(itemClassIndex);
		
		int idx = MstSupplierItemManager.convertIndex(pcFrom, supplier, itemIndex);
		MstSupplierItem item = pcFrom.remove(idx);
		pcTo.add(item);
		
		if (isSelect)
		{
			item.setSupplier(this.getSupplier());
		}
		else
		{
			item.setSupplier(null);
		}
		
		this.sort(isSelect, itemClassIndex);
	}
	
	/**
	 * ���i��I������B
	 * @param supplierID �d����ID
	 * @param itemClassIndex ���i���ނ̃C���f�b�N�X
	 * @param itemIndex ���i�̃C���f�b�N�X
	 */
	public void selectSupplierItem(int supplierID, Integer itemClassIndex, Integer itemIndex)
	{
		if(itemClassIndex == null || itemIndex == null)
		{
			return;
		}
		
		ArrayList<ArrayList<MstSupplierItem>>	from		=	reference;
		ArrayList<ArrayList<MstSupplierItem>>	to			=	selected;
		
		ArrayList<MstSupplierItem>	pcFrom	=	from.get(itemClassIndex);
		ArrayList<MstSupplierItem>	pcTo	=	to.get(itemClassIndex);
		
		MstSupplierItem item = pcFrom.remove(itemIndex.intValue());
		pcTo.add(item);
		
		MstSupplier supplier = new MstSupplier();
		supplier.setSupplierID(supplierID);
		item.setSupplier(supplier);
		
		this.sort(true, itemClassIndex);
	}
	
	/**
	 * ���i��S�đI���i�����j����B
	 * @param itemClassIndex ���i���ނ̃C���f�b�N�X
	 * @param isSelect true - �I���Afalse - ����
	 */
	public void moveAllSupplierItem(boolean isSelect, Integer itemClassIndex)
	{
		if(itemClassIndex == null || itemClassIndex < 0)
		{
			return;
		}
		
		ArrayList<ArrayList<MstSupplierItem>>	from	=	(isSelect ? reference : selected);
		ArrayList<ArrayList<MstSupplierItem>>	to		=	(isSelect ? selected : reference);
		
		ArrayList<MstSupplierItem>	pcFrom	=	from.get(itemClassIndex);
		ArrayList<MstSupplierItem>	pcTo	=	to.get(itemClassIndex);
		
		for (int i = pcFrom.size() - 1; i >= 0; i--)
		{
			MstSupplierItem item = pcFrom.get(i);
			if (isSelect)
			{
				item.setSupplier(this.getSupplier());
			}
			else
			{
				if (item.getSupplier() != null && !item.getSupplier().getSupplierID().equals(this.getSupplier().getSupplierID()))
				{
					continue;
				}
				item.setSupplier(null);
			}
			
			pcFrom.remove(i);
			pcTo.add(item);
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
		if(itemClassIndex == null || itemClassIndex < 0)
		{
			return;
		}
		
		ArrayList<ArrayList<MstSupplierItem>>	mpcs		=	(isSelect ? selected : reference);
		ArrayList<MstSupplierItem>	mpc	=	mpcs.get(itemClassIndex);
		Collections.sort(mpc, new ProductComparator());
	}

	private class ProductComparator implements Comparator<MstSupplierItem>
	{
		public int compare(MstSupplierItem mp0, MstSupplierItem mp1)
		{
			return	mp0.getDisplaySeq().compareTo(mp1.getDisplaySeq());
		}
	}
	
	/**
	 * �o�^�������s���B
	 * @return true - �����Afalse - ���s
	 */
	public boolean register(ConnectionWrapper con) throws SQLException
	{
		try
		{
			if(this.deleteData(con))
			{
				for(ArrayList<MstSupplierItem> msis : selected)
				{
					for(MstSupplierItem msi : msis)
					{
						if (msi.getSupplier().getSupplierID() == null)
						{
							msi.setSupplier(this.getSupplier());
						}
						else if (this.getSupplier() != null && !this.getSupplier().getSupplierID().equals(msi.getSupplier().getSupplierID()))
						{
							continue;
						}
						
						if(!msi.regist(con))
						{
							return false;
						}
					}
				}
			}
			else
			{
				return false;
			}
			
			return true;
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw e;
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
				if (register(con))
				{
					con.rollback();
					return false;
				}
				
				con.commit();
				return true;
			}
			catch(SQLException e)
			{
				con.rollback();
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}
	
        public boolean checkExistItemRegisted() {
            ConnectionWrapper	con	=	SystemInfo.getConnection();
            try {
                ResultSetWrapper	rs	=	con.executeQuery(this.getCheckExistItemRegistedSQL());
                if(rs.next()) return true;
            }catch (Exception e) {
                
            }
            
            return false;
        }
        private String getCheckExistItemRegistedSQL() {
            return	"select 1 from mst_supplier_item \n"
			+ " where delete_date is null  and supplier_id = " + SQLUtil.convertForSQL(this.getSupplier().getSupplierID()) + "\n"; 
        }
	/**
	 * �d����폜���ɌĂ�
	 */
	public boolean delete()
	{
		boolean		result	=	true;
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			con.begin();
			try
			{
				if (this.updateSql(con, this.getDeleteBySupplierSQL()))
				{
					con.commit();
					return true;
				}
				
				con.rollback();
				return false;
			}
			catch(SQLException e)
			{
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
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
		return updateSql(con, this.getDeleteBySupplierSQL());
	}
	
	/**
	 * �ėpSQL���s���\�b�h
	 */
	private boolean updateSql(ConnectionWrapper con, String sql) throws SQLException
	{
		return (0 <= con.executeUpdate(sql));
	}
	
	/**
	 * ���̃f�[�^���폜����r�p�k�����擾����B
	 * @return ���̃f�[�^���폜����r�p�k��
	 */
	private String getLogicalDeleteSQL()
	{
		return	"delete from mst_supplier_item\n"
			+ "where supplier_id = " + SQLUtil.convertForSQL(this.getSupplier().getSupplierID()) + "\n";
	}
	
	/**
	 * �d����폜���ɔ��s����SQL
	 */
	private String getDeleteBySupplierSQL()
	{
		return	"update mst_supplier_item set\n"
			+ "delete_date = current_timestamp\n"
			+ "where supplier_id = " + SQLUtil.convertForSQL(this.getSupplier().getSupplierID()) + "\n";
	}
	
	/**
	 * list�Ɋi�[����Ă���MstSupplierItem�̒��ŁAsupplier�Ɉ�v����index�Ԗڂ�MstSupplierItem��Ԃ�
	 * @param list MstSupplierItem���i�[���ꂽ���X�g
	 * @param supplier �d����
	 * @param index ���i�̒��Ŏw�肳�ꂽ�d����̂��̂̃C���f�b�N�X
	 * @return list�ł̃C���f�b�N�X
	 */
	public static int convertIndex(List<MstSupplierItem> list, MstSupplier supplier, int index)
	{
            int cnt = -1;
            int size = list.size();

            for (int i = 0; i < size; i++) {
                MstSupplierItem msi = list.get(i);
                if (supplier != null && !supplier.getSupplierID().equals(msi.getSupplier().getSupplierID())) {
                    continue;
                }

                cnt++;
                if (cnt == index) {
                    return i;
                }
            }

            return -1;
	}
}
