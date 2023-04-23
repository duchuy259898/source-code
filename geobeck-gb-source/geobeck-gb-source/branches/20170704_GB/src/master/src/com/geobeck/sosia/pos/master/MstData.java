/*
 * MstData.java
 *
 * Created on 2006/06/05, 14:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * ＩＤ、名称、表示順を保持しているクラス
 * @author katagiri
 */
public class MstData
{
	/**
	 * ＩＤ
	 */
	protected	Integer		id			=	null;
	/**
	 * 名称
	 */
	protected	String		name		=	"";
	/**
	 * 表示順
	 */
	protected	Integer		displaySeq	=	null;
	
         protected	String		response_no	=	null;
        
        protected	Integer		response_class_id	=	null;
        
        protected	String		response_class_name	=	null;
        //IVS_LTThuc start add 20140710 MASHU_按分マスタ登録
        protected       Integer         shopCategoryID          =       null;
        //IVS_LTThuc start end 20140710 MASHU_按分マスタ登録
        	/** Creates a new instance of MstData */
	public MstData()
	{
	}
	
	/**
	 * コンストラクタ
	 * @param id ＩＤ
	 * @param name 名称
	 * @param displaySeq 表示順
	 */
	public MstData(Integer id, String name, Integer displaySeq)
	{
		this.setData(id, name, displaySeq);
	}
	public MstData(Integer id, String name, Integer displaySeq,String response_no,Integer response_class_id,String response_class_name)
	{
		this.setData(id, name, displaySeq,response_no,response_class_id,response_class_name);
	}
        public void setData(Integer id, String name, Integer displaySeq,String response_no,Integer response_class_id,String response_class_name)
	{
		this.setID(id);
		this.setName(name);
		this.setDisplaySeq(displaySeq);
                this.setResponse_no(response_no);
                this.setResponse_class_id(response_class_id);
                this.setResponse_class_name(response_class_name);
                
	}
	public MstData(MstData md)
	{
		this.setData(md);
	}

	/**
	 * ＩＤを取得する。
	 * @return ＩＤ
	 */
	public Integer getID()
	{
		return id;
	}

	/**
	 * ＩＤをセットする。
	 * @param id ＩＤ
	 */
	public void setID(Integer id)
	{
		this.id = id;
	}
    
        
       public Integer getShopCategoryID() {
        return shopCategoryID;
    }

    public void setShopCategoryID(Integer shopCategoryID) {
        this.shopCategoryID = shopCategoryID;
    }
	/**
	 * 名称を取得する。
	 * @return 名称
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * 名称をセットする。
	 * @param name 名称
	 */
	public void setName(String name)
	{
		this.name = name;
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
	 public void setResponse_no(String response_no) {
            this.response_no = response_no;
        }

        public void setResponse_class_id(Integer response_class_id) {
            this.response_class_id = response_class_id;
        }

        public String getResponse_no() {
            return response_no;
        }

        public Integer getResponse_class_id() {
            return response_class_id;
        }

        public String getResponse_class_name() {
            return response_class_name;
        }

        public void setResponse_class_name(String response_class_name) {
            this.response_class_name = response_class_name;
        }
        
    @Override
	public String toString()
	{
		if(this.getName() != null)
		{
			return	this.getName();
		}
		
		return	"";
	}
	
    @Override
	public boolean equals(Object obj)
	{
		if(obj instanceof MstData)
		{
                    Integer oid = ((MstData) obj).getID();
                    if(oid == null){
                       return (this.getID() == null);
                    }
                    if(this.getID() == null){
                        return false;
                    }
                    return this.getID().intValue() == oid.intValue();
		}
		
		return	false;
	}

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
	
	public void setData(MstData md)
	{
		this.setData(md.getID(), md.getName(), md.getDisplaySeq());
	}
	
	public void setData(Integer id, String name, Integer displaySeq)
	{
		this.setID(id);
		this.setName(name);
		this.setDisplaySeq(displaySeq);
	}
	
	public static ArrayList<MstData> loadData(ConnectionWrapper con,
			String tableName, String idColName, String nameColName) throws SQLException
	{
		return	MstData.loadData(con, tableName, idColName, nameColName, "");
	}
	
	public static ArrayList<MstData> loadData(ConnectionWrapper con,
			String tableName, String idColName, String nameColName,
			String whereCondition) throws SQLException
	{
		ArrayList<MstData>		result	=	new ArrayList<MstData>();
		
		ResultSetWrapper rs	=	con.executeQuery(MstData.getLoadDataSQL(
				tableName, idColName, nameColName, whereCondition));

		while(rs.next())
		{
			MstData		temp	=	new MstData(
					rs.getInt("data_id"),
					rs.getString("data_name"),
					rs.getInt("display_seq"));
			result.add(temp);
		}

		rs.close();
		
		return	result;
	}
	
	private static String getLoadDataSQL(
			String tableName, String idColName, String nameColName, String whereCondition)
	{
		return	"select " + idColName + " as data_id,\n" +
				nameColName + " as data_name,\n" +
				"display_seq\n" +
				"from " + tableName + "\n" +
				"where delete_date is null\n" +
				(whereCondition.equals("") ? "" :
				"and " + whereCondition) +
				"order by display_seq\n";
	}
}
