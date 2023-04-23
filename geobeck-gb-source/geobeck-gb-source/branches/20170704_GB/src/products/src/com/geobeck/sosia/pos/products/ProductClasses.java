/*
 * ProductClasses.java
 *
 * Created on 2006/05/26, 20:32
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
 * �Z�p�E���i���ރ}�X�^�f�[�^��ArrayList
 * @author katagiri
 */
public class ProductClasses extends Vector<ProductClass>
{
	/**
	 * �����敪�i1�F�Z�p�A2�G���i�j
	 */
	protected	Integer		productDivision		=	0;
	
	/**
	 * �R���X�g���N�^
	 */
	public ProductClasses()
	{
	}

	/**
	 * �����敪�i1�F�Z�p�A2�G���i�j
	 * @return �����敪�i1�F�Z�p�A2�G���i�j
	 */
	public Integer getProductDivision()
	{
		return productDivision;
	}

	/**
	 * �����敪�i1�F�Z�p�A2�G���i�j
	 * @param productDivision �����敪�i1�F�Z�p�A2�G���i�j
	 */
	public void setProductDivision(Integer productDivision)
	{
		this.productDivision = productDivision;
	}
	
	/**
	 * ���i���ނ��擾����B
	 * @param productClassID ���i���ނh�c
	 * @return ���i����
	 */
	public ProductClass getProductClass(Integer productClassID)
	{
		for(ProductClass pc : this)
		{
			if(pc.getProductClassID().intValue() == productClassID)
			{
				return	pc;
			}
		}
		
		return	null;
	}
	
	/**
	 * ���i���ނ̃C���f�b�N�X���擾����B
	 * @param productClassID ���i���ނh�c
	 * @return ���i���ނ̃C���f�b�N�X
	 */
	public Integer getProductClassIndex(Integer productClassID)
	{
		for(Integer i = 0; i < this.size(); i ++)
		{
			if(this.get(i).getProductClassID().intValue() == productClassID.intValue())
			{       
				return	i;
			}
		}
		
		return	null;
	}
	
	/**
	 * �f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @return true - �����Afalse - ���s
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean load(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		if(productDivision != 1 && productDivision != 2 && productDivision != 3)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getLoadSQL());

		while(rs.next())
		{
			ProductClass	pc	=	new ProductClass();
			pc.setData(rs);
			this.add(pc);
		}

		rs.close();
		
		return	true;
	}
      
	
	/**
	 * �f�[�^��ǂݍ��ނr�p�k�����擾����B
	 * @return �f�[�^��ǂݍ��ނr�p�k��
	 */
	public String getLoadSQL()
	{
		String	sql		=	"";
		
		switch(productDivision)
		{
			//�Z�p
			case 1:
				sql	=	"select technic_class_id as product_class_id,\n" +
						"technic_class_name as product_class_name, display_seq\n" +
                                                // IVS_Thanh start add 2014/07/14 Mashu_����v�\��
                                                ",shop_category_id\n" +
                                                // IVS_Thanh end add 2014/07/14 Mashu_����v�\��
						"from mst_technic_class mtc\n" +
						"where delete_date is null\n" +
						"order by display_seq, technic_class_name, technic_class_id\n";
				break;
			//���i
			case 2:
				sql	=	"select item_class_id as product_class_id,\n" +
						"item_class_name as product_class_name, display_seq\n" +
                                                // IVS_Thanh start add 2014/07/14 Mashu_����v�\��
                                                ",shop_category_id\n" +
                                                // IVS_Thanh end add 2014/07/14 Mashu_����v�\��
						"from mst_item_class mgc\n" +
						"where delete_date is null\n" +
						"order by display_seq, item_class_name, item_class_id\n";
				break;
                        //�R�[�X
                        case 3:
                                sql     =       "select course_class_id as product_class_id, \n" +
                                                "course_class_name as product_class_name, display_seq\n" +
                                                // IVS_Thanh start add 2014/07/14 Mashu_����v�\��
                                                ",shop_category_id\n" +
                                                // IVS_Thanh end add 2014/07/14 Mashu_����v�\��
                                                "from mst_course_class\n" +
                                                "where delete_date is null\n" +
                                                "order by display_seq, course_class_name, course_class_id";
                            break;

		}
		
		return	sql;
	}
	
