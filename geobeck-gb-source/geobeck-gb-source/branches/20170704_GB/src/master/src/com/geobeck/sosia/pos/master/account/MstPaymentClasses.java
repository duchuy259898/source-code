/*
 * MstPaymentClasses.java
 *
 * Created on 2006/05/09, 12:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.account;

import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;

/**
 * x•¥‹æ•ªƒf[ƒ^‚ÌArrayList
 * @author katagiri
 */
public class MstPaymentClasses extends ArrayList<MstPaymentClass>
{
	/**
	 * ƒRƒ“ƒXƒgƒ‰ƒNƒ^
	 */
	public MstPaymentClasses()
	{
	}
	
	/**
	 * x•¥‹æ•ª‚ğ“Ç‚İ‚Ş
	 * @param con ConnectionWrapper
	 * @return true - ¬Œ÷
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean loadClasses(ConnectionWrapper con) throws SQLException
	{
            this.clear();

            ResultSetWrapper rs = con.executeQuery(MstPaymentClasses.getSelectSQL());
            MstPaymentClass tempClass = new MstPaymentClass();

            while (rs.next()) {
                
                if (tempClass.getPaymentClassID() == null || tempClass.getPaymentClassID() != rs.getInt("payment_class_id")) {
                    if (tempClass.getPaymentClassID() != null) {
                        this.add(tempClass);
                        tempClass = new MstPaymentClass();
                    }

                    tempClass.setData(rs);
                }

                MstPaymentMethod tempMethod = new MstPaymentMethod();

                tempMethod.setData(rs);

                if (tempMethod.getPaymentMethodID() != null && 0 < tempMethod.getPaymentMethodID()) {
                    tempClass.add(tempMethod);
                }
            }

            if (tempClass.getPaymentClassID() != null) {
                this.add(tempClass);
            }

            return true;
	}
	
	/**
	 * Select•¶‚ğæ“¾‚·‚éB
	 * @return Select•¶
	 */
	public static String getSelectSQL()
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      mpc.payment_class_id");
            sql.append("     ,mpc.payment_class_name");
            sql.append("     ,mpm.payment_method_id");
            sql.append("     ,mpm.payment_method_name");
            sql.append("     ,mpm.cutoff_day");
            sql.append("     ,mpm.receipt_class");
            sql.append("     ,mpm.receipt_day");
            sql.append("     ,mpm.prepaid");
            sql.append(" from");
            sql.append("     mst_payment_class mpc");
            sql.append("         left join mst_payment_method mpm");
            sql.append("                on mpm.payment_class_id = mpc.payment_class_id");
            sql.append("               and mpm.delete_date is null");
            sql.append(" where");
            sql.append("     mpc.delete_date is null");
            sql.append(" order by");
            sql.append("      mpc.payment_class_id");
            sql.append("     ,mpm.payment_method_id");

            return sql.toString();
	}
	
	/**
	 * x•¥‹æ•ª‚ğæ“¾‚·‚éB
	 * @param paymentClassID x•¥‹æ•ª‚h‚c
	 * @return x•¥‹æ•ª
	 */
	public MstPaymentClass getPaymentClass(Integer paymentClassID)
	{
            if (paymentClassID == null) return new MstPaymentClass();

            for (MstPaymentClass mpc : this) {
                if (mpc.getPaymentClassID() == paymentClassID) {
                    return mpc;
                }
            }

            return new MstPaymentClass();
	}
}
