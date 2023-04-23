/*
 * ShopRankingList.java
 *
 * Created on 2008/07/23, 22:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report;

import com.geobeck.sosia.pos.report.bean.ReportParameterBean;
import java.util.*;
import java.util.logging.*;
import java.math.BigDecimal;
import java.text.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.system.SystemInfo;

/**
 * ìXï‹ÉâÉìÉLÉìÉOàÍóóèàóù
 * @author saito
 */
public class ShopRankingList extends ArrayList<ShopRanking>
{
        //ê≈ãÊï™
        private final	int	TAX_TYPE_BLANK				=	1;      //ê≈î≤
	private final	int	TAX_TYPE_UNIT				=	2;      //ê≈çû
        //ï\é¶îÕàÕ
	private final	int	RANGE_DISPLAY_TO_10                     =	0;	//Å`10à 
	private final	int	RANGE_DISPLAY_TO_20                     =	1;	//Å`20à 
	private final	int	RANGE_DISPLAY_ALL                       =	2;	//ëSÇƒ

        /**
	 * ëŒè€ä˙ä‘(äJénì˙)
	 */
	private	GregorianCalendar   termFrom    =   new GregorianCalendar();
	/**
	 * ëŒè€ä˙ä‘(èIóπì˙)
	 */
	private	GregorianCalendar   termTo      =   new GregorianCalendar();
	/**
	 * ê≈ãÊï™
	 */
	private	Integer	taxType          =	null;
	/**
	 * ï\é¶îÕàÕ
	 */
	private	Integer	rangeDisplay    =	null;
	/**
	 * ï\é¶èá
	 */
	private	Integer	orderDisplay      =	null;
	
        
        //nhanvt start add 20141201 New request #33406
        private String listCategoryId = null;
        private boolean hideCategory = false;
        private Integer useShopCategory = null;
        //nhanvt start add 20150309 New request #35223
        private boolean isLastYear = false;
        private boolean isMutiShop = false;
        private String calculationStartDate = "";
        private String calculationEndDate = "";
        private boolean isValue = false;
        private Integer month = null;
        private Integer year = null;
        private Integer countMonth = 1;
        //IVS_LVTu start edit 2015/04/14 Bug #36254
        
         private Integer countMonthLast = 1;
        /**
	 * ìXï‹IDÉäÉXÉg
	 */
	private	String   shopIDList             =   null;
        
        private boolean courseFlag = false;

        public boolean isCourseFlag() {
            return courseFlag;
        }

        public void setCourseFlag(boolean courseFlag) {
            this.courseFlag = courseFlag;
        }

    public String getShopIDList() {
        return shopIDList;
    }

    public void setShopIDList(String shopIDList) {
        this.shopIDList = shopIDList;
    }
    //IVS_LVTu end edit 2015/04/14 Bug #36254
    public Integer getCountMonth() {
        return countMonth;
    }

    public void setCountMonth(Integer countMonth) {
        this.countMonth = countMonth;
    }

    public Integer getCountMonthLast() {
        return countMonthLast;
    }

    public void setCountMonthLast(Integer countMonthLast) {
        this.countMonthLast = countMonthLast;
    }
    
        private String nameColumn = null;
        private String countDate = null;

    public String getCountDate() {
        return countDate;
    }

    public void setCountDate(String countDate) {
        this.countDate = countDate;
    }
        //nhanvt end add 20150309 New request #35223
        public String getListCategoryId() {
            return listCategoryId;
        }

        public void setListCategoryId(String listCategoryId) {
            this.listCategoryId = listCategoryId;
        }

        public boolean isHideCategory() {
            return hideCategory;
        }

        public void setHideCategory(boolean hideCategory) {
            this.hideCategory = hideCategory;
        }

        public Integer getUseShopCategory() {
            return useShopCategory;
        }

        public void setUseShopCategory(Integer useShopCategory) {
            this.useShopCategory = useShopCategory;
        }
	//nhanvt end add 20141201 New request #33406
        
        //nhanvt start add 20150309 New request #35223
        public String getNameColumn() {
        return nameColumn;
        }

        public void setNameColumn(String nameColumn) {
            this.nameColumn = nameColumn;
        }

        public Integer getMonth() {
            return month;
        }

        public void setMonth(Integer month) {
            this.month = month;
        }

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

        public boolean isIsValue() {
            return isValue;
        }

        public void setIsValue(boolean isValue) {
            this.isValue = isValue;
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

        public boolean isIsMutiShop() {
            return isMutiShop;
        }

        public void setIsMutiShop(boolean isMutiShop) {
            this.isMutiShop = isMutiShop;
        }
        public boolean isIsLastYear() {
            return isLastYear;
        }

        public void setIsLastYear(boolean isLastYear) {
            this.isLastYear = isLastYear;
        }
        //nhanvt end add 20150309 New request #35223
        
        
	/**
         * Creates a new instance of ShopRankingList
         */
	public ShopRankingList()
	{
	}
	
	/**
	 * ëŒè€ä˙ä‘(äJénì˙)ÇéÊìæÇ∑ÇÈÅB
	 * @return ëŒè€ä˙ä‘(äJénì˙)
	 */
        public String getTermFrom() {
                return SQLUtil.convertForSQLDateOnly(termFrom);
        }
	/**
	 * ëŒè€ä˙ä‘(äJénì˙)ÇÉZÉbÉgÇ∑ÇÈÅB
	 * @param termFrom ëŒè€ä˙ä‘(äJénì˙)
	 */
        public void setTermFrom(java.util.Date termFrom) {
                this.termFrom.setTime(termFrom);
        }
	/**
	 * ëŒè€ä˙ä‘(èIóπì˙)ÇéÊìæÇ∑ÇÈÅB
	 * @return ëŒè€ä˙ä‘(èIóπì˙)
	 */
        public String getTermTo() {
                return SQLUtil.convertForSQLDateOnly(termTo);
        }
	/**
	 * ëŒè€ä˙ä‘(èIóπì˙)ÇÉZÉbÉgÇ∑ÇÈÅB
	 * @param termTo ëŒè€ä˙ä‘(èIóπì˙)
	 */
        public void setTermTo(java.util.Date termTo) {
                this.termTo.setTime(termTo);
        }
	/**
	 * ê≈ãÊï™ÇéÊìæÇ∑ÇÈÅB
	 * @return ê≈ãÊï™
	 */
        public int getTaxType() {
                return taxType;
        }
	/**
	 * ê≈ãÊï™ÇÉZÉbÉgÇ∑ÇÈÅB
	 * @param taxType ê≈ãÊï™
	 */
        public void setTaxType(int taxType) {
                this.taxType = taxType;
        }
	/**
	 * ï\é¶îÕàÕÇéÊìæÇ∑ÇÈÅB
	 * @return ï\é¶îÕàÕ
	 */
        public int getRangeDisplay() {
                return rangeDisplay;
        }
	/**
	 * ï\é¶îÕàÕÇÉZÉbÉgÇ∑ÇÈÅB
	 * @param rangeDisplay ï\é¶îÕàÕ
	 */
        public void setRangeDisplay(int rangeDisplay) {
                this.rangeDisplay = rangeDisplay;
        }
	/**
	 * ï\é¶èáÇéÊìæÇ∑ÇÈÅB
	 * @return ï\é¶èá
	 */
        public int getOrderDisplay() {
                return orderDisplay;
        }
	/**
         * ï\é¶èáÇÉZÉbÉgÇ∑ÇÈÅB
         * @param orderDisplay ï\é¶èá
         */
        public void setOrderDisplay(int orderDisplay) {
                this.orderDisplay = orderDisplay;
        }

    /**
     * ÉfÅ[É^Çì«Ç›çûÇﬁÅB
     */
	public void load()
	{
		try
		{
                    //ÉâÉìÉLÉìÉOÉäÉXÉgéÊìæ
                    this.getShopRankingList();
                   
		}
		catch(Exception e)
		{
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
        
	
	/**
	 * ìXï‹ÉâÉìÉLÉìÉOÇÃÉäÉXÉgÇéÊìæÇ∑ÇÈÅB
	 * @exception Exception
	 */
	private void getShopRankingList() throws Exception
	{
		this.clear();
                boolean isNonsSystem = false;
		ResultSetWrapper rs = new ResultSetWrapper();
                if(!(SystemInfo.getDatabase().startsWith("pos_hair_nons") ||SystemInfo.getDatabase().startsWith("pos_hair_nons_bak"))){
                    isNonsSystem = true;
                    rs = SystemInfo.getConnection().executeQuery(this.getSelectShopRankingSQLOld());
                }else{
                    rs = SystemInfo.getConnection().executeQuery(this.getSelectShopRankingSQL());
                }
		while(rs.next())
		{
                    ShopRanking	temp = new ShopRanking();
                    if(isNonsSystem){
                        temp.setIsLastYear(this.isLastYear);
                        temp.setDataOld(rs);
                    }else{
                        temp.setData(rs);
                    }
                    this.add(temp);
		}

                BigDecimal bd = null;
                NumberFormat nf = NumberFormat.getInstance();
                nf.setMinimumFractionDigits(2);
                
                // ãZèpêVãKî‰ó¶
                String str = "0";
                for(ShopRanking temp : this) {
                    
                    if (temp.getTechCount() == 0) {
                        bd = new BigDecimal(0);
                        str = "0";
                    } else {
                        
                        double techCount = temp.getTechCount();
                        double newTechCount = temp.getNewCustomerCount();
                        
                        bd = new BigDecimal(newTechCount / techCount * 100);
                        str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                        str = str.substring(0, str.length()-3);
                        
                    }
                    temp.setNewCustomerCountRatio(str + "%");
                    
                    if (temp.getTechCountLast()== 0) {
                        bd = new BigDecimal(0);
                        str = "0";
                    } else {
                        
                        double techCountLast = temp.getTechCountLast();
                        double newTechCountLast = temp.getNewCustomerCountLast();
                        
                        bd = new BigDecimal(newTechCountLast / techCountLast * 100);
                        str = nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP));
                        str = str.substring(0, str.length()-3);
                        
                    }
                    temp.setNewCustomerCountRatioLast(str + "%");
                }

		rs.close();
	}
	/**
	 * ìXï‹ÉâÉìÉLÉìÉOíäèoópSQLÇéÊìæÇ∑ÇÈÅB
	 * @return ìXï‹ÉâÉìÉLÉìÉOíäèoópSQL
	 * @exception Exception
	 */
	private String getSelectShopRankingSQL() throws Exception
	{
	    StringBuilder sql = new StringBuilder(1000);
	    sql.append(" select");
	    sql.append("      mss.shop_name");
	    sql.append("     ,coalesce(totalCount, 0)        as totalCount");
	    sql.append("     ,coalesce(techCount, 0)         as techCount");
	    sql.append("     ,coalesce(newCustomerCount, 0)  as newCustomerCount");
	    sql.append("     ,coalesce(techSales, 0)         as techSales");            

            // ãZèpãqíPâøÅiÉ\Å[ÉgópÅj
	    sql.append("     ,coalesce(techSales, 0) / case when coalesce(techCount, 0) = 0 then 1 else coalesce(techCount, 0) end as techUnitSales");
            
	    sql.append("     ,coalesce(itemSales, 0)         as itemSales");
	    sql.append("     ,coalesce(techSales + coalesce(totalPaymentValue, 0) + coalesce(totalProductValue2, 0) + itemSales, 0)        as totalSales");
	    sql.append("     ,coalesce(customerCount, 0)     as customerCount");
            sql.append("     ,coalesce(techSales1, 0)         as techSales1");
            sql.append("     ,coalesce(totalPaymentValue, 0)         as totalPaymentValue");
            sql.append("     ,coalesce(totalProductValue1, 0)        as totalProductValue1");
            sql.append("     ,coalesce(totalProductValue2, 0)        as totalProductValue2");
            sql.append("     ,coalesce(totalStaff, 0)        as totalStaff");
            sql.append("     ,mss.prefix_string");
	    sql.append(" from");
	    sql.append("     (" + getSelectShopRankingValueSQL() + ") t");
	    sql.append("         join mst_shop mss");
	    sql.append("             using(shop_id)");
	    sql.append(" where");
	    sql.append("     mss.delete_date is null");
            //IVS_LVTu start edit 2015/04/14 Bug #36254
            sql.append("         and mss.shop_id in (" + getShopIDList() +")");
            //IVS_LVTu end edit 2015/04/14 Bug #36254
	    //ï\é¶èá
            if (getOrderDisplay() < 7)
	    {
		sql.append(" order by " + (getOrderDisplay() + 2) + " desc");
	    }
	    else
	    {
		// ãqíPâø
		sql.append(" order by case when coalesce(totalCount, 0) = 0 then 0 else coalesce(totalSales, 0) / coalesce(totalCount, 0) end desc");
	    }

	    //ï\é¶îÕàÕ
            if (getRangeDisplay() == RANGE_DISPLAY_TO_10)
            {
                   //Å`10à 
                   sql.append(" limit 10");
            }
            else if (getRangeDisplay() == RANGE_DISPLAY_TO_20)
            {
                   //Å`20à 
                   sql.append(" limit 20");
            }
            
            return sql.toString();
	}
	
        //LVTu start edit 2016/01/19 New request #46728
        /**
	 * ìXï‹ÉâÉìÉLÉìÉOíäèoópSQLÇéÊìæÇ∑ÇÈÅB
	 * @return ìXï‹ÉâÉìÉLÉìÉOíäèoópSQL
	 * @exception Exception
	 */
	private String getSelectShopRankingSQLOld() throws Exception
	{
	    StringBuilder sql = new StringBuilder(1000);
            sql.append(" select * from \n");
            sql.append(" (\n");
	    sql.append(" select");
            sql.append("      mss.shop_id, ");
	    sql.append("      mss.shop_name");
	    sql.append("     ,coalesce(totalCount, 0)        as totalCount");
	    sql.append("     ,coalesce(techCount, 0)         as techCount");
	    sql.append("     ,coalesce(newCustomerCount, 0)  as newCustomerCount");
	    sql.append("     ,coalesce(techSales, 0)         as techSales");

            // ãZèpãqíPâøÅiÉ\Å[ÉgópÅj
	    sql.append("     ,coalesce(techSales, 0) / case when coalesce(techCount, 0) = 0 then 1 else coalesce(techCount, 0) end as techUnitSales");
            
	    sql.append("     ,coalesce(itemSales, 0)         as itemSales");
            
            sql.append("     ,coalesce(courseSales, 0) AS courseSales");
            sql.append("     ,coalesce(course_digestionSales, 0) as course_digestionSales");
            
	    sql.append("     ,coalesce(totalSales, 0)        as totalSales");
	    sql.append("     ,coalesce(customerCount, 0)     as customerCount");
            //sql.append("     ,repeat_rate ");
            //nhanvt start add 20150309 New request #35223
             sql.append( "          ,coalesce((select count(distinct slip_no) ");
             sql.append( "                from view_data_sales_detail_valid ds ");
             sql.append( "                where ds.sales_date between  " + getTermFrom() + " and date " + getTermTo() + "\n");
             sql.append( "                and ds.shop_id = t.shop_id  ");
             sql.append( "                AND EXISTS ");
             sql.append( "                          (SELECT 1 ");
             sql.append( "                           FROM data_sales_detail dsd ");
             sql.append( "                           INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
             sql.append( "                           LEFT JOIN mst_technic_class mstc ON mstc.technic_class_id = mst.technic_class_id ");
             sql.append( "                           WHERE dsd.shop_id = ds.shop_id ");
             sql.append( "                             AND dsd.slip_no = ds.slip_no ");
             sql.append( "                             AND dsd.slip_detail_no = ds.slip_detail_no ");
             sql.append( "                             AND dsd.delete_date IS NULL ");
             sql.append( "                             AND dsd.product_division IN (1,3) ");
             if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");
             }
                                                       
             sql.append( "                           UNION ALL SELECT 1");
             sql.append( "                           FROM data_sales_detail dsd");
             sql.append( "                           INNER JOIN mst_item mi ON dsd.product_id = mi.item_id");
             sql.append( "                           AND mi.delete_date IS NULL");
             sql.append( "                           LEFT JOIN mst_item_class mic ON mic.item_class_id = mi.item_class_id");
             sql.append( "                           AND mic.delete_date IS NULL");
             sql.append( "                           WHERE dsd.shop_id = ds.shop_id");
             sql.append( "                             AND dsd.slip_no = ds.slip_no");
             sql.append( "                             AND dsd.slip_detail_no = ds.slip_detail_no");
             sql.append( "                             AND dsd.delete_date IS NULL");
             sql.append( "                             AND dsd.product_division IN (2,4)");
             if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");
             }
                                                              
             sql.append( "                           UNION ALL SELECT 1");
             sql.append( "                           FROM data_sales_detail dsd");
             sql.append( "                           INNER JOIN mst_course msc ON msc.course_id = dsd.product_id");
             sql.append( "                           LEFT JOIN mst_course_class mscc ON mscc.course_class_id = msc.course_class_id");
             sql.append( "                           WHERE dsd.shop_id = ds.shop_id");
             sql.append( "                             AND dsd.slip_no = ds.slip_no");
             sql.append( "                             AND dsd.slip_detail_no = ds.slip_detail_no");
             sql.append( "                             AND dsd.delete_date IS NULL");
             
             if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                sql.append("                  AND mscc.shop_category_id in (" + this.getListCategoryId() + ") ");
             }
             sql.append( "                             AND dsd.product_division IN (5,6))");
             sql.append( "                         And exists (select 1 from data_sales ds1 ");
             sql.append( "                                     where ds1.customer_id = ds.customer_id ");
             if(isIsMutiShop()){
                sql.append( "                                      and ds1.shop_id = ds.shop_id");
             }
             
