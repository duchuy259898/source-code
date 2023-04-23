/*
 * ShopRanking.java
 *
 * Created on 2008/07/23, 21:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report;

import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;

import com.geobeck.sql.*;

/**
 * 店舗ランキングデータ
 * @author saito
 */
public class ShopRanking
{
	/**
	 * 店舗名
	 */
	protected	String	    shopName		=   "";
	/**
	 * 総客数
	 */
	private	Long	    totalCount		=   0l;
	/**
	 * 会員数
	 */
	private	Long	    customerCount	=   0l;
	/**
	 * 新規客数
	 */
	private	Long	    newCustomerCount	=   0l;
	/**
	 * 技術客数
	 */
	private	Long	    techCount		=   0l;
	/**
	 * 技術売上
	 */
	protected Long	    techSales		=   0l;
	/**
	 * 商品売上
	 */
	protected Long	    itemSales		=   0l;
	/**
	 * 総売上
	 */
	protected Long	    totalSales		=   0l;

        /**
	 * ギフト券利用
	 */
	protected Long	    totalPaymentValue		=   0l;
        /**
	 * 回数券販売
	 */
	protected Long	    totalProductValue1		=   0l;
        /**
	 * 回数券利用
	 */
	protected Long	    totalProductValue2		=   0l;

        /**
	 * スタッフ数
	 */
	protected Double	    totalStaff		=   0.0;

        /**
	 * ギフト券販売
	 */
	protected Long	    techSales1		=   0l;

        /**
	 * 店舗コード
	 */
	protected String	    prefixString		=   "";

        /**
	 * 技術新規比率
	 */
	protected String    newCustomerCountRatio = "";
        
        //nhanvt start add 20150309 New request #35223
        private Integer shopId ;
        protected Long	    repeatRate		=   0l;
        private	Long	    staffCount		=   0l;

        //Luc start add 20150625 #38408
        /**
	 * 昨年の総客数
	 */
	private	Long	    totalCountLast		=   0l;
	/**
	 * 昨年の会員数
	 */
	private	Long	    customerCountLast	=   0l;
	/**
	 * 昨年の新規客数
	 */
	private	Long	    newCustomerCountLast	=   0l;
	/**
	 * 昨年の技術客数
	 */
	private	Long	    techCountLast		=   0l;
	/**
	 * 昨年の技術売上
	 */
	protected Long	    techSalesLast		=   0l;
	/**
	 * 昨年の商品売上
	 */
	protected Long	    itemSalesLast		=   0l;
	/**
	 * 昨年の総売上
	 */
	protected Long	    totalSalesLast		=   0l;

        /**
	 * 昨年のギフト券利用
	 */
	protected Long	    totalPaymentValueLast		=   0l;
        /**
	 * 昨年の回数券販売
	 */
	protected Long	    totalProductValue1Last		=   0l;
        /**
	 * 昨年の回数券利用
	 */
	protected Long	    totalProductValue2Last		=   0l;

        /**
	 * 昨年のスタッフ数
	 */
	protected Double	    totalStaffLast		=   0.0;

        /**
	 * 昨年のギフト券販売
	 */
	protected Long	    techSales1Last		=   0l;

        
        protected Long	    repeatRateLast		=   0l;
        
        private	Long	    staffCountLast		=   0l;
        
        protected String    newCustomerCountRatioLast = "";
        //Luc end add 20150625 #38408
        private boolean isLastYear ;
        
        //IVS_TMTrong start add 20150721 Bug #40595
        private Long repeatCount = 0l;
        private Long repeatCountLast=0l;
        private Long repeatTotal = 0l;
        private Long repeatTotalLast = 0l;
        //IVS_TMTrong end add 20150721 Bug #40595
        
        //LVTu start edit 2016/01/19 New request #46728
        protected	Long			courseCount                 =	0l;
        protected	Long			courseSales                 =	0l;
        protected	Long			courseDigestionSales        =	0l;
        
        protected	Long			courseCountLast              =	0l;
        protected	Long			courseSalesLast              =	0l;
        protected	Long			courseDigestionSalesLast     =	0l;

        public Long getCourseCount() {
            return courseCount;
        }

        public void setCourseCount(Long courseCount) {
            this.courseCount = courseCount;
        }

        public Long getCourseSales() {
            return courseSales;
        }

        public void setCourseSales(Long courseSales) {
            this.courseSales = courseSales;
        }

        public Long getCourseDigestionSales() {
            return courseDigestionSales;
        }

