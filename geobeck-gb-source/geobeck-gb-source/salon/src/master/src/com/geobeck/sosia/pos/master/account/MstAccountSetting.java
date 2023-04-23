/*
 * MstAccountSetting.java
 *
 * Created on 2007/11/28, 15:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.master.account;

import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.AccountUtil;
import com.ibm.icu.text.DecimalFormat;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Logger;

/**
 * 会計設定マスタクラス
 *
 * @author katagiri
 */
public class MstAccountSetting {

    /**
     * メニュー表示。
     * 
     * 0 = 内税, 1 = 外税
     */
    private Integer displayPriceType = null;
    /**
     * 割引設定。
     * 
     * 0 = 税込から割引, 1 = 税抜から割引
     */
    private Integer discountType = null;
    
    /** 会計単位。
     * 
     * 0 = 1円単位, 1 = 10円単位
     */
    private Integer accountingUnit = null;
    /**
     * 端数丸め。
     * 
     * 0 = 切り捨て, 1 = 四捨五入, 2 = 切り上げ
     */
    private Integer rounding = null;
    /**
     * 締日。
     * 
     * 31 = 末日
     */
    private Integer cutoffDay = null;
    /**
     * 帳票税区分。
     * 
     * 0 = 税込, 1 = 税抜
     */
    private Integer reportPriceType = null;
    /**
     * 前月以前のデータ更新
     */
    private Integer prevSalesUpdate = null;
    
     /**
     * 店舗のエリア表示 
     */
     private Integer displayArea = null;
     
     // IVS_Thanh start add 2014/07/11 Mashu_お会計表示
     private Integer displaySwitchTechnic = null;

     public Integer getDisplaySwitchTechnic() {
        return displaySwitchTechnic;
     }
    
     public void setDisplaySwitchTechnic(Integer displaySwitchTechnic) {
        this.displaySwitchTechnic = displaySwitchTechnic;
     }
     // IVS_Thanh start add 2014/07/11 Mashu_お会計表示
     
     private Integer creditLockManage = null;
     
     //nhanvt start add 20150325 Bug #35729
     private Double TaxRate = null;
     

    public Double getTaxRate() {
        return TaxRate;
    }

    public void setTaxRate(Double TaxRate) {
        this.TaxRate = TaxRate;
    }
    //nhanvt end add 20150325 Bug #35729
    /**
     * コンストラクタ
     */
    public MstAccountSetting() {
    }

    /**
     * メニュー表示を取得する
     *
     * @return メニュー表示
     */
    public Integer getDisplayPriceType() {
        return displayPriceType;
    }

    /**
     * メニュー表示を設定する
     *
     * @param displayPriceType メニュー表示
     */
    public void setDisplayPriceType(Integer displayPriceType) {
        this.displayPriceType = displayPriceType;
    }

    /**
     * 割引設定を取得する
     *
     * @return 割引設定
     */
    public Integer getDiscountType() {
        return discountType;
    }

    /**
     * 割引設定を設定する
     *
     * @param discountType 割引設定
     */
    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    /**
     * 会計単位を取得する
     *
     * @return 会計単位
     */
    public Integer getAccountingUnit() {
        return accountingUnit;
    }

    /**
     * 会計単位を設定する
     *
     * @param accountingUnit 会計単位
     */
    public void setAccountingUnit(Integer accountingUnit) {
        this.accountingUnit = accountingUnit;
    }

    /**
     * 端数丸めを取得する
     *
     * @return 端数丸め
     */
    public Integer getRounding() {
        return rounding;
    }

    /**
     * 端数丸めを設定する
     *
     * @param rounding 端数丸め
     */
    public void setRounding(Integer rounding) {
        this.rounding = rounding;
    }

    /**
     * 締日を取得する
     *
     * @return 締日
     */
    public Integer getCutoffDay() {
        return cutoffDay;
    }

    /**
     * 締日を設定する
     *
     * @param cutoffDay 締日
     */
    public void setCutoffDay(Integer cutoffDay) {
        this.cutoffDay = cutoffDay;
    }

    /**
     * 帳票税区分を取得する
     *
     * @return 帳票税区分
     */
    public Integer getReportPriceType() {
        return reportPriceType;
    }

    /**
     * 帳票税区分を設定する
     *
     * @param reportPriceType 帳票税区分
     */
    public void setReportPriceType(Integer reportPriceType) {
        this.reportPriceType = reportPriceType;
    }

