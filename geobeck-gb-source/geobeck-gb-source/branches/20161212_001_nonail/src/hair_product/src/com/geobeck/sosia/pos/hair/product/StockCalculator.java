/*
 * StockCalculator.java
 *
 * Created on 2008/09/30, 10:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.product;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * �݌Ɍv�Z�p�̃I�u�W�F�N�g
 * @author mizukawa
 */
public class StockCalculator
{
	/** Creates a new instance of StockCalculator */
	protected StockCalculator(int shopId, Date date)
	{
		this.shopId = shopId;
		this.date = date;
	}

	protected StockCalculator(int shopId, Date date, List<Integer> itemIdList)
	{
		this.shopId = shopId;
		this.date = date;
                this.itemIdList = itemIdList;
	}
        
	private int shopId;
	private Date date;
        private List<Integer> itemIdList;
	private Map<String, Value> stock = new HashMap<String, Value>();

	/**
	 * �K���݌ɂ̒l���v�Z�������ʂ��i�[����StockCalculator��Ԃ��B
	 * @param con �f�[�^�x�[�X�R�l�N�V����
	 * @param shopId �V���b�vID
	 * @param date �K���݌Ɍv�Z��
	 * @return �K���݌ɐ����i�[����StockCalculator
	 */
	public static StockCalculator calcProperStock(ConnectionWrapper con, int shopId, Date date) throws SQLException
	{
		StockCalculator stockCalculator = new StockCalculator(shopId, date);
		stockCalculator.loadProperStock(con);
		return stockCalculator;
	}

	/**
	 * �݌ɐ����v�Z���Ԃ��B
	 * @param con �f�[�^�x�[�X�R�l�N�V����
	 * @param shopId �V���b�vID
	 * @param date ���
	 * @return �X�܂ɂ��鏤�i�̍݌ɐ���Map�ŕԂ��B�L�[�́u���iID/�X�̋Ɩ��敪�v
	 */
	public static StockCalculator calcStock(ConnectionWrapper con, int shopId, Date date) throws SQLException
	{
		StockCalculator stockCalculator = new StockCalculator(shopId, date);
		stockCalculator.loadStock(con);
		return stockCalculator;
	}
	public static StockCalculator calcStock(ConnectionWrapper con, int shopId, Date date, List<Integer> itemIdList) throws SQLException
	{
		StockCalculator stockCalculator = new StockCalculator(shopId, date, itemIdList);
		stockCalculator.loadStock(con);
		return stockCalculator;
	}

	/**
	 * �݌ɐ���Ԃ��i�N�G�����s�j
	 * @param itemId ���iID
	 * @param itemUseDivision �X�̋Ɩ��敪
	 * @return �݌ɐ�
	 */
	public int getStockFromDB(ConnectionWrapper con, int itemId, int itemUseDivision) throws SQLException
	{
                int result = 0;
                
                ResultSetWrapper rs = con.executeQuery(getStockSQL(itemId, itemUseDivision));
		try {
                    if (rs.next()) {
                        result = rs.getInt("stock");
                    }
		} finally {
                    rs.close();
		}

                return result;
	}

	/**
	 * �݌ɐ���Ԃ�
	 * @param itemId ���iID
	 * @param itemUseDivision �X�̋Ɩ��敪
	 * @return �݌ɐ�
	 */
	public int getStock(int itemId, int itemUseDivision)
	{
		String key = itemId + "/" + itemUseDivision;
		if (!stock.containsKey(key))
		{
			return 0;
		}

		return stock.get(key).getStock();
	}

	public int getShopId()
	{
		return this.shopId;
	}

	public Date getDate()
	{
		return this.date;
	}

	/**
	 * �K���݌ɂ̒l���v�Z�������ʂ�ǂݍ���
	 * @param con �f�[�^�x�[�X�R�l�N�V����
	 * @param shopId �V���b�vID
	 * @param date �K���݌Ɍv�Z��
	 */
	protected void loadProperStock(ConnectionWrapper con) throws SQLException
	{
		Map<String, StockCalculator.Value> map = new HashMap<String, StockCalculator.Value>();
		ResultSetWrapper rs = con.executeQuery(getProperStockSQL(shopId, date));
		try
		{
			while (rs.next())
			{
				Value vs = new Value();
				vs.setShopId(rs.getInt("shop_id"));
				vs.setItemId(rs.getInt("item_id"));
				vs.setItemUseDivision(1);
				vs.setStock(rs.getInt("sell_proper_stock"));
				map.put(vs.getItemId() + "/" + vs.getItemUseDivision(), vs);

				Value vu = new Value();
				vu.setShopId(rs.getInt("shop_id"));
				vu.setItemId(rs.getInt("item_id"));
				vu.setItemUseDivision(2);
				vu.setStock(rs.getInt("use_proper_stock"));
				map.put(vu.getItemId() + "/" + vu.getItemUseDivision(), vu);
			}
		}
		finally
		{
			rs.close();
		}

		this.stock = map;
	}

