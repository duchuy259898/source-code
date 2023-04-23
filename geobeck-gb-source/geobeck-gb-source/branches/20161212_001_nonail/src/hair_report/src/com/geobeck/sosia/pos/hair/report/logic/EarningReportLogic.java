/*
 * ReportLogic.java
 *
 * Created on 2006/05/18, 21:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.report.logic;

import com.geobeck.sosia.pos.hair.report.util.*;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.report.bean.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.*;
import com.geobeck.util.*;
import java.util.*;

/**
 * ÉåÉ|Å[ÉgÉçÉWÉbÉN
 *
 * @author k.anayama
 */
public class EarningReportLogic {

    public static int REPORT_TYPE_PDF = 0;
    public static int REPORT_TYPE_EXCEL = 1;
    public static int STAFF_REPORT_COLUMN_COUNT = 6;
    public static String REPORT_RESOURCE_PATH = "/reports/";
    public static String REPORT_EXPORT_PATH = "./";
    public static String REPORT_XML_FILE_EXT = ".jasper";
    public static String REPORT_FILE_STAFF_TECHNICAL = "StaffReportTechnic";
    public static String REPORT_FILE_STAFF_TECHNICAL_CONTINUE = "StaffReportTechnicContinue";
    public static String REPORT_FILE_STAFF_GOODS = "StaffReportItem";
    public static String REPORT_FILE_STAFF_GOODS_CONTINUE = "StaffReportItemContinue";
    public static String REPORT_FILE_STAFF_CUSTOMER_SALES = "StaffReportCustomerSales";
    public static String REPORT_FILE_OCCUPATION_RATIO = "StaffReportOccupationRatio";
    public static String REPORT_FILE_PROPORTIONALLY_DISTRIBUTION = "StaffReportPropDistribution";
    public static int ORDER_DISPLAY_KINGAKU = 0;
    public static int ORDER_DISPLAY_POINT = 1;
    //ExcelÇPÉVÅ[ÉgìñÇËÇÃà¬ï™êî
    public static int PROPORTIONALLY_COUNT_OF_ONE_EXCEL_SHEET = 15;
    //à¬ï™ê¨ê—ÇÃÉ\Å[Égóp
    private int proportionallyOrderDisplay;

    /**
     * ÉRÉìÉXÉgÉâÉNÉ^
     */
    public EarningReportLogic() {
    }

    /**
     * à¬ï™ê¨ê—ÇÃÉ\Å[ÉgópÉNÉâÉX
     */
    public class StaffReportBeanProportionallyComparator implements Comparator {

        public int compare(Object object1, Object object2) {
            StaffReportBeanProportionally srbp1 = (StaffReportBeanProportionally) object1;
            StaffReportBeanProportionally srbp2 = (StaffReportBeanProportionally) object2;

            //ñﬂÇËíl
            int ret = 0;
            if (proportionallyOrderDisplay == ORDER_DISPLAY_KINGAKU) {
                //ã‡äzÇÃç~èáÇ≈ï¿Ç—ë÷Ç¶
                Long kingaku1 = new Long(srbp1.getTotalKingaku());
                Long kingaku2 = new Long(srbp2.getTotalKingaku());
                ret = -(kingaku1.compareTo(kingaku2));
            } else if (proportionallyOrderDisplay == ORDER_DISPLAY_POINT) {
                //É|ÉCÉìÉgÇÃç~èáÇ≈ï¿Ç—ë÷Ç¶
                Double point1 = new Double(srbp1.getTotalPoint());
                Double point2 = new Double(srbp2.getTotalPoint());
                ret = -(point1.compareTo(point2));
            }
            return ret;
        }
    }