        public void setCourseDigestionSales(Long courseDigestionSales) {
            this.courseDigestionSales = courseDigestionSales;
        }
        
         public Long getCourseCountLast() {
            return courseCountLast;
        }

        public void setCourseCountLast(Long courseCountLast) {
            this.courseCountLast = courseCountLast;
        }

        public Long getCourseSalesLast() {
            return courseSalesLast;
        }

        public void setCourseSalesLast(Long courseSalesLast) {
            this.courseSalesLast = courseSalesLast;
        }

        public Long getCourseDigestionSalesLast() {
            return courseDigestionSalesLast;
        }

        public void setCourseDigestionSalesLast(Long courseDigestionSalesLast) {
            this.courseDigestionSalesLast = courseDigestionSalesLast;
        }

        public boolean isIsLastYear() {
            return isLastYear;
        }

        public void setIsLastYear(boolean isLastYear) {
            this.isLastYear = isLastYear;
        }
        
        
        public Long getStaffCount() {
            return staffCount;
        }

        public void setStaffCount(Long staffCount) {
            this.staffCount = staffCount;
        }

        public Long getRepeatRate() {
            return repeatRate;
        }

        public void setRepeatRate(Long repeatRate) {
            this.repeatRate = repeatRate;
        }

        public Integer getShopId() {
            return shopId;
        }

        public void setShopId(Integer shopId) {
            this.shopId = shopId;
        }
        //nhanvt end add 20150309 New request #35223
	/**
        * Creates a new instance of ShopRanking
        */
	public ShopRanking()
	{
	}
	
	/**
	 * ResultSetWrapperからデータを取得する。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
                //setShopId(rs.getInt("shop_id"));
		setShopName(rs.getString("shop_name"));
		setTotalCount(rs.getLong("totalCount"));
		setCustomerCount(rs.getLong("customerCount"));
		setNewCustomerCount(rs.getLong("newCustomerCount"));
		setTechCount(rs.getLong("techCount"));
		setTechSales(rs.getLong("techSales"));
                setTechSales1(rs.getLong("techSales1"));
		setItemSales(rs.getLong("itemSales"));		
                setTotalPaymentValue(rs.getLong("totalPaymentValue"));
                setTotalProductValue1(rs.getLong("totalProductValue1"));
                setTotalProductValue2(rs.getLong("totalProductValue2"));
                setTotalStaff(rs.getDouble("totalStaff"));
                setPrefixString(rs.getString("prefix_string"));
                //IVS_TMTrong start add 20150721 Bug #40595
                setRepeatCount(rs.getLong("repeat_count"));
                setRepeatCountLast(rs.getLong("repeat_count_last"));
                setRepeatTotal(rs.getLong("repeat_total"));
                setRepeatTotalLast(rs.getLong("repeat_total_last"));
                //IVS_TMTrong end add 20150721 Bug #40595
	}
        
        /**
	 * ResultSetWrapperからデータを取得する。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setDataOld(ResultSetWrapper rs) throws SQLException
	{
                setShopId(rs.getInt("shop_id"));
		setShopName(rs.getString("shop_name"));
		setTotalCount(rs.getLong("totalCount"));
		setCustomerCount(rs.getLong("customerCount"));
		setNewCustomerCount(rs.getLong("newCustomerCount"));
		setTechCount(rs.getLong("techCount"));
		setTechSales(rs.getLong("techSales"));
		setItemSales(rs.getLong("itemSales"));
		setTotalSales(rs.getLong("totalSales"));
                //nhanvt start add 20150309 New request #35223
                setRepeatRate(rs.getLong("repeat_rate"));
                setStaffCount(rs.getLong("staff_count"));
                //nhanvt end add 20150309 New request #35223
                //IVS_TMTrong start add 20150721 Bug #40595
                setRepeatCount(rs.getLong("repeat_count"));
                setRepeatCountLast(rs.getLong("repeat_count_last"));
                setRepeatTotal(rs.getLong("repeat_total"));
                setRepeatTotalLast(rs.getLong("repeat_total_last"));
                //IVS_TMTrong end add 20150721 Bug #40595
                setCourseSales(rs.getLong("courseSales"));
                setCourseDigestionSales(((Double) rs.getDouble("course_digestionSales")).longValue());
                
                //Luc start add 20150625 #38408
                if(isIsLastYear()) {
                    setTotalCountLast(rs.getLong("totalCount_last"));
                    setCustomerCountLast(rs.getLong("customerCount_last"));
                    setNewCustomerCountLast(rs.getLong("newCustomerCount_last"));
                    setTechCountLast(rs.getLong("techCount_last"));
                    setTechSalesLast(rs.getLong("techSales_last"));
                    setItemSalesLast(rs.getLong("itemSales_last"));
                    setTotalSalesLast(rs.getLong("totalSales_last"));
                    setRepeatRateLast(rs.getLong("repeat_rate_last"));
                    setStaffCountLast(rs.getLong("staff_count_last"));
                    //IVS_TMTrong start add 20150721 Bug #40595
                    setRepeatCountLast(rs.getLong("repeat_count_last"));
                    setRepeatTotalLast(rs.getLong("repeat_total_last"));
                    //IVS_TMTrong end add 20150721 Bug #40595
                    setCourseSalesLast(rs.getLong("courseSales_last"));
                    setCourseDigestionSalesLast(((Double) rs.getDouble("course_digestion_Sales_last")).longValue());
                }else {
                   setTotalCountLast(rs.getLong("totalCount_target"));
                   setCustomerCountLast(rs.getLong("customerCount_target"));
                   setNewCustomerCountLast(rs.getLong("newCustomerCount_target"));
                   setTechCountLast(rs.getLong("techCount_target"));
                   setTechSalesLast(rs.getLong("techSales_target"));
                   setItemSalesLast(rs.getLong("itemSales_target"));
                   setTotalSalesLast(rs.getLong("totalSales_target"));
                   setRepeatRateLast(rs.getLong("repeat_rate_target"));
                   setStaffCountLast(rs.getLong("staff_count_target"));
                   
                   setCourseSalesLast(rs.getLong("courseSales_target"));
                   setCourseDigestionSalesLast(((Double) rs.getDouble("courseDigestionSales_target")).longValue());
                }
                //Luc end add 20150625 #38408
	}

        //LVTu end edit 2016/01/19 New request #46728
	/**
	 * 店舗名を取得する。
	 * @return 店舗名
	 */
	public String getShopName()
	{
		return shopName;
	}

