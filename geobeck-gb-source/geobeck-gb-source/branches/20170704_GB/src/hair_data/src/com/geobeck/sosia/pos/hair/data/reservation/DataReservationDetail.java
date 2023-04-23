/*
 * DataReservationDetail.java
 *
 * Created on 2007/02/15, 10:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.data.reservation;

import com.geobeck.sosia.pos.hair.data.account.ConsumptionCourse;
import com.geobeck.sosia.pos.hair.data.account.Course;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.hair.master.company.MstBed;
import com.geobeck.sosia.pos.hair.master.product.MstTechnic;
import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 *
 * @author katagiri
 */
public class DataReservationDetail extends ArrayList<DataReservationProportionally>
{
	/**
	 * 予約
	 */
	protected	DataReservation		reservation			=	null;
	/**
	 * 予約詳細No.
	 */
	protected	Integer				reservationDetailNo	=	null;
	/**
	 * 予約日時
	 */
	protected	GregorianCalendar	reservationDatetime	=	new GregorianCalendar();
	/**
	 * 技術
	 */
	protected	MstTechnic			technic				=	new MstTechnic();
	/**
	 * 施術台
	 */
	protected	MstBed				bed					=	new MstBed();
	/**
	 * 指名
	 */
	protected	boolean				designated			= false;
	/**
	 * スタッフ
	 */
	protected	MstStaff			staff				=	new MstStaff();

        /**
         * コース契約・消化フラグ
         * 1:コース契約
         * 2:消化コース
         */
        protected Integer courseFlg = null;

        /**
         * コース契約番号
         */
        protected Integer contractNo = null;

        /**
         * コース契約詳細番号
         */
        protected Integer contractDetailNo = null;

        /**
         * コース契約
         */
        protected Course course = null;

        /**
         * 消化コース
         */
        protected ConsumptionCourse consumptionCourse = null;
        
        // start add 20130806 nakhoa
        /**
         * 
         */
        protected Integer contractShopId = null;
        // end add 20130806 nakhoa

	/** Creates a new instance of DataReservationDetail */
	public DataReservationDetail()
	{
	}

	/**
	 * 店舗を取得する。
	 * @return 店舗
	 */
	public DataReservation getReservation()
	{
		return reservation;
	}

	/**
	 * 予約をセットする。
	 * @param reservation 予約
	 */
	public void setReservation(DataReservation reservation)
	{
		this.reservation = reservation;
	}

	/**
	 * 店舗を取得する。
	 * @return 店舗
	 */
	public MstShop getShop()
	{
		if(reservation != null)
		{
			return reservation.getShop();
		}
		else
		{
			return	null;
		}
	}
	
	/**
	 * 予約No.を取得する。
	 * @return 予約No.
	 */
	public Integer getReservationNo()
	{
		if(reservation != null)
		{
			return reservation.getReservationNo();
		}
		else
		{
			return	null;
		}
	}
	
	/**
	 * 予約No.を取得する。
	 * @return 予約No.
	 */
	public Integer getReservationDetailNo()
	{
		return reservationDetailNo;
	}

	/**
	 * 予約No.をセットする。
	 * @param reservationNo 予約No.
	 */
	public void setReservationDetailNo(Integer reservationDetailNo)
	{
		this.reservationDetailNo = reservationDetailNo;
	}

	/**
	 * 予約日時を取得する。
	 * @return 予約日時
	 */
	public GregorianCalendar getReservationDatetime()
	{
		return reservationDatetime;
	}
	
	/**
	 * 予約時間を取得する。
	 * @return 予約時間
	 */
	public String getReservationTime(GregorianCalendar baseDate)
	{
//		if(baseDate.get(Calendar.DAY_OF_MONTH) == reservationDatetime.get(Calendar.DAY_OF_MONTH))
//		{
//			return	String.format("%1$tH:%1$tM", reservationDatetime);
//		}
//		else
//		{
//			return	String.format("%1$02d:%2$tM",
//					reservationDatetime.get(Calendar.HOUR_OF_DAY) + 24,
//					reservationDatetime);
//		}
		return	String.format("%1$tH:%1$tM", reservationDatetime);
	}
	
