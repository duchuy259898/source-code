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
public class ReportParameterBean
{
	public static final	int	MAIN_REPORT_BUSINESS		=	1;
	public static final	int	MAIN_REPORT_SALES			=	2;
	public static final	int	MAIN_REPORT_STAFF			=	3;
	public static final	int	SUB_REPORT_BUSINESS_ALL		=	1;
	public static final	int	SUB_REPORT_BUSINESS_TECHNIC	=	2;
	public static final	int	SUB_REPORT_BUSINESS_GOODS	=	3;
        public static final     int     SUB_REPORT_BUSINESS_COURSE      =       4; //ã∆ñ±ïÒçêÅ@ÉRÅ[ÉXè⁄ç◊
        public static final     int     SUB_REPORT_BUSINESS_CONSUMPTION =       5; //ã∆ñ±ïÒçêÅ@è¡âªÉRÅ[ÉX
	public static final	int	SUB_REPORT_SALES_DAY		=	1;
	public static final	int	SUB_REPORT_SALES_MONTH		=	2;
	public static final	int	SUB_REPORT_STAFF_TECHNIC	=	1;
	public static final	int	SUB_REPORT_STAFF_GOODS		=	2;
	public static final	int	TAX_TYPE_BLANK			=	1;
	public static final	int	TAX_TYPE_UNIT			=	2;
	public static final	int	TARGET_TYPE_DAY			=	1;
	public static final	int	TARGET_TYPE_MONTH		=	2;
//iida@geobeck
	public static final	int	DISCOUNT_TYPE_BLANK		=	1;	//ê≈î≤Ç´Ç©ÇÁäÑà¯
	public static final	int	DISCOUNT_TYPE_UNIT		=	0;	//ê≈çûÇ›Ç©ÇÁäÑà¯

	public static final	int	NEW_VISIT_CURRENT		=	0;
	public static final	int	NEW_VISIT_ALL                   =	1;

	public static final	int	STAFF_TYPE_MAIN			=	0;
	public static final	int	STAFF_TYPE_TECH			=	1;

    //IVS_vtnhan start add 20140723 MASHU_íSìñçƒóàï™êÕ
    public static final     int     NEW_DEVISION			=	1;
    public static final     int     NEW_ALL_DEVISION        =   2;
    //IVS_vtnhan end add 20140723 MASHU_íSìñçƒóàï™êÕ
	//IVS_LVTu start add 2014/09/03 MASHU_î≠íçèëçÏê¨(î[ïièëèoóÕ)
    private int slipNo;
    //IVS_LVTu end add 2014/09/03 MASHU_î≠íçèëçÏê¨(î[ïièëèoóÕ)

        
        private String  typeCondi   = "";

	private int		mainReportType;
	private int		subReportType;
	private String	shopIDList;
	private String	targetName;
	private int		taxType;
//iida@geobeck
        private int  cutoffDay;
	private int		discountType;
        private int displayPriceType; //201801 add
	private double	taxRate;
	private Integer	staffId;
	private String	staffNo;
	private String	staffName;
	private String	targetStartDate;
	private String	targetEndDate;
	private Date	targetStartDateObj;
	private Date	targetEndDateObj;
	private int		dayTargetType;
	private String	dayTargetStartDate;
	private String	dayTargetEndDate;
	private String	monthTargetYear;
	private String	monthTargetMonth;
	private String	targetStartYear;
	private String	targetStartMonth;
	private String	targetEndYear;
	private String	targetEndMonth;

	private String	calculationStartDate;
	private String	calculationEndDate;
	private Date	calculationStartDateObj;
	private Date	calculationEndDateObj;
// ryugou@logicalwave
        private int fixedCount;
        private String numberCondition;
        private int reappearanceCount;
        private String reappearanceCountMessage;
        private boolean sameTechnicClass;

        private String lastVisitDate;
        private String customerName;
        private String customerNo;
        private String isCharge;
        private String mailaddressEnable;
        private String nextVisitDate;
        private String comingCycle;
        private String lastSales;
        private String progressDay;
        private String visitCount;

        private int paymentClassID;
        private int newVisitType;
        private int staffType;
        // VTBPHUONG ADD 20120131
        private int technicClassID;
        private String technicClassName;
        // VTBPHUONG END ADD
        //An start add 20130320
        private int targetTechnic;
        private int targetCourse;
        private int targetDigestion;
        private int targetItem;
        private int year;
        private int month;
        private int shopId;
        //An end add 20130320
        
