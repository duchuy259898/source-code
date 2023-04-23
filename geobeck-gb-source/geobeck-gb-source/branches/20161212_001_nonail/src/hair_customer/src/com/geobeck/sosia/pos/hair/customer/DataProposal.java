package com.geobeck.sosia.pos.hair.customer;

/*
 * DataProposal.java
 *
 * Created on 2015/11/16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */




import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import java.sql.*;
import java.util.*;
import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * @author lvtu
 */
public class DataProposal extends ArrayList<DataProposalDetail>
{
        /**
	 * “X•ÜID.
	 */
	protected	Integer			shopID                  =	null;
	/**
	 * ’ñˆÄ‘ID.
	 */
	protected	Integer			proposalID		=	null;
        /**
	 * ’ñˆÄ‘ì¬“ú.
	 */
	protected	java.util.Date  	proposalDate		=	null;
	
        /**
	 * ’ñˆÄ‘–¼.
	 */
	protected	String			proposalName    	=	null;
        
        /**
	 * ŒÚ‹qID.
	 */
	protected	Integer			customerID		=	null;
        
        /**
	 * ƒXƒ^ƒbƒtID.
	 */
	protected	Integer			staffID                 =	null;
        
         /**
	 * ’ñˆÄ—LŒøŠúŒÀ.
	 */
	protected	java.util.Date			proposalValidDate     =	null;
        
        /**
	 * ’ñˆÄƒƒ‚.
	 */
	protected	String			proposalMemo    	=	null;
        
        /**
	 * Œ_–ñ“X•ÜID.
	 */
	protected	Integer			contractShopID    	=	null;
        
        /**
	 * Œ_–ñ“X•Ü“`•[NO.
	 */
	protected	Integer			slipNo                  =	null;

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

        public java.util.Date getProposalDate() {
            return proposalDate;
        }

        public void setProposalDate(java.util.Date proposalDate) {
            this.proposalDate = proposalDate;
        }

        public String getProposalName() {
            return proposalName;
        }

        public void setProposalName(String proposalName) {
            this.proposalName = proposalName;
        }

        public Integer getCustomerID() {
            return customerID;
        }

        public void setCustomerID(Integer customerID) {
            this.customerID = customerID;
        }

        public Integer getStaffID() {
            return staffID;
        }

        public void setStaffID(Integer staffID) {
            this.staffID = staffID;
        }

        public java.util.Date getProposalValidDate() {
            return proposalValidDate;
        }

        public void setProposalValidDate(java.util.Date proposalValidDate) {
            this.proposalValidDate = proposalValidDate;
        }

        public String getProposalMemo() {
            return proposalMemo;
        }

        public void setProposalMemo(String proposalMemo) {
            this.proposalMemo = proposalMemo;
        }

        public Integer getContractShopID() {
            return contractShopID;
        }

        public void setContractShopID(Integer contractShopID) {
            this.contractShopID = contractShopID;
        }

        public Integer getSlipNo() {
            return slipNo;
        }

        public void setSlipNo(Integer slipNo) {
            this.slipNo = slipNo;
        }
        
	public DataProposal()
	{
            
	}
	
