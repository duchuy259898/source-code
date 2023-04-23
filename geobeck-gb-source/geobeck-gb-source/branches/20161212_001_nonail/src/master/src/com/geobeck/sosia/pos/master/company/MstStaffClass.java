/*
 * MstStaffClass.java
 *
 * Created on 2006/04/25, 17:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.company;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * スタッフ区分データ
 * @author katagiri
 */
public class MstStaffClass
{
	/**
	 * スタッフ区分ＩＤ
	 */
	private	Integer         staffClassID        =   null;
	/**
	 * スタッフ区分名
	 */
	private	String          staffClassName      =   "";
	/**
	 * スタッフ区分略称
	 */
	protected String        staffClassContractedName    =   "";
	/**
	 * 予約表表示フラグ
	 */
        private Boolean         displayReservation  =   false;
	/**
	 * 表示順
	 */
	private Integer         displaySeq          =   null;

	/**
	 * コンストラクタ
	 */
	public MstStaffClass()
	{
	}
	
	/**
	 * 文字列に変換する。（スタッフ区分名）
	 * @return スタッフ区分名
	 */
	public String toString()
	{
		return	this.getStaffClassName();
	}

	/**
	 * スタッフ区分ＩＤを取得する。
	 * @return スタッフ区分ＩＤ
	 */
	public Integer getStaffClassID()
	{
		return staffClassID;
	}

	/**
	 * スタッフ区分ＩＤをセットする。
	 * @param staffClassID スタッフ区分ＩＤ
	 */
	public void setStaffClassID(Integer staffClassID)
	{
		this.staffClassID = staffClassID;
	}

	/**
	 * スタッフ区分名を取得する。
	 * @return スタッフ区分名
	 */
	public String getStaffClassName()
	{
		return staffClassName;
	}

	/**
	 * スタッフ区分名をセットする。
	 * @param staffClassName スタッフ区分名
	 */
	public void setStaffClassName(String staffClassName)
	{
		this.staffClassName = staffClassName;
	}

        /**
	 * スタッフ区分略称を取得する。
	 * @return スタッフ区分略称
	 */
	public String getStaffClassContractedName()
	{
		return staffClassContractedName;
	}

	/**
	 * スタッフ区分略称をセットする。
	 * @param staffClassContractedName スタッフ区分略称
	 */
	public void setStaffClassContractedName(String staffClassContractedName)
	{
		this.staffClassContractedName = staffClassContractedName;
	}
        
