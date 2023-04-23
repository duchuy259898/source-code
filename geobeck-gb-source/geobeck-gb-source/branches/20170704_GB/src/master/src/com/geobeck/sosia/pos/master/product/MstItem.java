/*
 * MstItem.java
 *
 * Created on 2006/05/29, 9:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.product;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;


/**
 * ���i�}�X�^�f�[�^
 * @author katagiri
 */
public class MstItem
{
	/**
	 * ���i����
	 */
	protected	MstItemClass	itemClass		=	new MstItemClass();
	/**
	 * ���i�h�c
	 */
	protected	Integer			itemID			=	null;
	/**
	 * ���iNo.
	 */
	protected	String			itemNo			=	"";
	/**
	 * JAN�R�[�h
	 */
	protected	String			janCode			=	"";
	/**
	 * ���i��
	 */
	protected	String			itemName		=	"";
	/**
	 * �P��
	 */
	protected	Long			price			=	null;
	/**
	 * �Ɩ��K���݌�
	 */
	protected	Integer			useProperStock	=	null;
	/**
	 * �X�̓K���݌�
	 */
	protected	Integer			sellProperStock	=	null;
	/**
	 * �\����
	 */
	protected	Integer			displaySeq		=	null;
        /**
         * �g�p�敪
         */
        protected	Integer			itemUseDivision	=	null;
        /**
         *�@�u����
         */
        protected	Integer			placeID			=	null;

        /**
        * @author nahoang START. Add to customize for Mission screen.
        */
       /**
        * �v���y�C�h���p�\�z
        */
       protected Integer prepaidPrice = null;

       /**
        * �v���y�C�h
        */
       protected Integer prepaidID = null;

       /**
        * �v���y�C�h���p�\�z���擾����B
        *
        * @return �v���y�C�h���p�\�z
        */
       public Integer getPrepaidPrice() {
           return prepaidPrice;
       }

       /**
        * �v���y�C�h���p�\�z���Z�b�g����B
        *
        * @param prepaidPrice
        */
       public void setPrepaidPrice(Integer prepaidPrice) {
           this.prepaidPrice = prepaidPrice;
       }

       /**
        * �v���y�C�h���擾����B
        *
        * @return �v���y�C�h
        */
       public Integer getPrepaidID() {
           return prepaidID;
       }

        /**
         * �v���y�C�h���Z�b�g����B
         *
         * @param prepaidID
         */
        public void setPrepaidID(Integer prepaidID) {
            this.prepaidID = prepaidID;
        }

        /**
         * @author nahoang END.
         */
        /**
    
	/**
	 * �R���X�g���N�^
	 */
	public MstItem()
	{
	}

	/**
	 * ���i���ނ��擾����B
	 * @return ���i����
	 */
	public MstItemClass getItemClass()
	{
		return itemClass;
	}

	/**
	 * ���i���ނ��Z�b�g����B
	 * @param itemClass ���i����
	 */
	public void setItemClass(MstItemClass itemClass)
	{
		this.itemClass = itemClass;
	}

	/**
	 * ���i�h�c���擾����B
	 * @return ���i�h�c
	 */
	public Integer getItemID()
	{
		return itemID;
	}

	/**
	 * ���i�h�c���Z�b�g����B
	 * @param itemID ���i�h�c
	 */
	public void setItemID(Integer itemID)
	{
		this.itemID = itemID;
	}

	/**
	 * ���i�R�[�h���擾����B
	 * @return ���i�R�[�h
	 */
	public String getItemNo()
	{
		return itemNo;
	}

	/**
	 * ���i�R�[�h���Z�b�g����B
	 * @param itemNo ���i�R�[�h
	 */
	public void setItemNo(String itemNo)
	{
		this.itemNo = itemNo;
	}

	public String getJANCode()
	{
		return janCode;
	}

	public void setJANCode(String janCode)
	{
		this.janCode = janCode;
	}

