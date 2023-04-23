/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.data.course;

import com.geobeck.sosia.pos.hair.data.account.DataSalesDetail;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ÉRÅ[ÉXè¡âªóöó
 * @author s_furukawa
 */
public class DataContractDigestion {
	/**
	 * ìXï‹
	 */
	protected MstShop shop = new MstShop();
	/**
	 * ì`ï[No.
	 */
	protected Integer slipNo = null;
        /**
         * å_ñÒNo
         */
        protected Integer contractNo = null;
        /**
         * å_ñÒè⁄ç◊No
         */
        protected Integer contractDetailNo = null;
        /**
         * è¡âªâÒêî
         */
	protected Double productNum = 0d;
	/**
	 * è¡âªíSìñÉXÉ^ÉbÉt
	 */
	protected MstStaff staff = null;
	/**
	 * è¡âªóòópé“
	 */
	protected MstCustomer customer = null;
        /**
         * ìoò^ì˙
         */
        protected Date insertDate;

        /**
         * è¡âªì˙
         */
        protected Date salesDate;

	/**
	 * å_ñÒå≥ìXï‹ID
	 */
	private Integer contractShopId = null;

        /**
         * ÉRÉìÉXÉgÉâÉNÉ^
         */
        public DataContractDigestion()
        {
        }

        public DataContractDigestion(DataSalesDetail dataSalesDetail)
        {
            this.shop = dataSalesDetail.getShop();
            this.slipNo = dataSalesDetail.getSlipNo();
            this.contractNo = dataSalesDetail.getConsumptionCourse().getContractNo();
            this.contractDetailNo = dataSalesDetail.getConsumptionCourse().getContractDetailNo();
//            this.productNum = dataSalesDetail.getProductNum().doubleValue();
            this.productNum = dataSalesDetail.getConsumptionNum();
            this.staff = dataSalesDetail.getStaff();
            this.setContractShopId(dataSalesDetail.getContractShopId());
            // Start add 20130410 nakhoa
            if(dataSalesDetail.getContractShopId() != null){
                this.setContractShopId(dataSalesDetail.getContractShopId());
            }else{
                this.setContractShopId(dataSalesDetail.getShop().getShopID());
            }
            // End add 20130410 nakhoa
        }

        public Integer getContractDetailNo() {
            return contractDetailNo;
        }

        public void setContractDetailNo(Integer contractDetailNo) {
            this.contractDetailNo = contractDetailNo;
        }

        public Integer getContractNo() {
            return contractNo;
        }

        public void setContractNo(Integer contractNo) {
            this.contractNo = contractNo;
        }

        public Double getProductNum() {
            return productNum;
        }

        public void setProductNum(Double productNum) {
            this.productNum = productNum;
        }

        public MstShop getShop() {
            return shop;
        }

        public void setShop(MstShop shop) {
            this.shop = shop;
        }

        public Integer getSlipNo() {
            return slipNo;
        }

        public void setSlipNo(Integer slipNo) {
            this.slipNo = slipNo;
        }

        public MstStaff getStaff() {
            return staff;
        }

        public void setStaff(MstStaff staff) {
            this.staff = staff;
        }
        
        public MstCustomer getCustomer() {
            return customer;
        }

        public void setCustomer(MstCustomer customer) {
            this.customer = customer;
        }

        public Date getInsertDate() {
            return insertDate;
        }

        public void setInsertDate(Date insertDate) {
            this.insertDate = insertDate;
        }

        public Date getSalesDate() {
            return salesDate;
        }

        public void setSalesDate(Date insertDate) {
            this.salesDate = salesDate;
        }

        public Integer getContractShopId() {
            return contractShopId;
        }

        public void setContractShopId(Integer contractShopId) {
            this.contractShopId = contractShopId;
        }

       	public void setData(ResultSetWrapper rs) throws SQLException
	{
            this.shop.setShopID(rs.getInt("shop_id"));
            this.slipNo = rs.getInt("slip_no");
            this.contractNo = rs.getInt("contract_no");
            this.contractDetailNo = rs.getInt("contract_detail_no");
            this.productNum = rs.getDouble("product_num");
            this.staff = new MstStaff();
            this.staff.setStaffID(rs.getInt("staff_id"));
            this.staff.setStaffName(new String[]{rs.getString("staff_name1"), rs.getString("staff_name2")});
            this.customer = new MstCustomer();
            this.customer.setCustomerID(rs.getInt("customer_id"));
            this.customer.setCustomerName(new String[]{rs.getString("customer_name1"), rs.getString("customer_name2")});
            this.setCustomer(this.customer);
            this.insertDate = rs.getDate("insert_date");
            this.salesDate = rs.getDate("sales_date");
            this.contractShopId = (rs.getInt("contract_shop_id"));
        }

        /**
         * ÉRÅ[ÉXè¡âªóöóÇìoò^Ç∑ÇÈ
         * @param con
         * @return
         * @throws SQLException
         */
       	public boolean regist(ConnectionWrapper con) throws SQLException
	{
            String	sql		=	"";

//            if(isExists(con))
//            {
//                    sql	=	this.getUpdateSQL();
//            }
//            else
//            {
//                    sql	=	this.getInsertSQL();
//            }

            sql = this.getInsertSQL();

            if(con.executeUpdate(sql) == 1)
            {
                    return	true;
            }
            else
            {
                    return	false;
            }
        }

      	public static List<DataContractDigestion> getDataContractDigestionList(ConnectionWrapper con, Integer shopId, Integer slipNo) throws SQLException
	{
               List<DataContractDigestion> list = new ArrayList<DataContractDigestion>();

                ResultSetWrapper	rs	=	con.executeQuery(DataContractDigestion.getSelectSQL(shopId, slipNo));
                while (rs.next()) {
                    DataContractDigestion dataContractDigestion = new DataContractDigestion();
                    MstShop shop = new MstShop();
                    shop.setShopID(shopId);
                    dataContractDigestion.setShop(shop);
                    dataContractDigestion.setSlipNo(slipNo);
                    dataContractDigestion.setContractNo(rs.getInt("contract_no"));
                    dataContractDigestion.setContractDetailNo(rs.getInt("contract_detail_no"));
                    dataContractDigestion.setProductNum(rs.getDouble("product_num"));
                    MstStaff staff = new MstStaff();
                    staff.setStaffID(rs.getInt("staff_id"));
                    dataContractDigestion.setStaff(staff);
                    dataContractDigestion.setInsertDate(rs.getDate("insert_date"));
                    dataContractDigestion.setContractShopId(rs.getInt("contract_shop_id"));

                    list.add(dataContractDigestion);
                }

                return list;
        }

        /**
         * ÉRÅ[ÉXè¡âªóöóÇìoò^Ç∑ÇÈÉNÉGÉäÇï‘Ç∑
         * @return
         */
        private String getInsertSQL()
        {
            String sql = "INSERT INTO data_contract_digestion\n"
                        + "(shop_id, \n"
                        + "slip_no, \n"
                        + "contract_no, \n"
                        + "contract_detail_no, \n"
                        + "product_num, \n"
                        + "staff_id, \n"
                        + "contract_shop_id, \n"
                        + "insert_date, \n"
                        + "update_date, \n"
                        + "delete_date) \n"
                        + "VALUES (\n"
                        + SQLUtil.convertForSQL(this.shop.getShopID()) + ", \n"
                        + SQLUtil.convertForSQL(this.slipNo) + ", \n"
                        + SQLUtil.convertForSQL(this.contractNo) + ", \n"
                        + SQLUtil.convertForSQL(this.contractDetailNo) + ", \n"
                        + SQLUtil.convertForSQL(this.productNum) + ", \n"
                        + SQLUtil.convertForSQL(this.staff.getStaffID()) + ", \n"
                        + SQLUtil.convertForSQL(this.getContractShopId()) + ", \n"
                        + "current_timestamp, \n"
                        + "current_timestamp, \n"
                        + "null)";
            return sql;
        }

        /**
         * ì`ï[î‘çÜÇ©ÇÁÉRÅ[ÉXè¡âªóöóÇéÊìæÇ∑ÇÈÉNÉGÉäÇï‘Ç∑
         * @param shopId
         * @param slipNo
         * @return
         */
        private static String getSelectSQL(Integer shopId, Integer slipNo){
            String sql = "select * from data_contract_digestion " +
                         "where shop_id = " + SQLUtil.convertForSQL(shopId) +
                         "and slip_no = " + SQLUtil.convertForSQL(slipNo) +
                         "and delete_date is null " +
                         "order by insert_date";
            return sql;
        }

}
