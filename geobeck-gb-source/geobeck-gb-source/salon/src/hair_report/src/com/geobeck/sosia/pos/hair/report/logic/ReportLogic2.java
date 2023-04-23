/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report.logic;

import com.geobeck.sosia.pos.hair.report.util.JExcelApi;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.report.bean.ReportParameterBean;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lvtu
 */
public class ReportLogic2 {
    /**
     * 千円単位・・・百円単位以下、切り捨て
     */
    private static final int ROUND_DOWN = 1000 ;
    
    public enum OrderType {
        DIGESTION
        , PRODUCTSALES
        , SERVICES
        , PRODUCTSALES_DIGESTION
        , PRODUCTSALES_SERVICES; 
    };
    
    /**
     * 担当別技術成績＜コース契約成績＞出力ロジック
     *
     * @param paramBean レポートパラメータBean
     * @return true:成功 false:出力対象なし
     * @exception Exception
     */
    public boolean outStaffReportCourseContract(ReportParameterBean paramBean) throws Exception {
        ConnectionWrapper cw = SystemInfo.getConnection();

        //スタッフリストを取得する
        List<OriginalDetailBean> staffRankBeanList = getStaffRankList(cw, paramBean);
        
        //消化客数, 消化金額
        Map<Integer, Map<String, Integer>> staffCourseConsumptionMap = getStaffCourseConsumption(cw, paramBean);
        
        for (OriginalDetailBean detailBean : staffRankBeanList) {
            MstStaff staff  = detailBean.getStaff() ;
            if (staffCourseConsumptionMap.containsKey(staff.getStaffID())) {
                Map<String, Integer> staffResultMap = staffCourseConsumptionMap.get(staff.getStaffID()) ;
                int digestionAmount = staffResultMap.get("ConsumptionTotal") ;
                detailBean.setDigestionAmount( Long.valueOf(digestionAmount));
            }
        }

        JExcelApi jx = new JExcelApi("スタッフ成績_担当別技術成績_コース契約成績");
        //テンプレートとなるファイルパスをセット
        jx.setTemplateFile("/reports/スタッフ成績_担当別技術成績_コース契約成績_original.xls");

        // ヘッダ
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        jx.setValue(4, 3, format.format(paramBean.getTargetStartDateObj()) + " ～ " + format.format(paramBean.getTargetEndDateObj()));

        if (paramBean.getTaxType() == ReportParameterBean.TAX_TYPE_BLANK) {
            jx.setValue(4, 4, "税抜");
        } else if (paramBean.getTaxType() == ReportParameterBean.TAX_TYPE_UNIT) {
            jx.setValue(4, 4, "税込");
        }

        int baseRow = 8;
        //スタッフ数分行追加
        jx.insertRow(baseRow, (staffRankBeanList.size() - 2));
        
        //スタッフ情報出力
        int row = baseRow;
        int col = 2;
        
        //集計結果リストのソート処理
        sortByOrder(staffRankBeanList, OrderType.DIGESTION);
        fillData(jx, staffRankBeanList, row, col, OrderType.DIGESTION); // 消化欄・金額
        
        col = 7;
        //集計結果リストのソート処理
        sortByOrder(staffRankBeanList, OrderType.PRODUCTSALES);
        fillData(jx, staffRankBeanList, row, col, OrderType.PRODUCTSALES); // 物販欄・金額
        
        col = 12;
        //集計結果リストのソート処理
        sortByOrder(staffRankBeanList, OrderType.SERVICES);
        fillData(jx, staffRankBeanList, row, col, OrderType.SERVICES); // 役務欄・金額
        
        col = 17;
        //集計結果リストのソート処理
        sortByOrder(staffRankBeanList, OrderType.PRODUCTSALES_DIGESTION);
        fillData(jx, staffRankBeanList, row, col, OrderType.PRODUCTSALES_DIGESTION); // 物販＋消化欄・金額
        
        col = 22;
        //集計結果リストのソート処理
        sortByOrder(staffRankBeanList, OrderType.PRODUCTSALES_SERVICES);
        fillData(jx, staffRankBeanList, row, col, OrderType.PRODUCTSALES_SERVICES); // 物販＋契約欄・金額

        //ファイル出力
        jx.openWorkbook();


        return true;
    }
    