	/**
	 * ���i�����擾����B
	 * @return ���i��
	 */
	public String getItemName()
	{
		return itemName;
	}

	/**
	 * ���i�����Z�b�g����B
	 * @param itemName ���i��
	 */
	public void setItemName(String itemName)
	{
		this.itemName = itemName;
	}

	/**
	 * �P�����擾����B
	 * @return �P��
	 */
	public Long getPrice()
	{
		return price;
	}

	/**
	 * �P�����Z�b�g����B
	 * @param price �P��
	 */
	public void setPrice(Long price)
	{
		this.price = price;
	}

	public Integer getUseProperStock()
	{
		return useProperStock;
	}

	public void setUseProperStock(Integer useProperStock)
	{
		this.useProperStock = useProperStock;
	}

	public Integer getSellProperStock()
	{
		return sellProperStock;
	}

	public void setSellProperStock(Integer sellProperStock)
	{
		this.sellProperStock = sellProperStock;
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
	
	public String toString()
	{
		return	this.getItemName();
 	}
 	   
    /**
     * �g�p�敪���擾����B
     * @return �g�p�敪
     */
    public Integer getItemUseDivision() {
        return itemUseDivision;
    }
    
    /**
     * �g�p�敪���Z�b�g����B
     * @param itemUseDivision �g�p�敪
     */
    public void setItemUseDivision(Integer itemUseDivision) {
        this.itemUseDivision = itemUseDivision;
    }
    
    /**
     * �u������擾����B
     * @return �u����
     */
    public Integer getPlaceID() {
        return placeID;
    }
    
    /**
     * �u������Z�b�g����B
     * @param placeID �u����
     */
    public void setPlaceID(Integer placeID) {
        this.placeID = placeID;
    }

	
	/**
	 * ���i�}�X�^�f�[�^����f�[�^���Z�b�g����B
	 * @param mg ���i�}�X�^�f�[�^
	 */
	public void setData(MstItem mi)
	{
		this.setItemClass(new MstItemClass(mi.getItemClass().getItemClassID()));
		this.setItemID(mi.getItemID());
		this.setItemNo(mi.getItemNo());
		this.setJANCode(mi.getJANCode());
		this.setItemName(mi.getItemName());
		this.setPrice(mi.getPrice());
		this.setUseProperStock(mi.getUseProperStock());
		this.setSellProperStock(mi.getSellProperStock());
		this.setDisplaySeq(mi.getDisplaySeq());
                this.setItemUseDivision(mi.getItemUseDivision());
                this.setPlaceID(mi.getPlaceID());
                //nahoang Add.
                this.setPrepaidID(mi.getPrepaidID());
                this.setPrepaidPrice(mi.getPrepaidPrice());
	}
	
	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setItemClass(new MstItemClass(rs.getInt("item_class_id")));
		this.setItemID(rs.getInt("item_id"));
		this.setItemNo(rs.getString("item_no"));
		this.setJANCode(rs.getString("jan_code"));
		this.setItemName(rs.getString("item_name"));
		this.setPrice(rs.getLong("price"));
		this.setUseProperStock(rs.getInt("use_proper_stock"));
		this.setSellProperStock(rs.getInt("sell_proper_stock"));
		this.setDisplaySeq(rs.getInt("display_seq"));
                this.setItemUseDivision(rs.getInt("item_use_division"));
                this.setPlaceID(rs.getInt("place_id"));
                //nahoang Add.
                this.setPrepaidID(rs.getInt("prepa_id"));
                this.setPrepaidPrice(rs.getInt("prepaid_price"));
	}
	
