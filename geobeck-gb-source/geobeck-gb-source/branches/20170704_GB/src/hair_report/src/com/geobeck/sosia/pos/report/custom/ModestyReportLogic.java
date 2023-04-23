/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.report.custom;

import com.geobeck.sosia.pos.hair.report.util.JPOIApi;
import com.geobeck.sosia.pos.master.company.MstGroup;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ResultSetWrapper;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

/**
 *
 * @author yasumoto
 */
public class ModestyReportLogic {

	/**
	 * コミッション明細書
	 * @param shop
	 * @param staff
	 * @param targetDate
	 * @return 
	 */
	protected boolean printMonthlyCommission(MstShop shop, MstStaff staff, Date targetDate) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Calendar td = Calendar.getInstance();
		td.setTime(targetDate);
		
		SimpleDateFormat sdft = new SimpleDateFormat("yyyy年MM");
		String month = sdft.format(td.getTime());
		
		// 月初
		td.set(Calendar.DATE, 1);
		Date bef = td.getTime();
		// 月末
		td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date aft = td.getTime();
		// ログ出力
		SystemInfo.getLogger().info(sdf.format(bef) + " to " + sdf.format(aft) + " - "
				+ shop.getShopName() + " - " + staff.getStaffNo() + " : " + staff.getStaffName(0));

		//------------------------------
		// データ取得
		//------------------------------	
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>(0);
		try {
            StringBuilder sql = new StringBuilder(4000);
			sql.append(" select  ");
			sql.append(" detail.shop_id, mshop.shop_name ,sales_date");
			sql.append(",customer_no ");
			sql.append(",customer_name1 || customer_name2 as customer_name ");
			sql.append(",slip_no ");
			sql.append(",SUM(detail_value_no_tax) as price ");
			sql.append(",rtype ");
			sql.append(",introducer_no ");
			sql.append(",introducer_name1 || introducer_name2 as introducer_name ");
			sql.append(",(select ms.staff_name1 || ms.staff_name2 from mst_staff ms where ms.staff_id = (CASE WHEN staff='"+staff.getStaffID()+"' THEN stf ELSE staff END)) as staffb ");
			sql.append(",sum(cnt) as cnts");
			sql.append(" from ");
			sql.append("( "); // 明細毎の対象データを取得 
			sql.append(" select ");
			sql.append(" dsd.shop_id, dsd.slip_no, dsd.slip_detail_no, ds.sales_date, dsd.staff_id as stf ");
			sql.append(" , ( sign( ( (dsd.product_num * dsd.product_value) / (1.0 + get_tax_rate(ds.sales_date))))   ");
			sql.append("     * ceil( abs( ( (dsd.product_num * dsd.product_value) / (1.0 + get_tax_rate(ds.sales_date)))))) as detail_value_no_tax   ");
			sql.append(",mc.customer_no, mc.customer_name1, mc.customer_name2 ");
			sql.append(",mc2.customer_no as introducer_no, mc2.customer_name1 as introducer_name1, mc2.customer_name2 as introducer_name2 ");
			sql.append(",COALESCE(dsdp.staff_id, dcss.staff_id) as staff, free_heading_name as rtype ,ds.customer_id ");
			sql.append(" from ");// 販売詳細 
			sql.append(" data_sales_detail dsd  ");
			sql.append(" inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no ");
			sql.append(" inner join mst_customer mc on ds.customer_id = mc.customer_id ");
			sql.append(" left outer join mst_customer mc2 on mc.introducer_id = mc2.customer_id ");
			sql.append(" left outer join data_sales_detail_proportionally dsdp on dsd.product_division in (2,4) and dsd.shop_id = dsdp.shop_id and  dsd.slip_no = dsdp.slip_no and  dsd.slip_detail_no = dsdp.slip_detail_no and dsd.staff_id <> dsdp.staff_id ");
			sql.append(" left outer join data_contract dc on dc.shop_id = dsd.shop_id and dc.slip_no = dsd.slip_no and dsd.product_division = 5 and dsd.product_id = dc.product_id ");
			sql.append(" left outer join data_contract_staff_share dcss on dsd.shop_id = dcss.shop_id  and dc.slip_no = dcss.slip_no and dc.contract_no = dcss.contract_no and dc.contract_detail_no = dcss.contract_detail_no and dsd.staff_id <> dcss.staff_id ");
			sql.append(" left outer join mst_customer_free_heading mcfh on ds.customer_id = mcfh.customer_id and mcfh.free_heading_class_id = '4' ");
			sql.append(" left outer join mst_free_heading mfh on mcfh.free_heading_class_id = mfh.free_heading_class_id and mcfh.free_heading_id = mfh.free_heading_id ");
			sql.append(" where ");
			sql.append(" ( ");// 技術or技術クレーム and アイラッシュ会員orフリー会員
			sql.append(" ( dsd.product_division in (1,2) and exists(select technic_id from mst_technic mt where mt.technic_class_id in (2,4) and dsd.product_id = mt.technic_id ) ) ");
			sql.append(" or ");// 商品or商品返品＞右記以外の商品（ガウン＋業務用＋その他＋インオーラ＋社員○○○を集計しない）
                        // 20170602 GB START EDIT #4331 モデスティ＞カスタマイズ帳票　コミッション明細書仕様変更希望
			//sql.append(" ( dsd.product_division in (2,4) and exists(select item_id from mst_item mt where mt.item_class_id not in (10,12,14,15,16,39,46,55,57,60,64,65,66,67,68,69,70,71,72,73,74,75) and dsd.product_id = mt.item_id ) ) ");
                        sql.append(" ( dsd.product_division in (2,4) and exists(select item_id from mst_item mt where mt.item_class_id not in (1,2,3,10,12,13,14,15,16,18,22,39,40,42,43,45,54,55,56,57,60,64,65,66,67,68,69,70,71,72,73,74,75,78) and dsd.product_id = mt.item_id ) ) ");
                        // 20170602 GB END EDIT #4331 モデスティ＞カスタマイズ帳票　コミッション明細書仕様変更希望
                        sql.append(" or ");// 契約＞すべての分類 
			sql.append(" dsd.product_division = 5) ");
			sql.append(" and (dsd.staff_id ='"+staff.getStaffID()+"' or dsdp.staff_id ='"+staff.getStaffID()+"' or dcss.staff_id ='"+staff.getStaffID()+"' ) ");
			sql.append(" and ds.sales_date >= '"+sdf.format(bef)+"' and ds.sales_date <= '"+sdf.format(aft)+"' ");
			sql.append("  ) detail ");
			sql.append(" inner join mst_shop mshop on mshop.shop_id = detail.shop_id ");
			// 過去に契約等がある顧客を取得
			sql.append(" left outer join");
 			sql.append(" (select ds.customer_id, count(ds.customer_id) as cnt");
			sql.append(" from ");// 販売詳細 
			sql.append(" data_sales_detail dsd  ");
			sql.append(" inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no ");
			//全ユーザーだと重いので今月アクティブな人で絞り込み
			sql.append(" inner join data_sales ds2 on ds2.customer_id = ds.customer_id and ds2.sales_date >= '"+sdf.format(bef)+"' and ds2.sales_date <= '"+sdf.format(aft)+"'");
			sql.append(" where ");
			sql.append(" (( dsd.product_division in (1,2) and exists(select technic_id from mst_technic mt where mt.technic_class_id in (2,4) and dsd.product_id = mt.technic_id ) ) ");
			// 20170602 GB START EDIT #4331 モデスティ＞カスタマイズ帳票　コミッション明細書仕様変更希望
                        //sql.append(" or ( dsd.product_division in (2,4) and exists(select item_id from mst_item mt where mt.item_class_id not in (10,12,14,15,16,39,46,55,57,60,64,65,66,67,68,69,70,71,72,73,74,75) and dsd.product_id = mt.item_id ) ) ");
			sql.append(" or ( dsd.product_division in (2,4) and exists(select item_id from mst_item mt where mt.item_class_id not in (1,2,3,10,12,13,14,15,16,18,22,39,40,42,43,45,54,55,56,57,60,64,65,66,67,68,69,70,71,72,73,74,75,78) and dsd.product_id = mt.item_id ) ) ");
                        // 20170602 GB END EDIT #4331 モデスティ＞カスタマイズ帳票　コミッション明細書仕様変更希望
                        sql.append(" or dsd.product_division = 5)");
			sql.append(" and ds.sales_date < '"+sdf.format(bef)+"'");
			sql.append(" group by ds.customer_id) cont on cont.customer_id = detail.customer_id");
			sql.append(" group by detail.shop_id, mshop.shop_name, sales_date, slip_no, stf, customer_no, staff, customer_name1, customer_name2, rtype, introducer_no, introducer_name1, introducer_name2 ");
			sql.append(" order by sales_date, slip_no");

			SystemInfo.getLogger().info(sql.toString());
            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
				// 按分時の割合
				int n = (rs.getString("staffb")!=null && !rs.getString("staffb").equals(""))? 2: 1;
				
                Map<String, Object> resMap = new  HashMap<String, Object>();
				resMap.put("salesDate", rs.getDate("sales_date"));
				resMap.put("customerNo", rs.getString("shop_name"));
				resMap.put("customerName", rs.getString("customer_name"));
				resMap.put("price", rs.getInt("price")/n);
				resMap.put("rType", rs.getString("rtype"));
				resMap.put("introducerNo", rs.getString("introducer_no"));
				resMap.put("introducerName", rs.getString("introducer_name"));
				
				// 過去の来店
				if ( rs.getInt("cnts") > 0) {
					resMap.put("newP", "-");
					resMap.put("repP", rs.getInt("price")/(2 * n) );
				} else {
					resMap.put("newP", rs.getInt("price")/(2 * n) );
					resMap.put("repP", "-");
				}
				resMap.put("staffB", rs.getString("staffb"));

				SystemInfo.getLogger().info(resMap.toString());
                resultList.add(resMap);
            }
            rs.close();

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
		//対象データが0件の場合falseを返す
		if (resultList.size() < 1) {
			return false;
		}
		
