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
 * 商品分類マスタデータ
 * @author katagiri
 */
public class MstItemClass extends ArrayList<MstItem>
{
	/**
	 * 商品分類ＩＤ
	 */
	protected	Integer		itemClassID		=	null;
	/**
	 * 商品分類名
	 */
	protected	String		itemClassName	=	"";
	/**
	 * 商品分類略称
	 */
	protected	String		itemClassContractedName	    =	    "";
	/**
	 * 表示順
	 */
	protected	Integer		displaySeq			=	null;
	/**
	 * 統合情報
	 */
	private	MstData		integration      =	null;
	
        
        
         /**
         * 2014/06/04 An added プリペイド分類フラグ
         */
        protected Integer prepaClassId = null;
        
        //IVS_vtnhan start add 20140707 MASHU_商品分類登録
        private String shopClassName = "";
        private Integer shopCategoryId = null;
        private MstData shopCategory = null;
        private Integer useShopCategory = null;
        private MstShop mtShop = new MstShop();
        //IVS_vtnhan end add 20140707 MASHU_商品分類登録

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
	 * コンストラクタ
	 */
	public MstItemClass()
	{
		super();
	}
	/**
	 * コンストラクタ
	 * @param itemClassID 商品分類ＩＤ
	 */
	public MstItemClass(Integer itemClassID)
	{
		this.setItemClassID(itemClassID);
	}
	
	/**
	 * 文字列に変換する。（商品分類名）
	 * @return 商品分類名
	 */
	public String toString()
	{
		return	this.getItemClassName();
	}

	/**
	 * 商品分類ＩＤを取得する。
	 * @return 商品分類ＩＤ
	 */
	public Integer getItemClassID()
	{
		return itemClassID;
	}

	/**
	 * 商品分類ＩＤをセットする。
	 * @param itemClassID 商品分類ＩＤ
	 */
	public void setItemClassID(Integer itemClassID)
	{
		this.itemClassID = itemClassID;
	}

	/**
	 * 商品分類名を取得する。
	 * @return 商品分類名
	 */
	public String getItemClassName()
	{
		return itemClassName;
	}

	/**
	 * 商品分類名をセットする。
	 * @param itemClassName 商品分類名
	 */
	public void setItemClassName(String itemClassName)
	{
		this.itemClassName = itemClassName;
	}
	
	/**
	 * 商品分類略称を取得する。
	 * @return 商品分類略称
	 */
	public String getItemClassContractedName()
	{
		return itemClassContractedName;
	}

	/**
	 * 商品分類略称をセットする。
	 * @param itemClassContractedName 商品分類略称
	 */
	public void setItemClassContractedName(String itemClassContractedName)
	{
		this.itemClassContractedName = itemClassContractedName;
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

            /**
             * @param integration the integration to set
             */
            public void setIntegration(MstData integration) {
                this.integration = integration;
            }

            /**
         * プリペイド分類フラグを取得する。
         *
         * @return プリペイド分類フラグ
         */
        public Integer getPrepaClassId() {
            return prepaClassId;
        }
        
         //IVS_vtnhan start add 20140707 MASHU_商品分類登録
        
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

        //IVS_vtnhan end add 20140707 MASHU_商品分類登録

    /**
     * プリペイド分類フラグをセットする。
     *
     * @param prepaClassId プリペイド分類フラグ
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
	 * 商品分類マスタデータからデータをセットする。
	 * @param mgc 商品分類マスタデータ
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
	 * ResultSetWrapperからデータをセットする
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
                //IVS_vtnhan start add 20140707 MASHU_商品分類登録
                MstData dataCategory = new MstData(rs.getInt("shop_category_id"), rs.getString("shop_class_name"),rs.getInt("display_seq"));
                this.setShopCategory(dataCategory);
                //IVS_vtnhan end add 20140707 MASHU_商品分類登録
	}
	
	
	/**
	 * 商品分類マスタにデータを登録する。
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
	 * 商品分類マスタからデータを削除する。（論理削除）
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
	 * 商品分類マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
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
	 * Select文を取得する。
	 * @return Select文
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
	 * 表示順をずらすＳＱＬ文を取得する
	 * @param seq 元の表示順
	 * @param isIncrement true - 加算、false - 減算
	 * @return 表示順をずらすＳＱＬ文
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
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String	getInsertSQL()
	{
                //IVS_vtnhan start add 20140707 MASHU_商品分類登録
                Integer categoryId;
                if(this.getShopCategory() == null){
                    categoryId = null;
                }else{
                    categoryId = this.getShopCategory().getID();
                }
                //IVS_vtnhan end add 20140707 MASHU_商品分類登録
		 return "insert into mst_item_class\n"
                //IVS_vtnhan start add 20140707 MASHU_商品分類登録
                + "(shop_category_id,item_class_id, item_class_name, item_class_contracted_name, display_seq, item_integration_id,\n"
                 //IVS_vtnhan end add 20140707 MASHU_商品分類登録
                + "insert_date, update_date, delete_date, prepa_class_id )\n" + //2014/06/04 Thien An added prepa_class_id
                "select\n"
                //IVS_vtnhan start add 20140707 MASHU_商品分類登録        
                +  SQLUtil.convertForSQL(categoryId) + ",\n"
                 //IVS_vtnhan end add 20140707 MASHU_商品分類登録
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
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateSQL()
	{
                //IVS_vtnhan start add 20140707 MASHU_商品分類登録
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
                //IVS_vtnhan end add 20140707 MASHU_商品分類登録
                
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
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_item_class\n"
			+	"set\n"
			+	"delete_date = current_timestamp\n"
			+	"where	item_class_id = " + SQLUtil.convertForSQL(this.getItemClassID()) + "\n";
	}
	
	/**
	 * 商品マスタデータをArrayListに読み込む。
	 * @param itemClassID 商品分類ＩＤ
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
	 * 商品マスタデータを全て取得するＳＱＬ文を取得する
	 * @param itemClassID 商品分類ＩＤ
	 * @return 商品マスタデータを全て取得するＳＱＬ文
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