	/**
	 * �f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @return true - �����Afalse - ���s
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean load(ConnectionWrapper con, Integer shopID) throws SQLException
	{
            this.clear();

            if(productDivision != 1 && productDivision != 2 && productDivision != 3) return false;

            ResultSetWrapper rs = con.executeQuery(this.getLoadSQL(shopID));

            while (rs.next()) {
                ProductClass pc = new ProductClass();
                pc.setData(rs);
                this.add(pc);
            }

            rs.close();

            return true;
	}
        public boolean loadMisson(ConnectionWrapper con, Integer shopID) throws SQLException
	{
            this.clear();

            if(productDivision != 1 && productDivision != 2 && productDivision != 3) return false;

            ResultSetWrapper rs = con.executeQuery(this.getLoadSQLMission(shopID));

            while (rs.next()) {
                ProductClass pc = new ProductClass();
                pc.setData(rs,productDivision);
                this.add(pc);
            }

            rs.close();

            return true;
	}
        
        public boolean loadItemClasses(ConnectionWrapper con, Integer shopID,String integrationId) throws SQLException
	{
            this.clear();

            if(productDivision != 1 && productDivision != 2 && productDivision != 3) return false;

            ResultSetWrapper rs = con.executeQuery(this.getLoadItemClassesSQL(shopID,integrationId));

            while (rs.next()) {
                ProductClass pc = new ProductClass();
                pc.setData(rs,productDivision);
                this.add(pc);
            }

            rs.close();

            return true;
	}
        
        public boolean load(ConnectionWrapper con, Integer shopID,String integrationId) throws SQLException
	{
            this.clear();
            ResultSetWrapper rs = con.executeQuery(this.getLoadSQL(shopID,integrationId));

            while (rs.next()) {
                ProductClass pc = new ProductClass();
                pc.setData(rs);
                this.add(pc);
            }

            rs.close();

            return true;
	}
        
        public String getLoadSQL(Integer shopID,String integrationId)
	{
            StringBuilder sql = new StringBuilder(1000);
            
            if (shopID > 0) {
                        sql.append(" select");
                        sql.append("      mtc.technic_class_id as product_class_id");
                        sql.append("     ,mtc.technic_class_name as product_class_name");
                        sql.append("     ,mtc.display_seq");
                        // Thanh start add 2014/07/11 Mashu_����v�\��
                        sql.append("     ,mtc.shop_category_id");
                        // Thanh end add 2014/07/11
                        sql.append(" from");
                        sql.append("     (");
                        sql.append("         select distinct");
                        sql.append("             mt.technic_class_id");
                        sql.append("         from");
                        sql.append("             mst_use_product mup");
                        sql.append("                 inner join mst_technic mt");
                        sql.append("                         on mt.technic_id = mup.product_id");
                        sql.append("                        and mt.delete_date is null");
                        sql.append("         where");
                        sql.append("                 mup.delete_date is null");
                        sql.append("             and mup.shop_id = " + SQLUtil.convertForSQL(shopID));
                        sql.append("             and mup.product_division = 1");
                        sql.append("     ) mt");
                        sql.append("     inner join mst_technic_class mtc");
                        sql.append("             on mtc.technic_class_id = mt.technic_class_id");
                        sql.append("            and mtc.delete_date is null");
                        if(!"".equals(integrationId)){
                                sql.append(" where");
                                sql.append("     mtc.technic_integration_id = ").append(integrationId);
                        }
                        sql.append(" order by");
                        sql.append("     mtc.display_seq");

                    } else {

                        sql.append(" select");
                        sql.append("      technic_class_id as product_class_id");
                        sql.append("     ,technic_class_name as product_class_name");
                        sql.append("     ,display_seq");
                        // Thanh start add 2014/07/11 Mashu_����v�\��
                        sql.append("     ,shop_category_id");
                        // Thanh end add 2014/07/11
                        sql.append(" from");
                        sql.append("     mst_technic_class");
                        sql.append(" where");
                        sql.append("     delete_date is null");
                        if(!"".equals(integrationId)){
                                sql.append("     and technic_integration_id = ").append(integrationId);
                        }
                        sql.append(" order by");
                        sql.append("     display_seq");
            }

            return sql.toString();
	}
        
	
	/**
	 * �f�[�^��ǂݍ��ނr�p�k�����擾����B
	 * @return �f�[�^��ǂݍ��ނr�p�k��
	 */
	public String getLoadSQL(Integer shopID)
	{
            StringBuilder sql = new StringBuilder(1000);
            
            switch(productDivision)
            {
                //�Z�p
                case 1:
                    if (shopID > 0) {
                        sql.append(" select");
                        sql.append("      mtc.technic_class_id as product_class_id");
                        sql.append("     ,mtc.technic_class_name as product_class_name");
                        sql.append("     ,mtc.display_seq");
                        // IVS_Thanh start add 2014/07/14 Mashu_����v�\��
                        sql.append("     ,mtc.shop_category_id");
                        // IVS_Thanh start end 2014/07/14 Mashu_����v�\��                       
                        sql.append(" from");
                        sql.append("     (");
                        sql.append("         select distinct");
                        sql.append("             mt.technic_class_id");
                        sql.append("         from");
                        sql.append("             mst_use_product mup");
                        sql.append("                 inner join mst_technic mt");
                        sql.append("                         on mt.technic_id = mup.product_id");
                        sql.append("                        and mt.delete_date is null");
                      
                        sql.append("         where");
                        sql.append("                 mup.delete_date is null");
                        sql.append("             and mup.shop_id = " + SQLUtil.convertForSQL(shopID));
                        sql.append("             and mup.product_division = 1");
                        sql.append("     ) mt");
                        sql.append("     inner join mst_technic_class mtc");
                        sql.append("             on mtc.technic_class_id = mt.technic_class_id");
                        sql.append("            and mtc.delete_date is null");
                        sql.append(" order by");
                        sql.append("     mtc.display_seq");

                    } else {

                        sql.append(" select");
                        sql.append("      technic_class_id as product_class_id");
                        sql.append("     ,technic_class_name as product_class_name");
                        sql.append("     ,display_seq");
                        // IVS_Thanh start add 2014/07/14 Mashu_����v�\��
                        sql.append("     ,shop_category_id");
                        // IVS_Thanh start end 2014/07/14 Mashu_����v�\��
                        sql.append(" from");
                        sql.append("     mst_technic_class");
                        sql.append(" where");
                        sql.append("     delete_date is null");
                        sql.append(" order by");
                        sql.append("     display_seq");
                    }

                    break;

                //���i
                case 2:
                    if (shopID > 0) {
                        sql.append(" select");
                        sql.append("      mic.item_class_id as product_class_id");
                        sql.append("     ,mic.item_class_name as product_class_name");
                        sql.append("     ,mic.display_seq");
                        // IVS_Thanh start add 2014/07/14 Mashu_����v�\��
                        sql.append("     ,mic.shop_category_id");
                        // IVS_Thanh start end 2014/07/14 Mashu_����v�\��
                        sql.append(" from");
                        sql.append("     (");
                        sql.append("         select distinct");
                        sql.append("             mi.item_class_id");
                        sql.append("         from");
                        sql.append("             mst_use_product mup");
                        sql.append("                 inner join mst_item mi");
                        sql.append("                         on mi.item_id = mup.product_id");
                        sql.append("                        and mi.delete_date is null");
                        sql.append("         where");
                        sql.append("                 mup.delete_date is null");
                        sql.append("             and mup.shop_id = " + SQLUtil.convertForSQL(shopID));
                        sql.append("             and mup.product_division = 2");
                        sql.append("     ) mi");
                        sql.append("     inner join mst_item_class mic");
                        sql.append("             on mic.item_class_id = mi.item_class_id");
                        sql.append("            and mic.delete_date is null");
                        sql.append(" order by");
                        sql.append("     mic.display_seq");

                    } else {

                        sql.append(" select");
                        sql.append("      item_class_id as product_class_id");
                        sql.append("     ,item_class_name as product_class_name");
                        sql.append("     ,display_seq");
                        // IVS_Thanh start add 2014/07/14 Mashu_����v�\��
                        sql.append("     ,shop_category_id");
                        // IVS_Thanh start end 2014/07/14 Mashu_����v�\��
                        sql.append(" from");
                        sql.append("     mst_item_class");
                        sql.append(" where");
                        sql.append("     delete_date is null");
                        sql.append(" order by");
                        sql.append("     display_seq");
                    }

                    break;

                    //�R�[�X
                case 3:
                    sql.append(" select");
                    sql.append("      mcc.course_class_id as product_class_id");
                    sql.append("     ,mcc.course_class_name as product_class_name");
                    sql.append("     ,mcc.display_seq");
                    // IVS_Thanh start add 2014/07/14 Mashu_����v�\��
                    sql.append("     ,mcc.shop_category_id");
                    // IVS_Thanh start end 2014/07/14 Mashu_����v�\��
                    sql.append(" from");
                    sql.append("     (");
                    sql.append("         select distinct");
                    sql.append("             mc.course_class_id");
                    sql.append("         from");
                    sql.append("             mst_use_product mup");
                    sql.append("                 inner join mst_course mc");
                    sql.append("                         on mc.course_id = mup.product_id");
                    sql.append("                        and mc.delete_date is null");
                    sql.append("         where");
                    sql.append("                 mup.delete_date is null");
                    sql.append("             and mup.shop_id = " + SQLUtil.convertForSQL(shopID));
                    sql.append("             and mup.product_division = 3");
                    sql.append("     ) mc");
                    sql.append("     inner join mst_course_class mcc");
                    sql.append("             on mcc.course_class_id = mc.course_class_id");
                    sql.append("            and mcc.delete_date is null");
                    sql.append(" order by");
                    sql.append("     mcc.display_seq");

                    break;
                 //�R�[�X

            }

            return sql.toString();
	}
        
