/*
 * DataReservation.java
 *
 * Created on 2006/06/16, 18:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.data.reservation;

import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.hair.master.company.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 予約データ
 * @author katagiri
 */
public class DataReservation extends ArrayList<DataReservationDetail> implements Cloneable
{
	/**
	 * 店舗
	 */
	protected	MstShop         shop            = new MstShop();
	/**
	 * 予約No.
	 */
	protected	Integer         reservationNo   = null;
	/**
	 * 顧客
	 */
	protected	MstCustomer     customer        = new MstCustomer();
	/**
	 * 指名
	 */
	protected	boolean        designated      = false;
	/**
	 * 主担当スタッフ
	 */
	protected	MstStaff        staff           = null;
	/**
	 * 合計時間
	 */
	protected	Integer         totalTime       = 0;
	/**
	 * 来店時間
	 */
	protected	GregorianCalendar   visitTime   = null;
	/**
	 * 開始時間
	 */
	protected	GregorianCalendar   startTime   = null;
	/**
	 * 退店時間
	 */
	protected	GregorianCalendar   leaveTime   = null;
	/**
	 * 状態フラグ
	 */
	protected	Integer             status      = 0;
	/**
	 * 副状態フラグ
	 */
	protected	Integer             subStatus   = 0;
	/**
	 * 伝票No.
	 */
	protected	Integer             slipNo      = 0;
        /**
	 *モバイル予約フラグ
         *1:未表示　2:表示済
	 */        
        protected       int     mobileFlag      = 0;
        /**
	 *来店時次回予約フラグ
	 */        
        private       int     nextFlag    = 0;
        /**
	 *電話・事前予約フラグ
	 */        
        private       int     preorderFlag    = 0;
         /**
	 *予約日時
	 */        
         protected      java.util.Date  reserveDate = null;  
         /**
	 *予約コメント
	 */ 
	 protected	String comment = "";

         /**
	 *技術分類略称リスト
	 */ 
	 protected	String technicClassContractedNameList = "";

	/**
	 * 登録者
	 */
	protected	MstStaff        regStaff           = null;

	/**
	 * 登録日時
	 */
	protected	GregorianCalendar   insertDate   = null;

        /**
	 * 更新日時
	 */
	protected	GregorianCalendar   updateDate   = null;
        
        protected MstResponse Response1 = null;
        protected MstResponse Response2 = null;
        protected int cancelFlag;
        //IVS_LVTu start add 2015/09/17 Bug #42681
        protected java.util.Date lastVisitDate = null;
        protected java.util.Date nextReservationDate = null;
        protected String nextReservationDateStrArray = null;
        //施術時間の変更 start
        /** 施術時間（分） */
        protected String opeMinute = null;
        /** 施術時間（秒） */
        protected String opeSecond = null;
        
        /**
         * 施術時間（分）を返す
         * @return 施術時間（分）
         */
        public String getOpeMinute() {
            return opeMinute;
        }

        /**
         * 施術時間（分）を設定
         * @param opeMinute 施術時間（分）
         */
        public void setOpeMinute(String opeMinute) {
            this.opeMinute = opeMinute;
        }
        
        /**
         * 施術時間（秒）を返す
         * @return 施術時間（秒）
         */
        public String getOpeSecond() {
            return opeSecond;
        }

        /**
         * 施術時間（秒）を設定
         * @param opeMinute 施術時間（秒）
         */
        public void setOpeSecond(String opeSecond) {
            this.opeSecond = opeSecond;
        }
        //施術時間の変更 end
        
