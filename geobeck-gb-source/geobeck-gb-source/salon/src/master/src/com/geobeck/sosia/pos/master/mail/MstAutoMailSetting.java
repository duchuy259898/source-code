/*
 * MstAutoMailSetting.java
 *
 * Created on 2006/05/29, 9:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.mail;

import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;

/**
 * é©ìÆÉÅÅ[Éãê›íËÉ}ÉXÉ^
 * @author geobeck
 */
public class MstAutoMailSetting
{
    public static Integer count = 0;

    private MstShop shop = null;
    private Integer mailId = null;
    private String mailName = null;
    private Integer displaySeq = null;
    private Integer active = null;
    private Integer sendHour = null;
    private Integer sendMinute = null;
    private Integer dateCondition = null;
    private Integer daysAfterVisit = null;
    private Integer lastDaysAfterVisit = null;
    private Integer daysBeforeBirthday = null;
    private Integer daysBeforeReservation = null;
    //VTAn Start add 20130108
    private Integer daysCycleBefore = null;
    private Integer daysCycleBeforeFollow = null;
    private Integer daysReCycleBefore = null;
    //VTAn End add 20130108
    private Integer monthFixedDay = null;
    private Integer visitType = null;
    private Integer sex = null;
    private Integer technicClassId = null;
    private Integer itemClassId = null;
    private String mailTitle = null;
    private String mailBody = null;

    /**
     * ÉRÉìÉXÉgÉâÉNÉ^
     */
    public MstAutoMailSetting()
    {
    }

    /**
     * @return the shop
     */
    public MstShop getShop() {
        return shop;
    }

    /**
     * @param shop the shop to set
     */
    public void setShop(MstShop shop) {
        this.shop = shop;
    }

    /**
     * @return the mailId
     */
    public Integer getMailId() {
        return mailId;
    }

    /**
     * @param mailId the mailId to set
     */
    public void setMailId(Integer mailId) {
        this.mailId = mailId;
    }

    /**
     * @return the mailName
     */
    public String getMailName() {
        return mailName;
    }

    /**
     * @param mailName the mailName to set
     */
    public void setMailName(String mailName) {
        this.mailName = mailName;
    }

    /**
     * @return the displaySeq
     */
    public Integer getDisplaySeq() {
        return displaySeq;
    }

    /**
     * @param displaySeq the displaySeq to set
     */
    public void setDisplaySeq(Integer displaySeq) {
        this.displaySeq = displaySeq;
    }

    /**
     * @return the active
     */
    public Integer getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(Integer active) {
        this.active = active;
    }

    /**
     * @return the sendHour
     */
    public Integer getSendHour() {
        return sendHour;
    }

    /**
     * @param sendHour the sendHour to set
     */
    public void setSendHour(Integer sendHour) {
        this.sendHour = sendHour;
    }

    /**
     * @return the sendMinute
     */
    public Integer getSendMinute() {
        return sendMinute;
    }

    /**
     * @param sendMinute the sendMinute to set
     */
    public void setSendMinute(Integer sendMinute) {
        this.sendMinute = sendMinute;
    }

    /**
     * @return the dateCondition
     */
    public Integer getDateCondition() {
        return dateCondition;
    }

    /**
     * @param dateCondition the dateCondition to set
     */
    public void setDateCondition(Integer dateCondition) {
        this.dateCondition = dateCondition;
    }

    /**
     * @return the daysAfterVisit
     */
    public Integer getDaysAfterVisit() {
        return daysAfterVisit;
    }

    /**
     * @param daysAfterVisit the daysAfterVisit to set
     */
    public void setDaysAfterVisit(Integer daysAfterVisit) {
        this.daysAfterVisit = daysAfterVisit;
    }

    /**
     * @return the lastDaysAfterVisit
     */
    public Integer getLastDaysAfterVisit() {
        return lastDaysAfterVisit;
    }

    /**
     * @param lastDaysAfterVisit the lastDaysAfterVisit to set
     */
    public void setLastDaysAfterVisit(Integer lastDaysAfterVisit) {
        this.lastDaysAfterVisit = lastDaysAfterVisit;
    }

    /**
     * @return the monthFixedDay
     */
    public Integer getMonthFixedDay() {
        return monthFixedDay;
    }

    /**
     * @param monthFixedDay the monthFixedDay to set
     */
    public void setMonthFixedDay(Integer monthFixedDay) {
        this.monthFixedDay = monthFixedDay;
    }

