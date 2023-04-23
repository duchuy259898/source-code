/*
 * CustomerRankingList.java
 *
 * Created on 2008/07/20, 22:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report;

import com.geobeck.sosia.pos.master.company.MstShopSetting;
import java.util.*;
import java.util.logging.*;
import java.math.BigDecimal;
import java.text.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.system.SystemInfo;

/**
 * �X�^�b�t�����L���O�ꗗ�擾
 * �N���X���͈ꗗ�擾
 * @author saito
 */
public class CustomerRanking5List extends ArrayList<CustomerRanking>
{
        //�ŋ敪
        private final	int	TAX_TYPE_BLANK		=   1;	//�Ŕ�
	private final	int	TAX_TYPE_UNIT		=   2;	//�ō�

        private long limitCount = 0l;
        
        /**
	 * �X��ID���X�g
	 */
	private	String   shopIDList             =   null;
	/**
	 * �S����ID
	 */
	private	Integer	staffID                 =   null;
        /**
	 * �Ώۊ���(�J�n��)
	 */
	private	GregorianCalendar   termFrom    =   new GregorianCalendar();
	/**
	 * �Ώۊ���(�I����)
	 */
	private	GregorianCalendar   termTo      =   new GregorianCalendar();
        
        private boolean isPastTotal            =  false;
	/**
	 * �ŋ敪
	 */
	private	Integer	taxType                 =   null;
	/**
	 * �]���Ώ�
	 */
	private	Integer	productDivision         =   null;
	
