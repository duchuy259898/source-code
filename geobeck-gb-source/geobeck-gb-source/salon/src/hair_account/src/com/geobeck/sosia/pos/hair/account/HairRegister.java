/*
 * HairRegister.java
 *
 * Created on 2007/03/26, 9:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.account;

import com.geobeck.sosia.pos.account.PrintReceipt;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.data.account.*;
import com.geobeck.sosia.pos.hair.report.util.JExcelApi;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.report.util.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.mail.*;
import com.geobeck.sosia.pos.util.*;

/**
 *
 * @author katagiri
 */
public class HairRegister extends DataRegister {

    private static final String PDF_REPORT_NAME = "Register";
    private static final String PDF_REPORT_PATH = "/report/Register.jasper";
    private static final String RECEIPT_REPORT_PATH = "/report/Receipt.jasper";
    private Integer dayCustomerCount = 0;
    private Integer dayNewCustomerCount = 0;
    private Integer dayTechCustomerCount = 0;
    private Integer dayDesignatedCustomerCount = 0;
    private Integer dayTechValue = 0;
    private Integer dayItemValue = 0;
    private Integer daychangeCancelValue = 0;
    private Integer dayContractValue = 0;
    private Integer dayContractDiscount = 0;
    private Float dayDigestionValue = 0F;
    private Integer dayAllDiscount = 0;
    private Long dayTotalValue = 0L;
    private Long dayTotalValueInTax = 0L;
    private long monthDateCount = 0l;
    private long monthCustomerCount = 0l;
    private long monthNewCustomerCount = 0l;
    private long monthTechCustomerCount = 0l;
    private long monthDesignatedCustomerCount = 0l;
    private long monthTechValue = 0l;
    private long monthItemValue = 0l;
    private long monthchangeCancelValue = 0l;
    private long monthContractValue = 0;
    private long monthContractDiscount = 0;
    private Float monthDigestionValue = 0F;
    private long monthAllDiscount = 0l;
    private long monthTotalValue = 0l;
    ///////////////////////////////////////////////////////////////
    private Integer technicValue = 0;
    private Integer technicDiscount = 0;
    private Integer itemValue = 0;
    private Integer itemDiscount = 0;
    private Integer allDiscount = 0;
    private Integer taxValue = 0;
    private Integer technicTaxValue = 0;
    private Integer itemTaxValue = 0;
    private Integer technicSales = 0;
    private Integer itemSales = 0;
    private Long totalSales = 0L;
    private Integer cashSales = 0;
    private Integer cardSales = 0;
    private Integer eCashSales = 0;
    private Integer giftSales = 0;
    private Integer cashCollect = 0;
    private Integer cardCollect = 0;
    private Integer eCashCollect = 0;
    private Integer giftCollect = 0;
    private Integer billValue = 0;
    private Integer cashInOut = 0;
    //IVS_TMTrong start add 2015/10/19 New request #43500
    private Integer  courseValue= 0;
    private Integer  courseDiscount= 0;
    private Integer  courseSales= 0;
    private Integer changeCancelCourse = 0;
    
    // 20170817 nami add start #21070 [GB���Ή�][gb] ���W���߃��[�� ���z�̏W�v�ΏہE�v�Z���ύX
    private Calendar calStart = null; //�W�v�J�n
    private Calendar calEnd = null;   //�W�v�I���
    private long dayCancelContractValue = 0l;
    private long monthCancelContractValue = 0l;
    // 20170817 nami add end #21070

    public Integer getChangeCancelCourse() {
        return changeCancelCourse;
    }

    public void setChangeCancelCourse(Integer changeCancelCourse) {
        this.changeCancelCourse = changeCancelCourse;
    }
    //IVS_TMTrong end add 2015/10/19 New request #43500
    private long monthTechnicSales = 0l;
    private long monthItemSales = 0l;
    private long monthTotalSales = 0l;
    private Double taxRate = 0d;
    private long total_payment = 0l;
    private ArrayList<MstManager> managerArray = new ArrayList<MstManager>();
    private ArrayList<DataMail> mailDatasManager = new ArrayList<DataMail>();

    public HairRegister() {
        super();
    }

    public HairRegister(MstShop shop, GregorianCalendar manageDate) {
        this();
        this.setShop(shop);
        this.setManageDate(manageDate);
        this.load();
    }

    public Integer getTechnicValue() {
        return technicValue;
    }
    
    //IVS_TMTrong start add 2015/10/19 New request #43500
    /**
     * @return the courseValue
     */
    public Integer getCourseValue() {
        return courseValue;
    }

    /**
     * @param courseValue the courseValue to set
     */
    public void setCourseValue(Integer courseValue) {
        this.courseValue = courseValue;
    }

    /**
     * @return the courseDiscount
     */
    public Integer getCourseDiscount() {
        return courseDiscount;
    }

    /**
     * @param courseDiscount the courseDiscount to set
     */
    public void setCourseDiscount(Integer courseDiscount) {
        this.courseDiscount = courseDiscount;
    }

    /**
     * @return the courseSales
     */
    public Integer getCourseSales() {
        return courseSales;
    }

    /**
     * @param courseSales the courseSales to set
     */
    public void setCourseSales(Integer courseSales) {
        this.courseSales = courseSales;
    }
    //IVS_TMTrong end add 2015/10/19 New request #43500
    
    public void setTechnicValue(Integer technicValue) {
        this.technicValue = technicValue;
    }

    public Integer getTechnicDiscount() {
        return technicDiscount;
    }

    public void setTechnicDiscount(Integer technicDiscount) {
        this.technicDiscount = technicDiscount;
    }

    public Integer getTechnicSales() {
        return technicSales;
    }

    public void setTechnicSales(Integer technicSales) {
        this.technicSales = technicSales;
    }

    public Integer getItemValue() {
        return itemValue;
    }

    public void setItemValue(Integer itemValue) {
        this.itemValue = itemValue;
    }

    public Long getTotalValue() {
        return new Long(getTechnicValue() + getItemValue());
    }

    public Integer getItemDiscount() {
        return itemDiscount;
    }

    public void setItemDiscount(Integer itemDiscount) {
        this.itemDiscount = itemDiscount;
    }

    public Integer getItemSales() {
        return itemSales;
    }

    public void setItemSales(Integer itemSales) {
        this.itemSales = itemSales;
    }

    public Integer getAllDiscount() {
        return allDiscount;
    }

    public void setAllDiscount(Integer allDiscount) {
        this.allDiscount = allDiscount;
    }

    public Long getTotalDiscount() {
        return new Long(getAllDiscount() + getTechnicDiscount() + getItemDiscount());
    }

    public Integer getTaxValue() {
        return taxValue;
    }

    public void setTaxValue(Integer taxValue) {
        this.taxValue = taxValue;
    }

    public Integer getTechnicTaxValue() {
        return technicTaxValue;
    }

    public void setTechnicTaxValue(Integer technicTaxValue) {
        this.technicTaxValue = technicTaxValue;
    }

    public Integer getItemTaxValue() {
        return itemTaxValue;
    }

    public void setItemTaxValue(Integer itemTaxValue) {
        this.itemTaxValue = itemTaxValue;
    }

    public Integer getDayCustomerCount() {
        return dayCustomerCount;
    }

    public void setDayCustomerCount(Integer customerCount) {
        this.dayCustomerCount = customerCount;
    }

    public void setDayNewCustomerCount(Integer newCustomerCount) {
        this.dayNewCustomerCount = newCustomerCount;
    }

    public Integer getDayNewCustomerCount() {
        return dayNewCustomerCount;
    }

    public Integer getDayTechCustomerCount() {
        return dayTechCustomerCount;
    }

    public void setDayTechCustomerCount(Integer dayTechCustomerCount) {
        this.dayTechCustomerCount = dayTechCustomerCount;
    }

    public Integer getDayDesignatedCustomerCount() {
        return dayDesignatedCustomerCount;
    }

    public void setDayDesignatedCustomerCount(Integer dayDesignatedCustomerCount) {
        this.dayDesignatedCustomerCount = dayDesignatedCustomerCount;
    }

    public Long getTotalSales() {
        return this.totalSales;
    }

    public void setTotalSales(Long totalSales) {
        this.totalSales = totalSales;
    }

    public Integer getCashSales() {
        return cashSales;
    }

    public void setCashSales(Integer cashSales) {
        this.cashSales = cashSales;
    }

    public Integer getCardSales() {
        return cardSales;
    }

    public void setCardSales(Integer cardSales) {
        this.cardSales = cardSales;
    }

    public Integer getECashSales() {
        return eCashSales;
    }

    public void setECashSales(Integer eCashSales) {
        this.eCashSales = eCashSales;
    }

    public Integer getGiftSales() {
        return giftSales;
    }

    public void setGiftSales(Integer giftSales) {
        this.giftSales = giftSales;
    }

    public Integer getCashCollect() {
        return cashCollect;
    }

    public void setCashCollect(Integer cashCollect) {
        this.cashCollect = cashCollect;
    }

    public Integer getCardCollect() {
        return cardCollect;
    }

    public void setCardCollect(Integer cardCollect) {
        this.cardCollect = cardCollect;
    }

    public Integer getECashCollect() {
        return eCashCollect;
    }

    public void setECashCollect(Integer eCashCollect) {
        this.eCashCollect = eCashCollect;
    }

    public Integer getGiftCollect() {
        return giftCollect;
    }

    public void setGiftCollect(Integer giftCollect) {
        this.giftCollect = giftCollect;
    }

    public Integer getCashCollectOther() {
        return this.getCardCollect() + this.getECashCollect() + this.getGiftCollect();
    }

    public Integer getBillValue() {
        return billValue;
    }

    public void setBillValue(Integer billValue) {
        this.billValue = billValue;
    }

    public Integer getCashTotal() {
        return this.getCashSales() + this.getCashCollect() + this.getCashInOut();
    }

    public long getTotal_payment() {
        return total_payment;
    }

    public void setTotal_payment(long total_payment) {
        this.total_payment = total_payment;
    }

    public Integer getLogicalValue() {
        if (SystemInfo.getDatabase().startsWith("pos_hair_lim")) {
            return this.getBaseValue();
        } else {
            return this.getBaseValue() + this.getCashTotal();
        }
    }

    public Integer getPhysicalValue() {
        return this.getTotalMoney();
    }

    public Integer getDiscrepancies() {
        return this.getPhysicalValue() - this.getLogicalValue();
    }

    public void setCashInOut(Integer cashInOut) {
        this.cashInOut = cashInOut;
    }

    public Integer getCashInOut() {
        return cashInOut;
    }

    public Integer getDayContractDiscount() {
        return dayContractDiscount;
    }

    public void setDayContractDiscount(Integer dayContractDiscount) {
        this.dayContractDiscount = dayContractDiscount;
    }

    public long getMonthContractDiscount() {
        return monthContractDiscount;
    }

    public void setMonthContractDiscount(long monthContractDiscount) {
        this.monthContractDiscount = monthContractDiscount;
    }

    public long getMonthTechnicSales() {
        return monthTechnicSales;
    }

    public void setMonthTechnicSales(long monthTechnicSales) {
        this.monthTechnicSales = monthTechnicSales;
    }

    public long getMonthItemSales() {
        return monthItemSales;
    }

    public void setMonthItemSales(long monthItemSales) {
        this.monthItemSales = monthItemSales;
    }

    public long getMonthTotalSales() {
        return monthTotalSales;
    }

    public void setMonthTotalSales(long monthTotalSales) {
        this.monthTotalSales = monthTotalSales;
    }

    public long getMonthCustomerCount() {
        return monthCustomerCount;
    }

    public void setMonthCustomerCount(long monthCustomerCount) {
        this.monthCustomerCount = monthCustomerCount;
    }

    public long getMonthNewCustomerCount() {
        return monthNewCustomerCount;
    }

    public void setMonthNewCustomerCount(long monthNewCustomerCount) {
        this.monthNewCustomerCount = monthNewCustomerCount;
    }

    public long getMonthTechCustomerCount() {
        return monthTechCustomerCount;
    }

    public void setMonthTechCustomerCount(long monthTechCustomerCount) {
        this.monthTechCustomerCount = monthTechCustomerCount;
    }

    public long getMonthDesignatedCustomerCount() {
        return monthDesignatedCustomerCount;
    }

    public void setMonthDesignatedCustomerCount(long monthDesignatedCustomerCount) {
        this.monthDesignatedCustomerCount = monthDesignatedCustomerCount;
    }

    public Integer getDayTechValue() {
        return dayTechValue;
    }

    public void setDayTechValue(Integer dayTechValue) {
        this.dayTechValue = dayTechValue;
    }

    public Integer getDayItemValue() {
        return dayItemValue;
    }

    public void setDayItemValue(Integer dayItemValue) {
        this.dayItemValue = dayItemValue;
    }

    public Integer getDayContractValue() {
        return dayContractValue;
    }

    public void setDayContractValue(Integer dayContractValue) {
        this.dayContractValue = dayContractValue;
    }

  
    public Float getDayDigestionValue() {
        return dayDigestionValue;
    }

    public void setDayDigestionValue(Float dayDigestionValue) {
        this.dayDigestionValue = dayDigestionValue;
    }

    public Integer getDayAllDiscount() {
        return dayAllDiscount;
    }

    public void setDayAllDiscount(Integer dayAllDiscount) {
        this.dayAllDiscount = dayAllDiscount;
    }

    public Long getDayTotalValue() {
        return dayTotalValue;
    }

    public void setDayTotalValue(Long dayTotalValue) {
        this.dayTotalValue = dayTotalValue;
    }

    public long getMonthDateCount() {
        return monthDateCount;
    }

    public void setMonthDateCount(long monthDateCount) {
        this.monthDateCount = monthDateCount;
    }

    public long getMonthTechValue() {
        return monthTechValue;
    }

    public void setMonthTechValue(long monthTechValue) {
        this.monthTechValue = monthTechValue;
    }

    public long getMonthItemValue() {
        return monthItemValue;
    }

    public void setMonthItemValue(long monthItemValue) {
        this.monthItemValue = monthItemValue;
    }

    public long getMonthContractValue() {
        return monthContractValue;
    }

    public void setMonthContractValue(long monthContractValue) {
        this.monthContractValue = monthContractValue;
    }
    

    public Float getMonthDigestionValue() {
        return monthDigestionValue;
    }

    public void setMonthDigestionValue(Float monthDigestionValue) {
        this.monthDigestionValue = monthDigestionValue;
    }

    public long getMonthAllDiscount() {
        return monthAllDiscount;
    }

    public void setMonthAllDiscount(long monthAllDiscount) {
        this.monthAllDiscount = monthAllDiscount;
    }

    public long getMonthTotalValue() {
        return monthTotalValue;
    }

    public void setMonthTotalValue(long monthTotalValue) {
        this.monthTotalValue = monthTotalValue;
    }

    public Long getDayTotalValueInTax() {
        return dayTotalValueInTax;
    }

    public void setDayTotalValueInTax(Long dayTotalValueInTax) {
        this.dayTotalValueInTax = dayTotalValueInTax;
    }
    public Integer getDaychangeCancelValue() {
        return daychangeCancelValue;
    }

    public void setDaychangeCancelValue(Integer daychangeCancelValue) {
        this.daychangeCancelValue = daychangeCancelValue;
    }

    public long getMonthchangeCancelValue() {
        return monthchangeCancelValue;
    }

    public void setMonthchangeCancelValue(long monthchangeCancelValue) {
        this.monthchangeCancelValue = monthchangeCancelValue;
    }
    
    // 20170817 nami add start #21070 [GB���Ή�][gb] ���W���߃��[�� ���z�̏W�v�ΏہE�v�Z���ύX
    public void setDayCancelContractValue(long dayCancelContractValue) {
        this.dayCancelContractValue = dayCancelContractValue;
    }
    public long getDayCancelContractValue() {
        return this.dayCancelContractValue;
    }
    public void setMonthCancelContractValue(long monthCancelContractValue) {
        this.monthCancelContractValue = monthCancelContractValue;
    }
    public long getMonthCancelContractValue() {
        return this.monthCancelContractValue;
    }
    // 20170817 nami add end #21070
    
