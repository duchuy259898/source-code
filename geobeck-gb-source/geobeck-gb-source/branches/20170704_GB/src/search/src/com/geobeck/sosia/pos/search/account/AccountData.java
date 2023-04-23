/*
 * AccountData.java
 *
 * Created on 2006/05/19, 16:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.search.account;

import com.geobeck.sosia.pos.data.account.*;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.*;
import com.geobeck.sosia.pos.master.account.*;

/**
 * �`�[�����p���Z�f�[�^
 * @author katagiri
 */
public class AccountData
{
	/**
	 * �`�[�w�b�_�f�[�^
	 */
	protected	DataSales			sales                       =	new DataSales(SystemInfo.getTypeID());
        
	protected	Integer                         reservationNo               =   null;
	
	protected	Long				totalValue                  =	0l;
	
	protected	Long				billValue                   =	0l;

        protected	Long				total                       =	0l;
	
	protected	Long				billTotal                   =	0l;

	protected	Long				paymentTotal                =	0l;

	protected	Long				paymentValue1               =	0l;
	protected	Long				paymentValue2               =	0l;
	protected	Long				paymentValue1Total          =	0l;
	protected	Long				paymentValue2Total          =	0l;
	protected	Integer				karteCount                  =	0;
	protected	Integer				proportionallyCount         =	0;
	protected	Integer				proportionallyInputCount    =	0;

       /**
	 * ���W�X�^�b�t�f�[�^
	 */
	protected	MstStaff			staff			=	new MstStaff();
	/**
	 * ��S���҃f�[�^
	 */
	protected	MstStaff			chargeStaff		=	new MstStaff();
        
	/**
	 * ��S���w���t���O
	 */
        protected       Boolean                         designatedFlag         =       null;
        
	/**
	 * �x�����z
	 */
	protected	ArrayList<Long>		paymentValue	=	new ArrayList<Long>();

	/**
	 * �폜��
	 */
	protected	java.util.Date	deleteDate                              =	null;
        
	/**
	 * �R���X�g���N�^
	 */
	public AccountData()
	{
	}

	/**
	 * �`�[�w�b�_�f�[�^���擾����B
	 * @return �`�[�w�b�_�f�[�^
	 */
	public DataSales getSales()
	{
		return sales;
	}

	/**
	 * �`�[�w�b�_�f�[�^���Z�b�g����B
	 * @param sales �`�[�w�b�_�f�[�^
	 */
	public void setSales(DataSales sales)
	{
		this.sales = sales;
	}

	public Long getTotalValue()
	{
		return totalValue;
	}

	public Long getBillValue()
	{
		return billValue;
	}

	public void setBillValue(Long billValue)
	{
		this.billValue = billValue;
	}

	/**
	 * ���W�X�^�b�t�f�[�^���擾����B
	 * @return �X�^�b�t�f�[�^
	 */
	public MstStaff getStaff()
	{
		return staff;
	}

	/**
	 * ���W�X�^�b�t�f�[�^���Z�b�g����B
	 * @param staff �X�^�b�t�f�[�^
	 */
	public void setStaff(MstStaff staff)
	{
		this.staff = staff;
	}

	/**
	 * ��S���҃f�[�^���擾����B
	 * @return �X�^�b�t�f�[�^
	 */
	public MstStaff getChargeStaff()
	{
		return chargeStaff;
	}

	/**
	 * ��S���҃f�[�^���Z�b�g����B
	 * @param chargeStaff �X�^�b�t�f�[�^
	 */
	public void setChargeStaff(MstStaff chargeStaff)
	{
		this.chargeStaff = chargeStaff;
	}

	/**
	 * ��S���w���t���O���擾����B
	 * @return ��S���w���t���O
	 */
	public Boolean getDesignatedFlag()
	{
		return designatedFlag;
	}

	/**
	 * ��S���w���t���O���Z�b�g����B
	 * @param designatedFlag ��S���w���t���O
	 */
	public void setDesignatedFlag(Boolean designatedFlag)
	{
		this.designatedFlag = designatedFlag;
	}
        
	/**
	 * �x�����z���擾����B
	 * @return �x�����z
	 */
	public ArrayList<Long> getPaymentValue()
	{
		return paymentValue;
	}

	/**
	 * �x�����v���擾����B
	 * @return �x�����v
	 */
	public Long getPaymentTotalAll()
	{
		return paymentTotal;
	}

	/**
	 * �����x�����擾����B
	 * @return �����x��
	 */
	public Long getPaymentValue1()
	{
	    Long result = 0l;
	    
	    if (paymentValue1 != 0l) {
                //IVS_TMTrong start edit 20150716 Bug #37392
                if(totalValue < 0){
                    result = paymentValue1;
                }else{
                            if (totalValue < getPaymentValue2()) {
                                result = totalValue;
                            } else {
                                Long tmpValue = totalValue - getPaymentValue2();
                                if (tmpValue < paymentValue1) {
                                    result = tmpValue;
                                } else {
                                    result = paymentValue1;
                                }
                            }
                }
                //IVS_TMTrong end edit 20150716 Bug #37392
	    }
	    
	    return result;
	}
	/**
	 * �J�[�h�x�����擾����B
	 * @return �J�[�h�x��
	 */
	public Long getPaymentValue2()
	{
	    Long result = 0l;

	    if (paymentValue2 != 0l) {

                //IVS_TMTrong start edit 20150716 Bug #37392
                if(totalValue < 0){
                    result = paymentValue2;
                } else{
                    if (totalValue < paymentValue2) {
                        result = totalValue;
                    } else {
                        result = paymentValue2;
                    }
                }
                //IVS_TMTrong end edit 20150716 Bug #37392
	    }

	    return result;
	}

