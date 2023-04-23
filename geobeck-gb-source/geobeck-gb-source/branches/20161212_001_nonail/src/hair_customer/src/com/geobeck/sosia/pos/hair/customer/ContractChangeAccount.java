/*
 * HairInputAccount.java
 *
 * Created on 2006/05/29, 11:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.customer;

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
import com.geobeck.sosia.pos.master.product.MstItem;
import java.util.*;
import java.util.logging.*;
import java.sql.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.account.*;
import com.geobeck.sosia.pos.hair.data.course.DataContractDigestion;
import com.geobeck.sosia.pos.hair.master.company.MstBed;
import com.geobeck.sosia.pos.master.customer.MstCustomer;
import com.geobeck.swing.MessageDialog;
import javax.swing.JOptionPane;

/**
 * 伝票入力処理（リラクゼーション用）
 *
 * @author katagiri
 */
public class ContractChangeAccount {

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
    /**
     * 伝票ヘッダデータ
     */
    protected DataSales hairSales = new DataSales(SystemInfo.getTypeID());
    protected Integer reservationNo = null;
    private Double taxRate = 0d;
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

    /**
     * Creates a new instance of HairInputAccount
     */
    public ContractChangeAccount() {
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
     * 合計をセットする。
     */
    public void setTotal() {
        //商品合計
        this.getTotal(0).setValue(this.taxFilter(hairSales.getValueTotal()));
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
            
            for (DataSalesDetail dsd : hairSales) {
                if (discountDivision.equals(hairSales.DISCOUNT_DIVISION_ALL) || discountDivision.equals(dsd.getProductDivision())) {
                    valTmp += this.getAccountSetting().getDisplayValue(dsd.getValue(), dsd.getDiscountValue(), this.getTaxRate());
                    taxTmp += this.getAccountSetting().getTax(dsd.getValue(), dsd.getDiscountValue(), this.getTaxRate());
                }
            }
            
            if (this.getAccountSetting().getDisplayPriceType() == 1) {
                valTmp += taxTmp;
            }
            
            hairSales.getDiscount().setProductValue(valTmp);
            hairSales.getDiscount().setProductNum(1);
            
            long discountValue = hairSales.getDiscount().getDiscountValue();
            double discountRate = hairSales.getDiscount().getDiscountRate();
            
            if (discountRate != 0) {
                allDiscount = (long) (valTmp * discountRate);
            } else if (discountValue != 0) {
                allDiscount = discountValue;
            }
        }
        this.getTotal(2).setValue(allDiscount);
        
        long valueTotal = 0;
        long taxTotal = 0;
        this.cunsumptionTotal = 0;
        for (DataSalesDetail dsd : hairSales) {
            valueTotal += this.getAccountSetting().getDisplayValue(dsd.getValue(), dsd.getDiscountValue(), this.getTaxRate());
            if (dsd.getProductDivision() != 6) {
                taxTotal += this.getAccountSetting().getTax(dsd.getValue(), dsd.getDiscountValue(), this.getTaxRate());
                taxTotal += this.getAccountSetting().getTax(dsd.getValueForConsumption(), dsd.getDiscountValue(), this.getTaxRate());
                this.cunsumptionTotal += dsd.getValueForConsumption();
            }
        }

        //全体割引に含まれている消費税分を引く
        taxTotal -= (this.getAccountSetting().getTax(this.getTotal(2).getValue(), 0l, this.getTaxRate()));
        if (taxTotal < 0) {
            this.getTotal(4).setValue(0l);
        } else {
            this.getTotal(4).setValue(taxTotal);
        }
        //請求金額
        if (this.getAccountSetting().getDisplayPriceType() == 1) {
            this.getTotal(3).setValue(valueTotal - (this.getTotal(2).getValue() - this.getAccountSetting().getTax(this.getTotal(2).getValue(), 0l, this.getTaxRate())));
            this.getTotal(3).setValue(this.getTotal(3).getValue() + taxTotal);
        } else {
            this.getTotal(3).setValue(valueTotal - this.getTotal(2).getValue());
        }
        
        Long temp = this.getSales().getPayment(0).getPaymentTotal();
        
        temp -= this.getTotal(3).getValue();

        //お釣り
        this.getTotal(5).setValue(temp);

        //売掛金をセット
        if (temp < 0) {
            hairSales.getPayment(0).setChangeValue(0l);
            hairSales.getPayment(0).setBillValue(-temp);
        } else {
            hairSales.getPayment(0).setChangeValue(temp);
            hairSales.getPayment(0).setBillValue(0l);
        }
    }

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
     * 伝票データを読み込む。
     *
     * @param slipNo 伝票No.
     */
    public void cancelCourse() throws SQLException {
        ConnectionWrapper con = SystemInfo.getConnection();
        try {
            
            con.begin();
            //売上明細テーブルにデーだを登録する。
            this.getSales().setNewSlipNo(con);
            this.getSales().regist(con);
            for (DataSalesDetail dsd : this.getSales()) {
                dsd.setSlipNo(this.getSales().getSlipNo());
                dsd.setNewSlipDetailNo(con);
                
                dsd.regist(con);
            }
            // 支払テーブルにデータを登録する。
            
            for (DataPayment dp : this.getSales().getPayments()) {
                if (dp.getChangeValue() > 0) {
                    dp.registAll(con);
                }
            }
            con.commit();
            con.close();
            
        } catch (SQLException e) {
            con.rollback();
        }
        
    }
    
    public void load(MstShop shop, Integer slipNo) {
        hairSales.clear();
        hairSales.setShop(shop);
        hairSales.setSlipNo(slipNo);
        
        try {
            //コネクションを取得
            ConnectionWrapper con = SystemInfo.getConnection();
            
            hairSales.loadAll(con);
            
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        
        this.optimizePayment(0);
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
    
    public boolean registContract(ConnectionWrapper con) {
        DataPayment dp = hairSales.getPayment(0);
        dp.setPaymentDate(this.getSales().getSalesDate());
        dp.setShop(this.getShop());
        dp.setSlipNo(hairSales.getSlipNo());
        dp.setTempFlag(hairSales.getTempFlag());
        try {
            if (!dp.registAll(con)) {
                return false;
            }
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return true;
        
    }
    
    public boolean registCourseContract(ConnectionWrapper con) {
        try {
            //コース契約履歴登録
            hairSales.registCourseContract(con);
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
    
    private Long taxFilter(Long value) {
        Long result = value;
        
        if (this.getAccountSetting().getDisplayPriceType() == 1) {
            result -= SystemInfo.getTax(result, this.getSales().getSalesDate());
        }
        
        return result;
    }

    /**
     * 顧客コードに対応する顧客データの配列を取得する。
     *
     * @param no 顧客コード
     * @return 顧客コードに対応する顧客データの配列
     */
    public ArrayList<MstCustomer> getMstCustomerArrayByNo(
            ConnectionWrapper con,
            int cusID) throws SQLException {
        ArrayList<MstCustomer> result = new ArrayList<MstCustomer>();
        
        ResultSetWrapper rs = con.executeQuery(this.getMstCustomerByIDSQL(cusID));
        
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
    public String getMstCustomerByIDSQL(int CusID) {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("     *");
        sql.append(" from");
        sql.append("     mst_customer mc");
        sql.append(" where");
        sql.append("         mc.delete_date is null");
        //sql.append("     and mc.shop_id = " + SQLUtil.convertForSQL(shopID));

        sql.append(" and customer_id = '" + CusID + "'");
        
        
        sql.append(" order by");
        sql.append("     mc.customer_id");
        
        return sql.toString();
    }
}
