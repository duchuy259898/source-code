/*
 * SystemInfo.java
 *
 * Created on 2006/04/21, 11:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.system;


import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.io.File;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.text.NumberFormatter;

import com.geobeck.barcode.BarcodeListener;
import com.geobeck.barcode.SerialConnection;
import com.geobeck.barcode.SerialParameters;
import com.geobeck.cti.CTIListener;
import com.geobeck.sosia.pos.basicinfo.WorkTimePasswordDialog;
import com.geobeck.sosia.pos.hair.master.company.MstBeds;
import com.geobeck.sosia.pos.hair.master.company.MstResponses;
import com.geobeck.sosia.pos.master.MstSkin;
import com.geobeck.sosia.pos.master.MstUser;
import com.geobeck.sosia.pos.master.account.MstAccountSetting;
import com.geobeck.sosia.pos.master.account.MstDiscounts;
import com.geobeck.sosia.pos.master.account.MstPaymentClasses;
import com.geobeck.sosia.pos.master.company.MstAuthority;
import com.geobeck.sosia.pos.master.company.MstGroup;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.master.system.MstSetting;
import com.geobeck.sosia.pos.pointcard.AbstractCardCommunication;
import com.geobeck.sosia.pos.pointcard.enumPointcardProduct;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.sosia.pos.util.TaxUtil;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.Reconnection;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.swing.table.BevelBorderHeaderRenderer;
import com.geobeck.swing.table.LostFocusEditingStopper;
import com.geobeck.util.MoveNextField;
import com.geobeck.util.SQLUtil;
import com.geobeck.util.SelectText;

/**
 * �V�X�e�����
 * @author katagiri
 */
public class SystemInfo
{
	/**
	 * JDBC�h���C�o
	 */
	private static final String JDBC_DRIVER = "org.postgresql.Driver";

        /**
         * SOSIA_POS_system.jar ���T�[�o�ɃA�b�v����Ƃ��͂����̒l���ύX���邱�ƁI�I
         *
	 * �J���T�[�o�Ɩ{�ԃT�[�o�̐ڑ���̐؂�ւ�
         * �i 0�F�J���T�[�o �^ 1�F�{�ԃT�[�o �^ 9�F���[�J���T�[�o �j
	 */
        private static int destination_flag = 1;

        /**
	 * �T�[�o�[URL
	 */
//	private static final	String	SERVER_URL		=	"jdbc:postgresql://127.0.0.1:5432";
//	private static final	String	SERVER_URL		=	"jdbc:postgresql://192.168.10.18:5432";
//	private static final	String	SERVER_URL		=	"jdbc:postgresql://180.211.85.100:5432";

	/**
	 * ���ʃT�[�o�[URL
	 */
        //private static final	String	BASE_SERVER_URL         =	"jdbc:postgresql://210.233.66.40:5432";
        //private static final	String	BASE_SERVER_URL         =	"jdbc:postgresql://192.168.0.130:5432";
        //private static final	String	BASE_SERVER_URL         =	"jdbc:postgresql://192.168.0.112:5432";
        private static final	String	BASE_SERVER_URL         =	"jdbc:postgresql://soarer90.gb-lab.com";
	private static final	String	BASE_SERVER_URL_DEV     =	"jdbc:postgresql://192.168.0.130:5432";
	private static final	String	BASE_SERVER_URL_LOCAL   =	"jdbc:postgresql://127.0.0.1:5432";

	/**
	 * ���ʃf�[�^�x�[�X��
	 */
	private	static final	String	BASE_DATABASE	=	"pos_lic_test";

	/**
	 * ���ʃf�[�^�x�[�X���[�U�[
	 */
//	private	static final	String	BASE_DB_USER	=	"sosiapos";
	private	static final	String	BASE_DB_USER	=	"postgres";

	/**
	 * ���ʃf�[�^�x�[�X�p�X���[�h
	 */
//	private	static final	String	BASE_DB_PASS	=	"Ohbf8ww3";
	private	static final	String	BASE_DB_PASS	=	"7SoWHaDX";

	/**
	 * ���[�����M�T�[�o�[URL
	 */
	private static final	String	MAIL_SERVER_URL	=	"jdbc:postgresql://soarer01q.gb-lab.com:5432";

	/**
	 * ���[�����M�f�[�^�x�[�X��
	 */
	private	static final	String	MAIL_DATABASE	=	"mailqueue";

	/**
	 * ���[�����M�f�[�^�x�[�X���[�U�[
	 */
	private	static final	String	MAIL_DB_USER	=	"mailqueue";

	/**
	 * ���[�����M�f�[�^�x�[�X�p�X���[�h
	 */
	private	static final	String	MAIL_DB_PASS	=	"DK2yl-iR";

	/**
	 * �P�[�^�C����T�[�o�[URL
	 */
	private static final	String	MOBILE_SERVER_URL	=	"jdbc:postgresql://cnt1.gb-lab.com:5432";

	/**
	 * �P�[�^�C����f�[�^�x�[�X��
	 */
	private	static final	String	MOBILE_DATABASE	=	"WildCard";

	/**
	 * �P�[�^�C����f�[�^�x�[�X���[�U�[
	 */
	private	static final	String	MOBILE_DB_USER	=	"postgres";

	/**
	 * �P�[�^�C����f�[�^�x�[�X�p�X���[�h
	 */
	private	static final	String	MOBILE_DB_PASS	=	"7SoWHaDX";

	/**
	 * �e�X�g�f�[�^�x�[�X��
	 */
	private	static final	String	TEST_DATABASE	=	"pos_hair_dev";

	/**
	 * Logger�I�u�W�F�N�g��
	 */
	private static final	String	LOGGER_NAME		=	"com.geobeck.sosia.pos";

	/**
	 * ���O���x��
	 */
	private static final	Level	LOGGER_LEVEL	=	Level.INFO;

	/**
	 * ���O�t�@�C���p�X
	 */
	//private static final	String	LOG_FILE_PATH = getLogRoot() + "/log";

	/**
	 * ���O�t�@�C����
	 */
	private static final	String	LOG_FILE_NAME	=	"SosiaPos%g.log";

	/**
	 * ���O�t�@�C���̃T�C�Y
	 */
	private	static final	int		LOG_FILE_LIMIT	=	1000000;

	/**
	 * ���O�t�@�C����
	 */
	private	static final	int		LOG_FILE_COUNT	=	10;

	/**
	 * �e�[�u���̍s�̍���
	 */
	public	static final	int		TABLE_ROW_HEIGHT	=	26;


	/**
	 * ��Ђ̃A�C�R���̃p�X
	 */
	private static final String		ROOT_ICON_PATH		=	"/images/common/icon/root.png";
	/**
	 * �O���[�v�̃A�C�R���̃p�X
	 */
	private static final String		GROUP_ICON_PATH		=	"/images/common/icon/group.png";
	/**
	 * �X�܂̃A�C�R���̃p�X
	 */
	private static final String		SHOP_ICON_PATH		=	"/images/common/icon/shop.png";

	/**
	 * �T�[�o�[IP�A�h���X
	 */
	private static String           serverIP		=	"";

        /**
	 * �T�[�o�[�|�[�g�ԍ�
	 */
	private static String           serverPort		=	"";

	/**
	 * �f�[�^�x�[�X��
	 */
	private	static	String		database		=	TEST_DATABASE;

	/**
	 * ���[�U�[
	 */
	private	static	String		user			=	BASE_DB_USER;

	/**
	 * �p�X���[�h
	 */
	private	static	String		pass			=	BASE_DB_PASS;

	/**
	 * ���O�C���h�c
	 */
	private static	String		loginID			=	"";

	/**
	 * ���O�C���p�X���[�h
	 */
	private static	String		loginPass		=	"";

        /**
	 * WEB�f�B���N�g����
	 */
	private	static	String		directoryName		=	"";

	/**
	 * �l�`�b�A�h���X�h�c
	 */
	private static Integer		macID			=	null;

	/**
	 * ��ʂh�c
	 */
	private static Integer		typeID			=	null;

	/**
	 * �ݒ�
	 */
	private static MstSetting	setting			=	null;

	private	static MstAccountSetting accountSetting         =       null;

	/**
	 * �O���[�v
	 */
	private static MstGroup		group			=	new MstGroup();

	/**
	 * ���O�C���h�c�ɑΉ�����X�܃f�[�^
	 */
	private static MstShop		currentShop		=	new MstShop();

	/**
	 * ����
	 */
	private static MstAuthority	authority		=	null;

	/**
	 * SOSIA�A����{URL
	 */
	private static String		sosiaGearBaseURL	=	null;
	/**
	 * SOSIA�A��ID
	 */
	private static String		sosiaGearID		=	null;
	/**
	 * SOSIA�A��PASS
	 */
	private static String		sosiaGearPassWord	=	null;
	/**
	 * SOSIA�A���R�[�h
	 */
	private static String		sosiaCode               =	null;

	/**
	 * SOSIA�A���X�܃��X�g
	 */
        private static Map              sosiaGearShopList       =       null;


        private static Map              mapSosiaIDShopName       =       null;

	/**
	 * ���V�[�g�v�����^�^�C�v(0�FStar�A1�FEpson)
	 */
	private static Integer		receiptPrinterType      =	null;

	/**
	 * �\��@�\��p�t���O(0�FOFF�A1�FON)
	 */
        //IVS_LVTu start edit 2016/02/24 New request #48455
	//private static Integer		reservationOnly         =	null;
        private static Integer		systemType         =	null;

	/**
	 * Enter���������Ƃ��Ɏ��̃R���|�[�l���g�Ɉړ�������KeyListener
	 */
	private static	MoveNextField	mnf		=	null;
	/**
	 * �t�H�[�J�X�擾���Ƀe�L�X�g��S�I��������FocusListener
	 */
	private static	SelectText		st		=	null;

	/**
	 * �t�H�[�J�X�������Ȃ����Ƃ��ɕҏW���I��������N���X
	 */
	private static	LostFocusEditingStopper	lfes	=	null;

	/**
	 * �����l�pFormatter
	 */
	private static NumberFormatter	decimalFormatter	=	null;

	/**
	 * �����l�pFormatter
	 */
	private static NumberFormatter	floatFormatter		=	null;

	/**
	 * TableHeaderRenderer
	 */
	private static BevelBorderHeaderRenderer	tableHeaderRenderer	=	null;