	/**
	 * �݌ɐ����v�Z���Ԃ��B
	 * @param con �f�[�^�x�[�X�R�l�N�V����
	 * @param shopId �V���b�vID
	 * @param date ���
	 */
	public void loadStock(ConnectionWrapper con) throws SQLException
	{
		Map<String, StockCalculator.Value> map = new HashMap<String, StockCalculator.Value>();

		// �I�����擾
		ResultSetWrapper rs = con.executeQuery(getStockSQL());
		try
		{
			while (rs.next())
			{
				Value v = new Value();
				v.setShopId(rs.getInt("shop_id"));
				v.setItemId(rs.getInt("item_id"));
				v.setItemUseDivision(rs.getInt("item_use_division"));
				v.setStock(rs.getInt("stock"));

				map.put(v.getItemId() + "/" + v.getItemUseDivision(), v);
			}
		}
		finally
		{
			rs.close();
		}

		this.stock = map;
	}

	/**
	 * �݌ɐ����i�[����Map���Z�b�g����B
	 * @param �݌ɐ����i�[����Map�B�L�[�́u���iID/�X�̋Ɩ��敪�v
	 */
	protected void setStockMap(Map<String, Value> stock)
	{
		this.stock = stock;
	}

	/**
	 * �K���݌ɂ̒l���v�Z����SQL��Ԃ��B
	 * @param shopId �V���b�vID
	 * @param date �K���݌Ɍv�Z���
	 * @return �K���݌ɂ̒l���v�Z����SQL
	 */
	protected static String getProperStockSQL(int shopId, Date date)
	{
		StringBuilder buf = new StringBuilder();
		buf.append("select\n")
			.append("mup.shop_id\n")
			.append(", mup.product_id as item_id\n")
			.append(", coalesce(case when mup.use_proper_stock is null then calc_use.proper_stock else mup.use_proper_stock end, 0) as use_proper_stock\n")
			.append(", coalesce(case when mup.sell_proper_stock is null then calc_sell.proper_stock else mup.sell_proper_stock end, 0) as sell_proper_stock\n")
			.append("from\n")
			.append("mst_use_product mup\n")
			// �Ɩ��p�K���݌Ɍv�Z
			.append("left outer join (\n")
				.append("select\n")
				.append("a.item_id\n")
				.append(", round(b.avg / a.cnt * 1.2, 0) as proper_stock\n")
				.append("from\n")
					// �����񐔃J�E���g
					.append("(select\n")
					.append("data_slip_order_detail.shop_id\n")
					.append(", data_slip_order_detail.item_id\n")
					.append(", count(1) as cnt\n")
					.append("from\n")
					.append("data_slip_order\n")
					.append(", data_slip_order_detail\n")
					.append("where\n")
					.append("data_slip_order.shop_id = ").append(SQLUtil.convertForSQL(shopId)).append("\n")
					.append("and data_slip_order.delete_date is null\n")
					.append("and data_slip_order.shop_id = data_slip_order_detail.shop_id\n")
					.append("and data_slip_order.slip_no = data_slip_order_detail.slip_no\n")
					.append("and data_slip_order.order_date between to_timestamp(").append(SQLUtil.convertForSQLDateOnly(date)).append(", 'YYYY/MM/DD') + '-3 months' and to_timestamp(").append(SQLUtil.convertForSQLDateOnly(date)).append(", 'YYYY/MM/DD')\n")
					.append("and data_slip_order_detail.item_use_division = 2\n")
					.append("group by\n")
					.append("data_slip_order_detail.shop_id\n")
					.append(", data_slip_order_detail.item_id\n")
					.append(") a\n")
					// ���Ϗo�ɐ��Z�o
					.append(", 	(select\n")
					.append("data_slip_ship_detail.item_id\n")
					.append(", avg(data_slip_ship_detail.out_num) as avg\n")
					.append("from\n")
					.append("data_slip_ship\n")
					.append(", data_slip_ship_detail\n")
					.append("where\n")
					.append("data_slip_ship.shop_id = ").append(SQLUtil.convertForSQL(shopId)).append("\n")
					.append("and data_slip_ship.delete_date is null\n")
					.append("and data_slip_ship.shop_id = data_slip_ship_detail.shop_id\n")
					.append("and data_slip_ship.slip_no = data_slip_ship_detail.slip_no\n")
					.append("and data_slip_ship.ship_date between to_timestamp(").append(SQLUtil.convertForSQLDateOnly(date)).append(", 'YYYY/MM/DD') + '-3 months' and to_timestamp(").append(SQLUtil.convertForSQLDateOnly(date)).append(", 'YYYY/MM/DD')\n")
					.append("and data_slip_ship_detail.item_use_division = 2\n")
					.append("group by\n")
					.append("data_slip_ship_detail.shop_id\n")
					.append(", data_slip_ship_detail.item_id\n")
					.append(") b\n")
				.append("where\n")
				.append("a.item_id=b.item_id\n")
			.append(") calc_use\n")
			.append("on mup.product_id = calc_use.item_id\n")
			.append("and mup.product_division = 2\n")
			.append("and mup.delete_date is null\n")
			// �X�̗p�K���݌Ɍv�Z
			.append("left outer join (\n")
				.append("select\n")
				.append("a.item_id\n")
				.append(", round(b.avg / a.cnt * 1.2, 0) as proper_stock\n")
				.append("from\n")
					// �����񐔃J�E���g
					.append("(select\n")
					.append("data_slip_order_detail.shop_id\n")
					.append(", data_slip_order_detail.item_id\n")
					.append(", count(1) as cnt\n")
					.append("from\n")
					.append("data_slip_order\n")
					.append(", data_slip_order_detail\n")
					.append("where\n")
					.append("data_slip_order.shop_id = ").append(SQLUtil.convertForSQL(shopId)).append("\n")
					.append("and data_slip_order.delete_date is null\n")
					.append("and data_slip_order.shop_id = data_slip_order_detail.shop_id\n")
					.append("and data_slip_order.slip_no = data_slip_order_detail.slip_no\n")
					.append("and data_slip_order.order_date between to_timestamp(").append(SQLUtil.convertForSQLDateOnly(date)).append(", 'YYYY/MM/DD') + '-3 months' and to_timestamp(").append(SQLUtil.convertForSQLDateOnly(date)).append(", 'YYYY/MM/DD')\n")
					.append("and data_slip_order_detail.item_use_division = 1\n")
					.append("group by\n")
					.append("data_slip_order_detail.shop_id\n")
					.append(", data_slip_order_detail.item_id\n")
					.append(") a\n")
					// ���Ϗo�ɐ��Z�o
					.append(", 	(select\n")
					.append("data_slip_ship_detail.item_id\n")
					.append(", avg(data_slip_ship_detail.out_num) as avg\n")
					.append("from\n")
					.append("data_slip_ship\n")
					.append(", data_slip_ship_detail\n")
					.append("where\n")
					.append("data_slip_ship.shop_id = ").append(SQLUtil.convertForSQL(shopId)).append("\n")
					.append("and data_slip_ship.delete_date is null\n")
					.append("and data_slip_ship.shop_id = data_slip_ship_detail.shop_id\n")
					.append("and data_slip_ship.slip_no = data_slip_ship_detail.slip_no\n")
					.append("and data_slip_ship.ship_date between to_timestamp(").append(SQLUtil.convertForSQLDateOnly(date)).append(", 'YYYY/MM/DD') + '-3 months' and to_timestamp(").append(SQLUtil.convertForSQLDateOnly(date)).append(", 'YYYY/MM/DD')\n")
					.append("and data_slip_ship_detail.item_use_division = 1\n")
					.append("group by\n")
					.append("data_slip_ship_detail.shop_id\n")
					.append(", data_slip_ship_detail.item_id\n")
					.append(") b\n")
				.append("where\n")
				.append("a.item_id = b.item_id\n")
			.append(") calc_sell\n")
			.append("on mup.product_id = calc_sell.item_id\n")
			.append("and mup.product_division = 2\n")
			.append("and mup.delete_date is null\n")
		.append("where\n")
		.append("mup.shop_id = ").append(shopId).append("\n")
		.append("and mup.product_division = 2\n")
		.append("and mup.delete_date is null\n")
		;

		return new String(buf);
	}