    /**
     * @return the prevSalesUpdate
     */
    public Boolean isPrevSalesUpdate() {
        return (prevSalesUpdate == 1);
    }

    /**
     * @return the prevSalesUpdate
     */
    public Integer getPrevSalesUpdate() {
        return prevSalesUpdate;
    }

    /**
     * @param prevSalesUpdate the prevSalesUpdate to set
     */
    public void setPrevSalesUpdate(Integer prevSalesUpdate) {
        this.prevSalesUpdate = prevSalesUpdate;
    }
    
    // IVS SANG START INSERT 20131103 [gbソース]ソースマージ
    public Integer getCreditLockManage() {
        return creditLockManage;
    }

    public void setCreditLockManage(Integer creditLockManage) {
        this.creditLockManage = creditLockManage;
    }
    // IVS SANG END INSERT 20131103 [gbソース]ソースマージ
    
    public void setDatas(ResultSetWrapper rs) throws SQLException {
        this.setDisplayPriceType(rs.getInt("display_price_type"));
        this.setDiscountType(rs.getInt("discount_type"));
        this.setAccountingUnit(rs.getInt("accounting_unit"));
        this.setRounding(rs.getInt("rounding"));
        this.setCutoffDay(rs.getInt("cutoff_day"));
        this.setReportPriceType(rs.getInt("report_price_type"));
        this.setPrevSalesUpdate(rs.getInt("prev_sales_update"));
        // IVS_Thanh start add 2014/07/11 Mashu_お会計表示
        this.setDisplaySwitchTechnic(rs.getInt("display_switch_technic"));
        // IVS_Thanh end add 2014/07/11 Mashu_お会計表示
        // IVS SANG START INSERT 20131030 [gbソース]ソースマージ
        this.setDisplayArea(rs.getInt("display_area"));
        //Luc start add 20130528
        try {
            this.setCreditLockManage(rs.getInt("credit_lock_manage"));
        }catch (Exception e) {
            
        }
        //Luc end add 20130528
        // IVS SANG END INSERT 20131030 [gbソース]ソースマージ
        //Luc start add 20141202 期末を追加
        try {
            this.setPeriodMonth(rs.getInt("period_month"));
        }catch (Exception e) {
            
        }
        //Luc end add 20141202 期末を追加
    }

    public boolean isExist(ConnectionWrapper con) throws SQLException {
        boolean result = false;

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        if (rs.next()) {
            result = true;
        }

        rs.close();

        return result;
    }

    public boolean load(ConnectionWrapper con) throws SQLException {
        boolean result = false;

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        if (rs.next()) {
            this.setDatas(rs);
            result = true;
        }

        rs.close();

        return result;
    }

    public boolean regist(ConnectionWrapper con) throws SQLException {
        boolean result = false;

        if (this.isExist(con)) {
            result = con.executeUpdate(this.getUpdateSQL()) == 1;
        } else {
            result = con.executeUpdate(this.getInsertSQL()) == 1;
        }

        SystemInfo.setAccountSetting(null);

        return result;
    }

    private String getSelectSQL() {
        return "select *\n"
                + "from mst_account_setting\n";
    }

    private String getInsertSQL() {
        return "insert into mst_account_setting\n"
                + "(display_price_type, discount_type,\n"
                + "accounting_unit, rounding, cutoff_day,\n"
                //IVS_LVTu start edit 2014/07/11 Mashu_会社情報登録
                //+ "insert_date, update_date, delete_date, report_price_type, prev_sales_update)\n"
                + "insert_date, update_date, delete_date, report_price_type, prev_sales_update, display_switch_technic,period_month)\n"
                 //IVS_LVTu end edit 2014/07/11 Mashu_会社情報登録
                + "values(\n"
                + SQLUtil.convertForSQL(this.getDisplayPriceType()) + ",\n"
                + SQLUtil.convertForSQL(this.getDiscountType()) + ",\n"
                + SQLUtil.convertForSQL(this.getAccountingUnit()) + ",\n"
                + SQLUtil.convertForSQL(this.getRounding()) + ",\n"
                + SQLUtil.convertForSQL(this.getCutoffDay()) + ",\n"
                + "current_timestamp, current_timestamp, null,\n"
                + SQLUtil.convertForSQL(this.getReportPriceType()) + ",\n"
                + SQLUtil.convertForSQL(this.getPrevSalesUpdate()) + ",\n"
                //IVS_LVTu start add 2014/07/11 Mashu_会社情報登録
                + SQLUtil.convertForSQL(this.getDisplaySwitchTechnic()) + ",\n"
                //IVS_LVTu end add 2014/07/11 Mashu_会社情報登録
                //Luc start add 20141202 期末を追加
                + SQLUtil.convertForSQL(this.getPeriodMonth()) + ")\n"; 
                //Luc end add 20141202 期末を追加
    }

