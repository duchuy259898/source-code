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
 * システム情報
 * @author katagiri
 */
public class SystemInfo
{
	/**
	 * JDBCドライバ
	 */
	private static final String JDBC_DRIVER = "org.postgresql.Driver";

        /**
         * SOSIA_POS_system.jar をサーバにアップするときはここの値も変更すること！！
         *
	 * 開発サーバと本番サーバの接続先の切り替え
         * （ 0：開発サーバ ／ 1：本番サーバ ／ 9：ローカルサーバ ）
	 */
        private static int destination_flag = 1;

        /**
	 * サーバーURL
	 */
//	private static final	String	SERVER_URL		=	"jdbc:postgresql://127.0.0.1:5432";
//	private static final	String	SERVER_URL		=	"jdbc:postgresql://192.168.10.18:5432";
//	private static final	String	SERVER_URL		=	"jdbc:postgresql://180.211.85.100:5432";

	/**
	 * 共通サーバーURL
	 */
        //private static final	String	BASE_SERVER_URL         =	"jdbc:postgresql://210.233.66.40:5432";
        //private static final	String	BASE_SERVER_URL         =	"jdbc:postgresql://192.168.0.130:5432";
        //private static final	String	BASE_SERVER_URL         =	"jdbc:postgresql://192.168.0.112:5432";
        private static final	String	BASE_SERVER_URL         =	"jdbc:postgresql://soarer90.gb-lab.com";
	private static final	String	BASE_SERVER_URL_DEV     =	"jdbc:postgresql://192.168.0.130:5432";
	private static final	String	BASE_SERVER_URL_LOCAL   =	"jdbc:postgresql://127.0.0.1:5432";

	/**
	 * 共通データベース名
	 */
	private	static final	String	BASE_DATABASE	=	"pos_lic_test";

	/**
	 * 共通データベースユーザー
	 */
//	private	static final	String	BASE_DB_USER	=	"sosiapos";
	private	static final	String	BASE_DB_USER	=	"postgres";

	/**
	 * 共通データベースパスワード
	 */
//	private	static final	String	BASE_DB_PASS	=	"Ohbf8ww3";
	private	static final	String	BASE_DB_PASS	=	"7SoWHaDX";

	/**
	 * メール送信サーバーURL
	 */
	private static final	String	MAIL_SERVER_URL	=	"jdbc:postgresql://soarer01q.gb-lab.com:5432";

	/**
	 * メール送信データベース名
	 */
	private	static final	String	MAIL_DATABASE	=	"mailqueue";

	/**
	 * メール送信データベースユーザー
	 */
	private	static final	String	MAIL_DB_USER	=	"mailqueue";

	/**
	 * メール送信データベースパスワード
	 */
	private	static final	String	MAIL_DB_PASS	=	"DK2yl-iR";

	/**
	 * ケータイ会員サーバーURL
	 */
	private static final	String	MOBILE_SERVER_URL	=	"jdbc:postgresql://cnt1.gb-lab.com:5432";

	/**
	 * ケータイ会員データベース名
	 */
	private	static final	String	MOBILE_DATABASE	=	"WildCard";

	/**
	 * ケータイ会員データベースユーザー
	 */
	private	static final	String	MOBILE_DB_USER	=	"postgres";

	/**
	 * ケータイ会員データベースパスワード
	 */
	private	static final	String	MOBILE_DB_PASS	=	"7SoWHaDX";

	/**
	 * テストデータベース名
	 */
	private	static final	String	TEST_DATABASE	=	"pos_hair_dev";

	/**
	 * Loggerオブジェクト名
	 */
	private static final	String	LOGGER_NAME		=	"com.geobeck.sosia.pos";

	/**
	 * ログレベル
	 */
	private static final	Level	LOGGER_LEVEL	=	Level.INFO;

	/**
	 * ログファイルパス
	 */
	//private static final	String	LOG_FILE_PATH = getLogRoot() + "/log";

	/**
	 * ログファイル名
	 */
	private static final	String	LOG_FILE_NAME	=	"SosiaPos%g.log";

	/**
	 * ログファイルのサイズ
	 */
	private	static final	int		LOG_FILE_LIMIT	=	1000000;

	/**
	 * ログファイル数
	 */
	private	static final	int		LOG_FILE_COUNT	=	10;

	/**
	 * テーブルの行の高さ
	 */
	public	static final	int		TABLE_ROW_HEIGHT	=	26;


	/**
	 * 会社のアイコンのパス
	 */
	private static final String		ROOT_ICON_PATH		=	"/images/common/icon/root.png";
	/**
	 * グループのアイコンのパス
	 */
	private static final String		GROUP_ICON_PATH		=	"/images/common/icon/group.png";
	/**
	 * 店舗のアイコンのパス
	 */
	private static final String		SHOP_ICON_PATH		=	"/images/common/icon/shop.png";

	/**
	 * サーバーIPアドレス
	 */
	private static String           serverIP		=	"";

        /**
	 * サーバーポート番号
	 */
	private static String           serverPort		=	"";

	/**
	 * データベース名
	 */
	private	static	String		database		=	TEST_DATABASE;

	/**
	 * ユーザー
	 */
	private	static	String		user			=	BASE_DB_USER;