    public boolean load() {
        boolean result = false;

        this.clear();

        try {

            ConnectionWrapper con = SystemInfo.getConnection();

            this.loadSales(con);
            this.loadPayment(con);
            super.load(con);
            this.cashInOut(con);

            result = true;

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return result;
    }

    private boolean setDaySales() {
        try {
            
            // 20170821 nami add srat #21070 [GB���Ή�][gb] ���W���߃��[�� ���z�̏W�v�ΏہE�v�Z���ύX
            long tmpDayTotalValueInTax = 0l;
            Integer contract_val = 0;
            Integer change_val   = 0;
            Integer cancel_val   = 0;
            // 20170821 nami add end #21070

            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(this.getSalesSQL(false));
            if (rs.next()) {
               //Luc start edit 20151201 #44895
                //this.setDayCustomerCount(rs.getInt("totalCount"));
                // 20170817 nami del start #21070 [GB���Ή�][gb] ���W���߃��[�� ���z�̏W�v�ΏہE�v�Z���ύX
                //                if((SystemInfo.getCurrentShop().getCourseFlag()!=null) && (SystemInfo.getCurrentShop().getCourseFlag()==1)) {
                //                    this.setDayCustomerCount(rs.getInt("tech_only_count") + rs.getInt("tech_and_item_count") + rs.getInt("item_only_count") +  rs.getInt("course_and_degestion_count"));
                //                }else {
                //                    this.setDayCustomerCount(rs.getInt("tech_only_count") + rs.getInt("tech_and_item_count") + rs.getInt("item_only_count"));
                //                }
                // 20170817 nami del start #21070
                //Luc end edit 20151201 #44895
                this.setDayNewCustomerCount(rs.getInt("newCount"));
                this.setDayDesignatedCustomerCount(rs.getInt("designated_count"));
                this.setDayTechCustomerCount(rs.getInt("tech_only_count") + rs.getInt("tech_and_item_count"));
                this.setDayTechValue(rs.getInt("techValue"));
                this.setDayItemValue(rs.getInt("itemValue"));
                //this.setDaychangeCancelValue(rs.getInt("change_cancel")); //20170818 del #21070
                this.setDayDigestionValue(rs.getFloat("digestionValue"));
                this.setDayAllDiscount(rs.getInt("discountValue"));
                //nhanvt start edit 20150120 Bug #34974
                //this.setDayTotalValue(rs.getLong("totalValue"));
                //nhanvt start edit 20150202 Bug #35146 
                this.setDayTotalValue(rs.getLong("techValue") + rs.getLong("itemValue")-rs.getInt("discountValue"));
                //nhanvt end edit 20150202 Bug #35146 
                //nhanvt end edit 20150120 Bug #34974
                // 20170821 nami add srat #21070 [GB���Ή�][gb] ���W���߃��[�� ���z�̏W�v�ΏہE�v�Z���ύX
                //this.setDayTotalValueInTax(rs.getLong("totalValueInTax"));
                tmpDayTotalValueInTax = rs.getLong("totalValueInTax"); //�_��ύX�ȊO�̔�����z�i�ō��j
                // 20170821 nami add end #21070
            }
            
            // 20170818 nami edit start #21070 [GB���Ή�][gb] ���W���߃��[�� ���z�̏W�v�ΏہE�v�Z���ύX
            //rs = SystemInfo.getConnection().executeQuery(this.getSalesSQL_ContractValue(false));
            rs = SystemInfo.getConnection().executeQuery(this.getCourseContractSQL(false));
            if (rs.next()) {
                //this.setDayContractValue(rs.getInt("total_contract_value"));
                contract_val = rs.getInt("total_contract_value"); //�_����z
            }
            rs = SystemInfo.getConnection().executeQuery(this.getChangeContractSQL(false));
            if (rs.next()) {
                change_val = rs.getInt("total_change_contract_value"); //�_��ύX���z�i�ύX���̐������z�j
                tmpDayTotalValueInTax = tmpDayTotalValueInTax + rs.getLong("total_change_contract_value_in_tax"); //�ō��̌_��ύX���z
            }
            rs = SystemInfo.getConnection().executeQuery(this.getCancelContractSQL(false));
            if (rs.next()) {
                cancel_val = rs.getInt("total_cancel_contract_value"); // �����z
            }
            this.setDayCancelContractValue(cancel_val);
            this.setDayContractValue(contract_val);                //[�R�[�X�_��]
            this.setDaychangeCancelValue(cancel_val + change_val); //[���ύX���z]
            // 20170818 nami edit end #21070

            rs = SystemInfo.getConnection().executeQuery(this.getDiscountAllForCourse(false));
            if (rs.next()) {
                this.setDayContractDiscount(rs.getInt("discountValue"));
            }
            
            // 20170817 nami add start #21070 [GB���Ή�][gb] ���W���߃��[�� ���z�̏W�v�ΏہE�v�Z���ύX
            rs = SystemInfo.getConnection().executeQuery(this.getTotalSlipCountSQL(false));
            if (rs.next()) {
                // ���q���i���`�[�����j�ݒ�
                this.setDayCustomerCount(rs.getInt("total_count"));
            }
            
            // ������i�ō��j
            this.setDayTotalValueInTax(tmpDayTotalValueInTax);
            // 20170817 nami add end #21070

            rs.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }

        return true;
    }

    private boolean loadSales(ConnectionWrapper con) throws SQLException {
        boolean result = false;

        String shopId = SQLUtil.convertForSQL(this.getShopID());
        String salesDate = SQLUtil.convertForSQLDateOnly(this.getManageDate());

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select" + "\n");
        sql.append("      sum(case when product_division in (1,3) then detail_value_in_tax else 0 end) as technicvalue" + "\n");
        //sql.append("     ,sum(case when product_division in (1,3) then detail_value_in_tax - discount_detail_value_in_tax else 0 end) as technicdiscount" + "\n");
        sql.append("     ,sum(case when product_division in (1,3) then discount_detail_value_in_tax else 0 end) as technicsales" + "\n");
        sql.append("     ,sum(case when product_division in (2,4) then detail_value_in_tax else 0 end) as itemvalue" + "\n");
        //sql.append("     ,sum(case when product_division in (2,4) then detail_value_in_tax - discount_detail_value_in_tax else 0 end) as itemdiscount" + "\n");
        sql.append("     ,sum(case when product_division in (2,4) then discount_detail_value_in_tax else 0 end) as itemsales" + "\n");
        //IVS_TMTrong start add 2015/10/19 New request #43500
        sql.append("     ,sum(case when product_division in (5) then detail_value_in_tax else 0 end) as courseValue" + "\n");
        //IVS_TMTrong start add 2015/10/22 New request #43500
        //sql.append("     ,sum(case when product_division in (5) then dsd.discount_value else 0 end) as courseDiscount" + "\n");
        //sql.append("     ,sum(case when product_division in (5) then dsd.product_value - dsd.discount_value else 0 end) as courseSales" + "\n");
        //sql.append("     ,sum(case when product_division in (5) then detail_value_in_tax - discount_detail_value_in_tax else 0 end) as courseDiscount" + "\n");
        if(SystemInfo.getAccountSetting().getDisplayPriceType()==1 && SystemInfo.getAccountSetting().getDiscountType()==1){
            // �ō��o�́��O�ŕ\�����Ŕ����z���犄��
            //IVS start edit 20220626 1�~�̍��ق�����
            sql.append("     ,sum(trunc(case when product_division in (1,3) then detail_value_in_tax - ").append(this.getTechItemCourseValueQuery()).append(" else 0 end)) as technicdiscount" + "\n");
            sql.append("     ,sum(trunc(case when product_division in (2,4) then detail_value_in_tax - ").append(this.getTechItemCourseValueQuery()).append(" else 0 end)) as itemdiscount" + "\n");
            sql.append("     ,sum(trunc(case when product_division in (5) then detail_value_in_tax - ").append(this.getTechItemCourseValueQuery()).append(" else 0 end)) as courseDiscount" + "\n");
            //IVS end edit 20220626 1�~�̍��ق�����
        } else {
            sql.append("     ,sum(case when product_division in (1,3) then detail_value_in_tax - discount_detail_value_in_tax else 0 end) as technicdiscount" + "\n");
            sql.append("     ,sum(case when product_division in (2,4) then detail_value_in_tax - discount_detail_value_in_tax else 0 end) as itemdiscount" + "\n");
            sql.append("     ,sum(case when product_division in (5) then detail_value_in_tax - discount_detail_value_in_tax else 0 end) as courseDiscount" + "\n");
        }
        sql.append("     ,sum(case when product_division in (5,8,9) then discount_detail_value_in_tax else 0 end) + \n");
        sql.append("     coalesce((\n");
        //IVS_LVTu start edit 2016/04/29 Bug #49538
//        sql.append("     select coalesce(sum(coalesce(dsd1.discount_detail_value_in_tax,0) - coalesce(dsd2.discount_detail_value_in_tax,0) + coalesce(dc1.service_charge,0)),0)\n");
//        sql.append("     from  view_data_sales_detail_valid dsd1 \n");
//        sql.append("     left join data_contract dc1 on dsd1.slip_no = dc1.slip_no and dsd1.shop_id = dc1.shop_id and dsd1.product_id = dc1.product_id \n");
//        sql.append("     left join data_contract dc2 on dc1.before_contract_shop_id = dc2.shop_id and dc1.before_contract_no = dc2.contract_no\n");
//        sql.append("     left join view_data_sales_detail_valid dsd2 on dsd2.slip_no = dc2.slip_no and dsd2.shop_id = dc2.shop_id and dsd2.product_id = dc2.product_id\n");
//        sql.append("      where \n");
//        sql.append("      dsd1.shop_id = " + shopId + "\n");
//        sql.append("      and dsd1.sales_date =  " + salesDate + "\n");
//        sql.append("      and dsd1.product_division in(7)\n");

        sql.append("      SELECT sum(\n");
        sql.append("           (SELECT SUM (coalesce(payment_value,0))\n");
        sql.append("            FROM\n");
        sql.append("              (SELECT DISTINCT dpd.slip_no,dpd.shop_id,dpd.payment_no,payment_method_id,payment_value\n");
        sql.append("               FROM data_payment_detail dpd\n");
        sql.append("               WHERE dp.shop_id = dpd.shop_id\n");
        sql.append("                 AND dp.slip_no = dpd.slip_no\n");
        sql.append("                 AND dp.payment_no = dpd.payment_no) AS t) + coalesce(dp.bill_value,0)) AS value\n");
        sql.append("      FROM view_data_sales_detail_valid dsd1\n");
        sql.append("      INNER JOIN data_payment dp ON dsd1.shop_id = dp.shop_id\n");
        sql.append("      AND dsd1.slip_no = dp.slip_no\n");
        sql.append("      WHERE dsd1.shop_id = " + shopId + "\n");
        sql.append("        AND dsd1.sales_date = " + salesDate + "\n");
        sql.append("        AND dsd1.product_division IN(7)\n");
        // 20170303 GB Start add Bug #60791�@[gb]����ꗗ��ʂƃ��W���߉�ʂł̕\�����z�s��
        sql.append("        AND dsd1.slip_detail_no = 1 \n");
        // 20170303 GB End add Bug #60791
        sql.append("     ),0) \n");
        sql.append("     as courseSales" + "\n");
        //IVS_TMTrong end add 2015/10/22 New request #43500
        //IVS_TMTrong end add 2015/10/19 New request #43500
        sql.append("     ,sum(case when product_division in (1,2,3,4,5,8,9) then discount_detail_value_in_tax end) + \n");
        sql.append("     coalesce((\n");
//        sql.append("     select coalesce(sum(coalesce(dsd1.discount_detail_value_in_tax,0) - coalesce(dsd2.discount_detail_value_in_tax,0) + coalesce(dc1.service_charge,0)),0)\n");
//        sql.append("     from  view_data_sales_detail_valid dsd1 \n");
//        sql.append("     left join data_contract dc1 on dsd1.slip_no = dc1.slip_no and dsd1.shop_id = dc1.shop_id and dsd1.product_id = dc1.product_id \n");
//        sql.append("     left join data_contract dc2 on dc1.before_contract_shop_id = dc2.shop_id and dc1.before_contract_no = dc2.contract_no\n");
//        sql.append("     left join view_data_sales_detail_valid dsd2 on dsd2.slip_no = dc2.slip_no and dsd2.shop_id = dc2.shop_id and dsd2.product_id = dc2.product_id\n");
//        sql.append("      where \n");
//        sql.append("      dsd1.shop_id = " + shopId + "\n");
//        sql.append("      and dsd1.sales_date =  " + salesDate + "\n");
//        sql.append("      and dsd1.product_division in(7)\n");
        sql.append("      SELECT sum(\n");
        sql.append("           (SELECT SUM (coalesce(payment_value,0))\n");
        sql.append("            FROM\n");
        sql.append("              (SELECT DISTINCT dpd.slip_no,dpd.shop_id,dpd.payment_no,payment_method_id,payment_value\n");
        sql.append("               FROM data_payment_detail dpd\n");
        sql.append("               WHERE dp.shop_id = dpd.shop_id\n");
        sql.append("                 AND dp.slip_no = dpd.slip_no\n");
        sql.append("                 AND dp.payment_no = dpd.payment_no) AS t) + coalesce(dp.bill_value,0)) AS value\n");
        sql.append("      FROM view_data_sales_detail_valid dsd1\n");
        sql.append("      INNER JOIN data_payment dp ON dsd1.shop_id = dp.shop_id\n");
        sql.append("      AND dsd1.slip_no = dp.slip_no\n");
        sql.append("      WHERE dsd1.shop_id = " + shopId + "\n");
        sql.append("        AND dsd1.sales_date = " + salesDate + "\n");
        sql.append("        AND dsd1.product_division IN(7)\n");
        // 20170303 GB Start add Bug #60791�@[gb]����ꗗ��ʂƃ��W���߉�ʂł̕\�����z�s��
        sql.append("        AND dsd1.slip_detail_no = 1 \n");
        // 20170303 GB End add Bug #60791
        sql.append("     ),0) \n");        
        sql.append("     as totalSales" + "\n");
        sql.append("     ,(\n");
        sql.append("     select coalesce(sum(coalesce(dsd1.discount_detail_value_in_tax,0)),0)\n");
        sql.append("     from  view_data_sales_detail_valid dsd1 \n");
        sql.append("      where \n");
        sql.append("      dsd1.shop_id = " + shopId + "\n");
        sql.append("      and dsd1.sales_date =  " + salesDate + "\n");
        sql.append("      and dsd1.product_division in(8,9)\n");
        sql.append("     ) \n");
        sql.append("     + coalesce((\n");
//        sql.append("    select coalesce(sum(coalesce(dsd1.discount_detail_value_in_tax,0) - coalesce(dsd2.discount_detail_value_in_tax,0) + coalesce(dc1.service_charge,0)),0)\n");
//        sql.append("     from  view_data_sales_detail_valid dsd1 \n");
//        sql.append("     left join data_contract dc1 on dsd1.slip_no = dc1.slip_no and dsd1.shop_id = dc1.shop_id and dsd1.product_id = dc1.product_id \n");
//        sql.append("     left join data_contract dc2 on dc1.before_contract_shop_id = dc2.shop_id and dc1.before_contract_no = dc2.contract_no\n");
//        sql.append("     left join view_data_sales_detail_valid dsd2 on dsd2.slip_no = dc2.slip_no and dsd2.shop_id = dc2.shop_id and dsd2.product_id = dc2.product_id\n");
//        sql.append("      where \n");
//        sql.append("      dsd1.shop_id = " + shopId + "\n");
//        sql.append("      and dsd1.sales_date =  " + salesDate + "\n");
//        sql.append("      and dsd1.product_division in(7)\n");
        sql.append("      SELECT sum(\n");
        sql.append("           (SELECT SUM (coalesce(payment_value,0))\n");
        sql.append("            FROM\n");
        sql.append("              (SELECT DISTINCT dpd.slip_no,dpd.shop_id,dpd.payment_no,payment_method_id,payment_value\n");
        sql.append("               FROM data_payment_detail dpd\n");
        sql.append("               WHERE dp.shop_id = dpd.shop_id\n");
        sql.append("                 AND dp.slip_no = dpd.slip_no\n");
        sql.append("                 AND dp.payment_no = dpd.payment_no) AS t) + coalesce(dp.bill_value,0)) AS value\n");
        sql.append("      FROM view_data_sales_detail_valid dsd1\n");
        sql.append("      INNER JOIN data_payment dp ON dsd1.shop_id = dp.shop_id\n");
        sql.append("      AND dsd1.slip_no = dp.slip_no\n");
        sql.append("      WHERE dsd1.shop_id = " + shopId + "\n");
        sql.append("        AND dsd1.sales_date = " + salesDate + "\n");
        sql.append("        AND dsd1.product_division IN(7)\n");
        // 20170303 GB Start add Bug #60791�@[gb]����ꗗ��ʂƃ��W���߉�ʂł̕\�����z�s��
        sql.append("        AND dsd1.slip_detail_no = 1\n");
        // 20170303 GB Start add Bug #60791
        //IVS_LVTu end edit 2016/04/29 Bug #49538
        sql.append("     ),0) as change_cancel_course \n");
        
        sql.append("     ,(" + "\n");
        sql.append("         select" + "\n");
        sql.append("             sum(salesvalid.discount_value)" + "\n");
        sql.append("         from" + "\n");
        sql.append("             view_data_sales_valid salesvalid" + "\n");
        //nhanvt start edit 20141128 Bug #33247
        
        //sql.append("             inner join mst_customer mc ON mc.customer_id = salesvalid.customer_id and mc.customer_no <> '0' and mc.delete_date is null ");
        
        sql.append("         where" + "\n");
        sql.append("                 salesvalid.shop_id = " + shopId + "\n");
        sql.append("             and salesvalid.sales_date = " + salesDate + "\n");
        sql.append("      ) as alldiscount" + "\n");
        //nhanvt end edit 20141128 Bug #33247
        //sql.append("     ,sum(discount_detail_value_in_tax - discount_detail_value_no_tax) as tax" + "\n");
        // vtbphuong start delete 20140821 Bug #30054
        sql.append("     ,(");
//        sql.append("         select");
//        sql.append("             sum(discount_sales_value_in_tax - discount_sales_value_no_tax)");
//        sql.append("         from");
//        sql.append("             (" + getViewDataSalesValidEx() + ") t");
//        sql.append("         where");
//        sql.append("             shop_id = max(dsd.shop_id)");
//        sql.append("         and sales_date = max(dsd.sales_date)");
        
////          sql.append("  select sum(discount_detail_value_in_tax - discount_detail_value_no_tax)  ");
////          sql.append("   from view_data_sales_detail_valid  ");
////          sql.append(" where " + "\n");
////          sql.append("     shop_id = " + shopId + "\n");
////          sql.append(" and sales_date = " + salesDate + "\n");
////          sql.append(" and product_division <> 6   \n");
////         // vtbphuong end delete 20140821 Bug #30054
        // vtbphuong start change 
         sql.append("  select  " + "\n");
       
        sql.append("  sum(  " + "\n");
        sql.append(" CASE " + "\n");
        sql.append("       WHEN  coalesce (a.overall_discount,0) = 0::numeric THEN discount_detail_value_in_tax - discount_detail_value_no_tax   " + "\n");
        sql.append("      WHEN a.overall_discount > 0 THEN discount_detail_value_in_tax - discount_detail_value_no_tax  - (  a.overall_discount -  trunc( ceil((a.overall_discount) / (1.0 + get_tax_rate(a.sales_date))))  )  " + "\n");
        sql.append("     ELSE NULL::numeric " + "\n");
        sql.append("   END  " + "\n");
        sql.append(" )      " + "\n");
        sql.append(" 	from (  " + "\n");
        sql.append(" (  select detailvalid.shop_id , detailvalid.slip_no , sum(detailvalid.discount_detail_value_in_tax)  as  discount_detail_value_in_tax  , sum(discount_detail_value_no_tax )  as  discount_detail_value_no_tax " + "\n");
        sql.append("  , detailvalid.sales_date  from   view_data_sales_detail_valid detailvalid  " + "\n");
        //sql.append("     inner join mst_customer mc ON mc.customer_id = detailvalid.customer_id and mc.customer_no <> '0' and mc.delete_date is null ");
        sql.append("          where  detailvalid.shop_id = " + shopId + "\n");
        sql.append(" 		and detailvalid.sales_date = " + salesDate + "\n");
        // 20170821 nami edit strat #21070 [GB���Ή�][gb] ���W���߃��[�� ���z�̏W�v�ΏہE�v�Z���ύX
        //sql.append(" 		and detailvalid.product_division <> 6  " + "\n");
        sql.append(" 		and detailvalid.product_division not in ( 6, 7 )  " + "\n");
        // 20170821 nami edit end #21070 
        sql.append(" 	group by  detailvalid.shop_id , detailvalid.slip_no ,detailvalid.sales_date   " + "\n");
        sql.append("  ) a  " + "\n");
        sql.append("        left join   " + "\n");
        sql.append("            ( select salesdetailall.shop_id , salesdetailall.slip_no  ,coalesce( salesdetailall.discount_value ,0)  as overall_discount  " + "\n");
        //nhanvt start edit 20141128 Bug #33247
        sql.append("          from  view_data_sales_detail_all salesdetailall " + "\n");
        //sql.append("     inner join mst_customer mc ON mc.customer_id = salesdetailall.customer_id and mc.customer_no <> '0'  and mc.delete_date is null ");
        sql.append("         where  salesdetailall.shop_id = " + shopId + "\n");
        sql.append(" 	and salesdetailall.sales_date = " + salesDate + "\n");
        sql.append(" 	and salesdetailall.product_division = 0   ) b using (shop_id , slip_no )   ) a    " + "\n");
        //nhanvt end edit 20141128 Bug #33247
        sql.append("      ) as tax" + "\n");

        sql.append("     ,sum(case when product_division in (1,3) then discount_detail_value_in_tax - discount_detail_value_no_tax else 0 end) as technictax" + "\n");
        sql.append("     ,sum(case when product_division in (2,4) then discount_detail_value_in_tax - discount_detail_value_no_tax else 0 end) as itemtax" + "\n");
        sql.append(" from" + "\n");
        sql.append("     view_data_sales_detail_valid dsd" + "\n");
        //nhanvt start edit 20141128 Bug #33247
        //sql.append("     inner join mst_customer mc ON mc.customer_id = dsd.customer_id and mc.customer_no <> '0'  and mc.delete_date is null ");
        sql.append(" where" + "\n");
        sql.append("     dsd.shop_id = " + shopId + "\n");
        sql.append(" and dsd.sales_date = " + salesDate + "\n");
        //nhanvt end edit 20141128 Bug #33247
        ResultSetWrapper rs = con.executeQuery(sql.toString());
        
        Integer tmpTax = 0;
        
        if (rs.next()) {
            this.setTechnicValue(rs.getInt("technicvalue"));
            this.setTechnicDiscount(rs.getInt("technicdiscount"));
            this.setTechnicSales(rs.getInt("technicsales"));
            this.setItemValue(rs.getInt("itemvalue"));
            this.setItemDiscount(rs.getInt("itemdiscount"));
            this.setItemSales(rs.getInt("itemsales"));
            this.setAllDiscount(rs.getInt("alldiscount"));
            // 20170821 nami edit start #21070 [GB���Ή�][gb] ���W���߃��[�� ���z�̏W�v�ΏہE�v�Z���ύX
            //this.setTotalSales(rs.getLong("totalSales") - rs.getLong("alldiscount"));
            //this.setTaxValue(rs.getInt("tax"));
            tmpTax = rs.getInt("tax"); //�����ƕύX�ȊO�̏����
            // 20170821 nami edit end #21070
            this.setTechnicTaxValue(rs.getInt("technictax"));
            this.setItemTaxValue(rs.getInt("itemtax"));
            //IVS_TMTrong start add 2015/10/19 New request #43500
            this.setCourseValue(rs.getInt("courseValue"));
            this.setCourseDiscount(rs.getInt("courseDiscount"));
            // 20170821 nami del start #21070 [GB���Ή�][gb] ���W���߃��[�� ���z�̏W�v�ΏہE�v�Z���ύX
            //this.setCourseSales(rs.getInt("courseSales"));
            //this.setChangeCancelCourse(rs.getInt("change_cancel_course"));
            // 20170821 nami del end #21070
            //IVS_TMTrong end add 2015/10/19 New request #43500

            result = true;
        }
        
        // 20170821 nami add start #21070 [GB���Ή�][gb] ���W���߃��[�� ���z�̏W�v�ΏہE�v�Z���ύX
        Integer tmpChangeVal = 0;
        Integer tmpCancelVal = 0;
        Integer tmpDiscountCourseVal = 0;
        // �_��ύX���z�擾
        rs = con.executeQuery(this.getChangeContractSQL(false).toString());
        if(rs.next()) {
            tmpChangeVal = rs.getInt("total_change_contract_value_in_tax");
            tmpTax       = tmpTax + rs.getInt("change_contract_tax"); //�_��ύX�̏����
        }
        // �����z�擾
        rs = con.executeQuery(this.getCancelContractSQL(false));
        if(rs.next()) {
            tmpCancelVal = rs.getInt("total_cancel_contract_value_in_tax");
        }
        
        // �_��ύX�{�����z�ݒ�
        this.setChangeCancelCourse(tmpChangeVal + tmpCancelVal);
        
        // �_��g�[�^�����z�ݒ�i�_��{�ύX�{���j
        tmpDiscountCourseVal = this.getCourseValue() - this.getCourseDiscount();
        this.setCourseSales(tmpDiscountCourseVal + this.getChangeCancelCourse());
        
        // ��������z�ݒ�
        this.setTotalSales((long)(this.getTechnicSales() + this.getItemSales() + this.getCourseSales() - this.getAllDiscount()));
        
        // ����Őݒ�
        this.setTaxValue(tmpTax);
        // 20170821 nami add end #21070

        rs.close();

        return result;
    }

    private boolean loadPayment(ConnectionWrapper con) throws SQLException {
        boolean result = false;

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      sum(case dp.payment_no when 0 then dpd.cash_value - (case when dp.change_value < 0 then 0 else dp.change_value end) else 0 end) as cash_sales");
        sql.append("     ,sum(case dp.payment_no when 0 then dpd.card_value else 0 end) as card_sales");
        sql.append("     ,sum(case dp.payment_no when 0 then dpd.ecash_value else 0 end) as ecash_sales");
        sql.append("     ,sum(case dp.payment_no when 0 then dpd.gift_value else 0 end) as gift_sales");

        sql.append("     ,sum(case dp.payment_no when 0 then 0 else dpd.cash_value - (case when dp.change_value < 0 then 0 else dp.change_value end) end) as cash_collect");
        sql.append("     ,sum(case dp.payment_no when 0 then 0 else dpd.card_value end) as card_collect");
        sql.append("     ,sum(case dp.payment_no when 0 then 0 else dpd.ecash_value end) as ecash_collect");
        sql.append("     ,sum(case dp.payment_no when 0 then 0 else dpd.gift_value end) as gift_collect");

        sql.append("     ,sum(case dp.payment_no when 0 then dp.bill_value else 0 end) as bill_value");
        sql.append(" from");
        sql.append("     data_payment dp");
        sql.append("         left outer join");
        sql.append("             (");
        sql.append("                 select");
        sql.append("                      dpd.shop_id");
        sql.append("                     ,dpd.slip_no");
        sql.append("                     ,dpd.payment_no");
        sql.append("                     ,sum(case mpm.payment_class_id when 1 then dpd.payment_value else 0 end) as cash_value");
        sql.append("                     ,sum(case mpm.payment_class_id when 2 then dpd.payment_value else 0 end) as card_value");
        sql.append("                     ,sum(case mpm.payment_class_id when 3 then dpd.payment_value else 0 end) as ecash_value");
        sql.append("                     ,sum(case mpm.payment_class_id when 4 then dpd.payment_value else 0 end) as gift_value");
        sql.append("                 from");
        sql.append("                     data_payment_detail dpd");
        sql.append(" 				        inner join mst_payment_method mpm");
        sql.append(" 				                on mpm.payment_method_id = dpd.payment_method_id");
        sql.append(" 				        inner join data_payment dp");
        sql.append(" 				                on dpd.shop_id = dp.shop_id");
        sql.append("                                and dpd.slip_no = dp.slip_no");
        sql.append("                                and dpd.payment_no = dp.payment_no");
        sql.append("                                and dp.shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
        sql.append("                                and dp.payment_date = " + SQLUtil.convertForSQLDateOnly(this.getManageDate()));
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
        sql.append(" where");
        sql.append("         dp.delete_date is null");
        sql.append("     and dp.shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
        sql.append("     and dp.payment_date = " + SQLUtil.convertForSQLDateOnly(this.getManageDate()));
        sql.append("     and exists");
        sql.append("         (");
        sql.append("             select 1");
        sql.append("             from");
        sql.append("                 view_data_sales_valid salesvalid ");
        //nhanvt start edit 20141128 Bug #33247
        //sql.append("             inner join mst_customer mc ON mc.customer_id = salesvalid.customer_id and mc.customer_no <> '0'  and mc.delete_date is null ");
        sql.append("             where");
        sql.append("                     salesvalid.shop_id = dp.shop_id");
        sql.append("                 and salesvalid.slip_no = dp.slip_no");
        sql.append("                 and salesvalid.sales_date is not null");
        sql.append("         )");
        //nhanvt end edit 20141128 Bug #33247

        ResultSetWrapper rs = con.executeQuery(sql.toString());

        if (rs.next()) {
            this.setCashSales(rs.getInt("cash_sales"));
            this.setCardSales(rs.getInt("card_sales"));
            this.setECashSales(rs.getInt("ecash_sales"));
            this.setGiftSales(rs.getInt("gift_sales"));

            this.setCashCollect(rs.getInt("cash_collect"));
            this.setCardCollect(rs.getInt("card_collect"));
            this.setECashCollect(rs.getInt("ecash_collect"));
            this.setGiftCollect(rs.getInt("gift_collect"));

            this.setBillValue(rs.getInt("bill_value"));

            result = true;
        }

        rs.close();

        return result;
    }

    private boolean cashInOut(ConnectionWrapper con) throws SQLException {
        boolean result = false;

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append(" sum (case when in_out = true   then io_value else 0 end) as In ,\n ");
        sql.append(" sum (case when in_out = false  then io_value else 0 end) as Out \n ");
        sql.append(" from data_cash_io \n");
        sql.append(" where\n");
        sql.append(" delete_date is null\n");
        sql.append(" and shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
        sql.append(" and io_date = " + SQLUtil.convertForSQLDateOnly(this.getManageDate()));

        ResultSetWrapper rs = con.executeQuery(sql.toString());
        if (rs.next()) {
            this.setCashInOut(rs.getInt("In") - rs.getInt("Out"));
            result = true;
        }
        rs.close();
        return result;
    }

    public boolean regist() {
        ConnectionWrapper con = SystemInfo.getConnection();

        boolean result = false;

        try {
            con.begin();
            result = super.regist(con);
            if (result) {
                con.commit();
            } else {
                con.rollback();
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return result;
    }

    public void print() {
        /*
         HashMap<String, Object>		param	=	new HashMap<String, Object>();
		
         param.put("shopName", this.getShop().getShopName());
         param.put("manageDate", String.format("%1$tY�N%1$tm��%1$td��", this.getManageDate()));
		
         this.putSalesData(param);
         this.putPaymentData(param);
         this.putMoneyData(param);
		
         param.put("cashInOut",this.getCashInOut());
	
         InputStream report = HairRegister.class.getResourceAsStream(PDF_REPORT_PATH);
         String fileName = PDF_REPORT_NAME + String.format("%1$tY%1$tm%1$td%2$ts", this.getManageDate(), new java.util.Date());
		
         Vector<Integer>	temp = new Vector<Integer>();
         temp.add(0);
         ReportManager.exportReport(report, fileName, ReportManager.PDF_FILE, param, temp);
         */
        JExcelApi jx = null;
         if((SystemInfo.getCurrentShop().getCourseFlag()!=null) && (SystemInfo.getCurrentShop().getCourseFlag()==1)) {
            jx =  new JExcelApi("���W�m�F_�_��");
            jx.setTemplateFile("/report/���W�m�F_�_��.xls");
         }else {
            jx = new JExcelApi("���W�m�F");
            jx.setTemplateFile("/report/���W�m�F.xls");
         }
        // �w�b�_�o��
        jx.setValue(6, 3, this.getShop().getShopName());
        jx.setValue(6, 4, String.format("%1$tY�N%1$tm��%1$td��", this.getManageDate()));
        
        jx.setValue(6, 5, this.getStaff().getStaffName(0) + " " + this.getStaff().getStaffName(1));
        // ������
        jx.setValue(10, 9, this.getTechnicValue());
        jx.setValue(10, 10, this.getTechnicDiscount());
        jx.setValue(10, 11, this.getTechnicSales());
        jx.setValue(10, 12, this.getItemValue());
        jx.setValue(10, 13, this.getItemDiscount());
        jx.setValue(10, 14, this.getItemSales());
        if((SystemInfo.getCurrentShop().getCourseFlag()!=null) && (SystemInfo.getCurrentShop().getCourseFlag()==1)) {
            jx.setValue(10, 15, this.getCourseValue());
            jx.setValue(10, 16, this.getCourseDiscount());
            jx.setValue(10, 17, this.getChangeCancelCourse());
            jx.setValue(10, 18, this.getCourseSales());
            jx.setValue(10, 19, this.getAllDiscount());
            jx.setValue(10, 20, this.getTaxValue());
            jx.setValue(10, 21, this.getTotalSales());
        }else{
            jx.setValue(10, 15, this.getAllDiscount());
            jx.setValue(10, 16, this.getTaxValue());
            jx.setValue(10, 17, this.getTotalSales());
        }
        // ���W������
        if((SystemInfo.getCurrentShop().getCourseFlag()!=null) && (SystemInfo.getCurrentShop().getCourseFlag()==1)) {
            jx.setValue(7, 25, this.getMoney().get(0));
            jx.setValue(7, 26, this.getMoney().get(1));
            jx.setValue(7, 27, this.getMoney().get(2));
            jx.setValue(7, 28, this.getMoney().get(3));
            jx.setValue(7, 29, this.getMoney().get(4));
            jx.setValue(7, 30, this.getMoney().get(5));
            jx.setValue(7, 31, this.getMoney().get(6));
            jx.setValue(7, 32, this.getMoney().get(7));
            jx.setValue(7, 33, this.getMoney().get(8));
            jx.setValue(7, 34, this.getMoney().get(9));
        }else {
            jx.setValue(7, 21, this.getMoney().get(0));
            jx.setValue(7, 22, this.getMoney().get(1));
            jx.setValue(7, 23, this.getMoney().get(2));
            jx.setValue(7, 24, this.getMoney().get(3));
            jx.setValue(7, 25, this.getMoney().get(4));
            jx.setValue(7, 26, this.getMoney().get(5));
            jx.setValue(7, 27, this.getMoney().get(6));
            jx.setValue(7, 28, this.getMoney().get(7));
            jx.setValue(7, 29, this.getMoney().get(8));
            jx.setValue(7, 30, this.getMoney().get(9));
        }

        // �������
        jx.setValue(26, 9, this.getCashSales());
        jx.setValue(26, 10, this.getCardSales());
        jx.setValue(26, 11, this.getECashSales());
        jx.setValue(26, 12, this.getGiftSales());
        jx.setValue(26, 13, this.getBillValue());
        jx.setValue(26, 14, this.getCashCollect());
        jx.setValue(26, 15, this.getCashCollectOther());

        // ���W���ߕs�����
        if((SystemInfo.getCurrentShop().getCourseFlag()!=null) && (SystemInfo.getCurrentShop().getCourseFlag()==1)) {
            jx.setValue(26, 22, this.getBaseValue());
            jx.setValue(26, 23, this.getCashTotal());
            jx.setValue(26, 24, this.getCashInOut());
            jx.setValue(26, 25, this.getLogicalValue());
            jx.setValue(26, 26, this.getPhysicalValue());
            jx.setValue(26, 27, this.getDiscrepancies());
            jx.setValue(18, 30, this.getComment());
            
        }else {
            jx.setValue(26, 21, this.getBaseValue());
            jx.setValue(26, 22, this.getCashTotal());
            jx.setValue(26, 23, this.getCashInOut());
            jx.setValue(26, 24, this.getLogicalValue());
            jx.setValue(26, 25, this.getPhysicalValue());
            jx.setValue(26, 26, this.getDiscrepancies());
            jx.setValue(18, 29, this.getComment());
        }
        
        jx.openWorkbook();
    }

    public void putSalesData(HashMap<String, Object> param) {
        param.put("technicValue", this.getTechnicValue());
        param.put("technicDiscount", this.getTechnicDiscount());
        param.put("technicSales", this.getTechnicSales());

        param.put("itemValue", this.getItemValue());
        param.put("itemDiscount", this.getItemDiscount());
        param.put("itemSales", this.getItemSales());

        param.put("allDiscount", this.getAllDiscount());
        param.put("taxValue", this.getTaxValue());
        param.put("totalSales", this.getTotalSales());
    }

    public void putPaymentData(HashMap<String, Object> param) {
        param.put("cashPayment", this.getCashSales());
        param.put("cardPayment", this.getCardSales());
        param.put("eCashPayment", this.getECashSales());
        param.put("giftPayment", this.getGiftSales());
        param.put("cashCollect", this.getCashCollect());
        param.put("cashCollectOther", this.getCashCollectOther());
        param.put("billValue", this.getBillValue());
    }

    public void putMoneyData(HashMap<String, Object> param) {
        param.put("baseValue", this.getBaseValue());
        param.put("moneyNumber", this.getMoney());
    }

    /**
     * ���W�Ǘ���񂪑��M����Ă��邩�B
     *
     * @return �����M���͖����M�c�Ɠ��A���M����Ă����null
     */
    public java.sql.Date checkMailSend() {
        ConnectionWrapper con = SystemInfo.getConnection();

        java.sql.Date result = null;

        String sql = "";
        sql = "SELECT manage_date  " + "\n"
                + "FROM data_register " + "\n"
                + "where manage_date =( " + "\n"
                + "        SELECT MAX(manage_date)  " + "\n"
                + "          FROM data_register " + "\n"
                + "          where delete_date IS NULL " + "\n"
                + "          and manage_date <= '" + SystemInfo.getSystemDate() + "'" + "\n"
                + "          and shop_id = '" + SystemInfo.getCurrentShop().getShopID() + "') " + "\n"
                + "and send_end_flg = 0 ";

        try {
            ResultSetWrapper rs = con.executeQuery(sql);
            if (rs.next()) {
                result = rs.getDate("manage_date");
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return result;
    }

    public boolean sendMailEndFlgOn(ConnectionWrapper con, GregorianCalendar date) {
        boolean result = false;

        try {
            String sql = "UPDATE data_register SET send_end_flg = 1 WHERE manage_date = '"
                    + String.format("%1$tY%1$tm%1$td", date) + "'";
            result = (con.executeUpdate(sql) == 1);
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return result;
    }

    //���[�����M
    //nhanvt start edit 20141209 New request #34634
    public boolean sendMail(ConnectionWrapper con) throws Exception {

        boolean result = false;

        // �e�X�g�p�̃R�[�h�����s����Ƃ��̓R�����g�A�E�g���邱�ƁI�I�I�I�I
        this.setManagerArrayList();

        /*
         // �e�X�g�p
         managerArray.clear();
         MstManager mm = null;
         mm = new MstManager();
         mm.setMailAddress("sakaue@geobeck.com");
         managerArray.add(mm);
         //mm = new MstManager();
         //mm.setMailAddress("danura@geobeck.com");
         //managerArray.add(mm);
         */

        this.setMailDatas(managerArray);
        
        this.setDataCollectionPeriod(); //20170818 nami add #21070
        
        this.setDaySales();
        this.setMonthSales();

        this.setRegisterMailData(true);

        result = MailUtil.sendMails(mailDatasManager,con);
        return result;
    }
    //nhanvt end edit 20141209 New request #34634

    /**
     * ���[���f�[�^���擾����B
     */
    public ArrayList<DataMail> getMailDatas() {
        return mailDatasManager;
    }

    /**
     * ���[���f�[�^��ݒ肷��B
     */
    public void setMailDatas(ArrayList<MstManager> manager) {
        mailDatasManager.clear();
        System.out.println("�o�^���[�����@:" + manager.size());
        for (MstManager mm : manager) {
            mailDatasManager.add(new DataMail(mm));
        }
    }

    /**
     * �Ǘ��҂̃��[������ݒ肷��B
     */
    public void setRegisterMailData(boolean isDecodeKey) {

        for (DataMail dm : mailDatasManager) {
            dm.setMailTitle(createMailTitle());

            String s = createMailBody();
            s += "----------------\n";
            s += "���M�����F" + String.format("%1$tY/%1$tm/%1$td %1$tH:%1$tM:%1$tS", Calendar.getInstance()) + "\n";

            dm.setMailBody(s);
        }
    }

    private String createMailTitle() {
        return String.format("%1$tY�N%1$tm��%1$td��", this.getManageDate()) + "���W���";
    }

    private String createMailBody() {
        
        long tmpAllDiscount = 0l;
        long tmpMonthTotalValueInCourse = 0l;
        
        String stringBody = "";
        stringBody += this.getShop().getShopName() + "�̔�����ł��B\n";

        stringBody += "----------------\n";

        stringBody += "[�X��]" + this.getShop().getShopName() + "\n";
        stringBody += "[���t]" + String.format("%1$tY�N%1$tm��%1$td��", this.getManageDate()) + "\n";
        if((this.getStaff().getStaffID() != null && this.getStaff().getStaffID()>0) ) {
            stringBody += "[�񍐎�]" + this.getStaff().getStaffName(0) + " " + this.getStaff().getStaffName(1)+ "\n";
        }
        if((this.getComment() != null && !this.getComment().trim().equals(""))) {
            stringBody += "[�񍐓��e]"+ "\n";
            stringBody += this.getComment()+ "\n";
        }
        stringBody += "----------------\n";

        stringBody += "���{���̔���" + getDisplayPriceTypeName() + "\n";

        stringBody += "[�Z�p����] " + FormatUtil.decimalFormat(this.getDayTechValue()) + "�~\n";
        stringBody += "[���i����] " + FormatUtil.decimalFormat(this.getDayItemValue()) + "�~\n";
        //Luc start edit 20150212 #35202
        if((SystemInfo.getCurrentShop().getCourseFlag()!=null) && (SystemInfo.getCurrentShop().getCourseFlag()==1)) {
            stringBody += "[�R�[�X�_��] " + FormatUtil.decimalFormat(this.getDayContractValue()) + "�~\n";
            stringBody += "[���ύX���z] " + FormatUtil.decimalFormat(this.getDaychangeCancelValue()) + "�~\n";
            stringBody += "[�������z] " + FormatUtil.decimalFormat(this.getDayDigestionValue()) + "�~\n";
        }
        //Luc end edit 20150212 #35202
        stringBody += "[�S�̊���] " + FormatUtil.decimalFormat(this.getDayAllDiscount()) + "�~\n";
        stringBody += "[������] " + FormatUtil.decimalFormat(this.getDayTotalValue()) + "�~\n";
        //IVS_TMTrong start edit 20150804 Bug #41291
        //SystemInfo.getCurrentShop().getCourseFlag()==1
        if((SystemInfo.getCurrentShop().getCourseFlag()!=null) && (SystemInfo.getCurrentShop().getCourseFlag()==1)) {
        //IVS_TMTrong end edit 20150804 Bug #41291
            // 20170817 nami edit start #21070 [GB���Ή�][gb] ���W���߃��[�� ���z�̏W�v�ΏہE�v�Z���ύX
            tmpAllDiscount = this.getDayContractDiscount();
            //stringBody += "[�S�̊���(�����)] " + FormatUtil.decimalFormat(this.getDayAllDiscount()+this.getDayContractDiscount()) + "�~\n";
            stringBody += "[�S�̊���(�����)] " + FormatUtil.decimalFormat(tmpAllDiscount) + "�~\n";
            // stringBody += "[������(�����)] " + FormatUtil.decimalFormat(this.getDayTotalValue()+this.getDayContractValue()) + "�~\n";
            stringBody += "[������(�����)] " + FormatUtil.decimalFormat(this.getDayTechValue() + this.getDayItemValue() + this.getDayContractValue() + this.getDaychangeCancelValue() - tmpAllDiscount) + "�~\n";
            // 20170817 nami edit end #21070
        }
        stringBody += "[�����] " + FormatUtil.decimalFormat(this.getTaxValue()) + "�~\n";

        stringBody += "[�Z�p�q�P��] " + FormatUtil.decimalFormat(getAverage(this.getDayTechValue(), this.getDayTechCustomerCount())) + "�~\n";

        stringBody += "[���q��] " + FormatUtil.decimalFormat(this.getDayCustomerCount()) + "�l\n";
        stringBody += "[�w���q��] " + FormatUtil.decimalFormat(this.getDayDesignatedCustomerCount()) + "�l\n";
        stringBody += "[�V�K�q��] " + FormatUtil.decimalFormat(this.getDayNewCustomerCount()) + "�l\n";

        stringBody += "----------------\n";

        stringBody += "�����݌v" + getDisplayPriceTypeName() + "\n";
        stringBody += "[�c�Ɠ���] " + FormatUtil.decimalFormat(this.getMonthDateCount()) + "��\n";
        stringBody += "[�Z�p����] " + FormatUtil.decimalFormat(this.getMonthTechValue()) + "�~\n";
        stringBody += "[���i����] " + FormatUtil.decimalFormat(this.getMonthItemValue()) + "�~\n";
         //Luc start edit 20150212 #35202
        //IVS_TMTrong start edit 20150804 Bug #41291
        //SystemInfo.getCurrentShop().getCourseFlag()==1
        if((SystemInfo.getCurrentShop().getCourseFlag()!=null) && (SystemInfo.getCurrentShop().getCourseFlag()==1)) {
        //IVS_TMTrong end edit 20150804 Bug #41291
            stringBody += "[�R�[�X�_��] " + FormatUtil.decimalFormat(this.getMonthContractValue()) + "�~\n";
            stringBody += "[���ύX���z] " + FormatUtil.decimalFormat(this.getMonthchangeCancelValue()) + "�~\n";
            stringBody += "[�������z] " + FormatUtil.decimalFormat(this.getMonthDigestionValue()) + "�~\n";
        }
        //Luc end edit 20150212 #35202
        stringBody += "[�S�̊���] " + FormatUtil.decimalFormat(this.getMonthAllDiscount()) + "�~\n";
        stringBody += "[������] " + FormatUtil.decimalFormat(this.getMonthTotalValue()) + "�~\n";
        //IVS_TMTrong start edit 20150804 Bug #41291
        //SystemInfo.getCurrentShop().getCourseFlag()==1
        if((SystemInfo.getCurrentShop().getCourseFlag()!=null) && (SystemInfo.getCurrentShop().getCourseFlag()==1)) {
        //IVS_TMTrong end edit 20150804 Bug #41291
            // 20170817 nami edit start #21070 [GB���Ή�][gb] ���W���߃��[�� ���z�̏W�v�ΏہE�v�Z���ύX
            tmpAllDiscount = this.getMonthContractDiscount();
            tmpMonthTotalValueInCourse = this.getMonthTechValue()+this.getMonthItemValue()+this.getMonthContractValue()+this.getMonthchangeCancelValue()-tmpAllDiscount;
            //stringBody += "[�S�̊���(�����)] " + FormatUtil.decimalFormat(this.getMonthAllDiscount()+this.getMonthContractDiscount()) + "�~\n";
            stringBody += "[�S�̊���(�����)] " + FormatUtil.decimalFormat(tmpAllDiscount) + "�~\n";
            //stringBody += "[������(�����)] " + FormatUtil.decimalFormat(this.getMonthTotalValue()+this.getMonthContractValue()) + "�~\n";
            stringBody += "[������(�����)] " + FormatUtil.decimalFormat(tmpMonthTotalValueInCourse) + "�~\n";
        }
        stringBody += "[�Z�p�q�P��] " + FormatUtil.decimalFormat(getAverage(this.getMonthTechValue(), this.getMonthTechCustomerCount())) + "�~\n";
        stringBody += "[���q��] " + FormatUtil.decimalFormat(this.getMonthCustomerCount()) + "�l\n";
        stringBody += "[�w���q��] " + FormatUtil.decimalFormat(this.getMonthDesignatedCustomerCount()) + "�l\n";
        stringBody += "[�V�K�q��] " + FormatUtil.decimalFormat(this.getMonthNewCustomerCount()) + "�l\n";
        if (this.getShop().getCourseFlag() == 1) {
            stringBody += "[������|��] " + FormatUtil.decimalFormat(this.getTotal_payment()) + "�~\n";
        }
        stringBody += "----------------\n";

        stringBody += "������1������" + getDisplayPriceTypeName() + "\n";
        stringBody += "[�Z�p����] " + FormatUtil.decimalFormat(getDailyAverage(this.getMonthTechValue())) + "�~\n";
        stringBody += "[���i����] " + FormatUtil.decimalFormat(getDailyAverage(this.getMonthItemValue())) + "�~\n";
        //Luc start edit 20150212 #35202
        //IVS_TMTrong start edit 20150804 Bug #41291
        //SystemInfo.getCurrentShop().getCourseFlag()==1
        if((SystemInfo.getCurrentShop().getCourseFlag()!=null) && (SystemInfo.getCurrentShop().getCourseFlag()==1)) {
        //IVS_TMTrong end edit 20150804 Bug #41291
            stringBody += "[�R�[�X�_��] " + FormatUtil.decimalFormat(getDailyAverage(this.getMonthContractValue())) + "�~\n";
            stringBody += "[�������z] " + FormatUtil.decimalFormat(getDailyAverage(this.getMonthDigestionValue().longValue())) + "�~\n";
        }
        //Luc end edit 20150212 #35202
        stringBody += "[�S�̊���] " + FormatUtil.decimalFormat(getDailyAverage(this.getMonthAllDiscount())) + "�~\n";
        stringBody += "[������] " + FormatUtil.decimalFormat(getDailyAverage(this.getMonthTotalValue())) + "�~\n";
        //IVS_TMTrong start edit 20150804 Bug #41291
        //SystemInfo.getCurrentShop().getCourseFlag()==1
        if((SystemInfo.getCurrentShop().getCourseFlag()!=null) && (SystemInfo.getCurrentShop().getCourseFlag()==1)) {
            // 20170817 nami edit start #21070 [GB���Ή�][gb] ���W���߃��[�� ���z�̏W�v�ΏہE�v�Z���ύX
            //IVS_TMTrong end edit 20150804 Bug #41291
            //stringBody += "[�S�̊���(�����)] " + FormatUtil.decimalFormat(getDailyAverage(this.getMonthAllDiscount()+this.getMonthContractDiscount()))+ "�~\n";
            stringBody += "[�S�̊���(�����)] " + FormatUtil.decimalFormat(getDailyAverage(tmpAllDiscount))+ "�~\n";
            //Luc start 20151201 #44895
            // stringBody += "[������(�����)] " + FormatUtil.decimalFormat(getDailyAverage(this.getMonthTotalValue()+this.getMonthContractValue()-this.getMonthContractDiscount())) + "�~\n";
            //stringBody += "[������(�����)] " + FormatUtil.decimalFormat(getDailyAverage(this.getMonthTotalValue()+this.getMonthContractValue())) + "�~\n";
            stringBody += "[������(�����)] " + FormatUtil.decimalFormat(getDailyAverage(tmpMonthTotalValueInCourse)) + "�~\n";
            //Luc start 20151201  #44895
            // 20170817 nami edit end #21070
        }
        stringBody += "[���q��] " + FormatUtil.decimalFormat(getDailyAverage(this.getMonthCustomerCount())) + "�l\n";
        stringBody += "[�w���q��] " + FormatUtil.decimalFormat(getDailyAverage(this.getMonthDesignatedCustomerCount())) + "�l\n";
        stringBody += "[�V�K�q��] " + FormatUtil.decimalFormat(getDailyAverage(this.getMonthNewCustomerCount())) + "�l\n";

        stringBody += "----------------\n";

        stringBody += "�����W���i�ō��j\n";
        stringBody += "[������] " + FormatUtil.decimalFormat(this.getDayTotalValueInTax()) + "�~\n";
        stringBody += "[����] " + FormatUtil.decimalFormat(this.getCashSales()) + "�~\n";
        stringBody += "[�J�[�h] " + FormatUtil.decimalFormat(this.getCardSales()) + "�~\n";
        stringBody += "[�d�q�}�l�[] " + FormatUtil.decimalFormat(this.getECashSales()) + "�~\n";
        stringBody += "[���̑�] " + FormatUtil.decimalFormat(this.getGiftSales()) + "�~\n";
        stringBody += "[���|��] " + FormatUtil.decimalFormat(this.getBillValue()) + "�~\n";
        stringBody += "\n";
        stringBody += "[������|����] " + FormatUtil.decimalFormat(this.getCashCollect()) + "�~\n";
        stringBody += "[������|�����ȊO] " + FormatUtil.decimalFormat(this.getCashCollectOther()) + "�~\n";
        stringBody += "[������|���v] " + FormatUtil.decimalFormat(this.getCashCollect() + this.getCashCollectOther()) + "�~\n";
        stringBody += "\n";
        stringBody += "[������] " + FormatUtil.decimalFormat(this.getBaseValue()) + "�~\n";
        stringBody += "[�������] " + FormatUtil.decimalFormat(this.getCashTotal()) + "�~\n";
        stringBody += "[���W���o��] " + FormatUtil.decimalFormat(this.getCashInOut()) + "�~\n";
        stringBody += "[�_�����W��] " + FormatUtil.decimalFormat(this.getLogicalValue()) + "�~\n";
        stringBody += "[�������W��] " + FormatUtil.decimalFormat(this.getPhysicalValue()) + "�~\n";
        stringBody += "\n";
        stringBody += "[�ߕs�����z] " + FormatUtil.decimalFormat(this.getDiscrepancies()) + "�~\n";

        return stringBody;
    }

    private String getDisplayPriceTypeName() {

        if (SystemInfo.getAccountSetting().getDisplayPriceType() == 1) {
            return "�i�Ŕ��j";
        } else {
            return "�i�ō��j";
        }

    }

    private long getDailyAverage(long value) {
        return getAverage(value, this.getMonthDateCount());
    }

    private long getAverage(long value1, long value2) {
        return value2 == 0 ? 0 : value1 / value2;
    }

    private boolean setManagerArrayList() {
        managerArray.clear();

        try {
            ConnectionWrapper con = SystemInfo.getConnection();

            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select *");
            sql.append(" from mst_manager");
            sql.append(" where	shop_id = " + SystemInfo.getCurrentShop().getShopID());
            sql.append("  and  mail_division = 1");
            sql.append("  and  delete_date is null");

            ResultSetWrapper rs = con.executeQuery(sql.toString());

            while (rs.next()) {
                MstManager mm = new MstManager();

                mm.setMailAddress(rs.getString("mail_address"));

                managerArray.add(mm);
            }

            rs.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
        return true;
    }

    private boolean setMonthSales() {
        try {

            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(this.getSalesSQL(true));
            if (rs.next()) {
                //Luc start edit 20151201 #44895
                //this.setMonthCustomerCount(rs.getLong("totalCount"));
                // 20170817 nami del start #21070 [GB���Ή�][gb] ���W���߃��[�� ���z�̏W�v�ΏہE�v�Z���ύX
                //                 if((SystemInfo.getCurrentShop().getCourseFlag()!=null) && (SystemInfo.getCurrentShop().getCourseFlag()==1)) {
                //                    this.setMonthCustomerCount(rs.getLong("tech_only_count")+ rs.getLong("tech_and_item_count") + rs.getLong("item_only_count") + rs.getLong("course_and_degestion_count"));
                //                 }else {
                //                    this.setMonthCustomerCount(rs.getLong("tech_only_count")+ rs.getLong("tech_and_item_count") + rs.getLong("item_only_count") );
                //                 }
                 // 20170817 nami del end #21070
                //Luc end edit 20151201 #44895
                this.setMonthNewCustomerCount(rs.getLong("newCount"));
                this.setMonthDesignatedCustomerCount(rs.getLong("designated_count"));
                this.setMonthTechCustomerCount(rs.getLong("tech_only_count")+ rs.getLong("tech_and_item_count"));
                this.setMonthDateCount(rs.getLong("dateCount"));
                this.setMonthTechValue(rs.getLong("techValue"));
                this.setMonthItemValue(rs.getLong("itemValue"));
                //this.setMonthchangeCancelValue(rs.getLong("change_cancel")); //20170818 del #21070
                this.setMonthDigestionValue(rs.getFloat("digestionValue"));
                this.setMonthAllDiscount(rs.getLong("discountValue"));
                //nhanvt start edit 20150120 Bug #34974
                //this.setMonthTotalValue(rs.getLong("totalValue"));
                this.setMonthTotalValue(rs.getLong("techValue") + rs.getLong("itemValue")-rs.getLong("discountValue"));
                //nhanvt end edit 20150120 Bug #34974
                this.setTotal_payment(rs.getLong("total_payment"));
            }
            
            // 20170818 nami edit start #21070 [GB���Ή�][gb] ���W���߃��[�� ���z�̏W�v�ΏہE�v�Z���ύX
            long contract_val = 0;
            long change_val   = 0;
            long cancel_val   = 0l;
            //rs = SystemInfo.getConnection().executeQuery(this.getSalesSQL_ContractValue(true));
            rs = SystemInfo.getConnection().executeQuery(this.getCourseContractSQL(true));
            if (rs.next()) {
                //this.setMonthContractValue(rs.getLong("total_contract_value"));
                contract_val = rs.getLong("total_contract_value"); //�_����z
            }
            rs = SystemInfo.getConnection().executeQuery(this.getChangeContractSQL(true));
            if (rs.next()) {
                change_val = rs.getLong("total_change_contract_value"); //�_��ύX���z�i�ύX���̐������z�j
            }
            rs = SystemInfo.getConnection().executeQuery(this.getCancelContractSQL(true));
            if (rs.next()) {
                cancel_val = rs.getLong("total_cancel_contract_value");
            }
            this.setMonthCancelContractValue(cancel_val);
            this.setMonthContractValue(contract_val);                //[�R�[�X�_��]
            this.setMonthchangeCancelValue(cancel_val + change_val); //[���ύX���z]           
            // 20170818 nami edit end #21070

            rs = SystemInfo.getConnection().executeQuery(this.getDiscountAllForCourse(true));
            if (rs.next()) {
                this.setMonthContractDiscount(rs.getLong("discountValue"));
            }
            // 20170817 nami add start #21070 [GB���Ή�][gb] ���W���߃��[�� ���z�̏W�v�ΏہE�v�Z���ύX
            // ���q���i���`�[�����j�擾
            rs = SystemInfo.getConnection().executeQuery(this.getTotalSlipCountSQL(true));
            if (rs.next()) {
                this.setMonthCustomerCount(rs.getLong("total_count"));
            }
            // 20170817 nami add end #21070
            rs.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }
        return true;
    }

    private String getSalesSQL(boolean isMonth) {
        
        // 20170817 nami del start #21070 [GB���Ή�][gb] ���W���߃��[�� ���z�̏W�v�ΏہE�v�Z���ύX
        //        // �W�v���ԊJ�n��
        //        Calendar calStart = Calendar.getInstance();
        //        calStart.setTime(this.getManageDate().getTime());
        //        calStart.set(Calendar.DAY_OF_MONTH, 1);
        //        // �W�v���ԏI����
        //        Calendar calEnd = (Calendar) calStart.clone();;
        //        calEnd.add(Calendar.MONTH, 1);
        //        calEnd.add(Calendar.DAY_OF_MONTH, -1);
        //
        //        // �Ώۓ��t�������𒴂����ꍇ�͗����̌��݌v�Ƃ���
        //        int cutoffDay = this.getShop().getCutoffDay().intValue();
        //        Calendar calTmp = Calendar.getInstance();
        //        calTmp.setTime(this.getManageDate().getTime());
        //        if (cutoffDay < calTmp.get(Calendar.DAY_OF_MONTH)) {
        //            calStart.add(Calendar.MONTH, 1);
        //            calEnd.add(Calendar.MONTH, 2);
        //            calEnd.set(Calendar.DAY_OF_MONTH, 1);
        //            calEnd.add(Calendar.DAY_OF_MONTH, -1);
        //        }
        //
        //        // ���ߓ����l���������Ԃ��擾
        //        this.resetSpan(calStart, calEnd);
        //
        //        // �Ώۓ��t���W�v���Ԃ̏I������菬�����ꍇ�͑Ώۓ��t���W�v���ԏI�����Ƃ���
        //        // (�ߋ��̃��W�����Q�Ƃ��Ă���ꍇ�͂��̓��܂ł̏W�v�Ƃ���)
        //        if (calEnd.after(calTmp)) {
        //            calEnd.setTime(calTmp.getTime());
        //        }
        // 20170817 nami del end #21070 [GB���Ή�][gb] ���W���߃��[�� ���z�̏W�v�ΏہE�v�Z���ύX

        // ���o����
        StringBuilder whereSQL = new StringBuilder(1000);
        whereSQL.append(" shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
        if (isMonth) {
            whereSQL.append(" and sales_date between " + SQLUtil.convertForSQLDateOnly(calStart));
            whereSQL.append("                    and " + SQLUtil.convertForSQLDateOnly(calEnd));
        } else {
            whereSQL.append(" and sales_date = " + SQLUtil.convertForSQLDateOnly(this.getManageDate()));
        }

        // �Z�p�E���i����
        String productValue = "";
        String totalValue = "";
        String discountValue = "";
        if (SystemInfo.getAccountSetting().getDisplayPriceType() == 0) {
            // �ō�
            productValue = "discount_detail_value_in_tax";
            totalValue = "discount_sales_value_in_tax";
            discountValue = "discount_value";
        } else {
            // �Ŕ�
            productValue = "discount_detail_value_no_tax";
            totalValue = "discount_sales_value_no_tax";
            discountValue = "discount_value_no_tax";
        }

        StringBuilder sql = new StringBuilder(10000);
        sql.append(" select");
        sql.append("      count(*) as totalCount");
        sql.append("     ,sum(case when designated_flag then 1 else 0 end) as designatedCount");
        //sql.append("     ,sum(case when get_visit_count_new_customer(customer_id, shop_id, sales_date) = 1 then 1 else 0 end) as newCount");
        
      //sql.append("     ,sum(case when get_visit_count_new_customer(customer_id, shop_id, sales_date) = 1 then 1 else 0 end) as newCount");
        sql.append("     ,sum(case when  ");
        sql.append("(");
        //IVS_LVTu start edit 2015/05/07 Bug #36493
        //sql.append("SELECT count(dscus.slip_no) + coalesce(max(before_visit_num),0) FROM data_sales dscus ");
	//sql.append(" INNER JOIN mst_customer mccus on dscus.customer_id = mccus.customer_id and mccus.customer_no <> '0' ");
        //sql.append("                               and mccus.delete_date is null ");
	//sql.append(" WHERE dscus.customer_id = ds.customer_id ");
	//sql.append("	AND dscus.shop_id = ds.shop_id ");
	//sql.append("	AND dscus.sales_date <= ds.sales_date ");
	//sql.append("	AND dscus.delete_date IS NULL");
        sql.append("	SELECT count(distinct dscus.customer_id) ");
        sql.append("	      FROM data_sales dscus");
        sql.append("	      INNER JOIN data_sales_detail dsd using (slip_no,shop_id)");
        sql.append("	      INNER JOIN mst_customer mccus ON dscus.customer_id = mccus.customer_id");
        sql.append("	      AND mccus.customer_no <> '0'");
        sql.append("	      AND mccus.delete_date IS NULL");
        sql.append("	      WHERE ");
        sql.append("	        dscus.customer_id = ds.customer_id");
        sql.append("	        AND dscus.shop_id = ds.shop_id");
        sql.append("	        AND dscus.sales_date = ds.sales_date");
        sql.append("	        AND dscus.delete_date IS NULL");
        sql.append("	        AND dsd.product_division in(1,3)");
        sql.append("	        AND get_visit_count(dscus.customer_id, dscus.shop_id, dscus.sales_date) = 1");
        sql.append("	        group by dscus.customer_id");
        sql.append("	        order by dscus.customer_id");
        //IVS_LVTu start edit 2015/05/07 Bug #36493
        sql.append(" ) ");
        sql.append(" = 1 then 1 else 0 end) as newCount");
        
        
        sql.append("     ,sum(");
        sql.append("         case when");
        sql.append("         (");
        sql.append("             select");
        sql.append("                 count(*)");
        sql.append("             from");
        sql.append("                 data_sales_detail");
        sql.append("             where");
        sql.append("                     shop_id = ds.shop_id");
        sql.append("                 and slip_no = ds.slip_no");
        sql.append("                 and product_division = 1");
        sql.append("                 and delete_date is null");
        sql.append("         ) > 0 then 1 else 0");
        sql.append("         end");
        sql.append("      ) as techCount");
        
        //Luc start add 20151201 #44895 
        sql.append("    ,(");
        sql.append("             select sum(1) from (");
	sql.append("    		SELECT ds.shop_id,");
	sql.append("    	       ds.slip_no ,");		       
	sql.append("    	       sum(CASE ds.product_division WHEN 1 THEN 1 ELSE 0 END) AS tech_num ,");
	sql.append("    	       sum(CASE ds.product_division WHEN 3 THEN 1 ELSE 0 END) AS tech_crame_num ,");
	sql.append("    	       sum(CASE ds.product_division WHEN 2 THEN 1 ELSE 0 END) AS item_num ,");
	sql.append("    	       sum(CASE ds.product_division WHEN 4 THEN 1 ELSE 0 END) AS item_crame_num");
        
	sql.append("    	FROM ");
	sql.append("    	view_data_sales_detail_valid ds");
	sql.append(" where ds.shop_id in (").append(SQLUtil.convertForSQL(this.getShopID())).append(")");
        if (isMonth) {
            sql.append(" and ds.sales_date between ").append(SQLUtil.convertForSQLDateOnly(calStart)).append(" and ").append( SQLUtil.convertForSQLDateOnly(calEnd)).append("");
        } else {
            sql.append(" and ds.sales_date between ").append(SQLUtil.convertForSQLDateOnly(this.getManageDate())).append(" and ").append(SQLUtil.convertForSQLDateOnly(this.getManageDate())).append("");
        }
	sql.append("    	GROUP BY ds.shop_id,");
	sql.append("    		 ds.slip_no");
        sql.append("                  ) t");
        sql.append("         where t.tech_num+tech_crame_num>0 and t.item_num + item_crame_num = 0");
        sql.append("         ) as tech_only_count");
        
        sql.append("    ,(");
        sql.append("             select sum(1) from (");
	sql.append("    		SELECT ds.shop_id,");
	sql.append("    	       ds.slip_no ,");		       
	sql.append("    	       sum(CASE ds.product_division WHEN 1 THEN 1 ELSE 0 END) AS tech_num ,");
	sql.append("    	       sum(CASE ds.product_division WHEN 3 THEN 1 ELSE 0 END) AS tech_crame_num ,");
	sql.append("    	       sum(CASE ds.product_division WHEN 2 THEN 1 ELSE 0 END) AS item_num ,");
	sql.append("    	       sum(CASE ds.product_division WHEN 4 THEN 1 ELSE 0 END) AS item_crame_num");
	sql.append("    	FROM ");
	sql.append("    	view_data_sales_detail_valid ds");
	sql.append(" where ds.shop_id in (").append(SQLUtil.convertForSQL(this.getShopID())).append(")");
        if (isMonth) {
            sql.append(" and ds.sales_date between ").append(SQLUtil.convertForSQLDateOnly(calStart)).append(" and ").append( SQLUtil.convertForSQLDateOnly(calEnd)).append("");
        } else {
            sql.append(" and ds.sales_date between ").append(SQLUtil.convertForSQLDateOnly(this.getManageDate())).append(" and ").append(SQLUtil.convertForSQLDateOnly(this.getManageDate())).append("");
        }
	sql.append("    	GROUP BY ds.shop_id,");
	sql.append("    		 ds.slip_no");
        sql.append("                  ) t");
        sql.append("         where t.tech_num+tech_crame_num>0 and t.item_num + item_crame_num > 0");
        sql.append("         ) as tech_and_item_count");
                     
        sql.append("    ,(");
        sql.append("             select sum(1) from (");
	sql.append("    		SELECT ds.shop_id,");
	sql.append("    	       ds.slip_no ,");		       
	sql.append("    	       sum(CASE ds.product_division WHEN 1 THEN 1 ELSE 0 END) AS tech_num ,");
	sql.append("    	       sum(CASE ds.product_division WHEN 3 THEN 1 ELSE 0 END) AS tech_crame_num ,");
	sql.append("    	       sum(CASE ds.product_division WHEN 2 THEN 1 ELSE 0 END) AS item_num ,");
	sql.append("    	       sum(CASE ds.product_division WHEN 4 THEN 1 ELSE 0 END) AS item_crame_num");
	sql.append("    	FROM ");
	sql.append("    	view_data_sales_detail_valid ds");
	sql.append(" where ds.shop_id in (").append(SQLUtil.convertForSQL(this.getShopID())).append(")");
        if (isMonth) {
            sql.append(" and ds.sales_date between ").append(SQLUtil.convertForSQLDateOnly(calStart)).append(" and ").append( SQLUtil.convertForSQLDateOnly(calEnd)).append("");
        } else {
            sql.append(" and ds.sales_date between ").append(SQLUtil.convertForSQLDateOnly(this.getManageDate())).append(" and ").append(SQLUtil.convertForSQLDateOnly(this.getManageDate())).append("");
        }
	sql.append("    	GROUP BY ds.shop_id,");
	sql.append("    		 ds.slip_no");
        sql.append("                  ) t");
        sql.append("         where t.tech_num+tech_crame_num=0 and t.item_num + item_crame_num > 0");
        sql.append("         ) as item_only_count");
        //
        sql.append("    ,(");
        sql.append("             select sum(1) from (");
	sql.append("    		SELECT ds.shop_id,");
	sql.append("    	       ds.slip_no ,");	
        sql.append("    	       sum(CASE ds.product_division WHEN 1 THEN 1 ELSE 0 END) AS tech_num ,");
	sql.append("    	       sum(CASE ds.product_division WHEN 3 THEN 1 ELSE 0 END) AS tech_crame_num ,");
	sql.append("    	       sum(CASE ds.product_division WHEN 2 THEN 1 ELSE 0 END) AS item_num ,");
	sql.append("    	       sum(CASE ds.product_division WHEN 4 THEN 1 ELSE 0 END) AS item_crame_num,");
	sql.append("    	       sum(CASE ds.product_division WHEN 5 THEN 1 ELSE 0 END) AS course_num,");
        sql.append("    	       sum(CASE ds.product_division WHEN 6 THEN 1 ELSE 0 END) AS digestion_num");
	
	sql.append("    	FROM ");
	sql.append("    	view_data_sales_detail_valid ds");
	sql.append(" where ds.shop_id in (").append(SQLUtil.convertForSQL(this.getShopID())).append(")");
        if (isMonth) {
            sql.append(" and ds.sales_date between ").append(SQLUtil.convertForSQLDateOnly(calStart)).append(" and ").append( SQLUtil.convertForSQLDateOnly(calEnd)).append("");
        } else {
            sql.append(" and ds.sales_date between ").append(SQLUtil.convertForSQLDateOnly(this.getManageDate())).append(" and ").append(SQLUtil.convertForSQLDateOnly(this.getManageDate())).append("");
        }
	sql.append("    	GROUP BY ds.shop_id,");
	sql.append("    		 ds.slip_no");
        sql.append("                  ) t");
        sql.append("         where tech_num+tech_crame_num+item_num+item_crame_num = 0 and course_num+digestion_num>0");
        sql.append("         ) as course_and_degestion_count");
        
        
        sql.append("    ,(");
        sql.append("             select sum(1) from (");
	sql.append("    		SELECT ds.shop_id,");
	sql.append("    	       ds.slip_no ,");		       
	sql.append("    	       sum(CASE ds.product_division WHEN 1 THEN 1 ELSE 0 END) AS tech_num ,");
	sql.append("    	       sum(CASE ds.product_division WHEN 3 THEN 1 ELSE 0 END) AS tech_crame_num ,");
	sql.append("    	       sum(CASE ds.product_division WHEN 2 THEN 1 ELSE 0 END) AS item_num ,");
	sql.append("    	       sum(CASE ds.product_division WHEN 4 THEN 1 ELSE 0 END) AS item_crame_num");
	sql.append("    	FROM ");
	sql.append("    	view_data_sales_detail_valid ds");
	sql.append(" where ds.shop_id in (").append(SQLUtil.convertForSQL(this.getShopID())).append(")");
        if (isMonth) {
            sql.append(" and ds.sales_date between ").append(SQLUtil.convertForSQLDateOnly(calStart)).append(" and ").append( SQLUtil.convertForSQLDateOnly(calEnd)).append("");
        } else {
            sql.append(" and ds.sales_date between ").append(SQLUtil.convertForSQLDateOnly(this.getManageDate())).append(" and ").append(SQLUtil.convertForSQLDateOnly(this.getManageDate())).append("");
        }
        sql.append("and ds.designated_flag = true ");
	sql.append("    	GROUP BY ds.shop_id,");
	sql.append("    		 ds.slip_no");
        sql.append("                  ) t");
        sql.append("         where t.tech_num+tech_crame_num>0");
        sql.append("         ) as designated_count");
        //Luc end add 20151201 #44895 
        // �c�Ɠ����i����Ȃ��̃��W���ߓ������܂߂�j
        sql.append("     ,count(distinct sales_date)");
        sql.append("       + (");
        sql.append("             select");
        sql.append("                 count(distinct manage_date)");
        sql.append("             from");
        sql.append("                 data_register dr");
        sql.append("             where");
        sql.append("                     delete_date is null");
        sql.append("                 and shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
        sql.append("                 and manage_date between " + SQLUtil.convertForSQLDateOnly(calStart));
        sql.append("                                     and " + SQLUtil.convertForSQLDateOnly(calEnd));
        sql.append("                 and not exists");
        sql.append("                     (");
        sql.append("                         select 1");
        sql.append("                         from");
        //nhanvt start edit 20141128 Bug #33247
        sql.append("                             data_sales ds ");
        //sql.append("                             inner join mst_customer mc ON mc.customer_id = ds.customer_id and mc.customer_no <> '0'  and mc.delete_date is null ");
        sql.append("                         where");
        sql.append("                                 ds.delete_date is null");
        sql.append("                             and ds.shop_id = dr.shop_id");
        sql.append("                             and ds.sales_date = dr.manage_date");
        //nhanvt end edit 20141128 Bug #33247
        sql.append("                     )");
        sql.append("         ) as dateCount");

        // �Z�p����
        sql.append("     ,(");
        sql.append("         select");
        sql.append("             sum(" + productValue + ")");
        sql.append("         from");
        sql.append("             view_data_sales_detail_valid detailvalid ");
        //nhanvt start edit 20141128 Bug #33247
        //sql.append("             inner join mst_customer mc ON mc.customer_id = detailvalid.customer_id and mc.customer_no <> '0'  and mc.delete_date is null ");
        
        //sql.append("         where" + whereSQL);
        sql.append("         where" );
      
        
        sql.append(" detailvalid.shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
        if (isMonth) {
            sql.append(" and detailvalid.sales_date between " + SQLUtil.convertForSQLDateOnly(calStart));
            sql.append("                    and " + SQLUtil.convertForSQLDateOnly(calEnd));
        } else {
            sql.append(" and detailvalid.sales_date = " + SQLUtil.convertForSQLDateOnly(this.getManageDate()));
        }
        
        sql.append("             and detailvalid.product_division in (1,3)");
        //nhanvt end edit 20141128 Bug #33247
        sql.append("      ) as techValue");

        // ���i����
        sql.append("     ,(");
        sql.append("         select");
        sql.append("             sum(" + productValue + ")");
        sql.append("         from");
        sql.append("             view_data_sales_detail_valid detailvalid ");
        //nhanvt start edit 20141128 Bug #33247
        //sql.append("             inner join mst_customer mc ON mc.customer_id = detailvalid.customer_id and mc.customer_no <> '0'  and mc.delete_date is null ");
        //sql.append("         where" + whereSQL);
        sql.append("         where" );
        sql.append(" detailvalid.shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
        if (isMonth) {
            sql.append(" and detailvalid.sales_date between " + SQLUtil.convertForSQLDateOnly(calStart));
            sql.append("                    and " + SQLUtil.convertForSQLDateOnly(calEnd));
        } else {
            sql.append(" and detailvalid.sales_date = " + SQLUtil.convertForSQLDateOnly(this.getManageDate()));
        }
        sql.append("             and detailvalid.product_division in (2,4)");
        //nhanvt end edit 20141128 Bug #33247
        sql.append("      ) as itemValue");
        sql.append("      ,( \n");
        sql.append("     select coalesce(sum(dsd1.discount_detail_value_in_tax),0) \n");
        sql.append("     from  view_data_sales_detail_valid dsd1  \n");
        sql.append("      where  \n");
        sql.append("      dsd1.shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
         if (isMonth) {
            sql.append(" and dsd1.sales_date between " + SQLUtil.convertForSQLDateOnly(calStart));
            sql.append("                    and " + SQLUtil.convertForSQLDateOnly(calEnd));
        } else {
            sql.append("      and dsd1.sales_date = " + SQLUtil.convertForSQLDateOnly(this.getManageDate()));
         }
        sql.append("      and dsd1.product_division in(8,9) \n");
        sql.append("     )   \n"); 
        sql.append("      + coalesce(( \n");
        sql.append("      SELECT sum(\n");
        sql.append("           (SELECT SUM (coalesce(payment_value,0))\n");
        sql.append("            FROM\n");
        sql.append("              (SELECT DISTINCT dpd.slip_no,dpd.shop_id,dpd.payment_no,payment_method_id,payment_value\n");
        sql.append("               FROM data_payment_detail dpd\n");
        sql.append("               WHERE dp.shop_id = dpd.shop_id\n");
        sql.append("                 AND dp.slip_no = dpd.slip_no\n");
        sql.append("                 AND dp.payment_no = dpd.payment_no) AS t) + coalesce(dp.bill_value,0)) AS value\n");
        sql.append("      FROM view_data_sales_detail_valid dsd1\n");
        sql.append("      INNER JOIN data_payment dp ON dsd1.shop_id = dp.shop_id\n");
        sql.append("      AND dsd1.slip_no = dp.slip_no\n");
        sql.append("      where  \n");
        sql.append("      dsd1.shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
         if (isMonth) {
            sql.append(" and dsd1.sales_date between " + SQLUtil.convertForSQLDateOnly(calStart));
            sql.append("                    and " + SQLUtil.convertForSQLDateOnly(calEnd));
        } else {
            sql.append("      and dsd1.sales_date = " + SQLUtil.convertForSQLDateOnly(this.getManageDate()));
         }
        sql.append("      and dsd1.product_division in(7) \n");
        sql.append("     ),0)  AS change_cancel \n"); 
        
        //nhanvt start edit 20141008 Bug #30958
        // �������z
         //Luc start edit 20151201 #44895
        /*
        sql.append(" ,(select \n");
        sql.append("sum (total_consumption_value) as total_consumption_value  \n");
        sql.append("from  ( \n");
        sql.append(" select \n ");

        sql.append("     sum(coalesce(dcd.product_num,0)) as total_consumption_num");
       if (SystemInfo.getAccountSetting().getDisplayPriceType() !=0) {
            // �Ŕ���
             //IVS_TMTrong start edit 20150804 Bug #41291
            //sql.append("     ,ceil(sum(dcd.product_num*dc.product_value/dc.product_num) / (1.0 + get_tax_rate(max(coalesce(ds.sales_date,current_date))))) as total_consumption_value");
            sql.append("     ,ceil(SUM(case when dc.product_num >0 then dcd.product_num*dc.product_value/dc.product_num else 0 end ) / (1.0 + get_tax_rate(max(coalesce(ds.sales_date,current_date))))) as total_consumption_value");
             //IVS_TMTrong end edit 20150804 Bug #41291
        } else {
            // �ō���
            //IVS_TMTrong start edit 20150804 Bug #41291
            //sql.append("     ,sum(dcd.product_num*dc.product_value/dc.product_num) as total_consumption_value");
             sql.append(" ,SUM(case when dc.product_num >0 then dcd.product_num*dc.product_value/dc.product_num else 0 end ) as total_consumption_value ");
             //IVS_TMTrong end edit 20150804 Bug #41291
        }
        sql.append(",dc.product_id ");
        sql.append(" from data_sales ds");
         //nhanvt start add 20141128 Bug #33247
        //sql.append("     inner join mst_customer mc ON mc.customer_id = ds.customer_id and mc.customer_no <> '0'  and mc.delete_date is null ");
        //nhanvt end add 20141128 Bug #33247
        sql.append("     inner join data_contract_digestion dcd");
        sql.append("     on ds.shop_id = dcd.shop_id and ds.slip_no = dcd.slip_no");
        sql.append("     inner join data_contract dc");
        sql.append("     on dcd.shop_id = dc.shop_id and dcd.contract_no = dc.contract_no and dcd.contract_detail_no = dc.contract_detail_no");
        sql.append(" where ds.shop_id in (").append(SQLUtil.convertForSQL(this.getShopID())).append(")");
        //sql.append(" and ds.sales_date between '").append(paramBean.getTargetStartDate()).append("' and '").append(paramBean.getTargetEndDate()).append("'");
        
        if (isMonth) {
            sql.append(" and ds.sales_date between ").append(SQLUtil.convertForSQLDateOnly(calStart)).append(" and ").append( SQLUtil.convertForSQLDateOnly(calEnd)).append("");
        } else {
            sql.append(" and ds.sales_date between ").append(SQLUtil.convertForSQLDateOnly(this.getManageDate())).append(" and ").append(SQLUtil.convertForSQLDateOnly(this.getManageDate())).append("");
        }
        
        sql.append("group by dc.product_id   ");
        sql.append("order by dc.product_id ");
        sql.append(") dc ) as digestionValue ");
        */
        
        sql.append("     ,(SELECT SUM (total_consumption_value) AS total_consumption_value   ");
        sql.append(" FROM   ");
        sql.append("   (SELECT SUM(coalesce(dcd.product_num,0)) AS total_consumption_num,   ");
        if (SystemInfo.getAccountSetting().getDisplayPriceType() !=0) {
            //LVTu Edit 2019/08/26 SPOS���őΉ�
            sql.append("       ceil(SUM(dcd.product_num*dc.product_value/dc.product_num) / (1.0 + get_tax_rate(dsd.tax_rate, max(coalesce(dsd.sales_date,CURRENT_DATE))))) AS total_consumption_value,   ");
        }else {
           sql.append("       SUM(dcd.product_num*dc.product_value/dc.product_num) AS total_consumption_value,");
        }
        sql.append("        dc.product_id   ");
        sql.append("    FROM   ");
        sql.append("      (SELECT DISTINCT ds.sales_date,   ");
        sql.append("                       dsd.slip_no,   ");
        sql.append("                       dsd.shop_id,   ");
        sql.append("                       dsd.contract_detail_no,   ");
        sql.append("                       dsd.product_id,   ");
        sql.append("                       dsd.tax_rate,   ");
        sql.append("                       dsd.product_division   ");
        sql.append("       FROM data_sales ds   ");
        sql.append("       INNER JOIN data_sales_detail dsd ON ds.slip_no = dsd.slip_no   ");
        sql.append("       AND ds.shop_id = dsd.shop_id   ");
        
        sql.append(" where ds.shop_id in (").append(SQLUtil.convertForSQL(this.getShopID())).append(")");
        if (isMonth) {
            sql.append(" and ds.sales_date between ").append(SQLUtil.convertForSQLDateOnly(calStart)).append(" and ").append( SQLUtil.convertForSQLDateOnly(calEnd)).append("");
        } else {
            sql.append(" and ds.sales_date between ").append(SQLUtil.convertForSQLDateOnly(this.getManageDate())).append(" and ").append(SQLUtil.convertForSQLDateOnly(this.getManageDate())).append("");
        }
        sql.append("         AND dsd.product_division = 6) dsd   ");
        sql.append("    INNER JOIN data_contract_digestion dcd ON dsd.shop_id = dcd.shop_id   ");
        sql.append("    AND dsd.slip_no = dcd.slip_no   ");
        sql.append("    AND dcd.contract_detail_no = dsd.contract_detail_no   ");
        sql.append("    INNER JOIN data_contract dc ON dcd.contract_shop_id = dc.shop_id   ");
        sql.append("    AND dcd.contract_no = dc.contract_no   ");
        sql.append("    AND dcd.contract_detail_no = dc.contract_detail_no   ");
        sql.append("    AND dc.product_id = dsd.product_id   ");
        sql.append("    GROUP BY dc.product_id   ");
        // LVTu Edit 2019/08/26 SPOS���őΉ�
        sql.append("    , dsd.tax_rate   ");
        sql.append("    ORDER BY dc.product_id) dc) as digestionValue   ");
        //Luc end edit 20151201 #44895
        
        //nhanvt end edit 20141008 Bug #30958
       
        // �S�̊����z
        //Luc start edit 20150127 Bug 35084
        //sql.append("     ,sum(" + discountValue + ") as discountValue");
        sql.append("     ,COALESCE(( select ");
        //nhanvt start edit 20150202 Bug #35136
        sql.append("                 sum(ds."+ discountValue +") as all_discount ");
        //nhanvt end edit 20150202 Bug #35136
	sql.append("     	    from view_data_sales_valid ds ");
	sql.append("     	    where ds.shop_id in ("+SQLUtil.convertForSQL(this.getShopID())+") ");
         if (isMonth) {
            sql.append(" and ds.sales_date between ").append(SQLUtil.convertForSQLDateOnly(calStart)).append(" and ").append( SQLUtil.convertForSQLDateOnly(calEnd)).append("");
        } else {
            sql.append(" and ds.sales_date between ").append(SQLUtil.convertForSQLDateOnly(this.getManageDate())).append(" and ").append(SQLUtil.convertForSQLDateOnly(this.getManageDate())).append("");
        }
        //Luc end edit 20150127 Bug 35084
	sql.append("                     and exists         (             select 1 ");            
	sql.append("     		from                 view_data_sales_detail_valid ");            
	sql.append("     		where                     shop_id = ds.shop_id ");                 
	sql.append("     		and slip_no = ds.slip_no                 and product_division in(1,2) )),0) AS discountValue ");

        // ������i�S�̊�����̋��z�j
        sql.append("     ,sum(" + totalValue + ") as totalValue");

        // �ō�������i�S�̊�����̋��z�j
        sql.append("     ,sum(discount_sales_value_in_tax) as totalValueInTax");

        //������|��
        /*sql.append("      ,( \n");

        sql.append("     SELECT \n");
        sql.append("     sum(coalesce(dpd.payment_total, 0))  \n");
        sql.append("     FROM data_sales ds\n");
        //nhanvt start edit
        sql.append("     LEFT JOIN mst_customer mc ON mc.customer_id = ds.customer_id and mc.delete_date is null \n");
        //sql.append("     inner join mst_customer mc ON mc.customer_id = ds.customer_id and mc.customer_no <> '0'  and mc.delete_date is null ");
        //nhanvt end edit
        sql.append("     INNER JOIN data_payment dp ON dp.shop_id = ds.shop_id\n");
        sql.append("     AND dp.slip_no = ds.slip_no\n");
        sql.append("     AND dp.payment_no > 0\n");
        sql.append("     LEFT JOIN\n");
        sql.append("     (SELECT dpd.shop_id ,\n");
        sql.append("     dpd.slip_no ,\n");
        sql.append("     dpd.payment_no ,\n");
        sql.append("     sum(payment_value) AS payment_total\n");
        sql.append("     FROM data_payment_detail dpd\n");
        sql.append("     WHERE dpd.delete_date IS NULL\n");
        sql.append("     GROUP BY dpd.shop_id ,\n");
        sql.append("     dpd.slip_no ,\n");
        sql.append("     dpd.payment_no) dpd ON dpd.shop_id = dp.shop_id\n");
        sql.append("     AND dpd.slip_no = dp.slip_no\n");
        sql.append("     AND dpd.payment_no = dp.payment_no\n");
        sql.append("     LEFT JOIN mst_staff ms ON ms.staff_id = dp.staff_id\n");
        sql.append("     WHERE ds.delete_date IS NULL\n");
        sql.append("     AND ds.shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n");
        sql.append("     AND ds.sales_date >= " + SQLUtil.convertForSQLDateOnly(calStart) + "\n");
        sql.append("     AND ds.sales_date <= " + SQLUtil.convertForSQLDateOnly(calEnd) + "\n");
        sql.append("      ) as total_payment\n");
        */
        sql.append("    ,(SELECT sum(CASE WHEN mpm.payment_class_id in(1,2,3,4) THEN coalesce(dpd.payment_value - (CASE WHEN dp.change_value < 0 THEN 0 ELSE dp.change_value END), 0) ELSE 0 END) AS payment_value ");
        sql.append("      FROM data_payment_detail dpd ");
        sql.append("      JOIN data_payment dp USING (shop_id, ");
        sql.append("                                  slip_no, ");
        sql.append("                                  payment_no) ");
        sql.append("      JOIN mst_payment_method mpm USING (payment_method_id) ");
        sql.append("      WHERE dpd.delete_date IS NULL ");
        sql.append("        AND dp.delete_date IS NULL ");
        sql.append("        AND dp.payment_no > 0 ");
       sql.append("     	    and dp.shop_id in ("+SQLUtil.convertForSQL(this.getShopID())+") ");
         if (isMonth) {
            sql.append(" and dp.payment_date between ").append(SQLUtil.convertForSQLDateOnly(calStart)).append(" and ").append( SQLUtil.convertForSQLDateOnly(calEnd)).append("");
        } else {
            sql.append(" and dp.payment_date between ").append(SQLUtil.convertForSQLDateOnly(this.getManageDate())).append(" and ").append(SQLUtil.convertForSQLDateOnly(this.getManageDate())).append("");
        }
        sql.append("          ) as total_payment");
        sql.append(" from");
        sql.append("     (" + getViewDataSalesValidEx() + ") ds");
        sql.append(" where" + whereSQL);

        return sql.toString();
    }
    
    private String getDiscountAllForCourse(boolean isMonth) {
        // 20170818 nami del start #21070 [GB���Ή�][gb] ���W���߃��[�� ���z�̏W�v�ΏہE�v�Z���ύX
        //        Calendar calStart = Calendar.getInstance();
        //        calStart.setTime(this.getManageDate().getTime());
        //        calStart.set(Calendar.DAY_OF_MONTH, 1);
        //        // �W�v���ԏI����
        //        Calendar calEnd = (Calendar) calStart.clone();;
        //        calEnd.add(Calendar.MONTH, 1);
        //        calEnd.add(Calendar.DAY_OF_MONTH, -1);
        // 20170818 nami del end  #21070
        
        String discountValue = "";
        if (SystemInfo.getAccountSetting().getDisplayPriceType() == 0) {
            // �ō�
            discountValue = "discount_value";
        } else {
            // �Ŕ�
            discountValue = "discount_value_no_tax";
        }
        StringBuilder sql = new StringBuilder();
        sql.append("     select  sum(ds.").append(discountValue).append(") AS discountValue ");
        //nhanvt end edit 20150202 Bug #35136
	sql.append("     	    from view_data_sales_valid ds ");
	sql.append("     	    where ds.shop_id in (").append(SQLUtil.convertForSQL(this.getShopID())).append(") ");
        // 20170818 nami edit start #21070 [GB���Ή�][gb] ���W���߃��[�� ���z�̏W�v�ΏہE�v�Z���ύX
         if (isMonth) {
            sql.append(" and ds.sales_date between ").append(SQLUtil.convertForSQLDateOnly(calStart)).append(" and ").append( SQLUtil.convertForSQLDateOnly(calEnd)).append("");
        } else {
            sql.append(" and ds.sales_date = ").append(SQLUtil.convertForSQLDateOnly(this.getManageDate()));
        }
        //Luc end edit 20150127 Bug 35084
        //	sql.append("                     and exists         (             select 1 ");            
        //	sql.append("     		from                 view_data_sales_detail_valid ");            
        //	sql.append("     		where                     shop_id = ds.shop_id ");                 
        //	sql.append("     		and slip_no = ds.slip_no                 and product_division in(5) ) ");
        // 20170818 nami edit start #21070
        
        return sql.toString();
    }
    private String getSalesSQL_ContractValue(boolean isMonth) {

        // �W�v���ԊJ�n��
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(this.getManageDate().getTime());
        calStart.set(Calendar.DAY_OF_MONTH, 1);
        // �W�v���ԏI����
        Calendar calEnd = (Calendar) calStart.clone();;
        calEnd.add(Calendar.MONTH, 1);
        calEnd.add(Calendar.DAY_OF_MONTH, -1);

        // �Ώۓ��t�������𒴂����ꍇ�͗����̌��݌v�Ƃ���
        int cutoffDay = this.getShop().getCutoffDay().intValue();
        Calendar calTmp = Calendar.getInstance();
        calTmp.setTime(this.getManageDate().getTime());
        if (cutoffDay < calTmp.get(Calendar.DAY_OF_MONTH)) {
            calStart.add(Calendar.MONTH, 1);
            calEnd.add(Calendar.MONTH, 2);
            calEnd.set(Calendar.DAY_OF_MONTH, 1);
            calEnd.add(Calendar.DAY_OF_MONTH, -1);
        }

        // ���ߓ����l���������Ԃ��擾
        this.resetSpan(calStart, calEnd);

        // �Ώۓ��t���W�v���Ԃ̏I������菬�����ꍇ�͑Ώۓ��t���W�v���ԏI�����Ƃ���
        // (�ߋ��̃��W�����Q�Ƃ��Ă���ꍇ�͂��̓��܂ł̏W�v�Ƃ���)
        if (calEnd.after(calTmp)) {
            calEnd.setTime(calTmp.getTime());
        }

        StringBuilder sql = new StringBuilder(1000);
        //Luc start edit 20151201 #44895
        /*sql.append(" select");
        sql.append("      count(1) as total_contract_num");

        if (SystemInfo.getAccountSetting().getDisplayPriceType() == 0) {
            // �ō���
            sql.append("     ,sum(dsd.product_value - dsd.discount_value) as total_contract_value");
        } else {
            // �Ŕ���
            sql.append("     ,ceil(sum(dsd.product_value - dsd.discount_value) / (1.0 + get_tax_rate(max(dsd.sales_date)))) as total_contract_value");
        }
        // vtbphuong start delete 20140904 Bug #30375 
        sql.append(" from");
        sql.append("     (");
        sql.append("         select");
     //   sql.append("              mup.shop_id , ");
        sql.append("             mc.course_id");
        sql.append("         from");
        sql.append("             mst_course mc");
     //   sql.append("                 left join mst_use_product mup");
    //    sql.append("                        on mc.course_id = mup.product_id");
    //    sql.append("                       and mup.product_division = 3");
        sql.append("         where");
        sql.append("                 mc.delete_date is null");
    //    sql.append("             and mup.delete_date is null");
    //    sql.append("             and mup.shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
        sql.append("     ) mc");
        sql.append("         inner join");
        sql.append("             (");
        sql.append("                 select");
        sql.append("                      ds.sales_date");
        sql.append("                     ,dsd.*");
        sql.append("                 from");
        sql.append("                     data_sales ds");
        //nhanvt start add 20141128 Bug #33247
        //sql.append("             inner join mst_customer mc ON mc.customer_id = ds.customer_id and mc.customer_no <> '0'  and mc.delete_date is null ");
        //nhanvt end add 20141128 Bug #33247
        sql.append("                         inner join data_sales_detail dsd");
        //sql.append("                             using(shop_id, slip_no)");
        sql.append("                             on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no ");
        sql.append("                 where");
        sql.append("                         ds.delete_date is null");
        sql.append("                     and dsd.delete_date is null");
        //LUC START EDIT 20140207
        //sql.append("                     and dsd.product_division in (5, 6)");
        sql.append("                     and dsd.product_division in (5)");
        //LUC END EDIT 20140207
        sql.append("                     and ds.shop_id = " + SQLUtil.convertForSQL(this.getShopID()));

        if (isMonth) {
            sql.append("                 and ds.sales_date between " + SQLUtil.convertForSQLDateOnly(calStart));
            sql.append("                                       and " + SQLUtil.convertForSQLDateOnly(calEnd));
        } else {
            sql.append("                 and ds.sales_date = " + SQLUtil.convertForSQLDateOnly(this.getManageDate()));
        }

        sql.append("             ) dsd");
       // sql.append("                 on mc.shop_id = dsd.shop_id");
     //   sql.append("                and mc.course_id = dsd.product_id");
           sql.append("                on mc.course_id = dsd.product_id");
         // vtbphuong end delete 20140904 Bug #30375 
        */
       

        sql.append(" SELECT SUM (total_contract_num) AS total_contract_num, ");
        sql.append("            SUM (total_contract_value) AS total_contract_value ");
        sql.append(" FROM ");
        sql.append("   (SELECT SUM (total_contract_num) AS total_contract_num, ");
        sql.append("                                       SUM (total_contract_value) AS total_contract_value ");
        sql.append("    FROM ");
        sql.append("      (SELECT sales_date, ");
        sql.append("              ds.product_id, ");
        sql.append("              count(ds.product_id) AS total_contract_num, ");
         if (SystemInfo.getAccountSetting().getDisplayPriceType() == 0) {
            sql.append("          SUM(CASE WHEN product_division IN (7) THEN ds.payment_value ELSE ds.discount_detail_value_in_tax END) AS total_contract_value ");
         }else {
            sql.append("          SUM(CASE WHEN product_division IN (7) THEN ds.payment_value ELSE ds.discount_detail_value_no_tax END) AS total_contract_value ");    
         }
        sql.append("       FROM ");
        sql.append("         (SELECT DISTINCT ds.* , ");
        sql.append("            (SELECT SUM(CASE WHEN product_division IN (7) THEN dpd.payment_value ELSE 0 END) ");
        sql.append("             FROM data_payment_detail dpd ");
        sql.append("             WHERE dpd.shop_id = ds.shop_id ");
        sql.append("               AND dpd.slip_no = ds.slip_no ");
        sql.append("               AND delete_date IS NULL) AS payment_value ");
        sql.append("          FROM view_data_sales_detail_valid ds ");
        sql.append("          LEFT JOIN data_payment_detail dpd ON dpd.shop_id = ds.shop_id ");
        sql.append("          AND dpd.slip_no = ds.slip_no ");
        sql.append("          INNER JOIN data_contract dc ON ds.shop_id = dc.shop_id ");
        sql.append("          AND ds.slip_no = dc.slip_no ");
        sql.append("          AND ds.product_id = dc.product_id ");
         sql.append("                     and ds.shop_id = " + SQLUtil.convertForSQL(this.getShopID()));
        if (isMonth) {
            sql.append("                 and ds.sales_date between " + SQLUtil.convertForSQLDateOnly(calStart));
            sql.append("                                       and " + SQLUtil.convertForSQLDateOnly(calEnd));
        } else {
            sql.append("                 and ds.sales_date = " + SQLUtil.convertForSQLDateOnly(this.getManageDate()));
        }
        sql.append("            AND ds.product_division IN (5, ");
        sql.append("                                        7)) ds ");
        sql.append("       GROUP BY ds.product_id, ");
        sql.append("                sales_date ");
        sql.append("       ORDER BY ds.product_id) a ");
        sql.append("    GROUP BY sales_date) t ");
           //Luc start edit 20151201 #44895 
        return sql.toString();
    }

    public boolean printReceipt() {
        
        this.setDataCollectionPeriod(); //20170818 nami add #21070
        
        this.setDaySales();
        this.setMonthSales();

        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("data", createMailTitle() + "\n\n" + createMailBody());

        try {
            PrintReceipt pr = new PrintReceipt();
            //�v�����^��񂪑��݂��Ȃ��ꍇ�͏����𔲂���
            if (!pr.canPrint()) {
                return false;
            }

            InputStream report = HairRegister.class.getResourceAsStream(RECEIPT_REPORT_PATH);
            Vector<Integer> temp = new Vector<Integer>();
            temp.add(0);
            ReportManager.exportReport(report, pr.getPrinter(), 3, param, temp);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void resetSpan(Calendar calStart, Calendar calEnd) {

        int cutoffDay = this.getShop().getCutoffDay().intValue();

        if (cutoffDay == 31) {
            return;
        }

        Calendar calTo = Calendar.getInstance();
        calTo.setTime(calEnd.getTime());

        if (calTo.getActualMaximum(Calendar.DATE) <= cutoffDay) {
            calTo.set(Calendar.DAY_OF_MONTH, calTo.getActualMaximum(Calendar.DATE));
        } else {
            calTo.set(Calendar.DAY_OF_MONTH, cutoffDay);
        }

        Calendar calFrom = (Calendar) calTo.clone();
        calFrom.add(Calendar.MONTH, -1);
        calFrom.add(Calendar.DAY_OF_MONTH, 1);

        calStart.setTime(calFrom.getTime());
        calEnd.setTime(calTo.getTime());
    }

    private String getViewDataSalesValidEx() {

        /*
         * view_data_sales_valid�r���[�̃R�[�h�����̂܂ܗ��p���A
         * view_data_sales_detail_valid �Ɂuproduct_division not in (6)�v�̏�����ǉ�
         */

        StringBuilder sql = new StringBuilder(1000);

        sql.append(" select");
        sql.append("      a.*");
        sql.append("     ,discount_detail_value_in_tax - discount_value  as discount_sales_value_in_tax");
         // vtbphuong start change 20140917 Bug #30758
//        sql.append("     ,case when");
//        sql.append("         (discount_detail_value_in_tax - discount_value) -");
//        sql.append("             (");
//        sql.append("                 discount_detail_value_no_tax -");
//        sql.append("                     sign(discount_value /");
//        sql.append("                         (1.0 + get_tax_rate(a.sales_date))");
//        sql.append("                     )");
//        sql.append("                     * ceil(");
//        sql.append("                         abs(discount_value /");
//        sql.append("                             (1.0 + get_tax_rate(a.sales_date))");
//        sql.append("                         ))");
//        sql.append("             ) < 0");
//        sql.append("         then");
//        sql.append("             (discount_detail_value_in_tax - discount_value)");
//        sql.append("         else");
       
//        sql.append("             (");
//        sql.append("                 discount_detail_value_no_tax -");
//        sql.append("                     sign(discount_value /");
//        sql.append("                         (1.0 + get_tax_rate(a.sales_date))");
//        sql.append("                     )");
//        sql.append("                     * ceil(");
//        sql.append("                         abs(discount_value /");
//        sql.append("                             (1.0 + get_tax_rate(a.sales_date))");
//        sql.append("                         ))");
//        sql.append("             )");
         sql.append("             ");
         sql.append("                 , discount_detail_value_no_tax - discount_value_no_tax "); 
         sql.append("             ");
        // vtbphuong start change 20140917 Bug #30758
        sql.append("          as discount_sales_value_no_tax");
        sql.append(" from");
        sql.append("     (");
        sql.append("         select");
        sql.append("              a.shop_id");
        sql.append("             ,a.slip_no");
        sql.append("             ,a.sales_date");
        sql.append("             ,a.customer_id");
        sql.append("             ,a.designated_flag");
        sql.append("             ,a.staff_id");
        sql.append("             ,a.visit_num");
        sql.append("             ,a.visited_memo");
        sql.append("             ,a.next_visit_date");
        sql.append("             ,a.reappearance_date");
        sql.append("             ,coalesce(b.discount_value, 0)  as discount_value");
        sql.append("             ,sign(");
        sql.append("                 coalesce(b.discount_value, 0) /");
        sql.append("                 (1.0 + get_tax_rate(a.sales_date))");
        sql.append("              )");
        sql.append("              * ceil(");
        sql.append("                 abs(");
        sql.append("                     coalesce(b.discount_value, 0) /");
        sql.append("                     (1.0 + get_tax_rate(a.sales_date))");
        sql.append("                 )) as discount_value_no_tax");
        //Luc start edit 20160411 #49538
        /*sql.append("             ,(");
        sql.append("                 select");
        sql.append("                     sum(discount_detail_value_in_tax)");
        sql.append("                 from");
        sql.append("                     view_data_sales_detail_valid");
        sql.append("                 where");
        sql.append("                     shop_id = a.shop_id");
        sql.append("                 and slip_no = a.slip_no");
        sql.append("                 and product_division not in (6)");
        sql.append("              ) as discount_detail_value_in_tax");
        */
        sql.append("             ,(");
        sql.append("                 select");
        sql.append("                     sum(discount_detail_value_in_tax)");
        sql.append("                 from");
        sql.append("                     view_data_sales_detail_valid");
        sql.append("                 where");
        sql.append("                     shop_id = a.shop_id");
        sql.append("                 and slip_no = a.slip_no");
        sql.append("                 and product_division in (1,2,3,4,5,8,9)");
        // 20170821 nami edit start #21070 [GB���Ή�][gb] ���W���߃��[�� ���z�̏W�v�ΏہE�v�Z���ύX
        //sql.append("              )");
        sql.append("              ) as discount_detail_value_in_tax ");
        //        sql.append("              +(select coalesce(sum(coalesce(dsd1.discount_detail_value_in_tax,0) - coalesce(dsd2.discount_detail_value_in_tax,0) + coalesce(dc1.service_charge,0)),0)");
        //        sql.append("              from  view_data_sales_detail_valid dsd1 ");
        //        sql.append("              left join data_contract dc1 on dsd1.slip_no = dc1.slip_no and dsd1.shop_id = dc1.shop_id and dsd1.product_id = dc1.product_id ");
        //        sql.append("              left join data_contract dc2 on dc1.before_contract_shop_id = dc2.shop_id and dc1.before_contract_no = dc2.contract_no");
        //        sql.append("              left join view_data_sales_detail_valid dsd2 on dsd2.slip_no = dc2.slip_no and dsd2.shop_id = dc2.shop_id and dsd2.product_id = dc2.product_id");
        //        sql.append("              where ");
        //        sql.append("              dsd1.shop_id = a.shop_id");
        //        sql.append("              and dsd1.slip_no =  a.slip_no");
        //        sql.append("              and dsd1.product_division in(7)");
        //        sql.append("              )");
        //        sql.append("             as discount_detail_value_in_tax");
        // 20170821 nami edit end #21070 
        //Luc end edit 20160411 #49538
        sql.append("             ,(");
        sql.append("                 select");
        sql.append("                     sum(discount_detail_value_no_tax)");
        sql.append("                 from");
        sql.append("                     view_data_sales_detail_valid");
        sql.append("                 where");
        sql.append("                     shop_id = a.shop_id");
        sql.append("                 and slip_no = a.slip_no");
        sql.append("                 and product_division not in (6)");
        sql.append("              ) as discount_detail_value_no_tax");
        sql.append("             ,(");
        sql.append("                 select");
        sql.append("                     sum(account_setting_value) as sum");
        sql.append("                 from");
        sql.append("                     view_data_sales_detail_valid");
        sql.append("                 where");
        sql.append("                         shop_id = a.shop_id");
        sql.append("                     and slip_no = a.slip_no");
        sql.append("              ) as account_setting_value_total");
        sql.append("         from");
        sql.append("             data_sales a");
        //nhanvt start edit 20141128 Bug #33247
        //sql.append("     inner join mst_customer mc ON mc.customer_id = a.customer_id and mc.customer_no <> '0'  and mc.delete_date is null ");
        //nhanvt start edit 20141128 Bug #33247
        sql.append("                 left outer join");
        sql.append("                             (");
        sql.append("                                 select");
        sql.append("                                     dsd.*");
        sql.append("                                 from");
        sql.append("                                     data_sales_detail dsd");
        sql.append("                                 where");
        sql.append("                                     not exists");
        sql.append("                                     (");
        sql.append("                                         select 1");
        sql.append("                                         from");
        sql.append("                                             mst_technic mt");
        sql.append("                                                 join mst_technic_class mtc");
        sql.append("                                                 using (technic_class_id)");
        sql.append("                                         where");
        sql.append("                                                 mtc.prepaid = 1");
        sql.append("                                             and mt.technic_id = dsd.product_id");
        sql.append("                                             and dsd.product_division in (1,3)");
        sql.append("                                     )");
        sql.append("                             ) b");
        sql.append("                              on a.shop_id = b.shop_id");
        sql.append("                             and a.slip_no = b.slip_no");
        sql.append("                             and b.product_division = 0");
        sql.append("                             and b.delete_date is null");
        sql.append("         where");
        sql.append("             a.delete_date is null");
        sql.append("     ) a");

        return sql.toString();
    }
    
    private String getCancelContractSQL(Boolean isMonth) {
            StringBuilder sql = new StringBuilder();
            
            if (SystemInfo.getAccountSetting().getDisplayPriceType() == 0) {
                //�ō�
                sql.append(" select sum(dpd.payment_value) as total_cancel_contract_value ");
            }else {
                //�Ŕ� �_�񎞂̐ŗ��Ōv�Z
                sql.append(" select ceil( sum( dpd.payment_value / (1.0 + get_tax_rate(t.contract_date)) ) ) as total_cancel_contract_value ");
            }
            sql.append(" , sum(dpd.payment_value) as total_cancel_contract_value_in_tax ");
            sql.append(" from (select distinct ds.shop_id, ds.slip_no, ds2.sales_date as contract_date from ");
            sql.append("  data_sales ds left join data_sales_detail dsd ");
            sql.append("  on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no ");
            sql.append(" left join data_contract dc on dsd.contract_no = dc.contract_no and dsd.contract_shop_id = dc.shop_id ");
            sql.append("   left join data_sales ds2 on dc.shop_id = ds2.shop_id and dc.slip_no = ds2.slip_no ");
            sql.append("  where ds.delete_date is null ");
            sql.append("     and dsd.product_division = 8 ");
            sql.append(" and ds.shop_id = ").append(SQLUtil.convertForSQL(this.getShopID()));
            if(isMonth) {
                sql.append(" and ds.sales_date between ").append(SQLUtil.convertForSQLDateOnly(this.calStart));
                sql.append(" and ").append(SQLUtil.convertForSQLDateOnly(this.calEnd));
            }else {
                sql.append(" and ds.sales_date = ").append(SQLUtil.convertForSQLDateOnly(this.getManageDate()));
            }
            sql.append("  ) t");
            sql.append(" left join data_payment_detail dpd ");
            sql.append(" on t.shop_id = dpd.shop_id and t.slip_no = dpd.slip_no ");

            return sql.toString();
    }
    
    private String getCourseContractSQL (Boolean isMonth) {
            StringBuilder sql = new StringBuilder();
            
            if (SystemInfo.getAccountSetting().getDisplayPriceType() == 0) {
                //�ō�
                sql.append(" select sum(discount_detail_value_in_tax) as total_contract_value ");
            }else {
                //�Ŕ�
                sql.append(" select sum(discount_detail_value_no_tax) as total_contract_value ");
            }
            sql.append(" from view_data_sales_detail_valid where product_division = 5 ");
            sql.append(" and shop_id = ").append(SQLUtil.convertForSQL(this.getShopID()));
            if (isMonth) {
                sql.append(" and sales_date between ").append(SQLUtil.convertForSQLDateOnly(this.calStart));
                sql.append(" and ").append(SQLUtil.convertForSQLDateOnly(this.calEnd));
            }else {
                sql.append(" and sales_date = ").append(SQLUtil.convertForSQLDateOnly(this.getManageDate()));
            }

            return sql.toString();
    }
    
    private String getChangeContractSQL (Boolean isMonth) {
            StringBuilder sql = new StringBuilder();
            
            if (SystemInfo.getAccountSetting().getDisplayPriceType() == 0) {
                //�ō�
                sql.append(" select sum ( (t3.payment_value + t3.bill_value - t3.change_value) ) as total_change_contract_value ");
            }else {
                //�Ŕ�
                sql.append(" select ceil (sum( (t3.payment_value + t3.bill_value - t3.change_value)  / (1.0 + get_tax_rate(t3.sales_date)) ) ) as total_change_contract_value ");
            }
            sql.append(" , sum ( (t3.payment_value + t3.bill_value - t3.change_value) ) as total_change_contract_value_in_tax ");
            sql.append("  , sum(t3.payment_value + t3.bill_value - t3.change_value) - sum(ceil((t3.payment_value + t3.bill_value - t3.change_value) / (1.0 + get_tax_rate(t3.sales_date))) ) as change_contract_tax");
            sql.append(" from (select * , (select bill_value from data_payment dp where dp.shop_id = t2.shop_id and dp.slip_no = t2.slip_no order by dp.payment_no desc limit 1) as bill_value ");
            sql.append(" , (select change_value from data_payment dp where dp.shop_id = t2.shop_id and dp.slip_no = t2.slip_no order by dp.payment_no limit 1) as change_value ");
            sql.append(" from (select t.sales_date, t.shop_id, t.slip_no, sum(coalesce(dpd.payment_value, 0)) as payment_value from ");
            sql.append(" (select distinct shop_id, slip_no, sales_date from view_data_sales_detail_valid ");
            sql.append(" where product_division = 7 ");
            sql.append(" and shop_id = ").append(SQLUtil.convertForSQL(this.getShopID()));
            if (isMonth) {
                sql.append(" and sales_date between ").append(SQLUtil.convertForSQLDateOnly(this.calStart));
                sql.append(" and ").append(SQLUtil.convertForSQLDateOnly(this.calEnd));
            }else {
                sql.append(" and sales_date = ").append(SQLUtil.convertForSQLDateOnly(this.getManageDate()));
            }
            sql.append("  ) t   left join data_payment_detail dpd on t.shop_id = dpd.shop_id and t.slip_no = dpd.slip_no ");
            sql.append(" group by t.shop_id, t.slip_no, t.sales_date) t2    ) t3 ");

            return sql.toString();
    }
    
    private String getTotalSlipCountSQL(Boolean isMonth) {
            StringBuilder sql = new StringBuilder();
            
            sql.append(" select count(*) as total_count from data_sales ");
            sql.append(" where delete_date is null ");
            sql.append(" and shop_id = ").append(SQLUtil.convertForSQL(this.getShopID()));
            if(isMonth) {
                sql.append(" and sales_date between ").append(SQLUtil.convertForSQLDateOnly(this.calStart));
                sql.append(" and ").append(SQLUtil.convertForSQLDateOnly(this.calEnd));
            }else {
                sql.append(" and sales_date = ").append(SQLUtil.convertForSQLDateOnly(this.getManageDate()));
            }

            return sql.toString();
    }
    
    /**
     * �O�Ł��������z���犄���ݒ�̏ꍇ�ƁA����ȊO�̐ݒ�ƂŎg�p����SQL��ς��܂�
     * @param paramBean
     * @return
     */
    private String getTechItemCourseValueQuery() {
        String sql = "";

        // �O�Ł��Ŕ����z���犄��
        sql = "case when  discount_value != 0 then ( ceil(product_value / (1 + get_tax_rate(tax_rate, sales_date))) * product_num) - discount_value + trunc( ( ( ceil(product_value / (1 + get_tax_rate(tax_rate, sales_date))) * product_num) - discount_value) * get_tax_rate(tax_rate, sales_date)) else detail_value_in_tax end ";

        return sql;
    }
    
    private void setDataCollectionPeriod() {
            
            this.calStart = Calendar.getInstance();
            
            // �W�v���ԊJ�n��
            this.calStart = Calendar.getInstance();
            this.calStart.setTime(this.getManageDate().getTime());
            this.calStart.set(Calendar.DAY_OF_MONTH, 1);
            // �W�v���ԏI����
            this.calEnd = (Calendar) this.calStart.clone();;
            this.calEnd.add(Calendar.MONTH, 1);
            this.calEnd.add(Calendar.DAY_OF_MONTH, -1);

            // �Ώۓ��t�������𒴂����ꍇ�͗����̌��݌v�Ƃ���
            int cutoffDay = this.getShop().getCutoffDay().intValue();
            Calendar calTmp = Calendar.getInstance();
            calTmp.setTime(this.getManageDate().getTime());
            if (cutoffDay < calTmp.get(Calendar.DAY_OF_MONTH)) {
                this.calStart.add(Calendar.MONTH, 1);
                this.calEnd.add(Calendar.MONTH, 2);
                this.calEnd.set(Calendar.DAY_OF_MONTH, 1);
                this.calEnd.add(Calendar.DAY_OF_MONTH, -1);
            }

            // ���ߓ����l���������Ԃ��擾
            this.resetSpan(this.calStart, this.calEnd);

            // �Ώۓ��t���W�v���Ԃ̏I������菬�����ꍇ�͑Ώۓ��t���W�v���ԏI�����Ƃ���
            // (�ߋ��̃��W�����Q�Ƃ��Ă���ꍇ�͂��̓��܂ł̏W�v�Ƃ���)
            if (this.calEnd.after(calTmp)) {
                this.calEnd.setTime(calTmp.getTime());
            }
    }
}
