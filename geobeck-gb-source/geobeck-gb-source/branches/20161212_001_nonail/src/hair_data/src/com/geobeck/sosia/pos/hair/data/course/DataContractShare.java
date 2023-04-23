/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.data.course;

import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * �R�[�X�_�񋤓����p��
 * @author ennyu
 */
public class DataContractShare{
	/**
	 * �X��
	 */
	protected MstShop shop = new MstShop();
	/**
	 * �ڋq�f�[�^
	 */
	protected MstCustomer	customer	=	new MstCustomer();
	/**
	* �_��No
	*/
	protected Integer contractNo = null;
	/**
	 * �_��ڍ�No
	 */
	protected Integer contractDetailNo = null;
	
	/**
	 * �ŏI���X��
	 */
	protected java.util.Date salesDate = null;
	
	/**
	 * �ŏI���X��
	 */
	protected Double productNum = null;

	public Integer getContractNo() {
		return contractNo;
	}
	
	public void setContractNo(Integer contractNo) {
		this.contractNo = contractNo;
	}
	
	public Integer getContractDetailNo() {
		return contractDetailNo;
	}
	
	public void setContractDetailNo(Integer contractDetailNo) {
		this.contractDetailNo = contractDetailNo;
	}
	
	public java.util.Date getSalesDate() {
		return salesDate;
	}
	
	public void setSalesDate(java.util.Date salesDate) {
		this.salesDate = salesDate;
	}
	
	public MstCustomer getCustomer()
	{
		return customer;
	}
	
	public void setCustomer(MstCustomer customer)
	{
		this.customer = customer;
	}
	
	public MstShop getShop()
	{
		return shop;
	}
	
	public void setShop(MstShop shop)
	{
		this.shop = shop;
	}
	
	public Double getProductNum()
	{
		return productNum;
	}
	
	public void setProductNum(Double productNum)
	{
		this.productNum = productNum;
	}
	
	public DataContractShare(){
		
	}
	
 	/**
	 * �������p�ҏ�񃊃X�g���擾����
	 * @param con ConnectionWrapper
	 * @param shopId �X��ID
	 * @param contractNo �_��No
	 * @param contractDetailNo �_��ڍ�No
	 * @return �������p�ҏ�񃊃X�g
	 * @throws java.sql.SQLException ��O
	 */
	public static List<DataContractShare> getDataContractShareList(ConnectionWrapper con, Integer shopId, Integer slipNo, Integer contractNo,Integer contractDetailNo) throws SQLException
	{
		List<DataContractShare> list = new ArrayList<DataContractShare>();
		System.out.println(DataContractShare.getSelectSQL(shopId, slipNo,contractNo,contractDetailNo));
		ResultSetWrapper	rs	=	con.executeQuery(DataContractShare.getSelectSQL(shopId, slipNo,contractNo,contractDetailNo));
		while(rs.next()){
			DataContractShare dataContractShare = new DataContractShare();
			MstShop shop = new MstShop();
			shop.setShopID(shopId);
			dataContractShare.setShop(shop);
			MstCustomer customer = new MstCustomer();
			customer.setCustomerID(rs.getInt("customer_id"));
			customer.setCustomerNo(rs.getString("customer_no"));
			customer.setCustomerName(0,rs.getString("customer_name1"));
			customer.setCustomerName(1,rs.getString("customer_name2"));
			dataContractShare.setCustomer(customer);
			dataContractShare.setSalesDate(rs.getDate("sales_date"));
			dataContractShare.setProductNum(rs.getDouble("product_num"));
			list.add(dataContractShare);
		}
		return list;
	}
	
	/**
	 * �������p�ҏ�񃊃X�g���擾����r�p�k�����擾����B
	 * @return �������p�ҏ�񃊃X�g���擾����r�p�k��
	 */
	public static String getSelectSQL(Integer shopId, Integer slipNo, Integer contractNo, Integer contractDetailNo)
	{
		return	"select mc.customer_no,mc.customer_name1,customer_name2,dcs.customer_id,last_ds.sales_date,dcd.product_num\n"
				+ "from data_contract_share dcs\n"
				+ "inner join mst_customer mc\n"
				+ "on mc.customer_id=dcs.customer_id\n"
				+ "left join (select coalesce(sum(dcd.product_num), 0) as product_num ,ds.customer_id as dcd_customer_id from data_contract_digestion dcd inner join data_sales ds on ds.shop_id=dcd.shop_id and ds.slip_no=dcd.slip_no where dcd.shop_id = " + SQLUtil.convertForSQL(shopId) + " and dcd.contract_no = " + SQLUtil.convertForSQL(contractNo) + " and dcd.contract_detail_no = " + SQLUtil.convertForSQL(contractDetailNo) + " and dcd.delete_date is null group by ds.customer_id) as dcd\n"
				+ "on dcd.dcd_customer_id=dcs.customer_id\n"
                                //IVS_LVTu start edit 2015/11/06 Bug #44177
				//+ "left join (select max(sales_date) as sales_date,customer_id from data_sales where delete_date is null and sales_date is not null and shop_id = " + SQLUtil.convertForSQL(shopId) + " group by customer_id) last_ds\n"
                                + "left join (select max(sales_date) as sales_date,customer_id from data_sales where delete_date is null and sales_date is not null group by customer_id) last_ds\n"
                                //IVS_LVTu end edit 2015/11/06 Bug #44177
				+ "on dcs.customer_id = last_ds.customer_id\n"
				+ "where dcs.shop_id = " + SQLUtil.convertForSQL(shopId) + "\n"
				+ "and dcs.contract_no = " + SQLUtil.convertForSQL(contractNo) + "\n"
				+ "and dcs.contract_detail_no = " + SQLUtil.convertForSQL(contractDetailNo) + "\n"
				+ "and dcs.delete_date is null";
	}
	
	/**
	 * �R�[�X�_��Ƌ����w���҂�o�^����
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public static boolean registDataContractShare(ConnectionWrapper con, Integer shopId, Integer customerID,Integer contractNo, Integer contractDetailNo) throws SQLException{
		String selectSql = "select * from  data_contract_share\n " 
				+ "where	customer_id = " + SQLUtil.convertForSQL(customerID) + "\n"
				+ "and	shop_id = " + SQLUtil.convertForSQL(shopId) + "\n"
				+ "and	contract_no = " + SQLUtil.convertForSQL(contractNo) + "\n"
				+ "and	contract_detail_no = " + SQLUtil.convertForSQL(contractDetailNo) + "\n"
				+ "and  delete_date is not null\n";
		ResultSetWrapper rs = con.executeQuery(selectSql);
		boolean existFlg = (rs.next()) ? true : false;
		String sql = getInsertDataContractShareSQL(shopId, customerID,contractNo,contractDetailNo,existFlg);
		if(con.execute(sql)){
			return	true;
		}else{
			return	false;
		}
	}
	
	 /**
	 * �R�[�X�_��Ƌ����w���҂�o�^����N�G����Ԃ�
	 * @return
	 */
	private static String getInsertDataContractShareSQL(Integer shopId, Integer customerID,Integer contractNo, Integer contractDetailNo, boolean existFlg)
	{
		String sql = "";
		if (existFlg) {
			sql = "update data_contract_share\n " 
				+ "set\n"
				+ "insert_date = current_timestamp,\n"
				+ "update_date = current_timestamp,\n"
				+ "delete_date = null\n"
				+ "where	customer_id = " + SQLUtil.convertForSQL(customerID) + "\n"
				+ "and	shop_id = " + SQLUtil.convertForSQL(shopId) + "\n"
				+ "and	contract_no = " + SQLUtil.convertForSQL(contractNo) + "\n"
				+ "and	contract_detail_no = " + SQLUtil.convertForSQL(contractDetailNo) + "\n"; 		
		} else {
			sql = "INSERT INTO data_contract_share\n " 
				+ "(shop_id, \n"
				+ "contract_no, \n" 
				+ "contract_detail_no, \n"                       
				+ "customer_id, \n"
				+ "insert_date, \n"
				+ "update_date, \n"
				+ "delete_date) \n"
				+ "VALUES (\n"
				+ SQLUtil.convertForSQL(shopId) + ", \n"
				+ SQLUtil.convertForSQL(contractNo) + ", \n"
				+ SQLUtil.convertForSQL(contractDetailNo) + ", \n"                       
				+ SQLUtil.convertForSQL(customerID) + ", \n"
				+ "current_timestamp, \n"
				+ "current_timestamp, \n"
				+ "null)";
			}
		return sql;
	}
	
	/**
	 * �X�^�b�t�}�X�^����f�[�^���폜����B�i�_���폜�j
	 *
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - ����
	 */
	public static boolean delete(ConnectionWrapper con,Integer shopId, Integer customerID,Integer contractNo, Integer contractDetailNo) throws SQLException {
		String sql = getDeleteSQL(shopId,customerID,contractNo,contractDetailNo);
		if (con.executeUpdate(sql) == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * �폜�pUpdate�����擾����B
	 *
	 * @return �폜�pUpdate��
	 */
	private static String getDeleteSQL(Integer shopId, Integer customerID,Integer contractNo, Integer contractDetailNo) {
		return "update data_contract_share\n"
		+ "set\n"
		+ "delete_date = current_timestamp\n"
		+ "where	customer_id = " + SQLUtil.convertForSQL(customerID) + "\n"
		+ "and	shop_id = " + SQLUtil.convertForSQL(shopId) + "\n"
		+ "and	contract_no = " + SQLUtil.convertForSQL(contractNo) + "\n"
		+ "and	contract_detail_no = " + SQLUtil.convertForSQL(contractDetailNo) + "\n";
	}
}
