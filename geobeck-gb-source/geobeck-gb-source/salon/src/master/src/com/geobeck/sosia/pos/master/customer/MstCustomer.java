/*
 * MstCustomer.java
 *
 * Created on 2006/04/27, 10:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.master.customer;

import java.sql.*;
import java.util.*;
import javax.swing.*;

import com.ibm.icu.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.swing.*;
import java.util.logging.Level;

/**
 * 顧客マスタデータ
 *
 * @author katagiri
 */
public class MstCustomer {

    /**
     * 顧客ＩＤ
     */
    private Integer customerID = null;
    /**
     * 顧客番号
     */
    private String customerNo = null;
    /**
     * 店舗
     */
    private MstShop shop = new MstShop();
    /**
     * 顧客名
     */
    private String[] customerName = {"", ""};
    /**
     * 顧客フリガナ
     */
    private String[] customerKana = {"", ""};
    /**
     * 郵便番号
     */
    private String postalCode = "";
    /**
     * 住所
     */
    private String[] address = {"", "", "", ""};
    /**
     * 電話番号
     */
    private String phoneNumber = "";
    /**
     * 携帯番号
     */
    private String cellularNumber = "";
    /**
     * ＦＡＸ番号
     */
    private String faxNumber = "";
    /**
     * ＰＣメールアドレス
     */
    private String pcMailAddress = "";
    /**
     * 携帯メールアドレス
     */
    private String cellularMailAddress = "";
    /**
     * メール配信可否
     */
    private Integer sendMail = 1;
    /**
     * DM配信可否
     */
    private Integer sendDm = 1;
    /**
     * 電話連絡可否
     */
    private Integer callFlag = 1;
    /**
     * 性別
     */
    private Integer sex = null;
    /**
     * 誕生日
     */
    private JapaneseCalendar birthday = null;
    /**
     * 職業
     */
    private MstJob job = null;
    /**
     * 備考
     */
    private String note = "";
    /**
     * 導入前来店回数
     */
    private Integer beforeVisitNum = 0;
    /**
     * 紹介した人
     */
    private Integer introducerID = null;
    /**
     * 紹介元顧客メモ
     */
    private String introducerNote = null;
    /**
     * 初回来店動機
     */
    private MstFirstComingMotive firstComingMotive = null;
    /**
     * 初回来店動機メモ
     */
    private String firstComingMotiveNote = null;
    /**
     * SOSIA連動データ
     */
    private MstSosiaCustomer msc = new MstSosiaCustomer();
    /**
     * 訪問回数
     */
    protected Long visitCount = 0l;
    /**
     * 初回来店日
     */
    private java.util.Calendar firstVisitday = null;
    /**
     * ランク
     */
    private String rank = null;
    // start add 20130117 nakhoa お会計
    /**
     * アラート
     */
    private String alertMark = null;
    // end add 20130117 nakhoa お会計
    private Integer mainStaffId = null;
    /**
     * スタッフ
     */
    protected MstStaff staff = new MstStaff();
    /**
     * 家族
     */
    private ArrayList<MstCustomer> familyList = new ArrayList<MstCustomer>();
    private boolean deleted = false;
    private boolean familyMain = false;
    //An start add 20130125
    private String question_1 = "";
    private String question_2 = "";
    private Integer creditLock = null;
    // An end add 20130125
    //Start add 20131014 lvut HPB POS
    private String sbCustomerId = "";
    //End add 20131014 lvut HPB POS
    //IVS_LVTu start add 2016/03/10 mmd.getSosiaID()
    private String              fbID        = null;

    //Luc start add 20160316 #49043
    
    //次回予日
    private String nextVisitDate;

    //次回予約時
    private String nextVisitTime;
    
    //Luc end add 20160316 #49043
    public String getFbID() {
        return fbID;
    }

    public void setFbID(String fbID) {
        this.fbID = fbID;
    }
    //Luc start add 20160316 #49043
    public String getNextVisitTime() {
        return nextVisitTime;
    }

    public void setNextVisitTime(String nextVisitTime) {
        this.nextVisitTime = nextVisitTime;
    }
    
    public String getNextVisitDate() {
        return nextVisitDate;
    }

    public void setNextVisitDate(String nextVisitDate) {
        this.nextVisitDate = nextVisitDate;
    }
    //Luc end add 20160316 #49043
    //IVS_LVTu end add 2016/03/10 mmd.getSosiaID()
    /**
     * Creates a new instance of MstCustomer
     */
    public MstCustomer() {
    }

    public MstCustomer(MstCustomer mc) {
        this.setData(mc);
    }

    /**
     * コンストラクタ
     *
     * @param customerID 顧客ＩＤ
     */
    public MstCustomer(Integer customerID) {
        this.setCustomerID(customerID);
    }

    public String toString() {
        String result = this.getFullCustomerName();

        if (this.getRank() != null) {
            result = "[" + this.getRank() + "] " + result;
        }

        return result;
    }

    /**
     * 顧客ＩＤを取得する。
     *
     * @return 顧客ＩＤ
     */
    public Integer getCustomerID() {
        return customerID;
    }

