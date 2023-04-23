/*
 * ReportParameterBean.java
 *
 * Created on 2006/05/21, 1:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.report.bean;

import java.util.Date;

/**
 *
 * @author k-anayama
 */
public class RemainingParameterBean
{
	
	public static final	int	TAX_TYPE_BLANK			=	1;
	public static final	int	TAX_TYPE_UNIT			=	2;
        
	private String	shopIDList;
	private String	targetName;
	private int	taxType;
        private boolean ckDigestion ;
	private String	courseStartDate;
	private String	courseEndDate;
	private Date	courseStartDateObj;
	private Date	courseEndDateObj;
        private String	digestionStartDate;
	private String	digestionEndDate;
	private Date	digestionStartDateObj;
	private Date	digestionEndDateObj;
	
	/** Creates a new instance of ReportParameterBean */
	public RemainingParameterBean() {
	}

        public String getShopIDList() {
            return shopIDList;
        }

        public void setShopIDList(String shopIDList) {
            this.shopIDList = shopIDList;
        }

        public String getTargetName() {
            return targetName;
        }

        public void setTargetName(String targetName) {
            this.targetName = targetName;
        }

        public int getTaxType() {
            return taxType;
        }

        public void setTaxType(int taxType) {
            this.taxType = taxType;
        }

        public String getCourseStartDate() {
            return courseStartDate;
        }

        public void setCourseStartDate(String courseStartDate) {
            this.courseStartDate = courseStartDate;
        }

        public String getCourseEndDate() {
            return courseEndDate;
        }

        public void setCourseEndDate(String courseEndDate) {
            this.courseEndDate = courseEndDate;
        }

        public Date getCourseStartDateObj() {
            return courseStartDateObj;
        }

        public void setCourseStartDateObj(Date courseStartDateObj) {
            this.courseStartDateObj = courseStartDateObj;
        }

        public Date getCourseEndDateObj() {
            return courseEndDateObj;
        }

        public void setCourseEndDateObj(Date courseEndDateObj) {
            this.courseEndDateObj = courseEndDateObj;
        }

        public boolean isCkDigestion() {
            return ckDigestion;
        }

        public void setCkDigestion(boolean ckDigestion) {
            this.ckDigestion = ckDigestion;
        }

        public String getDigestionStartDate() {
            return digestionStartDate;
        }

        public void setDigestionStartDate(String digestionStartDate) {
            this.digestionStartDate = digestionStartDate;
        }

        public String getDigestionEndDate() {
            return digestionEndDate;
        }

        public void setDigestionEndDate(String digestionEndDate) {
            this.digestionEndDate = digestionEndDate;
        }

        public Date getDigestionStartDateObj() {
            return digestionStartDateObj;
        }

        public void setDigestionStartDateObj(Date digestionStartDateObj) {
            this.digestionStartDateObj = digestionStartDateObj;
        }

        public Date getDigestionEndDateObj() {
            return digestionEndDateObj;
        }

        public void setDigestionEndDateObj(Date digestionEndDateObj) {
            this.digestionEndDateObj = digestionEndDateObj;
        }        
}