	/**
	 * パスワード
	 */
	private	static	String		pass			=	BASE_DB_PASS;

	/**
	 * ログインＩＤ
	 */
	private static	String		loginID			=	"";

	/**
	 * ログインパスワード
	 */
	private static	String		loginPass		=	"";

        /**
	 * WEBディレクトリ名
	 */
	private	static	String		directoryName		=	"";

	/**
	 * ＭＡＣアドレスＩＤ
	 */
	private static Integer		macID			=	null;

	/**
	 * 種別ＩＤ
	 */
	private static Integer		typeID			=	null;

	/**
	 * 設定
	 */
	private static MstSetting	setting			=	null;

	private	static MstAccountSetting accountSetting         =       null;

	/**
	 * グループ
	 */
	private static MstGroup		group			=	new MstGroup();

	/**
	 * ログインＩＤに対応する店舗データ
	 */
	private static MstShop		currentShop		=	new MstShop();

	/**
	 * 権限
	 */
	private static MstAuthority	authority		=	null;

	/**
	 * SOSIA連動基本URL
	 */
	private static String		sosiaGearBaseURL	=	null;
	/**
	 * SOSIA連動ID
	 */
	private static String		sosiaGearID		=	null;
	/**
	 * SOSIA連動PASS
	 */
	private static String		sosiaGearPassWord	=	null;
	/**
	 * SOSIA連動コード
	 */
	private static String		sosiaCode               =	null;

	/**
	 * SOSIA連動店舗リスト
	 */
        private static Map              sosiaGearShopList       =       null;


        private static Map              mapSosiaIDShopName       =       null;

	/**
	 * レシートプリンタタイプ(0：Star、1：Epson)
	 */
	private static Integer		receiptPrinterType      =	null;

	/**
	 * 予約機能専用フラグ(0：OFF、1：ON)
	 */
        //IVS_LVTu start edit 2016/02/24 New request #48455
	//private static Integer		reservationOnly         =	null;
        private static Integer		systemType         =	null;

	/**
	 * Enterを押したときに次のコンポーネントに移動させるKeyListener
	 */
	private static	MoveNextField	mnf		=	null;
	/**
	 * フォーカス取得時にテキストを全選択させるFocusListener
	 */
	private static	SelectText		st		=	null;

	/**
	 * フォーカスが無くなったときに編集を終了させるクラス
	 */
	private static	LostFocusEditingStopper	lfes	=	null;

	/**
	 * 整数値用Formatter
	 */
	private static NumberFormatter	decimalFormatter	=	null;

	/**
	 * 小数値用Formatter
	 */
	private static NumberFormatter	floatFormatter		=	null;

	/**
	 * TableHeaderRenderer
	 */
	private static BevelBorderHeaderRenderer	tableHeaderRenderer	=	null;

	/**
	 * 基本コネクション
	 */
	private static ConnectionWrapper	baseConnection		=	null;
	/**
	 * 個別コネクション
	 */
	private static ConnectionWrapper	personalConnection	=	null;
	/**
	 * メール用コネクション
	 */
	private static ConnectionWrapper	mailConnection		=	null;
	/**
	 * ケータイ会員取得用コネクション
	 */
	private static ConnectionWrapper	mobileConnection		=	null;

	/**
	 * 支払区分List
	 */
	private	static MstPaymentClasses	paymentClasses		=	null;

	/**
	 * Loggerオブジェクト
	 */
	private static	Logger		logger				=	null;

	/**
	 * 会社のアイコン
	 */
	private static	ImageIcon	rootIcon			=	null;
	/**
	 * グループのアイコン
	 */
	private static	ImageIcon	groupIcon			=	null;
	/**
	 * 店舗のアイコン
	 */
	private static	ImageIcon	shopIcon			=	null;

	/**
	 * スキン
	 */
	private	static	MstSkin		skin				=	null;

	/**
	 * バーコードリーダーを使うか
	 */
	private static	Boolean				useBarcodeReader		=	false;
	/**
	 * バーコードリーダーのパラメータ
	 */
	private static	SerialParameters	barcodeReaderParameters	=	null;
	/**
	 * バーコードリーダーのコネクション
	 */
	private static	SerialConnection	barcodeReaderConnection	=	null;

	/**
	 * CTIリーダーを使うか
	 */
	private static	Boolean			useCtiReader		=	false;
	/**
	 * CTIリーダーのパラメータ
	 */
	private static	SerialParameters	ctiReaderParameters	=	null;
	/**
	 * CTIリーダーのコネクション
	 */
	private static	SerialConnection	ctiReaderConnection	=	null;
	/**
	 * CTI NEC　AtermIT42
	 */
	private static final	    String	CTI_NEC_ATERM_IT42	=	"\n\nRING ANALOG";

	/**
	 * ポイントカードリーダ・ライター情報読み込み済みか？
	 */
	private static Boolean				isReadPointcard	=	false;

	/**
	 * ポイントカードリーダ・ライターを使うか
	 */
	private static Boolean				usePointcard	=	false;

    /**
     * ポイントカードリーダ・ライターのパラメータ
     */
    private static SerialParameters	pointcardParameters	=	null;

