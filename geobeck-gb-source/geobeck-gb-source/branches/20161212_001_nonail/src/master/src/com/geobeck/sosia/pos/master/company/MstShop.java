/*
 * MstShop.java
 *
 * Created on 2006/04/24, 11:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.master.company;

import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;

/**
 * 店舗マスタデータ
 * @author katagiri
 */
public class MstShop implements Cloneable
{
	/**
	 * 店舗名の文字数の最大値
	 */
	public static int		SHOP_NAME_MAX			=	50;
	/**
	 * 郵便番号の文字数の最大値
	 */
	public static int		POSTAL_CODE_MAX			=	7;
	/**
	 * 電話番号の文字数の最大値
	 */
	public static int		PHONE_NUMBER_MAX		=	15;
	/**
	 * ＦＡＸ番号の文字数の最大値
	 */
	public static int		FAX_NUMBER_MAX			=	15;
	/**
	 * メールアドレスの文字数の最大値
	 */
	public static int		MAIL_ADDRESS_MAX		=	64;
	/**
	 * 会計単位の最小値
	 */
	public static int		ACCOUNTING_UNIT_MIN		=	1;
	/**
	 * 会計単位の最大値
	 */
	public static int		ACCOUNTING_UNIT_MAX		=	2;
	/**
	 * 端数丸めの最小値
	 */
	public static int		ROUNDING_MIN			=	1;
	/**
	 * 端数丸めの最大値
	 */
	public static int		ROUNDING_MAX			=	3;
	/**
	 * 締め日の最小値
	 */
	public static int		CUTOFF_DAY_MIN			=	1;
	/**
	 * 締め日の最大値
	 */
	public static int		CUTOFF_DAY_MAX			=	31;
	
	/**
	 * 店舗ＩＤ
	 */
	private	Integer		shopID				=	null;
	/**
	 * グループＩＤ
	 */
	private	Integer		groupID				=	null;
	/**
	 * 店舗名
	 */
	private	String		shopName			=	"";
	/**
	 * 郵便番号
	 */
	private	String		postalCode			=	"";
	/**
	 * 住所
	 */
	private	String[]	address				=	{"", "", "", ""};
	/**
	 * 電話番号
	 */
	private	String		phoneNumber			=	"";
	/**
	 * ＦＡＸ番号
	 */
	private	String		faxNumber			=	"";
	/**
	 * メールアドレス
	 */
	private	String		mailAddress			=	"";
	/**
	 * 会計単位
	 */
	private	Integer		accountingUnit		=	null;
	/**
	 * 端数丸め
	 */
	private	Integer		rounding			=	null;
	/**
	 * 締め日
	 */
	private	Integer		cutoffDay			=	null;
        
       //An start add 20130322 有効期限間近
        private	Integer		coursedisplayday			=	null;
        //An end add 20130233 有効期限間近
       
	/**
	 * 開店時間（時）
	 */
	private Integer		openHour			=	null;
	/**
	 * 開店時間（分）
	 */
	private Integer		openMinute			=	null;
	/**
	 * 閉店時間（時）
	 */
	private Integer		closeHour			=	null;
	/**
	 * 閉店時間（分）
	 */
	private Integer		closeMinute			=	null;
	/**
	 * 表示単位
	 */
	private Integer		term				=	null;
	/**
	 * 階層
	 */
	private	Integer		level				=	0;
	
	/**
         * 施術台の使用有無
         */
        private Integer         bed                             =       0;
        
	/**
         * 按分の使用有無
         */
        private Integer         proportionally                  =       0;

	/**
         * 按分のお会計画面表示有無
         */
        private Integer         displayProportionally           =       0;

	/**
         * 予約表空行の表示有無
         */
        private Integer         reservationNullLine             =       0;

	/**
         * お会計内容を予約表に反映の表示有無
         */
        private Integer         reservationMenuUpdate             =       0;

	/**
         * 予約表のスタッフシフト表示有無
         */
        private Integer         reservationStaffShift             =       0;

	/**
         * 予約表休日スタッフ表示有無
         */
        private Integer         reservationStaffHoliday           =       0;

	/**
         * 出退勤シフト連動有無
         */
        private Integer         shiftGearWorking             =       0;

	/**
         * 指名入力アシスト有無
         */
        private Integer         designatedAssist             =       0;

	/**
         * 顧客No.自動採番の使用有無
         */
        private Integer         autoNumber                      =       0;
        
	/**
	 * 接頭文字
	 */
	private	String		prefixString			=	"";
        
	/**
	 * 連番桁数
	 */
	private	Integer		seqLength			=	null;

	/**
         * 誕生日マーク表示設定
         */
        private Integer         birthMonthFlag             =       0;
	/**
	 * 誕生日の何日前
	 */
	private	Integer		birthBeforeDays			=	null;
	/**
	 * 誕生日の何日後
	 */
	private	Integer		birthAfterDays			=	null;
        // start add 20130114 nakhoa 店舗情報登録
        /*
         * 二重予約警告フラグ
         */
        private	Integer		reservationDuplicateWarningFlag			=	null;
        
        /*
         * 予約率表示
         */
        private	Integer		reservationRateDisplay			=	null;
        
        /*
         * しきい値赤色
         */
        private	Integer		displayReservationRatoRed			=	null;
        
        /*
         * しきい値黄色
         */
        private	Integer		displayReservationRatoYellow			=	null;
        
        /*
         * 全予約バッファ時間
         */
        private	Integer		reservationBufferTime			=	null;
        
        /*
         * エリアID
         */
        private	Integer		areaId			=	null;
        
        /*
         * 推薦フラグ
         */
        private	Integer		recommendationFlag			=	null;
        
        // end add 20130114 nakhoa 店舗情報登録
        
        /**
	 * 役務機能の使用
	 */
	private	Integer		courseFlag			=	null;
        // end add 20130802 nakhoa 役務機能使用有無の制御追加
        
        //An start add 20130328 店舗情報登録
         private Integer          tabIndex =0;
        //An end add 20130328 店舗情報登録
         
            //vtbphuong start add 20140421 Request #22456
        private Integer reservationOverbookingFlag = null;

        //IVS_TTMLOAN Start add 20140703 MASHU_店舗情報登録
        private Integer useShopCategory = null;

        public Integer getUseShopCategory() {
            return useShopCategory;
        }

        public void setUseShopCategory(Integer useShopCategory) {
            this.useShopCategory = useShopCategory;
        }
       //IVS_TTMLOAN End add 20140703 MASHU_店舗情報登録

        public Integer getReservationOverbookingFlag() {
            return reservationOverbookingFlag;
        }

        public void setReservationOverbookingFlag(Integer reservationOverbookingFlag) {
            this.reservationOverbookingFlag = reservationOverbookingFlag;
        }
         //vtbphuong end add 20140421 Request #22456
	/**
	 * コンストラクタ
	 */
	public MstShop()
	{
	}
        //An start add 20130328 店舗情報登録
        public Integer getTabIndex() {
            return tabIndex;
        }

        public void setTabIndex(Integer tabIndex) {
            this.tabIndex = tabIndex;
        }

         //An end add 20130328 店舗情報登録
    
	/**
	 * 文字列に変換する。（店舗名）
	 * @return 店舗名
	 */
	public String toString()
	{
		StringBuffer	sb	=	new StringBuffer();
		
		for(int i = 0; i < this.level; i ++)
				sb.append("　　");
		
		return	sb.toString() + this.getShopName();
	}
	
	public boolean equals(Object o)
	{
		if(o != null && o instanceof MstShop)
		{
			if(((MstShop)o).getShopID() == this.getShopID())
			{
				return	true;
			}
			else
			{
				return	false;
			}
		}
		else
		{
			return	false;
		}
	}

	/**
	 * 店舗ＩＤを取得する。
	 * @return 店舗ＩＤ
	 */
	public Integer getShopID()
	{
		return shopID;
	}

	/**
	 * 店舗ＩＤをセットする。
	 * @param shopID 店舗ＩＤ
	 */
	public void setShopID(Integer shopID)
	{
		this.shopID = shopID;
	}

	public Integer getGroupID()
	{
		return groupID;
	}

	public void setGroupID(Integer groupID)
	{
		this.groupID = groupID;
	}