		JPOIApi jx = new JPOIApi("コミッション明細書");
        jx.setTemplateFile("/com/geobeck/sosia/pos/report/custom/modesty_monthly_commission.xls");
		int maxPage = (int)Math.ceil(resultList.size() / 19d);
		int p = 0;
		SimpleDateFormat dd = new SimpleDateFormat("dd");
		
		for(int x=maxPage; x>1; x--) {
			jx.copySheet(0);
		}
		
		int a_sum_price=0;
		int a_sum_new=0;
		int a_sum_rep=0;
		//対象データが20件以上ある場合（複数ページ出力する必要がある場合）
		while(p<maxPage) {
			p++;
			//対象データを全て出力するまでループ
			jx.setTargetSheet(p-1);
			//------------------------------
			// ヘッダ
			//------------------------------
			jx.setCellValue(1, 1, month);
			jx.setCellValue(3, 2, shop.getShopName());
			jx.setCellValue(3, 3, staff.getStaffName(0) +" "+  staff.getStaffName(1));
			jx.setCellValue(14, 1, p);
			//------------------------------
			// ボディー
			//------------------------------
			int ll = 19*(p-1);
			int row = 8;
			int sum_price=0;
			int sum_new=0;
			int sum_rep=0;
			for (int i = 0; i < 19; i++){
				if (i+ll < resultList.size()) {
					Map<String, Object> nowRow = resultList.get(i+ll);
					jx.setCellValue(1, row+i, dd.format(nowRow.get("salesDate")));
					jx.setCellValue(2, row+i, (String)nowRow.get("customerNo"));
					jx.setCellValue(3, row+i, (String)nowRow.get("customerName"));
					jx.setCellValue(4, row+i, (int)nowRow.get("price"));
					jx.setCellValue(10, row+i, (String)nowRow.get("staffB"));
					
					if (nowRow.get("price") != null) {
						sum_price += (int)nowRow.get("price");
					}
					if(!nowRow.get("newP").equals("-")) {
						//新規のみ表示
						jx.setCellValue(8, row+i, (int)nowRow.get("newP"));
						jx.setCellValue(9, row+i, (String)nowRow.get("repP"));
						jx.setCellValue(5, row+i, (String)nowRow.get("rType"));
						jx.setCellValue(6, row+i, (String)nowRow.get("introducerNo"));
						jx.setCellValue(7, row+i, (String)nowRow.get("introducerName"));
						sum_new += (int)nowRow.get("newP");
					}
					if(!nowRow.get("repP").equals("-")) {
						jx.setCellValue(8, row+i, (String)nowRow.get("newP"));
						jx.setCellValue(9, row+i, (int)nowRow.get("repP"));
						sum_rep += (int)nowRow.get("repP");
					}
				}
			}
			//------------------------------
			// 合計欄
			//------------------------------
			jx.setCellValue(4, row+19, sum_price);
			jx.setCellValue(8, row+19, sum_new);
			jx.setCellValue(9, row+19, sum_rep);
			
			a_sum_price += sum_price;
			a_sum_new += sum_new;
			a_sum_rep += sum_rep;
			
			if (p<maxPage) {	
				jx.setCellValue(4, row+20, "");
				jx.setCellValue(8, row+20, "");
				jx.setCellValue(9, row+20, "");
			} else {
				jx.setCellValue(4, row+20, a_sum_price);
				jx.setCellValue(8, row+20, a_sum_new);
				jx.setCellValue(9, row+20, a_sum_rep);
			}
		}

