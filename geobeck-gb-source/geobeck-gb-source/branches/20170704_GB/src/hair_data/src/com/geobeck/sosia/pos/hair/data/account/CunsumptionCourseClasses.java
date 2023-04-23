/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.data.account;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author s_furukawa
 */
public class CunsumptionCourseClasses extends Vector<ConsumptionCourseClass> {
    	/**
	 * データを読み込む。
	 * @param con ConnectionWrapper
	 * @return true - 成功、false - 失敗
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean loadConsumptionCourseClass(ConnectionWrapper con, Integer shopID, Integer customerID) throws SQLException
	{
            this.clear();

            ResultSetWrapper rs = con.executeQuery(this.getLoadConsumptionCourseClassSQL(shopID, customerID));

            Map consumptionCourseClassMap = new LinkedHashMap();
            while (rs.next()) {
                //コースの消化回数（受講回数）
                int consumptionNum = rs.getInt("num");
                //消化回数（実際にコースを消化した回数）
                float consumptionCount = rs.getFloat("count");
                if (consumptionNum == consumptionCount) {
                    //すべて消化しているコースは除外する
                    continue;
                }

                ConsumptionCourseClass ccc = new ConsumptionCourseClass();
                ccc.setData(rs);

                if(!consumptionCourseClassMap.containsKey(ccc.getCourseClassId())){
                    consumptionCourseClassMap.put(ccc.getCourseClassId(), ccc);
                }
            }
            this.addAll(consumptionCourseClassMap.values());

            rs.close();

            return true;
	}
        
        
        public boolean loadConsumptionCourseClass(ConnectionWrapper con, Integer shopID, Integer customerID, java.util.Date reservationDate ) throws SQLException
	{
            this.clear();

            ResultSetWrapper rs = con.executeQuery(this.getLoadConsumptionCourseClassSQL(shopID, customerID,reservationDate));
            Map consumptionCourseClassMap = new LinkedHashMap();
            
            while (rs.next()) {
                //コースの消化回数（受講回数）
                int consumptionNum = rs.getInt("num");
                //消化回数（実際にコースを消化した回数）
                float consumptionCount = rs.getFloat("count");
                if (consumptionNum == consumptionCount) {
                    //すべて消化しているコースは除外する
                    continue;
                }

                ConsumptionCourseClass ccc = new ConsumptionCourseClass();
                ccc.setData(rs);
                
                if(!consumptionCourseClassMap.containsKey(ccc.getCourseClassId())){
                    consumptionCourseClassMap.put(ccc.getCourseClassId(), ccc);
                }
            }
            
            this.addAll(consumptionCourseClassMap.values());

            rs.close();

            return true;
	}

      	/**
	 * データを読み込むＳＱＬ文を取得する。
	 * @return データを読み込むＳＱＬ文
	 */
	public String getLoadConsumptionCourseClassSQL(Integer shopID, Integer customerID)
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("    dc.slip_no,");
            sql.append("    dc.contract_no,");
            sql.append("    dc.contract_detail_no,");
            sql.append("    mcc.course_class_id,");
            sql.append("    mcc.course_class_name,");
            sql.append("    mc.course_id,");
            sql.append("    mc.course_name,");
            sql.append("    dc.product_num as num,");
            sql.append("    mcc.display_seq,");
            // IVS_Thanh start add 2014/07/14 Mashu_お会計表示
            sql.append("    mcc.shop_category_id,");            
            // IVS_Thanh end add 2014/07/14 Mashu_お会計表示
            sql.append("    coalesce(sum(dcd.product_num), 0) as count,");
            // 共同利用者ID
            sql.append("    dcs.customer_id as customer_id");
            sql.append(" from");
            sql.append("    data_sales ds");
            sql.append("    inner join data_contract dc");
            sql.append("        on ds.slip_no = dc.slip_no");
            // Add start 2013-11-04 Hoa
            sql.append("        and ds.shop_id = dc.shop_id and dc.delete_date is null and (dc.contract_status is null or dc.contract_status = 2 ) ");
            // Add end  2013-11-04 Hoa
            sql.append("    inner join mst_course mc");
            sql.append("        on dc.product_id = mc.course_id");
            sql.append("    inner join mst_course_class mcc");
            sql.append("        on mc.course_class_id = mcc.course_class_id");
            //Add start 2014-9-24 ennyu
            sql.append("    left join data_contract_share dcs");
            sql.append("        on dc.shop_id = dcs.shop_id");
            sql.append("        and dc.contract_no = dcs.contract_no");
            sql.append("        and dc.contract_detail_no = dcs.contract_detail_no");
            sql.append("        and dcs.delete_date is null");
            //Add end 2014-9-24 ennyu
            sql.append("    left outer join data_contract_digestion dcd");
            sql.append("        on dc.shop_id = dcd.contract_shop_id");
            sql.append("        and dc.contract_no = dcd.contract_no");
            sql.append("        and dc.contract_detail_no = dcd.contract_detail_no");
            sql.append(" where");
            // IVS SANG START INSERT 20131118 [gb]予約登録画面、お会計画面の制御調整
            sql.append("         ds.sales_date is not null and");
            // IVS SANG END INSERT 20131118 [gb]予約登録画面、お会計画面の制御調整
            // Updated 2013-01-26 契約店舗以外でも消化できるように対応
            // sql.append("        dc.shop_id = ").append(SQLUtil.convertForSQL(shopID));
            // sql.append("    and ds.customer_id = ").append(SQLUtil.convertForSQL(customerID));
            // sql.append("        ds.customer_id = ").append(SQLUtil.convertForSQL(customerID));
            
