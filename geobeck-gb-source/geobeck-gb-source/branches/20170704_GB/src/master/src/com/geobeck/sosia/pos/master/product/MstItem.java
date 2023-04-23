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
 * 商品マスタデータ
 * @author katagiri
 */
public class MstItem
{
	/**
	 * 商品分類
	 */
	protected	MstItemClass	itemClass		=	new MstItemClass();
	/**
	 * 商品ＩＤ
	 */
	protected	Integer			itemID			=	null;
	/**
	 * 商品No.
	 */
	protected	String			itemNo			=	"";
	/**
	 * JANコード
	 */
	protected	String			janCode			=	"";
	/**
	 * 商品名
	 */
	protected	String			itemName		=	"";
	/**
	 * 単価
	 */
	protected	Long			price			=	null;
	/**
	 * 業務適正在庫
	 */
	protected	Integer			useProperStock	=	null;
	/**
	 * 店販適正在庫
	 */
	protected	Integer			sellProperStock	=	null;
	/**
	 * 表示順
	 */
	protected	Integer			displaySeq		=	null;
        /**
         * 使用区分
         */
        protected	Integer			itemUseDivision	=	null;
        /**
         *　置き場
         */
        protected	Integer			placeID			=	null;

        /**
        * @author nahoang START. Add to customize for Mission screen.
        */
       /**
        * プリペイド利用可能額
        */
       protected Integer prepaidPrice = null;

       /**
        * プリペイド
        */
       protected Integer prepaidID = null;

       /**
        * プリペイド利用可能額を取得する。
        *
        * @return プリペイド利用可能額
        */
       public Integer getPrepaidPrice() {
           return prepaidPrice;
       }

       /**
        * プリペイド利用可能額をセットする。
        *
        * @param prepaidPrice
        */
       public void setPrepaidPrice(Integer prepaidPrice) {
           this.prepaidPrice = prepaidPrice;
       }

       /**
        * プリペイドを取得する。
        *
        * @return プリペイド
        */
       public Integer getPrepaidID() {
           return prepaidID;
       }

        /**
         * プリペイドをセットする。
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
	 * コンストラクタ
	 */
	public MstItem()
	{
	}

	/**
	 * 商品分類を取得する。
	 * @return 商品分類
	 */
	public MstItemClass getItemClass()
	{
		return itemClass;
	}

	/**
	 * 商品分類をセットする。
	 * @param itemClass 商品分類
	 */
	public void setItemClass(MstItemClass itemClass)
	{
		this.itemClass = itemClass;
	}

	/**
	 * 商品ＩＤを取得する。
	 * @return 商品ＩＤ
	 */
	public Integer getItemID()
	{
		return itemID;
	}

	/**
	 * 商品ＩＤをセットする。
	 * @param itemID 商品ＩＤ
	 */
	public void setItemID(Integer itemID)
	{
		this.itemID = itemID;
	}

	/**
	 * 商品コードを取得する。
	 * @return 商品コード
	 */
	public String getItemNo()
	{
		return itemNo;
	}

	/**
	 * 商品コードをセットする。
	 * @param itemNo 商品コード
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
	 * 商品名を取得する。
	 * @return 商品名
	 */
	public String getItemName()
	{
		return itemName;
	}

	/**
	 * 商品名をセットする。
	 * @param itemName 商品名
	 */
	public void setItemName(String itemName)
	{
		this.itemName = itemName;
	}

	/**
	 * 単価を取得する。
	 * @return 単価
	 */
	public Long getPrice()
	{
		return price;
	}

	/**
	 * 単価をセットする。
	 * @param price 単価
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
	
	public String toString()
	{
		return	this.getItemName();
 	}
 	   
    /**
     * 使用区分を取得する。
     * @return 使用区分
     */
    public Integer getItemUseDivision() {
        return itemUseDivision;
    }
    
    /**
     * 使用区分をセットする。
     * @param itemUseDivision 使用区分
     */
    public void setItemUseDivision(Integer itemUseDivision) {
        this.itemUseDivision = itemUseDivision;
    }
    
    /**
     * 置き場を取得する。
     * @return 置き場
     */
    public Integer getPlaceID() {
        return placeID;
    }
    
    /**
     * 置き場をセットする。
     * @param placeID 置き場
     */
    public void setPlaceID(Integer placeID) {
        this.placeID = placeID;
    }

	
	/**
	 * 商品マスタデータからデータをセットする。
	 * @param mg 商品マスタデータ
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
	 * ResultSetWrapperからデータをセットする
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
     * ResultSetWrapperからデータをセットする
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
	 * 商品マスタにデータを登録する。
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
	 * 商品マスタからデータを削除する。（論理削除）
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
	 * 商品マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
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
	 * 商品マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
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
     * IDをもとに、商品情報をロードする。
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 取得成功する
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
	 * 商品マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
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
	 * 商品マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
	 */
	public boolean isExistsByItemNo(ConnectionWrapper con) throws SQLException
	{
		if(this.getItemNo() == null || this.getItemNo().equals(""))	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectByItemNoSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
        
        //IVS_LVTu start add 2014/08/05 Mashu_入庫管理フォーマット取り込み
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
        //IVS_LVTu start add 2014/08/05 Mashu_入庫管理フォーマット取り込み
	
	/**
	 * Select文を取得する。
	 * @return Select文
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
	 * Select文を取得する。
	 * @return Select文
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
	 * Select文を取得する。
	 * @return Select文
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
	 * 表示順をずらすＳＱＬ文を取得する
	 * @param seq 元の表示順
	 * @param isIncrement true - 加算、false - 減算
	 * @return 表示順をずらすＳＱＬ文
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
	 * Insert文を取得する。
	 * @return Insert文
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
	 * Update文を取得する。
	 * @return Update文
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
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
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