		jx.openWorkbook();
		return true;
	}

	/**
	 * 役務償却日報出力
	 * @param shop
	 * @param targetDate
	 * @return 
	 */
	protected boolean printDailyReport(MstShop shop, Date targetDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		
		String shopList = shop.getShopID()+"";
		String shopName = shop.getShopName();

		// 日付関連
		Calendar td = Calendar.getInstance();
		td.setTime(targetDate);
		//当月末
		td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date aftDate = td.getTime();
		// 月初
		td.set(Calendar.DATE, 1);
		Date baseDate = td.getTime();
		// 1カ月前
		td.add(Calendar.MONTH, -1);
		Date bef1m = td.getTime();
		// 2カ月前
		td.add(Calendar.MONTH, -1);
		Date bef2m = td.getTime();
		// 3カ月前
		td.add(Calendar.MONTH, -1);
		Date bef3m = td.getTime();

		// ログ出力
		SystemInfo.getLogger().info(sdf.format(targetDate) + " - " + shop.getClass()+"_"+shopList);

		//------------------------------
		// データ取得
		//------------------------------	
		int money = 0;
		int money_tax = 0;
		int invalid_money = 0;
		Long[] mdata = new Long[3];
		Set<Integer> cIds = new HashSet<Integer>(0);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>(0);
		List<Map<Integer, Integer>> summaryList = new ArrayList<Map<Integer, Integer>>(0);
		Set<String> staffNames = new HashSet<String>(0);
		try {
            StringBuilder sql = new StringBuilder(4000);
			sql.append(" select");
			sql.append("   mc.customer_id ");
			sql.append("   , mc.customer_name1 ");
			sql.append("   , mc.customer_name2 ");
			sql.append("   , mcr.course_name ");
			sql.append("   , mcr.num , (dcc.product_value / mcr.num) as product_value");
			sql.append("   , ( sign( ( (dc.product_num * (dcc.product_value/mcr.num)) / (1.0 + get_tax_rate(dsx.sales_date))))  ");
			sql.append("     * ceil( abs( ( (dc.product_num * (dcc.product_value/mcr.num)) / (1.0 + get_tax_rate(dsx.sales_date))))) ");
			sql.append("     ) AS detail_value_no_tax ");
			sql.append("   , dc.staff_id ");
			sql.append("   , ms.staff_name1 || ms.staff_name2 AS staff_name ");
			sql.append("   , (select sum(n.product_num) from data_contract_digestion n  ");
			sql.append("      where n.contract_no = dc.contract_no  ");
			sql.append("      and n.slip_no <= dc.slip_no and n.contract_detail_no = dc.contract_detail_no ");
			sql.append("      and n.contract_shop_id = dc.contract_shop_id and n.delete_date is null) as used ");
			sql.append("   ,dcc.valid_date ");
			sql.append("   ,(select sales_date from data_sales sdt where sdt.shop_id = dcc.shop_id and sdt.slip_no = dcc.slip_no ) as insert_date ");
			sql.append("   ,(CASE WHEN mcr.is_praise_time THEN  mcr.praise_time_limit ");
			sql.append("    ELSE 0 END) AS limit_flg ");
			sql.append(",(select count(cds.slip_no) as can2 from data_sales_detail cds where cds.shop_id = ds.shop_id and cds.slip_no = ds.slip_no and cds.product_id=24 and cds.product_division=1) as can2 ");
			sql.append(" from ");
			sql.append("   data_sales ds  ");
			sql.append("   inner join mst_customer mc on ds.customer_id = mc.customer_id  ");
			sql.append("   left outer join data_sales_detail dsd on ds.shop_id = dsd.shop_id  and ds.slip_no = dsd.slip_no  ");
			sql.append("   left outer join data_contract_digestion dc on ds.shop_id = dc.shop_id  and ds.slip_no = dc.slip_no and dsd.contract_no = dc.contract_no and dsd.contract_detail_no = dc.contract_detail_no  ");
			sql.append("   left outer join data_contract dcc on  dc.contract_shop_id = dcc.shop_id and dc.contract_no = dcc.contract_no and dc.contract_detail_no = dcc.contract_detail_no ");
			sql.append("   left outer join data_sales dsx on  dsx.shop_id = dcc.shop_id and dsx.slip_no = dcc.slip_no ");
			sql.append("   left outer join mst_course mcr on dsd.product_id = mcr.course_id ");
			sql.append("   left outer join mst_staff ms on dc.staff_id = ms.staff_id ");
			sql.append(" where ");
			sql.append("   dsd.product_division = 6  ");
			sql.append("   and ds.shop_id =").append(shopList);
			sql.append("   and ds.sales_date = '"+sdf.format(targetDate)+"' ");
			sql.append("   and ds.delete_date is null ");
			sql.append("   and dsd.delete_date is null ");
			sql.append(" order by ds.slip_no, dsd.slip_detail_no ");
			SystemInfo.getLogger().info(sql.toString());
		    ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
                Map<String, Object> resMap = new  HashMap<String, Object>();
				resMap.put("customer_id", rs.getInt("customer_id"));
				resMap.put("customer_name1", rs.getString("customer_name1"));
				resMap.put("customer_name2", rs.getString("customer_name2"));
				resMap.put("course_name", rs.getString("course_name"));
				resMap.put("num", rs.getBigDecimal("num"));
				resMap.put("detail_value_no_tax", rs.getInt("detail_value_no_tax"));
				resMap.put("staff_name", rs.getString("staff_name"));
				resMap.put("used", rs.getBigDecimal("used"));
				resMap.put("limit_flg", "");
				resMap.put("can2", rs.getInt("can2"));
				
				if (rs.getInt("limit_flg") > 0
						&& rs.getDate("valid_date")!=null
						&& rs.getDate("insert_date")!=null) {
					// カレンダークラスのインスタンスを取得
					Calendar cal1 = Calendar.getInstance();
					Calendar cal2 = Calendar.getInstance();
					cal1.setTime(rs.getDate("valid_date"));
					cal2.setTime(rs.getDate("insert_date"));
					cal2.add(Calendar.MONTH, rs.getInt("limit_flg"));
				
					if(cal1.compareTo(cal2)>0){
						SystemInfo.getLogger().info(
								sdf.format(cal1.getTime()) +","
								+ sdf.format(cal2.getTime()));

						resMap.put("limit_flg", "×");
						invalid_money+=rs.getBigDecimal("product_value").intValue();
					} else {
						cIds.add(rs.getInt("customer_id"));
						money+=rs.getInt("detail_value_no_tax");
						money_tax+=rs.getBigDecimal("product_value").intValue();
					}
//					if(cal1.get(Calendar.YEAR)==cal2.get(Calendar.YEAR)
//						&&cal1.get(Calendar.MONTH)==cal2.get(Calendar.MONTH)
//						&&cal1.get(Calendar.DATE)==cal2.get(Calendar.DATE)) {
//						resMap.put("limit_flg", "×");
//						//invalid_money+=rs.getInt("detail_value_no_tax");
//						invalid_money+=rs.getInt("product_value");
//					}
				}

				SystemInfo.getLogger().info(resMap.toString());
                resultList.add(resMap);

            }
            rs.close();

			//---------------------------------------------------------------------------------
			// ①前月稼働人数
			//---------------------------------------------------------------------------------
			sql = new StringBuilder(4000);
			sql.append(" select ");
			sql.append(" course_class_id ");
			sql.append(" ,count(customer_id) as cnt ");
			sql.append(" from (select distinct ");
			sql.append(" mc.course_class_id, ds.customer_id");
			sql.append(" from data_contract_digestion dcd ");
			sql.append(" inner join data_sales_detail dsd ");
			sql.append("  on  dcd.shop_id = dsd.shop_id and dcd.slip_no = dsd.slip_no ");
			sql.append("  and dcd.contract_no = dsd.contract_no and dcd.contract_detail_no = dsd.contract_detail_no ");
			sql.append("  and dsd.product_division = 6 ");
			sql.append(" inner join data_sales ds ");
			sql.append("  on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and ds.delete_date is null ");
			sql.append(" inner join mst_course mc ");
			sql.append("  on dsd.product_id = mc.course_id ");
			//sql.append(" and mc.course_class_id in (1,2,7,8,9,22,23) ");
			sql.append(" where dcd.product_num > 0 ");
			sql.append("  and sales_date < '").append(sdf.format(baseDate)).append("' ");
			sql.append("  and sales_date >= '").append(sdf.format(bef3m)).append("' ");
			sql.append("  and ds.shop_id =").append(shopList);
			sql.append(" group by course_class_id, ds.customer_id, dcd.contract_shop_id, dcd.contract_no, dcd.contract_detail_no) sum ");
			sql.append(" group by course_class_id ");

			SystemInfo.getLogger().info(sql.toString());
		    rs = SystemInfo.getConnection().executeQuery(sql.toString());
			Map<Integer, Integer> summaryMap = new HashMap<Integer, Integer>();
            while (rs.next()) {
				summaryMap.put(rs.getInt("course_class_id"), rs.getInt("cnt"));
			}
			summaryList.add(summaryMap);
            rs.close();
			//---------------------------------------------------------------------------------
			// ②"未"来店引上げ人数
			//---------------------------------------------------------------------------------
			sql = new StringBuilder(4000);
			sql.append("	select");
			sql.append("	course_class_id");
			sql.append("	,count(customer_id) as cnt");
			sql.append("	from");
			sql.append("	(select distinct  a.course_class_id, a.customer_id from");
			sql.append("	(select distinct");
			sql.append("	mc.course_class_id, ds.customer_id, dcd.contract_shop_id, dcd.contract_no, dcd.contract_detail_no");
			sql.append("	from data_contract_digestion dcd");
			sql.append("	inner join data_sales_detail dsd");
			sql.append("	 on  dcd.shop_id = dsd.shop_id");
			sql.append("	 and dcd.slip_no = dsd.slip_no");
			sql.append("	 and dcd.contract_no = dsd.contract_no");
			sql.append("	 and dcd.contract_detail_no = dsd.contract_detail_no");
			sql.append("	 and dsd.product_division = 6");
			sql.append("	inner join data_sales ds");
			sql.append("	 on ds.shop_id = dsd.shop_id");
			sql.append("	 and ds.slip_no = dsd.slip_no ");
			sql.append("     and ds.shop_id =").append(shopList);
			sql.append("	inner join mst_course mc ");
			sql.append("	 on dsd.product_id = mc.course_id");
			//sql.append("	and mc.course_class_id in (1,2,7,8,9,22,23)");
			sql.append("	where dcd.product_num > 0");
			sql.append("	 and sales_date >= '").append(sdf.format(baseDate)).append("'");
			sql.append("	 and sales_date <= '").append(sdf.format(targetDate)).append("'");
			sql.append("	group by course_class_id, ds.customer_id, dcd.contract_shop_id, dcd.contract_no, dcd.contract_detail_no) a");
			sql.append("	left outer join");
			sql.append("	(select distinct");
			sql.append("	mc.course_class_id, ds.customer_id, dcd.contract_shop_id, dcd.contract_no, dcd.contract_detail_no");
			sql.append("	from data_contract_digestion dcd");
			sql.append("	inner join data_sales_detail dsd");
			sql.append("	 on  dcd.shop_id = dsd.shop_id");
			sql.append("	 and dcd.slip_no = dsd.slip_no");
			sql.append("	 and dcd.contract_no = dsd.contract_no");
			sql.append("	 and dcd.contract_detail_no = dsd.contract_detail_no");
			sql.append("	 and dsd.product_division = 6");
			sql.append("	inner join data_sales ds");
			sql.append("	 on ds.shop_id = dsd.shop_id");
			sql.append("	 and ds.slip_no = dsd.slip_no");
			sql.append("     and ds.shop_id =").append(shopList);
			sql.append("	inner join mst_course mc ");
			sql.append("	 on dsd.product_id = mc.course_id");
			//sql.append("	and mc.course_class_id in (1,2,7,8,9,22,23)");
			sql.append("	where dcd.product_num > 0");
			sql.append("	 and sales_date < '").append(sdf.format(baseDate)).append("'");
			sql.append("	 and sales_date >= '").append(sdf.format(bef3m)).append("'");
			sql.append("	group by course_class_id, ds.customer_id, dcd.contract_shop_id, dcd.contract_no, dcd.contract_detail_no) b");
			sql.append("	on a.course_class_id = b.course_class_id");
			sql.append("	and a.customer_id = b.customer_id");
			sql.append("	and a.contract_no = b.contract_no");
			sql.append("	and a.contract_detail_no = b.contract_detail_no");
			sql.append("	where b.customer_id is null) aa");
			sql.append("	group by course_class_id");

			SystemInfo.getLogger().info(sql.toString());
		    rs = SystemInfo.getConnection().executeQuery(sql.toString());
			summaryMap = new HashMap<Integer, Integer>();
            while (rs.next()) {
				summaryMap.put(rs.getInt("course_class_id"), rs.getInt("cnt"));
			}
			summaryList.add(summaryMap);
            rs.close();
			
			//---------------------------------------------------------------------------------
			// ③新規・継続人数
			//---------------------------------------------------------------------------------
			sql = new StringBuilder(4000);
			 sql.append(" select " );
			 sql.append("   course_class_id " );
			 sql.append("   , count(customer_id) as cnt  " );
			 sql.append(" from " );
			 sql.append("  (select " );
			 sql.append("   ds.sales_date " );
			 sql.append(" , ds.customer_id " );
			 sql.append(" , course_class_id " );
			 sql.append(" , ds.shop_id " );
			 sql.append(" , dcd.contract_shop_id " );
			 sql.append(" , dcd.contract_no " );
			 sql.append(" , dcd.contract_detail_no " );
			 sql.append(" , counter.cnt " );
			 sql.append(" from " );
			 sql.append(" data_contract_digestion dcd  " );
			 sql.append(" inner join data_sales_detail dsd  " );
			 sql.append("   on  dcd.shop_id = dsd.shop_id  " );
			 sql.append("   and dcd.slip_no = dsd.slip_no  " );
			 sql.append("   and dcd.contract_no = dsd.contract_no  " );
			 sql.append("   and dcd.contract_detail_no = dsd.contract_detail_no  " );
			 sql.append("   and dsd.product_division = 6  " );
			 sql.append("   and dsd.delete_date is null " );
			 sql.append(" inner join data_sales ds  " );
			 sql.append("   on  ds.shop_id = dsd.shop_id  " );
			 sql.append("   and ds.slip_no = dsd.slip_no  " );
			 sql.append("   and ds.delete_date is null " );
			 sql.append("    inner join mst_course mc  ");
			 sql.append("   on dsd.product_id = mc.course_id  ");
			 //sql.append("   and mc.course_class_id in (1, 2, 7, 8, 9, 22, 23)  ");
			 sql.append("    left outer join (select ");
			 sql.append("  dcd.contract_shop_id ");
			 sql.append("   , dcd.contract_no ");
			 sql.append("   , dcd.contract_detail_no ");
			 sql.append("   , count(dcd.slip_no) as cnt  ");
			 sql.append("    from ");
			 sql.append("   data_contract_digestion dcd  ");
			 sql.append("   inner join data_sales_detail dsd  ");
			 sql.append("  on dcd.shop_id = dsd.shop_id  ");
			 sql.append("  and dcd.slip_no = dsd.slip_no  ");
			 sql.append("  and dcd.contract_no = dsd.contract_no  ");
			 sql.append("  and dcd.contract_detail_no = dsd.contract_detail_no  ");
			 sql.append("  and dsd.product_division = 6  ");
			 sql.append("  and dsd.delete_date is null ");
			 sql.append("   inner join data_sales ds  ");
			 sql.append("  on ds.shop_id = dsd.shop_id  ");
			 sql.append("  and ds.slip_no = dsd.slip_no  ");
			 sql.append("  and ds.delete_date is null ");
			 sql.append("   inner join mst_course mc  ");
			 sql.append("  on dsd.product_id = mc.course_id  ");
			 //sql.append("  and mc.course_class_id in (1, 2, 7, 8, 9, 22, 23)  ");
			 sql.append("    where ");
			 sql.append("   dcd.product_num > 0  ");
			 sql.append("    group by ");
			 sql.append("  dcd.contract_shop_id ");
			 sql.append("   , dcd.contract_no ");
			 sql.append("   , dcd.contract_detail_no ");
			 sql.append("  ) counter on counter.contract_shop_id = dcd.contract_shop_id ");
			 sql.append("  and counter.contract_no = dcd.contract_no ");
			 sql.append("  and counter.contract_detail_no = dcd.contract_detail_no ");
			 sql.append("   where ");
			 sql.append("    ds.shop_id =").append(shopList);
			 sql.append(" and sales_date >= '").append(sdf.format(baseDate)).append("'");
			 sql.append(" and sales_date <= '").append(sdf.format(targetDate)).append("'");
			 sql.append(" ) base ");
			 sql.append(" where ");
			 sql.append("  base.cnt = 1  ");
			 sql.append(" group by course_class_id ");

			SystemInfo.getLogger().info(sql.toString());
			rs = SystemInfo.getConnection().executeQuery(sql.toString());
			summaryMap = new HashMap<Integer, Integer>();
            while (rs.next()) {
				summaryMap.put(rs.getInt("course_class_id"), rs.getInt("cnt"));
			}
			summaryList.add(summaryMap);
            rs.close();
			
			//---------------------------------------------------------------------------------
			// ④終了人数
			//---------------------------------------------------------------------------------
			sql = new StringBuilder(4000);
			 sql.append(" select " );
			 sql.append("   course_class_id " );
			 sql.append("   , count(customer_id) as cnt  " );
			 sql.append(" from " );
			 sql.append("  (select " );
			 sql.append("   ds.sales_date " );
			 sql.append(" , ds.customer_id " );
			 sql.append(" , course_class_id " );
			 sql.append(" , ds.shop_id " );
			 sql.append(" , dcd.contract_shop_id " );
			 sql.append(" , dcd.contract_no " );
			 sql.append(" , dcd.contract_detail_no " );
			 sql.append(" , counter.cnt " );
			 sql.append(" from " );
			 sql.append(" data_contract_digestion dcd  " );
			 sql.append(" inner join data_sales_detail dsd  " );
			 sql.append("   on  dcd.shop_id = dsd.shop_id  " );
			 sql.append("   and dcd.slip_no = dsd.slip_no  " );
			 sql.append("   and dcd.contract_no = dsd.contract_no  " );
			 sql.append("   and dcd.contract_detail_no = dsd.contract_detail_no  " );
			 sql.append("   and dsd.product_division = 6  " );
			 sql.append("   and dsd.delete_date is null " );
			 sql.append(" inner join data_sales ds  " );
			 sql.append("   on  ds.shop_id = dsd.shop_id  " );
			 sql.append("   and ds.slip_no = dsd.slip_no  " );
			 sql.append("   and ds.delete_date is null " );
			 sql.append("    inner join mst_course mc  ");
			 sql.append("   on dsd.product_id = mc.course_id  ");
			 //sql.append("   and mc.course_class_id in (1, 2, 7, 8, 9, 22, 23)  ");
			 sql.append("    left outer join (select ");
			 sql.append("  dcd.contract_shop_id ");
			 sql.append("   , dcd.contract_no ");
			 sql.append("   , dcd.contract_detail_no ");
			 sql.append("   , count(dcd.product_num) as cnt  ");
			 sql.append("    from ");
			 sql.append("   data_contract_digestion dcd  ");
			 sql.append("   inner join data_sales_detail dsd  ");
			 sql.append("  on dcd.shop_id = dsd.shop_id  ");
			 sql.append("  and dcd.slip_no = dsd.slip_no  ");
			 sql.append("  and dcd.contract_no = dsd.contract_no  ");
			 sql.append("  and dcd.contract_detail_no = dsd.contract_detail_no  ");
			 sql.append("  and dsd.product_division = 6  ");
			 sql.append("  and dsd.delete_date is null ");
			 sql.append("   inner join data_sales ds  ");
			 sql.append("  on ds.shop_id = dsd.shop_id  ");
			 sql.append("  and ds.slip_no = dsd.slip_no  ");
			 sql.append("  and ds.delete_date is null ");
			 sql.append("   inner join mst_course mc  ");
			 sql.append("  on dsd.product_id = mc.course_id  ");
			 //sql.append("  and mc.course_class_id in (1, 2, 7, 8, 9, 22, 23)  ");
			 sql.append("    where ");
			 sql.append("   dcd.product_num > 0  ");
			 sql.append("    group by ");
			 sql.append("  dcd.contract_shop_id ");
			 sql.append("   , dcd.contract_no ");
			 sql.append("   , dcd.contract_detail_no ");
			 sql.append("  ) counter on counter.contract_shop_id = dcd.contract_shop_id ");
			 sql.append("  and counter.contract_no = dcd.contract_no ");
			 sql.append("  and counter.contract_detail_no = dcd.contract_detail_no ");
			 sql.append("   where ");
			 sql.append("     ds.shop_id =").append(shopList);
			 sql.append(" and mc.num = counter.cnt and sales_date >= '").append(sdf.format(baseDate)).append("'");
			 sql.append(" and sales_date <= '").append(sdf.format(targetDate)).append("'");
			 sql.append(" ) base ");
			 sql.append(" group by course_class_id ");

			SystemInfo.getLogger().info(sql.toString());
			rs = SystemInfo.getConnection().executeQuery(sql.toString());
			summaryMap = new HashMap<Integer, Integer>();
            while (rs.next()) {
				summaryMap.put(rs.getInt("course_class_id"), rs.getInt("cnt"));
			}
			summaryList.add(summaryMap);
            rs.close();
			
			// ダミー行
			summaryList.add(new HashMap<Integer, Integer>());
			
			//---------------------------------------------------------------------------------
			// 今月稼動償却人数
			//---------------------------------------------------------------------------------
			sql = new StringBuilder(4000);
			sql.append(" select  ");
			sql.append(" course_class_id  ");
			sql.append(" ,count(customer_id) as cnt  ");
			sql.append(" from (select  ");
			sql.append(" mc.course_class_id, ds.customer_id, count(dcd.slip_no) as dcnt  ");
			sql.append(" from data_contract_digestion dcd  ");
			sql.append(" inner join data_sales_detail dsd  ");
			sql.append("  on  dcd.shop_id = dsd.shop_id  ");
			sql.append("  and dcd.slip_no = dsd.slip_no  ");
			sql.append("  and dcd.contract_no = dsd.contract_no  ");
			sql.append("  and dcd.contract_detail_no = dsd.contract_detail_no  ");
			sql.append("  and dsd.product_division = 6 and dsd.delete_date is null ");
			sql.append(" inner join data_sales ds  ");
			sql.append("  on ds.shop_id = dsd.shop_id  ");
			sql.append("  and ds.slip_no = dsd.slip_no and ds.delete_date is null ");
			sql.append(" inner join mst_course mc ");
			sql.append("  on dsd.product_id = mc.course_id  ");
			//sql.append(" and mc.course_class_id in (1,2,7,8,9,22,23)  ");
			sql.append(" where  ");
			sql.append("  dcd.product_num > 0  ");
			sql.append(" and ds.shop_id =").append(shopList);
			sql.append(" and sales_date >= '").append(sdf.format(baseDate)).append("'");
			sql.append(" and sales_date <= '").append(sdf.format(targetDate)).append("'");
			sql.append(" group by course_class_id, ds.customer_id) sum  ");
			sql.append(" group by course_class_id  ");

			SystemInfo.getLogger().info(sql.toString());
			rs = SystemInfo.getConnection().executeQuery(sql.toString());
			summaryMap = new HashMap<Integer, Integer>();
            while (rs.next()) {
				summaryMap.put(rs.getInt("course_class_id"), rs.getInt("cnt"));
			}
			summaryList.add(summaryMap);
            rs.close();
			
			//---------------------------------------------------------------------------------
			// ２回以上償却した人数
			//---------------------------------------------------------------------------------
			sql = new StringBuilder(4000);
			sql.append(" select  ");
			sql.append(" course_class_id  ");
			sql.append(" ,count(customer_id) as cnt  ");
			sql.append(" from (select  ");
			sql.append(" mc.course_class_id, ds.customer_id, count(dcd.slip_no) as dcnt  ");
			sql.append(" from data_contract_digestion dcd  ");
			sql.append(" inner join data_sales_detail dsd  ");
			sql.append("  on  dcd.shop_id = dsd.shop_id  ");
			sql.append("  and dcd.slip_no = dsd.slip_no  ");
			sql.append("  and dcd.contract_no = dsd.contract_no  ");
			sql.append("  and dcd.contract_detail_no = dsd.contract_detail_no  ");
			sql.append("  and dsd.product_division = 6  and dsd.delete_date is null ");
			sql.append(" inner join data_sales ds  ");
			sql.append("  on ds.shop_id = dsd.shop_id  ");
			sql.append("  and ds.slip_no = dsd.slip_no  and ds.delete_date is null ");
			sql.append(" inner join mst_course mc   ");
			sql.append("  on dsd.product_id = mc.course_id  ");
			//sql.append(" and mc.course_class_id in (1,2,7,8,9,22,23)  ");
			sql.append(" where  ");
			sql.append("  dcd.product_num > 0  ");
			sql.append(" and ds.shop_id =").append(shopList);
			sql.append(" and sales_date >= '").append(sdf.format(baseDate)).append("'");
			sql.append(" and sales_date <= '").append(sdf.format(targetDate)).append("'");
			sql.append(" group by course_class_id, ds.customer_id) sum  ");
			sql.append(" where dcnt >1 ");
			sql.append(" group by course_class_id  ");

			SystemInfo.getLogger().info(sql.toString());
			rs = SystemInfo.getConnection().executeQuery(sql.toString());
			summaryMap = new HashMap<Integer, Integer>();
            while (rs.next()) {
				summaryMap.put(rs.getInt("course_class_id"), rs.getInt("cnt"));
			}
			summaryList.add(summaryMap);
            rs.close();

			//---------------------------------------------------------------------------------
			// スタッフ
			//---------------------------------------------------------------------------------
			sql = new StringBuilder(4000);
			sql.append(" select ");
			sql.append(" ms.staff_id, ms.staff_name1, ms.staff_name2");
			sql.append(" from ");
			sql.append(" data_contract_digestion dcd  ");
			sql.append(" inner join data_sales_detail dsd  ");
			sql.append(" on dcd.shop_id = dsd.shop_id and dcd.slip_no = dsd.slip_no  ");
			sql.append(" and dcd.contract_no = dsd.contract_no and dcd.contract_detail_no = dsd.contract_detail_no  ");
			sql.append(" and dsd.product_division = 6 and dsd.delete_date is null ");
			sql.append(" inner join data_sales ds  ");
			sql.append(" on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and ds.delete_date is null ");
			sql.append(" inner join mst_staff ms ");
			sql.append(" on dcd.staff_id = ms.staff_id  ");
			sql.append(" where ");
			sql.append(" dcd.product_num > 0  ");
			sql.append(" and ds.shop_id =").append(shopList);
			sql.append(" and sales_date >= '").append(sdf.format(baseDate)).append("'");
			sql.append(" and sales_date <= '").append(sdf.format(targetDate)).append("'");
			sql.append(" group by ");
			sql.append(" ms.display_seq, ms.shop_id, ms.staff_id, ms.staff_name1, ms.staff_name2 ");
			sql.append("  order by ms.display_seq, ms.shop_id ");
			SystemInfo.getLogger().info(sql.toString());
			rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
				if(1 > rs.getString("staff_name1").indexOf('/')
						&& 1 > rs.getString("staff_name2").indexOf('/')
						&& 1 > rs.getString("staff_name1").indexOf('／')
						&& 1 > rs.getString("staff_name2").indexOf('／')) {
					staffNames.add(rs.getString("staff_name1"));
				}
			}
            rs.close();
			
			//---------------------------------------------------------------------------------
			// 月の集計
			//---------------------------------------------------------------------------------
			sql = new StringBuilder(4000);
			sql.append(" select ");
			sql.append("     count(distinct customer_id) as a ");
			sql.append("   , count(distinct dsd.slip_no || '' ||  dsd.slip_detail_no) as b ");
			sql.append("   , sum( sign( ( (dcd.product_num * (dcc.product_value/mcr.num)) / (1.0 + get_tax_rate(ds.sales_date))))  ");
			sql.append("     * ceil( abs( ( (dcd.product_num * (dcc.product_value/mcr.num)) / (1.0 + get_tax_rate(ds.sales_date))))) ");
			sql.append("     ) AS val ");
			sql.append(" from ");
			sql.append("   data_contract_digestion dcd ");
			sql.append("   inner join data_sales_detail dsd ");
			sql.append("     on dcd.shop_id = dsd.shop_id and dcd.slip_no = dsd.slip_no ");
			sql.append("     and dcd.contract_no = dsd.contract_no and dcd.contract_detail_no = dsd.contract_detail_no ");
			sql.append("     and dsd.product_division = 6  and dsd.delete_date is null ");
			sql.append("   inner join data_sales ds ");
			sql.append("     on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and ds.delete_date is null ");
			sql.append("   inner join mst_staff ms on dcd.staff_id = ms.staff_id ");
			sql.append("   inner join data_contract dcc on  dcd.contract_shop_id = dcc.shop_id and dcd.contract_no = dcc.contract_no and dcd.contract_detail_no = dcc.contract_detail_no ");
			sql.append("   inner join mst_course mcr on dsd.product_id = mcr.course_id ");
			sql.append(" where ");
			sql.append(" ds.shop_id =").append(shopList);
			sql.append(" and sales_date >= '").append(sdf.format(baseDate)).append("'");
			sql.append(" and sales_date <= '").append(sdf.format(targetDate)).append("'");
			SystemInfo.getLogger().info(sql.toString());
			rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
				mdata[0] = rs.getLong("a");
				mdata[1] = rs.getLong("b");
				mdata[2] = rs.getLong("val");
			}
            rs.close();
			
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
		
		JPOIApi jx = new JPOIApi("役務償却日報");
        jx.setTemplateFile("/com/geobeck/sosia/pos/report/custom/modesty_daily_report.xls");

		SimpleDateFormat printSdf = new SimpleDateFormat("yyyy年MM月dd日（E）");
		jx.setCellValue( 1, 1, printSdf.format(targetDate));	
		jx.setCellValue(18, 1, shopName);	
		jx.setCellValue( 8, 3, cIds.size());	
		//jx.setCellValue(11, 3, resultList.size());
		jx.setCellValue( 3, 4, money);
		//小計
		jx.setCellValue(  7, 27, cIds.size());
		jx.setCellValue( 15, 27, money);
		jx.setCellValue( 22, 27, money_tax-invalid_money);
		
		jx.setCellValue( 20, 3, mdata[0]);
		//jx.setCellValue( 23, 3, mdata[1]);
		jx.setCellValue( 15, 4, mdata[2]);
		
		int row=7;
		int addCnt=0;
		for(int i = 20; i <=resultList.size(); i++) {
			jx.copyRow(9, 10);
			addCnt++;
		}
		//　一覧
		for(int i = 0; i < resultList.size(); i++) {
			Map<String, Object> nowRow = resultList.get(i);
			jx.setCellValue(1, row+i, i+1);
			jx.setCellValue(2, row+i, (String)nowRow.get("customer_name1"));
			jx.setCellValue(4, row+i, (String)nowRow.get("customer_name2"));
			jx.setCellValue(6, row+i, (String)nowRow.get("course_name"));
			jx.setCellValue(10, row+i, ((BigDecimal)nowRow.get("num")).doubleValue());
			jx.setCellValue(12, row+i, (Integer)nowRow.get("detail_value_no_tax"));
			jx.setCellValue(14, row+i, (String)nowRow.get("staff_name"));
			jx.setCellValue(18, row+i, ((BigDecimal)nowRow.get("used")).doubleValue());
			jx.setCellValue(20, row+i, ((BigDecimal)nowRow.get("num")).subtract((BigDecimal)nowRow.get("used")).doubleValue());
			jx.setCellValue(22, row+i, (0<(int)nowRow.get("can2"))?"当キャン②":"");
			jx.setCellValue(25, row+i, (String)nowRow.get("limit_flg"));
		}
		
		//プリモの場合　フェイシャル　スキャルプ　メモリアル　プリスティン　コダマ　スタンダード
		if(shop.getGroupID() == 2) {
			 jx.setCellValue( 6, 29+addCnt, "フェイシャル");
			 jx.setCellValue( 8, 29+addCnt, "スキャルプ");
			 jx.setCellValue(10, 29+addCnt, "メモリアル");
			 jx.setCellValue(12, 29+addCnt, "プリスティン");
			 jx.setCellValue(14, 29+addCnt, "コダマ");
			 jx.setCellValue(16, 29+addCnt, "スタンダード");
			 // 分類別集計　30,6～
			for(int i = 0; i < summaryList.size(); i++) {
				if (i==4) {	continue;}
				Map<Integer, Integer> nowRow = summaryList.get(i);
				// スタンダード
				if(nowRow.containsKey(1)) {	jx.setCellValue(16, 30+addCnt+i, nowRow.get(1));
				} else { jx.setCellValue(16, 30+addCnt+i, 0); }
				//フェイシャル
				if(nowRow.containsKey(2)) { jx.setCellValue(6, 30+addCnt+i, nowRow.get(2));
				} else { jx.setCellValue(6, 30+addCnt+i, 0); }
				//スキャルプ
				if(nowRow.containsKey(7)) { jx.setCellValue(8, 30+addCnt+i, nowRow.get(7));
				}else { jx.setCellValue(8, 30+addCnt+i, 0);}
				//コダマ
				if(nowRow.containsKey(8)) { jx.setCellValue(14, 30+addCnt+i, nowRow.get(8));
				}else { jx.setCellValue(14, 30+addCnt+i, 0); }
				//メモリアル
				if(nowRow.containsKey(9)) { jx.setCellValue(10, 30+addCnt+i, nowRow.get(9));
				} else { jx.setCellValue(10, 30+addCnt+i, 0);}
				//プリスティン
				if(nowRow.containsKey(22) || nowRow.containsKey(23)) {
					int nm = ((nowRow.containsKey(22)?nowRow.get(22):0)
								+(nowRow.containsKey(23)?nowRow.get(23):0));
					jx.setCellValue(12, 30+addCnt+i, nm);
				} else { jx.setCellValue(12, 30+addCnt+i, 0);}
			}
		} else {
		// スタイル ストロング フェザー ミックス アロマ ピーリング キープ スタンダード メモリアル 旧コース
			 jx.setCellValue( 6, 29+addCnt, "スタイル");
			 jx.setCellValue( 8, 29+addCnt, "ストロング");
			 jx.setCellValue(10, 29+addCnt, "フェザー");
			 jx.setCellValue(12, 29+addCnt, "ミックス");
			 jx.setCellValue(14, 29+addCnt, "アロマ");
			 jx.setCellValue(16, 29+addCnt, "ピーリング");
			 jx.setCellValue(18, 29+addCnt, "キープ");
			 jx.setCellValue(20, 29+addCnt, "スタンダード");
			 jx.setCellValue(22, 29+addCnt, "メモリアル");
			 jx.setCellValue(24, 29+addCnt, "旧コース");
			// 分類別集計　30,6～
			for(int i = 0; i < summaryList.size(); i++) {
				if (i==4) {	continue;}
				Map<Integer, Integer> nowRow = summaryList.get(i);
				// スタイル
				if(nowRow.containsKey(18)) {	jx.setCellValue( 6, 30+addCnt+i, nowRow.get(18));
				} else { jx.setCellValue( 6, 30+addCnt+i, 0); }
				//ストロング
				if(nowRow.containsKey(16)) { jx.setCellValue(8, 30+addCnt+i, nowRow.get(16));
				} else { jx.setCellValue(8, 30+addCnt+i, 0); }
				//フェザー
				if(nowRow.containsKey(17)) { jx.setCellValue(10, 30+addCnt+i, nowRow.get(17));
				}else { jx.setCellValue(10, 30+addCnt+i, 0);}
				//ミックス
				if(nowRow.containsKey(19)) { jx.setCellValue(12, 30+addCnt+i, nowRow.get(19));
				}else { jx.setCellValue(12, 30+addCnt+i, 0); }
				//アロマ
				if(nowRow.containsKey(24) || nowRow.containsKey(25)) {
					int nm = ((nowRow.containsKey(24)?nowRow.get(24):0)
								+(nowRow.containsKey(25)?nowRow.get(25):0));
					jx.setCellValue(14, 30+addCnt+i, nm);
				} else { jx.setCellValue(14, 30+addCnt+i, 0);}
				//ピーリング
				if(nowRow.containsKey(15)) { jx.setCellValue(16, 30+addCnt+i, nowRow.get(15));
				} else { jx.setCellValue(16, 30+addCnt+i, 0);}
				//キープ
				if(nowRow.containsKey(14)) { jx.setCellValue(18, 30+addCnt+i, nowRow.get(14));
				} else { jx.setCellValue(18, 30+addCnt+i, 0);}
				//スタンダード
				if(nowRow.containsKey(1)) { jx.setCellValue(20, 30+addCnt+i, nowRow.get(1));
				} else { jx.setCellValue(20, 30+addCnt+i, 0);}
				//メモリアル
				if(nowRow.containsKey(9)) { jx.setCellValue(22, 30+addCnt+i, nowRow.get(9));
				} else { jx.setCellValue(22, 30+addCnt+i, 0);}
				//旧コース
				if(nowRow.containsKey(11) || nowRow.containsKey(12)|| nowRow.containsKey(13)) {
					int nm = ((nowRow.containsKey(11)?nowRow.get(11):0)
								+(nowRow.containsKey(12)?nowRow.get(12):0)
								+(nowRow.containsKey(13)?nowRow.get(13):0));
					jx.setCellValue(24, 30+addCnt+i, nm);
				} else { jx.setCellValue(24, 30+addCnt+i, 0);}
			}
		}

		int i =0;
		for(String stff: staffNames) {
			 jx.setCellValue( 8+(i*2), 38+addCnt, stff);
			 i++;
		}

		jx.openWorkbook();
		return true;
	}
	
	
	/**
	 * 販売実績
	 * @param shop
	 * @param targetDate
	 * @return 
	 */
	protected boolean printPaymentReport(MstShop shop, Date targetDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		
		String shopList = shop.getShopID()+"";
		String shopName = shop.getShopName();

		Map<String, Map<String, Object>> checkMap = new HashMap<String, Map<String, Object>>(0);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>(0);
		List<Map<String, Object>> resultList2 = new ArrayList<Map<String, Object>>(0);
		List<Map<String, Object>> outList = new ArrayList<Map<String, Object>>(0);
		
		Map<Integer, Map<String, String>> payMap = new HashMap<Integer, Map<String, String>>(0);
		
		Integer regCash = 0;
		String regStaff = "";
		try {
			//出金一覧
			StringBuilder sql = new StringBuilder(4000);
			sql.append("select use_for ,ms.staff_name1 || ms.staff_name2 as staff ,io_value");
			sql.append(" from data_cash_io cash");
			sql.append(" left outer join mst_staff ms on cash.staff_id = ms.staff_id");
			sql.append(" where in_out = false");
			sql.append(" and cash.shop_id =").append(shopList);
			sql.append(" and io_date = '"+sdf.format(targetDate)+"' and cash.delete_date is null ");
			sql.append(" order by io_no");
			SystemInfo.getLogger().info(sql.toString());
			ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
				Map<String, Object> resMap = new  HashMap<String, Object>();
				resMap.put("use_for", rs.getString("use_for"));
				resMap.put("staff", rs.getString("staff"));
				resMap.put("io_value", rs.getInt("io_value"));
				outList.add(resMap);
			}
			// 前日のレジデータ
			Calendar td = Calendar.getInstance();
			td.setTime(targetDate);
			td.add(Calendar.DATE, -1);
			sql = new StringBuilder(4000);
			sql.append("select ( money_1 + money_5*5 + money_10*10 + money_50*50 ");
			sql.append("+ money_100*100 + money_500*500 + money_1000*1000 ");
			sql.append("+ money_2000*2000 + money_5000*5000 + money_10000*10000) as regi_money");
			sql.append(", (select staff_name1 || staff_name2 from mst_staff ms where ms.staff_id = dr.staff_id) as staff_name");
			sql.append(" from data_register dr ");
			sql.append(" where");
			sql.append(" shop_id =").append(shopList);
			sql.append(" and manage_date = '"+sdf.format(td.getTime())+"' ");
			SystemInfo.getLogger().info(sql.toString());
			rs = SystemInfo.getConnection().executeQuery(sql.toString());
			while (rs.next()) {
				regCash= rs.getInt("regi_money");
				regStaff =rs.getString("staff_name");
			}

			//支払いマスタ
			sql = new StringBuilder(4000);
			sql.append("select mpm.payment_method_id, mpm.payment_method_name, mpm.payment_class_id, mpc.payment_class_name");
			sql.append(" from mst_payment_method mpm inner join mst_payment_class mpc on mpm.payment_class_id = mpc.payment_class_id");
			sql.append(" order by mpm.payment_class_id, mpm.payment_method_id");
			rs = SystemInfo.getConnection().executeQuery(sql.toString());
			while (rs.next()) {
				 Map<String, String> tmpMap = new HashMap<String, String>(0);
				 tmpMap.put("payment_method_name", rs.getString("payment_method_name"));
				 tmpMap.put("payment_class_id", rs.getString("payment_class_id"));
				 tmpMap.put("payment_class_name", rs.getString("payment_class_name"));
				 payMap.put(rs.getInt("payment_method_id"), tmpMap);
			}
			
			//---------------------
			// 判定データの取得
			//---------------------
            sql = new StringBuilder(4000);
			sql.append(" select   ");
			sql.append(" ds.customer_id, first_date, first_date + '1 month' as one_month, ddd.shop_id, ddd.product_division, ddd.product_id, ddd.slip_cnt  ");
			sql.append(" from data_sales ds  ");
			sql.append(" left outer join   ");
			sql.append(" (select min(ds.sales_date) as first_date, ds.customer_id, ds.shop_id, dsd.product_division, dsd.product_id, count(ds.slip_no) as slip_cnt  ");
			sql.append("  from data_sales_detail dsd  ");
			sql.append("  inner join data_sales ds on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and ds.delete_date is null  ");
			sql.append("  where dsd.delete_date is null  ");
			sql.append("  group by ds.customer_id, ds.shop_id, dsd.product_division, dsd.product_id  ");
			sql.append("  order by ds.customer_id, ds.shop_id, dsd.product_division, dsd.product_id) ddd  ");
			sql.append("  on ds.customer_id = ddd.customer_id  ");
			sql.append(" left outer join mst_technic mt on ddd.product_id = mt.technic_id and ddd.product_division in (1,3)   ");
			sql.append(" where  ");
			sql.append(" ds.shop_id =").append(shopList);
			sql.append(" and ds.sales_date = '"+sdf.format(targetDate)+"' ");
			 // 全体割り引き・消化以外 
			sql.append(" and ddd.product_division not in (0, 6, 9)  ");
			 // 1：技術 3：技術クレームで一部技術分類を除く 
			sql.append(" and ( ddd.product_division in (1,3) and mt.technic_class_id not in (5,6,7,8,9,10,11,12,13,16,17,18))  ");
			sql.append(" order by ds.customer_id, ddd.product_division, ddd.product_id, first_date  ");
		
			SystemInfo.getLogger().info(sql.toString());
			rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
				String customerId = rs.getString("customer_id");
				Map<String, Object> customerMap = null;
				if (checkMap.containsKey(customerId)) {
					customerMap = checkMap.get(customerId);
					// 初回来店日が新しければ更新
					if (rs.getDate("first_date").compareTo((Date)customerMap.get("first_date")) < 0) {
						customerMap.put("first_date", rs.getDate("first_date"));
						customerMap.put("one_month", rs.getDate("one_month"));
					}
				} else {
					customerMap = new HashMap<String, Object>(0);
					customerMap.put("first_date", rs.getDate("first_date"));
					customerMap.put("one_month", rs.getDate("one_month"));
				}
				String key = rs.getString("product_division") +"-"+ rs.getString("product_id");
				customerMap.put(key, rs.getInt("slip_cnt"));
				SystemInfo.getLogger().info(customerMap.toString());
				checkMap.put(customerId, customerMap);
			}

			//---------------------
			// 一覧データの取得
			//---------------------
            sql = new StringBuilder(4000);
			sql.append(" select   ");
			sql.append(" ds.sales_date, dsd.product_division, dsd.product_id, dsd.slip_no, dsd.slip_detail_no  ");
			sql.append(" , mc.customer_id, mc.customer_name1, mc.customer_name2  ");
			sql.append(" , ( sign( ( (dsd.product_num * dsd.product_value) / (1.0 + get_tax_rate(ds.sales_date))))   ");
			sql.append("     * ceil( abs( ( (dsd.product_num * dsd.product_value) / (1.0 + get_tax_rate(ds.sales_date)))))) as detail_value_no_tax   ");
			sql.append(" , (dsd.product_num * dsd.product_value) as product_value  ");
			sql.append(" , dp.bill_value  ");
			sql.append(" , payment.pm1, payment.pv1, payment.pm2, payment.pv2, payment.pm3, payment.pv3, payment.pm4, payment.pv4 ");
			sql.append(" , dsd.staff_id  ");
			sql.append(" , mt.technic_class_id, mt.technic_name ");
			sql.append(" , mi.item_class_id, mi.item_name ");
			sql.append(" , mcc.course_class_id, mcc.course_name ");
			sql.append(" ,(select ms.staff_name1 || ms.staff_name2 from mst_staff ms where ms.staff_id = dsd.staff_id) as staff ");
			sql.append(" , total ,dp.change_value ,mt.technic_class_id ");
			sql.append(" from ");
			sql.append(" data_sales_detail dsd  ");
			sql.append(" inner join data_sales ds on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and ds.delete_date is null  ");
			sql.append(" inner join data_payment dp on dp.shop_id = ds.shop_id and dp.slip_no = ds.slip_no and dp.payment_no = 0 and dp.delete_date is null  ");
			sql.append(" inner join mst_customer mc on ds.customer_id = mc.customer_id  ");
			sql.append(" inner join ( select ds.shop_id, ds.sales_date, ds.slip_no, dp.payment_no, ds.customer_id");
			sql.append(" , dp.staff_id, dp.bill_value, dp.change_value");
			sql.append(" , p1.payment_method_id as pm1, p1.payment_value as pv1, p2.payment_method_id as pm2, p2.payment_value as pv2");
			sql.append(" , p3.payment_method_id as pm3, p3.payment_value as pv3, p4.payment_method_id as pm4, p4.payment_value as pv4");
			sql.append(" from data_sales ds inner join data_payment dp on ds.shop_id = dp.shop_id and  ds.slip_no = dp.slip_no and dp.payment_no = 0");
			sql.append(" left outer join data_payment_detail p1 on p1.payment_detail_no = 1 and ds.shop_id = p1.shop_id and  ds.slip_no = p1.slip_no and dp.payment_no = p1.payment_no");
			sql.append(" left outer join data_payment_detail p2 on p2.payment_detail_no = 2 and ds.shop_id = p2.shop_id and  ds.slip_no = p2.slip_no and dp.payment_no = p2.payment_no");
			sql.append(" left outer join data_payment_detail p3 on p3.payment_detail_no = 3 and ds.shop_id = p3.shop_id and  ds.slip_no = p3.slip_no and dp.payment_no = p3.payment_no");
			sql.append(" left outer join data_payment_detail p4 on p4.payment_detail_no = 4 and ds.shop_id = p4.shop_id and  ds.slip_no = p4.slip_no and dp.payment_no = p4.payment_no");
			sql.append(" where ds.sales_date = '"+sdf.format(targetDate)+"' ) payment on dp.shop_id = payment.shop_id and dp.slip_no = payment.slip_no and dp.payment_no = payment.payment_no ");
			sql.append(" left outer join mst_technic mt on dsd.product_id = mt.technic_id and dsd.product_division in (1,3)   ");
			sql.append(" left outer join mst_item mi on dsd.product_id = mi.item_id and dsd.product_division in (2,4)   ");
			sql.append(" left outer join mst_course mcc on dsd.product_id = mcc.course_id and dsd.product_division in (5,6,7,8) ");
			sql.append(" left outer join (select dx.shop_id, dx.slip_no, SUM(dx.product_value) as total from data_sales_detail dx group by dx.shop_id, dx.slip_no) dsdd on dsdd.shop_id = ds.shop_id and dsdd.slip_no = ds.slip_no   ");
			sql.append(" where ");
			sql.append(" ds.shop_id =").append(shopList);
			sql.append(" and ds.sales_date = '"+sdf.format(targetDate)+"' ");
			// --全体割り引き・消化以外  1：技術 3：技術クレームで一部技術分類を除く 
			sql.append(" and ( dsd.product_division in (1, 2, 3, 4, 5, 7, 8) and (mt.technic_class_id is null or mt.technic_class_id not in (5, 6, 7, 8, 9, 10, 11, 12, 13, 16, 17, 18)))  ");
			sql.append(" order by dsd.slip_no, dsd.staff_id, dsd.slip_detail_no");
			SystemInfo.getLogger().info(sql.toString());
			rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
				int cash = 0;
				int voucher = 0;
				Map<String, Object> resMap = new  HashMap<String, Object>();

				String productName = (rs.getString("technic_name")!=null)?rs.getString("technic_name")
										:(rs.getString("item_name")!=null)?rs.getString("item_name")
										:rs.getString("course_name");
				resMap.put("slip_no", rs.getInt("slip_no"));
				resMap.put("productName", productName);
				resMap.put("customerName", rs.getString("customer_name1") + rs.getString("customer_name2"));
				resMap.put("detailValueNoTax", rs.getInt("detail_value_no_tax"));
				resMap.put("tax", rs.getInt("product_value") - rs.getInt("detail_value_no_tax"));
				
				//支払明細
				for(int i=1; i<=4; i++) {
					if (rs.getString("pm"+i)!= null && !rs.getString("pm"+i).equals("")) {
						Map<String, String> tmpMap = payMap.get(rs.getInt("pm"+i));
						resMap.put("pm"+i,tmpMap.get("payment_method_name"));
						resMap.put("pc"+i,tmpMap.get("payment_class_id"));
						resMap.put("pcn"+i,tmpMap.get("payment_class_name"));
						resMap.put("pv"+i,rs.getInt("pv"+i));
						
						if(tmpMap.get("payment_class_id").equals("1")) {
							cash += rs.getInt("pv"+i);
						} else if(tmpMap.get("payment_class_id").equals("4")) {
							voucher += rs.getInt("pv"+i);
						}
					}
				}
				resMap.put("cash", cash-rs.getInt("change_value"));
				resMap.put("staff", rs.getString("staff"));
				resMap.put("billValue", rs.getInt("bill_value"));
				
				String cId = rs.getString("customer_id");
				// 新規リピート
				resMap.put("nr","");
				int tc = rs.getInt("technic_class_id");
				int it = rs.getInt("item_class_id");
				if (tc == 14 || tc == 20 || it == 15 || it ==16 || ( 65<= it && it<= 76)) {
					// 技術：14 アイラッシュ社員, 20 社員お手入れサービス
					// 商品：15 社員購入, 16 アイラッシュ社員購入, 65~76 社員　ほげほげ
					resMap.put("nr","社");
				} else {
					// 過去データがない場合
					if(!checkMap.containsKey(cId)){
						resMap.put("nr","新");
					} else {
						Map<String, Object> customerMap = checkMap.get(cId);
						String key = rs.getString("product_division") +"-"+ rs.getString("product_id");
						
						if (customerMap.containsKey(key)
								&& (int)customerMap.get(key) == 2) {
							// 2回目の商品購入の場合
							resMap.put("nr","リ");
						} else {
							// 初回来店日の1ヶ月後の日付を取得
							Date oneMonth = (Date)customerMap.get("one_month");
							if( rs.getDate("sales_date").compareTo(oneMonth) < 1) {
								resMap.put("nr","新");
							} else if (!customerMap.containsKey(key)){
								resMap.put("nr","内");
							}
						}
					}
				}
				// 受・物・サロン
				resMap.put("kbn","");
				if (resMap.get("nr").equals("社") 
					|| rs.getInt("staff_id")==17 || rs.getInt("staff_id")==19 || rs.getInt("staff_id")==20
					|| voucher > 0) {
					resMap.put("kbn","サ");
				} else if(rs.getInt("product_division") >= 5 || rs.getInt("product_division") <= 8 ) {
					resMap.put("kbn","受");
				} else if(rs.getInt("product_division") >= 2 || rs.getInt("product_division") <= 4 ) {
					resMap.put("kbn","物");
				}

				// 3：技術クレーム　4：商品返品は分ける
