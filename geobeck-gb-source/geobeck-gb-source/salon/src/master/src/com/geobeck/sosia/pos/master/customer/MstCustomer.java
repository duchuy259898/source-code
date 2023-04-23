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
 * �ڋq�}�X�^�f�[�^
 *
 * @author katagiri
 */
public class MstCustomer {

    /**
     * �ڋq�h�c
     */
    private Integer customerID = null;
    /**
     * �ڋq�ԍ�
     */
    private String customerNo = null;
    /**
     * �X��
     */
    private MstShop shop = new MstShop();
    /**
     * �ڋq��
     */
    private String[] customerName = {"", ""};
    /**
     * �ڋq�t���K�i
     */
    private String[] customerKana = {"", ""};
    /**
     * �X�֔ԍ�
     */
    private String postalCode = "";
    /**
     * �Z��
     */
    private String[] address = {"", "", "", ""};
    /**
     * �d�b�ԍ�
     */
    private String phoneNumber = "";
    /**
     * �g�єԍ�
     */
    private String cellularNumber = "";
    /**
     * �e�`�w�ԍ�
     */
    private String faxNumber = "";
    /**
     * �o�b���[���A�h���X
     */
    private String pcMailAddress = "";
    /**
     * �g�у��[���A�h���X
     */
    private String cellularMailAddress = "";
    /**
     * ���[���z�M��
     */
    private Integer sendMail = 1;
    /**
     * DM�z�M��
     */
    private Integer sendDm = 1;
    /**
     * �d�b�A����
     */
    private Integer callFlag = 1;
    /**
     * ����
     */
    private Integer sex = null;
    /**
     * �a����
     */
    private JapaneseCalendar birthday = null;
    /**
     * �E��
     */
    private MstJob job = null;
    /**
     * ���l
     */
    private String note = "";
    /**
     * �����O���X��
     */
    private Integer beforeVisitNum = 0;
    /**
     * �Љ���l
     */
    private Integer introducerID = null;
    /**
     * �Љ�ڋq����
     */
    private String introducerNote = null;
    /**
     * ���񗈓X���@
     */
    private MstFirstComingMotive firstComingMotive = null;
    /**
     * ���񗈓X���@����
     */
    private String firstComingMotiveNote = null;
    /**
     * SOSIA�A���f�[�^
     */
    private MstSosiaCustomer msc = new MstSosiaCustomer();
    /**
     * �K���
     */
    protected Long visitCount = 0l;
    /**
     * ���񗈓X��
     */
    private java.util.Calendar firstVisitday = null;
    /**
     * �����N
     */
    private String rank = null;
    // start add 20130117 nakhoa ����v
    /**
     * �A���[�g
     */
    private String alertMark = null;
    // end add 20130117 nakhoa ����v
    private Integer mainStaffId = null;
    /**
     * �X�^�b�t
     */
    protected MstStaff staff = new MstStaff();
    /**
     * �Ƒ�
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
    
    //����\��
    private String nextVisitDate;

    //����\��
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
     * �R���X�g���N�^
     *
     * @param customerID �ڋq�h�c
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
     * �ڋq�h�c���擾����B
     *
     * @return �ڋq�h�c
     */
    public Integer getCustomerID() {
        return customerID;
    }

    /**
     * �ڋq�h�c���Z�b�g����B
     *
     * @param customerID �ڋq�h�c
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
     * �ڋq�h�c���Z�b�g����B
     *
     * @param customerID �ڋq�h�c
     */
    public void setMainStaffId(Integer mainStaffId) {
        this.mainStaffId = mainStaffId;
    }

    /**
     * �X�^�b�t
     *
     * @return �X�^�b�t
     */
    public MstStaff getStaff() {
        return staff;
    }

    /**
     * �X�^�b�t
     *
     * @param staff �X�^�b�t
     */
    public void setStaff(MstStaff staff) {
        this.staff = staff;
    }
    //Thanh add start 2013/06/27

    /**
     * �X�܂��擾����B
     *
     * @return �X��
     */
    public MstShop getShop() {
        return shop;
    }

    /**
     * �X�܂��Z�b�g����B
     *
     * @param shop �X��
     */
    public void setShop(MstShop shop) {
        this.shop = shop;
    }

    /**
     * �ڋq�����擾����B
     *
     * @return �ڋq��
     */
    public String[] getCustomerName() {
        return customerName;
    }

    /**
     * �ڋq�����擾����B
     *
     * @param index �C���f�b�N�X
     * @return �ڋq��
     */
    public String getCustomerName(int index) {
        return customerName[index];
    }

    /**
     * �ڋq�����Z�b�g����B
     *
     * @param customerName �ڋq��
     */
    public void setCustomerName(String[] customerName) {
        this.customerName = customerName;
    }

    /**
     * �ڋq�����Z�b�g����B
     *
     * @param index �C���f�b�N�X
     * @param customerName �ڋq��
     */
    public void setCustomerName(int index, String customerName) {
        this.customerName[index] = customerName;
    }

    /**
     * �ڋq�̃t���l�[�����擾����B
     *
     * @return �t���l�[��
     */
    public String getFullCustomerName() {
        return (customerName[0] == null ? "" : customerName[0])
                + (customerName[1] == null || customerName[1].equals("") ? "" : "�@" + customerName[1]);
    }

    /**
     * �ڋq�t���K�i���擾����B
     *
     * @return �ڋq�t���K�i
     */
    public String[] getCustomerKana() {
        return customerKana;
    }

    /**
     * �ڋq�t���K�i���擾����B
     *
     * @param index �C���f�b�N�X
     * @return �ڋq�t���K�i
     */
    public String getCustomerKana(int index) {
        return customerKana[index];
    }

    /**
     * �ڋq�t���K�i���Z�b�g����B
     *
     * @param customerKana �ڋq�t���K�i
     */
    public void setCustomerKana(String[] customerKana) {
        this.customerKana = customerKana;
    }

    /**
     * �ڋq�t���K�i���Z�b�g����B
     *
     * @param index �C���f�b�N�X
     * @param customerKana �ڋq�t���K�i
     */
    public void setCustomerKana(int index, String customerKana) {
        this.customerKana[index] = customerKana;
    }

    /**
     * �ڋq�̃t���K�i�̃t���l�[�����擾����B
     *
     * @return �t���K�i�̃t���l�[��
     */
    public String getFullCustomerKana() {
        return (customerKana[0] == null ? "" : customerKana[0])
                + (customerKana[1] == null || customerKana[1].equals("") ? "" : "�@" + customerKana[1]);
    }

    /**
     * �X�֔ԍ����擾����B
     *
     * @return �X�֔ԍ�
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * �X�֔ԍ����Z�b�g����B
     *
     * @param postalCode �X�֔ԍ�
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
     * �X�֔ԍ����擾����B
     *
     * @return �X�֔ԍ�
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
     * �Z�����擾����B
     *
     * @return �Z��
     */
    public String[] getAddress() {
        return address;
    }

    /**
     * �Z�����擾����B
     *
     * @param index �C���f�b�N�X
     * @return �Z��
     */
    public String getAddress(int index) {
        return address[index] == null ? "" : address[index];
    }

    /**
     * �Z�����Z�b�g����B
     *
     * @param address �Z��
     */
    public void setAddress(String[] address) {
        this.address = address;
    }

    /**
     * �Z�����Z�b�g����B
     *
     * @param index �C���f�b�N�X
     * @param address �Z��
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
     * �d�b�ԍ����擾����B
     *
     * @return �d�b�ԍ�
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * �d�b�ԍ����Z�b�g����B
     *
     * @param phoneNumber �d�b�ԍ�
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * �g�єԍ����擾����B
     *
     * @return �g�єԍ�
     */
    public String getCellularNumber() {
        return cellularNumber;
    }

    /**
     * �g�єԍ����Z�b�g����B
     *
     * @param cellularNumber �g�єԍ�
     */
    public void setCellularNumber(String cellularNumber) {
        this.cellularNumber = cellularNumber;
    }

    /**
     * �e�`�w�ԍ����擾����B
     *
     * @return �e�`�w�ԍ�
     */
    public String getFaxNumber() {
        return faxNumber;
    }

    /**
     * �e�`�w�ԍ����Z�b�g����B
     *
     * @param faxNumber �e�`�w�ԍ�
     */
    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    /**
     * �o�b���[���A�h���X���擾����B
     *
     * @return �o�b���[���A�h���X
     */
    public String getPCMailAddress() {
        return pcMailAddress;
    }

    /**
     * �o�b���[���A�h���X���Z�b�g����B
     *
     * @param pcMailAddress �o�b���[���A�h���X
     */
    public void setPCMailAddress(String pcMailAddress) {
        if (pcMailAddress == null) {
            this.pcMailAddress = "";
        } else {
            this.pcMailAddress = pcMailAddress;
        }

    }

    /**
     * �g�у��[���A�h���X���擾����B
     *
     * @return �g�у��[���A�h���X
     */
    public String getCellularMailAddress() {
        return cellularMailAddress;
    }

    /**
     * �g�у��[���A�h���X���Z�b�g����B
     *
     * @param cellularMailAddress �g�у��[���A�h���X
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
     * ���[���z�M�ۂ��擾����B
     *
     * @return ���[���z�M��
     */
    public Integer getSendMail() {
        return sendMail;
    }

    /**
     * ���[���z�M�ۂ��Z�b�g����B
     *
     * @param sendMail ���[���z�M��
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
                return "�s��";
            case 1:
                return "��";
        }

        return "";
    }

    /**
     * DM�z�M�ۂ��擾����B
     *
     * @return DM�z�M��
     */
    public Integer getSendDm() {
        return sendDm;
    }

    /**
     * DM�z�M�ۂ��Z�b�g����B
     *
     * @param sendDm DM�z�M��
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
                return "�s��";
            case 1:
                return "��";
        }

        return "";
    }

    /**
     * �d�b�A���ۂ��擾����B
     *
     * @return �d�b�A����
     */
    public Integer getCallFlag() {
        return callFlag;
    }

    /**
     * �d�b�A���ۂ��Z�b�g����B
     *
     * @param callFlag �d�b�A����
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
                return "�s��";
            case 1:
                return "��";
        }

        return "";
    }

    /**
     * ���ʂ��擾����B
     *
     * @return ����
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * ���ʂ��Z�b�g����B
     *
     * @param sex ����
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
                return "�j";
            case 2:
                return "��";
        }

        return "";
    }

    /**
     * �a�������擾����B
     *
     * @return �a����
     */
    public JapaneseCalendar getBirthday() {
        return birthday;
    }

    /**
     * �a�������Z�b�g����B
     *
     * @param birthday �a����
     */
    public void setBirthday(JapaneseCalendar birthday) {
        this.birthday = birthday;
    }

    /**
     * �a�������Z�b�g����B
     *
     * @param birthday �a����
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
     * �a���N���擾����B
     *
     * @return �a���N
     */
    public Integer getBirthYear() {
        if (birthday == null) {
            return null;
        } else {
            return birthday.get(birthday.EXTENDED_YEAR);
        }
    }

    /**
     * �a�������擾����B
     *
     * @return �a����
     */
    public Integer getBirthMonth() {
        if (birthday == null) {
            return null;
        } else {
            return birthday.get(birthday.MONTH) + 1;
        }
    }

    /**
     * �a�������擾����B
     *
     * @return �a����
     */
    public Integer getBirthDay() {
        if (birthday == null) {
            return null;
        } else {
            return birthday.get(birthday.DAY_OF_MONTH);
        }
    }

    /**
     * �a�����̕�������擾����B
     *
     * @return �a�����̕�����
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
     * �a�����̕�������擾����B
     *
     * @param pattern ���t�̃p�^�[��
     * @return �a�����̕�����
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
     * �a�����̕�������擾����B
     *
     * @return �a�����̕�����
     */
    public String getBirthdayString2() {
        if (birthday == null) {
            return null;
        } else {
            if (1900 < birthday.get(JapaneseCalendar.EXTENDED_YEAR)) {
                return DateUtil.format(birthday.getTime(), "yyyy�NM��d��");
            } else {
                return DateUtil.format(birthday.getTime(), "M��d��");
            }
        }
    }
    //IVS_LVTu end add 2015/11/13 New request #44270
    /**
     * �E�Ƃ��擾����B
     *
     * @return �E��
     */
    public MstJob getJob() {
        return job;
    }

    /**
     * �E�Ƃ��Z�b�g����B
     *
     * @param job �E��
     */
    public void setJob(MstJob job) {
        this.job = job;
    }

    /**
     * �E�Ƃh�c���擾����B
     *
     * @return �E�Ƃh�c
     */
    public Integer getJobID() {
        if (job == null) {
            return null;
        } else {
            return job.getJobID();
        }
    }

    /**
     * �E�Ƃh�c���Z�b�g����B
     *
     * @param jobID �E�Ƃh�c
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
     * ���l���擾����B
     *
     * @return ���l
     */
    public String getNote() {
        return note;
    }

    /**
     * ���l���Z�b�g����B
     *
     * @param note ���l
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * �����O���X�񐔂��擾����B
     *
     * @return �����O���X��
     */
    public Integer getBeforeVisitNum() {
        return this.beforeVisitNum;
    }

    /**
     * �����O���X�񐔂��Z�b�g����B
     *
     * @param beforeVisitNum �����O���X��
     */
    public void setBeforeVisitNum(Integer beforeVisitNum) {
        this.beforeVisitNum = beforeVisitNum;
    }

    /**
     * �Љ��ID���擾����B
     *
     * @return �Љ��ID
     */
    public Integer getIntroducerID() {
        return introducerID;
    }

    /**
     * �Љ��ID���Z�b�g����B
     *
     * @param note �Љ��ID
     */
    public void setIntroducerID(Integer introducerID) {
        this.introducerID = introducerID;
    }

    /**
     * �Љ�ڋq�������擾����B
     *
     * @return �Љ�ڋq����
     */
    public String getIntroducerNote() {
        return introducerNote;
    }

    /**
     * �Љ�ڋq�������Z�b�g����B
     *
     * @param introducerNote �Љ�ڋq����
     */
    public void setIntroducerNote(String introducerNote) {
        this.introducerNote = introducerNote;
    }

    /**
     * ���񗈓X���@���擾����B
     *
     * @return ���񗈓X���@
     */
    public MstFirstComingMotive getFirstComingMotive() {
        return firstComingMotive;
    }

    /**
     * ���񗈓X���@���Z�b�g����B
     *
     * @param firstComingMotive ���񗈓X���@
     */
    public void setFirstComingMotive(MstFirstComingMotive firstComingMotive) {
        this.firstComingMotive = firstComingMotive;
    }

    /**
     * ���񗈓X���@��ʂh�c���擾����B
     *
     * @return ���񗈓X���@��ʂh�c
     */
    public Integer getFirstComingMotiveClassId() {
        if (firstComingMotive == null) {
            return null;
        } else {
            return firstComingMotive.getFirstComingMotiveClassId();
        }
    }

    /**
     * ���񗈓X���@��ʂh�c���Z�b�g����B
     *
     * @param firstComingMotiveClassId ���񗈓X���@��ʂh�c
     */
    public void setFirstComingMotiveClassId(Integer firstComingMotiveClassId) {
        if (firstComingMotive == null) {
            firstComingMotive = new MstFirstComingMotive();
        }
        this.firstComingMotive.setFirstComingMotiveClassId(firstComingMotiveClassId);
    }

    /**
     * ���񗈓X���@��ʖ����擾����B
     *
     * @return ���񗈓X���@��ʖ�
     */
    public String getFirstComingMotiveName() {
        if (firstComingMotive == null) {
            return "";
        } else {
            return firstComingMotive.getFirstComingMotiveName();
        }
    }

    /**
     * ���񗈓X���@�������擾����B
     *
     * @return ���񗈓X���@����
     */
    public String getFirstComingMotiveNote() {
        return firstComingMotiveNote;
    }

    /**
     * ���񗈓X���@�������Z�b�g����B
     *
     * @param firstComingMotiveNote �Љ�ڋq����
     */
    public void setFirstComingMotiveNote(String firstComingMotiveNote) {
        this.firstComingMotiveNote = firstComingMotiveNote;
    }

    /**
     * �K��񐔂��Z�b�g����
     *
     * @param count �K���
     */
    public void setVisitCount(Long count) {
        visitCount = count;
    }

    /**
     * �K��񐔂��擾����
     *
     * @return �K���
     */
    public Long getVisitCount() {
        return visitCount;
    }

    /**
     * ���񗈓X�����擾����B
     *
     * @return ���񗈓X��
     */
    public java.util.Calendar getFirstVisitDate() {
        return firstVisitday;
    }

    /**
     * ���񗈓X�����Z�b�g����B
     *
     * @param firstVisitday ���񗈓X��
     */
    public void setFirstVisitDay(java.util.Calendar firstVisitday) {
        this.firstVisitday = firstVisitday;
    }

    /**
     * ���񗈓X�����Z�b�g����B
     *
     * @param firstVisitday ���񗈓X��
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
     * ���񗈓X�N���擾����B
     *
     * @return ���񗈓X�N
     */
    public Integer getFirstVisitYear() {
        if (firstVisitday == null) {
            return null;
        } else {
            return firstVisitday.get(java.util.Calendar.YEAR);
        }
    }

    /**
     * ���񗈓X�����擾����B
     *
     * @return ���񗈓X��
     */
    public Integer getFirstVisitMonth() {
        if (firstVisitday == null) {
            return null;
        } else {
            return firstVisitday.get(firstVisitday.MONTH) + 1;
        }
    }

    /**
     * ���񗈓X�����擾����B
     *
     * @return ���񗈓X��
     */
    public Integer getFirstVisitDay() {
        if (firstVisitday == null) {
            return null;
        } else {
            return firstVisitday.get(firstVisitday.DAY_OF_MONTH);
        }
    }

    /**
     * ���񗈓X���̕�������擾����B
     *
     * @return ���񗈓X���̕�����
     */
    public String getFirstVisitdayString() {
        if (firstVisitday == null) {
            return null;
        } else {
            return DateUtil.format(firstVisitday.getTime(), "yyyy/MM/dd");
        }
    }

    /**
     * ���񗈓X���̕�������擾����B
     *
     * @param pattern ���t�̃p�^�[��
     * @return ���񗈓X���̕�����
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
     * SOSIA�A���f�[�^���擾����B
     *
     * @return SOSIA�A���f�[�^
     */
    public MstSosiaCustomer getSosiaCustomer() {
        return msc;
    }

    /**
     * SOSIA�A���f�[�^���Z�b�g����B
     *
     * @param msc SOSIA�A���f�[�^
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

        // IVS SANG START INSERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
        // start add 20130117 nakhoa ����v
        this.setAlertMark(customer.getAlertMark());
        // start add 20130117 nakhoa ����v
        //An start add 20130125
        this.setReservationBufferTime(customer.getReservationBufferTime());
        this.setWebReservationFlag(customer.getWebReservationFlag());
        this.setQuestion_1(customer.getQuestion_1());
        this.setQuestion_2(customer.getQuestion_2());
        this.setSelectedCreditLock(customer.getSelectedCreditLock());
        //An end add 20130125
        // IVS SANG END INSERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
    }

    /**
     * �f�[�^���N���A����B
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
     * �X�^�b�t�}�X�^����A�ݒ肳��Ă���X�^�b�tID�̃f�[�^��ǂݍ��ށB
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ����
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
     * Select�����擾����B
     *
     * @return Select��
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
     * ResultSetWrapper����f�[�^��ǂݍ��ށB
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
        // IVS SANG START INSER 20131103 [gb�\�[�X]�\�[�X�}�[�W
        // start add 20130117 nakhoa ����v
        this.setAlertMark(rs.getString("alert_mark"));
        // end add 20130117 nakhoa ����v
        //Luc start add 20130125
        this.setReservationBufferTime(rs.getInt("reservation_buffer_time"));
        //Luc end add 20130125

        //An start add 20130125
        this.setQuestion_1(rs.getString("question_1"));
        this.setQuestion_2(rs.getString("question_2"));
        this.setWebReservationFlag(rs.getInt("web_reservation_flag"));
        this.setSelectedCreditLock(rs.getInt("credit_lock_flag"));

        // An end add 20130125

        // IVS SANG END INSER 20131103 [gb�\�[�X]�\�[�X�}�[�W

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
     * ResultSetWrapper����f�[�^��ǂݍ��ށB
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
        // IVS SANG START INSER 20131103 [gb�\�[�X]�\�[�X�}�[�W
        // start add 20130117 nakhoa ����v
        this.setAlertMark(rs.getString("alert_mark"));
        // end add 20130117 nakhoa ����v
        //Luc start add 20130125
        this.setReservationBufferTime(rs.getInt("reservation_buffer_time"));
        //Luc end add 20130125

        //An start add 20130125
        this.setQuestion_1(rs.getString("question_1"));
        this.setQuestion_2(rs.getString("question_2"));
        this.setWebReservationFlag(rs.getInt("web_reservation_flag"));
        this.setSelectedCreditLock(rs.getInt("credit_lock_flag"));

        //Luc start add 20160316 #49043
        
        //����\��
        try {
            this.setNextVisitDate(rs.getString("next_date"));
        }catch(Exception ex) {}
        
        //����\���
        try {
            this.setNextVisitTime(rs.getString("next_time"));
        }catch(Exception ex) {}
        
        //Luc end add 20160316 #49043
        // An end add 20130125

        // IVS SANG END INSER 20131103 [gb�\�[�X]�\�[�X�}�[�W

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
     * �ڋq�}�X�^�Ƀf�[�^��o�^����B
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ����
     */
    public boolean regist(ConnectionWrapper con) throws SQLException {
        //if(this.getCustomerID() == null || this.getCustomerID().equals(""))	return	false;

        String sql = "";
        
        // 20170801 nami edit start #18158 [gb] �V�K�ڋq�o�^�Ɏ��s�����ۂ́A�ێ����Ă���ڋqID��j������
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
            
            // �R��܂Ń��g���C
            for(int i = 0; i < 3; i++) {

                this.setCustomerID(getMaxCustomerID(con) + 1);
                sql = this.getInsertSQL();
                try {
                    if(con.executeUpdate(sql) == 1) {
                        System.out.println("�ڋq���F�V�K�o�^����");
                        return true;
                    }
                }catch(SQLException e) {
                    con.rollback();
                    System.out.println("�ڋq���F�V�K�o�^���s " + (i+1) + "���");
                }
            }
            
            this.setCustomerID(null); //�ڋqID�j��
            return false;
        }
        
        
        // �e�X�g�p�@��������
            //        SystemInfo.getLogger().log(Level.INFO, "���ڋq���o�^�J�n�� ���݂�MAX�ڋqID = {0}", new Object[]{getMaxCustomerID(con)});   //test
            //        System.out.println("���݂̓����ێ��ڋqID = " + this.getCustomerID());   //test
            //        
            //        if (isExists(con)) {
            //            System.out.println("�ڋq���F�X�V�J�n cus_id = " + this.getCustomerID());   //test
            //            sql = this.getUpdateSQL();
            //            if (con.executeUpdate(sql) == 1) {
            //                System.out.println("�ڋq���F�X�V���� cus_id = " + this.getCustomerID());   //test
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
            //                System.out.println("�ڋq���F�V�K�o�^�J�n " + (i+1) + "���, �ڋqID�Z�b�g cus_id = " +this.getCustomerID());   //test
            //
            //                sql = this.getInsertSQL();
            //                try {
            //                    if(con.executeUpdate(sql) == 1) {
            //                        System.out.println("�ڋq���F�V�K�o�^���� cus_id = " + this.getCustomerID());   //test
            //                        return true;
            //                    }
            //                }catch(SQLException e) {
            //                    con.rollback();
            //                    System.out.println("�ڋq���F�V�K�o�^���s cus_id = " + this.getCustomerID());
            //                }
            //            }
            //            
            //            this.setCustomerID(null); //�ڋqID�j��
            //            System.out.println("�������ڋqID�j��");   //test
            //            System.out.println("�ڋq���F�R��ȏ�V�K�o�^���s cus_id = " +  this.getCustomerID());   //test
            //            System.out.println("-----------------------------------------------------------");   //test
            //            return false;
            //        }
        // �e�X�g�p�@�����܂�
        // 20170801 nami edit end #18158
    }

    //IVS_LVTu start add 2017/10/12 #27848 [gb]WEB�A���F�R�t�����Ă���̂ɁA��Ɂu0�v��WEB�\�񂪓����Ă���ڋq������
    /**
     * �Y��SOSIA ID�Ŋ��ɂق��̌ڋq�ɕR���Ă���ꍇ�́A���ɕR���Ă���ڋq����SOSIA ID���폜�iNull�j����
     *
     * @return Update��
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
    //IVS_LVTu end add 2017/10/12 #27848 [gb]WEB�A���F�R�t�����Ă���̂ɁA��Ɂu0�v��WEB�\�񂪓����Ă���ڋq������

    /**
     * �X�^�b�t�}�X�^�Ƀf�[�^��o�^����B
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ����
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
     * �X�^�b�t�}�X�^����f�[�^���폜����B�i�_���폜�j
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ����
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
     * �ڋq�}�X�^�Ƀf�[�^�����݂��邩�`�F�b�N����B
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     * @return true - ���݂���
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
     * Select�����擾����B
     *
     * @return Select��
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
     * Insert�����擾����B
     *
     * @return Insert��
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
        // IVS SANG START INSERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
        // start add 20130117 nakhoa ����v
        SQL += "alert_mark,\n";
        // end add 20130117 nakhoa ����v
        // start add 20130117 An 
        SQL += "reservation_buffer_time,\n"
                + "question_1,\n"
                + "question_2,\n"
                + "web_reservation_flag,\n"
                + "credit_lock_flag,\n";
        // end add 20130117 An
        // IVS SANG END INSERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
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
        // IVS SANG START INSERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
        // start add 20130117 nakhoa ����v
        SQL += SQLUtil.convertForSQL(this.getAlertMark()) + ",\n";
        // end add 20130117 nakhoa ����v
        // start add 20130117 An
        SQL += SQLUtil.convertForSQL(this.getReservationBufferTime()) + ",\n"
                + SQLUtil.convertForSQL(this.getQuestion_1()) + ",\n"
                + SQLUtil.convertForSQL(this.getQuestion_2()) + ",\n"
                + SQLUtil.convertForSQL(this.getWebReservationFlag()) + ",\n"
                + SQLUtil.convertForSQL(this.getSelectedCreditLock()) + ",\n";
        // end add 20130117 An
        // IVS SANG START INSERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
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
     * Update�����擾����B
     *
     * @return Update��
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
                + // IVS SANG START INSERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
                // start add 20130117 nakhoa ����v
                "alert_mark = " + SQLUtil.convertForSQL(this.getAlertMark()) + ",\n"
                // end add 20130117 nakhoa ����v
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
                // IVS SANG START INSERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
                "delete_date = null\n"
                + "where	customer_id = " + SQLUtil.convertForSQL(this.getCustomerID()) + "\n";
    }

    /**
     * �폜�pUpdate�����擾����B
     *
     * @return �폜�pUpdate��
     */
    private String getDeleteSQL() {
        return "update mst_customer\n"
                + "set\n"
                + "delete_date = current_timestamp\n"
                + "where	customer_id = " + SQLUtil.convertForSQL(this.getCustomerID()) + "\n";
    }

    /**
     * �ڋqNo.���X�V����Update�����擾����B
     *
     * @return Update��
     */
    private String getUpdateCustomerNoSQL() {
        return "update mst_customer\n"
                + "set\n"
                + "customer_no = " + SQLUtil.convertForSQL(this.getCustomerNo()) + ",\n"
                + "update_date = current_timestamp\n"
                + "where	customer_id = " + SQLUtil.convertForSQL(this.getCustomerID()) + "\n";
    }

    /**
     * �ڋqID�ɑΉ�����ڋq�f�[�^�̔z����擾����B
     *
     * @param id �ڋqID
     * @return �ڋqID�ɑΉ�����ڋq�f�[�^�̔z��
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
     * �ڋqID�ɑΉ�����ڋq�f�[�^�̔z����擾����SQL�����擾����B
     *
     * @param id �ڋqID
     * @return �ڋqID�ɑΉ�����ڋq�f�[�^�̔z����擾����SQL��
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
     * �ڋq�R�[�h�ɑΉ�����ڋq�f�[�^�̔z����擾����B
     *
     * @param no �ڋq�R�[�h
     * @return �ڋq�R�[�h�ɑΉ�����ڋq�f�[�^�̔z��
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
     * �ڋq�R�[�h�ɑΉ�����ڋq�f�[�^�̔z����擾����SQL�����擾����B
     *
     * @param no �ڋq�R�[�h
     * @return �ڋq�R�[�h�ɑΉ�����ڋq�f�[�^�̔z����擾����SQL��
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
     * �d�b�ɂ������Ă����ԍ��ɑΉ�����ڋq�f�[�^�̔z����擾����B
     *
     * @param no �ԍ�
     * @return �d�b�ɂ������Ă����ԍ��ɑΉ�����ڋq�f�[�^�̔z��
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
     * �d�b�ɂ������Ă����ԍ��ɑΉ�����ڋq�f�[�^�̔z����擾����SQL�����擾����B
     *
     * @param no �ԍ�
     * @return �ԍ��ɑΉ�����ڋq�f�[�^�̔z����擾����SQL��
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
     * �Љ���l�ꗗ���擾����B
     *
     * @param con �R�l�N�V�����I�u�W�F�N�g
     * @param customerID�@�ڋqID
     * @return �d�b�ɂ������Ă����ԍ��ɑΉ�����ڋq�f�[�^�̔z��
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
     * �Љ���l�ꗗ���擾����SQL�����擾����
     *
     * @param customerID �ڋqID
     * @return �Љ���l�̈ꗗ���擾����SQL��
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

                    msg += "�����̔Ԃ̍ő�l�i" + maxNo + "�j���g�p����Ă��邽�߁A\n";
                    msg += newNo + "�Ŕ��s���܂��B\n";
                    msg += "\n";
                    msg += "���y��{�ݒ�z�ˁy��Њ֘A�z�ˁy�X�܏��o�^�z���\n";
                    msg += "�����̔Ԃ̘A�Ԍ�����" + length + "������" + (length + 1) + "���ɕύX���Ă��������B\n";
                    msg += "\n";
                    msg += "���A�Ԍ����ݒ��ύX����܂ł́A\n";
                    msg += "�@���" + newNo + "�����s����܂��̂ł����ӂ��������B\n";

                } else {

                    msg += "�����̔Ԃ̍ő�l�i" + maxNo + "�j���g�p����Ă��܂��B\n";
                    msg += "�����̔Ԃ̘A�Ԍ����͂���ȏ㑝�₷���Ƃ͂ł��܂���B\n";

                    newNo = maxNo;
                }

                MessageDialog.showMessageDialog(
                        null,
                        msg,
                        "�ڋqNo.�����̔�",
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
    // IVS SANG START INSERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
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
                return "�s��";
            case 1:
                return "��";
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

    // start add 20130117 nakhoa ����v
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
    // IVS SANG END INSERT 20131102 [gb�\�[�X]�\�[�X�}�[�W
}