    /**
     * ポイントカードリーダ・ライターのコネクション
     */
    private static AbstractCardCommunication pointcardConnection = null;

    /**
     * ポイントカードリーダ・ライターの製品種類
     */
    private static int pointcardProductId = 0;

    /**
     * ポイントの出力区分
     */
    private static int pointOutputType = 0;

    /**
     * ポイント画面の日付印字初期値フラグ（0：来店日付、1：次回予約日、2：賞美期限、3：次回来店案内）
     */
    private static int pointDefaultDateType = 0;

    /**
     * CTI画面のサイズフラグ（0：全体表示、1：縮小表示）
     */
    private static int ctiDisplaySizeType = 0;

    /**
     * DB接続処理中フラグ
     */
    private static boolean isConnProcessing = false;

    /**
     * 起動OS判定フラグ
     */
    private static boolean windows = true;

    /**
    * カスタマーディスプレイを使うか
    */
    private static Boolean useCustomerDisplay = false;
    /**
    * カスタマーディスプレイのポート
    */
    private static String customerDisplayPort = null;

    // Khoa Add Start 20121024 Main画面のロゴ変更
    /**
    * ispot店舗ID
    */
    private static String ispotShopID	=	"";
    // Khoa Add End 20121024 Main画面のロゴ変更

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

    /** 来店カルテ入力使用フラグ(true:使用 false:使用しない)
      * 20170413 add #61376
      */
    private static boolean UseVisitKarte                = false;

    private static MstUser mstUser                    = new MstUser();

	// 20170627 GB Start Add Edit #17201 [GB内対応][gb] 予約表 速度改善 mst_user.use_api取得メソッド
    private static HashMap<Integer, Integer> userApiMap = new HashMap<Integer, Integer>();
    // 20170627 GB End Add #17201 [GB内対応][gb] 予約表 速度改善 mst_user.use_api取得メソッド

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
	 * コンストラクタ
	 */
	public SystemInfo()
	{
	}

        /**
         * サーバーIPアドレスを取得する。
         * @return サーバーIPアドレス
         */
        public static String getServerIP() {
            return serverIP;
        }

        /**
         * サーバーIPアドレスを設定する。
         * @param aServerIP サーバーIPアドレス
         */
        public static void setServerIP(String aServerIP) {
            serverIP = aServerIP;
        }

        /**
         * サーバーポート番号を取得する
         * @return the サーバーポート番号
         */
        public static String getServerPort() {
            return serverPort;
        }

        /**
         * サーバーポート番号を設定する
         * @param aServerPort サーバーポート番号
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
	 * データベース名を取得する。
	 * @return データベース名
	 */
	public static String getDatabase()
	{
		return database;
	}

	/**
	 * データベース名を設定する。
	 * @param aDatabase データベース名
	 */
	public static void setDatabase(String aDatabase)
	{
		database = aDatabase;
	}

	/**
	 * ユーザーを取得する。
	 * @return ユーザー
	 */
	public static String getUser()
	{
		return user;
	}

	/**
	 * ユーザーを設定する。
	 * @param aUser ユーザー
	 */
	public static void setUser(String aUser)
	{
		user = aUser;
	}

	/**
	 * パスワードを取得する。
	 * @return パスワード
	 */
	public static String getPass()
	{
		return pass;
	}

	/**
	 * パスワードを設定する。
	 * @param aPass パスワード
	 */
	public static void setPass(String aPass)
	{
		pass = aPass;
	}

	/**
	 * ログインＩＤを設定する。
	 * @param aLoginID ログインＩＤ
	 */
	public static void setLoginID(String aLoginID)
	{
		loginID = aLoginID;
	}

	/**
	 * ログインＩＤを取得する。
	 * @return ログインＩＤ
	 */
	public static String getLoginID()
	{
		return loginID;
	}


        // Khoa Add Start 20121024 Main画面のロゴ変更
        /**
	 * ispot店舗IDを設定する。
	 * @param aLoginID ispot店舗ID
	 */
        public static void setIspotShopID(String aIspotShopID)
	{
		ispotShopID = aIspotShopID;
	}

        /**
	 * ispot店舗IDを設定する。
	 * @param aLoginID ispot店舗ID
	 */
	public static String getIspotShopID()
	{
		return ispotShopID;
	}

        // Khoa Add End 20121024 Main画面のロゴ変更
        // start add 20130122 nakhoa SetSystem
        /**
	 * nSystemを設定する。
	 * @param nSystem
	 */
        public static void setNSystem(int anSystem)
	{
		nSystem = anSystem;
	}

        /**
	 * nSystemを設定する。
	 * @param nSystem
	 */
	public static int getNSystem()
	{
		return nSystem;
	}
        // end add 20130122 nakhoa SetSystem

	/**
	 * ログインパスワードを設定する。
	 * @param aLoginPass ログインパスワード
	 */
	public static void setLoginPass(String aLoginPass)
	{
		loginPass = aLoginPass;
	}

	/**
	 * ログインパスワードを取得する。
	 * @return ログインパスワード
	 */
	public static String getLoginPass()
	{
		return loginPass;
	}

	/**
	 * WEBディレクトリ名を設定する。
	 * @param aDirectoryName WEBディレクトリ名
	 */
	public static void setDirectoryName(String aDirectoryName)
	{
		directoryName = aDirectoryName;
	}