        public String getLoadSQLMission(Integer shopID)
	{
            StringBuilder sql = new StringBuilder(1000);
            
            switch(productDivision)
            {
                //�Z�p
                case 1:
                    if (shopID > 0) {
                        sql.append(" select");
                        sql.append("      mtc.technic_class_id as product_class_id");
                        sql.append("     ,mtc.technic_class_name as product_class_name");
                        sql.append("     ,mtc.display_seq");
                       
                        sql.append(" from");
                        sql.append("     (");
                        sql.append("         select distinct");
                        sql.append("             mt.technic_class_id");
                        sql.append("         from");
                        sql.append("             mst_use_product mup");
                        sql.append("                 inner join mst_technic mt");
                        sql.append("                         on mt.technic_id = mup.product_id");
                        sql.append("                        and mt.delete_date is null");
                      
                        sql.append("         where");
                        sql.append("                 mup.delete_date is null");
                        sql.append("             and mup.shop_id = " + SQLUtil.convertForSQL(shopID));
                        sql.append("             and mup.product_division = 1");
                        sql.append("     ) mt");
                        sql.append("     inner join mst_technic_class mtc");
                        sql.append("             on mtc.technic_class_id = mt.technic_class_id");
                        sql.append("            and mtc.delete_date is null");
                        sql.append(" order by");
                        sql.append("     mtc.display_seq");

                    } else {

                        sql.append(" select");
                        sql.append("      technic_class_id as product_class_id");
                        sql.append("     ,technic_class_name as product_class_name");
                        sql.append("     ,display_seq");
                        sql.append(" from");
                        sql.append("     mst_technic_class");
                        sql.append(" where");
                        sql.append("     delete_date is null");
                        sql.append(" order by");
                        sql.append("     display_seq");
                    }

                    break;

                //���i
                case 2:
                    if (shopID > 0) {
                        sql.append(" select");
                        sql.append("      mic.item_class_id as product_class_id");
                        sql.append("     ,mic.item_class_name as product_class_name");
                        sql.append("     ,mic.display_seq");
                        sql.append("     ,mic.prepa_class_id");
                        sql.append(" from");
                        sql.append("     (");
                        sql.append("         select distinct");
                        sql.append("             mi.item_class_id");
                        sql.append("         from");
                        sql.append("             mst_use_product mup");
                        sql.append("                 inner join mst_item mi");
                        sql.append("                         on mi.item_id = mup.product_id");
                        sql.append("                        and mi.delete_date is null");
                        sql.append("         where");
                        sql.append("                 mup.delete_date is null");
                        sql.append("             and mup.shop_id = " + SQLUtil.convertForSQL(shopID));
                        sql.append("             and mup.product_division = 2");
                        sql.append("     ) mi");
                        sql.append("     inner join mst_item_class mic");
                        sql.append("             on mic.item_class_id = mi.item_class_id");
                        sql.append("            and mic.delete_date is null");
                        sql.append(" order by");
                        sql.append("     mic.display_seq");

                    } else {

                        sql.append(" select");
                        sql.append("      item_class_id as product_class_id");
                        sql.append("     ,item_class_name as product_class_name");
                        sql.append("     ,display_seq");
                        //add by ltthuc 2014/06/20
                        sql.append("     ,prepa_class_id");
                        //end add by ltthuc
                        sql.append(" from");
                        sql.append("     mst_item_class");
                        sql.append(" where");
                        sql.append("     delete_date is null");
                        sql.append(" order by");
                        sql.append("     display_seq");
                    }

                    break;

                    //�R�[�X
                case 3:
                    sql.append(" select");
                    sql.append("      mcc.course_class_id as product_class_id");
                    sql.append("     ,mcc.course_class_name as product_class_name");
                    sql.append("     ,mcc.display_seq");
                    sql.append(" from");
                    sql.append("     (");
                    sql.append("         select distinct");
                    sql.append("             mc.course_class_id");
                    sql.append("         from");
                    sql.append("             mst_use_product mup");
                    sql.append("                 inner join mst_course mc");
                    sql.append("                         on mc.course_id = mup.product_id");
                    sql.append("                        and mc.delete_date is null");
                    sql.append("         where");
                    sql.append("                 mup.delete_date is null");
                    sql.append("             and mup.shop_id = " + SQLUtil.convertForSQL(shopID));
                    sql.append("             and mup.product_division = 3");
                    sql.append("     ) mc");
                    sql.append("     inner join mst_course_class mcc");
                    sql.append("             on mcc.course_class_id = mc.course_class_id");
                    sql.append("            and mcc.delete_date is null");
                    sql.append(" order by");
                    sql.append("     mcc.display_seq");

                    break;
                 //�R�[�X

            }

            return sql.toString();
	}
        
