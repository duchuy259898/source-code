/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.master.area;

import java.sql.*;
import java.util.*;
import com.geobeck.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ivs
 */
public class MstArea {
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
   protected	ArrayList<MstArea>		areas	=	new ArrayList<MstArea>();
   
   /** Creates a new instance of MstData */
    public MstArea()
    {
    }
    
    /**
	 * エリアマスタ
	 * @param id ＩＤ
	 * @param name 名称
	 * @param displaySeq 表示順
	 */
	public MstArea(Integer id, String name, Integer displaySeq)
	{
		this.setData(id, name, displaySeq);
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
		if(obj instanceof MstArea)
		{
                    Integer oid = ((MstArea) obj).getID();
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
    
        public void setData(MstArea md)
	{
		this.setData(md.getID(), md.getName(), md.getDisplaySeq());
	}
	
	public void setData(Integer id, String name, Integer displaySeq)
	{
		this.setID(id);
		this.setName(name);
		this.setDisplaySeq(displaySeq);
	}
        
        
	public static ArrayList<MstArea> loadData(ConnectionWrapper con) throws SQLException
	{
		ArrayList<MstArea>		result	=	new ArrayList<MstArea>();
		ResultSetWrapper rs	=	con.executeQuery(MstArea.getLoadDataSQL());
		while(rs.next())
		{
			MstArea		temp	=	new MstArea(
					rs.getInt("area_id"),
					rs.getString("area_name"),
					rs.getInt("display_seq"));
			result.add(temp);
		}

		rs.close();
		
		return	result;
	}
	
	private static String getLoadDataSQL()
	{
		return	"select  area_id,\n" +
				" area_name,\n" +
				"display_seq\n" +
				"from mst_area" + "\n" +
                                "where delete_date is null" + "\n" +
				"order by display_seq\n";
	}
        
        public void getAreaByID(ConnectionWrapper con)throws SQLException {
                try {
                        ResultSetWrapper rs	=	con.executeQuery(this.getAreaByIDSQL());
                        while(rs.next())
                        {
                                MstArea		tempArea	=	new MstArea(
                                                rs.getInt("area_id"),
                                                rs.getString("area_name"),
                                                rs.getInt("display_seq"));
                                this.setData(tempArea);
                        }
                        rs.close();
                } catch (SQLException ex) {
                        Logger.getLogger(MstArea.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        private String getAreaByIDSQL()
	{
		return	"select  area_id,\n" +
				" area_name,\n" +
				"display_seq\n" +
				"from mst_area" + "\n" +
                                "where delete_date is null" + "\n" +
                                "and area_id = " + this.getID() + "\n" ;
	}
        
        public ArrayList<MstArea> getArea(ConnectionWrapper con) throws SQLException
        {
                ArrayList<MstArea>		result	=	new ArrayList<MstArea>();
                try {
                        ResultSetWrapper rs	=	con.executeQuery(MstArea.getLoadDataSQL());
                        while(rs.next())
                        {
                                MstArea		temp	=	new MstArea(
                                                rs.getInt("area_id"),
                                                rs.getString("area_name"),
                                                rs.getInt("display_seq"));
                                result.add(temp);
                        }
                        rs.close();
                } catch (SQLException ex) {
                        Logger.getLogger(MstArea.class.getName()).log(Level.SEVERE, null, ex);
                }
                return	result;
        }
}