            sql.append( "    and ds1.sales_date <= ds.sales_date - interval '"+getCountDate()+"' ");
            sql.append( "    	    and ds1.sales_date > ds.sales_date - interval '"+getCountDate()+"' - interval '1 month' ");

            sql.append( "                          )");
            sql.append( "                 ),0)  as repeat_rate ");
            //IVS_TMTrong start add 20150721 Bug #40595
            //Luc start edit 20150724 #40595
            //sql.append( "          ,coalesce((select count(distinct slip_no) ");
            sql.append( "          ,coalesce((select count(distinct customer_id) ");
            //Luc start edit 20150724 #40595
             sql.append( "                from view_data_sales_detail_valid ds ");
             sql.append( "                where ds.sales_date between  " + getTermFrom() + " and date " + getTermTo() + "\n");
             sql.append("                AND ds.shop_id = t.shop_id");
             //IVS_TMTrong end edit 2015/08/11 Bug #41292
             //IVS_TMTrong start edit 2015/08/10 Bug #41292
            if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                sql.append( "                AND EXISTS ");
                sql.append( "                          (SELECT 1 ");
                sql.append( "                           FROM data_sales_detail dsd ");
                sql.append( "                           INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
                sql.append( "                           LEFT JOIN mst_technic_class mstc ON mstc.technic_class_id = mst.technic_class_id ");
                sql.append( "                           WHERE dsd.shop_id = ds.shop_id ");
                sql.append( "                             AND dsd.slip_no = ds.slip_no ");
                sql.append( "                             AND dsd.slip_detail_no = ds.slip_detail_no ");
                sql.append( "                             AND dsd.delete_date IS NULL ");
                sql.append( "                             AND dsd.product_division IN (1,3) ");
                sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");

                sql.append( "                           UNION ALL SELECT 1");
                sql.append( "                           FROM data_sales_detail dsd");
                sql.append( "                           INNER JOIN mst_item mi ON dsd.product_id = mi.item_id");
                sql.append( "                           AND mi.delete_date IS NULL");
                sql.append( "                           LEFT JOIN mst_item_class mic ON mic.item_class_id = mi.item_class_id");
                sql.append( "                           AND mic.delete_date IS NULL");
                sql.append( "                           WHERE dsd.shop_id = ds.shop_id");
                sql.append( "                             AND dsd.slip_no = ds.slip_no");
                sql.append( "                             AND dsd.slip_detail_no = ds.slip_detail_no");
                sql.append( "                             AND dsd.delete_date IS NULL");
                sql.append( "                             AND dsd.product_division IN (2,4)");
                sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");

                sql.append( "                           UNION ALL SELECT 1");
                sql.append( "                           FROM data_sales_detail dsd");
                sql.append( "                           INNER JOIN mst_course msc ON msc.course_id = dsd.product_id");
                sql.append( "                           LEFT JOIN mst_course_class mscc ON mscc.course_class_id = msc.course_class_id");
                sql.append( "                           WHERE dsd.shop_id = ds.shop_id");
                sql.append( "                             AND dsd.slip_no = ds.slip_no");
                sql.append( "                             AND dsd.slip_detail_no = ds.slip_detail_no");
                sql.append( "                             AND dsd.delete_date IS NULL");
                sql.append("                  AND mscc.shop_category_id in (" + this.getListCategoryId() + ") ");
                sql.append( "                             AND dsd.product_division IN (5,6))");
              }
             sql.append( "                 And exists (select 1 from data_sales ds1 ");
             sql.append( "                                     where ds1.customer_id = ds.customer_id ");
            if(isIsMutiShop()){
                sql.append("                AND ds1.shop_id = t.shop_id");
            }
            //Luc start edit 20150724 #40595
            //sql.append( "    and ds1.sales_date <= ds.sales_date - interval '"+getCountDate()+"' ");
            //sql.append( "    	    and ds1.sales_date > ds.sales_date - interval '"+getCountDate()+"' - interval '1 month' ");
            sql.append( "    and ds1.sales_date <  to_date("+getTermFrom()+",'YYYY/MM/dd')");
            sql.append( "    	    and ds1.sales_date >=  to_date("+getTermFrom()+",'YYYY/MM/dd') - interval '"+getCountDate()+"'");
            //Luc end edit 20150724 #40595
            sql.append( "                          )");
            sql.append( "                 ),0)  as repeat_count ");
            
            //IVS_TMTrong edit add 2015/08/10 Bug #41292
            sql.append(", ( ");
            sql.append("     SELECT count(distinct(ds.customer_id)) ");	
            sql.append("     FROM view_data_sales_detail_valid ds ");
            sql.append("     WHERE ds.sales_date < to_date("+getTermFrom()+",'YYYY/MM/dd') ");
            sql.append("     AND ds.sales_date >=  to_date("+getTermFrom()+",'YYYY/MM/dd') - interval '"+getCountDate()+"' ");
           
            if(isIsMutiShop()){
                sql.append("                AND ds.shop_id = t.shop_id");
            }

             if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
             sql.append( "                AND EXISTS ");
             sql.append( "                          (SELECT 1 ");
             sql.append( "                           FROM data_sales_detail dsd ");
             sql.append( "                           INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
             sql.append( "                           LEFT JOIN mst_technic_class mstc ON mstc.technic_class_id = mst.technic_class_id ");
             sql.append( "                           WHERE dsd.shop_id = ds.shop_id ");
             sql.append( "                             AND dsd.slip_no = ds.slip_no ");
             sql.append( "                             AND dsd.slip_detail_no = ds.slip_detail_no ");
             sql.append( "                             AND dsd.delete_date IS NULL ");
             sql.append( "                             AND dsd.product_division IN (1,3) ");
             sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");
                                                       
             sql.append( "                           UNION ALL SELECT 1");
             sql.append( "                           FROM data_sales_detail dsd");
             sql.append( "                           INNER JOIN mst_item mi ON dsd.product_id = mi.item_id");
             sql.append( "                           AND mi.delete_date IS NULL");
             sql.append( "                           LEFT JOIN mst_item_class mic ON mic.item_class_id = mi.item_class_id");
             sql.append( "                           AND mic.delete_date IS NULL");
             sql.append( "                           WHERE dsd.shop_id = ds.shop_id");
             sql.append( "                             AND dsd.slip_no = ds.slip_no");
             sql.append( "                             AND dsd.slip_detail_no = ds.slip_detail_no");
             sql.append( "                             AND dsd.delete_date IS NULL");
             sql.append( "                             AND dsd.product_division IN (2,4)");
             sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");
                                                              
             sql.append( "                           UNION ALL SELECT 1");
             sql.append( "                           FROM data_sales_detail dsd");
             sql.append( "                           INNER JOIN mst_course msc ON msc.course_id = dsd.product_id");
             sql.append( "                           LEFT JOIN mst_course_class mscc ON mscc.course_class_id = msc.course_class_id");
             sql.append( "                           WHERE dsd.shop_id = ds.shop_id");
             sql.append( "                             AND dsd.slip_no = ds.slip_no");
             sql.append( "                             AND dsd.slip_detail_no = ds.slip_detail_no");
             sql.append( "                             AND dsd.delete_date IS NULL");
             sql.append("                  AND mscc.shop_category_id in (" + this.getListCategoryId() + ") ");
             sql.append( "                             AND dsd.product_division IN (5,6))");
             }
            //IVS_TMTrong end edit 2015/08/10 Bug #41292
             sql.append(" ) AS repeat_total ");
            //IVS_TMTrong end add 20150721 Bug #40595
            sql.append("     ,coalesce(staff_count,0) as staff_count ");
            
            //IVS_TMTrong start add 2015/08/07 Bug #41295
            if (courseFlag) {
                sql.append(" ,coalesce(techCount_last,0) + coalesce(itemOnlyCount_last,0) + coalesce(courseCount_last,0) AS totalCount_last ");
            }else {
                sql.append(" ,coalesce(techCount_last,0) + coalesce(itemOnlyCount_last,0) AS totalCount_last ");
            }
            sql.append(" ,coalesce(techCount_last, 0) AS techCount_last ");
            sql.append(" ,coalesce(newCustomerCount_last, 0) AS newCustomerCount_last ");
            sql.append(" ,coalesce(techSales_last, 0) AS techSales_last ");
            sql.append(" ,coalesce(techSales_last, 0) / CASE  ");
            sql.append("    WHEN coalesce(techCount_last, 0) = 0 ");
            sql.append("    THEN 1 ");
            sql.append("    ELSE coalesce(techCount_last, 0) ");
            sql.append("    END AS techUnitSales_last ");
            sql.append(" ,coalesce(itemSales_last, 0) AS itemSales_last ");
            
            sql.append(" ,coalesce(courseSales_last, 0) AS courseSales_last ");
            sql.append(" ,coalesce(course_digestion_Sales_last, 0) AS course_digestion_Sales_last ");
            