    /**
     * データを入力
     * @param jx
     * @param staffRankBeanList
     * @param row
     * @param col
     * @param selected 
     */
    private void fillData (JExcelApi jx, List<OriginalDetailBean> staffRankBeanList, int row, int col, OrderType selected ) {
        
        int rank                    = 1 ;
        Long amountTotalPrevious    = 0l ;
        for (OriginalDetailBean detailBean : staffRankBeanList) {
            MstStaff staff  = detailBean.getStaff() ;
            MstShop shop    = detailBean.getShop() ;
            
            Long amountTotal = 0l;
            switch(selected) {
            case DIGESTION :
                amountTotal = detailBean.getDigestionTotal() ;
                break;
            case PRODUCTSALES :
                amountTotal = detailBean.getProductSalesTotal() ;
                break;
            case SERVICES :
                amountTotal = detailBean.getServicesTotal() ;
                break;
            case PRODUCTSALES_DIGESTION :
                amountTotal = detailBean.getProductSalesDigestionTotal() ;
                break;
            case PRODUCTSALES_SERVICES :
                amountTotal = detailBean.getProductSalesServicesTotal();
                break;
            }

            if (amountTotal > 0) {
                // 同じ数値の場合、同じ順位で表示
                if ( amountTotalPrevious > amountTotal) {
                    rank    ++ ;
                }
                jx.setValue( col, row, rank ) ;
            }
            jx.setValue( col+1, row, staff.getStaffName(0) + " " + staff.getStaffName(1) ) ;
            jx.setValue( col+2, row, shop.getShopName() ) ;
            jx.setValue( col+3, row, amountTotal ) ;
            
            row     ++ ;
            amountTotalPrevious = amountTotal ;
        }
    }
    
