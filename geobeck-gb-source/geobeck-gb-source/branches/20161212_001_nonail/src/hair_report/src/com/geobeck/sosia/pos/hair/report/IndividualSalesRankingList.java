/*
 * IndividualSalesRankingList.java
 *
 * Created on 2013/01/23, 11:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report;

import java.util.*;
import java.util.logging.*;
import java.math.BigDecimal;
import java.text.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.system.SystemInfo;

/**
 * �X�܃����L���O�ꗗ����
 * @author IVS_tttung
 */
public class IndividualSalesRankingList extends ArrayList<IndividualSalesRanKing>
{
        //�ŋ敪
        private final	int	TAX_TYPE_BLANK				=	1;      //�Ŕ�
	private final	int	TAX_TYPE_UNIT				=	2;      //�ō�
        //�\���͈�
	private final	int	RANGE_DISPLAY_TO_10                     =	0;	//�`10��
	private final	int	RANGE_DISPLAY_TO_20                     =	1;	//�`20��
	private final	int	RANGE_DISPLAY_ALL                       =	2;	//�S��

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
        /**
	 * �S����ID
	 */
	private	Integer	staffID                 =   null;
	
	/**
         * Creates a new instance of IndividualSalesRankingList
         */
	public IndividualSalesRankingList()
	{
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
        /**
	 * �S����ID���擾����B
	 * @return �S����ID
	 */
        public Integer getStaffID() {
                return staffID;
        }
	/**
         * �S����ID���Z�b�g����B
         * @param staffID �S����ID
         */
        public void setStaffID(Integer staffID) {
                this.staffID = staffID;
        }
    /**
     * �f�[�^��ǂݍ��ށB
     */
	public void load()
	{
		try
		{
                    //�����L���O���X�g�擾
                    this.getIndividualSalesRankingList();
		}
		catch(Exception e)
		{
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * �X�܃����L���O�̃��X�g���擾����B
	 * @exception Exception
	 */
	private void getIndividualSalesRankingList() throws Exception
	{
		this.clear();
		
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(this.getSelectIndividualSalesRankingSQL());

		while(rs.next())
		{
                    IndividualSalesRanKing temp = new IndividualSalesRanKing();
                    temp.setData(rs);
                    this.add(temp);
		}              
                // �Z�p�V�K�䗦
                for(IndividualSalesRanKing temp : this) {
                    temp.setTotalSales(temp.getTechSum() + temp.getNominaSum() + temp.getItemPrice() + temp.getClaimSum() + temp.getMisum() + temp.getMcsum() + temp.getConsumpsum());
                }
		rs.close();
	}
	
	/**
	 * �X�܃����L���O���o�pSQL���擾����B
	 * @return �X�܃����L���O���o�pSQL
	 * @exception Exception
	 */
	private String getSelectIndividualSalesRankingSQL() throws Exception
	{   
            NumberFormat format = NumberFormat.getInstance(Locale.US);
            Number gross_margin_rate = null;
            Number spread_margin_rate = null;
            
            ConnectionWrapper con = SystemInfo.getConnection();
            String SQL = " select gross_margin_rate from mst_account_setting ";
            ResultSetWrapper rs = con.executeQuery(SQL);
            while (rs.next()) {
                gross_margin_rate = format.parse(rs.getString(1));///
            }
            
            SQL = " select spread_margin_rate from mst_account_setting ";
            rs = con.executeQuery(SQL);
            while (rs.next()) {
                spread_margin_rate = format.parse(rs.getString(1));///
            }
            
           StringBuilder sql = new StringBuilder(1000);
            sql.append("     select");
            sql.append("     	 b.staff_id");
            sql.append("     	,max(case when b.staff_id is null then '�S���Ȃ�' else b.staff_name1 || '�@' || b.staff_name2 end) as StaffName ");
            if (getTaxType() == TAX_TYPE_BLANK) {
                // �Ŕ���
                sql.append("     		 ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 1 and detail_designated_flag = FALSE then discount_detail_value_no_tax * ( " + gross_margin_rate + " ) else 0 end)) as Hodocojuts_value ");
                sql.append("     		 ,count(distinct case when a.product_division = 1 and detail_designated_flag = FALSE then a.slip_no end) as Hodocojuts_Num ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 1 and detail_designated_flag = TRUE then discount_detail_value_no_tax * ( " + gross_margin_rate + " ) else 0 end)) as Nomination_value ");
                sql.append("     		 ,count(distinct case when a.product_division = 1 and detail_designated_flag = TRUE then a.slip_no end) as Nomination_Num ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 2 then discount_detail_value_no_tax * ( " + gross_margin_rate + " - " + spread_margin_rate + " ) else 0 end)) as sum_item_value ");
                //sql.append("     		 ,ROUND(sum(case when a.product_division = 2 then discount_detail_value_no_tax * ( " + gross_margin_rate + " ) else 0 end)) as sum_item_value ");
                sql.append("     		 ,count(case when a.product_division = 2 then discount_detail_value_no_tax else 0 end) as count_item_value ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 3 then discount_detail_value_no_tax * ( " + gross_margin_rate + " )  else 0 end)) as ClaimValue  ");
                sql.append("     		 ,count (distinct case when a.product_division = 3 then a.slip_no else null end) as ClaimCount ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 4 then discount_detail_value_no_tax * ( " + gross_margin_rate + " - " + spread_margin_rate + " ) else 0 end)) as Misum ");
                //sql.append("     		 ,ROUND(sum(case when a.product_division = 4 then discount_detail_value_no_tax * ( " + gross_margin_rate + " ) else 0 end)) as Misum ");
                sql.append("     		 ,count (distinct case when a.product_division = 4 then a.slip_no else null end) as Micount ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 5 then discount_detail_value_no_tax * ( " + spread_margin_rate + " ) else 0 end)) as Mcsum ");
                sql.append("     		 ,count (distinct case when a.product_division = 5 then a.slip_no else null end) as Mccount ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 6 then discount_detail_value_no_tax * ( 1 - ( " + spread_margin_rate + " )) else 0 end)) as Consumpsum ");
                sql.append("     		 ,count (distinct case when a.product_division = 6 then a.slip_no else null end) as Consumpcount ");

            } else {
                // �ō���
                sql.append("     		 ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 1 and detail_designated_flag = FALSE then discount_detail_value_in_tax * ( " + gross_margin_rate + " ) else 0 end)) as Hodocojuts_value ");
                sql.append("     		 ,count(distinct case when a.product_division = 1 and detail_designated_flag = FALSE then a.slip_no end) as Hodocojuts_Num ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 1 and detail_designated_flag = TRUE then discount_detail_value_in_tax * ( " + gross_margin_rate + " ) else 0 end)) as Nomination_value ");
                sql.append("     		 ,count(distinct case when a.product_division = 1 and detail_designated_flag = TRUE then a.slip_no end) as Nomination_Num ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 2 then discount_detail_value_in_tax * ( " + gross_margin_rate + " - " + spread_margin_rate + " ) else 0 end)) as sum_item_value ");
                //sql.append("     		 ,ROUND(sum(case when a.product_division = 2 then discount_detail_value_in_tax * ( " + gross_margin_rate + " ) else 0 end)) as sum_item_value ");
                sql.append("     		 ,count(case when a.product_division = 2 then discount_detail_value_no_tax else 0 end) as count_item_value ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 3 then discount_detail_value_in_tax * ( " + gross_margin_rate + " )  else 0 end)) as ClaimValue  ");
                sql.append("     		 ,count (distinct case when a.product_division = 3 then a.slip_no else null end) as ClaimCount ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 4 then discount_detail_value_no_tax * ( " + gross_margin_rate + " - " + spread_margin_rate + " ) else 0 end)) as Misum ");
                //sql.append("     		 ,ROUND(sum(case when a.product_division = 4 then discount_detail_value_no_tax * ( " + gross_margin_rate + " ) else 0 end)) as Misum ");
                sql.append("     		 ,count (distinct case when a.product_division = 4 then a.slip_no else null end) as Micount ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 5 then discount_detail_value_in_tax * ( " + spread_margin_rate + " ) else 0 end)) as Mcsum ");
                sql.append("     		 ,count (distinct case when a.product_division = 5 then a.slip_no else null end) as Mccount ");
                sql.append("     		 ,ROUND(sum(case when a.product_division = 6 then discount_detail_value_in_tax * ( 1 - ( " + spread_margin_rate + " )) else 0 end)) as Consumpsum ");
                sql.append("     		 ,count (distinct case when a.product_division = 6 then a.slip_no else null end) as Consumpcount ");
            }
            sql.append("         from");
            sql.append("             view_data_sales_detail_valid a");
            sql.append("             join mst_staff b ");
            sql.append("             on a.detail_staff_id = b.staff_id ");
            sql.append("     ");
            sql.append("     ");
            sql.append("         where");
            //sql.append("             a.shop_id in (0, 1, 2)   and");
            sql.append("             a.sales_date between " + getTermFrom() + " and " + getTermTo());
            if (getStaffID() != null) {
                sql.append(" and b.staff_id = " + getStaffID().intValue());
            }
            sql.append("         group by");
            sql.append("              b.staff_id");
            sql.append("         order by");
            switch (getOrderDisplay()) {
                case 0:
                    sql.append("  Hodocojuts_value desc ");
                    break;
                case 1:
                    sql.append("  Hodocojuts_Num desc ");
                    break;
                case 2:
                    sql.append("  Nomination_value desc ");
                    break;
                case 3:
                    sql.append("  Nomination_Num desc ");
                    break;
                case 4:
                    sql.append("  sum_item_value desc ");
                    break;
                case 5:
                    sql.append("  count_item_value desc ");
                    break;
                case 6:
                    sql.append("  ClaimValue desc ");
                    break;
                case 7:
                    sql.append("  ClaimCount desc ");
                    break;
                case 8:
                    sql.append("  Misum desc ");
                    break;
                case 9:
                    sql.append("  Micount desc ");
                    break;
                case 10:
                    sql.append("  Mcsum desc ");
                    break;
                case 11:
                    sql.append("  Mccount desc ");
                    break;
                case 12:
                    sql.append("  Consumpsum desc ");
                    break;
                case 13:
                    sql.append("  Consumpcount desc ");
                    break;
                default:
                    sql.append("  Hodocojuts_value desc");
                    break;
            }
            sql.append("             ,max(case when b.display_seq is null then 1 else 0 end) ");
            sql.append("             ,max(b.display_seq) ");
            sql.append("             ,max(lpad(b.staff_no, 10, '0')) ");
            sql.append("             ,b.staff_id desc");

            //�\���͈�
            if (getRangeDisplay() == RANGE_DISPLAY_TO_10) {
                //�`10��
                sql.append(" limit 10");
            } else if (getRangeDisplay() == RANGE_DISPLAY_TO_20) {
                //�`20��
                sql.append(" limit 20");
            }
            return sql.toString();
	}
}