	/**
	 * ��{�R�l�N�V����
	 */
	private static ConnectionWrapper	baseConnection		=	null;
	/**
	 * �ʃR�l�N�V����
	 */
	private static ConnectionWrapper	personalConnection	=	null;
	/**
	 * ���[���p�R�l�N�V����
	 */
	private static ConnectionWrapper	mailConnection		=	null;
	/**
	 * �P�[�^�C����擾�p�R�l�N�V����
	 */
	private static ConnectionWrapper	mobileConnection		=	null;

	/**
	 * �x���敪List
	 */
	private	static MstPaymentClasses	paymentClasses		=	null;

	/**
	 * Logger�I�u�W�F�N�g
	 */
	private static	Logger		logger				=	null;

	/**
	 * ��Ђ̃A�C�R��
	 */
	private static	ImageIcon	rootIcon			=	null;
	/**
	 * �O���[�v�̃A�C�R��
	 */
	private static	ImageIcon	groupIcon			=	null;
	/**
	 * �X�܂̃A�C�R��
	 */
	private static	ImageIcon	shopIcon			=	null;

	/**
	 * �X�L��
	 */
	private	static	MstSkin		skin				=	null;

	/**
	 * �o�[�R�[�h���[�_�[���g����
	 */
	private static	Boolean				useBarcodeReader		=	false;
	/**
	 * �o�[�R�[�h���[�_�[�̃p�����[�^
	 */
	private static	SerialParameters	barcodeReaderParameters	=	null;
	/**
	 * �o�[�R�[�h���[�_�[�̃R�l�N�V����
	 */
	private static	SerialConnection	barcodeReaderConnection	=	null;

	/**
	 * CTI���[�_�[���g����
	 */
	private static	Boolean			useCtiReader		=	false;
	/**
	 * CTI���[�_�[�̃p�����[�^
	 */
	private static	SerialParameters	ctiReaderParameters	=	null;
	/**
	 * CTI���[�_�[�̃R�l�N�V����
	 */
	private static	SerialConnection	ctiReaderConnection	=	null;
	/**
	 * CTI NEC�@AtermIT42
	 */
	private static final	    String	CTI_NEC_ATERM_IT42	=	"\n\nRING ANALOG";

	/**
	 * �|�C���g�J�[�h���[�_�E���C�^�[���ǂݍ��ݍς݂��H
	 */
	private static Boolean				isReadPointcard	=	false;

	/**
	 * �|�C���g�J�[�h���[�_�E���C�^�[���g����
	 */
	private static Boolean				usePointcard	=	false;

    /**
     * �|�C���g�J�[�h���[�_�E���C�^�[�̃p�����[�^
     */
    private static SerialParameters	pointcardParameters	=	null;

    /**
     * �|�C���g�J�[�h���[�_�E���C�^�[�̃R�l�N�V����
     */
    private static AbstractCardCommunication pointcardConnection = null;

    /**
     * �|�C���g�J�[�h���[�_�E���C�^�[�̐��i���
     */
    private static int pointcardProductId = 0;

    /**
     * �|�C���g�̏o�͋敪
     */
    private static int pointOutputType = 0;

    /**
     * �|�C���g��ʂ̓��t�󎚏����l�t���O�i0�F���X���t�A1�F����\����A2�F�ܔ������A3�F���񗈓X�ē��j
     */
    private static int pointDefaultDateType = 0;

    /**
     * CTI��ʂ̃T�C�Y�t���O�i0�F�S�̕\���A1�F�k���\���j
     */
    private static int ctiDisplaySizeType = 0;

    /**
     * DB�ڑ��������t���O
     */
    private static boolean isConnProcessing = false;

    /**
     * �N��OS����t���O
     */
    private static boolean windows = true;

    /**
    * �J�X�^�}�[�f�B�X�v���C���g����
    */
    private static Boolean useCustomerDisplay = false;
    /**
    * �J�X�^�}�[�f�B�X�v���C�̃|�[�g
    */
    private static String customerDisplayPort = null;

    // Khoa Add Start 20121024 Main��ʂ̃��S�ύX
    /**
    * ispot�X��ID
    */
    private static String ispotShopID	=	"";
    // Khoa Add End 20121024 Main��ʂ̃��S�ύX

    // start add 20130122 nakhoa SetSystem
    /**
     * nSystem
     * 0 : Default System
     * 1 : Nons System
     */
    private static int nSystem          = 0;
    // end add 20130122 nakhoa SetSystem
    /**
     * @param aWindows the windows to set
     */
     //Luc start add 20140226
    private static String posId = "";
    private static String posPassWord = "";
    private static String possSalonId = "";

    //Luc end add 20160226
    //IVS_LVTu start add 2015/05/12 New request #36645
    public static boolean flagChangeContents = false;
    //IVS_LVTu end add 2015/05/12 New request #36645
    private static MstResponses responses          = null;

    //IVS_LVTu start add 2016/05/27 New request #50223
    private static Integer UserAPI                 = null;

    private static Integer apiSalonID              = null;

    /** ���X�J���e���͎g�p�t���O(true:�g�p false:�g�p���Ȃ�)
      * 20170413 add #61376
      */
    private static boolean UseVisitKarte                = false;

    private static MstUser mstUser                    = new MstUser();

	// 20170627 GB Start Add Edit #17201 [GB���Ή�][gb] �\��\ ���x���P mst_user.use_api�擾���\�b�h
    private static HashMap<Integer, Integer> userApiMap = new HashMap<Integer, Integer>();
    // 20170627 GB End Add #17201 [GB���Ή�][gb] �\��\ ���x���P mst_user.use_api�擾���\�b�h

    public static MstUser getMstUser() {
        return mstUser;
    }

    public static void setMstUser(MstUser mstUser) {
        SystemInfo.mstUser = mstUser;
    }

    public static Integer getApiSalonID() {
        return apiSalonID;
    }

    public static void setApiSalonID(Integer apiSalonID) {
        SystemInfo.apiSalonID = apiSalonID;
    }

    public static Integer getUserAPI() {
        return UserAPI;
    }

    public static void setUserAPI(Integer UserAPI) {
        SystemInfo.UserAPI = UserAPI;
    }
    //IVS_LVTu end add 2016/05/27 New request #50223

    public static MstResponses getResponses() {
        return responses;
    }

    public static void setResponses(MstResponses responses) {
        SystemInfo.responses = responses;
    }
    // vtbphuong start add 20131212
    private static Integer databaseID = null;

    public static Integer getDatabaseID() {
        return databaseID;
    }

    public static void setDatabaseID(Integer databaseID) {
        SystemInfo.databaseID = databaseID;
    }
    // vtbphuong end add 20131212

     public static String getPosId() {
        return posId;
    }

    public static void setPosId(String posId) {
        SystemInfo.posId = posId;
    }

    public static String getPosPassWord() {
        return posPassWord;
    }

    public static void setPosPassWord(String posPassWord) {
        SystemInfo.posPassWord = posPassWord;
    }

    public static String getPossSalonId() {
        return possSalonId;
    }

    public static void setPossSalonId(String possSalonId) {
        SystemInfo.possSalonId = possSalonId;
    }
    public static void setBootOS(String os) {
        windows = (os.indexOf("Windows") != -1);
    }

    /**
     * @return the windows
     */
    public static boolean isWindows() {
        return windows;
    }

    public static String getTempDirStr() {
        return isWindows() ? "TEMP" : "TMPDIR";
    }

    public static String getLogRoot() {
        return (isWindows() ? "C:" : "/tmp") + "/sosia/pos";
    }

    private static String getLogFilePath() {
        return getLogRoot() + "/log";
    }

    // 20170413 add #61376
    public static boolean getUseVisitKarte() {
        return UseVisitKarte;
    }

    // 20170413 add #61376
    public static void setUseVisitKarte(boolean useVisitKarte) {
        SystemInfo.UseVisitKarte = (SystemInfo.getCurrentShop().getShopID() != 0 && useVisitKarte);
    }


	/**
	 * �R���X�g���N�^
	 */
	public SystemInfo()
	{
	}

        /**
         * �T�[�o�[IP�A�h���X���擾����B
         * @return �T�[�o�[IP�A�h���X
         */
        public static String getServerIP() {
            return serverIP;
        }

        /**
         * �T�[�o�[IP�A�h���X��ݒ肷��B
         * @param aServerIP �T�[�o�[IP�A�h���X
         */
        public static void setServerIP(String aServerIP) {
            serverIP = aServerIP;
        }

        /**
         * �T�[�o�[�|�[�g�ԍ����擾����
         * @return the �T�[�o�[�|�[�g�ԍ�
         */
        public static String getServerPort() {
            return serverPort;
        }

        /**
         * �T�[�o�[�|�[�g�ԍ���ݒ肷��
         * @param aServerPort �T�[�o�[�|�[�g�ԍ�
         */
        public static void setServerPort(String aServerPort) {
            serverPort = aServerPort;
        }

        public static String getServerUrl() {
            return "jdbc:postgresql://" + getServerIP() + ":" + getServerPort();
        }

        public static String getBaseServerUrl() {

            String result = "";

            switch (destination_flag) {
                case 0:
                    result = BASE_SERVER_URL_DEV;
                    break;
                case 1:
                    result = BASE_SERVER_URL;
                    break;
                case 9:
                    result = BASE_SERVER_URL_LOCAL;
                    break;
            }

            return result;
        }

	/**
	 * �f�[�^�x�[�X�����擾����B
	 * @return �f�[�^�x�[�X��
	 */
	public static String getDatabase()
	{
		return database;
	}

	/**
	 * �f�[�^�x�[�X����ݒ肷��B
	 * @param aDatabase �f�[�^�x�[�X��
	 */
	public static void setDatabase(String aDatabase)
	{
		database = aDatabase;
	}

	/**
	 * ���[�U�[���擾����B
	 * @return ���[�U�[
	 */
	public static String getUser()
	{
		return user;
	}

	/**
	 * ���[�U�[��ݒ肷��B
	 * @param aUser ���[�U�[
	 */
	public static void setUser(String aUser)
	{
		user = aUser;
	}

	/**
	 * �p�X���[�h���擾����B
	 * @return �p�X���[�h
	 */
	public static String getPass()
	{
		return pass;
	}

	/**
	 * �p�X���[�h��ݒ肷��B
	 * @param aPass �p�X���[�h
	 */
	public static void setPass(String aPass)
	{
		pass = aPass;
	}

	/**
	 * ���O�C���h�c��ݒ肷��B
	 * @param aLoginID ���O�C���h�c
	 */
	public static void setLoginID(String aLoginID)
	{
		loginID = aLoginID;
	}