    /**
     * 顧客ＩＤをセットする。
     *
     * @param customerID 顧客ＩＤ
     */
    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }
    //Thanh add start 2013/06/27

    public Integer getMainStaffId() {
        return mainStaffId;
    }

    /**
     * 顧客ＩＤをセットする。
     *
     * @param customerID 顧客ＩＤ
     */
    public void setMainStaffId(Integer mainStaffId) {
        this.mainStaffId = mainStaffId;
    }

    /**
     * スタッフ
     *
     * @return スタッフ
     */
    public MstStaff getStaff() {
        return staff;
    }

    /**
     * スタッフ
     *
     * @param staff スタッフ
     */
    public void setStaff(MstStaff staff) {
        this.staff = staff;
    }
    //Thanh add start 2013/06/27

    /**
     * 店舗を取得する。
     *
     * @return 店舗
     */
    public MstShop getShop() {
        return shop;
    }

    /**
     * 店舗をセットする。
     *
     * @param shop 店舗
     */
    public void setShop(MstShop shop) {
        this.shop = shop;
    }

    /**
     * 顧客名を取得する。
     *
     * @return 顧客名
     */
    public String[] getCustomerName() {
        return customerName;
    }

    /**
     * 顧客名を取得する。
     *
     * @param index インデックス
     * @return 顧客名
     */
    public String getCustomerName(int index) {
        return customerName[index];
    }

    /**
     * 顧客名をセットする。
     *
     * @param customerName 顧客名
     */
    public void setCustomerName(String[] customerName) {
        this.customerName = customerName;
    }

    /**
     * 顧客名をセットする。
     *
     * @param index インデックス
     * @param customerName 顧客名
     */
    public void setCustomerName(int index, String customerName) {
        this.customerName[index] = customerName;
    }

    /**
     * 顧客のフルネームを取得する。
     *
     * @return フルネーム
     */
    public String getFullCustomerName() {
        return (customerName[0] == null ? "" : customerName[0])
                + (customerName[1] == null || customerName[1].equals("") ? "" : "　" + customerName[1]);
    }

    /**
     * 顧客フリガナを取得する。
     *
     * @return 顧客フリガナ
     */
    public String[] getCustomerKana() {
        return customerKana;
    }

    /**
     * 顧客フリガナを取得する。
     *
     * @param index インデックス
     * @return 顧客フリガナ
     */
    public String getCustomerKana(int index) {
        return customerKana[index];
    }

    /**
     * 顧客フリガナをセットする。
     *
     * @param customerKana 顧客フリガナ
     */
    public void setCustomerKana(String[] customerKana) {
        this.customerKana = customerKana;
    }

    /**
     * 顧客フリガナをセットする。
     *
     * @param index インデックス
     * @param customerKana 顧客フリガナ
     */
    public void setCustomerKana(int index, String customerKana) {
        this.customerKana[index] = customerKana;
    }

    /**
     * 顧客のフリガナのフルネームを取得する。
     *
     * @return フリガナのフルネーム
     */
    public String getFullCustomerKana() {
        return (customerKana[0] == null ? "" : customerKana[0])
                + (customerKana[1] == null || customerKana[1].equals("") ? "" : "　" + customerKana[1]);
    }

    /**
     * 郵便番号を取得する。
     *
     * @return 郵便番号
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * 郵便番号をセットする。
     *
     * @param postalCode 郵便番号
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = null;

        if (postalCode != null) {
            if (postalCode.matches("[0-9]{7}")) {
                this.postalCode = postalCode;
            }
        }
    }

    /**
     * 郵便番号を取得する。
     *
     * @return 郵便番号
     */
    public String getFormatedPostalCode() {
        if (postalCode == null || postalCode.equals("")) {
            return "";
        } else {
            return (postalCode.length() < 4 ? postalCode
                    : postalCode.subSequence(0, 3) + "-" + postalCode.substring(3, 7));
        }
    }

    /**
     * 住所を取得する。
     *
     * @return 住所
     */
    public String[] getAddress() {
        return address;
    }

    /**
     * 住所を取得する。
     *
     * @param index インデックス
     * @return 住所
     */
    public String getAddress(int index) {
        return address[index] == null ? "" : address[index];
    }

    /**
     * 住所をセットする。
     *
     * @param address 住所
     */
    public void setAddress(String[] address) {
        this.address = address;
    }

    /**
     * 住所をセットする。
     *
     * @param index インデックス
     * @param address 住所
     */
    public void setAddress(int index, String address) {
        this.address[index] = address != null ? address : "";
    }

    public String getFullAddress() {
        return (address[0] == null ? "" : address[0])
                + (address[1] == null ? "" : address[1])
                + (address[2] == null ? "" : address[2])
                + (address[3] == null ? "" : address[3]);
    }

    /**
     * 電話番号を取得する。
     *
     * @return 電話番号
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * 電話番号をセットする。
     *
     * @param phoneNumber 電話番号
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * 携帯番号を取得する。
     *
     * @return 携帯番号
     */
    public String getCellularNumber() {
        return cellularNumber;
    }

    /**
     * 携帯番号をセットする。
     *
     * @param cellularNumber 携帯番号
     */
    public void setCellularNumber(String cellularNumber) {
        this.cellularNumber = cellularNumber;
    }

    /**
     * ＦＡＸ番号を取得する。
     *
     * @return ＦＡＸ番号
     */
    public String getFaxNumber() {
        return faxNumber;
    }

    /**
     * ＦＡＸ番号をセットする。
     *
     * @param faxNumber ＦＡＸ番号
     */
    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    /**
     * ＰＣメールアドレスを取得する。
     *
     * @return ＰＣメールアドレス
     */
    public String getPCMailAddress() {
        return pcMailAddress;
    }

    /**
     * ＰＣメールアドレスをセットする。
     *
     * @param pcMailAddress ＰＣメールアドレス
     */
    public void setPCMailAddress(String pcMailAddress) {
        if (pcMailAddress == null) {
            this.pcMailAddress = "";
        } else {
            this.pcMailAddress = pcMailAddress;
        }

    }

    /**
     * 携帯メールアドレスを取得する。
     *
     * @return 携帯メールアドレス
     */
    public String getCellularMailAddress() {
        return cellularMailAddress;
    }

    /**
     * 携帯メールアドレスをセットする。
     *
     * @param cellularMailAddress 携帯メールアドレス
     */
    public void setCellularMailAddress(String cellularMailAddress) {
        if (cellularMailAddress == null) {
            this.cellularMailAddress = "";
        } else {
            this.cellularMailAddress = cellularMailAddress;
        }
    }

    public boolean isInputMailAddress() {
        if (pcMailAddress == null || cellularMailAddress == null) {
            return false;
        } else {
            return !this.pcMailAddress.equals("") || !this.cellularMailAddress.equals("");
        }
    }

    /**
     * メール配信可否を取得する。
     *
     * @return メール配信可否
     */
    public Integer getSendMail() {
        return sendMail;
    }

    /**
     * メール配信可否をセットする。
     *
     * @param sendMail メール配信可否
     */
    public void setSendMail(Integer sendMail) {
        this.sendMail = sendMail;
    }

    public String getSendMailString() {
        if (this.getSendMail() == null) {
            return "";
        }

        switch (this.getSendMail()) {
            case 0:
                return "不可";
            case 1:
                return "可";
        }

        return "";
    }

    /**
     * DM配信可否を取得する。
     *
     * @return DM配信可否
     */
    public Integer getSendDm() {
        return sendDm;
    }

    /**
     * DM配信可否をセットする。
     *
     * @param sendDm DM配信可否
     */
    public void setSendDm(Integer sendDm) {
        this.sendDm = sendDm;
    }

    public String getSendDmString() {
        if (this.getSendDm() == null) {
            return "";
        }

        switch (this.getSendDm()) {
            case 0:
                return "不可";
            case 1:
                return "可";
        }

        return "";
    }

    /**
     * 電話連絡可否を取得する。
     *
     * @return 電話連絡可否
     */
    public Integer getCallFlag() {
        return callFlag;
    }

    /**
     * 電話連絡可否をセットする。
     *
     * @param callFlag 電話連絡可否
     */
    public void setCallFlag(Integer callFlag) {
        this.callFlag = callFlag;
    }

    public String getCallFlagString() {
        if (this.getCallFlag() == null) {
            return "";
        }

        switch (this.getCallFlag()) {
            case 0:
                return "不可";
            case 1:
                return "可";
        }

        return "";
    }

    /**
     * 性別を取得する。
     *
     * @return 性別
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * 性別をセットする。
     *
     * @param sex 性別
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getSexString() {
        if (this.getSex() == null) {
            return "";
        }

        switch (this.getSex()) {
            case 1:
                return "男";
            case 2:
                return "女";
        }

        return "";
    }

    /**
     * 誕生日を取得する。
     *
     * @return 誕生日
     */
    public JapaneseCalendar getBirthday() {
        return birthday;
    }

    /**
     * 誕生日をセットする。
     *
     * @param birthday 誕生日
     */
    public void setBirthday(JapaneseCalendar birthday) {
        this.birthday = birthday;
    }

    /**
     * 誕生日をセットする。
     *
     * @param birthday 誕生日
     */
    public void setBirthdayDate(java.util.Date birthday) {
        if (birthday == null) {
            this.birthday = null;
        } else {
            if (this.birthday == null) {
                this.birthday = new JapaneseCalendar();
            }
            this.birthday.setTime(birthday);
        }
    }

    /**
     * 誕生年を取得する。
     *
     * @return 誕生年
     */
    public Integer getBirthYear() {
        if (birthday == null) {
            return null;
        } else {
            return birthday.get(birthday.EXTENDED_YEAR);
        }
    }

    /**
     * 誕生月を取得する。
     *
     * @return 誕生月
     */
    public Integer getBirthMonth() {
        if (birthday == null) {
            return null;
        } else {
            return birthday.get(birthday.MONTH) + 1;
        }
    }

    /**
     * 誕生日を取得する。
     *
     * @return 誕生日
     */
    public Integer getBirthDay() {
        if (birthday == null) {
            return null;
        } else {
            return birthday.get(birthday.DAY_OF_MONTH);
        }
    }

    /**
     * 誕生日の文字列を取得する。
     *
     * @return 誕生日の文字列
     */
    public String getBirthdayString() {
        if (birthday == null) {
            return null;
        } else {
            if (1800 < birthday.get(JapaneseCalendar.EXTENDED_YEAR)) {
                return DateUtil.format(birthday.getTime(), "yyyy/MM/dd");
            } else {
                return DateUtil.format(birthday.getTime(), "MM/dd");
            }
        }
    }

    /**
     * 誕生日の文字列を取得する。
     *
     * @param pattern 日付のパターン
     * @return 誕生日の文字列
     */
    public String getBirthdayString(String pattern) {
        if (birthday == null) {
            return null;
        } else {
            return DateUtil.format(birthday.getTime(), pattern);
        }
    }

    //IVS_LVTu start add 2015/11/13 New request #44270
    /**
     * 誕生日の文字列を取得する。
     *
     * @return 誕生日の文字列
     */
    public String getBirthdayString2() {
        if (birthday == null) {
            return null;
        } else {
            if (1900 < birthday.get(JapaneseCalendar.EXTENDED_YEAR)) {
                return DateUtil.format(birthday.getTime(), "yyyy年M月d日");
            } else {
                return DateUtil.format(birthday.getTime(), "M月d日");
            }
        }
    }
    //IVS_LVTu end add 2015/11/13 New request #44270
    /**
     * 職業を取得する。
     *
     * @return 職業
     */
    public MstJob getJob() {
        return job;
    }

    /**
     * 職業をセットする。
     *
     * @param job 職業
     */
    public void setJob(MstJob job) {
        this.job = job;
    }

    /**
     * 職業ＩＤを取得する。
     *
     * @return 職業ＩＤ
     */
    public Integer getJobID() {
        if (job == null) {
            return null;
        } else {
            return job.getJobID();
        }
    }

    /**
     * 職業ＩＤをセットする。
     *
     * @param jobID 職業ＩＤ
     */
    public void setJobID(Integer jobID) {
        if (job == null) {
            job = new MstJob();
        }
        this.job.setJobID(jobID);
    }

    public String getJobName() {
        if (job == null) {
            return "";
        } else {
            return job.getJobName();
        }
    }

    /**
     * 備考を取得する。
     *
     * @return 備考
     */
    public String getNote() {
        return note;
    }

    /**
     * 備考をセットする。
     *
     * @param note 備考
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * 導入前来店回数を取得する。
     *
     * @return 導入前来店回数
     */
    public Integer getBeforeVisitNum() {
        return this.beforeVisitNum;
    }

    /**
     * 導入前来店回数をセットする。
     *
     * @param beforeVisitNum 導入前来店回数
     */
    public void setBeforeVisitNum(Integer beforeVisitNum) {
        this.beforeVisitNum = beforeVisitNum;
    }

    /**
     * 紹介者IDを取得する。
     *
     * @return 紹介者ID
     */
    public Integer getIntroducerID() {
        return introducerID;
    }

    /**
     * 紹介者IDをセットする。
     *
     * @param note 紹介者ID
     */
    public void setIntroducerID(Integer introducerID) {
        this.introducerID = introducerID;
    }

    /**
     * 紹介元顧客メモを取得する。
     *
     * @return 紹介元顧客メモ
     */
    public String getIntroducerNote() {
        return introducerNote;
    }

    /**
     * 紹介元顧客メモをセットする。
     *
     * @param introducerNote 紹介元顧客メモ
     */
    public void setIntroducerNote(String introducerNote) {
        this.introducerNote = introducerNote;
    }

    /**
     * 初回来店動機を取得する。
     *
     * @return 初回来店動機
     */
    public MstFirstComingMotive getFirstComingMotive() {
        return firstComingMotive;
    }

    /**
     * 初回来店動機をセットする。
     *
     * @param firstComingMotive 初回来店動機
     */
    public void setFirstComingMotive(MstFirstComingMotive firstComingMotive) {
        this.firstComingMotive = firstComingMotive;
    }

    /**
     * 初回来店動機種別ＩＤを取得する。
     *
     * @return 初回来店動機種別ＩＤ
     */
    public Integer getFirstComingMotiveClassId() {
        if (firstComingMotive == null) {
            return null;
        } else {
            return firstComingMotive.getFirstComingMotiveClassId();
        }
    }

    /**
     * 初回来店動機種別ＩＤをセットする。
     *
     * @param firstComingMotiveClassId 初回来店動機種別ＩＤ
     */
    public void setFirstComingMotiveClassId(Integer firstComingMotiveClassId) {
        if (firstComingMotive == null) {
            firstComingMotive = new MstFirstComingMotive();
        }
        this.firstComingMotive.setFirstComingMotiveClassId(firstComingMotiveClassId);
    }

    /**
     * 初回来店動機種別名を取得する。
     *
     * @return 初回来店動機種別名
     */
    public String getFirstComingMotiveName() {
        if (firstComingMotive == null) {
            return "";
        } else {
            return firstComingMotive.getFirstComingMotiveName();
        }
    }

    /**
     * 初回来店動機メモを取得する。
     *
     * @return 初回来店動機メモ
     */
    public String getFirstComingMotiveNote() {
        return firstComingMotiveNote;
    }

    /**
     * 初回来店動機メモをセットする。
     *
     * @param firstComingMotiveNote 紹介元顧客メモ
     */
    public void setFirstComingMotiveNote(String firstComingMotiveNote) {
        this.firstComingMotiveNote = firstComingMotiveNote;
    }

    /**
     * 訪問回数をセットする
     *
     * @param count 訪問回数
     */
    public void setVisitCount(Long count) {
        visitCount = count;
    }

    /**
     * 訪問回数を取得する
     *
     * @return 訪問回数
     */
    public Long getVisitCount() {
        return visitCount;
    }

    /**
     * 初回来店日を取得する。
     *
     * @return 初回来店日
     */
    public java.util.Calendar getFirstVisitDate() {
        return firstVisitday;
    }

    /**
     * 初回来店日をセットする。
     *
     * @param firstVisitday 初回来店日
     */
    public void setFirstVisitDay(java.util.Calendar firstVisitday) {
        this.firstVisitday = firstVisitday;
    }

    /**
     * 初回来店日をセットする。
     *
     * @param firstVisitday 初回来店日
     */
    public void setFirstVisitDate(java.util.Date firstVisitDay) {
        if (firstVisitDay == null) {
            this.firstVisitday = null;
        } else {
            if (this.firstVisitday == null) {
                this.firstVisitday = java.util.Calendar.getInstance();
            }
            this.firstVisitday.setTime(firstVisitDay);
        }
    }

    /**
     * 初回来店年を取得する。
     *
     * @return 初回来店年
     */
    public Integer getFirstVisitYear() {
        if (firstVisitday == null) {
            return null;
        } else {
            return firstVisitday.get(java.util.Calendar.YEAR);
        }
    }

    /**
     * 初回来店月を取得する。
     *
     * @return 初回来店月
     */
    public Integer getFirstVisitMonth() {
        if (firstVisitday == null) {
            return null;
        } else {
            return firstVisitday.get(firstVisitday.MONTH) + 1;
        }
    }

    /**
     * 初回来店日を取得する。
     *
     * @return 初回来店日
     */
    public Integer getFirstVisitDay() {
        if (firstVisitday == null) {
            return null;
        } else {
            return firstVisitday.get(firstVisitday.DAY_OF_MONTH);
        }
    }

    /**
     * 初回来店日の文字列を取得する。
     *
     * @return 初回来店日の文字列
     */
    public String getFirstVisitdayString() {
        if (firstVisitday == null) {
            return null;
        } else {
            return DateUtil.format(firstVisitday.getTime(), "yyyy/MM/dd");
        }
    }

    /**
     * 初回来店日の文字列を取得する。
     *
     * @param pattern 日付のパターン
     * @return 初回来店日の文字列
     */
    public String getFirstVisitdayString(String pattern) {
        if (firstVisitday == null) {
            return null;
        } else {
            return DateUtil.format(firstVisitday.getTime(), pattern);
        }
    }

    // start add 20131014 lvut HPB POS
    public String getSbCustomerId() {
        return sbCustomerId;
    }

    public void setSbCustomerId(String sbCustomerId) {
        this.sbCustomerId = sbCustomerId;
    }
    // End add 20131014 lvut HPB POS

    /**
     * SOSIA連動データを取得する。
     *
     * @return SOSIA連動データ
     */
    public MstSosiaCustomer getSosiaCustomer() {
        return msc;
    }

    /**
     * SOSIA連動データをセットする。
     *
     * @param msc SOSIA連動データ
     */
    public void setSosiaCustomer(MstSosiaCustomer msc) {
        this.msc = msc;
    }

    public void setData(MstCustomer customer) {
        this.setCustomerID(customer.getCustomerID());
        this.setCustomerNo(customer.getCustomerNo());
        this.setShop(customer.getShop());
        this.setCustomerName(0, customer.getCustomerName(0));
        this.setCustomerName(1, customer.getCustomerName(1));
        this.setCustomerKana(0, customer.getCustomerKana(0));
        this.setCustomerKana(1, customer.getCustomerKana(1));
        this.setPostalCode(customer.getPostalCode());
        this.setAddress(0, customer.getAddress(0));
        this.setAddress(1, customer.getAddress(1));
        this.setAddress(2, customer.getAddress(2));
        this.setAddress(3, customer.getAddress(3));
        this.setPhoneNumber(customer.getPhoneNumber());
        this.setCellularNumber(customer.getCellularNumber());
        this.setFaxNumber(customer.getFaxNumber());
        this.setPCMailAddress(customer.getPCMailAddress());
        this.setCellularMailAddress(customer.getCellularMailAddress());
        this.setSendMail(customer.getSendMail());
        this.setSendDm(customer.getSendDm());
        this.setCallFlag(customer.getCallFlag());
        this.setSex(customer.getSex());
        this.setBirthday(customer.getBirthday());
        this.setJob(customer.getJob());
        this.setNote(customer.getNote());
        this.setSosiaCustomer(customer.getSosiaCustomer());
        this.setIntroducerID(customer.getIntroducerID());
        this.setIntroducerNote(customer.getIntroducerNote());
        this.setFirstComingMotive(customer.getFirstComingMotive());
        this.setFirstComingMotiveNote(customer.getFirstComingMotiveNote());
        this.setBeforeVisitNum(customer.getBeforeVisitNum());
        this.setFirstVisitDay(customer.getFirstVisitDate());

        // IVS SANG START INSERT 20131102 [gbソース]ソースマージ
        // start add 20130117 nakhoa お会計
        this.setAlertMark(customer.getAlertMark());
        // start add 20130117 nakhoa お会計
        //An start add 20130125
        this.setReservationBufferTime(customer.getReservationBufferTime());
        this.setWebReservationFlag(customer.getWebReservationFlag());
        this.setQuestion_1(customer.getQuestion_1());
        this.setQuestion_2(customer.getQuestion_2());
        this.setSelectedCreditLock(customer.getSelectedCreditLock());
        //An end add 20130125
        // IVS SANG END INSERT 20131102 [gbソース]ソースマージ
    }

    /**
     * データをクリアする。
     */
    public void clear() {
        this.setCustomerID(null);
        this.setCustomerNo("");
        this.setShop(new MstShop());
        this.setCustomerName(0, "");
        this.setCustomerName(1, "");
        this.setCustomerKana(0, "");
        this.setCustomerKana(1, "");
        this.setPostalCode("");
        this.setAddress(0, "");
        this.setAddress(1, "");
        this.setAddress(2, "");
        this.setAddress(3, "");
        this.setPhoneNumber("");
        this.setCellularNumber("");
        this.setSendMail(null);
        this.setSendDm(null);
        this.setCallFlag(null);
        this.setFaxNumber("");
        this.setPCMailAddress("");
        this.setCellularMailAddress("");
        this.setSex(null);
        this.setBirthday(null);
        this.setJob(null);
        this.setNote("");
        this.setSosiaCustomer(null);
        this.setIntroducerNote("");
        this.setFirstComingMotive(null);
        this.setFirstComingMotiveNote("");
        this.setBeforeVisitNum(null);
        this.setFirstVisitDay(null);
    }

    /**
     * スタッフマスタから、設定されているスタッフIDのデータを読み込む。
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 成功
     */
    public boolean load(ConnectionWrapper con) throws SQLException {
        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        if (rs.next()) {
            this.setData(rs);
            if (SystemInfo.getDatabase().startsWith("pos_hair_rizap") || SystemInfo.getDatabase().startsWith("pos_hair_rizap_bak")) {
                this.setMainStaffId(rs.getInt("main_staff_id"));
                MstStaff msStaff = new MstStaff();
                msStaff.setStaffID(rs.getInt("staff_id"));
                msStaff.setStaffNo(rs.getString("staff_no"));
                this.setStaff(msStaff);
            }
        }

        return true;
    }

    public boolean loadForPrepaid(ConnectionWrapper con) throws SQLException {
        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getSelectForPrepaidSQL());

        if (rs.next()) {
            this.setDataForPrepaid(rs);
            if (SystemInfo.getDatabase().startsWith("pos_hair_rizap") || SystemInfo.getDatabase().startsWith("pos_hair_rizap_bak")) {
                this.setMainStaffId(rs.getInt("main_staff_id"));
                MstStaff msStaff = new MstStaff();
                msStaff.setStaffID(rs.getInt("staff_id"));
                msStaff.setStaffNo(rs.getString("staff_no"));
                this.setStaff(msStaff);
            }
        }

        return true;
    }
    
     /**
     * Select文を取得する。
     *
     * @return Select文
     */
    private String getSelectForPrepaidSQL() {
        String sql = "";
        if (SystemInfo.getDatabase().startsWith("pos_hair_rizap") || SystemInfo.getDatabase().startsWith("pos_hair_rizap_bak")) {
            sql += "select mc.*,ms.staff_no,ms.staff_id,\n";
            sql += " (select \n";                  
            sql +=" to_char(drd.reservation_datetime,'yyyy/mm/dd') as reservation_datetime\n";                 
            sql +=" from                     data_reservation dr \n";                        
            sql +=" join data_reservation_detail drd using(shop_id, reservation_no)\n";                 
            sql +=" where  dr.delete_date is null\n";                     
            sql +=" and drd.delete_date is null\n";                        
            sql +=" and drd.reservation_datetime >= now()\n";   
            if(SystemInfo.getCurrentShop().getShopID()!=0) {       
                sql +=" and dr.shop_id in("+SystemInfo.getCurrentShop().getShopID()+")\n";
            }
            sql +=" and dr.customer_id = mc.customer_id\n";      
            sql +=" group by dr.customer_id,drd.reservation_datetime";
            sql +="  order by drd.reservation_datetime asc";
            sql +="  limit 1";
            sql +="  ) as next_date,\n";   
            sql +=" (select\n" ;                       
            sql +="   to_char(drd.reservation_datetime,'HH:mm') as reservation_datetime\n";                    
            sql +=" from                     data_reservation dr\n";                            
            sql +=" join data_reservation_detail drd using(shop_id, reservation_no)\n" ;                   
            sql +=" where  dr.delete_date is null\n";                        
            sql +=" and drd.delete_date is null\n" ;                       
            sql +=" and drd.reservation_datetime >= now()\n";                   
           if(SystemInfo.getCurrentShop().getShopID()!=0) {       
                sql +=" and dr.shop_id in("+SystemInfo.getCurrentShop().getShopID()+")\n";
            }                  
            sql +=" and dr.customer_id = mc.customer_id\n";      
            sql +=" group by dr.customer_id,drd.reservation_datetime";
            sql +="  order by drd.reservation_datetime asc";
            sql +="  limit 1";
            sql +="  ) as next_time ";
            sql += "from mst_customer mc \n";   
            sql +="left join mst_staff ms on mc.main_staff_id = ms.staff_id \n";   
            sql +="where	customer_id = " + SQLUtil.convertForSQL(this.getCustomerID()) + "\n";
        } else {
            sql += " select *,\n";
            sql += " (select \n";                  
            sql +=" to_char(drd.reservation_datetime,'yyyy/mm/dd') as reservation_datetime\n";                 
            sql +=" from                     data_reservation dr \n";                        
            sql +=" join data_reservation_detail drd using(shop_id, reservation_no)\n";                 
            sql +=" where  dr.delete_date is null\n";                     
            sql +=" and drd.delete_date is null\n";                        
            sql +=" and drd.reservation_datetime >= now()\n";   
            if(SystemInfo.getCurrentShop().getShopID()!=0) {       
                sql +=" and dr.shop_id in("+SystemInfo.getCurrentShop().getShopID()+")\n";
            }
            sql +=" and dr.customer_id = mc.customer_id\n";      
            sql +=" group by dr.customer_id,drd.reservation_datetime";
            sql +="  order by drd.reservation_datetime asc";
            sql +="  limit 1";
            sql +="  ) as next_date,\n";   
            sql +=" (select\n" ;                       
            sql +="   to_char(drd.reservation_datetime,'HH24:MI') as reservation_datetime\n";                    
            sql +=" from                     data_reservation dr\n";                            
            sql +=" join data_reservation_detail drd using(shop_id, reservation_no)\n" ;                   
            sql +=" where  dr.delete_date is null\n";                        
            sql +=" and drd.delete_date is null\n" ;                       
            sql +=" and drd.reservation_datetime >= now()\n";                   
           if(SystemInfo.getCurrentShop().getShopID()!=0) {       
                sql +=" and dr.shop_id in("+SystemInfo.getCurrentShop().getShopID()+")\n";
            }                  
            sql +=" and dr.customer_id = mc.customer_id\n";      
            sql +=" group by dr.customer_id,drd.reservation_datetime";
            sql +="  order by drd.reservation_datetime asc";
            sql +="  limit 1";
            sql +="  ) as next_time ";
            sql +=" from mst_customer mc \n";
            sql +=" where	customer_id = " + SQLUtil.convertForSQL(this.getCustomerID()) + "\n";
        }
        return sql;

    }
    public boolean loadByCustomerNo(ConnectionWrapper con) throws SQLException {
        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getByCustomerNoSQL());
        if (rs.next()) {
            this.setData(rs);
        }

        return true;
    }
    //Start add 20131014 lvut HPB POS

    public boolean loadBySbCustomerId(ConnectionWrapper con) throws SQLException {
        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getBySbCustomerIdSQL());
        if (rs.next()) {
            this.setData(rs);
        }
        return true;
    }

    //End add 20131014 lvut HPB POS
    public String getByCustomerNoSQL() {
        return "select *\n"
                + "from mst_customer mc\n"
                + "where "
                + "delete_date is null\n"
                + "and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + " and customer_no = " + SQLUtil.convertForSQL(this.getCustomerNo()) + "\n"
                + "order by mc.customer_id\n";

    }

    //Start add 20131014 lvut HPB POS
    public String getBySbCustomerIdSQL() {
        return "select * \n"
                + "from mst_customer mc\n"
                + "where "
                + "delete_date is null\n"
                + " and sb_customer_id = " + SQLUtil.convertForSQL(this.getSbCustomerId()) + "\n"
                + "order by mc.customer_id\n";

    }
    //End add 20131014 lvut HPB POS

    /**
     * ResultSetWrapperからデータを読み込む。
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        int tmpValue = 0;

        this.setCustomerID(rs.getInt("customer_id"));
        this.setCustomerNo(rs.getString("customer_no"));
        MstShop ms = new MstShop();
        ms.setShopID(rs.getInt("shop_id"));
        this.setShop(ms);
        this.setCustomerName(0, rs.getString("customer_name1"));
        this.setCustomerName(1, rs.getString("customer_name2"));
        this.setCustomerKana(0, rs.getString("customer_kana1"));
        this.setCustomerKana(1, rs.getString("customer_kana2"));
        this.setPostalCode(rs.getString("postal_code"));
        this.setAddress(0, rs.getString("address1"));
        this.setAddress(1, rs.getString("address2"));
        this.setAddress(2, rs.getString("address3"));
        this.setAddress(3, rs.getString("address4"));
        this.setPhoneNumber(rs.getString("phone_number"));
        this.setCellularNumber(rs.getString("cellular_number"));
        this.setFaxNumber(rs.getString("fax_number"));
        this.setPCMailAddress(rs.getString("pc_mail_address"));
        this.setCellularMailAddress(rs.getString("cellular_mail_address"));
        this.setSendMail(rs.getInt("send_mail"));
        this.setSendDm(rs.getInt("send_dm"));
        this.setCallFlag(rs.getInt("call_flag"));
        this.setSex(rs.getInt("sex"));
        this.setBirthdayDate(rs.getDate("birthday"));
        this.setFirstVisitDate(rs.getDate("first_visit_date"));
        tmpValue = rs.getInt("job_id");
        this.setJobID(rs.wasNull() ? null : tmpValue);

        this.setNote(rs.getString("note"));
        this.setBeforeVisitNum(rs.getInt("before_visit_num"));

        tmpValue = rs.getInt("introducer_id");
        this.setIntroducerID(rs.wasNull() ? null : tmpValue);

        this.setIntroducerNote(rs.getString("introducer_note"));

        tmpValue = rs.getInt("first_coming_motive_class_id");
        this.setFirstComingMotiveClassId(rs.wasNull() ? null : tmpValue);

        this.setFirstComingMotiveNote(rs.getString("first_coming_motive_note"));
        MstSosiaCustomer msc = new MstSosiaCustomer();

        tmpValue = rs.getInt("sosia_id");
        msc.setSosiaID(rs.wasNull() ? 0 : tmpValue);

        this.setSosiaCustomer(msc);
        // IVS SANG START INSER 20131103 [gbソース]ソースマージ
        // start add 20130117 nakhoa お会計
        this.setAlertMark(rs.getString("alert_mark"));
        // end add 20130117 nakhoa お会計
        //Luc start add 20130125
        this.setReservationBufferTime(rs.getInt("reservation_buffer_time"));
        //Luc end add 20130125

        //An start add 20130125
        this.setQuestion_1(rs.getString("question_1"));
        this.setQuestion_2(rs.getString("question_2"));
        this.setWebReservationFlag(rs.getInt("web_reservation_flag"));
        this.setSelectedCreditLock(rs.getInt("credit_lock_flag"));

        // An end add 20130125

        // IVS SANG END INSER 20131103 [gbソース]ソースマージ

        try {
            this.setDeleted(rs.getString("delete_date") != null);
        } catch (Exception e) {
        }

        try {
            if (rs.getInt("family_main") == 1) {
                this.setFamilyMain(true);
            }
        } catch (Exception e) {
        }
    }

    /**
     * ResultSetWrapperからデータを読み込む。
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void setDataForPrepaid(ResultSetWrapper rs) throws SQLException {
        int tmpValue = 0;

        this.setCustomerID(rs.getInt("customer_id"));
        this.setCustomerNo(rs.getString("customer_no"));
        MstShop ms = new MstShop();
        ms.setShopID(rs.getInt("shop_id"));
        this.setShop(ms);
        this.setCustomerName(0, rs.getString("customer_name1"));
        this.setCustomerName(1, rs.getString("customer_name2"));
        this.setCustomerKana(0, rs.getString("customer_kana1"));
        this.setCustomerKana(1, rs.getString("customer_kana2"));
        this.setPostalCode(rs.getString("postal_code"));
        this.setAddress(0, rs.getString("address1"));
        this.setAddress(1, rs.getString("address2"));
        this.setAddress(2, rs.getString("address3"));
        this.setAddress(3, rs.getString("address4"));
        this.setPhoneNumber(rs.getString("phone_number"));
        this.setCellularNumber(rs.getString("cellular_number"));
        this.setFaxNumber(rs.getString("fax_number"));
        this.setPCMailAddress(rs.getString("pc_mail_address"));
        this.setCellularMailAddress(rs.getString("cellular_mail_address"));
        this.setSendMail(rs.getInt("send_mail"));
        this.setSendDm(rs.getInt("send_dm"));
        this.setCallFlag(rs.getInt("call_flag"));
        this.setSex(rs.getInt("sex"));
        this.setBirthdayDate(rs.getDate("birthday"));
        this.setFirstVisitDate(rs.getDate("first_visit_date"));
        tmpValue = rs.getInt("job_id");
        this.setJobID(rs.wasNull() ? null : tmpValue);

        this.setNote(rs.getString("note"));
        this.setBeforeVisitNum(rs.getInt("before_visit_num"));

        tmpValue = rs.getInt("introducer_id");
        this.setIntroducerID(rs.wasNull() ? null : tmpValue);

        this.setIntroducerNote(rs.getString("introducer_note"));

        tmpValue = rs.getInt("first_coming_motive_class_id");
        this.setFirstComingMotiveClassId(rs.wasNull() ? null : tmpValue);

        this.setFirstComingMotiveNote(rs.getString("first_coming_motive_note"));
        MstSosiaCustomer msc = new MstSosiaCustomer();

        tmpValue = rs.getInt("sosia_id");
        msc.setSosiaID(rs.wasNull() ? 0 : tmpValue);

        this.setSosiaCustomer(msc);
        // IVS SANG START INSER 20131103 [gbソース]ソースマージ
        // start add 20130117 nakhoa お会計
        this.setAlertMark(rs.getString("alert_mark"));
        // end add 20130117 nakhoa お会計
        //Luc start add 20130125
        this.setReservationBufferTime(rs.getInt("reservation_buffer_time"));
        //Luc end add 20130125

        //An start add 20130125
        this.setQuestion_1(rs.getString("question_1"));
        this.setQuestion_2(rs.getString("question_2"));
        this.setWebReservationFlag(rs.getInt("web_reservation_flag"));
        this.setSelectedCreditLock(rs.getInt("credit_lock_flag"));

        //Luc start add 20160316 #49043
        
        //次回予約時
        try {
            this.setNextVisitDate(rs.getString("next_date"));
        }catch(Exception ex) {}
        
        //次回予約日
        try {
            this.setNextVisitTime(rs.getString("next_time"));
        }catch(Exception ex) {}
        
        //Luc end add 20160316 #49043
        // An end add 20130125

        // IVS SANG END INSER 20131103 [gbソース]ソースマージ

        try {
            this.setDeleted(rs.getString("delete_date") != null);
        } catch (Exception e) {
        }

        try {
            if (rs.getInt("family_main") == 1) {
                this.setFamilyMain(true);
            }
        } catch (Exception e) {
        }
    }
    /**
     * 顧客マスタにデータを登録する。
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 成功
     */
    public boolean regist(ConnectionWrapper con) throws SQLException {
        //if(this.getCustomerID() == null || this.getCustomerID().equals(""))	return	false;

        String sql = "";
        
        // 20170801 nami edit start #18158 [gb] 新規顧客登録に失敗した際は、保持している顧客IDを破棄する
        //        if (isExists(con)) {
        //            sql = this.getUpdateSQL();
        //        } else {
        //            this.setCustomerID(getMaxCustomerID(con) + 1);
        //            sql = this.getInsertSQL();
        //        }
        //
        //        if (con.executeUpdate(sql) == 1) {
        //            return true;
        //        } else {
        //            return false;
        //        }

        if (isExists(con)) {
            sql = this.getUpdateSQL();
            if (con.executeUpdate(sql) == 1) {
                return true;
            } else {
                return false;
            }
        } else {
            
            // ３回までリトライ
            for(int i = 0; i < 3; i++) {

                this.setCustomerID(getMaxCustomerID(con) + 1);
                sql = this.getInsertSQL();
                try {
                    if(con.executeUpdate(sql) == 1) {
                        System.out.println("顧客情報：新規登録完了");
                        return true;
                    }
                }catch(SQLException e) {
                    con.rollback();
                    System.out.println("顧客情報：新規登録失敗 " + (i+1) + "回目");
                }
            }
            
            this.setCustomerID(null); //顧客ID破棄
            return false;
        }
        
        
        // テスト用　ここから
            //        SystemInfo.getLogger().log(Level.INFO, "★顧客情報登録開始★ 現在のMAX顧客ID = {0}", new Object[]{getMaxCustomerID(con)});   //test
            //        System.out.println("現在の内部保持顧客ID = " + this.getCustomerID());   //test
            //        
            //        if (isExists(con)) {
            //            System.out.println("顧客情報：更新開始 cus_id = " + this.getCustomerID());   //test
            //            sql = this.getUpdateSQL();
            //            if (con.executeUpdate(sql) == 1) {
            //                System.out.println("顧客情報：更新完了 cus_id = " + this.getCustomerID());   //test
            //                return true;
            //            } else {
            //                return false;
            //            }
            //        } else {
            //            
            //            for(int i = 0; i < 3; i++) {
            //
            //                if(this.getCustomerName(0).equals("ng")) {
            //                    this.setCustomerID(getMaxCustomerID(con));
            //                }else if(this.getCustomerName(0).equals("ok")) {
            //                    this.setCustomerID(getMaxCustomerID(con) + 1);
            //                }else {
            //                    this.setCustomerID(getMaxCustomerID(con)+i);
            //                }
            //                
            //                System.out.println("顧客情報：新規登録開始 " + (i+1) + "回目, 顧客IDセット cus_id = " +this.getCustomerID());   //test
            //
            //                sql = this.getInsertSQL();
            //                try {
            //                    if(con.executeUpdate(sql) == 1) {
            //                        System.out.println("顧客情報：新規登録完了 cus_id = " + this.getCustomerID());   //test
            //                        return true;
            //                    }
            //                }catch(SQLException e) {
            //                    con.rollback();
            //                    System.out.println("顧客情報：新規登録失敗 cus_id = " + this.getCustomerID());
            //                }
            //            }
            //            
            //            this.setCustomerID(null); //顧客ID破棄
            //            System.out.println("★★★顧客ID破棄");   //test
            //            System.out.println("顧客情報：３回以上新規登録失敗 cus_id = " +  this.getCustomerID());   //test
            //            System.out.println("-----------------------------------------------------------");   //test
            //            return false;
            //        }
        // テスト用　ここまで
        // 20170801 nami edit end #18158
    }

    //IVS_LVTu start add 2017/10/12 #27848 [gb]WEB連動：紐付けしているのに、常に「0」でWEB予約が入ってくる顧客がいる
    /**
     * 該当SOSIA IDで既にほかの顧客に紐ついている場合は、既に紐ついている顧客からSOSIA IDを削除（Null）して
     *
     * @return Update文
     */
    public boolean updateSosiaID(ConnectionWrapper con) throws SQLException {
        String sql = "update mst_customer\n"
                    + "set\n"
                    + "sosia_id =  null ,\n"
                    + "update_date = current_timestamp\n"
                    + "where	sosia_id = " + SQLUtil.convertForSQL(this.getSosiaCustomer().getSosiaID()) + "\n"
                    + "and delete_date is null \n"
                    + "and customer_id <> " + SQLUtil.convertForSQL(this.getCustomerID()) + "\n";

        if (con.executeUpdate(sql) == 1) {
            return true;
        } else {
            return false;
        }
    }
    //IVS_LVTu end add 2017/10/12 #27848 [gb]WEB連動：紐付けしているのに、常に「0」でWEB予約が入ってくる顧客がいる

    /**
     * スタッフマスタにデータを登録する。
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 成功
     */
    public boolean updateCustomerNo(ConnectionWrapper con) throws SQLException {
        if (this.getCustomerID() == null || this.getCustomerID().equals("")) {
            return false;
        }

        if (isExists(con)) {
            if (con.executeUpdate(this.getUpdateCustomerNoSQL()) == 1) {
                return true;
            }
        }

        return false;
    }

    /**
     * スタッフマスタからデータを削除する。（論理削除）
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 成功
     */
    public boolean delete(ConnectionWrapper con) throws SQLException {
        String sql = "";

        if (isExists(con)) {
            sql = this.getDeleteSQL();
        } else {
            return false;
        }

        if (con.executeUpdate(sql) == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 顧客マスタにデータが存在するかチェックする。
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - 存在する
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if (this.getCustomerID() == null || this.getCustomerID().equals("")) {
            return false;
        }

        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Select文を取得する。
     *
     * @return Select文
     */
    private String getSelectSQL() {
        if (SystemInfo.getDatabase().startsWith("pos_hair_rizap") || SystemInfo.getDatabase().startsWith("pos_hair_rizap_bak")) {
            return "select mc.*,ms.staff_no,ms.staff_id\n"
                    + "from mst_customer mc \n"
                    + "left join mst_staff ms on mc.main_staff_id = ms.staff_id \n"
                    + "where	customer_id = " + SQLUtil.convertForSQL(this.getCustomerID()) + "\n";
        } else {
            return "select *\n"
                    + "from mst_customer \n"
                    + "where	customer_id = " + SQLUtil.convertForSQL(this.getCustomerID()) + "\n";
        }

    }

    /**
     * Insert文を取得する。
     *
     * @return Insert文
     */
    private String getInsertSQL() {
        //Thanh edit start 2013/06/27
        String SQL = "insert into mst_customer\n"
                + "(customer_id, customer_no, shop_id, customer_name1, customer_name2,"
                + "customer_kana1, customer_kana2,\n"
                + "postal_code, address1, address2, address3, address4,\n"
                + "phone_number, cellular_number, fax_number,\n"
                + "pc_mail_address, cellular_mail_address, send_mail, send_dm, call_flag,\n"
                + "sex, birthday, job_id, note, sosia_id, first_visit_date,\n"
                + "insert_date, update_date, delete_date, introducer_id, introducer_note,\n";
        if (SystemInfo.getDatabase().startsWith("pos_hair_rizap") || SystemInfo.getDatabase().startsWith("pos_hair_rizap_bak")) {
            SQL += "main_staff_id,\n";
        }
        // IVS SANG START INSERT 20131102 [gbソース]ソースマージ
        // start add 20130117 nakhoa お会計
        SQL += "alert_mark,\n";
        // end add 20130117 nakhoa お会計
        // start add 20130117 An 
        SQL += "reservation_buffer_time,\n"
                + "question_1,\n"
                + "question_2,\n"
                + "web_reservation_flag,\n"
                + "credit_lock_flag,\n";
        // end add 20130117 An
        // IVS SANG END INSERT 20131102 [gbソース]ソースマージ
        SQL += "first_coming_motive_class_id, first_coming_motive_note, before_visit_num, login_id, password)\n"
                // 20170802 nami edit start #18158
                // + "select\n"
                // + "coalesce(max(mc.customer_id), 0) + 1,\n"
                + " values( "
                + SQLUtil.convertForSQL(this.getCustomerID()) + ",\n"   
                // 20170802 nami edit end #18158
                + SQLUtil.convertForSQL(this.getCustomerNo()) + ",\n"
                + SQLUtil.convertForSQL(this.getShop().getShopID(), "0") + ",\n"
                + SQLUtil.convertForSQL(this.getCustomerName(0)) + ",\n"
                + SQLUtil.convertForSQL(this.getCustomerName(1)) + ",\n"
                + SQLUtil.convertForSQL(this.getCustomerKana(0)) + ",\n"
                + SQLUtil.convertForSQL(this.getCustomerKana(1)) + ",\n"
                + SQLUtil.convertForSQL(this.getPostalCode()) + ",\n"
                + SQLUtil.convertForSQL(this.getAddress(0)) + ",\n"
                + SQLUtil.convertForSQL(this.getAddress(1)) + ",\n"
                + SQLUtil.convertForSQL(this.getAddress(2)) + ",\n"
                + SQLUtil.convertForSQL(this.getAddress(3)) + ",\n"
                + SQLUtil.convertForSQL(this.getPhoneNumber()) + ",\n"
                + SQLUtil.convertForSQL(this.getCellularNumber()) + ",\n"
                + SQLUtil.convertForSQL(this.getFaxNumber()) + ",\n"
                + SQLUtil.convertForSQL(this.getPCMailAddress()) + ",\n"
                + SQLUtil.convertForSQL(this.getCellularMailAddress()) + ",\n"
                + SQLUtil.convertForSQL(this.getSendMail()) + ",\n"
                + SQLUtil.convertForSQL(this.getSendDm()) + ",\n"
                + SQLUtil.convertForSQL(this.getCallFlag()) + ",\n"
                + SQLUtil.convertForSQL(this.getSex()) + ",\n"
                + SQLUtil.convertForSQLDateOnly(this.getBirthday()) + ",\n"
                + SQLUtil.convertForSQL(this.getJobID()) + ",\n"
                + SQLUtil.convertForSQL(this.getNote()) + ",\n"
                + SQLUtil.convertForSQL((this.getSosiaCustomer() == null ? null : this.getSosiaCustomer().getSosiaID())) + ",\n"
                + SQLUtil.convertForSQLDateOnly(this.getFirstVisitDate()) + ",\n"
                + "current_timestamp, current_timestamp, null,\n"
                + SQLUtil.convertForSQL((this.getIntroducerID() == null ? null : this.getIntroducerID())) + ",\n"
                + SQLUtil.convertForSQL(this.getIntroducerNote()) + ",\n";
        if (SystemInfo.getDatabase().startsWith("pos_hair_rizap") || SystemInfo.getDatabase().startsWith("pos_hair_rizap_bak")) {
            SQL += SQLUtil.convertForSQL(this.mainStaffId) + ",\n";
        }
        // IVS SANG START INSERT 20131102 [gbソース]ソースマージ
        // start add 20130117 nakhoa お会計
        SQL += SQLUtil.convertForSQL(this.getAlertMark()) + ",\n";
        // end add 20130117 nakhoa お会計
        // start add 20130117 An
        SQL += SQLUtil.convertForSQL(this.getReservationBufferTime()) + ",\n"
                + SQLUtil.convertForSQL(this.getQuestion_1()) + ",\n"
                + SQLUtil.convertForSQL(this.getQuestion_2()) + ",\n"
                + SQLUtil.convertForSQL(this.getWebReservationFlag()) + ",\n"
                + SQLUtil.convertForSQL(this.getSelectedCreditLock()) + ",\n";
        // end add 20130117 An
        // IVS SANG START INSERT 20131102 [gbソース]ソースマージ
        SQL += SQLUtil.convertForSQL((this.getFirstComingMotiveClassId() == null ? null : this.getFirstComingMotiveClassId())) + ",\n"
                + SQLUtil.convertForSQL(this.getFirstComingMotiveNote()) + ",\n"
                + SQLUtil.convertForSQL(this.getBeforeVisitNum()) + ",\n"
                + "make_login_id() " + ",\n"
                
                // 20170802 nami edit start #18158
                //+ "make_password(8)" + "\n"
                //+ "from mst_customer mc\n";
                + "make_password(8)  )";
                // 20170802 nami edit end #18158
        return SQL;
        //Thanh edit end 2013/06/27
    }

    /**
     * Update文を取得する。
     *
     * @return Update文
     */
    private String getUpdateSQL() {
        //Thanh add start 2013/06/27
        String mainStaffID = "";
        if (SystemInfo.getDatabase().startsWith("pos_hair_rizap") || SystemInfo.getDatabase().startsWith("pos_hair_rizap_bak")) {
            mainStaffID = "main_staff_id = " + SQLUtil.convertForSQL(this.mainStaffId) + ",\n";
        }
        //Thanh add end 2013/06/27
        return "update mst_customer\n"
                + "set\n"
                + "customer_no = " + SQLUtil.convertForSQL(this.getCustomerNo()) + ",\n"
                + "shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID(), "0") + ",\n"
                + "customer_name1 = " + SQLUtil.convertForSQL(this.getCustomerName(0)) + ",\n"
                + "customer_name2 = " + SQLUtil.convertForSQL(this.getCustomerName(1)) + ",\n"
                + "customer_kana1 = " + SQLUtil.convertForSQL(this.getCustomerKana(0)) + ",\n"
                + "customer_kana2 = " + SQLUtil.convertForSQL(this.getCustomerKana(1)) + ",\n"
                + "postal_code = " + SQLUtil.convertForSQL(this.getPostalCode()) + ",\n"
                + "address1 = " + SQLUtil.convertForSQL(this.getAddress(0)) + ",\n"
                + "address2 = " + SQLUtil.convertForSQL(this.getAddress(1)) + ",\n"
                + "address3 = " + SQLUtil.convertForSQL(this.getAddress(2)) + ",\n"
                + "address4 = " + SQLUtil.convertForSQL(this.getAddress(3)) + ",\n"
                + mainStaffID
                + "phone_number = " + SQLUtil.convertForSQL(this.getPhoneNumber()) + ",\n"
                + "cellular_number = " + SQLUtil.convertForSQL(this.getCellularNumber()) + ",\n"
                + "fax_number = " + SQLUtil.convertForSQL(this.getFaxNumber()) + ",\n"
                + "pc_mail_address = " + SQLUtil.convertForSQL(this.getPCMailAddress()) + ",\n"
                + "cellular_mail_address = " + SQLUtil.convertForSQL(this.getCellularMailAddress()) + ",\n"
                + "send_mail = " + SQLUtil.convertForSQL(this.getSendMail()) + ",\n"
                + "send_dm = " + SQLUtil.convertForSQL(this.getSendDm()) + ",\n"
                + "call_flag = " + SQLUtil.convertForSQL(this.getCallFlag()) + ",\n"
                + "sex = " + SQLUtil.convertForSQL(this.getSex()) + ",\n"
                + "birthday = " + SQLUtil.convertForSQLDateOnly(this.getBirthday()) + ",\n"
                + "job_id = " + SQLUtil.convertForSQL(this.getJobID()) + ",\n"
                + "note = " + SQLUtil.convertForSQL(this.getNote()) + ",\n"
                + "sosia_id = " + SQLUtil.convertForSQL((this.getSosiaCustomer() == null ? null : this.getSosiaCustomer().getSosiaID())) + ",\n"
                + "first_visit_date = " + SQLUtil.convertForSQLDateOnly(this.getFirstVisitDate()) + ",\n"
                + "update_date = current_timestamp,\n"
                + "introducer_id = " + SQLUtil.convertForSQL((this.getIntroducerID() == null ? null : this.getIntroducerID())) + ",\n"
                + "introducer_note = " + SQLUtil.convertForSQL(this.getIntroducerNote()) + ",\n"
                + "first_coming_motive_class_id = " + SQLUtil.convertForSQL((this.getFirstComingMotiveClassId() == null ? null : this.getFirstComingMotiveClassId())) + ",\n"
                + "first_coming_motive_note = " + SQLUtil.convertForSQL(this.getFirstComingMotiveNote()) + ",\n"
                + "before_visit_num = " + SQLUtil.convertForSQL(this.beforeVisitNum) + ",\n"
                + // IVS SANG START INSERT 20131102 [gbソース]ソースマージ
                // start add 20130117 nakhoa お会計
                "alert_mark = " + SQLUtil.convertForSQL(this.getAlertMark()) + ",\n"
                // end add 20130117 nakhoa お会計
                // start add 20130117 An
                + "reservation_buffer_time = " + SQLUtil.convertForSQL(this.getReservationBufferTime()) + ",\n"
                + "question_1 = " + SQLUtil.convertForSQL(this.getQuestion_1()) + ",\n"
                + "question_2 = " + SQLUtil.convertForSQL(this.getQuestion_2()) + ",\n"
                + "web_reservation_flag = " + SQLUtil.convertForSQL(this.getWebReservationFlag()) + ",\n"
                + "credit_lock_flag = " + SQLUtil.convertForSQL(this.getSelectedCreditLock()) + ",\n"
                //IVS_LVTu start add 2016/03/10 mmd.getSosiaID()
                + "fb_id = " + SQLUtil.convertForSQL(this.getFbID()) + ",\n"
                //IVS_LVTu end add 2016/03/10 mmd.getSosiaID()
                + // end add 20130117 An
                // IVS SANG START INSERT 20131102 [gbソース]ソースマージ
                "delete_date = null\n"
                + "where	customer_id = " + SQLUtil.convertForSQL(this.getCustomerID()) + "\n";
    }

    /**
     * 削除用Update文を取得する。
     *
     * @return 削除用Update文
     */
    private String getDeleteSQL() {
        return "update mst_customer\n"
                + "set\n"
                + "delete_date = current_timestamp\n"
                + "where	customer_id = " + SQLUtil.convertForSQL(this.getCustomerID()) + "\n";
    }

    /**
     * 顧客No.を更新するUpdate文を取得する。
     *
     * @return Update文
     */
    private String getUpdateCustomerNoSQL() {
        return "update mst_customer\n"
                + "set\n"
                + "customer_no = " + SQLUtil.convertForSQL(this.getCustomerNo()) + ",\n"
                + "update_date = current_timestamp\n"
                + "where	customer_id = " + SQLUtil.convertForSQL(this.getCustomerID()) + "\n";
    }

    /**
     * 顧客IDに対応する顧客データの配列を取得する。
     *
     * @param id 顧客ID
     * @return 顧客IDに対応する顧客データの配列
     */
    public static ArrayList<MstCustomer> getMstCustomerArrayByID(
            ConnectionWrapper con,
            Integer id, Integer shopID) throws SQLException {
        ArrayList<MstCustomer> result = new ArrayList<MstCustomer>();

        ResultSetWrapper rs = con.executeQuery(MstCustomer.getMstCustomerByIdSQL(id, shopID));

        while (rs.next()) {
            MstCustomer mc = new MstCustomer();

            mc.setData(rs);

            result.add(mc);
        }

        rs.close();

        return result;
    }

    /**
     * 顧客IDに対応する顧客データの配列を取得するSQL文を取得する。
     *
     * @param id 顧客ID
     * @return 顧客IDに対応する顧客データの配列を取得するSQL文
     */
    public static String getMstCustomerByIdSQL(Integer customerID, Integer shopID) {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     *");
        sql.append(" from");
        sql.append("     mst_customer mc");
        sql.append(" where");
        sql.append("         mc.delete_date is null");
        sql.append("     and mc.shop_id = " + SQLUtil.convertForSQL(shopID));
        sql.append("     and customer_id = " + SQLUtil.convertForSQL(customerID));
        sql.append(" order by");
        sql.append("     mc.customer_id");

        return sql.toString();
    }

    /**
     * 顧客コードに対応する顧客データの配列を取得する。
     *
     * @param no 顧客コード
     * @return 顧客コードに対応する顧客データの配列
     */
    public static ArrayList<MstCustomer> getMstCustomerArrayByNo(
            ConnectionWrapper con,
            String no, Integer shopID) throws SQLException {
        ArrayList<MstCustomer> result = new ArrayList<MstCustomer>();

        ResultSetWrapper rs = con.executeQuery(MstCustomer.getMstCustomerByNoSQL(no, shopID));

        while (rs.next()) {
            MstCustomer mc = new MstCustomer();

            mc.setData(rs);

            result.add(mc);
        }

        rs.close();

        return result;
    }

    /**
     * 顧客コードに対応する顧客データの配列を取得するSQL文を取得する。
     *
     * @param no 顧客コード
     * @return 顧客コードに対応する顧客データの配列を取得するSQL文
     */
    public static String getMstCustomerByNoSQL(String customerNo, Integer shopID) {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     *");
        sql.append(" from");
        sql.append("     mst_customer mc");
        sql.append(" where");
        sql.append("         mc.delete_date is null");
        sql.append("     and mc.shop_id = " + SQLUtil.convertForSQL(shopID));

        if (!customerNo.startsWith("0") && CheckUtil.isNumeric(customerNo)) {
            sql.append(" and translate(customer_no, '0123456789', '') = ''");
            sql.append(" and customer_no::text::numeric = " + customerNo);
        } else {
            sql.append(" and customer_no = '" + customerNo + "'");
        }

        sql.append(" order by");
        sql.append("     mc.customer_id");

        return sql.toString();
    }

    /**
     * 電話にかかってきた番号に対応する顧客データの配列を取得する。
     *
     * @param no 番号
     * @return 電話にかかってきた番号に対応する顧客データの配列
     */
    public static ArrayList<MstCustomer> getMstCustomerArrayByPhoneNo(
            ConnectionWrapper con,
            String no, Integer shopID) throws SQLException {
        ArrayList<MstCustomer> result = new ArrayList<MstCustomer>();

        ResultSetWrapper rs = con.executeQuery(MstCustomer.getMstCustomerByPhoneNoSQL(no, shopID));

        while (rs.next()) {
            MstCustomer mc = new MstCustomer();

            mc.setData(rs);

            result.add(mc);
        }

        rs.close();

        return result;
    }

    /**
     * 電話にかかってきた番号に対応する顧客データの配列を取得するSQL文を取得する。
     *
     * @param no 番号
     * @return 番号に対応する顧客データの配列を取得するSQL文
     */
    public static String getMstCustomerByPhoneNoSQL(String no, Integer shopID) {
        return "select *\n"
                + "from mst_customer mc\n"
                + "where mc.delete_date is null\n"
                + "and mc.shop_id = " + shopID.toString() + "\n"
                + "and (mc.phone_number = '" + no + "'\n"
                + "or mc.cellular_number= '" + no + "')\n"
                + "order by mc.customer_id\n";
    }

    /**
     * 紹介した人一覧を取得する。
     *
     * @param con コネクションオブジェクト
     * @param customerID　顧客ID
     * @return 電話にかかってきた番号に対応する顧客データの配列
     */
    public static ArrayList<MstCustomer> getIntroduceList(
            ConnectionWrapper con,
            Integer customerID,
            Integer shopID) throws SQLException {
        ArrayList<MstCustomer> result = new ArrayList<MstCustomer>();

        ResultSetWrapper rs = con.executeQuery(MstCustomer.getIntroduceListSQL(customerID, shopID));

        while (rs.next()) {
            MstCustomer mc = new MstCustomer();

            mc.setData(rs);

            result.add(mc);
        }

        rs.close();

        return result;
    }

    /**
     * 紹介した人一覧を取得するSQL文を取得する
     *
     * @param customerID 顧客ID
     * @return 紹介した人の一覧を取得するSQL文
     */
    public static String getIntroduceListSQL(Integer customerID, Integer shopID) {
        return "select *\n"
                + "from mst_customer mc\n"
                + "where introducer_id = " + customerID + "\n"
                + "and delete_date is null\n"
                + "and shop_id = " + SQLUtil.convertForSQL(shopID, "0") + "\n"
                + "order by mc.customer_id\n";
    }

    public static Integer getMaxCustomerID(ConnectionWrapper con) throws SQLException {
        if (con == null) {
            return null;
        }

        ResultSetWrapper rs = con.executeQuery(MstCustomer.getMaxCustomerIDSQL());

        if (rs.next()) {
            return rs.getInt("max_id");
        } else {
            return null;
        }
    }

    private static String getMaxCustomerIDSQL() {
        return "select max(customer_id) as max_id\n"
                + "from mst_customer";
    }

    public static String getNewCustomerNo(ConnectionWrapper con, String prefix, int length) throws SQLException {
        if (con == null) {
            return null;
        }

        ResultSetWrapper rs = con.executeQuery(MstCustomer.getNewCustomerNoSQL(prefix, length));

        if (rs.next()) {

            String newNo = rs.getString("new_no");
            String maxNo = rs.getString("max_no");

            if (rs.getBoolean("overflow")) {

                String msg = "";

                if (length < 9) {

                    msg += "自動採番の最大値（" + maxNo + "）が使用されているため、\n";
                    msg += newNo + "で発行します。\n";
                    msg += "\n";
                    msg += "※【基本設定】⇒【会社関連】⇒【店舗情報登録】より\n";
                    msg += "自動採番の連番桁数を" + length + "桁から" + (length + 1) + "桁に変更してください。\n";
                    msg += "\n";
                    msg += "※連番桁数設定を変更するまでは、\n";
                    msg += "　常に" + newNo + "が発行されますのでご注意ください。\n";

                } else {

                    msg += "自動採番の最大値（" + maxNo + "）が使用されています。\n";
                    msg += "自動採番の連番桁数はこれ以上増やすことはできません。\n";

                    newNo = maxNo;
                }

                MessageDialog.showMessageDialog(
                        null,
                        msg,
                        "顧客No.自動採番",
                        JOptionPane.WARNING_MESSAGE);
            }

            return newNo;

        } else {
            return null;
        }
    }

    private static String getNewCustomerNoSQL(String prefix, int length) {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     '" + prefix + "' ||");
        sql.append("         case when " + length + " < length((max_no + 1)::text)");
        sql.append("             then (max_no + 1)::text");
        sql.append("             else lpad((max_no + 1)::text, " + length + ", '0')");
        sql.append("         end as new_no");
        sql.append("     ,'" + prefix + "' ||");
        sql.append("             lpad((max_no)::text, " + length + ", '0') as max_no");
        sql.append("     ," + length + " < length((max_no + 1)::text) as overflow");
        sql.append(" from");
        sql.append("     (");
        sql.append("         select");
        sql.append("             coalesce(max(substr(customer_no, " + (prefix.length() + 1) + ")), '0')::numeric as max_no");
        sql.append("         from");
        sql.append("             mst_customer");
        sql.append("         where");
        sql.append("             customer_no like '" + prefix + "%'");
        sql.append("         and length(substr(customer_no, " + (prefix.length() + 1) + ")) = " + length);
        sql.append("         and translate(substr(customer_no, " + (prefix.length() + 1) + "),'0123456789','') = ''");
        sql.append("         and delete_date is null");
        sql.append("     ) t");

        return sql.toString();
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * @return the rank
     */
    public String getRank() {
        return rank;
    }

    /**
     * @param rank the rank to set
     */
    public void setRank(String rank) {
        this.rank = rank;
    }

    /**
     * @return the familyList
     */
    public ArrayList<MstCustomer> getFamilyList() {
        return familyList;
    }

    /**
     * @param familyList the familyList to set
     */
    public void setFamilyList(ArrayList<MstCustomer> familyList) {
        this.familyList = familyList;
    }

    public boolean existsFamily() {
        return familyList.size() > 0;
    }

    /**
     * @return the familyMain
     */
    public boolean isFamilyMain() {
        return familyMain;
    }

    /**
     * @param familyMain the familyMain to set
     */
    public void setFamilyMain(boolean familyMain) {
        this.familyMain = familyMain;
    }
    // IVS SANG START INSERT 20131102 [gbソース]ソースマージ
    private Integer reservationBufferTime = null;

    public Integer getReservationBufferTime() {
        return reservationBufferTime;
    }

    public void setReservationBufferTime(Integer reservationBufferTime) {
        this.reservationBufferTime = reservationBufferTime;
    }
    private Integer webReservationFlag = null;

    public Integer getWebReservationFlag() {
        return webReservationFlag;
    }

    public void setWebReservationFlag(Integer webReservationFlag) {
        if (webReservationFlag != null) {
            this.webReservationFlag = webReservationFlag;
        } else {
            this.webReservationFlag = null;
        }
    }

    //An start add 20130125
    public String getWebReservationFlagString() {
        if (this.getWebReservationFlag() == null) {
            return "";
        }

        switch (this.getWebReservationFlag()) {
            case 0:
                return "不可";
            case 1:
                return "可";
        }

        return "";
    }

    public Integer getSelectedCreditLock() {
        return creditLock;
    }

    public void setSelectedCreditLock(Integer creditLock) {
        if (creditLock != null) {
            this.creditLock = creditLock;
        } else {
            this.creditLock = null;
        }
    }
    //An start add 20130125

    public String getQuestion_1() {
        return question_1;
    }

    public void setQuestion_1(String question_1) {
        this.question_1 = question_1;
    }

    public String getQuestion_2() {
        return question_2;
    }

    public void setQuestion_2(String question_2) {
        this.question_2 = question_2;
    }

    // start add 20130117 nakhoa お会計
    /**
     * @return the alertMark
     */
    public String getAlertMark() {
        return alertMark;
    }

    /**
     * @param alertMark the alertMark to set
     */
    public void setAlertMark(String alertMark) {
        this.alertMark = alertMark;
    }
    //An end add 20130125
    // IVS SANG END INSERT 20131102 [gbソース]ソースマージ
}