//				if (rs.getInt("product_division") == 3 || rs.getInt("product_division") == 4 
//						|| (rs.getInt("product_division") ==1 && rs.getInt("technic_class_id") ==1)	) {
				if ( rs.getInt("technic_class_id") ==1 
						|| rs.getInt("technic_class_id") ==15
						|| rs.getInt("technic_class_id") ==19
						|| rs.getInt("technic_class_id") ==20) {
					resultList2.add(resMap);
				} else {
					resultList.add(resMap);
				}
			}
			// その他入金
			sql = new StringBuilder(4000);
			sql.append(" select ds.slip_no, TO_CHAR(ds.sales_date, 'MM月DD日') || ' 売掛分' as  title");
			sql.append(" , mc.customer_name1 || mc.customer_name2 as cusname");
			sql.append(" , ( select ms.staff_name1 || ms.staff_name2 from mst_staff ms where ms.staff_id = dp.staff_id ) as staff");
			sql.append(" , p1.payment_method_id as pm1, p1.payment_value as pv1, p2.payment_method_id as pm2, p2.payment_value as pv2");
			sql.append(" , p3.payment_method_id as pm3, p3.payment_value as pv3, p4.payment_method_id as pm4, p4.payment_value as pv4");
			sql.append(" , ( select sum(dpdx.payment_value) from data_payment_detail dpdx");
			sql.append("  where dpdx.shop_id = dp.shop_id and dpdx.slip_no = dp.slip_no ");
			sql.append("  and dpdx.payment_no = dp.payment_no and dpdx.payment_method_id = 1) as sum");
			sql.append(" from data_sales ds ");
			sql.append(" inner join data_payment dp on ds.shop_id = dp.shop_id and ds.slip_no = dp.slip_no");
			sql.append(" inner join mst_customer mc on ds.customer_id = mc.customer_id ");
			sql.append(" left outer join data_payment_detail p1 on p1.payment_detail_no = 1 and ds.shop_id = p1.shop_id and ds.slip_no = p1.slip_no and dp.payment_no = p1.payment_no ");
			sql.append(" left outer join data_payment_detail p2 on p2.payment_detail_no = 2 and ds.shop_id = p2.shop_id and ds.slip_no = p2.slip_no and dp.payment_no = p2.payment_no ");
			sql.append(" left outer join data_payment_detail p3 on p3.payment_detail_no = 3 and ds.shop_id = p3.shop_id and ds.slip_no = p3.slip_no and dp.payment_no = p3.payment_no ");
			sql.append(" left outer join data_payment_detail p4 on p4.payment_detail_no = 4 and ds.shop_id = p4.shop_id and ds.slip_no = p4.slip_no and dp.payment_no = p4.payment_no ");
			sql.append(" where dp.shop_id = ").append(shopList).append(" and dp.payment_date = '"+sdf.format(targetDate)+"' and dp.payment_no > 0");
			sql.append(" union all");
			sql.append(" select ( -99 - io_no)as slip_no, use_for as  title, null as cusname");
			sql.append(" , ( select ms.staff_name1 || ms.staff_name2 from mst_staff ms where ms.staff_id = dci.staff_id ) as staff");
			sql.append(" , null as pm1, null as pv1, null as pm2, null as pv2, null as pm3, null as pv3, null as pm4, null as pv4");
			sql.append(" , io_value as sum from data_cash_io dci");
			sql.append(" where dci.in_out = True and dci.shop_id = ").append(shopList).append(" and dci.io_date = '"+sdf.format(targetDate)+"'");

			rs = SystemInfo.getConnection().executeQuery(sql.toString());
			
			SystemInfo.getLogger().info(sql.toString());
			
			while (rs.next()) {
				Map<String, Object> resMap = new  HashMap<String, Object>();
				resMap.put("slip_no", rs.getInt("slip_no"));
				resMap.put("productName", rs.getString("title"));
				resMap.put("customerName", rs.getString("cusname"));
				//支払明細
				for(int i=1; i<=4; i++) {
					if (rs.getString("pm"+i)!= null && !rs.getString("pm"+i).equals("")) {
						Map<String, String> tmpMap = payMap.get(rs.getInt("pm"+i));
						resMap.put("pm"+i,tmpMap.get("payment_method_name"));
						resMap.put("pc"+i,tmpMap.get("payment_class_id"));
						resMap.put("pcn"+i,tmpMap.get("payment_class_name"));
						resMap.put("pv"+i,rs.getInt("pv"+i));
					}
				}
				resMap.put("cash", rs.getInt("sum"));
				resMap.put("staff", rs.getString("staff"));
				resultList2.add(resMap);
			}
		} catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			return false;
        }
		
		JPOIApi jx = new JPOIApi("販売実績表");
        jx.setTemplateFile("/com/geobeck/sosia/pos/report/custom/modesty_payment_report.xls");
		// ヘッダー
		SimpleDateFormat printSdf = new SimpleDateFormat("yyyy年MM月dd日（E）");
		jx.setCellValue(20, 1, printSdf.format(targetDate));	
		jx.setCellValue(20, 2, shopName);	

		//　一覧
		int row=5;
		int addCnt=0;
		int salesSum=0;
		int cashSum=0;
		for(int i = 18; i < resultList.size(); i++) {
			jx.copyRow(5, 6);
			addCnt++;
		}
		
		int tmpSlip = 0;
		for(int i = 0; i < resultList.size(); i++) {
			Map<String, Object> nowRow = resultList.get(i);
			jx.setCellValue(2, row+i, nowRow.get("nr").toString());
			jx.setCellValue(3, row+i, nowRow.get("kbn").toString());
			jx.setCellValue(4, row+i, nowRow.get("productName").toString());
			jx.setCellValue(6, row+i, nowRow.get("customerName").toString());
			jx.setCellValue(8, row+i, (int)nowRow.get("detailValueNoTax"));
			jx.setCellValue(10, row+i, (int)nowRow.get("tax"));

			if(tmpSlip != (int)nowRow.get("slip_no")) {
				for(int j=1; j<=4; j++) {
					if (nowRow.containsKey("pm"+j)) {
						jx.setCellValue(11+(j*2-2), row+i, nowRow.get("pm"+j).toString());
						jx.setCellValue(12+(j*2-2), row+i, (int)nowRow.get("pv"+j));
					}
				}
				jx.setCellValue(20, row+i,(int) nowRow.get("cash"));
				jx.setCellValue(23, row+i, (int)nowRow.get("billValue"));
				cashSum+= (int) nowRow.get("cash");
			} 
			jx.setCellValue(22, row+i, (String)nowRow.get("staff"));
			salesSum+= (int)nowRow.get("detailValueNoTax");
			tmpSlip= (int)nowRow.get("slip_no");
		}
		jx.setCellValue(8, 25+addCnt, salesSum);
		
		//　クレーム・返品一覧
		tmpSlip = 0;
		int row2 = 30+addCnt;
		int addCnt2=0;
		int cashSum2=0;
		for(int i = 3; i < resultList2.size(); i++) {
			jx.copyRow(30, 31);
			addCnt2++;
		}
		for(int i = 0; i < resultList2.size(); i++) {
			Map<String, Object> nowRow = resultList2.get(i);
			jx.setCellValue(2, row2+i, (String)nowRow.get("nr"));
			jx.setCellValue(3, row2+i, (String)nowRow.get("kbn"));
			jx.setCellValue(4, row2+i, (String)nowRow.get("productName"));
			jx.setCellValue(6, row2+i, (String)nowRow.get("customerName"));
			if (nowRow.containsKey("detailValueNoTax")) {
				jx.setCellValue(8, row2+i, (Integer)nowRow.get("detailValueNoTax"));
			}
			if (nowRow.containsKey("tax")) {
				jx.setCellValue(10, row2+i,(Integer)nowRow.get("tax"));
			}
			
			if (tmpSlip == -99 ) {
				jx.setCellValue(20, row+i,(int) nowRow.get("cash"));
				cashSum2+= (int) nowRow.get("cash");
			} else if(tmpSlip != (Integer)nowRow.get("slip_no")) {
				for(int j=1; j<=4; j++) {
					if (nowRow.containsKey("pm"+j)) {
						jx.setCellValue(11+(j*2-2), row2+i, nowRow.get("pm"+j).toString());
						jx.setCellValue(12+(j*2-2), row2+i, (int)nowRow.get("pv"+j));
					}
				}
				jx.setCellValue(20, row2+i, (Integer) nowRow.get("cash"));
				jx.setCellValue(22, row2+i, (String) nowRow.get("staff"));
				if (nowRow.containsKey("billValue")) {
					jx.setCellValue(23, row2+i, (Integer)nowRow.get("billValue"));
				}
				cashSum2+= (Integer) nowRow.get("cash");
			}
			jx.setCellValue(22, row2+i, nowRow.get("staff").toString());
			tmpSlip= (Integer)nowRow.get("slip_no");
		}
		jx.setCellValue(20, 35+addCnt+addCnt2, cashSum + cashSum2);
		//出金内容
		int row3 = 38 + addCnt + addCnt2;
		int r3Sum=0;
		for(int i = 0; i < outList.size(); i++) {
			Map<String, Object> nowRow = outList.get(i);
			jx.setCellValue(11, row3+i, (String)nowRow.get("use_for"));
			jx.setCellValue(21, row3+i, (nowRow.get("staff")!= null)?(String)nowRow.get("staff"): "");
			jx.setCellValue(23, row3+i, (Integer)nowRow.get("io_value"));
			r3Sum += (Integer)nowRow.get("io_value");
		}
		jx.setCellValue(21, 44 + addCnt + addCnt2, r3Sum);
		//前日レジ
		jx.setCellValue( 6, 45 + addCnt + addCnt2, regStaff);
		jx.setCellValue( 6, 37 + addCnt + addCnt2, regCash);
		jx.setCellValue( 6, 39 + addCnt + addCnt2, cashSum + cashSum2);
		jx.setCellValue( 6, 41 + addCnt + addCnt2, cashSum + cashSum2 + regCash);
		
		jx.setCellValue( 8, 44 + addCnt + addCnt2, cashSum + cashSum2 + regCash - r3Sum);
		
		jx.openWorkbook();
		return true;
	}
}