	/**
	 * ���O�C���h�c���擾����B
	 * @return ���O�C���h�c
	 */
	public static String getLoginID()
	{
		return loginID;
	}


        // Khoa Add Start 20121024 Main��ʂ̃��S�ύX
        /**
	 * ispot�X��ID��ݒ肷��B
	 * @param aLoginID ispot�X��ID
	 */
        public static void setIspotShopID(String aIspotShopID)
	{
		ispotShopID = aIspotShopID;
	}

        /**
	 * ispot�X��ID��ݒ肷��B
	 * @param aLoginID ispot�X��ID
	 */
	public static String getIspotShopID()
	{
		return ispotShopID;
	}

        // Khoa Add End 20121024 Main��ʂ̃��S�ύX
        // start add 20130122 nakhoa SetSystem
        /**
	 * nSystem��ݒ肷��B
	 * @param nSystem
	 */
        public static void setNSystem(int anSystem)
	{
		nSystem = anSystem;
	}

        /**
	 * nSystem��ݒ肷��B
	 * @param nSystem
	 */
	public static int getNSystem()
	{
		return nSystem;
	}
        // end add 20130122 nakhoa SetSystem

	/**
	 * ���O�C���p�X���[�h��ݒ肷��B
	 * @param aLoginPass ���O�C���p�X���[�h
	 */
	public static void setLoginPass(String aLoginPass)
	{
		loginPass = aLoginPass;
	}

	/**
	 * ���O�C���p�X���[�h���擾����B
	 * @return ���O�C���p�X���[�h
	 */
	public static String getLoginPass()
	{
		return loginPass;
	}

	/**
	 * WEB�f�B���N�g������ݒ肷��B
	 * @param aDirectoryName WEB�f�B���N�g����
	 */
	public static void setDirectoryName(String aDirectoryName)
	{
		directoryName = aDirectoryName;
	}

	/**
	 * WEB�f�B���N�g�������擾����B
	 * @return WEB�f�B���N�g����
	 */
	public static String getDirectoryName()
	{
		return directoryName;
	}

	/**
	 * �l�`�b�A�h���X�h�c���擾����B
	 * @return �l�`�b�A�h���X�h�c
	 */
	public static Integer getMacID()
	{
		return macID;
	}

	/**
	 * �l�`�b�A�h���X�h�c���Z�b�g����B
	 * @param aMacID �l�`�b�A�h���X�h�c
	 */
	public static void setMacID(Integer aMacID)
	{
		macID = aMacID;
	}

	/**
	 * SOSIA�A�����\�����擾����
	 *   �A���\�̗L���͘A�����{URL���ݒ肳��Ă��邩�Ŕ��f����
	 * @return SOSIA�A�����\��
	 */
	public static boolean isSosiaGearEnable()
	{
		if( sosiaGearBaseURL == null || sosiaGearBaseURL.length() == 0 ) return false;
		return true;
	}

	/**
	 * SOSIA�A���p��{URL���擾����
	 * @return SOSIA�A����{URL
	 */
	public static String getSosiaGearBaseURL()
	{
		return sosiaGearBaseURL;
	}

	/**
	 * SOSIA�A���p��{URL���Z�b�g����B
	 * @param setSosiaGearBaseURL SOSIA�A���p��{URL
	 */
	public static void setSosiaGearBaseURL( String setSosiaGearBaseURL )
	{
		sosiaGearBaseURL = setSosiaGearBaseURL;
	}

	/**
	 * SOSIA�A���pID���擾����
	 * @return SOSIA�A��ID
	 */
	public static String getSosiaGearID()
	{
		return sosiaGearID;
	}

	/**
	 * SOSIA�A���pID���Z�b�g����B
	 * @param setSosiaGearID SOSIA�A���pID
	 */
	public static void setSosiaGearID( String setSosiaGearID )
	{
		sosiaGearID = setSosiaGearID;
	}

	/**
	 * SOSIA�A���pPassWord���擾����
	 * @return SOSIA�A��PassWord
	 */
	public static String getSosiaGearPassWord()
	{
		return sosiaGearPassWord;
	}

	/**
	 * SOSIA�A���pPassWord���Z�b�g����B
	 * @param setSosiaGearPassWord SOSIA�A���pPassWord
	 */
	public static void setSosiaGearPassWord( String setSosiaGearPassWord )
	{
		sosiaGearPassWord = setSosiaGearPassWord;
	}

	/**
	 * SOSIA�A���p�R�[�h���擾����
	 * @return SOSIA�A���R�[�h
	 */
	public static String getSosiaCode()
	{
		return sosiaCode;
	}

	/**
	 * SOSIA�A���p�R�[�h���Z�b�g����B
	 * @param setSosiaCode SOSIA�A���p�R�[�h
	 */
	public static void setSosiaCode( String setSosiaCode )
	{
		sosiaCode = setSosiaCode;
	}

	/**
	 * ���V�[�g�v�����^�^�C�v���擾����
	 * @return ���V�[�g�v�����^�^�C�v
	 */
        public static Integer getReceiptPrinterType() {
            return receiptPrinterType;
        }

	/**
	 * ���V�[�g�v�����^�^�C�v���Z�b�g����B
	 * @param setReceiptPrinterType ���V�[�g�v�����^�^�C�v
	 */
        public static void setReceiptPrinterType(Integer setReceiptPrinterType) {
            receiptPrinterType = setReceiptPrinterType;
        }

	/**
	 * �\��@�\��p�t���O���擾����
	 * @return �\��@�\��p�t���O
	 */
//        public static Boolean isReservationOnly() {
//            return reservationOnly.equals(1);
//        }
        public static Integer getSystemType() {
            return systemType;
        }

	/**
	 * �\��@�\��p�t���O���Z�b�g����B
	 * @param setReservationOnly �\��@�\��p�t���O
	 */
        public static void setReservationOnly(Integer setReservationOnly) {
            systemType = setReservationOnly;
        }
        //IVS_LVTu end edit 2016/02/24 New request #48455

	/**
	 * �R�l�N�V�������擾����B�i���ʃf�[�^�x�[�X�p�j
	 * @return �R�l�N�V�����i���ʃf�[�^�x�[�X�p�j
	 */
	public static ConnectionWrapper getBaseConnection()
	{
		Reconnection reCon = new Reconnection();
                if(baseConnection == null || baseConnection.getConnection() == null)
		{
			baseConnection = new ConnectionWrapper(
                            SystemInfo.JDBC_DRIVER,
                            SystemInfo.getBaseServerUrl() + "/" + SystemInfo.BASE_DATABASE,
                            SystemInfo.BASE_DB_USER,
                            SystemInfo.BASE_DB_PASS);

			baseConnection.open();
		}

                if (!isConnProcessing) {
                    isConnProcessing = true;
                    do {
                        //���Ă���ꍇ�͊J������
                        baseConnection = reCon.tryReconnection(
                            baseConnection,
                            SystemInfo.JDBC_DRIVER,
                            SystemInfo.getBaseServerUrl(),
                            SystemInfo.BASE_DATABASE,
                            SystemInfo.BASE_DB_USER,
                            SystemInfo.BASE_DB_PASS,
                            SystemInfo.getLogger());

                    } while (baseConnection == null && !isSystemExit());
                }

                isConnProcessing = false;

		return	baseConnection;
	}

	/**
	 * �R�l�N�V�������擾����B�i�ʃf�[�^�x�[�X�p�j
	 * @return �R�l�N�V�����i�ʃf�[�^�x�[�X�p�j
	 */
	public static ConnectionWrapper getConnection()
	{
// info("call getConnection()");
		Reconnection reCon = new Reconnection();
        if(personalConnection == null || personalConnection.getConnection() == null)
		{
			personalConnection = new ConnectionWrapper(
                            SystemInfo.JDBC_DRIVER,
                            SystemInfo.getServerUrl() + "/" + SystemInfo.getDatabase(),
                            SystemInfo.getUser(),
                            SystemInfo.getPass());
			personalConnection.open();
		}
		if (!isConnProcessing) {
            isConnProcessing = true;
            do {
// info("call getConnection() - open");
                //���Ă���ꍇ�͊J������
                personalConnection = reCon.tryReconnection(
                            personalConnection,
                            SystemInfo.JDBC_DRIVER,
                            SystemInfo.getServerUrl(),
                            SystemInfo.getDatabase(),
                            SystemInfo.getUser(),
                            SystemInfo.getPass(),
                            SystemInfo.getLogger());
			} while (personalConnection == null && !isSystemExit());
        }
		isConnProcessing = false;
		return	personalConnection;
	}

	/**
	 * �R�l�N�V�������擾����B�i���[���p�j
	 * @return �R�l�N�V�����i���[���p�j
	 */
	public static ConnectionWrapper getMailConnection()
	{
		Reconnection reCon = new Reconnection();
                System.out.println(" SystemInfo.JDBC_Driver :\n" + SystemInfo.JDBC_DRIVER +"\n"+
                                        SystemInfo.MAIL_SERVER_URL + "/" + SystemInfo.MAIL_DATABASE+"\n"+
                                        SystemInfo.MAIL_DB_USER+"\n"+
                                        SystemInfo.MAIL_DB_PASS+"\n" );
                 //nhanvt start add 20150226 Bug #35221
                if(mailConnection != null){
                closeConnection(mailConnection);
                mailConnection = null;
                }
                //nhanvt end add 20150226 Bug #35221
                if(mailConnection == null || mailConnection.getConnection() == null)
		{
			mailConnection = new ConnectionWrapper(
                            SystemInfo.JDBC_DRIVER,
                            SystemInfo.MAIL_SERVER_URL + "/" + SystemInfo.MAIL_DATABASE,
                            SystemInfo.MAIL_DB_USER,
                            SystemInfo.MAIL_DB_PASS);

			mailConnection.open();
		}
                //nhanvt start edit 20141229 New request #34634
//                if (!isConnProcessing) {
//                    isConnProcessing = true;
//                    do {
//                        //���Ă���ꍇ�͊J������
//                        mailConnection = reCon.tryReconnection(
//                            mailConnection,
//                            SystemInfo.JDBC_DRIVER,
//                            SystemInfo.MAIL_SERVER_URL,
//                            SystemInfo.MAIL_DATABASE,
//                            SystemInfo.MAIL_DB_USER,
//                            SystemInfo.MAIL_DB_PASS,
//                            SystemInfo.getLogger());
//
//                    } while (mailConnection == null && !isSystemExit());
//                }
//
//                isConnProcessing = false;
            //nhanvt end edit 20141229 New request #34634
		return	mailConnection;
	}