	/**
	 *
	 */
	protected String getStockSQL()
        {
                return getStockSQL(null, null);
        }
	protected String getStockSQL(Integer itemId, Integer itemUseDivision)
	{
		StringBuilder buf = new StringBuilder();
		buf.append("select \n")
			.append("a.shop_id \n")
			.append(", a.item_id\n")
			.append(", a.item_use_division\n")
			.append(", get_item_stock(a.shop_id, a.item_id, a.item_use_division, ").append(SQLUtil.convertForSQLDateOnly(date)).append(") as stock\n")
			.append("from (\n")
			.append("select distinct dssd.shop_id, dssd.item_id, dssd.item_use_division ")
				.append("from data_slip_store dss, data_slip_store_detail dssd ")
				.append("where dss.shop_id = ").append(SQLUtil.convertForSQL(shopId))
				.append(" and dss.shop_id = dssd.shop_id and dss.slip_no = dssd.slip_no and dss.store_date <= ").append(SQLUtil.convertForSQLDateOnly(date))
				.append(" and dss.delete_date is null\n")
				.append(" and dssd.delete_date is null\n")
			.append("union\n")
			.append("select distinct dssd.shop_id, dssd.item_id, dssd.item_use_division ")
				.append("from data_slip_ship dss, data_slip_ship_detail dssd ")
				.append("where dss.shop_id = ").append(SQLUtil.convertForSQL(shopId))
				.append(" and dss.shop_id = dssd.shop_id and dss.slip_no = dssd.slip_no and dss.ship_date <= ").append(SQLUtil.convertForSQLDateOnly(date))
				.append(" and dss.delete_date is null\n")
				.append(" and dssd.delete_date is null\n")
			.append("union\n")
			.append("select distinct dssd.shop_id, dssd.item_id, dssd.item_use_division ")
				.append("from data_staff_sales dss, data_staff_sales_detail dssd ")
				.append("where dss.shop_id = ").append(SQLUtil.convertForSQL(shopId))
				.append(" and dss.shop_id = dssd.shop_id and dss.slip_no = dssd.slip_no and dss.sales_date <= ").append(SQLUtil.convertForSQLDateOnly(date))
				.append(" and dss.delete_date is null\n")
				.append(" and dssd.delete_date is null\n")
			.append("union\n")
			.append("select distinct inv.shop_id, invd.item_id, invd.inventory_division as item_use_division ")
				.append("from data_inventory inv, data_inventory_detail invd ")
				.append("where inv.shop_id = ").append(SQLUtil.convertForSQL(shopId))
				.append(" and inv.inventory_id = invd.inventory_id and inv.inventory_date <= ").append(SQLUtil.convertForSQLDateOnly(date))
				.append(" and inv.delete_date is null\n")
				.append(" and invd.delete_date is null\n")
			.append(") a")
		;

                buf.append(" where true");
                
                if (itemIdList != null && itemIdList.size() > 0) {
                    StringBuilder ids = new StringBuilder(1000);
                    for (Integer id : itemIdList) {
                        ids.append(",");
                        ids.append(id.toString());
                    }
                    buf.append(" and a.item_id in (" + ids.substring(1) + ")");
                }

                if (itemId != null) {
                    buf.append(" and a.item_id in (" + itemId.toString() + ")");
                }

                if (itemUseDivision != null) {
                    buf.append(" and a.item_use_division = " + itemUseDivision.toString());
                }
                
		return new String(buf);
	}