            //共同利用者対応
            sql.append("        (ds.customer_id = ").append(SQLUtil.convertForSQL(customerID)).append(" or dcs.customer_id = ").append(SQLUtil.convertForSQL(customerID)).append(")");
            // Updated 2013-01-26
             //IVS NNTUAN START EDIT 20131104
            sql.append("     and ( dc.valid_date is null or dc.valid_date >= current_date)");
             //IVS NNTUAN END EDIT 20131104
            //IVS NNTUAN START DELETE 20131105
            /*
            sql.append("    and (");
            sql.append("          (mc.is_praise_time = FALSE)");
            sql.append("          or");
            sql.append("          (");
            sql.append("           mc.is_praise_time = TRUE");
            sql.append("           and");
            sql.append("           current_date < date_trunc('day', ds.sales_date + cast(mc.praise_time_limit || ' months' as interval))");
            sql.append("          )");
            sql.append("        )");
            */
            //IVS NNTUAN END DELETE 20131105 
            sql.append(" group by dc.slip_no, dc.contract_no, dc.contract_detail_no,mcc.course_class_id, mcc.course_class_name, mc.course_id, mc.course_name, dc.product_num, mcc.display_seq,mcc.shop_category_id,dcs.customer_id");
            sql.append(" order by mcc.display_seq");

            return sql.toString();
	}
        
        
        public String getLoadConsumptionCourseClassSQL(Integer shopID, Integer customerID, java.util.Date reservationDate )
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("    dc.slip_no,");
            sql.append("    dc.contract_no,");
            sql.append("    dc.contract_detail_no,");
            sql.append("    mcc.course_class_id,");
            sql.append("    mcc.course_class_name,");
            sql.append("    mc.course_id,");
            sql.append("    mc.course_name,");
            sql.append("    dc.product_num as num,");
            sql.append("    mcc.display_seq,");
            // IVS_Thanh start add 2014/07/14 Mashu_お会計表示
            sql.append("    mcc.shop_category_id,");            
            // IVS_Thanh end add 2014/07/14 Mashu_お会計表示
            sql.append("    coalesce(sum(dcd.product_num), 0) as count");
            sql.append(" from");
            sql.append("    data_sales ds");
            sql.append("    inner join data_contract dc");
            sql.append("        on ds.slip_no = dc.slip_no");
            sql.append("        and ds.shop_id = dc.shop_id and dc.delete_date is null and (dc.contract_status is null or dc.contract_status = 2 ) ");
            sql.append("    inner join mst_course mc");
            sql.append("        on dc.product_id = mc.course_id");
            sql.append("    inner join mst_course_class mcc");
            sql.append("        on mc.course_class_id = mcc.course_class_id");
            //Add start 2014-9-24 ennyu
            sql.append("    left join data_contract_share dcs");
            sql.append("        on dc.shop_id = dcs.shop_id");
            sql.append("        and dc.contract_no = dcs.contract_no");
            sql.append("        and dc.contract_detail_no = dcs.contract_detail_no");
            sql.append("        and dcs.delete_date is null");
            //Add end 2014-9-24 ennyu
            sql.append("    left outer join data_contract_digestion dcd");
            sql.append("        on dc.shop_id = dcd.contract_shop_id");
            sql.append("        and dc.contract_no = dcd.contract_no");
            sql.append("        and dc.contract_detail_no = dcd.contract_detail_no");
            sql.append(" where");
            sql.append("         ds.sales_date is not null and");
            // sql.append("        ds.customer_id = ").append(SQLUtil.convertForSQL(customerID));
            
            //共同利用者対応
            sql.append("        (ds.customer_id = ").append(SQLUtil.convertForSQL(customerID)).append(" or dcs.customer_id = ").append(SQLUtil.convertForSQL(customerID)).append(")");
            sql.append("     and ( dc.valid_date is null or dc.valid_date >= current_date)");
            sql.append("    and to_char(ds.sales_date,'YYYYMMDD') <=  to_char (").append(SQLUtil.convertForSQL(reservationDate)).append("::date,'YYYYMMDD')");
            sql.append(" group by dc.slip_no, dc.contract_no, dc.contract_detail_no,mcc.course_class_id, mcc.course_class_name, mc.course_id, mc.course_name, dc.product_num, mcc.display_seq,mcc.shop_category_id");
            sql.append(" order by mcc.display_seq");
            return sql.toString();
	}

}