	/**
	 * ResultSetWrapper‚©‚çƒf[ƒ^‚ğƒZƒbƒg‚·‚éB
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
            this.setShopID(rs.getInt("shop_id"));
            this.setProposalID(rs.getInt("proposal_id"));
            this.setProposalDate(rs.getDate("proposal_date"));
            this.setProposalName(rs.getString("proposal_name"));
            this.setCustomerID(rs.getInt("customer_id"));
            this.setStaffID(rs.getInt("staff_id"));
            this.setProposalValidDate(rs.getDate("proposal_valid_date"));
            this.setProposalMemo(rs.getString("proposal_memo"));
            this.setContractShopID(rs.getInt("contract_shop_id"));
            this.setSlipNo(rs.getInt("slip_no"));
	}
	
	
	
	/**
	 * “`•[ƒwƒbƒ_ƒf[ƒ^A“`•[–¾×ƒf[ƒ^Ax•¥ƒf[ƒ^Ax•¥–¾×ƒf[ƒ^‚ğ“o˜^‚·‚éB
	 * @param con ConnectionWrapper
	 * @param paymentNo x•¥No.
	 * @return true - ¬Œ÷
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean registAll(ConnectionWrapper con) throws SQLException
	{
                //delte data data_proposal_detail
		this.deleteDetail(con);

		if(!this.regist(con))	return	false;
		
		//”„ã–¾×ƒe[ƒuƒ‹
		for(DataProposalDetail dsd : this)
		{
			dsd.setShopID(this.shopID);
			dsd.setProposalID(proposalID);
			dsd.setNewProposalDetailNo(con);
			if(!dsd.regist(con))	return	false;
		}
		
		return	true;
	}
	
	
	/**
	 * “o˜^ˆ—‚ğs‚¤B
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
                        setNewSlipNo(con);
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
	 * “`•[ƒwƒbƒ_ƒf[ƒ^‚ª‘¶İ‚·‚é‚©‚ğæ“¾‚·‚éB
	 * @param con ConnectionWrapper
	 * @return true - ‘¶İ‚·‚é
	 * @throws java.sql.SQLException SQLException
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.proposalID == null || this.proposalID < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 * “`•[ƒwƒbƒ_ƒf[ƒ^A“`•[–¾×ƒf[ƒ^Ax•¥ƒf[ƒ^Ax•¥–¾×ƒf[ƒ^‚ğ“Ç‚İ‚ŞB
	 * @param con ConnectionWrapper
	 * @param paymentNo x•¥No.
	 * @return true - ¬Œ÷
	 * @throws java.lang.Exception Exception
	 */
	public boolean loadAll(ConnectionWrapper con) throws Exception
	{
		this.clear();
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectAllSQL());
		
		if(rs.next())
		{
			this.setData(rs);
			
			rs.beforeFirst();
			
			while(rs.next())
			{
				DataProposalDetail	dsd	=	new DataProposalDetail();
				dsd.setData(rs);
				this.add(dsd);
			}
		}
		
		rs.close();
                