	/**
	 * �X�̏��i���Ƃ̍݌ɐ���\���I�u�W�F�N�g
	 */
	public static class Value
	{
		/** �V���b�vID */
		private int shopId;
		/** ���iID */
		private int itemId;
		/** �X�̋Ɩ��敪 */
		private int itemUseDivision;
		/** �݌ɐ� */
		private int stock;

		/**
		 * �V���b�vID���Z�b�g����B
		 * @param shopId �V���b�vID
		 */
		protected void setShopId(int shopId)
		{
			this.shopId = shopId;
		}

		/**
		 * �V���b�vID��Ԃ��B
		 * @return �V���b�vID
		 */
		public int getShopId()
		{
			return this.shopId;
		}

		/**
		 * ���iID���Z�b�g����B
		 * @param itemId ���iID
		 */
		protected void setItemId(int itemId)
		{
			this.itemId = itemId;
		}

		/**
		 * ���iID��Ԃ��B
		 * @return ���iID
		 */
		public int getItemId()
		{
			return this.itemId;
		}

		/**
		 * �X�̋Ɩ��敪���Z�b�g����B
		 * @param itemUseDivision �X�̋Ɩ��敪
		 */
		protected void setItemUseDivision(int itemUseDivision)
		{
			this.itemUseDivision = itemUseDivision;
		}

		/**
		 * �X�̋Ɩ��敪��Ԃ��B
		 * @return �X�̋Ɩ��敪
		 */
		public int getItemUseDivision()
		{
			return this.itemUseDivision;
		}

		/**
		 * �݌ɐ����Z�b�g����B
		 * @param stock �݌ɐ�
		 */
		protected void setStock(int stock)
		{
			this.stock = stock;
		}

		/**
		 * �݌ɐ���Ԃ��B
		 * @return �݌ɐ�
		 */
		public int getStock()
		{
			return this.stock;
		}
	}
}
