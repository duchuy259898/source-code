/*
 * MstStaffs.java
 *
 * Created on 2006/07/10, 10:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.company;

import com.geobeck.sosia.pos.system.SystemInfo;
import java.util.*;
import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.SQLUtil;

/**
 *
 * @author katagiri
 */
public class MstStaffs extends ArrayList<MstStaff>
{
	private String		shopIDList		=	"";
	
	/**
	 * コンストラクタ
	 */
	public MstStaffs()
	{
	}

	public String getShopIDList()
	{
		return shopIDList;
	}

	public void setShopIDList(String shopIDList)
	{
		this.shopIDList = shopIDList;
	}
	
	/**
	 * スタッフを読み込む
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean load(ConnectionWrapper con, boolean isAddBlank) throws SQLException
	{
		this.clear();
		
		if (isAddBlank) {
                    this.add(new MstStaff());
		}
		
		ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

		while (rs.next()) {
                    
                    MstStaff ms = new MstStaff();
                    ms.setData(rs);
                    MstStaffClass msc = new MstStaffClass();
                    msc.setStaffClassID(rs.getInt("staff_class_id"));
                    msc.setStaffClassName(rs.getString("staff_class_name"));
                    ms.setStaffClass(msc);

                    this.add(ms);
		}
		
		return	true;
	}
	
	public String getSelectSQL()
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      ms.*");
            sql.append("     ,msc.staff_class_name");
            sql.append("     ,(");
            sql.append("         select 1 from mst_staff_nondisplay");
            sql.append("         where shop_id in (" + this.getShopIDList() + ")");
            sql.append("           and staff_id = ms.staff_id");
            sql.append("     ) is null as display");
            sql.append(" from");
            sql.append("     mst_staff ms");
            sql.append("         left outer join mst_staff_class msc");
            sql.append("                      on msc.staff_class_id = ms.staff_class_id");
            sql.append("                     and msc.delete_date is null");
            sql.append(" where");
            sql.append("     ms.delete_date is null");
            sql.append(" order by");
            
            if (! this.getShopIDList().equals("")) {
                sql.append("  case when ms.shop_id in (" + this.getShopIDList() + ") then 0 else 1 end,");
            }
            
            sql.append("      ms.shop_id");
            sql.append("     ,ms.display_seq");
            sql.append("     ,lpad(ms.staff_no, 10, '0')");
            sql.append("     ,ms.staff_id");

            return sql.toString();
	}
	
	/**
	 * スタッフを読み込む
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean loadOnlyShop(ConnectionWrapper con, boolean isAddBlank) throws SQLException
	{
		this.clear();
		
		if (isAddBlank) {
                    this.add(new MstStaff());
		}
		
		ResultSetWrapper rs = con.executeQuery(this.getSelectSQLOnlyShop());

		while(rs.next())
		{
                    MstStaff ms = new MstStaff();
                    ms.setData(rs);
                    MstStaffClass msc = new MstStaffClass();
                    msc.setStaffClassID(rs.getInt("staff_class_id"));
                    msc.setStaffClassName(rs.getString("staff_class_name"));
                    ms.setStaffClass(msc);

                    this.add(ms);
		}
		
		return true;
	}
	
	public String getSelectSQLOnlyShop()
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      ms.*");
            sql.append("     ,msc.staff_class_name");
            sql.append(" from");
            sql.append("     mst_staff ms");
            sql.append("         left join mst_staff_class msc");
            sql.append("                on msc.staff_class_id = ms.staff_class_id");
            sql.append("               and msc.delete_date is null");
            sql.append(" where");
            sql.append("         ms.delete_date is null");
            sql.append("     and ms.shop_id in (" + this.getShopIDList() + ")");
            sql.append(" order by");
            
            if (!this.getShopIDList().equals("")) {
                sql.append(" case when ms.shop_id in (" + this.getShopIDList() + ") then 0 else 1 end,");
            }
            sql.append("      ms.shop_id");
            sql.append("     ,ms.display_seq");
            sql.append("     ,lpad(ms.staff_no, 10, '0')");
            sql.append("     ,ms.staff_id");

            return sql.toString();
	}
        
	public int getIndexByID(Integer staffID)
	{	
		for(int i = 0; i < this.size(); i ++)
		{
			MstStaff ms	=	this.get(i);
			if(ms.getStaffID() != null && ms.getStaffID().equals( staffID ))
			{
				return	i;
			}
		}
		
		return	-1;
	}
}
