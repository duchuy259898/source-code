/*
 * MstAuthority.java
 *
 * Created on 2006/09/04, 10:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.company;

import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * �}�X�^�o�^�����p�N���X
 * @author katagiri
 */
public class MstAuthority
{
	/**
	 * �����̐�
	 */
	public static final int		AUTHORYTY_MAX	=	300;
	
	/**
	 * �ʏ�ł̌����̐�
	 */
	public static final int		DEFAULT_AUTHORYTY_NUM	=	7;
	
	/**
	 * �����N�[�[�V�����ł̌����̐�
	 */
	public static final int		RELAXATION_AUTHORYTY_NUM	=	10;
	
	/**
	 * �w�A�[�ł̌����̐�
	 */
	public static final int		HAIR_AUTHORYTY_NUM	=	10;
	
	
	/**
	 * �O���[�v
	 */
	private MstGroup		group		=	new MstGroup();
	
	/**
	 * �X��
	 */
	private MstShop			shop		=	new MstShop();
	
	/**
	 * ����
	 */
	private boolean[]		authorytys	=	new boolean[AUTHORYTY_MAX];
	
	/**
	 * �R���X�g���N�^
	 */
	public MstAuthority()
	{
		
	}
	
	/**
	 * ������������
	 */
	private void initAuthorytys()
	{
		for(int i = 0; i < AUTHORYTY_MAX; i ++)
		{
			setAuthoryty(i, false);
		}
	}

	/**
	 * �O���[�v���擾����B
	 * @return �O���[�v
	 */
	public MstGroup getGroup()
	{
		return group;
	}

	/**
	 * �O���[�v���Z�b�g����B
	 * @param group �O���[�v
	 */
	public void setGroup(MstGroup group)
	{
		this.group = group;
	}

	/**
	 * �X�܂��擾����B
	 * @return �X��
	 */
	public MstShop getShop()
	{
		return shop;
	}

	/**
	 * �X�܂��Z�b�g����B
	 * @param shop �X��
	 */
	public void setShop(MstShop shop)
	{
		this.shop = shop;
	}

	/**
	 * �������擾����B
	 * @return ����
	 */
	public boolean[] getAuthorytys()
	{
		return authorytys;
	}

	/**
	 * �������Z�b�g����B
	 * @param authorytys ����
	 */
	public void setAuthorytys(boolean[] authorytys)
	{
		this.authorytys = authorytys;
	}

	/**
	 * �������擾����B
	 * @param index �C���f�b�N�X
	 * @return ����
	 */
	public boolean getAuthoryty(int index)
	{
		if(0 <= index && index < AUTHORYTY_MAX)
			return authorytys[index];
		else
			return	false;
	}

	/**
	 * �������Z�b�g����B
	 * @param index �C���f�b�N�X
	 * @param authoryty ����
	 */
	public void setAuthoryty(int index, boolean authoryty)
	{
		if(0 <= index && index < AUTHORYTY_MAX)
			this.authorytys[index] = authoryty;
	}
	
	/**
	 * 
	 * @param rs 
	 * @throws java.sql.SQLException 
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		MstGroup	mg	=	new MstGroup();
		mg.setGroupID(rs.getInt("group_id"));
		mg.setGroupName(rs.getString("group_name"));
		this.setGroup(mg);
		MstShop		ms	=	new MstShop();
		ms.setShopID(rs.getInt("shop_id"));
		ms.setShopName(rs.getString("shop_name"));
		this.setShop(ms);
		for(Integer i = 0; i < AUTHORYTY_MAX; i ++)
		{
			this.setAuthoryty(i, rs.getBoolean("authority" + i.toString()));
		}
	}
	
	/**
	 * 
	 * @param con 
	 * @throws java.sql.SQLException 
	 * @return 
	 */
	public boolean load(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		while(rs.next())
		{
			this.setData(rs);
		}

		rs.close();
		
		return	true;
	}
	
	
	/**
	 * �����}�X�^�Ƀf�[�^��o�^����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
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
	 * �����}�X�^����f�[�^���폜����B�i�_���폜�j
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
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
	 * �����}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		boolean		result	=	false;
		
		if(this.getGroup().getGroupID() == null || this.getGroup().getGroupID() < 0)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	result	=	true;
		
		rs.close();
		
		return	result;
	}
	
	/**
	 * 
	 * @return 
	 */
	public String getSelectSQL()
	{
		return	"select ma.*,\n" +
				"mg.group_name,\n" +
				"ms.shop_name\n" +
				"from mst_authority ma\n" +
				"left outer join mst_group mg\n" +
				"on mg.group_id = ma.group_id\n" +
				"left outer join mst_shop ms\n" +
				"on ms.shop_id = ma.shop_id\n" +
				"where ma.delete_date is null\n" +
				"and ma.group_id = " + SQLUtil.convertForSQL(this.getGroup().getGroupID()) + "\n" +
				"and ma.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID(), "0") + "\n";
	}
	