    public boolean viewSaleAnalysis(ReportParameterBean paramBean, boolean isPDF) throws Exception {

        StringBuffer sql = new StringBuffer();

        String fileName = "î‰ärèWåvï\";
        MstShop shop = new MstShop();
        shop.setShopID(paramBean.getShopId());
        shop.load(SystemInfo.getConnection());
        String title = shop.getShopName() + " îÑè„î‰ärèWåvï\";
        JExcelApi jx = new JExcelApi(fileName);

        Calendar cal = Calendar.getInstance();
        cal.setTime(paramBean.getCalculationStartDateObj());
        cal.clone();

        String startDay = DateUtil.format(paramBean.getCalculationStartDateObj(), "yyyy/MM/dd HH:mm").substring(0, 11).split("/")[2];
        String startMonth = DateUtil.format(paramBean.getCalculationStartDateObj(), "yyyy/MM/dd HH:mm").substring(0, 11).split("/")[1];
        String startYear = DateUtil.format(paramBean.getCalculationStartDateObj(), "yyyy/MM/dd HH:mm").substring(0, 11).split("/")[0];

        String endDay = DateUtil.format(paramBean.getCalculationEndDateObj(), "yyyy/MM/dd HH:mm").substring(0, 11).split("/")[2];
        String endMonth = DateUtil.format(paramBean.getCalculationEndDateObj(), "yyyy/MM/dd HH:mm").substring(0, 11).split("/")[1];
        String endYear = DateUtil.format(paramBean.getCalculationEndDateObj(), "yyyy/MM/dd HH:mm").substring(0, 11).split("/")[0];

        //ÉeÉìÉvÉåÅ[ÉgÇ∆Ç»ÇÈÉtÉ@ÉCÉãÇÉZÉbÉg
        jx.setTemplateFile("/reports/" + fileName + ".xls");
        jx.setValue(1, 1, endYear + "îN" + endMonth + "åé" + endDay + "ì˙");
        jx.setValue(16, 1, title);
        
        try {            
            sql.append("select  \n");
            sql.append("a.shop_id,a.shop_name  \n");
            sql.append(", sum(a.col2) as col2  \n");
            sql.append(", sum(a.col3) as col3  \n");
            sql.append(",sum(a.col4) as col4  \n");
            sql.append(", sum(a.col5) as col5  \n");
            sql.append(", sum(a.col6) as col6  \n");
            sql.append(",sum(a.col7) as col7  \n");
            sql.append(",sum(a.col8) as col8  \n");
            sql.append(",sum(a.col9) as col9  \n");
            sql.append(",sum(a.col10) as col10  \n");
            sql.append(", sum(a.col11) as col11  \n");
            sql.append(",sum(a.col12) as col12  \n");
            sql.append(", sum(a.col13notax) as col13notax  \n");
            sql.append(",sum(a.col13intax) as col13intax  \n");
            sql.append(", sum(a.col14 ) as col14  \n");
            sql.append(", sum(a.col15 ) as col15  \n");
            sql.append(", sum(a.col16notax ) as col16notax  \n");
            sql.append(", sum(a.col16intax ) as col16intax  \n");
            sql.append(", sum(a.col19) as col19  \n");
            sql.append(",sum(a.col20) as col20  \n");
            sql.append(", sum(a.col21) as col21 ");
            sql.append(", sum(a.col22) as col22  \n");
            sql.append(",sum(a.col23) as col23  \n");
            sql.append(",sum(a.col24) as col24  \n");
            sql.append(",sum(a.col2)/sum(a.col20::numeric) as col25  \n");
            sql.append(",sum(a.col3)/sum(a.col20::numeric) as col26  \n");
            sql.append(", sum(a.col4)/sum(a.col20::numeric) as col27  \n");
            sql.append(",sum(a.col5)/sum(a.col20::numeric) as col28  \n");
            sql.append(", sum(a.col6)/sum(a.col20::numeric)  as col29  \n");
            sql.append(",  sum(a.col7)/sum(a.col20::numeric) as col30  \n");
            sql.append(",sum(a.col8)/sum(a.col20::numeric) as col31  \n");
            sql.append(",sum(a.col9)/sum(a.col20::numeric) as col32  \n");
            sql.append(",sum(a.col10)/sum(a.col20::numeric) as col33  \n");
            sql.append(", sum(a.col11)/sum(col20)::numeric as col34  \n");
           // sql.append(", ( sum(a.col16notax)/ ( sum(a.col13intax::numeric) + sum(a.col16intax::numeric ))) as col35  \n");
            sql.append(", ( sum(a.col16notax)/ ( sum(a.col13notax::numeric) + sum(a.col16notax::numeric )))   as col35  \n");
            sql.append(", sum(a.col16notax)/sum(a.col20::numeric) as col36  \n");
            sql.append(", sum(a.col13notax)/sum(a.col20::numeric) as col37  \n");
            //sql.append(",( sum(a.col13intax) + sum(a.col16intax) ) /sum(a.col20::numeric) as col38  \n");
            sql.append(",( sum(a.col13notax) + sum(a.col16notax) ) /sum(a.col20::numeric) as col38  \n");

            sql.append("from   \n");
            sql.append("(	select distinct   \n");
            // Thanh start edit 2014/06/03
            if (SystemInfo.getCurrentShop().getShopID() == 0) {
                sql.append(" 0 as shop_id, " + SQLUtil.convertForSQL(paramBean.getTargetName()) + " as shop_name");
            }
            else
            {
                sql.append("shop.shop_id,shop.shop_name   \n");
            }
            // Thanh start edit 2014/06/03
            sql.append(", coalesce(integration1.ÉJÉbÉgÇÃêîó ,0) as col2  \n");
            sql.append(",coalesce (integration1.ÉJÉbÉgÇÃã‡äz,0) as col3  \n");
            sql.append(",coalesce (integration2.ÉpÅ[É}ÇÃêîó ,0) as col4  \n");
            sql.append(",coalesce (integration2.ÉpÅ[É}ÇÃã‡äz,0) as col5  \n");
            sql.append(", coalesce(integration3.ÉJÉâÅ[ÇÃêîó , 0) as col6  \n");
            sql.append(",coalesce (integration3.ÉJÉâÅ[ÇÃã‡äz,0) as col7  \n");
            sql.append(",coalesce (integration4.ÉXÉpÇÃêîó ,0) as col8  \n");
            sql.append(",coalesce (integration4.ÉXÉpÇÃã‡äz,0) as col9  \n");
            sql.append(",coalesce (integration5.ƒÿ∞ƒ“›ƒÇÃêîó ,0) as col10  \n");
            sql.append(",coalesce (integration5.ƒÿ∞ƒ“›ƒÇÃã‡äz,0) as col11  \n");
            sql.append(",coalesce (division13.ãZèpîÑè„ÇÃêîó ,0) as col12  \n");
            sql.append(",coalesce (division13.ãZèpîÑè„ÇÃã‡äznotax,0) as col13notax  \n");
            sql.append(",coalesce (division13.ãZèpîÑè„ÇÃã‡äzintax,0) as col13intax  \n");
            sql.append(",coalesce (pro13.ãZèpé¿ ,0) as col14   \n");
            sql.append(",coalesce (division24.ìXîÃîÑè„ÇÃêîó  ,0) as col15  \n");
            sql.append(",coalesce (division24.ìXîÃîÑè„ÇÃã‡äznotax ,0) as col16notax   \n");
            sql.append(",coalesce (division24.ìXîÃîÑè„ÇÃã‡äzintax ,0) as col16intax   \n");
            sql.append(",coalesce (pro24.ìXîÃé¿intax ,0) as col19  \n");
            sql.append(",coalesce (countslip.col20 ,0) as col20  \n");
            sql.append(",coalesce (work.HELP_POINT ,0) as col21  \n");
            sql.append(",coalesce (countnewcustomer.col22 ,0) as col22  \n");
            sql.append(",coalesce (customerdesignated.col23 ,0) as col23  \n");
            sql.append(",coalesce (cusapproached.col24,0) as col24   \n");
            sql.append("from  \n");
            sql.append("    (select distinct shop_id,shop_name  \n");
            // Thanh start edit 2014/06/03
            sql.append("        from mst_shop where shop_id in (" + paramBean.getShopIDList() + ")) shop \n");
            // Thanh end edit 2014/06/03
            sql.append("left join (   \n");
            sql.append("        select a.shop_id,sum(discount_detail_value_no_tax) as ÉJÉbÉgÇÃã‡äz,count(distinct a.slip_no) as ÉJÉbÉgÇÃêîó     \n");
            sql.append("        from view_data_sales_detail_valid a  \n");
            sql.append("        left join mst_technic b on a.product_id=b.technic_id  \n");
            sql.append("        left join mst_technic_class c on b.technic_class_id=c.technic_class_id  \n");
            sql.append("        left join mst_staff d on a.detail_staff_id=d.staff_id and a.shop_id = d.shop_id   \n");
            sql.append("        inner join mst_technic_integration e on c.technic_integration_id=e.technic_integration_id  \n");
            sql.append("        where e.technic_integration_id=1  \n");
            sql.append("        and a.product_division in ('1','3')  \n");
            sql.append("        and a.sales_date between  " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) +" \n");
            sql.append("        group by a.shop_id   \n");
            sql.append("        ) integration1 on shop.shop_id=integration1.shop_id   \n");
            sql.append("left join (   \n");
            sql.append("        select a.shop_id,sum(discount_detail_value_no_tax) as ÉpÅ[É}ÇÃã‡äz,count(distinct  a.slip_no) as ÉpÅ[É}ÇÃêîó     \n");
            sql.append("        from view_data_sales_detail_valid a  \n");
            sql.append("        left join mst_technic b on a.product_id=b.technic_id  \n");
            sql.append("        left join mst_technic_class c on b.technic_class_id=c.technic_class_id  \n");
            sql.append("        left join mst_staff d on a.detail_staff_id=d.staff_id and a.shop_id = d.shop_id   \n");
            sql.append("        inner join mst_technic_integration e on c.technic_integration_id=e.technic_integration_id  \n");
            sql.append("        where e.technic_integration_id=2  \n");
            sql.append("            and a.product_division = 1  \n");
            sql.append("            and a.sales_date between  " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) +" \n");
            sql.append("        group by a.shop_id  \n");
            sql.append("        ) integration2 on shop.shop_id=integration2.shop_id  \n");
            sql.append("left join (  \n");
            sql.append("        select a.shop_id,sum(discount_detail_value_no_tax) as ÉJÉâÅ[ÇÃã‡äz,count(distinct  a.slip_no) as ÉJÉâÅ[ÇÃêîó   \n");
            sql.append("        from view_data_sales_detail_valid a  \n");
            sql.append("        left join mst_technic b on a.product_id=b.technic_id  \n");
            sql.append("        left join mst_technic_class c on b.technic_class_id=c.technic_class_id  \n");
            sql.append("        left join mst_staff d on a.detail_staff_id=d.staff_id and a.shop_id = d.shop_id   \n");
            sql.append("        inner join mst_technic_integration e on c.technic_integration_id=e.technic_integration_id  \n");
            sql.append("        where e.technic_integration_id=3  \n");
            sql.append("            and a.product_division = 1  \n");
            sql.append("            and a.sales_date between  " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) +" \n");
            sql.append("        group by a.shop_id   \n");
            sql.append("        ) integration3 on shop.shop_id=integration3.shop_id  \n");
            sql.append("left join (   \n");
            sql.append("        select a.shop_id,sum(discount_detail_value_no_tax) as ÉXÉpÇÃã‡äz,count(distinct  a.slip_no) as ÉXÉpÇÃêîó      \n");
            sql.append("        from view_data_sales_detail_valid a  \n");
            sql.append("        left join mst_technic b on a.product_id=b.technic_id  \n");
            sql.append("        left join mst_technic_class c on b.technic_class_id=c.technic_class_id  \n");
            sql.append("        left join mst_staff d on a.detail_staff_id=d.staff_id and a.shop_id = d.shop_id   \n");
            sql.append("        inner join mst_technic_integration e on c.technic_integration_id=e.technic_integration_id  \n");
            sql.append("        where e.technic_integration_id=4  \n");
            sql.append("        and a.product_division = 1  \n");
            sql.append("        and a.sales_date between  " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) +" \n");
            sql.append("        group by a.shop_id   \n");
            sql.append("        ) integration4 on shop.shop_id=integration4.shop_id   \n");
            sql.append("left join (   \n");
            sql.append("        select a.shop_id,sum(discount_detail_value_no_tax) as ƒÿ∞ƒ“›ƒÇÃã‡äz ,count(distinct  a.slip_no) as  ƒÿ∞ƒ“›ƒÇÃêîó \n");
            sql.append("        from view_data_sales_detail_valid a  \n");
            sql.append("        left join mst_technic b on a.product_id=b.technic_id  \n");
            sql.append("        left join mst_technic_class c on b.technic_class_id=c.technic_class_id  \n");
            sql.append("        left join mst_staff d on a.detail_staff_id=d.staff_id and a.shop_id = d.shop_id   \n");
            sql.append("        inner join mst_technic_integration e on c.technic_integration_id=e.technic_integration_id \n");
            sql.append("        where e.technic_integration_id=5  \n");
            sql.append("        and a.product_division = 1  \n");
            sql.append("        and a.sales_date between  " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) +" \n");
            sql.append("        group by a.shop_id   \n");
            sql.append("        ) integration5 on shop.shop_id=integration5.shop_id  \n");
            sql.append("left join (   \n");
            sql.append("        select a.shop_id,sum(a.discount_detail_value_no_tax) as ãZèpîÑè„ÇÃã‡äznotax \n");
            sql.append("        ,sum(a.discount_detail_value_in_tax) as ãZèpîÑè„ÇÃã‡äzintax  \n");
            sql.append("        ,sum(a.product_num) as ãZèpîÑè„ÇÃêîó     \n");
            sql.append("        from view_data_sales_detail_valid a  \n");
            sql.append("        left join mst_staff d on a.detail_staff_id=d.staff_id and a.shop_id = d.shop_id   \n");
            sql.append("         where a.product_division in ('1','3')    \n");
            sql.append("        and a.sales_date between  " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) +" \n");
            sql.append("        group by a.shop_id ) division13 on shop.shop_id=division13.shop_id   \n");
            sql.append("left join (   \n");
            sql.append("            select a.shop_id  \n");
            sql.append("            ,sum(a.ãZèpé¿) as ãZèpé¿  \n");
            sql.append("            from (  \n");
            sql.append("                select a.shop_id, b.staff_id ,  \n");
            sql.append("                trunc( ( ( discount_detail_value_no_tax )  * ( ratio) ) / 100 ) as ãZèpé¿  \n");
            sql.append("                from view_data_sales_detail_valid a  \n");
            sql.append("                Inner join data_sales_proportionally b on a.slip_no =b.slip_no and a.slip_detail_no =b.slip_detail_no  and a.shop_id = b.shop_id and a.staff_id = b.staff_id    \n");
            sql.append("                INNER JOIN data_proportionally f   \n");
            sql.append("                ON f.data_proportionally_id = b.data_proportionally_id   \n");
            sql.append("                where a.product_division in('1','3') 	  \n");
            sql.append("                and a.sales_date between  " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) +" \n");
            sql.append("                and ratio != 0    \n");
            sql.append("                and b.staff_id != 0  and b.delete_date is null  \n");
            sql.append("              ) a group by a.shop_id 		 \n");
            sql.append("         ) pro13 on shop.shop_id=pro13.shop_id   \n");
            sql.append("left join (   \n");
            sql.append("        select a.shop_id,sum(a.discount_detail_value_no_tax) as ìXîÃîÑè„ÇÃã‡äznotax  \n");
            sql.append("        ,sum(a.discount_detail_value_in_tax) as ìXîÃîÑè„ÇÃã‡äzintax  \n");
            // Thanh start edit 2014/06/02           
            sql.append("        ,count(distinct a.slip_no) as ìXîÃîÑè„ÇÃêîó      \n");
            // Thanh end edit 2014/06/02
            sql.append("        from view_data_sales_detail_valid a  \n");
            sql.append("        INNER JOIN mst_item mi		  \n");
            sql.append("        ON a.product_id = mi.item_id		  \n");
            sql.append("        left join mst_staff d on a.detail_staff_id=d.staff_id and a.shop_id = d.shop_id   \n");
            sql.append("        where a.product_division in ('2','4')  \n");
            sql.append("        and   mi.item_use_division IN ( '1', '3')  \n");
            sql.append("        and a.sales_date between  " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) +" \n");
            sql.append("    group by a.shop_id ) division24 on shop.shop_id=division24.shop_id   \n");
            sql.append("left join (   \n");
            sql.append("        select a.shop_id,sum(a.discount_detail_value_no_tax) as ìXîÃé¿notax  \n");
            sql.append("        ,sum(a.discount_detail_value_in_tax) as ìXîÃé¿intax  \n");
            sql.append("        from view_data_sales_detail_valid a  \n");
            // Thanh start edit 2014/06/02 Bug #2459
            //sql.append("        inner JOIN data_sales_proportionally b   \n");
            //sql.append("        ON  a.shop_id        = b.shop_id	 \n");
            //sql.append("        AND a.slip_no        = b.slip_no		  \n");
            //sql.append("        AND a.slip_detail_no = b.slip_detail_no				  \n");
            //sql.append("        AND a.staff_id = b.staff_id				  \n");            
            sql.append("        INNER JOIN mst_item mi		  \n");
            sql.append("        ON a.product_id = mi.item_id		  \n");
            sql.append("        left join mst_staff d on a.detail_staff_id=d.staff_id and a.shop_id = d.shop_id   \n");
            sql.append("        where a.product_division in ('2')  \n");
            sql.append("        and   mi.item_use_division IN ( '1', '3')  \n");
            sql.append("        and detail_staff_id is not null   \n");
            // Thanh end edit 2014/06/02 Bug #2459
            sql.append("        and a.sales_date between  " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) +" \n");
            sql.append("        group by a.shop_id ) pro24 on shop.shop_id=pro24.shop_id  \n");
            sql.append("left join (  \n");
            sql.append("        SELECT a.shop_id,	count( distinct cast(a.shop_id as text) || cast(a.slip_no as text)   ) 	as col20  \n");
            sql.append("        FROM 	view_data_sales_detail_valid a	  \n");
            sql.append("        LEFT JOIN mst_staff c	ON  a.detail_staff_id = c.staff_id	  \n");
            sql.append("        WHERE 	a.sales_date between  " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) +" \n");
            sql.append("        group by a.shop_id  \n");
            sql.append("        ) countslip  on shop.shop_id = countslip.shop_id  \n");
            sql.append("LEFT JOIN (  \n");
            
            sql.append("SELECT	\n");
            sql.append("WORK1.shop_id,	\n");
            sql.append("ROUND( AVG( ( WORK1.point / WORK2.point) * 100 ) ) /100   AS HELP_POINT	\n");
            sql.append("FROM	\n");
            sql.append("( SELECT	\n");
            sql.append("C.shop_id,	\n");
            sql.append("C.slip_no,	\n");
            sql.append("C.slip_detail_no,	\n");
            sql.append("A.staff_id AS S_staff_id,	\n");
            sql.append("SUM( C.point ) AS point	\n");
            sql.append("FROM	\n");
            sql.append("data_sales A	\n");
            sql.append("INNER JOIN	\n");
            sql.append("data_sales_detail B	\n");
            sql.append("ON  A.shop_id        = B.shop_id	\n");
            sql.append("AND A.slip_no        = B.slip_no	\n");
            sql.append("INNER JOIN	\n");
            sql.append("data_sales_proportionally C 	\n");
            sql.append("ON  B.shop_id        = C.shop_id	\n");
            sql.append("AND B.slip_no        = C.slip_no	\n");
            sql.append("AND B.slip_detail_no = C.slip_detail_no	\n");
            sql.append("AND B.product_division  IN ( 1, 3 )	\n");
            sql.append("WHERE	\n");
            sql.append("        a.sales_date between  " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) +" \n");
            sql.append("AND A.staff_id       != C.staff_id	\n");
            sql.append("AND A.delete_date is null	\n");
            sql.append("AND B.delete_date is null	\n");
            sql.append("GROUP BY	\n");
            sql.append("C.shop_id,	\n");
            sql.append("C.slip_no,	\n");
            sql.append("C.slip_detail_no,	\n");
            sql.append("A.staff_id,	\n");
            sql.append("C.staff_id ) WORK1	\n");
            sql.append("INNER JOIN	\n");
            sql.append("(  SELECT	\n");
            sql.append("C.shop_id,	\n");
            sql.append("C.slip_no,	\n");
            sql.append("C.slip_detail_no,	\n");
            sql.append("A.staff_id AS S_staff_id,	\n");
            sql.append("SUM( C.point ) AS point	\n");
            sql.append("FROM	\n");
            sql.append("data_sales A	\n");
            sql.append("INNER JOIN	\n");
            sql.append("data_sales_detail B	\n");
            sql.append("ON  A.shop_id        = B.shop_id	\n");
            sql.append("AND A.slip_no        = B.slip_no	\n");
            sql.append("INNER JOIN	\n");
            sql.append("data_sales_proportionally C 	\n");
            sql.append("ON  B.shop_id        = C.shop_id	\n");
            sql.append("AND B.slip_no        = C.slip_no	\n");
            sql.append("AND B.slip_detail_no = C.slip_detail_no	\n");
            sql.append("AND B.product_division  IN ( 1, 3 )	\n");
            sql.append("WHERE	\n");
            sql.append("    a.sales_date between  " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) +" \n");
            sql.append("AND A.delete_date is null	\n");
            sql.append("AND B.delete_date is null	\n");
            sql.append("AND C.delete_date is null	\n");
            sql.append("GROUP BY	\n");
            sql.append("C.shop_id,	\n");
            sql.append("C.slip_no,	\n");
            sql.append("C.slip_detail_no,	\n");
            sql.append("A.staff_id,	\n");
            sql.append("C.staff_id ) WORK2	\n");
            sql.append("ON	\n");
            sql.append("WORK1.shop_id        =  WORK2.shop_id	\n");
            sql.append("AND WORK1.slip_no        =  WORK2.slip_no	\n");
            sql.append("AND WORK1.slip_detail_no =  WORK2.slip_detail_no	\n");
            sql.append("AND WORK1.point          != 0	\n");
            sql.append("AND WORK2.point          != 0	\n");
            sql.append("INNER JOIN mst_staff D	\n");
            sql.append("ON  work1.S_staff_id = d.staff_id 	\n");
            sql.append("GROUP BY	\n");
            sql.append("WORK1.shop_id\n");
            sql.append("            ) work on work.shop_id = shop.shop_id \n");           
            sql.append("LEFT JOIN(  \n");
            sql.append("        SELECT a.shop_id,  \n");
            sql.append("        sum(a.êVãK) AS col22  \n");
            sql.append("        FROM  \n");
            sql.append("        ( SELECT a.shop_id,  \n");
            sql.append("        count(DISTINCT a.customer_id)AS êVãK  \n");
            sql.append("        FROM view_data_sales_detail_valid a  \n");
            sql.append("        LEFT JOIN mst_staff d ON a.staff_id=d.staff_id  \n");
            sql.append("        AND a.shop_id = d.shop_id  \n");
            sql.append("         WHERE a.visit_num=1  \n");
            sql.append("         AND a.sales_date between  " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) +" \n");
            sql.append("        GROUP BY a.shop_id,  \n");
            sql.append("             a.customer_id ) a  \n");
            sql.append("        GROUP BY a.shop_id ) countnewcustomer ON shop.shop_id=countnewcustomer.shop_id  \n");
            sql.append("  LEFT JOIN  \n");
            sql.append("        (SELECT a.shop_id,  \n");
            sql.append("            sum(a.éwêV) AS col23  \n");
            sql.append("        FROM  \n");
            sql.append("            ( SELECT a.shop_id,  \n");
            sql.append("              count(DISTINCT a.customer_id)AS éwêV  \n");
            sql.append("              FROM view_data_sales_detail_valid a  \n");
            sql.append("              LEFT JOIN mst_staff d ON a.staff_id=d.staff_id  \n");
            sql.append("              AND a.shop_id = d.shop_id  \n");
            sql.append("              WHERE a.visit_num=1  \n");
            sql.append("              AND a.sales_date between  " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) +" \n");
            sql.append("              AND designated_flag=TRUE  \n");
            sql.append("              GROUP BY a.shop_id  ) a   \n");
            sql.append("  GROUP BY a.shop_id ) customerdesignated ON shop.shop_id=customerdesignated.shop_id  \n");

            // Thanh start edit 2014/06/02 Bug #24591
            sql.append("LEFT JOIN  \n");
            sql.append(" (SELECT a.shop_id,  \n");
            sql.append("        sum(a.è–êV) AS col24  \n");
            sql.append("        FROM  \n");
            sql.append("        ( SELECT a.shop_id,  \n");
            sql.append("              coalesce(count(ms.introducer_id),0)AS è–êV  \n");
            sql.append("            FROM data_sales a  \n");           
            sql.append("            LEFT JOIN mst_staff d ON a.staff_id=d.staff_id  \n");
            sql.append("            INNER JOIN mst_customer ms on ms.customer_id = a.customer_id   \n");
            sql.append("            AND a.shop_id = d.shop_id  \n");
            sql.append("             WHERE a.visit_num=1 and ms.introducer_id is not null  \n");
            sql.append("            AND a.sales_date between  " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) +" \n");
            sql.append("            GROUP BY a.shop_id ) a   \n");
            sql.append("    GROUP BY a.shop_id ) cusapproached ON shop.shop_id=cusapproached.shop_id  \n");
             // Thanh start edit 2014/06/02 Bug #24591

            sql.append(")a  \n");
            sql.append("group by a.shop_id,a.shop_name  \n");
            
            ConnectionWrapper con = SystemInfo.getConnection();
            ResultSetWrapper rs = con.executeQuery(sql.toString());
            int i = 0;
            int rowCount = 0;

            while (rs.next()) {
                jx.setValue(1, 4,  rs.getString("shop_name"));
                jx.setValue(2, 4,  rs.getInt("col2"));
                jx.setValue(3, 4,  rs.getInt("col3"));
                jx.setValue(4, 4,  rs.getInt("col4"));
                jx.setValue(5, 4,  rs.getInt("col5"));
                jx.setValue(6, 4,  rs.getInt("col6"));
                jx.setValue(7, 4,  rs.getInt("col7"));
                jx.setValue(8, 4,  rs.getInt("col8"));
                jx.setValue(9, 4,  rs.getInt("col9"));
                jx.setValue(10, 4, rs.getInt("col10"));
                jx.setValue(11, 4, rs.getInt("col11"));
//                jx.setValue(12, 4, rs.getInt("col12"));
                // Thanh start edit 2014/06/02 Bug #24591
                jx.setValue(13, 4, rs.getInt("col13notax"));
                // Thanh end edit 2014/06/02
                jx.setValue(14, 4, rs.getDouble("col14"));
                jx.setValue(15, 4, rs.getInt("col15"));
                jx.setValue(16, 4, rs.getInt("col16notax"));
                jx.setValue(17, 4, rs.getInt("col13intax") + rs.getInt("col16intax"));
                 // Thanh start edit 2014/06/02 Bug #24591
                jx.setValue(18, 4,rs.getInt("col13notax") + rs.getInt("col16notax"));
                // Thanh end edit 2014/06/02
                jx.setValue(19, 4, rs.getInt("col19"));
                jx.setValue(20, 4, rs.getInt("col20"));
                //jx.setValue(21, 4, rs.getDouble("col21"));
                jx.setValue(22, 4, rs.getInt("col22"));
                jx.setValue(23, 4, rs.getInt("col23"));
                jx.setValue(24, 4, rs.getInt("col24"));
                jx.setValue(25, 4, rs.getInt("col20") == 0 ? 0 : rs.getDouble("col2") / rs.getDouble("col20"));
                jx.setValue(26, 4, rs.getInt("col2") == 0 ? 0 : rs.getDouble("col3") / rs.getDouble("col2"));
                jx.setValue(27, 4, rs.getInt("col20") == 0 ? 0 : rs.getDouble("col4") / rs.getDouble("col20"));
                jx.setValue(28, 4, rs.getInt("col4") == 0 ? 0 : rs.getDouble("col5") / rs.getDouble("col4"));
                jx.setValue(29, 4, rs.getInt("col20") == 0 ? 0 : rs.getDouble("col6") / rs.getDouble("col20"));
                jx.setValue(30, 4, rs.getInt("col6") == 0 ? 0 : rs.getDouble("col7") / rs.getDouble("col6"));
                jx.setValue(31, 4, rs.getInt("col20") == 0 ? 0 : rs.getDouble("col8") / rs.getDouble("col20"));
                jx.setValue(32, 4, rs.getInt("col8") == 0 ? 0 : rs.getDouble("col9") / rs.getDouble("col8"));
                jx.setValue(33, 4, rs.getInt("col20") == 0 ? 0 : rs.getDouble("col10") / rs.getDouble("col20"));
                jx.setValue(34, 4, rs.getInt("col10") == 0 ? 0 : rs.getDouble("col11") / rs.getDouble("col10"));
//                jx.setValue(35, 4, rs.getInt("col16intax") == 0 ? 0 : rs.getDouble("col19") / rs.getDouble("col16intax"));
//                jx.setValue(36, 4, rs.getInt("col15") == 0 ? 0 : rs.getDouble("col16intax") / rs.getDouble("col15"));
//                jx.setValue(37, 4, (rs.getInt("col2") + rs.getInt("col4") + rs.getInt("col6") + rs.getInt("col8") + rs.getInt("col10")) == 0 ? 0 : (rs.getInt("col3") + rs.getInt("col5") + rs.getInt("col7") + rs.getInt("col9") + rs.getInt("col11")) / (rs.getInt("col2") + rs.getInt("col4") + rs.getInt("col6") + rs.getInt("col8") + rs.getInt("col10")));
//                jx.setValue(38, 4, ((rs.getInt("col2") + rs.getInt("col4") + rs.getInt("col6") + rs.getInt("col8") + rs.getInt("col10")) + rs.getInt("col15")) == 0 ? 0 : ((rs.getInt("col3") + rs.getInt("col5") + rs.getInt("col7") + rs.getInt("col9") + rs.getInt("col11")) + rs.getInt("col16intax")) / ((rs.getInt("col2") + rs.getInt("col4") + rs.getInt("col6") + rs.getInt("col8") + rs.getInt("col10")) + rs.getInt("col15")));
                jx.setValue(35,4, rs.getDouble("col35"));
                jx.setValue(36,4, rs.getDouble("col36"));
                jx.setValue(37,4, rs.getDouble("col37"));
                jx.setValue(38,4, rs.getDouble("col38"));

            }            
            sql = new StringBuffer();
            sql.append("select \n");
            sql.append(" a.staff_id,a.staff_name1,a.staff_name2,a.shop_id\n");
            sql.append(", a.col2 as col2\n");
            sql.append(", a.col3 as col3\n");
            sql.append(",a.col4 as col4\n");
            sql.append(", a.col5 as col5\n");
            sql.append(", a.col6 as col6\n");
            sql.append(",a.col7 as col7\n");
            sql.append(",a.col8 as col8\n");
            sql.append(",a.col9 as col9\n");
            sql.append(",a.col10 as col10\n");
            sql.append(", a.col11 as col11\n");
            sql.append(",a.col12 as col12\n");
            sql.append(", a.col13notax as col13notax \n");
            sql.append(",a.col13intax as col13intax \n");
            sql.append(", a.col14  as col14\n");
            sql.append(", a.col15  as col15\n");
            sql.append(", a.col16notax  as col16notax\n");
            sql.append(", a.col16intax  as col16intax\n");
            sql.append(", a.col19 as col19\n");
            sql.append(",a.col20 as col20 \n");
            sql.append(", a.col21 as col21 \n");
            sql.append(", a.col22 as col22 \n");
            sql.append(",a.col23 as col23 \n");
            sql.append(",a.col24 as col24 \n");
            sql.append(", case when( a.col2 > 0 and a.col20 > 0)  then a.col2/a.col20  else 0 end as col25 \n");
            sql.append(", case when (a.col3 >0 and a.col20 > 0) then a.col3 /a.col20 else 0 end  as col26 \n");
            sql.append(", case when (a.col4 > 0 and a.col20 > 0) then a.col4/a.col20 else 0 end  as col27 \n");
            sql.append(",case when  (a.col5  > 0 and a.col20 > 0) then a.col5 /a.col20 else 0 end  as col28 \n");
            sql.append(", case when  ( a.col6 > 0 and a.col20 > 0) then  a.col6 /a.col20 else 0 end   as col29 \n");
            sql.append(",  case when (a.col7 > 0 and a.col20 > 0) then a.col7/a.col20 else 0 end  as col30 \n");
            sql.append(", case when (a.col8 > 0 and a.col20 > 0) then a.col8/ a.col20 else 0 end  as col31 \n");
            sql.append(",case when  ( a.col9 > 0 and a.col20 > 0) then a.col9/a.col20 else 0 end  as col32 \n");
            sql.append(", case when  ( a.col10 > 0  and a.col20 > 0) then a.col10/ a.col20 else 0 end  as col33 \n");
            sql.append(",  case when  ( col20 > 0 and a.col20 > 0)  then a.col11/col20 else 0 end  as col34\n");
            //sql.append(",  case when  a.col16notax > 0 then a.col16notax/ ( a.col13intax + a.col16intax) else 0 end as col35\n");
            sql.append(",  case when  a.col16notax > 0 then (a.col16notax/ ( a.col13notax + a.col16notax))   else 0 end as col35\n");
            sql.append(",  case when ( a.col16notax > 0 and a.col20 > 0)  then   a.col16notax/a.col20 else 0 end as col36 \n");
            sql.append(",  case when ( a.col13notax > 0 and a.col20 > 0)  then a.col13notax/a.col20 else 0 end as col37 \n");
           // sql.append(", case when  ( ( a.col13intax + a.col16intax   > 0  ) and a.col20 > 0)then ( a.col13intax + a.col16intax ) /a.col20  else 0 end as col38 \n");
             sql.append(", case when  ( ( a.col13notax + a.col16notax   > 0  ) and a.col20 > 0)then ( a.col13notax + a.col16notax ) /a.col20  else 0 end as col38 \n");


            sql.append("from \n");
            sql.append("(	  \n");
            sql.append("    select shop.staff_id,shop.staff_name1,shop.staff_name2, shop.shop_id , shop.staff_no, shop.display_seq  \n");
            sql.append("    ,coalesce(integration1.ÉJÉbÉgÇÃêîó ,0) as col2 \n");
            sql.append("    ,coalesce (integration1.ÉJÉbÉgÇÃã‡äz,0) as col3 \n");
            sql.append("    ,coalesce (integration2.ÉpÅ[É}ÇÃêîó ,0) as col4 \n");
            sql.append("    ,coalesce (integration2.ÉpÅ[É}ÇÃã‡äz,0) as col5 \n");
            sql.append("    ,coalesce (integration3.ÉJÉâÅ[ÇÃêîó ,0) as col6 \n");
            sql.append("    ,coalesce (integration3.ÉJÉâÅ[ÇÃã‡äz,0) as col7 \n");
            sql.append("    ,coalesce (integration4.ÉXÉpÇÃêîó  ,0) as col8 \n");
            sql.append("    ,coalesce (integration4.ÉXÉpÇÃã‡äz,0) as col9 \n");
            sql.append("    ,coalesce (integration5.ƒÿ∞ƒ“›ƒÇÃêîó ,0) as col10 \n");
            sql.append("    ,coalesce (integration5.ƒÿ∞ƒ“›ƒÇÃã‡äz,0) as col11 \n");
            sql.append("    ,coalesce (division13.ãZèpîÑè„ÇÃêîó ,0) as col12 \n");
            sql.append("    ,coalesce (division13.ãZèpîÑè„ÇÃã‡äznotax,0) as col13notax \n");
            sql.append("    ,coalesce (division13.ãZèpîÑè„ÇÃã‡äzintax,0) as col13intax \n");
            sql.append("    ,coalesce (pro13.ãZèpé¿ ,0) as col14  \n");
            sql.append("    ,coalesce (division24.ìXîÃîÑè„ÇÃêîó  ,0) as col15  \n");
            sql.append("    ,coalesce (division24.ìXîÃîÑè„ÇÃã‡äznotax ,0) as col16notax  \n");
            sql.append("    ,coalesce (division24.ìXîÃîÑè„ÇÃã‡äzintax ,0) as col16intax  \n");
            sql.append("    ,coalesce (pro24.ìXîÃé¿intax ,0) as col19 \n");
            sql.append("    ,coalesce (countslip.col20 ,0) as col20 \n");
            sql.append("    ,coalesce (work.HELP_POINT ,0) as col21 \n");
            sql.append("    ,coalesce (countnewcustomer.col22 ,0) as col22 \n");
            sql.append("    ,coalesce (customerdesignated.col23 ,0) as col23 \n");
            sql.append("    ,coalesce (cusapproached.col24,0) as col24  \n");

            sql.append("from \n");
            sql.append("    ( select distinct   \n");
            sql.append("        case when b.detail_staff_id is not null then b.detail_staff_id else -1 end as staff_id \n");
            sql.append("        ,a.staff_name1,a.staff_name2,b.shop_id , a.display_seq , a.staff_no \n");
            sql.append("        from mst_staff a right join view_data_sales_detail_valid b  \n");
            sql.append("        on a.staff_id=b.detail_staff_id  where b.sales_date \n");
            sql.append("    between " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) + "\n");
            sql.append("    and b.shop_id in  (" + paramBean.getShopIDList() + "))shop \n");
            sql.append("    left join (  \n");
            sql.append("                select a.shop_id \n");
            sql.append("                ,case when a.detail_staff_id is not null then a.detail_staff_id else -1 end as staff_id \n");
            sql.append("                ,sum(discount_detail_value_no_tax) as ÉJÉbÉgÇÃã‡äz,count(distinct  a.slip_no) as ÉJÉbÉgÇÃêîó    \n");
            sql.append("                from view_data_sales_detail_valid a \n");
            sql.append("                left join mst_technic b on a.product_id=b.technic_id \n");
            sql.append("                left join mst_technic_class c on b.technic_class_id=c.technic_class_id \n");
            sql.append("                left join mst_staff d on a.detail_staff_id=d.staff_id and a.shop_id = d.shop_id  \n");
            sql.append("                inner join mst_technic_integration e on c.technic_integration_id=e.technic_integration_id \n");
            sql.append("                where e.technic_integration_id=1 \n");
            sql.append("                and a.product_division in ('1','3') \n");
            sql.append("                and a.sales_date between " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) + "\n");
            sql.append("                group by a.detail_staff_id,a.shop_id  \n");
            sql.append("    ) integration1 on shop.shop_id=integration1.shop_id and shop.staff_id=integration1.staff_id \n");

            sql.append(" 	left join (  \n");
            sql.append("                    select a.shop_id \n");
            sql.append("                    ,case when a.detail_staff_id is not null then a.detail_staff_id else -1 end as staff_id \n");
            sql.append("                    ,sum(discount_detail_value_no_tax) as ÉpÅ[É}ÇÃã‡äz,count(distinct  a.slip_no) as ÉpÅ[É}ÇÃêîó    \n");
            sql.append("                    from view_data_sales_detail_valid a \n");
            sql.append("                    left join mst_technic b on a.product_id=b.technic_id \n");
            sql.append("                    left join mst_technic_class c on b.technic_class_id=c.technic_class_id \n");
            sql.append("                    left join mst_staff d on a.detail_staff_id=d.staff_id and a.shop_id = d.shop_id  \n");
            sql.append("                    inner join mst_technic_integration e on c.technic_integration_id=e.technic_integration_id \n");
            sql.append("                    where e.technic_integration_id=2 \n");
            sql.append("                    and a.product_division = 1 \n");
            sql.append("                    and a.sales_date between " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) + "\n");
            sql.append("                    group by a.detail_staff_id , a.shop_id  \n");
            sql.append("                    ) integration2 on shop.shop_id=integration2.shop_id and shop.staff_id = integration2.staff_id  \n");
            sql.append("        left join (  \n");
            sql.append("                    select a.shop_id \n");
            sql.append("                    ,case when a.detail_staff_id is not null then a.detail_staff_id else -1 end as staff_id  \n");
            sql.append("                    ,sum(discount_detail_value_no_tax) as ÉJÉâÅ[ÇÃã‡äz,count(distinct  a.slip_no) as ÉJÉâÅ[ÇÃêîó    \n");
            sql.append("                    from view_data_sales_detail_valid a \n");
            sql.append("                    left join mst_technic b on a.product_id=b.technic_id \n");
            sql.append("                    left join mst_technic_class c on b.technic_class_id=c.technic_class_id \n");
            sql.append("                    left join mst_staff d on a.detail_staff_id=d.staff_id and a.shop_id = d.shop_id  \n");
            sql.append("                    inner join mst_technic_integration e on c.technic_integration_id=e.technic_integration_id \n");
            sql.append("                    where e.technic_integration_id=3 \n");
            sql.append("                    and a.product_division = 1 \n");
            sql.append("                    and a.sales_date between " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) + "\n");
            sql.append("                    group by a.detail_staff_id , a.shop_id  \n");
            sql.append("                ) integration3 on shop.shop_id=integration3.shop_id  and shop.staff_id = integration3.staff_id  \n");
            sql.append("        left join (  \n");
            sql.append("                select a.shop_id \n");
            sql.append("                ,case when a.detail_staff_id is not null then a.detail_staff_id else -1 end as staff_id \n");
            sql.append("                ,sum(discount_detail_value_no_tax) as ÉXÉpÇÃã‡äz ,count(distinct  a.slip_no) as   ÉXÉpÇÃêîó   \n");
            sql.append("                from view_data_sales_detail_valid a \n");
            sql.append("                left join mst_technic b on a.product_id=b.technic_id \n");
            sql.append("                left join mst_technic_class c on b.technic_class_id=c.technic_class_id \n");
            sql.append("                left join mst_staff d on a.detail_staff_id=d.staff_id and a.shop_id = d.shop_id  \n");
            sql.append("                inner join mst_technic_integration e on c.technic_integration_id=e.technic_integration_id \n");
            sql.append("                where e.technic_integration_id=4 \n");
            sql.append("                and a.product_division = 1 \n");
            sql.append("                and a.sales_date between " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) + "\n");
            sql.append("                group by a.detail_staff_id , a.shop_id  \n");
            sql.append("            ) integration4 on shop.shop_id=integration4.shop_id and integration4.staff_id = shop.staff_id \n");
            sql.append("        left join (  \n");
            sql.append("                select a.shop_id \n");
            sql.append("                ,case when a.detail_staff_id is not null then a.detail_staff_id else -1 end as staff_id \n");
            sql.append("                ,sum(discount_detail_value_no_tax) as ƒÿ∞ƒ“›ƒÇÃã‡äz ,count(distinct  a.slip_no) as  ƒÿ∞ƒ“›ƒÇÃêîó   \n");
            sql.append("                from view_data_sales_detail_valid a \n");
            sql.append("                left join mst_technic b on a.product_id=b.technic_id \n");
            sql.append("                left join mst_technic_class c on b.technic_class_id=c.technic_class_id \n");
            sql.append("                left join mst_staff d on a.detail_staff_id=d.staff_id and a.shop_id = d.shop_id  \n");
            sql.append("                inner join mst_technic_integration e on c.technic_integration_id=e.technic_integration_id \n");
            sql.append("                where e.technic_integration_id=5 \n");
            sql.append("                and a.product_division = 1 \n");
            sql.append("                and a.sales_date between " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) + "\n");
            sql.append("                group by a.detail_staff_id , a.shop_id  \n");
            sql.append("            ) integration5 on shop.shop_id=integration5.shop_id and shop.staff_id = integration5.staff_id  \n");
            sql.append("        left join (  \n");
            sql.append("                select a.shop_id \n");
            sql.append("                ,case when a.detail_staff_id is not null then a.detail_staff_id else -1 end as staff_id  \n");
            sql.append("                ,sum(a.discount_detail_value_no_tax) as ãZèpîÑè„ÇÃã‡äznotax \n");
            sql.append("                ,sum(a.discount_detail_value_in_tax) as ãZèpîÑè„ÇÃã‡äzintax \n");
            sql.append("                ,sum(a.product_num) as ãZèpîÑè„ÇÃêîó     \n");
            sql.append("                from view_data_sales_detail_valid a \n");
            sql.append("                left join mst_staff d on a.detail_staff_id=d.staff_id and a.shop_id = d.shop_id  \n");
            sql.append("                where a.product_division in ('1','3')   \n");
            sql.append("                and a.sales_date between " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) + "\n");
            sql.append("                group by a.detail_staff_id , a.shop_id  \n");
            sql.append("            ) division13 on shop.shop_id=division13.shop_id and shop.staff_id = division13.staff_id \n");
            sql.append("        left join (  \n");
            sql.append("                select a.shop_id \n");
            sql.append("                ,a.staff_id  \n");
            sql.append("                ,sum(a.ãZèpé¿) as ãZèpé¿ \n");
            sql.append("                from ( \n");
            sql.append("                        select a.shop_id \n");
            sql.append("                        ,case when b.staff_id is not null then b.staff_id else -1 end as staff_id \n");
            sql.append("                        ,trunc( ( (discount_detail_value_no_tax)  * ( ratio ) ) / 100 ) as ãZèpé¿  \n");
            sql.append("                        from view_data_sales_detail_valid a \n");
            // Thanh start edit 2014/06/12 Bug #24591
            //sql.append("                        left join data_sales_proportionally b on a.slip_no =b.slip_no and a.slip_detail_no =b.slip_detail_no  and a.shop_id = b.shop_id  \n");
            sql.append("                        inner join data_sales_proportionally b on a.slip_no =b.slip_no and a.slip_detail_no =b.slip_detail_no  and a.shop_id = b.shop_id and a.staff_id = b.staff_id  \n");
             // Thanh end edit 2014/06/12
            sql.append("                        INNER JOIN data_proportionally f  \n");
            sql.append("                        ON f.data_proportionally_id = b.data_proportionally_id  \n");
            sql.append("                        where a.product_division in('1','3') 	 \n");
            sql.append("                        and a.sales_date between " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) + "\n");
            sql.append("                        and ratio != 0  \n");
            sql.append("                        and b.staff_id  != 0 and  b.delete_date is null  \n");
            // Thanh start add 2014/06/12 Bug #24591
            sql.append("                        and b.staff_id  is not null \n");
            // Thanh end add 2014/06/12 Bug #24591
            sql.append("                ) a group by a.staff_id , a.shop_id  		 \n");
            sql.append("            ) pro13 on shop.shop_id=pro13.shop_id  and shop.staff_id = pro13.staff_id  \n");
            sql.append("        left join (  \n");
            sql.append("                select a.shop_id \n");
            sql.append("                ,case when a.detail_staff_id is not null then a.detail_staff_id else -1 end as staff_id \n");
            sql.append("                ,sum(a.discount_detail_value_no_tax) as ìXîÃîÑè„ÇÃã‡äznotax \n");
            sql.append("                ,sum(a.discount_detail_value_in_tax) as ìXîÃîÑè„ÇÃã‡äzintax \n");
            // Thanh start edit 2014/06/02 Bug #24591
            //sql.append("                ,sum(a.product_num) as ìXîÃîÑè„ÇÃêîó     \n");
            sql.append("                ,count(distinct a.slip_no) as ìXîÃîÑè„ÇÃêîó     \n");
            // Thanh start edit 2014/06/02
            sql.append("                from view_data_sales_detail_valid a \n");
            sql.append("                INNER JOIN mst_item mi		\n");
            sql.append("                ON a.product_id = mi.item_id		 \n");
            sql.append("                left join mst_staff d on a.detail_staff_id=d.staff_id and a.shop_id = d.shop_id  \n");
            sql.append("                where a.product_division in ('2','4') \n");
            sql.append("                and   mi.item_use_division IN ( '1', '3') \n");
            sql.append("                and a.sales_date between " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) + "\n");
            sql.append("                group by a.detail_staff_id, a.shop_id  \n");
            sql.append("            ) division24 on shop.shop_id=division24.shop_id \n");
            sql.append("                        and shop.staff_id = division24.staff_id  \n");
            sql.append("        left join (  \n");
            sql.append("            select a.shop_id \n");
            sql.append("            ,case when a.detail_staff_id is not null then a.detail_staff_id else -1 end as staff_id \n");
            sql.append("            ,sum(a.discount_detail_value_no_tax) as ìXîÃé¿notax \n");
            sql.append("            ,sum(a.discount_detail_value_in_tax) as ìXîÃé¿intax \n");
            sql.append("            from view_data_sales_detail_valid a \n");
            // Thanh start edit 2014/06/02 Bug #24591
            //sql.append("            inner JOIN data_sales_proportionally b 				 \n");
            //sql.append("            ON  a.shop_id        = b.shop_id				 \n");
            //sql.append("            AND a.slip_no        = b.slip_no				 \n");
            //sql.append("            AND a.slip_detail_no = b.slip_detail_no				 \n");
            //sql.append("            AND a.staff_id = b.staff_id				 \n");            
            sql.append("            INNER JOIN mst_item mi		 \n");
            sql.append("            ON a.product_id = mi.item_id		 \n");
            sql.append("            left join mst_staff d on a.detail_staff_id=d.staff_id and a.shop_id = d.shop_id \n");
            sql.append("            where a.product_division in ('2')\n");
            sql.append("            and   mi.item_use_division IN ( '1', '3') \n");
            sql.append("            and detail_staff_id is not null and a.detail_staff_id = a.staff_id \n");
            // Thanh end edit 2014/06/02 Bug #24591
            sql.append("            and a.sales_date between " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) + "\n");
            sql.append("            group by a.detail_staff_id, a.shop_id  \n");
            sql.append("               ) pro24 on shop.shop_id=pro24.shop_id and shop.staff_id = pro24.staff_id  \n");
            sql.append("        left join ( \n");
            sql.append("                SELECT a.shop_id\n");
            sql.append(",               count( distinct cast(a.shop_id as text) || cast(a.slip_no as text)   ) 	as col20\n");
            sql.append("                ,case when a.staff_id is not null then a.staff_id else -1 end as staff_id\n");
            sql.append("                FROM 	view_data_sales_detail_valid a	 \n");
            sql.append("                LEFT JOIN mst_staff c	ON  a.staff_id = c.staff_id	\n");
            sql.append("                WHERE 	a.sales_date between " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) + "\n");
            sql.append("                group by a.staff_id , a.shop_id \n");
            sql.append("                ) countslip  on shop.shop_id = countslip.shop_id and shop.staff_id = countslip.staff_id \n");
            sql.append("LEFT JOIN (  \n");
            sql.append("SELECT	\n");
            sql.append("WORK1.S_staff_id as staff_id ,	\n");
            sql.append("WORK1.shop_id,	\n");
            sql.append("D.staff_name1,	\n");
            sql.append("D.staff_name2,	\n");
            sql.append("ROUND( AVG( ( WORK1.point / WORK2.point) * 100 ) )/100   AS HELP_POINT	\n");
            sql.append("FROM	\n");
            sql.append("( SELECT	\n");
            sql.append("C.shop_id,	\n");
            sql.append("C.slip_no,	\n");
            sql.append("C.slip_detail_no,	 \n");
            sql.append("A.staff_id AS S_staff_id,	\n");
            sql.append("SUM( C.point ) AS point	\n");
            sql.append("FROM	\n");
            sql.append("data_sales A	\n");
            sql.append("INNER JOIN	\n");
            sql.append("data_sales_detail B	\n");
            sql.append("ON  A.shop_id        = B.shop_id\n");
            sql.append("AND A.slip_no        = B.slip_no\n");
            sql.append("INNER JOIN	\n");
            sql.append("data_sales_proportionally C \n");
            sql.append("ON  B.shop_id        = C.shop_id\n");
            sql.append("AND B.slip_no        = C.slip_no\n");
            sql.append("AND B.slip_detail_no = C.slip_detail_no	\n");
            sql.append("AND B.product_division  IN ( 1, 3 )	\n");
            sql.append("WHERE	\n");
            sql.append("     a.sales_date between  " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) +" \n");
            sql.append("AND A.staff_id       != C.staff_id	\n");
            sql.append("AND A.delete_date is null	\n");
            sql.append("AND B.delete_date is null	\n");
            sql.append("GROUP BY	\n");
            sql.append("C.shop_id,	\n");
            sql.append("C.slip_no,	\n");
            sql.append("C.slip_detail_no,	\n");
            sql.append("A.staff_id,	\n");
            sql.append("C.staff_id ) WORK1	\n");
            sql.append("INNER JOIN	\n");
            sql.append("(  SELECT	\n");
            sql.append("C.shop_id,	\n");
            sql.append("C.slip_no,	\n");
            sql.append("C.slip_detail_no,	\n");
            sql.append("A.staff_id AS S_staff_id,	\n");
            sql.append("SUM( C.point ) AS point	\n");
            sql.append("FROM	\n");
            sql.append("data_sales A	\n");
            sql.append("INNER JOIN	\n");
            sql.append("data_sales_detail B	\n");
            sql.append("ON  A.shop_id        = B.shop_id	\n");
            sql.append("AND A.slip_no        = B.slip_no	\n");
            sql.append("INNER JOIN	\n");
            sql.append("data_sales_proportionally C 	\n");
            sql.append("ON  B.shop_id        = C.shop_id	\n");
            sql.append("AND B.slip_no        = C.slip_no	\n");
            sql.append("AND B.slip_detail_no = C.slip_detail_no	\n");
            sql.append("AND B.product_division  IN ( 1, 3 )	\n");
            sql.append("WHERE	\n");
            sql.append("     a.sales_date between  " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) +" \n");
            sql.append("AND A.delete_date is null	\n");
            sql.append("AND B.delete_date is null	\n");
            sql.append("    AND C.delete_date is null	\n");
            sql.append("GROUP BY	\n");
            sql.append("C.shop_id,	\n");
            sql.append("C.slip_no,	\n");
            sql.append("C.slip_detail_no,	\n");
            sql.append("A.staff_id,	\n");
            sql.append(" C.staff_id ) WORK2	\n");
            sql.append("ON	\n");
            sql.append("WORK1.shop_id        =  WORK2.shop_id	\n");
            sql.append("AND WORK1.slip_no        =  WORK2.slip_no	\n");
            sql.append("AND WORK1.slip_detail_no =  WORK2.slip_detail_no	\n");
            sql.append("AND WORK1.point          != 0	\n");
            sql.append("AND WORK2.point          != 0	\n");
            sql.append("INNER JOIN mst_staff D	\n");
            sql.append("ON  work1.S_staff_id = d.staff_id 	\n");
            sql.append("GROUP BY	\n");
            sql.append("WORK1.S_staff_id,	\n");
            sql.append("WORK1.shop_id,	\n");
            sql.append("D.staff_name1,	\n");
            sql.append("D.staff_name2	\n");

            sql.append("            ) work on work.shop_id = shop.shop_id and  work.staff_id = shop.staff_id \n");  
            
            sql.append("        LEFT JOIN( \n");
            sql.append("                SELECT a.shop_id, \n");
            sql.append("                a.staff_id , \n");
            sql.append("                sum(a.êVãK) AS col22 \n");
            sql.append("                FROM \n");
            sql.append("                    ( SELECT  a.shop_id \n");
            sql.append("                    ,case when a.staff_id is not null then a.staff_id else -1 end as staff_id \n");
            sql.append("                    ,count(DISTINCT a.customer_id)AS êVãK \n");
            sql.append("                    FROM view_data_sales_detail_valid a \n");
            sql.append("                    LEFT JOIN mst_staff d ON a.staff_id=d.staff_id \n");
            sql.append("                    AND a.shop_id = d.shop_id \n");
            sql.append("                    WHERE a.visit_num=1 \n");
            sql.append("                    AND a.sales_date between " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) + "\n");
            sql.append("                    GROUP BY a.staff_id ,a.shop_id) a \n");
            sql.append("        GROUP BY a.staff_id ,  a.shop_id  \n");
            sql.append("            ) countnewcustomer ON shop.shop_id=countnewcustomer.shop_id and shop.staff_id = countnewcustomer.staff_id \n");
            sql.append("        LEFT JOIN \n");
            sql.append("            ( SELECT a.shop_id \n");
            sql.append("            ,a.staff_id  \n");
            sql.append("            ,sum(a.éwêV) AS col23 \n");
            sql.append("            FROM \n");
            sql.append("                ( SELECT a.shop_id \n");
            sql.append("                ,case when a.staff_id is not null then a.staff_id else -1 end as staff_id \n");
            sql.append("                ,count(DISTINCT a.slip_no)AS éwêV \n");
            sql.append("                FROM view_data_sales_detail_valid a \n");
            sql.append("                LEFT JOIN mst_staff d ON a.staff_id=d.staff_id \n");
            sql.append("                AND a.shop_id = d.shop_id \n");
            sql.append("                WHERE  \n");
            sql.append("                a.sales_date between " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) + "\n");
            sql.append("                AND product_division IN (1, 3) AND designated_flag=TRUE \n");
            sql.append("                GROUP BY a.shop_id, \n");
            sql.append("            a.staff_id ) a \n");
            sql.append("            GROUP BY a.shop_id, a.staff_id   \n");
            sql.append("      ) customerdesignated ON shop.shop_id=customerdesignated.shop_id and customerdesignated.staff_id = shop.staff_id  \n");
            
            // Thanh start edit 2014/06/02 Bug #2459
            sql.append("        LEFT JOIN \n");
            sql.append("            ( SELECT a.shop_id \n");
            sql.append("            ,a.staff_id   \n");
            sql.append("            ,sum(a.è–êV) AS col24 \n");
            sql.append("            FROM \n");
            sql.append("            ( SELECT a.shop_id \n");
            sql.append("            , case when a.staff_id is not null then a.staff_id else -1 end as staff_id \n");
            sql.append("            ,coalesce(count(mc.introducer_id),0)AS è–êV \n");
            sql.append("            FROM data_sales a \n");            
            sql.append("            LEFT JOIN mst_staff d ON a.staff_id=d.staff_id \n");
            sql.append("            INNER JOIN mst_customer mc on mc.customer_id = a.customer_id \n");
            sql.append("            AND a.shop_id = d.shop_id \n");
            sql.append("            WHERE a.visit_num=1 and mc.introducer_id is not null  \n");
            sql.append("            AND a.sales_date between " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) + "\n");
            // Thanh end edit 2014/06/02 Bug #2459
            //sql.append("            AND a.approached_flag=TRUE \n");
            sql.append("            GROUP BY a.shop_id, \n");
            sql.append("        a.staff_id  ) a \n");
            sql.append("        GROUP BY a.shop_id, a.staff_id   \n");
            sql.append("        ) cusapproached ON shop.shop_id=cusapproached.shop_id and shop.staff_id=cusapproached.staff_id \n");
            sql.append(")a \n");
            // vtbphuong start add 20140618 Request #25433
             // sql.append("order by a.staff_id  \n");
            sql.append(" order by");
            sql.append("      a.shop_id");
            sql.append("     ,a.display_seq");
            sql.append("     ,lpad(a.staff_no, 10, '0')");
            sql.append("     ,a.staff_id");
            // vtbphuong end add 20140618 Request #25433
  
  
            rs = con.executeQuery(sql.toString());
            i = 0;
            boolean isStaffNull = false;
            while (rs.next()) {
                if (i == 0) {
//                    if (rs.getInt("staff_id") == -1) {
//                        isStaffNull = true;
//                        continue;
//                    }
                    jx.setValue(1, 7, rs.getString("staff_name1") + rs.getString("staff_name2"));
                    jx.setValue(2, 7, rs.getInt("col2"));
                    jx.setValue(3, 7, rs.getInt("col3"));
                    jx.setValue(4, 7, rs.getInt("col4"));
                    jx.setValue(5, 7, rs.getInt("col5"));
                    jx.setValue(6, 7, rs.getInt("col6"));
                    jx.setValue(7, 7, rs.getInt("col7"));
                    jx.setValue(8, 7, rs.getInt("col8"));
                    jx.setValue(9, 7, rs.getInt("col9"));
                    jx.setValue(10, 7, rs.getInt("col10"));
                    jx.setValue(11, 7, rs.getInt("col11"));
                    jx.setValue(12, 7, rs.getInt("col2") + rs.getInt("col4") + rs.getInt("col6") + rs.getInt("col8") + rs.getInt("col10"));
                    jx.setValue(13, 7, rs.getInt("col13notax"));
                    jx.setValue(14, 7, rs.getDouble("col14"));
                    jx.setValue(15, 7, rs.getInt("col15"));
                    jx.setValue(16, 7, rs.getInt("col16notax"));
                    jx.setValue(17, 7, rs.getInt("col13intax") + rs.getInt("col16intax"));
                    jx.setValue(18, 7, rs.getInt("col13notax") + rs.getInt("col16notax"));
                    jx.setValue(19, 7, rs.getInt("col19"));
                    jx.setValue(20, 7, rs.getInt("col20"));
                    jx.setValue(21, 7, rs.getInt("col13notax") == 0 ? 0 :  (rs.getInt("col13notax") - rs.getDouble("col14")) / rs.getInt("col13notax"));
                    jx.setValue(22, 7, rs.getInt("col22"));
                    jx.setValue(23, 7, rs.getInt("col23"));
                    jx.setValue(24, 7, rs.getInt("col24"));
                    jx.setValue(25, 7, rs.getInt("col20") == 0 ? 0 :  rs.getDouble("col2") / rs.getDouble("col20"));
                    jx.setValue(26, 7,rs.getInt("col2") == 0 ? 0 :  rs.getDouble("col3") / rs.getDouble("col2"));                    
                    jx.setValue(27, 7, rs.getInt("col20") == 0 ? 0 :  rs.getDouble("col4") / rs.getDouble("col20"));
                    jx.setValue(28, 7, rs.getInt("col4") == 0 ? 0 :  rs.getDouble("col5") / rs.getDouble("col4"));                    
                    jx.setValue(29, 7,  rs.getInt("col20") == 0 ? 0 :  rs.getDouble("col6") / rs.getDouble("col20"));
                    jx.setValue(30, 7, rs.getInt("col6") == 0 ? 0 :  rs.getDouble("col7") / rs.getDouble("col6"));                    
                    jx.setValue(31, 7, rs.getInt("col20") == 0 ? 0 :  rs.getDouble("col8") / rs.getDouble("col20"));
                    jx.setValue(32, 7, rs.getInt("col8") == 0 ? 0 :  rs.getDouble("col9") / rs.getDouble("col8"));                    
                    jx.setValue(33, 7, rs.getInt("col20") == 0 ? 0 :  rs.getDouble("col10") / rs.getDouble("col20"));
                    jx.setValue(34, 7, rs.getInt("col10") == 0 ? 0 :  rs.getDouble("col11") / rs.getDouble("col10"));                    
//                    jx.setValue(35, 7, rs.getInt("col16intax") == 0 ? 0 :  rs.getDouble("col19") / rs.getDouble("col16intax"));
//                    jx.setValue(36, 7, rs.getInt("col15") == 0 ? 0 :  rs.getDouble("col16intax") / rs.getDouble("col15"));
//                    jx.setValue(37, 7,(rs.getInt("col2") + rs.getInt("col4") + rs.getInt("col6") + rs.getInt("col8") + rs.getInt("col10")) ==0 ? 0 : (rs.getInt("col3") + rs.getInt("col5") + rs.getInt("col7") + rs.getInt("col9") + rs.getInt("col11")) / (rs.getInt("col2") + rs.getInt("col4") + rs.getInt("col6") + rs.getInt("col8") + rs.getInt("col10")));
//                    jx.setValue(38, 7,((rs.getInt("col2") + rs.getInt("col4") + rs.getInt("col6") + rs.getInt("col8") + rs.getInt("col10")) + rs.getInt("col15"))==0 ? 0 : ((rs.getInt("col3") + rs.getInt("col5") + rs.getInt("col7") + rs.getInt("col9") + rs.getInt("col11") )+ rs.getInt("col16intax")) / ((rs.getInt("col2") + rs.getInt("col4") + rs.getInt("col6") + rs.getInt("col8") + rs.getInt("col10")) + rs.getInt("col15")));
                    jx.setValue(35, 7 , rs.getDouble("col35"));
                    jx.setValue(36, 7 , rs.getDouble("col36"));
                    jx.setValue(37, 7 , rs.getDouble("col37"));
                    jx.setValue(38, 7 , rs.getDouble("col38"));
                    
                } else {
                    if (rs.getInt("staff_id") == -1) {
                        isStaffNull = true;
                      //  continue;
                    }
                    jx.insertRow(7 + i - 1, 1);
                    rowCount++;
                    if(isStaffNull){
                        jx.setValue(1, 7 + i, "ÇªÇÃëº");   
                    }else{
                        jx.setValue(1, 7 + i, rs.getString("staff_name1") + rs.getString("staff_name2"));    
                    }           
                    jx.setValue(2, 7 + i, rs.getInt("col2"));
                    jx.setValue(3, 7 + i, rs.getInt("col3"));
                    jx.setValue(4, 7 + i, rs.getInt("col4"));
                    jx.setValue(5, 7 + i, rs.getInt("col5"));
                    jx.setValue(6, 7 + i, rs.getInt("col6"));
                    jx.setValue(7, 7 + i, rs.getInt("col7"));
                    jx.setValue(8, 7 + i, rs.getInt("col8"));
                    jx.setValue(9, 7 + i, rs.getInt("col9"));
                    jx.setValue(10, 7 + i, rs.getInt("col10"));
                    jx.setValue(11, 7 + i, rs.getInt("col11"));
                    jx.setValue(12, 7 + i, rs.getInt("col2") + rs.getInt("col4") + rs.getInt("col6") + rs.getInt("col8") + rs.getInt("col10"));
                    //jx.setValue(13, 7 + i, rs.getInt("col3") + rs.getInt("col5") + rs.getInt("col7") + rs.getInt("col9") + rs.getInt("col11") );
                    // Thanh start edit 2014/06/02 Bug #24591
                    jx.setValue(13, 7 + i, rs.getInt("col13notax"));
                    // Thanh end edit 2014/06/02
                    jx.setValue(14, 7 + i, rs.getDouble("col14"));
                    jx.setValue(15, 7 + i, rs.getInt("col15"));
                    jx.setValue(16, 7 + i, rs.getInt("col16notax"));
                    jx.setValue(17, 7 + i, rs.getInt("col13intax") + rs.getInt("col16intax"));
                    jx.setValue(18, 7 + i,rs.getInt("col13notax") + rs.getInt("col16notax"));
                    jx.setValue(19, 7 + i, rs.getInt("col19"));
                    //jx.setValue(20, 7 + i, (rs.getInt("col2") + rs.getInt("col4") + rs.getInt("col6") + rs.getInt("col8") + rs.getInt("col10")) + rs.getInt("col15"));
                    jx.setValue(20, 7 + i, rs.getInt("col20"));
                    //jx.setValue(21, 7 + i, rs.getDouble("col21"));
                    jx.setValue(21, 7 + i, rs.getInt("col13notax") == 0 ? 0 :  (rs.getInt("col13notax") - rs.getDouble("col14")) / rs.getInt("col13notax"));
                    jx.setValue(22, 7 + i, rs.getInt("col22"));
                    jx.setValue(23, 7 + i, rs.getInt("col23"));
                    jx.setValue(24, 7 + i, rs.getInt("col24"));
                    //jx.setValue(25, 7 + i, rs.getDouble("col25"));
                    jx.setValue(25, 7 + i, rs.getInt("col20") == 0 ? 0 :  rs.getDouble("col2") / rs.getDouble("col20"));
                    jx.setValue(26, 7 + i,rs.getInt("col2") == 0 ? 0 :  rs.getDouble("col3") / rs.getDouble("col2"));                    
                    jx.setValue(27, 7 + i, rs.getInt("col20") == 0 ? 0 :  rs.getDouble("col4") / rs.getDouble("col20"));
                    jx.setValue(28, 7 + i, rs.getInt("col4") == 0 ? 0 :  rs.getDouble("col5") / rs.getDouble("col4"));                    
                    jx.setValue(29, 7 + i,  rs.getInt("col20") == 0 ? 0 :  rs.getDouble("col6") / rs.getDouble("col20"));
                    jx.setValue(30, 7 + i, rs.getInt("col6") == 0 ? 0 :  rs.getDouble("col7") / rs.getDouble("col6"));                    
                    jx.setValue(31, 7 + i, rs.getInt("col20") == 0 ? 0 :  rs.getDouble("col8") / rs.getDouble("col20"));
                    jx.setValue(32, 7 + i, rs.getInt("col8") == 0 ? 0 :  rs.getDouble("col9") / rs.getDouble("col8"));                    
                    jx.setValue(33, 7 + i, rs.getInt("col20") == 0 ? 0 :  rs.getDouble("col10") / rs.getDouble("col20"));
                    jx.setValue(34, 7 + i, rs.getInt("col10") == 0 ? 0 :  rs.getDouble("col11") / rs.getDouble("col10"));                    
//                    jx.setValue(35, 7 + i, rs.getInt("col16intax") == 0 ? 0 :  rs.getDouble("col19") / rs.getDouble("col16intax"));
//                    jx.setValue(36, 7 + i, rs.getInt("col15") == 0 ? 0 :  rs.getDouble("col16intax") / rs.getDouble("col15"));
//                    jx.setValue(37, 7 + i,(rs.getInt("col2") + rs.getInt("col4") + rs.getInt("col6") + rs.getInt("col8") + rs.getInt("col10")) ==0 ? 0 : (rs.getInt("col3") + rs.getInt("col5") + rs.getInt("col7") + rs.getInt("col9") + rs.getInt("col11")) / (rs.getInt("col2") + rs.getInt("col4") + rs.getInt("col6") + rs.getInt("col8") + rs.getInt("col10")));
//                    jx.setValue(38, 7 + i,((rs.getInt("col2") + rs.getInt("col4") + rs.getInt("col6") + rs.getInt("col8") + rs.getInt("col10")) + rs.getInt("col15"))==0 ? 0 : ((rs.getInt("col3") + rs.getInt("col5") + rs.getInt("col7") + rs.getInt("col9") + rs.getInt("col11") )+ rs.getInt("col16intax")) / ((rs.getInt("col2") + rs.getInt("col4") + rs.getInt("col6") + rs.getInt("col8") + rs.getInt("col10")) + rs.getInt("col15")));
                     jx.setValue(35, 7 + i, rs.getDouble("col35"));
                     jx.setValue(36, 7 + i, rs.getDouble("col36"));
                     jx.setValue(37, 7 + i, rs.getDouble("col37"));
                     jx.setValue(38, 7 + i, rs.getDouble("col38"));
                }
                i++;
            }
