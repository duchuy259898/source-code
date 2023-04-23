/*
 * Product.java
 *
 * Created on 2006/05/26, 18:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.products;

import java.sql.*;

import com.geobeck.sql.*;

/**
 * ���i�f�[�^
 * @author katagiri
 */
public class Product<T>
{
	/**
	 * ����
	 */
	protected	ProductClass	productClass	=	new ProductClass();
	/**
	 * ���i�h�c
	 */
	protected	Integer			productID		=	null;
	/**
	 * ���iNo.
	 */
	protected	String			productNo		=	"";
	/**
	 * ���i��
	 */
	protected	String			productName		=	"";
	/**
	 * ���i
	 */
	protected	Long			price			=	null;
	/**
	 * ���̉��i
	 */
	private	Long			basePrice		=	null;
	/**
	 * �\����
	 */
	protected	Integer			displaySeq		=	null;
	/**
	 * ���̕\����
	 */
	private	Integer			baseDisplaySeq		=	null;
	/**
	 * ��Ǝ���
	 */
	protected	Integer			operationTime	=	null;

        protected  T sourceObject = null;
	
        /*
         * �ܔ��������g�����ǂ���
         **/
        protected       boolean         isPraiseTime                   =        false;
        
        /**
         * �ܔ�����
         **/
        protected       Integer         praiseTimeLimit                 =       null;

        /** �Ɩ��p�K���݌� */
        protected		Integer			useProperStock					=		null;
        /** �X�̗p�K���݌� */
        protected		Integer			sellProperStock					=		null;
	/**
	 * �J���[
	 */
	private	String	color		=	"";
	/**
	 * �\���t���O
	 */
        private boolean reserveFlg = false;
        // vtbphuong start add 20140612 Request #24859
        private Integer ispotMenuId = null ;

        public Integer getIspotMenuId() {
            return ispotMenuId;
        }

        public void setIspotMenuId(Integer ispotMenuId) {
            this.ispotMenuId = ispotMenuId;
        }
        
        // vtbphuong end add 20140612 Request #24859
        
        // add by ltthuc 2014/06/08
       // private Integer prepa_class_id = null;
        private Integer prepa_id;
        private Long prepaid_price;

        public Integer getPrepa_id() {
            return prepa_id;
        }

        public void setPrepa_id(Integer prepa_id) {
            this.prepa_id = prepa_id;
        }



        public Long getPrepaid_price() {
            return prepaid_price;
        }

        public void setPrepaid_price(Long prepaid_price) {
            this.prepaid_price = prepaid_price;
        }

        public T getSourceObject() {
            return sourceObject;
        }

        public void setSourceObject(T sourceObject) {
            this.sourceObject = sourceObject;
        }
	
	/**
	 * �R���X�g���N�^
	 */
	public Product()
	{
	}

	/**
	 * ���ނ��擾����B
	 * @return ����
	 */
	public ProductClass getProductClass()
	{
		return productClass;
	}

	/**
	 * ���ނ�ݒ肷��B
	 * @param productClass ����
	 */
	public void setProductClass(ProductClass productClass)
	{
		this.productClass = productClass;
	}
	
	/**
	 * ������\���i���i���j���擾����B
	 * @return ������\���i���i���j
	 */
	public String toString()
	{
		return	this.getProductName();
	}

	/**
	 * ���i�h�c���擾����B
	 * @return ���i�h�c
	 */
	public Integer getProductID()
	{
		return productID;
	}

	/**
	 * ���i�h�c��ݒ肷��B
	 * @param productID ���i�h�c
	 */
	public void setProductID(Integer productID)
	{
		this.productID = productID;
	}

	/**
	 * ���iNo.���擾����B
	 * @return ���iNo.
	 */
	public String getProductNo()
	{
		return productNo;
	}

	/**
	 * ���iNo.��ݒ肷��B
	 * @param productNo ���iNo.
	 */
	public void setProductNo(String productNo)
	{
		this.productNo = productNo;
	}

	/**
	 * ���i�����擾����B
	 * @return ���i��
	 */
	public String getProductName()
	{
		return productName;
	}

