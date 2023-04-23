/*
 * StaffRankingList.java
 *
 * Created on 2008/07/20, 22:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report;

import com.geobeck.sosia.pos.hair.report.logic.ReportLogic;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.system.SystemInfo;

/**
 * �X�^�b�t�����L���O�ꗗ����
 * @author saito
 */
public class StaffRankingList extends ArrayList<StaffRanking>
{
        //�ŋ敪
        private final	int	TAX_TYPE_BLANK		=   1;	//�Ŕ�
	private final	int	TAX_TYPE_UNIT		=   2;	//�ō�
        //�\���͈�
	private final	int	RANGE_DISPLAY_TO_10	=   0;	//�`10��
	private final	int	RANGE_DISPLAY_TO_20	=   1;	//�`20��
	private final	int	RANGE_DISPLAY_ALL	=   2;	//�S��

	/**
	 * ��S���E�{�p�S���^�u�C���f�b�N�X
	 */
	private	Integer	selectedTabIndex	=   null;
        /**
	 * �X��ID���X�g
	 */
	private	String   shopIDList             =   null;
        /**
	 * �Ώۊ���(�J�n��)
	 */
	private	GregorianCalendar   termFrom    =   new GregorianCalendar();
	/**
	 * �Ώۊ���(�I����)
	 */
	private	GregorianCalendar   termTo      =   new GregorianCalendar();
	/**
	 * �ŋ敪
	 */
	private	Integer	taxType          =	null;
	/**
	 * �\���͈�
	 */
	private	Integer	rangeDisplay    =	null;
	/**
	 * �\����
	 */
	private	Integer	orderDisplay      =	null;
        
        //nhanvt start add 20141216 New request #33406
        private String listCategoryId = null;
        private boolean hideCategory = false;
        private Integer useShopCategory = null;
        
        //nhanvt start add 20150309 New request #35223
        
        //��N�ƖڕW��
        private boolean isLastYear = false;
        
        //�S���҃^�u��I��
        private boolean pic = false; 
        
        private boolean isMutiShop = false;
        private String calculationStartDate = "";
        private String calculationEndDate = "";
        private boolean isValue = false;
        private Integer month = null;
        private Integer year = null;
        private String nameColumn = null;
        private String countDate = null;
        //nhanvt end add 20150309 New request #35223
        private boolean courseFlag = false;

        public boolean isCourseFlag() {
            return courseFlag;
        }

        public void setCourseFlag(boolean courseFlag) {
            this.courseFlag = courseFlag;
        }
    
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
	//nhanvt end add 20141216 New request #33406
        
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
        public String getCountDate() {
            return countDate;
        }

        public void setCountDate(String countDate) {
            this.countDate = countDate;
        }
        //nhanvt end add 20150309 New request #35223
        
	/**
         * Creates a new instance of StaffRankingList
         */
	public StaffRankingList()
	{
	}
	
	/**
	 * �I�����ꂽ�^�u�C���f�b�N�X�擾����B
	 * @return �I�����ꂽ�^�u�C���f�b�N�X
	 */
        public int getSelectedTabIndex() {
                return selectedTabIndex;
        }
	/**
         * �I�����ꂽ�^�u�C���f�b�N�X���Z�b�g����B
         * @param selectedTabIndex �I�����ꂽ�^�u�C���f�b�N�X
         */
        public void setSelectedTabIndex(int selectedTabIndex) {
                this.selectedTabIndex = selectedTabIndex;
        }
	/**
	 * �X��ID���X�g���擾����B
	 * @return �X��ID���X�g
	 */
        public String getShopIDList() {
                return shopIDList;
        }
	/**
	 * �X��ID���X�g���Z�b�g����B
	 * @param shopIDList �X��ID���X�g
	 */
        public void setShopIDList(String shopIDList) {
                this.shopIDList = shopIDList;
        }
	/**
	 * �Ώۊ���(�J�n��)���擾����B
	 * @return �Ώۊ���(�J�n��)
	 */
        public String getTermFrom() {
                return SQLUtil.convertForSQLDateOnly(termFrom);
        }
	/**
	 * �Ώۊ���(�J�n��)���Z�b�g����B
	 * @param termFrom �Ώۊ���(�J�n��)
	 */
        public void setTermFrom(java.util.Date termFrom) {
                this.termFrom.setTime(termFrom);
        }
	/**
	 * �Ώۊ���(�I����)���擾����B
	 * @return �Ώۊ���(�I����)
	 */
        public String getTermTo() {
                return SQLUtil.convertForSQLDateOnly(termTo);
        }
	/**
	 * �Ώۊ���(�I����)���Z�b�g����B
	 * @param termTo �Ώۊ���(�I����)
	 */
        public void setTermTo(java.util.Date termTo) {
                this.termTo.setTime(termTo);
        }
	/**
	 * �ŋ敪���擾����B
	 * @return �ŋ敪
	 */
        public int getTaxType() {
                return taxType;
        }
	/**
	 * �ŋ敪���Z�b�g����B
	 * @param taxType �ŋ敪
	 */
        public void setTaxType(int taxType) {
                this.taxType = taxType;
        }
	/**
	 * �\���͈͂��擾����B
	 * @return �\���͈�
	 */
        public int getRangeDisplay() {
                return rangeDisplay;
        }
	/**
	 * �\���͈͂��Z�b�g����B
	 * @param rangeDisplay �\���͈�
	 */
        public void setRangeDisplay(int rangeDisplay) {
                this.rangeDisplay = rangeDisplay;
        }
	/**
	 * �\�������擾����B
	 * @return �\����
	 */
        public int getOrderDisplay() {
                return orderDisplay;
        }
	/**
         * �\�������Z�b�g����B
         * @param orderDisplay �\����
         */
        public void setOrderDisplay(int orderDisplay) {
                this.orderDisplay = orderDisplay;
        }
        
        public boolean isPic() {
        return pic;
        }