	/**
	 * �R�l�N�V�������擾����B�i�P�[�^�C����擾�p�j
	 * @return �R�l�N�V�����i�P�[�^�C����擾�p�j
	 */
	public static ConnectionWrapper getMobileConnection()
	{
		Reconnection reCon = new Reconnection();
                System.out.println(" SystemInfo.JDBC_Driver :\n" + SystemInfo.JDBC_DRIVER +"\n"+
                                        SystemInfo.MOBILE_SERVER_URL + "/" + SystemInfo.MOBILE_DATABASE+"\n"+
                                        SystemInfo.MOBILE_DB_USER+"\n"+
                                        SystemInfo.MOBILE_DB_PASS+"\n" );
                if(mobileConnection == null || mobileConnection.getConnection() == null)
		{
			mobileConnection = new ConnectionWrapper(
                            SystemInfo.JDBC_DRIVER,
                            SystemInfo.MOBILE_SERVER_URL + "/" + SystemInfo.MOBILE_DATABASE,
                            SystemInfo.MOBILE_DB_USER,
                            SystemInfo.MOBILE_DB_PASS);

			mobileConnection.open();
		}

                if (!isConnProcessing) {
                    isConnProcessing = true;
                    do {
                        //���Ă���ꍇ�͊J������
                        mobileConnection = reCon.tryReconnection(
                            mobileConnection,
                            SystemInfo.JDBC_DRIVER,
                            SystemInfo.MOBILE_SERVER_URL,
                            SystemInfo.MOBILE_DATABASE,
                            SystemInfo.MOBILE_DB_USER,
                            SystemInfo.MOBILE_DB_PASS,
                            SystemInfo.getLogger());

                    } while (mobileConnection == null && !isSystemExit());
                }

                isConnProcessing = false;

		return	mobileConnection;
	}

	/**
	 * �R�l�N�V���������
	 */
	public static void closeConnection()
	{
		closeConnection(baseConnection);
		closeConnection(personalConnection);
		closeConnection(mailConnection);
		closeConnection(mobileConnection);

		baseConnection = null;
		personalConnection = null;
		mailConnection = null;
		mobileConnection = null;
	}

	/**
	 * �R�l�N�V���������
	 * @param con �R�l�N�V����
	 */
	private static void closeConnection(ConnectionWrapper con)
	{
		if(con != null)
		{
			try
			{
				con.close();
			}
			catch(SQLException e)
			{
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}

			con	=	null;
		}
	}


	/**
	 * �V�X�e�����t���擾����B
	 * @return �V�X�e�����t
	 */
	public static java.util.Date getSystemDate()
	{
		java.util.Date temp = null;

		try
		{
                    //�R�l�N�V�������擾
                    ConnectionWrapper con = SystemInfo.getBaseConnection();
                    //�T�[�o�[�̃V�X�e�����t���擾
                    ResultSetWrapper rs = con.executeQuery("select current_timestamp as sysdate");
                    if(rs.next())
                    {
                        temp = rs.getDate("sysdate");
                    }
		}
		catch(SQLException e)
		{
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}

		//���t���擾�ł��Ă��Ȃ��ꍇ
		if(temp == null)
		{
                    //���[�J���̃V�X�e�����t���擾
                    temp = Calendar.getInstance().getTime();
		}

		return temp;
	}

	/**
	 * �ݒ�����擾����
	 * @return �ݒ���
	 */
	public static MstSetting getSetteing()
	{
		if(setting == null)
		{
			setting	=	new MstSetting();

			try
			{
				setting.load(SystemInfo.getConnection());
			}
			catch(Exception e)
			{
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}

		return	setting;
	}

	/**
	 * ��v�ݒ�����Z�b�g����
	 * @param accountSetting ��v�ݒ���
	 */
	public static void setAccountSetting(MstAccountSetting setting)
	{
                accountSetting = setting;
	}

	/**
	 * ��v�ݒ�����擾����
	 * @return ��v�ݒ���
	 */
	public static MstAccountSetting getAccountSetting()
	{
		if(accountSetting == null)
		{
			accountSetting = new MstAccountSetting();

                        try
                        {
                                accountSetting.load(SystemInfo.getConnection());
                        }
                        catch(Exception e)
                        {
                                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                        }
		}

		return accountSetting;
	}

	/**
	 * �O���[�v���擾����
	 * @return �O���[�v
	 */
	public static MstGroup getGroup()
	{
		return group;
	}

	/**
	 * �O���[�v��ݒ肷��
	 * @param aGroup �O���[�v
	 */
	public static void setGroup(MstGroup aGroup)
	{
		group = aGroup;
	}

	/**
	 * ���O�C���h�c�ɑΉ�����X�܃f�[�^���擾����B
	 * @return ���O�C���h�c�ɑΉ�����X�܃f�[�^
	 */
	public static MstShop getCurrentShop()
	{
		if(currentShop != null && currentShop.getShopName().equals(""))
		{
			try
			{
				currentShop.load(SystemInfo.getConnection());
			}
			catch(Exception e)
			{
				SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}

		return	currentShop;
	}

	/**
	 * �ŐV�̃��O�C���h�c�ɑΉ�����X�܃f�[�^���X�V����B
	 */
	public static void refleshCurrentShop()
	{
		try
		{
			currentShop.load(SystemInfo.getConnection());
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage());
		}
	}

	/**
	 * �ŐV�̃��O�C���h�c�ɑΉ�����O���[�v���͓X�܃f�[�^���擾����
	 * @return �ŐV�̃��O�C���h�c�ɑΉ�����O���[�v���͓X�܃f�[�^
	 */
	public static Object getCurrentTarget()
	{
		//�O���[�v
		if(isGroup())
		{
			return	getGroup();
		}
		//�X��
		else
		{
			return	getCurrentShop();
		}
	}


	/**
	 * �X�ܑI��p�R���{�{�b�N�X������������
	 * @param combobox �R���{�{�b�N�X
	 * @param type 1�F�O���[�v�̂�
	 * 2�F�X�܂̂�
	 * 3�F�O���[�v�ƓX��
	 */
	public static void initGroupShopComponents(JComboBox combobox, int type)
	{
		//�O���[�v
		if(SystemInfo.getCurrentShop().getShopID() == 0)
		{
			if(type == 1 || type == 3)
			{
				combobox.addItem(SystemInfo.getGroup());
			}

			SystemInfo.getGroup().addGroupDataToJComboBox(combobox, type);
		}
		//�X��
		else
		{
			if(type == 1)
			{
				combobox.addItem(SystemInfo.getGroup());
			}
			else if(type == 2 || type == 3)
			{
				combobox.addItem(SystemInfo.getCurrentShop());
			}

			if(0 < combobox.getItemCount())
			{
				combobox.setSelectedIndex(0);
			}
		}
	}


	/**
	 * �ڋq�o�^�̓X�ܑI��p�R���{�{�b�N�X������������
	 * @param combobox �R���{�{�b�N�X
	 * @param type 1�F�O���[�v�̂�
	 * 2�F�X�܂̂�
	 * 3�F�O���[�v�ƓX��
	 */
	public static void initGropuShopComponentsForCustomer(JComboBox combobox, int type)
	{
		//�ڋq���L
		if(SystemInfo.getSetteing().isShareCustomer())
		{
			MstShop	ms	=	new MstShop();
			ms.setShopID(0);
			combobox.addItem(ms);
			combobox.setSelectedIndex(0);
		}
		else
		{
			SystemInfo.initGroupShopComponents(combobox, type);
		}
	}

	/**
	 * �X�^�b�t�I��p�R���{�{�b�N�X������������
	 * @param combobox �R���{�{�b�N�X
	 */
	public static void initStaffComponent(JComboBox combobox)
	{
		try
		{
			//�O���[�v
			if(SystemInfo.getCurrentShop().getShopID() == 0)
			{
				MstStaff.addStaffDataToJComboBox(SystemInfo.getConnection(),
						combobox, SystemInfo.getGroup().getShopIDListAll());
			}
			//�X��
			else
			{
				MstStaff.addStaffDataToJComboBox(SystemInfo.getConnection(),
						combobox, SystemInfo.getCurrentShop().getShopID());
			}
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage());
		}
	}

	/**
	 * �����\������X�܂��擾����B
	 * @return �����\������X��
	 */
	public static MstShop getFirstShop()
	{
		if(SystemInfo.getCurrentShop().getShopID() == 0)
		{
			if(0 < SystemInfo.getGroup().getShops().size())
				return	SystemInfo.getGroup().getShops().get(0);
			else
				return	new MstShop();
		}
		else
		{
			return	SystemInfo.getCurrentShop();
		}
	}

	/**
	 * ���O�C�����[�U�[���O���[�v���ǂ������擾����B
	 * @return true�F�O���[�v
	 * fales�F�X��
	 */
	public static boolean isGroup()
	{
		return	(SystemInfo.getCurrentShop().getShopID() == 0);
	}

	/**
	 * �x���敪�̃��X�g���擾����
	 * @return �x���敪�̃��X�g
	 */
	public static MstPaymentClasses getPaymentClasses()
	{
		if(paymentClasses == null)
		{
			reloadPaymentClasses();
		}

		return paymentClasses;
	}