	/**
	 * 店舗名をセットする。
	 * @param shopName 店舗名
	 */
	public void setShopName(String shopName)
	{
		this.shopName = shopName;
	}

	
	/**
	 * 総客数を取得する。
	 * @return 総客数
	 */
	public Long getTotalCount() {
		return totalCount;
	}

	/**
	 * 総客数をセットする。
	 * @param totalCount
	 */
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 会員数を取得する。
	 * @return 会員数
	 */
	public Long getCustomerCount() {
		return customerCount;
	}

	/**
	 * 会員数をセットする。
	 * @param customerCount
	 */
	public void setCustomerCount(Long customerCount) {
		this.customerCount = customerCount;
	}

	/**
	 * 新規客数を取得する。
	 * @return 新規客数
	 */
	public Long getNewCustomerCount() {
		return newCustomerCount;
	}

	/**
	 * 新規客数をセットする。
	 * @param newCustomerCount
	 */
	public void setNewCustomerCount(Long newCustomerCount) {
		this.newCustomerCount = newCustomerCount;
	}

	/**
	 * 技術客数を取得する。
	 * @return 技術客数
	 */
	public Long getTechCount() {
		return techCount;
	}

	/**
	 * 技術客数をセットする。
	 * @param techCount
	 */
	public void setTechCount(Long techCount) {
		this.techCount = techCount;
	}
	
	/**
	 * 技術売上を取得する。
	 * @return 技術売上
	 */
	public Long getTechSales()
	{
		return techSales;
	}

	/**
	 * 技術売上をセットする。
	 * @param techSales 技術売上
	 */
	public void setTechSales(Long techSales)
	{
		this.techSales = techSales;
	}
	
	/**
	 * 技術客単価を取得する。
	 * @return 技術客単価
	 */
	public Long getTechUnitValue()
	{
	    if (getTechCount() > 0)
	    {
		return getTechSales() / getTechCount();
	    }
	    else
	    {
		return 0l;
	    }
	}
        /**
	 * 技術客単価を取得する。
	 * @return 技術客単価
	 */
	public Long getTechUnitValueLast()
	{
	    if (getTechCountLast() > 0)
	    {
		return getTechSalesLast() / getTechCountLast();
	    }
	    else
	    {
		return 0l;
	    }
	}

