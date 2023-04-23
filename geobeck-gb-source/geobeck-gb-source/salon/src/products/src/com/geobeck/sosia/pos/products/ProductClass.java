/*
 * ProductClass.java
 *
 * Created on 2006/05/26, 18:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.products;


import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * ���i���ރf�[�^
 * @author katagiri
 */
public class ProductClass extends ArrayList<Product>
{
	/**
	 * ���i���ނh�c
	 */
	protected	Integer		productClassID		=	null;
	/**
	 * ���i���ޖ�
	 */
	protected	String		productClassName	=	"";
	/**
	 * �\����
	 */
	protected	Integer		displaySeq			=	null;
        /**
	 * �R���X�g���N�^
	 */
        private Integer prepa_class_id = null;

        public Integer getPrepa_class_id() {
            return prepa_class_id;
        }

        public void setPrepa_class_id(Integer prepa_class_id) {
            this.prepa_class_id = prepa_class_id;
        }

        // Thanh start add 2014/07/11 Mashu_����v�\��
        private Integer shopCategoryID = null;

        public Integer getShopCategoryID() {
            return shopCategoryID;
        }

        public void setShopCategoryID(Integer shopCategoryID) {
            this.shopCategoryID = shopCategoryID;
        }
        // Thanh end add 2014/07/11 Mashu_����v�\��

	/**
	 * �R���X�g���N�^
	 */
	public ProductClass()
	{
	}

	/**
	 * ������\���i���i���ޖ��j���擾����B
	 * @return ������\���i���i���ޖ��j
	 */
	public String toString()
	{
		return	this.getProductClassName();
	}

	/**
	 * ���i���ނh�c���擾����B
	 * @return ���i���ނh�c
	 */
	public Integer getProductClassID()
	{
		return productClassID;
	}

	/**
	 * ���i���ނh�c��ݒ肷��B
	 * @param productClassID ���i���ނh�c
	 */
	public void setProductClassID(Integer productClassID)
	{
		this.productClassID = productClassID;
	}

	/**
	 * ���i���ޖ����擾����B
	 * @return ���i���ޖ�
	 */
	public String getProductClassName()
	{
		return productClassName;
	}

	/**
	 * ���i���ޖ���ݒ肷��B
	 * @param productClassName ���i���ޖ�
	 */
	public void setProductClassName(String productClassName)
	{
		this.productClassName = productClassName;
	}

	/**
	 * �\�������擾����B
	 * @return �\����
	 */
	public Integer getDisplaySeq()
	{
		return displaySeq;
	}

	/**
	 * �\������ݒ肷��B
	 * @param displaySeq �\����
	 */
	public void setDisplaySeq(Integer displaySeq)
	{
		this.displaySeq = displaySeq;
	}

	/**
	 * ���i���擾����B
	 * @param productID ���i�h�c
	 * @return ���i
	 */
	public Product getProduct(Integer productID)
	{
		for(Product p : this)
		{
			if(p.getProductID().intValue() == productID)
			{
				return	p;
			}
		}

		return	null;
	}

	/**
	 * ���i�̃C���f�b�N�X���擾����B
	 * @param productID ���i�h�c
	 * @return ���i�̃C���f�b�N�X
	 */
	public Integer getProductIndex(Integer productID)
	{
		for(Integer i = 0; i < this.size(); i ++)
		{
			if(this.get(i).getProductID().intValue() == productID.intValue())
			{
				return	i;
			}
		}

		return	null;
	}