	/**
	 * 開始-終了時間を取得する
	 */
	public String getReservationAllTime( GregorianCalendar baseDate )
	{
		String sTime, fTime;
		GregorianCalendar wTime = (GregorianCalendar)reservationDatetime.clone();
		
//		if(baseDate.get(Calendar.DAY_OF_MONTH) == wTime.get(Calendar.DAY_OF_MONTH))
//		{
//			sTime = String.format("%1$tH:%1$tM", wTime);
//		}
//		else
//		{
//			sTime = String.format("%1$02d:%2$tM",
//					wTime.get(Calendar.HOUR_OF_DAY) + 24,
//					wTime);
//		}
		sTime = String.format("%1$tH:%1$tM", wTime);
		
		wTime.add( GregorianCalendar.MINUTE, this.getTechnic().getOperationTime() );
		
//		if(baseDate.get(Calendar.DAY_OF_MONTH) == wTime.get(Calendar.DAY_OF_MONTH))
//		{
//			fTime = String.format("%1$tH:%1$tM", wTime);
//		}
//		else
//		{
//			fTime = String.format("%1$02d:%2$tM",
//					wTime.get(Calendar.HOUR_OF_DAY) + 24,
//					wTime
//					);
//		}
		fTime = String.format("%1$tH:%1$tM", wTime);
		//return sTime + " - " + fTime + " (" + this.getTechnic().getOperationTime() + ")";
		//uchiyama
		return fTime ;
	}

	/**
	 * 予約日時をセットする。
	 * @param reservationDatetime 予約日時
	 */
	public void setReservationDatetime(GregorianCalendar reservationDatetime)
	{
		this.reservationDatetime = reservationDatetime;
	}

	/**
	 * 予約日をセットする。
	 * @param reservationDate 予約日
	 */
	public void setReservationDate(java.util.Date reservationDate)
	{
		if(this.reservationDatetime == null)
				this.reservationDatetime	=	new GregorianCalendar();
		this.reservationDatetime.setTime(reservationDate);
	}

	/**
	 * 技術を取得する。
	 * @return 技術
	 */
	public MstTechnic getTechnic()
	{
		return technic;
	}

	/**
	 * 技術をセットする。
	 * @param technic 技術
	 */
	public void setTechnic(MstTechnic technic)
	{
		this.technic = technic;
	}

        /**
         * コース契約を取得する
         * @return
         */
        public Course getCourse()
        {
            return course;
        }

        /**
         * コース契約をセットする
         * @param course
         */
        public void setCourse(Course course)
        {
            this.course = course;
        }

        /**
         * 消化コースを取得する
         * @return
         */
        public ConsumptionCourse getConsumptionCourse()
        {
            return consumptionCourse;
        }

        /**
         * 消化コースをセットする
         * @param consumptionCourse
         */
        public void setConsumptionCourse(ConsumptionCourse consumptionCourse)
        {
            this.consumptionCourse = consumptionCourse;
        }

	/**
	 * 施術台を取得する。
	 * @return 施術台
	 */
	public MstBed getBed()
	{
		return bed;
	}

	/**
	 * 施術台をセットする。
	 * @param bed 施術台
	 */
	public void setBed(MstBed bed)
	{
		this.bed = bed;
	}

	/**
	 * 指名フラグを取得する
	 * @return designated 指名 true:指名 false:フリー
	 */
	public boolean getDesignated()
	{
		return designated;
	}
	
	/**
	 * 指名フラグをセットする
	 * @param designated 指名フラグ
	 */
	public void setDesignated( boolean designated )
	{
		this.designated = designated;
	}
	
	/**
	 * スタッフを取得する。
	 * @return スタッフ
	 */
	public MstStaff getStaff()
	{
		return staff;
	}

	/**
	 * スタッフをセットする。
	 * @param staff スタッフ
	 */
	public void setStaff(MstStaff staff)
	{
		this.staff = staff;
	}

        /**
         * コース契約・消化フラグを取得する
         * @return
         */
        public Integer getCourseFlg()
        {
            return courseFlg;
        }

        /**
         * コース契約・消化フラグをセットする
         * @param courseFlg
         */
        public void setCourseFlg(Integer courseFlg)
        {
            this.courseFlg = courseFlg;
        }

        /**
         * コース契約番号を取得する
         * @return
         */
        public Integer getContractNo()
        {
            return contractNo;
        }

        /**
         * コース契約番号をセットする
         * @param contractNo
         */
        public void setContractNo(Integer contractNo)
        {
            this.contractNo = contractNo;
        }

        /**
         * コース契約詳細番号を取得する
         * @return
         */
        public Integer getContractDetailNo()
        {
            return contractDetailNo;
        }

        /**
         * コース契約詳細番号をセットする
         * @param contractedDetailNo
         */
        public void setContractDetailNo(Integer contractDetailNo)
        {
            this.contractDetailNo = contractDetailNo;
        }

        // start add 20130806 nakhoa
        public Integer getContractShopId(){
            return this.contractShopId;
        }
        
        public void setContractShopId(Integer contractShopId){
            this.contractShopId = contractShopId;
        }
        // end add 20130806 nakhoa
        