	/**
	 * �����x�����v���擾����B
	 * @return �����x�����v
	 */
	public Long getPaymentValue1Total()
	{
		return paymentValue1Total;
	}
	/**
	 * �J�[�h�x�����v���擾����B
	 * @return �J�[�h�x�����v
	 */
	public Long getPaymentValue2Total()
	{
		return paymentValue2Total;
	}

        /**
	 * �������t���O���擾����B
	 * @return �������t���O
	 */
	public Integer getProportionallyCount()
	{
		return proportionallyCount;
	}

        /**
	 * ���o�^�σt���O���擾����B
	 * @return ���o�^�σt���O
	 */
	public Integer getProportionallyInputCount()
	{
		return proportionallyInputCount;
	}
        
	/**
	 * �J���e�o�^�t���O���擾����B
	 * @return �J���e�o�^�t���O
	 */
	public Integer getKarteCount()
	{
		return karteCount;
	}
        
	/**
	 * ���|���v���擾����B
	 * @return ���|���v
	 */
	public Long getBillTotal()
	{
		return billTotal;
	}

	/**
	 * �������v���擾����B
	 * @return �������v
	 */
	public Long getTotal()
	{
		return total;
	}
        
	/**
	 * �폜�����擾����B
	 * @return �폜��
	 */
	public java.util.Date getDeleteDate()
	{
		return deleteDate;
	}

	/**
	 * �폜�����Z�b�g����B
	 * @param deleteDate �폜��
	 */
	public void setDeleteDate(java.util.Date deleteDate)
	{
		this.deleteDate = deleteDate;
	}
        
        public void addValue(AccountData ad, Long temp)
	{
		total			+=	ad.getTotalValue();
		billTotal		+=	ad.getBillValue();
		paymentTotal		+=	temp;

		paymentValue1Total	+=	ad.getPaymentValue1();
		paymentValue2Total	+=	ad.getPaymentValue2();
	}

        /**
	 * �x�����z���Z�b�g����B
	 * @param paymentValue �x�����z
	 */
	public void setPaymentValue(ArrayList<Long> paymentValue)
	{
		this.paymentValue = paymentValue;
	}
	
	public Long getPaymentTotal()
	{
		Long	temp	=	0l;
		
		for(Long pv : paymentValue)
		{
			temp	+=	pv;
		}
		
		if(totalValue < temp)
		{
			temp	=	totalValue;
		}
		
		return	temp;
	}
	
        public Integer getReservationNo() {
            return reservationNo;
        }

        public void setReservationNo(Integer reservationNo) {
            this.reservationNo = reservationNo;
        }
        
	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����B
	 * @param rs ResultSetWrapper
	 * @param paymentClasses �x���敪���X�g�f�[�^
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs, MstPaymentClasses paymentClasses) throws SQLException
	{
		sales.getShop().setShopID(rs.getInt("shop_id"));
		sales.setSalesDate(rs.getDate("sales_date"));
		sales.setSlipNo(rs.getInt("slip_no"));
		sales.getCustomer().setCustomerID(rs.getInt("customer_id"));
		sales.getCustomer().setCustomerNo(rs.getString("customer_no"));
		sales.getCustomer().setCustomerName(0, rs.getString("customer_name1"));
		sales.getCustomer().setCustomerName(1, rs.getString("customer_name2"));
		totalValue	=	rs.getLong("total_value") - rs.getLong("alldiscount");
		billValue	=	rs.getLong("bill_value");
		staff.setStaffID(rs.getInt("staff_id"));
		staff.setStaffNo(rs.getString("staff_no"));
		staff.setStaffName(0, rs.getString("staff_name1"));
		staff.setStaffName(1, rs.getString("staff_name2"));
		setDesignatedFlag(rs.getBoolean("designated_flag"));
                
		chargeStaff.setStaffID(rs.getInt("chargeStaff_id"));
		chargeStaff.setStaffNo(rs.getString("chargeStaff_no"));
		chargeStaff.setStaffName(0, rs.getString("chargeStaff_name1"));
		chargeStaff.setStaffName(1, rs.getString("chargeStaff_name2"));

                // ����
		this.paymentValue1 = rs.getLong("payment_value1");
                
                // �����ȊO
		this.paymentValue2 = rs.getLong("payment_value2") + rs.getLong("payment_value3") + rs.getLong("payment_value4");

		this.proportionallyCount = rs.getInt("proportionally_count");
		this.proportionallyInputCount = rs.getInt("proportionally_input_count");
		this.karteCount = rs.getInt("karte_count");
                
		for(MstPaymentClass mpc : paymentClasses)
		{
			paymentValue.add(rs.getLong("payment_value" +
					mpc.getPaymentClassID().toString()));
		}
                
		this.setDeleteDate(rs.getDate("delete_date"));
                
                this.setReservationNo(rs.getInt("reservation_no"));
	}
}