    /**
     * @return the daysBeforeBirthday
     */
    public Integer getDaysBeforeBirthday() {
        return daysBeforeBirthday;
    }

    /**
     * @param daysBeforeBirthday the daysBeforeBirthday to set
     */
    public void setDaysBeforeBirthday(Integer daysBeforeBirthday) {
        this.daysBeforeBirthday = daysBeforeBirthday;
    }

    /**
     * @return the daysBeforeReservation
     */
    public Integer getDaysBeforeReservation() {
        return daysBeforeReservation;
    }

    /**
     * @param daysBeforeReservation the daysBeforeReservation to set
     */
    public void setDaysBeforeReservation(Integer daysBeforeReservation) {
        this.daysBeforeReservation = daysBeforeReservation;
    }

    /**
     * @return the visitType
     */
    public Integer getVisitType() {
        return visitType;
    }

    /**
     * @param visitType the visitType to set
     */
    public void setVisitType(Integer visitType) {
        this.visitType = visitType;
    }

    /**
     * @return the sex
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * @param sex the sex to set
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * @return the technicClassId
     */
    public Integer getTechnicClassId() {
        return technicClassId;
    }

    /**
     * @param technicClassId the technicClassId to set
     */
    public void setTechnicClassId(Integer technicClassId) {
        this.technicClassId = technicClassId;
    }

    /**
     * @return the itemClassId
     */
    public Integer getItemClassId() {
        return itemClassId;
    }

    /**
     * @param itemClassId the itemClassId to set
     */
    public void setItemClassId(Integer itemClassId) {
        this.itemClassId = itemClassId;
    }

    /**
     * @return the mailTitle
     */
    public String getMailTitle() {
        return mailTitle;
    }

    /**
     * @param mailTitle the mailTitle to set
     */
    public void setMailTitle(String mailTitle) {
        this.mailTitle = mailTitle;
    }

    /**
     * @return the mailBody
     */
    public String getMailBody() {
        return mailBody;
    }

    /**
     * @param mailBody the mailBody to set
     */
    public void setMailBody(String mailBody) {
        this.mailBody = mailBody;
    }

    public String toString() {
        return getMailName();
    }
//VTAn Start add 20130108
    public Integer getDaysCycleBefore() {
        return daysCycleBefore;
    }

    public void setDaysCycleBefore(Integer daysCycleBefore) {
        this.daysCycleBefore = daysCycleBefore;
    }

    public Integer getDaysCycleBeforeFollow() {
        return daysCycleBeforeFollow;
    }

    public void setDaysCycleBeforeFollow(Integer daysCycleBeforeFollow) {
        this.daysCycleBeforeFollow = daysCycleBeforeFollow;
    }

    public Integer getDaysReCycleBefore() {
        return daysReCycleBefore;
    }

    public void setDaysReCycleBefore(Integer daysReCycleBefore) {
        this.daysReCycleBefore = daysReCycleBefore;
    }
    
//VTAn End add 20130108
    public void setData(ConnectionWrapper con, ResultSetWrapper rs) throws SQLException
    {
        MstShop ms = new MstShop();
        ms.setShopID(rs.getInt("shop_id"));
        ms.load(con);
        
        this.setShop(ms);
        this.setMailId(rs.getInt("mail_id"));
        this.setMailName(rs.getString("mail_name"));
        this.setDisplaySeq(rs.getInt("display_seq"));
        this.setActive(rs.getInt("active"));
        this.setSendHour(rs.getInt("send_hour"));
        this.setSendMinute(rs.getInt("send_minute"));
        this.setDateCondition(rs.getInt("date_condition"));
        this.setVisitType(rs.getInt("visit_type"));
        this.setSex(rs.getInt("sex"));
        this.setMailTitle(rs.getString("mail_title"));
        this.setMailBody(rs.getString("mail_body"));

        int tmpValue = 0;
        tmpValue = rs.getInt("days_after_visit");
        this.setDaysAfterVisit(rs.wasNull() ? null : tmpValue);
        tmpValue = rs.getInt("last_days_after_visit");
        this.setLastDaysAfterVisit(rs.wasNull() ? null : tmpValue);
        tmpValue = rs.getInt("month_fixed_day");
        this.setMonthFixedDay(rs.wasNull() ? null : tmpValue);
        tmpValue = rs.getInt("days_before_birthday");
        this.setDaysBeforeBirthday(rs.wasNull() ? null : tmpValue);
        tmpValue = rs.getInt("days_before_reservation");
        this.setDaysBeforeReservation(rs.wasNull() ? null : tmpValue);
        tmpValue = rs.getInt("technic_class_id");
        this.setTechnicClassId(rs.wasNull() ? null : tmpValue);
        tmpValue = rs.getInt("item_class_id");
        this.setItemClassId(rs.wasNull() ? null : tmpValue);
        //VTAn Start add 20130108
        tmpValue=rs.getInt("cycle_before");
        this.setDaysCycleBefore(rs.wasNull() ? null :tmpValue);
        tmpValue=rs.getInt("cycle_before_follow");
        this.setDaysCycleBeforeFollow(rs.wasNull() ? null :tmpValue);
        tmpValue=rs.getInt("re_cycle_before");
        this.setDaysReCycleBefore(rs.wasNull() ? null :tmpValue);
        //VTAn End add 20130108
    }