    /**
     * 集計結果リストのソート処理
     * @param staffRankBeanList
     * @param selected 
     */
    private void sortByOrder(List<OriginalDetailBean> staffRankBeanList, OrderType selected) {

        switch(selected) {
        case DIGESTION :
            Collections.sort(staffRankBeanList, new Comparator<OriginalDetailBean>() {
                @Override
                public int compare(OriginalDetailBean o1, OriginalDetailBean o2) {
                    if ( o2.getDigestionTotal().compareTo(o1.getDigestionTotal()) == 0 ) {
                        
                        if ( o1.getShop().getShopID().compareTo(o2.getShop().getShopID()) == 0 ) {
                            return o1.getStaff().getStaffID().compareTo(o2.getStaff().getStaffID()) ;
                        }
                        
                        if ( o1.getShop().getShopID() != 0 && o2.getShop().getShopID() != 0 ) {
                            return o1.getShop().getShopID().compareTo(o2.getShop().getShopID()) ;
                        }
                        if ( o1.getShop().getShopID() != 0 && o2.getShop().getShopID() == 0 ) {
                            return -1;
                        }
                        if ( o1.getShop().getShopID() == 0 && o2.getShop().getShopID() != 0 ) {
                            return 1;
                        }
                    }
                    return o2.getDigestionTotal().compareTo(o1.getDigestionTotal());
                }
            });
            break;
        case PRODUCTSALES :
            Collections.sort(staffRankBeanList, new Comparator<OriginalDetailBean>() {
                @Override
                public int compare(OriginalDetailBean o1, OriginalDetailBean o2) {
                    if ( o2.getProductSalesTotal().compareTo(o1.getProductSalesTotal()) == 0 ) {
                        
                        if ( o1.getShop().getShopID().compareTo(o2.getShop().getShopID()) == 0 ) {
                            return o1.getStaff().getStaffID().compareTo(o2.getStaff().getStaffID()) ;
                        }
                        
                        if ( o1.getShop().getShopID() != 0 && o2.getShop().getShopID() != 0 ) {
                            return o1.getShop().getShopID().compareTo(o2.getShop().getShopID()) ;
                        }
                        if ( o1.getShop().getShopID() != 0 && o2.getShop().getShopID() == 0 ) {
                            return -1;
                        }
                        if ( o1.getShop().getShopID() == 0 && o2.getShop().getShopID() != 0 ) {
                            return 1;
                        }
                    }
                    return o2.getProductSalesTotal().compareTo(o1.getProductSalesTotal());
                }
            });
            break;
        case SERVICES :
            Collections.sort(staffRankBeanList, new Comparator<OriginalDetailBean>() {
                @Override
                public int compare(OriginalDetailBean o1, OriginalDetailBean o2) {
                    if ( o2.getServicesTotal().compareTo(o1.getServicesTotal()) == 0 ) {
                        
                        if ( o1.getShop().getShopID().compareTo(o2.getShop().getShopID()) == 0 ) {
                            return o1.getStaff().getStaffID().compareTo(o2.getStaff().getStaffID()) ;
                        }
                        
                        if ( o1.getShop().getShopID() != 0 && o2.getShop().getShopID() != 0 ) {
                            return o1.getShop().getShopID().compareTo(o2.getShop().getShopID()) ;
                        }
                        if ( o1.getShop().getShopID() != 0 && o2.getShop().getShopID() == 0 ) {
                            return -1;
                        }
                        if ( o1.getShop().getShopID() == 0 && o2.getShop().getShopID() != 0 ) {
                            return 1;
                        }
                    }
                    return o2.getServicesTotal().compareTo(o1.getServicesTotal());
                }
            });
            break;
        case PRODUCTSALES_DIGESTION :
            Collections.sort(staffRankBeanList, new Comparator<OriginalDetailBean>() {
                @Override
                public int compare(OriginalDetailBean o1, OriginalDetailBean o2) {
                    if ( o2.getProductSalesDigestionTotal().compareTo(o1.getProductSalesDigestionTotal()) == 0 ) {
                        
                        if ( o1.getShop().getShopID().compareTo(o2.getShop().getShopID()) == 0 ) {
                            return o1.getStaff().getStaffID().compareTo(o2.getStaff().getStaffID()) ;
                        }
                        
                        if ( o1.getShop().getShopID() != 0 && o2.getShop().getShopID() != 0 ) {
                            return o1.getShop().getShopID().compareTo(o2.getShop().getShopID()) ;
                        }
                        if ( o1.getShop().getShopID() != 0 && o2.getShop().getShopID() == 0 ) {
                            return -1;
                        }
                        if ( o1.getShop().getShopID() == 0 && o2.getShop().getShopID() != 0 ) {
                            return 1;
                        }
                    }
                    return o2.getProductSalesDigestionTotal().compareTo(o1.getProductSalesDigestionTotal());
                }
            });
            break;
        case PRODUCTSALES_SERVICES :
            Collections.sort(staffRankBeanList, new Comparator<OriginalDetailBean>() {
                @Override
                public int compare(OriginalDetailBean o1, OriginalDetailBean o2) {
                    if ( o2.getProductSalesServicesTotal().compareTo(o1.getProductSalesServicesTotal()) == 0 ) {
                        
                        if ( o1.getShop().getShopID().compareTo(o2.getShop().getShopID()) == 0 ) {
                            return o1.getStaff().getStaffID().compareTo(o2.getStaff().getStaffID()) ;
                        }
                        
                        if ( o1.getShop().getShopID() != 0 && o2.getShop().getShopID() != 0 ) {
                            return o1.getShop().getShopID().compareTo(o2.getShop().getShopID()) ;
                        }
                        if ( o1.getShop().getShopID() != 0 && o2.getShop().getShopID() == 0 ) {
                            return -1;
                        }
                        if ( o1.getShop().getShopID() == 0 && o2.getShop().getShopID() != 0 ) {
                            return 1;
                        }
                    }
                    return o2.getProductSalesServicesTotal().compareTo(o1.getProductSalesServicesTotal());
                }
            });
            break;
        }
    }
    
