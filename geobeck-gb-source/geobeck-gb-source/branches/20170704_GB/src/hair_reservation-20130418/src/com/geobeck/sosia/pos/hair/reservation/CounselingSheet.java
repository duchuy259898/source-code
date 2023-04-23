/*
 * CounselingSheet.java
 *
 * Created on 2007/11/13, 15:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.reservation;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.print.*;
import java.text.SimpleDateFormat;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;
import net.sf.jasperreports.engine.util.JRLoader;
import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.customer.*;
import com.geobeck.sosia.pos.hair.master.reservation.*;
import com.geobeck.sosia.pos.hair.data.reservation.*;
import com.geobeck.sosia.pos.hair.pointcard.PointData;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.report.util.*;
import com.geobeck.sosia.pos.account.PrintReceipt;

/**
 *
 * @author katagiri
 */
public class CounselingSheet extends ArrayList<CounselingSheetData>
{
	public static final int		INTRODUCED_CUSTOMER_MAX		=	9;
	public static final int		CUSTOMER_CHECK_NUM			=	5;
	/**
	 * カウンセリングシートレポートファイルパス
	 */
	private static final String		REPORT_PATH		=	"/reports/CounselingSheet.jasper";
	private static final String		SUB_REPORT_PATH		=	"/reports/LastProductList.jasper";
        
	private static final String		CUSTOMER_SHEET		=	"/reports/CustomerSheet.jasper";
	private static final String		SERVICE_SHEET		=	"/reports/ServiceSheet.jasper";
        
	/**
	 * カウンセリングシートレポート名
	 */
	private static final String		REPORT_NAME		=	"CounselingSheet";
	
	private	MstShop				shop					=	null;
	private java.util.Date			salesDate				=	null;
	private ArrayList<MstCounselingCheck>	counselingCheckList		=	null;
	
        private JRBeanCollectionDataSource lastTechnicList;
        
	/**
	 * Creates a new instance of CounselingSheet
	 */
	public CounselingSheet(MstShop shop)
	{
		super();
		this.setCounselingCheckList(new ArrayList<MstCounselingCheck>());
		this.setShop(shop);
		this.loadCounselingCheckList();
	}

	public MstShop getShop()
	{
		return shop;
	}

	public void setShop(MstShop shop)
	{
		this.shop = shop;
	}
	
	public Integer getShopID()
	{
		if(this.getShop() != null)
		{
			return	this.getShop().getShopID();
		}
		
		return	null;
	}

	public java.util.Date getSalesDate()
	{
		return salesDate;
	}

	public void setSalesDate(java.util.Date salesDate)
	{
		this.salesDate = salesDate;
	}

	public ArrayList<MstCounselingCheck> getCounselingCheckList()
	{
		return counselingCheckList;
	}

	public void setCounselingCheckList(ArrayList<MstCounselingCheck> counselingCheckList)
	{
		this.counselingCheckList = counselingCheckList;
	}
	
	public void addData(DataReservation dr)
	{
		CounselingSheetData		csd	=	new CounselingSheetData();
		
		csd.setData(dr.getCustomer());
		csd.setReservation(dr);
		
		this.add(csd);
	}
	
