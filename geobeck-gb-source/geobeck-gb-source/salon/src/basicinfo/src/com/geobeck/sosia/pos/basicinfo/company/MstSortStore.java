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
	 * �}�X�^�̖���
	 */
	protected	String		masterName	=	"";
	/**
	 * �e�[�u����
	 */
	protected	String		tableName	=	"";
	/**
	 * �h�c�̗�
	 */
	protected	String		idColName	=	"";
	/**
	 * ���̗̂�
	 */
	protected	String		nameColName	=	"";
	/**
	 * ���̂̍ő啶����
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
	 * �}�X�^�̖���
	 * @param masterName �}�X�^�̖���
	 */
	public void setMasterName(String masterName)
	{
		this.masterName = masterName;
	}

	/**
	 * �e�[�u����
	 * @return �e�[�u����
	 */
	public String getTableName()
	{
		return tableName;
	}

	/**
	 * �e�[�u����
	 * @param tableName �e�[�u����
	 */
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	/**
	 * �h�c�̗�
	 * @return �h�c�̗�
	 */
	public String getIDColName()
	{
		return idColName;
	}

	/**
	 * �h�c�̗�
	 * @param idColName �h�c�̗�
	 */
	public void setIDColName(String idColName)
	{
		this.idColName = idColName;
	}

	/**
	 * ���̗̂�
	 * @return ���̗̂�
	 */
	public String getNameColName()
	{
		return nameColName;
	}

	/**
	 * ���̗̂�
	 * @param nameColName ���̗̂�
	 */
	public void setNameColName(String nameColName)
	{
		this.nameColName = nameColName;
	}

	/**
	 * ���̂̍ő啶����
	 * @return ���̂̍ő啶����
	 */
	public int getNameLength()
	{
		return nameLength;
	}

	/**
	 * ���̂̍ő啶����
	 * @param nameLength ���̂̍ő啶����
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
		//���݂��Ȃ��f�[�^�̏ꍇ
		if(dataIndex < 0)
		{
			//�\���������͂���Ă���ꍇ
			if(0 <= data.getDisplaySeq())
			{
				//���͂��ꂽ�\�����ȍ~�̃f�[�^�̕\�������P���Z����
				if(con.executeUpdate(this.getSlideSQL(data.getDisplaySeq(), true)) < 0)
				{
					return	false;
				}
			}
			//�f�[�^��Insert����
			if(con.executeUpdate(this.getInsertDataSQL(data)) != 1)
			{
				return	false;
			}
		}
		//���݂���f�[�^�̏ꍇ
		else
		{
			//�\�������ύX����Ă���ꍇ
			if(this.get(dataIndex).getDisplaySeq() != data.getDisplaySeq())
			{
				//���̕\�����ȍ~�̃f�[�^�̕\�������P���Z����
				if(con.executeUpdate(this.getSlideSQL(this.get(dataIndex).getDisplaySeq(), false)) < 0)
				{
					return	false;
				}
				//�\���������͂���Ă���ꍇ
				if(0 <= data.getDisplaySeq())
				{
					//���͂��ꂽ�\�����ȍ~�̃f�[�^�̕\�������P���Z����
					if(con.executeUpdate(this.getSlideSQL(data.getDisplaySeq(), true)) < 0)
					{
						return	false;
					}
				}
			}
			//�f�[�^��Update����
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
