/*
 * MstGroup.java
 *
 * Created on 2006/08/02, 14:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.company;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.tree.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * �O���[�v�}�X�^�N���X
 * @author katagiri
 */
public class MstGroup
{
	/**
	 * �X�֔ԍ��̕������̍ő�l
	 */
	public static int		POSTAL_CODE_MAX			=	7;
	/**
	 * �d�b�ԍ��̕������̍ő�l
	 */
	public static int		PHONE_NUMBER_MAX		=	15;
	/**
	 * �e�`�w�ԍ��̕������̍ő�l
	 */
	public static int		FAX_NUMBER_MAX			=	15;
	/**
	 * ���[���A�h���X�̕������̍ő�l
	 */
	public static int		MAIL_ADDRESS_MAX		=	64;

	/**
	 * �O���[�vID
	 */
	protected	Integer					groupID		=	null;
	/**
	 * �O���[�v��
	 */
	protected	String					groupName	=	"";
	/**
	 * �e�O���[�v
	 */
	protected	MstGroup				parent		=	null;
	/**
	 * �K�w
	 */
	protected	Integer					level		=	0;
	/**
	 * �X�֔ԍ�
	 */
	private	String		postalCode			=	"";
	/**
	 * �Z��
	 */
	private	String[]	address				=	{"", "", "", ""};
	/**
	 * �d�b�ԍ�
	 */
	private	String		phoneNumber			=	"";
	/**
	 * �e�`�w�ԍ�
	 */
	private	String		faxNumber			=	"";
	/**
	 * ���[���A�h���X
	 */
	private	String		mailAddress			=	"";

	/**
	 * �q�O���[�v
	 */
	protected	ArrayList<MstGroup>		groups	=	new ArrayList<MstGroup>();
	/**
	 * �q�X��
	 */
	protected	ArrayList<MstShop>		shops	=	new ArrayList<MstShop>();

	/**
	 * �R���X�g���N�^
	 */
	public MstGroup()
	{
	}

	/**
	 * �O���[�vID���擾����B
	 * @return �O���[�vID
	 */
	public Integer getGroupID()
	{
		return groupID;
	}

	/**
	 * �O���[�vID���Z�b�g����B
	 * @param groupID �O���[�vID
	 */
	public void setGroupID(Integer groupID)
	{
		this.groupID = groupID;
	}

	/**
	 * �O���[�v�����擾����B
	 * @return �O���[�v��
	 */
	public String getGroupName()
	{
		return groupName;
	}

	/**
	 * �O���[�v�����Z�b�g����B
	 * @param groupName �O���[�v��
	 */
	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	/**
	 * �e�O���[�v���擾����B
	 * @return �e�O���[�v
	 */
	public MstGroup getParent()
	{
		return parent;
	}

	/**
	 * �e�O���[�v��ݒ肷��B
	 * @param parent �e�O���[�v
	 */
	public void setParent(MstGroup parent)
	{
		this.parent = parent;
	}

	/**
	 * �e�O���[�v��ID���擾����B
	 * @return �e�O���[�v��ID
	 */
	public Integer getParentID()
	{
		if(this.getParent() == null)
			return	null;
		else
			return	this.getParent().getGroupID();
	}

	/**
	 * �K�w���擾����B
	 * @return �K�w
	 */
	public Integer getLevel()
	{
		return level;
	}

	/**
	 * �K�w���Z�b�g����B
	 * @param level �K�w
	 */
	public void setLevel(Integer level)
	{
		this.level = level;
	}

	/**
	 * �X�֔ԍ����擾����B
	 * @return �X�֔ԍ�
	 */
	public String getPostalCode()
	{
		return postalCode;
	}

	/**
	 * �X�֔ԍ����Z�b�g����B
	 * @param postalCode �X�֔ԍ�
	 */
	public void setPostalCode(String postalCode)
	{
		if(postalCode == null || postalCode.length() <= MstGroup.POSTAL_CODE_MAX)
		{
			this.postalCode	=	postalCode;
		}
		else
		{
			this.postalCode	=	postalCode.substring(0, MstGroup.POSTAL_CODE_MAX);
		}
	}

