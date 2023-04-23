/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.report.custom;

import com.geobeck.sosia.pos.hair.master.product.MstCourseClass;
import com.geobeck.sosia.pos.hair.report.util.JExcelApi;
import com.geobeck.sosia.pos.hair.report.util.JPOIApi;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.product.MstItemClass;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ResultSetWrapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.format.Colour;

/**
 *
 * @author gb_user
 */
public class ModestyReportAnalysisLogic {

	public static final String[] ANLY_KEY = {"D", "E", "V", "W", "G", "H", "P", "J", "K", "L", "M", "N", "O", "R", "Q", "S", "T", "U", "AG", "AI", "AJ", "BI", "BJ", "BK", "BL", "CI", "CK", "CL", "CM", "CO", "CP"};
	public static final String[] ANLY_LABEL = {"会員 - 総エステ", "会員 - 来店顧客", "会員 - 次月予約", "会員 - 未予約", "会員 - 月１役務償却", "会員 - 月２以上役務償却", "会員 - 月３以上役務償却", "会員 - 未来店者数", "会員 - 中期未来店者数", "会員 - 長期未来店者数", "会員 - 未来店者引き上げ", "会員 - 中長期未来店引き上げ", "会員 - 期限切れ予備軍", "会員 - 期限切れ3か月前", "会員 - 内新(美容器)来店", "会員 - 新規会員", "会員 - 継続人数", "会員 - 卒業者", "会員 - 商品愛用者", "会員 - リピートなし", "会員 - ２か月以上リピートなし", "卒業 - 商品愛用者", "卒業 - 新規愛用者", "卒業 - リピートなし", "卒業 - 2か月以上リピートなし", "フリー会員", "フリー - 来店顧客", "フリー - 未予約", "フリー - 商品愛用者", "フリー - リピートなし", "フリー - 2か月以上リピートなし"};

	static Set<Integer> getTargetCustomerIdSet(ModestyReportPanel parentPanel, MstShop mstShop, Date targetDate, String listKey) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Calendar td = Calendar.getInstance();
		td.setTime(targetDate);
		//当月
		td.set(Calendar.DATE, 1);
		Date baseOneDate = td.getTime();
		td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date baseEndDate = td.getTime();
		//翌月
		td.add(Calendar.MONTH, +1);
		td.set(Calendar.DATE, 1);
		Date nextFDate = td.getTime();
		td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date nextEndDate = td.getTime();
		//3ヶ月後
		td.setTime(targetDate);
		td.add(Calendar.MONTH, +3);
		td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date aft3EndDate = td.getTime();
		//4ヶ月後
		td.setTime(targetDate);
		td.add(Calendar.MONTH, +4);
		td.set(Calendar.DATE, 1);
		Date aft4OneDate = td.getTime();
		//12ヶ月後
		td.setTime(targetDate);
		td.add(Calendar.MONTH, +12);
		td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date aft12EndDate = td.getTime();

		// 過去1日
		td.setTime(targetDate);
		td.add(Calendar.MONTH, -1);
		Date bef1OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef2OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef3OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef4OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef5OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef6OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef7OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef8OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef9OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef10OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef11OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef12OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef13OneDate = td.getTime();
		td.setTime(targetDate);
		td.add(Calendar.MONTH, -23);
		Date bef23OneDate = td.getTime();
		td.setTime(targetDate);
		td.add(Calendar.MONTH, -32);
		td.set(Calendar.DATE, 1);
		Date bef32OneDate = td.getTime();
		td.setTime(targetDate);
		td.add(Calendar.MONTH, -34);
		td.set(Calendar.DATE, 1);
		Date bef34OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		td.set(Calendar.DATE, 1);
		Date bef35OneDate = td.getTime();
		td.set(Calendar.DATE, 2);
		Date bef35TwoDate = td.getTime();
		
		td.setTime(targetDate);
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef1EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef2EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef3EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef4EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef5EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef6EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef7EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef8EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef9EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef10EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef11EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef12EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef13EndDate = td.getTime();
		td.setTime(targetDate);
		td.add(Calendar.MONTH, -23); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef23EndDate = td.getTime();