	/**
	 * 店舗名を取得する。
	 * @return 店舗名
	 */
	public String getShopName()
	{
		return shopName == null ? "" : shopName;
	}

	/**
	 * 店舗名をセットする。
	 * @param shopName 店舗名
	 */
	public void setShopName(String shopName)
	{
		if(shopName == null || shopName.length() <= MstShop.SHOP_NAME_MAX)
		{
			this.shopName	=	shopName;
		}
		else
		{
			this.shopName	=	shopName.substring(0, MstShop.SHOP_NAME_MAX);
		}
	}

	/**
	 * 郵便番号を取得する。
	 * @return 郵便番号
	 */
	public String getPostalCode()
	{
		return postalCode;
	}

	/**
	 * 郵便番号をセットする。
	 * @param postalCode 郵便番号
	 */
	public void setPostalCode(String postalCode)
	{
		if(postalCode == null || postalCode.length() <= MstShop.POSTAL_CODE_MAX)
		{
			this.postalCode	=	postalCode;
		}
		else
		{
			this.postalCode	=	postalCode.substring(0, MstShop.POSTAL_CODE_MAX);
		}
	}

        /** 住所の各要素を改行で区切った表現を返す. 
         * @return 住所の表現
         */
        public String getPrettyAddress(){
            StringBuffer sb = new StringBuffer();
            sb.append("〒"+this.getPostalCode()+"\n");
            for(String s:this.getAddress()){
                sb.append(s);
                sb.append("\n");
            }
            String R = sb.toString();
            return R.substring(0,sb.length() - 1);
        }

	/**
	 * 住所を取得する。
	 * @return 住所
	 */
	public String[] getAddress()
	{
		return address;
	}

	/**
	 * 住所を取得する。
	 * @param index インデックス
	 * @return 住所
	 */
	public String getAddress(int index)
	{
		return address[index];
	}

	/**
	 * 住所
	 * @param address 住所
	 */
	public void setAddress(String[] address)
	{
		this.address = address;
	}

	/**
	 * 住所をセットする。
	 * @param index インデックス
	 * @param address 住所
	 */
	public void setAddress(int index, String address)
	{
		this.address[index] = address;
	}
	
	public String getFullAddress()
	{
		return	(address[0] == null ? "" : address[0]) +
				(address[1] == null ? "" : address[1]) +
				(address[2] == null ? "" : address[2]) +
				(address[3] == null ? "" : address[3]);
	}

	/**
	 * 電話番号を取得する。
	 * @return 電話番号
	 */
	public String getPhoneNumber()
	{
            return phoneNumber;
	}

	/**
	 * 電話番号をセットする。
	 * @param phoneNumber 電話番号
	 */
	public void setPhoneNumber(String phoneNumber)
	{
            if (phoneNumber == null) return;
            
            if (phoneNumber.length() <= MstShop.PHONE_NUMBER_MAX) {
                this.phoneNumber = phoneNumber;
            } else {
                this.phoneNumber = phoneNumber.substring(0, MstShop.PHONE_NUMBER_MAX);
            }
	}

	/**
	 * ＦＡＸ番号を取得する。
	 * @return ＦＡＸ番号
	 */
	public String getFaxNumber()
	{
            return faxNumber;
	}

	/**
	 * ＦＡＸ番号をセットする。
	 * @param faxNumber ＦＡＸ番号
	 */
	public void setFaxNumber(String faxNumber)
	{
            if (faxNumber == null) return;
            
            if (faxNumber.length() <= MstShop.FAX_NUMBER_MAX) {
                this.faxNumber = faxNumber;
            } else {
                this.faxNumber = faxNumber.substring(0, MstShop.FAX_NUMBER_MAX);
            }
	}

	/**
	 * メールアドレスを取得する。
	 * @return メールアドレス
	 */
	public String getMailAddress()
	{
            return mailAddress;
	}

	/**
	 * メールアドレスをセットする。
	 * @param mailAddress メールアドレス
	 */
	public void setMailAddress(String mailAddress)
	{
            if (mailAddress == null) return;
            
            if (mailAddress.length() <= MstShop.MAIL_ADDRESS_MAX) {
                this.mailAddress = mailAddress;
            } else {
                this.mailAddress = mailAddress.substring(0, MstShop.MAIL_ADDRESS_MAX);
            }
	}

	/**
	 * 会計単位を取得する。
	 * @return 会計単位
	 */
	public Integer getAccountingUnit()
	{
		return accountingUnit;
	}

	/**
	 * 会計単位をセットする。
	 * @param accountingUnit 会計単位
	 */
	public void setAccountingUnit(Integer accountingUnit)
	{
		if(accountingUnit == null	||
				accountingUnit < MstShop.ACCOUNTING_UNIT_MIN ||
				MstShop.ACCOUNTING_UNIT_MAX < accountingUnit)
		{
			this.accountingUnit	=	null;
		}
		else
		{
			this.accountingUnit	=	accountingUnit;
		}
	}

	/**
	 * 端数丸めを取得する。
	 * @return 端数丸め
	 */
	public Integer getRounding()
	{
		return rounding;
	}

	/**
	 * 端数丸めをセットする。
	 * @param rounding 端数丸め
	 */
	public void setRounding(Integer rounding)
	{
		if(rounding == null	||
				rounding < MstShop.ROUNDING_MIN ||
				MstShop.ROUNDING_MAX < rounding)
		{
			this.rounding	=	null;
		}
		else
		{
			this.rounding	=	rounding;
		}
	}

	/**
	 * 締め日を取得する。
	 * @return 締め日
	 */
	public Integer getCutoffDay()
	{
		return cutoffDay;
	}

	/**
	 * 締め日をセットする。
	 * @param cutoffDay 締め日
	 */
	public void setCutoffDay(Integer cutoffDay)
	{
		if(cutoffDay == null	||
				cutoffDay < MstShop.CUTOFF_DAY_MIN ||
				MstShop.CUTOFF_DAY_MAX < cutoffDay)
		{
			this.cutoffDay	=	null;
		}
		else
		{
			this.cutoffDay	=	cutoffDay;
		}
	}

        //An start add 20130322 有効期限間近
        public Integer getCourseDisplayDay()
	{
		return coursedisplayday;
	}

	public void setCourseDisplayDay(Integer coursedisplayday)
	{
		if(coursedisplayday == null	||
				coursedisplayday < MstShop.CUTOFF_DAY_MIN ||
				MstShop.CUTOFF_DAY_MAX < coursedisplayday)
		{
			this.coursedisplayday	=	null;
		}
		else
		{
			this.coursedisplayday	=	coursedisplayday;
		}
	}
        //An end add 20130233 有効期限間近
	/**
	 * 開店時間（時）を取得する。
	 * @return 開店時間（時）
	 */
	public Integer getOpenHour()
	{
		return openHour;
	}

	/**
	 * 開店時間（時）をセットする。
	 * @param openHour 開店時間（時）
	 */
	public void setOpenHour(Integer openHour)
	{
		this.openHour = openHour;
	}

	/**
	 * 開店時間（分）を取得する。
	 * @return 開店時間（分）
	 */
	public Integer getOpenMinute()
	{
		return openMinute;
	}

	/**
	 * 開店時間（分）をセットする。
	 * @param openMinute 開店時間（分）
	 */
	public void setOpenMinute(Integer openMinute)
	{
		this.openMinute = openMinute;
	}
	
	/**
	 * 開店時間をdateにセットする。
	 * @param date 開店時間をセットする日付
	 * @return 開店時間をセットしたdate
	 */
	public GregorianCalendar getOpenTime(GregorianCalendar date)
	{
		if(date == null)	return	null;
		
		GregorianCalendar	calendar	=	(GregorianCalendar)date.clone();
		calendar.set(GregorianCalendar.HOUR_OF_DAY, openHour);
		calendar.set(GregorianCalendar.MINUTE, openMinute);
		return	calendar;
	}
	
	/**
	 * 開店時間をdateにセットする。
	 * @param date 開店時間をセットする日付
	 * @return 開店時間をセットしたdate
	 */
	public java.util.Date getOpenTime(java.util.Date date)
	{
		if(date == null)	return	null;
		
		GregorianCalendar	calendar	=	new GregorianCalendar();
		calendar.setTime(date);
		return	this.getOpenTime(calendar).getTime();
	}

