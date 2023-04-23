/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.account;

import com.geobeck.sosia.pos.master.account.MstReceiptSetting;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.io.*;
import java.util.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.report.util.*;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;

/**
 *
 * @author Tran Thi Mai Loan
 */
public class PrintConfirm extends ArrayList<DeliveryConfirmData>{
    
      private MstReceiptSetting receiptSetting = null;
      private PrintService printer = null;      
      private MstCustomer customer = null;
      
      public enum ReceiptType { NORMAL, LARGE, SPECIAL };
      private ReceiptType receiptType = ReceiptType.NORMAL;
      private static final String REPORT_PATH_DELIVERY= "/report/DeliveryConfirm.jasper";
      private static final String REPORT_PATH_DELIVERY1= "/report/DeliveryConfirm1.jasper";
      
      public PrintConfirm()
	{
		this.init();
	}
	
	public void init()
	{
		setReceiptSetting(new MstReceiptSetting());
		getReceiptSetting().setShop(SystemInfo.getCurrentShop());
		
		try {
                    this.getReceiptSetting().load(SystemInfo.getConnection());
		} catch(SQLException e) {
		}
		
		this.setPrinter();
	}
        public void setReceiptSetting(MstReceiptSetting receiptSetting)
	{
		this.receiptSetting = receiptSetting;
	}
        
        public MstReceiptSetting getReceiptSetting()
                {
                        return receiptSetting;
                }

        public Integer getTotalNum() {
            Integer result=0;
            for (DeliveryConfirmData deliveryConfirmData : this) {
               result += deliveryConfirmData.getRemainNum();
            }
            return result;
        }

        
        public PrintService getPrinter()
	{
		return printer;
	}
        
        	public MstCustomer getCustomer()
	{
		return customer;
	}

	public void setCustomer(MstCustomer customer)
	{
		this.customer = customer;
	}
        
        public void setReceiptType(ReceiptType rtype){
        receiptType = rtype;
        }
	
        private void setPrinter()
	{
		DocFlavor	flavor	=	DocFlavor.SERVICE_FORMATTED.PRINTABLE;
		PrintRequestAttributeSet	aset	=	new HashPrintRequestAttributeSet();
		PrintService[]	printServices	=	PrintServiceLookup.lookupPrintServices(flavor, aset);
		
		for(PrintService ps : printServices)
		{
			if(ps.getName().equals(this.getReceiptSetting().getPrinterName()))
			{
				this.printer = ps;
				System.out.println("使用レシートプリンタセット :"+this.getPrinter().toString());
			}
		}
	}
	public boolean canPrint()
	{
		return	this.getPrinter() != null;
	}
        
        public boolean print(Integer isShop) 
	{
                return this.print(null, null,isShop);
        }
	public boolean print(MediaSizeName size, OrientationRequested orientation,Integer isShop) 
	{
		MstShop shop = this.getReceiptSetting().getShop();
                MstGroup group = new MstGroup();
                group.setGroupID(shop.getGroupID());
                try{
                    group.load(SystemInfo.getConnection());
                }
                catch( Exception ex)
                {
                }
                HashMap<String, Object> param = new HashMap<String, Object>();
                param.put("groupName", group.getGroupName());
		param.put("shopName", shop.getShopName());
                param.put("phoneNumber", shop.getPhoneNumber().length() > 0 ? shop.getPhoneNumber() : "");
                //IVS_LVTu start edit 2015/10/15 Bug #43436
                //param.put("customerName", (this.getCustomer() == null ? "" : this.getCustomer().getFullCustomerName()));
                // 顧客名が15桁過ぎの場合は15桁まで切って表示します。
                String fullName = "";
                if (this.getCustomer() != null) {
                    // 20170629 GB Start Edit #17358 [GB内対応][gb] レシートプリンタｍPOP、右ハシが切れて印字される
//                    if ( this.getCustomer().getFullCustomerName().length() > 15 ){
//                        fullName = this.getCustomer().getFullCustomerName().substring(0, 14);
                    if ( this.getCustomer().getFullCustomerName().length() > 15 ){
                        fullName = this.getCustomer().getFullCustomerName().substring(0, 15);
                    // 20170629 GB End Edit #17358 [GB内対応][gb] レシートプリンタｍPOP、右ハシが切れて印字される
                    }else {
                        fullName = this.getCustomer().getFullCustomerName();
                    }
                }
                param.put("customerName", fullName);
                //IVS_LVTu end edit 2015/10/15 Bug #43436
                param.put("memberNo", (this.getCustomer() == null ? "" : this.getCustomer().getCustomerNo()));
                param.put("totalNum", this.getTotalNum());
                param.put("flagShop", isShop);
                  
                String layout = null;
                //IVS_TMTrong start edit 2015/10/14 Bug #43436
                /*if ((SystemInfo.getSetteing().isUsePrepaid()) && (SystemInfo.getDatabase().equals("pos_hair_missionf") || SystemInfo.getDatabase().equals("pos_hair_missionf_dev"))) {
                   param.put("Label", str); 
                   
                   layout = REPORT_PATH_DELIVERY;
                   
                }else{                   
                   
                    layout = REPORT_PATH_DELIVERY1;
                    
                }*/
                String str= "";
                
                switch (receiptType) {
                    case NORMAL:
                          layout = REPORT_PATH_DELIVERY1;
                            break;
                    case LARGE:
                         layout = REPORT_PATH_DELIVERY;
                        break;
                    default:
                        throw new Error("Panic!!");
                }     
                 
                if ((SystemInfo.getSetteing().isUsePrepaid()) && (SystemInfo.getDatabase().equals("pos_hair_missionf") || SystemInfo.getDatabase().equals("pos_hair_missionf_dev"))) {
                    str="受渡/配送\n数計";
                     
                }else {
                    str="受渡数計";
                }
                param.put("Label", str);
                //IVS_TMTrong end edit 2015/10/14 Bug #43436
                
		InputStream report = PrintReceipt.class.getResourceAsStream(layout);
		
		System.out.println("レシートプリンタ :"+this.getPrinter().toString());
		
		ReportManager.exportReport(report, this.getPrinter(), 3, param, this, size, orientation);
		
		return true;
	}

    
}
