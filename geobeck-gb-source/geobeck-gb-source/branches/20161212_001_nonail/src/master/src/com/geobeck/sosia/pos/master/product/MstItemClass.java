/*
 * MstItemClass.java
 *
 * Created on 2006/05/29, 9:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.product;

import com.geobeck.sosia.pos.master.MstData;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstShopCategory;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * ���i���ރ}�X�^�f�[�^
 * @author katagiri
 */
public class MstItemClass extends ArrayList<MstItem>
{
	/**
	 * ���i���ނh�c
	 */
	protected	Integer		itemClassID		=	null;
	/**
	 * ���i���ޖ�
	 */
	protected	String		itemClassName	=	"";
	/**
	 * ���i���ޗ���
	 */
	protected	String		itemClassContractedName	    =	    "";
	/**
	 * �\����
	 */
	protected	Integer		displaySeq			=	null;
	/**
	 * �������
	 */
	private	MstData		integration      =	null;
	
        
        
         /**
         * 2014/06/04 An added �v���y�C�h���ރt���O
         */
        protected Integer prepaClassId = null;
        
        //IVS_vtnhan start add 20140707 MASHU_���i���ޓo�^
        private String shopClassName = "";
        private Integer shopCategoryId = null;
        private MstData shopCategory = null;
        private Integer useShopCategory = null;
        private MstShop mtShop = new MstShop();
        //IVS_vtnhan end add 20140707 MASHU_���i���ޓo�^

    public MstShop getMtShop() {
        return mtShop;
    }

    public void setMtShop(MstShop mtShop) {
        this.mtShop = mtShop;
    }

        public MstData getShopCategory() {
            return shopCategory;
        }

        public void setShopCategory(MstData shopCategory) {
            this.shopCategory = shopCategory;
        }

   
  
	/**
	 * �R���X�g���N�^
	 */
	public MstItemClass()
	{
		super();
	}
	/**
	 * �R���X�g���N�^
	 * @param itemClassID ���i���ނh�c
	 */
	public MstItemClass(Integer itemClassID)
	{
		this.setItemClassID(itemClassID);
	}
	
	/**
	 * ������ɕϊ�����B�i���i���ޖ��j
	 * @return ���i���ޖ�
	 */
	public String toString()
	{
		return	this.getItemClassName();
	}

	/**
	 * ���i���ނh�c���擾����B
	 * @return ���i���ނh�c
	 */
	public Integer getItemClassID()
	{
		return itemClassID;
	}

	/**
	 * ���i���ނh�c���Z�b�g����B
	 * @param itemClassID ���i���ނh�c
	 */
	public void setItemClassID(Integer itemClassID)
	{
		this.itemClassID = itemClassID;
	}

	/**
	 * ���i���ޖ����擾����B
	 * @return ���i���ޖ�
	 */
	public String getItemClassName()
	{
		return itemClassName;
	}

	/**
	 * ���i���ޖ����Z�b�g����B
	 * @param itemClassName ���i���ޖ�
	 */
	public void setItemClassName(String itemClassName)
	{
		this.itemClassName = itemClassName;
	}
	
	/**
	 * ���i���ޗ��̂��擾����B
	 * @return ���i���ޗ���
	 */
	public String getItemClassContractedName()
	{
		return itemClassContractedName;
	}

