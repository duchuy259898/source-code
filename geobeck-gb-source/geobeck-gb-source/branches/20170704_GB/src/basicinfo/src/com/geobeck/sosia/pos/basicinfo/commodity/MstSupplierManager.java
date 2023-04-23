/*
 * MstSupplierManager.java
 *
 * Created on 2007/04/02, 17:50
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.basicinfo.commodity;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.master.product.*;
import com.geobeck.sosia.pos.master.commodity.*;

/**
 * 仕入先登録処理クラス
 * @author katagiri
 */
public class MstSupplierManager extends ArrayList<MstSupplier>
{
	/**
	 * コンストラクタ
	 */
	public MstSupplierManager()
	{
		currentSupplier	=	new MstSupplier();
	}
	
	/**
	 * 処理中の仕入先
	 */
	private	MstSupplier		currentSupplier		=	null;

	/**
	 * 処理中の仕入先を取得する。
	 * @return 処理中の仕入先
	 */
	public MstSupplier getCurrentSupplier()
	{
		return currentSupplier;
	}

	/**
	 * 処理中の仕入先を設定する。
	 * @param currentSupplier 処理中の仕入先
	 */
	public void setCurrentSupplier(MstSupplier currentSupplier)
	{
		this.currentSupplier.setData(currentSupplier);
	}
	
	/**
	 * 仕入先データを読み込む。
	 * @return true - 成功、false - 失敗
	 */
	public boolean load()
	{
		boolean		result	=	false;
		
		this.clear();
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			ResultSetWrapper	rs	=	con.executeQuery(this.getLoadSQL());
			
			while(rs.next())
			{
				MstSupplier		temp	=	new MstSupplier();
				
				temp.setData(rs);
				
				this.add(temp);
			}
			
			rs.close();
			
			result	=	true;
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}
	
	/**
	 * 仕入先データを読み込むためのＳＱＬ文を取得する。
	 * @return 仕入先データを読み込むためのＳＱＬ文
	 */
	private String getLoadSQL()
	{
		return	"select *\n" +
				"from mst_supplier\n" +
				"where delete_date is null\n" +
				"order by supplier_no\n";
	}
	
	/**
	 * 仕入先No.の重複チェックを行う。
	 * @return true - OK、false - NG
	 */
	public boolean checkSupplierNo()
	{
		try
		{
			return	currentSupplier.checkSupplierNo(SystemInfo.getConnection());
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			return	false;
		}
	}

	/**
	 * 処理中の仕入先のデータを登録する。
	 * @param con ConnectionWrapper
	 * @return true - 成功、false - 失敗
	 */
	public boolean register(ConnectionWrapper con) throws SQLException
	{
		try
		{
			if(currentSupplier.regist(con))
			{
				return true;
			}
			
			return false;
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw e;
		}
	}

	/**
	 * 処理中の仕入先のデータを登録する。
	 * @return true - 成功、false - 失敗
	 */
	public boolean regist()
	{
		boolean		result	=	false;
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			try
			{
				con.begin();
				
				if (register(con))
				{
					con.commit();
					result	=	true;
				}
				else
				{
					con.rollback();
				}
			}
			catch(SQLException e)
			{
				con.rollback();
				throw e;
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}
	
	/**
	 * 処理中の仕入先のデータを削除する。
	 * @return true - 成功、false - 失敗
	 */
	public boolean delete()
	{
		boolean		result	=	false;
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			try
			{
				con.begin();
				
				if(currentSupplier.delete(con))
				{
					con.commit();
					result	=	true;
				}
				else
				{
					con.rollback();
				}
			}
			catch(SQLException e)
			{
				con.rollback();
				throw e;
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}

	/**
	 * 仕入先Noに対応する仕入先を返す。
	 * @param supplierNo 仕入先No
	 * @return 仕入先Noに対応する仕入先
	 */
	public MstSupplier lookupSupplier(Integer supplierNo)
	{
		for (MstSupplier s : this)
		{
			if (s.getSupplierNo().equals(supplierNo))
			{
				return s;
			}
		}
		
		return null;
	}
}
