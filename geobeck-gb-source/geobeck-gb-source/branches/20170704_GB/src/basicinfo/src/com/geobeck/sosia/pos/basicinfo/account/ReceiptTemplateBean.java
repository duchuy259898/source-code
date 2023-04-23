/*
 * ReceiptTemplateBean.java
 *
 * Created on 2011/03/22, 18:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.basicinfo.account;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.DateUtil;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author geobeck
 */
public class ReceiptTemplateBean
{
    private int shopId;
    private int templateId;
    private Date fromDate;
    private Date toDate;
    private String message;
    private Date insertDate;
    private Date updateDate;
    private Date deleteDate;
    
    /** Creates a new instance of PointCardLayoutBean */
    public ReceiptTemplateBean()
    {
    }

    public void setData(ResultSetWrapper rs)  throws SQLException
    {
        this.setShopId(rs.getInt("shop_id"));
        this.setTemplateId(rs.getInt("template_id"));
        this.setFromDate(rs.getDate("from_date"));
        this.setToDate(rs.getDate("to_date"));
        this.setMessage(rs.getString("message"));
        this.setInsertDate(rs.getTimestamp("insert_date"));
        this.setUpdateDate(rs.getTimestamp("update_date"));
        this.setDeleteDate(rs.getTimestamp("delete_date"));
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getTemplateId()
    {
        return templateId;
    }

    public void setTemplateId(int templateId)
    {
        this.templateId = templateId;
    }

    public Date getFromDate()
    {
        return fromDate;
    }

    public void setFromDate(Date fromDate)
    {
        this.fromDate = fromDate;
    }

    public Date getToDate()
    {
        return toDate;
    }

    public void setToDate(Date toDate)
    {
        this.toDate = toDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public Date getInsertDate() 
    {
        return insertDate;
    }
    
    public void setInsertDate( Date newDate )
    {
        insertDate = newDate;
    }
    
    public Date getUpdateDate()
    {
        return updateDate;
    }
    
    public void setUpdateDate( Date newDate )
    {
        updateDate = newDate;
    }
    
    public Date getDeleteDate()
    {
        return deleteDate;
    }
    
    public void setDeleteDate( Date newDate )
    {
        deleteDate = newDate;
    }    

    public String toString() {
        return DateUtil.format(getFromDate(), "yyyy/MM/dd") + "Å`" + DateUtil.format(getToDate(), "yyyy/MM/dd");
    }

    public boolean insert(ConnectionWrapper con)  throws SQLException
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" insert into mst_receipt_template (");
        sql.append("      shop_id");
        sql.append("     ,template_id");
        sql.append("     ,from_date");
        sql.append("     ,to_date");
        sql.append("     ,message");
        sql.append("     ,insert_date");
        sql.append("     ,update_date");
        sql.append(" ) values (");
        sql.append("      " + SQLUtil.convertForSQL(getShopId()));
        sql.append("     ,(select coalesce(max(template_id), 0) + 1 from mst_receipt_template where shop_id = " + SQLUtil.convertForSQL(getShopId()) + ")");
        sql.append("     ," + SQLUtil.convertForSQLDateOnly(getFromDate()));
        sql.append("     ," + SQLUtil.convertForSQLDateOnly(getToDate()));
        sql.append("     ," + SQLUtil.convertForSQL(getMessage()));
        sql.append("     ,current_timestamp");
        sql.append("     ,current_timestamp");
        sql.append(" )");

        return (con.executeUpdate(sql.toString()) == 1);
    }

    public boolean update(ConnectionWrapper con)  throws SQLException
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" update mst_receipt_template");
        sql.append(" set");
        sql.append("      from_date = " + SQLUtil.convertForSQLDateOnly(getFromDate()));
        sql.append("     ,to_date = " + SQLUtil.convertForSQLDateOnly(getToDate()));
        sql.append("     ,message = " + SQLUtil.convertForSQL(getMessage()));
        sql.append("     ,update_date = current_timestamp");
        sql.append(" where");
        sql.append("         shop_id = " + SQLUtil.convertForSQL(getShopId()));
        sql.append("     and template_id = " + SQLUtil.convertForSQL(getTemplateId()));

        return (con.executeUpdate(sql.toString()) == 1);
    }

    public boolean delete(ConnectionWrapper con)  throws SQLException
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" update mst_receipt_template");
        sql.append(" set");
        sql.append("      update_date = current_timestamp");
        sql.append("     ,delete_date = current_timestamp");
        sql.append(" where");
        sql.append("         shop_id = " + SQLUtil.convertForSQL(getShopId()));
        sql.append("     and template_id = " + SQLUtil.convertForSQL(getTemplateId()));

        return (con.executeUpdate(sql.toString()) == 1);
    }

}