    private String getUpdateSQL() {
        return "update mst_account_setting\n"
                + "set\n"
                + "display_price_type = " + SQLUtil.convertForSQL(this.getDisplayPriceType()) + ",\n"
                + "discount_type = " + SQLUtil.convertForSQL(this.getDiscountType()) + ",\n"
                + "accounting_unit = " + SQLUtil.convertForSQL(this.getAccountingUnit()) + ",\n"
                + "rounding = " + SQLUtil.convertForSQL(this.getRounding()) + ",\n"
                + "cutoff_day = " + SQLUtil.convertForSQL(this.getCutoffDay()) + ",\n"
                + "update_date = current_timestamp,\n"
                + "report_price_type = " + SQLUtil.convertForSQL(this.getReportPriceType()) + ",\n"
                + "prev_sales_update = " + SQLUtil.convertForSQL(this.getPrevSalesUpdate()) + ",\n"
                //IVS_LVTu start add 2014/07/11 Mashu_会社情報登録
                + "display_switch_technic = " +SQLUtil.convertForSQL(this.getDisplaySwitchTechnic()) + ",\n"
                //IVS_LVTu end add 2014/07/11 Mashu_会社情報登録
                //Luc start add 20141202 期末を追加
                + "period_month = " +SQLUtil.convertForSQL(this.getPeriodMonth()) + "\n";
                //Luc end add 20141202 期末を追加

    }

    public Long getDisplayValue(Long value, Double taxRate) {
        //vtbphuong start edit 20140415 Request #22169
//        if (this.getDisplayPriceType() == 1) {
//            //Luc start edit 20140324
//           // return this.round(value.doubleValue()
//           //         - Math.signum(value.doubleValue())
//           //         * Math.floor(Math.abs(value.doubleValue()) / (1d + taxRate) * taxRate));
//             return Math.round(value.doubleValue() / (1d + taxRate));
//            //Luc end edit 20140324
//        } else {
//            return value;
//        }
        return getDisplayValue(value, 0L, taxRate);
        //vtbphuong start edit 20140415 Request #22169
        
    }

