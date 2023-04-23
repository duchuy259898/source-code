/*
 * MstStore.java
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
 * �h�c�A���́A�\������ێ����Ă���N���X
 * @author katagiri
 */
public class MstStore
{
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
	
	/** Creates a new instance of MstStore */
	public MstStore()
	{
	}
	
	/**
	 * �R���X�g���N�^
	 * @param id �h�c
	 * @param name ����
	 * @param displaySeq �\����
	 */
	public MstStore(Integer id, String name, Integer displaySeq)
	{
		this.setData(id, name, displaySeq);
	}
	
	public MstStore(MstStore mt)
	{
		this.setData(mt);
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
		if(obj instanceof MstStore)
		{
                    Integer oid = ((MstStore) obj).getID();
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
	
	public void setData(MstStore mt)
	{
		this.setData(mt.getID(), mt.getName(), mt.getDisplaySeq());
	}
	
	public void setData(Integer id, String name, Integer displaySeq)
	{
		this.setID(id);
		this.setName(name);
		this.setDisplaySeq(displaySeq);
	}
	
	public static ArrayList<MstStore> loadData(ConnectionWrapper con,
			String tableName, String idColName, String nameColName) throws SQLException
	{
		return	MstStore.loadData(con, tableName, idColName, nameColName, "");
	}
	
	public static ArrayList<MstStore> loadData(ConnectionWrapper con,
			String tableName, String idColName, String nameColName,
			String whereCondition) throws SQLException
	{
		ArrayList<MstStore>		result	=	new ArrayList<MstStore>();
		
		ResultSetWrapper rs	=	con.executeQuery(MstStore.getLoadDataSQL(
				tableName, idColName, nameColName, whereCondition));

		while(rs.next())
		{
			MstStore		temp	=	new MstStore(
					rs.getInt("shop_id"),
					rs.getString("shop_name"),
					rs.getInt("display_seq"));
			result.add(temp);
		}

		rs.close();
		
		return	result;
	}
	
	private static String getLoadDataSQL(
			String tableName, String idColName, String nameColName, String whereCondition)
	{
		return	"select " + idColName + " as shop_id,\n" +
				nameColName + " as shop_name,\n" +
				"display_seq\n" +
				"from " + tableName + "\n" +
				"where delete_date is null\n" +
				(whereCondition.equals("") ? "" :
				"and " + whereCondition) +
				"order by display_seq\n";
	}
}
