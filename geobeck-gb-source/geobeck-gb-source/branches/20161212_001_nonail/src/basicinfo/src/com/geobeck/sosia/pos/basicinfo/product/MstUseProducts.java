/*
 * MstUseProducts.java
 *
 * Created on 2007/01/18, 15:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.basicinfo.product;

import com.geobeck.sosia.pos.products.*;
import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;

/**
 * �g�p���i�E�Z�p�I���N���X
 * @author katagiri
 */
public class MstUseProducts
{
	/**
	 * �X��
	 */
	private	MstShop				shop				=	new MstShop();
	/**
	 * �����敪�i1�F�Z�p�A2�F���i�j
	 */
	private Integer				productDivision		=	null;
	
	/**
	 * �Q�Ə��i�E�Z�p
	 */
	private	ProductClasses		referenceProducts	=	new ProductClasses();
	/**
	 * �I�����i�E�Z�p
	 */
	private	ProductClasses		selectProducts		=	new ProductClasses();
	
	/**
	 * �R���X�g���N�^
	 */
	public MstUseProducts()
	{
	}

	/**
	 * �X�܂��擾����B
	 * @return �X��
	 */
	public MstShop getShop()
	{
		return shop;
	}

	/**
	 * �X�܂�ݒ肷��B
	 * @param shop �X��
	 */
	public void setShop(MstShop shop)
	{
		this.shop = shop;
	}

	/**
	 * �����敪�i1�F�Z�p�A2�F���i�j���擾����B
	 * @return �����敪�i1�F�Z�p�A2�F���i�j
	 */
	public Integer getProductDivision()
	{
		return productDivision;
	}

	/**
	 * �����敪�i1�F�Z�p�A2�F���i�j��ݒ肷��B
	 * @param productDivision �����敪�i1�F�Z�p�A2�F���i�j
	 */
	public void setProductDivision(Integer productDivision)
	{
		this.productDivision = productDivision;
		referenceProducts.setProductDivision(productDivision);
		selectProducts.setProductDivision(productDivision);
	}
	
	/**
	 * �����敪�����擾����B
	 * @return �����敪��
	 */
	public String getProductDivisionName()
	{
		switch(productDivision)
		{
			case 1:
				return	"�X�܎g�p�Z�p";
			case 2:
				return	"�X�܎g�p���i";
			default:
				return	"";
		}
	}

	/**
	 * �Q�Ə��i�E�Z�p���擾����B
	 * @return �Q�Ə��i�E�Z�p
	 */
	public ProductClasses getReferenceProducts()
	{
		return referenceProducts;
	}

	/**
	 * �Q�Ə��i�E�Z�p��ݒ肷��B
	 * @param referenceProducts �Q�Ə��i�E�Z�p
	 */
	public void setReferenceProducts(ProductClasses referenceProducts)
	{
		this.referenceProducts = referenceProducts;
	}

	/**
	 * �I�����i�E�Z�p���擾����B
	 * @return �I�����i�E�Z�p
	 */
	public ProductClasses getSelectProducts()
	{
		return selectProducts;
	}

	/**
	 * �I�����i�E�Z�p��ݒ肷��B
	 * @param selectProducts �I�����i�E�Z�p
	 */
	public void setSelectProducts(ProductClasses selectProducts)
	{
		this.selectProducts = selectProducts;
	}
	
	
	/**
	 * �f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @return true - �����Afalse - ���s
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean load(ConnectionWrapper con) throws SQLException
	{
		if(shop.getShopID() == null || productDivision == null)
		{
			return	false;
		}
		
		if(!referenceProducts.load(con))
		{
			return	false;
		}
		
		if(!selectProducts.load(con))
		{
			return	false;
		}
		
		for(ProductClass pc : referenceProducts)
		{
			if(!pc.loadProducts(con, this.getProductDivision()))
			{
				return	false;
			}
		}
		
		if(!this.loadMstUseProduct(con))
		{
			return	false;
		}
		
		return	true;
	}
	
	
	/**
	 * �戵���i�E�Z�p��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @return true - ����
	 * @throws java.sql.SQLException SQLException
	 */
	private boolean loadMstUseProduct(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper rs = con.executeQuery(this.getLoadMstUseProductSQL());
		
		while(rs.next())
		{
			ProductClass pc = referenceProducts.getProductClass(rs.getInt("product_class_id"));
			if (pc == null) continue;
				
			Product p = pc.getProduct(rs.getInt("product_id"));
			if (p == null) continue;
                        
			long price = rs.getLong("price");
			if (!rs.wasNull()) {
                            p.setPrice(price);
			}
                        
			int useProperStock = rs.getInt("use_proper_stock");
			if (rs.wasNull()) {
                            p.setUseProperStock(null);
			} else {
                            p.setUseProperStock(useProperStock);
			}

			int sellProperStock = rs.getInt("sell_proper_stock");
			if (rs.wasNull()) {
                            p.setSellProperStock(null);
			} else {
                            p.setSellProperStock(sellProperStock);
			}

			int displaySeq = rs.getInt("display_seq");
			if (rs.wasNull()) {
                            p.setDisplaySeq(null);
			} else {
                            p.setDisplaySeq(displaySeq);
			}

			String color = rs.getString("color");
			if (rs.wasNull()) {
                            p.setColor("");
			} else {
                            p.setColor(color);
			}

			int reserveFlg = rs.getInt("reserve_flg");
			if (rs.wasNull()) {
                            p.setReserveFlg(false);
			} else {
                            p.setReserveFlg(reserveFlg == 1);
			}

			this.moveProduct(true,
					referenceProducts.getProductClassIndex(rs.getInt("product_class_id")),
					referenceProducts.getProductClass(rs.getInt("product_class_id")).getProductIndex(rs.getInt("product_id")));
		}
		
		rs.close();
		rs = null;
		
		return true;
	}
	
