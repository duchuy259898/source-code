/*
 * DataSales.java
 *
 * Created on 2006/05/09, 9:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.data.account;


import com.geobeck.sosia.pos.master.account.MstDiscount;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.master.product.MstProduct;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.*;

/**
 * �`�[�w�b�_�f�[�^
 * @author katagiri
 */
public class DataSales extends ArrayList<DataSalesDetail>
{
	private	Integer			type		=	0;
	/**
	 * �X��
	 */
	protected	MstShop			shop		=	new MstShop();
	/**
	 * �`�[No.
	 */
	protected	Integer			slipNo		=	null;
	/**
	 * �����
	 */
	protected	java.util.Date	salesDate	=	null;
	/**
	 * �ڋq
	 */
	protected	MstCustomer		customer	=	new MstCustomer();
	/**
	 * ���X��
	 */
	protected	Integer			visitNum	=	null;
	
	/**
	 * �����f�[�^
	 */
	protected	ArrayList<DataSalesDetail>		discounts	=	new ArrayList<DataSalesDetail>();
	/**
	 * �x���f�[�^
	 */
	protected	HashMap<Integer, DataPayment>	payments	=	new HashMap<Integer, DataPayment>();
	
	/** Creates a new instance of DataSales */
	public DataSales(Integer type)
	{
		this.setType(type);
	}

	public Integer getType()
	{
		return type;
	}

