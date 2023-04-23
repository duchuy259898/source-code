/*
 * PrintReceipt.java
 *
 * Created on 2007/10/23, 15:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.account;

import com.geobeck.sosia.pos.hair.report.logic.ReportGeneratorLogic;
import java.io.*;
import java.sql.*;
import java.util.*;

import javax.print.*;
import javax.print.attribute.*;

import com.geobeck.sosia.pos.system.*;
import com.geobeck.sosia.pos.master.account.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.customer.*;
import com.geobeck.sosia.pos.report.util.*;
import java.text.SimpleDateFormat;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author katagiri
 */
public class PrintReceipt extends ArrayList<ReceiptData>
{

    /**
     * @return the cardTitle
     */
    public String getCardTitle() {
        return cardTitle.length() > 10 ? cardTitle.substring(0, 10) : cardTitle;
    }

    /**
     * @param cardTitle the cardTitle to set
     */
    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    /**
     * @return the eCashTitle
     */
    public String geteCashTitle() {
        return eCashTitle.length() > 10 ? eCashTitle.substring(0, 10) : eCashTitle;
    }

    /**
     * @param eCashTitle the eCashTitle to set
     */
    public void seteCashTitle(String eCashTitle) {
        this.eCashTitle = eCashTitle;
    }

    /**
     * @return the giftTitle
     */
    public String getGiftTitle() {
        return giftTitle.length() > 10 ? giftTitle.substring(0, 10) : giftTitle;
    }

    /**
     * @param giftTitle the giftTitle to set
     */
    public void setGiftTitle(String giftTitle) {
        this.giftTitle = giftTitle;
    }
    public enum ReceiptType { NORMAL, LARGE, SPECIAL, CASHBACK };
    
    /**
     * 商品別売上順位表レポートファイルパス
     */
    //VTAn Start add 20140613
    private static final String REPORT_PATH_STAR   = "/report/StarReceipt.jasper";
    private static final String REPORT_PATH_EPSON  = "/report/EpsonReceipt.jasper";
    private static final String REPORT_PATH_STAR_LARGE  = "/report/StarLargeReceipt.jasper";
    private static final String REPORT_PATH_EPSON_LARGE  = "/report/EpsonLargeReceipt.jasper"; 
    
    private static final String REPORT_PATH_STAR1   = "/report/StarReceipt1.jasper";
    private static final String REPORT_PATH_EPSON1  = "/report/EpsonReceipt1.jasper";
    private static final String REPORT_PATH_STAR_LARGE1  = "/report/StarLargeReceipt1.jasper";
    private static final String REPORT_PATH_EPSON_LARGE1  = "/report/EpsonLargeReceipt1.jasper";
     //VTAn End add 20140613
    private static final String REPORT_PATH_BLANK  = "/report/BlankReceipt.jasper";
    private static final String REPORT_PATH_SPECIAL  = "/report/SpecialReceipt.jasper";
    private static final String REPORT_PATH_CASHBACK  = "/report/StarCashback.jasper";
    //IVS_PTQUANG start add 2017/05/05 New Request #61615 [gb]売掛回収時にレシートを出力したい
    private static final String REPORT_PATH_RECEIPT_REPRINT  = "/report/ReceiptReprint.jasper";
    private static final String REPORT_PATH_LARGE_RECEIPT_REPRINT  = "/report/LargeReceiptReprint.jasper";
    //IVS_PTQUANG   end add 2017/05/05 New Request #61615 [gb]売掛回収時にレシートを出力したい
    //IVS_LVTu start add 2017/11/01 SPOSのカスタマイズ案件_SOSIA領収証
    private static final String REPORT_PATH_SPECIAL_YOSHIE  = "/report/YoshieReceipt.jasper";
    private static final String SUB_REPORT_PATH_YOSHIE = "/report/YoshieReceipt_subReceipt.jasper";
    //IVS_LVTu end add 2017/11/01 SPOSのカスタマイズ案件_SOSIA領収証
    // IVS_LVTU add 2019/08/28 #97064 [gb]SPOS増税対応
    private static final String SUB_REPORT_PATH_TAXES_LARGE = "/report/TaxesLargeSubReceipt.jasper";
    private static final String SUB_REPORT_PATH_TAXES_STAR_LARGE = "/report/TaxesLargeSubStarReceipt1.jasper";
    private static final String SUB_REPORT_PATH_TAXES = "/report/TaxesSubReceipt.jasper";

