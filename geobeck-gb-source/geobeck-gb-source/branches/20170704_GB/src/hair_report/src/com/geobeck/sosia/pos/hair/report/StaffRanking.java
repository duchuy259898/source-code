/*
 * StaffRanking.java
 *
 * Created on 2008/07/20, 16:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report;

import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.sosia.pos.master.company.*;

/**
 * スタッフランキングデータ
 * @author saito
 */
public class StaffRanking
{
	/**
	 * スタッフ
	 */
	protected	MstStaff		staff               =	new MstStaff();
	/**
	 * 在籍店舗名
	 */
	protected	String			shopName            =	"";
        /**
	 * 総客数
	 */
	protected	Long			totalCount           =	0l;
        /**
	 * 技術客数
	 */
	protected	Long			techCount           =	0l;
        /**
	 * 技術売上
	 */
	protected	Long			techSales           =	0l;
        /**
	 * 指名客数
	 */
	protected	Long			designatedCount     =	0l;
        /**
	 * 指名売上
	 */
	protected	Long			designatedSales     =	0l;
	/**
	 * 商品売上
	 */
	protected	Long			itemSales	    =	0l;
        /**
	 * 技術新規客数
	 */
	protected	Long			newCount     =	0l;
        /**
	 * 技術新規紹介客数
	 */
	protected	Long			introduceCount     =	0l;
        /**
	 * アプローチ客数
	 */
	private	Long                            approachedCount    =	0l;
        /**
	 * アプローチ売上
	 */
	private	Long                            approachedSales    =	0l;
        //nhanvt start add 20150309 New request #35223
	private Long staffId ;
        protected Long	    repeatRate		=   0l;

        //Luc start add 20150625 #38408
        /**
	 * 昨年の総客数
	 */
        protected	Long			totalCountLast           =	0l;
        /**
	 * 昨年の技術客数
	 */
	protected	Long			techCountLast           =	0l;
        /**
	 * 昨年の技術売上
	 */
	protected	Long			techSalesLast           =	0l;
        /**
	 * 昨年の指名客数
	 */
	protected	Long			designatedCountLast     =	0l;
        /**
	 * 昨年の指名売上
	 */
	protected	Long			designatedSalesLast     =	0l;
	/**
	 * 昨年の商品売上
	 */
	protected	Long			itemSalesLast	    =	0l;
        /**
	 * 昨年の技術新規客数
	 */
	protected	Long			newCountLast     =	0l;
        /**
	 * 昨年の技術新規紹介客数
	 */
	protected	Long			introduceCountLast     =	0l;
        /**
	 * 昨年のアプローチ客数
	 */
	private	Long                            approachedCountLast    =	0l;
        /**
	 * 昨年のアプローチ売上
	 */
	private	Long                            approachedSalesLast    =	0l;

        //IVS_TMTrong start edit 2015-07-20 Bug #40595
        //protected Long	    repeatRateLast		=   0l;
        //IVS_TMTrong end edit 2015-07-20 Bug #40595
        private boolean  isLastYear;
        
        //IVS_TMTrong start add 2015-07-20 Bug #40595
        private Long repeat_count = 0l;
        private Long repeat_total= 0l;
        private Long repeat_count_last=0l;
        private Long repeat_total_last=0l;
        //IVS_TMTrong end add 2015-07-20 Bug #40595
        
        //LVTu start edit 2016/01/19 New request #46728
        protected	Long			courseCount                 =	0l;
        protected	Long			courseSales                 =	0l;
        protected	Long			courseDigestionSales        =	0l;
        
        protected	Long			courseCountLast              =	0l;
        protected	Long			courseSalesLast              =	0l;
        protected	Long			courseDigestionSalesLast     =	0l;
        
        //LVTu start edit 2016/02/05 New request #46728
        private boolean courseFlag = false;

        public boolean isCourseFlag() {
            return courseFlag;
        }

        public void setCourseFlag(boolean courseFlag) {
            this.courseFlag = courseFlag;
        }
        //LVTu end edit 2016/02/05 New request #46728

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

        private boolean pic;

        public boolean isPic() {
            return pic;
        }

        public void setPic(boolean pic) {
            this.pic = pic;
        }

        
        
        public void setIsLastYear(boolean isLastYear) {
            this.isLastYear = isLastYear;
        }
        