		return	true;
	}
	
	
	public void setNewSlipNo(ConnectionWrapper con) throws SQLException
	{
		ResultSetWrapper	rs	=	con.executeQuery(this.getNewProposalSQL());
		
		if(rs.next())
		{
			this.proposalID = rs.getInt("new_proposal_id");
		}
		
		rs.close();
	}
        
        
        public boolean delete(ConnectionWrapper con) throws SQLException
	{
		boolean	result	=	false;
                
                deleteDetail(con);
		
		result	=	(con.executeUpdate(this.getDeleteSQL()) == 1);
		
		return	result;
	}
        
        public String getDeleteSQL()
	{
		return	"delete from data_proposal\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n" +
				"and proposal_id = " + SQLUtil.convertForSQL(this.proposalID) + "\n";
	}
        
        /**
	 * “`•[ƒwƒbƒ_ƒf[ƒ^‚ğ˜_—íœ‚·‚éB
	 * @param con ConnectionWrapper
	 * @return true - ¬Œ÷
	 */
	public boolean deleteDetail(ConnectionWrapper con) throws SQLException
	{
		boolean	result	=	false;
		
		result	=	(con.executeUpdate(this.getDeleteDetailSQL()) == 1);
		
		return	result;
	}
        
        public String getDeleteDetailSQL()
	{
		return	"delete from data_proposal_detail\n" +
				"where delete_date is null\n" +
				"and shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n" +
				"and proposal_id = " + SQLUtil.convertForSQL(this.proposalID) + "\n";
	}
        
	/**
	 * “`•[ƒwƒbƒ_ƒf[ƒ^‚ğæ“¾‚·‚é‚r‚p‚k•¶‚ğæ“¾‚·‚éB
	 * @return “`•[ƒwƒbƒ_ƒf[ƒ^‚ğæ“¾‚·‚é‚r‚p‚k•¶
	 */
	public String getSelectSQL()
	{
		return	"select dp.*\n" +
				"from data_proposal dp\n" +
				"where dp.delete_date is null\n" +
				"and dp.shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n" +
				"and dp.proposal_id = " + SQLUtil.convertForSQL(this.proposalID) + "\n";
	}
	
        
        public String getNewProposalSQL()
	{
		return	"select coalesce(max(proposal_id), 0) + 1 as new_proposal_id\n" +
				"from data_proposal\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n";
	}
	/**
	 * “`•[ƒwƒbƒ_ƒf[ƒ^‚ğInsert‚·‚é‚r‚p‚k•¶‚ğæ“¾‚·‚éB
	 * @return “`•[ƒwƒbƒ_ƒf[ƒ^‚ğInsert‚·‚é‚r‚p‚k•¶
	 */
	public String getInsertSQL()
	{
		return	"insert into data_proposal \n" +
				"(shop_id, proposal_id, proposal_date, proposal_name,\n" +
				"customer_id, staff_id, proposal_valid_date, proposal_memo,\n " +
				"insert_date, update_date, delete_date)\n" +
				"values(\n" +
				SQLUtil.convertForSQL(this.shopID) + ",\n" +
                                SQLUtil.convertForSQL(this.proposalID) + ",\n" +
				"current_timestamp ,\n" +
                                SQLUtil.convertForSQL(this.proposalName) + ",\n" +
                                SQLUtil.convertForSQL(this.customerID) + ",\n" +
                                SQLUtil.convertForSQL(this.staffID) + ",\n" +
                                SQLUtil.convertForSQLDateOnly(this.proposalValidDate) + ",\n" +
                                SQLUtil.convertForSQL(this.proposalMemo) + ",\n" +
				"current_timestamp, current_timestamp, null)\n";
	}
	
	/**
	 * “`•[ƒwƒbƒ_ƒf[ƒ^‚ğUpdate‚·‚é‚r‚p‚k•¶‚ğæ“¾‚·‚éB
	 * @return “`•[ƒwƒbƒ_ƒf[ƒ^‚ğUpdate‚·‚é‚r‚p‚k•¶
	 */
	public String getUpdateSQL()
	{
		return	"update data_proposal \n" +
				"set\n" +
                                "proposal_name =" +SQLUtil.convertForSQL(this.proposalName) + ",\n" +
                                "staff_id =" +SQLUtil.convertForSQL(this.staffID) + ",\n" +
                                "proposal_valid_date =" +SQLUtil.convertForSQLDateOnly(this.proposalValidDate) + ",\n" +
                                "proposal_memo =" +SQLUtil.convertForSQL(this.proposalMemo) + ",\n" +
				"update_date = current_timestamp\n" +
                                " where \n" +
				"shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n" +
				"and proposal_id = " + SQLUtil.convertForSQL(this.proposalID) ;
	}
	

        private String getSelectAllSQL() {
            return	"select dp.*, dpd.*,\n" +
                            "case when dpd.product_division = 2 then mic.item_class_id else mcc.course_class_id end as product_class_id,\n" +
                            "case when dpd.product_division = 2 then mic.item_class_name else mcc.course_class_name end as product_class_name,\n" +
                            "case when dpd.product_division = 2 then mi.item_name else mc.course_name end as product_name\n" +
                            "from data_proposal dp\n" +
                            "inner join data_proposal_detail dpd on dpd.shop_id = dp.shop_id and dpd.proposal_id = dp.proposal_id\n" +
                            "left join mst_item mi on mi.item_id = dpd.product_id\n" +
                            "left join mst_item_class mic on mic.item_class_id = mi.item_class_id\n" +
                            "left join mst_course mc on dpd.product_id = mc.course_id\n" +
                            "left join mst_course_class mcc on mcc.course_class_id = mc.course_class_id\n" +
                            "where dp.shop_id = " + SQLUtil.convertForSQL(this.shopID) + "\n" +
                            "and dp.proposal_id = " + SQLUtil.convertForSQL(this.proposalID) + "\n" +
                            "and dp.delete_date is null\n" +
                            "and dpd.delete_date is null" ;
        }
        
        public void sort()
	{
		Collections.sort(this, new ProposalComparator());
	}
    private class ProposalComparator implements Comparator<DataProposalDetail> {
        @Override    
        public int compare(DataProposalDetail p0, DataProposalDetail p1)
        {
                return	p0.getProductID().compareTo(p1.getProductID());
        }
    }
}