	/**
	 * �Z�����擾����B
	 * @return �Z��
	 */
	public String[] getAddress()
	{
		return address;
	}

	/**
	 * �Z�����擾����B
	 * @param index �C���f�b�N�X
	 * @return �Z��
	 */
	public String getAddress(int index)
	{
		return address[index];
	}

	/**
	 * �Z��
	 * @param address �Z��
	 */
	public void setAddress(String[] address)
	{
		this.address = address;
	}

	/**
	 * �Z�����Z�b�g����B
	 * @param index �C���f�b�N�X
	 * @param address �Z��
	 */
	public void setAddress(int index, String address)
	{
		this.address[index] = address;
	}

	/**
	 * �Z�����擾����B
	 * @return �Z��
	 */
	public String getFullAddress()
	{
		return	(address[0] == null ? "" : address[0]) +
				(address[1] == null ? "" : address[1]) +
				(address[2] == null ? "" : address[2]) +
				(address[3] == null ? "" : address[3]);
	}

	/**
	 * �d�b�ԍ����擾����B
	 * @return �d�b�ԍ�
	 */
	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	/**
	 * �d�b�ԍ����Z�b�g����B
	 * @param phoneNumber �d�b�ԍ�
	 */
	public void setPhoneNumber(String phoneNumber)
	{
		if(phoneNumber == null || phoneNumber.length() <= MstGroup.PHONE_NUMBER_MAX)
		{
			this.phoneNumber	=	phoneNumber;
		}
		else
		{
			this.phoneNumber	=	phoneNumber.substring(0, MstGroup.PHONE_NUMBER_MAX);
		}
	}

	/**
	 * �e�`�w�ԍ����擾����B
	 * @return �e�`�w�ԍ�
	 */
	public String getFaxNumber()
	{
		return faxNumber;
	}

	/**
	 * �e�`�w�ԍ����Z�b�g����B
	 * @param faxNumber �e�`�w�ԍ�
	 */
	public void setFaxNumber(String faxNumber)
	{
		if(faxNumber == null || faxNumber.length() <= MstGroup.FAX_NUMBER_MAX)
		{
			this.faxNumber	=	faxNumber;
		}
		else
		{
			this.faxNumber	=	faxNumber.substring(0, MstGroup.FAX_NUMBER_MAX);
		}
	}

	/**
	 * ���[���A�h���X���擾����B
	 * @return ���[���A�h���X
	 */
	public String getMailAddress()
	{
		return mailAddress;
	}

	/**
	 * ���[���A�h���X���Z�b�g����B
	 * @param mailAddress ���[���A�h���X
	 */
	public void setMailAddress(String mailAddress)
	{
		if(mailAddress == null || mailAddress.length() <= MstGroup.MAIL_ADDRESS_MAX)
		{
			this.mailAddress	=	mailAddress;
		}
		else
		{
			this.mailAddress	=	mailAddress.substring(0, MstGroup.MAIL_ADDRESS_MAX);
		}
	}

	/**
	 * �q�O���[�v���擾����B
	 * @return �q�O���[�v
	 */
	public ArrayList<MstGroup> getGroups()
	{
		return groups;
	}

	/**
	 * �q�O���[�v���Z�b�g����B
	 * @param groups �q�O���[�v
	 */
	public void setGroups(ArrayList<MstGroup> groups)
	{
		this.groups = groups;
	}

	/**
	 * �q�X�܂��擾����B
	 * @return �q�X��
	 */
	public ArrayList<MstShop> getShops()
	{
		return shops;
	}

	/**
	 * �q�X�܂��Z�b�g����B
	 * @param shops �q�X��
	 */
	public void setShops(ArrayList<MstShop> shops)
	{
		this.shops = shops;
	}