    /**
     * 月度個人実績ランキング
     *
     * @param cw
     * @param paramBean
     * @return
     * @throws SQLException
     */
    private List<OriginalDetailBean> getStaffRankList(ConnectionWrapper cw, ReportParameterBean paramBean) throws SQLException {

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT T1.staff_id     , ");
        sql.append(" 	   T1.staff_no      , ");
        sql.append(" 	   T1.staff_name1   , ");
        sql.append(" 	   T1.staff_name2   , ");
        sql.append(" 	   T1.staff_kana1   , ");
        sql.append(" 	   T1.staff_kana2   , ");
        sql.append(" 	   COALESCE (T2.shop_id, 0) AS shop_id ,        ");
        sql.append(" 	   T2.shop_name     , ");
        if (paramBean.getTaxType() == ReportParameterBean.TAX_TYPE_BLANK) {
            sql.append(" 	   SUM(CASE           ");
            sql.append(" 			   WHEN T3.product_division in (1, 3) THEN T3.discount_detail_value_no_tax ");
            sql.append(" 			   ELSE 0               ");
            sql.append(" 		   END) AS technic_amount ,     ");
            sql.append(" 	   SUM(CASE                             ");
            sql.append(" 			   WHEN T3.product_division in (2, 4) THEN T3.discount_detail_value_no_tax ");
            sql.append(" 			   ELSE 0               ");
            sql.append(" 		   END) AS item_amount ,        ");
            sql.append(" 	   SUM(CASE                             ");
            sql.append(" 			   WHEN T3.product_division IN (5, 8) THEN T3.discount_detail_value_no_tax ");
            sql.append(" 			   ELSE 0               ");
            sql.append(" 		   END) AS course_amount ,      ");
            sql.append(" 	   SUM(CASE                             ");
            sql.append(" 			   WHEN T3.product_division IN (6) THEN T3.discount_detail_value_no_tax ");
            sql.append(" 			   ELSE 0               ");
            sql.append(" 		   END) AS digestion_amount     ");
        } else {
            sql.append(" 	   SUM(CASE           ");
            sql.append(" 			   WHEN T3.product_division in (1, 3) THEN T3.discount_detail_value_in_tax ");
            sql.append(" 			   ELSE 0               ");
            sql.append(" 		   END) AS technic_amount ,     ");
            sql.append(" 	   SUM(CASE                             ");
            sql.append(" 			   WHEN T3.product_division in (2, 4) THEN T3.discount_detail_value_in_tax ");
            sql.append(" 			   ELSE 0               ");
            sql.append(" 		   END) AS item_amount ,        ");
            sql.append(" 	   SUM(CASE                             ");
            sql.append(" 			   WHEN T3.product_division IN (5, 8) THEN T3.discount_detail_value_in_tax ");
            sql.append(" 			   ELSE 0               ");
            sql.append(" 		   END) AS course_amount ,      ");
            sql.append(" 	   SUM(CASE                             ");
            sql.append(" 			   WHEN T3.product_division IN (6) THEN T3.discount_detail_value_in_tax ");
            sql.append(" 			   ELSE 0               ");
            sql.append(" 		   END) AS digestion_amount     ");
        }
        sql.append(" FROM mst_staff T1                          ");
        sql.append(" LEFT JOIN mst_shop T2 ON T1.shop_id = T2.shop_id ");
        sql.append(" LEFT JOIN view_data_sales_detail_valid T3 ");
        sql.append(" ON T1.staff_id = T3.detail_staff_id       ");
        sql.append(" AND T3.sales_date BETWEEN '").append(paramBean.getTargetStartDate()).append("' AND '").append(paramBean.getTargetEndDate()).append("' ");
        if ((paramBean.getListCategoryId() != null && !paramBean.getListCategoryId().equals(""))) {
             sql.append( " AND      ");
            sql.append(" (          ");
            sql.append(" EXISTS (   ");
            sql.append("         SELECT 1 ");
            sql.append("         FROM view_data_sales_detail_valid dsd1 ");
            sql.append("         INNER JOIN mst_technic mt ON dsd1.product_id = mt.technic_id AND T3.product_division = dsd1.product_division AND dsd1.product_division in(1,3) ");
            sql.append("         INNER JOIN mst_technic_class mtc ON mtc.technic_class_id = mt.technic_class_id  ");
            sql.append("         WHERE dsd1.slip_no = T3.slip_no AND dsd1.shop_id = T3.shop_id AND dsd1.slip_detail_no = T3.slip_detail_no  ");
            sql.append("         AND mtc.shop_category_id in( ").append(paramBean.getListCategoryId()).append(" )");
            sql.append("         ) 	 ");
            sql.append(" OR ");
            sql.append(" EXISTS(");
            sql.append("         SELECT 1 ");
            sql.append("         FROM view_data_sales_detail_valid dsd1");
            sql.append("         INNER JOIN mst_item mi ON dsd1.product_id = mi.item_id AND T3.product_division = dsd1.product_division AND dsd1.product_division in (2,4) ");
            sql.append("         INNER JOIN mst_item_class mic ON mic.item_class_id = mi.item_class_id");
            sql.append("         WHERE dsd1.slip_no = T3.slip_no AND dsd1.shop_id = T3.shop_id AND dsd1.slip_detail_no = T3.slip_detail_no");
            sql.append("         AND mic.shop_category_id in( ").append(paramBean.getListCategoryId()).append(" )");
            sql.append("         )");
            sql.append(" OR ");
            sql.append(" EXISTS(");
            sql.append("         SELECT 1 ");
            sql.append("         FROM view_data_sales_detail_valid dsd1");
            sql.append("         INNER JOIN mst_course mc ON dsd1.product_id = mc.course_id AND T3.product_division = dsd1.product_division AND dsd1.product_division in (5,6) ");
            sql.append("         INNER JOIN mst_course_class mcc ON mcc.course_class_id = mc.course_class_id");
            sql.append("         WHERE dsd1.slip_no = T3.slip_no AND dsd1.shop_id = T3.shop_id AND dsd1.slip_detail_no = T3.slip_detail_no");
            sql.append("         AND mcc.shop_category_id in( ").append(paramBean.getListCategoryId()).append(" )");
            sql.append("         )");
            sql.append(" )");
        }
        sql.append(" WHERE T1.delete_date IS NULL   ");
        sql.append(" GROUP BY T1.staff_id ,         ");
        sql.append(" 	         T1.staff_no    ,   ");
        sql.append(" 		 T1.staff_name1 ,   ");
        sql.append(" 		 T1.staff_name2 ,   ");
        sql.append(" 		 T1.staff_kana1 ,   ");
        sql.append(" 		 T1.staff_kana2 ,   ");
        sql.append(" 		 T2.shop_id ,       ");
        sql.append(" 		 T2.shop_name       ");
        sql.append(" ORDER BY  T2.shop_id, T1.staff_id   ");

        ResultSetWrapper rs = cw.executeQuery(sql.toString());

        List<OriginalDetailBean> detailList = new ArrayList<OriginalDetailBean>();

        // 変更に伴う解約以外の施術担当スタッフを取得
        while (rs.next()) {
            Integer staffID = rs.getInt("staff_id");
            if (staffID != 0) {
                OriginalDetailBean bean = new OriginalDetailBean ();
                MstStaff staff = new MstStaff();
                MstShop shop = new MstShop();
                staff.setStaffID( staffID );
                staff.setStaffNo( rs.getString("staff_no"));
                staff.setStaffName( new String[]{ rs.getString("staff_name1"), rs.getString("staff_name2") });
                shop.setShopID( rs.getInt("shop_id") );
                staff.setShop(shop);
                staff.setShopName( rs.getString("shop_name") );
                
                bean.setStaff(staff);
                bean.setShop(shop);
                bean.setTechnicAmount( rs.getLong("technic_amount") );
                bean.setItemAmount( rs.getLong("item_amount")    );
                bean.setCourseAmount( rs.getLong("course_amount")  );
                bean.setDigestionAmount( rs.getLong("digestion_amount"));
                
                detailList.add(bean) ;
            }
        }

        return detailList;
    }
    
