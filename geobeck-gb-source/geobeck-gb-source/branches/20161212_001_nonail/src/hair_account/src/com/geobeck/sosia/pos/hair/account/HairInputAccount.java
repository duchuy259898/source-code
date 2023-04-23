/*
 * HairInputAccount.java
 *
 * Created on 2006/05/29, 11:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.account;

import com.geobeck.sosia.pos.data.account.DataPayment;
import com.geobeck.sosia.pos.data.account.DataPaymentDetail;
import com.geobeck.sosia.pos.master.account.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.product.*;
import com.geobeck.sosia.pos.products.*;
import com.geobeck.sosia.pos.hair.data.account.*;
import com.geobeck.sosia.pos.hair.data.company.*;
import com.geobeck.sosia.pos.hair.data.product.*;
import com.geobeck.sosia.pos.hair.data.reservation.*;
import com.geobeck.sosia.pos.hair.master.product.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.util.*;
import java.util.logging.*;
import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.account.*;
import com.geobeck.sosia.pos.hair.data.course.DataContractDigestion;
import com.geobeck.sosia.pos.hair.master.company.MstBed;
import com.geobeck.swing.MessageDialog;
import com.ibm.icu.text.DecimalFormat;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.JOptionPane;

/**
 * 伝票入力処理（リラクゼーション用）
 *
 * @author katagiri
 */
public class HairInputAccount {

    /**
     * 会計設定マスタ
     */
    protected MstAccountSetting accountSetting = new MstAccountSetting();
    /**
     * 割引マスタ
     */
    protected MstDiscounts discounts = new MstDiscounts();
    /**
     * 支払区分
     */
    protected MstPaymentClasses paymentClasses = new MstPaymentClasses();
    /**
     * 合計
     */
    protected ArrayList<NameValue> total = new ArrayList<NameValue>();
    /**
     * 売掛金データ
     */
    protected DataPayment bill = new DataPayment();
    /**
     * スタッフリスト
     */
    protected MstStaffs staffs = null;
    
    // IVS_Thanh start add 2014/07/07 GB_Mashu_業態主担当設定
    public ArrayList<DataReservationMainstaff> reservationMainstaffs = new ArrayList<DataReservationMainstaff>();
    private boolean tempSave = false;
    
    //nhanvt start add 20150326 Bug #35729
    private MstAccountSetting msSetting = null;

    public MstAccountSetting getMsSetting() {
        return msSetting;
    }

    public void setMsSetting(MstAccountSetting msSetting) {
        this.msSetting = msSetting;
    }
    //nhanvt end add 20150326 Bug #35729
    
    

    /**
     * @return the tempSave
     */
    public boolean isTempSave() {
        return tempSave;
    }

    /**
     * @param tempSave the tempSave to set
     */
    public void setTempSave(boolean tempSave) {
        this.tempSave = tempSave;
    }
    // IVS_Thanh end add 2014/07/07 GB_Mashu_業態主担当設定
    
    /**
     * 伝票ヘッダデータ
     */
    protected DataSales hairSales = new DataSales(SystemInfo.getTypeID());
    public Integer reservationNo = null;
    private Double taxRate = 0d;
    /**
     * 顧客状態 1：予約状態 2：在店状態 3：退店状態
     */
    private int status = 2;
    /**
     * 顧客副状態 1：施術待ち 2：施術中
     */
    private int subStatus = 1;
    private boolean skipSales = false;
    /**
     * 按分
     */
    private ArrayList<DataProportionally> proportionallys = new ArrayList<DataProportionally>();
    /**
     * レスポンスデータ
     */
    protected DataResponseEffect[] dre = new DataResponseEffect[2];
    //コース消化合計額
    private long cunsumptionTotal = 0;

    //施術時間の変更 start
    /** 施術時間（分） */
    protected String opeMinute = null;
    /** 施術時間（秒） */
    protected String opeSecond = null;
    //施術時間の変更 end
    
    /**
     * Creates a new instance of HairInputAccount
     */
    public HairInputAccount() {
        super();
        dre[0] = new DataResponseEffect();
        dre[1] = new DataResponseEffect();
    }

    public MstAccountSetting getAccountSetting() {
        return accountSetting;
    }

    public void setAccountSetting(MstAccountSetting accountSetting) {
        this.accountSetting = accountSetting;
    }

    public void setShop(MstShop ms) {
        hairSales.setShop(ms);
    }

    public MstShop getShop() {
        return hairSales.getShop();
    }

    public MstStaffs getStaffs() {
        if (staffs == null) {
            return getStaffs(SystemInfo.getCurrentShop());
        }
        return staffs;
    }

