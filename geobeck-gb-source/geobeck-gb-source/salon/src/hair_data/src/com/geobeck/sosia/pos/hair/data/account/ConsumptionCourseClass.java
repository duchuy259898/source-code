/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.data.account;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author s_furukawa
 */
public class ConsumptionCourseClass extends ArrayList<ConsumptionCourse> {

    protected Integer shopId;

    protected Integer slipNo;

    protected Integer courseClassId;
    
    protected String courseClassName;

    protected Integer displaySeq;
    
    // Thanh start add 2014/07/11 Mashu_お会計表示
    private Integer shopCategoryID = null;

    public Integer getShopCategoryID() {
        return shopCategoryID;
    }

    public void setShopCategoryID(Integer shopCategoryID) {
        this.shopCategoryID = shopCategoryID;
    }
    // Thanh end add 2014/07/11 Mashu_お会計表示

    /**
     * コンストラクタ
     */
    public ConsumptionCourseClass(){

    }

    /**
     * 購入データを設定する。
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException
    {
//            this.setShopId(rs.getInt("shop_id"));
            this.setSlipNo(rs.getInt("slip_no"));//コース販売を特定するため伝票番号をセットする
            this.setCourseClassId(rs.getInt("course_class_id"));
            this.setCourseClassName(rs.getString("course_class_name"));
            this.setDisplaySeq(rs.getInt("display_seq"));
            // IVS_Thanh start add 2014/07/14 Mashu_お会計表示
            this.setShopCategoryID(rs.getInt("shop_category_id"));
            // IVS_Thanh end add 2014/07/14 Mashu_お会計表示
    }

    public String getCourseClassName() {
        return courseClassName;
    }

    public void setCourseClassName(String courseClassName) {
        this.courseClassName = courseClassName;
    }

    public Integer getCourseClassId() {
        return courseClassId;
    }

    public void setCourseClassId(Integer courseClassId) {
        this.courseClassId = courseClassId;
    }

    public Integer getDisplaySeq() {
        return displaySeq;
    }

    public void setDisplaySeq(Integer displaySeq) {
        this.displaySeq = displaySeq;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getSlipNo() {
        return slipNo;
    }

    public void setSlipNo(Integer slipNo) {
        this.slipNo = slipNo;
    }

    /**
     * 商品を読み込む。
     * @param con ConnectionWrapper
     * @param productDivision 商品分類
     * @return true - 成功、false - 失敗
     * @throws java.sql.SQLException SQLException
     */
    public boolean loadConsumptionCourse(ConnectionWrapper con, Integer shopID, Integer customerId, Integer courseClassId) throws SQLException
    {
            this.clear();
            ResultSetWrapper rs = con.executeQuery(this.getLoadConsumptionCourseSQL(shopID, customerId, courseClassId));

            while(rs.next())
            {
                    ConsumptionCourse cc = new ConsumptionCourse();
                    cc.setData(rs);
                    cc.setConsumptionRestNum(cc.getNum() - cc.getConsumptionNum());
                    if(!cc.hasConsumptionRest()){
                        continue;
                    }
                    cc.setConsumptionCourseClass((ConsumptionCourseClass)this.clone());
                    this.add(cc);
            }

            rs.close();

            return	true;
    }

