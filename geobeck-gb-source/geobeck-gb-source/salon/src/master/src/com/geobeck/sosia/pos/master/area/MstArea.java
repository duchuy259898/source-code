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
    * �h�c
    */
   protected	Integer		id			=	null;
   /**
    * ����
    */
   protected	String		name		=	"";
   /**
    * �\����
    */
   protected	Integer		displaySeq	=	null;
   protected	ArrayList<MstArea>		areas	=	new ArrayList<MstArea>();
   
   /** Creates a new instance of MstData */
    public MstArea()
    {
    }
    
    /**
	 * �G���A�}�X�^
	 * @param id �h�c
	 * @param name ����
	 * @param displaySeq �\����
	 */
	public MstArea(Integer id, String name, Integer displaySeq)
	{
		this.setData(id, name, displaySeq);
	}
        
        /**
	 * �h�c���擾����B
	 * @return �h�c
	 */
	public Integer getID()
	{
		return id;
	}

	/**
	 * �h�c���Z�b�g����B
	 * @param id �h�c
	 */
	public void setID(Integer id)
	{
		this.id = id;
	}

	/**
	 * ���̂��擾����B
	 * @return ����
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * ���̂��Z�b�g����B
	 * @param name ����
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * �\�������擾����B
	 * @return �\����
	 */
	public Integer getDisplaySeq()
	{
		return displaySeq;
	}

	/**
	 * �\�������Z�b�g����B
	 * @param displaySeq �\����
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