    public Long getDisplayValue(Long value, Long discount, Double taxRate) {
        Long taxValue = this.getTax(value, discount, taxRate);
        Long result = 0l;

        // 符号取得
        long signum = (long) Math.signum(value.doubleValue());
        if (signum == 0) {
            signum = 1;
        }

        if (this.getDisplayPriceType() == 0) {

            if (this.getDiscountType() == 0) {

                /**
                 * *******************************
                 */
                // 内税表示・税込額から割引
                /**
                 * *******************************
                 */
                result = value - discount;

            } else {

                /**
                 * *******************************
                 */
                // 内税表示・税抜額から割引
                /**
                 * *******************************
                 */
//                        result = this.round(value.doubleValue() - Math.floor(value.doubleValue() / (1d + taxRate) * taxRate));
//                        result = ((Double)Math.ceil(value.doubleValue() / (1d + taxRate))).longValue();
                if (discount == 0) {

                    result = Math.abs(value);

                } else {
                    // vtbphuong start change 20140812  Bug #29346
//                    double dtmp = Math.round(Math.abs(value.doubleValue()) / (1d + taxRate));
//                    dtmp = dtmp - discount;
//
//                    if (value.longValue() == 0) {
//                        result = (long) dtmp;
//                    } else {
//                         //vtbphuong start change 20140612 Bug #25038 sheet 3
//                        result = ((Double) Math.floor(dtmp * (1d + taxRate))).longValue();
////                         BigDecimal a = new BigDecimal(dtmp * (1d + taxRate));
////                         a = a.setScale(3, RoundingMode.HALF_UP);
////                         //result = ((Double) Math.ceil(dtmp * (1d + taxRate))).longValue();
////                        result = ((Double) Math.ceil(a.doubleValue())).longValue();
//                         //vtbphuong end change 20140612 Bug #25038  sheet 3
                      double dtmp =Math.abs(this.getTax(0, discount, taxRate));
                      result = value.longValue() - discount - (long) dtmp;
                       // vtbphuong end change 20140812 Bug #29346
                }

                result = signum * result.longValue();
            }

        } else {

            if (this.getDiscountType() == 0) {

                /**
                 * *******************************
                 */
                // 外税表示・税込額から割引
                /**
                 * *******************************
                 */
                result = value - discount - taxValue;

            } else {

                /**
                 * *******************************
                 */
                // 外税表示・税抜額から割引
                /**
                 * *******************************
                 */
//                        result = this.round(value.doubleValue() - Math.floor(value.doubleValue() / (1d + taxRate) * taxRate));
//                        result = ((Double)Math.ceil(value.doubleValue() / (1d + taxRate))).longValue();
                //vtbphuong start change 20140415 Request #22169
               //double dtmp = Math.abs(value.doubleValue()) / (1d + taxRate))
                BigDecimal a = new BigDecimal(Math.abs(value.doubleValue()) / (1d + taxRate));
                a = a.setScale(3, RoundingMode.HALF_UP);
                double dtmp = a.doubleValue();                
                result = ((Double) Math.ceil(dtmp - discount)).longValue();
                result = signum * result.longValue();
                //vtbphuong start change 20140415 Request #22169
                
            }

        }

        return result;
    }

     
     public Long getDisplayValue(Long value,Long num, Long discount, Double taxRate) {
        Long taxValue = this.getTax(value, discount, taxRate);
        Long result = 0l;

        // 符号取得
        long signum = (long) Math.signum(value.doubleValue());
        if (signum == 0) {
            signum = 1;
        }

        if (this.getDisplayPriceType() == 0) {

            if (this.getDiscountType() == 0) {

                /**
                 * *******************************
                 */
                // 内税表示・税込額から割引
                /**
                 * *******************************
                 */
                result = value - discount;

            } else {

                /**
                 * *******************************
                 */
                // 内税表示・税抜額から割引
                /**
                 * *******************************
                 */
                if (discount == 0) {

                    result = Math.abs(value);

                } else {
                    //TVHoa start edit 20150717 Bug #40178
                    //double dtmp =Math.abs(this.getTax(0, discount, taxRate));
                    //result = value.longValue() - discount - (long) dtmp;
                    result = (long) ((Math.ceil((value / (1 +taxRate)))  - discount) *(1+ taxRate)) ;
                    //TVHoa end edit 20150717 Bug #40178
                }

                result = signum * result.longValue();
            }

        } else {

            if (this.getDiscountType() == 0) {

                /**
                 * *******************************
                 */
                // 外税表示・税込額から割引
                /**
                 * *******************************
                 */
                result = value - discount - taxValue;

            } else {

                /**
                 * *******************************
                 */
                // 外税表示・税抜額から割引
                /**
                 * *******************************
                 */
                
                Double price = (num !=0 ? value.doubleValue()/num:0d);
                
                Long basePrice;
                BigDecimal a = new BigDecimal(Math.abs(price) / (1d + taxRate));
                a = a.setScale(3, RoundingMode.HALF_UP);
                double dtmp = a.doubleValue();                
                basePrice = ((Double) Math.ceil(dtmp)).longValue();
                result = basePrice*num - discount;
                result = signum * result.longValue();
            }

        }

        return result;
    }