            sql.append(" ,coalesce(techSales_last,0) + coalesce(itemSales_last, 0) + coalesce(courseSales_last, 0) AS totalSales_last ");
            sql.append(" ,coalesce(customerCount_last, 0) AS customerCount_last ");
            sql.append(" ,coalesce(( ");
            sql.append("    SELECT count(DISTINCT slip_no) ");
            sql.append("    FROM view_data_sales_detail_valid ds ");
            sql.append("    WHERE ds.sales_date BETWEEN DATE "+getTermFrom()+" - interval '1 year' ");
            sql.append("    AND DATE "+getTermTo()+" - interval '1 year' ");
             if(isIsMutiShop()){
                sql.append("                AND ds.shop_id = t.shop_id");
            }
            if (this.getListCategoryId() != null && this.getListCategoryId() != "") {                           
                sql.append( "                AND EXISTS ");
                sql.append( "                          (SELECT 1 ");
                sql.append( "                           FROM data_sales_detail dsd ");
                sql.append( "                           INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
                sql.append( "                           LEFT JOIN mst_technic_class mstc ON mstc.technic_class_id = mst.technic_class_id ");
                sql.append( "                           WHERE dsd.shop_id = ds.shop_id ");
                sql.append( "                             AND dsd.slip_no = ds.slip_no ");
                sql.append( "                             AND dsd.slip_detail_no = ds.slip_detail_no ");
                sql.append( "                             AND dsd.delete_date IS NULL ");
                sql.append( "                             AND dsd.product_division IN (1,3) ");
                sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");

                sql.append( "                           UNION ALL SELECT 1");
                sql.append( "                           FROM data_sales_detail dsd");
                sql.append( "                           INNER JOIN mst_item mi ON dsd.product_id = mi.item_id");
                sql.append( "                           AND mi.delete_date IS NULL");
                sql.append( "                           LEFT JOIN mst_item_class mic ON mic.item_class_id = mi.item_class_id");
                sql.append( "                           AND mic.delete_date IS NULL");
                sql.append( "                           WHERE dsd.shop_id = ds.shop_id");
                sql.append( "                             AND dsd.slip_no = ds.slip_no");
                sql.append( "                             AND dsd.slip_detail_no = ds.slip_detail_no");
                sql.append( "                             AND dsd.delete_date IS NULL");
                sql.append( "                             AND dsd.product_division IN (2,4)");
                sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");

                sql.append( "                           UNION ALL SELECT 1");
                sql.append( "                           FROM data_sales_detail dsd");
                sql.append( "                           INNER JOIN mst_course msc ON msc.course_id = dsd.product_id");
                sql.append( "                           LEFT JOIN mst_course_class mscc ON mscc.course_class_id = msc.course_class_id");
                sql.append( "                           WHERE dsd.shop_id = ds.shop_id");
                sql.append( "                             AND dsd.slip_no = ds.slip_no");
                sql.append( "                             AND dsd.slip_detail_no = ds.slip_detail_no");
                sql.append( "                             AND dsd.delete_date IS NULL");
                sql.append("                  AND mscc.shop_category_id in (" + this.getListCategoryId() + ") ");
                sql.append( "                             AND dsd.product_division IN (5,6))");
            }                      
            sql.append("                        AND EXISTS ( ");
            sql.append("                        SELECT 1 ");
            sql.append("                        FROM data_sales ds1 ");
            sql.append("                        WHERE ds1.customer_id = ds.customer_id ");
             if(isIsMutiShop()){
                    sql.append( "               and ds1.shop_id = ds.shop_id");
             }
            sql.append("                        AND ds1.sales_date <= ds.sales_date - interval '"+getCountDate()+"' ");
            sql.append("                        AND ds1.sales_date > ds.sales_date - interval '"+getCountDate()+"' - interval '1 month') ");
            sql.append("), 0) AS repeat_rate_last ");
            
            sql.append(" ,coalesce(( ");
            sql.append("    SELECT count(DISTINCT customer_id) ");
            sql.append("    FROM view_data_sales_detail_valid ds ");
            sql.append("    WHERE ds.sales_date BETWEEN DATE "+getTermFrom()+" - interval '1 year' ");
            sql.append("    AND DATE "+getTermTo()+" - interval '1 year' ");
            sql.append("                AND ds.shop_id = t.shop_id");
            if (this.getListCategoryId() != null && this.getListCategoryId() != "") {                           
                sql.append( "                AND EXISTS ");
                sql.append( "                          (SELECT 1 ");
                sql.append( "                           FROM data_sales_detail dsd ");
                sql.append( "                           INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
                sql.append( "                           LEFT JOIN mst_technic_class mstc ON mstc.technic_class_id = mst.technic_class_id ");
                sql.append( "                           WHERE dsd.shop_id = ds.shop_id ");
                sql.append( "                             AND dsd.slip_no = ds.slip_no ");
                sql.append( "                             AND dsd.slip_detail_no = ds.slip_detail_no ");
                sql.append( "                             AND dsd.delete_date IS NULL ");
                sql.append( "                             AND dsd.product_division IN (1,3) ");
                sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");

                sql.append( "                           UNION ALL SELECT 1");
                sql.append( "                           FROM data_sales_detail dsd");
                sql.append( "                           INNER JOIN mst_item mi ON dsd.product_id = mi.item_id");
                sql.append( "                           AND mi.delete_date IS NULL");
                sql.append( "                           LEFT JOIN mst_item_class mic ON mic.item_class_id = mi.item_class_id");
                sql.append( "                           AND mic.delete_date IS NULL");
                sql.append( "                           WHERE dsd.shop_id = ds.shop_id");
                sql.append( "                             AND dsd.slip_no = ds.slip_no");
                sql.append( "                             AND dsd.slip_detail_no = ds.slip_detail_no");
                sql.append( "                             AND dsd.delete_date IS NULL");
                sql.append( "                             AND dsd.product_division IN (2,4)");
                sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");

                sql.append( "                           UNION ALL SELECT 1");
                sql.append( "                           FROM data_sales_detail dsd");
                sql.append( "                           INNER JOIN mst_course msc ON msc.course_id = dsd.product_id");
                sql.append( "                           LEFT JOIN mst_course_class mscc ON mscc.course_class_id = msc.course_class_id");
                sql.append( "                           WHERE dsd.shop_id = ds.shop_id");
                sql.append( "                             AND dsd.slip_no = ds.slip_no");
                sql.append( "                             AND dsd.slip_detail_no = ds.slip_detail_no");
                sql.append( "                             AND dsd.delete_date IS NULL");
                sql.append("                  AND mscc.shop_category_id in (" + this.getListCategoryId() + ") ");
                sql.append( "                             AND dsd.product_division IN (5,6))");
            }            
            sql.append("                    AND EXISTS ( ");
            sql.append("                        SELECT 1 ");
            sql.append("                        FROM data_sales ds1 ");
            sql.append("                        WHERE ds1.customer_id = ds.customer_id ");
            if(isIsMutiShop()){
                   sql.append( "                and ds1.shop_id = ds.shop_id");
            }
            sql.append("                        AND ds1.sales_date < to_date("+getTermFrom()+", 'YYYY/MM/dd')  - interval '1 year' ");
            sql.append("                        AND ds1.sales_date >= to_date("+getTermFrom()+", 'YYYY/MM/dd') - interval '"+getCountDate()+"'  - interval '1 year' )");
            sql.append(" ), 0) AS repeat_count_last ");
            
            //IVS_TMTrong start edit 2015/08/10 Bug #41292
            sql.append(" ,coalesce(( ");
            sql.append("    SELECT count(DISTINCT (ds.customer_id)) ");
            sql.append("    FROM view_data_sales_detail_valid ds ");
            sql.append("    WHERE ds.sales_date < to_date("+getTermFrom()+", 'YYYY/MM/dd') - interval '1 year' ");
            sql.append("    AND ds.sales_date >= to_date("+getTermFrom()+", 'YYYY/MM/dd') - interval '"+getCountDate()+"'  - interval '1 year' ");
            if(isIsMutiShop()){
                sql.append("                AND ds.shop_id = t.shop_id");
            }
            if (this.getListCategoryId() != null && this.getListCategoryId() != "") {                           
                sql.append( "                AND EXISTS ");
                sql.append( "                          (SELECT 1 ");
                sql.append( "                           FROM data_sales_detail dsd ");
                sql.append( "                           INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
                sql.append( "                           LEFT JOIN mst_technic_class mstc ON mstc.technic_class_id = mst.technic_class_id ");
                sql.append( "                           WHERE dsd.shop_id = ds.shop_id ");
                sql.append( "                             AND dsd.slip_no = ds.slip_no ");
                sql.append( "                             AND dsd.slip_detail_no = ds.slip_detail_no ");
                sql.append( "                             AND dsd.delete_date IS NULL ");
                sql.append( "                             AND dsd.product_division IN (1,3) ");
                sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");

                sql.append( "                           UNION ALL SELECT 1");
                sql.append( "                           FROM data_sales_detail dsd");
                sql.append( "                           INNER JOIN mst_item mi ON dsd.product_id = mi.item_id");
                sql.append( "                           AND mi.delete_date IS NULL");
                sql.append( "                           LEFT JOIN mst_item_class mic ON mic.item_class_id = mi.item_class_id");
                sql.append( "                           AND mic.delete_date IS NULL");
                sql.append( "                           WHERE dsd.shop_id = ds.shop_id");
                sql.append( "                             AND dsd.slip_no = ds.slip_no");
                sql.append( "                             AND dsd.slip_detail_no = ds.slip_detail_no");
                sql.append( "                             AND dsd.delete_date IS NULL");
                sql.append( "                             AND dsd.product_division IN (2,4)");
                sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");

                sql.append( "                           UNION ALL SELECT 1");
                sql.append( "                           FROM data_sales_detail dsd");
                sql.append( "                           INNER JOIN mst_course msc ON msc.course_id = dsd.product_id");
                sql.append( "                           LEFT JOIN mst_course_class mscc ON mscc.course_class_id = msc.course_class_id");
                sql.append( "                           WHERE dsd.shop_id = ds.shop_id");
                sql.append( "                             AND dsd.slip_no = ds.slip_no");
                sql.append( "                             AND dsd.slip_detail_no = ds.slip_detail_no");
                sql.append( "                             AND dsd.delete_date IS NULL");
                sql.append("                  AND mscc.shop_category_id in (" + this.getListCategoryId() + ") ");
                sql.append( "                             AND dsd.product_division IN (5,6))");
            }
            //IVS_TMTrong end edit 2015/08/10 Bug #41292
            sql.append(" ), 0) AS repeat_total_last ");
            
            sql.append(" ,coalesce(( ");
            sql.append("    SELECT sum(target_staff_num) ");
            sql.append("    FROM data_target_result dt ");
            sql.append("    WHERE dt.delete_date IS NULL ");
             if(getYearMonth().size() >0){
                sql.append(" and (");
                Iterator iterator = getYearMonth().entrySet().iterator();
                int count = 0;
                while (iterator.hasNext()) {
                      Map.Entry mapEntry = (Map.Entry) iterator.next();
                      if(count >0){
                        sql.append(" or ");
                      }
                      sql.append("( dt.year = " + mapEntry.getKey());
                      sql.append(" and dt.month in (" );
                      List<Integer> lstTmp = new ArrayList<Integer>();
                      lstTmp = (List<Integer>)mapEntry.getValue();
                      int countPoint = 0;
                      for(int i = 0; i< lstTmp.size(); i++){
                          countPoint++;
                          sql.append(lstTmp.get(i) );
                          if(countPoint < lstTmp.size()){
                              sql.append(",");
                          }
                      }
                      sql.append(") )");
                      count++;
                }
                sql.append(" )");
            }
            sql.append(" AND shop_id = t.shop_id ");
            sql.append(" GROUP BY dt.shop_id ");
            sql.append(" ), 0) AS staff_count_last ");
            //IVS_TMTrong end add 2015/08/07 Bug #41295
            
	    sql.append(" from");
	    sql.append("     (" + getSelectShopRankingValueSQLOld(false) + ") t");
	    sql.append("         join mst_shop mss");
	    sql.append("             using(shop_id)");
	    sql.append(" where");
	    sql.append("     mss.delete_date is null");
            //IVS_LVTu start edit 2015/04/14 Bug #36254
            sql.append("         and mss.shop_id in (" + getShopIDList() +")");
            //IVS_LVTu end edit 2015/04/14 Bug #36254
            sql.append(" ) as this_year_last_year\n");

            /*//IVS_TMTrong start edit 2015/08/07 Bug #41295
            //çîNÇÃÉfÅ[É^
            sql.append(" left join \n"); 
            sql.append(" ( \n");
             sql.append(" select");
            sql.append("      mss.shop_id, ");
	    sql.append("      mss.shop_name");
	    sql.append("     ,coalesce(totalCount, 0)        as totalCount_last");
	    sql.append("     ,coalesce(techCount, 0)         as techCount_last");
	    sql.append("     ,coalesce(newCustomerCount, 0)  as newCustomerCount_last");
	    sql.append("     ,coalesce(techSales, 0)         as techSales_last");

            // ãZèpãqíPâøÅiÉ\Å[ÉgópÅj
	    sql.append("     ,coalesce(techSales, 0) / case when coalesce(techCount, 0) = 0 then 1 else coalesce(techCount, 0) end as techUnitSales_last");
            
	    sql.append("     ,coalesce(itemSales, 0)         as itemSales_last");
	    sql.append("     ,coalesce(totalSales, 0)        as totalSales_last");
	    sql.append("     ,coalesce(customerCount, 0)     as customerCount_last");
            //sql.append("     ,repeat_rate ");
            //nhanvt start add 20150309 New request #35223
             sql.append( "          ,coalesce((select count(distinct slip_no) ");
             sql.append( "                from view_data_sales_detail_valid ds ");
             sql.append( "                where ds.sales_date between  date" + getTermFrom() + " -interval '1 year' and date " + getTermTo() + " -interval '1 year'\n");
             sql.append( "                and ds.shop_id = t.shop_id  ");
             sql.append( "                AND EXISTS ");
             sql.append( "                          (SELECT 1 ");
             sql.append( "                           FROM data_sales_detail dsd ");
             sql.append( "                           INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
             sql.append( "                           LEFT JOIN mst_technic_class mstc ON mstc.technic_class_id = mst.technic_class_id ");
             sql.append( "                           WHERE dsd.shop_id = ds.shop_id ");
             sql.append( "                             AND dsd.slip_no = ds.slip_no ");
             sql.append( "                             AND dsd.slip_detail_no = ds.slip_detail_no ");
             sql.append( "                             AND dsd.delete_date IS NULL ");
             sql.append( "                             AND dsd.product_division IN (1,3) ");
             if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");
             }
                                                       
             sql.append( "                           UNION ALL SELECT 1");
             sql.append( "                           FROM data_sales_detail dsd");
             sql.append( "                           INNER JOIN mst_item mi ON dsd.product_id = mi.item_id");
             sql.append( "                           AND mi.delete_date IS NULL");
             sql.append( "                           LEFT JOIN mst_item_class mic ON mic.item_class_id = mi.item_class_id");
             sql.append( "                           AND mic.delete_date IS NULL");
             sql.append( "                           WHERE dsd.shop_id = ds.shop_id");
             sql.append( "                             AND dsd.slip_no = ds.slip_no");
             sql.append( "                             AND dsd.slip_detail_no = ds.slip_detail_no");
             sql.append( "                             AND dsd.delete_date IS NULL");
             sql.append( "                             AND dsd.product_division IN (2,4)");
             if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");
             }
                                                              
             sql.append( "                           UNION ALL SELECT 1");
             sql.append( "                           FROM data_sales_detail dsd");
             sql.append( "                           INNER JOIN mst_course msc ON msc.course_id = dsd.product_id");
             sql.append( "                           LEFT JOIN mst_course_class mscc ON mscc.course_class_id = msc.course_class_id");
             sql.append( "                           WHERE dsd.shop_id = ds.shop_id");
             sql.append( "                             AND dsd.slip_no = ds.slip_no");
             sql.append( "                             AND dsd.slip_detail_no = ds.slip_detail_no");
             sql.append( "                             AND dsd.delete_date IS NULL");
             
             if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                sql.append("                  AND mscc.shop_category_id in (" + this.getListCategoryId() + ") ");
             }
             sql.append( "                             AND dsd.product_division IN (5,6))");
             sql.append( "                         And exists (select 1 from data_sales ds1 ");
             sql.append( "                                     where ds1.customer_id = ds.customer_id ");
             if(isIsMutiShop()){
                sql.append( "                                      and ds1.shop_id = ds.shop_id");
             }
             
            sql.append( "    and ds1.sales_date <= ds.sales_date - interval '"+getCountDate()+"' ");
            sql.append( "    	    and ds1.sales_date > ds.sales_date - interval '"+getCountDate()+"' - interval '1 month' ");

            sql.append( "                          )");
            sql.append( "                 ),0)  as repeat_rate_last ");
            
            //IVS_TMTrong start add 20150721 Bug #40595
            //Luc start edit 20150724  #40595
            //sql.append( "          ,coalesce((select count(distinct slip_no) ");
            sql.append( "          ,coalesce((select count(distinct customer_id) ");
            //Luc start edit 20150724  #40595
             sql.append( "                from view_data_sales_detail_valid ds ");
             sql.append( "                where ds.sales_date between  date" + getTermFrom() + " -interval '1 year' and date " + getTermTo() + " -interval '1 year'\n");
             sql.append( "                and ds.shop_id = t.shop_id  ");
             sql.append( "                AND EXISTS ");
             sql.append( "                          (SELECT 1 ");
             sql.append( "                           FROM data_sales_detail dsd ");
             sql.append( "                           INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
             sql.append( "                           LEFT JOIN mst_technic_class mstc ON mstc.technic_class_id = mst.technic_class_id ");
             sql.append( "                           WHERE dsd.shop_id = ds.shop_id ");
             sql.append( "                             AND dsd.slip_no = ds.slip_no ");
             sql.append( "                             AND dsd.slip_detail_no = ds.slip_detail_no ");
             sql.append( "                             AND dsd.delete_date IS NULL ");
             sql.append( "                             AND dsd.product_division IN (1,3) ");
             if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");
             }
                                                       
             sql.append( "                           UNION ALL SELECT 1");
             sql.append( "                           FROM data_sales_detail dsd");
             sql.append( "                           INNER JOIN mst_item mi ON dsd.product_id = mi.item_id");
             sql.append( "                           AND mi.delete_date IS NULL");
             sql.append( "                           LEFT JOIN mst_item_class mic ON mic.item_class_id = mi.item_class_id");
             sql.append( "                           AND mic.delete_date IS NULL");
             sql.append( "                           WHERE dsd.shop_id = ds.shop_id");
             sql.append( "                             AND dsd.slip_no = ds.slip_no");
             sql.append( "                             AND dsd.slip_detail_no = ds.slip_detail_no");
             sql.append( "                             AND dsd.delete_date IS NULL");
             sql.append( "                             AND dsd.product_division IN (2,4)");
             if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");
             }
                                                              
             sql.append( "                           UNION ALL SELECT 1");
             sql.append( "                           FROM data_sales_detail dsd");
             sql.append( "                           INNER JOIN mst_course msc ON msc.course_id = dsd.product_id");
             sql.append( "                           LEFT JOIN mst_course_class mscc ON mscc.course_class_id = msc.course_class_id");
             sql.append( "                           WHERE dsd.shop_id = ds.shop_id");
             sql.append( "                             AND dsd.slip_no = ds.slip_no");
             sql.append( "                             AND dsd.slip_detail_no = ds.slip_detail_no");
             sql.append( "                             AND dsd.delete_date IS NULL");
             
             if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                sql.append("                  AND mscc.shop_category_id in (" + this.getListCategoryId() + ") ");
             }
             sql.append( "                             AND dsd.product_division IN (5,6))");
             sql.append( "                         And exists (select 1 from data_sales ds1 ");
             sql.append( "                                     where ds1.customer_id = ds.customer_id ");
             if(isIsMutiShop()){
                sql.append( "                                      and ds1.shop_id = ds.shop_id");
             }
            //Luc start edit 20150724 #40595 
            //sql.append( "    and ds1.sales_date <= ds.sales_date - interval '"+getCountDate()+"' ");
            //sql.append( "    	    and ds1.sales_date > ds.sales_date - interval '"+getCountDate()+"' - interval '1 month' ");
            sql.append( "    and ds1.sales_date <= to_date("+getTermTo()+",'YYYY/MM/dd') - interval '"+getCountDate()+"' ");
            sql.append( "    	    and ds1.sales_date > to_date("+getTermTo()+",'YYYY/MM/dd') - interval '"+getCountDate()+"' - interval '1 month' ");
            //Luc start edit 20150724 #40595 
            sql.append( "                          )");
            sql.append( "                 ),0)  as repeat_count_last ");
            
            sql.append(" ,coalesce(( ");
            sql.append("        SELECT count(DISTINCT (ds.customer_id)) ");
            sql.append("        FROM data_sales ds ");
            sql.append("        WHERE ds.sales_date <= to_date("+getTermTo()+",'YYYY/MM/dd') - interval '"+getCountDate()+"' - interval '1 year' ");
            sql.append("        AND ds.sales_date > to_date("+getTermFrom()+",'YYYY/MM/dd') - interval '"+getCountDate()+"' - interval '1 month' - interval '1 year' ");
            if(isIsMutiShop()){
                sql.append("     AND ds.shop_id in (" + getShopIDList() + ") ");
            }
            sql.append(" ),0) AS repeat_total_last ");
            //IVS_TMTrong end add 20150721 Bug #40595
            
            sql.append("     ,coalesce(staff_count,0) as staff_count_last ");
	    sql.append(" from");
	    sql.append("     (" + getSelectShopRankingValueSQLOld(true) + ") t");
	    sql.append("         join mst_shop mss");
	    sql.append("             using(shop_id)");
	    sql.append(" where");
	    sql.append("     mss.delete_date is null");
            //IVS_LVTu start edit 2015/04/14 Bug #36254
            sql.append("         and mss.shop_id in (" + getShopIDList() +")");
            //IVS_LVTu end edit 2015/04/14 Bug #36254
            sql.append(" )last_year on this_year.shop_id = last_year.shop_id ");
            */
            //IVS_TMTrong end edit 2015/08/07 Bug #41295
            sql.append("    left join  ");
            sql.append("    ( ");
            sql.append("     SELECT dt.shop_id, ");
            //IVS_TMTrong start edit 2015/08/11 Bug #41292
            if(getTaxType() == TAX_TYPE_BLANK){
                sql.append(" 0 AS customerCount_target, ");
                sql.append(" coalesce(sum(target_technic_num ), 0) AS techCount_target, ");
                sql.append(" coalesce(sum(ceil(target_technic/( 1 + get_tax_rate(to_date(dt.year || '-' || dt.month || '-' || '01','YYYY-MM-DD'))))), 0) AS techSales_target, ");
                sql.append(" coalesce(sum(target_new_num), 0) AS newCustomerCount_target, ");
		sql.append(" coalesce(sum(ceil(target_item/( 1 + get_tax_rate(to_date(dt.year || '-' || dt.month || '-' || '01','YYYY-MM-DD'))))), 0) AS itemSales_target, ");
		sql.append(" sum(target_technic_num) + sum( target_item_num) AS totalCount_target, ");
		sql.append(" sum(ceil(target_technic/( 1 + get_tax_rate(to_date(dt.year || '-' || dt.month || '-' || '01','YYYY-MM-DD')))) ) + sum(ceil(target_item/( 1 + get_tax_rate(to_date(dt.year || '-' || dt.month || '-' || '01','YYYY-MM-DD'))))) AS totalSales_target, ");
		sql.append(" sum(repert_30_fix) AS repeat_rate_target, ");
		sql.append(" sum(target_staff_num) AS staff_count_target ");
                sql.append(" ,coalesce(sum(ceil(target_course/( 1 + get_tax_rate(to_date(dt.year || '-' || dt.month || '-' || '01','YYYY-MM-DD'))))), 0) AS courseSales_target ");
                sql.append(" ,coalesce(sum(ceil(target_digestion/( 1 + get_tax_rate(to_date(dt.year || '-' || dt.month || '-' || '01','YYYY-MM-DD'))))), 0) AS courseDigestionSales_target ");
            }else {
                sql.append("             0 AS customerCount_target, ");
                sql.append("             coalesce(sum(target_technic_num), 0) AS techCount_target, ");
                sql.append("              coalesce(sum(target_technic),0) AS techSales_target,");
                sql.append("                         coalesce(sum(target_new_num), 0) AS newCustomerCount_target,");
                sql.append("                                   coalesce(sum(target_item),0) AS itemSales_target,");
                sql.append("          sum(target_technic_num + target_item_num) AS totalCount_target, ");
                sql.append("          sum(target_technic + target_item) AS totalSales_target, ");
                sql.append("          sum("+getNameColumn()+") as repeat_rate_target,  ");
                sql.append(" sum(target_staff_num) as staff_count_target ");     
                sql.append("    ,coalesce(sum(target_course),0) AS courseSales_target");
                sql.append("    ,coalesce(sum(target_digestion),0) AS courseDigestionSales_target");
            }
            //IVS_TMTrong end edit 2015/08/11 Bug #41292
            sql.append("      FROM data_target_result dt ");
            sql.append(" where dt.delete_date is null ");
            
            if(getYearMonth().size() >0){
                sql.append(" and (");
                Iterator iterator = getYearMonth().entrySet().iterator();
                int count = 0;
                while (iterator.hasNext()) {
                      Map.Entry mapEntry = (Map.Entry) iterator.next();
                      if(count >0){
                        sql.append(" or ");
}
                      sql.append("( dt.year = " + mapEntry.getKey());
                      sql.append(" and dt.month in (" );
                      List<Integer> lstTmp = new ArrayList<Integer>();
                      lstTmp = (List<Integer>)mapEntry.getValue();
                      int countPoint = 0;
                      for(int i = 0; i< lstTmp.size(); i++){
                          countPoint++;
                          sql.append(lstTmp.get(i) );
                          if(countPoint < lstTmp.size()){
                              sql.append(",");
                          }
                      }
                      sql.append(") )");
                      count++;
                }
                sql.append(" )");
            }
            sql.append("      GROUP BY dt.shop_id ");
            //IVS_TMTrong start edit 2015/08/10 Bug #41292
            //sql.append("      ) as target on this_year.shop_id = target.shop_id ");
            sql.append("      ) as target on this_year_last_year.shop_id = target.shop_id ");
            //IVS_TMTrong end edit 2015/08/10 Bug #41292
            //Luc start edit 20150707 #40006
            switch(getOrderDisplay()) {
                    //ëçãqêî
                    case 0:
                        sql.append(" order by totalCount desc");
                        break;
                    //ãZèpãqêî    
                    case 1:
                        sql.append(" order by techCount desc");
                         break;
                    //ãZèpêVãKãqêî  
                    case 2:
                        sql.append(" order by newCustomerCount desc");
                         break;
                        //ãZèpêVãKãqêî  
                    case 3:
                        sql.append(" order by techSales desc");
                         break;
                     //ãZèpãqíPâø   
                    case 4:
                        sql.append(" order by coalesce(techSales, 0) / case when coalesce(techCount, 0) = 0 then 1 else coalesce(techCount, 0) end desc");
                         break;
                    //è§ïiîÑè„    
                    case 5:
                        sql.append(" order by itemSales desc");
                         break;
                    //ëçîÑè„    
                    case 6:
                        sql.append(" order by totalSales desc");
                         break;
                    //ëçãqíPâø    
                    case 7:
                        sql.append(" order by totalSales /coalesce(customerCount,1) desc");
                         break;
                   
                }
            //Luc end edit 20150624 38408
	    //ï\é¶îÕàÕ
            if (getRangeDisplay() == RANGE_DISPLAY_TO_10)
            {
                   //Å`10à 
                   sql.append(" limit 10");
            }
            else if (getRangeDisplay() == RANGE_DISPLAY_TO_20)
            {
                   //Å`20à 
                   sql.append(" limit 20");
            }
            //Luc end edit 20150707 #40006
            return sql.toString();
	}
        
      
	/**
	 * ìXï‹ÉâÉìÉLÉìÉOÇÃäeîÑè„íäèoópSQLÇéÊìæÇ∑ÇÈÅB
	 *
	 * @param
	 * @return ìXï‹ÉâÉìÉLÉìÉOÇÃäeîÑè„íäèoópSQL
	 * @exception Exception
	 */
	private String getSelectShopRankingValueSQL() throws Exception
	{
	    StringBuilder sql = new StringBuilder(1000);
            sql.append(" select a.shop_id, totalCount, customerCount, newCustomerCount, techCount, techSales, techSales1, itemSales, totalSales, totalPaymentValue, totalProductValue1, totalProductValue2, totalStaff from (");
	    sql.append(" select");
	    sql.append("      t.shop_id");
	    sql.append("     /* ëçãqêî */");
	    sql.append("     ,sum(t.techCount + t.itemOnlyCount) as totalCount");
	    sql.append("     /* âÔàıêî */");
	    sql.append("     ,sum( case when c.customer_no <> '0' then 1 else 0 end ) as customerCount");
	    sql.append("     /* êVãKãqêî */");
            //IVS_TMTrong start edit 20150717 Bug #40595
	    //sql.append("     ,sum( case when c.customer_no <> '0' and t.itemOnlyCount <> 1 and get_visit_count(t.customer_id,t.shop_id,t.sales_date) = 1 then 1 else 0 end) as newCustomerCount");
            if(isMutiShop){
                sql.append("     ,sum( case when c.customer_no <> '0' and t.itemOnlyCount <> 1 and get_visit_count(t.customer_id,t.shop_id,t.sales_date) = 1 then 1 else 0 end) as newCustomerCount");
            }else{
                sql.append("     ,sum( case when c.customer_no <> '0' and t.itemOnlyCount <> 1 and get_visit_count(t.customer_id,t.shop_id,t.sales_date) = 1 ");
                sql.append("    AND (SELECT count(slip_no) + coalesce(max(before_visit_num),0) FROM data_sales INNER JOIN mst_customer USING(customer_id) ");
                sql.append("    WHERE customer_id = t.customer_id AND data_sales.shop_id in (select shop_id from mst_shop where shop_id <> t.shop_id) ");
                sql.append("    AND data_sales.sales_date <= t.sales_date AND data_sales.delete_date IS NULL) < 1 ");
                sql.append("        then 1 else 0 end) as newCustomerCount ");
            }
            //IVS_TMTrong end edit 20150717 Bug #40595
	    sql.append("     /* ãZèpãqêî */");
	    sql.append("     ,sum(t.techCount) as techCount");
	    sql.append("     /* ãZèpîÑè„ */");
	    sql.append("     ,sum(t.techSales) as techSales");
            sql.append("     /* ÉMÉtÉgåîîÃîÑ */");
	    sql.append("     ,sum(t.techSales1) as techSales1");
	    sql.append("     /* è§ïiîÑè„ */");
	    sql.append("     ,sum(t.itemSales) as itemSales");

	    if(getTaxType() == TAX_TYPE_BLANK){

		// ê≈î≤Ç´
		sql.append("     /* ëSëÃäÑà¯å„ê≈î≤Ç´ã‡äz */");
                sql.append("     ,sum((select discount_sales_value_no_tax from view_data_sales_valid where shop_id = t.shop_id and slip_no = t.slip_no)) as totalSales");

	    } else {

		// ê≈çûÇ›
		sql.append("     /* ëSëÃäÑà¯å„ê≈çûÇ›ã‡äz */");
                sql.append("     ,sum((select discount_sales_value_in_tax from view_data_sales_valid where shop_id = t.shop_id and slip_no = t.slip_no)) as totalSales");
	    }

            //ÉMÉtÉgåîóòóp
            sql.append("     /* ÉMÉtÉgåîóòóp */");
            sql.append("     ,sum((select sum(payment_value) as payment_value from data_payment_detail where shop_id = t.shop_id and slip_no = t.slip_no and payment_method_id = 2 and delete_date IS NULL group by shop_id, slip_no)) as totalPaymentValue");

            //âÒêîåîîÃîÑ
            sql.append("     /* âÒêîåîîÃîÑ */");
            sql.append("     ,sum((select sum(product_value) as product_value1 from data_sales_detail where shop_id = t.shop_id and slip_no = t.slip_no and product_division = 5 and delete_date IS NULL group by shop_id, slip_no)) as totalProductValue1");

            //âÒêîåîóòóp
            sql.append("     /* âÒêîåîóòóp */");
            sql.append("     ,sum((select sum(product_value) as product_value2 from data_sales_detail where shop_id = t.shop_id and slip_no = t.slip_no and product_division = 6 and delete_date IS NULL group by shop_id, slip_no)) as totalProductValue2");


	    sql.append(" from");
	    sql.append("     (");
	    sql.append("         select");
	    sql.append("              shop_id");
	    sql.append("             ,slip_no");
	    sql.append("             ,sales_date");
	    sql.append("             /* å⁄ãqID */");
	    sql.append("             ,max(customer_id) as customer_id");
	    sql.append("             /* è§ïiÇÃÇ›ãqêî */");
	    sql.append("             ,case when sum( case when product_division = 2 then 1 else 0 end ) = count(*) then 1 else 0 end as itemOnlyCount");
	    sql.append("             /* ãZèpãqêî */");
	    sql.append("             ,count( distinct case when product_division = 1 then slip_no else null end ) as techCount");

	    if(getTaxType() == TAX_TYPE_BLANK){

		// ê≈î≤Ç´
		sql.append("             /* ãZèpîÑè„(ê≈î≤) */");
		sql.append("             ,sum( case when product_division = 1 and m.technic_class_id <> 8 then discount_detail_value_no_tax else 0 end ) as techSales");
                sql.append("             /* ÉMÉtÉgåîîÃîÑ(ê≈î≤) */");
		sql.append("             ,sum( case when product_division = 1 and m.technic_class_id = 8 then discount_detail_value_no_tax else 0 end ) as techSales1");
		sql.append("             /* è§ïiîÑè„(ê≈î≤) */");
		sql.append("             ,sum( case when product_division = 2 then discount_detail_value_no_tax else 0 end ) as itemSales");

	    } else {

		// ê≈çûÇ›
		sql.append("             /* ãZèpîÑè„(ê≈çû) */");
		sql.append("             ,sum( case when product_division = 1 and m.technic_class_id <> 8 then discount_detail_value_in_tax else 0 end ) as techSales");
                sql.append("             /* ÉMÉtÉgåîîÃîÑ(ê≈çû) */");
		sql.append("             ,sum( case when product_division = 1 and m.technic_class_id = 8 then discount_detail_value_in_tax else 0 end ) as techSales1");
		sql.append("             /* è§ïiîÑè„(ê≈çû) */");
		sql.append("             ,sum( case when product_division = 2 then discount_detail_value_in_tax else 0 end ) as itemSales");
	    }
	    
	    sql.append("         from");
	    sql.append("             view_data_sales_detail_valid a");
            sql.append("             inner join mst_technic m on m.technic_id = a.product_id and m.delete_date IS NULL");
	    sql.append("         where");
	    sql.append("             sales_date between " + getTermFrom() + " and " + getTermTo());
	    sql.append("         group by");
	    sql.append("              shop_id");
	    sql.append("             ,slip_no");
	    sql.append("             ,sales_date");
	    sql.append("     ) t");
	    sql.append("     inner join mst_customer c");
	    sql.append("             on t.customer_id = c.customer_id");

	    sql.append(" group by");
	    sql.append("     t.shop_id) a");
            sql.append(" inner join (select m.shop_id, sum(case when m.staff_class_id = 8 then 0.5 else 1 end) as totalStaff");
            sql.append(" from mst_staff m, data_schedule d where m.staff_id = d.staff_id and d.shift_id <> 0 and d.schedule_date between " + getTermFrom() + " and " + getTermTo() + "and m.delete_date IS NULL and d.delete_date IS NULL group by     m.shop_id) b on a.shop_id = b.shop_id");
	    return sql.toString();
	}
        
        /**
	 * ìXï‹ÉâÉìÉLÉìÉOÇÃäeîÑè„íäèoópSQLÇéÊìæÇ∑ÇÈÅB
	 *
	 * @param
	 * @return ìXï‹ÉâÉìÉLÉìÉOÇÃäeîÑè„íäèoópSQL
	 * @exception Exception
	 */
	private String getSelectShopRankingValueSQLOld(Boolean isLast) throws Exception
	{
	    StringBuilder sql = new StringBuilder(1000);
	    sql.append(" select");
	    sql.append("      t.shop_id");
            sql.append("    ,coalesce(( SELECT sum(target_staff_num) ");
            sql.append("      FROM data_target_result dt  ");
           sql.append(" where dt.delete_date is null ");
           
            if(getYearMonth().size() >0){
                sql.append(" and (");
                Iterator iterator = getYearMonth().entrySet().iterator();
                int count = 0;
                while (iterator.hasNext()) {
                      Map.Entry mapEntry = (Map.Entry) iterator.next();
                      if(count >0){
                        sql.append(" or ");
                      }
                      sql.append("( dt.year = " + mapEntry.getKey());
                      sql.append(" and dt.month in (" );
                      List<Integer> lstTmp = new ArrayList<Integer>();
                      lstTmp = (List<Integer>)mapEntry.getValue();
                      int countPoint = 0;
                      for(int i = 0; i< lstTmp.size(); i++){
                          countPoint++;
                          sql.append(lstTmp.get(i) );
                          if(countPoint < lstTmp.size()){
                              sql.append(",");
                          }
                      }
                      sql.append(") )");
                      count++;
                }
                sql.append(" )");
            }
            
            sql.append(" and shop_id = t.shop_id ");
            sql.append("    group by dt.shop_id ");
           
            sql.append("                ),0) as staff_count ");
            //nhanvt end add 20150309 New request #35223
	    sql.append("     /* ëçãqêî */");
            //LVTu start edit 2016/02/04 New request #46728
	    //sql.append("     ,sum(t.techCount + t.itemOnlyCount + t.courseCount) as totalCount");
            sql.append("     ,sum(totalCount) as totalCount");
	    sql.append("     /* âÔàıêî */");
	    sql.append("     ,sum( case when c.customer_no <> '0' then 1 else 0 end ) as customerCount");
	    sql.append("     /* êVãKãqêî */");
            
            //IVS_TMTrong start edit 20150715 Bug #40595
            //sql.append("     ,sum( case when c.customer_no <> '0' and t.itemOnlyCount <> 1 and get_visit_count(t.customer_id,t.shop_id,t.sales_date) = 1 then 1 else 0 end) as newCustomerCount");
            if(isMutiShop){
                sql.append("     ,sum( case when c.customer_no <> '0' and t.itemOnlyCount <> 1 and get_visit_count(t.customer_id,t.shop_id,t.sales_date) = 1 then 1 else 0 end) as newCustomerCount");
            }else{
                sql.append("     ,sum( case when c.customer_no <> '0' and t.itemOnlyCount <> 1 and get_visit_count(t.customer_id,t.shop_id,t.sales_date) = 1 ");
                sql.append("    AND (SELECT count(slip_no) + coalesce(max(before_visit_num),0) FROM data_sales INNER JOIN mst_customer USING(customer_id) ");
                sql.append("    WHERE customer_id = t.customer_id AND data_sales.shop_id in (select shop_id from mst_shop where shop_id <> t.shop_id) ");
                sql.append("    AND data_sales.sales_date <= t.sales_date AND data_sales.delete_date IS NULL) < 1 ");
                sql.append("        then 1 else 0 end) as newCustomerCount ");
            }
	   //IVS_TMTrong end edit 20150715 Bug #40595
	    
	    sql.append("     /* ãZèpãqêî */");
	    sql.append("     ,sum(t.techCount) as techCount");
	    sql.append("     /* ãZèpîÑè„ */");
	    sql.append("     ,sum(t.techSales) as techSales");
	    sql.append("     /* è§ïiîÑè„ */");
	    sql.append("     ,sum(t.itemSales) as itemSales");
            
            sql.append("     ,sum(t.courseSales) as courseSales");
            sql.append("     ,sum(t.course_digestionsales) as course_digestionsales");
            //nhanvt start edit 20150202 Bug #35146
	    // ê≈î≤Ç´
            sql.append("      ,sum(t.techSales)+sum(t.itemSales) + sum(t.courseSales) AS totalSales ");
            
            //IVS_TMTrong start add 2015/08/07 Bug #41295
            sql.append("  ,( ");
            sql.append("  SELECT sum(coalesce(a.itemOnlyCount,0)) ");
            sql.append("  FROM ( ");
            sql.append("  SELECT CASE  ");
            sql.append("  WHEN sum(CASE  ");
            sql.append("  WHEN a.product_division = 2 ");
            sql.append("  THEN 1 ");
            sql.append("  ELSE 0 ");
            sql.append("  END) = count(*) ");
            sql.append("  THEN 1 ");
            sql.append("  ELSE 0 ");
            sql.append("  END AS itemOnlyCount ");
            sql.append("  FROM view_data_sales_detail_valid a ");
            sql.append("  WHERE sales_date BETWEEN DATE "+getTermFrom()+" - interval '1 year' ");
            sql.append("  AND DATE "+getTermTo()+" - interval '1 year' ");
            sql.append("  AND a.shop_id = t.shop_id ");
                                     
            sql.append( "                AND EXISTS ");
            sql.append( "                          (SELECT 1 ");
            sql.append( "                           FROM data_sales_detail dsd ");
            sql.append( "                           INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
            sql.append( "                           LEFT JOIN mst_technic_class mstc ON mstc.technic_class_id = mst.technic_class_id ");
            sql.append( "                           WHERE dsd.shop_id = a.shop_id ");
            sql.append( "                             AND dsd.slip_no = a.slip_no ");
            sql.append( "                             AND dsd.slip_detail_no = a.slip_detail_no ");
            sql.append( "                             AND dsd.delete_date IS NULL ");
            sql.append( "                             AND dsd.product_division IN (1,3) ");
            sql.append( "                           UNION ALL SELECT 1");
            sql.append( "                           FROM data_sales_detail dsd");
            sql.append( "                           INNER JOIN mst_item mi ON dsd.product_id = mi.item_id");
            sql.append( "                           AND mi.delete_date IS NULL");
            sql.append( "                           LEFT JOIN mst_item_class mic ON mic.item_class_id = mi.item_class_id");
            sql.append( "                           AND mic.delete_date IS NULL");
            sql.append( "                           WHERE dsd.shop_id = a.shop_id");
            sql.append( "                             AND dsd.slip_no = a.slip_no");
            sql.append( "                             AND dsd.slip_detail_no = a.slip_detail_no");
            sql.append( "                             AND dsd.delete_date IS NULL");
            sql.append( "                             AND dsd.product_division IN (2,4)");
            sql.append( "                           UNION ALL SELECT 1");
            sql.append( "                           FROM data_sales_detail dsd");
            sql.append( "                           INNER JOIN mst_course msc ON msc.course_id = dsd.product_id");
            sql.append( "                           LEFT JOIN mst_course_class mscc ON mscc.course_class_id = msc.course_class_id");
            sql.append( "                           WHERE dsd.shop_id = a.shop_id");
            sql.append( "                             AND dsd.slip_no = a.slip_no");
            sql.append( "                             AND dsd.slip_detail_no = a.slip_detail_no");
            sql.append( "                             AND dsd.delete_date IS NULL");
            sql.append( "                             AND dsd.product_division IN (5,6)");
             sql.append( "                            )");

            if(this.getUseShopCategory() != null && this.getUseShopCategory() == 1){
                if (this.getListCategoryId() != null && this.getListCategoryId() != "") {                           
                sql.append( "                AND EXISTS ");
                sql.append( "                          ( ");


                sql.append( "                           SELECT 1");
                sql.append( "                           FROM data_sales_detail dsd");
                sql.append( "                           INNER JOIN mst_item mi ON dsd.product_id = mi.item_id");
                sql.append( "                           AND mi.delete_date IS NULL");
                sql.append( "                           LEFT JOIN mst_item_class mic ON mic.item_class_id = mi.item_class_id");
                sql.append( "                           AND mic.delete_date IS NULL");
                sql.append( "                           WHERE dsd.shop_id = a.shop_id");
                sql.append( "                             AND dsd.slip_no = a.slip_no");
                sql.append( "                             AND dsd.slip_detail_no = a.slip_detail_no");
                sql.append( "                             AND dsd.delete_date IS NULL");
                sql.append( "                             AND dsd.product_division IN (2)");
                sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");

                sql.append("                  )");
                }
            }

            sql.append(" GROUP BY shop_id ");
            sql.append(" ,slip_no ");
            sql.append(" ,sales_date ");
            sql.append(" ) a ");
            sql.append(" ) AS itemOnlyCount_last ");
            sql.append(" ,( ");
            sql.append(" SELECT count(DISTINCT CASE  ");
            sql.append(" WHEN product_division = 1 ");
            sql.append(" THEN slip_no ");
            sql.append(" ELSE NULL ");
            sql.append(" END) ");
            sql.append(" FROM view_data_sales_detail_valid a ");
            sql.append(" WHERE a.sales_date BETWEEN DATE "+getTermFrom()+" - interval '1 year' ");
            sql.append(" AND DATE "+getTermTo()+" - interval '1 year' ");
            sql.append(" AND a.shop_id = t.shop_id ");
            
             if(this.getUseShopCategory() != null && this.getUseShopCategory() == 1){
                                                             
                if (this.getListCategoryId() != null && this.getListCategoryId() != "") {                           
                sql.append( "                AND EXISTS ");
                sql.append( "                          (SELECT 1 ");
                sql.append( "                           FROM data_sales_detail dsd ");
                sql.append( "                           INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
                sql.append( "                           LEFT JOIN mst_technic_class mstc ON mstc.technic_class_id = mst.technic_class_id ");
                sql.append( "                           WHERE dsd.shop_id = a.shop_id ");
                sql.append( "                             AND dsd.slip_no = a.slip_no ");
                sql.append( "                             AND dsd.slip_detail_no = a.slip_detail_no ");
                sql.append( "                             AND dsd.delete_date IS NULL ");
                sql.append( "                             AND dsd.product_division IN (1,3) ");
                sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");
                sql.append("                   )");
                }
            }
            
            sql.append(" ) AS techCount_last "); 
            sql.append(" ,( ");
            sql.append(" SELECT sum(coalesce(a.courseCount_last,0))  ");
            sql.append(" FROM ");
            sql.append(" (SELECT CASE WHEN sum(CASE WHEN a.product_division in (5,6) THEN 1 ELSE 0 END) = count(*) THEN 1 ELSE 0 END AS courseCount_last ");
            sql.append(" FROM view_data_sales_detail_valid a ");
            sql.append(" WHERE a.sales_date BETWEEN DATE "+getTermFrom()+" - interval '1 year' ");
            sql.append(" AND DATE "+getTermTo()+" - interval '1 year' ");
            sql.append(" AND a.shop_id = t.shop_id ");
            
             if(this.getUseShopCategory() != null && this.getUseShopCategory() == 1){
                                                             
                if (this.getListCategoryId() != null && this.getListCategoryId() != "") {                           
                sql.append( "                AND EXISTS ");
                sql.append( "                          (SELECT 1 ");
                sql.append( "                           FROM data_sales_detail dsd");
                sql.append( "                           INNER JOIN mst_course msc ON msc.course_id = dsd.product_id");
                sql.append( "                           LEFT JOIN mst_course_class mscc ON mscc.course_class_id = msc.course_class_id");
                sql.append( "                           WHERE dsd.shop_id = a.shop_id");
                sql.append( "                             AND dsd.slip_no = a.slip_no");
                sql.append( "                             AND dsd.slip_detail_no = a.slip_detail_no");
                sql.append( "                             AND dsd.delete_date IS NULL");
                sql.append("                  AND mscc.shop_category_id in (" + this.getListCategoryId() + ") ");
                sql.append( "                             AND dsd.product_division IN (5,6))");
                }
            }
            sql.append(" GROUP BY shop_id, slip_no,sales_date) a "); 
            sql.append(" ) AS courseCount_last "); 
            sql.append(" ,( ");
            sql.append(" SELECT coalesce(newCustomerCount_last, 0) AS newCustomerCount_last ");
            sql.append(" FROM ( ");
            sql.append(" SELECT sum(CASE  ");
            sql.append(" WHEN c.customer_no <> '0' ");
            sql.append(" AND t.itemOnlyCount <> 1 ");
            sql.append(" AND get_visit_count(t.customer_id, t.shop_id, t.sales_date) = 1 ");
            sql.append(" AND (  ");
            sql.append(" SELECT count(slip_no) + coalesce(max(before_visit_num), 0) ");
            sql.append(" FROM data_sales ");
            sql.append(" INNER JOIN mst_customer USING (customer_id) ");
            sql.append(" WHERE customer_id = t.customer_id ");
            sql.append(" AND data_sales.shop_id IN ( ");
            sql.append(" SELECT shop_id ");
            sql.append(" FROM mst_shop ");
            sql.append(" WHERE shop_id <> t.shop_id ");
            sql.append(" ) ");
            sql.append(" AND data_sales.sales_date <= t.sales_date ");
            sql.append(" AND data_sales.delete_date IS NULL ");
            sql.append(" ) < 1 ");
            sql.append(" THEN 1 ");
            sql.append(" ELSE 0 ");
            sql.append(" END) AS newCustomerCount_last ");
            sql.append(" FROM ( ");
            sql.append(" SELECT shop_id ");
            sql.append(" ,slip_no ");
            sql.append(" ,sales_date ");
            sql.append(" ,max(customer_id) AS customer_id ");
            sql.append(" ,CASE  ");
            sql.append(" WHEN sum(CASE  ");
            sql.append(" WHEN product_division = 2 ");
            sql.append(" THEN 1 ");
            sql.append(" ELSE 0 ");
            sql.append(" END) = count(*) ");
            sql.append(" THEN 1 ");
            sql.append(" ELSE 0 ");
            sql.append(" END AS itemOnlyCount ");
            sql.append(" FROM view_data_sales_detail_valid a ");
            sql.append(" WHERE sales_date BETWEEN DATE "+getTermFrom()+" - interval '1 year' ");
            sql.append(" AND DATE "+getTermTo()+" - interval '1 year' ");
            sql.append(" AND a.shop_id = t.shop_id ");
                                                         
                                                         
                if (this.getListCategoryId() != null && this.getListCategoryId() != "") {                           
                sql.append( "                AND EXISTS ");
                sql.append( "                          (SELECT 1 ");
                sql.append( "                           FROM data_sales_detail dsd ");
                sql.append( "                           INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
                sql.append( "                           LEFT JOIN mst_technic_class mstc ON mstc.technic_class_id = mst.technic_class_id ");
                sql.append( "                           WHERE dsd.shop_id = a.shop_id ");
                sql.append( "                             AND dsd.slip_no = a.slip_no ");
                sql.append( "                             AND dsd.slip_detail_no = a.slip_detail_no ");
                sql.append( "                             AND dsd.delete_date IS NULL ");
                sql.append( "                             AND dsd.product_division IN (1,3) ");
                sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");
                sql.append("                  )");
                }
                
            sql.append(" GROUP BY shop_id ");
            sql.append(" ,slip_no ");
            sql.append(" ,sales_date ");
            sql.append(" ) t ");
            sql.append(" INNER JOIN mst_customer c ON t.customer_id = c.customer_id ");
            sql.append(" GROUP BY t.shop_id ");
            sql.append(" ) AS t ");
            sql.append(" ) AS newCustomerCount_last ");
            sql.append(" ,( ");
            sql.append(" SELECT sum(CASE  ");
            sql.append(" WHEN a.product_division IN (1,3) ");
            //IVS_TMTrong start edit 2015/08/11 Bug #41292
            if(getTaxType() == TAX_TYPE_BLANK){
                sql.append(" THEN a.discount_detail_value_no_tax ");
            }
            else{
                sql.append(" THEN a.discount_detail_value_in_tax ");
            }
            //IVS_TMTrong end edit 2015/08/11 Bug #41292
            sql.append(" ELSE 0 ");
            sql.append(" END) ");
            sql.append(" FROM view_data_sales_detail_valid a ");
            sql.append(" WHERE a.sales_date BETWEEN DATE "+getTermFrom()+" - interval '1 year' ");
            sql.append(" AND DATE "+getTermTo()+" - interval '1 year' ");
            sql.append(" AND a.shop_id = t.shop_id ");
             if(this.getUseShopCategory() != null && this.getUseShopCategory() == 1){
                                                             
                if (this.getListCategoryId() != null && this.getListCategoryId() != "") {                           
                sql.append( "                AND EXISTS ");
                sql.append( "                          (SELECT 1 ");
                sql.append( "                           FROM data_sales_detail dsd ");
                sql.append( "                           INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
                sql.append( "                           LEFT JOIN mst_technic_class mstc ON mstc.technic_class_id = mst.technic_class_id ");
                sql.append( "                           WHERE dsd.shop_id = a.shop_id ");
                sql.append( "                             AND dsd.slip_no = a.slip_no ");
                sql.append( "                             AND dsd.slip_detail_no = a.slip_detail_no ");
                sql.append( "                             AND dsd.delete_date IS NULL ");
                sql.append( "                             AND dsd.product_division IN (1,3) ");
                sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");
                sql.append("                   )");
                }
            }
            sql.append("    ) AS techSales_last ");
            sql.append("    ,( ");
            sql.append("         SELECT coalesce(itemSales_last, 0) AS itemSales_last ");
            sql.append("         FROM ( ");
            sql.append("                SELECT t.shop_id ");
            sql.append("                ,sum(t.itemSales) AS itemSales_last ");
            sql.append("                FROM ( ");
            sql.append("                    SELECT shop_id ");
            sql.append("                    ,slip_no ");
            sql.append("                    ,sales_date ");
            sql.append("                    ,max(customer_id) AS customer_id ");
            sql.append("                    ,sum(CASE  ");
            sql.append("                            WHEN product_division IN (2,4) ");
             //IVS_TMTrong start edit 2015/08/11 Bug #41292
             if(getTaxType() == TAX_TYPE_BLANK){
                 sql.append("                           THEN discount_detail_value_no_tax ");
             }else {
                sql.append("                            THEN discount_detail_value_in_tax ");
             }
             //IVS_TMTrong end edit 2015/08/11 Bug #41292
            sql.append("                    ELSE 0 ");
            sql.append("                    END) AS itemSales ");
            sql.append("                    FROM view_data_sales_detail_valid a ");
            sql.append("                    WHERE sales_date BETWEEN DATE "+getTermFrom()+" - interval '1 year' ");
            sql.append("                    AND DATE "+getTermTo()+" - interval '1 year' ");
            sql.append("                    AND a.shop_id = t.shop_id ");
                                                                      
            if(this.getUseShopCategory() != null && this.getUseShopCategory() == 1){
                if (this.getListCategoryId() != null && this.getListCategoryId() != "") {                           
                sql.append( "                AND EXISTS ");
                sql.append( "                          (");
                sql.append( "                           SELECT 1");
                sql.append( "                           FROM data_sales_detail dsd");
                sql.append( "                           INNER JOIN mst_item mi ON dsd.product_id = mi.item_id");
                sql.append( "                           AND mi.delete_date IS NULL");
                sql.append( "                           LEFT JOIN mst_item_class mic ON mic.item_class_id = mi.item_class_id");
                sql.append( "                           AND mic.delete_date IS NULL");
                sql.append( "                           WHERE dsd.shop_id = a.shop_id");
                sql.append( "                             AND dsd.slip_no = a.slip_no");
                sql.append( "                             AND dsd.slip_detail_no = a.slip_detail_no");
                sql.append( "                             AND dsd.delete_date IS NULL");
                sql.append( "                             AND dsd.product_division IN (2,4)");
                sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");

                sql.append("                   )");
                }
            }
            sql.append(" GROUP BY shop_id ");
            sql.append(" ,slip_no ");
            sql.append(" ,sales_date ");
            sql.append(" ) t ");
            sql.append(" INNER JOIN mst_customer c ON t.customer_id = c.customer_id ");
            sql.append(" GROUP BY t.shop_id ");
            sql.append(" ) t ");
            sql.append(" ) AS itemSales_last ");
            sql.append("    ,( ");
            sql.append("         SELECT coalesce(course_digestion_Sales_last, 0) AS course_digestion_Sales_last ");
            sql.append("         FROM ( ");
            sql.append("                SELECT t.shop_id ");
            sql.append("                ,sum(t.course_digestion_Sales_last) AS course_digestion_Sales_last ");
            sql.append("                FROM ( ");
            sql.append("                    SELECT shop_id ");
            sql.append("                    ,slip_no ");
            sql.append("                    ,sales_date ");
            sql.append("                    ,max(customer_id) AS customer_id ");
            sql.append("                    ,sum(CASE  ");
            sql.append("                            WHEN product_division IN (5) ");
             //IVS_TMTrong start edit 2015/08/11 Bug #41292
             if(getTaxType() == TAX_TYPE_BLANK){
                 sql.append("                           THEN discount_detail_value_no_tax ");
             }else {
                sql.append("                            THEN discount_detail_value_in_tax ");
             }
             //IVS_TMTrong end edit 2015/08/11 Bug #41292
            sql.append("                    ELSE 0 ");
            sql.append("                    END) AS course_digestion_Sales_last ");
            sql.append("                    FROM view_data_sales_detail_valid a ");
            sql.append("                    WHERE sales_date BETWEEN DATE "+getTermFrom()+" - interval '1 year' ");
            sql.append("                    AND DATE "+getTermTo()+" - interval '1 year' ");
            sql.append("                    AND a.shop_id = t.shop_id ");
                                                                      
            if(this.getUseShopCategory() != null && this.getUseShopCategory() == 1){
                if (this.getListCategoryId() != null && this.getListCategoryId() != "") {                           
                sql.append( "                AND EXISTS ");
                sql.append( "                          (");
                sql.append( "                           SELECT 1");
                sql.append( "                           FROM data_sales_detail dsd");
                sql.append( "                           INNER JOIN mst_course msc ON msc.course_id = dsd.product_id");
                sql.append( "                           LEFT JOIN mst_course_class mscc ON mscc.course_class_id = msc.course_class_id");
                sql.append( "                           WHERE dsd.shop_id = a.shop_id");
                sql.append( "                             AND dsd.slip_no = a.slip_no");
                sql.append( "                             AND dsd.slip_detail_no = a.slip_detail_no");
                sql.append( "                             AND dsd.delete_date IS NULL");
                sql.append("                            AND mscc.shop_category_id in (" + this.getListCategoryId() + ") ");
                sql.append( "                           AND dsd.product_division IN (5)");
                sql.append("                   )");
                }
            }
            sql.append(" GROUP BY shop_id ");
            sql.append(" ,slip_no ");
            sql.append(" ,sales_date ");
            sql.append(" ) t ");
            sql.append(" INNER JOIN mst_customer c ON t.customer_id = c.customer_id ");
            sql.append(" GROUP BY t.shop_id ");
            sql.append(" ) t ");
            sql.append(" ) AS courseSales_last ");
            
            sql.append("    ,( ");
            sql.append("         SELECT coalesce(course_digestion_Sales_last, 0) AS course_digestion_Sales_last ");
            sql.append("         FROM ( ");
            sql.append("                SELECT t.shop_id ");
            sql.append("                ,sum(t.course_digestion_Sales_last) AS course_digestion_Sales_last ");
            sql.append("                FROM ( ");
            sql.append("                    SELECT shop_id ");
            sql.append("                    ,slip_no ");
            sql.append("                    ,sales_date ");
            sql.append("                    ,max(customer_id) AS customer_id ");
            sql.append("                    ,sum(CASE  ");
            sql.append("                            WHEN product_division IN (6) ");
             //IVS_TMTrong start edit 2015/08/11 Bug #41292
             if(getTaxType() == TAX_TYPE_BLANK){
                sql.append("                           THEN discount_detail_value_no_tax * (select dcd.product_num  ");
                sql.append("                          from data_contract_digestion dcd  ");
                sql.append("     inner join data_sales_detail dsd on dsd.contract_no = dcd.contract_no ");
                sql.append("     and dsd.shop_id = dcd.shop_id");
                sql.append("     and dsd.slip_no = dcd.slip_no");
                sql.append("     and dsd.contract_detail_no = dcd.contract_detail_no");
                sql.append("     WHERE dsd.shop_id = a.shop_id");
                sql.append("     AND dsd.slip_no = a.slip_no");
                sql.append("     and dsd.staff_id = a.detail_staff_id");
                sql.append("     and dsd.slip_detail_no = a.slip_detail_no) ");
             }else {
                sql.append("                            THEN discount_detail_value_in_tax * (select dcd.product_num  ");
                sql.append("                          from data_contract_digestion dcd  ");
                sql.append("     inner join data_sales_detail dsd on dsd.contract_no = dcd.contract_no ");
                sql.append("     and dsd.shop_id = dcd.shop_id");
                sql.append("     and dsd.slip_no = dcd.slip_no");
                sql.append("     and dsd.contract_detail_no = dcd.contract_detail_no");
                sql.append("     WHERE dsd.shop_id = a.shop_id");
                sql.append("     AND dsd.slip_no = a.slip_no");
                sql.append("     and dsd.staff_id = a.detail_staff_id");
                sql.append("     and dsd.slip_detail_no = a.slip_detail_no ) ");
             }
             //IVS_TMTrong end edit 2015/08/11 Bug #41292
            sql.append("                    ELSE 0 ");
            sql.append("                    END) AS course_digestion_Sales_last ");
            sql.append("                    FROM view_data_sales_detail_valid a ");
            sql.append("                    WHERE sales_date BETWEEN DATE "+getTermFrom()+" - interval '1 year' ");
            sql.append("                    AND DATE "+getTermTo()+" - interval '1 year' ");
            sql.append("                    AND a.shop_id = t.shop_id ");
                                                                      
            if(this.getUseShopCategory() != null && this.getUseShopCategory() == 1){
                if (this.getListCategoryId() != null && this.getListCategoryId() != "") {                           
                sql.append( "                AND EXISTS ");
                sql.append( "                          (");
                sql.append( "                           SELECT 1");
                sql.append( "                           FROM data_sales_detail dsd");
                sql.append( "                           INNER JOIN mst_course msc ON msc.course_id = dsd.product_id");
                sql.append( "                           LEFT JOIN mst_course_class mscc ON mscc.course_class_id = msc.course_class_id");
                sql.append( "                           WHERE dsd.shop_id = a.shop_id");
                sql.append( "                             AND dsd.slip_no = a.slip_no");
                sql.append( "                             AND dsd.slip_detail_no = a.slip_detail_no");
                sql.append( "                             AND dsd.delete_date IS NULL");
                sql.append("                            AND mscc.shop_category_id in (" + this.getListCategoryId() + ") ");
                sql.append( "                           AND dsd.product_division IN (6)");
                sql.append("                   )");
                }
            }
            sql.append(" GROUP BY shop_id ");
            sql.append(" ,slip_no ");
            sql.append(" ,sales_date ");
            sql.append(" ) t ");
            sql.append(" INNER JOIN mst_customer c ON t.customer_id = c.customer_id ");
            sql.append(" GROUP BY t.shop_id ");
            sql.append(" ) t ");
            sql.append(" ) AS course_digestion_Sales_last ");
            sql.append(" ,( ");
            sql.append(" SELECT coalesce(customerCount_last, 0) AS customerCount_last ");
            sql.append(" FROM ( ");
            sql.append(" SELECT t.shop_id ");
            sql.append(" ,sum(CASE  ");
            sql.append(" WHEN c.customer_no <> '0' ");
            sql.append(" THEN 1 ");
            sql.append(" ELSE 0 ");
            sql.append(" END) AS customerCount_last ");
            sql.append(" FROM ( ");
            sql.append(" SELECT shop_id ");
            sql.append(" ,slip_no ");
            sql.append(" ,sales_date ");
            sql.append(" ,max(customer_id) AS customer_id ");
            sql.append(" FROM view_data_sales_detail_valid a ");
            sql.append(" WHERE sales_date BETWEEN DATE "+getTermFrom()+" - interval '1 year' ");
            sql.append(" AND DATE "+getTermTo()+" - interval '1 year' ");
            sql.append(" AND a.shop_id = t.shop_id ");
                                                         
            if(this.getUseShopCategory() != null && this.getUseShopCategory() == 1){
                                                             
                if (this.getListCategoryId() != null && this.getListCategoryId() != "") {                           
                sql.append( "                AND EXISTS ");
                sql.append( "                          (SELECT 1 ");
                sql.append( "                           FROM data_sales_detail dsd ");
                sql.append( "                           INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
                sql.append( "                           LEFT JOIN mst_technic_class mstc ON mstc.technic_class_id = mst.technic_class_id ");
                sql.append( "                           WHERE dsd.shop_id = a.shop_id ");
                sql.append( "                             AND dsd.slip_no = a.slip_no ");
                sql.append( "                             AND dsd.slip_detail_no = a.slip_detail_no ");
                sql.append( "                             AND dsd.delete_date IS NULL ");
                sql.append( "                             AND dsd.product_division IN (1,3) ");
                sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");


                sql.append( "                           UNION ALL SELECT 1");
                sql.append( "                           FROM data_sales_detail dsd");
                sql.append( "                           INNER JOIN mst_item mi ON dsd.product_id = mi.item_id");
                sql.append( "                           AND mi.delete_date IS NULL");
                sql.append( "                           LEFT JOIN mst_item_class mic ON mic.item_class_id = mi.item_class_id");
                sql.append( "                           AND mic.delete_date IS NULL");
                sql.append( "                           WHERE dsd.shop_id = a.shop_id");
                sql.append( "                             AND dsd.slip_no = a.slip_no");
                sql.append( "                             AND dsd.slip_detail_no = a.slip_detail_no");
                sql.append( "                             AND dsd.delete_date IS NULL");
                sql.append( "                             AND dsd.product_division IN (2,4)");
                sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");

                sql.append( "                           UNION ALL SELECT 1");
                sql.append( "                           FROM data_sales_detail dsd");
                sql.append( "                           INNER JOIN mst_course msc ON msc.course_id = dsd.product_id");
                sql.append( "                           LEFT JOIN mst_course_class mscc ON mscc.course_class_id = msc.course_class_id");
                sql.append( "                           WHERE dsd.shop_id = a.shop_id");
                sql.append( "                             AND dsd.slip_no = a.slip_no");
                sql.append( "                             AND dsd.slip_detail_no = a.slip_detail_no");
                sql.append( "                             AND dsd.delete_date IS NULL");
                sql.append("                  AND mscc.shop_category_id in (" + this.getListCategoryId() + ") ");
                sql.append( "                             AND dsd.product_division IN (5,6))");
                }
            }
            sql.append(" GROUP BY shop_id ");
            sql.append(" ,slip_no ");
            sql.append(" ,sales_date ");
            sql.append(" ) t ");
            sql.append(" INNER JOIN mst_customer c ON t.customer_id = c.customer_id ");
            sql.append(" GROUP BY t.shop_id ");
            sql.append(" ) t ");
            sql.append(" ) AS customerCount_last ");
            //IVS_TMTrong end add 2015/08/07 Bug #41295
            
            
            //nhanvt start edit 20150202 Bug #35146
	    sql.append(" from");
	    sql.append("     (");
	    sql.append("         select");
	    sql.append("              shop_id");
	    sql.append("             ,slip_no");
	    sql.append("             ,sales_date");
	    sql.append("             /* å⁄ãqID */");
	    sql.append("             ,max(customer_id) as customer_id");
	    sql.append("             /* è§ïiÇÃÇ›ãqêî */");
	    sql.append("             ,case when sum( case when product_division = 2 then 1 else 0 end ) = count(*) then 1 else 0 end as itemOnlyCount");
	    sql.append("             /* ãZèpãqêî */");
	    sql.append("             ,count( distinct case when product_division = 1 then slip_no else null end ) as techCount");
            sql.append("             ,count( distinct case when product_division in (5,6) then slip_no else null end ) as courseCount");
            if (courseFlag) {
            sql.append("             ,count( distinct case when product_division in (1,2,3,4,5,6) then slip_no else null end ) as totalCount");
            }else {
                sql.append("             ,count( distinct case when product_division in (1,2,3,4) then slip_no else null end ) as totalCount");
            }
            //LVTu end edit 2016/02/04 New request #46728
            
	    if(getTaxType() == TAX_TYPE_BLANK){

		// ê≈î≤Ç´
		sql.append("             /* ãZèpîÑè„(ê≈î≤) */");
		//Luc start edit 20150601 #37109
                //sql.append("             ,sum( case when product_division = 1 then discount_detail_value_no_tax else 0 end ) as techSales");
                sql.append("             ,sum( case when product_division in(1,3) then discount_detail_value_no_tax else 0 end ) as techSales");
                //Luc end edit 20150601 #37109
		sql.append("             /* è§ïiîÑè„(ê≈î≤) */");
                //Luc start edit 20150601 #37109
		//sql.append("             ,sum( case when product_division = 2 then discount_detail_value_no_tax else 0 end ) as itemSales");
                sql.append("             ,sum( case when product_division in(2,4) then discount_detail_value_no_tax else 0 end ) as itemSales");
                
                sql.append("             ,sum( case when product_division in(5) then discount_detail_value_no_tax else 0 end ) as courseSales");
                
                sql.append("             ,sum( case when product_division in(6) then discount_detail_value_no_tax * (select dcd.product_num ");
                sql.append("     from data_contract_digestion dcd ");
                sql.append("     inner join data_sales_detail dsd on dsd.contract_no = dcd.contract_no ");
                sql.append("     and dsd.shop_id = dcd.shop_id");
                sql.append("     and dsd.slip_no = dcd.slip_no");
                sql.append("     and dsd.contract_detail_no = dcd.contract_detail_no");
                sql.append("     WHERE dsd.shop_id = a.shop_id");
                sql.append("     AND dsd.slip_no = a.slip_no");
                sql.append("     and dsd.staff_id = a.detail_staff_id");
                sql.append("     and dsd.slip_detail_no = a.slip_detail_no");
                sql.append("     ) else 0 end ) as course_digestionsales");
                 //Luc end edit 20150601 #37109

	    } else {

		// ê≈çûÇ›
		sql.append("             /* ãZèpîÑè„(ê≈çû) */");
                //Luc start edit 20150601 #37109
		//sql.append("             ,sum( case when product_division = 1 then discount_detail_value_in_tax else 0 end ) as techSales");
                sql.append("             ,sum( case when product_division in(1,3) then discount_detail_value_in_tax else 0 end ) as techSales");
		//Luc end edit 20150601 #37109
                sql.append("             /* è§ïiîÑè„(ê≈çû) */");
                //Luc start edit 20150601 #37109
		//sql.append("             ,sum( case when product_division = 2 then discount_detail_value_in_tax else 0 end ) as itemSales");
                sql.append("             ,sum( case when product_division in(2,4) then discount_detail_value_in_tax else 0 end ) as itemSales");
                sql.append("             ,sum( case when product_division in(5) then discount_detail_value_in_tax else 0 end ) as courseSales");
                sql.append("             ,sum( case when product_division in(6) then discount_detail_value_in_tax * (select dcd.product_num ");
                sql.append("             from data_contract_digestion dcd ");
                sql.append("     inner join data_sales_detail dsd on dsd.contract_no = dcd.contract_no ");
                sql.append("     and dsd.shop_id = dcd.shop_id");
                sql.append("     and dsd.slip_no = dcd.slip_no");
                sql.append("     and dsd.contract_detail_no = dcd.contract_detail_no");
                sql.append("     WHERE dsd.shop_id = a.shop_id");
                sql.append("     AND dsd.slip_no = a.slip_no");
                sql.append("     and dsd.staff_id = a.detail_staff_id");
                sql.append("     and dsd.slip_detail_no = a.slip_detail_no");
                sql.append("           ) else 0 end ) as course_digestionsales");   
                //Luc end edit 20150601 #37109
	    }
	    
	    sql.append("         from");
	    sql.append("             view_data_sales_detail_valid a");
	    sql.append("         where");
            if(isLast) {
                sql.append(" sales_date between date" + getTermFrom() + " - interval '1 year' and date" + getTermTo() + " - interval '1 year'\n");
            }else {
                sql.append(" sales_date between " + getTermFrom() + " and " + getTermTo() + "\n");
            }
            if(this.getUseShopCategory() != null && this.getUseShopCategory() == 1){
                sql.append("      and   exists");
                sql.append("             (");

                //ã∆ë‘óòópÇ∑ÇÈÇ©ÇµÇ»Ç¢Ç©É`ÉFÉbÉN
           
                sql.append("                 SELECT");
                sql.append("                     1");
                sql.append("                 FROM");
                sql.append("                     data_sales_detail dsd");
                sql.append("                     inner join mst_technic mst on mst.technic_id = dsd.product_id ");
                sql.append("                     left join mst_technic_class mstc on mstc.technic_class_id = mst.technic_class_id ");
                sql.append("                 WHERE");
                sql.append("                         dsd.shop_id = a.shop_id");
                sql.append("                     AND dsd.slip_no = a.slip_no");
                sql.append("                     and dsd.slip_detail_no = a.slip_detail_no   ");
                sql.append("                     AND dsd.delete_date is null");
                sql.append("                     AND dsd.product_division in (1,3)");
                //ã∆ë‘ëIëÇÃèÍçá
                if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                    sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");
                }
                
                sql.append("                  UNION ALL  ");
                sql.append("                  SELECT ");
                sql.append("                     1");
                sql.append("                 FROM");
                sql.append("                      data_sales_detail dsd");
                sql.append("                     inner join mst_item mi on dsd.product_id = mi.item_id and mi.delete_date is null ");
                sql.append("                     left join mst_item_class mic on mic.item_class_id = mi.item_class_id and mic.delete_date is null ");
                sql.append("                  WHERE");
                sql.append("                          dsd.shop_id = a.shop_id");
                sql.append("                      AND dsd.slip_no = a.slip_no");
                sql.append("                     and dsd.slip_detail_no = a.slip_detail_no   ");
                sql.append("                      AND dsd.delete_date is null");
                sql.append("                      AND dsd.product_division in (2,4)");
                //ã∆ë‘ëIëÇÃèÍçá
                if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                    sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");
                }
                sql.append("                 UNION ALL  ");
                sql.append("                 SELECT ");
                sql.append("                     1");
                sql.append("                 FROM");
                sql.append("                     data_sales_detail dsd");
                sql.append("                     inner join mst_course msc on msc.course_id = dsd.product_id ");
                sql.append("                     left join mst_course_class mscc on mscc.course_class_id = msc.course_class_id ");
                sql.append("                 WHERE");
                sql.append("                         dsd.shop_id = a.shop_id");
                sql.append("                     AND dsd.slip_no = a.slip_no");
                sql.append("                     and dsd.slip_detail_no = a.slip_detail_no   ");
                sql.append("                     AND dsd.delete_date is null");
                sql.append("                     AND dsd.product_division in (5,6)");
                //ã∆ë‘ëIëÇÃèÍçá
                if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                    sql.append("                  AND mscc.shop_category_id in (" + this.getListCategoryId() + ") ");
                }
               
                sql.append(" ) ");
            }
            //nhanvt end add 20141201 New request #33406
	    sql.append("         group by");
	    sql.append("              shop_id");
	    sql.append("             ,slip_no");
	    sql.append("             ,sales_date");
	    sql.append("     ) t");
	    sql.append("     inner join mst_customer c");
	    sql.append("             on t.customer_id = c.customer_id");
	    sql.append(" group by");
	    sql.append("     t.shop_id");

	    return sql.toString();
	}
        
        //LVTu end edit 2016/01/19 New request #46728
        
        //nhanvt start add 20150309 New request #35223
        private String getSelectShopRankingValueSQLOldLastYear() throws Exception
	{
           
            StringBuilder sql = new StringBuilder(1000);
          
                sql.append(" select");
                sql.append("      t.shop_id");
                sql.append("     /* ëçãqêî */");
                sql.append("     ,sum(t.techCount + t.itemOnlyCount) as totalCount");
                sql.append("     /* âÔàıêî */");
                sql.append("     ,sum( case when c.customer_no <> '0' then 1 else 0 end ) as customerCount");
                sql.append("     /* êVãKãqêî */");
                //IVS_TMTrong start edit 20150717
                //sql.append("     ,sum( case when c.customer_no <> '0' and t.itemOnlyCount <> 1 and get_visit_count(t.customer_id,t.shop_id,t.sales_date) = 1 then 1 else 0 end) as newCustomerCount");
            if(isMutiShop){
                sql.append("     ,sum( case when c.customer_no <> '0' and t.itemOnlyCount <> 1 and get_visit_count(t.customer_id,t.shop_id,t.sales_date) = 1 then 1 else 0 end) as newCustomerCount");
            }else{
                sql.append("     ,sum( case when c.customer_no <> '0' and t.itemOnlyCount <> 1 and get_visit_count(t.customer_id,t.shop_id,t.sales_date) = 1 ");
                sql.append("    AND (SELECT count(slip_no) + coalesce(max(before_visit_num),0) FROM data_sales INNER JOIN mst_customer USING(customer_id) ");
                sql.append("    WHERE customer_id = t.customer_id AND data_sales.shop_id in (select shop_id from mst_shop where shop_id <> t.shop_id) ");
                sql.append("    AND data_sales.sales_date <= t.sales_date AND data_sales.delete_date IS NULL) < 1 ");
                sql.append("        then 1 else 0 end) as newCustomerCount ");
            }
                //IVS_TMTrong end edit 20150717
                sql.append("     /* ãZèpãqêî */");
                sql.append("     ,sum(t.techCount) as techCount");
                sql.append("     /* ãZèpîÑè„ */");
                sql.append("     ,sum(t.techSales) as techSales");
                sql.append("     /* è§ïiîÑè„ */");
                sql.append("     ,sum(t.itemSales) as itemSales");
                //Luc start edit 20150623 #37109
                //nhanvt start edit 20150202 Bug #35146
                //if(getTaxType() == TAX_TYPE_BLANK){
                //
                //    // ê≈î≤Ç´
                //    sql.append("     /* ëSëÃäÑà¯å„ê≈î≤Ç´ã‡äz */");
                //    sql.append("      ,sum( ");
                //    sql.append("             ( ");
                //    sql.append("             SELECT ");
                //    sql.append("              (sum(CASE WHEN dsd.product_division = 1 THEN discount_detail_value_no_tax ELSE 0 END)  + ");
                //    sql.append("             sum(CASE WHEN dsd.product_division = 3 THEN discount_detail_value_no_tax ELSE 0 END)  + ");
                //    sql.append("             sum(CASE WHEN dsd.product_division = 2 THEN discount_detail_value_no_tax ELSE 0 END)  + ");
                //
                //    sql.append("             sum(CASE WHEN dsd.product_division = 4 THEN discount_detail_value_no_tax ELSE 0 END)  ) as  discount_detail_value_no_tax ");
                //    sql.append("              FROM view_data_sales_detail_valid dsd ");
                //    sql.append("             WHERE shop_id = t.shop_id ");
                //    sql.append("               AND slip_no = t.slip_no) ");
                //    sql.append("               ) - (Select");
                //
                //    if (getTaxType()  == ReportParameterBean.TAX_TYPE_BLANK) {
                //        // ëSëÃäÑà¯
                //        sql.append(" sum(ds.discount_value_no_tax) as all_discount\n");
                //     } //ê≈çûÇ›ï\é¶
                //    else {
                //        sql.append(" sum(ds.discount_value) as all_discount\n");
                //    }
                //    sql.append(" from view_data_sales_valid ds\n"
                //    + "where ds.shop_id = t.shop_id\n"
                //
                //    + "and ds.sales_date between  date " + getTermFrom() + " -interval '1 year' and date " + getTermTo() + " -interval '1 year'\n");
                //    sql.append( "     and exists\n");
                //    sql.append( "         (\n");
                //    sql.append( "             select 1\n");
                //    sql.append( "             from\n");
                //    sql.append( "                 view_data_sales_detail_valid\n");
                //    sql.append( "             where\n");
                //    sql.append( "                     shop_id = ds.shop_id\n");
                //    sql.append( "                 and slip_no = ds.slip_no\n");
                //    sql.append( "                 and product_division in(1,2)\n");
                //    sql.append( "         ))AS totalSales\n");
                //
                //} else {
                //
                //    // ê≈çûÇ›
                //    sql.append("     /* ëSëÃäÑà¯å„ê≈çûÇ›ã‡äz */");
                //    sql.append("      ,sum( ");
                //    sql.append("             ( ");
                //    sql.append("             SELECT ");
                //    sql.append("              (sum(CASE WHEN dsd.product_division = 1 THEN discount_detail_value_in_tax ELSE 0 END)  + ");
                //    sql.append("             sum(CASE WHEN dsd.product_division = 3 THEN discount_detail_value_in_tax ELSE 0 END)  + ");
                //    sql.append("             sum(CASE WHEN dsd.product_division = 2 THEN discount_detail_value_in_tax ELSE 0 END)  + ");
                //    sql.append("             sum(CASE WHEN dsd.product_division = 4 THEN discount_detail_value_in_tax ELSE 0 END)  ) as  discount_sales_value_in_tax ");
                //    sql.append("              FROM view_data_sales_detail_valid dsd ");
                //    sql.append("             WHERE shop_id = t.shop_id ");
                //    sql.append("               AND slip_no = t.slip_no) ");
                //    sql.append("               ) - (Select");
                //
                //    if (getTaxType()  == ReportParameterBean.TAX_TYPE_BLANK) {
                //        // ëSëÃäÑà¯
                //        sql.append(" sum(ds.discount_value_no_tax) as all_discount\n");
                //     } //ê≈çûÇ›ï\é¶
                //    else {
                //        sql.append(" sum(ds.discount_value) as all_discount\n");
                //    }
                //    sql.append(" from view_data_sales_valid ds\n"
                //    + "where ds.shop_id = t.shop_id\n"
                //    + "and ds.sales_date between  date " + getTermFrom() + " -interval '1 year' and date " + getTermTo() + " -interval '1 year'\n");
                //
                //    sql.append( "     and exists\n");
                //    sql.append( "         (\n");
                //    sql.append( "             select 1\n");
                //    sql.append( "             from\n");
                //    sql.append( "                 view_data_sales_detail_valid\n");
                //    sql.append( "             where\n");
                //    sql.append( "                     shop_id = ds.shop_id\n");
                //    sql.append( "                 and slip_no = ds.slip_no\n");
                //    sql.append( "                 and product_division in(1,2)\n");
                //    sql.append( "         ))AS totalSales\n");
                //}
                //nhanvt start edit 20150202 Bug #35146
                sql.append( "         ,sum(t.techSales)+sum(t.itemSales) AS totalSales ");
                 //Luc end edit 20150623 #37109 
                sql.append(" from");
                sql.append("     (");
                sql.append("         select");
                sql.append("              shop_id");
                sql.append("             ,slip_no");
                sql.append("             ,sales_date");
                sql.append("             /* å⁄ãqID */");
                sql.append("             ,max(customer_id) as customer_id");
                sql.append("             /* è§ïiÇÃÇ›ãqêî */");
                sql.append("             ,case when sum( case when product_division = 2 then 1 else 0 end ) = count(*) then 1 else 0 end as itemOnlyCount");
                sql.append("             /* ãZèpãqêî */");
                sql.append("             ,count( distinct case when product_division = 1 then slip_no else null end ) as techCount");

                if(getTaxType() == TAX_TYPE_BLANK){

                    // ê≈î≤Ç´
                    sql.append("             /* ãZèpîÑè„(ê≈î≤) */");
                    sql.append("             ,sum( case when product_division = 1 then discount_detail_value_no_tax else 0 end ) as techSales");
                    sql.append("             /* è§ïiîÑè„(ê≈î≤) */");
                    sql.append("             ,sum( case when product_division = 2 then discount_detail_value_no_tax else 0 end ) as itemSales");

                } else {

                    // ê≈çûÇ›
                    sql.append("             /* ãZèpîÑè„(ê≈çû) */");
                    sql.append("             ,sum( case when product_division = 1 then discount_detail_value_in_tax else 0 end ) as techSales");
                    sql.append("             /* è§ïiîÑè„(ê≈çû) */");
                    sql.append("             ,sum( case when product_division = 2 then discount_detail_value_in_tax else 0 end ) as itemSales");
                }

                sql.append("         from");
                sql.append("             view_data_sales_detail_valid a");
                sql.append("         where");
                sql.append(" sales_date between  date " + getTermFrom() + " -interval '1 year' and date " + getTermTo() + " -interval '1 year'\n");

                //nhanvt start add 20141201 New request #33406
                if(this.getUseShopCategory() != null && this.getUseShopCategory() == 1){
                    sql.append("      and   exists");
                    sql.append("             (");

                    //ã∆ë‘óòópÇ∑ÇÈÇ©ÇµÇ»Ç¢Ç©É`ÉFÉbÉN

                    sql.append("                 SELECT");
                    sql.append("                     1");
                    sql.append("                 FROM");
                    sql.append("                     data_sales_detail dsd");
                    sql.append("                     inner join mst_technic mst on mst.technic_id = dsd.product_id ");
                    sql.append("                     left join mst_technic_class mstc on mstc.technic_class_id = mst.technic_class_id ");
                    sql.append("                 WHERE");
                    sql.append("                         dsd.shop_id = a.shop_id");
                    sql.append("                     AND dsd.slip_no = a.slip_no");
                    sql.append("                     and dsd.slip_detail_no = a.slip_detail_no   ");
                    sql.append("                     AND dsd.delete_date is null");
                    sql.append("                     AND dsd.product_division in (1,3)");
                    //ã∆ë‘ëIëÇÃèÍçá
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                        sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");
                    }

                    sql.append("                  UNION ALL  ");
                    sql.append("                  SELECT ");
                    sql.append("                     1");
                    sql.append("                 FROM");
                    sql.append("                      data_sales_detail dsd");
                    sql.append("                     inner join mst_item mi on dsd.product_id = mi.item_id and mi.delete_date is null ");
                    sql.append("                     left join mst_item_class mic on mic.item_class_id = mi.item_class_id and mic.delete_date is null ");
                    sql.append("                  WHERE");
                    sql.append("                          dsd.shop_id = a.shop_id");
                    sql.append("                      AND dsd.slip_no = a.slip_no");
                    sql.append("                     and dsd.slip_detail_no = a.slip_detail_no   ");
                    sql.append("                      AND dsd.delete_date is null");
                    sql.append("                      AND dsd.product_division in (2,4)");
                    //ã∆ë‘ëIëÇÃèÍçá
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                        sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");
                    }
                    sql.append("                 UNION ALL  ");
                    sql.append("                 SELECT ");
                    sql.append("                     1");
                    sql.append("                 FROM");
                    sql.append("                     data_sales_detail dsd");
                    sql.append("                     inner join mst_course msc on msc.course_id = dsd.product_id ");
                    sql.append("                     left join mst_course_class mscc on mscc.course_class_id = msc.course_class_id ");
                    sql.append("                 WHERE");
                    sql.append("                         dsd.shop_id = a.shop_id");
                    sql.append("                     AND dsd.slip_no = a.slip_no");
                    sql.append("                     and dsd.slip_detail_no = a.slip_detail_no   ");
                    sql.append("                     AND dsd.delete_date is null");
                    sql.append("                     AND dsd.product_division in (5,6)");
                    //ã∆ë‘ëIëÇÃèÍçá
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                        sql.append("                  AND mscc.shop_category_id in (" + this.getListCategoryId() + ") ");
                    }

                    sql.append(" ) ");
                }
                //nhanvt end add 20141201 New request #33406
                sql.append("         group by");
                sql.append("              shop_id");
                sql.append("             ,slip_no");
                sql.append("             ,sales_date");
                sql.append("     ) t");
                sql.append("     inner join mst_customer c");
                sql.append("             on t.customer_id = c.customer_id");
                sql.append(" group by");
                sql.append("     t.shop_id");
            
	    return sql.toString();
	}
        /**
         * 
         * @return 
         */
        public String getTargetResult(){
            StringBuilder sql = new StringBuilder(1000);
            
            sql.append("     SELECT dt.shop_id, ");
            sql.append("             '' AS shop_name, ");
            sql.append("             0 AS customerCount, ");
            sql.append("             coalesce(sum(target_technic_num), 0) AS techCount, ");
            sql.append("              coalesce(sum(target_technic),0) AS techSales,");
            sql.append("                         coalesce(sum(target_new_num), 0) AS newCustomerCount,");
            sql.append("                                   coalesce(sum(target_item),0) AS itemSales,");
            sql.append("          sum(target_technic_num + target_item_num) AS totalCount, ");
            sql.append("          sum(target_technic + target_item) AS totalSales, ");
            sql.append("          sum("+getNameColumn()+") as repeat_rate,  ");
            sql.append(" sum(target_staff_num) as staff_count ");     
            sql.append("      FROM data_target_result dt ");
            sql.append(" where dt.delete_date is null ");
            
            if(getYearMonth().size() >0){
                sql.append(" and (");
                Iterator iterator = getYearMonth().entrySet().iterator();
                int count = 0;
                while (iterator.hasNext()) {
                      Map.Entry mapEntry = (Map.Entry) iterator.next();
                      if(count >0){
                        sql.append(" or ");
}
                      sql.append("( dt.year = " + mapEntry.getKey());
                      sql.append(" and dt.month in (" );
                      List<Integer> lstTmp = new ArrayList<Integer>();
                      lstTmp = (List<Integer>)mapEntry.getValue();
                      int countPoint = 0;
                      for(int i = 0; i< lstTmp.size(); i++){
                          countPoint++;
                          sql.append(lstTmp.get(i) );
                          if(countPoint < lstTmp.size()){
                              sql.append(",");
                          }
                      }
                      sql.append(") )");
                      count++;
                }
                sql.append(" )");
            }
            sql.append("      GROUP BY dt.shop_id ");
           
            return sql.toString();
        }
        //nhanvt end add 20150309 New request #35223
        
        /**
         * get month and year
         * @return 
         */
        private Map<Integer, List<Integer>> getYearMonth(){
            Integer yearStart = Integer.parseInt(getTermFrom().substring(1, 5));
            Integer yearEnd = Integer.parseInt(getTermTo().substring(1, 5));
            Integer monthStart = Integer.parseInt(getTermFrom().substring(6, 8));
            Integer monthEnd = Integer.parseInt(getTermTo().substring(6, 8));
            Map<Integer, List<Integer>> listData = new HashMap<Integer, List<Integer>>();
            if(yearEnd.equals(yearStart)){
                List<Integer> lstMonth = new ArrayList<Integer>();
                for(Integer i=monthStart; i< monthEnd+1; i++){
                    lstMonth.add(i);
                }
                listData.put(yearStart, lstMonth);
            }else{
                if(yearEnd > yearStart){
                    List<Integer> lstMonthStart = new ArrayList<Integer>();
                    for(Integer i=monthStart; i< 13; i++){
                        lstMonthStart.add(i);
                    }
                    listData.put(yearStart, lstMonthStart);
                    List<Integer> lstMonthEnd = new ArrayList<Integer>();
                    for(Integer i=1; i< monthEnd+1; i++){
                        lstMonthEnd.add(i);
                    }
                    listData.put(yearEnd, lstMonthEnd);
                    
                    if(yearEnd - yearStart > 1){
                        List<Integer> lstMonthAll = new ArrayList<Integer>();
                        for(Integer i=yearStart+1; i< yearEnd; i++){
                            lstMonthAll =  new ArrayList<Integer>();
                            for(Integer k= 1; k< 13; k++){
                                lstMonthAll.add(k);
                            }
                            listData.put(i, lstMonthAll);
                        }
                    }
                }
            }
            if(listData.size() >0){
                Iterator iterator = listData.entrySet().iterator();
                int countMonthTmp = 0;
                while (iterator.hasNext()) {
                      Map.Entry mapEntry = (Map.Entry) iterator.next();
                      List<Integer> lstTmp = new ArrayList<Integer>();
                      lstTmp = (List<Integer>)mapEntry.getValue();
                      countMonthTmp += lstTmp.size();
                }
                this.setCountMonth(countMonthTmp);
            }
           
            return listData;
        }
        
        
}