	/**
	 * ���i���ޗ��̂��Z�b�g����B
	 * @param itemClassContractedName ���i���ޗ���
	 */
	public void setItemClassContractedName(String itemClassContractedName)
	{
		this.itemClassContractedName = itemClassContractedName;
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

            /**
         * �v���y�C�h���ރt���O���擾����B
         *
         * @return �v���y�C�h���ރt���O
         */
        public Integer getPrepaClassId() {
            return prepaClassId;
        }
        
         //IVS_vtnhan start add 20140707 MASHU_���i���ޓo�^
        
        public String getShopClassName() {
            return shopClassName;
        }

        public void setShopClassName(String shopClassName) {
            this.shopClassName = shopClassName;
        }
        
         public Integer getShopCategoryId() {
            return shopCategoryId;
        }

        public void setShopCategoryId(Integer shopCategoryId) {
            this.shopCategoryId = shopCategoryId;
        }

        //IVS_vtnhan end add 20140707 MASHU_���i���ޓo�^

    /**
     * �v���y�C�h���ރt���O���Z�b�g����B
     *
     * @param prepaClassId �v���y�C�h���ރt���O
     */
    public void setPrepaClassId(Integer prepaClassId) {
        this.prepaClassId = prepaClassId;
    }
        public boolean equals(Object o)
	{
            if (o instanceof MstItemClass) {
                MstItemClass mic = (MstItemClass)o;
                if (mic.getItemClassID() == itemClassID &&
                    mic.getItemClassName().equals(itemClassName) &&
                    mic.getItemClassContractedName().equals( itemClassContractedName ) &&
                    mic.getDisplaySeq() == displaySeq &&
                    mic.getIntegration() == integration)
                {
                    return true;
                }
            }

            return false;
	}
	
	/**
	 * ���i���ރ}�X�^�f�[�^����f�[�^���Z�b�g����B
	 * @param mgc ���i���ރ}�X�^�f�[�^
	 */
	public void setData(MstItemClass mgc)
	{
		this.setItemClassID(mgc.getItemClassID());
                this.setItemClassName(mgc.getItemClassName());
                this.setItemClassContractedName(mgc.getItemClassContractedName());
                this.setDisplaySeq(mgc.getDisplaySeq());
                this.setIntegration(mgc.getIntegration());
                this.setPrepaClassId(mgc.getPrepaClassId()); //2014/06/04 An added
	}
	
	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setItemClassID(rs.getInt("item_class_id"));
                this.setItemClassName(rs.getString("item_class_name"));
                this.setItemClassContractedName(rs.getString("item_class_contracted_name"));
                this.setDisplaySeq(rs.getInt("display_seq")); 
                MstData data = new MstData(rs.getInt("item_integration_id"), rs.getString("item_integration_name"), rs.getInt("display_seq"));
                this.setIntegration(data);
                this.setPrepaClassId(rs.getInt("prepa_class_id")); //2014/06/04 An added
                //IVS_vtnhan start add 20140707 MASHU_���i���ޓo�^
                MstData dataCategory = new MstData(rs.getInt("shop_category_id"), rs.getString("shop_class_name"),rs.getInt("display_seq"));
                this.setShopCategory(dataCategory);
                //IVS_vtnhan end add 20140707 MASHU_���i���ޓo�^
	}
	
	
	/**
	 * ���i���ރ}�X�^�Ƀf�[�^��o�^����B
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
	 * ���i���ރ}�X�^����f�[�^���폜����B�i�_���폜�j
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
		
		if(con.executeUpdate(sql) != 1)
		{
			return	false;
		}
		
		if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), false)) < 0)
		{
			return	false;
		}
		
		return	true;
	}
	
	/**
	 * ���i���ރ}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getItemClassID() == null || this.getItemClassID() < 1)	return	false;
		
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
            sql.append("     ,b.item_integration_name");
            sql.append("     ,b.display_seq");
            sql.append(" from");
            sql.append("     mst_item_class a");
            sql.append("         left join (select * from mst_item_integration where delete_date is null) b");
            sql.append("         using (item_integration_id)");
            sql.append(" where");
            sql.append("     item_class_id = " + SQLUtil.convertForSQL(this.getItemClassID()));

            return sql.toString();
	}
	
	/**
	 * �\���������炷�r�p�k�����擾����
	 * @param seq ���̕\����
	 * @param isIncrement true - ���Z�Afalse - ���Z
	 * @return �\���������炷�r�p�k��
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_item_class\n" +
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
                //IVS_vtnhan start add 20140707 MASHU_���i���ޓo�^
                Integer categoryId;
                if(this.getShopCategory() == null){
                    categoryId = null;
                }else{
                    categoryId = this.getShopCategory().getID();
                }
                //IVS_vtnhan end add 20140707 MASHU_���i���ޓo�^
		 return "insert into mst_item_class\n"
                //IVS_vtnhan start add 20140707 MASHU_���i���ޓo�^
                + "(shop_category_id,item_class_id, item_class_name, item_class_contracted_name, display_seq, item_integration_id,\n"
                 //IVS_vtnhan end add 20140707 MASHU_���i���ޓo�^
                + "insert_date, update_date, delete_date, prepa_class_id )\n" + //2014/06/04 Thien An added prepa_class_id
                "select\n"
                //IVS_vtnhan start add 20140707 MASHU_���i���ޓo�^        
                +  SQLUtil.convertForSQL(categoryId) + ",\n"
                 //IVS_vtnhan end add 20140707 MASHU_���i���ޓo�^
                + "coalesce(max(item_class_id), 0) + 1,\n"
                + SQLUtil.convertForSQL(this.getItemClassName()) + ",\n"
                + SQLUtil.convertForSQL(this.getItemClassContractedName()) + ",\n"
                + "case\n"
                + "when " + SQLUtil.convertForSQL(this.getDisplaySeq())
                + " between 0 and coalesce((select max(display_seq)\n"
               
                + "from mst_item_class\n"
                + "where delete_date is null), 0) then "
                + SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n"
                + "else coalesce((select max(display_seq)\n"
                + "from mst_item_class\n"
                + "where delete_date is null), 0) + 1 end,\n"
                + (this.getIntegration() == null ? "null" : SQLUtil.convertForSQL(this.getIntegration().getID())) + ",\n"
                + "current_timestamp, current_timestamp, null, " + SQLUtil.convertForSQL(this.getPrepaClassId()) + "\n" + //2014/06/04 Thien An added prepa_class_id
                "from mst_item_class\n";
	}
	
	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateSQL()
	{
                //IVS_vtnhan start add 20140707 MASHU_���i���ޓo�^
                Integer categoryId = null;
                boolean isUpdateCategoryId = false;
                if(mtShop.getShopID() == 0){
                    useShopCategory = 1;
                }else{
                    useShopCategory = mtShop.getUseShopCategory();
                }
       
                if(useShopCategory != null){
                    if(!useShopCategory.equals(1)){
                        isUpdateCategoryId = false;
                    }else{
                        isUpdateCategoryId = true;
                         if(this.getShopCategory() == null){
                            categoryId = null;
                        }else{
                            categoryId = this.getShopCategory().getID();
                        }


                    }
                }else{
                    isUpdateCategoryId = false;
                }
             
                if(isUpdateCategoryId){
                    return "update mst_item_class\n"
                    + "set\n"
                    + " shop_category_id = " + SQLUtil.convertForSQL(categoryId) + ",\n" 
                    + "item_class_name = " + SQLUtil.convertForSQL(this.getItemClassName()) + ",\n"
                    + "item_class_contracted_name = " + SQLUtil.convertForSQL(this.getItemClassContractedName()) + ",\n"
                    + "display_seq = case\n"
                    + "when " + SQLUtil.convertForSQL(this.getDisplaySeq())
                    + " between 0 and coalesce((select max(display_seq) \n"
                    + "from mst_item_class\n"
                    + "where delete_date is null\n"
                    + "and item_class_id != "
                    + SQLUtil.convertForSQL(this.getItemClassID()) + "), 0) then "
                    + SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n"
                    + "else coalesce((select max(display_seq)\n"
                    + "from mst_item_class\n"
                    + "where delete_date is null\n"
                    + "and item_class_id != "
                    + SQLUtil.convertForSQL(this.getItemClassID()) + "), 0) + 1 end,\n"
                    + "item_integration_id = " + (this.getIntegration() == null ? "null" : SQLUtil.convertForSQL(this.getIntegration().getID())) + ",\n"
                    + "prepa_class_id = " + SQLUtil.convertForSQL(this.getPrepaClassId()) + ",\n" + //2014/06/04 Thien An added
                    "update_date = current_timestamp\n"
                    + "where	item_class_id = " + SQLUtil.convertForSQL(this.getItemClassID()) + "\n";
                }
                //IVS_vtnhan end add 20140707 MASHU_���i���ޓo�^
                
		return "update mst_item_class\n"
                + "set\n"
                + "item_class_name = " + SQLUtil.convertForSQL(this.getItemClassName()) + ",\n"
                + "item_class_contracted_name = " + SQLUtil.convertForSQL(this.getItemClassContractedName()) + ",\n"
                + "display_seq = case\n"
                + "when " + SQLUtil.convertForSQL(this.getDisplaySeq())
                + " between 0 and coalesce((select max(display_seq) \n"
                + "from mst_item_class\n"
                + "where delete_date is null\n"
                + "and item_class_id != "
                + SQLUtil.convertForSQL(this.getItemClassID()) + "), 0) then "
                + SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n"
                + "else coalesce((select max(display_seq)\n"
                + "from mst_item_class\n"
                + "where delete_date is null\n"
                + "and item_class_id != "
                + SQLUtil.convertForSQL(this.getItemClassID()) + "), 0) + 1 end,\n"
                + "item_integration_id = " + (this.getIntegration() == null ? "null" : SQLUtil.convertForSQL(this.getIntegration().getID())) + ",\n"
                + "prepa_class_id = " + SQLUtil.convertForSQL(this.getPrepaClassId()) + ",\n" + //2014/06/04 Thien An added
                "update_date = current_timestamp\n"
                + "where	item_class_id = " + SQLUtil.convertForSQL(this.getItemClassID()) + "\n";
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_item_class\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	item_class_id = " + SQLUtil.convertForSQL(this.getItemClassID()) + "\n";
	}
	
	/**
	 * ���i�}�X�^�f�[�^��ArrayList�ɓǂݍ��ށB
	 * @param itemClassID ���i���ނh�c
	 */
	public void loadItem(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(
				this.getSelectItemSQL());

		while(rs.next())
		{
			MstItem	mg	=	new	MstItem();
			mg.setData(rs);
			this.add(mg);
		}

		rs.close();
	}
	
	/**
	 * ���i�}�X�^�f�[�^��S�Ď擾����r�p�k�����擾����
	 * @param itemClassID ���i���ނh�c
	 * @return ���i�}�X�^�f�[�^��S�Ď擾����r�p�k��
	 */
	public String getSelectItemSQL()
	{
		return	"select *\n" +
			"from mst_item\n" +
			"where delete_date is null\n" +
			"and item_class_id = " + SQLUtil.convertForSQL(this.getItemClassID()) + "\n" +
			"order by display_seq, item_id\n";
	}
        
       

}