	/**
	 * ���i����ݒ肷��B
	 * @param productName ���i��
	 */
	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	/**
	 * ���i���擾����B
	 * @return ���i
	 */
	public Long getPrice()
	{
		return price;
	}

	/**
	 * ���i��ݒ肷��B
	 * @param price ���i
	 */
	public void setPrice(Long price)
	{
		this.price = price;
	}

        /**
         * @return the basePrice
         */
        public Long getBasePrice() {
            return basePrice;
        }

        /**
         * @param basePrice the basePrice to set
         */
        public void setBasePrice(Long basePrice) {
            this.basePrice = basePrice;
        }

	/**
	 * �\�������擾����B
	 * @return �\����
	 */
	public Integer getDisplaySeq()
	{
		return displaySeq;
	}

	/**
	 * �\������ݒ肷��B
	 * @param displaySeq �\����
	 */
	public void setDisplaySeq(Integer displaySeq)
	{
		this.displaySeq = displaySeq;
	}

        /**
         * @return the baseDisplaySeq
         */
        public Integer getBaseDisplaySeq() {
            return baseDisplaySeq;
        }

        /**
         * @param baseDisplaySeq the baseDisplaySeq to set
         */
        public void setBaseDisplaySeq(Integer baseDisplaySeq) {
            this.baseDisplaySeq = baseDisplaySeq;
        }

	/**
	 * ��Ǝ��Ԃ��擾����B
	 * @return ��Ǝ���
	 */
	public Integer getOperationTime()
	{
		return operationTime;
	}

	/**
	 * ��Ǝ��Ԃ�ݒ肷��B
	 * @param operationTime ��Ǝ���
	 */
	public void setOperationTime(Integer operationTime)
	{
		this.operationTime = operationTime;
	}

        public void setPraiseTime(boolean b){
            this.isPraiseTime = b;
        }

        public boolean isPraiseTime() {
            return isPraiseTime;
        }
        
        public void setPraiseTimeLimit(Integer i){
            this.praiseTimeLimit = i;
        }
        
        public Integer getPraiseTimeLimit(){
            return this.praiseTimeLimit;
        }

        public void setUseProperStock(Integer useProperStock)
        {
                this.useProperStock = useProperStock;
        }

        public Integer getUseProperStock()
        {
                return this.useProperStock;
        }

        public void setSellProperStock(Integer sellProperStock)
        {
                this.sellProperStock = sellProperStock;
        }

        public Integer getSellProperStock()
        {
                return this.sellProperStock;
        }

        /**
         * @return the color
         */
        public String getColor() {
            return color;
        }

        /**
         * @param color the color to set
         */
        public void setColor(String color) {
            this.color = color;
        }

        /**
         * @return the reserveFlg
         */
        public boolean isReserveFlg() {
            return reserveFlg;
        }

        /**
         * @param reserveFlg the reserveFlg to set
         */
        public void setReserveFlg(boolean reserveFlg) {
            this.reserveFlg = reserveFlg;
        }

	/**
	 * �f�[�^��ݒ肷��B
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setProductID(rs.getInt("product_id"));
		this.setProductNo(rs.getString("product_no"));
		this.setProductName(rs.getString("product_name"));
		this.setPrice(rs.getLong("price"));
		this.setBasePrice(rs.getLong("base_price"));
		this.setDisplaySeq(rs.getInt("display_seq"));
		this.setBaseDisplaySeq(rs.getInt("base_display_seq"));
		this.setOperationTime(rs.getInt("operation_time"));

	}
          public void setData(ResultSetWrapper rs,Integer productDivision) throws SQLException
	{
		this.setProductID(rs.getInt("product_id"));
		this.setProductNo(rs.getString("product_no"));
		this.setProductName(rs.getString("product_name"));
		this.setPrice(rs.getLong("price"));
		this.setBasePrice(rs.getLong("base_price"));
		this.setDisplaySeq(rs.getInt("display_seq"));
		this.setBaseDisplaySeq(rs.getInt("base_display_seq"));
		this.setOperationTime(rs.getInt("operation_time"));
                if(productDivision==2){
                this.setPrepaid_price(rs.getLong("prepaid_price"));
                this.setPrepa_id(rs.getInt("prepa_id"));}

	}

}
