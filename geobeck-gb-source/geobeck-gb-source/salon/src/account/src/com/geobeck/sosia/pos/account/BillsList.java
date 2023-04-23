/*
 * BillsList.java
 *
 * Created on 2006/05/09, 9:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.account;

import com.geobeck.sosia.pos.hair.report.util.JExcelApi;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.report.util.ReportManager;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.Level;
/**
 * îÑä|ã‡àÍóóèàóù
 * @author katagiri
 */
public class BillsList extends ArrayList<Bill>
{
	private static final String REPORT_PATH = "/report/BillsList.jasper";
        private static final String REPORT_PATH1 = "/report/BillsList1.jasper";
	private static final String REPORT_NAME	= "BillsList";
        private static final String REPORT_NAME1	= "BillsList1";
	
	private	ArrayList<CollectedBillData> collectedBills = null;
	
	private	GregorianCalendar termFrom = null;
	private	GregorianCalendar termTo   = null;
        //nhanvt start add  20150313 Bug #35485
        private Integer slipNo = null;
        //IVS_LVTu start edit 2015/07/29 New request #41101
        private static Integer shop_id = null;
        private MstShop mstShop = null;
        
        //nhanvt end add  20150313 Bug #35485
        //IVS_LVTu start add 2015/10/23 New request #43755
        private static final Integer SALES_DATE     = 1;
        private static final Integer PAYMENT_DATE   = 2;
        private static Integer condition = -1;

        public static Integer getCondition() {
            return condition;
        }

        public void setCondition(Integer condition) {
            this.condition = condition;
        }
        //IVS_LVTu end add 2015/10/23 New request #43755

        public MstShop getMstShop() {
            return mstShop;
        }

        public void setMstShop(MstShop mstShop) {
            this.mstShop = mstShop;
        }

        //IVS_LVTu end edit 2015/07/29 New request #41101
        public Integer getSlipNo() {
            return slipNo;
        }

        public void setSlipNo(Integer slipNo) {
            this.slipNo = slipNo;
        }

        //IVS_LVTu start edit 2015/07/29 New request #41101
        public static Integer getShop_id() {
            return shop_id;
        }
        //IVS_LVTu end edit 2015/07/29 New request #41101

        public void setShop_id(Integer shop_id) {
            this.shop_id = shop_id;
        }
	
	/** Creates a new instance of BillsList */
	public BillsList()
	{
	    collectedBills = new ArrayList<CollectedBillData>();
	}

	public ArrayList<CollectedBillData> getCollectedBills()
	{
	    return collectedBills;
	}