        //Luc end add 20150625 #38408
        public Long getStaffId() {
            return staffId;
        }

        public void setStaffId(Long staffId) {
            this.staffId = staffId;
        }
        
        //IVS_TMTrong start edit 2015-07-20 Bug #40595
//        public Long getRepeatRate() {
//            return repeatRate;
//        }
//
//        public void setRepeatRate(Long repeatRate) {
//            this.repeatRate = repeatRate;
//        }
        //IVS_TMTrong end edit 2015-07-20 Bug #40595
       //nhanvt end add 20150309 New request #35223
        
	/**
        * Creates a new instance of StaffRanking
        */
	public StaffRanking()
	{
	}
	
	/**
	 * ResultSetWrapperからデータを取得する。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
        
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		getStaff().setStaffName(0, rs.getString("staff_name1"));
		getStaff().setStaffName(1, rs.getString("staff_name2"));
		setShopName(rs.getString("shop_name"));
		setTotalCount(rs.getLong("totalCount"));
		setTechCount(rs.getLong("techCount"));
		setTechSales(rs.getLong("techSales"));
		setDesignatedCount(rs.getLong("designatedCount"));
		setDesignatedSales(rs.getLong("designatedSales"));
		setItemSales(rs.getLong("itemSales"));
		setNewCount(rs.getLong("newCount"));
		setIntroduceCount(rs.getLong("introduceCount"));
		setApproachedCount(rs.getLong("approachedCount"));
		setApproachedSales(rs.getLong("approachedSales"));
                //nhanvt start add 20150309 New request #35223
                setStaffId(rs.getLong("staff_id"));
                //IVS_TMTrong start add 2015-07-20 Bug #40595
                //setRepeatRate(rs.getLong("repeat_rate"));
                //IVS_TMTrong end add 2015-07-20 Bug #40595
                //nhanvt end add 20150309 New request #35223
                
               //Luc start add 20150625 #38408
                
                //IVS_TMTrong start add 2015-07-20 Bug #40595
                setCourseCount(rs.getLong("coursecount"));
                Double t = rs.getDouble("courseSales");
                setCourseSales(t.longValue());
                setCourseDigestionSales(((Double) rs.getDouble("course_digestionSales")).longValue());
                try {
                setRepeat_count(rs.getLong("repeat_count"));
                setRepeat_total(rs.getLong("repeat_total"));
                setRepeat_count_last(rs.getLong("repeat_count_last"));
                setRepeat_total_last(rs.getLong("repeat_total_last"));
                }catch(Exception e) {}
                //IVS_TMTrong end add 2015-07-20 Bug #40595
                
                //担当者を選択する。
                if(pic) {
                    //昨年
                    if(isIsLastYear()) {
                    setTotalCountLast(rs.getLong("totalCount_last"));
                    setTechCountLast(rs.getLong("techCount_last"));
                    setTechSalesLast(rs.getLong("techSales_last"));
                    setDesignatedCountLast(rs.getLong("designatedCount_last"));
                    setDesignatedSalesLast(rs.getLong("designatedSales_last"));
                    setItemSalesLast(rs.getLong("itemSales_last"));
                    setNewCountLast(rs.getLong("newCount_last"));
                    setIntroduceCountLast(rs.getLong("introduceCount_last"));
                    setApproachedCountLast(rs.getLong("approachedCount_last"));
                    setApproachedSalesLast(rs.getLong("approachedSales_last"));
                    //IVS_TMTrong start edit 2015-07-20 Bug #40595
                    //setRepeatRateLast(rs.getLong("repeat_rate_last"));
                    //IVS_TMTrong end edit 2015-07-20 Bug #40595
                    setCourseCountLast(rs.getLong("courseCount_last"));
                    setCourseSalesLast(rs.getLong("courseSales_last"));
                    setCourseDigestionSalesLast(((Double) rs.getDouble("course_digestionSales_last")).longValue());
                    }else {
                      //目標
                        setTotalCountLast(rs.getLong("totalCount_target"));
                        setTechCountLast(rs.getLong("techCount_target"));
                        setTechSalesLast(rs.getLong("techSales_target"));
                        setDesignatedCountLast(rs.getLong("designatedCount_target"));
                        setDesignatedSalesLast(rs.getLong("designatedSales_target"));
                        setItemSalesLast(rs.getLong("itemSales_target"));
                        setNewCountLast(rs.getLong("newCount_target"));
                        setIntroduceCountLast(rs.getLong("introduceCount_target"));
                        setApproachedCountLast(rs.getLong("approachedCount_target"));
                        setApproachedSalesLast(rs.getLong("approachedSales_target"));
                        //IVS_TMTrong start edit 2015-07-20 Bug #40595
                        //setRepeatRateLast(rs.getLong("repeat_rate_target"));
                        setCourseSalesLast(rs.getLong("courseSales_target"));
                        setCourseDigestionSalesLast(((Double) rs.getDouble("courseDigestionSales_target")).longValue());
                        try {
                        setRepeat_count_last(rs.getLong("repeat_count_last"));
                        }catch(Exception e) {}
                        //IVS_TMTrong end edit 2015-07-20 Bug #40595
                    }
                   //Luc end add 20150625 #38408
                }
                
	}
        
        //LVTu end edit 2016/01/19 New request #46728

	/**
	 * スタッフを取得する。
	 * @return スタッフ
	 */
	public MstStaff getStaff()
	{
		return staff;
	}