        public String getLoadItemClassesSQL(Integer shopID,String integrationId)
	{
            StringBuilder sql = new StringBuilder(1000);
            
            switch(productDivision)
            {
                //�Z�p
                case 1:
                    if (shopID > 0) {
                        sql.append(" select");
                        sql.append("      mtc.technic_class_id as product_class_id");
                        sql.append("     ,mtc.technic_class_name as product_class_name");
                        sql.append("     ,mtc.display_seq");
                        //IVS_LVTu start add 2015/06/26 New request #38256
                        sql.append("     ,mtc.shop_category_id");
                        //IVS_LVTu end add 2015/06/26 New request #38256
                       
                        sql.append(" from");
                        sql.append("     (");
                        sql.append("         select distinct");
                        sql.append("             mt.technic_class_id");
                        sql.append("         from");
                        sql.append("             mst_use_product mup");
                        sql.append("                 inner join mst_technic mt");
                        sql.append("                         on mt.technic_id = mup.product_id");
                        sql.append("                        and mt.delete_date is null");
                      
                        sql.append("         where");
                        sql.append("                 mup.delete_date is null");
                        sql.append("             and mup.shop_id = " + SQLUtil.convertForSQL(shopID));
                        sql.append("             and mup.product_division = 1");
                        sql.append("     ) mt");
                        sql.append("     inner join mst_technic_class mtc");
                        sql.append("             on mtc.technic_class_id = mt.technic_class_id");
                        sql.append("            and mtc.delete_date is null");
                        if(!"".equals(integrationId)){
                                sql.append(" where");
                                sql.append("     mtc.technic_integration_id = ").append(integrationId);
                        }
                        sql.append(" order by");
                        sql.append("     mtc.display_seq");

                    } else {

                        sql.append(" select");
                        sql.append("      technic_class_id as product_class_id");
                        sql.append("     ,technic_class_name as product_class_name");
                        sql.append("     ,display_seq");
                        //IVS_LVTu start add 2015/06/26 New request #38256
                        sql.append("     ,shop_category_id");
                        //IVS_LVTu end add 2015/06/26 New request #38256
                        sql.append(" from");
                        sql.append("     mst_technic_class");
                        sql.append(" where");
                        sql.append("     delete_date is null");
                        if(!"".equals(integrationId)){
                                sql.append("     and technic_integration_id = ").append(integrationId);
                        }
                        sql.append(" order by");
                        sql.append("     display_seq");
                    }

                    break;

                //���i
                case 2:
                    if (shopID > 0) {
                        sql.append(" select");
                        sql.append("      mic.item_class_id as product_class_id");
                        sql.append("     ,mic.item_class_name as product_class_name");
                        sql.append("     ,mic.display_seq");
                        sql.append("     ,mic.prepa_class_id");
                        //IVS_LVTu start add 2015/06/26 New request #38256
                        sql.append("     ,mic.shop_category_id");
                        //IVS_LVTu end add 2015/06/26 New request #38256
                        sql.append(" from");
                        sql.append("     (");
                        sql.append("         select distinct");
                        sql.append("             mi.item_class_id");
                        sql.append("         from");
                        sql.append("             mst_use_product mup");
                        sql.append("                 inner join mst_item mi");
                        sql.append("                         on mi.item_id = mup.product_id");
                        sql.append("                        and mi.delete_date is null");
                        sql.append("         where");
                        sql.append("                 mup.delete_date is null");
                        sql.append("             and mup.shop_id = " + SQLUtil.convertForSQL(shopID));
                        sql.append("             and mup.product_division = 2");
                        sql.append("     ) mi");
                        sql.append("     inner join mst_item_class mic");
                        sql.append("             on mic.item_class_id = mi.item_class_id");
                        sql.append("            and mic.delete_date is null");
                        if(!"".equals(integrationId)){
                                sql.append(" where");
                                sql.append("     mic.item_integration_id = ").append(integrationId);
                        }
                        sql.append(" order by");
                        sql.append("     mic.display_seq");

                    } else {

                        sql.append(" select");
                        sql.append("      item_class_id as product_class_id");
                        sql.append("     ,item_class_name as product_class_name");
                        sql.append("     ,display_seq");
                        //add by ltthuc 2014/06/20
                        sql.append("     ,prepa_class_id");
                        //end add by ltthuc
                        //IVS_LVTu start add 2015/06/26 New request #38256
                        sql.append("     ,shop_category_id");
                        //IVS_LVTu end add 2015/06/26 New request #38256
                        sql.append(" from");
                        sql.append("     mst_item_class");
                        sql.append(" where");
                        sql.append("     delete_date is null");
                        if(!"".equals(integrationId)){
                                sql.append("     and item_integration_id = ").append(integrationId);
                        }
                        sql.append(" order by");
                        sql.append("     display_seq");
                    }

                    break;

                    //�R�[�X
                case 3:
                    sql.append(" select");
                    sql.append("      mcc.course_class_id as product_class_id");
                    sql.append("     ,mcc.course_class_name as product_class_name");
                    sql.append("     ,mcc.display_seq");
                    //IVS_LVTu start add 2015/06/26 New request #38256
                    sql.append("     ,mcc.shop_category_id");
                    //IVS_LVTu end add 2015/06/26 New request #38256
                    sql.append(" from");
                    sql.append("     (");
                    sql.append("         select distinct");
                    sql.append("             mc.course_class_id");
                    sql.append("         from");
                    sql.append("             mst_use_product mup");
                    sql.append("                 inner join mst_course mc");
                    sql.append("                         on mc.course_id = mup.product_id");
                    sql.append("                        and mc.delete_date is null");
                    sql.append("         where");
                    sql.append("                 mup.delete_date is null");
                    sql.append("             and mup.shop_id = " + SQLUtil.convertForSQL(shopID));
                    sql.append("             and mup.product_division = 3");
                    sql.append("     ) mc");
                    sql.append("     inner join mst_course_class mcc");
                    sql.append("             on mcc.course_class_id = mc.course_class_id");
                    sql.append("            and mcc.delete_date is null");
                    if (!"".equals(integrationId)) {
                        sql.append(" where");
                        sql.append("     mcc.course_integration_id = ").append(integrationId);
                    }
                    sql.append(" order by");
                    sql.append("     mcc.display_seq");

                    break;
                 //�R�[�X

            }

            return sql.toString();
	}
        
