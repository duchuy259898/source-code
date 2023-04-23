package com.geobeck.sosia.pos.hair.report;

import java.util.ArrayList;
import java.util.logging.Level;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ResultSetWrapper;

/**
 * 商品ランキング一覧。売れ筋ランキング機能中に該当クラスを使用。
 * @author aims_katsu
 */
public class SimpleProductRankingList extends ArrayList<ProductRanking> {
	/**
	 * 店舗IDリスト
	 */
	private String shopIDList = "";
	/**
	 * 対象期間(開始日)
	 */
	private String startDateStr = "";
	/**
	 * 対象期間(終了日)
	 */
	private String endDateStr = "";
	
	/**
	 * 表示順
	 */
	private String orderKey = "product_num";

	/**
	 * Creates a new instance of ProductRankingList
	 */
	public SimpleProductRankingList() {
	}

	/**
	 * 店舗IDリストを取得する。
	 * @return 店舗IDリスト
	 */
	public String getShopIDList() {
		return shopIDList;
	}

	/**
	 * 店舗IDリストをセットする。
	 * @param shopIDList 店舗IDリスト
	 */
	public void setShopIDList(String shopIDList) {
		this.shopIDList = shopIDList;
	}

	/**
	 * 対象期間(開始日)を取得する。
	 * @return 対象期間(開始日)
	 */
	public String getStartDateStr() {
		return startDateStr;
	}

	/**
	 * 対象期間(開始日)をセットする。
	 * @param termFrom 対象期間(開始日)
	 */
	public void setStartDateStr(String s) {
		this.startDateStr = s;
	}

	/**
	 * 対象期間(終了日)を取得する。
	 * @return 対象期間(終了日)
	 */
	public String getEndDateStr() {
		return endDateStr;
	}

	/**
	 * 対象期間(終了日)をセットする。
	 * @param termTo 対象期間(終了日)
	 */
	public void setEndDateStr(String s) {
		this.endDateStr = s;
	}
	
	public String getOrderKey() {
		return orderKey;
	}

	public void setOrderKey(String orderKey) {
		this.orderKey = orderKey;
	}

	/**
	 * データを読み込む。
	 */
	public void load() {
		try {
			//ランキングリスト取得
			this.getProductRankingList();
		} catch (Exception e) {
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	/**
	 * スタッフランキングのリストを取得する。
	 * @exception Exception
	 */
	private void getProductRankingList() throws Exception {
		this.clear();

		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(this.getProductRankingSQL());

		while (rs.next()) {
			ProductRanking temp = new ProductRanking();
            temp.setProdName(rs.getString("product_name"));
            temp.setProdCount(rs.getLong("product_num"));
            temp.setProdSales(rs.getLong("sales_value"));
            temp.setProdDiscount(rs.getLong("discount_value"));
			this.add(temp);
		}

		rs.close();
	}

	/**
	 * スタッフランキング抽出用SQLを取得する。
	 * @return スタッフランキング抽出用SQL
	 * @exception Exception
	 */
	private String getProductRankingSQL() throws Exception {
		StringBuilder sql = new StringBuilder(1000);
		sql.append(" select");
		sql.append("      a.product_id");
		sql.append("     ,max(b.product_name)    as product_name");
		sql.append("     ,sum(a.product_num)     as product_num");

		// 割引後税込み金額
		sql.append(" ,sum(a.discount_detail_value_in_tax) as sales_value");
		// 税込み割引金額
		sql.append(" ,sum(a.detail_value_in_tax - a.discount_detail_value_in_tax) as discount_value");

		sql.append(" from");
		sql.append("     view_data_sales_detail_valid a");
		sql.append("         join");
		sql.append("             (");

		//--------------------
		// 商品
		//--------------------
		sql.append(" select");
		sql.append("      a.item_id          as product_id");
		sql.append("     ,a.item_name        as product_name");
		sql.append("     ,b.item_class_name  as class_name");

		// 税込
		sql.append(" ,a.price        as unit_price");

		sql.append("     ,2              as product_division");
		sql.append(" from");
		sql.append("     mst_item a");
		sql.append("         join mst_item_class b");
		sql.append("             using(item_class_id)");
		sql.append(" where true");

		//Thanh end add 2013/03/18   
		sql.append("             ) b");
		sql.append("             using (product_id, product_division)");
		sql.append(" where");
		sql.append("         a.shop_id in (" + getShopIDList() + ")");
		sql.append("     and a.sales_date between '" + getStartDateStr() + "' and '" + getEndDateStr() + "'");
		sql.append(" group by");
		sql.append("     a.product_id");

		sql.append(" order by");

		//表示順
		sql.append(" " + this.getOrderKey() + " desc");

		// limit
		sql.append(" limit 10");

		System.out.println("SimpleProductRankingList##getProductRankingSQL:" + sql);
		return sql.toString();
	}
}