	/**
	 * 閉店時間（時）を取得する。
	 * @return 閉店時間（時）
	 */
	public Integer getCloseHour()
	{
		return closeHour;
	}

	/**
	 * 閉店時間（時）をセットする。
	 * @param closeHour 閉店時間（時）
	 */
	public void setCloseHour(Integer closeHour)
	{
		this.closeHour = closeHour;
	}

	/**
	 * 閉店時間（分）を取得する。
	 * @return 閉店時間（分）
	 */
	public Integer getCloseMinute()
	{
		return closeMinute;
	}

	/**
	 * 閉店時間（分）をセットする。
	 * @param closeMinute 閉店時間（分）
	 */
	public void setCloseMinute(Integer closeMinute)
	{
		this.closeMinute = closeMinute;
	}
	
	/**
	 * 閉店時間をdateにセットする。
	 * @param date 閉店時間をセットする日付
	 * @return 閉店時間をセットしたdate
	 */
	public GregorianCalendar getCloseTime(GregorianCalendar date)
	{
		if(date == null)	return	null;
		
		GregorianCalendar	calendar	=	(GregorianCalendar)date.clone();
		if(24 <= closeHour)
		{
			calendar.add(GregorianCalendar.DAY_OF_MONTH, 1);
			calendar.set(GregorianCalendar.HOUR_OF_DAY, closeHour - 24);
		}
		else
		{
			calendar.set(GregorianCalendar.HOUR_OF_DAY, closeHour);
		}
		calendar.set(GregorianCalendar.MINUTE, closeMinute);
		return	calendar;
	}
	
	/**
	 * 閉店時間をdateにセットする。
	 * @param date 閉店時間をセットする日付
	 * @return 閉店時間をセットしたdate
	 */
	public java.util.Date getCloseTime(java.util.Date date)
	{
		if(date == null)	return	null;
		
		GregorianCalendar	calendar	=	new GregorianCalendar();
		calendar.setTime(date);
		return	this.getCloseTime(calendar).getTime();
	}

	/**
	 * 表示単位を取得する。
	 * @return 表示単位
	 */
	public Integer getTerm()
	{
		return term;
	}

	/**
	 * 表示単位をセットする。
	 * @param term 表示単位
	 */
	public void setTerm(Integer term)
	{
		this.term = term;
	}
	
        public Integer getBed()
        {
            return bed;
        }
        
        public void setBed(Integer bed)
        {
            this.bed = bed;
        }
        
        public boolean isBed(){
            return (bed == null || bed != 0);
        }
	
        public Integer getProportionally()
        {
            return proportionally;
        }
        
        public void setProportionally(Integer proportionally)
        {
            this.proportionally = proportionally;
        }
        
        public boolean isProportionally(){
            return (proportionally == null || proportionally != 0);
        }

        public Integer getDisplayProportionally()
        {
            return displayProportionally;
        }
        
        public void setDisplayProportionally(Integer displayProportionally)
        {
            this.displayProportionally = displayProportionally;
        }
        
        public boolean isDisplayProportionally(){
            return (displayProportionally == null || displayProportionally != 0);
        }
        
        public Integer getReservationNullLine()
        {
            return reservationNullLine;
        }
        
        public void setReservationNullLine(Integer reservationNullLine)
        {
            this.reservationNullLine = reservationNullLine;
        }
        
        public boolean isReservationNullLine(){
            return (reservationNullLine == null || reservationNullLine != 0);
        }

        public Integer getReservationMenuUpdate()
        {
            return reservationMenuUpdate;
        }

        public void setReservationMenuUpdate(Integer reservationMenuUpdate)
        {
            this.reservationMenuUpdate = reservationMenuUpdate;
        }

        public boolean isReservationMenuUpdate(){
            return (reservationMenuUpdate == null || reservationMenuUpdate != 0);
        }

        public Integer getReservationStaffShift()
        {
            return reservationStaffShift;
        }
        
        public void setReservationStaffShift(Integer reservationStaffShift)
        {
            this.reservationStaffShift = reservationStaffShift;
        }
        
        public boolean isReservationStaffShift(){
            return (reservationStaffShift == null || reservationStaffShift != 0);
        }

        public Integer getReservationStaffHoliday()
        {
            return reservationStaffHoliday;
        }

        public void setReservationStaffHoliday(Integer reservationStaffHoliday)
        {
            this.reservationStaffHoliday = reservationStaffHoliday;
        }

        public boolean isReservationStaffHoliday(){
            return (reservationStaffHoliday == null || reservationStaffHoliday != 0);
        }

        public Integer getShiftGearWorking()
        {
            return shiftGearWorking;
        }
        
        public void setShiftGearWorking(Integer shiftGearWorking)
        {
            this.shiftGearWorking = shiftGearWorking;
        }
        
        public boolean isShiftGearWorking(){
            return (shiftGearWorking == null || shiftGearWorking != 0);
        }

        public Integer getDesignatedAssist()
        {
            return designatedAssist;
        }

        public void setDesignatedAssist(Integer designatedAssist)
        {
            this.designatedAssist = designatedAssist;
        }

        public boolean isDesignatedAssist(){
            return (designatedAssist == null || designatedAssist != 0);
        }

        public Integer getAutoNumber()
        {
            return autoNumber;
        }
        
        public void setAutoNumber(Integer autoNumber)
        {
            this.autoNumber = autoNumber;
        }
        
        public boolean isAutoNumber(){
            return (autoNumber == null || autoNumber != 0);
        }
        
	/**
	 * 接頭文字を取得する。
	 * @return 接頭文字
	 */
	public String getPrefixString()
	{
		return prefixString == null ? "" : prefixString;
	}

	/**
	 * 接頭文字をセットする。
	 * @param prefixString 接頭文字
	 */
	public void setPrefixString(String prefixString)
	{
                this.prefixString = (prefixString == null ? "" : prefixString);
	}

	/**
	 * 連番桁数を取得する。
	 * @return 連番桁数
	 */
	public Integer getSeqLength()
	{
		return seqLength;
	}

	/**
	 * 連番桁数をセットする。
	 * @param seqLength 連番桁数
	 */
	public void setSeqLength(Integer seqLength)
	{
		this.seqLength = seqLength;
	}

        public Integer getBirthMonthFlag()
        {
            return birthMonthFlag;
        }
        
        public void setBirthMonthFlag(Integer birthMonthFlag)
        {
            this.birthMonthFlag = birthMonthFlag;
        }
        
        public boolean isBirthMonthFlag(){
            return (birthMonthFlag != null && birthMonthFlag == 1);
        }
	public Integer getBirthBeforeDays()
	{
		return birthBeforeDays;
	}
	public void setBirthBeforeDays(Integer birthBeforeDays)
	{
		this.birthBeforeDays = birthBeforeDays;
	}
	public Integer getBirthAfterDays()
	{
		return birthAfterDays;
	}
	public void setBirthAfterDays(Integer birthAfterDays)
	{
		this.birthAfterDays = birthAfterDays;
	}
        
        // start add 20130114n akhoa 店舗情報登録
        public Integer getReservationDuplicateWarningFlag()
	{
		return reservationDuplicateWarningFlag;
	}
	public void setReservationDuplicateWarningFlag(Integer reservationDuplicateWarningFlag)
	{
                if(reservationDuplicateWarningFlag == null){
                        this.reservationDuplicateWarningFlag = null;
                }else{
                        this.reservationDuplicateWarningFlag = reservationDuplicateWarningFlag;
                }
	}
        public boolean isReservationDuplicateWarningFlag(){
            return (reservationDuplicateWarningFlag == null || reservationDuplicateWarningFlag != 0);
        }
        
        public Integer getDisplayRreservationRatoFlag()
	{
		return reservationRateDisplay;
	}
	public void setDisplayRreservationRatoFlag(Integer reservationRateDisplay)
	{
                if(reservationRateDisplay == null){
                        this.reservationRateDisplay = null;
                }else{
                        this.reservationRateDisplay = reservationRateDisplay;
                }
	}
        public boolean isDisplayRreservationRatoFlag(){
            return (reservationRateDisplay == null || reservationRateDisplay != 0);
        }
        
