/*
 * ReservationStatus.java
 *
 * Created on 2006/06/12, 14:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.reservation;

import com.geobeck.sosia.pos.hair.data.account.ConsumptionCourse;
import com.geobeck.sosia.pos.hair.data.account.ConsumptionCourseClass;
import com.geobeck.sosia.pos.hair.data.account.Course;
import com.geobeck.sosia.pos.hair.data.account.CourseClass;
import com.geobeck.sosia.pos.hair.data.reservation.*;
import com.geobeck.sosia.pos.hair.master.product.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.customer.*;

import com.geobeck.sosia.pos.master.account.MstAccountSetting;
import com.geobeck.sosia.pos.master.system.MstSetting;
/**
 * 予約ステータス処理
 * @author katagiri
 */
public class ReservationStatus
{
	/**
	 * 店舗
	 */
	protected	MstShop				shop	=	new MstShop();
	/**
	 * 日付
	 */
	protected	GregorianCalendar	date	=	null;
	/**
	 * 状態
	 */
	protected	Integer				status	=	null;
	/**
	 * 副状態
	 */
	protected	Integer				subStatus	=	null;
	/**
	 * 予約者数
	 */
	protected	Integer				reservationCnt	=	null;
	/**
	 * 在店者数
	 */
	protected	Integer				zaitenCnt	=	null;
	/**
	 * 退店者数
	 */
	protected	Integer				taitenCnt	=	null;
	
	/**
	 * 予約データのリスト
	 */
	protected	ArrayList<DataReservation>	datas	=	new ArrayList<DataReservation>();
	
	/**
	 * 売上データのリスト
	 */
	protected	ArrayList<SalesData>		sales	=	new ArrayList<SalesData>();
	
	protected	ReservationComparator		reservationComparator	=	null;
	protected	SalesComparator				salesComparator			=	null;
	
	protected	SalesData					total	=	new SalesData();
	
	/**
	 * コンストラクタ
	 */
	public ReservationStatus()
	{
		this.setShop(SystemInfo.getCurrentShop());
		this.initComparator();
	}

	/**
	 * 店舗を取得する。
	 * @return 店舗
	 */
	public MstShop getShop()
	{
		return shop;
	}

	/**
	 * 店舗を設定する。
	 * @param shop 店舗
	 */
	public void setShop(MstShop shop)
	{
		this.shop = shop;
	}
	
	/**
	 * 予約者数を取得する。
	 * @return 予約者数
	 */
	public Integer getReservationCnt()
	{
		return reservationCnt;
	}

	/**
	 * 予約者数をセットする。
	 * @param reservationCnt 予約者数
	 */
	public void setReservationCnt(Integer reservationCnt)
	{
		this.reservationCnt = reservationCnt;
	}
	
	/**
	 * 在店者数を取得する。
	 * @return 在店者数
	 */
	public Integer getZaitenCnt()
	{
		return zaitenCnt;
	}

	/**
	 * 在店者数をセットする。
	 * @param zaitenCnt 在店者数
	 */
	public void setZaitenCnt(Integer zaitenCnt)
	{
		this.zaitenCnt = zaitenCnt;
	}
	
	/**
	 * 退店者数を取得する。
	 * @return 退店者数
	 */
	public Integer getTaitenCnt()
	{
		return taitenCnt;
	}

	/**
	 * 退店者数をセットする。
	 * @param taitenCnt 退店者数
	 */
	public void setTaitenCnt(Integer taitenCnt)
	{
		this.taitenCnt = taitenCnt;
	}
	
	/**
	 * 総客数を取得する。
	 * @return 総客数
	 */
	public Integer getTotalCnt()
	{
		return reservationCnt + zaitenCnt + taitenCnt;
	}
	
	/**
	 * 予約データのリストを取得する。
	 * @return 予約データのリスト
	 */
	public ArrayList<DataReservation> getDatas()
	{
		return datas;
	}

	/**
	 * 予約データのリストをセットする。
	 * @param datas 予約データのリスト
	 */
	public void setDatas(ArrayList<DataReservation> datas)
	{
		this.datas = datas;
	}

	/**
	 * 売上データのリストを取得する。
	 * @return 売上データのリスト
	 */
	public ArrayList<SalesData> getSales()
	{
		return sales;
	}

	/**
	 * 売上データのリストをセットする。
	 * @param sales 売上データのリスト
	 */
	public void setSales(ArrayList<SalesData> sales)
	{
		this.sales = sales;
	}

	public SalesData getTotal()
	{
		return total;
	}

	public void setTotal(SalesData total)
	{
		this.total = total;
	}
	
	public void setReservationSortCol(Integer col)
	{
		reservationComparator.setCol(col);
	}
	
	public void setSalesSortCol(Integer col)
	{
		salesComparator.setCol(col);
	}
	
	private void initComparator()
	{
		reservationComparator	=	new ReservationComparator();
                //予約データのデフォルトソートを予約時間に設定
		reservationComparator.setCol(4);
		
		salesComparator			=	new SalesComparator();
		salesComparator.setCol(0);
	}
	
