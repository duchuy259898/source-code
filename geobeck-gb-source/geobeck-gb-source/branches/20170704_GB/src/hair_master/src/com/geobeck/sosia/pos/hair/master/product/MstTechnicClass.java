/*
 * MstTechnicClass.java
 *
 * Created on 2006/05/24, 11:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.master.product;

import com.geobeck.sosia.pos.master.MstData;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstShopCategory;
import com.geobeck.sosia.pos.master.company.MstShopCategorys;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * �Z�p���ރ}�X�^�f�[�^
 * @author katagiri
 */
public class MstTechnicClass extends ArrayList<MstTechnic>
{
	/**
	 * �Z�p���ނh�c
	 */
	protected	Integer		technicClassID		=	null;
	/**
	 * �Z�p���ޖ�
	 */
	protected	String		technicClassName	=	"";
	/**
	 * �Z�p���ޖ�����
	 */
	protected	String		technicClassContractedName	=	"";
	/**
	 * �\����
	 */
	protected	Integer		displaySeq			=	null;
	/**
	 * �v���y�C�h
	 */
	protected	Integer		prepaid			=	null;
	/**
	 * �������
	 */
	protected	MstData		integration     =	null;
        
        // IVS_LeTheHieu Start add 20140703 GB_MASHU_�Z�p���ޓo�^
        protected	Integer		shopcategoryid	=	null;
        
        protected	String		shopClassName	=	"";
        
        protected	MstData         business        =     null;
        
        private         Integer         useShopCategory =       null;
        
        private         Integer         shopId          =       null;
        
        private         MstShop         mstShop          = new MstShop();
        // IVS_LeTheHieu End add 20140703 GB_MASHU_�Z�p���ޓo�^
        
	/**
	 * �R���X�g���N�^
	 */
	public MstTechnicClass()
	{
	}
	/**
	 * �R���X�g���N�^
	 * @param technicClassID �Z�p���ނh�c
	 */
	public MstTechnicClass(Integer technicClassID)
	{
		this.setTechnicClassID(technicClassID);
	}
	
	/**
	 * ������ɕϊ�����B�i�Z�p���j
	 * @return �Z�p��
	 */
	public String toString()
	{
		return	this.getTechnicClassName();
	}

	/**
	 * �Z�p���ނh�c���擾����B
	 * @return �Z�p���ނh�c
	 */
	public Integer getTechnicClassID()
	{
		return technicClassID;
	}

	/**
	 * �Z�p���ނh�c���Z�b�g����B
	 * @param technicClassID �Z�p���ނh�c
	 */
	public void setTechnicClassID(Integer technicClassID)
	{
		this.technicClassID = technicClassID;
	}

	/**
	 * �Z�p�����擾����B
	 * @return �Z�p��
	 */
	public String getTechnicClassName()
	{
		return technicClassName;
	}

	/**
	 * �Z�p�����Z�b�g����B
	 * @param technicClassName �Z�p��
	 */
	public void setTechnicClassName(String technicClassName)
	{
		this.technicClassName = technicClassName;
	}

	/**
	 * �Z�p���ޖ����̂��擾����B
	 * @return �Z�p���ޖ�����
	 */
	public String getTechnicClassContractedName()
	{
		if(technicClassContractedName == null){
			return "";
		}
		return technicClassContractedName;
	}