	/**
	 * 
	 * @return 
	 */
	public String getInsertSQL()
	{
		String	sql	=	"insert into mst_authority\n" +
						"values(\n" +
						SQLUtil.convertForSQL(this.getGroup().getGroupID()) + ",\n" +
						SQLUtil.convertForSQL(this.getShop().getShopID(), "0") + ",\n";
		
		for(Integer i = 0; i < AUTHORYTY_MAX; i ++)
		{
			sql	+=	SQLUtil.convertForSQL(this.getAuthoryty(i)) + ",\n";
		}
		
		sql	+=	"current_timestamp, current_timestamp, null)\n";
		
		return	sql;
	}
	
	/**
	 * �f�t�H���g�̌�������f�[�^��}������B
	 * @param con 
	 * @param defaultAuthorityGroupID 
	 * @param groupID 
	 * @param shopID 
	 * @throws java.sql.SQLException 
	 * @return 
	 */
	public static boolean insertFromDefaultAuthority(
			ConnectionWrapper con, Integer defaultAuthorityGroupID,
			Integer groupID, Integer shopID) throws SQLException
	{
		if(con.executeUpdate(getInsertFromDefaultAuthoritySQL(
				defaultAuthorityGroupID, groupID, shopID)) == 1)
		{
			return	true;
		}
		else
		{
			return	false;
		}
	}
	
	/**
	 * 
	 * @param defaultAuthorityGroupID 
	 * @param groupID 
	 * @param shopID 
	 * @return 
	 */
	private static String getInsertFromDefaultAuthoritySQL(Integer defaultAuthorityGroupID,
			Integer groupID, Integer shopID)
	{
		String	sql		=	"insert into mst_authority\n" +
							"select " + groupID.toString() + ",\n" +
							shopID.toString() + ",\n";
		
		sql	+=	(shopID == 0 ? "ma.authority0,\n" : "false,\n");
		
		for(Integer i = 1; i < AUTHORYTY_MAX; i ++)
		{
			sql	+=	"ma.authority" + i.toString() + ",\n";
		}
		
		sql	+=	"current_timestamp,\n" +
				"current_timestamp,\n" +
				"null\n" +
				"from mst_authority ma\n" +
				"where ma.group_id = " + defaultAuthorityGroupID.toString() + "\n" +
				"and ma.shop_id = 0\n";
		
		return	sql;
	}
	
	/**
	 * 
	 * @return 
	 */
	public String getUpdateSQL()
	{
		String	sql	=	"update mst_authority\n" +
						"set\n";
		
		for(Integer i = 0; i < AUTHORYTY_MAX; i ++)
		{
			sql	+=	"authority" + i.toString() + " = " + SQLUtil.convertForSQL(this.getAuthoryty(i)) + ",\n";
		}
		
		sql	+=	"update_date = current_timestamp\n" +
				"where group_id = " + SQLUtil.convertForSQL(this.getGroup().getGroupID()) + "\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID(), "0") + "\n";
		
		return	sql;
	}
	
	/**
	 * 
	 * @return 
	 */
	public String getDeleteSQL()
	{
		return	"update mst_authority\n" +
				"set\n" +
				"update_date = current_timestamp,\n" +
				"delete_date = current_timestamp\n" +
				"where group_id = " + SQLUtil.convertForSQL(this.getGroup().getGroupID()) + "\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID(), "0") + "\n";
	}
}
