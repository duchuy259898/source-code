/*
 * InputAccount.java
 *
 * Created on 2006/05/09, 12:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.account;

import com.geobeck.sosia.pos.data.account.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.product.MstProduct;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.sosia.pos.master.account.*;

/**
 * �`�[���͉�ʏ���
 * @author katagiri
 */
public class InputAccount
{
	/**
	 * �`�[�f�[�^
	 */
	protected	DataSales		sales			=	new	DataSales(
			SystemInfo.getTypeID());
	
	/**
	 * �����}�X�^
	 */
	protected	MstDiscounts	discounts		=	new MstDiscounts();
	
	/**
	 * ���v
	 */
	protected	ArrayList<NameValue>	total	=	new ArrayList<NameValue>();
	
	/**
	 * ���|���f�[�^
	 */
	protected	DataPayment		bill			=	new DataPayment();
	
	/**
	 * �X�^�b�t���X�g
	 */
	protected	MstStaffs		staffs			=	null;

	public MstStaffs getStaffs()
	{
		if(staffs == null)
		{
			staffs	=	new MstStaffs();
			
			staffs.setShopIDList(SystemInfo.getCurrentShop().getShopID().toString());
			
			try
			{
				staffs.load(SystemInfo.getConnection(), true);
			}
			catch(SQLException e)
			{
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
		return staffs;
	}

	public void setStaffs(MstStaffs staffs)
	{
		this.staffs = staffs;
	}
	
	
	/** Creates a new instance of InputAccount */
	public InputAccount()
	{
		for(int i = 0; i < 5; i ++)	total.add(new NameValue());
	}
	
	/**
	 * �������������s���B
	 */
	public void init()
	{
		sales.clear();
		sales.addPayment(0, new DataPayment());
		
		try
		{
			ConnectionWrapper con	=	SystemInfo.getConnection();
			
			discounts.load(con);
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e.getCause());
		}
		
		this.initTotal();
	}
	
	/**
	 * ���v������������B
	 */
	protected void initTotal()
	{
		total.clear();
		total.add(new NameValue("���i���v", 0l));
		total.add(new NameValue("�������v", 0l));
		total.add(new NameValue("�������z", 0l));
		total.add(new NameValue("�i���Łj", 0l));
		for(MstPaymentClass mpc : SystemInfo.getPaymentClasses())
		{
			total.add(new NameValue(mpc.getPaymentClassName() + "�x��", 0l));
		}
		total.add(new NameValue("���ނ�", 0l));
	}

	/**
	 * �����}�X�^���擾����B
	 * @return �����}�X�^
	 */
	public MstDiscounts getDiscounts()
	{
		return discounts;
	}

	/**
	 * �����}�X�^���Z�b�g����B
	 * @param discounts �����}�X�^
	 */
	public void setDiscounts(MstDiscounts discounts)
	{
		this.discounts = discounts;
	}

	/**
	 * �`�[�f�[�^���擾����B
	 * @return �`�[�f�[�^
	 */
	public DataSales getSales()
	{
		return sales;
	}

	/**
	 * �`�[�f�[�^���Z�b�g����B
	 * @param sales �`�[�f�[�^
	 */
	public void setSales(DataSales sales)
	{
		this.sales = sales;
	}
	
	/**
	 * �`�[�ڍ׃f�[�^��ǉ�����B
	 * @param mp �`�[�ڍ׃f�[�^
	 */
	public void addSalesDetail(MstProduct mp)
	{
		DataSalesDetail		dsd	=	new DataSalesDetail(mp);
		dsd.setProductDivision(1);
		dsd.setProductNum(1);
		sales.add(dsd);
	}
	
	/**
	 * ���v���Z�b�g����B
	 */
	public void setTotal()
	{
		this.getTotal(0).setValue(sales.getSalesTotal());
		this.getTotal(1).setValue(sales.getDiscountTotal());
		this.getTotal(2).setValue(sales.getDiscountTotal());
		this.getTotal(2).setValue(this.getTotal(0).getValue()
				- this.getTotal(1).getValue());
		long	temp	=	this.getTotal(2).getValue();
		
		this.getTotal(4).setValue(sales.getTaxTotal(
				SystemInfo.getTaxRate(this.getSales().getSalesDate()),
				SystemInfo.getCurrentShop().getRounding()));
		
		try
		{
			//���|�����Z�b�g
			if(temp < 0)
			{
				this.getSales().getPayment(0).setChangeValue(0l);
				this.getSales().getPayment(0).setBillValue(-temp);
			}
			else
			{
				this.getSales().getPayment(0).setChangeValue(temp);
				this.getSales().getPayment(0).setBillValue(0l);
			}
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	public long getPaymentTotal()
	{
		try
		{
			return	this.getSales().getPayment(0).getPaymentTotal();
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	0l;
	}
	
	/**
	 * ���v���擾����B
	 * @return ���v
	 */
	public ArrayList<NameValue> getTotal()
	{
		return	total;
	}
	
	/**
	 * ���v���擾����B
	 * @param index �C���f�b�N�X
	 * @return ���v
	 */
	public NameValue getTotal(int index)
	{
		if(0 <= index && index < total.size())	return	total.get(index);
		return	null;
	}
	
	/**
	 * �o�^�������s���B
	 * @return true - ����
	 */
	public boolean regist()
	{
		boolean		result	=	false;
		
		try
		{
			sales.getPayment(0).setPaymentDate(sales.getSalesDate());
			total.get(4).setValue(total.get(4).getValue()
					- total.get(total.size() - 1).getValue());
			
			if(0 < total.get(total.size() - 1).getValue() &&
					0 < sales.getPayment(0).get(0).getPaymentValue())
			{
				sales.getPayment(0).get(0).setPaymentValue(total.get(4).getValue());
			}
			
			//�R�l�N�V�������擾
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			try
			{
				//�g�����U�N�V�����J�n
				con.begin();
				
				if(sales.getSlipNo() == null)
				{
					sales.setNewSlipNo(con);
				}
				
				//����No���擾
				if(sales.registAll(con, 0))
				{
					//�g�����U�N�V�����R�~�b�g
					con.commit();
					result	=	true;
				}
				else
				{
					//�g�����U�N�V�������[���o�b�N
					con.rollback();
				}
			}
			catch(Exception e)
			{
				//�g�����U�N�V�������[���o�b�N
				con.rollback();
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	result;
	}

	/**
	 * ���|�����擾����B
	 * @return ���|��
	 */
	public DataPayment getBill()
	{
		return bill;
	}

	/**
	 * ���|�����Z�b�g����B
	 * @param bill ���|��
	 */
	public void setBill(DataPayment bill)
	{
		this.bill = bill;
	}
	
	/**
	 * ���|����ǂݍ��ށB
	 * @param customerID �ڋq�h�c
	 */
	public void loadBill(Integer customerID)
	{
		Long	billValue	=	0l;
		Integer	slipNo		=	null;
		int		count		=	0;
		
		if(!customerID.equals(""))
		{
			try
			{
				ConnectionWrapper con	=	SystemInfo.getConnection();

				ResultSetWrapper	rs	=	con.executeQuery(BillsList.getSelectSQL(customerID));

				while(rs.next())
				{
					slipNo		=	rs.getInt("slip_no");
					billValue	+=	rs.getLong("bill_value_rest");

					count	++;
				}
			}
			catch(SQLException e)
			{
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
		
		if(count != 1)	slipNo	=	null;
		this.getBill().setSlipNo(slipNo);
		this.getBill().setBillValue(billValue);
	}
	
	/**
	 * �f�[�^��ǂݍ��ށB
	 * @param slipNo �`�[No.
	 */
	public void load(MstShop shop, Integer slipNo)
	{
		sales.setShop(shop);
		sales.setSlipNo(slipNo);
		
		try
		{
			//�R�l�N�V�������擾
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			sales.loadAll(con, 0);
			//sales.getShop().load(con);
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		this.optimizePayment(0);
	}
	
	/**
	 * �x���f�[�^���œK������B
	 * @param index 
	 */
	protected void optimizePayment(int index)
	{
		ArrayList<DataPaymentDetail>	temp	=	new ArrayList<DataPaymentDetail>();
		
		try
		{
			for(MstPaymentClass mpc : SystemInfo.getPaymentClasses())
			{
				boolean		isFind	=	false;

				for(int i = 0; i < sales.getPayment(index).size(); i ++)
				{
					DataPaymentDetail	dpd	=	sales.getPayment(index).get(i);
					if(mpc.getPaymentClassID() == dpd.getPaymentMethod().getPaymentClassID())
					{
						temp.add(dpd);
						sales.getPayment(index).remove(i);
						isFind	= true;
						break;
					}
				}

				if(!isFind)
				{
					temp.add(new DataPaymentDetail());
				}
			}

			for(DataPaymentDetail dpd : sales.getPayment(index))
			{
				temp.add(dpd);
			}

			temp.add(new DataPaymentDetail());

			sales.getPayment(index).clear();

			for(DataPaymentDetail dpd : temp)
			{
				sales.getPayment(index).add(dpd);
			}
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
}
