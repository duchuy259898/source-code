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
 * x•¥ƒf[ƒ^
 * @author katagiri
 */
public class DataPayment extends ArrayList<DataPaymentDetail>
{
	/**
	 * “X•Ü
	 */
	protected	MstShop			shop		=	new MstShop();
	/**
	 * “`•[No.
	 */
	protected	Integer			slipNo			=	null;
	/**
	 * x•¥No.
	 */
	protected	Integer			paymentNo		=	null;
	/**
	 * x•¥“ú
	 */
	protected	java.util.Date	paymentDate		=	null;
	/**
	 * ƒXƒ^ƒbƒt
	 */
	protected	MstStaff    staff   =	  new MstStaff();
	/**
	 * ”„Š|‹àŠz
	 */
	protected	Long	    billValue	=   0l;
	/**
	 * ‚¨’Ş‚è
	 */
	protected	Long	    changeValue	=   0l;
        /**
	 * ˆê•Û‘¶—pƒtƒ‰ƒO@
	 */
        protected	boolean	    tempFlag	=   false;

	
	/**
	 * ƒRƒ“ƒXƒgƒ‰ƒNƒ^
	 */
	public DataPayment()
	{
	}

	/**
	 * “X•Ü‚ğæ“¾‚·‚éB
	 * @return “X•Ü
	 */
	public MstShop getShop()
	{
		return shop;
	}

	/**
	 * “X•Ü‚ğƒZƒbƒg‚·‚éB
	 * @param shop “X•Ü
	 */
	public void setShop(MstShop shop)
	{
		this.shop = shop;
	}

	/**
	 * “`•[No.‚ğæ“¾‚·‚éB
	 * @return “`•[No.
	 */
	public Integer getSlipNo()
	{
		return slipNo;
	}

	/**
	 * “`•[No.‚ğƒZƒbƒg‚·‚éB
	 * @param slipNo “`•[No.
	 */
	public void setSlipNo(Integer slipNo)
	{
		this.slipNo = slipNo;
	}

	/**
	 * x•¥No.‚ğæ“¾‚·‚éB
	 * @return x•¥No.
	 */
	public Integer getPaymentNo()
	{
		return paymentNo;
	}

	/**
	 * x•¥No.‚ğƒZƒbƒg‚·‚éB
	 * @param paymentNo x•¥No.
	 */
	public void setPaymentNo(Integer paymentNo)
	{
		this.paymentNo = paymentNo;
	}

	/**
	 * x•¥“ú‚ğæ“¾‚·‚éB
	 * @return x•¥“ú
	 */
	public java.util.Date getPaymentDate()
	{
		return paymentDate;
	}

	/**
	 * x•¥“ú‚ğƒZƒbƒg‚·‚éB
	 * @param paymentDate x•¥“ú
	 */
	public void setPaymentDate(java.util.Date paymentDate)
	{
		this.paymentDate = paymentDate;
	}

	/**
	 * ƒXƒ^ƒbƒt‚ğæ“¾‚·‚éB
	 * @return ƒXƒ^ƒbƒt
	 */
	public MstStaff getStaff()
	{
		return staff;
	}

	/**
	 * ƒXƒ^ƒbƒt‚ğƒZƒbƒg‚·‚éB
	 * @param staff ƒXƒ^ƒbƒt
	 */
	public void setStaff(MstStaff staff)
	{
		this.staff = staff;
	}

	/**
	 * ”„Š|‹àŠz‚ğæ“¾‚·‚éB
	 * @return ”„Š|‹àŠz
	 */
	public Long getBillValue()
	{
		return billValue;
	}

	/**
	 * ”„Š|‹àŠz‚ğƒZƒbƒg‚·‚éB
	 * @param billValue ”„Š|‹àŠz
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
	 * ˆê•Û‘¶—pƒtƒ‰ƒO‚ğƒZƒbƒg‚·‚éB
	 * @param@ˆê•Û‘¶—pƒtƒ‰ƒO
	 */
        public boolean getTempFlag()
        {
                return tempFlag ;
        }
        /**
	 * ˆê•Û‘¶—pƒtƒ‰ƒO‚ğæ“¾‚·‚éB
	 * @param@ˆê•Û‘¶—pƒtƒ‰ƒO@
         * @return ˆê•Û‘¶—pƒtƒ‰ƒO
         */        
        public void setTempFlag(boolean tempFlag)
        {
                this.tempFlag=tempFlag;
        }	

	
	/**
	 * ResultSetWrapper‚©‚çƒf[ƒ^‚ğƒZƒbƒg‚·‚éB
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
	 * x•¥–¾×‚ğ’Ç‰Á‚·‚éB
	 * @param mpc x•¥‹æ•ª
	 * @param mpm x•¥•û–@
	 * @param value ‹àŠz
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
	 * x•¥–¾×‚ğƒZƒbƒg‚·‚éB
	 * @param index ƒCƒ“ƒfƒbƒNƒX
	 * @param mpc x•¥‹æ•ª
	 * @param mpm x•¥•û–@
	 * @param value x•¥‹àŠz
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
	 * x•¥‹àŠz‚Ì‡Œv‚ğæ“¾‚·‚éB
	 * @return x•¥‹àŠz‚Ì‡Œv
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
	 * x•¥‹àŠz‚Ì‡Œv‚ğæ“¾‚·‚éB
	 * @param paymentClass x•¥‹æ•ª
	 * @return x•¥‹àŠz‚Ì‡Œv
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
	 * x•¥ƒf[ƒ^‚ÆA–¾×‚ğ‘S‚Äæ“¾‚·‚éB
	 * @param con ConnectionWrapper
	 * @return true - ¬Œ÷
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
	 * V‚µ‚¢x•¥No.‚ğƒZƒbƒg‚·‚éB
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
	 * x•¥ƒf[ƒ^‚ÆA–¾×‚ğ‘S‚Ä“o˜^‚·‚éB
	 * @param con ConnectionWrapper
	 * @return true - ¬Œ÷
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean registAll(ConnectionWrapper con) throws SQLException
	{
		if(!this.regist(con))	return	false;

		//‰ñû–¾×ƒe[ƒuƒ‹
                boolean flg = false;
		for(DataPaymentDetail dpd : this)
		{
                        //‹àŠz‚ª‚È‚¢
			if(dpd.getPaymentValue() == null ||
					dpd.getPaymentValue() == 0)	continue;
                        
                        //“¯‚¶payment_no‚Í‚P‚ÂŒ»‹à‚µ‚©“o˜^ - New request #35011
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

		//IVS_LVTU start add 2017/10/27 #28534 [gb]x•¥–¾×‚ÉŒ»‹à‚ª‚Qs“o˜^‚³‚ê‚é
		if (this.checkRecordMultiLine(con)) {
			this.deletePaymentDetail(con);
		}
		//IVS_LVTU end add 2017/10/27 #28534 [gb]x•¥–¾×‚ÉŒ»‹à‚ª‚Qs“o˜^‚³‚ê‚é

		return	true;
	}
	
	
	/**
	 * x•¥ƒf[ƒ^‚ğ“o˜^‚·‚éB
	 * @param con ConnectionWrapper
	 * @return true - ¬Œ÷
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
	 * x•¥ƒf[ƒ^‚ª‘¶İ‚·‚é‚©‚ğæ“¾‚·‚éB
	 * @param con ConnectionWrapper
	 * @return true - ‘¶İ‚·‚é
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

	//IVS_LVTU start add 2017/10/27 #28534 [gb]x•¥–¾×‚ÉŒ»‹à‚ª‚Qs“o˜^‚³‚ê‚é
	/**
	 * data_payment_detail‚Ìshop_id,slip_no,payment_no‚ª“¯‚¶‚Æ‚±‚ë‚ÉŒ»‹à‚Ìx•¥î•ñ‚ª‚QsˆÈã‚ ‚é‚©‚Ç‚¤‚©
	 * @param con
	 * @return
	 * @throws SQLException
	*/
	public boolean checkRecordMultiLine(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper rs = con.executeQuery(this.getCountRecordSQL());

		if (rs.next())
		{
			int count = rs.getInt("count");
			if (count > 1)
            	return true;
		}

		rs.close();
		return false;
	}

	/**
	 * delete payment detail
	 * @param con
	 * @return
	 * @throws SQLException 
	*/
	public boolean deletePaymentDetail(ConnectionWrapper con) throws SQLException
	{
		if (con.execute(this.getDeletePaymentDetailSQL()))
			return true;
		else
			return false;
	}
	//IVS_LVTU end add 2017/10/27 #28534 [gb]x•¥–¾×‚ÉŒ»‹à‚ª‚Qs“o˜^‚³‚ê‚é
	
	/**
	 * V‚µ‚¢x•¥No.‚ğæ“¾‚·‚é‚r‚p‚k•¶‚ğæ“¾‚·‚éB
	 * @return V‚µ‚¢x•¥No.‚ğæ“¾‚·‚é‚r‚p‚k•¶
	 */
	public String getNewPaymentNoSQL()
	{
		return	"select coalesce(max(payment_no), 0) + 1 as new_payment_no\n" +
				"from data_payment\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
	}
	
	
	/**
	 * x•¥ƒf[ƒ^‚ğæ“¾‚·‚é‚r‚p‚k•¶‚ğæ“¾‚·‚éB
	 * @return x•¥ƒf[ƒ^‚ğæ“¾‚·‚é‚r‚p‚k•¶
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
	 * x•¥ƒf[ƒ^‚ğInsert‚·‚é‚r‚p‚k•¶‚ğæ“¾‚·‚éB
	 * @return x•¥ƒf[ƒ^‚ğInsert‚·‚é‚r‚p‚k•¶
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
	 * x•¥ƒf[ƒ^‚ğUpdate‚·‚é‚r‚p‚k•¶‚ğæ“¾‚·‚éB
	 * @return x•¥ƒf[ƒ^‚ğUpdate‚·‚é‚r‚p‚k•¶
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
	 * x•¥ƒf[ƒ^‚ğ˜_—íœ‚·‚é‚r‚p‚k•¶‚ğæ“¾‚·‚éB
	 * @return x•¥ƒf[ƒ^‚ğ˜_—íœ‚·‚é‚r‚p‚k•¶
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

	//IVS_LVTU start add 2017/10/27 #28534 [gb]x•¥–¾×‚ÉŒ»‹à‚ª‚Qs“o˜^‚³‚ê‚é
	/**
	 * data_payment_detail‚Ìshop_id,slip_no,payment_n ‚ª“¯‚¶‚Æ‚±‚ë‚ÉŒ»‹à‚Ìx•¥î•ñ‚ª‰½s‚ ‚é‚©H
	 * @return
	*/
	private String getCountRecordSQL()
	{
		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT count(1) as count\n ");
		sql.append("   FROM data_payment_detail\n ");
		sql.append("  WHERE delete_date IS NULL\n ");
		sql.append("    AND payment_method_id = 1\n ");
		sql.append("    AND shop_id = ").append(SQLUtil.convertForSQL(this.getShop().getShopID())).append("\n");
		sql.append("    AND slip_no = ").append(SQLUtil.convertForSQL(this.getSlipNo())).append("\n");
		sql.append("    AND payment_no = ").append(SQLUtil.convertForSQL(this.getPaymentNo())).append("\n");

		return sql.toString();
	}

	/**
	 * Œ»‹à‚Ìx•¥‚Ìdata_payment_detailƒf[ƒ^‚ğ˜_—íœ‚·‚éSQL•¶‚ğæ“¾‚·‚é
	 * @return
	*/
	private String getDeletePaymentDetailSQL()
	{
		StringBuilder sql = new StringBuilder();
		sql.append("Update data_payment_detail\n ");
		sql.append("   set delete_date = current_timestamp\n ");
		sql.append(" where delete_date is null\n ");
		sql.append("   and payment_method_id = 1 \n");
		sql.append("   and shop_id = ").append(SQLUtil.convertForSQL(this.getShop().getShopID())).append("\n");
		sql.append("   and slip_no = ").append(SQLUtil.convertForSQL(this.getSlipNo())).append("\n");
		sql.append("   and payment_no = ").append(SQLUtil.convertForSQL(this.getPaymentNo())).append("\n");
		sql.append("   and payment_detail_no <> (select max(payment_detail_no)\n ");
		sql.append("                               from data_payment_detail\n ");
		sql.append("                              where delete_date is null\n ");
		sql.append("                                and payment_method_id = 1\n ");
		sql.append("                                and shop_id = ").append(SQLUtil.convertForSQL(this.getShop().getShopID())).append("\n");
		sql.append("                                and slip_no = ").append(SQLUtil.convertForSQL(this.getSlipNo())).append("\n");
		sql.append("                                and payment_no = ").append(SQLUtil.convertForSQL(this.getPaymentNo())).append(")\n");

		return sql.toString();
	}
	//IVS_LVTU end add 2017/10/27 #28534 [gb]x•¥–¾×‚ÉŒ»‹à‚ª‚Qs“o˜^‚³‚ê‚é

}