	/**
	 * �f�[�^��ݒ肷��B
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setProductClassID(rs.getInt("product_class_id"));
		this.setProductClassName(rs.getString("product_class_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
                // Thanh start add 2014/07/11 Mashu_����v�\��
                this.setShopCategoryID(rs.getInt("shop_category_id"));
                // Thanh end add 2014/07/11 Mashu_����v�\��
	}
        public void setData(ResultSetWrapper rs,Integer productdivision) throws SQLException
	{
		this.setProductClassID(rs.getInt("product_class_id"));
		this.setProductClassName(rs.getString("product_class_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
                //IVS_LVTu start add 2015/06/26 New request #38256
                try {
                    this.setShopCategoryID(rs.getInt("shop_category_id"));
                }catch(Exception e){

                }
                //IVS_LVTu end add 2015/06/26 New request #38256
                if(productdivision==2){
                this.setPrepa_class_id(rs.getInt("prepa_class_id"));
                }
	}

	/**
	 * ���i��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @param productDivision ���i����
	 * @return true - �����Afalse - ���s
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean loadProducts(ConnectionWrapper con, Integer productDivision) throws SQLException
	{
		this.clear();

		if(productDivision != 1 && productDivision != 2 && productDivision != 3)	return	false;

		ResultSetWrapper	rs	=	con.executeQuery(
				this.getLoadProductsSQL(productDivision));

		while(rs.next())
		{
			Product	p	=	new Product();
			p.setData(rs);
                        if(productDivision == 1){
                            p.setPraiseTimeLimit(rs.getInt("praise_time_limit"));
                            p.setPraiseTime(rs.getBoolean("is_praise_time"));
                            p.setIspotMenuId(rs.getInt("ispot_menu_id"));
                        } else if (productDivision == 2){
							p.setUseProperStock(rs.getInt("use_proper_stock"));
							p.setSellProperStock(rs.getInt("sell_proper_stock"));
						}
			p.setProductClass(this);
			this.add(p);
		}

		rs.close();

		return	true;
	}

	/**
	 * ���i��ǂݍ��ނr�p�k�����擾����B
	 * @param productDivision ���i����
	 * @return ���i��ǂݍ��ނr�p�k��
	 */
	public String getLoadProductsSQL(Integer productDivision)
	{
		String	sql		=	"";

		switch(productDivision)
		{
			//�Z�p
			case 1:
				sql	=	"select technic_id as product_id,\n" +
						"technic_no as product_no,\n" +
						"technic_name as product_name, price, price as base_price, display_seq, display_seq as base_display_seq,\n" +
						"operation_time,\n" +
                                                "praise_time_limit, \n" +
                                                "is_praise_time\n" +
                                                // vtbphuong start add 20140612 Request #24859
                                                ",ispot_menu_id\n" +
                                                // vtbphuong start add 20140612 Request #24859
						"from mst_technic mt\n" +
						"where delete_date is null\n" +
						"and technic_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()) + "\n" +
						"order by display_seq, technic_name, technic_id\n";
				break;
			//���i
			case 2:
				sql	=	"select item_id as product_id,\n" +
						"item_no as product_no,\n" +
						"item_name as product_name, price, price as base_price, display_seq, display_seq as base_display_seq,\n" +
						"0 as operation_time,\n" +
												"use_proper_stock, \n" +
												"sell_proper_stock\n" +
						"from mst_item mgc\n" +
						"where delete_date is null\n" +
						"and item_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()) + "\n" +
//						"and item_use_division in (1, 3)\n" +
						"order by display_seq, item_name, item_id\n";
				break;
            //�R�[�X
            case 3:
                    sql     =       "select course_id as product_id,\n" +
                                    "course_id as product_no,\n" +
                                    "course_name as product_name, price, price as base_price, display_seq, display_seq as base_display_seq,\n" +
			"operation_time,\n" +
                                    "praise_time_limit, \n" +
                                    "is_praise_time\n" +
                                    "from mst_course\n" +
                                    "where delete_date is null\n" +
                                    "and course_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()) + "\n" +
                                    "order by display_seq, course_name, course_id\n";
                break;
		}

		return	sql;
	}