	public void setType(Integer type)
	{
		this.type = type;
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
	 * ��������擾����B
	 * @return �����
	 */
	public java.util.Date getSalesDate()
	{
		return salesDate;
	}

	/**
	 * ��������Z�b�g����B
	 * @param salesDate �����
	 */
	public void setSalesDate(java.util.Date salesDate)
	{
		this.salesDate = salesDate;
	}

	/**
	 * �ڋq���擾����B
	 * @return �ڋq
	 */
	public MstCustomer getCustomer()
	{
		return customer;
	}

	/**
	 * �ڋq���Z�b�g����B
	 * @param customer �ڋq
	 */
	public void setCustomer(MstCustomer customer)
	{
		this.customer = customer;
	}

	/**
	 * ���X�񐔂��擾����B
	 * @return ���X��
	 */
	public Integer getVisitNum()
	{
		return visitNum;
	}

	/**
	 * ���X�񐔂��Z�b�g����B
	 * @param visitNum ���X��
	 */
	public void setVisitNum(Integer visitNum)
	{
		this.visitNum = visitNum;
	}

	/**
	 * �����f�[�^���擾����B
	 * @return �����f�[�^
	 */
	public ArrayList<DataSalesDetail> getDiscounts()
	{
		return discounts;
	}

	/**
	 * �����f�[�^���Z�b�g����B
	 * @param discounts �����f�[�^
	 */
	public void setDiscounts(ArrayList<DataSalesDetail> discounts)
	{
		this.discounts = discounts;
	}

	/**
	 * �x���f�[�^���擾����B
	 * @return �x���f�[�^
	 */
	public HashMap<Integer, DataPayment> getPayments()
	{
		return payments;
	}

	/**
	 * �x���f�[�^���Z�b�g����B
	 * @param payments �x���f�[�^
	 */
	public void setPayments(HashMap<Integer, DataPayment> payments)
	{
		this.payments = payments;
	}

	/**
	 * �x���f�[�^���擾����B
	 * @param paymentNo �x��No.
	 * @return �x���f�[�^
	 */
	public DataPayment getPayment(int paymentNo) throws Exception
	{
		return payments.get(paymentNo);
	}
	
	
	/**
	 * �����f�[�^��ǉ�����B
	 * @param md �����}�X�^�f�[�^
	 * @param value �������z
	 */
	public void addDiscount(MstDiscount md, Long value)
	{
		DataSalesDetail	dsd	=	new DataSalesDetail();
		
		dsd.setShop(this.getShop());
		dsd.setSlipNo(this.getSlipNo());
		dsd.setProductDivision(0);
		MstProduct	mp	=	new MstProduct();
		mp.setProductID(md.getDiscountID());
		mp.setProductName(md.getDiscountName());
		dsd.setProduct(mp);
		dsd.setProductValue(0l);
		dsd.setDiscountValue(value);
		discounts.add(dsd);
	}
	
	
	/**
	 * �x�����׃f�[�^��ǉ�����B
	 * @param paymentNo �x��No.
	 * @param payment �x�����׃f�[�^
	 */
	public void addPayment(int paymentNo, DataPayment payment)
	{
		payment.setShop(this.getShop());
		payment.setSlipNo(this.slipNo);
		payment.setPaymentNo(paymentNo);
		payments.put(paymentNo, payment);
	}
	
	/**
	 * �x���f�[�^�̐����擾����B
	 * @return �x���f�[�^�̐�
	 */
	public int paymentSize()
	{
		return	this.payments.size();
	}
	
	/**
	 * ���z�̍��v���擾����B
	 * @return ������z�̍��v
	 */
	public long getValueTotal()
	{
		long	total	=	0;
		
		for(DataSalesDetail dsd : this)
		{
			total	+=	dsd.getProductValue() * dsd.getProductNum();
		}
		
		return	total;
	}
	
	/**
	 * ������z�̍��v���擾����B
	 * @return ������z�̍��v
	 */
	public long getSalesTotal()
	{
		long	total	=	0;
		
		for(DataSalesDetail dsd : this)
		{
			if(dsd.getProductValue() != null &&
					dsd.getProductNum() != null)
			{
				total	+=	dsd.getProductValue() * dsd.getProductNum();
			}
		}
		
		return	total;
	}
	
	/**
	 * �������z�̍��v���擾����B
	 * @return �������z�̍��v
	 */
	public long getDiscountTotal()
	{
		long	total	=	0;
		
		for(DataSalesDetail dsd : this)
		{
			if(dsd.getDiscountValue() != null)
			{
				total	+=	dsd.getDiscountValue();
			}
		}
		
		for(DataSalesDetail dsd : discounts)
		{
			if(dsd.getDiscountValue() != null)
			{
				total	+=	dsd.getDiscountValue();
			}
		}
		
		return	total;
	}
	
	/**
	 * �ŋ��̍��v���擾����B
	 * @return �ŋ��̍��v
	 */
	public long getTaxTotal(Double taxRate, Integer rounding)
	{
		long	total	=	0;
		
		for(DataSalesDetail dsd : this)
		{
			total	+=	dsd.getTax(taxRate, rounding);
		}
		
		return	total;
	}
	
	/**
	 * ResultSetWrapper����f�[�^���Z�b�g����B
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		MstShop	ms	=	new MstShop();
		ms.setShopID(rs.getInt("shop_id"));
		this.setShop(ms);
		this.setSalesDate(rs.getDate("sales_date"));
		MstCustomer	mc	=	new MstCustomer();
		mc.setCustomerID(rs.getInt("customer_id"));
		mc.setCustomerName(0, rs.getString("customer_name1"));
		mc.setCustomerName(1, rs.getString("customer_name2"));
		this.setCustomer(mc);
		if(type == 1)
				this.setVisitNum(rs.getInt("visit_num"));
	}
	
	
	/**
	 * �V�����`�[No.���Z�b�g����B
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setNewSlipNo(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getNewSlipNoSQL());
		
		if(rs.next())
		{
			this.setSlipNo(rs.getInt("new_slip_no"));
		}
		
		rs.close();
	}
	
	/**
	 * �`�[�w�b�_�f�[�^�A�`�[���׃f�[�^�A�x���f�[�^�A�x�����׃f�[�^��o�^����B
	 * @param con ConnectionWrapper
	 * @param paymentNo �x��No.
	 * @return true - ����
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean registAll(ConnectionWrapper con, int paymentNo) throws SQLException
	{
		if(!this.deleteAllChildren(con))
				return	false;
		
		if(!this.regist(con))	return	false;
		
		//���㖾�׃e�[�u��
		for(DataSalesDetail dsd : this)
		{
			dsd.setShop(this.getShop());
			dsd.setSlipNo(this.getSlipNo());
			dsd.setNewSlipDetailNo(con);
			if(!dsd.regist(con))	return	false;
		}
		
		for(DataSalesDetail dsd : discounts)
		{
			if(dsd.getProduct().getProductID().equals("") ||
					dsd.getDiscountValue() == 0)	continue;
			
			dsd.setShop(this.getShop());
			dsd.setSlipNo(this.getSlipNo());
			dsd.setNewSlipDetailNo(con);
			if(!dsd.regist(con))	return	false;
		}
		
		DataPayment dp	=	this.getPayments().get(paymentNo);
		
		dp.setShop(this.getShop());
		dp.setSlipNo(this.getSlipNo());
		dp.registAll(con);
		
		return	true;
	}
	
	
	/**
	 * �o�^�������s���B
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
	 * �`�[�w�b�_�f�[�^�����݂��邩���擾����B
	 * @param con ConnectionWrapper
	 * @return true - ���݂���
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getSlipNo() == null || this.getSlipNo() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 * �`�[�w�b�_�f�[�^�A�`�[���׃f�[�^�A�x���f�[�^�A�x�����׃f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @param paymentNo �x��No.
	 * @return true - ����
	 * @throws java.lang.Exception Exception
	 */
	public boolean loadAll(ConnectionWrapper con, Integer paymentNo) throws Exception
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectAllSQL());
		