        /**
	 * 技術客単価を取得する。
	 * @return 技術客単価
	 */
	public Double getOneSalesPerson()
	{
	    if (getTotalStaff() > 0)
	    {
		return getTotalSales() / getTotalStaff();
	    }
	    else
	    {
		return 0.0;
	    }
	}
	
	/**
	 * 商品売上を取得する。
	 * @return 商品売上
	 */
	public Long getItemSales()
	{
		return itemSales;
	}

	/**
	 * 商品売上をセットする。
	 * @param itemSales 商品売上
	 */
	public void setItemSales(Long itemSales)
	{
		this.itemSales = itemSales;
	}
	
	/**
	 * 総売上を取得する。
	 * @return 総売上
	 */
	public Long getTotalSales()
	{   
            if(!(SystemInfo.getDatabase().startsWith("pos_hair_nons") ||SystemInfo.getDatabase().startsWith("pos_hair_nons_bak"))){
                    return totalSales;
            }
            return getTechSales() + getTotalPaymentValue() + getTotalProductValue2() + getItemSales();
	}
        
	/**
	 * 総売上をセットする。
	 * @param totalSales 総売上
	 */
	public void setTotalSales(Long totalSales)
	{
		this.totalSales = totalSales;
	}

        /**
	 * ギフト券利用を取得する。
	 * @return ギフト券利用
	 */
	public Long getTotalPaymentValue()
	{
		return totalPaymentValue;
	}

	/**
	 * ギフト券利用をセットする。
	 * @param totalSales ギフト券利用
	 */
	public void setTotalPaymentValue(Long totalPaymentValue)
	{
		this.totalPaymentValue = totalPaymentValue;
	}

        /**
	 * 回数券販売を取得する。
	 * @return 回数券販売
	 */
	public Long getTotalProductValue1()
	{
		return totalProductValue1;
	}

	/**
	 * 回数券販売をセットする。
	 * @param totalSales 回数券販売
	 */
	public void setTotalProductValue1(Long totalProductValue1)
	{
		this.totalProductValue1 = totalProductValue1;
	}

        /**
	 * 回数券利用を取得する。
	 * @return 回数券利用
	 */
	public Long getTotalProductValue2()
	{
		return totalProductValue2;
	}

	/**
	 * 回数券利用をセットする。
	 * @param totalSales 回数券利用
	 */
	public void setTotalProductValue2(Long totalProductValue2)
	{
		this.totalProductValue2 = totalProductValue2;
	}

        /**
	 * スタッフ数を取得する。
	 * @return スタッフ数
	 */
	public Double getTotalStaff()
	{
		return totalStaff;
	}

	/**
	 * スタッフ数をセットする。
	 * @param totalSales スタッフ数
	 */
	public void setTotalStaff(Double totalStaff)
	{
		this.totalStaff = totalStaff;
	}

        /**
	 * ギフト券販売を取得する。
	 * @return ギフト券販売
	 */
	public Long getTechSales1()
	{
		return techSales1;
	}

	/**
	 * ギフト券販売をセットする。
	 * @param techSales ギフト券販売
	 */
	public void setTechSales1(Long techSales1)
	{
		this.techSales1 = techSales1;
	}

        /**
	 * 店舗コードを取得する。
	 * @return 店舗コード
	 */
	public String getPrefixString()
	{
		return prefixString;
	}

	/**
	 * 店舗コードをセットする。
	 * @param techSales 店舗コード
	 */
	public void setPrefixString(String prefixString)
	{
		this.prefixString = prefixString;
	}

	/**
	 * 客単価を取得する。
	 * @return 客単価
	 */
	public Long getUnitValue()
	{
	    if (getTotalCount() > 0)
	    {
		return getTotalSales() / getTotalCount();
	    }
	    else
	    {
		return 0l;
	    }
	}	

        /**
	 * 客単価を取得する。
	 * @return 客単価
	 */
	public Long getUnitValueLast()
	{
	    if (getTotalCountLast() > 0)
	    {
		return getTotalSalesLast() / getTotalCountLast();
	    }
	    else
	    {
		return 0l;
	    }
	}
        /**
         * 技術新規比率を取得する。
         * @return 技術新規比率
         */
        public String getNewCustomerCountRatio()
        {
        	return newCustomerCountRatio;
        }

        /**
         * 技術新規比率をセットする。
         * @param newCustomerCountRatio 技術新規比率
         */
        public void setNewCustomerCountRatio(String newCustomerCountRatio)
        {
        	this.newCustomerCountRatio = newCustomerCountRatio;
        }