	/**
	 * ���i��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @param productDivision ���i����
	 * @return true - �����Afalse - ���s
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean loadProducts(ConnectionWrapper con, Integer productDivision, Integer shopID) throws SQLException
	{
		this.clear();

		if(productDivision != 1 && productDivision != 2 && productDivision != 3)	return	false;

		ResultSetWrapper	rs	=	con.executeQuery(
				this.getLoadProductsSQL(productDivision, shopID));

		while(rs.next())
		{
			Product	p	=	new Product();
			p.setData(rs);
                        if(productDivision == 1){
                            p.setPraiseTimeLimit(rs.getInt("praise_time_limit"));
                            p.setPraiseTime(rs.getBoolean("is_praise_time"));
                        }
            // 20191225 change
			//p.setProductClass((ProductClass)this.clone());
			ProductClass pc = new ProductClass();
			pc.setPrepa_class_id(prepa_class_id);
			pc.setProductClassID(productClassID);
			pc.setProductClassName(productClassName);
			pc.setShopCategoryID(shopCategoryID);
			pc.setDisplaySeq(displaySeq);
			p.setProductClass(pc);

			this.add(p);
		}

		rs.close();

		return	true;
	}

	/**
	 * ���i��ǂݍ��ނr�p�k�����擾����B
	 * @param productDivision ���i����
	 * @return ���i��ǂݍ��ނr�p�k��
	 */
	public String getLoadProductsSQL(Integer productDivision, Integer shopID)
	{
               StringBuilder sql = new StringBuilder(1000);

		switch(productDivision)
		{
			//�Z�p
			case 1:
                            if (shopID > 0) {
                                sql.append(" select");
                                sql.append("      mt.technic_id      as product_id");
                                sql.append("     ,mt.technic_no      as product_no");
                                sql.append("     ,mt.technic_name ||");
                                sql.append("         case when (mt.technic_no like 'mo-rsv-%') or (mt.mobile_flag=1)");
                                sql.append("             then '�i�\��p�j'");
                                sql.append("             else ''");
                                sql.append("         end as product_name");
                                sql.append("     ,mup.price");
                                sql.append("     ,mt.price as base_price");
                                sql.append("     ,mt.display_seq as base_display_seq");
                                sql.append("     ,mup.display_seq");
                                sql.append("     ,mt.operation_time");
                                sql.append("     ,mt.praise_time_limit");
                                sql.append("     ,mt.is_praise_time");
                                sql.append(" from");
                                sql.append("     mst_use_product mup");
                                sql.append("         inner join mst_technic mt");
                                sql.append("                 on mt.technic_id = mup.product_id");
                                sql.append("                and mt.technic_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()));
                                sql.append("                and mt.delete_date is null");
                                sql.append(" where");
                                sql.append("         mup.delete_date is null");
                                sql.append("     and mup.shop_id = " + SQLUtil.convertForSQL(shopID));
                                sql.append("     and mup.product_division = 1");
                                sql.append(" order by");
                                sql.append("      mup.display_seq");
                                sql.append("     ,mt.technic_name");
                                sql.append("     ,mt.technic_id");

                            } else {

                                sql.append(" select");
                                sql.append("      technic_id      as product_id");
                                sql.append("     ,technic_no      as product_no");
                                sql.append("     ,technic_name ||");
                                sql.append("         case when (technic_no like 'mo-rsv-%') or (mobile_flag=1)");
                                sql.append("             then '�i�\��p�j'");
                                sql.append("             else ''");
                                sql.append("         end as product_name");
                                sql.append("     ,price");
                                sql.append("     ,price as base_price");
                                sql.append("     ,display_seq as base_display_seq");
                                sql.append("     ,display_seq");
                                sql.append("     ,operation_time");
                                sql.append("     ,praise_time_limit");
                                sql.append("     ,is_praise_time");
                                sql.append(" from");
                                sql.append("     mst_technic");
                                sql.append(" where");
                                sql.append("         technic_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()));
                                sql.append("     and delete_date is null");
                                sql.append(" order by");
                                sql.append("      display_seq");
                                sql.append("     ,technic_name");
                                sql.append("     ,technic_id");

                            }

                            break;

			//���i
			case 2:
                            if (shopID > 0) {
                                sql.append(" select");
                                sql.append("     mi.prepa_id");
                                sql.append("     ,mi.prepaid_price");
                                sql.append("      ,mi.item_id     as product_id");
                                sql.append("     ,mi.item_no     as product_no");
                                sql.append("     ,mi.item_name   as product_name");
                                sql.append("     ,mup.price");
                                sql.append("     ,mi.price as base_price");
                                sql.append("     ,mi.display_seq as base_display_seq");
                                sql.append("     ,mup.display_seq");
                                sql.append("     ,0 as operation_time");
                                sql.append("     ,mic.prepa_class_id");
                                sql.append(" from");
                                sql.append("     mst_use_product mup");
                                sql.append("         inner join mst_item mi");
                                sql.append("                 on mi.item_id = mup.product_id");
                                sql.append("                and mi.item_use_division in (1, 3) ");
                                sql.append("                and mi.item_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()));
                                sql.append("                and mi.delete_date is null");
                                sql.append("          inner join mst_item_class mic");
                                sql.append("                 on mi.item_class_id = mic.item_class_id");
                                sql.append(" where");
                                sql.append("         mup.delete_date is null");
                                sql.append("     and mup.shop_id = " + SQLUtil.convertForSQL(shopID));
                                sql.append("     and mup.product_division = 2");
                                sql.append(" order by");
                                sql.append("      mup.display_seq");
                                sql.append("     ,mi.item_name");
                                sql.append("     ,mi.item_id");

                            } else {

                                sql.append(" select");
                                sql.append("      item_id     as product_id");
                                sql.append("     ,item_no     as product_no");
                                sql.append("     ,item_name   as product_name");
                                sql.append("     ,price");
                                sql.append("     ,price as base_price");
                                sql.append("     ,display_seq as base_display_seq");
                                sql.append("     ,display_seq");
                                sql.append("     ,0 as operation_time");
                                sql.append(" from");
                                sql.append("     mst_item");
                                sql.append(" where");
                                sql.append("         item_use_division in (1, 3) ");
                                sql.append("     and item_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()));
                                sql.append("     and delete_date is null");
                                sql.append(" order by");
                                sql.append("      display_seq");
                                sql.append("     ,item_name");
                                sql.append("     ,item_id");

                            }

                            break;
			//�R�[�X
			case 3:
                                sql.append(" select");
                                sql.append("      mc.course_id      as product_id");
                                sql.append("     ,mc.course_id      as product_no");
                                sql.append("     ,mc.course_name as product_name");
                                sql.append("     ,mup.price");
                                sql.append("     ,mc.price as base_price");
                                sql.append("     ,mc.display_seq as base_display_seq");
                                sql.append("     ,mup.display_seq");
                                sql.append("     ,mc.operation_time");
                                sql.append("     ,mc.praise_time_limit");
                                sql.append("     ,mc.is_praise_time");
                                sql.append(" from");
                                sql.append("     mst_use_product mup");
                                sql.append("         inner join mst_course mc");
                                sql.append("                 on mc.course_id = mup.product_id");
                                sql.append("                and mc.course_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()));
                                sql.append("                and mc.delete_date is null");
                                sql.append(" where");
                                sql.append("         mup.delete_date is null");
                                sql.append("     and mup.shop_id = " + SQLUtil.convertForSQL(shopID));
                                sql.append("     and mup.product_division = 3");
                                sql.append(" order by");
                                sql.append("      mup.display_seq");
                                sql.append("     ,mc.course_name");
                                sql.append("     ,mc.course_id");

                                break;

		}

		return sql.toString();
	}

        public boolean loadProducts(ConnectionWrapper con, Integer productDivision, Integer shopID,boolean flag) throws SQLException
	{
		this.clear();

		if(productDivision != 1 && productDivision != 2 && productDivision != 3)	return	false;

		ResultSetWrapper	rs	=	con.executeQuery(
				this.getLoadProductsSQL(productDivision, shopID,flag));

		while(rs.next())
		{
			Product	p	=	new Product();
			p.setData(rs);
                        if(productDivision == 1){
                            p.setPraiseTimeLimit(rs.getInt("praise_time_limit"));
                            p.setPraiseTime(rs.getBoolean("is_praise_time"));
                        }

			//p.setProductClass((ProductClass)this.clone());
			ProductClass pc = new ProductClass();
			pc.setPrepa_class_id(prepa_class_id);
			pc.setProductClassID(productClassID);
			pc.setProductClassName(productClassName);
			pc.setShopCategoryID(shopCategoryID);
			pc.setDisplaySeq(displaySeq);
			p.setProductClass(pc);
			this.add(p);
		}

		rs.close();

		return	true;
	}

         public boolean loadProductsMission(ConnectionWrapper con, Integer productDivision, Integer shopID,boolean flag) throws SQLException
	{
		this.clear();

		if(productDivision != 1 && productDivision != 2 && productDivision != 3)	return	false;

		ResultSetWrapper	rs	=	con.executeQuery(
				this.getLoadProductsSQLMission(productDivision, shopID,flag));

		while(rs.next())
		{
			Product	p	=	new Product();
			p.setData(rs,productDivision);
                        if(productDivision == 1){
                            p.setPraiseTimeLimit(rs.getInt("praise_time_limit"));
                            p.setPraiseTime(rs.getBoolean("is_praise_time"));
                        }

			//p.setProductClass((ProductClass)this.clone());
			ProductClass pc = new ProductClass();
			pc.setPrepa_class_id(prepa_class_id);
			pc.setProductClassID(productClassID);
			pc.setProductClassName(productClassName);
			pc.setShopCategoryID(shopCategoryID);
			pc.setDisplaySeq(displaySeq);
			p.setProductClass(pc);
			this.add(p);
		}

		rs.close();

		return	true;
	}

        public String getLoadProductsSQL(Integer productDivision, Integer shopID,boolean flag)
	{
                StringBuilder sql = new StringBuilder(1000);

		switch(productDivision)
		{
			//�Z�p
			case 1:
                            if (shopID > 0) {
                                sql.append(" select");
                                sql.append("      mt.technic_id      as product_id");
                                sql.append("     ,mt.technic_no      as product_no");
                                sql.append("     ,mt.technic_name ||");
                                sql.append("         case when (mt.technic_no like 'mo-rsv-%') or (mt.mobile_flag=1)");
                                sql.append("             then '�i�\��p�j'");
                                sql.append("             else ''");
                                sql.append("         end as product_name");
                                sql.append("     ,mup.price");
                                sql.append("     ,mt.price as base_price");
                                sql.append("     ,mt.display_seq as base_display_seq");
                                sql.append("     ,mup.display_seq");
                                sql.append("     ,mt.operation_time");
                                sql.append("     ,mt.praise_time_limit");
                                sql.append("     ,mt.is_praise_time");
                                sql.append(" from");
                                sql.append("     mst_use_product mup");
                                sql.append("         inner join mst_technic mt");
                                sql.append("                 on mt.technic_id = mup.product_id");
                                sql.append("                and mt.technic_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()));
                                sql.append("                and mt.delete_date is null");
                                sql.append(" where");
                                sql.append("         mup.delete_date is null");
                                sql.append("     and mup.shop_id = " + SQLUtil.convertForSQL(shopID));
                                sql.append("     and mup.product_division = 1");
                                sql.append(" order by");
                                sql.append("      mup.display_seq");
                                sql.append("     ,mt.technic_name");
                                sql.append("     ,mt.technic_id");

                            } else {

                                sql.append(" select");
                                sql.append("      technic_id      as product_id");
                                sql.append("     ,technic_no      as product_no");
                                sql.append("     ,technic_name ||");
                                sql.append("         case when (technic_no like 'mo-rsv-%') or (mobile_flag=1)");
                                sql.append("             then '�i�\��p�j'");
                                sql.append("             else ''");
                                sql.append("         end as product_name");
                                sql.append("     ,price");
                                sql.append("     ,price as base_price");
                                sql.append("     ,display_seq as base_display_seq");
                                sql.append("     ,display_seq");
                                sql.append("     ,operation_time");
                                sql.append("     ,praise_time_limit");
                                sql.append("     ,is_praise_time");
                                sql.append(" from");
                                sql.append("     mst_technic");
                                sql.append(" where");
                                sql.append("         technic_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()));
                                sql.append("     and delete_date is null");
                                sql.append(" order by");
                                sql.append("      display_seq");
                                sql.append("     ,technic_name");
                                sql.append("     ,technic_id");

                            }

                            break;

			//���i
			case 2:
                            if (shopID > 0) {
                                sql.append(" select");
                               // sql.append("     mi.prepa_id");
                                //sql.append("     ,mi.prepaid_price");
                                sql.append("      mi.item_id     as product_id");
                                sql.append("     ,mi.item_no     as product_no");
                                sql.append("     ,mi.item_name   as product_name");
                                sql.append("     ,mup.price");
                                sql.append("     ,mi.price as base_price");
                                sql.append("     ,mi.display_seq as base_display_seq");
                                sql.append("     ,mup.display_seq");
                                sql.append("     ,0 as operation_time");
                               // sql.append("     ,mic.prepa_class_id");
                                sql.append(" from");
                                sql.append("     mst_use_product mup");
                                sql.append("         inner join mst_item mi");
                                sql.append("                 on mi.item_id = mup.product_id");
                                sql.append("                and mi.item_use_division in (1, 3) ");
                                sql.append("                and mi.item_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()));
                                sql.append("                and mi.delete_date is null");
                                sql.append("          inner join mst_item_class mic");
                                sql.append("                 on mi.item_class_id = mic.item_class_id");
                                sql.append(" where");
                                sql.append("         mup.delete_date is null");
                             /*  if(flag==true){
                               sql.append("     and mic.prepa_class_id= 1");
                               }
                               else {
                                    sql.append("     and (mic.prepa_class_id is null or mic.prepa_class_id  <> 1)");
                               }*/
                                sql.append("     and mup.shop_id = " + SQLUtil.convertForSQL(shopID));
                                sql.append("     and mup.product_division = 2");
                                sql.append(" order by");
                                sql.append("      mup.display_seq");
                                sql.append("     ,mi.item_name");
                                sql.append("     ,mi.item_id");

                            } else {

                                sql.append(" select");
                                sql.append("      item_id     as product_id");
                                sql.append("     ,item_no     as product_no");
                                sql.append("     ,item_name   as product_name");
                                sql.append("     ,price");
                                sql.append("     ,price as base_price");
                                sql.append("     ,display_seq as base_display_seq");
                                sql.append("     ,display_seq");
                                sql.append("     ,0 as operation_time");
                                sql.append(" from");
                                sql.append("     mst_item");
                                sql.append(" where");
                                sql.append("         item_use_division in (1, 3) ");
                                sql.append("     and item_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()));
                                sql.append("     and delete_date is null");
                                sql.append(" order by");
                                sql.append("      display_seq");
                                sql.append("     ,item_name");
                                sql.append("     ,item_id");

                            }

                            break;
			//�R�[�X
			case 3:
                                sql.append(" select");
                                sql.append("      mc.course_id      as product_id");
                                sql.append("     ,mc.course_id      as product_no");
                                sql.append("     ,mc.course_name as product_name");
                                sql.append("     ,mup.price");
                                sql.append("     ,mc.price as base_price");
                                sql.append("     ,mc.display_seq as base_display_seq");
                                sql.append("     ,mup.display_seq");
                                sql.append("     ,mc.operation_time");
                                sql.append("     ,mc.praise_time_limit");
                                sql.append("     ,mc.is_praise_time");
                                sql.append(" from");
                                sql.append("     mst_use_product mup");
                                sql.append("         inner join mst_course mc");
                                sql.append("                 on mc.course_id = mup.product_id");
                                sql.append("                and mc.course_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()));
                                sql.append("                and mc.delete_date is null");
                                sql.append(" where");
                                sql.append("         mup.delete_date is null");
                                sql.append("     and mup.shop_id = " + SQLUtil.convertForSQL(shopID));
                                sql.append("     and mup.product_division = 3");
                                sql.append(" order by");
                                sql.append("      mup.display_seq");
                                sql.append("     ,mc.course_name");
                                sql.append("     ,mc.course_id");

                                break;

		}

		return sql.toString();
	}
          public String getLoadProductsSQLMission(Integer productDivision, Integer shopID,boolean flag)
	{
                StringBuilder sql = new StringBuilder(1000);

		switch(productDivision)
		{
			//�Z�p
			case 1:
                            if (shopID > 0) {
                                sql.append(" select");
                                sql.append("      mt.technic_id      as product_id");
                                sql.append("     ,mt.technic_no      as product_no");
                                sql.append("     ,mt.technic_name ||");
                                sql.append("         case when (mt.technic_no like 'mo-rsv-%') or (mt.mobile_flag=1)");
                                sql.append("             then '�i�\��p�j'");
                                sql.append("             else ''");
                                sql.append("         end as product_name");
                                sql.append("     ,mup.price");
                                sql.append("     ,mt.price as base_price");
                                sql.append("     ,mt.display_seq as base_display_seq");
                                sql.append("     ,mup.display_seq");
                                sql.append("     ,mt.operation_time");
                                sql.append("     ,mt.praise_time_limit");
                                sql.append("     ,mt.is_praise_time");
                                sql.append(" from");
                                sql.append("     mst_use_product mup");
                                sql.append("         inner join mst_technic mt");
                                sql.append("                 on mt.technic_id = mup.product_id");
                                sql.append("                and mt.technic_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()));
                                sql.append("                and mt.delete_date is null");
                                sql.append(" where");
                                sql.append("         mup.delete_date is null");
                                sql.append("     and mup.shop_id = " + SQLUtil.convertForSQL(shopID));
                                sql.append("     and mup.product_division = 1");
                                sql.append(" order by");
                                sql.append("      mup.display_seq");
                                sql.append("     ,mt.technic_name");
                                sql.append("     ,mt.technic_id");

                            } else {

                                sql.append(" select");
                                sql.append("      technic_id      as product_id");
                                sql.append("     ,technic_no      as product_no");
                                sql.append("     ,technic_name ||");
                                sql.append("         case when (technic_no like 'mo-rsv-%') or (mobile_flag=1)");
                                sql.append("             then '�i�\��p�j'");
                                sql.append("             else ''");
                                sql.append("         end as product_name");
                                sql.append("     ,price");
                                sql.append("     ,price as base_price");
                                sql.append("     ,display_seq as base_display_seq");
                                sql.append("     ,display_seq");
                                sql.append("     ,operation_time");
                                sql.append("     ,praise_time_limit");
                                sql.append("     ,is_praise_time");
                                sql.append(" from");
                                sql.append("     mst_technic");
                                sql.append(" where");
                                sql.append("         technic_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()));
                                sql.append("     and delete_date is null");
                                sql.append(" order by");
                                sql.append("      display_seq");
                                sql.append("     ,technic_name");
                                sql.append("     ,technic_id");

                            }

                            break;

			//���i
			case 2:
                            if (shopID > 0) {
                                sql.append(" select");
                                sql.append("     mi.prepa_id");
                                sql.append("     ,mi.prepaid_price");
                                sql.append("      ,mi.item_id     as product_id");
                                sql.append("     ,mi.item_no     as product_no");
                                sql.append("     ,mi.item_name   as product_name");
                                sql.append("     ,mup.price");
                                sql.append("     ,mi.price as base_price");
                                sql.append("     ,mi.display_seq as base_display_seq");
                                sql.append("     ,mup.display_seq");
                                sql.append("     ,0 as operation_time");
                                sql.append("     ,mic.prepa_class_id");
                                sql.append(" from");
                                sql.append("     mst_use_product mup");
                                sql.append("         inner join mst_item mi");
                                sql.append("                 on mi.item_id = mup.product_id");
                                sql.append("                and mi.item_use_division in (1, 3) ");
                                sql.append("                and mi.item_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()));
                                sql.append("                and mi.delete_date is null");
                                sql.append("          inner join mst_item_class mic");
                                sql.append("                 on mi.item_class_id = mic.item_class_id");
                                sql.append(" where");
                                sql.append("         mup.delete_date is null");
                               if(flag==true){
                               sql.append("     and mic.prepa_class_id= 1");
                               }
                               else {
                                    sql.append("     and (mic.prepa_class_id is null or mic.prepa_class_id  <> 1)");
                               }
                                sql.append("     and mup.shop_id = " + SQLUtil.convertForSQL(shopID));
                                sql.append("     and mup.product_division = 2");
                                sql.append(" order by");
                                sql.append("      mup.display_seq");
                                sql.append("     ,mi.item_name");
                                sql.append("     ,mi.item_id");

                            } else {

                                sql.append(" select");
                                sql.append("      item_id     as product_id");
                                sql.append("     ,item_no     as product_no");
                                sql.append("     ,item_name   as product_name");
                                sql.append("     ,price");
                                sql.append("     ,price as base_price");
                                sql.append("     ,display_seq as base_display_seq");
                                sql.append("     ,display_seq");
                                sql.append("     ,0 as operation_time");
                                sql.append(" from");
                                sql.append("     mst_item");
                                sql.append(" where");
                                sql.append("         item_use_division in (1, 3) ");
                                sql.append("     and item_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()));
                                sql.append("     and delete_date is null");
                                sql.append(" order by");
                                sql.append("      display_seq");
                                sql.append("     ,item_name");
                                sql.append("     ,item_id");

                            }

                            break;
			//�R�[�X
			case 3:
                                sql.append(" select");
                                sql.append("      mc.course_id      as product_id");
                                sql.append("     ,mc.course_id      as product_no");
                                sql.append("     ,mc.course_name as product_name");
                                sql.append("     ,mup.price");
                                sql.append("     ,mc.price as base_price");
                                sql.append("     ,mc.display_seq as base_display_seq");
                                sql.append("     ,mup.display_seq");
                                sql.append("     ,mc.operation_time");
                                sql.append("     ,mc.praise_time_limit");
                                sql.append("     ,mc.is_praise_time");
                                sql.append(" from");
                                sql.append("     mst_use_product mup");
                                sql.append("         inner join mst_course mc");
                                sql.append("                 on mc.course_id = mup.product_id");
                                sql.append("                and mc.course_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()));
                                sql.append("                and mc.delete_date is null");
                                sql.append(" where");
                                sql.append("         mup.delete_date is null");
                                sql.append("     and mup.shop_id = " + SQLUtil.convertForSQL(shopID));
                                sql.append("     and mup.product_division = 3");
                                sql.append(" order by");
                                sql.append("      mup.display_seq");
                                sql.append("     ,mc.course_name");
                                sql.append("     ,mc.course_id");

                                break;

		}

		return sql.toString();
	}


	public void sort()
	{
		Collections.sort(this, new ProductComparator());
	}


	private class ProductComparator implements Comparator<Product>
	{
		public int compare(Product p0, Product p1)
		{
			return	p0.getDisplaySeq().compareTo(p1.getDisplaySeq());
		}
	}

        /**
	 * �X�̏��i�O���[�v�ڍׂ�ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @param productDivision ���i����
	 * @return true - �����Afalse - ���s
	 * @throws java.sql.SQLException SQLException
         * @author ttmloan
         * @since 2014/12/29
	 */
	public boolean loadProductsRankItemDetail(ConnectionWrapper con, Integer productDivision) throws SQLException
	{
		this.clear();

		ResultSetWrapper	rs	=	con.executeQuery(
				this.getLoadProductsForRankItemDetailSQL(productDivision));

		while(rs.next())
		{
			Product	p	=	new Product();
			p.setProductID(rs.getInt("product_id"));
                        p.setProductName(rs.getString("product_name"));
                        p.setPrice(rs.getLong("price"));
                        p.setDisplaySeq(rs.getInt("display_seq"));
			p.setProductClass(this);
			this.add(p);
		}

		rs.close();

		return	true;
	}

        /**
	 * �X�̏��i�O���[�v�ڍׂ�ǂݍ��ނr�p�k�����擾����B
	 * @param productDivision ���i����
	 * @return ���i��ǂݍ��ނr�p�k��
         * @author ttmloan
         * @since 2014/12/29
	 */
	public String getLoadProductsForRankItemDetailSQL(Integer productDivision)
	{
		String	sql = "select item_id as product_id,\n" +
                                "item_no as product_no,\n" +
                                "item_name as product_name, price, display_seq\n" +
                                "from mst_item \n" +
                                "where delete_date is null\n" +
                                "and item_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()) + "\n" +
                                "order by display_seq\n";
		return	sql;
	}

        public boolean loadCourse(ConnectionWrapper con, String listShopID) throws SQLException
	{
		this.clear();

                if(listShopID.trim().equals("")) {
                    return	false;
                }

		StringBuilder sql = new StringBuilder(1000);

                sql.append(" select");
                sql.append("      mc.course_id      as product_id");
                sql.append("     ,mc.course_id      as product_no");
                sql.append("     ,mc.course_name as product_name");
                sql.append("     ,mup.price");
                sql.append("     ,mc.price as base_price");
                sql.append("     ,mc.display_seq as base_display_seq");
                sql.append("     ,mup.display_seq");
                sql.append("     ,mc.operation_time");
                sql.append("     ,mc.praise_time_limit");
                sql.append("     ,mc.is_praise_time");
                sql.append(" from");
                sql.append("     mst_use_product mup");
                sql.append("         inner join mst_course mc");
                sql.append("                 on mc.course_id = mup.product_id");
                sql.append("                and mc.course_class_id = " + SQLUtil.convertForSQL(this.getProductClassID()));
                sql.append("                and mc.delete_date is null");
                sql.append(" where");
                sql.append("         mup.delete_date is null");
                sql.append("     and mup.shop_id in ( " + SQLUtil.convertForSQL(listShopID) + ")");
                sql.append("     and mup.product_division = 3");
                sql.append(" order by");
                sql.append("      mup.display_seq");
                sql.append("     ,mc.course_name");
                sql.append("     ,mc.course_id");


		ResultSetWrapper	rs	=	con.executeQuery(sql.toString());

		while(rs.next())
		{
			Product	p	=	new Product();
			p.setData(rs);
			//p.setProductClass((ProductClass)this.clone());
			ProductClass pc = new ProductClass();
			pc.setPrepa_class_id(prepa_class_id);
			pc.setProductClassID(productClassID);
			pc.setProductClassName(productClassName);
			pc.setShopCategoryID(shopCategoryID);
			pc.setDisplaySeq(displaySeq);
			p.setProductClass(pc);
			this.add(p);
		}

		rs.close();

		return	true;
	}
}
