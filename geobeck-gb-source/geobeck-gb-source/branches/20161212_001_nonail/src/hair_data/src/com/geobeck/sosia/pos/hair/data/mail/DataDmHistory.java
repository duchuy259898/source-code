/*
 * DataDmHistory.java
 *
 * Created on 2010/01/25, 14:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.mail;

import java.util.*;
import java.util.logging.Level;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.util.SQLUtil;
import com.geobeck.util.DateUtil;
import java.text.SimpleDateFormat;

/**
 * DM作成履歴データ
 * @author geobeck
 */
public class DataDmHistory {

    private Integer shopID = null;
    private Integer dmType = null;
    private Date makeDate = null;
    private String dmTitle = null;
    private Integer dmCount = null;

    public Integer getShopID() {
        return shopID;
    }

    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }

    public Integer getDmType() {
        return dmType;
    }

    public void setDmType(Integer dmType) {
        this.dmType = dmType;
    }

    public Date getMakeDate() {
        return makeDate;
    }

    public void setMakeDate(Date makeDate) {
        this.makeDate = makeDate;
    }

    public String getMakeDateStr() {
        return DateUtil.format(getMakeDate(), "yyyy/MM/dd HH:mm:ss");
    }

    public String getMakeDateStrDateOnly() {
        return DateUtil.format(getMakeDate(), "yyyy/MM/dd");
    }

    public String getDmTitle() {
        return dmTitle;
    }

    public void setDmTitle(String dmTitle) {
        this.dmTitle = dmTitle;
    }

    public Integer getDmCount() {
        return dmCount;
    }

    public void setDmCount(Integer dmCount) {
        this.dmCount = dmCount;
    }

    public String toString() {
        return dmTitle;
    }

    public static ArrayList<DataDmHistory> getList(String shopIdList) {

        ArrayList<DataDmHistory> result = new ArrayList<DataDmHistory>();

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     *");
        sql.append(" from");
        sql.append("     data_dm_history");
        sql.append(" where");
        sql.append("     shop_id in (" + shopIdList + ")");
        sql.append("     and dm_type <> 0");
        sql.append(" order by");
        sql.append("      dm_type");
        sql.append("     ,make_date desc");

        try {

            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
                DataDmHistory dh = new DataDmHistory();
                dh.setShopID(rs.getInt("shop_id"));
                dh.setDmType(rs.getInt("dm_type"));
                //Start edit 20131115 lvut reset timestamp gmt +9
                dh.setMakeDate(getFormatDate(rs.getString("make_date")));
                //End edit 20131115 lvut reset timestamp gmt +9
                dh.setDmTitle(rs.getString("dm_title"));
                dh.setDmCount(rs.getInt("dm_count"));
                result.add(dh);
            }

            rs.close();

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return result;
    }

    public ArrayList<DataDmHistoryDetail> getDetailList() {

        ArrayList<DataDmHistoryDetail> result = new ArrayList<DataDmHistoryDetail>();

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      a.dm_type");
        sql.append("     ,a.mail_address");
        sql.append("     ,a.mail_title");
        sql.append("     ,a.mail_body");
        sql.append("     ,b.*");
        sql.append(" from");
        sql.append("     data_dm_history_detail a");
        sql.append("         join mst_customer b");
        sql.append("         using(customer_id)");
        sql.append(" where");
        sql.append("         a.shop_id = " + SQLUtil.convertForSQL(getShopID()));
        sql.append("     and a.dm_type = " + SQLUtil.convertForSQL(getDmType()));
        //sql.append("     and a.make_date = " + SQLUtil.convertForSQL(getMakeDateStr()));
        //Start edit 20131115 lvut DM作成履歴の履歴表示の不具合
        // sql.append("     and date(a.make_date) = " + "date("+SQLUtil.convertForSQL(getMakeDateStr())+")");
         sql.append("     and a.make_date = " + SQLUtil.convertForSQL(getMakeDateStr()));
        //End edit 20131115 lvut DM作成履歴の履歴表示の不具合

        // ふりがな順で出力（未設定は最後に出力）
        sql.append(" order by");
        sql.append("      case when length(coalesce(b.customer_kana1, '')) > 0 then 0 else 1 end");
        sql.append("     ,b.customer_kana1");
        sql.append("     ,b.customer_kana2");

        try {

            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
                DataDmHistoryDetail dhd = new DataDmHistoryDetail();
                dhd.setData(rs);
                dhd.setDmType(rs.getInt("dm_type"));
                dhd.setMailAddress(rs.getString("mail_address"));
                dhd.setMailTitle(rs.getString("mail_title"));
                dhd.setMailBody(rs.getString("mail_body"));
                result.add(dhd);
            }

            rs.close();

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return result;
    }

    public boolean delete() {

        boolean result = false;

        ConnectionWrapper con = SystemInfo.getConnection();

        try {

            con.begin();

            try {
                StringBuilder sql = null;

                sql = new StringBuilder(1000);
                sql.append(" delete from data_dm_history");
                sql.append(" where");
                sql.append("         shop_id = " + SQLUtil.convertForSQL(getShopID()));
                sql.append("     and dm_type = " + SQLUtil.convertForSQL(getDmType()));
                sql.append("     and make_date = " + SQLUtil.convertForSQL(getMakeDateStr()));
                con.executeUpdate(sql.toString());

                sql = new StringBuilder(1000);
                sql.append(" delete from data_dm_history_detail");
                sql.append(" where");
                sql.append("         shop_id = " + SQLUtil.convertForSQL(getShopID()));
                sql.append("     and dm_type = " + SQLUtil.convertForSQL(getDmType()));
                sql.append("     and make_date = " + SQLUtil.convertForSQL(getMakeDateStr()));
                con.executeUpdate(sql.toString());

                con.commit();
                result = true;

            } catch (Exception e) {
                con.rollback();
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return result;
    }
     private static  Date getFormatDate(String string) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(string);
        }
        catch (Exception ex){
            System.out.println("Exception "+ex);
        }
        return date;
    }
}