	/**
	 * 予約表表示フラグを取得する。
	 * @return 予約表表示フラグ
	 */
        public Boolean isDisplayReservation(){
                return this.displayReservation;
        }
	/**
	 * 予約表表示フラグをセットする。
	 * @param displayReservation 予約表表示フラグ
	 */
        public void setDisplayReservation(Boolean displayReservation){
                this.displayReservation = displayReservation;
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
	
	/**
	 * データをクリアする。
	 */
	public void clear()
	{
		this.setStaffClassID(null);
		this.setStaffClassName("");
                this.setStaffClassContractedName("");
                this.setDisplayReservation(false);
                this.setDisplaySeq(0);
	}
	
	/**
	 * スタッフ区分マスタデータからデータをセットする。
	 * @param msc スタッフ区分マスタデータ
	 */
	public void setData(MstStaffClass msc)
	{
		this.setStaffClassID(msc.getStaffClassID());
		this.setStaffClassName(msc.getStaffClassName());
		this.setStaffClassContractedName(msc.getStaffClassContractedName());
		this.setDisplayReservation(msc.isDisplayReservation());
		this.setDisplaySeq(msc.getDisplaySeq());
	}
        
	/**
	 * ResultSetWrapperのデータを読み込む。
	 * @param rs ResultSetWrapper 
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setStaffClassID(rs.getInt("staff_class_id"));
		this.setStaffClassName(rs.getString("staff_class_name"));
		this.setStaffClassContractedName(rs.getString("staff_class_contracted_name"));
		this.setDisplaySeq(rs.getInt("display_seq"));
		this.setDisplayReservation(rs.getBoolean("display_reservation"));
	}
	
	/**
	 * スタッフ区分マスタデータをArrayListに読み込む。
	 */
	public ArrayList<MstStaffClass> load(ConnectionWrapper con) throws SQLException
	{
		ArrayList<MstStaffClass> list = new ArrayList<MstStaffClass>();
                
		ResultSetWrapper rs = con.executeQuery(getSelectAllSQL());

		while(rs.next())
		{
			MstStaffClass msc = new	MstStaffClass();
			msc.setData(rs);
			list.add(msc);
		}

		rs.close();
                
                return list;
	}

	/**
	 * スタッフ区分マスタにデータを登録する。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean regist(ConnectionWrapper con,
			Integer lastSeq) throws SQLException
	{
		if(isExists(con))
		{
			if(lastSeq != this.getDisplaySeq())
			{
				if(con.executeUpdate(this.getSlideSQL(lastSeq, false)) < 0)
				{
					return	false;
				}
				
				if(0 <= this.getDisplaySeq())
				{
					if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0)
					{
						return	false;
					}
				}
			}
			
			if(con.executeUpdate(this.getUpdateSQL()) != 1)
			{
				return	false;
			}
		}
		else
		{
			if(0 <= this.getDisplaySeq())
			{
				if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0)
				{
					return	false;
				}
			}
			
			if(con.executeUpdate(this.getInsertSQL()) != 1)
			{
				return	false;
			}
		}
		
		return	true;
	}
	
	/**
	 * スタッフ区分マスタからデータを削除する。（論理削除）
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
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
		
		if(con.executeUpdate(sql) != 1)
		{
			return	false;
		}
		
		if(con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), false)) < 0)
		{
			return	false;
		}
		
		return	true;
	}
	
	/**
	 * スタッフ区分マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getStaffClassID() == null || this.getStaffClassID() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}

	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectAllSQL()
	{
		return	"select *\n" +
				"from mst_staff_class\n" +
				"where delete_date is null\n" +
				"order by display_seq, staff_class_id\n";
	}
        
	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_staff_class\n" +
				"where	staff_class_id = " + SQLUtil.convertForSQL(this.getStaffClassID()) + "\n";
	}

	/**
	 * 表示順をずらすＳＱＬ文を取得する
	 * @param seq 元の表示順
	 * @param isIncrement true - 加算、false - 減算
	 * @return 表示順をずらすＳＱＬ文
	 */
	private String getSlideSQL(Integer seq, boolean isIncrement)
	{
		return	"update mst_staff_class\n" +
				"set display_seq = display_seq " +
						(isIncrement ? "+" : "-") + " 1\n" +
				"where delete_date is null\n" +
				"and display_seq >= " + SQLUtil.convertForSQL(seq) + "\n";
	}
        
	/**
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_staff_class\n" +
				"(staff_class_id, staff_class_name, staff_class_contracted_name, display_reservation, display_seq, \n" +
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				"coalesce(max(staff_class_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getStaffClassName()) + ",\n" +
				SQLUtil.convertForSQL(this.getStaffClassContractedName()) + ",\n" +
				SQLUtil.convertForSQL(this.isDisplayReservation()) + ",\n" +
				"case\n" +
				"when " + SQLUtil.convertForSQL(this.getDisplaySeq()) +
				" between 0 and coalesce((select max(display_seq)\n" +
				"from mst_staff_class\n" +
				"where delete_date is null), 0) then " +
				SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
				"else coalesce((select max(display_seq)\n" +
				"from mst_staff_class\n" +
				"where delete_date is null), 0) + 1 end,\n" +
				"current_timestamp, current_timestamp, null\n" +
				"from mst_staff_class\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_staff_class\n" +
				"set\n" +
				"staff_class_name = " + SQLUtil.convertForSQL(this.getStaffClassName()) + ",\n" +
				"staff_class_contracted_name = " + SQLUtil.convertForSQL(this.getStaffClassContractedName()) + ",\n" +
				"display_reservation = " + SQLUtil.convertForSQL(this.isDisplayReservation()) + ",\n" +
                                "display_seq =\n" +
                                "    case\n" +
                                "        when " + SQLUtil.convertForSQL(this.getDisplaySeq()) + " between 0\n" +
                                "         and coalesce\n" +
                                "                ((\n" +
                                "                    select\n" +
                                "                        max(display_seq)\n" +
                                "                    from\n" +
                                "                        mst_staff_class\n" +
                                "                    where\n" +
                                "                            delete_date is null\n" +
                                "                        and staff_class_id != " + SQLUtil.convertForSQL(this.getStaffClassID()) + "\n" +
                                "                ), 0)\n" +
                                "        then " + SQLUtil.convertForSQL(this.getDisplaySeq()) + "\n" +
                                "        else\n" +
                                "            coalesce\n" +
                                "                ((\n" +
                                "                    select\n" +
                                "                        max(display_seq)\n" +
                                "                    from\n" +
                                "                        mst_staff_class\n" +
                                "                    where\n" +
                                "                            delete_date is null\n" +
                                "                        and staff_class_id != " + SQLUtil.convertForSQL(this.getStaffClassID()) + "\n" +
                                "                ), 0) + 1\n" +
                                "    end,\n" +
				"update_date = current_timestamp\n" +
				"where	staff_class_id = " + SQLUtil.convertForSQL(this.getStaffClassID()) + "\n";
	}

	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_staff_class\n" +
				"set\n" +
				"delete_date = current_timestamp\n" +
				"where	staff_class_id = " + SQLUtil.convertForSQL(this.getStaffClassID()) + "\n";
	}
}
