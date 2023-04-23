/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.report.custom;

import com.geobeck.sosia.pos.hair.report.util.JExcelApi;
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
		int month = td.get(Calendar.MONTH)+1;
		
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
			sql.append(" sales_date");
			sql.append(",customer_no ");
			sql.append(",customer_name1 || customer_name2 as customer_name ");
			sql.append(",slip_no ");
			sql.append(",SUM(val_notax) as price ");
			sql.append(",rtype ");
			sql.append(",introducer_no ");
			sql.append(",introducer_name1 || introducer_name2 as introducer_name ");
			sql.append(",(select ms.staff_name1 || ms.staff_name2 from mst_staff ms where ms.staff_id = staff) as staffb ");
			sql.append(",sum(cnt) as cnts");
			sql.append(" from ");
			sql.append("( "); // 明細毎の対象データを取得 
			sql.append(" select ");
			sql.append(" dsd.shop_id, dsd.slip_no, dsd.slip_detail_no, dsd.sales_date ");
			sql.append(",dsd.discount_detail_value_no_tax as val_notax ");
			sql.append(",mc.customer_no, mc.customer_name1, mc.customer_name2 ");
			sql.append(",mc2.customer_no as introducer_no, mc2.customer_name1 as introducer_name1, mc2.customer_name2 as introducer_name2 ");
			sql.append(",COALESCE(dsdp.staff_id, dcss.staff_id) as staff, free_heading_name as rtype ,dsd.customer_id ");
			sql.append(" from ");// 販売詳細 
			sql.append(" view_data_sales_detail_valid dsd  ");
			sql.append(" inner join mst_customer mc on dsd.customer_id = mc.customer_id ");
			sql.append(" left outer join mst_customer mc2 on mc.introducer_id = mc2.customer_id ");
			sql.append(" left outer join data_sales_detail_proportionally dsdp on dsd.product_division in (2,4) and dsd.shop_id = dsdp.shop_id and  dsd.slip_no = dsdp.slip_no and  dsd.slip_detail_no = dsdp.slip_detail_no and dsd.staff_id <> dsdp.staff_id ");
			sql.append(" left outer join data_contract dc on dc.shop_id = dsd.shop_id and dc.slip_no = dsd.slip_no and dsd.product_division = 5 and dsd.product_id = dc.product_id ");
			sql.append(" left outer join data_contract_staff_share dcss on dsd.shop_id = dcss.shop_id  and dc.slip_no = dcss.slip_no and dc.contract_no = dcss.contract_no and dc.contract_detail_no = dcss.contract_detail_no and dsd.staff_id <> dcss.staff_id ");
			sql.append(" left outer join mst_customer_free_heading mcfh on dsd.customer_id = mcfh.customer_id and mcfh.free_heading_class_id = '4' ");
			sql.append(" left outer join mst_free_heading mfh on mcfh.free_heading_class_id = mfh.free_heading_class_id and mcfh.free_heading_id = mfh.free_heading_id ");
			sql.append(" where ");
			sql.append(" ( ");// 技術or技術クレーム and アイラッシュ会員orフリー会員
			sql.append(" ( dsd.product_division in (1,2) and exists(select technic_id from mst_technic mt where mt.technic_class_id in (2,4) and dsd.product_id = mt.technic_id ) ) ");
			sql.append(" or ");// 商品or商品返品＞右記以外の商品（ガウン＋業務用＋その他＋インオーラ＋社員○○○を集計しない）
			sql.append(" ( dsd.product_division in (2,4) and exists(select item_id from mst_item mt where mt.item_class_id not in (10,12,14,15,16,39,46,55,57,60,64,65,66,67,68,69,70,71,72,73,74,75) and dsd.product_id = mt.item_id ) ) ");
			sql.append(" or ");// 契約＞すべての分類 
			sql.append(" dsd.product_division = 5) ");
			sql.append(" and (dsd.staff_id ='"+staff.getStaffID()+"' or dsdp.staff_id ='"+staff.getStaffID()+"' or dcss.staff_id ='"+staff.getStaffID()+"' ) ");
			sql.append(" and dsd.sales_date >= '"+sdf.format(bef)+"' and dsd.sales_date <= '"+sdf.format(aft)+"' ");
			sql.append("  ) detail ");
			// 過去に契約等がある顧客を取得
			sql.append(" left outer join");
 			sql.append(" (select dsd.customer_id, count(dsd.customer_id) as cnt");
			sql.append(" from view_data_sales_detail_valid dsd ");
			sql.append(" where ");
			sql.append(" (( dsd.product_division in (1,2) and exists(select technic_id from mst_technic mt where mt.technic_class_id in (2,4) and dsd.product_id = mt.technic_id ) ) ");
			sql.append(" or ( dsd.product_division in (2,4) and exists(select item_id from mst_item mt where mt.item_class_id not in (10,12,14,15,16,39,46,55,57,60,64,65,66,67,68,69,70,71,72,73,74,75) and dsd.product_id = mt.item_id ) ) ");
			sql.append(" or dsd.product_division = 5)");
			sql.append(" and dsd.sales_date < '"+sdf.format(bef)+"'");
			sql.append(" group by customer_id) cont on cont.customer_id = detail.customer_id");
			sql.append(" group by sales_date, slip_no, customer_no, staff, customer_name1, customer_name2, rtype, introducer_no, introducer_name1, introducer_name2 ");
			sql.append(" order by sales_date, slip_no");

            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
                Map<String, Object> resMap = new  HashMap<String, Object>();
				resMap.put("salesDate", rs.getDate("sales_date"));
				resMap.put("customerNo", rs.getString("customer_no"));
				resMap.put("customerName", rs.getString("customer_name"));
				resMap.put("price", rs.getInt("price"));
				resMap.put("rType", rs.getString("rtype"));
				resMap.put("introducerNo", rs.getString("introducer_no"));
				resMap.put("introducerName", rs.getString("introducer_name"));
				// 過去の来店
				if ( rs.getInt("cnts") > 0) {
					resMap.put("newP", "-");
					resMap.put("repP", rs.getInt("price")/2 );
				} else {
					resMap.put("newP", rs.getInt("price")/2 );
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
		
		JExcelApi jx = new JExcelApi("コミッション明細書");
        jx.setTemplateFile("/com/geobeck/sosia/pos/report/custom/modesty_monthly_commission.xls");
		int maxPage = (int)Math.ceil(resultList.size() / 19d);
		int p = 0;
		SimpleDateFormat dd = new SimpleDateFormat("dd");
		//対象データが20件以上ある場合（複数ページ出力する必要がある場合）
		while(p<maxPage) {
			p++;
			//対象データを全て出力するまでループ
			jx.setTargetSheet("page"+p);
			//------------------------------
			// ヘッダ
			//------------------------------
			jx.setValue(1, 1, month);
			jx.setValue(3, 2, shop.getShopName());
			jx.setValue(3, 3, staff.getStaffName(0) +" "+  staff.getStaffName(1));
			jx.setValue(14, 1, p);
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
					jx.setValue(1, row+i, dd.format(nowRow.get("salesDate")));
					jx.setValue(2, row+i, nowRow.get("customerNo"));
					jx.setValue(3, row+i, nowRow.get("customerName"));
					jx.setValue(4, row+i, nowRow.get("price"));
					jx.setValue(5, row+i, nowRow.get("rType"));
					jx.setValue(6, row+i, nowRow.get("introducerNo"));
					jx.setValue(7, row+i, nowRow.get("introducerName"));
					jx.setValue(8, row+i, nowRow.get("newP"));
					jx.setValue(9, row+i, nowRow.get("repP"));
					jx.setValue(10, row+i, nowRow.get("staffB"));
					
					if (nowRow.get("price") != null) {
						sum_price += (int)nowRow.get("price");
					}
					if(!nowRow.get("newP").equals("-")) {
						sum_new += (int)nowRow.get("newP");
					}
					if(!nowRow.get("repP").equals("-")) {
						sum_rep += (int)nowRow.get("repP");
					}
				}
			}
			//------------------------------
			// 合計欄
			//------------------------------
			jx.setValue(4, row+19, sum_price);
			jx.setValue(8, row+19, sum_new);
			jx.setValue(9, row+19, sum_rep);
			
			if (p!=maxPage) {	
				jx.setValue(4, row+20, "");
				jx.setValue(8, row+20, "");
				jx.setValue(9, row+20, "");
			}
		}
		for (int i=maxPage+1; i<=5; i++) {
			jx.removeSheet(i);
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
	protected boolean printDailyReport(Object shop, Date targetDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		
		String shopList = "";
		String shopName = "";
		if( shop instanceof MstGroup) {
			shopList = ((MstGroup) shop).getShopIDListAll();
			shopName = ((MstGroup) shop).getGroupName();
		} else if  ( shop instanceof MstShop){
			shopList = ((MstShop) shop).getShopID()+"";
			shopName = ((MstShop) shop).getShopName();
		}

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
			sql.append("   , mcr.num ");
			sql.append("   , ( sign( ( (dsd.product_num * dsd.product_value) / (1.0 + get_tax_rate(ds.sales_date))))  ");
			sql.append("     * ceil( abs( ( (dsd.product_num * dsd.product_value) / (1.0 + get_tax_rate(ds.sales_date))))) ");
			sql.append("     ) AS detail_value_no_tax ");
			sql.append("   , dc.staff_id ");
			sql.append("   , ms.staff_name1 || ms.staff_name2 AS staff_name ");
			sql.append("   , (select sum(n.product_num) from data_contract_digestion n  ");
			sql.append("      where n.contract_no = dc.contract_no  ");
			sql.append("      and n.slip_no <= dc.slip_no and n.contract_detail_no = dc.contract_detail_no ");
			sql.append("      and n.contract_shop_id = dc.contract_shop_id and n.delete_date is null) as used ");
			sql.append("   ,dcc.valid_date ");
			sql.append("   ,dcc.insert_date ");
			sql.append("   ,(CASE WHEN mcr.is_praise_time THEN  mcr.praise_time_limit ");
			sql.append("    ELSE 0 END) AS limit_flg ");
			sql.append(" from ");
			sql.append("   data_sales ds  ");
			sql.append("   inner join mst_customer mc on ds.customer_id = mc.customer_id  ");
			sql.append("   left outer join data_sales_detail dsd on ds.shop_id = dsd.shop_id  and ds.slip_no = dsd.slip_no  ");
			sql.append("   left outer join data_contract_digestion dc on ds.shop_id = dc.shop_id  and ds.slip_no = dc.slip_no and dsd.contract_no = dc.contract_no and dsd.contract_detail_no = dc.contract_detail_no  ");
			sql.append("   left outer join data_contract dcc on  dc.contract_shop_id = dcc.shop_id and dc.contract_no = dcc.contract_no and dc.contract_detail_no = dcc.contract_detail_no ");
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
				resMap.put("detail_value_no_tax", rs.getString("detail_value_no_tax"));
				resMap.put("staff_name", rs.getString("staff_name"));
				resMap.put("used", rs.getBigDecimal("used"));
				resMap.put("limit_flg", "");
				
				if (rs.getInt("limit_flg") > 0
						&& rs.getDate("valid_date")!=null
						&& rs.getDate("insert_date")!=null) {
					// カレンダークラスのインスタンスを取得
					Calendar cal1 = Calendar.getInstance();
					Calendar cal2 = Calendar.getInstance();
					cal1.setTime(rs.getDate("valid_date"));
					cal2.setTime(rs.getDate("insert_date"));
					cal2.add(Calendar.MONTH, rs.getInt("limit_flg"));
					
					if(cal1.get(Calendar.YEAR)==cal2.get(Calendar.YEAR)
						&&cal1.get(Calendar.MONTH)==cal2.get(Calendar.MONTH)
						&&cal1.get(Calendar.DATE)==cal2.get(Calendar.DATE)) {
						resMap.put("limit_flg", "×");
					}
				}

				SystemInfo.getLogger().info(resMap.toString());
                resultList.add(resMap);
				cIds.add(rs.getInt("customer_id"));
				money+=rs.getInt("detail_value_no_tax");
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
			sql.append(" and mc.course_class_id in (1,2,7,8,9,22,23) ");
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
			sql.append("	and mc.course_class_id in (1,2,7,8,9,22,23)");
			sql.append("	where dcd.product_num > 0");
			sql.append("	 and sales_date >= '").append(sdf.format(baseDate)).append("'");
			sql.append("	 and sales_date <= '").append(sdf.format(aftDate)).append("'");
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
			sql.append("	and mc.course_class_id in (1,2,7,8,9,22,23)");
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
			 sql.append("   and mc.course_class_id in (1, 2, 7, 8, 9, 22, 23)  ");
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
			 sql.append("  and mc.course_class_id in (1, 2, 7, 8, 9, 22, 23)  ");
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
			 sql.append(" and sales_date <= '").append(sdf.format(aftDate)).append("'");
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
			 sql.append("   and mc.course_class_id in (1, 2, 7, 8, 9, 22, 23)  ");
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
			 sql.append("  and mc.course_class_id in (1, 2, 7, 8, 9, 22, 23)  ");
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
			 sql.append(" and sales_date <= '").append(sdf.format(aftDate)).append("'");
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
			sql.append(" and mc.course_class_id in (1,2,7,8,9,22,23)  ");
			sql.append(" where  ");
			sql.append("  dcd.product_num > 0  ");
			sql.append(" and ds.shop_id =").append(shopList);
			sql.append(" and sales_date >= '").append(sdf.format(baseDate)).append("'");
			sql.append(" and sales_date <= '").append(sdf.format(aftDate)).append("'");
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
			sql.append(" and mc.course_class_id in (1,2,7,8,9,22,23)  ");
			sql.append(" where  ");
			sql.append("  dcd.product_num > 0  ");
			sql.append(" and ds.shop_id =").append(shopList);
			sql.append(" and sales_date >= '").append(sdf.format(baseDate)).append("'");
			sql.append(" and sales_date <= '").append(sdf.format(aftDate)).append("'");
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
			sql.append(" ms.staff_id, ms.staff_name1 ");
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
			sql.append(" and sales_date <= '").append(sdf.format(aftDate)).append("'");
			sql.append(" group by ");
			sql.append(" ms.staff_id, ms.staff_name1 ");
			sql.append("  order by ms.staff_id  ");
			SystemInfo.getLogger().info(sql.toString());
			rs = SystemInfo.getConnection().executeQuery(sql.toString());
            while (rs.next()) {
				staffNames.add(rs.getString("staff_name1"));
			}
            rs.close();
			
			//---------------------------------------------------------------------------------
			// 月の集計
			//---------------------------------------------------------------------------------
			sql = new StringBuilder(4000);
			sql.append(" select ");
			sql.append("     count(distinct customer_id) as a ");
			sql.append("   , count(distinct dsd.slip_no || '' ||  dsd.slip_detail_no) as b ");
			sql.append("   , sum( sign( ( (dsd.product_num * dsd.product_value) / (1.0 + get_tax_rate(ds.sales_date))))  ");
			sql.append("     * ceil( abs( ( (dsd.product_num * dsd.product_value) / (1.0 + get_tax_rate(ds.sales_date))))) ");
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
			sql.append(" where ");
			sql.append(" ds.shop_id =").append(shopList);
			sql.append(" and sales_date >= '").append(sdf.format(baseDate)).append("'");
			sql.append(" and sales_date <= '").append(sdf.format(aftDate)).append("'");
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
		
		JExcelApi jx = new JExcelApi("役務償却日報");
        jx.setTemplateFile("/com/geobeck/sosia/pos/report/custom/modesty_daily_report.xls");

		SimpleDateFormat printSdf = new SimpleDateFormat("yyyy年MM月dd日（E）");
		jx.setValue( 1, 1, printSdf.format(targetDate));	
		jx.setValue(18, 1, shopName);	
		jx.setValue( 8, 3, cIds.size());	
		jx.setValue(11, 3, resultList.size());
		jx.setValue( 3, 4, money);
		
		jx.setValue( 20, 3, mdata[0]);
		jx.setValue( 23, 3, mdata[1]);
		jx.setValue( 15, 4, mdata[2]);
		
		int row=7;
		int addCnt=0;
		for(int i = 20; i <=resultList.size(); i++) {
			jx.insertRow(8, 1);
			addCnt++;
		}
		//　一覧
		for(int i = 0; i < resultList.size(); i++) {
			Map<String, Object> nowRow = resultList.get(i);
			jx.setValue(1, row+i, i+1);
			jx.setValue(2, row+i, nowRow.get("customer_name1"));
			jx.setValue(4, row+i, nowRow.get("customer_name2"));
			jx.setValue(6, row+i, nowRow.get("course_name"));
			jx.setValue(10, row+i, nowRow.get("num"));
			jx.setValue(12, row+i, nowRow.get("detail_value_no_tax"));
			jx.setValue(14, row+i, nowRow.get("staff_name"));
			jx.setValue(16, row+i, nowRow.get("used"));
			jx.setValue(18, row+i, ((BigDecimal)nowRow.get("num")).subtract((BigDecimal)nowRow.get("used")));
			jx.setValue(25, row+i, nowRow.get("limit_flg"));
		}
		// 分類別集計　30,6～
		for(int i = 0; i < summaryList.size(); i++) {
			if (i==4) {	continue;}
			Map<Integer, Integer> nowRow = summaryList.get(i);
			// スタンダード
			if(nowRow.containsKey(1)) {	jx.setValue(16, 30+addCnt+i, nowRow.get(1));
			} else { jx.setValue(16, 30+addCnt+i, 0); }
			//フェイシャル
			if(nowRow.containsKey(2)) { jx.setValue(6, 30+addCnt+i, nowRow.get(2));
			} else { jx.setValue(6, 30+addCnt+i, 0); }
			//スキャルプ
			if(nowRow.containsKey(7)) { jx.setValue(8, 30+addCnt+i, nowRow.get(7));
			}else { jx.setValue(8, 30+addCnt+i, 0);}
			//コダマ
			if(nowRow.containsKey(8)) { jx.setValue(14, 30+addCnt+i, nowRow.get(8));
			}else { jx.setValue(14, 30+addCnt+i, 0); }
			//メモリアル
			if(nowRow.containsKey(9)) { jx.setValue(10, 30+addCnt+i, nowRow.get(9));
			} else { jx.setValue(10, 30+addCnt+i, 0);}
			//プリスティン
			if(nowRow.containsKey(22) || nowRow.containsKey(23)) {
				int nm = ((nowRow.containsKey(22)?nowRow.get(22):0)
							+(nowRow.containsKey(23)?nowRow.get(23):0));
				jx.setValue(12, 30+addCnt+i, nm);
			} else { jx.setValue(12, 30+addCnt+i, 0);}
		}
		
		int i =0;
		for(String stff: staffNames) {
			 jx.setValue( 8+(i*2), 38+addCnt, stff);
			 i++;
		}

		jx.openWorkbook();
		return true;
	}
}