    /**
     * 消化金額
     * @param cw
     * @param paramBean
     * @return
     * @throws Exception 
     */
    private Map<Integer, Map<String, Integer>> getStaffCourseConsumption(ConnectionWrapper cw, ReportParameterBean paramBean) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        //消化状況を集計
        sql.append(" select");
        sql.append("     t.staff_id");
        sql.append("     ,count(total_customer_num) as total_customer_num");
        sql.append("     ,sum(total_consumption_value) as total_consumption_value");
        sql.append(" from (");
        sql.append(" select");
        sql.append("     dcd.staff_id");
        sql.append("     ,mcc.display_seq as mcc_display_seq");
        sql.append("     ,mcc.course_class_id");
        sql.append("     ,mcc.course_class_name");
        sql.append("     ,count(distinct dcd.customer_id) as total_customer_num");
        
        if (paramBean.getTaxType() == ReportParameterBean.TAX_TYPE_BLANK) {
            // 税抜き
            sql.append("     ,ceil(sum(dcd.product_num*dcd.course_sales_value/dcd.course_consumption_num) / (1.0 + get_tax_rate(dcd.tax_rate, max(dcd.sales_date)))) as total_consumption_value");
        } else {
            // 税込み
            sql.append("     ,ceil(sum(dcd.product_num*dcd.course_sales_value/dcd.course_consumption_num)) as total_consumption_value");
        }
        