        //Luc start add 20150625 #38408
        
        public Long getTotalCountLast() {
            return totalCountLast;
        }

        public void setTotalCountLast(Long totalCountLast) {
            this.totalCountLast = totalCountLast;
        }

        public Long getCustomerCountLast() {
            return customerCountLast;
        }

        public void setCustomerCountLast(Long customerCountLast) {
            this.customerCountLast = customerCountLast;
        }

        public Long getNewCustomerCountLast() {
            return newCustomerCountLast;
        }

        public void setNewCustomerCountLast(Long newCustomerCountLast) {
            this.newCustomerCountLast = newCustomerCountLast;
        }

        public Long getTechCountLast() {
            return techCountLast;
        }

        public void setTechCountLast(Long techCountLast) {
            this.techCountLast = techCountLast;
        }

        public Long getTechSalesLast() {
            return techSalesLast;
        }

        public void setTechSalesLast(Long techSalesLast) {
            this.techSalesLast = techSalesLast;
        }

        public Long getItemSalesLast() {
            return itemSalesLast;
        }

        public void setItemSalesLast(Long itemSalesLast) {
            this.itemSalesLast = itemSalesLast;
        }

        public Long getTotalSalesLast() {
            return totalSalesLast;
        }

        public void setTotalSalesLast(Long totalSalesLast) {
            this.totalSalesLast = totalSalesLast;
        }

        public Long getTotalPaymentValueLast() {
            return totalPaymentValueLast;
        }

        public void setTotalPaymentValueLast(Long totalPaymentValueLast) {
            this.totalPaymentValueLast = totalPaymentValueLast;
        }

        public Long getTotalProductValue1Last() {
            return totalProductValue1Last;
        }

        public void setTotalProductValue1Last(Long totalProductValue1Last) {
            this.totalProductValue1Last = totalProductValue1Last;
        }

        public Long getTotalProductValue2Last() {
            return totalProductValue2Last;
        }

        public void setTotalProductValue2Last(Long totalProductValue2Last) {
            this.totalProductValue2Last = totalProductValue2Last;
        }

        public Double getTotalStaffLast() {
            return totalStaffLast;
        }

        public void setTotalStaffLast(Double totalStaffLast) {
            this.totalStaffLast = totalStaffLast;
        }

        public Long getTechSales1Last() {
            return techSales1Last;
        }

        public void setTechSales1Last(Long techSales1Last) {
            this.techSales1Last = techSales1Last;
        }

        public Long getRepeatRateLast() {
            return repeatRateLast;
        }

        public void setRepeatRateLast(Long repeatRateLast) {
            this.repeatRateLast = repeatRateLast;
        }

        public Long getStaffCountLast() {
            return staffCountLast;
        }

        public void setStaffCountLast(Long staffCountLast) {
            this.staffCountLast = staffCountLast;
        }
        
        
        public String getNewCustomerCountRatioLast() {
            return newCustomerCountRatioLast;
        }

        public void setNewCustomerCountRatioLast(String newCustomerCountRatioLast) {
            this.newCustomerCountRatioLast = newCustomerCountRatioLast;
        }
        //Luc end add 20150625 #38408

    /**
     * @return the repeatCount
     */
    public Long getRepeatCount() {
        return repeatCount;
    }

    /**
     * @param repeatCount the repeatCount to set
     */
    public void setRepeatCount(Long repeatCount) {
        this.repeatCount = repeatCount;
    }

    /**
     * @return the repeatCountLast
     */
    public Long getRepeatCountLast() {
        return repeatCountLast;
    }

    /**
     * @param repeatCountLast the repeatCountLast to set
     */
    public void setRepeatCountLast(Long repeatCountLast) {
        this.repeatCountLast = repeatCountLast;
    }

    /**
     * @return the repeatTotal
     */
    public Long getRepeatTotal() {
        return repeatTotal;
    }

    /**
     * @param repeatTotal the repeatTotal to set
     */
    public void setRepeatTotal(Long repeatTotal) {
        this.repeatTotal = repeatTotal;
    }

    /**
     * @return the repeatTotalLast
     */
    public Long getRepeatTotalLast() {
        return repeatTotalLast;
    }

    /**
     * @param repeatTotalLast the repeatTotalLast to set
     */
    public void setRepeatTotalLast(Long repeatTotalLast) {
        this.repeatTotalLast = repeatTotalLast;
    }
}
