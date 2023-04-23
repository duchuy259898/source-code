/*
 * SearchAccount.java
 *
 * Created on 2006/05/19, 16:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.search.account;

import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.*;
import com.geobeck.sosia.pos.master.account.*;

/**
 * 伝票検索処理
 * @author katagiri
 */
public class SearchAccount extends ArrayList<AccountData>
{
	protected MstShop				shop		=	new MstShop();
	/**
	 * 売上日
	 */
	protected	java.util.Date[]	salesDate	=	{	null, null	};
	/**
	 * 伝票No.
	 */
	protected	Integer[]			slipNO		=	{	null, null	};
	/**
	 * 顧客ＩＤ
	 */
	protected	String[]			customerNo	=	{	"", ""	};
	/**
	 * スタッフデータ
	 */
	protected	MstStaff			staff		=	new MstStaff();
	/**
	 * 主担当データ
	 */
	protected	MstStaff			chargeStaff		=	new MstStaff();
	/**
	 * 検索する支払区分
	 */
	protected	ArrayList<Boolean>	searchPC	=	new ArrayList<Boolean>();
	
	/**
	 * 支払区分のリスト
	 */
	protected	MstPaymentClasses	paymentClasses	=	new MstPaymentClasses();
	/**
	 * カルテ情報
	 */
	private	Integer     karteFlg	=	null;
        
        /**
         * 表示順
         */
        private int     sortIndex   =       0;
        
        
	/**
	 * コンストラクタ
	 */
	public SearchAccount()
	{
	}

	public MstShop getShop()
	{
		return shop;
	}

	public void setShop(MstShop shop)
	{
		this.shop = shop;
	}

	/**
	 * 売上日
	 * @return 売上日
	 */
	public java.util.Date[] getSalesDate()
	{
		return salesDate;
	}

	/**
	 * 売上日
	 * @param salesDate 売上日
	 */
	public void setSalesDate(java.util.Date[] salesDate)
	{
		this.salesDate = salesDate;
	}

	/**
	 * 売上日
	 * @param index インデックス
	 * @return 売上日
	 */
	public java.util.Date getSalesDate(int index)
	{
		return salesDate[index];
	}

	/**
	 * 売上日
	 * @param index インデックス
	 * @param salesDate 売上日
	 */
	public void setSalesDate(int index, java.util.Date salesDate)
	{
		this.salesDate[index] = salesDate;
	}

	/**
	 * 伝票No.
	 * @return 伝票No.
	 */
	public Integer[] getSlipNO()
	{
		return slipNO;
	}

	/**
	 * 伝票No.
	 * @param slipNO 伝票No.
	 */
	public void setSlipNO(Integer[] slipNO)
	{
		this.slipNO = slipNO;
	}

	/**
	 * 伝票No.
	 * @param index インデックス
	 * @return 伝票No.
	 */
	public Integer getSlipNO(int index)
	{
		return slipNO[index];
	}

	/**
	 * 伝票No.
	 * @param index インデックス
	 * @param slipNO 伝票No.
	 */
	public void setSlipNO(int index, Integer slipNO)
	{
		this.slipNO[index] = slipNO;
	}

	/**
	 * 顧客ＩＤ
	 * @return 顧客ＩＤ
	 */
	public String[] getCustomerNo()
	{
		return customerNo;
	}

	/**
	 * 顧客コード
	 * @param customerNo 顧客コード
	 */
	public void setCustomerID(String[] customerNo)
	{
		this.customerNo = customerNo;
	}

	/**
	 * 顧客コード
	 * @param index インデックス
	 * @return 顧客コード
	 */
	public String getCustomerNo(int index)
	{
		return customerNo[index];
	}

	/**
	 * 顧客コード
	 * @param index インデックス
	 * @param customerNo 顧客コード
	 */
	public void setCustomerNo(int index, String customerNo)
	{
		this.customerNo[index] = customerNo;
	}

	/**
	 * スタッフデータ
	 * @return スタッフデータ
	 */
	public MstStaff getStaff()
	{
		return staff;
	}

	/**
	 * スタッフデータ
	 * @param staff スタッフデータ
	 */
	public void setStaff(MstStaff staff)
	{
		this.staff = staff;
	}

	/**
	 * 主担当者データ
	 * @return スタッフデータ
	 */
	public MstStaff getChargeStaff()
	{
		return chargeStaff;
	}

