/*
 * PrintQR.java
 *
 * Created on 2007/10/23, 15:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.account;

import java.io.*;
import java.sql.*;
import java.util.*;

import javax.print.*;
import javax.print.attribute.*;

import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.report.util.*;
import com.geobeck.sosia.pos.basicinfo.company.VisitKarteSetting;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.util.logging.Level;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.ImageIcon;

/***
 * カルテ入力QRコードを印刷
 * @author GB
 * 
 */
public class PrintQR
{
    
    /** ファイルパス
     */
    private static final String REPORT_PATH_STAR         = "/report/StarQR.jasper";
    private static final String REPORT_PATH_EPSON        = "/report/EpsonQR.jasper";
    private static final String REPORT_PATH_STAR_LARGE   = "/report/StarLargeQR.jasper";
    private static final String REPORT_PATH_EPSON_LARGE  = "/report/EpsonLargeQR.jasper";
    private static final String REPORT_PATH_SPECIAL      = "/report/SpecialQR.jasper";
    private static final String REPORT_PATH_A4           = "/report/A4QR.jasper";


    /** レシートの大きさ
     */
    private enum ReceiptType { NORMAL, LARGE, SPECIAL, A4 };
    
    private ReceiptType receiptType = ReceiptType.NORMAL;
    
    /** 使用プリンタ
     */
    private PrintService printer = null;

    
    private MstShop	shop			= null;
    
    private String	printerName		= "";
    
    private Integer	receiptSize		= -1;
    
    
    
	/** Creates a new instance of PrintQR */
	public PrintQR() {
            
		this.init();
	}
        