    /**
     * ResultSetWrapper����f�[�^���Z�b�g����
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setDataWithName(ResultSetWrapper rs) throws SQLException {
        MstItemClass mc = new MstItemClass(rs.getInt("item_class_id"));
        mc.setItemClassName(rs.getString("item_class_name"));
        this.setItemClass(mc);
        this.setItemID(rs.getInt("item_id"));
        this.setItemNo(rs.getString("item_no"));
        this.setJANCode(rs.getString("jan_code"));
        this.setItemName(rs.getString("item_name"));
        this.setPrice(rs.getLong("price"));
        this.setUseProperStock(rs.getInt("use_proper_stock"));
        this.setSellProperStock(rs.getInt("sell_proper_stock"));
        this.setDisplaySeq(rs.getInt("display_seq"));
        this.setItemUseDivision(rs.getInt("item_use_division"));
        this.setPlaceID(rs.getInt("place_id"));
    }
	
	/**
	 * ���i�}�X�^�Ƀf�[�^��o�^����B
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
                                if (0 < lastSeq)
                                {
                                    if(con.executeUpdate(this.getSlideSQL(lastSeq, false)) < 0)
                                    {
                                            return	false;
                                    }
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
			
			this.setMaxItemID(con);
		}
		
		return	true;
	}
	
	private void setMaxItemID(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(getMaxItemIDSQL());
		
		if(rs.next())
		{
			this.setItemID(rs.getInt("max_id"));
		}
		
		rs.close();
	}
	
	
	/**
	 * ���i�}�X�^����f�[�^���폜����B�i�_���폜�j
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
	 * ���i�}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getItemID() == null)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 * ���i�}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean loadByItemNo(ConnectionWrapper con) throws SQLException
	{
		if(this.getItemNo() == null || this.getItemNo().equals(""))	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectByItemNoSQL());

		if(rs.next())
		{
			this.setData(rs);
			rs.close();
			return	true;
		}
		
		rs.close();
		return	false;
	}
	
	/**
     * ID�����ƂɁA���i�������[�h����B
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - �擾��������
     */
    public boolean load(ConnectionWrapper con) throws SQLException {
        if(con == null)	return	false;
        
        ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());
        
        if(rs.next()) {
            this.setDataWithName(rs);
            rs.close();
            return	true;
        }
        