	/**
	 * 主担当者データ
	 * @param staff スタッフデータ
	 */
	public void setChargeStaff(MstStaff chargeStaff)
	{
		this.chargeStaff = chargeStaff;
	}
	/**
	 * 検索する支払区分
	 * @return 検索する支払区分
	 */
	public ArrayList<Boolean> getSearchPC()
	{
		return searchPC;
	}

	/**
	 * 検索する支払区分
	 * @param searchPC 検索する支払区分
	 */
	public void setSearchPC(ArrayList<Boolean> searchPC)
	{
		this.searchPC = searchPC;
	}

	/**
	 * 支払区分のリスト
	 * @return 支払区分のリスト
	 */
	public MstPaymentClasses getPaymentClasses()
	{
		return paymentClasses;
	}

	/**
	 * 支払区分のリスト
	 * @param paymentClasses 支払区分のリスト
	 */
	public void setPaymentClasses(MstPaymentClasses paymentClasses)
	{
		this.paymentClasses = paymentClasses;
	}
	
	/**
	 * カルテ情報
	 * @return カルテ情報
	 */
        public Integer getKarteFlg() {
            return karteFlg;
        }

	/**
	 * カルテ情報
	 * @param カルテ情報
	 */
        public void setKarteFlg(Integer karteFlg) {
            this.karteFlg = karteFlg;
        }
        
        public int getSortIndex() {
            return sortIndex;
        }

        public void setSortIndex(int sortIndex) {
            this.sortIndex = sortIndex;
        }
        
