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
 * �J���e����QR�R�[�h�����
 * @author GB
 * 
 */
public class PrintQR
{
    
    /** �t�@�C���p�X
     */
    private static final String REPORT_PATH_STAR         = "/report/StarQR.jasper";
    private static final String REPORT_PATH_EPSON        = "/report/EpsonQR.jasper";
    private static final String REPORT_PATH_STAR_LARGE   = "/report/StarLargeQR.jasper";
    private static final String REPORT_PATH_EPSON_LARGE  = "/report/EpsonLargeQR.jasper";
    private static final String REPORT_PATH_SPECIAL      = "/report/SpecialQR.jasper";
    private static final String REPORT_PATH_A4           = "/report/A4QR.jasper";


    /** ���V�[�g�̑傫��
     */
    private enum ReceiptType { NORMAL, LARGE, SPECIAL, A4 };
    
    private ReceiptType receiptType = ReceiptType.NORMAL;
    
    /** �g�p�v�����^
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
         * ���[�h
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
         * �X�܏��擾
         * @return MstShop
         */
        public MstShop getShop() {
                return shop;
        }

        /**
         * �X�܏��ݒ�
         * @param shop 
         */
        public void setShop(MstShop shop) {
                this.shop = shop;
        }
        
        /**
         * �g�p�v�����^�̖��O�擾
         * @return �g�p�v�����^�̖��O
         */
        public String getPrinterName() {
                return printerName;
        }

        /**
         * �g�p�v�����^�̖��O�ݒ�
         * @param printerName 
         */
        public void setPrinterName(String printerName) {
                this.printerName = printerName;
        }
        
        /**
         * �g�p���V�[�g�T�C�Y�擾
         * @return 0:58mm 1:80mm 2:��p�`�[ -1:���V�[�g�ݒ�Ȃ��iA4�T�C�Y�j
         */
        public Integer getReceiptSize() {
                return receiptSize;
        }

        /**
         * �g�p���V�[�g�T�C�Y�ݒ�
         * @param receiptSize 
         */
        public void setReceiptSize(Integer receiptSize) {
                this.receiptSize = receiptSize;
        }
        
        
        /*
         * �g�p�v�����^���擾
        */
        public PrintService getPrinter() {
             return printer;
	}
        
        /**
         * �g�p�v�����^��ݒ�
         * @param ps 
         */
        public void setPrinter(PrintService ps) {
            this.printer = ps;
        }
               
        /*
         * �g�p����v�����^��T��
        */
        private void searchPrinter() {
		DocFlavor	flavor	=	DocFlavor.SERVICE_FORMATTED.PRINTABLE;
		PrintRequestAttributeSet	aset	=	new HashPrintRequestAttributeSet();
		PrintService[]	printServices	=	PrintServiceLookup.lookupPrintServices(flavor, aset);
		
		int i = 0;
                if(this.getReceiptSize() > -1) {
                    // ���V�v���g�p
                    for(i = 0; i < printServices.length; i++) {
                        if(printServices[i].getName().equals(this.getPrinterName())) {
                                this.setPrinter(printServices[i]);
                                System.out.println("�g�p�v�����^�Z�b�g :"+this.getPrinter().toString());
                                break;
                        }
                    }
                }
                if(this.getReceiptSize() == -1 || (this.getReceiptSize() > -1 && i >= printServices.length)) {
                    // ���V�v�����g�p or mst_receipt_setting�Ƀf�[�^�����邪�v�����^��������Ȃ���
                    // A4�ɐݒ肷��
                    this.setReceiptSize(-1);
                    this.setReceiptType();
                    // �f�t�H���g�v�����g�T�[�r�X��T��
                    PrintService defaultPrintServices = PrintServiceLookup.lookupDefaultPrintService();
                    if(defaultPrintServices != null) {
                            this.setPrinter(defaultPrintServices);
                            System.out.println("�g�p�v�����^�Z�b�g :"+this.getPrinter().toString());
                    }
                }
	}
        
        
        
        /**
         * ���V�[�g��ʂ�ݒ�
         * �����V�[�g���g�p���Ă��Ȃ��X�܂�A4�T�C�Y�ɐݒ�
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
         * �����
         * @return true:�g�p�v�����^���� false:�g�p�v�����^�Ȃ�
         */
        public boolean canPrint() {
		return	this.getPrinter() != null;
	}

        
        
	/**
         * �������
         * @param img QR�R�[�h�̉摜�L���v�`��
         * @param karteQrID �J���eID
         * @param size null
         * @param orientation null
         * @return true:���� false:���s
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

                    System.out.println("�v�����^ :"+this.getPrinter().toString());
                    
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
         * ���V�[�g�ݒ�擾SQL
         * @return SQL��
         */
        private String getSelectSQL() {
            StringBuilder sql = new StringBuilder();
            sql.append("select printer_name, receipt_size ");
            sql.append(" from mst_receipt_setting where shop_id = ");
            sql.append(SQLUtil.convertForSQL(this.getShop().getShopID()));

            return sql.toString();
        }

}

