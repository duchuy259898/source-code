/*
 * MstMailTemplateClass.java
 *
 * Created on 2006/10/31, 18:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.mail;

import com.geobeck.sosia.pos.master.*;
import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;

/**
 *
 * @author katagiri
 */
public class MstMailTemplateClass
{
	/**
	 * テンプレート分類ＩＤの最小値
	 */
	public static int		MAIL_TEMPLATE_CLASS_ID_MIN		=	1;
	/**
	 * テンプレート分類ＩＤの最大値
	 */
	public static int		MAIL_TEMPLATE_CLASS_ID_MAX		=	Integer.MAX_VALUE;
	/**
	 * テンプレート分類名の文字数の最大値
	 */
	public static int		MAIL_TEMPLATE_CLASS_NAME_MAX	=	20;
	/**
	 * 表示順の最小値
	 */
	public static int		DISPLAY_SEQ_MIN					=	0;
	/**
	 * 表示順の最大値
	 */
	public static int		DISPLAY_SEQ_MAX					=	9999;
	
	private	MstGroup	group					=	new MstGroup();
	private	MstShop		shop					=	new MstShop();
	/**
	 * テンプレート分類ＩＤ
	 */
	private Integer		mailTemplateClassID		=	null;
	/**
	 * テンプレート分類名
	 */
	private	String		mailTemplateClassName		=	null;
	/**
	 * 表示順
	 */
	private Integer		displaySeq	=	null;
	
	/**
	 * コンストラクタ
	 */
	public MstMailTemplateClass()
	{
	}

	public MstGroup getGroup()
	{
		return group;
	}

	public void setGroup(MstGroup group)
	{
		this.group = group;
	}
	
	public Integer getGroupID()
	{
		if(group == null)	return	null;
		else	return	group.getGroupID();
	}
	
	public void setGroupID(Integer groupID)
	{
		if(group == null)
		{
			group	=	new MstGroup();
		}
		
		group.setGroupID(groupID);
	}

	public MstShop getShop()
	{
		return shop;
	}

	public void setShop(MstShop shop)
	{
		this.shop = shop;
	}
	
	public Integer getShopID()
	{
		if(shop == null)	return	null;
		else	return	shop.getShopID();
	}
	
	public void setShopID(Integer shopID)
	{
		if(shop == null)
		{
			shop	=	new MstShop();
		}
		
		shop.setShopID(shopID);
	}
	
	/**
	 * 文字列に変換する。（テンプレート分類名）
	 * @return テンプレート分類名
	 */
	public String toString()
	{
		return mailTemplateClassName;
	}

	/**
	 * テンプレート分類ＩＤを取得する。
	 * @return テンプレート分類ＩＤ
	 */
	public Integer getMailTemplateClassID()
	{
		return mailTemplateClassID;
	}

	/**
	 * テンプレート分類ＩＤをセットする。
	 * @param mailTemplateClassID テンプレート分類ＩＤ
	 */
	public void setMailTemplateClassID(Integer mailTemplateClassID)
	{
		this.mailTemplateClassID = mailTemplateClassID;
	}

	/**
	 * テンプレート分類名を取得する。
	 * @return テンプレート分類名
	 */
	public String getMailTemplateClassName()
	{
		return mailTemplateClassName;
	}

	/**
	 * テンプレート分類名をセットする。
	 * @param mailTemplateClassName テンプレート分類名
	 */
	public void setMailTemplateClassName(String mailTemplateClassName)
	{
		if(mailTemplateClassName == null || mailTemplateClassName.length() <= MstMailTemplateClass.MAIL_TEMPLATE_CLASS_NAME_MAX)
		{
			this.mailTemplateClassName	=	mailTemplateClassName;
		}
		else
		{
			this.mailTemplateClassName	=	mailTemplateClassName.substring(0, MstMailTemplateClass.MAIL_TEMPLATE_CLASS_NAME_MAX);
		}
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
		this.displaySeq	=	displaySeq;
	}
	
	/**
	 * データをクリアする。
	 */
	public void clear()
	{
		this.setGroup(new MstGroup());
		this.setShop(new MstShop());
		this.setMailTemplateClassID(null);
		this.setMailTemplateClassName("");
		this.setDisplaySeq(null);
	}
	
	/**
	 * ResultSetWrapperのデータを読み込む。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setGroupID(rs.getInt("group_id"));
		this.setShopID(rs.getInt("shop_id"));
		this.setMailTemplateClassID(rs.getInt("mail_template_class_id"));
		this.setMailTemplateClassName(rs.getString("mail_template_class_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
	}
	
	
	/**
	 * テンプレート分類マスタにデータを登録する。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
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
	 * テンプレート分類マスタからデータを削除する。（論理削除）
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
	 * テンプレート分類マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getMailTemplateClassID() == null || this.getMailTemplateClassID() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 * テンプレート分類マスタ取得用のＳＱＬ文を取得する。
	 * @return テンプレート分類マスタ取得用のＳＱＬ文
	 */
	public static String getSelectAllSQL()
	{
		return	"select		*\n" +
				"from		mst_mail_template_class\n" +
				"where		delete_date is null\n" +
				"order by	display_seq\n";
	}
	
	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_mail_template_class mmtc\n" +
				"where mmtc.delete_date is null\n" +
				"and mmtc.group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and mmtc.shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"and mmtc.mail_template_class_id = " + SQLUtil.convertForSQL(this.getMailTemplateClassID()) + "\n";
	}
	
	/**
	 * 表示順をずらすＳＱＬ文を取得する。
	 * @param seq 表示順
	 * @param isIncrement true - 加算、false - 減算
	 * @return 表示順をずらすＳＱＬ文
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_mail_template_class\n" +
				"set display_seq = display_seq " +
						(isIncrement ? "+" : "-") + " 1\n" +
				"where delete_date is null\n" +
				"and group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_mail_template_class\n" +
				"(group_id, shop_id, mail_template_class_id,\n" +
				"mail_template_class_name, display_seq,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				SQLUtil.convertForSQL(this.getGroupID()) + ",\n" +
				SQLUtil.convertForSQL(this.getShopID()) + ",\n" +
				"(select coalesce(max(mail_template_class_id), 0) + 1\n" +
				"from mst_mail_template_class\n" +
				"where group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n),\n" +
				SQLUtil.convertForSQL(this.getMailTemplateClassName()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from mst_mail_template_class\n" +
				"where delete_date is null\n" +
				"and group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "), 1) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_mail_template_class\n" +
				"where delete_date is null\n" +
				"and group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "), 0) + 1 end,\n" +
				"current_timestamp, current_timestamp, null\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_mail_template_class\n" +
				"set\n" +
				"mail_template_class_name = " + SQLUtil.convertForSQL(this.getMailTemplateClassName()) + ",\n" +
				"display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from mst_mail_template_class\n" +
				"where delete_date is null\n" +
				"and group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "), 1) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_mail_template_class\n" +
				"where delete_date is null\n" +
				"and group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "), 0) + 1 end,\n" +
				"update_date = current_timestamp\n" +
				"where group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"and mail_template_class_id = " + SQLUtil.convertForSQL(this.getMailTemplateClassID()) + "\n";
	}
	
	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_mail_template_class\n" +
				"set\n" +
				"delete_date = current_timestamp\n" +
				"where group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"and mail_template_class_id = " + SQLUtil.convertForSQL(this.getMailTemplateClassID()) + "\n";
	}
}
