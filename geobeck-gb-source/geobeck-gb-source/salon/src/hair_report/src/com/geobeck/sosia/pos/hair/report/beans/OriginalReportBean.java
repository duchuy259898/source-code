/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

import com.geobeck.sosia.pos.master.company.MstGroup;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.apache.commons.lang.time.DateUtils;

/**
 *
 * @author user
 */
public class OriginalReportBean implements Serializable {

    public static final String YMD_FORMAT = "yyyy/MM/dd";

    public enum ExpReportType {
        RANKING
        , DESIGN_RANKING
        , MONTH_TRANSITION
        , LANDING_FOCUS; 
    };
    
    // IVS_LVTu start edit 20210129 #18378 underfive 数量追加
    // 201710 GB edit start #27108 R3追加
    public enum OrderType {
        R2
        , R3
        , RR
        , TO_RATE
        , SALES
        , DESIGNATED
        , GOOD
        , BAD
        , RETOUCH
        , RETOUCH_RATE
        ,UNDERFIVE_QUANTITY
        ,UNDERFIVE_DISCOUNT_QUANTITY
        , PRODUCT
        , PROCEEDS; 
    };
    // 201710 GB edit end #27108 R3追加
    // IVS_LVTu end edit 20210129 #18378 underfive 数量追加
    
    private ExpReportType expType = null;
    
    private MstGroup group = null;
    
    private MstShop shop = null;
    
    private MstStaff staff = null;
    
    private Date targetDateFrom = null;
    
    private Date targetDateTo = null;
    
    /** 再来集計期間 */
    private String calculationPeriod = "";
    
    private Integer targetFromYear = null;
    
    private Integer targetFromMonth = null;
    
    private Integer targetToYear = null;
    
    private Integer targetToMonth = null;
    
    private Date periodDateFrom = null;
    
    private Date periodDateTo = null;
    
    private String order = null;
    
    private Integer orderIndex = null;
    
    private boolean taxFlag = true;
    
    private boolean shopFlag = false;
    
    private boolean staffFlag = false;
    
    private boolean targetPeriod01Flag = false;

    private boolean targetPeriod02Flag = false;
    
    /**
     * 対象年月に対する再来算出期間を保持するマップ。
     * キー：対象年月 バリュー：再来算出期間のリスト（0=開始 1=終了）
     */
    private Map<String, List<Date>> periodYmdMap = null;
    
    public OriginalReportBean(ExpReportType expType) {
        this.expType = expType;
    }

    /**
     * @return the expType
     */
    public ExpReportType getExpType() {
        return expType;
    }
    
    public void setShop(Object obj) {
        this.group = null;
        this.shop = null;
        
        if (obj != null) {
            if (obj instanceof MstGroup) {
                this.group = (MstGroup) obj;
            } else if (obj instanceof MstShop) {
                this.shop = (MstShop) obj;
            }
        }
    }
    
    public Integer getGroupId() {
        if (this.shop != null) {
            return this.shop.getGroupID();
        } else if (this.group != null) {
            return this.group.getGroupID();
        }
        return null;
    }
    
    public String getGroupName() {
        return (this.group != null) ? this.group.getGroupName() : null;
    }
    
    public Integer getShopId() {
        return (this.shop != null) ? this.shop.getShopID() : null;
    }
    
    public String getShopName() {
        return (this.shop != null) ? this.shop.getShopName() : null;
    }
    
    public void setStaff(Object obj) {
        this.staff = null;
        
        if (obj != null && obj instanceof MstStaff) {
            this.staff = (MstStaff) obj;
        }
    }
    
    public Integer getStaffId() {
        return (this.staff != null) ? this.staff.getStaffID() : null;
    }
    
    public String getStaffName() {
        if (this.staff == null) {
            return null;
        }
        StringBuilder sbName = new StringBuilder();
        for (String name : this.staff.getStaffName()) {
            sbName.append(name);
        }
        return sbName.toString();
    }
    
    /**
     * @return the targetDateFrom
     */
    public Date getTargetDateFrom() {
        return targetDateFrom;
    }

    /**
     * @param targetDateFrom the targetDateFrom to set
     */
    public void setTargetDateFrom(Date targetDateFrom) {
        if (targetDateFrom != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(targetDateFrom);
            this.targetFromYear = calendar.get(Calendar.YEAR);
            this.targetFromMonth = calendar.get(Calendar.MONTH - 1);
        }
        this.targetDateFrom = targetDateFrom;
    }

    public void setTargetDateFrom(Integer year, Integer month, Integer day) {
        
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        if (year != null) {
            calendar.set(Calendar.YEAR, year);
        }
        if (month != null) {
            calendar.set(Calendar.MONTH, month - 1);
        }
        if (day == null) {
            day = calendar.getActualMinimum(Calendar.DATE);
        }
        calendar.set(Calendar.DATE, day);
        
        this.targetFromYear = year;
        this.targetFromMonth = month;
        this.targetDateFrom = calendar.getTime();
    }

    /**
     * @return the targetDateTo
     */
    public Date getTargetDateTo() {
        return targetDateTo;
    }

    /**
     * @param targetDateTo the targetDateTo to set
     */
    public void setTargetDateTo(Date targetDateTo) {
        if (targetDateTo != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(targetDateTo);
            this.targetToYear = calendar.get(Calendar.YEAR);
            this.targetToMonth = calendar.get(Calendar.MONTH - 1);
        }
        this.targetDateTo = targetDateTo;
    }

    public void setTargetDateTo(Integer year, Integer month, Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        if (year != null) {
            calendar.set(Calendar.YEAR, year);
        }
        if (month != null) {
            calendar.set(Calendar.MONTH, month - 1);
        }
        if (day == null) {
            day = calendar.getActualMaximum(Calendar.DATE);
        }
        calendar.set(Calendar.DATE, day);
        
        this.targetToYear = year;
        this.targetToMonth = month;
        this.targetDateTo = calendar.getTime();
    }