	/**
	 * ���X�񐔐ݒ�|�����N�T
	 */
        private long rankF5 = 0l;
        /**
	 * ���X�񐔐ݒ�|�����N�S
	 */
        private long rankF4 = 0l;
        /**
	 * ���X�񐔐ݒ�|�����N�R
	 */
        private long rankF3 = 0l;
	/**
	 * ���X�񐔐ݒ�|�����N�Q
	 */
        private long rankF2 = 0l;
	/**
	 * ���X�񐔐ݒ�|�����N�P
	 */
        private long rankF1 = 0l;
	/**
	 * ������z�ݒ�|�����N�T
	 */
        private long rankM5 = 0l;
        /**
	 * ������z�ݒ�|�����N�S
	 */
        private long rankM4 = 0l;
        /**
	 * ������z�ݒ�|�����N�R
	 */
        private long rankM3 = 0l;
	/**
	 * ������z�ݒ�|�����N�Q
	 */
        private long rankM2 = 0l;
	/**
	 * ������z�ݒ�|�����N�P
	 */
        private long rankM1 = 0l;
        /**
	 * �Љ�l���ݒ�|�����N�T
	 */
        private long rankI5 = 0l;
        /**
	 * �Љ�l���ݒ�|�����N�S
	 */
        private long rankI4 = 0l;
        /**
	 * �Љ�l���ݒ�|�����N�R
	 */
        private long rankI3 = 0l;
	/**
	 * �Љ�l���ݒ�|�����N�Q
	 */
        private long rankI2 = 0l;
	/**
	 * �Љ�l���ݒ�|�����N�P
	 */
        private long rankI1 = 0l;
        /**
	 * �q�P���ݒ�|�����N�T
	 */
        private long rankA5 = 0l;
        /**
	 * �q�P���ݒ�|�����N�S
	 */
        private long rankA4 = 0l;
        /**
	 * �q�P���ݒ�|�����N�R
	 */
        private long rankA3 = 0l;
	/**
	 * �q�P���ݒ�|�����N�Q
	 */
        private long rankA2 = 0l;
	/**
	 * �q�P���ݒ�|�����N�P
	 */
        private long rankA1 = 0l;
        /**
	 * �����N�e�T�l�T����
	 */
        private String rankF5M5Name = "VIP�q";
        /**
	 * �����N�e�T�l�S����
	 */
        private String rankF5M4Name = "VIP�q";
        /**
	 * �����N�e�T�l�R����
	 */
        private String rankF5M3Name = "�S�[���h�q";
        /**
	 * �����N�e�T�l�Q����
	 */
        private String rankF5M2Name = "�V���o�[�q";
        /**
	 * �����N�e�T�l�P����
	 */
        private String rankF5M1Name = "��ʋq";
	/**
	 * �����N�e�S�l�T����
	 */
        private String rankF4M5Name = "VIP�q";
        /**
	 * �����N�e�S�l�S����
	 */
        private String rankF4M4Name = "�S�[���h�q";
        /**
	 * �����N�e�S�l�R����
	 */
        private String rankF4M3Name = "�S�[���h�q";
        /**
	 * �����N�e�S�l�Q����
	 */
        private String rankF4M2Name = "�V���o�[�q";
        /**
	 * �����N�e�S�l�P����
	 */
        private String rankF4M1Name = "��ʋq";
        /**
	 * �����N�e�R�l�T����
	 */
        private String rankF3M5Name = "�S�[���h�q";
        /**
	 * �����N�e�R�l�S����
	 */
        private String rankF3M4Name = "�S�[���h�q";
        /**
	 * �����N�e�R�l�R����
	 */
        private String rankF3M3Name = "�S�[���h�q";
        /**
	 * �����N�e�R�l�Q����
	 */
        private String rankF3M2Name = "�V���o�[�q";
        /**
	 * �����N�e�R�l�P����
	 */
        private String rankF3M1Name = "��ʋq";
        /**
	 * �����N�e�Q�l�T����
	 */
        private String rankF2M5Name = "�V���o�[�q";
        /**
	 * �����N�e�Q�l�S����
	 */
        private String rankF2M4Name = "�V���o�[�q";
        /**
	 * �����N�e�Q�l�R����
	 */
        private String rankF2M3Name = "�V���o�[�q";
        /**
	 * �����N�e�Q�l�Q����
	 */
        private String rankF2M2Name = "�V���o�[�q";
        /**
	 * �����N�e�Q�l�P����
	 */
        private String rankF2M1Name = "��ʋq";
        /**
	 * �����N�e�P�l�T����
	 */
        private String rankF1M5Name = "�V�K�܂ވ�ʋq";
        /**
	 * �����N�e�P�l�S����
	 */
        private String rankF1M4Name = "�V�K�܂ވ�ʋq";
        /**
	 * �����N�e�P�l�R����
	 */
        private String rankF1M3Name = "�V�K�܂ވ�ʋq";
        /**
	 * �����N�e�P�l�Q����
	 */
        private String rankF1M2Name = "�V�K�܂ވ�ʋq";
        /**
	 * �����N�e�P�l�P����
	 */
        private String rankF1M1Name = "�V�K�܂ވ�ʋq";
        /**
	 * �ғ��q
	 */
        private boolean chkValid1 = false;
	/**
	 * �����q
	 */
        private boolean chkValid2 = false;
	/**
	 * ���X�q
	 */
        private boolean chkValid3 = false;
        /**
	 * ���X��
	 */
        private boolean chkCondition1 = false;
        /**
	 * ������z
	 */
        private boolean chkCondition2 = false;
        /**
	 * �Љ�ҋq��
	 */
        private boolean chkCondition3 = false;
        /**
	 * �q�P��
	 */
        private boolean chkCondition4 = false;
	/**
         * Creates a new instance of CustomerRankingList
         */
	public CustomerRanking5List()
	{
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
	 * �]���Ώۂ��擾����B
	 * @return �]���Ώ�
	 */
        public int getProductDivision() {
                return productDivision;
        }
	/**
	 * �]���Ώۂ��Z�b�g����B
	 * @param productDivision �]���Ώ�
	 */
        public void setProductDivision(int productDivision) {
                this.productDivision = productDivision;
        }


        /**
         * �f�V�����̓f�[�^��ǂݍ��ށB
         */
	public void load_CustomerRanking()
	{
		try
		{
                    //�Ώۃf�[�^�����[�N�e�[�u���Ɋi�[
                    this.getTargetList(false);

                    //�����L���O���X�g�擾
                    this.getCustomerRankingList();
		}
		catch(Exception e)
		{
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

        /**
         * �N���X���̓f�[�^��ǂݍ��ށB
         */
	public void load_CrossAnalysis()
	{
		try
		{
                    //�Ώۃf�[�^�����[�N�e�[�u���Ɋi�[
                    this.getTargetList(true);

                    //�N���X���̓��X�g�擾
                    this.getCrossAnalysisList();
		}
		catch(Exception e)
		{
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
        
	/**
	 * �Ώۃf�[�^�����[�N�e�[�u���Ɋi�[
	 * @exception Exception
	 */
	private void getTargetList(boolean isCrossAnalysis) throws Exception
	{
                ConnectionWrapper con = SystemInfo.getConnection();

                try {
                    con.execute("drop table wk_rank");
                } catch (Exception e) {
                }

                StringBuilder sql = new StringBuilder(1000);
                
                sql.append(" create temporary table wk_rank as");

                sql.append(" select");
                sql.append("      a.customer_id");
                sql.append("     ,max(coalesce(b.customer_no, '')) as customer_no");
                sql.append("     ,max(coalesce(b.customer_name1, '') || '�@' || coalesce(b.customer_name2, '')) as customer_name");
                sql.append("     ,max(coalesce(b.pc_mail_address, '')) as pc_mail_address");
                sql.append("     ,max(coalesce(b.cellular_mail_address, '')) as cellular_mail_address");
                sql.append("     ,max(a.sales_date) as sales_date");
                
                // ���X��
                if (isPastTotal) {
                    
                    // �ߋ��ݐς��܂ޏꍇ
                    sql.append("     ,(");
                    sql.append("         select");
                    sql.append("             count(ds.slip_no) + coalesce(max(mc.before_visit_num), 0)");
                    sql.append("         from");
                    sql.append("             data_sales ds");
                    sql.append("                 inner join mst_customer mc");
                    sql.append("                 using(customer_id)");
                    sql.append("         where");
                    sql.append("                 ds.customer_id = a.customer_id");
                    sql.append("             and ds.shop_id in (" + getShopIDList() + ")");
                    sql.append("             and ds.sales_date is not null");
                    sql.append("             and ds.delete_date is null");
                    sql.append("     ) as visit_count");
                    
                } else {
                    
                    sql.append("     ,count(distinct slip_no) as visit_count");
                }
                
                sql.append("     ,(");
                sql.append("         select");
                sql.append("             ms.staff_name1 || '�@' || ms.staff_name2");
                sql.append("         from");
                sql.append("             data_sales ds");
                sql.append("                 join mst_staff ms");
                sql.append("                 using(staff_id)");
                sql.append("         where");
                sql.append("                 ds.customer_id = a.customer_id");
                sql.append("             and ds.shop_id in (" + getShopIDList() + ")");
                sql.append("             and ds.sales_date = max(a.sales_date)");
                sql.append("             and ds.delete_date is null");
                sql.append("         order by");
                sql.append("              ds.insert_date desc");
                sql.append("             ,ds.slip_no desc");
                sql.append("         limit 1");
                sql.append("      ) as staff_name");
                sql.append("     ,(");
                sql.append("         select");
                sql.append("             designated_flag");
                sql.append("         from");
                sql.append("             data_sales");
                sql.append("         where");
                sql.append("                 customer_id = a.customer_id");
                sql.append("             and shop_id in (" + getShopIDList() + ")");
                sql.append("             and sales_date = max(a.sales_date)");
                sql.append("             and delete_date is null");
                sql.append("         order by");
                sql.append("              insert_date desc");
                sql.append("             ,slip_no desc");
                sql.append("         limit 1");
                sql.append("      ) as designated_flag");

                //�Љ�q��
                if( isPastTotal && SystemInfo.getSetteing().isShareCustomer()){

                    //�ߋ��ݐς��܂ށA�ڋq���L����̏ꍇ
                    sql.append("    ,(");
                    sql.append("        select count(c.customer_id)");
                    sql.append("         from");
                    sql.append("             mst_customer c ");
                    sql.append("        where");
                    sql.append("                c.introducer_id = a.customer_id");
                    sql.append("        ) as introducer_cnt");

                }else{

                    sql.append("    ,(");
                    sql.append("        select count(sc.customer_id)");
                    sql.append("         from");
                    sql.append("             mst_customer c");
                    sql.append("             ,(select customer_id");
                    sql.append("                 from data_sales");
                    sql.append("                where");
                    if( ! isPastTotal){
                        sql.append("                    sales_date between " + getTermFrom() + " and " + getTermTo());
                    }
                    if ( ! SystemInfo.getSetteing().isShareCustomer()){
                        if( ! isPastTotal){
                            sql.append("                and shop_id in("+ getShopIDList() + ")");
                        }else{
                            sql.append("                    shop_id in("+ getShopIDList() + ")");
                        }
                    }
                    sql.append("                 group by customer_id");
                    sql.append("               )as sc");
                    sql.append("        where");
                    sql.append("                c.introducer_id = a.customer_id");
                    sql.append("            and c.customer_id = sc.customer_id");
                    sql.append("            and c.customer_no <> '0'");
                    sql.append("        ) as introducer_cnt");

                }

                if (getProductDivision() == 0) {

                    //---------------------------------
                    //�]���Ώہ@�S��
                    //---------------------------------
                    String salesValueColumn = "";
                    if (getTaxType() == TAX_TYPE_BLANK){
                        //�S�̊�����Ŕ������z
                        salesValueColumn = "a.discount_sales_value_no_tax";
                    } else {
                        //�S�̊�����ō��݋��z
                        salesValueColumn = "a.discount_sales_value_in_tax";
                    }

                    // ������z
                    if (isPastTotal) {

                        // �ߋ��ݐς��܂ޏꍇ
                        sql.append("     ,(");
                        sql.append("         select");
                        sql.append("             sum(discount_sales_value_in_tax)");
                        sql.append("         from");
                        sql.append("             view_data_sales_valid");
                        sql.append("         where");
                        sql.append("                 shop_id in (" + getShopIDList() + ")");
                        sql.append("             and customer_id = a.customer_id");
                        sql.append("             and sales_date is not null");
                        sql.append("     ) as sales_value");

                    } else {

                        sql.append(" ,sum(" + salesValueColumn + ") as sales_value");
                    }

                    sql.append(" from");
                    sql.append("     view_data_sales_valid a");
                    sql.append("         join mst_customer b using (customer_id)");
                    sql.append(" where");
                    sql.append("         a.shop_id in (" + getShopIDList() + ")");
                    if (getStaffID() != null) {
                        sql.append(" and a.staff_id = " + getStaffID().intValue());
                    }
                    sql.append("     and a.sales_date between " + getTermFrom() + " and " + getTermTo());
                    sql.append("     and b.customer_no <> '0'");

                } else {
                    
                    //---------------------------------
                    //�]���Ώہ@�Z�p�܂��͏��i
                    //---------------------------------

                    /*
                    if (getTaxType() == TAX_TYPE_BLANK){
                        //������Ŕ������z
                        sql.append(" ,sum(discount_detail_value_no_tax) as sales_value");
                    } else {
                        //������ō��݋��z
                        sql.append(" ,sum(discount_detail_value_in_tax) as sales_value");
                    }
                     *
                     */
                    String salesValueColumn = "";
                    if (getTaxType() == TAX_TYPE_BLANK){
                        //������Ŕ������z
                        salesValueColumn = "discount_detail_value_no_tax";
                    } else {
                        //������ō��݋��z
                        salesValueColumn = "discount_detail_value_in_tax";
                    }
                    // ������z
                    if (isPastTotal) {

                        // �ߋ��ݐς��܂ޏꍇ
                        sql.append("     ,(");
                        sql.append("         select");
                        sql.append("             sum(" + salesValueColumn + ")");
                        sql.append("         from");
                        sql.append("             view_data_sales_detail_valid");
                        sql.append("         where");
                        sql.append("                 shop_id in (" + getShopIDList() + ")");
                        sql.append("             and customer_id = a.customer_id");
                        sql.append("             and sales_date is not null");
                        if (getProductDivision() == 1) {
                            //�Z�p�E�Z�p�N���[��
                            sql.append("             and product_division in (1,3)");
                        } else {
                            //���i�E���i�ԕi
                            sql.append("             and product_division in (2,4)");
                        }
                        sql.append("     ) as sales_value");

                    } else {

                        sql.append(" ,sum(" + salesValueColumn + ") as sales_value");
                    }

                    sql.append(" from");
                    sql.append("     view_data_sales_detail_valid a");
                    sql.append("         join mst_customer b using (customer_id)");
                    sql.append(" where");
                    sql.append("         a.shop_id in (" + getShopIDList() + ")");
                    if (getStaffID() != null) {
                        sql.append(" and a.staff_id = " + getStaffID().intValue());
                    }
                    sql.append("     and a.sales_date between " + getTermFrom() + " and " + getTermTo());
                    sql.append("     and b.customer_no <> '0'");
                    
                    if (getProductDivision() == 1) {
                        //�Z�p�E�Z�p�N���[��
                        sql.append(" and a.product_division in (1,3)");
                    } else {
                        //���i�E���i�ԕi
                        sql.append(" and a.product_division in (2,4)");
                    }
                }

                sql.append(" group by");
                sql.append("     a.customer_id");
                
                //if (isCrossAnalysis && getProductDivision() == 0) {
                if (isCrossAnalysis ) {

                    String visitCycle = "((extract(month from age(" + getTermTo() + "::date, " + getTermFrom() + "::date)) + 1) / count(*))";
                    
                    sql.append(" having");
                    sql.append("     (");
                    sql.append("         false");

                    MstShopSetting mss = MstShopSetting.getInstance();

                    if (isChkValid1()) {
                        sql.append(" or " + visitCycle + " <= " + mss.getValidCustomerPeriod1());
                    }

                    if (isChkValid2()) {
                        sql.append(" or (");
                        sql.append(visitCycle + " > " + mss.getValidCustomerPeriod1() + " and ");
                        sql.append(visitCycle + " <= " + mss.getValidCustomerPeriod2());
                        sql.append(" )");
                    }

                    if (isChkValid3()) {
                        sql.append(" or " + visitCycle + " > " + (mss.getValidCustomerPeriod2()));
                    }
                    sql.append("     )");
                }
                
                sql.append(" order by");
                sql.append("     sales_value desc");
                
                con.execute(sql.toString());
	}
        
	/**
	 * �X�^�b�t�����L���O�̃��X�g���擾����B
	 * @exception Exception
	 */
	private void getCustomerRankingList() throws Exception
	{
		this.clear();

                ConnectionWrapper con = SystemInfo.getConnection();
		ResultSetWrapper rs = null;
                
                //���[�N�e�[�u���̌����擾
                long totalCount = 0;
		rs = con.executeQuery("select count(*) as cnt from wk_rank");
		while (rs.next()) {
                    totalCount = rs.getLong("cnt");
		}
                
                if (totalCount == 0) return;

                BigDecimal bd = null;
                bd = new BigDecimal(totalCount / 10d);
                this.setLimitCount(bd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue());
                
		rs = con.executeQuery(this.getSelectCustomerRankingSQL(getLimitCount()));

                double salesTotal = 0d;
                double subTotal = 0d;
                
		while (rs.next()) {
                    CustomerRanking temp = new CustomerRanking();
                    temp.setData(rs);

                    salesTotal += rs.getLong("sales_value");

                    this.add(temp);
		}

                NumberFormat nf = NumberFormat.getInstance();
                nf.setMinimumFractionDigits(1);
                
                for(CustomerRanking temp : this) {

                    if (temp.getTargetCount().doubleValue() > 0) {
                        // �q�P��
                        bd = new BigDecimal(temp.getSalesValue().doubleValue() / temp.getTargetCount().doubleValue());
                        temp.setAvgUnitPrice(bd.setScale(0, BigDecimal.ROUND_HALF_UP).longValue());

                        // ���ϗ��X��
                        bd = new BigDecimal(temp.getVisitCount().doubleValue() / temp.getTargetCount().doubleValue());
                        temp.setAvgVisitCount(bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
                    }
                    
                    if (temp.getAvgVisitCount().doubleValue() > 0) {
                        // ���ϗ��X�P��
                        bd = new BigDecimal(temp.getAvgUnitPrice().doubleValue() / temp.getAvgVisitCount().doubleValue());
                        temp.setAvgVisitPrice(bd.setScale(1, BigDecimal.ROUND_HALF_UP).longValue());
                    }

                    if (salesTotal > 0) {
                        // ����V�F�A��
                        bd = new BigDecimal(temp.getSalesValue() / salesTotal * 100);
                        temp.setSalesShareRate(nf.format(bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()) + "%");

                        // �݌v����V�F�A��
                        subTotal += temp.getSalesValue();
                        bd = new BigDecimal(subTotal / salesTotal * 100);
                        temp.setTotalSalesShareRate(nf.format(bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()) + "%");
                    }
                }
                
		rs.close();
	}
	
	/**
	 * �X�^�b�t�����L���O���o�pSQL���擾����B
	 * @return �X�^�b�t�����L���O���o�pSQL
	 * @exception Exception
	 */
	private String getSelectCustomerRankingSQL(long limitCount) throws Exception
	{
	    StringBuilder sql = new StringBuilder(1000);
            
            for (int i = 0; i < 10; i++) {

                long offset = 0;
                if (i > 0) {
                    offset = (i * limitCount);
                }
                sql.append(" select");
                sql.append("      count(customer_id) as customer_count");
                sql.append("     ,sum(visit_count) as visit_count");
                sql.append("     ,sum(sales_value) as sales_value");
                sql.append(" from");
                sql.append("     (");
                sql.append("         select * from wk_rank");
                sql.append("         offset " + offset);
                if (i < 9) {
                    sql.append("     limit " + limitCount);
                }
                sql.append("     ) t");
                if (i < 9) {
                    sql.append(" union all");
                }
            }

            return sql.toString();
	}

	/**
	 * �N���X���͂̃��X�g���擾����B
	 * @exception Exception
	 */
	private void getCrossAnalysisList() throws Exception
	{
            this.clear();

            ConnectionWrapper con = SystemInfo.getConnection();
            ResultSetWrapper rs = null;

            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            if( isChkCondition1() && isChkCondition2()){   // ���X��,������z
                sql.append("      sum(case when visit_count >= " + getRankF5() + " and sales_value >= " + getRankM5() + " then 1 else 0 end) as f5m5");
                sql.append("     ,sum(case when visit_count >= " + getRankF5() + " and sales_value between " + getRankM4() + " and " + (getRankM5() - 1) + " then 1 else 0 end) as f5m4");
                sql.append("     ,sum(case when visit_count >= " + getRankF5() + " and sales_value between " + getRankM3() + " and " + (getRankM4() - 1) + " then 1 else 0 end) as f5m3");
                sql.append("     ,sum(case when visit_count >= " + getRankF5() + " and sales_value between " + getRankM2() + " and " + (getRankM3() - 1) + " then 1 else 0 end) as f5m2");
                sql.append("     ,sum(case when visit_count >= " + getRankF5() + " and sales_value between " + getRankM1() + " and " + (getRankM2() - 1) + " then 1 else 0 end) as f5m1");

                sql.append("     ,sum(case when visit_count between " + getRankF4() + " and " + (getRankF5() - 1) + " and sales_value >= " + getRankM5() + " then 1 else 0 end) as f4m5");
                sql.append("     ,sum(case when visit_count between " + getRankF4() + " and " + (getRankF5() - 1) + " and sales_value between " + getRankM4() + " and " + (getRankM5() - 1) + " then 1 else 0 end) as f4m4");
                sql.append("     ,sum(case when visit_count between " + getRankF4() + " and " + (getRankF5() - 1) + " and sales_value between " + getRankM3() + " and " + (getRankM4() - 1) + " then 1 else 0 end) as f4m3");
                sql.append("     ,sum(case when visit_count between " + getRankF4() + " and " + (getRankF5() - 1) + " and sales_value between " + getRankM2() + " and " + (getRankM3() - 1) + " then 1 else 0 end) as f4m2");
                sql.append("     ,sum(case when visit_count between " + getRankF4() + " and " + (getRankF5() - 1) + " and sales_value between " + getRankM1() + " and " + (getRankM2() - 1) + " then 1 else 0 end) as f4m1");

                sql.append("     ,sum(case when visit_count between " + getRankF3() + " and " + (getRankF4() - 1) + " and sales_value >= " + getRankM5() + " then 1 else 0 end) as f3m5");
                sql.append("     ,sum(case when visit_count between " + getRankF3() + " and " + (getRankF4() - 1) + " and sales_value between " + getRankM4() + " and " + (getRankM5() - 1) + " then 1 else 0 end) as f3m4");
                sql.append("     ,sum(case when visit_count between " + getRankF3() + " and " + (getRankF4() - 1) + " and sales_value between " + getRankM3() + " and " + (getRankM4() - 1) + " then 1 else 0 end) as f3m3");
                sql.append("     ,sum(case when visit_count between " + getRankF3() + " and " + (getRankF4() - 1) + " and sales_value between " + getRankM2() + " and " + (getRankM3() - 1) + " then 1 else 0 end) as f3m2");
                sql.append("     ,sum(case when visit_count between " + getRankF3() + " and " + (getRankF4() - 1) + " and sales_value between " + getRankM1() + " and " + (getRankM2() - 1) + " then 1 else 0 end) as f3m1");

                sql.append("     ,sum(case when visit_count between " + getRankF2() + " and " + (getRankF3() - 1) + " and sales_value >= " + getRankM5() + " then 1 else 0 end) as f2m5");
                sql.append("     ,sum(case when visit_count between " + getRankF2() + " and " + (getRankF3() - 1) + " and sales_value between " + getRankM4() + " and " + (getRankM5() - 1) + " then 1 else 0 end) as f2m4");
                sql.append("     ,sum(case when visit_count between " + getRankF2() + " and " + (getRankF3() - 1) + " and sales_value between " + getRankM3() + " and " + (getRankM4() - 1) + " then 1 else 0 end) as f2m3");
                sql.append("     ,sum(case when visit_count between " + getRankF2() + " and " + (getRankF3() - 1) + " and sales_value between " + getRankM2() + " and " + (getRankM3() - 1) + " then 1 else 0 end) as f2m2");
                sql.append("     ,sum(case when visit_count between " + getRankF2() + " and " + (getRankF3() - 1) + " and sales_value between " + getRankM1() + " and " + (getRankM2() - 1) + " then 1 else 0 end) as f2m1");

                sql.append("     ,sum(case when visit_count between " + getRankF1() + " and " + (getRankF2() - 1) + " and sales_value >= " + getRankM5() + " then 1 else 0 end) as f1m5");
                sql.append("     ,sum(case when visit_count between " + getRankF1() + " and " + (getRankF2() - 1) + " and sales_value between " + getRankM4() + " and " + (getRankM5() - 1) + " then 1 else 0 end) as f1m4");
                sql.append("     ,sum(case when visit_count between " + getRankF1() + " and " + (getRankF2() - 1) + " and sales_value between " + getRankM3() + " and " + (getRankM4() - 1) + " then 1 else 0 end) as f1m3");
                sql.append("     ,sum(case when visit_count between " + getRankF1() + " and " + (getRankF2() - 1) + " and sales_value between " + getRankM2() + " and " + (getRankM3() - 1) + " then 1 else 0 end) as f1m2");
                sql.append("     ,sum(case when visit_count between " + getRankF1() + " and " + (getRankF2() - 1) + " and sales_value between " + getRankM1() + " and " + (getRankM2() - 1) + " then 1 else 0 end) as f1m1");

            }else if( isChkCondition1() && isChkCondition3() ){     //���X��,�Љ�q��
                sql.append("      sum(case when visit_count >= " + getRankF5() + " and introducer_cnt >= " + getRankI5() + " then 1 else 0 end) as f5m5");
                sql.append("     ,sum(case when visit_count >= " + getRankF5() + " and introducer_cnt between " + getRankI4() + " and " + (getRankI5() - 1) + " then 1 else 0 end) as f5m4");
                sql.append("     ,sum(case when visit_count >= " + getRankF5() + " and introducer_cnt between " + getRankI3() + " and " + (getRankI4() - 1) + " then 1 else 0 end) as f5m3");
                sql.append("     ,sum(case when visit_count >= " + getRankF5() + " and introducer_cnt between " + getRankI2() + " and " + (getRankI3() - 1) + " then 1 else 0 end) as f5m2");
                sql.append("     ,sum(case when visit_count >= " + getRankF5() + " and introducer_cnt between " + getRankI1() + " and " + (getRankI2() - 1) + " then 1 else 0 end) as f5m1");

                sql.append("     ,sum(case when visit_count between " + getRankF4() + " and " + (getRankF5() - 1) + " and introducer_cnt >= " + getRankI5() + " then 1 else 0 end) as f4m5");
                sql.append("     ,sum(case when visit_count between " + getRankF4() + " and " + (getRankF5() - 1) + " and introducer_cnt between " + getRankI4() + " and " + (getRankI5() - 1) + " then 1 else 0 end) as f4m4");
                sql.append("     ,sum(case when visit_count between " + getRankF4() + " and " + (getRankF5() - 1) + " and introducer_cnt between " + getRankI3() + " and " + (getRankI4() - 1) + " then 1 else 0 end) as f4m3");
                sql.append("     ,sum(case when visit_count between " + getRankF4() + " and " + (getRankF5() - 1) + " and introducer_cnt between " + getRankI2() + " and " + (getRankI3() - 1) + " then 1 else 0 end) as f4m2");
                sql.append("     ,sum(case when visit_count between " + getRankF4() + " and " + (getRankF5() - 1) + " and introducer_cnt between " + getRankI1() + " and " + (getRankI2() - 1) + " then 1 else 0 end) as f4m1");

                sql.append("     ,sum(case when visit_count between " + getRankF3() + " and " + (getRankF4() - 1) + " and introducer_cnt >= " + getRankI5() + " then 1 else 0 end) as f3m5");
                sql.append("     ,sum(case when visit_count between " + getRankF3() + " and " + (getRankF4() - 1) + " and introducer_cnt between " + getRankI4() + " and " + (getRankI5() - 1) + " then 1 else 0 end) as f3m4");
                sql.append("     ,sum(case when visit_count between " + getRankF3() + " and " + (getRankF4() - 1) + " and introducer_cnt between " + getRankI3() + " and " + (getRankI4() - 1) + " then 1 else 0 end) as f3m3");
                sql.append("     ,sum(case when visit_count between " + getRankF3() + " and " + (getRankF4() - 1) + " and introducer_cnt between " + getRankI2() + " and " + (getRankI3() - 1) + " then 1 else 0 end) as f3m2");
                sql.append("     ,sum(case when visit_count between " + getRankF3() + " and " + (getRankF4() - 1) + " and introducer_cnt between " + getRankI1() + " and " + (getRankI2() - 1) + " then 1 else 0 end) as f3m1");

                sql.append("     ,sum(case when visit_count between " + getRankF2() + " and " + (getRankF3() - 1) + " and introducer_cnt >= " + getRankI5() + " then 1 else 0 end) as f2m5");
                sql.append("     ,sum(case when visit_count between " + getRankF2() + " and " + (getRankF3() - 1) + " and introducer_cnt between " + getRankI4() + " and " + (getRankI5() - 1) + " then 1 else 0 end) as f2m4");
                sql.append("     ,sum(case when visit_count between " + getRankF2() + " and " + (getRankF3() - 1) + " and introducer_cnt between " + getRankI3() + " and " + (getRankI4() - 1) + " then 1 else 0 end) as f2m3");
                sql.append("     ,sum(case when visit_count between " + getRankF2() + " and " + (getRankF3() - 1) + " and introducer_cnt between " + getRankI2() + " and " + (getRankI3() - 1) + " then 1 else 0 end) as f2m2");
                sql.append("     ,sum(case when visit_count between " + getRankF2() + " and " + (getRankF3() - 1) + " and introducer_cnt between " + getRankI1() + " and " + (getRankI2() - 1) + " then 1 else 0 end) as f2m1");

                sql.append("     ,sum(case when visit_count between " + getRankF1() + " and " + (getRankF2() - 1) + " and introducer_cnt >= " + getRankI5() + " then 1 else 0 end) as f1m5");
                sql.append("     ,sum(case when visit_count between " + getRankF1() + " and " + (getRankF2() - 1) + " and introducer_cnt between " + getRankI4() + " and " + (getRankI5() - 1) + " then 1 else 0 end) as f1m4");
                sql.append("     ,sum(case when visit_count between " + getRankF1() + " and " + (getRankF2() - 1) + " and introducer_cnt between " + getRankI3() + " and " + (getRankI4() - 1) + " then 1 else 0 end) as f1m3");
                sql.append("     ,sum(case when visit_count between " + getRankF1() + " and " + (getRankF2() - 1) + " and introducer_cnt between " + getRankI2() + " and " + (getRankI3() - 1) + " then 1 else 0 end) as f1m2");
                sql.append("     ,sum(case when visit_count between " + getRankF1() + " and " + (getRankF2() - 1) + " and introducer_cnt between " + getRankI1() + " and " + (getRankI2() - 1) + " then 1 else 0 end) as f1m1");

            }else if( isChkCondition1() && isChkCondition4() ){     //���X��,�q�P��
                sql.append("      sum(case when visit_count >= " + getRankF5() + " and round(sales_value/visit_count) >= " + getRankA5() + " then 1 else 0 end) as f5m5");
                sql.append("     ,sum(case when visit_count >= " + getRankF5() + " and round(sales_value/visit_count) between " + getRankA4() + " and " + (getRankA5() - 1) + " then 1 else 0 end) as f5m4");
                sql.append("     ,sum(case when visit_count >= " + getRankF5() + " and round(sales_value/visit_count) between " + getRankA3() + " and " + (getRankA4() - 1) + " then 1 else 0 end) as f5m3");
                sql.append("     ,sum(case when visit_count >= " + getRankF5() + " and round(sales_value/visit_count) between " + getRankA2() + " and " + (getRankA3() - 1) + " then 1 else 0 end) as f5m2");
                sql.append("     ,sum(case when visit_count >= " + getRankF5() + " and round(sales_value/visit_count) between " + getRankA1() + " and " + (getRankA2() - 1) + " then 1 else 0 end) as f5m1");

                sql.append("     ,sum(case when visit_count between " + getRankF4() + " and " + (getRankF5() - 1) + " and round(sales_value/visit_count) >= " + getRankA5() + " then 1 else 0 end) as f4m5");
                sql.append("     ,sum(case when visit_count between " + getRankF4() + " and " + (getRankF5() - 1) + " and round(sales_value/visit_count) between " + getRankA4() + " and " + (getRankA5() - 1) + " then 1 else 0 end) as f4m4");
                sql.append("     ,sum(case when visit_count between " + getRankF4() + " and " + (getRankF5() - 1) + " and round(sales_value/visit_count) between " + getRankA3() + " and " + (getRankA4() - 1) + " then 1 else 0 end) as f4m3");
                sql.append("     ,sum(case when visit_count between " + getRankF4() + " and " + (getRankF5() - 1) + " and round(sales_value/visit_count) between " + getRankA2() + " and " + (getRankA3() - 1) + " then 1 else 0 end) as f4m2");
                sql.append("     ,sum(case when visit_count between " + getRankF4() + " and " + (getRankF5() - 1) + " and round(sales_value/visit_count) between " + getRankA1() + " and " + (getRankA2() - 1) + " then 1 else 0 end) as f4m1");

                sql.append("     ,sum(case when visit_count between " + getRankF3() + " and " + (getRankF4() - 1) + " and round(sales_value/visit_count) >= " + getRankA5() + " then 1 else 0 end) as f3m5");
                sql.append("     ,sum(case when visit_count between " + getRankF3() + " and " + (getRankF4() - 1) + " and round(sales_value/visit_count) between " + getRankA4() + " and " + (getRankA5() - 1) + " then 1 else 0 end) as f3m4");
                sql.append("     ,sum(case when visit_count between " + getRankF3() + " and " + (getRankF4() - 1) + " and round(sales_value/visit_count) between " + getRankA3() + " and " + (getRankA4() - 1) + " then 1 else 0 end) as f3m3");
                sql.append("     ,sum(case when visit_count between " + getRankF3() + " and " + (getRankF4() - 1) + " and round(sales_value/visit_count) between " + getRankA2() + " and " + (getRankA3() - 1) + " then 1 else 0 end) as f3m2");
                sql.append("     ,sum(case when visit_count between " + getRankF3() + " and " + (getRankF4() - 1) + " and round(sales_value/visit_count) between " + getRankA1() + " and " + (getRankA2() - 1) + " then 1 else 0 end) as f3m1");

                sql.append("     ,sum(case when visit_count between " + getRankF2() + " and " + (getRankF3() - 1) + " and round(sales_value/visit_count) >= " + getRankA5() + " then 1 else 0 end) as f2m5");
                sql.append("     ,sum(case when visit_count between " + getRankF2() + " and " + (getRankF3() - 1) + " and round(sales_value/visit_count) between " + getRankA4() + " and " + (getRankA5() - 1) + " then 1 else 0 end) as f2m4");
                sql.append("     ,sum(case when visit_count between " + getRankF2() + " and " + (getRankF3() - 1) + " and round(sales_value/visit_count) between " + getRankA3() + " and " + (getRankA4() - 1) + " then 1 else 0 end) as f2m3");
                sql.append("     ,sum(case when visit_count between " + getRankF2() + " and " + (getRankF3() - 1) + " and round(sales_value/visit_count) between " + getRankA2() + " and " + (getRankA3() - 1) + " then 1 else 0 end) as f2m2");
                sql.append("     ,sum(case when visit_count between " + getRankF2() + " and " + (getRankF3() - 1) + " and round(sales_value/visit_count) between " + getRankA1() + " and " + (getRankA2() - 1) + " then 1 else 0 end) as f2m1");

                sql.append("     ,sum(case when visit_count between " + getRankF1() + " and " + (getRankF2() - 1) + " and round(sales_value/visit_count) >= " + getRankA5() + " then 1 else 0 end) as f1m5");
                sql.append("     ,sum(case when visit_count between " + getRankF1() + " and " + (getRankF2() - 1) + " and round(sales_value/visit_count) between " + getRankA4() + " and " + (getRankA5() - 1) + " then 1 else 0 end) as f1m4");
                sql.append("     ,sum(case when visit_count between " + getRankF1() + " and " + (getRankF2() - 1) + " and round(sales_value/visit_count) between " + getRankA3() + " and " + (getRankA4() - 1) + " then 1 else 0 end) as f1m3");
                sql.append("     ,sum(case when visit_count between " + getRankF1() + " and " + (getRankF2() - 1) + " and round(sales_value/visit_count) between " + getRankA2() + " and " + (getRankA3() - 1) + " then 1 else 0 end) as f1m2");
                sql.append("     ,sum(case when visit_count between " + getRankF1() + " and " + (getRankF2() - 1) + " and round(sales_value/visit_count) between " + getRankA1() + " and " + (getRankA2() - 1) + " then 1 else 0 end) as f1m1");

            }else if( isChkCondition2() && isChkCondition3() ){     //������z,�Љ�q��
                sql.append("      sum(case when sales_value >= " + getRankM5() + " and introducer_cnt >= " + getRankI5() + " then 1 else 0 end) as f5m5");
                sql.append("     ,sum(case when sales_value >= " + getRankM5() + " and introducer_cnt between " + getRankI4() + " and " + (getRankI5() - 1) + " then 1 else 0 end) as f5m4");
                sql.append("     ,sum(case when sales_value >= " + getRankM5() + " and introducer_cnt between " + getRankI3() + " and " + (getRankI4() - 1) + " then 1 else 0 end) as f5m3");
                sql.append("     ,sum(case when sales_value >= " + getRankM5() + " and introducer_cnt between " + getRankI2() + " and " + (getRankI3() - 1) + " then 1 else 0 end) as f5m2");
                sql.append("     ,sum(case when sales_value >= " + getRankM5() + " and introducer_cnt between " + getRankI1() + " and " + (getRankI2() - 1) + " then 1 else 0 end) as f5m1");

                sql.append("     ,sum(case when sales_value between " + getRankM4() + " and " + (getRankM5() - 1) + " and introducer_cnt >= " + getRankI5() + " then 1 else 0 end) as f4m5");
                sql.append("     ,sum(case when sales_value between " + getRankM4() + " and " + (getRankM5() - 1) + " and introducer_cnt between " + getRankI4() + " and " + (getRankI5() - 1) + " then 1 else 0 end) as f4m4");
                sql.append("     ,sum(case when sales_value between " + getRankM4() + " and " + (getRankM5() - 1) + " and introducer_cnt between " + getRankI3() + " and " + (getRankI4() - 1) + " then 1 else 0 end) as f4m3");
                sql.append("     ,sum(case when sales_value between " + getRankM4() + " and " + (getRankM5() - 1) + " and introducer_cnt between " + getRankI2() + " and " + (getRankI3() - 1) + " then 1 else 0 end) as f4m2");
                sql.append("     ,sum(case when sales_value between " + getRankM4() + " and " + (getRankM5() - 1) + " and introducer_cnt between " + getRankI1() + " and " + (getRankI2() - 1) + " then 1 else 0 end) as f4m1");

                sql.append("     ,sum(case when sales_value between " + getRankM3() + " and " + (getRankM4() - 1) + " and introducer_cnt >= " + getRankI5() + " then 1 else 0 end) as f3m5");
                sql.append("     ,sum(case when sales_value between " + getRankM3() + " and " + (getRankM4() - 1) + " and introducer_cnt between " + getRankI4() + " and " + (getRankI5() - 1) + " then 1 else 0 end) as f3m4");
                sql.append("     ,sum(case when sales_value between " + getRankM3() + " and " + (getRankM4() - 1) + " and introducer_cnt between " + getRankI3() + " and " + (getRankI4() - 1) + " then 1 else 0 end) as f3m3");
                sql.append("     ,sum(case when sales_value between " + getRankM3() + " and " + (getRankM4() - 1) + " and introducer_cnt between " + getRankI2() + " and " + (getRankI3() - 1) + " then 1 else 0 end) as f3m2");
                sql.append("     ,sum(case when sales_value between " + getRankM3() + " and " + (getRankM4() - 1) + " and introducer_cnt between " + getRankI1() + " and " + (getRankI2() - 1) + " then 1 else 0 end) as f3m1");

                sql.append("     ,sum(case when sales_value between " + getRankM2() + " and " + (getRankM3() - 1) + " and introducer_cnt >= " + getRankI5() + " then 1 else 0 end) as f2m5");
                sql.append("     ,sum(case when sales_value between " + getRankM2() + " and " + (getRankM3() - 1) + " and introducer_cnt between " + getRankI4() + " and " + (getRankI5() - 1) + " then 1 else 0 end) as f2m4");
                sql.append("     ,sum(case when sales_value between " + getRankM2() + " and " + (getRankM3() - 1) + " and introducer_cnt between " + getRankI3() + " and " + (getRankI4() - 1) + " then 1 else 0 end) as f2m3");
                sql.append("     ,sum(case when sales_value between " + getRankM2() + " and " + (getRankM3() - 1) + " and introducer_cnt between " + getRankI2() + " and " + (getRankI3() - 1) + " then 1 else 0 end) as f2m2");
                sql.append("     ,sum(case when sales_value between " + getRankM2() + " and " + (getRankM3() - 1) + " and introducer_cnt between " + getRankI1() + " and " + (getRankI2() - 1) + " then 1 else 0 end) as f2m1");

                sql.append("     ,sum(case when sales_value between " + getRankM1() + " and " + (getRankM2() - 1) + " and introducer_cnt >= " + getRankI5() + " then 1 else 0 end) as f1m5");
                sql.append("     ,sum(case when sales_value between " + getRankM1() + " and " + (getRankM2() - 1) + " and introducer_cnt between " + getRankI4() + " and " + (getRankI5() - 1) + " then 1 else 0 end) as f1m4");
                sql.append("     ,sum(case when sales_value between " + getRankM1() + " and " + (getRankM2() - 1) + " and introducer_cnt between " + getRankI3() + " and " + (getRankI4() - 1) + " then 1 else 0 end) as f1m3");
                sql.append("     ,sum(case when sales_value between " + getRankM1() + " and " + (getRankM2() - 1) + " and introducer_cnt between " + getRankI2() + " and " + (getRankI3() - 1) + " then 1 else 0 end) as f1m2");
                sql.append("     ,sum(case when sales_value between " + getRankM1() + " and " + (getRankM2() - 1) + " and introducer_cnt between " + getRankI1() + " and " + (getRankI2() - 1) + " then 1 else 0 end) as f1m1");

            }else if( isChkCondition2() && isChkCondition4() ){     //������z,�q�P��
                sql.append("      sum(case when sales_value >= " + getRankM5() + " and round(sales_value/visit_count) >= " + getRankA5() + " then 1 else 0 end) as f5m5");
                sql.append("     ,sum(case when sales_value >= " + getRankM5() + " and round(sales_value/visit_count) between " + getRankA4() + " and " + (getRankA5() - 1) + " then 1 else 0 end) as f5m4");
                sql.append("     ,sum(case when sales_value >= " + getRankM5() + " and round(sales_value/visit_count) between " + getRankA3() + " and " + (getRankA4() - 1) + " then 1 else 0 end) as f5m3");
                sql.append("     ,sum(case when sales_value >= " + getRankM5() + " and round(sales_value/visit_count) between " + getRankA2() + " and " + (getRankA3() - 1) + " then 1 else 0 end) as f5m2");
                sql.append("     ,sum(case when sales_value >= " + getRankM5() + " and round(sales_value/visit_count) between " + getRankA1() + " and " + (getRankA2() - 1) + " then 1 else 0 end) as f5m1");

                sql.append("     ,sum(case when sales_value between " + getRankM4() + " and " + (getRankM5() - 1) + " and round(sales_value/visit_count) >= " + getRankA5() + " then 1 else 0 end) as f4m5");
                sql.append("     ,sum(case when sales_value between " + getRankM4() + " and " + (getRankM5() - 1) + " and round(sales_value/visit_count) between " + getRankA4() + " and " + (getRankA5() - 1) + " then 1 else 0 end) as f4m4");
                sql.append("     ,sum(case when sales_value between " + getRankM4() + " and " + (getRankM5() - 1) + " and round(sales_value/visit_count) between " + getRankA3() + " and " + (getRankA4() - 1) + " then 1 else 0 end) as f4m3");
                sql.append("     ,sum(case when sales_value between " + getRankM4() + " and " + (getRankM5() - 1) + " and round(sales_value/visit_count) between " + getRankA2() + " and " + (getRankA3() - 1) + " then 1 else 0 end) as f4m2");
                sql.append("     ,sum(case when sales_value between " + getRankM4() + " and " + (getRankM5() - 1) + " and round(sales_value/visit_count) between " + getRankA1() + " and " + (getRankA2() - 1) + " then 1 else 0 end) as f4m1");

                sql.append("     ,sum(case when sales_value between " + getRankM3() + " and " + (getRankM4() - 1) + " and round(sales_value/visit_count) >= " + getRankA5() + " then 1 else 0 end) as f3m5");
                sql.append("     ,sum(case when sales_value between " + getRankM3() + " and " + (getRankM4() - 1) + " and round(sales_value/visit_count) between " + getRankA4() + " and " + (getRankA5() - 1) + " then 1 else 0 end) as f3m4");
                sql.append("     ,sum(case when sales_value between " + getRankM3() + " and " + (getRankM4() - 1) + " and round(sales_value/visit_count) between " + getRankA3() + " and " + (getRankA4() - 1) + " then 1 else 0 end) as f3m3");
                sql.append("     ,sum(case when sales_value between " + getRankM3() + " and " + (getRankM4() - 1) + " and round(sales_value/visit_count) between " + getRankA2() + " and " + (getRankA3() - 1) + " then 1 else 0 end) as f3m2");
                sql.append("     ,sum(case when sales_value between " + getRankM3() + " and " + (getRankM4() - 1) + " and round(sales_value/visit_count) between " + getRankA1() + " and " + (getRankA2() - 1) + " then 1 else 0 end) as f3m1");

                sql.append("     ,sum(case when sales_value between " + getRankM2() + " and " + (getRankM3() - 1) + " and round(sales_value/visit_count) >= " + getRankA5() + " then 1 else 0 end) as f2m5");
                sql.append("     ,sum(case when sales_value between " + getRankM2() + " and " + (getRankM3() - 1) + " and round(sales_value/visit_count) between " + getRankA4() + " and " + (getRankA5() - 1) + " then 1 else 0 end) as f2m4");
                sql.append("     ,sum(case when sales_value between " + getRankM2() + " and " + (getRankM3() - 1) + " and round(sales_value/visit_count) between " + getRankA3() + " and " + (getRankA4() - 1) + " then 1 else 0 end) as f2m3");
                sql.append("     ,sum(case when sales_value between " + getRankM2() + " and " + (getRankM3() - 1) + " and round(sales_value/visit_count) between " + getRankA2() + " and " + (getRankA3() - 1) + " then 1 else 0 end) as f2m2");
                sql.append("     ,sum(case when sales_value between " + getRankM2() + " and " + (getRankM3() - 1) + " and round(sales_value/visit_count) between " + getRankA1() + " and " + (getRankA2() - 1) + " then 1 else 0 end) as f2m1");

                sql.append("     ,sum(case when sales_value between " + getRankM1() + " and " + (getRankM2() - 1) + " and round(sales_value/visit_count) >= " + getRankA5() + " then 1 else 0 end) as f1m5");
                sql.append("     ,sum(case when sales_value between " + getRankM1() + " and " + (getRankM2() - 1) + " and round(sales_value/visit_count) between " + getRankA4() + " and " + (getRankA5() - 1) + " then 1 else 0 end) as f1m4");
                sql.append("     ,sum(case when sales_value between " + getRankM1() + " and " + (getRankM2() - 1) + " and round(sales_value/visit_count) between " + getRankA3() + " and " + (getRankA4() - 1) + " then 1 else 0 end) as f1m3");
                sql.append("     ,sum(case when sales_value between " + getRankM1() + " and " + (getRankM2() - 1) + " and round(sales_value/visit_count) between " + getRankA2() + " and " + (getRankA3() - 1) + " then 1 else 0 end) as f1m2");
                sql.append("     ,sum(case when sales_value between " + getRankM1() + " and " + (getRankM2() - 1) + " and round(sales_value/visit_count) between " + getRankA1() + " and " + (getRankA2() - 1) + " then 1 else 0 end) as f1m1");

            }else if( isChkCondition3() && isChkCondition4() ){     //�Љ�q��,�q�P��
                sql.append("      sum(case when introducer_cnt >= " + getRankI5() + " and round(sales_value/visit_count) >= " + getRankA5() + " then 1 else 0 end) as f5m5");
                sql.append("     ,sum(case when introducer_cnt >= " + getRankI5() + " and round(sales_value/visit_count) between " + getRankA4() + " and " + (getRankA5() - 1) + " then 1 else 0 end) as f5m4");
                sql.append("     ,sum(case when introducer_cnt >= " + getRankI5() + " and round(sales_value/visit_count) between " + getRankA3() + " and " + (getRankA4() - 1) + " then 1 else 0 end) as f5m3");
                sql.append("     ,sum(case when introducer_cnt >= " + getRankI5() + " and round(sales_value/visit_count) between " + getRankA2() + " and " + (getRankA3() - 1) + " then 1 else 0 end) as f5m2");
                sql.append("     ,sum(case when introducer_cnt >= " + getRankI5() + " and round(sales_value/visit_count) between " + getRankA1() + " and " + (getRankA2() - 1) + " then 1 else 0 end) as f5m1");

                sql.append("     ,sum(case when introducer_cnt between " + getRankI4() + " and " + (getRankI5() - 1) + " and round(sales_value/visit_count) >= " + getRankA5() + " then 1 else 0 end) as f4m5");
                sql.append("     ,sum(case when introducer_cnt between " + getRankI4() + " and " + (getRankI5() - 1) + " and round(sales_value/visit_count) between " + getRankA4() + " and " + (getRankA5() - 1) + " then 1 else 0 end) as f4m4");
                sql.append("     ,sum(case when introducer_cnt between " + getRankI4() + " and " + (getRankI5() - 1) + " and round(sales_value/visit_count) between " + getRankA3() + " and " + (getRankA4() - 1) + " then 1 else 0 end) as f4m3");
                sql.append("     ,sum(case when introducer_cnt between " + getRankI4() + " and " + (getRankI5() - 1) + " and round(sales_value/visit_count) between " + getRankA2() + " and " + (getRankA3() - 1) + " then 1 else 0 end) as f4m2");
                sql.append("     ,sum(case when introducer_cnt between " + getRankI4() + " and " + (getRankI5() - 1) + " and round(sales_value/visit_count) between " + getRankA1() + " and " + (getRankA2() - 1) + " then 1 else 0 end) as f4m1");

                sql.append("     ,sum(case when introducer_cnt between " + getRankI3() + " and " + (getRankI4() - 1) + " and round(sales_value/visit_count) >= " + getRankA5() + " then 1 else 0 end) as f3m5");
                sql.append("     ,sum(case when introducer_cnt between " + getRankI3() + " and " + (getRankI4() - 1) + " and round(sales_value/visit_count) between " + getRankA4() + " and " + (getRankA5() - 1) + " then 1 else 0 end) as f3m4");
                sql.append("     ,sum(case when introducer_cnt between " + getRankI3() + " and " + (getRankI4() - 1) + " and round(sales_value/visit_count) between " + getRankA3() + " and " + (getRankA4() - 1) + " then 1 else 0 end) as f3m3");
                sql.append("     ,sum(case when introducer_cnt between " + getRankI3() + " and " + (getRankI4() - 1) + " and round(sales_value/visit_count) between " + getRankA2() + " and " + (getRankA3() - 1) + " then 1 else 0 end) as f3m2");
                sql.append("     ,sum(case when introducer_cnt between " + getRankI3() + " and " + (getRankI4() - 1) + " and round(sales_value/visit_count) between " + getRankA1() + " and " + (getRankA2() - 1) + " then 1 else 0 end) as f3m1");

                sql.append("     ,sum(case when introducer_cnt between " + getRankI2() + " and " + (getRankI3() - 1) + " and round(sales_value/visit_count) >= " + getRankA5() + " then 1 else 0 end) as f2m5");
                sql.append("     ,sum(case when introducer_cnt between " + getRankI2() + " and " + (getRankI3() - 1) + " and round(sales_value/visit_count) between " + getRankA4() + " and " + (getRankA5() - 1) + " then 1 else 0 end) as f2m4");
                sql.append("     ,sum(case when introducer_cnt between " + getRankI2() + " and " + (getRankI3() - 1) + " and round(sales_value/visit_count) between " + getRankA3() + " and " + (getRankA4() - 1) + " then 1 else 0 end) as f2m3");
                sql.append("     ,sum(case when introducer_cnt between " + getRankI2() + " and " + (getRankI3() - 1) + " and round(sales_value/visit_count) between " + getRankA2() + " and " + (getRankA3() - 1) + " then 1 else 0 end) as f2m2");
                sql.append("     ,sum(case when introducer_cnt between " + getRankI2() + " and " + (getRankI3() - 1) + " and round(sales_value/visit_count) between " + getRankA1() + " and " + (getRankA2() - 1) + " then 1 else 0 end) as f2m1");

                sql.append("     ,sum(case when introducer_cnt between " + getRankI1() + " and " + (getRankI2() - 1) + " and round(sales_value/visit_count) >= " + getRankA5() + " then 1 else 0 end) as f1m5");
                sql.append("     ,sum(case when introducer_cnt between " + getRankI1() + " and " + (getRankI2() - 1) + " and round(sales_value/visit_count) between " + getRankA4() + " and " + (getRankA5() - 1) + " then 1 else 0 end) as f1m4");
                sql.append("     ,sum(case when introducer_cnt between " + getRankI1() + " and " + (getRankI2() - 1) + " and round(sales_value/visit_count) between " + getRankA3() + " and " + (getRankA4() - 1) + " then 1 else 0 end) as f1m3");
                sql.append("     ,sum(case when introducer_cnt between " + getRankI1() + " and " + (getRankI2() - 1) + " and round(sales_value/visit_count) between " + getRankA2() + " and " + (getRankA3() - 1) + " then 1 else 0 end) as f1m2");
                sql.append("     ,sum(case when introducer_cnt between " + getRankI1() + " and " + (getRankI2() - 1) + " and round(sales_value/visit_count) between " + getRankA1() + " and " + (getRankA2() - 1) + " then 1 else 0 end) as f1m1");

            }
            sql.append(" from");
            sql.append("     wk_rank");

            rs = con.executeQuery(sql.toString());
            if (rs.next()) {

                CustomerRanking temp = null;
                
                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f5m5"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f5m4"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f5m3"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f5m2"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f5m1"));
                this.add(temp);
                
                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f4m5"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f4m4"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f4m3"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f4m2"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f4m1"));
                this.add(temp);
                
                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f3m5"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f3m4"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f3m3"));
                this.add(temp);
                
                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f3m2"));
                this.add(temp);
                
                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f3m1"));
                this.add(temp);
                
                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f2m5"));
                this.add(temp);
                
                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f2m4"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f2m3"));
                this.add(temp);
                
                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f2m2"));
                this.add(temp);
                
                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f2m1"));
                this.add(temp);
                
                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f1m5"));
                this.add(temp);
                
                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f1m4"));
                this.add(temp);

                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f1m3"));
                this.add(temp);
                
                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f1m2"));
                this.add(temp);
                
                temp = new CustomerRanking();
                temp.setTargetCount(rs.getLong("f1m1"));
                this.add(temp);
            }

            rs.close();
	}
        
    public long getLimitCount() {
        return limitCount;
    }

    public void setLimitCount(long limitCount) {
        this.limitCount = limitCount;
    }

    public long getRankF5() {
        return rankF5;
    }

    public void setRankF5(long rankF5) {
        this.rankF5 = rankF5;
    }

    public long getRankF4() {
        return rankF4;
    }

    public void setRankF4(long rankF4) {
        this.rankF4 = rankF4;
    }

    public long getRankF3() {
        return rankF3;
    }

    public void setRankF3(long rankF3) {
        this.rankF3 = rankF3;
    }

    public long getRankF2() {
        return rankF2;
    }

    public void setRankF2(long rankF2) {
        this.rankF2 = rankF2;
    }

    public long getRankF1() {
        return rankF1;
    }

    public void setRankF1(long rankF1) {
        this.rankF1 = rankF1;
    }

    public long getRankM5() {
        return rankM5;
    }

    public void setRankM5(long rankM5) {
        this.rankM5 = rankM5;
    }

    public long getRankM4() {
        return rankM4;
    }

    public void setRankM4(long rankM4) {
        this.rankM4 = rankM4;
    }

    public long getRankM3() {
        return rankM3;
    }

    public void setRankM3(long rankM3) {
        this.rankM3 = rankM3;
    }

    public long getRankM2() {
        return rankM2;
    }

    public void setRankM2(long rankM2) {
        this.rankM2 = rankM2;
    }

    public long getRankM1() {
        return rankM1;
    }

    public void setRankM1(long rankM1) {
        this.rankM1 = rankM1;
    }

    public long getRankI5() {
        return rankI5;
    }

    public void setRankI5(long rankI5) {
        this.rankI5 = rankI5;
    }

    public long getRankI4() {
        return rankI4;
    }

    public void setRankI4(long rankI4) {
        this.rankI4 = rankI4;
    }

    public long getRankI3() {
        return rankI3;
    }

    public void setRankI3(long rankI3) {
        this.rankI3 = rankI3;
    }

    public long getRankI2() {
        return rankI2;
    }

    public void setRankI2(long rankI2) {
        this.rankI2 = rankI2;
    }

    public long getRankI1() {
        return rankI1;
    }

    public void setRankI1(long rankI1) {
        this.rankI1 = rankI1;
    }

    public long getRankA5() {
        return rankA5;
    }

    public void setRankA5(long rankA5) {
        this.rankA5 = rankA5;
    }

    public long getRankA4() {
        return rankA4;
    }

    public void setRankA4(long rankA4) {
        this.rankA4 = rankA4;
    }

    public long getRankA3() {
        return rankA3;
    }

    public void setRankA3(long rankA3) {
        this.rankA3 = rankA3;
    }

    public long getRankA2() {
        return rankA2;
    }

    public void setRankA2(long rankA2) {
        this.rankA2 = rankA2;
    }

    public long getRankA1() {
        return rankA1;
    }

    public void setRankA1(long rankA1) {
        this.rankA1 = rankA1;
    }

    public String getRankF5M5Name() {
        return rankF5M5Name;
    }

    public void setRankF5M5Name(String rankF5M5Name) {
        this.rankF5M5Name = rankF5M5Name;
    }

    public String getRankF5M4Name() {
        return rankF5M4Name;
    }

    public void setRankF5M4Name(String rankF5M4Name) {
        this.rankF5M4Name = rankF5M4Name;
    }

    public String getRankF5M3Name() {
        return rankF5M3Name;
    }

    public void setRankF5M3Name(String rankF5M3Name) {
        this.rankF5M3Name = rankF5M3Name;
    }

    public String getRankF5M2Name() {
        return rankF5M2Name;
    }

    public void setRankF5M2Name(String rankF5M2Name) {
        this.rankF5M2Name = rankF5M2Name;
    }

    public String getRankF5M1Name() {
        return rankF5M1Name;
    }

    public void setRankF5M1Name(String rankF5M1Name) {
        this.rankF5M1Name = rankF5M1Name;
    }

    public String getRankF4M5Name() {
        return rankF4M5Name;
    }

    public void setRankF4M5Name(String rankF4M5Name) {
        this.rankF4M5Name = rankF4M5Name;
    }

    public String getRankF4M4Name() {
        return rankF4M4Name;
    }

    public void setRankF4M4Name(String rankF4M4Name) {
        this.rankF4M4Name = rankF4M4Name;
    }

    public String getRankF4M3Name() {
        return rankF4M3Name;
    }

    public void setRankF4M3Name(String rankF4M3Name) {
        this.rankF4M3Name = rankF4M3Name;
    }

    public String getRankF4M2Name() {
        return rankF4M2Name;
    }

    public void setRankF4M2Name(String rankF4M2Name) {
        this.rankF4M2Name = rankF4M2Name;
    }

    public String getRankF4M1Name() {
        return rankF4M1Name;
    }

    public void setRankF4M1Name(String rankF4M1Name) {
        this.rankF4M1Name = rankF4M1Name;
    }

    public String getRankF3M5Name() {
        return rankF3M5Name;
    }

    public void setRankF3M5Name(String rankF3M5Name) {
        this.rankF3M5Name = rankF3M5Name;
    }

    public String getRankF3M4Name() {
        return rankF3M4Name;
    }

    public void setRankF3M4Name(String rankF3M4Name) {
        this.rankF3M4Name = rankF3M4Name;
    }

    public String getRankF3M3Name() {
        return rankF3M3Name;
    }

    public void setRankF3M3Name(String rankF3M3Name) {
        this.rankF3M3Name = rankF3M3Name;
    }

    public String getRankF3M2Name() {
        return rankF3M2Name;
    }

    public void setRankF3M2Name(String rankF3M2Name) {
        this.rankF3M2Name = rankF3M2Name;
    }

    public String getRankF3M1Name() {
        return rankF3M1Name;
    }

    public void setRankF3M1Name(String rankF3M1Name) {
        this.rankF3M1Name = rankF3M1Name;
    }

    public String getRankF2M5Name() {
        return rankF2M5Name;
    }

    public void setRankF2M5Name(String rankF2M5Name) {
        this.rankF2M5Name = rankF2M5Name;
    }

    public String getRankF2M4Name() {
        return rankF2M4Name;
    }

    public void setRankF2M4Name(String rankF2M4Name) {
        this.rankF2M4Name = rankF2M4Name;
    }

    public String getRankF2M3Name() {
        return rankF2M3Name;
    }

    public void setRankF2M3Name(String rankF2M3Name) {
        this.rankF2M3Name = rankF2M3Name;
    }

    public String getRankF2M2Name() {
        return rankF2M2Name;
    }

    public void setRankF2M2Name(String rankF2M2Name) {
        this.rankF2M2Name = rankF2M2Name;
    }

    public String getRankF2M1Name() {
        return rankF2M1Name;
    }

    public void setRankF2M1Name(String rankF2M1Name) {
        this.rankF2M1Name = rankF2M1Name;
    }

    public String getRankF1M5Name() {
        return rankF1M5Name;
    }

    public void setRankF1M5Name(String rankF1M5Name) {
        this.rankF1M5Name = rankF1M5Name;
    }

    public String getRankF1M4Name() {
        return rankF1M4Name;
    }

    public void setRankF1M4Name(String rankF1M4Name) {
        this.rankF1M4Name = rankF1M4Name;
    }

    public String getRankF1M3Name() {
        return rankF1M3Name;
    }

    public void setRankF1M3Name(String rankF1M3Name) {
        this.rankF1M3Name = rankF1M3Name;
    }

    public String getRankF1M2Name() {
        return rankF1M2Name;
    }

    public void setRankF1M2Name(String rankF1M2Name) {
        this.rankF1M2Name = rankF1M2Name;
    }

    public String getRankF1M1Name() {
        return rankF1M1Name;
    }

    public void setRankF1M1Name(String rankF1M1Name) {
        this.rankF1M1Name = rankF1M1Name;
    }

    public void setPastTotal(boolean isPastTotal) {
        this.isPastTotal = isPastTotal;
    }

    public boolean isChkValid1() {
        return chkValid1;
    }

    public void setChkValid1(boolean chkValid1) {
        this.chkValid1 = chkValid1;
    }

    public boolean isChkValid2() {
        return chkValid2;
    }

    public void setChkValid2(boolean chkValid2) {
        this.chkValid2 = chkValid2;
    }

    public boolean isChkValid3() {
        return chkValid3;
    }

    public void setChkValid3(boolean chkValid3) {
        this.chkValid3 = chkValid3;
    }

    public boolean isChkCondition1() {
        return chkCondition1;
    }

    public void setChkCondition1(boolean chkCondition1) {
        this.chkCondition1 = chkCondition1;
    }

    public boolean isChkCondition2() {
        return chkCondition2;
    }

    public void setChkCondition2(boolean chkCondition2) {
        this.chkCondition2 = chkCondition2;
    }

    public boolean isChkCondition3() {
        return chkCondition3;
    }

    public void setChkCondition3(boolean chkCondition3) {
        this.chkCondition3 = chkCondition3;
    }

    public boolean isChkCondition4() {
        return chkCondition4;
    }

    public void setChkCondition4(boolean chkCondition4) {
        this.chkCondition4 = chkCondition4;
    }
}