        public void setPic(boolean pic) {
            this.pic = pic;
        }
    /**
     * �f�[�^��ǂݍ��ށB
     */
	public void load()
	{
		try
		{
                    //�����L���O���X�g�擾
                    this.getStaffRankingList();
		}
		catch(Exception e)
		{
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
        
        //nhanvt start add 20150309 New request #35223
        public void loadLastYear()
	{
		try
		{
                    //�����L���O���X�g�擾
                    this.getStaffRankingListLastYear();
		}
		catch(Exception e)
		{
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
        //nhanvt end add 20150309 New request #35223
	
	/**
	 * �X�^�b�t�����L���O�̃��X�g���擾����B
	 * @exception Exception
	 */
	private void getStaffRankingList() throws Exception
	{
		this.clear();
		
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(this.getSelectStaffRankingSQL());

		while(rs.next())
		{
			StaffRanking temp = new StaffRanking();
                        //�S���҃^�u��I���̂�true,�{�p�S���^�u��I���̂�false
                        temp.setPic(this.pic);
                        //LVTu start add 2016/02/05 New request #46728
                        temp.setCourseFlag(courseFlag);
                        //LVTu end add 2016/02/05 New request #46728
                        //��N�A�ڕW
                        temp.setIsLastYear(this.isIsLastYear());
			
                        temp.setData(rs);
			this.add(temp);
		}
		
		rs.close();
	}
        
        //nhanvt start add 20150309 New request #35223
        private void getStaffRankingListLastYear() throws Exception
	{
		this.clear();
                ResultSetWrapper rs = null;
		if(isIsLastYear()){
                    rs= SystemInfo.getConnection().executeQuery(this.getSelectStaffRankingLastYearSQL());
                }else{
                    rs = SystemInfo.getConnection().executeQuery(this.getTargetResultNormal());
                }
		while(rs.next())
		{
			StaffRanking temp = new StaffRanking();
			temp.setData(rs);
			this.add(temp);
		}
		
		rs.close();
	}
        //nhanvt end add 20150309 New request #35223
	
        //LVTu start edit 2016/01/19 New request #46728
	/**
	 * �X�^�b�t�����L���O���o�pSQL���擾����B
	 * @return �X�^�b�t�����L���O���o�pSQL
	 * @exception Exception
	 */
	private String getSelectStaffRankingSQL() throws Exception
	{
          
            
	    StringBuilder sql = new StringBuilder(1000);
            sql.append(" select * "); 
            sql.append(" from  ");
            sql.append(" ( ");
	    sql.append(" select");
            sql.append("      ms.staff_id ");
	    sql.append("      ,ms.staff_name1");
	    sql.append("     ,ms.staff_name2");
            sql.append("     ,mss.shop_id");
	    sql.append("     ,mss.shop_name");
	    sql.append("     ,coalesce(totalcount, 0)        as totalcount");
	    sql.append("     ,coalesce(techcount, 0)         as techcount");
	    sql.append("     ,coalesce(techsales, 0)         as techsales");
	    sql.append("     ,coalesce(designatedCount, 0)   as designatedCount");
	    sql.append("     ,coalesce(designatedSales, 0)   as designatedSales");
	    sql.append("     ,coalesce(newcount, 0)          as newcount");
	    sql.append("     ,coalesce(introducecount, 0)    as introducecount");
	    sql.append("     ,coalesce(itemsales, 0)         as itemsales");
	    sql.append("     ,coalesce(approachedCount, 0)   as approachedCount");
	    sql.append("     ,coalesce(approachedSales, 0)   as approachedSales");
            sql.append("     ,coalesce(coursecount, 0) AS coursecount ");
            sql.append("     ,coalesce(courseSales, 0) AS courseSales ");
            sql.append("     ,coalesce(course_digestionSales, 0) AS course_digestionSales ");
            //sql.append("     ,coalesce(repeat_rate, 0)   as repeat_rate");
            //nhanvt start add 20150309 New request #35223
            //IVS_TMTrong start edit 2015-07-20 Bug #40595
                //Luc start edit 20150724 #40595
                //sql.append( "          ,coalesce((select count(distinct slip_no) ");
                sql.append( "          ,coalesce((select count(distinct customer_id) ");
                //Luc start edit 20150724 #40595
                sql.append( "                from view_data_sales_detail_valid ds ");
              
                
                //sql.append( "                where ds.sales_date  BETWEEN '"+getCalculationStartDate()+"' AND '"+getCalculationEndDate() +"'");
                sql.append("     where ds.sales_date between " + getTermFrom() + " and " + getTermTo());
                sql.append("           and ds.shop_id in (" + getShopIDList() + ")");
                sql.append( "                and ds.staff_id = t.staff_id  ");
                sql.append( "                AND EXISTS ");
                sql.append( "                          (SELECT 1 ");
                sql.append( "                           FROM data_sales_detail dsd ");
                if (this.getListCategoryId() != null && this.getListCategoryId() != "" ) {
                    sql.append("     INNER JOIN data_sales_mainstaff dsmt ON dsmt.shop_id = ds.shop_id AND dsmt.slip_no = ds.slip_no ");
                }
                sql.append( "                           INNER JOIN mst_technic mst ON mst.technic_id = dsd.product_id ");
                sql.append( "                           LEFT JOIN mst_technic_class mstc ON mstc.technic_class_id = mst.technic_class_id ");
                sql.append( "                           WHERE dsd.shop_id = ds.shop_id ");
                sql.append( "                             AND dsd.slip_no = ds.slip_no ");
                sql.append( "                             AND dsd.slip_detail_no = ds.slip_detail_no ");
                sql.append( "                             AND dsd.delete_date IS NULL ");
                sql.append( "                             AND dsd.product_division IN (1,3) ");
                if (this.getListCategoryId() != null && this.getListCategoryId() != "" ) {
                    sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");
                } 

                sql.append( "                           UNION ALL SELECT 1");
                sql.append( "                           FROM data_sales_detail dsd");
                if (this.getListCategoryId() != null && this.getListCategoryId() != "" ) {
                    sql.append("     INNER JOIN data_sales_mainstaff dsmt ON dsmt.shop_id = ds.shop_id AND dsmt.slip_no = ds.slip_no ");
                }
                sql.append( "                           INNER JOIN mst_item mi ON dsd.product_id = mi.item_id");
                //IVS_LVTu start edit 2015/08/07 Bug #41292
                //sql.append( "                           AND mi.delete_date IS NULL");
                sql.append( "                           LEFT JOIN mst_item_class mic ON mic.item_class_id = mi.item_class_id");
                //sql.append( "                           AND mic.delete_date IS NULL");
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
                if (this.getListCategoryId() != null && this.getListCategoryId() != "" ) {
                    sql.append("     INNER JOIN data_sales_mainstaff dsmt ON dsmt.shop_id = ds.shop_id AND dsmt.slip_no = ds.slip_no ");
                }
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
                //sql.append( "                         And exists (select 1 from data_sales ds1 ");
                sql.append( "                         And exists (select 1 from view_data_sales_detail_valid ds1 ");
                sql.append( "                                     where ds1.customer_id = ds.customer_id ");
                if(isIsMutiShop()){
                   sql.append( "                                      and ds1.shop_id in (" + getShopIDList() + ") ");
                }
                sql.append( "           AND ds1.staff_id = ds.staff_id ");
                if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                   sql.append("  "+ checkListCategory() +" ");
                }
               //Luc start edit 20150724 #40595
               //sql.append( "    and ds1.sales_date <= ds.sales_date - interval '"+getCountDate()+"' ");
               //sql.append( "    	    and ds1.sales_date > ds.sales_date - interval '"+getCountDate()+"' - interval '1 month' ");
               sql.append( "    and ds1.sales_date < to_date("+getTermFrom()+",'YYYY/MM/dd') ");
               sql.append( "    	    and ds1.sales_date >= to_date("+getTermFrom()+",'YYYY/MM/dd') - interval '"+getCountDate()+"' ");
                
               //Luc start edit 20150724 #40595
               sql.append( "                          )");
               sql.append( "                 ),0)  as repeat_count ");
               
               sql.append(", ( ");
               sql.append("     SELECT count(distinct(ds1.customer_id)) ");	
               //sql.append("     FROM data_sales ds ");
               sql.append("     FROM view_data_sales_detail_valid ds1 ");
               sql.append("     WHERE ds1.sales_date < to_date("+getTermFrom()+",'YYYY/MM/dd') ");
               sql.append("     AND ds1.sales_date >=  to_date("+getTermFrom()+",'YYYY/MM/dd') - interval '"+getCountDate()+"' ");
               if(isIsMutiShop()){
                    sql.append("     AND ds1.shop_id in (" + getShopIDList() + ") ");
               }
               sql.append("  and ds1.staff_id = t.staff_id ");
               if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                   sql.append("  "+ checkListCategory() +" ");
               }
               sql.append(" ) AS repeat_total ");
            //IVS_TMTrong end edit 2015-07-20 Bug #40595  
            
            sql.append("       ," + getDataLastYear() + " ");   
               //nhanvt end add 20150309 New request #35223
	    sql.append(" from");
	    sql.append("     mst_staff ms");
	    sql.append("         left outer join mst_shop mss");
	    sql.append("             using(shop_id)");
	    sql.append("         inner join");
	    sql.append("             (" + getSelectStaffRankingValueSQL(false) + ") t");
	    sql.append("             using(staff_id)");
	    sql.append(" where");
	    sql.append("     mss.delete_date is null");

	    
	    if(this.getSelectedTabIndex() == 0){

            sql.append(" ) as this_year \n");

            sql.append(" left join "); 
            sql.append(" (");
            sql.append(" SELECT ");
            sql.append("             dt.shop_id, ");
            sql.append("             dt.staff_id,");
            sql.append("              0 as approachedCount_target, ");
            sql.append("              0 as approachedSales_target, ");
            
            //IVS_LVTu start edit 2015/08/11 Bug #4129
            if(getTaxType() == TAX_TYPE_BLANK){
                // �Ŕ���
                sql.append(" 	ceil(coalesce(sum(target_technic / (1 +get_tax_rate(to_date(year || '-' || month || '- 01' ,'YYYY/MM/dd')))),0)) AS techSales_target, /*�Z�p����*/");
                sql.append(" 	ceil(coalesce(sum(target_nomination_value / (1 +get_tax_rate(to_date(year || '-' || month || '- 01' ,'YYYY/MM/dd')))),0)) as designatedSales_target , /*�w������*/");
                sql.append("    ceil(coalesce(sum(target_item / (1 +get_tax_rate(to_date(year || '-' || month || '- 01' ,'YYYY/MM/dd')))),0)) as itemSales_target, /*���i����*/");
            } else {
                // �ō���
                sql.append(" 	coalesce(sum(target_technic),0) AS techSales_target, /*�Z�p����*/");
                sql.append(" 	sum(target_nomination_value) as designatedSales_target , /*�w������*/");
                sql.append("    sum(target_item) as itemSales_target , /*���i����*/");
            }
                        
            sql.append("        sum(target_technic_num + target_item_num) AS totalCount_target, /*���q��*/");
            sql.append(" 	coalesce(sum(target_technic_num), 0) AS techCount_target, /*�Z�p�q��*/");
            //sql.append(" 	coalesce(sum(target_technic),0) AS techSales_target, /*�Z�p����*/");
            sql.append("         sum(target_nomination_num) as designatedCount_target /*�w���q��*/");
            //sql.append(" 	,sum(target_nomination_value) as designatedSales_target /*�w������*/");
            sql.append(" 	,sum(target_new_num ) as newCount_target/*�V�K�q��*/");
            sql.append("        ,sum(target_introduction_num) as introduceCount_target  /*�Љ�q��*/");
            //sql.append("        ,sum(target_item) as itemSales_target /*���i����*/");
            sql.append("        ,sum(" + getNameColumn() + ") as repeat_rate_target  ");
            //IVS_LVTu end edit 2015/08/11 Bug #4129
            sql.append(" 	,coalesce(sum(target_course),0) AS courseSales_target");
            sql.append(" 	,coalesce(sum(target_digestion),0) AS courseDigestionSales_target");

            sql.append("     FROM data_target_staff_normal dt ");
            sql.append("     where ");
            sql.append("     dt.delete_date is null ");
            sql.append("       and  dt.shop_id in (" + getShopIDList() + ")");
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
            //Luc start edit 20150707 #40006
            sql.append("      GROUP BY dt.shop_id,dt.staff_id ");
            sql.append("    ) as target on this_year.staff_id = target.staff_id and this_year.shop_id = target.shop_id ");
            switch(getOrderDisplay()) {
                    //���q��
                    case 0:
                        sql.append(" order by totalcount desc");
                        break;
                    //�Z�p�q��    
                    case 1:
                        sql.append(" order by techcount desc");
                         break;
                    //�Z�p����    
                    case 2:
                        sql.append(" order by techsales desc");
                         break;
                    //�w���q��    
                    case 3:
                        sql.append(" order by designatedCount desc");
                         break;
                    //�w������    
                    case 4:
                        sql.append(" order by designatedSales desc");
                         break;
                    //�V�K�q��    
                    case 5:
                        sql.append(" order by newcount desc"); 
                         break;
                    //�Љ�q��    
                    case 6:
                        sql.append(" order by introducecount desc");
                         break;
                    //���i����    
                    case 7:
                        sql.append(" order by itemsales desc");
                         break;
                    //������    
                    case 8:
                        sql.append(" order by (coalesce(techsales, 0) + coalesce(itemsales, 0)) desc");
                         break;
                    //���q�P��    
                    case 9:
                        sql.append(" order by case when coalesce(techcount, 0) = 0 then 0 else (coalesce(techsales, 0) + coalesce(itemsales, 0)) / coalesce(totalcount, 0) end desc");
                         break;
                    //�Z�p�q�P��
                    case 10:
                       sql.append(" order by case when coalesce(techcount, 0) = 0 then 0 else (coalesce(techsales, 0)) / coalesce(techcount, 0) end desc"); 
                         break;
                }
                //Luc end edit 20150624 #38408

	    }else{
		//�\����
                sql.append(") as t");
                //�{�p�q��
		if (getOrderDisplay() == 0)
		{
		    sql.append(" order by techcount desc");
		}
                //�{�p����
		if (getOrderDisplay() == 1)
		{
		    sql.append(" order by techsales desc");
		}
                 //�{�p�w���q��
		if (getOrderDisplay() == 2)
		{
		    sql.append(" order by designatedCount desc");
		}
                 //�{�p�w������
		if (getOrderDisplay() == 3)
		{
		    sql.append(" order by designatedSales desc");
		}
		else if (getOrderDisplay() == 4)
		{
		    // �A�v���[�`�q��
		    sql.append(" order by approachedCount desc");
		}
		else if (getOrderDisplay() == 5)
		{
		    // �A�v���[�`����
		    sql.append(" order by approachedSales desc");
		}
		else if (getOrderDisplay() == 6)
		{
		    // ���i����
		    sql.append(" order by coalesce(itemsales, 0) desc");
		}
		else if (getOrderDisplay() == 7)
		{
		    // ������
                    // Fix IVS_ptquang
		    sql.append(" order by (coalesce(techsales, 0) + coalesce(itemsales, 0)) + coalesce(coursesales, 0) desc");
		}
		else if (getOrderDisplay() == 8)
		{
		    // �q�P��
		    sql.append(" order by case when coalesce(techcount, 0) = 0 then 0 else (coalesce(techsales, 0) + coalesce(itemsales, 0)) / coalesce(totalcount, 0) end desc");
		}
	    }
	    
	    //�\���͈�
            if (getRangeDisplay() == RANGE_DISPLAY_TO_10)
            {
                   //�`10��
                   sql.append(" limit 10");
            }
            else if (getRangeDisplay() == RANGE_DISPLAY_TO_20)
            {
                   //�`20��
                   sql.append(" limit 20");
            }
            //Luc end edit 20150707 #40006
            return sql.toString();
            
            
            
	}
        //nhanvt start add 20150309 New request #35223
        private String getSelectStaffRankingLastYearSQL() throws Exception
	{
	    StringBuilder sql = new StringBuilder(1000);
	    sql.append(" select");
            sql.append("     ms.staff_id");
	    sql.append("     , ms.staff_name1");
	    sql.append("     ,ms.staff_name2");
	    sql.append("     ,mss.shop_name");
	    sql.append("     ,coalesce(totalcount, 0)        as totalcount");
	    sql.append("     ,coalesce(techcount, 0)         as techcount");
	    sql.append("     ,coalesce(techsales, 0)         as techsales");
	    sql.append("     ,coalesce(designatedCount, 0)   as designatedCount");
	    sql.append("     ,coalesce(designatedSales, 0)   as designatedSales");
	    sql.append("     ,coalesce(newcount, 0)          as newcount");
	    sql.append("     ,coalesce(introducecount, 0)    as introducecount");
	    sql.append("     ,coalesce(itemsales, 0)         as itemsales");
	    sql.append("     ,coalesce(approachedCount, 0)   as approachedCount");
	    sql.append("     ,coalesce(approachedSales, 0)   as approachedSales");
            //sql.append("     ,coalesce(repeat_rate, 0)   as repeat_rate");
            sql.append( "          ,coalesce((select count(distinct slip_no) ");
                sql.append( "                from view_data_sales_detail_valid ds ");
                sql.append( "                where ds.sales_date between date " + getTermFrom() + " -interval '1 year' and date " + getTermTo() +" -interval '1 year' ");
               sql.append("          and  ds.shop_id in (" + getShopIDList() + ")");
                sql.append( "                and ds.staff_id = t.staff_id  ");
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
                if (this.getListCategoryId() != null && this.getListCategoryId() != "" ) {
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

               //sql.append( "                                     and ds1.sales_date BETWEEN date '"+getCalculationStartDate()+"' -interval '1 year' AND date '"+getCalculationEndDate() +"' -interval '1 year' ");
               
                sql.append( "    and ds1.sales_date <= ds.sales_date - interval '"+getCountDate()+"' ");
		sql.append( "    	    and ds1.sales_date >= ds.sales_date - interval '"+getCountDate()+"' - interval '1 month' ");
               
               
               sql.append( "                          )");
               sql.append( "                 ),0)  as repeat_rate ");
                 // nhanvt khtn
	    sql.append(" from");
	    sql.append("     mst_staff ms");
	    sql.append("         left outer join mst_shop mss");
	    sql.append("             using(shop_id)");
	    sql.append("         inner join");
	    sql.append("             (" + getSelectStaffRankingValueLastYearSQL() + ") t");
	    sql.append("             using(staff_id)");
	    sql.append(" where");
	    sql.append("     mss.delete_date is null");

	    
	    if(this.getSelectedTabIndex() == 0){
		//�\����
		if (getOrderDisplay() < 8)
		{
		    sql.append(" order by " + (getOrderDisplay() + 4) + " desc");
		}
		else if (getOrderDisplay() == 8)
		{
		    // ������
		    sql.append(" order by (coalesce(techsales, 0) + coalesce(itemsales, 0)) desc");
		}
		else if (getOrderDisplay() == 9)
		{
		    // ���q�P��
		    sql.append(" order by case when coalesce(techcount, 0) = 0 then 0 else (coalesce(techsales, 0) + coalesce(itemsales, 0)) / coalesce(totalcount, 0) end desc");
		}
		else if (getOrderDisplay() == 10)
		{
		    // �Z�p�q�P��
		    sql.append(" order by case when coalesce(techcount, 0) = 0 then 0 else (coalesce(techsales, 0)) / coalesce(techcount, 0) end desc");
		}

	    }else{
		//�\����
		if (getOrderDisplay() < 4)
		{
		    sql.append(" order by " + (getOrderDisplay() + 5) + " desc");
		}
		else if (getOrderDisplay() == 4)
		{
		    // �A�v���[�`�q��
		    sql.append(" order by approachedCount desc");
		}
		else if (getOrderDisplay() == 5)
		{
		    // �A�v���[�`����
		    sql.append(" order by approachedSales desc");
		}
		else if (getOrderDisplay() == 6)
		{
		    // ���i����
		    sql.append(" order by coalesce(itemsales, 0) desc");
		}
		else if (getOrderDisplay() == 7)
		{
		    // ������
		    sql.append(" order by (coalesce(techsales, 0) + coalesce(itemsales, 0)) desc");
		}
		else if (getOrderDisplay() == 8)
		{
		    // �q�P��
		    sql.append(" order by case when coalesce(techcount, 0) = 0 then 0 else (coalesce(techsales, 0) + coalesce(itemsales, 0)) / coalesce(totalcount, 0) end desc");
		}
	    }
	    
	    //�\���͈�
            if (getRangeDisplay() == RANGE_DISPLAY_TO_10)
            {
                   //�`10��
                   sql.append(" limit 10");
            }
            else if (getRangeDisplay() == RANGE_DISPLAY_TO_20)
            {
                   //�`20��
                   sql.append(" limit 20");
            }
            
            return sql.toString();
	}
        //nhanvt end add 20150309 New request #35223
	
	/**
	 * �X�^�b�t�����L���O�̊e���㒊�o�pSQL���擾����B
	 *
	 * @param
	 * @return �X�^�b�t�����L���O�̊e���㒊�o�pSQL
	 * @exception Exception
	 */
	private String getSelectStaffRankingValueSQL( Boolean isLast) throws Exception
	{
            
	    StringBuilder sql = new StringBuilder(1000);

	    if (getSelectedTabIndex() == 0)
	    {
		// ��S��
		sql.append(" select");
		sql.append("      c.staff_id");
		sql.append("     /* ��S�����q�� */");
		//sql.append("     ,count( distinct a.slip_no ) as totalCount");
                //LVTu start edit 2016/02/04 New request #46728
                if(courseFlag) {
                    sql.append("     ,count(DISTINCT CASE WHEN product_division IN(1,2,3,4,5,6) THEN a.slip_no else null end) as totalCount");
                }else {
                    sql.append("     ,count(DISTINCT CASE WHEN product_division IN(1,2,3,4) THEN a.slip_no else null end) as totalCount");
                }
                sql.append("     ,count(CASE WHEN product_division IN(5,6) and exists(select 1 ");
                sql.append("     from data_sales_detail dsd ");
                sql.append("     where dsd.shop_id = a.shop_id and dsd.slip_no = a.slip_no and dsd.staff_id = a.staff_id) ");
                sql.append("     THEN a.slip_no ELSE NULL END) AS courseCount "); 
		sql.append("     /* ��S���Z�p�q�� */");
		sql.append("     ,count( distinct case when product_division IN(1,3) then a.slip_no else null end ) as techCount");
		sql.append("     /* ��S���w���q�� */");
		sql.append("     ,count( distinct case when product_division IN(1,3) and a.designated_flag then a.slip_no else null end ) as designatedCount");

		sql.append("     /* ��S���V�K�q�� */");
                //IVS_TMTrong start edit 20150717 Bug #40595
//		sql.append("     ,count( distinct case when product_division IN(1,3) and get_visit_count(a.customer_id ,a.shop_id ,a.sales_date) = 1 ");
//		sql.append("         and customer_no <> '0' then a.slip_no else null end ) as newCount");
                if(isMutiShop){
                    sql.append("     ,count( distinct case when product_division IN(1,3) and get_visit_count(a.customer_id ,a.shop_id ,a.sales_date) = 1 ");
                    sql.append("         and customer_no <> '0' then a.slip_no else null end ) as newCount");
                }else{
                    sql.append("     ,count(DISTINCT CASE ");
                    sql.append("                WHEN product_division IN (1,3) ");
                    sql.append("                AND get_visit_count(a.customer_id, a.shop_id, a.sales_date) = 1 ");
                    sql.append("                AND ( ");
                    sql.append("                        SELECT count(slip_no) + coalesce(max(before_visit_num), 0) ");
                    sql.append("                        FROM data_sales ");
                    sql.append("                        INNER JOIN mst_customer USING (customer_id) ");
                    sql.append("                        WHERE customer_id = a.customer_id ");
                    sql.append("                                AND data_sales.shop_id IN ( ");
                    sql.append("                                    SELECT shop_id FROM mst_shop WHERE shop_id <> a.shop_id)");
                    sql.append("                                AND data_sales.sales_date <= a.sales_date ");
                    sql.append("                                AND data_sales.delete_date IS NULL ");
                    sql.append("                  ) < 1 ");
                    sql.append("                AND customer_no <> '0' ");
                    sql.append("                THEN a.slip_no ");
                    sql.append("                ELSE NULL");
                    sql.append("       END) AS newCount");
                }
                //IVS_TMTrong end edit 20150717 Bug #40595
		sql.append("     /* ��S���V�K�Љ�q�� */");
		sql.append("     ,count( distinct case when product_division IN(1,3) and get_visit_count(a.customer_id ,a.shop_id ,a.sales_date) = 1 ");
		sql.append("         and customer_no <> '0' and introducer_id is not null then a.slip_no else null end ) as introduceCount");

		sql.append("     /* �A�v���[�`�q�� */");
		sql.append("     ,0 as approachedCount");
                sql.append("     /* �A�v���[�`���� */");
                sql.append("     ,0 as approachedSales");
                
                
		if(getTaxType() == TAX_TYPE_BLANK){

		    // �Ŕ���
		    sql.append("     /* ��S���Z�p����(�Ŕ�) */");
		    sql.append("     ,sum( case when product_division IN(1,3) then discount_detail_value_no_tax else 0 end ) as techSales");
		    sql.append("     /* ��S���w������(�Ŕ�) */");
		    sql.append("     ,sum( case when product_division IN(1,3) and a.designated_flag then discount_detail_value_no_tax else 0 end ) as designatedSales");
		    sql.append("     /* ��S�����i����(�Ŕ�) */");
		    sql.append("     ,sum( case when product_division IN(2,4) then discount_detail_value_no_tax else 0 end ) as itemSales");
                    sql.append("     ,sum(CASE WHEN product_division IN(5) THEN discount_detail_value_no_tax ELSE 0 END) AS courseSales ");
                    // LVTu start Edit 2019/11/01 SPOS���őΉ�
//                    sql.append("     ,sum(CASE WHEN product_division IN(6) THEN discount_detail_value_no_tax*(select dcd.product_num from ");
//                    sql.append("     data_contract_digestion dcd");
//                    sql.append("     inner join data_sales_detail dsd on dsd.contract_no = dcd.contract_no ");
//                    sql.append("     and dsd.shop_id = dcd.shop_id");
//                    sql.append("     and dsd.slip_no = dcd.slip_no");
//                    sql.append("     and dsd.contract_detail_no = dcd.contract_detail_no");
//                    sql.append("     WHERE dsd.shop_id = a.shop_id");
//                    sql.append("     AND dsd.slip_no = a.slip_no");
//                    sql.append("     and dsd.staff_id = a.detail_staff_id");
//                    sql.append("     and dsd.slip_detail_no = a.slip_detail_no");
//                    sql.append("     ) ELSE 0 END) AS course_digestionSales");

		} else {

		    // �ō���
		    sql.append("     /* ��S���Z�p����(�ō�) */");
		    sql.append("     ,sum( case when product_division IN(1,3) then discount_detail_value_in_tax else 0 end ) as techSales");
		    sql.append("     /* ��S���w������(�ō�) */");
		    sql.append("     ,sum( case when product_division IN(1,3) and a.designated_flag then discount_detail_value_in_tax else 0 end ) as designatedSales");
		    sql.append("     /* ��S�����i����(�ō�) */");
		    sql.append("     ,sum( case when product_division IN(2,4) then discount_detail_value_in_tax else 0 end ) as itemSales");
                    sql.append("     ,sum(CASE WHEN product_division IN(5) THEN discount_detail_value_in_tax ELSE 0 END) AS courseSales ");
//                    sql.append("     ,sum(CASE WHEN product_division IN(6) THEN discount_detail_value_in_tax*(select dcd.product_num from ");
//                    sql.append("     data_contract_digestion dcd ");
//                    sql.append("     inner join data_sales_detail dsd on dsd.contract_no = dcd.contract_no ");
//                    sql.append("     and dsd.shop_id = dcd.shop_id");
//                    sql.append("     and dsd.slip_no = dcd.slip_no");
//                    sql.append("     and dsd.contract_detail_no = dcd.contract_detail_no");
//                    sql.append("     WHERE dsd.shop_id = a.shop_id");
//                    sql.append("     AND dsd.slip_no = a.slip_no");
//                    sql.append("     and dsd.staff_id = a.detail_staff_id");
//                    sql.append("     and dsd.slip_detail_no = a.slip_detail_no");
//                    sql.append("     ) ELSE 0 END) AS course_digestionSales");

		}
                sql.append(" ,sum( case when product_division in(6) ");
                sql.append(getConsumptionValueSQL());
                sql.append(" else 0 end ) as course_digestionsales ");
                // LVTu end Edit 2019/11/01 SPOS���őΉ�

		sql.append(" from");
		sql.append("     view_data_sales_detail_valid a");
                
		sql.append("         left join mst_customer b using(customer_id)");
               if (this.getListCategoryId() != null && this.getListCategoryId() != "" ) {
                      sql.append("     INNER JOIN data_sales_mainstaff dsmt ON dsmt.shop_id = a.shop_id AND dsmt.slip_no = a.slip_no ");
                }
                sql.append("         inner join mst_staff c");
                if (this.getListCategoryId() != null && this.getListCategoryId() != "" ) {
                    sql.append("         on dsmt.staff_id = c.staff_id"); 
                }else {
                    sql.append("         on a.staff_id = c.staff_id"); 
                }
                //IVS_LVTu start edit 2015/04/14 Bug #36254
                //sql.append("         and a.shop_id = c.shop_id"); 
                //IVS_LVTu end edit 2015/04/14 Bug #36254
		sql.append(" where");
		sql.append("         a.shop_id in (" + getShopIDList() + ")");
                if(isLast){
                    sql.append("     and a.sales_date between date" + getTermFrom() + " - interval '1 year' and date" + getTermTo()+" -interval '1 year'");
                }else {
		sql.append("     and a.sales_date between " + getTermFrom() + " and " + getTermTo());
                }
                //nhanvt start add 20141216 New request #33406
                if(this.getUseShopCategory() != null && this.getUseShopCategory() == 1){
                    sql.append("      and   exists");
                    sql.append("             (");

                    //�Ƒԗ��p���邩���Ȃ����`�F�b�N

                    sql.append("                 SELECT");
                    sql.append("                     1");
                    sql.append("                 FROM");
                    sql.append("                     data_sales_detail dsd");
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "" ) {
                        sql.append("     INNER JOIN data_sales_mainstaff dsmt ON dsmt.shop_id = dsd.shop_id AND dsmt.slip_no = dsd.slip_no ");
                    }
                    sql.append("                     inner join mst_technic mst on mst.technic_id = dsd.product_id ");
                    sql.append("                     left join mst_technic_class mstc on mstc.technic_class_id = mst.technic_class_id ");
                    sql.append("                 WHERE");
                    sql.append("                         dsd.shop_id = a.shop_id");
                    sql.append("                     AND dsd.slip_no = a.slip_no");
                    sql.append("                     and dsd.slip_detail_no = a.slip_detail_no   ");
                    sql.append("                     AND dsd.delete_date is null");
                    sql.append("                     AND dsd.product_division in (1,3)");
                    //�ƑԑI���̏ꍇ
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "" ) {
                        sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");
                    }                    
                    sql.append("                  UNION ALL  ");
                    sql.append("                  SELECT ");
                    sql.append("                     1");
                    sql.append("                 FROM");
                    sql.append("                      data_sales_detail dsd");
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "" ) {
                        sql.append("     INNER JOIN data_sales_mainstaff dsmt ON dsmt.shop_id = dsd.shop_id AND dsmt.slip_no = dsd.slip_no ");
                    }
                    // IVS_TKDU START EDIT 20160607 #51200 �����L���O�i��S���j���i����E������̏W�v�s��
                    // sql.append("                     inner join mst_item mi on dsd.product_id = mi.item_id and mi.delete_date is null ");
                    sql.append("                     inner join mst_item mi on dsd.product_id = mi.item_id ");
                    // IVS_TKDU END EDIT 20160607 #51200 �����L���O�i��S���j���i����E������̏W�v�s��
                    sql.append("                     left join mst_item_class mic on mic.item_class_id = mi.item_class_id and mic.delete_date is null ");
                    sql.append("                  WHERE");
                    sql.append("                          dsd.shop_id = a.shop_id");
                    sql.append("                      AND dsd.slip_no = a.slip_no");
                    sql.append("                     and dsd.slip_detail_no = a.slip_detail_no   ");
                    sql.append("                      AND dsd.delete_date is null");
                    sql.append("                      AND dsd.product_division in (2,4)");
                    //�ƑԑI���̏ꍇ
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                        sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");
                    }
                    if(courseFlag) {                   
                    sql.append("                 UNION ALL  ");
                    sql.append("                 SELECT ");
                    sql.append("                     1");
                    sql.append("                 FROM");
                    sql.append("                     data_sales_detail dsd");
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "" ) {
                        sql.append("     INNER JOIN data_sales_mainstaff dsmt ON dsmt.shop_id = dsd.shop_id AND dsmt.slip_no = dsd.slip_no ");
                    }
                    sql.append("                     inner join mst_course msc on msc.course_id = dsd.product_id ");
                    sql.append("                     left join mst_course_class mscc on mscc.course_class_id = msc.course_class_id ");
                    sql.append("                 WHERE");
                    sql.append("                         dsd.shop_id = a.shop_id");
                    sql.append("                     AND dsd.slip_no = a.slip_no");
                    sql.append("                     and dsd.slip_detail_no = a.slip_detail_no   ");
                    sql.append("                     AND dsd.delete_date is null");
                    sql.append("                     AND dsd.product_division in (5,6)");
                    }
                    //�ƑԑI���̏ꍇ
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                        sql.append("                  AND mscc.shop_category_id in (" + this.getListCategoryId() + ") ");
                    }