    public static ResultSetWrapper getSelectAll(ConnectionWrapper con, Integer shopId) throws Exception  {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     *");
        sql.append(" from");
        sql.append("     mst_automail_setting");
        sql.append(" where");
        sql.append("     shop_id = " + SQLUtil.convertForSQL(shopId));
        sql.append(" order by");
        sql.append("     display_seq");

        ResultSetWrapper result = con.executeQuery(sql.toString());
        result.last();
        count = result.getRow();
        result.beforeFirst();
        return result;
    }

    public boolean regist(ConnectionWrapper con) throws SQLException {
        return regist(con, this.getDisplaySeq());
    }

    public boolean regist(ConnectionWrapper con, Integer lastSeq) throws SQLException {

        if (isExists(con)) {

            if (lastSeq != this.getDisplaySeq()) {
                if (con.executeUpdate(this.getSlideSQL(lastSeq, false)) < 0) {
                    return false;
                }

                if (0 <= this.getDisplaySeq()) {
                    if (con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0) {
                        return false;
                    }
                }
            }

            if (con.executeUpdate(this.getUpdateSQL()) != 1) {
                return false;
            }

        } else {

            if (0 <= this.getDisplaySeq()) {
                if (con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), true)) < 0) {
                    return false;
                }
            }

            if (con.executeUpdate(this.getInsertSQL()) != 1) {
                return false;
            }
        }
            return true;
	}

    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if (this.getShop() == null || this.getMailId() == null) return false;
        if (con == null) return false;

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     1");
        sql.append(" from");
        sql.append("     mst_automail_setting");
        sql.append(" where");
        sql.append("         shop_id = " + SQLUtil.convertForSQL(shop.getShopID()));
        sql.append("     and mail_id = " + SQLUtil.convertForSQL(this.getMailId()));

        ResultSetWrapper rs = con.executeQuery(sql.toString());

        return rs.next();
    }

    /**
    * ï\é¶èáÇÇ∏ÇÁÇ∑ÇrÇpÇkï∂ÇéÊìæÇ∑ÇÈ
    * @param seq å≥ÇÃï\é¶èá
    * @param isIncrement true - â¡éZÅAfalse - å∏éZ
    * @return ï\é¶èáÇÇ∏ÇÁÇ∑ÇrÇpÇkï∂
    */
    private String getSlideSQL(Integer seq, boolean isIncrement) {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" update mst_automail_setting");
        sql.append(" set");
        sql.append("     display_seq = display_seq " + (isIncrement ? "+" : "-") + "1");
        sql.append(" where");
        sql.append("         shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
        sql.append("     and display_seq >= " + SQLUtil.convertForSQL(seq));

        System.out.println(sql.toString());

        return sql.toString();
    }

    /**
     * Insertï∂ÇéÊìæÇ∑ÇÈÅB
     * @return Insertï∂
     */
    private String getInsertSQL() {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" insert into mst_automail_setting (");
        sql.append("      shop_id");
        sql.append("     ,mail_id");
        sql.append("     ,mail_name");
        sql.append("     ,display_seq");
        sql.append("     ,active");
        sql.append(" )");
        sql.append(" select");
        sql.append("      " + SQLUtil.convertForSQL(this.getShop().getShopID()));
        sql.append("     ,coalesce(max(mail_id), 0) + 1");
        sql.append("     ," + SQLUtil.convertForSQL(this.getMailName()));
        sql.append("     ,case when " + SQLUtil.convertForSQL(this.getDisplaySeq()) + " between 0 and coalesce(max(display_seq), 0)");
        sql.append("         then " + SQLUtil.convertForSQL(this.getDisplaySeq()));
        sql.append("         else coalesce((");
        sql.append("                 select");
        sql.append("                     max(display_seq)");
        sql.append("                 from");
        sql.append("                     mst_automail_setting");
        sql.append("                 where");
        sql.append("                     shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
        sql.append("             ), 0) + 1");
        sql.append("      end");
        sql.append("     ," + SQLUtil.convertForSQL(this.getActive()));
        sql.append(" from");
        sql.append("     mst_automail_setting");

        System.out.println(sql.toString());

        return sql.toString();
    }

    /**
     * Updateï∂ÇéÊìæÇ∑ÇÈÅB
     * @return Updateï∂
     */
    private String getUpdateSQL() {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" update mst_automail_setting");
        sql.append(" set");
        sql.append("      mail_name = " + SQLUtil.convertForSQL(this.getMailName()));
        sql.append("     ,active = " + SQLUtil.convertForSQL(this.getActive()));
        sql.append("     ,send_hour = " + SQLUtil.convertForSQL(this.getSendHour()));
        sql.append("     ,send_minute = " + SQLUtil.convertForSQL(this.getSendMinute()));
        sql.append("     ,date_condition = " + SQLUtil.convertForSQL(this.getDateCondition()));
        sql.append("     ,days_after_visit = " + SQLUtil.convertForSQL(this.getDaysAfterVisit()));
        sql.append("     ,last_days_after_visit = " + SQLUtil.convertForSQL(this.getLastDaysAfterVisit()));
        sql.append("     ,month_fixed_day = " + SQLUtil.convertForSQL(this.getMonthFixedDay()));
        sql.append("     ,days_before_birthday = " + SQLUtil.convertForSQL(this.getDaysBeforeBirthday()));
        sql.append("     ,days_before_reservation = " + SQLUtil.convertForSQL(this.getDaysBeforeReservation()));
        //VTAn Start add 20130108
        sql.append("     ,cycle_before = " + SQLUtil.convertForSQL(this.getDaysCycleBefore()));
        sql.append("     ,cycle_before_follow = " + SQLUtil.convertForSQL(this.getDaysCycleBeforeFollow()));
        sql.append("     ,re_cycle_before = " + SQLUtil.convertForSQL(this.getDaysReCycleBefore()));
        
        //VTAn End add 20130108
        sql.append("     ,visit_type = " + SQLUtil.convertForSQL(this.getVisitType()));
        sql.append("     ,sex = " + SQLUtil.convertForSQL(this.getSex()));
        sql.append("     ,technic_class_id = " + SQLUtil.convertForSQL(this.getTechnicClassId()));
        sql.append("     ,item_class_id = " + SQLUtil.convertForSQL(this.getItemClassId()));
        sql.append("     ,mail_title = " + SQLUtil.convertForSQL(this.getMailTitle()));
        sql.append("     ,mail_body = " + SQLUtil.convertForSQL(this.getMailBody()));
        sql.append("     ,display_seq =");
        sql.append("         case when " + SQLUtil.convertForSQL(this.getDisplaySeq()) + " between 0 and coalesce((");
        sql.append("                     select");
        sql.append("                         max(display_seq)");
        sql.append("                     from");
        sql.append("                         mst_automail_setting");
        sql.append("                     where");
        sql.append("                             shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
        sql.append("                         and mail_id != " + SQLUtil.convertForSQL(this.getMailId()) + "), 0)");
        sql.append("             then " + SQLUtil.convertForSQL(this.getDisplaySeq()));
        sql.append("             else coalesce((");
        sql.append("                         select");
        sql.append("                             max(display_seq)");
        sql.append("                         from");
        sql.append("                             mst_automail_setting");
        sql.append("                         where");
        sql.append("                                 shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
        sql.append("                             and mail_id != " + SQLUtil.convertForSQL(this.getMailId()) + "), 0) + 1");
        sql.append("         end");
        sql.append(" where");
        sql.append("         shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
        sql.append("     and mail_id = " + SQLUtil.convertForSQL(this.getMailId()));

        System.out.println(sql.toString());

        return sql.toString();
    }

    public boolean delete(ConnectionWrapper con) throws SQLException {

        if (con.executeUpdate(this.getSlideSQL(this.getDisplaySeq(), false)) < 0) {
            return false;
        }

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" delete from mst_automail_setting");
        sql.append(" where");
        sql.append("         shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
        sql.append("     and mail_id = " + SQLUtil.convertForSQL(this.getMailId()));

        return con.executeUpdate(sql.toString()) == 1;

    }

}