	/**
	 * �Z�p���ޖ����̂��Z�b�g����B
	 * @param technicClassName �Z�p���ޖ�����
	 */
	public void setTechnicClassContractedName(String technicClassContractedName)
	{
		this.technicClassContractedName = technicClassContractedName;
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
	 * �\�������Z�b�g����B
	 * @param displaySeq �\����
	 */
	public void setDisplaySeq(Integer displaySeq)
	{
		this.displaySeq = displaySeq;
	}

        /**
         * @return the prepaid
         */
        public Integer getPrepaid() {
            return prepaid;
        }

        /**
         * @param prepaid the prepaid to set
         */
        public void setPrepaid(Integer prepaid) {
            this.prepaid = prepaid;
        }

        public Boolean isPrepaid() {
            return prepaid == 1;
        }

        /**
         * @return the integration
         */
        public MstData getIntegration() {
            return integration;
        }

        /**
         * @param integration the integration to set
         */
        public void setIntegration(MstData integration) {
            this.integration = integration;
        }

        // IVS_LeTheHieu Start add 20140703 GB_MASHU_�Z�p���ޓo�^
        public Integer getShopcategoryid() {
            return shopcategoryid;
        }

        public void setShopcategoryid(Integer shopcategoryid) {
            this.shopcategoryid = shopcategoryid;
        }
        
        public String getShopClassName() {
            return shopClassName;
        }

        public void setShopClassName(String shopClassName) {
            this.shopClassName = shopClassName;
        }
        
        public MstData getBusiness() {
            return business;
        }

        public void setBusiness(MstData business) {
            this.business = business;
        }

        public MstShop getMstShop() {
            return mstShop;
        }

        public void setMstShop(MstShop mstShop) {
            this.mstShop = mstShop;
        }
	// IVS_LeTheHieu End add 20140703 GB_MASHU_�Z�p���ޓo�^
        
	public boolean equals(Object o)
	{
            if (o instanceof MstTechnicClass) {
                MstTechnicClass mtc = (MstTechnicClass)o;
                if (mtc.getTechnicClassID() == technicClassID &&
                    mtc.getTechnicClassName().equals(technicClassName) &&
                    mtc.getTechnicClassContractedName().equals( technicClassContractedName ) &&
                    mtc.getDisplaySeq() == displaySeq &&
                    mtc.getPrepaid() == prepaid &&
                    mtc.getIntegration() == integration)
                {
                    return true;
                }
            }

            return false;
	}
	
	/**
	 * �Z�p���ރ}�X�^�f�[�^����f�[�^���Z�b�g����B
	 * @param mtc �Z�p���ރ}�X�^�f�[�^
	 */
	public void setData(MstTechnicClass mtc)
	{
		this.setTechnicClassID(mtc.getTechnicClassID());
		this.setTechnicClassName(mtc.getTechnicClassName());
		this.setTechnicClassContractedName( mtc.getTechnicClassContractedName() );
		this.setDisplaySeq(mtc.getDisplaySeq());
		this.setPrepaid(mtc.getPrepaid());
                this.setIntegration(mtc.getIntegration());
                
	}
	
	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setTechnicClassID(rs.getInt("technic_class_id"));
		this.setTechnicClassName(rs.getString("technic_class_name"));
		this.setTechnicClassContractedName( rs.getString( "technic_class_contracted_name" ) );
		this.setDisplaySeq(rs.getInt("display_seq"));
		this.setPrepaid(rs.getInt("prepaid"));
                MstData data = new MstData(rs.getInt("technic_integration_id"), rs.getString("technic_integration_name"), rs.getInt("display_seq"));
                this.setIntegration(data);
                // IVS_LeTheHieu Start add 20140703 GB_MASHU_�Z�p���ޓo�^
                MstData businessdata = new MstData(rs.getInt("shop_category_id"), rs.getString("shop_class_name"), rs.getInt("display_seq"));
                this.setBusiness(businessdata);
                // IVS_LeTheHieu End add 20140703 GB_MASHU_�Z�p���ޓo�^
                
	}
	
