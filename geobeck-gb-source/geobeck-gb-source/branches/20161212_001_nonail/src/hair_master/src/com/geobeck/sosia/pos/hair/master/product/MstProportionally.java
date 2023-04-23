/*
 * MstProportionally.java
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
import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * ���}�X�^�f�[�^
 * @author kanemoto
 */
public class MstProportionally extends ArrayList<MstProportionally>
{
	/** ���h�c */
	protected	Integer		proportionallyID			=	null;
	/** ���� */
	protected	String		proportionallyName			=	"";	
	/** ���|�C���g */
	protected	Integer		proportionallyPoint			=	null;
	/** �{�p���� */
	protected	Integer		proportionallyTechnicTime	=	null;
	/** �\���� */
	protected	Integer		displaySeq					=	null;
        
	/** ������� */
	private	MstData		integration      =	null;
        // IVS_LTThuc start add 20140725 MASHU_���}�X�^�o�^
        //IVS_LVTu start edit 2015/10/01 New request #43038
        private Integer shopID = null;
        //IVS_LVTu end edit 2015/10/01 New request #43038
        // IVS_LTThuc start end 20140725 MASHU_���}�X�^�o�^
	// IVS_LTThuc start add 20140710 MASHU_���}�X�^�o�^
      
        private MstData shopCategory = null;
        private Integer    shoCategoryID  = null;
    //IVS_LVTu start add 2015/10/01 New request #43038
    public MstProportionally(MstShop shop) {
        this.shopID = shop.getShopID();
    }
    //IVS_LVTu end add 2015/10/01 New request #43038

    public Integer getShoCategoryID() {
        return shoCategoryID;
    }

    public void setShoCategoryID(Integer shoCategoryID) {
        this.shoCategoryID = shoCategoryID;
    }

    public MstData getShopCategory() {
        return shopCategory;
    }

    public void setShopCategory(MstData shopCategory) {
        this.shopCategory = shopCategory;
    }
    // IVS_LTThuc start end 20140710 MASHU_���}�X�^�o�^

	/**
	 * �R���X�g���N�^
	 */
	public MstProportionally()
	{
            //IVS_LVTu start add 2015/10/01 New request #43038
            this.shopID = SystemInfo.getCurrentShop().getShopID();
            //IVS_LVTu end add 2015/10/01 New request #43038
	}
	/**
	 * �R���X�g���N�^
	 * @param proportionallyID ���h�c
	 */
	public MstProportionally(Integer proportionallyID)
	{
                //IVS_LVTu start add 2015/10/01 New request #43038
                this.shopID = SystemInfo.getCurrentShop().getShopID();
                //IVS_LVTu end add 2015/10/01 New request #43038
		this.setProportionallyID(proportionallyID);
	}
	
	/**
	 * ������ɕϊ�����B�i�����j
	 * @return ����
	 */
	public String toString()
	{
		return	this.getProportionallyName();
	}

	/**
	 * ���h�c���擾����B
	 * @return ���h�c
	 */
	public Integer getProportionallyID()
	{
		return proportionallyID;
	}

	/**
	 * ���h�c���Z�b�g����B
	 * @param technicClassID ���h�c
	 */
	public void setProportionallyID(Integer proportionallyID)
	{
		this.proportionallyID = proportionallyID;
	}

	/**
	 * �������擾����B
	 * @return ����
	 */
	public String getProportionallyName()
	{
		return proportionallyName;
	}

	/**
	 * �������Z�b�g����B
	 * @param proportionallyName ����
	 */
	public void setProportionallyName(String proportionallyName)
	{
		this.proportionallyName = proportionallyName;
	}
	
	/**
	 * ���|�C���g���擾����B
	 * @return ���|�C���g
	 */
	public Integer getProportionallyPoint()
	{
		return proportionallyPoint;
	}

	/**
	 * ���|�C���g���Z�b�g����B
	 * @param proportionallyPoint ���|�C���g
	 */
	public void setProportionallyPoint(Integer proportionallyPoint)
	{
		this.proportionallyPoint = proportionallyPoint;
	}
	
	/**
	 * ���{�p���Ԃ��擾����B
	 * @return ���{�p����
	 */
	public Integer getProportionallyTechnicTime()
	{
		return proportionallyTechnicTime;
	}