    public Double getDisplayValue(Double value, Long discount, Double taxRate) {
        Double taxValue = this.getTax(value, discount, taxRate);
        Double result = 0d;

        // 符号取得
        long signum = (long) Math.signum(value.doubleValue());
        if (signum == 0) {
            signum = 1;
        }

        if (this.getDisplayPriceType() == 0) {

            if (this.getDiscountType() == 0) {

                /**
                 * *******************************
                 */
                // 内税表示・税込額から割引
                /**
                 * *******************************
                 */
                result = value - discount;

            } else {

                /**
                 * *******************************
                 */
                // 内税表示・税抜額から割引
                /**
                 * *******************************
                 */
//                        result = this.round(value.doubleValue() - Math.floor(value.doubleValue() / (1d + taxRate) * taxRate));
//                        result = ((Double)Math.ceil(value.doubleValue() / (1d + taxRate))).longValue();
                if (discount == 0) {

                    result = Math.abs(value);

                } else {

                    double dtmp = Math.abs(value.doubleValue()) / (1d + taxRate);
                    dtmp = dtmp - discount;

                    if (value.longValue() == 0) {
                        result = dtmp;
                    } else {
                        result = (Double) Math.floor(dtmp * (1d + taxRate));
                    }
                }

                result = signum * result;
            }

        } else {

            if (this.getDiscountType() == 0) {

                /**
                 * *******************************
                 */
                // 外税表示・税込額から割引
                /**
                 * *******************************
                 */
                result = value - discount - taxValue;

            } else {

                /**
                 * *******************************
                 */
                // 外税表示・税抜額から割引
                /**
                 * *******************************
                 */
//                        result = this.round(value.doubleValue() - Math.floor(value.doubleValue() / (1d + taxRate) * taxRate));
//                        result = ((Double)Math.ceil(value.doubleValue() / (1d + taxRate))).longValue();
                double dtmp = Math.abs(value.doubleValue()) / (1d + taxRate);
                result = (Double) Math.ceil(dtmp - discount);
                result = dtmp - discount;
                result = signum * result;
            }

        }

        BigDecimal a = new BigDecimal(result);
        a = a.setScale(0, RoundingMode.UP);
        result = a.doubleValue();

        return result;
    }

    /**
     * mst_account_settingのdiscount_typeによって影響の出る額を算出し取得する。
     *
     * @param value 税込み額
     * @param discount 割引額
     * @param taxRate 消費税
     */
    public Long getDisplay(Long value, Long discount, Double taxRate) {
        //税抜きから割引
        if (this.getDiscountType() == 1) {
            Long taxValue = this.getTax(value, discount, taxRate);
            Long temp = 0l;
            temp = this.round(value.doubleValue()
                    - Math.floor(value.doubleValue() / (1d + taxRate) * taxRate));
            return temp - discount + taxValue;
        } //税込みから割引
        else {
            return value - discount;
        }

    }

    public Long getDiscountValue(Long value, Double discountRate, Double taxRate) {
        if (this.getDiscountType() == 1) {
            
            //IVS_LVTu start edit 20150415 Bug #36268
            //return this.round((value.doubleValue()
                    //- Math.floor(value.doubleValue() / (1d + taxRate) * taxRate)) * discountRate);
            BigDecimal a = new BigDecimal(value/(1+taxRate));
            a = a.setScale(3, RoundingMode.HALF_UP);
            Long taxOffValue = ((Double) Math.ceil(a.doubleValue())).longValue();
            return  this.round(taxOffValue*discountRate);
            //IVS_LVTu end edit 20150415 Bug #36268
        } else {
            return this.round(value.doubleValue() * discountRate);
        }
    }