	/**
	 * �x���敪�̃��X�g�Ď擾����
	 */
	public static void reloadPaymentClasses()
	{
		paymentClasses	=	new MstPaymentClasses();

		try
		{
			paymentClasses.loadClasses(SystemInfo.getConnection());
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage());
		}
	}

	/**
	 * ���O�C�����[�U�[�����擾����B
	 * @return ���O�C�����[�U�[��
	 */
	public static String getName()
	{
		if(SystemInfo.isGroup())
		{
			return	SystemInfo.getGroup().getGroupName();
		}
		else
		{
			return	SystemInfo.getCurrentShop().getShopName();
		}
	}

	/**
	 * ���O�C�����[�U�[�̃��[���A�h���X���擾����B
	 * @return ���O�C�����[�U�[�̃��[���A�h���X
	 */
	public static String getMailAddress()
	{
		//�O���[�v
		if(SystemInfo.isGroup())
		{
			return	SystemInfo.getGroup().getMailAddress();
		}
		//�X��
		else
		{
			return	SystemInfo.getCurrentShop().getMailAddress();
		}
	}

	/**
	 * �������擾����B
	 * @return ����
	 */
	public static MstAuthority getAuthority()
	{
		if(authority == null)
		{
			authority	=	new MstAuthority();
			authority.setGroup(getGroup());

			//��Ђ̏ꍇ
			if(getGroup().getGroupID() == 1 && getCurrentShop().getShopID() == 0)
			{
				for(int i = 0; i < MstAuthority.AUTHORYTY_MAX; i ++)
					authority.setAuthoryty(i, true);
			}
			else
			{
				authority.setShop(getCurrentShop());

				try
				{
					authority.load(SystemInfo.getConnection());
				}
				catch(Exception e)
				{
					SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			}
		}
		return authority;
	}

	/**
	 * �������`�F�b�N����B
	 * @param index �`�F�b�N���錠���̃C���f�b�N�X
	 * @return true�FOK
	 */
	public static boolean checkAuthority(int index)
	{
            if (0 <= index && index < MstAuthority.AUTHORYTY_MAX && getAuthority().getAuthoryty(index)) {
                return true;
            } else {
                return false;
            }
	}

	/**
	 * �p�X���[�h������`�F�b�N����B
	 * @param index �`�F�b�N����p�X���[�h����̃C���f�b�N�X
	 * @return true�FOK
	 */
	public static boolean checkAuthorityPassword(int index)
	{
            if (checkAuthority(index)) {
                if (WorkTimePasswordDialog.isAuthPassword()) {
                    return true;
                } else {
                    return false;
                }
            }
            return true;
	}

	/**
	 * ��ʂh�c���擾����B
	 * @return ��ʂh�c
	 */
	public static Integer getTypeID()
	{
		return typeID;
	}

	/**
	 * ��ʂh�c���Z�b�g����B
	 * @param aTypeID ��ʂh�c
	 */
	public static void setTypeID(Integer aTypeID)
	{
		typeID = aTypeID;
	}

	/**
	 * Enter���������Ƃ��Ɏ��̃R���|�[�l���g�Ɉړ�������KeyListener���擾����B
	 * @return Enter���������Ƃ��Ɏ��̃R���|�[�l���g�Ɉړ�������KeyListener
	 */
	public static MoveNextField getMoveNextField()
	{
		if(SystemInfo.mnf == null)
		{
			SystemInfo.mnf	=	new MoveNextField();
		}
		return	SystemInfo.mnf;
	}

	/**
	 * �t�H�[�J�X�擾���Ƀe�L�X�g��S�I��������FocusListener���擾����B
	 * @return �t�H�[�J�X�擾���Ƀe�L�X�g��S�I��������FocusListener
	 */
	public static SelectText getSelectText()
	{
		if(SystemInfo.st == null)
		{
			SystemInfo.st	=	new SelectText();
		}
		return	SystemInfo.st;
	}

	/**
	 * �t�H�[�J�X�������Ȃ����Ƃ��ɕҏW���I��������N���X���擾����
	 * @return �t�H�[�J�X�������Ȃ����Ƃ��ɕҏW���I��������N���X
	 */
	public static LostFocusEditingStopper getLostFocusEditingStopper()
	{
		if(SystemInfo.lfes == null)
		{
			SystemInfo.lfes	=	new LostFocusEditingStopper();
		}
		return lfes;
	}

	/**
	 * �����l�pFormatter���擾����
	 * @return �����l�pFormatter
	 */
	public static NumberFormatter getDecimalFormatter()
	{
		if(decimalFormatter == null)
		{
			decimalFormatter	=	new NumberFormatter(
						new DecimalFormat("#,###"));
		}
		return decimalFormatter;
	}

	/**
	 * �����l�pFormatter���擾����
	 * @return �����l�pFormatter
	 */
	public static NumberFormatter getFloatFormatter()
	{
		if(floatFormatter == null)
		{
			floatFormatter	=	new NumberFormatter(
						new DecimalFormat("#,###.##"));
		}
		return floatFormatter;
	}

	/**
	 * Logger�I�u�W�F�N�g������������
	 */
	private static void initLogger()
	{
		try
		{
			logger	=	Logger.getLogger(SystemInfo.LOGGER_NAME);

			logger.setLevel(LOGGER_LEVEL);

			//���O���o�͂���f�B���N�g�����쐬
			if(!SystemInfo.createLogDirectory()) return;

			//String logPath = SystemInfo.LOG_FILE_PATH + "/" + SystemInfo.LOG_FILE_NAME;
			String logPath = getLogFilePath() + "/" + SystemInfo.LOG_FILE_NAME;

			//FileHandler���쐬
			FileHandler fh = new FileHandler(logPath,
					SystemInfo.LOG_FILE_LIMIT,
					SystemInfo.LOG_FILE_COUNT,
					true);

			// Formatter��ݒ�
			fh.setFormatter(new SimpleFormatter());

			// ���O�̏o�͐��ǉ�
			logger.addHandler(fh);

			LogManager.getLogManager().addLogger(logger);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * ���O���o�͂���f�B���N�g�����Ȃ���΁A�쐬����B
	 * @return �f�B���N�g�������݂��邩�A�쐬�ł����true
	 */
	private static boolean createLogDirectory()
	{
		//File dir = new File(SystemInfo.LOG_FILE_PATH);
		File dir = new File(getLogFilePath());

		//�f�B���N�g�������݂���ꍇ
		if(dir.exists())
			return	true;
		else
			//�f�B���N�g�����쐬
			return	dir.mkdirs();
	}

	/**
	 * Logger�I�u�W�F�N�g���擾����B
	 * @return Logger�I�u�W�F�N�g
	 */
	public static Logger getLogger()
	{
		if(logger == null)
		{
			SystemInfo.initLogger();
		}

		return	logger;
	}

	/**
	 * ��Ђ̃A�C�R�����擾����
	 * @return ��Ђ̃A�C�R��
	 */
	public static ImageIcon getRootIcon()
	{
		if(rootIcon == null)
		{
			rootIcon	=	new ImageIcon(
					SystemInfo.class .getResource(
							SystemInfo.ROOT_ICON_PATH));
		}

		return rootIcon;
	}

	/**
	 * �O���[�v�̃A�C�R�����擾����
	 * @return �O���[�v�̃A�C�R��
	 */
	public static ImageIcon getGroupIcon()
	{
		if(groupIcon == null)
		{
			groupIcon	=	new ImageIcon(
					SystemInfo.class .getResource(
							SystemInfo.GROUP_ICON_PATH));
		}

		return groupIcon;
	}

	/**
	 * �X�܂̃A�C�R�����擾����
	 * @return �X�܂̃A�C�R��
	 */
	public static ImageIcon getShopIcon()
	{
		if(shopIcon == null)
		{
			shopIcon	=	new ImageIcon(
					SystemInfo.class .getResource(
							SystemInfo.SHOP_ICON_PATH));
		}

		return shopIcon;
	}

	/**
	 * �R���|�[�l���g��Ƀ}�E�X�J�[�\����������Ƃ��ɁA
	 * �}�E�X�J�[�\������̃J�[�\���ɕύX����C�x���g���X�i�[��ǉ�����
	 * @param component �R���|�[�l���g
	 */
	public static void addMouseCursorChange(final Component component)
	{
            component.addMouseListener(new java.awt.event.MouseAdapter()
            {
                public void mouseEntered(java.awt.event.MouseEvent evt)
                {
                                    if(component.isEnabled() && component.isVisible())
                                    {
                                            component.setCursor(new Cursor(Cursor.HAND_CURSOR));
                                    }
                }

                public void mouseExited(java.awt.event.MouseEvent evt)
                {
                    component.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });
	}

	/**
	 * �I������Ă���X�L�����擾����
	 * @return �I������Ă���X�L��
	 */
	public static MstSkin getSkin()
	{
		if(skin == null)
		{
			skin	=	new MstSkin();
		}

		if(skin != null && skin.getPackageName().equals(""))
		{
			if(skin.getSkinID() != null)
			{
				try
				{
					skin.load(getBaseConnection());
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		return	skin;
	}

	/**
	 * �I������Ă���X�L���̃p�b�P�[�W���擾����
	 * @return �I������Ă���X�L���̃p�b�P�[�W
	 */
	public static String getSkinPackage()
	{
		return getSkin().getPackageName();
	}

	/**
	 * ���j���[�̐F���擾����
	 * @return ���j���[�̐F
	 */
	public static Color getMenuColor()
	{
		return getSkin().getMenuColor();
	}

	/**
	 * �e�[�u���w�b�_�[�̃����_���[���擾����
	 * @return �e�[�u���w�b�_�[�̃����_���[
	 */
	public static BevelBorderHeaderRenderer getTableHeaderRenderer()
	{
		if(tableHeaderRenderer == null)
		{
			tableHeaderRenderer	=	new BevelBorderHeaderRenderer(
					new Color(204,204,204));
			//		getSkin().getTableColor());
			//System.out.println("getSkin().getTableColor():"+getSkin().getTableColor());
		}
		return tableHeaderRenderer;
	}


	/**
	 * �ŗ����擾����
	 * @param date �ΏۂƂȂ���t
	 * @return �ŗ�
	 */
	public static Double getTaxRate(java.util.Date date)
	{
		Double		rate	=	0d;

		try
		{
			ConnectionWrapper	con	=	SystemInfo.getConnection();

			ResultSetWrapper	rs	=	con.executeQuery(
					"select get_tax_rate(" +
					(date == null ? "current_date" :
					String.format("to_date('%1$tY/%1$tm/%1$td', 'YYYY/MM/DD')", date)) + ") as tax_rate");

			if(rs.next())
			{
				rate	=	rs.getDouble("tax_rate");
			}

			rs.close();
			rs	=	null;
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}

		return	rate;
	}

	/**
	 * path�̉摜��ImageIcon���擾����
	 * @param path �摜�̃p�X
	 * @return path�̉摜��ImageIcon
	 */
	public static javax.swing.ImageIcon getImageIcon(String path)
	{
		return	new javax.swing.ImageIcon(
				SystemInfo.class.getResource(
						"/images/" + SystemInfo.getSkinPackage() + path));
	}

	/**
	 * �o�[�R�[�h���[�_�[���g����
	 * @return true�F�g��
	 */
	public static Boolean isUseBarcodeReader()
	{
		// #18069
		if (useBarcodeReader==null)loadBarcodeReaderInfo();
		return useBarcodeReader;
	}

	/**
	 * �o�[�R�[�h���[�_�[���g������ݒ肷��
	 * @param aUseBarcodeReader true�F�o�[�R�[�h���[�_�[���g��
	 */
	public static void setUseBarcodeReader(Boolean aUseBarcodeReader)
	{
		useBarcodeReader = aUseBarcodeReader;
	}

	/**
	 * CTI���[�_�[���g����
	 * @return true�F�g��
	 */
	public static Boolean isUseCtiReader()
	{
		return useCtiReader;
	}
	/**
	 * CTI���[�_�[���g������ݒ肷��
	 * @param aUseCTIReader true�FCTI���[�_�[���g��
	 */
	public static void setUseCtiReader(Boolean aUseCtiReader)
	{
		useCtiReader = aUseCtiReader;
	}
	/**
	 * CTI���[�_�[�̃p�����[�^���擾����
	 * @return CTI���[�_�[�̃p�����[�^
	 */
	public static SerialParameters getCtiReaderParameters()
	{
		return ctiReaderParameters;
	}

	/**
	 * CTI���[�_�[�̃p�����[�^��ݒ肷��
	 * @param aCtieReaderParameters �o�[�R�[�h���[�_�[�̃p�����[�^
	 */
	public static void setCtiReaderParameters(SerialParameters aCtiReaderParameters)
	{
		ctiReaderParameters = aCtiReaderParameters;
	}

	/**
	 * Cti�̃R�l�N�V�������擾����
	 * @return Cti���[�_�[�̃R�l�N�V����
	 */
	public static SerialConnection getCtiReaderConnection()
	{
		return ctiReaderConnection;
	}

	/**
	 * �o�[�R�[�h���[�_�[�̃p�����[�^���擾����
	 * @return �o�[�R�[�h���[�_�[�̃p�����[�^
	 */
	public static SerialParameters getBarcordReaderParameters()
	{
		return barcodeReaderParameters;
	}

	/**
	 * �o�[�R�[�h���[�_�[�̃p�����[�^��ݒ肷��
	 * @param aBarcodeReaderParameters �o�[�R�[�h���[�_�[�̃p�����[�^
	 */
	public static void setBarcordReaderParameters(SerialParameters aBarcodeReaderParameters)
	{
		barcodeReaderParameters = aBarcodeReaderParameters;
	}

	/**
	 * �o�[�R�[�h���[�_�[�̃R�l�N�V�������擾����
	 * @return �o�[�R�[�h���[�_�[�̃R�l�N�V����
	 */
	public static SerialConnection getBarcodeReaderConnection()
	{
		return barcodeReaderConnection;
	}

	/**
	 * �o�[�R�[�h���[�_�[�̃R�l�N�V�������J��
	 * @param parent �o�[�R�[�h���X�i�[
	 * @return true�F����
	 */
	public static boolean openBarcodeReaderConnection(
			BarcodeListener parent)

	{
		try
		{
			//�o�[�R�[�h���[�_�[�̃R�l�N�V���������
			closeBarcodeReaderConnection();

			barcodeReaderConnection	=	null;

			//�o�[�R�[�h���[�_�[�̏���ǂݍ���
			loadBarcodeReaderInfo();

			if(isUseBarcodeReader())
			{
				System.out.println("�o�[�R�[�h���[�_�[�̃R�l�N�V���������J�n");
			//�w�肳��Ă���|�[�g�����݂��Ȃ��ꍇ�A�����𔲂���
			if(barcodeReaderParameters.getPortName() == null ||
	                   barcodeReaderParameters.getPortName().equals("") ||
					!SerialConnection.isExistSerialPort(
							barcodeReaderParameters.getPortName()))
			{
				return	false;
			}

			//�R�l�N�V�����̃C���X�^���X���擾
			barcodeReaderConnection	=	new SerialConnection(
					parent, getBarcordReaderParameters());

				//�R�l�N�V�������J��
				getBarcodeReaderConnection().openConnection();
			}
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			return	false;
		}

		return	true;
	}

	/**
	 * �o�[�R�[�h���[�_�[�̃R�l�N�V���������
	 */
	public static void closeBarcodeReaderConnection()
	{
		if(getBarcodeReaderConnection() != null)
		{
			getBarcodeReaderConnection().closeConnection();
		}
	}

	/**
	 * �o�[�R�[�h���[�_�[�̏���ǂݍ���
	 * @return true�F����
	 */
	private static boolean loadBarcodeReaderInfo()
	{
		barcodeReaderParameters	=	new SerialParameters();

		try
		{
			ResultSetWrapper	rs	=	getBaseConnection().executeQuery(
					getLoadBarcodeReaderInfoSQL());

			if(rs.next())
			{
				setUseBarcodeReader(rs.getBoolean("use_barcode_reader"));
				barcodeReaderParameters.setPortName(rs.getString("barcode_reader_port"));
				barcodeReaderParameters.setBaudRate(rs.getInt("baud_rate"));
				barcodeReaderParameters.setFlowControlIn(rs.getInt("flow_control_in"));
				barcodeReaderParameters.setFlowControlOut(rs.getInt("flow_control_out"));
				barcodeReaderParameters.setDatabits(rs.getInt("databits"));
				barcodeReaderParameters.setStopbits(rs.getInt("stopbits"));
				barcodeReaderParameters.setParity(rs.getInt("parity"));
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			return	false;
		}

		return	true;
	}

	/**
	 * �o�[�R�[�h���[�_�[�̏����擾����SQL�����擾����
	 * @return �o�[�R�[�h���[�_�[�̏����擾����SQL��
	 */
	private static String getLoadBarcodeReaderInfoSQL()
	{
		return	"select mm.use_barcode_reader,\n" +
				"mm.barcode_reader_port, msp.*\n" +
				"from mst_mac mm\n" +
				"left outer join mst_serial_parameter msp\n" +
				"on msp.serial_parameter_id = mm.serial_parameter_id\n" +
				"where mm.login_id = " + SQLUtil.convertForSQL(getLoginID()) + "\n" +
				"and mm.mac_id = " + SQLUtil.convertForSQL(getMacID()) + "\n";
	}
	/**
	 * CTI���[�_�[�̃R�l�N�V�������J��
	 * @param parent CTI���X�i�[
	 * @return true�F����
	 */
	public static boolean openCtiReaderConnection(CTIListener parent)
	{
		try
		{
			//CTI���[�_�[�̃R�l�N�V���������
			closeCtiReaderConnection();

			ctiReaderConnection	=	null;

			//CTI���[�_�[�̏���ǂݍ���
			loadCtiReaderInfo();

			if(isUseCtiReader())
			{
				System.out.println("CTI���[�_�[�̃R�l�N�V���������J�n");
				System.out.println("getName :"+ctiReaderParameters.getPortName());
			//�w�肳��Ă���|�[�g�����݂��Ȃ��ꍇ�A�����𔲂���
			if(ctiReaderParameters.getPortName() == null ||
					!SerialConnection.isExistSerialPort(
							ctiReaderParameters.getPortName()))
			{
					System.out.println("�w�肳��Ă���|�[�g�����݂��Ȃ�");
				return	false;
			}
				System.out.println("�C���X�^���X�擾�J�n");
				System.out.flush();
			//�R�l�N�V�����̃C���X�^���X���擾
			ctiReaderConnection	=	new SerialConnection(
					parent, getCtiReaderParameters());
				System.out.println("�R�l�N�V�������J�������J�n");
				//�R�l�N�V�������J��
				getCtiReaderConnection().openConnection();
				System.out.println("�R�l�N�V����open");
			}
		}
		catch(Exception e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			return	false;
		}

		return	true;
	}
	/**
	 * CTI���[�_�[�̏���ǂݍ���
	 * @return true�F����
	 */
	private static boolean loadCtiReaderInfo()
	{
		ctiReaderParameters	=	new SerialParameters();

		try
		{
			ResultSetWrapper	rs	=	getBaseConnection().executeQuery(
					getLoadCtiReaderInfoSQL());

			if(rs.next())
			{
				setUseCtiReader(rs.getBoolean("use_cti_reader"));
				ctiReaderParameters.setPortName(rs.getString("cti_reader_port"));
				ctiReaderParameters.setBaudRate(rs.getInt("baud_rate"));
				ctiReaderParameters.setFlowControlIn(rs.getInt("flow_control_in"));
				ctiReaderParameters.setFlowControlOut(rs.getInt("flow_control_out"));
				ctiReaderParameters.setDatabits(rs.getInt("databits"));
				ctiReaderParameters.setStopbits(rs.getInt("stopbits"));
				ctiReaderParameters.setParity(rs.getInt("parity"));
                                ctiDisplaySizeType = rs.getInt("cti_display_size_type");
			}
		}
		catch(SQLException e)
		{
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
			return	false;
		}

		return	true;
	}

	/**
	 * CTI���[�_�[�̏����擾����SQL�����擾����
	 * @return CTI���[�_�[�̏����擾����SQL��
	 */
	private static String getLoadCtiReaderInfoSQL()
	{
		return	"select mm.use_cti_reader,\n" +
				"mm.cti_reader_port, mm.cti_display_size_type, msp.*\n" +
				"from mst_mac mm\n" +
				"left outer join mst_serial_parameter msp\n" +
				"on msp.serial_parameter_id = mm.serial_parameter_id\n" +
				"where mm.login_id = " + SQLUtil.convertForSQL(getLoginID()) + "\n" +
				"and mm.mac_id = " + SQLUtil.convertForSQL(getMacID()) + "\n";
	}
	/**
	 * CTI���[�_�[�̃R�l�N�V���������
	 */
	public static void closeCtiReaderConnection()
	{
		if(getCtiReaderConnection() != null)
		{
			getCtiReaderConnection().closeConnection();
		}
	}

    /**
     * �|�C���g�J�[�h���[�_�E���C�^�[���g����
     * @return true�F�g��
     */
    public static Boolean isUsePointcard()
    {
        //�|�C���g�J�[�h���[�_�E���C�^�[�̏���ǂݍ���
        if(!isReadPointcard) loadPointcardInfo();
        return usePointcard;
    }

    /**
     * �|�C���g�J�[�h���[�_�E���C�^�[���g������ݒ肷��
     * @param aUsePointcard true�F�|�C���g�J�[�h���[�_�E���C�^�[���g��
     */
    public static void setUsePointcard(Boolean aUsePointcard)
    {
        usePointcard = aUsePointcard;
    }

    /**
     * �|�C���g�J�[�h���[�_�E���C�^�[�̃p�����[�^���擾����
     * @return �|�C���g�J�[�h���[�_�E���C�^�[�̃p�����[�^
     */
    public static SerialParameters getPointcardParameters()
    {
        return pointcardParameters;
    }

    /**
     * �|�C���g�J�[�h���[�_�E���C�^�[�̃p�����[�^��ݒ肷��
     * @param aPointcardParameters �|�C���g�J�[�h���[�_�E���C�^�[�̃p�����[�^
     */
    public static void setPointcardParameters(SerialParameters aPointcardParameters)
    {
        pointcardParameters = aPointcardParameters;
    }

    /**
     * �|�C���g�J�[�h���[�_�E���C�^�[�̃R�l�N�V�������擾����
     * @return �|�C���g�J�[�h���[�_�E���C�^�[�̃R�l�N�V����
     */
    public static AbstractCardCommunication getPointcardConnection()
    {
        return pointcardConnection;
    }

    /**
     * �|�C���g�J�[�h���[�_�E���C�^�[�̐��i���ʎq��ݒ肷��
     * @param ProductId �|�C���g�J�[�h���[�_�E���C�^�[�̐��i���ʎq
     * �i0:SANWA�A1:STAR�A2:NATEC�j
     */
    public static void setPointcardProductId(int ProductId)
    {
        pointcardProductId = ProductId;
    }

    /**
     * �|�C���g�J�[�h���[�_�E���C�^�[�̐��i���ʎq���擾����
     */
    public static int getPointcardProductId()
    {
        return pointcardProductId;
    }

    /**
     * �|�C���g�̏o�͋敪���擾����
     * @return �|�C���g�̏o�͋敪
     */
    public static int getPointOutputType()
    {
        return pointOutputType;
    }

    /**
     * �|�C���g�̏o�͋敪��ݒ肷��
     * @param outputType �|�C���g�̏o�͋敪
     * �i0:�����C�g�J�[�h�A1:���V�[�g�j
     */
    public static void setPointOutputType(int outputType)
    {
        pointOutputType = outputType;
    }

    /**
     * �|�C���g��ʂ̓��t�󎚏����l�t���O���擾����
     * @return �|�C���g��ʂ̓��t�󎚏����l�t���O
     */
    public static int getPointDefaultDateType() {
        return pointDefaultDateType;
    }

    /**
     * �|�C���g��ʂ̓��t�󎚏����l�t���O��ݒ肷��
     * @param aPointDefaultDateType �|�C���g��ʂ̓��t�󎚏����l�t���O
     * �i0�F���X���t�A1�F����\����A2�F�ܔ������A3�F���񗈓X�ē��j
     */
    public static void setPointDefaultDateType(int aPointDefaultDateType) {
        pointDefaultDateType = aPointDefaultDateType;
    }

    /**
     * �|�C���g�J�[�h���[�_�E���C�^�[�̃R�l�N�V�������J��
     * @return true�F����
     */
    public static boolean openPointcardConnection()
    {
        try
        {
            //�|�C���g�J�[�h���[�_�E���C�^�[�̃R�l�N�V���������
            closePointcardConnection();

            pointcardConnection =   null;

            //�|�C���g�J�[�h���[�_�E���C�^�[�̏���ǂݍ���
            if(!isReadPointcard) loadPointcardInfo();

            if(isUsePointcard())
            {
                System.out.println("�|�C���g�J�[�h���[�_�E���C�^�[�̃R�l�N�V���������J�n");

                // �i�e�b�N�ȊO�̃J�[�h���[�_�[�̓|�[�g���݃`�F�b�N���s��
                if (pointcardProductId != 2) {
                    //�w�肳��Ă���|�[�g�����݂��Ȃ��ꍇ�A�����𔲂���
                    if(pointcardParameters.getPortName() == null ||
                            !SerialConnection.isExistSerialPort(
                                    pointcardParameters.getPortName()))
                    {
                        return  false;
                    }
                }

                //�R�l�N�V�����̃C���X�^���X���擾
                pointcardConnection = enumPointcardProduct.getConnection(
                                                    pointcardProductId,
                                                    getPointcardParameters());

                //�R�l�N�V�������J��
                getPointcardConnection().Open();
            }
        }
        catch(Exception e)
        {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return  false;
        }

        return  true;
    }

    /**
     * �|�C���g�J�[�h���[�_�E���C�^�[�̃R�l�N�V���������
     */
    public static void closePointcardConnection()
    {
        if(getPointcardConnection() != null)
        {
            getPointcardConnection().Close();
        }
    }

    /**
     * �|�C���g�J�[�h���[�_�E���C�^�[�̏���ǂݍ���
     * @return true�F����
     */
    private static boolean loadPointcardInfo()
    {
        pointcardParameters =   new SerialParameters();

        try
        {
            ResultSetWrapper    rs  =   getBaseConnection().executeQuery(
                    getLoadPointcardInfoSQL());

            if(rs.next())
            {
                setUsePointcard(rs.getBoolean("use_pointcard_reader"));
                pointcardParameters.setPortName(rs.getString("pointcard_reader_port"));
                pointcardParameters.setBaudRate(rs.getInt("baud_rate"));
                pointcardParameters.setFlowControlIn(rs.getInt("flow_control_in"));
                pointcardParameters.setFlowControlOut(rs.getInt("flow_control_out"));
                pointcardParameters.setDatabits(rs.getInt("databits"));
                pointcardParameters.setStopbits(rs.getInt("stopbits"));
                pointcardParameters.setParity(rs.getInt("parity"));
                pointcardProductId = rs.getInt("pointcard_reader_product_id");
                pointOutputType = rs.getInt("point_output_type");
                pointDefaultDateType = rs.getInt("point_default_date_type");
                isReadPointcard = true;
            }
        }
        catch(SQLException e)
        {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return  false;
        }

        return  true;
    }

    /**
     * �|�C���g�J�[�h���[�_�E���C�^�[�̏����擾����SQL�����擾����
     * @return �|�C���g�J�[�h���[�_�E���C�^�[�̏����擾����SQL��
     */
    private static String getLoadPointcardInfoSQL()
    {
        return  "select mm.use_pointcard_reader,\n" +
                "mm.pointcard_reader_port,\n" +
                "mm.pointcard_reader_product_id,\n" +
                "mm.point_output_type,\n" +
                "mm.point_default_date_type,\n" +
                "msp.*\n" +
                "from mst_mac mm\n" +
                "left outer join mst_serial_parameter msp\n" +
                "on msp.serial_parameter_id = mm.pointcard_serial_parameter_id\n" +
                "where mm.login_id = " + SQLUtil.convertForSQL(getLoginID()) + "\n" +
                "and mm.mac_id = " + SQLUtil.convertForSQL(getMacID()) + "\n";
    }

	/**
	 * ����ł��擾����
	 * @param value ���ƂȂ鐔�l
	 * @param date �ΏۂƂȂ���t
	 * @return �����
	 */
	public static Long getTax(Long value, java.util.Date date)
	{
		return	TaxUtil.getTax(value, getTaxRate(date), 2);
	}

	/**
	 * ����ł��擾����
	 * @param value ���ƂȂ鐔�l
	 * @param date �ΏۂƂȂ���t
	 * @return �����
	 */
	public static Integer getTax(Integer value, java.util.Date date)
	{
		return	TaxUtil.getTax(value, getTaxRate(date), 1);
	}

	/**
	 * SOSIA�A���X�܃��X�g���擾����
	 * @return SOSIA�A���X�܃��X�g
	 */
        public static Map getSosiaGearShopList() {

            if (sosiaGearShopList == null) {

                sosiaGearShopList = new HashMap();

                StringBuilder sql = new StringBuilder(1000);
                sql.append(" select");
                sql.append("      mu.shop_id");
                sql.append("     ,max(mu.sosia_code) as sosia_code");
                sql.append(" from");
                sql.append("     mst_user mu");
                sql.append("         join mst_database md");
                sql.append("         using (database_id)");
                sql.append(" where");
                sql.append("         coalesce(mu.sosia_gear_base_url, '') <> ''");
                sql.append("     and shop_id <> 0");
                sql.append("     and md.database_name = " + SQLUtil.convertForSQL(SystemInfo.getDatabase()));
                sql.append(" group by");
                sql.append("     shop_id");

		try {
                    ResultSetWrapper rs = SystemInfo.getBaseConnection().executeQuery(sql.toString());

                    while (rs.next()) {
                        sosiaGearShopList.put(rs.getInt("shop_id"), rs.getString("sosia_code"));
                    }

                    rs.close();

		} catch(SQLException e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}

            }

            return sosiaGearShopList;
    }

    /**
	 * SOSIA�A���X�܃��X�g���擾����
	 * @return SOSIA�A���X�܃��X�g
	 */
        public static String getSosiaGearByShopIdList(String ShopIdList) {
            String sosiaGearShopListStr = "";
                sosiaGearShopList = new HashMap();
                StringBuilder sql = new StringBuilder(1000);
                sql.append(" select");
                sql.append("      mu.shop_id");
                sql.append("     ,max(mu.sosia_code) as sosia_code");
                sql.append(" from");
                sql.append("     mst_user mu");
                sql.append("         join mst_database md");
                sql.append("         using (database_id)");
                sql.append(" where");
                sql.append("         coalesce(mu.sosia_gear_base_url, '') <> ''");
                sql.append("     and shop_id in(" + ShopIdList +")");
                sql.append("     and md.database_name = " + SQLUtil.convertForSQL(SystemInfo.getDatabase()));
                sql.append(" group by");
                sql.append("     shop_id");

		try {
                    ResultSetWrapper rs = SystemInfo.getBaseConnection().executeQuery(sql.toString());


                    Integer i = 0;
                    while (rs.next()) {
                        if(i>0) {
                            sosiaGearShopListStr += "," + "'" + rs.getString("sosia_code") + "'";
                        }else {
                            sosiaGearShopListStr +=   "'" + rs.getString("sosia_code") + "'" ;
                        }
                        i++;
                    }
                    rs.close();



		} catch(SQLException e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
            return sosiaGearShopListStr;
    }
    public static Map getMapSosiaShopName(String sosiaGearShopListStr) {
        StringBuffer sql1 = new StringBuffer();
        StringBuffer sql2 = new StringBuffer();
        mapSosiaIDShopName = new HashMap();
        try {
            sql1.append(" select max(sosia_code) as sosia_code,shop_id from mst_user u ");
            sql1.append(" inner join mst_database d on u.database_id = d.database_id ");
            sql1.append(" where  ");
            sql1.append("  d.database_name = "+ SQLUtil.convertForSQL(SystemInfo.getDatabase()));
            sql1.append("  group by shop_id,sosia_code ");
            sql1.append("  having ");
            sql1.append("  max(sosia_code) in ("+sosiaGearShopListStr + ")");
            ResultSetWrapper rs1 = SystemInfo.getBaseConnection().executeQuery(sql1.toString());
            while(rs1.next()) {;
                sql2 = new StringBuffer();
                sql2.append( "select shop_name from mst_shop where shop_id = "+SQLUtil.convertForSQL(rs1.getString("shop_id")));
                ResultSetWrapper rs2 = SystemInfo.getConnection().executeQuery(sql2.toString());
                rs2.first();
                mapSosiaIDShopName.put(rs1.getString("sosia_code"), rs2.getString("shop_name"));
            }
        }catch(Exception e) {}
        return mapSosiaIDShopName;
    }
    public static int getCtiDisplaySizeType() {
        return ctiDisplaySizeType;
    }

    public static void setCtiDisplaySizeType(int aCtiDisplaySizeType) {
        ctiDisplaySizeType = aCtiDisplaySizeType;
    }

    private static boolean isSystemExit() {

        String msg = MessageUtil.getMessage(1000, "") + "\n"
                   + "�C���^�[�l�b�g�̐ڑ����m�F���ĉ������B\n\n"
                   + "�ēx�ڑ������݂�ꍇ�́A���Ď��s�����A\n"
                   + "�V�X�e�����I������ꍇ�́A���I�����������ĉ������B\n";

        Object[] options = { "  �Ď��s  ", "  �I ��  " };
        int ret = JOptionPane.showOptionDialog(
                        null,
                        msg,
                        "�f�[�^�x�[�X�ڑ��G���[",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        options,
                        options[0]);

        if (ret == 0) return false;

        System.gc();

        try {
            Process process = Runtime.getRuntime().exec("cmd /c taskkill /F /IM javaw.exe");
            process.waitFor();
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        System.exit(0);

        return true;
    }

    /**
     * �J�X�^�}�[�f�B�X�v���C���g����
     * @return true�F�g��
     */
    public static Boolean isUseCustomerDisplay()
    {
            return useCustomerDisplay;
    }

    /**
     * �J�X�^�}�[�f�B�X�v���C���g������ݒ肷��
     * @param aUseCustomerDisplay true�F�J�X�^�}�[�f�B�X�v���C���g��
     */
    public static void setUseCustomerDisplay(Boolean aUseCustomerDisplay)
    {
            useCustomerDisplay = aUseCustomerDisplay;
    }

    /**
     * �J�X�^�}�[�f�B�X�v���C�̃p�����[�^���擾����
     * @return �J�X�^�}�[�f�B�X�v���C�̃p�����[�^
     */
    public static String getCustomerDisplayPort()
    {
            return customerDisplayPort;
    }

    /**
     * �J�X�^�}�[�f�B�X�v���C�̃|�[�g��ݒ肷��
     * @param aCustomerDisplayParameters �J�X�^�}�[�f�B�X�v���C�̃|�[�g
     */
    public static void setCustomerDisplayPort(String aCustomerDisplayPort)
    {
            customerDisplayPort = aCustomerDisplayPort;
    }

    /**
     * �J�X�^�}�[�f�B�X�v���C�̏���ǂݍ���
     * @return true�F����
     */
    public static boolean loadCustomerDisplayInfo()
    {
            try
            {
                ResultSetWrapper rs = getBaseConnection().executeQuery(getLoadCustomerDisplayInfoSQL());
                if (rs.next()) {
                    setUseCustomerDisplay(rs.getBoolean("use_customer_display"));
                    setCustomerDisplayPort(rs.getString("customer_display_port"));

                    if (getCustomerDisplayPort() != null) {
                        SerialConnection.isExistSerialPort(getCustomerDisplayPort());
                    }

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
     * �o�[�R�[�h���[�_�[�̏����擾����SQL�����擾����
     * @return �o�[�R�[�h���[�_�[�̏����擾����SQL��
     */
    private static String getLoadCustomerDisplayInfoSQL()
    {
            return	"select mm.use_customer_display,\n" +
                            "mm.customer_display_port\n" +
                            "from mst_mac mm\n" +
                            "where mm.login_id = " + SQLUtil.convertForSQL(getLoginID()) + "\n" +
                            "and mm.mac_id = " + SQLUtil.convertForSQL(getMacID()) + "\n";
    }

    // IVS_LVTu start add 20150303 Bug #35227
    /**
	 * @param index 66
	 * @return true�FOK
	 */
	public static boolean checkAuthority66(int index)
	{
            MstAuthority authority	=	new MstAuthority();
            authority.setGroup(getGroup());

            authority.setShop(getCurrentShop());

            try
            {
                    authority.load(SystemInfo.getConnection());
            }
            catch(Exception e)
            {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }

            if (0 <= index && index < MstAuthority.AUTHORYTY_MAX && authority.getAuthoryty(index)) {
                return true;
            } else {
                return false;
            }
	}
        // IVS_LVTu end add 20150303 Bug #35227


    // IVS_LVTu start add 20161024 New request #54379
    /**
	 * get user_api from user
	 * @return integer
	 */
	public static Integer getUserByUserApi()
	{
            // 20170627 GB Start Edit #17201 [GB���Ή�][gb] �\��\ ���x���P mst_user.use_api�擾���\�b�h
//            Integer userApi = null;

//            mstUser.setDatabaseID(databaseID);
//            if(mstUser != null && mstUser.getShopID() != null && mstUser.getDatabaseID() != null)
//            {
//                try
//                {
//                    userApi = mstUser.load(SystemInfo.getBaseConnection());
//                }
//                catch(Exception e)
//                {
//                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
//                }
//            }
		Integer shopId = mstUser.getShopID();
		Integer userApi = null;
		if (userApiMap.containsKey(shopId)) {
			userApi = userApiMap.get(shopId);
			SystemInfo.getLogger().log(Level.INFO, "cache in shop:"+shopId+" - "+ userApi);
		} else {

            mstUser.setDatabaseID(databaseID);
            if(mstUser != null && mstUser.getShopID() != null && mstUser.getDatabaseID() != null)
            {
                try
                {
                    userApi = mstUser.load(SystemInfo.getBaseConnection());
					userApiMap.put(mstUser.getShopID(), userApi);
					SystemInfo.getLogger().log(Level.INFO, "load shop:"+shopId+" - "+ userApi);
                }
                catch(Exception e)
                {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                }
            }
		}
            // 20170627 GB End Edit #17201 [GB���Ή�][gb] �\��\ ���x���P mst_user.use_api�擾���\�b�h

        return userApi;
	}
    // IVS_LVTu end add 20161024 New request #54379

	public static final String TYPE_ITEM = "i";
	public static final String TYPE_TECHNIC = "t";
	public static final String TYPE_COURCE = "c";
	public static final String TYPE_ITEM_RETURNED = "r";
	private static HashMap<String, ArrayList<? extends Object>> cachedProducts = new HashMap<String, ArrayList<? extends Object>>();

	public static boolean isCachedProducts(Integer shop_id, String type, Integer classId) {
		if (shop_id == null) {
			shop_id = currentShop.getShopID();
		}
		return cachedProducts.containsKey(shop_id+"_"+type+"_"+classId);
	}
	public static void setCachedProducts(Integer shop_id, String type, Integer classId, ArrayList<? extends Object> classList) {
		if (shop_id == null) {
			shop_id = currentShop.getShopID();
		}
		cachedProducts.put(shop_id+"_"+type+"_"+classId, classList);
	}

	public static ArrayList<? extends Object> getCachedProducts(Integer shop_id, String type, Integer classId) {
		if (shop_id == null) {
			shop_id = currentShop.getShopID();
		}
		return cachedProducts.get(shop_id+"_"+type+"_"+classId);
	}

	// 20170704 yasumoto add start
	private static MstDiscounts mstDiscountsAll;
	private static MstDiscounts mstDiscountsUse;
	private static MstDiscounts mstDiscountsNotUse;

	public static MstDiscounts getDiscounts() {
		return getDiscounts(null);
	}
	public static MstDiscounts getDiscounts(Boolean isUse) {
		try {
			ConnectionWrapper con = SystemInfo.getConnection();
			if (isUse == null) {
				if (mstDiscountsAll == null) {
					mstDiscountsAll = new MstDiscounts();
					mstDiscountsAll.load(con);
				}
				return mstDiscountsAll;
			} else if (isUse) {
				if (mstDiscountsUse == null) {
					mstDiscountsUse = new MstDiscounts();
					mstDiscountsUse.load_Use(con);
				}
				return mstDiscountsUse;
			} else {
				if (mstDiscountsNotUse == null) {
					mstDiscountsNotUse = new MstDiscounts();
					mstDiscountsNotUse.load_NotUse(con);
				}
				return mstDiscountsNotUse;
			}
		} catch (SQLException e) {
			SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e.getCause());
		}
		return null;
	}

	private static Map<Integer, MstBeds> bedsMap = new HashMap<Integer, MstBeds>();
	public static MstBeds getBeds(MstShop mshop) {
		if (bedsMap.containsKey(mshop.getShopID())) {
			return bedsMap.get(mshop.getShopID());
		}

		MstBeds beds = new MstBeds();
        try {
			ConnectionWrapper con = SystemInfo.getConnection();
			beds.setShop(mshop);
            beds.loadBedInputData(con);
			bedsMap.put(mshop.getShopID(), beds);
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
		return beds;
	}
	// 20170704 yasumoto add end

	private static ArrayList proportionallys = new ArrayList(0);
	public static ArrayList getProportionallys() {
		if( proportionallys.size() > 0) {
			return proportionallys;
		} else {
			return null;
		}
	}
	public static void setProportionallys(ArrayList _proportionallys) {
		proportionallys.addAll(_proportionallys);
	}


	private static ArrayList shopRelations = new ArrayList(0);
		public static ArrayList getShopRelations() {
		if( shopRelations.size() > 0) {
			return shopRelations;
		} else {
			return null;
		}
	}
	public static void setShopRelations(ArrayList _shopRelations) {
		shopRelations.addAll(_shopRelations);
	}



	private static long datetime;
	public static void info(String msg) {
		long tmpdatetime  = Calendar.getInstance().getTimeInMillis() - datetime;
        SystemInfo.getLogger().log(Level.INFO, tmpdatetime+" :::::: "+msg);
		datetime = Calendar.getInstance().getTimeInMillis();
	}
}