	/**
	 * MstGroup����f�[�^���Z�b�g����B
	 * @param mg MstGroup
	 */
	public void setData(MstGroup mg)
	{
		this.setGroupID(mg.getGroupID());
		this.setGroupName(mg.getGroupName());
		this.setParent(mg.getParent());
		this.setLevel(mg.getLevel());
		this.setPostalCode(mg.getPostalCode());
		this.setAddress(mg.getAddress());
		this.setPhoneNumber(mg.getPhoneNumber());
		this.setFaxNumber(mg.getFaxNumber());
		this.setMailAddress(mg.getMailAddress());
		this.setGroups(mg.getGroups());
		this.setShops(mg.getShops());
	}

	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����B
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException ��O
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setGroupID(rs.getInt("group_id"));
		this.setGroupName(rs.getString("group_name"));
		this.setPostalCode(rs.getString("postal_code"));
		this.setAddress(0, rs.getString("address1"));
		this.setAddress(1, rs.getString("address2"));
		this.setAddress(2, rs.getString("address3"));
		this.setAddress(3, rs.getString("address4"));
		this.setPhoneNumber(rs.getString("phone_number"));
		this.setFaxNumber(rs.getString("fax_number"));
		this.setMailAddress(rs.getString("mail_address"));
	}

	/**
	 * ������i�O���[�v���j���擾����B
	 * @return ������i�O���[�v���j
	 */
	public String toString()
	{
		StringBuffer	sb	=	new StringBuffer();

		for(int i = 0; i < this.level; i ++)
				sb.append("�@�@");

		return	sb.toString() + this.getGroupName();
	}

	public boolean equals(Object o)
	{
		if(o != null && o instanceof MstGroup)
		{
			if(((MstGroup)o).getGroupID() == this.getGroupID())
			{
				return	true;
			}
			else
			{
				return	false;
			}
		}
		else
		{
			return	false;
		}
	}


	/**
	 * �f�[�^�x�[�X����f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException ��O
	 * @return ���������ꍇtrue
	 */
	public boolean loadData(ConnectionWrapper con) throws SQLException
	{
		//�h�c���ݒ肳��Ă��Ȃ��ꍇtrue��Ԃ�
		if(this.getGroupID() == null)	return	true;
		//���g�̃f�[�^���擾
		if(!this.load(con))	return	false;
		//�q�O���[�v���擾
		if(!this.loadGroups(con))	return	false;
		//�q�X�܂��擾
		if(!this.loadShops(con))	return	false;
		//�f�[�^�x�[�X����q�O���[�v�̎q�̃f�[�^���擾
		if(!this.loadChildrenData(con))	return	false;

		return	true;
	}

	/**
	 * �f�[�^�x�[�X����f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException ��O
	 * @return ���������ꍇtrue
	 */
	public boolean load(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())
		{
			this.setData(rs);
		}

		rs.close();

		return	true;
	}

	/**
	 * �f�[�^�x�[�X����q�O���[�v�̃f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException ��O
	 * @return ���������ꍇtrue
	 */
	private boolean loadGroups(ConnectionWrapper con) throws SQLException
	{
		this.getGroups().clear();

		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectGroupsSQL());

		while(rs.next())
		{
			MstGroup	mg	=	new MstGroup();
			mg.setData(rs);
			mg.setParent(this);
			mg.setLevel(this.getLevel() + 1);
			this.getGroups().add(mg);
		}

		rs.close();

		return	true;
	}

	/**
	 * �f�[�^�x�[�X����q�X�܂̃f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException ��O
	 * @return ���������ꍇtrue
	 */
	private boolean loadShops(ConnectionWrapper con) throws SQLException
	{
		this.getShops().clear();

		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectShopsSQL());

		while(rs.next())
		{
			MstShop	ms	=	new MstShop();
			ms.setData(rs);
			this.getShops().add(ms);
		}

		rs.close();

		return	true;
	}


	/**
	 * �f�[�^�x�[�X����q�O���[�v�̎q�̃f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException ��O
	 * @return ���������ꍇtrue
	 */
	private boolean loadChildrenData(ConnectionWrapper con) throws SQLException
	{
		for(MstGroup mg : this.getGroups())
		{
			mg.loadData(con);
		}

		return	true;
	}


	/**
	 * �O���[�v�}�X�^�Ƀf�[�^��o�^����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		if(isExists(con))
		{
			return	(con.executeUpdate(this.getUpdateSQL()) == 1);
		}
		else
		{
			if(con.executeUpdate(this.getInsertSQL()) != 1)
			{
				return	false;
			}

			this.setGroupID(getMaxGroupID(con));

			return	(MstAuthority.insertFromDefaultAuthority(con, parent.getGroupID(), groupID, 0));
		}
	}


	/**
	 * �O���[�v�}�X�^����f�[�^���폜����B�i�_���폜�j
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
	 */
	public boolean delete(ConnectionWrapper con) throws SQLException
	{
		if(isExists(con))
		{
			if(con.executeUpdate(this.getDeleteSQL()) == 1)
			{
				MstAuthority	ma	=	new MstAuthority();
				ma.setGroup(this);
				MstShop		ms	=	new MstShop();
				ms.setShopID(0);
				ma.setShop(ms);

				if(ma.delete(con))
				{
					return	true;
				}
			}
		}

		return	false;
	}

	/**
	 * �O���[�v�}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ���݂���
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getGroupID() == null || this.getGroupID() < 1)	return	false;

		if(con == null)	return	false;

		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}

	/**
	 * �q�O���[�v���擾����r�p�k�����擾����B
	 * @return �q�O���[�v���擾����r�p�k��
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_group\n" +
				"where group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and delete_date is null\n";
	}

	/**
	 * �q�O���[�v���擾����r�p�k�����擾����B
	 * @return �q�O���[�v���擾����r�p�k��
	 */
	private String getSelectGroupsSQL()
	{
		return	"select *\n" +
				"from mst_group\n" +
				"where parent = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and delete_date is null\n" +
				"order by group_id\n";
	}

	/**
	 * �q�X�܂��擾����r�p�k�����擾����B
	 * @return �q�X�܂��擾����r�p�k��
	 */
	private String getSelectShopsSQL()
	{
		return	"select *\n" +
				"from mst_shop\n" +
				"where group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and delete_date is null\n" +
				"order by shop_id\n";
	}

	/**
	 * Insert�����擾����B
	 * @return Insert��
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_group\n" +
				"select\n" +
				"coalesce(max(group_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getGroupName()) + ",\n" +
				SQLUtil.convertForSQL(this.getParentID()) + ",\n" +
				SQLUtil.convertForSQL(this.getPostalCode()) + ",\n" +
				SQLUtil.convertForSQL(this.getAddress(0)) + ",\n" +
				SQLUtil.convertForSQL(this.getAddress(1)) + ",\n" +
				SQLUtil.convertForSQL(this.getAddress(2)) + ",\n" +
				SQLUtil.convertForSQL(this.getAddress(3)) + ",\n" +
				SQLUtil.convertForSQL(this.getPhoneNumber()) + ",\n" +
				SQLUtil.convertForSQL(this.getFaxNumber()) + ",\n" +
				SQLUtil.convertForSQL(this.getMailAddress()) + ",\n" +
				"current_timestamp, current_timestamp, null\n" +
				"from mst_group\n";
	}

	/**
	 * Update�����擾����B
	 * @return Update��
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_group\n" +
				"set\n" +
				"group_name = " + SQLUtil.convertForSQL(this.getGroupName()) + ",\n" +
				"parent = " + SQLUtil.convertForSQL(this.getParentID()) + ",\n" +
				"postal_code = " + SQLUtil.convertForSQL(this.getPostalCode()) + ",\n" +
				"address1 = " + SQLUtil.convertForSQL(this.getAddress(0)) + ",\n" +
				"address2 = " + SQLUtil.convertForSQL(this.getAddress(1)) + ",\n" +
				"address3 = " + SQLUtil.convertForSQL(this.getAddress(2)) + ",\n" +
				"address4 = " + SQLUtil.convertForSQL(this.getAddress(3)) + ",\n" +
				"phone_number = " + SQLUtil.convertForSQL(this.getPhoneNumber()) + ",\n" +
				"fax_number = " + SQLUtil.convertForSQL(this.getFaxNumber()) + ",\n" +
				"mail_address = " + SQLUtil.convertForSQL(this.getMailAddress()) + ",\n" +
				"update_date = current_timestamp\n" +
				"where	group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n";
	}

	/**
	 * �폜�pUpdate�����擾����B
	 * @return �폜�pUpdate��
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_group\n" +
				"set\n" +
				"update_date = current_timestamp,\n" +
				"delete_date = current_timestamp\n" +
				"where	group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n";
	}

	/**
	 * �O���[�vID�̍ő�l���擾����B
	 * @param con
	 * @throws java.sql.SQLException
	 * @return
	 */
	public static Integer getMaxGroupID(ConnectionWrapper con) throws SQLException
	{
		Integer		result	=	null;

		if(con == null)	return	result;

		ResultSetWrapper	rs	=	con.executeQuery(getMaxGroupIDSQL());

		if(rs.next())
		{
			result	=	rs.getInt("max_group_id");
		}

		rs.close();

		return	result;
	}

	/**
	 *
	 * @return
	 */
	private static String getMaxGroupIDSQL()
	{
		return	"select max(group_id) as max_group_id\n" +
				"from mst_group";
	}

	/**
	 *
	 * @param type
	 * @return
	 */
	public JComboBox createComboBox(int type)
	{
		JComboBox	cb	=	new JComboBox();

		this.addGroupDataToJComboBox(cb, type);

		return	cb;
	}

	/**
	 *
	 * @param cb
	 * @param type
	 */
	public void addGroupDataToJComboBox(JComboBox cb, int type)
	{
		for(MstGroup mg : this.getGroups())
		{
			if(type == 1 || type == 3)
				cb.addItem(mg);

			mg.addGroupDataToJComboBox(cb, type);
		}

		if(type == 2 || type == 3)
		{
			for(MstShop ms : this.getShops())
			{
				cb.addItem(ms);
			}
		}
	}

	/**
	 *
	 * @param isSetShop
	 * @return
	 */
	public JTree createJTree(boolean isSetShop)
	{
		JTree	t	=	new JTree(this.createTreeNode(isSetShop));

		return	t;
	}


	/**
	 *
	 * @param isSetShop
	 * @return
	 */
	public DefaultMutableTreeNode createTreeNode(boolean isSetShop)
	{
		DefaultMutableTreeNode	root	=	new DefaultMutableTreeNode(this);

		this.setTreeNodeChildren(root, isSetShop);

		return	root;
	}

	/**
	 *
	 * @param baseNode
	 * @param isSetShop
	 */
	public void setTreeNodeChildren(DefaultMutableTreeNode baseNode, boolean isSetShop)
	{
		for(MstGroup mg : this.getGroups())
		{
			DefaultMutableTreeNode	groupNode	=	new DefaultMutableTreeNode(mg);
			groupNode.setAllowsChildren(true);
			mg.setTreeNodeChildren(groupNode, isSetShop);
			baseNode.add(groupNode);
		}

		if(isSetShop)
		{
			for(MstShop ms : this.getShops())
			{
				DefaultMutableTreeNode	shopNode	=	new DefaultMutableTreeNode(ms);
				shopNode.setAllowsChildren(false);
				baseNode.add(shopNode);
			}
		}
	}

	/**
	 *
	 * @return
	 */
	public String getShopIDListAll()
	{
		String	temp	=	"";

		for(MstGroup mg : this.getGroups())
		{
			String tmp = mg.getShopIDListAll();
			if((0 < mg.getShops().size() && !temp.equals(""))
					|| (!temp.equals("") && tmp.charAt(0)!=','))
			{
				temp	+=	", ";
			}
			temp	+=	tmp;
		}

		if(0 < this.getShops().size())
		{
			if(!temp.equals(""))
				temp	+=	", ";
			temp	+=	this.getShopIDList();
		}

		return	temp;
	}

	/**
	 *
	 * @return
	 */
	public String getShopIDList()
	{
		String	temp	=	"";

		for(MstShop ms : this.getShops())
		{
			if(!temp.equals(""))
				temp	+=	", ";
			temp	+=	ms.getShopID().toString();
		}

		return	temp;
	}
}