    /**
     * 商品別売上順位表レポート名
     */
    private static final String REPORT_NAME = "Receipt";
	
    private PrintService printer = null;
	
    private MstReceiptSetting receiptSetting = null;
	
    private MstCustomer customer = null;
	
    private Long subtotal = 0l;
    private Long subtotalInTax = 0l;
	
    private Long tax = 0l;
	
    private Long discount = 0l;
    
    private Long allDiscount = 0l;
	
    private Long sumtotal = 0l;
	
    private Long outOfValue = 0l;
	
    private Long changeValue = 0l;
	
    private MstStaff staff = null;
    private MstStaff chargeStaff = null;

    private String message = "";
    
    private Integer slipNo = 0;

    private Integer prevPoint = 0;
    private Integer addPoint = 0;
    private Integer usePoint = 0;
    private Integer totalPoint = 0;

    private Long cashValue = 0l;
    private Long cardValue = 0l;
    private Long eCashValue = 0l;
    private Long giftValue = 0l;

    private String cardTitle = "";
    private String eCashTitle = "";
    private String giftTitle = "";
    
    private boolean printCustomer = true;

    private java.util.Date salesDate = null;
    //IVS_PTQUANG start add 2017/05/05 New Request #61615 [gb]売掛回収時にレシートを出力したい
    private java.util.Date paymentDate = null;
    //IVS_PTQUANG   end add 2017/05/05 New Request #61615 [gb]売掛回収時にレシートを出力したい
    private boolean lastPage = false;
    //IVS_LVTu start add 2017/11/01 SPOSのカスタマイズ案件_SOSIA領収証
    private boolean firstPage = false;
    //IVS_LVTu end add 2017/11/01 SPOSのカスタマイズ案件_SOSIA領収証
    private String pageNo = "";
     //VTAn Start add 20140612
    private Long prevAmount = 0l;
    private Long addAmount = 0l;
    private Long useAmount = 0l;
    private Long totalAmount = 0l;
    // IVS_LVTU add 2019/08/28 #97064 [gb]SPOS増税対応
    private List<SubTaxesData> subTaxList = new ArrayList<>();
    //VTAn End add 20140612
    //IVS_LVTu start add 2017/11/01 SPOSのカスタマイズ案件_SOSIA領収証
    /** カスタマイズ判定用DB名 */
    private static final String DB_POS_HAIR_YOSHIE  = "pos_hair_yoshie";
    private static final String DB_POS_HAIR_DEV     = "pos_hair_dev";
    //IVS_LVTu end add 2017/11/01 SPOSのカスタマイズ案件_SOSIA領収証

    /** レポート型種別 (大きさ)
     */
    private ReceiptType receiptType = ReceiptType.NORMAL;

    /** レポート種別を設定する. 
     */
    public void setReceiptType(ReceiptType rtype){
        receiptType = rtype;
    }
	
	/** Creates a new instance of PrintReceipt */
	public PrintReceipt()
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