//            if(isStaffNull)
//            {
//                rs.first();
//                if(rs.getInt("staff_id") == -1)
//                {
//                    jx.insertRow(7 + i - 1, 1);
//                    rowCount++;
//                    jx.setValue(1, 7 + i, "ÇªÇÃëº");
//                    jx.setValue(2, 7 + i, rs.getInt("col2"));
//                    jx.setValue(3, 7 + i, rs.getInt("col3"));
//                    jx.setValue(4, 7 + i, rs.getInt("col4"));
//                    jx.setValue(5, 7 + i, rs.getInt("col5"));
//                    jx.setValue(6, 7 + i, rs.getInt("col6"));
//                    jx.setValue(7, 7 + i, rs.getInt("col7"));
//                    jx.setValue(8, 7 + i, rs.getInt("col8"));
//                    jx.setValue(9, 7 + i, rs.getInt("col9"));
//                    jx.setValue(10, 7 + i, rs.getInt("col10"));
//                    jx.setValue(11, 7 + i, rs.getInt("col11"));
//                    jx.setValue(12, 7 + i, rs.getInt("col2") + rs.getInt("col4") + rs.getInt("col6") + rs.getInt("col8") + rs.getInt("col10"));                  
//                    //jx.setValue(13, 7 + i, rs.getInt("col3") + rs.getInt("col5") + rs.getInt("col7") + rs.getInt("col9") + rs.getInt("col11") );
//                    // Thanh start edit 2014/06/02 Bug #24591
//                    jx.setValue(13, 7 + i, rs.getInt("col13notax"));
//                    // Thanh end edit 2014/06/02
//                    jx.setValue(14, 7 + i, rs.getDouble("col14"));
//                    jx.setValue(15, 7 + i, rs.getInt("col15"));
//                    jx.setValue(16, 7 + i, rs.getInt("col16notax"));
////                    jx.setValue(17, 7 + i, rs.getInt("col13intax") + rs.getInt("col16intax"));
//                    jx.setValue(17, 7 + i, rs.getInt("col13intax") + rs.getInt("col16intax"));
//                    jx.setValue(18, 7 + i,rs.getInt("col13notax") + rs.getInt("col16notax"));
//                    jx.setValue(19, 7 + i, rs.getInt("col19"));
//                    jx.setValue(20, 7 + i, rs.getInt("col20"));
//                    //jx.setValue(21, 7 + i, rs.getDouble("col21"));
//                    jx.setValue(21, 7 + i, rs.getInt("col13notax") == 0 ? 0 :  (rs.getInt("col13notax") - rs.getDouble("col14")) / rs.getInt("col13notax"));
//                    jx.setValue(22, 7 + i, rs.getInt("col22"));
//                    jx.setValue(23, 7 + i, rs.getInt("col23"));
//                    jx.setValue(24, 7 + i, rs.getInt("col24"));
//                    //jx.setValue(25, 7 + i, rs.getDouble("col25"));
//                    jx.setValue(25, 7 + i, rs.getInt("col20") == 0 ? 0 :  rs.getInt("col2") / rs.getInt("col20"));
//                    jx.setValue(26, 7 + i,rs.getInt("col2") == 0 ? 0 :  rs.getInt("col3") / rs.getInt("col2"));
//                    //jx.setValue(27, 7 + i, rs.getDouble("col27"));
//                    jx.setValue(27, 7 + i, rs.getInt("col20") == 0 ? 0 :  rs.getInt("col4") / rs.getInt("col20"));
//                    jx.setValue(28, 7 + i, rs.getInt("col4") == 0 ? 0 :  rs.getInt("col5") / rs.getInt("col4"));
//                    //jx.setValue(29, 7 + i, rs.getDouble("col29"));
//                    jx.setValue(29, 7 + i,  rs.getInt("col20") == 0 ? 0 :  rs.getInt("col6") / rs.getInt("col20"));
//                    jx.setValue(30, 7 + i, rs.getInt("col6") == 0 ? 0 :  rs.getInt("col7") / rs.getInt("col6"));
//                    //jx.setValue(31, 7 + i, rs.getDouble("col31"));
//                    jx.setValue(31, 7 + i, rs.getInt("col20") == 0 ? 0 :  rs.getInt("col8") / rs.getInt("col20"));
//                    jx.setValue(32, 7 + i, rs.getInt("col8") == 0 ? 0 :  rs.getInt("col9") / rs.getInt("col8"));
//                    //jx.setValue(33, 7 + i, rs.getDouble("col33"));
//                    jx.setValue(33, 7 + i, rs.getInt("col20") == 0 ? 0 :  rs.getInt("col10") / rs.getInt("col20"));
//                    jx.setValue(34, 7 + i, rs.getInt("col10") == 0 ? 0 :  rs.getInt("col11") / rs.getInt("col10"));
////                    jx.setValue(35, 7 + i, rs.getInt("col16intax") == 0 ? 0 :  rs.getInt("col19") / rs.getInt("col16intax"));
////                    jx.setValue(36, 7 + i, rs.getInt("col15") == 0 ? 0 :  rs.getInt("col16intax") / rs.getInt("col15"));
////                    jx.setValue(37, 7 + i,(rs.getInt("col2") + rs.getInt("col4") + rs.getInt("col6") + rs.getInt("col8") + rs.getInt("col10")) ==0 ? 0 : (rs.getInt("col3") + rs.getInt("col5") + rs.getInt("col7") + rs.getInt("col9") + rs.getInt("col11")) / (rs.getInt("col2") + rs.getInt("col4") + rs.getInt("col6") + rs.getInt("col8") + rs.getInt("col10")));
////                    jx.setValue(38, 7 + i,((rs.getInt("col2") + rs.getInt("col4") + rs.getInt("col6") + rs.getInt("col8") + rs.getInt("col10")) + rs.getInt("col15"))==0 ? 0 : ((rs.getInt("col3") + rs.getInt("col5") + rs.getInt("col7") + rs.getInt("col9") + rs.getInt("col11") )+ rs.getInt("col16intax")) / ((rs.getInt("col2") + rs.getInt("col4") + rs.getInt("col6") + rs.getInt("col8") + rs.getInt("col10")) + rs.getInt("col15")));
//                     jx.setValue(35, 7 + i, rs.getDouble("col35"));
//                     jx.setValue(36, 7 + i, rs.getDouble("col36"));
//                     jx.setValue(37, 7 + i, rs.getDouble("col37"));
//                     jx.setValue(38, 7 + i, rs.getDouble("col38"));
//                    
//                }
//            }
            sql = new StringBuffer();
            sql.append("SELECT ms.* \n");
            sql.append("    ,coalesce (detail.ìXîÃé¿,0) as ìXîÃé¿ \n");
            sql.append("     FROM  \n");
            sql.append("        ( \n");
            sql.append("        SELECT \n");
            sql.append("        a.shop_id ,  	 \n");
            sql.append("        e.staff_id,	 \n");
            sql.append("        e.staff_name1,	\n");
            sql.append("        e.staff_name2 ,	e.staff_no, e.display_seq	 \n");
            sql.append("        FROM 	 \n");
            sql.append("        view_data_sales_detail_valid a 	 \n");
            //sql.append("        INNER JOIN data_sales_proportionally c	 \n");
            //sql.append("        ON  a.shop_id        = c.shop_id	 \n");
            //sql.append("        AND a.slip_no        = c.slip_no	 \n");
            //sql.append("        AND a.slip_detail_no = c.slip_detail_no 	\n");
            sql.append("        INNER JOIN mst_staff e	 \n");
            sql.append("        ON  a.detail_staff_id = e.staff_id 	 \n");
            sql.append("        WHERE	\n");
            sql.append("        a.sales_date between " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) + "\n");
            sql.append("        AND a.shop_id in(" + paramBean.getShopIDList() + ")\n"); 
            sql.append("    UNION	\n");
            sql.append("        SELECT  \n");
            sql.append("        a.shop_id, \n");
            sql.append("        b.staff_id  ,	\n");
            sql.append("        e.staff_name1,	\n");
            sql.append("        e.staff_name2 ,	e.staff_no, e.display_seq \n");
            sql.append("        FROM 	\n");
            sql.append("        data_sales  a 	\n");
            sql.append("        INNER JOIN data_sales_detail  b on a.shop_id = b.shop_id and a.slip_no = b.slip_no  	\n");
            sql.append("        INNER JOIN mst_staff e	 \n");
            sql.append("        ON  b.staff_id = e.staff_id 	 \n");
            sql.append("        WHERE	\n");
            sql.append("        a.sales_date between " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) + "\n");
            sql.append("        AND a.shop_id in(" + paramBean.getShopIDList() + ")\n"); 
            sql.append("        ) ms \n");
            sql.append("LEFT JOIN  \n");
            sql.append("    ( \n");
            // Thanh start edit 2014/06/02
            sql.append("        SELECT	\n");
            sql.append("        a.shop_id , \n");
            sql.append("        a.detail_staff_id as staff_id, -- éÂíSìñID	 \n");
            sql.append("        E.staff_name1,	 \n");
            sql.append("        E.staff_name2,	 \n");
            sql.append("        sum(a.product_value - a.discount_value ) as ìXîÃé¿	 \n");
            sql.append("        FROM	 \n");
            sql.append("        view_data_sales_detail_valid a 	 \n");                      
            sql.append("        INNER JOIN	 \n");
            sql.append("        mst_item D	 \n");
            sql.append("        ON  a.product_id     = D.item_id	 \n");
            sql.append("        INNER JOIN mst_staff E	 \n");
            sql.append("        ON  a.detail_staff_id = E.staff_id 	 \n");
            sql.append("    WHERE	 \n");
            sql.append("        a.product_division  IN ( 2 )	\n");
            //sql.append("        and drd.staff_id = a.detail_staff_id	\n");
            sql.append("        AND D.item_use_division IN ( 1, 3 )	 \n");
            sql.append("        AND A.sales_date between " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " and " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) + "\n");
            sql.append("        AND a.shop_id in(" + paramBean.getShopIDList() + ")\n"); 
            sql.append("    group by \n");
            sql.append("    a.shop_id, \n");
            sql.append("    a.detail_staff_id,	 \n");
            sql.append("    E.staff_name1,	\n");
            sql.append("    E.staff_name2 \n");
            // Thanh end edit 2014/06/02
            sql.append("    ) detail ON ms.shop_id = detail.shop_id and ms.staff_id = detail.staff_id \n");
            sql.append("    order by shop_id, display_seq,lpad(staff_no, 10, '0'),staff_id \n ");
            
            rs = con.executeQuery(sql.toString());
            i = 0;
            while (rs.next()) {
                int staffId = rs.getInt("staff_id");
                if (i == 0) {
                    jx.setValue(1, 10 + rowCount, rs.getString("staff_name1") + rs.getString("staff_name2"));
                    jx.setValue(19, 10 + rowCount, rs.getInt("ìXîÃé¿"));
                    sql = new StringBuffer();
                    // Thanh start edit 2014/06/02
                    sql.append(" SELECT data.staff_id, coalesce(sum(data.value),0) AS ãZèpóøã‡, coalesce(sum(data.point),0)  AS Œﬂ≤›ƒçáåv\n");
                    sql.append(" FROM ( \n");
                    sql.append("  SELECT a.proportionally_integration_name,a.display_seq,b.*\n");
                    sql.append(" FROM\n");
                    sql.append(" mst_proportionally_integration a\n");
                    sql.append(" left outer join \n");
                    sql.append(" (");
                    //IVS_LVTu start edit 2015/11/27 Bug #44795
                    sql.append("       SELECT  proportionally_integration_id,dsd.slip_no , ms.staff_id, \n");
                    sql.append("        sum(floor( discount_detail_value_no_tax* dsp.ratio * 0.01) ) AS value,\n");
                    sql.append("        sum(dsp.point) AS point  \n");
                    sql.append("        FROM data_sales_proportionally AS dsp  \n");
                    sql.append("        INNER JOIN view_data_sales_detail_valid AS dsd ON dsd.shop_id = dsp.shop_id AND dsd.slip_no = dsp.slip_no AND dsd.slip_detail_no = dsp.slip_detail_no  \n");
                    //sql.append("        INNER JOIN data_sales AS ds ON ds.shop_id = dsp.shop_id AND ds.slip_no = dsp.slip_no  \n");
                    sql.append("        INNER JOIN mst_staff ms ON ms.staff_id = dsp.staff_id  \n");                    
                    sql.append("        INNER JOIN (  \n");
                    sql.append("        SELECT data_proportionally_id,proportionally_integration_id\n");
                    sql.append("        FROM data_proportionally  \n");
                    sql.append("        inner join mst_proportionally msp using (proportionally_id)  \n");
                    sql.append("        inner join mst_proportionally_integration mpi using(proportionally_integration_id)  \n");
                    sql.append("        ) dp ON dp.data_proportionally_id = dsp.data_proportionally_id  \n");
                    sql.append("        WHERE dsp.delete_date IS NULL  \n");
                    //sql.append("        AND ds.delete_date IS NULL  \n");
                    sql.append("        AND dsd.sales_date between " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " AND " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) + "  \n");
                    sql.append("        AND dsd.shop_id in (" + paramBean.getShopIDList() + ")       " + "\n");
                    sql.append("        AND dsd.product_division = 1 \n");
                    sql.append("        AND ms.staff_id=" + SQLUtil.convertForSQL(staffId) + "\n");
                    sql.append("        GROUP BY dsd.slip_no , ms.staff_id,proportionally_integration_id\n");
                    sql.append(" ) b on a.proportionally_integration_id = b.proportionally_integration_id where a.delete_date is null  ");
                    sql.append(" ) data  \n");

                    sql.append("  GROUP BY data.staff_id\n");
                    // Thanh end edit 2014/06/02
                    ResultSetWrapper res = con.executeQuery(sql.toString());
                    while (res.next()) {
                        jx.setValue(20, 10 + rowCount, res.getInt("Œﬂ≤›ƒçáåv"));
                        jx.setValue(20, 10 + rowCount + 1, res.getDouble("ãZèpóøã‡"));
                    }
                    sql = new StringBuffer();
                    // Thanh start edit 2014/06/02
                    sql.append(" SELECT data.proportionally_integration_id,data.proportionally_integration_name,data.staff_id, coalesce(sum(data.value),0) AS ã‡äz, coalesce(sum(data.point),0)  AS Œﬂ≤›ƒ\n");
                    sql.append(" FROM ( \n");
                    sql.append("  SELECT a.proportionally_integration_name,a.display_seq,b.*\n");
                    sql.append(" FROM\n");
                    sql.append(" mst_proportionally_integration a\n");
                    sql.append(" left outer join \n");
                    sql.append(" (");
                    sql.append("       SELECT  proportionally_integration_id,dsd.slip_no , ms.staff_id, \n");
                    sql.append("        sum(floor( discount_detail_value_no_tax* dsp.ratio * 0.01) ) AS value,\n");
                    sql.append("        sum(dsp.point) AS point  \n");
                    sql.append("        FROM data_sales_proportionally AS dsp  \n");
                    sql.append("        INNER JOIN view_data_sales_detail_valid AS dsd ON dsd.shop_id = dsp.shop_id AND dsd.slip_no = dsp.slip_no AND dsd.slip_detail_no = dsp.slip_detail_no  \n");
                    //sql.append("        INNER JOIN data_sales AS ds ON ds.shop_id = dsp.shop_id AND ds.slip_no = dsp.slip_no  \n");
                    sql.append("        INNER JOIN mst_staff ms ON ms.staff_id = dsp.staff_id  \n");                    
                    sql.append("        INNER JOIN (  \n");
                    sql.append("        SELECT data_proportionally_id,proportionally_integration_id\n");
                    sql.append("        FROM data_proportionally  \n");
                    sql.append("        inner join mst_proportionally msp using (proportionally_id)  \n");
                    sql.append("        inner join mst_proportionally_integration mpi using(proportionally_integration_id)  \n");
                    sql.append("        ) dp ON dp.data_proportionally_id = dsp.data_proportionally_id  \n");
                    sql.append("        WHERE dsp.delete_date IS NULL  \n");
                    //sql.append("        AND ds.delete_date IS NULL  \n");
                    sql.append("        AND dsd.sales_date between " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " AND " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) + "  \n");
                    sql.append("        AND dsd.shop_id in (" + paramBean.getShopIDList() + ")       " + "\n");
                    sql.append("        AND dsd.product_division = 1 \n");
                    sql.append("        AND ms.staff_id=" + SQLUtil.convertForSQL(staffId) + "\n");
                    sql.append("        GROUP BY dsd.slip_no , ms.staff_id,proportionally_integration_id\n");
                    sql.append(" ) b on a.proportionally_integration_id = b.proportionally_integration_id where a.delete_date is null  ");
                    sql.append(" ) data  \n");

                    sql.append("  GROUP BY data.staff_id,data.proportionally_integration_name,data.proportionally_integration_id,DATA.display_seq \n");                    
                    sql.append("  order BY DATA.display_seq \n");
                    // Thanh end edit 2014/06/02
                    ResultSetWrapper rs1 = con.executeQuery(sql.toString());

                    int k = 0;
                    while (rs1.next()) {
                        jx.setValue(21 + k, 9 + rowCount, rs1.getString("proportionally_integration_name"));
                        jx.setValue(21 + k, 10 + rowCount, rs1.getInt("Œﬂ≤›ƒ"));
                        jx.setValue(21 + k, 10 + rowCount + 1, rs1.getDouble("ã‡äz"));
                        k++;
                    }
                } else {
                    jx.insertRow(10 + rowCount + (i - 1) * 2 + 1, 1);

                    jx.insertRow(10 + rowCount + (i - 1) * 2 + 1, 1);
                    jx.setValue(1, 10 + rowCount + i * 2, rs.getString("staff_name1") + rs.getString("staff_name2"));
                    jx.setValue(19, 10 + rowCount + i * 2, rs.getInt("ìXîÃé¿"));
                    for (int k = 1; k <= 19; k++) {
                        jx.mergeCells(k, 10 + rowCount + i * 2, k, 10 + rowCount + i * 2 + 1);
                    }
                    sql = new StringBuffer();

                    // Thanh start edit 2014/06/02
                    sql.append(" SELECT data.staff_id, coalesce(sum(data.value),0) AS ãZèpóøã‡, coalesce(sum(data.point),0)  AS Œﬂ≤›ƒçáåv\n");
                    sql.append(" FROM ( \n");
                    sql.append("  SELECT a.proportionally_integration_name,a.display_seq,b.*\n");
                    sql.append(" FROM\n");
                    sql.append(" mst_proportionally_integration a\n");
                    sql.append(" left outer join \n");
                    sql.append(" (");
                    sql.append("       SELECT  proportionally_integration_id,dsd.slip_no , ms.staff_id, \n");
                    sql.append("        sum(floor( discount_detail_value_no_tax* dsp.ratio * 0.01) ) AS value,\n");
                    sql.append("        sum(dsp.point) AS point  \n");
                    sql.append("        FROM data_sales_proportionally AS dsp  \n");
                    sql.append("        INNER JOIN view_data_sales_detail_valid AS dsd ON dsd.shop_id = dsp.shop_id AND dsd.slip_no = dsp.slip_no AND dsd.slip_detail_no = dsp.slip_detail_no  \n");
                    //sql.append("        INNER JOIN data_sales AS ds ON ds.shop_id = dsp.shop_id AND ds.slip_no = dsp.slip_no  \n");
                    sql.append("        INNER JOIN mst_staff ms ON ms.staff_id = dsp.staff_id  \n");                    
                    sql.append("        INNER JOIN (  \n");
                    sql.append("        SELECT data_proportionally_id,proportionally_integration_id\n");
                    sql.append("        FROM data_proportionally  \n");
                    sql.append("        inner join mst_proportionally msp using (proportionally_id)  \n");
                    sql.append("        inner join mst_proportionally_integration mpi using(proportionally_integration_id)  \n");
                    sql.append("        ) dp ON dp.data_proportionally_id = dsp.data_proportionally_id  \n");
                    sql.append("        WHERE dsp.delete_date IS NULL  \n");
                    //sql.append("        AND ds.delete_date IS NULL  \n");
                    sql.append("        AND dsd.sales_date between " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " AND " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) + "  \n");
                    sql.append("        AND dsd.shop_id in (" + paramBean.getShopIDList() + ")       " + "\n");
                    sql.append("        AND dsd.product_division = 1 \n");
                    sql.append("        AND ms.staff_id=" + SQLUtil.convertForSQL(staffId) + "\n");
                    sql.append("        GROUP BY dsd.slip_no , ms.staff_id,proportionally_integration_id\n");
                    sql.append(" ) b on a.proportionally_integration_id = b.proportionally_integration_id where a.delete_date is null  ");
                    sql.append(" ) data  \n");

                    sql.append("  GROUP BY data.staff_id \n");
                    // Thanh end edit 2014/06/02
                    ResultSetWrapper res = con.executeQuery(sql.toString());
                    while (res.next()) {
                        jx.setValue(20, 10 + rowCount + i * 2, res.getInt("Œﬂ≤›ƒçáåv"));
                        jx.setValue(20, 10 + rowCount + i * 2 + 1, res.getDouble("ãZèpóøã‡"));
                    }


                    sql = new StringBuffer();                   
                    sql.append(" SELECT data.proportionally_integration_id,data.proportionally_integration_name,data.staff_id, coalesce(sum(data.value),0) AS ã‡äz, coalesce(sum(data.point),0)  AS Œﬂ≤›ƒ\n");
                    sql.append(" FROM ( \n");
                    sql.append("  SELECT a.proportionally_integration_name,a.display_seq,b.*\n");
                    sql.append(" FROM\n");
                    sql.append(" mst_proportionally_integration a\n");
                    sql.append(" left outer join \n");
                    sql.append(" (");
                    sql.append("       SELECT  proportionally_integration_id,dsd.slip_no , ms.staff_id, \n");
                    sql.append("        sum(floor( discount_detail_value_no_tax* dsp.ratio * 0.01) ) AS value,\n");
                    sql.append("        sum(dsp.point) AS point  \n");
                    sql.append("        FROM data_sales_proportionally AS dsp  \n");
                    sql.append("        INNER JOIN view_data_sales_detail_valid AS dsd ON dsd.shop_id = dsp.shop_id AND dsd.slip_no = dsp.slip_no AND dsd.slip_detail_no = dsp.slip_detail_no  \n");
                    //sql.append("        INNER JOIN data_sales AS ds ON ds.shop_id = dsp.shop_id AND ds.slip_no = dsp.slip_no  \n");
                    sql.append("        INNER JOIN mst_staff ms ON ms.staff_id = dsp.staff_id  \n");                    
                    sql.append("        INNER JOIN (  \n");
                    sql.append("        SELECT data_proportionally_id,proportionally_integration_id\n");
                    sql.append("        FROM data_proportionally  \n");
                    sql.append("        inner join mst_proportionally msp using (proportionally_id)  \n");
                    sql.append("        inner join mst_proportionally_integration mpi using(proportionally_integration_id)  \n");
                    sql.append("        ) dp ON dp.data_proportionally_id = dsp.data_proportionally_id  \n");
                    sql.append("        WHERE dsp.delete_date IS NULL  \n");
                    //sql.append("        AND ds.delete_date IS NULL  \n");
                    sql.append("        AND dsd.sales_date between " + SQLUtil.convertForSQL(paramBean.getCalculationStartDateObj()) + " AND " + SQLUtil.convertForSQL(paramBean.getCalculationEndDateObj()) + "  \n");
                    sql.append("        AND dsd.shop_id in (" + paramBean.getShopIDList() + ")       " + "\n");
                    sql.append("        AND dsd.product_division = 1 \n");
                    sql.append("        AND ms.staff_id=" + SQLUtil.convertForSQL(staffId) + "\n");
                    sql.append("        GROUP BY dsd.slip_no , ms.staff_id,proportionally_integration_id\n");
                    sql.append(" ) b on a.proportionally_integration_id = b.proportionally_integration_id where a.delete_date is null  ");
                    sql.append(" ) data  \n");
                    sql.append("  GROUP BY data.staff_id,data.proportionally_integration_name,data.proportionally_integration_id,DATA.display_seq \n");
                    sql.append("  order BY DATA.display_seq \n");
                    //IVS_LVTu end edit 2015/11/27 Bug #44795


                    ResultSetWrapper rs1 = con.executeQuery(sql.toString());
                    int k = 0;
                    while (rs1.next()) {
                        jx.setValue(21 + k, 10 + rowCount + i * 2, rs1.getInt("Œﬂ≤›ƒ"));
                        jx.setValue(21 + k, 10 + rowCount + i * 2 + 1, rs1.getDouble("ã‡äz"));
                        k++;
                    }
                }

                i++;
            }
            sql = new StringBuffer();

        } catch (Exception e) {
        }



        //ÉtÉ@ÉCÉãèoóÕ
        jx.openWorkbook();

        return true;
    }
    //IVS end add ÉAÉNÉeÉBÉuå⁄ãqï™êÕèàóùÇ∑ÇÈÅB20121127
}