    public MstStaffs getStaffs(MstShop ms) {
        staffs = new MstStaffs();
        if (ms != null) {
            staffs.setShopIDList(ms.getShopID().toString());
        } else {
            staffs.setShopIDList(SystemInfo.getCurrentShop().getShopID().toString());
        }

        try {

            staffs.load(SystemInfo.getConnection(), true);

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return staffs;
    }

    public void setStaffs(MstStaffs staffs) {
        this.staffs = staffs;
    }
    
    // IVS_Thanh start add 2014/07/07 GB_Mashu_業態主担当設定
    public ArrayList<DataReservationMainstaff> getReservationMainstaffs() {
        return reservationMainstaffs;
    }

    public void setReservationMainstaffs(ArrayList<DataReservationMainstaff> reservationMainstaffs) {
        this.reservationMainstaffs = reservationMainstaffs;
    }
    // IVS_Thanh end add 2014/07/07 GB_Mashu_業態主担当設定

    //施術時間の変更 start
    public String getOpeMinute() {
        return opeMinute;
    }

    public void setOpeMinute(String opeMinute) {
        this.opeMinute = opeMinute;
    }

    public String getOpeSecond() {
        return opeSecond;
    }

    public void setOpeSecond(String opeSecond) {
        this.opeSecond = opeSecond;
    }
    //施術時間の変更 end
    
    /**
     * 伝票ヘッダデータを取得する。
     *
     * @return 伝票ヘッダデータ
     */
    public DataSales getSales() {
        return hairSales;
    }

    /**
     * 伝票ヘッダデータをセットする。
     *
     * @param sales 伝票ヘッダデータ
     */
    public void setSales(DataSales sales) {
        this.hairSales = hairSales;
    }

    /**
     * 割引マスタを取得する。
     *
     * @return 割引マスタ
     */
    public MstDiscounts getDiscounts() {
        return discounts;
    }

    /**
     * 割引マスタをセットする。
     *
     * @param discounts 割引マスタ
     */
    public void setDiscounts(MstDiscounts discounts) {
        this.discounts = discounts;
    }

    /**
     * 割引マスタをセットする。
     *
     * @param discounts 割引マスタ
     */
    public void setReservationNo(Integer reservationNo) {
        this.reservationNo = reservationNo;
    }

    /**
     * 支払区分を取得する。
     *
     * @return 支払区分
     */
    public MstPaymentClasses getPaymentClasses() {
        return paymentClasses;
    }

    /**
     * 支払区分をセットする。
     *
     * @param paymentClasses 支払区分
     */
    public void setPaymentClasses(MstPaymentClasses paymentClasses) {
        this.paymentClasses = paymentClasses;
    }

    /**
     * 合計を取得する。
     *
     * @return 合計
     */
    public ArrayList<NameValue> getTotal() {
        return total;
    }

    /**
     * 合計を取得する。
     *
     * @param index インデックス
     * @return 合計
     */
    public NameValue getTotal(int index) {
        if (0 <= index && index < total.size()) {
            return total.get(index);
        }
        return null;
    }

    public Long getPaymentTotal() {
        return this.getSales().getPayment(0).getPaymentTotal();
    }

    /**
     * 売掛金を取得する。
     *
     * @return 売掛金
     */
    public DataPayment getBill() {
        return bill;
    }

    /**
     * 売掛金をセットする。
     *
     * @param bill 売掛金
     */
    public void setBill(DataPayment bill) {
        this.bill = bill;
    }

    /**
     * レスポンスデータを取得する。
     *
     * @return レスポンスデータ
     */
    public DataResponseEffect[] getResponseEffect() {
        return dre;
    }

    /**
     * レスポンスデータをセットする。
     *
     * @param dre レスポンスデータ
     */
    public void setResponseEffect(DataResponseEffect[] dre) {
        this.dre = dre;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(int subStatus) {
        this.subStatus = subStatus;
    }

    public void setVisitedMemo(String memo) {
        this.hairSales.setVisitedMemo(memo);
    }

    public void setNextVisitDate(java.util.Date d) {
        this.hairSales.setNextVisitDate(d);
    }

    public boolean isSkipSales() {
        return skipSales;
    }

    public void setSkipSales(boolean skipSales) {
        this.skipSales = skipSales;
    }

    public long getCunsumptionTotal() {
        return this.cunsumptionTotal;
    }

    public void setCunsumptionTotal(long cunsumptionTotal) {
        this.cunsumptionTotal = cunsumptionTotal;
    }

    /**
     * 初期化処理を行う。
     */
    public void init() {
        this.setAccountSetting(SystemInfo.getAccountSetting());

        try {
            ConnectionWrapper con = SystemInfo.getConnection();

            discounts.load_Use(con);
            paymentClasses.loadClasses(con);

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e.getCause());
        }

        hairSales = new DataSales(SystemInfo.getTypeID());
        hairSales.addPayment(new DataPayment());
        reservationNo = null;

        this.initTotal();

        // 按分リストを取得する
        this.getProportionallys();
    }

    /**
     * 按分リストを取得する
     */
    private void getProportionallys() {
        ConnectionWrapper con = SystemInfo.getConnection();
        proportionallys.clear();

        try {

            ResultSetWrapper rs = con.executeQuery(this.getProportionallyListSelectSQL());

            //IVS_LVTu start edit 2015/12/04 Bug #45068
            MstProportionally proportionallytemp = new MstProportionally();
            while (rs.next()) {
                DataProportionally drp = new DataProportionally();
                drp.setDataProportionallyID(rs.getInt("data_proportionally_id"));
                //MstProportionally proportionally = new MstProportionally();
                MstProportionally proportionally = (MstProportionally)proportionallytemp.clone();
                //IVS_LVTu end edit 2015/12/04 Bug #45068
                proportionally.setProportionallyID(rs.getInt("proportionally_id"));
                proportionally.setProportionallyName(rs.getString("proportionally_name"));
                proportionally.setProportionallyPoint(rs.getInt("proportionally_point"));
                drp.setProportionally(proportionally);
                MstTechnic mt = new MstTechnic();
                mt.setData(rs);
                drp.setTechnic(mt);
                drp.setRatio(rs.getInt("proportionally_ratio"));
                proportionallys.add(drp);
            }

        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    /**
     * 按分リスト取得SQLを取得する
     */
    private String getProportionallyListSelectSQL() {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      dp.data_proportionally_id");
        sql.append("     ,mp.proportionally_id");
        sql.append("     ,mp.proportionally_name");
        sql.append("     ,mp.proportionally_point");
        sql.append("     ,mt.*");
        sql.append("     ,dp.proportionally_ratio");
        sql.append(" from");
        sql.append("     data_proportionally dp");
        sql.append("         inner join mst_proportionally mp");
        sql.append("                 on mp.proportionally_id = dp.proportionally_id");
        sql.append("         inner join mst_technic mt");
        sql.append("                 on mt.technic_id = dp.technic_id");
        sql.append(" where");
        sql.append("     dp.delete_date is null");
        sql.append(" order by");
        sql.append("      dp.technic_id");
        sql.append("     ,mp.display_seq");
        sql.append("     ,mp.proportionally_id");

        return sql.toString();
    }

    /**
     * 合計の初期化処理を行う。
     */
    protected void initTotal() {
        total.clear();
        total.add(new NameValue("合計", 0l));
        total.add(new NameValue("明細割引", 0l));
        total.add(new NameValue("全体割引", 0l));
        total.add(new NameValue("請求金額", 0l));
        total.add(new NameValue("（内税）", 0l));
        total.add(new NameValue("お釣り", 0l));
    }

    /**
     * 伝票明細データを追加する。
     *
     * @param productDivision 伝票明細区分
     * @param product 伝票明細データ
     */
    public void addSalesDetail(Integer productDivision, Product product, MstStaff ms, boolean designated) {
        DataSalesDetail dsd = new DataSalesDetail(productDivision, product);
        dsd.setProductNum(1);
        dsd.setStaff(ms);
        dsd.setDesignated(designated);
        
        //IVS_LVTu start edit 2015/10/07 Bug #43006
        // 伝票明細区分が技術かつ、按分有の店舗の場合には按分項目を追加する
        if (productDivision == DataSalesDetail.PRODUCT_DIVISION_TECHNIC 
                && (SystemInfo.getCurrentShop().isProportionally() || (SystemInfo.getCurrentShop().getShopID() == 0 
                    && this.getShop().getShopID() != null && this.getShop().isProportionally()))) {
        //IVS_LVTu end edit 2015/10/07 Bug #43006    
            for (DataProportionally dp : proportionallys) {
                if (dp.getTechnic().getTechnicID().intValue() == product.getProductID().intValue()) {
                    DataSalesProportionally dsp = new DataSalesProportionally();
                    dsp.setDataSalesDetail(dsd);
                    dsp.setProportionally(dp);
//                                        dsp.setStaff(ms);
//					dsp.setDesignated( designated );
                    dsp.setPoint(dp.getProportionally().getProportionallyPoint());
                    dsp.setRate(dp.getRatio());
                    dsd.add(dsp);
                }
            }
        }
        hairSales.add(dsd);
    }

    /**
     * 伝票明細データを追加する。
     *
     * @param productDivision 伝票明細区分
     * @param product 伝票明細データ
     */
    public void addSalesDetail(Integer productDivision, Course course, MstStaff ms, boolean designated, String tmpContractNo) {
        DataSalesDetail dsd = new DataSalesDetail(productDivision, course);
        dsd.setProductNum(1);
        dsd.setStaff(ms);
        dsd.setDesignated(designated);
        dsd.setTmpContractNo(tmpContractNo);

//		// 伝票明細区分が技術かつ、按分有の店舗の場合には按分項目を追加する
//		if( productDivision == DataSalesDetail.PRODUCT_DIVISION_TECHNIC && SystemInfo.getCurrentShop().isProportionally() )
//		{
//			for( DataProportionally dp : proportionallys )
//			{
//				if( dp.getTechnic().getTechnicID().intValue() == product.getProductID().intValue() )
//				{
//					DataSalesProportionally dsp = new DataSalesProportionally();
//					dsp.setDataSalesDetail( dsd );
//					dsp.setProportionally( dp );
////                                        dsp.setStaff(ms);
////					dsp.setDesignated( designated );
//					dsp.setPoint( dp.getProportionally().getProportionallyPoint() );
//					dsp.setRate( dp.getRatio() );
//					dsd.add( dsp );
//				}
//			}
//		}
        hairSales.add(dsd);
    }

    /**
     * 伝票明細データを追加する。
     *
     * @param productDivision 伝票明細区分
     * @param product 伝票明細データ
     */
    public void addSalesDetail(Integer productDivision, ConsumptionCourse consumptionCourse, MstStaff ms, boolean designated, String tmpContractNo) {
        DataSalesDetail dsd = new DataSalesDetail(productDivision, consumptionCourse);
        dsd.setConsumptionNum(1d);
        dsd.setStaff(ms);
        dsd.setDesignated(designated);
        dsd.setTmpContractNo(tmpContractNo);

//		// 伝票明細区分が技術かつ、按分有の店舗の場合には按分項目を追加する
//		if( productDivision == DataSalesDetail.PRODUCT_DIVISION_TECHNIC && SystemInfo.getCurrentShop().isProportionally() )
//		{
//			for( DataProportionally dp : proportionallys )
//			{
//				if( dp.getTechnic().getTechnicID().intValue() == product.getProductID().intValue() )
//				{
//					DataSalesProportionally dsp = new DataSalesProportionally();
//					dsp.setDataSalesDetail( dsd );
//					dsp.setProportionally( dp );
////                                        dsp.setStaff(ms);
////					dsp.setDesignated( designated );
//					dsp.setPoint( dp.getProportionally().getProportionallyPoint() );
//					dsp.setRate( dp.getRatio() );
//					dsd.add( dsp );
//				}
//			}
//		}
        hairSales.add(dsd);
    }

    /**
     * 伝票明細データを追加する。
     *
     * @param productDivision 伝票明細区分
     * @param product 伝票明細データ
     */
    public int addSalesDetail(MstItem mi) {
        int index = getSalesDetailIndex(mi);

        if (index == -1) {
            Product product = new Product();
            product.setProductID(mi.getItemID());
            product.setProductName(mi.getItemName());
            //product.setPrice(mi.getSelectedPriceValue());
            product.setPrice(mi.getPrice());
            product.setDisplaySeq(mi.getDisplaySeq());

            DataSalesDetail dsd = new DataSalesDetail(2, product);
            dsd.setProductNum(1);
            hairSales.add(dsd);
        } else {
            hairSales.get(index).setProductNum(
                    hairSales.get(index).getProductNum() + 1);
        }

        return index;
    }

    public int getSalesDetailIndex(MstItem mi) {
        for (int i = 0; i < hairSales.size(); i++) {
            if (hairSales.get(i).getProductDivision().equals(2)
                    && hairSales.get(i).getProduct().getProductID().equals(mi.getItemID())) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 合計をセットする。
     */
    // vtbphuong start delete 20140605 Bug #24803
//    public void setTotal() {
//        
//        
//        long allDiscount = 0;
//        Integer discountDivision = hairSales.getDiscount().getDiscountDivision();
//        if (discountDivision != null) {
//            long valTmp = 0;
//            long taxTmp = 0;
//
//            for (DataSalesDetail dsd : hairSales) {
//                Integer productDivisionTemp = dsd.getProductDivision().intValue();
//                switch (productDivisionTemp) {
//                    case 1:
//                    case 2:
//                    case 3:
//                    case 4:
//                    case 5:
//                        if (discountDivision.equals(hairSales.DISCOUNT_DIVISION_ALL) || discountDivision.equals(dsd.getProductDivision())) {
//                            valTmp += this.getAccountSetting().getDisplayValue(dsd.getValue(), dsd.getDiscountValue(), this.getTaxRate());
//                            taxTmp += this.getAccountSetting().getTax(dsd.getValue(), dsd.getDiscountValue(), this.getTaxRate());
//                        }
//                    case 6:
//                }
//
//            }
//
//            if (this.getAccountSetting().getDisplayPriceType() == 1) {
//                valTmp += taxTmp;
//            }
//
//            hairSales.getDiscount().setProductValue(valTmp);
//            hairSales.getDiscount().setProductNum(1);
//
//            long discountValue = hairSales.getDiscount().getDiscountValue();
//            double discountRate = hairSales.getDiscount().getDiscountRate();
//
//            if (discountRate != 0) {
//                allDiscount = (long) (valTmp * discountRate);
//            } else if (discountValue != 0) {
//                allDiscount = discountValue;
//            }
//        }
//        this.getTotal(2).setValue(allDiscount);
//
//        long valueTotal = 0;
//        long taxTotal = 0;
//        long totalValueForTax = 0;
//        long totalDiscountForTax = 0;
//        this.cunsumptionTotal = 0;
//        for (DataSalesDetail dsd : hairSales) {
//            Integer productDivision = dsd.getProductDivision().intValue();
//            switch (productDivision) {
//                case 1:
//                case 2:
//                case 3:
//                case 4:
//                case 5:
//                case 8:
//                case 9:
//                    valueTotal += this.getAccountSetting().getDisplayValue(dsd.getValue(), dsd.getDiscountValue(), this.getTaxRate());
//                    //LUc start edit 20140512
////                    if (dsd.getValue() > 0) {
////                        totalValueForTax += dsd.getValue();
////                        totalDiscountForTax += dsd.getDiscountValue();
////                    }
//                    //Luc end edit 20140512
//                    // vtbphuong start change 20140603 Bug #24589
//                    totalValueForTax += dsd.getValue();
//                    totalDiscountForTax += dsd.getDiscountValue();
//                    // vtbphuong end change 20140603 Bug #24589
//                    this.cunsumptionTotal += dsd.getValueForConsumption();
//                case 6:
//                    break;
//            }
//        }
//        //商品合計
//        this.getTotal(0).setValue(valueTotal+hairSales.getDetailDiscountTotal());
//        //割引合計
//        this.getTotal(1).setValue(hairSales.getDetailDiscountTotal());
//       //-----------------------------------------
//        //全体割引
//        //-----------------------------------------
//        if(totalDiscountForTax>0){
//            taxTotal += this.getAccountSetting().getTax(totalValueForTax, totalDiscountForTax, this.getTaxRate());
//        }else {
//            //taxTotal += hairSales.getValueTotal()-valueTotal;
//             taxTotal += this.getAccountSetting().getTax(totalValueForTax, 0, this.getTaxRate());
//        }
//        //全体割引に含まれている消費税分を引く
//        taxTotal -= (this.getAccountSetting().getTax(this.getTotal(2).getValue(), 0l, this.getTaxRate()));
//        // vtbphuong start change 20140603 Bug #24589
////        if (taxTotal < 0) {
////            this.getTotal(4).setValue(0l);
////        } else {
////            this.getTotal(4).setValue(taxTotal);
////        }
//          this.getTotal(4).setValue(taxTotal);
//        // vtbphuong end change 20140603 Bug #24589
//        //請求金額
//        if (this.getAccountSetting().getDisplayPriceType() == 1) {
//            this.getTotal(3).setValue(valueTotal - (this.getTotal(2).getValue() - this.getAccountSetting().getTax(this.getTotal(2).getValue(), 0l, this.getTaxRate())));
//            this.getTotal(3).setValue(this.getTotal(3).getValue() + taxTotal);
//        } else {
//            this.getTotal(3).setValue(valueTotal - this.getTotal(2).getValue());
//        }
//
//        Long temp = this.getSales().getPayment(0).getPaymentTotal();
//
//        temp -= this.getTotal(3).getValue();
//
//        //お釣り
//        this.getTotal(5).setValue(temp);
//
//        //売掛金をセット
//        if (temp < 0) {
//            hairSales.getPayment(0).setChangeValue(0l);
//            hairSales.getPayment(0).setBillValue(-temp);
//        } else {
//            hairSales.getPayment(0).setChangeValue(temp);
//            hairSales.getPayment(0).setBillValue(0l);
//        }
//    }
    
    
    
        public void setTotal()
	{
		//商品合計
                // vtbphuong start change 20140606 Bug #24803
		//this.getTotal(0).setValue(this.taxFilter(hairSales.getValueTotal()));
                this.getTotal(0).setValue(this.taxFilter());
                // vtbphuong start change 20140606 Bug #24803
		//割引合計
		this.getTotal(1).setValue(hairSales.getDetailDiscountTotal());

                //-----------------------------------------
		//全体割引
                //-----------------------------------------
		long allDiscount = 0;
                Integer discountDivision = hairSales.getDiscount().getDiscountDivision();
                if (discountDivision != null) {
                    long valTmp = 0;
                    long taxTmp = 0;
                    
                    for(DataSalesDetail dsd : hairSales) {
                        Integer productDivisionTemp = dsd.getProductDivision().intValue();
                        switch(productDivisionTemp){
                            case 1 : case 2 : case 3 : case 4 :
                            case 5 :
                                if (discountDivision.equals(hairSales.DISCOUNT_DIVISION_ALL) || discountDivision.equals(dsd.getProductDivision())) {
                                //Luc start edit 20150527 ticket 37040
                                //valTmp += this.getAccountSetting().getDisplayValue(dsd.getValue(), dsd.getDiscountValue(), this.getTaxRate());
                                valTmp += this.getAccountSetting().getDisplayValue(dsd.getValue(),dsd.getProductNum().longValue(), dsd.getDiscountValue(), this.getTaxRate());
                                //Luc end edit 20150527 ticket 37040
                                taxTmp += this.getAccountSetting().getTax(dsd.getValue(), dsd.getDiscountValue(), this.getTaxRate());
                            }
                            case 6 :
                        }
                    }

                    if(this.getAccountSetting().getDisplayPriceType() == 1) {
                        valTmp += taxTmp;
                    }

                    hairSales.getDiscount().setProductValue(valTmp);
                    hairSales.getDiscount().setProductNum(1);

                    long discountValue = hairSales.getDiscount().getDiscountValue();
                    double discountRate = hairSales.getDiscount().getDiscountRate();
                    //IVS_LVTu start edit 2015/07/10 Bug #40178
//                    if (discountRate != 0) {                       
//                        allDiscount = this.getAccountSetting().round(valTmp * discountRate);
//                    } else 
                    if (discountValue != 0) {
                        allDiscount = discountValue;
                    }
                    //IVS_LVTu end edit 2015/07/10 Bug #40178
                }
                this.getTotal(2).setValue(allDiscount);

		long valueTotal = 0;
		long taxTotal = 0;
                this.cunsumptionTotal = 0;
                for(DataSalesDetail dsd : hairSales) {
                    Integer productDivision = dsd.getProductDivision().intValue();
                    switch(productDivision){
                        case 1 : case 2 : case 3 : case 4 :
                        case 5: case 8: case 9:
                            //Luc start edit 20150527 ticket 37040
                            //valueTotal += this.getAccountSetting().getDisplayValue(dsd.getValue(), dsd.getDiscountValue(), this.getTaxRate());
                            valueTotal += this.getAccountSetting().getDisplayValue(dsd.getValue(),dsd.getProductNum().longValue(), dsd.getDiscountValue(), this.getTaxRate());
                            //Luc end edit 20150527 ticket 37040
                            Long dis = 0L;
                            // vtbphuong start change 20150604 
                          //  taxTotal += this.getAccountSetting().getTax(dsd.getValue(),dsd.getDiscountValue(), this.getTaxRate());
                            long netPrice = this.getAccountSetting().getDisplayValue(dsd.getValue(),dsd.getProductNum().longValue(),0L, this.getTaxRate());
                              taxTotal += this.getAccountSetting().getTax(dsd.getValue(),dsd.getDiscountValue(), this.getTaxRate(),netPrice );
                              // vtbphuong end change 20150604 
                            this.cunsumptionTotal += dsd.getValueForConsumption();
                        case 6 :
                            break;
                    }
                }
               //全体割引に含まれている消費税分を引く
		taxTotal -= (this.getAccountSetting().getTax(this.getTotal(2).getValue(), 0l, this.getTaxRate()));
//                if (taxTotal < 0) {
//                    this.getTotal(4).setValue(0l);
//                } else {
//                    this.getTotal(4).setValue(taxTotal);
//                }
                  this.getTotal(4).setValue(taxTotal);
		//請求金額
		if(this.getAccountSetting().getDisplayPriceType() == 1) {
		    this.getTotal(3).setValue(valueTotal - (this.getTotal(2).getValue() - this.getAccountSetting().getTax( this.getTotal(2).getValue(), 0l, this.getTaxRate())) );
		    this.getTotal(3).setValue(this.getTotal(3).getValue() + taxTotal);
		} else {
		    this.getTotal(3).setValue(valueTotal - this.getTotal(2).getValue());
		}
		
		Long temp = this.getSales().getPayment(0).getPaymentTotal();

		temp -= this.getTotal(3).getValue();

                //お釣り
		this.getTotal(5).setValue(temp);

		//売掛金をセット
		if(temp < 0) {
		    hairSales.getPayment(0).setChangeValue(0l);
		    hairSales.getPayment(0).setBillValue(-temp);
		} else {
		    hairSales.getPayment(0).setChangeValue(temp);
		    hairSales.getPayment(0).setBillValue(0l);
		}
	}
    // vtbphuong end delete 20140605 Bug #24803

    /**
     * 売掛金を読み込む。
     *
     * @param customerID 顧客ＩＤ
     */
    public void loadBill(Integer customerID) {
        Long billValue = 0l;
        Integer slipNo = null;
        int count = 0;

        if (customerID != null) {
            try {
                ConnectionWrapper con = SystemInfo.getConnection();

                ResultSetWrapper rs = con.executeQuery(BillsList.getSelectSQL(customerID));

                while (rs.next()) {
                    slipNo = rs.getInt("slip_no");
                    billValue += rs.getLong("bill_value_rest");

                    count++;
                }
            } catch (SQLException e) {
                SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            }
        }

        if (count != 1) {
            slipNo = null;
        }

        this.getBill().setSlipNo(slipNo);
        this.getBill().setBillValue(billValue);
    }

    /**
     * レスポンスデータを読み込む
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void loadResponseData(ConnectionWrapper con) throws SQLException {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" SELECT");
        sql.append("      dre.slip_no");
        sql.append("     ,dre.shop_id");
        sql.append("     ,dre.data_response_date");
        sql.append("     ,0 as response_issue_id");
        sql.append("     ,0 as staff_id");
        sql.append("     ,'' as staff_no");
        sql.append("     ,'' as staff_name1");
        sql.append("     ,'' as staff_name2");
        sql.append("     ,current_timestamp as circulation_monthly_date");
        sql.append("     ,0 as circulation_number");
        sql.append("     ,current_timestamp as regist_date");
        sql.append("     ,mr.response_id");
        sql.append("     ,mr.response_name");
        sql.append("     ,mr.circulation_type");
        sql.append("     ,mr.display_seq");
        //start add 20130521 VTAN
        sql.append("     ,mr.response_no");
        //end add 20120521 VTAN
        sql.append(" FROM");
        sql.append("     data_response_effect as dre");
        sql.append("         inner join mst_response as mr");
        sql.append("                 on dre.response_id = mr.response_id");
        sql.append(" WHERE");
        sql.append("         dre.delete_date is null");
        sql.append("     AND dre.shop_id = " + SQLUtil.convertForSQL(hairSales.getShop().getShopID()));
        sql.append("     AND dre.slip_no = coalesce( " + SQLUtil.convertForSQL(hairSales.getSlipNo()) + ", 0 )");
        ResultSetWrapper rs = con.executeQuery(sql.toString());

        int index = 0;
        while (rs.next()) {
            this.dre[index].setData(rs);
            index++;
        }

        rs.close();
    }

    /**
     * 予約データの施術台を読み込む
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void loadReservationBedData(ConnectionWrapper con) throws SQLException {
        if (hairSales.getCustomer().getCustomerID() == null) {
            // 存在しない伝票No.が予約データに登録されていたら「0」で更新する
            con.executeUpdate(getResetReservationSlipNo());
            throw new SQLException();
        }

        ResultSetWrapper rs = con.executeQuery(this.getSelectReservationSQL());

        int first = 0;
        while (rs.next()) {
            if (first == 0) {
                //1行目の場合
                this.reservationNo = rs.getInt("reservation_no");
                hairSales.setReservationDatetime(rs.getTimestamp("reservation_datetime"));

                Integer flg = rs.getInt("mobile_flag");
                if (!rs.wasNull()) {
                    hairSales.setMobileFlag(flg);
                }
                flg = rs.getInt("preorder_flag");
                if (!rs.wasNull()) {
                    hairSales.setPreorderFlag(flg);
                }
                //施術追加対応 start
                if (SystemInfo.getDatabase().startsWith("pos_hair_nonail")) {
                    String opeTime = rs.getString("ope_time");

                    if (opeTime != null) {
                        String[] opeTimes = opeTime.split(":");
                        int minute = toInt(opeTimes[0]) * 60 + toInt(opeTimes[1]);
                        int second = toInt(opeTimes[2]);

                        this.setOpeMinute(Integer.toString(minute));
                        this.setOpeSecond(Integer.toString(second));
                    }
                }
                //施術追加対応 end
                first++;
            }
            MstBed bed = new MstBed();
            for (int detailCount = 0; detailCount < hairSales.size(); detailCount++) {
                Object courseFlg = rs.getObject("course_flg");
                if (hairSales.get(detailCount).getProductDivision().intValue() == 1
                        && courseFlg == null
                        && hairSales.get(detailCount).getProduct().getProductID().intValue() == rs.getInt("technic_id")) {
                    bed.setBedID(rs.getInt("bed_id"));
                    bed.setBedName(rs.getString("bed_name"));
                    hairSales.get(detailCount).setBed(bed);
                    break;
                } else if (hairSales.get(detailCount).getProductDivision().intValue() == 5
                        && courseFlg != null
                        && (Integer) courseFlg == 1
                        && hairSales.get(detailCount).getCourse().getCourseId().intValue() == rs.getInt("technic_id")) {
                    bed.setBedID(rs.getInt("bed_id"));
                    bed.setBedName(rs.getString("bed_name"));
                    hairSales.get(detailCount).setBed(bed);
                    break;
                } else if (hairSales.get(detailCount).getProductDivision().intValue() == 6
                        && courseFlg != null
                        && (Integer) courseFlg == 2
                        && hairSales.get(detailCount).getConsumptionCourse().getCourseId().intValue() == rs.getInt("technic_id")) {
                    bed.setBedID(rs.getInt("bed_id"));
                    bed.setBedName(rs.getString("bed_name"));
                    hairSales.get(detailCount).setBed(bed);
                    break;
                }

//                                if((hairSales.get(detailCount).getProductDivision().intValue() == 1
//                                    || hairSales.get(detailCount).getProductDivision().intValue() == 5
//                                    || hairSales.get(detailCount).getProductDivision().intValue() == 6)
//                                && hairSales.get(detailCount).getProduct().getProductID().intValue() == rs.getInt("technic_id"))
//                                {
//                                    bed.setBedID(rs.getInt("bed_id"));
//                                    bed.setBedName(rs.getString("bed_name"));
//				    hairSales.get(detailCount).setBed(bed);
//                                    break;
//                                }
            }
//                        hairSales.get(detailCount).setBed(bed);
        }

        rs.close();
    }
    private int toInt(String val) {
        int intVal = 0;
        try {
            intVal = Integer.parseInt(val);        
        } catch(NumberFormatException ne) {
            intVal = 0;
        }
        return intVal;
    }
    /**
     * 伝票データを読み込む。
     *
     * @param slipNo 伝票No.
     */
    public void load(MstShop shop, Integer slipNo) {
        hairSales.clear();
        hairSales.setShop(shop);
        hairSales.setSlipNo(slipNo);
        //nhanvt start add 20150326 Bug #35729
        hairSales.setAccountSetting(this.getMsSetting());
        //nhanvt end add 20150326 Bug #35729

        try {
            //コネクションを取得
            ConnectionWrapper con = SystemInfo.getConnection();

            hairSales.loadAll(con);
            
            // IVS_Thanh start add 2014/07/11 Mashu_お会計表示
            hairSales.loadDataSaleMainStaff(con);
            // IVS_Thanh start add 2014/07/11 Mashu_お会計表示        

            //予約データの施術台を取得
            loadReservationBedData(con);

            // レスポンスを取得
            loadResponseData(con);
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        this.optimizePayment(0);
    }

    public void loadReservation(Integer shopID, Integer reservationNo) {
        hairSales.clear();
        hairSales.getShop().setShopID(shopID);
        this.reservationNo = reservationNo;

        try {
            //コネクションを取得
            ConnectionWrapper con = SystemInfo.getConnection();

            hairSales.loadReservation(con, reservationNo);
            // IVS_Thanh start add 2014/07/11 Mashu_お会計表示'
            hairSales.loadDataReservaionMainStaff(con, reservationNo);
            // IVS_Thanh start add 2014/07/11 Mashu_お会計表示
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    /**
     * 読み込んだ支払データを最適化する。
     *
     * @param index インデックス
     */
    protected void optimizePayment(int index) {
        ArrayList<DataPaymentDetail> temp = new ArrayList<DataPaymentDetail>();

        //
        for (MstPaymentClass mpc : paymentClasses) {
            boolean isFind = false;

            for (int i = 0; i < hairSales.getPayment(index).size(); i++) {
                DataPaymentDetail dpd = hairSales.getPayment(index).get(i);
                if (dpd.getPaymentMethod() != null
                        && mpc.getPaymentClassID() == dpd.getPaymentMethod().getPaymentClassID()) {
                    temp.add(dpd);
                    hairSales.getPayment(index).remove(i);
                    isFind = true;
                    break;
                }
            }

            if (!isFind) {
                temp.add(new DataPaymentDetail());
            }
        }

        //支払データを追加
        for (DataPaymentDetail dpd1 : hairSales.getPayment(index)) {
            temp.add(dpd1);
        }

        temp.add(new DataPaymentDetail());

        hairSales.getPayment(index).clear();

        //支払明細データを追加
        for (DataPaymentDetail dpd1 : temp) {
            hairSales.getPayment(index).add(dpd1);
        }
    }

    public boolean regist() {
        return regist(SystemInfo.getConnection());
    }

    /**
     * 登録処理を行う。
     *
     * @return true - 成功
     */
    public boolean regist(ConnectionWrapper con) {
        boolean result = false;

        //hairSales.setShop(SystemInfo.getCurrentShop());
        hairSales.getPayment(0).setPaymentDate(hairSales.getSalesDate());
        //nhanvt start add 20150326 Bug #35729
        hairSales.setAccountSetting(this.getMsSetting());
        //nhanvt end add 20150326 Bug #35729
        try {
            //トランザクション開始
            con.begin();

            //Start Add 20131004 lvut 
            DataSales hairSalesClone = (DataSales) this.getSales().clone();
            if (hairSalesClone.getSlipNo() == null) {
                hairSalesClone.setSlipNo(0);
            }
            //End Add 20131004 lvut

            //顧客情報を登録
            if (hairSales.getCustomer().getCustomerNo().equals("0")&&hairSales.getCustomer().getCustomerID()== null) {
                if (!hairSales.getCustomer().regist(con)) {
                    con.rollback();
                    return false;
                }

                if (hairSales.getCustomer().getCustomerID() == null) {
                    con.rollback();
                    return false;
                }
            }

            boolean isReservationSlipNo = false;
            if (hairSales.getSlipNo() == null) {
                hairSales.setNewAccount(true);
                hairSales.setNewSlipNo(con);

                // 存在しない伝票No.が予約データに登録されていたら「0」で更新する
                con.executeUpdate(getResetReservationSlipNo());

                // 予約データの伝票No.存在チェック
                StringBuilder sql = new StringBuilder(1000);
                sql.append(" select");
                sql.append("     count(*) as cnt");
                sql.append(" from");
                sql.append("     data_reservation");
                sql.append(" where");
                sql.append("         delete_date is null");
                sql.append("     and shop_id = " + SQLUtil.convertForSQL(hairSales.getShop().getShopID()));
                sql.append("     and slip_no = " + SQLUtil.convertForSQL(hairSales.getSlipNo()));
                ResultSetWrapper rs = con.executeQuery(sql.toString());
                if (rs.next()) {
                    if (rs.getInt("cnt") > 0) {
                        isReservationSlipNo = true;
                    }
                }
                rs.close();
            }

            // レスポンス項目を登録する
            if (DataResponseEffect.deleteAll(con, hairSales.getShop().getShopID(), hairSales.getSlipNo())) {
                for (int i = 0; i < dre.length; i++) {
                    dre[i].setSlipNo(hairSales.getSlipNo());
                    dre[i].setShopID(hairSales.getShop().getShopID());

                    if (!dre[i].regist(con)) {
                        con.rollback();
                        return false;
                    }
                }
            } else {
                con.rollback();
                return false;
            }
            // Phuong start add 2014/07/10
            hairSales.get(0).setTaxRate(taxRate);
            // Phuong end add 2014/07/10
            //データを登録
            if (hairSales.registAll(con, 0) && this.updateReservationStatus(con) && !isReservationSlipNo) {
                if (!hairSales.isNewAccount()) {
                    //コース契約を削除
                    if (!hairSales.deleteDataContract(con)) {
                        con.rollback();
                        return false;
                    }

                    //コース消化を削除
                    if (!hairSales.deleteDataContractDigestion(con)) {
                        con.rollback();
                        return false;
                    }
                } 
                
                //コース契約履歴登録
                if(!hairSales.registCourseContract(con)){
                    con.rollback();
                    return false;
                }
                //コース消化履歴登録
                if(!hairSales.registConsumptionCourse(con)){
                    con.rollback();
                    return false;
                }

                //予約データの登録
                //コース契約・消化コースの予約は上記メソッド「updateReservationStatus」では破綻しているためここで再度登録しなおす
                this.updateReservationStatusWithCourseContractAndContractDigestion(con);

                //トランザクションコミット
                con.commit();
                result = true;

                hairSales.setNewAccount(false);
            } else {
                //トランザクションロールバック
                
                con.rollback();
                return false;
            }

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        if (hairSales.isNewAccount() && !result) {

            MessageDialog.showMessageDialog(
                    null,
                    "データベースの接続に失敗しました。\n再度、「一時保存」または「精算ボタン」から登録してください。",
                    "",
                    JOptionPane.ERROR_MESSAGE);

        }

        return result;
    }

    //Start Add 20131004 lvut
    private void writeLog(DataSales hairSalesClone, DataSales hairSales, StringBuilder message, boolean temp) {
        if (!temp) {
            message.append("Shop: ").append(hairSales.getShop().getShopName()).append("    boot Sales Temp \n");
        } else {
            message.append("Shop: ").append(hairSales.getShop().getShopName()).append("    boot Sales complete \n");
            message.append("Sales date: ").append(hairSales.getSalesDate()).append("\n");
        }
        message.append("Customer ID: ").append(hairSales.getCustomer().getCustomerID()).append("\n");
        message.append("Staff ID: ").append(hairSales.getStaff().getStaffID()).append("\n");
        if (hairSales.getSlipNo() == null) {
            message.append("Slip No :  0 \n");
        } else {
            message.append("Slip No:  ").append(hairSales.getSlipNo()).append("\n");
        }
        for (int i = 0; i < hairSales.size(); i++) {
            message.append("Product ID: ").append(hairSales.get(i).getProduct().getProductID()).append("\n");
            if (hairSales.get(i).getProductDivision() == 5) {
                if (temp) {
                    message.append("Contract No: ").append(hairSales.get(i).getDataContract().getContractNo()).append("\n");
                    message.append("Contract detail No: ").append(hairSales.get(i).getDataContract().getContractDetailNo()).append("\n");
                }
            }
            if (hairSales.get(i).getProductDivision() == 6) {
                message.append("Contract Shop ID: ").append(hairSales.get(i).getContractShopId()).append("\n");
                message.append("Contract No : ").append(hairSales.get(i).getConsumptionCourse().getContractNo()).append("\n");
            }
            message.append("------------------\n");
        }

        if (!hairSalesClone.getShop().getShopID().equals(hairSales.getShop().getShopID())) {
            message.append("Shop ID:  ").append(hairSalesClone.getShop().getShopID())
                    .append("    Change --> Shop ID: ").append(hairSales.getShop().getShopID()).append("\n");
        }
        if (!hairSalesClone.getCustomer().getCustomerID().equals(hairSales.getCustomer().getCustomerID())) {
            message.append("Customer ID:  ").append(hairSalesClone.getCustomer().getCustomerID())
                    .append("    Change --> Customer ID: ").append(hairSales.getCustomer().getCustomerID()).append("\n");
        }
        if (!hairSalesClone.getStaff().getStaffID().equals(hairSales.getStaff().getStaffID())) {
            message.append("Staff ID:  ").append(hairSalesClone.getStaff().getStaffID())
                    .append("    Change --> Staff ID: ").append(hairSales.getStaff().getStaffID()).append("\n");
        }
        if (!hairSalesClone.getSlipNo().equals(hairSales.getSlipNo())) {
            message.append("Slip No:  ").append(hairSalesClone.getSlipNo())
                    .append("    Change --> Slip No: ").append(hairSales.getSlipNo()).append("\n");
        }
        if (hairSales.size() < hairSalesClone.size()) {
            message.append("Product change ");
        }
        for (int i = 0; i < hairSalesClone.size(); i++) {
            if (!hairSalesClone.get(i).getProduct().getProductID().equals(hairSales.get(i).getProduct().getProductID())) {
                message.append("productID: ").append(hairSalesClone.get(i).getProduct().getProductID())
                        .append("   change  --> productID:").append(hairSales.get(i).getProduct().getProductID()).append("\n");
            }
            if (hairSalesClone.get(i).getProductDivision() == 5) {
                if (temp) {
                    if (hairSalesClone.get(i).getDataContract() != null && !hairSalesClone.get(i).getDataContract().getContractNo().equals(hairSales.get(i).getDataContract().getContractNo())) {
                        message.append("Contract No: ").append(hairSalesClone.get(i).getDataContract().getContractNo())
                                .append("   change  --> Contract No:").append(hairSales.get(i).getDataContract().getContractNo()).append("\n");
                    }
                    if (hairSalesClone.get(i).getDataContract() != null && !hairSalesClone.get(i).getDataContract().getContractDetailNo().equals(hairSales.get(i).getDataContract().getContractDetailNo())) {
                        message.append("Contract detail No: ").append(hairSalesClone.get(i).getDataContract().getContractDetailNo())
                                .append("   change  --> Contract detail No:").append(hairSales.get(i).getDataContract().getContractDetailNo()).append("\n");
                    }
                }
            }
            if (hairSalesClone.get(i).getProductDivision() == 6) {
                if (!hairSalesClone.get(i).getContractShopId().equals(hairSales.get(i).getContractShopId())) {
                    message.append("Contract Shop ID: ").append(hairSalesClone.get(i).getContractShopId())
                            .append("   change -->  Contract Shop ID:").append(hairSales.get(i).getContractShopId()).append("\n");
                }
                if (!hairSalesClone.get(i).getConsumptionCourse().getContractNo().equals(hairSales.get(i).getConsumptionCourse().getContractNo())) {
                    message.append("Contract No : ").append(hairSalesClone.get(i).getConsumptionCourse().getContractNo())
                            .append("   change --> Contract No:").append(hairSales.get(i).getConsumptionCourse().getContractNo()).append("\n");
                }

            }

        }

        SystemInfo.getLogger().log(Level.INFO, message.toString());
    }

    //End Add 20131004 lvut
    public boolean registCourseContract(ConnectionWrapper con) {
        try {
            //コース契約履歴登録
            hairSales.registCourseContract(con);
        } catch (SQLException e) {
            return false;
        }

        return true;
    }

    public boolean registConsumptionCourse(ConnectionWrapper con) {
        try {
            //コース契約履歴登録
            hairSales.registConsumptionCourse(con);
        } catch (SQLException e) {
            return false;
        }

        return true;
    }

    public boolean deleteCourseContract(ConnectionWrapper con) {
        try {
            //コース契約履歴削除
            hairSales.deleteDataContract(con);
        } catch (SQLException e) {
            return false;
        }

        return true;
    }

    public boolean deleteConsumptionCourse(ConnectionWrapper con) {
        try {
            //コース消化履歴削除
            hairSales.deleteDataContractDigestion(con);
        } catch (SQLException e) {
            return false;
        }

        return true;
    }

    //施術時間の変更 start add
    /**
     * 予約データの施術開始時間・施術終了時間を施術時間から計算し更新する
     * 
     * @return true:正常終了 / false:異常終了
     */
    public boolean updateReservationTime() {
        if (reservationNo == null) {
            return true;
        }
        ConnectionWrapper con = SystemInfo.getConnection();
        try {
            con.begin();
            
            //予約ヘッダの読込
            DataReservation dr = new DataReservation();
            dr.setShop(this.getSales().getShop());
            dr.setReservationNo(reservationNo);
            dr.setOpeMinute(this.getOpeMinute());
            dr.setOpeSecond(this.getOpeSecond());
            dr.setStatus(this.getStatus());
            
            //予約データの時間更新
            if (!dr.updateTime(con)) {
                con.rollback();
                return false;
            }
            
            con.commit();
            
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }

        return true;
    }
    //施術時間の変更 start end
    
    private boolean updateReservationStatus(ConnectionWrapper con) {
        if (reservationNo == null) {
            return true;
        }

        try {
            //予約ヘッダの読込
            DataReservation dr = new DataReservation();
            dr.setShop(this.getSales().getShop());
            dr.setReservationNo(reservationNo);

            dr.setStatus(this.getStatus());

            //Status 2:一時保存　3:精算ボタン  SubStaus 1:施術待ち 2:施術中
            if (getStatus() == 2 && this.getSubStatus() != 2) {
                dr.setSubStatus(1);
            } else {
                dr.setSubStatus(2);
            }

            dr.setSlipNo(hairSales.getSlipNo());
            dr.setDesignated(hairSales.getDesignated());
            dr.setStaff(hairSales.getStaff());
            dr.setCustomer(hairSales.getCustomer());

            //予約明細・按分の読込み
            loadSalesToReservation(con, dr);

            //予約ヘッダの更新
            if (!dr.updateStatus(con)) {
                return false;
            }

            // スキップお会計の場合は予約明細を更新する
            if (isSkipSales()) {

                //施術時間の変更 start add
                if (SystemInfo.getDatabase().startsWith("pos_hair_nonail")) {
                    //3:精算ボタン押下時の処理
                    if (this.getStatus() == 3) {
                        dr.setOpeMinute(this.getOpeMinute());
                        dr.setOpeSecond(this.getOpeSecond());
                        if (!dr.updateTimeForSkipSales(con)) {
                            con.rollback();
                            return false;
                        }
                    }
                }
                //施術時間の変更 end add
                
                //予約明細の削除
                if (!dr.deleteDetail(con)) {
                    con.rollback();
                    return false;
                }
                // 按分の削除
                if (!dr.deleteProportionally(con)) {
                    con.rollback();
                    return false;
                }

                //予約明細・按分の登録
                for (DataReservationDetail drd : dr) {
                    drd.setReservation(dr);
                    if (!drd.regist(con)) {
                        con.rollback();
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }

        return true;
    }

    private boolean updateReservationStatusWithCourseContractAndContractDigestion(ConnectionWrapper con) {
        if (reservationNo == null) {
            return true;
        }

        try {
            //予約ヘッダの読込
            DataReservation dr = new DataReservation();
            dr.setShop(this.getSales().getShop());
            dr.setReservationNo(reservationNo);

            dr.setStatus(this.getStatus());

            //Status 2:一時保存　3:精算ボタン  SubStaus 1:施術待ち 2:施術中
            if (getStatus() == 2 && this.getSubStatus() != 2) {
                dr.setSubStatus(1);
            } else {
                dr.setSubStatus(2);
            }

            dr.setSlipNo(hairSales.getSlipNo());
            dr.setDesignated(hairSales.getDesignated());
            dr.setStaff(hairSales.getStaff());
            dr.setCustomer(hairSales.getCustomer());

            List<DataContractDigestion> dataContractDigestionList = DataContractDigestion.getDataContractDigestionList(con, this.getSales().getShop().getShopID(), hairSales.getSlipNo());

            GregorianCalendar datetime = new GregorianCalendar();
            datetime.setTime((Timestamp) hairSales.getReservationDatetime().clone());
            //お会計の明細での表示順と予約データの順番をあわながら予約登録する
            int dataContractDigestionCounter = 0;
            for (DataSalesDetail dsd : this.getSales()) {
                if (dsd.getProductDivision() != 1
                        && dsd.getProductDivision() != 5
                        && dsd.getProductDivision() != 6) {
                    continue;
                }

                DataReservationDetail drd = new DataReservationDetail();
                drd.setReservation(dr);
                drd.setReservationDatetime((GregorianCalendar) datetime.clone());
                if (dsd.getProductDivision() == 1) {
                    datetime.add(Calendar.MINUTE, dsd.getProduct().getOperationTime());

                    MstTechnicClass technicClass = new MstTechnicClass();
                    technicClass.setTechnicClassID(dsd.getProduct().getProductClass().getProductClassID());
                    technicClass.setTechnicClassName(dsd.getProduct().getProductClass().getProductClassName());

                    MstTechnic technic = new MstTechnic();
                    technic.setTechnicClass(technicClass);
                    technic.setTechnicID(dsd.getProduct().getProductID());
                    technic.setTechnicNo(dsd.getProduct().getProductNo());
                    technic.setTechnicName(dsd.getProduct().getProductName());
                    technic.setOperationTime(dsd.getProduct().getOperationTime());
                    drd.setTechnic(technic);
                } else if (dsd.getProductDivision() == 5) {
                    datetime.add(Calendar.MINUTE, dsd.getCourse().getOperationTime());

                    CourseClass courseClass = new CourseClass();
                    courseClass.setCourseClassId(dsd.getCourse().getCourseClass().getCourseClassId());
                    courseClass.setCourseClassName(dsd.getCourse().getCourseClass().getCourseClassName());

                    Course course = new Course();
                    course.setCourseClass(courseClass);
                    course.setCourseId(dsd.getCourse().getCourseId());
                    course.setCourseName(dsd.getCourse().getCourseName());
                    course.setOperationTime(dsd.getCourse().getOperationTime());

                    drd.setCourse(course);
                    drd.setCourseFlg(1);
                } else if (dsd.getProductDivision() == 6) {
                    datetime.add(Calendar.MINUTE, dsd.getConsumptionCourse().getOperationTime());

                    ConsumptionCourseClass consumptionCourseClass = new ConsumptionCourseClass();
                    consumptionCourseClass.setCourseClassId(dsd.getConsumptionCourse().getConsumptionCourseClass().getCourseClassId());
                    consumptionCourseClass.setCourseClassName(dsd.getConsumptionCourse().getConsumptionCourseClass().getCourseClassName());

                    ConsumptionCourse consumptionCourse = new ConsumptionCourse();
                    consumptionCourse.setConsumptionCourseClass(consumptionCourseClass);
                    consumptionCourse.setCourseId(dsd.getConsumptionCourse().getCourseId());
                    consumptionCourse.setCourseName(dsd.getConsumptionCourse().getCourseName());
                    consumptionCourse.setOperationTime(dsd.getConsumptionCourse().getOperationTime());

                    drd.setConsumptionCourse(consumptionCourse);
                    drd.setCourseFlg(2);

                    DataContractDigestion dataContractDigestion = dataContractDigestionList.get(dataContractDigestionCounter);
                    drd.setContractNo(dataContractDigestion.getContractNo());
                    drd.getConsumptionCourse().setContractNo(dataContractDigestion.getContractNo());
                    drd.setContractDetailNo(dataContractDigestion.getContractDetailNo());
                    drd.getConsumptionCourse().setContractDetailNo(dataContractDigestion.getContractDetailNo());


                    dataContractDigestionCounter++;
                }
                drd.setDesignated(dsd.getDesignated());

                MstStaff staff = new MstStaff();
                staff.setStaffID(dsd.getStaff().getStaffID());
                staff.setStaffNo(dsd.getStaff().getStaffNo());
                staff.setStaffName(0, dsd.getStaff().getStaffName(0));
                staff.setStaffName(1, dsd.getStaff().getStaffName(1));
                drd.setStaff(staff);

                drd.setBed(dsd.getBed());

                // 按分データを取得する
                loadSalesProportionallyToReservation(con, drd, dsd.getSlipDetailNo());

                dr.add(drd);
            }


//                        //予約明細・按分の読込み
//                        loadSalesToReservation(con,dr);

//                        //予約ヘッダの更新
//                        if (!dr.updateStatus(con)) {
//                            return false;
//			}

            // スキップお会計の場合は予約明細を更新する
//                        if (isSkipSales()) {

            //予約明細の削除
//                            if (!dr.deleteDetail(con)) {
//                                con.rollback();
//                                return false;
//                            }
            // 按分の削除
//                            if ( !dr.deleteProportionally( con ) ) {
//                                con.rollback();
//                                return false;
//                            }

            //予約明細・按分の登録
//                            for (DataReservationDetail drd : dr) {
//                                drd.setReservation(dr);
//                                if (!drd.regist(con)) {
//                                    con.rollback();
//                                    return false;
//                                }
//                            }
//                        }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            return false;
        }

        return true;
    }

    private Long taxFilter(Long value) {
        Long result = value;
        double v_result = value;
        if (this.getAccountSetting().getDisplayPriceType() == 1) {
            //vtbphuong start change 20140415 discount * taxRate
            //result	-=	SystemInfo.getTax(result, this.getSales().getSalesDate());
            BigDecimal a = new BigDecimal(result / (1 + SystemInfo.getTaxRate(SystemInfo.getSystemDate())));
            a = a.setScale(3, RoundingMode.HALF_UP);
            v_result = a.doubleValue();
            result = ((Double) Math.ceil(v_result)).longValue();;
            //vtbphuong end change 20140415 discount * taxRate
        }

        return result;
    }
    
    private Long taxFilter() {
        Long result = hairSales.getValueTotal();
        Long temp = 0L;
        Long price = 0L;
        double v_result = 0;
        if (this.getAccountSetting().getDisplayPriceType() == 1) {
            for (DataSalesDetail dsd : hairSales) {
                if(dsd.getProductDivision()!=6) {
                    price = (dsd.getProductNum() != 0 ? dsd.getValue() / dsd.getProductNum():0) ;

                    BigDecimal basePrice = new BigDecimal(price / (1 + SystemInfo.getTaxRate(SystemInfo.getSystemDate())));
                    basePrice = basePrice.setScale(3, RoundingMode.HALF_UP);
                    v_result = Math.ceil(basePrice.doubleValue()) * dsd.getProductNum();
                    if (v_result > 0) {
                        temp += ((Double) Math.ceil(v_result)).longValue();;
                    } else {
                        temp += ((Double) Math.floor(v_result)).longValue();;
                    }
                }

            }
            result = temp;
        }
        return result;
    }

    /**
     * 伝票データを技術メニューのみ読み込み、予約データを作成する。
     *
     * @param con コネクション
     * @param dr 予約データ
     * @return true - 成功
     */
    public boolean loadSalesToReservation(ConnectionWrapper con, DataReservation dr) {

        try {
            ResultSetWrapper rs = con.executeQuery(this.getSalesOnlyTechnicSQL(dr));

            int i = 0;
            int detailCount = 0;
            GregorianCalendar datetime = new GregorianCalendar();

            while (rs.next()) {
                DataReservationDetail drd = new DataReservationDetail();

                drd.setReservation(dr);

                //１件目のデータの場合
                if (i++ == 0) {
                    datetime.setTime((Timestamp) hairSales.getReservationDatetime().clone());
                }
                drd.setReservationDatetime((GregorianCalendar) datetime.clone());
                datetime.add(Calendar.MINUTE, rs.getInt("operation_time"));

                drd.setDesignated(rs.getBoolean("designated_flag"));
                MstTechnicClass technicClass = new MstTechnicClass();
                technicClass.setTechnicClassID(rs.getInt("technic_class_id"));
                technicClass.setTechnicClassName(rs.getString("technic_class_name"));
                MstTechnic technic = new MstTechnic();
                technic.setTechnicClass(technicClass);
                technic.setTechnicID(rs.getInt("technic_id"));
                technic.setTechnicNo(rs.getString("technic_no"));
                technic.setTechnicName(rs.getString("technic_name"));
                technic.setOperationTime(rs.getInt("operation_time"));
                drd.setTechnic(technic);

                MstStaff staff = new MstStaff();
                staff.setStaffID(rs.getInt("staff_id"));
                staff.setStaffNo(rs.getString("staff_no"));
                staff.setStaffName(0, "staff_name1");
                staff.setStaffName(1, "staff_name2");
                drd.setStaff(staff);

                MstBed bed = new MstBed();
                for (; detailCount < hairSales.size(); detailCount++) {
                    if (hairSales.get(detailCount).getProductDivision().intValue() == 1
                            && hairSales.get(detailCount).getProduct().getProductID().equals(drd.getTechnic().getTechnicID())) {
                        bed.setBedID(hairSales.get(detailCount).getBed().getBedID());
                        bed.setBedName(hairSales.get(detailCount).getBed().getBedName());
                        break;
                    }
                }
                drd.setBed(bed);

                // 按分データを取得する
                loadSalesProportionallyToReservation(con, drd, rs.getInt("slip_detail_no"));

                dr.add(drd);
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return true;
    }

    /**
     * 伝票按分データを読み込み、予約按分データを作成する。
     *
     * @param con コネクション
     * @param drd 予約詳細データ
     * @param slipDetailNo 伝票詳細NO
     */
    private boolean loadSalesProportionallyToReservation(ConnectionWrapper con, DataReservationDetail drd, Integer slipDetailNo) {
        // 予約詳細データ内の按分データをクリアする
        drd.clear();

        try {
            ResultSetWrapper rs = con.executeQuery(
                    this.getSelectSalesProportionallySQL(drd, slipDetailNo));

            while (rs.next()) {
                DataReservationProportionally drp = new DataReservationProportionally();
                drp.setReservationDetail(drd);
                DataProportionally proportionally = new DataProportionally();
                proportionally.setDataProportionallyID(rs.getInt("data_proportionally_id"));
                proportionally.setTechnic(drd.getTechnic());
                //IVS_LVTu start edit 2015/10/01 New request #43038
                MstProportionally mp = new MstProportionally(this.getShop());
                //IVS_LVTu end edit 2015/10/01 New request #43038
                mp.setProportionallyID(rs.getInt("proportionally_id"));
                mp.setProportionallyName(rs.getString("proportionally_name"));
                mp.setProportionallyPoint(rs.getInt("proportionally_point"));
                mp.setDisplaySeq(rs.getInt("display_seq"));
                proportionally.setProportionally(mp);
                proportionally.setRatio(rs.getInt("proportionally_ratio"));
                drp.setProportionally(proportionally);
                drp.setDesignated(rs.getBoolean("designated_flag"));
                MstStaff staff = new MstStaff();
                staff.setStaffID(rs.getInt("staff_id"));
                staff.setStaffNo(rs.getString("staff_no"));
                staff.setStaffName(0, "staff_name1");
                staff.setStaffName(1, "staff_name2");
                drp.setStaff(staff);
                drd.add(drp);
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return true;
    }

    /**
     * 伝票データを技術メニューのみ読み込むＳＱＬ文を取得する。
     *
     * @param dr 予約データ
     * @return 伝票データを技術メニューのみ読み込むＳＱＬ文
     */
    public String getSalesOnlyTechnicSQL(DataReservation dr) {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select distinct");
        sql.append("      dsd.slip_detail_no");
        sql.append("     ,mt.technic_class_id");
        sql.append("     ,mtc.technic_class_name");
        sql.append("     ,dsd.product_id as technic_id");
        sql.append("     ,mt.technic_no");
        sql.append("     ,mt.technic_name");
        sql.append("     ,coalesce(drd.operation_time, mt.operation_time) as operation_time");
        sql.append("     ,dsd.designated_flag");
        sql.append("     ,dsd.staff_id");
        sql.append("     ,ms.staff_no");
        sql.append("     ,ms.staff_name1");
        sql.append("     ,ms.staff_name2");
        sql.append(" from");
        sql.append("     data_sales ds");
        sql.append("         left join data_sales_detail dsd");
        sql.append("                on dsd.shop_id = ds.shop_id");
        sql.append("               and dsd.slip_no = ds.slip_no");
        sql.append("               and dsd.product_division = 1");
        sql.append("               and dsd.delete_date is null");
        sql.append("         left join mst_technic mt");
        sql.append("                on mt.technic_id = dsd.product_id");
        sql.append("         left join mst_technic_class mtc");
        sql.append("                on mtc.technic_class_id = mt.technic_class_id");
        sql.append("         left join mst_staff ms");
        sql.append("                on ms.staff_id = dsd.staff_id");
        sql.append("         left join data_reservation dr");
        sql.append("                on ds.shop_id = dr.shop_id");
        sql.append("               and dr.reservation_no = " + SQLUtil.convertForSQL(dr.getReservationNo()));
        sql.append("         left join data_reservation_detail drd");
        sql.append("                on dr.shop_id = drd.shop_id");
        sql.append("               and dr.reservation_no = drd.reservation_no");
        sql.append("               and drd.technic_id = mt.technic_id");
        sql.append("               and drd.delete_date is null");
        sql.append(" where");
        sql.append("         ds.delete_date is null");
        sql.append("     and ds.shop_id = " + SQLUtil.convertForSQL(dr.getShop().getShopID()));
        sql.append("     and ds.slip_no = " + SQLUtil.convertForSQL(dr.getSlipNo()));
        sql.append(" order by");
        sql.append("     dsd.slip_detail_no");

        return sql.toString();
    }

    /**
     * 伝票按分を取得するＳＱＬ文を取得する。
     *
     * @param drd 予約明細データ
     * @param slipDetailNo 伝票詳細NO
     * @return 伝票按分を取得するＳＱＬ文
     */
    public String getSelectSalesProportionallySQL(DataReservationDetail drd, Integer slipDetailNo) {
        return "select\n"
                + "mp.*,\n"
                + "ms.staff_id,\n"
                + "ms.staff_no,\n"
                + "ms.staff_name1,\n"
                + "ms.staff_name2,\n"
                + "dp.*,\n"
                + "dsp.designated_flag,\n"
                + "dsp.point,\n"
                + "dsp.ratio\n"
                + "from\n"
                + "data_sales_proportionally as dsp\n"
                + "inner join data_proportionally as dp on\n"
                + "dp.data_proportionally_id = dsp.data_proportionally_id\n"
                + "inner join mst_proportionally as mp on\n"
                + "mp.proportionally_id = dp.proportionally_id\n"
                + "left outer join mst_staff as ms on\n"
                + "ms.staff_id = dsp.staff_id\n"
                + "where\n"
                + "dsp.delete_date is null\n"
                + "and dsp.shop_id = " + SQLUtil.convertForSQL(drd.getShop().getShopID()) + "\n"
                + "and dsp.slip_no = " + SQLUtil.convertForSQL(drd.getReservation().getSlipNo()) + "\n"
                + "and slip_detail_no = " + SQLUtil.convertForSQL(slipDetailNo) + "\n"
                + "order by\n"
                + "mp.display_seq, dsp.data_proportionally_id\n"
                + ";\n";
    }

    /**
     * 予約データを読み込むＳＱＬ文を取得する。
     *
     * @return 予約データを読み込むＳＱＬ文
     */
    private String getSelectReservationSQL() {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      dr.reservation_no");
        sql.append("     ,drd.technic_id");
        sql.append("     ,drd.reservation_datetime");
        sql.append("     ,drd.course_flg");
        sql.append("     ,drd.bed_id");
        sql.append("     ,mb.bed_name");
        sql.append("     ,dr.mobile_flag");
        sql.append("     ,dr.preorder_flag");
        //施術時間追加対応 start
        sql.append("     ,dr.leave_time - dr.start_time as ope_time");
        //施術時間追加対応 end
        sql.append(" from");
        sql.append("     data_reservation dr");
        sql.append("         inner join data_reservation_detail drd");
        sql.append("            on drd.shop_id = dr.shop_id ");
        sql.append("           and drd.reservation_no = dr.reservation_no");
        sql.append("           and drd.delete_date is null");
        sql.append("         left join mst_bed mb");
        sql.append("           on mb.shop_id = dr.shop_id");
        sql.append("          and mb.bed_id = drd.bed_id");
        sql.append(" where");
        sql.append("         dr.delete_date is null");
        sql.append("     and dr.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
        sql.append("     and dr.slip_no = " + hairSales.getSlipNo().toString());
        sql.append("     and dr.customer_id = " + hairSales.getCustomer().getCustomerID().toString());
        sql.append(" order by");
        sql.append("     drd.reservation_detail_no");
        return sql.toString();
    }

    private String getResetReservationSlipNo() {

        SystemInfo.getLogger().log(Level.WARNING, "getResetReservationSlipNo() ： 存在しない伝票No.が予約データに登録されていたら「0」で更新するクエリ発行");

        StringBuilder sql = new StringBuilder(1000);
        sql.append(" update data_reservation");
        sql.append(" set");
        sql.append("     slip_no = 0");
        sql.append(" where");
        sql.append("         delete_date is null");
        sql.append("     and shop_id = " + SQLUtil.convertForSQL(hairSales.getShop().getShopID()));
        sql.append("     and slip_no = " + SQLUtil.convertForSQL(hairSales.getSlipNo()));
        sql.append("     and not exists");
        sql.append("         (");
        sql.append("             select 1 from data_sales");
        sql.append("             where");
        sql.append("                     shop_id = data_reservation.shop_id");
        sql.append("                 and slip_no = data_reservation.slip_no");
        sql.append("         )");

        return sql.toString();
    }
}