        public Integer getDisplayReaservationRatoRed()
	{
                return this.displayReservationRatoRed;
	}
	public void setDisplayReaservationRatoRed(Integer displayReservationRatoRed)
	{
                if(displayReservationRatoRed == null){
                        this.displayReservationRatoRed = null;
                }else{
                        this.displayReservationRatoRed = displayReservationRatoRed;
                }
	}
        
        public Integer getDisplayReaservationRatoYellow()
	{
		return displayReservationRatoYellow;
	}
	public void setDisplayReaservationRatoYellow(Integer displayReservationRatoYellow)
	{
                if(displayReservationRatoYellow == null){
                        this.displayReservationRatoYellow = null;
                }else{
                        this.displayReservationRatoYellow = displayReservationRatoYellow;
                }
	}
        
        public Integer getReservationBufferTime()
	{
		return reservationBufferTime;
	}
	public void setReservationBufferTime(Integer reservationBufferTime)
	{
                if(reservationBufferTime == null){
                        this.reservationBufferTime = null;
                }else{
                        this.reservationBufferTime = reservationBufferTime;
                }
	}
        
        public Integer getAreaId()
	{
		return areaId;
	}
	public void setAreaId(Integer areaId)
	{
                if(areaId == null){
                        this.areaId = null;
                }else{
                        this.areaId = areaId;
                }
	}

        public Integer getRecommendationFlag()
	{
		return recommendationFlag;
	}
	public void setRecommendationFlag(Integer recommendationFlag)
	{
                if(recommendationFlag == null){
                        this.recommendationFlag = null;
                }else{
                        this.recommendationFlag = recommendationFlag;
                }
	}
        
        // end add 20130114 nakhoa 店舗情報登録
        // start add 20130802 nakhoa 役務機能使用有無の制御追加
        public Integer getCourseFlag()
	{
		return courseFlag;
	}
	public void setCourseFlag(Integer courseFlag)
	{
		this.courseFlag = courseFlag;
	}
        // end add 20130802 nakhoa 役務機能使用有無の制御追加
	/**
	 * データをクリアする。
	 */
	public void clear()
	{
		this.setShopID(null);
		this.setGroupID(null);
		this.setShopName("");
		this.setPostalCode("");
		this.setAddress(0, "");
		this.setAddress(1, "");
		this.setAddress(2, "");
		this.setAddress(3, "");
		this.setPhoneNumber("");
		this.setFaxNumber("");
		this.setMailAddress("");
		this.setAccountingUnit(null);
		this.setRounding(null);
		this.setCutoffDay(null);
                 //An start add 20130322 有効期限間近
                this.setCourseDisplayDay(null);
                 //An end add 20130322 有効期限間近
		this.setOpenHour(null);
		this.setOpenMinute(null);
		this.setCloseHour(null);
		this.setCloseMinute(null);
		this.setTerm(null);
                this.setBed(0);
                this.setProportionally(0);
                this.setDisplayProportionally(0);
                this.setReservationNullLine(0);
                this.setReservationMenuUpdate(0);
                this.setReservationStaffShift(0);
                this.setReservationStaffHoliday(0);
                this.setShiftGearWorking(0);
                this.setDesignatedAssist(0);
                this.setAutoNumber(0);
		this.setPrefixString("");
		this.setSeqLength(null);
                this.setBirthMonthFlag(0);
		this.setBirthBeforeDays(null);
		this.setBirthAfterDays(null);
                // start add 20130114 nakhoa 店舗情報登録
                this.setAreaId(null);
                this.setReservationDuplicateWarningFlag(null);
                this.setDisplayRreservationRatoFlag(null);
                this.setDisplayReaservationRatoRed(null);
                this.setDisplayReaservationRatoYellow(null);
                this.setReservationBufferTime(null);
                this.setRecommendationFlag(0);
                // end add 20130114 nakhoa 店舗情報登録
                // start add 20130802 nakhoa 役務機能使用有無の制御追加
                this.setCourseFlag(null);
                // end add 20130802 nakhoa 役務機能使用有無の制御追加
	}
	
	/**
	 * 店舗マスタから、設定されている店舗IDのデータを読み込む。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean load(ConnectionWrapper con) throws SQLException
	{
		if(this.getShopID() == null || this.getShopID() < 0)	return	true;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())
		{
			this.setData(rs);
		}
		else
		{
			return false;
		}
		
		return	true;
	}
	
	/**
	 * ResultSetWrapperのデータを読み込む。
	 * 
	 * @param ms 
	 */
	public void setData(MstShop ms)
	{
		this.setShopID(ms.getShopID());
		this.setGroupID(ms.getGroupID());
		this.setShopName(ms.getShopName());
		this.setPostalCode(ms.getPostalCode());
		this.setAddress(ms.getAddress());
		this.setPhoneNumber(ms.getPhoneNumber());
		this.setFaxNumber(ms.getFaxNumber());
		this.setMailAddress(ms.getMailAddress());
		this.setAccountingUnit(ms.getAccountingUnit());
		this.setRounding(ms.getRounding());
		this.setCutoffDay(ms.getCutoffDay());
                 //An start add 20130322 有効期限間近
                this.setCourseDisplayDay(ms.getCourseDisplayDay());
                 //An end add 20130322 有効期限間近
		this.setOpenHour(ms.getOpenHour());
		this.setOpenMinute(ms.getOpenMinute());
		this.setCloseHour(ms.getCloseHour());
		this.setCloseMinute(ms.getCloseMinute());
		this.setTerm(ms.getTerm());
                this.setBed(ms.getBed());
                this.setProportionally(ms.getProportionally());
                this.setDisplayProportionally(ms.getDisplayProportionally());
                this.setReservationNullLine(ms.getReservationNullLine());
                this.setReservationMenuUpdate(ms.getReservationMenuUpdate());
                this.setReservationStaffShift(ms.getReservationStaffShift());
                this.setReservationStaffHoliday(ms.getReservationStaffHoliday());
                this.setShiftGearWorking(ms.getShiftGearWorking());
                this.setDesignatedAssist(ms.getDesignatedAssist());
                this.setAutoNumber(ms.getAutoNumber());
		this.setPrefixString(ms.getPrefixString());
		this.setSeqLength(ms.getSeqLength());
                this.setBirthMonthFlag(ms.getBirthMonthFlag());
		this.setBirthBeforeDays(ms.getBirthBeforeDays());
		this.setBirthAfterDays(ms.getBirthAfterDays());
				// IVS SANG START DELETE 20131102
				// this.setBirthAfterDays(null);
				// IVS SANG END DELETE 20131102
                // start add 20130114 nakhoa 店舗情報登録
                this.setAreaId(ms.getAreaId());
                this.setReservationDuplicateWarningFlag(ms.getReservationDuplicateWarningFlag());
                this.setDisplayRreservationRatoFlag(ms.getDisplayRreservationRatoFlag());
                this.setDisplayReaservationRatoRed(ms.getDisplayReaservationRatoRed());
                this.setDisplayReaservationRatoYellow(ms.getDisplayReaservationRatoYellow());
                this.setReservationBufferTime(ms.getReservationBufferTime());
                this.setRecommendationFlag(ms.getRecommendationFlag());
                // end add 20130114 nakhoa 店舗情報登録
                // start add 20130802 nakhoa 役務機能使用有無の制御追加
                this.setCourseFlag(ms.getCourseFlag());
                // end add 20130802 nakhoa 役務機能使用有無の制御追加
                 //vtbphuong start add 20140421 Request #22456
                this.setReservationOverbookingFlag(ms.getReservationOverbookingFlag());
                //vtbphuong end add 20140421 Request #22456
                //IVS_TTMLoan Start add 20140707 MASHU_店舗情報登録
                this.setUseShopCategory(ms.getUseShopCategory());
                //IVS_TTMLoan End add 20140707 MASHU_店舗情報登録
	}
	