        rs.close();
        return	false;
    }
    

    /**
	 * ���i�}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean loadByJANCode(ConnectionWrapper con) throws SQLException
	{
		if(this.getJANCode() == null || this.getJANCode().equals(""))	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectByJANCodeSQL());

		if(rs.next())
		{
			this.setData(rs);
			MstItemClass	mic	=	new MstItemClass();
			mic.setItemClassName(rs.getString("item_class_name"));
			this.setItemClass(mic);
			rs.close();
			return	true;
		}
		
		rs.close();
		return	false;
	}
	
	/**
	 * ���i�}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExistsByItemNo(ConnectionWrapper con) throws SQLException
	{
		if(this.getItemNo() == null || this.getItemNo().equals(""))	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectByItemNoSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
        
        //IVS_LVTu start add 2014/08/05 Mashu_���ɊǗ��t�H�[�}�b�g��荞��
        /**
         * check data exists.
         * @param con ConnectionWrapper
         * @return boolean
         * @throws SQLException 
         */
        public boolean isExistsByItemNo(ConnectionWrapper con, int supplierId) throws SQLException
	{
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectByItemNoSQL(supplierId));

		if(rs.next()) 
                {
                    this.setItemID(rs.getInt("item_id"));
                    this.setPrice(rs.getLong("cost_price"));
                    return	true;
                }
		else	return	false;
	}
        /**
         * get String.
         * @return String.
         */
        private String getSelectByItemNoSQL(int supplierId)
	{
		return	"select mi.item_id, msi.cost_price \n" +
				"from mst_item mi \n" +
                                "join mst_supplier_item msi \n" +
                                "using (item_id) \n" +
				"where	mi.item_no = " + SQLUtil.convertForSQL(this.getItemNo()) + "\n" +
                                "and msi.supplier_id = " + supplierId +" \n" +
                                "and mi.delete_date is null \n" +
                                "and msi.delete_date is null \n";
	}
        //IVS_LVTu start add 2014/08/05 Mashu_���ɊǗ��t�H�[�}�b�g��荞��
	
	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectSQL()
	{
		return	"select mic.item_class_name, mi.*\n" +
				"from mst_item mi\n" +
				"left outer join mst_item_class mic\n" +
				"on mic.item_class_id = mi.item_class_id\n" +
				"where	item_id = " + SQLUtil.convertForSQL(this.getItemID()) + "\n";
	}
	
	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectByItemNoSQL()
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      mic.item_class_name");
            sql.append("     ,mi.*");
            sql.append(" from");
            sql.append("     mst_item mi");
            sql.append("         join mst_item_class mic");
            sql.append("         using (item_class_id)");
            sql.append(" where");
            sql.append("         mi.delete_date is null");
            sql.append("     and mic.delete_date is null");
            sql.append("     and item_no = " + SQLUtil.convertForSQL(this.getItemNo()));

            return sql.toString();
	}
	
	/**
	 * Select�����擾����B
	 * @return Select��
	 */
	private String getSelectByJANCodeSQL()
	{
		return	"select mic.item_class_name, mi.*\n" +
				"from mst_item mi\n" +
				"left outer join mst_item_class mic\n" +
				"on mic.item_class_id = mi.item_class_id\n" +
				"where	jan_code = " + SQLUtil.convertForSQL(this.getJANCode()) + "\n";
	}
	
	/**
	 * �\���������炷�r�p�k�����擾����
	 * @param seq ���̕\����
	 * @param isIncrement true - ���Z�Afalse - ���Z
	 * @return �\���������炷�r�p�k��
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
                StringBuilder sql = new StringBuilder(1000);
                sql.append(" update mst_item");
                sql.append(" set");
                sql.append("     display_seq = display_seq " + (isIncrement ? "+" : "-") + " 1");
                sql.append(" where");
                sql.append("         delete_date is null");
                sql.append("     and item_class_id = " + SQLUtil.convertForSQL(this.getItemClass().getItemClassID()));
                sql.append("     and display_seq >= " + SQLUtil.convertForSQL(seq));
                sql.append("     and display_seq " + (isIncrement ? "+" : "-") + " 1 >= 0");
                
                if (!isIncrement) {
                    sql.append("     and not exists");
                    sql.append("            (");
                    sql.append("                 select 1");
                    sql.append("                 from");
                    sql.append("                     (");
                    sql.append("                         select");
                    sql.append("                             count(*) as cnt");
                    sql.append("                         from");
                    sql.append("                             mst_item");
                    sql.append("                         where");
                    sql.append("                                 item_class_id = " + SQLUtil.convertForSQL(this.getItemClass().getItemClassID()));
                    sql.append("                             and delete_date is null");
                    sql.append("                         group by");
                    sql.append("                             display_seq");
                    sql.append("                     ) t");
                    sql.append("                 where");
                    sql.append("                     cnt > 1");
                    sql.append("            )");
                }
                
                return sql.toString();
	}
	
	/**
	 * Insert�����擾����B
	 * @return Insert��
	 */
	private String	getInsertSQL()
	{
		return "insert into mst_item\n"
                + "(item_class_id, item_id, item_no, jan_code, item_name,\n"
                + "price, use_proper_stock, sell_proper_stock, display_seq,\n"
                + "insert_date, update_date, delete_date, item_use_division, place_id, prepa_id, prepaid_price)\n"
                + "select\n"
                + SQLUtil.convertForSQL(this.getItemClass().getItemClassID()) + ",\n"
                + "(select coalesce(max(item_id), 0) + 1\n"
                + "from mst_item),\n"
                + SQLUtil.convertForSQL(this.getItemNo()) + ",\n"
                + SQLUtil.convertForSQL(this.getJANCode()) + ",\n"
                + SQLUtil.convertForSQL(this.getItemName()) + ",\n"
                + SQLUtil.convertForSQL(this.getPrice()) + ",\n"
                + SQLUtil.convertForSQL(this.getUseProperStock()) + ",\n"
                + SQLUtil.convertForSQL(this.getSellProperStock()) + ",\n"
                + "case\n"
                + "when " + SQLUtil.convertForSQL(this.getDisplaySeq())
                + " between 0 and coalesce((select max(display_seq)\n"
                + "from mst_item\n"
                + "where delete_date is null\n"
                + "and item_class_id = "
                + SQLUtil.convertForSQL(this.getItemClass().getItemClassID()) + "), 0) then "
                + SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n"
                + "else coalesce((select max(display_seq)\n"
                + "from mst_item\n"
                + "where delete_date is null\n"
                + "and item_class_id = "
                + SQLUtil.convertForSQL(this.getItemClass().getItemClassID()) + "), 0) + 1 end,\n"
                + "current_timestamp, current_timestamp, null,\n"
                + SQLUtil.convertForSQL(this.getItemUseDivision()) + ","
                + SQLUtil.convertForSQL(this.getPlaceID()) + ","
                + SQLUtil.convertForSQL(this.getPrepaidID()) + ","              //nahoang Add   
                + SQLUtil.convertForSQL(this.getPrepaidPrice());                //nahoang Add
	}
	
	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateSQL()
	{
		return "update mst_item\n"
                + "set\n"
                + "item_class_id = " + SQLUtil.convertForSQL(this.getItemClass().getItemClassID()) + ",\n"
                + "item_no = " + SQLUtil.convertForSQL(this.getItemNo()) + ",\n"
                + "jan_code = " + SQLUtil.convertForSQL(this.getJANCode()) + ",\n"
                + "item_name = " + SQLUtil.convertForSQL(this.getItemName()) + ",\n"
                + "price = " + SQLUtil.convertForSQL(this.getPrice()) + ",\n"
                + "use_proper_stock = " + SQLUtil.convertForSQL(this.getUseProperStock()) + ",\n"
                + "prepaid_price=" + SQLUtil.convertForSQL(this.getPrepaidPrice()) + ",\n"              //nahoang Add
                + "prepa_id=" + SQLUtil.convertForSQL(this.getPrepaidID()) + ",\n"                      //nahoang Add
                + "sell_proper_stock = " + SQLUtil.convertForSQL(this.getSellProperStock()) + ",\n"
                + "display_seq = case\n"
                + "when " + SQLUtil.convertForSQL(this.getDisplaySeq())
                + " between 0 and coalesce((select max(display_seq)\n"
                + "from mst_item\n"
                + "where delete_date is null\n"
                + "and item_class_id = "
                + SQLUtil.convertForSQL(this.getItemClass().getItemClassID()) + "\n"
                + "and item_id != "
                + SQLUtil.convertForSQL(this.getItemID()) + "), 0) then "
                + SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n"
                + "else coalesce((select max(display_seq)\n"
                + "from mst_item\n"
                + "where delete_date is null\n"
                + "and item_class_id = "
                + SQLUtil.convertForSQL(this.getItemClass().getItemClassID()) + "\n"
                + "and item_id != "
                + SQLUtil.convertForSQL(this.getItemID()) + "), 0) + 1 end,\n"
                + "update_date = current_timestamp,\n"
                + "item_use_division=" + SQLUtil.convertForSQL(this.getItemUseDivision()) + ",\n"
                + "place_id=" + SQLUtil.convertForSQL(this.getPlaceID()) + "\n"
                + "where item_id = " + SQLUtil.convertForSQL(this.getItemID()) + "\n";
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String getDeleteSQL()
	{
		return	"update mst_item\n" +
				"set\n" +
				"delete_date = current_timestamp\n" +
				"where	item_id = " + SQLUtil.convertForSQL(this.getItemID()) + "\n";
	}
	
	
	private static String getMaxItemIDSQL()
	{
		return	"select max(item_id) as max_id\n" +
				"from mst_item\n";
	}
}