	private void loadCounselingCheckList()
	{
		this.getCounselingCheckList().clear();
		
		try
		{
                    ConnectionWrapper con = SystemInfo.getConnection();
                    
                    ResultSetWrapper rs = con.executeQuery(this.getLoadCounselingCheckListSQL());

                    while(rs.next())
                    {
                        MstCounselingCheck mcc = new MstCounselingCheck();
                        mcc.setData(rs);
                        this.getCounselingCheckList().add(mcc);
                    }

                    rs.close();
		}
		catch(SQLException e)
		{
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	private String getLoadCounselingCheckListSQL()
	{
		return	"select *\n" +
				"from mst_counseling_check\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"order by check_id\n";
	}
	
	public boolean load()
	{
            try
            {
                ConnectionWrapper con = SystemInfo.getConnection();
                
                
                for(CounselingSheetData csd : this)
                {
                    ResultSetWrapper rs = con.executeQuery(this.getLoadSQL(csd.getCustomerID()));

                    if(rs.next())
                    {
                        csd.setData(rs);
                    }

                    rs.close();

                    if(!csd.loadLastSales(con,SystemInfo.getTypeID(), this.getSalesDate()))
                    {
                        return false;
                    }
                }
            }
            catch(SQLException e)
            {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            return true;
	}
	
	private String getLoadSQL(Integer customerID)
	{
		return	"select mc.*, mj.job_name\n" +
				"from mst_customer mc\n" +
				"left outer join mst_job mj\n" +
				"on mj.job_id = mc.job_id\n" +
				"and mj.delete_date is null\n" +
				"where mc.customer_id = " + SQLUtil.convertForSQL(customerID) + "\n" +
				"order by customer_no, customer_id\n";
	}
	
        /*********************************************/
        // カウンセリングシート
        /*********************************************/
	public boolean printCounselingSheet()
	{
            HashMap<String, Object> param = new HashMap<String, Object>();

            param.put("salesDate", this.getSalesDate());
            param.put("shopName", this.getShop().getShopName());

            try
            {
                ConnectionWrapper con = SystemInfo.getConnection();

                InputStream subReport = CounselingSheet.class.getResourceAsStream(SUB_REPORT_PATH);
                JasperReport subProductReport = (JasperReport)JRLoader.loadObject(subReport);
                param.put("lastProductReport", subProductReport);

                for(Integer i = 0; i < CounselingSheet.CUSTOMER_CHECK_NUM; i ++)
                {
                    if(i < this.getCounselingCheckList().size())
                    {
                        MstCounselingCheck mcc = this.getCounselingCheckList().get(i);
                        param.put("checkContent" + i.toString(), mcc.getCheckContent());
                    }
                    else
                    {
                        param.put("checkContent" + i.toString(), "");
                    }
                }

                InputStream report = CounselingSheet.class.getResourceAsStream(REPORT_PATH);
                CounselingSheetData csd = this.get(0);
                MstCustomer introducer = new MstCustomer(csd.getIntroducerID());
                introducer.load(con);
                csd.setIntroducer(introducer);

                //顧客共有の設定でショップIDを変更
                Integer shopID = (SystemInfo.getSetteing().isShareCustomer() ? 0 : SystemInfo.getCurrentShop().getShopID());                    
                csd.setIntroducedCustomers(MstCustomer.getIntroduceList(con, csd.getCustomerID(), shopID));

                // 担当者
                param.put("fullStaffName", "担当\r\n" + csd.getReservation().getStaff().getFullStaffName());

                try {
                    ResultSetWrapper rs = SystemInfo.getConnection().executeQuery("select * from mst_receipt_setting where shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
                    if (rs.next()) {
                        param.put("counselingMenu1", rs.getString("counseling_menu1"));
                        param.put("counselingMenu2", rs.getString("counseling_menu2"));
                        param.put("counselingMenu3", rs.getString("counseling_menu3"));
                        param.put("counselingMenu4", rs.getString("counseling_menu4"));
                    } else {
                        param.put("counselingMenu1", "カット");
                        param.put("counselingMenu2", "パーマ");
                        param.put("counselingMenu3", "カラー");
                        param.put("counselingMenu4", "トリートメント");
                    }
                    rs.close();
                } catch(SQLException e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                }

                ReportManager.exportReport(report, PrintServiceLookup.lookupDefaultPrintService(),3, param, this);
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return false;
            }

            return true;
	}
        
        class SubLastTechnic{
            private String productName;
            
            public SubLastTechnic(String s){
                productName = s;
            }
            
            public String getProductName(){
                return productName;
            }
        }
        
        public JRBeanCollectionDataSource getLastTechnicList(){
            return lastTechnicList;
        }
        
        public void setLastTechnicList(JRBeanCollectionDataSource bean ){
            lastTechnicList = bean;
        }

        /*********************************************/
        // 接客シート
        /*********************************************/
	public boolean printServiceSheet()
	{
            HashMap<String, Object> param = new HashMap<String, Object>();

            param.put("customerNo", this.get(0).getCustomerNo());
            param.put("customerName", this.get(0).getFullCustomerName());
            param.put("customerKana", this.get(0).getFullCustomerKana());
            
            String staffName = this.get(0).getReservation().getStaff().getFullStaffName();
            if (this.get(0).getReservation().getDesignated()) {
                staffName += "（指名）";
            }
            param.put("chargeStaffName", staffName);

            if (SystemInfo.isUsePointcard()) {
                param.put("totalPoint", PointData.getNowPoint(this.get(0).getCustomerID()));
            }

            try
            {
                ConnectionWrapper con = SystemInfo.getConnection();
                ResultSetWrapper rs = null;
                int idx = 0;
                
                rs = con.executeQuery(this.getServiceSheetSQL1());
                idx = 1;
                while(rs.next())
                {
                    param.put("salesDate" + idx, rs.getDate("sales_date"));
                    param.put("techItemName" + idx, rs.getString("product_name"));
                    param.put("salesValue" + idx, rs.getLong("sales_value"));
                    param.put("staffName" + idx, rs.getString("staff_name"));
                    idx++;
                }
                rs.close();
                
                rs = con.executeQuery(this.getServiceSheetSQL2());
                idx = 7;
                while(rs.next())
                {
                    param.put("salesDate" + idx, rs.getDate("sales_date"));
                    param.put("techItemName" + idx, rs.getString("product_name"));
                    param.put("salesValue" + idx, rs.getLong("sales_value"));
                    param.put("staffName" + idx, rs.getString("staff_name"));
                    idx++;
                }
                rs.close();
                
                rs = con.executeQuery(this.getServiceSheetSQL3());
                idx = 1;
                while(rs.next())
                {
                    if (idx == 1) {
                        param.put("reservationDateTime", rs.getDate("reservation_datetime"));
                        param.put("visitTime", rs.getString("visit_time"));
                    }
                    
                    param.put("techName" + idx, rs.getString("technic_name"));
                    param.put("price" + idx, rs.getLong("price"));
                    param.put("reservationStaffName" + idx, rs.getString("reservation_staff_name"));
                    idx++;
                }
                rs.close();
                
            }
            catch(SQLException e)
            {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
            
            
            
            try
            {
		PrintReceipt pr = new PrintReceipt();
                //プリンタ情報が存在しない場合は処理を抜ける
		if (!pr.canPrint()) return false;
                
                InputStream report = CounselingSheet.class.getResourceAsStream(SERVICE_SHEET);
                ReportManager.exportReport(report, pr.getPrinter(),3, param, this);
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return false;
            }

            return true;
	}
        
        /*********************************************/
        // お客様シート
        /*********************************************/
	public boolean printCustomerSheet()
	{
            HashMap<String, Object> param = new HashMap<String, Object>();

            param.put("salesDate", this.getSalesDate());
            param.put("reservationNo", this.get(0).getReservationNo());
            
            try
            {
		PrintReceipt pr = new PrintReceipt();
                //プリンタ情報が存在しない場合は処理を抜ける
		if (!pr.canPrint()) return false;
                
                InputStream report = CounselingSheet.class.getResourceAsStream(CUSTOMER_SHEET);
                ReportManager.exportReport(report, pr.getPrinter(),3, param, this);
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return false;
            }

            return true;
	}

	private String getServiceSheetSQL1()
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      sales_date");
            sql.append("     ,(select technic_name from mst_technic where technic_id = a.product_id) as product_name");
            sql.append("     ,account_setting_value as sales_value");
            sql.append("     ,case when detail_designated_flag then '指) ' else '' end");
            sql.append("         || (select staff_name1 from mst_staff where staff_id = a.detail_staff_id) as staff_name");
            sql.append(" from");
            sql.append("     view_data_sales_detail_valid a");
            sql.append(" where");
            sql.append("         shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
            sql.append("     and customer_id = " + SQLUtil.convertForSQL(this.get(0).getCustomerID()));
            sql.append("     and product_division in (1,3)");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            sql.append("     and sales_date <= '" + sdf.format(this.getSalesDate()) + " 23:59:59'");
            
            sql.append(" order by");
            sql.append("      sales_date desc");
            sql.append("     ,slip_no");
            sql.append("     ,slip_detail_no");
            sql.append(" limit 6");
            
            return sql.toString();
	}

	private String getServiceSheetSQL2()
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      sales_date");
            sql.append("     ,(select item_name from mst_item where item_id = a.product_id) as product_name");
            sql.append("     ,account_setting_value as sales_value");
            sql.append("     ,case when detail_designated_flag then '指) ' else '' end");
            sql.append("         || (select staff_name1 from mst_staff where staff_id = a.detail_staff_id) as staff_name");
            sql.append(" from");
            sql.append("     view_data_sales_detail_valid a");
            sql.append(" where");
            sql.append("         shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
            sql.append("     and customer_id = " + SQLUtil.convertForSQL(this.get(0).getCustomerID()));
            sql.append("     and product_division in (2,4)");
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            sql.append("     and sales_date <= '" + sdf.format(this.getSalesDate()) + " 23:59:59'");
            
            sql.append(" order by");
            sql.append("      sales_date desc");
            sql.append("     ,slip_no");
            sql.append("     ,slip_detail_no");
            sql.append(" limit 3");

            return sql.toString();
	}

	private String getServiceSheetSQL3()
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      b.reservation_datetime");
            sql.append("     ,to_char(a.visit_time, 'hh24:mi') as visit_time");
            sql.append("     ,c.technic_name");
            sql.append("     ,c.price");
            sql.append("     ,case when b.designated_flag then '指) ' else '' end");
            sql.append("         || (select staff_name1 from mst_staff where staff_id = b.staff_id) as reservation_staff_name");
            sql.append(" from");
            sql.append("     data_reservation a");
            sql.append("         join data_reservation_detail b");
            sql.append("             using (shop_id, reservation_no)");
            sql.append("         join mst_technic c");
            sql.append("             using (technic_id)");
            sql.append(" where");
            sql.append("         a.delete_date is null");
            sql.append("     and b.delete_date is null");
            sql.append("     and a.shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
            sql.append("     and a.reservation_no = " + SQLUtil.convertForSQL(this.get(0).getReservationNo()));
            sql.append(" order by");
            sql.append("     b.reservation_detail_no");
            sql.append(" limit 6");

            return sql.toString();
	}
        
}