	/**
	 * ResultSetWrapperのデータを読み込む。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSetWrapper rs) throws SQLException
	{
		this.setShopID(rs.getInt("shop_id"));
		this.setGroupID(rs.getInt("group_id"));
		this.setShopName(rs.getString("shop_name"));
		this.setPostalCode(rs.getString("postal_code"));
		this.setAddress(0, rs.getString("address1"));
		this.setAddress(1, rs.getString("address2"));
		this.setAddress(2, rs.getString("address3"));
		this.setAddress(3, rs.getString("address4"));
		this.setPhoneNumber(rs.getString("phone_number"));
		this.setFaxNumber(rs.getString("fax_number"));
		this.setMailAddress(rs.getString("mail_address"));
		this.setAccountingUnit(rs.getInt("accounting_unit"));
		this.setRounding(rs.getInt("rounding"));
		this.setCutoffDay(rs.getInt("cutoff_day"));
                 //An start add 20130322 有効期限間近
                this.setCourseDisplayDay(rs.getInt("course_display_day"));
                 //An start add 20130322 有効期限間近
		this.setOpenHour(rs.getInt("open_hour"));
		this.setOpenMinute(rs.getInt("open_minute"));
		this.setCloseHour(rs.getInt("close_hour"));
		this.setCloseMinute(rs.getInt("close_minute"));
		this.setTerm(rs.getInt("term"));
                this.setBed(rs.getInt("bed"));
                this.setProportionally(rs.getInt("proportionally"));
                this.setDisplayProportionally(rs.getInt("display_proportionally"));
                this.setReservationNullLine(rs.getInt("reservation_null_line"));
                this.setReservationMenuUpdate(rs.getInt("reservation_menu_update"));
                this.setReservationStaffShift(rs.getInt("reservation_staff_shift"));
                this.setReservationStaffHoliday(rs.getInt("reservation_staff_holiday"));
                this.setShiftGearWorking(rs.getInt("shift_gear_working"));
                this.setDesignatedAssist(rs.getInt("designated_assist"));
                this.setAutoNumber(rs.getInt("auto_number"));
		this.setPrefixString(rs.getString("prefix_string"));
		this.setSeqLength(rs.getInt("seq_length"));
                this.setBirthMonthFlag(rs.getInt("birth_month_flag"));
		this.setBirthBeforeDays(rs.getInt("birth_before_days"));
		this.setBirthAfterDays(rs.getInt("birth_after_days"));
                // start add 20130114 nakhoa 店舗情報登録
                if(rs.getString("area_id") == null){
                    this.setAreaId(null);
                }else{
                    this.setAreaId(rs.getInt("area_id"));
                }
                if(rs.getString("reservation_duplicate_warning_flag") == null){
                    this.setReservationDuplicateWarningFlag(null);
                }else{
                    this.setReservationDuplicateWarningFlag(rs.getInt("reservation_duplicate_warning_flag"));
                }
                if(rs.getString("display_reservation_rato_flag") == null){
                    this.setDisplayRreservationRatoFlag(null);
                }else{
                    this.setDisplayRreservationRatoFlag(rs.getInt("display_reservation_rato_flag"));
                }
                if(rs.getString("display_reaservation_rato_red") == null){
                    this.setDisplayReaservationRatoRed(null);
                }else{
                    this.setDisplayReaservationRatoRed(rs.getInt("display_reaservation_rato_red"));
                }
                if(rs.getString("display_reaservation_rato_yellow") == null){
                    this.setDisplayReaservationRatoYellow(null);
                }else{
                    this.setDisplayReaservationRatoYellow(rs.getInt("display_reaservation_rato_yellow"));
                }
                if(rs.getString("reservation_buffer_time") == null){
                    this.setReservationBufferTime(null);
                }else{
                    this.setReservationBufferTime(rs.getInt("reservation_buffer_time"));
                }
                if(rs.getString("recommendation_flag") == null){
                    this.setRecommendationFlag(null);
                }else{
                    this.setRecommendationFlag(rs.getInt("recommendation_flag"));
                }
                this.setTabIndex(rs.getInt("customer_tab_index"));
                
                // end add 20130114 nakhoa 店舗情報登録
                this.setBirthAfterDays(rs.getInt("birth_after_days"));
                // start add 20130802 nakhoa 役務機能使用有無の制御追加
                this.setCourseFlag(rs.getInt("course_flg"));
                // end add 20130802 nakhoa 役務機能使用有無の制御追加
                
                //vtbphuong start add 20140421 Request #22456
                this.setReservationOverbookingFlag(rs.getInt("reservation_overbooking_flag"));
                //vtbphuong end add 20140421 Request #22456
                
                //IVS_TTMLoan Start add 20140707 MASHU_店舗情報登録
                this.setUseShopCategory(rs.getInt("use_shop_category"));
                //IVS_TTMLoan End add 20140707 MASHU_店舗情報登録
	}
	
// 2017/01/04 顧客メモ ADD START
	/**
	 * ResultSetのデータを読み込む。
	 * @param rs ResultSetWrapper
	 * @throws java.sql.SQLException SQLException
	 */
	public void setData(ResultSet rs) throws SQLException
	{
		this.setShopID(rs.getInt("shop_id"));
		this.setGroupID(rs.getInt("group_id"));
		this.setShopName(rs.getString("shop_name"));
		this.setPostalCode(rs.getString("postal_code"));
		this.setAddress(0, rs.getString("address1"));
		this.setAddress(1, rs.getString("address2"));
		this.setAddress(2, rs.getString("address3"));
		this.setAddress(3, rs.getString("address4"));
		this.setPhoneNumber(rs.getString("phone_number"));
		this.setFaxNumber(rs.getString("fax_number"));
		this.setMailAddress(rs.getString("mail_address"));
		this.setAccountingUnit(rs.getInt("accounting_unit"));
		this.setRounding(rs.getInt("rounding"));
		this.setCutoffDay(rs.getInt("cutoff_day"));
                 //An start add 20130322 有効期限間近
                this.setCourseDisplayDay(rs.getInt("course_display_day"));
                 //An start add 20130322 有効期限間近
		this.setOpenHour(rs.getInt("open_hour"));
		this.setOpenMinute(rs.getInt("open_minute"));
		this.setCloseHour(rs.getInt("close_hour"));
		this.setCloseMinute(rs.getInt("close_minute"));
		this.setTerm(rs.getInt("term"));
                this.setBed(rs.getInt("bed"));
                this.setProportionally(rs.getInt("proportionally"));
                this.setDisplayProportionally(rs.getInt("display_proportionally"));
                this.setReservationNullLine(rs.getInt("reservation_null_line"));
                this.setReservationMenuUpdate(rs.getInt("reservation_menu_update"));
                this.setReservationStaffShift(rs.getInt("reservation_staff_shift"));
                this.setReservationStaffHoliday(rs.getInt("reservation_staff_holiday"));
                this.setShiftGearWorking(rs.getInt("shift_gear_working"));
                this.setDesignatedAssist(rs.getInt("designated_assist"));
                this.setAutoNumber(rs.getInt("auto_number"));
		this.setPrefixString(rs.getString("prefix_string"));
		this.setSeqLength(rs.getInt("seq_length"));
                this.setBirthMonthFlag(rs.getInt("birth_month_flag"));
		this.setBirthBeforeDays(rs.getInt("birth_before_days"));
		this.setBirthAfterDays(rs.getInt("birth_after_days"));
                // start add 20130114 nakhoa 店舗情報登録
                if(rs.getString("area_id") == null){
                    this.setAreaId(null);
                }else{
                    this.setAreaId(rs.getInt("area_id"));
                }
                if(rs.getString("reservation_duplicate_warning_flag") == null){
                    this.setReservationDuplicateWarningFlag(null);
                }else{
                    this.setReservationDuplicateWarningFlag(rs.getInt("reservation_duplicate_warning_flag"));
                }
                if(rs.getString("display_reservation_rato_flag") == null){
                    this.setDisplayRreservationRatoFlag(null);
                }else{
                    this.setDisplayRreservationRatoFlag(rs.getInt("display_reservation_rato_flag"));
                }
                if(rs.getString("display_reaservation_rato_red") == null){
                    this.setDisplayReaservationRatoRed(null);
                }else{
                    this.setDisplayReaservationRatoRed(rs.getInt("display_reaservation_rato_red"));
                }
                if(rs.getString("display_reaservation_rato_yellow") == null){
                    this.setDisplayReaservationRatoYellow(null);
                }else{
                    this.setDisplayReaservationRatoYellow(rs.getInt("display_reaservation_rato_yellow"));
                }
                if(rs.getString("reservation_buffer_time") == null){
                    this.setReservationBufferTime(null);
                }else{
                    this.setReservationBufferTime(rs.getInt("reservation_buffer_time"));
                }
                if(rs.getString("recommendation_flag") == null){
                    this.setRecommendationFlag(null);
                }else{
                    this.setRecommendationFlag(rs.getInt("recommendation_flag"));
                }
                this.setTabIndex(rs.getInt("customer_tab_index"));
                
                // end add 20130114 nakhoa 店舗情報登録
                this.setBirthAfterDays(rs.getInt("birth_after_days"));
                // start add 20130802 nakhoa 役務機能使用有無の制御追加
                this.setCourseFlag(rs.getInt("course_flg"));
                // end add 20130802 nakhoa 役務機能使用有無の制御追加
                
                //vtbphuong start add 20140421 Request #22456
                this.setReservationOverbookingFlag(rs.getInt("reservation_overbooking_flag"));
                //vtbphuong end add 20140421 Request #22456
                
                //IVS_TTMLoan Start add 20140707 MASHU_店舗情報登録
                this.setUseShopCategory(rs.getInt("use_shop_category"));
                //IVS_TTMLoan End add 20140707 MASHU_店舗情報登録
	}
// 2017/01/04 顧客メモ ADD END
        