		Set<Integer> retSet = new HashSet<Integer>();
		try {
			if (listKey.equals("D")) {
				retSet = getDset(mstShop, sdf, baseOneDate, baseEndDate, bef12OneDate, bef23OneDate);
			} else if (listKey.equals("E")) {
				// G,H,P
				retSet.addAll(getGset(mstShop, sdf, baseOneDate, baseEndDate));
				retSet.addAll(getHset(mstShop, sdf, baseOneDate, baseEndDate));
				retSet.addAll(getPset(mstShop, sdf, baseOneDate, baseEndDate));
			} else if (listKey.equals("V")) {
				Set<Integer> dSet = getDset(mstShop, sdf, baseOneDate, baseEndDate, bef12OneDate, bef23OneDate);
				retSet = getVset(mstShop, sdf, baseOneDate, baseEndDate, nextFDate, nextEndDate, dSet);
			} else if (listKey.equals("W")) {
				Set<Integer> dSet = getDset(mstShop, sdf, baseOneDate, baseEndDate, bef12OneDate, bef23OneDate);
				retSet = getWset(mstShop, sdf, baseOneDate, baseEndDate, nextFDate, nextEndDate, dSet);
			} else if (listKey.equals("G")) {
				retSet = getGset(mstShop, sdf, baseOneDate, baseEndDate);
			} else if (listKey.equals("H")) {
				retSet = getHset(mstShop, sdf, baseOneDate, baseEndDate);
			} else if (listKey.equals("P")) {
				retSet = getPset(mstShop, sdf, baseOneDate, baseEndDate);
			} else if (listKey.equals("J")) {
				Set<Integer> dSet = getDset(mstShop, sdf, baseOneDate, baseEndDate, bef12OneDate, bef23OneDate);
				retSet = getJset(mstShop, sdf, baseOneDate, baseEndDate, bef1EndDate, bef3OneDate, dSet);
			} else if (listKey.equals("K")) {
				Set<Integer> dSet = getDset(mstShop, sdf, baseOneDate, baseEndDate, bef12OneDate, bef23OneDate);
				retSet = getKset(mstShop, sdf, baseOneDate, baseEndDate, bef4EndDate, bef11OneDate, dSet);
			} else if (listKey.equals("L")) {
				Set<Integer> dSet = getDset(mstShop, sdf, baseOneDate, baseEndDate, bef12OneDate, bef23OneDate);
				retSet = getLset(mstShop, sdf, baseOneDate, baseEndDate, bef12EndDate, bef23OneDate, dSet);
			} else if (listKey.equals("M")) {
				retSet = getMset(mstShop, sdf, baseOneDate, baseEndDate, bef7OneDate, bef5EndDate, bef1EndDate);
			} else if (listKey.equals("N")) {
				retSet = getNset(mstShop, sdf, baseOneDate, baseEndDate, bef23OneDate, bef7EndDate, bef1EndDate);
			} else if (listKey.equals("O")) {
				retSet = getOset(mstShop, sdf, aft12EndDate, aft4OneDate);
			} else if (listKey.equals("R")) {
				retSet = getRset(mstShop, sdf, bef32OneDate, bef35TwoDate);
			} else if (listKey.equals("Q")) {
				Set<Integer> dSet = getDset(mstShop, sdf, baseOneDate, baseEndDate, bef12OneDate, bef23OneDate);
				retSet = getQset(mstShop, sdf, baseOneDate, baseEndDate, dSet);
			} else if (listKey.equals("S")) {
				Set<Integer> dSet = getDset(mstShop, sdf, baseOneDate, baseEndDate, bef12OneDate, bef23OneDate);
				retSet = getSset(mstShop, sdf, baseOneDate, baseEndDate, dSet);
			} else if (listKey.equals("T")) {
				Set<Integer> dSet = getDset(mstShop, sdf, baseOneDate, baseEndDate, bef12OneDate, bef23OneDate);
				retSet = getTset(mstShop, sdf, baseOneDate, baseEndDate, dSet);
			} else if (listKey.equals("U")) {
				retSet = getUset(mstShop, sdf, baseOneDate, baseEndDate);
			} else if (listKey.equals("AG")) {
				Set<Integer> dSet = getDset(mstShop, sdf, baseOneDate, baseEndDate, bef12OneDate, bef23OneDate);
				retSet = getAGset(mstShop, sdf, baseOneDate, baseEndDate, bef12OneDate, dSet);
			} else if (listKey.equals("AI")) {
				Set<Integer> dSet = getDset(mstShop, sdf, baseOneDate, baseEndDate, bef12OneDate, bef23OneDate);
				Set<Integer> agSet = getAGset(mstShop, sdf, baseOneDate, baseEndDate, bef12OneDate, dSet);
				retSet = getAIset(mstShop, sdf, baseOneDate, baseEndDate, bef1EndDate, bef1OneDate, agSet);
			} else if (listKey.equals("AJ")) {
				Set<Integer> dSet = getDset(mstShop, sdf, baseOneDate, baseEndDate, bef12OneDate, bef23OneDate);
				Set<Integer> agSet = getAGset(mstShop, sdf, baseOneDate, baseEndDate, bef12OneDate, dSet);
				retSet = getAJset(mstShop, sdf, bef2EndDate, agSet);
			} else if (listKey.equals("BI")) {
				Set<Integer> setCI = getCIset(mstShop, sdf, baseOneDate, baseEndDate, bef23OneDate);
				retSet = getBIset(mstShop, sdf, baseOneDate, baseEndDate, bef12OneDate, bef23OneDate, setCI);
			} else if (listKey.equals("BJ")) {
				retSet = getBJset(mstShop, sdf, baseOneDate, baseEndDate, bef12OneDate, bef12EndDate, bef23OneDate);
			} else if (listKey.equals("BK")) {
				Set<Integer> setCI = getCIset(mstShop, sdf, baseOneDate, baseEndDate, bef23OneDate);
				Set<Integer> setBI = getBIset(mstShop, sdf, baseOneDate, baseEndDate, bef12OneDate, bef23OneDate, setCI);
				retSet = getBKset(mstShop, sdf, baseOneDate, baseEndDate, bef1OneDate, bef1EndDate, setBI);
			} else if (listKey.equals("BL")) {
				Set<Integer> setCI = getCIset(mstShop, sdf, baseOneDate, baseEndDate, bef23OneDate);
				Set<Integer> setBI = getBIset(mstShop, sdf, baseOneDate, baseEndDate, bef12OneDate, bef23OneDate, setCI);
				retSet = getBLset(mstShop, sdf, bef2EndDate, setBI);
			} else if (listKey.equals("CI")) {
				retSet = getCIset(mstShop, sdf, baseOneDate, baseEndDate, bef23OneDate);
			} else if (listKey.equals("CK")) {
				Set<Integer> setCI = getCIset(mstShop, sdf, baseOneDate, baseEndDate, bef23OneDate);
				retSet = getCKset(mstShop, sdf, baseOneDate, baseEndDate, setCI);
			} else if (listKey.equals("CL")) {
				Set<Integer> setCI = getCIset(mstShop, sdf, baseOneDate, baseEndDate, bef23OneDate);
				retSet = getCLset(mstShop, sdf, baseOneDate, baseEndDate, nextFDate, setCI);
			} else if (listKey.equals("CM")) {
				Set<Integer> setCI = getCIset(mstShop, sdf, baseOneDate, baseEndDate, bef23OneDate);
				retSet = getCMset(mstShop, sdf, bef12OneDate, baseEndDate, setCI);
			} else if (listKey.equals("CO")) {
				Set<Integer> setCI = getCIset(mstShop, sdf, baseOneDate, baseEndDate, bef23OneDate);
				Set<Integer> setCM = getCMset(mstShop, sdf, bef12OneDate, baseEndDate, setCI);
				retSet = getCOset(mstShop, sdf, baseOneDate, baseEndDate, bef1EndDate, bef1OneDate, setCM);
			} else if (listKey.equals("CP")) {
				Set<Integer> setCI = getCIset(mstShop, sdf, baseOneDate, baseEndDate, bef23OneDate);
				Set<Integer> setCM = getCMset(mstShop, sdf, bef12OneDate, baseEndDate, setCI);
				retSet = getCPset(mstShop, sdf, bef2EndDate, setCM);
			}
		} catch (SQLException ex) {
			Logger.getLogger(ModestyReportAnalysisLogic.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
		return retSet;
	}

	static boolean printListReport(ModestyReportPanel parentPanel, MstShop mstShop, Date targetDate, String tgt) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Set<Integer> setTarget = new HashSet<Integer>(0);
		HashMap<String, String> headerMap = new HashMap<String, String>();
		List<HashMap<String, String>> listMap = new ArrayList<HashMap<String, String>>();

		Calendar td = Calendar.getInstance();
		// 日付関連
		td.setTime(targetDate);
		SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
		String year = yyyy.format(td.getTime());
		SimpleDateFormat mm = new SimpleDateFormat("MM");
		String month = mm.format(td.getTime());
		//当月
		td.setTime(targetDate);
		td.set(Calendar.DATE, 1);
		Date baseOneDate = td.getTime();
		td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date baseEndDate = td.getTime();
		//翌月
		td.add(Calendar.MONTH, +1);
		td.set(Calendar.DATE, 1);
		Date nextOneDate = td.getTime();
		td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date nextEndDate = td.getTime();
		//3ヶ月後
		td.setTime(targetDate);
		td.add(Calendar.MONTH, +3);
		td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date aft3EndDate = td.getTime();
		//12ヶ月後
		td.setTime(targetDate);
		td.add(Calendar.MONTH, +12);
		td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date aft12EndDate = td.getTime();

		// 過去1日
		td.setTime(targetDate);
		td.add(Calendar.MONTH, -1);
		Date bef1OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef2OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef3OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef4OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef5OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef6OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef7OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef8OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef9OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef10OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef11OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef12OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef13OneDate = td.getTime();
		td.setTime(targetDate);
		td.add(Calendar.MONTH, -23);
		Date bef23OneDate = td.getTime();
		
		td.setTime(targetDate);
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef1EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef2EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef3EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef4EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef5EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef6EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef7EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef8EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef9EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef10EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef11EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef12EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef13EndDate = td.getTime();
		td.setTime(targetDate);
		td.add(Calendar.MONTH, -23); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef23EndDate = td.getTime();
		// 36カ月前
		td.setTime(targetDate);
		td.add(Calendar.MONTH, -35);
		td.set(Calendar.DATE, 1);
		Date bef35OneDate = td.getTime();
		try {
			if (tgt.equals("F")) {
				setTarget = getDset(mstShop, sdf, baseOneDate, baseEndDate, bef12OneDate, bef23OneDate);
			} else if (tgt.equals("G")) {
				setTarget = getCIset(mstShop, sdf, baseOneDate, baseEndDate, bef23OneDate);
			} else if (tgt.equals("H")) {
				Set<Integer> setCI = getCIset(mstShop, sdf, baseOneDate, baseEndDate, bef23OneDate);
				setTarget = getBIset(mstShop, sdf, baseOneDate, baseEndDate, bef12OneDate, bef23OneDate, setCI);
			}

			for (Integer cid : setTarget) {
				HashMap<String, String> rowMap = new HashMap<String, String>();

				StringBuilder sql = new StringBuilder();
				sql.append("select sales_date,customer_name1 || ' ' || customer_name2 as cus_name , staff_name1 ||' '|| staff_name2 as staff_name\n"
						+ ", (select MIN(reservation_datetime) as res_date from data_reservation dr\n"
						+ "inner join data_reservation_detail drd on drd.shop_id=dr.shop_id and drd.reservation_no=dr.reservation_no\n"
						+ "where dr.delete_date is null and drd.delete_date is null and dr.customer_id = mc.customer_id\n"
						+ " and dr.shop_id =" + mstShop.getShopID() + "\n"
						+ " and reservation_datetime>='" + sdf.format(nextOneDate) + "') as res_date\n"
						+ "from data_sales ds left outer join mst_staff ms on ds.staff_id = ms.staff_id\n"
						+ "inner join mst_customer mc on ds.customer_id = mc.customer_id\n"
						+ "where ds.delete_date is null and sales_date <='" + sdf.format(baseEndDate) + "'\n"
						+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
						+ " and (ds.staff_id <> 316 or ds.staff_id is null)"
						+ " and ds.customer_id = " + cid + "\n"
						+ " and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
						+ "order by sales_date desc");
				SystemInfo.getLogger().info("[G]" + sql.toString());
				ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
				while (rs.next()) {
					rowMap.put("F", (rs.getString("cus_name") != null) ? rs.getString("cus_name") : "");
					rowMap.put("G", (rs.getString("staff_name") != null) ? rs.getString("staff_name") : "");
					if (rs.getDate("sales_date") != null) {
						Date ddd = rs.getDate("sales_date");
						rowMap.put("J", sdf.format(ddd));
						String COL = (baseOneDate.before(ddd))?"BLUE":(
										(bef2OneDate.before(ddd) ? "BLACK":(
											(bef12OneDate.before(ddd) ? "GREEN":"RED"))));
						rowMap.put("JCOL", COL);
					} else {
						rowMap.put("J", "");
						rowMap.put("JCOL", "");
					}
					if (rs.getDate("res_date") != null) {
						rowMap.put("K", sdf.format(rs.getDate("res_date")));
						rowMap.put("KCOL", nextOneDate.after(rs.getDate("res_date")) + "");
					} else {
						rowMap.put("K", "");
					}
					break;
				}

				sql = new StringBuilder();
				sql.append("select customer_id, free_heading_name from mst_customer_free_heading mcfh\n"
						+ "inner join mst_free_heading mfh on mcfh.free_heading_id = mfh.free_heading_id\n"
						+ "where mcfh.delete_date is null and mfh.delete_date is null and mfh.free_heading_class_id=3\n"
						+ "and customer_id = " + cid + " order by mcfh.insert_date desc\n");
				//SystemInfo.getLogger().info("[G]" + sql.toString());
				rs = SystemInfo.getConnection().executeQuery(sql.toString());
				String strL = "";
				while (rs.next()) {
					//strL += (strL.length() > 0) ? "," : "";
					strL += rs.getString("free_heading_name");
					break;
				}
				rowMap.put("L", strL);

				sql = new StringBuilder();
				sql.append("select ds.customer_id, ds.sales_date, dsdx.slip_no as cancel_slip, dc.product_num, ceil(COALESCE(sum(dsdu.product_num),0)) as dig_num, dc.valid_date \n"
						+ "from data_sales ds\n"
						+ "inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and dsd.delete_date is null and dsd.product_division = 5\n"
						+ "inner join data_contract dc on ds.shop_id = dc.shop_id and ds.slip_no = dc.slip_no and dsd.product_id = dc.product_id \n"
						+ "inner join mst_course mc on mc.course_id = dsd.product_id\n"
						+ "left outer join "
						+ " ( select dsdx.* from data_sales_detail dsdx "
						+ "  inner join data_sales dsx on dsx.shop_id = dsdx.shop_id and dsx.slip_no = dsdx.slip_no and dsx.delete_date is null \n"
						+ "  where dsdx.product_division = 8 and dsx.sales_date <='" + sdf.format(baseEndDate) + "'"
						+ ") dsdx on dc.shop_id = dsdx.contract_shop_id and dc.contract_no = dsdx.contract_no and dc.contract_detail_no = dsdx.contract_detail_no \n"
						+ " left outer join  ( select dcdx.* from data_contract_digestion dcdx inner join data_sales dsx on  dsx.customer_id = " + cid + " and dsx.shop_id = dcdx.shop_id and dsx.slip_no = dcdx.slip_no and dsx.delete_date is null \n"
						+ "  where dsx.sales_date <='" + sdf.format(baseEndDate) + "'"
						+ ") dsdu on dc.shop_id = dsdu.contract_shop_id and dc.contract_no = dsdu.contract_no and dc.contract_detail_no = dsdu.contract_detail_no \n"
						+ "where customer_id = " + cid + "\n"
						+ " and mc.course_class_id = \n");

				StringBuilder sql2 = new StringBuilder(4000);
				sql2.append(" and ds.shop_id =" + mstShop.getShopID() + " and sales_date <='" + sdf.format(baseEndDate) + "' group by ds.customer_id, ds.sales_date, dsdx.slip_no, dc.product_num, dc.valid_date \n"
						+ "order by ds.customer_id, ds.sales_date desc");
				
				MstCourseClass[] mccr = {
					(MstCourseClass) parentPanel.cmbCourseClass1.getSelectedItem(), (MstCourseClass) parentPanel.cmbCourseClass2.getSelectedItem(), (MstCourseClass) parentPanel.cmbCourseClass3.getSelectedItem(), (MstCourseClass) parentPanel.cmbCourseClass4.getSelectedItem(), (MstCourseClass) parentPanel.cmbCourseClass5.getSelectedItem(), (MstCourseClass) parentPanel.cmbCourseClass6.getSelectedItem()
				};
				for (int i = 1; i <= 6; i++) {
					Integer mc1s = 0;
					MstCourseClass mc1 = mccr[i - 1];
					if (mc1.getCourseClassID() != null) {
						mc1s = mc1.getCourseClassID();
						headerMap.put("H" + i, mc1.getCourseClassName());

						SystemInfo.getLogger().info("[H]" + sql.toString() + mc1s + sql2.toString());
						rs = SystemInfo.getConnection().executeQuery(sql.toString() + mc1s + sql2.toString());

						int dtype = 0;
						int hcnt = 0;
						int hcnta = 0; //解約あり
						int hcntb = 0; //終了あり
						int hcntc = 0; //日付け
						int hcntd = 0; //残あり
						Date nnn = null;
						while (rs.next()) {
							if (rs.getInt("cancel_slip") > 0) {
								if (hcnt == 0) {
									rowMap.put("H" + i, "解約");
									dtype = 1;
								}
								hcnta++;
							} else if (rs.getInt("product_num") == rs.getInt("dig_num")) {
								if (hcnt == 0) {
									rowMap.put("H" + i, "終了");
									dtype = 2;
								}
								//hcntb++;
							} else {
								if (hcnt == 0) {
									rowMap.put("H" + i, sdf.format(rs.getDate("sales_date")));
									nnn = rs.getDate("sales_date");
								}
								hcntc++;
								if ((rs.getInt("product_num") - rs.getInt("dig_num")) > 0) {
									hcntd++;
									
									Date ddd = rs.getDate("sales_date");
									hcntb += bef35OneDate.after(ddd) ? 1: 0;
								}
							}
							
							hcnt++;
						}
						
						
						String COL = "BLACK";
						// 役務残があるとき
						if ( dtype == 0 && hcntd > 0) {
							COL = baseOneDate.before(nnn) ? "BLUE"
								: (bef23OneDate.after(nnn)
								? (bef35OneDate.after(nnn) ? "RED" : "GREEN") : "BLACK");
						} else if (dtype == 2) {
							COL = (hcntd > 0) ? "GREEN" : (hcntb > 0) ? "RED" : "BLACK";
						} else if (dtype == 1){
							COL = (hcntd > 0) ? "GREEN" : (hcntb > 0) ? "RED" : "BLACK";
						}
						rowMap.put("H" + i + "COL", COL);	
					}
				}

				sql = new StringBuilder();
				sql.append("select ds.customer_id, ds.sales_date, dsd.product_division\n"
						+ "from data_sales ds\n"
						+ "inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and dsd.delete_date is null and dsd.product_division in (2,4)\n"
						+ "inner join mst_item mi on mi.item_id = dsd.product_id\n"
						+ "where customer_id = " + cid + "\n"
						+ " and mi.item_class_id = \n");
				sql2 = new StringBuilder(4000);
				sql2.append(" and ds.shop_id =" + mstShop.getShopID() + " and sales_date <='" + sdf.format(baseEndDate) + "' \n"
						+ "order by ds.customer_id, ds.sales_date desc");

				MstItemClass[] mic = {
					(MstItemClass) parentPanel.cmbItemClass1.getSelectedItem(), (MstItemClass) parentPanel.cmbItemClass2.getSelectedItem(), (MstItemClass) parentPanel.cmbItemClass3.getSelectedItem(), (MstItemClass) parentPanel.cmbItemClass4.getSelectedItem(), (MstItemClass) parentPanel.cmbItemClass5.getSelectedItem(), (MstItemClass) parentPanel.cmbItemClass6.getSelectedItem(), (MstItemClass) parentPanel.cmbItemClass7.getSelectedItem()
				};
				for (int i = 1; i <= 7; i++) {
					
					int icnt = 0;
					int icnta = 0; //返品あり
					Date nnn = null;
				
					Integer mi1s = 0;
					MstItemClass mi1 = mic[i - 1];
					if (mi1.getItemClassID() != null) {
						mi1s = mi1.getItemClassID();
						headerMap.put("I" + i, mi1.getItemClassName());
						SystemInfo.getLogger().info("[I"+i+"]" + sql.toString() + mi1s + sql2.toString());

						rs = SystemInfo.getConnection().executeQuery(sql.toString() + mi1s + sql2.toString());
						while (rs.next()) {
							if (icnt == 0) {
								nnn = rs.getDate("sales_date");
								rowMap.put("I" + i, sdf.format(nnn));
								rowMap.put("I" + i+"PD", rs.getInt("product_division")+"");
							}
							icnt++;
							icnta += (rs.getInt("product_division")==4) ? 1 : 0;
						}
						
						if (icnt == 1) {
							if(rowMap.get("I" + i+"PD").equals("4")){
								rowMap.put("I" + i + "COL", "RED");
							} else {
								if (baseOneDate.before(nnn) && baseEndDate.after(nnn)) {
									rowMap.put("I" + i + "COL", "BLUE");
								} else if (bef12OneDate.before(nnn) ) {
									rowMap.put("I" + i + "COL", "BLACK");
								} else {
									rowMap.put("I" + i + "COL", "RED");
								}
							}
							
						} else if (icnt > 1) {
							if (icnt == icnta) {
								rowMap.put("I" + i + "COL", "RED");
							} else {
								rowMap.put("I" + i + "COL", "BLACK");
							}
						} else {
							rowMap.put("I" + i + "COL", "BLACK");
						}
					}
				}
				listMap.add(rowMap);
			}

		} catch (SQLException ex) {
			Logger.getLogger(ModestyReportAnalysisLogic.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}
		JExcelApi jx = new JExcelApi("現状分析用リスト");
		jx.setTemplateFile("/com/geobeck/sosia/pos/report/custom/現状分析用リスト.xls");

		// シート名
		jx.getTargetSheet().setName("list");

		// タイトル
		if (tgt.equals("F")) {
			jx.setValue(3, 1, "サロン会員");
		} else if (tgt.equals("G")) {
			jx.setValue(3, 1, "フリー会員");
		} else {
			jx.setValue(3, 1, "愛用者");
		}

		// ヘッダ
		jx.setValue(11, 1, mstShop.getShopName());
		jx.setValue(15, 1, year);
		jx.setValue(17, 1, month);

		jx.setValue(4, 2, headerMap.containsKey("H1") ? headerMap.get("H1") : "");
		jx.setValue(5, 2, headerMap.containsKey("H2") ? headerMap.get("H2") : "");
		jx.setValue(6, 2, headerMap.containsKey("H3") ? headerMap.get("H3") : "");
		jx.setValue(7, 2, headerMap.containsKey("H4") ? headerMap.get("H4") : "");
		jx.setValue(8, 2, headerMap.containsKey("H5") ? headerMap.get("H5") : "");
		jx.setValue(9, 2, headerMap.containsKey("H6") ? headerMap.get("H6") : "");
		jx.setValue(10, 2, headerMap.containsKey("I1") ? headerMap.get("I1") : "");
		jx.setValue(11, 2, headerMap.containsKey("I2") ? headerMap.get("I2") : "");
		jx.setValue(12, 2, headerMap.containsKey("I3") ? headerMap.get("I3") : "");
		jx.setValue(13, 2, headerMap.containsKey("I4") ? headerMap.get("I4") : "");
		jx.setValue(14, 2, headerMap.containsKey("I5") ? headerMap.get("I5") : "");
		jx.setValue(15, 2, headerMap.containsKey("I6") ? headerMap.get("I6") : "");
		jx.setValue(16, 2, headerMap.containsKey("I7") ? headerMap.get("I7") : "");
		int row = 3;
		jx.insertRow(row, listMap.size());
		for (HashMap<String, String> rowData : listMap) {

			jx.setValue(1, row, row - 2);
			jx.setValue(2, row, rowData.get("F"));
			jx.setValue(3, row, rowData.get("G"));

			jx.setValue(4, row, rowData.containsKey("H1") ? rowData.get("H1") : "", rowData.containsKey("H1COL") ? getColor(rowData.get("H1COL")) : Colour.BLACK);
			jx.setValue(5, row, rowData.containsKey("H2") ? rowData.get("H2") : "", rowData.containsKey("H2COL") ? getColor(rowData.get("H2COL")) : Colour.BLACK);
			jx.setValue(6, row, rowData.containsKey("H3") ? rowData.get("H3") : "", rowData.containsKey("H3COL") ? getColor(rowData.get("H3COL")) : Colour.BLACK);
			jx.setValue(7, row, rowData.containsKey("H4") ? rowData.get("H4") : "", rowData.containsKey("H4COL") ? getColor(rowData.get("H4COL")) : Colour.BLACK);
			jx.setValue(8, row, rowData.containsKey("H5") ? rowData.get("H5") : "", rowData.containsKey("H5COL") ? getColor(rowData.get("H5COL")) : Colour.BLACK);
			jx.setValue(9, row, rowData.containsKey("H6") ? rowData.get("H6") : "", rowData.containsKey("H6COL") ? getColor(rowData.get("H6COL")) : Colour.BLACK);
			jx.setValue(10, row, rowData.containsKey("I1") ? rowData.get("I1") : "", rowData.containsKey("I1COL") ? getColor(rowData.get("I1COL")) : Colour.BLACK);
			jx.setValue(11, row, rowData.containsKey("I2") ? rowData.get("I2") : "", rowData.containsKey("I2COL") ? getColor(rowData.get("I2COL")) : Colour.BLACK);
			jx.setValue(12, row, rowData.containsKey("I3") ? rowData.get("I3") : "", rowData.containsKey("I3COL") ? getColor(rowData.get("I3COL")) : Colour.BLACK);
			jx.setValue(13, row, rowData.containsKey("I4") ? rowData.get("I4") : "", rowData.containsKey("I4COL") ? getColor(rowData.get("I4COL")) : Colour.BLACK);
			jx.setValue(14, row, rowData.containsKey("I5") ? rowData.get("I5") : "", rowData.containsKey("I5COL") ? getColor(rowData.get("I5COL")) : Colour.BLACK);
			jx.setValue(15, row, rowData.containsKey("I6") ? rowData.get("I6") : "", rowData.containsKey("I6COL") ? getColor(rowData.get("I6COL")) : Colour.BLACK);
			jx.setValue(16, row, rowData.containsKey("I7") ? rowData.get("I7") : "", rowData.containsKey("I7COL") ? getColor(rowData.get("I7COL")) : Colour.BLACK);
			jx.setValue(17, row, rowData.get("J"), rowData.containsKey("JCOL") ? getColor(rowData.get("JCOL")) : Colour.BLACK);
			jx.setValue(18, row, rowData.get("K"), rowData.containsKey("KCOL") ? Colour.BLUE : Colour.BLACK);
			jx.setValue(19, row, rowData.get("L"));

			row++;
		}

		jx.openWorkbook();
		return true;
	}

	static Colour getColor(String col) {
		if (col.equals("RED")) {
			return Colour.RED;
		} else if (col.equals("BLUE")) {
			return Colour.BLUE;
		} else if (col.equals("GREEN")) {
			return Colour.GREEN;
		}
		return Colour.BLACK;
	}

	static boolean printDailyReport(ModestyReportPanel parentPanel, MstShop mstShop, Date targetDate, ArrayList<String> ls) {

		try {
			Calendar td = Calendar.getInstance();
			// 日付関連
			td.setTime(targetDate);
			SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
			String year = yyyy.format(td.getTime());
			SimpleDateFormat mm = new SimpleDateFormat("MM");
			String month = mm.format(td.getTime());

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		//当月
		td.setTime(targetDate);
		td.set(Calendar.DATE, 1);
		Date baseOneDate = td.getTime();
		td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date baseEndDate = td.getTime();
		//翌月
		td.add(Calendar.MONTH, +1);
		td.set(Calendar.DATE, 1);
		Date nextOneDate = td.getTime();
		td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date nextEndDate = td.getTime();
		//3ヶ月後
		td.setTime(targetDate);
		td.add(Calendar.MONTH, +3);
		td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date aft3EndDate = td.getTime();
		//4ヶ月後
		td.setTime(targetDate);
		td.add(Calendar.MONTH, +3);
		td.set(Calendar.DATE, 1);
		Date aft4OneDate = td.getTime();
		td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date aft4EndDate = td.getTime();
		//12ヶ月後
		td.setTime(targetDate);
		td.add(Calendar.MONTH, +12);
		td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date aft12EndDate = td.getTime();

		// 過去1日
		td.setTime(targetDate);
		td.add(Calendar.MONTH, -1);
		Date bef1OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef2OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef3OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef4OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef5OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef6OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef7OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef8OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef9OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef10OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef11OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef12OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef13OneDate = td.getTime();
		td.setTime(targetDate);
		td.add(Calendar.MONTH, -23);
		Date bef23OneDate = td.getTime();
		td.setTime(targetDate);
		td.add(Calendar.MONTH, -32);
		td.set(Calendar.DATE, 1);
		Date bef32OneDate = td.getTime();
		td.setTime(targetDate);
		td.add(Calendar.MONTH, -34);
		td.set(Calendar.DATE, 1);
		Date bef34OneDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		td.set(Calendar.DATE, 1);
		Date bef35OneDate = td.getTime();
		td.set(Calendar.DATE, 2);
		Date bef35TwoDate = td.getTime();
		td.add(Calendar.MONTH, -1);
		Date bef36TwoDate = td.getTime();
		
		td.setTime(targetDate);
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef1EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef2EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef3EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef4EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef5EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef6EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef7EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef8EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef9EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef10EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef11EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef12EndDate = td.getTime();
		td.add(Calendar.MONTH, -1); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef13EndDate = td.getTime();
		td.setTime(targetDate);
		td.add(Calendar.MONTH, -23); td.set(Calendar.DATE, td.getActualMaximum(Calendar.DATE));
		Date bef23EndDate = td.getTime();
		
			String responsiblePerson = "";
			//---------------------------------------------------------------------------------
			// 責任者 と、所属スタッフ
			//---------------------------------------------------------------------------------
			StringBuilder sql = new StringBuilder(4000);
			sql.append(" select");
			sql.append("  ms.staff_no");
			sql.append(" ,ms.staff_name1  ");
			sql.append(" ,ms.staff_name2  ");
			sql.append(" ,msc.staff_class_name  ");
			sql.append(" from   ");
			sql.append(" mst_staff ms ");
			sql.append(" left join mst_staff_class msc                              ");
			sql.append("   on ms.staff_class_id = msc.staff_class_id                ");
			sql.append(" where  ");
			sql.append("  ms.shop_id =").append(mstShop.getShopID());
			sql.append(" and ms.delete_date is null ");
			sql.append(" order by ms.display_seq ASC ;  ");

			SystemInfo.getLogger().info(sql.toString());

			ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());

			int staffMaxCounter = 0;
			while (rs.next()) {
				//ヘッダ 責任者欄 自分スタッフ、表示順：１の人 
				if (staffMaxCounter == 0) {
					responsiblePerson = rs.getString("staff_name1") + " " + rs.getString("staff_name2");
				}
				break;
			}

			JPOIApi jx = new JPOIApi("現状分析報告書");
			jx.setTemplateFile("/com/geobeck/sosia/pos/report/custom/modesty_anlyreport.xls");

			// シートの再計算を許可
			jx.getSheet().setForceFormulaRecalculation(true);
			jx.setCellValue(10, 1, year);
			jx.setCellValue(13, 1, month);
			jx.setCellValue(9, 6, month);
			jx.setCellValue(9, 13, month);
			jx.setCellValue(9, 14, month);
			jx.setCellValue(24, 13, month);
			jx.setCellValue(24, 14, month);
			jx.setCellValue(22, 2, mstShop.getShopName());
			jx.setCellValue(28, 2, responsiblePerson);

			Set<Integer> dSet = getDset(mstShop, sdf, baseOneDate, baseEndDate, bef12OneDate, bef23OneDate);
			int D_num = dSet.size();
			jx.setCellValue(5, 4, D_num);

			Set<Integer> vSet = getVset(mstShop, sdf, baseOneDate, baseEndDate, nextOneDate, nextEndDate, dSet);
			jx.setCellValue(11, 5, vSet.size());

			Set<Integer> wSet = getWset(mstShop, sdf, baseOneDate, baseEndDate, nextOneDate, nextEndDate, dSet);
			jx.setCellValue(15, 5, wSet.size());

			Set<Integer> setG = getGset(mstShop, sdf, baseOneDate, baseEndDate);
			jx.setCellValue(20, 4, setG.size());

			Set<Integer> setH = getHset(mstShop, sdf, baseOneDate, baseEndDate);
			jx.setCellValue(25, 4, setH.size());

			Set<Integer> setP = getPset(mstShop, sdf, baseOneDate, baseEndDate);
			jx.setCellValue(30, 4, setP.size());

			Set<Integer> setJ = getJset(mstShop, sdf, baseOneDate, baseEndDate, bef1EndDate, bef3OneDate, dSet);
			jx.setCellValue(20, 5, setJ.size());

			Set<Integer> setK = getKset(mstShop, sdf, baseOneDate, baseEndDate, bef4EndDate, bef11OneDate, dSet);
			jx.setCellValue(25, 5, setK.size());

			Set<Integer> setL = getLset(mstShop, sdf, baseOneDate, baseEndDate, bef12EndDate, bef23OneDate, dSet);
			jx.setCellValue(30, 5, setL.size());

			Set<Integer> setM = getMset(mstShop, sdf, baseOneDate, baseEndDate, bef6OneDate, bef4EndDate, bef1EndDate);
			jx.setCellValue(20, 6, setM.size());

			Set<Integer> setN = getNset(mstShop, sdf, baseOneDate, baseEndDate, bef23OneDate, bef7EndDate, bef1EndDate);
			jx.setCellValue(25, 6, setN.size());

			Set<Integer> setO = getOset(mstShop, sdf, aft12EndDate, aft4OneDate);
			jx.setCellValue(30, 6, setO.size());

			Set<Integer> setQ = getQset(mstShop, sdf, baseOneDate, baseEndDate, dSet);
			jx.setCellValue(25, 8, setQ.size());

			Set<Integer> setR = getRset(mstShop, sdf, bef32OneDate, bef35TwoDate);
			jx.setCellValue(30, 8, setR.size());

			Set<Integer> setS = getSset(mstShop, sdf, baseOneDate, baseEndDate, dSet);
			jx.setCellValue(20, 10, setS.size());
			jx.setCellValue(22, 25, setS.size());

			Set<Integer> setT = getTset(mstShop, sdf, baseOneDate, baseEndDate, dSet);
			jx.setCellValue(25, 10, setT.size());
			jx.setCellValue(22, 26, setT.size());

			Set<Integer> setU = getUset(mstShop, sdf, baseOneDate, baseEndDate);
			jx.setCellValue(20, 8, setU.size());

			Set<Integer> setAG = getAGset(mstShop, sdf, baseOneDate, baseEndDate, bef12OneDate, dSet);
			jx.setCellValue(5, 12, setAG.size());

			Set<Integer> setAI = getAIset(mstShop, sdf, baseOneDate, baseEndDate, bef1EndDate, bef1OneDate, setAG);
			jx.setCellValue(11, 12, setAI.size());

			Set<Integer> setAJ = getAJset(mstShop, sdf, bef2EndDate, setAG);
			jx.setCellValue(15, 12, setAJ.size());

			Set<Integer> setCI = getCIset(mstShop, sdf, baseOneDate, baseEndDate, bef23OneDate);
			jx.setCellValue(5, 22, setCI.size());

			Set<Integer> setBI = getBIset(mstShop, sdf, baseOneDate, baseEndDate, bef12OneDate, bef23OneDate, setCI);
			jx.setCellValue(20, 12, setBI.size());

			Set<Integer> setBJ = getBJset(mstShop, sdf, baseOneDate, baseEndDate, bef12OneDate, bef12EndDate, bef23OneDate);
			jx.setCellValue(22, 13, setBJ.size());

			Set<Integer> setBK = getBKset(mstShop, sdf, baseOneDate, baseEndDate, bef1OneDate, bef1EndDate, setBI);
			jx.setCellValue(26, 12, setBK.size());

			Set<Integer> setBL = getBLset(mstShop, sdf, bef2EndDate, setBI);
			jx.setCellValue(30, 12, setBL.size());

			Set<Integer> setCK = getCKset(mstShop, sdf, baseOneDate, baseEndDate, setCI);
			jx.setCellValue(11, 22, setCK.size());

			Set<Integer> setCL = getCLset(mstShop, sdf, baseOneDate, baseEndDate, nextOneDate, setCI);
			jx.setCellValue(15, 22, setCL.size());

			Set<Integer> setCM = getCMset(mstShop, sdf, bef12OneDate, baseEndDate, setCI);
			jx.setCellValue(20, 22, setCM.size());

			Set<Integer> setCO = getCOset(mstShop, sdf, baseOneDate, baseEndDate, bef1EndDate, bef1OneDate, setCM);
			jx.setCellValue(26, 22, setCO.size());

			Set<Integer> setCP = getCPset(mstShop, sdf, bef2EndDate, setCM);
			jx.setCellValue(30, 22, setCP.size());

			// [AA1][AB1][AC1][AD1][AE1][AF1]
			String cids = "";
			MstCourseClass mc1 = (MstCourseClass) parentPanel.cmbCourseClass1.getSelectedItem();
			if (mc1!=null && mc1.getCourseClassID() != null) {
				jx.setCellValue(2, 6, mc1.getCourseClassName());
				cids += mc1.getCourseClassID();
			}
			MstCourseClass mc2 = (MstCourseClass) parentPanel.cmbCourseClass2.getSelectedItem();
			if (mc2!=null &&mc2.getCourseClassID() != null) {
				jx.setCellValue(2, 7, mc2.getCourseClassName());
				cids += (cids.length() > 0) ? "," : "";
				cids += mc2.getCourseClassID();
			}
			MstCourseClass mc3 = (MstCourseClass) parentPanel.cmbCourseClass3.getSelectedItem();
			if (mc3!=null &&mc3.getCourseClassID() != null) {
				jx.setCellValue(2, 8, mc3.getCourseClassName());
				cids += (cids.length() > 0) ? "," : "";
				cids += mc3.getCourseClassID();
			}
			MstCourseClass mc4 = (MstCourseClass) parentPanel.cmbCourseClass4.getSelectedItem();
			if (mc4!=null &&mc4.getCourseClassID() != null) {
				jx.setCellValue(2, 9, mc4.getCourseClassName());
				cids += (cids.length() > 0) ? "," : "";
				cids += mc4.getCourseClassID();
			}
			MstCourseClass mc5 = (MstCourseClass) parentPanel.cmbCourseClass5.getSelectedItem();
			if (mc5!=null &&mc5.getCourseClassID() != null) {
				jx.setCellValue(2, 10, mc5.getCourseClassName());
				cids += (cids.length() > 0) ? "," : "";
				cids += mc5.getCourseClassID();
			}
			MstCourseClass mc6 = (MstCourseClass) parentPanel.cmbCourseClass6.getSelectedItem();
			if (mc6!=null &&mc6.getCourseClassID() != null) {
				jx.setCellValue(2, 11, mc6.getCourseClassName());
				cids += (cids.length() > 0) ? "," : "";
				cids += mc6.getCourseClassID();
			}

			if (cids.length() > 0) {
				sql = new StringBuilder(4000);
				sql.append("select course_class_id, count(distinct customer_id) as cnt from \n"
						+ "(select distinct customer_id, mc.course_class_id from \n"
						+ " data_sales_detail dsd\n"
						+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no \n"
						+ " inner join data_contract dc on dsd.contract_shop_id = dc.shop_id and dsd.contract_no = dc.contract_no and dsd.contract_detail_no = dc.contract_detail_no \n"
						+ " inner join mst_course mc on mc.course_id = dc.product_id\n"
						+ " where dsd.delete_date is null \n"
						+ " and dsd.product_division = 6\n"
						+ " and mc.course_class_id in(" + cids + ")\n"
						+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
						+ " and ds.sales_date >= '" + sdf.format(bef23OneDate) + "'\n"
						+ " and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
						+ " and ds. customer_id in ( -1");
				for (Integer dc : dSet) {
					sql.append("," + dc);
				}
				sql.append(")"
						+ " UNION \n"
						+ " select distinct customer_id, mc.course_class_id from \n"
						+ " data_sales_detail dsd\n"
						+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no \n"
						+ " inner join data_contract dc on dsd.shop_id = dc.shop_id and dsd.slip_no = dc.slip_no and dsd.product_id = dc.product_id\n"
						+ " inner join mst_course mc on mc.course_id = dc.product_id\n"
						+ " where dsd.delete_date is null \n"
						+ " and dsd.product_division = 5\n"
						+ " and not exists (select dsdz.slip_no from data_sales_detail dsdz where dsdz.delete_date is null and dsdz.product_division = 8 and dsdz.contract_shop_id = dc.shop_id and dsdz.contract_no=dc.contract_no and dsdz.contract_detail_no=dc.contract_detail_no)\n"
						+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
						+ " and mc.course_class_id in(" + cids + ")\n"
						+ " and ds. customer_id in ( -1");
				for (Integer dc : dSet) {
					sql.append("," + dc);
				}
				sql.append(")"
						+ " and ds.sales_date >= '" + sdf.format(baseOneDate) + "'\n"
						+ " and ds.sales_date <= '" + sdf.format(baseEndDate) + "') as al\n"
						+ " group by course_class_id");

				SystemInfo.getLogger().info("[AA]" + sql.toString());
				rs = SystemInfo.getConnection().executeQuery(sql.toString());
				while (rs.next()) {
					SystemInfo.getLogger().info("[" + rs.getInt("course_class_id") + "]" + rs.getInt("cnt"));
					if (mc1!=null && mc1.getCourseClassID() != null && mc1.getCourseClassID() == rs.getInt("course_class_id")) {
						jx.setCellValue(5, 6, rs.getInt("cnt"));
					}
					if (mc2!=null &&mc2.getCourseClassID() != null && mc2.getCourseClassID() == rs.getInt("course_class_id")) {
						jx.setCellValue(5, 7, rs.getInt("cnt"));
					}
					if (mc3!=null &&mc3.getCourseClassID() != null && mc3.getCourseClassID() == rs.getInt("course_class_id")) {
						jx.setCellValue(5, 8, rs.getInt("cnt"));
					}
					if (mc4!=null &&mc4.getCourseClassID() != null && mc4.getCourseClassID() == rs.getInt("course_class_id")) {
						jx.setCellValue(5, 9, rs.getInt("cnt"));
					}
					if (mc5!=null &&mc5.getCourseClassID() != null && mc5.getCourseClassID() == rs.getInt("course_class_id")) {
						jx.setCellValue(5, 10, rs.getInt("cnt"));
					}
					if (mc6!=null &&mc6.getCourseClassID() != null && mc6.getCourseClassID() == rs.getInt("course_class_id")) {
						jx.setCellValue(5, 11, rs.getInt("cnt"));
					}
				}

				// [AA2][AB2][AC2][AD2][AE2][AF2]
				sql = new StringBuilder(4000);
				sql.append(" select dsd.product_division, count(distinct customer_id) as cnt, sum(ceil(dsd.product_num * dsd.product_value / (1.0 + get_tax_rate(dsd.tax_rate, ds.sales_date)))-dsd.discount_value) as val, mc.course_class_id from \n"
						+ " data_sales_detail dsd\n"
						+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no \n"
						+ " inner join data_contract dc on dsd.shop_id = dc.shop_id and dsd.slip_no = dc.slip_no and dsd.product_id = dc.product_id\n"
						+ " inner join mst_course mc on mc.course_id = dc.product_id\n"
						+ " where dsd.delete_date is null \n"
						+ " and not exists (select dsdz.slip_no from data_sales_detail dsdz where dsdz.delete_date is null and dsdz.product_division = 8 and dsdz.contract_shop_id = dc.shop_id and dsdz.contract_no=dc.contract_no and dsdz.contract_detail_no=dc.contract_detail_no)\n"
						+ " and dsd.product_division = 5\n"
						+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
						+ " and mc.course_class_id in(" + cids + ")\n"
						+ " and ds.sales_date >= '" + sdf.format(baseOneDate) + "'\n"
						+ " and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
						+ " group by course_class_id, dsd.product_division");

				SystemInfo.getLogger().info("[AA4]" + sql.toString());
				rs = SystemInfo.getConnection().executeQuery(sql.toString());
				int[] cnt = {0,0,0,0,0,0};
				int[] val = {0,0,0,0,0,0};
				
				while (rs.next()) {
					SystemInfo.getLogger().info("[" + rs.getInt("course_class_id") + "]" + rs.getInt("cnt"));
					if (mc1!=null && mc1.getCourseClassID() != null && mc1.getCourseClassID() == rs.getInt("course_class_id")) {
						if (rs.getInt("product_division")>5) {
							cnt[0] -= rs.getInt("cnt");
						} else {
							cnt[0] += rs.getInt("cnt");
						}
						val[0] += rs.getInt("val");
					}
					if (mc2!=null && mc2.getCourseClassID() != null && mc2.getCourseClassID() == rs.getInt("course_class_id")) {
						if (rs.getInt("product_division")>5) {
							cnt[1] -= rs.getInt("cnt");
						} else {
							cnt[1] += rs.getInt("cnt");
						}
						val[1] += rs.getInt("val");
					}
					if (mc3!=null &&mc3.getCourseClassID() != null && mc3.getCourseClassID() == rs.getInt("course_class_id")) {
						if (rs.getInt("product_division")>5) {
							cnt[2] -= rs.getInt("cnt");
						} else {
							cnt[2] += rs.getInt("cnt");
						}
						val[2] += rs.getInt("val");
					}
					if (mc4!=null &&mc4.getCourseClassID() != null && mc4.getCourseClassID() == rs.getInt("course_class_id")) {
						if (rs.getInt("product_division")>5) {
							cnt[3] -= rs.getInt("cnt");
						} else {
							cnt[3] += rs.getInt("cnt");
						}
						val[3] += rs.getInt("val");
					}
					if (mc5!=null &&mc5.getCourseClassID() != null && mc5.getCourseClassID() == rs.getInt("course_class_id")) {
						if (rs.getInt("product_division")>5) {
							cnt[4] -= rs.getInt("cnt");
						} else {
							cnt[4] += rs.getInt("cnt");
						}
						val[4] += rs.getInt("val");
					}
					if (mc6!=null &&mc6.getCourseClassID() != null && mc6.getCourseClassID() == rs.getInt("course_class_id")) {
						if (rs.getInt("product_division")>5) {
							cnt[5] -= rs.getInt("cnt");
						} else {
							cnt[5] += rs.getInt("cnt");
						}
						val[5] += rs.getInt("val");
					}
				}
				for (int i=0; i<6; i++) {
					jx.setCellValue(12, 6+i, cnt[i]);
					jx.setCellValue(14, 6+i, val[i]);
				}
			}

			/* [AK][AL]
			 * AGのうち、出力月に商品購入履歴のある顧客数　　※商品分類は問わない
			 * AKの顧客の商品購入の合計金額　※商品分類は問わない　（AKの顧客のなかで、返品明細があれば、減額する）
			 */
			sql = new StringBuilder();
			sql.append("select dsd.product_division, count(distinct customer_id) as cnt ,sum(ceil(dsd.product_num * dsd.product_value / (1.0 + get_tax_rate(dsd.tax_rate, ds.sales_date)))-dsd.discount_value) as val"
					+ " from data_sales_detail dsd\n"
					+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
					+ " where dsd.product_division in (2,4)\n"
					+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
					+ " and ds.sales_date >= '" + sdf.format(baseOneDate) + "'\n"
					+ " and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
					+ " and ds. customer_id in ( -1");
			for (Integer dc : setAG) {
				sql.append("," + dc);
			}
			sql.append(") \n"
					+ "group by dsd.product_division");
			SystemInfo.getLogger().info("[AK][AL]" + sql.toString());
			rs = SystemInfo.getConnection().executeQuery(sql.toString());
			int cnt = 0;
			int val = 0;
			while (rs.next()) {
				if (rs.getInt("product_division")>2) {
					cnt -= rs.getInt("cnt");
				} else {
					cnt += rs.getInt("cnt");
				}
				val += rs.getInt("val");
			}
			jx.setCellValue(12, 13, cnt);
			jx.setCellValue(14, 13, val);

			// [BA1][BB1][BC1][BD1][BE1][BF1] [BG1]
			String icids = "";
			MstItemClass mic1 = (MstItemClass) parentPanel.cmbItemClass1.getSelectedItem();
			if (mic1!=null && mic1.getItemClassID() != null) {
				jx.setCellValue(2, 15, mic1.getItemClassName());
				jx.setCellValue(17, 15, mic1.getItemClassName());
				icids += mic1.getItemClassID();
			}
			MstItemClass mic2 = (MstItemClass) parentPanel.cmbItemClass2.getSelectedItem();
			if (mic2!=null && mic2.getItemClassID() != null) {
				jx.setCellValue(2, 16, mic2.getItemClassName());
				jx.setCellValue(17, 16, mic2.getItemClassName());
				icids += (icids.length() > 0) ? "," : "";
				icids += mic2.getItemClassID();
			}
			MstItemClass mic3 = (MstItemClass) parentPanel.cmbItemClass3.getSelectedItem();
			if (mic3!=null && mic3.getItemClassID() != null) {
				jx.setCellValue(2, 17, mic3.getItemClassName());
				jx.setCellValue(17, 17, mic3.getItemClassName());
				icids += (icids.length() > 0) ? "," : "";
				icids += mic3.getItemClassID();
			}
			MstItemClass mic4 = (MstItemClass) parentPanel.cmbItemClass4.getSelectedItem();
			if (mic4!=null && mic4.getItemClassID() != null) {
				jx.setCellValue(2, 18, mic4.getItemClassName());
				jx.setCellValue(17, 18, mic4.getItemClassName());
				icids += (icids.length() > 0) ? "," : "";
				icids += mic4.getItemClassID();
			}
			MstItemClass mic5 = (MstItemClass) parentPanel.cmbItemClass5.getSelectedItem();
			if (mic5!=null && mic5.getItemClassID() != null) {
				jx.setCellValue(2, 19, mic5.getItemClassName());
				jx.setCellValue(17, 19, mic5.getItemClassName());
				icids += (icids.length() > 0) ? "," : "";
				icids += mic5.getItemClassID();
			}
			MstItemClass mic6 = (MstItemClass) parentPanel.cmbItemClass6.getSelectedItem();
			if (mic6!=null && mic6.getItemClassID() != null) {
				jx.setCellValue(2, 20, mic6.getItemClassName());
				jx.setCellValue(17, 20, mic6.getItemClassName());
				icids += (icids.length() > 0) ? "," : "";
				icids += mic6.getItemClassID();
			}
			MstItemClass mic7 = (MstItemClass) parentPanel.cmbItemClass7.getSelectedItem();
			if (mic7!=null && mic7.getItemClassID() != null) {
				jx.setCellValue(2, 21, mic7.getItemClassName());
				jx.setCellValue(17, 21, mic7.getItemClassName());
				icids += (icids.length() > 0) ? "," : "";
				icids += mic7.getItemClassID();
			}
			if (icids.length() > 0) {
				sql = new StringBuilder();
				sql.append("select item_class_id,  count(distinct customer_id) as cnt from data_sales_detail dsd\n"
						+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
						+ " inner join mst_item  mi on dsd.product_id = mi.item_id "
						+ " where dsd.product_division = 2\n"
						+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
						+ " and ds.sales_date >= '" + sdf.format(bef12OneDate) + "'\n"
						+ " and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
						+ " and mi.item_class_id in ( " + icids + ")\n"
						+ " and ds.customer_id in ( -1");
				for (Integer ag : setAG) {
					sql.append("," + ag);
				}
				sql.append(") group by item_class_id");
				SystemInfo.getLogger().info("[BA1]" + sql.toString());
				rs = SystemInfo.getConnection().executeQuery(sql.toString());
				while (rs.next()) {
					SystemInfo.getLogger().info("[" + rs.getInt("item_class_id") + "]" + rs.getInt("cnt"));
					if (mic1!=null && mic1.getItemClassID() != null && mic1.getItemClassID() == rs.getInt("item_class_id")) {
						jx.setCellValue(5, 15, rs.getInt("cnt"));
					}
					if (mic2!=null && mic2.getItemClassID() != null && mic2.getItemClassID() == rs.getInt("item_class_id")) {
						jx.setCellValue(5, 16, rs.getInt("cnt"));
					}
					if (mic3!=null && mic3.getItemClassID() != null && mic3.getItemClassID() == rs.getInt("item_class_id")) {
						jx.setCellValue(5, 17, rs.getInt("cnt"));
					}
					if (mic4!=null && mic4.getItemClassID() != null && mic4.getItemClassID() == rs.getInt("item_class_id")) {
						jx.setCellValue(5, 18, rs.getInt("cnt"));
					}
					if (mic5!=null && mic5.getItemClassID() != null && mic5.getItemClassID() == rs.getInt("item_class_id")) {
						jx.setCellValue(5, 19, rs.getInt("cnt"));
					}
					if (mic6!=null && mic6.getItemClassID() != null && mic6.getItemClassID() == rs.getInt("item_class_id")) {
						jx.setCellValue(5, 20, rs.getInt("cnt"));
					}
					if (mic7!=null && mic7.getItemClassID() != null && mic7.getItemClassID() == rs.getInt("item_class_id")) {
						jx.setCellValue(5, 21, rs.getInt("cnt"));
					}
				}
				// [BA2]
				sql = new StringBuilder();
				sql.append("select item_class_id, dsd.product_division, count(customer_id) as cnt,sum(ceil(dsd.product_num * dsd.product_value / (1.0 + get_tax_rate(dsd.tax_rate, ds.sales_date)))-dsd.discount_value) as val from data_sales_detail dsd\n"
						+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
						+ " inner join mst_item  mi on dsd.product_id = mi.item_id "
						+ " where dsd.product_division in (2,4)\n"
						+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
						+ " and ds.sales_date >= '" + sdf.format(baseOneDate) + "'\n"
						+ " and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
						+ " and item_class_id in ( " + icids + ")\n"
						+ " and ds.customer_id in ( -1");
				for (Integer ag : setAG) {
					sql.append("," + ag);
				}
				sql.append(") group by item_class_id, dsd.product_division");
				SystemInfo.getLogger().info("[BA1][BB1][BC1][BD1][BE1][BF1] [BG1]"+sql.toString());
				rs = SystemInfo.getConnection().executeQuery(sql.toString());
				
				int[] cntz = {0,0,0,0,0,0,0};
				int[] valz = {0,0,0,0,0,0,0};
				while (rs.next()) {
					SystemInfo.getLogger().info("[" + rs.getInt("item_class_id") + "]" + rs.getInt("cnt"));
					if (mic1!=null && mic1.getItemClassID() != null && mic1.getItemClassID() == rs.getInt("item_class_id")) {
						if (rs.getInt("product_division")>2) {
							cntz[0] -= rs.getInt("cnt");
						} else {
							cntz[0] += rs.getInt("cnt");
						}
						valz[0] += rs.getInt("val");
					}
					if (mic2!=null && mic2.getItemClassID() != null && mic2.getItemClassID() == rs.getInt("item_class_id")) {
						if (rs.getInt("product_division")>2) {
							cntz[1] -= rs.getInt("cnt");
						} else {
							cntz[1] += rs.getInt("cnt");
						}
						valz[1] += rs.getInt("val");
					}
					if (mic3!=null && mic3.getItemClassID() != null && mic3.getItemClassID() == rs.getInt("item_class_id")) {
						if (rs.getInt("product_division")>2) {
							cntz[2] -= rs.getInt("cnt");
						} else {
							cntz[2] += rs.getInt("cnt");
						}
						valz[2] += rs.getInt("val");
					}
					if (mic4!=null && mic4.getItemClassID() != null && mic4.getItemClassID() == rs.getInt("item_class_id")) {
						if (rs.getInt("product_division")>2) {
							cntz[3] -= rs.getInt("cnt");
						} else {
							cntz[3] += rs.getInt("cnt");
						}
						valz[3] += rs.getInt("val");
					}
					if (mic5!=null && mic5.getItemClassID() != null && mic5.getItemClassID() == rs.getInt("item_class_id")) {
						if (rs.getInt("product_division")>2) {
							cntz[4] -= rs.getInt("cnt");
						} else {
							cntz[4] += rs.getInt("cnt");
						}
						valz[4] += rs.getInt("val");
					}
					if (mic6!=null && mic6.getItemClassID() != null && mic6.getItemClassID() == rs.getInt("item_class_id")) {
						if (rs.getInt("product_division")>2) {
							cntz[5] -= rs.getInt("cnt");
						} else {
							cntz[5] += rs.getInt("cnt");
						}
						valz[5] += rs.getInt("val");
					}
					if (mic7!=null && mic7.getItemClassID() != null && mic7.getItemClassID() == rs.getInt("item_class_id")) {
						if (rs.getInt("product_division")>2) {
							cntz[6] -= rs.getInt("cnt");
						} else {
							cntz[6] += rs.getInt("cnt");
						}
						valz[6] += rs.getInt("val");
					}
				}
				
				for (int i=0; i<7; i++) {
					jx.setCellValue(12, 15+i, cntz[i]);
					jx.setCellValue(14, 15+i, valz[i]);
				}
			}

			/* [I]
			 * 前提：すべてのコース契約・有効期限：３６カ月
			 *＜0603_追記＞
			 *出力店舗で契約した全顧客が対象（サロン会員：D　または　サロン卒業：ＢＩ　または　どちらにも含まない顧客）
			 *（新条件・20200309／0603変更）
			 *出力月-35の契約で未消化となっている金額の合計　※出力時点の有効期限無視
			 *→　契約期間：出力月-35の1か月（2020年2月出力の場合、2017年3月が対象月となる）
			 *→　消化期間：出力月-35～出力月の期間（2020年2月出力の場合、2017年3月から2020年2月が対象月となる）
			 *→　この期間での未消化金額として固定・・・・いつ出力しても未消化金額は同じ金額になる。
			 */

			sql = new StringBuilder();
			sql.append("select COALESCE(sum(product_value - (product_value/product_num*use_product_num)),0) as val from (\n"
					+ "select dc.shop_id,  dc.slip_no, dc.contract_no,  dc.contract_detail_no, dc.product_num, dc.product_value\n"
					+ ",(select sum(dcd.product_num) as use_product_num from data_contract_digestion dcd where\n"
					+ "    dcd.contract_shop_id = dc.shop_id \n"
					+ "    and dcd.contract_no = dc.contract_no \n"
					+ "    and dcd.contract_detail_no = dc.contract_detail_no group by dcd.contract_shop_id, dcd.contract_no, dcd.contract_detail_no) as use_product_num\n"
					+ "from data_sales ds\n"
					+ " inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and dsd.product_division=5\n"
					+ " inner join data_contract dc on dsd.shop_id = dc.shop_id and dsd.slip_no = dc.slip_no and dsd.product_id = dc.product_id\n"
					+ " where\n"
					+ " ds.shop_id =" + mstShop.getShopID() + "\n"
					+ " and not exists (select dsdz.slip_no from data_sales_detail dsdz where dsdz.delete_date is null and dsdz.product_division = 8 and dsdz.contract_shop_id = dc.shop_id and dsdz.contract_no=dc.contract_no and dsdz.contract_detail_no=dc.contract_detail_no)\n"
					+ "and dc.insert_date <= '" + sdf.format(bef35OneDate) + "'\n"
					+ "and dc.insert_date >= '" + sdf.format(bef36TwoDate) + "'\n"
					+ ") total\n"
			);
			SystemInfo.getLogger().info(sql.toString());
			rs = SystemInfo.getConnection().executeQuery(sql.toString());
			while (rs.next()) {
				String sval = rs.getBigDecimal("val").divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP).toString();
				jx.setCellValue(30, 10, sval);
			}

			// [CA1] 「BI」の中で、出力月から過去12カ月以内に【出力画面で指定した商品分類「１」】の購入履歴がある人
			if (icids.length() > 0) {
				sql = new StringBuilder();
				sql.append("select item_class_id,  count(customer_id) as cnt from data_sales_detail dsd\n"
						+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
						+ " inner join mst_item  mi on dsd.product_id = mi.item_id "
						+ " where dsd.product_division = 2\n"
						+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
						+ " and ds.sales_date >= '" + sdf.format(bef12OneDate) + "'\n"
						+ " and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
						+ " and item_class_id in ( " + icids + ")\n"
						+ " and ds.customer_id in ( -1");
				for (Integer ag : setBI) {
					sql.append("," + ag);
				}
				sql.append(") group by item_class_id");
				SystemInfo.getLogger().info(sql.toString());
				rs = SystemInfo.getConnection().executeQuery(sql.toString());
				
				while (rs.next()) {
					SystemInfo.getLogger().info("[" + rs.getInt("item_class_id") + "]" + rs.getInt("cnt"));
					if (mic1.getItemClassID() != null && mic1.getItemClassID() == rs.getInt("item_class_id")) {
						jx.setCellValue(20, 15, rs.getInt("cnt"));
					}
					if (mic2.getItemClassID() != null && mic2.getItemClassID() == rs.getInt("item_class_id")) {
						jx.setCellValue(20, 16, rs.getInt("cnt"));
					}
					if (mic3.getItemClassID() != null && mic3.getItemClassID() == rs.getInt("item_class_id")) {
						jx.setCellValue(20, 17, rs.getInt("cnt"));
					}
					if (mic4.getItemClassID() != null && mic4.getItemClassID() == rs.getInt("item_class_id")) {
						jx.setCellValue(20, 18, rs.getInt("cnt"));
					}
					if (mic5.getItemClassID() != null && mic5.getItemClassID() == rs.getInt("item_class_id")) {
						jx.setCellValue(20, 19, rs.getInt("cnt"));
					}
					if (mic6.getItemClassID() != null && mic6.getItemClassID() == rs.getInt("item_class_id")) {
						jx.setCellValue(20, 20, rs.getInt("cnt"));
					}
					if (mic7.getItemClassID() != null && mic7.getItemClassID() == rs.getInt("item_class_id")) {
						jx.setCellValue(20, 21, rs.getInt("cnt"));
					}
				}
				//[CA3] 「ＣＡ１」のうち、出力月に【出力画面で指定した商品分類「１」】の購入履歴がある顧客数
				sql = new StringBuilder();
				sql.append("select item_class_id, dsd.product_division, count(customer_id) as cnt , sum(ceil(dsd.product_num * dsd.product_value / (1.0 + get_tax_rate(dsd.tax_rate, ds.sales_date)))-dsd.discount_value) as val from data_sales_detail dsd\n"
						+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
						+ " inner join mst_item  mi on dsd.product_id = mi.item_id "
						+ " where dsd.product_division in (2,4)\n"
						+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
						+ " and ds.sales_date >= '" + sdf.format(baseOneDate) + "'\n"
						+ " and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
						+ " and item_class_id in ( " + icids + ")\n"
						+ " and ds.customer_id in ( -1");
				for (Integer ag : setBI) {
					sql.append("," + ag);
				}
				sql.append(") group by item_class_id, dsd.product_division");
				SystemInfo.getLogger().info(sql.toString());
				rs = SystemInfo.getConnection().executeQuery(sql.toString());
				
				int[] cnts = {0,0,0,0,0,0,0};
				int[] vals = {0,0,0,0,0,0,0};
				while (rs.next()) {
					SystemInfo.getLogger().info("[" + rs.getInt("item_class_id") + "]" + rs.getInt("cnt"));
					if (mic1.getItemClassID() != null && mic1.getItemClassID() == rs.getInt("item_class_id")) {
						if (rs.getInt("product_division")>2) {
							cnts[0] -= rs.getInt("cnt");
						} else {
							cnts[0] += rs.getInt("cnt");
						}
						vals[0] += rs.getInt("val");
					}
					if (mic2.getItemClassID() != null && mic2.getItemClassID() == rs.getInt("item_class_id")) {
						if (rs.getInt("product_division")>2) {
							cnts[1] -= rs.getInt("cnt");
						} else {
							cnts[1] += rs.getInt("cnt");
						}
						vals[1] += rs.getInt("val");
					}
					if (mic3.getItemClassID() != null && mic3.getItemClassID() == rs.getInt("item_class_id")) {
						if (rs.getInt("product_division")>2) {
							cnts[2] -= rs.getInt("cnt");
						} else {
							cnts[2] += rs.getInt("cnt");
						}
						vals[2] += rs.getInt("val");
					}
					if (mic4.getItemClassID() != null && mic4.getItemClassID() == rs.getInt("item_class_id")) {
						if (rs.getInt("product_division")>2) {
							cnts[3] -= rs.getInt("cnt");
						} else {
							cnts[3] += rs.getInt("cnt");
						}
						vals[3] += rs.getInt("val");
					}
					if (mic5.getItemClassID() != null && mic5.getItemClassID() == rs.getInt("item_class_id")) {
						if (rs.getInt("product_division")>2) {
							cnts[4] -= rs.getInt("cnt");
						} else {
							cnts[4] += rs.getInt("cnt");
						}
						vals[4] += rs.getInt("val");
					}
					if (mic6.getItemClassID() != null && mic6.getItemClassID() == rs.getInt("item_class_id")) {
						if (rs.getInt("product_division")>2) {
							cnts[5] -= rs.getInt("cnt");
						} else {
							cnts[5] += rs.getInt("cnt");
						}
						vals[5] += rs.getInt("val");
					}
					if (mic7.getItemClassID() != null && mic7.getItemClassID() == rs.getInt("item_class_id")) {
						if (rs.getInt("product_division")>2) {
							cnts[6] -= rs.getInt("cnt");
						} else {
							cnts[6] += rs.getInt("cnt");
						}
						vals[6] += rs.getInt("val");
					}
				}
				for (int i=0; i<7; i++) {
					jx.setCellValue(27, 15+i, cnts[i]);
					jx.setCellValue(29, 15+i, vals[i]);
				}
			}
			/*			
			 *[CQ]	ＢＩのうち、出力月に商品購入履歴のある顧客数　　※商品分類は問わない																										
			 *[CR]	BIのうち、　出力月-1と出力月に商品購入履歴がない顧客数		?AKの顧客の商品購入の合計金額　※商品分類は問わない　（AKの顧客のなかで、返品明細があれば、減額する） 																								
			 */
			sql = new StringBuilder();
			sql.append("select count(distinct customer_id) as cnt, sum(ceil(dsd.product_num * dsd.product_value / (1.0 + get_tax_rate(dsd.tax_rate, ds.sales_date)))-dsd.discount_value) as val from data_sales_detail dsd\n"
					+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
					+ " where dsd.product_division in (2,4)\n"
					+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
					+ " and ds.sales_date >= '" + sdf.format(baseOneDate) + "'\n"
					+ " and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
					+ " and ds. customer_id in ( -1");
			for (Integer dc : setBI) {
				sql.append("," + dc);
			}
			sql.append(")");
			SystemInfo.getLogger().info(sql.toString());
			rs = SystemInfo.getConnection().executeQuery(sql.toString());
			while (rs.next()) {
				jx.setCellValue(27, 13, rs.getInt("cnt"));
				jx.setCellValue(29, 13, rs.getInt("val"));
			}

			// [BH]
			if (ls.size() > 0) {
				String itemClassId = "0";
				String itemId = "0";
				for (String cnd : ls) {
					// XXのいづれか
					if (cnd.split("__")[0].equals("0")) {
						itemClassId += "," + cnd.split("__")[1];
					} // 各商品
					else {
						itemId += "," + cnd.split("__")[0];
					}
				}
				sql = new StringBuilder();
				sql.append("select product_division, count(distinct customer_id) as cnt from data_sales_detail dsd\n"
						+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
						+ " inner join mst_item  mi on dsd.product_id = mi.item_id "
						+ " where dsd.product_division in (2,4)\n"
						+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
						+ " and ds.sales_date >= '" + sdf.format(bef12OneDate) + "'\n"
						+ " and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
						+ " and (mi.item_class_id in(" + itemClassId + ") or mi.item_id in (" + itemId + ") )\n"
						+ " and ds.customer_id in ( -1");
				for (Integer ag : setAG) {
					sql.append("," + ag);
				}
				sql.append(") group by product_division");
				SystemInfo.getLogger().info(sql.toString());
				rs = SystemInfo.getConnection().executeQuery(sql.toString());
				int BH1 = 0;
				while (rs.next()) {
					if (rs.getInt("product_division") == 2) {
						BH1 += rs.getInt("cnt");
					} else {
						BH1 -= rs.getInt("cnt");
					}
				}
				jx.setCellValue(5, 14, BH1);

				sql = new StringBuilder();
				sql.append("select product_division, count(distinct customer_id) as cnt, sum(ceil(dsd.product_num * dsd.product_value / (1.0 + get_tax_rate(dsd.tax_rate, ds.sales_date)))-dsd.discount_value) as val from data_sales_detail dsd\n"
						+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
						+ " inner join mst_item  mi on dsd.product_id = mi.item_id "
						+ " where dsd.product_division in (2,4)\n"
						+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
						+ " and ds.sales_date >= '" + sdf.format(baseOneDate) + "'\n"
						+ " and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
						+ " and (mi.item_class_id in(" + itemClassId + ") or mi.item_id in (" + itemId + ") )\n"
						+ " and ds.customer_id in ( -1");
				for (Integer ag : setAG) {
					sql.append("," + ag);
				}
				sql.append(") group by product_division");
				SystemInfo.getLogger().info(sql.toString());
				rs = SystemInfo.getConnection().executeQuery(sql.toString());
				int BH3 = 0;
				int BH4 = 0;
				while (rs.next()) {
					if (rs.getInt("product_division") == 2) {
						BH3 += rs.getInt("cnt");
					} else {
						BH3 -= rs.getInt("cnt");
					}
					BH4 += rs.getInt("val");
				}
				jx.setCellValue(12, 14, BH3);
				jx.setCellValue(14, 14, BH4);
				
				//[CH]
				sql = new StringBuilder();
				sql.append("select product_division, count(distinct customer_id) as cnt from data_sales_detail dsd\n"
						+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
						+ " inner join mst_item  mi on dsd.product_id = mi.item_id "
						+ " where dsd.product_division in (2,4)\n"
						+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
						+ " and ds.sales_date >= '" + sdf.format(bef12OneDate) + "'\n"
						+ " and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
						+ " and (mi.item_class_id in(" + itemClassId + ") or mi.item_id in (" + itemId + ") )\n"
						+ " and ds.customer_id in ( -1");
				for (Integer ag : setBI) {
					sql.append("," + ag);
				}
				sql.append(")  group by product_division");
				SystemInfo.getLogger().info(sql.toString());
				rs = SystemInfo.getConnection().executeQuery(sql.toString());
				
				int CH1=0;
				while (rs.next()) {
					if (rs.getInt("product_division") == 2) {
						CH1 += rs.getInt("cnt");
					} else {
						CH1 -= rs.getInt("cnt");
					}
				}
				jx.setCellValue(20, 14, CH1);
				
				sql = new StringBuilder();
				sql.append("select product_division, count(distinct customer_id) as cnt , sum(ceil(dsd.product_num * dsd.product_value / (1.0 + get_tax_rate(dsd.tax_rate, ds.sales_date)))-dsd.discount_value) as val from data_sales_detail dsd\n"
						+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
						+ " inner join mst_item  mi on dsd.product_id = mi.item_id "
						+ " where dsd.product_division in (2,4)\n"
						+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
						+ " and ds.sales_date >= '" + sdf.format(baseOneDate) + "'\n"
						+ " and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
						+ " and (mi.item_class_id in(" + itemClassId + ") or mi.item_id in (" + itemId + ") )\n"
						+ " and ds.customer_id in ( -1");
				for (Integer ag : setBI) {
					sql.append("," + ag);
				}
				sql.append(") group by product_division");
				SystemInfo.getLogger().info("[CH3 CH4]"+sql.toString());
				rs = SystemInfo.getConnection().executeQuery(sql.toString());
				int CH3 = 0;
				int CH4 = 0;
				while (rs.next()) {
					if (rs.getInt("product_division") == 2) {
						CH3 += rs.getInt("cnt");
					} else {
						CH3 -= rs.getInt("cnt");
					}
					CH4 += rs.getInt("val");
				}
				jx.setCellValue(27, 14, CH3);
				jx.setCellValue(29, 14, CH4);
				
			}
			//==============================================================================
			jx.openWorkbook();
			return true;

		} catch (SQLException e) {
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			return false;
		}
	}

	/**
	 * [D] （１）出力月から過去24カ月以内に消化履歴があり、 かつ、コースに関係なく出力月月末に未消化残がある顧客 （２）出力月から過去24カ月以内に【契約】履歴があり、 かつ、コースに関係なく出力月月末に未消化残がある顧客 ＜0604追加＞ 卒業者：Uに該当する顧客で、
	 * 出力月から過去12か月以内に商品購入履歴がない顧客。 （卒業者：Uに該当する顧客で、サロン卒業：ＢＩに含まな顧客）
	 */
	static Set<Integer> getDset(MstShop mstShop, SimpleDateFormat sdf, Date baseOneDate, Date baseEndDate, Date bef12OneDate, Date bef23OneDate) throws SQLException {

		StringBuilder sql = new StringBuilder(4000);
		sql.append("select act.customer_id\n"
				+ " from\n"
				//+ " --2年以内に契約または消化がある顧客\n"
				+ " (select distinct customer_id from \n"
				+ " data_sales_detail dsd\n"
				+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
				+ " where dsd.delete_date is null \n"
				+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
				+ " and dsd.product_division in (5,6)\n"
				+ " and ds.sales_date >= '" + sdf.format(bef23OneDate) + "'\n"
				+ " and ds.sales_date <= '" + sdf.format(baseEndDate) + "' ) as act\n"
				//+ " --契約残がある顧客\n"
				+ " ,(select distinct ds.customer_id\n"
				+ " from\n"
				+ " data_sales ds\n"
				+ " inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and dsd.product_division=5\n"
				+ " inner join data_contract dc on dsd.shop_id = dc.shop_id and dsd.slip_no = dc.slip_no and dsd.product_id = dc.product_id\n"
				+ " where\n"
				+ " dc.valid_date >= '" + sdf.format(baseEndDate) + "'\n"
				+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
				+ " and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
				+ " and not exists (select dsdz.slip_no from data_sales_detail dsdz where dsdz.delete_date is null and dsdz.product_division = 8 and dsdz.contract_shop_id = dc.shop_id and dsdz.contract_no=dc.contract_no and dsdz.contract_detail_no=dc.contract_detail_no)\n"
				+ " and dc.product_num > COALESCE((\n"
				+ "select sum(dcdx.product_num) from \n"
				+ "data_sales dsx\n"
				+ "inner join data_sales_detail dsdx on dsx.shop_id = dsdx.shop_id and dsx.slip_no=dsdx.slip_no and dsdx.delete_date is null and dsdx.product_division=6\n"
				+ "inner join data_contract_digestion dcdx on dsx.shop_id=dcdx.shop_id and dsdx.slip_no = dcdx.slip_no and dsdx.contract_no = dcdx.contract_no and dsdx.contract_detail_no=dcdx.contract_detail_no\n"
				+ "where dsx.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ "    and dcdx.contract_shop_id = dc.shop_id \n"
				+ "    and dcdx.contract_no = dc.contract_no \n"
				+ "    and dcdx.contract_detail_no = dc.contract_detail_no\n"
				+ "group by dcdx.contract_shop_id, dcdx.contract_no, dcdx.contract_detail_no\n"
				+ "),0)\n"
				+ ") as con\n"
				+ " where act.customer_id = con.customer_id	"
				+ "union \n"
				+ "select  ds.customer_id\n"
				+ "from\n"
				+ "data_sales ds\n"
				+ "inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and dsd.product_division=6\n"
				+ "inner join data_contract_digestion n on n.shop_id = ds.shop_id and n.slip_no = ds.slip_no and n.contract_no = dsd.contract_no and n.contract_detail_no = dsd.contract_detail_no\n"
				+ "inner join data_contract dc on n.contract_shop_id = dc.shop_id and n.contract_no = dc.contract_no and n.contract_detail_no = dc.contract_detail_no and dsd.product_id=  dc.product_id\n"
				+ "where "
				// 当月消化
				+ " ds.sales_date >= '" + sdf.format(baseOneDate) + "'\n"
				+ " and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
				+ " and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ " and ds.shop_id = " + mstShop.getShopID() + "\n"
				+ " and dc.product_num = (\n"
				+ "   select\n"
				+ "    sum(nx.product_num) \n"
				+ "  from\n"
				+ "    data_contract_digestion nx\n"
				+ "  where\n"
				+ "    nx.contract_shop_id = dc.shop_id \n"
				+ "    and nx.contract_no = dc.contract_no \n"
				+ "    and nx.contract_detail_no = dc.contract_detail_no \n"
				+ ")"
				// 12カ月商品購入がない
				+ " and not exists (\n"
				+ "   select distinct nds.customer_id from data_sales_detail ndsd\n"
				+ "   inner join data_sales nds on nds.shop_id= ndsd.shop_id and nds.slip_no=ndsd.slip_no and nds.delete_date is null\n"
				+ "   where ndsd.product_division=2\n"
				+ "    and ndsd.delete_date is null\n"
				+ "    and nds.shop_id = " + mstShop.getShopID() + "\n"
				+ "    and nds.sales_date >= '" + sdf.format(bef12OneDate) + "'\n"
				+ "	   and nds.customer_id = ds.customer_id\n"
				+ " ) \n"
		);
		SystemInfo.getLogger().info("[D]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());

		Set<Integer> dSet = new HashSet<Integer>();
		while (rs.next()) {
			dSet.add(rs.getInt("customer_id"));
		}
		return dSet;
	}

	/**
	 * [V] 「D」の顧客のうち、出力月＋１に予約が入っている顧客数＝次回予約が翌月の顧客数
	 */
	static Set<Integer> getVset(MstShop mstShop, SimpleDateFormat sdf, Date baseOneDate, Date baseEndDate, Date nextFDate, Date nextEndDate, Set<Integer> dSet) throws SQLException {

		StringBuilder sql = new StringBuilder();

		sql = new StringBuilder(4000);
		sql.append("select distinct customer_id from \n"
				+ "data_reservation dr\n"
				+ "inner join data_reservation_detail drd on dr.shop_id = drd.shop_id and dr.reservation_no = drd.reservation_no\n"
				+ "where \n"
				+ " dr.shop_id =" + mstShop.getShopID() + "\n"
				+ "and drd.reservation_datetime >= '" + sdf.format(nextFDate) + "'\n"
				+ "and drd.reservation_datetime <= '" + sdf.format(nextEndDate) + "'\n"
				+ "and drd.delete_date is null \n"
				+ "and drd.insert_date < '" + sdf.format(nextFDate) + "'\n"
				+ "and dr. customer_id in ( -1");
		for (Integer dc : dSet) {
			sql.append("," + dc);
		}
		sql.append(")");
		SystemInfo.getLogger().info("[V]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());

		Set<Integer> vSet = new HashSet<Integer>();
		while (rs.next()) {
			vSet.add(rs.getInt("customer_id"));
		}
		return vSet;

	}

	/**
	 * [W] 「D」の顧客から出力月に出力店舗で消化履歴がなく、かつ、出力月＋1以降の未来予約がない顧客 または、出力月に出力店舗で契約した顧客、かつ、出力月＋1以降の未来予約がない顧客（＝出力月2月に契約があり、かつ、3月以降に予約が無い） ※該当する契約が有効期限切れまたは解約している場合は対象外とする。
	 * ＜0603_追加＞ 出力タイミングにより数値変わらないようにしたい。＞＞＞登録日（insert_date）が出力月＋１以降の予約情報は集計対象としない。
	 */
	private static Set<Integer> getWset(MstShop mstShop, SimpleDateFormat sdf, Date baseOneDate, Date baseEndDate, Date nextFDate, Date nextEndDate, Set<Integer> dSet) throws SQLException {

		StringBuilder sql = new StringBuilder(4000);
		sql.append(
				//-- 出力店舗で消化履歴がある　または　予約がある顧客を除外
				" select distinct ds.customer_id from data_sales_detail dsd\n"
				+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
				+ " where dsd.delete_date is null \n"
				+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
				+ " and dsd.product_division = 6\n"
				+ " and ds.sales_date >= '" + sdf.format(baseOneDate) + "'\n"
				+ " and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ " and ds.customer_id in( -1");
		for (Integer dc : dSet) {
			sql.append("," + dc);
		}
		sql.append(") \n"
				+ " union \n"
				+ " select distinct dr.customer_id from data_reservation dr\n"
				+ " inner join data_reservation_detail drd on dr.shop_id = drd.shop_id and dr.reservation_no = drd.reservation_no\n"
				+ " where dr.shop_id =" + mstShop.getShopID() + "\n"
				+ " and drd.reservation_datetime >= '" + sdf.format(nextFDate) + "'\n"
				+ " and drd.insert_date < '" + sdf.format(nextFDate) + "'\n"
				+ " and dr.customer_id in( -1");
		for (Integer dc : dSet) {
			sql.append("," + dc);
		}
		sql.append(")");
		SystemInfo.getLogger().info("[W]1:::" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		HashSet<Integer> wList = new HashSet<Integer>();
		wList.addAll(dSet);
		while (rs.next()) {
			wList.remove((Integer) rs.getInt("customer_id"));
		}
		sql = new StringBuilder(4000);
		sql.append(
				//"-- 出力店舗で契約あり予約なし\n" +
				" select distinct ds.customer_id from data_sales_detail dsd\n"
				+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
				+ " inner join data_contract dc on dsd.shop_id = dc.shop_id and dsd.slip_no = dc.slip_no and dsd.product_id = dc.product_id\n"
				+ " where dsd.delete_date is null \n"
				+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
				+ " and not exists (select dsdz.slip_no from data_sales_detail dsdz where dsdz.delete_date is null and dsdz.product_division = 8 and dsdz.contract_shop_id = dc.shop_id and dsdz.contract_no=dc.contract_no and dsdz.contract_detail_no=dc.contract_detail_no)\n"
				+ " and dsd.product_division = 5\n"
				+ " and ds.sales_date >= '" + sdf.format(baseOneDate) + "'\n"
				+ " and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ " and not exists (\n"
				+ "    select dr.customer_id from data_reservation dr\n"
				+ "    inner join data_reservation_detail drd on dr.shop_id = drd.shop_id and dr.reservation_no = drd.reservation_no\n"
				+ "    where dr.shop_id =" + mstShop.getShopID() + "\n"
				+ "    and drd.reservation_datetime >= '" + sdf.format(nextFDate) + "'\n"
				+ "    and drd.insert_date < '" + sdf.format(nextFDate) + "'\n"
				+ "    and dr.customer_id = ds.customer_id\n"
				+ "	)\n"
				+ " and ds.customer_id in( -1");
		for (Integer dc : wList) {
			sql.append("," + dc);
		}
		sql.append(")");

		SystemInfo.getLogger().info("[W]2:::" + sql.toString());
		rs = SystemInfo.getConnection().executeQuery(sql.toString());

		while (rs.next()) {
			wList.add(rs.getInt("customer_id"));
		}
		return wList;
	}

	/**
	 * [G] コース消化を含む来店が当月１回だけの人＞純客数、かつ、１伝票に複数消化明細があって、１回とカウントとする ＜0604_追加＞ 当月、消化来店した全顧客が対象（サロン会員：Ｄに限らない）
	 */
	private static Set<Integer> getGset(MstShop mstShop, SimpleDateFormat sdf, Date baseOneDate, Date baseEndDate) throws SQLException {
		StringBuilder sql = new StringBuilder(4000);
		sql.append("select distinct ccc.customer_id from(\n"
				+ "select customer_id, count(distinct ds.slip_no) as ct from \n"
				+ " data_sales_detail dsd\n"
				+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
				+ " where dsd.delete_date is null \n"
				+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
				+ " and dsd.product_division in (6)\n"
				+ " and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
				+ " and ds.sales_date >= '" + sdf.format(baseOneDate) + "' and ds.sales_date <='" + sdf.format(baseEndDate) + "'\n"
				+ " group by customer_id) as ccc\n"
				+ " where ct = 1");
		SystemInfo.getLogger().info("[G]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		Set<Integer> setG = new HashSet<Integer>();
		while (rs.next()) {
			setG.add(rs.getInt("customer_id"));
		}
		return setG;
	}

	/**
	 * [H] コース消化を含む来店が当月２回の人＞純客数、かつ、１伝票に複数消化明細があって、１回とカウントとする ＜0604_追加＞ 当月、消化来店した全顧客が対象（サロン会員：Ｄに限らない）
	 */
	private static Set<Integer> getHset(MstShop mstShop, SimpleDateFormat sdf, Date baseOneDate, Date baseEndDate) throws SQLException {
		StringBuilder sql = new StringBuilder(4000);
		sql.append("select distinct ccc.customer_id from(\n"
				+ "select customer_id, count(distinct ds.slip_no) as ct from \n"
				+ " data_sales_detail dsd\n"
				+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
				+ " where dsd.delete_date is null \n"
				+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
				+ " and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
				+ " and dsd.product_division in (6)\n"
				+ " and ds.sales_date >= '" + sdf.format(baseOneDate) + "' and ds.sales_date <='" + sdf.format(baseEndDate) + "'\n"
				+ " group by customer_id) as ccc\n"
				+ " where ct = 2");

		SystemInfo.getLogger().info("[H]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		Set<Integer> setH = new HashSet<Integer>();
		while (rs.next()) {
			setH.add(rs.getInt("customer_id"));
		}
		return setH;
	}

	/**
	 * [P] ＜0406項目追加＞コース消化を含む来店が当月３回以上ある人＞純客数、かつ、１伝票に複数消化明細があって、１回とカウントとする ＜0604_追加＞ 当月、消化来店した全顧客が対象（サロン会員：Ｄに限らない）
	 */
	private static Set<Integer> getPset(MstShop mstShop, SimpleDateFormat sdf, Date baseOneDate, Date baseEndDate) throws SQLException {
		StringBuilder sql = new StringBuilder(4000);
		sql.append("select distinct ccc.customer_id from(\n"
				+ "select customer_id, count(distinct ds.slip_no) as ct from \n"
				+ " data_sales_detail dsd\n"
				+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
				+ " where dsd.delete_date is null \n"
				+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
				+ " and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
				+ " and dsd.product_division in (6)\n"
				+ " and ds.sales_date >= '" + sdf.format(baseOneDate) + "' and ds.sales_date <='" + sdf.format(baseEndDate) + "'\n"
				+ " group by customer_id) as ccc\n"
				+ " where ct > 2");

		SystemInfo.getLogger().info("[P]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		Set<Integer> setP = new HashSet<Integer>();
		while (rs.next()) {
			setP.add(rs.getInt("customer_id"));
		}
		return setP;
	}

	/**
	 * [J] 「サロン会員・Ｄ」のうち前回の消化来店が、出力月-1～出力-3の顧客の人数。 2019年12月の出力の場合、前回の消化来店が、2019年11月～2019年9月の顧客（但し、出力月～出力-23の24か月の来店履歴のみ抽出対象とします） （また全て、出力月-1時点で役務残がある顧客のみを対象にします）
	 * このとき「消化」内容は問わない。 また「出力月-1時点で役務残」があるコースが、出力月に“期限切れ”または“解約”となった場合、集計対象から除外する。
	 */
	private static Set<Integer> getJset(MstShop mstShop, SimpleDateFormat sdf, Date baseOneDate, Date baseEndDate, Date bef1EndDate, Date bef3OneDate, Set<Integer> dSet) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("--期間中の消化来店\n"
				+ "select distinct customer_id from data_sales_detail dsd\n"
				+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
				+ " where dsd.delete_date is null \n"
				+ " and ds.shop_id = " + mstShop.getShopID() + " and ds.sales_date <='" + sdf.format(baseEndDate) + "'\n"
				+ " and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
				+ " and ds.customer_id in ( -1");
		for (Integer dc : dSet) {
			sql.append("," + dc);
		}
		sql.append(")\n and dsd.product_division = 6\n"
				+ " and ds.sales_date between '" + sdf.format(bef3OneDate) + "' and '" + sdf.format(bef1EndDate) + "'\n"
				+ " and ds.customer_id not in (\n"
				+ "--期間後の消化来店\n"
				+ "   select distinct customer_id from data_sales_detail dsd\n"
				+ "   inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
				+ "   where dsd.delete_date is null \n"
				+ "   and ds.shop_id = " + mstShop.getShopID() + " and ds.sales_date <='" + sdf.format(baseEndDate) + "'\n"
				+ "   and ds.customer_id in ( -1");
		for (Integer dc : dSet) {
			sql.append("," + dc);
		}
		sql.append(")\n"
				+ "   and dsd.product_division = 6\n"
				+ "   and ds.sales_date between '" + sdf.format(baseOneDate) + "'and '" + sdf.format(baseEndDate) + "'\n"
				+ ")\n"
				+ " and ds.customer_id in (\n"
				+ "--期間終了時に役務残がある顧客\n"
				+ "   select distinct ds.customer_id\n"
				+ "   from data_sales ds\n"
				+ "   inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and dsd.product_division=5\n"
				+ "   inner join data_contract dc on dsd.shop_id = dc.shop_id and dsd.slip_no = dc.slip_no and dsd.product_id = dc.product_id\n"
				+ "   where dc.valid_date > '" + sdf.format(bef1EndDate) + "' and ds.sales_date <='" + sdf.format(baseEndDate) + "'\n"
				+ "   and ds.delete_date is null and not exists (select dsdz.slip_no from data_sales_detail dsdz where dsdz.delete_date is null and dsdz.product_division = 8 and dsdz.contract_shop_id = dc.shop_id and dsdz.contract_no=dc.contract_no and dsdz.contract_detail_no=dc.contract_detail_no)\n"
				+ "   and ds.customer_id in ( -1");
		for (Integer dc : dSet) {
			sql.append("," + dc);
		}
		sql.append(")\n"
				+ "   and ds.shop_id =" + mstShop.getShopID() + "\n"
				+ "   and dc.product_num > COALESCE(\n"
				+ "       (select sum(n.product_num) from data_contract_digestion n \n"
				+ "       inner join data_sales s on n.shop_id = s.shop_id and n.slip_no = s.slip_no and s.delete_date is null \n"
				+ "       where n.contract_no = dc.contract_no \n"
				+ "       and ds.shop_id = n.shop_id  and ds.sales_date <='" + sdf.format(baseEndDate) + "'\n"
				+ "       and n.contract_detail_no = dc.contract_detail_no \n"
				+ "       and n.contract_shop_id = dc.shop_id)\n"
				+ "   ,0)\n"
				+ ")"
		);

		SystemInfo.getLogger().info("[J]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		Set<Integer> setJ = new HashSet<Integer>();
		while (rs.next()) {
			setJ.add(rs.getInt("customer_id"));
		}
		return setJ;
	}

	/**
	 * [K] ＜0604_変更＞ 「サロン会員・Ｄ」のうち前回の消化来店が、出力月-4～出力-12の顧客の人数。 2019年12月の出力の場合、前回の消化来店が、2019年8月～2018年12月の顧客（但し、出力月～出力-23の24か月の来店履歴のみ抽出対象とします）
	 * （また全て、出力月-1時点で役務残がある顧客のみを対象にします） このとき「消化」内容は問わない。 また「出力月-1時点で役務残」があるコースが、出力月に“期限切れ”または“解約”となった場合、集計対象から除外する。
	 */
	private static Set<Integer> getKset(MstShop mstShop, SimpleDateFormat sdf, Date baseOneDate, Date baseEndDate, Date bef4EndDate, Date bef12OneDate, Set<Integer> dSet) throws SQLException {

		// 期間計算
		StringBuilder sql = new StringBuilder();
		sql.append("--期間中の消化来店\n"
				+ "select distinct customer_id from data_sales_detail dsd\n"
				+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
				+ " where dsd.delete_date is null \n"
				+ " and ds.shop_id = " + mstShop.getShopID() + "\n"
				+ " and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
				+ " and ds.customer_id in ( -1");
		for (Integer dc : dSet) {
			sql.append("," + dc);
		}
		sql.append(")\n and dsd.product_division = 6\n"
				+ " and ds.sales_date between '" + sdf.format(bef12OneDate) + "' and '" + sdf.format(bef4EndDate) + "'\n"
				+ " and ds.customer_id not in (\n"
				+ "--期間後の消化来店\n"
				+ "   select distinct customer_id from data_sales_detail dsd\n"
				+ "   inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
				+ "   where dsd.delete_date is null \n"
				+ "   and ds.shop_id = " + mstShop.getShopID() + "\n"
				+ "   and ds.customer_id in ( -1");
		for (Integer dc : dSet) {
			sql.append("," + dc);
		}
		sql.append(")\n"
				+ "   and dsd.product_division = 6\n"
				+ "   and ds.sales_date > '" + sdf.format(bef4EndDate) + "' and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ ")\n"
				+ " and ds.customer_id in (\n"
				+ "--期間終了時に役務残がある顧客\n"
				+ "   select distinct ds.customer_id\n"
				+ "   from data_sales ds\n"
				+ "   inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and dsd.product_division=5\n"
				+ "   inner join data_contract dc on dsd.shop_id = dc.shop_id and dsd.slip_no = dc.slip_no and dsd.product_id = dc.product_id\n"
				+ "   where dc.valid_date > '" + sdf.format(baseOneDate) + "'\n"
				+ "   and ds.delete_date is null and not exists (select dsdz.slip_no from data_sales_detail dsdz where dsdz.delete_date is null and dsdz.product_division = 8 and dsdz.contract_shop_id = dc.shop_id and dsdz.contract_no=dc.contract_no and dsdz.contract_detail_no=dc.contract_detail_no)\n"
				+ "   and ds.customer_id in ( -1");
		for (Integer dc : dSet) {
			sql.append("," + dc);
		}
		sql.append(")\n"
				+ "   and ds.shop_id =" + mstShop.getShopID() + "\n"
				+ "   and dc.product_num > COALESCE(\n"
				+ "       (select sum(n.product_num) from data_contract_digestion n \n"
				+ "       inner join data_sales s on n.shop_id = s.shop_id and n.slip_no = s.slip_no and s.delete_date is null \n"
				+ "       where n.contract_no = dc.contract_no \n"
				+ "       and ds.shop_id = n.shop_id and ds.delete_date is null \n"
				+ "       and n.contract_detail_no = dc.contract_detail_no \n"
				+ "       and n.contract_shop_id = dc.shop_id)\n"
				+ "   ,0)\n"
				+ ")"
		);
		SystemInfo.getLogger().info("[K]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		Set<Integer> setK = new HashSet<Integer>();
		while (rs.next()) {
			setK.add(rs.getInt("customer_id"));
		}
		return setK;
	}

	/**
	 * [L] 「サロン会員・Ｄ」のうち前回の消化来店が、出力月-13以前の顧客の人数。 2019年12月の出力の場合、前回の消化来店が、2018年11月以前の顧客すべて（但し、出力月～出力-23の24か月の来店履歴のみ抽出対象とします） （また全て、出力月-1時点で役務残がある顧客のみを対象にします）
	 * このとき「消化」内容は問わない。 また「出力月-1時点で役務残」があるコースが、出力月に“期限切れ”または“解約”となった場合、集計対象から除外する。
	 */
	private static Set<Integer> getLset(MstShop mstShop, SimpleDateFormat sdf, Date baseOneDate, Date baseEndDate, Date bef12EndDate, Date bef23OneDate, Set<Integer> dSet) throws SQLException {
		StringBuilder sql = new StringBuilder();

		sql.append("--期間中の消化来店\n"
				+ "select distinct customer_id from data_sales_detail dsd\n"
				+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
				+ " where dsd.delete_date is null \n"
				+ " and ds.shop_id = " + mstShop.getShopID() + "\n"
				+ " and ds.customer_id in ( -1");
		for (Integer dc : dSet) {
			sql.append("," + dc);
		}
		sql.append(")\n and dsd.product_division = 6\n"
				+ " and ds.sales_date between '" + sdf.format(bef23OneDate) + "' and '" + sdf.format(bef12EndDate) + "'\n"
				+ " and ds.customer_id not in (\n"
				+ "--期間後の消化来店\n"
				+ "   select distinct customer_id from data_sales_detail dsd\n"
				+ "   inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
				+ "   where dsd.delete_date is null \n"
				+ "   and ds.shop_id = " + mstShop.getShopID() + "\n"
				+ "   and ds.customer_id in ( -1");
		for (Integer dc : dSet) {
			sql.append("," + dc);
		}
		sql.append(")\n"
				+ "   and dsd.product_division = 6\n"
				+ "   and ds.sales_date > '" + sdf.format(bef12EndDate) + "'\n" // and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ ")\n"
				+ " and ds.customer_id in (\n"
				+ "--期間終了時に役務残がある顧客\n"
				+ "   select distinct ds.customer_id\n"
				+ "   from data_sales ds\n"
				+ "   inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and dsd.product_division=5\n"
				+ "   inner join data_contract dc on dsd.shop_id = dc.shop_id and dsd.slip_no = dc.slip_no and dsd.product_id = dc.product_id\n"
				+ "   where dc.valid_date > '" + sdf.format(baseOneDate) + "'\n"
				+ "   and ds.delete_date is null and not exists (select dsdz.slip_no from data_sales_detail dsdz where dsdz.delete_date is null and dsdz.product_division = 8 and dsdz.contract_shop_id = dc.shop_id and dsdz.contract_no=dc.contract_no and dsdz.contract_detail_no=dc.contract_detail_no)\n"
				+ "   and ds.customer_id in ( -1");
		for (Integer dc : dSet) {
			sql.append("," + dc);
		}
		sql.append(")\n"
				+ "   and ds.shop_id =" + mstShop.getShopID() + "\n"
				+ "   and dc.product_num > COALESCE(\n"
				+ "       (select sum(n.product_num) from data_contract_digestion n \n"
				+ "       inner join data_sales s on n.shop_id = s.shop_id and n.slip_no = s.slip_no and s.delete_date is null \n"
				+ "       where n.contract_no = dc.contract_no \n"
				+ "       and ds.shop_id = n.shop_id  \n"
				+ "       and n.contract_detail_no = dc.contract_detail_no \n"
				+ "       and n.contract_shop_id = dc.shop_id)\n"
				+ "   ,0)\n"
				+ ")"
		);
		SystemInfo.getLogger().info("[L]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		Set<Integer> set = new HashSet<Integer>();
		while (rs.next()) {
			set.add(rs.getInt("customer_id"));
		}
		return set;
	}

	/**
	 * [M] ＜0604_追加＞ 当月、消化来店した顧客が対象（サロン会員：Ｄに限らない） aft base ＜0604_変更＞ 出力月に消化来店があり、前回来店が、出力月-2～出力月-4の顧客。 2019年12月出力の際、
	 * 当月、消化来店があり、前回の消化来店が、2019年10月～8月の顧客（但し、出力月～出力-23の24か月の来店履歴のみ抽出対象とします） （また全て、出力月-1時点で役務残がある顧客のみを対象にします） このとき「消化」内容は問わない。
	 * また「出力月-1時点で役務残」があるコースが、出力月に“期限切れ”または“解約”となった場合、集計対象から除外する。
	 */
	private static Set<Integer> getMset(MstShop mstShop, SimpleDateFormat sdf, Date baseOneDate, Date baseEndDate, Date bef7OneDate, Date bef4EndDate, Date bef1EndDate) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("--当月中の消化来店\n"
				+ "select distinct customer_id from data_sales_detail dsd\n"
				+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null \n"
				+ " where dsd.delete_date is null \n"
				+ " and ds.shop_id = " + mstShop.getShopID() + "\n"
				+ " and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
				+ " and dsd.product_division = 6\n"
				+ " and ds.sales_date between '" + sdf.format(baseOneDate) + "' and '" + sdf.format(baseEndDate) + "'\n"
				+ " and ds.customer_id in (\n"
				+ "--期間中の消化来店\n"
				+ "   select distinct customer_id from data_sales_detail dsd\n"
				+ "   inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no \n"
				+ "   where dsd.delete_date is null \n"
				+ "   and ds.shop_id = " + mstShop.getShopID() + "\n"
				+ "   and dsd.product_division = 6\n"
				+ "   and ds.sales_date between '" + sdf.format(bef7OneDate) + "' and '" + sdf.format(bef4EndDate) + "'\n"
				+ ")\n"
				+ " and ds.customer_id not in (\n"
				+ "--期間後の消化来店\n"
				+ "   select distinct customer_id from data_sales_detail dsd\n"
				+ "   inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no \n"
				+ "   where dsd.delete_date is null \n"
				+ "   and ds.shop_id = " + mstShop.getShopID() + "\n"
				+ "   and dsd.product_division = 6\n"
				+ "   and ds.sales_date > '" + sdf.format(bef4EndDate) + "' and ds.sales_date <= '" + sdf.format(bef1EndDate) + "'\n"
				+ ")\n"
				+ " and ds.customer_id in (\n"
				+ "--解約・期限切れなし\n"
				+ "   select distinct ds.customer_id\n"
				+ "   from data_sales ds\n"
				+ "   inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and dsd.product_division=5\n"
				+ "   inner join data_contract dc on dsd.shop_id = dc.shop_id and dsd.slip_no = dc.slip_no and dsd.product_id = dc.product_id\n"
				+ "   where dc.valid_date > '" + sdf.format(baseOneDate) + "'\n"
				+ "   and ds.delete_date is null and not exists (select dsdz.slip_no from data_sales_detail dsdz where dsdz.delete_date is null and dsdz.product_division = 8 and dsdz.contract_shop_id = dc.shop_id and dsdz.contract_no=dc.contract_no and dsdz.contract_detail_no=dc.contract_detail_no)\n"
				+ "   and ds.shop_id =" + mstShop.getShopID() + "\n"
				//					+ "   and dc.product_num > COALESCE(\n"
				//					+ "       (select sum(n.product_num) from data_contract_digestion n \n"
				//					+ "       inner join data_sales s on n.shop_id = s.shop_id and n.slip_no = s.slip_no and s.delete_date is null \n"
				//					+ "       where n.contract_no = dc.contract_no \n"
				//					+ "       and ds.shop_id = n.shop_id  \n"
				//					+ "       and n.contract_detail_no = dc.contract_detail_no \n"
				//					+ "       and n.contract_shop_id = dc.shop_id)\n"
				//					+ "   ,0)\n"
				+ ")"
		);

		SystemInfo.getLogger().info("[M]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		Set<Integer> set = new HashSet<Integer>();
		while (rs.next()) {
			set.add(rs.getInt("customer_id"));
		}
		return set;
	}

	/**
	 * [N] ＜0604_追加＞ 当月、消化来店した顧客が対象（サロン会員：Ｄに限らない） ＜0604_変更＞ 出力月に消化来店があり、前回来店が、出力月-5以前の顧客。 2019年12月出力の際、
	 * 当月、消化来店があり、前回の消化来店が、2019年7月以前の顧客すべて（但し、出力月～出力-23の24か月の来店履歴のみ抽出対象とします） （また全て、出力月-1時点で役務残がある顧客のみを対象にします） このとき「消化」内容は問わない。
	 * また「出力月-1時点で役務残」があるコースが、出力月に“期限切れ”または“解約”となった場合、集計対象から除外する
	 */
	private static Set<Integer> getNset(MstShop mstShop, SimpleDateFormat sdf, Date baseOneDate, Date baseEndDate, Date bef23OneDate, Date bef7endDate, Date bef1EndDate) throws SQLException {
		StringBuilder sql = new StringBuilder();

		sql.append("--当月中の消化来店\n"
				+ "select distinct customer_id from data_sales_detail dsd\n"
				+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
				+ " where dsd.delete_date is null \n"
				+ " and ds.shop_id = " + mstShop.getShopID() + "\n"
				+ " and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
				+ " and dsd.product_division = 6\n"
				+ " and ds.sales_date between '" + sdf.format(baseOneDate) + "' and '" + sdf.format(baseEndDate) + "'\n"
				+ " and ds.customer_id in (\n"
				+ "--期間中の消化来店\n"
				+ "   select distinct customer_id from data_sales_detail dsd\n"
				+ "   inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no \n"
				+ "   where dsd.delete_date is null \n"
				+ "   and ds.shop_id = " + mstShop.getShopID() + "\n"
				+ "   and dsd.product_division = 6\n"
				+ "   and ds.sales_date between '" + sdf.format(bef23OneDate) + "' and '" + sdf.format(bef7endDate) + "'\n"
				+ ")\n"
				+ " and ds.customer_id not in (\n"
				+ "--期間後の消化来店\n"
				+ "   select distinct customer_id from data_sales_detail dsd\n"
				+ "   inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no \n"
				+ "   where dsd.delete_date is null \n"
				+ "   and ds.shop_id = " + mstShop.getShopID() + "\n"
				+ "   and dsd.product_division = 6\n"
				+ "   and ds.sales_date > '" + sdf.format(bef7endDate) + "' and ds.sales_date <= '" + sdf.format(bef1EndDate) + "'\n"
				+ ")\n"
				+ " and ds.customer_id in (\n"
				+ "--解約・期限切れなし\n"
				+ "   select distinct ds.customer_id\n"
				+ "   from data_sales ds\n"
				+ "   inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and dsd.product_division=5\n"
				+ "   inner join data_contract dc on dsd.shop_id = dc.shop_id and dsd.slip_no = dc.slip_no and dsd.product_id = dc.product_id\n"
				+ "   where dc.valid_date > '" + sdf.format(baseOneDate) + "'\n"
				+ "   and ds.delete_date is null and not exists (select dsdz.slip_no from data_sales_detail dsdz where dsdz.delete_date is null and dsdz.product_division = 8 and dsdz.contract_shop_id = dc.shop_id and dsdz.contract_no=dc.contract_no and dsdz.contract_detail_no=dc.contract_detail_no)\n"
				+ "   and ds.shop_id =" + mstShop.getShopID() + "\n"
				//					+ "   and dc.product_num > COALESCE(\n"
				//					+ "       (select sum(n.product_num) from data_contract_digestion n \n"
				//					+ "       inner join data_sales s on n.shop_id = s.shop_id and n.slip_no = s.slip_no and s.delete_date is null \n"
				//					+ "       where n.contract_no = dc.contract_no \n"
				//					+ "       and ds.shop_id = n.shop_id  \n"
				//					+ "       and n.contract_detail_no = dc.contract_detail_no \n"
				//					+ "       and n.contract_shop_id = dc.shop_id)\n"
				//					+ "   ,0)\n"
				+ ")"
		);
		SystemInfo.getLogger().info("[N]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		Set<Integer> set = new HashSet<Integer>();
		while (rs.next()) {
			set.add(rs.getInt("customer_id"));
		}
		return set;
	}

	/**
	 * [O] 「Ｄ」の中で、出力月＋4～出力月＋12の期間に「コース」の有効期限が切れる顧客数
	 */
	private static Set<Integer> getOset(MstShop mstShop, SimpleDateFormat sdf, Date aft12EndDate, Date aft4OneDate) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("--[O]期限切れがある顧客\n"
				+ "   select distinct customer_id \n"
				+ "   from data_sales ds\n"
				+ "   inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and dsd.product_division=5\n"
				+ "   inner join data_contract dc on dsd.shop_id = dc.shop_id and dsd.slip_no = dc.slip_no and dsd.product_id = dc.product_id\n"
				+ "   where dc.valid_date between '" + sdf.format(aft4OneDate) + "' and '" + sdf.format(aft12EndDate) + "'\n"
				+ "   and ds.delete_date is null and not exists (select dsdz.slip_no from data_sales_detail dsdz where dsdz.delete_date is null and dsdz.product_division = 8 and dsdz.contract_shop_id = dc.shop_id and dsdz.contract_no=dc.contract_no and dsdz.contract_detail_no=dc.contract_detail_no)\n"
				+ "   and ds.shop_id =" + mstShop.getShopID() + "\n"
				+ " and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
//				+ "   and ds.customer_id in ( -1");
//		for (Integer dc : dSet) {
//			sql.append("," + dc);
//		}
//		sql.append(")\n"
				+ "   and dc.product_num > COALESCE(\n"
				+ "       (select sum(n.product_num) from data_contract_digestion n \n"
				+ "       inner join data_sales s on n.shop_id = s.shop_id and n.slip_no = s.slip_no and s.delete_date is null \n"
				+ "       where n.contract_no = dc.contract_no \n"
				+ "       and ds.shop_id = n.shop_id  \n"
				+ "       and n.contract_detail_no = dc.contract_detail_no \n"
				+ "       and n.contract_shop_id = dc.shop_id)\n"
				+ "   ,0)\n"
		);
		SystemInfo.getLogger().info("[O]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		Set<Integer> set = new HashSet<Integer>();
		while (rs.next()) {
			set.add(rs.getInt("customer_id"));
		}
		return set;
	}

	/**
	 * [R] ＜0604_変更＞ 出力店舗で、出力月-34～出力月-32で契約している顧客が対象。 出力月・月末に役務残があり、出力月＋1～出力月＋3の期間に「コース」の有効期限が切れる顧客数。 ※解約済み契約は集計に該当しない ※消化来店の有無は関係なし。
	 * 2020年2月出力＞2020年3月～5月に有効期限が切れるコース契約を所有する顧客＝有効期限３６カ月のため、2017年4月～6月にコース契約した人
	 */
	private static Set<Integer> getRset(MstShop mstShop, SimpleDateFormat sdf, Date bef32OneDate, Date bef35TwoDate) throws SQLException {
		StringBuilder sql = new StringBuilder();

		sql.append("--[R]期限切れがある顧客\n"
				+ "   select distinct customer_id \n"
				+ "   from data_sales ds\n"
				+ "   inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and dsd.product_division=5\n"
				+ "   inner join data_contract dc on dsd.shop_id = dc.shop_id and dsd.slip_no = dc.slip_no and dsd.product_id = dc.product_id\n"
				+ "   where dc.insert_date between '" + sdf.format(bef32OneDate) + "' and '" + sdf.format(bef35TwoDate) + "'\n"
				+ "   and ds.delete_date is null and not exists (select dsdz.slip_no from data_sales_detail dsdz where dsdz.delete_date is null and dsdz.product_division = 8 and dsdz.contract_shop_id = dc.shop_id and dsdz.contract_no=dc.contract_no and dsdz.contract_detail_no=dc.contract_detail_no)\n"
				+ "   and ds.shop_id =" + mstShop.getShopID() + "\n"
				+ "   and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
				//					+ "   and ds.customer_id in ( -1");
				//			for (Integer dc : dSet) { sql.append("," + dc);}
				//			sql.append(")\n"
//				+ "   and dc.product_num > COALESCE(\n"
//				+ "       (select sum(n.product_num) from data_contract_digestion n \n"
//				+ "       inner join data_sales s on n.shop_id = s.shop_id and n.slip_no = s.slip_no and s.delete_date is null \n"
//				+ "       where n.contract_no = dc.contract_no \n"
//				+ "       and ds.shop_id = n.shop_id  \n"
//				+ "       and n.contract_detail_no = dc.contract_detail_no \n"
//				+ "       and n.contract_shop_id = dc.shop_id)\n"
//				+ "   ,0)\n"
		);
		SystemInfo.getLogger().info("[R]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		Set<Integer> set = new HashSet<Integer>();
		while (rs.next()) {
			set.add(rs.getInt("customer_id"));
		}
		return set;
	}

	/**
	 * [Q] 「D」の中で、出力月に「技術＞分類・内新（technic__class_id=10）＞技術名・美容器案内（technic__id=38）」を明細に含む顧客　※純顧客
	 */
	private static Set<Integer> getQset(MstShop mstShop, SimpleDateFormat sdf, Date baseOneDate, Date baseEndDate, Set<Integer> dSet) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("select \n"
				+ "distinct ds.customer_id \n"
				+ "from\n"
				+ "data_sales ds\n"
				+ "inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and dsd.product_division=1 and dsd.product_id='38'\n"
				+ "where "
				// 当月消化
				+ "     ds.sales_date >= '" + sdf.format(baseOneDate) + "' and ds.delete_date is null\n"
				+ " and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ " and ds.shop_id = " + mstShop.getShopID() + "\n"
				+ " and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
				+ " and ds.customer_id in ( -1");
		for (Integer dc : dSet) {
			sql.append("," + dc);
		}
		sql.append(") ");
		SystemInfo.getLogger().info("[Q]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		Set<Integer> set = new HashSet<Integer>();
		while (rs.next()) {
			set.add(rs.getInt("customer_id"));
		}
		return set;
	}

	/**
	 * [S] 「Ｄ」の中で、出力月に出力店舗で契約した顧客で、かつ　同じ伝票内に技術分類・入会金（technic__class_id=3）の技術明細がある顧客数 ※出力月に解約している契約は対象外とする。
	 */
	private static Set<Integer> getSset(MstShop mstShop, SimpleDateFormat sdf, Date baseOneDate, Date baseEndDate, Set<Integer> dSet) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("select  distinct ds.customer_id \n"
				+ "from data_sales ds\n"
				+ " inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and dsd.product_division=5\n"
				+ " inner join data_contract dc on dsd.shop_id = dc.shop_id and dsd.slip_no = dc.slip_no and dsd.product_id = dc.product_id\n"
				+ " where\n"
				+ " ds.shop_id =" + mstShop.getShopID() + "\n"
				+ " and ds.delete_date is null and not exists (select dsdz.slip_no from data_sales_detail dsdz where dsdz.delete_date is null and dsdz.product_division = 8 and dsdz.contract_shop_id = dc.shop_id and dsdz.contract_no=dc.contract_no and dsdz.contract_detail_no=dc.contract_detail_no)\n"
				+ "and ds. customer_id in ( -1");
		for (Integer dc : dSet) {
			sql.append("," + dc);
		}
		sql.append(") "
				+ "and ds.sales_date >= '" + sdf.format(baseOneDate) + "'\n"
				+ "and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ " and exists (\n"
				+ "  select dsdx.slip_detail_no from data_sales_detail dsdx\n"
				+ "  left join mst_technic mt on dsdx.product_id = mt.technic_id and mt.technic_class_id = 3\n"
				+ "   where ds.shop_id = dsdx.shop_id and ds.slip_no = dsdx.slip_no and dsdx.product_division=1\n"
				+ " )\n");

		SystemInfo.getLogger().info("[S]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		Set<Integer> set = new HashSet<Integer>();
		while (rs.next()) {
			set.add(rs.getInt("customer_id"));
		}
		return set;
	}

	/**
	 * [T] 「Ｄ」の中で、出力月に出力店舗で契約した顧客で、かつ過去に出力店舗で一度以上、契約履歴がある顧客（解約・終了・有効期限切れ問わず）
	 */
	private static Set<Integer> getTset(MstShop mstShop, SimpleDateFormat sdf, Date baseOneDate, Date baseEndDate, Set<Integer> dSet) throws SQLException {

		StringBuilder sql = new StringBuilder();
		sql.append("select distinct ds.customer_id \n"
				+ "from data_sales ds\n"
				+ " inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and dsd.product_division=5\n"
				+ " inner join data_contract dc on dsd.shop_id = dc.shop_id and dsd.slip_no = dc.slip_no and dsd.product_id = dc.product_id\n"
				+ " where\n"
				+ " ds.shop_id =" + mstShop.getShopID() + "\n"
				+ " and ds.delete_date is null "//and not exists (select dsdz.slip_no from data_sales_detail dsdz where dsdz.delete_date is null and dsdz.product_division = 8 and dsdz.contract_shop_id = dc.shop_id and dsdz.contract_no=dc.contract_no and dsdz.contract_detail_no=dc.contract_detail_no)\n"
				+ " and ds. customer_id in ( -1");
		for (Integer dc : dSet) {
			sql.append("," + dc);
		}
		sql.append(") "
				+ "and ds.sales_date >= '" + sdf.format(baseOneDate) + "'\n"
				+ "and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ " and exists (\n"
				+ "  select dsdx.slip_detail_no from data_sales_detail dsdx\n"
				+ "  inner join data_sales dsx on dsx.shop_id = dsdx.shop_id and dsx.slip_no = dsdx.slip_no\n"
				+ "  inner join data_contract dcx on dsdx.shop_id = dcx.shop_id and dsdx.slip_no = dcx.slip_no and dsdx.product_id = dcx.product_id\n"
				+ "  where dsdx.product_division=5 and dsx.customer_id = ds.customer_id\n"
				+ "  and dsx.shop_id =" + mstShop.getShopID() + "\n"
				+ "  and dsx.sales_date < '" + sdf.format(baseOneDate) + "'\n"
				+ " )\n");
		SystemInfo.getLogger().info("[T]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		Set<Integer> set = new HashSet<Integer>();
		while (rs.next()) {
			set.add(rs.getInt("customer_id"));
		}
		return set;
	}

	/**
	 * [U] ＜0604_変更＞ 当月、消化来店した顧客で出力店舗で契約した全顧客が対象。 出力月の来店・消化で「契約履歴」欄のステータスに｢契約中｣がなくなった顧客 (出力月に解約／期限切れとなった契約は集計対象にしない）。 ※消化の来店履歴で残数「０」になった人だけが対象
	 */
	private static Set<Integer> getUset(MstShop mstShop, SimpleDateFormat sdf, Date baseOneDate, Date baseEndDate) throws SQLException {
		StringBuilder sql = new StringBuilder(4000);
		sql.append("--[U]当月中の消化来店\n"
				+ "select distinct customer_id from data_sales_detail dsd\n"
				+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
				+ " where dsd.delete_date is null \n"
				+ " and ds.shop_id = " + mstShop.getShopID() + "\n"
				+ " and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
				+ " and dsd.product_division = 6\n"
				+ " and ds.sales_date between '" + sdf.format(baseOneDate) + "' and '" + sdf.format(baseEndDate) + "'\n"
				+ " and ds.customer_id in (\n"
				+ "--解約・期限切れなしで消化完了\n"
				+ "   select distinct ds.customer_id\n"
				+ "   from data_sales ds\n"
				+ "   inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and dsd.product_division=5\n"
				+ "   inner join data_contract dc on dsd.shop_id = dc.shop_id and dsd.slip_no = dc.slip_no and dsd.product_id = dc.product_id\n"
				+ "   where dc.valid_date > '" + sdf.format(baseOneDate) + "'\n"
				+ "   and not exists (select dsdz.slip_no from data_sales_detail dsdz where dsdz.delete_date is null and dsdz.product_division = 8 and dsdz.contract_shop_id = dc.shop_id and dsdz.contract_no=dc.contract_no and dsdz.contract_detail_no=dc.contract_detail_no)\n"
				+ "   and ds.shop_id =" + mstShop.getShopID() + "\n"
				+ "   and dc.product_num = COALESCE(\n"
				+ "       (select sum(n.product_num) from data_contract_digestion n \n"
				+ "       inner join data_sales s on n.shop_id = s.shop_id and n.slip_no = s.slip_no and s.delete_date is null \n"
				+ "       where n.contract_no = dc.contract_no \n"
				+ "       and ds.sales_date <= '" + sdf.format(baseEndDate) + "'  \n"
				+ "       and ds.shop_id = n.shop_id  \n"
				+ "       and n.contract_detail_no = dc.contract_detail_no \n"
				+ "       and n.contract_shop_id = dc.shop_id)\n"
				+ "   ,0)\n"
				+ ") and ds.customer_id not in (select distinct ds.customer_id\n"
				+ "     from\n"
				+ " data_sales ds\n"
				+ " inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and dsd.product_division=5\n"
				+ " inner join data_contract dc on dsd.shop_id = dc.shop_id and dsd.slip_no = dc.slip_no and dsd.product_id = dc.product_id\n"
				+ " where\n"
				+ " dc.valid_date >= '" + sdf.format(baseEndDate) + "'\n"
				+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
				+ " and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
				+ " and not exists (select dsdz.slip_no from data_sales_detail dsdz where dsdz.delete_date is null and dsdz.product_division = 8 and dsdz.contract_shop_id = dc.shop_id and dsdz.contract_no=dc.contract_no and dsdz.contract_detail_no=dc.contract_detail_no)\n"
				+ " and dc.product_num > COALESCE((\n"
				+ "select sum(dcdx.product_num) from \n"
				+ "data_sales dsx\n"
				+ "inner join data_sales_detail dsdx on dsx.shop_id = dsdx.shop_id and dsx.slip_no=dsdx.slip_no and dsdx.delete_date is null and dsdx.product_division=6\n"
				+ "inner join data_contract_digestion dcdx on dsx.shop_id=dcdx.shop_id and dsdx.slip_no = dcdx.slip_no and dsdx.contract_no = dcdx.contract_no and dsdx.contract_detail_no=dcdx.contract_detail_no\n"
				+ "where dsx.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ "    and dcdx.contract_shop_id = dc.shop_id \n"
				+ "    and dcdx.contract_no = dc.contract_no \n"
				+ "    and dcdx.contract_detail_no = dc.contract_detail_no\n"
				+ "group by dcdx.contract_shop_id, dcdx.contract_no, dcdx.contract_detail_no\n"
				+ "),0)\n"
				+ ")\n"
		);
		SystemInfo.getLogger().info("[U]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		Set<Integer> set = new HashSet<Integer>();
		while (rs.next()) {
			set.add(rs.getInt("customer_id"));
		}
		return set;
	}

	/**
	 * [AG] 「Ｄ」のうち　出力対象月から過去12カ月以内に商品購入履歴（data_sales.product_division=2）がある顧客数　※商品返品は含まない。
	 */
	private static Set<Integer> getAGset(MstShop mstShop, SimpleDateFormat sdf, Date baseOneDate, Date baseEndDate, Date bef12OneDate, Set<Integer> dSet) throws SQLException {

		StringBuilder sql = new StringBuilder(4000);

		sql = new StringBuilder();
		sql.append("select distinct customer_id from data_sales_detail dsd\n"
				+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
				+ " where dsd.product_division = 2\n"
				+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
				+ " and ds.sales_date >= '" + sdf.format(bef12OneDate) + "'\n"
				+ " and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ " and ds. customer_id in ( -1");
		for (Integer dc : dSet) {
			sql.append("," + dc);
		}
		sql.append(")");
		SystemInfo.getLogger().info("[AG]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());

		Set<Integer> setAG = new HashSet<Integer>();
		while (rs.next()) {
			setAG.add(rs.getInt("customer_id"));
		}
		return setAG;

	}

	/**
	 * [AI] AGのうち【前回購入履歴が】出力月-1にあり、かつ、出力月に商品購入履歴がない顧客数 →　対象年月：2018年2月とした場合、2018年1月に商品購入履歴があり、かつ　2018年2月に商品購入履歴がない顧客数　※商品分類は問わない。
	 */
	private static Set<Integer> getAIset(MstShop mstShop, SimpleDateFormat sdf, Date baseOneDate, Date baseEndDate, Date bef1EndDate, Date bef1OneDate, Set<Integer> agSet) throws SQLException {

		StringBuilder sql = new StringBuilder();
		//--先月商品購入がある
		sql.append("select distinct ds.customer_id from data_sales_detail dsd\n"
				+ "inner join data_sales ds on ds.shop_id= dsd.shop_id and ds.slip_no=dsd.slip_no and ds.delete_date is null\n"
				+ "where dsd.product_division=2\n"
				+ " and dsd.delete_date is null\n"
				+ " and ds.shop_id = " + mstShop.getShopID() + "\n"
				+ " and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
				+ " and ds.sales_date <= '" + sdf.format(bef1EndDate) + "'\n"
				+ " and ds.sales_date >= '" + sdf.format(bef1OneDate) + "'\n"
				+ " --今月商品購入がない\n"
				+ " and not exists (\n"
				+ "   select distinct nds.customer_id from data_sales_detail ndsd\n"
				+ "   inner join data_sales nds on nds.shop_id= ndsd.shop_id and nds.slip_no=ndsd.slip_no and nds.delete_date is null\n"
				+ "   where ndsd.product_division=2\n"
				+ "    and ndsd.delete_date is null\n"
				+ " and nds.shop_id = " + mstShop.getShopID() + "\n"
				+ "    and nds.sales_date >= '" + sdf.format(baseOneDate) + "'\n"
				+ "    and nds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ "	and nds.customer_id = ds.customer_id\n"
				+ " ) \n"
				+ " and ds.customer_id in ( -1");
		for (Integer dc : agSet) {
			sql.append("," + dc);
		}
		sql.append(") "
		);
		SystemInfo.getLogger().info("[AI]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		Set<Integer> setAI = new HashSet<Integer>();
		while (rs.next()) {
			setAI.add(rs.getInt("customer_id"));
		}
		return setAI;
	}

	/* [AJ]
	 *＜0609_変更＞
	 * CMのうち、前回の商品購入履歴が、出力月-3以前の顧客数
	 *　→　対象年月：2018年2月とした場合、前回の商品購入履歴が、2017年12月以前の顧客　　※商品分類は問わない。
	 */
	private static Set<Integer> getAJset(MstShop mstShop, SimpleDateFormat sdf, Date bef2EndDate, Set<Integer> setAG) throws SQLException {
		StringBuilder sql = new StringBuilder();
		//--商品購入がある
		sql.append("select distinct ds.customer_id from data_sales_detail dsd\n"
				+ "inner join data_sales ds on ds.shop_id= dsd.shop_id and ds.slip_no=dsd.slip_no and ds.delete_date is null\n"
				+ "where dsd.product_division=2\n"
				+ " and dsd.delete_date is null\n"
				+ " and ds.shop_id = " + mstShop.getShopID() + "\n"
				+ " and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
				+ " and ds.sales_date <= '" + sdf.format(bef2EndDate) + "'\n"
				+ " --今月商品購入がない\n"
				+ " and not exists (\n"
				+ "   select distinct nds.customer_id from data_sales_detail ndsd\n"
				+ "   inner join data_sales nds on nds.shop_id= ndsd.shop_id and nds.slip_no=ndsd.slip_no and nds.delete_date is null\n"
				+ "   where ndsd.product_division=2\n"
				+ "    and ndsd.delete_date is null\n"
				+ " and nds.shop_id = " + mstShop.getShopID() + "\n"
				+ "    and nds.sales_date > '" + sdf.format(bef2EndDate) + "'\n"
				+ "	and nds.customer_id = ds.customer_id\n"
				+ " ) \n"
				+ " and ds.customer_id in ( -1");
		for (Integer dc : setAG) {
			sql.append("," + dc);
		}
		sql.append(") "
		);
		SystemInfo.getLogger().info("[AJ]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		Set<Integer> setAJ = new HashSet<Integer>();
		while (rs.next()) {
			setAJ.add(rs.getInt("customer_id"));
		}
		return setAJ;
	}

	/**
	 * [BI] ●商品愛用者・BI＜0325変更＞ （１） コース契約履歴があり、出力月月末に未消化残がない顧客 かつ出力月から過去12カ月以内に商品購入履歴がある人 （２） 出力月月末にコース未消化残があり かつ　コース契約履歴が出力月から過去24か月以内にない顧客 かつ
	 * 消化履歴が出力月から過去25か月以前の顧客 かつ　出力月から過去12か月以内に商品購入履歴がある顧客 ＜0410追加＞ （３）ただしＣＩにも含まれる場合は、ＢＩにカウントしない。
	 */
	private static Set<Integer> getBIset(MstShop mstShop, SimpleDateFormat sdf, Date baseOneDate, Date baseEndDate, Date bef12OneDate, Date bef23OneDate, Set<Integer> setCI) throws SQLException {

		Set<Integer> setBI = new HashSet<Integer>();

		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct ds.customer_id\n"
				+ "   from data_sales ds\n"
				+ "   inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and dsd.product_division=5\n"
				+ "   inner join data_contract dc on dsd.shop_id = dc.shop_id and dsd.slip_no = dc.slip_no and dsd.product_id = dc.product_id\n"
				+ "   where "
				+ "   ds.shop_id =" + mstShop.getShopID() + "\n"
				+ "   and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
				+ "   and ds.delete_date is null"
				+ "   and ((dc.valid_date > '" + sdf.format(baseEndDate) + "'\n"
				+ "   and not exists (select dsdz.slip_no from data_sales_detail dsdz where dsdz.delete_date is null and dsdz.product_division = 8 and dsdz.contract_shop_id = dc.shop_id and dsdz.contract_no=dc.contract_no and dsdz.contract_detail_no=dc.contract_detail_no)\n"
				+ "   and dc.product_num <= COALESCE(\n"
				+ "       (select sum(n.product_num) from data_contract_digestion n \n"
				+ "       inner join data_sales s on n.shop_id = s.shop_id and n.slip_no = s.slip_no and s.delete_date is null \n"
				+ "       where n.contract_no = dc.contract_no\n"
				+ "       and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ "       and ds.shop_id = n.shop_id\n"
				+ "       and n.contract_detail_no = dc.contract_detail_no \n"
				+ "       and n.contract_shop_id = dc.shop_id)\n"
				+ "   ,0)\n"
				+ "   ) OR dc.valid_date < '" + sdf.format(baseEndDate) + "'"
				+ "     OR exists (select dsdz.slip_no from data_sales_detail dsdz where dsdz.delete_date is null and dsdz.product_division = 8 and dsdz.contract_shop_id = dc.shop_id and dsdz.contract_no=dc.contract_no and dsdz.contract_detail_no=dc.contract_detail_no)\n"
				+ "   ) and ds.customer_id in (\n"
				+ "     select distinct nds.customer_id from data_sales_detail ndsd\n"
				+ "     inner join data_sales nds on nds.shop_id= ndsd.shop_id and nds.slip_no=ndsd.slip_no and nds.delete_date is null\n"
				+ "     where ndsd.product_division=2\n"
				+ "     and ndsd.delete_date is null\n"
				+ "     and nds.shop_id = " + mstShop.getShopID() + "\n"
				+ "     and nds.sales_date >= '" + sdf.format(bef12OneDate) + "'\n"
				+ "     and nds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ "     and nds.customer_id = ds.customer_id\n"
				+ "   ) and ds.customer_id not in (select distinct ds.customer_id\n"
				+ "     from\n"
				+ " data_sales ds\n"
				+ " inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and dsd.product_division=5\n"
				+ " inner join data_contract dc on dsd.shop_id = dc.shop_id and dsd.slip_no = dc.slip_no and dsd.product_id = dc.product_id\n"
				+ " where\n"
				+ " dc.valid_date >= '" + sdf.format(baseEndDate) + "'\n"
				+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
				+ " and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
				+ " and not exists (select dsdz.slip_no from data_sales_detail dsdz where dsdz.delete_date is null and dsdz.product_division = 8 and dsdz.contract_shop_id = dc.shop_id and dsdz.contract_no=dc.contract_no and dsdz.contract_detail_no=dc.contract_detail_no)\n"
				+ " and dc.product_num > COALESCE((\n"
				+ "select sum(dcdx.product_num) from \n"
				+ "data_sales dsx\n"
				+ "inner join data_sales_detail dsdx on dsx.shop_id = dsdx.shop_id and dsx.slip_no=dsdx.slip_no and dsdx.delete_date is null and dsdx.product_division=6\n"
				+ "inner join data_contract_digestion dcdx on dsx.shop_id=dcdx.shop_id and dsdx.slip_no = dcdx.slip_no and dsdx.contract_no = dcdx.contract_no and dsdx.contract_detail_no=dcdx.contract_detail_no\n"
				+ "where dsx.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ "    and dcdx.contract_shop_id = dc.shop_id \n"
				+ "    and dcdx.contract_no = dc.contract_no \n"
				+ "    and dcdx.contract_detail_no = dc.contract_detail_no\n"
				+ "group by dcdx.contract_shop_id, dcdx.contract_no, dcdx.contract_detail_no\n"
				+ "),0)\n"
				+ ")\n"
		);
		SystemInfo.getLogger().info("[BI](1):" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		while (rs.next()) {
			setBI.add(rs.getInt("customer_id"));
		}
		//(２）
		// 出力月月末にコース未消化残があり
		// かつ　コース契約履歴が出力月から過去24か月以内にない顧客
		// かつ　消化履歴が出力月から過去25か月以前の顧客
		// かつ　出力月から過去12か月以内に商品購入履歴がある顧客
		sql = new StringBuilder();
		sql.append(" select distinct ds.customer_id\n"
				+ "   from data_sales ds\n"
				+ "   inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and dsd.product_division=5\n"
				+ "   inner join data_contract dc on dsd.shop_id = dc.shop_id and dsd.slip_no = dc.slip_no and dsd.product_id = dc.product_id\n"
				+ "   where dc.valid_date > '" + sdf.format(baseEndDate) + "' and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ "   and ds.delete_date is null and not exists (select dsdz.slip_no from data_sales_detail dsdz where dsdz.delete_date is null and dsdz.product_division = 8 and dsdz.contract_shop_id = dc.shop_id and dsdz.contract_no=dc.contract_no and dsdz.contract_detail_no=dc.contract_detail_no)\n"
				+ "   and ds.shop_id =" + mstShop.getShopID() + "\n"
				+ " and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
				+ "   and dc.product_num > COALESCE(\n"
				+ "       (select sum(n.product_num) from data_contract_digestion n \n"
				+ "       inner join data_sales s on n.shop_id = s.shop_id and n.slip_no = s.slip_no and s.delete_date is null \n"
				+ "       where n.contract_no = dc.contract_no\n"
				+ "       and s.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ "       and ds.shop_id = n.shop_id\n"
				+ "       and n.contract_detail_no = dc.contract_detail_no \n"
				+ "       and n.contract_shop_id = dc.shop_id)\n"
				+ "   ,0)\n"
				+ "   and ds.customer_id not in (\n"
				+ "     select distinct nds.customer_id from data_sales_detail ndsd\n"
				+ "     inner join data_sales nds on nds.shop_id= ndsd.shop_id and nds.slip_no=ndsd.slip_no and nds.delete_date is null\n"
				+ "     where ndsd.product_division=5\n"
				+ "     and ndsd.delete_date is null\n"
				+ "     and nds.shop_id = " + mstShop.getShopID() + "\n"
				+ "     and nds.sales_date >= '" + sdf.format(bef23OneDate) + "'\n"
				+ "     and nds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ "     and nds.customer_id = ds.customer_id\n"
				+ "   )"
				+ "   and ds.customer_id in (\n"
				+ "     select distinct nds.customer_id from data_sales_detail ndsd\n"
				+ "     inner join data_sales nds on nds.shop_id= ndsd.shop_id and nds.slip_no=ndsd.slip_no and nds.delete_date is null\n"
				+ "     where ndsd.product_division=6\n"
				+ "     and ndsd.delete_date is null\n"
				+ "     and nds.shop_id = " + mstShop.getShopID() + "\n"
				+ "     and nds.sales_date < '" + sdf.format(bef23OneDate) + "'\n"
				+ "     and nds.customer_id = ds.customer_id\n"
				+ "   )"
				+ "   and ds.customer_id in (\n"
				+ "     select distinct nds.customer_id from data_sales_detail ndsd\n"
				+ "     inner join data_sales nds on nds.shop_id= ndsd.shop_id and nds.slip_no=ndsd.slip_no and nds.delete_date is null\n"
				+ "     where ndsd.product_division=2\n"
				+ "     and ndsd.delete_date is null\n"
				+ "     and nds.shop_id = " + mstShop.getShopID() + "\n"
				+ "     and nds.sales_date >= '" + sdf.format(bef12OneDate) + "'\n"
				+ "     and nds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ "     and nds.customer_id = ds.customer_id\n"
				+ "   )"
		);
		SystemInfo.getLogger().info("[BI](2):" + sql.toString());
		rs = SystemInfo.getConnection().executeQuery(sql.toString());

		while (rs.next()) {
			setBI.add(rs.getInt("customer_id"));
		}
		setBI.removeAll(setCI);
		return setBI;
	}

	/**
	 * [BJ] 出力月-1の月末時点で、1つもコース契約残数がない人（有効期限切れ、解約を含む） かつ、出力月に商品購入履歴がある人 →　出力月：2018年2月とした場合、2018年1月末時点でコース契約残が無い　かつ　2018年2月に出力店舗で商品購入履歴がある顧客 ※商品返品は、集計対象外とする
	 */
	private static Set<Integer> getBJset(MstShop mstShop, SimpleDateFormat sdf, Date baseOneDate, Date baseEndDate, Date bef12OneDate, Date bef12EndDate, Date bef23OneDate) throws SQLException {

		StringBuilder sql = new StringBuilder();
		sql.append("select customer_id from (\n"
				// --コース契約履歴あり
				+ "select ds.customer_id\n"
				+ "from data_sales ds\n"
				+ " inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and dsd.product_division=5\n"
				+ " inner join data_contract dc on dsd.shop_id = dc.shop_id and dsd.slip_no = dc.slip_no and dsd.product_id = dc.product_id\n"
				+ " where\n"
				+ " ds.shop_id =" + mstShop.getShopID() + "\n"
				+ " and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
				+ " and ds.delete_date is null and ( not exists (select dsdz.slip_no from data_sales_detail dsdz where dsdz.delete_date is null and dsdz.product_division = 8 and dsdz.contract_shop_id = dc.shop_id and dsdz.contract_no=dc.contract_no and dsdz.contract_detail_no=dc.contract_detail_no)\n"
				+ "and ds.sales_date >= '" + sdf.format(bef23OneDate) + "'\n"
				+ "and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				//  --消化残なし
				+ "and dc.product_num <=  COALESCE((\n"
				+ "select sum(dcd.product_num) as use_product_num from data_contract_digestion dcd where\n"
				+ "    dcd.contract_shop_id = dc.shop_id \n"
				+ "    and dcd.contract_no = dc.contract_no \n"
				+ "    and dcd.contract_detail_no = dc.contract_detail_no\n"
				//					+ "	and dcd.insert_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ "	group by dcd.contract_shop_id, dcd.contract_no, dcd.contract_detail_no\n"
				+ "),0)\n"
				+ " OR dc.valid_date < '" + sdf.format(baseEndDate) + "'"
				+ " OR exists (select dsdz.slip_no from data_sales_detail dsdz where dsdz.delete_date is null and dsdz.product_division = 8 and dsdz.contract_shop_id = dc.shop_id and dsdz.contract_no=dc.contract_no and dsdz.contract_detail_no=dc.contract_detail_no)\n"
				// --当月に商品購入履歴がある人
				+ "   ) and exists (\n"
				+ "select distinct nds.customer_id from data_sales_detail ndsd\n"
				+ "inner join data_sales nds on nds.shop_id= ndsd.shop_id and nds.slip_no=ndsd.slip_no and nds.delete_date is null\n"
				+ "where ndsd.product_division=2\n"
				+ " and ndsd.delete_date is null\n"
				+ " and nds.sales_date >= '" + sdf.format(bef12OneDate) + "'\n"
				+ " and nds.sales_date <= '" + sdf.format(bef12EndDate) + "'\n"
				+ " and nds.customer_id = ds.customer_id\n"
				+ ") ) val\n");

		SystemInfo.getLogger().info("[BJ]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		Set<Integer> set = new HashSet<Integer>();
		while (rs.next()) {
			set.add(rs.getInt("customer_id"));
		}
		return set;
	}

	/**
	 * [BK] BIのうち、出力月-1に商品購入履歴があり、かつ、出力月に商品購入履歴がない顧客数 →　対象月：2018年2月とした場合、2018年1月に商品購入履歴があり、かつ　2018年2月に商品購入履歴がない顧客数　※商品分類は問わない。
	 */
	private static Set<Integer> getBKset(MstShop mstShop, SimpleDateFormat sdf, Date baseOneDate, Date baseEndDate, Date bef1OneDate, Date bef1EndDate, Set<Integer> setBI) throws SQLException {

		StringBuilder sql = new StringBuilder();
		//--先月商品購入がある
		sql.append("select distinct ds.customer_id from data_sales_detail dsd\n"
				+ "inner join data_sales ds on ds.shop_id= dsd.shop_id and ds.slip_no=dsd.slip_no and ds.delete_date is null\n"
				+ "where dsd.product_division=2\n"
				+ " and dsd.delete_date is null and ds.delete_date is null\n"
				+ " and ds.shop_id = " + mstShop.getShopID() + "\n"
				+ " and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
				+ " and ds.sales_date >= '" + sdf.format(bef1OneDate) + "'\n"
				+ " and ds.sales_date <= '" + sdf.format(bef1EndDate) + "'\n"
				//  --今月商品購入がない
				+ " and not exists (\n"
				+ "   select distinct nds.customer_id from data_sales_detail ndsd\n"
				+ "   inner join data_sales nds on nds.shop_id= ndsd.shop_id and nds.slip_no=ndsd.slip_no and nds.delete_date is null\n"
				+ "   where ndsd.product_division=2\n"
				+ "    and ndsd.delete_date is null\n"
				+ " and nds.shop_id = " + mstShop.getShopID() + "\n"
				+ "    and nds.sales_date >= '" + sdf.format(baseOneDate) + "'\n"
				+ "    and nds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ "	and nds.customer_id = ds.customer_id\n"
				+ " ) \n"
				+ " and ds.customer_id in ( -1");
		for (Integer dc : setBI) {
			sql.append("," + dc);
		}
		sql.append(") "
		);
		SystemInfo.getLogger().info("[BK]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		Set<Integer> set = new HashSet<Integer>();
		while (rs.next()) {
			set.add(rs.getInt("customer_id"));
		}
		return set;
	}

	/**
	 * [BL] ＢＩのうち、前回の商品購入履歴が、出力月-3以前の顧客数 →　対象年月：2018年2月とした場合、前回の商品購入履歴が、2017年12月以前の顧客　※商品分類は問わない。
	 */
	private static Set<Integer> getBLset(MstShop mstShop, SimpleDateFormat sdf, Date bef2EndDate, Set<Integer> setBI) throws SQLException {

		StringBuilder sql = new StringBuilder();
		//--商品購入がある
		sql.append("select distinct ds.customer_id from data_sales_detail dsd\n"
				+ "inner join data_sales ds on ds.shop_id= dsd.shop_id and ds.slip_no=dsd.slip_no and ds.delete_date is null\n"
				+ "where dsd.product_division=2\n"
				+ " and dsd.delete_date is null\n"
				+ " and ds.shop_id = " + mstShop.getShopID() + "\n"
				+ " and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
				+ " and ds.sales_date <= '" + sdf.format(bef2EndDate) + "'\n"
				+ " --今月商品購入がない\n"
				+ " and not exists (\n"
				+ "   select distinct nds.customer_id from data_sales_detail ndsd\n"
				+ "   inner join data_sales nds on nds.shop_id= ndsd.shop_id and nds.slip_no=ndsd.slip_no and nds.delete_date is null\n"
				+ "   where ndsd.product_division=2\n"
				+ "    and ndsd.delete_date is null\n"
				+ " and nds.shop_id = " + mstShop.getShopID() + "\n"
				+ "    and nds.sales_date > '" + sdf.format(bef2EndDate) + "'\n"
				+ "	and nds.customer_id = ds.customer_id\n"
				+ " ) \n"
				+ " and ds.customer_id in ( -1");
		for (Integer dc : setBI) {
			sql.append("," + dc);
		}
		sql.append(") "
		);
		SystemInfo.getLogger().info("[BL]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		Set<Integer> set = new HashSet<Integer>();
		while (rs.next()) {
			set.add(rs.getInt("customer_id"));
		}
		return set;
	}

	/**
	 * [CI] 出力月から過去24カ月以内に技術分類・フリー会員（technic__class_id=2）の履歴があり、 かつ、未消化残の契約が無い顧客
	 */
	private static Set<Integer> getCIset(MstShop mstShop, SimpleDateFormat sdf, Date baseOneDate, Date baseEndDate, Date bef23OneDate) throws SQLException {

		StringBuilder sql = new StringBuilder();
		sql.append("select distinct customer_id from \n"
				+ " data_sales_detail dsd\n"
				+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null \n"
				+ " inner join mst_technic mt on mt.technic_id = dsd.product_id and mt.technic_class_id=2 \n"
				+ " where product_division=1  \n"
				+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
				+ " and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
				+ " and ds.sales_date >= '" + sdf.format(bef23OneDate) + "'\n"
				+ " and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ " and dsd.delete_date is null\n"
				+ " and customer_id not in (\n"
				+ " select distinct ds.customer_id\n"
				+ " from\n"
				+ " data_sales ds\n"
				+ " inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and dsd.product_division=5\n"
				+ " inner join data_contract dc on dsd.shop_id = dc.shop_id and dsd.slip_no = dc.slip_no and dsd.product_id = dc.product_id\n"
				+ " where\n"
				+ " dc.valid_date >= '" + sdf.format(baseOneDate) + "'\n"
				+ " and not exists (select dsdz.slip_no from data_sales_detail dsdz where dsdz.delete_date is null and dsdz.product_division = 8 and dsdz.contract_shop_id = dc.shop_id and dsdz.contract_no=dc.contract_no and dsdz.contract_detail_no=dc.contract_detail_no)\n"
				+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
				+ " and dc.product_num > COALESCE((\n"
				+ "select sum(dcdx.product_num) from \n"
				+ "data_sales dsx\n"
				+ "inner join data_sales_detail dsdx on dsx.shop_id = dsdx.shop_id and dsx.slip_no=dsdx.slip_no and dsdx.delete_date is null and dsdx.product_division=6\n"
				+ "inner join data_contract_digestion dcdx on dsx.shop_id=dcdx.shop_id and dsdx.slip_no = dcdx.slip_no and dsdx.contract_no = dcdx.contract_no and dsdx.contract_detail_no=dcdx.contract_detail_no\n"
				+ "where dsx.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ "    and dcdx.contract_shop_id = dc.shop_id \n"
				+ "    and dcdx.contract_no = dc.contract_no \n"
				+ "    and dcdx.contract_detail_no = dc.contract_detail_no\n"
				+ "group by dcdx.contract_shop_id, dcdx.contract_no, dcdx.contract_detail_no\n"
				+ "),0)"
				+ ")");
		SystemInfo.getLogger().info("[CI]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		Set<Integer> set = new HashSet<Integer>();
		while (rs.next()) {
			set.add(rs.getInt("customer_id"));
		}
		return set;
	}

	/**
	 * [CK] 「ＣＩ」のうち、出力月に、技術分類・フリー会員（technic_class_id=2）の履歴がある顧客数
	 */
	private static Set<Integer> getCKset(MstShop mstShop, SimpleDateFormat sdf, Date baseOneDate, Date baseEndDate, Set<Integer> setCI) throws SQLException {

		StringBuilder sql = new StringBuilder(4000);
		sql.append("select  distinct customer_id from \n"
				+ " data_sales_detail dsd\n"
				+ " inner join data_sales ds on dsd.shop_id = ds.shop_id and dsd.slip_no = ds.slip_no and ds.delete_date is null\n"
				+ " inner join mst_technic mt on mt.technic_id = dsd.product_id and mt.technic_class_id=2 \n"
				+ " where product_division=1  \n"
				+ " and ds.shop_id =" + mstShop.getShopID() + "\n"
				+ " and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
				+ " and ds.sales_date >= '" + sdf.format(baseOneDate) + "'\n"
				+ " and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ " and dsd.delete_date is null\n"
				+ " and customer_id in (-1");
		for (Integer ag : setCI) {
			sql.append("," + ag);
		}
		sql.append(")");

		SystemInfo.getLogger().info("[CK]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		Set<Integer> set = new HashSet<Integer>();
		while (rs.next()) {
			set.add(rs.getInt("customer_id"));
		}
		return set;
	}

	/**
	 * [CL]	「ＣＩ」の顧客から、出力月に来店履歴がなくかつ、出力月＋1以降の未来予約がない顧客
	 */
	private static Set<Integer> getCLset(MstShop mstShop, SimpleDateFormat sdf, Date baseOneDate, Date baseEndDate, Date nextFDate, Set<Integer> setCI) throws SQLException {

		StringBuilder sql = new StringBuilder(4000);
		sql.append("select distinct ds.customer_id from data_sales ds\n"
				+ "where ds.shop_id=" + mstShop.getShopID() + "\n"
				+ " and ds.delete_date is null\n"
				+ " and ds.customer_id not in (select dsn.customer_id from data_sales dsn where dsn.shop_id=" + mstShop.getShopID() + " and dsn.sales_date between '" + sdf.format(baseOneDate) + "' and '" + sdf.format(baseEndDate) + "')\n"
				+ " and not exists (\n"
				+ " select * from data_reservation dr\n"
				+ " inner join data_reservation_detail drd on dr.shop_id = drd.shop_id and dr.reservation_no = drd.reservation_no and drd.delete_date is null\n"
				+ " where dr.shop_id = ds.shop_id and dr.customer_id = ds.customer_id and drd.reservation_datetime  >= '" + sdf.format(nextFDate) + "' \n"
				+ " )"
				+ " and customer_id in (-1");
		for (Integer ag : setCI) {
			sql.append("," + ag);
		}
		sql.append(")");

		SystemInfo.getLogger().info("[CL]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		Set<Integer> set = new HashSet<Integer>();
		while (rs.next()) {
			set.add(rs.getInt("customer_id"));
		}
		return set;
	}

	/**
	 * [CM]	「ＣＩ」の顧客で、出力月から過去12か月に出力店舗で商品購入履歴がある顧客
	 */
	private static Set<Integer> getCMset(MstShop mstShop, SimpleDateFormat sdf, Date bef12OneDate, Date baseEndDate, Set<Integer> setCI) throws SQLException {

		StringBuilder sql = new StringBuilder();
		sql.append("select distinct ds.customer_id\n"
				+ "from data_sales ds\n"
				+ " inner join data_sales_detail dsd on ds.shop_id = dsd.shop_id and ds.slip_no = dsd.slip_no and dsd.product_division=2\n"
				+ " where\n"
				+ " ds.shop_id =" + mstShop.getShopID() + " and ds.delete_date is null\n"
				+ " and ds.sales_date >= '" + sdf.format(bef12OneDate) + "'\n"
				+ " and ds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ " and customer_id in (-1");
		for (Integer ag : setCI) {
			sql.append("," + ag);
		}
		sql.append(")");

		SystemInfo.getLogger().info("[CM]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		Set<Integer> set = new HashSet<Integer>();
		while (rs.next()) {
			set.add(rs.getInt("customer_id"));
		}
		return set;
	}

	/**
	 * [CO] CMのうち、出力月-1に商品購入履歴があり、かつ、出力月に商品購入履歴がない顧客数
	 */
	private static Set<Integer> getCOset(MstShop mstShop, SimpleDateFormat sdf, Date baseOneDate, Date baseEndDate, Date bef1EndDate, Date bef1OneDate, Set<Integer> setCM) throws SQLException {

		StringBuilder sql = new StringBuilder();
		//--先月商品購入がある
		sql.append("select distinct ds.customer_id from data_sales_detail dsd\n"
				+ "inner join data_sales ds on ds.shop_id= dsd.shop_id and ds.slip_no=dsd.slip_no and ds.delete_date is null\n"
				+ "where dsd.product_division=2\n"
				+ " and dsd.delete_date is null\n"
				+ " and ds.customer_id not in (select mcx.customer_id from mst_customer mcx where mcx.customer_no = '0')\n"
				+ " and ds.shop_id = " + mstShop.getShopID() + "\n"
				+ " and ds.sales_date <= '" + sdf.format(bef1EndDate) + "'\n"
				+ " and ds.sales_date >= '" + sdf.format(bef1OneDate) + "'\n"
				//  --今月商品購入がない
				+ " and not exists (\n"
				+ "   select distinct nds.customer_id from data_sales_detail ndsd\n"
				+ "   inner join data_sales nds on nds.shop_id= ndsd.shop_id and nds.slip_no=ndsd.slip_no and nds.delete_date is null\n"
				+ "   where ndsd.product_division=2\n"
				+ "    and ndsd.delete_date is null\n"
				+ " and nds.shop_id = " + mstShop.getShopID() + "\n"
				+ "    and nds.sales_date >= '" + sdf.format(baseOneDate) + "'\n"
				+ "    and nds.sales_date <= '" + sdf.format(baseEndDate) + "'\n"
				+ "	and nds.customer_id = ds.customer_id\n"
				+ " ) \n"
				+ " and ds.customer_id in ( -1");
		for (Integer dc : setCM) {
			sql.append("," + dc);
		}
		sql.append(") "
		);
		SystemInfo.getLogger().info("[CO]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		Set<Integer> set = new HashSet<Integer>();
		while (rs.next()) {
			set.add(rs.getInt("customer_id"));
		}
		return set;
	}

	/**
	 * [CP] CMのうち、前回の商品購入履歴が、出力月-3以前の顧客数 →　対象年月：2018年2月とした場合、前回の商品購入履歴が、2017年12月以前の顧客　※商品分類は問わない。
	 */
	private static Set<Integer> getCPset(MstShop mstShop, SimpleDateFormat sdf, Date bef2EndDate, Set<Integer> setCM) throws SQLException {

		StringBuilder sql = new StringBuilder();
		//--商品購入がある
		sql.append("select distinct ds.customer_id from data_sales_detail dsd\n"
				+ "inner join data_sales ds on ds.shop_id= dsd.shop_id and ds.slip_no=dsd.slip_no and ds.delete_date is null\n"
				+ "where dsd.product_division=2\n"
				+ " and dsd.delete_date is null\n"
				+ " and ds.shop_id = " + mstShop.getShopID() + "\n"
				+ " and ds.sales_date <= '" + sdf.format(bef2EndDate) + "'\n"
				+ " --今月商品購入がない\n"
				+ " and not exists (\n"
				+ "   select distinct nds.customer_id from data_sales_detail ndsd\n"
				+ "   inner join data_sales nds on nds.shop_id= ndsd.shop_id and nds.slip_no=ndsd.slip_no and nds.delete_date is null\n"
				+ "   where ndsd.product_division=2\n"
				+ "    and ndsd.delete_date is null\n"
				+ " and nds.shop_id = " + mstShop.getShopID() + "\n"
				+ "    and nds.sales_date > '" + sdf.format(bef2EndDate) + "'\n"
				+ "	and nds.customer_id = ds.customer_id\n"
				+ " ) \n"
				+ " and ds.customer_id in ( -1");
		for (Integer dc : setCM) {
			sql.append("," + dc);
		}
		sql.append(") "
		);

		SystemInfo.getLogger().info("[CP]" + sql.toString());
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
		Set<Integer> set = new HashSet<Integer>();
		while (rs.next()) {
			set.add(rs.getInt("customer_id"));
		}
		return set;
	}

}