	/**
	 * WEBディレクトリ名を取得する。
	 * @return WEBディレクトリ名
	 */
	public static String getDirectoryName()
	{
		return directoryName;
	}

	/**
	 * ＭＡＣアドレスＩＤを取得する。
	 * @return ＭＡＣアドレスＩＤ
	 */
	public static Integer getMacID()
	{
		return macID;
	}

	/**
	 * ＭＡＣアドレスＩＤをセットする。
	 * @param aMacID ＭＡＣアドレスＩＤ
	 */
	public static void setMacID(Integer aMacID)
	{
		macID = aMacID;
	}

	/**
	 * SOSIA連動が可能かを取得する
	 *   連動可能の有無は連動先基本URLが設定されているかで判断する
	 * @return SOSIA連動が可能か
	 */
	public static boolean isSosiaGearEnable()
	{
		if( sosiaGearBaseURL == null || sosiaGearBaseURL.length() == 0 ) return false;
		return true;
	}

	/**
	 * SOSIA連動用基本URLを取得する
	 * @return SOSIA連動基本URL
	 */
	public static String getSosiaGearBaseURL()
	{
		return sosiaGearBaseURL;
	}

	/**
	 * SOSIA連動用基本URLをセットする。
	 * @param setSosiaGearBaseURL SOSIA連動用基本URL
	 */
	public static void setSosiaGearBaseURL( String setSosiaGearBaseURL )
	{
		sosiaGearBaseURL = setSosiaGearBaseURL;
	}

	/**
	 * SOSIA連動用IDを取得する
	 * @return SOSIA連動ID
	 */
	public static String getSosiaGearID()
	{
		return sosiaGearID;
	}

	/**
	 * SOSIA連動用IDをセットする。
	 * @param setSosiaGearID SOSIA連動用ID
	 */
	public static void setSosiaGearID( String setSosiaGearID )
	{
		sosiaGearID = setSosiaGearID;
	}

	/**
	 * SOSIA連動用PassWordを取得する
	 * @return SOSIA連動PassWord
	 */
	public static String getSosiaGearPassWord()
	{
		return sosiaGearPassWord;
	}

	/**
	 * SOSIA連動用PassWordをセットする。
	 * @param setSosiaGearPassWord SOSIA連動用PassWord
	 */
	public static void setSosiaGearPassWord( String setSosiaGearPassWord )
	{
		sosiaGearPassWord = setSosiaGearPassWord;
	}

	/**
	 * SOSIA連動用コードを取得する
	 * @return SOSIA連動コード
	 */
	public static String getSosiaCode()
	{
		return sosiaCode;
	}

	/**
	 * SOSIA連動用コードをセットする。
	 * @param setSosiaCode SOSIA連動用コード
	 */
	public static void setSosiaCode( String setSosiaCode )
	{
		sosiaCode = setSosiaCode;
	}

	/**
	 * レシートプリンタタイプを取得する
	 * @return レシートプリンタタイプ
	 */
        public static Integer getReceiptPrinterType() {
            return receiptPrinterType;
        }

	/**
	 * レシートプリンタタイプをセットする。
	 * @param setReceiptPrinterType レシートプリンタタイプ
	 */
        public static void setReceiptPrinterType(Integer setReceiptPrinterType) {
            receiptPrinterType = setReceiptPrinterType;
        }

	/**
	 * 予約機能専用フラグを取得する
	 * @return 予約機能専用フラグ
	 */
//        public static Boolean isReservationOnly() {
//            return reservationOnly.equals(1);
//        }
        public static Integer getSystemType() {
            return systemType;
        }

	/**
	 * 予約機能専用フラグをセットする。
	 * @param setReservationOnly 予約機能専用フラグ
	 */
        public static void setReservationOnly(Integer setReservationOnly) {
            systemType = setReservationOnly;
        }
        //IVS_LVTu end edit 2016/02/24 New request #48455