        public StringBuffer getNextReservationDateStrArray() {
            String str[] ;
            StringBuffer strBuffer = new StringBuffer();
            strBuffer.append("");
            if(this.nextReservationDateStrArray != null) {
                str = this.nextReservationDateStrArray.split(",");
                
                for(int i = 0;i < str.length; i ++) {
                    if(!str[i].trim().equals("")) {
                        SimpleDateFormat	sdf	=	new SimpleDateFormat("yyyy-MM-dd");
                        java.util.Date ReservationDate;
                        try {
                            ReservationDate = sdf.parse(str[i]);
                            String strNextDate = DateUtil.format(ReservationDate, "yyyy年M月d日 (E)");
                            strBuffer.append(strNextDate);
                        } catch (ParseException ex) {
                            Logger.getLogger(DataReservation.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if(((i+1)< str.length) && (!str[i+1].trim().equals(""))) {
                        strBuffer.append("<br>");
                    }
                }
            }
            
            return strBuffer;
        }

        public void setNextReservationDateStrArray(String nextReservationDateStrArray) {
            this.nextReservationDateStrArray = nextReservationDateStrArray;
        }
        
        public java.util.Date getLastVisitDate() {
            return lastVisitDate;
        }

        public void setLastVisitDate(java.util.Date lastVisitDate) {
            this.lastVisitDate = lastVisitDate;
        }

        public java.util.Date getNextReservationDate() {
            return nextReservationDate;
        }

        public void setNextReservationDate(java.util.Date nextReservationDate) {
            this.nextReservationDate = nextReservationDate;
        }
        //IVS_LVTu end add 2015/09/17 Bug #42681
        public MstResponse getResponse1() {
            return Response1;
        }

        public void setResponse1(MstResponse Response1) {
            this.Response1 = Response1;
        }

        public MstResponse getResponse2() {
            return Response2;
        }

        public void setResponse2(MstResponse Response2) {
            this.Response2 = Response2;
        }
	
         /**
	 *予約詳細データのソート用クラス
	 */ 
	public class DataReservationDetailComparator implements Comparator {
            
            public int compare(Object object1, Object object2) {
                //予約日時の昇順に並び替え
                DataReservationDetail drd1 = (DataReservationDetail) object1;
                DataReservationDetail drd2 = (DataReservationDetail) object2;
                GregorianCalendar date1 = drd1.getReservationDatetime();
                GregorianCalendar date2 = drd2.getReservationDatetime();
                return (date1.compareTo(date2));
            }
	}
	
        //Start add 20131002 lvut
       
        @Override
        public Object clone()  {
             return super.clone();
        }
        //End add 20131002 lvut
	/** Creates a new instance of DataReservation */
	public DataReservation()
	{
	}
        
        /**
	 *モバイル予約フラグを取得する。
         *@return フラグ
         */        
        public int getMobileFlag(){
            return mobileFlag ;
        }
        public void setMobileFlag(int mobileFlag){
            this.mobileFlag = mobileFlag ;
        }

        /**
	 *来店時次回予約フラグを取得する。
         *@return フラグ
         */        
        public int getNextFlag() {
            return nextFlag;
        }

        public void setNextFlag(int nextFlag) {
            this.nextFlag = nextFlag;
        }
        
        /**
	 *電話・事前予約フラグを取得する。
         *@return フラグ
         */        
        public int getPreorderFlag() {
            return preorderFlag;
        }

        public void setPreorderFlag(int preorderFlag) {
            this.preorderFlag = preorderFlag;
        }
       
        /**
	 *予約日付を取得する。
         *@return 予約日付
         */        
        public java.util.Date getReserveDate(){
            return reserveDate ;
        }
        public void setReserveDate(java.util.Date reserveDate){
            this.reserveDate = reserveDate ;
        }
        
        // IVS SANG START 20131103
         protected      java.util.Date  deleteDate = null; 
         public java.util.Date getDeleteDate(){
            return deleteDate ;
        }
        public void setDeleteDate(java.util.Date deleteDate){
            this.deleteDate = deleteDate ;
        }
        // IVS SANG START 20131103
        
	/**
	 * 店舗を取得する。
	 * @return 店舗
	 */
	public MstShop getShop()
	{
            return shop;
	}

	/**
	 * 店舗をセットする。
	 * @param shop 店舗
	 */
	public void setShop(MstShop shop)
	{
            this.shop = shop;
	}
	
	/**
	 * 予約No.を取得する。
	 * @return 予約No.
	 */
	public Integer getReservationNo()
	{
		return reservationNo;
	}

	/**
	 * 予約No.をセットする。
	 * @param reservationNo 予約No.
	 */
	public void setReservationNo(Integer reservationNo)
	{
		this.reservationNo = reservationNo;
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
	 * 顧客をセットする。
	 * @param customer 顧客
	 */
	public void setCustomer(MstCustomer customer)
	{
		this.customer = customer;
	}
	
	/**
	 * 指名を取得する。
	 * @return 指名
	 */
	public boolean getDesignated()
	{
		return designated;
	}

	/**
	 * 指名をセットする。
	 * @param designated 指名
	 */
	public void setDesignated( boolean designated )
	{
		this.designated = designated;
	}
	
	/**
	 * 主担当スタッフを取得する。
	 * @return 主担当スタッフ
	 */
	public MstStaff getStaff()
	{
            return staff;
	}

	/**
	 * 主担当スタッフをセットする。
	 * @param MstStaff 主担当スタッフ
	 */
	public void setStaff( MstStaff staff )
	{
            this.staff = staff;
	}
	
	/**
	 * 登録者を取得する。
	 * @return 登録者
	 */
	public MstStaff getRegStaff()
	{
            return regStaff;
	}

	/**
	 * 登録者をセットする。
	 * @param MstStaff 登録者
	 */
	public void setRegStaff( MstStaff regStaff )
	{
            this.regStaff = regStaff;
	}

	/**
	 * 登録日時を取得する
	 * @return 登録日時
	 */
	public GregorianCalendar getInsertDate()
	{
            return insertDate;
	}
	/**
	 * 登録日時をセットする
	 * @param 登録日時
	 */
	public void setInsertDate(GregorianCalendar insertDate)
	{
            this.insertDate = insertDate;
	}
	public void setInsertDate(java.util.Date insertDate)
	{
            if (this.insertDate == null) {
                this.insertDate = new GregorianCalendar();
            }

            this.insertDate.setTime(insertDate);
	}

	/**
	 * 更新日時を取得する
	 * @return 更新日時
	 */
	public GregorianCalendar getUpdateDate()
	{
            return updateDate;
	}
	/**
	 * 更新日時をセットする
	 * @param 更新日時
	 */
	public void setUpdateDate(GregorianCalendar updateDate)
	{
            this.updateDate = updateDate;
	}
	public void setUpdateDate(java.util.Date updateDate)
	{
            if (this.updateDate == null) {
                this.updateDate = new GregorianCalendar();
            }

            this.updateDate.setTime(updateDate);
	}

	/**
	 * 合計時間を取得する。
	 * @return 合計時間
	 */
	public Integer getTotalTime()
	{
            totalTime = 0;

            if (this.size() > 0) {
                
                Calendar calMin = Calendar.getInstance();
                calMin.setTime(this.get(0).getReservationDatetime().getTime());
                Calendar calMax = (Calendar)calMin.clone();
                
                for (DataReservationDetail drd : this) {

                    Calendar calTmp = Calendar.getInstance();
                    calTmp.setTime(drd.getReservationDatetime().getTime());
                    
                    if (calMin.after(calTmp)) {
                        calMin.setTime(calTmp.getTime());
                    }

                    if (drd.getCourse() != null) {
                        calTmp.add(Calendar.MINUTE, drd.getCourse().getOperationTime());
                    } else if (drd.getConsumptionCourse() != null) {
                        calTmp.add(Calendar.MINUTE, drd.getConsumptionCourse().getOperationTime());
                    } else {
                        calTmp.add(Calendar.MINUTE, drd.getTechnic().getOperationTime());
                    }

                    if (calMax.before(calTmp)) {
                        calMax.setTime(calTmp.getTime());
                    }
                        
                }
                
                totalTime = Long.valueOf((calMax.getTimeInMillis() - calMin.getTimeInMillis()) / 1000 / 60).intValue();
            }
            
/*            
            for (DataReservationDetail drd : this) {
                totalTime += drd.getTechnic().getOperationTime();
            }
*/            
            return totalTime;
	}

	/**
	 * 合計時間をセットする。
	 * @param totalTime 合計時間
	 */
	public void setTotalTime(Integer totalTime)
	{
            this.totalTime = totalTime;
	}
	
	/**
	 * 来店時間を取得する
	 * @return 来店時間
	 */
	public GregorianCalendar getVisitTime()
	{
            return visitTime;
	}
	
	/**
	 * 来店時間をセットする
	 * @param startTime 来店時間
	 */
	public void setVisitTime(GregorianCalendar visitTime)
	{
            this.visitTime = visitTime;
	}
        
	public void setVisitTime(java.util.Date visitTime)
	{
            if (this.visitTime == null) {
                this.visitTime = new GregorianCalendar();
            }

            this.visitTime.setTime(visitTime);
	}
	
	/**
	 * 施術開始時間を取得する
	 * @return 施術開始時間
	 */
	public GregorianCalendar getStartTime()
	{
            return startTime;
	}
	
	/**
	 * 施術開始時間をセットする
	 * @param startTime 施術開始時間
	 */
	public void setStartTime(GregorianCalendar startTime)
	{
            this.startTime = startTime;
	}
        
	public void setStartTime(java.util.Date startTime)
	{
            if (this.startTime == null) {
                this.startTime = new GregorianCalendar();
            }

            this.startTime.setTime(startTime);
	}
	
	/**
	 * 退店時間を取得する
	 * @return 退店時間
	 */
	public GregorianCalendar getLeaveTime()
	{
            return leaveTime;
	}
	
	/**
	 * 退店時間をセットする
	 * @param startTime 退店時間
	 */
	public void setLeaveTime(GregorianCalendar leaveTime)
	{
            this.leaveTime = leaveTime;
	}
        
	public void setLeaveTime(java.util.Date leaveTime)
	{
            if (this.leaveTime == null) {
                this.leaveTime = new GregorianCalendar();
            }

            this.leaveTime.setTime(leaveTime);
	}
	
	/**
	 * 状態フラグを取得する。
	 * @return 状態フラグ
	 */
	public Integer getStatus()
	{
		return status;
	}

	/**
	 * 状態フラグをセットする。
	 * @param status 状態フラグ
	 */
	public void setStatus(Integer status)
	{
		this.status = status;
	}

	/**
	 * 副状態フラグを取得する。
	 * @return 副状態フラグ
	 */
	public Integer getSubStatus()
	{
		return subStatus;
	}

	/**
	 * 副状態フラグをセットする。
	 * @param subStatus 副状態フラグ
	 */
	public void setSubStatus(Integer subStatus)
	{
		this.subStatus = subStatus;
	}

	/**
	 * 伝票No.を取得する。
	 * @return 伝票No.
	 */
	public Integer getSlipNo()
	{
		return slipNo;
	}

	/**
	 * 伝票No.をセットする。
	 * @param slipNo 伝票No.
	 */
	public void setSlipNo(Integer slipNo)
	{
		this.slipNo = slipNo;
	}
	
	/**
	 * 予約コメントを取得する。
	 * @return 予約コメント
	 */
	public String getComment()
	{
            if (comment == null) return "";

            return comment;
	}

	/**
	 * 予約コメントをセットする。
	 * @param comment 予約コメント
	 */
	public void setComment(String comment)
	{
            this.comment = comment;
	}
	
        // IVS START INSERT 20131102 [gbソース]ソースマージ
        public int getCancelFlag() {
            return cancelFlag;
        }

        public void setCancelFlag(int cancelFlag) {
            this.cancelFlag = cancelFlag;
        }
        // IVS END INSERT 20131102 [gbソース]ソースマージ
	public String getTechnicClassContractedName()
	{
		String name = "";
		
                //予約日時の昇順に並び替え
		Collections.sort(this, new DataReservationDetailComparator());

		for (DataReservationDetail drd : this) {
                    if (0 < name.length()) name += "、";

                    if (drd.getCourseFlg() == null) {
                        //技術
                        if (drd.getTechnic().getTechnicNo() != null && (drd.getTechnic().getTechnicNo().startsWith("mo-rsv-")||drd.getTechnic().getMobileFlag()==1)) {
                            name += drd.getTechnic().getTechnicName();
                        } else {
                            name += drd.getTechnic().getTechnicClass().getTechnicClassContractedName();
                        }
                    } else if ((Integer)drd.getCourseFlg() == 1) {
                        //コース契約
                        name += drd.getCourse().getCourseClass().getCourseClassName();
                    } else if ((Integer)drd.getCourseFlg() == 2) {
                        //消化コース
                        name += drd.getConsumptionCourse().getConsumptionCourseClass().getCourseClassName();
                    }
		}

		return name;
	}

	public String getTechnicName()
	{
		String name = "";
		
                //予約日時の昇順に並び替え
		Collections.sort(this, new DataReservationDetailComparator());

		for (DataReservationDetail drd : this) {
                    if (0 < name.length()) name += "、";
                    name += drd.getTechnic().getTechnicName();
		}

		return name;
	}
        
	/**
	 * 予約詳細データの担当者名をすべて取得する。
	 * @return 担当者名
	 */
	public String getDRDStaffName()
	{
                final String NO_DATA = "[指名なし]";

		String name = "";

                //予約日時の昇順に並び替え
		Collections.sort(this, new DataReservationDetailComparator());

                ArrayList<String> list = new ArrayList<String>();

		for(DataReservationDetail drd : this)
		{
                    if(drd.getStaff().getStaffName(0) == null || drd.getStaff().getStaffName(0).length() == 0){
                        if (list.contains(NO_DATA)) continue;
                        if(0 < name.length()) name += "、";
                        list.add(NO_DATA);
                        name += NO_DATA;
                    } else {
                        if (list.contains(drd.getStaff().getStaffName(0))) continue;
                        if(0 < name.length()) name += "、";
                        list.add(drd.getStaff().getStaffName(0));
                        name += drd.getStaff().getStaffName(0);
                    }
		}

		return name;
	}
	/**
	 * 予約詳細データの担当者名をすべて取得する。
	 * @return 担当者名
	 */
	public String getDRDFullStaffName()
	{
                final String NO_DATA = "[指名なし]";
                
		String name = "";
		
                //予約日時の昇順に並び替え
		Collections.sort(this, new DataReservationDetailComparator());

                ArrayList<String> list = new ArrayList<String>();
                
		for(DataReservationDetail drd : this)
		{
                    if(drd.getStaff().getFullStaffName() == null || drd.getStaff().getFullStaffName().length() == 0){
                        if (list.contains(NO_DATA)) continue;
                        if(0 < name.length()) name += "、";
                        list.add(NO_DATA);
                        name += NO_DATA;
                    } else {
                        if (list.contains(drd.getStaff().getFullStaffName())) continue;
                        if(0 < name.length()) name += "、";
                        list.add(drd.getStaff().getFullStaffName());
                        name += drd.getStaff().getFullStaffName();
                    }
		}
		
		return name;
	}
	
	/**
	 * 予約詳細データの施術台名をすべて取得する。
	 * @return 施術台名
	 */
	public String getDRDBedName()
	{
                final String NO_DATA = "[指定なし]";
                
		String name = "";
		
                //予約日時の昇順に並び替え
		Collections.sort(this, new DataReservationDetailComparator());

                ArrayList<String> list = new ArrayList<String>();
                
                for(DataReservationDetail drd : this)
		{
                    if(drd.getBed().getBedName() == null || drd.getBed().getBedName().length() == 0){
                        if (list.contains(NO_DATA)) continue;
                        if(0 < name.length()) name += "、";
                        list.add(NO_DATA);
                        name += NO_DATA;
                    } else {
                        if (list.contains(drd.getBed().getBedName())) continue;
                        if(0 < name.length()) name += "、";
                        list.add(drd.getBed().getBedName());
                        name += drd.getBed().getBedName();
                    }
		}
		
		return name;
	}
	
	/**
	 * 予約詳細データの技術名をすべて取得する。
	 * @return 技術名
	 */
	public String getDRDTechnicName()
	{
		String	name	=	"";
		
                //予約日時の昇順に並び替え
		Collections.sort(this, new DataReservationDetailComparator());

		for(DataReservationDetail drd : this)
		{
			if(0 < name.length())	name	+=	"、";
			name	+=	drd.getTechnic().getTechnicName() == null ? " " : drd.getTechnic().getTechnicName();
		}
		
		return	name;
	}
	
	/**
	 * 予約詳細データの予約日時(開始)を取得する。
	 * @return 予約日時(開始)
	 */
	public GregorianCalendar getDRDStartReservationDatetime()
	{
		GregorianCalendar	date	=	null;
		
                //予約日時の昇順に並び替え
		Collections.sort(this, new DataReservationDetailComparator());

		for(DataReservationDetail drd : this)
		{
			date	=	drd.getReservationDatetime();
                        break;
		}
		
		return	date;
	}
	
	/**
	 * 予約データをセットする。
	 * @param dr 予約データ
	 */
	public void set(DataReservation dr)
	{
            this.setShop(dr.getShop());
            this.setReservationNo(dr.getReservationNo());
            this.setCustomer(dr.getCustomer());
            this.setDesignated(dr.getDesignated());
            this.setStaff(dr.getStaff());
            this.setTotalTime(dr.getTotalTime());
            this.setVisitTime(dr.getVisitTime());
            this.setStartTime(dr.getStartTime());
            this.setLeaveTime(dr.getLeaveTime());
            this.setStatus(dr.getStatus());
            this.setSubStatus(dr.getSubStatus());
            this.setSlipNo(dr.getSlipNo());
            this.setComment(dr.getComment());
            this.setNextFlag(dr.getNextFlag());
            this.setPreorderFlag(dr.getPreorderFlag());
            this.setRegStaff(dr.getRegStaff());
            this.setInsertDate(dr.getInsertDate());
            this.setUpdateDate(dr.getUpdateDate());
	}
	
	public void initTechnics()
	{
		this.clear();
		this.setTotalTime( 0 );
	}
	
	/**
	 * データを登録する。
	 * @param con 
	 * @param isStay 顧客状況 1:予約 2;在店4:退店
	 * @throws java.sql.SQLException 
	 * @return 
	 */
//	public boolean regist(ConnectionWrapper con, boolean isStay) throws SQLException
	public boolean regist(ConnectionWrapper con ) throws SQLException
	{
		String	sql		=	"";
		
		if(isExists(con))
		{
//			sql	=	this.getUpdateSQL(isStay);
			sql	=	this.getUpdateSQL();
		}
		else
		{
//			sql	=	this.getInsertSQL(isStay);
			sql	=	this.getInsertSQL();
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
		
		if(con.executeUpdate(sql) != 1)
		{
			return	false;
		}
		
		if( !this.deleteDetail(con) ) return false;
		if( !this.deleteProportionally(con) ) return false;
                // IVS_Thanh start add 2014/07/07 GB_Mashu_業態主担当設定
                if( !this.deleteDataReservationMainStaffLogic(con) ) return false;
                // IVS_Thanh end add 2014/07/07 GB_Mashu_業態主担当設定
		return	true;
	}
	
	/**
	 * データが存在するかチェックする。
	 * @param con 
	 * @throws java.sql.SQLException 
	 * @return 
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
            if (this.getReservationNo() == null || this.getReservationNo() < 1) return false;
		
            if (con == null) return false;
		
            ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());
            //IVS NNTUAN START EDIT 20131028
            //return rs.next();
            //if slip no has value . get value of slip_no
            if (rs.next()) {
                int slipNo = rs.getInt("slip_no");
                if (slipNo != 0) {
                    this.setSlipNo(slipNo);
                }
                return true;
            }
            return false;
            //IVS NNTUAN END EDIT 20131028
	}
	
	
	/**
	 * 状態フラグを更新する。
	 * @param con 
	 * @throws java.sql.SQLException 
	 * @return 
	 */
	public boolean updateStatus(ConnectionWrapper con) throws SQLException
	{
		if(con.executeUpdate(this.getUpdateStatusSQL()) == 1)
		{
			return	true;
		}
		else
		{
			return	false;
		}
	}
	
	
	/**
	 * 明細データを削除する。
	 * @param con 
	 * @throws java.sql.SQLException 
	 * @return 
	 */
	public boolean deleteDetail(ConnectionWrapper con) throws SQLException
	{
		if(0 <= con.executeUpdate(this.getDeleteDetailSQL()))
		{
			return	true;
		}
		else
		{
			return	false;
		}
	}
	
	/**
	 * 按分データを削除する
	 * @param con 
	 * @throws java.sql.SQLException 
	 * @return 
	 */
	public boolean deleteProportionally( ConnectionWrapper con ) throws SQLException
	{
		if( 0 <= con.executeUpdate( this.getDeleteProportionallySQL() ) )
		{
			return true;
		}
		return false;
	}
        
        // IVS_Thanh start add 2014/07/10 Mashu_お会計表示
        public boolean deleteDataReservationMainStaff(ConnectionWrapper con) throws SQLException {
            return 0 <= con.executeUpdate(this.getDeleteDataReservationMainStaffSQL());
        }
        
        public String getDeleteDataReservationMainStaffSQL() {
            return "delete from data_reservation_mainstaff\n"
                    + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                    + "and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n";
        }   
        
         public boolean deleteDataReservationMainStaffLogic(ConnectionWrapper con) throws SQLException {
            return 0 <= con.executeUpdate(this.getDeleteDataReservationMainStaffSQLLogic());
        }
        
        public String getDeleteDataReservationMainStaffSQLLogic() {
            return
			"update data_reservation_mainstaff\n" +
			"set\n" +
			"update_date = current_timestamp,\n" +
			"delete_date = current_timestamp\n" +
			"where\n" +
			"shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
			"and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n";
        }
        // IVS_Thanh end add 2014/07/10 Mashu_お会計表示
	
	/**
	 * Select文を取得する。
	 * @return 
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from data_reservation\n" +
				"where	shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and	reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return 
	 */
//	private String	getInsertSQL(boolean isStay)
	private String	getInsertSQL()
	{
		return
			"insert into data_reservation\n" +
			"(shop_id, reservation_no, customer_id,\n" +
			"designated_flag, staff_id, total_time, visit_time,\n" +
			"start_time, leave_time, status, sub_status, slip_no,comment,next_flag,preorder_flag,reg_staff_id,response_id1,response_id2,\n" +
			"insert_date, update_date, delete_date)\n" +
			"select\n" +
			SQLUtil.convertForSQL(this.getShop().getShopID()) + ",\n" +
			"coalesce(max(reservation_no), 0) + 1,\n" +
			SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + ",\n" +
			SQLUtil.convertForSQL(this.getDesignated() ) + ",\n" +
			( this.getStaff() != null && this.getStaff().getStaffID() != null ? SQLUtil.convertForSQL(this.getStaff().getStaffID() ) : null ) + ",\n" +
			SQLUtil.convertForSQL(this.getTotalTime()) + ",\n" +
			// visit_time  1:null  2-1:現時刻  2-2:保持値  3:保持値
			( this.getStatus() == 1 ? ( "null" ) : ( this.getStatus() == 2 ? ( this.getSubStatus() == 1 ? ( "current_timestamp" ) : ( SQLUtil.convertForSQL( this.getVisitTime() ) ) ) : ( SQLUtil.convertForSQL( this.getVisitTime() ) ) ) ) + ",\n" +
			// start_time  1:null  2-1:null    2-2:現時刻  3:保持値(無ければ在店時刻)
			( this.getStatus() == 1 ? ( "null" ) : ( this.getStatus() == 2 ? ( this.getSubStatus() == 1 ? ( "null" ) : ( "current_timestamp" ) ) : ( this.getStartTime() == null ? ( SQLUtil.convertForSQL( this.getVisitTime() ) ) : SQLUtil.convertForSQL( this.getStartTime() ) ) ) ) + ",\n" +
			// leave_time  1:null  2-1:null    2-2:null    3:現時刻
			//( this.getStatus() <= 2 ? ( "null" ) : ( "current_timestamp" ) ) + ",\n" +
			( this.getStatus() <= 2 ? ( "null" ) : SQLUtil.convertForSQL( this.getLeaveTime() ) ) + ",\n" +
			SQLUtil.convertForSQL(this.getStatus()) + ",\n" +
			SQLUtil.convertForSQL(this.getSubStatus()) + ",\n" +
			SQLUtil.convertForSQL(this.getSlipNo()) + ",\n" +
			SQLUtil.convertForSQL(this.getComment()) + ",\n" +
			SQLUtil.convertForSQL(this.getNextFlag()) + ",\n" +
			SQLUtil.convertForSQL(this.getPreorderFlag()) + ",\n" +
			( this.getRegStaff() != null && this.getRegStaff().getStaffID() != null ? SQLUtil.convertForSQL(this.getRegStaff().getStaffID() ) : null ) + ",\n" 
			+ (this.getResponse1() != null && this.getResponse1().getResponseID() != null ? SQLUtil.convertForSQL(this.getResponse1().getResponseID()) : null) + ",\n"
                        + (this.getResponse2() != null && this.getResponse2().getResponseID() != null ? SQLUtil.convertForSQL(this.getResponse2().getResponseID()) : null) + ",\n"
			+"current_timestamp, current_timestamp, null\n" +
			"from data_reservation\n" +
			"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n";
	}
	
	/**
	 * Update文を取得する。
	 * @return 
	 */
//	private String	getUpdateSQL(boolean isStay)
	private String	getUpdateSQL()
	{      
		return
                        
			"update data_reservation\n" +
			"set\n" +
			"customer_id = " + SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + ",\n" +
			"designated_flag = " + SQLUtil.convertForSQL(this.getDesignated() ) + ",\n" +
			"staff_id = " + ( this.getStaff() != null && this.getStaff().getStaffID() != null ? SQLUtil.convertForSQL(this.getStaff().getStaffID() ) : null ) + ",\n" +
			// IVS SANG START EDIT 20131115 [gb]他店舗のお会計をすると自身の店舗IDが変わってしまう
                        // (this.getStatus() < 3 || SystemInfo.getCurrentShop().isReservationMenuUpdate() ? "total_time = " + SQLUtil.convertForSQL(this.getTotalTime()) + ",\n" : "") +
                        (this.getStatus() < 3 || this.getShop().isReservationMenuUpdate() ? "total_time = " + SQLUtil.convertForSQL(this.getTotalTime()) + ",\n" : "") +
			// IVS SANG END EDIT 20131115 [gb]他店舗のお会計をすると自身の店舗IDが変わってしまう
                        // visit_time  1:null  2-1:現時刻  2-2:保持値  3:保持値
			( this.getStatus() == 1 ? "visit_time = null,\n" : ( this.getStatus() == 2 ? ( this.getSubStatus() == 1 ? "visit_time = (case when visit_time is null then current_timestamp else visit_time end),\n" : "" ) : "" ) ) +
			// start_time  1:null  2-1:null    2-2:現時刻  3:保持値(無ければ在店時刻)
			//( this.getStatus() == 1 ? "start_time = null,\n" : ( this.getStatus() == 2 ? ( this.getSubStatus() == 1 ? "start_time = null,\n" : "start_time = current_timestamp,\n" ) : "start_time = (select coalesce( start_time, visit_time ) from data_reservation where shop_id = 1 and reservation_no = 11),\n" ) ) +
			( this.getStatus() == 1 ? "start_time = null,\n" : ( this.getStatus() == 2 ? ( this.getSubStatus() == 1 ? "start_time = start_time,\n" : "start_time = current_timestamp,\n" ) : "start_time = (select coalesce( start_time, visit_time ) from data_reservation where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + " and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "),\n" ) ) +
			// leave_time  1:null  2-1:null    2-2:null    3:現時刻
			"leave_time = " + ( this.getStatus() <= 2 ? ( "null" ) : ( "coalesce(leave_time, current_timestamp)" ) ) + ",\n" +
			"status = " + SQLUtil.convertForSQL(this.getStatus()) + ",\n" +
			"sub_status = " + SQLUtil.convertForSQL(this.getSubStatus()) + ",\n" +
			"slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + ",\n" +
			"comment = " + SQLUtil.convertForSQL(this.getComment()) + ",\n" +
			"next_flag = " + SQLUtil.convertForSQL(this.getNextFlag()) + ",\n" +
			"preorder_flag = " + SQLUtil.convertForSQL(this.getPreorderFlag()) + ",\n" +
			"reg_staff_id = " + ( this.getRegStaff() != null && this.getRegStaff().getStaffID() != null ? SQLUtil.convertForSQL(this.getRegStaff().getStaffID() ) : null ) + ",\n" 
			 //+ "response_id1 = " + SQLUtil.convertForSQL(this.getResponse1().getResponseID()) + ",\n"
                        + "response_id1 = " + (this.getResponse1() == null ? null : SQLUtil.convertForSQL(this.getResponse1().getResponseID())) + ",\n"
                        //+ "response_id2 = " + SQLUtil.convertForSQL(this.getResponse2().getResponseID()) + ",\n"
                        + "response_id2 = " + (this.getResponse2() == null ? null : SQLUtil.convertForSQL(this.getResponse2().getResponseID())) + ",\n"
			+"update_date = current_timestamp ,\n" +
                        "mobile_flag = case when mobile_flag in (1, 3) then 2 else mobile_flag end \n" +
			"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
			"and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n";
	}
	
	/**
	 * 削除用Update文を取得する。
	 * @return 
	 */
	private String	getDeleteSQL()
	{
		return	"update data_reservation\n" +
				"set\n" +
				"update_date = current_timestamp,\n" +
				"delete_date = current_timestamp\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n";
	}
	
	/**
	 * 状態フラグを更新するＳＱＬ文を取得する。
	 * @return 状態フラグを更新するＳＱＬ文
	 */
	private String	getUpdateStatusSQL()
	{
		return
			"update data_reservation\n" +
			"set\n" +
			"designated_flag = " + SQLUtil.convertForSQL(this.getDesignated()) + ",\n" +
			"staff_id = " + SQLUtil.convertForSQL(this.getStaff().getStaffID()) + ",\n" +
                        // IVS SANG START EDIT 20131115 [gb]他店舗のお会計をすると自身の店舗IDが変わってしまう
			//(SystemInfo.getCurrentShop().isReservationMenuUpdate() ? "total_time = " + SQLUtil.convertForSQL(this.getTotalTime()) + ",\n" : "") +
			(this.getShop().isReservationMenuUpdate() ? "total_time = " + SQLUtil.convertForSQL(this.getTotalTime()) + ",\n" : "") +
                        // IVS SANG START EDIT 20131115 [gb]他店舗のお会計をすると自身の店舗IDが変わってしまう
                        "status = " + SQLUtil.convertForSQL(this.getStatus()) + ",\n" +
			"sub_status = " + SQLUtil.convertForSQL(this.getSubStatus()) + ",\n" +
                        
			// visit_time  1:null  2-1:現時刻  2-2:保持値  3:保持値
			( this.getStatus() == 1 ? "visit_time = null,\n" : ( this.getStatus() == 2 ? ( this.getSubStatus() == 1 ? "visit_time = (case when visit_time is null then current_timestamp else visit_time end),\n" : "" ) : "" ) ) +
			// start_time  1:null  2-1:null    2-2:現時刻  3:保持値(なければ在店時刻)
			//( this.getStatus() == 1 ? "start_time = null,\n" : ( this.getStatus() == 2 ? ( this.getSubStatus() == 1 ? "start_time = null,\n" : "start_time = current_timestamp,\n" ) : "start_time = (select coalesce( start_time, visit_time ) from data_reservation where shop_id = 1 and reservation_no = 11),\n " ) ) +
			( this.getStatus() == 1 ? "start_time = null,\n" : ( this.getStatus() == 2 ? ( this.getSubStatus() == 1 ? "start_time = null,\n" : "start_time = (case when start_time is null then current_timestamp else start_time end),\n" ) : "start_time = (select coalesce( start_time, visit_time ) from data_reservation where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + " and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "),\n " ) ) +
			// leave_time  1:null  2-1:null    2-2:null    3:現時刻
			"leave_time = " + ( this.getStatus() <= 2 ? ( "null" ) : ( "coalesce(leave_time, current_timestamp)" ) ) + ",\n" +
			"slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + ",\n" +
			"customer_id = " + this.getCustomer().getCustomerID() + ",\n" +
			"reg_staff_id = " + (this.getRegStaff() != null && this.getRegStaff().getStaffID() != null ? SQLUtil.convertForSQL(this.getRegStaff().getStaffID()) : null) + ",\n" +
			"update_date = current_timestamp\n" +
			"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
			"and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n";
	}
	
	/**
	 * 削除用Update文を取得する。
	 * @return 
	 */
	private String	getDeleteDetailSQL()
	{
		return	"update data_reservation_detail\n" +
				"set\n" +
				"update_date = current_timestamp,\n" +
				"delete_date = current_timestamp\n" +
				"where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
				"and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n";
	}
	
	/**
	 * 按分削除用Update文を取得する
	 */
	private String getDeleteProportionallySQL()
	{
		return
			"update data_reservation_proportionally\n" +
			"set\n" +
			"update_date = current_timestamp,\n" +
			"delete_date = current_timestamp\n" +
			"where\n" +
			"shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
			"and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n";
	}
        private String getMobileFlagUpdateSQL()
        {
                return
                          "update data_reservation \n" +
                          "set\n"+
                          "mobile_flag = 2,\n" +
                          "update_date = current_timestamp\n" +
                          "where\n" +
                          "shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
                          "and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n";

        }
        public  int  upDateMobileFlag(ConnectionWrapper con)throws SQLException{
                int result= 0;
 		result	=	con.executeUpdate(this.getMobileFlagUpdateSQL());
                return result;
         }          
        
        public boolean selectSlipNoStatus( ConnectionWrapper con ) throws SQLException{

        if( this.reservationNo == null || shop.getShopID() == null  )
                return false ;

                ResultSetWrapper	rs	=	con.executeQuery(this.getLoadSlipNoSql());


                while(rs.next())
                {
                        this.setStatus(rs.getInt("status"));
                        this.setSlipNo(rs.getInt("slip_no"));
                        this.setSubStatus(rs.getInt("sub_status"));
                }  

                return true;
        }
         
        public String getLoadSlipNoSql(){

              return  "select slip_no , status ,sub_status\n"+ 
                       "from data_reservation \n" + 
                       "where reservation_no = " + reservationNo + " \n" +  
                       "and delete_date is null" + " \n" +
		       "and shop_id = "  + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n";                                                            
                                 
        }

        public String getTechnicClassContractedNameList() {
            return technicClassContractedNameList;
        }

        public void setTechnicClassContractedNameList(String technicClassContractedNameList) {
            if (technicClassContractedNameList != null && technicClassContractedNameList.length() > 0) {
                this.technicClassContractedNameList = technicClassContractedNameList.replace("{", "").replace("}", "").replace(",", "、");
            }
        }
        
        //施術時間の変更 start add
	/**
	 * 施術開始時間（start_time）施術終了時間（leave_time）を更新する
	 * @param con コネクション
	 * @throws java.sql.SQLException 
	 * @return 更新正常：true / 更新異常：false
	 */
	public boolean updateTime(ConnectionWrapper con) throws SQLException {
            String sql = this.getUpdateTimeSQL();
            return (con.executeUpdate(sql) == 1);
	}
        
        /**
         * 施術開始時間・施術終了時間を更新するSQLを返す
         * @return SQL文
         */
	private String	getUpdateTimeSQL() {
            String strSql = "update data_reservation\n" ;
            strSql = strSql + "set\n" ;
            // start_time
            strSql = strSql + "start_time = (case when start_time is null then visit_time else start_time end),\n" ;
            // leave_time
            strSql = strSql + "leave_time = (case when start_time is null then visit_time + " + SQLUtil.convertForSQL( this.getOpeMinute() + "minute" ) + " + " + SQLUtil.convertForSQL( this.getOpeSecond() + "second" ) + 
                              " else start_time + " + SQLUtil.convertForSQL( this.getOpeMinute() + "minute" ) + " + " + SQLUtil.convertForSQL( this.getOpeSecond() + "second" ) + " end)" + ",\n" ;
            strSql = strSql + "update_date = current_timestamp\n" ;
            strSql = strSql + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" ;
            strSql = strSql + "and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n";
            return strSql;
	}
        
	/**
	 * 施術開始時間（start_time）、施術終了時間（leave_time）、受付時間（visit_time）を更新する<br>
         * 【スキップお会計による精算時に使用】
	 * @param con コネクション
	 * @throws java.sql.SQLException 
	 * @return 更新正常：true / 更新異常：false
	 */
	public boolean updateTimeForSkipSales(ConnectionWrapper con) throws SQLException {
            String sql = this.getUpdateTimeForSkipSalesSQL();
            return (con.executeUpdate(sql) == 1);
	}
        
        /**
         * 施術開始時間・施術終了時間を更新するSQLを返す<br>
         * 【スキップお会計による精算時に使用】
         * @return SQL文
         */
	private String	getUpdateTimeForSkipSalesSQL() {
            String strSql = "update data_reservation\n" ;
            strSql = strSql + "set\n" ;
            strSql = strSql + "visit_time = current_timestamp + " + SQLUtil.convertForSQL( "- " + this.getOpeMinute() + "minute" ) + " + " + SQLUtil.convertForSQL( "- " + this.getOpeSecond() + "second" ) + " ,\n";
            strSql = strSql + "start_time = current_timestamp + " + SQLUtil.convertForSQL( "- " + this.getOpeMinute() + "minute" ) + " + " + SQLUtil.convertForSQL( "- " + this.getOpeSecond() + "second" ) + " ,\n";
            strSql = strSql + "leave_time = current_timestamp ,\n" ;
            strSql = strSql + "update_date = current_timestamp \n" ;
            strSql = strSql + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" ;
            strSql = strSql + "and reservation_no = " + SQLUtil.convertForSQL(this.getReservationNo()) + "\n";
            return strSql;
	}
        //施術時間の変更 end add
}
