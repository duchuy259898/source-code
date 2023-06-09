package com.geobeck.sosia.pos.hair.customer;

/*
 * DataProposalDetail.java
 *
 * Created on 2015/11/16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */



import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

import com.geobeck.sosia.pos.master.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.product.*;
import com.geobeck.sosia.pos.util.*;

/**
 * @author lvtu
 */
public class DataProposalDetail
{
	
	/**
	 * 店舗ID.
	 */
	protected	Integer		shopID                          =	null;
        
        /**
	 * 提案書ID.
	 */
	protected	Integer		proposalID                      =	null;
        
        /**
	 * 提案書明細ID.
	 */
	protected	Integer		proposalDetailID		=	null;
        
        /**
	 * 区分ID.
	 */
	protected	Integer		productDivision                 =	null;
        
        /**
	 * コース/商品ID.
	 */
	protected	Integer		productID                       =	null;
        
        /**
	 * 契約回数/数量.
	 */
	protected	Integer		productNum                       =	null;
        
        /**
	 * 単価.
	 */
	protected	Long		productValue                     =	null;
        
        /**
	 * コース／商品名.
	 */
        protected       String          productName                        =      null;
        
        /**
	 * 分類ID.
	 */
        protected       Integer          classID                        =      null;

        /**
	 * 分類.
	 */
        protected       String          className                        =      null;

        public Integer getShopID() {
            return shopID;
        }

        public void setShopID(Integer shopID) {
            this.shopID = shopID;
        }

        public Integer getProposalID() {
            return proposalID;
        }

        public void setProposalID(Integer proposalID) {
            this.proposalID = proposalID;
        }

        public Integer getProposalDetailID() {
            return proposalDetailID;
        }

        public void setProposalDetailID(Integer proposalDetailID) {
            this.proposalDetailID = proposalDetailID;
        }

        public Integer getProductDivision() {
            return productDivision;
        }

        public void setProductDivision(Integer productDivision) {
            this.productDivision = productDivision;
        }

        public Integer getProductID() {
            return productID;
        }

        public void setProductID(Integer productID) {
            this.productID = productID;
        }

        public Integer getProductNum() {
            return productNum;
        }

        public void setProductNum(Integer productNum) {
            this.productNum = productNum;
        }

        public Long getProductValue() {
            return productValue;
        }

        public void setProductValue(Long productValue) {
            this.productValue = productValue;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }
	
        public Integer getClassID() {
            return classID;
        }

        public void setClassID(Integer classID) {
            this.classID = classID;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }
	
	/**
	 * コンストラクタ
	 */
	public DataProposalDetail()
	{
	}
	
        
	/**
	 * ResultSetWrapperからデータを取得する。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
            Double value = rs.getDouble("product_value");
            
            this.setShopID(rs.getInt("shop_id"));
            this.setProposalID(rs.getInt("proposal_id"));
            this.setProposalDetailID(rs.getInt("proposal_detail_id"));
            this.setProductDivision(rs.getInt("product_division"));
            this.setProductID(rs.getInt("product_id"));
            this.setProductNum(rs.getInt("product_num"));
            this.setProductValue(value.longValue());
            this.setClassID(rs.getInt("product_class_id"));
            this.setClassName(rs.getString("product_class_name"));
            this.setProductName(rs.getString("product_name"));	
	}

	
	/**
	 * 登録処理を行う。
	 * @param con ConnectionWrapper
	 * @return true - 成功
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		String	sql		=	"";
		
		sql	=	this.getInsertSQL();
		
		if(con.executeUpdate(sql) == 1)
		{
			return	true;
		}
		else
		{
			return	false;
		}
	}
	
	
	
	public void setNewProposalDetailNo(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getNewProposalDetailNoSQL());
		
		if(rs.next())
		{
			this.proposalDetailID = rs.getInt("new_proposal_detail_id");
		}
		
		rs.close();
	}
        
        
	/**
	 * 新しい伝票明細No.を取得するＳＱＬ文を取得する。
	 * @return 新しい伝票明細No.を取得するＳＱＬ文
	 */
	public String getNewProposalDetailNoSQL()
	{
		return	"select coalesce(max(proposal_detail_id), 0) + 1 as new_proposal_detail_id\n" +
				"from data_proposal_detail \n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n" +
				"and proposal_id = " + SQLUtil.convertForSQL(this.proposalID) + "\n";
	}
	
	
	/**
	 * 伝票明細データを取得するＳＱＬ文を取得する。
	 * @return 伝票明細データを取得するＳＱＬ文
	 */
	public String getSelectSQL()
	{
		return	"select dpd.* \n" +
				"from data_proposal_detail dpd\n" +
				"where dpd.delete_date is null\n" +
				"and dpd.shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n" +
				"and dpd.proposal_id = " + SQLUtil.convertForSQL(this.proposalID) + "\n" +
				"and dpd.proposal_detail_id = " + SQLUtil.convertForSQL(this.proposalDetailID);
	}
	
	/**
	 * 伝票明細データをInsertするＳＱＬ文を取得する。
	 * @return 伝票明細データをInsertするＳＱＬ文
	 */
	public String getInsertSQL()
	{
		return	"insert into data_proposal_detail\n" +
				"(shop_id, proposal_id, proposal_detail_id, product_division, product_id,\n" +
				"product_num, product_value,\n" +
				"insert_date, update_date, delete_date)\n" +
				"values(\n" +
				SQLUtil.convertForSQL(this.shopID) + ",\n" +
				SQLUtil.convertForSQL(this.proposalID) + ",\n" +
				SQLUtil.convertForSQL(this.proposalDetailID) + ",\n" +
				SQLUtil.convertForSQL(this.productDivision) + ",\n" +
				SQLUtil.convertForSQL(this.productID) + ",\n" +
				SQLUtil.convertForSQL(this.getProductNum(), "0") + ",\n" +
				SQLUtil.convertForSQL(this.getProductValue(), "0") + ",\n" +
				"current_timestamp, current_timestamp, null)\n";
	}
	
        
        
}