	public PrintService getPrinter()
	{
		return printer;
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

	public MstReceiptSetting getReceiptSetting()
	{
		return receiptSetting;
	}

	public void setReceiptSetting(MstReceiptSetting receiptSetting)
	{
		this.receiptSetting = receiptSetting;
	}

	public MstCustomer getCustomer()
	{
		return customer;
	}

	public void setCustomer(MstCustomer customer)
	{
		this.customer = customer;
	}

	public Long getSubtotal()
	{
		return subtotal;
	}

	public void setSubtotal(Long subtotal)
	{
		this.subtotal = subtotal;
	}

        public Long getSubtotalInTax() {
            return subtotalInTax;
        }

        public void setSubtotalInTax(Long subtotalInTax) {
            this.subtotalInTax = subtotalInTax;
        }
        
	public Long getTax()
	{
		return tax;
	}

	public void setTax(Long tax)
	{
		this.tax = tax;
	}

	public Long getDiscount()
	{
		return discount;
	}

	public void setDiscount(Long discount)
	{
		this.discount = discount;
	}

        public Long getAllDiscount() {
            return allDiscount;
        }

        public void setAllDiscount(Long allDiscount) {
            this.allDiscount = allDiscount;
        }
        
	public Long getSumtotal()
	{
		return sumtotal;
	}

	public void setSumtotal(Long sumtotal)
	{
		this.sumtotal = sumtotal;
	}

	public Long getOutOfValue()
	{
		return outOfValue;
	}

	public void setOutOfValue(Long outOfValue)
	{
		this.outOfValue = outOfValue;
	}

	public Long getChangeValue()
	{
		return changeValue;
	}

	public void setChangeValue(Long changeValue)
	{
		this.changeValue = changeValue;
	}

	public MstStaff getStaff()
	{
		return staff;
	}

	public void setStaff(MstStaff staff)
	{
		this.staff = staff;
	}

	public MstStaff getChargeStaff()
	{
		return chargeStaff;
	}

	public void setChargeStaff(MstStaff chargeStaff)
	{
		this.chargeStaff = chargeStaff;
	}
        
	public Integer getSlipNo()
	{
		return slipNo;
	}

	public void setSlipNo(Integer slipNo)
	{
		this.slipNo = slipNo;
	}

        public Integer getPrevPoint() {
            return prevPoint;
        }

        public void setPrevPoint(Integer prevPoint) {
            this.prevPoint = prevPoint;
        }

        public Integer getAddPoint() {
            return addPoint;
        }

        public void setAddPoint(Integer addPoint) {
            this.addPoint = addPoint;
        }

        public Integer getUsePoint() {
            return usePoint;
        }

        public void setUsePoint(Integer usePoint) {
            this.usePoint = usePoint;
        }

        public Integer getTotalPoint() {
            return totalPoint;
        }

        public void setTotalPoint(Integer totalPoint) {
            this.totalPoint = totalPoint;
        }
        
        public void setMessage(String m){
            this.message = m;
        }
        
        public String getMessage(){
            return message;
        }

        public Long getCashValue() {
            return cashValue;
        }

        public void setCashValue(Long cashValue) {
            this.cashValue = cashValue;
        }

        public Long getCardValue() {
            return cardValue;
        }

        public void setCardValue(Long cardValue) {
            this.cardValue = cardValue;
        }
        
        public Long getECashValue() {
            return eCashValue;
        }

        public void setECashValue(Long eCashValue) {
            this.eCashValue = eCashValue;
        }

        public Long getGiftValue() {
            return giftValue;
        }

        public void setGiftValue(Long giftValue) {
            this.giftValue = giftValue;
        }
        //VTAn Start add 20140612
        public Long getPrevAmount() {
            return prevAmount;
        }

        public void setPrevAmount(Long prevAmount) {
            this.prevAmount = prevAmount;
        }

        public Long getAddAmount() {
            return addAmount;
        }

        public void setAddAmount(Long addAmount) {
            this.addAmount = addAmount;
        }

        public Long getUseAmount() {
            return useAmount;
        }

        public void setUseAmount(Long useAmount) {
            this.useAmount = useAmount;
        }

        public Long getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(Long totalAmount) {
            this.totalAmount = totalAmount;
        }
    //IVS_LVTu start add 2017/11/01 SPOSのカスタマイズ案件_SOSIA領収証
    public boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }
    //IVS_LVTu end add 2017/11/01 SPOSのカスタマイズ案件_SOSIA領収証
    
    public void setSubTaxList(List<SubTaxesData> subTaxList) {
        this.subTaxList = subTaxList;
    }

        //VTAn End add 20140612
        
	public boolean print()
	{
                return this.print(null, null);
        }

