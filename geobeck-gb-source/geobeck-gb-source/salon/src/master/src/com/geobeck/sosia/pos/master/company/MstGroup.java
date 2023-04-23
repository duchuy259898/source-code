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
 * グループマスタクラス
 * @author katagiri
 */
public class MstGroup
{
	/**
	 * 郵便番号の文字数の最大値
	 */
	public static int		POSTAL_CODE_MAX			=	7;
	/**
	 * 電話番号の文字数の最大値
	 */
	public static int		PHONE_NUMBER_MAX		=	15;
	/**
	 * ＦＡＸ番号の文字数の最大値
	 */
	public static int		FAX_NUMBER_MAX			=	15;
	/**
	 * メールアドレスの文字数の最大値
	 */
	public static int		MAIL_ADDRESS_MAX		=	64;

	/**
	 * グループID
	 */
	protected	Integer					groupID		=	null;
	/**
	 * グループ名
	 */
	protected	String					groupName	=	"";
	/**
	 * 親グループ
	 */
	protected	MstGroup				parent		=	null;
	/**
	 * 階層
	 */
	protected	Integer					level		=	0;
	/**
	 * 郵便番号
	 */
	private	String		postalCode			=	"";
	/**
	 * 住所
	 */
	private	String[]	address				=	{"", "", "", ""};
	/**
	 * 電話番号
	 */
	private	String		phoneNumber			=	"";
	/**
	 * ＦＡＸ番号
	 */
	private	String		faxNumber			=	"";
	/**
	 * メールアドレス
	 */
	private	String		mailAddress			=	"";

	/**
	 * 子グループ
	 */
	protected	ArrayList<MstGroup>		groups	=	new ArrayList<MstGroup>();
	/**
	 * 子店舗
	 */
	protected	ArrayList<MstShop>		shops	=	new ArrayList<MstShop>();

	/**
	 * コンストラクタ
	 */
	public MstGroup()
	{
	}

	/**
	 * グループIDを取得する。
	 * @return グループID
	 */
	public Integer getGroupID()
	{
		return groupID;
	}

	/**
	 * グループIDをセットする。
	 * @param groupID グループID
	 */
	public void setGroupID(Integer groupID)
	{
		this.groupID = groupID;
	}

	/**
	 * グループ名を取得する。
	 * @return グループ名
	 */
	public String getGroupName()
	{
		return groupName;
	}

	/**
	 * グループ名をセットする。
	 * @param groupName グループ名
	 */
	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	/**
	 * 親グループを取得する。
	 * @return 親グループ
	 */
	public MstGroup getParent()
	{
		return parent;
	}

	/**
	 * 親グループを設定する。
	 * @param parent 親グループ
	 */
	public void setParent(MstGroup parent)
	{
		this.parent = parent;
	}

	/**
	 * 親グループのIDを取得する。
	 * @return 親グループのID
	 */
	public Integer getParentID()
	{
		if(this.getParent() == null)
			return	null;
		else
			return	this.getParent().getGroupID();
	}

	/**
	 * 階層を取得する。
	 * @return 階層
	 */
	public Integer getLevel()
	{
		return level;
	}

	/**
	 * 階層をセットする。
	 * @param level 階層
	 */
	public void setLevel(Integer level)
	{
		this.level = level;
	}

	/**
	 * 郵便番号を取得する。
	 * @return 郵便番号
	 */
	public String getPostalCode()
	{
		return postalCode;
	}

	/**
	 * 郵便番号をセットする。
	 * @param postalCode 郵便番号
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
	 * 住所を取得する。
	 * @return 住所
	 */
	public String[] getAddress()
	{
		return address;
	}

	/**
	 * 住所を取得する。
	 * @param index インデックス
	 * @return 住所
	 */
	public String getAddress(int index)
	{
		return address[index];
	}

	/**
	 * 住所
	 * @param address 住所
	 */
	public void setAddress(String[] address)
	{
		this.address = address;
	}

	/**
	 * 住所をセットする。
	 * @param index インデックス
	 * @param address 住所
	 */
	public void setAddress(int index, String address)
	{
		this.address[index] = address;
	}