	/**
	 * コネクションを取得する。（共通データベース用）
	 * @return コネクション（共通データベース用）
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
                        //閉じている場合は開き直す
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
	 * コネクションを取得する。（個別データベース用）
	 * @return コネクション（個別データベース用）
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
                //閉じている場合は開き直す
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
	 * コネクションを取得する。（メール用）
	 * @return コネクション（メール用）
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
//                        //閉じている場合は開き直す
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
	 * コネクションを取得する。（ケータイ会員取得用）
	 * @return コネクション（ケータイ会員取得用）
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
                        //閉じている場合は開き直す
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
	 * コネクションを閉じる
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
	 * コネクションを閉じる
	 * @param con コネクション
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
	 * システム日付を取得する。
	 * @return システム日付
	 */
	public static java.util.Date getSystemDate()
	{
		java.util.Date temp = null;

		try
		{
                    //コネクションを取得
                    ConnectionWrapper con = SystemInfo.getBaseConnection();
                    //サーバーのシステム日付を取得
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

		//日付を取得できていない場合
		if(temp == null)
		{
                    //ローカルのシステム日付を取得
                    temp = Calendar.getInstance().getTime();
		}

		return temp;
	}

	/**
	 * 設定情報を取得する
	 * @return 設定情報
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
	 * 会計設定情報をセットする
	 * @param accountSetting 会計設定情報
	 */
	public static void setAccountSetting(MstAccountSetting setting)
	{
                accountSetting = setting;
	}

	/**
	 * 会計設定情報を取得する
	 * @return 会計設定情報
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
	 * グループを取得する
	 * @return グループ
	 */
	public static MstGroup getGroup()
	{
		return group;
	}

	/**
	 * グループを設定する
	 * @param aGroup グループ
	 */
	public static void setGroup(MstGroup aGroup)
	{
		group = aGroup;
	}

	/**
	 * ログインＩＤに対応する店舗データを取得する。
	 * @return ログインＩＤに対応する店舗データ
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
	 * 最新のログインＩＤに対応する店舗データを更新する。
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
	 * 最新のログインＩＤに対応するグループ又は店舗データを取得する
	 * @return 最新のログインＩＤに対応するグループ又は店舗データ
	 */
	public static Object getCurrentTarget()
	{
		//グループ
		if(isGroup())
		{
			return	getGroup();
		}
		//店舗
		else
		{
			return	getCurrentShop();
		}
	}


	/**
	 * 店舗選択用コンボボックスを初期化する
	 * @param combobox コンボボックス
	 * @param type 1：グループのみ
	 * 2：店舗のみ
	 * 3：グループと店舗
	 */
	public static void initGroupShopComponents(JComboBox combobox, int type)
	{
		//グループ
		if(SystemInfo.getCurrentShop().getShopID() == 0)
		{
			if(type == 1 || type == 3)
			{
				combobox.addItem(SystemInfo.getGroup());
			}

			SystemInfo.getGroup().addGroupDataToJComboBox(combobox, type);
		}
		//店舗
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
	 * 顧客登録の店舗選択用コンボボックスを初期化する
	 * @param combobox コンボボックス
	 * @param type 1：グループのみ
	 * 2：店舗のみ
	 * 3：グループと店舗
	 */
	public static void initGropuShopComponentsForCustomer(JComboBox combobox, int type)
	{
		//顧客共有
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
	 * スタッフ選択用コンボボックスを初期化する
	 * @param combobox コンボボックス
	 */
	public static void initStaffComponent(JComboBox combobox)
	{
		try
		{
			//グループ
			if(SystemInfo.getCurrentShop().getShopID() == 0)
			{
				MstStaff.addStaffDataToJComboBox(SystemInfo.getConnection(),
						combobox, SystemInfo.getGroup().getShopIDListAll());
			}
			//店舗
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
	 * 初期表示する店舗を取得する。
	 * @return 初期表示する店舗
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
	 * ログインユーザーがグループかどうかを取得する。
	 * @return true：グループ
	 * fales：店舗
	 */
	public static boolean isGroup()
	{
		return	(SystemInfo.getCurrentShop().getShopID() == 0);
	}

	/**
	 * 支払区分のリストを取得する
	 * @return 支払区分のリスト
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
	 * 支払区分のリスト再取得する
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
	 * ログインユーザー名を取得する。
	 * @return ログインユーザー名
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
	 * ログインユーザーのメールアドレスを取得する。
	 * @return ログインユーザーのメールアドレス
	 */
	public static String getMailAddress()
	{
		//グループ
		if(SystemInfo.isGroup())
		{
			return	SystemInfo.getGroup().getMailAddress();
		}
		//店舗
		else
		{
			return	SystemInfo.getCurrentShop().getMailAddress();
		}
	}

	/**
	 * 権限を取得する。
	 * @return 権限
	 */
	public static MstAuthority getAuthority()
	{
		if(authority == null)
		{
			authority	=	new MstAuthority();
			authority.setGroup(getGroup());

			//会社の場合
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
	 * 権限をチェックする。
	 * @param index チェックする権限のインデックス
	 * @return true：OK
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
	 * パスワード制御をチェックする。
	 * @param index チェックするパスワード制御のインデックス
	 * @return true：OK
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
	 * 種別ＩＤを取得する。
	 * @return 種別ＩＤ
	 */
	public static Integer getTypeID()
	{
		return typeID;
	}

	/**
	 * 種別ＩＤをセットする。
	 * @param aTypeID 種別ＩＤ
	 */
	public static void setTypeID(Integer aTypeID)
	{
		typeID = aTypeID;
	}

	/**
	 * Enterを押したときに次のコンポーネントに移動させるKeyListenerを取得する。
	 * @return Enterを押したときに次のコンポーネントに移動させるKeyListener
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
	 * フォーカス取得時にテキストを全選択させるFocusListenerを取得する。
	 * @return フォーカス取得時にテキストを全選択させるFocusListener
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
	 * フォーカスが無くなったときに編集を終了させるクラスを取得する
	 * @return フォーカスが無くなったときに編集を終了させるクラス
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
	 * 整数値用Formatterを取得する
	 * @return 整数値用Formatter
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
	 * 小数値用Formatterを取得する
	 * @return 小数値用Formatter
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
	 * Loggerオブジェクトを初期化する
	 */
	private static void initLogger()
	{
		try
		{
			logger	=	Logger.getLogger(SystemInfo.LOGGER_NAME);

			logger.setLevel(LOGGER_LEVEL);

			//ログを出力するディレクトリを作成
			if(!SystemInfo.createLogDirectory()) return;

			//String logPath = SystemInfo.LOG_FILE_PATH + "/" + SystemInfo.LOG_FILE_NAME;
			String logPath = getLogFilePath() + "/" + SystemInfo.LOG_FILE_NAME;

			//FileHandlerを作成
			FileHandler fh = new FileHandler(logPath,
					SystemInfo.LOG_FILE_LIMIT,
					SystemInfo.LOG_FILE_COUNT,
					true);

			// Formatterを設定
			fh.setFormatter(new SimpleFormatter());

			// ログの出力先を追加
			logger.addHandler(fh);

			LogManager.getLogManager().addLogger(logger);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * ログを出力するディレクトリがなければ、作成する。
	 * @return ディレクトリが存在するか、作成できればtrue
	 */
	private static boolean createLogDirectory()
	{
		//File dir = new File(SystemInfo.LOG_FILE_PATH);
		File dir = new File(getLogFilePath());

		//ディレクトリが存在する場合
		if(dir.exists())
			return	true;
		else
			//ディレクトリを作成
			return	dir.mkdirs();
	}

	/**
	 * Loggerオブジェクトを取得する。
	 * @return Loggerオブジェクト
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
	 * 会社のアイコンを取得する
	 * @return 会社のアイコン
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
	 * グループのアイコンを取得する
	 * @return グループのアイコン
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
	 * 店舗のアイコンを取得する
	 * @return 店舗のアイコン
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
	 * コンポーネント上にマウスカーソルが乗ったときに、
	 * マウスカーソルを手のカーソルに変更するイベントリスナーを追加する
	 * @param component コンポーネント
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
	 * 選択されているスキンを取得する
	 * @return 選択されているスキン
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
	 * 選択されているスキンのパッケージを取得する
	 * @return 選択されているスキンのパッケージ
	 */
	public static String getSkinPackage()
	{
		return getSkin().getPackageName();
	}

	/**
	 * メニューの色を取得する
	 * @return メニューの色
	 */
	public static Color getMenuColor()
	{
		return getSkin().getMenuColor();
	}

	/**
	 * テーブルヘッダーのレンダラーを取得する
	 * @return テーブルヘッダーのレンダラー
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
	 * 税率を取得する
	 * @param date 対象となる日付
	 * @return 税率
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
	 * pathの画像のImageIconを取得する
	 * @param path 画像のパス
	 * @return pathの画像のImageIcon
	 */
	public static javax.swing.ImageIcon getImageIcon(String path)
	{
		return	new javax.swing.ImageIcon(
				SystemInfo.class.getResource(
						"/images/" + SystemInfo.getSkinPackage() + path));
	}

	/**
	 * バーコードリーダーを使うか
	 * @return true：使う
	 */
	public static Boolean isUseBarcodeReader()
	{
		// #18069
		if (useBarcodeReader==null)loadBarcodeReaderInfo();
		return useBarcodeReader;
	}

	/**
	 * バーコードリーダーを使うかを設定する
	 * @param aUseBarcodeReader true：バーコードリーダーを使う
	 */
	public static void setUseBarcodeReader(Boolean aUseBarcodeReader)
	{
		useBarcodeReader = aUseBarcodeReader;
	}

	/**
	 * CTIリーダーを使うか
	 * @return true：使う
	 */
	public static Boolean isUseCtiReader()
	{
		return useCtiReader;
	}
	/**
	 * CTIリーダーを使うかを設定する
	 * @param aUseCTIReader true：CTIリーダーを使う
	 */
	public static void setUseCtiReader(Boolean aUseCtiReader)
	{
		useCtiReader = aUseCtiReader;
	}
	/**
	 * CTIリーダーのパラメータを取得する
	 * @return CTIリーダーのパラメータ
	 */
	public static SerialParameters getCtiReaderParameters()
	{
		return ctiReaderParameters;
	}

	/**
	 * CTIリーダーのパラメータを設定する
	 * @param aCtieReaderParameters バーコードリーダーのパラメータ
	 */
	public static void setCtiReaderParameters(SerialParameters aCtiReaderParameters)
	{
		ctiReaderParameters = aCtiReaderParameters;
	}

	/**
	 * Ctiのコネクションを取得する
	 * @return Ctiリーダーのコネクション
	 */
	public static SerialConnection getCtiReaderConnection()
	{
		return ctiReaderConnection;
	}

	/**
	 * バーコードリーダーのパラメータを取得する
	 * @return バーコードリーダーのパラメータ
	 */
	public static SerialParameters getBarcordReaderParameters()
	{
		return barcodeReaderParameters;
	}

	/**
	 * バーコードリーダーのパラメータを設定する
	 * @param aBarcodeReaderParameters バーコードリーダーのパラメータ
	 */
	public static void setBarcordReaderParameters(SerialParameters aBarcodeReaderParameters)
	{
		barcodeReaderParameters = aBarcodeReaderParameters;
	}

	/**
	 * バーコードリーダーのコネクションを取得する
	 * @return バーコードリーダーのコネクション
	 */
	public static SerialConnection getBarcodeReaderConnection()
	{
		return barcodeReaderConnection;
	}

	/**
	 * バーコードリーダーのコネクションを開く
	 * @param parent バーコードリスナー
	 * @return true：成功
	 */
	public static boolean openBarcodeReaderConnection(
			BarcodeListener parent)

	{
		try
		{
			//バーコードリーダーのコネクションを閉じる
			closeBarcodeReaderConnection();

			barcodeReaderConnection	=	null;

			//バーコードリーダーの情報を読み込む
			loadBarcodeReaderInfo();

			if(isUseBarcodeReader())
			{
				System.out.println("バーコードリーダーのコネクション処理開始");
			//指定されているポートが存在しない場合、処理を抜ける
			if(barcodeReaderParameters.getPortName() == null ||
	                   barcodeReaderParameters.getPortName().equals("") ||
					!SerialConnection.isExistSerialPort(
							barcodeReaderParameters.getPortName()))
			{
				return	false;
			}

			//コネクションのインスタンスを取得
			barcodeReaderConnection	=	new SerialConnection(
					parent, getBarcordReaderParameters());

				//コネクションを開く
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
	 * バーコードリーダーのコネクションを閉じる
	 */
	public static void closeBarcodeReaderConnection()
	{
		if(getBarcodeReaderConnection() != null)
		{
			getBarcodeReaderConnection().closeConnection();
		}
	}

	/**
	 * バーコードリーダーの情報を読み込む
	 * @return true：成功
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
	 * バーコードリーダーの情報を取得するSQL文を取得する
	 * @return バーコードリーダーの情報を取得するSQL文
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
	 * CTIリーダーのコネクションを開く
	 * @param parent CTIリスナー
	 * @return true：成功
	 */
	public static boolean openCtiReaderConnection(CTIListener parent)
	{
		try
		{
			//CTIリーダーのコネクションを閉じる
			closeCtiReaderConnection();

			ctiReaderConnection	=	null;

			//CTIリーダーの情報を読み込む
			loadCtiReaderInfo();

			if(isUseCtiReader())
			{
				System.out.println("CTIリーダーのコネクション処理開始");
				System.out.println("getName :"+ctiReaderParameters.getPortName());
			//指定されているポートが存在しない場合、処理を抜ける
			if(ctiReaderParameters.getPortName() == null ||
					!SerialConnection.isExistSerialPort(
							ctiReaderParameters.getPortName()))
			{
					System.out.println("指定されているポートが存在しない");
				return	false;
			}
				System.out.println("インスタンス取得開始");
				System.out.flush();
			//コネクションのインスタンスを取得
			ctiReaderConnection	=	new SerialConnection(
					parent, getCtiReaderParameters());
				System.out.println("コネクションを開く処理開始");
				//コネクションを開く
				getCtiReaderConnection().openConnection();
				System.out.println("コネクションopen");
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
	 * CTIリーダーの情報を読み込む
	 * @return true：成功
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
	 * CTIリーダーの情報を取得するSQL文を取得する
	 * @return CTIリーダーの情報を取得するSQL文
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
	 * CTIリーダーのコネクションを閉じる
	 */
	public static void closeCtiReaderConnection()
	{
		if(getCtiReaderConnection() != null)
		{
			getCtiReaderConnection().closeConnection();
		}
	}

    /**
     * ポイントカードリーダ・ライターを使うか
     * @return true：使う
     */
    public static Boolean isUsePointcard()
    {
        //ポイントカードリーダ・ライターの情報を読み込む
        if(!isReadPointcard) loadPointcardInfo();
        return usePointcard;
    }

    /**
     * ポイントカードリーダ・ライターを使うかを設定する
     * @param aUsePointcard true：ポイントカードリーダ・ライターを使う
     */
    public static void setUsePointcard(Boolean aUsePointcard)
    {
        usePointcard = aUsePointcard;
    }

    /**
     * ポイントカードリーダ・ライターのパラメータを取得する
     * @return ポイントカードリーダ・ライターのパラメータ
     */
    public static SerialParameters getPointcardParameters()
    {
        return pointcardParameters;
    }

    /**
     * ポイントカードリーダ・ライターのパラメータを設定する
     * @param aPointcardParameters ポイントカードリーダ・ライターのパラメータ
     */
    public static void setPointcardParameters(SerialParameters aPointcardParameters)
    {
        pointcardParameters = aPointcardParameters;
    }

    /**
     * ポイントカードリーダ・ライターのコネクションを取得する
     * @return ポイントカードリーダ・ライターのコネクション
     */
    public static AbstractCardCommunication getPointcardConnection()
    {
        return pointcardConnection;
    }

    /**
     * ポイントカードリーダ・ライターの製品識別子を設定する
     * @param ProductId ポイントカードリーダ・ライターの製品識別子
     * （0:SANWA、1:STAR、2:NATEC）
     */
    public static void setPointcardProductId(int ProductId)
    {
        pointcardProductId = ProductId;
    }

    /**
     * ポイントカードリーダ・ライターの製品識別子を取得する
     */
    public static int getPointcardProductId()
    {
        return pointcardProductId;
    }

    /**
     * ポイントの出力区分を取得する
     * @return ポイントの出力区分
     */
    public static int getPointOutputType()
    {
        return pointOutputType;
    }

    /**
     * ポイントの出力区分を設定する
     * @param outputType ポイントの出力区分
     * （0:リライトカード、1:レシート）
     */
    public static void setPointOutputType(int outputType)
    {
        pointOutputType = outputType;
    }

    /**
     * ポイント画面の日付印字初期値フラグを取得する
     * @return ポイント画面の日付印字初期値フラグ
     */
    public static int getPointDefaultDateType() {
        return pointDefaultDateType;
    }

    /**
     * ポイント画面の日付印字初期値フラグを設定する
     * @param aPointDefaultDateType ポイント画面の日付印字初期値フラグ
     * （0：来店日付、1：次回予約日、2：賞美期限、3：次回来店案内）
     */
    public static void setPointDefaultDateType(int aPointDefaultDateType) {
        pointDefaultDateType = aPointDefaultDateType;
    }

    /**
     * ポイントカードリーダ・ライターのコネクションを開く
     * @return true：成功
     */
    public static boolean openPointcardConnection()
    {
        try
        {
            //ポイントカードリーダ・ライターのコネクションを閉じる
            closePointcardConnection();

            pointcardConnection =   null;

            //ポイントカードリーダ・ライターの情報を読み込む
            if(!isReadPointcard) loadPointcardInfo();

            if(isUsePointcard())
            {
                System.out.println("ポイントカードリーダ・ライターのコネクション処理開始");

                // ナテック以外のカードリーダーはポート存在チェックを行う
                if (pointcardProductId != 2) {
                    //指定されているポートが存在しない場合、処理を抜ける
                    if(pointcardParameters.getPortName() == null ||
                            !SerialConnection.isExistSerialPort(
                                    pointcardParameters.getPortName()))
                    {
                        return  false;
                    }
                }

                //コネクションのインスタンスを取得
                pointcardConnection = enumPointcardProduct.getConnection(
                                                    pointcardProductId,
                                                    getPointcardParameters());

                //コネクションを開く
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
     * ポイントカードリーダ・ライターのコネクションを閉じる
     */
    public static void closePointcardConnection()
    {
        if(getPointcardConnection() != null)
        {
            getPointcardConnection().Close();
        }
    }

    /**
     * ポイントカードリーダ・ライターの情報を読み込む
     * @return true：成功
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
     * ポイントカードリーダ・ライターの情報を取得するSQL文を取得する
     * @return ポイントカードリーダ・ライターの情報を取得するSQL文
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
	 * 消費税を取得する
	 * @param value 元となる数値
	 * @param date 対象となる日付
	 * @return 消費税
	 */
	public static Long getTax(Long value, java.util.Date date)
	{
		return	TaxUtil.getTax(value, getTaxRate(date), 2);
	}

	/**
	 * 消費税を取得する
	 * @param value 元となる数値
	 * @param date 対象となる日付
	 * @return 消費税
	 */
	public static Integer getTax(Integer value, java.util.Date date)
	{
		return	TaxUtil.getTax(value, getTaxRate(date), 1);
	}

	/**
	 * SOSIA連動店舗リストを取得する
	 * @return SOSIA連動店舗リスト
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
	 * SOSIA連動店舗リストを取得する
	 * @return SOSIA連動店舗リスト
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
                   + "インターネットの接続を確認して下さい。\n\n"
                   + "再度接続を試みる場合は、＜再試行＞を、\n"
                   + "システムを終了する場合は、＜終了＞を押して下さい。\n";

        Object[] options = { "  再試行  ", "  終 了  " };
        int ret = JOptionPane.showOptionDialog(
                        null,
                        msg,
                        "データベース接続エラー",
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
     * カスタマーディスプレイを使うか
     * @return true：使う
     */
    public static Boolean isUseCustomerDisplay()
    {
            return useCustomerDisplay;
    }

    /**
     * カスタマーディスプレイを使うかを設定する
     * @param aUseCustomerDisplay true：カスタマーディスプレイを使う
     */
    public static void setUseCustomerDisplay(Boolean aUseCustomerDisplay)
    {
            useCustomerDisplay = aUseCustomerDisplay;
    }

    /**
     * カスタマーディスプレイのパラメータを取得する
     * @return カスタマーディスプレイのパラメータ
     */
    public static String getCustomerDisplayPort()
    {
            return customerDisplayPort;
    }

    /**
     * カスタマーディスプレイのポートを設定する
     * @param aCustomerDisplayParameters カスタマーディスプレイのポート
     */
    public static void setCustomerDisplayPort(String aCustomerDisplayPort)
    {
            customerDisplayPort = aCustomerDisplayPort;
    }

    /**
     * カスタマーディスプレイの情報を読み込む
     * @return true：成功
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
     * バーコードリーダーの情報を取得するSQL文を取得する
     * @return バーコードリーダーの情報を取得するSQL文
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
	 * @return true：OK
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
            // 20170627 GB Start Edit #17201 [GB内対応][gb] 予約表 速度改善 mst_user.use_api取得メソッド
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
            // 20170627 GB End Edit #17201 [GB内対応][gb] 予約表 速度改善 mst_user.use_api取得メソッド

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