	/**
	 * 初期化処理を行う。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void init(ConnectionWrapper con) throws SQLException
	{
		paymentClasses.loadClasses(con);
		
		getSearchPC().clear();
		
		getSearchPC().add(new Boolean(true));
		
		for(MstPaymentClass	mpc : paymentClasses)
		{
			getSearchPC().add(new Boolean(true));
		}
	}
	
	
	/**
	 * 伝票データを読み込む。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void load(ConnectionWrapper con) throws SQLException
	{
		this.clear();
                
                // 20171128 GB edit start #32128
                MstAccountSetting ma = new MstAccountSetting();
		ma.load( SystemInfo.getConnection() );
                
		//ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());
                ResultSetWrapper rs = con.executeQuery(this.getSelectSQL(ma.getDisplayPriceType(), ma.getDiscountType()));
                
                //        MstAccountSetting ma = new MstAccountSetting();
                //        ma.load( SystemInfo.getConnection() );
                // 20171128 GB edit end #32128
                
                

		double tax = SystemInfo.getTaxRate(SystemInfo.getSystemDate());
		
		while(rs.next())
		{
			AccountData ad = new AccountData();
			ad.setData(rs, paymentClasses);
                        //IVS_LVTu start add 2015/10/16 Bug #43537
                        ad.getSales().getShop().load(con);
                        //IVS_LVTu end add 2015/10/16 Bug #43537
			this.add(ad);
		}
		
		rs.close();
	}
	
	/**
	 * 伝票データを取得するＳＱＬ文を取得する。
         * 20171128 GB edit #32128
         * 
	 * @return 伝票データを取得するＳＱＬ文
	 */
	//private String getSelectSQL()
        private String getSelectSQL(Integer displayPriceType, Integer discountType)
	{
                String proportionallyCountSql = "";
                proportionallyCountSql += " (";
                proportionallyCountSql += "     select";
                proportionallyCountSql += "         count(*)";
                proportionallyCountSql += "     from";
                proportionallyCountSql += "         data_sales_proportionally";
                proportionallyCountSql += "     where";
                proportionallyCountSql += "             shop_id = t.shop_id";
                proportionallyCountSql += "         and slip_no = t.slip_no";
                proportionallyCountSql += "         and delete_date is null";
                proportionallyCountSql += " )";

                String proportionallyInputCountSql = "";
                proportionallyInputCountSql += " (";
                proportionallyInputCountSql += "     select";
                proportionallyInputCountSql += "         count(*)";
                proportionallyInputCountSql += "     from";
                proportionallyInputCountSql += "         data_sales_proportionally";
                proportionallyInputCountSql += "     where";
                proportionallyInputCountSql += "             shop_id = t.shop_id";
                proportionallyInputCountSql += "         and slip_no = t.slip_no";
                proportionallyInputCountSql += "         and delete_date is null";
                proportionallyInputCountSql += "         and coalesce(staff_id, 0) <> 0";
                proportionallyInputCountSql += " )";
                
                String karteCountSql = "";
                karteCountSql += " (";
                karteCountSql += "     select";
                karteCountSql += "         count(*)";
                karteCountSql += "     from";
                karteCountSql += "         data_technic_karte";
                karteCountSql += "     where";
                karteCountSql += "             shop_id = t.shop_id";
                karteCountSql += "         and slip_no = t.slip_no";
                karteCountSql += "         and delete_date is null";
                karteCountSql += " )";
            
		String sql = "";
		sql += " select";
		sql += "      t.*";
		sql += "     ,(select discount_value from view_data_sales_valid where shop_id = t.shop_id and slip_no = t.slip_no) as alldiscount";
                sql += "     ," + proportionallyCountSql + " as proportionally_count";
                sql += "     ," + proportionallyInputCountSql + " as proportionally_input_count";
                sql += "     ," + karteCountSql + " as karte_count";
                sql += "     ,(select reservation_no from data_reservation where shop_id = t.shop_id and slip_no = t.slip_no and customer_id = t.customer_id) as reservation_no";
		sql += " from (";
		sql += " select";
		sql += "    ds.shop_id,\n";
		sql += "    ds.sales_date,\n";
		sql += "    ds.slip_no,\n";
		sql += "    ds.customer_id,\n";
		sql += "    ds.delete_date,\n";
		sql += "    mc.customer_no,\n";
		sql += "    mc.customer_name1,\n";
		sql += "    mc.customer_name2,\n";

                sql += "    case when exists(select * from view_data_sales_detail_valid where product_division = 8 and shop_id=ds.shop_id and slip_no = ds.slip_no) then \n";
                // 20171128 GB start edit #31747 解約手数料が含まれていない
                //sql += "    (select sum(product_value) from view_data_sales_detail_valid where product_division = 8 and shop_id=ds.shop_id and slip_no = ds.slip_no)\n";
                sql += "    (select sum(product_value) from view_data_sales_detail_valid where product_division in (8, 9) and shop_id=ds.shop_id and slip_no = ds.slip_no)\n";
                // 20171128 GB end edit #31747 解約手数料が含まれていない
                sql += "    else\n";
                //IVS_TMTrong start edit 20150716 Bug #40178
//		sql += "    (" + "\n";
//		sql += "        SELECT" + "\n";
//		sql += "            sum(discount_detail_value_in_tax) as total_value" + "\n";
//		sql += "        FROM" + "\n";
//		sql += "            view_data_sales_detail_valid" + "\n";
//		sql += "        where" + "\n";
//		sql += "            shop_id = ds.shop_id" + "\n";
//		sql += "        and slip_no = ds.slip_no" + "\n";
//		sql += "        and product_division <> 6" + "\n";
//		sql += "    ) ";
//                sql += " end as total_value," + "\n";
                
                sql += "    (" + "\n";
                //IVS_TMTrong start add 20150716 Bug #37392
                sql +=" CASE WHEN EXISTS ( SELECT * FROM view_data_sales_detail_valid WHERE product_division = 7 AND shop_id = ds.shop_id AND slip_no = ds.slip_no) ";
                sql +=" THEN ( ";
                sql +=" SELECT sum(dpd.payment_value) AS payment_value1 FROM data_payment_detail dpd ";
                sql +=" INNER JOIN mst_payment_method mpm ON mpm.payment_method_id = dpd.payment_method_id AND mpm.payment_class_id in (1,2,3,4) ";
                sql +=" WHERE true AND dpd.payment_no = 0 AND shop_id = ds.shop_id AND slip_no = ds.slip_no AND payment_no = dp.payment_no ";
                sql +=" ) ";
                sql +=" ELSE( ";
                //IVS_TMTrong end add 20150716 Bug #37392
                sql += "        SELECT sum( ";
		sql += "  case when ac.discount_type = 0 then   ";
                //Luc start add 20150724 #40579
                sql += "      case when discount_value != 0 then \n";
                //Luc start add 20150724 #40579
                //IVS_LVTu start edit 2015/07/23 Bug #40912
                //sql += "          floor(detail_value_no_tax *( 1+ get_tax_rate(sales_date))) - discount_value ";
                sql += "      CASE WHEN ac.display_price_type = 0 THEN \n";
                sql += "      detail_value_in_tax - discount_value  \n";
                sql += "      else \n";
                sql += "      floor(detail_value_no_tax *(1+ get_tax_rate(sales_date))) - discount_value  \n";
                sql += "      end \n";
                //IVS_LVTu end edit 2015/07/23 Bug #40912
                sql += "      else\n"; 
                //Hoa start edit 20150724 #40912
		//sql +="           discount_detail_value_in_tax - discount_detail_value_no_tax\n"; 
                sql +="           discount_detail_value_in_tax \n"; 
                //Hoa end edit 20150724 #40912
                sql +="       end\n";
                sql += "  else       \n";
                sql += "       case when discount_value !=0 then \n";
                // 20171128 GB edit start #32128
                //IVS_LVTu start edit 2017/10/17 #28171 [gb]伝票確認：お会計画面と数字が違う
                //sql +="             ceil(detail_value_no_tax *( 1+ get_tax_rate(view_data_sales_detail_valid.sales_date)) - ceil(discount_value * ( 1+ get_tax_rate(view_data_sales_detail_valid.sales_date))))\n";
                //sql +="             view_data_sales_detail_valid.discount_detail_value_in_tax\n";
                //IVS_LVTu end edit 2017/10/17 #28171 [gb]伝票確認：お会計画面と数字が違う
                if(displayPriceType == 1 && discountType == 1) {
                    // 外税＆税抜き額から割引
                    // 201712 GB edit start #34095 [GB内対応][gb]伝票確認：請求金額がお会計画面と違う
                    //sql +="             ceil(detail_value_no_tax *( 1+ get_tax_rate(view_data_sales_detail_valid.sales_date)) - ceil(discount_value * ( 1+ get_tax_rate(view_data_sales_detail_valid.sales_date))))\n";
                    sql +=" (ceil(product_value / (1 + get_tax_rate(sales_date))) * product_num) - discount_value+trunc(((ceil(product_value / (1 + get_tax_rate(sales_date))) * product_num) - discount_value) * get_tax_rate(view_data_sales_detail_valid.sales_date))\n";
                    // 201712 GB edit end #34095 [GB内対応][gb]伝票確認：請求金額がお会計画面と違う
                }else {
                    sql +="             view_data_sales_detail_valid.discount_detail_value_in_tax\n";
                }
                // 20171128 GB edit end #32128
                sql += "       else \n";
                sql += "            detail_value_in_tax \n";
                sql += "       end \n ";
                 sql += " end \n ";
        	sql += " ) AS total_value ";
		sql += "        FROM" + "\n";
		sql += "            view_data_sales_detail_valid," + "\n";
                sql += "            (SELECT mst_account_setting.display_price_type, mst_account_setting.discount_type ";
                sql += "                FROM mst_account_setting ";
                sql += "                WHERE mst_account_setting.delete_date IS NULL ";
                sql += "                LIMIT 1) ac ";
		sql += "        where" + "\n";
		sql += "            shop_id = ds.shop_id" + "\n";
		sql += "        and slip_no = ds.slip_no" + "\n";
		sql += "        and product_division <> 6" + "\n";
                //IVS_TMTrong start add 20150716 Bug #37392
		sql += "    ) END ) ";
                //IVS_TMTrong end add 20150716 Bug #37392
                sql += " end as total_value," + "\n";
                //IVS_TMTrong end edit 20150716 Bug #40178
                
		sql += "    dp.bill_value,\n";
                sql += "    ds.designated_flag,\n";
		sql += "    ms.staff_id,\n";
		sql += "    ms.staff_no,\n";
		sql += "    ms.staff_name1,\n";
		sql += "    ms.staff_name2,\n";
		sql += "    ms2.staff_id as chargeStaff_id,\n";
		sql += "    ms2.staff_no as chargeStaff_no,\n";
		sql += "    ms2.staff_name1 as chargeStaff_name1,\n";
		sql += "    ms2.staff_name2 as chargeStaff_name2";
		
		for(MstPaymentClass mpc : paymentClasses)
		{
			sql += " ,(\n";
			sql += "     select" + "\n";
			sql += "         sum(dpd.payment_value) as payment_value" + mpc.getPaymentClassID().toString() + "\n";
			sql += "     from" + "\n";
			sql += "         data_payment_detail dpd" + "\n";
			sql += "             inner join mst_payment_method mpm" + "\n";
			sql += "                     on mpm.payment_method_id = dpd.payment_method_id" + "\n";
			sql += "                    and mpm.payment_class_id = " + mpc.getPaymentClassID().toString() + "\n";
			sql += "     where" + "\n";
			sql += "             true" + "\n";
			sql += "         and dpd.payment_no = 0" + "\n";
			sql += "         and shop_id = ds.shop_id" + "\n";
			sql += "         and slip_no = ds.slip_no" + "\n";
			sql += "         and payment_no = dp.payment_no" + "\n";
            //IVS_LVTu start add 2017/08/01 #20653 [gb]閉店業務＞伝票確認で数字差異
            sql += "         and dpd.delete_date is null" + "\n";
            //IVS_LVTu end add 2017/08/01 #20653 [gb]閉店業務＞伝票確認で数字差異
			sql += " )";
			sql += " as payment_value" + mpc.getPaymentClassID().toString();
		}
		
		sql	+=	" from data_sales ds\n" +
				" left outer join mst_customer mc\n" +
				" on	mc.customer_id = ds.customer_id\n" +
				"		left outer join data_payment dp\n" +
				"		on	dp.shop_id = ds.shop_id\n" +
				"		and	dp.slip_no = ds.slip_no\n" +
				"		and	dp.payment_no = 0\n" +
				"		left outer join mst_staff ms\n" +
				"		on	ms.staff_id = dp.staff_id\n";

		sql	+=	" left outer join mst_staff ms2 \n"+
                                " on ds.staff_id = ms2.staff_id \n" +
                                " where\n" +
                                "      ds.sales_date is not null\n" +
				"  and ds.shop_id = " + SQLUtil.convertForSQL(shop.getShopID()) + "\n";
		
		//精算日
		if(salesDate[0] != null)
				sql	+=	"	and	to_char(ds.sales_date, 'YYYY/MM/DD') >= " +
						SQLUtil.convertForSQLDateOnly(salesDate[0]) + "\n";
		if(salesDate[1] != null)
				sql	+=	"	and	to_char(ds.sales_date, 'YYYY/MM/DD') <= " +
						SQLUtil.convertForSQLDateOnly(salesDate[1]) + "\n";
		//伝票No.
		if(slipNO[0] != null)
				sql	+=	"	and	ds.slip_no >= " + SQLUtil.convertForSQL(slipNO[0]) + "\n";
		if(slipNO[1] != null)
				sql	+=	"	and	ds.slip_no <= " + SQLUtil.convertForSQL(slipNO[1]) + "\n";
		//顧客コード
		if (customerNo[0] != null) {
                    String s = customerNo[0];
                    if (!s.startsWith("0") && CheckUtil.isNumeric(s)) {
                        sql += " and translate(mc.customer_no, '0123456789', '') = ''" + "\n";
                        sql += " and mc.customer_no::text::numeric >= " + s + "\n";
                    } else {
                        sql += " and mc.customer_no >= '" + s + "'" + "\n";
                    }
                }
                
		if (customerNo[1] != null) {
                    String s = customerNo[1];
                    if (!s.startsWith("0") && CheckUtil.isNumeric(s)) {
                        sql += " and translate(mc.customer_no, '0123456789', '') = ''" + "\n";
                        sql += " and mc.customer_no::text::numeric <= " + s + "\n";
                    } else {
                        sql += " and mc.customer_no <= '" + s + "'" + "\n";
                    }
                }
                
		//スタッフ
		if(staff.getStaffID() != null && !staff.getStaffID().equals(""))
				sql	+=	"	and	dp.staff_id = " + SQLUtil.convertForSQL(staff.getStaffID()) + "\n";
		//主担当者
		if(chargeStaff.getStaffID() != null && ! chargeStaff.getStaffID().equals(""))
				sql	+=	"	and	ds.staff_id = " + SQLUtil.convertForSQL(chargeStaff.getStaffID()) + "\n";

		sql += ") t ";

                sql += " where true";
                
		//支払区分
		if (!getSearchPC().get(0)) {
			
			String temp = "";
			
			for(int i = 0; i < paymentClasses.size(); i ++)
			{
				MstPaymentClass	mpc = paymentClasses.get(i);
				
				if(getSearchPC().get(i + 1))
				{
					if(!temp.equals("")) temp += "or";
					temp +=	"	0 < " + "payment_value" + mpc.getPaymentClassID().toString() + "\n";
				}
			}
			
			if(!temp.equals("")) sql += " and (" + temp + ")\n";
		}

                //カルテ情報
                if (karteFlg != null) {
                    if (karteFlg.intValue() == 0) {
                        sql += " and " + karteCountSql + " = 0";
                    } else {
                        sql += " and " + karteCountSql + " > 0";
                    }
                }
                
		sql += " order by\n";
                
                
                switch (getSortIndex()) {
                    case 0:
                        sql += "  sales_date\n";
                        sql += " ,slip_no\n";
                        break;
                    case 1:
                        sql += "  reservation_no\n";
                        sql += " ,sales_date\n";
                        break;
                    case 2:
                        sql += "  slip_no\n";
                        sql += " ,sales_date\n";
                        break;
                }
                
		return	sql;
	}

}