		if(rs.next())
		{
			this.setData(rs);
			
			rs.beforeFirst();
			
			while(rs.next())
			{
				DataSalesDetail	dsd	=	new DataSalesDetail();
				dsd.setData(rs);
				this.add(dsd);
			}
		}
		
		rs.close();
		
		this.loadDiscount(con);
		
		this.addPayment(0, new DataPayment());
		
		this.getPayment(0).loadAll(con);
		
		return	true;
	}
	
	/**
	 * �����f�[�^��ǂݍ��ށB
	 * @param con ConnectionWrapper
	 * @return true - ����
	 * @throws java.sql.SQLException SQLException
	 */
	private boolean loadDiscount(ConnectionWrapper con) throws SQLException
	{
		discounts.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectDiscountSQL());
		
		while(rs.next())
		{
			DataSalesDetail	dsd	=	new DataSalesDetail();
			dsd.setDiscountData(rs);
			discounts.add(dsd);
		}
		
		rs.close();
		
		return	true;
	}
	
	
	/**
	 * ������̔��|�������݂��邩���擾����B
	 * @param con ConnectionWrapper
	 * @param slipNo �`�[No.
	 * @return true - ���݂���
	 * @throws java.sql.SQLException SQLException
	 */
	public static boolean isExistCollectedBill(ConnectionWrapper con,
			Integer shopID, Integer slipNo) throws SQLException
	{
		boolean	result	=	false;
		
		if(slipNo == null || slipNo < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(DataSales.getExistCollectedBillSQL(shopID, slipNo));

		if(rs.next())	result	=	(0 < rs.getInt("cnt"));
		
		rs.close();
		
		return	result;
	}
	
	
	/**
	 * ������̔��|�������݂��邩���擾����B
	 * @param con ConnectionWrapper
	 * @return true - ���݂���
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean isExistCollectedBill(ConnectionWrapper con) throws SQLException
	{
		boolean	result	=	false;
		
		if(this.getSlipNo() == null || this.getSlipNo() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	result	=	(0 < rs.getInt("cnt"));
		
		rs.close();
		
		return	result;
	}
	
	/**
	 * �`�[�w�b�_�f�[�^��_���폜����B
	 * @param con ConnectionWrapper
	 * @return true - ����
	 */
	public boolean delete(ConnectionWrapper con) throws SQLException
	{
		boolean	result	=	false;
		
		result	=	(con.executeUpdate(this.getDeleteSQL()) == 1);
		
		return	result;
	}
	
	/**
	 * �`�[���׃f�[�^�A�x���f�[�^�A�x�����׃f�[�^��_���폜����B
	 * @param con ConnectionWrapper
	 * @return true - ����
	 */
	public boolean deleteAllChildren(ConnectionWrapper con) throws SQLException
	{
		if(!this.deleteDetail(con))
				return	false;
		
		if(!this.deletePayment(con))
				return	false;
		
		if(!this.deletePaymentDetail(con))
				return	false;
		
		return	true;
	}
	
	/**
	 * ����w�b�_�f�[�^�A�`�[���׃f�[�^�A�x���f�[�^�A�x�����׃f�[�^��_���폜����B
	 * @param con ConnectionWrapper
	 * @return true - ����
	 */
	public boolean deleteAll(ConnectionWrapper con) throws SQLException
	{
		//���㖾�ׁA�x���A�x������
		if(!this.deleteAllChildren(con))
				return	false;
		//����w�b�_
		if(!this.delete(con))
				return	false;
		
		return	true;
	}
	
	
	/**
	 * �`�[���׃f�[�^��_���폜����B
	 * @param con ConnectionWrapper
	 * @return true - ����
	 */
	public boolean deleteDetail(ConnectionWrapper con) throws SQLException
	{
		con.execute(this.getDeleteDetailSQL());
		
		return	true;
	}
	
	
	/**
	 * �x���f�[�^��_���폜����B
	 * @param con ConnectionWrapper
	 * @return true - ����
	 */
	public boolean deletePayment(ConnectionWrapper con) throws SQLException
	{
		con.execute(this.getDeletePaymentSQL());
		
		return	true;
	}
	
	
	/**
	 * �x�����׃f�[�^��_���폜����B
	 * @param con ConnectionWrapper
	 * @return true - ����
	 */
	public boolean deletePaymentDetail(ConnectionWrapper con) throws SQLException
	{
		con.execute(this.getDeletePaymentDetailSQL());
		
		return	true;
	}
	
	
	/**
	 * �V�����`�[No.���擾����r�p�k�����擾����B
	 * @return �V�����`�[No.���擾����r�p�k��
	 */
	public String getNewSlipNoSQL()
	{
		return	"select coalesce(max(slip_no), 0) + 1 as new_slip_no\n" +
				"from data_sales\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n";
	}
	
	
	/**
	 * �`�[�w�b�_�f�[�^���擾����r�p�k�����擾����B
	 * @return �`�[�w�b�_�f�[�^���擾����r�p�k��
	 */
	public String getSelectSQL()
	{
		return	"select ds.*,\n" +
				"mc.customer_name1,\n" +
				"mc.customer_name2\n" +
				"from data_sales ds\n" +
				"left outer join mst_customer mc\n" +
				"on mc.customer_id = ds.customer_id\n" +
				"where ds.delete_date is null\n" +
				"and ds.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and ds.slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
	}
	
	/**
	 * �`�[�w�b�_�f�[�^��Insert����r�p�k�����擾����B
	 * @return �`�[�w�b�_�f�[�^��Insert����r�p�k��
	 */
	public String getInsertSQL()
	{
		return	"insert into data_sales\n" +
				"(shop_id, slip_no, sales_date, customer_id,\n" +
				(type == 1 ? "visit_num,\n" : "") +
				"insert_date, update_date, delete_date)\n" +
				"values(\n" +
				SQLUtil.convertForSQL(this.getShop().getShopID()) + ",\n" +
				SQLUtil.convertForSQL(this.getSlipNo()) + ",\n" +
				SQLUtil.convertForSQLDateOnly(this.getSalesDate()) + ",\n" +
				SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + ",\n" +
				(type == 1 ? "(select count(*) + 1\n" +
						"from data_sales\n" +
						"where delete_date is null\n" +
						"and sales_date < " + SQLUtil.convertForSQL(this.getSalesDate()) + "\n" +
						"and customer_id = " + SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + ")\n" : "") +
				"current_timestamp, current_timestamp, null)\n";
	}
	
	/**
	 * �`�[�w�b�_�f�[�^��Update����r�p�k�����擾����B
	 * @return �`�[�w�b�_�f�[�^��Update����r�p�k��
	 */
	public String getUpdateSQL()
	{
		return	"update data_sales\n" +
				"set\n" +
				"sales_date = " + SQLUtil.convertForSQLDateOnly(this.getSalesDate()) + ",\n" +
				"customer_id = " + SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + ",\n" +
				(type == 1 ? "visit_num = (select count(*) + 1" +
						"from data_sales" +
						"where delete_date is null\n" +
						"and sales_date < " + SQLUtil.convertForSQL(this.getSalesDate()) + "\n" +
						"and customer_id = " + SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + ")\n" : "") +
				"update_date = current_timestamp\n" +
				"and ds.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and ds.slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
	}
	
	
	/**
	 * ����w�b�_�f�[�^�A�`�[���׃f�[�^���擾����r�p�k�����擾����B
	 * @return ����w�b�_�f�[�^�A�`�[���׃f�[�^���擾����r�p�k��
	 */
	public String getSelectAllSQL()
	{
		return	"select ds.shop_id,\n" +
				"ds.slip_no,\n" +
				"ds.sales_date,\n" +
				"ds.customer_id,\n" +
				"mc.customer_name1,\n" +
				"mc.customer_name2,\n" +
				(type == 1 ? "visit_num,\n" : "") +
				"dsd.slip_detail_no,\n" +
				"mp.product_class_id,\n" +
				"mpc.product_class_name,\n" +
				"dsd.product_division,\n" +
				"dsd.product_id,\n" +
				"mp.product_name,\n" +
				"dsd.product_num,\n" +
				"dsd.product_value,\n" +
				"dsd.discount_value,\n" +
				"dsd.staff_id,\n" +
				"ms.staff_name1,\n" +
				"ms.staff_name2\n" +
				"from data_sales ds\n" +
				"left outer join mst_customer mc\n" +
				"on mc.customer_id = ds.customer_id\n" +
				"left outer join data_sales_detail dsd\n" +
				"on dsd.shop_id = ds.shop_id\n" +
				"and dsd.slip_no = ds.slip_no\n" +
				"and dsd.product_division != 0\n" +
				"and dsd.delete_date is null\n" +
				"left outer join mst_product mp\n" +
				"on mp.product_id = dsd.product_id\n" +
				"left outer join mst_product_class mpc\n" +
				"on mpc.product_class_id = mp.product_class_id\n" +
				"left outer join mst_staff ms\n" +
				"on ms.staff_id = dsd.staff_id\n" +
				"where ds.delete_date is null\n" +
				"and ds.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and ds.slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
	}
	
	/**
	 * �����f�[�^���擾����r�p�k�����擾����B
	 * @return �����f�[�^���擾����r�p�k��
	 */
	public String getSelectDiscountSQL()
	{
		return	"select dsd.slip_no,\n" +
				"dsd.slip_detail_no,\n" +
				"dsd.product_division,\n" +
				"dsd.product_id,\n" +
				"md.discount_name,\n" +
				"dsd.product_value,\n" +
				"dsd.discount_value,\n" +
				"dsd.staff_id,\n" +
				"ms.staff_name1,\n" +
				"ms.staff_name2\n" +
				"from data_sales_detail dsd\n" +
				"left outer join mst_discount md\n" +
				"on md.discount_id = dsd.product_id\n" +
				"left outer join mst_staff ms\n" +
				"on ms.staff_id = dsd.staff_id\n" +
				"where dsd.delete_date is null\n" +
				"and dsd.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and dsd.slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n" +
				"and dsd.product_division = 0\n";
	}
	
	/**
	 * ������̔��|�������݂��邩���擾����r�p�k�����擾����B
	 * @param slipNo �`�[No.
	 * @return ������̔��|�������݂��邩���擾����r�p�k��
	 */
	public static String getExistCollectedBillSQL(Integer shopID, Integer slipNo)
	{
		return	"select count(*) as cnt\n" +
				"from data_sales ds\n" +
				"inner join data_payment dp\n" +
				"on dp.shop_id = ds.shop_id\n" +
				"and dp.slip_no = ds.slip_no\n" +
				"and dp.payment_no > 0\n" +
				"and dp.delete_date is null\n" +
				"where ds.delete_date is null\n" +
				"and ds.shop_id = " + SQLUtil.convertForSQL(shopID) + "\n" +
				"and ds.slip_no = " + SQLUtil.convertForSQL(slipNo) + "\n";
	}
	
	/**
	 * ����w�b�_�f�[�^���폜����r�p�k�����擾����B
	 * @return ����w�b�_�f�[�^���폜����r�p�k��
	 */
	public String getDeleteSQL()
	{
		return	"update data_sales\n" +
				"set delete_date = current_timestamp\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
	}
	
	/**
	 * �`�[���׃f�[�^��_���폜����r�p�k�����擾����B
	 * @return �`�[���׃f�[�^��_���폜����r�p�k��
	 */
	public String getDeleteDetailSQL()
	{
		return	"update data_sales_detail\n" +
				"set delete_date = current_timestamp\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
	}
	
	/**
	 * �x���f�[�^��_���폜����r�p�k�����擾����B
	 * @return �x���f�[�^��_���폜����r�p�k��
	 */
	public String getDeletePaymentSQL()
	{
		return	"update data_payment\n" +
				"set delete_date = current_timestamp\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n" +
				"and payment_no >= 0\n";
	}
	
	/**
	 * �x�����׃f�[�^��_���폜����r�p�k�����擾����B
	 * @return �x�����׃f�[�^��_���폜����r�p�k��
	 */
	public String getDeletePaymentDetailSQL()
	{
		return	"update data_payment_detail\n" +
				"set delete_date = current_timestamp\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
	}
}
