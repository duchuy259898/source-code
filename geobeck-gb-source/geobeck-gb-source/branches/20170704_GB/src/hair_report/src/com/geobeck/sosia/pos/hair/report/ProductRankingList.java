/*
 * ProductRankingList.java
 *
 * Created on 2008/07/20, 22:00
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
 * �X�^�b�t�����L���O�ꗗ����
 * @author saito
 */
public class ProductRankingList extends ArrayList<ProductRanking>
{
        //�ŋ敪
        private final	int	TAX_TYPE_BLANK		=   1;	//�Ŕ�
	private final	int	TAX_TYPE_UNIT		=   2;	//�ō�

	/**
	 * �Z�p�E���i�^�u�C���f�b�N�X
	 */
	private	Integer	selectedTabIndex	=   null;
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
	/**
	 * �ŋ敪
	 */
	private	Integer	taxType                 =   null;
	/**
	 * ����ID
	 */
	private	Integer	prodClassID             =   null;
	/**
	 * �\����
	 */
	private	Integer	orderDisplay            =   null;
	
        //nhanvt start add 20141216 New request #33406
        private String listCategoryId = null;
        private boolean hideCategory = false;
        private Integer useShopCategory = null;
        

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
        
	/**
         * Creates a new instance of ProductRankingList
         */
	public ProductRankingList()
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
	 * ����ID���擾����B
	 * @return ����ID
	 */
        public Integer getProdClassID() {
                return prodClassID;
        }
	/**
         * ����ID���Z�b�g����B
         * @param classID ����ID
         */
        public void setProdClassID(Integer prodClassID) {
                this.prodClassID = prodClassID;
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
     * �f�[�^��ǂݍ��ށB
     */
	public void load()
	{
		try
		{
                    //�����L���O���X�g�擾
                    this.getProductRankingList();
		}
		catch(Exception e)
		{
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * �X�^�b�t�����L���O�̃��X�g���擾����B
	 * @exception Exception
	 */
	private void getProductRankingList() throws Exception
	{
		this.clear();
		
		ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(this.getSelectProductRankingSQL());

                double countTotal = 0;
                double salesTotal = 0;
                
		while(rs.next())
		{
			ProductRanking temp = new ProductRanking();
			temp.setData(rs);
                        
                        countTotal += rs.getLong("product_num");
                        salesTotal += rs.getLong("sales_value");
                        
			this.add(temp);
		}

                BigDecimal bd = null;
                NumberFormat nf = NumberFormat.getInstance();
                nf.setMinimumFractionDigits(2);
                
                for(ProductRanking temp : this) {
                    
                    // �䗦�i���ʁj
                    if (countTotal == 0) {
                        bd = new BigDecimal(0);
                    } else {
                        bd = new BigDecimal(temp.getProdCount() / countTotal * 100);
                    }
                    temp.setCountRatio(nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "%");
                    
                    // �䗦�i����j
                    if (salesTotal == 0) {
                        bd = new BigDecimal(0);
                    } else {
                        bd = new BigDecimal(temp.getProdSales() / salesTotal * 100);
                    }
                    temp.setSalesRatio(nf.format(bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "%");
                }
                
		rs.close();
	}
	
	/**
	 * �X�^�b�t�����L���O���o�pSQL���擾����B
	 * @return �X�^�b�t�����L���O���o�pSQL
	 * @exception Exception
	 */
	private String getSelectProductRankingSQL() throws Exception
	{
	    StringBuilder sql = new StringBuilder(1000);
            sql.append(" select");
            sql.append("      a.product_id");
            sql.append("     ,max(b.product_name)    as product_name");
            sql.append("     ,max(b.class_name)      as class_name");
            sql.append("     ,max(b.unit_price)      as unit_price");
            sql.append("     ,sum(a.product_num)     as product_num");
            
            if (getTaxType() == TAX_TYPE_BLANK){
                // ������Ŕ������z
                sql.append(" ,sum(a.discount_detail_value_no_tax) as sales_value");
                // �Ŕ����������z
                sql.append(" ,sum(a.detail_value_no_tax - a.discount_detail_value_no_tax) as discount_value");
            } else {
                // ������ō��݋��z
                sql.append(" ,sum(a.discount_detail_value_in_tax) as sales_value");
                // �ō��݊������z
                sql.append(" ,sum(a.detail_value_in_tax - a.discount_detail_value_in_tax) as discount_value");
            }
            
            sql.append(" from");
            sql.append("     view_data_sales_detail_valid a");
            sql.append("         join");
            sql.append("             (");
            
                if (this.getSelectedTabIndex() == 3){
                    //--------------------
                    // �Z�p
                    //--------------------
                    sql.append(" select");
                    sql.append("      a.technic_id          as product_id");
                    sql.append("     ,a.technic_name        as product_name");
                    sql.append("     ,b.technic_class_name  as class_name");

                    if (getTaxType() == TAX_TYPE_BLANK){
                        // �Ŕ�
                        sql.append(" ,sign(a.price / (1.0 + get_tax_rate(current_date))) * ceil(abs(a.price / (1.0 + get_tax_rate(current_date)))) as unit_price");
                    } else {
                        // �ō�
                        sql.append(" ,a.price        as unit_price");
                    }

                    sql.append("     ,1              as product_division");
                    sql.append(" from");
                    sql.append("     mst_technic a");
                    sql.append("         join mst_technic_class b");
                    sql.append("             using(technic_class_id)");
                    sql.append(" where true");
                    if (getProdClassID() != null) {
                        sql.append(" and a.technic_class_id = " + getProdClassID());
                    }

                } else if (this.getSelectedTabIndex() == 4) {
                    //--------------------
                    // ���i
                    //--------------------
                    sql.append(" select");
                    sql.append("      a.item_id          as product_id");
                    sql.append("     ,a.item_name        as product_name");
                    sql.append("     ,b.item_class_name  as class_name");
                    
                    if (getTaxType() == TAX_TYPE_BLANK){
                        // �Ŕ�
                        sql.append(" ,sign(a.price / (1.0 + get_tax_rate(current_date))) * ceil(abs(a.price / (1.0 + get_tax_rate(current_date)))) as unit_price");
                    } else {
                        // �ō�
                        sql.append(" ,a.price        as unit_price");
                    }
                    
                    sql.append("     ,2              as product_division");
                    sql.append(" from");
                    sql.append("     mst_item a");
                    sql.append("         join mst_item_class b");
                    sql.append("             using(item_class_id)");
                    sql.append(" where true");
                    if (getProdClassID() != null) {
                        sql.append(" and a.item_class_id = " + getProdClassID());
                    }
                }
                //Thanh start add 2013/03/18
                else if (this.getSelectedTabIndex() == 5) {
                    //--------------------
                    // ���i
                    //--------------------
                    sql.append(" select");
                    sql.append("      a.course_id          as product_id");
                    sql.append("     ,a.course_name        as product_name");
                    sql.append("     ,b.course_class_name  as class_name");
                    
                    if (getTaxType() == TAX_TYPE_BLANK){
                        // �Ŕ�
                        sql.append(" ,sign(a.price / (1.0 + get_tax_rate(current_date))) * ceil(abs(a.price / (1.0 + get_tax_rate(current_date)))) as unit_price");
                    } else {
                        // �ō�
                        sql.append(" ,a.price        as unit_price");
                    }
                    
                    sql.append("     ,5              as product_division");
                    sql.append(" from");
                    sql.append("     mst_course a");
                    sql.append("         join mst_course_class b");
                    sql.append("             using(course_class_id)");
                    sql.append(" where true");
                    if (getProdClassID() != null) {
                        sql.append(" and a.course_class_id = " + getProdClassID());
                    }
                }
             //Thanh end add 2013/03/18   
            sql.append("             ) b");
            sql.append("             using (product_id, product_division)");
            sql.append(" where");
            sql.append("         a.shop_id in (" + getShopIDList() + ")");
            if (getStaffID() != null) {
                sql.append(" and a.staff_id = " + getStaffID().intValue());
            }
            sql.append("     and a.sales_date between " + getTermFrom() + " and " + getTermTo());
            //nhanvt start add 20141216 New request #33406
            if(this.getUseShopCategory() != null &&  this.getUseShopCategory() == 1){
                
                if (this.getSelectedTabIndex() == 3){
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
                    sql.append("                     AND dsd.product_division in (1)");
                    //�ƑԑI���̏ꍇ
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                        sql.append("                  AND mstc.shop_category_id in (" + this.getListCategoryId() + ") ");
                    }
                   
                    sql.append(" ) ");
                } else if (this.getSelectedTabIndex() == 4) {
                    sql.append("      and   exists");
                    sql.append("             (");
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
                    sql.append("                      AND dsd.product_division in (2)");
                    //�ƑԑI���̏ꍇ
                    if (this.getListCategoryId() != null && this.getListCategoryId() != "") {
                        sql.append("                   AND mic.shop_category_id in (" + this.getListCategoryId() + ") ");
                    }

                    sql.append(" ) ");
                }
                
                
            }
            //nhanvt end add 20141216 New request #33406
            sql.append(" group by");
            sql.append("     a.product_id");
            
            sql.append(" order by");
            
            //�\����
            if (getOrderDisplay() == 0) {
                sql.append(" product_num desc");
            } else if (getOrderDisplay() == 1) {
                sql.append(" sales_value desc");
            }else {
                sql.append(" sales_value desc");
            }

            return sql.toString();
	}
}
