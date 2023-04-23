/*
 * CustomerData.java
 *
 * Created on 2011/07/25, 18:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.search.account;

import com.geobeck.sosia.pos.data.account.*;
import java.sql.*;
import com.geobeck.sql.*;

/**
 * 顧客統合検索用データ
 * @author
 */
public class CustomerData
{
        protected	DataCustomer			customer                    =	new DataCustomer();

	/**
	 * コンストラクタ
	 */
	public CustomerData()
	{
	}

        //顧客IDを取得
        public Integer getCustomerId()
	{
            return customer.getCustomerId();
	}
        
        //顧客NOを取得
        public String getCustomerNo()
	{
            return customer.getCustomerNo();
	}

        //顧客名を取得
        public String getCustomerName()
	{
            return customer.getCustomerName();
	}

        //郵便番号を取得
        public String getPostalcode()
	{
            return customer.getPostalcode();
	}

        //住所を取得
        public String getAddress()
	{
            return customer.getAddress();
	}

        //電話番号を取得
        public String getPhoneNumber()
	{
            return customer.getPhoneNumber();
	}

        //携帯番号を取得
        public String getCellularNumber()
	{
            return customer.getCellularNumber();
	}

        //生年月日を取得
        public java.util.Date getBirthday()
	{
            return customer.getBirthday();
	}

        //対象者数を取得
        public Integer getTargetNumber()
	{
            return customer.getTargetNumber();
	}

        //顧客IDを取得（詳細）
        public Integer getCustomerId_D()
	{
            return customer.getCustomerId_D();
	}

        //顧客NOを取得（詳細）
        public String getCustomerNo_D()
	{
            return customer.getCustomerNo_D();
	}

        //顧客名を取得（詳細）
        public String getCustomerName_D()
	{
            return customer.getCustomerName_D();
	}

        //訪問回数を取得（詳細）
        public Integer getVisitNumber_D()
	{
            return customer.getVisitNumber_D();
	}

        //ふりがな（詳細）
        public String getCustomerKana_D()
	{
            return customer.getCustomerKana_D();
	}

        //郵便番号（詳細）
        public String getPostalcode_D()
	{
            return customer.getPostalcode_D();
	}

        //住所1（詳細）
        public String getAddress1_D()
	{
            return customer.getAddress1_D();
	}

        //住所2（詳細）
        public String getAddress2_D()
	{
            return customer.getAddress2_D();
	}

        //住所3（詳細）
        public String getAddress3_D()
	{
            return customer.getAddress3_D();
	}

        //住所4（詳細）
        public String getAddress4_D()
	{
            return customer.getAddress4_D();
	}

        //電話番号（詳細）
        public String getPhoneNumber_D()
	{
            return customer.getPhoneNumber_D();
	}

        //携帯番号（詳細）
        public String getCellularNumber_D()
	{
            return customer.getCellularNumber_D();
	}

        //PCメール（詳細）
        public String getPc_mail_address_D()
	{
            return customer.getPc_mail_address_D();
	}

        //携帯メール（詳細）
        public String getCellular_mail_address_D()
	{
            return customer.getCellular_mail_address_D();
	}

        //生年月日（詳細）
        public java.util.Date getBirthday_D()
	{
            return customer.getBirthday_D();
	}

        //性別（詳細）
        public String getSex_D()
	{
            return customer.getSex_D();
	}

        //職業（詳細）
        public String getJob_D()
	{
            return customer.getJob_D();
	}

        //備考（詳細）
        public String getNote_D()
	{
            return customer.getNote_D();
	}

         /**
	 * ResultSetWrapperからデータをセットする。
	 * @param rs ResultSetWrapper
	 * @param paymentClasses 支払区分リストデータ
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
                //顧客ID
                customer.setCustomerId(rs.getInt("customer_id"));
                //顧客NO
                customer.setCustomerNo(rs.getString("customer_no"));
                //顧客名
                customer.setCustomerName(rs.getString("customer_name"));
                //郵便番号
                customer.setPostalcode(rs.getString("postal_code"));
                //住所(市区町村まで)
                customer.setAddress(rs.getString("address"));
                //電話番号
                customer.setPhoneNumber(rs.getString("phone_number"));
                //携帯番号
                customer.setCellularNumber(rs.getString("cellular_number"));
                //生年月日
                customer.setBirthday(rs.getDate("birthday"));
                //対象者数
                customer.setTargetNumber(rs.getInt("target_number"));

	}

         /**
	 * ResultSetWrapperからデータをセットする。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setCustomerDetailData(ResultSetWrapper rs) throws SQLException
	{
                //顧客ID
                customer.setCustomerId_D(rs.getInt("customer_id"));
                //顧客NO
                customer.setCustomerNo_D(rs.getString("customer_no"));
                //顧客名
                customer.setCustomerName_D(rs.getString("customer_name"));
                //来店回数
                customer.setVisitNumber_D(rs.getInt("visit_num"));

                //ふりがな
                customer.setCustomerKana_D(rs.getString("customer_kana"));
                //郵便番号
                customer.setPostalcode_D(rs.getString("postal_code"));
                //住所1
                customer.setAddress1_D(rs.getString("address1"));
                //住所2
                customer.setAddress2_D(rs.getString("address2"));
                //住所3
                customer.setAddress3_D(rs.getString("address3"));
                //住所4
                customer.setAddress4_D(rs.getString("address4"));
                //電話番号
                customer.setPhoneNumber_D(rs.getString("phone_number"));
                //携帯番号
                customer.setCellularNumber_D(rs.getString("cellular_number"));
                //PCメール
                customer.setPc_mail_address_D(rs.getString("pc_mail_address"));
                //携帯メール
                customer.setCellular_mail_address_D(rs.getString("cellular_mail_address"));
                //生年月日
                customer.setBirthday_D(rs.getDate("birthday"));
                //性別
                customer.setSex_D(rs.getString("sex"));
                //職業
                customer.setJob_D(rs.getString("job_name"));
                //備考
                customer.setNote_D(rs.getString("note"));
	}

        public String toString() {
            return getCustomerName_D();
        }
}