        sql.append(" from mst_course mc");
        sql.append("     inner join mst_course_class mcc");
        sql.append("     on mc.course_class_id = mcc.course_class_id");
        sql.append("     left outer join ");
        sql.append("     (");
        sql.append("         select");
       
        if ((paramBean.getCategoryIDList()!=null && !paramBean.getCategoryIDList().equals(""))) {
            sql.append("         dsmt.staff_id,dcd2.product_num, ds2.customer_id, ds2.sales_date, dc2.product_id, dc2.product_num as course_consumption_num, dc2.product_value as course_sales_value, dsd.tax_rate");
        } else {
            sql.append("         dcd2.*, ds2.customer_id, ds2.sales_date, dc2.product_id, dc2.product_num as course_consumption_num, dc2.product_value as course_sales_value, dsd.tax_rate");
        }

        sql.append("         from data_sales ds2");
        
        if ((paramBean.getCategoryIDList()!=null && !paramBean.getCategoryIDList().equals(""))) {
            sql.append("         INNER JOIN data_sales_mainstaff dsmt ON dsmt.shop_id = ds2.shop_id AND dsmt.slip_no = ds2.slip_no ");
        }

        sql.append("         inner join data_contract_digestion dcd2");
        sql.append("         on ds2.shop_id = dcd2.shop_id and ds2.slip_no = dcd2.slip_no");
        sql.append("         inner join data_contract  dc2");        
        sql.append("         on dcd2.contract_shop_id = dc2.shop_id and dcd2.contract_no = dc2.contract_no and dcd2.contract_detail_no = dc2.contract_detail_no");
        sql.append("         inner join mst_staff ms2");
        sql.append("         on dcd2.staff_id = ms2.staff_id");
        sql.append("         inner join data_sales_detail dsd");
        sql.append("         on dc2.shop_id = dsd.contract_shop_id");
        sql.append("         and dc2.contract_no = dsd.contract_no");
        sql.append("         and dc2.contract_detail_no = dsd.contract_detail_no");
        sql.append("         and ds2.shop_id = dsd.shop_id");
        sql.append("         and ds2.slip_no = dsd.slip_no");
        sql.append("         where ds2.shop_id in (").append(paramBean.getShopIDList()).append(")");
        sql.append("         and ds2.sales_date between '").append(paramBean.getTargetStartDate()).append("' and '").append(paramBean.getTargetEndDate()).append("'");
        sql.append("     ) dcd");
        sql.append("     on dcd.product_id = mc.course_id");
        sql.append(" where mc.delete_date is null");

