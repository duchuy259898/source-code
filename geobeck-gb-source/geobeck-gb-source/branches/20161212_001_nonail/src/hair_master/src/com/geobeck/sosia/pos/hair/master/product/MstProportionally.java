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
 * 按分マスタデータ
 * @author kanemoto
 */
public class MstProportionally extends ArrayList<MstProportionally>
{
	/** 按分ＩＤ */
	protected	Integer		proportionallyID			=	null;
	/** 按分名 */
	protected	String		proportionallyName			=	"";	
	/** 按分ポイント */
	protected	Integer		proportionallyPoint			=	null;
	/** 施術時間 */
	protected	Integer		proportionallyTechnicTime	=	null;
	/** 表示順 */
	protected	Integer		displaySeq					=	null;
        
	/** 統合情報 */
	private	MstData		integration      =	null;
        // IVS_LTThuc start add 20140725 MASHU_按分マスタ登録
        //IVS_LVTu start edit 2015/10/01 New request #43038
        private Integer shopID = null;
        //IVS_LVTu end edit 2015/10/01 New request #43038
        // IVS_LTThuc start end 20140725 MASHU_按分マスタ登録
	// IVS_LTThuc start add 20140710 MASHU_按分マスタ登録
      
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
    // IVS_LTThuc start end 20140710 MASHU_按分マスタ登録

	/**
	 * コンストラクタ
	 */
	public MstProportionally()
	{
            //IVS_LVTu start add 2015/10/01 New request #43038
            this.shopID = SystemInfo.getCurrentShop().getShopID();
            //IVS_LVTu end add 2015/10/01 New request #43038
	}
	/**
	 * コンストラクタ
	 * @param proportionallyID 按分ＩＤ
	 */
	public MstProportionally(Integer proportionallyID)
	{
                //IVS_LVTu start add 2015/10/01 New request #43038
                this.shopID = SystemInfo.getCurrentShop().getShopID();
                //IVS_LVTu end add 2015/10/01 New request #43038
		this.setProportionallyID(proportionallyID);
	}
	
	/**
	 * 文字列に変換する。（按分名）
	 * @return 按分名
	 */
	public String toString()
	{
		return	this.getProportionallyName();
	}

	/**
	 * 按分ＩＤを取得する。
	 * @return 按分ＩＤ
	 */
	public Integer getProportionallyID()
	{
		return proportionallyID;
	}

	/**
	 * 按分ＩＤをセットする。
	 * @param technicClassID 按分ＩＤ
	 */
	public void setProportionallyID(Integer proportionallyID)
	{
		this.proportionallyID = proportionallyID;
	}

	/**
	 * 按分名を取得する。
	 * @return 按分名
	 */
	public String getProportionallyName()
	{
		return proportionallyName;
	}

	/**
	 * 按分名をセットする。
	 * @param proportionallyName 按分名
	 */
	public void setProportionallyName(String proportionallyName)
	{
		this.proportionallyName = proportionallyName;
	}
	
	/**
	 * 按分ポイントを取得する。
	 * @return 按分ポイント
	 */
	public Integer getProportionallyPoint()
	{
		return proportionallyPoint;
	}

	/**
	 * 按分ポイントをセットする。
	 * @param proportionallyPoint 按分ポイント
	 */
	public void setProportionallyPoint(Integer proportionallyPoint)
	{
		this.proportionallyPoint = proportionallyPoint;
	}
	
	/**
	 * 按分施術時間を取得する。
	 * @return 按分施術時間
	 */
	public Integer getProportionallyTechnicTime()
	{
		return proportionallyTechnicTime;
	}

	/**
	 * 按分施術時間をセットする。
	 * @param proportionallyTechnicTime 按分施術時間
	 */
	public void setProportionallyTechnicTime(Integer proportionallyTechnicTime)
	{
		this.proportionallyTechnicTime = proportionallyTechnicTime;
	}
	
	/**
	 * 表示順を取得する。
	 * @return 表示順
	 */
	public Integer getDisplaySeq()
	{
		return displaySeq;
	}

	/**
	 * 表示順をセットする。
	 * @param displaySeq 表示順
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
        //IVS_LTThuc start add 20140710 MASHU_按分マスタ登録
        
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
	 * 按分マスタデータからデータをセットする。
	 * @param mpc 按分マスタデータ
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
	 * ResultSetWrapperからデータをセットする
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
	 * 按分マスタにデータを登録する。
	 * @return true - 成功
	 * @param lastSeq 表示順の最大値
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
	 * 按分マスタからデータを削除する。（論理削除）
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
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
	 * 按分マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
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
	 * Select文を取得する。
	 * @return Select文
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
	 * 表示順をずらすＳＱＬ文を取得する。
	 * @param seq 元の表示順
	 * @param isIncrement true - 加算、false - 減算
	 * @return 表示順をずらすＳＱＬ文
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
	 * Insert文を取得する。
	 * @return Insert文
	 */
        private String	getInsertSQL()
	{       
                 //IVS_LTThuc start edit 20140722 MASHU_按分マスタ
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
                                //IVS_LTThuc start add 20140715 MASHU_按分マスタ
				(this.getIntegration() == null ? "null" : SQLUtil.convertForSQL(this.getIntegration().getID())) + ",\n" +
                                (this.getShopCategory() == null ? "null" : SQLUtil.convertForSQL(this.getShopCategory().getID())) + ",\n"  +
                                 //IVS_LTThuc end add 20140715 MASHU_按分マスタ
				"current_timestamp, current_timestamp, null\n" +
				"from mst_proportionally\n";
            
            
	}
	
        private String	getUpdateSQL()
	{       
            //IVS_LTThuc start edit 20140722 MASHU_按分マスタ
            if(shopID==0){
       return	"update mst_proportionally\n" +
				"set\n" +
				"proportionally_name = " + SQLUtil.convertForSQL(this.getProportionallyName()) + ",\n" +
				"proportionally_point = " + SQLUtil.convertForSQL(this.getProportionallyPoint()) + ",\n" +
				"proportionally_technic_time = " + SQLUtil.convertForSQL(this.getProportionallyTechnicTime()) + ",\n" +
                                //IVS_LTThuc start add 20140715 MASHU_按分マスタ
                                "shop_category_id = " + (this.getShopCategory() == null ? "null" : SQLUtil.convertForSQL(this.getShopCategory().getID())) + ",\n" + 
                                //IVS_LTThuc start add 20140715 MASHU_按分マスタ
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
                                //IVS_LTThuc start add 20140715 MASHU_按分マスタ
                                "shop_category_id = " + (this.getShopCategory() == null ? "null" : SQLUtil.convertForSQL(this.getShopCategory().getID())) + ",\n" + 
                                //IVS_LTThuc start add 20140715 MASHU_按分マスタ
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
       //IVS_LTThuc end edit 20140722 MASHU_按分マスタ
	}
		
        

	
	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_proportionally\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	proportionally_id = " + SQLUtil.convertForSQL(this.getProportionallyID()) + "\n";
	}
	
	/**
	 * 按分マスタデータをArrayListに読み込む。
	 * @param technicClassID 技術分類ＩＤ
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
	 * 按分マスタデータを全て取得するＳＱＬ文を取得する
	 * @param proportionallyID 按分ＩＤ
	 * @return 按分マスタデータを全て取得するＳＱＬ文
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