	/**
	 * �Z�p���ރ}�X�^�Ƀf�[�^��o�^����B
	 * @return true - ����
	 * @param lastSeq �\�����̍ő�l
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean regist(ConnectionWrapper con,
			Integer lastSeq) throws SQLException
	{
		if(isExists(con))
		{
			if(lastSeq != this.getDisplaySeq())
			{
				if(con.executeUpdate(this.getSlideSQL(lastSeq, false)) < 0)
				{
					return	false;
				}
				
				if(0 <= this.getDisplaySeq())
				{
					if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0)
					{
						return	false;
					}
				}
			}
                        
			if(con.executeUpdate(this.getUpdateSQL()) != 1)
			{
				return	false;
			}
		}
		else
		{
			if(0 <= this.getDisplaySeq())
			{
				if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0)
				{
					return	false;
				}
			}
                        
			if(con.executeUpdate(this.getInsertSQL()) != 1)
			{
				return	false;
			}
		}
		
		return	true;
	}
	
	/**
	 * �Z�p���ރ}�X�^����f�[�^���폜����B�i�_���폜�j
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
	 */
	public boolean delete(ConnectionWrapper con) throws SQLException
	{
		String	sql		=	"";
		
		if(isExists(con))
		{
			sql	=	this.getDeleteSQL();
		}
		else
		{
			return	false;
		}
		
		if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), false)) < 0)
		{
			return	false;
		}
		
		if(con.executeUpdate(sql) == 1)
		{
			return	true;
		}
		else
		{
			return	false;
		}
	}
	
	/**
	 * �Z�p���ރ}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getTechnicClassID() == null || this.getTechnicClassID() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectSQL()
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      a.*");
            sql.append("     ,b.technic_integration_name");
            sql.append("     ,b.display_seq");
            sql.append(" from");
            sql.append("     mst_technic_class a");
            sql.append("         left join (select * from mst_technic_integration where delete_date is null) b");
            sql.append("         using (technic_integration_id)");
            sql.append(" where");
            sql.append("     technic_class_id = " + SQLUtil.convertForSQL(this.getTechnicClassID()));

            return sql.toString();
	}
	
	/**
	 * �\���������炷�r�p�k�����擾����B
	 * @param seq ���̕\����
	 * @param isIncrement true - ���Z�Afalse - ���Z
	 * @return �\���������炷�r�p�k��
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_technic_class\n" +
				"set display_seq = display_seq " +
						(isIncrement ? "+" : "-") + " 1\n" +
				"where delete_date is null\n" +
				"and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
	}
	
	/**
	 * Insert�����擾����B
	 * @return Insert��
	 */
        private String	getInsertSQL()
	{       
                // IVS_LeTheHieu Start add 20140703 GB_MASHU_�Z�p���ޓo�^
                Integer cateId;
                if(this.getBusiness()== null){
                    cateId = null;
                }else{
                    cateId = this.getBusiness().getID();
                }
                // IVS_LeTheHieu End add 20140703 GB_MASHU_�Z�p���ޓo�^
		return	"insert into mst_technic_class\n" +
				"(technic_class_id, technic_class_name, technic_class_contracted_name, display_seq, prepaid,"
                               + " technic_integration_id, "
                               // IVS_LeTheHieu Start add 20140703 GB_MASHU_�Z�p���ޓo�^
                               + "shop_category_id,\n" +
                               // IVS_LeTheHieu End add 20140703 GB_MASHU_�Z�p���ޓo�^
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				"coalesce(max(technic_class_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getTechnicClassName()) + ",\n" +
				SQLUtil.convertForSQL(this.getTechnicClassContractedName()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce(max(display_seq), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_technic_class\n" +
				"where delete_date is null), 0) + 1 end,\n" +
				SQLUtil.convertForSQL(this.getPrepaid()) + ",\n" +
                                (this.getIntegration() == null ? "null" : SQLUtil.convertForSQL(this.getIntegration().getID())) + ",\n" +
                                // IVS_LeTheHieu Start add 20140703 GB_MASHU_�Z�p���ޓo�^
                                SQLUtil.convertForSQL(cateId) + ",\n"+
                                // IVS_LeTheHieu End add 20140703 GB_MASHU_�Z�p���ޓo�^
				"current_timestamp, current_timestamp, null\n" +
                                //IVS_LVTu start edit 2016/12/13 New request #58891
                                "from mst_technic_class\n" +
                                "where technic_class_id > 0";
                               //IVS_LVTu end edit 2016/12/13 New request #58891
	}
	
	/**
	 * Update�����擾����B
	 * @return Update��
	 */
        private String	getUpdateSQL()
	{
                // IVS_LeTheHieu Start add 20140703 GB_MASHU_�Z�p���ޓo�^
                Integer cateId = null;
                shopId = mstShop.getShopID();
                useShopCategory = mstShop.getUseShopCategory();
                boolean isUpdate = false;
                // Only update cateId incase shopId=0 or useShopCategory = 1
                if (shopId.equals(0)){
                    isUpdate = true;
                   if(this.getBusiness()== null){
                    cateId = null;
                    }else{
                        cateId = this.getBusiness().getID();
                    }
                } else{
                    if (useShopCategory.equals(1)){
                        isUpdate = true;
                        if(this.getBusiness()== null){
                            cateId = null;
                        }else{
                            cateId = this.getBusiness().getID();
                        }
                    }
                    else{
                        isUpdate = false;
                    } 
                }
                if (isUpdate == true){
                    return	"update mst_technic_class\n" +
                                    "set\n" +
                                    "technic_class_name = " + SQLUtil.convertForSQL(this.getTechnicClassName()) + ",\n" +
                                    "technic_class_contracted_name = " + SQLUtil.convertForSQL(this.getTechnicClassContractedName()) + ",\n" +
                                    // IVS_LeTheHieu Start add 20140703 GB_MASHU_�Z�p���ޓo�^
                                    "shop_category_id = " + SQLUtil.convertForSQL(cateId) + ",\n" +
                                    // IVS_LeTheHieu End add 20140703 GB_MASHU_�Z�p���ޓo�^
                                    "display_seq = case\n" +
                                    "when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
                                    " between 0 and coalesce((select max(display_seq)\n" +
                                    "from mst_technic_class\n" +
                                    "where delete_date is null\n" +
                                    "and technic_class_id != " +
                                    SQLUtil.convertForSQL(this.getTechnicClassID()) + "), 0) then " +
                                    SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
                                    "else coalesce((select max(display_seq)\n" +
                                    "from mst_technic_class\n" +
                                    "where delete_date is null\n" +
                                    "and technic_class_id != " +
                                    SQLUtil.convertForSQL(this.getTechnicClassID()) + "), 0) + 1 end,\n" +
                                    "prepaid = " + SQLUtil.convertForSQL(this.getPrepaid()) + ",\n" +
                                    "technic_integration_id = " + (this.getIntegration() == null ? "null" : SQLUtil.convertForSQL(this.getIntegration().getID())) + ",\n" +
                                    "update_date = current_timestamp\n" +
                                    "where	technic_class_id = " + SQLUtil.convertForSQL(this.getTechnicClassID()) + "\n";
                    }
                // IVS_LeTheHieu End add 20140703 GB_MASHU_�Z�p���ޓo�^
                return	"update mst_technic_class\n" +
                                    "set\n" +
                                    "technic_class_name = " + SQLUtil.convertForSQL(this.getTechnicClassName()) + ",\n" +
                                    "technic_class_contracted_name = " + SQLUtil.convertForSQL(this.getTechnicClassContractedName()) + ",\n" +
                                    "display_seq = case\n" +
                                    "when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
                                    " between 0 and coalesce((select max(display_seq)\n" +
                                    "from mst_technic_class\n" +
                                    "where delete_date is null\n" +
                                    "and technic_class_id != " +
                                    SQLUtil.convertForSQL(this.getTechnicClassID()) + "), 0) then " +
                                    SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
                                    "else coalesce((select max(display_seq)\n" +
                                    "from mst_technic_class\n" +
                                    "where delete_date is null\n" +
                                    "and technic_class_id != " +
                                    SQLUtil.convertForSQL(this.getTechnicClassID()) + "), 0) + 1 end,\n" +
                                    "prepaid = " + SQLUtil.convertForSQL(this.getPrepaid()) + ",\n" +
                                    "technic_integration_id = " + (this.getIntegration() == null ? "null" : SQLUtil.convertForSQL(this.getIntegration().getID())) + ",\n" +
                                    "update_date = current_timestamp\n" +
                                    "where	technic_class_id = " + SQLUtil.convertForSQL(this.getTechnicClassID()) + "\n";
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_technic_class\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	technic_class_id = " + SQLUtil.convertForSQL(this.getTechnicClassID()) + "\n";
	}
	
	/**
	 * �Z�p�}�X�^�f�[�^��ArrayList�ɓǂݍ��ށB
	 * @param technicClassID �Z�p���ނh�c
	 */
	public void loadTechnic(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectTechnicSQL());

		while(rs.next())
		{
			MstTechnic	mt	=	new	MstTechnic();
			mt.setData(rs);
			this.add(mt);
		}

		rs.close();
	}
	
	/**
	 * �Z�p�}�X�^�f�[�^��S�Ď擾����r�p�k�����擾����
	 * @param technicClassID �Z�p���ނh�c
	 * @return �Z�p�}�X�^�f�[�^��S�Ď擾����r�p�k��
	 */
	public String getSelectTechnicSQL()
	{
		return	"select *\n" +
			"from mst_technic\n" +
			"where delete_date is null\n" +
					"and technic_class_id = " +
					SQLUtil.convertForSQL(this.getTechnicClassID()) + "\n" +
			"order by display_seq, technic_id\n";
	}
        
        //VUINV start add 20140717 MASHU_�\��o�^���
        
        public int getShopCategoryIdByTechnicId(ConnectionWrapper con, int technicId) throws SQLException {
            
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" SELECT mtc.shop_category_id ");
            sql.append(" FROM mst_technic mst ");
            sql.append(" INNER JOIN mst_technic_class mtc ON mtc.technic_class_id = mst.technic_class_id ");
            sql.append(" WHERE mst.technic_id = ").append(SQLUtil.convertForSQL(technicId));
            sql.append(" AND mst.delete_date IS NULL ");
            sql.append(" AND mtc.delete_date IS NULL ");
            
            ResultSetWrapper	rs	=	con.executeQuery(sql.toString());
            
            if (rs.next()) {
                return rs.getInt("shop_category_id");
            }
            
            return 0;
        }
        
        //VUINV end add 20140717 MASHU_�\��o�^���
        
}