	/**
	 * ���{�p���Ԃ��Z�b�g����B
	 * @param proportionallyTechnicTime ���{�p����
	 */
	public void setProportionallyTechnicTime(Integer proportionallyTechnicTime)
	{
		this.proportionallyTechnicTime = proportionallyTechnicTime;
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
        //IVS_LTThuc start add 20140710 MASHU_���}�X�^�o�^
        
        /**
         * @param integration the integration to set
         */
        public void setIntegration(MstData integration) {
            this.integration = integration;
        }

	public boolean equals(Object o)
	{
		if(o instanceof MstProportionally)
		{
                    MstProportionally mp = (MstProportionally)o;

                    if(mp.getProportionallyID() == proportionallyID &&
                                    mp.getProportionallyName().equals(proportionallyName) &&
                                    mp.getProportionallyPoint() == proportionallyPoint &&
                                    mp.getProportionallyTechnicTime() == proportionallyTechnicTime &&
                                    mp.getDisplaySeq() == displaySeq &&
                                    mp.getIntegration().equals(integration))
                    {
                            return	true;
                    }
		}
		
		return	false;
	}
	
	/**
	 * ���}�X�^�f�[�^����f�[�^���Z�b�g����B
	 * @param mpc ���}�X�^�f�[�^
	 */
	public void setData(MstProportionally mpc)
	{
		this.setProportionallyID(mpc.getProportionallyID());
		this.setProportionallyName(mpc.getProportionallyName());
		this.setProportionallyPoint(mpc.getProportionallyPoint());
		this.setProportionallyTechnicTime( mpc.getProportionallyTechnicTime());
		this.setDisplaySeq(mpc.getDisplaySeq());
                this.setIntegration(mpc.getIntegration());
	}
	
	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setProportionallyID(rs.getInt("proportionally_id"));
		this.setProportionallyName(rs.getString("proportionally_name"));
		this.setProportionallyPoint(rs.getInt("proportionally_point"));
		this.setProportionallyTechnicTime(rs.getInt("proportionally_technic_time"));
		this.setDisplaySeq(rs.getInt("display_seq"));
                this.setShoCategoryID(rs.getInt("shop_category_id"));
               
                MstData data = new MstData(rs.getInt("proportionally_integration_id"), rs.getString("proportionally_integration_name"), rs.getInt("display_seq"));
                this.setIntegration(data);
	}
	
	
	/**
	 * ���}�X�^�Ƀf�[�^��o�^����B
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
	 * ���}�X�^����f�[�^���폜����B�i�_���폜�j
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
	 * ���}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getProportionallyID() == null || this.getProportionallyID() < 1)	return	false;
		
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
            sql.append("     ,b.proportionally_integration_name");
            sql.append("     ,b.display_seq");
            sql.append(" from");
            sql.append("     mst_proportionally a");
            sql.append("         left join (select * from mst_proportionally_integration where delete_date is null) b");
            sql.append("         using (proportionally_integration_id)");
            sql.append(" where");
            sql.append("     proportionally_id = " + SQLUtil.convertForSQL(this.getProportionallyID()));

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
		return	"update mst_proportionally\n" +
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
                 //IVS_LTThuc start edit 20140722 MASHU_���}�X�^
                return	"insert into mst_proportionally\n" +
				"(proportionally_id, proportionally_name, proportionally_point, proportionally_technic_time, display_seq, proportionally_integration_id,shop_category_id,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				"coalesce(max(proportionally_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getProportionallyName()) + ",\n" +
				SQLUtil.convertForSQL(this.getProportionallyPoint()) + ",\n" +
				SQLUtil.convertForSQL(this.getProportionallyTechnicTime()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce(max(display_seq), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_proportionally\n" +
				"where delete_date is null), 0) + 1 end,\n" +
                                //IVS_LTThuc start add 20140715 MASHU_���}�X�^
				(this.getIntegration() == null ? "null" : SQLUtil.convertForSQL(this.getIntegration().getID())) + ",\n" +
                                (this.getShopCategory() == null ? "null" : SQLUtil.convertForSQL(this.getShopCategory().getID())) + ",\n"  +
                                 //IVS_LTThuc end add 20140715 MASHU_���}�X�^
				"current_timestamp, current_timestamp, null\n" +
				"from mst_proportionally\n";
            
            
	}
	
        private String	getUpdateSQL()
	{       
            //IVS_LTThuc start edit 20140722 MASHU_���}�X�^
            if(shopID==0){
       return	"update mst_proportionally\n" +
				"set\n" +
				"proportionally_name = " + SQLUtil.convertForSQL(this.getProportionallyName()) + ",\n" +
				"proportionally_point = " + SQLUtil.convertForSQL(this.getProportionallyPoint()) + ",\n" +
				"proportionally_technic_time = " + SQLUtil.convertForSQL(this.getProportionallyTechnicTime()) + ",\n" +
                                //IVS_LTThuc start add 20140715 MASHU_���}�X�^
                                "shop_category_id = " + (this.getShopCategory() == null ? "null" : SQLUtil.convertForSQL(this.getShopCategory().getID())) + ",\n" + 
                                //IVS_LTThuc start add 20140715 MASHU_���}�X�^
                                "display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce((select max(display_seq)\n" +
				"from mst_proportionally\n" +
				"where delete_date is null\n" +
				"and proportionally_id != " +
				SQLUtil.convertForSQL(this.getProportionallyID()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_proportionally\n" +
				"where delete_date is null\n" +
				"and proportionally_id != " +
				SQLUtil.convertForSQL(this.getProportionallyID()) + "), 0) + 1 end,\n" +
				"proportionally_integration_id = " + (this.getIntegration() == null ? "null" : SQLUtil.convertForSQL(this.getIntegration().getID())) + ",\n" +
				"update_date = current_timestamp\n" +
				"where	proportionally_id = " + SQLUtil.convertForSQL(this.getProportionallyID()) + "\n";
        }else{
            if(SystemInfo.getCurrentShop().getUseShopCategory() == 1){
                return	"update mst_proportionally\n" +
				"set\n" +
				"proportionally_name = " + SQLUtil.convertForSQL(this.getProportionallyName()) + ",\n" +
				"proportionally_point = " + SQLUtil.convertForSQL(this.getProportionallyPoint()) + ",\n" +
				"proportionally_technic_time = " + SQLUtil.convertForSQL(this.getProportionallyTechnicTime()) + ",\n" +
                                //IVS_LTThuc start add 20140715 MASHU_���}�X�^
                                "shop_category_id = " + (this.getShopCategory() == null ? "null" : SQLUtil.convertForSQL(this.getShopCategory().getID())) + ",\n" + 
                                //IVS_LTThuc start add 20140715 MASHU_���}�X�^
                                "display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce((select max(display_seq)\n" +
				"from mst_proportionally\n" +
				"where delete_date is null\n" +
				"and proportionally_id != " +
				SQLUtil.convertForSQL(this.getProportionallyID()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_proportionally\n" +
				"where delete_date is null\n" +
				"and proportionally_id != " +
				SQLUtil.convertForSQL(this.getProportionallyID()) + "), 0) + 1 end,\n" +
				"proportionally_integration_id = " + (this.getIntegration() == null ? "null" : SQLUtil.convertForSQL(this.getIntegration().getID())) + ",\n" +
				"update_date = current_timestamp\n" +
				"where	proportionally_id = " + SQLUtil.convertForSQL(this.getProportionallyID()) + "\n";
            }else{
                return	"update mst_proportionally\n" +
				"set\n" +
				"proportionally_name = " + SQLUtil.convertForSQL(this.getProportionallyName()) + ",\n" +
				"proportionally_point = " + SQLUtil.convertForSQL(this.getProportionallyPoint()) + ",\n" +
				"proportionally_technic_time = " + SQLUtil.convertForSQL(this.getProportionallyTechnicTime()) + ",\n" +
                                "display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce((select max(display_seq)\n" +
				"from mst_proportionally\n" +
				"where delete_date is null\n" +
				"and proportionally_id != " +
				SQLUtil.convertForSQL(this.getProportionallyID()) + "), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_proportionally\n" +
				"where delete_date is null\n" +
				"and proportionally_id != " +
				SQLUtil.convertForSQL(this.getProportionallyID()) + "), 0) + 1 end,\n" +
				"proportionally_integration_id = " + (this.getIntegration() == null ? "null" : SQLUtil.convertForSQL(this.getIntegration().getID())) + ",\n" +
				"update_date = current_timestamp\n" +
				"where	proportionally_id = " + SQLUtil.convertForSQL(this.getProportionallyID()) + "\n";
            }
        }
       //IVS_LTThuc end edit 20140722 MASHU_���}�X�^
	}
		
        

	
	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_proportionally\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	proportionally_id = " + SQLUtil.convertForSQL(this.getProportionallyID()) + "\n";
	}
	
	/**
	 * ���}�X�^�f�[�^��ArrayList�ɓǂݍ��ށB
	 * @param technicClassID �Z�p���ނh�c
	 */
	public void loadProportionally(ConnectionWrapper con) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(
				this.getSelectProportionallySQL());

		while(rs.next())
		{
			MstProportionally	mp	=	new	MstProportionally();
			mp.setData(rs);
			this.add(mp);
		}

		rs.close();
	}
	
	/**
	 * ���}�X�^�f�[�^��S�Ď擾����r�p�k�����擾����
	 * @param proportionallyID ���h�c
	 * @return ���}�X�^�f�[�^��S�Ď擾����r�p�k��
	 */
	public String getSelectProportionallySQL()
	{
		return	"select *\n" +
			"from data_proportionally\n" +
			"where delete_date is null\n" +
					"and proportionally_id = " +
					SQLUtil.convertForSQL(this.getProportionallyID()) + "\n" +
			"order by display_seq, proportionally_id\n";
	}

}
