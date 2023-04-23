/*
 * ResponseReport.java
 *
 * Created on 2007/09/05, 11:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report;

import java.util.*;
import java.text.*;
import java.util.logging.*;
import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.hair.master.company.*;
import com.geobeck.sosia.pos.hair.report.util.*;

/**
 *
 * @author kanemoto
 */
public class ResponseReport extends ArrayList<DataResponseReport> {
	
	private         String                  shopIDList      =       null;
	private         String                  targetName      =       null;
	protected	String			responseType	=	null;				/** レスポンスタイプ */
	protected	ArrayList<MstResponse>	mrs		=	new ArrayList<MstResponse>();	/** レスポンス */
	protected	Integer			rankingType	=	null;				/** ランキングタイプ */
	protected	GregorianCalendar	distStartDate	=	null;				/** 発行開始日 */
	protected	GregorianCalendar	distFinishDate	=	null;				/** 発行終了日 */
	protected	GregorianCalendar	recStartDate	=	null;				/** 集計開始日 */
	protected	GregorianCalendar	recFinishDate	=	null;				/** 集計終了日 */
	protected	Integer			sheetType	=	null;				/** シートタイプ */
	protected	Integer			rankViewType	=	null;				/** ランキング表示タイプ */

	protected	GregorianCalendar	targetStartDate	=	null;				/** 対象期間 開始日 */
	protected	GregorianCalendar	targetEndDate	=	null;				/** 対象期間 終了日 */
	protected	Integer			fixedCount	=	null;				/** 固定回数 */

	/** Creates a new instance of ResponseReport */
	public ResponseReport() {
	}
	
//	/**
//	 * 店舗を登録する
//	 */
//	public void setShop( MstShop shop )
//	{
//		this.shop = shop;
//	}

	public String getShopIDList()
	{
		return shopIDList;
	}

	public void setShopIDList(String shopIDList)
	{
		this.shopIDList = shopIDList;
	}

	public String getTargetName()
	{
		return targetName;
	}

	public void setTargetName(String targetName)
	{
		this.targetName = targetName;
	}
	
	/**
	 * レスポンスリスト文字列を取得する
	 */
	public String getResponses()
	{
		String   retStr = "";
		boolean addFlg = false;
		
		for( MstResponse mr : mrs )
		{
		    retStr += ( addFlg ? ", " : "" ) + SQLUtil.convertForSQL( mr.getResponseID() );
		    addFlg = true;
		}
		return retStr;
	}
	
	/**
	 * レスポンスリストを登録する
	 */
	public void setResponses( ArrayList<MstResponse> mrs )
	{
		this.mrs = mrs;
	}
	
	/**
	 * ランキングタイプ文字列を取得する
	 */
	public String getRankingTypeStr()
	{
		if( rankingType == 0 ) return "dist_num";
		if( rankingType == 1 ) return "rec_num";
		return "recRate";
	}

	/**
	 * 対象期間 開始日を登録する
	 */
	public void setTargetStartDate( GregorianCalendar targetStartDate )
	{
		this.targetStartDate = targetStartDate;
	}
	/**
	/**
	 * 対象期間 終了日を登録する
	 */
	public void setTargetEndDate( GregorianCalendar targetEndDate )
	{
		this.targetEndDate = targetEndDate;
	}
	/**
	 * 固定回数を登録する
	 */
	public void setFixedCount( Integer fixedCount )
	{
		this.fixedCount = fixedCount;
	}
	
