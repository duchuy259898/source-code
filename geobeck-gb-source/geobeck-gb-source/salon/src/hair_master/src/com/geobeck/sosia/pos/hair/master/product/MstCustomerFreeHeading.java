/*
 * MstCustomerFreeHeading.java
 *
 * Created on 2007/08/20, 18:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.master.product;

import java.sql.*;
import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.customer.*;

/**
 *
 * @author kanemoto
 */
public class MstCustomerFreeHeading {
	protected	MstCustomer		mc	=	null;	/** 顧客情報 */
	protected	MstFreeHeading		mfh	=	null;
        protected	MstCustomerFreeHeadings		mcfhs	=	null;
        /** フリー項目 */
        protected String            freeHeadingText    =  "";
        //An start add 20130417
        protected String            freeHeadingName    =  "";
        protected String            freeHeadingClassName    =  "";
        //An end add 20130417
	/** Creates a new instance of MstCustomerFreeHeading */
	public MstCustomerFreeHeading() {
	}

        //An start add 20130417
        public String getFreeHeadingName() {
            return freeHeadingName;
        }

        public void setFreeHeadingName(String freeHeadingName) {
            this.freeHeadingName = freeHeadingName;
        }

        public String getFreeHeadingClassName() {
            return freeHeadingClassName;
        }

        public void setFreeHeadingClassName(String freeHeadingClassName) {
            this.freeHeadingClassName = freeHeadingClassName;
        }

         //An end add 20130417
        public String getFreeHeadingText() {
            return freeHeadingText;
        }

        public void setFreeHeadingText(String freeHeadingText) {
            this.freeHeadingText = freeHeadingText;
        }

        public MstCustomerFreeHeadings getMstCustomerFreeHeadings() {
            return mcfhs;
        }

        public void setMstCustomerFreeHeadings(MstCustomerFreeHeadings mcfhs) {
            this.mcfhs = mcfhs;
        }


	/**
	 * 顧客を取得する。
	 * @return 顧客
	 */
	public MstCustomer getMstCustomer()
	{
		return mc;
	}

	/**
	 * 顧客をセットする。
	 * @param mc 顧客
	 */
	public void setMstCustomer(MstCustomer mc)
	{
		this.mc = mc;
	}

	/**
	 * フリー項目区分を取得する。
	 * @return フリー項目区分
	 */
	public MstFreeHeading getMstFreeHeading()
	{
		return mfh;
	}

	/**
	 * フリー項目区分をセットする。
	 * @param mfh フリー項目区分
	 */
	public void setMstFreeHeading(MstFreeHeading mfh)
	{
		this.mfh = mfh;
	}

	/**
	 * データをセットする
	 */
	public void setData( MstCustomerFreeHeading mcfh )
	{
	    this.setMstCustomer( this.getMstCustomer() );
	    this.setMstFreeHeading( this.getMstFreeHeading() );
            this.setMstCustomerFreeHeadings( this.getMstCustomerFreeHeadings() );
	}

