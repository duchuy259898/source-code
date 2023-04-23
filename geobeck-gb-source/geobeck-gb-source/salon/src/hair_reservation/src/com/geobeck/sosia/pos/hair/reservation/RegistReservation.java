/*
 * RegistReservation.java
 *
 * Created on 2006/06/21, 11:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.reservation;

import com.geobeck.sosia.pos.hair.data.account.ConsumptionCourse;
import com.geobeck.sosia.pos.hair.data.account.ConsumptionCourseClass;
import com.geobeck.sosia.pos.hair.data.account.Course;
import com.geobeck.sosia.pos.hair.data.account.CourseClass;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.hair.data.reservation.*;
import com.geobeck.sosia.pos.hair.master.company.MstBed;
import com.geobeck.sosia.pos.hair.master.product.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.hair.data.product.*;
import com.geobeck.sosia.pos.hair.master.company.MstAPI;
import com.geobeck.sosia.pos.hair.master.company.MstResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import org.apache.commons.collections.map.LinkedMap;

/**
 * 予約登録処理
 * @author katagiri
 */
public class RegistReservation implements Cloneable
{
        //IVS_LVTu start add 2016/07/13 New request #50223
        final int           FLAG_INSERT_UPDATE         = 1;
        final int           FLAG_DELETE                = 3;
        //IVS_LVTu end add 2016/07/13 New request #50223
	/**
	 * 店舗
	 */
	protected	MstShop				shop		=	new MstShop();
	/**
	 * 全スタッフ
	 */
	protected	Vector<MstStaff>	staffs		=	new Vector<MstStaff>();
	/**
	 * 全ベッド
	 */
	protected	Vector<MstBed>		beds		=	new Vector<MstBed>();
	
	/**
	 * 顧客
	 */
	protected	MstCustomer			customer	=	new MstCustomer();
	/**
	 * 予約日
	 */
	protected	GregorianCalendar	date		=	new	GregorianCalendar();
	/**
	 * 予約
	 */
	protected	DataReservation		reservation	=	new DataReservation();
        
        // GB Start add 20161117 #58629
        /**
         * フリー項目
         */
        protected       ArrayList<MstCustomerFreeHeading> freeHeading = new ArrayList<MstCustomerFreeHeading>();
        // GB End add 20161117 #58629
        
        //IVS_LVTu start add 2016/07/13 New request #50223
        protected	DataReservation		reservationCopy	=	null;
            
        //IVS_NHTVINH start add 2016/09/05 New request #54380
        protected MstAPI mstApi = null;
        
//        ReservationRegistAPI                    ReservationAPI  = new ReservationRegistAPI();
//
//        public ReservationRegistAPI getReservationAPI() {
//            return ReservationAPI;
//        }
//
//        public void setReservationAPI(ReservationRegistAPI ReservationAPI) {
//            this.ReservationAPI = ReservationAPI;
//        }
        //IVS_LVTu end add 2016/07/13 New request #50223
        
        // IVS_Thanh start add 2014/07/07 GB_Mashu_業態主担当設定
        public ArrayList<DataReservationMainstaff> reservationMainstaffs = new ArrayList<DataReservationMainstaff>();
        private boolean tempSave = false;
        /**
        * @return the tempSave
        */
        public boolean isTempSave() {
            return tempSave;
        }

        /**
         * @param tempSave the tempSave to set
         */
        public void setTempSave(boolean tempSave) {
            this.tempSave = tempSave;
        }
        // IVS_Thanh end add 2014/07/07 GB_Mashu_業態主担当設定
	
        //Start add 20131002 lvut
        @Override
        protected Object clone() throws CloneNotSupportedException {
             return super.clone();
        }
        //End add 20131002 lvut
	/** Creates a new instance of RegistReservation */
	public RegistReservation()
	{
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
		reservation.setShop(this.shop);
	}

	/**
	 * 全スタッフを取得する。
	 * @return 全スタッフ
	 */
	public Vector<MstStaff> getStaffs()
	{
		return staffs;
	}

	/**
	 * 全スタッフを設定する。
	 * @param staffs 全スタッフ
	 */
	public void setStaffs(Vector<MstStaff> staffs)
	{
		this.staffs = staffs;
	}

	/**
	 * 全ベッドを取得する。
	 * @return 全ベッド
	 */
	public Vector<MstBed> getBeds()
	{
		return beds;
	}

	/**
	 * 全ベッドを設定する。
	 * @param beds 全ベッド
	 */
	public void setBeds(Vector<MstBed> beds)
	{
		this.beds = beds;
	}

	/**
	 * 顧客を取得する。
	 * @return 顧客
	 */
	public MstCustomer getCustomer()
	{
		return customer;
	}

	/**
	 * 顧客を設定する。
	 * @param customer 顧客
	 */
	public void setCustomer(MstCustomer customer)
	{
		this.customer = customer;
		
		reservation.setCustomer(customer);
	}
        
        // IVS_Thanh start add 2014/07/07 GB_Mashu_業態主担当設定
        public ArrayList<DataReservationMainstaff> getReservationMainstaffs()
	{
		return reservationMainstaffs;
	}
	
	public void setReservationMainstaffs(ArrayList<DataReservationMainstaff> reservationMainstaffs)
	{
		this.reservationMainstaffs = reservationMainstaffs;
	}
        // IVS_Thanh end add 2014/07/07 GB_Mashu_業態主担当設定

	/**
	 * 予約日を取得する。
	 * @return 予約日
	 */
	public GregorianCalendar getDate()
	{
		return date;
	}

	/**
	 * 予約日を設定する。
	 * @param date 予約日
	 */
	public void setDate(GregorianCalendar date)
	{
		this.date = date;
	}

	/**
	 * 予約を取得する。
	 * @return 予約
	 */
	public DataReservation getReservation()
	{
		return reservation;
	}

	/**
	 * 予約のリストを設定する。
	 * @param reservations 予約のリスト
	 */
	public void setReservation(DataReservation reservation)
	{
		this.reservation = reservation;
	}
        
        // GB STart add 20161117 #58629
        /**
         * フリー項目を設定する
         */
        public void setFreeHeadingInfo(ArrayList<MstCustomerFreeHeading> freeHeading)
        {
            this.freeHeading = freeHeading;
        }
        
        /**
         * フリー項目を取得する
         */
        public ArrayList<MstCustomerFreeHeading> getFreeHeadingInfo()
        {
            return freeHeading;
        }
	// GB End add 20161117 #58629
        
	/**
	 * 初期化処理を行う。
	 */
	public void init()
	{
		ConnectionWrapper	con	=	SystemInfo.getConnection();

		this.loadStaffs(con);
		this.loadBeds(con);
		
		this.clear();
	}
	