    public String getTargetYmdFrom() {
        return toYmd(this.targetDateFrom);
    }
    
    public String getTargetYmdTo() {
        return toYmd(this.targetDateTo);
    }
    
    /**
     * @return the targetFromYear
     */
    public Integer getTargetFromYear() {
        return targetFromYear;
    }

    /**
     * @return the targetFromMonth
     */
    public Integer getTargetFromMonth() {
        return targetFromMonth;
    }

    /**
     * @return the targetToYear
     */
    public Integer getTargetToYear() {
        return targetToYear;
    }

    /**
     * @return the targetToMonth
     */
    public Integer getTargetToMonth() {
        return targetToMonth;
    }
    
    public Long getTargetDayRange() {
        return dayDiff(this.targetDateFrom, new Date(targetDateTo.getTime() + TimeUnit.DAYS.toMillis(1)));
    }
    
    public Long getOptDayRange() {
        // 実行日を取得
        Date today = DateUtils.truncate(new Date(), Calendar.DATE);
        
        // 集計日
        Date dateTo = today; 
        
        if (today.after(this.targetDateTo)) {
            // 実行日が対象期間終了日より大きい場合は対象期間終了日を集計日とする。
            dateTo = targetDateTo;
        } else if (today.before(this.targetDateFrom)) {
            // 実行日が対象期間開始日より小さい場合は対象期間開始日の前日を集計日とする。
            Calendar cal = Calendar.getInstance();
            cal.setTime(this.targetDateFrom);
            cal.add(Calendar.DATE, -1);
            dateTo = cal.getTime();
        }
        return dayDiff(this.targetDateFrom, new Date(dateTo.getTime() + TimeUnit.DAYS.toMillis(1)));
    }
    
    /**
     * @return the periodDateFrom
     */
    public Date getPeriodDateFrom() {
        return periodDateFrom;
    }

    /**
     * @param periodDateFrom the periodDateFrom to set
     */
    public void setPeriodDateFrom(Date periodDateFrom) {
        this.periodDateFrom = periodDateFrom;
    }

    /**
     * @return the periodDateTo
     */
    public Date getPeriodDateTo() {
        return periodDateTo;
    }

    /**
     * @param periodDateTo the periodDateTo to set
     */
    public void setPeriodDateTo(Date periodDateTo) {
        this.periodDateTo = periodDateTo;
    }
    
    public String getPeriodYmdFrom() {
        return toYmd(this.periodDateFrom);
    }
    
    public String getPeriodYmdTo() {
        return toYmd(this.periodDateTo);
    }
    
    /**
     * @return the order
     */
    public String getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * @return the orderIndex
     */
    public Integer getOrderIndex() {
        return orderIndex;
    }

    /**
     * @param orderIndex the orderIndex to set
     */
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    /**
     * @return the taxFlag
     */
    public boolean isTaxFlag() {
        return taxFlag;
    }

    /**
     * @param taxFlag the taxFlag to set
     */
    public void setTaxFlag(boolean taxFlag) {
        this.taxFlag = taxFlag;
    }

    public String getTax() {
        return this.taxFlag ? "税込" : "税抜";
    }
    
    /**
     * @return the shopFlag
     */
    public boolean isShopFlag() {
        return shopFlag;
    }

    /**
     * @param shopFlag the shopFlag to set
     */
    public void setShopFlag(boolean shopFlag) {
        this.shopFlag = shopFlag;
    }

    /**
     * @return the staffFlag
     */
    public boolean isStaffFlag() {
        return staffFlag;
    }

    /**
     * @param staffFlag the staffFlag to set
     */
    public void setStaffFlag(boolean staffFlag) {
        this.staffFlag = staffFlag;
    }

    /**
     * @return the calculationPeriod
     */
    public String getCalculationPeriod() {
        return calculationPeriod;
    }

    /**
     * @param calculationPeriod the calculationPeriod to set
     */
    public void setCalculationPeriod(String calculationPeriod) {
        this.calculationPeriod = calculationPeriod;
    }

    static final String toYmd(Date date) {
        if (date != null) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(YMD_FORMAT);
                return format.format(date);
            } catch (Exception e) {
                SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
                return null;
            }
        }
        return null;
    }
    
    static final Long dayDiff(Date from, Date to) {
        if (from == null || to == null) {
            return null;
        }
        // 日付をlong値に変換
        long fromTime = from.getTime();
        long toTime = to.getTime();
        long dayDiff = (toTime - fromTime) / (1000 * 60 * 60 * 24);
        
        return dayDiff;
    }
    
    /**
     * @return the targetPeriod01Flag
     */
    public boolean isTargetPeriod01Flag() {
        return targetPeriod01Flag;
    }

    /**
     * @param targetPeriod01Flag the targetPeriod01Flag to set
     */
    public void setTargetPeriod01Flag(boolean targetPeriod01Flag) {
        this.targetPeriod01Flag = targetPeriod01Flag;
    }

    /**
     * @return the targetPeriod02Flag
     */
    public boolean isTargetPeriod02Flag() {
        return targetPeriod02Flag;
    }

    /**
     * @param targetPeriod02Flag the targetPeriod02Flag to set
     */
    public void setTargetPeriod02Flag(boolean targetPeriod02Flag) {
        this.targetPeriod02Flag = targetPeriod02Flag;
    }
    
    public Map<String, List<Date>> getPeriodYmdMap() {
        return periodYmdMap;
    }

    public void setPeriodYmdMap(Map<String, List<Date>> periodYmdMap) {
        this.periodYmdMap = periodYmdMap;
    }
}