	/**
	 * 店舗マスタにデータを登録する。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean regist(ConnectionWrapper con) throws SQLException
	{
		if(isExists(con))
		{
			return	(con.executeUpdate(this.getUpdateSQL()) == 1);
		}
		else
		{
			if(con.executeUpdate(this.getInsertSQL()) != 1)
			{
				return	false;
			}
			
			this.setShopID(getMaxShopID(con));
			
			return	(MstAuthority.insertFromDefaultAuthority(con, groupID, groupID, shopID));
		}
	}
        /**
	 * 店舗マスタにデータを登録する。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean registNons(ConnectionWrapper con) throws SQLException
	{
		if(isExists(con))
		{
			return	(con.executeUpdate(this.getUpdateNonsSQL()) == 1);
		}
		else
		{
			if(con.executeUpdate(this.getInsertNonsSQL()) != 1)
			{
				return	false;
			}
			
			this.setShopID(getMaxShopID(con));
			
			return	(MstAuthority.insertFromDefaultAuthority(con, groupID, groupID, shopID));
		}
	}
	
	
	/**
	 * 店舗マスタからデータを削除する。（論理削除）
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 成功
	 */
	public boolean delete(ConnectionWrapper con) throws SQLException
	{
		if(isExists(con))
		{
			if(con.executeUpdate(this.getDeleteSQL()) == 1)
			{
				MstAuthority	ma	=	new MstAuthority();
				MstGroup		mg	=	new MstGroup();
				mg.setGroupID(this.getGroupID());
				ma.setGroup(mg);
				ma.setShop(this);
				
				if(ma.delete(con))
				{
					return	true;
				}
			}
		}
		
		return	false;
	}
	
	/**
	 * 店舗マスタにデータが存在するかチェックする。
	 * @param con ConnectionWrapper
	 * @throws java.sql.SQLException SQLException
	 * @return true - 存在する
	 */
	public boolean isExists(ConnectionWrapper con) throws SQLException
	{
		if(this.getShopID() == null || this.getShopID() < 0)	return	false;
		
		if(con == null)	return	false;
		
		ResultSetWrapper	rs	=	con.executeQuery(this.getSelectSQL());

		if(rs.next())	return	true;
		else	return	false;
	}
	
	/**
	 * Select文を取得する。
	 * @return Select文
	 */
	private String getSelectSQL()
	{
		return	"select *\n" +
				"from mst_shop\n" +
				"where	shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n" +
				"order by	shop_id\n";
	}
	
