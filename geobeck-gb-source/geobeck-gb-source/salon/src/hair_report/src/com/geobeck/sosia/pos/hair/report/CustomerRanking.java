/*
 * CustomerRanking.java
 *
 * Created on 2008/07/20, 16:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report;

import java.sql.*;

import com.geobeck.sql.*;

/**
 * �X�^�b�t�����L���O�f�[�^
 * @author saito
 */
public class CustomerRanking
{
        /**
         * �ڋq��
         */
        protected   Long        targetCount = 0l;
        /**
         * ���X��
         */
        protected   Long        visitCount = 0l;
        /**
         * ������z
         */
        protected   Long        salesValue = 0l;
        /**
         * ����V�F�A��
         */
        protected   String      salesShareRate = "";
        /**
         * �݌v����V�F�A��
         */
        protected   String      totalSalesShareRate = "";
        /**
         * �q�P��
         */
        protected   Long        avgUnitPrice = 0l;
        /**
         * ���ϗ��X��
         */
        protected   Double      avgVisitCount = 0d;
        /**
         * ���ϗ��X�P��
         */
        protected   Long        avgVisitPrice = 0l;
        /**
         * �ڍ�
         */
        protected   Object      moreInfo = null;

	/**
        * Creates a new instance of CustomerRanking
        */
	public CustomerRanking()
	{
	}
	
	/**
	 * ResultSetWrapper����f�[�^���擾����B
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
            setTargetCount(rs.getLong("customer_count"));
            setVisitCount(rs.getLong("visit_count"));
            setSalesValue(rs.getLong("sales_value"));
	}

        /**
         * �ڋq�����擾����B
         * @return �ڋq��
         */
        public Long getTargetCount() {
            return targetCount;
        }

        /**
         * �ڋq�����Z�b�g����B
         * @param targetCount �ڋq��
         */
        public void setTargetCount(Long targetCount) {
            this.targetCount = targetCount;
        }

        /**
         * ���X�񐔂��擾����B
         * @return ���X��
         */
        public Long getVisitCount() {
            return visitCount;
        }

        /**
         * ���X�񐔂��Z�b�g����B
         * @param visitCount ���X��
         */
        public void setVisitCount(Long visitCount) {
            this.visitCount = visitCount;
        }

        /**
         * ������z���擾����B
         * @return ������z
         */
        public Long getSalesValue() {
            return salesValue;
        }

        /**
         * ������z���Z�b�g����B
         * @param salesValue ������z
         */
        public void setSalesValue(Long salesValue) {
            this.salesValue = salesValue;
        }

        /**
         * ����V�F�A�����擾����B
         * @return ����V�F�A��
         */
        public String getSalesShareRate() {
            return salesShareRate;
        }

        /**
         * ����V�F�A�����Z�b�g����B
         * @param salesShareRate ����V�F�A��
         */
        public void setSalesShareRate(String salesShareRate) {
            this.salesShareRate = salesShareRate;
        }

        /**
         * �݌v����V�F�A�����擾����B
         * @return �݌v����V�F�A��
         */
        public String getTotalSalesShareRate() {
            return totalSalesShareRate;
        }

        /**
         * �݌v����V�F�A�����Z�b�g����B
         * @param totalSalesShareRate �݌v����V�F�A��
         */
        public void setTotalSalesShareRate(String totalSalesShareRate) {
            this.totalSalesShareRate = totalSalesShareRate;
        }

        /**
         * �q�P�����擾����B
         * @return �q�P��
         */
        public Long getAvgUnitPrice() {
            return avgUnitPrice;
        }

        /**
         * �q�P�����Z�b�g����B
         * @param avgUnitPrice �q�P��
         */
        public void setAvgUnitPrice(Long avgUnitPrice) {
            this.avgUnitPrice = avgUnitPrice;
        }

        /**
         * ���ϗ��X�񐔂��擾����B
         * @return ���ϗ��X��
         */
        public Double getAvgVisitCount() {
            return avgVisitCount;
        }

        /**
         * ���ϗ��X�񐔂��Z�b�g����B
         * @param avgVisitCount ���ϗ��X��
         */
        public void setAvgVisitCount(Double avgVisitCount) {
            this.avgVisitCount = avgVisitCount;
        }

        /**
         * ���ϗ��X�P�����擾����B
         * @return ���ϗ��X�P��
         */
        public Long getAvgVisitPrice() {
            return avgVisitPrice;
        }

        /**
         * ���ϗ��X�P�����Z�b�g����B
         * @param avgVisitPrice ���ϗ��X�P��
         */
        public void setAvgVisitPrice(Long avgVisitPrice) {
            this.avgVisitPrice = avgVisitPrice;
        }

        /**
         * �ڍׂ��擾����B
         * @return �ڍ�
         */
        public Object getMoreInfo() {
            return moreInfo;
        }

        /**
         * �ڍׂ��Z�b�g����B
         * @param moreInfo �ڍ�
         */
        public void setMoreInfo(Object moreInfo) {
            this.moreInfo = moreInfo;
        }
}
