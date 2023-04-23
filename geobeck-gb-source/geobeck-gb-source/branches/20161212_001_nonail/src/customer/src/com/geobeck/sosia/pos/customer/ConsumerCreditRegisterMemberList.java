/*
 * ConsumerCreditRegisterMemberList.java
 *
 * Created on 2013/02/27, 09:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.customer;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.system.*;

/**
 *
 * @author ivs_tttung
 */
public class ConsumerCreditRegisterMemberList extends ArrayList<ConsumerCreditRegisterMemberData>
{
    private String  sosiaCode = null;
    private String  addDateFrom = null;
    private String  addDateTo = null;
    private Integer gearCondition = null;
    private Integer shopID = null;
    private String customerNo = null;
    private String shopIDList = null;
    private  String credit_name = null;

    public String getSosiaCode() {
        return sosiaCode;
    }

    public void setSosiaCode(String sosiaCode) {
        this.sosiaCode = sosiaCode;
    }
    
    public String getAddDateFrom() {
        return addDateFrom;
    }

    public void setAddDateFrom(String addDateFrom) {
        this.addDateFrom = addDateFrom;
    }

    public String getAddDateTo() {
        return addDateTo;
    }

    public void setAddDateTo(String addDateTo) {
        this.addDateTo = addDateTo;
    }

    public Integer getGearCondition() {
        return gearCondition;
    }

    public void setGearCondition(Integer gearCondition) {
        this.gearCondition = gearCondition;
    }
    
    // Khoa add Start 20121105 ispot‰ïˆõˆê——
    public Integer getShopID() {
        return shopID;
    }

    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }
    
    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }
    public String getShopIDList() {
        return shopIDList;
    }

    public void setShopIDList(String shopIDList) {
        this.shopIDList = shopIDList;
    }    
    
    /**
     * @return the credit_name
     */
    public String getCredit_name() {
        return credit_name;
    }

    /**
     * @param credit_name the credit_name to set
     */
    public void setCredit_name(String credit_name) {
        this.credit_name = credit_name;
    }

    
    public ConsumerCreditRegisterMemberList()
    {
    }

    public boolean load(Integer flag, String credit_name)
    {
        this.clear();
        try
        {
            ConnectionWrapper con = SystemInfo.getConnection();
            ResultSetWrapper rs = con.executeQuery(this.getCustomerCreditRegistMemberList(flag, credit_name));
            while (rs.next()) {
                ConsumerCreditRegisterMemberData mmd = new ConsumerCreditRegisterMemberData();
                mmd.setDataForConsumer(rs);
                this.add(mmd);
            }
            rs.close();
        }
        catch(SQLException e)
        {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
        return true;
    }
    
    /**
     * ‰æ–Ê‚É‚ÄŒŸõðŒ‚ði‚èž‚Ý‚µIspot‰ïˆõŒŸõAˆê——•\Ž¦
     * @param flag
     * @return 
     */
    private String getCustomerCreditRegistMemberList(Integer flag ,String credit_name)
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select DISTINCT \n"); 
        //An start add 20131024
        sql.append(" dcr.approval, \n ");
        //An end add 20131024
        sql.append(" dcr.insert_date::timestamp::date,  \n");
        sql.append("  c.customer_id,  \n");
        sql.append("  c.customer_no, \n");
        sql.append(" case when c.customer_name1 is null or c.customer_name2 is null then '' else c.customer_name1 || '@' || c.customer_name2 end as customer_name,  \n ");
        sql.append(" dcr.contract_status, \n ");
        sql.append(" dcr.credit_flag, \n ");
        sql.append(" credit_name, \n ");
        sql.append(" CAST(coalesce(credit_value, '0') AS integer) as credit_value, \n ");
        sql.append(" mcr.credit_status_name, \n ");
        sql.append(" apply_start_date, \n ");
        sql.append(" apply_end_date, \n ");
        sql.append(" submission_date, \n ");
        sql.append(" receive_money_date, \n ");
        sql.append("  dcr.cancel_date \n ");
        sql.append(" ,array_to_string(array(SELECT course_name FROM data_contract dc  \n ");
        sql.append("  inner join mst_course msc on  dc.product_id = msc.course_id  \n ");
        sql.append("  WHERE dc.slip_no  = dcr.slip_no and dcr.shop_id = dc.shop_id    \n ");
        sql.append("  ), ', ') AS course_name  \n ");
        sql.append(" ,dcr.shop_id ,dcr.slip_no ,dcr.payment_no,dcr.payment_detail_no   \n ");        
        sql.append(" from data_credit dcr \n ");
        sql.append(" inner join mst_credit mcr \n  ");
        sql.append("        on dcr.credit_status_no = mcr.credit_status_no \n ");
        sql.append(" inner join mst_customer c \n ");
        sql.append("          on dcr.customer_id = c.customer_id \n ");
        
        sql.append(" ");
        sql.append(" where");
        //sql.append(" data_credit.shop_id = " + SQLUtil.convertForSQL(shopID) + " and ");
        sql.append("   dcr.delete_date is null \n");
        if (addDateFrom != null)
        {
            sql.append("     and dcr.insert_date >= " + SQLUtil.convertForSQL(addDateFrom));
        }
        if (addDateTo != null)
        {
            sql.append("     and dcr.insert_date <= " + SQLUtil.convertForSQL(addDateTo));
        }
       // sql.append(" and ispot_id is not null ");
        // Thanh start edit 2013/05/27
        /*
        if(flag == 0){
            sql.append(" and c.customer_no = 0 ");
        }else if(flag == 1){
            sql.append(" and c.customer_no <> 0 ");
        }
        */ 
         if(flag == 0){
            sql.append(" and dcr.receive_money_date is null ");
        }else if(flag == 1){
            sql.append(" and dcr.receive_money_date is not null ");
        }
         // Thanh start edit 2013/05/27
         
        if(credit_name==null){
            //sql.append(" and customer_no = 0 ");
        }else 
        {
            sql.append(" and credit_name = " + SQLUtil.convertForSQL(credit_name));
        }
       
         
        sql.append(" order by ");
        sql.append("      insert_date ,dcr.slip_no desc"); 
        return sql.toString();
    }
}