	/**
	 * Insert文を取得する。
	 * @return Insert文
	 */
	private String	getInsertSQL()
	{
		return	"insert into mst_shop\n" +
				"(shop_id, group_id, shop_name, postal_code,\n" +
				"address1, address2, address3, address4,\n" +
				"phone_number, fax_number, mail_address,\n" +
				"accounting_unit, rounding, cutoff_day,course_display_day, customer_tab_index,\n" +
				"open_hour, open_minute, close_hour, close_minute, term, bed, proportionally, display_proportionally, reservation_null_line, reservation_staff_shift, shift_gear_working, auto_number, \n" +
				"prefix_string, seq_length, birth_month_flag, birth_before_days, birth_after_days,course_flg,reservation_overbooking_flag ,\n" +
                                 //IVS_TTMLoan start add 20140707 Task #26453: [Product_P]店舗情報登録   
                                "use_shop_category,\n" +
				//IVS_TTMLoan end add 20140707 Task #26453: [Product_P]店舗情報登録
				"insert_date, update_date, delete_date)\n" +
				"select\n" +
				"coalesce(max(shop_id), 0) + 1,\n" +
				SQLUtil.convertForSQL(this.getGroupID()) + ",\n" +
				SQLUtil.convertForSQL(this.getShopName()) + ",\n" +
				SQLUtil.convertForSQL(this.getPostalCode()) + ",\n" +
				SQLUtil.convertForSQL(this.getAddress(0)) + ",\n" +
				SQLUtil.convertForSQL(this.getAddress(1)) + ",\n" +
				SQLUtil.convertForSQL(this.getAddress(2)) + ",\n" +
				SQLUtil.convertForSQL(this.getAddress(3)) + ",\n" +
				SQLUtil.convertForSQL(this.getPhoneNumber()) + ",\n" +
				SQLUtil.convertForSQL(this.getFaxNumber()) + ",\n" +
				SQLUtil.convertForSQL(this.getMailAddress()) + ",\n" +
				SQLUtil.convertForSQL(this.getAccountingUnit()) + ",\n" +
				SQLUtil.convertForSQL(this.getRounding()) + ",\n" +
				SQLUtil.convertForSQL(this.getCutoffDay()) + ",\n" +
                                //An start add 20130322 有効期限間近
                                SQLUtil.convertForSQL(this.getCourseDisplayDay()) + ",\n" +
                                SQLUtil.convertForSQL(this.getTabIndex()) + ",\n" +
                                //An end add 20130322 有効期限間近
				SQLUtil.convertForSQL(this.getOpenHour()) + ",\n" +
				SQLUtil.convertForSQL(this.getOpenMinute()) + ",\n" +
				SQLUtil.convertForSQL(this.getCloseHour()) + ",\n" +
				SQLUtil.convertForSQL(this.getCloseMinute()) + ",\n" +
				SQLUtil.convertForSQL(this.getTerm()) + ",\n" +
                        	SQLUtil.convertForSQL(this.getBed()) + ",\n" +
                        	SQLUtil.convertForSQL(this.getProportionally()) + ",\n" +
                        	SQLUtil.convertForSQL(this.getDisplayProportionally()) + ",\n" +
                        	SQLUtil.convertForSQL(this.getReservationNullLine()) + ",\n" +
                        	SQLUtil.convertForSQL(this.getReservationMenuUpdate()) + ",\n" +
                        	SQLUtil.convertForSQL(this.getReservationStaffShift()) + ",\n" +
                        	SQLUtil.convertForSQL(this.getReservationStaffHoliday()) + ",\n" +
                        	SQLUtil.convertForSQL(this.getShiftGearWorking()) + ",\n" +
                        	SQLUtil.convertForSQL(this.getDesignatedAssist()) + ",\n" +
                        	SQLUtil.convertForSQL(this.getAutoNumber()) + ",\n" +
				SQLUtil.convertForSQL(this.getPrefixString()) + ",\n" +
				SQLUtil.convertForSQL(this.getSeqLength()) + ",\n" +
                        	SQLUtil.convertForSQL(this.getBirthMonthFlag()) + ",\n" +
				SQLUtil.convertForSQL(this.getBirthBeforeDays()) + ",\n" +
				SQLUtil.convertForSQL(this.getBirthAfterDays()) + ",\n" +
                                // start add 20130802 nakhoa 役務機能使用有無の制御追加
                                SQLUtil.convertForSQL(this.getCourseFlag()) + ",\n" +
                                // start add 20130802 nakhoa 役務機能使用有無の制御追加
                                // vtbphuong start add 20140421 Request #22456
                                SQLUtil.convertForSQL(this.getReservationOverbookingFlag()) + ",\n" +
                                // vtbphuong end add 20140421 Request #22456
                                // IVS_TTMLoan start add 20140707 Task #26453: [Product_P]店舗情報登録
                                SQLUtil.convertForSQL(this.getUseShopCategory()) + ",\n" +
                                // IVS_TTMLoan end add 20140707 Task #26453: [Product_P]店舗情報登録
				"current_timestamp, current_timestamp, null\n" +
				"from mst_shop\n";
	}
            /**
         * Insert文を取得する。
         *
         * @return Insert文
         */
        private String getInsertNonsSQL() {
            return "insert into mst_shop\n"
                    + "(shop_id, group_id, shop_name, postal_code,\n"
                    + "address1, address2, address3, address4,\n"
                    + "phone_number, fax_number, mail_address,\n"
                    + "accounting_unit, rounding, cutoff_day,course_display_day,\n"
                    + "open_hour, open_minute, close_hour, close_minute, term, bed, proportionally, display_proportionally, reservation_null_line, reservation_staff_shift, shift_gear_working, auto_number, \n"
                    + "prefix_string, seq_length, birth_month_flag, birth_before_days, birth_after_days, \n"
                    + "insert_date, update_date, delete_date, display_switch_technic)\n"
                    + "values(\n"     
                    + SQLUtil.convertForSQL(this.getGroupID()) + ",\n"
                    + SQLUtil.convertForSQL(this.getShopName()) + ",\n"
                    + SQLUtil.convertForSQL(this.getPostalCode()) + ",\n"
                    + SQLUtil.convertForSQL(this.getAddress(0)) + ",\n"
                    + SQLUtil.convertForSQL(this.getAddress(1)) + ",\n"
                    + SQLUtil.convertForSQL(this.getAddress(2)) + ",\n"
                    + SQLUtil.convertForSQL(this.getAddress(3)) + ",\n"
                    + SQLUtil.convertForSQL(this.getPhoneNumber()) + ",\n"
                    + SQLUtil.convertForSQL(this.getFaxNumber()) + ",\n"
                    + SQLUtil.convertForSQL(this.getMailAddress()) + ",\n"
                    + SQLUtil.convertForSQL(this.getAccountingUnit()) + ",\n"
                    + SQLUtil.convertForSQL(this.getRounding()) + ",\n"
                    + SQLUtil.convertForSQL(this.getCutoffDay()) + ",\n"
                    //An start add 20130322 有効期限間近
                     + SQLUtil.convertForSQL(this.getCourseDisplayDay()) + ",\n"
                    //An end add 20130322 有効期限間近
                    + SQLUtil.convertForSQL(this.getOpenHour()) + ",\n"
                    + SQLUtil.convertForSQL(this.getOpenMinute()) + ",\n"
                    + SQLUtil.convertForSQL(this.getCloseHour()) + ",\n"
                    + SQLUtil.convertForSQL(this.getCloseMinute()) + ",\n"
                    + SQLUtil.convertForSQL(this.getTerm()) + ",\n"
                    + SQLUtil.convertForSQL(this.getBed()) + ",\n"
                    + SQLUtil.convertForSQL(this.getProportionally()) + ",\n"
                    + SQLUtil.convertForSQL(this.getDisplayProportionally()) + ",\n"
                    + SQLUtil.convertForSQL(this.getReservationNullLine()) + ",\n"
                    + SQLUtil.convertForSQL(this.getReservationMenuUpdate()) + ",\n"
                    + SQLUtil.convertForSQL(this.getReservationStaffShift()) + ",\n"
                    + SQLUtil.convertForSQL(this.getReservationStaffHoliday()) + ",\n"
                    + SQLUtil.convertForSQL(this.getShiftGearWorking()) + ",\n"
                    + SQLUtil.convertForSQL(this.getDesignatedAssist()) + ",\n"
                    + SQLUtil.convertForSQL(this.getAutoNumber()) + ",\n"
                    + SQLUtil.convertForSQL(this.getPrefixString()) + ",\n"
                    + SQLUtil.convertForSQL(this.getSeqLength()) + ",\n"
                    + SQLUtil.convertForSQL(this.getBirthMonthFlag()) + ",\n"
                    + SQLUtil.convertForSQL(this.getBirthBeforeDays()) + ",\n"
                    + "current_timestamp, current_timestamp, null)\n";

        }
	/**
	 * Update文を取得する。
	 * @return Update文
	 */
	private String	getUpdateSQL()
	{
		return	"update mst_shop\n" +
				"set\n" +
				"group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + ",\n" +
				"shop_name = " + SQLUtil.convertForSQL(this.getShopName()) + ",\n" +
				"postal_code = " + SQLUtil.convertForSQL(this.getPostalCode()) + ",\n" +
				"address1 = " + SQLUtil.convertForSQL(this.getAddress(0)) + ",\n" +
				"address2 = " + SQLUtil.convertForSQL(this.getAddress(1)) + ",\n" +
				"address3 = " + SQLUtil.convertForSQL(this.getAddress(2)) + ",\n" +
				"address4 = " + SQLUtil.convertForSQL(this.getAddress(3)) + ",\n" +
				"phone_number = " + SQLUtil.convertForSQL(this.getPhoneNumber()) + ",\n" +
				"fax_number = " + SQLUtil.convertForSQL(this.getFaxNumber()) + ",\n" +
				"mail_address = " + SQLUtil.convertForSQL(this.getMailAddress()) + ",\n" +
				"accounting_unit = " + SQLUtil.convertForSQL(this.getAccountingUnit()) + ",\n" +
				"rounding = " + SQLUtil.convertForSQL(this.getRounding()) + ",\n" +
				"cutoff_day = " + SQLUtil.convertForSQL(this.getCutoffDay()) + ",\n" +
                                //An start add 20130322 有効期限間近
                                "course_display_day = " + SQLUtil.convertForSQL(this.getCourseDisplayDay()) + ",\n" +
                                "customer_tab_index = " + SQLUtil.convertForSQL(this.getTabIndex()) + ",\n" +
                                //An end add 20130322 有効期限間近
                                
				"open_hour = " + SQLUtil.convertForSQL(this.getOpenHour()) + ",\n" +
				"open_minute = " + SQLUtil.convertForSQL(this.getOpenMinute()) + ",\n" +
				"close_hour = " + SQLUtil.convertForSQL(this.getCloseHour()) + ",\n" +
				"close_minute = " + SQLUtil.convertForSQL(this.getCloseMinute()) + ",\n" +
				"term = " + SQLUtil.convertForSQL(this.getTerm()) + ",\n" +
                                "bed = " + SQLUtil.convertForSQL(this.getBed()) + ",\n" +
                                "proportionally = " + SQLUtil.convertForSQL(this.getProportionally()) + ",\n" +
                                "display_proportionally = " + SQLUtil.convertForSQL(this.getDisplayProportionally()) + ",\n" +
                                "reservation_null_line = " + SQLUtil.convertForSQL(this.getReservationNullLine()) + ",\n" +
                                "reservation_menu_update = " + SQLUtil.convertForSQL(this.getReservationMenuUpdate()) + ",\n" +
                                "reservation_staff_shift = " + SQLUtil.convertForSQL(this.getReservationStaffShift()) + ",\n" +
                                "reservation_staff_holiday = " + SQLUtil.convertForSQL(this.getReservationStaffHoliday()) + ",\n" +
                                "shift_gear_working = " + SQLUtil.convertForSQL(this.getShiftGearWorking()) + ",\n" +
                                "designated_assist = " + SQLUtil.convertForSQL(this.getDesignatedAssist()) + ",\n" +
                                "auto_number = " + SQLUtil.convertForSQL(this.getAutoNumber()) + ",\n" +
				"prefix_string = " + SQLUtil.convertForSQL(this.getPrefixString()) + ",\n" +
				"seq_length = " + SQLUtil.convertForSQL(this.getSeqLength()) + ",\n" +
                                "birth_month_flag = " + SQLUtil.convertForSQL(this.getBirthMonthFlag()) + ",\n" +
				"birth_before_days = " + SQLUtil.convertForSQL(this.getBirthBeforeDays()) + ",\n" +
				"birth_after_days = " + SQLUtil.convertForSQL(this.getBirthAfterDays()) + ",\n" +
                                // start add 20130802 nakhoa 役務機能使用有無の制御追加
                                "course_flg = " + SQLUtil.convertForSQL(this.getCourseFlag()) + ",\n" +
                                // end add 20130802 nakhoa 役務機能使用有無の制御追加
                                //vtbphuong start add 20140421 Request #22456
                                " reservation_overbooking_flag = " + SQLUtil.convertForSQL(this.getReservationOverbookingFlag()) + ",\n" +
                                //vtbphuong end add 20140421 Request #22456
                                //IVS_TTMLoan start add 20140707 Task #26453: [Product_P]店舗情報登録
                                " use_shop_category = " + SQLUtil.convertForSQL(this.getUseShopCategory()) + ",\n" +
                                //IVS_TTMLoan end add 20140707 Task #26453: [Product_P]店舗情報登録
				"update_date = current_timestamp\n" +
				"where	shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n";
	}
        private String getUpdateNonsSQL() 
        {
                    return "update mst_shop\n"
                            + "set\n"
                            + "group_id = " + SQLUtil.convertForSQL(this.getGroupID()) + ",\n"
                            + "shop_name = " + SQLUtil.convertForSQL(this.getShopName()) + ",\n"
                            + "postal_code = " + SQLUtil.convertForSQL(this.getPostalCode()) + ",\n"
                            + "address1 = " + SQLUtil.convertForSQL(this.getAddress(0)) + ",\n"
                            + "address2 = " + SQLUtil.convertForSQL(this.getAddress(1)) + ",\n"
                            + "address3 = " + SQLUtil.convertForSQL(this.getAddress(2)) + ",\n"
                            + "address4 = " + SQLUtil.convertForSQL(this.getAddress(3)) + ",\n"
                            + "phone_number = " + SQLUtil.convertForSQL(this.getPhoneNumber()) + ",\n"
                            + "fax_number = " + SQLUtil.convertForSQL(this.getFaxNumber()) + ",\n"
                            + "mail_address = " + SQLUtil.convertForSQL(this.getMailAddress()) + ",\n"
                            + "accounting_unit = " + SQLUtil.convertForSQL(this.getAccountingUnit()) + ",\n"
                            + "rounding = " + SQLUtil.convertForSQL(this.getRounding()) + ",\n"
                            + "cutoff_day = " + SQLUtil.convertForSQL(this.getCutoffDay()) + ",\n"
                             //An start add 20130322 有効期限間近
                            + "course_display_day = " + SQLUtil.convertForSQL(this.getCourseDisplayDay()) + ",\n"
                             //An end add 20130322 有効期限間近
                            + "open_hour = " + SQLUtil.convertForSQL(this.getOpenHour()) + ",\n"
                            + "open_minute = " + SQLUtil.convertForSQL(this.getOpenMinute()) + ",\n"
                            + "close_hour = " + SQLUtil.convertForSQL(this.getCloseHour()) + ",\n"
                            + "close_minute = " + SQLUtil.convertForSQL(this.getCloseMinute()) + ",\n"
                            + "term = " + SQLUtil.convertForSQL(this.getTerm()) + ",\n"
                            + "bed = " + SQLUtil.convertForSQL(this.getBed()) + ",\n"
                            + "proportionally = " + SQLUtil.convertForSQL(this.getProportionally()) + ",\n"
                            + "display_proportionally = " + SQLUtil.convertForSQL(this.getDisplayProportionally()) + ",\n"
                            + "reservation_null_line = " + SQLUtil.convertForSQL(this.getReservationNullLine()) + ",\n"
                            + "reservation_menu_update = " + SQLUtil.convertForSQL(this.getReservationMenuUpdate()) + ",\n"
                            + "reservation_staff_shift = " + SQLUtil.convertForSQL(this.getReservationStaffShift()) + ",\n"
                            + "reservation_staff_holiday = " + SQLUtil.convertForSQL(this.getReservationStaffHoliday()) + ",\n"
                            + "shift_gear_working = " + SQLUtil.convertForSQL(this.getShiftGearWorking()) + ",\n"
                            + "designated_assist = " + SQLUtil.convertForSQL(this.getDesignatedAssist()) + ",\n"
                            + "auto_number = " + SQLUtil.convertForSQL(this.getAutoNumber()) + ",\n"
                            + "prefix_string = " + SQLUtil.convertForSQL(this.getPrefixString()) + ",\n"
                            + "seq_length = " + SQLUtil.convertForSQL(this.getSeqLength()) + ",\n"
                            + "birth_month_flag = " + SQLUtil.convertForSQL(this.getBirthMonthFlag()) + ",\n"
                            + "birth_before_days = " + SQLUtil.convertForSQL(this.getBirthBeforeDays()) + ",\n"
                            + "birth_after_days = " + SQLUtil.convertForSQL(this.getBirthAfterDays()) + ",\n"
                            // start add 20130122 nakhoa 店舗情報登録
                            + "reservation_duplicate_warning_flag = " + SQLUtil.convertForSQL(this.getReservationDuplicateWarningFlag()) + ",\n"
                            + "display_reservation_rato_flag = " + SQLUtil.convertForSQL(this.getDisplayRreservationRatoFlag()) + ",\n"
                            + "display_reaservation_rato_red = " + SQLUtil.convertForSQL(this.getDisplayReaservationRatoRed()) + ",\n"
                            + "display_reaservation_rato_yellow = " + SQLUtil.convertForSQL(this.getDisplayReaservationRatoYellow()) + ",\n"
                            + "reservation_buffer_time = " + SQLUtil.convertForSQL(this.getReservationBufferTime()) + ",\n"
                            + "area_id = " + SQLUtil.convertForSQL(this.getAreaId()) + ",\n"
                            + "recommendation_flag = " + SQLUtil.convertForSQL(this.getRecommendationFlag()) + ",\n"
                            // end add 20130122 nakhoa 店舗情報登録
                            + "update_date = current_timestamp\n"
                            + "where	shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n";
    }
	/**
	 * 削除用Update文を取得する。
	 * @return 削除用Update文
	 */
	private String	getDeleteSQL()
	{
		return	"update mst_shop\n" +
				"set\n" +
				"update_date = current_timestamp,\n" +
				"delete_date = current_timestamp\n" +
				"where	shop_id = " + SQLUtil.convertForSQL(this.getShopID()) + "\n";
	}
	
