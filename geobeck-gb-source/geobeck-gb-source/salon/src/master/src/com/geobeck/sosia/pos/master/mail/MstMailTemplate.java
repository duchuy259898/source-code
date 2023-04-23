/*
 * MstMailTemplate.java
 *
 * Created on 2006/10/31, 19:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.mail;

import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;

/**
 *
 * @author katagiri
 */
public class MstMailTemplate
{
	private	MstGroup				group				=	new MstGroup();
	private	MstShop					shop				=	new MstShop();
	private	MstMailTemplateClass	mailTemplateClass	=	new MstMailTemplateClass();
	private	Integer					mailTemplateID		=	null;
	private	String					mailTemplateTitle	=	"";
	private	Integer					displaySeq			=	null;
	private	String					mailTemplateBody	=	"";
	
	/** Creates a new instance of MstMailTemplate */
	public MstMailTemplate()
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

	public MstMailTemplateClass getMailTemplateClass()
	{
		return mailTemplateClass;
	}

	public void setMailTemplateClass(MstMailTemplateClass mailTemplateClass)
	{
		this.mailTemplateClass = mailTemplateClass;
	}

	public Integer getMailTemplateClassID()
	{
		if(mailTemplateClass == null)	return	null;
		else	return mailTemplateClass.getMailTemplateClassID();
	}

	public void setMailTemplateClassID(Integer mailTemplateClassID)
	{
		if(mailTemplateClass == null)
		{
			mailTemplateClass	=	new MstMailTemplateClass();
		}
		
		this.mailTemplateClass.setMailTemplateClassID(mailTemplateClassID);
	}

	public Integer getMailTemplateID()
	{
		return mailTemplateID;
	}

	public void setMailTemplateID(Integer mailTemplateID)
	{
		this.mailTemplateID = mailTemplateID;
	}

	public String getMailTemplateTitle()
	{
		return mailTemplateTitle;
	}

	public void setMailTemplateTitle(String mailTemplateTitle)
	{
		this.mailTemplateTitle = mailTemplateTitle;
	}

	public Integer getDisplaySeq()
	{
		return displaySeq;
	}

	public void setDisplaySeq(Integer displaySeq)
	{
		this.displaySeq = displaySeq;
	}

	public String getMailTemplateBody()
	{
		return mailTemplateBody;
	}

	public void setMailTemplateBody(String mailTemplateBody)
	{
		this.mailTemplateBody = mailTemplateBody;
	}
	
	public String toString()
	{
		return	mailTemplateTitle;
	}
	
	
	/**
	 * データをクリアする。
	 */
	public void clear()
	{
		this.setGroup(new MstGroup());
		this.setShop(new MstShop());
		this.setMailTemplateClass(new MstMailTemplateClass());
		this.setMailTemplateID(null);
		this.setMailTemplateTitle("");
		this.setMailTemplateBody("");
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
		this.setMailTemplateID(rs.getInt("mail_template_id"));
		this.setMailTemplateTitle(rs.getString("mail_template_title"));
		this.setMailTemplateBody(rs.getString("mail_template_body"));
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
		if(this.getMailTemplateClassID() == null || this.getMailTemplateClassID() < 1 ||
				this.getMailTemplateID() == null || this.getMailTemplateID() < 1)	return	false;
		
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
				"from		mst_mail_template\n" +
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
				"from mst_mail_template mmt\n" +
				"inner join mst_mail_template_class mmtc\n" +
				"on mmtc.mail_template_class_id = mmt.mail_template_class_id\n" +
				"and mmtc.delete_date is null\n" +
				"where mmt.delete_date is null\n" +
				"and mmt.group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and mmt.shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"and mmt.mail_template_class_id = " + SQLUtil.convertForSQL(this.getMailTemplateClassID()) + "\n" +
				"and mmt.mail_template_id = " + SQLUtil.convertForSQL(this.getMailTemplateID()) + "\n";
	}
	
	/**
	 * 表示順をずらすＳＱＬ文を取得する。
	 * @param seq 表示順
	 * @param isIncrement true - 加算、false - 減算
	 * @return 表示順をずらすＳＱＬ文
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_mail_template\n" +
				"set display_seq = display_seq " +
						(isIncrement ? "+" : "-") + " 1\n" +
				"where delete_date is null\n" +
				"and group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"and mail_template_class_id = " +
				SQLUtil.convertForSQL(this.getMailTemplateClassID()) + "\n" +
				"and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_mail_template\n" +
				"(group_id, shop_id, mail_template_class_id, mail_template_id,\n" +
				"mail_template_title, mail_template_body, display_seq,\n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				SQLUtil.convertForSQL(this.getGroupID()) + ",\n" +
				SQLUtil.convertForSQL(this.getShopID()) + ",\n" +
				SQLUtil.convertForSQL(this.getMailTemplateClassID()) + ",\n" +
				"(select coalesce(max(mmt.mail_template_id), 0) + 1\n" +
				"from mst_mail_template mmt\n" +
				"where group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"and mmt.mail_template_class_id = " + SQLUtil.convertForSQL(this.getMailTemplateClassID()) + "),\n" +
				SQLUtil.convertForSQL(this.getMailTemplateTitle()) + ",\n" +
				SQLUtil.convertForSQL(this.getMailTemplateBody()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from mst_mail_template\n" +
				"where delete_date is null\n" +
				"and group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"and mail_template_class_id = " +
				SQLUtil.convertForSQL(this.getMailTemplateClassID()) + "), 1) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_mail_template\n" +
				"where delete_date is null\n" +
				"and group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"and mail_template_class_id = " +
				SQLUtil.convertForSQL(this.getMailTemplateClassID()) + "), 0) + 1 end,\n" +
				"current_timestamp, current_timestamp, null\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_mail_template\n" +
				"set\n" +
				"mail_template_title = " + SQLUtil.convertForSQL(this.getMailTemplateTitle()) + ",\n" +
				"mail_template_body = " + SQLUtil.convertForSQL(this.getMailTemplateBody()) + ",\n" +
				"display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from mst_mail_template\n" +
				"where delete_date is null\n" +
				"and group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"and mail_template_class_id = " +
				SQLUtil.convertForSQL(this.getMailTemplateClassID()) + "\n" +
				"and mail_template_id != " +
				SQLUtil.convertForSQL(this.getMailTemplateID()) + "), 1) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_mail_template\n" +
				"where delete_date is null\n" +
				"and group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"and mail_template_class_id = " +
				SQLUtil.convertForSQL(this.getMailTemplateClassID()) + "\n" +
				"and mail_template_id != " +
				SQLUtil.convertForSQL(this.getMailTemplateID()) + "), 0) + 1 end,\n" +
				"update_date = current_timestamp\n" +
				"where group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"and mail_template_class_id = " + SQLUtil.convertForSQL(this.getMailTemplateClassID()) + "\n" +
				"and mail_template_id = " + SQLUtil.convertForSQL(this.getMailTemplateID()) + "\n";
	}
	
	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_mail_template\n" +
				"set\n" +
				"delete_date = current_timestamp\n" +
				"where group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"and mail_template_class_id = " + SQLUtil.convertForSQL(this.getMailTemplateClassID()) + "\n" +
				"and mail_template_id = " + SQLUtil.convertForSQL(this.getMailTemplateID()) + "\n";
	}
}