    /**
     * �f�[�^��ǂݍ��ށB
     *
     * @param con ConnectionWrapper
     * @return true - �����Afalse - ���s
     * @throws java.sql.SQLException SQLException
     * @author ttmloan
     * @since 2014/12/29
     */
    public boolean loadRankItemDetail(ConnectionWrapper con) throws SQLException {
        this.clear();

        if (productDivision != 2) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getLoadRankItemDetailSQL());

        while (rs.next()) {
            ProductClass pc = new ProductClass();
            pc.setData(rs);
            this.add(pc);
        }

        rs.close();

        return true;
    }
  /**
     * �f�[�^��ǂݍ��ނr�p�k�����擾����B
     *
     * @return �f�[�^��ǂݍ��ނr�p�k��
     * @author ttmloan
     * @since 2014/12/29
     */
    public String getLoadRankItemDetailSQL() {
        String sql = "";
        sql = "select item_class_id as product_class_id,\n"
                        + "item_class_name as product_class_name, display_seq\n"
                        + ",shop_category_id\n"
                        + "from mst_item_class mgc\n"
                        + "inner join mst_rank_item_detail md \n"
                        + "where delete_date is null\n"
                        + "order by display_seq, item_class_name, item_class_id\n";
        return sql;
    }
    
    //�R�[�X
    public boolean loadCourse(ConnectionWrapper con, String listshopID) throws SQLException
    {
        if(listshopID.equals("")) {
            return false;
        }
        this.clear();
        StringBuilder sql = new StringBuilder(1000);
        
        sql.append(" select");
        sql.append("      mcc.course_class_id as product_class_id");
        sql.append("     ,mcc.course_class_name as product_class_name");
        sql.append("     ,mcc.display_seq");
        sql.append("     ,shop_category_id");
        sql.append(" from");
        sql.append("     (");
        sql.append("         select distinct");
        sql.append("             mc.course_class_id");
        sql.append("         from");
        sql.append("             mst_use_product mup");
        sql.append("                 inner join mst_course mc");
        sql.append("                         on mc.course_id = mup.product_id");
        sql.append("                        and mc.delete_date is null");
        sql.append("         where");
        sql.append("                 mup.delete_date is null");
        sql.append("             and mup.shop_id in ( " + SQLUtil.convertForSQL(listshopID) + ")");
        sql.append("             and mup.product_division = 3");
        sql.append("     ) mc");
        sql.append("     inner join mst_course_class mcc");
        sql.append("             on mcc.course_class_id = mc.course_class_id");
        sql.append("            and mcc.delete_date is null");
        sql.append(" order by");
        sql.append("     mcc.display_seq");
        

        ResultSetWrapper rs = con.executeQuery(sql.toString());

        while (rs.next()) {
            ProductClass pc = new ProductClass();
            pc.setData(rs);
            this.add(pc);
        }

        rs.close();

        return true;
    }
}