    /**
     * 商品を読み込むＳＱＬ文を取得する。
     * @param productDivision 商品分類
     * @return 商品を読み込むＳＱＬ文
     */
    public String getLoadConsumptionCourseSQL(Integer shopID, Integer customerID, Integer courseClassId)
    {
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      dc.shop_id");
            sql.append("     ,dc.slip_no");
            sql.append("     ,dc.contract_no");
            sql.append("     ,dc.contract_detail_no");
            sql.append("     ,mcc.course_class_id as product_class_id");
            sql.append("     ,mcc.course_class_name as product_class_name");
            sql.append("     ,mc.course_id");
            sql.append("     ,mc.course_name");
            sql.append("     ,dc.product_num as num");
            // IVS_LVTU edit 2019/08/28 #97064 [gb]SPOS増税対応
            sql.append("     ,get_tax_rate(dc.tax_rate, ds.sales_date) as tax_rate");
            sql.append("     ,mc.price");
            sql.append("     ,dc.product_value");
            sql.append("     ,mc.operation_time");
            sql.append("     ,mc.praise_time_limit");
            sql.append("     ,mc.is_praise_time");
            sql.append("     ,mcc.display_seq");
            sql.append("     ,coalesce(sum(dcd.product_num), 0) as consumption_num");
            sql.append(" from");
            sql.append("     data_sales ds");
            sql.append("         inner join data_contract dc");
            sql.append("                 on ds.slip_no = dc.slip_no");
            sql.append("                and ds.shop_id = dc.shop_id");
            sql.append("         inner join mst_course mc");
            sql.append("                 on dc.product_id = mc.course_id");
            sql.append("         inner join mst_course_class mcc");
            sql.append("                 on mc.course_class_id = mcc.course_class_id");
            // start delete 20130627 IVS
            // sql.append("                and mcc.course_class_id = " + SQLUtil.convertForSQL(courseClassId));
            // end delete 20130627 IVS
            sql.append("         left join data_contract_digestion dcd");
            sql.append("                on dc.shop_id = dcd.contract_shop_id");
            sql.append("               and dc.contract_no = dcd.contract_no");
            sql.append("               and dc.contract_detail_no = dcd.contract_detail_no");
            sql.append(" where");
            sql.append("         ds.customer_id = " + SQLUtil.convertForSQL(customerID));
            sql.append("     and");
            sql.append("     (");
            sql.append("         (mc.is_praise_time = false)");
            sql.append("      or (mc.is_praise_time = true and current_date < date_trunc('day', ds.sales_date + cast(mc.praise_time_limit || ' months' as interval)))");
            sql.append("     )");
            sql.append(" group by");
            sql.append("      dc.shop_id");
            sql.append("     ,dc.slip_no");
            sql.append("     ,dc.contract_no");
            sql.append("     ,dc.contract_detail_no");
            sql.append("     ,mcc.course_class_id");
            sql.append("     ,mcc.course_class_name");
            sql.append("     ,mc.course_id");
            sql.append("     ,mc.course_name");
            sql.append("     ,dc.product_num");
            // IVS_LVTU edit 2019/08/28 #97064 [gb]SPOS増税対応
            sql.append("     ,dc.tax_rate");
            sql.append("     ,mc.price");
            sql.append("     ,dc.product_value");
            sql.append("     ,mc.operation_time");
            sql.append("     ,mc.praise_time_limit");
            sql.append("     ,mc.is_praise_time");
            sql.append("     ,mcc.display_seq");

            return sql.toString();
    }

    /**
     * 商品を読み込む。
     * @param con ConnectionWrapper
     * @param productDivision 商品分類
     * @return true - 成功、false - 失敗
     * @throws java.sql.SQLException SQLException
     */
    public boolean loadConsumptionCourseWithSalesDate(ConnectionWrapper con, Integer shopID, Integer customerId, Integer courseClassId) throws SQLException
    {
        this.clear();
        ResultSetWrapper rs = con.executeQuery(this.getLoadConsumptionCourseWithSalesDateSQL(shopID, customerId, courseClassId));

        while(rs.next())
        {
            ConsumptionCourse cc = new ConsumptionCourse();
            cc.setDataWithSalesDate(rs);
            //Start add 20131202 lvut 
            cc.setValidDate(rs.getDate("valid_date"));
            //End add 20131202 lvut 
            cc.setConsumptionRestNum(cc.getNum() - cc.getConsumptionNum());
            if(!cc.hasConsumptionRest()){
                continue;
            }
            cc.setConsumptionCourseClass((ConsumptionCourseClass)this.clone());
            this.add(cc);
        }

        rs.close();

        return true;
    }

