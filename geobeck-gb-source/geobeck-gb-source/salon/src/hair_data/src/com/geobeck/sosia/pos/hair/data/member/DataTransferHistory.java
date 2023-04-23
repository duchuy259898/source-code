/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.member;

import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author lvtu
 */
public class DataTransferHistory extends ArrayList<DataTransferResult> {
    
        /**
	 * 口座情報
	 */
	private         DataAccountInfo         dataAccountInfo		=	null;

        /**
         * 口座情報
         * @return 
         */
        public DataAccountInfo getDataAccountInfo() {
            return dataAccountInfo;
        }

        /**
         * 口座情報
         * @param dataAccountInfo 
         */
        public void setDataAccountInfo(DataAccountInfo dataAccountInfo) {
            this.dataAccountInfo = dataAccountInfo;
        }
        
        /**
	 * ResultSetWrapperからデータをセットする
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
            DataAccountInfo accountInfo = new DataAccountInfo();
            accountInfo.setData(rs);
            this.setDataAccountInfo(accountInfo);
	}
        
/**
	 * ResultSetWrapperからデータをセットする
	 * @param rs ResultSetWrapper
         * @param detail
	 * @throws java.sql.SQLException SQLException
	 */
	public void setDataTransferResultDetail(ResultSetWrapper rs, DataTransferResultDetail detail) throws SQLException
	{
		detail.setTransferID(rs.getInt("transfer_id"));
		detail.setTransferDetailID(rs.getInt("transfer_detail_id"));
		detail.setCustomerID(rs.getInt("customer_id"));
                if (detail.getCustomerID() != null ) {
                    MstCustomer cus = new MstCustomer(detail.getCustomerID());
                    DataAccountInfo accountInfo = new DataAccountInfo(cus);
                    accountInfo.load(SystemInfo.getConnection());
                    detail.setDataAccountInfo(accountInfo);
                }
                detail.setAccountCustomerNo(rs.getString("account_customer_no"));
                detail.setBankCode(rs.getString("bank_code"));
                detail.setBranchCode(rs.getString("branch_code"));
		detail.setAccountType(rs.getString("account_type"));
		detail.setAccountNumber(rs.getString("account_number"));
		detail.setAccountName(rs.getString("account_name"));
                detail.setBillingAmount(rs.getInt("billing_amount"));
                Object obj = rs.getObject("result_code");
                if (obj != null) {
                    detail.setResultCode(rs.getInt("result_code"));
                }
                detail.setShopID(rs.getInt("shop_id"));
                if (detail.getShopID() != null) {
                    MstShop shop = new MstShop();
                    shop.setShopID(detail.getShopID());
                    shop.load(SystemInfo.getConnection());
                    detail.setMstShop(shop);
                }
                detail.setSlipNo(rs.getInt("slip_no"));
                detail.setPlanName(rs.getString("plan_name"));
	}
        
        /**
	 * 請求・結果情報明細をArrayListに読み込む。
         * @param con
        * @param customerID
        * @throws java.sql.SQLException
	 */
	public void loadByCustomer(ConnectionWrapper con, Integer customerID) throws SQLException
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getTransferDetailByCustomerSQL(customerID));

		while(rs.next())
		{
                        if (this.getDataAccountInfo() == null || this.getDataAccountInfo().getCustomer() == null ) {
                            this.setData(rs);
                        }
                        Integer  transferId = rs.getInt("transfer_id");
                        DataTransferResult transferResult = this.lookup(transferId);
                        if (transferResult == null) {
                            transferResult	=	new	DataTransferResult();
                            transferResult.setData(rs);
                            DataTransferResultDetail	detail	=	new	DataTransferResultDetail();
                            setDataTransferResultDetail(rs, detail);
                            transferResult.add(detail);
                            
                            this.add(transferResult);
                        } else {
                            DataTransferResultDetail detail = new DataTransferResultDetail();
                            setDataTransferResultDetail(rs, detail);
                            
                            transferResult.add(detail);
                        }
		}

		rs.close();
	}
        
        /**
	 * DataTransferResult。
	 * @param transferId
	 * @return 
	 */
	private DataTransferResult lookup(Integer transferId)
	{
		for (DataTransferResult result : this)
		{
			if (transferId.equals(result.getTransferID()))
			{
				return result;
			}
		}

		return null;
	}
        
        /**
	 * 請求・結果情報明細
        * @param customerID
	 * @return 請求・結果情報明細タデータを全て取得するＳＱＬ文
	 */
	public String getTransferDetailByCustomerSQL(Integer customerID)
	{
		return	"select T1.*, \n" +
                        "T3.transfer_id, \n" +
                        "T3.target_month, \n" +
                        "T3.transfer_year, \n" +
                        "T3.transfer_month, \n" +
                        "T3.transfer_date, \n" +
                        "T3.total_num, \n" +
                        "T3.total_amount, \n" +
                        "T3.status, \n" +
                        "T2.transfer_detail_id, \n" +
                        "T2.billing_amount, \n" +
                        "T2.result_code, \n" +
                        "T2.slip_no, \n" +
                        "T4.shop_id, \n" +
                        "T4.shop_name, \n" +
                        "T8.plan_name \n" +
                        "from data_account_info T1 \n" +
                        "inner join data_transfer_result_detail T2 on T2.customer_id = T1.customer_id \n" +
                        "inner join data_transfer_result T3 on T3.transfer_id = T2.transfer_id \n" +
                        "inner join mst_shop T4 on T4.shop_id = T2.shop_id \n" +
                        "inner join data_monthly_batch_detail_log T5 on T5.slip_no = T2.slip_no and T5.customer_id = T2.customer_id \n" +
                        "inner join data_monthly_batch_log T6 on T6.batch_id = T5.batch_id and T6.shop_id = T2.shop_id \n" +
                        "inner join data_month_member T7 on T7.shop_id = T2.shop_id and T7.month_contract_id = T5.month_contract_id \n" +
                        "inner join mst_plan T8 on T8.plan_id = T7.plan_id \n" +
			"where T1.delete_date is null\n" +
				"and T1.customer_id = " + SQLUtil.convertForSQL(customerID) + "\n" +
			"order by T3.target_month, T3.transfer_id, T2.transfer_detail_id\n";
	}
}