	/**
	 * 全スタッフをデータベースから読み込む。
	 * @param con ConnectionWrapper
	 */
	private void loadStaffs(ConnectionWrapper con)
	{
		staffs.clear();
		
		try
		{
			ResultSetWrapper	rs	=	con.executeQuery(this.getLoadStaffsSQL());

			while(rs.next())
			{
				MstStaff	staff	=	new MstStaff();
				staff.setData(rs);
				staffs.add(staff);
			}

			rs.close();
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * 全ベッドをデータベースから読み込む。
	 * @param con ConnectionWrapper
	 */
	private void loadBeds(ConnectionWrapper con)
	{
		beds.clear();
		
		try
		{
			ResultSetWrapper	rs	=	con.executeQuery(this.getLoadBedsSQL());

			while(rs.next())
			{
				MstBed	bed	=	new MstBed();
				bed.setData(rs);
				beds.add(bed);
			}

			rs.close();
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * データをクリアする。
	 */
	public void clear()
	{
//		customer	=	new MstCustomer();
		reservation	=	new DataReservation();
		reservation.setShop(shop);
	}
	
	/**
	 * 削除処理を行う。
	 * @param index 技術のインデックス
	 */
	public void deleteTechnic(int index)
	{
		if(0 <= index && index < reservation.size())
		{
                        if (reservation.get(index).getCourse() != null) {
        			reservation.setTotalTime(reservation.getTotalTime() - reservation.get(index).getCourse().getOperationTime());
                        } else if (reservation.get(index).getConsumptionCourse() != null) {
        			reservation.setTotalTime(reservation.getTotalTime() - reservation.get(index).getConsumptionCourse().getOperationTime());
                        } else {
        			reservation.setTotalTime(reservation.getTotalTime() - reservation.get(index).getTechnic().getOperationTime());
                        }
			reservation.remove(index);
		}
	}
	
	public boolean delete()
	{
		boolean	result	=	false;
		
		if(this.getReservation() != null)
		{
			if(this.getReservation().getReservationNo() != null)
			{
                                //IVS_LVTu start add 2016/07/13 New request #50223
                                if(SystemInfo.getUserAPI() != null && SystemInfo.getUserAPI() != 0) {
                                    // 201712 GB add start #34836 [GB内対応][gb]予約キャンセル機能：SC連携店舗の場合、キャンセルフラグが入らない
                                    int tmpCancelFlg = -1;
                                    if(this.getReservation().getCancelFlag() != null) {
                                        tmpCancelFlg = this.getReservation().getCancelFlag();
                                    }
                                    // 201712 GB add end #34836 [GB内対応][gb]予約キャンセル機能：SC連携店舗の場合、キャンセルフラグが入らない
                                    
                                    this.getDetailReservation();
                                    
                                    // 201712 GB add start #34836 [GB内対応][gb]予約キャンセル機能：SC連携店舗の場合、キャンセルフラグが入らない
                                    if(tmpCancelFlg > -1) {
                                        this.getReservation().setCancelFlag(tmpCancelFlg);
                                    }
                                    // 201712 GB add end #34836 [GB内対応][gb]予約キャンセル機能：SC連携店舗の場合、キャンセルフラグが入らない
                                    this.reservationCopy = (DataReservation) this.reservation.clone();
                                    this.reservationCopy.setReserveDate(this.getDate().getTime());
                                }
				ConnectionWrapper	con	=	SystemInfo.getConnection();

				try
				{
					con.begin();
					int reservationNo = this.getReservation().getReservationNo();
					if(this.getReservation().delete(con))
					{
						con.commit();
                                                result = true;
                                                if(SystemInfo.getUserAPI() != null && SystemInfo.getUserAPI() != 0) {
                                                    //ReservationAPI.setReservation(reservationCopy);
                                                    //ReservationAPI.executeAPI(con, this.FLAG_DELETE);
                                                }
                                                
                                                //IVS_LVTu end add 2016/07/13 New request #50223
					}
					else
					{
						con.rollback();
					}
				}
				catch(SQLException e)
				{
					SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			}
		}
		
		return	result;
	}
        
        //IVS_LVTu start add 2018/04/06 GB_Studio 予約登録
        /**
         * 予約削除
         * @param con
         * @return true - 成功
         */
        public boolean deleteStudio(ConnectionWrapper	con) throws SQLException
	{
		
		if(this.getReservation() != null)
		{
			if(this.getReservation().getReservationNo() != null)
			{
                                if(SystemInfo.getUserAPI() != null && SystemInfo.getUserAPI() != 0) {
                                    int tmpCancelFlg = -1;
                                    if(this.getReservation().getCancelFlag() != null) {
                                        tmpCancelFlg = this.getReservation().getCancelFlag();
                                    }
                                    
                                    this.getDetailReservation();
                                    
                                    if(tmpCancelFlg > -1) {
                                        this.getReservation().setCancelFlag(tmpCancelFlg);
                                    }
                                    this.reservationCopy = (DataReservation) this.reservation.clone();
                                    this.reservationCopy.setReserveDate(this.getDate().getTime());
                                }

                                if(!this.getReservation().delete(con))
                                {
                                        return false;
                                }
			}
		}
		
		return	true;
	}
        //IVS_LVTu end add 2018/04/06 GB_Studio 予約登録
	
	/**
	 * 予約データを読み込む。
	 * @param reservationNo 予約番号の配列
	 * @return Array - 読み込み成功
	 */
	public boolean loadCopyReservation(Integer reservationNo,GregorianCalendar clickTime)
	{
		MstStaff staff = null;
		
		this.clear();
		
		ConnectionWrapper con = SystemInfo.getConnection();
		try
		{
			ResultSetWrapper rs = con.executeQuery(this.getLoadReservationSQL(reservationNo));
			
			while(rs.next())
			{
				//１件目のデータの場合
				if(reservation.size() == 0)
				{
					reservation		=	new DataReservation();
					reservation.setDesignated( rs.getBoolean( "charge_staff_designated_flag" ) );
					staff = new MstStaff();
					staff.setStaffID(rs.getInt("charge_staff_id"));
					staff.setStaffNo(rs.getString("charge_staff_no"));
					staff.setStaffName(0, rs.getString("charge_staff_name1") );
					staff.setStaffName(1, rs.getString("charge_staff_name2") );
					reservation.setStaff(staff);
					reservation.setShop(shop);
					//reservation.setReservationNo(rs.getInt("reservation_no"));
					customer.setCustomerID(rs.getInt("customer_id"));
					customer.setCustomerNo(rs.getString("customer_no"));
					customer.setCustomerName(0, rs.getString("customer_name1"));
					customer.setCustomerName(1, rs.getString("customer_name2"));
					reservation.setCustomer(customer);

					staff = new MstStaff();
					staff.setStaffID(rs.getInt("reg_staff_id"));
					staff.setStaffNo(rs.getString("reg_staff_no"));
					staff.setStaffName(0, rs.getString("reg_staff_name1") );
					staff.setStaffName(1, rs.getString("reg_staff_name2") );
					reservation.setRegStaff(staff);
				}
				
				DataReservationDetail drd = new DataReservationDetail();
				
                                // start add 20130812 nakhoa
                                Object contractShopId = rs.getObject("contract_shop_id");
                                if(contractShopId == null){
                                    drd.setContractShopId(null);
                                }else{
                                    drd.setContractShopId(Integer.valueOf(contractShopId.toString()));
                                }
                                // end add 20130812 nakhoa
				drd.getReservationDatetime().setTime(clickTime.getTime());
				drd.setDesignated( rs.getBoolean( "menu_staff_designated_flag" ) );
                                Object courseFlg = rs.getObject("course_flg");
                                
                                if (courseFlg == null) {
                                    //技術の場合
                                    drd.setCourseFlg(null);
                                    
                                    MstTechnicClass	technicClass	=	new MstTechnicClass();
                                    technicClass.setTechnicClassID(rs.getInt("technic_class_id"));
                                    technicClass.setTechnicClassName(rs.getString("technic_class_name"));
                                     //nhanvt start add 20150128 New request #35057
                                    technicClass.setShopcategoryid(rs.getInt("shop_category_id"));
                                     //nhanvt end add 20150128 New request #35057
                                    MstTechnic technic = new MstTechnic();
                                    technic.setTechnicClass(technicClass);
                                    technic.setTechnicID(rs.getInt("technic_id"));
                                    technic.setTechnicNo(rs.getString("technic_no"));
                                    technic.setTechnicName(rs.getString("technic_name"));
                                    technic.setOperationTime(rs.getInt("operation_time"));
                                    drd.setTechnic(technic);
                                    clickTime.add(Calendar.MINUTE,rs.getInt("operation_time"));
                                } else if ((Integer)courseFlg == 1) {
                                    //コース契約の場合
                                    drd.setCourseFlg((Integer)courseFlg);
                                    
                                    CourseClass courseClass = new CourseClass();
                                    courseClass.setCourseClassId(rs.getInt("technic_class_id"));
                                    courseClass.setCourseClassName(rs.getString("technic_class_name"));
                                    //nhanvt start add 20150128 New request #35057
                                    courseClass.setShopCategoryID(rs.getInt("shop_category_id"));
                                     //nhanvt end add 20150128 New request #35057
                                    Course course = new Course();
                                    course.setCourseClass(courseClass);
                                    course.setCourseId(rs.getInt("technic_id"));
                                    course.setCourseName(rs.getString("technic_name"));
                                    course.setOperationTime(rs.getInt("operation_time"));
                                    drd.setCourse(course);
                                    clickTime.add(Calendar.MINUTE,rs.getInt("operation_time"));
                                } else if ((Integer)courseFlg == 2) {
                                    //消化コースの場合
                                    drd.setCourseFlg((Integer)courseFlg);

                                    ConsumptionCourseClass consumptionCourseClass = new ConsumptionCourseClass();
                                    consumptionCourseClass.setCourseClassId(rs.getInt("technic_class_id"));
                                    consumptionCourseClass.setCourseClassName(rs.getString("technic_class_name"));
                                     //nhanvt start add 20150128 New request #35057
                                    consumptionCourseClass.setShopCategoryID(rs.getInt("shop_category_id"));
                                     //nhanvt end add 20150128 New request #35057
                                    ConsumptionCourse consumptionCourse = new ConsumptionCourse();
                                    consumptionCourse.setConsumptionCourseClass(consumptionCourseClass);
                                    consumptionCourse.setCourseId(rs.getInt("technic_id"));
                                    consumptionCourse.setCourseName(rs.getString("technic_name"));
                                    consumptionCourse.setOperationTime(rs.getInt("operation_time"));
                                    consumptionCourse.setContractNo(rs.getInt("contract_no"));
                                    consumptionCourse.setContractDetailNo(rs.getInt("contract_detail_no"));
                                    // vtbphuong start add 20140422 
                                    consumptionCourse.setContractShopId(rs.getInt("contract_shop_id"));
                                    // vtbphuong end add 20140422 
                                    drd.setConsumptionCourse(consumptionCourse);
                                    clickTime.add(Calendar.MINUTE,rs.getInt("operation_time"));
                                }
				
				staff = new MstStaff();
				staff.setStaffID(rs.getInt("staff_id"));
				staff.setStaffNo(rs.getString("staff_no"));
				staff.setStaffName(0, "staff_name1");
				staff.setStaffName(1, "staff_name2");
				drd.setStaff(staff);
				
				MstBed bed = new MstBed();
				if (rs.getInt("bed_id") != 0) {
					bed.setBedID(rs.getInt("bed_id"));
					bed.setBedName(rs.getString("bed_name"));
					drd.setBed(bed);
				}
				
				// 按分データを取得する
				loadReservationProportionally( drd );
                                
                                // IVS_Thanh start add 2014/08/05 Mashu_業態主担当設定
                                //loadDataReservaionMainStaff(reservationNo);
                                // IVS_Thanh end add 2014/08/05 Mashu_業態主担当設定
				
				reservation.add(drd);
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	true;
	}
	
	/**
	 * 予約データを読み込む。
	 * @param reservationNo 予約番号の配列
	 * @return true - 読み込み成功
	 */
	public boolean loadReservation(Integer reservationNo)
	{
		MstStaff staff = null;
		
		this.clear();
		
		ConnectionWrapper con = SystemInfo.getConnection();
		
		try
		{
			ResultSetWrapper rs = con.executeQuery(this.getLoadReservationSQL(reservationNo));
			
			while(rs.next())
			{
				//１件目のデータの場合
				if(reservation.size() == 0)
				{
					reservation		=	new DataReservation();
					reservation.setDesignated( rs.getBoolean( "charge_staff_designated_flag" ) );
					staff = new MstStaff();
					staff.setStaffID(rs.getInt("charge_staff_id"));
					staff.setStaffNo(rs.getString("charge_staff_no"));
					staff.setStaffName(0, rs.getString("charge_staff_name1") );
					staff.setStaffName(1, rs.getString("charge_staff_name2") );
					reservation.setStaff(staff);
					reservation.setShop(shop);
					reservation.setReservationNo(rs.getInt("reservation_no"));
					customer.setCustomerID(rs.getInt("customer_id"));
					customer.setCustomerNo(rs.getString("customer_no"));
					customer.setCustomerName(0, rs.getString("customer_name1"));
					customer.setCustomerName(1, rs.getString("customer_name2"));
					reservation.setCustomer(customer);
					reservation.setStatus(rs.getInt("status"));
					reservation.setSubStatus(rs.getInt("sub_status"));
					reservation.setComment(rs.getString("comment"));
					reservation.setNextFlag(rs.getInt("next_flag"));
					reservation.setPreorderFlag(rs.getInt("preorder_flag"));

					staff = new MstStaff();
					staff.setStaffID(rs.getInt("reg_staff_id"));
					staff.setStaffNo(rs.getString("reg_staff_no"));
					staff.setStaffName(0, rs.getString("reg_staff_name1") );
					staff.setStaffName(1, rs.getString("reg_staff_name2") );
					reservation.setRegStaff(staff);

					reservation.setInsertDate(rs.getTimestamp("insert_date"));
					reservation.setUpdateDate(rs.getTimestamp("update_date"));
                                        //Start add 20131030 lvut 
                                        MstResponse response = new MstResponse();
                                        response.setResponseID(rs.getInt("response_id1"));
                                        try {
                                            response.load(con);
                                        } catch (Exception e) {
                                        }
                                        reservation.setResponse1(response);
                                        response = new MstResponse();
                                        response.setResponseID(rs.getInt("response_id2"));
                                        try {
                                            response.load(con);
                                        } catch (Exception e) {
                                        }
                                        reservation.setResponse2(response);
                                        //End add 20131030 lvut 
					date.setTime(rs.getDate("reservation_datetime"));
				}
				
				DataReservationDetail drd = new DataReservationDetail();
				
				drd.setReservation(reservation);
				drd.setReservationDetailNo(rs.getInt("reservation_detail_no"));
                                // start add 20130812 nakhoa
                                Object contractShopId = rs.getObject("contract_shop_id");
                                if(contractShopId == null){
                                    drd.setContractShopId(null);
                                }else{
                                    drd.setContractShopId(Integer.valueOf(contractShopId.toString()));
                                }
                                // end add 20130812 nakhoa
				drd.getReservationDatetime().setTime(rs.getTimestamp("reservation_datetime"));
				drd.setDesignated( rs.getBoolean( "menu_staff_designated_flag" ) );
                                Object courseFlg = rs.getObject("course_flg");
                                
                                if (courseFlg == null) {
                                    //技術の場合
                                    drd.setCourseFlg(null);
                                    
                                    MstTechnicClass	technicClass	=	new MstTechnicClass();
                                    technicClass.setTechnicClassID(rs.getInt("technic_class_id"));
                                    technicClass.setTechnicClassName(rs.getString("technic_class_name"));
                                    //nhanvt start add 20150128 New request #35057
                                    technicClass.setShopcategoryid(rs.getInt("shop_category_id"));
                                    //nhanvt end add 20150128 New request #35057
                                    MstTechnic technic = new MstTechnic();
                                    technic.setTechnicClass(technicClass);
                                    technic.setTechnicID(rs.getInt("technic_id"));
                                    technic.setTechnicNo(rs.getString("technic_no"));
                                    technic.setTechnicName(rs.getString("technic_name"));
                                    technic.setOperationTime(rs.getInt("operation_time"));
                                    drd.setTechnic(technic);
                                } else if ((Integer)courseFlg == 1) {
                                    //コース契約の場合
                                    drd.setCourseFlg((Integer)courseFlg);
                                    
                                    CourseClass courseClass = new CourseClass();
                                    courseClass.setCourseClassId(rs.getInt("technic_class_id"));
                                    courseClass.setCourseClassName(rs.getString("technic_class_name"));
                                    //nhanvt start add 20150128 New request #35057
                                    courseClass.setShopCategoryID(rs.getInt("shop_category_id"));
                                     //nhanvt end add 20150128 New request #35057
                                    Course course = new Course();
                                    course.setCourseClass(courseClass);
                                    course.setCourseId(rs.getInt("technic_id"));
                                    course.setCourseName(rs.getString("technic_name"));
                                    course.setOperationTime(rs.getInt("operation_time"));
                                    drd.setCourse(course);
                                } else if ((Integer)courseFlg == 2) {
                                    //消化コースの場合
                                    drd.setCourseFlg((Integer)courseFlg);

                                    ConsumptionCourseClass consumptionCourseClass = new ConsumptionCourseClass();
                                    consumptionCourseClass.setCourseClassId(rs.getInt("technic_class_id"));
                                    consumptionCourseClass.setCourseClassName(rs.getString("technic_class_name"));
                                    //nhanvt start add 20150128 New request #35057
                                    consumptionCourseClass.setShopCategoryID(rs.getInt("shop_category_id"));
                                     //nhanvt end add 20150128 New request #35057
                                    ConsumptionCourse consumptionCourse = new ConsumptionCourse();
                                    consumptionCourse.setConsumptionCourseClass(consumptionCourseClass);
                                    consumptionCourse.setCourseId(rs.getInt("technic_id"));
                                    consumptionCourse.setCourseName(rs.getString("technic_name"));
                                    consumptionCourse.setOperationTime(rs.getInt("operation_time"));
                                    consumptionCourse.setContractNo(rs.getInt("contract_no"));
                                    consumptionCourse.setContractDetailNo(rs.getInt("contract_detail_no"));
                                    // vtbphuong start add 20140422 
                                    consumptionCourse.setContractShopId(rs.getInt("contract_shop_id"));
                                    // vtbphuong end add 20140422 
                                    drd.setConsumptionCourse(consumptionCourse);
                                }
				
				staff = new MstStaff();
				staff.setStaffID(rs.getInt("staff_id"));
				staff.setStaffNo(rs.getString("staff_no"));
				staff.setStaffName(0, "staff_name1");
				staff.setStaffName(1, "staff_name2");
				drd.setStaff(staff);
				
				MstBed bed = new MstBed();
				bed.setBedID(rs.getInt("bed_id"));
				bed.setBedName(rs.getString("bed_name"));
				drd.setBed(bed);
				
				// 按分データを取得する
				loadReservationProportionally( drd );
                                
                                // IVS_Thanh start add 2014/08/05 Mashu_業態主担当設定
                                loadDataReservaionMainStaff(reservationNo);
                                // IVS_Thanh end add 2014/08/05 Mashu_業態主担当設定
				
				reservation.add(drd);
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	true;
	}
        
        // IVS_Thanh start add 2014/08/05 Mashu_業態主担当設定
        public boolean loadDataReservaionMainStaff(Integer reservationNo) throws SQLException {
            ConnectionWrapper	con	=	SystemInfo.getConnection();
            ResultSetWrapper rs = con.executeQuery(this.getSelecDatatReservationMainStaffSQL(reservationNo));
            while (rs.next()) {
                DataReservationMainstaff dsm = new DataReservationMainstaff();
                dsm.setData(rs);
                this.reservationMainstaffs.add(dsm);
            }
            rs.close();
            return true;

         }

         private String getSelecDatatReservationMainStaffSQL(Integer reservationNo) {
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" select \n");
             sql.append("     drm.*,staff_no,msc.shop_class_name \n");
             sql.append(" from \n");
             sql.append("    mst_shop_category msc \n");
             sql.append("    left join  data_reservation_mainstaff drm using(shop_category_id)  \n");
             sql.append("    left join mst_staff ms using(staff_id) \n");
             sql.append(" where \n");
             sql.append("     drm.delete_date is null \n");
             sql.append("     and drm.reservation_no = " + SQLUtil.convertForSQL(reservationNo));
             sql.append("     and drm.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
             sql.append(" order by \n");
             sql.append("      shop_category_id \n"); 
             return sql.toString();
         }
         // IVS_Thanh end add 2014/08/051 Mashu_業態主担当設定
	
	/**
	 * 按分予約データを取得する
	 */
	private boolean loadReservationProportionally( DataReservationDetail drd )
	{
		// 予約詳細データ内の按分データをクリアする
		drd.clear();
		
		ConnectionWrapper	con	=	SystemInfo.getConnection();
		
		try
		{
			ResultSetWrapper	rs	=	con.executeQuery(
					this.loadReservationProportionallySQL( drd.getReservationNo(), drd.getReservationDetailNo(), drd.getTechnic().getTechnicID() ) );
			
			while(rs.next())
			{
				DataReservationProportionally drp = new DataReservationProportionally();
				drp.setReservationDetail( drd );
				DataProportionally proportionally = new DataProportionally();
				proportionally.setDataProportionallyID( rs.getInt( "data_proportionally_id" ) );
				proportionally.setTechnic( drd.getTechnic() );
				MstProportionally mp = new MstProportionally();
				mp.setProportionallyID(    rs.getInt("proportionally_id") );
				mp.setProportionallyName(  rs.getString("proportionally_name") );
				mp.setProportionallyPoint( rs.getInt("proportionally_point") );
				mp.setDisplaySeq(          rs.getInt("display_seq") );
				proportionally.setProportionally( mp );
				proportionally.setRatio( rs.getInt( "proportionally_ratio" ) );
				drp.setProportionally( proportionally );
				drp.setDesignated( rs.getBoolean( "designated_flag" ) );
				MstStaff	staff			=	new MstStaff();
				staff.setStaffID(rs.getInt("staff_id"));
				staff.setStaffNo(rs.getString("staff_no"));
				staff.setStaffName(0, "staff_name1");
				staff.setStaffName(1, "staff_name2");
				drp.setStaff( staff );
				drd.add( drp );
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return true;
	}
	
	/**
	 * 予約処理を行う。
	 * @return true - 成功
	 */
	public boolean reserve()
	{
		try
		{
                        DataReservation reserClone = (DataReservation) this.getReservation().clone();
			reservation.setStatus(1);
			// 施術開始予約時間を登録する
			
			
			ConnectionWrapper con = SystemInfo.getConnection();
			
			try
			{
				con.begin();
				
//				if(!this.registCommon(con, false))
				if(!this.registCommon(con))
				{
					return	false;
				}
				con.commit();   
			}
			catch(SQLException e)
			{
				con.rollback();
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
				return	false;
			}
                        //Start Add 20131002 lvut 
                        if(SystemInfo.getDatabase().equalsIgnoreCase("pos_hair_rizap") || SystemInfo.getDatabase().equalsIgnoreCase("pos_hair_rizap_bak")){
                            Integer reserNo = reservation.getReservationNo();
                            reservation.clear();
                            this.loadReservation(reserNo);
                            //Delete Start 2013-10-31 Hoa
                            //writeLog(reserClone, this.getReservation());
                            //Delete End 2013-10-31 Hoa
                        } 
                        //End Add 20131002 lvut 
                       
                       
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	true;
	}
        
        //IVS_LVTu start add 2018/04/06 GB_Studio 予約登録
        /**
	 * 予約処理を行う。
         * @param con
	 * @return true - 成功
         * @throws java.sql.SQLException
	 */
	public boolean registReserveStudio(ConnectionWrapper con) throws SQLException
	{
            reservation.setStatus(1);
            // 施術開始予約時間を登録する
            if(!this.registCommon(con))
            {
                    return	false;
            }

            if(SystemInfo.getDatabase().equalsIgnoreCase("pos_hair_rizap") || SystemInfo.getDatabase().equalsIgnoreCase("pos_hair_rizap_bak")){
                Integer reserNo = reservation.getReservationNo();
                reservation.clear();
                this.loadReservation(reserNo);
            } 

            return	true;
	}
        //IVS_LVTu end add 2018/04/06 GB_Studio 予約登録
        
        //IVS_NHTVINH start add 2016/08/31 New request #54380
        /**
         * send loginId and reservationNo to reservationAPI to register
         * then check response , if success return true, else return false
         * @return 
         */
        public Boolean sendReservationAPI(String login_id, Integer shopId, Integer reservation_no){
            try{
                //String url = "http://10.32.5.144/API/reservation/send_reservation.php";
                //接続情報はmst_apiから取得する。（api_id=0）
                mstApi = new MstAPI(0);
                String apiUrl = mstApi.getApiUrl();
                String url = apiUrl + "/s/send/reservation.php";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
                con.setRequestProperty( "charset", "utf-8");
                 
                //set parameter
                Map mapParam = new LinkedMap();
                mapParam.put("login_id", login_id);
                mapParam.put("shop_id", shopId);
                mapParam.put("reservation_no", reservation_no);
                Gson gson = new Gson(); 
                String jsonParam = gson.toJson(mapParam); 
                String urlParameters = "param=" + jsonParam;
                
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
                
                //get response
                BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
                if(response.toString().contains("\"code\":200")){
                    return true;
                }
            }catch(Exception e){
                return false;
            }
            return false;
        }
        
	
	/**
	 * 受付処理を行う。
	 * @return true - 成功
	 */
	public boolean receipt()
	{
		try
		{       DataReservation reserClone = (DataReservation) this.getReservation().clone();
                        if (reservation.getStatus() < 3) {
                            reservation.setStatus(2);
                            reservation.setSubStatus(1);
                        }
			
			ConnectionWrapper	con	=	SystemInfo.getConnection();
			
			try
			{
				con.begin();
				
//				if(!this.registCommon(con, true))
				if(!this.registCommon(con))
				{
					con.rollback();
					return	false;
				}
				
				con.commit();
			}
			catch(SQLException e)
			{
				con.rollback();
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
				return	false;
			}
                          //Start Add 20131002 lvut 
                        if(SystemInfo.getDatabase().equalsIgnoreCase("pos_hair_rizap") || SystemInfo.getDatabase().equalsIgnoreCase("pos_hair_rizap_bak")){
                            Integer reserNo = reservation.getReservationNo();
                            reservation.clear();
                            this.loadReservation(reserNo);
                            //Delete Start 2013-10-31 Hoa
                            //writeLog(reserClone, this.getReservation());
                            //Delete End 2013-10-31 Hoa
                        }
                          //End Add 20131002 lvut 
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	true;
	}
        
        //IVS_LVTu start add 2018/04/06 GB_Studio 予約登録
        /**
	 * 受付処理を行う。
         * @param con
	 * @return true - 成功
         * @throws java.sql.SQLException
	 */
	public boolean receiptStudio(ConnectionWrapper 	con) throws SQLException
	{ 
                if (reservation.getStatus() < 3) {
                    reservation.setStatus(2);
                    reservation.setSubStatus(1);
                }

                        if(!this.registCommon(con))
                        {
                                return	false;
                        }

                if(SystemInfo.getDatabase().equalsIgnoreCase("pos_hair_rizap") || SystemInfo.getDatabase().equalsIgnoreCase("pos_hair_rizap_bak")){
                    Integer reserNo = reservation.getReservationNo();
                    reservation.clear();
                    this.loadReservation(reserNo);
                }
		
		return	true;
	}
        //IVS_LVTu end add 2018/04/06 GB_Studio 予約登録
	
//	private boolean registCommon(ConnectionWrapper con, boolean isStay) throws SQLException
	private boolean registCommon(ConnectionWrapper con) throws SQLException
	{
		if(customer.getCustomerNo().equals("0") || customer.getCustomerID() == null)
		{
			if(SystemInfo.getSetteing().isShareCustomer())
			{
				customer.setShop(new MstShop());
				customer.getShop().setShopID(0);
			}
			else
			{
				customer.setShop(shop);
			}

//			customer.setCustomerID(null);

                        // かな名が未入力の場合は名前と同じものをセットする
                        if (customer.getCustomerKana(0).length() == 0) customer.setCustomerKana(0, customer.getCustomerName(0));
                        if (customer.getCustomerKana(1).length() == 0) customer.setCustomerKana(1, customer.getCustomerName(1));

			if(!customer.regist(con))
			{
				con.rollback();
				return	false;
			}

//			customer.setCustomerID(MstCustomer.getMaxCustomerID(con));

			if(customer.getCustomerID() == null)
			{
				con.rollback();
				return	false;
			}
		}

		reservation.setShop(shop);
		reservation.setCustomer(customer);
                //Start add 20131030 lvut 
                reservation.setResponse1(this.getReservation().getResponse1());
                reservation.setResponse2(this.getReservation().getResponse2());
                //End add 20131030 lvut 

//		if(!reservation.regist(con, isStay))
		if(!reservation.regist(con))
		{
			con.rollback();
			return	false;
		}

		if(reservation.getReservationNo() == null)
		{
			Integer		maxNo	=	this.getMaxReservationNo(con);

			if(maxNo == null)
			{
				con.rollback();
				return	false;
			}

			reservation.setReservationNo(maxNo);
		}
                
		if(!reservation.deleteDetail(con))
		{
			con.rollback();
			return	false;
		}
		// 按分データを削除する
		if( !reservation.deleteProportionally( con ) )
		{
			con.rollback();
			return false;
		}

		for(DataReservationDetail drd : reservation)
		{
			drd.setReservation(reservation);
                        // start add 20130806 nakhoa
                        if(drd.getContractShopId() == null){
                            drd.setContractShopId(reservation.getShop().getShopID());
                        }
                        // end add 20130806 nakhoa
			if(!drd.regist(con))
			{
				con.rollback();
				return	false;
			}
		}
                
                // IVS_Thanh start add 2014/07/07 GB_Mashu_業態主担当設定
                if (!reservation.deleteDataReservationMainStaff(con)) {
                        con.rollback();
                        return false; 
                    }
                for (DataReservationMainstaff drm : reservationMainstaffs) {
                    drm.setShop(reservation.getShop());
                    drm.setReservationNo(reservation.getReservationNo());
                    
                    if (!drm.regist(con)) {
                        con.rollback();
                        return false;
                    }
                }
                // IVS_Thanh end add 2014/07/07 GB_Mashu_業態主担当設定
                
		return	true;
	}
	 //Start Add log 20131002 lvut
	public void writeLog(DataReservation reserClone , DataReservation reservation){
         StringBuilder message = new StringBuilder();
         if(reserClone.getReservationNo() == null){
            message.append("Shop: ").append(reservation.getShop().getShopName()).append("    boot reservation \n");
            message.append("Reservation No:  ").append(reservation.getReservationNo()).append("\n");
            message.append("Customer ID: ").append(reservation.getCustomer().getCustomerID()).append("\n");
            message.append("Staff ID: ").append(reservation.getStaff().getStaffID()).append("\n");
         }else{
            message.append("Shop: ").append(reservation.getShop().getShopName()).append("    update reservation \n");
            message.append("Reservation No:  ").append(reservation.getReservationNo()).append("\n");
            message.append("Customer ID: ").append(reservation.getCustomer().getCustomerID()).append("\n");
            message.append("Staff ID: ").append(reservation.getStaff().getStaffID()).append("\n");
         }
         for(DataReservationDetail drd : reservation){
             if(drd.getProductId()!= null)
              message.append("Product ID: ").append(drd.getProductId()).append("\n");
              if(drd.getCourseFlg() != null && drd.getCourseFlg() == 2){
                  message.append("Contract Shop ID: ").append(drd.getContractShopId()).append("\n");
                  message.append("Contract No : ").append(drd.getConsumptionCourse().getContractNo()).append("\n");
              }
              message.append("------------------\n");
         }
         
         if(!reserClone.getCustomer().getCustomerID().equals(reservation.getCustomer().getCustomerID())){
             message.append("Customer ID: ").append(reserClone.getCustomer().getCustomerID()).append("   change --> customer ID: ")
                     .append(reservation.getCustomer().getCustomerID()).append("\n");
         }
         if(!reserClone.getStaff().getStaffID().equals(reservation.getStaff().getStaffID())){
            message.append("Staff ID: ").append(reserClone.getStaff().getStaffID()).append("   change --> staff: ")
                     .append(reservation.getStaff().getStaffID()).append("\n");
         }
         for(int i = 0; i<reserClone.size();i++){
             if(reserClone.get(i).getProductId() !=null && !reserClone.get(i).getProductId().equals(reservation.get(i).getProductId())){
                message.append("productID: ").append(reserClone.get(i).getProductId())
                        .append("   change  --> productID:").append(reservation.get(i).getProductId()).append("\n");
             }
             if( reserClone.get(i).getCourseFlg() != null && reserClone.get(i).getCourseFlg() == 2 ){
                 if(!reserClone.get(i).getContractShopId().equals(reservation.get(i).getContractShopId())){
                      message.append("Contract Shop ID: ").append(reserClone.get(i).getContractShopId())
                        .append("   change -->  Contract Shop ID:").append(reservation.get(i).getContractShopId()).append("\n");
                 }
                 if(!reserClone.get(i).getConsumptionCourse().getContractNo().equals(reservation.get(i).getConsumptionCourse().getContractNo())){
                      message.append("Contract No : ").append(reserClone.get(i).getConsumptionCourse().getContractNo())
                        .append("   change --> Contract No:").append(reservation.get(i).getConsumptionCourse().getContractNo()).append("\n");
                 }
            }
         }
         SystemInfo.getLogger().log(Level.INFO, message.toString());
        }
        //End Add log 20131002 lvut
	/**
	 * 予約番号の最大値（登録した予約番号）を取得する。
	 * @param con ConnectionWrapper
	 * @return 予約番号の最大値（登録した予約番号）
	 * @throws java.sql.SQLException SQLException
	 */
	private Integer	getMaxReservationNo(ConnectionWrapper con) throws SQLException
	{
		Integer		maxNo	=	null;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getMaxReservationNoSQL());
		
		if(rs.next())
		{
			maxNo	=	rs.getInt("max_no");
		}
		
		return	maxNo;
	}
        
        //IVS_LVTu start add 2018/04/06 GB_Studio 予約登録
        /**
         * 予約登録時に同一予約の有無をチェックして同一予約が存在している場合は
         * @param con
         * @return
         * @throws SQLException 
         */
        public boolean	isSameReservation(ConnectionWrapper con) throws SQLException
	{
		HashMap<Integer, DataReservation> drList = new HashMap<Integer, DataReservation>();
		ResultSetWrapper	rs	=	con.executeQuery(this.getSQLCheckSameReservation());
		
		while(rs.next())
		{
                    final int reservationNO = rs.getInt("reservation_no");
                    DataReservation dr = new DataReservation();
                    DataReservationDetail drd = new DataReservationDetail();
                    
                    drd.setReservationDetailNo(rs.getInt("reservation_detail_no"));
                    drd.getReservationDatetime().setTime(rs.getTimestamp("reservation_datetime"));

                    MstTechnicClass mtc = new MstTechnicClass();
                    mtc.setTechnicClassID(rs.getInt("technic_class_id"));
                    MstTechnic mt = new MstTechnic();
                    mt.setTechnicClass(mtc);
                    mt.setTechnicID(rs.getInt("technic_id"));
                    mt.setTechnicName(rs.getString("technic_name"));
                    mt.setOperationTime(rs.getInt("operation_time"));
                    drd.setTechnic(mt);

                    drd.getBed().setBedID(rs.getInt("bed_id"));
                    drd.getBed().setBedName(rs.getString("bed_name"));
                    drd.getStaff().setStaffID(rs.getInt("staff_id"));
                    
                    if(drList.containsKey(reservationNO)) {
                        dr = drList.get(reservationNO);
                    }else {
                        MstShop mshop = new MstShop();
                        mshop.setShopID(rs.getInt("shop_id"));
                        mshop.setBed(rs.getInt("bed"));
                        dr.setShop(mshop);
                        
                        MstStaff mst = new MstStaff();
                        mst.setStaffID(rs.getInt("charge_staff_id"));
                        dr.setStaff(mst);
                    }
                    drd.setReservation(dr);
                    dr.add(drd);
                    drList.put(reservationNO, dr);
		}
                
                for(Map.Entry<Integer, DataReservation> entry : drList.entrySet()) {
                    DataReservation dr = entry.getValue();
                    
                    if(this.reservation.reservationEquals(dr)) {
                        return true;
                    }
                }
		
		return	false;
	}
        
        /**
         * 予約登録時に同一予約の有無をチェックして同一予約が存在している場合は
         * @return String
         */
        private String getSQLCheckSameReservation() {
            GregorianCalendar fromDate = new GregorianCalendar();
            fromDate.setTime(this.getDate().getTime());
            fromDate.set(date.HOUR_OF_DAY, this.getShop().getOpenHour());
            fromDate.set(date.MINUTE, this.getShop().getOpenMinute());
        
            GregorianCalendar nextDate = new GregorianCalendar();
            nextDate.setTime(fromDate.getTime());
            nextDate.add(nextDate.DAY_OF_MONTH, 1);
            StringBuilder sql = new StringBuilder();
            
            sql.append("SELECT dr.shop_id, ms.bed,\n");
            sql.append("		  dr.reservation_no,\n");
            sql.append("		  dr.designated_flag,\n");
            sql.append("		  dr.staff_id AS charge_staff_id,\n");
            sql.append("		  drd.reservation_detail_no,\n");
            sql.append("		  drd.reservation_datetime,\n");
            sql.append("		  CASE\n");
            sql.append("			  WHEN drd.staff_id = 0 THEN NULL\n");
            sql.append("			  ELSE drd.staff_id\n");
            sql.append("		  END AS staff_id,\n");
            sql.append("		  CASE\n");
            sql.append("			  WHEN drd.bed_id = 0 THEN NULL\n");
            sql.append("			  ELSE drd.bed_id\n");
            sql.append("		  END AS bed_id,\n");
            sql.append("		  mb.bed_name,\n");
            sql.append("		  drd.technic_id,\n");
            sql.append("		  mt.technic_class_id,\n");
            sql.append("		  mtc.technic_class_name,\n");
            sql.append("		  CASE\n");
            sql.append("			  WHEN drd.course_flg = 1 THEN c.course_name\n");
            sql.append("			  WHEN drd.course_flg = 2 THEN c.course_name\n");
            sql.append("			  ELSE mt.technic_name\n");
            sql.append("		  END AS technic_name,\n");
            sql.append("	  coalesce(drd.operation_time, mt.operation_time) AS operation_time,\n");
            sql.append("	  dr.status,\n");
            sql.append("	  drd.course_flg\n");
            sql.append("   FROM data_reservation dr\n");
            sql.append("   INNER JOIN data_reservation_detail drd ON drd.shop_id = dr.shop_id\n");
            sql.append("   AND drd.reservation_no = dr.reservation_no\n");
            sql.append("   AND drd.delete_date IS NULL\n");
            sql.append("   INNER JOIN mst_shop ms ON ms.shop_id = dr.shop_id\n");
            sql.append("   LEFT OUTER JOIN mst_bed mb ON mb.bed_id = drd.bed_id\n");
            sql.append("   LEFT OUTER JOIN mst_technic mt ON mt.technic_id = drd.technic_id\n");
            sql.append("   LEFT OUTER JOIN mst_technic_class mtc ON mtc.technic_class_id = mt.technic_class_id\n");
            sql.append("   LEFT OUTER JOIN mst_course c ON c.course_id = drd.technic_id\n");
            sql.append("   LEFT OUTER JOIN mst_course_class cc ON cc.course_class_id = c.course_class_id\n");
            sql.append("   WHERE dr.delete_date IS NULL\n");
            sql.append("	 AND dr.shop_id = ").append(this.getShop().getShopID());
            sql.append("	 AND drd.reservation_datetime >= ").append(SQLUtil.convertForSQL(fromDate)).append("\n");
            sql.append("	 AND drd.reservation_datetime < ").append(SQLUtil.convertForSQL(nextDate)).append("\n");
            if(this.getReservation() != null && this.getReservation().getReservationNo() != null) {
                sql.append("	 AND drd.reservation_no <> ").append(SQLUtil.convertForSQL(this.getReservation().getReservationNo())).append("\n");
            }
            sql.append("   ORDER BY \n");
            sql.append("	 drd.reservation_no,\n");
            sql.append("	 drd.reservation_datetime,\n");
            sql.append("	 drd.reservation_detail_no\n");
            
            return sql.toString();
        }
        //IVS_LVTu end add 2018/04/06 GB_Studio 予約登録
	
	/**
	 * 全スタッフを取得するＳＱＬ文を取得する。
	 * @return 全スタッフを取得するＳＱＬ文
	 */
	private String getLoadStaffsSQL()
	{
		return	"select *\n" +
			"from mst_staff ms\n" +
			"where ms.delete_date is null\n" +
			"order by ms.staff_id\n";
	}
	
	/**
	 * 全ベッドを取得するＳＱＬ文を取得する。
	 * @return 全ベッドを取得するＳＱＬ文
	 */
	private String getLoadBedsSQL()
	{
		return	"select *\n" +
			"from mst_bed mb\n" +
			"where mb.delete_date is null\n" +
			"and mb.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
			"order by mb.display_seq\n";
	}
	
	/**
	 * 予約データを取得するＳＱＬ文を取得する。
	 * @param reservationNo 予約番号の配列
	 * @return 予約データを取得するＳＱＬ文
	 */
	private String getLoadReservationSQL(Integer reservationNo)
	{
		return	"select dr.shop_id,\n" +
				"dr.reservation_no,\n" +
				"dr.designated_flag as charge_staff_designated_flag,\n" +
				"cms.staff_id as charge_staff_id,\n" +
				"cms.staff_no as charge_staff_no,\n" +
				"cms.staff_name1 as charge_staff_name1,\n" +
				"cms.staff_name2 as charge_staff_name2,\n" +
				"drd.reservation_detail_no,\n" +
				"drd.reservation_datetime,\n" +
				"drd.designated_flag as menu_staff_designated_flag,\n" +
				"dr.customer_id,\n" +
				"mc.customer_no,\n" +
				"mc.customer_name1,\n" +
				"mc.customer_name2,\n" +
				"mt.technic_class_id,\n" +
				"mtc.technic_class_name,\n" +
                                 //nhanvt start add 20150128 New request #35057
                                "mtc.shop_category_id as shop_category_id,\n" +
                                 //nhanvt end add 20150128 New request #35057
				"drd.technic_id,\n" +
				"mt.technic_no,\n" +
				"mt.technic_name || case when (mt.technic_no like 'mo-rsv-%') or (mt.mobile_flag=1) then '（予約用）' else '' end as technic_name,\n" +
				"coalesce(drd.operation_time, mt.operation_time) as operation_time,\n" +
				"drd.staff_id,\n" +
				"ms.staff_no,\n" +
				"ms.staff_name1,\n" +
				"ms.staff_name2,\n" +
				"drd.bed_id,\n" +
				"mb.bed_name,\n" +
				"dr.status,\n" +
				"dr.sub_status,\n" +
				"dr.comment,\n" +
				"dr.next_flag,\n" +
				"dr.preorder_flag,\n" +
				"rms.staff_id as reg_staff_id,\n" +
				"rms.staff_no as reg_staff_no,\n" +
				"rms.staff_name1 as reg_staff_name1,\n" +
				"rms.staff_name2 as reg_staff_name2,\n" +
				"dr.insert_date,\n" +
				"dr.update_date,\n" +
                                "drd.contract_no ,\n" +
                                "drd.contract_detail_no ,\n" +
                                // start add 20130812 nakhoa
                                "drd.contract_shop_id ,\n" +
                                // end add 20130812 nakhoa
                                // start add 20131030 lvut
                                "dr.response_id1,\n" +
                                "dr.response_id2,\n" +
                                // end add 20131030 lvut
                                "drd.course_flg\n" +
				"from data_reservation dr\n" +
				"left outer join mst_staff as cms on\n" +
				"dr.staff_id = cms.staff_id\n" +
				"left outer join mst_staff as rms on\n" +
				"dr.reg_staff_id = rms.staff_id\n" +
				"inner join data_reservation_detail drd\n" +
				"on drd.shop_id = dr.shop_id \n" +
				"and drd.reservation_no = dr.reservation_no\n" +
				"and (dr.mobile_flag = 3 or drd.delete_date is null)\n" +
				"left outer join mst_customer mc\n" +
				"on mc.customer_id = dr.customer_id\n" +
				"left outer join mst_Technic mt\n" +
				"on mt.technic_id = drd.technic_id\n" +
				"left outer join mst_technic_class mtc\n" +
				"on mtc.technic_class_id = mt.technic_class_id\n" +
				"left outer join mst_staff ms\n" +
				"on ms.staff_id = drd.staff_id\n" +
				"left outer join mst_bed mb\n" +
				"on mb.shop_id = dr.shop_id\n" +
				"and mb.bed_id = drd.bed_id\n" +
				"where dr.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and dr.reservation_no = " + reservationNo.toString() + "\n" +
                                "and drd.course_flg is null\n" +

                                //コース契約
                                "union all\n" +
                                "select dr.shop_id,\n" +
				"dr.reservation_no,\n" +
				"dr.designated_flag as charge_staff_designated_flag,\n" +
				"cms.staff_id as charge_staff_id,\n" +
				"cms.staff_no as charge_staff_no,\n" +
				"cms.staff_name1 as charge_staff_name1,\n" +
				"cms.staff_name2 as charge_staff_name2,\n" +
				"drd.reservation_detail_no,\n" +
				"drd.reservation_datetime,\n" +
				"drd.designated_flag as menu_staff_designated_flag,\n" +
				"dr.customer_id,\n" +
				"mc.customer_no,\n" +
				"mc.customer_name1,\n" +
				"mc.customer_name2,\n" +
				"c.course_class_id as technic_class_id,\n" +
				"cc.course_class_name as technic_class_name,\n" +
                                 //nhanvt start edit 20150128 New request #35057
                                "cc.shop_category_id as shop_category_id ,\n" +
                                 //nhanvt end edit 20150128 New request #35057
				"drd.technic_id,\n" +
				"'' as technic_no,\n" +
				"c.course_name as technic_name,\n" +
				"coalesce(drd.operation_time, c.operation_time) as operation_time,\n" +
				"drd.staff_id,\n" +
				"ms.staff_no,\n" +
				"ms.staff_name1,\n" +
				"ms.staff_name2,\n" +
				"drd.bed_id,\n" +
				"mb.bed_name,\n" +
				"dr.status,\n" +
				"dr.sub_status,\n" +
				"dr.comment,\n" +
				"dr.next_flag,\n" +
				"dr.preorder_flag,\n" +
				"rms.staff_id as reg_staff_id,\n" +
				"rms.staff_no as reg_staff_no,\n" +
				"rms.staff_name1 as reg_staff_name1,\n" +
				"rms.staff_name2 as reg_staff_name2,\n" +
				"dr.insert_date,\n" +
				"dr.update_date,\n" +
                                "drd.contract_no ,\n" +
                                "drd.contract_detail_no ,\n" +
                                // start add 20130812 nakhoa
                                "drd.contract_shop_id ,\n" +
                                // end add 20130812 nakhoa
                                // start add 20131030 lvut
                                "dr.response_id1,\n" +
                                "dr.response_id2,\n" +
                                // end add 20131030 lvut
                                "drd.course_flg\n" +
				"from data_reservation dr\n" +
				"left outer join mst_staff as cms on\n" +
				"dr.staff_id = cms.staff_id\n" +
				"left outer join mst_staff as rms on\n" +
				"dr.reg_staff_id = rms.staff_id\n" +
				"inner join data_reservation_detail drd\n" +
				"on drd.shop_id = dr.shop_id \n" +
				"and drd.reservation_no = dr.reservation_no\n" +
				"and (dr.mobile_flag = 3 or drd.delete_date is null)\n" +
				"left outer join mst_customer mc\n" +
				"on mc.customer_id = dr.customer_id\n" +
				"left outer join mst_course c\n" +
				"on c.course_id = drd.technic_id\n" +
				"left outer join mst_course_class cc\n" +
				"on cc.course_class_id = c.course_class_id\n" +
				"left outer join mst_staff ms\n" +
				"on ms.staff_id = drd.staff_id\n" +
				"left outer join mst_bed mb\n" +
				"on mb.shop_id = dr.shop_id\n" +
				"and mb.bed_id = drd.bed_id\n" +
				"where dr.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and dr.reservation_no = " + reservationNo.toString() + "\n" +
                                "and drd.course_flg = 1\n" +

                                //消化コース
                                "union all\n" +
                                "select distinct dr.shop_id,\n" +
				"dr.reservation_no,\n" +
				"dr.designated_flag as charge_staff_designated_flag,\n" +
				"cms.staff_id as charge_staff_id,\n" +
				"cms.staff_no as charge_staff_no,\n" +
				"cms.staff_name1 as charge_staff_name1,\n" +
				"cms.staff_name2 as charge_staff_name2,\n" +
				"drd.reservation_detail_no,\n" +
				"drd.reservation_datetime,\n" +
				"drd.designated_flag as menu_staff_designated_flag,\n" +
				"dr.customer_id,\n" +
				"mc.customer_no,\n" +
				"mc.customer_name1,\n" +
				"mc.customer_name2,\n" +
				"c.course_class_id as technic_class_id,\n" +
				"cc.course_class_name as technic_class_name,\n" +
                                 //nhanvt start add 20150128 New request #35057
                                "cc.shop_category_id as shop_category_id,\n" +
                                 //nhanvt end add 20150128 New request #35057
				"drd.technic_id,\n" +
				"'' as technic_no,\n" +
				"c.course_name as technic_name,\n" +
				"coalesce(drd.operation_time, c.operation_time) as operation_time,\n" +
				"drd.staff_id,\n" +
				"ms.staff_no,\n" +
				"ms.staff_name1,\n" +
				"ms.staff_name2,\n" +
				"drd.bed_id,\n" +
				"mb.bed_name,\n" +
				"dr.status,\n" +
				"dr.sub_status,\n" +
				"dr.comment,\n" +
				"dr.next_flag,\n" +
				"dr.preorder_flag,\n" +
				"rms.staff_id as reg_staff_id,\n" +
				"rms.staff_no as reg_staff_no,\n" +
				"rms.staff_name1 as reg_staff_name1,\n" +
				"rms.staff_name2 as reg_staff_name2,\n" +
				"dr.insert_date,\n" +
				"dr.update_date,\n" +
                                "drd.contract_no ,\n" +
                                "drd.contract_detail_no ,\n" +
                                // start add 20130812 nakhoa
                                "drd.contract_shop_id ,\n" +
                                // end add 20130812 nakhoa
                                // start add 20131030 lvut
                                "dr.response_id1,\n" +
                                "dr.response_id2,\n" +
                                // end add 20131030 lvut
                                "drd.course_flg\n" +
				"from data_reservation dr\n" +
				"left outer join mst_staff as cms on\n" +
				"dr.staff_id = cms.staff_id\n" +
				"left outer join mst_staff as rms on\n" +
				"dr.reg_staff_id = rms.staff_id\n" +
				"inner join data_reservation_detail drd\n" +
				"on drd.shop_id = dr.shop_id \n" +
				"and drd.reservation_no = dr.reservation_no\n" +
				"and (dr.mobile_flag = 3 or drd.delete_date is null)\n" +
				"left outer join mst_customer mc\n" +
				"on mc.customer_id = dr.customer_id\n" +
                                "left outer join data_contract_digestion dcd\n" +
                                "on dcd.shop_id = drd.shop_id and dcd.contract_no = drd.contract_no and dcd.contract_detail_no = drd.contract_detail_no\n" +
                                "left outer join mst_course c\n" +
                                "on c.course_id = drd.technic_id\n" +
				"left outer join mst_course_class cc\n" +
				"on cc.course_class_id = c.course_class_id\n" +
				"left outer join mst_staff ms\n" +
				"on ms.staff_id = drd.staff_id\n" +
				"left outer join mst_bed mb\n" +
				"on mb.shop_id = dr.shop_id\n" +
				"and mb.bed_id = drd.bed_id\n" +
				"where dr.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and dr.reservation_no = " + reservationNo.toString() + "\n" +
                                "and drd.course_flg = 2\n" +

				"order by reservation_datetime, reservation_detail_no\n";
	}
	
	private String loadReservationProportionallySQL( Integer reservationNo, Integer reservationDetailNo, Integer technicID )
	{
		return
			"select\n" +
			"mp.*,\n" +
			"ms.staff_id,\n" +
			"ms.staff_no,\n" +
			"ms.staff_name1,\n" +
			"ms.staff_name2,\n" +
			"dp.*,\n" +
			"drp.designated_flag\n" +
			"from\n" +
			"data_proportionally as dp\n" +
			"inner join mst_proportionally as mp on\n" +
			"mp.proportionally_id = dp.proportionally_id\n" +
			"left outer join data_reservation_proportionally as drp on\n" +
			"drp.delete_date is null\n" +
			"and drp.reservation_no = " + SQLUtil.convertForSQL( reservationNo ) + "\n" +
			"and drp.reservation_detail_no = " + SQLUtil.convertForSQL( reservationDetailNo ) + "\n" +
			"and drp.data_proportionally_id = dp.data_proportionally_id\n" +
			"left outer join mst_staff as ms on\n" +
			"ms.staff_id = drp.staff_id\n" +
			"and drp.data_proportionally_id = dp.data_proportionally_id\n" +
			"where\n" +
			"dp.delete_date is null\n" +
			"and dp.technic_id = " + SQLUtil.convertForSQL( technicID ) + "\n" +
			"order by\n" +
			"mp.display_seq, dp.data_proportionally_id\n" +
			";\n";
	}
	
	/**
	 * 予約番号の最大値（登録した予約番号）を取得するＳＱＬ文を取得する。
	 * @return 予約番号の最大値（登録した予約番号）を取得するＳＱＬ文
	 */
	private String getMaxReservationNoSQL()
	{
		return	"select max(dr.reservation_no) as max_no\n" +
				"from data_reservation dr\n" +
				"where dr.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n";
	}
        //IVS_LVTu start add 2015/12/15 New request #44115
        public boolean Registreservation(ConnectionWrapper con)
	{
		try
		{
                    reservation.setStatus(1);
                    // 施術開始予約時間を登録する

                    try
                    {

                        if(!this.registCommon(con))
                        {
                                return	false;
                        }

                    }
                    catch(SQLException e)
                    {
                        SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                        return	false;
                    }
                       
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return	true;
	}
        //IVS_LVTu end add 2015/12/15 New request #44115
        
        //IVS_LVTu start add 2016/07/13 New request #50223
        private void getDetailReservation() {
            this.loadReservation(this.reservation.getReservationNo());
            
            GregorianCalendar calendar = new GregorianCalendar();

            if (0 < this.reservation.size()) {

                calendar.setTime(this.reservation.get(0).getReservationDatetime().getTime());

                if (calendar.get(Calendar.HOUR_OF_DAY) < this.getShop().getOpenHour()
                        || (calendar.get(Calendar.HOUR_OF_DAY) == this.getShop().getOpenHour()
                        && calendar.get(Calendar.MINUTE) < this.getShop().getOpenMinute())) {

                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                }

                GregorianCalendar startTime = this.reservation.get(0).getReservationDatetime();
                GregorianCalendar endTime = (GregorianCalendar) this.reservation.get(this.reservation.size() - 1).getReservationDatetime().clone();

                // 予約時間
                //this.ReservationAPI.setStartTime(getFormatTime(calendar.getTime(), startTime));

                // 終了時間
                if (this.reservation.get(this.reservation.size() - 1).getCourse() != null) {
                    endTime.add(Calendar.MINUTE, this.reservation.get(this.reservation.size() - 1).getCourse().getOperationTime());
                } else if (this.reservation.get(this.reservation.size() - 1).getConsumptionCourse() != null) {
                    endTime.add(Calendar.MINUTE, this.reservation.get(this.reservation.size() - 1).getConsumptionCourse().getOperationTime());
                } else {
                    endTime.add(Calendar.MINUTE, this.reservation.get(this.reservation.size() - 1).getTechnic().getOperationTime());
                }
                //this.ReservationAPI.setEndTime(getFormatTime(calendar.getTime(), endTime));
            } else {
                calendar.setTime(this.getDate().getTime());
            }
            this.setDate(calendar);
        }
        
        private String getFormatTime(java.util.Date dt, Calendar cal) {

        int h = cal.get(Calendar.HOUR_OF_DAY);
        int m = cal.get(Calendar.MINUTE);
        if (Integer.parseInt(DateUtil.format(dt, "dd")) + 1 == cal.get(Calendar.DAY_OF_MONTH)) {
            h += 24;
        }

        return String.format("%1$02d", h) + ":" + String.format("%1$02d", m);
    }
    //IVS_LVTu end add 2016/07/13 New request #50223
        
    // GB Start 20161117 #58629
    // フリー項目区分の予約使用欄チェックがゼロならfalseを返す
    public boolean getReserveUseFlag(){
        
                ConnectionWrapper   con      =      SystemInfo.getConnection();
		
		try
		{
			ResultSetWrapper	rs	=	con.executeQuery(
                            this.getSelectReserveUseSQL() );
			
			if (rs.next())
			{
                            return true;
			}else
                        {
                            return false;
                        }
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
		return true;

    }
    
    private String getSelectReserveUseSQL(){
        
        return
                "SELECT * FROM mst_free_heading_class\n" +
                "WHERE reserve_use_type = 't'\n" +
                "AND delete_date IS NULL\n";
        
        /* return
                "SELECT * FROM mst_free_heading_class\n" +
                "WHERE use_type = 't'\n" +
                "AND delete_date IS NULL\n"; */
                
    }
    
    /**
     * フリー項目を読み込む
     * 
     * @return true - 成功
     */
    public boolean loadFreeHeadingInfo(MstCustomer cus) {
        boolean result = false;
        ConnectionWrapper con = SystemInfo.getConnection();

        try {
            getFreeHeadingInfo(con, cus);
            result = true;
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return result;
    }

    /**
     * フリー項目データを取得する
     */
    private void getFreeHeadingInfo(ConnectionWrapper con, MstCustomer cus) {
        
        freeHeading.clear();

        try {
            ResultSetWrapper rs = con.executeQuery(
                    this.getFreeHeadingSelectSQL(cus));

            while (rs.next()) {
                MstCustomerFreeHeading mt = new MstCustomerFreeHeading();
                mt.setData(rs);
                mt.setFreeHeadingClassName(rs.getString("free_heading_class_name"));
                if(rs.getInt("input_type") == 0){
                    mt.setFreeHeadingName(rs.getString("free_heading_name"));
                }else if(rs.getInt("input_type") == 1){
                    mt.setFreeHeadingText(rs.getString("free_heading_text"));
                }
                freeHeading.add(mt);
            }
            rs.close();
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }
    
    private String getFreeHeadingSelectSQL(MstCustomer cus) {
        return "select\n"
                + "mfhc.*,\n"
                + "mc.*,\n"
                + "mfh.*,\n"
                + "coalesce( mfh.free_heading_id, -1 ),\n"
                + "mfh.free_heading_name,\n"
                + "mfh.display_seq\n"
                + " , mcfh.free_heading_text \n "
                + "from\n"
                + "mst_free_heading_class as mfhc\n"
                + "left join mst_customer_free_heading as mcfh on\n"
                + "mcfh.delete_date is null\n"
                + "and mcfh.customer_id = " + SQLUtil.convertForSQL(cus.getCustomerID()) + "\n"
                + "and mcfh.free_heading_class_id = mfhc.free_heading_class_id\n"
                + "left join mst_customer as mc on\n"
                + "mc.delete_date is null\n"
                + "and mc.customer_id = " + SQLUtil.convertForSQL(cus.getCustomerID()) + "\n"
                + "left join mst_free_heading as mfh on\n"
                + "mfh.delete_date is null\n"
                + "and mfh.free_heading_class_id = mcfh.free_heading_class_id\n"
                + "and mfh.free_heading_id = mcfh.free_heading_id\n"
                + "where\n"
                + "mfhc.delete_date is null\n"
                + "and mfhc.reserve_use_type = 't'\n"
                + "order by\n"
                + "mfhc.free_heading_class_id\n"
                + ";\n";
    }
    // GB End 20161117 #58629
}