    /**
     * 消費税を計算する。<br />
     * 
     * <h3>getDiscountType() == 0 (税込から割引) の場合</h3>
     * 
     * <pre>
     * 請求金額 = 明細金額 - 割引額
     * 税抜金額 = 請求金額 / 1.05
     * 消費税   = 税抜金額 * 0.05      ※計算結果の小数点以下は切り捨てる。
     * </pre>
     * 
     * <h3>getDiscountType() == 1 (税抜から割引) の場合</h3>
     * 
     * <pre>
     * 税抜金額 = 明細金額 / 1.05
     * 税抜金額に対する消費税 = 税抜金額 * 0.05     ※計算結果の小数点以下は切り捨てる。
     * 割引額に対する消費税  = 割引額 * 0.05        ※計算結果の小数点以下は切り捨てる
     * 本当の消費税 = 税抜金額に対する消費税 - 割引額に対する消費税
     * </pre>
     * 
     * @param value 明細金額 [円]
     * @param discount 割引金額 [円]
     * @param taxRate 消費税 (例: 5% ⇒ 0.05)
     * @return 消費税 [円]
     */
     public long getTax(long value, long discount, double taxRate) {
        if (this.getDiscountType() == 1) {
            // 税抜金額
          //  double netPrice = Math.round(value / (1d + taxRate));
              BigDecimal a = new BigDecimal(value / (1d + taxRate));
              a = a.setScale(3, RoundingMode.HALF_UP);
              double dtmp = a.doubleValue(); 
              Double netPrice = 0D;
              if(dtmp > 0){
                 netPrice = Math.ceil(dtmp);
              }else{
                 netPrice = Math.floor(dtmp);
              }  
            //TVHoa start edit 20150702 Bug #38901
            if (discount == 0) {
                //return value - netPrice;
                long tax = 0l;
                tax = (long) Math.floor(netPrice * taxRate);
                return tax;
            } else {

                long tax;
	        //TVHoa start edit 20150713 Bug #40178
                if (this.getDisplayPriceType()==1){
                   tax = (long) Math.floor((netPrice - discount) * taxRate);
                }else{
                    //TVHoa start edit 20150717 Bug #40178
                    if(this.getDiscountType()==1){
                        //tax = (long) (value - netPrice - Math.floor(discount * taxRate)); 
                        if(value ==0){
                            tax = -1 * (long) (Math.ceil(discount * (1+taxRate))- discount) ;
                        }else{
                            tax = (long) ((value - Math.ceil(discount * (1+taxRate))) - ((value - Math.ceil(discount * (1+taxRate)))/ (1+taxRate)));
                        }                        
                    }else{
                        tax = (long) (value - netPrice - Math.ceil(discount * taxRate));
                    }
                    //tax = (long) (value - netPrice - Math.ceil(discount * taxRate));
                    //TVHoa end edit 20150717 Bug #40178
                }    
                //TVHoa End edit 20150702 Bug #38901
		//TVHoa end edit 20150713 Bug #40178
                return tax;
           }    
              // vtbphuong end change 20140813  Bug #29557 
        } else {
            // 請求金額
            long charge = value - discount;
            // 税抜金額
            //double netPrice = Math.round(charge / (1d + taxRate) * taxRate);
            double netPrice = Math.signum(charge) * Math.ceil(Math.abs(charge / (1d + taxRate)));
            // 消費税
            double tax = charge - netPrice ;   
            return (long) tax;
        }
    }
     // vtbphuong end change 20140605 Bug #24803
     
     // vtbphuong start add 20150604 
        public long getTax(long value, long discount, double taxRate, long netPrice) {
        if (this.getDiscountType() == 1) {
            // 税抜金額
          //  double netPrice = Math.round(value / (1d + taxRate));
              BigDecimal a = new BigDecimal(value / (1d + taxRate));
              a = a.setScale(3, RoundingMode.HALF_UP);
 
              //TVHoa start edit 20150702 Bug #38901
              long newvalue;
              if (this.getDisplayPriceType()==1){
                   newvalue = (long)((netPrice * (1+taxRate)) - (discount* (1+taxRate))) ;
              }else{
                   //TVHoa start edit 20150717 Bug #40178
                   newvalue = (long)(netPrice  -  Math.floor(discount* (1+taxRate))) ;
                   //TVHoa END edit 20150717 Bug #40178
              }
   
              if(discount == 0){
                  //return value - netPrice;
                  long tax = 0l;
                  if(displayPriceType == 1) {
                    //IVS_LVTu start edit 2015/07/22 Bug #40809
                    //tax =   (long) Math.floor(netPrice * taxRate);
                    tax = value - netPrice;
                    //IVS_LVTu end edit 2015/07/22 Bug #40809
                  }else {
                    tax =   netPrice- (long) Math.ceil(newvalue / (1+taxRate));
                  }
                  //LVTu end edit 2015/05/13 Bug #36668
                  return tax;
              }else{
                  //LVTu start edit 2015/05/13 Bug #36668
                  //long tax =  (long) AccountUtil.floor(newvalue * taxRate);
                  long tax;
                  if(displayPriceType == 1) {
                      //TVHoa start edit 20150713 Bug #40178
                      //tax =    (long)(netPrice * (1+taxRate)) - netPrice  - (long) Math.floor(discount * taxRate);
                      //IVS_PTThu strart edit 2015-05-20 New request #50523
                      Double t = (netPrice-discount) * taxRate;
                      
                      //tax =    (long) Math.floor((netPrice-discount) * taxRate);
                      if (t >= 0) {
                          tax = (long) Math.floor(t);
                      } else {
                          tax = -(long) Math.floor(Math.abs(t));
                      }
                      //IVS_PTThu end edit 2015-05-20 New request #50523
                      //TVHoa End edit 20150713 Bug #40178
                  }else {
                      //TVHoa start edit 20150717 Bug #40178
                      //tax =   (long) ((netPrice -  Math.ceil(netPrice /(1+ taxRate)))- ((discount* (1+taxRate))-discount) );
                      tax = (long) ((value - Math.ceil(discount * (1+taxRate))) - ((value - Math.ceil(discount * (1+taxRate)))/ (1+taxRate)));
                      //TVHoa end edit 20150717 Bug #40178
                  }
                  //LVTu end edit 2015/05/13 Bug #36668
                  //TVHoa End edit 20150702 Bug #38901
                  return tax;
              }
              
              // vtbphuong end change 20140813  Bug #29557 
        } else {
            // 請求金額
            long charge = value - discount;
            // 税抜金額
            //double netPrice = Math.round(charge / (1d + taxRate) * taxRate);
            Double temp = Math.signum(charge) * Math.ceil(Math.abs(charge / (1d + taxRate)));
            netPrice = temp.longValue();
            // 消費税
            double tax = charge - netPrice ;   
            return (long) tax;
        }
    }
     // vtbphuong start add 20150604 

