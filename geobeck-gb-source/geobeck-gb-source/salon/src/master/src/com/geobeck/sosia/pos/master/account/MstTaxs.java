/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.master.account;

import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author lvtu
 */
public class MstTaxs extends ArrayList<MstTax>
{
    /**
	 * コンストラクタ
	 */
	public MstTaxs()
	{
	}
	
	/**
	 * 軽減税率を読み込む
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean loadTaxs(ConnectionWrapper con, java.util.Date date) throws SQLException
	{
            this.clear();

            ResultSetWrapper rs = con.executeQuery(MstTaxs.getSelectSQL(date));

            while (rs.next()) {

                MstTax tempTax = new MstTax();

                tempTax.setDatas(rs);

                if (tempTax.getTaxID() != null && 0 < tempTax.getTaxID()) {
                    this.add(tempTax);
                }
            }

            return true;
	}
	
	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	public static String getSelectSQL(java.util.Date date)
	{
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select tax_id, apply_date, coalesce(tax_rate, 0) as tax_rate, reduced_tax");
            sql.append("     from mst_tax");
            sql.append("     where delete_date is null");
            sql.append("     and reduced_tax = true");
            sql.append("     and apply_date = ");
            sql.append("     (select max(apply_date)");
            sql.append("     from mst_tax");
            sql.append("     where delete_date is null");
            if (date != null) {
                sql.append("     and apply_date <= ").append(SQLUtil.convertForSQLDateOnly(date)).append(")");
            } else {
                sql.append("     and apply_date <= current_date)");
            }
            sql.append(" order by");
            sql.append("      reduced_tax");
            sql.append("      ,tax_rate");
            sql.append("     ,tax_id");

            return sql.toString();
	}
	
	/**
	 * 軽減税率を取得する。
	 * @param taxID 消費税ＩＤ
	 * @return 消費税
	 */
	public MstTax getTax(Integer taxID)
	{
            if (taxID == null) return new MstTax();

            for (MstTax tax : this) {
                if (tax.getTaxID() == taxID) {
                    return tax;
                }
            }

            return new MstTax();
	}
}