	/**
	 * ResultSetWrapperからデータをセットする
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		MstFreeHeadingClass mfhc = new MstFreeHeadingClass();

		mc	=	new	MstCustomer();
		mfh	=	new	MstFreeHeading();
                mcfhs   = new MstCustomerFreeHeadings();
		this.getMstCustomer().setData( rs );
		mfhc.setData( rs );
		this.getMstFreeHeading().setData( rs );
                mfhc.setFreeHeadingClassID(rs.getInt("free_heading_class_id"));
		this.getMstFreeHeading().setFreeHeadingClass( mfhc );


                mcfhs.setData(rs);


	}

	/**
	 * データを登録する。
	 * @param con ConnectionWrapper
	 * @param data 対象となるデータ
	 * @param dataIndex データのインデックス
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		//存在しないデータの場合
		if(!this.isExist(con))
		{
			//データをInsertする
			if(con.executeUpdate(this.getInsertDataSQL()) != 1)
			{
				return	false;
			}
		}
		//存在するデータの場合
		else
		{
			//データをUpdateする
			if(con.executeUpdate(this.getUpdateDataSQL()) != 1)
			{
				return	false;
			}
		}
		return	true;
	}

        //An start add 20130406
        public boolean registText(ConnectionWrapper con) throws SQLException
	{
		//存在しないデータの場合
		if(!this.isExist(con))
		{
			//データをInsertする
			if(con.executeUpdate(this.getInsertDataSQLText()) != 1)
			{
				return	false;
			}
		}
		//存在するデータの場合
		else
		{
			//データをUpdateする
			if(con.executeUpdate(this.getUpdateDataSQLText()) != 1)
			{
				return	false;
			}
		}
		return	true;
	}
        //An end add 20130406

	public boolean isExist(ConnectionWrapper con) throws SQLException
	{
		boolean		result		=	false;

		if(this.getMstCustomer() == null ||
				this.getMstFreeHeading() == null)
		{
			return	result;
		}

		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())
		{
			result	=	true;
		}

		rs.close();

		return	result;
	}

	/**
	 * データを削除する。
	 * @param con ConnectionWrapper
	 * @param data 対象となるデータ
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean delete(ConnectionWrapper con) throws SQLException
	{
		if(con.executeUpdate(this.getDeleteDataSQL()) != 1)
		{
			return	false;
		}

		return	true;
	}

	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return
			"select\n" +
			"*\n" +
			"from\n" +
			"mst_customer_free_heading\n" +
			"where\n" +
			"customer_id = " + SQLUtil.convertForSQL( this.getMstCustomer().getCustomerID() ) + "\n" +
			"and free_heading_class_id = " + SQLUtil.convertForSQL( this.getMstFreeHeading().getFreeHeadingClass().getFreeHeadingClassID() ) + "\n" +
			";\n";
	}

        //An start add 20130405
        private String	getInsertDataSQLText()
	{
		return
			"insert into mst_customer_free_heading\n" +
			"(customer_id, free_heading_class_id,free_heading_text,insert_date, \n" +
			"	update_date, delete_date,free_heading_id)\n" +
			"values(\n" +
			SQLUtil.convertForSQL( this.getMstCustomer().getCustomerID() ) + ",\n" +
			SQLUtil.convertForSQL( this.getMstFreeHeading().getFreeHeadingClass().getFreeHeadingClassID() ) + ",\n" +
			SQLUtil.convertForSQL( this.getMstFreeHeading().getFreeHeadingText() ) + ",\n" +
			"current_timestamp,current_timestamp, null,-1);\n";
	}

        //An end add 20130405


	/**
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String	getInsertDataSQL()
	{
		return
			"insert into mst_customer_free_heading\n" +
			"(customer_id, free_heading_class_id, free_heading_id, insert_date, \n" +
			"	update_date, delete_date)\n" +
			"values(\n" +
			SQLUtil.convertForSQL( this.getMstCustomer().getCustomerID() ) + ",\n" +
			SQLUtil.convertForSQL( this.getMstFreeHeading().getFreeHeadingClass().getFreeHeadingClassID() ) + ",\n" +
			SQLUtil.convertForSQL( this.getMstFreeHeading().getFreeHeadingID() ) + ",\n" +
			"current_timestamp,current_timestamp, null);\n";
	}

	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateDataSQL()
	{
		return
			"update mst_customer_free_heading\n" +
			"set free_heading_id = " + SQLUtil.convertForSQL( this.getMstFreeHeading().getFreeHeadingID() ) + ",\n" +
			"update_date = current_timestamp\n" +
			"where \n" +
			"customer_id = " + SQLUtil.convertForSQL( this.getMstCustomer().getCustomerID() ) + "\n" +
			"and\n" +
			"free_heading_class_id = " + SQLUtil.convertForSQL( this.getMstFreeHeading().getFreeHeadingClass().getFreeHeadingClassID() ) + "\n" +
			";\n";
	}
        //An start add 20130405
        private String	getUpdateDataSQLText()
	{
		return
			"update mst_customer_free_heading\n" +
			"set free_heading_text = " + SQLUtil.convertForSQL( this.getFreeHeadingText() ) + ",\n" +
			//"set \n" +
			"update_date = current_timestamp\n" +
			"where \n" +
			"customer_id = " + SQLUtil.convertForSQL( this.getMstCustomer().getCustomerID() ) + "\n" +
			"and\n" +
			"free_heading_class_id = " + SQLUtil.convertForSQL( this.getMstFreeHeading().getFreeHeadingClass().getFreeHeadingClassID() ) + "\n" +
			";\n";
	}

        //An end add 20130405

	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteDataSQL()
	{
		return
			"update mst_customer_free_heading\n" +
			"set update_date = current_timestamp\n" +
			"delete_date = current_timestamp\n" +
			"where \n" +
			"customer_id = " + SQLUtil.convertForSQL( this.getMstCustomer().getCustomerID() ) + "\n" +
			"and\n" +
			"free_heading_class_id = " + SQLUtil.convertForSQL( this.getMstFreeHeading().getFreeHeadingClass().getFreeHeadingClassID() ) + "\n" +
			";\n";
	}
}