	/**
	 * 住所を取得する。
	 * @return 住所
	 */
	public String getFullAddress()
	{
		return	(address[0] == null ? "" : address[0]) +
				(address[1] == null ? "" : address[1]) +
				(address[2] == null ? "" : address[2]) +
				(address[3] == null ? "" : address[3]);
	}

	/**
	 * 電話番号を取得する。
	 * @return 電話番号
	 */
	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	/**
	 * 電話番号をセットする。
	 * @param phoneNumber 電話番号
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
	 * ＦＡＸ番号を取得する。
	 * @return ＦＡＸ番号
	 */
	public String getFaxNumber()
	{
		return faxNumber;
	}

	/**
	 * ＦＡＸ番号をセットする。
	 * @param faxNumber ＦＡＸ番号
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
	 * メールアドレスを取得する。
	 * @return メールアドレス
	 */
	public String getMailAddress()
	{
		return mailAddress;
	}

	/**
	 * メールアドレスをセットする。
	 * @param mailAddress メールアドレス
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
	 * 子グループを取得する。
	 * @return 子グループ
	 */
	public ArrayList<MstGroup> getGroups()
	{
		return groups;
	}

	/**
	 * 子グループをセットする。
	 * @param groups 子グループ
	 */
	public void setGroups(ArrayList<MstGroup> groups)
	{
		this.groups = groups;
	}

	/**
	 * 子店舗を取得する。
	 * @return 子店舗
	 */
	public ArrayList<MstShop> getShops()
	{
		return shops;
	}

	/**
	 * 子店舗をセットする。
	 * @param shops 子店舗
	 */
	public void setShops(ArrayList<MstShop> shops)
	{
		this.shops = shops;
	}

	/**
	 * MstGroupからデータをセットする。
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
	 * ResultSetWrapperからデータをセットする。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException 例外
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
	 * 文字列（グループ名）を取得する。
	 * @return 文字列（グループ名）
	 */
	public String toString()
	{
		StringBuffer	sb	=	new StringBuffer();

		for(int i = 0; i < this.level; i ++)
				sb.append("　　");

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
	 * データベースからデータを読み込む。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException 例外
	 * @return 成功した場合true
	 */
	public boolean loadData(ConnectionWrapper con) throws SQLException
	{
		//ＩＤが設定されていない場合trueを返す
		if(this.getGroupID() == null)	return	true;
		//自身のデータを取得
		if(!this.load(con))	return	false;
		//子グループを取得
		if(!this.loadGroups(con))	return	false;
		//子店舗を取得
		if(!this.loadShops(con))	return	false;
		//データベースから子グループの子のデータを取得
		if(!this.loadChildrenData(con))	return	false;

		return	true;
	}

	/**
	 * データベースからデータを読み込む。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException 例外
	 * @return 成功した場合true
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
	 * データベースから子グループのデータを読み込む。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException 例外
	 * @return 成功した場合true
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
	 * データベースから子店舗のデータを読み込む。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException 例外
	 * @return 成功した場合true
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
	 * データベースから子グループの子のデータを読み込む。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException 例外
	 * @return 成功した場合true
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
	 * グループマスタにデータを登録する。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
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
	 * グループマスタからデータを削除する。（論理削除）
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
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
	 * グループマスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
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
	 * 子グループを取得するＳＱＬ文を取得する。
	 * @return 子グループを取得するＳＱＬ文
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_group\n" +
				"where group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + "\n" +
				"and delete_date is null\n";
	}

	/**
	 * 子グループを取得するＳＱＬ文を取得する。
	 * @return 子グループを取得するＳＱＬ文
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
	 * 子店舗を取得するＳＱＬ文を取得する。
	 * @return 子店舗を取得するＳＱＬ文
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
	 * Insert文を取得する。
	 * @return Insert文
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
	 * Update文を取得する。
	 * @return Update文
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
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
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
	 * グループIDの最大値を取得する。
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