        /**
     * 商品を読み込むＳＱＬ文を取得する。
     * @param productDivision 商品分類
     * @return 商品を読み込むＳＱＬ文
     */
    public String getLoadConsumptionCourseWithSalesDateSQL(Integer shopID, Integer customerID, Integer courseClassId)
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      dc.shop_id");
        sql.append("     ,dc.slip_no");
        sql.append("     ,dc.contract_no");
        sql.append("     ,dc.contract_detail_no");
        sql.append("     ,ds.sales_date");
        //Start add 20131202 lvut 
        sql.append("     ,dc.valid_date");
        //End add 20131202 lvut 
        sql.append("     ,mcc.course_class_id as product_class_id");
        sql.append("     ,mcc.course_class_name as product_class_name");
        sql.append("     ,mc.course_id");
        sql.append("     ,mc.course_name");
        sql.append("     ,dc.product_num as num");
        // IVS_LVTU edit 2019/08/28 #97064 [gb]SPOS増税対応
        sql.append("     ,get_tax_rate(dc.tax_rate, ds.sales_date) as tax_rate");
        sql.append("     ,mc.price");
        sql.append("     ,dc.product_value");
        sql.append("     ,mc.operation_time");
        sql.append("     ,mc.praise_time_limit");
        sql.append("     ,mc.is_praise_time");
        sql.append("     ,mcc.display_seq");
        sql.append("     ,coalesce(sum(dcd.product_num), 0) as consumption_num");
        sql.append(" from");
        sql.append("     data_sales ds");
        sql.append("         inner join data_contract dc");
        sql.append("                 on ds.slip_no = dc.slip_no");
         // Add start 2013-11-04 Hoa
        sql.append("                and ds.shop_id = dc.shop_id and dc.delete_date is null and (dc.contract_status is null or dc.contract_status = 2 ) ");
         // Add end 2013-11-04 Hoa
        sql.append("         inner join mst_course mc");
        sql.append("                 on dc.product_id = mc.course_id");
        sql.append("         inner join mst_course_class mcc");
        sql.append("                 on mc.course_class_id = mcc.course_class_id");
        sql.append("                and mcc.course_class_id = " + SQLUtil.convertForSQL(courseClassId));
        sql.append("         left join data_contract_digestion dcd");
        sql.append("                on dc.shop_id = dcd.contract_shop_id");
        sql.append("               and dc.contract_no = dcd.contract_no");
        sql.append("               and dc.contract_detail_no = dcd.contract_detail_no");
        //Add start 2014-9-24 ennyu
        sql.append("    left join data_contract_share dcs");
        sql.append("        on dc.shop_id = dcs.shop_id");
        sql.append("        and dc.contract_no = dcs.contract_no");
        sql.append("        and dc.contract_detail_no = dcs.contract_detail_no");
        sql.append("        and dcs.delete_date is null");
        //Add end 2014-9-24 ennyu
        sql.append(" where");
        // IVS SANG START INSERT 20131118 [gb]予約登録画面、お会計画面の制御調整
        sql.append("         ds.sales_date is not null");
        // IVS SANG END INSERT 20131118 [gb]予約登録画面、お会計画面の制御調整
        //sql.append("         ds.customer_id = " + SQLUtil.convertForSQL(customerID));
        
        //共同利用者対応
        sql.append("         and (ds.customer_id = " + SQLUtil.convertForSQL(customerID) + " or dcs.customer_id = "+SQLUtil.convertForSQL(customerID) +")");
         //IVS NNTUAN START ADD 20131104
        sql.append("     and ( dc.valid_date is null or dc.valid_date >= current_date)");
         //IVS NNTUAN END ADD 20131104
        //IVS NNTUAN START DELETE 20131105
        /*sql.append("     and");
        sql.append("     (");
        sql.append("         (mc.is_praise_time = false)");
        sql.append("      or (mc.is_praise_time = true and current_date < date_trunc('day', ds.sales_date + cast(mc.praise_time_limit || ' months' as interval)))");
        sql.append("     )");*/
        //IVS NNTUAN END DELETE 20131105
        sql.append(" group by");
        sql.append("      dc.shop_id");
        sql.append("     ,dc.slip_no");
        sql.append("     ,dc.contract_no");
        sql.append("     ,dc.contract_detail_no");
        sql.append("     ,ds.sales_date");
        //Start add 20131202 lvut 
        sql.append("     ,dc.valid_date");
        //End add 20131202 lvut 
        sql.append("     ,mcc.course_class_id");
        sql.append("     ,mcc.course_class_name");
        sql.append("     ,mc.course_id");
        sql.append("     ,mc.course_name");
        sql.append("     ,dc.product_num");
        // IVS_LVTU edit 2019/08/28 #97064 [gb]SPOS増税対応
        sql.append("     ,dc.tax_rate");
        sql.append("     ,mc.price");
        sql.append("     ,dc.product_value");
        sql.append("     ,mc.operation_time");
        sql.append("     ,mc.praise_time_limit");
        sql.append("     ,mc.is_praise_time");
        sql.append("     ,mcc.display_seq");

