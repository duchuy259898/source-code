/*
 * TimeAnalysisPanel.java
 *
 * Created on 2008/07/20, 11:00
 */
package com.geobeck.sosia.pos.hair.report;

import com.geobeck.sosia.pos.hair.report.util.JExcelApi;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.*;
import com.geobeck.swing.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.swing.SelectTableCellRenderer;
import com.geobeck.sosia.pos.swing.table.ColumnGroup;
import com.geobeck.sosia.pos.swing.table.GroupableTableHeader;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.CheckUtil;
import com.geobeck.util.SQLUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import jxl.HeaderFooter;
import java.util.*;
import java.util.Map;

/**
 *
 * @author geobeck
 */
public class TimeAnalysisPanel1 extends com.geobeck.sosia.pos.swing.AbstractImagePanelEx {

    private static final String[] fn_strWDay = {"日", "月", "火", "水", "木", "金", "土"};
    private String shopIdList = "";
    private String shopName = "";
    private JTableEx dailyFixed;
    private JTableEx categoryFixed;
    private JTableEx weeklyFixed;
    private List<Integer> listTime = new ArrayList<Integer>();
   
    /**
     * Creates new form TimeAnalysisPanel
     */
    public TimeAnalysisPanel1() {
        initComponents();
        addMouseCursorChange();
        this.setSize(835, 690);
        this.setPath("帳票出力");
        this.setTitle("時間帯分析");
        this.setKeyListener();

        SystemInfo.initGroupShopComponents(shop, 3);

        this.init();

        this.initDailyHeader();
        this.initCategoryHeader();
        this.initWeeklyHeader();
        

    }
    public TimeAnalysisPanel1(HashMap hashMap) 
    {
        
        initComponents();
        addMouseCursorChange();
        this.setSize(835, 690);
        this.setPath("帳票出力");
        this.setTitle("時間帯分析");
        this.setKeyListener();

        SystemInfo.initGroupShopComponents(shop, 3);

        this.init();
        this.setValueInit(hashMap);
        this.initDailyHeader();
        this.initCategoryHeader();
        this.initWeeklyHeader();
        autoShow();
    }
    
    public void setValueInit(HashMap hashMap)
    {
       try
       {
        
           /*this.showButton.setVisible(false);
           this.btnTimeSetting.setVisible(false);
           this.startDate.setEnabled(false);
           this.endDate.setEnabled(false);
           this.rdoClassAll.setEnabled(false);
           this.rdoClassTech.setEnabled(false);
           this.rdoClassItem.setEnabled(false);
           this.rdoVisitTime.setEnabled(false);
           this.rdoStartTime.setEnabled(false);
           this.rdoLeaveTime.setEnabled(false);
           this.rdoTimeFixed.setEnabled(false);
           this.rdoTimeCustom.setEnabled(false);
           this.rdoOutValue.setEnabled(false);
           this.rdoOutCount.setEnabled(false);
           this.rdoTaxUnit.setEnabled(false);
           this.rdoTaxBlank.setEnabled(false);*/
        Set set = hashMap.entrySet();
        Iterator i = set.iterator(); 
        while(i.hasNext())
        {
                Map.Entry me = (Map.Entry)i.next();
                if(me.getKey().toString().equals("対象期間"))
                {
                    
                    startDate.setDate(me.getValue().toString());
                }
                if(me.getKey().toString().equals("対象期間1"))
                {
                    endDate.setDate(me.getValue().toString());
                }
                if(me.getKey().toString().equals("対象"))
                {
                
                  this.shop.getLabel().setText(me.getValue().toString());
                       
                }
                if(me.getKey().toString().equals("日別集計"))
                {
                    this.timeTab.setSelectedIndex(0);
                    //this.timeTab.setEnabledAt(1, false);
                    //this.timeTab.setEnabledAt(2, false);
                    //autoShow();
                }
                if(me.getKey().toString().equals("分類別集計"))
                {
                    this.timeTab.setSelectedIndex(1);
                    //this.timeTab.setEnabledAt(0, false);
                    //this.timeTab.setEnabledAt(2, false);
                    //autoShow();
                }
                if(me.getKey().toString().equals("曜日別集計"))
                {
                    this.timeTab.setSelectedIndex(2);
                   // this.timeTab.setEnabledAt(1, false);
                    //this.timeTab.setEnabledAt(0, false);
                   // autoShow();
                }
                if(me.getKey().toString().equals("すべて"))
                {
                   this.rdoClassAll.setSelected(Boolean.parseBoolean(me.getValue().toString()));
                }
                
                if(me.getKey().toString().equals("技術分類"))
                {
                    this.rdoClassTech.setSelected(Boolean.parseBoolean(me.getValue().toString()));
                }
                 if(me.getKey().toString().equals("商品分類"))
                {
                    this.rdoClassItem.setSelected(Boolean.parseBoolean(me.getValue().toString()));
                }
                if(me.getKey().toString().equals("受付時間"))
                {
                    this.rdoVisitTime.setSelected(Boolean.parseBoolean(me.getValue().toString()));
                }
                if(me.getKey().toString().equals("施術開始時間"))
                {
                    this.rdoStartTime.setSelected(Boolean.parseBoolean(me.getValue().toString()));
                }
                 if(me.getKey().toString().equals("精算時間"))
                {
                    this.rdoLeaveTime.setSelected(Boolean.parseBoolean(me.getValue().toString()));
                }
                 if(me.getKey().toString().equals("1時間"))
                {
                    this.rdoTimeFixed.setSelected(Boolean.parseBoolean(me.getValue().toString()));
                }
                 if(me.getKey().toString().equals("個別設定"))
                {
                     this.rdoTimeCustom.setSelected(Boolean.parseBoolean(me.getValue().toString()));
                }
                  if(me.getKey().toString().equals("金額"))
                {
                    this.rdoOutValue.setSelected(Boolean.parseBoolean(me.getValue().toString()));
                }
                  if(me.getKey().toString().equals("人数"))
                {
                     this.rdoOutCount.setSelected(Boolean.parseBoolean(me.getValue().toString()));
                }
                   if(me.getKey().toString().equals("税込"))
                {
                    this.rdoTaxUnit.setSelected(Boolean.parseBoolean(me.getValue().toString()));
                }
                    if(me.getKey().toString().equals("税抜"))
                {
                    this.rdoTaxBlank.setSelected(Boolean.parseBoolean(me.getValue().toString()));
                }
        }
       }
        catch(Exception  ex )
        {
        }
    }
    /**
     * 初期化処理を行う。
     */
    public void init() {

        //対象期間の設定
        endDate.setDate(new Date());
        startDate.setDate(new Date());

        //税抜、税込の初期設定
        if (SystemInfo.getAccountSetting().getReportPriceType() == 0) {
            rdoTaxBlank.setSelected(false);
            rdoTaxUnit.setSelected(true);
        } else {
            rdoTaxBlank.setSelected(true);
            rdoTaxUnit.setSelected(false);
        }

        MstShopSetting mss = MstShopSetting.getInstance();
        switch (mss.getTimeAnalysisCondition1()) {
            case 0:
                rdoVisitTime.setSelected(true);
                break;
            case 1:
                rdoStartTime.setSelected(true);
                break;
            case 2:
                rdoLeaveTime.setSelected(true);
                break;
        }
        switch (mss.getTimeAnalysisCondition2()) {
            case 0:
                rdoTimeFixed.setSelected(true);
                break;
            case 1:
                rdoTimeCustom.setSelected(true);
                break;
        }
    }

    /**
     * --------------------------------------- 日別集計
     * ---------------------------------------
     */
    private void showDaily() {

        SwingUtil.clearTable(daily);
        DefaultTableModel model = (DefaultTableModel) daily.getModel();

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate.getDate());
        Calendar calPrev = (Calendar) cal.clone();
        calPrev.add(Calendar.YEAR, -1);

        final int MAC_COL = daily.getColumnCount();

        final int MAX_DAY = Math.max(cal.getActualMaximum(Calendar.DAY_OF_MONTH), calPrev.getActualMaximum(Calendar.DAY_OF_MONTH));

