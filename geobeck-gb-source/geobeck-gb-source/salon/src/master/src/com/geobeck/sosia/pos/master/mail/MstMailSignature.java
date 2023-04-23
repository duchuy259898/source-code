/*
 * MstMailSignature.java
 *
 * Created on 2006/11/02, 14:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.mail;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;
/**
 *
 * @author katagiri
 */
public class MstMailSignature
{
	private	MstGroup	group					=	new MstGroup();
	private	MstShop		shop					=	new MstShop();
	private	Integer		mailSignaturID			=	null;
	private	String		mailSignaturBody		=	"";
	
	/** Creates a new instance of MstMailSignature */
	public MstMailSignature()
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

	public Integer getMailSignaturID()
	{
		return mailSignaturID;
	}

	public void setMailSignaturID(Integer mailSignaturID)
	{
		this.mailSignaturID = mailSignaturID;
	}

	public String getMailSignaturBody()
	{
		return mailSignaturBody;
	}

	public void setMailSignaturBody(String mailSignaturBody)
	{
		this.mailSignaturBody = mailSignaturBody;
	}
	
	
	/**
	 * データをクリアする。
	 */
	public void clear()
	{
		this.setGroup(new MstGroup());
		this.setShop(new MstShop());
		this.setMailSignaturID(null);
		this.setMailSignaturBody("");
	}
	
	public void load(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());
		
		if(rs.next())
		{
			this.setData(rs);
		}
		
		rs.close();
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
		this.setMailSignaturID(rs.getInt("mail_signature_id"));
		this.setMailSignaturBody(rs.getString("mail_signature_body"));
	}
	
	
	/**
	 * テンプレート分類マスタにデータを登録する。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		String	sql		=	"";
		
		if(isExists(con))
		{
			sql	=	this.getUpdateSQL();
		}
		else
		{
			sql	=	this.getInsertSQL();
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
		if(this.getMailSignaturID() == null || this.getMailSignaturID() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 * 署名マスタ取得用のＳＱＬ文を取得する。
	 * @return 署名マスタ取得用のＳＱＬ文
	 */
	public static String getSelectAllSQL()
	{
		return	"select		*\n" +
				"from		mst_mail_signature\n" +
				"where		delete_date is null\n" +
				"order by	group_id, shop_id, mail_signature_id\n";
	}
	
	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_mail_signature\n" +
				"where	group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and	shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"and	mail_signature_id = " + SQLUtil.convertForSQL(this.getMailSignaturID()) + "\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_mail_signature\n" +
				"(group_id, shop_id, mail_signature_id, mail_signature_body,\n" +
				"insert_date, update_date, delete_date)\n" +
				"values(" + SQLUtil.convertForSQL(this.getGroupID()) + ",\n" +
				SQLUtil.convertForSQL(this.getShopID()) + ",\n" +
				SQLUtil.convertForSQL(this.getMailSignaturID()) + ",\n" +
				SQLUtil.convertForSQL(this.getMailSignaturBody()) + ",\n" +
				"current_timestamp, current_timestamp, null)\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_mail_signature\n" +
				"set\n" +
				"mail_signature_body = " + SQLUtil.convertForSQL(this.getMailSignaturBody()) + ",\n" +
				"update_date = current_timestamp\n" +
				"where	group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and	shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"and	mail_signature_id = " + SQLUtil.convertForSQL(this.getMailSignaturID()) + "\n";
	}
	
	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_mail_signature\n" +
				"set\n" +
				"delete_date = current_timestamp\n" +
				"where	group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and	shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"and	mail_signature_id = " + SQLUtil.convertForSQL(this.getMailSignaturID()) + "\n";
	}
}
