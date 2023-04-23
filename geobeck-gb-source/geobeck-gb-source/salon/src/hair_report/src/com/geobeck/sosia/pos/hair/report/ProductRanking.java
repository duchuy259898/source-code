/*
 * ProductRanking.java
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
public class ProductRanking
{
	/**
	 * �Z�p���ޖ��E���i���ޖ�
	 */
	protected	String			className        = "";
	/**
	 * �Z�p���E���i��
	 */
	protected	String			prodName        = "";
        /**
	 * �P��
	 */
	protected	Long			unitPrice       = 0l;
        /**
	 * ����
	 */
	protected	Long			prodCount       = 0l;
        /**
	 * ���㍇�v
	 */
	protected	Long			prodSales       = 0l;
        /**
	 * �������z
	 */
	protected	Long			prodDiscount    = 0l;
        /**
	 * �䗦(����)
	 */
	protected	String			salesRatio      = "";
        /**
	 * �䗦(����)
	 */
	protected	String			countRatio      = "";

	/**
        * Creates a new instance of ProductRanking
        */
	public ProductRanking()
	{
	}
	
	/**
	 * ResultSetWrapper����f�[�^���擾����B
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
            setClassName(rs.getString("class_name"));
            setProdName(rs.getString("product_name"));
            setUnitPrice(rs.getLong("unit_price"));
            setProdCount(rs.getLong("product_num"));
            setProdSales(rs.getLong("sales_value"));
            setProdDiscount(rs.getLong("discount_value"));
	}

        /**
         * �Z�p���ޖ����擾����B
         * @return �Z�p���ޖ�
         */
        public String getClassName()
        {
        	return className;
        }

        /**
         * �Z�p���ޖ����Z�b�g����B
         * @param className �Z�p���ޖ�
         */
        public void setClassName(String className)
        {
        	this.className = className;
        }

        /**
         * �Z�p�����擾����B
         * @return �Z�p��
         */
        public String getProdName()
        {
        	return prodName;
        }

        /**
         * �Z�p�����Z�b�g����B
         * @param prodName �Z�p��
         */
        public void setProdName(String prodName)
        {
        	this.prodName = prodName;
        }

        /**
        * �P�����擾����B
        * @return �P��
        */
        public Long getUnitPrice()
        {
            return unitPrice;
        }

        /**
        * �P�����Z�b�g����B
        * @param unitPrice �P��
        */
        public void setUnitPrice(Long unitPrice)
        {
            this.unitPrice = unitPrice;
        }
        
        /**
        * ���ʂ��擾����B
        * @return ����
        */
        public Long getProdCount()
        {
            return prodCount;
        }

        /**
        * ���ʂ��Z�b�g����B
        * @param prodCount ����
        */
        public void setProdCount(Long prodCount)
        {
            this.prodCount = prodCount;
        }
        
        /**
        * ���㍇�v���擾����B
        * @return ���㍇�v
        */
        public Long getProdSales()
        {
            return prodSales;
        }

        /**
        * ���㍇�v���Z�b�g����B
        * @param prodSales ���㍇�v
        */
        public void setProdSales(Long prodSales)
        {
            this.prodSales = prodSales;
        }

        /**
        * �������z���擾����B
        * @return �������z
        */
        public Long getProdDiscount()
        {
            return prodDiscount;
        }

        /**
        * �������z���Z�b�g����B
        * @param prodDiscount �������z
        */
        public void setProdDiscount(Long prodDiscount)
        {
            this.prodDiscount = prodDiscount;
        }
        
        /**
         * �䗦(����)���擾����B
         * @return �䗦(����)
         */
        public String getSalesRatio()
        {
        	return salesRatio;
        }

        /**
         * �䗦(����)���Z�b�g����B
         * @param salesRatio �䗦(����)
         */
        public void setSalesRatio(String salesRatio)
        {
        	this.salesRatio = salesRatio;
        }
        
        /**
         * �䗦(����)���擾����B
         * @return �䗦(����)
         */
        public String getCountRatio()
        {
        	return countRatio;
        }

        /**
         * �䗦(����)���Z�b�g����B
         * @param countRatio �䗦(����)
         */
        public void setCountRatio(String countRatio)
        {
        	this.countRatio = countRatio;
        }
}