	/**
	 * スタッフをセットする。
	 * @param staff スタッフ
	 */
	public void setStaff(MstStaff staff)
	{
		this.staff = staff;
	}
	
	/**
	 * スタッフ名を取得する。
	 * @return スタッフ名
	 */
	public String getStaffName()
	{
		if(this.getStaff() != null)
		{
			return	this.getStaff().getFullStaffName();
		}
		
		return	null;
	}

	/**
	 * 在籍店舗名を取得する。
	 * @return 在籍店舗名
	 */
	public String getShopName()
	{
		return shopName;
	}

	/**
	 * 在籍店舗名をセットする。
	 * @param shopName 在籍店舗名
	 */
	public void setShopName(String shopName)
	{
		this.shopName = shopName;
	}

        /**
	 * 総客数を取得する。
	 * @return 総客数
	 */
	public Long getTotalCount()
	{
		return totalCount;
	}
        
        /**
	 * 総客数をセットする。
	 * @param totalCount 総客数
	 */
	public void setTotalCount(Long totalCount)
	{
		this.totalCount = totalCount;
	}
        
        /**
	 * 技術客数を取得する。
	 * @return 技術客数
	 */
	public Long getTechCount()
	{
		return techCount;
	}
        
        /**
	 * 技術客数をセットする。
	 * @param techCount 技術客数
	 */
	public void setTechCount(Long techCount)
	{
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
	 * 指名客数を取得する。
	 * @return 指名客数
	 */
	public Long getDesignatedCount()
	{
		return designatedCount;
	}

	/**
	 * 指名客数をセットする。
	 * @param designatedCount 指名客数
	 */
	public void setDesignatedCount(Long designatedCount)
	{
		this.designatedCount = designatedCount;
	}
	
	/**
	 * 指名売上を取得する。
	 * @return 指名売上
	 */
	public Long getDesignatedSales()
	{
		return designatedSales;
	}

	/**
	 * 指名売上をセットする。
	 * @param designatedSales 指名売上
	 */
	public void setDesignatedSales(Long designatedSales)
	{
		this.designatedSales = designatedSales;
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
	 * 新規客数を取得する。
	 * @return 新規客数
	 */
	public Long getNewCount()
	{
		return newCount;
	}
        
        /**
	 * 新規客数をセットする。
	 * @param newCount 新規客数
	 */
	public void setNewCount(Long newCount)
	{
		this.newCount = newCount;
	}
        
        /**
	 * 技術新規紹介客数を取得する。
	 * @return 技術新規紹介客数
	 */
	public Long getIntroduceCount()
	{
		return introduceCount;
	}
        
        /**
	 * 技術新規紹介客数をセットする。
	 * @param introduceCount 技術新規紹介客数
	 */
	public void setIntroduceCount(Long introduceCount)
	{
		this.introduceCount = introduceCount;
	}
        
	/**
	 * 総売上を取得する。
	 * @return 総売上
	 */
        //LVTu start edit 2016/02/05 New request #46728
	public Long getTotalSales()
	{
            if (courseFlag) {
                return techSales + itemSales + courseSales;
            }
		return techSales + itemSales;
	}

	/**
	 * 総売上を取得する。
	 * @return 総売上
	 */
	public Long getTotalSalesLast()
	{
            if (courseFlag) {
                return techSalesLast + itemSalesLast + courseSalesLast;
            }
		return techSalesLast + itemSalesLast;
	}
        //LVTu end edit 2016/02/05 New request #46728
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
	 * アプローチ客数を取得する。
	 * @return アプローチ客数
	 */
        public Long getApproachedCount() {
            return approachedCount;
        }

        /**
	 * アプローチ客数をセットする。
	 * @param techCount アプローチ客数
	 */
        public void setApproachedCount(Long approachedCount) {
            this.approachedCount = approachedCount;
        }

	/**
	 * アプローチ売上を取得する。
	 * @return アプローチ売上
	 */
        public Long getApproachedSales() {
            return approachedSales;
        }

	/**
	 * アプローチ売上をセットする。
	 * @param techSales アプローチ売上
	 */
        public void setApproachedSales(Long approachedSales) {
            this.approachedSales = approachedSales;
        }

        //Luc start add 20150625 #38408
        
        public Long getTotalCountLast() {
            return totalCountLast;
        }

        public void setTotalCountLast(Long totalCountLast) {
            this.totalCountLast = totalCountLast;
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

        public Long getDesignatedCountLast() {
            return designatedCountLast;
        }

        public void setDesignatedCountLast(Long designatedCountLast) {
            this.designatedCountLast = designatedCountLast;
        }

        public Long getDesignatedSalesLast() {
            return designatedSalesLast;
        }

        public void setDesignatedSalesLast(Long designatedSalesLast) {
            this.designatedSalesLast = designatedSalesLast;
        }

        public Long getItemSalesLast() {
            return itemSalesLast;
        }

        public void setItemSalesLast(Long itemSalesLast) {
            this.itemSalesLast = itemSalesLast;
        }

        public Long getNewCountLast() {
            return newCountLast;
        }

        public void setNewCountLast(Long newCountLast) {
            this.newCountLast = newCountLast;
        }

        public Long getIntroduceCountLast() {
            return introduceCountLast;
        }

        public void setIntroduceCountLast(Long introduceCountLast) {
            this.introduceCountLast = introduceCountLast;
        }

        public Long getApproachedCountLast() {
            return approachedCountLast;
        }

        public void setApproachedCountLast(Long approachedCountLast) {
            this.approachedCountLast = approachedCountLast;
        }

        public Long getApproachedSalesLast() {
            return approachedSalesLast;
        }

        public void setApproachedSalesLast(Long approachedSalesLast) {
            this.approachedSalesLast = approachedSalesLast;
        }
        
        //IVS_TMTrong start edit 2015-07-20 Bug #40595
//        public Long getRepeatRateLast() {
//            return repeatRateLast;
//        }
//
//        public void setRepeatRateLast(Long repeatRateLast) {
//            this.repeatRateLast = repeatRateLast;
//        }
        //IVS_TMTrong end edit 2015-07-20 Bug #40595
        //Luc end add 20150625 #38408

    /**
     * @return the repeat_count
     */
    public Long getRepeat_count() {
        return repeat_count;
    }

    /**
     * @param repeat_count the repeat_count to set
     */
    public void setRepeat_count(Long repeat_count) {
        this.repeat_count = repeat_count;
    }

    /**
     * @return the repeat_total
     */
    public Long getRepeat_total() {
        return repeat_total;
    }

    /**
     * @param repeat_total the repeat_total to set
     */
    public void setRepeat_total(Long repeat_total) {
        this.repeat_total = repeat_total;
    }

    /**
     * @return the repeat_count_last
     */
    public Long getRepeat_count_last() {
        return repeat_count_last;
    }

    /**
     * @param repeat_count_last the repeat_count_last to set
     */
    public void setRepeat_count_last(Long repeat_count_last) {
        this.repeat_count_last = repeat_count_last;
    }

    /**
     * @return the repeat_total_last
     */
    public Long getRepeat_total_last() {
        return repeat_total_last;
    }

    /**
     * @param repeat_total_last the repeat_total_last to set
     */
    public void setRepeat_total_last(Long repeat_total_last) {
        this.repeat_total_last = repeat_total_last;
    }
}