        if ((paramBean.getCategoryIDList()!=null && !paramBean.getCategoryIDList().equals(""))) {
            sql.append(" and mcc.shop_category_id in ("+paramBean.getCategoryIDList()+")");
        }

        sql.append(" group by dcd.staff_id,mcc.display_seq,mcc.course_class_id,mcc.course_class_name, dcd.tax_rate, dcd.product_id, dcd.shop_id, dcd.slip_no )");
        sql.append("  as t ");
        sql.append(" group by t.staff_id");
        
        sql.append(" union all");

        //削除されたコースに対する消化状況を集計
        sql.append(" select");
        sql.append("     t.staff_id");
        sql.append("     ,count(total_customer_num) as total_customer_num");
        sql.append("     ,sum(total_consumption_value) as total_consumption_value");
        sql.append(" from (");
        sql.append(" select");
        sql.append("     dcd.staff_id");
        sql.append("     ,mcc.display_seq as mcc_display_seq");
        sql.append("     ,mcc.course_class_id");
        sql.append("     ,mcc.course_class_name");
        sql.append("     ,count(distinct dcd.customer_id) as total_customer_num");
        
        if (paramBean.getTaxType() == ReportParameterBean.TAX_TYPE_BLANK) {
            // 税抜き
            sql.append("     ,ceil(sum(dcd.product_num*dcd.course_sales_value/dcd.course_consumption_num) / (1.0 + get_tax_rate(dcd.tax_rate, max(dcd.sales_date)))) as total_consumption_value");
        } else {
            // 税込み
            sql.append("     ,ceil(sum(dcd.product_num*dcd.course_sales_value/dcd.course_consumption_num)) as total_consumption_value");
        }
        sql.append(" from mst_course mc");
        sql.append("     inner join mst_course_class mcc");
        sql.append("     on mc.course_class_id = mcc.course_class_id");
        sql.append("     inner join ");
        sql.append("     (");
        sql.append("         select");
        
        if ((paramBean.getCategoryIDList()!=null && !paramBean.getCategoryIDList().equals(""))) {
            sql.append("         dsmt.staff_id,dcd2.product_num, ds2.customer_id, ds2.sales_date, dc2.product_id, dc2.product_num as course_consumption_num, dc2.product_value as course_sales_value, dsd.tax_rate");
        } else {
            sql.append("         dcd2.*, ds2.customer_id, ds2.sales_date, dc2.product_id, dc2.product_num as course_consumption_num, dc2.product_value as course_sales_value, dsd.tax_rate");
        }
        
        sql.append("         from data_sales ds2");
        
        if ((paramBean.getCategoryIDList()!=null && !paramBean.getCategoryIDList().equals(""))) {
            sql.append("         INNER JOIN data_sales_mainstaff dsmt ON dsmt.shop_id = ds2.shop_id AND dsmt.slip_no = ds2.slip_no ");
        }

        sql.append("         inner join data_contract_digestion dcd2");
        sql.append("         on ds2.shop_id = dcd2.shop_id and ds2.slip_no = dcd2.slip_no");
        sql.append("         inner join data_contract  dc2");        
        sql.append("         on dcd2.contract_shop_id = dc2.shop_id and dcd2.contract_no = dc2.contract_no and dcd2.contract_detail_no = dc2.contract_detail_no");
        sql.append("         inner join mst_staff ms2");
        sql.append("         on dcd2.staff_id = ms2.staff_id");
        sql.append("         inner join data_sales_detail dsd");
        sql.append("         on dc2.shop_id = dsd.contract_shop_id");
        sql.append("         and dc2.contract_no = dsd.contract_no");
        sql.append("         and dc2.contract_detail_no = dsd.contract_detail_no");
        sql.append("         and ds2.shop_id = dsd.shop_id");
        sql.append("         and ds2.slip_no = dsd.slip_no");
        sql.append("         where ds2.shop_id in (").append(paramBean.getShopIDList()).append(")");
        sql.append("         and ds2.sales_date between '").append(paramBean.getTargetStartDate()).append("' and '").append(paramBean.getTargetEndDate()).append("'");
        sql.append("     ) dcd");
        sql.append("     on dcd.product_id = mc.course_id");
        sql.append(" where mc.delete_date is not null");
        