	/**
	 * �戵���i�E�Z�p��ǂݍ��ނr�p�k�����擾����B
	 * @return �戵���i�E�Z�p��ǂݍ��ނr�p�k��
	 */
	private String getLoadMstUseProductSQL()
	{
		StringBuilder sql = new StringBuilder(1000);
		
		switch(productDivision)
		{
                    //�Z�p
                    case 1:
                        sql.append(" select");
                        sql.append("      mup.product_id");
                        sql.append("     ,mt.technic_class_id as product_class_id");
                        sql.append("     ,mup.price");
                        sql.append("     ,null as use_proper_stock");
                        sql.append("     ,null as sell_proper_stock");
                        sql.append("     ,mup.display_seq");
                        sql.append("     ,mup.color");
                        sql.append("     ,coalesce(mup.reserve_flg, 0) as reserve_flg");
                        sql.append(" from");
                        sql.append("     mst_use_product mup");
                        sql.append("         inner join mst_technic mt");
                        sql.append("                 on mt.technic_id = mup.product_id");
                        sql.append("                and mt.delete_date is null");
                        sql.append(" where");
                        sql.append("         mup.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
                        sql.append("     and mup.product_division = 1");
                        sql.append("     and mup.delete_date is null ");
                        sql.append(" order by");
                        sql.append("     mup.display_seq");

                        break;

                    //���i
                    case 2:
                        sql.append(" select");
                        sql.append("      mup.product_id");
                        sql.append("     ,mi.item_class_id as product_class_id");
                        sql.append("     ,mup.price");
                        sql.append("     ,mup.use_proper_stock");
                        sql.append("     ,mup.sell_proper_stock");
                        sql.append("     ,mup.display_seq");
                        sql.append("     ,null as color");
                        sql.append("     ,null as reserve_flg");
                        sql.append(" from");
                        sql.append("     mst_use_product mup");
                        sql.append("         inner join mst_item mi");
                        sql.append("                 on mi.item_id = mup.product_id");
                        sql.append("                and mi.delete_date is null");
                        sql.append(" where");
                        sql.append("         mup.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
                        sql.append("     and mup.product_division = 2");
                        sql.append("     and mup.delete_date is null");
                        sql.append(" order by");
                        sql.append("     mup.display_seq");
                        //IVS_LVTu start add 2015/09/04 New request #42445
                        sql.append("     , mi.item_id");
                        //IVS_LVTu end add 2015/09/04 New request #42445

                        break;

                    //�R�[�X
                    case 3:
                        sql.append(" select");
                        sql.append("      mup.product_id");
                        sql.append("     ,mc.course_class_id as product_class_id");
                        sql.append("     ,mup.price");
                        sql.append("     ,null as use_proper_stock");
                        sql.append("     ,null as sell_proper_stock");
                        sql.append("     ,mup.display_seq");
                        sql.append("     ,mup.color");
                        sql.append("     ,coalesce(mup.reserve_flg, 0) as reserve_flg");
                        sql.append(" from");
                        sql.append("     mst_use_product mup");
                        sql.append("         inner join mst_course mc");
                        sql.append("         on mc.course_id = mup.product_id");
                        sql.append("         and mc.delete_date is null");
                        sql.append(" where");
                        sql.append("         mup.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
                        sql.append("     and mup.product_division = 3");
                        sql.append("     and mup.delete_date is null");
                        sql.append(" order by");
                        sql.append("     mup.display_seq");
                        
                        break;
		}
		
		return	sql.toString();
	}
	
	/**
	 * ���i�E�Z�p��I���i�����j����B
	 * @param isSelect true�F�I���Afalse�F����
	 * @param productClassIndex ���ނ̃C���f�b�N�X
	 * @param productIndex ���i�E�Z�p�̃C���f�b�N�X
	 */
	public void moveProduct(boolean isSelect, Integer productClassIndex, Integer productIndex)
	{
		if(productClassIndex == null || productIndex == null)
		{
			return;
		}
		
		ProductClasses	from	=	(isSelect ? referenceProducts : selectProducts);
		ProductClasses	to		=	(isSelect ? selectProducts : referenceProducts);
		
		ProductClass	pcFrom	=	from.get(productClassIndex);
		ProductClass	pcTo	=	to.get(productClassIndex);
		
		pcTo.add(pcFrom.remove(productIndex.intValue()));
	}
	
	/**
	 * ���i�E�Z�p��S�đI���i�����j����B
	 * @param isSelect true�F�I���Afalse�F����
	 * @param productClassIndex ���ނ̃C���f�b�N�X
	 */
	public void moveProductAll(boolean isSelect, Integer productClassIndex)
	{
		if(productClassIndex == null)
		{
			return;
		}
		
		ProductClasses	from	=	(isSelect ? referenceProducts : selectProducts);
		ProductClasses	to		=	(isSelect ? selectProducts : referenceProducts);
		
		ProductClass	pcFrom	=	from.get(productClassIndex);
		ProductClass	pcTo	=	to.get(productClassIndex);
		
		while(0 < pcFrom.size())
		{
			pcTo.add(pcFrom.remove(0));
		}
	}
	
	public void sort(boolean isSelect, Integer productClassIndex)
	{
		if(productClassIndex == null)
		{
			return;
		}
		
		ProductClasses	pcs		=	(isSelect ? selectProducts : referenceProducts);
		ProductClass	pc	=	pcs.get(productClassIndex);
		pc.sort();
	}
	
	/**
	 * �o�^�������s���B
	 * @param con ConnectionWrapper
	 * @return true - �����Afalse - ���s
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		//���f�[�^���폜
		if(!this.deleteData(con))
		{
			return	false;
		}
		//�f�[�^��o�^
		if(!this.registData(con))
		{
			return false;
		}
		
		return	true;
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
		return	"delete from mst_use_product\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and  product_division = " + SQLUtil.convertForSQL(this.getProductDivision()) + "\n";
	}
	
	/**
	 * �f�[�^��o�^����B
	 * @param con ConnectionWrapper
	 * @return true - �����Afalse - ���s
	 * @throws java.sql.SQLException SQLException
	 */
	private boolean registData(ConnectionWrapper con) throws SQLException
	{
		for(ProductClass pc : selectProducts)
		{
			for(Product p : pc)
			{
				if(con.executeUpdate(this.getInsertMstUseProductSQL(p)) != 1)
				{
					return	false;
				}
			}
		}
		
		return	true;
	}
	
	/**
	 * �P�����̃f�[�^��o�^����r�p�k�����擾����B
	 * @param p �o�^���鏤�i�E�Z�p
	 * @return �P�����̃f�[�^��o�^����r�p�k��
	 */
	private String getInsertMstUseProductSQL(Product p)
	{
		return	"insert into mst_use_product\n" +
				"(shop_id, product_division, product_id,\n" +
				"insert_date, update_date, delete_date,\n" +
				"use_proper_stock, sell_proper_stock, price, display_seq, color, reserve_flg)\n" +
				"values(\n" +
				SQLUtil.convertForSQL(this.getShop().getShopID()) + ",\n" +
				SQLUtil.convertForSQL(this.getProductDivision()) + ",\n" +
				SQLUtil.convertForSQL(p.getProductID()) + ",\n" +
				"current_timestamp,\n" +
				"current_timestamp,\n" +
				"null,\n" +
				SQLUtil.convertForSQL(p.getUseProperStock()) + ",\n" +
				SQLUtil.convertForSQL(p.getSellProperStock()) + ",\n" +
				SQLUtil.convertForSQL(p.getPrice()) + ",\n" +
				SQLUtil.convertForSQL(p.getDisplaySeq()) + ",\n" +
				SQLUtil.convertForSQL(p.getColor()) + ",\n" +
				SQLUtil.convertForSQL(p.isReserveFlg() ? 1 : 0) + ")\n";
	}
	
	/**
	 * �폜�������s���B
	 * @param con ConnectionWrapper
	 * @param id �v���_�N�gID
	 * @return true - �����Afalse - ���s
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean deleteProductId(ConnectionWrapper con, Integer id) throws SQLException
	{
		return (0 <= con.executeUpdate(this.getProductIdSQL(id)));
	}
	
	/**
	 * �v���_�N�gID�̃f�[�^���폜����r�p�k�����擾����B
	 * @param id �폜����v���_�N�gID
	 * @return �v���_�N�gID�̃f�[�^���폜����r�p�k��
	 */
	private String getProductIdSQL(Integer id)
	{
		return	"delete from mst_use_product\n" +
				"where product_division = " + SQLUtil.convertForSQL(this.getProductDivision()) + "\n" +
				"and  product_id = " + SQLUtil.convertForSQL(id) + "\n";
	}
}
