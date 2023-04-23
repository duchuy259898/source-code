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
 * 商品データ
 * @author katagiri
 */
public class Product<T>
{
	/**
	 * 分類
	 */
	protected	ProductClass	productClass	=	new ProductClass();
	/**
	 * 商品ＩＤ
	 */
	protected	Integer			productID		=	null;
	/**
	 * 商品No.
	 */
	protected	String			productNo		=	"";
	/**
	 * 商品名
	 */
	protected	String			productName		=	"";
	/**
	 * 価格
	 */
	protected	Long			price			=	null;
	/**
	 * 元の価格
	 */
	private	Long			basePrice		=	null;
	/**
	 * 表示順
	 */
	protected	Integer			displaySeq		=	null;
	/**
	 * 元の表示順
	 */
	private	Integer			baseDisplaySeq		=	null;
	/**
	 * 作業時間
	 */
	protected	Integer			operationTime	=	null;

        protected  T sourceObject = null;
	
        /*
         * 賞美期限を使うかどうか
         **/
        protected       boolean         isPraiseTime                   =        false;
        
        /**
         * 賞美期限
         **/
        protected       Integer         praiseTimeLimit                 =       null;

        /** 業務用適正在庫 */
        protected		Integer			useProperStock					=		null;
        /** 店販用適正在庫 */
        protected		Integer			sellProperStock					=		null;
	/**
	 * カラー
	 */
	private	String	color		=	"";
	/**
	 * 表示フラグ
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
	 * コンストラクタ
	 */
	public Product()
	{
	}

	/**
	 * 分類を取得する。
	 * @return 分類
	 */
	public ProductClass getProductClass()
	{
		return productClass;
	}

	/**
	 * 分類を設定する。
	 * @param productClass 分類
	 */
	public void setProductClass(ProductClass productClass)
	{
		this.productClass = productClass;
	}
	
	/**
	 * 文字列表現（商品名）を取得する。
	 * @return 文字列表現（商品名）
	 */
	public String toString()
	{
		return	this.getProductName();
	}

	/**
	 * 商品ＩＤを取得する。
	 * @return 商品ＩＤ
	 */
	public Integer getProductID()
	{
		return productID;
	}

	/**
	 * 商品ＩＤを設定する。
	 * @param productID 商品ＩＤ
	 */
	public void setProductID(Integer productID)
	{
		this.productID = productID;
	}

	/**
	 * 商品No.を取得する。
	 * @return 商品No.
	 */
	public String getProductNo()
	{
		return productNo;
	}

	/**
	 * 商品No.を設定する。
	 * @param productNo 商品No.
	 */
	public void setProductNo(String productNo)
	{
		this.productNo = productNo;
	}

	/**
	 * 商品名を取得する。
	 * @return 商品名
	 */
	public String getProductName()
	{
		return productName;
	}

	/**
	 * 商品名を設定する。
	 * @param productName 商品名
	 */
	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	/**
	 * 価格を取得する。
	 * @return 価格
	 */
	public Long getPrice()
	{
		return price;
	}

	/**
	 * 価格を設定する。
	 * @param price 価格
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
	 * 表示順を取得する。
	 * @return 表示順
	 */
	public Integer getDisplaySeq()
	{
		return displaySeq;
	}

	/**
	 * 表示順を設定する。
	 * @param displaySeq 表示順
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
	 * 作業時間を取得する。
	 * @return 作業時間
	 */
	public Integer getOperationTime()
	{
		return operationTime;
	}

	/**
	 * 作業時間を設定する。
	 * @param operationTime 作業時間
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
	 * データを設定する。
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
