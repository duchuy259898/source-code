/*
 * ConsumerCreditRegisterMemberData.java
 *
 * Created on 2013/02/27, 10:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.customer;

import com.geobeck.sql.*;
import java.util.*;
import java.sql.SQLException;

/**
 *
 * @author ivs_tttung
 */
public class ConsumerCreditRegisterMemberData
{
    
    //data_credit
    private Integer shop_id = null;
    private Integer slip_no = null;
    private Integer payment_no = null;
    private Integer payment_detail_no = null;
    
    private Date adddate = new Date();
    private Integer customer_id = null;
    private String credit_no = null;    
    private String credit_name =  null;
    private String customer_name = null;
    private String contract_status = null;
    private String credit_flag = null;
    private Integer credit_value = null;
    //An start add 20131023
    private String approval = null;
    //An start add 20131023
    private String credit_status_no = null;
    private Date apply_start_date = new Date(); //--与信申請日
    private Date apply_end_date = new Date(); //--与信完了日
    private Date submission_date = new Date(); //--申込提出日
    private Date receive_money_date = new Date(); //--入金日
    private Date cancel_date = new Date(); //--キャンセル日
    
    //mst_credit
    private Integer mst_credit_status_no = null;
    private String mst_credit_status_name = null;
    
    // VTBPhuong start add 2010514
    //顧客No.
    private String customerNo = null;
    private String contractName = null;
    //course_name
    // VTBPhuong start add 2010514

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String courseName) {
        this.contractName = courseName;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    
    public ConsumerCreditRegisterMemberData()
    {
    }

    
    public void setData(ResultSetWrapper rs) throws SQLException
    {  
        this.setAdddate(rs.getDate("insert_date"));
        this.setCustomer_id(rs.getInt("customer_id"));
        this.setCredit_no(rs.getString("customer_no"));        
        this.setCustomer_name(rs.getString("customer_name"));       
        this.setContract_status(rs.getString("contract_status"));
        this.setCredit_name(rs.getString("credit_name"));        
        this.setCredit_value(rs.getInt("credit_value"));
        this.setCredit_status_no(rs.getString("credit_status_name"));
        this.setApply_start_date(rs.getDate("apply_start_date"));
        this.setApply_end_date(rs.getDate("apply_end_date"));
        this.setSubmission_date(rs.getDate("submission_date"));
        this.setReceive_money_date(rs.getDate("receive_money_date"));
        this.setCancel_date(rs.getDate("cancel_date"));
        this.setShop_id(rs.getInt("shop_id"));
        this.setSlip_no(rs.getInt("slip_no"));
        this.setPayment_no(rs.getInt("payment_no"));
        this.setPayment_detail_no(rs.getInt("payment_detail_no"));
    }
    
     public void setDataForConsumer(ResultSetWrapper rs) throws SQLException
    {  
        this.setAdddate(rs.getDate("insert_date"));
        this.setCustomer_id(rs.getInt("customer_id"));
        this.setCredit_no(rs.getString("customer_no"));        
        this.setCustomer_name(rs.getString("customer_name"));       
        //this.setContract_status(rs.getString("contract_status"));
        this.setCreditFlag(rs.getString("credit_flag"));
        this.setCredit_name(rs.getString("credit_name"));        
        this.setCredit_value(rs.getInt("credit_value"));
        //An start add  20131023
        this.setApproval(rs.getString("approval"));
        //An end add  20131023
        this.setCredit_status_no(rs.getString("credit_status_name"));
        this.setApply_start_date(rs.getDate("apply_start_date"));
        this.setApply_end_date(rs.getDate("apply_end_date"));
        this.setSubmission_date(rs.getDate("submission_date"));
        this.setReceive_money_date(rs.getDate("receive_money_date"));
        this.setCancel_date(rs.getDate("cancel_date"));
        this.setShop_id(rs.getInt("shop_id"));
        this.setSlip_no(rs.getInt("slip_no"));
        this.setPayment_no(rs.getInt("payment_no"));
        this.setPayment_detail_no(rs.getInt("payment_detail_no"));
        this.setCustomerNo(rs.getString("customer_no"));
        this.setContractName(rs.getString("course_name"));
    }
    
    
    /**
     * @return the shop_id
     */
    public Integer getShop_id() {
        return shop_id;
    }

    /**
     * @param shop_id the shop_id to set
     */
    public void setShop_id(Integer shop_id) {
        this.shop_id = shop_id;
    }

    /**
     * @return the slip_no
     */
    public Integer getSlip_no() {
        return slip_no;
    }

    /**
     * @param slip_no the slip_no to set
     */
    public void setSlip_no(Integer slip_no) {
        this.slip_no = slip_no;
    }
    
    /**
     * @return the credit_name
     */
    public String getCredit_name() {
        return credit_name;
    }

    /**
     * @param credit_name the credit_name to set
     */
    public void setCredit_name(String credit_name) {
        this.credit_name = credit_name;
    }

    /**
     * @return the credit_value
     */
    public Integer getCredit_value() {
        return credit_value;
    }

    /**
     * @param credit_value the credit_value to set
     */
    public void setCredit_value(Integer credit_value) {
        this.credit_value = credit_value;
    }

    //An start add  20131023
    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }
   
    
    //An end add 20131023
    
    
    /**
     * @return the credit_status_no
     */
    public String getCredit_status_no() {
        return credit_status_no;
    }

    /**
     * @param credit_status_no the credit_status_no to set
     */
    public void setCredit_status_no(String credit_status_no) {
        this.credit_status_no = credit_status_no;
    }

    /**
     * @return the apply_start_date
     */
    public Date getApply_start_date() {
        return apply_start_date;
    }
    
    /**
     * @param apply_start_date the apply_start_date to set
     */
    public void setApply_start_date(Date apply_start_date) {
        this.apply_start_date = apply_start_date;
    }

    /**
     * @return the apply_end_date
     */
    public Date getApply_end_date() {
        return apply_end_date;
    }

    /**
     * @param apply_end_date the apply_end_date to set
     */
    public void setApply_end_date(Date apply_end_date) {
        this.apply_end_date = apply_end_date;
    }

    /**
     * @return the submission_date
     */
    public Date getSubmission_date() {
        return submission_date;
    }

    /**
     * @param submission_date the submission_date to set
     */
    public void setSubmission_date(Date submission_date) {
        this.submission_date = submission_date;
    }

    /**
     * @return the receive_money_date
     */
    public Date getReceive_money_date() {
        return receive_money_date;
    }

    /**
     * @param receive_money_date the receive_money_date to set
     */
    public void setReceive_money_date(Date receive_money_date) {
        this.receive_money_date = receive_money_date;
    }

    /**
     * @return the cancel_date
     */
    public Date getCancel_date() {
        return cancel_date;
    }

    /**
     * @param cancel_date the cancel_date to set
     */
    public void setCancel_date(Date cancel_date) {
        this.cancel_date  = cancel_date;
    }

    /**
     * @return the mst_credit_status_no
     */
    public Integer getMst_credit_status_no() {
        return mst_credit_status_no;
    }

    /**
     * @param mst_credit_status_no the mst_credit_status_no to set
     */
    public void setMst_credit_status_no(Integer mst_credit_status_no) {
        this.mst_credit_status_no = mst_credit_status_no;
    }

    /**
     * @return the mst_credit_status_name
     */
    public String getMst_credit_status_name() {
        return mst_credit_status_name;
    }

    /**
     * @param mst_credit_status_name the mst_credit_status_name to set
     */
    public void setMst_credit_status_name(String mst_credit_status_name) {
        this.mst_credit_status_name = mst_credit_status_name;
    }

    /**
     * @return the adddate
     */
    public Date getAdddate() {
        return adddate;
    }

    /**
     * @param adddate the adddate to set
     */
    public void setAdddate(Date adddate) {
        this.adddate  = adddate;
    }

    /**
     * @return the credit_no
     */
    public String getCredit_no() {
        return credit_no;
    }

    /**
     * @param credit_no the credit_no to set
     */
    public void setCredit_no(String credit_no) {
        this.credit_no = credit_no;
    }

    /**
     * @return the customer_name
     */
    public String getCustomer_name() {
        return customer_name;
    }

    /**
     * @param customer_name the customer_name to set
     */
    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    /**
     * @return the contract_status
     */
    public String getContract_status() {
        return contract_status;
    }

    /**
     * @param contract_status the contract_status to set
     */
    public void setContract_status(String contract_status) {
        this.contract_status = contract_status;
    }
    
       public String getCreditFlag() {
        return credit_flag;
    }

    /**
     * @param contract_status the contract_status to set
     */
    public void setCreditFlag(String credit_flag) {
        this.credit_flag = credit_flag;
    }


    /**
     * @return the customer_id
     */
    public Integer getCustomer_id() {
        return customer_id;
    }

    /**
     * @param customer_id the customer_id to set
     */
    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }

    /**
     * @return the payment_no
     */
    public Integer getPayment_no() {
        return payment_no;
    }

    /**
     * @param payment_no the payment_no to set
     */
    public void setPayment_no(Integer payment_no) {
        this.payment_no = payment_no;
    }

    /**
     * @return the payment_detail_no
     */
    public Integer getPayment_detail_no() {
        return payment_detail_no;
    }

    /**
     * @param payment_detail_no the payment_detail_no to set
     */
    public void setPayment_detail_no(Integer payment_detail_no) {
        this.payment_detail_no = payment_detail_no;
    }
}