        if ((paramBean.getCategoryIDList()!=null && !paramBean.getCategoryIDList().equals(""))) {
            sql.append(" and mcc.shop_category_id in ("+paramBean.getCategoryIDList()+")");
        }
        
        sql.append(" group by dcd.staff_id,mcc.display_seq,mcc.course_class_id,mcc.course_class_name, dcd.tax_rate, dcd.product_id, dcd.shop_id, dcd.slip_no )");
        sql.append("  as t ");
        sql.append(" group by t.staff_id");

        ResultSetWrapper rs = cw.executeQuery(sql.toString());
        
        Map<Integer, Map<String, Integer>> staffCourseConsumptionMap = new HashMap<Integer, Map<String, Integer>>();
        while (rs.next()) {
            Map<String, Integer> staffResultMap = new HashMap<String, Integer>();
            staffResultMap.put("CustomerTotal", rs.getInt("total_customer_num"));
            staffResultMap.put("ConsumptionTotal", rs.getInt("total_consumption_value"));

            staffCourseConsumptionMap.put(rs.getInt("staff_id"), staffResultMap);
        }
        return staffCourseConsumptionMap;
    }
    

    class OriginalDetailBean implements Serializable {
        
        /**
         * 担当
         */
        private MstStaff staff          = null ;
        /**
         * 所属
         */
        private MstShop shop            = null ;
        /**
         * 技術
         */
        private Long technicAmount      = null ;
        /**
         * 商品
         */
        private Long itemAmount         = null ;
        /**
         * 契約欄
         */
        private Long courseAmount       = null ;
        /**
         * 消化欄
         */
        private Long digestionAmount    = null ;
        
        
        public MstStaff getStaff() {
            return staff;
        }

        public void setStaff(MstStaff staff) {
            this.staff = staff;
        }

        public MstShop getShop() {
            return shop;
        }

        public void setShop(MstShop shop) {
            this.shop = shop;
        }

        public Long getTechnicAmount() {
            return technicAmount;
        }

        public void setTechnicAmount(Long technicAmount) {
            this.technicAmount = technicAmount;
        }

        public Long getItemAmount() {
            return itemAmount;
        }

        public void setItemAmount(Long itemAmount) {
            this.itemAmount = itemAmount;
        }

        public Long getCourseAmount() {
            return courseAmount;
        }

        public void setCourseAmount(Long courseAmount) {
            this.courseAmount = courseAmount;
        }

        public Long getDigestionAmount() {
            return digestionAmount;
        }

        public void setDigestionAmount(Long digestionAmount) {
            this.digestionAmount = digestionAmount;
        }
        
        /**
         * 消化欄・金額
         * @return 
         */
        public Long getDigestionTotal () {
            return (nullToZero (digestionAmount) + nullToZero(technicAmount))/ROUND_DOWN ; // 消化＋技術
        }
        
        /**
         * 物販欄・金額
         * @return 
         */
        public Long getProductSalesTotal () {
            return nullToZero (itemAmount)/ROUND_DOWN ; // 商品の金額　
        }
        
        /**
         * 役務欄・金額
         * @return 
         */
        public Long getServicesTotal () {
            return (nullToZero (courseAmount) + nullToZero(technicAmount))/ROUND_DOWN ; // 契約＋技術
        }
        
        /**
         * 物販＋消化欄・金額
         * @return 
         */
        public Long getProductSalesDigestionTotal () {
            return (nullToZero (itemAmount) + nullToZero (digestionAmount) + nullToZero(technicAmount))/ROUND_DOWN ; // 商品＋消化＋技術
        }
        
        /**
         * 物販＋契約欄・金額
         * @return 
         */
        public Long getProductSalesServicesTotal () {
            return (nullToZero (itemAmount) + nullToZero (courseAmount) + nullToZero(technicAmount))/ROUND_DOWN ; // 商品＋契約＋技術
        }
        
        private Long nullToZero(Long value) {
            return value == null ? 0L : value;
        }
    }
    
}
