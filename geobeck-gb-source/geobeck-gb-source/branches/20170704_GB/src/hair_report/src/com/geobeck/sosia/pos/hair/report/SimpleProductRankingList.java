package com.geobeck.sosia.pos.hair.report;

import java.util.ArrayList;
import java.util.logging.Level;

import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ResultSetWrapper;

/**
 * ���i�����L���O�ꗗ�B����؃����L���O�@�\���ɊY���N���X���g�p�B
 * @author aims_katsu
 */
public class SimpleProductRankingList extends ArrayList<ProductRanking> {
	/**
	 * �X��ID���X�g
	 */
	private String shopIDList = "";
	/**
	 * �Ώۊ���(�J�n��)
	 */
	private String startDateStr = "";
	/**
	 * �Ώۊ���(�I����)
	 */
	private String endDateStr = "";
	
	/**
	 * �\����
	 */
	private String orderKey = "product_num";

	/**
	 * Creates a new instance of ProductRankingList
	 */
	public SimpleProductRankingList() {
	}

	/**
	 * �X��ID���X�g���擾����B
	 * @return �X��ID���X�g
	 */
	public String getShopIDList() {
		return shopIDList;
	}

	/**
	 * �X��ID���X�g���Z�b�g����B
	 * @param shopIDList �X��ID���X�g
	 */
	public void setShopIDList(String shopIDList) {
		this.shopIDList = shopIDList;
	}

	/**
	 * �Ώۊ���(�J�n��)���擾����B
	 * @return �Ώۊ���(�J�n��)
	 */
	public String getStartDateStr() {
		return startDateStr;
	}

	/**
	 * �Ώۊ���(�J�n��)���Z�b�g����B
	 * @param termFrom �Ώۊ���(�J�n��)
	 */
	public void setStartDateStr(String s) {
		this.startDateStr = s;
	}

	/**
	 * �Ώۊ���(�I����)���擾����B
	 * @return �Ώۊ���(�I����)
	 */
	public String getEndDateStr() {
		return endDateStr;
	}

	/**
	 * �Ώۊ���(�I����)���Z�b�g����B
	 * @param termTo �Ώۊ���(�I����)
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
	 * �f�[�^��ǂݍ��ށB
	 */
	public void load() {
		try {
			//�����L���O���X�g�擾
			this.getProductRankingList();
		} catch (Exception e) {
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	/**
	 * �X�^�b�t�����L���O�̃��X�g���擾����B
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
	 * �X�^�b�t�����L���O���o�pSQL���擾����B
	 * @return �X�^�b�t�����L���O���o�pSQL
	 * @exception Exception
	 */
	private String getProductRankingSQL() throws Exception {
		StringBuilder sql = new StringBuilder(1000);
		sql.append(" select");
		sql.append("      a.product_id");
		sql.append("     ,max(b.product_name)    as product_name");
		sql.append("     ,sum(a.product_num)     as product_num");

		// ������ō��݋��z
		sql.append(" ,sum(a.discount_detail_value_in_tax) as sales_value");
		// �ō��݊������z
		sql.append(" ,sum(a.detail_value_in_tax - a.discount_detail_value_in_tax) as discount_value");

		sql.append(" from");
		sql.append("     view_data_sales_detail_valid a");
		sql.append("         join");
		sql.append("             (");

		//--------------------
		// ���i
		//--------------------
		sql.append(" select");
		sql.append("      a.item_id          as product_id");
		sql.append("     ,a.item_name        as product_name");
		sql.append("     ,b.item_class_name  as class_name");

		// �ō�
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

		//�\����
		sql.append(" " + this.getOrderKey() + " desc");

		// limit
		sql.append(" limit 10");

		System.out.println("SimpleProductRankingList##getProductRankingSQL:" + sql);
		return sql.toString();
	}
}