	/**
	 * 予約データをセットする。
	 * @param dr 予約データ
	 */
	public void set(DataReservationDetail drd)
	{
		this.setReservation(drd.getReservation());
		this.setReservationDetailNo(drd.getReservationDetailNo());
		this.setReservationDatetime(drd.getReservationDatetime());
		this.setTechnic(drd.getTechnic());
		this.setBed(drd.getBed());
		this.setStaff(drd.getStaff());
		this.setDesignated( drd.getDesignated() );
                
	}
	
	
	/**
	 * データを登録する。
	 * @param con 
	 * @throws java.sql.SQLException 
	 * @return 
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		String	sql		=	"";
		
		if(isExists(con))
		{
			sql	=	this.getUpdateSQL();
		}
		else
		{
			sql	=	this.getInsertSQL();
		}
		
		
		if( con.executeUpdate(sql) != 1 ) return false;
		
		Integer		maxNo	=	this.getMaxReservationDetailNo(con);

		if(maxNo == null)
		{
			return	false;
		}
		this.setReservationDetailNo( maxNo );

		// 按分が存在する場合には按分データを保存する
		if( 0 < size() )
		{
			for( DataReservationProportionally drp : this )
			{
				drp.setReservationDetail( this );
				if( !drp.regist( con ) ) return false;
			}
		}
		return true;
	}
	
	
	/**
	 * データを削除する。（論理削除）
	 * @param con 
	 * @throws java.sql.SQLException 
	 * @return 
	 */
	public boolean delete(ConnectionWrapper con) throws SQLException
	{
		String	sql		=	"";
		
		if(isExists(con))
		{
			sql	=	this.getDeleteSQL();
		}
		else
		{
			return	false;
		}
		
		if(con.executeUpdate(sql) == 1)
		{
			return	true;
		}
		else
		{
			return	false;
		}
	}
	
	/**
	 * データが存在するかチェックする。
	 * @param con 
	 * @throws java.sql.SQLException 
	 * @return 
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getReservation() == null ||
				this.getReservationDetailNo() == null ||
				this.getReservationDetailNo() < 1)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	
	/**
	 * 予約日時を更新する。
	 * @param con 
	 * @throws java.sql.SQLException 
	 * @return 
	 */
	public boolean updateReservationDatetime(ConnectionWrapper con) throws SQLException
	{
		if(con.executeUpdate(this.getUpdateReservationDatetimeSQL()) == 1)
		{
			return	true;
		}
		else
		{
			return	false;
		}
	}
	
	/**
	 * Select文を取得する。
	 * @return 
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from data_reservation_detail\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n" +
				"and reservation_detail_no = " + SQLUtil.convertForSQL(this.getReservationDetailNo()) + "\n" +
				"and delete_date is null\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return 
	 */
	private String	getInsertSQL()
	{
		return	"insert into data_reservation_detail\n" +
				"(shop_id, reservation_no, reservation_detail_no, reservation_datetime,\n" +
				"technic_id, bed_id, staff_id, designated_flag,\n" +
				"insert_date, update_date, delete_date, operation_time, course_flg, contract_no, contract_detail_no,contract_shop_id)\n" +
				"select\n" +
				SQLUtil.convertForSQL(this.getShop().getShopID()) + ",\n" +
				SQLUtil.convertForSQL(this.getReservation().getReservationNo()) + ",\n" +
				"coalesce(max(reservation_detail_no), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getReservationDatetime()) + ",\n" +
				SQLUtil.convertForSQL(getProductId()) + ",\n" +
				SQLUtil.convertForSQL(this.getBed().getBedID()) + ",\n" +
				SQLUtil.convertForSQL(this.getStaff().getStaffID()) + ",\n" +
				SQLUtil.convertForSQL(this.getDesignated()) + ",\n" +
				"current_timestamp, current_timestamp, null,\n" +
				SQLUtil.convertForSQL(getOperationTime()) + ",\n" +
                                getCourseFlgStr() + ",\n" +
                                getContractNoStr() + ",\n" +
                                getContractDetailNoStr() + ",\n" +
                                // start add 20130806 nakhoa
                                SQLUtil.convertForSQL(this.getContractShopId()) + "\n" +
                                // end add 20130806 nakhoa
				"from data_reservation_detail\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return 
	 */
	private String	getUpdateSQL()
	{
		return	"update data_reservation_detail\n" +
				"set\n" +
				"reservation_datetime = " + SQLUtil.convertForSQL(this.getReservationDatetime()) + ",\n" +
				"technic_id = " + SQLUtil.convertForSQL(getProductId()) + ",\n" +
				"bed_id = " + SQLUtil.convertForSQL(this.getBed().getBedID()) + ",\n" +
				"staff_id = " + SQLUtil.convertForSQL(this.getStaff().getStaffID()) + ",\n" +
				"designated_flag = " + SQLUtil.convertForSQL(this.getDesignated()) + ",\n" +
                                "course_flg = " + getCourseFlgStr() + ",\n" +
                                "contract_no = " + getContractNoStr() + ",\n" +
                                "contract_detail_no = " + getContractDetailNoStr() + ",\n" +
                                // start add 20130806 nakhoa
                                "contract_shop_id = " + this.getContractShopId() + ",\n" +
                                // end add 20130806 nakhoa
				"update_date = current_timestamp\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n" +
				"and reservation_detail_no = " + SQLUtil.convertForSQL(this.getReservationDetailNo()) + "\n";
	}
	
	/**
	 * 削除用Update文を取得する。
	 * @return 
	 */
	private String	getDeleteSQL()
	{
		return	"update data_reservation_detail\n" +
				"set\n" +
				"update_date = current_timestamp,\n" +
				"delete_date = current_timestamp\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n" +
				"and reservation_detail_no = " + SQLUtil.convertForSQL(this.getReservationDetailNo()) + "\n";
	}
	
	/**
	 * 予約日時を更新するＳＱＬ文を取得する。
	 * @return 予約日時を更新するＳＱＬ文
	 */
	private String	getUpdateReservationDatetimeSQL()
	{
		return	"update data_reservation_detail\n" +
				"set\n" +
				"reservation_datetime = " + SQLUtil.convertForSQL(this.getReservationDatetime()) + ",\n" +
				"update_date = current_timestamp\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n" +
				"and reservation_detail_no = " + SQLUtil.convertForSQL(this.getReservationDetailNo()) + "\n";
	}

	public void setReservationDate(GregorianCalendar selectedDate) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	/**
	 * 予約番号の最大値（登録した予約番号）を取得する。
	 * @param con ConnectionWrapper
	 * @return 予約番号の最大値（登録した予約番号）
	 * @throws java.sql.SQLException SQLException
	 */
	private Integer	getMaxReservationDetailNo(ConnectionWrapper con) throws SQLException
	{
		Integer		maxNo	=	null;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getMaxReservationDetailNoSQL());
		
		if(rs.next())
		{
			maxNo	=	rs.getInt("max_no");
		}
		
		return	maxNo;
	}
	