        return sql.toString();
    }
    
    
     public boolean loadConsumptionCourseWithSalesDate(ConnectionWrapper con, Integer shopID, Integer customerId, Integer courseClassId, java.util.Date reservationDate ) throws SQLException
    {
        this.clear();
        ResultSetWrapper rs = con.executeQuery(this.getLoadConsumptionCourseWithSalesDateSQL(shopID, customerId, courseClassId, reservationDate ));

        while(rs.next())
        {
            ConsumptionCourse cc = new ConsumptionCourse();
            cc.setDataWithSalesDate(rs);
            //Start add 20131202 lvut 
            cc.setValidDate(rs.getDate("valid_date"));
            //End add 20131202 lvut 
            cc.setConsumptionRestNum(cc.getNum() - cc.getConsumptionNum());
            if(!cc.hasConsumptionRest()){
                continue;
            }
            cc.setConsumptionCourseClass((ConsumptionCourseClass)this.clone());
            this.add(cc);
        }

        rs.close();

        return true;
    }
    public String getLoadConsumptionCourseWithSalesDateSQL(Integer shopID, Integer customerID, Integer courseClassId, java.util.Date reservationDate)
    {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      dc.shop_id");
        sql.append("     ,dc.slip_no");
        sql.append("     ,dc.contract_no");
        sql.append("     ,dc.contract_detail_no");
        sql.append("     ,ds.sales_date");
        sql.append("     ,dc.valid_date");
        sql.append("     ,mcc.course_class_id as product_class_id");
        sql.append("     ,mcc.course_class_name as product_class_name");
        sql.append("     ,mc.course_id");
        sql.append("     ,mc.course_name");
        sql.append("     ,dc.product_num as num");
        // IVS_LVTU edit 2019/08/28 #97064 [gb]SPOS増税対応
        sql.append("     ,get_tax_rate(dc.tax_rate, ds.sales_date) as tax_rate");
        sql.append("     ,mc.price");
        sql.append("     ,dc.product_value");
        sql.append("     ,mc.operation_time");
        sql.append("     ,mc.praise_time_limit");
        sql.append("     ,mc.is_praise_time");
        sql.append("     ,mcc.display_seq");
        //IVS_LVTu start edit 2015/11/06 Bug #44177
        //sql.append("     ,coalesce(sum(dcd.product_num), 0) as consumption_num");
        sql.append("     ,( select sum(product_num) from data_contract_digestion where contract_no = dc.contract_no ");
        sql.append("     and contract_detail_no = dc.contract_detail_no ");
        sql.append("     and contract_shop_id = dc.shop_id ");
        sql.append("     ) as consumption_num ");
        //IVS_LVTu end edit 2015/11/06 Bug #44177
        sql.append(" from");
        sql.append("     data_sales ds");
        sql.append("         inner join data_contract dc");
        sql.append("                 on ds.slip_no = dc.slip_no"); 
        sql.append("                and ds.shop_id = dc.shop_id and dc.delete_date is null and (dc.contract_status is null or dc.contract_status = 2 ) ");
        sql.append("         inner join mst_course mc");
        sql.append("                 on dc.product_id = mc.course_id");
        sql.append("         inner join mst_course_class mcc");
        sql.append("                 on mc.course_class_id = mcc.course_class_id");
        sql.append("                and mcc.course_class_id = " + SQLUtil.convertForSQL(courseClassId));
        sql.append("         left join data_contract_digestion dcd");
        sql.append("                on dc.shop_id = dcd.contract_shop_id");
        sql.append("               and dc.contract_no = dcd.contract_no");
        sql.append("               and dc.contract_detail_no = dcd.contract_detail_no");
        //Add start 2014-9-24 ennyu
        sql.append("    left join data_contract_share dcs");
        sql.append("        on dc.shop_id = dcs.shop_id");
        sql.append("        and dc.contract_no = dcs.contract_no");
        sql.append("        and dc.contract_detail_no = dcs.contract_detail_no");
        sql.append("        and dcs.delete_date is null");
        //Add end 2014-9-24 ennyu
        sql.append(" where");
        sql.append("         ds.sales_date is not null ");
        sql.append("         and to_char(ds.sales_date,'YYYYMMDD') <=  to_char (").append(SQLUtil.convertForSQL(reservationDate)).append("::date,'YYYYMMDD')");
        //sql.append("         and ds.customer_id = " + SQLUtil.convertForSQL(customerID));
        
        //共同利用者対応
        sql.append("         and (ds.customer_id = " + SQLUtil.convertForSQL(customerID) + " or dcs.customer_id = "+SQLUtil.convertForSQL(customerID) +")");
        sql.append("     and ( dc.valid_date is null or dc.valid_date >= current_date)");
        sql.append(" group by");
        sql.append("      dc.shop_id");
        sql.append("     ,dc.slip_no");
        sql.append("     ,dc.contract_no");
        sql.append("     ,dc.contract_detail_no");
        sql.append("     ,ds.sales_date");
        sql.append("     ,dc.valid_date");
        sql.append("     ,mcc.course_class_id");
        sql.append("     ,mcc.course_class_name");
        sql.append("     ,mc.course_id");
        sql.append("     ,mc.course_name");
        sql.append("     ,dc.product_num");
        // IVS_LVTU edit 2019/08/28 #97064 [gb]SPOS増税対応
        sql.append("     ,dc.tax_rate");
        sql.append("     ,mc.price");
        sql.append("     ,dc.product_value");
        sql.append("     ,mc.operation_time");
        sql.append("     ,mc.praise_time_limit");
        sql.append("     ,mc.is_praise_time");
        sql.append("     ,mcc.display_seq");
        return sql.toString();
    }

    @Override
    public String toString(){
        return getCourseClassName();
    }
}