        // 日、曜日
        for (int i = 0; i < MAX_DAY; i++) {
            model.addRow(new Object[MAC_COL - 1]);
            int row = model.getRowCount() - 1;
            dailyFixed.setValueAt(i + 1, row, 0);
            dailyFixed.setValueAt(fn_strWDay[cal.get(Calendar.DAY_OF_WEEK) - 1], row, 1);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        // 天気
        try {
            ResultSetWrapper rs = getWeatherData();
            while (rs.next()) {
                Calendar calTmp = Calendar.getInstance();
                calTmp.setTime(rs.getDate("manage_date"));
                int row = calTmp.get(Calendar.DAY_OF_MONTH) - 1;
                dailyFixed.setValueAt(rs.getString("weather_name"), row, 2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // フッタ合計欄初期化
        SwingUtil.clearTable(dailyTotal);
        DefaultTableModel modelTotal = (DefaultTableModel) dailyTotal.getModel();
        modelTotal.addRow(new Object[dailyTotal.getColumnCount() - 1]);
        modelTotal.addRow(new Object[dailyTotal.getColumnCount() - 1]);

        // 時間帯集計
        try {
            Calendar calTmp = Calendar.getInstance();
            ResultSetWrapper rs = getDailyData();
            while (rs.next()) {
                calTmp.setTime(rs.getDate("sales_date"));
                int row = calTmp.get(Calendar.DAY_OF_MONTH) - 1;
                int col = rs.getInt("col_index");
                if (rdoOutValue.isSelected()) {
                    addValue(daily, row, col, rs.getLong("sales_value"));
                } else {
                    addValue(daily, row, col, rs.getLong("customer_count"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 営業日数
        int dateCount = 0;

        // 客数、日計
        try {
            Calendar calTmp = Calendar.getInstance();
            ResultSetWrapper rs = getDailyCount();
            while (rs.next()) {
                calTmp.setTime(rs.getDate("sales_date"));
                int row = calTmp.get(Calendar.DAY_OF_MONTH) - 1;
                int col = 0;

                if (rdoOutValue.isSelected()) {
                    // 時間帯集計の日計
                    col = MAC_COL - 10;
                    addValue(daily, row, col, rs.getLong("sales_value"));

                    // 客数
                    col = MAC_COL - 8;
                    addValue(daily, row, col, rs.getLong("customer_count"));

                    // 客単価
                    col = MAC_COL - 6;
                    daily.setValueAt(Math.round(rs.getDouble("sales_value") / rs.getDouble("customer_count")), row, col);

                } else {

                    // 時間帯集計の日計
                    col = MAC_COL - 10;
                    addValue(daily, row, col, rs.getLong("customer_count"));

                }

                // 客数が存在する場合は営業日のためNULL列をゼロ埋めする
                for (col = 0; col < MAC_COL; col++) {
                    addValue(daily, row, col, 0);
                }
                dateCount++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 前年実績
        try {
            Calendar calTmp = Calendar.getInstance();
            ResultSetWrapper rs = getDailyDataPrev();
            while (rs.next()) {
                calTmp.setTime(rs.getDate("sales_date"));
                int row = calTmp.get(Calendar.DAY_OF_MONTH) - 1;
                int col = MAC_COL - 4;
                if (rdoOutValue.isSelected()) {
                    addValue(daily, row, col, rs.getLong("sales_value"));
                } else {
                    addValue(daily, row, col, rs.getLong("customer_count"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 累計算出
        Long totalSales = 0l;
        Long totalCount = 0l;
        Long totalPrev = 0l;
        BigDecimal bd = null;

        for (int row = 0; row < daily.getRowCount(); row++) {

            Long dayTotalSales = 0l;
            Long dayTotalCount = 0l;
            Long dayTotalPrev = 0l;

            // 時間帯集計
            int col = MAC_COL - 10;
            Long value = (Long) daily.getValueAt(row, col);
            if (value != null) {
                dayTotalSales = value;
                totalSales += dayTotalSales;
                col = MAC_COL - 9;
                daily.setValueAt(totalSales, row, col);
            }

            // 客数
            col = MAC_COL - 8;
            value = (Long) daily.getValueAt(row, col);
            if (value != null) {
                dayTotalCount = value;
                totalCount += dayTotalCount;

                if (rdoOutValue.isSelected()) {
                    // 客数
                    col = MAC_COL - 7;
                    daily.setValueAt(totalCount, row, col);

                    // 客単価
                    col = MAC_COL - 5;
                    daily.setValueAt(Math.round(totalSales.doubleValue() / totalCount.doubleValue()), row, col);
                }
            }

            // 前年実績
            col = MAC_COL - 4;
            value = (Long) daily.getValueAt(row, col);
            if (value != null) {
                dayTotalPrev = value;
                totalPrev += dayTotalPrev;
                col = MAC_COL - 3;
                daily.setValueAt(totalPrev, row, col);
            }

            // 前年対比
            if (dayTotalPrev != 0) {
                col = MAC_COL - 2;
                bd = new BigDecimal(dayTotalSales.doubleValue() / dayTotalPrev * 100);
                daily.setValueAt(bd.setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "%", row, col);
            }

            if (totalPrev != 0) {
                col = MAC_COL - 1;
                bd = new BigDecimal(totalSales.doubleValue() / totalPrev * 100);
                daily.setValueAt(bd.setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "%", row, col);
            }

        }

        // フッタ合計
        for (int col = 0; col < MAC_COL; col++) {

            if (col == (MAC_COL - 9) || col == (MAC_COL - 7) || col == (MAC_COL - 3)) {

                dailyTotal.setValueAt(dailyTotal.getValueAt(0, col - 1), 0, col);
                dailyTotal.setValueAt(dailyTotal.getValueAt(1, col - 1), 1, col);

            } else if (col == (MAC_COL - 6) && rdoOutValue.isSelected()) {

                double d1 = Double.valueOf(dailyTotal.getValueAt(0, MAC_COL - 10).toString());
                double d2 = Double.valueOf(dailyTotal.getValueAt(0, MAC_COL - 8).toString());
                dailyTotal.setValueAt(Math.round(d1 / d2), 0, col);
                dailyTotal.setValueAt(Math.round(d1 / d2), 1, col);

            } else if (col == (MAC_COL - 5) && rdoOutValue.isSelected()) {

                double d1 = Double.valueOf(dailyTotal.getValueAt(0, MAC_COL - 9).toString());
                double d2 = Double.valueOf(dailyTotal.getValueAt(0, MAC_COL - 7).toString());
                dailyTotal.setValueAt(Math.round(d1 / d2), 0, col);
                dailyTotal.setValueAt(Math.round(d1 / d2), 1, col);

            } else if (col == (MAC_COL - 2)) {

                double v1 = Long.valueOf(dailyTotal.getValueAt(0, MAC_COL - 10).toString());
                double v2 = Long.valueOf(dailyTotal.getValueAt(0, MAC_COL - 4).toString());
                if (v2 != 0) {
                    bd = new BigDecimal(v1 / v2 * 100);
                    dailyTotal.setValueAt(bd.setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "%", 0, col);
                }

                v1 = Long.valueOf(dailyTotal.getValueAt(1, MAC_COL - 10).toString());
                v2 = Long.valueOf(dailyTotal.getValueAt(1, MAC_COL - 4).toString());
                if (v2 != 0) {
                    bd = new BigDecimal(v1 / v2 * 100);
                    dailyTotal.setValueAt(bd.setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "%", 1, col);
                }

            } else if (col == (MAC_COL - 1)) {

                double v1 = Long.valueOf(dailyTotal.getValueAt(0, MAC_COL - 9).toString());
                double v2 = Long.valueOf(dailyTotal.getValueAt(0, MAC_COL - 3).toString());
                if (v2 != 0) {
                    bd = new BigDecimal(v1 / v2 * 100);
                    dailyTotal.setValueAt(bd.setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "%", 0, col);
                }

                v1 = Long.valueOf(dailyTotal.getValueAt(1, MAC_COL - 9).toString());
                v2 = Long.valueOf(dailyTotal.getValueAt(1, MAC_COL - 3).toString());
                if (v2 != 0) {
                    bd = new BigDecimal(v1 / v2 * 100);
                    dailyTotal.setValueAt(bd.setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "%", 1, col);
                }

            } else if (col == (MAC_COL - 4)) {

                Long total = 0l;
                Long count = 0l;

                for (int row = 0; row < daily.getRowCount(); row++) {
                    Long value = (Long) daily.getValueAt(row, col);
                    if (value != null) {
                        total += value;
                        count++;
                    }
                }

                dailyTotal.setValueAt(total, 0, col);
                dailyTotal.setValueAt(Math.round(total.doubleValue() / count), 1, col);

            } else {

                Long total = 0l;
                for (int row = 0; row < daily.getRowCount(); row++) {
                    Long value = (Long) daily.getValueAt(row, col);
                    if (value != null) {
                        total += value;
                    }
                }

                dailyTotal.setValueAt(total, 0, col);
                dailyTotal.setValueAt(Math.round(total.doubleValue() / dateCount), 1, col);
            }
        }

        dailyFixed.changeSelection(0, 0, false, false);
        daily.changeSelection(0, 0, false, false);
    }

    private ResultSetWrapper getWeatherData() {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      a.manage_date");
        sql.append("     ,b.weather_name");
        sql.append(" from");
        sql.append("     data_register a");
        sql.append("         join mst_weather b");
        sql.append("             using(weather_id)");
        sql.append(" where");
        sql.append("         a.delete_date is null");

        if (shop.getSelectedItem() instanceof MstShop) {
            sql.append("     and a.shop_id = " + SQLUtil.convertForSQL(((MstShop) shop.getSelectedItem()).getShopID()));
            sql.append("     and a.manage_date between " + SQLUtil.convertForSQLDateOnly(startDate.getDate()));
            sql.append("                           and " + SQLUtil.convertForSQLDateOnly(endDate.getDate()));
        } else {
            sql.append("     and false");
        }

        ResultSetWrapper rs = null;

        try {

            rs = SystemInfo.getConnection().executeQuery(sql.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    private ResultSetWrapper getDailyData() {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      sales_date");
        sql.append("     ,case");

        for (int i = 0; i < (listTime.size() - 1); i++) {
            sql.append("     when " + listTime.get(i) + " <= target_time and target_time < " + listTime.get(i + 1) + " then " + i);
        }

        sql.append("      end as col_index");
        sql.append("     ,sum(sales_value) as sales_value");

        sql.append("     ,count(");
        sql.append("         case when exists");
        sql.append("             (");
        sql.append("                 select 1");
        sql.append("                 from");
        sql.append("                     data_sales_detail");
        sql.append("                 where");
        sql.append("                         shop_id = t.shop_id");
        sql.append("                     and slip_no = t.slip_no");
        sql.append("                     and delete_date is null");
        sql.append("                     and product_division not in (3)");
        sql.append("             ) then slip_no end");
        sql.append("      ) as customer_count");

        sql.append(" from");
        sql.append("     (");
        sql.append("         select");
        sql.append("              ds.shop_id");
        sql.append("             ,ds.slip_no");
        sql.append("             ,ds.sales_date");

        if (rdoVisitTime.isSelected()) {
            sql.append("         ," + getTargetTimeColumn("dr.visit_time"));
        } else if (rdoStartTime.isSelected()) {
            sql.append("         ," + getTargetTimeColumn("dr.start_time"));
        } else {
            sql.append("         ," + getTargetTimeColumn("dr.leave_time"));
        }

        //IVS_LVTu start edit 2017/10/16 #27846 [gb]時間帯分析：精算時間が営業時間前の時に集計時間帯がおかしい
         sql.append("    ,(select coalesce(sum(");
        if (rdoTaxUnit.isSelected()) {
            sql.append("    a.detail_value_in_tax - a.discount_value");
        } else {
            sql.append("    CASE");
            sql.append("    WHEN (a.detail_value_in_tax - a.discount_value - (a.detail_value_no_tax - sign(a.discount_value / (1.0 + get_tax_rate(a.sales_date))) * ceil(abs(a.discount_value / (1.0 + get_tax_rate(a.sales_date)))))) < 0::numeric THEN a.detail_value_in_tax - a.discount_value - (a.detail_value_in_tax - a.discount_value - (a.detail_value_no_tax - sign(a.discount_value / (1.0 + get_tax_rate(a.sales_date))) * ceil(abs(a.discount_value / (1.0 + get_tax_rate(a.sales_date))))))");
            sql.append("    ELSE a.detail_value_no_tax - sign(a.discount_value / (1.0 + get_tax_rate(a.sales_date))) * ceil(abs(a.discount_value / (1.0 + get_tax_rate(a.sales_date))))");
            sql.append("    END ");
        }
        sql.append("    ),0) AS sales_value");
        sql.append("    from view_data_sales_detail_valid a");
        sql.append("    where a.shop_id = ds.shop_id and a.slip_no = ds.slip_no and a.product_division <> 6");
        sql.append("    )  AS sales_value");
        //IVS_LVTu end edit 2017/10/16 #27846 [gb]時間帯分析：精算時間が営業時間前の時に集計時間帯がおかしい
        sql.append("         from");
        sql.append("             view_data_sales_valid ds");
        sql.append("                 join data_reservation dr");
        sql.append("                     using(shop_id, slip_no)");
        sql.append("                 join mst_shop ms");
        sql.append("                     using(shop_id)");
        sql.append("         where");
        sql.append("                 ds.shop_id in (" + this.shopIdList + ")");
        sql.append("             and ds.sales_date between " + SQLUtil.convertForSQLDateOnly(startDate.getDate()));
        sql.append("                                   and " + SQLUtil.convertForSQLDateOnly(endDate.getDate()));
        sql.append("     ) t");
        sql.append(" group by");
        sql.append("      sales_date");
        sql.append("     ,col_index");

        ResultSetWrapper rs = null;

        try {

            rs = SystemInfo.getConnection().executeQuery(sql.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    private ResultSetWrapper getDailyCount() {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      sales_date");

        // 客数にはクレームのみ客を含めない
        sql.append("     ,count(");
        sql.append("         case when exists");
        sql.append("             (");
        sql.append("                 select 1");
        sql.append("                 from");
        sql.append("                     data_sales_detail");
        sql.append("                 where");
        sql.append("                         shop_id = ds.shop_id");
        sql.append("                     and slip_no = ds.slip_no");
        sql.append("                     and delete_date is null");
        sql.append("                     and product_division not in (3)");
        sql.append("             ) then slip_no end");
        sql.append("      ) as customer_count");
        
        // 20171019 edit start #27846 [GB内対応][gb]時間帯分析：消化金額も含まれる
        //        if (rdoTaxUnit.isSelected()) {
        //            sql.append(" ,sum(discount_sales_value_in_tax) as sales_value");
        //        } else {
        //            sql.append(" ,sum(discount_sales_value_no_tax) as sales_value");
        //        }
        //
        //        sql.append(" from");
        //        sql.append("     view_data_sales_valid ds");
        //        sql.append(" where");
        //        sql.append("         shop_id in (" + this.shopIdList + ")");
        //        sql.append("     and sales_date between " + SQLUtil.convertForSQLDateOnly(startDate.getDate()));
        //        sql.append("                        and " + SQLUtil.convertForSQLDateOnly(endDate.getDate()));
        //        sql.append(" group by");
        //        sql.append("     sales_date");
        sql.append("    ,sum((select coalesce(sum(");
        if (rdoTaxUnit.isSelected()) {
            sql.append("    a.detail_value_in_tax - a.discount_value");
        } else {
            sql.append("    CASE");
            sql.append("    WHEN (a.detail_value_in_tax - a.discount_value - (a.detail_value_no_tax - sign(a.discount_value / (1.0 + get_tax_rate(a.sales_date))) * ceil(abs(a.discount_value / (1.0 + get_tax_rate(a.sales_date)))))) < 0::numeric THEN a.detail_value_in_tax - a.discount_value - (a.detail_value_in_tax - a.discount_value - (a.detail_value_no_tax - sign(a.discount_value / (1.0 + get_tax_rate(a.sales_date))) * ceil(abs(a.discount_value / (1.0 + get_tax_rate(a.sales_date))))))");
            sql.append("    ELSE a.detail_value_no_tax - sign(a.discount_value / (1.0 + get_tax_rate(a.sales_date))) * ceil(abs(a.discount_value / (1.0 + get_tax_rate(a.sales_date))))");
            sql.append("    END ");
        }
        sql.append("    ),0)AS sales_value");
	sql.append("    from view_data_sales_detail_valid a");
	sql.append("    where a.shop_id = ds.shop_id and a.slip_no = ds.slip_no and a.product_division <> 6");
	sql.append("    ))  AS sales_value");

        sql.append(" from");
        sql.append("     view_data_sales_valid ds");
        sql.append("     JOIN data_reservation dr using(shop_id, slip_no) ");
        sql.append(" where");
        sql.append("         shop_id in (" + this.shopIdList + ")");
        sql.append("     and sales_date between " + SQLUtil.convertForSQLDateOnly(startDate.getDate()));
        sql.append("                        and " + SQLUtil.convertForSQLDateOnly(endDate.getDate()));
        sql.append(" group by");
        sql.append("     sales_date");
         // 20171019 edit end #27846 [GB内対応][gb]時間帯分析：消化金額も含まれる

        // 売上なしのレジ締め日取得
        sql.append(" union all");
        sql.append(" select distinct");
        sql.append("      manage_date");
        sql.append("     ,0");
        sql.append("     ,0");
        sql.append(" from");
        sql.append("     data_register dr");
        sql.append(" where");
        sql.append("         delete_date is null");
        sql.append("     and shop_id in (" + this.shopIdList + ")");
        sql.append("     and manage_date between " + SQLUtil.convertForSQLDateOnly(startDate.getDate()));
        sql.append("                         and " + SQLUtil.convertForSQLDateOnly(endDate.getDate()));
        sql.append("     and not exists");
        sql.append("         (");
        sql.append("             select 1");
        sql.append("             from");
        sql.append("                 data_sales");
        sql.append("             where");
        sql.append("                     delete_date is null");
        sql.append("                 and shop_id = dr.shop_id");
        sql.append("                 and sales_date = dr.manage_date");
        sql.append("         )");

        ResultSetWrapper rs = null;

        try {

            rs = SystemInfo.getConnection().executeQuery(sql.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    private ResultSetWrapper getDailyDataPrev() {

        Calendar calStart = Calendar.getInstance();
        calStart.setTime(startDate.getDate());
        calStart.add(Calendar.YEAR, -1);
        Calendar calEnd = (Calendar) calStart.clone();
        calEnd.set(Calendar.DAY_OF_MONTH, calEnd.getActualMaximum(Calendar.DAY_OF_MONTH));

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      sales_date");

        if (rdoTaxUnit.isSelected()) {
            sql.append(" ,sum(discount_sales_value_in_tax) as sales_value");
        } else {
            sql.append(" ,sum(discount_sales_value_no_tax) as sales_value");
        }

        sql.append("     ,count(");
        sql.append("         case when exists");
        sql.append("             (");
        sql.append("                 select 1");
        sql.append("                 from");
        sql.append("                     data_sales_detail");
        sql.append("                 where");
        sql.append("                         shop_id = ds.shop_id");
        sql.append("                     and slip_no = ds.slip_no");
        sql.append("                     and delete_date is null");
        sql.append("                     and product_division not in (3)");
        sql.append("             ) then slip_no end");
        sql.append("      ) as customer_count");

        sql.append(" from");
        sql.append("     view_data_sales_valid ds");
        sql.append(" where");
        sql.append("         shop_id in (" + this.shopIdList + ")");
        sql.append("     and sales_date between " + SQLUtil.convertForSQLDateOnly(calStart));
        sql.append("                        and " + SQLUtil.convertForSQLDateOnly(calEnd));
        sql.append(" group by");
        sql.append("     sales_date");

        ResultSetWrapper rs = null;

        try {

            rs = SystemInfo.getConnection().executeQuery(sql.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    /**
     * --------------------------------------- 分類別集計
     * ---------------------------------------
     */
    private void showCategory() {

        SwingUtil.clearTable(category);
        DefaultTableModel model = (DefaultTableModel) category.getModel();

        final int MAC_COL = category.getColumnCount();

        Map map = new HashMap();

        // 分類名
        try {
            ResultSetWrapper rs = getCategoryHeader();
            while (rs.next()) {
                model.addRow(new Object[MAC_COL - 1]);
                int row = model.getRowCount() - 1;
                int div = rs.getInt("product_division");
                String s = "";
                switch (div) {
                    case 1:
                        s = "（技）";
                        break;
                    case 2:
                        s = "（商）";
                        break;
                    case 3:
                        s = "（技ク）";
                        break;
                    case 4:
                        s = "（商返）";
                        break;
                }
                categoryFixed.setValueAt(s + rs.getString("class_name"), row, 0);
                map.put(div + "_" + rs.getString("class_id"), row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // フッタ合計欄初期化
        SwingUtil.clearTable(categoryTotal);
        DefaultTableModel modelTotal = (DefaultTableModel) categoryTotal.getModel();
        modelTotal.addRow(new Object[categoryTotal.getColumnCount() - 1]);
        modelTotal.addRow(new Object[categoryTotal.getColumnCount() - 1]);
        modelTotal.addRow(new Object[categoryTotal.getColumnCount() - 1]);
        modelTotal.addRow(new Object[categoryTotal.getColumnCount() - 1]);

        // 分類別集計
        try {
            ResultSetWrapper rs = getCategoryData();
            while (rs.next()) {
                int row = Integer.valueOf(map.get(rs.getInt("product_division") + "_" + rs.getString("class_id")).toString());
                int col = rs.getInt("col_index") * 2;
                addValue(category, row, col, rs.getLong("sales_value"));
                addValue(category, row, col + 1, rs.getLong("product_num"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // フッタ集計
        try {
            ResultSetWrapper rs = getCategoryTotal();
            while (rs.next()) {
                int col = rs.getInt("col_index") * 2;
                categoryTotal.setValueAt(rs.getLong("discount_value"), 0, col);
                categoryTotal.setValueAt(rs.getLong("discount_sales_value"), 1, col);
                categoryTotal.setValueAt(rs.getLong("product_num"), 1, col + 1);
                categoryTotal.setValueAt(rs.getLong("customer_count"), 2, col);
                categoryTotal.setValueAt(Math.round(rs.getDouble("discount_sales_value") / rs.getLong("customer_count")), 3, col);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 分類別合計
        for (int row = 0; row < category.getRowCount(); row++) {

            long totalValue = 0;
            long totalCount = 0;

            for (int col = 0; col < (category.getColumnCount() - 2); col++) {

                addValue(category, row, col, 0);

                if (col % 2 == 0) {
                    totalValue += Long.valueOf(category.getValueAt(row, col).toString());
                } else {
                    totalCount += Long.valueOf(category.getValueAt(row, col).toString());
                }
            }

            category.setValueAt(totalValue, row, category.getColumnCount() - 2);
            category.setValueAt(totalCount, row, category.getColumnCount() - 1);
        }

        // フッタ合計
        for (int row = 0; row < categoryTotal.getRowCount(); row++) {

            long totalValue = 0;
            long totalCount = 0;

            for (int col = 0; col < (categoryTotal.getColumnCount() - 2); col++) {

                if (col % 2 == 0) {
                    addValue(categoryTotal, row, col, 0);
                    totalValue += Long.valueOf(categoryTotal.getValueAt(row, col).toString());
                } else {
                    if (row == 1) {
                        addValue(categoryTotal, row, col, 0);
                        totalCount += Long.valueOf(categoryTotal.getValueAt(row, col).toString());
                    }
                }
            }

            categoryTotal.setValueAt(totalValue, row, categoryTotal.getColumnCount() - 2);
            if (row == 1) {
                categoryTotal.setValueAt(totalCount, row, categoryTotal.getColumnCount() - 1);
            }

            if (row == 3) {
                Double d1 = Double.valueOf(categoryTotal.getValueAt(1, categoryTotal.getColumnCount() - 2).toString());
                Double d2 = Double.valueOf(categoryTotal.getValueAt(2, categoryTotal.getColumnCount() - 2).toString());
                categoryTotal.setValueAt(Math.round(d1 / d2), row, categoryTotal.getColumnCount() - 2);
            }
        }

        categoryFixed.changeSelection(0, 0, false, false);
        category.changeSelection(0, 0, false, false);

    }

    private ResultSetWrapper getCategoryHeader() {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select distinct");
        sql.append("      ds.product_division");
        sql.append("     ,coalesce(mtc.technic_class_id, mic.item_class_id) as class_id");
        sql.append("     ,coalesce(mtc.technic_class_name, mic.item_class_name) as class_name");
        sql.append("     ,coalesce(mtc.display_seq, mic.display_seq) as display_seq");
        sql.append(" from");
        sql.append("     view_data_sales_detail_valid ds");
        sql.append("         inner join data_reservation dr");
        sql.append("                 on ds.shop_id = dr.shop_id");
        sql.append("                and ds.slip_no = dr.slip_no");
        sql.append("         left join mst_technic mt");
        sql.append("                on ds.product_division in (1, 3)");
        sql.append("               and ds.product_id = mt.technic_id");
        sql.append("         left join mst_technic_class mtc");
        sql.append("                on mt.technic_class_id = mtc.technic_class_id");
        sql.append("         left join mst_item mi");
        sql.append("                on ds.product_division in (2, 4)");
        sql.append("               and ds.product_id = mi.item_id");
        sql.append("         left join mst_item_class mic");
        sql.append("                on mi.item_class_id = mic.item_class_id");
        sql.append(" where");
        sql.append("         ds.shop_id in (" + this.shopIdList + ")");
        sql.append("     and ds.sales_date between " + SQLUtil.convertForSQLDateOnly(startDate.getDate()));
        sql.append("                           and " + SQLUtil.convertForSQLDateOnly(endDate.getDate()));

        if (rdoClassTech.isSelected()) {
            sql.append(" and ds.product_division in (1, 3)");
        } else if (rdoClassItem.isSelected()) {
            sql.append(" and ds.product_division in (2, 4)");
        }

        sql.append(" order by");
        sql.append("      ds.product_division");
        sql.append("     ,display_seq");

        ResultSetWrapper rs = null;

        try {

            rs = SystemInfo.getConnection().executeQuery(sql.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    private String getCategoryBaseSQL() {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      ds.shop_id");
        sql.append("     ,ds.slip_no");
        sql.append("     ,ds.customer_id");
        sql.append("     ,ds.product_division");
        sql.append("     ,coalesce(mt.technic_class_id, mi.item_class_id) as class_id");

        if (rdoVisitTime.isSelected()) {
            sql.append(" ," + getTargetTimeColumn("dr.visit_time"));
        } else if (rdoStartTime.isSelected()) {
            sql.append(" ," + getTargetTimeColumn("dr.start_time"));
        } else {
            sql.append(" ," + getTargetTimeColumn("dr.leave_time"));
        }

        if (rdoTaxUnit.isSelected()) {
            sql.append(" ,detail_value_in_tax as sales_value");
            sql.append(" ,detail_value_in_tax - discount_detail_value_in_tax as discount_value");
        } else {
            sql.append(" ,detail_value_no_tax as sales_value");
            sql.append(" ,detail_value_no_tax - discount_detail_value_no_tax as discount_value");
        }

        sql.append("     ,ds.product_num");

        sql.append(" from");
        sql.append("     view_data_sales_detail_valid ds");
        sql.append("         inner join data_reservation dr");
        sql.append("                 on ds.shop_id = dr.shop_id");
        sql.append("                and ds.slip_no = dr.slip_no");
        sql.append("         inner join mst_shop ms");
        sql.append("                 on ds.shop_id = ms.shop_id");
        sql.append("         left join mst_technic mt");
        sql.append("                on ds.product_division in (1, 3)");
        sql.append("               and ds.product_id = mt.technic_id");
        sql.append("         left join mst_item mi");
        sql.append("                on ds.product_division in (2, 4)");
        sql.append("               and ds.product_id = mi.item_id");
        sql.append(" where");
        sql.append("         ds.shop_id in (" + this.shopIdList + ")");
        sql.append("     and ds.sales_date between " + SQLUtil.convertForSQLDateOnly(startDate.getDate()));
        sql.append("                           and " + SQLUtil.convertForSQLDateOnly(endDate.getDate()));

        if (rdoClassTech.isSelected()) {
            sql.append(" and ds.product_division in (1, 3)");
        } else if (rdoClassItem.isSelected()) {
            sql.append(" and ds.product_division in (2, 4)");
        }

        return sql.toString();
    }

    private ResultSetWrapper getCategoryData() {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      product_division");
        sql.append("     ,class_id");
        sql.append("     ,case");

        for (int i = 0; i < (listTime.size() - 1); i++) {
            sql.append("     when " + listTime.get(i) + " <= target_time and target_time < " + listTime.get(i + 1) + " then " + i);
        }

        sql.append("      end as col_index");
        sql.append("     ,sum(product_num) as product_num");
        sql.append("     ,sum(sales_value) as sales_value");
        sql.append("     ,sum(discount_value) as discount_value");
        sql.append(" from (" + getCategoryBaseSQL() + ") t");
        sql.append(" group by");
        sql.append("      product_division");
        sql.append("     ,class_id");
        sql.append("     ,col_index");

        ResultSetWrapper rs = null;

        try {

            rs = SystemInfo.getConnection().executeQuery(sql.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    private ResultSetWrapper getCategoryTotal() {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      case");
        for (int i = 0; i < (listTime.size() - 1); i++) {
            sql.append("     when " + listTime.get(i) + " <= target_time and target_time < " + listTime.get(i + 1) + " then " + i);
        }
        sql.append("      end as col_index");
        sql.append("     ,sum(product_num) as product_num");
        sql.append("     ,sum(discount_value) as discount_value");
        sql.append("     ,sum(sales_value - discount_value) as discount_sales_value");

        // 客数にはクレームのみ客を含めない
        sql.append("     ,count(");
        sql.append("         distinct");
        sql.append("         case when exists");
        sql.append("             (");
        sql.append("                 select 1");
        sql.append("                 from");
        sql.append("                     data_sales_detail");
        sql.append("                 where");
        sql.append("                         shop_id = t.shop_id");
        sql.append("                     and slip_no = t.slip_no");
        sql.append("                     and delete_date is null");
        sql.append("                     and product_division not in (3)");
        sql.append("             ) then slip_no end");
        sql.append("      ) as customer_count");

        sql.append(" from");
        sql.append("     (");
        sql.append("         select");
        sql.append("              shop_id");
        sql.append("             ,slip_no");
        sql.append("             ,customer_id");
        sql.append("             ,target_time");
        sql.append("             ,sum(product_num) as product_num");
        sql.append("             ,sum(sales_value) as sales_value");
        sql.append("             ,sum(discount_value) +");
        sql.append("                 (");
        sql.append("                     select");
        if (rdoTaxUnit.isSelected()) {
            sql.append("                     discount_value");
        } else {
            sql.append("                     discount_value_no_tax");
        }
        sql.append("                     from");
        sql.append("                         view_data_sales_valid b");
        sql.append("                     where");
        sql.append("                             shop_id = t.shop_id");
        sql.append("                         and slip_no = t.slip_no");
        sql.append("                 ) as discount_value");
        sql.append(" from (" + getCategoryBaseSQL() + ") t");
        sql.append("         group by");
        sql.append("              shop_id");
        sql.append("             ,slip_no");
        sql.append("             ,customer_id");
        sql.append("             ,target_time");
        sql.append("     ) t");
        sql.append(" group by");
        sql.append("     col_index");

        ResultSetWrapper rs = null;

        try {

            rs = SystemInfo.getConnection().executeQuery(sql.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    /**
     * --------------------------------------- 曜日別集計
     * ---------------------------------------
     */
    private void showWeekly() {

        SwingUtil.clearTable(weekly);
        DefaultTableModel model = (DefaultTableModel) weekly.getModel();

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate.getDate());
        Calendar calPrev = (Calendar) cal.clone();
        calPrev.add(Calendar.YEAR, -1);

        final int MAC_COL = weekly.getColumnCount();

        for (int i = 0; i < 7; i++) {
            model.addRow(new Object[MAC_COL - 1]);
            int row = model.getRowCount() - 1;
            weeklyFixed.setValueAt(fn_strWDay[(i == 6 ? -1 : i) + 1], row, 0);
        }

        // フッタ合計欄初期化
        SwingUtil.clearTable(weeklyTotal);
        DefaultTableModel modelTotal = (DefaultTableModel) weeklyTotal.getModel();
        modelTotal.addRow(new Object[weeklyTotal.getColumnCount() - 1]);
        modelTotal.addRow(new Object[weeklyTotal.getColumnCount() - 1]);

        // 時間帯集計
        try {
            ResultSetWrapper rs = getWeeklyData();
            while (rs.next()) {
                int row = rs.getInt("weekly_id") - 1;
                int col = rs.getInt("col_index");
                if (rdoOutValue.isSelected()) {
                    addValue(weekly, row, col, rs.getLong("sales_value"));
                } else {
                    addValue(weekly, row, col, rs.getLong("customer_count"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 営業日数
        int weeklyCount = 0;

        // 客数、日計
        try {
            ResultSetWrapper rs = getWeeklyCount();
            while (rs.next()) {
                int row = rs.getInt("weekly_id") - 1;
                int col = 0;

                if (rdoOutValue.isSelected()) {
                    // 時間帯集計の日計
                    col = MAC_COL - 10;
                    addValue(weekly, row, col, rs.getLong("sales_value"));

                    // 客数
                    col = MAC_COL - 8;
                    addValue(weekly, row, col, rs.getLong("customer_count"));

                    // 客単価
                    col = MAC_COL - 6;
                    weekly.setValueAt(Math.round(rs.getDouble("sales_value") / rs.getDouble("customer_count")), row, col);

                } else {

                    // 時間帯集計の日計
                    col = MAC_COL - 10;
                    addValue(weekly, row, col, rs.getLong("customer_count"));

                }

                // 客数が存在する場合は営業日のためNULL列をゼロ埋めする
                for (col = 0; col < MAC_COL; col++) {
                    addValue(weekly, row, col, 0);
                }
                weeklyCount++;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 前年実績
        try {
            ResultSetWrapper rs = getWeeklyDataPrev();
            while (rs.next()) {
                int row = rs.getInt("weekly_id") - 1;
                int col = MAC_COL - 4;
                if (rdoOutValue.isSelected()) {
                    addValue(weekly, row, col, rs.getLong("sales_value"));
                } else {
                    addValue(weekly, row, col, rs.getLong("customer_count"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 累計算出
        Long totalSales = 0l;
        Long totalCount = 0l;
        Long totalPrev = 0l;
        BigDecimal bd = null;

        for (int row = 0; row < weekly.getRowCount(); row++) {

            Long dayTotalSales = 0l;
            Long dayTotalCount = 0l;
            Long dayTotalPrev = 0l;

            // 時間帯集計
            int col = MAC_COL - 10;
            Long value = (Long) weekly.getValueAt(row, col);
            if (value != null) {
                dayTotalSales = value;
                totalSales += dayTotalSales;
                col = MAC_COL - 9;
                weekly.setValueAt(totalSales, row, col);
            }

            // 客数
            col = MAC_COL - 8;
            value = (Long) weekly.getValueAt(row, col);
            if (value != null) {
                dayTotalCount = value;
                totalCount += dayTotalCount;

                if (rdoOutValue.isSelected()) {
                    // 客数
                    col = MAC_COL - 7;
                    weekly.setValueAt(totalCount, row, col);

                    // 客単価
                    col = MAC_COL - 5;
                    weekly.setValueAt(Math.round(totalSales.doubleValue() / totalCount.doubleValue()), row, col);
                }
            }

            // 前年実績
            col = MAC_COL - 4;
            value = (Long) weekly.getValueAt(row, col);
            if (value != null) {
                dayTotalPrev = value;
                totalPrev += dayTotalPrev;
                col = MAC_COL - 3;
                weekly.setValueAt(totalPrev, row, col);
            }

            // 前年対比
            if (dayTotalPrev != 0) {
                col = MAC_COL - 2;
                bd = new BigDecimal(dayTotalSales.doubleValue() / dayTotalPrev * 100);
                weekly.setValueAt(bd.setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "%", row, col);
            }

            if (totalPrev != 0) {
                col = MAC_COL - 1;
                bd = new BigDecimal(totalSales.doubleValue() / totalPrev * 100);
                weekly.setValueAt(bd.setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "%", row, col);
            }

        }

        // フッタ合計
        for (int col = 0; col < MAC_COL; col++) {

            if (col == (MAC_COL - 9) || col == (MAC_COL - 7) || col == (MAC_COL - 3)) {

                weeklyTotal.setValueAt(weeklyTotal.getValueAt(0, col - 1), 0, col);
                weeklyTotal.setValueAt(weeklyTotal.getValueAt(1, col - 1), 1, col);

            } else if (col == (MAC_COL - 6) && rdoOutValue.isSelected()) {

                double d1 = Double.valueOf(weeklyTotal.getValueAt(0, MAC_COL - 10).toString());
                double d2 = Double.valueOf(weeklyTotal.getValueAt(0, MAC_COL - 8).toString());
                weeklyTotal.setValueAt(Math.round(d1 / d2), 0, col);
                weeklyTotal.setValueAt(Math.round(d1 / d2), 1, col);

            } else if (col == (MAC_COL - 5) && rdoOutValue.isSelected()) {

                double d1 = Double.valueOf(weeklyTotal.getValueAt(0, MAC_COL - 9).toString());
                double d2 = Double.valueOf(weeklyTotal.getValueAt(0, MAC_COL - 7).toString());
                weeklyTotal.setValueAt(Math.round(d1 / d2), 0, col);
                weeklyTotal.setValueAt(Math.round(d1 / d2), 1, col);

            } else if (col == (MAC_COL - 2)) {

                double v1 = Long.valueOf(weeklyTotal.getValueAt(0, MAC_COL - 10).toString());
                double v2 = Long.valueOf(weeklyTotal.getValueAt(0, MAC_COL - 4).toString());
                if (v2 != 0) {
                    bd = new BigDecimal(v1 / v2 * 100);
                    weeklyTotal.setValueAt(bd.setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "%", 0, col);
                }

                v1 = Long.valueOf(weeklyTotal.getValueAt(1, MAC_COL - 10).toString());
                v2 = Long.valueOf(weeklyTotal.getValueAt(1, MAC_COL - 4).toString());
                if (v2 != 0) {
                    bd = new BigDecimal(v1 / v2 * 100);
                    weeklyTotal.setValueAt(bd.setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "%", 1, col);
                }

            } else if (col == (MAC_COL - 1)) {

                double v1 = Long.valueOf(weeklyTotal.getValueAt(0, MAC_COL - 9).toString());
                double v2 = Long.valueOf(weeklyTotal.getValueAt(0, MAC_COL - 3).toString());
                if (v2 != 0) {
                    bd = new BigDecimal(v1 / v2 * 100);
                    weeklyTotal.setValueAt(bd.setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "%", 0, col);
                }

                v1 = Long.valueOf(weeklyTotal.getValueAt(1, MAC_COL - 9).toString());
                v2 = Long.valueOf(weeklyTotal.getValueAt(1, MAC_COL - 3).toString());
                if (v2 != 0) {
                    bd = new BigDecimal(v1 / v2 * 100);
                    weeklyTotal.setValueAt(bd.setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "%", 1, col);
                }

            } else if (col == (MAC_COL - 4)) {

                Long total = 0l;
                Long count = 0l;

                for (int row = 0; row < weekly.getRowCount(); row++) {
                    Long value = (Long) weekly.getValueAt(row, col);
                    if (value != null) {
                        total += value;
                        count++;
                    }
                }

                weeklyTotal.setValueAt(total, 0, col);
                weeklyTotal.setValueAt(Math.round(total.doubleValue() / count), 1, col);

            } else {

                Long total = 0l;
                for (int row = 0; row < weekly.getRowCount(); row++) {
                    Long value = (Long) weekly.getValueAt(row, col);
                    if (value != null) {
                        total += value;
                    }
                }

                weeklyTotal.setValueAt(total, 0, col);
                weeklyTotal.setValueAt(Math.round(total.doubleValue() / weeklyCount), 1, col);
            }
        }

        weeklyFixed.changeSelection(0, 0, false, false);
        weekly.changeSelection(0, 0, false, false);
    }

    private ResultSetWrapper getWeeklyData() {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      translate(date_part('dow', sales_date), 0, 7) as weekly_id");
        sql.append("     ,case");

        for (int i = 0; i < (listTime.size() - 1); i++) {
            sql.append("     when " + listTime.get(i) + " <= target_time and target_time < " + listTime.get(i + 1) + " then " + i);
        }

        sql.append("      end as col_index");
        sql.append("     ,sum(sales_value) as sales_value");

        sql.append("     ,count(");
        sql.append("         case when exists");
        sql.append("             (");
        sql.append("                 select 1");
        sql.append("                 from");
        sql.append("                     data_sales_detail");
        sql.append("                 where");
        sql.append("                         shop_id = t.shop_id");
        sql.append("                     and slip_no = t.slip_no");
        sql.append("                     and delete_date is null");
        sql.append("                     and product_division not in (3)");
        sql.append("             ) then slip_no end");
        sql.append("      ) as customer_count");

        sql.append(" from");
        sql.append("     (");
        sql.append("         select");
        sql.append("              ds.shop_id");
        sql.append("             ,ds.slip_no");
        sql.append("             ,ds.sales_date");

        if (rdoVisitTime.isSelected()) {
            sql.append("         ," + getTargetTimeColumn("dr.visit_time"));
        } else if (rdoStartTime.isSelected()) {
            sql.append("         ," + getTargetTimeColumn("dr.start_time"));
        } else {
            sql.append("         ," + getTargetTimeColumn("dr.leave_time"));
        }

        if (rdoTaxUnit.isSelected()) {
            sql.append("         ,ds.discount_sales_value_in_tax as sales_value");
        } else {
            sql.append("         ,ds.discount_sales_value_no_tax as sales_value");
        }

        sql.append("         from");
        sql.append("             view_data_sales_valid ds");
        sql.append("                 join data_reservation dr");
        sql.append("                     using(shop_id, slip_no)");
        sql.append("                 join mst_shop ms");
        sql.append("                     using(shop_id)");
        sql.append("         where");
        sql.append("                 ds.shop_id in (" + this.shopIdList + ")");
        sql.append("             and ds.sales_date between " + SQLUtil.convertForSQLDateOnly(startDate.getDate()));
        sql.append("                                   and " + SQLUtil.convertForSQLDateOnly(endDate.getDate()));
        sql.append("     ) t");
        sql.append(" group by");
        sql.append("      weekly_id");
        sql.append("     ,col_index");
        sql.append(" order by");
        sql.append("      weekly_id");

        ResultSetWrapper rs = null;

        try {

            rs = SystemInfo.getConnection().executeQuery(sql.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    private ResultSetWrapper getWeeklyCount() {

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      weekly_id");
        sql.append("     ,sum(customer_count) as customer_count");
        sql.append("     ,sum(sales_value) as sales_value");
        sql.append(" from ");
        sql.append(" (");

        sql.append(" select");
        sql.append("      translate(date_part('dow', sales_date), 0, 7) as weekly_id");

        // 客数にはクレームのみ客を含めない
        sql.append("     ,count(");
        sql.append("         case when exists");
        sql.append("             (");
        sql.append("                 select 1");
        sql.append("                 from");
        sql.append("                     data_sales_detail");
        sql.append("                 where");
        sql.append("                         shop_id = ds.shop_id");
        sql.append("                     and slip_no = ds.slip_no");
        sql.append("                     and delete_date is null");
        sql.append("                     and product_division not in (3)");
        sql.append("             ) then slip_no end");
        sql.append("      ) as customer_count");

        if (rdoTaxUnit.isSelected()) {
            sql.append(" ,sum(discount_sales_value_in_tax) as sales_value");
        } else {
            sql.append(" ,sum(discount_sales_value_no_tax) as sales_value");
        }

        sql.append(" from");
        sql.append("     view_data_sales_valid ds");
        sql.append(" where");
        sql.append("         shop_id in (" + this.shopIdList + ")");
        sql.append("     and sales_date between " + SQLUtil.convertForSQLDateOnly(startDate.getDate()));
        sql.append("                        and " + SQLUtil.convertForSQLDateOnly(endDate.getDate()));
        sql.append(" group by");
        sql.append("     weekly_id");

        // 売上なしのレジ締め日取得
        sql.append(" union all");
        sql.append(" select distinct");
        sql.append("      translate(date_part('dow', manage_date), 0, 7) as weekly_id");
        sql.append("     ,0");
        sql.append("     ,0");
        sql.append(" from");
        sql.append("     data_register dr");
        sql.append(" where");
        sql.append("         delete_date is null");
        sql.append("     and shop_id in (" + this.shopIdList + ")");
        sql.append("     and manage_date between " + SQLUtil.convertForSQLDateOnly(startDate.getDate()));
        sql.append("                         and " + SQLUtil.convertForSQLDateOnly(endDate.getDate()));
        sql.append("     and not exists");
        sql.append("         (");
        sql.append("             select 1");
        sql.append("             from");
        sql.append("                 data_sales");
        sql.append("             where");
        sql.append("                     delete_date is null");
        sql.append("                 and shop_id = dr.shop_id");
        sql.append("                 and sales_date = dr.manage_date");
        sql.append("         )");
        sql.append(" group by");
        sql.append("     weekly_id");

        sql.append(" ) t");
        sql.append(" group by");
        sql.append("     weekly_id");

        ResultSetWrapper rs = null;

        try {

            rs = SystemInfo.getConnection().executeQuery(sql.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    private ResultSetWrapper getWeeklyDataPrev() {

        Calendar calStart = Calendar.getInstance();
        calStart.setTime(startDate.getDate());
        calStart.add(Calendar.YEAR, -1);
        Calendar calEnd = (Calendar) calStart.clone();
        calEnd.set(Calendar.DAY_OF_MONTH, calEnd.getActualMaximum(Calendar.DAY_OF_MONTH));

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      translate(date_part('dow', sales_date), 0, 7) as weekly_id");

        if (rdoTaxUnit.isSelected()) {
            sql.append(" ,sum(discount_sales_value_in_tax) as sales_value");
        } else {
            sql.append(" ,sum(discount_sales_value_no_tax) as sales_value");
        }

        sql.append("     ,count(");
        sql.append("         case when exists");
        sql.append("             (");
        sql.append("                 select 1");
        sql.append("                 from");
        sql.append("                     data_sales_detail");
        sql.append("                 where");
        sql.append("                         shop_id = ds.shop_id");
        sql.append("                     and slip_no = ds.slip_no");
        sql.append("                     and delete_date is null");
        sql.append("                     and product_division not in (3)");
        sql.append("             ) then slip_no end");
        sql.append("      ) as customer_count");

        sql.append(" from");
        sql.append("     view_data_sales_valid ds");
        sql.append(" where");
        sql.append("         shop_id in (" + this.shopIdList + ")");
        sql.append("     and sales_date between " + SQLUtil.convertForSQLDateOnly(calStart));
        sql.append("                        and " + SQLUtil.convertForSQLDateOnly(calEnd));
        sql.append(" group by");
        sql.append("     weekly_id");
        sql.append(" order by");
        sql.append("     weekly_id");

        ResultSetWrapper rs = null;

        try {

            rs = SystemInfo.getConnection().executeQuery(sql.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    private void addValue(JTableEx table, int row, int col, long value) {
        Long baseValue = (Long) table.getValueAt(row, col);
        if (baseValue == null) {
            baseValue = 0l;
        }
        table.setValueAt(baseValue + value, row, col);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        taxGroup = new javax.swing.ButtonGroup();
        techItemGroup = new javax.swing.ButtonGroup();
        timeUnitGroup = new javax.swing.ButtonGroup();
        timeGroup = new javax.swing.ButtonGroup();
        outGroup = new javax.swing.ButtonGroup();
        pnlMain = new javax.swing.JPanel();
        targetLabel = new javax.swing.JLabel();
        shop = new com.geobeck.sosia.pos.swing.JComboBoxLabel();
        techItemClassLabel = new javax.swing.JLabel();
        lblTargetPeriod = new javax.swing.JLabel();
        startDate = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        jLabel1 = new javax.swing.JLabel();
        endDate = new jp.co.flatsoft.fscomponent.FSCalenderCombo();
        targetLabel2 = new javax.swing.JLabel();
        rdoVisitTime = new javax.swing.JRadioButton();
        rdoStartTime = new javax.swing.JRadioButton();
        rdoLeaveTime = new javax.swing.JRadioButton();
        lblOutput = new javax.swing.JLabel();
        rdoTaxUnit = new javax.swing.JRadioButton();
        rdoTaxBlank = new javax.swing.JRadioButton();
        lblTax1 = new javax.swing.JLabel();
        rdoTimeFixed = new javax.swing.JRadioButton();
        rdoTimeCustom = new javax.swing.JRadioButton();
        btnTimeSetting = new javax.swing.JButton();
        showButton = new javax.swing.JButton();
        btnOutput = new javax.swing.JButton();
        rdoClassAll = new javax.swing.JRadioButton();
        rdoClassTech = new javax.swing.JRadioButton();
        rdoClassItem = new javax.swing.JRadioButton();
        lblTax = new javax.swing.JLabel();
        rdoOutValue = new javax.swing.JRadioButton();
        rdoOutCount = new javax.swing.JRadioButton();
        closeButton = new javax.swing.JButton();
        timeTab = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        dailyScroll = new javax.swing.JScrollPane();
        daily = new JTableEx() {
            protected JTableHeader createDefaultTableHeader() {
                return new GroupableTableHeader(columnModel);
            }
        };
        ;
        dailyTotalScroll = new JScrollPane() {
            public void setColumnHeaderView(Component view) {
            }
        };
        dailyTotal = new JTableEx() {
            protected JTableHeader createDefaultTableHeader() {
                return new GroupableTableHeader(columnModel);
            }
        };
        ;
        targetLabel1 = new javax.swing.JLabel();
        targetLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        categoryScroll = new javax.swing.JScrollPane();
        category = new JTableEx() {
            protected JTableHeader createDefaultTableHeader() {
                return new GroupableTableHeader(columnModel);
            }
        };
        ;
        categoryTotalScroll = new JScrollPane() {
            public void setColumnHeaderView(Component view) {
            }
        };
        categoryTotal = new JTableEx() {
            protected JTableHeader createDefaultTableHeader() {
                return new GroupableTableHeader(columnModel);
            }
        };
        ;
        targetLabel4 = new javax.swing.JLabel();
        targetLabel5 = new javax.swing.JLabel();
        targetLabel6 = new javax.swing.JLabel();
        targetLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        weeklyScroll = new javax.swing.JScrollPane();
        weekly = new JTableEx() {
            protected JTableHeader createDefaultTableHeader() {
                return new GroupableTableHeader(columnModel);
            }
        };
        ;
        weeklyTotalScroll = new JScrollPane() {
            public void setColumnHeaderView(Component view) {
            }
        };
        weeklyTotal = new JTableEx() {
            protected JTableHeader createDefaultTableHeader() {
                return new GroupableTableHeader(columnModel);
            }
        };
        ;
        targetLabel8 = new javax.swing.JLabel();
        targetLabel9 = new javax.swing.JLabel();

        setFocusCycleRoot(true);

        pnlMain.setFocusCycleRoot(true);
        pnlMain.setOpaque(false);
        pnlMain.setLayout(null);

        targetLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        targetLabel.setText("対象店舗");
        targetLabel.setFocusCycleRoot(true);
        pnlMain.add(targetLabel);
        targetLabel.setBounds(10, 10, 50, 20);

        shop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shopActionPerformed(evt);
            }
        });
        pnlMain.add(shop);
        shop.setBounds(70, 10, 137, 20);

        techItemClassLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        techItemClassLabel.setText("分類種別");
        techItemClassLabel.setFocusCycleRoot(true);
        pnlMain.add(techItemClassLabel);
        techItemClassLabel.setBounds(10, 65, 50, 20);

        lblTargetPeriod.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTargetPeriod.setText("対象期間");
        lblTargetPeriod.setFocusCycleRoot(true);
        pnlMain.add(lblTargetPeriod);
        lblTargetPeriod.setBounds(10, 40, 50, 20);

        startDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        startDate.setFocusCycleRoot(true);
        startDate.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                startDateItemStateChanged(evt);
            }
        });
        startDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                startDateFocusGained(evt);
            }
        });
        pnlMain.add(startDate);
        startDate.setBounds(70, 40, 88, 20);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("～");
        jLabel1.setFocusCycleRoot(true);
        pnlMain.add(jLabel1);
        jLabel1.setBounds(160, 40, 20, 20);

        endDate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        endDate.setFocusCycleRoot(true);
        endDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                endDateFocusGained(evt);
            }
        });
        pnlMain.add(endDate);
        endDate.setBounds(180, 40, 88, 20);

        targetLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        targetLabel2.setText("集計方法");
        targetLabel2.setFocusCycleRoot(true);
        pnlMain.add(targetLabel2);
        targetLabel2.setBounds(300, 10, 50, 20);

        timeGroup.add(rdoVisitTime);
        rdoVisitTime.setSelected(true);
        rdoVisitTime.setText("受付時間");
        rdoVisitTime.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoVisitTime.setFocusCycleRoot(true);
        rdoVisitTime.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoVisitTime.setOpaque(false);
        pnlMain.add(rdoVisitTime);
        rdoVisitTime.setBounds(360, 10, 70, 20);

        timeGroup.add(rdoStartTime);
        rdoStartTime.setText("施術開始時間");
        rdoStartTime.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoStartTime.setFocusCycleRoot(true);
        rdoStartTime.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoStartTime.setOpaque(false);
        pnlMain.add(rdoStartTime);
        rdoStartTime.setBounds(440, 10, 90, 20);

        timeGroup.add(rdoLeaveTime);
        rdoLeaveTime.setText("精算時間");
        rdoLeaveTime.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoLeaveTime.setFocusCycleRoot(true);
        rdoLeaveTime.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoLeaveTime.setOpaque(false);
        pnlMain.add(rdoLeaveTime);
        rdoLeaveTime.setBounds(540, 10, 80, 20);

        lblOutput.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOutput.setText("出力条件");
        lblOutput.setFocusCycleRoot(true);
        pnlMain.add(lblOutput);
        lblOutput.setBounds(300, 56, 50, 20);

        taxGroup.add(rdoTaxUnit);
        rdoTaxUnit.setSelected(true);
        rdoTaxUnit.setText("税込");
        rdoTaxUnit.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTaxUnit.setFocusCycleRoot(true);
        rdoTaxUnit.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTaxUnit.setOpaque(false);
        pnlMain.add(rdoTaxUnit);
        rdoTaxUnit.setBounds(360, 80, 70, 20);

        taxGroup.add(rdoTaxBlank);
        rdoTaxBlank.setText("税抜");
        rdoTaxBlank.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTaxBlank.setFocusCycleRoot(true);
        rdoTaxBlank.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTaxBlank.setOpaque(false);
        pnlMain.add(rdoTaxBlank);
        rdoTaxBlank.setBounds(440, 80, 80, 20);

        lblTax1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTax1.setText("時間条件");
        lblTax1.setFocusCycleRoot(true);
        pnlMain.add(lblTax1);
        lblTax1.setBounds(300, 33, 50, 20);

        timeUnitGroup.add(rdoTimeFixed);
        rdoTimeFixed.setSelected(true);
        rdoTimeFixed.setText("1時間毎");
        rdoTimeFixed.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTimeFixed.setFocusCycleRoot(true);
        rdoTimeFixed.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTimeFixed.setOpaque(false);
        pnlMain.add(rdoTimeFixed);
        rdoTimeFixed.setBounds(360, 33, 70, 20);

        timeUnitGroup.add(rdoTimeCustom);
        rdoTimeCustom.setText("個別設定");
        rdoTimeCustom.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoTimeCustom.setFocusCycleRoot(true);
        rdoTimeCustom.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoTimeCustom.setOpaque(false);
        pnlMain.add(rdoTimeCustom);
        rdoTimeCustom.setBounds(440, 33, 70, 20);

        btnTimeSetting.setIcon(SystemInfo.getImageIcon("/button/common/time_set_off.jpg"));
        btnTimeSetting.setBorderPainted(false);
        btnTimeSetting.setFocusCycleRoot(true);
        btnTimeSetting.setPressedIcon(SystemInfo.getImageIcon("/button/common/time_set_on.jpg"));
        btnTimeSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimeSettingActionPerformed(evt);
            }
        });
        pnlMain.add(btnTimeSetting);
        btnTimeSetting.setBounds(690, 40, 92, 25);

        showButton.setIcon(SystemInfo.getImageIcon("/button/common/show_off.jpg"));
        showButton.setBorderPainted(false);
        showButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/show_on.jpg"));
        showButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showButtonActionPerformed(evt);
            }
        });
        pnlMain.add(showButton);
        showButton.setBounds(580, 70, 92, 25);

        btnOutput.setIcon(SystemInfo.getImageIcon("/button/print/excel_off.jpg"));
        btnOutput.setBorderPainted(false);
        btnOutput.setFocusCycleRoot(true);
        btnOutput.setPressedIcon(SystemInfo.getImageIcon("/button/print/excel_on.jpg"));
        btnOutput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOutputActionPerformed(evt);
            }
        });
        pnlMain.add(btnOutput);
        btnOutput.setBounds(690, 70, 92, 25);

        techItemGroup.add(rdoClassAll);
        rdoClassAll.setSelected(true);
        rdoClassAll.setText("すべて");
        rdoClassAll.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoClassAll.setFocusCycleRoot(true);
        rdoClassAll.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoClassAll.setOpaque(false);
        pnlMain.add(rdoClassAll);
        rdoClassAll.setBounds(70, 65, 50, 20);

        techItemGroup.add(rdoClassTech);
        rdoClassTech.setText("技術分類");
        rdoClassTech.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoClassTech.setFocusCycleRoot(true);
        rdoClassTech.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoClassTech.setOpaque(false);
        pnlMain.add(rdoClassTech);
        rdoClassTech.setBounds(130, 65, 70, 20);

        techItemGroup.add(rdoClassItem);
        rdoClassItem.setText("商品分類");
        rdoClassItem.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoClassItem.setFocusCycleRoot(true);
        rdoClassItem.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoClassItem.setOpaque(false);
        pnlMain.add(rdoClassItem);
        rdoClassItem.setBounds(210, 65, 70, 20);

        lblTax.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTax.setText("税区分");
        lblTax.setFocusCycleRoot(true);
        pnlMain.add(lblTax);
        lblTax.setBounds(300, 80, 50, 20);

        outGroup.add(rdoOutValue);
        rdoOutValue.setSelected(true);
        rdoOutValue.setText("金額");
        rdoOutValue.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoOutValue.setFocusCycleRoot(true);
        rdoOutValue.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoOutValue.setOpaque(false);
        pnlMain.add(rdoOutValue);
        rdoOutValue.setBounds(360, 56, 70, 20);

        outGroup.add(rdoOutCount);
        rdoOutCount.setText("人数");
        rdoOutCount.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        rdoOutCount.setFocusCycleRoot(true);
        rdoOutCount.setMargin(new java.awt.Insets(0, 0, 0, 0));
        rdoOutCount.setOpaque(false);
        pnlMain.add(rdoOutCount);
        rdoOutCount.setBounds(440, 56, 80, 20);

        closeButton.setIcon(SystemInfo.getImageIcon("/button/common/close_off.jpg"));
        closeButton.setBorderPainted(false);
        closeButton.setPressedIcon(SystemInfo.getImageIcon("/button/common/close_on.jpg"));
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        pnlMain.add(closeButton);
        closeButton.setBounds(690, 10, 92, 25);

        timeTab.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        timeTab.setPreferredSize(new java.awt.Dimension(457, 429));
        timeTab.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                timeTabStateChanged(evt);
            }
        });

        jPanel1.setOpaque(false);

        dailyScroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        dailyScroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        dailyScroll.setPreferredSize(new java.awt.Dimension(457, 402));

        daily.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        daily.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        daily.setSelectionBackground(new java.awt.Color(220, 220, 220));
        daily.setSelectionForeground(new java.awt.Color(0, 0, 0));
        dailyScroll.setViewportView(daily);

        dailyTotalScroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        dailyTotalScroll.setPreferredSize(new java.awt.Dimension(457, 402));

        dailyTotal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        dailyTotal.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        dailyTotal.setSelectionBackground(new java.awt.Color(220, 220, 220));
        dailyTotal.setSelectionForeground(new java.awt.Color(0, 0, 0));
        dailyTotalScroll.setViewportView(dailyTotal);

        targetLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        targetLabel1.setText("合計");
        targetLabel1.setFocusCycleRoot(true);

        targetLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        targetLabel3.setText("平均");
        targetLabel3.setFocusCycleRoot(true);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(95, 95, 95)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(targetLabel1)
                    .add(targetLabel3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(dailyTotalScroll, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 649, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(17, 17, 17))
            .add(dailyScroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(dailyScroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(dailyTotalScroll, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(targetLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, 0)
                        .add(targetLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        timeTab.addTab("　日別集計　", jPanel1);

        jPanel2.setOpaque(false);

        categoryScroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        categoryScroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        categoryScroll.setPreferredSize(new java.awt.Dimension(457, 402));

        category.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        category.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        category.setSelectionBackground(new java.awt.Color(220, 220, 220));
        category.setSelectionForeground(new java.awt.Color(0, 0, 0));
        categoryScroll.setViewportView(category);

        categoryTotalScroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        categoryTotalScroll.setPreferredSize(new java.awt.Dimension(457, 402));

        categoryTotal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        categoryTotal.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        categoryTotal.setSelectionBackground(new java.awt.Color(220, 220, 220));
        categoryTotal.setSelectionForeground(new java.awt.Color(0, 0, 0));
        categoryTotalScroll.setViewportView(categoryTotal);

        targetLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        targetLabel4.setText("割引");
        targetLabel4.setFocusCycleRoot(true);

        targetLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        targetLabel5.setText("合計");
        targetLabel5.setFocusCycleRoot(true);

        targetLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        targetLabel6.setText("客数");
        targetLabel6.setFocusCycleRoot(true);

        targetLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        targetLabel7.setText("客単価");
        targetLabel7.setFocusCycleRoot(true);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .add(45, 45, 45)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(targetLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(targetLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(targetLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(targetLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(categoryTotalScroll, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 669, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(17, 17, 17))
            .add(categoryScroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .add(categoryScroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(categoryTotalScroll, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 103, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(targetLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, 0)
                        .add(targetLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, 0)
                        .add(targetLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, 0)
                        .add(targetLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        timeTab.addTab("　分類別集計　", jPanel2);

        jPanel3.setOpaque(false);

        weeklyScroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        weeklyScroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        weeklyScroll.setPreferredSize(new java.awt.Dimension(457, 402));

        weekly.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        weekly.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        weekly.setSelectionBackground(new java.awt.Color(220, 220, 220));
        weekly.setSelectionForeground(new java.awt.Color(0, 0, 0));
        weeklyScroll.setViewportView(weekly);

        weeklyTotalScroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        weeklyTotalScroll.setPreferredSize(new java.awt.Dimension(457, 402));

        weeklyTotal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        weeklyTotal.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        weeklyTotal.setSelectionBackground(new java.awt.Color(220, 220, 220));
        weeklyTotal.setSelectionForeground(new java.awt.Color(0, 0, 0));
        weeklyTotalScroll.setViewportView(weeklyTotal);

        targetLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        targetLabel8.setText("合計");
        targetLabel8.setFocusCycleRoot(true);

        targetLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        targetLabel9.setText("平均");
        targetLabel9.setFocusCycleRoot(true);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(targetLabel8)
                    .add(targetLabel9))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(weeklyTotalScroll, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 732, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(17, 17, 17))
            .add(weeklyScroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .add(weeklyScroll, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(targetLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, 0)
                        .add(targetLabel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(weeklyTotalScroll, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        timeTab.addTab("　曜日別集計　", jPanel3);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, pnlMain, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 795, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, timeTab, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 795, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(pnlMain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, 0)
                .add(timeTab, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnOutputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOutputActionPerformed

        btnOutput.setCursor(null);

        try {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            if (timeTab.getSelectedIndex() == 0) {

                if (daily.getRowCount() > 0) {

                    this.printDaily();

                } else {

                    MessageDialog.showMessageDialog(
                            this,
                            MessageUtil.getMessage(4001),
                            this.getTitle() + " - 日別集計",
                            JOptionPane.ERROR_MESSAGE);
                }

            } else if (timeTab.getSelectedIndex() == 1) {

                if (category.getRowCount() > 0) {

                    this.printCategory();

                } else {

                    MessageDialog.showMessageDialog(
                            this,
                            MessageUtil.getMessage(4001),
                            this.getTitle() + " - 分類別集計",
                            JOptionPane.ERROR_MESSAGE);
                }

            } else if (timeTab.getSelectedIndex() == 2) {

                if (weekly.getRowCount() > 0) {

                    this.printWeekly();

                } else {

                    MessageDialog.showMessageDialog(
                            this,
                            MessageUtil.getMessage(4001),
                            this.getTitle() + " - 曜日別集計",
                            JOptionPane.ERROR_MESSAGE);
                }

            }

        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }//GEN-LAST:event_btnOutputActionPerformed

    private void startDateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_startDateItemStateChanged
        this.setInitDate();
    }//GEN-LAST:event_startDateItemStateChanged

    private void timeTabStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_timeTabStateChanged
       
            boolean mode = (timeTab.getSelectedIndex() == 1);
            techItemClassLabel.setEnabled(mode);
            rdoClassAll.setEnabled(mode);
            rdoClassTech.setEnabled(mode);
            rdoClassItem.setEnabled(mode);
            lblOutput.setEnabled(!mode);
            rdoOutValue.setEnabled(!mode);
            rdoOutCount.setEnabled(!mode);

            endDate.setEditable(mode || timeTab.getSelectedIndex() == 2);

            this.setInitDate();
        
    }//GEN-LAST:event_timeTabStateChanged

    private void btnTimeSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimeSettingActionPerformed

        TimeAnalysisSettingPanel p = new TimeAnalysisSettingPanel(shop.getSelectedItem());
        SwingUtil.openAnchorDialog(null, true, p, "時間設定", SwingUtil.ANCHOR_HCENTER | SwingUtil.ANCHOR_VCENTER);
        p = null;
        System.gc();

    }//GEN-LAST:event_btnTimeSettingActionPerformed

    private void startDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_startDateFocusGained
        startDate.getInputContext().setCharacterSubsets(null);
    }//GEN-LAST:event_startDateFocusGained

    private void endDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_endDateFocusGained
        endDate.getInputContext().setCharacterSubsets(null);
    }//GEN-LAST:event_endDateFocusGained

    private void showButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showButtonActionPerformed

        autoShow();

    }//GEN-LAST:event_showButtonActionPerformed
    public void autoShow()
    {
        
           showButton.setCursor(null);

        try {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            MstShopSetting mss = MstShopSetting.getInstance();
            if (rdoVisitTime.isSelected()) {
                mss.setTimeAnalysisCondition1(0);
            }
            if (rdoStartTime.isSelected()) {
                mss.setTimeAnalysisCondition1(1);
            }
            if (rdoLeaveTime.isSelected()) {
                mss.setTimeAnalysisCondition1(2);
            }
            if (rdoTimeFixed.isSelected()) {
                mss.setTimeAnalysisCondition2(0);
            }
            if (rdoTimeCustom.isSelected()) {
                mss.setTimeAnalysisCondition2(1);
            }
            mss.regist();

            if (timeTab.getSelectedIndex() == 0) {
                this.initDailyHeader();
                if (rdoTimeCustom.isSelected() && listTime.size() < 1) {
                    MessageDialog.showMessageDialog(
                            this,
                            "時間設定が登録されていません",
                            this.getTitle(),
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                this.showDaily();

            } else if (timeTab.getSelectedIndex() == 1) {
                this.initCategoryHeader();
                if (rdoTimeCustom.isSelected() && listTime.size() < 1) {
                    MessageDialog.showMessageDialog(
                            this,
                            "時間設定が登録されていません",
                            this.getTitle(),
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                this.showCategory();

            } else if (timeTab.getSelectedIndex() == 2) {
                this.initWeeklyHeader();
                if (rdoTimeCustom.isSelected() && listTime.size() < 1) {
                    MessageDialog.showMessageDialog(
                            this,
                            "時間設定が登録されていません",
                            this.getTitle(),
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                this.showWeekly();
            }

        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
    private void shopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shopActionPerformed

        if (shop.getSelectedItem() instanceof MstGroup) {
            //グループ
            MstGroup mg = (MstGroup) shop.getSelectedItem();
            this.shopIdList = mg.getShopIDListAll();
            this.shopName = mg.getGroupName();
        } else {
            // 店舗
            MstShop ms = (MstShop) shop.getSelectedItem();
            this.shopIdList = ms.getShopID().toString();
            this.shopName = ms.getShopName();
        }

    }//GEN-LAST:event_shopActionPerformed

    public void dispose() {
        if (this.isDialog()) {
            ((JDialog) this.getParent().getParent().getParent().getParent()).dispose();
        }
    }
    
    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_closeButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOutput;
    private javax.swing.JButton btnTimeSetting;
    private com.geobeck.swing.JTableEx category;
    private javax.swing.JScrollPane categoryScroll;
    private com.geobeck.swing.JTableEx categoryTotal;
    private javax.swing.JScrollPane categoryTotalScroll;
    private javax.swing.JButton closeButton;
    private com.geobeck.swing.JTableEx daily;
    private javax.swing.JScrollPane dailyScroll;
    private com.geobeck.swing.JTableEx dailyTotal;
    private javax.swing.JScrollPane dailyTotalScroll;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo endDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblOutput;
    private javax.swing.JLabel lblTargetPeriod;
    private javax.swing.JLabel lblTax;
    private javax.swing.JLabel lblTax1;
    private javax.swing.ButtonGroup outGroup;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JRadioButton rdoClassAll;
    private javax.swing.JRadioButton rdoClassItem;
    private javax.swing.JRadioButton rdoClassTech;
    private javax.swing.JRadioButton rdoLeaveTime;
    private javax.swing.JRadioButton rdoOutCount;
    private javax.swing.JRadioButton rdoOutValue;
    private javax.swing.JRadioButton rdoStartTime;
    private javax.swing.JRadioButton rdoTaxBlank;
    private javax.swing.JRadioButton rdoTaxUnit;
    private javax.swing.JRadioButton rdoTimeCustom;
    private javax.swing.JRadioButton rdoTimeFixed;
    private javax.swing.JRadioButton rdoVisitTime;
    private com.geobeck.sosia.pos.swing.JComboBoxLabel shop;
    private javax.swing.JButton showButton;
    private jp.co.flatsoft.fscomponent.FSCalenderCombo startDate;
    private javax.swing.JLabel targetLabel;
    private javax.swing.JLabel targetLabel1;
    private javax.swing.JLabel targetLabel2;
    private javax.swing.JLabel targetLabel3;
    private javax.swing.JLabel targetLabel4;
    private javax.swing.JLabel targetLabel5;
    private javax.swing.JLabel targetLabel6;
    private javax.swing.JLabel targetLabel7;
    private javax.swing.JLabel targetLabel8;
    private javax.swing.JLabel targetLabel9;
    private javax.swing.ButtonGroup taxGroup;
    private javax.swing.JLabel techItemClassLabel;
    private javax.swing.ButtonGroup techItemGroup;
    private javax.swing.ButtonGroup timeGroup;
    private javax.swing.JTabbedPane timeTab;
    private javax.swing.ButtonGroup timeUnitGroup;
    private com.geobeck.swing.JTableEx weekly;
    private javax.swing.JScrollPane weeklyScroll;
    private com.geobeck.swing.JTableEx weeklyTotal;
    private javax.swing.JScrollPane weeklyTotalScroll;
    // End of variables declaration//GEN-END:variables

    /**
     * ボタンにマウスカーソルが乗ったときにカーソルを変更するイベントを追加する。
     */
    private void addMouseCursorChange() {
        SystemInfo.addMouseCursorChange(showButton);
        SystemInfo.addMouseCursorChange(btnOutput);
        SystemInfo.addMouseCursorChange(btnTimeSetting);
    }

    private void setKeyListener() {
        endDate.addKeyListener(SystemInfo.getMoveNextField());
        endDate.addFocusListener(SystemInfo.getSelectText());
        startDate.addKeyListener(SystemInfo.getMoveNextField());
        startDate.addFocusListener(SystemInfo.getSelectText());
        rdoTaxBlank.addKeyListener(SystemInfo.getMoveNextField());
        rdoTaxUnit.addKeyListener(SystemInfo.getMoveNextField());
    }

    private void setInitDate() {

        if (timeTab.getSelectedIndex() == 1) {
            return;
        }
        if (startDate.getDate() == null) {
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate.getDate());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        startDate.setDate(cal.getTime());
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        endDate.setDate(cal.getTime());
    }

    private List<String> getTimeArray() {

        DecimalFormat df = new DecimalFormat("00");
        List<String> list = new ArrayList<String>();

        if (rdoTimeFixed.isSelected()) {

            int openH = 0;
            int closeH = 0;

            if (shop.getSelectedItem() instanceof MstGroup) {

                StringBuilder sql = new StringBuilder(1000);
                sql.append(" select");
                sql.append("      min(open_hour) as open_hour");
                sql.append("     ,max(close_hour + (case when close_minute > 0 then 1 else 0 end)) as close_hour");
                sql.append(" from");
                sql.append("     mst_shop");
                sql.append(" where");
                sql.append("     delete_date is null");

                try {
                    ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
                    if (rs.next()) {
                        openH = rs.getInt("open_hour");
                        closeH = rs.getInt("close_hour");
                    }
                    rs.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                MstShop ms = (MstShop) shop.getSelectedItem();
                openH = ms.getOpenHour().intValue();
                closeH = ms.getCloseHour().intValue();
                if (ms.getCloseMinute().intValue() > 0) {
                    closeH++;
                }
            }

            list.add("-" + df.format(openH));
            for (int i = openH; i < closeH; i++) {
                list.add(df.format(i) + "-" + df.format(i + 1));
            }
            list.add(df.format(closeH) + "-");

            listTime.clear();
            listTime.add(0);
            listTime.add(openH);
            for (int i = openH; i < closeH; i++) {
                listTime.add((i + 1));
            }
            listTime.add(9999);

        } else {

            MstTimeSettings shiftsAllInShop = new MstTimeSettings();
            ConnectionWrapper con;

            try {
                con = SystemInfo.getConnection();

                int shopId = 0;
                if (shop.getSelectedItem() instanceof MstShop) {
                    shopId = ((MstShop) shop.getSelectedItem()).getShopID();
                }

                shiftsAllInShop.setShopId(shopId);
                shiftsAllInShop.load(con, false);

                listTime.clear();
                String strLastTime = "";

                for (MstTimeSetting eachshift : shiftsAllInShop) {

                    String strShiftName = eachshift.getTimeName();

                    if (strShiftName != null && strShiftName.length() > 0) {

                        int iShift = strShiftName.charAt(0) - 'A';

                        if (iShift >= 0) {

                            String strStartTime = eachshift.getStartTime();
                            String strEndTime = eachshift.getEndTime();

                            boolean isTime = true;
                            isTime = isTime && strStartTime != null;
                            isTime = isTime && CheckUtil.isNumber(strStartTime);
                            isTime = isTime && strEndTime != null;
                            isTime = isTime && CheckUtil.isNumber(strEndTime);
                            isTime = isTime && !strStartTime.equals("00");
                            isTime = isTime && !strEndTime.equals("00");

                            if (isTime) {
                                String s =
                                        String.format("%c(%s-%s)", strShiftName.charAt(0), strStartTime, strEndTime);

                                list.add(s);
                                listTime.add(Integer.valueOf(strStartTime));
                                strLastTime = strEndTime;
                            }
                        }
                    }
                }

                if (strLastTime.length() > 0) {
                    listTime.add(Integer.valueOf(strLastTime));
                }

                con.close();

            } catch (SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

        }

        return list;
    }

    /**
     * ********************************************
     */
    // 日別集計の初期化処理
    /**
     * ********************************************
     */
    private void initDailyHeader() {

        List<String> list = this.getDailyHeaderList();

        daily.setModel(new DefaultTableModel(
                new Object[][]{}, (String[]) list.toArray(new String[0])) {
            public Class getColumnClass(int columnIndex) {
                return String.class;
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });

        list.remove(0);
        list.remove(0);
        list.remove(0);

        dailyTotal.setModel(new DefaultTableModel(
                new Object[][]{}, (String[]) list.toArray(new String[0])) {
            public Class getColumnClass(int columnIndex) {
                return String.class;
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });

        dailyTotal.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        dailyTotal.setSelectionBackground(new java.awt.Color(255, 210, 142));
        dailyTotal.setSelectionForeground(new java.awt.Color(0, 0, 0));
        dailyTotal.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dailyTotal.getTableHeader().setReorderingAllowed(false);
        dailyTotal.getTableHeader().setResizingAllowed(false);
        dailyTotal.setDefaultRenderer(String.class, new TimeTableCellRenderer());
        SwingUtil.setJTableHeaderRenderer(dailyTotal, SystemInfo.getTableHeaderRenderer());
        dailyTotal.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT - 5);
        this.setDailyTableColumnWidth(dailyTotal, 0);

        daily.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        daily.setSelectionBackground(new java.awt.Color(255, 210, 142));
        daily.setSelectionForeground(new java.awt.Color(0, 0, 0));
        daily.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        daily.getTableHeader().setReorderingAllowed(false);
        daily.getTableHeader().setResizingAllowed(false);
        daily.setDefaultRenderer(String.class, new TimeTableCellRenderer());
        SwingUtil.setJTableHeaderRenderer(daily, SystemInfo.getTableHeaderRenderer());
        daily.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT - 5);
        this.setDailyTableColumnWidth(daily, 3);

        this.initDailyFixedTable();

        GroupableTableHeader header = (GroupableTableHeader) daily.getTableHeader();
        header.setBackground(new Color(204, 204, 204));
        TableColumnModel cm = daily.getColumnModel();

        int colums = (daily.getColumnCount());

        ColumnGroup name = new ColumnGroup("時間帯情報");
        for (int i = 0; i < (colums - 8); i++) {
            name.add(cm.getColumn(i));
        }
        header.addColumnGroup(name);

        name = new ColumnGroup("客数");
        for (int i = (colums - 8); i < (colums - 6); i++) {
            name.add(cm.getColumn(i));
        }
        header.addColumnGroup(name);

        name = new ColumnGroup("客単価");
        for (int i = (colums - 6); i < (colums - 4); i++) {
            name.add(cm.getColumn(i));
        }
        header.addColumnGroup(name);

        name = new ColumnGroup("前年実績");
        for (int i = (colums - 4); i < (colums - 2); i++) {
            name.add(cm.getColumn(i));
        }
        header.addColumnGroup(name);

        name = new ColumnGroup("前年対比");
        for (int i = (colums - 2); i < colums; i++) {
            name.add(cm.getColumn(i));
        }
        header.addColumnGroup(name);

        dailyTotalScroll.setHorizontalScrollBar(dailyScroll.getHorizontalScrollBar());

    }

    private void setDailyTableColumnWidth(JTableEx table, int startIndex) {

        for (int i = startIndex; i < table.getColumnCount(); i++) {

            if (i < (table.getColumnCount() - 9)) {
                table.getColumnModel().getColumn(i).setPreferredWidth(57);
            } else {
                table.getColumnModel().getColumn(i).setPreferredWidth(60);
            }
        }

        table.getColumnModel().getColumn(table.getColumnCount() - 1).setPreferredWidth(45);
        table.getColumnModel().getColumn(table.getColumnCount() - 2).setPreferredWidth(45);
        table.getColumnModel().getColumn(table.getColumnCount() - 5).setPreferredWidth(50);
        table.getColumnModel().getColumn(table.getColumnCount() - 6).setPreferredWidth(50);
        table.getColumnModel().getColumn(table.getColumnCount() - 7).setPreferredWidth(43);
        table.getColumnModel().getColumn(table.getColumnCount() - 8).setPreferredWidth(43);
    }

    private List<String> getDailyHeaderList() {

        List<String> list = new ArrayList<String>();

        list.add("日");
        list.add("曜日");
        list.add("天気");

        for (String s : getTimeArray()) {
            list.add(s);
        }

        for (int i = 0; i < 5; i++) {
            list.add("日計");
            list.add("累計");
        }

        return list;
    }

    private void initDailyFixedTable() {

        dailyFixed = new JTableEx(daily.getModel());
        for (int i = daily.getColumnCount() - 1; i >= 0; i--) {
            if (i > 2) {
                dailyFixed.removeColumn(dailyFixed.getColumnModel().getColumn(i));
            } else {
                daily.removeColumn(daily.getColumnModel().getColumn(0));
            }
        }

        dailyFixed.getColumnModel().getColumn(0).setPreferredWidth(35);
        dailyFixed.getColumnModel().getColumn(1).setPreferredWidth(35);
        dailyFixed.getColumnModel().getColumn(2).setPreferredWidth(70);

        dailyFixed.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        dailyFixed.setSelectionBackground(new java.awt.Color(255, 210, 142));
        dailyFixed.setSelectionForeground(new java.awt.Color(0, 0, 0));
        dailyFixed.getTableHeader().setReorderingAllowed(false);
        dailyFixed.getTableHeader().setResizingAllowed(false);
        dailyFixed.setDefaultRenderer(String.class, new TimeTableCellRenderer());
        SwingUtil.setJTableHeaderRenderer(dailyFixed, SystemInfo.getTableHeaderRenderer());
        dailyFixed.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT - 5);
        dailyFixed.setSelectionModel(daily.getSelectionModel());
        dailyFixed.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        dailyFixed.setPreferredScrollableViewportSize(dailyFixed.getPreferredSize());
        dailyFixed.setSelectionBackground(daily.getSelectionBackground());
        dailyScroll.setRowHeaderView(dailyFixed);
        dailyScroll.setCorner(JScrollPane.UPPER_LEFT_CORNER, dailyFixed.getTableHeader());
        daily.setRowHeight(dailyFixed.getRowHeight());
    }

    /**
     * ********************************************
     */
    // 分類別集計の初期化処理
    /**
     * ********************************************
     */
    private void initCategoryHeader() {

        List<String> timeArrayList = getTimeArray();

        List<String> list = this.getCategoryHeaderList(timeArrayList);

        category.setModel(new DefaultTableModel(
                new Object[][]{}, (String[]) list.toArray(new String[0])) {
            public Class getColumnClass(int columnIndex) {
                return String.class;
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });

        list.remove(0);

        categoryTotal.setModel(new DefaultTableModel(
                new Object[][]{}, (String[]) list.toArray(new String[0])) {
            public Class getColumnClass(int columnIndex) {
                return String.class;
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });

        categoryTotal.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        categoryTotal.setSelectionBackground(new java.awt.Color(255, 210, 142));
        categoryTotal.setSelectionForeground(new java.awt.Color(0, 0, 0));
        categoryTotal.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryTotal.getTableHeader().setReorderingAllowed(false);
        categoryTotal.getTableHeader().setResizingAllowed(false);
        categoryTotal.setDefaultRenderer(String.class, new TimeTableCellRenderer());
        SwingUtil.setJTableHeaderRenderer(categoryTotal, SystemInfo.getTableHeaderRenderer());
        categoryTotal.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT - 5);
        for (int i = 0; i < categoryTotal.getColumnCount(); i++) {
            categoryTotal.getColumnModel().getColumn(i).setPreferredWidth(i % 2 > 0 ? 35 : 60);
        }

        category.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        category.setSelectionBackground(new java.awt.Color(255, 210, 142));
        category.setSelectionForeground(new java.awt.Color(0, 0, 0));
        category.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        category.getTableHeader().setReorderingAllowed(false);
        category.getTableHeader().setResizingAllowed(false);
        category.setDefaultRenderer(String.class, new TimeTableCellRenderer());
        SwingUtil.setJTableHeaderRenderer(category, SystemInfo.getTableHeaderRenderer());
        category.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT - 5);

        for (int i = 1; i < category.getColumnCount(); i++) {
            category.getColumnModel().getColumn(i).setPreferredWidth(i % 2 > 0 ? 60 : 35);
        }

        this.initCategoryFixedTable();

        GroupableTableHeader header = (GroupableTableHeader) category.getTableHeader();
        header.setBackground(new Color(204, 204, 204));
        TableColumnModel cm = category.getColumnModel();

        int col = 0;
        ColumnGroup name = null;
        for (String s : timeArrayList) {
            name = new ColumnGroup(s);
            name.add(cm.getColumn(col++));
            name.add(cm.getColumn(col++));
            header.addColumnGroup(name);
        }
        name = new ColumnGroup("合計");
        name.add(cm.getColumn(col++));
        name.add(cm.getColumn(col++));
        header.addColumnGroup(name);

        categoryTotalScroll.setHorizontalScrollBar(categoryScroll.getHorizontalScrollBar());
    }

    private List<String> getCategoryHeaderList(List<String> timeArrayList) {

        List<String> list = new ArrayList<String>();

        list.add("分類");

        for (int i = 0; i <= timeArrayList.size(); i++) {
            list.add("金額");
            list.add("数量");
        }

        return list;
    }

    private void initCategoryFixedTable() {

        categoryFixed = new JTableEx(category.getModel());
        for (int i = category.getColumnCount() - 1; i >= 0; i--) {
            if (i > 0) {
                categoryFixed.removeColumn(categoryFixed.getColumnModel().getColumn(i));
            } else {
                category.removeColumn(category.getColumnModel().getColumn(0));
            }
        }

        categoryFixed.getColumnModel().getColumn(0).setPreferredWidth(120);

        categoryFixed.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        categoryFixed.setSelectionBackground(new java.awt.Color(255, 210, 142));
        categoryFixed.setSelectionForeground(new java.awt.Color(0, 0, 0));
        categoryFixed.getTableHeader().setReorderingAllowed(false);
        categoryFixed.getTableHeader().setResizingAllowed(false);
        categoryFixed.setDefaultRenderer(String.class, new TimeTableCellRenderer());
        SwingUtil.setJTableHeaderRenderer(categoryFixed, SystemInfo.getTableHeaderRenderer());
        categoryFixed.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT - 5);
        categoryFixed.setSelectionModel(category.getSelectionModel());
        categoryFixed.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        categoryFixed.setPreferredScrollableViewportSize(categoryFixed.getPreferredSize());
        categoryFixed.setSelectionBackground(category.getSelectionBackground());
        categoryScroll.setRowHeaderView(categoryFixed);
        categoryScroll.setCorner(JScrollPane.UPPER_LEFT_CORNER, categoryFixed.getTableHeader());
        category.setRowHeight(categoryFixed.getRowHeight());
    }

    /**
     * ********************************************
     */
    // 曜日別集計の初期化処理
    /**
     * ********************************************
     */
    private void initWeeklyHeader() {

        List<String> list = this.getWeeklyHeaderList();

        weekly.setModel(new DefaultTableModel(
                new Object[][]{}, (String[]) list.toArray(new String[0])) {
            public Class getColumnClass(int columnIndex) {
                return String.class;
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });

        list.remove(0);

        weeklyTotal.setModel(new DefaultTableModel(
                new Object[][]{}, (String[]) list.toArray(new String[0])) {
            public Class getColumnClass(int columnIndex) {
                return String.class;
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });

        weeklyTotal.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        weeklyTotal.setSelectionBackground(new java.awt.Color(255, 210, 142));
        weeklyTotal.setSelectionForeground(new java.awt.Color(0, 0, 0));
        weeklyTotal.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        weeklyTotal.getTableHeader().setReorderingAllowed(false);
        weeklyTotal.getTableHeader().setResizingAllowed(false);
        weeklyTotal.setDefaultRenderer(String.class, new TimeTableCellRenderer());
        SwingUtil.setJTableHeaderRenderer(weeklyTotal, SystemInfo.getTableHeaderRenderer());
        weeklyTotal.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT - 5);
        this.setWeeklyTableColumnWidth(weeklyTotal, 0);

        weekly.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        weekly.setSelectionBackground(new java.awt.Color(255, 210, 142));
        weekly.setSelectionForeground(new java.awt.Color(0, 0, 0));
        weekly.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        weekly.getTableHeader().setReorderingAllowed(false);
        weekly.getTableHeader().setResizingAllowed(false);
        weekly.setDefaultRenderer(String.class, new TimeTableCellRenderer());
        SwingUtil.setJTableHeaderRenderer(weekly, SystemInfo.getTableHeaderRenderer());
        weekly.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT - 5);
        this.setWeeklyTableColumnWidth(weekly, 3);

        this.initWeeklyFixedTable();

        GroupableTableHeader header = (GroupableTableHeader) weekly.getTableHeader();
        header.setBackground(new Color(204, 204, 204));
        TableColumnModel cm = weekly.getColumnModel();

        int colums = (weekly.getColumnCount());

        ColumnGroup name = new ColumnGroup("時間帯情報");
        for (int i = 0; i < (colums - 8); i++) {
            name.add(cm.getColumn(i));
        }
        header.addColumnGroup(name);

        name = new ColumnGroup("客数");
        for (int i = (colums - 8); i < (colums - 6); i++) {
            name.add(cm.getColumn(i));
        }
        header.addColumnGroup(name);

        name = new ColumnGroup("客単価");
        for (int i = (colums - 6); i < (colums - 4); i++) {
            name.add(cm.getColumn(i));
        }
        header.addColumnGroup(name);

        name = new ColumnGroup("前年実績");
        for (int i = (colums - 4); i < (colums - 2); i++) {
            name.add(cm.getColumn(i));
        }
        header.addColumnGroup(name);

        name = new ColumnGroup("前年対比");
        for (int i = (colums - 2); i < colums; i++) {
            name.add(cm.getColumn(i));
        }
        header.addColumnGroup(name);

        weeklyTotalScroll.setHorizontalScrollBar(weeklyScroll.getHorizontalScrollBar());

    }

    private void setWeeklyTableColumnWidth(JTableEx table, int startIndex) {

        for (int i = startIndex; i < table.getColumnCount(); i++) {

            if (i < (table.getColumnCount() - 9)) {
                table.getColumnModel().getColumn(i).setPreferredWidth(75);
            } else {
                table.getColumnModel().getColumn(i).setPreferredWidth(60);
            }
        }

        table.getColumnModel().getColumn(table.getColumnCount() - 1).setPreferredWidth(45);
        table.getColumnModel().getColumn(table.getColumnCount() - 2).setPreferredWidth(45);
        table.getColumnModel().getColumn(table.getColumnCount() - 5).setPreferredWidth(50);
        table.getColumnModel().getColumn(table.getColumnCount() - 6).setPreferredWidth(50);
        table.getColumnModel().getColumn(table.getColumnCount() - 7).setPreferredWidth(43);
        table.getColumnModel().getColumn(table.getColumnCount() - 8).setPreferredWidth(43);
    }

    private List<String> getWeeklyHeaderList() {

        List<String> list = new ArrayList<String>();

        list.add("曜日");

        for (String s : getTimeArray()) {
            list.add(s);
        }

        for (int i = 0; i < 5; i++) {
            list.add("日計");
            list.add("累計");
        }

        return list;
    }

    private void initWeeklyFixedTable() {

        weeklyFixed = new JTableEx(weekly.getModel());
        for (int i = weekly.getColumnCount() - 1; i >= 0; i--) {
            if (i > 0) {
                weeklyFixed.removeColumn(weeklyFixed.getColumnModel().getColumn(i));
            } else {
                weekly.removeColumn(weekly.getColumnModel().getColumn(0));
            }
        }

        weeklyFixed.getColumnModel().getColumn(0).setPreferredWidth(57);

        weeklyFixed.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        weeklyFixed.setSelectionBackground(new java.awt.Color(255, 210, 142));
        weeklyFixed.setSelectionForeground(new java.awt.Color(0, 0, 0));
        weeklyFixed.getTableHeader().setReorderingAllowed(false);
        weeklyFixed.getTableHeader().setResizingAllowed(false);
        weeklyFixed.setDefaultRenderer(String.class, new TimeTableCellRenderer());
        SwingUtil.setJTableHeaderRenderer(weeklyFixed, SystemInfo.getTableHeaderRenderer());
        weeklyFixed.setRowHeight(SystemInfo.TABLE_ROW_HEIGHT - 5);
        weeklyFixed.setSelectionModel(weekly.getSelectionModel());
        weeklyFixed.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        weeklyFixed.setPreferredScrollableViewportSize(weeklyFixed.getPreferredSize());
        weeklyFixed.setSelectionBackground(weekly.getSelectionBackground());
        weeklyScroll.setRowHeaderView(weeklyFixed);
        weeklyScroll.setCorner(JScrollPane.UPPER_LEFT_CORNER, weeklyFixed.getTableHeader());
        weekly.setRowHeight(weeklyFixed.getRowHeight());
    }

    /**
     * TimeTableCellRenderer
     */
    public class TimeTableCellRenderer extends SelectTableCellRenderer {

        public TimeTableCellRenderer() {
            super();
        }

        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (timeTab.getSelectedIndex() == 0) {
                // 日別集計
                if (table.equals(dailyFixed)) {
                    super.setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    super.setHorizontalAlignment(SwingConstants.RIGHT);
                }
            } else if (timeTab.getSelectedIndex() == 1) {
                // 分類別集計
                if (table.equals(categoryFixed)) {
                    super.setHorizontalAlignment(SwingConstants.LEFT);
                } else {
                    super.setHorizontalAlignment(SwingConstants.RIGHT);
                }
            } else if (timeTab.getSelectedIndex() == 2) {
                // 曜日別集計
                if (table.equals(weeklyFixed)) {
                    super.setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    super.setHorizontalAlignment(SwingConstants.RIGHT);
                }
            }

            Object obj = table.getValueAt(row, column);
            if (obj != null && CheckUtil.isNumeric(obj.toString())) {
                if (Double.parseDouble(obj.toString()) < 0) {
                    super.setForeground(Color.RED);
                }
            }

            return this;
        }
    }

    private String getTargetTimeName() {

        if (rdoVisitTime.isSelected()) {
            return rdoVisitTime.getText();
        } else if (rdoStartTime.isSelected()) {
            return rdoStartTime.getText();
        } else {
            return rdoLeaveTime.getText();
        }
    }

    /**
     * --------------------------------------- 日別集計 Excel出力
     * ---------------------------------------
     */
    private void printDaily() {

        JExcelApi jx = new JExcelApi("日別時間帯別売上");
        jx.setTemplateFile("/reports/時間帯別帳票.xls");

        // シート削除
        jx.removeSheet(2);
        jx.removeSheet(2);
        jx.setTargetSheet(1);

        // ヘッダ
        jx.setValue(3, 2, this.shopName);
        jx.setValue(3, 3, startDate.getDateStr("/") + " ～ " + endDate.getDateStr("/"));
        jx.setValue(3, 4, getTargetTimeName());
        jx.setValue(3, 5, rdoTaxUnit.isSelected() ? "税込" : "税抜");

        // 列削除
        for (int col = 0; col < (40 - (daily.getColumnCount() - 10)); col++) {
            jx.removeColumn(4);
        }
        jx.setValue(4, 7, "時間帯情報");

        // 時間帯情報
        for (int i = 0; i < daily.getColumnCount() - 10; i++) {
            jx.setValue(i + 4, 8, daily.getColumnModel().getColumn(i).getHeaderValue());
        }

        int row = 9;

        // 行削除
        for (int i = 0; i < (31 - daily.getRowCount()); i++) {
            jx.removeRow(row);
        }

        // データセット
        for (int i = 0; i < daily.getRowCount(); i++) {

            jx.setValue(1, row, (Integer) dailyFixed.getValueAt(i, 0));
            jx.setValue(2, row, (String) dailyFixed.getValueAt(i, 1));
            jx.setValue(3, row, (String) dailyFixed.getValueAt(i, 2));

            for (int j = 0; j < (daily.getColumnCount() - 10); j++) {
                jx.setValue(j + 4, row, daily.getValueAt(i, j));
            }

            // 客数（日計）
            int tmpCol = daily.getColumnCount() - 8;
            jx.setValue(tmpCol + 4, row, daily.getValueAt(i, tmpCol));

            // 前年実績（日計）
            tmpCol = daily.getColumnCount() - 4;
            jx.setValue(tmpCol + 4, row, daily.getValueAt(i, tmpCol));

            row++;
        }

        // ウィンドウ枠の固定
        jx.getTargetSheet().getSettings().setVerticalFreeze(8);
        jx.getTargetSheet().getSettings().setHorizontalFreeze(3);

        // 横、縦 1ページに印刷
        jx.getTargetSheet().getSettings().setFitWidth(1);
        jx.getTargetSheet().getSettings().setFitHeight(1);

        jx.openWorkbook();

    }

    /**
     * --------------------------------------- 分類別集計 Excel出力
     * ---------------------------------------
     */
    private void printCategory() {

        JExcelApi jx = new JExcelApi("分類別時間帯別売上");
        jx.setTemplateFile("/reports/時間帯別帳票.xls");

        // シート削除
        jx.removeSheet(1);
        jx.removeSheet(2);
        jx.setTargetSheet(1);

        // ヘッダ
        jx.setValue(2, 2, this.shopName);
        jx.setValue(2, 3, startDate.getDateStr("/") + " ～ " + endDate.getDateStr("/"));
        jx.setValue(2, 4, getTargetTimeName());
        jx.setValue(2, 5, rdoTaxUnit.isSelected() ? "税込" : "税抜");

        if (!rdoClassAll.isSelected()) {
            String s = rdoClassTech.isSelected() ? "技術" : "商品";
            jx.getTargetSheet().setName(s + jx.getTargetSheet().getName());

            HeaderFooter header = jx.getTargetSheet().getSettings().getHeader();
            header.getCentre().clear();
            header.getCentre().setFontSize(16);
            header.getCentre().toggleBold();
            header.getCentre().append(jx.getTargetSheet().getName());
        }

        // 列削除
        int colCount = category.getColumnCount();
        for (int col = 0; col < (80 - (colCount - 2)); col++) {
            jx.removeColumn(colCount);
        }
        jx.getTargetSheet().setColumnView(colCount - 1, 9);
        jx.getTargetSheet().setColumnView(colCount, 8);

        // 時間帯情報
        List<String> timeArrayList = getTimeArray();
        for (int i = 0; i < timeArrayList.size(); i++) {
            jx.setValue(2 * (i + 1), 7, timeArrayList.get(i));
        }

        int row = 9;

        // 追加行数セット
        jx.insertRow(row, category.getRowCount() - 1);

        // 分類別集計
        for (int i = 0; i < category.getRowCount(); i++) {

            jx.setValue(1, row, (String) categoryFixed.getValueAt(i, 0));

            for (int j = 0; j < (category.getColumnCount() - 2); j++) {
                jx.setValue(j + 2, row, category.getValueAt(i, j));
            }

            row++;
        }

        jx.removeRow(row);

        // フッタ合計
        for (int i = 0; i < (categoryTotal.getColumnCount() - 2); i++) {
            if (i % 2 > 0) {
                continue;
            }
            jx.setValue(i + 2, row, categoryTotal.getValueAt(0, i));
            jx.setValue(i + 2, row + 2, categoryTotal.getValueAt(2, i));
        }

        // ウィンドウ枠の固定
        jx.getTargetSheet().getSettings().setVerticalFreeze(8);
        jx.getTargetSheet().getSettings().setHorizontalFreeze(1);

        // 横、縦 1ページに印刷
        jx.getTargetSheet().getSettings().setFitWidth(1);
        jx.getTargetSheet().getSettings().setFitHeight(1);

        jx.openWorkbook();

    }

    /**
     * --------------------------------------- 曜日別集計 Excel出力
     * ---------------------------------------
     */
    private void printWeekly() {

        JExcelApi jx = new JExcelApi("曜日別時間帯別売上");
        jx.setTemplateFile("/reports/時間帯別帳票.xls");

        // シート削除
        jx.removeSheet(1);
        jx.removeSheet(1);
        jx.setTargetSheet(1);

        // ヘッダ
        jx.setValue(3, 2, this.shopName);
        jx.setValue(3, 3, startDate.getDateStr("/") + " ～ " + endDate.getDateStr("/"));
        jx.setValue(3, 4, getTargetTimeName());
        jx.setValue(3, 5, rdoTaxUnit.isSelected() ? "税込" : "税抜");

        // 列削除
        for (int col = 0; col < (40 - (weekly.getColumnCount() - 10)); col++) {
            jx.removeColumn(4);
        }
        jx.setValue(2, 7, "時間帯情報");

        // 時間帯情報
        for (int i = 0; i < weekly.getColumnCount() - 10; i++) {
            jx.setValue(i + 2, 8, weekly.getColumnModel().getColumn(i).getHeaderValue());
        }

        int row = 9;

        // データセット
        for (int i = 0; i < weekly.getRowCount(); i++) {

            jx.setValue(1, row, weeklyFixed.getValueAt(i, 0).toString());

            for (int j = 0; j < (weekly.getColumnCount() - 10); j++) {
                jx.setValue(j + 2, row, weekly.getValueAt(i, j));
            }

            // 客数（日計）
            int tmpCol = weekly.getColumnCount() - 8;
            jx.setValue(tmpCol + 2, row, weekly.getValueAt(i, tmpCol));

            // 前年実績（日計）
            tmpCol = weekly.getColumnCount() - 4;
            jx.setValue(tmpCol + 2, row, weekly.getValueAt(i, tmpCol));

            row++;
        }

        // ウィンドウ枠の固定
        jx.getTargetSheet().getSettings().setVerticalFreeze(8);
        jx.getTargetSheet().getSettings().setHorizontalFreeze(3);

        // 横、縦 1ページに印刷
        jx.getTargetSheet().getSettings().setFitWidth(1);
        jx.getTargetSheet().getSettings().setFitHeight(1);

        jx.openWorkbook();

    }

    private String getTargetTimeColumn(String col) {
        StringBuilder sql = new StringBuilder();
        sql.append(" to_char(" + col + ", 'hh24')::integer");
        sql.append("     + case when");
        sql.append("         (date_part('hour', " + col + ") || to_char(" + col + ", 'mi'))::integer");
        sql.append("             < (ms.open_hour || lpad(cast(ms.open_minute as varchar), 2, '0'))::integer");
        //IVS_LVTu start add 2017/10/16 #27846 [gb]時間帯分析：精算時間が営業時間前の時に集計時間帯がおかしい
        if (rdoLeaveTime.isSelected()) {
            sql.append("  and dr.leave_time::date > ds.sales_date::date ");
        }
        if (rdoVisitTime.isSelected()) {
            sql.append("  and dr.visit_time::date > ds.sales_date::date ");
        }
        if (rdoStartTime.isSelected()) {
            sql.append("  and dr.start_time::date > ds.sales_date::date ");
        }
        //IVS_LVTu end add 2017/10/16 #27846 [gb]時間帯分析：精算時間が営業時間前の時に集計時間帯がおかしい
        sql.append("       then 24 else 0 end as target_time");
        return sql.toString();
    }
}
