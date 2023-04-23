/*
 * CalculatePoint.java
 *
 * Created on 2008/09/29, 15:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.pointcard;

import com.geobeck.sosia.pos.hair.data.account.DataSales;
import com.geobeck.sosia.pos.hair.data.account.DataSalesDetail;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

/**
 *
 * @author takeda
 */
public class CalculatePoint {

    /**
     * 発生ポイントを計算する
     *
     */
    public static long calcPoint( DataSales sales ){

        long newPoint = 0;
        long basePoint = 0;

        try {
            MstCustomer customer = sales.getCustomer();
            customer.load(SystemInfo.getConnection());

            // 販売データなし
            if( sales == null ) return 0;

            PointCalculateData point = new PointCalculateData();
            PointCalculateBean bean = point.getPointCalculateData(sales.getShop().getShopID());

            // ポイント発生データなし
            if( bean == null ) return 0;

            // 基本ポインを取得
            long acountValue = 0;

            // 加算対象フラグ
            boolean isBasicTarget = true;

            // 加算対象2（全顧客・SOSIA会員）
            if (bean.getBasicTarget2() == PointCalculateBean.BASIC_TARGET2_SOSIA) {
                if (customer.getSosiaCustomer().getSosiaID() == 0) {
                    isBasicTarget = false;
                }
            }

            if (isBasicTarget) {

                if (bean.getBasicTarget() == PointCalculateBean.BASIC_TARGET_TECH_ITEM) {

                    // 加算対象「技術＋商品」
                    acountValue = sales.getValueTotal();
                    if(bean.getBasicTaxType() == PointCalculateBean.BASIC_TAX_EXCLUDED ){
                        acountValue -=	SystemInfo.getTax(acountValue, sales.getSalesDate());
						acountValue -= sales.getAllDiscount();
						acountValue -= sales.getDetailDiscountTotal();
                    } else {
						//20200525 割引の税込金額を引くように変更
						acountValue -= sales.getAllDiscount();
						acountValue -= sales.getDetailDiscountTotal();
						acountValue -= 	SystemInfo.getTax(sales.getAllDiscount(), sales.getSalesDate());
						acountValue -= 	SystemInfo.getTax(sales.getDetailDiscountTotal(), sales.getSalesDate());
					}

                } else {

                    int div1, div2;

                    if (bean.getBasicTarget() == PointCalculateBean.BASIC_TARGET_TECH_ONLY) {
                        // 加算対象「技術のみ」
                        div1 = 1;
                        div2 = 3;
                    } else {
                        // 加算対象「商品のみ」
                        div1 = 2;
                        div2 = 4;
                    }

                    Long total = 0l;
                    Long discount = 0l;
                    for (DataSalesDetail dsd : sales) {
                        if (dsd.getProductDivision().equals(div1) || dsd.getProductDivision().equals(div2)) {
                            total += dsd.getValue();
                            if (dsd.getDiscountValue() != null) {
                                discount += dsd.getDiscountValue();
                            }
                        }
                    }
                    acountValue = total;
                    if(bean.getBasicTaxType() == PointCalculateBean.BASIC_TAX_EXCLUDED ){
                        acountValue -= SystemInfo.getTax(acountValue, sales.getSalesDate());
                    }
                    acountValue -= discount;
                }
            }

            //if( bean.getBasicRate() != 0 ){
            //    basePoint = acountValue * bean.getBasicPoint() / bean.getBasicRate();
            //}

            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      b.payment_class_id");
            sql.append("     ,a.payment_value");
            sql.append(" from");
            sql.append("     data_payment_detail a");
            sql.append("         join mst_payment_method b");
            sql.append("             using(payment_method_id)");
            sql.append(" where");
            sql.append("         a.shop_id = " + SQLUtil.convertForSQL(sales.getShop().getShopID()));
            sql.append("     and a.slip_no = " + SQLUtil.convertForSQL(sales.getSlipNo()));
            sql.append(" order by");
            sql.append("     b.payment_class_id");
            try {
                ResultSetWrapper result = SystemInfo.getConnection().executeQuery(sql.toString());
                while (acountValue > 0 && result.next()) {

                    long paymentValue = result.getLong("payment_value");

                    switch (result.getInt("payment_class_id")) {
                        case 1:
                            if (bean.getBasicRate() != 0) {
                                if (paymentValue < acountValue) {
                                    basePoint += paymentValue * bean.getBasicPoint() / bean.getBasicRate();
                                    acountValue -= paymentValue;
                                } else {
                                    basePoint += acountValue * bean.getBasicPoint() / bean.getBasicRate();
                                    acountValue -= paymentValue;
                                }
                            }
                            break;
                        case 2:
                            if (bean.getBasicRate2() != 0) {
                                if (paymentValue < acountValue) {
                                    basePoint += paymentValue * bean.getBasicPoint2() / bean.getBasicRate2();
                                    acountValue -= paymentValue;
                                } else {
                                    basePoint += acountValue * bean.getBasicPoint2() / bean.getBasicRate2();
                                    acountValue -= paymentValue;
                                }
                            }
                            break;
                        case 3:
                            if (bean.getBasicRate3() != 0) {
                                if (paymentValue < acountValue) {
                                    basePoint += paymentValue * bean.getBasicPoint3() / bean.getBasicRate3();
                                    acountValue -= paymentValue;
                                } else {
                                    basePoint += acountValue * bean.getBasicPoint3() / bean.getBasicRate3();
                                    acountValue -= paymentValue;
                                }
                            }
                            break;
                        case 4:
                            if (bean.getBasicRate4() != 0) {
                                if (paymentValue < acountValue) {
                                    basePoint += paymentValue * bean.getBasicPoint4() / bean.getBasicRate4();
                                    acountValue -= paymentValue;
                                } else {
                                    basePoint += acountValue * bean.getBasicPoint4() / bean.getBasicRate4();
                                    acountValue -= paymentValue;
                                }
                            }
                            break;
                    }
                }
            } catch( Exception e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                System.out.println( e.getLocalizedMessage() );
            }

            newPoint = basePoint;

            // 来店ポイント
            if ( isBasicTarget && bean.getVisitPoint() != 0 ) {
                newPoint += bean.getVisitPoint();
            }

            // 初回来店ポイント
            if( bean.getFirstTimeEnabled() == PointCalculateBean.FIRST_TIME_VISIT_ENABLED ){
                // 初回来店ポイント
                int visitNum = getVisitNum(sales);
                if( visitNum == 0 ){    // 来店無し＝初回来店
                    if( bean.getFirstTimePoint() != 0 ){
                        newPoint += bean.getFirstTimePoint();
                    }else if( bean.getFirstTimeRate() != 0.0 ){
                        newPoint += basePoint * (bean.getFirstTimeRate() - 1);
                    }
                }
            }


            if( bean.getBirthdayEnabled() == PointCalculateBean.BIRTHDAY_ENABLED ){
                if( customer.getBirthday() != null ){
                    // 誕生月ポイント
                    Integer birthMonth = customer.getBirthMonth() - 1;
                    Integer birthDay   = customer.getBirthDay();
                    boolean fBirth = false;

                    if( bean.getBirthdayCond() == PointCalculateBean.BIRTHDAY_BEFORE_AFTER ){
                        Calendar from            = Calendar.getInstance();
                        Calendar to              = Calendar.getInstance();
                        Calendar birth           = Calendar.getInstance();
                        Calendar birthBeforeYear = Calendar.getInstance();
                        Calendar birthAfterYear  = Calendar.getInstance();

                        from.setTime(sales.getSalesDate());
                        to.setTime(sales.getSalesDate());

                        Integer range = bean.getBirthdayRange();
                        from.add(Calendar.DATE,-1 * range);
                        from.add(Calendar.MILLISECOND,-1);
                        to.add(Calendar.DATE, range);
                        to.add(Calendar.DATE, 1);

                        int salesYear = from.get(Calendar.YEAR);

                        // 前後一年の日付を取得
                        birthBeforeYear.set(salesYear-1, birthMonth, birthDay, 0, 0, 0);
                        if( birthBeforeYear.get(Calendar.DATE) != birthDay && birthMonth == 1 & birthDay == 29){
                            // 閏年以外
                            birthBeforeYear.set(salesYear-1, birthMonth, 28, 0, 0, 0);
                        }
                        birth.set(salesYear, birthMonth, birthDay, 0, 0, 0);
                        if( birth.get(Calendar.DATE) != birthDay && birthMonth == 1 & birthDay == 29){
                            // 閏年以外
                            birth.set(salesYear-1, birthMonth, 28, 0, 0, 0);
                        }
                        birthAfterYear.set(salesYear+1, birthMonth, birthDay, 0, 0, 0);
                        if( birthAfterYear.get(Calendar.DATE) != birthDay && birthMonth == 1 & birthDay == 29){
                            // 閏年以外
                            birthAfterYear.set(salesYear-1, birthMonth, 28, 0, 0, 0);
                        }

                        if( (birthBeforeYear.after(from) && birthBeforeYear.before(to)) ||
                                (birth.after(from) && birth.before(to)) ||
                                (birthAfterYear.after(from) && birthAfterYear.before(to)) ) {
                            fBirth = true;
                        }
                    }else if( bean.getBirthdayCond() == PointCalculateBean.BIRTHDAY_MONTH ){
                        Calendar salesDate  = Calendar.getInstance();
                        salesDate.setTime(sales.getSalesDate());

                        if(salesDate.get(Calendar.MONTH) == birthMonth){
                            fBirth = true;
                        }
                    }else{
                        Calendar salesDate  = Calendar.getInstance();
                        salesDate.setTime(sales.getSalesDate());
                        if(salesDate.get(Calendar.MONTH) == birthMonth &&
                                salesDate.get(Calendar.DATE) == birthDay){
                            fBirth = true;
                        }
                    }

                    if( fBirth ){
                        if( bean.getBirthdayPoint() != 0 ){
                            newPoint += bean.getBirthdayPoint();
                        }else if( bean.getBirthdayRate() != 0.0 ){
                            newPoint += basePoint * (bean.getBirthdayRate() - 1 );
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return newPoint;
    }

    /**
     *
     */
    private static int getVisitNum(DataSales sales){
        int ret = -1;   // エラー

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     count(a.slip_no) + coalesce(max(b.before_visit_num),0) as get_visit_count");
        sql.append(" from");
        sql.append("     data_sales a");
        sql.append("         join mst_customer b");
        sql.append("         using(customer_id)");
        sql.append(" where");
        sql.append("         customer_id = " + SQLUtil.convertForSQL(sales.getCustomer().getCustomerID()));
        sql.append("     and a.shop_id = " + SQLUtil.convertForSQL(sales.getShop().getShopID()));
        sql.append("     and a.sales_date <= " + SQLUtil.convertForSQL(sales.getSalesDate()));
        sql.append("     and a.slip_no <> " + SQLUtil.convertForSQL(sales.getSlipNo()));
        sql.append("     and a.delete_date is null");

        try {
            ResultSetWrapper result = SystemInfo.getConnection().executeQuery(sql.toString());
            if( result.next() ) {
                ret = result.getInt("get_visit_count");
            }
        } catch( Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            System.out.println( e.getLocalizedMessage() );
        }
        return ret;
    }


}
