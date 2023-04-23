/*
 * RegistProportionally.java
 *
 * Created on 2009/10/30, 17:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.account;

import	java.sql.*;
import	com.geobeck.sql.*;

/**
 *
 * @author geobeck
 */
public class RegistProportionally {

    private Integer slipDetailNo            = null;
    private Integer dataProportionallyId    = null;
    private String  proportionallyName      = null;
    private Boolean designatedFlag          = null;
    private Integer staffId                 = null;
    private String  staffNo                 = null;
    private Integer point                   = null;
    private Integer ratio                   = null;
    private Integer seqNum                  = null;
    //nhanvt start 20141121 New request #32737
    private boolean lastSelectFlag = false;
    //nhanvt start 20141121 New request #32737

    /**
     * Creates a new instance of RegistProportionally
     */
    public RegistProportionally(ResultSetWrapper rs) throws SQLException {
        this.slipDetailNo = rs.getInt("slip_detail_no");
        this.dataProportionallyId = rs.getInt("data_proportionally_id");
        this.seqNum = rs.getInt("seq_num");
        this.proportionallyName = rs.getString("proportionally_name");
        this.setDesignatedFlag(rs.getBoolean("designated_flag"));
        this.setStaffNo(rs.getString("staff_no"));
        this.setStaffId(rs.getInt("staff_id"));
        this.setPoint(rs.getInt("point"));
        this.setRatio(rs.getInt("ratio"));
        this.setLastSelectFlag(rs.getBoolean("last_select_flag"));
    }    

    public RegistProportionally(RegistProportionally rp) {
        this.slipDetailNo = rp.getSlipDetailNo();
        this.dataProportionallyId = rp.getDataProportionallyId();
        this.seqNum = rp.getSeqNum();
        this.proportionallyName = rp.getProportionallyName();
        this.setDesignatedFlag(rp.getDesignatedFlag());
        this.setStaffNo(rp.getStaffNo());
        this.setStaffId(rp.getStaffId());
        this.setPoint(rp.getPoint());
        this.setRatio(rp.getRatio());
    }
    

    public Integer getSlipDetailNo() {
        return slipDetailNo;
    }
    public Integer getDataProportionallyId() {
        return dataProportionallyId;
    }
    public String getProportionallyName() {
        return proportionallyName;
    }
    public Boolean getDesignatedFlag() {
        return designatedFlag;
    }
    public Integer getStaffId() {
        return staffId;
    }
    public String getStaffNo() {
        return staffNo;
    }
    public Integer getPoint() {
        return point;
    }
    public Integer getRatio() {
        return ratio;
    }

    public Integer getSeqNum() {
        return seqNum;
    }

    public void setDesignatedFlag(Boolean designatedFlag) {
        this.designatedFlag = designatedFlag;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public void setStaffNo(String staffNo) {
        this.staffNo = staffNo;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public void setRatio(Integer ratio) {
        this.ratio = ratio;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    public String toString() {
        return proportionallyName;
    }
    
    //nhanvt start 20141121 New request #32737
    public boolean isLastSelectFlag() {
        return lastSelectFlag;
    }

    public void setLastSelectFlag(boolean lastSelectFlag) {
        this.lastSelectFlag = lastSelectFlag;
    }
    //nhanvt end 20141121 New request #32737

}