	//IVS_PTQUANG start add 2017/05/05 New Request #61615 [gb]売掛回収時にレシートを出力したい
    /**
     *
     * @param checkSetting
     * @return 
     */
    public boolean print(boolean checkSetting) {
		MstShop shop = this.getReceiptSetting().getShop();

        HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("logoImage", this.getReceiptSetting().getLogoImage());
		param.put("shopName", shop.getShopName());

        if (shop.getPostalCode() != null && shop.getPostalCode() != "" && shop.getPostalCode().trim().length() >= 7) {
        	param.put("postalCode", "〒" + shop.getPostalCode().substring(0, 3) + "-" + shop.getPostalCode().substring(3));
        }

        param.put("address1", shop.getAddress(0) != null?shop.getAddress(0):"");
        param.put("address2", shop.getAddress(1) != null?shop.getAddress(1):"");
        param.put("address3", shop.getAddress(2) != null?shop.getAddress(2):"");
        param.put("address4", shop.getAddress(3) != null?shop.getAddress(3):"");

        param.put("phoneNumber", shop.getPhoneNumber().length() > 0 ? shop.getPhoneNumber() : null);
        param.put("faxNumber", shop.getFaxNumber().length() > 0 ? shop.getFaxNumber() : null);

        param.put("salesDate", getSalesDate());
        param.put("paymentDate", getPaymentDate());
        String fullName = "";
        if (this.getCustomer() != null) {
	        if (this.getCustomer().getFullCustomerName().length() > 15 ) {
            	fullName = this.getCustomer().getFullCustomerName().substring(0, 14);
            } else {
            	fullName = this.getCustomer().getFullCustomerName();
            }
        }
		param.put("customerName", fullName);

        param.put("customerNo", "お客様№ " + (this.getCustomer() == null ? "" : this.getCustomer().getCustomerNo()));
        param.put("subtotal", this.getTotalAmount());
        param.put("outOfValue", this.getOutOfValue());
        param.put("prevAmount", this.getPrevAmount());
        param.put("useAmount", this.getUseAmount());
        param.put("cashValue", this.getCashValue());
        param.put("cardValue", this.getCardValue());
		param.put("eCashValue", this.getECashValue());
		param.put("giftValue", this.getGiftValue());
		param.put("cardTitle", "( " + this.getCardTitle());
		param.put("eCashTitle", "( " + this.geteCashTitle());
		param.put("giftTitle", "( " + this.getGiftTitle());
        param.put("footerImage", this.getReceiptSetting().getFooterImage());
        param.put("staffName", this.getStaff().getFullStaffName());
        param.put("slipNo", this.getSlipNo());                
        
		String layout = null;
        switch (receiptType) {
        	case NORMAL:
            	layout = REPORT_PATH_RECEIPT_REPRINT;
                break;
            case LARGE:
            	layout = REPORT_PATH_LARGE_RECEIPT_REPRINT;
                break;
            default:
                throw new Error("Panic!!");
        }

		InputStream report = PrintReceipt.class.getResourceAsStream(layout);
		System.out.println("レシートプリンタ :"+this.getPrinter().toString());
		ReportManager.exportReport(report, this.getPrinter(), 3, param, this, null, null);
        return true;
    }
	//IVS_PTQUANG end add 2017/05/05 New Request #61615 [gb]売掛回収時にレシートを出力したい

