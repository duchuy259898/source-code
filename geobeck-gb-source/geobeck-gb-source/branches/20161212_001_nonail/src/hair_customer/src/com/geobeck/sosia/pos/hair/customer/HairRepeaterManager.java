/*
 * HairRepeaterManager.java
 *
 * Created on 2007/03/29, 9:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.customer;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.customer.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.report.util.*;

/**
 *
 * @author katagiri
 */
public class HairRepeaterManager extends ArrayList<HairRepeaterData>
{
	private static final String		REPORT_PATH	=	"/report/RepeaterList.jasper";
	private static final String		REPORT_NAME	=	"RepeaterList";
	
	private String				targetName		=	"";
	private String				targetList		=	"";
	private	boolean				repeater		=	true;
	private	GregorianCalendar	termFrom		=	null;
	private	GregorianCalendar	termTo			=	null;
        private	GregorianCalendar	totalFrom		=	null;
        private	GregorianCalendar	totalTo			=	null;
        
	
	/** Creates a new instance of HairRepeaterManager */
	public HairRepeaterManager()
	{
		super();
	}

	public String getTargetName()
	{
		return targetName;
	}

	public void setTargetName(String targetName)
	{
		this.targetName = targetName;
	}

	public String getTargetList()
	{
		return targetList;
	}

	public void setTargetList(String targetList)
	{
		this.targetList = targetList;
	}

	public boolean isRepeater()
	{
		return repeater;
	}

	public void setRepeater(boolean repeater)
	{
		this.repeater = repeater;
	}
	
	public String getTargetTypeName()
	{
		if(repeater)
		{
			return	"再来店者";
		}
		else
		{
			return	"失客候補者";
		}
	}

	public GregorianCalendar getTermFrom()
	{
		return termFrom;
	}

	public void setTermFrom(GregorianCalendar termFrom)
	{
		this.termFrom = termFrom;
	}

	public GregorianCalendar getTermTo()
	{
		return termTo;
	}

	public void setTermTo(GregorianCalendar termTo)
	{
		this.termTo = termTo;
	}
	
	public String getTermString()
	{
		if(this.getTermFrom() == null || this.getTermTo() == null)
		{
			return	"";
		}
		
		if(this.getTermFrom() == this.getTermTo())
		{
			return	String.format("%1$tY年%1$tm月%1$td日", this.getTermFrom());
		}
		else
		{
			return	String.format("%1$tY年%1$tm月%1$td日　～　%2$tY年%2$tm月%2$td日",
					this.getTermFrom(), this.getTermTo());
		}
	}
	
	public String getTotalString()
	{
		if(this.getTotalFrom() == null || this.getTotalTo() == null)
		{
			return	"";
		}
		
		if(this.getTotalFrom() == this.getTotalTo())
		{
			return	String.format("%1$tY年%1$tm月%1$td日", this.getTotalFrom());
		}
		else
		{
			return	String.format("%1$tY年%1$tm月%1$td日　～　%2$tY年%2$tm月%2$td日",
					this.getTotalFrom(), this.getTotalTo());
		}
	}
	
	public GregorianCalendar getTotalFrom()
	{
		return totalFrom;
	}

	public void setTotalFrom(GregorianCalendar totalFrom)
	{
		this.totalFrom = totalFrom;
	}
        
	public GregorianCalendar getTotalTo()
	{
		return totalTo;
	}

	public void setTotalTo(GregorianCalendar totalTo)
	{
		this.totalTo = totalTo;
	}
        
        
        