	public void setReportData()
	{
		this.clear();
		
		try
		{
			ConnectionWrapper con = SystemInfo.getConnection();
			ResultSetWrapper rs = con.executeQuery( getResponseSQL() );
			
			while(rs.next()) {
			    DataResponseReport drr = new DataResponseReport();
			    drr.setData( rs );
			    this.add( drr );
			}
			rs.close();
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	private String getRankingValue()
	{
		switch( sheetType )
		{
		case 0:
			switch( rankingType )
			{
			case 0: return  "issue.circulation_number";
			case 1: return  "count( dre.* )";
			default: return "( Cast( count( dre.* ) AS Decimal( 10, 1 ) ) / issue.circulation_number ) * 10000";
			}
		case 1:
			switch( rankingType )
			{
			case 0: return  "issue.cnt";
			case 1: return  "count( dre.* )";
			default: return "( Cast( count( dre.* ) AS Decimal( 10, 1 ) ) / issue.cnt ) * 10000";
			}
		default:
			switch( rankingType )
			{
			case 0: return  "sum( issue.circulation_number )";
			case 1: return  "count( dre.* )";
			default: return "( Cast( count( dre.* ) AS Decimal( 3, 1 ) ) / sum( issue.circulation_number ) ) * 10000";
			}
		}
	}

	private String getLoadResponseSheetSQL()
	{
		if( sheetType == 0 ) return this.getLoadResponseIssueSQL();
		if( sheetType == 1 ) return this.getLoadResponseResponseSQL();
		return this.getLoadResponseStaffSQL();
	}
	
	private String getLoadResponseIssueSQL()
	{
		return
			"select\n" +
			"issue.res_id as response_id,\n" +
			"issue.response_name as response_name,\n" +
			"coalesce( issue.staff_name1 || ' ' || issue.staff_name2, TO_CHAR( issue.circulation_monthly_date, 'YYYY年MM月号' ) ) as response_object_name,\n" +
			"issue.regist_date as regist_date,\n" +
			"issue.circulation_number as dist_num,\n" +
			"count( dre.* ) as rec_num,\n" +
			"( Cast( count( dre.* ) AS Decimal( 3, 1 ) ) / issue.circulation_number ) as recRate,\n" +
			"Cast( ( " + getRankingValue() + " ) AS Decimal( 10, 1 ) ) as ranking_value\n" +
			"from\n" +
			"(\n" +
			"select\n" +
			"dri.response_id as res_id,\n" +
			"*\n" +
			"from\n" +
			"data_response_issue as dri\n" +
			"left join mst_response as mr on\n" +
			"dri.response_id = mr.response_id\n" +
			"left join mst_staff as ms on\n" +
			"dri.staff_id = ms.staff_id\n" +
			"where\n" +
			"dri.delete_date is null\n" +
			"and dri.shop_id in (" + shopIDList + ") \n" +	// 店舗ID
			"and dri.regist_date between " + SQLUtil.convertForSQL( distStartDate ) + " and " + SQLUtil.convertForSQL( distFinishDate ) + "\n"	+	// 発行期間
			"and dri.response_id IN ( " + getResponses() + " )\n" +	// 表示レスポンス
			") as issue\n" +
			"left join data_response_effect as dre on\n" +
			"dre.delete_date is null\n" +
			"and issue.response_issue_id = dre.response_issue_id\n" +
			"and dre.data_response_date between " + SQLUtil.convertForSQL( recStartDate ) + " and " + SQLUtil.convertForSQL( recFinishDate ) + "\n"	+	// 回収期間
			"group by\n" +
			"issue.res_id, dre.response_issue_id, issue.response_name, issue.staff_name1, issue.staff_name2, issue.circulation_monthly_date, issue.regist_date, issue.circulation_number\n" +
			"order by\n" +
			( ( rankViewType == 0 ) ? "" : "response_id, " ) + getRankingTypeStr() + " desc,regist_date\n" +
			";\n";
	}
	
	private String getLoadResponseResponseSQL()
	{
		return
			"select\n" +
			"issue.response_id as response_id,\n" +
			"issue.response_name as response_name,\n" +
			"'' as response_object_name,\n" +
			"'' as regist_date,\n" +
			"issue.cnt as dist_num,\n" +
			"count( dre.* ) as rec_num,\n" +
			"( Cast( count( dre.* ) AS Decimal( 3, 1 ) ) / issue.cnt ) as recRate,\n" +
			"Cast( ( " + getRankingValue() + " ) AS Decimal( 10, 1 ) ) as ranking_value\n" +
			"from\n" +
			"(\n" +
			"select\n" +
			"mr.response_id,\n" +
			"mr.response_name,\n" +
			"mr.circulation_type,\n" +
			"sum( dri.circulation_number ) as cnt\n" +
			"from\n" +
			"data_response_issue as dri\n" +
			"left join mst_response as mr on\n" +
			"dri.response_id = mr.response_id\n" +
			"where\n" +
			"dri.delete_date is null\n" +
			"and dri.shop_id in (" + shopIDList + ") \n" +	// 店舗ID
			"and dri.regist_date between " + SQLUtil.convertForSQL( distStartDate ) + " and " + SQLUtil.convertForSQL( distFinishDate ) + "\n"	+	// 発行期間
			"and dri.response_id IN ( " + getResponses() + " )\n" +		// 表示レスポンス
			"group by\n" +
			"mr.response_id, mr.response_name,mr.circulation_type\n" +
			") as issue\n" +
			"left join data_response_effect as dre on\n" +
			"dre.delete_date is null\n" +
			"and issue.response_id = dre.response_id\n" +
			"and dre.data_response_date between " + SQLUtil.convertForSQL( recStartDate ) + " and " + SQLUtil.convertForSQL( recFinishDate ) + "\n"	+	// 回収期間
			"group by\n" +
			"issue.response_id, issue.response_name, issue.cnt\n" +
			"order by\n" +
			getRankingTypeStr() + " desc,regist_date\n" +
			";\n";
	}
	
	private String getLoadResponseStaffSQL()
	{
		return
			"select\n" +
			"issue.res_id as response_id,\n" +
			"issue.response_name as response_name,\n" +
			"coalesce( issue.staff_name1 || ' ' || issue.staff_name2, TO_CHAR( issue.circulation_monthly_date, 'YYYY年MM月号' ) ) as response_object_name,\n" +
			"max( issue.regist_date ) as regist_date,\n" +
			"sum( issue.circulation_number ) as dist_num,\n" +
			"count( dre.* ) as rec_num,\n" +
			"( Cast( count( dre.* ) AS Decimal( 3, 1 ) ) / sum( issue.circulation_number ) ) as recRate,\n" +
			"Cast( ( " + getRankingValue() + " ) AS Decimal( 10, 1 ) ) as ranking_value\n" +
			"from\n" +
			"(\n" +
			"select\n" +
			"dri.response_id as res_id,\n" +
			"dri.staff_id as stf_id,\n" +
			"*\n" +
			"from\n" +
			"data_response_issue as dri\n" +
			"left join mst_response as mr on\n" +
			"dri.response_id = mr.response_id\n" +
			"left join mst_staff as ms on\n" +
			"dri.staff_id = ms.staff_id\n" +
			"where\n" +
			"dri.delete_date is null\n" +
			"and dri.shop_id in (" + shopIDList + ") \n" +	// 店舗ID
			"and dri.regist_date between " + SQLUtil.convertForSQL( distStartDate ) + " and " + SQLUtil.convertForSQL( distFinishDate ) + "\n"	+	// 発行期間
			"and dri.response_id IN ( " + getResponses() + " )\n" +		// 表示レスポンス
			") as issue\n" +
			"left join data_response_effect as dre on\n" +
			"dre.delete_date is null\n" +
			"and issue.response_issue_id = dre.response_issue_id\n" +
			"and dre.data_response_date between " + SQLUtil.convertForSQL( recStartDate ) + " and " + SQLUtil.convertForSQL( recFinishDate ) + "\n"	+	// 回収期間
			"group by\n" +
			"issue.stf_id, issue.res_id, issue.response_name, issue.circulation_monthly_date, issue.staff_name1, issue.staff_name2\n" +
			"order by\n" +
			( ( rankViewType == 0 ) ? "" : "response_id, " ) + getRankingTypeStr() + " desc,regist_date\n" +
			";\n";
	}
	
	
	private String getResponseSQL()
	{
	    StringBuilder sql = new StringBuilder(1000);
	    sql.append(" select");
	    sql.append("      dr.response_id");
	    sql.append("     ,mr.response_name");
	    sql.append("     ,mr.display_seq");
	    // 件数
	    sql.append("     ,count(*) as total_count");
	    // 新規数
	    sql.append("     ,coalesce(sum(case when ds.visit_num = 1 then 1 else 0 end), 0) as new_count");
	    // 新規金額
	    sql.append("     ,coalesce(sum(case when visit_num = 1 then ds.sales_value else 0 end), 0) as new_amount");
	    // 準固定数
	    sql.append("     ,coalesce(sum(case when visit_num between 2 and " + (fixedCount - 1) + " then 1 else 0 end), 0) as sub_fixed_count");
	    // 準固定金額
	    sql.append("     ,coalesce(sum(case when visit_num between 2 and " + (fixedCount - 1) + " then ds.sales_value else 0 end), 0) as sub_fixed_amount");
	    // 固定数
	    sql.append("     ,coalesce(sum(case when visit_num >= " + fixedCount + " then 1 else 0 end), 0) as fixed_count");
	    // 固定金額
	    sql.append("     ,coalesce(sum(case when visit_num >= " + fixedCount + " then ds.sales_value else 0 end), 0) as fixed_amount");
	    // 男性数
	    sql.append("     ,coalesce(sum(case when mc.sex = 1 then 1 else 0 end), 0) as male_count");
	    // 男性金額
	    sql.append("     ,coalesce(sum(case when mc.sex = 1 then ds.sales_value else 0 end), 0) as male_amount");
	    // 女性数
	    sql.append("     ,coalesce(sum(case when mc.sex = 2 then 1 else 0 end), 0) as female_count");
	    // 女性金額
	    sql.append("     ,coalesce(sum(case when mc.sex = 2 then ds.sales_value else 0 end), 0) as female_amount");
	    // 不明数
	    sql.append("     ,coalesce(sum(case when mc.sex is null then 1 else 0 end), 0) as unknown_count");
	    // 不明金額
	    sql.append("     ,coalesce(sum(case when mc.sex is null then ds.sales_value else 0 end), 0) as unknown_amount");
	    // 売上合計
	    sql.append("     ,coalesce(sum(ds.sales_value), 0) as total_amount");
	    sql.append(" from");
	    sql.append("     (");
	    sql.append("         select");
	    sql.append("              ds.shop_id");
	    sql.append("             ,ds.slip_no");
	    sql.append("             ,ds.sales_date");
	    sql.append("             ,ds.customer_id");
	    sql.append("             ,discount_sales_value_in_tax as sales_value");
	    sql.append("             ,(");
	    sql.append("                 SELECT");
	    sql.append("                     count(slip_no) + coalesce(max(before_visit_num),0)");
	    sql.append("                 FROM");
	    sql.append("                     data_sales");
	    sql.append("                         inner join mst_customer using(customer_id)");
	    sql.append("                 WHERE");
	    sql.append("                         data_sales.delete_date is null");
	    sql.append("                     AND mst_customer.delete_date is null");
	    sql.append("                     AND customer_id = ds.customer_id");
	    sql.append("                     AND data_sales.shop_id in (" + shopIDList + ")");
	    sql.append("                     AND");
	    sql.append("                         (");
	    sql.append("                              data_sales.sales_date < ds.sales_date");
	    sql.append("                           or (data_sales.sales_date = ds.sales_date and data_sales.insert_date <= ds2.insert_date)");
	    sql.append("                         )");
	    sql.append("              ) as visit_num");
	    sql.append("         from");
	    sql.append("             view_data_sales_valid ds");
	    sql.append("                 join data_sales ds2");
	    sql.append("                     using(shop_id, slip_no)");
	    sql.append("     ) ds");
	    sql.append("         inner join data_response_effect dr");
	    sql.append("                 on ds.shop_id = dr.shop_id");
	    sql.append("                and ds.slip_no = dr.slip_no");
	    sql.append("                and dr.delete_date is null");
	    sql.append("         inner join mst_response mr");
	    sql.append("                 on dr.response_id = mr.response_id");
	    sql.append("         inner join mst_customer mc");
	    sql.append("                 on ds.customer_id = mc.customer_id");
	    sql.append(" where");
	    sql.append("         ds.shop_id in (" + shopIDList + ")");
	    
	    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
	    sql.append("     and ds.sales_date between '" + format.format(targetStartDate.getTime()) + " 00:00:00' and '" + format.format(targetEndDate.getTime()) + " 23:59:59'");
	    
	    sql.append(" group by");
	    sql.append("      dr.response_id");
	    sql.append("     ,mr.response_name");
	    sql.append("     ,mr.display_seq");
	    sql.append(" order by");
	    sql.append("     mr.display_seq");

	    return sql.toString();
	}
	
	public void print()
	{
	    JExcelApi jx = new JExcelApi("反響分析");
	    jx.setTemplateFile("/reports/ResponseAnalysis.xls");
	    
	    // ヘッダ
	    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
            //IVS_TMTrong start edit 2015/10/15 New request #43502
	    /*jx.setValue(1, 3, "集計期間：" + format.format(targetStartDate.getTime()) + "～" + format.format(targetEndDate.getTime()));
	    jx.setValue(6, 6, "2-" + (fixedCount - 1) + "回");

	    int row = 7;*/
            jx.setValue(1, 3, "店舗名："+targetName);
            jx.setValue(1, 4, "集計期間：" + format.format(targetStartDate.getTime()) + "～" + format.format(targetEndDate.getTime()));
	    jx.setValue(6, 7, "2-" + (fixedCount - 1) + "回");

	    int row = 8;
	    //IVS_TMTrong start edit 2015/10/15 New request #43502
            
	    // 追加行数セット
	    jx.insertRow(row, this.size() - 1);
	    
	    // データセット
	    for (DataResponseReport dr : this) {

		jx.setValue(2, row, dr.getResponseName());
		jx.setValue(3, row, dr.getTotalCount());
		jx.setValue(4, row, dr.getNewCount());
		jx.setValue(5, row, dr.getNewAmount());
		jx.setValue(6, row, dr.getSubFixedCount());
		jx.setValue(7, row, dr.getSubFixedAmount());
		jx.setValue(8, row, dr.getFixedCount());
		jx.setValue(9, row, dr.getFixedAmount());
		jx.setValue(10, row, dr.getMaleCount());
		jx.setValue(11, row, dr.getMaleAmount());
		jx.setValue(12, row, dr.getFemaleCount());
		jx.setValue(13, row, dr.getFemaleAmount());
		jx.setValue(14, row, dr.getUnknownCount());
		jx.setValue(15, row, dr.getUnknownAmount());
		jx.setValue(16, row, dr.getTotalAmount());
		
		row++;
	    }

	    jx.openWorkbook();
	}
}