	public boolean print(MediaSizeName size, OrientationRequested orientation)
	{
		MstShop shop = this.getReceiptSetting().getShop();
                //IVS_LVTu start add 2017/11/01 SPOSのカスタマイズ案件_SOSIA領収証
                List listData = new ArrayList<SubTempData>();
                //IVS_LVTu end add 2017/11/01 SPOSのカスタマイズ案件_SOSIA領収証

                HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("logoImage", this.getReceiptSetting().getLogoImage());
		param.put("shopName", shop.getShopName());
                
                //IVS_LVTu start edit 2015/10/05 Bug #43144
                //if ( shop.getPostalCode() != "" && shop.getPostalCode().trim().length() >= 7) {
                if ( shop.getPostalCode() != null && shop.getPostalCode() != "" && shop.getPostalCode().trim().length() >= 7) {
                   param.put("postalCode", "〒" + shop.getPostalCode().substring(0, 3) + "-" + shop.getPostalCode().substring(3));
                }
                //IVS_LVTu end edit 2015/10/05 Bug #43144
                
                
                 //nhanvt start edit 20141031 Request #31902
                param.put("address1", shop.getAddress(0) !=null?shop.getAddress(0): "");
                param.put("address2", shop.getAddress(1) !=null?shop.getAddress(1) : "");
                param.put("address3", shop.getAddress(2) != null?shop.getAddress(2) : "");
                param.put("address4", shop.getAddress(3) != null?shop.getAddress(3) : "");
                
                //nhanvt end edit 20141031 Request #31902
                param.put("phoneNumber", shop.getPhoneNumber().length() > 0 ? shop.getPhoneNumber() : null);
                param.put("faxNumber", shop.getFaxNumber().length() > 0 ? shop.getFaxNumber() : null);
                
                // #43651 201803 GB start edit 売上日付を表示するように変更
                //param.put("salesDate", getSalesDate());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 (E)");
                param.put("salesDate", sdf.format(getSalesDate()));
                // #43651 201803 GB end edit 売上日付を表示するように変更
                
                param.put("isLastPage", isLastPage());
                param.put("pageNo", getPageNo());
                //IVS_LVTu start add 2017/11/01 SPOSのカスタマイズ案件_SOSIA領収証
                param.put("isFirstPage", isFirstPage());
                listData.add(new SubTempData(this.getSalesDate(), this.getCustomer().getCustomerNo(), this.getCustomer().getFullCustomerName(), this.getSumtotal()));
                param.put("yoshieSubReport", getSubReportCategoryJasperReport());
                param.put("yoshieDataSource", new JRBeanCollectionDataSource(listData));
                //IVS_LVTu end add 2017/11/01 SPOSのカスタマイズ案件_SOSIA領収証
                
                //nhanvt start add 20141031 Request #31902
                if (SystemInfo.getDatabase().startsWith("pos_hair_ashsinga")) {
                    param.put("dvMoney", "pos_hair_ashsinga");
                }else if(SystemInfo.getDatabase().startsWith("pos_hair_ashjakar")){
                    param.put("dvMoney", "pos_hair_ashjakar");
                }else{
                    param.put("dvMoney", "none");
                }
                //nhanvt end add 20141031 Request #31902

                Integer sosiaID = null;
                if (this.getCustomer() != null) {
                    sosiaID = this.getCustomer().getSosiaCustomer().getSosiaID();
                    if (sosiaID != null && sosiaID == 0) {
                        sosiaID = null;
                    }
                }
                
                if (isPrintCustomer()) {
                    //IVS_LVTu start edit 2015/10/15 Bug #43436
                    //param.put("customerName", (this.getCustomer() == null ? "" : this.getCustomer().getFullCustomerName()));
                    // 顧客名が15桁過ぎの場合は15桁まで切って表示します。
                    String fullName = "";
                    if (this.getCustomer() != null) {
                        // 20170629 GB Start Edit #17358 [GB内対応][gb] レシートプリンタｍPOP、右ハシが切れて印字される
                        // 顧客名が13桁過ぎの場合は13桁まで切って表示します。
//                        if ( this.getCustomer().getFullCustomerName().length() > 15 ){
//                            fullName = this.getCustomer().getFullCustomerName().substring(0, 14);
                        if ( this.getCustomer().getFullCustomerName().length() > 13 ){
                            fullName = this.getCustomer().getFullCustomerName().substring(0, 13);
                        // 20170629 GB End Edit #17358 [GB内対応][gb] レシートプリンタｍPOP、右ハシが切れて印字される
                        }else {
                            fullName = this.getCustomer().getFullCustomerName();
                        }
                    }
                    param.put("customerName", fullName);
                    //IVS_LVTu end edit 2015/10/15 Bug #43436
                    param.put("customerNo", "お客様№ " + (this.getCustomer() == null ? "" : this.getCustomer().getCustomerNo()));
                    param.put("sosiaID", sosiaID);
                } else {
                    if (receiptType == ReceiptType.LARGE) {
                        param.put("customerName", "　　　　　　　　　　　　　　　");
                    } else {
                        param.put("customerName", "　　　　　　　　　　　");
                    }
                }
                
                // IVS_LVTU add 2019/08/28 #97064 [gb]SPOS増税対応
                List listTax = new ArrayList<>();
                if (this.subTaxList != null) {
                    for (SubTaxesData subTax : this.subTaxList) {
                        listTax.add(new SubTaxesData(subTax.getTaxRateLabel() + "%", subTax.getTargetTotal(), subTax.getTax(), param.get("dvMoney").toString()));
                    }
                }
                param.put("taxesSubReport", getSubReportTaxJasperReport());
                param.put("taxesDataSource", new JRBeanCollectionDataSource(listTax));
                
                param.put("subtotal", this.getSubtotal());
                param.put("subtotalInTax", this.getSubtotalInTax());
		param.put("tax", this.getTax());
		param.put("discount", this.getDiscount());
		param.put("allDiscount", this.getAllDiscount());
		param.put("sumtotal", this.getSumtotal());
		param.put("outOfValue", this.getOutOfValue());
		param.put("changeValue", this.getChangeValue());
		param.put("message", this.getReceiptSetting().getMessage());
		param.put("footerImage", this.getReceiptSetting().getFooterImage());
                // vtbphuong start change 20150513 
		//param.put("staffName", (this.getStaff() == null ? "" : this.getStaff().getFullStaffName()));
		//param.put("chargeStaffName", (this.getChargeStaff() == null ? "" : this.getChargeStaff().getFullStaffName()));
                
                param.put("staffName", (this.getStaff() == null ? "" :( (this.getStaff().getDisplayName() ==null|| this.getStaff().getDisplayName().equals(""))  ?this.getStaff().getFullStaffName():this.getStaff().getDisplayName()) ));
		param.put("chargeStaffName", (this.getChargeStaff() == null ? "" :((  this.getChargeStaff().getDisplayName()==null || this.getChargeStaff().getDisplayName().equals(""))  ?this.getChargeStaff().getFullStaffName():this.getChargeStaff().getDisplayName())));
                 // vtbphuong end change 20150513 
                param.put("message", this.getMessage());
                param.put("slipNo", this.getSlipNo());

		param.put("cashValue", this.getCashValue());
		param.put("cardValue", this.getCardValue());
		param.put("eCashValue", this.getECashValue());
		param.put("giftValue", this.getGiftValue());

		param.put("cardTitle", "( " + this.getCardTitle());
		param.put("eCashTitle", "( " + this.geteCashTitle());
		param.put("giftTitle", "( " + this.getGiftTitle());
                
                // ポイント情報
                if( (SystemInfo.isUsePointcard() && (SystemInfo.getPointOutputType() == 1 || SystemInfo.getPointOutputType() == 2)) ){
                    param.put("prevPoint", getPrevPoint());
                    param.put("addPoint", getAddPoint());
                    param.put("usePoint", getUsePoint());
                    param.put("totalPoint", getTotalPoint());
                }
                //VTAn Start add 20140612
                if ((SystemInfo.getSetteing().isUsePrepaid()) && (SystemInfo.getDatabase().equals("pos_hair_missionf") || SystemInfo.getDatabase().equals("pos_hair_missionf_dev"))) {
                    param.put("prevP", getPrevAmount() ==null ? "0" : getPrevAmount());
                    param.put("addP", getAddAmount() == null ? "0" : getAddAmount());
                    param.put("useP", getUseAmount()==null ? "0" : getUseAmount());
                    param.put("totalP", getTotalAmount()==null ? "0" : getTotalAmount());
                }
                
                //VTAn End add 20140612
                
                String layout = null;
                switch(receiptType)
                {
                    case NORMAL:
                            //VTAN Start Edit 20140613
                        if ((SystemInfo.getSetteing().isUsePrepaid()) && (SystemInfo.getDatabase().equals("pos_hair_missionf") || SystemInfo.getDatabase().equals("pos_hair_missionf_dev"))) {
                            if (SystemInfo.getReceiptPrinterType().equals(1)) {
                                layout = REPORT_PATH_EPSON;
                            } else {
                                layout = REPORT_PATH_STAR;
                            }
                        } else {
                            if (SystemInfo.getReceiptPrinterType().equals(1)) {
                                layout = REPORT_PATH_EPSON1;
                            } else {
                                layout = REPORT_PATH_STAR1;
                            }
                           //VTAN End Edit 20140613
                        }         
                        break;
                    case LARGE:
                        //VTAN Start Edit 20140613
                        if ((SystemInfo.getSetteing().isUsePrepaid()) && (SystemInfo.getDatabase().equals("pos_hair_missionf") || SystemInfo.getDatabase().equals("pos_hair_missionf_dev"))) {
                            if (SystemInfo.getReceiptPrinterType().equals(1)) {
                                layout = REPORT_PATH_EPSON_LARGE;
                            } else {
                                layout = REPORT_PATH_STAR_LARGE;
                            }
                        } else {
                            if (SystemInfo.getReceiptPrinterType().equals(1)) {
                                layout = REPORT_PATH_EPSON_LARGE1;
                            } else {
                                layout = REPORT_PATH_STAR_LARGE1;
                            }
                        }
                        //VTAN End Edit 20140613
                        break;
                    case SPECIAL:
                        //IVS_LVTu start add 2017/11/01 SPOSのカスタマイズ案件_SOSIA領収証
                        if (SystemInfo.getDatabase().equals(DB_POS_HAIR_YOSHIE)
                                || SystemInfo.getDatabase().equals(DB_POS_HAIR_DEV)) {
                            layout = REPORT_PATH_SPECIAL_YOSHIE;
                        } else {
                            layout = REPORT_PATH_SPECIAL;
                        }
                        //IVS_LVTu end add 2017/11/01 SPOSのカスタマイズ案件_SOSIA領収証
                        break;
                    case CASHBACK:
                        layout = REPORT_PATH_CASHBACK;
                        break;
                    default:
                        throw new Error("Panic!!");
                }

		InputStream report = PrintReceipt.class.getResourceAsStream(layout);
		
		System.out.println("レシートプリンタ :"+this.getPrinter().toString());
		
		ReportManager.exportReport(report, this.getPrinter(), 3, param, this, size, orientation);
		
		return true;
	}