	public void setCollectedBills(ArrayList<CollectedBillData> collectedBills)
	{
	    this.collectedBills = collectedBills;
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
	
	private String getTerm()
	{
	    if(this.getTermFrom() == null && this.getTermTo() == null) {
		return	"";
	    }
		
	    String term = "";
		
	    if(this.getTermFrom() != null) {
		term += String.format("%1$tYîN%1$tmåé%1$tdì˙", this.getTermFrom());
	    }
		
	    term += "Å@Å`Å@";
		
	    if(this.getTermTo() != null) {
		term += String.format("%1$tYîN%1$tmåé%1$tdì˙", this.getTermTo());
	    }
		
	    return term;
	}
	
	/**
	 * ÉfÅ[É^Çì«Ç›çûÇﬁÅB
	 * @param customerID å⁄ãqÇhÇc
	 */
	public void load(Integer customerID)
	{
	    try
	    {
		this.getBillsList(customerID);
		this.getCollectedBills(customerID);
	    }
	    catch(SQLException e)
	    {
		SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
	    }
	}
        
        //nhanvt start add  20150313 Bug #35485
        public void load2(Integer customerID)
	{
	    try
	    {
		this.getBillsList(customerID);
		this.getCollectedBills2(customerID);
	    }
	    catch(SQLException e)
	    {
		SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
	    }
	}
        //nhanvt end add  20150313 Bug #35485
	
	/**
	 * îÑä|ã‡ÇÃÉäÉXÉgÇéÊìæÇ∑ÇÈÅB
	 * @param customerID å⁄ãqÇhÇc
	 */
	private void getBillsList(Integer customerID) throws SQLException
	{
	    this.clear();
		
	    ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(this.getSelectSQL_DateSpan(customerID));

	    while(rs.next()) {
		Bill temp = new Bill();
		temp.setData(rs);
		this.add(temp);
	    }
	
	    rs.close();
	}
	
	/**
	 * îÑä|ã‡ÇÃÉäÉXÉgÇéÊìæÇ∑ÇÈÅB
	 * @param customerID å⁄ãqÇhÇc
	 */
	private void getCollectedBills(Integer customerID) throws SQLException
	{
	    collectedBills.clear();
		
	    ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(
				this.getCollectedBillsSQL(customerID, this.getTermFrom(), this.getTermTo()));

	    while(rs.next()) {
		CollectedBillData temp = new CollectedBillData();
                //IVS_LVTu start add 2015/10/23 New request #43755
                temp.setFlagScreenBill(true);
                //IVS_LVTu end add 2015/10/23 New request #43755
		temp.setData(rs);
		collectedBills.add(temp);
	    }
	    
	    rs.close();
	}
        
        //nhanvt start add  20150313 Bug #35485
        private void getCollectedBills2(Integer customerID) throws SQLException
	{
	    collectedBills.clear();
		
	    ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(
				this.getCollectedBillsSQL2(customerID, this.getTermFrom(), this.getTermTo()));

	    while(rs.next()) {
		CollectedBillData temp = new CollectedBillData();
		temp.setData(rs);
		collectedBills.add(temp);
	    }
	    
	    rs.close();
	}
        //nhanvt end add  20150313 Bug #35485

	/**
	 * îÑä|ã‡ÇÃÉäÉXÉgéÊìæópÇÃÇrÇpÇkï∂ÇéÊìæÇ∑ÇÈÅB
	 * @param customerID å⁄ãqÇhÇc
	 * @return îÑä|ã‡ÇÃÉäÉXÉgéÊìæópÇÃÇrÇpÇkï∂
	 */
	public String getSelectSQL_DateSpan(Integer customerID)
	{
	    return BillsList.getSelectSQL(customerID, this.getTermFrom(), this.getTermTo());
	}
	
	/**
	 * îÑä|ã‡ÇÃÉäÉXÉgéÊìæópÇÃÇrÇpÇkï∂ÇéÊìæÇ∑ÇÈÅB
	 * @param customerID å⁄ãqÇhÇc
	 * @return îÑä|ã‡ÇÃÉäÉXÉgéÊìæópÇÃÇrÇpÇkï∂
	 */
	public static String getSelectSQL(Integer customerID)
	{
	    return	BillsList.getSelectSQL(customerID, null, null);
	}
	
	public static String getSelectSQL(Integer customerID,
			GregorianCalendar dateFrom,
			GregorianCalendar dateTo)
	{
	    
            StringBuilder paymentSql = new StringBuilder(1000);
	    paymentSql.append(" (");
	    paymentSql.append("     select");
	    paymentSql.append("         bill_value");
	    paymentSql.append("     from");
	    paymentSql.append("         data_payment");
	    paymentSql.append("     where");
	    paymentSql.append("             shop_id = ds.shop_id");
	    paymentSql.append("         and slip_no = ds.slip_no");
            //nhanvt start add 20141104 Request #31293
            paymentSql.append("         and delete_date is null ");
            //nhanvt end add 20141104 Request #31293
	    paymentSql.append("     order by");
	    paymentSql.append("         payment_no desc");
	    paymentSql.append("     limit 1");
	    paymentSql.append(" )");

            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      ds.sales_date");
            sql.append("     ,ds.slip_no");
            sql.append("     ,ds.customer_id");
            sql.append("     ,mc.customer_no");
            sql.append("     ,mc.customer_name1");
            sql.append("     ,mc.customer_name2");
            sql.append("     ,dp.staff_id");
            sql.append("     ,ms.staff_no");
            sql.append("     ,ms.staff_name1");
            sql.append("     ,ms.staff_name2");
            sql.append("     ,dp.bill_value");
            //nhanvt start edit 20141110 New request #31293
            sql.append(" ,( dp.bill_value - coalesce(( ");
            sql.append(" 		SELECT  sum(b.payment_value) as payment_value");
            sql.append(" 		FROM data_payment a");
            sql.append(" 		inner join data_payment_detail b on a.shop_id = b.shop_id and a.slip_no = b.slip_no and a.payment_no = b.payment_no and b.payment_no !=0 and b.delete_date is null");
            sql.append(" 		 WHERE a.shop_id = ds.shop_id");
            sql.append(" 		 AND a.slip_no = ds.slip_no");
            sql.append("                 and a.delete_date is null ");
            sql.append(" 		),0)) as bill_value_rest ");
            //sql.append("     ," + paymentSql1.toString() + " as bill_value_rest");
            //nhanvt end edit 20141110 New request #31293
            sql.append(" from");
            sql.append("     data_sales ds");
            //IVS_LVTu start edit 2015/10/23 New request #43755
            //sql.append("         left join mst_customer mc");
            sql.append("         inner join mst_customer mc");
            sql.append("                on mc.customer_id = ds.customer_id");
            sql.append("         left join data_payment dp");
            sql.append("                on dp.shop_id = ds.shop_id");
            sql.append("               and dp.slip_no = ds.slip_no");
            sql.append("               and dp.payment_no = 0");
            //nhanvt start add 20141104 Request #31293
            sql.append("               and dp.delete_date is null ");
            //nhanvt end add 20141104 Request #31293
            sql.append("         left join mst_staff ms");
            sql.append("                on ms.staff_id = dp.staff_id");
            sql.append(" where");
            sql.append("         ds.delete_date is null");
            //IVS_LVTu start edit 2015/07/29 New request #41101
            if ( getShop_id() == null ) {
                sql.append("     and ds.shop_id = " + SQLUtil.convertForSQL(SystemInfo.getCurrentShop().getShopID()));
            } else {
                sql.append("     and ds.shop_id = " + SQLUtil.convertForSQL(getShop_id()));
            }
            
            //IVS_LVTu end edit 2015/07/29 New request #41101
            if (dateFrom != null) {
            sql.append(" and ds.sales_date >= " + SQLUtil.convertForSQLDateOnly(dateFrom));
            }

            if (dateTo != null) {
                sql.append(" and ds.sales_date <= " + SQLUtil.convertForSQLDateOnly(dateTo));
            }
            //IVS_LVTu end edit 2015/10/23 New request #43755
            
            //nhanvt start edit 20141110 New request #31293
            sql.append("     and ((" + paymentSql.toString() + " > 0 )  ");
            sql.append("or(");
            sql.append("    (dp.bill_value -");
            sql.append("      coalesce((SELECT sum(b.payment_value) AS payment_value");
            sql.append("       FROM data_payment a");
            sql.append("       INNER JOIN data_payment_detail b ON a.shop_id = b.shop_id");
            sql.append("       AND a.slip_no = b.slip_no");
            sql.append("       AND a.payment_no = b.payment_no");
            sql.append("       AND b.payment_no != 0 ");
            sql.append("       AND b.delete_date IS NULL");
            sql.append("       WHERE a.shop_id = ds.shop_id");
            sql.append("         AND a.slip_no = ds.slip_no");
            sql.append("         and a.payment_no >0");
            sql.append("         AND a.delete_date IS NULL),0)");
            sql.append("     ) >0");
            sql.append(")");
            sql.append(")");
            //nhanvt end edit 20141110 New request #31293
            
            if (customerID != null) {
                sql.append(" and ds.customer_id = " + SQLUtil.convertForSQL(customerID));
            }
            
            sql.append(" order by");
            sql.append("      ds.sales_date");
            sql.append("     ,ds.slip_no");

            return sql.toString();
            
	}
	
	private String getCollectedBillsSQL(Integer customerID,
			GregorianCalendar dateFrom,
			GregorianCalendar dateTo)
	{
                StringBuilder sql = new StringBuilder(1000);
                sql.append(" select");
                sql.append("      ds.shop_id");
                sql.append("     ,ds.slip_no");
                sql.append("     ,ds.sales_date");
                sql.append("     ,dp.payment_no");
                sql.append("     ,dp.payment_date");
                sql.append("     ,mc.customer_id");
                sql.append("     ,mc.customer_no");
                sql.append("     ,mc.customer_name1");
                sql.append("     ,mc.customer_name2");
                sql.append("     ,dp.staff_id");
                sql.append("     ,ms.staff_no");
                sql.append("     ,ms.staff_name1");
                sql.append("     ,ms.staff_name2");
                sql.append("     ,coalesce(dpd.payment_total, 0) as collected_value");
                //Luc start edit 20140714
                //sql.append("     ,0 as change_value");
                sql.append("     ,dp.change_value as change_value");
                //Luc end edit 20140714
                //nhanvt start edit 20141110 New request #31293
                //sql.append("     ,dp.bill_value");
                sql.append(",(");
                sql.append("       (select t.bill_value from data_payment t where t.shop_id = ds.shop_id");
                sql.append("            AND t.slip_no = ds.slip_no");
                sql.append("            AND t.delete_date IS NULL");
                sql.append("            AND t.payment_no = 0 ");
                sql.append("       ) ");
                sql.append("       -");
                sql.append("       (select sum(dpd.payment_value)");
                sql.append("         from data_payment dp1");
                sql.append("        inner join data_payment_detail dpd on dp1.shop_id = dpd.shop_id and dp1.slip_no = dpd.slip_no and dp1.payment_no= dpd.payment_no and dpd.payment_no !=0 ");
                sql.append("        where dp1.slip_no = ds.slip_no and dp1.delete_date is null and dp1.shop_id = ds.shop_id and dp1.payment_no <= dp.payment_no  ");
                sql.append("       and dp1.delete_date is null and dpd.delete_date is null");

                sql.append("        ) ");
                sql.append(") as bill_value ");
                //nhanvt end edit 20141110 New request #31293
                //IVS_LVTu start edit 2015/10/23 New request #43755
                sql.append("       ,coalesce(dpd.cash_value, 0) AS cash_value ");
                sql.append("       ,coalesce(dpd.card_value, 0) AS card_value ");
                sql.append("       ,coalesce(dpd.ecash_value, 0) AS ecash_value ");
                sql.append("       ,coalesce(dpd.vouchers_value, 0) AS vouchers_value ");
                sql.append(" from");
                sql.append("     data_sales ds");
                //sql.append("         left join mst_customer mc");
                sql.append("         inner join mst_customer mc");
                sql.append("                on mc.customer_id = ds.customer_id");
                sql.append("         inner join data_payment dp");
                sql.append("                 on dp.shop_id = ds.shop_id");
                sql.append("                and dp.slip_no = ds.slip_no");
                //nhanvt start add 20141104 Request #31293
                sql.append("                and dp.delete_date is null ");
                //nhanvt end add 20141104 Request #31293
                sql.append("                and dp.payment_no > 0");
                sql.append("         left join");
                sql.append("             (");
                sql.append("                 select");
                sql.append("                      dpd.shop_id");
                sql.append("                     ,dpd.slip_no");
                sql.append("                     ,dpd.payment_no");
                sql.append("                     ,sum(payment_value) as payment_total");
                sql.append("                    ,sum(case when mpc.payment_class_id = 1 then coalesce(dpd.payment_value, 0) else 0 end) as cash_value ");
                sql.append("                   ,sum(case when mpc.payment_class_id = 2 then coalesce(dpd.payment_value, 0) else 0 end) as card_value ");
                sql.append("                   ,sum(case when mpc.payment_class_id = 3 then coalesce(dpd.payment_value, 0) else 0 end) as ecash_value ");
                sql.append("                   ,sum(case when mpc.payment_class_id = 4 then coalesce(dpd.payment_value, 0) else 0 end) as vouchers_value ");
                sql.append("                 from");
                sql.append("                     data_payment_detail dpd");
                sql.append("            	inner join mst_payment_method mpm using(payment_method_id) ");
                sql.append("                    inner join mst_payment_class mpc using(payment_class_id) ");
                sql.append("                 where");
                sql.append("                     dpd.delete_date is null");
                sql.append("                 group by");
                sql.append("                      dpd.shop_id");
                sql.append("                     ,dpd.slip_no");
                sql.append("                     ,dpd.payment_no");
                sql.append("             ) dpd");
                sql.append("              on dpd.shop_id = dp.shop_id");
                sql.append("             and dpd.slip_no = dp.slip_no");
                sql.append("             and dpd.payment_no = dp.payment_no");
                sql.append("         left join mst_staff ms");
                sql.append("                on ms.staff_id = dp.staff_id");
                sql.append(" where");
                sql.append("         ds.delete_date is null");
                //IVS_LVTu start edit 2015/07/29 New request #41101
                if ( getShop_id() == null ) {
                    sql.append("     and ds.shop_id = " + SQLUtil.convertForSQL(SystemInfo.getCurrentShop().getShopID()));
                } else {
                    sql.append("     and ds.shop_id = " + SQLUtil.convertForSQL(getShop_id()));
                }
                //IVS_LVTu end edit 2015/07/29 New request #41101
                
                if ( getCondition() == SALES_DATE) {
                    if (dateFrom != null) {
                        sql.append(" and ds.sales_date >= " + SQLUtil.convertForSQLDateOnly(dateFrom));
                    }

                    if (dateTo != null) {
                        sql.append(" and ds.sales_date <= " + SQLUtil.convertForSQLDateOnly(dateTo));
                    }
                } else if (getCondition() == PAYMENT_DATE){
                    if (dateFrom != null) {
                        sql.append(" and dp.payment_date >= " + SQLUtil.convertForSQLDateOnly(dateFrom));
                    }

                    if (dateTo != null) {
                        sql.append(" and dp.payment_date <= " + SQLUtil.convertForSQLDateOnly(dateTo));
                    }
                }
                //IVS_LVTu end edit 2015/10/23 New request #43755
                
                if (customerID != null) {
                    sql.append(" and ds.customer_id = " + SQLUtil.convertForSQL(customerID));
                }
                
                sql.append(" order by");
                sql.append("      ds.sales_date");
                sql.append("     ,ds.slip_no");
                sql.append("     ,dp.payment_no");

                return sql.toString();
	}
        
        //nhanvt start add  20150313 Bug #35485
        private String getCollectedBillsSQL2(Integer customerID,
			GregorianCalendar dateFrom,
			GregorianCalendar dateTo)
	{
                StringBuilder sql = new StringBuilder(1000);
                sql.append(" select");
                sql.append("      ds.shop_id");
                sql.append("     ,ds.slip_no");
                sql.append("     ,ds.sales_date");
                sql.append("     ,dp.payment_no");
                sql.append("     ,dp.payment_date");
                sql.append("     ,mc.customer_id");
                sql.append("     ,mc.customer_no");
                sql.append("     ,mc.customer_name1");
                sql.append("     ,mc.customer_name2");
                sql.append("     ,dp.staff_id");
                sql.append("     ,ms.staff_no");
                sql.append("     ,ms.staff_name1");
                sql.append("     ,ms.staff_name2");
                sql.append("     ,coalesce(dpd.payment_total, 0) as collected_value");
                sql.append("     ,dp.change_value as change_value");
                sql.append(",(");
                sql.append("       (select t.bill_value from data_payment t where t.shop_id = ds.shop_id");
                sql.append("            AND t.slip_no = ds.slip_no");
                sql.append("            AND t.delete_date IS NULL");
                sql.append("            AND t.payment_no = 0 ");
                sql.append("       ) ");
                sql.append("       -");
                sql.append("       (select sum(dpd.payment_value)");
                sql.append("         from data_payment dp1");
                sql.append("        inner join data_payment_detail dpd on dp1.shop_id = dpd.shop_id and dp1.slip_no = dpd.slip_no and dp1.payment_no= dpd.payment_no and dpd.payment_no !=0 ");
                sql.append("        where dp1.slip_no = ds.slip_no and dp1.delete_date is null and dp1.shop_id = ds.shop_id and dp1.payment_no <= dp.payment_no  ");
                sql.append("       and dp1.delete_date is null and dpd.delete_date is null");

                sql.append("        ) ");
                sql.append(") as bill_value ");
                sql.append(" from");
                sql.append("     data_sales ds");
                sql.append("         left join mst_customer mc");
                sql.append("                on mc.customer_id = ds.customer_id");
                sql.append("         inner join data_payment dp");
                sql.append("                 on dp.shop_id = ds.shop_id");
                sql.append("                and dp.slip_no = ds.slip_no");
                sql.append("                and dp.delete_date is null ");
                sql.append("                and dp.payment_no > 0");
                sql.append("         left join");
                sql.append("             (");
                sql.append("                 select");
                sql.append("                      dpd.shop_id");
                sql.append("                     ,dpd.slip_no");
                sql.append("                     ,dpd.payment_no");
                sql.append("                     ,sum(payment_value) as payment_total");
                sql.append("                 from");
                sql.append("                     data_payment_detail dpd");
                sql.append("                 where");
                sql.append("                     dpd.delete_date is null");
                sql.append("                 group by");
                sql.append("                      dpd.shop_id");
                sql.append("                     ,dpd.slip_no");
                sql.append("                     ,dpd.payment_no");
                sql.append("             ) dpd");
                sql.append("              on dpd.shop_id = dp.shop_id");
                sql.append("             and dpd.slip_no = dp.slip_no");
                sql.append("             and dpd.payment_no = dp.payment_no");
                sql.append("         left join mst_staff ms");
                sql.append("                on ms.staff_id = dp.staff_id");
                sql.append(" where");
                sql.append("         ds.delete_date is null");
                //IVS_LVTu start edit 2015/07/29 New request #41101
                if ( getShop_id() == null ) {
                    sql.append("     and ds.shop_id = " + SQLUtil.convertForSQL(SystemInfo.getCurrentShop().getShopID()));
                } else {
                    sql.append("     and ds.shop_id = " + SQLUtil.convertForSQL(getShop_id()));
                }
                //IVS_LVTu end edit 2015/07/29 New request #41101
                if(this.getSlipNo() != null){
                    sql.append(" and ds.slip_no = " + this.getSlipNo());
                }
                if (dateFrom != null) {
                    sql.append(" and ds.sales_date >= " + SQLUtil.convertForSQLDateOnly(dateFrom));
                }
                
                if (dateTo != null) {
                    sql.append(" and ds.sales_date <= " + SQLUtil.convertForSQLDateOnly(dateTo));
                }
                
                if (customerID != null) {
                    sql.append(" and ds.customer_id = " + SQLUtil.convertForSQL(customerID));
                }
                
                sql.append(" order by");
                sql.append("      ds.sales_date");
                sql.append("     ,ds.slip_no");
                sql.append("     ,dp.payment_no");

                return sql.toString();
	}
        //nhanvt end add  20150313 Bug #35485
	
	public boolean print()
	{
	    HashMap<String, Object> param = new HashMap<String, Object>();
             SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	   PrintReceipt pr = new PrintReceipt();
           
            
            param.put("targetName", "ñ¢âÒé˚àÍóó");
	    
            if ( this.getTermFrom() != null && this.getTermTo() != null) {
		param.put("date",sdf.format( this.getTermFrom().getTime()) +" Å` "+ sdf.format(this.getTermTo().getTime()));
            }else {
                param.put("date","ä˙ä‘éwíËÇ»Çµ");
            }
	    InputStream report = BillsList.class.getResourceAsStream(REPORT_PATH);
	    
	    String fileName = REPORT_NAME + String.format("%1$tY%1$tm%1$td%1$ts", new java.util.Date());
            ReportManager.exportReport(report, pr.getPrinter(),3, param, this);
            return	true;
	}
        
        public boolean print1( )
	{
            PrintReceipt pr = new PrintReceipt();
	    HashMap<String, Object> param = new HashMap<String, Object>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	    param.put("targetName", "âÒé˚çœàÍóó");
            if ( this.getTermFrom() != null && this.getTermTo() != null) {
                param.put("date",sdf.format( this.getTermFrom().getTime()) +" Å` "+ sdf.format(this.getTermTo().getTime()));
            }else {
                param.put("date","ä˙ä‘éwíËÇ»Çµ");
            }
	 
            InputStream report = BillsList.class.getResourceAsStream(REPORT_PATH1);
	    
	    String fileName = REPORT_NAME1 + String.format("%1$tY%1$tm%1$td%1$ts", new java.util.Date());
        
          
                ReportManager.exportReport(report, pr.getPrinter(), 3, param, this.collectedBills);
           
	    return	true;
	}
        
        public void printExcel1()
        {
            JExcelApi jx = new JExcelApi("îÑä|âÒé˚-ñ¢âÒé˚àÍóó");
            jx.setTemplateFile("/reports/îÑä|âÒé˚-ñ¢âÒé˚àÍóó.xls");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            //IVS_LVTu start edit 2015/07/29 New request #41101
            //jx.setValue(4, 2,"ä˙ä‘ " +  sdf.format( this.getTermFrom().getTime()) +" Å` "+ sdf.format(this.getTermTo().getTime())); 
            jx.setValue(2, 2,this.getMstShop().getShopName()); 
            if ( this.getTermFrom() != null && this.getTermTo() != null) {
                jx.setValue(2, 3,sdf.format( this.getTermFrom().getTime()) +" Å` "+ sdf.format(this.getTermTo().getTime()));
            }else {
                jx.setValue(2, 3,"ä˙ä‘éwíËÇ»Çµ");
            }
            
            //int row = 5;
            int row = 6;
            // í«â¡çsêîÉZÉbÉg
            jx.insertRow(row, this.size() - 1);
            
            for(Bill bl : this)
            {
//                jx.setValue(2, row, bl.getSalesDate()); 
//                jx.setValue(3, row, bl.getSlipNo()); 
//                jx.setValue(4, row, bl.getCustomerNo()); 
//                jx.setValue(5, row, bl.getCustomerName()); 
//                jx.setValue(6, row, bl.getStaffName()); 
                // IVS SANG START EDIT 20131113 Bug #15850: [gb]ÉåÉWã‡ä«óùÅÀîÑä|âÒé˚
                // jx.setValue(7, row, bl.getBillRest()); 
                // jx.setValue(8, row, bl.getBill()); 
//                jx.setValue(7, row, bl.getBill()); 
//                jx.setValue(8, row, bl.getBillRest()); 
                // IVS SANG END EDIT 20131113 Bug #15850: [gb]ÉåÉWã‡ä«óùÅÀîÑä|âÒé˚
                
                jx.setValue(1, row, bl.getSalesDate()); 
                jx.setValue(2, row, bl.getSlipNo()); 
                jx.setValue(3, row, bl.getCustomerNo()); 
                jx.setValue(4, row, bl.getCustomerName()); 
                jx.setValue(5, row, bl.getStaffName()); 
                jx.setValue(6, row, bl.getBill()); 
                jx.setValue(7, row, bl.getBillRest());
                //IVS_LVTu end edit 2015/07/29 New request #41101
                row++;
            }
            jx.removeRow(row);
            jx.openWorkbook();
        }
           
            public void printExcel2()
        {
            JExcelApi jx = new JExcelApi("îÑä|âÒé˚-âÒé˚àÍóó");
            jx.setTemplateFile("/reports/îÑä|âÒé˚-âÒé˚àÍóó.xls");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            //IVS_LVTu start edit 2015/07/29 New request #41101
            //jx.setValue(4, 2,"ä˙ä‘ " +  sdf.format( this.getTermFrom().getTime()) +" Å` "+ sdf.format(this.getTermTo().getTime())); 
            jx.setValue(2, 2,this.getMstShop().getShopName()); 
            if ( this.getTermFrom() != null && this.getTermTo() != null) {
                jx.setValue(2, 3,sdf.format( this.getTermFrom().getTime()) +" Å` "+ sdf.format(this.getTermTo().getTime())); 
            }else {
                jx.setValue(2, 3,"ä˙ä‘éwíËÇ»Çµ"); 
            }
            
            //int row = 5;
            int row = 6;
            // í«â¡çsêîÉZÉbÉg
            jx.insertRow(row, this.collectedBills.size() - 1);
            
            for(CollectedBillData bl : this.collectedBills)
            {
//                jx.setValue(2, row, bl.getSalesDate()); 
//                jx.setValue(3, row, bl.getSlipNo()); 
//                jx.setValue(4, row, bl.getCustomerNo()); 
//                jx.setValue(5, row, bl.getCustomerName()); 
//                jx.setValue(6, row, bl.getStaffName()); 
//                jx.setValue(7, row, bl.getBillRest()); 
//                jx.setValue(8, row, bl.getBill()); 
                jx.setValue(1, row, bl.getSalesDate()); 
                jx.setValue(2, row, bl.getSlipNo()); 
                jx.setValue(3, row, bl.getCustomerNo()); 
                jx.setValue(4, row, bl.getCustomerName()); 
                jx.setValue(5, row, bl.getStaffName()); 
                jx.setValue(6, row, bl.getBillRest()); 
                //åªã‡
                jx.setValue(7, row, bl.getCashValue()<= bl.getBillRest()?bl.getCashValue():bl.getBillRest()); 
                //ÉJÅ[Éh
                jx.setValue(8, row, bl.getCardValue()); 
                
                jx.setValue(9, row, bl.getEcashValue()); 
                jx.setValue(10, row, bl.getVouchersValue()); 
                
                jx.setValue(11, row, bl.getBill()<0 ? 0:bl.getBill()); 
                
                //IVS_LVTu end edit 2015/07/29 New request #41101
                row++;
            }
            jx.removeRow(row);
            jx.openWorkbook();
        }
}