	/**
	 * 
	 * @param con 
	 * @throws java.sql.SQLException 
	 * @return 
	 */
	public static Integer getMaxShopID(ConnectionWrapper con) throws SQLException
	{
		Integer		result	=	null;
		
		if(con == null)	return	result;
		
		ResultSetWrapper	rs	=	con.executeQuery(getMaxShopIDSQL());

		if(rs.next())
		{
			result	=	rs.getInt("max_shop_id");
		}
		
		rs.close();
		
		return	result;
	}
	
	/**
	 * 
	 * @return 
	 */
	private static String getMaxShopIDSQL()
	{
		return	"select max(shop_id) as max_shop_id\n" +
				"from mst_shop";
	}

        public Calendar getSystemDate()
        {
            return getSystemDate(Calendar.getInstance().getTime());
        }

        public Calendar getSystemDate(java.util.Date date)
        {
            if (SystemInfo.isGroup()) return Calendar.getInstance();
            
            Calendar result = null;

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            
            Calendar open = (Calendar)cal.clone();
            open.set(Calendar.HOUR_OF_DAY, openHour);
            open.set(Calendar.MINUTE, openMinute);
            open.set(Calendar.SECOND, 0);
            open.set(Calendar.MILLISECOND, 0);
            
            if (cal.before(open)) {
                open.add(Calendar.DAY_OF_MONTH, -1);
            }

            Calendar close = (Calendar)open.clone();
            close.set(Calendar.HOUR_OF_DAY, closeHour);
            close.set(Calendar.MINUTE, closeMinute);

            result = cal.before(close) ? open : cal;
            result.set(Calendar.HOUR_OF_DAY, 0);
            result.set(Calendar.MINUTE, 0);
            result.set(Calendar.SECOND, 0);
            result.set(Calendar.MILLISECOND, 0);

            return result;
        }
    // IVS SANG START INSERT 20131017
    @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        } 
    // IVS SANG END INSERT 20131017
}