        //IVS_vtnhan start add 20140723 MASHU_íSìñçƒóàï™êÕ
        private int newDevision;
        private String listCategoryId;
        private int useShopCategory;
        private String listCategoryName;
        private String devisionName;
        private boolean isHideCategory;
        //IVS_vtnhan end add 20140723 MASHU_íSìñçƒóàï™êÕ
        
        //IVS_TTMLoan  start add 2014/07/28 Mashu_îÑè„êÑà⁄ï\
        private String categoryIDList;
        private String categoryNameList;
        //IVS_LVTu start add 2015/07/09 Bug #39505
        private boolean courseFlag = false;
        private boolean checkOutDate = false;

        public boolean isCheckOutDate() {
            return checkOutDate;
        }

        public void setCheckOutDate(boolean checkOutDate) {
            this.checkOutDate = checkOutDate;
        }

        public boolean isCourseFlag() {
            return courseFlag;
        }

        public void setCourseFlag(boolean courseFlag) {
            this.courseFlag = courseFlag;
        }
        //IVS_LVTu end add 2015/07/09 Bug #39505
        public String getCategoryIDList() {
            return categoryIDList;
        }

        public void setCategoryIDList(String categoryIDList) {
            this.categoryIDList = categoryIDList;
        }

        public String getCategoryNameList() {
            return categoryNameList;
        }

        public void setCategoryNameList(String categoryNameList) {
            this.categoryNameList = categoryNameList;
        }
        //IVS_TTMLoan  end add 2014/07/28 Mashu_îÑè„êÑà⁄ï\
        
        //IVS_Thanh  start add 2014/08/01 Mashu_çƒóàï™êÕ
        private Boolean newVisitCurrent = false;
        //nhanvt start add 20150123 New request #34998
        private Boolean diffShopCatVisit = false;
        //nhanvt start add 20150123 New request #34998
        public Boolean getNewVisitCurrent() {
            return newVisitCurrent;
        }

        public void setNewVisitCurrent(Boolean newVisitCurrent) {
            this.newVisitCurrent = newVisitCurrent;
        }
        private Boolean isGroupSelected = false; 
        public Boolean getIsGroupSelected() {
            return isGroupSelected;
        }

        public void setIsGroupSelected(Boolean isGroupSelected) {
            this.isGroupSelected = isGroupSelected;
        }
        //IVS_Thanh  end add 2014/08/01 Mashu_çƒóàï™êÕ
        
        
	/** Creates a new instance of ReportParameterBean */
	public ReportParameterBean() {
	}

        //An start add 20130320
          public int getShopId() {
              return shopId;
          }

          public void setShopId(int shopId) {
              this.shopId = shopId;
          }
        public int getTargetTechnic() {
            return targetTechnic;
        }

        public void setTargetTechnic(int targetTechnic) {
            this.targetTechnic = targetTechnic;
        }

        public int getTargetItem() {
            return targetItem;
        }

        public void setTargetItem(int targetItem) {
            this.targetItem = targetItem;
        }

        public int getTargetCourse() {
            return targetCourse;
        }

        public void setTargetCourse(int targetCourse) {
            this.targetCourse = targetCourse;
        }

        public int getTargetDigestion() {
            return targetDigestion;
        }

        public void setTargetDigestion(int targetDigestion) {
            this.targetDigestion = targetDigestion;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }


        //An end add 20130320

