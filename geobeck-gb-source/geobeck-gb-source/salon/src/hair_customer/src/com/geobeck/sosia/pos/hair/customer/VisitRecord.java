/*
 * VisitRecord.java
 *
 * Created on 2007/03/20, 15:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.customer;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.hair.data.account.*;
import com.geobeck.sosia.pos.master.account.MstAccountSetting;

/**
 *
 * @author katagiri
 */
public class VisitRecord extends DataSales
{
	private	GregorianCalendar startTime = null;
	private	Long salesTotal = 0l;
	private	Long technicTotal = 0l;
	private	Long itemTotal = 0l;
        //nhanvt start edit
        private	Long courseTotal = 0l;
        private	Long comsumCourseTotal = 0l;
        //nhanvt start add  20150327 Bug #35748
        private MstAccountSetting msSetting = null;

        public MstAccountSetting getMsSetting() {
            return msSetting;
        }

        public void setMsSetting(MstAccountSetting msSetting) {
            this.msSetting = msSetting;
        }
        //nhanvt end add  20150327 Bug #35748

    public Long getComsumCourseTotal() {
        return comsumCourseTotal;
    }

    public void setComsumCourseTotal(Long comsumCourseTotal) {
        this.comsumCourseTotal = comsumCourseTotal;
    }
    public Long getCourseTotal() {
        return courseTotal;
    }

    public void setCourseTotal(Long courseTotal) {
        this.courseTotal = courseTotal;
    }
        //nhanvt end edit
	private Long technicClameTotal = 0l;
        private Long itemReturnedTotal = 0l;
	private java.util.Date nextReserveDate = null;
        
	/** Creates a new instance of VisitRecord */
	public VisitRecord(Integer type)
	{
		super(type);
	}

	public GregorianCalendar getStartTime()
	{
		return startTime;
	}

	public void setStartTime(GregorianCalendar startTime)
	{
		this.startTime = startTime;
	}

	public Long getSalesTotal()
	{
		return salesTotal;
	}

	public void setSalesTotal(Long salesTotal)
	{
		this.salesTotal = salesTotal;
	}

	public Long getTechnicTotal()
	{
		return technicTotal;
	}

	public void setTechnicTotal(Long technicTotal)
	{
		this.technicTotal = technicTotal;
	}

	public Long getItemTotal()
	{
		return itemTotal;
	}

	public void setItemTotal(Long itemTotal)
	{
		this.itemTotal = itemTotal;
	}
        
        public void setTechnicClameTotal(Long technicClameTotal){
            this.technicClameTotal = technicClameTotal;
        }
        
        public Long getTechnicClameTotal(){
            return this.technicClameTotal;
        }
	
        public void setItemReturnedTotal(Long itemReturnedTotal){
            this.itemReturnedTotal = itemReturnedTotal;
        }
        
        public Long getItemReturnedTotal(){
            return this.itemReturnedTotal;
        }
        
	public boolean loadAll(ConnectionWrapper con) throws SQLException
	{
		boolean	result = super.loadAll(con);
		this.setSalesTotal(super.getSalesTotal());
		return	result;
	}

	public boolean loadCustomerAll(ConnectionWrapper con) throws SQLException
	{
		boolean	result = super.loadCustomerAll(con);
		this.setSalesTotal(super.getSalesTotal());
		return	result;
	}
        //nhanvt start add  20150327 Bug #35748
        public boolean loadCustomerAll2(ConnectionWrapper con) throws SQLException
	{
                super.setAccountSetting(this.getMsSetting());
		boolean	result = super.loadCustomerAll(con);
		return	result;
	}
        //nhanvt end add  20150327 Bug #35748
        
	public void setNextReserveDate(java.util.Date nextReserveDate) {
            this.nextReserveDate = nextReserveDate;
	}

	public java.util.Date getNextReserveDate(){
            return this.nextReserveDate;
        }
	
	public void setVisitRecordData(ResultSetWrapper rs) throws SQLException
	{
		super.setData(rs);
		
		MstShop	ms	=	new MstShop();
		ms.setShopID(rs.getInt("shop_id"));
                ms.setShopName(rs.getString("shop_name"));
                //ms.load(SystemInfo.getConnection());
		this.setShop(ms);
		
		if(rs.getDate("start_time") != null)
		{
			GregorianCalendar	st	=	new GregorianCalendar();
			st.setTime(rs.getTimestamp("start_time"));
			this.setStartTime(st);
		}
		
		this.setSalesTotal(rs.getLong("sales_total"));
		this.setTechnicTotal(rs.getLong("technic_total"));
		this.setItemTotal(rs.getLong("item_total"));
                //nhanvt start add 
                this.setCourseTotal(rs.getLong("course_total"));
                this.setComsumCourseTotal(rs.getLong("consumption_value_total"));
                //nhanvt end add
                this.setTechnicClameTotal(rs.getLong("technic_clame_total"));
                this.setItemReturnedTotal(rs.getLong("item_returned_total"));
                this.setNextReserveDate(rs.getDate("nextReserveDate"));
	}
}
