/*
 * DataPayment.java
 *
 * Created on 2006/05/09, 9:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.data.account;

import com.geobeck.sosia.pos.master.company.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.*;
import com.geobeck.sosia.pos.master.account.*;

/**
 * �x���f�[�^
 * @author katagiri
 */
public class DataPayment extends ArrayList<DataPaymentDetail>
{
	/**
	 * �X��
	 */
	protected	MstShop			shop		=	new MstShop();
	/**
	 * �`�[No.
	 */
	protected	Integer			slipNo			=	null;
	/**
	 * �x��No.
	 */
	protected	Integer			paymentNo		=	null;
	/**
	 * �x����
	 */
	protected	java.util.Date	paymentDate		=	null;
	/**
	 * �X�^�b�t
	 */
	protected	MstStaff    staff   =	  new MstStaff();
	/**
	 * ���|���z
	 */
	protected	Long	    billValue	=   0l;
	/**
	 * ���ނ�
	 */
	protected	Long	    changeValue	=   0l;
        /**
	 * �ꎞ�ۑ��p�t���O�@
	 */
        protected	boolean	    tempFlag	=   false;

	
	/**
	 * �R���X�g���N�^
	 */
	public DataPayment()
	{
	}

	/**
	 * �X�܂��擾����B
	 * @return �X��
	 */
	public MstShop getShop()
	{
		return shop;
	}

	/**
	 * �X�܂��Z�b�g����B
	 * @param shop �X��
	 */
	public void setShop(MstShop shop)
	{
		this.shop = shop;
	}

	/**
	 * �`�[No.���擾����B
	 * @return �`�[No.
	 */
	public Integer getSlipNo()
	{
		return slipNo;
	}

	/**
	 * �`�[No.���Z�b�g����B
	 * @param slipNo �`�[No.
	 */
	public void setSlipNo(Integer slipNo)
	{
		this.slipNo = slipNo;
	}

	/**
	 * �x��No.���擾����B
	 * @return �x��No.
	 */
	public Integer getPaymentNo()
	{
		return paymentNo;
	}

	/**
	 * �x��No.���Z�b�g����B
	 * @param paymentNo �x��No.
	 */
	public void setPaymentNo(Integer paymentNo)
	{
		this.paymentNo = paymentNo;
	}

	/**
	 * �x�������擾����B
	 * @return �x����
	 */
	public java.util.Date getPaymentDate()
	{
		return paymentDate;
	}

	/**
	 * �x�������Z�b�g����B
	 * @param paymentDate �x����
	 */
	public void setPaymentDate(java.util.Date paymentDate)
	{
		this.paymentDate = paymentDate;
	}

	/**
	 * �X�^�b�t���擾����B
	 * @return �X�^�b�t
	 */
	public MstStaff getStaff()
	{
		return staff;
	}

	/**
	 * �X�^�b�t���Z�b�g����B
	 * @param staff �X�^�b�t
	 */
	public void setStaff(MstStaff staff)
	{
		this.staff = staff;
	}

	/**
	 * ���|���z���擾����B
	 * @return ���|���z
	 */
	public Long getBillValue()
	{
		return billValue;
	}

	/**
	 * ���|���z���Z�b�g����B
	 * @param billValue ���|���z
	 */
	public void setBillValue(Long billValue)
	{
		this.billValue = billValue;
	}

	public Long getChangeValue()
	{
		return changeValue;
	}

	public void setChangeValue(Long changeValue)
	{
		this.changeValue = changeValue;
	}