        private void init() {
            this.load();
            this.setReceiptType();
            this.searchPrinter();
        }
        
        
        /**
         * ロード
         */
        private void load() {
            
            this.setShop(SystemInfo.getCurrentShop());
            
            try {
                ConnectionWrapper con = SystemInfo.getConnection();
                ResultSetWrapper rs = con.executeQuery(this.getSelectSQL());
                
                if(rs.next()) {
                    setPrinterName(rs.getString("printer_name"));
                    setReceiptSize((rs.getInt("receipt_size")));
                }
                
                rs.close();
                
            }catch(SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }
        
        
        /**
         * 店舗情報取得
         * @return MstShop
         */
        public MstShop getShop() {
                return shop;
        }

        /**
         * 店舗情報設定
         * @param shop 
         */
        public void setShop(MstShop shop) {
                this.shop = shop;
        }
        
        /**
         * 使用プリンタの名前取得
         * @return 使用プリンタの名前
         */
        public String getPrinterName() {
                return printerName;
        }

        /**
         * 使用プリンタの名前設定
         * @param printerName 
         */
        public void setPrinterName(String printerName) {
                this.printerName = printerName;
        }
        
        /**
         * 使用レシートサイズ取得
         * @return 0:58mm 1:80mm 2:専用伝票 -1:レシート設定なし（A4サイズ）
         */
        public Integer getReceiptSize() {
                return receiptSize;
        }

        /**
         * 使用レシートサイズ設定
         * @param receiptSize 
         */
        public void setReceiptSize(Integer receiptSize) {
                this.receiptSize = receiptSize;
        }
        
        
        /*
         * 使用プリンタを取得
        */
        public PrintService getPrinter() {
             return printer;
	}
        
        /**
         * 使用プリンタを設定
         * @param ps 
         */
        public void setPrinter(PrintService ps) {
            this.printer = ps;
        }
               
        /*
         * 使用するプリンタを探す
        */
        private void searchPrinter() {
		DocFlavor	flavor	=	DocFlavor.SERVICE_FORMATTED.PRINTABLE;
		PrintRequestAttributeSet	aset	=	new HashPrintRequestAttributeSet();
		PrintService[]	printServices	=	PrintServiceLookup.lookupPrintServices(flavor, aset);
		
		int i = 0;
                if(this.getReceiptSize() > -1) {
                    // レシプリ使用
                    for(i = 0; i < printServices.length; i++) {
                        if(printServices[i].getName().equals(this.getPrinterName())) {
                                this.setPrinter(printServices[i]);
                                System.out.println("使用プリンタセット :"+this.getPrinter().toString());
                                break;
                        }
                    }
                }
                if(this.getReceiptSize() == -1 || (this.getReceiptSize() > -1 && i >= printServices.length)) {
                    // レシプリ未使用 or mst_receipt_settingにデータがあるがプリンタが見つからない時
                    // A4に設定する
                    this.setReceiptSize(-1);
                    this.setReceiptType();
                    // デフォルトプリントサービスを探す
                    PrintService defaultPrintServices = PrintServiceLookup.lookupDefaultPrintService();
                    if(defaultPrintServices != null) {
                            this.setPrinter(defaultPrintServices);
                            System.out.println("使用プリンタセット :"+this.getPrinter().toString());
                    }
                }
	}
        
        
        
        /**
         * レシート種別を設定
         * ※レシートを使用していない店舗はA4サイズに設定
         */
        private void setReceiptType() {
            
            switch (this.getReceiptSize()) {
                case 0:
                    receiptType = ReceiptType.LARGE;
                    break;
                case 1:
                    receiptType = ReceiptType.NORMAL;
                    break;
                case 2:
                    receiptType = ReceiptType.SPECIAL;
                    break;
                case -1:
                    receiptType = ReceiptType.A4;
                    break;
            }   
        }
    

	
	/**
         * 印刷可否
         * @return true:使用プリンタあり false:使用プリンタなし
         */
        public boolean canPrint() {
		return	this.getPrinter() != null;
	}

        
        
	/**
         * 印刷処理
         * @param img QRコードの画像キャプチャ
         * @param karteQrID カルテID
         * @param size null
         * @param orientation null
         * @return true:成功 false:失敗
         */
        public boolean print(ImageIcon img, Integer karteQrID, MediaSizeName size, OrientationRequested orientation) {
		try {
                    VisitKarteSetting vks = new VisitKarteSetting();

                    HashMap<String, Object> param = new HashMap<String, Object>();
                    param.put("qrImage", img);
                    param.put("shopName", shop.getShopName());
                    param.put("exprirationTime", vks.getExprirationTime(karteQrID));

                    String layout = null;

                    switch(receiptType) {
                        
                        case NORMAL:
                            if (SystemInfo.getReceiptPrinterType().equals(1)) {
                                    layout = REPORT_PATH_EPSON;
                                } else {
                                    layout = REPORT_PATH_STAR;
                                }       
                            break;
                        case LARGE:
                                if (SystemInfo.getReceiptPrinterType().equals(1)) {
                                    layout = REPORT_PATH_EPSON_LARGE;
                                } else {
                                    layout = REPORT_PATH_STAR_LARGE;
                                }
                            break;
                        case SPECIAL:
                            layout = REPORT_PATH_SPECIAL;
                            break;
                        case A4:
                            layout = REPORT_PATH_A4;
                            break;
                        default:
                            throw new Error("Panic!!");
                    }

                    InputStream report = PrintReceipt.class.getResourceAsStream(layout);

                    System.out.println("プリンタ :"+this.getPrinter().toString());
                    
                    ArrayList<String> tmpArr = new ArrayList<>();
                    tmpArr.add("");

                    ReportManager.exportReport(report, this.getPrinter(), 3, param, tmpArr, size, orientation);
                    
                }catch(Exception e) {
                    SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                    return false;
                }

		return true;
	}
        
        
        /**
         * レシート設定取得SQL
         * @return SQL文
         */
        private String getSelectSQL() {
            StringBuilder sql = new StringBuilder();
            sql.append("select printer_name, receipt_size ");
            sql.append(" from mst_receipt_setting where shop_id = ");
            sql.append(SQLUtil.convertForSQL(this.getShop().getShopID()));

            return sql.toString();
        }

}

