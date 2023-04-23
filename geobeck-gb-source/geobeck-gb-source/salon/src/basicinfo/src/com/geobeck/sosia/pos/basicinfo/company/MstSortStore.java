/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.basicinfo.company;


import com.geobeck.sosia.pos.master.MstData;
import com.geobeck.sosia.pos.master.MstStore;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import com.geobeck.sql.*;
import com.geobeck.util.*;
/**
 *
 * @author Vo.Thanh.An
 */
public class MstSortStore extends ArrayList<MstStore>
{
    /**
	 * マスタの名称
	 */
	protected	String		masterName	=	"";
	/**
	 * テーブル名
	 */
	protected	String		tableName	=	"";
	/**
	 * ＩＤの列名
	 */
	protected	String		idColName	=	"";
	/**
	 * 名称の列名
	 */
	protected	String		nameColName	=	"";
	/**
	 * 名称の最大文字数
	 */
	protected	int			nameLength	=	0;
        
        public MstSortStore(String masterName,
							String tableName,
							String idColName,
							String nameColName,
							int nameLength)
        {
                this.setMasterName(masterName);
		this.setTableName(tableName);
		this.setIDColName(idColName);
		this.setNameColName(nameColName);
		this.setNameLength(nameLength);
            
        }
    
      public String getMasterName()
	{
		return masterName;
	}

	/**
	 * マスタの名称
	 * @param masterName マスタの名称
	 */
	public void setMasterName(String masterName)
	{
		this.masterName = masterName;
	}

	/**
	 * テーブル名
	 * @return テーブル名
	 */
	public String getTableName()
	{
		return tableName;
	}

	/**
	 * テーブル名
	 * @param tableName テーブル名
	 */
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	/**
	 * ＩＤの列名
	 * @return ＩＤの列名
	 */
	public String getIDColName()
	{
		return idColName;
	}

	/**
	 * ＩＤの列名
	 * @param idColName ＩＤの列名
	 */
	public void setIDColName(String idColName)
	{
		this.idColName = idColName;
	}

	/**
	 * 名称の列名
	 * @return 名称の列名
	 */
	public String getNameColName()
	{
		return nameColName;
	}

	/**
	 * 名称の列名
	 * @param nameColName 名称の列名
	 */
	public void setNameColName(String nameColName)
	{
		this.nameColName = nameColName;
	}

	/**
	 * 名称の最大文字数
	 * @return 名称の最大文字数
	 */
	public int getNameLength()
	{
		return nameLength;
	}

	/**
	 * 名称の最大文字数
	 * @param nameLength 名称の最大文字数
	 */
	public void setNameLength(int nameLength)
	{
		this.nameLength = nameLength;
	}
	
        
        public void loadData()
	{
		this.clear();
		
		ConnectionWrapper con	=	SystemInfo.getConnection();
		
		try
		{
			ResultSetWrapper rs	=	con.executeQuery(this.getSelectAllSQL());
			
			while(rs.next())
			{
				MstStore	temp	=	new MstStore(
						rs.getInt("shop_id"),
						rs.getString("shop_name"),
						rs.getInt("display_seq"));
				this.add(temp);
			}
			
			rs.close();
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
     private String getSelectAllSQL()
	{
		return	"select " + this.getIDColName() + " as shop_id,\n" +
				this.getNameColName() + " as shop_name,\n" +
				"display_seq\n" +
				"from " + this.getTableName() + "\n" +
				"where delete_date is null\n" +
				"order by display_seq\n";
	}
	
     public boolean regist(MstStore data, Integer dataIndex)
	{
		boolean		result	=	false;
		
		ConnectionWrapper con	=	SystemInfo.getConnection();
		
		try
		{
			con.begin();
			
			if(this.registData(con, data, dataIndex))
			{
				con.commit();
				result = true;
			}
			else
			{
				con.rollback();
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}
    private boolean registData(ConnectionWrapper con,
			MstStore data, Integer dataIndex) throws SQLException
	{
		//存在しないデータの場合
		if(dataIndex < 0)
		{
			//表示順が入力されている場合
			if(0 <= data.getDisplaySeq())
			{
				//入力された表示順以降のデータの表示順を１加算する
				if(con.executeUpdate(this.getSlideSQL(data.getDisplaySeq(), true)) < 0)
				{
					return	false;
				}
			}
			//データをInsertする
			if(con.executeUpdate(this.getInsertDataSQL(data)) != 1)
			{
				return	false;
			}
		}
		//存在するデータの場合
		else
		{
			//表示順が変更されている場合
			if(this.get(dataIndex).getDisplaySeq() != data.getDisplaySeq())
			{
				//元の表示順以降のデータの表示順を１減算する
				if(con.executeUpdate(this.getSlideSQL(this.get(dataIndex).getDisplaySeq(), false)) < 0)
				{
					return	false;
				}
				//表示順が入力されている場合
				if(0 <= data.getDisplaySeq())
				{
					//入力された表示順以降のデータの表示順を１加算する
					if(con.executeUpdate(this.getSlideSQL(data.getDisplaySeq(), true)) < 0)
					{
						return	false;
					}
				}
			}
			//データをUpdateする
			if(con.executeUpdate(this.getUpdateDataSQL(data)) != 1)
			{
				return	false;
			}
		}
		
		return	true;
	}	
     
    private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update " + this.getTableName() + "\n" +
				"set display_seq = display_seq " +
						(isIncrement ? "+" : "-") + " 1\n" +
				"where delete_date is null\n" +
				"and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
	} 
    private String getInsertDataSQL(MstStore	data)
	{
		return	"insert into " + this.getTableName() + "\n" +
				"(" + this.idColName + ", " + this.nameColName + ", \n" + "display_seq, " +
				"insert_date, update_date, delete_date)" +
				"select\n" +
				"coalesce(max(" + this.getIDColName() + "), 0) + 1,\n" +
				SQLUtil.convertForSQL(data.getName()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(data.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from " + this.getTableName() + "\n" +
				"where delete_date is null), 0) then " +
				SQLUtil.convertForSQL(data.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from " + this.getTableName() + "\n" +
				"where delete_date is null), 0) + 1 end,\n" +
				"current_timestamp, current_timestamp, null\n" +
				"from " + this.getTableName() + "\n";
	}
    
    private String getUpdateDataSQL(MstStore	data)
	{
		return	"update " + this.getTableName() + "\n" +
				"set\n" +
				this.getNameColName() + " = " +
				SQLUtil.convertForSQL(data.getName()) + ",\n" +
				"display_seq = case\n" +
				"when " + SQLUtil.convertForSQL(data.getDisplaySeq()) +
				" between 1 and coalesce((select max(display_seq)\n" +
				"from " + this.getTableName() + "\n" +
				"where delete_date is null\n" +
				"and " + this.getIDColName() + " != " +
				SQLUtil.convertForSQL(data.getID()) + "), 0) then " +
				SQLUtil.convertForSQL(data.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from " + this.getTableName() + "\n" +
				"where delete_date is null\n" +
				"and " + this.getIDColName() + " != " +
				SQLUtil.convertForSQL(data.getID()) + "), 0) + 1 end,\n" +
				"update_date = current_timestamp\n" +
				"where " + this.getIDColName() + " = " +
				SQLUtil.convertForSQL(data.getID()) + "\n";
	}
	
    
}