                    sql.append(" ) ");
                }
                //nhanvt end add 20141216 New request #33406
                
		sql.append(" group by");
		sql.append("     c.staff_id");
                   
	    }
	    else
	    {
		// �{�p�S��
		sql.append(" select");
                sql.append("      ms.staff_id    as staff_id");
		sql.append("     /* �{�p���q�� */");
		//sql.append("     ,count( distinct a.slip_no ) as totalCount");
                if(courseFlag) {
                    sql.append("     ,count( DISTINCT CASE WHEN product_division IN(1,2,3,4,5,6) THEN a.slip_no else null end ) as totalCount");
                }else {
                    sql.append("     ,count( DISTINCT CASE WHEN product_division IN(1,2,3,4) THEN a.slip_no else null end ) as totalCount");
                }
                sql.append("     ,count(CASE WHEN product_division IN(5,6) and exists(select 1 ");
                sql.append("     from data_sales_detail dsd ");
                //-Luc start add #47781
                sql.append("    left join data_contract dc on dsd.shop_id = dc.shop_id and dsd.slip_no = dc.slip_no and dsd.product_division = 5 ");
                sql.append("    left join data_contract_staff_share c  ");
                sql.append("    on c.shop_id = dc.shop_id and c.slip_no = dc.slip_no  ");
                sql.append("    and c.contract_no = dc.contract_no and c.contract_detail_no = dc.contract_detail_no ");
                //Luc end add #47781
                // sql.append("     where dsd.shop_id = a.shop_id and dsd.slip_no = a.slip_no and dsd.staff_id = a.staff_id) ");
                sql.append("     where dsd.shop_id = a.shop_id and dsd.slip_no = a.slip_no  AND coalesce(dsd.staff_id,c.staff_id) = a.detail_staff_id) ");
        
                sql.append("     THEN a.slip_no ELSE NULL END) AS courseCount "); 
		sql.append("     /* �{�p�q�� */");
		sql.append("     ,count( distinct case when product_division IN(1,3) then a.slip_no else null end ) as techCount");
		sql.append("     /* �{�p�w���q�� */");
		sql.append("     ,count( distinct case when product_division IN(1,3) and detail_designated_flag then a.slip_no else null end ) as designatedCount");

		sql.append("     /* �{�p�V�K�q�� */");
		sql.append("     ,count( distinct case when product_division IN(1,3) and get_visit_count(a.customer_id ,a.shop_id ,a.sales_date) = 1 ");
		sql.append("         and customer_no <> '0' then a.slip_no else null end ) as newCount");
		sql.append("     /* �{�p�V�K�Љ�q�� */");
		sql.append("     ,count( distinct case when product_division IN(1,3) and get_visit_count(a.customer_id ,a.shop_id ,a.sales_date) = 1 ");
		sql.append("         and customer_no <> '0' and introducer_id is not null then a.slip_no else null end ) as introduceCount");

		sql.append("     /* �A�v���[�`�q�� */");
		sql.append("     ,count( distinct case when product_division = 1 and detail_approached_flag then a.slip_no else null end ) as approachedCount");
                sql.append("     , 0 as repeat_rate ");
		if(getTaxType() == TAX_TYPE_BLANK){

		    // �Ŕ���
		    sql.append("     /* �{�p����(�Ŕ�) */");
		    sql.append("     ,sum( case when product_division IN(1,3) then discount_detail_value_no_tax else 0 end ) as techSales");
		    sql.append("     /* �{�p�w������(�Ŕ�) */");
		    sql.append("     ,sum( case when product_division IN(1,3) and detail_designated_flag then discount_detail_value_no_tax else 0 end ) as designatedSales");
		    sql.append("     /* ���i����(�Ŕ�) */");
		    sql.append("     ,sum( case when product_division IN(2,4) then discount_detail_value_no_tax else 0 end ) as itemSales");

		    sql.append("     /* �A�v���[�`����(�Ŕ�) */");
		    sql.append("     ,sum( case when product_division = 1 and detail_approached_flag then discount_detail_value_no_tax else 0 end ) as approachedSales");
                    sql.append("     ,sum(CASE WHEN product_division IN(5) THEN discount_detail_value_no_tax ELSE 0 END) AS courseSales ");
                    // LVTu start Edit 2019/11/01 SPOS���őΉ�
//                    sql.append("     ,sum(CASE WHEN product_division IN(6) THEN discount_detail_value_no_tax*(select dcd.product_num from ");
//                    sql.append("     data_contract_digestion dcd  ");
//                    sql.append("     inner join data_sales_detail dsd on dsd.contract_no = dcd.contract_no ");
//                    sql.append("     and dsd.shop_id = dcd.shop_id");
//                    sql.append("     and dsd.slip_no = dcd.slip_no");
//                    sql.append("     and dsd.contract_detail_no = dcd.contract_detail_no");
//                    sql.append("     WHERE dsd.shop_id = a.shop_id");
//                    sql.append("     AND dsd.slip_no = a.slip_no");
//                    sql.append("     and dsd.staff_id = a.detail_staff_id");
//                    sql.append("     and dsd.slip_detail_no = a.slip_detail_no");
//                    sql.append("     ) ELSE 0 END) AS course_digestionSales");
                    
		} else {

		    // �ō���
		    sql.append("     /* �{�p����(�ō�) */");
		    sql.append("     ,sum( case when product_division IN(1,3) then discount_detail_value_in_tax else 0 end ) as techSales");
		    sql.append("     /* �{�p�w������(�ō�) */");
		    sql.append("     ,sum( case when product_division IN(1,3) and detail_designated_flag then discount_detail_value_in_tax else 0 end ) as designatedSales");
		    sql.append("     /* ���i����(�ō�) */");
		    sql.append("     ,sum( case when product_division IN(2,4) then discount_detail_value_in_tax else 0 end ) as itemSales");

		    sql.append("     /* �A�v���[�`����(�ō�) */");
		    sql.append("     ,sum( case when product_division = 1 and detail_approached_flag then discount_detail_value_in_tax else 0 end ) as approachedSales");
                    sql.append("     ,sum(CASE WHEN product_division IN(5) THEN discount_detail_value_in_tax ELSE 0 END) AS courseSales ");
//                    sql.append("     ,sum(CASE WHEN product_division IN(6) THEN discount_detail_value_in_tax*(select dcd.product_num from ");
//                    sql.append("     data_contract_digestion dcd ");
//                    sql.append("     inner join data_sales_detail dsd on dsd.contract_no = dcd.contract_no ");
//                    sql.append("     and dsd.shop_id = dcd.shop_id");
//                    sql.append("     and dsd.slip_no = dcd.slip_no");
//                    sql.append("     and dsd.contract_detail_no = dcd.contract_detail_no");
//                    sql.append("     WHERE dsd.shop_id = a.shop_id");
//                    sql.append("     AND dsd.slip_no = a.slip_no");
//                    sql.append("     and dsd.staff_id = a.detail_staff_id");
//                    sql.append("     and dsd.slip_detail_no = a.slip_detail_no");
//                    sql.append("     ) ELSE 0 END) AS course_digestionSales");

                }
                sql.append(" ,sum( case when product_division in(6) ");
                sql.append(getConsumptionValueSQL());
                sql.append(" else 0 end ) as course_digestionsales ");
                // LVTu end Edit 2019/11/01 SPOS���őΉ�

		sql.append(" from");
                //Luc start edit 20160218 #47781
		//sql.append("     (" + ReportLogic.getDataSalesDetailShareTable() + ") a");
                sql.append("     (" + getDataSalesDetailShareTable() + ") a");
                 //Luc end edit 20160218 #47781
                
		sql.append("         left join mst_customer b using(customer_id)");
                sql.append(" INNER JOIN mst_staff ms ");
                sql.append("    on a.detail_staff_id = ms.staff_id ");
                //IVS_LVTu start edit 2015/04/14 Bug #36254
                //sql.append("         and a.shop_id = ms.shop_id"); 
                //IVS_LVTu end edit 2015/04/14 Bug #36254
		sql.append(" where");
		sql.append("         a.shop_id in (" + getShopIDList() + ")");
		sql.append("     and a.sales_date between " + getTermFrom() + " and " + getTermTo());
		sql.append("     and exists");
		sql.append("         (");
		sql.append("             select 1 from mst_staff");
		sql.append("              where staff_id = a.detail_staff_id");
		sql.append("         )");
                //nhanvt start add 20141216 New request #33406
                if(this.getUseShopCategory() != null && this.getUseShopCategory() == 1){
                    sql.append("      and   exists");
                    sql.append("             (");

                    //�Ƒԗ��p���邩���Ȃ����`�F�b�N

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
                    //�ƑԑI���̏ꍇ
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
                    //�ƑԑI���̏ꍇ
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                        sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");
                    }
                    if (courseFlag) {                   
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
                    //�ƑԑI���̏ꍇ
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                        sql.append("                  AND mscc.shop_category_id in (" + this.getListCategoryId() + ") ");
                    }
                    }
                    sql.append(" ) ");
                }
                //nhanvt end add 20141216 New request #33406
		sql.append(" group by");
		sql.append("     ms.staff_id");
		
	    }
            
            return sql.toString();
	}
        
        // LVTu start add 2019/11/01 SPOS���őΉ�
        /**
         * �������z�擾�r�p�k
         * 201805 add #35807
         * @return 
         */
        private String getConsumptionValueSQL() {
            
            StringBuilder sql = new StringBuilder();
            sql.append(" THEN  ( select ");
            // LVTu start Edit 2019/08/26 SPOS���őΉ�
            sql.append(" SUM(total_consumption_value) as total_consumption_value ");
            sql.append(" FROM ");
            sql.append(" ( SELECT ");
             if(getTaxType() == TAX_TYPE_BLANK){
                 sql.append(" ceil( sum( dcd.product_num * dc.product_value / dc.product_num ) / ( ");
                 sql.append("   1.0 + get_tax_rate(dsd.tax_rate, max(coalesce(ds.sales_date, current_date))) ))  AS total_consumption_value ");
             }else {
                 // IVS start edit 2019/04/03
                 sql.append(" ceil( sum(dcd.product_num * dc.product_value / dc.product_num))  AS total_consumption_value ");
                 // IVS end edit 2019/04/03
             }
             sql.append(" from  data_sales ds ");
             sql.append("   inner join data_sales_detail dsd ");
             sql.append("     on ds.slip_no = dsd.slip_no ");
             sql.append("         and ds.shop_id = dsd.shop_id ");
             sql.append("   inner join data_contract_digestion dcd ");
             sql.append("     on dsd.shop_id = dcd.shop_id ");
             sql.append("        and dsd.slip_no = dcd.slip_no ");
            // IVS start add 2019/04/03
             sql.append("        and dsd.contract_no = dcd.contract_no ");
            // IVS end add 2019/04/03
             sql.append("        and dcd.contract_detail_no = dsd.contract_detail_no ");
             sql.append("   inner join data_contract dc ");
             sql.append("     on dcd.contract_shop_id = dc.shop_id ");
             sql.append("       and dcd.contract_no = dc.contract_no ");
             sql.append("       and dcd.contract_detail_no = dc.contract_detail_no ");
             sql.append("       and dc.product_id = dsd.product_id ");
             sql.append("   WHERE  dsd.shop_id = a.shop_id ");
             sql.append("      AND dsd.slip_no = a.slip_no ");
             sql.append("      and dsd.staff_id = a.detail_staff_id ");
             sql.append("      and dsd.slip_detail_no = a.slip_detail_no ");
             sql.append("   GROUP BY dsd.shop_id, ");
             sql.append("   dsd.slip_no, ");
             sql.append("   dsd.tax_rate) t) ");
             // LVTu end Edit 2019/08/26 SPOS���őΉ�
             
             return sql.toString();
        }
        // LVTu end Edit 2019/11/01 SPOS���őΉ�
        
        //Luc start add 20160218  #47781
        public static String getDataSalesDetailShareTable() {

            StringBuilder sql = new StringBuilder(1000);
            // IVS_LVTu start edit 2017/07/20 #19019 [gb] �����L���O���{�p�S���^�u �R�[�X�_�񂪕������ׂ���ƁA�R�[�X�_��̋��z����������
            sql.append(" select DISTINCT");
            // IVS_LVTu end edit 2017/07/20 #19019 [gb] �����L���O���{�p�S���^�u �R�[�X�_�񂪕������ׂ���ƁA�R�[�X�_��̋��z����������
            sql.append("      a.shop_id");
            sql.append("     ,a.slip_no");
            sql.append("     ,a.sales_date");
            sql.append("     ,a.customer_id");
            sql.append("     ,a.designated_flag");
            sql.append("     ,a.staff_id");
            sql.append("     ,a.visit_num");
            sql.append("     ,a.visited_memo");
            sql.append("     ,a.next_visit_date");
            sql.append("     ,a.reappearance_date");
            sql.append("     ,a.slip_detail_no");
            sql.append("     ,a.product_division");
            sql.append("     ,a.product_id");
            sql.append("     ,a.product_num");
            sql.append("     ,a.product_value");
            sql.append("     ,a.discount_rate");
            sql.append("     ,a.discount_value");
            sql.append("     ,a.detail_designated_flag");
            sql.append("     ,coalesce(b.approached_flag, a.detail_approached_flag, false) as detail_approached_flag");
            
            //Luc start edit 20160218  #47781
            //sql.append("     ,coalesce(b.staff_id, a.detail_staff_id) as detail_staff_id");
            //sql.append("     ,trunc(a.detail_value_in_tax * coalesce(b.ratio, 100) / 100) as detail_value_in_tax");
            //sql.append("     ,trunc(a.detail_value_no_tax * coalesce(b.ratio, 100) / 100) as detail_value_no_tax");
            //sql.append("     ,trunc(a.discount_detail_value_in_tax * coalesce(b.ratio, 100) / 100) as discount_detail_value_in_tax");
            //sql.append("     ,trunc(a.discount_detail_value_no_tax * coalesce(b.ratio, 100) / 100) as discount_detail_value_no_tax");
            
            //GEOBECK start edit 20160823 #54147
            //sql.append("     ,case when c.staff_id is not null then c.staff_id else case when a.detail_staff_id is not null then a.detail_staff_id else b.staff_id end  end   as detail_staff_id\n");
            sql.append("     ,case when c.staff_id is not null then c.staff_id else case when b.staff_id is not null then b.staff_id else a.detail_staff_id end  end   as detail_staff_id\n");            
            //GEOBECK end edit 20160823 #54147
            sql.append("     ,trunc(a.detail_value_in_tax * coalesce(b.ratio, 100) / 100) * (coalesce(c.rate,100)/100::float) as detail_value_in_tax");
            sql.append("     ,trunc(a.detail_value_no_tax * coalesce(b.ratio, 100) / 100) * (coalesce(c.rate,100)/100::float) as detail_value_no_tax");
            sql.append("     ,trunc(a.discount_detail_value_in_tax * coalesce(b.ratio, 100) / 100) * (coalesce(c.rate,100)/100::float) as discount_detail_value_in_tax");
            sql.append("     ,trunc(a.discount_detail_value_no_tax * coalesce(b.ratio, 100) / 100) * (coalesce(c.rate,100)/100::float) as discount_detail_value_no_tax");
            //Luc start edit 20160218  #47781
            
            sql.append("     ,trunc(a.account_setting_value * coalesce(b.ratio, 100) / 100) as account_setting_value");
            sql.append(" from");
            sql.append("     view_data_sales_detail_valid a");
            sql.append("         left join data_sales_detail_proportionally b");
            sql.append("         using(shop_id, slip_no, slip_detail_no)");
            
            //Luc start add 20160218 #47781
            sql.append("     left join data_contract dc on a.shop_id = dc.shop_id and a.slip_no = dc.slip_no and a.product_division = 5 ");
            sql.append("     left join data_contract_staff_share c ");
            sql.append("     on c.shop_id = dc.shop_id and c.slip_no = dc.slip_no ");
            sql.append("     and c.contract_no = dc.contract_no and c.contract_detail_no = dc.contract_detail_no ");
           
            //--Luc end add 20160218 #47781
            return sql.toString();
        }
        //Luc start add 20160218  #47781
        //nhanvt start add 20150309 New request #35223
        
        private String getSelectStaffRankingValueLastYearSQL() throws Exception
	{
            
	    StringBuilder sql = new StringBuilder(1000);

	    if (getSelectedTabIndex() == 0)
	    {
		// ��S��
		sql.append(" select");
		sql.append("      a.staff_id");
		sql.append("     /* ��S�����q�� */");
		sql.append("     ,count( distinct slip_no ) as totalCount");
		sql.append("     /* ��S���Z�p�q�� */");
		sql.append("     ,count( distinct case when product_division IN(1,3) then slip_no else null end ) as techCount");
		sql.append("     /* ��S���w���q�� */");
		sql.append("     ,count( distinct case when product_division IN(1,3) and designated_flag then slip_no else null end ) as designatedCount");

		sql.append("     /* ��S���V�K�q�� */");
		sql.append("     ,count( distinct case when product_division IN(1,3) and get_visit_count(a.customer_id ,a.shop_id ,a.sales_date) = 1 ");
		sql.append("         and customer_no <> '0' then slip_no else null end ) as newCount");
		sql.append("     /* ��S���V�K�Љ�q�� */");
		sql.append("     ,count( distinct case when product_division IN(1,3) and get_visit_count(a.customer_id ,a.shop_id ,a.sales_date) = 1 ");
		sql.append("         and customer_no <> '0' and introducer_id is not null then slip_no else null end ) as introduceCount");

		sql.append("     /* �A�v���[�`�q�� */");
		sql.append("     ,0 as approachedCount");
                sql.append("     /* �A�v���[�`���� */");
                sql.append("     ,0 as approachedSales");
                
		if(getTaxType() == TAX_TYPE_BLANK){

		    // �Ŕ���
		    sql.append("     /* ��S���Z�p����(�Ŕ�) */");
		    sql.append("     ,sum( case when product_division IN(1,3) then discount_detail_value_no_tax else 0 end ) as techSales");
		    sql.append("     /* ��S���w������(�Ŕ�) */");
		    sql.append("     ,sum( case when product_division IN(1,3) and designated_flag then discount_detail_value_no_tax else 0 end ) as designatedSales");
		    sql.append("     /* ��S�����i����(�Ŕ�) */");
		    sql.append("     ,sum( case when product_division IN(2,4) then discount_detail_value_no_tax else 0 end ) as itemSales");

		} else {

		    // �ō���
		    sql.append("     /* ��S���Z�p����(�ō�) */");
		    sql.append("     ,sum( case when product_division IN(1,3) then discount_detail_value_in_tax else 0 end ) as techSales");
		    sql.append("     /* ��S���w������(�ō�) */");
		    sql.append("     ,sum( case when product_division IN(1,3) and designated_flag then discount_detail_value_in_tax else 0 end ) as designatedSales");
		    sql.append("     /* ��S�����i����(�ō�) */");
		    sql.append("     ,sum( case when product_division IN(2,4) then discount_detail_value_in_tax else 0 end ) as itemSales");
		}

		sql.append(" from");
		sql.append("     view_data_sales_detail_valid a");
		sql.append("         left join mst_customer b using(customer_id)");
                sql.append("         inner join mst_staff c using(staff_id)");
		sql.append(" where");
		sql.append("         a.shop_id in (" + getShopIDList() + ")");
		sql.append("     and a.sales_date between date " + getTermFrom() + "  -interval '1 year' and date " + getTermTo() +" -interval '1 year' ");
  
                if(this.getUseShopCategory() != null && this.getUseShopCategory() == 1){
                    sql.append("      and   exists");
                    sql.append("             (");

                    //�Ƒԗ��p���邩���Ȃ����`�F�b�N

                    sql.append("                 SELECT");
                    sql.append("                     1");
                    sql.append("                 FROM");
                    sql.append("                     data_sales_detail dsd");
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "" ) {
                        sql.append("     INNER JOIN data_sales_mainstaff dsmt ON dsmt.shop_id = dsd.shop_id AND dsmt.slip_no = dsd.slip_no ");
                    }
                    sql.append("                     inner join mst_technic mst on mst.technic_id = dsd.product_id ");
                    sql.append("                     left join mst_technic_class mstc on mstc.technic_class_id = mst.technic_class_id ");
                    sql.append("                 WHERE");
                    sql.append("                         dsd.shop_id = a.shop_id");
                    sql.append("                     AND dsd.slip_no = a.slip_no");
                    sql.append("                     and dsd.slip_detail_no = a.slip_detail_no   ");
                    sql.append("                     AND dsd.delete_date is null");
                    sql.append("                     AND dsd.product_division in (1,3)");
                    //�ƑԑI���̏ꍇ
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "" ) {
                        sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");
                    }                    
                    sql.append("                  UNION ALL  ");
                    sql.append("                  SELECT ");
                    sql.append("                     1");
                    sql.append("                 FROM");
                    sql.append("                      data_sales_detail dsd");
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "" ) {
                        sql.append("     INNER JOIN data_sales_mainstaff dsmt ON dsmt.shop_id = dsd.shop_id AND dsmt.slip_no = dsd.slip_no ");
                    }
                    sql.append("                     inner join mst_item mi on dsd.product_id = mi.item_id and mi.delete_date is null ");
                    sql.append("                     left join mst_item_class mic on mic.item_class_id = mi.item_class_id and mic.delete_date is null ");
                    sql.append("                  WHERE");
                    sql.append("                          dsd.shop_id = a.shop_id");
                    sql.append("                      AND dsd.slip_no = a.slip_no");
                    sql.append("                     and dsd.slip_detail_no = a.slip_detail_no   ");
                    sql.append("                      AND dsd.delete_date is null");
                    sql.append("                      AND dsd.product_division in (2,4)");
                    //�ƑԑI���̏ꍇ
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                        sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");
                    }
                                       
                    sql.append("                 UNION ALL  ");
                    sql.append("                 SELECT ");
                    sql.append("                     1");
                    sql.append("                 FROM");
                    sql.append("                     data_sales_detail dsd");
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "" ) {
                        sql.append("     INNER JOIN data_sales_mainstaff dsmt ON dsmt.shop_id = dsd.shop_id AND dsmt.slip_no = dsd.slip_no ");
                    }
                    sql.append("                     inner join mst_course msc on msc.course_id = dsd.product_id ");
                    sql.append("                     left join mst_course_class mscc on mscc.course_class_id = msc.course_class_id ");
                    sql.append("                 WHERE");
                    sql.append("                         dsd.shop_id = a.shop_id");
                    sql.append("                     AND dsd.slip_no = a.slip_no");
                    sql.append("                     and dsd.slip_detail_no = a.slip_detail_no   ");
                    sql.append("                     AND dsd.delete_date is null");
                    sql.append("                     AND dsd.product_division in (5,6)");
                    //�ƑԑI���̏ꍇ
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                        sql.append("                  AND mscc.shop_category_id in (" + this.getListCategoryId() + ") ");
                    }

                    sql.append(" ) ");
                }
                
		sql.append(" group by");
		sql.append("     a.staff_id");
		
	    }
	    else
	    {
		// �{�p�S��
		sql.append(" select");
		sql.append("      detail_staff_id    as staff_id");
		sql.append("     /* �{�p���q�� */");
		sql.append("     ,count( distinct slip_no ) as totalCount");
		sql.append("     /* �{�p�q�� */");
		sql.append("     ,count( distinct case when product_division IN(1,3) then slip_no else null end ) as techCount");
		sql.append("     /* �{�p�w���q�� */");
		sql.append("     ,count( distinct case when product_division IN(1,3) and detail_designated_flag then slip_no else null end ) as designatedCount");

		sql.append("     /* �{�p�V�K�q�� */");
		sql.append("     ,count( distinct case when product_division IN(1,3) and get_visit_count(a.customer_id ,a.shop_id ,a.sales_date) = 1 ");
		sql.append("         and customer_no <> '0' then slip_no else null end ) as newCount");
		sql.append("     /* �{�p�V�K�Љ�q�� */");
		sql.append("     ,count( distinct case when product_division IN(1,3) and get_visit_count(a.customer_id ,a.shop_id ,a.sales_date) = 1 ");
		sql.append("         and customer_no <> '0' and introducer_id is not null then slip_no else null end ) as introduceCount");

		sql.append("     /* �A�v���[�`�q�� */");
		sql.append("     ,count( distinct case when product_division = 1 and detail_approached_flag then slip_no else null end ) as approachedCount");
                
		if(getTaxType() == TAX_TYPE_BLANK){

		    // �Ŕ���
		    sql.append("     /* �{�p����(�Ŕ�) */");
		    sql.append("     ,sum( case when product_division IN(1,3) then discount_detail_value_no_tax else 0 end ) as techSales");
		    sql.append("     /* �{�p�w������(�Ŕ�) */");
		    sql.append("     ,sum( case when product_division IN(1,3) and detail_designated_flag then discount_detail_value_no_tax else 0 end ) as designatedSales");
		    sql.append("     /* ���i����(�Ŕ�) */");
		    sql.append("     ,sum( case when product_division IN(2,4) then discount_detail_value_no_tax else 0 end ) as itemSales");

		    sql.append("     /* �A�v���[�`����(�Ŕ�) */");
		    sql.append("     ,sum( case when product_division = 1 and detail_approached_flag then discount_detail_value_no_tax else 0 end ) as approachedSales");
                    
		} else {

		    // �ō���
		    sql.append("     /* �{�p����(�ō�) */");
		    sql.append("     ,sum( case when product_division IN(1,3) then discount_detail_value_in_tax else 0 end ) as techSales");
		    sql.append("     /* �{�p�w������(�ō�) */");
		    sql.append("     ,sum( case when product_division IN(1,3) and detail_designated_flag then discount_detail_value_in_tax else 0 end ) as designatedSales");
		    sql.append("     /* ���i����(�ō�) */");
		    sql.append("     ,sum( case when product_division IN(2,4) then discount_detail_value_in_tax else 0 end ) as itemSales");

		    sql.append("     /* �A�v���[�`����(�ō�) */");
		    sql.append("     ,sum( case when product_division = 1 and detail_approached_flag then discount_detail_value_in_tax else 0 end ) as approachedSales");
                }

		sql.append(" from");
		sql.append("     (" + ReportLogic.getDataSalesDetailShareTable() + ") a");
		sql.append("         left join mst_customer b using(customer_id)");
		sql.append(" where");
		sql.append("         a.shop_id in (" + getShopIDList() + ")");
		sql.append("     and a.sales_date between date " + getTermFrom() + " -interval '1 year' and date " + getTermTo() +" -interval '1 year' ");
		sql.append("     and exists");
		sql.append("         (");
		sql.append("             select 1 from mst_staff");
		sql.append("              where staff_id = a.detail_staff_id");
		sql.append("         )");
                //nhanvt start add 20141216 New request #33406
                if(this.getUseShopCategory() != null && this.getUseShopCategory() == 1){
                    sql.append("      and   exists");
                    sql.append("             (");

                    //�Ƒԗ��p���邩���Ȃ����`�F�b�N

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
                    //�ƑԑI���̏ꍇ
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
                    //�ƑԑI���̏ꍇ
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
                    //�ƑԑI���̏ꍇ
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                        sql.append("                  AND mscc.shop_category_id in (" + this.getListCategoryId() + ") ");
                    }
                    sql.append(" ) ");
                }
                //nhanvt end add 20141216 New request #33406
		sql.append(" group by");
		sql.append("     detail_staff_id");
		
	    }
            
            return sql.toString();
	}
        /**
         * 
         * @return 
         */
        public String getTargetResultNormal(){
            StringBuilder sql = new StringBuilder(1000);
            
            sql.append(" SELECT ");
            sql.append("             '' AS staff_name1, ");
            sql.append("             '' as staff_name2,");
            sql.append("             '' as shop_name,");
            sql.append("             dt.staff_id,");
            sql.append("              0 as approachedCount, ");
            sql.append("              0 as approachedSales, ");
            
            sql.append("            sum(target_technic_num + target_item_num) AS totalCount, /*���q��*/");
            sql.append(" 	coalesce(sum(target_technic_num), 0) AS techCount, /*�Z�p�q��*/");
            sql.append(" 	coalesce(sum(target_technic),0) AS techSales, /*�Z�p����*/");
            sql.append("         sum(target_nomination_num) as designatedCount /*�w���q��*/");
            sql.append(" 	,sum(target_nomination_value) as designatedSales /*�w������*/");
            sql.append(" 	,sum(target_new_num ) as newCount/*�V�K�q��*/");
            sql.append("            ,sum(target_introduction_num) as introduceCount  /*�Љ�q��*/");
            sql.append("            ,sum(target_item) as itemSales /*���i����*/");
            sql.append("          ,sum(" + getNameColumn() + ") as repeat_rate  ");

            sql.append("     FROM data_target_staff_normal dt ");
            sql.append("     where ");
            sql.append("     dt.delete_date is null ");
            sql.append("       and  dt.shop_id in (" + getShopIDList() + ")");
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

            sql.append("      GROUP BY dt.shop_id, ");
            sql.append(" 	dt.staff_id");
            return sql.toString();
        }
       
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
           
            return listData;
        }
       
        //nhanvt end add 20150309 New request #35223
        //IVS_LVTu start add 2015/08/07 Bug #41292
        public String checkListCategory() {
            StringBuilder sql = new StringBuilder(1000);
            sql.append("      and   exists");
            sql.append("             (");
            //�Ƒԗ��p���邩���Ȃ����`�F�b�N
            sql.append("                 SELECT");
            sql.append("                     1");
            sql.append("                 FROM");
            sql.append("                     data_sales_detail dsd");
            sql.append("     INNER JOIN data_sales_mainstaff dsmt ON dsmt.shop_id = dsd.shop_id AND dsmt.slip_no = dsd.slip_no ");
            sql.append("                     inner join mst_technic mst on mst.technic_id = dsd.product_id ");
            sql.append("                     left join mst_technic_class mstc on mstc.technic_class_id = mst.technic_class_id ");
            sql.append("                 WHERE");
            sql.append("                         dsd.shop_id = ds1.shop_id");
            sql.append("                     AND dsd.slip_no = ds1.slip_no");
            sql.append("                     and dsd.slip_detail_no = ds1.slip_detail_no   ");
            sql.append("                     AND dsd.delete_date is null");
            sql.append("                     AND dsd.product_division in (1,3)");
            //�ƑԑI���̏ꍇ
            sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");                
            sql.append("                  UNION ALL  ");
            sql.append("                  SELECT ");
            sql.append("                     1");
            sql.append("                 FROM");
            sql.append("                      data_sales_detail dsd");
            sql.append("     INNER JOIN data_sales_mainstaff dsmt ON dsmt.shop_id = dsd.shop_id AND dsmt.slip_no = dsd.slip_no ");
            sql.append("                     inner join mst_item mi on dsd.product_id = mi.item_id ");
            sql.append("                     left join mst_item_class mic on mic.item_class_id = mi.item_class_id ");
            sql.append("                  WHERE");
            sql.append("                          dsd.shop_id = ds1.shop_id");
            sql.append("                      AND dsd.slip_no = ds1.slip_no");
            sql.append("                     and dsd.slip_detail_no = ds1.slip_detail_no   ");
            sql.append("                      AND dsd.delete_date is null");
            sql.append("                      AND dsd.product_division in (2,4)");
            //�ƑԑI���̏ꍇ
            sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");
            sql.append("                 UNION ALL  ");
            sql.append("                 SELECT ");
            sql.append("                     1");
            sql.append("                 FROM");
            sql.append("                     data_sales_detail dsd");
            sql.append("     INNER JOIN data_sales_mainstaff dsmt ON dsmt.shop_id = dsd.shop_id AND dsmt.slip_no = dsd.slip_no ");
            sql.append("                     inner join mst_course msc on msc.course_id = dsd.product_id ");
            sql.append("                     left join mst_course_class mscc on mscc.course_class_id = msc.course_class_id ");
            sql.append("                 WHERE");
            sql.append("                         dsd.shop_id = ds1.shop_id");
            sql.append("                     AND dsd.slip_no = ds1.slip_no");
            sql.append("                     and dsd.slip_detail_no = ds1.slip_detail_no   ");
            sql.append("                     AND dsd.delete_date is null");
            sql.append("                     AND dsd.product_division in (5,6)");
            //�ƑԑI���̏ꍇ
                sql.append("                  AND mscc.shop_category_id in (" + this.getListCategoryId() + ") ");
            sql.append(" ) ");

            return sql.toString();
        }
        
        public String getDataLastYear() {
            StringBuilder sql = new StringBuilder(1000);
            String strDiscountTaxType = "";
            if(getTaxType() == TAX_TYPE_BLANK){
                // �Ŕ���
                strDiscountTaxType = " discount_detail_value_no_tax ";
            } else {

                // �ō���
                strDiscountTaxType = " discount_detail_value_in_tax ";
            }

            // ��S��
            sql.append("     /* ��S�����q�� */");
            sql.append("     (select count(DISTINCT ds1.slip_no)  ");
            sql.append("     FROM view_data_sales_detail_valid ds1 ");
            sql.append("     WHERE ds1.shop_id IN (" + getShopIDList() + ")");
            sql.append("     and ds1.sales_date between date" + getTermFrom() + " - interval '1 year' and date" + getTermTo()+" -interval '1 year'");
            sql.append("     and ds1.staff_id = ms.staff_id ");
            if(courseFlag) {
                sql.append("     and ds1.product_division IN (1,2,3,4,5,6) ");
            }else {
                sql.append("     and ds1.product_division IN (1,2,3,4) ");
            }
            //LVTu end edit 2016/02/04 New request #46728
            if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                sql.append(this.checkListCategory());
            }
            sql.append("     )  AS totalCount_last, ");
            
            sql.append("     (select count(DISTINCT ds1.slip_no)  ");
            sql.append("     FROM view_data_sales_detail_valid ds1 ");
            sql.append("     WHERE ds1.shop_id IN (" + getShopIDList() + ")");
            sql.append("     and ds1.sales_date between date" + getTermFrom() + " - interval '1 year' and date" + getTermTo()+" -interval '1 year'");
            sql.append("     and ds1.detail_staff_id = ms.staff_id ");
            sql.append("     and ds1.product_division IN (5,6) ");
            if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                sql.append(this.checkListCategory());
            }
            sql.append("     )  AS courseCount_last, ");
            sql.append("     /* ��S���Z�p�q�� */");
            sql.append("     (select count(DISTINCT ds1.slip_no)  ");
            sql.append("     FROM view_data_sales_detail_valid ds1 ");
            sql.append("     WHERE ds1.shop_id IN (" + getShopIDList() + ")");
            sql.append("     and ds1.sales_date between date" + getTermFrom() + " - interval '1 year' and date" + getTermTo()+" -interval '1 year'");
            sql.append("     and ds1.staff_id = ms.staff_id ");
            sql.append("     and ds1.product_division in (1,3) ");
            if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                sql.append(this.checkListCategory());
            }
            sql.append("     ) as techcount_last, ");
            //��S���Z�p����
            sql.append("     (select sum(" + strDiscountTaxType + ")  ");
            sql.append("     FROM view_data_sales_detail_valid ds1 ");
            sql.append("     WHERE ds1.shop_id IN (" + getShopIDList() + ")");
            sql.append("     and ds1.sales_date between date" + getTermFrom() + " - interval '1 year' and date" + getTermTo()+" -interval '1 year'");
            sql.append("     and ds1.staff_id = ms.staff_id ");
            sql.append("     and ds1.product_division in (1,3) ");
            if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                sql.append(this.checkListCategory());
            }
            sql.append("     ) AS techSales_last , ");
            
            sql.append("     coalesce((select sum(" + strDiscountTaxType + ")  ");
            sql.append("     FROM view_data_sales_detail_valid ds1 ");
            sql.append("     WHERE ds1.shop_id IN (" + getShopIDList() + ")");
            sql.append("     and ds1.sales_date between date" + getTermFrom() + " - interval '1 year' and date" + getTermTo()+" -interval '1 year'");
            sql.append("     and ds1.staff_id = ms.staff_id ");
            sql.append("     and ds1.product_division in (5) ");
            if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                sql.append(this.checkListCategory());
            }
            sql.append("     ),0) AS courseSales_last , ");
            
            sql.append("     coalesce((select sum(" + strDiscountTaxType + ")  ");
            sql.append("     FROM view_data_sales_detail_valid ds1 ");
            sql.append("     WHERE ds1.shop_id IN (" + getShopIDList() + ")");
            sql.append("     and ds1.sales_date between date" + getTermFrom() + " - interval '1 year' and date" + getTermTo()+" -interval '1 year'");
            sql.append("     and ds1.detail_staff_id = ms.staff_id ");
            sql.append("     and ds1.product_division in (6) ");
            if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                sql.append(this.checkListCategory());
            }
            sql.append("     ),0) AS course_digestionSales_last , ");

            sql.append("     /* ��S���w���q�� */");
            sql.append("     (select count(DISTINCT ds1.slip_no)  ");
            sql.append("     FROM view_data_sales_detail_valid ds1 ");
            sql.append("     WHERE ds1.shop_id IN (" + getShopIDList() + ")");
            sql.append("     and ds1.sales_date between date" + getTermFrom() + " - interval '1 year' and date" + getTermTo()+" -interval '1 year'");
            sql.append("     and ds1.staff_id = ms.staff_id ");
            sql.append("     and ds1.product_division in (1,3) ");
            sql.append("     and designated_flag = true ");
            if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                sql.append(this.checkListCategory());
            }
            sql.append("     ) as designatedCount_last, ");

            //��S���w������
            sql.append("     (select sum(" + strDiscountTaxType + ")  ");
            sql.append("     FROM view_data_sales_detail_valid ds1 ");
            sql.append("     WHERE ds1.shop_id IN (" + getShopIDList() + ")");
            sql.append("     and ds1.sales_date between date" + getTermFrom() + " - interval '1 year' and date" + getTermTo()+" -interval '1 year'");
            sql.append("     and ds1.staff_id = ms.staff_id ");
            sql.append("     and ds1.product_division in (1,3) ");
            sql.append("     and designated_flag = true ");
            if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                sql.append(this.checkListCategory());
            }
            sql.append("     ) as designatedSales_last, ");

            sql.append("     /* ��S���V�K�q�� */");
            if(isMutiShop){
                sql.append("     (select count(DISTINCT ds1.slip_no)  ");
                sql.append("     FROM view_data_sales_detail_valid ds1 ");
                sql.append("     LEFT JOIN mst_customer b using (customer_id) ");
                sql.append("     WHERE ds1.shop_id IN (" + getShopIDList() + ")");
                sql.append("     and ds1.sales_date between date" + getTermFrom() + " - interval '1 year' and date" + getTermTo()+" -interval '1 year'");
                sql.append("     and ds1.staff_id = ms.staff_id ");
                sql.append("     and ds1.product_division in (1,3) ");
                sql.append("     and get_visit_count(ds1.customer_id, ds1.shop_id, ds1.sales_date) = 1 ");
                sql.append("     and customer_no <> '0' ");
                if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                    sql.append(this.checkListCategory());
                }
                sql.append("     ) AS newcount_last,");
            }else{
                sql.append("     (select count(DISTINCT ds1.slip_no)  ");
                sql.append("     FROM view_data_sales_detail_valid ds1 ");
                sql.append("     LEFT JOIN mst_customer b using (customer_id) ");
                sql.append("     WHERE ds1.shop_id IN (" + getShopIDList() + ")");
                sql.append("     and ds1.sales_date between date" + getTermFrom() + " - interval '1 year' and date" + getTermTo()+" -interval '1 year'");
                sql.append("     and ds1.staff_id = ms.staff_id ");
                sql.append("     and ds1.product_division in (1,3) ");
                sql.append("     and get_visit_count(ds1.customer_id, ds1.shop_id, ds1.sales_date) = 1 ");
                sql.append("                AND ( ");
                sql.append("                        SELECT count(slip_no) + coalesce(max(before_visit_num), 0) ");
                sql.append("                        FROM data_sales ");
                sql.append("                        INNER JOIN mst_customer USING (customer_id) ");
                sql.append("                        WHERE customer_id = ds1.customer_id ");
                sql.append("                                AND data_sales.shop_id IN ( ");
                sql.append("                                    SELECT shop_id FROM mst_shop WHERE shop_id <> ds1.shop_id)");
                sql.append("                                AND data_sales.sales_date <= ds1.sales_date ");
                sql.append("                                AND data_sales.delete_date IS NULL ");
                sql.append("                  ) < 1 ");
                sql.append("                AND customer_no <> '0' ");
                if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                    sql.append(this.checkListCategory());
                }
                sql.append("       ) as newcount_last,");
            }
            sql.append("     /* ��S���V�K�Љ�q�� */");
            sql.append("     (select count(DISTINCT ds1.slip_no)  ");
            sql.append("     FROM view_data_sales_detail_valid ds1 ");
            sql.append("     LEFT JOIN mst_customer b using (customer_id) ");
            sql.append("     WHERE ds1.shop_id IN (" + getShopIDList() + ")");
            sql.append("     and ds1.sales_date between date" + getTermFrom() + " - interval '1 year' and date" + getTermTo()+" -interval '1 year'");
            sql.append("     and ds1.staff_id = ms.staff_id ");
            sql.append("     and ds1.product_division in (1,3) ");
            sql.append("     and get_visit_count(ds1.customer_id, ds1.shop_id, ds1.sales_date) = 1 ");
            sql.append("     AND customer_no <> '0' ");
            sql.append("     AND introducer_id IS NOT NULL ");
            if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                sql.append(this.checkListCategory());
            }
            sql.append("     ) as introduceCount_last, ");

            sql.append("     (select sum(" + strDiscountTaxType + ") ");
            sql.append("     FROM view_data_sales_detail_valid ds1 ");
            sql.append("     WHERE ds1.shop_id IN (" + getShopIDList() + ")");
            sql.append("     and ds1.sales_date between date" + getTermFrom() + " - interval '1 year' and date" + getTermTo()+" -interval '1 year'");
            sql.append("     and ds1.staff_id = ms.staff_id ");
            sql.append("     and ds1.product_division in (2,4) ");
            if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                sql.append(this.checkListCategory());
            }
            sql.append("     ) as itemSales_last ");

            sql.append("     /* �A�v���[�`�q�� */");
            sql.append("     ,0 as approachedCount_last");
            sql.append("     /* �A�v���[�`���� */");
            sql.append("     ,0 as approachedSales_last");

            sql.append( "      ,coalesce((select count(distinct customer_id) ");
            sql.append( "      from view_data_sales_detail_valid ds1 ");
            sql.append("     where ds1.sales_date between date" + getTermFrom() + " - interval '1 year' and date" + getTermTo()+" - interval '1 year'");
            sql.append("          and  ds1.shop_id in (" + getShopIDList() + ")");
            sql.append( "         and ds1.staff_id = ms.staff_id  ");

            if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                    sql.append(this.checkListCategory());
            }
            sql.append( "         And exists (select 1 from view_data_sales_detail_valid ds2 ");
            sql.append( "         where ds2.customer_id = ds1.customer_id ");
            if(isIsMutiShop()){
               sql.append( "      and ds2.shop_id in (" + getShopIDList() + ") ");
            }
            sql.append( "           AND ds2.staff_id = ds1.staff_id");
            if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                sql.append("      and   exists");
                sql.append("             (");
                //�Ƒԗ��p���邩���Ȃ����`�F�b�N
                sql.append("                 SELECT");
                sql.append("                     1");
                sql.append("                 FROM");
                sql.append("                     data_sales_detail dsd");
                sql.append("     INNER JOIN data_sales_mainstaff dsmt ON dsmt.shop_id = dsd.shop_id AND dsmt.slip_no = dsd.slip_no ");
                sql.append("                     inner join mst_technic mst on mst.technic_id = dsd.product_id ");
                sql.append("                     left join mst_technic_class mstc on mstc.technic_class_id = mst.technic_class_id ");
                sql.append("                 WHERE");
                sql.append("                         dsd.shop_id = ds2.shop_id");
                sql.append("                     AND dsd.slip_no = ds2.slip_no");
                sql.append("                     and dsd.slip_detail_no = ds2.slip_detail_no   ");
                sql.append("                     AND dsd.delete_date is null");
                sql.append("                     AND dsd.product_division in (1,3)");
                //�ƑԑI���̏ꍇ
                sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");                
                sql.append("                  UNION ALL  ");
                sql.append("                  SELECT ");
                sql.append("                     1");
                sql.append("                 FROM");
                sql.append("                      data_sales_detail dsd");
                sql.append("     INNER JOIN data_sales_mainstaff dsmt ON dsmt.shop_id = dsd.shop_id AND dsmt.slip_no = dsd.slip_no ");
                sql.append("                     inner join mst_item mi on dsd.product_id = mi.item_id ");
                sql.append("                     left join mst_item_class mic on mic.item_class_id = mi.item_class_id ");
                sql.append("                  WHERE");
                sql.append("                          dsd.shop_id = ds2.shop_id");
                sql.append("                      AND dsd.slip_no = ds2.slip_no");
                sql.append("                     and dsd.slip_detail_no = ds2.slip_detail_no   ");
                sql.append("                      AND dsd.delete_date is null");
                sql.append("                      AND dsd.product_division in (2,4)");
                //�ƑԑI���̏ꍇ
                sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");
                sql.append("                 UNION ALL  ");
                sql.append("                 SELECT ");
                sql.append("                     1");
                sql.append("                 FROM");
                sql.append("                     data_sales_detail dsd");
                sql.append("     INNER JOIN data_sales_mainstaff dsmt ON dsmt.shop_id = dsd.shop_id AND dsmt.slip_no = dsd.slip_no ");
                sql.append("                     inner join mst_course msc on msc.course_id = dsd.product_id ");
                sql.append("                     left join mst_course_class mscc on mscc.course_class_id = msc.course_class_id ");
                sql.append("                 WHERE");
                sql.append("                         dsd.shop_id = ds2.shop_id");
                sql.append("                     AND dsd.slip_no = ds2.slip_no");
                sql.append("                     and dsd.slip_detail_no = ds2.slip_detail_no   ");
                sql.append("                     AND dsd.delete_date is null");
                sql.append("                     AND dsd.product_division in (5,6)");
                //�ƑԑI���̏ꍇ
                sql.append("                  AND mscc.shop_category_id in (" + this.getListCategoryId() + ") ");
                sql.append(" ) ");
            }
            sql.append( "       and ds2.sales_date < to_date("+getTermFrom()+",'YYYY/MM/dd') - interval '1 year' ");
            sql.append( "    	and ds2.sales_date >= to_date("+getTermFrom()+",'YYYY/MM/dd') - interval '"+getCountDate()+"' - interval '1 year' ");

           sql.append( "        )");
           sql.append( "        ),0)  as repeat_count_last ");

            sql.append(" ,coalesce(( ");
            sql.append("        SELECT count(DISTINCT (ds1.customer_id)) ");
            sql.append("        FROM view_data_sales_detail_valid ds1 ");
            sql.append("        WHERE ds1.sales_date < to_date("+getTermFrom()+",'YYYY/MM/dd') - interval '1 year' ");
            sql.append("        AND ds1.sales_date >= to_date("+getTermFrom()+",'YYYY/MM/dd') - interval '"+getCountDate()+"' - interval '1 year' ");
            if(isIsMutiShop()){
                sql.append("     AND ds1.shop_id in (" + getShopIDList() + ") ");
            }
            sql.append("  and ds1.staff_id = t.staff_id ");
            if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                sql.append(this.checkListCategory());
            }
            sql.append(" ),0) AS repeat_total_last ");
            
            return sql.toString();
        }
        //IVS_LVTu end add 2015/08/07 Bug #41292
        //LVTu end edit 2016/01/19 New request #46728
}