	/**
	 * 状態(予約・在店・退店)ごとの客数を読み込む。
	 * @param loadDate 日付
	 */
	public void loadStatusCount(GregorianCalendar loadDate)
	{
                //状態ごとの客数をクリア
                setReservationCnt(0);
                setZaitenCnt(0);
                setTaitenCnt(0);
		
		if(loadDate == null || shop.getShopID() == null || shop.getShopID() == 0)	return;
		
		date	=	loadDate;
		date.set(date.HOUR_OF_DAY, shop.getOpenHour());
		date.set(date.MINUTE, shop.getOpenMinute());
                        
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			ResultSetWrapper	rs	=	con.executeQuery(this.getLoadStatusCountSQL());
			
			DataReservation		reservation	=	null;
			
			while(rs.next())
			{
                                int iStatus = rs.getInt("status");
				
                                switch(iStatus)
                                {
                                        //予約
                                        case 1:
                                                setReservationCnt(rs.getInt("status_count"));
                                                break;
                                        //在店
                                        case 2:
                                                setZaitenCnt(rs.getInt("status_count"));
                                                break;
                                        //退店
                                        case 3:
                                                setTaitenCnt(rs.getInt("status_count"));
                                                break;
                                }
			}
			
			rs.close();
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * 予約データを読み込む。
	 * @param loadDate 日付
	 * @param status 状態
	 */
	public void loadReservation(GregorianCalendar loadDate, int status)
        {
            loadReservation(loadDate, status, -1);
        }
	public void loadReservation(GregorianCalendar loadDate, int status, int staffID)
	{
		datas.clear();

		if(loadDate == null || shop.getShopID() == null || shop.getShopID() == 0)	return;
		
		date = loadDate;
		date.set(date.HOUR_OF_DAY, shop.getOpenHour());
		date.set(date.MINUTE, shop.getOpenMinute());
		
		this.status = status;
		
		try
		{
			ConnectionWrapper con = SystemInfo.getConnection();

			ResultSetWrapper rs = con.executeQuery(this.getLoadReservationSQL(staffID));
			
			DataReservation reservation = null;
			
			while(rs.next()) {

				boolean isNewReservation = true;
				
				if (reservation != null) {
                                    if (reservation.getReservationNo() == rs.getInt("reservation_no")) {
                                        isNewReservation = false;
                                    }
				}
				
				if (isNewReservation) {

					if (reservation != null) {
                                            datas.add(reservation);
					}
					
					reservation = new DataReservation();
					reservation.setReservationNo(rs.getInt("reservation_no"));
					
					MstShop	ms = new MstShop();
					ms.setShopID(rs.getInt("shop_id"));
					reservation.setShop(ms);
					
					MstCustomer mc = new MstCustomer();
					mc.setCustomerID(rs.getInt("customer_id"));
					mc.setCustomerNo(rs.getString("customer_no"));
					mc.setCustomerKana(0, rs.getString("customer_kana1"));
					mc.setCustomerKana(1, rs.getString("customer_kana2"));
					mc.setCustomerName(0, rs.getString("customer_name1"));
					mc.setCustomerName(1, rs.getString("customer_name2"));
                                        //誕生日
                                        if ((rs.getDate("birthday")) != null) {
                                            String sBirthday = String.format("%1$tY/%1$tm/%1$td", rs.getDate("birthday"));
                                            mc.setBirthdayDate(DateUtil.toDate(sBirthday));
                                        }
                                        //IVS_LVTu start add 2015/09/07 New request #42428
					mc.setCellularNumber(rs.getString("cellular_number"));
                                        mc.setPhoneNumber(rs.getString("phone_number"));
                                        //IVS_LVTu end add 2015/09/07 New request #42428
                                        mc.setVisitCount(rs.getLong("visit_count"));
                                        mc.setRank(rs.getString("rank"));
					reservation.setCustomer(mc);
					
					reservation.setDesignated(rs.getBoolean("designated_flag"));
                                        MstStaff staff = new MstStaff();
                                        staff.setData( rs );
                                        reservation.setStaff( staff );
					
					reservation.setTotalTime(rs.getInt("total_time"));
					reservation.setStatus(rs.getInt("status"));
					reservation.setSubStatus(rs.getInt("sub_status"));
                                        reservation.setSlipNo(rs.getInt("slip_no"));
                                        reservation.setComment(rs.getString("comment"));
                                        
					if(rs.getTimestamp("visit_time") != null)
					{
						reservation.setVisitTime(rs.getTimestamp("visit_time"));
					}
					if(rs.getTimestamp("start_time") != null)
					{
						reservation.setStartTime(rs.getTimestamp("start_time"));
					}
					if(rs.getTimestamp("leave_time") != null)
					{
						reservation.setLeaveTime(rs.getTimestamp("leave_time"));
					}
                                        
                                        reservation.setTechnicClassContractedNameList(rs.getString("technic_class_contracted_nameList"));
				}
				
				DataReservationDetail drd = new DataReservationDetail();
				
				drd.setReservation(reservation);
				drd.setReservationDate(rs.getTimestamp("reservation_datetime"));
				
                                Object courseFlg = rs.getObject("course_flg");
                                if (courseFlg == null) {
                                    //技術
                                    MstTechnic technic = new MstTechnic();
                                    technic.setTechnicID(rs.getInt("technic_id"));
                                    technic.setTechnicNo(rs.getString("technic_no"));
                                    technic.setTechnicName(rs.getString("technic_name"));
                                    technic.setOperationTime(rs.getInt("operation_time"));

                                    MstTechnicClass technicClass = new MstTechnicClass();
                                    technicClass.setTechnicClassID( rs.getInt("technic_class_id") );
                                    technicClass.setTechnicClassContractedName(rs.getString("technic_class_contracted_name"));
                                    technic.setTechnicClass( technicClass );
                                    drd.setTechnic(technic);


                                } else if ((Integer)courseFlg == 1)  {
                                    //コース契約
                                    CourseClass courseClass = new CourseClass();
                                    courseClass.setCourseClassId(rs.getInt("technic_class_id"));
                                    courseClass.setCourseClassName(rs.getString("technic_class_contracted_name"));

                                    Course course = new Course();
                                    course.setCourseClass(courseClass);
                                    course.setCourseId(rs.getInt("technic_id"));
                                    course.setCourseName(rs.getString("technic_name"));
                                    course.setOperationTime(rs.getInt("operation_time"));

                                    drd.setCourse(course);
                                    drd.setCourseFlg(1);
                                } else if ((Integer)courseFlg == 2) {
                                    //消化コース
                                    ConsumptionCourseClass consumptionCourseClass = new ConsumptionCourseClass();
                                    consumptionCourseClass.setCourseClassId(rs.getInt("technic_class_id"));
                                    consumptionCourseClass.setCourseClassName(rs.getString("technic_class_contracted_name"));

                                    ConsumptionCourse consumptionCourse = new ConsumptionCourse();
                                    consumptionCourse.setConsumptionCourseClass(consumptionCourseClass);
                                    consumptionCourse.setCourseId(rs.getInt("technic_id"));
                                    consumptionCourse.setCourseName(rs.getString("technic_name"));
                                    consumptionCourse.setOperationTime(rs.getInt("operation_time"));

                                    drd.setConsumptionCourse(consumptionCourse);
                                    drd.setCourseFlg(2);
                                }
				
				reservation.add(drd);
			}
			
			rs.close();
			
			if (reservation != null) {
                            datas.add(reservation);
			}
			
			Collections.sort(datas, reservationComparator);
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * 売上データを読み込む。
	 * @param loadDate 日付
	 */
	public void loadSales(GregorianCalendar loadDate, int staffID)
	{       

		sales.clear();
		total.clearValue();
		
		if(loadDate == null)	return;
		
		date	=	loadDate;
                
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();

			ResultSetWrapper	rs	=	con.executeQuery(this.getLoadSalesSQL(staffID));

			long tax;
			MstAccountSetting ma = new MstAccountSetting();
			ma.load(SystemInfo.getConnection());
			
			Double taxRate = SystemInfo.getTaxRate(SystemInfo.getSystemDate());
			
			while(rs.next())
			{
				SalesData	sd	=	new SalesData();
				MstShop	ms	=	new MstShop();
				ms.setShopID(rs.getInt("shop_id"));
				sd.setShop(ms);
				sd.setSlipNo(rs.getInt("slip_no"));
				MstCustomer	mc	=	new MstCustomer();
                                
				sd.setDesignated(rs.getBoolean("designated_flag"));
				MstStaff staff = new MstStaff();
//				staff.setData(rs);
//                                if(staff.getStaffName(0) == null){
//                                    staff.setStaffName(0, "");
//                                }
//                                if(staff.getStaffName(1) == null){
//                                    staff.setStaffName(1, "");
//                                }
				staff.setStaffName(0, rs.getString("staff_name1"));
				staff.setStaffName(1, rs.getString("staff_name2"));
				sd.setStaff( staff );

				mc.setCustomerID(rs.getInt("customer_id"));
				mc.setCustomerNo(rs.getString("customer_no"));
				mc.setCustomerKana(0, rs.getString("customer_kana1"));
				mc.setCustomerKana(1, rs.getString("customer_kana2"));
				mc.setCustomerName(0, rs.getString("customer_name1"));
				mc.setCustomerName(1, rs.getString("customer_name2"));
                                mc.setVisitCount(rs.getLong("visit_count"));
				sd.setCustomer(mc);
//				sd.setTechnic(rs.getLong("technic"));
//				sd.setItem(rs.getLong("item"));
//				sd.setTechnic(ma.getDisplayValue(rs.getLong("technic"),taxRate));
//				sd.setItem(ma.getDisplayValue(rs.getLong("item"),taxRate));
				sd.setTechnic(rs.getLong("technic"));
				sd.setItem(rs.getLong("item"));
                                sd.setValueCourse(rs.getLong("course_value"));
				sd.setOverallDiscount( rs.getLong( "overall_discount" ) );
				sd.setDiscount(rs.getLong("discount"));
//				sd.setTax(rs.getLong("tax"));

				sd.setTotalValue(rs.getLong("total_value"));
				
//				tax = ma.getTax(rs.getLong("technic")+rs.getLong("item"),rs.getLong( "overall_discount" )+rs.getLong("discount"),taxRate);
//				tax = (long)Math.floor((rs.getLong("technic")+rs.getLong("item")) * taxRate);
//				sd.setTax(tax);

				sd.setTax(rs.getLong("tax"));
				
				sales.add(sd);
				
				total.addValue(sd);
			}
			
			rs.close();

//ソート機能未実装のため、レスポンス向上するのにコメント
//			Collections.sort(sales, salesComparator);
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	
	/**
	 * 在店処理を行う。
	 * @param indexes 予約データのインデックスの配列
	 * @return true - 成功
	 */
	public boolean changeReservationToStay(int[] indexes)
	{
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			try
			{
				con.begin();

				for(int i = 0; i < indexes.length; i ++)
				{
					datas.get(indexes[i]).setStatus(2);
					datas.get(indexes[i]).setSubStatus(1);
					
					if(!datas.get(indexes[i]).updateStatus(con))
					{
						con.rollback();
						return	false;
					}
				}

				con.commit();
			}
			catch(SQLException e)
			{
				con.rollback();
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
				return	false;
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			return	false;
		}
		
		return true;
	}
	
	
	/**
	 * 在店取消処理を行う。
	 * @param indexes 予約データのインデックスの配列
	 * @return true - 成功
	 */
	public boolean cancelStay(int[] indexes)
	{
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			try
			{
				con.begin();

				for(int i = 0; i < indexes.length; i ++)
				{
					datas.get(indexes[i]).setStatus(1);
					
					if(!datas.get(indexes[i]).updateStatus(con))
					{
						con.rollback();
						return	false;
					}
				}

				con.commit();
			}
			catch(SQLException e)
			{
				con.rollback();
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
				return	false;
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			return	false;
		}
		
		return true;
	}
	
	/**
	 * 施術開始処理を行う。
	 * @param indexes 在店データのインデックスの配列
	 * @return true - 成功
	 */
	public boolean changeStayToStart(int[] indexes)
	{
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			try
			{
				con.begin();

				for(int i = 0; i < indexes.length; i ++)
				{
					datas.get(indexes[i]).setStatus(2);
					datas.get(indexes[i]).setSubStatus(2);
					
					if(!datas.get(indexes[i]).updateStatus(con))
					{
						con.rollback();
						return	false;
					}
				}

				con.commit();
			}
			catch(SQLException e)
			{
				con.rollback();
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
				return	false;
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			return	false;
		}
		
		return true;
	}
	
	
	/**
	 * 在店取消処理を行う。
	 * @param indexes 予約データのインデックスの配列
	 * @return true - 成功
	 */
	public boolean cancelStart(int[] indexes)
	{
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			try
			{
				con.begin();

				for(int i = 0; i < indexes.length; i ++)
				{
					datas.get(indexes[i]).setStatus(2);
					datas.get(indexes[i]).setSubStatus(1);
					
					if(!datas.get(indexes[i]).updateStatus(con))
					{
						con.rollback();
						return	false;
					}
				}

				con.commit();
			}
			catch(SQLException e)
			{
				con.rollback();
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
				return	false;
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			return	false;
		}
		
		return true;
	}
	
	/**
	 * 予約削除処理を行う。
	 * @param indexes 予約データのインデックスの配列
	 * @return true - 成功
	 */
	public boolean deleteReservation(int[] indexes)
	{
		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			try
			{
				con.begin();

				for(int i = 0; i < indexes.length; i ++)
				{
					if(!datas.get(indexes[i]).delete(con))
					{
						con.rollback();
						return	false;
					}
				}

				con.commit();
			}
			catch(SQLException e)
			{
				con.rollback();
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
				return	false;
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			return false;
		}
		
		return true;
	}
	
	/**
	 * 状態(予約・在店・退店)ごとの客数を取得するＳＱＬ文を取得する。
	 * @return 状態(予約・在店・退店)ごとの客数を取得するＳＱＬ文
	 */
	private String getLoadStatusCountSQL()
	{
		GregorianCalendar	dateTemp	=	new GregorianCalendar();
		dateTemp.setTime(date.getTime());
		dateTemp.set(Calendar.HOUR_OF_DAY, shop.getOpenHour());
		dateTemp.set(Calendar.MINUTE, shop.getOpenMinute());
		GregorianCalendar	nextDate	=	new GregorianCalendar();
		nextDate.setTime(dateTemp.getTime());
		nextDate.add(nextDate.DAY_OF_MONTH, 1);
		
		return	String.format(
				"select status, count(*) as status_count\n" +
				"from (\n" +
				"   select distinct dr.shop_id, dr.reservation_no, dr.status\n" +
				"   from data_reservation dr\n" +
				"   inner join data_reservation_detail drd\n" +
				"   on drd.shop_id = dr.shop_id \n" +
				"   and drd.reservation_no = dr.reservation_no\n" +
				"   and drd.delete_date is null\n" +
				"   where dr.delete_date is null\n" +
				"   and dr.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"   and drd.reservation_datetime >= '%1$tY/%1$tm/%1$td %1$tH:%1$tM:00'\n" +
				"   and drd.reservation_datetime < '%2$tY/%2$tm/%2$td %2$tH:%2$tM:00'\n" +
				") temp\n" +
				"group by status\n" +
				"order by status\n", dateTemp, nextDate);
	}
	
	/**
	 * 予約データを取得するＳＱＬ文を取得する。
	 * @return 予約データを取得するＳＱＬ文
	 */
	private String getLoadReservationSQL(int staffID)
	{
		GregorianCalendar dateTemp = new GregorianCalendar();
		dateTemp.setTime(date.getTime());
		dateTemp.set(Calendar.HOUR_OF_DAY, shop.getOpenHour());
		dateTemp.set(Calendar.MINUTE, shop.getOpenMinute());
		GregorianCalendar nextDate = new GregorianCalendar();
		nextDate.setTime(dateTemp.getTime());
		nextDate.add(nextDate.DAY_OF_MONTH, 1);
		
                StringBuilder sql = new StringBuilder(1000);
                //技術 ここから--------------------------------------
                sql.append(" select");
                sql.append("      dr.shop_id");
                sql.append("     ,dr.reservation_no");
                sql.append("     ,dr.customer_id");
                sql.append("     ,mc.customer_no");
                sql.append("     ,mc.customer_kana1");
                sql.append("     ,mc.customer_kana2");
                sql.append("     ,mc.customer_name1");
                sql.append("     ,mc.customer_name2");
                sql.append("     ,mc.birthday");
                //IVS_LVTu start edit 2015/09/07 New request #42428
                sql.append("     ,mc.phone_number");
                sql.append("     ,mc.cellular_number");
                sql.append("     ,get_visit_count(mc.customer_id, " + SQLUtil.convertForSQL(this.getShop().getShopID()) + ", " + SQLUtil.convertForSQL(date) + ") as visit_count");
                sql.append("     ,drd.technic_id");
                sql.append("     ,mt.technic_no");
                sql.append("     ,mt.technic_name");
                sql.append("     ,coalesce(drd.operation_time, mt.operation_time) as operation_time");
                sql.append("     ,mtc.technic_class_id");
                sql.append("     ,mtc.technic_class_contracted_name");
                sql.append("     ,drd.reservation_datetime");
                sql.append("     ,ms.*");
                sql.append("     ,dr.designated_flag");
                sql.append("     ,dr.staff_id");
                sql.append("     ,dr.total_time");
                sql.append("     ,dr.visit_time");
                sql.append("     ,dr.start_time");
                sql.append("     ,dr.leave_time");
                sql.append("     ,dr.status");
                sql.append("     ,dr.slip_no");
                sql.append("     ,dr.comment");
                sql.append("     ,dr.sub_status");
                sql.append("     ,null as course_flg");
                sql.append("     ,array(");
                sql.append("         select");
                sql.append("             case when (mt.technic_no like 'mo-rsv-%') or (mt.mobile_flag=1) ");
                sql.append("                 then technic_name");
                sql.append("                 else mtc.technic_class_contracted_name");
                sql.append("             end as technic_class_contracted_name");
                sql.append("         from");
                sql.append("             data_sales_detail dsd");
                sql.append("                 inner join mst_technic mt");
                sql.append("                         on dsd.product_id = mt.technic_id");
                sql.append("                        and dsd.product_division in (1,3)");
                sql.append("                 inner join mst_technic_class mtc");
                sql.append("                         on mt.technic_class_id = mtc.technic_class_id");
                sql.append("         where");
                sql.append("             dsd.delete_date is null");
                sql.append("         and dsd.shop_id = dr.shop_id");
                sql.append("         and dsd.slip_no = dr.slip_no");
                sql.append("      ) as technic_class_contracted_nameList");

                // 顧客ランク
                MstSetting setting = SystemInfo.getSetteing();
                if (setting.getRankStartDate() != null && setting.getRankEndDate() != null) {
                    sql.append(" ,(");
                    sql.append("     select");
                    sql.append("         (");
                    sql.append("             select");
                    sql.append("                 rank_name");
                    sql.append("             from");
                    sql.append("                 mst_customer_rank_setting");
                    sql.append("             where");
                    sql.append("                 id =");
                    sql.append("                     (");
                    sql.append("                         select");
                    sql.append("                             min(id)");
                    sql.append("                         from");
                    sql.append("                             mst_customer_rank_setting");
                    sql.append("                         where");
                    sql.append("                                 t.count >= count_from");
                    sql.append("                             and t.value >= value_from");
                    sql.append("                     )");
                    sql.append("         ) as rank");
                    sql.append("     from");
                    sql.append("         (");
                    sql.append("             select");
                    sql.append("                  count(*) as count");
                    if (SystemInfo.getSetteing().getRankTaxType() == 0) {
                        // 税込み
                        sql.append("             ,sum(discount_sales_value_in_tax) as value");
                    } else {
                        // 税抜き
                        sql.append("             ,sum(discount_sales_value_no_tax) as value");
                    }
                    sql.append("             from");
                    sql.append("                 view_data_sales_valid");
                    sql.append("             where");
                    sql.append("                     sales_date between " + SQLUtil.convertForSQLDateOnly(setting.getRankStartDate()));
                    sql.append("                                    and " + SQLUtil.convertForSQLDateOnly(setting.getRankEndDate()));
                    sql.append("                 and customer_id = mc.customer_id");
                    sql.append("         ) t");
                    sql.append(" ) as rank");
                    
                } else {
                    sql.append(" ,null as rank");
                }

                sql.append(" from");
                sql.append("     data_reservation dr");
                sql.append("         inner join data_reservation_detail drd");
                sql.append("                 on drd.shop_id = dr.shop_id");
                sql.append("                and drd.reservation_no = dr.reservation_no");
                sql.append("                and drd.delete_date is null");
                sql.append("         left join mst_customer mc");
                sql.append("                on mc.customer_id = dr.customer_id");
                sql.append("         left join mst_technic mt");
                sql.append("                on mt.technic_id = drd.technic_id");
                sql.append("         left join mst_technic_class mtc");
                sql.append("                on mtc.technic_class_id = mt.technic_class_id");
                sql.append("         left join mst_staff ms");
                sql.append("                on ms.staff_id = dr.staff_id");
                sql.append(" where");
                sql.append("         dr.delete_date is null");
                sql.append("     and dr.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
                sql.append("     and drd.reservation_datetime >= " + SQLUtil.convertForSQL(dateTemp));
                sql.append("     and drd.reservation_datetime < " + SQLUtil.convertForSQL(nextDate));
                sql.append("     and drd.course_flg is null");
                if (status == 1) {
                    sql.append(" and dr.status in (1)");
                } else {
                    sql.append(" and dr.status in (2, 3)");
                }

                if (staffID >= 0) {
                    sql.append(" and dr.staff_id = " + SQLUtil.convertForSQL(staffID));
                }
                //技術 ここまで--------------------------------------



                //コース契約　ここから-----------------------------------
                sql.append(" union all");
                sql.append(" select");
                sql.append("      dr.shop_id");
                sql.append("     ,dr.reservation_no");
                sql.append("     ,dr.customer_id");
                sql.append("     ,mc.customer_no");
                sql.append("     ,mc.customer_kana1");
                sql.append("     ,mc.customer_kana2");
                sql.append("     ,mc.customer_name1");
                sql.append("     ,mc.customer_name2");
                sql.append("     ,mc.birthday");
                sql.append("     ,mc.phone_number");
                sql.append("     ,mc.cellular_number");
                sql.append("     ,get_visit_count(mc.customer_id, " + SQLUtil.convertForSQL(this.getShop().getShopID()) + ", " + SQLUtil.convertForSQL(date) + ") as visit_count");
                sql.append("     ,drd.technic_id");
                sql.append("     ,null as technic_no");
                sql.append("     ,c.course_name as technic_name");
                sql.append("     ,coalesce(drd.operation_time, c.operation_time) as operation_time");
                sql.append("     ,cc.course_class_id as technic_class_id");
                sql.append("     ,cc.course_class_contracted_name as technic_class_contracted_name");
                sql.append("     ,drd.reservation_datetime");
                sql.append("     ,ms.*");
                sql.append("     ,dr.designated_flag");
                sql.append("     ,dr.staff_id");
                sql.append("     ,dr.total_time");
                sql.append("     ,dr.visit_time");
                sql.append("     ,dr.start_time");
                sql.append("     ,dr.leave_time");
                sql.append("     ,dr.status");
                sql.append("     ,dr.slip_no");
                sql.append("     ,dr.comment");
                sql.append("     ,dr.sub_status");
                sql.append("     ,1 as course_flg");
                sql.append("     ,array(");
                sql.append("         select");
                sql.append("             cc.course_class_contracted_name as technic_class_contracted_name");
                sql.append("         from");
                sql.append("             data_sales_detail dsd");
                sql.append("                 inner join mst_course c");
                sql.append("                         on dsd.product_id = c.course_id");
                sql.append("                        and dsd.product_division = 5");
                sql.append("                 inner join mst_course_class cc");
                sql.append("                         on c.course_class_id = cc.course_class_id");
                sql.append("         where");
                sql.append("             dsd.delete_date is null");
                sql.append("         and dsd.shop_id = dr.shop_id");
                sql.append("         and dsd.slip_no = dr.slip_no");
                sql.append("      ) as technic_class_contracted_nameList");

                // 顧客ランク
                if (setting.getRankStartDate() != null && setting.getRankEndDate() != null) {
                    sql.append(" ,(");
                    sql.append("     select");
                    sql.append("         (");
                    sql.append("             select");
                    sql.append("                 rank_name");
                    sql.append("             from");
                    sql.append("                 mst_customer_rank_setting");
                    sql.append("             where");
                    sql.append("                 id =");
                    sql.append("                     (");
                    sql.append("                         select");
                    sql.append("                             min(id)");
                    sql.append("                         from");
                    sql.append("                             mst_customer_rank_setting");
                    sql.append("                         where");
                    sql.append("                                 t.count >= count_from");
                    sql.append("                             and t.value >= value_from");
                    sql.append("                     )");
                    sql.append("         ) as rank");
                    sql.append("     from");
                    sql.append("         (");
                    sql.append("             select");
                    sql.append("                  count(*) as count");
                    if (SystemInfo.getSetteing().getRankTaxType() == 0) {
                        // 税込み
                        sql.append("             ,sum(discount_sales_value_in_tax) as value");
                    } else {
                        // 税抜き
                        sql.append("             ,sum(discount_sales_value_no_tax) as value");
                    }
                    sql.append("             from");
                    sql.append("                 view_data_sales_valid");
                    sql.append("             where");
                    sql.append("                     sales_date between " + SQLUtil.convertForSQLDateOnly(setting.getRankStartDate()));
                    sql.append("                                    and " + SQLUtil.convertForSQLDateOnly(setting.getRankEndDate()));
                    sql.append("                 and customer_id = mc.customer_id");
                    sql.append("         ) t");
                    sql.append(" ) as rank");

                } else {
                    sql.append(" ,null as rank");
                }

                sql.append(" from");
                sql.append("     data_reservation dr");
                sql.append("         inner join data_reservation_detail drd");
                sql.append("                 on drd.shop_id = dr.shop_id");
                sql.append("                and drd.reservation_no = dr.reservation_no");
                sql.append("                and drd.delete_date is null");
                sql.append("         left join mst_customer mc");
                sql.append("                on mc.customer_id = dr.customer_id");
                sql.append("         left join mst_course c");
                sql.append("                on c.course_id = drd.technic_id");
                sql.append("         left join mst_course_class cc");
                sql.append("                on cc.course_class_id = c.course_class_id");
                sql.append("         left join mst_staff ms");
                sql.append("                on ms.staff_id = dr.staff_id");
                sql.append(" where");
                sql.append("         dr.delete_date is null");
                sql.append("     and dr.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
                sql.append("     and drd.reservation_datetime >= " + SQLUtil.convertForSQL(dateTemp));
                sql.append("     and drd.reservation_datetime < " + SQLUtil.convertForSQL(nextDate));
                sql.append("     and drd.course_flg = 1");
                if (status == 1) {
                    sql.append(" and dr.status in (1)");
                } else {
                    sql.append(" and dr.status in (2, 3)");
                }

                if (staffID >= 0) {
                    sql.append(" and dr.staff_id = " + SQLUtil.convertForSQL(staffID));
                }
                //コース契約　ここまで-----------------------------------




                //消化コース　ここから-----------------------------------
                sql.append(" union all");
                sql.append(" select distinct");
                sql.append("      dr.shop_id");
                sql.append("     ,dr.reservation_no");
                sql.append("     ,dr.customer_id");
                sql.append("     ,mc.customer_no");
                sql.append("     ,mc.customer_kana1");
                sql.append("     ,mc.customer_kana2");
                sql.append("     ,mc.customer_name1");
                sql.append("     ,mc.customer_name2");
                sql.append("     ,mc.birthday");
                sql.append("     ,mc.phone_number");
                sql.append("     ,mc.cellular_number");
                //IVS_LVTu end edit 2015/09/07 New request #42428
                sql.append("     ,get_visit_count(mc.customer_id, " + SQLUtil.convertForSQL(this.getShop().getShopID()) + ", " + SQLUtil.convertForSQL(date) + ") as visit_count");
                sql.append("     ,drd.technic_id");
                sql.append("     ,null as technic_no");
                sql.append("     ,c.course_name as technic_name");
                sql.append("     ,coalesce(drd.operation_time, c.operation_time) as operation_time");
                sql.append("     ,cc.course_class_id as technic_class_id");
                sql.append("     ,cc.course_class_contracted_name as technic_class_contracted_name");
                sql.append("     ,drd.reservation_datetime");
                sql.append("     ,ms.*");
                sql.append("     ,dr.designated_flag");
                sql.append("     ,dr.staff_id");
                sql.append("     ,dr.total_time");
                sql.append("     ,dr.visit_time");
                sql.append("     ,dr.start_time");
                sql.append("     ,dr.leave_time");
                sql.append("     ,dr.status");
                sql.append("     ,dr.slip_no");
                sql.append("     ,dr.comment");
                sql.append("     ,dr.sub_status");
                sql.append("     ,2 as course_flg");
                sql.append("     ,array(");
                sql.append("         select");
                sql.append("             cc.course_class_contracted_name as technic_class_contracted_name");
                sql.append("         from");
                sql.append("             data_sales_detail dsd");
                sql.append("                 inner join mst_course c");
                sql.append("                         on dsd.product_id = c.course_id");
                sql.append("                        and dsd.product_division = 6");
                sql.append("                 inner join mst_course_class cc");
                sql.append("                         on c.course_class_id = cc.course_class_id");
                sql.append("         where");
                sql.append("             dsd.delete_date is null");
                sql.append("         and dsd.shop_id = dr.shop_id");
                sql.append("         and dsd.slip_no = dr.slip_no");
                sql.append("      ) as technic_class_contracted_nameList");

                // 顧客ランク
                if (setting.getRankStartDate() != null && setting.getRankEndDate() != null) {
                    sql.append(" ,(");
                    sql.append("     select");
                    sql.append("         (");
                    sql.append("             select");
                    sql.append("                 rank_name");
                    sql.append("             from");
                    sql.append("                 mst_customer_rank_setting");
                    sql.append("             where");
                    sql.append("                 id =");
                    sql.append("                     (");
                    sql.append("                         select");
                    sql.append("                             min(id)");
                    sql.append("                         from");
                    sql.append("                             mst_customer_rank_setting");
                    sql.append("                         where");
                    sql.append("                                 t.count >= count_from");
                    sql.append("                             and t.value >= value_from");
                    sql.append("                     )");
                    sql.append("         ) as rank");
                    sql.append("     from");
                    sql.append("         (");
                    sql.append("             select");
                    sql.append("                  count(*) as count");
                    if (SystemInfo.getSetteing().getRankTaxType() == 0) {
                        // 税込み
                        sql.append("             ,sum(discount_sales_value_in_tax) as value");
                    } else {
                        // 税抜き
                        sql.append("             ,sum(discount_sales_value_no_tax) as value");
                    }
                    sql.append("             from");
                    sql.append("                 view_data_sales_valid");
                    sql.append("             where");
                    sql.append("                     sales_date between " + SQLUtil.convertForSQLDateOnly(setting.getRankStartDate()));
                    sql.append("                                    and " + SQLUtil.convertForSQLDateOnly(setting.getRankEndDate()));
                    sql.append("                 and customer_id = mc.customer_id");
                    sql.append("         ) t");
                    sql.append(" ) as rank");

                } else {
                    sql.append(" ,null as rank");
                }

                sql.append(" from");
                sql.append("     data_reservation dr");
                sql.append("         inner join data_reservation_detail drd");
                sql.append("                 on drd.shop_id = dr.shop_id");
                sql.append("                and drd.reservation_no = dr.reservation_no");
                sql.append("                and drd.delete_date is null");
                sql.append("         left join mst_customer mc");
                sql.append("                on mc.customer_id = dr.customer_id");
                sql.append("         left join data_contract_digestion dcd");
                sql.append("                on drd.shop_id = dcd.shop_id and drd.contract_no = dcd.contract_no and drd.contract_detail_no = dcd.contract_detail_no");
                sql.append("         left join mst_course c");
                sql.append("                on c.course_id = drd.technic_id");
                sql.append("         left join mst_course_class cc");
                sql.append("                on cc.course_class_id = c.course_class_id");
                sql.append("         left join mst_staff ms");
                sql.append("                on ms.staff_id = dr.staff_id");
                sql.append(" where");
                sql.append("         dr.delete_date is null");
                sql.append("     and dr.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
                sql.append("     and drd.reservation_datetime >= " + SQLUtil.convertForSQL(dateTemp));
                sql.append("     and drd.reservation_datetime < " + SQLUtil.convertForSQL(nextDate));
                sql.append("     and drd.course_flg = 2");
                if (status == 1) {
                    sql.append(" and dr.status in (1)");
                } else {
                    sql.append(" and dr.status in (2, 3)");
                }

                if (staffID >= 0) {
                    sql.append(" and dr.staff_id = " + SQLUtil.convertForSQL(staffID));
                }
                //消化コース　ここまで-----------------------------------

                sql.append(" order by");
                sql.append("      reservation_no");
                sql.append("     ,status");
                sql.append("     ,reservation_datetime");

                return sql.toString();
	}
	
	/**
	 * 売上データを取得するＳＱＬ文を取得する。
	 * @return 売上データを取得するＳＱＬ文
	 */
	private String getLoadSalesSQL(int staffID)
	{
		String shopId = SQLUtil.convertForSQL(this.getShop().getShopID());

		StringBuilder sql = new StringBuilder(1000);
                // start edit 2013-10-30 Hoa
                // vtbphuong start add 20140926 Bug #30956
                 // vtbphuong start add 20140926 Bug #30956
                    sql.append("SELECT          a.shop_id , \n");
                 sql.append("a.slip_no , \n");
                 sql.append("a.customer_id ,\n");
                 sql.append(" a.staff_id ,\n");
                 sql.append("a.designated_flag ,\n");
                 sql.append(" a.visit_count ,\n");
                 sql.append("a.staff_name1 ,\n");
                 sql.append(" a.staff_name2 ,\n");
                 sql.append("a.customer_no ,\n");
                 sql.append("a.customer_kana1 ,\n");
                 sql.append("a.customer_kana2 ,\n");
                 sql.append("a.customer_name1 ,\n");
                 sql.append("a.customer_name2 ,\n");
                 sql.append("a.technic ,\n");
                 sql.append("a.item ,\n");
                 //IVS_LVTu start edit 2015/10/05 New request #43048
                 sql.append("a.course_value ,\n");
                 sql.append("a.discount ,\n");
                 sql.append("a.overall_discount ,\n");
                 sql.append("CASE\n");
                 sql.append("  WHEN a.overall_discount = 0::numeric THEN a.total_value \n");
                 sql.append("WHEN a.overall_discount > 0 THEN a.total_value -  overall_discount  \n");
                 sql.append("ELSE NULL::numeric \n");
                 sql.append("END AS total_value,   \n");
                 sql.append("CASE \n");
                 sql.append("   WHEN a.overall_discount = 0::numeric THEN a.tax \n");
                 sql.append(" WHEN a.overall_discount > 0 THEN a.tax - (  overall_discount -  trunc( ceil((a.overall_discount) / (1.0 + get_tax_rate(a.sales_date))))  )  \n");
                 sql.append("ELSE NULL::numeric \n");
                 sql.append("END AS tax \n");
                 sql.append("FROM ( \n");
        
		sql.append(" select  distinct " + "\n");
                // end edit 2013-10-30 Hoa
		sql.append("      a.shop_id" + "\n");
		sql.append("     ,a.slip_no , b.sales_date  " + "\n");
		sql.append("     ,b.customer_id" + "\n");
		sql.append("     ,b.staff_id" + "\n");
		sql.append("     ,b.designated_flag" + "\n");
		sql.append("     ,get_visit_count(b.customer_id, " + shopId + ", " + SQLUtil.convertForSQL(date) + ") as visit_count" + "\n");
		sql.append("     ,ms.staff_name1" + "\n");
		sql.append("     ,ms.staff_name2" + "\n");
		sql.append("     ,mc.customer_no" + "\n");
		sql.append("     ,mc.customer_kana1" + "\n");
		sql.append("     ,mc.customer_kana2" + "\n");
		sql.append("     ,mc.customer_name1" + "\n");
		sql.append("     ,mc.customer_name2" + "\n");
		sql.append("     ,a.technic" + "\n");
		sql.append("     ,a.item" + "\n");
                sql.append("     ,a.course_value \n");
		sql.append("     ,a.discount" + "\n");
                // start delete 2013-10-30 Hoa
                // start add 20130807 nakhoa
                //sql.append("     ,a.product_division" + "\n");
                // end add 20130807 nakhoa
                // end delete 2013-10-30 Hoa
		sql.append("     ,b.discount_value as overall_discount" + "\n");
		//sql.append("     ,b.discount_sales_value_in_tax - b.discount_sales_value_no_tax as tax" + "\n");
                sql.append(" ,   a.tax  as tax \n ") ; 
		//sql.append("     ,b.discount_sales_value_in_tax as total_value" + "\n");
                sql.append("    ,a.total_value as total_value \n ");
		sql.append(" from" + "\n");
		sql.append("     (" + "\n");
                // start edit 2013-10-30 Hoa
		sql.append("         select  distinct " + "\n");
                // end edit 2013-10-30 Hoa
		sql.append("              shop_id" + "\n");
                // start delete 2013-10-30 Hoa
                // start add 20130807 nakhoa
                //sql.append("             ,product_division" + "\n");
                // end add 20130807 nakhoa
                // start delete 2013-10-30 Hoa
                sql.append("             ,slip_no" + "\n");
		sql.append("             ,sum(case when product_division in (1,3) then discount_detail_value_no_tax else 0 end) as technic" + "\n");
		sql.append("             ,sum(case when product_division in (2,4) then discount_detail_value_no_tax else 0 end) as item" + "\n");
                sql.append("             ,sum(case when product_division in (5) then discount_detail_value_no_tax else 0 end) as course_value" + "\n");
                //IVS_LVTu end edit 2015/10/05 New request #43048
		sql.append("             ,sum(discount_value) as discount" + "\n");
                //Luc start add 20150714 #40178
		//sql.append("             ,sum(discount_detail_value_in_tax - discount_detail_value_no_tax) as tax" + "\n");
		//sql.append("             ,sum(discount_detail_value_in_tax) as total_value" + "\n");
                
                //IVS_TMTrong start edit 20150716 Bug #37392
                sql.append("            ,sum(" );
                sql.append("                    CASE WHEN a.product_division = 7 THEN ");
                sql.append("                    ( 0");
//                sql.append("                        floor(get_tax_rate(sales_date) * (SELECT sum(dpd.payment_value) AS payment_value1 ");
//                sql.append("                                                           FROM data_payment_detail dpd ");
//                sql.append("                                                           INNER JOIN mst_payment_method mpm ON mpm.payment_method_id = dpd.payment_method_id ");
//                sql.append("                                                            AND mpm.payment_class_id IN (1, 2, 3, 4) ");
//                sql.append("                                                            WHERE true ");
//                sql.append("                                                            AND dpd.payment_no = 0 ");
//                sql.append("                                                            AND shop_id = a.shop_id ");
//                sql.append("                                                            AND slip_no = a.slip_no)) ");
                sql.append("                       )");
                sql.append("                      ELSE(");
                sql.append("                            case when ac.discount_type = 0 then\n");
                //Luc start add 20150724 #40579
                sql.append("                                case when discount_value != 0 then \n");
                //Luc start add 20150724 #40579
                sql.append("                                floor(detail_value_no_tax* ( 1+ get_tax_rate(sales_date)) - discount_value  - (detail_value_no_tax* ( 1+ get_tax_rate(sales_date)) - discount_value)/( 1+ get_tax_rate(sales_date)))\n"); 
                //Luc start add 20150724 #40579
                sql.append("             		else\n");
		sql.append("                                    discount_detail_value_in_tax - discount_detail_value_no_tax\n"); 
                sql.append("                                end\n");
                //Luc start add 20150724 #40579
                sql.append("             		else\n");
                sql.append("                                case when discount_value !=  0 \n");
                sql.append("                                then \n");
                sql.append("                                    ceil((detail_value_no_tax* ( 1+ get_tax_rate(sales_date))- detail_value_no_tax - ceil(discount_value*get_tax_rate(sales_date))))\n");
                sql.append("                                else \n");
                sql.append("                                    discount_detail_value_in_tax - discount_detail_value_no_tax\n");
                sql.append("                                end\n");
                sql.append("                       end \n");
                sql.append("                       )END ");
                sql.append("                ) AS tax");

                sql.append("            ,sum(CASE WHEN a.product_division = 7  ");
                sql.append("                    THEN ( SELECT sum(dpd.payment_value) AS payment_value1 FROM data_payment_detail dpd INNER JOIN mst_payment_method mpm ON mpm.payment_method_id = dpd.payment_method_id ");
                sql.append("                            AND mpm.payment_class_id IN (1,2,3,4) WHERE true AND dpd.payment_no = 0 AND shop_id = a.shop_id AND slip_no = a.slip_no) ");
                sql.append("                    ELSE ( ");
                sql.append("                            case when ac.discount_type = 0 then");
                 //Luc start add 20150724 #40579
                sql.append("                                case when discount_value != 0 then \n");
                //Luc start add 20150724 #40579
                sql.append("                                    case when ac.display_price_type = 0 then \n"); 
                sql.append("                                        detail_value_in_tax - discount_value \n");
                sql.append("                                    else \n");
                sql.append("                                floor(detail_value_no_tax *( 1+ get_tax_rate(sales_date))) - discount_value \n");
                sql.append("                                    end \n");
                //Luc start add 20150724 #40579
                sql.append("                                else\n"); 
                sql.append("                                    detail_value_in_tax\n"); 
                sql.append("                                end\n");
                //Luc start add 20150724 #40579
                
                sql.append("                            else  \n");
                sql.append("                                case when discount_value != 0 then\n"); 
                sql.append("                                    ceil(detail_value_no_tax *( 1+ get_tax_rate(sales_date)) - ceil(discount_value *  ( 1+ get_tax_rate(sales_date)))) \n");
                sql.append("                                else \n"); 
                sql.append("                                    detail_value_in_tax\n"); 
                sql.append("                                end \n"); 
                sql.append("                            end) \n");
                sql.append("                    end  \n");
                sql.append("                ) AS total_value \n");
                //IVS_TMTrong end edit 20150716 Bug #37392
                //Luc end add 20150714 #40178
		sql.append("         from" + "\n");
		sql.append("             view_data_sales_detail_valid a" + "\n");
                //Luc start add 20150714 #40178
                sql.append("             ,(SELECT mst_account_setting.display_price_type, mst_account_setting.discount_type \n");
                sql.append("             FROM mst_account_setting \n");
                sql.append("             WHERE mst_account_setting.delete_date IS NULL \n");
                sql.append("             LIMIT 1) ac \n");
                //Luc end add 20150714 #40178
		sql.append("         where" + "\n");
                 // start edit 2013-10-30 Hoa
		sql.append("                 shop_id = " + shopId + "  and product_division <> 6 \n");
                 // end 2013-10-30 Hoa
		sql.append("             and sales_date = '%1$tY/%1$tm/%1$td'" + "\n");
		sql.append("         group by" + "\n");
		sql.append("              shop_id" + "\n");
                // start delete 2013-10-30 Hoa
                // start add 20130807 nakhoa
                //sql.append("              ,product_division" + "\n");
                // end add 20130807 nakhoa
                // end delete 2013-10-30 Hoa
		sql.append("             ,slip_no" + "\n");
		sql.append("     ) a" + "\n");
		sql.append("     inner join view_data_sales_valid b" + "\n");
		sql.append("             on a.shop_id = b.shop_id" + "\n");
		sql.append("            and a.slip_no = b.slip_no" + "\n");

		sql.append("     left outer join mst_customer mc" + "\n");
		sql.append("                  on b.customer_id = mc.customer_id" + "\n");

		sql.append("     left outer join mst_staff as ms" + "\n");
		sql.append("                  on b.staff_id = ms.staff_id" + "\n");

		sql.append("     inner join data_sales c" + "\n");
		sql.append("             on b.shop_id = c.shop_id" + "\n");
		sql.append("            and b.slip_no = c.slip_no" + "\n");
		if (staffID >= 0)
		{
			sql.append(" and c.staff_id = " + SQLUtil.convertForSQL(new Integer(staffID)));
		}

		sql.append(" where" + "\n");
		sql.append("         b.shop_id = " + shopId + "\n");
		sql.append("     and b.sales_date = '%1$tY/%1$tm/%1$td'" + "\n");
                // start delete 2013-10-30 Hoa
                //sql.append("     and a.product_division <> 6" + "\n");
                // end delete 2013-10-30 Hoa    
		sql.append(" order by" + "\n");
		sql.append("     slip_no" + "\n");
                
                sql.append(" ) a  \n");
		return String.format(sql.toString(), date);
	}
	
	
	private class ReservationComparator implements Comparator<DataReservation>
	{
		private	Integer		col		=	-1;
		private	boolean		isAsc	=	true;
		
		// コンストラクタ
		public ReservationComparator()
		{
			super();
		}

		public Integer getCol()
		{
			return col;
		}

		public void setCol(Integer col)
		{
			isAsc	=	(col != this.col || !isAsc);
			this.col = col;
		}

		public boolean equals(DataReservation dt)
		{
			return (super.equals(dt));
		}

		// ここで多い順に指定する
		// 今回は Integer オブジェクトが引数でわたってくると断定
		public int compare(DataReservation dr0, DataReservation dr1)
		{
			if(dr0.getStatus() != dr1.getStatus())
			{
				return	dr0.getStatus().compareTo(dr1.getStatus());
			}
			
			switch(col)
			{
				//予約番号
				case 0:
					if(isAsc)
					{
						return	dr0.getReservationNo().compareTo(dr1.getReservationNo());
					}
					else
					{
						return	dr1.getReservationNo().compareTo(dr0.getReservationNo());
					}
				//顧客No.
				case 1:
					if(isAsc)
					{
						return	dr0.getCustomer().getCustomerNo().compareTo(dr1.getCustomer().getCustomerNo());
					}
					else
					{
						return	dr1.getCustomer().getCustomerNo().compareTo(dr0.getCustomer().getCustomerNo());
					}
				//顧客名
				case 2:
					if(isAsc)
					{
						return	dr0.getCustomer().getFullCustomerName().compareTo(dr1.getCustomer().getFullCustomerName());
					}
					else
					{
						return	dr1.getCustomer().getFullCustomerName().compareTo(dr0.getCustomer().getFullCustomerName());
					}
				//メニュー
				case 3:
					if(isAsc)
					{
						return	dr0.getTechnicClassContractedName().compareTo(dr1.getTechnicClassContractedName());
					}
					else
					{
						return	dr1.getTechnicClassContractedName().compareTo(dr0.getTechnicClassContractedName());
					}
				//予約時間
				case 4:
					if(isAsc)
					{
						return	dr0.get(0).getReservationDatetime().compareTo(dr1.get(0).getReservationDatetime());
					}
					else
					{
						return	dr1.get(0).getReservationDatetime().compareTo(dr0.get(0).getReservationDatetime());
					}
				//開始時間
				case 5:
					if(isAsc)
					{
						return	dr0.getStartTime().compareTo(dr1.getStartTime());
					}
					else
					{
						return	dr1.getStartTime().compareTo(dr0.getStartTime());
					}
				//施術時間
				case 6:
					if(isAsc)
					{
						return	dr0.getTotalTime().compareTo(dr1.getTotalTime());
					}
					else
					{
						return	dr1.getTotalTime().compareTo(dr0.getTotalTime());
					}
				//終了時間
				case 7:
					GregorianCalendar	finishTime0	=	new GregorianCalendar();
					finishTime0.setTime(dr0.getStartTime().getTime());
					finishTime0.add(finishTime0.MINUTE, dr0.get(dr0.size() - 1).getTechnic().getOperationTime());
					
					GregorianCalendar	finishTime1	=	new GregorianCalendar();
					finishTime1.setTime(dr1.getStartTime().getTime());
					finishTime1.add(finishTime1.MINUTE, dr1.get(dr1.size() - 1).getTechnic().getOperationTime());
					
					if(isAsc)
					{
						return	finishTime0.compareTo(finishTime1);
					}
					else
					{
						return	finishTime1.compareTo(finishTime0);
					}
			}
			
			return	0;
		}
	}
	
	
	private class SalesComparator implements Comparator<SalesData>
	{
		private	Integer		col		=	-1;
		private	boolean		isAsc	=	true;
		
		// コンストラクタ
		public SalesComparator()
		{
			super();
		}

		public Integer getCol()
		{
			return col;
		}

		public void setCol(Integer col)
		{
			isAsc	=	(col != this.col || !isAsc);
			this.col = col;
		}

		public boolean equals(SalesData dt)
		{
			return (super.equals(dt));
		}

		// ここで多い順に指定する
		// 今回は Integer オブジェクトが引数でわたってくると断定
		public int compare(SalesData sd0, SalesData sd1)
		{
			switch(col)
			{
				//伝票番号
				case 0:
					if(isAsc)
					{
						return	sd0.getSlipNo().compareTo(sd1.getSlipNo());
					}
					else
					{
						return	sd1.getSlipNo().compareTo(sd0.getSlipNo());
					}
				//顧客No.
				case 1:
					if(isAsc)
					{
						return	sd0.getCustomer().getCustomerNo().compareTo(sd1.getCustomer().getCustomerNo());
					}
					else
					{
						return	sd1.getCustomer().getCustomerNo().compareTo(sd0.getCustomer().getCustomerNo());
					}
				//顧客名
				case 2:
					if(isAsc)
					{
						return	sd0.getCustomer().getFullCustomerName().compareTo(sd1.getCustomer().getFullCustomerName());
					}
					else
					{
						return	sd1.getCustomer().getFullCustomerName().compareTo(sd0.getCustomer().getFullCustomerName());
					}
				//技術
				case 3:
					if(isAsc)
					{
						return	sd0.getTechnic().compareTo(sd1.getTechnic());
					}
					else
					{
						return	sd1.getTechnic().compareTo(sd0.getTechnic());
					}
				//商品
				case 4:
					if(isAsc)
					{
						return	sd0.getItem().compareTo(sd1.getItem());
					}
					else
					{
						return	sd1.getItem().compareTo(sd0.getItem());
					}
				//割引
				case 5:
					if(isAsc)
					{
						return	sd0.getDiscount().compareTo(sd1.getDiscount());
					}
					else
					{
						return	sd1.getDiscount().compareTo(sd0.getDiscount());
					}
				//消費税
				case 6:
					if(isAsc)
					{
						return	sd0.getTax().compareTo(sd1.getTax());
					}
					else
					{
						return	sd1.getTax().compareTo(sd0.getTax());
					}
				//請求金額
				case 7:
					if(isAsc)
					{
						return	sd0.getTotal().compareTo(sd1.getTotal());
					}
					else
					{
						return	sd1.getTotal().compareTo(sd0.getTotal());
					}
			}
			
			return	0;
		}
	}
        public void setSlipNo(DataReservation dr){
                  ConnectionWrapper	con	=	SystemInfo.getConnection();
                  try
                  {
                          dr.selectSlipNoStatus(con);
                  }
                  catch(SQLException e)
                 {
                        SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                 }                                           
        }
}