        /**
	 * �ꎞ�ۑ��p�t���O���Z�b�g����B
	 * @param�@�ꎞ�ۑ��p�t���O
	 */
        public boolean getTempFlag()
        {
                return tempFlag ;
        }
        /**
	 * �ꎞ�ۑ��p�t���O���擾����B
	 * @param�@�ꎞ�ۑ��p�t���O�@
         * @return �ꎞ�ۑ��p�t���O
         */        
        public void setTempFlag(boolean tempFlag)
        {
                this.tempFlag=tempFlag;
        }	

	
	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����B
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.getShop().setShopID(rs.getInt("shop_id"));
		this.setSlipNo(rs.getInt("slip_no"));
		this.setPaymentNo(rs.getInt("payment_no"));
		this.setPaymentDate(rs.getDate("payment_date"));
		MstStaff	ms	=	new MstStaff();
		ms.setStaffID(rs.getInt("staff_id"));
		ms.setStaffName(0, rs.getString("staff_name1"));
		ms.setStaffName(1, rs.getString("staff_name2"));
		this.setStaff(ms);
		this.setBillValue(rs.getLong("bill_value"));
		this.setChangeValue(rs.getLong("change_value"));
	}
	
	/**
	 * �x�����ׂ�ǉ�����B
	 * @param mpc �x���敪
	 * @param mpm �x�����@
	 * @param value ���z
	 */
	public void addPaymentDetail(
			MstPaymentClass mpc,
			MstPaymentMethod mpm,
			Long value)
	{
		DataPaymentDetail dpd = new DataPaymentDetail();

		dpd.setPaymentMethod(mpm);

                if(mpc != null && dpd.getPaymentMethod() != null)
				dpd.getPaymentMethod().setPaymentClass(mpc);
		dpd.setPaymentValue(value);
		
		this.add(dpd);
	}
	
	/**
	 * �x�����ׂ��Z�b�g����B
	 * @param index �C���f�b�N�X
	 * @param mpc �x���敪
	 * @param mpm �x�����@
	 * @param value �x�����z
	 */
	public void setPaymentDetail(int index,
			MstPaymentClass mpc,
			MstPaymentMethod mpm,
			Long value)
	{
		if(0 <= index && index < this.size())
		{
                    DataPaymentDetail dpd = this.get(index);

                    dpd.setPaymentMethod(mpm);
                    if(mpc != null && dpd.getPaymentMethod() != null) {
                        dpd.getPaymentMethod().setPaymentClass(mpc);
                    }
                    dpd.setPaymentValue(value);
		}
	}
	
	/**
	 * �x�����z�̍��v���擾����B
	 * @return �x�����z�̍��v
	 */
	public long getPaymentTotal()
	{
            Long total = 0l;

            //IVS_LVTu start edit 2016/06/21 Bug #51065
            for (DataPaymentDetail dpd : this) {
                //if(dpd.getPaymentMethod() != null){
                if(dpd.getPaymentValue() != null)
                    total += dpd.getPaymentValue();
                //}        
            }
            //IVS_LVTu end edit 2016/06/21 Bug #51065

            return total;
	}
	
	/**
	 * �x�����z�̍��v���擾����B
	 * @param paymentClass �x���敪
	 * @return �x�����z�̍��v
	 */
	public long getPaymentTotal(int paymentClass)
	{
		long	total	=	0;
		
		for(DataPaymentDetail dpd : this)
		{
			if(dpd.getPaymentMethod() != null &&
					dpd.getPaymentMethod().getPaymentClassID() == paymentClass)
			{
				total	+=	dpd.getPaymentValue();
			}
		}
		
		return	total;
	}
	
	/**
	 * �x���f�[�^�ƁA���ׂ�S�Ď擾����B
	 * @param con ConnectionWrapper
	 * @return true - ����
	 */
	public boolean loadAll(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectAllSQL());

		if(rs.next())
		{
			this.setData(rs);

			rs.beforeFirst();

			while(rs.next())
			{
				DataPaymentDetail	dpd	=	new DataPaymentDetail();
				dpd.setData(rs);
                                
				this.add(dpd);
			}
		}

		rs.close();
		
		return	true;
	}
	
	
	/**
	 * �V�����x��No.���Z�b�g����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setNewPaymentNo(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getNewPaymentNoSQL());
		
		if(rs.next())
		{
			this.setPaymentNo(rs.getInt("new_payment_no"));
		}
		
		rs.close();
	}
	
	/**
	 * �x���f�[�^�ƁA���ׂ�S�ēo�^����B
	 * @param con ConnectionWrapper
	 * @return true - ����
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean registAll(ConnectionWrapper con) throws SQLException
	{
		if(!this.regist(con))	return	false;

		//������׃e�[�u��
                boolean flg = false;
		for(DataPaymentDetail dpd : this)
		{
                        //���z���Ȃ�
			if(dpd.getPaymentValue() == null ||
					dpd.getPaymentValue() == 0)	continue;
                        
                        //����payment_no�͂P���������o�^ - New request #35011
			if(dpd.paymentMethod.getPaymentClassID()==1 && flg) continue;
			
                        dpd.setShop(this.getShop());
			dpd.setSlipNo(this.getSlipNo());
			dpd.setPaymentNo(this.getPaymentNo());
			dpd.setNewSlipDetailNo(con);
			if(!dpd.regist(con))	return	false;			
                        dpd.setNewSlipDetailNo(con);
                        if(dpd.paymentMethod.getPaymentClassID()==1) {
                            flg = true;
                        }

		}
		
		return	true;
	}
	
	
	/**
	 * �x���f�[�^��o�^����B
	 * @param con ConnectionWrapper
	 * @return true - ����
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		String	sql		=	"";
		
		if(isExists(con))
		{
			sql	=	this.getUpdateSQL();
		}
		else
		{
			sql	=	this.getInsertSQL();
		}
		
		if(con.executeUpdate(sql) == 1)
		{
			return	true;
		}
		else
		{
			return	false;
		}
	}
	
	
	/**
	 * �x���f�[�^�����݂��邩���擾����B
	 * @param con ConnectionWrapper
	 * @return true - ���݂���
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getSlipNo() == null || this.getSlipNo() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())
		{
			return	true;
		}
		else
		{
			return	false;
		}
	}
	
	
	/**
	 * �V�����x��No.���擾����r�p�k�����擾����B
	 * @return �V�����x��No.���擾����r�p�k��
	 */
	public String getNewPaymentNoSQL()
	{
		return	"select coalesce(max(payment_no), 0) + 1 as new_payment_no\n" +
				"from data_payment\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
	}
	
	
	/**
	 * �x���f�[�^���擾����r�p�k�����擾����B
	 * @return �x���f�[�^���擾����r�p�k��
	 */
	public String getSelectSQL()
	{
		return	"select *\n" +
				"from data_payment\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n" +
				"and payment_no = " + SQLUtil.convertForSQL(this.getPaymentNo());
	}
	
	/**
	 * �x���f�[�^��Insert����r�p�k�����擾����B
	 * @return �x���f�[�^��Insert����r�p�k��
	 */
	public String getInsertSQL()
	{
		return	"insert into data_payment\n" +
				"(shop_id, slip_no, payment_no, payment_date,\n" +
				"staff_id, bill_value, change_value,\n" +
				"insert_date, update_date, delete_date)\n" +
				"values(\n" +
				SQLUtil.convertForSQL(this.getShop().getShopID()) + ",\n" +
				SQLUtil.convertForSQL(this.getSlipNo()) + ",\n" +
				SQLUtil.convertForSQL(this.getPaymentNo()) + ",\n" +
				SQLUtil.convertForSQLDateOnly(this.getPaymentDate()) + ",\n" +
				SQLUtil.convertForSQL(this.getStaff().getStaffID()) + ",\n" +
				(this.getTempFlag() ? "0" : 
                                        SQLUtil.convertForSQL(this.getBillValue(), "0")) + ",\n" +
				SQLUtil.convertForSQL(this.getChangeValue(), "0") + ",\n" +
				"current_timestamp, current_timestamp, null)\n";
	}
	
	/**
	 * �x���f�[�^��Update����r�p�k�����擾����B
	 * @return �x���f�[�^��Update����r�p�k��
	 */
	public String getUpdateSQL()
	{
		return	"update data_payment\n" +
				"set\n" +
				"payment_date = " + SQLUtil.convertForSQLDateOnly(this.getPaymentDate()) + ",\n" +
				"staff_id = " + SQLUtil.convertForSQL(this.getStaff().getStaffID()) + ",\n" +
				"bill_value = " +( this.getTempFlag() ? "0" :
                                        SQLUtil.convertForSQL(this.getBillValue(), "0")) + ",\n" +
				"change_value = " + SQLUtil.convertForSQL(this.getChangeValue(), "0") + ",\n" +
				"update_date = current_timestamp\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n" +
				"and payment_no = " + SQLUtil.convertForSQL(this.getPaymentNo());
	}
	
	
	/**
	 * �x���f�[�^��_���폜����r�p�k�����擾����B
	 * @return �x���f�[�^��_���폜����r�p�k��
	 */
	public String getSelectAllSQL()
	{
		return	"select dp.*,\n" +
				"ms.staff_name1,\n" +
				"ms.staff_name2,\n" +
				"dpd.payment_detail_no,\n" +
				"mpm.payment_class_id,\n" +
				"mpc.payment_class_name,\n" +
				"dpd.payment_method_id,\n" +
				"mpm.payment_method_name,\n" +
				"mpm.prepaid,\n" +
				"dpd.payment_value\n" +
				"from data_payment dp\n" +
				"left outer join mst_staff ms\n" +
				"on ms.staff_id = dp.staff_id\n" +
				"left outer join data_payment_detail dpd\n" +
				"on dpd.shop_id = dp.shop_id\n" +
				"and dpd.slip_no = dp.slip_no\n" +
				"and dpd.payment_no = dp.payment_no\n" +
				"and dpd.delete_date is null\n" +
				"left outer join mst_payment_method mpm\n" +
				"on mpm.payment_method_id = dpd.payment_method_id\n" +
				"left outer join mst_payment_class mpc\n" +
				"on mpc.payment_class_id = mpm.payment_class_id\n" +
				"where dp.delete_date is null\n" +
				"and dp.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and dp.slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n" +
				"and dp.payment_no = " + SQLUtil.convertForSQL(this.getPaymentNo()) + "\n";
	}

}