	public boolean drawerOpenPrint()
	{
                this.add(null);
                HashMap<String, Object> param = new HashMap<String, Object>();
		InputStream report = PrintReceipt.class.getResourceAsStream(REPORT_PATH_BLANK);
		ReportManager.exportReport(report, this.getPrinter(), 3, param, this);
		
		return true;
	}

    public boolean isPrintCustomer() {
        return printCustomer;
    }

    public void setPrintCustomer(boolean printCustomer) {
        this.printCustomer = printCustomer;
    }

    /**
     * @return the lastPage
     */
    public boolean isLastPage() {
        return lastPage;
    }

    /**
     * @param lastPage the lastPage to set
     */
    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    /**
     * @return the pageNo
     */
    public String getPageNo() {
        return pageNo;
    }

    /**
     * @param pageNo the pageNo to set
     */
    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    /**
     * @return the salesDate
     */
    public java.util.Date getSalesDate() {
        return salesDate;
    }

    /**
     * @param salesDate the salesDate to set
     */
    public void setSalesDate(java.util.Date salesDate) {
        this.salesDate = salesDate;
    }

    /**
     * @return the paymentDate
     */
    public java.util.Date getPaymentDate() {
        return paymentDate;
    }

    /**
     * @param paymentDate the paymentDate to set
     */
    public void setPaymentDate(java.util.Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    //IVS_LVTu start add 2017/11/01 SPOSのカスタマイズ案件_SOSIA領収証
    /**
     * Put sub report into main report
     * @param filename
     * @param filetype
     * @return
     * @throws Exception 
     */
    public JasperReport loadReport(String filename, int filetype) throws Exception {        
        InputStream report = null;
        JasperReport jasperReport = null;

        report = ReportGeneratorLogic.class.getResourceAsStream(filename);

        jasperReport = (JasperReport) JRLoader.loadObject(report);

        return jasperReport;
    }

    /** 
    * Get sub jaspter report
     */
    private JasperReport getSubReportCategoryJasperReport() {
        JasperReport jasperReport = null;
        try {
            jasperReport = this.loadReport(SUB_REPORT_PATH_YOSHIE, ReportGeneratorLogic.REPORT_FILE_TYPE_JASPER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jasperReport;
    }
    
    /** 
    * Get sub jasper report tax
     */
    private JasperReport getSubReportTaxJasperReport() {
        JasperReport jasperReport = null;
        try {
             switch(receiptType)
                {
                    case NORMAL:
                         jasperReport = this.loadReport(SUB_REPORT_PATH_TAXES, ReportGeneratorLogic.REPORT_FILE_TYPE_JASPER);
                        break;
                    case LARGE:
                         if ((SystemInfo.getSetteing().isUsePrepaid()) && (SystemInfo.getDatabase().equals("pos_hair_missionf") || SystemInfo.getDatabase().equals("pos_hair_missionf_dev"))) {
                            jasperReport = this.loadReport(SUB_REPORT_PATH_TAXES_LARGE, ReportGeneratorLogic.REPORT_FILE_TYPE_JASPER);
                        } else {
                            if (SystemInfo.getReceiptPrinterType().equals(1)) {
                                jasperReport = this.loadReport(SUB_REPORT_PATH_TAXES_LARGE, ReportGeneratorLogic.REPORT_FILE_TYPE_JASPER);
                            } else {
                                jasperReport = this.loadReport(SUB_REPORT_PATH_TAXES_STAR_LARGE, ReportGeneratorLogic.REPORT_FILE_TYPE_JASPER);
                            }
                        }
                        break;
                    default:
                        break;
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jasperReport;
    }

    public class SubTempData {
        private java.util.Date salesDate = null;
        private String customerNo        = "";
        private String customerName      = "";
        private Long sumtotal            = null;

        public java.util.Date getSalesDate() {
            return salesDate;
        }

        public void setSalesDate(java.util.Date salesDate) {
            this.salesDate = salesDate;
        }

        public String getCustomerNo() {
            return customerNo;
        }

        public void setCustomerNo(String customerNo) {
            this.customerNo = customerNo;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public Long getSumtotal() {
            return sumtotal;
        }

        public void setSumtotal(Long sumtotal) {
            this.sumtotal = sumtotal;
        }

        public SubTempData()
        {
        }

        public SubTempData(java.util.Date salesDate, String customerNo, String customerName, Long sumtotal)
        {
            this.salesDate = salesDate;
            this.customerNo = customerNo;
            this.customerName = customerName;
            this.sumtotal = sumtotal;
        }

    }
    //IVS_LVTu end add 2017/11/01 SPOSのカスタマイズ案件_SOSIA領収証
    
    // IVS_LVTU add 2019/08/28 #97064 [gb]SPOS増税対応
    public class SubTaxesData {
        
        private String taxRateLabel        = "";
        private Long targetTotal           = 0l;
        private Long tax                   = 0l;
        private String dvMoney             = "";

        public String getTaxRateLabel() {
            return taxRateLabel;
        }

        public void setTaxRateLabel(String taxRateLabel) {
            this.taxRateLabel = taxRateLabel;
        }

        public Long getTargetTotal() {
            return targetTotal;
        }

        public void setTargetTotal(Long targetTotal) {
            this.targetTotal = targetTotal;
        }

        public Long getTax() {
            return tax;
        }

        public void setTax(Long tax) {
            this.tax = tax;
        }
        
        public String getDvMoney() {
            return dvMoney;
        }

        public void setDvMoney(String dvMoney) {
            this.dvMoney = dvMoney;
        }

        public SubTaxesData()
        {
        }

        public SubTaxesData(String taxRateLabel, Long targetTotal, Long tax)
        {
            this.taxRateLabel = taxRateLabel;
            this.targetTotal = targetTotal;
            this.tax = tax;
        }
        
        public SubTaxesData(String taxRateLabel, Long targetTotal, Long tax, String dvMoney)
        {
            this.taxRateLabel = taxRateLabel;
            this.targetTotal = targetTotal;
            this.tax = tax;
            this.dvMoney = dvMoney;
        }
    }
}
