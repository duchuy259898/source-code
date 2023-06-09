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
 * 技術分類マスタデータ
 * @author katagiri
 */
public class MstTechnicClass extends ArrayList<MstTechnic>
{
	/**
	 * 技術分類ＩＤ
	 */
	protected	Integer		technicClassID		=	null;
	/**
	 * 技術分類名
	 */
	protected	String		technicClassName	=	"";
	/**
	 * 技術分類名略称
	 */
	protected	String		technicClassContractedName	=	"";
	/**
	 * 表示順
	 */
	protected	Integer		displaySeq			=	null;
	/**
	 * プリペイド
	 */
	protected	Integer		prepaid			=	null;
	/**
	 * 統合情報
	 */
	protected	MstData		integration     =	null;
        
        // IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
        protected	Integer		shopcategoryid	=	null;
        
        protected	String		shopClassName	=	"";
        
        protected	MstData         business        =     null;
        
        private         Integer         useShopCategory =       null;
        
        private         Integer         shopId          =       null;
        
        private         MstShop         mstShop          = new MstShop();
        // IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録
        
	/**
	 * コンストラクタ
	 */
	public MstTechnicClass()
	{
	}
	/**
	 * コンストラクタ
	 * @param technicClassID 技術分類ＩＤ
	 */
	public MstTechnicClass(Integer technicClassID)
	{
		this.setTechnicClassID(technicClassID);
	}
	
	/**
	 * 文字列に変換する。（技術名）
	 * @return 技術名
	 */
	public String toString()
	{
		return	this.getTechnicClassName();
	}

	/**
	 * 技術分類ＩＤを取得する。
	 * @return 技術分類ＩＤ
	 */
	public Integer getTechnicClassID()
	{
		return technicClassID;
	}

	/**
	 * 技術分類ＩＤをセットする。
	 * @param technicClassID 技術分類ＩＤ
	 */
	public void setTechnicClassID(Integer technicClassID)
	{
		this.technicClassID = technicClassID;
	}

	/**
	 * 技術名を取得する。
	 * @return 技術名
	 */
	public String getTechnicClassName()
	{
		return technicClassName;
	}

	/**
	 * 技術名をセットする。
	 * @param technicClassName 技術名
	 */
	public void setTechnicClassName(String technicClassName)
	{
		this.technicClassName = technicClassName;
	}

	/**
	 * 技術分類名略称を取得する。
	 * @return 技術分類名略称
	 */
	public String getTechnicClassContractedName()
	{
		if(technicClassContractedName == null){
			return "";
		}
		return technicClassContractedName;
	}

	/**
	 * 技術分類名略称をセットする。
	 * @param technicClassName 技術分類名略称
	 */
	public void setTechnicClassContractedName(String technicClassContractedName)
	{
		this.technicClassContractedName = technicClassContractedName;
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

        // IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
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
	// IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録
        
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
	 * 技術分類マスタデータからデータをセットする。
	 * @param mtc 技術分類マスタデータ
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
	 * ResultSetWrapperからデータをセットする
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
                // IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
                MstData businessdata = new MstData(rs.getInt("shop_category_id"), rs.getString("shop_class_name"), rs.getInt("display_seq"));
                this.setBusiness(businessdata);
                // IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録
                
	}
	
	/**
	 * 技術分類マスタにデータを登録する。
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
	 * 技術分類マスタからデータを削除する。（論理削除）
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
	 * 技術分類マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
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
	 * Select文を取得する。
	 * @return Select文
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
	 * 表示順をずらすＳＱＬ文を取得する。
	 * @param seq 元の表示順
	 * @param isIncrement true - 加算、false - 減算
	 * @return 表示順をずらすＳＱＬ文
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
	 * Insert文を取得する。
	 * @return Insert文
	 */
        private String	getInsertSQL()
	{       
                // IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
                Integer cateId;
                if(this.getBusiness()== null){
                    cateId = null;
                }else{
                    cateId = this.getBusiness().getID();
                }
                // IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録
		return	"insert into mst_technic_class\n" +
				"(technic_class_id, technic_class_name, technic_class_contracted_name, display_seq, prepaid,"
                               + " technic_integration_id, "
                               // IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
                               + "shop_category_id,\n" +
                               // IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録
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
                                // IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
                                SQLUtil.convertForSQL(cateId) + ",\n"+
                                // IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録
				"current_timestamp, current_timestamp, null\n" +
                                //IVS_LVTu start edit 2016/12/13 New request #58891
                                "from mst_technic_class\n" +
                                "where technic_class_id > 0";
                               //IVS_LVTu end edit 2016/12/13 New request #58891
	}
	
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
        private String	getUpdateSQL()
	{
                // IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
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
                                    // IVS_LeTheHieu Start add 20140703 GB_MASHU_技術分類登録
                                    "shop_category_id = " + SQLUtil.convertForSQL(cateId) + ",\n" +
                                    // IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録
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
                // IVS_LeTheHieu End add 20140703 GB_MASHU_技術分類登録
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
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_technic_class\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	technic_class_id = " + SQLUtil.convertForSQL(this.getTechnicClassID()) + "\n";
	}
	
	/**
	 * 技術マスタデータをArrayListに読み込む。
	 * @param technicClassID 技術分類ＩＤ
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
	 * 技術マスタデータを全て取得するＳＱＬ文を取得する
	 * @param technicClassID 技術分類ＩＤ
	 * @return 技術マスタデータを全て取得するＳＱＬ文
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
        
        //VUINV start add 20140717 MASHU_予約登録画面
        
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
        
        //VUINV end add 20140717 MASHU_予約登録画面
        
}