	/**
	 * 予約番号の最大値（登録した予約番号）を取得するＳＱＬ文を取得する。
	 * @return 予約番号の最大値（登録した予約番号）を取得するＳＱＬ文
	 */
	private String getMaxReservationDetailNoSQL()
	{
		return
			"select\n" +
			"max( drd.reservation_detail_no ) as max_no\n" +
			"from\n" +
			"data_reservation_detail as drd\n" +
			"where\n" +
			"drd.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
			"and drd.reservation_no = " + SQLUtil.convertForSQL( this.getReservationNo() ) + "\n" +
			";\n";
	}

        public Integer getProductId()
        {
            if (getCourseFlg() == null) {
                //技術の場合
                return getTechnic().getTechnicID();
            } else if (getCourseFlg() == 1) {
                //コース契約の場合
                return getCourse().getCourseId();
            } else if (getCourseFlg() == 2) {
                //消化コースの場合
                return getConsumptionCourse().getCourseId();
            }
            
            return null;
        }

        private Integer getOperationTime()
        {

            if (getCourseFlg() == null) {
                //技術の場合
                return getTechnic().getOperationTime();
            } else if (getCourseFlg() == 1) {
                //コース契約の場合
                return getCourse().getOperationTime();
            } else if (getCourseFlg() == 2) {
                //消化コースの場合
                return getConsumptionCourse().getOperationTime();
            }

            return null;
        }

        private String getCourseFlgStr()
        {
            if (getCourseFlg() == null) {
                //技術の場合
                return "null";
            } else if (getCourseFlg() == 1) {
                //コース契約の場合
                return courseFlg.toString();
            } else if (getCourseFlg() == 2) {
                //消化コースの場合
                return courseFlg.toString();
            }

            return null;

        }

        private String getContractNoStr()
        {
            if (getCourseFlg() == null) {
                //技術の場合
                return "null";
            } else if (getCourseFlg() == 1) {
                //コース契約の場合
                return "null";
            } else if (getCourseFlg() == 2) {
                //消化コースの場合
                return consumptionCourse.getContractNo().toString();
            }

            return null;

        }

        private String getContractDetailNoStr()
        {
            if (getCourseFlg() == null) {
                //技術の場合
                return "null";
            } else if (getCourseFlg() == 1) {
                //コース契約の場合
                return "null";
            } else if (getCourseFlg() == 2) {
                //消化コースの場合
                return consumptionCourse.getContractDetailNo().toString();
            }

            return null;

        }
}