        public boolean load()
	{
		this.clear();
		
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			ResultSetWrapper	rs	=	con.executeQuery(this.getLoadSQL());
			
			while(rs.next())
			{
				HairRepeaterData	rd	=	new HairRepeaterData();
				rd.setData(rs);
				rd.setSelected( true );		// メール送信対象フラグをデフォルトチェック。
				this.add(rd);
			}

                        rs.close();
                      
                        
                        //同一来店日のときに再来店日付も同一になってしまうので再来店日を修正
                        //データ取得時に、来店日、顧客ID、来店回数、伝票Noの順にソートされていることが前提
                        for(int i = 1 ; i < this.size() ; i++)
                        {
                            //同一顧客で
                            if(this.get(i - 1).getCustomerID().equals(this.get(i).getCustomerID()))
                            {
                                //同一来店日
                                if(this.get(i - 1).getSalesDate().equals(this.get(i).getSalesDate()))
                                {
                                    if(this.isRepeater())
                                    {
                                        //再来
                                        this.get(i - 1).setRepeatDate(this.get(i).getSalesDate());

                                    }
                                    else
                                    {
                                        //再来
                                        this.remove(i - 1);
                                        i--;
                                    }
                                }    
                                
                            }
                        }

		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e.getCause());
		}
		
		return	true;
	}
	
	private String getLoadSQL()
	{
		String SQL =	"select ds.sales_date, ds.repeat_date, ds.visit_num, dsd.sales_value, mc.*\n" +
				"from (\n" +
                                "select ds_sub.shop_id, ds_sub.slip_no, ds_sub.sales_date, ds_sub.customer_id \n" +
				"     , get_visit_count(ds_sub.customer_id,ds_sub.shop_id,repeat_date) As visit_num \n" +
				"     , ds_sub.repeat_date \n" +
                                "from (\n" +
				"select ds.shop_id, ds.slip_no, ds.sales_date, ds.customer_id, ds.visit_num,\n" +
				"min(ds_repeat.sales_date) as repeat_date\n" +
				"from data_sales ds\n" ;
		if(this.isRepeater())
                {
                    SQL += "inner join data_sales ds_repeat\n" ;
                }else
                {
                    SQL += "left join data_sales ds_repeat\n" ;
                }
		SQL += 		"on ds_repeat.sales_date > ds.sales_date\n" +
				"and ds_repeat.delete_date is null\n" +
				"and ds_repeat.shop_id = ds.shop_id\n" +
				"and ds_repeat.customer_id = ds.customer_id\n" +
				"where ds.delete_date is null\n" +
				"and ds.shop_id in (" + SQLUtil.convertForSQL(this.getTargetList()) + ")\n" +
				"and ds.sales_date between " + SQLUtil.convertForSQLDateOnly(this.getTermFrom()) + "\n" +
				"and " + SQLUtil.convertForSQLDateOnly(this.getTermTo()) + "\n" +
				"group by ds.shop_id, ds.slip_no, ds.sales_date, ds.customer_id, ds.visit_num\n" +
				") ds_sub\n" ;
                if(this.isRepeater())
                {
                       SQL +=   "where ds_sub.repeat_date between " + SQLUtil.convertForSQLDateOnly(this.getTotalFrom()) + "\n" +
                                "and " + SQLUtil.convertForSQLDateOnly(this.getTotalTo()) + "\n" ;
                }else
		{
                       SQL +=   "where  ds_sub.repeat_date is null \n" +
			        "or (ds_sub.repeat_date not between " + SQLUtil.convertForSQLDateOnly(this.getTotalFrom()) + "\n" +
                                "and  " + SQLUtil.convertForSQLDateOnly(this.getTotalTo()) + ") \n" ;	
		}
                SQL +=          ") ds\n" +
				"inner join mst_customer mc\n" +
				"on mc.customer_id = ds.customer_id\n" +
				"and mc.customer_no != '0'\n" +
				"left outer join (\n" +
				"select dsd.shop_id, dsd.slip_no,\n" +
				"sum(case dsd.product_division\n" +
				"when 0 then - dsd.discount_value\n" +
				"else dsd.product_value * dsd.product_num - dsd.discount_value\n" +
				"end) as sales_value\n" +
				"from data_sales_detail dsd\n" +
				"where dsd.delete_date is null\n" +
				"group by dsd.shop_id, dsd.slip_no\n" +
				") dsd\n" +
				"on dsd.shop_id = ds.shop_id\n" +
				"and dsd.slip_no = ds.slip_no\n" +
				"where ds.repeat_date is " + (this.isRepeater() ? "not " : "") + "null\n" +
				"order by ds.sales_date, mc.customer_id, ds.visit_num, ds.slip_no\n";
                        
                return SQL;
	}
	
	public void print()
	{
		HashMap<String, Object>		param	=	new HashMap<String, Object>();
		
		param.put("targetName", this.getTargetName());
		param.put("targetType", this.getTargetTypeName());
		param.put("targetTerm", this.getTermString());
		param.put("totalTerm", this.getTotalString());
		
		InputStream		report		=	HairRepeaterManager.class.getResourceAsStream(REPORT_PATH);
		String			fileName	=	REPORT_NAME + String.format("%1$tY%1$tm%1$td%2$ts",
				this.getTermFrom(), new java.util.Date());
		
		ReportManager.exportReport(report, fileName, ReportManager.PDF_FILE, param, this);
	}
	
	public ArrayList<MstCustomer> getSelectedCustomers(Integer optimizeType)
	{
		ArrayList<MstCustomer> customers	=	new ArrayList<MstCustomer>();
		
		for(HairRepeaterData rd : this)
		{
			if(!rd.isSelected())
			{
				continue;
			}
			
			boolean		isAdd	=	true;
			
			switch(optimizeType)
			{
				case 0:
				case 1:
					if(rd.getPCMailAddress().equals("") &&
							rd.getCellularMailAddress().equals(""))
					{
						isAdd	=	false;
					}
					break;
				case 2:
					if(rd.getPCMailAddress().equals(""))
					{
						isAdd	=	false;
					}
					break;
				case 3:
					if(rd.getCellularMailAddress().equals(""))
					{
						isAdd	=	false;
					}
					break;
				case 4:
					if(rd.getFullAddress().equals(""))
					{
						isAdd	=	false;
					}
					break;
			}
			
			if(isAdd)
			{
				MstCustomer	mc	=	new MstCustomer(rd);
				customers.add(mc);
			}
		}
		
		return	customers;
	}
}
