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
 * �ڋq���������p�f�[�^
 * @author
 */
public class CustomerData
{
        protected	DataCustomer			customer                    =	new DataCustomer();

	/**
	 * �R���X�g���N�^
	 */
	public CustomerData()
	{
	}

        //�ڋqID���擾
        public Integer getCustomerId()
	{
            return customer.getCustomerId();
	}
        
        //�ڋqNO���擾
        public String getCustomerNo()
	{
            return customer.getCustomerNo();
	}

        //�ڋq�����擾
        public String getCustomerName()
	{
            return customer.getCustomerName();
	}

        //�X�֔ԍ����擾
        public String getPostalcode()
	{
            return customer.getPostalcode();
	}

        //�Z�����擾
        public String getAddress()
	{
            return customer.getAddress();
	}

        //�d�b�ԍ����擾
        public String getPhoneNumber()
	{
            return customer.getPhoneNumber();
	}

        //�g�єԍ����擾
        public String getCellularNumber()
	{
            return customer.getCellularNumber();
	}

        //���N�������擾
        public java.util.Date getBirthday()
	{
            return customer.getBirthday();
	}

        //�ΏێҐ����擾
        public Integer getTargetNumber()
	{
            return customer.getTargetNumber();
	}

        //�ڋqID���擾�i�ڍׁj
        public Integer getCustomerId_D()
	{
            return customer.getCustomerId_D();
	}

        //�ڋqNO���擾�i�ڍׁj
        public String getCustomerNo_D()
	{
            return customer.getCustomerNo_D();
	}

        //�ڋq�����擾�i�ڍׁj
        public String getCustomerName_D()
	{
            return customer.getCustomerName_D();
	}

        //�K��񐔂��擾�i�ڍׁj
        public Integer getVisitNumber_D()
	{
            return customer.getVisitNumber_D();
	}

        //�ӂ肪�ȁi�ڍׁj
        public String getCustomerKana_D()
	{
            return customer.getCustomerKana_D();
	}

        //�X�֔ԍ��i�ڍׁj
        public String getPostalcode_D()
	{
            return customer.getPostalcode_D();
	}

        //�Z��1�i�ڍׁj
        public String getAddress1_D()
	{
            return customer.getAddress1_D();
	}

        //�Z��2�i�ڍׁj
        public String getAddress2_D()
	{
            return customer.getAddress2_D();
	}

        //�Z��3�i�ڍׁj
        public String getAddress3_D()
	{
            return customer.getAddress3_D();
	}

        //�Z��4�i�ڍׁj
        public String getAddress4_D()
	{
            return customer.getAddress4_D();
	}

        //�d�b�ԍ��i�ڍׁj
        public String getPhoneNumber_D()
	{
            return customer.getPhoneNumber_D();
	}

        //�g�єԍ��i�ڍׁj
        public String getCellularNumber_D()
	{
            return customer.getCellularNumber_D();
	}

        //PC���[���i�ڍׁj
        public String getPc_mail_address_D()
	{
            return customer.getPc_mail_address_D();
	}

        //�g�у��[���i�ڍׁj
        public String getCellular_mail_address_D()
	{
            return customer.getCellular_mail_address_D();
	}

        //���N�����i�ڍׁj
        public java.util.Date getBirthday_D()
	{
            return customer.getBirthday_D();
	}

        //���ʁi�ڍׁj
        public String getSex_D()
	{
            return customer.getSex_D();
	}

        //�E�Ɓi�ڍׁj
        public String getJob_D()
	{
            return customer.getJob_D();
	}

        //���l�i�ڍׁj
        public String getNote_D()
	{
            return customer.getNote_D();
	}

         /**
	 * ResultSetWrapper����f�[�^���Z�b�g����B
	 * @param rs ResultSetWrapper
	 * @param paymentClasses �x���敪���X�g�f�[�^
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
                //�ڋqID
                customer.setCustomerId(rs.getInt("customer_id"));
                //�ڋqNO
                customer.setCustomerNo(rs.getString("customer_no"));
                //�ڋq��
                customer.setCustomerName(rs.getString("customer_name"));
                //�X�֔ԍ�
                customer.setPostalcode(rs.getString("postal_code"));
                //�Z��(�s�撬���܂�)
                customer.setAddress(rs.getString("address"));
                //�d�b�ԍ�
                customer.setPhoneNumber(rs.getString("phone_number"));
                //�g�єԍ�
                customer.setCellularNumber(rs.getString("cellular_number"));
                //���N����
                customer.setBirthday(rs.getDate("birthday"));
                //�ΏێҐ�
                customer.setTargetNumber(rs.getInt("target_number"));

	}

         /**
	 * ResultSetWrapper����f�[�^���Z�b�g����B
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setCustomerDetailData(ResultSetWrapper rs) throws SQLException
	{
                //�ڋqID
                customer.setCustomerId_D(rs.getInt("customer_id"));
                //�ڋqNO
                customer.setCustomerNo_D(rs.getString("customer_no"));
                //�ڋq��
                customer.setCustomerName_D(rs.getString("customer_name"));
                //���X��
                customer.setVisitNumber_D(rs.getInt("visit_num"));

                //�ӂ肪��
                customer.setCustomerKana_D(rs.getString("customer_kana"));
                //�X�֔ԍ�
                customer.setPostalcode_D(rs.getString("postal_code"));
                //�Z��1
                customer.setAddress1_D(rs.getString("address1"));
                //�Z��2
                customer.setAddress2_D(rs.getString("address2"));
                //�Z��3
                customer.setAddress3_D(rs.getString("address3"));
                //�Z��4
                customer.setAddress4_D(rs.getString("address4"));
                //�d�b�ԍ�
                customer.setPhoneNumber_D(rs.getString("phone_number"));
                //�g�єԍ�
                customer.setCellularNumber_D(rs.getString("cellular_number"));
                //PC���[��
                customer.setPc_mail_address_D(rs.getString("pc_mail_address"));
                //�g�у��[��
                customer.setCellular_mail_address_D(rs.getString("cellular_mail_address"));
                //���N����
                customer.setBirthday_D(rs.getDate("birthday"));
                //����
                customer.setSex_D(rs.getString("sex"));
                //�E��
                customer.setJob_D(rs.getString("job_name"));
                //���l
                customer.setNote_D(rs.getString("note"));
	}

        public String toString() {
            return getCustomerName_D();
        }
}