    public double getTax(double value, long discount, double taxRate) {
        return getTax((long) value, discount, taxRate);
    }

    public Double getTaxDiscount(Double value, Long discount, Double taxRate) {
        Double temp = 0d;

        if (this.getDiscountType() == 1) {
            temp = value - ((Double) Math.floor(value / (1d + taxRate) * taxRate)).longValue() - discount;
            return ((Double) (Math.signum(temp) * Math.abs(temp) * taxRate)).doubleValue();
        } else {
            temp = value - discount;
            //return this.round(Math.floor(temp / (1d + taxRate) * taxRate));
            return this.roundToDouble(Math.signum(temp / (1d + taxRate) * taxRate) * Math.floor(Math.abs(temp / (1d + taxRate) * taxRate)));
        }
    }

    public Long round(Double value) {
        Long result = 0l;

        switch (this.getRounding()) {
            case 0:
                result = ((Double) Math.floor(value)).longValue();
//				result	=	((Double)Math.ceil(value)).longValue();
                break;
            case 1:
                result = Math.round(value);
                break;
            case 2:
                result = ((Double) Math.ceil(value)).longValue();
//				result	=	((Double)Math.floor(value)).longValue();
                break;
        }

        return result;
    }

    public Double roundToDouble(Double value) {
        Double result = 0d;

        switch (this.getRounding()) {
            case 0:
                result = (Double) Math.floor(value);
                break;
            case 1:
                result = (double) Math.round(value);
                break;
            case 2:
                result = (Double) Math.ceil(value);
                break;
        }

        return result;
    }

    public Long accountRound(Double value) {
        Long result = 0l;

        switch (this.getAccountingUnit()) {
            case 0:
                result = this.round(value);
                break;
            case 1:
                result = this.round(value / 10d) * 10;
                break;
        }

        return result;
    }
    
     // IVS SANG START INSERT 20131030 [gbソース]ソースマージ
    
    /**
     * IVS_LVTU edit 2019/08/28 #97064 [gb]SPOS増税対応
     * @param value
     * @param taxRateBase 常税率
     * @return 
     */
    public Long getPriceNoTax(Long value, Double taxRateBase) {
        Long taxValue = this.getTax(value, 0l, taxRateBase);
            
        return value - taxValue;
    }

    /**
     * @return the displayArea
     */
    public Integer getDisplayArea() {
        return displayArea;
    }

    /**
     *
     *
     * @param displayArea 帳票税区分
     */
    public void setDisplayArea(Integer displayArea) {
        this.displayArea = displayArea;
    }
     // IVS SANG END INSERT 20131030 [gbソース]ソースマージ
     
     
    //IVS nahoang start add GB_Mashu_目標設定
    private Integer periodMonth = null;

    public Integer getPeriodMonth() {
        return periodMonth;
    }

    public void setPeriodMonth(Integer periodMonth) {
        this.periodMonth = periodMonth;
    }

    public boolean getSQLPeriodMonth() {
        boolean result = true;
        String sql = "select period_month from mst_account_setting";
        try {
            ConnectionWrapper con = SystemInfo.getConnection();
            ResultSetWrapper rs = con.executeQuery(sql);

            if (rs.next()) {
                this.setPeriodMonth(rs.getInt("period_month"));
            }

            rs.close();
            con.close();
        } catch (SQLException ex) {
            result = false;
            Logger.getLogger(MstAccountSetting.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return result;
    }
    //IVS nahoang end add GB_Mashu_目標設定
}