	public void setTypeCondi(String condi)
        {
            this.typeCondi = condi;
        }
        public String getTypeCondi()
        {
            return this.typeCondi;
        }
        public String getLastVisitDate() {
		return lastVisitDate;
	}
	public void setLastVisitDate(String lastVisitDate) {
		this.lastVisitDate = lastVisitDate;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getIsCharge() {
		return isCharge;
	}
	public void setIsCharge(String isCharge) {
		this.isCharge = isCharge;
	}
	public String getMailaddressEnable() {
		return mailaddressEnable;
	}
	public void setMailaddressEnable(String mailaddressEnable) {
		this.mailaddressEnable = mailaddressEnable;
	}
	public String getNextVisitDate() {
		return nextVisitDate;
	}
	public void setNextVisitDate(String nextVisitDate) {
		this.nextVisitDate = nextVisitDate;
	}
	public String getComingCycle() {
		return comingCycle;
	}
	public void setComingCycle(String comingCycle) {
		this.comingCycle = comingCycle;
	}
	public String getLastSales() {
		return lastSales;
	}
	public void setLastSales(String lastSales) {
		this.lastSales = lastSales;
	}
        public void setProgressDay(String s){
            progressDay = s;
        }
        public String getProgressDay(){
            return progressDay;
        }
        public void setVisitCount(String s){
            visitCount = s;
        }
        public String getVisitCount(){
            return visitCount;
        }

        public int getFixedCount(){
            return fixedCount;
        }

        public void setFixedCount(int c){
            this.fixedCount = c;
        }

        public String getNumberCondition(){
            return numberCondition;
        }

        public void setNumberCondition(String condition){
            this.numberCondition = condition;
        }

        public int getReappearanceCount(){
            return this.reappearanceCount;
        }

        public void setReappearanceCount(int s){
            this.reappearanceCount = s;
        }

        public String getReappearanceCountMessage(){
            return this.reappearanceCountMessage;
        }

        public void setReappearanceCountMessage(String s){
            this.reappearanceCountMessage = s;
        }

	public int getMainReportType() {
		return mainReportType;
	}

	public void setMainReportType(int mainReportType) {
		this.mainReportType = mainReportType;
	}

	public int getSubReportType() {
		return subReportType;
	}

	public void setSubReportType(int subReportType) {
		this.subReportType = subReportType;
	}

	public String getShopIDList()
	{
		return shopIDList;
	}

	public void setShopIDList(String shopIDList)
	{
		this.shopIDList = shopIDList;
	}

	public String getTargetName()
	{
		return targetName;
	}

	public void setTargetName(String targetName)
	{
		this.targetName = targetName;
	}

    public int getTaxType() {
	    return taxType;
    }

    public void setTaxType(int taxType) {
	    this.taxType = taxType;
    }

    public Integer getStaffId() {
	    return staffId;
    }

    public void setStaffId(Integer staffId) {
	    this.staffId = staffId;
    }

    public String getStaffNo() {
	    return staffNo;
    }

    public void setStaffNo(String staffNo) {
	    this.staffNo = staffNo;
    }

    public String getStaffName() {
	    return staffName;
    }

    public void setStaffName(String staffName) {
	    this.staffName = staffName;
    }

    public String getTargetStartDate() {
	    return targetStartDate;
    }

    public void setTargetStartDate(String targetStartDate) {
	    this.targetStartDate = targetStartDate;
    }

    public String getTargetEndDate() {
	    return targetEndDate;
    }

    public void setTargetEndDate(String targetEndDate) {
	    this.targetEndDate = targetEndDate;
    }

    public int getDayTargetType() {
	    return dayTargetType;
    }

    public void setDayTargetType(int dayTargetType) {
	    this.dayTargetType = dayTargetType;
    }

    public String getDayTargetStartDate() {
	    return dayTargetStartDate;
    }

    public void setDayTargetStartDate(String dayTargetStartDate) {
	    this.dayTargetStartDate = dayTargetStartDate;
    }

    public String getDayTargetEndDate() {
	    return dayTargetEndDate;
    }

    public void setDayTargetEndDate(String dayTargetEndDate) {
	    this.dayTargetEndDate = dayTargetEndDate;
    }

    public String getMonthTargetYear() {
	    return monthTargetYear;
    }

    public void setMonthTargetYear(String monthTargetYear) {
	    this.monthTargetYear = monthTargetYear;
    }

    public String getMonthTargetMonth() {
	    return monthTargetMonth;
    }

    public void setMonthTargetMonth(String monthTargetMonth) {
	    this.monthTargetMonth = monthTargetMonth;
    }

    public String getTargetStartYear() {
	    return targetStartYear;
    }

    public void setTargetStartYear(String targetStartYear) {
	    this.targetStartYear = targetStartYear;
    }

    public String getTargetStartMonth() {
	    return targetStartMonth;
    }

    public void setTargetStartMonth(String targetStartMonth) {
	    this.targetStartMonth = targetStartMonth;
    }

    public String getTargetEndYear() {
	    return targetEndYear;
    }

    public void setTargetEndYear(String targetEndYear) {
	    this.targetEndYear = targetEndYear;
    }

    public String getTargetEndMonth() {
	    return targetEndMonth;
    }

    public void setTargetEndMonth(String targetEndMonth) {
	    this.targetEndMonth = targetEndMonth;
    }

    public Date getTargetStartDateObj() {
	    return targetStartDateObj;
    }

    public void setTargetStartDateObj(Date targetStartDateObj) {
	    this.targetStartDateObj = targetStartDateObj;
    }

    public Date getTargetEndDateObj() {
	    return targetEndDateObj;
    }

    public void setTargetEndDateObj(Date targetEndDateObj) {
	    this.targetEndDateObj = targetEndDateObj;
    }

    public double getTaxRate() {
	    return taxRate;
    }

    public void setTaxRate(double taxRate) {
	    this.taxRate = taxRate;
    }

    public String getCalculationStartDate() {
	    return calculationStartDate;
    }

    public void setCalculationStartDate(String calculationStartDate) {
	    this.calculationStartDate = calculationStartDate;
    }

    public String getCalculationEndDate() {
	    return calculationEndDate;
    }

    public void setCalculationEndDate(String calculationEndDate) {
	    this.calculationEndDate = calculationEndDate;
    }

    public Date getCalculationStartDateObj() {
	    return calculationStartDateObj;
    }

    public void setCalculationStartDateObj(Date calculationStartDateObj) {
	    this.calculationStartDateObj = calculationStartDateObj;
    }

    public Date getCalculationEndDateObj() {
	    return calculationEndDateObj;
    }

    public void setCalculationEndDateObj(Date calculationEndDateObj) {
	    this.calculationEndDateObj = calculationEndDateObj;
    }
//ida@geobeck

    public int getDiscountType() {
	    return discountType;
    }

    public void setDiscountType(int discountType) {
	    this.discountType = discountType;
    }
    
    // 201801 add
    public int getDisplayPriceType() {
	    return displayPriceType;
    }
    // 201801 add
    public void setDisplayPriceType(int displayPriceType) {
	    this.displayPriceType = displayPriceType;
    }

    public boolean isSameTechnicClass() {
        return sameTechnicClass;
    }

    public void setSameTechnicClass(boolean sameTechnicClass) {
        this.sameTechnicClass = sameTechnicClass;
    }

    public int getPaymentClassID() {
        return paymentClassID;
    }

    public void setPaymentClassID(int paymentClassID) {
        this.paymentClassID = paymentClassID;
    }

    public int getCutoffDay() {
        return cutoffDay;
    }

    public void setCutoffDay(int cutoffDay) {
        this.cutoffDay = cutoffDay;
    }

    public int getNewVisitType() {
        return newVisitType;
    }

    public void setNewVisitType(int newVisitType) {
        this.newVisitType = newVisitType;
    }

    public int getStaffType() {
        return staffType;
    }

    public void setStaffType(int staffType) {
        this.staffType = staffType;
    }

    // VTBPHUONG ADD

    public Integer getTechnicClassID() {
	    return technicClassID;
    }

    public void setTechnicClassID(Integer technicClassID) {
	    this.technicClassID = technicClassID;
    }

    public String getTechnicClassName() {
	    return technicClassName;
    }

    public void setTechnicClassName(String technicClassName) {
	    this.technicClassName = technicClassName;
    }
    
    //IVS_vtnhan start add 20140723 MASHU_íSìñçƒóàï™êÕ
    public int getNewDevision() {
        return newDevision;
    }

    public void setNewDevision(int newDevision) {
        this.newDevision = newDevision;
    }
    
    public String getListCategoryId() {
        return listCategoryId;
    }

    public void setListCategoryId(String listCategoryId) {
        this.listCategoryId = listCategoryId;
    }
    
    public int getUseShopCategory() {
        return useShopCategory;
    }

    public void setUseShopCategory(int useShopCategory) {
        this.useShopCategory = useShopCategory;
    }
    
    public String getListCategoryName() {
        return listCategoryName;
    }

    public void setListCategoryName(String listCategoryName) {
        this.listCategoryName = listCategoryName;
    }
    
    public String getDevisionName() {
        return devisionName;
    }

    public void setDevisionName(String devisionName) {
        this.devisionName = devisionName;
    }
    
     public boolean isIsHideCategory() {
        return isHideCategory;
    }

    public void setIsHideCategory(boolean isHideCategory) {
        this.isHideCategory = isHideCategory;
    }
    //IVS_vtnhan end add 20140723 MASHU_íSìñçƒóàï™êÕ
	
	//IVS_LVTu start add 2014/09/03 MASHU_î≠íçèëçÏê¨(î[ïièëèoóÕ)
     public int getSlipNo() {
        return slipNo;
    }

    public void setSlipNo(int slipNo) {
        this.slipNo = slipNo;
    }
     //IVS_LVTu end add 2014/09/03 MASHU_î≠íçèëçÏê¨(î[ïièëèoóÕ)

    //nhanvt start add 20150123 New request #34998
    public Boolean getDiffShopCatVisit() {
        return diffShopCatVisit;
    }

    public void setDiffShopCatVisit(Boolean diffShopCatVisit) {
        this.diffShopCatVisit = diffShopCatVisit;
    }
    //nhanvt end add 20150123 New request #34998
}
